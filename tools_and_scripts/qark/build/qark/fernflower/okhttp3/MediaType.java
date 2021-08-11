package okhttp3;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public final class MediaType {
   private static final Pattern PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
   private static final String QUOTED = "\"([^\"]*)\"";
   private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
   private static final Pattern TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
   @Nullable
   private final String charset;
   private final String mediaType;
   private final String subtype;
   private final String type;

   private MediaType(String var1, String var2, String var3, @Nullable String var4) {
      this.mediaType = var1;
      this.type = var2;
      this.subtype = var3;
      this.charset = var4;
   }

   @Nullable
   public static MediaType parse(String var0) {
      Matcher var2 = TYPE_SUBTYPE.matcher(var0);
      if (!var2.lookingAt()) {
         return null;
      } else {
         String var4 = var2.group(1).toLowerCase(Locale.US);
         String var5 = var2.group(2).toLowerCase(Locale.US);
         String var3 = null;
         Matcher var6 = PARAMETER.matcher(var0);

         String var8;
         for(int var1 = var2.end(); var1 < var0.length(); var3 = var8) {
            var6.region(var1, var0.length());
            if (!var6.lookingAt()) {
               return null;
            }

            String var7 = var6.group(1);
            var8 = var3;
            if (var7 != null) {
               if (!var7.equalsIgnoreCase("charset")) {
                  var8 = var3;
               } else {
                  var8 = var6.group(2);
                  if (var8 != null) {
                     if (var8.startsWith("'") && var8.endsWith("'") && var8.length() > 2) {
                        var8 = var8.substring(1, var8.length() - 1);
                     }
                  } else {
                     var8 = var6.group(3);
                  }

                  if (var3 != null && !var8.equalsIgnoreCase(var3)) {
                     return null;
                  }
               }
            }

            var1 = var6.end();
         }

         return new MediaType(var0, var4, var5, var3);
      }
   }

   @Nullable
   public Charset charset() {
      return this.charset((Charset)null);
   }

   @Nullable
   public Charset charset(@Nullable Charset var1) {
      try {
         if (this.charset != null) {
            Charset var2 = Charset.forName(this.charset);
            return var2;
         } else {
            return var1;
         }
      } catch (IllegalArgumentException var3) {
         return var1;
      }
   }

   public boolean equals(@Nullable Object var1) {
      return var1 instanceof MediaType && ((MediaType)var1).mediaType.equals(this.mediaType);
   }

   public int hashCode() {
      return this.mediaType.hashCode();
   }

   public String subtype() {
      return this.subtype;
   }

   public String toString() {
      return this.mediaType;
   }

   public String type() {
      return this.type;
   }
}
