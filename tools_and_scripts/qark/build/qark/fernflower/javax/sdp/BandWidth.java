package javax.sdp;

public interface BandWidth extends Field {
   // $FF: renamed from: AS java.lang.String
   String field_1 = "AS";
   // $FF: renamed from: CT java.lang.String
   String field_2 = "CT";

   String getType() throws SdpParseException;

   int getValue() throws SdpParseException;

   void setType(String var1) throws SdpException;

   void setValue(int var1) throws SdpException;
}
