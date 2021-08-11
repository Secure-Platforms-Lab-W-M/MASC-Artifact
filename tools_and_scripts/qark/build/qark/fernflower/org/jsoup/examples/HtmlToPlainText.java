package org.jsoup.examples;

import java.io.IOException;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class HtmlToPlainText {
   private static final int timeout = 5000;
   private static final String userAgent = "Mozilla/5.0 (jsoup)";

   public static void main(String... var0) throws IOException {
      boolean var1;
      if (var0.length != 1 && var0.length != 2) {
         var1 = false;
      } else {
         var1 = true;
      }

      Validate.isTrue(var1, "usage: java -cp jsoup.jar org.jsoup.examples.HtmlToPlainText url [selector]");
      String var2 = var0[0];
      String var4;
      if (var0.length == 2) {
         var4 = var0[1];
      } else {
         var4 = null;
      }

      Document var3 = Jsoup.connect(var2).userAgent("Mozilla/5.0 (jsoup)").timeout(5000).get();
      HtmlToPlainText var6 = new HtmlToPlainText();
      if (var4 != null) {
         Iterator var5 = var3.select(var4).iterator();

         while(var5.hasNext()) {
            String var7 = var6.getPlainText((Element)var5.next());
            System.out.println(var7);
         }
      } else {
         var4 = var6.getPlainText(var3);
         System.out.println(var4);
      }

   }

   public String getPlainText(Element var1) {
      HtmlToPlainText.FormattingVisitor var2 = new HtmlToPlainText.FormattingVisitor();
      (new NodeTraversor(var2)).traverse(var1);
      return var2.toString();
   }

   private class FormattingVisitor implements NodeVisitor {
      private static final int maxWidth = 80;
      private StringBuilder accum;
      private int width;

      private FormattingVisitor() {
         this.width = 0;
         this.accum = new StringBuilder();
      }

      // $FF: synthetic method
      FormattingVisitor(Object var2) {
         this();
      }

      private void append(String var1) {
         if (var1.startsWith("\n")) {
            this.width = 0;
         }

         if (!var1.equals(" ") || this.accum.length() != 0 && !StringUtil.method_16(this.accum.substring(this.accum.length() - 1), " ", "\n")) {
            if (var1.length() + this.width <= 80) {
               this.accum.append(var1);
               this.width += var1.length();
               return;
            }

            String[] var5 = var1.split("\\s+");

            for(int var2 = 0; var2 < var5.length; ++var2) {
               String var4 = var5[var2];
               boolean var3;
               if (var2 == var5.length - 1) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var1 = var4;
               if (!var3) {
                  var1 = var4 + " ";
               }

               if (var1.length() + this.width > 80) {
                  this.accum.append("\n").append(var1);
                  this.width = var1.length();
               } else {
                  this.accum.append(var1);
                  this.width += var1.length();
               }
            }
         }

      }

      public void head(Node var1, int var2) {
         String var3 = var1.nodeName();
         if (var1 instanceof TextNode) {
            this.append(((TextNode)var1).text());
         } else {
            if (var3.equals("li")) {
               this.append("\n * ");
               return;
            }

            if (var3.equals("dt")) {
               this.append("  ");
               return;
            }

            if (StringUtil.method_16(var3, "p", "h1", "h2", "h3", "h4", "h5", "tr")) {
               this.append("\n");
               return;
            }
         }

      }

      public void tail(Node var1, int var2) {
         String var3 = var1.nodeName();
         if (StringUtil.method_16(var3, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5")) {
            this.append("\n");
         } else if (var3.equals("a")) {
            this.append(String.format(" <%s>", var1.absUrl("href")));
            return;
         }

      }

      public String toString() {
         return this.accum.toString();
      }
   }
}
