package okio;

import java.io.Closeable;
import java.io.IOException;

public interface Source extends Closeable {
   void close() throws IOException;

   long read(Buffer var1, long var2) throws IOException;

   Timeout timeout();
}
