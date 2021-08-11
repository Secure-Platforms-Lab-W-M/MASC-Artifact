// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.io.ByteArrayOutputStream;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Utils
{
    public static int byteArrayToInt(final byte[] array) {
        return (array[3] & 0xFF) | (array[2] & 0xFF) << 8 | (array[1] & 0xFF) << 16 | (array[0] & 0xFF) << 24;
    }
    
    public static long byteArrayToLong(final byte[] array, final int n) {
        return (long)(array[7 + n] & 0xFF) | (long)(array[6 + n] & 0xFF) << 8 | (long)(array[5 + n] & 0xFF) << 16 | (long)(array[4 + n] & 0xFF) << 24 | (long)(array[3 + n] & 0xFF) << 32 | (long)(array[2 + n] & 0xFF) << 40 | (long)(array[1 + n] & 0xFF) << 48 | (long)(array[0 + n] & 0xFF) << 56;
    }
    
    public static void closeSocket(final Socket socket) {
        try {
            socket.shutdownOutput();
        }
        catch (IOException ex) {}
        try {
            socket.shutdownInput();
        }
        catch (IOException ex2) {}
        try {
            socket.close();
        }
        catch (IOException ex3) {}
    }
    
    public static void copyFile(final File file, final File file2) throws IOException {
        final File parentFile = file2.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }
        copyFully(new FileInputStream(file), new FileOutputStream(file2), true);
    }
    
    public static void copyFully(final InputStream inputStream, final OutputStream outputStream, final boolean b) throws IOException {
        final byte[] array = new byte[1024];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            outputStream.write(array, 0, read);
        }
        outputStream.flush();
        if (b) {
            outputStream.close();
            inputStream.close();
        }
    }
    
    public static void deleteFolder(final String s) {
        final File file = new File(s);
        if (file.exists() && file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; ++i) {
                if (listFiles[i].isDirectory()) {
                    deleteFolder(listFiles[i].getAbsolutePath());
                }
                else {
                    listFiles[i].delete();
                }
            }
            file.delete();
        }
    }
    
    public static Object deserializeObject(final byte[] array) throws IOException {
        final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(array));
        try {
            return objectInputStream.readObject();
        }
        catch (ClassNotFoundException ex) {
            throw new IOException(ex);
        }
    }
    
    public static long getLongStringHash(final String s) {
        int n = 0;
        int n2 = 0;
        final int length = s.length();
        final byte[] bytes = s.getBytes();
        for (int i = 0; i < length; ++i) {
            n = 31 * n + (bytes[i] & 0xFF);
            n2 = 31 * n2 + (bytes[length - i - 1] & 0xFF);
        }
        return (long)n << 32 | ((long)n2 & 0xFFFFFFFFL);
    }
    
    public static String getServerTime() {
        final Calendar instance = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(instance.getTime());
    }
    
    public static byte[] intToByteArray(final int n) {
        return new byte[] { (byte)(n >> 24 & 0xFF), (byte)(n >> 16 & 0xFF), (byte)(n >> 8 & 0xFF), (byte)(n & 0xFF) };
    }
    
    public static byte[] longToByteArray(final long n) {
        return new byte[] { (byte)(n >> 56), (byte)(n >> 48), (byte)(n >> 40), (byte)(n >> 32), (byte)(n >> 24), (byte)(n >> 16), (byte)(n >> 8), (byte)n };
    }
    
    public static String[] parseURI(String substring) throws IOException {
        try {
            final String substring2 = substring.substring(7);
            int n;
            if ((n = substring2.indexOf(47)) == -1) {
                n = substring2.length();
            }
            final String substring3 = substring2.substring(0, n);
            if (n == substring2.length()) {
                substring = "/";
            }
            else {
                substring = substring2.substring(n);
            }
            return new String[] { substring3, substring };
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot parse URI '");
            sb.append(substring);
            sb.append("'! - ");
            sb.append(ex.toString());
            throw new IOException(sb.toString());
        }
    }
    
    public static byte[] readFully(final InputStream inputStream, final int n) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] array = new byte[n];
        while (true) {
            final int read = inputStream.read(array, 0, n);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(array, 0, read);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static int readLineBytesFromStream(final InputStream inputStream, final byte[] array, final boolean b, final boolean b2) throws IOException {
        int n;
        for (n = inputStream.read(); b2 && n == 35; n = inputStream.read()) {
            if ((n = skipLine(inputStream)) != -1) {}
        }
        if (n == -1) {
            return -1;
        }
        if (array.length == 0) {
            throw new IOException("Buffer Overflow!");
        }
        array[0] = (byte)n;
        int n2 = 1;
        int n3 = n;
    Label_0078:
        while (n3 != -1 && n3 != 10) {
            int n4 = n2;
            int n5 = n3;
            int read = 0;
            Block_15: {
                while (true) {
                    n3 = n5;
                    n2 = n4;
                    if (n5 == -1) {
                        continue Label_0078;
                    }
                    n3 = n5;
                    n2 = n4;
                    if (n5 == 10) {
                        continue Label_0078;
                    }
                    read = inputStream.read();
                    if ((n5 = read) == -1) {
                        continue;
                    }
                    if (n4 == array.length) {
                        break;
                    }
                    if (b && read < 32 && read < 9 && read > 13) {
                        break Block_15;
                    }
                    array[n4] = (byte)read;
                    ++n4;
                    n5 = read;
                }
                throw new IOException("Buffer Overflow!");
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Non Printable character: ");
            sb.append(read);
            sb.append("(");
            sb.append((char)read);
            sb.append(")");
            throw new IOException(sb.toString());
        }
        return n2;
    }
    
    public static String readLineFromStream(final InputStream inputStream) throws IOException {
        return readLineFromStream(inputStream, false);
    }
    
    public static String readLineFromStream(final InputStream inputStream, final boolean b) throws IOException {
        final StringBuffer sb = new StringBuffer();
        int i = 0;
        int read = -1;
        int n = 0;
        int n2 = 0;
        while (i == 0) {
            read = inputStream.read();
            final byte b2 = (byte)read;
            int n3 = 0;
            Label_0070: {
                Label_0068: {
                    if (read != -1) {
                        if (b2 == 10) {
                            if (!b) {
                                break Label_0068;
                            }
                            if (n2 == 13) {
                                break Label_0068;
                            }
                        }
                        n3 = 0;
                        break Label_0070;
                    }
                }
                n3 = 1;
            }
            i = n3;
            int n4 = n;
            if (i == 0) {
                sb.append((char)b2);
                n4 = n + 1;
                n2 = b2;
            }
            n = n4;
        }
        if (read == -1 && n == 0) {
            throw new EOFException("Stream is closed!");
        }
        int n5;
        if ((n5 = n) > 0) {
            n5 = n;
            if (n2 == 13) {
                n5 = n - 1;
            }
        }
        return sb.substring(0, n5);
    }
    
    public static String readLineFromStreamRN(final InputStream inputStream) throws IOException {
        return readLineFromStream(inputStream, true);
    }
    
    public static byte[] serializeObject(final Object o) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(o);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
    
    public static int skipLine(final InputStream inputStream) throws IOException {
        int read;
        for (read = 0; read != -1 && read != 10; read = inputStream.read()) {}
        return read;
    }
    
    public static int skipWhitespace(final InputStream inputStream, int read) throws IOException {
        while (read != -1 && read != 10 && (read == 9 || read == 32 || read == 13)) {
            read = inputStream.read();
        }
        return read;
    }
    
    public static void writeLongToByteArray(final long n, final byte[] array, final int n2) {
        array[n2 + 0] = (byte)(n >> 56);
        array[n2 + 1] = (byte)(n >> 48);
        array[n2 + 2] = (byte)(n >> 40);
        array[n2 + 3] = (byte)(n >> 32);
        array[n2 + 4] = (byte)(n >> 24);
        array[n2 + 5] = (byte)(n >> 16);
        array[n2 + 6] = (byte)(n >> 8);
        array[n2 + 7] = (byte)n;
    }
}
