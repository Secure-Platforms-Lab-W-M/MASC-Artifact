package ch.imvs.sdes4j.srtp;

public class WshSessionParam extends SrtpSessionParam {
   private int wsh;

   public WshSessionParam(int var1) {
      if (var1 >= 64) {
         this.wsh = var1;
      } else {
         throw new IllegalArgumentException("Minimum size is 64");
      }
   }

   public WshSessionParam(String var1) {
      int var2 = Integer.valueOf(var1.split("=")[1]);
      this.wsh = var2;
      if (var2 < 64) {
         throw new IllegalArgumentException("Minimum size is 64");
      }
   }

   public String encode() {
      StringBuilder var1 = new StringBuilder();
      var1.append("WSH=");
      var1.append(this.wsh);
      return var1.toString();
   }

   public int getWindowSizeHint() {
      return this.wsh;
   }
}
