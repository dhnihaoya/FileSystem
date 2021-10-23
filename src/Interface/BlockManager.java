package Interface;

public interface BlockManager {
    Block getBlock(int indexId);
    Block newBlock(byte[] b);
    Block newEmptyBlock(int blockSize);
    int getId();
}
