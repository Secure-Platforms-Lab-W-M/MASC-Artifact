package net.sf.fmj.media.datasink.rtp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RTPUrlParser {
   private static final Pattern pattern = Pattern.compile("rtp://([a-zA-Z_/\\.0-9]+)(:([0-9]+))(/(audio|video)(/([0-9]+))?)(\\&([a-zA-Z_/\\.0-9]+)(:([0-9]+))(/(audio|video)(/([0-9]+))?))?");

   private static ParsedRTPUrlElement extract(Matcher var0, int var1) throws RTPUrlParserException {
      ParsedRTPUrlElement var2 = new ParsedRTPUrlElement();

      try {
         var2.host = var0.group(var1 + 1);
         var2.port = Integer.parseInt(var0.group(var1 + 3));
         var2.type = var0.group(var1 + 5);
         if (var0.group(var1 + 7) != null) {
            var2.ttl = Integer.parseInt(var0.group(var1 + 7));
         }

         return var2;
      } catch (NumberFormatException var3) {
         throw new RTPUrlParserException(var3);
      }
   }

   public static ParsedRTPUrl parse(String var0) throws RTPUrlParserException {
      Matcher var1 = pattern.matcher(var0);
      if (var1.matches()) {
         ParsedRTPUrlElement var2 = extract(var1, 0);
         if (var1.group(9) == null) {
            return new ParsedRTPUrl(var2);
         } else {
            ParsedRTPUrlElement var3 = extract(var1, 8);
            if (!var3.type.equals(var2.type)) {
               return new ParsedRTPUrl(var2, var3);
            } else {
               StringBuilder var4 = new StringBuilder();
               var4.append("Both elements of the RTP URL have type ");
               var4.append(var2.type);
               throw new RTPUrlParserException(var4.toString());
            }
         }
      } else {
         throw new RTPUrlParserException("URL does not match regular expression for RTP URLs");
      }
   }
}
