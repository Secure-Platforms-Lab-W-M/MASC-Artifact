package com.tokenautocomplete;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import java.util.ArrayList;

public class CharacterTokenizer implements Tokenizer {
   ArrayList splitChar;

   CharacterTokenizer(char[] var1) {
      this.splitChar = new ArrayList(var1.length);
      int var4 = var1.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         char var2 = var1[var3];
         this.splitChar.add(var2);
      }

   }

   public int findTokenEnd(CharSequence var1, int var2) {
      int var3;
      for(var3 = var1.length(); var2 < var3; ++var2) {
         if (this.splitChar.contains(var1.charAt(var2))) {
            return var2;
         }
      }

      return var3;
   }

   public int findTokenStart(CharSequence var1, int var2) {
      int var3 = var2;

      int var4;
      while(true) {
         var4 = var3;
         if (var3 <= 0) {
            break;
         }

         var4 = var3;
         if (this.splitChar.contains(var1.charAt(var3 - 1))) {
            break;
         }

         --var3;
      }

      while(var4 < var2 && var1.charAt(var4) == ' ') {
         ++var4;
      }

      return var4;
   }

   public CharSequence terminateToken(CharSequence var1) {
      int var2;
      for(var2 = var1.length(); var2 > 0 && var1.charAt(var2 - 1) == ' '; --var2) {
      }

      if (var2 > 0 && this.splitChar.contains(var1.charAt(var2 - 1))) {
         return var1;
      } else {
         StringBuilder var4 = new StringBuilder();
         Object var3;
         if (this.splitChar.size() > 1 && (Character)this.splitChar.get(0) == ' ') {
            var3 = this.splitChar.get(1);
         } else {
            var3 = this.splitChar.get(0);
         }

         var4.append((Character)var3);
         var4.append(" ");
         String var5 = var4.toString();
         if (var1 instanceof Spanned) {
            var4 = new StringBuilder();
            var4.append(var1);
            var4.append(var5);
            SpannableString var6 = new SpannableString(var4.toString());
            TextUtils.copySpansFrom((Spanned)var1, 0, var1.length(), Object.class, var6, 0);
            return var6;
         } else {
            var4 = new StringBuilder();
            var4.append(var1);
            var4.append(var5);
            return var4.toString();
         }
      }
   }
}
