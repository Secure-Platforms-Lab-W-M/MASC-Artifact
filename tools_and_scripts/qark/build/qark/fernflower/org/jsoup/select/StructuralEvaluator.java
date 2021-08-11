package org.jsoup.select;

import java.util.Iterator;
import org.jsoup.nodes.Element;

abstract class StructuralEvaluator extends Evaluator {
   Evaluator evaluator;

   static class Has extends StructuralEvaluator {
      public Has(Evaluator var1) {
         this.evaluator = var1;
      }

      public boolean matches(Element var1, Element var2) {
         Iterator var3 = var2.getAllElements().iterator();

         Element var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (Element)var3.next();
         } while(var4 == var2 || !this.evaluator.matches(var1, var4));

         return true;
      }

      public String toString() {
         return String.format(":has(%s)", this.evaluator);
      }
   }

   static class ImmediateParent extends StructuralEvaluator {
      public ImmediateParent(Evaluator var1) {
         this.evaluator = var1;
      }

      public boolean matches(Element var1, Element var2) {
         if (var1 != var2) {
            var2 = var2.parent();
            if (var2 != null && this.evaluator.matches(var1, var2)) {
               return true;
            }
         }

         return false;
      }

      public String toString() {
         return String.format(":ImmediateParent%s", this.evaluator);
      }
   }

   static class ImmediatePreviousSibling extends StructuralEvaluator {
      public ImmediatePreviousSibling(Evaluator var1) {
         this.evaluator = var1;
      }

      public boolean matches(Element var1, Element var2) {
         if (var1 != var2) {
            var2 = var2.previousElementSibling();
            if (var2 != null && this.evaluator.matches(var1, var2)) {
               return true;
            }
         }

         return false;
      }

      public String toString() {
         return String.format(":prev%s", this.evaluator);
      }
   }

   static class Not extends StructuralEvaluator {
      public Not(Evaluator var1) {
         this.evaluator = var1;
      }

      public boolean matches(Element var1, Element var2) {
         return !this.evaluator.matches(var1, var2);
      }

      public String toString() {
         return String.format(":not%s", this.evaluator);
      }
   }

   static class Parent extends StructuralEvaluator {
      public Parent(Evaluator var1) {
         this.evaluator = var1;
      }

      public boolean matches(Element var1, Element var2) {
         if (var1 != var2) {
            var2 = var2.parent();

            while(true) {
               if (this.evaluator.matches(var1, var2)) {
                  return true;
               }

               if (var2 == var1) {
                  break;
               }

               var2 = var2.parent();
            }
         }

         return false;
      }

      public String toString() {
         return String.format(":parent%s", this.evaluator);
      }
   }

   static class PreviousSibling extends StructuralEvaluator {
      public PreviousSibling(Evaluator var1) {
         this.evaluator = var1;
      }

      public boolean matches(Element var1, Element var2) {
         if (var1 != var2) {
            for(var2 = var2.previousElementSibling(); var2 != null; var2 = var2.previousElementSibling()) {
               if (this.evaluator.matches(var1, var2)) {
                  return true;
               }
            }
         }

         return false;
      }

      public String toString() {
         return String.format(":prev*%s", this.evaluator);
      }
   }

   static class Root extends Evaluator {
      public boolean matches(Element var1, Element var2) {
         return var1 == var2;
      }
   }
}
