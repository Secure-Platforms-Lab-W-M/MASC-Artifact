package org.jsoup.parser;

public class ParseError {
   private String errorMsg;
   private int pos;

   ParseError(int var1, String var2) {
      this.pos = var1;
      this.errorMsg = var2;
   }

   ParseError(int var1, String var2, Object... var3) {
      this.errorMsg = String.format(var2, var3);
      this.pos = var1;
   }

   public String getErrorMessage() {
      return this.errorMsg;
   }

   public int getPosition() {
      return this.pos;
   }

   public String toString() {
      return this.pos + ": " + this.errorMsg;
   }
}
