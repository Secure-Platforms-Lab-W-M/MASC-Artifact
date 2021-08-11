package ch.imvs.sdes4j.srtp;

public class FecOrderSessionParam extends SrtpSessionParam {
   public static final int FEC_SRTP = 1;
   public static final int SRTP_FEC = 2;
   private int mode;

   public FecOrderSessionParam(int var1) {
      if (var1 != 1 && var1 != 2) {
         throw new IllegalArgumentException("mode must be one of FEC_SRTP or SRTP_FEC");
      } else {
         this.mode = var1;
      }
   }

   public FecOrderSessionParam(String var1) {
      var1 = var1.substring("FEC_ORDER=".length());
      if (var1.equals("FEC_SRTP")) {
         this.mode = 1;
      } else if (var1.equals("SRTP_FEC")) {
         this.mode = 2;
      } else {
         throw new IllegalArgumentException("unknown value");
      }
   }

   public String encode() {
      int var1 = this.mode;
      if (var1 != 1) {
         if (var1 == 2) {
            return "FEC_ORDER=SRTP_FEC";
         } else {
            throw new IllegalArgumentException("invalid mode");
         }
      } else {
         return "FEC_ORDER=FEC_SRTP";
      }
   }

   public int getMode() {
      return this.mode;
   }
}
