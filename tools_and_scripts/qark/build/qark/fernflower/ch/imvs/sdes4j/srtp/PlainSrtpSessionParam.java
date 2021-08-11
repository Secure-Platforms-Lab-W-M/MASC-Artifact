package ch.imvs.sdes4j.srtp;

public class PlainSrtpSessionParam extends SrtpSessionParam {
   private static final String UNENCRYPTED_SRTP = "UNENCRYPTED_SRTP";

   public String encode() {
      return "UNENCRYPTED_SRTP";
   }

   public boolean equals(Object var1) {
      return "UNENCRYPTED_SRTP".equals(var1);
   }

   public int hashCode() {
      return "UNENCRYPTED_SRTP".hashCode();
   }
}
