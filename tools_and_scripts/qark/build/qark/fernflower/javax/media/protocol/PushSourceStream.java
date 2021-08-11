package javax.media.protocol;

import java.io.IOException;

public interface PushSourceStream extends SourceStream {
   int getMinimumTransferSize();

   int read(byte[] var1, int var2, int var3) throws IOException;

   void setTransferHandler(SourceTransferHandler var1);
}
