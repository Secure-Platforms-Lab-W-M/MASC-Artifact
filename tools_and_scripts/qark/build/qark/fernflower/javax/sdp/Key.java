package javax.sdp;

public interface Key extends Field {
   String getKey() throws SdpParseException;

   String getMethod() throws SdpParseException;

   boolean hasKey() throws SdpParseException;

   void setKey(String var1) throws SdpException;

   void setMethod(String var1) throws SdpException;
}
