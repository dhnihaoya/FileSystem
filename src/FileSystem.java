import Block.BlockManager;
import java.io.File;
import File.FileManager;

public class FileSystem {

    private final FileManager[] fileManagers;

    public FileSystem(int fileManagerNum, int blockMangerNum){
        fileManagers = new FileManager[fileManagerNum];
        BlockManager[] blockManagers = new BlockManager[blockMangerNum];

        //建立bm的路径
        String dataPath;
        String metaPath;
        File blkMeta;
        File blkData;
        for(int i = 0; i < blockMangerNum; i++){
            blockManagers[i] = new BlockManager(i);

            dataPath = "src/meta-data/BlockManagers/bm" + i + "/data/";
            metaPath = "src/meta-data/BlockManagers/bm" + i + "/meta/";

            blkData = new File(dataPath);
            if(!blkData.exists()){
                blkData.mkdirs();
            }
            blkMeta = new File(metaPath);
            if(!blkMeta.exists()){
                blkMeta.mkdirs();
            }
        }

        //建立fm的路径
        String fileMetaPath;
        File fileMeta;
        for(int i = 0; i < fileManagerNum; i++){
            fileManagers[i] = new FileManager(i, blockManagers);

            fileMetaPath = "src/meta-data/FileManagers/fm" + i +"/";
            fileMeta = new File(fileMetaPath);

            if(!fileMeta.exists()){
                fileMeta.mkdirs();
            }
        }
    }

    public FileManager[] getFileManagers(){
        return this.fileManagers;
    }
}
