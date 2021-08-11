package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

public class Elements extends ArrayList {
   public Elements() {
   }

   public Elements(int var1) {
      super(var1);
   }

   public Elements(Collection var1) {
      super(var1);
   }

   public Elements(List var1) {
      super(var1);
   }

   public Elements(Element... var1) {
      super(Arrays.asList(var1));
   }

   public Elements addClass(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).addClass(var1);
      }

      return this;
   }

   public Elements after(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).after(var1);
      }

      return this;
   }

   public Elements append(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).append(var1);
      }

      return this;
   }

   public String attr(String var1) {
      Iterator var2 = this.iterator();

      Element var3;
      do {
         if (!var2.hasNext()) {
            return "";
         }

         var3 = (Element)var2.next();
      } while(!var3.hasAttr(var1));

      return var3.attr(var1);
   }

   public Elements attr(String var1, String var2) {
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         ((Element)var3.next()).attr(var1, var2);
      }

      return this;
   }

   public Elements before(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).before(var1);
      }

      return this;
   }

   public Elements clone() {
      Elements var1 = new Elements(this.size());
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         var1.add(((Element)var2.next()).clone());
      }

      return var1;
   }

   public Elements empty() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         ((Element)var1.next()).empty();
      }

      return this;
   }

   // $FF: renamed from: eq (int) org.jsoup.select.Elements
   public Elements method_8(int var1) {
      return this.size() > var1 ? new Elements(new Element[]{(Element)this.get(var1)}) : new Elements();
   }

   public Element first() {
      return this.isEmpty() ? null : (Element)this.get(0);
   }

   public List forms() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Element var3 = (Element)var2.next();
         if (var3 instanceof FormElement) {
            var1.add((FormElement)var3);
         }
      }

      return var1;
   }

   public boolean hasAttr(String var1) {
      Iterator var2 = this.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!((Element)var2.next()).hasAttr(var1));

      return true;
   }

   public boolean hasClass(String var1) {
      Iterator var2 = this.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!((Element)var2.next()).hasClass(var1));

      return true;
   }

   public boolean hasText() {
      Iterator var1 = this.iterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(!((Element)var1.next()).hasText());

      return true;
   }

   public String html() {
      StringBuilder var1 = new StringBuilder();

      Element var3;
      for(Iterator var2 = this.iterator(); var2.hasNext(); var1.append(var3.html())) {
         var3 = (Element)var2.next();
         if (var1.length() != 0) {
            var1.append("\n");
         }
      }

      return var1.toString();
   }

   public Elements html(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).html(var1);
      }

      return this;
   }

   // $FF: renamed from: is (java.lang.String) boolean
   public boolean method_9(String var1) {
      return !this.select(var1).isEmpty();
   }

   public Element last() {
      return this.isEmpty() ? null : (Element)this.get(this.size() - 1);
   }

   public Elements not(String var1) {
      return Selector.filterOut(this, Selector.select((String)var1, (Iterable)this));
   }

   public String outerHtml() {
      StringBuilder var1 = new StringBuilder();

      Element var3;
      for(Iterator var2 = this.iterator(); var2.hasNext(); var1.append(var3.outerHtml())) {
         var3 = (Element)var2.next();
         if (var1.length() != 0) {
            var1.append("\n");
         }
      }

      return var1.toString();
   }

   public Elements parents() {
      LinkedHashSet var1 = new LinkedHashSet();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         var1.addAll(((Element)var2.next()).parents());
      }

      return new Elements(var1);
   }

   public Elements prepend(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).prepend(var1);
      }

      return this;
   }

   public Elements remove() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         ((Element)var1.next()).remove();
      }

      return this;
   }

   public Elements removeAttr(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).removeAttr(var1);
      }

      return this;
   }

   public Elements removeClass(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).removeClass(var1);
      }

      return this;
   }

   public Elements select(String var1) {
      return Selector.select((String)var1, (Iterable)this);
   }

   public Elements tagName(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).tagName(var1);
      }

      return this;
   }

   public String text() {
      StringBuilder var1 = new StringBuilder();

      Element var3;
      for(Iterator var2 = this.iterator(); var2.hasNext(); var1.append(var3.text())) {
         var3 = (Element)var2.next();
         if (var1.length() != 0) {
            var1.append(" ");
         }
      }

      return var1.toString();
   }

   public String toString() {
      return this.outerHtml();
   }

   public Elements toggleClass(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).toggleClass(var1);
      }

      return this;
   }

   public Elements traverse(NodeVisitor var1) {
      Validate.notNull(var1);
      NodeTraversor var3 = new NodeTraversor(var1);
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         var3.traverse((Element)var2.next());
      }

      return this;
   }

   public Elements unwrap() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         ((Element)var1.next()).unwrap();
      }

      return this;
   }

   public String val() {
      return this.size() > 0 ? this.first().val() : "";
   }

   public Elements val(String var1) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).val(var1);
      }

      return this;
   }

   public Elements wrap(String var1) {
      Validate.notEmpty(var1);
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         ((Element)var2.next()).wrap(var1);
      }

      return this;
   }
}
