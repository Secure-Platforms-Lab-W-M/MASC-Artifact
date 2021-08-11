package net.sf.fmj.media.rtp;

public class RTCPSDESItem {
   public static final int CNAME = 1;
   public static final int EMAIL = 3;
   public static final int HIGHEST = 8;
   public static final int LOC = 5;
   public static final int NAME = 2;
   public static final int NOTE = 7;
   public static final int PHONE = 4;
   public static final int PRIV = 8;
   public static final int TOOL = 6;
   public static final String[] names = new String[]{"CNAME", "NAME", "EMAIL", "PHONE", "LOC", "TOOL", "NOTE", "PRIV"};
   public byte[] data;
   public int type;

   public RTCPSDESItem() {
   }

   public RTCPSDESItem(int var1, String var2) {
      this.type = var1;
      this.data = new byte[var2.length()];
      this.data = var2.getBytes();
   }

   public RTCPSDESItem(int var1, byte[] var2) {
      this.type = var1;
      this.data = var2;
   }

   public static String toString(RTCPSDESItem[] var0) {
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
      var1.append("\t\t\t");
      var1.append(names[this.type - 1]);
      var1.append(": ");
      var1.append(new String(this.data));
      var1.append("\n");
      return var1.toString();
   }
}
