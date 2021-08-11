package javax.media.protocol;

import java.io.IOException;

public interface PullSourceStream extends SourceStream {
   int read(byte[] var1, int var2, int var3) throws IOException;

   boolean willReadBlock();
}
