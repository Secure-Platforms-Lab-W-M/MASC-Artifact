package javax.media;

import java.io.Serializable;
import java.util.Arrays;

public class CaptureDeviceInfo implements Serializable {
   protected Format[] formats;
   protected MediaLocator locator;
   protected String name;

   public CaptureDeviceInfo() {
   }

   public CaptureDeviceInfo(String var1, MediaLocator var2, Format[] var3) {
      this.name = var1;
      this.locator = var2;
      this.formats = var3;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (!(var1 instanceof CaptureDeviceInfo)) {
         return false;
      } else {
         CaptureDeviceInfo var4 = (CaptureDeviceInfo)var1;
         String var2 = this.getName();
         String var3 = var4.getName();
         if (var2 == null) {
            if (var3 != null) {
               return false;
            }
         } else if (!var2.equals(var3)) {
            return false;
         }

         MediaLocator var5 = this.getLocator();
         MediaLocator var6 = var4.getLocator();
         if (var5 == null) {
            if (var6 != null) {
               return false;
            }
         } else if (!var5.equals(var6)) {
            return false;
         }

         return Arrays.equals(this.getFormats(), var4.getFormats());
      }
   }

   public Format[] getFormats() {
      return this.formats;
   }

   public MediaLocator getLocator() {
      return this.locator;
   }

   public String getName() {
      return this.name;
   }

   public int hashCode() {
      int var2 = 0;
      String var5 = this.getName();
      if (var5 != null) {
         var2 = 0 + var5.hashCode();
      }

      MediaLocator var7 = this.getLocator();
      int var1 = var2;
      if (var7 != null) {
         var1 = var2 + var7.hashCode();
      }

      Format[] var8 = this.getFormats();
      int var3 = var1;
      if (var8 != null) {
         int var4 = var8.length;
         var2 = 0;

         while(true) {
            var3 = var1;
            if (var2 >= var4) {
               break;
            }

            Format var6 = var8[var2];
            var3 = var1;
            if (var6 != null) {
               var3 = var1 + var6.hashCode();
            }

            ++var2;
            var1 = var3;
         }
      }

      return var3;
   }

   public String toString() {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.name);
      var2.append(" : ");
      var2.append(this.locator);
      var2.append("\n");
      if (this.formats != null) {
         int var1 = 0;

         while(true) {
            Format[] var3 = this.formats;
            if (var1 >= var3.length) {
               break;
            }

            var2.append(var3[var1]);
            var2.append("\n");
            ++var1;
         }
      }

      return var2.toString();
   }
}
