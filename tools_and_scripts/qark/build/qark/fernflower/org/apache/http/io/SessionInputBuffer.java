package org.apache.http.io;

import java.io.IOException;
import org.apache.http.util.CharArrayBuffer;

public interface SessionInputBuffer {
   HttpTransportMetrics getMetrics();

   @Deprecated
   boolean isDataAvailable(int var1) throws IOException;

   int read() throws IOException;

   int read(byte[] var1) throws IOException;

   int read(byte[] var1, int var2, int var3) throws IOException;

   int readLine(CharArrayBuffer var1) throws IOException;

   String readLine() throws IOException;
}
