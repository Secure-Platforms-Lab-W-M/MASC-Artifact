package javax.sdp;

public interface RepeatTime extends Field {
   int getActiveDuration() throws SdpParseException;

   int[] getOffsetArray() throws SdpParseException;

   int getRepeatInterval() throws SdpParseException;

   boolean getTypedTime() throws SdpParseException;

   void setActiveDuration(int var1) throws SdpException;

   void setOffsetArray(int[] var1) throws SdpException;

   void setRepeatInterval(int var1) throws SdpException;

   void setTypedTime(boolean var1);
}
