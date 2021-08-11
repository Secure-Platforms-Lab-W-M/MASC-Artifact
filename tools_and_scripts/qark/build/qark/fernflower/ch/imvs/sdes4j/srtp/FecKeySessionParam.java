package ch.imvs.sdes4j.srtp;

public class FecKeySessionParam extends SrtpSessionParam {
   private SrtpKeyParam[] keyParams;

   public FecKeySessionParam(String var1) {
      String[] var4 = var1.substring("FEC_KEY=".length()).split(";");
      this.keyParams = new SrtpKeyParam[var4.length];
      int var2 = 0;

      while(true) {
         SrtpKeyParam[] var3 = this.keyParams;
         if (var2 >= var3.length) {
            return;
         }

         var3[var2] = this.createSrtpKeyParam(var4[var2]);
         ++var2;
      }
   }

   public FecKeySessionParam(SrtpKeyParam[] var1) {
      this.keyParams = var1;
   }

   protected SrtpKeyParam createSrtpKeyParam(String var1) {
      return new SrtpKeyParam(var1);
   }

   public String encode() {
      StringBuilder var2 = new StringBuilder();
      var2.append("FEC_KEY=");
      int var1 = 0;

      while(true) {
         SrtpKeyParam[] var3 = this.keyParams;
         if (var1 >= var3.length) {
            return var2.toString();
         }

         var2.append(var3[var1].encode());
         if (var1 < this.keyParams.length - 1) {
            var2.append(';');
         }

         ++var1;
      }
   }

   public SrtpKeyParam[] getKeyParams() {
      return this.keyParams;
   }
}
