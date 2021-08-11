package ch.imvs.sdes4j.srtp;

import ch.imvs.sdes4j.SessionParam;

public abstract class SrtpSessionParam implements SessionParam {
   SrtpSessionParam() {
   }

   public static SrtpSessionParam create(String var0) {
      if (var0.startsWith("KDR=")) {
         return new KdrSessionParam(var0);
      } else if (var0.equals("UNENCRYPTED_SRTP")) {
         return new PlainSrtpSessionParam();
      } else if (var0.equals("UNENCRYPTED_SRTCP")) {
         return new PlainSrtcpSessionParam();
      } else if (var0.equals("UNAUTHENTICATED_SRTP")) {
         return new NoAuthSessionParam();
      } else if (var0.startsWith("FEC_ORDER=")) {
         return new FecOrderSessionParam(var0);
      } else if (var0.startsWith("FEC_KEY=")) {
         return new FecKeySessionParam(var0);
      } else if (var0.startsWith("WSH=")) {
         return new WshSessionParam(var0);
      } else {
         throw new IllegalArgumentException("Unknown session parameter");
      }
   }
}
