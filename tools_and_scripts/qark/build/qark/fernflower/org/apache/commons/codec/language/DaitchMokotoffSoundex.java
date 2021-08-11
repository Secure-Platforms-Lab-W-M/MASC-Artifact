package org.apache.commons.codec.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.Resources;
import org.apache.commons.codec.StringEncoder;

public class DaitchMokotoffSoundex implements StringEncoder {
   private static final String COMMENT = "//";
   private static final String DOUBLE_QUOTE = "\"";
   private static final Map FOLDINGS = new HashMap();
   private static final int MAX_LENGTH = 6;
   private static final String MULTILINE_COMMENT_END = "*/";
   private static final String MULTILINE_COMMENT_START = "/*";
   private static final String RESOURCE_FILE = "org/apache/commons/codec/language/dmrules.txt";
   private static final Map RULES = new HashMap();
   private final boolean folding;

   static {
      Scanner var2 = new Scanner(Resources.getInputStream("org/apache/commons/codec/language/dmrules.txt"), "UTF-8");

      try {
         parseRules(var2, "org/apache/commons/codec/language/dmrules.txt", RULES, FOLDINGS);
      } catch (Throwable var14) {
         Throwable var0 = var14;

         try {
            throw var0;
         } finally {
            label118:
            try {
               var2.close();
            } catch (Throwable var12) {
               var14.addSuppressed(var12);
               break label118;
            }

         }
      }

      var2.close();
      Iterator var15 = RULES.entrySet().iterator();

      while(var15.hasNext()) {
         Collections.sort((List)((Entry)var15.next()).getValue(), new Comparator() {
            public int compare(DaitchMokotoffSoundex.Rule var1, DaitchMokotoffSoundex.Rule var2) {
               return var2.getPatternLength() - var1.getPatternLength();
            }
         });
      }

   }

   public DaitchMokotoffSoundex() {
      this(true);
   }

   public DaitchMokotoffSoundex(boolean var1) {
      this.folding = var1;
   }

   private String cleanup(String var1) {
      StringBuilder var6 = new StringBuilder();
      char[] var7 = var1.toCharArray();
      int var5 = var7.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         char var2 = var7[var4];
         if (!Character.isWhitespace(var2)) {
            char var3 = Character.toLowerCase(var2);
            var2 = var3;
            if (this.folding) {
               var2 = var3;
               if (FOLDINGS.containsKey(var3)) {
                  var2 = (Character)FOLDINGS.get(var3);
               }
            }

            var6.append(var2);
         }
      }

      return var6.toString();
   }

   private static void parseRules(Scanner var0, String var1, Map var2, Map var3) {
      int var5 = 0;
      boolean var6 = false;

      while(true) {
         String var8;
         String var9;
         String var10;
         label58:
         do {
            while(true) {
               while(var0.hasNextLine()) {
                  ++var5;
                  var10 = var0.nextLine();
                  if (!var6) {
                     if (!var10.startsWith("/*")) {
                        int var7 = var10.indexOf("//");
                        var9 = var10;
                        if (var7 >= 0) {
                           var9 = var10.substring(0, var7);
                        }

                        var8 = var9.trim();
                        continue label58;
                     }

                     var6 = true;
                  } else if (var10.endsWith("*/")) {
                     var6 = false;
                  }
               }

               return;
            }
         } while(var8.length() == 0);

         StringBuilder var14;
         String[] var17;
         if (var8.contains("=")) {
            var17 = var8.split("=");
            if (var17.length != 2) {
               var14 = new StringBuilder();
               var14.append("Malformed folding statement split into ");
               var14.append(var17.length);
               var14.append(" parts: ");
               var14.append(var10);
               var14.append(" in ");
               var14.append(var1);
               throw new IllegalArgumentException(var14.toString());
            }

            var9 = var17[0];
            var8 = var17[1];
            if (var9.length() != 1 || var8.length() != 1) {
               var14 = new StringBuilder();
               var14.append("Malformed folding statement - patterns are not single characters: ");
               var14.append(var10);
               var14.append(" in ");
               var14.append(var1);
               throw new IllegalArgumentException(var14.toString());
            }

            var3.put(var9.charAt(0), var8.charAt(0));
         } else {
            var17 = var8.split("\\s+");
            if (var17.length == 4) {
               IllegalArgumentException var10000;
               label88: {
                  char var4;
                  boolean var10001;
                  Object var18;
                  DaitchMokotoffSoundex.Rule var19;
                  try {
                     var19 = new DaitchMokotoffSoundex.Rule(stripQuotes(var17[0]), stripQuotes(var17[1]), stripQuotes(var17[2]), stripQuotes(var17[3]));
                     var4 = var19.pattern.charAt(0);
                     var18 = (List)var2.get(var4);
                  } catch (IllegalArgumentException var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label88;
                  }

                  if (var18 == null) {
                     try {
                        var18 = new ArrayList();
                        var2.put(var4, var18);
                     } catch (IllegalArgumentException var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label88;
                     }
                  }

                  try {
                     ((List)var18).add(var19);
                     continue;
                  } catch (IllegalArgumentException var11) {
                     var10000 = var11;
                     var10001 = false;
                  }
               }

               IllegalArgumentException var15 = var10000;
               StringBuilder var16 = new StringBuilder();
               var16.append("Problem parsing line '");
               var16.append(var5);
               var16.append("' in ");
               var16.append(var1);
               throw new IllegalStateException(var16.toString(), var15);
            }

            var14 = new StringBuilder();
            var14.append("Malformed rule statement split into ");
            var14.append(var17.length);
            var14.append(" parts: ");
            var14.append(var10);
            var14.append(" in ");
            var14.append(var1);
            throw new IllegalArgumentException(var14.toString());
         }
      }
   }

   private String[] soundex(String var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         var1 = this.cleanup(var1);
         LinkedHashSet var13 = new LinkedHashSet();
         var13.add(new DaitchMokotoffSoundex.Branch());
         char var5 = 0;

         int var4;
         DaitchMokotoffSoundex.Branch var19;
         for(var4 = 0; var4 < var1.length(); ++var4) {
            char var3 = var1.charAt(var4);
            if (!Character.isWhitespace(var3)) {
               String var11 = var1.substring(var4);
               List var12 = (List)RULES.get(var3);
               if (var12 != null) {
                  Object var10;
                  if (var2) {
                     var10 = new ArrayList();
                  } else {
                     var10 = Collections.emptyList();
                  }

                  Iterator var21 = var12.iterator();

                  while(var21.hasNext()) {
                     DaitchMokotoffSoundex.Rule var14 = (DaitchMokotoffSoundex.Rule)var21.next();
                     if (var14.matches(var11)) {
                        if (var2) {
                           ((List)var10).clear();
                        }

                        boolean var9;
                        if (var5 == 0) {
                           var9 = true;
                        } else {
                           var9 = false;
                        }

                        String[] var15 = var14.getReplacements(var11, var9);
                        boolean var6;
                        if (var15.length > 1 && var2) {
                           var6 = true;
                        } else {
                           var6 = false;
                        }

                        Iterator var16 = var13.iterator();

                        while(var16.hasNext()) {
                           DaitchMokotoffSoundex.Branch var22 = (DaitchMokotoffSoundex.Branch)var16.next();
                           int var8 = var15.length;

                           for(int var7 = 0; var7 < var8; ++var7) {
                              String var17 = var15[var7];
                              if (var6) {
                                 var19 = var22.createBranch();
                              } else {
                                 var19 = var22;
                              }

                              if ((var5 != 'm' || var3 != 'n') && (var5 != 'n' || var3 != 'm')) {
                                 var9 = false;
                              } else {
                                 var9 = true;
                              }

                              var19.processNextReplacement(var17, var9);
                              if (!var2) {
                                 break;
                              }

                              ((List)var10).add(var19);
                           }
                        }

                        if (var2) {
                           var13.clear();
                           var13.addAll((Collection)var10);
                        }

                        var4 += var14.getPatternLength() - 1;
                        break;
                     }
                  }

                  var5 = var3;
               }
            }
         }

         String[] var18 = new String[var13.size()];
         var4 = 0;

         for(Iterator var20 = var13.iterator(); var20.hasNext(); ++var4) {
            var19 = (DaitchMokotoffSoundex.Branch)var20.next();
            var19.finish();
            var18[var4] = var19.toString();
         }

         return var18;
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

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.encode((String)var1);
      } else {
         throw new EncoderException("Parameter supplied to DaitchMokotoffSoundex encode is not of type java.lang.String");
      }
   }

   public String encode(String var1) {
      return var1 == null ? null : this.soundex(var1, false)[0];
   }

   public String soundex(String var1) {
      String[] var6 = this.soundex(var1, true);
      StringBuilder var5 = new StringBuilder();
      int var3 = 0;
      int var4 = var6.length;

      for(int var2 = 0; var2 < var4; ++var2) {
         var5.append(var6[var2]);
         ++var3;
         if (var3 < var6.length) {
            var5.append('|');
         }
      }

      return var5.toString();
   }

   private static final class Branch {
      private final StringBuilder builder;
      private String cachedString;
      private String lastReplacement;

      private Branch() {
         this.builder = new StringBuilder();
         this.lastReplacement = null;
         this.cachedString = null;
      }

      // $FF: synthetic method
      Branch(Object var1) {
         this();
      }

      public DaitchMokotoffSoundex.Branch createBranch() {
         DaitchMokotoffSoundex.Branch var1 = new DaitchMokotoffSoundex.Branch();
         var1.builder.append(this.toString());
         var1.lastReplacement = this.lastReplacement;
         return var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            return !(var1 instanceof DaitchMokotoffSoundex.Branch) ? false : this.toString().equals(((DaitchMokotoffSoundex.Branch)var1).toString());
         }
      }

      public void finish() {
         while(this.builder.length() < 6) {
            this.builder.append('0');
            this.cachedString = null;
         }

      }

      public int hashCode() {
         return this.toString().hashCode();
      }

      public void processNextReplacement(String var1, boolean var2) {
         String var4 = this.lastReplacement;
         boolean var3;
         if (var4 != null && var4.endsWith(var1) && !var2) {
            var3 = false;
         } else {
            var3 = true;
         }

         if (var3 && this.builder.length() < 6) {
            this.builder.append(var1);
            if (this.builder.length() > 6) {
               StringBuilder var5 = this.builder;
               var5.delete(6, var5.length());
            }

            this.cachedString = null;
         }

         this.lastReplacement = var1;
      }

      public String toString() {
         if (this.cachedString == null) {
            this.cachedString = this.builder.toString();
         }

         return this.cachedString;
      }
   }

   private static final class Rule {
      private final String pattern;
      private final String[] replacementAtStart;
      private final String[] replacementBeforeVowel;
      private final String[] replacementDefault;

      protected Rule(String var1, String var2, String var3, String var4) {
         this.pattern = var1;
         this.replacementAtStart = var2.split("\\|");
         this.replacementBeforeVowel = var3.split("\\|");
         this.replacementDefault = var4.split("\\|");
      }

      private boolean isVowel(char var1) {
         return var1 == 'a' || var1 == 'e' || var1 == 'i' || var1 == 'o' || var1 == 'u';
      }

      public int getPatternLength() {
         return this.pattern.length();
      }

      public String[] getReplacements(String var1, boolean var2) {
         if (var2) {
            return this.replacementAtStart;
         } else {
            int var3 = this.getPatternLength();
            if (var3 < var1.length()) {
               var2 = this.isVowel(var1.charAt(var3));
            } else {
               var2 = false;
            }

            return var2 ? this.replacementBeforeVowel : this.replacementDefault;
         }
      }

      public boolean matches(String var1) {
         return var1.startsWith(this.pattern);
      }

      public String toString() {
         return String.format("%s=(%s,%s,%s)", this.pattern, Arrays.asList(this.replacementAtStart), Arrays.asList(this.replacementBeforeVowel), Arrays.asList(this.replacementDefault));
      }
   }
}
