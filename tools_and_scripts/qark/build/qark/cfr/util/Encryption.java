/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import util.CipherExample;
import util.PaddingCipherInputStream;
import util.PaddingCipherOutputStream;
import util.Utils;

public class Encryption {
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
        iv = new byte[]{12, -2, 30, 41, 101, -65, 17, -8, -91, 120, -11, 122, 13, -44, 45, 16};
        INIT_BYTES = new byte[]{45, 7, -8, 45, 6, -65, 89, 5};
        INITIALZED = false;
        keyphrase = "";
        dcipher = null;
        ecipher = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static byte[] decrypt(byte[] arrby) throws IOException {
        if (!INITIALZED) {
            throw new IOException("Encryption not initialized!");
        }
        try {
            Cipher cipher = dcipher;
            // MONITORENTER : cipher
        }
        catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Decryption failed:");
            stringBuilder.append(exception);
            throw new IOException(stringBuilder.toString());
        }
        arrby = dcipher.doFinal(arrby);
        // MONITOREXIT : cipher
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static byte[] encrypt(byte[] arrby) throws IOException {
        if (!INITIALZED) {
            throw new IOException("Encryption not initialized!");
        }
        try {
            Cipher cipher = ecipher;
            // MONITORENTER : cipher
        }
        catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Encryption failed:");
            stringBuilder.append(exception);
            throw new IOException(stringBuilder.toString());
        }
        arrby = ecipher.doFinal(arrby);
        // MONITOREXIT : cipher
        return arrby;
    }

    public static InputStream getDecryptedStream(InputStream inputStream) throws IOException {
        if (!INITIALZED) {
            throw new IOException("Encryption not initialized!");
        }
        return new PaddingCipherInputStream(inputStream);
    }

    public static OutputStream getEncryptedOutputStream(OutputStream outputStream, int n) throws IOException {
        if (!INITIALZED) {
            throw new IOException("Encryption not initialized!");
        }
        return new PaddingCipherOutputStream(outputStream, n);
    }

    public static void init_AES(String string2) throws IOException {
        if (keyphrase.equals(string2)) {
            return;
        }
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
            byteBuffer.putLong(Utils.getLongStringHash(string2));
            byteBuffer.putLong(Utils.getLongStringHash(Encryption.invertStr(string2)));
            paramSpec = new IvParameterSpec(iv);
            key = new SecretKeySpec(byteBuffer.array(), "AES");
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            Cipher.getInstance("de$s".replace("$", ""));
            Cipher.getInstance("des".toUpperCase(Locale.ENGLISH));
            Cipher.getInstance("des");
            Cipher.getInstance("AES".replace("A", "D"));
            Cipher.getInstance(new CipherExample().methodA().methodB().getCipherName());
            ecipher.init(1, (Key)key, paramSpec);
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher.init(2, (Key)key, paramSpec);
            keyphrase = string2;
            INITIALZED = true;
            ENCR_INIT_BYTES = Encryption.encrypt(INIT_BYTES);
            return;
        }
        catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Encryption can not be initialized:");
            stringBuilder.append(exception.getMessage());
            throw new IOException(stringBuilder.toString());
        }
    }

    private static String invertStr(String string2) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string2.length(); ++i) {
            StringBuilder stringBuilder;
            char c = string2.charAt(i);
            if (Character.isUpperCase(c)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(c);
                stringBuilder.append("");
                stringBuffer.append(stringBuilder.toString().toLowerCase());
                continue;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(c);
            stringBuilder.append("");
            stringBuffer.append(stringBuilder.toString().toUpperCase());
        }
        return stringBuffer.toString();
    }
}

