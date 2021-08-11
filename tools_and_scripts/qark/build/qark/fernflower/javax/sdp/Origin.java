package javax.sdp;

public interface Origin extends Field {
   String getAddress() throws SdpParseException;

   String getAddressType() throws SdpParseException;

   String getNetworkType() throws SdpParseException;

   long getSessionId() throws SdpParseException;

   long getSessionVersion() throws SdpParseException;

   String getUsername() throws SdpParseException;

   void setAddress(String var1) throws SdpException;

   void setAddressType(String var1) throws SdpException;

   void setNetworkType(String var1) throws SdpException;

   void setSessionId(long var1) throws SdpException;

   void setSessionVersion(long var1) throws SdpException;

   void setUsername(String var1) throws SdpException;
}
