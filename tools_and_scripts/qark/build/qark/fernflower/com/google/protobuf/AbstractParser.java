package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractParser implements Parser {
   private static final ExtensionRegistryLite EMPTY_REGISTRY = ExtensionRegistryLite.getEmptyRegistry();

   private MessageLite checkMessageInitialized(MessageLite var1) throws InvalidProtocolBufferException {
      if (var1 != null) {
         if (var1.isInitialized()) {
            return var1;
         } else {
            throw this.newUninitializedMessageException(var1).asInvalidProtocolBufferException().setUnfinishedMessage(var1);
         }
      } else {
         return var1;
      }
   }

   private UninitializedMessageException newUninitializedMessageException(MessageLite var1) {
      return var1 instanceof AbstractMessageLite ? ((AbstractMessageLite)var1).newUninitializedMessageException() : new UninitializedMessageException(var1);
   }

   public MessageLite parseDelimitedFrom(InputStream var1) throws InvalidProtocolBufferException {
      return this.parseDelimitedFrom(var1, EMPTY_REGISTRY);
   }

   public MessageLite parseDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
      return this.checkMessageInitialized(this.parsePartialDelimitedFrom(var1, var2));
   }

   public MessageLite parseFrom(ByteString var1) throws InvalidProtocolBufferException {
      return this.parseFrom(var1, EMPTY_REGISTRY);
   }

   public MessageLite parseFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
      return this.checkMessageInitialized(this.parsePartialFrom(var1, var2));
   }

   public MessageLite parseFrom(CodedInputStream var1) throws InvalidProtocolBufferException {
      return this.parseFrom(var1, EMPTY_REGISTRY);
   }

   public MessageLite parseFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
      return this.checkMessageInitialized((MessageLite)this.parsePartialFrom((CodedInputStream)var1, var2));
   }

   public MessageLite parseFrom(InputStream var1) throws InvalidProtocolBufferException {
      return this.parseFrom(var1, EMPTY_REGISTRY);
   }

   public MessageLite parseFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
      return this.checkMessageInitialized(this.parsePartialFrom(var1, var2));
   }

   public MessageLite parseFrom(byte[] var1) throws InvalidProtocolBufferException {
      return this.parseFrom(var1, EMPTY_REGISTRY);
   }

   public MessageLite parseFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException {
      return this.parseFrom(var1, var2, var3, EMPTY_REGISTRY);
   }

   public MessageLite parseFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException {
      return this.checkMessageInitialized(this.parsePartialFrom(var1, var2, var3, var4));
   }

   public MessageLite parseFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
      return this.parseFrom(var1, 0, var1.length, var2);
   }

   public MessageLite parsePartialDelimitedFrom(InputStream var1) throws InvalidProtocolBufferException {
      return this.parsePartialDelimitedFrom(var1, EMPTY_REGISTRY);
   }

   public MessageLite parsePartialDelimitedFrom(InputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
      // $FF: Couldn't be decompiled
   }

   public MessageLite parsePartialFrom(ByteString var1) throws InvalidProtocolBufferException {
      return this.parsePartialFrom(var1, EMPTY_REGISTRY);
   }

   public MessageLite parsePartialFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
      IOException var8;
      InvalidProtocolBufferException var9;
      CodedInputStream var10;
      MessageLite var11;
      try {
         var10 = var1.newCodedInput();
         var11 = (MessageLite)this.parsePartialFrom((CodedInputStream)var10, var2);
      } catch (InvalidProtocolBufferException var6) {
         var9 = var6;
         throw var9;
      } catch (IOException var7) {
         var8 = var7;
         throw new RuntimeException("Reading from a ByteString threw an IOException (should never happen).", var8);
      }

      try {
         try {
            var10.checkLastTagWas(0);
            return var11;
         } catch (InvalidProtocolBufferException var4) {
            var9 = var4;

            try {
               throw var9.setUnfinishedMessage(var11);
            } catch (InvalidProtocolBufferException var3) {
               var9 = var3;
            }
         }
      } catch (IOException var5) {
         var8 = var5;
         throw new RuntimeException("Reading from a ByteString threw an IOException (should never happen).", var8);
      }

      throw var9;
   }

   public MessageLite parsePartialFrom(CodedInputStream var1) throws InvalidProtocolBufferException {
      return (MessageLite)this.parsePartialFrom((CodedInputStream)var1, EMPTY_REGISTRY);
   }

   public MessageLite parsePartialFrom(InputStream var1) throws InvalidProtocolBufferException {
      return this.parsePartialFrom(var1, EMPTY_REGISTRY);
   }

   public MessageLite parsePartialFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
      CodedInputStream var4 = CodedInputStream.newInstance(var1);
      MessageLite var5 = (MessageLite)this.parsePartialFrom((CodedInputStream)var4, var2);

      try {
         var4.checkLastTagWas(0);
         return var5;
      } catch (InvalidProtocolBufferException var3) {
         throw var3.setUnfinishedMessage(var5);
      }
   }

   public MessageLite parsePartialFrom(byte[] var1) throws InvalidProtocolBufferException {
      return this.parsePartialFrom(var1, 0, var1.length, EMPTY_REGISTRY);
   }

   public MessageLite parsePartialFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException {
      return this.parsePartialFrom(var1, var2, var3, EMPTY_REGISTRY);
   }

   public MessageLite parsePartialFrom(byte[] param1, int param2, int param3, ExtensionRegistryLite param4) throws InvalidProtocolBufferException {
      // $FF: Couldn't be decompiled
   }

   public MessageLite parsePartialFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
      return this.parsePartialFrom(var1, 0, var1.length, var2);
   }
}
