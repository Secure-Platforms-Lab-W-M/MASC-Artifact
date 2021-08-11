package ch.imvs.sdes4j.srtp;

public class KdrSessionParam extends SrtpSessionParam {
   private int kdr;

   public KdrSessionParam(int var1) {
      if (var1 >= 0 && var1 <= 24) {
         this.kdr = var1;
      } else {
         throw new IllegalArgumentException("kdr must be in range 0..24 inclusive");
      }
   }

   public KdrSessionParam(String var1) {
      int var2 = Integer.valueOf(var1.substring("KDR=".length()));
      this.kdr = var2;
      if (var2 < 0 || var2 > 24) {
         throw new IllegalArgumentException("kdr must be in range 0..24 inclusive");
      }
   }

   public String encode() {
      StringBuilder var1 = new StringBuilder();
      var1.append("KDR=");
      var1.append(String.valueOf(this.kdr));
      return var1.toString();
   }

   public int getKeyDerivationRate() {
      return this.kdr;
   }

   public int getKeyDerivationRateExpanded() {
      return (int)Math.pow(2.0D, (double)this.kdr);
   }
}
