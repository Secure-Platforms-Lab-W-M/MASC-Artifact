package ip;

public class CheckSum {
   public static int chkSum(byte[] var0, int var1, int var2) {
      int var3 = 0;
      int var4 = 0;

      while(true) {
         int var5 = var3;
         if (var4 >= var2) {
            while(var5 >> 16 != 0) {
               var5 = (var5 & '\uffff') + (var5 >> 16);
            }

            return ~var5 & '\uffff';
         }

         int var6 = (var0[var1 + var4] & 255) << 8;
         var5 = var6;
         if (var4 + 1 < var2) {
            var5 = var6 + (var0[var1 + var4 + 1] & 255);
         }

         var3 += var5;
         var4 += 2;
      }
   }
}
