package javax.sdp;

import java.util.Date;

public interface Time extends Field {
   Date getStart() throws SdpParseException;

   Date getStop() throws SdpParseException;

   boolean getTypedTime();

   boolean isZero();

   void setStart(Date var1) throws SdpException;

   void setStop(Date var1) throws SdpException;

   void setTypedTime(boolean var1);

   void setZero();
}
