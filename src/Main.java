import File.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
       //System.exit(1);
        FileSystem fileSystem = new FileSystem(2, 3);
        System.out.println(" (  ^ω^)hello, this is file system");
        System.out.println(" (  ^ω^)print [help] for assistance");
        Tools.foolishLs(fileSystem.getFileManagers());

        Scanner input = new Scanner(System.in);
        String[] crtInstruct;
        File file = null;

        while(true){
            if( file != null ){
                System.out.println("(  ^ω^) you are now operating file: "
                        + file.getFileId()+" (FM id is " + file.getFileManager().getId() +")");
            }else {
                System.out.println("(  ^ω^)you're not operating any file");
            }
            System.out.print("(  ^ω^) enter cmd here >>>>");
            crtInstruct = input.nextLine().split(" ");
            file = doit(crtInstruct, fileSystem, file);
        }
    }

    //不想给面条写注释
    private static File doit(String[] instruction, FileSystem fileSystem, File crtOperatingFile){
        switch (instruction[0]){
            case "help":
                //normal function
                System.out.println("(  ^ω^) using guide");
                System.out.println("(  ^ω^) [create] [fileId] [managerId] creates a new empty file with given fileId");
                System.out.println("(  ^ω^) [get] [fileId] [managerId] gets your wanted file");
                System.out.println("(  ^ω^) [size] returns the size of a file (when operating a file)");
                System.out.println("(  ^ω^) [resize] [size] resizes the file");
                System.out.println("(  ^ω^) [move] [off] [where] moves the cursor, where 0 means current, 1 means from head");
                System.out.println("(  ^ω^) [close] closes the file and save it");
                //smart tools
                System.out.println("(  ^ω^) [smart] [tool type] [..args] calls smart tools,(  ^ω^) I am too lazy to introduce");
                System.out.println("(  ^ω^)");
                return crtOperatingFile;
            case "read":
                if( instruction.length != 2 && instruction.length != 1 ){
                    System.out.println("(  ^ω^) wrong input format! Recheck next time");
                    System.out.println("(  ^ω^) 2 or 1 arg(s) plz");
                    return crtOperatingFile;
                }
                if(instruction.length == 2 ) {
                    int len = Integer.parseInt(instruction[1]);
                    System.out.println(new String(crtOperatingFile.read(len)));
                }
                if(instruction.length == 1 ) {
                    int len = (int)(crtOperatingFile.size() - crtOperatingFile.pos());
                    System.out.println(new String(crtOperatingFile.read(len)));
                    crtOperatingFile.move(0, 1);
                }
                return crtOperatingFile;
            case "get":
                if( instruction.length != 3 ){
                    System.out.println("(  ^ω^) wrong input format! Recheck next time");
                    System.out.println("(  ^ω^) 3 args plz");
                    return crtOperatingFile;
                }
                int fileId = Integer.parseInt(instruction[1]);
                int managerId = Integer.parseInt(instruction[2]);
                if(managerId < fileSystem.getFileManagers().length && managerId > -1
                && fileId > 0){
                    FileManager fm = fileSystem.getFileManagers()[managerId];
                    crtOperatingFile = fm.getFile(fileId);
                    if( crtOperatingFile == null ){
                        System.out.println("(  ^ω^) file does not exist");
                        Tools.foolishLs(fileSystem.getFileManagers());
                        System.out.println("(  ^ω^) plz check");
                    }
                }
                else {
                    System.out.println("(  ^ω^) wrong input!");
                    System.out.println("(  ^ω^) manager doesn't exist or fileId is illegal");
                }
                return crtOperatingFile;
            case "create":
                if( instruction.length != 3 ) {
                    System.out.println("(  ^ω^) wrong input format! Recheck next time");
                    return crtOperatingFile;
                }
                int newFileId = Integer.parseInt(instruction[1]);
                int newManagerId = Integer.parseInt(instruction[2]);
                if(newManagerId < fileSystem.getFileManagers().length && newManagerId > -1
                        && newFileId > 0){
                    FileManager fm = fileSystem.getFileManagers()[newManagerId];
                    crtOperatingFile = fm.getFile(newFileId);
                    if( crtOperatingFile != null  ){
                        System.out.println("(  ^ω^) file of same name exists");
                        return crtOperatingFile;
                    }
                    System.out.println("(  ^ω^) create succeeded");
                    return fm.newFile(newFileId);
                }
                else {
                    System.out.println("(  ^ω^) wrong input format!Recheck next time");
                    return crtOperatingFile;
                }
            case "size":
                if(crtOperatingFile != null){
                    System.out.println("(  ^ω^) file size is" + crtOperatingFile.size());
                }
                else {
                    System.out.println("(  ^ω^) get me a file first, please");
                }
            case "resize":
                if( instruction.length != 2 ){
                    System.out.println("(  ^ω^) wrong input format! Recheck next time");
                    return crtOperatingFile;
                }
                int newSize = Integer.parseInt(instruction[1]);
                if (newSize < 0){
                    System.out.println("(  ^ω^) wrong input format! Recheck next time");
                    return crtOperatingFile;
                }
                if(crtOperatingFile == null){
                    System.out.println("(  ^ω^) get me a file first, please");
                }
                else {
                    crtOperatingFile.setSize(newSize);
                    System.out.println("(  ^ω^) reset succeeded");
                }
                return crtOperatingFile;
            case "move" :
                if( instruction.length != 3){
                    System.out.println("(  ^ω^) wrong input format! Recheck next time");
                    return crtOperatingFile;
                }
                int offset = Integer.parseInt(instruction[1]);
                int moveType = Integer.parseInt(instruction[2]);
                if( moveType != 0 && moveType != 1){
                    System.out.println("(  ^ω^) wrong input format! Recheck next time");
                    return crtOperatingFile;
                }
                if(crtOperatingFile == null){
                    System.out.println("(  ^ω^) get me a file first, please");
                }
                else {
                    crtOperatingFile.move(offset, moveType);
                    System.out.println("(  ^ω^) moved cursor to " + crtOperatingFile.getCursorPos());
                }
                return crtOperatingFile;
            case "close":
                if(crtOperatingFile == null){
                    System.out.println("(  ^ω^) get me a file first, please");
                }else {
                    crtOperatingFile.close();
                    System.out.println("(  ^ω^) closed this file");
                }
                return null;
            //smart tools
            case "smart":
                if(instruction.length == 1){
                    System.out.println("(  ^ω^) wrong input format!");
                    return crtOperatingFile;
                }
                smartTools(instruction, fileSystem, crtOperatingFile);
                return crtOperatingFile;
            case "exit":
                if( crtOperatingFile != null){
                    crtOperatingFile.close();
                }
                System.out.println("(  ^ω^) bye bye");
                System.exit(0);
            default:
                System.out.println("(  ^ω^) wrong input!");
                System.out.println("(  ^ω^) print help for guidance");
                return crtOperatingFile;
        }
    }

    private static void smartTools(String[] instruction, FileSystem fileSystem, File file){
        switch (instruction[1]){
            case "cat":
                if(file == null){
                    System.out.println("(  ^ω^) get me a file first, please");
                    break;
                }
                System.out.println("(  ^ω^) meow, here is content:");
                Tools.smartCat(file);
                break;
            case "hex":
                //todo
                if(file == null){
                    System.out.println("(  ^ω^) get me a file first, please");
                    break;
                }
                if( instruction.length != 3){
                    System.out.println("(  ^ω^) wrong input format! Recheck next time");
                    break;
                }
                int blkId = Integer.parseInt(instruction[2]);
                System.out.println("(  ^ω^) hex");
                Tools.smartHex(file, blkId);
                break;
            case "write":
                if(file == null){
                    System.out.println("(  ^ω^) get me a file first, please");
                    break;
                }
                byte[] content = instruction[2].getBytes();
                if( instruction.length == 3 ){
                    Tools.smartWrite(content, file);
                }
                else if(instruction.length == 4){
                    int cursorPos = Integer.parseInt(instruction[3]);
                    Tools.smartWrite(content, cursorPos, file);
                }
                else {
                    System.out.println("(  ^ω^) wrong input!");
                    System.out.println("(  ^ω^) print help for guidance");
                }
                break;
            //先新fileID，在managerID
            case "copy":
                if(file == null){
                    System.out.println("(  ^ω^) get me a file first, please");
                    break;
                }
                if(instruction.length == 4){
                    int copyDestManager = Integer.parseInt(instruction[3]);
                    int copyDestFileId = Integer.parseInt(instruction[2]);
                    if(copyDestManager < fileSystem.getFileManagers().length && copyDestManager> -1
                            && copyDestFileId > 0) {
                        Tools.smartCopy(fileSystem.getFileManagers()[copyDestManager], file, copyDestFileId);
                    }else {
                        System.out.println("(  ^ω^) wrong input!");
                        System.out.println("(  ^ω^) print help for guidance");
                    }
                }
                else {
                    System.out.println("(  ^ω^) wrong input!");
                    System.out.println("(  ^ω^) print help for guidance");
                }
                break;
            case "ls":
                Tools.smartLs(fileSystem.getFileManagers());
                break;
            default:
                System.out.println("(  ^ω^) wrong input! No such tool");
                System.out.println("(  ^ω^) print help for guidance");
                break;
        }

    }



}