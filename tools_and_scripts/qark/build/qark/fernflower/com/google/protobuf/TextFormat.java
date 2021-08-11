package com.google.protobuf;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextFormat {
   private static final int BUFFER_SIZE = 4096;
   private static final TextFormat.Printer DEFAULT_PRINTER = new TextFormat.Printer();
   private static final TextFormat.Printer SINGLE_LINE_PRINTER = (new TextFormat.Printer()).setSingleLineMode(true);
   private static final TextFormat.Printer UNICODE_PRINTER = (new TextFormat.Printer()).setEscapeNonAscii(false);

   private TextFormat() {
   }

   private static int digitValue(byte var0) {
      if (48 <= var0 && var0 <= 57) {
         return var0 - 48;
      } else {
         return 97 <= var0 && var0 <= 122 ? var0 - 97 + 10 : var0 - 65 + 10;
      }
   }

   static String escapeBytes(ByteString var0) {
      StringBuilder var3 = new StringBuilder(var0.size());

      for(int var1 = 0; var1 < var0.size(); ++var1) {
         byte var2 = var0.byteAt(var1);
         if (var2 != 34) {
            if (var2 != 39) {
               if (var2 != 92) {
                  switch(var2) {
                  case 7:
                     var3.append("\\a");
                     break;
                  case 8:
                     var3.append("\\b");
                     break;
                  case 9:
                     var3.append("\\t");
                     break;
                  case 10:
                     var3.append("\\n");
                     break;
                  case 11:
                     var3.append("\\v");
                     break;
                  case 12:
                     var3.append("\\f");
                     break;
                  case 13:
                     var3.append("\\r");
                     break;
                  default:
                     if (var2 >= 32) {
                        var3.append((char)var2);
                     } else {
                        var3.append('\\');
                        var3.append((char)((var2 >>> 6 & 3) + 48));
                        var3.append((char)((var2 >>> 3 & 7) + 48));
                        var3.append((char)((var2 & 7) + 48));
                     }
                  }
               } else {
                  var3.append("\\\\");
               }
            } else {
               var3.append("\\'");
            }
         } else {
            var3.append("\\\"");
         }
      }

      return var3.toString();
   }

   static String escapeText(String var0) {
      return escapeBytes(ByteString.copyFromUtf8(var0));
   }

   private static boolean isHex(byte var0) {
      return 48 <= var0 && var0 <= 57 || 97 <= var0 && var0 <= 102 || 65 <= var0 && var0 <= 70;
   }

   private static boolean isOctal(byte var0) {
      return 48 <= var0 && var0 <= 55;
   }

   public static void merge(CharSequence var0, ExtensionRegistry var1, Message.Builder var2) throws TextFormat.ParseException {
      TextFormat.Tokenizer var3 = new TextFormat.Tokenizer(var0);

      while(!var3.atEnd()) {
         mergeField(var3, var1, var2);
      }

   }

   public static void merge(CharSequence var0, Message.Builder var1) throws TextFormat.ParseException {
      merge(var0, ExtensionRegistry.getEmptyRegistry(), var1);
   }

   public static void merge(Readable var0, ExtensionRegistry var1, Message.Builder var2) throws IOException {
      merge((CharSequence)toStringBuilder(var0), var1, var2);
   }

   public static void merge(Readable var0, Message.Builder var1) throws IOException {
      merge(var0, ExtensionRegistry.getEmptyRegistry(), var1);
   }

   private static void mergeField(TextFormat.Tokenizer var0, ExtensionRegistry var1, Message.Builder var2) throws TextFormat.ParseException {
      Descriptors.Descriptor var10 = var2.getDescriptorForType();
      Object var8 = null;
      boolean var4 = var0.tryConsume("[");
      Descriptors.FieldDescriptor var7 = null;
      ExtensionRegistry.ExtensionInfo var6 = null;
      StringBuilder var13;
      Descriptors.FieldDescriptor var15;
      String var17;
      if (var4) {
         StringBuilder var5 = new StringBuilder(var0.consumeIdentifier());

         while(var0.tryConsume(".")) {
            var5.append('.');
            var5.append(var0.consumeIdentifier());
         }

         var6 = var1.findExtensionByName(var5.toString());
         if (var6 == null) {
            var13 = new StringBuilder();
            var13.append("Extension \"");
            var13.append(var5);
            var13.append("\" not found in the ExtensionRegistry.");
            throw var0.parseExceptionPreviousToken(var13.toString());
         }

         if (var6.descriptor.getContainingType() != var10) {
            var13 = new StringBuilder();
            var13.append("Extension \"");
            var13.append(var5);
            var13.append("\" does not extend message type \"");
            var13.append(var10.getFullName());
            var13.append("\".");
            throw var0.parseExceptionPreviousToken(var13.toString());
         }

         var0.consume("]");
         var15 = var6.descriptor;
      } else {
         String var11 = var0.consumeIdentifier();
         var7 = var10.findFieldByName(var11);
         var15 = var7;
         if (var7 == null) {
            var17 = var11.toLowerCase(Locale.US);
            Descriptors.FieldDescriptor var9 = var10.findFieldByName(var17);
            var15 = var9;
            if (var9 != null) {
               var15 = var9;
               if (var9.getType() != Descriptors.FieldDescriptor.Type.GROUP) {
                  var15 = null;
               }
            }
         }

         var7 = var15;
         if (var15 != null) {
            var7 = var15;
            if (var15.getType() == Descriptors.FieldDescriptor.Type.GROUP) {
               var7 = var15;
               if (!var15.getMessageType().getName().equals(var11)) {
                  var7 = null;
               }
            }
         }

         if (var7 == null) {
            var13 = new StringBuilder();
            var13.append("Message type \"");
            var13.append(var10.getFullName());
            var13.append("\" has no field named \"");
            var13.append(var11);
            var13.append("\".");
            throw var0.parseExceptionPreviousToken(var13.toString());
         }

         var15 = var7;
         var6 = (ExtensionRegistry.ExtensionInfo)var8;
      }

      var7 = null;
      Object var12;
      if (var15.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
         var0.tryConsume(":");
         if (var0.tryConsume("<")) {
            var17 = ">";
         } else {
            var0.consume("{");
            var17 = "}";
         }

         Message.Builder var16;
         if (var6 == null) {
            var16 = var2.newBuilderForField(var15);
         } else {
            var16 = var6.defaultInstance.newBuilderForType();
         }

         while(!var0.tryConsume(var17)) {
            if (var0.atEnd()) {
               var13 = new StringBuilder();
               var13.append("Expected \"");
               var13.append(var17);
               var13.append("\".");
               throw var0.parseException(var13.toString());
            }

            mergeField(var0, var1, var16);
         }

         var12 = var16.buildPartial();
      } else {
         var0.consume(":");
         switch(null.$SwitchMap$com$google$protobuf$Descriptors$FieldDescriptor$Type[var15.getType().ordinal()]) {
         case 1:
         case 2:
         case 3:
            var12 = var0.consumeInt32();
            break;
         case 4:
         case 5:
         case 6:
            var12 = var0.consumeInt64();
            break;
         case 7:
            var12 = var0.consumeBoolean();
            break;
         case 8:
            var12 = var0.consumeFloat();
            break;
         case 9:
            var12 = var0.consumeDouble();
            break;
         case 10:
         case 11:
            var12 = var0.consumeUInt32();
            break;
         case 12:
         case 13:
            var12 = var0.consumeUInt64();
            break;
         case 14:
            var12 = var0.consumeString();
            break;
         case 15:
            var12 = var0.consumeByteString();
            break;
         case 16:
            Descriptors.EnumDescriptor var18 = var15.getEnumType();
            Descriptors.EnumValueDescriptor var14;
            if (var0.lookingAtInteger()) {
               int var3 = var0.consumeInt32();
               var14 = var18.findValueByNumber(var3);
               if (var14 == null) {
                  var13 = new StringBuilder();
                  var13.append("Enum type \"");
                  var13.append(var18.getFullName());
                  var13.append("\" has no value with number ");
                  var13.append(var3);
                  var13.append('.');
                  throw var0.parseExceptionPreviousToken(var13.toString());
               }

               var12 = var14;
            } else {
               var17 = var0.consumeIdentifier();
               var14 = var18.findValueByName(var17);
               if (var14 == null) {
                  var13 = new StringBuilder();
                  var13.append("Enum type \"");
                  var13.append(var18.getFullName());
                  var13.append("\" has no value named \"");
                  var13.append(var17);
                  var13.append("\".");
                  throw var0.parseExceptionPreviousToken(var13.toString());
               }

               var12 = var14;
            }
            break;
         case 17:
         case 18:
            throw new RuntimeException("Can't get here.");
         default:
            var12 = var7;
         }
      }

      if (var15.isRepeated()) {
         var2.addRepeatedField(var15, var12);
      } else {
         var2.setField(var15, var12);
      }
   }

   static int parseInt32(String var0) throws NumberFormatException {
      return (int)parseInteger(var0, true, false);
   }

   static long parseInt64(String var0) throws NumberFormatException {
      return parseInteger(var0, true, true);
   }

   private static long parseInteger(String var0, boolean var1, boolean var2) throws NumberFormatException {
      int var3 = 0;
      boolean var5 = false;
      StringBuilder var13;
      if (var0.startsWith("-", 0)) {
         if (!var1) {
            var13 = new StringBuilder();
            var13.append("Number must be positive: ");
            var13.append(var0);
            throw new NumberFormatException(var13.toString());
         }

         var3 = 0 + 1;
         var5 = true;
      }

      byte var4 = 10;
      int var6;
      if (var0.startsWith("0x", var3)) {
         var6 = var3 + 2;
         var4 = 16;
      } else {
         var6 = var3;
         if (var0.startsWith("0", var3)) {
            var4 = 8;
            var6 = var3;
         }
      }

      String var11 = var0.substring(var6);
      long var9;
      if (var11.length() < 16) {
         var9 = Long.parseLong(var11, var4);
         long var7 = var9;
         if (var5) {
            var7 = -var9;
         }

         var9 = var7;
         if (!var2) {
            if (var1) {
               if (var7 <= 2147483647L && var7 >= -2147483648L) {
                  return var7;
               }

               var13 = new StringBuilder();
               var13.append("Number out of range for 32-bit signed integer: ");
               var13.append(var0);
               throw new NumberFormatException(var13.toString());
            }

            if (var7 < 4294967296L && var7 >= 0L) {
               return var7;
            }

            var13 = new StringBuilder();
            var13.append("Number out of range for 32-bit unsigned integer: ");
            var13.append(var0);
            throw new NumberFormatException(var13.toString());
         }
      } else {
         BigInteger var12 = new BigInteger(var11, var4);
         BigInteger var14 = var12;
         if (var5) {
            var14 = var12.negate();
         }

         if (!var2) {
            if (var1) {
               if (var14.bitLength() > 31) {
                  var13 = new StringBuilder();
                  var13.append("Number out of range for 32-bit signed integer: ");
                  var13.append(var0);
                  throw new NumberFormatException(var13.toString());
               }
            } else if (var14.bitLength() > 32) {
               var13 = new StringBuilder();
               var13.append("Number out of range for 32-bit unsigned integer: ");
               var13.append(var0);
               throw new NumberFormatException(var13.toString());
            }
         } else if (var1) {
            if (var14.bitLength() > 63) {
               var13 = new StringBuilder();
               var13.append("Number out of range for 64-bit signed integer: ");
               var13.append(var0);
               throw new NumberFormatException(var13.toString());
            }
         } else if (var14.bitLength() > 64) {
            var13 = new StringBuilder();
            var13.append("Number out of range for 64-bit unsigned integer: ");
            var13.append(var0);
            throw new NumberFormatException(var13.toString());
         }

         var9 = var14.longValue();
      }

      return var9;
   }

   static int parseUInt32(String var0) throws NumberFormatException {
      return (int)parseInteger(var0, false, false);
   }

   static long parseUInt64(String var0) throws NumberFormatException {
      return parseInteger(var0, false, true);
   }

   public static void print(MessageOrBuilder var0, Appendable var1) throws IOException {
      DEFAULT_PRINTER.print(var0, new TextFormat.TextGenerator(var1));
   }

   public static void print(UnknownFieldSet var0, Appendable var1) throws IOException {
      DEFAULT_PRINTER.printUnknownFields(var0, new TextFormat.TextGenerator(var1));
   }

   public static void printField(Descriptors.FieldDescriptor var0, Object var1, Appendable var2) throws IOException {
      DEFAULT_PRINTER.printField(var0, var1, new TextFormat.TextGenerator(var2));
   }

   public static String printFieldToString(Descriptors.FieldDescriptor var0, Object var1) {
      try {
         StringBuilder var2 = new StringBuilder();
         printField(var0, var1, var2);
         String var4 = var2.toString();
         return var4;
      } catch (IOException var3) {
         throw new IllegalStateException(var3);
      }
   }

   public static void printFieldValue(Descriptors.FieldDescriptor var0, Object var1, Appendable var2) throws IOException {
      DEFAULT_PRINTER.printFieldValue(var0, var1, new TextFormat.TextGenerator(var2));
   }

   public static String printToString(MessageOrBuilder var0) {
      try {
         StringBuilder var1 = new StringBuilder();
         print((MessageOrBuilder)var0, var1);
         String var3 = var1.toString();
         return var3;
      } catch (IOException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String printToString(UnknownFieldSet var0) {
      try {
         StringBuilder var1 = new StringBuilder();
         print((UnknownFieldSet)var0, var1);
         String var3 = var1.toString();
         return var3;
      } catch (IOException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String printToUnicodeString(MessageOrBuilder var0) {
      try {
         StringBuilder var1 = new StringBuilder();
         UNICODE_PRINTER.print(var0, new TextFormat.TextGenerator(var1));
         String var3 = var1.toString();
         return var3;
      } catch (IOException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String printToUnicodeString(UnknownFieldSet var0) {
      try {
         StringBuilder var1 = new StringBuilder();
         UNICODE_PRINTER.printUnknownFields(var0, new TextFormat.TextGenerator(var1));
         String var3 = var1.toString();
         return var3;
      } catch (IOException var2) {
         throw new IllegalStateException(var2);
      }
   }

   private static void printUnknownFieldValue(int var0, Object var1, TextFormat.TextGenerator var2) throws IOException {
      int var3 = WireFormat.getTagWireType(var0);
      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  if (var3 == 5) {
                     var2.print(String.format((Locale)null, "0x%08x", (Integer)var1));
                  } else {
                     StringBuilder var4 = new StringBuilder();
                     var4.append("Bad tag: ");
                     var4.append(var0);
                     throw new IllegalArgumentException(var4.toString());
                  }
               } else {
                  DEFAULT_PRINTER.printUnknownFields((UnknownFieldSet)var1, var2);
               }
            } else {
               var2.print("\"");
               var2.print(escapeBytes((ByteString)var1));
               var2.print("\"");
            }
         } else {
            var2.print(String.format((Locale)null, "0x%016x", (Long)var1));
         }
      } else {
         var2.print(unsignedToString((Long)var1));
      }
   }

   public static void printUnknownFieldValue(int var0, Object var1, Appendable var2) throws IOException {
      printUnknownFieldValue(var0, var1, new TextFormat.TextGenerator(var2));
   }

   public static String shortDebugString(MessageOrBuilder var0) {
      try {
         StringBuilder var1 = new StringBuilder();
         SINGLE_LINE_PRINTER.print(var0, new TextFormat.TextGenerator(var1));
         String var3 = var1.toString().trim();
         return var3;
      } catch (IOException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String shortDebugString(UnknownFieldSet var0) {
      try {
         StringBuilder var1 = new StringBuilder();
         SINGLE_LINE_PRINTER.printUnknownFields(var0, new TextFormat.TextGenerator(var1));
         String var3 = var1.toString().trim();
         return var3;
      } catch (IOException var2) {
         throw new IllegalStateException(var2);
      }
   }

   private static StringBuilder toStringBuilder(Readable var0) throws IOException {
      StringBuilder var2 = new StringBuilder();
      CharBuffer var3 = CharBuffer.allocate(4096);

      while(true) {
         int var1 = var0.read(var3);
         if (var1 == -1) {
            return var2;
         }

         var3.flip();
         var2.append(var3, 0, var1);
      }
   }

   static ByteString unescapeBytes(CharSequence var0) throws TextFormat.InvalidEscapeSequenceException {
      ByteString var8 = ByteString.copyFromUtf8(var0.toString());
      byte[] var7 = new byte[var8.size()];
      int var6 = 0;
      int var2 = 0;

      int var4;
      for(boolean var3 = false; var2 < var8.size(); var2 = var4) {
         byte var1 = var8.byteAt(var2);
         int var5;
         if (var1 == 92) {
            if (var2 + 1 >= var8.size()) {
               throw new TextFormat.InvalidEscapeSequenceException("Invalid escape sequence: '\\' at end of string.");
            }

            var5 = var2 + 1;
            var1 = var8.byteAt(var5);
            int var10;
            if (isOctal(var1)) {
               var10 = digitValue(var1);
               var4 = var5;
               var2 = var10;
               if (var5 + 1 < var8.size()) {
                  var4 = var5;
                  var2 = var10;
                  if (isOctal(var8.byteAt(var5 + 1))) {
                     var4 = var5 + 1;
                     var2 = var10 * 8 + digitValue(var8.byteAt(var4));
                  }
               }

               var5 = var4;
               var10 = var2;
               if (var4 + 1 < var8.size()) {
                  var5 = var4;
                  var10 = var2;
                  if (isOctal(var8.byteAt(var4 + 1))) {
                     var5 = var4 + 1;
                     var10 = var2 * 8 + digitValue(var8.byteAt(var5));
                  }
               }

               var7[var6] = (byte)var10;
               var2 = var6 + 1;
               var4 = var5;
            } else if (var1 != 34) {
               if (var1 != 39) {
                  if (var1 != 92) {
                     if (var1 != 102) {
                        if (var1 != 110) {
                           if (var1 != 114) {
                              if (var1 != 116) {
                                 if (var1 != 118) {
                                    if (var1 != 120) {
                                       if (var1 != 97) {
                                          if (var1 != 98) {
                                             StringBuilder var9 = new StringBuilder();
                                             var9.append("Invalid escape sequence: '\\");
                                             var9.append((char)var1);
                                             var9.append('\'');
                                             throw new TextFormat.InvalidEscapeSequenceException(var9.toString());
                                          }

                                          var2 = var6 + 1;
                                          var7[var6] = 8;
                                          var4 = var5;
                                       } else {
                                          var2 = var6 + 1;
                                          var7[var6] = 7;
                                          var4 = var5;
                                       }
                                    } else {
                                       if (var5 + 1 >= var8.size() || !isHex(var8.byteAt(var5 + 1))) {
                                          throw new TextFormat.InvalidEscapeSequenceException("Invalid escape sequence: '\\x' with no digits");
                                       }

                                       ++var5;
                                       var2 = digitValue(var8.byteAt(var5));
                                       var4 = var5;
                                       var10 = var2;
                                       if (var5 + 1 < var8.size()) {
                                          var4 = var5;
                                          var10 = var2;
                                          if (isHex(var8.byteAt(var5 + 1))) {
                                             var4 = var5 + 1;
                                             var10 = var2 * 16 + digitValue(var8.byteAt(var4));
                                          }
                                       }

                                       var2 = var6 + 1;
                                       var7[var6] = (byte)var10;
                                    }
                                 } else {
                                    var2 = var6 + 1;
                                    var7[var6] = 11;
                                    var4 = var5;
                                 }
                              } else {
                                 var2 = var6 + 1;
                                 var7[var6] = 9;
                                 var4 = var5;
                              }
                           } else {
                              var2 = var6 + 1;
                              var7[var6] = 13;
                              var4 = var5;
                           }
                        } else {
                           var2 = var6 + 1;
                           var7[var6] = 10;
                           var4 = var5;
                        }
                     } else {
                        var2 = var6 + 1;
                        var7[var6] = 12;
                        var4 = var5;
                     }
                  } else {
                     var7[var6] = 92;
                     var2 = var6 + 1;
                     var4 = var5;
                  }
               } else {
                  var2 = var6 + 1;
                  var7[var6] = 39;
                  var4 = var5;
               }
            } else {
               var2 = var6 + 1;
               var7[var6] = 34;
               var4 = var5;
            }
         } else {
            var7[var6] = var1;
            var5 = var6 + 1;
            var4 = var2;
            var2 = var5;
         }

         ++var4;
         var6 = var2;
      }

      return ByteString.copyFrom(var7, 0, var6);
   }

   static String unescapeText(String var0) throws TextFormat.InvalidEscapeSequenceException {
      return unescapeBytes(var0).toStringUtf8();
   }

   private static String unsignedToString(int var0) {
      return var0 >= 0 ? Integer.toString(var0) : Long.toString((long)var0 & 4294967295L);
   }

   private static String unsignedToString(long var0) {
      return var0 >= 0L ? Long.toString(var0) : BigInteger.valueOf(Long.MAX_VALUE & var0).setBit(63).toString();
   }

   static class InvalidEscapeSequenceException extends IOException {
      private static final long serialVersionUID = -8164033650142593304L;

      InvalidEscapeSequenceException(String var1) {
         super(var1);
      }
   }

   public static class ParseException extends IOException {
      private static final long serialVersionUID = 3196188060225107702L;
      private final int column;
      private final int line;

      public ParseException(int var1, int var2, String var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append(Integer.toString(var1));
         var4.append(":");
         var4.append(var2);
         var4.append(": ");
         var4.append(var3);
         super(var4.toString());
         this.line = var1;
         this.column = var2;
      }

      public ParseException(String var1) {
         this(-1, -1, var1);
      }

      public int getColumn() {
         return this.column;
      }

      public int getLine() {
         return this.line;
      }
   }

   private static final class Printer {
      boolean escapeNonAscii;
      boolean singleLineMode;

      private Printer() {
         this.singleLineMode = false;
         this.escapeNonAscii = true;
      }

      // $FF: synthetic method
      Printer(Object var1) {
         this();
      }

      private void print(MessageOrBuilder var1, TextFormat.TextGenerator var2) throws IOException {
         Iterator var3 = var1.getAllFields().entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            this.printField((Descriptors.FieldDescriptor)var4.getKey(), var4.getValue(), var2);
         }

         this.printUnknownFields(var1.getUnknownFields(), var2);
      }

      private void printField(Descriptors.FieldDescriptor var1, Object var2, TextFormat.TextGenerator var3) throws IOException {
         if (!var1.isRepeated()) {
            this.printSingleField(var1, var2, var3);
         } else {
            Iterator var4 = ((List)var2).iterator();

            while(var4.hasNext()) {
               this.printSingleField(var1, var4.next(), var3);
            }

         }
      }

      private void printFieldValue(Descriptors.FieldDescriptor var1, Object var2, TextFormat.TextGenerator var3) throws IOException {
         switch(null.$SwitchMap$com$google$protobuf$Descriptors$FieldDescriptor$Type[var1.getType().ordinal()]) {
         case 1:
         case 2:
         case 3:
            var3.print(((Integer)var2).toString());
            return;
         case 4:
         case 5:
         case 6:
            var3.print(((Long)var2).toString());
            return;
         case 7:
            var3.print(((Boolean)var2).toString());
            return;
         case 8:
            var3.print(((Float)var2).toString());
            return;
         case 9:
            var3.print(((Double)var2).toString());
            return;
         case 10:
         case 11:
            var3.print(TextFormat.unsignedToString((Integer)var2));
            return;
         case 12:
         case 13:
            var3.print(TextFormat.unsignedToString((Long)var2));
            return;
         case 14:
            var3.print("\"");
            String var4;
            if (this.escapeNonAscii) {
               var4 = TextFormat.escapeText((String)var2);
            } else {
               var4 = (String)var2;
            }

            var3.print(var4);
            var3.print("\"");
            return;
         case 15:
            var3.print("\"");
            var3.print(TextFormat.escapeBytes((ByteString)var2));
            var3.print("\"");
            return;
         case 16:
            var3.print(((Descriptors.EnumValueDescriptor)var2).getName());
            return;
         case 17:
         case 18:
            this.print((Message)var2, var3);
            return;
         default:
         }
      }

      private void printSingleField(Descriptors.FieldDescriptor var1, Object var2, TextFormat.TextGenerator var3) throws IOException {
         if (var1.isExtension()) {
            var3.print("[");
            if (var1.getContainingType().getOptions().getMessageSetWireFormat() && var1.getType() == Descriptors.FieldDescriptor.Type.MESSAGE && var1.isOptional() && var1.getExtensionScope() == var1.getMessageType()) {
               var3.print(var1.getMessageType().getFullName());
            } else {
               var3.print(var1.getFullName());
            }

            var3.print("]");
         } else if (var1.getType() == Descriptors.FieldDescriptor.Type.GROUP) {
            var3.print(var1.getMessageType().getName());
         } else {
            var3.print(var1.getName());
         }

         if (var1.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
            if (this.singleLineMode) {
               var3.print(" { ");
            } else {
               var3.print(" {\n");
               var3.indent();
            }
         } else {
            var3.print(": ");
         }

         this.printFieldValue(var1, var2, var3);
         if (var1.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
            if (this.singleLineMode) {
               var3.print("} ");
            } else {
               var3.outdent();
               var3.print("}\n");
            }
         } else if (this.singleLineMode) {
            var3.print(" ");
         } else {
            var3.print("\n");
         }
      }

      private void printUnknownField(int var1, int var2, List var3, TextFormat.TextGenerator var4) throws IOException {
         String var7;
         for(Iterator var5 = var3.iterator(); var5.hasNext(); var4.print(var7)) {
            Object var6 = var5.next();
            var4.print(String.valueOf(var1));
            var4.print(": ");
            TextFormat.printUnknownFieldValue(var2, var6, var4);
            if (this.singleLineMode) {
               var7 = " ";
            } else {
               var7 = "\n";
            }
         }

      }

      private void printUnknownFields(UnknownFieldSet var1, TextFormat.TextGenerator var2) throws IOException {
         Iterator var7 = var1.asMap().entrySet().iterator();

         while(var7.hasNext()) {
            Entry var4 = (Entry)var7.next();
            int var3 = (Integer)var4.getKey();
            UnknownFieldSet.Field var5 = (UnknownFieldSet.Field)var4.getValue();
            this.printUnknownField(var3, 0, var5.getVarintList(), var2);
            this.printUnknownField(var3, 5, var5.getFixed32List(), var2);
            this.printUnknownField(var3, 1, var5.getFixed64List(), var2);
            this.printUnknownField(var3, 2, var5.getLengthDelimitedList(), var2);
            Iterator var8 = var5.getGroupList().iterator();

            while(var8.hasNext()) {
               UnknownFieldSet var6 = (UnknownFieldSet)var8.next();
               var2.print(((Integer)var4.getKey()).toString());
               if (this.singleLineMode) {
                  var2.print(" { ");
               } else {
                  var2.print(" {\n");
                  var2.indent();
               }

               this.printUnknownFields(var6, var2);
               if (this.singleLineMode) {
                  var2.print("} ");
               } else {
                  var2.outdent();
                  var2.print("}\n");
               }
            }
         }

      }

      private TextFormat.Printer setEscapeNonAscii(boolean var1) {
         this.escapeNonAscii = var1;
         return this;
      }

      private TextFormat.Printer setSingleLineMode(boolean var1) {
         this.singleLineMode = var1;
         return this;
      }
   }

   private static final class TextGenerator {
      private boolean atStartOfLine;
      private final StringBuilder indent;
      private final Appendable output;

      private TextGenerator(Appendable var1) {
         this.indent = new StringBuilder();
         this.atStartOfLine = true;
         this.output = var1;
      }

      // $FF: synthetic method
      TextGenerator(Appendable var1, Object var2) {
         this(var1);
      }

      private void write(CharSequence var1, int var2) throws IOException {
         if (var2 != 0) {
            if (this.atStartOfLine) {
               this.atStartOfLine = false;
               this.output.append(this.indent);
            }

            this.output.append(var1);
         }
      }

      public void indent() {
         this.indent.append("  ");
      }

      public void outdent() {
         int var1 = this.indent.length();
         if (var1 != 0) {
            this.indent.delete(var1 - 2, var1);
         } else {
            throw new IllegalArgumentException(" Outdent() without matching Indent().");
         }
      }

      public void print(CharSequence var1) throws IOException {
         int var5 = var1.length();
         int var3 = 0;

         int var4;
         for(int var2 = 0; var2 < var5; var3 = var4) {
            var4 = var3;
            if (var1.charAt(var2) == '\n') {
               this.write(var1.subSequence(var3, var5), var2 - var3 + 1);
               var4 = var2 + 1;
               this.atStartOfLine = true;
            }

            ++var2;
         }

         this.write(var1.subSequence(var3, var5), var5 - var3);
      }
   }

   private static final class Tokenizer {
      private static final Pattern DOUBLE_INFINITY = Pattern.compile("-?inf(inity)?", 2);
      private static final Pattern FLOAT_INFINITY = Pattern.compile("-?inf(inity)?f?", 2);
      private static final Pattern FLOAT_NAN = Pattern.compile("nanf?", 2);
      private static final Pattern TOKEN = Pattern.compile("[a-zA-Z_][0-9a-zA-Z_+-]*+|[.]?[0-9+-][0-9a-zA-Z_.+-]*+|\"([^\"\n\\\\]|\\\\.)*+(\"|\\\\?$)|'([^'\n\\\\]|\\\\.)*+('|\\\\?$)", 8);
      private static final Pattern WHITESPACE = Pattern.compile("(\\s|(#.*$))++", 8);
      private int column;
      private String currentToken;
      private int line;
      private final Matcher matcher;
      private int pos;
      private int previousColumn;
      private int previousLine;
      private final CharSequence text;

      private Tokenizer(CharSequence var1) {
         this.pos = 0;
         this.line = 0;
         this.column = 0;
         this.previousLine = 0;
         this.previousColumn = 0;
         this.text = var1;
         this.matcher = WHITESPACE.matcher(var1);
         this.skipWhitespace();
         this.nextToken();
      }

      // $FF: synthetic method
      Tokenizer(CharSequence var1, Object var2) {
         this(var1);
      }

      private void consumeByteString(List var1) throws TextFormat.ParseException {
         int var3 = this.currentToken.length();
         char var2 = 0;
         if (var3 > 0) {
            var2 = this.currentToken.charAt(0);
         }

         if (var2 != '"' && var2 != '\'') {
            throw this.parseException("Expected string.");
         } else {
            if (this.currentToken.length() >= 2) {
               String var4 = this.currentToken;
               if (var4.charAt(var4.length() - 1) == var2) {
                  try {
                     ByteString var6 = TextFormat.unescapeBytes(this.currentToken.substring(1, this.currentToken.length() - 1));
                     this.nextToken();
                     var1.add(var6);
                     return;
                  } catch (TextFormat.InvalidEscapeSequenceException var5) {
                     throw this.parseException(var5.getMessage());
                  }
               }
            }

            throw this.parseException("String missing ending quote.");
         }
      }

      private TextFormat.ParseException floatParseException(NumberFormatException var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Couldn't parse number: ");
         var2.append(var1.getMessage());
         return this.parseException(var2.toString());
      }

      private TextFormat.ParseException integerParseException(NumberFormatException var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Couldn't parse integer: ");
         var2.append(var1.getMessage());
         return this.parseException(var2.toString());
      }

      private void skipWhitespace() {
         this.matcher.usePattern(WHITESPACE);
         if (this.matcher.lookingAt()) {
            Matcher var1 = this.matcher;
            var1.region(var1.end(), this.matcher.regionEnd());
         }

      }

      public boolean atEnd() {
         return this.currentToken.length() == 0;
      }

      public void consume(String var1) throws TextFormat.ParseException {
         if (!this.tryConsume(var1)) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Expected \"");
            var2.append(var1);
            var2.append("\".");
            throw this.parseException(var2.toString());
         }
      }

      public boolean consumeBoolean() throws TextFormat.ParseException {
         if (!this.currentToken.equals("true") && !this.currentToken.equals("t") && !this.currentToken.equals("1")) {
            if (!this.currentToken.equals("false") && !this.currentToken.equals("f") && !this.currentToken.equals("0")) {
               throw this.parseException("Expected \"true\" or \"false\".");
            } else {
               this.nextToken();
               return false;
            }
         } else {
            this.nextToken();
            return true;
         }
      }

      public ByteString consumeByteString() throws TextFormat.ParseException {
         ArrayList var1 = new ArrayList();
         this.consumeByteString(var1);

         while(this.currentToken.startsWith("'") || this.currentToken.startsWith("\"")) {
            this.consumeByteString(var1);
         }

         return ByteString.copyFrom((Iterable)var1);
      }

      public double consumeDouble() throws TextFormat.ParseException {
         if (DOUBLE_INFINITY.matcher(this.currentToken).matches()) {
            boolean var3 = this.currentToken.startsWith("-");
            this.nextToken();
            return var3 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
         } else if (this.currentToken.equalsIgnoreCase("nan")) {
            this.nextToken();
            return Double.NaN;
         } else {
            try {
               double var1 = Double.parseDouble(this.currentToken);
               this.nextToken();
               return var1;
            } catch (NumberFormatException var5) {
               throw this.floatParseException(var5);
            }
         }
      }

      public float consumeFloat() throws TextFormat.ParseException {
         if (FLOAT_INFINITY.matcher(this.currentToken).matches()) {
            boolean var2 = this.currentToken.startsWith("-");
            this.nextToken();
            return var2 ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
         } else if (FLOAT_NAN.matcher(this.currentToken).matches()) {
            this.nextToken();
            return Float.NaN;
         } else {
            try {
               float var1 = Float.parseFloat(this.currentToken);
               this.nextToken();
               return var1;
            } catch (NumberFormatException var4) {
               throw this.floatParseException(var4);
            }
         }
      }

      public String consumeIdentifier() throws TextFormat.ParseException {
         for(int var1 = 0; var1 < this.currentToken.length(); ++var1) {
            char var2 = this.currentToken.charAt(var1);
            if (('a' > var2 || var2 > 'z') && ('A' > var2 || var2 > 'Z') && ('0' > var2 || var2 > '9') && var2 != '_' && var2 != '.') {
               throw this.parseException("Expected identifier.");
            }
         }

         String var3 = this.currentToken;
         this.nextToken();
         return var3;
      }

      public int consumeInt32() throws TextFormat.ParseException {
         try {
            int var1 = TextFormat.parseInt32(this.currentToken);
            this.nextToken();
            return var1;
         } catch (NumberFormatException var3) {
            throw this.integerParseException(var3);
         }
      }

      public long consumeInt64() throws TextFormat.ParseException {
         try {
            long var1 = TextFormat.parseInt64(this.currentToken);
            this.nextToken();
            return var1;
         } catch (NumberFormatException var4) {
            throw this.integerParseException(var4);
         }
      }

      public String consumeString() throws TextFormat.ParseException {
         return this.consumeByteString().toStringUtf8();
      }

      public int consumeUInt32() throws TextFormat.ParseException {
         try {
            int var1 = TextFormat.parseUInt32(this.currentToken);
            this.nextToken();
            return var1;
         } catch (NumberFormatException var3) {
            throw this.integerParseException(var3);
         }
      }

      public long consumeUInt64() throws TextFormat.ParseException {
         try {
            long var1 = TextFormat.parseUInt64(this.currentToken);
            this.nextToken();
            return var1;
         } catch (NumberFormatException var4) {
            throw this.integerParseException(var4);
         }
      }

      public boolean lookingAtInteger() {
         int var1 = this.currentToken.length();
         boolean var2 = false;
         if (var1 == 0) {
            return false;
         } else {
            char var3 = this.currentToken.charAt(0);
            if ('0' <= var3 && var3 <= '9' || var3 == '-' || var3 == '+') {
               var2 = true;
            }

            return var2;
         }
      }

      public void nextToken() {
         this.previousLine = this.line;

         for(this.previousColumn = this.column; this.pos < this.matcher.regionStart(); ++this.pos) {
            if (this.text.charAt(this.pos) == '\n') {
               ++this.line;
               this.column = 0;
            } else {
               ++this.column;
            }
         }

         if (this.matcher.regionStart() == this.matcher.regionEnd()) {
            this.currentToken = "";
         } else {
            this.matcher.usePattern(TOKEN);
            Matcher var1;
            if (this.matcher.lookingAt()) {
               this.currentToken = this.matcher.group();
               var1 = this.matcher;
               var1.region(var1.end(), this.matcher.regionEnd());
            } else {
               this.currentToken = String.valueOf(this.text.charAt(this.pos));
               var1 = this.matcher;
               var1.region(this.pos + 1, var1.regionEnd());
            }

            this.skipWhitespace();
         }
      }

      public TextFormat.ParseException parseException(String var1) {
         return new TextFormat.ParseException(this.line + 1, this.column + 1, var1);
      }

      public TextFormat.ParseException parseExceptionPreviousToken(String var1) {
         return new TextFormat.ParseException(this.previousLine + 1, this.previousColumn + 1, var1);
      }

      public boolean tryConsume(String var1) {
         if (this.currentToken.equals(var1)) {
            this.nextToken();
            return true;
         } else {
            return false;
         }
      }
   }
}
