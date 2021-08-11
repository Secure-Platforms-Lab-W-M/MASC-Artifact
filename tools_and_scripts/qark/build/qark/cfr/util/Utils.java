/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    public static int byteArrayToInt(byte[] arrby) {
        return arrby[3] & 255 | (arrby[2] & 255) << 8 | (arrby[1] & 255) << 16 | (arrby[0] & 255) << 24;
    }

    public static long byteArrayToLong(byte[] arrby, int n) {
        return (long)(arrby[7 + n] & 255) | (long)(arrby[6 + n] & 255) << 8 | (long)(arrby[5 + n] & 255) << 16 | (long)(arrby[4 + n] & 255) << 24 | (long)(arrby[3 + n] & 255) << 32 | (long)(arrby[2 + n] & 255) << 40 | (long)(arrby[1 + n] & 255) << 48 | (long)(arrby[0 + n] & 255) << 56;
    }

    public static void closeSocket(Socket socket) {
        try {
            socket.shutdownOutput();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        try {
            socket.shutdownInput();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        try {
            socket.close();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    public static void copyFile(File file, File file2) throws IOException {
        File file3 = file2.getParentFile();
        if (file3 != null) {
            file3.mkdirs();
        }
        Utils.copyFully(new FileInputStream(file), new FileOutputStream(file2), true);
    }

    public static void copyFully(InputStream inputStream, OutputStream outputStream, boolean bl) throws IOException {
        int n;
        byte[] arrby = new byte[1024];
        while ((n = inputStream.read(arrby)) != -1) {
            outputStream.write(arrby, 0, n);
        }
        outputStream.flush();
        if (bl) {
            outputStream.close();
            inputStream.close();
        }
    }

    public static void deleteFolder(String object) {
        if ((object = new File((String)object)).exists() && object.isDirectory()) {
            File[] arrfile = object.listFiles();
            for (int i = 0; i < arrfile.length; ++i) {
                if (arrfile[i].isDirectory()) {
                    Utils.deleteFolder(arrfile[i].getAbsolutePath());
                    continue;
                }
                arrfile[i].delete();
            }
            object.delete();
        }
    }

    public static Object deserializeObject(byte[] object) throws IOException {
        object = new ObjectInputStream(new ByteArrayInputStream((byte[])object));
        try {
            object = object.readObject();
            return object;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new IOException(classNotFoundException);
        }
    }

    public static long getLongStringHash(String arrby) {
        int n = 0;
        int n2 = 0;
        int n3 = arrby.length();
        arrby = arrby.getBytes();
        for (int i = 0; i < n3; ++i) {
            n = 31 * n + (arrby[i] & 255);
            n2 = 31 * n2 + (arrby[n3 - i - 1] & 255);
        }
        return (long)n << 32 | (long)n2 & 0xFFFFFFFFL;
    }

    public static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(calendar.getTime());
    }

    public static byte[] intToByteArray(int n) {
        return new byte[]{(byte)(n >> 24 & 255), (byte)(n >> 16 & 255), (byte)(n >> 8 & 255), (byte)(n & 255)};
    }

    public static byte[] longToByteArray(long l) {
        return new byte[]{(byte)(l >> 56), (byte)(l >> 48), (byte)(l >> 40), (byte)(l >> 32), (byte)(l >> 24), (byte)(l >> 16), (byte)(l >> 8), (byte)l};
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String[] parseURI(String string2) throws IOException {
        String string3;
        try {
            int n;
            String string4 = string2.substring(7);
            int n2 = n = string4.indexOf(47);
            if (n == -1) {
                n2 = string4.length();
            }
            string3 = string4.substring(0, n2);
            string2 = n2 == string4.length() ? "/" : (string4 = string4.substring(n2));
        }
        catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot parse URI '");
            stringBuilder.append(string2);
            stringBuilder.append("'! - ");
            stringBuilder.append(exception.toString());
            throw new IOException(stringBuilder.toString());
        }
        return new String[]{string3, string2};
    }

    public static byte[] readFully(InputStream inputStream, int n) throws IOException {
        int n2;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrby = new byte[n];
        while ((n2 = inputStream.read(arrby, 0, n)) != -1) {
            byteArrayOutputStream.write(arrby, 0, n2);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static int readLineBytesFromStream(InputStream object, byte[] arrby, boolean bl, boolean bl2) throws IOException {
        int n;
        int n2 = object.read();
        while (bl2 && n2 == 35) {
            n2 = n = Utils.skipLine((InputStream)object);
            if (n == -1) continue;
            n2 = object.read();
        }
        if (n2 == -1) {
            return -1;
        }
        if (arrby.length == 0) {
            throw new IOException("Buffer Overflow!");
        }
        arrby[0] = (byte)n2;
        n = 1;
        int n3 = n2;
        block1 : while (n3 != -1 && n3 != 10) {
            int n4 = n;
            n2 = n3;
            do {
                n3 = n2;
                n = n4;
                if (n2 == -1) continue block1;
                n3 = n2;
                n = n4;
                if (n2 == 10) continue block1;
                n2 = n = object.read();
                if (n == -1) continue;
                if (n4 == arrby.length) {
                    throw new IOException("Buffer Overflow!");
                }
                if (bl && n < 32 && n < 9 && n > 13) {
                    object = new StringBuilder();
                    object.append("Non Printable character: ");
                    object.append(n);
                    object.append("(");
                    object.append((char)n);
                    object.append(")");
                    throw new IOException(object.toString());
                }
                arrby[n4] = (byte)n;
                ++n4;
                n2 = n;
            } while (true);
        }
        return n;
    }

    public static String readLineFromStream(InputStream inputStream) throws IOException {
        return Utils.readLineFromStream(inputStream, false);
    }

    public static String readLineFromStream(InputStream inputStream, boolean bl) throws IOException {
        int n;
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        int n3 = -1;
        int n4 = 0;
        int n5 = 0;
        while (n2 == 0) {
            n3 = inputStream.read();
            byte by = (byte)n3;
            n = n3 != -1 && (by != 10 || bl && n5 != 13) ? 0 : 1;
            n2 = n;
            n = n4;
            if (n2 == 0) {
                stringBuffer.append((char)by);
                n = n4 + 1;
                n5 = by;
            }
            n4 = n;
        }
        if (n3 == -1 && n4 == 0) {
            throw new EOFException("Stream is closed!");
        }
        n = n4;
        if (n4 > 0) {
            n = n4;
            if (n5 == 13) {
                n = n4 - 1;
            }
        }
        return stringBuffer.substring(0, n);
    }

    public static String readLineFromStreamRN(InputStream inputStream) throws IOException {
        return Utils.readLineFromStream(inputStream, true);
    }

    public static byte[] serializeObject(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static int skipLine(InputStream inputStream) throws IOException {
        int n = 0;
        while (n != -1 && n != 10) {
            n = inputStream.read();
        }
        return n;
    }

    public static int skipWhitespace(InputStream inputStream, int n) throws IOException {
        while (n != -1 && n != 10 && (n == 9 || n == 32 || n == 13)) {
            n = inputStream.read();
        }
        return n;
    }

    public static void writeLongToByteArray(long l, byte[] arrby, int n) {
        arrby[n + 0] = (byte)(l >> 56);
        arrby[n + 1] = (byte)(l >> 48);
        arrby[n + 2] = (byte)(l >> 40);
        arrby[n + 3] = (byte)(l >> 32);
        arrby[n + 4] = (byte)(l >> 24);
        arrby[n + 5] = (byte)(l >> 16);
        arrby[n + 6] = (byte)(l >> 8);
        arrby[n + 7] = (byte)l;
    }
}

