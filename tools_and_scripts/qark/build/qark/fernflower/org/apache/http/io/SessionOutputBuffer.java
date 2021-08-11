package org.apache.http.io;

import java.io.IOException;
import org.apache.http.util.CharArrayBuffer;

public interface SessionOutputBuffer {
   void flush() throws IOException;

   HttpTransportMetrics getMetrics();

   void write(int var1) throws IOException;

   void write(byte[] var1) throws IOException;

   void write(byte[] var1, int var2, int var3) throws IOException;

   void writeLine(String var1) throws IOException;

   void writeLine(CharArrayBuffer var1) throws IOException;
}
