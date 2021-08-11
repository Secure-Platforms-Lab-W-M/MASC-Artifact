package javax.sdp;

import java.io.Serializable;
import java.util.Vector;

public interface SessionDescription extends Serializable, Cloneable {
   Object clone() throws CloneNotSupportedException;

   String getAttribute(String var1) throws SdpParseException;

   Vector getAttributes(boolean var1);

   int getBandwidth(String var1) throws SdpParseException;

   Vector getBandwidths(boolean var1);

   Connection getConnection();

   Vector getEmails(boolean var1) throws SdpParseException;

   Info getInfo();

   Key getKey();

   Vector getMediaDescriptions(boolean var1) throws SdpException;

   Origin getOrigin();

   Vector getPhones(boolean var1) throws SdpException;

   SessionName getSessionName();

   Vector getTimeDescriptions(boolean var1) throws SdpException;

   URI getURI();

   Version getVersion();

   Vector getZoneAdjustments(boolean var1) throws SdpException;

   void removeAttribute(String var1);

   void removeBandwidth(String var1);

   void setAttribute(String var1, String var2) throws SdpException;

   void setAttributes(Vector var1) throws SdpException;

   void setBandwidth(String var1, int var2) throws SdpException;

   void setBandwidths(Vector var1) throws SdpException;

   void setConnection(Connection var1) throws SdpException;

   void setEmails(Vector var1) throws SdpException;

   void setInfo(Info var1) throws SdpException;

   void setKey(Key var1) throws SdpException;

   void setMediaDescriptions(Vector var1) throws SdpException;

   void setOrigin(Origin var1) throws SdpException;

   void setPhones(Vector var1) throws SdpException;

   void setSessionName(SessionName var1) throws SdpException;

   void setTimeDescriptions(Vector var1) throws SdpException;

   void setURI(URI var1) throws SdpException;

   void setVersion(Version var1) throws SdpException;

   void setZoneAdjustments(Vector var1) throws SdpException;
}
