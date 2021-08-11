package javax.media.protocol;

import java.io.IOException;
import javax.media.Buffer;
import javax.media.Format;

public interface PushBufferStream extends SourceStream {
   Format getFormat();

   void read(Buffer var1) throws IOException;

   void setTransferHandler(BufferTransferHandler var1);
}
