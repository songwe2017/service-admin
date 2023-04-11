package me.song.sys.shiro;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 这个类的目的是替换 {@link SimpleByteSource}
 * 因为该类没有实现 {@link Serializable} 接口，redis 序列化时 jdk 序列化器无法序列化
 * 如果使用 jackson 序列化器可以不使用该类
 *
 * @author Songwe
 * @since 2022/6/6 12:34
 */
public class ShiroByteSource implements ByteSource, Serializable {
    private byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public ShiroByteSource() { }

    public ShiroByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public ShiroByteSource(char[] chars) {
        this.bytes = CodecSupport.toBytes(chars);
    }

    public ShiroByteSource(String string) {
        this.bytes = CodecSupport.toBytes(string);
    }

    public ShiroByteSource(ByteSource source) {
        this.bytes = source.getBytes();
    }

    public ShiroByteSource(File file) {
        this.bytes = (new BytesHelper()).getBytes(file);
    }

    public ShiroByteSource(InputStream stream) {
        this.bytes = (new BytesHelper()).getBytes(stream);
    }

    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    public String toHex() {
        if (this.cachedHex == null) {
            this.cachedHex = Hex.encodeToString(this.getBytes());
        }

        return this.cachedHex;
    }

    public String toBase64() {
        if (this.cachedBase64 == null) {
            this.cachedBase64 = Base64.encodeToString(this.getBytes());
        }

        return this.cachedBase64;
    }

    public String toString() {
        return this.toBase64();
    }

    public int hashCode() {
        return this.bytes != null && this.bytes.length != 0 ? Arrays.hashCode(this.bytes) : 0;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource)o;
            return Arrays.equals(this.getBytes(), bs.getBytes());
        } else {
            return false;
        }
    }

    private static final class BytesHelper extends CodecSupport {
        private BytesHelper() {
        }

        public byte[] getBytes(File file) {
            return this.toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return this.toBytes(stream);
        }
    }

    /*取代原先加盐的工具类*/
    public static class Util{
        public static ByteSource bytes(byte[] bytes){
            return new ShiroByteSource(bytes);
        }

        public static ByteSource bytes(String arg0){
            return new ShiroByteSource(arg0);
        }
    }
}
