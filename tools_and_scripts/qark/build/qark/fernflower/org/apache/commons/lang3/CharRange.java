package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class CharRange implements Iterable, Serializable {
   private static final long serialVersionUID = 8270183163158333422L;
   private final char end;
   private transient String iToString;
   private final boolean negated;
   private final char start;

   private CharRange(char var1, char var2, boolean var3) {
      char var5 = var1;
      char var4 = var2;
      if (var1 > var2) {
         var4 = var1;
         var5 = var2;
      }

      this.start = var5;
      this.end = var4;
      this.negated = var3;
   }

   // $FF: renamed from: is (char) org.apache.commons.lang3.CharRange
   public static CharRange method_3(char var0) {
      return new CharRange(var0, var0, false);
   }

   public static CharRange isIn(char var0, char var1) {
      return new CharRange(var0, var1, false);
   }

   public static CharRange isNot(char var0) {
      return new CharRange(var0, var0, true);
   }

   public static CharRange isNotIn(char var0, char var1) {
      return new CharRange(var0, var1, true);
   }

   public boolean contains(char var1) {
      boolean var2;
      if (var1 >= this.start && var1 <= this.end) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2 != this.negated;
   }

   public boolean contains(CharRange var1) {
      boolean var3 = true;
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "The Range must not be null");
      if (this.negated) {
         if (var1.negated) {
            return this.start >= var1.start && this.end <= var1.end;
         } else {
            var2 = var3;
            if (var1.end >= this.start) {
               if (var1.start > this.end) {
                  return true;
               }

               var2 = false;
            }

            return var2;
         }
      } else if (var1.negated) {
         return this.start == 0 && this.end == '\uffff';
      } else {
         return this.start <= var1.start && this.end >= var1.end;
      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CharRange)) {
         return false;
      } else {
         CharRange var2 = (CharRange)var1;
         return this.start == var2.start && this.end == var2.end && this.negated == var2.negated;
      }
   }

   public char getEnd() {
      return this.end;
   }

   public char getStart() {
      return this.start;
   }

   public int hashCode() {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:632)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public boolean isNegated() {
      return this.negated;
   }

   public Iterator iterator() {
      return new CharRange.CharacterIterator(this);
   }

   public String toString() {
      if (this.iToString == null) {
         StringBuilder var1 = new StringBuilder(4);
         if (this.isNegated()) {
            var1.append('^');
         }

         var1.append(this.start);
         if (this.start != this.end) {
            var1.append('-');
            var1.append(this.end);
         }

         this.iToString = var1.toString();
      }

      return this.iToString;
   }

   private static class CharacterIterator implements Iterator {
      private char current;
      private boolean hasNext;
      private final CharRange range;

      private CharacterIterator(CharRange var1) {
         this.range = var1;
         this.hasNext = true;
         if (var1.negated) {
            if (this.range.start == 0) {
               if (this.range.end == '\uffff') {
                  this.hasNext = false;
               } else {
                  this.current = (char)(this.range.end + 1);
               }
            } else {
               this.current = 0;
            }
         } else {
            this.current = this.range.start;
         }
      }

      // $FF: synthetic method
      CharacterIterator(CharRange var1, Object var2) {
         this(var1);
      }

      private void prepareNext() {
         if (this.range.negated) {
            char var1 = this.current;
            if (var1 == '\uffff') {
               this.hasNext = false;
            } else if (var1 + 1 == this.range.start) {
               if (this.range.end == '\uffff') {
                  this.hasNext = false;
               } else {
                  this.current = (char)(this.range.end + 1);
               }
            } else {
               ++this.current;
            }
         } else if (this.current < this.range.end) {
            ++this.current;
         } else {
            this.hasNext = false;
         }
      }

      public boolean hasNext() {
         return this.hasNext;
      }

      public Character next() {
         if (this.hasNext) {
            char var1 = this.current;
            this.prepareNext();
            return var1;
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
