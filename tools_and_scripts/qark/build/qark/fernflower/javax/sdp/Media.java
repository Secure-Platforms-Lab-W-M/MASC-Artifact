package javax.sdp;

import java.util.Vector;

public interface Media extends Field {
   Vector getMediaFormats(boolean var1) throws SdpParseException;

   int getMediaPort() throws SdpParseException;

   String getMediaType() throws SdpParseException;

   int getPortCount() throws SdpParseException;

   String getProtocol() throws SdpParseException;

   void setMediaFormats(Vector var1) throws SdpException;

   void setMediaPort(int var1) throws SdpException;

   void setMediaType(String var1) throws SdpException;

   void setPortCount(int var1) throws SdpException;

   void setProtocol(String var1) throws SdpException;

   String toString();
}
