package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class PhoneticEngine {
   private static final int DEFAULT_MAX_PHONEMES = 20;
   private static final Map NAME_PREFIXES;
   private final boolean concat;
   private final Lang lang;
   private final int maxPhonemes;
   private final NameType nameType;
   private final RuleType ruleType;

   static {
      EnumMap var0 = new EnumMap(NameType.class);
      NAME_PREFIXES = var0;
      var0.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList("bar", "ben", "da", "de", "van", "von"))));
      NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList("al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
      NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList("da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
   }

   public PhoneticEngine(NameType var1, RuleType var2, boolean var3) {
      this(var1, var2, var3, 20);
   }

   public PhoneticEngine(NameType var1, RuleType var2, boolean var3, int var4) {
      if (var2 != RuleType.RULES) {
         this.nameType = var1;
         this.ruleType = var2;
         this.concat = var3;
         this.lang = Lang.instance(var1);
         this.maxPhonemes = var4;
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("ruleType must not be ");
         var5.append(RuleType.RULES);
         throw new IllegalArgumentException(var5.toString());
      }
   }

   private PhoneticEngine.PhonemeBuilder applyFinalRules(PhoneticEngine.PhonemeBuilder var1, Map var2) {
      Objects.requireNonNull(var2, "finalRules");
      if (var2.isEmpty()) {
         return var1;
      } else {
         TreeMap var5 = new TreeMap(Rule.Phoneme.COMPARATOR);
         Iterator var6 = var1.getPhonemes().iterator();

         while(var6.hasNext()) {
            Rule.Phoneme var7 = (Rule.Phoneme)var6.next();
            var1 = PhoneticEngine.PhonemeBuilder.empty(var7.getLanguages());
            String var10 = var7.getPhonemeText().toString();

            PhoneticEngine.RulesApplication var8;
            for(int var3 = 0; var3 < var10.length(); var3 = var8.getI()) {
               var8 = (new PhoneticEngine.RulesApplication(var2, var10, var1, var3, this.maxPhonemes)).invoke();
               boolean var4 = var8.isFound();
               var1 = var8.getPhonemeBuilder();
               if (!var4) {
                  var1.append(var10.subSequence(var3, var3 + 1));
               }
            }

            Iterator var9 = var1.getPhonemes().iterator();

            while(var9.hasNext()) {
               var7 = (Rule.Phoneme)var9.next();
               if (var5.containsKey(var7)) {
                  var7 = ((Rule.Phoneme)var5.remove(var7)).mergeWithLanguage(var7.getLanguages());
                  var5.put(var7, var7);
               } else {
                  var5.put(var7, var7);
               }
            }
         }

         return new PhoneticEngine.PhonemeBuilder(var5.keySet());
      }
   }

   private static String join(Iterable var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      Iterator var3 = var0.iterator();
      if (var3.hasNext()) {
         var2.append((String)var3.next());
      }

      while(var3.hasNext()) {
         var2.append(var1);
         var2.append((String)var3.next());
      }

      return var2.toString();
   }

   public String encode(String var1) {
      return this.encode(var1, this.lang.guessLanguages(var1));
   }

   public String encode(String var1, Languages.LanguageSet var2) {
      Map var4 = Rule.getInstanceMap(this.nameType, RuleType.RULES, var2);
      Map var5 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
      Map var6 = Rule.getInstanceMap(this.nameType, this.ruleType, var2);
      String var7 = var1.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
      Iterator var8;
      StringBuilder var15;
      if (this.nameType == NameType.GENERIC) {
         String var12;
         StringBuilder var17;
         if (var7.length() >= 2 && var7.substring(0, 2).equals("d'")) {
            var1 = var7.substring(2);
            var15 = new StringBuilder();
            var15.append("d");
            var15.append(var1);
            var12 = var15.toString();
            var17 = new StringBuilder();
            var17.append("(");
            var17.append(this.encode(var1));
            var17.append(")-(");
            var17.append(this.encode(var12));
            var17.append(")");
            return var17.toString();
         }

         var8 = ((Set)NAME_PREFIXES.get(this.nameType)).iterator();

         while(var8.hasNext()) {
            var1 = (String)var8.next();
            StringBuilder var9 = new StringBuilder();
            var9.append(var1);
            var9.append(" ");
            if (var7.startsWith(var9.toString())) {
               var12 = var7.substring(var1.length() + 1);
               var17 = new StringBuilder();
               var17.append(var1);
               var17.append(var12);
               var1 = var17.toString();
               var17 = new StringBuilder();
               var17.append("(");
               var17.append(this.encode(var12));
               var17.append(")-(");
               var17.append(this.encode(var1));
               var17.append(")");
               return var17.toString();
            }
         }
      }

      List var19 = Arrays.asList(var7.split("\\s+"));
      ArrayList var10 = new ArrayList();
      int var3 = null.$SwitchMap$org$apache$commons$codec$language$bm$NameType[this.nameType.ordinal()];
      if (var3 != 1) {
         if (var3 != 2) {
            if (var3 != 3) {
               StringBuilder var11 = new StringBuilder();
               var11.append("Unreachable case: ");
               var11.append(this.nameType);
               throw new IllegalStateException(var11.toString());
            }

            var10.addAll(var19);
         } else {
            var10.addAll(var19);
            var10.removeAll((Collection)NAME_PREFIXES.get(this.nameType));
         }
      } else {
         var8 = var19.iterator();

         while(var8.hasNext()) {
            String[] var20 = ((String)var8.next()).split("'");
            var10.add(var20[var20.length - 1]);
         }

         var10.removeAll((Collection)NAME_PREFIXES.get(this.nameType));
      }

      if (this.concat) {
         var1 = join(var10, " ");
      } else {
         if (var10.size() != 1) {
            var15 = new StringBuilder();
            Iterator var16 = var10.iterator();

            while(var16.hasNext()) {
               String var18 = (String)var16.next();
               var15.append("-");
               var15.append(this.encode(var18));
            }

            return var15.substring(1);
         }

         var1 = (String)var19.iterator().next();
      }

      PhoneticEngine.PhonemeBuilder var13 = PhoneticEngine.PhonemeBuilder.empty(var2);

      PhoneticEngine.RulesApplication var14;
      for(var3 = 0; var3 < var1.length(); var13 = var14.getPhonemeBuilder()) {
         var14 = (new PhoneticEngine.RulesApplication(var4, var1, var13, var3, this.maxPhonemes)).invoke();
         var3 = var14.getI();
      }

      return this.applyFinalRules(this.applyFinalRules(var13, var5), var6).makeString();
   }

   public Lang getLang() {
      return this.lang;
   }

   public int getMaxPhonemes() {
      return this.maxPhonemes;
   }

   public NameType getNameType() {
      return this.nameType;
   }

   public RuleType getRuleType() {
      return this.ruleType;
   }

   public boolean isConcat() {
      return this.concat;
   }

   static final class PhonemeBuilder {
      private final Set phonemes;

      private PhonemeBuilder(Set var1) {
         this.phonemes = var1;
      }

      // $FF: synthetic method
      PhonemeBuilder(Set var1, Object var2) {
         this(var1);
      }

      private PhonemeBuilder(Rule.Phoneme var1) {
         LinkedHashSet var2 = new LinkedHashSet();
         this.phonemes = var2;
         var2.add(var1);
      }

      public static PhoneticEngine.PhonemeBuilder empty(Languages.LanguageSet var0) {
         return new PhoneticEngine.PhonemeBuilder(new Rule.Phoneme("", var0));
      }

      public void append(CharSequence var1) {
         Iterator var2 = this.phonemes.iterator();

         while(var2.hasNext()) {
            ((Rule.Phoneme)var2.next()).append(var1);
         }

      }

      public void apply(Rule.PhonemeExpr var1, int var2) {
         LinkedHashSet var3 = new LinkedHashSet(var2);
         Iterator var4 = this.phonemes.iterator();

         label25:
         while(var4.hasNext()) {
            Rule.Phoneme var5 = (Rule.Phoneme)var4.next();
            Iterator var6 = var1.getPhonemes().iterator();

            while(var6.hasNext()) {
               Rule.Phoneme var7 = (Rule.Phoneme)var6.next();
               Languages.LanguageSet var8 = var5.getLanguages().restrictTo(var7.getLanguages());
               if (!var8.isEmpty()) {
                  var7 = new Rule.Phoneme(var5, var7, var8);
                  if (var3.size() < var2) {
                     var3.add(var7);
                     if (var3.size() >= var2) {
                        break label25;
                     }
                  }
               }
            }
         }

         this.phonemes.clear();
         this.phonemes.addAll(var3);
      }

      public Set getPhonemes() {
         return this.phonemes;
      }

      public String makeString() {
         StringBuilder var1 = new StringBuilder();

         Rule.Phoneme var3;
         for(Iterator var2 = this.phonemes.iterator(); var2.hasNext(); var1.append(var3.getPhonemeText())) {
            var3 = (Rule.Phoneme)var2.next();
            if (var1.length() > 0) {
               var1.append("|");
            }
         }

         return var1.toString();
      }
   }

   private static final class RulesApplication {
      private final Map finalRules;
      private boolean found;
      // $FF: renamed from: i int
      private int field_97;
      private final CharSequence input;
      private final int maxPhonemes;
      private final PhoneticEngine.PhonemeBuilder phonemeBuilder;

      public RulesApplication(Map var1, CharSequence var2, PhoneticEngine.PhonemeBuilder var3, int var4, int var5) {
         Objects.requireNonNull(var1, "finalRules");
         this.finalRules = var1;
         this.phonemeBuilder = var3;
         this.input = var2;
         this.field_97 = var4;
         this.maxPhonemes = var5;
      }

      public int getI() {
         return this.field_97;
      }

      public PhoneticEngine.PhonemeBuilder getPhonemeBuilder() {
         return this.phonemeBuilder;
      }

      public PhoneticEngine.RulesApplication invoke() {
         this.found = false;
         int var1 = 1;
         byte var2 = 1;
         Map var4 = this.finalRules;
         CharSequence var5 = this.input;
         int var3 = this.field_97;
         List var6 = (List)var4.get(var5.subSequence(var3, var3 + 1));
         if (var6 != null) {
            Iterator var7 = var6.iterator();
            var1 = var2;

            while(var7.hasNext()) {
               Rule var8 = (Rule)var7.next();
               var1 = var8.getPattern().length();
               if (var8.patternAndContextMatches(this.input, this.field_97)) {
                  this.phonemeBuilder.apply(var8.getPhoneme(), this.maxPhonemes);
                  this.found = true;
                  break;
               }
            }
         }

         if (!this.found) {
            var1 = 1;
         }

         this.field_97 += var1;
         return this;
      }

      public boolean isFound() {
         return this.found;
      }
   }
}
