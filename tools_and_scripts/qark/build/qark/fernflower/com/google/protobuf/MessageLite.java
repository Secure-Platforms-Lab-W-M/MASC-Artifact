package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface MessageLite extends MessageLiteOrBuilder {
   Parser getParserForType();

   int getSerializedSize();

   MessageLite.Builder newBuilderForType();

   MessageLite.Builder toBuilder();

   byte[] toByteArray();

   ByteString toByteString();

   void writeDelimitedTo(OutputStream var1) throws IOException;

   void writeTo(CodedOutputStream var1) throws IOException;

   void writeTo(OutputStream var1) throws IOException;

   public interface Builder extends MessageLiteOrBuilder, Cloneable {
      MessageLite build();

      MessageLite buildPartial();

      MessageLite.Builder clear();

      MessageLite.Builder clone();

      boolean mergeDelimitedFrom(InputStream var1) throws IOException;

      boolean mergeDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException;

      MessageLite.Builder mergeFrom(ByteString var1) throws InvalidProtocolBufferException;

      MessageLite.Builder mergeFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

      MessageLite.Builder mergeFrom(CodedInputStream var1) throws IOException;

      MessageLite.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException;

      MessageLite.Builder mergeFrom(InputStream var1) throws IOException;

      MessageLite.Builder mergeFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException;

      MessageLite.Builder mergeFrom(byte[] var1) throws InvalidProtocolBufferException;

      MessageLite.Builder mergeFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException;

      MessageLite.Builder mergeFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException;

      MessageLite.Builder mergeFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;
   }
}
