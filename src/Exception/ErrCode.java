package Exception;

import java.util.HashMap;
import java.util.Map;

public class ErrCode extends RuntimeException{
    public static final int IO_EXCEPTION = 1;

//能解决，抛出就可以了
    public static final int BLOCK_NOT_FOUND = 2;
    public static final int FILE_NOT_FOUND = 3;
    public static final int BLOCK_DAMAGED = 4;
    public static final int INVALID_ARG = 5;
    public static final int EOF = 6;
    public static final int FILE_ALREADY_EXIST = 7;

    public static final int UNKNOWN = 1000;

    private static final Map<Integer, String> ErrorCodeMap = new HashMap<>();

    static {
        ErrorCodeMap.put(IO_EXCEPTION, "IO exception");

        ErrorCodeMap.put(BLOCK_NOT_FOUND, "(  ^ω^)block not found, wrong index");
        ErrorCodeMap.put(FILE_NOT_FOUND, "(  ^ω^)cannot find file with given src.\n It's likely that sth is wrong with one of the bms");
        ErrorCodeMap.put(BLOCK_DAMAGED, "(  ^ω^)all duplication of current block has been damaged");
        ErrorCodeMap.put(INVALID_ARG, "(  ^ω^)the argument is invalid(or the format might be wrong)");
        ErrorCodeMap.put(EOF, "(  ^ω^)END_OF_FILE exception, you are moving the cursor out of file");
        ErrorCodeMap.put(FILE_ALREADY_EXIST, "(  ^ω^)File of same name already exist, please rename it");
        ErrorCodeMap.put(UNKNOWN, "(  ^ω^)unknown");
    }

    public static String getErrorText(int errorCode) {
        return ErrorCodeMap.getOrDefault(errorCode, "invalid");
    }

    //不能处理的
    public ErrCode(int errorCode) {
        super(String.format("(  ^ω^)error code '%d' \"%s\"", errorCode, getErrorText(errorCode)));
    }

    //可以处理的
    public static void errCodeHandler(int code){
        System.out.printf("(  ^ω^)error code: %d [%s]%n", code, getErrorText(code));
    }


}
