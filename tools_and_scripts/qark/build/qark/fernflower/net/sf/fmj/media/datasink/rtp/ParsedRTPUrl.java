package net.sf.fmj.media.datasink.rtp;

public class ParsedRTPUrl {
   public final ParsedRTPUrlElement[] elements;

   public ParsedRTPUrl(ParsedRTPUrlElement var1) {
      this(new ParsedRTPUrlElement[]{var1});
   }

   public ParsedRTPUrl(ParsedRTPUrlElement var1, ParsedRTPUrlElement var2) {
      this(new ParsedRTPUrlElement[]{var1, var2});
   }

   public ParsedRTPUrl(ParsedRTPUrlElement[] var1) {
      this.elements = var1;
   }

   public ParsedRTPUrlElement find(String var1) {
      int var2 = 0;

      while(true) {
         ParsedRTPUrlElement[] var3 = this.elements;
         if (var2 >= var3.length) {
            return null;
         }

         if (var3[var2].type.equals(var1)) {
            return this.elements[var2];
         }

         ++var2;
      }
   }

   public String toString() {
      if (this.elements == null) {
         return "null";
      } else {
         StringBuffer var2 = new StringBuffer();
         var2.append("rtp://");

         for(int var1 = 0; var1 < this.elements.length; ++var1) {
            if (var1 > 0) {
               var2.append("&");
            }

            var2.append(this.elements[var1]);
         }

         return var2.toString();
      }
   }
}
