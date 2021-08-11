/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.UnknownFieldSet;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextFormat {
    private static final int BUFFER_SIZE = 4096;
    private static final Printer DEFAULT_PRINTER = new Printer();
    private static final Printer SINGLE_LINE_PRINTER = Printer.access$100(new Printer(), true);
    private static final Printer UNICODE_PRINTER = Printer.access$200(new Printer(), false);

    private TextFormat() {
    }

    private static int digitValue(byte by) {
        if (48 <= by && by <= 57) {
            return by - 48;
        }
        if (97 <= by && by <= 122) {
            return by - 97 + 10;
        }
        return by - 65 + 10;
    }

    static String escapeBytes(ByteString byteString) {
        StringBuilder stringBuilder = new StringBuilder(byteString.size());
        for (int i = 0; i < byteString.size(); ++i) {
            byte by = byteString.byteAt(i);
            if (by != 34) {
                if (by != 39) {
                    if (by != 92) {
                        switch (by) {
                            default: {
                                if (by >= 32) {
                                    stringBuilder.append((char)by);
                                    break;
                                }
                                stringBuilder.append('\\');
                                stringBuilder.append((char)((by >>> 6 & 3) + 48));
                                stringBuilder.append((char)((by >>> 3 & 7) + 48));
                                stringBuilder.append((char)((by & 7) + 48));
                                break;
                            }
                            case 13: {
                                stringBuilder.append("\\r");
                                break;
                            }
                            case 12: {
                                stringBuilder.append("\\f");
                                break;
                            }
                            case 11: {
                                stringBuilder.append("\\v");
                                break;
                            }
                            case 10: {
                                stringBuilder.append("\\n");
                                break;
                            }
                            case 9: {
                                stringBuilder.append("\\t");
                                break;
                            }
                            case 8: {
                                stringBuilder.append("\\b");
                                break;
                            }
                            case 7: {
                                stringBuilder.append("\\a");
                                break;
                            }
                        }
                        continue;
                    }
                    stringBuilder.append("\\\\");
                    continue;
                }
                stringBuilder.append("\\'");
                continue;
            }
            stringBuilder.append("\\\"");
        }
        return stringBuilder.toString();
    }

    static String escapeText(String string2) {
        return TextFormat.escapeBytes(ByteString.copyFromUtf8(string2));
    }

    private static boolean isHex(byte by) {
        if (48 <= by && by <= 57 || 97 <= by && by <= 102 || 65 <= by && by <= 70) {
            return true;
        }
        return false;
    }

    private static boolean isOctal(byte by) {
        if (48 <= by && by <= 55) {
            return true;
        }
        return false;
    }

    public static void merge(CharSequence object, ExtensionRegistry extensionRegistry, Message.Builder builder) throws ParseException {
        object = new Tokenizer((CharSequence)object);
        while (!object.atEnd()) {
            TextFormat.mergeField((Tokenizer)object, extensionRegistry, builder);
        }
    }

    public static void merge(CharSequence charSequence, Message.Builder builder) throws ParseException {
        TextFormat.merge(charSequence, ExtensionRegistry.getEmptyRegistry(), builder);
    }

    public static void merge(Readable readable, ExtensionRegistry extensionRegistry, Message.Builder builder) throws IOException {
        TextFormat.merge(TextFormat.toStringBuilder(readable), extensionRegistry, builder);
    }

    public static void merge(Readable readable, Message.Builder builder) throws IOException {
        TextFormat.merge(readable, ExtensionRegistry.getEmptyRegistry(), builder);
    }

    private static void mergeField(Tokenizer object, ExtensionRegistry object2, Message.Builder builder) throws ParseException {
        Descriptors.Descriptor descriptor;
        String string2;
        block37 : {
            Object object3;
            Object object4;
            Object object5;
            block36 : {
                Object var8_4;
                block33 : {
                    block34 : {
                        block35 : {
                            descriptor = builder.getDescriptorForType();
                            var8_4 = null;
                            boolean bl = object.tryConsume("[");
                            object4 = null;
                            object5 = null;
                            if (!bl) break block33;
                            object3 = new StringBuilder(object.consumeIdentifier());
                            while (object.tryConsume(".")) {
                                object3.append('.');
                                object3.append(object.consumeIdentifier());
                            }
                            object5 = object2.findExtensionByName(object3.toString());
                            if (object5 == null) break block34;
                            if (object5.descriptor.getContainingType() != descriptor) break block35;
                            object.consume("]");
                            object3 = object5.descriptor;
                            break block36;
                        }
                        object2 = new StringBuilder();
                        object2.append("Extension \"");
                        object2.append(object3);
                        object2.append("\" does not extend message type \"");
                        object2.append(descriptor.getFullName());
                        object2.append("\".");
                        throw object.parseExceptionPreviousToken(object2.toString());
                    }
                    object2 = new StringBuilder();
                    object2.append("Extension \"");
                    object2.append(object3);
                    object2.append("\" not found in the ExtensionRegistry.");
                    throw object.parseExceptionPreviousToken(object2.toString());
                }
                string2 = object.consumeIdentifier();
                object3 = object4 = descriptor.findFieldByName(string2);
                if (object4 == null) {
                    object4 = string2.toLowerCase(Locale.US);
                    Descriptors.FieldDescriptor fieldDescriptor = descriptor.findFieldByName((String)object4);
                    object5 = object4;
                    object3 = fieldDescriptor;
                    if (fieldDescriptor != null) {
                        object5 = object4;
                        object3 = fieldDescriptor;
                        if (fieldDescriptor.getType() != Descriptors.FieldDescriptor.Type.GROUP) {
                            object3 = null;
                            object5 = object4;
                        }
                    }
                }
                object4 = object3;
                if (object3 != null) {
                    object4 = object3;
                    if (object3.getType() == Descriptors.FieldDescriptor.Type.GROUP) {
                        object4 = object3;
                        if (!object3.getMessageType().getName().equals(string2)) {
                            object4 = null;
                        }
                    }
                }
                if (object4 == null) break block37;
                object3 = object4;
                object4 = object5;
                object5 = var8_4;
            }
            object4 = null;
            if (object3.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                object.tryConsume(":");
                if (object.tryConsume("<")) {
                    object4 = ">";
                } else {
                    object.consume("{");
                    object4 = "}";
                }
                object5 = object5 == null ? builder.newBuilderForField((Descriptors.FieldDescriptor)object3) : object5.defaultInstance.newBuilderForType();
                while (!object.tryConsume((String)object4)) {
                    if (!object.atEnd()) {
                        TextFormat.mergeField((Tokenizer)object, (ExtensionRegistry)object2, (Message.Builder)object5);
                        continue;
                    }
                    object2 = new StringBuilder();
                    object2.append("Expected \"");
                    object2.append((String)object4);
                    object2.append("\".");
                    throw object.parseException(object2.toString());
                }
                object = object5.buildPartial();
            } else {
                object.consume(":");
                switch (.$SwitchMap$com$google$protobuf$Descriptors$FieldDescriptor$Type[object3.getType().ordinal()]) {
                    default: {
                        object = object4;
                        break;
                    }
                    case 17: 
                    case 18: {
                        throw new RuntimeException("Can't get here.");
                    }
                    case 16: {
                        object5 = object3.getEnumType();
                        if (object.lookingAtInteger()) {
                            int n = object.consumeInt32();
                            object2 = object5.findValueByNumber(n);
                            if (object2 != null) {
                                object = object2;
                                break;
                            }
                            object2 = new StringBuilder();
                            object2.append("Enum type \"");
                            object2.append(object5.getFullName());
                            object2.append("\" has no value with number ");
                            object2.append(n);
                            object2.append('.');
                            throw object.parseExceptionPreviousToken(object2.toString());
                        }
                        object4 = object.consumeIdentifier();
                        object2 = object5.findValueByName((String)object4);
                        if (object2 != null) {
                            object = object2;
                            break;
                        }
                        object2 = new StringBuilder();
                        object2.append("Enum type \"");
                        object2.append(object5.getFullName());
                        object2.append("\" has no value named \"");
                        object2.append((String)object4);
                        object2.append("\".");
                        throw object.parseExceptionPreviousToken(object2.toString());
                    }
                    case 15: {
                        object = object.consumeByteString();
                        break;
                    }
                    case 14: {
                        object = object.consumeString();
                        break;
                    }
                    case 12: 
                    case 13: {
                        object = object.consumeUInt64();
                        break;
                    }
                    case 10: 
                    case 11: {
                        object = object.consumeUInt32();
                        break;
                    }
                    case 9: {
                        object = object.consumeDouble();
                        break;
                    }
                    case 8: {
                        object = Float.valueOf(object.consumeFloat());
                        break;
                    }
                    case 7: {
                        object = object.consumeBoolean();
                        break;
                    }
                    case 4: 
                    case 5: 
                    case 6: {
                        object = object.consumeInt64();
                        break;
                    }
                    case 1: 
                    case 2: 
                    case 3: {
                        object = object.consumeInt32();
                    }
                }
            }
            if (object3.isRepeated()) {
                builder.addRepeatedField((Descriptors.FieldDescriptor)object3, object);
                return;
            }
            builder.setField((Descriptors.FieldDescriptor)object3, object);
            return;
        }
        object2 = new StringBuilder();
        object2.append("Message type \"");
        object2.append(descriptor.getFullName());
        object2.append("\" has no field named \"");
        object2.append(string2);
        object2.append("\".");
        throw object.parseExceptionPreviousToken(object2.toString());
    }

    static int parseInt32(String string2) throws NumberFormatException {
        return (int)TextFormat.parseInteger(string2, true, false);
    }

    static long parseInt64(String string2) throws NumberFormatException {
        return TextFormat.parseInteger(string2, true, true);
    }

    private static long parseInteger(String string2, boolean bl, boolean bl2) throws NumberFormatException {
        Object object;
        block26 : {
            long l;
            block22 : {
                block24 : {
                    block25 : {
                        block23 : {
                            int n;
                            boolean bl3;
                            block21 : {
                                int n2;
                                int n3 = 0;
                                bl3 = false;
                                if (string2.startsWith("-", 0)) {
                                    if (bl) {
                                        n3 = 0 + 1;
                                        bl3 = true;
                                    } else {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("Number must be positive: ");
                                        stringBuilder.append(string2);
                                        throw new NumberFormatException(stringBuilder.toString());
                                    }
                                }
                                n = 10;
                                if (string2.startsWith("0x", n3)) {
                                    n2 = n3 + 2;
                                    n = 16;
                                } else {
                                    n2 = n3;
                                    if (string2.startsWith("0", n3)) {
                                        n = 8;
                                        n2 = n3;
                                    }
                                }
                                object = string2.substring(n2);
                                if (object.length() >= 16) break block21;
                                long l2 = l = Long.parseLong((String)object, n);
                                if (bl3) {
                                    l2 = - l;
                                }
                                l = l2;
                                if (!bl2) {
                                    if (bl) {
                                        if (l2 <= Integer.MAX_VALUE && l2 >= Integer.MIN_VALUE) {
                                            return l2;
                                        }
                                        object = new StringBuilder();
                                        object.append("Number out of range for 32-bit signed integer: ");
                                        object.append(string2);
                                        throw new NumberFormatException(object.toString());
                                    }
                                    if (l2 < 0x100000000L && l2 >= 0L) {
                                        return l2;
                                    }
                                    object = new StringBuilder();
                                    object.append("Number out of range for 32-bit unsigned integer: ");
                                    object.append(string2);
                                    throw new NumberFormatException(object.toString());
                                }
                                break block22;
                            }
                            BigInteger bigInteger = new BigInteger((String)object, n);
                            object = bigInteger;
                            if (bl3) {
                                object = bigInteger.negate();
                            }
                            if (bl2) break block23;
                            if (bl) {
                                if (object.bitLength() > 31) {
                                    object = new StringBuilder();
                                    object.append("Number out of range for 32-bit signed integer: ");
                                    object.append(string2);
                                    throw new NumberFormatException(object.toString());
                                }
                            } else if (object.bitLength() > 32) {
                                object = new StringBuilder();
                                object.append("Number out of range for 32-bit unsigned integer: ");
                                object.append(string2);
                                throw new NumberFormatException(object.toString());
                            }
                            break block24;
                        }
                        if (!bl) break block25;
                        if (object.bitLength() > 63) {
                            object = new StringBuilder();
                            object.append("Number out of range for 64-bit signed integer: ");
                            object.append(string2);
                            throw new NumberFormatException(object.toString());
                        }
                        break block24;
                    }
                    if (object.bitLength() > 64) break block26;
                }
                l = object.longValue();
            }
            return l;
        }
        object = new StringBuilder();
        object.append("Number out of range for 64-bit unsigned integer: ");
        object.append(string2);
        throw new NumberFormatException(object.toString());
    }

    static int parseUInt32(String string2) throws NumberFormatException {
        return (int)TextFormat.parseInteger(string2, false, false);
    }

    static long parseUInt64(String string2) throws NumberFormatException {
        return TextFormat.parseInteger(string2, false, true);
    }

    public static void print(MessageOrBuilder messageOrBuilder, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.print(messageOrBuilder, new TextGenerator(appendable));
    }

    public static void print(UnknownFieldSet unknownFieldSet, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.printUnknownFields(unknownFieldSet, new TextGenerator(appendable));
    }

    public static void printField(Descriptors.FieldDescriptor fieldDescriptor, Object object, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.printField(fieldDescriptor, object, new TextGenerator(appendable));
    }

    public static String printFieldToString(Descriptors.FieldDescriptor object, Object object2) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            TextFormat.printField((Descriptors.FieldDescriptor)object, object2, stringBuilder);
            object = stringBuilder.toString();
            return object;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }

    public static void printFieldValue(Descriptors.FieldDescriptor fieldDescriptor, Object object, Appendable appendable) throws IOException {
        DEFAULT_PRINTER.printFieldValue(fieldDescriptor, object, new TextGenerator(appendable));
    }

    public static String printToString(MessageOrBuilder object) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            TextFormat.print((MessageOrBuilder)object, (Appendable)stringBuilder);
            object = stringBuilder.toString();
            return object;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }

    public static String printToString(UnknownFieldSet object) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            TextFormat.print((UnknownFieldSet)object, (Appendable)stringBuilder);
            object = stringBuilder.toString();
            return object;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }

    public static String printToUnicodeString(MessageOrBuilder object) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            UNICODE_PRINTER.print((MessageOrBuilder)object, new TextGenerator(stringBuilder));
            object = stringBuilder.toString();
            return object;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }

    public static String printToUnicodeString(UnknownFieldSet object) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            UNICODE_PRINTER.printUnknownFields((UnknownFieldSet)object, new TextGenerator(stringBuilder));
            object = stringBuilder.toString();
            return object;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }

    private static void printUnknownFieldValue(int n, Object object, TextGenerator textGenerator) throws IOException {
        int n2 = WireFormat.getTagWireType(n);
        if (n2 != 0) {
            if (n2 != 1) {
                if (n2 != 2) {
                    if (n2 != 3) {
                        if (n2 == 5) {
                            textGenerator.print(String.format((Locale)null, "0x%08x", (Integer)object));
                            return;
                        }
                        object = new StringBuilder();
                        object.append("Bad tag: ");
                        object.append(n);
                        throw new IllegalArgumentException(object.toString());
                    }
                    DEFAULT_PRINTER.printUnknownFields((UnknownFieldSet)object, textGenerator);
                    return;
                }
                textGenerator.print("\"");
                textGenerator.print(TextFormat.escapeBytes((ByteString)object));
                textGenerator.print("\"");
                return;
            }
            textGenerator.print(String.format((Locale)null, "0x%016x", (Long)object));
            return;
        }
        textGenerator.print(TextFormat.unsignedToString((Long)object));
    }

    public static void printUnknownFieldValue(int n, Object object, Appendable appendable) throws IOException {
        TextFormat.printUnknownFieldValue(n, object, new TextGenerator(appendable));
    }

    public static String shortDebugString(MessageOrBuilder object) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            SINGLE_LINE_PRINTER.print((MessageOrBuilder)object, new TextGenerator(stringBuilder));
            object = stringBuilder.toString().trim();
            return object;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }

    public static String shortDebugString(UnknownFieldSet object) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            SINGLE_LINE_PRINTER.printUnknownFields((UnknownFieldSet)object, new TextGenerator(stringBuilder));
            object = stringBuilder.toString().trim();
            return object;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
    }

    private static StringBuilder toStringBuilder(Readable readable) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        CharBuffer charBuffer = CharBuffer.allocate(4096);
        int n;
        while ((n = readable.read(charBuffer)) != -1) {
            charBuffer.flip();
            stringBuilder.append(charBuffer, 0, n);
        }
        return stringBuilder;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    static ByteString unescapeBytes(CharSequence object) throws InvalidEscapeSequenceException {
        object = ByteString.copyFromUtf8(object.toString());
        byte[] arrby = new byte[object.size()];
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (n2 < object.size()) {
            int n4;
            int n5;
            byte by = object.byteAt(n2);
            if (by == 92) {
                if (n2 + 1 >= object.size()) throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\' at end of string.");
                n5 = n2 + 1;
                by = object.byteAt(n5);
                if (TextFormat.isOctal(by)) {
                    n3 = TextFormat.digitValue(by);
                    n4 = n5;
                    n2 = n3;
                    if (n5 + 1 < object.size()) {
                        n4 = n5;
                        n2 = n3;
                        if (TextFormat.isOctal(object.byteAt(n5 + 1))) {
                            n4 = n5 + 1;
                            n2 = n3 * 8 + TextFormat.digitValue(object.byteAt(n4));
                        }
                    }
                    n5 = n4;
                    n3 = n2;
                    if (n4 + 1 < object.size()) {
                        n5 = n4;
                        n3 = n2;
                        if (TextFormat.isOctal(object.byteAt(n4 + 1))) {
                            n5 = n4 + 1;
                            n3 = n2 * 8 + TextFormat.digitValue(object.byteAt(n5));
                        }
                    }
                    arrby[n] = (byte)n3;
                    n2 = n + 1;
                    n4 = n5;
                } else if (by != 34) {
                    if (by != 39) {
                        if (by != 92) {
                            if (by != 102) {
                                if (by != 110) {
                                    if (by != 114) {
                                        if (by != 116) {
                                            if (by != 118) {
                                                if (by != 120) {
                                                    if (by != 97) {
                                                        if (by != 98) {
                                                            object = new StringBuilder();
                                                            object.append("Invalid escape sequence: '\\");
                                                            object.append((char)by);
                                                            object.append('\'');
                                                            throw new InvalidEscapeSequenceException(object.toString());
                                                        }
                                                        n2 = n + 1;
                                                        arrby[n] = 8;
                                                        n4 = n5;
                                                    } else {
                                                        n2 = n + 1;
                                                        arrby[n] = 7;
                                                        n4 = n5;
                                                    }
                                                } else {
                                                    if (n5 + 1 >= object.size()) throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\x' with no digits");
                                                    if (!TextFormat.isHex(object.byteAt(n5 + 1))) throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\x' with no digits");
                                                    n2 = TextFormat.digitValue(object.byteAt(++n5));
                                                    n4 = n5;
                                                    n3 = n2;
                                                    if (n5 + 1 < object.size()) {
                                                        n4 = n5;
                                                        n3 = n2;
                                                        if (TextFormat.isHex(object.byteAt(n5 + 1))) {
                                                            n4 = n5 + 1;
                                                            n3 = n2 * 16 + TextFormat.digitValue(object.byteAt(n4));
                                                        }
                                                    }
                                                    n2 = n + 1;
                                                    arrby[n] = (byte)n3;
                                                }
                                            } else {
                                                n2 = n + 1;
                                                arrby[n] = 11;
                                                n4 = n5;
                                            }
                                        } else {
                                            n2 = n + 1;
                                            arrby[n] = 9;
                                            n4 = n5;
                                        }
                                    } else {
                                        n2 = n + 1;
                                        arrby[n] = 13;
                                        n4 = n5;
                                    }
                                } else {
                                    n2 = n + 1;
                                    arrby[n] = 10;
                                    n4 = n5;
                                }
                            } else {
                                n2 = n + 1;
                                arrby[n] = 12;
                                n4 = n5;
                            }
                        } else {
                            arrby[n] = 92;
                            n2 = n + 1;
                            n4 = n5;
                        }
                    } else {
                        n2 = n + 1;
                        arrby[n] = 39;
                        n4 = n5;
                    }
                } else {
                    n2 = n + 1;
                    arrby[n] = 34;
                    n4 = n5;
                }
            } else {
                arrby[n] = by;
                n5 = n + 1;
                n4 = n2;
                n2 = n5;
            }
            n = n2;
            n2 = ++n4;
        }
        return ByteString.copyFrom(arrby, 0, n);
    }

    static String unescapeText(String string2) throws InvalidEscapeSequenceException {
        return TextFormat.unescapeBytes(string2).toStringUtf8();
    }

    private static String unsignedToString(int n) {
        if (n >= 0) {
            return Integer.toString(n);
        }
        return Long.toString((long)n & 0xFFFFFFFFL);
    }

    private static String unsignedToString(long l) {
        if (l >= 0L) {
            return Long.toString(l);
        }
        return BigInteger.valueOf(Long.MAX_VALUE & l).setBit(63).toString();
    }

    static class InvalidEscapeSequenceException
    extends IOException {
        private static final long serialVersionUID = -8164033650142593304L;

        InvalidEscapeSequenceException(String string2) {
            super(string2);
        }
    }

    public static class ParseException
    extends IOException {
        private static final long serialVersionUID = 3196188060225107702L;
        private final int column;
        private final int line;

        public ParseException(int n, int n2, String string2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Integer.toString(n));
            stringBuilder.append(":");
            stringBuilder.append(n2);
            stringBuilder.append(": ");
            stringBuilder.append(string2);
            super(stringBuilder.toString());
            this.line = n;
            this.column = n2;
        }

        public ParseException(String string2) {
            this(-1, -1, string2);
        }

        public int getColumn() {
            return this.column;
        }

        public int getLine() {
            return this.line;
        }
    }

    private static final class Printer {
        boolean escapeNonAscii = true;
        boolean singleLineMode = false;

        private Printer() {
        }

        static /* synthetic */ Printer access$100(Printer printer, boolean bl) {
            return printer.setSingleLineMode(bl);
        }

        static /* synthetic */ Printer access$200(Printer printer, boolean bl) {
            return printer.setEscapeNonAscii(bl);
        }

        private void print(MessageOrBuilder messageOrBuilder, TextGenerator textGenerator) throws IOException {
            for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : messageOrBuilder.getAllFields().entrySet()) {
                this.printField(entry.getKey(), entry.getValue(), textGenerator);
            }
            this.printUnknownFields(messageOrBuilder.getUnknownFields(), textGenerator);
        }

        private void printField(Descriptors.FieldDescriptor fieldDescriptor, Object iterator, TextGenerator textGenerator) throws IOException {
            if (fieldDescriptor.isRepeated()) {
                iterator = ((List)((Object)iterator)).iterator();
                while (iterator.hasNext()) {
                    this.printSingleField(fieldDescriptor, iterator.next(), textGenerator);
                }
                return;
            }
            this.printSingleField(fieldDescriptor, iterator, textGenerator);
        }

        private void printFieldValue(Descriptors.FieldDescriptor object, Object object2, TextGenerator textGenerator) throws IOException {
            switch (.$SwitchMap$com$google$protobuf$Descriptors$FieldDescriptor$Type[object.getType().ordinal()]) {
                default: {
                    return;
                }
                case 17: 
                case 18: {
                    this.print((Message)object2, textGenerator);
                    return;
                }
                case 16: {
                    textGenerator.print(((Descriptors.EnumValueDescriptor)object2).getName());
                    return;
                }
                case 15: {
                    textGenerator.print("\"");
                    textGenerator.print(TextFormat.escapeBytes((ByteString)object2));
                    textGenerator.print("\"");
                    return;
                }
                case 14: {
                    textGenerator.print("\"");
                    object = this.escapeNonAscii ? TextFormat.escapeText((String)object2) : (String)object2;
                    textGenerator.print((CharSequence)object);
                    textGenerator.print("\"");
                    return;
                }
                case 12: 
                case 13: {
                    textGenerator.print(TextFormat.unsignedToString((Long)object2));
                    return;
                }
                case 10: 
                case 11: {
                    textGenerator.print(TextFormat.unsignedToString((Integer)object2));
                    return;
                }
                case 9: {
                    textGenerator.print(((Double)object2).toString());
                    return;
                }
                case 8: {
                    textGenerator.print(((Float)object2).toString());
                    return;
                }
                case 7: {
                    textGenerator.print(((Boolean)object2).toString());
                    return;
                }
                case 4: 
                case 5: 
                case 6: {
                    textGenerator.print(((Long)object2).toString());
                    return;
                }
                case 1: 
                case 2: 
                case 3: 
            }
            textGenerator.print(((Integer)object2).toString());
        }

        private void printSingleField(Descriptors.FieldDescriptor fieldDescriptor, Object object, TextGenerator textGenerator) throws IOException {
            if (fieldDescriptor.isExtension()) {
                textGenerator.print("[");
                if (fieldDescriptor.getContainingType().getOptions().getMessageSetWireFormat() && fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.MESSAGE && fieldDescriptor.isOptional() && fieldDescriptor.getExtensionScope() == fieldDescriptor.getMessageType()) {
                    textGenerator.print(fieldDescriptor.getMessageType().getFullName());
                } else {
                    textGenerator.print(fieldDescriptor.getFullName());
                }
                textGenerator.print("]");
            } else if (fieldDescriptor.getType() == Descriptors.FieldDescriptor.Type.GROUP) {
                textGenerator.print(fieldDescriptor.getMessageType().getName());
            } else {
                textGenerator.print(fieldDescriptor.getName());
            }
            if (fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                if (this.singleLineMode) {
                    textGenerator.print(" { ");
                } else {
                    textGenerator.print(" {\n");
                    textGenerator.indent();
                }
            } else {
                textGenerator.print(": ");
            }
            this.printFieldValue(fieldDescriptor, object, textGenerator);
            if (fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                if (this.singleLineMode) {
                    textGenerator.print("} ");
                    return;
                }
                textGenerator.outdent();
                textGenerator.print("}\n");
                return;
            }
            if (this.singleLineMode) {
                textGenerator.print(" ");
                return;
            }
            textGenerator.print("\n");
        }

        private void printUnknownField(int n, int n2, List<?> object, TextGenerator textGenerator) throws IOException {
            Iterator iterator = object.iterator();
            while (iterator.hasNext()) {
                object = iterator.next();
                textGenerator.print(String.valueOf(n));
                textGenerator.print(": ");
                TextFormat.printUnknownFieldValue(n2, object, textGenerator);
                object = this.singleLineMode ? " " : "\n";
                textGenerator.print((CharSequence)object);
            }
        }

        private void printUnknownFields(UnknownFieldSet object, TextGenerator textGenerator) throws IOException {
            for (Map.Entry<Integer, UnknownFieldSet.Field> entry : object.asMap().entrySet()) {
                int n = entry.getKey();
                UnknownFieldSet.Field field = entry.getValue();
                this.printUnknownField(n, 0, field.getVarintList(), textGenerator);
                this.printUnknownField(n, 5, field.getFixed32List(), textGenerator);
                this.printUnknownField(n, 1, field.getFixed64List(), textGenerator);
                this.printUnknownField(n, 2, field.getLengthDelimitedList(), textGenerator);
                for (UnknownFieldSet unknownFieldSet : field.getGroupList()) {
                    textGenerator.print(entry.getKey().toString());
                    if (this.singleLineMode) {
                        textGenerator.print(" { ");
                    } else {
                        textGenerator.print(" {\n");
                        textGenerator.indent();
                    }
                    this.printUnknownFields(unknownFieldSet, textGenerator);
                    if (this.singleLineMode) {
                        textGenerator.print("} ");
                        continue;
                    }
                    textGenerator.outdent();
                    textGenerator.print("}\n");
                }
            }
        }

        private Printer setEscapeNonAscii(boolean bl) {
            this.escapeNonAscii = bl;
            return this;
        }

        private Printer setSingleLineMode(boolean bl) {
            this.singleLineMode = bl;
            return this;
        }
    }

    private static final class TextGenerator {
        private boolean atStartOfLine = true;
        private final StringBuilder indent = new StringBuilder();
        private final Appendable output;

        private TextGenerator(Appendable appendable) {
            this.output = appendable;
        }

        private void write(CharSequence charSequence, int n) throws IOException {
            if (n == 0) {
                return;
            }
            if (this.atStartOfLine) {
                this.atStartOfLine = false;
                this.output.append(this.indent);
            }
            this.output.append(charSequence);
        }

        public void indent() {
            this.indent.append("  ");
        }

        public void outdent() {
            int n = this.indent.length();
            if (n != 0) {
                this.indent.delete(n - 2, n);
                return;
            }
            throw new IllegalArgumentException(" Outdent() without matching Indent().");
        }

        public void print(CharSequence charSequence) throws IOException {
            int n = charSequence.length();
            int n2 = 0;
            for (int i = 0; i < n; ++i) {
                int n3 = n2;
                if (charSequence.charAt(i) == '\n') {
                    this.write(charSequence.subSequence(n2, n), i - n2 + 1);
                    n3 = i + 1;
                    this.atStartOfLine = true;
                }
                n2 = n3;
            }
            this.write(charSequence.subSequence(n2, n), n - n2);
        }
    }

    private static final class Tokenizer {
        private static final Pattern DOUBLE_INFINITY;
        private static final Pattern FLOAT_INFINITY;
        private static final Pattern FLOAT_NAN;
        private static final Pattern TOKEN;
        private static final Pattern WHITESPACE;
        private int column = 0;
        private String currentToken;
        private int line = 0;
        private final Matcher matcher;
        private int pos = 0;
        private int previousColumn = 0;
        private int previousLine = 0;
        private final CharSequence text;

        static {
            WHITESPACE = Pattern.compile("(\\s|(#.*$))++", 8);
            TOKEN = Pattern.compile("[a-zA-Z_][0-9a-zA-Z_+-]*+|[.]?[0-9+-][0-9a-zA-Z_.+-]*+|\"([^\"\n\\\\]|\\\\.)*+(\"|\\\\?$)|'([^'\n\\\\]|\\\\.)*+('|\\\\?$)", 8);
            DOUBLE_INFINITY = Pattern.compile("-?inf(inity)?", 2);
            FLOAT_INFINITY = Pattern.compile("-?inf(inity)?f?", 2);
            FLOAT_NAN = Pattern.compile("nanf?", 2);
        }

        private Tokenizer(CharSequence charSequence) {
            this.text = charSequence;
            this.matcher = WHITESPACE.matcher(charSequence);
            this.skipWhitespace();
            this.nextToken();
        }

        private void consumeByteString(List<ByteString> list) throws ParseException {
            Object object;
            int n = this.currentToken.length();
            char c = '\u0000';
            if (n > 0) {
                c = this.currentToken.charAt(0);
            }
            if (c != '\"' && c != '\'') {
                throw this.parseException("Expected string.");
            }
            if (this.currentToken.length() >= 2 && (object = this.currentToken).charAt(object.length() - 1) == c) {
                try {
                    object = TextFormat.unescapeBytes(this.currentToken.substring(1, this.currentToken.length() - 1));
                    this.nextToken();
                    list.add((ByteString)object);
                    return;
                }
                catch (InvalidEscapeSequenceException invalidEscapeSequenceException) {
                    throw this.parseException(invalidEscapeSequenceException.getMessage());
                }
            }
            throw this.parseException("String missing ending quote.");
        }

        private ParseException floatParseException(NumberFormatException numberFormatException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't parse number: ");
            stringBuilder.append(numberFormatException.getMessage());
            return this.parseException(stringBuilder.toString());
        }

        private ParseException integerParseException(NumberFormatException numberFormatException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't parse integer: ");
            stringBuilder.append(numberFormatException.getMessage());
            return this.parseException(stringBuilder.toString());
        }

        private void skipWhitespace() {
            this.matcher.usePattern(WHITESPACE);
            if (this.matcher.lookingAt()) {
                Matcher matcher = this.matcher;
                matcher.region(matcher.end(), this.matcher.regionEnd());
            }
        }

        public boolean atEnd() {
            if (this.currentToken.length() == 0) {
                return true;
            }
            return false;
        }

        public void consume(String string2) throws ParseException {
            if (this.tryConsume(string2)) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected \"");
            stringBuilder.append(string2);
            stringBuilder.append("\".");
            throw this.parseException(stringBuilder.toString());
        }

        public boolean consumeBoolean() throws ParseException {
            if (!(this.currentToken.equals("true") || this.currentToken.equals("t") || this.currentToken.equals("1"))) {
                if (!(this.currentToken.equals("false") || this.currentToken.equals("f") || this.currentToken.equals("0"))) {
                    throw this.parseException("Expected \"true\" or \"false\".");
                }
                this.nextToken();
                return false;
            }
            this.nextToken();
            return true;
        }

        public ByteString consumeByteString() throws ParseException {
            ArrayList<ByteString> arrayList = new ArrayList<ByteString>();
            this.consumeByteString(arrayList);
            while (this.currentToken.startsWith("'") || this.currentToken.startsWith("\"")) {
                this.consumeByteString(arrayList);
            }
            return ByteString.copyFrom(arrayList);
        }

        public double consumeDouble() throws ParseException {
            if (DOUBLE_INFINITY.matcher(this.currentToken).matches()) {
                boolean bl = this.currentToken.startsWith("-");
                this.nextToken();
                if (bl) {
                    return Double.NEGATIVE_INFINITY;
                }
                return Double.POSITIVE_INFINITY;
            }
            if (this.currentToken.equalsIgnoreCase("nan")) {
                this.nextToken();
                return Double.NaN;
            }
            try {
                double d = Double.parseDouble(this.currentToken);
                this.nextToken();
                return d;
            }
            catch (NumberFormatException numberFormatException) {
                throw this.floatParseException(numberFormatException);
            }
        }

        public float consumeFloat() throws ParseException {
            if (FLOAT_INFINITY.matcher(this.currentToken).matches()) {
                boolean bl = this.currentToken.startsWith("-");
                this.nextToken();
                if (bl) {
                    return Float.NEGATIVE_INFINITY;
                }
                return Float.POSITIVE_INFINITY;
            }
            if (FLOAT_NAN.matcher(this.currentToken).matches()) {
                this.nextToken();
                return Float.NaN;
            }
            try {
                float f = Float.parseFloat(this.currentToken);
                this.nextToken();
                return f;
            }
            catch (NumberFormatException numberFormatException) {
                throw this.floatParseException(numberFormatException);
            }
        }

        public String consumeIdentifier() throws ParseException {
            for (int i = 0; i < this.currentToken.length(); ++i) {
                char c = this.currentToken.charAt(i);
                if ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || '0' <= c && c <= '9' || c == '_' || c == '.') continue;
                throw this.parseException("Expected identifier.");
            }
            String string2 = this.currentToken;
            this.nextToken();
            return string2;
        }

        public int consumeInt32() throws ParseException {
            try {
                int n = TextFormat.parseInt32(this.currentToken);
                this.nextToken();
                return n;
            }
            catch (NumberFormatException numberFormatException) {
                throw this.integerParseException(numberFormatException);
            }
        }

        public long consumeInt64() throws ParseException {
            try {
                long l = TextFormat.parseInt64(this.currentToken);
                this.nextToken();
                return l;
            }
            catch (NumberFormatException numberFormatException) {
                throw this.integerParseException(numberFormatException);
            }
        }

        public String consumeString() throws ParseException {
            return this.consumeByteString().toStringUtf8();
        }

        public int consumeUInt32() throws ParseException {
            try {
                int n = TextFormat.parseUInt32(this.currentToken);
                this.nextToken();
                return n;
            }
            catch (NumberFormatException numberFormatException) {
                throw this.integerParseException(numberFormatException);
            }
        }

        public long consumeUInt64() throws ParseException {
            try {
                long l = TextFormat.parseUInt64(this.currentToken);
                this.nextToken();
                return l;
            }
            catch (NumberFormatException numberFormatException) {
                throw this.integerParseException(numberFormatException);
            }
        }

        public boolean lookingAtInteger() {
            int n = this.currentToken.length();
            boolean bl = false;
            if (n == 0) {
                return false;
            }
            n = this.currentToken.charAt(0);
            if (48 <= n && n <= 57 || n == 45 || n == 43) {
                bl = true;
            }
            return bl;
        }

        public void nextToken() {
            this.previousLine = this.line;
            this.previousColumn = this.column;
            while (this.pos < this.matcher.regionStart()) {
                if (this.text.charAt(this.pos) == '\n') {
                    ++this.line;
                    this.column = 0;
                } else {
                    ++this.column;
                }
                ++this.pos;
            }
            if (this.matcher.regionStart() == this.matcher.regionEnd()) {
                this.currentToken = "";
                return;
            }
            this.matcher.usePattern(TOKEN);
            if (this.matcher.lookingAt()) {
                this.currentToken = this.matcher.group();
                Matcher matcher = this.matcher;
                matcher.region(matcher.end(), this.matcher.regionEnd());
            } else {
                this.currentToken = String.valueOf(this.text.charAt(this.pos));
                Matcher matcher = this.matcher;
                matcher.region(this.pos + 1, matcher.regionEnd());
            }
            this.skipWhitespace();
        }

        public ParseException parseException(String string2) {
            return new ParseException(this.line + 1, this.column + 1, string2);
        }

        public ParseException parseExceptionPreviousToken(String string2) {
            return new ParseException(this.previousLine + 1, this.previousColumn + 1, string2);
        }

        public boolean tryConsume(String string2) {
            if (this.currentToken.equals(string2)) {
                this.nextToken();
                return true;
            }
            return false;
        }
    }

}

