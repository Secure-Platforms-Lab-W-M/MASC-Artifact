package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;

public interface Message extends MessageLite, MessageOrBuilder {
   boolean equals(Object var1);

   Parser getParserForType();

   int hashCode();

   Message.Builder newBuilderForType();

   Message.Builder toBuilder();

   String toString();

   public interface Builder extends MessageLite.Builder, MessageOrBuilder {
      Message.Builder addRepeatedField(Descriptors.FieldDescriptor var1, Object var2);

      Message build();

      Message buildPartial();

      Message.Builder clear();

      Message.Builder clearField(Descriptors.FieldDescriptor var1);

      Message.Builder clone();

      Descriptors.Descriptor getDescriptorForType();

      Message.Builder getFieldBuilder(Descriptors.FieldDescriptor var1);

      boolean mergeDelimitedFrom(InputStream var1) throws IOException;

      boolean mergeDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException;

      Message.Builder mergeFrom(ByteString var1) throws InvalidProtocolBufferException;

      Message.Builder mergeFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

      Message.Builder mergeFrom(CodedInputStream var1) throws IOException;

      Message.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException;

      Message.Builder mergeFrom(Message var1);

      Message.Builder mergeFrom(InputStream var1) throws IOException;

      Message.Builder mergeFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException;

      Message.Builder mergeFrom(byte[] var1) throws InvalidProtocolBufferException;

      Message.Builder mergeFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException;

      Message.Builder mergeFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException;

      Message.Builder mergeFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

      Message.Builder mergeUnknownFields(UnknownFieldSet var1);

      Message.Builder newBuilderForField(Descriptors.FieldDescriptor var1);

      Message.Builder setField(Descriptors.FieldDescriptor var1, Object var2);

      Message.Builder setRepeatedField(Descriptors.FieldDescriptor var1, int var2, Object var3);

      Message.Builder setUnknownFields(UnknownFieldSet var1);
   }
}
