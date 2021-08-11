package org.jsoup.examples;

import java.io.IOException;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ListLinks {
   public static void main(String[] var0) throws IOException {
      boolean var1;
      if (var0.length == 1) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "usage: supply url to fetch");
      String var5 = var0[0];
      print("Fetching %s...", var5);
      Document var2 = Jsoup.connect(var5).get();
      Elements var6 = var2.select("a[href]");
      Elements var3 = var2.select("[src]");
      Elements var8 = var2.select("link[href]");
      print("\nMedia: (%d)", var3.size());
      Iterator var11 = var3.iterator();

      while(var11.hasNext()) {
         Element var4 = (Element)var11.next();
         if (var4.tagName().equals("img")) {
            print(" * %s: <%s> %sx%s (%s)", var4.tagName(), var4.attr("abs:src"), var4.attr("width"), var4.attr("height"), trim(var4.attr("alt"), 20));
         } else {
            print(" * %s: <%s>", var4.tagName(), var4.attr("abs:src"));
         }
      }

      print("\nImports: (%d)", var8.size());
      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         Element var12 = (Element)var9.next();
         print(" * %s <%s> (%s)", var12.tagName(), var12.attr("abs:href"), var12.attr("rel"));
      }

      print("\nLinks: (%d)", var6.size());
      Iterator var7 = var6.iterator();

      while(var7.hasNext()) {
         Element var10 = (Element)var7.next();
         print(" * a: <%s>  (%s)", var10.attr("abs:href"), trim(var10.text(), 35));
      }

   }

   private static void print(String var0, Object... var1) {
      System.out.println(String.format(var0, var1));
   }

   private static String trim(String var0, int var1) {
      String var2 = var0;
      if (var0.length() > var1) {
         var2 = var0.substring(0, var1 - 1) + ".";
      }

      return var2;
   }
}
