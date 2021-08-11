package javax.sdp;

public interface EMail extends Field {
   String getValue() throws SdpParseException;

   void setValue(String var1) throws SdpException;
}
