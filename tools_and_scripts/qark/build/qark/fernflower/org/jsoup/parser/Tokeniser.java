package org.jsoup.parser;

import java.util.Arrays;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

final class Tokeniser {
   private static final char[] notCharRefCharsSorted = new char[]{'\t', '\n', '\r', '\f', ' ', '<', '&'};
   static final char replacementChar = '�';
   Token.Character charPending;
   private final char[] charRefHolder;
   private StringBuilder charsBuilder;
   private String charsString;
   Token.Comment commentPending;
   StringBuilder dataBuffer;
   Token.Doctype doctypePending;
   private Token emitPending;
   Token.EndTag endPending;
   private ParseErrorList errors;
   private boolean isEmitPending;
   private String lastStartTag;
   private CharacterReader reader;
   private boolean selfClosingFlagAcknowledged;
   Token.StartTag startPending;
   private TokeniserState state;
   Token.Tag tagPending;

   static {
      Arrays.sort(notCharRefCharsSorted);
   }

   Tokeniser(CharacterReader var1, ParseErrorList var2) {
      this.state = TokeniserState.Data;
      this.isEmitPending = false;
      this.charsString = null;
      this.charsBuilder = new StringBuilder(1024);
      this.dataBuffer = new StringBuilder(1024);
      this.startPending = new Token.StartTag();
      this.endPending = new Token.EndTag();
      this.charPending = new Token.Character();
      this.doctypePending = new Token.Doctype();
      this.commentPending = new Token.Comment();
      this.selfClosingFlagAcknowledged = true;
      this.charRefHolder = new char[1];
      this.reader = var1;
      this.errors = var2;
   }

   private void characterReferenceError(String var1) {
      if (this.errors.canAddError()) {
         this.errors.add(new ParseError(this.reader.pos(), "Invalid character reference: %s", new Object[]{var1}));
      }

   }

   private void error(String var1) {
      if (this.errors.canAddError()) {
         this.errors.add(new ParseError(this.reader.pos(), var1));
      }

   }

   void acknowledgeSelfClosingFlag() {
      this.selfClosingFlagAcknowledged = true;
   }

   void advanceTransition(TokeniserState var1) {
      this.reader.advance();
      this.state = var1;
   }

   String appropriateEndTagName() {
      return this.lastStartTag == null ? null : this.lastStartTag;
   }

   char[] consumeCharacterReference(Character var1, boolean var2) {
      if (this.reader.isEmpty()) {
         return null;
      } else if (var1 != null && var1 == this.reader.current()) {
         return null;
      } else if (this.reader.matchesAnySorted(notCharRefCharsSorted)) {
         return null;
      } else {
         char[] var6 = this.charRefHolder;
         this.reader.mark();
         String var8;
         if (this.reader.matchConsume("#")) {
            var2 = this.reader.matchConsumeIgnoreCase("X");
            if (var2) {
               var8 = this.reader.consumeHexSequence();
            } else {
               var8 = this.reader.consumeDigitSequence();
            }

            if (var8.length() == 0) {
               this.characterReferenceError("numeric reference with no numerals");
               this.reader.rewindToMark();
               return null;
            } else {
               if (!this.reader.matchConsume(";")) {
                  this.characterReferenceError("missing semicolon");
               }

               byte var4 = -1;
               byte var9;
               if (var2) {
                  var9 = 16;
               } else {
                  var9 = 10;
               }

               int var10;
               try {
                  var10 = Integer.valueOf(var8, var9);
               } catch (NumberFormatException var7) {
                  var10 = var4;
               }

               if (var10 != -1 && (var10 < 55296 || var10 > 57343) && var10 <= 1114111) {
                  if (var10 < 65536) {
                     var6[0] = (char)var10;
                     return var6;
                  } else {
                     return Character.toChars(var10);
                  }
               } else {
                  this.characterReferenceError("character outside of valid range");
                  var6[0] = '�';
                  return var6;
               }
            }
         } else {
            var8 = this.reader.consumeLetterThenDigitSequence();
            boolean var5 = this.reader.matches(';');
            boolean var3;
            if (Entities.isBaseNamedEntity(var8) || Entities.isNamedEntity(var8) && var5) {
               var3 = true;
            } else {
               var3 = false;
            }

            if (!var3) {
               this.reader.rewindToMark();
               if (var5) {
                  this.characterReferenceError(String.format("invalid named referenece '%s'", var8));
               }

               return null;
            } else if (!var2 || !this.reader.matchesLetter() && !this.reader.matchesDigit() && !this.reader.matchesAny('=', '-', '_')) {
               if (!this.reader.matchConsume(";")) {
                  this.characterReferenceError("missing semicolon");
               }

               var6[0] = Entities.getCharacterByName(var8);
               return var6;
            } else {
               this.reader.rewindToMark();
               return null;
            }
         }
      }
   }

   void createCommentPending() {
      this.commentPending.reset();
   }

   void createDoctypePending() {
      this.doctypePending.reset();
   }

   Token.Tag createTagPending(boolean var1) {
      Token.Tag var2;
      if (var1) {
         var2 = this.startPending.reset();
      } else {
         var2 = this.endPending.reset();
      }

      this.tagPending = var2;
      return this.tagPending;
   }

   void createTempBuffer() {
      Token.reset(this.dataBuffer);
   }

   boolean currentNodeInHtmlNS() {
      return true;
   }

   void emit(char var1) {
      this.emit(String.valueOf(var1));
   }

   void emit(String var1) {
      if (this.charsString == null) {
         this.charsString = var1;
      } else {
         if (this.charsBuilder.length() == 0) {
            this.charsBuilder.append(this.charsString);
         }

         this.charsBuilder.append(var1);
      }
   }

   void emit(Token var1) {
      Validate.isFalse(this.isEmitPending, "There is an unread token pending!");
      this.emitPending = var1;
      this.isEmitPending = true;
      if (var1.type == Token.TokenType.StartTag) {
         Token.StartTag var2 = (Token.StartTag)var1;
         this.lastStartTag = var2.tagName;
         if (var2.selfClosing) {
            this.selfClosingFlagAcknowledged = false;
         }
      } else if (var1.type == Token.TokenType.EndTag && ((Token.EndTag)var1).attributes != null) {
         this.error("Attributes incorrectly present on end tag");
         return;
      }

   }

   void emit(char[] var1) {
      this.emit(String.valueOf(var1));
   }

   void emitCommentPending() {
      this.emit((Token)this.commentPending);
   }

   void emitDoctypePending() {
      this.emit((Token)this.doctypePending);
   }

   void emitTagPending() {
      this.tagPending.finaliseTag();
      this.emit((Token)this.tagPending);
   }

   void eofError(TokeniserState var1) {
      if (this.errors.canAddError()) {
         this.errors.add(new ParseError(this.reader.pos(), "Unexpectedly reached end of file (EOF) in input state [%s]", new Object[]{var1}));
      }

   }

   void error(TokeniserState var1) {
      if (this.errors.canAddError()) {
         this.errors.add(new ParseError(this.reader.pos(), "Unexpected character '%s' in input state [%s]", new Object[]{this.reader.current(), var1}));
      }

   }

   TokeniserState getState() {
      return this.state;
   }

   boolean isAppropriateEndTagToken() {
      return this.lastStartTag != null && this.tagPending.tagName.equals(this.lastStartTag);
   }

   Token read() {
      if (!this.selfClosingFlagAcknowledged) {
         this.error("Self closing flag not acknowledged");
         this.selfClosingFlagAcknowledged = true;
      }

      while(!this.isEmitPending) {
         this.state.read(this, this.reader);
      }

      if (this.charsBuilder.length() > 0) {
         String var2 = this.charsBuilder.toString();
         this.charsBuilder.delete(0, this.charsBuilder.length());
         this.charsString = null;
         return this.charPending.data(var2);
      } else if (this.charsString != null) {
         Token.Character var1 = this.charPending.data(this.charsString);
         this.charsString = null;
         return var1;
      } else {
         this.isEmitPending = false;
         return this.emitPending;
      }
   }

   void transition(TokeniserState var1) {
      this.state = var1;
   }

   String unescapeEntities(boolean var1) {
      StringBuilder var2 = new StringBuilder();

      while(true) {
         while(true) {
            do {
               if (this.reader.isEmpty()) {
                  return var2.toString();
               }

               var2.append(this.reader.consumeTo('&'));
            } while(!this.reader.matches('&'));

            this.reader.consume();
            char[] var3 = this.consumeCharacterReference((Character)null, var1);
            if (var3 != null && var3.length != 0) {
               var2.append(var3);
            } else {
               var2.append('&');
            }
         }
      }
   }
}
