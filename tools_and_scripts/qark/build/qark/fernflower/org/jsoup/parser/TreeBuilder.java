package org.jsoup.parser;

import java.util.ArrayList;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

abstract class TreeBuilder {
   protected String baseUri;
   protected Token currentToken;
   protected Document doc;
   private Token.EndTag end = new Token.EndTag();
   protected ParseErrorList errors;
   CharacterReader reader;
   protected ArrayList stack;
   private Token.StartTag start = new Token.StartTag();
   Tokeniser tokeniser;

   protected Element currentElement() {
      int var1 = this.stack.size();
      return var1 > 0 ? (Element)this.stack.get(var1 - 1) : null;
   }

   protected void initialiseParse(String var1, String var2, ParseErrorList var3) {
      Validate.notNull(var1, "String input must not be null");
      Validate.notNull(var2, "BaseURI must not be null");
      this.doc = new Document(var2);
      this.reader = new CharacterReader(var1);
      this.errors = var3;
      this.tokeniser = new Tokeniser(this.reader, var3);
      this.stack = new ArrayList(32);
      this.baseUri = var2;
   }

   Document parse(String var1, String var2) {
      return this.parse(var1, var2, ParseErrorList.noTracking());
   }

   Document parse(String var1, String var2, ParseErrorList var3) {
      this.initialiseParse(var1, var2, var3);
      this.runParser();
      return this.doc;
   }

   protected abstract boolean process(Token var1);

   protected boolean processEndTag(String var1) {
      return this.currentToken == this.end ? this.process((new Token.EndTag()).name(var1)) : this.process(this.end.reset().name(var1));
   }

   protected boolean processStartTag(String var1) {
      return this.currentToken == this.start ? this.process((new Token.StartTag()).name(var1)) : this.process(this.start.reset().name(var1));
   }

   public boolean processStartTag(String var1, Attributes var2) {
      if (this.currentToken == this.start) {
         return this.process((new Token.StartTag()).nameAttr(var1, var2));
      } else {
         this.start.reset();
         this.start.nameAttr(var1, var2);
         return this.process(this.start);
      }
   }

   protected void runParser() {
      Token var1;
      do {
         var1 = this.tokeniser.read();
         this.process(var1);
         var1.reset();
      } while(var1.type != Token.TokenType.EOF);

   }
}
