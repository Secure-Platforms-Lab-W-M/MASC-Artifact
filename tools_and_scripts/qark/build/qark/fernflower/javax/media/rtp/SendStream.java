package javax.media.rtp;

import java.io.IOException;
import javax.media.rtp.rtcp.SourceDescription;

public interface SendStream extends RTPStream {
   void close();

   TransmissionStats getSourceTransmissionStats();

   int setBitRate(int var1);

   void setSourceDescription(SourceDescription[] var1);

   void start() throws IOException;

   void stop() throws IOException;
}
