package org.jsoup.nodes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Map.Entry;
import org.jsoup.SerializationException;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

public class Entities {
   private static final Map base = loadEntities("entities-base.properties");
   private static final Map baseByVal;
   private static final Map full;
   private static final Map fullByVal;
   private static final Object[][] xhtmlArray = new Object[][]{{"quot", 34}, {"amp", 38}, {"lt", 60}, {"gt", 62}};
   private static final Map xhtmlByVal = new HashMap();

   static {
      baseByVal = toCharacterKey(base);
      full = loadEntities("entities-full.properties");
      fullByVal = toCharacterKey(full);
      Object[][] var3 = xhtmlArray;
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         Object[] var4 = var3[var1];
         char var0 = (char)(Integer)var4[1];
         xhtmlByVal.put(var0, (String)var4[0]);
      }

   }

   private Entities() {
   }

   private static boolean canEncode(Entities.CoreCharset var0, char var1, CharsetEncoder var2) {
      boolean var4 = true;
      boolean var3 = var4;
      switch(var0) {
      case ascii:
         var3 = var4;
         if (var1 >= 128) {
            return false;
         }
      case utf:
         break;
      default:
         var3 = var2.canEncode(var1);
      }

      return var3;
   }

   static String escape(String var0, Document.OutputSettings var1) {
      StringBuilder var2 = new StringBuilder(var0.length() * 2);

      try {
         escape(var2, var0, var1, false, false, false);
      } catch (IOException var3) {
         throw new SerializationException(var3);
      }

      return var2.toString();
   }

   static void escape(Appendable var0, String var1, Document.OutputSettings var2, boolean var3, boolean var4, boolean var5) throws IOException {
      boolean var8 = false;
      boolean var7 = false;
      Entities.EscapeMode var14 = var2.escapeMode();
      CharsetEncoder var18 = var2.encoder();
      Entities.CoreCharset var15 = Entities.CoreCharset.byName(var18.charset().name());
      Map var16 = var14.getMap();
      int var12 = var1.length();

      boolean var10;
      for(int var11 = 0; var11 < var12; var7 = var10) {
         boolean var9;
         int var13;
         label67: {
            var13 = var1.codePointAt(var11);
            var9 = var8;
            var10 = var7;
            if (var4) {
               if (StringUtil.isWhitespace(var13)) {
                  if (var5) {
                     var9 = var8;
                     var10 = var7;
                     if (!var7) {
                        break label67;
                     }
                  }

                  if (var8) {
                     var10 = var7;
                     var9 = var8;
                  } else {
                     var0.append(' ');
                     var9 = true;
                     var10 = var7;
                  }
                  break label67;
               }

               var9 = false;
               var10 = true;
            }

            if (var13 < 65536) {
               char var6 = (char)var13;
               switch(var6) {
               case '"':
                  if (var3) {
                     var0.append("&quot;");
                  } else {
                     var0.append(var6);
                  }
                  break;
               case '&':
                  var0.append("&amp;");
                  break;
               case '<':
                  if (var3 && var14 != Entities.EscapeMode.xhtml) {
                     var0.append(var6);
                     break;
                  }

                  var0.append("&lt;");
                  break;
               case '>':
                  if (!var3) {
                     var0.append("&gt;");
                  } else {
                     var0.append(var6);
                  }
                  break;
               case ' ':
                  if (var14 != Entities.EscapeMode.xhtml) {
                     var0.append("&nbsp;");
                  } else {
                     var0.append("&#xa0;");
                  }
                  break;
               default:
                  if (canEncode(var15, var6, var18)) {
                     var0.append(var6);
                  } else if (var16.containsKey(var6)) {
                     var0.append('&').append((CharSequence)var16.get(var6)).append(';');
                  } else {
                     var0.append("&#x").append(Integer.toHexString(var13)).append(';');
                  }
               }
            } else {
               String var17 = new String(Character.toChars(var13));
               if (var18.canEncode(var17)) {
                  var0.append(var17);
               } else {
                  var0.append("&#x").append(Integer.toHexString(var13)).append(';');
               }
            }
         }

         var11 += Character.charCount(var13);
         var8 = var9;
      }

   }

   public static Character getCharacterByName(String var0) {
      return (Character)full.get(var0);
   }

   public static boolean isBaseNamedEntity(String var0) {
      return base.containsKey(var0);
   }

   public static boolean isNamedEntity(String var0) {
      return full.containsKey(var0);
   }

   private static Map loadEntities(String var0) {
      Properties var3 = new Properties();
      HashMap var2 = new HashMap();

      try {
         InputStream var4 = Entities.class.getResourceAsStream(var0);
         var3.load(var4);
         var4.close();
      } catch (IOException var5) {
         throw new MissingResourceException("Error loading entities resource: " + var5.getMessage(), "Entities", var0);
      }

      Iterator var6 = var3.entrySet().iterator();

      while(var6.hasNext()) {
         Entry var7 = (Entry)var6.next();
         char var1 = (char)Integer.parseInt((String)var7.getValue(), 16);
         var2.put((String)var7.getKey(), var1);
      }

      return var2;
   }

   private static Map toCharacterKey(Map var0) {
      HashMap var1 = new HashMap();
      Iterator var4 = var0.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var3 = (Entry)var4.next();
         Character var2 = (Character)var3.getValue();
         String var5 = (String)var3.getKey();
         if (var1.containsKey(var2)) {
            if (var5.toLowerCase().equals(var5)) {
               var1.put(var2, var5);
            }
         } else {
            var1.put(var2, var5);
         }
      }

      return var1;
   }

   static String unescape(String var0) {
      return unescape(var0, false);
   }

   static String unescape(String var0, boolean var1) {
      return Parser.unescapeEntities(var0, var1);
   }

   private static enum CoreCharset {
      ascii,
      fallback,
      utf;

      private static Entities.CoreCharset byName(String var0) {
         if (var0.equals("US-ASCII")) {
            return ascii;
         } else {
            return var0.startsWith("UTF-") ? utf : fallback;
         }
      }
   }

   public static enum EscapeMode {
      base(Entities.baseByVal),
      extended(Entities.fullByVal),
      xhtml(Entities.xhtmlByVal);

      private Map map;

      private EscapeMode(Map var3) {
         this.map = var3;
      }

      public Map getMap() {
         return this.map;
      }
   }
}
