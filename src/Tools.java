import Block.Block;
import Block.LogicBlock;
import File.*;
import Utils.HashUtil;
import Exception.ErrCode;
import Utils.LoadUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Tools {

    public static void smartCat( File file ){
        file.move(0, File.MOVE_HEAD);
        System.out.println(new String(file.read((int) file.size())));
    }

    public static void smartHex( File file, int blkId ){
        List<LogicBlock> blocks = file.getBlocks();
         if(blkId >= blocks.size() ){
             System.out.println("(  ^ω^) block of this id does not exist!");
         }else {
             StringBuilder sb = HashUtil.byte2Hex(blocks.get(blkId).read());
             System.out.println(sb);
         }
    }

    public static void smartWrite(byte[] newContent, File file){
        file.write(newContent);
        file.close();
        System.out.println("写入成功，新的文件内容是：");
        smartCat(file);
    }

    public static void smartWrite(byte[] newContent, int cursor, File destFile){
        destFile.move(cursor, File.MOVE_HEAD);
        destFile.write(newContent);
        destFile.close();
        System.out.println("写入成功，新的文件内容是：");
        smartCat(destFile);
    }

    public static void smartCopy(FileManager destManager, File sourceFile,
                                 int destFileId){
        sourceFile.close();
        FileManager srcManager = sourceFile.getFileManager();
        File destFile = destManager.getFile(destFileId);
        if( destFile!= null ){
            ErrCode.errCodeHandler(ErrCode.FILE_ALREADY_EXIST);
            return;
        }
        try {
            HashMap<String, Object> srcMeta = LoadUtil.loadFileMeta(srcManager.getId(),
                    sourceFile.getFileId());
            destFile = destManager.newFile(destFileId);
            destFile.copy(srcMeta);
            sourceFile.move(0, File.MOVE_HEAD);
            System.out.println("Successfully copied file");
        }catch (IOException e){
            e.printStackTrace();
            throw new ErrCode(ErrCode.IO_EXCEPTION);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("似乎是file meta意外损坏了");
            throw new ErrCode(ErrCode.UNKNOWN);
        }
    }


    public static void foolishLs(FileManager[] fileManagers) {
        System.out.println("File system has " + fileManagers.length + " managers");
        List<File> files;
        for (FileManager crtFileManager : fileManagers) {
            files = crtFileManager.getFiles();
            System.out.println("This is FileManager number" + crtFileManager.getId()
                    + ", there are " + files.size() + " files: ");
            if (files.size() != 0) {
                System.out.println(" |");
            }
            int fileCnt = 1;
            for(File crtFile : files) {
                System.out.println(" ｜---" + fileCnt + ". file id is " + crtFile.getFileId()
                        + ", size is " + crtFile.size());
                fileCnt++;
            }

        }
    }

    public static void smartLs(FileManager[] fileManagers){
        System.out.println("File system has " + fileManagers.length + " managers");
        List<File> files;
        for(FileManager crtFileManager : fileManagers){
            files = crtFileManager.getFiles();
            System.out.println("This is FileManager number" + crtFileManager.getId()
                    + ", there are " + files.size() + " files: ");
            if(files.size() != 0) {
                System.out.println("|");
            }
            int fileCnt = 1;
            List<LogicBlock> logicBlocks;
            for(File crtFile : files){
                System.out.println(" ｜---" +fileCnt + ": file id is " + crtFile.getFileId()
                        + ", size is " + crtFile.size());
                logicBlocks = crtFile.getBlocks();
                if(logicBlocks.size() != 0){
                    int blkCnt = 0;
                    for(LogicBlock crtBlk : logicBlocks ){
                        System.out.println("    ｜---file logic blk number: "+ blkCnt);
                        System.out.println("        ｜");
                        for( Block psyBlk : crtBlk.getPhysicalBlocks() ){
                            System.out.println("         ｜---｜physical blk: " + psyBlk.getIndexId()
                                    + " in manger " + psyBlk.getBlockManager().getId());
                        }
                        blkCnt++;
                    }

                }
                fileCnt++;
            }
        }
    }
}
