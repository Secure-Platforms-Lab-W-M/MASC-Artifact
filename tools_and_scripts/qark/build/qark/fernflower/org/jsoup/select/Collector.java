package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class Collector {
   private Collector() {
   }

   public static Elements collect(Evaluator var0, Element var1) {
      Elements var2 = new Elements();
      (new NodeTraversor(new Collector.Accumulator(var1, var2, var0))).traverse(var1);
      return var2;
   }

   private static class Accumulator implements NodeVisitor {
      private final Elements elements;
      private final Evaluator eval;
      private final Element root;

      Accumulator(Element var1, Elements var2, Evaluator var3) {
         this.root = var1;
         this.elements = var2;
         this.eval = var3;
      }

      public void head(Node var1, int var2) {
         if (var1 instanceof Element) {
            Element var3 = (Element)var1;
            if (this.eval.matches(this.root, var3)) {
               this.elements.add(var3);
            }
         }

      }

      public void tail(Node var1, int var2) {
      }
   }
}
