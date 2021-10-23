package Interface;

public interface File {

    int MOVE_CURR = 0;
    int MOVE_HEAD = 1;
    int getFileId();
    FileManager getFileManager();
    byte[] read(int length);
    void write(byte[] b);
    default long pos() {
        return move(0, MOVE_CURR);
    }
    long move(long offset, int where);

    //buffer part
    void close();
    long size();
    void setSize(long newSize);

}
