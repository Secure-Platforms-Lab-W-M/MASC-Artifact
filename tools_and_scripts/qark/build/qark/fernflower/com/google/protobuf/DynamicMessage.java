package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public final class DynamicMessage extends AbstractMessage {
   private final FieldSet fields;
   private int memoizedSize;
   private final Descriptors.Descriptor type;
   private final UnknownFieldSet unknownFields;

   private DynamicMessage(Descriptors.Descriptor var1, FieldSet var2, UnknownFieldSet var3) {
      this.memoizedSize = -1;
      this.type = var1;
      this.fields = var2;
      this.unknownFields = var3;
   }

   // $FF: synthetic method
   DynamicMessage(Descriptors.Descriptor var1, FieldSet var2, UnknownFieldSet var3, Object var4) {
      this(var1, var2, var3);
   }

   public static DynamicMessage getDefaultInstance(Descriptors.Descriptor var0) {
      return new DynamicMessage(var0, FieldSet.emptySet(), UnknownFieldSet.getDefaultInstance());
   }

   private static boolean isInitialized(Descriptors.Descriptor var0, FieldSet var1) {
      Iterator var3 = var0.getFields().iterator();

      Descriptors.FieldDescriptor var2;
      do {
         if (!var3.hasNext()) {
            return var1.isInitialized();
         }

         var2 = (Descriptors.FieldDescriptor)var3.next();
      } while(!var2.isRequired() || var1.hasField(var2));

      return false;
   }

   public static DynamicMessage.Builder newBuilder(Descriptors.Descriptor var0) {
      return new DynamicMessage.Builder(var0);
   }

   public static DynamicMessage.Builder newBuilder(Message var0) {
      return (new DynamicMessage.Builder(var0.getDescriptorForType())).mergeFrom(var0);
   }

   public static DynamicMessage parseFrom(Descriptors.Descriptor var0, ByteString var1) throws InvalidProtocolBufferException {
      return ((DynamicMessage.Builder)newBuilder(var0).mergeFrom((ByteString)var1)).buildParsed();
   }

   public static DynamicMessage parseFrom(Descriptors.Descriptor var0, ByteString var1, ExtensionRegistry var2) throws InvalidProtocolBufferException {
      return ((DynamicMessage.Builder)newBuilder(var0).mergeFrom(var1, var2)).buildParsed();
   }

   public static DynamicMessage parseFrom(Descriptors.Descriptor var0, CodedInputStream var1) throws IOException {
      return ((DynamicMessage.Builder)newBuilder(var0).mergeFrom((CodedInputStream)var1)).buildParsed();
   }

   public static DynamicMessage parseFrom(Descriptors.Descriptor var0, CodedInputStream var1, ExtensionRegistry var2) throws IOException {
      return ((DynamicMessage.Builder)newBuilder(var0).mergeFrom(var1, var2)).buildParsed();
   }

   public static DynamicMessage parseFrom(Descriptors.Descriptor var0, InputStream var1) throws IOException {
      return ((DynamicMessage.Builder)newBuilder(var0).mergeFrom((InputStream)var1)).buildParsed();
   }

   public static DynamicMessage parseFrom(Descriptors.Descriptor var0, InputStream var1, ExtensionRegistry var2) throws IOException {
      return ((DynamicMessage.Builder)newBuilder(var0).mergeFrom(var1, var2)).buildParsed();
   }

   public static DynamicMessage parseFrom(Descriptors.Descriptor var0, byte[] var1) throws InvalidProtocolBufferException {
      return ((DynamicMessage.Builder)newBuilder(var0).mergeFrom((byte[])var1)).buildParsed();
   }

   public static DynamicMessage parseFrom(Descriptors.Descriptor var0, byte[] var1, ExtensionRegistry var2) throws InvalidProtocolBufferException {
      return ((DynamicMessage.Builder)newBuilder(var0).mergeFrom(var1, var2)).buildParsed();
   }

   private void verifyContainingType(Descriptors.FieldDescriptor var1) {
      if (var1.getContainingType() != this.type) {
         throw new IllegalArgumentException("FieldDescriptor does not match message type.");
      }
   }

   public Map getAllFields() {
      return this.fields.getAllFields();
   }

   public DynamicMessage getDefaultInstanceForType() {
      return getDefaultInstance(this.type);
   }

   public Descriptors.Descriptor getDescriptorForType() {
      return this.type;
   }

   public Object getField(Descriptors.FieldDescriptor var1) {
      this.verifyContainingType(var1);
      Object var3 = this.fields.getField(var1);
      Object var2 = var3;
      if (var3 == null) {
         if (var1.isRepeated()) {
            return Collections.emptyList();
         }

         if (var1.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
            return getDefaultInstance(var1.getMessageType());
         }

         var2 = var1.getDefaultValue();
      }

      return var2;
   }

   public Parser getParserForType() {
      return new AbstractParser() {
         public DynamicMessage parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            DynamicMessage.Builder var3 = DynamicMessage.newBuilder(DynamicMessage.this.type);

            try {
               var3.mergeFrom(var1, var2);
            } catch (InvalidProtocolBufferException var4) {
               throw var4.setUnfinishedMessage(var3.buildPartial());
            } catch (IOException var5) {
               throw (new InvalidProtocolBufferException(var5.getMessage())).setUnfinishedMessage(var3.buildPartial());
            }

            return var3.buildPartial();
         }
      };
   }

   public Object getRepeatedField(Descriptors.FieldDescriptor var1, int var2) {
      this.verifyContainingType(var1);
      return this.fields.getRepeatedField(var1, var2);
   }

   public int getRepeatedFieldCount(Descriptors.FieldDescriptor var1) {
      this.verifyContainingType(var1);
      return this.fields.getRepeatedFieldCount(var1);
   }

   public int getSerializedSize() {
      int var1 = this.memoizedSize;
      if (var1 != -1) {
         return var1;
      } else {
         if (this.type.getOptions().getMessageSetWireFormat()) {
            var1 = this.fields.getMessageSetSerializedSize() + this.unknownFields.getSerializedSizeAsMessageSet();
         } else {
            var1 = this.fields.getSerializedSize() + this.unknownFields.getSerializedSize();
         }

         this.memoizedSize = var1;
         return var1;
      }
   }

   public UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
   }

   public boolean hasField(Descriptors.FieldDescriptor var1) {
      this.verifyContainingType(var1);
      return this.fields.hasField(var1);
   }

   public boolean isInitialized() {
      return isInitialized(this.type, this.fields);
   }

   public DynamicMessage.Builder newBuilderForType() {
      return new DynamicMessage.Builder(this.type);
   }

   public DynamicMessage.Builder toBuilder() {
      return this.newBuilderForType().mergeFrom(this);
   }

   public void writeTo(CodedOutputStream var1) throws IOException {
      if (this.type.getOptions().getMessageSetWireFormat()) {
         this.fields.writeMessageSetTo(var1);
         this.unknownFields.writeAsMessageSetTo(var1);
      } else {
         this.fields.writeTo(var1);
         this.unknownFields.writeTo(var1);
      }
   }

   public static final class Builder extends AbstractMessage.Builder {
      private FieldSet fields;
      private final Descriptors.Descriptor type;
      private UnknownFieldSet unknownFields;

      private Builder(Descriptors.Descriptor var1) {
         this.type = var1;
         this.fields = FieldSet.newFieldSet();
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      // $FF: synthetic method
      Builder(Descriptors.Descriptor var1, Object var2) {
         this(var1);
      }

      private DynamicMessage buildParsed() throws InvalidProtocolBufferException {
         if (this.isInitialized()) {
            return this.buildPartial();
         } else {
            throw newUninitializedMessageException(new DynamicMessage(this.type, this.fields, this.unknownFields)).asInvalidProtocolBufferException();
         }
      }

      private void ensureIsMutable() {
         if (this.fields.isImmutable()) {
            this.fields = this.fields.clone();
         }

      }

      private void verifyContainingType(Descriptors.FieldDescriptor var1) {
         if (var1.getContainingType() != this.type) {
            throw new IllegalArgumentException("FieldDescriptor does not match message type.");
         }
      }

      public DynamicMessage.Builder addRepeatedField(Descriptors.FieldDescriptor var1, Object var2) {
         this.verifyContainingType(var1);
         this.ensureIsMutable();
         this.fields.addRepeatedField(var1, var2);
         return this;
      }

      public DynamicMessage build() {
         if (this.isInitialized()) {
            return this.buildPartial();
         } else {
            throw newUninitializedMessageException(new DynamicMessage(this.type, this.fields, this.unknownFields));
         }
      }

      public DynamicMessage buildPartial() {
         this.fields.makeImmutable();
         return new DynamicMessage(this.type, this.fields, this.unknownFields);
      }

      public DynamicMessage.Builder clear() {
         if (this.fields.isImmutable()) {
            this.fields = FieldSet.newFieldSet();
         } else {
            this.fields.clear();
         }

         this.unknownFields = UnknownFieldSet.getDefaultInstance();
         return this;
      }

      public DynamicMessage.Builder clearField(Descriptors.FieldDescriptor var1) {
         this.verifyContainingType(var1);
         this.ensureIsMutable();
         this.fields.clearField(var1);
         return this;
      }

      public DynamicMessage.Builder clone() {
         DynamicMessage.Builder var1 = new DynamicMessage.Builder(this.type);
         var1.fields.mergeFrom(this.fields);
         var1.mergeUnknownFields(this.unknownFields);
         return var1;
      }

      public Map getAllFields() {
         return this.fields.getAllFields();
      }

      public DynamicMessage getDefaultInstanceForType() {
         return DynamicMessage.getDefaultInstance(this.type);
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return this.type;
      }

      public Object getField(Descriptors.FieldDescriptor var1) {
         this.verifyContainingType(var1);
         Object var3 = this.fields.getField(var1);
         Object var2 = var3;
         if (var3 == null) {
            if (var1.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
               return DynamicMessage.getDefaultInstance(var1.getMessageType());
            }

            var2 = var1.getDefaultValue();
         }

         return var2;
      }

      public Message.Builder getFieldBuilder(Descriptors.FieldDescriptor var1) {
         throw new UnsupportedOperationException("getFieldBuilder() called on a dynamic message type.");
      }

      public Object getRepeatedField(Descriptors.FieldDescriptor var1, int var2) {
         this.verifyContainingType(var1);
         return this.fields.getRepeatedField(var1, var2);
      }

      public int getRepeatedFieldCount(Descriptors.FieldDescriptor var1) {
         this.verifyContainingType(var1);
         return this.fields.getRepeatedFieldCount(var1);
      }

      public UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasField(Descriptors.FieldDescriptor var1) {
         this.verifyContainingType(var1);
         return this.fields.hasField(var1);
      }

      public boolean isInitialized() {
         return DynamicMessage.isInitialized(this.type, this.fields);
      }

      public DynamicMessage.Builder mergeFrom(Message var1) {
         if (var1 instanceof DynamicMessage) {
            DynamicMessage var2 = (DynamicMessage)var1;
            if (var2.type == this.type) {
               this.ensureIsMutable();
               this.fields.mergeFrom(var2.fields);
               this.mergeUnknownFields(var2.unknownFields);
               return this;
            } else {
               throw new IllegalArgumentException("mergeFrom(Message) can only merge messages of the same type.");
            }
         } else {
            return (DynamicMessage.Builder)super.mergeFrom(var1);
         }
      }

      public DynamicMessage.Builder mergeUnknownFields(UnknownFieldSet var1) {
         this.unknownFields = UnknownFieldSet.newBuilder(this.unknownFields).mergeFrom(var1).build();
         return this;
      }

      public DynamicMessage.Builder newBuilderForField(Descriptors.FieldDescriptor var1) {
         this.verifyContainingType(var1);
         if (var1.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
            return new DynamicMessage.Builder(var1.getMessageType());
         } else {
            throw new IllegalArgumentException("newBuilderForField is only valid for fields with message type.");
         }
      }

      public DynamicMessage.Builder setField(Descriptors.FieldDescriptor var1, Object var2) {
         this.verifyContainingType(var1);
         this.ensureIsMutable();
         this.fields.setField(var1, var2);
         return this;
      }

      public DynamicMessage.Builder setRepeatedField(Descriptors.FieldDescriptor var1, int var2, Object var3) {
         this.verifyContainingType(var1);
         this.ensureIsMutable();
         this.fields.setRepeatedField(var1, var2, var3);
         return this;
      }

      public DynamicMessage.Builder setUnknownFields(UnknownFieldSet var1) {
         this.unknownFields = var1;
         return this;
      }
   }
}
