package com.google.protobuf;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractMessageLite implements MessageLite {
   UninitializedMessageException newUninitializedMessageException() {
      return new UninitializedMessageException(this);
   }

   public byte[] toByteArray() {
      try {
         byte[] var1 = new byte[this.getSerializedSize()];
         CodedOutputStream var2 = CodedOutputStream.newInstance(var1);
         this.writeTo(var2);
         var2.checkNoSpaceLeft();
         return var1;
      } catch (IOException var3) {
         throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", var3);
      }
   }

   public ByteString toByteString() {
      try {
         ByteString.CodedBuilder var1 = ByteString.newCodedBuilder(this.getSerializedSize());
         this.writeTo(var1.getCodedOutput());
         ByteString var3 = var1.build();
         return var3;
      } catch (IOException var2) {
         throw new RuntimeException("Serializing to a ByteString threw an IOException (should never happen).", var2);
      }
   }

   public void writeDelimitedTo(OutputStream var1) throws IOException {
      int var2 = this.getSerializedSize();
      CodedOutputStream var3 = CodedOutputStream.newInstance(var1, CodedOutputStream.computePreferredBufferSize(CodedOutputStream.computeRawVarint32Size(var2) + var2));
      var3.writeRawVarint32(var2);
      this.writeTo(var3);
      var3.flush();
   }

   public void writeTo(OutputStream var1) throws IOException {
      CodedOutputStream var2 = CodedOutputStream.newInstance(var1, CodedOutputStream.computePreferredBufferSize(this.getSerializedSize()));
      this.writeTo(var2);
      var2.flush();
   }

   public abstract static class Builder implements MessageLite.Builder {
      protected static void addAll(Iterable var0, Collection var1) {
         if (var0 instanceof LazyStringList) {
            checkForNullValues(((LazyStringList)var0).getUnderlyingElements());
         } else {
            checkForNullValues(var0);
         }

         if (var0 instanceof Collection) {
            var1.addAll((Collection)var0);
         } else {
            Iterator var2 = var0.iterator();

            while(var2.hasNext()) {
               var1.add(var2.next());
            }

         }
      }

      private static void checkForNullValues(Iterable var0) {
         Iterator var1 = var0.iterator();

         do {
            if (!var1.hasNext()) {
               return;
            }
         } while(var1.next() != null);

         throw null;
      }

      protected static UninitializedMessageException newUninitializedMessageException(MessageLite var0) {
         return new UninitializedMessageException(var0);
      }

      public abstract AbstractMessageLite.Builder clone();

      public boolean mergeDelimitedFrom(InputStream var1) throws IOException {
         return this.mergeDelimitedFrom(var1, ExtensionRegistryLite.getEmptyRegistry());
      }

      public boolean mergeDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException {
         int var3 = var1.read();
         if (var3 == -1) {
            return false;
         } else {
            this.mergeFrom((InputStream)(new AbstractMessageLite.Builder.LimitedInputStream(var1, CodedInputStream.readRawVarint32(var3, var1))), var2);
            return true;
         }
      }

      public AbstractMessageLite.Builder mergeFrom(ByteString var1) throws InvalidProtocolBufferException {
         try {
            CodedInputStream var4 = var1.newCodedInput();
            this.mergeFrom(var4);
            var4.checkLastTagWas(0);
            return this;
         } catch (InvalidProtocolBufferException var2) {
            throw var2;
         } catch (IOException var3) {
            throw new RuntimeException("Reading from a ByteString threw an IOException (should never happen).", var3);
         }
      }

      public AbstractMessageLite.Builder mergeFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
         try {
            CodedInputStream var5 = var1.newCodedInput();
            this.mergeFrom(var5, var2);
            var5.checkLastTagWas(0);
            return this;
         } catch (InvalidProtocolBufferException var3) {
            throw var3;
         } catch (IOException var4) {
            throw new RuntimeException("Reading from a ByteString threw an IOException (should never happen).", var4);
         }
      }

      public AbstractMessageLite.Builder mergeFrom(CodedInputStream var1) throws IOException {
         return this.mergeFrom(var1, ExtensionRegistryLite.getEmptyRegistry());
      }

      public abstract AbstractMessageLite.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException;

      public AbstractMessageLite.Builder mergeFrom(InputStream var1) throws IOException {
         CodedInputStream var2 = CodedInputStream.newInstance(var1);
         this.mergeFrom(var2);
         var2.checkLastTagWas(0);
         return this;
      }

      public AbstractMessageLite.Builder mergeFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException {
         CodedInputStream var3 = CodedInputStream.newInstance(var1);
         this.mergeFrom(var3, var2);
         var3.checkLastTagWas(0);
         return this;
      }

      public AbstractMessageLite.Builder mergeFrom(byte[] var1) throws InvalidProtocolBufferException {
         return this.mergeFrom(var1, 0, var1.length);
      }

      public AbstractMessageLite.Builder mergeFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException {
         try {
            CodedInputStream var6 = CodedInputStream.newInstance(var1, var2, var3);
            this.mergeFrom(var6);
            var6.checkLastTagWas(0);
            return this;
         } catch (InvalidProtocolBufferException var4) {
            throw var4;
         } catch (IOException var5) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", var5);
         }
      }

      public AbstractMessageLite.Builder mergeFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException {
         try {
            CodedInputStream var7 = CodedInputStream.newInstance(var1, var2, var3);
            this.mergeFrom(var7, var4);
            var7.checkLastTagWas(0);
            return this;
         } catch (InvalidProtocolBufferException var5) {
            throw var5;
         } catch (IOException var6) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", var6);
         }
      }

      public AbstractMessageLite.Builder mergeFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
         return this.mergeFrom(var1, 0, var1.length, var2);
      }

      static final class LimitedInputStream extends FilterInputStream {
         private int limit;

         LimitedInputStream(InputStream var1, int var2) {
            super(var1);
            this.limit = var2;
         }

         public int available() throws IOException {
            return Math.min(super.available(), this.limit);
         }

         public int read() throws IOException {
            if (this.limit <= 0) {
               return -1;
            } else {
               int var1 = super.read();
               if (var1 >= 0) {
                  --this.limit;
               }

               return var1;
            }
         }

         public int read(byte[] var1, int var2, int var3) throws IOException {
            int var4 = this.limit;
            if (var4 <= 0) {
               return -1;
            } else {
               var2 = super.read(var1, var2, Math.min(var3, var4));
               if (var2 >= 0) {
                  this.limit -= var2;
               }

               return var2;
            }
         }

         public long skip(long var1) throws IOException {
            var1 = super.skip(Math.min(var1, (long)this.limit));
            if (var1 >= 0L) {
               this.limit = (int)((long)this.limit - var1);
            }

            return var1;
         }
      }
   }
}
