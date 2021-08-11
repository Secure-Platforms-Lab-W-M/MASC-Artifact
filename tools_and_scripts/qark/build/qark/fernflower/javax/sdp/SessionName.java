package javax.sdp;

public interface SessionName extends Field {
   String getValue() throws SdpParseException;

   void setValue(String var1) throws SdpException;
}
