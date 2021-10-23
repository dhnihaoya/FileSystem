package File;

import Block.*;
import Utils.LoadUtil;
import Utils.PersistenceUtil;
import Exception.ErrCode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class File implements Interface.File, Serializable {

    //文件名就是数字 = =
    private final int id;
    private final FileManager fileManager;
    private List<LogicBlock> blocks;
    private BlockManager[] blockManagers;
    private FileBuffer buffer;
    private long size;
    //写的是下一位，比如初始值是-1就往buffer的[0]里面写,值是0就往buffer的1里面写
    private long cursorPos;
    //（写入/修改大小之后）开始修改的位置，要把之后的内容都改掉
    private long modifyBeginIndex;

    //新建的文件
    public File(int id, FileManager fileManager, BlockManager[] blockManagers) {
        this.id = id;
        this.fileManager = fileManager;
        this.blockManagers = blockManagers;
        this.blocks = new ArrayList<>();
        this.buffer = new FileBuffer(0);
        this.size = 0;
        this.cursorPos = 0;
        this.modifyBeginIndex = -1;
        saveMeta();
    }

    /**
     * 保存文件的meta，不会对内容做处理
     */
    private void saveMeta() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("managers", blockManagers);
        map.put("size", size);
        map.put("blocks", blocks);
        try {
            PersistenceUtil.writeFileMeta(fileManager.getId(), id, map);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrCode(ErrCode.IO_EXCEPTION);
        }
    }

    //读取已有的文件
    public File(FileManager fileManager, int fileId) {
        this.fileManager = fileManager;
        this.cursorPos = 0;
        this.modifyBeginIndex = -1;
        try {
            HashMap<String, Object> map =
                    LoadUtil.loadFileMeta(fileManager.getId(), fileId);
            this.id = (int) map.get("id");
            this.blockManagers = (BlockManager[]) map.get("managers");
            this.size = (long) map.get("size");
            this.blocks = (ArrayList<LogicBlock>) map.get("blocks");
            this.buffer = new FileBuffer((int) this.size);
            init(this.buffer, this.blocks);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErrCode(ErrCode.IO_EXCEPTION);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("类型解析出错了，可能是meta文件被意外修改了非值部分");
            throw new ErrCode(ErrCode.UNKNOWN);
        }
    }

    //读出文件内容到buffer
    private void init(FileBuffer buffer, List<LogicBlock> logicBlocks) {
        byte[] data;
        int count = 0;
        for (LogicBlock block : logicBlocks) {
            data = block.read();
            if (data != null) {
                buffer.copy(data, count * Block.CAPACITY, data.length);
            }
            count++;
        }
    }

    @Override
    public byte[] read(int length) {
        if( length <= 0 ){
            ErrCode.errCodeHandler(ErrCode.INVALID_ARG);
            return null;
        }
        if( cursorPos + length > size ){
            ErrCode.errCodeHandler(ErrCode.EOF);
            return null;
        }
        //从buffer读出数据并移动光标
        byte[] data = new byte[length];
        System.arraycopy(buffer.getContent(), (int)cursorPos,
                data, 0, length);
        move(length, MOVE_CURR);
        return data;
    }

    @Override
    public void write(byte[] content) {
        buffer = buffer.insert(content, (int)cursorPos);
        if( modifyBeginIndex == -1 ) {
            modifyBeginIndex = cursorPos;
        }
        if( cursorPos < modifyBeginIndex ){
            modifyBeginIndex = cursorPos;
        }
        size = content.length + size;
        move(content.length, MOVE_CURR);
    }

    /**
     * 移动文件的光标
     * 接口提供了三种移动方法，但是本次项目事实上只需要两种
     * @param offset 移动的位数
     * @param where 移动的开始位置（传入的是MOVE_CURR，MOVE_HEAD）
     * @return 如果正常就返回移动之后cursor的位置，如果出现问题则返回-1
     */
    @Override
    public long move(long offset, int where) {
        long newPos;
        switch (where){
            //从文件头开始移动offset位
            case MOVE_HEAD:
                newPos = offset;
                break;
            //从现在的光标位置开始移动offset位
            case MOVE_CURR:
                newPos = cursorPos + offset;
                break;
            default:
                newPos = -1;
                ErrCode.errCodeHandler(ErrCode.INVALID_ARG);
        }
        if( newPos <= size && newPos >=0 ){
            cursorPos = newPos;
        }
        else {
            ErrCode.errCodeHandler(ErrCode.EOF);
            System.out.println("本次光标移动无效哦");
        }
        return newPos;
    }

    /**
     * 关闭文件时将buffer的内容写回block
     * 从modifyBeginIndex开始修改
     */
    @Override
    public void close() {
        if( modifyBeginIndex != -1 ){
            int modifyBeginBlkIdx = (int)( modifyBeginIndex / Block.CAPACITY);
            int totalBlkCnt = (int)Math.ceil((double) size / Block.CAPACITY);

            if( blocks.size() > modifyBeginBlkIdx ){
                blocks.subList(modifyBeginBlkIdx, blocks.size()).clear();
            }
            long totalLength = size - (long) modifyBeginBlkIdx * Block.CAPACITY;

            int crtBlkSize;
            byte[] data;
            for(int i = modifyBeginBlkIdx; i < totalBlkCnt; i++){
                crtBlkSize = Math.min(Block.CAPACITY, (int)totalLength);
                data = new byte[crtBlkSize];
                System.arraycopy(buffer.getContent(), i * Block.CAPACITY,
                        data, 0, crtBlkSize);
                LogicBlock block = new LogicBlock(blockManagers, data);
                blocks.add(block);
                totalLength -= crtBlkSize;
            }
        }
        saveMeta();
    }


    @Override
    public void setSize(long newSize) {

        if(newSize == size){
            return;
        }

        if(newSize > size){
            if(modifyBeginIndex == -1){
                modifyBeginIndex = size;
            }
        }else {
            move(0, MOVE_HEAD);
            if( modifyBeginIndex == -1 ){
                modifyBeginIndex = (newSize / Block.CAPACITY ) * Block.CAPACITY;
            }
            else if(( newSize/ Block.CAPACITY * Block.CAPACITY < modifyBeginIndex )){
                modifyBeginIndex = newSize/ Block.CAPACITY * Block.CAPACITY;
            }
        }

        FileBuffer buffer = FileBuffer.trim(this.buffer, (int) newSize);
        this.size = newSize;
        this.buffer = buffer;

    }

    public void copy(HashMap<String, Object> meta){
        blockManagers = (BlockManager[]) meta.get("managers");
        size = (Long)meta.get("size");
        blocks = (ArrayList<LogicBlock>) meta.get("blocks");
        buffer = new FileBuffer((int) this.size);
        init( buffer, blocks);
        try {
            PersistenceUtil.writeFileMeta(fileManager.getId(), this.id, meta );
        }catch (IOException e) {
            e.printStackTrace();
            throw new ErrCode(ErrCode.IO_EXCEPTION);
        }
    }

    @Override
    public int getFileId() {
        return id;
    }

    @Override
    public FileManager getFileManager() {
        return fileManager;
    }

    @Override
    public long size() {
        return size;
    }

    public List<LogicBlock> getBlocks(){
        return this.blocks;
    }

    public long getCursorPos(){
        return cursorPos;
    }

}
