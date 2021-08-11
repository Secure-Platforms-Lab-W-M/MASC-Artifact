// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.security.Key;
import java.util.Locale;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

public class Encryption
{
    public static byte[] ENCR_INIT_BYTES;
    private static boolean INITIALZED;
    public static byte[] INIT_BYTES;
    private static Cipher dcipher;
    private static Cipher ecipher;
    private static byte[] iv;
    private static SecretKey key;
    private static String keyphrase;
    private static AlgorithmParameterSpec paramSpec;
    
    static {
        Encryption.iv = new byte[] { 12, -2, 30, 41, 101, -65, 17, -8, -91, 120, -11, 122, 13, -44, 45, 16 };
        Encryption.INIT_BYTES = new byte[] { 45, 7, -8, 45, 6, -65, 89, 5 };
        Encryption.INITIALZED = false;
        Encryption.keyphrase = "";
        Encryption.dcipher = null;
        Encryption.ecipher = null;
    }
    
    public static byte[] decrypt(byte[] doFinal) throws IOException {
        if (!Encryption.INITIALZED) {
            throw new IOException("Encryption not initialized!");
        }
        try {
            synchronized (Encryption.dcipher) {
                doFinal = Encryption.dcipher.doFinal(doFinal);
                return doFinal;
            }
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Decryption failed:");
            sb.append(ex);
            throw new IOException(sb.toString());
        }
    }
    
    public static byte[] encrypt(byte[] doFinal) throws IOException {
        if (!Encryption.INITIALZED) {
            throw new IOException("Encryption not initialized!");
        }
        try {
            synchronized (Encryption.ecipher) {
                doFinal = Encryption.ecipher.doFinal(doFinal);
                return doFinal;
            }
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Encryption failed:");
            sb.append(ex);
            throw new IOException(sb.toString());
        }
    }
    
    public static InputStream getDecryptedStream(final InputStream inputStream) throws IOException {
        if (!Encryption.INITIALZED) {
            throw new IOException("Encryption not initialized!");
        }
        return new PaddingCipherInputStream(inputStream);
    }
    
    public static OutputStream getEncryptedOutputStream(final OutputStream outputStream, final int n) throws IOException {
        if (!Encryption.INITIALZED) {
            throw new IOException("Encryption not initialized!");
        }
        return new PaddingCipherOutputStream(outputStream, n);
    }
    
    public static void init_AES(final String keyphrase) throws IOException {
        if (Encryption.keyphrase.equals(keyphrase)) {
            return;
        }
        try {
            final ByteBuffer wrap = ByteBuffer.wrap(new byte[16]);
            wrap.putLong(Utils.getLongStringHash(keyphrase));
            wrap.putLong(Utils.getLongStringHash(invertStr(keyphrase)));
            Encryption.paramSpec = new IvParameterSpec(Encryption.iv);
            Encryption.key = new SecretKeySpec(wrap.array(), "AES");
            Encryption.ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            Cipher.getInstance("de$s".replace("$", ""));
            Cipher.getInstance("des".toUpperCase(Locale.ENGLISH));
            Cipher.getInstance("des");
            Cipher.getInstance("AES".replace("A", "D"));
            Cipher.getInstance(new CipherExample().methodA().methodB().getCipherName());
            Encryption.ecipher.init(1, Encryption.key, Encryption.paramSpec);
            (Encryption.dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding")).init(2, Encryption.key, Encryption.paramSpec);
            Encryption.keyphrase = keyphrase;
            Encryption.INITIALZED = true;
            Encryption.ENCR_INIT_BYTES = encrypt(Encryption.INIT_BYTES);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Encryption can not be initialized:");
            sb.append(ex.getMessage());
            throw new IOException(sb.toString());
        }
    }
    
    private static String invertStr(final String s) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (Character.isUpperCase(char1)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(char1);
                sb2.append("");
                sb.append(sb2.toString().toLowerCase());
            }
            else {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(char1);
                sb3.append("");
                sb.append(sb3.toString().toUpperCase());
            }
        }
        return sb.toString();
    }
}
