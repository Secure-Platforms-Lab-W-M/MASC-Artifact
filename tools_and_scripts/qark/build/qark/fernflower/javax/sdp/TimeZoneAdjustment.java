package javax.sdp;

import java.util.Hashtable;

public interface TimeZoneAdjustment extends Field {
   boolean getTypedTime();

   Hashtable getZoneAdjustments(boolean var1) throws SdpParseException;

   void setTypedTime(boolean var1);

   void setZoneAdjustments(Hashtable var1) throws SdpException;
}
