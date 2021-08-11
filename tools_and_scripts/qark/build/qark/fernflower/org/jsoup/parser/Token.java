package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

abstract class Token {
   Token.TokenType type;

   private Token() {
   }

   // $FF: synthetic method
   Token(Object var1) {
      this();
   }

   static void reset(StringBuilder var0) {
      if (var0 != null) {
         var0.delete(0, var0.length());
      }

   }

   final Token.Character asCharacter() {
      return (Token.Character)this;
   }

   final Token.Comment asComment() {
      return (Token.Comment)this;
   }

   final Token.Doctype asDoctype() {
      return (Token.Doctype)this;
   }

   final Token.EndTag asEndTag() {
      return (Token.EndTag)this;
   }

   final Token.StartTag asStartTag() {
      return (Token.StartTag)this;
   }

   final boolean isCharacter() {
      return this.type == Token.TokenType.Character;
   }

   final boolean isComment() {
      return this.type == Token.TokenType.Comment;
   }

   final boolean isDoctype() {
      return this.type == Token.TokenType.Doctype;
   }

   final boolean isEOF() {
      return this.type == Token.TokenType.EOF;
   }

   final boolean isEndTag() {
      return this.type == Token.TokenType.EndTag;
   }

   final boolean isStartTag() {
      return this.type == Token.TokenType.StartTag;
   }

   abstract Token reset();

   String tokenType() {
      return this.getClass().getSimpleName();
   }

   static final class Character extends Token {
      private String data;

      Character() {
         super(null);
         this.type = Token.TokenType.Character;
      }

      Token.Character data(String var1) {
         this.data = var1;
         return this;
      }

      String getData() {
         return this.data;
      }

      Token reset() {
         this.data = null;
         return this;
      }

      public String toString() {
         return this.getData();
      }
   }

   static final class Comment extends Token {
      boolean bogus = false;
      final StringBuilder data = new StringBuilder();

      Comment() {
         super(null);
         this.type = Token.TokenType.Comment;
      }

      String getData() {
         return this.data.toString();
      }

      Token reset() {
         reset(this.data);
         this.bogus = false;
         return this;
      }

      public String toString() {
         return "<!--" + this.getData() + "-->";
      }
   }

   static final class Doctype extends Token {
      boolean forceQuirks = false;
      final StringBuilder name = new StringBuilder();
      final StringBuilder publicIdentifier = new StringBuilder();
      final StringBuilder systemIdentifier = new StringBuilder();

      Doctype() {
         super(null);
         this.type = Token.TokenType.Doctype;
      }

      String getName() {
         return this.name.toString();
      }

      String getPublicIdentifier() {
         return this.publicIdentifier.toString();
      }

      public String getSystemIdentifier() {
         return this.systemIdentifier.toString();
      }

      public boolean isForceQuirks() {
         return this.forceQuirks;
      }

      Token reset() {
         reset(this.name);
         reset(this.publicIdentifier);
         reset(this.systemIdentifier);
         this.forceQuirks = false;
         return this;
      }
   }

   static final class EOF extends Token {
      EOF() {
         super(null);
         this.type = Token.TokenType.EOF;
      }

      Token reset() {
         return this;
      }
   }

   static final class EndTag extends Token.Tag {
      EndTag() {
         this.type = Token.TokenType.EndTag;
      }

      public String toString() {
         return "</" + this.name() + ">";
      }
   }

   static final class StartTag extends Token.Tag {
      StartTag() {
         this.attributes = new Attributes();
         this.type = Token.TokenType.StartTag;
      }

      Token.StartTag nameAttr(String var1, Attributes var2) {
         this.tagName = var1;
         this.attributes = var2;
         return this;
      }

      Token.Tag reset() {
         super.reset();
         this.attributes = new Attributes();
         return this;
      }

      public String toString() {
         return this.attributes != null && this.attributes.size() > 0 ? "<" + this.name() + " " + this.attributes.toString() + ">" : "<" + this.name() + ">";
      }
   }

   abstract static class Tag extends Token {
      Attributes attributes;
      private boolean hasEmptyAttributeValue = false;
      private boolean hasPendingAttributeValue = false;
      private String pendingAttributeName;
      private StringBuilder pendingAttributeValue = new StringBuilder();
      private String pendingAttributeValueS;
      boolean selfClosing = false;
      protected String tagName;

      Tag() {
         super(null);
      }

      private void ensureAttributeValue() {
         this.hasPendingAttributeValue = true;
         if (this.pendingAttributeValueS != null) {
            this.pendingAttributeValue.append(this.pendingAttributeValueS);
            this.pendingAttributeValueS = null;
         }

      }

      final void appendAttributeName(char var1) {
         this.appendAttributeName(String.valueOf(var1));
      }

      final void appendAttributeName(String var1) {
         if (this.pendingAttributeName != null) {
            var1 = this.pendingAttributeName.concat(var1);
         }

         this.pendingAttributeName = var1;
      }

      final void appendAttributeValue(char var1) {
         this.ensureAttributeValue();
         this.pendingAttributeValue.append(var1);
      }

      final void appendAttributeValue(String var1) {
         this.ensureAttributeValue();
         if (this.pendingAttributeValue.length() == 0) {
            this.pendingAttributeValueS = var1;
         } else {
            this.pendingAttributeValue.append(var1);
         }
      }

      final void appendAttributeValue(char[] var1) {
         this.ensureAttributeValue();
         this.pendingAttributeValue.append(var1);
      }

      final void appendTagName(char var1) {
         this.appendTagName(String.valueOf(var1));
      }

      final void appendTagName(String var1) {
         if (this.tagName != null) {
            var1 = this.tagName.concat(var1);
         }

         this.tagName = var1;
      }

      final void finaliseTag() {
         if (this.pendingAttributeName != null) {
            this.newAttribute();
         }

      }

      final Attributes getAttributes() {
         return this.attributes;
      }

      final boolean isSelfClosing() {
         return this.selfClosing;
      }

      final String name() {
         boolean var1;
         if (this.tagName != null && this.tagName.length() != 0) {
            var1 = false;
         } else {
            var1 = true;
         }

         Validate.isFalse(var1);
         return this.tagName;
      }

      final Token.Tag name(String var1) {
         this.tagName = var1;
         return this;
      }

      final void newAttribute() {
         if (this.attributes == null) {
            this.attributes = new Attributes();
         }

         if (this.pendingAttributeName != null) {
            Object var3;
            if (this.hasPendingAttributeValue) {
               String var2 = this.pendingAttributeName;
               String var1;
               if (this.pendingAttributeValue.length() > 0) {
                  var1 = this.pendingAttributeValue.toString();
               } else {
                  var1 = this.pendingAttributeValueS;
               }

               var3 = new Attribute(var2, var1);
            } else if (this.hasEmptyAttributeValue) {
               var3 = new Attribute(this.pendingAttributeName, "");
            } else {
               var3 = new BooleanAttribute(this.pendingAttributeName);
            }

            this.attributes.put((Attribute)var3);
         }

         this.pendingAttributeName = null;
         this.hasEmptyAttributeValue = false;
         this.hasPendingAttributeValue = false;
         reset(this.pendingAttributeValue);
         this.pendingAttributeValueS = null;
      }

      Token.Tag reset() {
         this.tagName = null;
         this.pendingAttributeName = null;
         reset(this.pendingAttributeValue);
         this.pendingAttributeValueS = null;
         this.hasEmptyAttributeValue = false;
         this.hasPendingAttributeValue = false;
         this.selfClosing = false;
         this.attributes = null;
         return this;
      }

      final void setEmptyAttributeValue() {
         this.hasEmptyAttributeValue = true;
      }
   }

   static enum TokenType {
      Character,
      Comment,
      Doctype,
      EOF,
      EndTag,
      StartTag;
   }
}
