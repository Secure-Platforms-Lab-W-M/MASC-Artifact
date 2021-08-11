package com.google.protobuf;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class GeneratedMessageLite extends AbstractMessageLite implements Serializable {
   private static final long serialVersionUID = 1L;

   protected GeneratedMessageLite() {
   }

   protected GeneratedMessageLite(GeneratedMessageLite.Builder var1) {
   }

   public static GeneratedMessageLite.GeneratedExtension newRepeatedGeneratedExtension(MessageLite var0, MessageLite var1, Internal.EnumLiteMap var2, int var3, WireFormat.FieldType var4, boolean var5) {
      return new GeneratedMessageLite.GeneratedExtension(var0, Collections.emptyList(), var1, new GeneratedMessageLite.ExtensionDescriptor(var2, var3, var4, true, var5));
   }

   public static GeneratedMessageLite.GeneratedExtension newSingularGeneratedExtension(MessageLite var0, Object var1, MessageLite var2, Internal.EnumLiteMap var3, int var4, WireFormat.FieldType var5) {
      return new GeneratedMessageLite.GeneratedExtension(var0, var1, var2, new GeneratedMessageLite.ExtensionDescriptor(var3, var4, var5, false, false));
   }

   private static boolean parseUnknownField(FieldSet var0, MessageLite var1, CodedInputStream var2, ExtensionRegistryLite var3, int var4) throws IOException {
      int var7 = WireFormat.getTagWireType(var4);
      GeneratedMessageLite.GeneratedExtension var9 = var3.findLiteExtensionByNumber(var1, WireFormat.getTagFieldNumber(var4));
      boolean var5 = false;
      boolean var6 = false;
      if (var9 == null) {
         var5 = true;
      } else if (var7 == FieldSet.getWireFormatForFieldType(var9.descriptor.getLiteType(), false)) {
         var6 = false;
      } else if (var9.descriptor.isRepeated && var9.descriptor.type.isPackable() && var7 == FieldSet.getWireFormatForFieldType(var9.descriptor.getLiteType(), true)) {
         var6 = true;
      } else {
         var5 = true;
      }

      if (var5) {
         return var2.skipField(var4);
      } else {
         Object var11;
         if (!var6) {
            var4 = null.$SwitchMap$com$google$protobuf$WireFormat$JavaType[var9.descriptor.getLiteJavaType().ordinal()];
            if (var4 != 1) {
               if (var4 != 2) {
                  var11 = FieldSet.readPrimitiveField(var2, var9.descriptor.getLiteType());
               } else {
                  var4 = var2.readEnum();
                  var11 = var9.descriptor.getEnumType().findValueByNumber(var4);
                  if (var11 == null) {
                     return true;
                  }
               }
            } else {
               MessageLite.Builder var8 = null;
               MessageLite.Builder var13 = var8;
               if (!var9.descriptor.isRepeated()) {
                  MessageLite var10 = (MessageLite)var0.getField(var9.descriptor);
                  var13 = var8;
                  if (var10 != null) {
                     var13 = var10.toBuilder();
                  }
               }

               var8 = var13;
               if (var13 == null) {
                  var8 = var9.messageDefaultInstance.newBuilderForType();
               }

               if (var9.descriptor.getLiteType() == WireFormat.FieldType.GROUP) {
                  var2.readGroup(var9.getNumber(), var8, var3);
               } else {
                  var2.readMessage(var8, var3);
               }

               var11 = var8.build();
            }

            if (var9.descriptor.isRepeated()) {
               var0.addRepeatedField(var9.descriptor, var11);
               return true;
            } else {
               var0.setField(var9.descriptor, var11);
               return true;
            }
         } else {
            var4 = var2.pushLimit(var2.readRawVarint32());
            if (var9.descriptor.getLiteType() == WireFormat.FieldType.ENUM) {
               while(var2.getBytesUntilLimit() > 0) {
                  int var14 = var2.readEnum();
                  Internal.EnumLite var12 = var9.descriptor.getEnumType().findValueByNumber(var14);
                  if (var12 == null) {
                     return true;
                  }

                  var0.addRepeatedField(var9.descriptor, var12);
               }
            } else {
               while(var2.getBytesUntilLimit() > 0) {
                  var11 = FieldSet.readPrimitiveField(var2, var9.descriptor.getLiteType());
                  var0.addRepeatedField(var9.descriptor, var11);
               }
            }

            var2.popLimit(var4);
            return true;
         }
      }
   }

   public Parser getParserForType() {
      throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
   }

   protected void makeExtensionsImmutable() {
   }

   protected boolean parseUnknownField(CodedInputStream var1, ExtensionRegistryLite var2, int var3) throws IOException {
      return var1.skipField(var3);
   }

   protected Object writeReplace() throws ObjectStreamException {
      return new GeneratedMessageLite.SerializedForm(this);
   }

   public abstract static class Builder extends AbstractMessageLite.Builder {
      protected Builder() {
      }

      public GeneratedMessageLite.Builder clear() {
         return this;
      }

      public GeneratedMessageLite.Builder clone() {
         throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
      }

      public abstract GeneratedMessageLite getDefaultInstanceForType();

      public abstract GeneratedMessageLite.Builder mergeFrom(GeneratedMessageLite var1);

      protected boolean parseUnknownField(CodedInputStream var1, ExtensionRegistryLite var2, int var3) throws IOException {
         return var1.skipField(var3);
      }
   }

   public abstract static class ExtendableBuilder extends GeneratedMessageLite.Builder implements GeneratedMessageLite.ExtendableMessageOrBuilder {
      private FieldSet extensions = FieldSet.emptySet();
      private boolean extensionsIsMutable;

      protected ExtendableBuilder() {
      }

      private FieldSet buildExtensions() {
         this.extensions.makeImmutable();
         this.extensionsIsMutable = false;
         return this.extensions;
      }

      private void ensureExtensionsIsMutable() {
         if (!this.extensionsIsMutable) {
            this.extensions = this.extensions.clone();
            this.extensionsIsMutable = true;
         }

      }

      private void verifyExtensionContainingType(GeneratedMessageLite.GeneratedExtension var1) {
         if (var1.getContainingTypeDefaultInstance() != this.getDefaultInstanceForType()) {
            throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
         }
      }

      public final GeneratedMessageLite.ExtendableBuilder addExtension(GeneratedMessageLite.GeneratedExtension var1, Object var2) {
         this.verifyExtensionContainingType(var1);
         this.ensureExtensionsIsMutable();
         this.extensions.addRepeatedField(var1.descriptor, var2);
         return this;
      }

      public GeneratedMessageLite.ExtendableBuilder clear() {
         this.extensions.clear();
         this.extensionsIsMutable = false;
         return (GeneratedMessageLite.ExtendableBuilder)super.clear();
      }

      public final GeneratedMessageLite.ExtendableBuilder clearExtension(GeneratedMessageLite.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         this.ensureExtensionsIsMutable();
         this.extensions.clearField(var1.descriptor);
         return this;
      }

      public GeneratedMessageLite.ExtendableBuilder clone() {
         throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
      }

      protected boolean extensionsAreInitialized() {
         return this.extensions.isInitialized();
      }

      public final Object getExtension(GeneratedMessageLite.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         Object var2 = this.extensions.getField(var1.descriptor);
         return var2 == null ? var1.defaultValue : var2;
      }

      public final Object getExtension(GeneratedMessageLite.GeneratedExtension var1, int var2) {
         this.verifyExtensionContainingType(var1);
         return this.extensions.getRepeatedField(var1.descriptor, var2);
      }

      public final int getExtensionCount(GeneratedMessageLite.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         return this.extensions.getRepeatedFieldCount(var1.descriptor);
      }

      public final boolean hasExtension(GeneratedMessageLite.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         return this.extensions.hasField(var1.descriptor);
      }

      protected final void mergeExtensionFields(GeneratedMessageLite.ExtendableMessage var1) {
         this.ensureExtensionsIsMutable();
         this.extensions.mergeFrom(var1.extensions);
      }

      protected boolean parseUnknownField(CodedInputStream var1, ExtensionRegistryLite var2, int var3) throws IOException {
         this.ensureExtensionsIsMutable();
         return GeneratedMessageLite.parseUnknownField(this.extensions, this.getDefaultInstanceForType(), var1, var2, var3);
      }

      public final GeneratedMessageLite.ExtendableBuilder setExtension(GeneratedMessageLite.GeneratedExtension var1, int var2, Object var3) {
         this.verifyExtensionContainingType(var1);
         this.ensureExtensionsIsMutable();
         this.extensions.setRepeatedField(var1.descriptor, var2, var3);
         return this;
      }

      public final GeneratedMessageLite.ExtendableBuilder setExtension(GeneratedMessageLite.GeneratedExtension var1, Object var2) {
         this.verifyExtensionContainingType(var1);
         this.ensureExtensionsIsMutable();
         this.extensions.setField(var1.descriptor, var2);
         return this;
      }
   }

   public abstract static class ExtendableMessage extends GeneratedMessageLite implements GeneratedMessageLite.ExtendableMessageOrBuilder {
      private final FieldSet extensions;

      protected ExtendableMessage() {
         this.extensions = FieldSet.newFieldSet();
      }

      protected ExtendableMessage(GeneratedMessageLite.ExtendableBuilder var1) {
         this.extensions = var1.buildExtensions();
      }

      private void verifyExtensionContainingType(GeneratedMessageLite.GeneratedExtension var1) {
         if (var1.getContainingTypeDefaultInstance() != this.getDefaultInstanceForType()) {
            throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
         }
      }

      protected boolean extensionsAreInitialized() {
         return this.extensions.isInitialized();
      }

      protected int extensionsSerializedSize() {
         return this.extensions.getSerializedSize();
      }

      protected int extensionsSerializedSizeAsMessageSet() {
         return this.extensions.getMessageSetSerializedSize();
      }

      public final Object getExtension(GeneratedMessageLite.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         Object var2 = this.extensions.getField(var1.descriptor);
         return var2 == null ? var1.defaultValue : var2;
      }

      public final Object getExtension(GeneratedMessageLite.GeneratedExtension var1, int var2) {
         this.verifyExtensionContainingType(var1);
         return this.extensions.getRepeatedField(var1.descriptor, var2);
      }

      public final int getExtensionCount(GeneratedMessageLite.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         return this.extensions.getRepeatedFieldCount(var1.descriptor);
      }

      public final boolean hasExtension(GeneratedMessageLite.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         return this.extensions.hasField(var1.descriptor);
      }

      protected void makeExtensionsImmutable() {
         this.extensions.makeImmutable();
      }

      protected GeneratedMessageLite.ExtendableMessage.ExtensionWriter newExtensionWriter() {
         return new GeneratedMessageLite.ExtendableMessage.ExtensionWriter(false);
      }

      protected GeneratedMessageLite.ExtendableMessage.ExtensionWriter newMessageSetExtensionWriter() {
         return new GeneratedMessageLite.ExtendableMessage.ExtensionWriter(true);
      }

      protected boolean parseUnknownField(CodedInputStream var1, ExtensionRegistryLite var2, int var3) throws IOException {
         return GeneratedMessageLite.parseUnknownField(this.extensions, this.getDefaultInstanceForType(), var1, var2, var3);
      }

      protected class ExtensionWriter {
         private final Iterator iter;
         private final boolean messageSetWireFormat;
         private Entry next;

         private ExtensionWriter(boolean var2) {
            Iterator var3 = ExtendableMessage.this.extensions.iterator();
            this.iter = var3;
            if (var3.hasNext()) {
               this.next = (Entry)this.iter.next();
            }

            this.messageSetWireFormat = var2;
         }

         // $FF: synthetic method
         ExtensionWriter(boolean var2, Object var3) {
            this(var2);
         }

         public void writeUntil(int var1, CodedOutputStream var2) throws IOException {
            while(true) {
               Entry var3 = this.next;
               if (var3 == null || ((GeneratedMessageLite.ExtensionDescriptor)var3.getKey()).getNumber() >= var1) {
                  return;
               }

               GeneratedMessageLite.ExtensionDescriptor var4 = (GeneratedMessageLite.ExtensionDescriptor)this.next.getKey();
               if (this.messageSetWireFormat && var4.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !var4.isRepeated()) {
                  var2.writeMessageSetExtension(var4.getNumber(), (MessageLite)this.next.getValue());
               } else {
                  FieldSet.writeField(var4, this.next.getValue(), var2);
               }

               if (this.iter.hasNext()) {
                  this.next = (Entry)this.iter.next();
               } else {
                  this.next = null;
               }
            }
         }
      }
   }

   public interface ExtendableMessageOrBuilder extends MessageLiteOrBuilder {
      Object getExtension(GeneratedMessageLite.GeneratedExtension var1);

      Object getExtension(GeneratedMessageLite.GeneratedExtension var1, int var2);

      int getExtensionCount(GeneratedMessageLite.GeneratedExtension var1);

      boolean hasExtension(GeneratedMessageLite.GeneratedExtension var1);
   }

   private static final class ExtensionDescriptor implements FieldSet.FieldDescriptorLite {
      private final Internal.EnumLiteMap enumTypeMap;
      private final boolean isPacked;
      private final boolean isRepeated;
      private final int number;
      private final WireFormat.FieldType type;

      private ExtensionDescriptor(Internal.EnumLiteMap var1, int var2, WireFormat.FieldType var3, boolean var4, boolean var5) {
         this.enumTypeMap = var1;
         this.number = var2;
         this.type = var3;
         this.isRepeated = var4;
         this.isPacked = var5;
      }

      // $FF: synthetic method
      ExtensionDescriptor(Internal.EnumLiteMap var1, int var2, WireFormat.FieldType var3, boolean var4, boolean var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }

      public int compareTo(GeneratedMessageLite.ExtensionDescriptor var1) {
         return this.number - var1.number;
      }

      public Internal.EnumLiteMap getEnumType() {
         return this.enumTypeMap;
      }

      public WireFormat.JavaType getLiteJavaType() {
         return this.type.getJavaType();
      }

      public WireFormat.FieldType getLiteType() {
         return this.type;
      }

      public int getNumber() {
         return this.number;
      }

      public MessageLite.Builder internalMergeFrom(MessageLite.Builder var1, MessageLite var2) {
         return ((GeneratedMessageLite.Builder)var1).mergeFrom((GeneratedMessageLite)var2);
      }

      public boolean isPacked() {
         return this.isPacked;
      }

      public boolean isRepeated() {
         return this.isRepeated;
      }
   }

   public static final class GeneratedExtension {
      private final MessageLite containingTypeDefaultInstance;
      private final Object defaultValue;
      private final GeneratedMessageLite.ExtensionDescriptor descriptor;
      private final MessageLite messageDefaultInstance;

      private GeneratedExtension(MessageLite var1, Object var2, MessageLite var3, GeneratedMessageLite.ExtensionDescriptor var4) {
         if (var1 != null) {
            if (var4.getLiteType() == WireFormat.FieldType.MESSAGE && var3 == null) {
               throw new IllegalArgumentException("Null messageDefaultInstance");
            } else {
               this.containingTypeDefaultInstance = var1;
               this.defaultValue = var2;
               this.messageDefaultInstance = var3;
               this.descriptor = var4;
            }
         } else {
            throw new IllegalArgumentException("Null containingTypeDefaultInstance");
         }
      }

      // $FF: synthetic method
      GeneratedExtension(MessageLite var1, Object var2, MessageLite var3, GeneratedMessageLite.ExtensionDescriptor var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      public MessageLite getContainingTypeDefaultInstance() {
         return this.containingTypeDefaultInstance;
      }

      public MessageLite getMessageDefaultInstance() {
         return this.messageDefaultInstance;
      }

      public int getNumber() {
         return this.descriptor.getNumber();
      }
   }

   static final class SerializedForm implements Serializable {
      private static final long serialVersionUID = 0L;
      private byte[] asBytes;
      private String messageClassName;

      SerializedForm(MessageLite var1) {
         this.messageClassName = var1.getClass().getName();
         this.asBytes = var1.toByteArray();
      }

      protected Object readResolve() throws ObjectStreamException {
         try {
            MessageLite.Builder var1 = (MessageLite.Builder)Class.forName(this.messageClassName).getMethod("newBuilder").invoke((Object)null);
            var1.mergeFrom(this.asBytes);
            MessageLite var7 = var1.buildPartial();
            return var7;
         } catch (ClassNotFoundException var2) {
            throw new RuntimeException("Unable to find proto buffer class", var2);
         } catch (NoSuchMethodException var3) {
            throw new RuntimeException("Unable to find newBuilder method", var3);
         } catch (IllegalAccessException var4) {
            throw new RuntimeException("Unable to call newBuilder method", var4);
         } catch (InvocationTargetException var5) {
            throw new RuntimeException("Error calling newBuilder", var5.getCause());
         } catch (InvalidProtocolBufferException var6) {
            throw new RuntimeException("Unable to understand proto buffer", var6);
         }
      }
   }
}
