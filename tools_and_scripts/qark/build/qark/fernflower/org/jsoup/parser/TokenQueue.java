package org.jsoup.parser;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class TokenQueue {
   private static final char ESC = '\\';
   private int pos = 0;
   private String queue;

   public TokenQueue(String var1) {
      Validate.notNull(var1);
      this.queue = var1;
   }

   private int remainingLength() {
      return this.queue.length() - this.pos;
   }

   public static String unescape(String var0) {
      StringBuilder var5 = new StringBuilder();
      char var3 = 0;
      char[] var6 = var0.toCharArray();
      int var4 = var6.length;

      for(int var2 = 0; var2 < var4; ++var2) {
         char var1 = var6[var2];
         if (var1 == '\\') {
            if (var3 != 0 && var3 == '\\') {
               var5.append(var1);
            }
         } else {
            var5.append(var1);
         }

         var3 = var1;
      }

      return var5.toString();
   }

   public void addFirst(Character var1) {
      this.addFirst(var1.toString());
   }

   public void addFirst(String var1) {
      this.queue = var1 + this.queue.substring(this.pos);
      this.pos = 0;
   }

   public void advance() {
      if (!this.isEmpty()) {
         ++this.pos;
      }

   }

   public String chompBalanced(char var1, char var2) {
      int var4 = -1;
      int var3 = -1;
      int var9 = 0;
      char var8 = 0;

      while(!this.isEmpty()) {
         int var5;
         int var6;
         int var7;
         Character var10;
         label36: {
            var10 = this.consume();
            if (var8 != 0) {
               var6 = var9;
               var5 = var4;
               if (var8 == '\\') {
                  break label36;
               }
            }

            if (var10.equals(var1)) {
               var7 = var9 + 1;
               var6 = var7;
               var5 = var4;
               if (var4 == -1) {
                  var5 = this.pos;
                  var6 = var7;
               }
            } else {
               var6 = var9;
               var5 = var4;
               if (var10.equals(var2)) {
                  var6 = var9 - 1;
                  var5 = var4;
               }
            }
         }

         var7 = var3;
         if (var6 > 0) {
            var7 = var3;
            if (var8 != 0) {
               var7 = this.pos;
            }
         }

         var8 = var10;
         var9 = var6;
         var3 = var7;
         var4 = var5;
         if (var6 <= 0) {
            var3 = var7;
            var4 = var5;
            break;
         }
      }

      return var3 >= 0 ? this.queue.substring(var4, var3) : "";
   }

   public String chompTo(String var1) {
      String var2 = this.consumeTo(var1);
      this.matchChomp(var1);
      return var2;
   }

   public String chompToIgnoreCase(String var1) {
      String var2 = this.consumeToIgnoreCase(var1);
      this.matchChomp(var1);
      return var2;
   }

   public char consume() {
      String var2 = this.queue;
      int var1 = this.pos++;
      return var2.charAt(var1);
   }

   public void consume(String var1) {
      if (!this.matches(var1)) {
         throw new IllegalStateException("Queue did not match expected sequence");
      } else {
         int var2 = var1.length();
         if (var2 > this.remainingLength()) {
            throw new IllegalStateException("Queue not long enough to consume sequence");
         } else {
            this.pos += var2;
         }
      }
   }

   public String consumeAttributeKey() {
      int var1;
      for(var1 = this.pos; !this.isEmpty() && (this.matchesWord() || this.matchesAny('-', '_', ':')); ++this.pos) {
      }

      return this.queue.substring(var1, this.pos);
   }

   public String consumeCssIdentifier() {
      int var1;
      for(var1 = this.pos; !this.isEmpty() && (this.matchesWord() || this.matchesAny('-', '_')); ++this.pos) {
      }

      return this.queue.substring(var1, this.pos);
   }

   public String consumeElementSelector() {
      int var1;
      for(var1 = this.pos; !this.isEmpty() && (this.matchesWord() || this.matchesAny('|', '_', '-')); ++this.pos) {
      }

      return this.queue.substring(var1, this.pos);
   }

   public String consumeTagName() {
      int var1;
      for(var1 = this.pos; !this.isEmpty() && (this.matchesWord() || this.matchesAny(':', '_', '-')); ++this.pos) {
      }

      return this.queue.substring(var1, this.pos);
   }

   public String consumeTo(String var1) {
      int var2 = this.queue.indexOf(var1, this.pos);
      if (var2 != -1) {
         var1 = this.queue.substring(this.pos, var2);
         this.pos += var1.length();
         return var1;
      } else {
         return this.remainder();
      }
   }

   public String consumeToAny(String... var1) {
      int var2;
      for(var2 = this.pos; !this.isEmpty() && !this.matchesAny(var1); ++this.pos) {
      }

      return this.queue.substring(var2, this.pos);
   }

   public String consumeToIgnoreCase(String var1) {
      int var2 = this.pos;
      String var5 = var1.substring(0, 1);
      boolean var4 = var5.toLowerCase().equals(var5.toUpperCase());

      while(!this.isEmpty() && !this.matches(var1)) {
         if (var4) {
            int var3 = this.queue.indexOf(var5, this.pos) - this.pos;
            if (var3 == 0) {
               ++this.pos;
            } else if (var3 < 0) {
               this.pos = this.queue.length();
            } else {
               this.pos += var3;
            }
         } else {
            ++this.pos;
         }
      }

      return this.queue.substring(var2, this.pos);
   }

   public boolean consumeWhitespace() {
      boolean var1;
      for(var1 = false; this.matchesWhitespace(); var1 = true) {
         ++this.pos;
      }

      return var1;
   }

   public String consumeWord() {
      int var1;
      for(var1 = this.pos; this.matchesWord(); ++this.pos) {
      }

      return this.queue.substring(var1, this.pos);
   }

   public boolean isEmpty() {
      return this.remainingLength() == 0;
   }

   public boolean matchChomp(String var1) {
      if (this.matches(var1)) {
         this.pos += var1.length();
         return true;
      } else {
         return false;
      }
   }

   public boolean matches(String var1) {
      return this.queue.regionMatches(true, this.pos, var1, 0, var1.length());
   }

   public boolean matchesAny(char... var1) {
      if (!this.isEmpty()) {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            char var4 = var1[var2];
            if (this.queue.charAt(this.pos) == var4) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean matchesAny(String... var1) {
      boolean var5 = false;
      int var3 = var1.length;
      int var2 = 0;

      boolean var4;
      while(true) {
         var4 = var5;
         if (var2 >= var3) {
            break;
         }

         if (this.matches(var1[var2])) {
            var4 = true;
            break;
         }

         ++var2;
      }

      return var4;
   }

   public boolean matchesCS(String var1) {
      return this.queue.startsWith(var1, this.pos);
   }

   public boolean matchesStartTag() {
      return this.remainingLength() >= 2 && this.queue.charAt(this.pos) == '<' && Character.isLetter(this.queue.charAt(this.pos + 1));
   }

   public boolean matchesWhitespace() {
      return !this.isEmpty() && StringUtil.isWhitespace(this.queue.charAt(this.pos));
   }

   public boolean matchesWord() {
      return !this.isEmpty() && Character.isLetterOrDigit(this.queue.charAt(this.pos));
   }

   public char peek() {
      return this.isEmpty() ? '\u0000' : this.queue.charAt(this.pos);
   }

   public String remainder() {
      String var1 = this.queue.substring(this.pos, this.queue.length());
      this.pos = this.queue.length();
      return var1;
   }

   public String toString() {
      return this.queue.substring(this.pos);
   }
}
