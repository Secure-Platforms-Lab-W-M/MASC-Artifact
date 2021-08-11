package org.jsoup.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;

public class Selector {
   private final Evaluator evaluator;
   private final Element root;

   private Selector(String var1, Element var2) {
      Validate.notNull(var1);
      var1 = var1.trim();
      Validate.notEmpty(var1);
      Validate.notNull(var2);
      this.evaluator = QueryParser.parse(var1);
      this.root = var2;
   }

   private Selector(Evaluator var1, Element var2) {
      Validate.notNull(var1);
      Validate.notNull(var2);
      this.evaluator = var1;
      this.root = var2;
   }

   static Elements filterOut(Collection var0, Collection var1) {
      Elements var4 = new Elements();
      Iterator var7 = var0.iterator();

      while(var7.hasNext()) {
         Element var5 = (Element)var7.next();
         boolean var3 = false;
         Iterator var6 = var1.iterator();

         boolean var2;
         while(true) {
            var2 = var3;
            if (!var6.hasNext()) {
               break;
            }

            if (var5.equals((Element)var6.next())) {
               var2 = true;
               break;
            }
         }

         if (!var2) {
            var4.add(var5);
         }
      }

      return var4;
   }

   private Elements select() {
      return Collector.collect(this.evaluator, this.root);
   }

   public static Elements select(String var0, Iterable var1) {
      Validate.notEmpty(var0);
      Validate.notNull(var1);
      Evaluator var6 = QueryParser.parse(var0);
      ArrayList var2 = new ArrayList();
      IdentityHashMap var3 = new IdentityHashMap();
      Iterator var7 = var1.iterator();

      while(var7.hasNext()) {
         Iterator var4 = select(var6, (Element)var7.next()).iterator();

         while(var4.hasNext()) {
            Element var5 = (Element)var4.next();
            if (!var3.containsKey(var5)) {
               var2.add(var5);
               var3.put(var5, Boolean.TRUE);
            }
         }
      }

      return new Elements(var2);
   }

   public static Elements select(String var0, Element var1) {
      return (new Selector(var0, var1)).select();
   }

   public static Elements select(Evaluator var0, Element var1) {
      return (new Selector(var0, var1)).select();
   }

   public static class SelectorParseException extends IllegalStateException {
      public SelectorParseException(String var1, Object... var2) {
         super(String.format(var1, var2));
      }
   }
}
