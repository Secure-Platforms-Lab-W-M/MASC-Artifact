package ch.imvs.sdes4j.srtp;

public class PlainSrtcpSessionParam extends SrtpSessionParam {
   private static final String UNENCRYPTED_SRTCP = "UNENCRYPTED_SRTCP";

   public String encode() {
      return "UNENCRYPTED_SRTCP";
   }

   public boolean equals(Object var1) {
      return "UNENCRYPTED_SRTCP".equals(var1);
   }

   public int hashCode() {
      return "UNENCRYPTED_SRTCP".hashCode();
   }
}
