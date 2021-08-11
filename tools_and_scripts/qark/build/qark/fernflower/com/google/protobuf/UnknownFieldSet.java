package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public final class UnknownFieldSet implements MessageLite {
   private static final UnknownFieldSet.Parser PARSER = new UnknownFieldSet.Parser();
   private static final UnknownFieldSet defaultInstance = new UnknownFieldSet(Collections.emptyMap());
   private Map fields;

   private UnknownFieldSet() {
   }

   private UnknownFieldSet(Map var1) {
      this.fields = var1;
   }

   // $FF: synthetic method
   UnknownFieldSet(Map var1, Object var2) {
      this(var1);
   }

   public static UnknownFieldSet getDefaultInstance() {
      return defaultInstance;
   }

   public static UnknownFieldSet.Builder newBuilder() {
      return UnknownFieldSet.Builder.create();
   }

   public static UnknownFieldSet.Builder newBuilder(UnknownFieldSet var0) {
      return newBuilder().mergeFrom(var0);
   }

   public static UnknownFieldSet parseFrom(ByteString var0) throws InvalidProtocolBufferException {
      return newBuilder().mergeFrom(var0).build();
   }

   public static UnknownFieldSet parseFrom(CodedInputStream var0) throws IOException {
      return newBuilder().mergeFrom(var0).build();
   }

   public static UnknownFieldSet parseFrom(InputStream var0) throws IOException {
      return newBuilder().mergeFrom(var0).build();
   }

   public static UnknownFieldSet parseFrom(byte[] var0) throws InvalidProtocolBufferException {
      return newBuilder().mergeFrom(var0).build();
   }

   public Map asMap() {
      return this.fields;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof UnknownFieldSet && this.fields.equals(((UnknownFieldSet)var1).fields);
      }
   }

   public UnknownFieldSet getDefaultInstanceForType() {
      return defaultInstance;
   }

   public UnknownFieldSet.Field getField(int var1) {
      UnknownFieldSet.Field var2 = (UnknownFieldSet.Field)this.fields.get(var1);
      return var2 == null ? UnknownFieldSet.Field.getDefaultInstance() : var2;
   }

   public final UnknownFieldSet.Parser getParserForType() {
      return PARSER;
   }

   public int getSerializedSize() {
      int var1 = 0;

      Entry var3;
      for(Iterator var2 = this.fields.entrySet().iterator(); var2.hasNext(); var1 += ((UnknownFieldSet.Field)var3.getValue()).getSerializedSize((Integer)var3.getKey())) {
         var3 = (Entry)var2.next();
      }

      return var1;
   }

   public int getSerializedSizeAsMessageSet() {
      int var1 = 0;

      Entry var3;
      for(Iterator var2 = this.fields.entrySet().iterator(); var2.hasNext(); var1 += ((UnknownFieldSet.Field)var3.getValue()).getSerializedSizeAsMessageSetExtension((Integer)var3.getKey())) {
         var3 = (Entry)var2.next();
      }

      return var1;
   }

   public boolean hasField(int var1) {
      return this.fields.containsKey(var1);
   }

   public int hashCode() {
      return this.fields.hashCode();
   }

   public boolean isInitialized() {
      return true;
   }

   public UnknownFieldSet.Builder newBuilderForType() {
      return newBuilder();
   }

   public UnknownFieldSet.Builder toBuilder() {
      return newBuilder().mergeFrom(this);
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

   public String toString() {
      return TextFormat.printToString(this);
   }

   public void writeAsMessageSetTo(CodedOutputStream var1) throws IOException {
      Iterator var2 = this.fields.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         ((UnknownFieldSet.Field)var3.getValue()).writeAsMessageSetExtensionTo((Integer)var3.getKey(), var1);
      }

   }

   public void writeDelimitedTo(OutputStream var1) throws IOException {
      CodedOutputStream var2 = CodedOutputStream.newInstance(var1);
      var2.writeRawVarint32(this.getSerializedSize());
      this.writeTo(var2);
      var2.flush();
   }

   public void writeTo(CodedOutputStream var1) throws IOException {
      Iterator var2 = this.fields.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         ((UnknownFieldSet.Field)var3.getValue()).writeTo((Integer)var3.getKey(), var1);
      }

   }

   public void writeTo(OutputStream var1) throws IOException {
      CodedOutputStream var2 = CodedOutputStream.newInstance(var1);
      this.writeTo(var2);
      var2.flush();
   }

   public static final class Builder implements MessageLite.Builder {
      private Map fields;
      private UnknownFieldSet.Field.Builder lastField;
      private int lastFieldNumber;

      private Builder() {
      }

      private static UnknownFieldSet.Builder create() {
         UnknownFieldSet.Builder var0 = new UnknownFieldSet.Builder();
         var0.reinitialize();
         return var0;
      }

      private UnknownFieldSet.Field.Builder getFieldBuilder(int var1) {
         UnknownFieldSet.Field.Builder var3 = this.lastField;
         if (var3 != null) {
            int var2 = this.lastFieldNumber;
            if (var1 == var2) {
               return var3;
            }

            this.addField(var2, var3.build());
         }

         if (var1 == 0) {
            return null;
         } else {
            UnknownFieldSet.Field var5 = (UnknownFieldSet.Field)this.fields.get(var1);
            this.lastFieldNumber = var1;
            UnknownFieldSet.Field.Builder var4 = UnknownFieldSet.Field.newBuilder();
            this.lastField = var4;
            if (var5 != null) {
               var4.mergeFrom(var5);
            }

            return this.lastField;
         }
      }

      private void reinitialize() {
         this.fields = Collections.emptyMap();
         this.lastFieldNumber = 0;
         this.lastField = null;
      }

      public UnknownFieldSet.Builder addField(int var1, UnknownFieldSet.Field var2) {
         if (var1 != 0) {
            if (this.lastField != null && this.lastFieldNumber == var1) {
               this.lastField = null;
               this.lastFieldNumber = 0;
            }

            if (this.fields.isEmpty()) {
               this.fields = new TreeMap();
            }

            this.fields.put(var1, var2);
            return this;
         } else {
            throw new IllegalArgumentException("Zero is not a valid field number.");
         }
      }

      public Map asMap() {
         this.getFieldBuilder(0);
         return Collections.unmodifiableMap(this.fields);
      }

      public UnknownFieldSet build() {
         this.getFieldBuilder(0);
         UnknownFieldSet var1;
         if (this.fields.isEmpty()) {
            var1 = UnknownFieldSet.getDefaultInstance();
         } else {
            var1 = new UnknownFieldSet(Collections.unmodifiableMap(this.fields));
         }

         this.fields = null;
         return var1;
      }

      public UnknownFieldSet buildPartial() {
         return this.build();
      }

      public UnknownFieldSet.Builder clear() {
         this.reinitialize();
         return this;
      }

      public UnknownFieldSet.Builder clone() {
         this.getFieldBuilder(0);
         return UnknownFieldSet.newBuilder().mergeFrom(new UnknownFieldSet(this.fields));
      }

      public UnknownFieldSet getDefaultInstanceForType() {
         return UnknownFieldSet.getDefaultInstance();
      }

      public boolean hasField(int var1) {
         if (var1 != 0) {
            return var1 == this.lastFieldNumber || this.fields.containsKey(var1);
         } else {
            throw new IllegalArgumentException("Zero is not a valid field number.");
         }
      }

      public boolean isInitialized() {
         return true;
      }

      public boolean mergeDelimitedFrom(InputStream var1) throws IOException {
         int var2 = var1.read();
         if (var2 == -1) {
            return false;
         } else {
            this.mergeFrom((InputStream)(new AbstractMessageLite.Builder.LimitedInputStream(var1, CodedInputStream.readRawVarint32(var2, var1))));
            return true;
         }
      }

      public boolean mergeDelimitedFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException {
         return this.mergeDelimitedFrom(var1);
      }

      public UnknownFieldSet.Builder mergeField(int var1, UnknownFieldSet.Field var2) {
         if (var1 != 0) {
            if (this.hasField(var1)) {
               this.getFieldBuilder(var1).mergeFrom(var2);
               return this;
            } else {
               this.addField(var1, var2);
               return this;
            }
         } else {
            throw new IllegalArgumentException("Zero is not a valid field number.");
         }
      }

      public boolean mergeFieldFrom(int var1, CodedInputStream var2) throws IOException {
         int var3 = WireFormat.getTagFieldNumber(var1);
         var1 = WireFormat.getTagWireType(var1);
         if (var1 != 0) {
            if (var1 != 1) {
               if (var1 != 2) {
                  if (var1 != 3) {
                     if (var1 != 4) {
                        if (var1 == 5) {
                           this.getFieldBuilder(var3).addFixed32(var2.readFixed32());
                           return true;
                        } else {
                           throw InvalidProtocolBufferException.invalidWireType();
                        }
                     } else {
                        return false;
                     }
                  } else {
                     UnknownFieldSet.Builder var4 = UnknownFieldSet.newBuilder();
                     var2.readGroup(var3, (MessageLite.Builder)var4, ExtensionRegistry.getEmptyRegistry());
                     this.getFieldBuilder(var3).addGroup(var4.build());
                     return true;
                  }
               } else {
                  this.getFieldBuilder(var3).addLengthDelimited(var2.readBytes());
                  return true;
               }
            } else {
               this.getFieldBuilder(var3).addFixed64(var2.readFixed64());
               return true;
            }
         } else {
            this.getFieldBuilder(var3).addVarint(var2.readInt64());
            return true;
         }
      }

      public UnknownFieldSet.Builder mergeFrom(ByteString var1) throws InvalidProtocolBufferException {
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

      public UnknownFieldSet.Builder mergeFrom(ByteString var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
         return this.mergeFrom(var1);
      }

      public UnknownFieldSet.Builder mergeFrom(CodedInputStream var1) throws IOException {
         while(true) {
            int var2 = var1.readTag();
            if (var2 != 0) {
               if (this.mergeFieldFrom(var2, var1)) {
                  continue;
               }

               return this;
            }

            return this;
         }
      }

      public UnknownFieldSet.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
         return this.mergeFrom(var1);
      }

      public UnknownFieldSet.Builder mergeFrom(UnknownFieldSet var1) {
         if (var1 != UnknownFieldSet.getDefaultInstance()) {
            Iterator var3 = var1.fields.entrySet().iterator();

            while(var3.hasNext()) {
               Entry var2 = (Entry)var3.next();
               this.mergeField((Integer)var2.getKey(), (UnknownFieldSet.Field)var2.getValue());
            }
         }

         return this;
      }

      public UnknownFieldSet.Builder mergeFrom(InputStream var1) throws IOException {
         CodedInputStream var2 = CodedInputStream.newInstance(var1);
         this.mergeFrom(var2);
         var2.checkLastTagWas(0);
         return this;
      }

      public UnknownFieldSet.Builder mergeFrom(InputStream var1, ExtensionRegistryLite var2) throws IOException {
         return this.mergeFrom(var1);
      }

      public UnknownFieldSet.Builder mergeFrom(byte[] var1) throws InvalidProtocolBufferException {
         try {
            CodedInputStream var4 = CodedInputStream.newInstance(var1);
            this.mergeFrom(var4);
            var4.checkLastTagWas(0);
            return this;
         } catch (InvalidProtocolBufferException var2) {
            throw var2;
         } catch (IOException var3) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", var3);
         }
      }

      public UnknownFieldSet.Builder mergeFrom(byte[] var1, int var2, int var3) throws InvalidProtocolBufferException {
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

      public UnknownFieldSet.Builder mergeFrom(byte[] var1, int var2, int var3, ExtensionRegistryLite var4) throws InvalidProtocolBufferException {
         return this.mergeFrom(var1, var2, var3);
      }

      public UnknownFieldSet.Builder mergeFrom(byte[] var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
         return this.mergeFrom(var1);
      }

      public UnknownFieldSet.Builder mergeVarintField(int var1, int var2) {
         if (var1 != 0) {
            this.getFieldBuilder(var1).addVarint((long)var2);
            return this;
         } else {
            throw new IllegalArgumentException("Zero is not a valid field number.");
         }
      }
   }

   public static final class Field {
      private static final UnknownFieldSet.Field fieldDefaultInstance = newBuilder().build();
      private List fixed32;
      private List fixed64;
      private List group;
      private List lengthDelimited;
      private List varint;

      private Field() {
      }

      // $FF: synthetic method
      Field(Object var1) {
         this();
      }

      public static UnknownFieldSet.Field getDefaultInstance() {
         return fieldDefaultInstance;
      }

      private Object[] getIdentityArray() {
         return new Object[]{this.varint, this.fixed32, this.fixed64, this.lengthDelimited, this.group};
      }

      public static UnknownFieldSet.Field.Builder newBuilder() {
         return UnknownFieldSet.Field.Builder.create();
      }

      public static UnknownFieldSet.Field.Builder newBuilder(UnknownFieldSet.Field var0) {
         return newBuilder().mergeFrom(var0);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            return !(var1 instanceof UnknownFieldSet.Field) ? false : Arrays.equals(this.getIdentityArray(), ((UnknownFieldSet.Field)var1).getIdentityArray());
         }
      }

      public List getFixed32List() {
         return this.fixed32;
      }

      public List getFixed64List() {
         return this.fixed64;
      }

      public List getGroupList() {
         return this.group;
      }

      public List getLengthDelimitedList() {
         return this.lengthDelimited;
      }

      public int getSerializedSize(int var1) {
         int var2 = 0;

         Iterator var3;
         for(var3 = this.varint.iterator(); var3.hasNext(); var2 += CodedOutputStream.computeUInt64Size(var1, (Long)var3.next())) {
         }

         for(var3 = this.fixed32.iterator(); var3.hasNext(); var2 += CodedOutputStream.computeFixed32Size(var1, (Integer)var3.next())) {
         }

         for(var3 = this.fixed64.iterator(); var3.hasNext(); var2 += CodedOutputStream.computeFixed64Size(var1, (Long)var3.next())) {
         }

         for(var3 = this.lengthDelimited.iterator(); var3.hasNext(); var2 += CodedOutputStream.computeBytesSize(var1, (ByteString)var3.next())) {
         }

         for(var3 = this.group.iterator(); var3.hasNext(); var2 += CodedOutputStream.computeGroupSize(var1, (UnknownFieldSet)var3.next())) {
         }

         return var2;
      }

      public int getSerializedSizeAsMessageSetExtension(int var1) {
         int var2 = 0;

         for(Iterator var3 = this.lengthDelimited.iterator(); var3.hasNext(); var2 += CodedOutputStream.computeRawMessageSetExtensionSize(var1, (ByteString)var3.next())) {
         }

         return var2;
      }

      public List getVarintList() {
         return this.varint;
      }

      public int hashCode() {
         return Arrays.hashCode(this.getIdentityArray());
      }

      public void writeAsMessageSetExtensionTo(int var1, CodedOutputStream var2) throws IOException {
         Iterator var3 = this.lengthDelimited.iterator();

         while(var3.hasNext()) {
            var2.writeRawMessageSetExtension(var1, (ByteString)var3.next());
         }

      }

      public void writeTo(int var1, CodedOutputStream var2) throws IOException {
         Iterator var3 = this.varint.iterator();

         while(var3.hasNext()) {
            var2.writeUInt64(var1, (Long)var3.next());
         }

         var3 = this.fixed32.iterator();

         while(var3.hasNext()) {
            var2.writeFixed32(var1, (Integer)var3.next());
         }

         var3 = this.fixed64.iterator();

         while(var3.hasNext()) {
            var2.writeFixed64(var1, (Long)var3.next());
         }

         var3 = this.lengthDelimited.iterator();

         while(var3.hasNext()) {
            var2.writeBytes(var1, (ByteString)var3.next());
         }

         var3 = this.group.iterator();

         while(var3.hasNext()) {
            var2.writeGroup(var1, (UnknownFieldSet)var3.next());
         }

      }

      public static final class Builder {
         private UnknownFieldSet.Field result;

         private Builder() {
         }

         private static UnknownFieldSet.Field.Builder create() {
            UnknownFieldSet.Field.Builder var0 = new UnknownFieldSet.Field.Builder();
            var0.result = new UnknownFieldSet.Field();
            return var0;
         }

         public UnknownFieldSet.Field.Builder addFixed32(int var1) {
            if (this.result.fixed32 == null) {
               this.result.fixed32 = new ArrayList();
            }

            this.result.fixed32.add(var1);
            return this;
         }

         public UnknownFieldSet.Field.Builder addFixed64(long var1) {
            if (this.result.fixed64 == null) {
               this.result.fixed64 = new ArrayList();
            }

            this.result.fixed64.add(var1);
            return this;
         }

         public UnknownFieldSet.Field.Builder addGroup(UnknownFieldSet var1) {
            if (this.result.group == null) {
               this.result.group = new ArrayList();
            }

            this.result.group.add(var1);
            return this;
         }

         public UnknownFieldSet.Field.Builder addLengthDelimited(ByteString var1) {
            if (this.result.lengthDelimited == null) {
               this.result.lengthDelimited = new ArrayList();
            }

            this.result.lengthDelimited.add(var1);
            return this;
         }

         public UnknownFieldSet.Field.Builder addVarint(long var1) {
            if (this.result.varint == null) {
               this.result.varint = new ArrayList();
            }

            this.result.varint.add(var1);
            return this;
         }

         public UnknownFieldSet.Field build() {
            UnknownFieldSet.Field var1;
            if (this.result.varint == null) {
               this.result.varint = Collections.emptyList();
            } else {
               var1 = this.result;
               var1.varint = Collections.unmodifiableList(var1.varint);
            }

            if (this.result.fixed32 == null) {
               this.result.fixed32 = Collections.emptyList();
            } else {
               var1 = this.result;
               var1.fixed32 = Collections.unmodifiableList(var1.fixed32);
            }

            if (this.result.fixed64 == null) {
               this.result.fixed64 = Collections.emptyList();
            } else {
               var1 = this.result;
               var1.fixed64 = Collections.unmodifiableList(var1.fixed64);
            }

            if (this.result.lengthDelimited == null) {
               this.result.lengthDelimited = Collections.emptyList();
            } else {
               var1 = this.result;
               var1.lengthDelimited = Collections.unmodifiableList(var1.lengthDelimited);
            }

            if (this.result.group == null) {
               this.result.group = Collections.emptyList();
            } else {
               var1 = this.result;
               var1.group = Collections.unmodifiableList(var1.group);
            }

            var1 = this.result;
            this.result = null;
            return var1;
         }

         public UnknownFieldSet.Field.Builder clear() {
            this.result = new UnknownFieldSet.Field();
            return this;
         }

         public UnknownFieldSet.Field.Builder mergeFrom(UnknownFieldSet.Field var1) {
            if (!var1.varint.isEmpty()) {
               if (this.result.varint == null) {
                  this.result.varint = new ArrayList();
               }

               this.result.varint.addAll(var1.varint);
            }

            if (!var1.fixed32.isEmpty()) {
               if (this.result.fixed32 == null) {
                  this.result.fixed32 = new ArrayList();
               }

               this.result.fixed32.addAll(var1.fixed32);
            }

            if (!var1.fixed64.isEmpty()) {
               if (this.result.fixed64 == null) {
                  this.result.fixed64 = new ArrayList();
               }

               this.result.fixed64.addAll(var1.fixed64);
            }

            if (!var1.lengthDelimited.isEmpty()) {
               if (this.result.lengthDelimited == null) {
                  this.result.lengthDelimited = new ArrayList();
               }

               this.result.lengthDelimited.addAll(var1.lengthDelimited);
            }

            if (!var1.group.isEmpty()) {
               if (this.result.group == null) {
                  this.result.group = new ArrayList();
               }

               this.result.group.addAll(var1.group);
            }

            return this;
         }
      }
   }

   public static final class Parser extends AbstractParser {
      public UnknownFieldSet parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
         UnknownFieldSet.Builder var5 = UnknownFieldSet.newBuilder();

         try {
            var5.mergeFrom(var1);
         } catch (InvalidProtocolBufferException var3) {
            throw var3.setUnfinishedMessage(var5.buildPartial());
         } catch (IOException var4) {
            throw (new InvalidProtocolBufferException(var4.getMessage())).setUnfinishedMessage(var5.buildPartial());
         }

         return var5.buildPartial();
      }
   }
}
