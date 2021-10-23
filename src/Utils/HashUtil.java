package Utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    static MessageDigest MD5;
    static {
        try {
            MD5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String hash(byte[] bytes) {
        byte[] digest = MD5.digest(bytes);
        StringBuilder sb = byte2Hex(digest);
        return sb.toString();
    }

    public static StringBuilder byte2Hex(byte[] data) {
        char[] chars = new char[]{'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder();
        // 处理成十六进制的字符串(通常)
        for (byte b : data) {
            sb.append(chars[(b >> 4) & 15]);
            sb.append(chars[b & 15]);
        }
        return sb;
    }
}
