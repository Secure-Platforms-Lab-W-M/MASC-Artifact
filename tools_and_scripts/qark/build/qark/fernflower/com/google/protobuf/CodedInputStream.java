package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public final class CodedInputStream {
   private static final int BUFFER_SIZE = 4096;
   private static final int DEFAULT_RECURSION_LIMIT = 64;
   private static final int DEFAULT_SIZE_LIMIT = 67108864;
   private final byte[] buffer;
   private int bufferPos;
   private int bufferSize;
   private int bufferSizeAfterLimit;
   private int currentLimit = Integer.MAX_VALUE;
   private final InputStream input;
   private int lastTag;
   private int recursionDepth;
   private int recursionLimit = 64;
   private int sizeLimit = 67108864;
   private int totalBytesRetired;

   private CodedInputStream(InputStream var1) {
      this.buffer = new byte[4096];
      this.bufferSize = 0;
      this.bufferPos = 0;
      this.totalBytesRetired = 0;
      this.input = var1;
   }

   private CodedInputStream(byte[] var1, int var2, int var3) {
      this.buffer = var1;
      this.bufferSize = var2 + var3;
      this.bufferPos = var2;
      this.totalBytesRetired = -var2;
      this.input = null;
   }

   public static int decodeZigZag32(int var0) {
      return var0 >>> 1 ^ -(var0 & 1);
   }

   public static long decodeZigZag64(long var0) {
      return var0 >>> 1 ^ -(1L & var0);
   }

   public static CodedInputStream newInstance(InputStream var0) {
      return new CodedInputStream(var0);
   }

   public static CodedInputStream newInstance(byte[] var0) {
      return newInstance(var0, 0, var0.length);
   }

   public static CodedInputStream newInstance(byte[] var0, int var1, int var2) {
      CodedInputStream var4 = new CodedInputStream(var0, var1, var2);

      try {
         var4.pushLimit(var2);
         return var4;
      } catch (InvalidProtocolBufferException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   public static int readRawVarint32(int var0, InputStream var1) throws IOException {
      if ((var0 & 128) == 0) {
         return var0;
      } else {
         int var2 = var0 & 127;
         var0 = 7;

         while(true) {
            int var3 = var0;
            if (var0 >= 32) {
               while(var3 < 64) {
                  var0 = var1.read();
                  if (var0 == -1) {
                     throw InvalidProtocolBufferException.truncatedMessage();
                  }

                  if ((var0 & 128) == 0) {
                     return var2;
                  }

                  var3 += 7;
               }

               throw InvalidProtocolBufferException.malformedVarint();
            }

            var3 = var1.read();
            if (var3 == -1) {
               throw InvalidProtocolBufferException.truncatedMessage();
            }

            var2 |= (var3 & 127) << var0;
            if ((var3 & 128) == 0) {
               return var2;
            }

            var0 += 7;
         }
      }
   }

   static int readRawVarint32(InputStream var0) throws IOException {
      int var1 = var0.read();
      if (var1 != -1) {
         return readRawVarint32(var1, var0);
      } else {
         throw InvalidProtocolBufferException.truncatedMessage();
      }
   }

   private void recomputeBufferSizeAfterLimit() {
      int var1 = this.bufferSize + this.bufferSizeAfterLimit;
      this.bufferSize = var1;
      int var2 = this.totalBytesRetired + var1;
      int var3 = this.currentLimit;
      if (var2 > var3) {
         var2 -= var3;
         this.bufferSizeAfterLimit = var2;
         this.bufferSize = var1 - var2;
      } else {
         this.bufferSizeAfterLimit = 0;
      }
   }

   private boolean refillBuffer(boolean var1) throws IOException {
      int var3 = this.bufferPos;
      int var2 = this.bufferSize;
      if (var3 >= var2) {
         var3 = this.totalBytesRetired;
         if (var3 + var2 == this.currentLimit) {
            if (!var1) {
               return false;
            } else {
               throw InvalidProtocolBufferException.truncatedMessage();
            }
         } else {
            this.totalBytesRetired = var3 + var2;
            this.bufferPos = 0;
            InputStream var4 = this.input;
            if (var4 == null) {
               var2 = -1;
            } else {
               var2 = var4.read(this.buffer);
            }

            this.bufferSize = var2;
            if (var2 != 0 && var2 >= -1) {
               if (var2 == -1) {
                  this.bufferSize = 0;
                  if (!var1) {
                     return false;
                  } else {
                     throw InvalidProtocolBufferException.truncatedMessage();
                  }
               } else {
                  this.recomputeBufferSizeAfterLimit();
                  var2 = this.totalBytesRetired + this.bufferSize + this.bufferSizeAfterLimit;
                  if (var2 <= this.sizeLimit && var2 >= 0) {
                     return true;
                  } else {
                     throw InvalidProtocolBufferException.sizeLimitExceeded();
                  }
               }
            } else {
               StringBuilder var5 = new StringBuilder();
               var5.append("InputStream#read(byte[]) returned invalid result: ");
               var5.append(this.bufferSize);
               var5.append("\nThe InputStream implementation is buggy.");
               throw new IllegalStateException(var5.toString());
            }
         }
      } else {
         throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
      }
   }

   public void checkLastTagWas(int var1) throws InvalidProtocolBufferException {
      if (this.lastTag != var1) {
         throw InvalidProtocolBufferException.invalidEndTag();
      }
   }

   public int getBytesUntilLimit() {
      int var1 = this.currentLimit;
      return var1 == Integer.MAX_VALUE ? -1 : var1 - (this.totalBytesRetired + this.bufferPos);
   }

   public int getTotalBytesRead() {
      return this.totalBytesRetired + this.bufferPos;
   }

   public boolean isAtEnd() throws IOException {
      int var1 = this.bufferPos;
      int var2 = this.bufferSize;
      boolean var4 = false;
      boolean var3 = var4;
      if (var1 == var2) {
         var3 = var4;
         if (!this.refillBuffer(false)) {
            var3 = true;
         }
      }

      return var3;
   }

   public void popLimit(int var1) {
      this.currentLimit = var1;
      this.recomputeBufferSizeAfterLimit();
   }

   public int pushLimit(int var1) throws InvalidProtocolBufferException {
      if (var1 >= 0) {
         var1 += this.totalBytesRetired + this.bufferPos;
         int var2 = this.currentLimit;
         if (var1 <= var2) {
            this.currentLimit = var1;
            this.recomputeBufferSizeAfterLimit();
            return var2;
         } else {
            throw InvalidProtocolBufferException.truncatedMessage();
         }
      } else {
         throw InvalidProtocolBufferException.negativeSize();
      }
   }

   public boolean readBool() throws IOException {
      return this.readRawVarint32() != 0;
   }

   public ByteString readBytes() throws IOException {
      int var1 = this.readRawVarint32();
      if (var1 == 0) {
         return ByteString.EMPTY;
      } else {
         int var2 = this.bufferSize;
         int var3 = this.bufferPos;
         if (var1 <= var2 - var3 && var1 > 0) {
            ByteString var4 = ByteString.copyFrom(this.buffer, var3, var1);
            this.bufferPos += var1;
            return var4;
         } else {
            return ByteString.copyFrom(this.readRawBytes(var1));
         }
      }
   }

   public double readDouble() throws IOException {
      return Double.longBitsToDouble(this.readRawLittleEndian64());
   }

   public int readEnum() throws IOException {
      return this.readRawVarint32();
   }

   public int readFixed32() throws IOException {
      return this.readRawLittleEndian32();
   }

   public long readFixed64() throws IOException {
      return this.readRawLittleEndian64();
   }

   public float readFloat() throws IOException {
      return Float.intBitsToFloat(this.readRawLittleEndian32());
   }

   public MessageLite readGroup(int var1, Parser var2, ExtensionRegistryLite var3) throws IOException {
      int var4 = this.recursionDepth;
      if (var4 < this.recursionLimit) {
         this.recursionDepth = var4 + 1;
         MessageLite var5 = (MessageLite)var2.parsePartialFrom(this, var3);
         this.checkLastTagWas(WireFormat.makeTag(var1, 4));
         --this.recursionDepth;
         return var5;
      } else {
         throw InvalidProtocolBufferException.recursionLimitExceeded();
      }
   }

   public void readGroup(int var1, MessageLite.Builder var2, ExtensionRegistryLite var3) throws IOException {
      int var4 = this.recursionDepth;
      if (var4 < this.recursionLimit) {
         this.recursionDepth = var4 + 1;
         var2.mergeFrom(this, var3);
         this.checkLastTagWas(WireFormat.makeTag(var1, 4));
         --this.recursionDepth;
      } else {
         throw InvalidProtocolBufferException.recursionLimitExceeded();
      }
   }

   public int readInt32() throws IOException {
      return this.readRawVarint32();
   }

   public long readInt64() throws IOException {
      return this.readRawVarint64();
   }

   public MessageLite readMessage(Parser var1, ExtensionRegistryLite var2) throws IOException {
      int var3 = this.readRawVarint32();
      if (this.recursionDepth < this.recursionLimit) {
         var3 = this.pushLimit(var3);
         ++this.recursionDepth;
         MessageLite var4 = (MessageLite)var1.parsePartialFrom(this, var2);
         this.checkLastTagWas(0);
         --this.recursionDepth;
         this.popLimit(var3);
         return var4;
      } else {
         throw InvalidProtocolBufferException.recursionLimitExceeded();
      }
   }

   public void readMessage(MessageLite.Builder var1, ExtensionRegistryLite var2) throws IOException {
      int var3 = this.readRawVarint32();
      if (this.recursionDepth < this.recursionLimit) {
         var3 = this.pushLimit(var3);
         ++this.recursionDepth;
         var1.mergeFrom(this, var2);
         this.checkLastTagWas(0);
         --this.recursionDepth;
         this.popLimit(var3);
      } else {
         throw InvalidProtocolBufferException.recursionLimitExceeded();
      }
   }

   public byte readRawByte() throws IOException {
      if (this.bufferPos == this.bufferSize) {
         this.refillBuffer(true);
      }

      byte[] var2 = this.buffer;
      int var1 = this.bufferPos++;
      return var2[var1];
   }

   public byte[] readRawBytes(int var1) throws IOException {
      if (var1 < 0) {
         throw InvalidProtocolBufferException.negativeSize();
      } else {
         int var2 = this.totalBytesRetired;
         int var3 = this.bufferPos;
         int var4 = this.currentLimit;
         if (var2 + var3 + var1 > var4) {
            this.skipRawBytes(var4 - var2 - var3);
            throw InvalidProtocolBufferException.truncatedMessage();
         } else {
            var4 = this.bufferSize;
            byte[] var11;
            if (var1 <= var4 - var3) {
               var11 = new byte[var1];
               System.arraycopy(this.buffer, var3, var11, 0, var1);
               this.bufferPos += var1;
               return var11;
            } else if (var1 < 4096) {
               var11 = new byte[var1];
               var2 = var4 - var3;
               System.arraycopy(this.buffer, var3, var11, 0, var2);
               this.bufferPos = this.bufferSize;
               this.refillBuffer(true);

               while(true) {
                  var3 = this.bufferSize;
                  if (var1 - var2 <= var3) {
                     System.arraycopy(this.buffer, 0, var11, var2, var1 - var2);
                     this.bufferPos = var1 - var2;
                     return var11;
                  }

                  System.arraycopy(this.buffer, 0, var11, var2, var3);
                  var3 = this.bufferSize;
                  var2 += var3;
                  this.bufferPos = var3;
                  this.refillBuffer(true);
               }
            } else {
               int var5 = this.bufferPos;
               int var6 = this.bufferSize;
               this.totalBytesRetired = var2 + var4;
               this.bufferPos = 0;
               this.bufferSize = 0;
               var2 = var1 - (var6 - var5);
               ArrayList var7 = new ArrayList();

               byte[] var8;
               while(var2 > 0) {
                  var8 = new byte[Math.min(var2, 4096)];

                  for(var3 = 0; var3 < var8.length; var3 += var4) {
                     InputStream var9 = this.input;
                     if (var9 == null) {
                        var4 = -1;
                     } else {
                        var4 = var9.read(var8, var3, var8.length - var3);
                     }

                     if (var4 == -1) {
                        throw InvalidProtocolBufferException.truncatedMessage();
                     }

                     this.totalBytesRetired += var4;
                  }

                  var2 -= var8.length;
                  var7.add(var8);
               }

               var8 = new byte[var1];
               var1 = var6 - var5;
               System.arraycopy(this.buffer, var5, var8, 0, var1);

               byte[] var12;
               for(Iterator var10 = var7.iterator(); var10.hasNext(); var1 += var12.length) {
                  var12 = (byte[])var10.next();
                  System.arraycopy(var12, 0, var8, var1, var12.length);
               }

               return var8;
            }
         }
      }
   }

   public int readRawLittleEndian32() throws IOException {
      return this.readRawByte() & 255 | (this.readRawByte() & 255) << 8 | (this.readRawByte() & 255) << 16 | (this.readRawByte() & 255) << 24;
   }

   public long readRawLittleEndian64() throws IOException {
      byte var1 = this.readRawByte();
      byte var2 = this.readRawByte();
      byte var3 = this.readRawByte();
      byte var4 = this.readRawByte();
      byte var5 = this.readRawByte();
      byte var6 = this.readRawByte();
      byte var7 = this.readRawByte();
      byte var8 = this.readRawByte();
      return (long)var1 & 255L | ((long)var2 & 255L) << 8 | ((long)var3 & 255L) << 16 | ((long)var4 & 255L) << 24 | ((long)var5 & 255L) << 32 | ((long)var6 & 255L) << 40 | ((long)var7 & 255L) << 48 | (255L & (long)var8) << 56;
   }

   public int readRawVarint32() throws IOException {
      byte var1 = this.readRawByte();
      if (var1 >= 0) {
         return var1;
      } else {
         int var4 = var1 & 127;
         byte var2 = this.readRawByte();
         if (var2 >= 0) {
            return var4 | var2 << 7;
         } else {
            var4 |= (var2 & 127) << 7;
            var2 = this.readRawByte();
            if (var2 >= 0) {
               return var4 | var2 << 14;
            } else {
               int var5 = var4 | (var2 & 127) << 14;
               byte var3 = this.readRawByte();
               if (var3 >= 0) {
                  return var5 | var3 << 21;
               } else {
                  var1 = this.readRawByte();
                  var5 = var5 | (var3 & 127) << 21 | var1 << 28;
                  if (var1 < 0) {
                     for(var4 = 0; var4 < 5; ++var4) {
                        if (this.readRawByte() >= 0) {
                           return var5;
                        }
                     }

                     throw InvalidProtocolBufferException.malformedVarint();
                  } else {
                     return var5;
                  }
               }
            }
         }
      }
   }

   public long readRawVarint64() throws IOException {
      int var1 = 0;

      for(long var3 = 0L; var1 < 64; var1 += 7) {
         byte var2 = this.readRawByte();
         var3 |= (long)(var2 & 127) << var1;
         if ((var2 & 128) == 0) {
            return var3;
         }
      }

      throw InvalidProtocolBufferException.malformedVarint();
   }

   public int readSFixed32() throws IOException {
      return this.readRawLittleEndian32();
   }

   public long readSFixed64() throws IOException {
      return this.readRawLittleEndian64();
   }

   public int readSInt32() throws IOException {
      return decodeZigZag32(this.readRawVarint32());
   }

   public long readSInt64() throws IOException {
      return decodeZigZag64(this.readRawVarint64());
   }

   public String readString() throws IOException {
      int var1 = this.readRawVarint32();
      if (var1 <= this.bufferSize - this.bufferPos && var1 > 0) {
         String var2 = new String(this.buffer, this.bufferPos, var1, "UTF-8");
         this.bufferPos += var1;
         return var2;
      } else {
         return new String(this.readRawBytes(var1), "UTF-8");
      }
   }

   public int readTag() throws IOException {
      if (this.isAtEnd()) {
         this.lastTag = 0;
         return 0;
      } else {
         int var1 = this.readRawVarint32();
         this.lastTag = var1;
         if (WireFormat.getTagFieldNumber(var1) != 0) {
            return this.lastTag;
         } else {
            throw InvalidProtocolBufferException.invalidTag();
         }
      }
   }

   public int readUInt32() throws IOException {
      return this.readRawVarint32();
   }

   public long readUInt64() throws IOException {
      return this.readRawVarint64();
   }

   @Deprecated
   public void readUnknownGroup(int var1, MessageLite.Builder var2) throws IOException {
      this.readGroup(var1, (MessageLite.Builder)var2, (ExtensionRegistryLite)null);
   }

   public void resetSizeCounter() {
      this.totalBytesRetired = -this.bufferPos;
   }

   public int setRecursionLimit(int var1) {
      if (var1 >= 0) {
         int var2 = this.recursionLimit;
         this.recursionLimit = var1;
         return var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Recursion limit cannot be negative: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public int setSizeLimit(int var1) {
      if (var1 >= 0) {
         int var2 = this.sizeLimit;
         this.sizeLimit = var1;
         return var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Size limit cannot be negative: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public boolean skipField(int var1) throws IOException {
      int var2 = WireFormat.getTagWireType(var1);
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 2) {
               if (var2 != 3) {
                  if (var2 != 4) {
                     if (var2 == 5) {
                        this.readRawLittleEndian32();
                        return true;
                     } else {
                        throw InvalidProtocolBufferException.invalidWireType();
                     }
                  } else {
                     return false;
                  }
               } else {
                  this.skipMessage();
                  this.checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(var1), 4));
                  return true;
               }
            } else {
               this.skipRawBytes(this.readRawVarint32());
               return true;
            }
         } else {
            this.readRawLittleEndian64();
            return true;
         }
      } else {
         this.readInt32();
         return true;
      }
   }

   public void skipMessage() throws IOException {
      while(true) {
         int var1 = this.readTag();
         if (var1 != 0) {
            if (this.skipField(var1)) {
               continue;
            }

            return;
         }

         return;
      }
   }

   public void skipRawBytes(int var1) throws IOException {
      if (var1 < 0) {
         throw InvalidProtocolBufferException.negativeSize();
      } else {
         int var3 = this.totalBytesRetired;
         int var2 = this.bufferPos;
         int var4 = this.currentLimit;
         if (var3 + var2 + var1 > var4) {
            this.skipRawBytes(var4 - var3 - var2);
            throw InvalidProtocolBufferException.truncatedMessage();
         } else {
            var3 = this.bufferSize;
            if (var1 <= var3 - var2) {
               this.bufferPos = var2 + var1;
            } else {
               var2 = var3 - var2;
               this.bufferPos = var3;
               this.refillBuffer(true);

               while(true) {
                  var3 = this.bufferSize;
                  if (var1 - var2 <= var3) {
                     this.bufferPos = var1 - var2;
                     return;
                  }

                  var2 += var3;
                  this.bufferPos = var3;
                  this.refillBuffer(true);
               }
            }
         }
      }
   }
}
