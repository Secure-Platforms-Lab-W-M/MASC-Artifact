package javax.sdp;

import java.io.Serializable;
import java.util.Vector;

public interface MediaDescription extends Serializable, Cloneable {
   void addDynamicPayloads(Vector var1, Vector var2) throws SdpException;

   String getAttribute(String var1) throws SdpParseException;

   Vector getAttributes(boolean var1);

   int getBandwidth(String var1) throws SdpParseException;

   Vector getBandwidths(boolean var1);

   Connection getConnection();

   Info getInfo();

   Key getKey();

   Media getMedia();

   Vector getMimeParameters() throws SdpException;

   Vector getMimeTypes() throws SdpException;

   void removeAttribute(String var1);

   void removeBandwidth(String var1);

   void setAttribute(String var1, String var2) throws SdpException;

   void setAttributes(Vector var1) throws SdpException;

   void setBandwidth(String var1, int var2) throws SdpException;

   void setBandwidths(Vector var1) throws SdpException;

   void setConnection(Connection var1) throws SdpException;

   void setInfo(Info var1) throws SdpException;

   void setKey(Key var1) throws SdpException;

   void setMedia(Media var1) throws SdpException;
}
