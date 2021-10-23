package Utils;

import java.io.*;
import java.util.HashMap;

public class PersistenceUtil {

    //for block
    private static void writeBlockData(int blockManagerId, int blockId, byte[] content) throws IOException {
        String dataPath = "src/meta-data/BlockManagers/bm" + blockManagerId + "/data/"
                + blockId + ".data";
        File file = new File(dataPath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, false));
        bos.write(content);
        bos.close();
    }

    private static void writeBlockMeta(int blockManagerId, int blockId, String blkMeta) throws IOException {
        String metaPath =  "src/meta-data/BlockManagers/bm" + blockManagerId + "/meta/"
                + blockId + ".meta";
        FileOutputStream fos = new FileOutputStream(metaPath);
        fos.write(blkMeta.getBytes());
        fos.close();
    }

    public static void saveBlock(int blockManagerId, int blockId, byte[] content) throws IOException {
        String blkMeta =  content.length + "\n" + HashUtil.hash(content);
        writeBlockData(blockManagerId, blockId, content);
        writeBlockMeta(blockManagerId, blockId, blkMeta);
    }

    //for file
    public static void writeFileMeta(int fileManagerId, int fileId, HashMap<String, Object> map) throws IOException{
        String metaPath =  "src/meta-data/FileManagers/fm" + fileManagerId
                + "/" + fileId;
        File file = new File(metaPath);
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        oos.writeObject( map );
        oos.close();
    }

}
