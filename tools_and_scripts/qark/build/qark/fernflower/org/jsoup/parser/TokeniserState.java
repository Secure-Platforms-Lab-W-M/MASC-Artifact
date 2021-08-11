package org.jsoup.parser;

import java.util.Arrays;

enum TokeniserState {
   AfterAttributeName {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeName('�');
            var1.transition(AttributeName);
            return;
         case '"':
         case '\'':
         case '<':
            var1.error((TokeniserState)this);
            var1.tagPending.newAttribute();
            var1.tagPending.appendAttributeName(var3);
            var1.transition(AttributeName);
            return;
         case '/':
            var1.transition(SelfClosingStartTag);
            return;
         case '=':
            var1.transition(BeforeAttributeValue);
            return;
         case '>':
            var1.emitTagPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
            var1.tagPending.newAttribute();
            var2.unconsume();
            var1.transition(AttributeName);
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
         }
      }
   },
   AfterAttributeValue_quoted {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            var1.transition(BeforeAttributeName);
            return;
         case '/':
            var1.transition(SelfClosingStartTag);
            return;
         case '>':
            var1.emitTagPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var2.unconsume();
            var1.transition(BeforeAttributeName);
         }
      }
   },
   AfterDoctypeName {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.isEmpty()) {
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
         } else if (var2.matchesAny('\t', '\n', '\r', '\f', ' ')) {
            var2.advance();
         } else if (var2.matches('>')) {
            var1.emitDoctypePending();
            var1.advanceTransition(Data);
         } else if (var2.matchConsumeIgnoreCase("PUBLIC")) {
            var1.transition(AfterDoctypePublicKeyword);
         } else if (var2.matchConsumeIgnoreCase("SYSTEM")) {
            var1.transition(AfterDoctypeSystemKeyword);
         } else {
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.advanceTransition(BogusDoctype);
         }
      }
   },
   AfterDoctypePublicIdentifier {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            var1.transition(BetweenDoctypePublicAndSystemIdentifiers);
            return;
         case '"':
            var1.error((TokeniserState)this);
            var1.transition(DoctypeSystemIdentifier_doubleQuoted);
            return;
         case '\'':
            var1.error((TokeniserState)this);
            var1.transition(DoctypeSystemIdentifier_singleQuoted);
            return;
         case '>':
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.transition(BogusDoctype);
         }
      }
   },
   AfterDoctypePublicKeyword {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            var1.transition(BeforeDoctypePublicIdentifier);
            return;
         case '"':
            var1.error((TokeniserState)this);
            var1.transition(DoctypePublicIdentifier_doubleQuoted);
            return;
         case '\'':
            var1.error((TokeniserState)this);
            var1.transition(DoctypePublicIdentifier_singleQuoted);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.transition(BogusDoctype);
         }
      }
   },
   AfterDoctypeSystemIdentifier {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '>':
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.transition(BogusDoctype);
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
         }
      }
   },
   AfterDoctypeSystemKeyword {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            var1.transition(BeforeDoctypeSystemIdentifier);
            return;
         case '"':
            var1.error((TokeniserState)this);
            var1.transition(DoctypeSystemIdentifier_doubleQuoted);
            return;
         case '\'':
            var1.error((TokeniserState)this);
            var1.transition(DoctypeSystemIdentifier_singleQuoted);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
         }
      }
   },
   AttributeName {
      void read(Tokeniser var1, CharacterReader var2) {
         String var4 = var2.consumeToAnySorted(TokeniserState.attributeNameCharsSorted);
         var1.tagPending.appendAttributeName(var4.toLowerCase());
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeName('�');
            return;
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            var1.transition(AfterAttributeName);
            return;
         case '"':
         case '\'':
         case '<':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeName(var3);
            return;
         case '/':
            var1.transition(SelfClosingStartTag);
            return;
         case '=':
            var1.transition(BeforeAttributeValue);
            return;
         case '>':
            var1.emitTagPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
         }
      }
   },
   AttributeValue_doubleQuoted {
      void read(Tokeniser var1, CharacterReader var2) {
         String var3 = var2.consumeToAny(TokeniserState.attributeDoubleValueCharsSorted);
         if (var3.length() > 0) {
            var1.tagPending.appendAttributeValue(var3);
         } else {
            var1.tagPending.setEmptyAttributeValue();
         }

         switch(var2.consume()) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeValue('�');
            return;
         case '"':
            var1.transition(AfterAttributeValue_quoted);
            return;
         case '&':
            char[] var4 = var1.consumeCharacterReference('"', true);
            if (var4 != null) {
               var1.tagPending.appendAttributeValue(var4);
               return;
            }

            var1.tagPending.appendAttributeValue('&');
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
         }
      }
   },
   AttributeValue_singleQuoted {
      void read(Tokeniser var1, CharacterReader var2) {
         String var3 = var2.consumeToAny(TokeniserState.attributeSingleValueCharsSorted);
         if (var3.length() > 0) {
            var1.tagPending.appendAttributeValue(var3);
         } else {
            var1.tagPending.setEmptyAttributeValue();
         }

         switch(var2.consume()) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeValue('�');
            return;
         case '&':
            char[] var4 = var1.consumeCharacterReference('\'', true);
            if (var4 != null) {
               var1.tagPending.appendAttributeValue(var4);
               return;
            }

            var1.tagPending.appendAttributeValue('&');
            return;
         case '\'':
            var1.transition(AfterAttributeValue_quoted);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
         }
      }
   },
   AttributeValue_unquoted {
      void read(Tokeniser var1, CharacterReader var2) {
         String var4 = var2.consumeToAnySorted(TokeniserState.attributeValueUnquoted);
         if (var4.length() > 0) {
            var1.tagPending.appendAttributeValue(var4);
         }

         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeValue('�');
            return;
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            var1.transition(BeforeAttributeName);
            return;
         case '"':
         case '\'':
         case '<':
         case '=':
         case '`':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeValue(var3);
            return;
         case '&':
            char[] var5 = var1.consumeCharacterReference('>', true);
            if (var5 != null) {
               var1.tagPending.appendAttributeValue(var5);
               return;
            }

            var1.tagPending.appendAttributeValue('&');
            return;
         case '>':
            var1.emitTagPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
         }
      }
   },
   BeforeAttributeName {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.tagPending.newAttribute();
            var2.unconsume();
            var1.transition(AttributeName);
            return;
         case '"':
         case '\'':
         case '<':
         case '=':
            var1.error((TokeniserState)this);
            var1.tagPending.newAttribute();
            var1.tagPending.appendAttributeName(var3);
            var1.transition(AttributeName);
            return;
         case '/':
            var1.transition(SelfClosingStartTag);
            return;
         case '>':
            var1.emitTagPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
            var1.tagPending.newAttribute();
            var2.unconsume();
            var1.transition(AttributeName);
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
         }
      }
   },
   BeforeAttributeValue {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeValue('�');
            var1.transition(AttributeValue_unquoted);
            return;
         case '"':
            var1.transition(AttributeValue_doubleQuoted);
            return;
         case '&':
            var2.unconsume();
            var1.transition(AttributeValue_unquoted);
            return;
         case '\'':
            var1.transition(AttributeValue_singleQuoted);
            return;
         case '<':
         case '=':
         case '`':
            var1.error((TokeniserState)this);
            var1.tagPending.appendAttributeValue(var3);
            var1.transition(AttributeValue_unquoted);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.emitTagPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.emitTagPending();
            var1.transition(Data);
            return;
         default:
            var2.unconsume();
            var1.transition(AttributeValue_unquoted);
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
         }
      }
   },
   BeforeDoctypeName {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchesLetter()) {
            var1.createDoctypePending();
            var1.transition(DoctypeName);
         } else {
            char var3 = var2.consume();
            switch(var3) {
            case '\u0000':
               var1.error((TokeniserState)this);
               var1.createDoctypePending();
               var1.doctypePending.name.append('�');
               var1.transition(DoctypeName);
               return;
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
               break;
            case '\uffff':
               var1.eofError(this);
               var1.createDoctypePending();
               var1.doctypePending.forceQuirks = true;
               var1.emitDoctypePending();
               var1.transition(Data);
               return;
            default:
               var1.createDoctypePending();
               var1.doctypePending.name.append(var3);
               var1.transition(DoctypeName);
               return;
            }
         }

      }
   },
   BeforeDoctypePublicIdentifier {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '"':
            var1.transition(DoctypePublicIdentifier_doubleQuoted);
            return;
         case '\'':
            var1.transition(DoctypePublicIdentifier_singleQuoted);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.transition(BogusDoctype);
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
         }
      }
   },
   BeforeDoctypeSystemIdentifier {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '"':
            var1.transition(DoctypeSystemIdentifier_doubleQuoted);
            return;
         case '\'':
            var1.transition(DoctypeSystemIdentifier_singleQuoted);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.transition(BogusDoctype);
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
         }
      }
   },
   BetweenDoctypePublicAndSystemIdentifiers {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '"':
            var1.error((TokeniserState)this);
            var1.transition(DoctypeSystemIdentifier_doubleQuoted);
            return;
         case '\'':
            var1.error((TokeniserState)this);
            var1.transition(DoctypeSystemIdentifier_singleQuoted);
            return;
         case '>':
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.transition(BogusDoctype);
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
         }
      }
   },
   BogusComment {
      void read(Tokeniser var1, CharacterReader var2) {
         var2.unconsume();
         Token.Comment var3 = new Token.Comment();
         var3.bogus = true;
         var3.data.append(var2.consumeTo('>'));
         var1.emit((Token)var3);
         var1.advanceTransition(Data);
      }
   },
   BogusDoctype {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '>':
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
         }
      }
   },
   CdataSection {
      void read(Tokeniser var1, CharacterReader var2) {
         var1.emit(var2.consumeTo("]]>"));
         var2.matchConsume("]]>");
         var1.transition(Data);
      }
   },
   CharacterReferenceInData {
      void read(Tokeniser var1, CharacterReader var2) {
         char[] var3 = var1.consumeCharacterReference((Character)null, false);
         if (var3 == null) {
            var1.emit('&');
         } else {
            var1.emit(var3);
         }

         var1.transition(Data);
      }
   },
   CharacterReferenceInRcdata {
      void read(Tokeniser var1, CharacterReader var2) {
         char[] var3 = var1.consumeCharacterReference((Character)null, false);
         if (var3 == null) {
            var1.emit('&');
         } else {
            var1.emit(var3);
         }

         var1.transition(Rcdata);
      }
   },
   Comment {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.current()) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var2.advance();
            var1.commentPending.data.append('�');
            return;
         case '-':
            var1.advanceTransition(CommentEndDash);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         default:
            var1.commentPending.data.append(var2.consumeToAny('-', '\u0000'));
         }
      }
   },
   CommentEnd {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.commentPending.data.append("--").append('�');
            var1.transition(Comment);
            return;
         case '!':
            var1.error((TokeniserState)this);
            var1.transition(CommentEndBang);
            return;
         case '-':
            var1.error((TokeniserState)this);
            var1.commentPending.data.append('-');
            return;
         case '>':
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.commentPending.data.append("--").append(var3);
            var1.transition(Comment);
         }
      }
   },
   CommentEndBang {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.commentPending.data.append("--!").append('�');
            var1.transition(Comment);
            return;
         case '-':
            var1.commentPending.data.append("--!");
            var1.transition(CommentEndDash);
            return;
         case '>':
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         default:
            var1.commentPending.data.append("--!").append(var3);
            var1.transition(Comment);
         }
      }
   },
   CommentEndDash {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.commentPending.data.append('-').append('�');
            var1.transition(Comment);
            return;
         case '-':
            var1.transition(CommentEnd);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         default:
            var1.commentPending.data.append('-').append(var3);
            var1.transition(Comment);
         }
      }
   },
   CommentStart {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.commentPending.data.append('�');
            var1.transition(Comment);
            return;
         case '-':
            var1.transition(CommentStartDash);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         default:
            var1.commentPending.data.append(var3);
            var1.transition(Comment);
         }
      }
   },
   CommentStartDash {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.commentPending.data.append('�');
            var1.transition(Comment);
            return;
         case '-':
            var1.transition(CommentStartDash);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.emitCommentPending();
            var1.transition(Data);
            return;
         default:
            var1.commentPending.data.append(var3);
            var1.transition(Comment);
         }
      }
   },
   Data {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.current()) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.emit(var2.consume());
            return;
         case '&':
            var1.advanceTransition(CharacterReferenceInData);
            return;
         case '<':
            var1.advanceTransition(TagOpen);
            return;
         case '\uffff':
            var1.emit((Token)(new Token.EOF()));
            return;
         default:
            var1.emit(var2.consumeData());
         }
      }
   },
   Doctype {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            var1.transition(BeforeDoctypeName);
            return;
         case '\uffff':
            var1.eofError(this);
         case '>':
            var1.error((TokeniserState)this);
            var1.createDoctypePending();
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.transition(BeforeDoctypeName);
         }
      }
   },
   DoctypeName {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchesLetter()) {
            String var4 = var2.consumeLetterSequence();
            var1.doctypePending.name.append(var4.toLowerCase());
         } else {
            char var3 = var2.consume();
            switch(var3) {
            case '\u0000':
               var1.error((TokeniserState)this);
               var1.doctypePending.name.append('�');
               return;
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
               var1.transition(AfterDoctypeName);
               return;
            case '>':
               var1.emitDoctypePending();
               var1.transition(Data);
               return;
            case '\uffff':
               var1.eofError(this);
               var1.doctypePending.forceQuirks = true;
               var1.emitDoctypePending();
               var1.transition(Data);
               return;
            default:
               var1.doctypePending.name.append(var3);
            }
         }
      }
   },
   DoctypePublicIdentifier_doubleQuoted {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.doctypePending.publicIdentifier.append('�');
            return;
         case '"':
            var1.transition(AfterDoctypePublicIdentifier);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.doctypePending.publicIdentifier.append(var3);
         }
      }
   },
   DoctypePublicIdentifier_singleQuoted {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.doctypePending.publicIdentifier.append('�');
            return;
         case '\'':
            var1.transition(AfterDoctypePublicIdentifier);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.doctypePending.publicIdentifier.append(var3);
         }
      }
   },
   DoctypeSystemIdentifier_doubleQuoted {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.doctypePending.systemIdentifier.append('�');
            return;
         case '"':
            var1.transition(AfterDoctypeSystemIdentifier);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.doctypePending.systemIdentifier.append(var3);
         }
      }
   },
   DoctypeSystemIdentifier_singleQuoted {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.doctypePending.systemIdentifier.append('�');
            return;
         case '\'':
            var1.transition(AfterDoctypeSystemIdentifier);
            return;
         case '>':
            var1.error((TokeniserState)this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.doctypePending.forceQuirks = true;
            var1.emitDoctypePending();
            var1.transition(Data);
            return;
         default:
            var1.doctypePending.systemIdentifier.append(var3);
         }
      }
   },
   EndTagOpen {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.isEmpty()) {
            var1.eofError(this);
            var1.emit("</");
            var1.transition(Data);
         } else if (var2.matchesLetter()) {
            var1.createTagPending(false);
            var1.transition(TagName);
         } else if (var2.matches('>')) {
            var1.error((TokeniserState)this);
            var1.advanceTransition(Data);
         } else {
            var1.error((TokeniserState)this);
            var1.advanceTransition(BogusComment);
         }
      }
   },
   MarkupDeclarationOpen {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchConsume("--")) {
            var1.createCommentPending();
            var1.transition(CommentStart);
         } else if (var2.matchConsumeIgnoreCase("DOCTYPE")) {
            var1.transition(Doctype);
         } else if (var2.matchConsume("[CDATA[")) {
            var1.transition(CdataSection);
         } else {
            var1.error((TokeniserState)this);
            var1.advanceTransition(BogusComment);
         }
      }
   },
   PLAINTEXT {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.current()) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var2.advance();
            var1.emit('�');
            return;
         case '\uffff':
            var1.emit((Token)(new Token.EOF()));
            return;
         default:
            var1.emit(var2.consumeTo('\u0000'));
         }
      }
   },
   RCDATAEndTagName {
      private void anythingElse(Tokeniser var1, CharacterReader var2) {
         var1.emit("</" + var1.dataBuffer.toString());
         var2.unconsume();
         var1.transition(Rcdata);
      }

      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchesLetter()) {
            String var3 = var2.consumeLetterSequence();
            var1.tagPending.appendTagName(var3.toLowerCase());
            var1.dataBuffer.append(var3);
         } else {
            switch(var2.consume()) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
               if (var1.isAppropriateEndTagToken()) {
                  var1.transition(BeforeAttributeName);
                  return;
               }

               this.anythingElse(var1, var2);
               return;
            case '/':
               if (var1.isAppropriateEndTagToken()) {
                  var1.transition(SelfClosingStartTag);
                  return;
               }

               this.anythingElse(var1, var2);
               return;
            case '>':
               if (var1.isAppropriateEndTagToken()) {
                  var1.emitTagPending();
                  var1.transition(Data);
                  return;
               }

               this.anythingElse(var1, var2);
               return;
            default:
               this.anythingElse(var1, var2);
            }
         }
      }
   },
   RCDATAEndTagOpen {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchesLetter()) {
            var1.createTagPending(false);
            var1.tagPending.appendTagName(Character.toLowerCase(var2.current()));
            var1.dataBuffer.append(Character.toLowerCase(var2.current()));
            var1.advanceTransition(RCDATAEndTagName);
         } else {
            var1.emit("</");
            var1.transition(Rcdata);
         }
      }
   },
   Rawtext {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.current()) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var2.advance();
            var1.emit('�');
            return;
         case '<':
            var1.advanceTransition(RawtextLessthanSign);
            return;
         case '\uffff':
            var1.emit((Token)(new Token.EOF()));
            return;
         default:
            var1.emit(var2.consumeToAny('<', '\u0000'));
         }
      }
   },
   RawtextEndTagName {
      void read(Tokeniser var1, CharacterReader var2) {
         TokeniserState.handleDataEndTag(var1, var2, Rawtext);
      }
   },
   RawtextEndTagOpen {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchesLetter()) {
            var1.createTagPending(false);
            var1.transition(RawtextEndTagName);
         } else {
            var1.emit("</");
            var1.transition(Rawtext);
         }
      }
   },
   RawtextLessthanSign {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matches('/')) {
            var1.createTempBuffer();
            var1.advanceTransition(RawtextEndTagOpen);
         } else {
            var1.emit('<');
            var1.transition(Rawtext);
         }
      }
   },
   Rcdata {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.current()) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var2.advance();
            var1.emit('�');
            return;
         case '&':
            var1.advanceTransition(CharacterReferenceInRcdata);
            return;
         case '<':
            var1.advanceTransition(RcdataLessthanSign);
            return;
         case '\uffff':
            var1.emit((Token)(new Token.EOF()));
            return;
         default:
            var1.emit(var2.consumeToAny('&', '<', '\u0000'));
         }
      }
   },
   RcdataLessthanSign {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matches('/')) {
            var1.createTempBuffer();
            var1.advanceTransition(RCDATAEndTagOpen);
         } else if (var2.matchesLetter() && var1.appropriateEndTagName() != null && !var2.containsIgnoreCase("</" + var1.appropriateEndTagName())) {
            var1.tagPending = var1.createTagPending(false).name(var1.appropriateEndTagName());
            var1.emitTagPending();
            var2.unconsume();
            var1.transition(Data);
         } else {
            var1.emit("<");
            var1.transition(Rcdata);
         }
      }
   },
   ScriptData {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.current()) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var2.advance();
            var1.emit('�');
            return;
         case '<':
            var1.advanceTransition(ScriptDataLessthanSign);
            return;
         case '\uffff':
            var1.emit((Token)(new Token.EOF()));
            return;
         default:
            var1.emit(var2.consumeToAny('<', '\u0000'));
         }
      }
   },
   ScriptDataDoubleEscapeEnd {
      void read(Tokeniser var1, CharacterReader var2) {
         TokeniserState.handleDataDoubleEscapeTag(var1, var2, ScriptDataEscaped, ScriptDataDoubleEscaped);
      }
   },
   ScriptDataDoubleEscapeStart {
      void read(Tokeniser var1, CharacterReader var2) {
         TokeniserState.handleDataDoubleEscapeTag(var1, var2, ScriptDataDoubleEscaped, ScriptDataEscaped);
      }
   },
   ScriptDataDoubleEscaped {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.current();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var2.advance();
            var1.emit('�');
            return;
         case '-':
            var1.emit(var3);
            var1.advanceTransition(ScriptDataDoubleEscapedDash);
            return;
         case '<':
            var1.emit(var3);
            var1.advanceTransition(ScriptDataDoubleEscapedLessthanSign);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
            var1.emit(var2.consumeToAny('-', '<', '\u0000'));
         }
      }
   },
   ScriptDataDoubleEscapedDash {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.emit('�');
            var1.transition(ScriptDataDoubleEscaped);
            return;
         case '-':
            var1.emit(var3);
            var1.transition(ScriptDataDoubleEscapedDashDash);
            return;
         case '<':
            var1.emit(var3);
            var1.transition(ScriptDataDoubleEscapedLessthanSign);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
            var1.emit(var3);
            var1.transition(ScriptDataDoubleEscaped);
         }
      }
   },
   ScriptDataDoubleEscapedDashDash {
      void read(Tokeniser var1, CharacterReader var2) {
         char var3 = var2.consume();
         switch(var3) {
         case '\u0000':
            var1.error((TokeniserState)this);
            var1.emit('�');
            var1.transition(ScriptDataDoubleEscaped);
            return;
         case '-':
            var1.emit(var3);
            return;
         case '<':
            var1.emit(var3);
            var1.transition(ScriptDataDoubleEscapedLessthanSign);
            return;
         case '>':
            var1.emit(var3);
            var1.transition(ScriptData);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
            var1.emit(var3);
            var1.transition(ScriptDataDoubleEscaped);
         }
      }
   },
   ScriptDataDoubleEscapedLessthanSign {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matches('/')) {
            var1.emit('/');
            var1.createTempBuffer();
            var1.advanceTransition(ScriptDataDoubleEscapeEnd);
         } else {
            var1.transition(ScriptDataDoubleEscaped);
         }
      }
   },
   ScriptDataEndTagName {
      void read(Tokeniser var1, CharacterReader var2) {
         TokeniserState.handleDataEndTag(var1, var2, ScriptData);
      }
   },
   ScriptDataEndTagOpen {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchesLetter()) {
            var1.createTagPending(false);
            var1.transition(ScriptDataEndTagName);
         } else {
            var1.emit("</");
            var1.transition(ScriptData);
         }
      }
   },
   ScriptDataEscapeStart {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matches('-')) {
            var1.emit('-');
            var1.advanceTransition(ScriptDataEscapeStartDash);
         } else {
            var1.transition(ScriptData);
         }
      }
   },
   ScriptDataEscapeStartDash {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matches('-')) {
            var1.emit('-');
            var1.advanceTransition(ScriptDataEscapedDashDash);
         } else {
            var1.transition(ScriptData);
         }
      }
   },
   ScriptDataEscaped {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.isEmpty()) {
            var1.eofError(this);
            var1.transition(Data);
         } else {
            switch(var2.current()) {
            case '\u0000':
               var1.error((TokeniserState)this);
               var2.advance();
               var1.emit('�');
               return;
            case '-':
               var1.emit('-');
               var1.advanceTransition(ScriptDataEscapedDash);
               return;
            case '<':
               var1.advanceTransition(ScriptDataEscapedLessthanSign);
               return;
            default:
               var1.emit(var2.consumeToAny('-', '<', '\u0000'));
            }
         }
      }
   },
   ScriptDataEscapedDash {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.isEmpty()) {
            var1.eofError(this);
            var1.transition(Data);
         } else {
            char var3 = var2.consume();
            switch(var3) {
            case '\u0000':
               var1.error((TokeniserState)this);
               var1.emit('�');
               var1.transition(ScriptDataEscaped);
               return;
            case '-':
               var1.emit(var3);
               var1.transition(ScriptDataEscapedDashDash);
               return;
            case '<':
               var1.transition(ScriptDataEscapedLessthanSign);
               return;
            default:
               var1.emit(var3);
               var1.transition(ScriptDataEscaped);
            }
         }
      }
   },
   ScriptDataEscapedDashDash {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.isEmpty()) {
            var1.eofError(this);
            var1.transition(Data);
         } else {
            char var3 = var2.consume();
            switch(var3) {
            case '\u0000':
               var1.error((TokeniserState)this);
               var1.emit('�');
               var1.transition(ScriptDataEscaped);
               return;
            case '-':
               var1.emit(var3);
               return;
            case '<':
               var1.transition(ScriptDataEscapedLessthanSign);
               return;
            case '>':
               var1.emit(var3);
               var1.transition(ScriptData);
               return;
            default:
               var1.emit(var3);
               var1.transition(ScriptDataEscaped);
            }
         }
      }
   },
   ScriptDataEscapedEndTagName {
      void read(Tokeniser var1, CharacterReader var2) {
         TokeniserState.handleDataEndTag(var1, var2, ScriptDataEscaped);
      }
   },
   ScriptDataEscapedEndTagOpen {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchesLetter()) {
            var1.createTagPending(false);
            var1.tagPending.appendTagName(Character.toLowerCase(var2.current()));
            var1.dataBuffer.append(var2.current());
            var1.advanceTransition(ScriptDataEscapedEndTagName);
         } else {
            var1.emit("</");
            var1.transition(ScriptDataEscaped);
         }
      }
   },
   ScriptDataEscapedLessthanSign {
      void read(Tokeniser var1, CharacterReader var2) {
         if (var2.matchesLetter()) {
            var1.createTempBuffer();
            var1.dataBuffer.append(Character.toLowerCase(var2.current()));
            var1.emit("<" + var2.current());
            var1.advanceTransition(ScriptDataDoubleEscapeStart);
         } else if (var2.matches('/')) {
            var1.createTempBuffer();
            var1.advanceTransition(ScriptDataEscapedEndTagOpen);
         } else {
            var1.emit('<');
            var1.transition(ScriptDataEscaped);
         }
      }
   },
   ScriptDataLessthanSign {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '!':
            var1.emit("<!");
            var1.transition(ScriptDataEscapeStart);
            return;
         case '/':
            var1.createTempBuffer();
            var1.transition(ScriptDataEndTagOpen);
            return;
         default:
            var1.emit("<");
            var2.unconsume();
            var1.transition(ScriptData);
         }
      }
   },
   SelfClosingStartTag {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.consume()) {
         case '>':
            var1.tagPending.selfClosing = true;
            var1.emitTagPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
            var1.error((TokeniserState)this);
            var1.transition(BeforeAttributeName);
         }
      }
   },
   TagName {
      void read(Tokeniser var1, CharacterReader var2) {
         String var3 = var2.consumeTagName().toLowerCase();
         var1.tagPending.appendTagName(var3);
         switch(var2.consume()) {
         case '\u0000':
            var1.tagPending.appendTagName(TokeniserState.replacementStr);
            return;
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
            var1.transition(BeforeAttributeName);
            return;
         case '/':
            var1.transition(SelfClosingStartTag);
            return;
         case '>':
            var1.emitTagPending();
            var1.transition(Data);
            return;
         case '\uffff':
            var1.eofError(this);
            var1.transition(Data);
            return;
         default:
         }
      }
   },
   TagOpen {
      void read(Tokeniser var1, CharacterReader var2) {
         switch(var2.current()) {
         case '!':
            var1.advanceTransition(MarkupDeclarationOpen);
            return;
         case '/':
            var1.advanceTransition(EndTagOpen);
            return;
         case '?':
            var1.advanceTransition(BogusComment);
            return;
         default:
            if (var2.matchesLetter()) {
               var1.createTagPending(true);
               var1.transition(TagName);
            } else {
               var1.error((TokeniserState)this);
               var1.emit('<');
               var1.transition(Data);
            }
         }
      }
   };

   private static final char[] attributeDoubleValueCharsSorted = new char[]{'"', '&', '\u0000'};
   private static final char[] attributeNameCharsSorted = new char[]{'\t', '\n', '\r', '\f', ' ', '/', '=', '>', '\u0000', '"', '\'', '<'};
   private static final char[] attributeSingleValueCharsSorted = new char[]{'\'', '&', '\u0000'};
   private static final char[] attributeValueUnquoted = new char[]{'\t', '\n', '\r', '\f', ' ', '&', '>', '\u0000', '"', '\'', '<', '=', '`'};
   private static final char eof = '\uffff';
   static final char nullChar = '\u0000';
   private static final char replacementChar = '�';
   private static final String replacementStr = String.valueOf('�');

   static {
      Arrays.sort(attributeSingleValueCharsSorted);
      Arrays.sort(attributeDoubleValueCharsSorted);
      Arrays.sort(attributeNameCharsSorted);
      Arrays.sort(attributeValueUnquoted);
   }

   private TokeniserState() {
   }

   // $FF: synthetic method
   TokeniserState(Object var3) {
      this();
   }

   private static void handleDataDoubleEscapeTag(Tokeniser var0, CharacterReader var1, TokeniserState var2, TokeniserState var3) {
      if (var1.matchesLetter()) {
         String var5 = var1.consumeLetterSequence();
         var0.dataBuffer.append(var5.toLowerCase());
         var0.emit(var5);
      } else {
         char var4 = var1.consume();
         switch(var4) {
         case '\t':
         case '\n':
         case '\f':
         case '\r':
         case ' ':
         case '/':
         case '>':
            if (var0.dataBuffer.toString().equals("script")) {
               var0.transition(var2);
            } else {
               var0.transition(var3);
            }

            var0.emit(var4);
            return;
         default:
            var1.unconsume();
            var0.transition(var3);
         }
      }
   }

   private static void handleDataEndTag(Tokeniser var0, CharacterReader var1, TokeniserState var2) {
      if (var1.matchesLetter()) {
         String var5 = var1.consumeLetterSequence();
         var0.tagPending.appendTagName(var5.toLowerCase());
         var0.dataBuffer.append(var5);
      } else {
         boolean var4 = false;
         if (var0.isAppropriateEndTagToken() && !var1.isEmpty()) {
            char var3 = var1.consume();
            switch(var3) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
               var0.transition(BeforeAttributeName);
               break;
            case '/':
               var0.transition(SelfClosingStartTag);
               break;
            case '>':
               var0.emitTagPending();
               var0.transition(Data);
               break;
            default:
               var0.dataBuffer.append(var3);
               var4 = true;
            }
         } else {
            var4 = true;
         }

         if (var4) {
            var0.emit("</" + var0.dataBuffer.toString());
            var0.transition(var2);
            return;
         }
      }

   }

   abstract void read(Tokeniser var1, CharacterReader var2);
}
