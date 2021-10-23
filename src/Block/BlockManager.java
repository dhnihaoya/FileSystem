package Block;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Exception.ErrCode;

public class BlockManager implements Interface.BlockManager, Serializable {
    private final int id;
    private final List<Block> blocks;

    public BlockManager(int id){
        this.id = id;
        this.blocks = new ArrayList<>();
        String rootPath = "src/meta-data/BlockManagers/bm" + id + "/data/";
        File blockManager = new File( rootPath );
        if(!blockManager.exists()){
            blockManager.mkdir();
        }
        File[] blockData = blockManager.listFiles();
        if( blockData != null ) {
            initManager(blockData);
        }
    }

    private void initManager(File[] blockData) {
        Block crtBlock;
        String crtId;
        for(File curBlockData : blockData){
            crtId = curBlockData.getName().substring(0,1);
            //避免系统生成的文件干扰
            if( !".".equals(crtId) ){
                crtBlock = new Block(Integer.parseInt(crtId), this);
                blocks.add( crtBlock );
            }
        }
    }

    @Override
    public Block getBlock(int indexId) {
        for(Block crtBlock: blocks){
            if( crtBlock.getIndexId() == indexId ) {
                return crtBlock;
            }
        }
        ErrCode.errCodeHandler(ErrCode.BLOCK_NOT_FOUND);
        System.out.println("请重新输入命令");
        return null;
    }

    @Override
    public Block newEmptyBlock(int blockSize){
        return newBlock(new byte[blockSize]);
    }

    //分配一个由给定内容生成的块
    @Override
    public Block newBlock(byte[] content) {
        int crtBlkId = blocks.size();
        Block newBlk =  new Block(crtBlkId, this, content);
        this.blocks.add(newBlk);
        return newBlk;
    }

    @Override
    public int getId() {
        return id;
    }
}
