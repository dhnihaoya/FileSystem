package Block;

import Utils.HashUtil;
import Utils.LoadUtil;
import Exception.ErrCode;
import Utils.PersistenceUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 类本身并不存储block内容，写在文件里
 */
public class Block implements Interface.Block, Serializable {
    public static final int CAPACITY = 4;
    private final int id;
    private final BlockManager blockManager;
    private final int size;

    /**
     * （重启等时）加载之前已经存在的块
     */
    public Block(int id, BlockManager blockManager) {
        this.id = id;
        this.blockManager = blockManager;
        try {
            this.size = LoadUtil.loadBlockSize(blockManager.getId(), id);
        }
        catch (IOException e) {
            throw new ErrCode(ErrCode.IO_EXCEPTION);
        }
    }

    /**
     * 写入新的块
     */
    public Block(int id, BlockManager blockManager, byte[] content) {
        this.id = id;
        this.blockManager = blockManager;
        try {
            this.size = content.length;
            PersistenceUtil.saveBlock(blockManager.getId(), id, content);
        }
        catch (IOException e) {
            throw new ErrCode(ErrCode.IO_EXCEPTION);
        }
    }

    /**
     * 读取block内容
     * @return 正常的话会返回block内容，
     *         如果block损坏或意外被修改或文件根本找不到则返回null
     */
    @Override
    public byte[] read() {
        try {
            HashMap<String, Object> map = LoadUtil.loadBlockMeta(blockManager.getId(), id);
            byte[] content = LoadUtil.loadBlockData(blockManager.getId(), id);
            String expectedHashVal = (String) map.get("hash");
            String actualHashVal = HashUtil.hash(content);

            int expectedLength = (Integer) map.get("size");
            int actualLength = content.length;

            //block 被损坏
            if((!expectedHashVal.equals(actualHashVal)) || (!(expectedLength==actualLength)) ){
                return null;
            }
            return content;
        }
        //整个文件就没了
        catch (FileNotFoundException e) {
            System.out.println("failed to find block" + id + "in manager" + blockManager.getId() );
            ErrCode.errCodeHandler(ErrCode.FILE_NOT_FOUND);
            return null;
        }
        catch (IOException e) {
            System.out.println("文件是找到了，但出现了别的IO问题_(:з」∠)_，只能退出了就是说");
            throw new ErrCode(ErrCode.IO_EXCEPTION);
        }
    }

    @Override
    public int getIndexId() {
        return id;
    }

    @Override
    public BlockManager getBlockManager() {
        return blockManager;
    }

    @Override
    public int blockSize() {
        return size;
    }



}
