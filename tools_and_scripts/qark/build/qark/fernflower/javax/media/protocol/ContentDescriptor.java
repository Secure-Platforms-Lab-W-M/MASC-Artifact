package javax.media.protocol;

import javax.media.Format;

public class ContentDescriptor extends Format {
   public static final String CONTENT_UNKNOWN = "UnknownContent";
   public static final String MIXED = "application.mixed-data";
   public static final String RAW = "raw";
   public static final String RAW_RTP = "raw.rtp";

   public ContentDescriptor(String var1) {
      super(var1);
      this.dataType = byteArray;
   }

   private static final boolean isAlpha(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z';
   }

   private static final boolean isNumeric(char var0) {
      return var0 >= '0' && var0 <= '9';
   }

   private static final boolean isUpperAlpha(char var0) {
      return var0 >= 'A' && var0 <= 'Z';
   }

   public static final String mimeTypeToPackageName(String var0) {
      StringBuffer var3 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var1 = var0.charAt(var2);
         if (var1 != '/' && var1 != '.') {
            if (!isAlpha(var1) && !isNumeric(var1)) {
               var3.append('_');
            } else if (isUpperAlpha(var1)) {
               var3.append(Character.toLowerCase(var1));
            } else {
               var3.append(var1);
            }
         } else {
            var3.append('.');
         }
      }

      return var3.toString();
   }

   public String getContentType() {
      return super.getEncoding();
   }

   public String toString() {
      return this.getContentType();
   }
}
