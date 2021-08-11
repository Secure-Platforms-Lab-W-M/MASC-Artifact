package org.apache.commons.codec.language.bm;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.codec.Resources;

public class Languages {
   public static final String ANY = "any";
   public static final Languages.LanguageSet ANY_LANGUAGE;
   private static final Map LANGUAGES = new EnumMap(NameType.class);
   public static final Languages.LanguageSet NO_LANGUAGES;
   private final Set languages;

   static {
      NameType[] var2 = NameType.values();
      int var1 = var2.length;

      for(int var0 = 0; var0 < var1; ++var0) {
         NameType var3 = var2[var0];
         LANGUAGES.put(var3, getInstance(langResourceName(var3)));
      }

      NO_LANGUAGES = new Languages.LanguageSet() {
         public boolean contains(String var1) {
            return false;
         }

         public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the empty language set.");
         }

         public boolean isEmpty() {
            return true;
         }

         public boolean isSingleton() {
            return false;
         }

         public Languages.LanguageSet merge(Languages.LanguageSet var1) {
            return var1;
         }

         public Languages.LanguageSet restrictTo(Languages.LanguageSet var1) {
            return this;
         }

         public String toString() {
            return "NO_LANGUAGES";
         }
      };
      ANY_LANGUAGE = new Languages.LanguageSet() {
         public boolean contains(String var1) {
            return true;
         }

         public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the any language set.");
         }

         public boolean isEmpty() {
            return false;
         }

         public boolean isSingleton() {
            return false;
         }

         public Languages.LanguageSet merge(Languages.LanguageSet var1) {
            return var1;
         }

         public Languages.LanguageSet restrictTo(Languages.LanguageSet var1) {
            return var1;
         }

         public String toString() {
            return "ANY_LANGUAGE";
         }
      };
   }

   private Languages(Set var1) {
      this.languages = var1;
   }

   public static Languages getInstance(String var0) {
      HashSet var3 = new HashSet();
      Scanner var61 = new Scanner(Resources.getInputStream(var0), "UTF-8");
      boolean var2 = false;

      Languages var62;
      label619: {
         Throwable var10000;
         while(true) {
            String var4;
            boolean var10001;
            label612: {
               try {
                  if (var61.hasNextLine()) {
                     var4 = var61.nextLine().trim();
                     break label612;
                  }
               } catch (Throwable var60) {
                  var10000 = var60;
                  var10001 = false;
                  break;
               }

               try {
                  var62 = new Languages(Collections.unmodifiableSet(var3));
                  break label619;
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break;
               }
            }

            boolean var1;
            if (var2) {
               label621: {
                  var1 = var2;

                  try {
                     if (!var4.endsWith("*/")) {
                        break label621;
                     }
                  } catch (Throwable var58) {
                     var10000 = var58;
                     var10001 = false;
                     break;
                  }

                  var1 = false;
               }
            } else {
               label596: {
                  label622: {
                     try {
                        if (var4.startsWith("/*")) {
                           break label622;
                        }
                     } catch (Throwable var57) {
                        var10000 = var57;
                        var10001 = false;
                        break;
                     }

                     var1 = var2;

                     try {
                        if (var4.length() <= 0) {
                           break label596;
                        }

                        var3.add(var4);
                     } catch (Throwable var56) {
                        var10000 = var56;
                        var10001 = false;
                        break;
                     }

                     var1 = var2;
                     break label596;
                  }

                  var1 = true;
               }
            }

            var2 = var1;
         }

         Throwable var63 = var10000;

         try {
            throw var63;
         } finally {
            label578:
            try {
               var61.close();
            } catch (Throwable var54) {
               var63.addSuppressed(var54);
               break label578;
            }

         }
      }

      var61.close();
      return var62;
   }

   public static Languages getInstance(NameType var0) {
      return (Languages)LANGUAGES.get(var0);
   }

   private static String langResourceName(NameType var0) {
      return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", var0.getName());
   }

   public Set getLanguages() {
      return this.languages;
   }

   public abstract static class LanguageSet {
      public static Languages.LanguageSet from(Set var0) {
         return (Languages.LanguageSet)(var0.isEmpty() ? Languages.NO_LANGUAGES : new Languages.SomeLanguages(var0));
      }

      public abstract boolean contains(String var1);

      public abstract String getAny();

      public abstract boolean isEmpty();

      public abstract boolean isSingleton();

      abstract Languages.LanguageSet merge(Languages.LanguageSet var1);

      public abstract Languages.LanguageSet restrictTo(Languages.LanguageSet var1);
   }

   public static final class SomeLanguages extends Languages.LanguageSet {
      private final Set languages;

      private SomeLanguages(Set var1) {
         this.languages = Collections.unmodifiableSet(var1);
      }

      // $FF: synthetic method
      SomeLanguages(Set var1, Object var2) {
         this(var1);
      }

      public boolean contains(String var1) {
         return this.languages.contains(var1);
      }

      public String getAny() {
         return (String)this.languages.iterator().next();
      }

      public Set getLanguages() {
         return this.languages;
      }

      public boolean isEmpty() {
         return this.languages.isEmpty();
      }

      public boolean isSingleton() {
         return this.languages.size() == 1;
      }

      public Languages.LanguageSet merge(Languages.LanguageSet var1) {
         if (var1 == Languages.NO_LANGUAGES) {
            return this;
         } else if (var1 == Languages.ANY_LANGUAGE) {
            return var1;
         } else {
            Languages.SomeLanguages var2 = (Languages.SomeLanguages)var1;
            HashSet var3 = new HashSet(this.languages);
            Iterator var4 = var2.languages.iterator();

            while(var4.hasNext()) {
               var3.add((String)var4.next());
            }

            return from(var3);
         }
      }

      public Languages.LanguageSet restrictTo(Languages.LanguageSet var1) {
         if (var1 == Languages.NO_LANGUAGES) {
            return var1;
         } else if (var1 == Languages.ANY_LANGUAGE) {
            return this;
         } else {
            Languages.SomeLanguages var5 = (Languages.SomeLanguages)var1;
            HashSet var2 = new HashSet(Math.min(this.languages.size(), var5.languages.size()));
            Iterator var3 = this.languages.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               if (var5.languages.contains(var4)) {
                  var2.add(var4);
               }
            }

            return from(var2);
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Languages(");
         var1.append(this.languages.toString());
         var1.append(")");
         return var1.toString();
      }
   }
}
