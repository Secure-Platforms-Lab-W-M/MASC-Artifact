package javax.media.rtp;

import java.io.IOException;
import javax.media.protocol.PushSourceStream;

public interface RTPConnector {
   void close();

   PushSourceStream getControlInputStream() throws IOException;

   OutputDataStream getControlOutputStream() throws IOException;

   PushSourceStream getDataInputStream() throws IOException;

   OutputDataStream getDataOutputStream() throws IOException;

   double getRTCPBandwidthFraction();

   double getRTCPSenderBandwidthFraction();

   int getReceiveBufferSize();

   int getSendBufferSize();

   void setReceiveBufferSize(int var1) throws IOException;

   void setSendBufferSize(int var1) throws IOException;
}
