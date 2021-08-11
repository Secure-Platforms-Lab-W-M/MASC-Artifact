package net.sf.fmj.media.rtp;

public class RTCPSDES {
   public RTCPSDESItem[] items;
   public int ssrc;

   public static String toString(RTCPSDES[] var0) {
      String var2 = "";

      for(int var1 = 0; var1 < var0.length; ++var1) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var2);
         var3.append(var0[var1]);
         var2 = var3.toString();
      }

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\t\tSource Description for sync source ");
      var1.append(this.ssrc);
      var1.append(":\n");
      var1.append(RTCPSDESItem.toString(this.items));
      return var1.toString();
   }
}
