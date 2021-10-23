package Block;

import Utils.RandomUtil;

import java.io.Serializable;
import java.util.ArrayList;

import Exception.ErrCode;

/**
 * 逻辑块
 * 文件直接操作的对象
 */
public class LogicBlock implements Serializable {
    //每个逻辑块对应几个physical block
    public static final int BLOCK_CNT = 2;
    private final Block[] physicalBlocks;

    public LogicBlock( BlockManager[] managers, byte[] content ){
        this.physicalBlocks = new Block[BLOCK_CNT];
        ArrayList<Integer> randomIndexes = RandomUtil.randomShuffledNumbers(managers.length);

        BlockManager crtBlkManager;
        int crtIdx;
        for( int i = 0; i < BLOCK_CNT; i++ ){
            crtIdx = randomIndexes.get(i);
            crtBlkManager = managers[crtIdx];
            physicalBlocks[i] = crtBlkManager.newBlock(content);
        }
    }

    public byte[] read(){
        byte[] data;
        byte[] result = null;
        boolean damageOccurred = false;
        ArrayList<Block> damagedBlk = new ArrayList<>();

        for( Block crtBlk : physicalBlocks ){
            data = crtBlk.read();
            if( data != null ){
                result = data;
            }else {
                damageOccurred = true;
                damagedBlk.add(crtBlk);
            }
        }

        if(result == null){
            ErrCode.errCodeHandler(ErrCode.BLOCK_DAMAGED);
            System.out.println("抱歉。所有备份全部损坏，备份也救不了了");
            return null;
        }

        if(damageOccurred) {
            for (Block crtBlk : damagedBlk) {
                fix(crtBlk, result);
            }
            System.out.println("出现损坏，但通过备份修复了");
        }
        return result;
    }

    private void fix(Block damagedBlock, byte[] rightContent){
        BlockManager bm = damagedBlock.getBlockManager();
        int id = damagedBlock.getIndexId();
        damagedBlock = new Block(id, bm, rightContent);
    }

    public Block[] getPhysicalBlocks(){
        return physicalBlocks;
    }

}
