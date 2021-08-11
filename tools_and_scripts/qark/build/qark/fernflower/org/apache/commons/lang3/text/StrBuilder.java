package org.apache.commons.lang3.text;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.Builder;

@Deprecated
public class StrBuilder implements CharSequence, Appendable, Serializable, Builder {
   static final int CAPACITY = 32;
   private static final long serialVersionUID = 7628716375283629643L;
   protected char[] buffer;
   private String newLine;
   private String nullText;
   protected int size;

   public StrBuilder() {
      this(32);
   }

   public StrBuilder(int var1) {
      int var2 = var1;
      if (var1 <= 0) {
         var2 = 32;
      }

      this.buffer = new char[var2];
   }

   public StrBuilder(String var1) {
      if (var1 == null) {
         this.buffer = new char[32];
      } else {
         this.buffer = new char[var1.length() + 32];
         this.append(var1);
      }
   }

   private void deleteImpl(int var1, int var2, int var3) {
      char[] var4 = this.buffer;
      System.arraycopy(var4, var2, var4, var1, this.size - var2);
      this.size -= var3;
   }

   private StrBuilder replaceImpl(StrMatcher var1, String var2, int var3, int var4, int var5) {
      if (var1 == null) {
         return this;
      } else if (this.size == 0) {
         return this;
      } else {
         int var6;
         if (var2 == null) {
            var6 = 0;
         } else {
            var6 = var2.length();
         }

         int var7 = var5;
         int var10 = var4;

         int var8;
         for(var5 = var3; var5 < var10 && var7 != 0; var7 = var8) {
            int var11 = var1.isMatch(this.buffer, var5, var3, var10);
            int var9 = var5;
            var4 = var10;
            var8 = var7;
            if (var11 > 0) {
               this.replaceImpl(var5, var5 + var11, var11, var2, var6);
               var4 = var10 - var11 + var6;
               var9 = var5 + var6 - 1;
               if (var7 > 0) {
                  var8 = var7 - 1;
               } else {
                  var8 = var7;
               }
            }

            var5 = var9 + 1;
            var10 = var4;
         }

         return this;
      }
   }

   private void replaceImpl(int var1, int var2, int var3, String var4, int var5) {
      int var6 = this.size - var3 + var5;
      if (var5 != var3) {
         this.ensureCapacity(var6);
         char[] var7 = this.buffer;
         System.arraycopy(var7, var2, var7, var1 + var5, this.size - var2);
         this.size = var6;
      }

      if (var5 > 0) {
         var4.getChars(0, var5, this.buffer, var1);
      }

   }

   public StrBuilder append(char var1) {
      this.ensureCapacity(this.length() + 1);
      char[] var3 = this.buffer;
      int var2 = this.size++;
      var3[var2] = var1;
      return this;
   }

   public StrBuilder append(double var1) {
      return this.append(String.valueOf(var1));
   }

   public StrBuilder append(float var1) {
      return this.append(String.valueOf(var1));
   }

   public StrBuilder append(int var1) {
      return this.append(String.valueOf(var1));
   }

   public StrBuilder append(long var1) {
      return this.append(String.valueOf(var1));
   }

   public StrBuilder append(CharSequence var1) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var1 instanceof StrBuilder) {
         return this.append((StrBuilder)var1);
      } else if (var1 instanceof StringBuilder) {
         return this.append((StringBuilder)var1);
      } else if (var1 instanceof StringBuffer) {
         return this.append((StringBuffer)var1);
      } else {
         return var1 instanceof CharBuffer ? this.append((CharBuffer)var1) : this.append(var1.toString());
      }
   }

   public StrBuilder append(CharSequence var1, int var2, int var3) {
      return var1 == null ? this.appendNull() : this.append(var1.toString(), var2, var3);
   }

   public StrBuilder append(Object var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         return var1 instanceof CharSequence ? this.append((CharSequence)var1) : this.append(var1.toString());
      }
   }

   public StrBuilder append(String var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length();
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            var1.getChars(0, var2, this.buffer, var3);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(String var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length()) {
         if (var3 >= 0 && var2 + var3 <= var1.length()) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               var1.getChars(var2, var2 + var3, this.buffer, var4);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("length must be valid");
         }
      } else {
         throw new StringIndexOutOfBoundsException("startIndex must be valid");
      }
   }

   public StrBuilder append(String var1, Object... var2) {
      return this.append(String.format(var1, var2));
   }

   public StrBuilder append(StringBuffer var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length();
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            var1.getChars(0, var2, this.buffer, var3);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(StringBuffer var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length()) {
         if (var3 >= 0 && var2 + var3 <= var1.length()) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               var1.getChars(var2, var2 + var3, this.buffer, var4);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("length must be valid");
         }
      } else {
         throw new StringIndexOutOfBoundsException("startIndex must be valid");
      }
   }

   public StrBuilder append(StringBuilder var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length();
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            var1.getChars(0, var2, this.buffer, var3);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(StringBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length()) {
         if (var3 >= 0 && var2 + var3 <= var1.length()) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               var1.getChars(var2, var2 + var3, this.buffer, var4);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("length must be valid");
         }
      } else {
         throw new StringIndexOutOfBoundsException("startIndex must be valid");
      }
   }

   public StrBuilder append(CharBuffer var1) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var1.hasArray()) {
         int var2 = var1.remaining();
         int var3 = this.length();
         this.ensureCapacity(var3 + var2);
         System.arraycopy(var1.array(), var1.arrayOffset() + var1.position(), this.buffer, var3, var2);
         this.size += var2;
         return this;
      } else {
         this.append(var1.toString());
         return this;
      }
   }

   public StrBuilder append(CharBuffer var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var1.hasArray()) {
         int var4 = var1.remaining();
         if (var2 >= 0 && var2 <= var4) {
            if (var3 >= 0 && var2 + var3 <= var4) {
               var4 = this.length();
               this.ensureCapacity(var4 + var3);
               System.arraycopy(var1.array(), var1.arrayOffset() + var1.position() + var2, this.buffer, var4, var3);
               this.size += var3;
               return this;
            } else {
               throw new StringIndexOutOfBoundsException("length must be valid");
            }
         } else {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
         }
      } else {
         this.append(var1.toString(), var2, var3);
         return this;
      }
   }

   public StrBuilder append(StrBuilder var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length();
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            System.arraycopy(var1.buffer, 0, this.buffer, var3, var2);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(StrBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else if (var2 >= 0 && var2 <= var1.length()) {
         if (var3 >= 0 && var2 + var3 <= var1.length()) {
            if (var3 > 0) {
               int var4 = this.length();
               this.ensureCapacity(var4 + var3);
               var1.getChars(var2, var2 + var3, this.buffer, var4);
               this.size += var3;
            }

            return this;
         } else {
            throw new StringIndexOutOfBoundsException("length must be valid");
         }
      } else {
         throw new StringIndexOutOfBoundsException("startIndex must be valid");
      }
   }

   public StrBuilder append(boolean var1) {
      int var2;
      int var3;
      char[] var4;
      if (var1) {
         this.ensureCapacity(this.size + 4);
         var4 = this.buffer;
         var3 = this.size;
         var2 = var3 + 1;
         this.size = var2;
         var4[var3] = 't';
         var3 = var2 + 1;
         this.size = var3;
         var4[var2] = 'r';
         var2 = var3 + 1;
         this.size = var2;
         var4[var3] = 'u';
         this.size = var2 + 1;
         var4[var2] = 'e';
         return this;
      } else {
         this.ensureCapacity(this.size + 5);
         var4 = this.buffer;
         var3 = this.size;
         var2 = var3 + 1;
         this.size = var2;
         var4[var3] = 'f';
         var3 = var2 + 1;
         this.size = var3;
         var4[var2] = 'a';
         var2 = var3 + 1;
         this.size = var2;
         var4[var3] = 'l';
         var3 = var2 + 1;
         this.size = var3;
         var4[var2] = 's';
         this.size = var3 + 1;
         var4[var3] = 'e';
         return this;
      }
   }

   public StrBuilder append(char[] var1) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         int var2 = var1.length;
         if (var2 > 0) {
            int var3 = this.length();
            this.ensureCapacity(var3 + var2);
            System.arraycopy(var1, 0, this.buffer, var3, var2);
            this.size += var2;
         }

         return this;
      }
   }

   public StrBuilder append(char[] var1, int var2, int var3) {
      if (var1 == null) {
         return this.appendNull();
      } else {
         StringBuilder var5;
         if (var2 >= 0 && var2 <= var1.length) {
            if (var3 >= 0 && var2 + var3 <= var1.length) {
               if (var3 > 0) {
                  int var4 = this.length();
                  this.ensureCapacity(var4 + var3);
                  System.arraycopy(var1, var2, this.buffer, var4, var3);
                  this.size += var3;
               }

               return this;
            } else {
               var5 = new StringBuilder();
               var5.append("Invalid length: ");
               var5.append(var3);
               throw new StringIndexOutOfBoundsException(var5.toString());
            }
         } else {
            var5 = new StringBuilder();
            var5.append("Invalid startIndex: ");
            var5.append(var3);
            throw new StringIndexOutOfBoundsException(var5.toString());
         }
      }
   }

   public StrBuilder appendAll(Iterable var1) {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            this.append(var2.next());
         }
      }

      return this;
   }

   public StrBuilder appendAll(Iterator var1) {
      if (var1 != null) {
         while(var1.hasNext()) {
            this.append(var1.next());
         }
      }

      return this;
   }

   public StrBuilder appendAll(Object... var1) {
      if (var1 != null && var1.length > 0) {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }
      }

      return this;
   }

   public StrBuilder appendFixedWidthPadLeft(int var1, int var2, char var3) {
      return this.appendFixedWidthPadLeft(String.valueOf(var1), var2, var3);
   }

   public StrBuilder appendFixedWidthPadLeft(Object var1, int var2, char var3) {
      if (var2 > 0) {
         this.ensureCapacity(this.size + var2);
         String var8;
         if (var1 == null) {
            var8 = this.getNullText();
         } else {
            var8 = var1.toString();
         }

         String var7 = var8;
         if (var8 == null) {
            var7 = "";
         }

         int var5 = var7.length();
         if (var5 >= var2) {
            var7.getChars(var5 - var2, var5, this.buffer, this.size);
         } else {
            int var6 = var2 - var5;

            for(int var4 = 0; var4 < var6; ++var4) {
               this.buffer[this.size + var4] = var3;
            }

            var7.getChars(0, var5, this.buffer, this.size + var6);
         }

         this.size += var2;
      }

      return this;
   }

   public StrBuilder appendFixedWidthPadRight(int var1, int var2, char var3) {
      return this.appendFixedWidthPadRight(String.valueOf(var1), var2, var3);
   }

   public StrBuilder appendFixedWidthPadRight(Object var1, int var2, char var3) {
      if (var2 > 0) {
         this.ensureCapacity(this.size + var2);
         String var7;
         if (var1 == null) {
            var7 = this.getNullText();
         } else {
            var7 = var1.toString();
         }

         String var6 = var7;
         if (var7 == null) {
            var6 = "";
         }

         int var5 = var6.length();
         if (var5 >= var2) {
            var6.getChars(0, var2, this.buffer, this.size);
         } else {
            var6.getChars(0, var5, this.buffer, this.size);

            for(int var4 = 0; var4 < var2 - var5; ++var4) {
               this.buffer[this.size + var5 + var4] = var3;
            }
         }

         this.size += var2;
      }

      return this;
   }

   public StrBuilder appendNewLine() {
      String var1 = this.newLine;
      if (var1 == null) {
         this.append(System.lineSeparator());
         return this;
      } else {
         return this.append(var1);
      }
   }

   public StrBuilder appendNull() {
      String var1 = this.nullText;
      return var1 == null ? this : this.append(var1);
   }

   public StrBuilder appendPadding(int var1, char var2) {
      if (var1 >= 0) {
         this.ensureCapacity(this.size + var1);

         for(int var3 = 0; var3 < var1; ++var3) {
            char[] var5 = this.buffer;
            int var4 = this.size++;
            var5[var4] = var2;
         }
      }

      return this;
   }

   public StrBuilder appendSeparator(char var1) {
      if (this.size() > 0) {
         this.append(var1);
      }

      return this;
   }

   public StrBuilder appendSeparator(char var1, char var2) {
      if (this.size() > 0) {
         this.append(var1);
         return this;
      } else {
         this.append(var2);
         return this;
      }
   }

   public StrBuilder appendSeparator(char var1, int var2) {
      if (var2 > 0) {
         this.append(var1);
      }

      return this;
   }

   public StrBuilder appendSeparator(String var1) {
      return this.appendSeparator(var1, (String)null);
   }

   public StrBuilder appendSeparator(String var1, int var2) {
      if (var1 != null && var2 > 0) {
         this.append(var1);
      }

      return this;
   }

   public StrBuilder appendSeparator(String var1, String var2) {
      if (this.isEmpty()) {
         var1 = var2;
      }

      if (var1 != null) {
         this.append(var1);
      }

      return this;
   }

   public void appendTo(Appendable var1) throws IOException {
      if (var1 instanceof Writer) {
         ((Writer)var1).write(this.buffer, 0, this.size);
      } else if (var1 instanceof StringBuilder) {
         ((StringBuilder)var1).append(this.buffer, 0, this.size);
      } else if (var1 instanceof StringBuffer) {
         ((StringBuffer)var1).append(this.buffer, 0, this.size);
      } else if (var1 instanceof CharBuffer) {
         ((CharBuffer)var1).put(this.buffer, 0, this.size);
      } else {
         var1.append(this);
      }
   }

   public StrBuilder appendWithSeparators(Iterable var1, String var2) {
      if (var1 != null) {
         var2 = Objects.toString(var2, "");
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            this.append(var3.next());
            if (var3.hasNext()) {
               this.append(var2);
            }
         }
      }

      return this;
   }

   public StrBuilder appendWithSeparators(Iterator var1, String var2) {
      if (var1 != null) {
         var2 = Objects.toString(var2, "");

         while(var1.hasNext()) {
            this.append(var1.next());
            if (var1.hasNext()) {
               this.append(var2);
            }
         }
      }

      return this;
   }

   public StrBuilder appendWithSeparators(Object[] var1, String var2) {
      if (var1 != null && var1.length > 0) {
         var2 = Objects.toString(var2, "");
         this.append(var1[0]);

         for(int var3 = 1; var3 < var1.length; ++var3) {
            this.append(var2);
            this.append(var1[var3]);
         }
      }

      return this;
   }

   public StrBuilder appendln(char var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(double var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(float var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(int var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(long var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(Object var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(String var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(String var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(String var1, Object... var2) {
      return this.append(var1, var2).appendNewLine();
   }

   public StrBuilder appendln(StringBuffer var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(StringBuffer var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(StringBuilder var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(StringBuilder var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(StrBuilder var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(StrBuilder var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public StrBuilder appendln(boolean var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(char[] var1) {
      return this.append(var1).appendNewLine();
   }

   public StrBuilder appendln(char[] var1, int var2, int var3) {
      return this.append(var1, var2, var3).appendNewLine();
   }

   public Reader asReader() {
      return new StrBuilder.StrBuilderReader();
   }

   public StrTokenizer asTokenizer() {
      return new StrBuilder.StrBuilderTokenizer();
   }

   public Writer asWriter() {
      return new StrBuilder.StrBuilderWriter();
   }

   public String build() {
      return this.toString();
   }

   public int capacity() {
      return this.buffer.length;
   }

   public char charAt(int var1) {
      if (var1 >= 0 && var1 < this.length()) {
         return this.buffer[var1];
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public StrBuilder clear() {
      this.size = 0;
      return this;
   }

   public boolean contains(char var1) {
      char[] var3 = this.buffer;

      for(int var2 = 0; var2 < this.size; ++var2) {
         if (var3[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   public boolean contains(String var1) {
      boolean var2 = false;
      if (this.indexOf((String)var1, 0) >= 0) {
         var2 = true;
      }

      return var2;
   }

   public boolean contains(StrMatcher var1) {
      boolean var2 = false;
      if (this.indexOf((StrMatcher)var1, 0) >= 0) {
         var2 = true;
      }

      return var2;
   }

   public StrBuilder delete(int var1, int var2) {
      var2 = this.validateRange(var1, var2);
      int var3 = var2 - var1;
      if (var3 > 0) {
         this.deleteImpl(var1, var2, var3);
      }

      return this;
   }

   public StrBuilder deleteAll(char var1) {
      int var3;
      for(int var2 = 0; var2 < this.size; var2 = var3 + 1) {
         var3 = var2;
         if (this.buffer[var2] == var1) {
            var3 = var2;

            int var4;
            do {
               var4 = var3 + 1;
               if (var4 >= this.size) {
                  break;
               }

               var3 = var4;
            } while(this.buffer[var4] == var1);

            var3 = var4 - var2;
            this.deleteImpl(var2, var4, var3);
            var3 = var4 - var3;
         }
      }

      return this;
   }

   public StrBuilder deleteAll(String var1) {
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = var1.length();
      }

      if (var2 > 0) {
         for(int var3 = this.indexOf((String)var1, 0); var3 >= 0; var3 = this.indexOf(var1, var3)) {
            this.deleteImpl(var3, var3 + var2, var2);
         }
      }

      return this;
   }

   public StrBuilder deleteAll(StrMatcher var1) {
      return this.replace(var1, (String)null, 0, this.size, -1);
   }

   public StrBuilder deleteCharAt(int var1) {
      if (var1 >= 0 && var1 < this.size) {
         this.deleteImpl(var1, var1 + 1, 1);
         return this;
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public StrBuilder deleteFirst(char var1) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         if (this.buffer[var2] == var1) {
            this.deleteImpl(var2, var2 + 1, 1);
            return this;
         }
      }

      return this;
   }

   public StrBuilder deleteFirst(String var1) {
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = var1.length();
      }

      if (var2 > 0) {
         int var3 = this.indexOf((String)var1, 0);
         if (var3 >= 0) {
            this.deleteImpl(var3, var3 + var2, var2);
         }
      }

      return this;
   }

   public StrBuilder deleteFirst(StrMatcher var1) {
      return this.replace(var1, (String)null, 0, this.size, 1);
   }

   public boolean endsWith(String var1) {
      if (var1 == null) {
         return false;
      } else {
         int var4 = var1.length();
         if (var4 == 0) {
            return true;
         } else {
            int var2 = this.size;
            if (var4 > var2) {
               return false;
            } else {
               var2 -= var4;

               for(int var3 = 0; var3 < var4; ++var2) {
                  if (this.buffer[var2] != var1.charAt(var3)) {
                     return false;
                  }

                  ++var3;
               }

               return true;
            }
         }
      }
   }

   public StrBuilder ensureCapacity(int var1) {
      if (var1 > this.buffer.length) {
         char[] var2 = this.buffer;
         char[] var3 = new char[var1 * 2];
         this.buffer = var3;
         System.arraycopy(var2, 0, var3, 0, this.size);
      }

      return this;
   }

   public boolean equals(Object var1) {
      return var1 instanceof StrBuilder && this.equals((StrBuilder)var1);
   }

   public boolean equals(StrBuilder var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         int var2 = this.size;
         if (var2 != var1.size) {
            return false;
         } else {
            char[] var3 = this.buffer;
            char[] var4 = var1.buffer;
            --var2;

            while(var2 >= 0) {
               if (var3[var2] != var4[var2]) {
                  return false;
               }

               --var2;
            }

            return true;
         }
      }
   }

   public boolean equalsIgnoreCase(StrBuilder var1) {
      if (this == var1) {
         return true;
      } else {
         int var4 = this.size;
         if (var4 != var1.size) {
            return false;
         } else {
            char[] var5 = this.buffer;
            char[] var6 = var1.buffer;
            --var4;

            while(var4 >= 0) {
               char var2 = var5[var4];
               char var3 = var6[var4];
               if (var2 != var3 && Character.toUpperCase(var2) != Character.toUpperCase(var3)) {
                  return false;
               }

               --var4;
            }

            return true;
         }
      }
   }

   public void getChars(int var1, int var2, char[] var3, int var4) {
      if (var1 >= 0) {
         if (var2 >= 0 && var2 <= this.length()) {
            if (var1 <= var2) {
               System.arraycopy(this.buffer, var1, var3, var4, var2 - var1);
            } else {
               throw new StringIndexOutOfBoundsException("end < start");
            }
         } else {
            throw new StringIndexOutOfBoundsException(var2);
         }
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public char[] getChars(char[] var1) {
      int var2;
      char[] var3;
      label11: {
         var2 = this.length();
         if (var1 != null) {
            var3 = var1;
            if (var1.length >= var2) {
               break label11;
            }
         }

         var3 = new char[var2];
      }

      System.arraycopy(this.buffer, 0, var3, 0, var2);
      return var3;
   }

   public String getNewLineText() {
      return this.newLine;
   }

   public String getNullText() {
      return this.nullText;
   }

   public int hashCode() {
      char[] var3 = this.buffer;
      int var2 = 0;

      for(int var1 = this.size - 1; var1 >= 0; --var1) {
         var2 = var2 * 31 + var3[var1];
      }

      return var2;
   }

   public int indexOf(char var1) {
      return this.indexOf(var1, 0);
   }

   public int indexOf(char var1, int var2) {
      if (var2 < 0) {
         var2 = 0;
      }

      if (var2 >= this.size) {
         return -1;
      } else {
         for(char[] var3 = this.buffer; var2 < this.size; ++var2) {
            if (var3[var2] == var1) {
               return var2;
            }
         }

         return -1;
      }
   }

   public int indexOf(String var1) {
      return this.indexOf((String)var1, 0);
   }

   public int indexOf(String var1, int var2) {
      if (var2 < 0) {
         var2 = 0;
      }

      if (var1 == null) {
         return -1;
      } else if (var2 >= this.size) {
         return -1;
      } else {
         int var4 = var1.length();
         if (var4 == 1) {
            return this.indexOf(var1.charAt(0), var2);
         } else if (var4 == 0) {
            return var2;
         } else {
            int var5 = this.size;
            if (var4 > var5) {
               return -1;
            } else {
               label47:
               for(char[] var6 = this.buffer; var2 < var5 - var4 + 1; ++var2) {
                  for(int var3 = 0; var3 < var4; ++var3) {
                     if (var1.charAt(var3) != var6[var2 + var3]) {
                        continue label47;
                     }
                  }

                  return var2;
               }

               return -1;
            }
         }
      }
   }

   public int indexOf(StrMatcher var1) {
      return this.indexOf((StrMatcher)var1, 0);
   }

   public int indexOf(StrMatcher var1, int var2) {
      if (var2 < 0) {
         var2 = 0;
      }

      if (var1 != null) {
         if (var2 >= this.size) {
            return -1;
         } else {
            int var4 = this.size;
            char[] var5 = this.buffer;

            for(int var3 = var2; var3 < var4; ++var3) {
               if (var1.isMatch(var5, var3, var2, var4) > 0) {
                  return var3;
               }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public StrBuilder insert(int var1, char var2) {
      this.validateIndex(var1);
      this.ensureCapacity(this.size + 1);
      char[] var3 = this.buffer;
      System.arraycopy(var3, var1, var3, var1 + 1, this.size - var1);
      this.buffer[var1] = var2;
      ++this.size;
      return this;
   }

   public StrBuilder insert(int var1, double var2) {
      return this.insert(var1, String.valueOf(var2));
   }

   public StrBuilder insert(int var1, float var2) {
      return this.insert(var1, String.valueOf(var2));
   }

   public StrBuilder insert(int var1, int var2) {
      return this.insert(var1, String.valueOf(var2));
   }

   public StrBuilder insert(int var1, long var2) {
      return this.insert(var1, String.valueOf(var2));
   }

   public StrBuilder insert(int var1, Object var2) {
      return var2 == null ? this.insert(var1, this.nullText) : this.insert(var1, var2.toString());
   }

   public StrBuilder insert(int var1, String var2) {
      this.validateIndex(var1);
      String var5 = var2;
      if (var2 == null) {
         var5 = this.nullText;
      }

      if (var5 != null) {
         int var3 = var5.length();
         if (var3 > 0) {
            int var4 = this.size + var3;
            this.ensureCapacity(var4);
            char[] var6 = this.buffer;
            System.arraycopy(var6, var1, var6, var1 + var3, this.size - var1);
            this.size = var4;
            var5.getChars(0, var3, this.buffer, var1);
         }
      }

      return this;
   }

   public StrBuilder insert(int var1, boolean var2) {
      this.validateIndex(var1);
      int var3;
      char[] var4;
      if (var2) {
         this.ensureCapacity(this.size + 4);
         var4 = this.buffer;
         System.arraycopy(var4, var1, var4, var1 + 4, this.size - var1);
         var4 = this.buffer;
         var3 = var1 + 1;
         var4[var1] = 't';
         var1 = var3 + 1;
         var4[var3] = 'r';
         var4[var1] = 'u';
         var4[var1 + 1] = 'e';
         this.size += 4;
         return this;
      } else {
         this.ensureCapacity(this.size + 5);
         var4 = this.buffer;
         System.arraycopy(var4, var1, var4, var1 + 5, this.size - var1);
         var4 = this.buffer;
         var3 = var1 + 1;
         var4[var1] = 'f';
         var1 = var3 + 1;
         var4[var3] = 'a';
         var3 = var1 + 1;
         var4[var1] = 'l';
         var4[var3] = 's';
         var4[var3 + 1] = 'e';
         this.size += 5;
         return this;
      }
   }

   public StrBuilder insert(int var1, char[] var2) {
      this.validateIndex(var1);
      if (var2 == null) {
         return this.insert(var1, this.nullText);
      } else {
         int var3 = var2.length;
         if (var3 > 0) {
            this.ensureCapacity(this.size + var3);
            char[] var4 = this.buffer;
            System.arraycopy(var4, var1, var4, var1 + var3, this.size - var1);
            System.arraycopy(var2, 0, this.buffer, var1, var3);
            this.size += var3;
         }

         return this;
      }
   }

   public StrBuilder insert(int var1, char[] var2, int var3, int var4) {
      this.validateIndex(var1);
      if (var2 == null) {
         return this.insert(var1, this.nullText);
      } else {
         StringBuilder var6;
         if (var3 >= 0 && var3 <= var2.length) {
            if (var4 >= 0 && var3 + var4 <= var2.length) {
               if (var4 > 0) {
                  this.ensureCapacity(this.size + var4);
                  char[] var5 = this.buffer;
                  System.arraycopy(var5, var1, var5, var1 + var4, this.size - var1);
                  System.arraycopy(var2, var3, this.buffer, var1, var4);
                  this.size += var4;
               }

               return this;
            } else {
               var6 = new StringBuilder();
               var6.append("Invalid length: ");
               var6.append(var4);
               throw new StringIndexOutOfBoundsException(var6.toString());
            }
         } else {
            var6 = new StringBuilder();
            var6.append("Invalid offset: ");
            var6.append(var3);
            throw new StringIndexOutOfBoundsException(var6.toString());
         }
      }
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public int lastIndexOf(char var1) {
      return this.lastIndexOf(var1, this.size - 1);
   }

   public int lastIndexOf(char var1, int var2) {
      int var3 = this.size;
      if (var2 >= var3) {
         var2 = var3 - 1;
      }

      if (var2 < 0) {
         return -1;
      } else {
         while(var2 >= 0) {
            if (this.buffer[var2] == var1) {
               return var2;
            }

            --var2;
         }

         return -1;
      }
   }

   public int lastIndexOf(String var1) {
      return this.lastIndexOf(var1, this.size - 1);
   }

   public int lastIndexOf(String var1, int var2) {
      int var3 = this.size;
      if (var2 >= var3) {
         var2 = var3 - 1;
      }

      if (var1 != null) {
         if (var2 < 0) {
            return -1;
         } else {
            int var4 = var1.length();
            if (var4 > 0 && var4 <= this.size) {
               if (var4 == 1) {
                  return this.lastIndexOf(var1.charAt(0), var2);
               } else {
                  label42:
                  for(var2 = var2 - var4 + 1; var2 >= 0; --var2) {
                     for(var3 = 0; var3 < var4; ++var3) {
                        if (var1.charAt(var3) != this.buffer[var2 + var3]) {
                           continue label42;
                        }
                     }

                     return var2;
                  }

                  return -1;
               }
            } else {
               return var4 == 0 ? var2 : -1;
            }
         }
      } else {
         return -1;
      }
   }

   public int lastIndexOf(StrMatcher var1) {
      return this.lastIndexOf(var1, this.size);
   }

   public int lastIndexOf(StrMatcher var1, int var2) {
      int var3 = this.size;
      if (var2 >= var3) {
         var2 = var3 - 1;
      }

      if (var1 != null) {
         if (var2 < 0) {
            return -1;
         } else {
            char[] var4 = this.buffer;

            for(var3 = var2; var3 >= 0; --var3) {
               if (var1.isMatch(var4, var3, 0, var2 + 1) > 0) {
                  return var3;
               }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public String leftString(int var1) {
      if (var1 <= 0) {
         return "";
      } else {
         return var1 >= this.size ? new String(this.buffer, 0, this.size) : new String(this.buffer, 0, var1);
      }
   }

   public int length() {
      return this.size;
   }

   public String midString(int var1, int var2) {
      int var3 = var1;
      if (var1 < 0) {
         var3 = 0;
      }

      if (var2 > 0) {
         var1 = this.size;
         if (var3 < var1) {
            if (var1 <= var3 + var2) {
               return new String(this.buffer, var3, this.size - var3);
            }

            return new String(this.buffer, var3, var2);
         }
      }

      return "";
   }

   public StrBuilder minimizeCapacity() {
      if (this.buffer.length > this.length()) {
         char[] var1 = this.buffer;
         char[] var2 = new char[this.length()];
         this.buffer = var2;
         System.arraycopy(var1, 0, var2, 0, this.size);
      }

      return this;
   }

   public int readFrom(Readable var1) throws IOException {
      int var2 = this.size;
      int var3;
      char[] var4;
      if (var1 instanceof Reader) {
         Reader var5 = (Reader)var1;
         this.ensureCapacity(this.size + 1);

         while(true) {
            var4 = this.buffer;
            var3 = this.size;
            var3 = var5.read(var4, var3, var4.length - var3);
            if (var3 == -1) {
               break;
            }

            var3 += this.size;
            this.size = var3;
            this.ensureCapacity(var3 + 1);
         }
      } else if (var1 instanceof CharBuffer) {
         CharBuffer var6 = (CharBuffer)var1;
         var3 = var6.remaining();
         this.ensureCapacity(this.size + var3);
         var6.get(this.buffer, this.size, var3);
         this.size += var3;
      } else {
         while(true) {
            this.ensureCapacity(this.size + 1);
            var4 = this.buffer;
            var3 = this.size;
            var3 = var1.read(CharBuffer.wrap(var4, var3, var4.length - var3));
            if (var3 == -1) {
               break;
            }

            this.size += var3;
         }
      }

      return this.size - var2;
   }

   public StrBuilder replace(int var1, int var2, String var3) {
      int var4 = this.validateRange(var1, var2);
      if (var3 == null) {
         var2 = 0;
      } else {
         var2 = var3.length();
      }

      this.replaceImpl(var1, var4, var4 - var1, var3, var2);
      return this;
   }

   public StrBuilder replace(StrMatcher var1, String var2, int var3, int var4, int var5) {
      return this.replaceImpl(var1, var2, var3, this.validateRange(var3, var4), var5);
   }

   public StrBuilder replaceAll(char var1, char var2) {
      if (var1 != var2) {
         for(int var3 = 0; var3 < this.size; ++var3) {
            char[] var4 = this.buffer;
            if (var4[var3] == var1) {
               var4[var3] = var2;
            }
         }
      }

      return this;
   }

   public StrBuilder replaceAll(String var1, String var2) {
      int var3;
      if (var1 == null) {
         var3 = 0;
      } else {
         var3 = var1.length();
      }

      if (var3 > 0) {
         int var4;
         if (var2 == null) {
            var4 = 0;
         } else {
            var4 = var2.length();
         }

         for(int var5 = this.indexOf((String)var1, 0); var5 >= 0; var5 = this.indexOf(var1, var5 + var4)) {
            this.replaceImpl(var5, var5 + var3, var3, var2, var4);
         }
      }

      return this;
   }

   public StrBuilder replaceAll(StrMatcher var1, String var2) {
      return this.replace(var1, var2, 0, this.size, -1);
   }

   public StrBuilder replaceFirst(char var1, char var2) {
      if (var1 != var2) {
         for(int var3 = 0; var3 < this.size; ++var3) {
            char[] var4 = this.buffer;
            if (var4[var3] == var1) {
               var4[var3] = var2;
               return this;
            }
         }
      }

      return this;
   }

   public StrBuilder replaceFirst(String var1, String var2) {
      int var3;
      if (var1 == null) {
         var3 = 0;
      } else {
         var3 = var1.length();
      }

      if (var3 > 0) {
         int var5 = this.indexOf((String)var1, 0);
         if (var5 >= 0) {
            int var4;
            if (var2 == null) {
               var4 = 0;
            } else {
               var4 = var2.length();
            }

            this.replaceImpl(var5, var5 + var3, var3, var2, var4);
         }
      }

      return this;
   }

   public StrBuilder replaceFirst(StrMatcher var1, String var2) {
      return this.replace(var1, var2, 0, this.size, 1);
   }

   public StrBuilder reverse() {
      int var2 = this.size;
      if (var2 == 0) {
         return this;
      } else {
         int var4 = var2 / 2;
         char[] var5 = this.buffer;
         int var3 = 0;
         --var2;

         while(var3 < var4) {
            char var1 = var5[var3];
            var5[var3] = var5[var2];
            var5[var2] = var1;
            ++var3;
            --var2;
         }

         return this;
      }
   }

   public String rightString(int var1) {
      if (var1 <= 0) {
         return "";
      } else {
         return var1 >= this.size ? new String(this.buffer, 0, this.size) : new String(this.buffer, this.size - var1, var1);
      }
   }

   public StrBuilder setCharAt(int var1, char var2) {
      if (var1 >= 0 && var1 < this.length()) {
         this.buffer[var1] = var2;
         return this;
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public StrBuilder setLength(int var1) {
      if (var1 >= 0) {
         int var2 = this.size;
         if (var1 < var2) {
            this.size = var1;
            return this;
         } else {
            if (var1 > var2) {
               this.ensureCapacity(var1);
               var2 = this.size;

               for(this.size = var1; var2 < var1; ++var2) {
                  this.buffer[var2] = 0;
               }
            }

            return this;
         }
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public StrBuilder setNewLineText(String var1) {
      this.newLine = var1;
      return this;
   }

   public StrBuilder setNullText(String var1) {
      String var2 = var1;
      if (var1 != null) {
         var2 = var1;
         if (var1.isEmpty()) {
            var2 = null;
         }
      }

      this.nullText = var2;
      return this;
   }

   public int size() {
      return this.size;
   }

   public boolean startsWith(String var1) {
      if (var1 == null) {
         return false;
      } else {
         int var3 = var1.length();
         if (var3 == 0) {
            return true;
         } else if (var3 > this.size) {
            return false;
         } else {
            for(int var2 = 0; var2 < var3; ++var2) {
               if (this.buffer[var2] != var1.charAt(var2)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public CharSequence subSequence(int var1, int var2) {
      if (var1 >= 0) {
         if (var2 <= this.size) {
            if (var1 <= var2) {
               return this.substring(var1, var2);
            } else {
               throw new StringIndexOutOfBoundsException(var2 - var1);
            }
         } else {
            throw new StringIndexOutOfBoundsException(var2);
         }
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   public String substring(int var1) {
      return this.substring(var1, this.size);
   }

   public String substring(int var1, int var2) {
      var2 = this.validateRange(var1, var2);
      return new String(this.buffer, var1, var2 - var1);
   }

   public char[] toCharArray() {
      int var1 = this.size;
      if (var1 == 0) {
         return ArrayUtils.EMPTY_CHAR_ARRAY;
      } else {
         char[] var2 = new char[var1];
         System.arraycopy(this.buffer, 0, var2, 0, var1);
         return var2;
      }
   }

   public char[] toCharArray(int var1, int var2) {
      var2 = this.validateRange(var1, var2) - var1;
      if (var2 == 0) {
         return ArrayUtils.EMPTY_CHAR_ARRAY;
      } else {
         char[] var3 = new char[var2];
         System.arraycopy(this.buffer, var1, var3, 0, var2);
         return var3;
      }
   }

   public String toString() {
      return new String(this.buffer, 0, this.size);
   }

   public StringBuffer toStringBuffer() {
      StringBuffer var1 = new StringBuffer(this.size);
      var1.append(this.buffer, 0, this.size);
      return var1;
   }

   public StringBuilder toStringBuilder() {
      StringBuilder var1 = new StringBuilder(this.size);
      var1.append(this.buffer, 0, this.size);
      return var1;
   }

   public StrBuilder trim() {
      if (this.size == 0) {
         return this;
      } else {
         int var3 = this.size;
         char[] var4 = this.buffer;
         int var1 = 0;

         int var2;
         while(true) {
            var2 = var3;
            if (var1 >= var3) {
               break;
            }

            var2 = var3;
            if (var4[var1] > ' ') {
               break;
            }

            ++var1;
         }

         while(var1 < var2 && var4[var2 - 1] <= ' ') {
            --var2;
         }

         var3 = this.size;
         if (var2 < var3) {
            this.delete(var2, var3);
         }

         if (var1 > 0) {
            this.delete(0, var1);
         }

         return this;
      }
   }

   protected void validateIndex(int var1) {
      if (var1 < 0 || var1 > this.size) {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   protected int validateRange(int var1, int var2) {
      if (var1 >= 0) {
         int var3 = var2;
         if (var2 > this.size) {
            var3 = this.size;
         }

         if (var1 <= var3) {
            return var3;
         } else {
            throw new StringIndexOutOfBoundsException("end < start");
         }
      } else {
         throw new StringIndexOutOfBoundsException(var1);
      }
   }

   class StrBuilderReader extends Reader {
      private int mark;
      private int pos;

      public void close() {
      }

      public void mark(int var1) {
         this.mark = this.pos;
      }

      public boolean markSupported() {
         return true;
      }

      public int read() {
         if (!this.ready()) {
            return -1;
         } else {
            StrBuilder var2 = StrBuilder.this;
            int var1 = this.pos++;
            return var2.charAt(var1);
         }
      }

      public int read(char[] var1, int var2, int var3) {
         if (var2 >= 0 && var3 >= 0 && var2 <= var1.length && var2 + var3 <= var1.length && var2 + var3 >= 0) {
            if (var3 == 0) {
               return 0;
            } else if (this.pos >= StrBuilder.this.size()) {
               return -1;
            } else {
               int var4 = var3;
               if (this.pos + var3 > StrBuilder.this.size()) {
                  var4 = StrBuilder.this.size() - this.pos;
               }

               StrBuilder var5 = StrBuilder.this;
               var3 = this.pos;
               var5.getChars(var3, var3 + var4, var1, var2);
               this.pos += var4;
               return var4;
            }
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public boolean ready() {
         return this.pos < StrBuilder.this.size();
      }

      public void reset() {
         this.pos = this.mark;
      }

      public long skip(long var1) {
         long var3 = var1;
         if ((long)this.pos + var1 > (long)StrBuilder.this.size()) {
            var3 = (long)(StrBuilder.this.size() - this.pos);
         }

         if (var3 < 0L) {
            return 0L;
         } else {
            this.pos = (int)((long)this.pos + var3);
            return var3;
         }
      }
   }

   class StrBuilderTokenizer extends StrTokenizer {
      public String getContent() {
         String var1 = super.getContent();
         return var1 == null ? StrBuilder.this.toString() : var1;
      }

      protected List tokenize(char[] var1, int var2, int var3) {
         return var1 == null ? super.tokenize(StrBuilder.this.buffer, 0, StrBuilder.this.size()) : super.tokenize(var1, var2, var3);
      }
   }

   class StrBuilderWriter extends Writer {
      public void close() {
      }

      public void flush() {
      }

      public void write(int var1) {
         StrBuilder.this.append((char)var1);
      }

      public void write(String var1) {
         StrBuilder.this.append(var1);
      }

      public void write(String var1, int var2, int var3) {
         StrBuilder.this.append(var1, var2, var3);
      }

      public void write(char[] var1) {
         StrBuilder.this.append(var1);
      }

      public void write(char[] var1, int var2, int var3) {
         StrBuilder.this.append(var1, var2, var3);
      }
   }
}
