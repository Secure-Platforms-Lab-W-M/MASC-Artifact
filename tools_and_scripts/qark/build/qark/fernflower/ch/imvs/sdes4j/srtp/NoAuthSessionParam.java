package ch.imvs.sdes4j.srtp;

public class NoAuthSessionParam extends SrtpSessionParam {
   private static final String UNAUTHENTICATED_SRTP = "UNAUTHENTICATED_SRTP";

   public String encode() {
      return "UNAUTHENTICATED_SRTP";
   }

   public boolean equals(Object var1) {
      return "UNAUTHENTICATED_SRTP".equals(var1);
   }

   public int hashCode() {
      return "UNAUTHENTICATED_SRTP".hashCode();
   }
}
