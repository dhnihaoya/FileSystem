package File;

import Block.BlockManager;
import Exception.ErrCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileManager implements Interface.FileManager, Serializable {

    private final int id;
    private final BlockManager[] blockManagers;
    private final List<File>  files;

    public FileManager(int id, BlockManager[] blockManagers) {
        this.id = id;
        this.blockManagers = blockManagers;
        files = new ArrayList<>();
        java.io.File fileManager =
                new java.io.File("src/meta-data/FileManagers/fm" + id);
        java.io.File[] fileMetas = fileManager.listFiles();
        if(fileMetas != null){
            File crtFile;
            for( java.io.File crtFileMeta : fileMetas ){
                if(crtFileMeta.getName().charAt(0) != '.'){
                    crtFile = new File(this, Integer.parseInt(crtFileMeta.getName()));
                    files.add(crtFile);
                }
            }
        }
    }

    //包装getFile，用于检查是否存在
    public boolean fileExists(int fieldId){
        return !(getFile(fieldId) == null);
    }

    @Override
    public File getFile(int fileId){
        for(File crtFile : files){
            if( crtFile.getFileId() == fileId ){
                return crtFile;
            }
        }
        return null;
    }

    @Override
    public File newFile(int fileId){
        if( getFile(fileId) != null ){
            ErrCode.errCodeHandler(ErrCode.FILE_ALREADY_EXIST);
            return null;
        }
        File newFile = new File( fileId, this, blockManagers);
        files.add(newFile);
        return newFile;
    }

    public int getId(){
        return this.id;
    }

    public List<File> getFiles(){
        return files;
    }

}
