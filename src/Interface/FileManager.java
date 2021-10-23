package Interface;

public interface FileManager {
    File getFile(int fileId) throws Exception;
    File newFile(int fileId) throws Exception;
}
