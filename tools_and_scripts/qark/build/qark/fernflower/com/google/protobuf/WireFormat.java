package com.google.protobuf;

public final class WireFormat {
   static final int MESSAGE_SET_ITEM = 1;
   static final int MESSAGE_SET_ITEM_END_TAG = makeTag(1, 4);
   static final int MESSAGE_SET_ITEM_TAG = makeTag(1, 3);
   static final int MESSAGE_SET_MESSAGE = 3;
   static final int MESSAGE_SET_MESSAGE_TAG = makeTag(3, 2);
   static final int MESSAGE_SET_TYPE_ID = 2;
   static final int MESSAGE_SET_TYPE_ID_TAG = makeTag(2, 0);
   static final int TAG_TYPE_BITS = 3;
   static final int TAG_TYPE_MASK = 7;
   public static final int WIRETYPE_END_GROUP = 4;
   public static final int WIRETYPE_FIXED32 = 5;
   public static final int WIRETYPE_FIXED64 = 1;
   public static final int WIRETYPE_LENGTH_DELIMITED = 2;
   public static final int WIRETYPE_START_GROUP = 3;
   public static final int WIRETYPE_VARINT = 0;

   private WireFormat() {
   }

   public static int getTagFieldNumber(int var0) {
      return var0 >>> 3;
   }

   static int getTagWireType(int var0) {
      return var0 & 7;
   }

   static int makeTag(int var0, int var1) {
      return var0 << 3 | var1;
   }

   public static enum FieldType {
      BOOL(WireFormat.JavaType.BOOLEAN, 0),
      BYTES(WireFormat.JavaType.BYTE_STRING, 2) {
         public boolean isPackable() {
            return false;
         }
      },
      DOUBLE(WireFormat.JavaType.DOUBLE, 1),
      ENUM(WireFormat.JavaType.ENUM, 0),
      FIXED32(WireFormat.JavaType.INT, 5),
      FIXED64(WireFormat.JavaType.LONG, 1),
      FLOAT(WireFormat.JavaType.FLOAT, 5),
      GROUP(WireFormat.JavaType.MESSAGE, 3) {
         public boolean isPackable() {
            return false;
         }
      },
      INT32(WireFormat.JavaType.INT, 0),
      INT64(WireFormat.JavaType.LONG, 0),
      MESSAGE(WireFormat.JavaType.MESSAGE, 2) {
         public boolean isPackable() {
            return false;
         }
      },
      SFIXED32(WireFormat.JavaType.INT, 5),
      SFIXED64(WireFormat.JavaType.LONG, 1),
      SINT32(WireFormat.JavaType.INT, 0),
      SINT64,
      STRING(WireFormat.JavaType.STRING, 2) {
         public boolean isPackable() {
            return false;
         }
      },
      UINT32(WireFormat.JavaType.INT, 0),
      UINT64(WireFormat.JavaType.LONG, 0);

      private final WireFormat.JavaType javaType;
      private final int wireType;

      static {
         WireFormat.FieldType var0 = new WireFormat.FieldType("SINT64", 17, WireFormat.JavaType.LONG, 0);
         SINT64 = var0;
      }

      private FieldType(WireFormat.JavaType var3, int var4) {
         this.javaType = var3;
         this.wireType = var4;
      }

      // $FF: synthetic method
      FieldType(WireFormat.JavaType var3, int var4, Object var5) {
         this(var3, var4);
      }

      public WireFormat.JavaType getJavaType() {
         return this.javaType;
      }

      public int getWireType() {
         return this.wireType;
      }

      public boolean isPackable() {
         return true;
      }
   }

   public static enum JavaType {
      BOOLEAN(false),
      BYTE_STRING(ByteString.EMPTY),
      DOUBLE(0.0D),
      ENUM((Object)null),
      FLOAT(0.0F),
      INT(0),
      LONG(0L),
      MESSAGE,
      STRING("");

      private final Object defaultDefault;

      static {
         WireFormat.JavaType var0 = new WireFormat.JavaType("MESSAGE", 8, (Object)null);
         MESSAGE = var0;
      }

      private JavaType(Object var3) {
         this.defaultDefault = var3;
      }

      Object getDefaultDefault() {
         return this.defaultDefault;
      }
   }
}
