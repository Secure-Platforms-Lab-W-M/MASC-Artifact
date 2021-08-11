package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.codec.Resources;

public class Lang {
   private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/%s_lang.txt";
   private static final Map Langs = new EnumMap(NameType.class);
   private final Languages languages;
   private final List rules;

   static {
      NameType[] var2 = NameType.values();
      int var1 = var2.length;

      for(int var0 = 0; var0 < var1; ++var0) {
         NameType var3 = var2[var0];
         Langs.put(var3, loadFromResource(String.format("org/apache/commons/codec/language/bm/%s_lang.txt", var3.getName()), Languages.getInstance(var3)));
      }

   }

   private Lang(List var1, Languages var2) {
      this.rules = Collections.unmodifiableList(var1);
      this.languages = var2;
   }

   public static Lang instance(NameType var0) {
      return (Lang)Langs.get(var0);
   }

   public static Lang loadFromResource(String var0, Languages var1) {
      ArrayList var9 = new ArrayList();
      Scanner var8 = new Scanner(Resources.getInputStream(var0), "UTF-8");
      boolean var2 = false;

      label928: {
         Throwable var10000;
         while(true) {
            String var7;
            boolean var10001;
            try {
               if (!var8.hasNextLine()) {
                  break label928;
               }

               var7 = var8.nextLine();
            } catch (Throwable var117) {
               var10000 = var117;
               var10001 = false;
               break;
            }

            String var5 = var7;
            if (var2) {
               try {
                  if (!var5.endsWith("*/")) {
                     continue;
                  }
               } catch (Throwable var112) {
                  var10000 = var112;
                  var10001 = false;
                  break;
               }

               var2 = false;
            } else {
               label920: {
                  try {
                     if (!var5.startsWith("/*")) {
                        break label920;
                     }
                  } catch (Throwable var119) {
                     var10000 = var119;
                     var10001 = false;
                     break;
                  }

                  var2 = true;
                  continue;
               }

               int var3;
               try {
                  var3 = var5.indexOf("//");
               } catch (Throwable var116) {
                  var10000 = var116;
                  var10001 = false;
                  break;
               }

               String var6 = var7;
               if (var3 >= 0) {
                  try {
                     var6 = var5.substring(0, var3);
                  } catch (Throwable var115) {
                     var10000 = var115;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var5 = var6.trim();
                  if (var5.length() == 0) {
                     continue;
                  }
               } catch (Throwable var114) {
                  var10000 = var114;
                  var10001 = false;
                  break;
               }

               try {
                  String[] var122 = var5.split("\\s+");
                  if (var122.length == 3) {
                     Pattern var123 = Pattern.compile(var122[0]);
                     String[] var124 = var122[1].split("\\+");
                     boolean var4 = var122[2].equals("true");
                     var9.add(new Lang.LangRule(var123, new HashSet(Arrays.asList(var124)), var4));
                     continue;
                  }
               } catch (Throwable var118) {
                  var10000 = var118;
                  var10001 = false;
                  break;
               }

               try {
                  StringBuilder var121 = new StringBuilder();
                  var121.append("Malformed line '");
                  var121.append(var7);
                  var121.append("' in language resource '");
                  var121.append(var0);
                  var121.append("'");
                  throw new IllegalArgumentException(var121.toString());
               } catch (Throwable var113) {
                  var10000 = var113;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var120 = var10000;

         try {
            throw var120;
         } finally {
            label883:
            try {
               var8.close();
            } catch (Throwable var110) {
               var120.addSuppressed(var110);
               break label883;
            }

         }
      }

      var8.close();
      return new Lang(var9, var1);
   }

   public String guessLanguage(String var1) {
      Languages.LanguageSet var2 = this.guessLanguages(var1);
      return var2.isSingleton() ? var2.getAny() : "any";
   }

   public Languages.LanguageSet guessLanguages(String var1) {
      var1 = var1.toLowerCase(Locale.ENGLISH);
      HashSet var2 = new HashSet(this.languages.getLanguages());
      Iterator var3 = this.rules.iterator();

      while(var3.hasNext()) {
         Lang.LangRule var4 = (Lang.LangRule)var3.next();
         if (var4.matches(var1)) {
            if (var4.acceptOnMatch) {
               var2.retainAll(var4.languages);
            } else {
               var2.removeAll(var4.languages);
            }
         }
      }

      Languages.LanguageSet var5 = Languages.LanguageSet.from(var2);
      if (var5.equals(Languages.NO_LANGUAGES)) {
         return Languages.ANY_LANGUAGE;
      } else {
         return var5;
      }
   }

   private static final class LangRule {
      private final boolean acceptOnMatch;
      private final Set languages;
      private final Pattern pattern;

      private LangRule(Pattern var1, Set var2, boolean var3) {
         this.pattern = var1;
         this.languages = var2;
         this.acceptOnMatch = var3;
      }

      // $FF: synthetic method
      LangRule(Pattern var1, Set var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }

      public boolean matches(String var1) {
         return this.pattern.matcher(var1).find();
      }
   }
}
