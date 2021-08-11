package org.jsoup.parser;

import java.util.ArrayList;
import java.util.Iterator;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;

enum HtmlTreeBuilderState {
   AfterAfterBody {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (var1.isComment()) {
            var2.insert(var1.asComment());
         } else {
            if (var1.isDoctype() || HtmlTreeBuilderState.isWhitespace(var1) || var1.isStartTag() && var1.asStartTag().name().equals("html")) {
               return var2.process(var1, InBody);
            }

            if (!var1.isEOF()) {
               var2.error(this);
               var2.transition(InBody);
               return var2.process(var1);
            }
         }

         return true;
      }
   },
   AfterAfterFrameset {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (var1.isComment()) {
            var2.insert(var1.asComment());
         } else {
            if (var1.isDoctype() || HtmlTreeBuilderState.isWhitespace(var1) || var1.isStartTag() && var1.asStartTag().name().equals("html")) {
               return var2.process(var1, InBody);
            }

            if (!var1.isEOF()) {
               if (var1.isStartTag() && var1.asStartTag().name().equals("noframes")) {
                  return var2.process(var1, InHead);
               }

               var2.error(this);
               return false;
            }
         }

         return true;
      }
   },
   AfterBody {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (HtmlTreeBuilderState.isWhitespace(var1)) {
            return var2.process(var1, InBody);
         } else {
            if (var1.isComment()) {
               var2.insert(var1.asComment());
            } else {
               if (var1.isDoctype()) {
                  var2.error(this);
                  return false;
               }

               if (var1.isStartTag() && var1.asStartTag().name().equals("html")) {
                  return var2.process(var1, InBody);
               }

               if (var1.isEndTag() && var1.asEndTag().name().equals("html")) {
                  if (var2.isFragmentParsing()) {
                     var2.error(this);
                     return false;
                  }

                  var2.transition(AfterAfterBody);
               } else if (!var1.isEOF()) {
                  var2.error(this);
                  var2.transition(InBody);
                  return var2.process(var1);
               }
            }

            return true;
         }
      }
   },
   AfterFrameset {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (HtmlTreeBuilderState.isWhitespace(var1)) {
            var2.insert(var1.asCharacter());
         } else if (var1.isComment()) {
            var2.insert(var1.asComment());
         } else {
            if (var1.isDoctype()) {
               var2.error(this);
               return false;
            }

            if (var1.isStartTag() && var1.asStartTag().name().equals("html")) {
               return var2.process(var1, InBody);
            }

            if (var1.isEndTag() && var1.asEndTag().name().equals("html")) {
               var2.transition(AfterAfterFrameset);
            } else {
               if (var1.isStartTag() && var1.asStartTag().name().equals("noframes")) {
                  return var2.process(var1, InHead);
               }

               if (!var1.isEOF()) {
                  var2.error(this);
                  return false;
               }
            }
         }

         return true;
      }
   },
   AfterHead {
      private boolean anythingElse(Token var1, HtmlTreeBuilder var2) {
         var2.processStartTag("body");
         var2.framesetOk(true);
         return var2.process(var1);
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (HtmlTreeBuilderState.isWhitespace(var1)) {
            var2.insert(var1.asCharacter());
         } else if (var1.isComment()) {
            var2.insert(var1.asComment());
         } else if (var1.isDoctype()) {
            var2.error(this);
         } else if (var1.isStartTag()) {
            Token.StartTag var3 = var1.asStartTag();
            String var4 = var3.name();
            if (var4.equals("html")) {
               return var2.process(var1, InBody);
            }

            if (var4.equals("body")) {
               var2.insert(var3);
               var2.framesetOk(false);
               var2.transition(InBody);
            } else if (var4.equals("frameset")) {
               var2.insert(var3);
               var2.transition(InFrameset);
            } else if (StringUtil.method_16(var4, "base", "basefont", "bgsound", "link", "meta", "noframes", "script", "style", "title")) {
               var2.error(this);
               Element var5 = var2.getHeadElement();
               var2.push(var5);
               var2.process(var1, InHead);
               var2.removeFromStack(var5);
            } else {
               if (var4.equals("head")) {
                  var2.error(this);
                  return false;
               }

               this.anythingElse(var1, var2);
            }
         } else if (var1.isEndTag()) {
            if (!StringUtil.method_16(var1.asEndTag().name(), "body", "html")) {
               var2.error(this);
               return false;
            }

            this.anythingElse(var1, var2);
         } else {
            this.anythingElse(var1, var2);
         }

         return true;
      }
   },
   BeforeHead {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (HtmlTreeBuilderState.isWhitespace(var1)) {
            return true;
         } else if (var1.isComment()) {
            var2.insert(var1.asComment());
            return true;
         } else if (var1.isDoctype()) {
            var2.error(this);
            return false;
         } else if (var1.isStartTag() && var1.asStartTag().name().equals("html")) {
            return InBody.process(var1, var2);
         } else if (var1.isStartTag() && var1.asStartTag().name().equals("head")) {
            var2.setHeadElement(var2.insert(var1.asStartTag()));
            var2.transition(InHead);
            return true;
         } else if (var1.isEndTag() && StringUtil.method_16(var1.asEndTag().name(), "head", "body", "html", "br")) {
            var2.processStartTag("head");
            return var2.process(var1);
         } else if (var1.isEndTag()) {
            var2.error(this);
            return false;
         } else {
            var2.processStartTag("head");
            return var2.process(var1);
         }
      }
   },
   BeforeHtml {
      private boolean anythingElse(Token var1, HtmlTreeBuilder var2) {
         var2.insertStartTag("html");
         var2.transition(BeforeHead);
         return var2.process(var1);
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (var1.isDoctype()) {
            var2.error(this);
            return false;
         } else {
            if (var1.isComment()) {
               var2.insert(var1.asComment());
            } else {
               if (HtmlTreeBuilderState.isWhitespace(var1)) {
                  return true;
               }

               if (!var1.isStartTag() || !var1.asStartTag().name().equals("html")) {
                  if (var1.isEndTag() && StringUtil.method_16(var1.asEndTag().name(), "head", "body", "html", "br")) {
                     return this.anythingElse(var1, var2);
                  }

                  if (var1.isEndTag()) {
                     var2.error(this);
                     return false;
                  }

                  return this.anythingElse(var1, var2);
               }

               var2.insert(var1.asStartTag());
               var2.transition(BeforeHead);
            }

            return true;
         }
      }
   },
   ForeignContent {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         return true;
      }
   },
   InBody {
      boolean anyOtherEndTag(Token var1, HtmlTreeBuilder var2) {
         String var6 = var1.asEndTag().name();
         ArrayList var4 = var2.getStack();

         for(int var3 = var4.size() - 1; var3 >= 0; --var3) {
            Element var5 = (Element)var4.get(var3);
            if (var5.nodeName().equals(var6)) {
               var2.generateImpliedEndTags(var6);
               if (!var6.equals(var2.currentElement().nodeName())) {
                  var2.error(this);
               }

               var2.popStackToClose(var6);
               break;
            }

            if (var2.isSpecial(var5)) {
               var2.error(this);
               return false;
            }
         }

         return true;
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         int var3;
         Element var9;
         switch(var1.type) {
         case Comment:
            var2.insert(var1.asComment());
            break;
         case Doctype:
            var2.error(this);
            return false;
         case StartTag:
            Token.StartTag var26 = var1.asStartTag();
            String var25 = var26.name();
            Element var22;
            if (var25.equals("a")) {
               if (var2.getActiveFormattingElement("a") != null) {
                  var2.error(this);
                  var2.processEndTag("a");
                  var22 = var2.getFromStack("a");
                  if (var22 != null) {
                     var2.removeFromActiveFormattingElements(var22);
                     var2.removeFromStack(var22);
                  }
               }

               var2.reconstructFormattingElements();
               var2.pushActiveFormattingElements(var2.insert(var26));
            } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartEmptyFormatters)) {
               var2.reconstructFormattingElements();
               var2.insertEmpty(var26);
               var2.framesetOk(false);
            } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartPClosers)) {
               if (var2.inButtonScope("p")) {
                  var2.processEndTag("p");
               }

               var2.insert(var26);
            } else if (var25.equals("span")) {
               var2.reconstructFormattingElements();
               var2.insert(var26);
            } else {
               ArrayList var17;
               if (var25.equals("li")) {
                  var2.framesetOk(false);
                  var17 = var2.getStack();

                  for(var3 = var17.size() - 1; var3 > 0; --var3) {
                     var9 = (Element)var17.get(var3);
                     if (var9.nodeName().equals("li")) {
                        var2.processEndTag("li");
                        break;
                     }

                     if (var2.isSpecial(var9) && !StringUtil.inSorted(var9.nodeName(), HtmlTreeBuilderState.Constants.InBodyStartLiBreakers)) {
                        break;
                     }
                  }

                  if (var2.inButtonScope("p")) {
                     var2.processEndTag("p");
                  }

                  var2.insert(var26);
               } else {
                  Iterator var18;
                  Attribute var29;
                  if (var25.equals("html")) {
                     var2.error(this);
                     var22 = (Element)var2.getStack().get(0);
                     var18 = var26.getAttributes().iterator();

                     while(var18.hasNext()) {
                        var29 = (Attribute)var18.next();
                        if (!var22.hasAttr(var29.getKey())) {
                           var22.attributes().put(var29);
                        }
                     }

                     return true;
                  } else {
                     if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartToHead)) {
                        return var2.process(var1, InHead);
                     }

                     if (var25.equals("body")) {
                        var2.error(this);
                        var17 = var2.getStack();
                        if (var17.size() == 1 || var17.size() > 2 && !((Element)var17.get(1)).nodeName().equals("body")) {
                           return false;
                        }

                        var2.framesetOk(false);
                        var22 = (Element)var17.get(1);
                        var18 = var26.getAttributes().iterator();

                        while(var18.hasNext()) {
                           var29 = (Attribute)var18.next();
                           if (!var22.hasAttr(var29.getKey())) {
                              var22.attributes().put(var29);
                           }
                        }

                        return true;
                     } else if (!var25.equals("frameset")) {
                        if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.Headings)) {
                           if (var2.inButtonScope("p")) {
                              var2.processEndTag("p");
                           }

                           if (StringUtil.inSorted(var2.currentElement().nodeName(), HtmlTreeBuilderState.Constants.Headings)) {
                              var2.error(this);
                              var2.pop();
                           }

                           var2.insert(var26);
                        } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartPreListing)) {
                           if (var2.inButtonScope("p")) {
                              var2.processEndTag("p");
                           }

                           var2.insert(var26);
                           var2.framesetOk(false);
                        } else if (var25.equals("form")) {
                           if (var2.getFormElement() != null) {
                              var2.error(this);
                              return false;
                           }

                           if (var2.inButtonScope("p")) {
                              var2.processEndTag("p");
                           }

                           var2.insertForm(var26, true);
                        } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.DdDt)) {
                           var2.framesetOk(false);
                           var17 = var2.getStack();

                           for(var3 = var17.size() - 1; var3 > 0; --var3) {
                              var9 = (Element)var17.get(var3);
                              if (StringUtil.inSorted(var9.nodeName(), HtmlTreeBuilderState.Constants.DdDt)) {
                                 var2.processEndTag(var9.nodeName());
                                 break;
                              }

                              if (var2.isSpecial(var9) && !StringUtil.inSorted(var9.nodeName(), HtmlTreeBuilderState.Constants.InBodyStartLiBreakers)) {
                                 break;
                              }
                           }

                           if (var2.inButtonScope("p")) {
                              var2.processEndTag("p");
                           }

                           var2.insert(var26);
                        } else if (var25.equals("plaintext")) {
                           if (var2.inButtonScope("p")) {
                              var2.processEndTag("p");
                           }

                           var2.insert(var26);
                           var2.tokeniser.transition(TokeniserState.PLAINTEXT);
                        } else if (var25.equals("button")) {
                           if (var2.inButtonScope("button")) {
                              var2.error(this);
                              var2.processEndTag("button");
                              var2.process(var26);
                           } else {
                              var2.reconstructFormattingElements();
                              var2.insert(var26);
                              var2.framesetOk(false);
                           }
                        } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.Formatters)) {
                           var2.reconstructFormattingElements();
                           var2.pushActiveFormattingElements(var2.insert(var26));
                        } else if (var25.equals("nobr")) {
                           var2.reconstructFormattingElements();
                           if (var2.inScope("nobr")) {
                              var2.error(this);
                              var2.processEndTag("nobr");
                              var2.reconstructFormattingElements();
                           }

                           var2.pushActiveFormattingElements(var2.insert(var26));
                        } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartApplets)) {
                           var2.reconstructFormattingElements();
                           var2.insert(var26);
                           var2.insertMarkerToFormattingElements();
                           var2.framesetOk(false);
                        } else if (var25.equals("table")) {
                           if (var2.getDocument().quirksMode() != Document.QuirksMode.quirks && var2.inButtonScope("p")) {
                              var2.processEndTag("p");
                           }

                           var2.insert(var26);
                           var2.framesetOk(false);
                           var2.transition(InTable);
                        } else if (var25.equals("input")) {
                           var2.reconstructFormattingElements();
                           if (!var2.insertEmpty(var26).attr("type").equalsIgnoreCase("hidden")) {
                              var2.framesetOk(false);
                           }
                        } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartMedia)) {
                           var2.insertEmpty(var26);
                        } else if (var25.equals("hr")) {
                           if (var2.inButtonScope("p")) {
                              var2.processEndTag("p");
                           }

                           var2.insertEmpty(var26);
                           var2.framesetOk(false);
                        } else if (var25.equals("image")) {
                           if (var2.getFromStack("svg") == null) {
                              return var2.process(var26.name("img"));
                           }

                           var2.insert(var26);
                        } else if (var25.equals("isindex")) {
                           var2.error(this);
                           if (var2.getFormElement() != null) {
                              return false;
                           }

                           var2.tokeniser.acknowledgeSelfClosingFlag();
                           var2.processStartTag("form");
                           if (var26.attributes.hasKey("action")) {
                              var2.getFormElement().attr("action", var26.attributes.get("action"));
                           }

                           var2.processStartTag("hr");
                           var2.processStartTag("label");
                           String var19;
                           if (var26.attributes.hasKey("prompt")) {
                              var19 = var26.attributes.get("prompt");
                           } else {
                              var19 = "This is a searchable index. Enter search keywords: ";
                           }

                           var2.process((new Token.Character()).data(var19));
                           Attributes var20 = new Attributes();
                           Iterator var28 = var26.attributes.iterator();

                           while(var28.hasNext()) {
                              Attribute var30 = (Attribute)var28.next();
                              if (!StringUtil.inSorted(var30.getKey(), HtmlTreeBuilderState.Constants.InBodyStartInputAttribs)) {
                                 var20.put(var30);
                              }
                           }

                           var20.put("name", "isindex");
                           var2.processStartTag("input", var20);
                           var2.processEndTag("label");
                           var2.processStartTag("hr");
                           var2.processEndTag("form");
                        } else if (var25.equals("textarea")) {
                           var2.insert(var26);
                           var2.tokeniser.transition(TokeniserState.Rcdata);
                           var2.markInsertionMode();
                           var2.framesetOk(false);
                           var2.transition(Text);
                        } else if (var25.equals("xmp")) {
                           if (var2.inButtonScope("p")) {
                              var2.processEndTag("p");
                           }

                           var2.reconstructFormattingElements();
                           var2.framesetOk(false);
                           HtmlTreeBuilderState.handleRawtext(var26, var2);
                        } else if (var25.equals("iframe")) {
                           var2.framesetOk(false);
                           HtmlTreeBuilderState.handleRawtext(var26, var2);
                        } else if (var25.equals("noembed")) {
                           HtmlTreeBuilderState.handleRawtext(var26, var2);
                        } else if (var25.equals("select")) {
                           var2.reconstructFormattingElements();
                           var2.insert(var26);
                           var2.framesetOk(false);
                           HtmlTreeBuilderState var21 = var2.state();
                           if (!var21.equals(InTable) && !var21.equals(InCaption) && !var21.equals(InTableBody) && !var21.equals(InRow) && !var21.equals(InCell)) {
                              var2.transition(InSelect);
                           } else {
                              var2.transition(InSelectInTable);
                           }
                        } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartOptions)) {
                           if (var2.currentElement().nodeName().equals("option")) {
                              var2.processEndTag("option");
                           }

                           var2.reconstructFormattingElements();
                           var2.insert(var26);
                        } else if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartRuby)) {
                           if (var2.inScope("ruby")) {
                              var2.generateImpliedEndTags();
                              if (!var2.currentElement().nodeName().equals("ruby")) {
                                 var2.error(this);
                                 var2.popStackToBefore("ruby");
                              }

                              var2.insert(var26);
                           }
                        } else if (var25.equals("math")) {
                           var2.reconstructFormattingElements();
                           var2.insert(var26);
                           var2.tokeniser.acknowledgeSelfClosingFlag();
                        } else if (var25.equals("svg")) {
                           var2.reconstructFormattingElements();
                           var2.insert(var26);
                           var2.tokeniser.acknowledgeSelfClosingFlag();
                        } else {
                           if (StringUtil.inSorted(var25, HtmlTreeBuilderState.Constants.InBodyStartDrop)) {
                              var2.error(this);
                              return false;
                           }

                           var2.reconstructFormattingElements();
                           var2.insert(var26);
                        }
                     } else {
                        var2.error(this);
                        var17 = var2.getStack();
                        if (var17.size() != 1 && (var17.size() <= 2 || ((Element)var17.get(1)).nodeName().equals("body"))) {
                           if (!var2.framesetOk()) {
                              return false;
                           }

                           var9 = (Element)var17.get(1);
                           if (var9.parent() != null) {
                              var9.remove();
                           }

                           while(var17.size() > 1) {
                              var17.remove(var17.size() - 1);
                           }

                           var2.insert(var26);
                           var2.transition(InFrameset);
                           break;
                        }

                        return false;
                     }
                  }
               }
            }
            break;
         case EndTag:
            Token.EndTag var8 = var1.asEndTag();
            String var13 = var8.name();
            if (StringUtil.inSorted(var13, HtmlTreeBuilderState.Constants.InBodyEndAdoptionFormatters)) {
               for(var3 = 0; var3 < 8; ++var3) {
                  Element var14 = var2.getActiveFormattingElement(var13);
                  if (var14 == null) {
                     return this.anyOtherEndTag(var1, var2);
                  }

                  if (!var2.onStack(var14)) {
                     var2.error(this);
                     var2.removeFromActiveFormattingElements(var14);
                     return true;
                  }

                  if (!var2.inScope(var14.nodeName())) {
                     var2.error(this);
                     return false;
                  }

                  if (var2.currentElement() != var14) {
                     var2.error(this);
                  }

                  var9 = null;
                  Element var11 = null;
                  boolean var5 = false;
                  ArrayList var12 = var2.getStack();
                  int var7 = var12.size();
                  int var4 = 0;

                  Element var10;
                  Element var24;
                  while(true) {
                     var24 = var9;
                     if (var4 >= var7) {
                        break;
                     }

                     var24 = var9;
                     if (var4 >= 64) {
                        break;
                     }

                     var10 = (Element)var12.get(var4);
                     boolean var6;
                     if (var10 == var14) {
                        var24 = (Element)var12.get(var4 - 1);
                        var6 = true;
                     } else {
                        var24 = var11;
                        var6 = var5;
                        if (var5) {
                           var24 = var11;
                           var6 = var5;
                           if (var2.isSpecial(var10)) {
                              var24 = var10;
                              break;
                           }
                        }
                     }

                     ++var4;
                     var11 = var24;
                     var5 = var6;
                  }

                  if (var24 == null) {
                     var2.popStackToClose(var14.nodeName());
                     var2.removeFromActiveFormattingElements(var14);
                     return true;
                  }

                  var9 = var24;
                  Element var31 = var24;

                  for(var4 = 0; var4 < 3; ++var4) {
                     var10 = var9;
                     if (var2.onStack(var9)) {
                        var10 = var2.aboveOnStack(var9);
                     }

                     if (!var2.isInActiveFormattingElements(var10)) {
                        var2.removeFromStack(var10);
                        var9 = var10;
                     } else {
                        if (var10 == var14) {
                           break;
                        }

                        var9 = new Element(Tag.valueOf(var10.nodeName()), var2.getBaseUri());
                        var2.replaceActiveFormattingElement(var10, var9);
                        var2.replaceOnStack(var10, var9);
                        if (var31 == var24) {
                        }

                        if (var31.parent() != null) {
                           var31.remove();
                        }

                        var9.appendChild(var31);
                        var31 = var9;
                     }
                  }

                  if (StringUtil.inSorted(var11.nodeName(), HtmlTreeBuilderState.Constants.InBodyEndTableFosters)) {
                     if (var31.parent() != null) {
                        var31.remove();
                     }

                     var2.insertInFosterParent(var31);
                  } else {
                     if (var31.parent() != null) {
                        var31.remove();
                     }

                     var11.appendChild(var31);
                  }

                  var9 = new Element(var14.tag(), var2.getBaseUri());
                  var9.attributes().addAll(var14.attributes());
                  Node[] var27 = (Node[])var24.childNodes().toArray(new Node[var24.childNodeSize()]);
                  int var23 = var27.length;

                  for(var4 = 0; var4 < var23; ++var4) {
                     var9.appendChild(var27[var4]);
                  }

                  var24.appendChild(var9);
                  var2.removeFromActiveFormattingElements(var14);
                  var2.removeFromStack(var14);
                  var2.insertOnStackAfter(var24, var9);
               }

               return true;
            } else {
               if (StringUtil.inSorted(var13, HtmlTreeBuilderState.Constants.InBodyEndClosers)) {
                  if (!var2.inScope(var13)) {
                     var2.error(this);
                     return false;
                  }

                  var2.generateImpliedEndTags();
                  if (!var2.currentElement().nodeName().equals(var13)) {
                     var2.error(this);
                  }

                  var2.popStackToClose(var13);
               } else {
                  if (var13.equals("span")) {
                     return this.anyOtherEndTag(var1, var2);
                  }

                  if (var13.equals("li")) {
                     if (!var2.inListItemScope(var13)) {
                        var2.error(this);
                        return false;
                     }

                     var2.generateImpliedEndTags(var13);
                     if (!var2.currentElement().nodeName().equals(var13)) {
                        var2.error(this);
                     }

                     var2.popStackToClose(var13);
                  } else if (var13.equals("body")) {
                     if (!var2.inScope("body")) {
                        var2.error(this);
                        return false;
                     }

                     var2.transition(AfterBody);
                  } else if (var13.equals("html")) {
                     if (var2.processEndTag("body")) {
                        return var2.process(var8);
                     }
                  } else if (var13.equals("form")) {
                     FormElement var16 = var2.getFormElement();
                     var2.setFormElement((FormElement)null);
                     if (var16 == null || !var2.inScope(var13)) {
                        var2.error(this);
                        return false;
                     }

                     var2.generateImpliedEndTags();
                     if (!var2.currentElement().nodeName().equals(var13)) {
                        var2.error(this);
                     }

                     var2.removeFromStack(var16);
                  } else if (var13.equals("p")) {
                     if (!var2.inButtonScope(var13)) {
                        var2.error(this);
                        var2.processStartTag(var13);
                        return var2.process(var8);
                     }

                     var2.generateImpliedEndTags(var13);
                     if (!var2.currentElement().nodeName().equals(var13)) {
                        var2.error(this);
                     }

                     var2.popStackToClose(var13);
                  } else if (StringUtil.inSorted(var13, HtmlTreeBuilderState.Constants.DdDt)) {
                     if (!var2.inScope(var13)) {
                        var2.error(this);
                        return false;
                     }

                     var2.generateImpliedEndTags(var13);
                     if (!var2.currentElement().nodeName().equals(var13)) {
                        var2.error(this);
                     }

                     var2.popStackToClose(var13);
                  } else if (StringUtil.inSorted(var13, HtmlTreeBuilderState.Constants.Headings)) {
                     if (!var2.inScope(HtmlTreeBuilderState.Constants.Headings)) {
                        var2.error(this);
                        return false;
                     }

                     var2.generateImpliedEndTags(var13);
                     if (!var2.currentElement().nodeName().equals(var13)) {
                        var2.error(this);
                     }

                     var2.popStackToClose(HtmlTreeBuilderState.Constants.Headings);
                  } else {
                     if (var13.equals("sarcasm")) {
                        return this.anyOtherEndTag(var1, var2);
                     }

                     if (!StringUtil.inSorted(var13, HtmlTreeBuilderState.Constants.InBodyStartApplets)) {
                        if (var13.equals("br")) {
                           var2.error(this);
                           var2.processStartTag("br");
                           return false;
                        }

                        return this.anyOtherEndTag(var1, var2);
                     }

                     if (!var2.inScope("name")) {
                        if (!var2.inScope(var13)) {
                           var2.error(this);
                           return false;
                        }

                        var2.generateImpliedEndTags();
                        if (!var2.currentElement().nodeName().equals(var13)) {
                           var2.error(this);
                        }

                        var2.popStackToClose(var13);
                        var2.clearFormattingElementsToLastMarker();
                     }
                  }
               }
               break;
            }
         case Character:
            Token.Character var15 = var1.asCharacter();
            if (var15.getData().equals(HtmlTreeBuilderState.nullString)) {
               var2.error(this);
               return false;
            }

            if (var2.framesetOk() && HtmlTreeBuilderState.isWhitespace((Token)var15)) {
               var2.reconstructFormattingElements();
               var2.insert(var15);
            } else {
               var2.reconstructFormattingElements();
               var2.insert(var15);
               var2.framesetOk(false);
            }
         }

         return true;
      }
   },
   InCaption {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (var1.isEndTag() && var1.asEndTag().name().equals("caption")) {
            if (!var2.inTableScope(var1.asEndTag().name())) {
               var2.error(this);
               return false;
            }

            var2.generateImpliedEndTags();
            if (!var2.currentElement().nodeName().equals("caption")) {
               var2.error(this);
            }

            var2.popStackToClose("caption");
            var2.clearFormattingElementsToLastMarker();
            var2.transition(InTable);
         } else {
            if ((!var1.isStartTag() || !StringUtil.method_16(var1.asStartTag().name(), "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr")) && (!var1.isEndTag() || !var1.asEndTag().name().equals("table"))) {
               if (var1.isEndTag() && StringUtil.method_16(var1.asEndTag().name(), "body", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                  var2.error(this);
                  return false;
               }

               return var2.process(var1, InBody);
            }

            var2.error(this);
            if (var2.processEndTag("caption")) {
               return var2.process(var1);
            }
         }

         return true;
      }
   },
   InCell {
      private boolean anythingElse(Token var1, HtmlTreeBuilder var2) {
         return var2.process(var1, InBody);
      }

      private void closeCell(HtmlTreeBuilder var1) {
         if (var1.inTableScope("td")) {
            var1.processEndTag("td");
         } else {
            var1.processEndTag("th");
         }
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (var1.isEndTag()) {
            String var3 = var1.asEndTag().name();
            if (StringUtil.method_16(var3, "td", "th")) {
               if (!var2.inTableScope(var3)) {
                  var2.error(this);
                  var2.transition(InRow);
                  return false;
               } else {
                  var2.generateImpliedEndTags();
                  if (!var2.currentElement().nodeName().equals(var3)) {
                     var2.error(this);
                  }

                  var2.popStackToClose(var3);
                  var2.clearFormattingElementsToLastMarker();
                  var2.transition(InRow);
                  return true;
               }
            } else if (StringUtil.method_16(var3, "body", "caption", "col", "colgroup", "html")) {
               var2.error(this);
               return false;
            } else if (StringUtil.method_16(var3, "table", "tbody", "tfoot", "thead", "tr")) {
               if (!var2.inTableScope(var3)) {
                  var2.error(this);
                  return false;
               } else {
                  this.closeCell(var2);
                  return var2.process(var1);
               }
            } else {
               return this.anythingElse(var1, var2);
            }
         } else if (var1.isStartTag() && StringUtil.method_16(var1.asStartTag().name(), "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr")) {
            if (!var2.inTableScope("td") && !var2.inTableScope("th")) {
               var2.error(this);
               return false;
            } else {
               this.closeCell(var2);
               return var2.process(var1);
            }
         } else {
            return this.anythingElse(var1, var2);
         }
      }
   },
   InColumnGroup {
      private boolean anythingElse(Token var1, TreeBuilder var2) {
         return var2.processEndTag("colgroup") ? var2.process(var1) : true;
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (HtmlTreeBuilderState.isWhitespace(var1)) {
            var2.insert(var1.asCharacter());
         } else {
            switch(var1.type) {
            case Comment:
               var2.insert(var1.asComment());
               return true;
            case Doctype:
               var2.error(this);
               return true;
            case StartTag:
               Token.StartTag var3 = var1.asStartTag();
               String var4 = var3.name();
               if (var4.equals("html")) {
                  return var2.process(var1, InBody);
               }

               if (var4.equals("col")) {
                  var2.insertEmpty(var3);
                  return true;
               }

               return this.anythingElse(var1, var2);
            case EndTag:
               if (var1.asEndTag().name().equals("colgroup")) {
                  if (var2.currentElement().nodeName().equals("html")) {
                     var2.error(this);
                     return false;
                  }

                  var2.pop();
                  var2.transition(InTable);
                  return true;
               }

               return this.anythingElse(var1, var2);
            case Character:
            default:
               return this.anythingElse(var1, var2);
            case EOF:
               if (!var2.currentElement().nodeName().equals("html")) {
                  return this.anythingElse(var1, var2);
               }
            }
         }

         return true;
      }
   },
   InFrameset {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (HtmlTreeBuilderState.isWhitespace(var1)) {
            var2.insert(var1.asCharacter());
         } else if (var1.isComment()) {
            var2.insert(var1.asComment());
         } else {
            if (var1.isDoctype()) {
               var2.error(this);
               return false;
            }

            if (var1.isStartTag()) {
               Token.StartTag var4 = var1.asStartTag();
               String var3 = var4.name();
               if (var3.equals("html")) {
                  return var2.process(var4, InBody);
               }

               if (var3.equals("frameset")) {
                  var2.insert(var4);
               } else {
                  if (!var3.equals("frame")) {
                     if (var3.equals("noframes")) {
                        return var2.process(var4, InHead);
                     }

                     var2.error(this);
                     return false;
                  }

                  var2.insertEmpty(var4);
               }
            } else if (var1.isEndTag() && var1.asEndTag().name().equals("frameset")) {
               if (var2.currentElement().nodeName().equals("html")) {
                  var2.error(this);
                  return false;
               }

               var2.pop();
               if (!var2.isFragmentParsing() && !var2.currentElement().nodeName().equals("frameset")) {
                  var2.transition(AfterFrameset);
               }
            } else {
               if (!var1.isEOF()) {
                  var2.error(this);
                  return false;
               }

               if (!var2.currentElement().nodeName().equals("html")) {
                  var2.error(this);
                  return true;
               }
            }
         }

         return true;
      }
   },
   InHead {
      private boolean anythingElse(Token var1, TreeBuilder var2) {
         var2.processEndTag("head");
         return var2.process(var1);
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (HtmlTreeBuilderState.isWhitespace(var1)) {
            var2.insert(var1.asCharacter());
         } else {
            String var3;
            switch(var1.type) {
            case Comment:
               var2.insert(var1.asComment());
               return true;
            case Doctype:
               var2.error(this);
               return false;
            case StartTag:
               Token.StartTag var4 = var1.asStartTag();
               var3 = var4.name();
               if (var3.equals("html")) {
                  return InBody.process(var1, var2);
               }

               if (!StringUtil.method_16(var3, "base", "basefont", "bgsound", "command", "link")) {
                  if (var3.equals("meta")) {
                     var2.insertEmpty(var4);
                     return true;
                  }

                  if (var3.equals("title")) {
                     HtmlTreeBuilderState.handleRcData(var4, var2);
                     return true;
                  }

                  if (StringUtil.method_16(var3, "noframes", "style")) {
                     HtmlTreeBuilderState.handleRawtext(var4, var2);
                     return true;
                  }

                  if (var3.equals("noscript")) {
                     var2.insert(var4);
                     var2.transition(InHeadNoscript);
                     return true;
                  }

                  if (var3.equals("script")) {
                     var2.tokeniser.transition(TokeniserState.ScriptData);
                     var2.markInsertionMode();
                     var2.transition(Text);
                     var2.insert(var4);
                     return true;
                  }

                  if (var3.equals("head")) {
                     var2.error(this);
                     return false;
                  }

                  return this.anythingElse(var1, var2);
               }

               Element var5 = var2.insertEmpty(var4);
               if (var3.equals("base") && var5.hasAttr("href")) {
                  var2.maybeSetBaseUri(var5);
                  return true;
               }
               break;
            case EndTag:
               var3 = var1.asEndTag().name();
               if (var3.equals("head")) {
                  var2.pop();
                  var2.transition(AfterHead);
                  return true;
               }

               if (StringUtil.method_16(var3, "body", "html", "br")) {
                  return this.anythingElse(var1, var2);
               }

               var2.error(this);
               return false;
            default:
               return this.anythingElse(var1, var2);
            }
         }

         return true;
      }
   },
   InHeadNoscript {
      private boolean anythingElse(Token var1, HtmlTreeBuilder var2) {
         var2.error(this);
         var2.insert((new Token.Character()).data(var1.toString()));
         return true;
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (var1.isDoctype()) {
            var2.error(this);
         } else {
            if (var1.isStartTag() && var1.asStartTag().name().equals("html")) {
               return var2.process(var1, InBody);
            }

            if (!var1.isEndTag() || !var1.asEndTag().name().equals("noscript")) {
               if (HtmlTreeBuilderState.isWhitespace(var1) || var1.isComment() || var1.isStartTag() && StringUtil.method_16(var1.asStartTag().name(), "basefont", "bgsound", "link", "meta", "noframes", "style")) {
                  return var2.process(var1, InHead);
               }

               if (var1.isEndTag() && var1.asEndTag().name().equals("br")) {
                  return this.anythingElse(var1, var2);
               }

               if ((!var1.isStartTag() || !StringUtil.method_16(var1.asStartTag().name(), "head", "noscript")) && !var1.isEndTag()) {
                  return this.anythingElse(var1, var2);
               }

               var2.error(this);
               return false;
            }

            var2.pop();
            var2.transition(InHead);
         }

         return true;
      }
   },
   InRow {
      private boolean anythingElse(Token var1, HtmlTreeBuilder var2) {
         return var2.process(var1, InTable);
      }

      private boolean handleMissingTr(Token var1, TreeBuilder var2) {
         return var2.processEndTag("tr") ? var2.process(var1) : false;
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (var1.isStartTag()) {
            Token.StartTag var3 = var1.asStartTag();
            String var4 = var3.name();
            if (!StringUtil.method_16(var4, "th", "td")) {
               if (StringUtil.method_16(var4, "caption", "col", "colgroup", "tbody", "tfoot", "thead", "tr")) {
                  return this.handleMissingTr(var1, var2);
               }

               return this.anythingElse(var1, var2);
            }

            var2.clearStackToTableRowContext();
            var2.insert(var3);
            var2.transition(InCell);
            var2.insertMarkerToFormattingElements();
         } else {
            if (!var1.isEndTag()) {
               return this.anythingElse(var1, var2);
            }

            String var5 = var1.asEndTag().name();
            if (!var5.equals("tr")) {
               if (var5.equals("table")) {
                  return this.handleMissingTr(var1, var2);
               }

               if (StringUtil.method_16(var5, "tbody", "tfoot", "thead")) {
                  if (!var2.inTableScope(var5)) {
                     var2.error(this);
                     return false;
                  }

                  var2.processEndTag("tr");
                  return var2.process(var1);
               }

               if (StringUtil.method_16(var5, "body", "caption", "col", "colgroup", "html", "td", "th")) {
                  var2.error(this);
                  return false;
               }

               return this.anythingElse(var1, var2);
            }

            if (!var2.inTableScope(var5)) {
               var2.error(this);
               return false;
            }

            var2.clearStackToTableRowContext();
            var2.pop();
            var2.transition(InTableBody);
         }

         return true;
      }
   },
   InSelect {
      private boolean anythingElse(Token var1, HtmlTreeBuilder var2) {
         var2.error(this);
         return false;
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         boolean var3 = false;
         switch(var1.type) {
         case Comment:
            var2.insert(var1.asComment());
            break;
         case Doctype:
            var2.error(this);
            return false;
         case StartTag:
            Token.StartTag var7 = var1.asStartTag();
            String var5 = var7.name();
            if (var5.equals("html")) {
               return var2.process(var7, InBody);
            }

            if (var5.equals("option")) {
               var2.processEndTag("option");
               var2.insert(var7);
            } else {
               if (!var5.equals("optgroup")) {
                  if (var5.equals("select")) {
                     var2.error(this);
                     return var2.processEndTag("select");
                  }

                  if (!StringUtil.method_16(var5, "input", "keygen", "textarea")) {
                     if (var5.equals("script")) {
                        return var2.process(var1, InHead);
                     }

                     return this.anythingElse(var1, var2);
                  }

                  var2.error(this);
                  if (var2.inSelectScope("select")) {
                     var2.processEndTag("select");
                     return var2.process(var7);
                  }

                  return var3;
               }

               if (var2.currentElement().nodeName().equals("option")) {
                  var2.processEndTag("option");
               } else if (var2.currentElement().nodeName().equals("optgroup")) {
                  var2.processEndTag("optgroup");
               }

               var2.insert(var7);
            }
            break;
         case EndTag:
            String var4 = var1.asEndTag().name();
            if (var4.equals("optgroup")) {
               if (var2.currentElement().nodeName().equals("option") && var2.aboveOnStack(var2.currentElement()) != null && var2.aboveOnStack(var2.currentElement()).nodeName().equals("optgroup")) {
                  var2.processEndTag("option");
               }

               if (var2.currentElement().nodeName().equals("optgroup")) {
                  var2.pop();
               } else {
                  var2.error(this);
               }
            } else if (var4.equals("option")) {
               if (var2.currentElement().nodeName().equals("option")) {
                  var2.pop();
               } else {
                  var2.error(this);
               }
            } else {
               if (!var4.equals("select")) {
                  return this.anythingElse(var1, var2);
               }

               if (!var2.inSelectScope(var4)) {
                  var2.error(this);
                  return false;
               }

               var2.popStackToClose(var4);
               var2.resetInsertionMode();
            }
            break;
         case Character:
            Token.Character var6 = var1.asCharacter();
            if (var6.getData().equals(HtmlTreeBuilderState.nullString)) {
               var2.error(this);
               return false;
            }

            var2.insert(var6);
            break;
         case EOF:
            if (!var2.currentElement().nodeName().equals("html")) {
               var2.error(this);
            }
            break;
         default:
            var3 = this.anythingElse(var1, var2);
            return var3;
         }

         return true;
      }
   },
   InSelectInTable {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         boolean var3 = false;
         if (var1.isStartTag() && StringUtil.method_16(var1.asStartTag().name(), "caption", "table", "tbody", "tfoot", "thead", "tr", "td", "th")) {
            var2.error(this);
            var2.processEndTag("select");
            var3 = var2.process(var1);
         } else {
            if (!var1.isEndTag() || !StringUtil.method_16(var1.asEndTag().name(), "caption", "table", "tbody", "tfoot", "thead", "tr", "td", "th")) {
               return var2.process(var1, InSelect);
            }

            var2.error(this);
            if (var2.inTableScope(var1.asEndTag().name())) {
               var2.processEndTag("select");
               return var2.process(var1);
            }
         }

         return var3;
      }
   },
   InTable {
      boolean anythingElse(Token var1, HtmlTreeBuilder var2) {
         var2.error(this);
         if (StringUtil.method_16(var2.currentElement().nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
            var2.setFosterInserts(true);
            boolean var3 = var2.process(var1, InBody);
            var2.setFosterInserts(false);
            return var3;
         } else {
            return var2.process(var1, InBody);
         }
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         boolean var3 = true;
         if (var1.isCharacter()) {
            var2.newPendingTableCharacters();
            var2.markInsertionMode();
            var2.transition(InTableText);
            var3 = var2.process(var1);
         } else {
            if (var1.isComment()) {
               var2.insert(var1.asComment());
               return true;
            }

            if (var1.isDoctype()) {
               var2.error(this);
               return false;
            }

            if (var1.isStartTag()) {
               Token.StartTag var4 = var1.asStartTag();
               String var5 = var4.name();
               if (var5.equals("caption")) {
                  var2.clearStackToTableContext();
                  var2.insertMarkerToFormattingElements();
                  var2.insert(var4);
                  var2.transition(InCaption);
                  return true;
               }

               if (var5.equals("colgroup")) {
                  var2.clearStackToTableContext();
                  var2.insert(var4);
                  var2.transition(InColumnGroup);
                  return true;
               }

               if (var5.equals("col")) {
                  var2.processStartTag("colgroup");
                  return var2.process(var1);
               }

               if (StringUtil.method_16(var5, "tbody", "tfoot", "thead")) {
                  var2.clearStackToTableContext();
                  var2.insert(var4);
                  var2.transition(InTableBody);
                  return true;
               }

               if (StringUtil.method_16(var5, "td", "th", "tr")) {
                  var2.processStartTag("tbody");
                  return var2.process(var1);
               }

               if (!var5.equals("table")) {
                  if (StringUtil.method_16(var5, "style", "script")) {
                     return var2.process(var1, InHead);
                  }

                  if (var5.equals("input")) {
                     if (!var4.attributes.get("type").equalsIgnoreCase("hidden")) {
                        return this.anythingElse(var1, var2);
                     }

                     var2.insertEmpty(var4);
                     return true;
                  }

                  if (var5.equals("form")) {
                     var2.error(this);
                     if (var2.getFormElement() != null) {
                        return false;
                     }

                     var2.insertForm(var4, false);
                     return true;
                  }

                  return this.anythingElse(var1, var2);
               }

               var2.error(this);
               if (var2.processEndTag("table")) {
                  return var2.process(var1);
               }
            } else {
               if (var1.isEndTag()) {
                  String var6 = var1.asEndTag().name();
                  if (var6.equals("table")) {
                     if (!var2.inTableScope(var6)) {
                        var2.error(this);
                        return false;
                     }

                     var2.popStackToClose("table");
                     var2.resetInsertionMode();
                     return true;
                  }

                  if (StringUtil.method_16(var6, "body", "caption", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                     var2.error(this);
                     return false;
                  }

                  return this.anythingElse(var1, var2);
               }

               if (!var1.isEOF()) {
                  return this.anythingElse(var1, var2);
               }

               if (var2.currentElement().nodeName().equals("html")) {
                  var2.error(this);
                  return true;
               }
            }
         }

         return var3;
      }
   },
   InTableBody {
      private boolean anythingElse(Token var1, HtmlTreeBuilder var2) {
         return var2.process(var1, InTable);
      }

      private boolean exitTableBody(Token var1, HtmlTreeBuilder var2) {
         if (!var2.inTableScope("tbody") && !var2.inTableScope("thead") && !var2.inScope("tfoot")) {
            var2.error(this);
            return false;
         } else {
            var2.clearStackToTableBodyContext();
            var2.processEndTag(var2.currentElement().nodeName());
            return var2.process(var1);
         }
      }

      boolean process(Token var1, HtmlTreeBuilder var2) {
         switch(var1.type) {
         case StartTag:
            Token.StartTag var5 = var1.asStartTag();
            String var4 = var5.name();
            if (!var4.equals("tr")) {
               if (StringUtil.method_16(var4, "th", "td")) {
                  var2.error(this);
                  var2.processStartTag("tr");
                  return var2.process(var5);
               }

               if (StringUtil.method_16(var4, "caption", "col", "colgroup", "tbody", "tfoot", "thead")) {
                  return this.exitTableBody(var1, var2);
               }

               return this.anythingElse(var1, var2);
            }

            var2.clearStackToTableBodyContext();
            var2.insert(var5);
            var2.transition(InRow);
            break;
         case EndTag:
            String var3 = var1.asEndTag().name();
            if (!StringUtil.method_16(var3, "tbody", "tfoot", "thead")) {
               if (var3.equals("table")) {
                  return this.exitTableBody(var1, var2);
               }

               if (StringUtil.method_16(var3, "body", "caption", "col", "colgroup", "html", "td", "th", "tr")) {
                  var2.error(this);
                  return false;
               }

               return this.anythingElse(var1, var2);
            }

            if (!var2.inTableScope(var3)) {
               var2.error(this);
               return false;
            }

            var2.clearStackToTableBodyContext();
            var2.pop();
            var2.transition(InTable);
            break;
         default:
            return this.anythingElse(var1, var2);
         }

         return true;
      }
   },
   InTableText {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         switch(var1.type) {
         case Character:
            Token.Character var5 = var1.asCharacter();
            if (var5.getData().equals(HtmlTreeBuilderState.nullString)) {
               var2.error(this);
               return false;
            }

            var2.getPendingTableCharacters().add(var5.getData());
            return true;
         default:
            if (var2.getPendingTableCharacters().size() > 0) {
               Iterator var3 = var2.getPendingTableCharacters().iterator();

               while(var3.hasNext()) {
                  String var4 = (String)var3.next();
                  if (!HtmlTreeBuilderState.isWhitespace(var4)) {
                     var2.error(this);
                     if (StringUtil.method_16(var2.currentElement().nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                        var2.setFosterInserts(true);
                        var2.process((new Token.Character()).data(var4), InBody);
                        var2.setFosterInserts(false);
                     } else {
                        var2.process((new Token.Character()).data(var4), InBody);
                     }
                  } else {
                     var2.insert((new Token.Character()).data(var4));
                  }
               }

               var2.newPendingTableCharacters();
            }

            var2.transition(var2.originalState());
            return var2.process(var1);
         }
      }
   },
   Initial {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (HtmlTreeBuilderState.isWhitespace(var1)) {
            return true;
         } else if (var1.isComment()) {
            var2.insert(var1.asComment());
            return true;
         } else if (var1.isDoctype()) {
            Token.Doctype var4 = var1.asDoctype();
            DocumentType var3 = new DocumentType(var4.getName(), var4.getPublicIdentifier(), var4.getSystemIdentifier(), var2.getBaseUri());
            var2.getDocument().appendChild(var3);
            if (var4.isForceQuirks()) {
               var2.getDocument().quirksMode(Document.QuirksMode.quirks);
            }

            var2.transition(BeforeHtml);
            return true;
         } else {
            var2.transition(BeforeHtml);
            return var2.process(var1);
         }
      }
   },
   Text {
      boolean process(Token var1, HtmlTreeBuilder var2) {
         if (var1.isCharacter()) {
            var2.insert(var1.asCharacter());
         } else {
            if (var1.isEOF()) {
               var2.error(this);
               var2.pop();
               var2.transition(var2.originalState());
               return var2.process(var1);
            }

            if (var1.isEndTag()) {
               var2.pop();
               var2.transition(var2.originalState());
            }
         }

         return true;
      }
   };

   private static String nullString = String.valueOf('\u0000');

   private HtmlTreeBuilderState() {
   }

   // $FF: synthetic method
   HtmlTreeBuilderState(Object var3) {
      this();
   }

   private static void handleRawtext(Token.StartTag var0, HtmlTreeBuilder var1) {
      var1.insert(var0);
      var1.tokeniser.transition(TokeniserState.Rawtext);
      var1.markInsertionMode();
      var1.transition(Text);
   }

   private static void handleRcData(Token.StartTag var0, HtmlTreeBuilder var1) {
      var1.insert(var0);
      var1.tokeniser.transition(TokeniserState.Rcdata);
      var1.markInsertionMode();
      var1.transition(Text);
   }

   private static boolean isWhitespace(String var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         if (!StringUtil.isWhitespace(var0.charAt(var1))) {
            return false;
         }
      }

      return true;
   }

   private static boolean isWhitespace(Token var0) {
      return var0.isCharacter() ? isWhitespace(var0.asCharacter().getData()) : false;
   }

   abstract boolean process(Token var1, HtmlTreeBuilder var2);

   private static final class Constants {
      private static final String[] DdDt = new String[]{"dd", "dt"};
      private static final String[] Formatters = new String[]{"b", "big", "code", "em", "font", "i", "s", "small", "strike", "strong", "tt", "u"};
      private static final String[] Headings = new String[]{"h1", "h2", "h3", "h4", "h5", "h6"};
      private static final String[] InBodyEndAdoptionFormatters = new String[]{"a", "b", "big", "code", "em", "font", "i", "nobr", "s", "small", "strike", "strong", "tt", "u"};
      private static final String[] InBodyEndClosers = new String[]{"address", "article", "aside", "blockquote", "button", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "listing", "menu", "nav", "ol", "pre", "section", "summary", "ul"};
      private static final String[] InBodyEndTableFosters = new String[]{"table", "tbody", "tfoot", "thead", "tr"};
      private static final String[] InBodyStartApplets = new String[]{"applet", "marquee", "object"};
      private static final String[] InBodyStartDrop = new String[]{"caption", "col", "colgroup", "frame", "head", "tbody", "td", "tfoot", "th", "thead", "tr"};
      private static final String[] InBodyStartEmptyFormatters = new String[]{"area", "br", "embed", "img", "keygen", "wbr"};
      private static final String[] InBodyStartInputAttribs = new String[]{"name", "action", "prompt"};
      private static final String[] InBodyStartLiBreakers = new String[]{"address", "div", "p"};
      private static final String[] InBodyStartMedia = new String[]{"param", "source", "track"};
      private static final String[] InBodyStartOptions = new String[]{"optgroup", "option"};
      private static final String[] InBodyStartPClosers = new String[]{"address", "article", "aside", "blockquote", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "menu", "nav", "ol", "p", "section", "summary", "ul"};
      private static final String[] InBodyStartPreListing = new String[]{"pre", "listing"};
      private static final String[] InBodyStartRuby = new String[]{"rp", "rt"};
      private static final String[] InBodyStartToHead = new String[]{"base", "basefont", "bgsound", "command", "link", "meta", "noframes", "script", "style", "title"};
   }
}
