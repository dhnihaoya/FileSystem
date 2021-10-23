package Utils;

import java.io.*;
import java.util.HashMap;

public class LoadUtil {

    public static HashMap<String, Object> loadMeta(String path) throws IOException{
        FileInputStream fis = new FileInputStream(path);

        byte[] content = new byte[fis.available()];
        fis.read(content);

        String[] val = new String(content).split("\n");
        HashMap<String, Object> map = new HashMap<>();
        map.put("hash", val[1]);
        map.put("size", Integer.parseInt(val[0]));

        return map;
    }

    //for block
    public static HashMap<String, Object> loadBlockMeta(int blockManagerId, int blockId) throws IOException{
        String path = "src/meta-data/BlockManagers/bm" + blockManagerId
                + "/meta/" + blockId + ".meta";
        return loadMeta(path);
    }

    public static int loadBlockSize(int blockManagerId, int blockId) throws IOException {
        String path = "src/meta-data/BlockManagers/bm" + blockManagerId
                + "/meta/" + blockId + ".meta";
        try {
            HashMap<String, Object> map = loadMeta(path);
            return (Integer)map.get("size");
        }catch (FileNotFoundException e){
               return -1;
        }

    }

    public static byte[] loadBlockData(int blockManagerId, int blockId) throws IOException {
        String path = "src/meta-data/BlockManagers/bm" + blockManagerId
                + "/data/" + blockId + ".data";
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream((path)));
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);
        inputStream.close();
        return data;
    }

    //for file
    public static HashMap<String, Object> loadFileMeta(int fileManagerId, int fileId) throws IOException, ClassNotFoundException {
        String path = "src/meta-data/FileManagers/fm" + fileManagerId
                + "/"  + fileId;
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream((path))));
        HashMap<String, Object> map = (HashMap<String, Object>) ois.readObject();
        ois.close();
        return map;
    }


}
