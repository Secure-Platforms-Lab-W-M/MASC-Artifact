package javax.sdp;

public interface Connection extends Field {
   // $FF: renamed from: IN java.lang.String
   String field_0 = "IN";
   String IP4 = "IP4";
   String IP6 = "IP6";

   String getAddress() throws SdpParseException;

   String getAddressType() throws SdpParseException;

   String getNetworkType() throws SdpParseException;

   void setAddress(String var1) throws SdpException;

   void setAddressType(String var1) throws SdpException;

   void setNetworkType(String var1) throws SdpException;
}
