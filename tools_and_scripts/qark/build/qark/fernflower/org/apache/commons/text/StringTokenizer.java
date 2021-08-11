package org.apache.commons.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;

public class StringTokenizer implements ListIterator, Cloneable {
   private static final StringTokenizer CSV_TOKENIZER_PROTOTYPE;
   private static final StringTokenizer TSV_TOKENIZER_PROTOTYPE;
   private char[] chars;
   private StringMatcher delimMatcher;
   private boolean emptyAsNull;
   private boolean ignoreEmptyTokens;
   private StringMatcher ignoredMatcher;
   private StringMatcher quoteMatcher;
   private int tokenPos;
   private String[] tokens;
   private StringMatcher trimmerMatcher;

   static {
      StringTokenizer var0 = new StringTokenizer();
      CSV_TOKENIZER_PROTOTYPE = var0;
      var0.setDelimiterMatcher(StringMatcherFactory.INSTANCE.commaMatcher());
      CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StringMatcherFactory.INSTANCE.doubleQuoteMatcher());
      CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StringMatcherFactory.INSTANCE.noneMatcher());
      CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StringMatcherFactory.INSTANCE.trimMatcher());
      CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
      CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
      var0 = new StringTokenizer();
      TSV_TOKENIZER_PROTOTYPE = var0;
      var0.setDelimiterMatcher(StringMatcherFactory.INSTANCE.tabMatcher());
      TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StringMatcherFactory.INSTANCE.doubleQuoteMatcher());
      TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StringMatcherFactory.INSTANCE.noneMatcher());
      TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StringMatcherFactory.INSTANCE.trimMatcher());
      TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
      TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
   }

   public StringTokenizer() {
      this.delimMatcher = StringMatcherFactory.INSTANCE.splitMatcher();
      this.quoteMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.ignoredMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.trimmerMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.emptyAsNull = false;
      this.ignoreEmptyTokens = true;
      this.chars = null;
   }

   public StringTokenizer(String var1) {
      this.delimMatcher = StringMatcherFactory.INSTANCE.splitMatcher();
      this.quoteMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.ignoredMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.trimmerMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.emptyAsNull = false;
      this.ignoreEmptyTokens = true;
      if (var1 != null) {
         this.chars = var1.toCharArray();
      } else {
         this.chars = null;
      }
   }

   public StringTokenizer(String var1, char var2) {
      this(var1);
      this.setDelimiterChar(var2);
   }

   public StringTokenizer(String var1, char var2, char var3) {
      this(var1, var2);
      this.setQuoteChar(var3);
   }

   public StringTokenizer(String var1, String var2) {
      this(var1);
      this.setDelimiterString(var2);
   }

   public StringTokenizer(String var1, StringMatcher var2) {
      this(var1);
      this.setDelimiterMatcher(var2);
   }

   public StringTokenizer(String var1, StringMatcher var2, StringMatcher var3) {
      this(var1, var2);
      this.setQuoteMatcher(var3);
   }

   public StringTokenizer(char[] var1) {
      this.delimMatcher = StringMatcherFactory.INSTANCE.splitMatcher();
      this.quoteMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.ignoredMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.trimmerMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
      this.emptyAsNull = false;
      this.ignoreEmptyTokens = true;
      if (var1 == null) {
         this.chars = null;
      } else {
         this.chars = (char[])var1.clone();
      }
   }

   public StringTokenizer(char[] var1, char var2) {
      this(var1);
      this.setDelimiterChar(var2);
   }

   public StringTokenizer(char[] var1, char var2, char var3) {
      this(var1, var2);
      this.setQuoteChar(var3);
   }

   public StringTokenizer(char[] var1, String var2) {
      this(var1);
      this.setDelimiterString(var2);
   }

   public StringTokenizer(char[] var1, StringMatcher var2) {
      this(var1);
      this.setDelimiterMatcher(var2);
   }

   public StringTokenizer(char[] var1, StringMatcher var2, StringMatcher var3) {
      this(var1, var2);
      this.setQuoteMatcher(var3);
   }

   private void addToken(List var1, String var2) {
      String var3;
      label19: {
         if (var2 != null) {
            var3 = var2;
            if (var2.length() != 0) {
               break label19;
            }
         }

         if (this.isIgnoreEmptyTokens()) {
            return;
         }

         var3 = var2;
         if (this.isEmptyTokenAsNull()) {
            var3 = null;
         }
      }

      var1.add(var3);
   }

   private void checkTokenized() {
      if (this.tokens == null) {
         char[] var1 = this.chars;
         List var2;
         if (var1 == null) {
            var2 = this.tokenize((char[])null, 0, 0);
            this.tokens = (String[])var2.toArray(new String[var2.size()]);
            return;
         }

         var2 = this.tokenize(var1, 0, var1.length);
         this.tokens = (String[])var2.toArray(new String[var2.size()]);
      }

   }

   private static StringTokenizer getCSVClone() {
      return (StringTokenizer)CSV_TOKENIZER_PROTOTYPE.clone();
   }

   public static StringTokenizer getCSVInstance() {
      return getCSVClone();
   }

   public static StringTokenizer getCSVInstance(String var0) {
      StringTokenizer var1 = getCSVClone();
      var1.reset(var0);
      return var1;
   }

   public static StringTokenizer getCSVInstance(char[] var0) {
      StringTokenizer var1 = getCSVClone();
      var1.reset(var0);
      return var1;
   }

   private static StringTokenizer getTSVClone() {
      return (StringTokenizer)TSV_TOKENIZER_PROTOTYPE.clone();
   }

   public static StringTokenizer getTSVInstance() {
      return getTSVClone();
   }

   public static StringTokenizer getTSVInstance(String var0) {
      StringTokenizer var1 = getTSVClone();
      var1.reset(var0);
      return var1;
   }

   public static StringTokenizer getTSVInstance(char[] var0) {
      StringTokenizer var1 = getTSVClone();
      var1.reset(var0);
      return var1;
   }

   private boolean isQuote(char[] var1, int var2, int var3, int var4, int var5) {
      for(int var6 = 0; var6 < var5; ++var6) {
         if (var2 + var6 >= var3 || var1[var2 + var6] != var1[var4 + var6]) {
            return false;
         }
      }

      return true;
   }

   private int readNextToken(char[] var1, int var2, int var3, TextStringBuilder var4, List var5) {
      while(true) {
         int var6;
         if (var2 < var3) {
            var6 = Math.max(this.getIgnoredMatcher().isMatch(var1, var2, var2, var3), this.getTrimmerMatcher().isMatch(var1, var2, var2, var3));
            if (var6 != 0 && this.getDelimiterMatcher().isMatch(var1, var2, var2, var3) <= 0 && this.getQuoteMatcher().isMatch(var1, var2, var2, var3) <= 0) {
               var2 += var6;
               continue;
            }
         }

         if (var2 >= var3) {
            this.addToken(var5, "");
            return -1;
         }

         var6 = this.getDelimiterMatcher().isMatch(var1, var2, var2, var3);
         if (var6 > 0) {
            this.addToken(var5, "");
            return var2 + var6;
         }

         var6 = this.getQuoteMatcher().isMatch(var1, var2, var2, var3);
         if (var6 > 0) {
            return this.readWithQuotes(var1, var2 + var6, var3, var4, var5, var2, var6);
         }

         return this.readWithQuotes(var1, var2, var3, var4, var5, 0, 0);
      }
   }

   private int readWithQuotes(char[] var1, int var2, int var3, TextStringBuilder var4, List var5, int var6, int var7) {
      var4.clear();
      boolean var8;
      if (var7 > 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var9 = var2;
      byte var11 = 0;
      boolean var10 = var8;
      int var12 = var11;

      while(true) {
         while(var9 < var3) {
            if (var10) {
               if (this.isQuote(var1, var9, var3, var6, var7)) {
                  if (this.isQuote(var1, var9 + var7, var3, var6, var7)) {
                     var4.append(var1, var9, var7);
                     var9 += var7 * 2;
                     var12 = var4.size();
                  } else {
                     var10 = false;
                     var9 += var7;
                  }
               } else {
                  var4.append(var1[var9]);
                  var12 = var4.size();
                  ++var9;
               }
            } else {
               int var13 = this.getDelimiterMatcher().isMatch(var1, var9, var2, var3);
               if (var13 > 0) {
                  this.addToken(var5, var4.substring(0, var12));
                  return var9 + var13;
               }

               if (var7 > 0 && this.isQuote(var1, var9, var3, var6, var7)) {
                  var10 = true;
                  var9 += var7;
               } else {
                  var13 = this.getIgnoredMatcher().isMatch(var1, var9, var2, var3);
                  if (var13 > 0) {
                     var9 += var13;
                  } else {
                     var13 = this.getTrimmerMatcher().isMatch(var1, var9, var2, var3);
                     if (var13 > 0) {
                        var4.append(var1, var9, var13);
                        var9 += var13;
                     } else {
                        var4.append(var1[var9]);
                        var12 = var4.size();
                        ++var9;
                     }
                  }
               }
            }
         }

         this.addToken(var5, var4.substring(0, var12));
         return -1;
      }
   }

   public void add(String var1) {
      throw new UnsupportedOperationException("add() is unsupported");
   }

   public Object clone() {
      try {
         Object var1 = this.cloneReset();
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   Object cloneReset() throws CloneNotSupportedException {
      StringTokenizer var1 = (StringTokenizer)super.clone();
      char[] var2 = var1.chars;
      if (var2 != null) {
         var1.chars = (char[])var2.clone();
      }

      var1.reset();
      return var1;
   }

   public String getContent() {
      return this.chars == null ? null : new String(this.chars);
   }

   public StringMatcher getDelimiterMatcher() {
      return this.delimMatcher;
   }

   public StringMatcher getIgnoredMatcher() {
      return this.ignoredMatcher;
   }

   public StringMatcher getQuoteMatcher() {
      return this.quoteMatcher;
   }

   public String[] getTokenArray() {
      this.checkTokenized();
      return (String[])this.tokens.clone();
   }

   public List getTokenList() {
      this.checkTokenized();
      ArrayList var1 = new ArrayList(this.tokens.length);
      Collections.addAll(var1, this.tokens);
      return var1;
   }

   public StringMatcher getTrimmerMatcher() {
      return this.trimmerMatcher;
   }

   public boolean hasNext() {
      this.checkTokenized();
      return this.tokenPos < this.tokens.length;
   }

   public boolean hasPrevious() {
      this.checkTokenized();
      return this.tokenPos > 0;
   }

   public boolean isEmptyTokenAsNull() {
      return this.emptyAsNull;
   }

   public boolean isIgnoreEmptyTokens() {
      return this.ignoreEmptyTokens;
   }

   public String next() {
      if (this.hasNext()) {
         String[] var2 = this.tokens;
         int var1 = this.tokenPos++;
         return var2[var1];
      } else {
         throw new NoSuchElementException();
      }
   }

   public int nextIndex() {
      return this.tokenPos;
   }

   public String nextToken() {
      if (this.hasNext()) {
         String[] var2 = this.tokens;
         int var1 = this.tokenPos++;
         return var2[var1];
      } else {
         return null;
      }
   }

   public String previous() {
      if (this.hasPrevious()) {
         String[] var2 = this.tokens;
         int var1 = this.tokenPos - 1;
         this.tokenPos = var1;
         return var2[var1];
      } else {
         throw new NoSuchElementException();
      }
   }

   public int previousIndex() {
      return this.tokenPos - 1;
   }

   public String previousToken() {
      if (this.hasPrevious()) {
         String[] var2 = this.tokens;
         int var1 = this.tokenPos - 1;
         this.tokenPos = var1;
         return var2[var1];
      } else {
         return null;
      }
   }

   public void remove() {
      throw new UnsupportedOperationException("remove() is unsupported");
   }

   public StringTokenizer reset() {
      this.tokenPos = 0;
      this.tokens = null;
      return this;
   }

   public StringTokenizer reset(String var1) {
      this.reset();
      if (var1 != null) {
         this.chars = var1.toCharArray();
         return this;
      } else {
         this.chars = null;
         return this;
      }
   }

   public StringTokenizer reset(char[] var1) {
      this.reset();
      if (var1 != null) {
         this.chars = (char[])var1.clone();
         return this;
      } else {
         this.chars = null;
         return this;
      }
   }

   public void set(String var1) {
      throw new UnsupportedOperationException("set() is unsupported");
   }

   public StringTokenizer setDelimiterChar(char var1) {
      return this.setDelimiterMatcher(StringMatcherFactory.INSTANCE.charMatcher(var1));
   }

   public StringTokenizer setDelimiterMatcher(StringMatcher var1) {
      if (var1 == null) {
         this.delimMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
         return this;
      } else {
         this.delimMatcher = var1;
         return this;
      }
   }

   public StringTokenizer setDelimiterString(String var1) {
      return this.setDelimiterMatcher(StringMatcherFactory.INSTANCE.stringMatcher(var1));
   }

   public StringTokenizer setEmptyTokenAsNull(boolean var1) {
      this.emptyAsNull = var1;
      return this;
   }

   public StringTokenizer setIgnoreEmptyTokens(boolean var1) {
      this.ignoreEmptyTokens = var1;
      return this;
   }

   public StringTokenizer setIgnoredChar(char var1) {
      return this.setIgnoredMatcher(StringMatcherFactory.INSTANCE.charMatcher(var1));
   }

   public StringTokenizer setIgnoredMatcher(StringMatcher var1) {
      if (var1 != null) {
         this.ignoredMatcher = var1;
      }

      return this;
   }

   public StringTokenizer setQuoteChar(char var1) {
      return this.setQuoteMatcher(StringMatcherFactory.INSTANCE.charMatcher(var1));
   }

   public StringTokenizer setQuoteMatcher(StringMatcher var1) {
      if (var1 != null) {
         this.quoteMatcher = var1;
      }

      return this;
   }

   public StringTokenizer setTrimmerMatcher(StringMatcher var1) {
      if (var1 != null) {
         this.trimmerMatcher = var1;
      }

      return this;
   }

   public int size() {
      this.checkTokenized();
      return this.tokens.length;
   }

   public String toString() {
      if (this.tokens == null) {
         return "StringTokenizer[not tokenized yet]";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("StringTokenizer");
         var1.append(this.getTokenList());
         return var1.toString();
      }
   }

   protected List tokenize(char[] var1, int var2, int var3) {
      if (var1 != null && var3 != 0) {
         TextStringBuilder var5 = new TextStringBuilder();
         ArrayList var6 = new ArrayList();

         while(var2 >= 0 && var2 < var3) {
            int var4 = this.readNextToken(var1, var2, var3, var5, var6);
            var2 = var4;
            if (var4 >= var3) {
               this.addToken(var6, "");
               var2 = var4;
            }
         }

         return var6;
      } else {
         return Collections.emptyList();
      }
   }
}
