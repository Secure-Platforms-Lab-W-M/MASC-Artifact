package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class CodedOutputStream {
   public static final int DEFAULT_BUFFER_SIZE = 4096;
   public static final int LITTLE_ENDIAN_32_SIZE = 4;
   public static final int LITTLE_ENDIAN_64_SIZE = 8;
   private final byte[] buffer;
   private final int limit;
   private final OutputStream output;
   private int position;

   private CodedOutputStream(OutputStream var1, byte[] var2) {
      this.output = var1;
      this.buffer = var2;
      this.position = 0;
      this.limit = var2.length;
   }

   private CodedOutputStream(byte[] var1, int var2, int var3) {
      this.output = null;
      this.buffer = var1;
      this.position = var2;
      this.limit = var2 + var3;
   }

   public static int computeBoolSize(int var0, boolean var1) {
      return computeTagSize(var0) + computeBoolSizeNoTag(var1);
   }

   public static int computeBoolSizeNoTag(boolean var0) {
      return 1;
   }

   public static int computeBytesSize(int var0, ByteString var1) {
      return computeTagSize(var0) + computeBytesSizeNoTag(var1);
   }

   public static int computeBytesSizeNoTag(ByteString var0) {
      return computeRawVarint32Size(var0.size()) + var0.size();
   }

   public static int computeDoubleSize(int var0, double var1) {
      return computeTagSize(var0) + computeDoubleSizeNoTag(var1);
   }

   public static int computeDoubleSizeNoTag(double var0) {
      return 8;
   }

   public static int computeEnumSize(int var0, int var1) {
      return computeTagSize(var0) + computeEnumSizeNoTag(var1);
   }

   public static int computeEnumSizeNoTag(int var0) {
      return computeInt32SizeNoTag(var0);
   }

   public static int computeFixed32Size(int var0, int var1) {
      return computeTagSize(var0) + computeFixed32SizeNoTag(var1);
   }

   public static int computeFixed32SizeNoTag(int var0) {
      return 4;
   }

   public static int computeFixed64Size(int var0, long var1) {
      return computeTagSize(var0) + computeFixed64SizeNoTag(var1);
   }

   public static int computeFixed64SizeNoTag(long var0) {
      return 8;
   }

   public static int computeFloatSize(int var0, float var1) {
      return computeTagSize(var0) + computeFloatSizeNoTag(var1);
   }

   public static int computeFloatSizeNoTag(float var0) {
      return 4;
   }

   public static int computeGroupSize(int var0, MessageLite var1) {
      return computeTagSize(var0) * 2 + computeGroupSizeNoTag(var1);
   }

   public static int computeGroupSizeNoTag(MessageLite var0) {
      return var0.getSerializedSize();
   }

   public static int computeInt32Size(int var0, int var1) {
      return computeTagSize(var0) + computeInt32SizeNoTag(var1);
   }

   public static int computeInt32SizeNoTag(int var0) {
      return var0 >= 0 ? computeRawVarint32Size(var0) : 10;
   }

   public static int computeInt64Size(int var0, long var1) {
      return computeTagSize(var0) + computeInt64SizeNoTag(var1);
   }

   public static int computeInt64SizeNoTag(long var0) {
      return computeRawVarint64Size(var0);
   }

   public static int computeLazyFieldMessageSetExtensionSize(int var0, LazyField var1) {
      return computeTagSize(1) * 2 + computeUInt32Size(2, var0) + computeLazyFieldSize(3, var1);
   }

   public static int computeLazyFieldSize(int var0, LazyField var1) {
      return computeTagSize(var0) + computeLazyFieldSizeNoTag(var1);
   }

   public static int computeLazyFieldSizeNoTag(LazyField var0) {
      int var1 = var0.getSerializedSize();
      return computeRawVarint32Size(var1) + var1;
   }

   public static int computeMessageSetExtensionSize(int var0, MessageLite var1) {
      return computeTagSize(1) * 2 + computeUInt32Size(2, var0) + computeMessageSize(3, var1);
   }

   public static int computeMessageSize(int var0, MessageLite var1) {
      return computeTagSize(var0) + computeMessageSizeNoTag(var1);
   }

   public static int computeMessageSizeNoTag(MessageLite var0) {
      int var1 = var0.getSerializedSize();
      return computeRawVarint32Size(var1) + var1;
   }

   static int computePreferredBufferSize(int var0) {
      return var0 > 4096 ? 4096 : var0;
   }

   public static int computeRawMessageSetExtensionSize(int var0, ByteString var1) {
      return computeTagSize(1) * 2 + computeUInt32Size(2, var0) + computeBytesSize(3, var1);
   }

   public static int computeRawVarint32Size(int var0) {
      if ((var0 & -128) == 0) {
         return 1;
      } else if ((var0 & -16384) == 0) {
         return 2;
      } else if ((-2097152 & var0) == 0) {
         return 3;
      } else {
         return (-268435456 & var0) == 0 ? 4 : 5;
      }
   }

   public static int computeRawVarint64Size(long var0) {
      if ((-128L & var0) == 0L) {
         return 1;
      } else if ((-16384L & var0) == 0L) {
         return 2;
      } else if ((-2097152L & var0) == 0L) {
         return 3;
      } else if ((-268435456L & var0) == 0L) {
         return 4;
      } else if ((-34359738368L & var0) == 0L) {
         return 5;
      } else if ((-4398046511104L & var0) == 0L) {
         return 6;
      } else if ((-562949953421312L & var0) == 0L) {
         return 7;
      } else if ((-72057594037927936L & var0) == 0L) {
         return 8;
      } else {
         return (Long.MIN_VALUE & var0) == 0L ? 9 : 10;
      }
   }

   public static int computeSFixed32Size(int var0, int var1) {
      return computeTagSize(var0) + computeSFixed32SizeNoTag(var1);
   }

   public static int computeSFixed32SizeNoTag(int var0) {
      return 4;
   }

   public static int computeSFixed64Size(int var0, long var1) {
      return computeTagSize(var0) + computeSFixed64SizeNoTag(var1);
   }

   public static int computeSFixed64SizeNoTag(long var0) {
      return 8;
   }

   public static int computeSInt32Size(int var0, int var1) {
      return computeTagSize(var0) + computeSInt32SizeNoTag(var1);
   }

   public static int computeSInt32SizeNoTag(int var0) {
      return computeRawVarint32Size(encodeZigZag32(var0));
   }

   public static int computeSInt64Size(int var0, long var1) {
      return computeTagSize(var0) + computeSInt64SizeNoTag(var1);
   }

   public static int computeSInt64SizeNoTag(long var0) {
      return computeRawVarint64Size(encodeZigZag64(var0));
   }

   public static int computeStringSize(int var0, String var1) {
      return computeTagSize(var0) + computeStringSizeNoTag(var1);
   }

   public static int computeStringSizeNoTag(String var0) {
      int var1;
      int var2;
      try {
         byte[] var4 = var0.getBytes("UTF-8");
         var1 = computeRawVarint32Size(var4.length);
         var2 = var4.length;
      } catch (UnsupportedEncodingException var3) {
         throw new RuntimeException("UTF-8 not supported.", var3);
      }

      return var1 + var2;
   }

   public static int computeTagSize(int var0) {
      return computeRawVarint32Size(WireFormat.makeTag(var0, 0));
   }

   public static int computeUInt32Size(int var0, int var1) {
      return computeTagSize(var0) + computeUInt32SizeNoTag(var1);
   }

   public static int computeUInt32SizeNoTag(int var0) {
      return computeRawVarint32Size(var0);
   }

   public static int computeUInt64Size(int var0, long var1) {
      return computeTagSize(var0) + computeUInt64SizeNoTag(var1);
   }

   public static int computeUInt64SizeNoTag(long var0) {
      return computeRawVarint64Size(var0);
   }

   @Deprecated
   public static int computeUnknownGroupSize(int var0, MessageLite var1) {
      return computeGroupSize(var0, var1);
   }

   @Deprecated
   public static int computeUnknownGroupSizeNoTag(MessageLite var0) {
      return computeGroupSizeNoTag(var0);
   }

   public static int encodeZigZag32(int var0) {
      return var0 << 1 ^ var0 >> 31;
   }

   public static long encodeZigZag64(long var0) {
      return var0 << 1 ^ var0 >> 63;
   }

   public static CodedOutputStream newInstance(OutputStream var0) {
      return newInstance(var0, 4096);
   }

   public static CodedOutputStream newInstance(OutputStream var0, int var1) {
      return new CodedOutputStream(var0, new byte[var1]);
   }

   public static CodedOutputStream newInstance(byte[] var0) {
      return newInstance(var0, 0, var0.length);
   }

   public static CodedOutputStream newInstance(byte[] var0, int var1, int var2) {
      return new CodedOutputStream(var0, var1, var2);
   }

   private void refreshBuffer() throws IOException {
      OutputStream var1 = this.output;
      if (var1 != null) {
         var1.write(this.buffer, 0, this.position);
         this.position = 0;
      } else {
         throw new CodedOutputStream.OutOfSpaceException();
      }
   }

   public void checkNoSpaceLeft() {
      if (this.spaceLeft() != 0) {
         throw new IllegalStateException("Did not write as much data as expected.");
      }
   }

   public void flush() throws IOException {
      if (this.output != null) {
         this.refreshBuffer();
      }

   }

   public int spaceLeft() {
      if (this.output == null) {
         return this.limit - this.position;
      } else {
         throw new UnsupportedOperationException("spaceLeft() can only be called on CodedOutputStreams that are writing to a flat array.");
      }
   }

   public void writeBool(int var1, boolean var2) throws IOException {
      this.writeTag(var1, 0);
      this.writeBoolNoTag(var2);
   }

   public void writeBoolNoTag(boolean var1) throws IOException {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public void writeBytes(int var1, ByteString var2) throws IOException {
      this.writeTag(var1, 2);
      this.writeBytesNoTag(var2);
   }

   public void writeBytesNoTag(ByteString var1) throws IOException {
      this.writeRawVarint32(var1.size());
      this.writeRawBytes(var1);
   }

   public void writeDouble(int var1, double var2) throws IOException {
      this.writeTag(var1, 1);
      this.writeDoubleNoTag(var2);
   }

   public void writeDoubleNoTag(double var1) throws IOException {
      this.writeRawLittleEndian64(Double.doubleToRawLongBits(var1));
   }

   public void writeEnum(int var1, int var2) throws IOException {
      this.writeTag(var1, 0);
      this.writeEnumNoTag(var2);
   }

   public void writeEnumNoTag(int var1) throws IOException {
      this.writeInt32NoTag(var1);
   }

   public void writeFixed32(int var1, int var2) throws IOException {
      this.writeTag(var1, 5);
      this.writeFixed32NoTag(var2);
   }

   public void writeFixed32NoTag(int var1) throws IOException {
      this.writeRawLittleEndian32(var1);
   }

   public void writeFixed64(int var1, long var2) throws IOException {
      this.writeTag(var1, 1);
      this.writeFixed64NoTag(var2);
   }

   public void writeFixed64NoTag(long var1) throws IOException {
      this.writeRawLittleEndian64(var1);
   }

   public void writeFloat(int var1, float var2) throws IOException {
      this.writeTag(var1, 5);
      this.writeFloatNoTag(var2);
   }

   public void writeFloatNoTag(float var1) throws IOException {
      this.writeRawLittleEndian32(Float.floatToRawIntBits(var1));
   }

   public void writeGroup(int var1, MessageLite var2) throws IOException {
      this.writeTag(var1, 3);
      this.writeGroupNoTag(var2);
      this.writeTag(var1, 4);
   }

   public void writeGroupNoTag(MessageLite var1) throws IOException {
      var1.writeTo(this);
   }

   public void writeInt32(int var1, int var2) throws IOException {
      this.writeTag(var1, 0);
      this.writeInt32NoTag(var2);
   }

   public void writeInt32NoTag(int var1) throws IOException {
      if (var1 >= 0) {
         this.writeRawVarint32(var1);
      } else {
         this.writeRawVarint64((long)var1);
      }
   }

   public void writeInt64(int var1, long var2) throws IOException {
      this.writeTag(var1, 0);
      this.writeInt64NoTag(var2);
   }

   public void writeInt64NoTag(long var1) throws IOException {
      this.writeRawVarint64(var1);
   }

   public void writeMessage(int var1, MessageLite var2) throws IOException {
      this.writeTag(var1, 2);
      this.writeMessageNoTag(var2);
   }

   public void writeMessageNoTag(MessageLite var1) throws IOException {
      this.writeRawVarint32(var1.getSerializedSize());
      var1.writeTo(this);
   }

   public void writeMessageSetExtension(int var1, MessageLite var2) throws IOException {
      this.writeTag(1, 3);
      this.writeUInt32(2, var1);
      this.writeMessage(3, var2);
      this.writeTag(1, 4);
   }

   public void writeRawByte(byte var1) throws IOException {
      if (this.position == this.limit) {
         this.refreshBuffer();
      }

      byte[] var3 = this.buffer;
      int var2 = this.position++;
      var3[var2] = var1;
   }

   public void writeRawByte(int var1) throws IOException {
      this.writeRawByte((byte)var1);
   }

   public void writeRawBytes(ByteString var1) throws IOException {
      this.writeRawBytes((ByteString)var1, 0, var1.size());
   }

   public void writeRawBytes(ByteString var1, int var2, int var3) throws IOException {
      int var5 = this.limit;
      int var4 = this.position;
      if (var5 - var4 >= var3) {
         var1.copyTo(this.buffer, var2, var4, var3);
         this.position += var3;
      } else {
         var5 -= var4;
         var1.copyTo(this.buffer, var2, var4, var5);
         var4 = var2 + var5;
         var2 = var3 - var5;
         this.position = this.limit;
         this.refreshBuffer();
         if (var2 <= this.limit) {
            var1.copyTo(this.buffer, var4, 0, var2);
            this.position = var2;
         } else {
            InputStream var6 = var1.newInput();
            if ((long)var4 == var6.skip((long)var4)) {
               while(var2 > 0) {
                  var3 = Math.min(var2, this.limit);
                  var4 = var6.read(this.buffer, 0, var3);
                  if (var4 != var3) {
                     throw new IllegalStateException("Read failed? Should never happen");
                  }

                  this.output.write(this.buffer, 0, var4);
                  var2 -= var4;
               }

            } else {
               throw new IllegalStateException("Skip failed? Should never happen.");
            }
         }
      }
   }

   public void writeRawBytes(byte[] var1) throws IOException {
      this.writeRawBytes((byte[])var1, 0, var1.length);
   }

   public void writeRawBytes(byte[] var1, int var2, int var3) throws IOException {
      int var5 = this.limit;
      int var4 = this.position;
      if (var5 - var4 >= var3) {
         System.arraycopy(var1, var2, this.buffer, var4, var3);
         this.position += var3;
      } else {
         var5 -= var4;
         System.arraycopy(var1, var2, this.buffer, var4, var5);
         var2 += var5;
         var3 -= var5;
         this.position = this.limit;
         this.refreshBuffer();
         if (var3 <= this.limit) {
            System.arraycopy(var1, var2, this.buffer, 0, var3);
            this.position = var3;
         } else {
            this.output.write(var1, var2, var3);
         }
      }
   }

   public void writeRawLittleEndian32(int var1) throws IOException {
      this.writeRawByte(var1 & 255);
      this.writeRawByte(var1 >> 8 & 255);
      this.writeRawByte(var1 >> 16 & 255);
      this.writeRawByte(var1 >> 24 & 255);
   }

   public void writeRawLittleEndian64(long var1) throws IOException {
      this.writeRawByte((int)var1 & 255);
      this.writeRawByte((int)(var1 >> 8) & 255);
      this.writeRawByte((int)(var1 >> 16) & 255);
      this.writeRawByte((int)(var1 >> 24) & 255);
      this.writeRawByte((int)(var1 >> 32) & 255);
      this.writeRawByte((int)(var1 >> 40) & 255);
      this.writeRawByte((int)(var1 >> 48) & 255);
      this.writeRawByte((int)(var1 >> 56) & 255);
   }

   public void writeRawMessageSetExtension(int var1, ByteString var2) throws IOException {
      this.writeTag(1, 3);
      this.writeUInt32(2, var1);
      this.writeBytes(3, var2);
      this.writeTag(1, 4);
   }

   public void writeRawVarint32(int var1) throws IOException {
      while((var1 & -128) != 0) {
         this.writeRawByte(var1 & 127 | 128);
         var1 >>>= 7;
      }

      this.writeRawByte(var1);
   }

   public void writeRawVarint64(long var1) throws IOException {
      while((-128L & var1) != 0L) {
         this.writeRawByte((int)var1 & 127 | 128);
         var1 >>>= 7;
      }

      this.writeRawByte((int)var1);
   }

   public void writeSFixed32(int var1, int var2) throws IOException {
      this.writeTag(var1, 5);
      this.writeSFixed32NoTag(var2);
   }

   public void writeSFixed32NoTag(int var1) throws IOException {
      this.writeRawLittleEndian32(var1);
   }

   public void writeSFixed64(int var1, long var2) throws IOException {
      this.writeTag(var1, 1);
      this.writeSFixed64NoTag(var2);
   }

   public void writeSFixed64NoTag(long var1) throws IOException {
      this.writeRawLittleEndian64(var1);
   }

   public void writeSInt32(int var1, int var2) throws IOException {
      this.writeTag(var1, 0);
      this.writeSInt32NoTag(var2);
   }

   public void writeSInt32NoTag(int var1) throws IOException {
      this.writeRawVarint32(encodeZigZag32(var1));
   }

   public void writeSInt64(int var1, long var2) throws IOException {
      this.writeTag(var1, 0);
      this.writeSInt64NoTag(var2);
   }

   public void writeSInt64NoTag(long var1) throws IOException {
      this.writeRawVarint64(encodeZigZag64(var1));
   }

   public void writeString(int var1, String var2) throws IOException {
      this.writeTag(var1, 2);
      this.writeStringNoTag(var2);
   }

   public void writeStringNoTag(String var1) throws IOException {
      byte[] var2 = var1.getBytes("UTF-8");
      this.writeRawVarint32(var2.length);
      this.writeRawBytes(var2);
   }

   public void writeTag(int var1, int var2) throws IOException {
      this.writeRawVarint32(WireFormat.makeTag(var1, var2));
   }

   public void writeUInt32(int var1, int var2) throws IOException {
      this.writeTag(var1, 0);
      this.writeUInt32NoTag(var2);
   }

   public void writeUInt32NoTag(int var1) throws IOException {
      this.writeRawVarint32(var1);
   }

   public void writeUInt64(int var1, long var2) throws IOException {
      this.writeTag(var1, 0);
      this.writeUInt64NoTag(var2);
   }

   public void writeUInt64NoTag(long var1) throws IOException {
      this.writeRawVarint64(var1);
   }

   @Deprecated
   public void writeUnknownGroup(int var1, MessageLite var2) throws IOException {
      this.writeGroup(var1, var2);
   }

   @Deprecated
   public void writeUnknownGroupNoTag(MessageLite var1) throws IOException {
      this.writeGroupNoTag(var1);
   }

   public static class OutOfSpaceException extends IOException {
      private static final long serialVersionUID = -6947486886997889499L;

      OutOfSpaceException() {
         super("CodedOutputStream was writing to a flat byte array and ran out of space.");
      }
   }
}
