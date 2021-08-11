package javax.media.pim;

import javax.media.Format;

class PlugInInfo {
   public String className;
   public Format[] inputFormats;
   public Format[] outputFormats;

   public PlugInInfo(String var1, Format[] var2, Format[] var3) {
      this.className = var1;
      this.inputFormats = var2;
      this.outputFormats = var3;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof PlugInInfo) {
         String var2 = this.className;
         if (var2 == ((PlugInInfo)var1).className || var2 != null && var2.equals(((PlugInInfo)var1).className)) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      return this.className.hashCode();
   }
}
