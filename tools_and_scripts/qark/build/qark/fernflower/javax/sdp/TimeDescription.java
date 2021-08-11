package javax.sdp;

import java.io.Serializable;
import java.util.Vector;

public interface TimeDescription extends Serializable, Cloneable {
   long NTP_CONST = 2208988800L;

   Vector getRepeatTimes(boolean var1);

   Time getTime() throws SdpParseException;

   void setRepeatTimes(Vector var1) throws SdpException;

   void setTime(Time var1) throws SdpException;
}
