package org.jsoup.select;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;

public abstract class Evaluator {
   protected Evaluator() {
   }

   public abstract boolean matches(Element var1, Element var2);

   public static final class AllElements extends Evaluator {
      public boolean matches(Element var1, Element var2) {
         return true;
      }

      public String toString() {
         return "*";
      }
   }

   public static final class Attribute extends Evaluator {
      private String key;

      public Attribute(String var1) {
         this.key = var1;
      }

      public boolean matches(Element var1, Element var2) {
         return var2.hasAttr(this.key);
      }

      public String toString() {
         return String.format("[%s]", this.key);
      }
   }

   public abstract static class AttributeKeyPair extends Evaluator {
      String key;
      String value;

      public AttributeKeyPair(String var1, String var2) {
         label22: {
            super();
            Validate.notEmpty(var1);
            Validate.notEmpty(var2);
            this.key = var1.trim().toLowerCase();
            if (!var2.startsWith("\"") || !var2.endsWith("\"")) {
               var1 = var2;
               if (!var2.startsWith("'")) {
                  break label22;
               }

               var1 = var2;
               if (!var2.endsWith("'")) {
                  break label22;
               }
            }

            var1 = var2.substring(1, var2.length() - 1);
         }

         this.value = var1.trim().toLowerCase();
      }
   }

   public static final class AttributeStarting extends Evaluator {
      private String keyPrefix;

      public AttributeStarting(String var1) {
         this.keyPrefix = var1;
      }

      public boolean matches(Element var1, Element var2) {
         Iterator var3 = var2.attributes().asList().iterator();

         do {
            if (!var3.hasNext()) {
               return false;
            }
         } while(!((org.jsoup.nodes.Attribute)var3.next()).getKey().startsWith(this.keyPrefix));

         return true;
      }

      public String toString() {
         return String.format("[^%s]", this.keyPrefix);
      }
   }

   public static final class AttributeWithValue extends Evaluator.AttributeKeyPair {
      public AttributeWithValue(String var1, String var2) {
         super(var1, var2);
      }

      public boolean matches(Element var1, Element var2) {
         return var2.hasAttr(this.key) && this.value.equalsIgnoreCase(var2.attr(this.key).trim());
      }

      public String toString() {
         return String.format("[%s=%s]", this.key, this.value);
      }
   }

   public static final class AttributeWithValueContaining extends Evaluator.AttributeKeyPair {
      public AttributeWithValueContaining(String var1, String var2) {
         super(var1, var2);
      }

      public boolean matches(Element var1, Element var2) {
         return var2.hasAttr(this.key) && var2.attr(this.key).toLowerCase().contains(this.value);
      }

      public String toString() {
         return String.format("[%s*=%s]", this.key, this.value);
      }
   }

   public static final class AttributeWithValueEnding extends Evaluator.AttributeKeyPair {
      public AttributeWithValueEnding(String var1, String var2) {
         super(var1, var2);
      }

      public boolean matches(Element var1, Element var2) {
         return var2.hasAttr(this.key) && var2.attr(this.key).toLowerCase().endsWith(this.value);
      }

      public String toString() {
         return String.format("[%s$=%s]", this.key, this.value);
      }
   }

   public static final class AttributeWithValueMatching extends Evaluator {
      String key;
      Pattern pattern;

      public AttributeWithValueMatching(String var1, Pattern var2) {
         this.key = var1.trim().toLowerCase();
         this.pattern = var2;
      }

      public boolean matches(Element var1, Element var2) {
         return var2.hasAttr(this.key) && this.pattern.matcher(var2.attr(this.key)).find();
      }

      public String toString() {
         return String.format("[%s~=%s]", this.key, this.pattern.toString());
      }
   }

   public static final class AttributeWithValueNot extends Evaluator.AttributeKeyPair {
      public AttributeWithValueNot(String var1, String var2) {
         super(var1, var2);
      }

      public boolean matches(Element var1, Element var2) {
         return !this.value.equalsIgnoreCase(var2.attr(this.key));
      }

      public String toString() {
         return String.format("[%s!=%s]", this.key, this.value);
      }
   }

   public static final class AttributeWithValueStarting extends Evaluator.AttributeKeyPair {
      public AttributeWithValueStarting(String var1, String var2) {
         super(var1, var2);
      }

      public boolean matches(Element var1, Element var2) {
         return var2.hasAttr(this.key) && var2.attr(this.key).toLowerCase().startsWith(this.value);
      }

      public String toString() {
         return String.format("[%s^=%s]", this.key, this.value);
      }
   }

   public static final class Class extends Evaluator {
      private String className;

      public Class(String var1) {
         this.className = var1;
      }

      public boolean matches(Element var1, Element var2) {
         return var2.hasClass(this.className);
      }

      public String toString() {
         return String.format(".%s", this.className);
      }
   }

   public static final class ContainsOwnText extends Evaluator {
      private String searchText;

      public ContainsOwnText(String var1) {
         this.searchText = var1.toLowerCase();
      }

      public boolean matches(Element var1, Element var2) {
         return var2.ownText().toLowerCase().contains(this.searchText);
      }

      public String toString() {
         return String.format(":containsOwn(%s", this.searchText);
      }
   }

   public static final class ContainsText extends Evaluator {
      private String searchText;

      public ContainsText(String var1) {
         this.searchText = var1.toLowerCase();
      }

      public boolean matches(Element var1, Element var2) {
         return var2.text().toLowerCase().contains(this.searchText);
      }

      public String toString() {
         return String.format(":contains(%s", this.searchText);
      }
   }

   public abstract static class CssNthEvaluator extends Evaluator {
      // $FF: renamed from: a int
      protected final int field_4;
      // $FF: renamed from: b int
      protected final int field_5;

      public CssNthEvaluator(int var1) {
         this(0, var1);
      }

      public CssNthEvaluator(int var1, int var2) {
         this.field_4 = var1;
         this.field_5 = var2;
      }

      protected abstract int calculatePosition(Element var1, Element var2);

      protected abstract String getPseudoClass();

      public boolean matches(Element var1, Element var2) {
         boolean var4 = true;
         Element var5 = var2.parent();
         if (var5 != null && !(var5 instanceof Document)) {
            int var3 = this.calculatePosition(var1, var2);
            if (this.field_4 == 0) {
               if (var3 != this.field_5) {
                  return false;
               }
            } else if ((var3 - this.field_5) * this.field_4 < 0 || (var3 - this.field_5) % this.field_4 != 0) {
               return false;
            }
         } else {
            var4 = false;
         }

         return var4;
      }

      public String toString() {
         if (this.field_4 == 0) {
            return String.format(":%s(%d)", this.getPseudoClass(), this.field_5);
         } else {
            return this.field_5 == 0 ? String.format(":%s(%dn)", this.getPseudoClass(), this.field_4) : String.format(":%s(%dn%+d)", this.getPseudoClass(), this.field_4, this.field_5);
         }
      }
   }

   public static final class class_18 extends Evaluator {
      // $FF: renamed from: id java.lang.String
      private String field_3;

      public class_18(String var1) {
         this.field_3 = var1;
      }

      public boolean matches(Element var1, Element var2) {
         return this.field_3.equals(var2.method_5());
      }

      public String toString() {
         return String.format("#%s", this.field_3);
      }
   }

   public static final class IndexEquals extends Evaluator.IndexEvaluator {
      public IndexEquals(int var1) {
         super(var1);
      }

      public boolean matches(Element var1, Element var2) {
         return var2.elementSiblingIndex() == this.index;
      }

      public String toString() {
         return String.format(":eq(%d)", this.index);
      }
   }

   public abstract static class IndexEvaluator extends Evaluator {
      int index;

      public IndexEvaluator(int var1) {
         this.index = var1;
      }
   }

   public static final class IndexGreaterThan extends Evaluator.IndexEvaluator {
      public IndexGreaterThan(int var1) {
         super(var1);
      }

      public boolean matches(Element var1, Element var2) {
         return var2.elementSiblingIndex() > this.index;
      }

      public String toString() {
         return String.format(":gt(%d)", this.index);
      }
   }

   public static final class IndexLessThan extends Evaluator.IndexEvaluator {
      public IndexLessThan(int var1) {
         super(var1);
      }

      public boolean matches(Element var1, Element var2) {
         return var2.elementSiblingIndex() < this.index;
      }

      public String toString() {
         return String.format(":lt(%d)", this.index);
      }
   }

   public static final class IsEmpty extends Evaluator {
      public boolean matches(Element var1, Element var2) {
         List var4 = var2.childNodes();

         for(int var3 = 0; var3 < var4.size(); ++var3) {
            Node var5 = (Node)var4.get(var3);
            if (!(var5 instanceof Comment) && !(var5 instanceof XmlDeclaration) && !(var5 instanceof DocumentType)) {
               return false;
            }
         }

         return true;
      }

      public String toString() {
         return ":empty";
      }
   }

   public static final class IsFirstChild extends Evaluator {
      public boolean matches(Element var1, Element var2) {
         var1 = var2.parent();
         return var1 != null && !(var1 instanceof Document) && var2.elementSiblingIndex() == 0;
      }

      public String toString() {
         return ":first-child";
      }
   }

   public static final class IsFirstOfType extends Evaluator.IsNthOfType {
      public IsFirstOfType() {
         super(0, 1);
      }

      public String toString() {
         return ":first-of-type";
      }
   }

   public static final class IsLastChild extends Evaluator {
      public boolean matches(Element var1, Element var2) {
         var1 = var2.parent();
         return var1 != null && !(var1 instanceof Document) && var2.elementSiblingIndex() == var1.children().size() - 1;
      }

      public String toString() {
         return ":last-child";
      }
   }

   public static final class IsLastOfType extends Evaluator.IsNthLastOfType {
      public IsLastOfType() {
         super(0, 1);
      }

      public String toString() {
         return ":last-of-type";
      }
   }

   public static final class IsNthChild extends Evaluator.CssNthEvaluator {
      public IsNthChild(int var1, int var2) {
         super(var1, var2);
      }

      protected int calculatePosition(Element var1, Element var2) {
         return var2.elementSiblingIndex() + 1;
      }

      protected String getPseudoClass() {
         return "nth-child";
      }
   }

   public static final class IsNthLastChild extends Evaluator.CssNthEvaluator {
      public IsNthLastChild(int var1, int var2) {
         super(var1, var2);
      }

      protected int calculatePosition(Element var1, Element var2) {
         return var2.parent().children().size() - var2.elementSiblingIndex();
      }

      protected String getPseudoClass() {
         return "nth-last-child";
      }
   }

   public static class IsNthLastOfType extends Evaluator.CssNthEvaluator {
      public IsNthLastOfType(int var1, int var2) {
         super(var1, var2);
      }

      protected int calculatePosition(Element var1, Element var2) {
         int var4 = 0;
         Elements var6 = var2.parent().children();

         int var5;
         for(int var3 = var2.elementSiblingIndex(); var3 < var6.size(); var4 = var5) {
            var5 = var4;
            if (((Element)var6.get(var3)).tag().equals(var2.tag())) {
               var5 = var4 + 1;
            }

            ++var3;
         }

         return var4;
      }

      protected String getPseudoClass() {
         return "nth-last-of-type";
      }
   }

   public static class IsNthOfType extends Evaluator.CssNthEvaluator {
      public IsNthOfType(int var1, int var2) {
         super(var1, var2);
      }

      protected int calculatePosition(Element var1, Element var2) {
         int var3 = 0;
         Elements var6 = var2.parent().children();
         int var5 = 0;

         int var4;
         while(true) {
            var4 = var3;
            if (var5 >= var6.size()) {
               break;
            }

            var4 = var3;
            if (((Element)var6.get(var5)).tag().equals(var2.tag())) {
               var4 = var3 + 1;
            }

            if (var6.get(var5) == var2) {
               break;
            }

            ++var5;
            var3 = var4;
         }

         return var4;
      }

      protected String getPseudoClass() {
         return "nth-of-type";
      }
   }

   public static final class IsOnlyChild extends Evaluator {
      public boolean matches(Element var1, Element var2) {
         var1 = var2.parent();
         return var1 != null && !(var1 instanceof Document) && var2.siblingElements().size() == 0;
      }

      public String toString() {
         return ":only-child";
      }
   }

   public static final class IsOnlyOfType extends Evaluator {
      public boolean matches(Element var1, Element var2) {
         var1 = var2.parent();
         if (var1 != null && !(var1 instanceof Document)) {
            int var4 = 0;
            Elements var6 = var1.children();

            int var5;
            for(int var3 = 0; var3 < var6.size(); var4 = var5) {
               var5 = var4;
               if (((Element)var6.get(var3)).tag().equals(var2.tag())) {
                  var5 = var4 + 1;
               }

               ++var3;
            }

            if (var4 == 1) {
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      public String toString() {
         return ":only-of-type";
      }
   }

   public static final class IsRoot extends Evaluator {
      public boolean matches(Element var1, Element var2) {
         boolean var3 = false;
         if (var1 instanceof Document) {
            var1 = var1.child(0);
         }

         if (var2 == var1) {
            var3 = true;
         }

         return var3;
      }

      public String toString() {
         return ":root";
      }
   }

   public static final class Matches extends Evaluator {
      private Pattern pattern;

      public Matches(Pattern var1) {
         this.pattern = var1;
      }

      public boolean matches(Element var1, Element var2) {
         return this.pattern.matcher(var2.text()).find();
      }

      public String toString() {
         return String.format(":matches(%s", this.pattern);
      }
   }

   public static final class MatchesOwn extends Evaluator {
      private Pattern pattern;

      public MatchesOwn(Pattern var1) {
         this.pattern = var1;
      }

      public boolean matches(Element var1, Element var2) {
         return this.pattern.matcher(var2.ownText()).find();
      }

      public String toString() {
         return String.format(":matchesOwn(%s", this.pattern);
      }
   }

   public static final class Tag extends Evaluator {
      private String tagName;

      public Tag(String var1) {
         this.tagName = var1;
      }

      public boolean matches(Element var1, Element var2) {
         return var2.tagName().equals(this.tagName);
      }

      public String toString() {
         return String.format("%s", this.tagName);
      }
   }
}
