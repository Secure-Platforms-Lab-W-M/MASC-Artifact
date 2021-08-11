package com.google.protobuf;

import java.io.InputStream;

public interface Parser {
   Object parseDelimitedFrom(InputStream var1) throws InvalidProtocolBufferException;

   Object parseDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parseFrom(ByteString var1) throws InvalidProtocolBufferException;

   Object parseFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parseFrom(CodedInputStream var1) throws InvalidProtocolBufferException;

   Object parseFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parseFrom(InputStream var1) throws InvalidProtocolBufferException;

   Object parseFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parseFrom(byte[] var1) throws InvalidProtocolBufferException;

   Object parseFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException;

   Object parseFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException;

   Object parseFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parsePartialDelimitedFrom(InputStream var1) throws InvalidProtocolBufferException;

   Object parsePartialDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parsePartialFrom(ByteString var1) throws InvalidProtocolBufferException;

   Object parsePartialFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parsePartialFrom(CodedInputStream var1) throws InvalidProtocolBufferException;

   Object parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parsePartialFrom(InputStream var1) throws InvalidProtocolBufferException;

   Object parsePartialFrom(InputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;

   Object parsePartialFrom(byte[] var1) throws InvalidProtocolBufferException;

   Object parsePartialFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException;

   Object parsePartialFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException;

   Object parsePartialFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException;
}
