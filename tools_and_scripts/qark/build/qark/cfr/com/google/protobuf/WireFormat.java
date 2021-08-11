/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.ByteString;

public final class WireFormat {
    static final int MESSAGE_SET_ITEM = 1;
    static final int MESSAGE_SET_ITEM_END_TAG;
    static final int MESSAGE_SET_ITEM_TAG;
    static final int MESSAGE_SET_MESSAGE = 3;
    static final int MESSAGE_SET_MESSAGE_TAG;
    static final int MESSAGE_SET_TYPE_ID = 2;
    static final int MESSAGE_SET_TYPE_ID_TAG;
    static final int TAG_TYPE_BITS = 3;
    static final int TAG_TYPE_MASK = 7;
    public static final int WIRETYPE_END_GROUP = 4;
    public static final int WIRETYPE_FIXED32 = 5;
    public static final int WIRETYPE_FIXED64 = 1;
    public static final int WIRETYPE_LENGTH_DELIMITED = 2;
    public static final int WIRETYPE_START_GROUP = 3;
    public static final int WIRETYPE_VARINT = 0;

    static {
        MESSAGE_SET_ITEM_TAG = WireFormat.makeTag(1, 3);
        MESSAGE_SET_ITEM_END_TAG = WireFormat.makeTag(1, 4);
        MESSAGE_SET_TYPE_ID_TAG = WireFormat.makeTag(2, 0);
        MESSAGE_SET_MESSAGE_TAG = WireFormat.makeTag(3, 2);
    }

    private WireFormat() {
    }

    public static int getTagFieldNumber(int n) {
        return n >>> 3;
    }

    static int getTagWireType(int n) {
        return n & 7;
    }

    static int makeTag(int n, int n2) {
        return n << 3 | n2;
    }

    public static class FieldType
    extends Enum<FieldType> {
        private static final /* synthetic */ FieldType[] $VALUES;
        public static final /* enum */ FieldType BOOL;
        public static final /* enum */ FieldType BYTES;
        public static final /* enum */ FieldType DOUBLE;
        public static final /* enum */ FieldType ENUM;
        public static final /* enum */ FieldType FIXED32;
        public static final /* enum */ FieldType FIXED64;
        public static final /* enum */ FieldType FLOAT;
        public static final /* enum */ FieldType GROUP;
        public static final /* enum */ FieldType INT32;
        public static final /* enum */ FieldType INT64;
        public static final /* enum */ FieldType MESSAGE;
        public static final /* enum */ FieldType SFIXED32;
        public static final /* enum */ FieldType SFIXED64;
        public static final /* enum */ FieldType SINT32;
        public static final /* enum */ FieldType SINT64;
        public static final /* enum */ FieldType STRING;
        public static final /* enum */ FieldType UINT32;
        public static final /* enum */ FieldType UINT64;
        private final JavaType javaType;
        private final int wireType;

        static {
            FieldType fieldType;
            DOUBLE = new FieldType(JavaType.DOUBLE, 1);
            FLOAT = new FieldType(JavaType.FLOAT, 5);
            INT64 = new FieldType(JavaType.LONG, 0);
            UINT64 = new FieldType(JavaType.LONG, 0);
            INT32 = new FieldType(JavaType.INT, 0);
            FIXED64 = new FieldType(JavaType.LONG, 1);
            FIXED32 = new FieldType(JavaType.INT, 5);
            BOOL = new FieldType(JavaType.BOOLEAN, 0);
            STRING = new FieldType("STRING", 8, JavaType.STRING, 2){

                @Override
                public boolean isPackable() {
                    return false;
                }
            };
            GROUP = new FieldType("GROUP", 9, JavaType.MESSAGE, 3){

                @Override
                public boolean isPackable() {
                    return false;
                }
            };
            MESSAGE = new FieldType("MESSAGE", 10, JavaType.MESSAGE, 2){

                @Override
                public boolean isPackable() {
                    return false;
                }
            };
            BYTES = new FieldType("BYTES", 11, JavaType.BYTE_STRING, 2){

                @Override
                public boolean isPackable() {
                    return false;
                }
            };
            UINT32 = new FieldType(JavaType.INT, 0);
            ENUM = new FieldType(JavaType.ENUM, 0);
            SFIXED32 = new FieldType(JavaType.INT, 5);
            SFIXED64 = new FieldType(JavaType.LONG, 1);
            SINT32 = new FieldType(JavaType.INT, 0);
            SINT64 = fieldType = new FieldType(JavaType.LONG, 0);
            $VALUES = new FieldType[]{DOUBLE, FLOAT, INT64, UINT64, INT32, FIXED64, FIXED32, BOOL, STRING, GROUP, MESSAGE, BYTES, UINT32, ENUM, SFIXED32, SFIXED64, SINT32, fieldType};
        }

        private FieldType(JavaType javaType, int n2) {
            this.javaType = javaType;
            this.wireType = n2;
        }

        public static FieldType valueOf(String string2) {
            return Enum.valueOf(FieldType.class, string2);
        }

        public static FieldType[] values() {
            return (FieldType[])$VALUES.clone();
        }

        public JavaType getJavaType() {
            return this.javaType;
        }

        public int getWireType() {
            return this.wireType;
        }

        public boolean isPackable() {
            return true;
        }

    }

    public static final class JavaType
    extends Enum<JavaType> {
        private static final /* synthetic */ JavaType[] $VALUES;
        public static final /* enum */ JavaType BOOLEAN;
        public static final /* enum */ JavaType BYTE_STRING;
        public static final /* enum */ JavaType DOUBLE;
        public static final /* enum */ JavaType ENUM;
        public static final /* enum */ JavaType FLOAT;
        public static final /* enum */ JavaType INT;
        public static final /* enum */ JavaType LONG;
        public static final /* enum */ JavaType MESSAGE;
        public static final /* enum */ JavaType STRING;
        private final Object defaultDefault;

        static {
            JavaType javaType;
            INT = new JavaType(0);
            LONG = new JavaType(0L);
            FLOAT = new JavaType(Float.valueOf(0.0f));
            DOUBLE = new JavaType(0.0);
            BOOLEAN = new JavaType(false);
            STRING = new JavaType("");
            BYTE_STRING = new JavaType(ByteString.EMPTY);
            ENUM = new JavaType(null);
            MESSAGE = javaType = new JavaType(null);
            $VALUES = new JavaType[]{INT, LONG, FLOAT, DOUBLE, BOOLEAN, STRING, BYTE_STRING, ENUM, javaType};
        }

        private JavaType(Object object) {
            this.defaultDefault = object;
        }

        public static JavaType valueOf(String string2) {
            return Enum.valueOf(JavaType.class, string2);
        }

        public static JavaType[] values() {
            return (JavaType[])$VALUES.clone();
        }

        Object getDefaultDefault() {
            return this.defaultDefault;
        }
    }

}

