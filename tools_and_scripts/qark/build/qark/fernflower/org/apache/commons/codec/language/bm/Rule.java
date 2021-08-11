package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.codec.Resources;

public class Rule {
   public static final String ALL = "ALL";
   public static final Rule.RPattern ALL_STRINGS_RMATCHER = new Rule.RPattern() {
      public boolean isMatch(CharSequence var1) {
         return true;
      }
   };
   private static final String DOUBLE_QUOTE = "\"";
   private static final String HASH_INCLUDE = "#include";
   private static final Map RULES = new EnumMap(NameType.class);
   private final Rule.RPattern lContext;
   private final String pattern;
   private final Rule.PhonemeExpr phoneme;
   private final Rule.RPattern rContext;

   static {
      NameType[] var8 = NameType.values();
      int var2 = var8.length;

      for(int var0 = 0; var0 < var2; ++var0) {
         NameType var4 = var8[var0];
         EnumMap var9 = new EnumMap(RuleType.class);
         RuleType[] var10 = RuleType.values();
         int var3 = var10.length;

         for(int var1 = 0; var1 < var3; ++var1) {
            RuleType var5 = var10[var1];
            HashMap var11 = new HashMap();
            Iterator var12 = Languages.getInstance(var4).getLanguages().iterator();

            while(var12.hasNext()) {
               String var6 = (String)var12.next();

               IllegalStateException var10000;
               label720: {
                  Scanner var7;
                  boolean var10001;
                  try {
                     var7 = createScanner(var4, var5, var6);
                  } catch (IllegalStateException var82) {
                     var10000 = var82;
                     var10001 = false;
                     break label720;
                  }

                  try {
                     var11.put(var6, parseRules(var7, createResourceName(var4, var5, var6)));
                  } catch (Throwable var81) {
                     Throwable var87 = var81;

                     try {
                        throw var87;
                     } finally {
                        if (var7 != null) {
                           try {
                              var7.close();
                           } catch (Throwable var78) {
                              Throwable var85 = var78;

                              label689:
                              try {
                                 var87.addSuppressed(var85);
                                 break label689;
                              } catch (IllegalStateException var77) {
                                 var10000 = var77;
                                 var10001 = false;
                                 break label720;
                              }
                           }
                        }

                        try {
                           ;
                        } catch (IllegalStateException var76) {
                           var10000 = var76;
                           var10001 = false;
                           break label720;
                        }
                     }
                  }

                  if (var7 == null) {
                     continue;
                  }

                  try {
                     var7.close();
                     continue;
                  } catch (IllegalStateException var80) {
                     var10000 = var80;
                     var10001 = false;
                  }
               }

               IllegalStateException var86 = var10000;
               StringBuilder var88 = new StringBuilder();
               var88.append("Problem processing ");
               var88.append(createResourceName(var4, var5, var6));
               throw new IllegalStateException(var88.toString(), var86);
            }

            if (!var5.equals(RuleType.RULES)) {
               Scanner var84 = createScanner(var4, var5, "common");

               try {
                  var11.put("common", parseRules(var84, createResourceName(var4, var5, "common")));
               } catch (Throwable var75) {
                  Throwable var83 = var75;

                  try {
                     throw var83;
                  } finally {
                     if (var84 != null) {
                        label665:
                        try {
                           var84.close();
                        } catch (Throwable var73) {
                           var75.addSuppressed(var73);
                           break label665;
                        }
                     }

                  }
               }

               if (var84 != null) {
                  var84.close();
               }
            }

            var9.put(var5, Collections.unmodifiableMap(var11));
         }

         RULES.put(var4, Collections.unmodifiableMap(var9));
      }

   }

   public Rule(String var1, String var2, String var3, Rule.PhonemeExpr var4) {
      this.pattern = var1;
      StringBuilder var5 = new StringBuilder();
      var5.append(var2);
      var5.append("$");
      this.lContext = pattern(var5.toString());
      var5 = new StringBuilder();
      var5.append("^");
      var5.append(var3);
      this.rContext = pattern(var5.toString());
      this.phoneme = var4;
   }

   private static boolean contains(CharSequence var0, char var1) {
      for(int var2 = 0; var2 < var0.length(); ++var2) {
         if (var0.charAt(var2) == var1) {
            return true;
         }
      }

      return false;
   }

   private static String createResourceName(NameType var0, RuleType var1, String var2) {
      return String.format("org/apache/commons/codec/language/bm/%s_%s_%s.txt", var0.getName(), var1.getName(), var2);
   }

   private static Scanner createScanner(String var0) {
      return new Scanner(Resources.getInputStream(String.format("org/apache/commons/codec/language/bm/%s.txt", var0)), "UTF-8");
   }

   private static Scanner createScanner(NameType var0, RuleType var1, String var2) {
      return new Scanner(Resources.getInputStream(createResourceName(var0, var1, var2)), "UTF-8");
   }

   private static boolean endsWith(CharSequence var0, CharSequence var1) {
      if (var1.length() > var0.length()) {
         return false;
      } else {
         int var3 = var0.length() - 1;

         for(int var2 = var1.length() - 1; var2 >= 0; --var2) {
            if (var0.charAt(var3) != var1.charAt(var2)) {
               return false;
            }

            --var3;
         }

         return true;
      }
   }

   public static List getInstance(NameType var0, RuleType var1, String var2) {
      return getInstance(var0, var1, Languages.LanguageSet.from(new HashSet(Arrays.asList(var2))));
   }

   public static List getInstance(NameType var0, RuleType var1, Languages.LanguageSet var2) {
      Map var4 = getInstanceMap(var0, var1, var2);
      ArrayList var3 = new ArrayList();
      Iterator var5 = var4.values().iterator();

      while(var5.hasNext()) {
         var3.addAll((List)var5.next());
      }

      return var3;
   }

   public static Map getInstanceMap(NameType var0, RuleType var1, String var2) {
      Map var3 = (Map)((Map)((Map)RULES.get(var0)).get(var1)).get(var2);
      if (var3 != null) {
         return var3;
      } else {
         throw new IllegalArgumentException(String.format("No rules found for %s, %s, %s.", var0.getName(), var1.getName(), var2));
      }
   }

   public static Map getInstanceMap(NameType var0, RuleType var1, Languages.LanguageSet var2) {
      return var2.isSingleton() ? getInstanceMap(var0, var1, var2.getAny()) : getInstanceMap(var0, var1, "any");
   }

   private static Rule.Phoneme parsePhoneme(String var0) {
      int var1 = var0.indexOf("[");
      if (var1 >= 0) {
         if (var0.endsWith("]")) {
            return new Rule.Phoneme(var0.substring(0, var1), Languages.LanguageSet.from(new HashSet(Arrays.asList(var0.substring(var1 + 1, var0.length() - 1).split("[+]")))));
         } else {
            throw new IllegalArgumentException("Phoneme expression contains a '[' but does not end in ']'");
         }
      } else {
         return new Rule.Phoneme(var0, Languages.ANY_LANGUAGE);
      }
   }

   private static Rule.PhonemeExpr parsePhonemeExpr(String var0) {
      if (!var0.startsWith("(")) {
         return parsePhoneme(var0);
      } else if (!var0.endsWith(")")) {
         throw new IllegalArgumentException("Phoneme starts with '(' so must end with ')'");
      } else {
         ArrayList var3 = new ArrayList();
         var0 = var0.substring(1, var0.length() - 1);
         String[] var4 = var0.split("[|]");
         int var2 = var4.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var3.add(parsePhoneme(var4[var1]));
         }

         if (var0.startsWith("|") || var0.endsWith("|")) {
            var3.add(new Rule.Phoneme("", Languages.ANY_LANGUAGE));
         }

         return new Rule.PhonemeList(var3);
      }
   }

   private static Map parseRules(Scanner var0, final String var1) {
      HashMap var8 = new HashMap();
      final int var3 = 0;
      boolean var2 = false;

      while(true) {
         while(true) {
            String var5;
            final String var6;
            final String var7;
            label409:
            do {
               while(true) {
                  while(var0.hasNextLine()) {
                     ++var3;
                     var7 = var0.nextLine();
                     if (!var2) {
                        if (!var7.startsWith("/*")) {
                           int var4 = var7.indexOf("//");
                           var6 = var7;
                           if (var4 >= 0) {
                              var6 = var7.substring(0, var4);
                           }

                           var5 = var6.trim();
                           continue label409;
                        }

                        var2 = true;
                     } else if (var7.endsWith("*/")) {
                        var2 = false;
                     }
                  }

                  return var8;
               }
            } while(var5.length() == 0);

            StringBuilder var39;
            if (!var5.startsWith("#include")) {
               String[] var42 = var5.split("\\s+");
               if (var42.length != 4) {
                  var39 = new StringBuilder();
                  var39.append("Malformed rule statement split into ");
                  var39.append(var42.length);
                  var39.append(" parts: ");
                  var39.append(var7);
                  var39.append(" in ");
                  var39.append(var1);
                  throw new IllegalArgumentException(var39.toString());
               }

               IllegalArgumentException var40;
               label444: {
                  final String var9;
                  Rule.PhonemeExpr var43;
                  try {
                     var6 = stripQuotes(var42[0]);
                     var7 = stripQuotes(var42[1]);
                     var9 = stripQuotes(var42[2]);
                     var43 = parsePhonemeExpr(stripQuotes(var42[3]));
                  } catch (IllegalArgumentException var37) {
                     var40 = var37;
                     break label444;
                  }

                  IllegalArgumentException var10000;
                  label445: {
                     boolean var10001;
                     List var47;
                     Rule var48;
                     try {
                        var48 = new Rule(var6, var7, var9, var43) {
                           private final String loc = var1;
                           private final int myLine = var3;

                           public String toString() {
                              StringBuilder var1x = new StringBuilder();
                              var1x.append("Rule");
                              var1x.append("{line=");
                              var1x.append(this.myLine);
                              var1x.append(", loc='");
                              var1x.append(this.loc);
                              var1x.append('\'');
                              var1x.append(", pat='");
                              var1x.append(var6);
                              var1x.append('\'');
                              var1x.append(", lcon='");
                              var1x.append(var7);
                              var1x.append('\'');
                              var1x.append(", rcon='");
                              var1x.append(var9);
                              var1x.append('\'');
                              var1x.append('}');
                              return var1x.toString();
                           }
                        };
                        var9 = var48.pattern.substring(0, 1);
                        var47 = (List)var8.get(var9);
                     } catch (IllegalArgumentException var36) {
                        var10000 = var36;
                        var10001 = false;
                        break label445;
                     }

                     Object var44 = var47;
                     if (var47 == null) {
                        try {
                           var44 = new ArrayList();
                           var8.put(var9, var44);
                        } catch (IllegalArgumentException var35) {
                           var10000 = var35;
                           var10001 = false;
                           break label445;
                        }
                     }

                     try {
                        ((List)var44).add(var48);
                        continue;
                     } catch (IllegalArgumentException var34) {
                        var10000 = var34;
                        var10001 = false;
                     }
                  }

                  var40 = var10000;
               }

               StringBuilder var45 = new StringBuilder();
               var45.append("Problem parsing line '");
               var45.append(var3);
               var45.append("' in ");
               var45.append(var1);
               throw new IllegalStateException(var45.toString(), var40);
            } else {
               var6 = var5.substring("#include".length()).trim();
               if (var6.contains(" ")) {
                  var39 = new StringBuilder();
                  var39.append("Malformed import statement '");
                  var39.append(var7);
                  var39.append("' in ");
                  var39.append(var1);
                  throw new IllegalArgumentException(var39.toString());
               }

               Scanner var41 = createScanner(var6);

               try {
                  StringBuilder var46 = new StringBuilder();
                  var46.append(var1);
                  var46.append("->");
                  var46.append(var6);
                  var8.putAll(parseRules(var41, var46.toString()));
               } catch (Throwable var33) {
                  Throwable var38 = var33;

                  try {
                     throw var38;
                  } finally {
                     if (var41 != null) {
                        label383:
                        try {
                           var41.close();
                        } catch (Throwable var31) {
                           var33.addSuppressed(var31);
                           break label383;
                        }
                     }

                  }
               }

               if (var41 != null) {
                  var41.close();
               }
            }
         }
      }
   }

   private static Rule.RPattern pattern(String var0) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   private static boolean startsWith(CharSequence var0, CharSequence var1) {
      if (var1.length() > var0.length()) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.length(); ++var2) {
            if (var0.charAt(var2) != var1.charAt(var2)) {
               return false;
            }
         }

         return true;
      }
   }

   private static String stripQuotes(String var0) {
      String var1 = var0;
      if (var0.startsWith("\"")) {
         var1 = var0.substring(1);
      }

      var0 = var1;
      if (var1.endsWith("\"")) {
         var0 = var1.substring(0, var1.length() - 1);
      }

      return var0;
   }

   public Rule.RPattern getLContext() {
      return this.lContext;
   }

   public String getPattern() {
      return this.pattern;
   }

   public Rule.PhonemeExpr getPhoneme() {
      return this.phoneme;
   }

   public Rule.RPattern getRContext() {
      return this.rContext;
   }

   public boolean patternAndContextMatches(CharSequence var1, int var2) {
      if (var2 >= 0) {
         int var3 = var2 + this.pattern.length();
         if (var3 > var1.length()) {
            return false;
         } else if (!var1.subSequence(var2, var3).equals(this.pattern)) {
            return false;
         } else {
            return !this.rContext.isMatch(var1.subSequence(var3, var1.length())) ? false : this.lContext.isMatch(var1.subSequence(0, var2));
         }
      } else {
         throw new IndexOutOfBoundsException("Can not match pattern at negative indexes");
      }
   }

   public static final class Phoneme implements Rule.PhonemeExpr {
      public static final Comparator COMPARATOR = new Comparator() {
         public int compare(Rule.Phoneme var1, Rule.Phoneme var2) {
            for(int var3 = 0; var3 < var1.phonemeText.length(); ++var3) {
               if (var3 >= var2.phonemeText.length()) {
                  return 1;
               }

               int var4 = var1.phonemeText.charAt(var3) - var2.phonemeText.charAt(var3);
               if (var4 != 0) {
                  return var4;
               }
            }

            if (var1.phonemeText.length() < var2.phonemeText.length()) {
               return -1;
            } else {
               return 0;
            }
         }
      };
      private final Languages.LanguageSet languages;
      private final StringBuilder phonemeText;

      public Phoneme(CharSequence var1, Languages.LanguageSet var2) {
         this.phonemeText = new StringBuilder(var1);
         this.languages = var2;
      }

      public Phoneme(Rule.Phoneme var1, Rule.Phoneme var2) {
         this((CharSequence)var1.phonemeText, (Languages.LanguageSet)var1.languages);
         this.phonemeText.append(var2.phonemeText);
      }

      public Phoneme(Rule.Phoneme var1, Rule.Phoneme var2, Languages.LanguageSet var3) {
         this((CharSequence)var1.phonemeText, (Languages.LanguageSet)var3);
         this.phonemeText.append(var2.phonemeText);
      }

      public Rule.Phoneme append(CharSequence var1) {
         this.phonemeText.append(var1);
         return this;
      }

      public Languages.LanguageSet getLanguages() {
         return this.languages;
      }

      public CharSequence getPhonemeText() {
         return this.phonemeText;
      }

      public Iterable getPhonemes() {
         return Collections.singleton(this);
      }

      @Deprecated
      public Rule.Phoneme join(Rule.Phoneme var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append(this.phonemeText.toString());
         var2.append(var1.phonemeText.toString());
         return new Rule.Phoneme(var2.toString(), this.languages.restrictTo(var1.languages));
      }

      public Rule.Phoneme mergeWithLanguage(Languages.LanguageSet var1) {
         return new Rule.Phoneme(this.phonemeText.toString(), this.languages.merge(var1));
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.phonemeText.toString());
         var1.append("[");
         var1.append(this.languages);
         var1.append("]");
         return var1.toString();
      }
   }

   public interface PhonemeExpr {
      Iterable getPhonemes();
   }

   public static final class PhonemeList implements Rule.PhonemeExpr {
      private final List phonemes;

      public PhonemeList(List var1) {
         this.phonemes = var1;
      }

      public List getPhonemes() {
         return this.phonemes;
      }
   }

   public interface RPattern {
      boolean isMatch(CharSequence var1);
   }
}
