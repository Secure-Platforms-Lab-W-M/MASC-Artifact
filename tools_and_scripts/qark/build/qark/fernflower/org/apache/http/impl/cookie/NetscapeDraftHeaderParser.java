package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.BitSet;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.TokenParser;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class NetscapeDraftHeaderParser {
   public static final NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();
   private static final char PARAM_DELIMITER = ';';
   private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET(new int[]{61, 59});
   private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(new int[]{59});
   private final TokenParser tokenParser;

   public NetscapeDraftHeaderParser() {
      this.tokenParser = TokenParser.INSTANCE;
   }

   private NameValuePair parseNameValuePair(CharArrayBuffer var1, ParserCursor var2) {
      String var4 = this.tokenParser.parseToken(var1, var2, TOKEN_DELIMS);
      if (var2.atEnd()) {
         return new BasicNameValuePair(var4, (String)null);
      } else {
         char var3 = var1.charAt(var2.getPos());
         var2.updatePos(var2.getPos() + 1);
         if (var3 != '=') {
            return new BasicNameValuePair(var4, (String)null);
         } else {
            String var5 = this.tokenParser.parseToken(var1, var2, VALUE_DELIMS);
            if (!var2.atEnd()) {
               var2.updatePos(var2.getPos() + 1);
            }

            return new BasicNameValuePair(var4, var5);
         }
      }
   }

   public HeaderElement parseHeader(CharArrayBuffer var1, ParserCursor var2) throws ParseException {
      Args.notNull(var1, "Char array buffer");
      Args.notNull(var2, "Parser cursor");
      NameValuePair var3 = this.parseNameValuePair(var1, var2);
      ArrayList var4 = new ArrayList();

      while(!var2.atEnd()) {
         var4.add(this.parseNameValuePair(var1, var2));
      }

      return new BasicHeaderElement(var3.getName(), var3.getValue(), (NameValuePair[])var4.toArray(new NameValuePair[var4.size()]));
   }
}
