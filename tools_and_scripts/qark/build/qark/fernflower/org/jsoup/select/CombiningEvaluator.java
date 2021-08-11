package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;

abstract class CombiningEvaluator extends Evaluator {
   final ArrayList evaluators;
   int num;

   CombiningEvaluator() {
      this.num = 0;
      this.evaluators = new ArrayList();
   }

   CombiningEvaluator(Collection var1) {
      this();
      this.evaluators.addAll(var1);
      this.updateNumEvaluators();
   }

   void replaceRightMostEvaluator(Evaluator var1) {
      this.evaluators.set(this.num - 1, var1);
   }

   Evaluator rightMostEvaluator() {
      return this.num > 0 ? (Evaluator)this.evaluators.get(this.num - 1) : null;
   }

   void updateNumEvaluators() {
      this.num = this.evaluators.size();
   }

   static final class And extends CombiningEvaluator {
      And(Collection var1) {
         super(var1);
      }

      And(Evaluator... var1) {
         this((Collection)Arrays.asList(var1));
      }

      public boolean matches(Element var1, Element var2) {
         for(int var3 = 0; var3 < this.num; ++var3) {
            if (!((Evaluator)this.evaluators.get(var3)).matches(var1, var2)) {
               return false;
            }
         }

         return true;
      }

      public String toString() {
         return StringUtil.join((Collection)this.evaluators, " ");
      }
   }

   static final class class_17 extends CombiningEvaluator {
      class_17() {
      }

      class_17(Collection var1) {
         if (this.num > 1) {
            this.evaluators.add(new CombiningEvaluator.And(var1));
         } else {
            this.evaluators.addAll(var1);
         }

         this.updateNumEvaluators();
      }

      public void add(Evaluator var1) {
         this.evaluators.add(var1);
         this.updateNumEvaluators();
      }

      public boolean matches(Element var1, Element var2) {
         for(int var3 = 0; var3 < this.num; ++var3) {
            if (((Evaluator)this.evaluators.get(var3)).matches(var1, var2)) {
               return true;
            }
         }

         return false;
      }

      public String toString() {
         return String.format(":or%s", this.evaluators);
      }
   }
}
