package org.jsoup.parser;

import java.util.List;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class XmlTreeBuilder extends TreeBuilder {
   private void insertNode(Node var1) {
      this.currentElement().appendChild(var1);
   }

   private void popStackToClose(Token.EndTag var1) {
      String var4 = var1.name();
      Element var3 = null;
      int var2 = this.stack.size() - 1;

      Element var5;
      while(true) {
         var5 = var3;
         if (var2 < 0) {
            break;
         }

         var5 = (Element)this.stack.get(var2);
         if (var5.nodeName().equals(var4)) {
            break;
         }

         --var2;
      }

      if (var5 != null) {
         for(var2 = this.stack.size() - 1; var2 >= 0; --var2) {
            var3 = (Element)this.stack.get(var2);
            this.stack.remove(var2);
            if (var3 == var5) {
               break;
            }
         }
      }

   }

   protected void initialiseParse(String var1, String var2, ParseErrorList var3) {
      super.initialiseParse(var1, var2, var3);
      this.stack.add(this.doc);
      this.doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
   }

   Element insert(Token.StartTag var1) {
      Tag var2 = Tag.valueOf(var1.name());
      Element var3 = new Element(var2, this.baseUri, var1.attributes);
      this.insertNode(var3);
      if (var1.isSelfClosing()) {
         this.tokeniser.acknowledgeSelfClosingFlag();
         if (!var2.isKnownTag()) {
            var2.setSelfClosing();
         }

         return var3;
      } else {
         this.stack.add(var3);
         return var3;
      }
   }

   void insert(Token.Character var1) {
      this.insertNode(new TextNode(var1.getData(), this.baseUri));
   }

   void insert(Token.Comment var1) {
      Comment var4 = new Comment(var1.getData(), this.baseUri);
      Object var3 = var4;
      if (var1.bogus) {
         String var5 = var4.getData();
         var3 = var4;
         if (var5.length() > 1) {
            label13: {
               if (!var5.startsWith("!")) {
                  var3 = var4;
                  if (!var5.startsWith("?")) {
                     break label13;
                  }
               }

               var3 = new XmlDeclaration(var5.substring(1), var4.baseUri(), var5.startsWith("!"));
            }
         }
      }

      this.insertNode((Node)var3);
   }

   void insert(Token.Doctype var1) {
      this.insertNode(new DocumentType(var1.getName(), var1.getPublicIdentifier(), var1.getSystemIdentifier(), this.baseUri));
   }

   List parseFragment(String var1, String var2, ParseErrorList var3) {
      this.initialiseParse(var1, var2, var3);
      this.runParser();
      return this.doc.childNodes();
   }

   protected boolean process(Token var1) {
      switch(var1.type) {
      case StartTag:
         this.insert(var1.asStartTag());
         break;
      case EndTag:
         this.popStackToClose(var1.asEndTag());
         break;
      case Comment:
         this.insert(var1.asComment());
         break;
      case Character:
         this.insert(var1.asCharacter());
         break;
      case Doctype:
         this.insert(var1.asDoctype());
      case EOF:
         break;
      default:
         Validate.fail("Unexpected token type: " + var1.type);
      }

      return true;
   }
}
