package org.jsoup.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.Selector;

public class Element extends Node {
   private static final Pattern classSplit = Pattern.compile("\\s+");
   private Tag tag;

   public Element(Tag var1, String var2) {
      this(var1, var2, new Attributes());
   }

   public Element(Tag var1, String var2, Attributes var3) {
      super(var2, var3);
      Validate.notNull(var1);
      this.tag = var1;
   }

   private static void accumulateParents(Element var0, Elements var1) {
      var0 = var0.parent();
      if (var0 != null && !var0.tagName().equals("#root")) {
         var1.add(var0);
         accumulateParents(var0, var1);
      }

   }

   private static void appendNormalisedText(StringBuilder var0, TextNode var1) {
      String var2 = var1.getWholeText();
      if (preserveWhitespace(var1.parentNode)) {
         var0.append(var2);
      } else {
         StringUtil.appendNormalisedWhitespace(var0, var2, TextNode.lastCharIsWhitespace(var0));
      }
   }

   private static void appendWhitespaceIfBr(Element var0, StringBuilder var1) {
      if (var0.tag.getName().equals("br") && !TextNode.lastCharIsWhitespace(var1)) {
         var1.append(" ");
      }

   }

   private void html(StringBuilder var1) {
      Iterator var2 = this.childNodes.iterator();

      while(var2.hasNext()) {
         ((Node)var2.next()).outerHtml(var1);
      }

   }

   private static Integer indexInList(Element var0, List var1) {
      Validate.notNull(var0);
      Validate.notNull(var1);

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         if ((Element)var1.get(var2) == var0) {
            return var2;
         }
      }

      return null;
   }

   private void ownText(StringBuilder var1) {
      Iterator var2 = this.childNodes.iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         if (var3 instanceof TextNode) {
            appendNormalisedText(var1, (TextNode)var3);
         } else if (var3 instanceof Element) {
            appendWhitespaceIfBr((Element)var3, var1);
         }
      }

   }

   static boolean preserveWhitespace(Node var0) {
      boolean var2 = false;
      boolean var1 = var2;
      if (var0 != null) {
         var1 = var2;
         if (var0 instanceof Element) {
            Element var3 = (Element)var0;
            if (!var3.tag.preserveWhitespace()) {
               var1 = var2;
               if (var3.parent() == null) {
                  return var1;
               }

               var1 = var2;
               if (!var3.parent().tag.preserveWhitespace()) {
                  return var1;
               }
            }

            var1 = true;
         }
      }

      return var1;
   }

   public Element addClass(String var1) {
      Validate.notNull(var1);
      Set var2 = this.classNames();
      var2.add(var1);
      this.classNames(var2);
      return this;
   }

   public Element after(String var1) {
      return (Element)super.after(var1);
   }

   public Element after(Node var1) {
      return (Element)super.after(var1);
   }

   public Element append(String var1) {
      Validate.notNull(var1);
      List var2 = Parser.parseFragment(var1, this, this.baseUri());
      this.addChildren((Node[])var2.toArray(new Node[var2.size()]));
      return this;
   }

   public Element appendChild(Node var1) {
      Validate.notNull(var1);
      this.reparentChild(var1);
      this.ensureChildNodes();
      this.childNodes.add(var1);
      var1.setSiblingIndex(this.childNodes.size() - 1);
      return this;
   }

   public Element appendElement(String var1) {
      Element var2 = new Element(Tag.valueOf(var1), this.baseUri());
      this.appendChild(var2);
      return var2;
   }

   public Element appendText(String var1) {
      Validate.notNull(var1);
      this.appendChild(new TextNode(var1, this.baseUri()));
      return this;
   }

   public Element attr(String var1, String var2) {
      super.attr(var1, var2);
      return this;
   }

   public Element attr(String var1, boolean var2) {
      this.attributes.put(var1, var2);
      return this;
   }

   public Element before(String var1) {
      return (Element)super.before(var1);
   }

   public Element before(Node var1) {
      return (Element)super.before(var1);
   }

   public Element child(int var1) {
      return (Element)this.children().get(var1);
   }

   public Elements children() {
      ArrayList var1 = new ArrayList(this.childNodes.size());
      Iterator var2 = this.childNodes.iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         if (var3 instanceof Element) {
            var1.add((Element)var3);
         }
      }

      return new Elements(var1);
   }

   public String className() {
      return this.attr("class").trim();
   }

   public Set classNames() {
      LinkedHashSet var1 = new LinkedHashSet(Arrays.asList(classSplit.split(this.className())));
      var1.remove("");
      return var1;
   }

   public Element classNames(Set var1) {
      Validate.notNull(var1);
      this.attributes.put("class", StringUtil.join((Collection)var1, " "));
      return this;
   }

   public Element clone() {
      return (Element)super.clone();
   }

   public String cssSelector() {
      if (this.method_5().length() > 0) {
         return "#" + this.method_5();
      } else {
         StringBuilder var1 = new StringBuilder(this.tagName());
         String var2 = StringUtil.join((Collection)this.classNames(), ".");
         if (var2.length() > 0) {
            var1.append('.').append(var2);
         }

         if (this.parent() != null && !(this.parent() instanceof Document)) {
            var1.insert(0, " > ");
            if (this.parent().select(var1.toString()).size() > 1) {
               var1.append(String.format(":nth-child(%d)", this.elementSiblingIndex() + 1));
            }

            return this.parent().cssSelector() + var1.toString();
         } else {
            return var1.toString();
         }
      }
   }

   public String data() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.childNodes.iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         if (var3 instanceof DataNode) {
            var1.append(((DataNode)var3).getWholeData());
         } else if (var3 instanceof Element) {
            var1.append(((Element)var3).data());
         }
      }

      return var1.toString();
   }

   public List dataNodes() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.childNodes.iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         if (var3 instanceof DataNode) {
            var1.add((DataNode)var3);
         }
      }

      return Collections.unmodifiableList(var1);
   }

   public Map dataset() {
      return this.attributes.dataset();
   }

   public Integer elementSiblingIndex() {
      return this.parent() == null ? 0 : indexInList(this, this.parent().children());
   }

   public Element empty() {
      this.childNodes.clear();
      return this;
   }

   public Element firstElementSibling() {
      Elements var1 = this.parent().children();
      return var1.size() > 1 ? (Element)var1.get(0) : null;
   }

   public Elements getAllElements() {
      return Collector.collect(new Evaluator.AllElements(), this);
   }

   public Element getElementById(String var1) {
      Validate.notEmpty(var1);
      Elements var2 = Collector.collect(new Evaluator.class_18(var1), this);
      return var2.size() > 0 ? (Element)var2.get(0) : null;
   }

   public Elements getElementsByAttribute(String var1) {
      Validate.notEmpty(var1);
      return Collector.collect(new Evaluator.Attribute(var1.trim().toLowerCase()), this);
   }

   public Elements getElementsByAttributeStarting(String var1) {
      Validate.notEmpty(var1);
      return Collector.collect(new Evaluator.AttributeStarting(var1.trim().toLowerCase()), this);
   }

   public Elements getElementsByAttributeValue(String var1, String var2) {
      return Collector.collect(new Evaluator.AttributeWithValue(var1, var2), this);
   }

   public Elements getElementsByAttributeValueContaining(String var1, String var2) {
      return Collector.collect(new Evaluator.AttributeWithValueContaining(var1, var2), this);
   }

   public Elements getElementsByAttributeValueEnding(String var1, String var2) {
      return Collector.collect(new Evaluator.AttributeWithValueEnding(var1, var2), this);
   }

   public Elements getElementsByAttributeValueMatching(String var1, String var2) {
      Pattern var3;
      try {
         var3 = Pattern.compile(var2);
      } catch (PatternSyntaxException var4) {
         throw new IllegalArgumentException("Pattern syntax error: " + var2, var4);
      }

      return this.getElementsByAttributeValueMatching(var1, var3);
   }

   public Elements getElementsByAttributeValueMatching(String var1, Pattern var2) {
      return Collector.collect(new Evaluator.AttributeWithValueMatching(var1, var2), this);
   }

   public Elements getElementsByAttributeValueNot(String var1, String var2) {
      return Collector.collect(new Evaluator.AttributeWithValueNot(var1, var2), this);
   }

   public Elements getElementsByAttributeValueStarting(String var1, String var2) {
      return Collector.collect(new Evaluator.AttributeWithValueStarting(var1, var2), this);
   }

   public Elements getElementsByClass(String var1) {
      Validate.notEmpty(var1);
      return Collector.collect(new Evaluator.Class(var1), this);
   }

   public Elements getElementsByIndexEquals(int var1) {
      return Collector.collect(new Evaluator.IndexEquals(var1), this);
   }

   public Elements getElementsByIndexGreaterThan(int var1) {
      return Collector.collect(new Evaluator.IndexGreaterThan(var1), this);
   }

   public Elements getElementsByIndexLessThan(int var1) {
      return Collector.collect(new Evaluator.IndexLessThan(var1), this);
   }

   public Elements getElementsByTag(String var1) {
      Validate.notEmpty(var1);
      return Collector.collect(new Evaluator.Tag(var1.toLowerCase().trim()), this);
   }

   public Elements getElementsContainingOwnText(String var1) {
      return Collector.collect(new Evaluator.ContainsOwnText(var1), this);
   }

   public Elements getElementsContainingText(String var1) {
      return Collector.collect(new Evaluator.ContainsText(var1), this);
   }

   public Elements getElementsMatchingOwnText(String var1) {
      Pattern var2;
      try {
         var2 = Pattern.compile(var1);
      } catch (PatternSyntaxException var3) {
         throw new IllegalArgumentException("Pattern syntax error: " + var1, var3);
      }

      return this.getElementsMatchingOwnText(var2);
   }

   public Elements getElementsMatchingOwnText(Pattern var1) {
      return Collector.collect(new Evaluator.MatchesOwn(var1), this);
   }

   public Elements getElementsMatchingText(String var1) {
      Pattern var2;
      try {
         var2 = Pattern.compile(var1);
      } catch (PatternSyntaxException var3) {
         throw new IllegalArgumentException("Pattern syntax error: " + var1, var3);
      }

      return this.getElementsMatchingText(var2);
   }

   public Elements getElementsMatchingText(Pattern var1) {
      return Collector.collect(new Evaluator.Matches(var1), this);
   }

   public boolean hasClass(String var1) {
      String var4 = this.attributes.get("class");
      if (!var4.equals("") && var4.length() >= var1.length()) {
         String[] var5 = classSplit.split(var4);
         int var3 = var5.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if (var1.equalsIgnoreCase(var5[var2])) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean hasText() {
      Iterator var1 = this.childNodes.iterator();

      while(var1.hasNext()) {
         Node var2 = (Node)var1.next();
         if (var2 instanceof TextNode) {
            if (!((TextNode)var2).isBlank()) {
               return true;
            }
         } else if (var2 instanceof Element && ((Element)var2).hasText()) {
            return true;
         }
      }

      return false;
   }

   public Appendable html(Appendable var1) {
      Iterator var2 = this.childNodes.iterator();

      while(var2.hasNext()) {
         ((Node)var2.next()).outerHtml(var1);
      }

      return var1;
   }

   public String html() {
      StringBuilder var1 = new StringBuilder();
      this.html(var1);
      return this.getOutputSettings().prettyPrint() ? var1.toString().trim() : var1.toString();
   }

   public Element html(String var1) {
      this.empty();
      this.append(var1);
      return this;
   }

   // $FF: renamed from: id () java.lang.String
   public String method_5() {
      return this.attributes.get("id");
   }

   public Element insertChildren(int var1, Collection var2) {
      Validate.notNull(var2, "Children collection to be inserted must not be null.");
      int var4 = this.childNodeSize();
      int var3 = var1;
      if (var1 < 0) {
         var3 = var1 + var4 + 1;
      }

      boolean var5;
      if (var3 >= 0 && var3 <= var4) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "Insert position out of bounds.");
      ArrayList var6 = new ArrayList(var2);
      this.addChildren(var3, (Node[])var6.toArray(new Node[var6.size()]));
      return this;
   }

   public boolean isBlock() {
      return this.tag.isBlock();
   }

   public Element lastElementSibling() {
      Elements var1 = this.parent().children();
      return var1.size() > 1 ? (Element)var1.get(var1.size() - 1) : null;
   }

   public Element nextElementSibling() {
      if (this.parentNode != null) {
         Elements var1 = this.parent().children();
         Integer var2 = indexInList(this, var1);
         Validate.notNull(var2);
         if (var1.size() > var2 + 1) {
            return (Element)var1.get(var2 + 1);
         }
      }

      return null;
   }

   public String nodeName() {
      return this.tag.getName();
   }

   void outerHtmlHead(Appendable var1, int var2, Document.OutputSettings var3) throws IOException {
      if (var3.prettyPrint() && (this.tag.formatAsBlock() || this.parent() != null && this.parent().tag().formatAsBlock() || var3.outline())) {
         if (var1 instanceof StringBuilder) {
            if (((StringBuilder)var1).length() > 0) {
               this.indent(var1, var2, var3);
            }
         } else {
            this.indent(var1, var2, var3);
         }
      }

      var1.append("<").append(this.tagName());
      this.attributes.html(var1, var3);
      if (this.childNodes.isEmpty() && this.tag.isSelfClosing()) {
         if (var3.syntax() == Document.OutputSettings.Syntax.html && this.tag.isEmpty()) {
            var1.append('>');
         } else {
            var1.append(" />");
         }
      } else {
         var1.append(">");
      }
   }

   void outerHtmlTail(Appendable var1, int var2, Document.OutputSettings var3) throws IOException {
      if (!this.childNodes.isEmpty() || !this.tag.isSelfClosing()) {
         if (var3.prettyPrint() && !this.childNodes.isEmpty() && (this.tag.formatAsBlock() || var3.outline() && (this.childNodes.size() > 1 || this.childNodes.size() == 1 && !(this.childNodes.get(0) instanceof TextNode)))) {
            this.indent(var1, var2, var3);
         }

         var1.append("</").append(this.tagName()).append(">");
      }

   }

   public String ownText() {
      StringBuilder var1 = new StringBuilder();
      this.ownText(var1);
      return var1.toString().trim();
   }

   public final Element parent() {
      return (Element)this.parentNode;
   }

   public Elements parents() {
      Elements var1 = new Elements();
      accumulateParents(this, var1);
      return var1;
   }

   public Element prepend(String var1) {
      Validate.notNull(var1);
      List var2 = Parser.parseFragment(var1, this, this.baseUri());
      this.addChildren(0, (Node[])var2.toArray(new Node[var2.size()]));
      return this;
   }

   public Element prependChild(Node var1) {
      Validate.notNull(var1);
      this.addChildren(0, new Node[]{var1});
      return this;
   }

   public Element prependElement(String var1) {
      Element var2 = new Element(Tag.valueOf(var1), this.baseUri());
      this.prependChild(var2);
      return var2;
   }

   public Element prependText(String var1) {
      Validate.notNull(var1);
      this.prependChild(new TextNode(var1, this.baseUri()));
      return this;
   }

   public Element previousElementSibling() {
      if (this.parentNode != null) {
         Elements var1 = this.parent().children();
         Integer var2 = indexInList(this, var1);
         Validate.notNull(var2);
         if (var2 > 0) {
            return (Element)var1.get(var2 - 1);
         }
      }

      return null;
   }

   public Element removeClass(String var1) {
      Validate.notNull(var1);
      Set var2 = this.classNames();
      var2.remove(var1);
      this.classNames(var2);
      return this;
   }

   public Elements select(String var1) {
      return Selector.select(var1, this);
   }

   public Elements siblingElements() {
      Elements var1;
      if (this.parentNode == null) {
         var1 = new Elements(0);
      } else {
         var1 = this.parent().children();
         Elements var2 = new Elements(var1.size() - 1);
         Iterator var3 = var1.iterator();

         while(true) {
            var1 = var2;
            if (!var3.hasNext()) {
               break;
            }

            Element var4 = (Element)var3.next();
            if (var4 != this) {
               var2.add(var4);
            }
         }
      }

      return var1;
   }

   public Tag tag() {
      return this.tag;
   }

   public String tagName() {
      return this.tag.getName();
   }

   public Element tagName(String var1) {
      Validate.notEmpty(var1, "Tag name must not be empty.");
      this.tag = Tag.valueOf(var1);
      return this;
   }

   public String text() {
      final StringBuilder var1 = new StringBuilder();
      (new NodeTraversor(new NodeVisitor() {
         public void head(Node var1x, int var2) {
            if (var1x instanceof TextNode) {
               TextNode var3 = (TextNode)var1x;
               Element.appendNormalisedText(var1, var3);
            } else if (var1x instanceof Element) {
               Element var4 = (Element)var1x;
               if (var1.length() > 0 && (var4.isBlock() || var4.tag.getName().equals("br")) && !TextNode.lastCharIsWhitespace(var1)) {
                  var1.append(" ");
                  return;
               }
            }

         }

         public void tail(Node var1x, int var2) {
         }
      })).traverse(this);
      return var1.toString().trim();
   }

   public Element text(String var1) {
      Validate.notNull(var1);
      this.empty();
      this.appendChild(new TextNode(var1, this.baseUri));
      return this;
   }

   public List textNodes() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.childNodes.iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         if (var3 instanceof TextNode) {
            var1.add((TextNode)var3);
         }
      }

      return Collections.unmodifiableList(var1);
   }

   public String toString() {
      return this.outerHtml();
   }

   public Element toggleClass(String var1) {
      Validate.notNull(var1);
      Set var2 = this.classNames();
      if (var2.contains(var1)) {
         var2.remove(var1);
      } else {
         var2.add(var1);
      }

      this.classNames(var2);
      return this;
   }

   public String val() {
      return this.tagName().equals("textarea") ? this.text() : this.attr("value");
   }

   public Element val(String var1) {
      if (this.tagName().equals("textarea")) {
         this.text(var1);
         return this;
      } else {
         this.attr("value", var1);
         return this;
      }
   }

   public Element wrap(String var1) {
      return (Element)super.wrap(var1);
   }
}
