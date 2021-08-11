package javax.sdp;

public interface Version extends Field {
   int getVersion() throws SdpParseException;

   void setVersion(int var1) throws SdpException;
}
