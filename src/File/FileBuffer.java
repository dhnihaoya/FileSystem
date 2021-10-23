package File;

import java.util.Arrays;

public class FileBuffer {

    private final byte[] content;

    public FileBuffer(int size) {
        this.content = new byte[size];
    }

    public FileBuffer(byte[] buffer) {
        this.content = buffer;
    }


    //在给定位置插入newContent里的内容
    public FileBuffer insert(byte[] newContent, int cursorPos){
        FileBuffer newBuffer = new FileBuffer(this.getSize() + newContent.length);
        newBuffer.copy(this.content, 0, cursorPos);
        newBuffer.copy(newContent, cursorPos, newContent.length);
        System.arraycopy(this.content, cursorPos, newBuffer.content, cursorPos + newContent.length,
                this.getSize() - cursorPos );
        return newBuffer;
    }


    public static FileBuffer trim(FileBuffer oldBuffer, int newSize){
        byte[] buffer = Arrays.copyOf(oldBuffer.getContent(), newSize);
        for(int i = 0; i < buffer.length; i++){
            if( buffer[i] == 0 ) {
                buffer[i] ='0';
            }
        }
        return new FileBuffer(buffer);
    }

    public void copy(byte[] src, int destPos, int length) {
        System.arraycopy(src, 0, content, destPos, length);
    }


    public byte[] getContent() {
        return content;
    }

    public int getSize() {
        return this.content.length;
    }

}
