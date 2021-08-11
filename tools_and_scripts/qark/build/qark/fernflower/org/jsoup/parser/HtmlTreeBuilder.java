package org.jsoup.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class HtmlTreeBuilder extends TreeBuilder {
   // $FF: synthetic field
   static final boolean $assertionsDisabled;
   private static final String[] TagSearchButton;
   private static final String[] TagSearchEndTags;
   private static final String[] TagSearchList;
   private static final String[] TagSearchSelectScope;
   private static final String[] TagSearchSpecial;
   private static final String[] TagSearchTableScope;
   private static final String[] TagsScriptStyle;
   public static final String[] TagsSearchInScope;
   private boolean baseUriSetFromDoc = false;
   private Element contextElement;
   private Token.EndTag emptyEnd = new Token.EndTag();
   private FormElement formElement;
   private ArrayList formattingElements = new ArrayList();
   private boolean fosterInserts = false;
   private boolean fragmentParsing = false;
   private boolean framesetOk = true;
   private Element headElement;
   private HtmlTreeBuilderState originalState;
   private List pendingTableCharacters = new ArrayList();
   private String[] specificScopeTarget = new String[]{null};
   private HtmlTreeBuilderState state;

   static {
      boolean var0;
      if (!HtmlTreeBuilder.class.desiredAssertionStatus()) {
         var0 = true;
      } else {
         var0 = false;
      }

      $assertionsDisabled = var0;
      TagsScriptStyle = new String[]{"script", "style"};
      TagsSearchInScope = new String[]{"applet", "caption", "html", "table", "td", "th", "marquee", "object"};
      TagSearchList = new String[]{"ol", "ul"};
      TagSearchButton = new String[]{"button"};
      TagSearchTableScope = new String[]{"html", "table"};
      TagSearchSelectScope = new String[]{"optgroup", "option"};
      TagSearchEndTags = new String[]{"dd", "dt", "li", "option", "optgroup", "p", "rp", "rt"};
      TagSearchSpecial = new String[]{"address", "applet", "area", "article", "aside", "base", "basefont", "bgsound", "blockquote", "body", "br", "button", "caption", "center", "col", "colgroup", "command", "dd", "details", "dir", "div", "dl", "dt", "embed", "fieldset", "figcaption", "figure", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hgroup", "hr", "html", "iframe", "img", "input", "isindex", "li", "link", "listing", "marquee", "menu", "meta", "nav", "noembed", "noframes", "noscript", "object", "ol", "p", "param", "plaintext", "pre", "script", "section", "select", "style", "summary", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr", "ul", "wbr", "xmp"};
   }

   HtmlTreeBuilder() {
   }

   private void clearStackToContext(String... var1) {
      for(int var2 = this.stack.size() - 1; var2 >= 0; --var2) {
         Element var3 = (Element)this.stack.get(var2);
         if (StringUtil.method_16(var3.nodeName(), var1) || var3.nodeName().equals("html")) {
            break;
         }

         this.stack.remove(var2);
      }

   }

   private boolean inSpecificScope(String var1, String[] var2, String[] var3) {
      this.specificScopeTarget[0] = var1;
      return this.inSpecificScope(this.specificScopeTarget, var2, var3);
   }

   private boolean inSpecificScope(String[] var1, String[] var2, String[] var3) {
      boolean var6 = false;
      int var4 = this.stack.size() - 1;

      boolean var5;
      while(true) {
         if (var4 < 0) {
            Validate.fail("Should not be reachable");
            return false;
         }

         String var7 = ((Element)this.stack.get(var4)).nodeName();
         if (StringUtil.method_16(var7, var1)) {
            var5 = true;
            break;
         }

         var5 = var6;
         if (StringUtil.method_16(var7, var2)) {
            break;
         }

         if (var3 != null) {
            var5 = var6;
            if (StringUtil.method_16(var7, var3)) {
               break;
            }
         }

         --var4;
      }

      return var5;
   }

   private void insertNode(Node var1) {
      if (this.stack.size() == 0) {
         this.doc.appendChild(var1);
      } else if (this.isFosterInserts()) {
         this.insertInFosterParent(var1);
      } else {
         this.currentElement().appendChild(var1);
      }

      if (var1 instanceof Element && ((Element)var1).tag().isFormListed() && this.formElement != null) {
         this.formElement.addElement((Element)var1);
      }

   }

   private boolean isElementInQueue(ArrayList var1, Element var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         if ((Element)var1.get(var3) == var2) {
            return true;
         }
      }

      return false;
   }

   private boolean isSameFormattingElement(Element var1, Element var2) {
      return var1.nodeName().equals(var2.nodeName()) && var1.attributes().equals(var2.attributes());
   }

   private void replaceInQueue(ArrayList var1, Element var2, Element var3) {
      int var4 = var1.lastIndexOf(var2);
      boolean var5;
      if (var4 != -1) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5);
      var1.set(var4, var3);
   }

   Element aboveOnStack(Element var1) {
      if (!$assertionsDisabled && !this.onStack(var1)) {
         throw new AssertionError();
      } else {
         for(int var2 = this.stack.size() - 1; var2 >= 0; --var2) {
            if ((Element)this.stack.get(var2) == var1) {
               return (Element)this.stack.get(var2 - 1);
            }
         }

         return null;
      }
   }

   void clearFormattingElementsToLastMarker() {
      while(!this.formattingElements.isEmpty() && this.removeLastFormattingElement() != null) {
      }

   }

   void clearStackToTableBodyContext() {
      this.clearStackToContext("tbody", "tfoot", "thead");
   }

   void clearStackToTableContext() {
      this.clearStackToContext("table");
   }

   void clearStackToTableRowContext() {
      this.clearStackToContext("tr");
   }

   void error(HtmlTreeBuilderState var1) {
      if (this.errors.canAddError()) {
         this.errors.add(new ParseError(this.reader.pos(), "Unexpected token [%s] when in state [%s]", new Object[]{this.currentToken.tokenType(), var1}));
      }

   }

   void framesetOk(boolean var1) {
      this.framesetOk = var1;
   }

   boolean framesetOk() {
      return this.framesetOk;
   }

   void generateImpliedEndTags() {
      this.generateImpliedEndTags((String)null);
   }

   void generateImpliedEndTags(String var1) {
      while(var1 != null && !this.currentElement().nodeName().equals(var1) && StringUtil.method_16(this.currentElement().nodeName(), TagSearchEndTags)) {
         this.pop();
      }

   }

   Element getActiveFormattingElement(String var1) {
      int var2 = this.formattingElements.size() - 1;

      Element var3;
      while(true) {
         if (var2 >= 0) {
            Element var4 = (Element)this.formattingElements.get(var2);
            if (var4 != null) {
               var3 = var4;
               if (var4.nodeName().equals(var1)) {
                  break;
               }

               --var2;
               continue;
            }
         }

         var3 = null;
         break;
      }

      return var3;
   }

   String getBaseUri() {
      return this.baseUri;
   }

   Document getDocument() {
      return this.doc;
   }

   FormElement getFormElement() {
      return this.formElement;
   }

   Element getFromStack(String var1) {
      for(int var2 = this.stack.size() - 1; var2 >= 0; --var2) {
         Element var3 = (Element)this.stack.get(var2);
         if (var3.nodeName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   Element getHeadElement() {
      return this.headElement;
   }

   List getPendingTableCharacters() {
      return this.pendingTableCharacters;
   }

   ArrayList getStack() {
      return this.stack;
   }

   boolean inButtonScope(String var1) {
      return this.inScope(var1, TagSearchButton);
   }

   boolean inListItemScope(String var1) {
      return this.inScope(var1, TagSearchList);
   }

   boolean inScope(String var1) {
      return this.inScope(var1, (String[])null);
   }

   boolean inScope(String var1, String[] var2) {
      return this.inSpecificScope(var1, TagsSearchInScope, var2);
   }

   boolean inScope(String[] var1) {
      return this.inSpecificScope((String[])var1, TagsSearchInScope, (String[])null);
   }

   boolean inSelectScope(String var1) {
      boolean var4 = false;
      int var2 = this.stack.size() - 1;

      boolean var3;
      while(true) {
         if (var2 < 0) {
            Validate.fail("Should not be reachable");
            return false;
         }

         String var5 = ((Element)this.stack.get(var2)).nodeName();
         if (var5.equals(var1)) {
            var3 = true;
            break;
         }

         var3 = var4;
         if (!StringUtil.method_16(var5, TagSearchSelectScope)) {
            break;
         }

         --var2;
      }

      return var3;
   }

   boolean inTableScope(String var1) {
      return this.inSpecificScope((String)var1, TagSearchTableScope, (String[])null);
   }

   Element insert(Token.StartTag var1) {
      Element var2;
      if (var1.isSelfClosing()) {
         var2 = this.insertEmpty(var1);
         this.stack.add(var2);
         this.tokeniser.transition(TokeniserState.Data);
         this.tokeniser.emit((Token)this.emptyEnd.reset().name(var2.tagName()));
         return var2;
      } else {
         var2 = new Element(Tag.valueOf(var1.name()), this.baseUri, var1.attributes);
         this.insert(var2);
         return var2;
      }
   }

   void insert(Element var1) {
      this.insertNode(var1);
      this.stack.add(var1);
   }

   void insert(Token.Character var1) {
      String var2 = this.currentElement().tagName();
      Object var3;
      if (!var2.equals("script") && !var2.equals("style")) {
         var3 = new TextNode(var1.getData(), this.baseUri);
      } else {
         var3 = new DataNode(var1.getData(), this.baseUri);
      }

      this.currentElement().appendChild((Node)var3);
   }

   void insert(Token.Comment var1) {
      this.insertNode(new Comment(var1.getData(), this.baseUri));
   }

   Element insertEmpty(Token.StartTag var1) {
      Tag var2 = Tag.valueOf(var1.name());
      Element var3 = new Element(var2, this.baseUri, var1.attributes);
      this.insertNode(var3);
      if (var1.isSelfClosing()) {
         if (!var2.isKnownTag()) {
            var2.setSelfClosing();
            this.tokeniser.acknowledgeSelfClosingFlag();
            return var3;
         }

         if (var2.isSelfClosing()) {
            this.tokeniser.acknowledgeSelfClosingFlag();
         }
      }

      return var3;
   }

   FormElement insertForm(Token.StartTag var1, boolean var2) {
      FormElement var3 = new FormElement(Tag.valueOf(var1.name()), this.baseUri, var1.attributes);
      this.setFormElement(var3);
      this.insertNode(var3);
      if (var2) {
         this.stack.add(var3);
      }

      return var3;
   }

   void insertInFosterParent(Node var1) {
      Element var4 = this.getFromStack("table");
      boolean var2 = false;
      Element var3;
      if (var4 != null) {
         if (var4.parent() != null) {
            var3 = var4.parent();
            var2 = true;
         } else {
            var3 = this.aboveOnStack(var4);
         }
      } else {
         var3 = (Element)this.stack.get(0);
      }

      if (var2) {
         Validate.notNull(var4);
         var4.before(var1);
      } else {
         var3.appendChild(var1);
      }
   }

   void insertMarkerToFormattingElements() {
      this.formattingElements.add((Object)null);
   }

   void insertOnStackAfter(Element var1, Element var2) {
      int var3 = this.stack.lastIndexOf(var1);
      boolean var4;
      if (var3 != -1) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4);
      this.stack.add(var3 + 1, var2);
   }

   Element insertStartTag(String var1) {
      Element var2 = new Element(Tag.valueOf(var1), this.baseUri);
      this.insert(var2);
      return var2;
   }

   boolean isFosterInserts() {
      return this.fosterInserts;
   }

   boolean isFragmentParsing() {
      return this.fragmentParsing;
   }

   boolean isInActiveFormattingElements(Element var1) {
      return this.isElementInQueue(this.formattingElements, var1);
   }

   boolean isSpecial(Element var1) {
      return StringUtil.method_16(var1.nodeName(), TagSearchSpecial);
   }

   Element lastFormattingElement() {
      return this.formattingElements.size() > 0 ? (Element)this.formattingElements.get(this.formattingElements.size() - 1) : null;
   }

   void markInsertionMode() {
      this.originalState = this.state;
   }

   void maybeSetBaseUri(Element var1) {
      if (!this.baseUriSetFromDoc) {
         String var2 = var1.absUrl("href");
         if (var2.length() != 0) {
            this.baseUri = var2;
            this.baseUriSetFromDoc = true;
            this.doc.setBaseUri(var2);
            return;
         }
      }

   }

   void newPendingTableCharacters() {
      this.pendingTableCharacters = new ArrayList();
   }

   boolean onStack(Element var1) {
      return this.isElementInQueue(this.stack, var1);
   }

   HtmlTreeBuilderState originalState() {
      return this.originalState;
   }

   Document parse(String var1, String var2, ParseErrorList var3) {
      this.state = HtmlTreeBuilderState.Initial;
      this.baseUriSetFromDoc = false;
      return super.parse(var1, var2, var3);
   }

   List parseFragment(String var1, Element var2, String var3, ParseErrorList var4) {
      this.state = HtmlTreeBuilderState.Initial;
      this.initialiseParse(var1, var3, var4);
      this.contextElement = var2;
      this.fragmentParsing = true;
      Element var5 = null;
      if (var2 != null) {
         if (var2.ownerDocument() != null) {
            this.doc.quirksMode(var2.ownerDocument().quirksMode());
         }

         var1 = var2.tagName();
         if (StringUtil.method_16(var1, "title", "textarea")) {
            this.tokeniser.transition(TokeniserState.Rcdata);
         } else if (StringUtil.method_16(var1, "iframe", "noembed", "noframes", "style", "xmp")) {
            this.tokeniser.transition(TokeniserState.Rawtext);
         } else if (var1.equals("script")) {
            this.tokeniser.transition(TokeniserState.ScriptData);
         } else if (var1.equals("noscript")) {
            this.tokeniser.transition(TokeniserState.Data);
         } else if (var1.equals("plaintext")) {
            this.tokeniser.transition(TokeniserState.Data);
         } else {
            this.tokeniser.transition(TokeniserState.Data);
         }

         Element var7 = new Element(Tag.valueOf("html"), var3);
         this.doc.appendChild(var7);
         this.stack.add(var7);
         this.resetInsertionMode();
         Elements var6 = var2.parents();
         var6.add(0, var2);
         Iterator var8 = var6.iterator();

         while(true) {
            var5 = var7;
            if (!var8.hasNext()) {
               break;
            }

            var5 = (Element)var8.next();
            if (var5 instanceof FormElement) {
               this.formElement = (FormElement)var5;
               var5 = var7;
               break;
            }
         }
      }

      this.runParser();
      return var2 != null && var5 != null ? var5.childNodes() : this.doc.childNodes();
   }

   Element pop() {
      int var1 = this.stack.size();
      return (Element)this.stack.remove(var1 - 1);
   }

   void popStackToBefore(String var1) {
      for(int var2 = this.stack.size() - 1; var2 >= 0 && !((Element)this.stack.get(var2)).nodeName().equals(var1); --var2) {
         this.stack.remove(var2);
      }

   }

   void popStackToClose(String var1) {
      for(int var2 = this.stack.size() - 1; var2 >= 0; --var2) {
         Element var3 = (Element)this.stack.get(var2);
         this.stack.remove(var2);
         if (var3.nodeName().equals(var1)) {
            break;
         }
      }

   }

   void popStackToClose(String... var1) {
      for(int var2 = this.stack.size() - 1; var2 >= 0; --var2) {
         Element var3 = (Element)this.stack.get(var2);
         this.stack.remove(var2);
         if (StringUtil.method_16(var3.nodeName(), var1)) {
            break;
         }
      }

   }

   protected boolean process(Token var1) {
      this.currentToken = var1;
      return this.state.process(var1, this);
   }

   boolean process(Token var1, HtmlTreeBuilderState var2) {
      this.currentToken = var1;
      return var2.process(var1, this);
   }

   void push(Element var1) {
      this.stack.add(var1);
   }

   void pushActiveFormattingElements(Element var1) {
      int var3 = 0;

      int var4;
      for(int var2 = this.formattingElements.size() - 1; var2 >= 0; var3 = var4) {
         Element var5 = (Element)this.formattingElements.get(var2);
         if (var5 == null) {
            break;
         }

         var4 = var3;
         if (this.isSameFormattingElement(var1, var5)) {
            var4 = var3 + 1;
         }

         if (var4 == 3) {
            this.formattingElements.remove(var2);
            break;
         }

         --var2;
      }

      this.formattingElements.add(var1);
   }

   void reconstructFormattingElements() {
      Element var6 = this.lastFormattingElement();
      if (var6 != null && !this.onStack(var6)) {
         int var5 = this.formattingElements.size();
         int var1 = var5 - 1;
         boolean var4 = false;

         boolean var2;
         int var3;
         Element var7;
         ArrayList var8;
         while(true) {
            if (var1 == 0) {
               var2 = true;
               break;
            }

            var8 = this.formattingElements;
            var3 = var1 - 1;
            var7 = (Element)var8.get(var3);
            var6 = var7;
            var1 = var3;
            var2 = var4;
            if (var7 == null) {
               break;
            }

            var6 = var7;
            var1 = var3;
            if (this.onStack(var7)) {
               var6 = var7;
               var1 = var3;
               var2 = var4;
               break;
            }
         }

         do {
            var3 = var1;
            if (!var2) {
               var8 = this.formattingElements;
               var3 = var1 + 1;
               var6 = (Element)var8.get(var3);
            }

            Validate.notNull(var6);
            var2 = false;
            var7 = this.insertStartTag(var6.nodeName());
            var7.attributes().addAll(var6.attributes());
            this.formattingElements.set(var3, var7);
            var1 = var3;
         } while(var3 != var5 - 1);

      }
   }

   void removeFromActiveFormattingElements(Element var1) {
      for(int var2 = this.formattingElements.size() - 1; var2 >= 0; --var2) {
         if ((Element)this.formattingElements.get(var2) == var1) {
            this.formattingElements.remove(var2);
            break;
         }
      }

   }

   boolean removeFromStack(Element var1) {
      for(int var2 = this.stack.size() - 1; var2 >= 0; --var2) {
         if ((Element)this.stack.get(var2) == var1) {
            this.stack.remove(var2);
            return true;
         }
      }

      return false;
   }

   Element removeLastFormattingElement() {
      int var1 = this.formattingElements.size();
      return var1 > 0 ? (Element)this.formattingElements.remove(var1 - 1) : null;
   }

   void replaceActiveFormattingElement(Element var1, Element var2) {
      this.replaceInQueue(this.formattingElements, var1, var2);
   }

   void replaceOnStack(Element var1, Element var2) {
      this.replaceInQueue(this.stack, var1, var2);
   }

   void resetInsertionMode() {
      boolean var2 = false;
      int var1 = this.stack.size() - 1;

      while(true) {
         if (var1 >= 0) {
            Element var3 = (Element)this.stack.get(var1);
            if (var1 == 0) {
               var2 = true;
               var3 = this.contextElement;
            }

            String var4 = var3.nodeName();
            if (!"select".equals(var4)) {
               if (!"td".equals(var4) && (!"th".equals(var4) || var2)) {
                  if ("tr".equals(var4)) {
                     this.transition(HtmlTreeBuilderState.InRow);
                     return;
                  }

                  if (!"tbody".equals(var4) && !"thead".equals(var4) && !"tfoot".equals(var4)) {
                     if ("caption".equals(var4)) {
                        this.transition(HtmlTreeBuilderState.InCaption);
                        return;
                     }

                     if ("colgroup".equals(var4)) {
                        this.transition(HtmlTreeBuilderState.InColumnGroup);
                        return;
                     }

                     if ("table".equals(var4)) {
                        this.transition(HtmlTreeBuilderState.InTable);
                        return;
                     }

                     if ("head".equals(var4)) {
                        this.transition(HtmlTreeBuilderState.InBody);
                        return;
                     }

                     if ("body".equals(var4)) {
                        this.transition(HtmlTreeBuilderState.InBody);
                        return;
                     }

                     if ("frameset".equals(var4)) {
                        this.transition(HtmlTreeBuilderState.InFrameset);
                        return;
                     }

                     if ("html".equals(var4)) {
                        this.transition(HtmlTreeBuilderState.BeforeHead);
                        return;
                     }

                     if (var2) {
                        this.transition(HtmlTreeBuilderState.InBody);
                        return;
                     }

                     --var1;
                     continue;
                  }

                  this.transition(HtmlTreeBuilderState.InTableBody);
                  return;
               }

               this.transition(HtmlTreeBuilderState.InCell);
               return;
            }

            this.transition(HtmlTreeBuilderState.InSelect);
         }

         return;
      }
   }

   void setFormElement(FormElement var1) {
      this.formElement = var1;
   }

   void setFosterInserts(boolean var1) {
      this.fosterInserts = var1;
   }

   void setHeadElement(Element var1) {
      this.headElement = var1;
   }

   void setPendingTableCharacters(List var1) {
      this.pendingTableCharacters = var1;
   }

   HtmlTreeBuilderState state() {
      return this.state;
   }

   public String toString() {
      return "TreeBuilder{currentToken=" + this.currentToken + ", state=" + this.state + ", currentElement=" + this.currentElement() + '}';
   }

   void transition(HtmlTreeBuilderState var1) {
      this.state = var1;
   }
}
