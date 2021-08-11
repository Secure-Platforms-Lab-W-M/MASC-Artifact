package com.google.protobuf;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public abstract class GeneratedMessage extends AbstractMessage implements Serializable {
   protected static boolean alwaysUseFieldBuilders = false;
   private static final long serialVersionUID = 1L;

   protected GeneratedMessage() {
   }

   protected GeneratedMessage(GeneratedMessage.Builder var1) {
   }

   static void enableAlwaysUseFieldBuildersForTesting() {
      alwaysUseFieldBuilders = true;
   }

   private Map getAllFieldsMutable() {
      TreeMap var1 = new TreeMap();
      Iterator var2 = this.internalGetFieldAccessorTable().descriptor.getFields().iterator();

      while(var2.hasNext()) {
         Descriptors.FieldDescriptor var3 = (Descriptors.FieldDescriptor)var2.next();
         if (var3.isRepeated()) {
            List var4 = (List)this.getField(var3);
            if (!var4.isEmpty()) {
               var1.put(var3, var4);
            }
         } else if (this.hasField(var3)) {
            var1.put(var3, this.getField(var3));
         }
      }

      return var1;
   }

   private static Method getMethodOrDie(Class var0, String var1, Class... var2) {
      try {
         Method var5 = var0.getMethod(var1, var2);
         return var5;
      } catch (NoSuchMethodException var4) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Generated message class \"");
         var3.append(var0.getName());
         var3.append("\" missing method \"");
         var3.append(var1);
         var3.append("\".");
         throw new RuntimeException(var3.toString(), var4);
      }
   }

   private static Object invokeOrDie(Method var0, Object var1, Object... var2) {
      try {
         Object var6 = var0.invoke(var1, var2);
         return var6;
      } catch (IllegalAccessException var3) {
         throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", var3);
      } catch (InvocationTargetException var4) {
         Throwable var5 = var4.getCause();
         if (!(var5 instanceof RuntimeException)) {
            if (var5 instanceof Error) {
               throw (Error)var5;
            } else {
               throw new RuntimeException("Unexpected exception thrown by generated accessor method.", var5);
            }
         } else {
            throw (RuntimeException)var5;
         }
      }
   }

   public static GeneratedMessage.GeneratedExtension newFileScopedGeneratedExtension(Class var0, Message var1) {
      return new GeneratedMessage.GeneratedExtension((GeneratedMessage.ExtensionDescriptorRetriever)null, var0, var1);
   }

   public static GeneratedMessage.GeneratedExtension newMessageScopedGeneratedExtension(final Message var0, final int var1, Class var2, Message var3) {
      return new GeneratedMessage.GeneratedExtension(new GeneratedMessage.ExtensionDescriptorRetriever() {
         public Descriptors.FieldDescriptor getDescriptor() {
            return (Descriptors.FieldDescriptor)var0.getDescriptorForType().getExtensions().get(var1);
         }
      }, var2, var3);
   }

   public Map getAllFields() {
      return Collections.unmodifiableMap(this.getAllFieldsMutable());
   }

   public Descriptors.Descriptor getDescriptorForType() {
      return this.internalGetFieldAccessorTable().descriptor;
   }

   public Object getField(Descriptors.FieldDescriptor var1) {
      return this.internalGetFieldAccessorTable().getField(var1).get(this);
   }

   public Parser getParserForType() {
      throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
   }

   public Object getRepeatedField(Descriptors.FieldDescriptor var1, int var2) {
      return this.internalGetFieldAccessorTable().getField(var1).getRepeated(this, var2);
   }

   public int getRepeatedFieldCount(Descriptors.FieldDescriptor var1) {
      return this.internalGetFieldAccessorTable().getField(var1).getRepeatedCount(this);
   }

   public UnknownFieldSet getUnknownFields() {
      throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
   }

   public boolean hasField(Descriptors.FieldDescriptor var1) {
      return this.internalGetFieldAccessorTable().getField(var1).has(this);
   }

   protected abstract GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable();

   public boolean isInitialized() {
      Iterator var1 = this.getDescriptorForType().getFields().iterator();

      while(true) {
         while(true) {
            Descriptors.FieldDescriptor var2;
            do {
               if (!var1.hasNext()) {
                  return true;
               }

               var2 = (Descriptors.FieldDescriptor)var1.next();
               if (var2.isRequired() && !this.hasField(var2)) {
                  return false;
               }
            } while(var2.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE);

            if (var2.isRepeated()) {
               Iterator var3 = ((List)this.getField(var2)).iterator();

               while(var3.hasNext()) {
                  if (!((Message)var3.next()).isInitialized()) {
                     return false;
                  }
               }
            } else if (this.hasField(var2) && !((Message)this.getField(var2)).isInitialized()) {
               return false;
            }
         }
      }
   }

   protected void makeExtensionsImmutable() {
   }

   protected abstract Message.Builder newBuilderForType(GeneratedMessage.BuilderParent var1);

   protected boolean parseUnknownField(CodedInputStream var1, UnknownFieldSet.Builder var2, ExtensionRegistryLite var3, int var4) throws IOException {
      return var2.mergeFieldFrom(var4, var1);
   }

   protected Object writeReplace() throws ObjectStreamException {
      return new GeneratedMessageLite.SerializedForm(this);
   }

   public abstract static class Builder extends AbstractMessage.Builder {
      private GeneratedMessage.BuilderParent builderParent;
      private boolean isClean;
      private GeneratedMessage.Builder.BuilderParentImpl meAsParent;
      private UnknownFieldSet unknownFields;

      protected Builder() {
         this((GeneratedMessage.BuilderParent)null);
      }

      protected Builder(GeneratedMessage.BuilderParent var1) {
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
         this.builderParent = var1;
      }

      private Map getAllFieldsMutable() {
         TreeMap var1 = new TreeMap();
         Iterator var2 = this.internalGetFieldAccessorTable().descriptor.getFields().iterator();

         while(var2.hasNext()) {
            Descriptors.FieldDescriptor var3 = (Descriptors.FieldDescriptor)var2.next();
            if (var3.isRepeated()) {
               List var4 = (List)this.getField(var3);
               if (!var4.isEmpty()) {
                  var1.put(var3, var4);
               }
            } else if (this.hasField(var3)) {
               var1.put(var3, this.getField(var3));
            }
         }

         return var1;
      }

      public GeneratedMessage.Builder addRepeatedField(Descriptors.FieldDescriptor var1, Object var2) {
         this.internalGetFieldAccessorTable().getField(var1).addRepeated(this, var2);
         return this;
      }

      public GeneratedMessage.Builder clear() {
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
         this.onChanged();
         return this;
      }

      public GeneratedMessage.Builder clearField(Descriptors.FieldDescriptor var1) {
         this.internalGetFieldAccessorTable().getField(var1).clear(this);
         return this;
      }

      public GeneratedMessage.Builder clone() {
         throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
      }

      void dispose() {
         this.builderParent = null;
      }

      public Map getAllFields() {
         return Collections.unmodifiableMap(this.getAllFieldsMutable());
      }

      public Descriptors.Descriptor getDescriptorForType() {
         return this.internalGetFieldAccessorTable().descriptor;
      }

      public Object getField(Descriptors.FieldDescriptor var1) {
         Object var2 = this.internalGetFieldAccessorTable().getField(var1).get(this);
         return var1.isRepeated() ? Collections.unmodifiableList((List)var2) : var2;
      }

      public Message.Builder getFieldBuilder(Descriptors.FieldDescriptor var1) {
         return this.internalGetFieldAccessorTable().getField(var1).getBuilder(this);
      }

      protected GeneratedMessage.BuilderParent getParentForChildren() {
         if (this.meAsParent == null) {
            this.meAsParent = new GeneratedMessage.Builder.BuilderParentImpl();
         }

         return this.meAsParent;
      }

      public Object getRepeatedField(Descriptors.FieldDescriptor var1, int var2) {
         return this.internalGetFieldAccessorTable().getField(var1).getRepeated(this, var2);
      }

      public int getRepeatedFieldCount(Descriptors.FieldDescriptor var1) {
         return this.internalGetFieldAccessorTable().getField(var1).getRepeatedCount(this);
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasField(Descriptors.FieldDescriptor var1) {
         return this.internalGetFieldAccessorTable().getField(var1).has(this);
      }

      protected abstract GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable();

      protected boolean isClean() {
         return this.isClean;
      }

      public boolean isInitialized() {
         Iterator var1 = this.getDescriptorForType().getFields().iterator();

         while(true) {
            while(true) {
               Descriptors.FieldDescriptor var2;
               do {
                  if (!var1.hasNext()) {
                     return true;
                  }

                  var2 = (Descriptors.FieldDescriptor)var1.next();
                  if (var2.isRequired() && !this.hasField(var2)) {
                     return false;
                  }
               } while(var2.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE);

               if (var2.isRepeated()) {
                  Iterator var3 = ((List)this.getField(var2)).iterator();

                  while(var3.hasNext()) {
                     if (!((Message)var3.next()).isInitialized()) {
                        return false;
                     }
                  }
               } else if (this.hasField(var2) && !((Message)this.getField(var2)).isInitialized()) {
                  return false;
               }
            }
         }
      }

      protected void markClean() {
         this.isClean = true;
      }

      public final GeneratedMessage.Builder mergeUnknownFields(UnknownFieldSet var1) {
         this.unknownFields = UnknownFieldSet.newBuilder(this.unknownFields).mergeFrom(var1).build();
         this.onChanged();
         return this;
      }

      public Message.Builder newBuilderForField(Descriptors.FieldDescriptor var1) {
         return this.internalGetFieldAccessorTable().getField(var1).newBuilder();
      }

      protected void onBuilt() {
         if (this.builderParent != null) {
            this.markClean();
         }

      }

      protected final void onChanged() {
         if (this.isClean) {
            GeneratedMessage.BuilderParent var1 = this.builderParent;
            if (var1 != null) {
               var1.markDirty();
               this.isClean = false;
            }
         }

      }

      protected boolean parseUnknownField(CodedInputStream var1, UnknownFieldSet.Builder var2, ExtensionRegistryLite var3, int var4) throws IOException {
         return var2.mergeFieldFrom(var4, var1);
      }

      public GeneratedMessage.Builder setField(Descriptors.FieldDescriptor var1, Object var2) {
         this.internalGetFieldAccessorTable().getField(var1).set(this, var2);
         return this;
      }

      public GeneratedMessage.Builder setRepeatedField(Descriptors.FieldDescriptor var1, int var2, Object var3) {
         this.internalGetFieldAccessorTable().getField(var1).setRepeated(this, var2, var3);
         return this;
      }

      public final GeneratedMessage.Builder setUnknownFields(UnknownFieldSet var1) {
         this.unknownFields = var1;
         this.onChanged();
         return this;
      }

      private class BuilderParentImpl implements GeneratedMessage.BuilderParent {
         private BuilderParentImpl() {
         }

         // $FF: synthetic method
         BuilderParentImpl(Object var2) {
            this();
         }

         public void markDirty() {
            Builder.this.onChanged();
         }
      }
   }

   protected interface BuilderParent {
      void markDirty();
   }

   public abstract static class ExtendableBuilder extends GeneratedMessage.Builder implements GeneratedMessage.ExtendableMessageOrBuilder {
      private FieldSet extensions = FieldSet.emptySet();

      protected ExtendableBuilder() {
      }

      protected ExtendableBuilder(GeneratedMessage.BuilderParent var1) {
         super(var1);
      }

      private FieldSet buildExtensions() {
         this.extensions.makeImmutable();
         return this.extensions;
      }

      private void ensureExtensionsIsMutable() {
         if (this.extensions.isImmutable()) {
            this.extensions = this.extensions.clone();
         }

      }

      private void verifyContainingType(Descriptors.FieldDescriptor var1) {
         if (var1.getContainingType() != this.getDescriptorForType()) {
            throw new IllegalArgumentException("FieldDescriptor does not match message type.");
         }
      }

      private void verifyExtensionContainingType(GeneratedMessage.GeneratedExtension var1) {
         if (var1.getDescriptor().getContainingType() != this.getDescriptorForType()) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Extension is for type \"");
            var2.append(var1.getDescriptor().getContainingType().getFullName());
            var2.append("\" which does not match message type \"");
            var2.append(this.getDescriptorForType().getFullName());
            var2.append("\".");
            throw new IllegalArgumentException(var2.toString());
         }
      }

      public final GeneratedMessage.ExtendableBuilder addExtension(GeneratedMessage.GeneratedExtension var1, Object var2) {
         this.verifyExtensionContainingType(var1);
         this.ensureExtensionsIsMutable();
         Descriptors.FieldDescriptor var3 = var1.getDescriptor();
         this.extensions.addRepeatedField(var3, var1.singularToReflectionType(var2));
         this.onChanged();
         return this;
      }

      public GeneratedMessage.ExtendableBuilder addRepeatedField(Descriptors.FieldDescriptor var1, Object var2) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            this.ensureExtensionsIsMutable();
            this.extensions.addRepeatedField(var1, var2);
            this.onChanged();
            return this;
         } else {
            return (GeneratedMessage.ExtendableBuilder)super.addRepeatedField(var1, var2);
         }
      }

      public GeneratedMessage.ExtendableBuilder clear() {
         this.extensions = FieldSet.emptySet();
         return (GeneratedMessage.ExtendableBuilder)super.clear();
      }

      public final GeneratedMessage.ExtendableBuilder clearExtension(GeneratedMessage.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         this.ensureExtensionsIsMutable();
         this.extensions.clearField(var1.getDescriptor());
         this.onChanged();
         return this;
      }

      public GeneratedMessage.ExtendableBuilder clearField(Descriptors.FieldDescriptor var1) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            this.ensureExtensionsIsMutable();
            this.extensions.clearField(var1);
            this.onChanged();
            return this;
         } else {
            return (GeneratedMessage.ExtendableBuilder)super.clearField(var1);
         }
      }

      public GeneratedMessage.ExtendableBuilder clone() {
         throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
      }

      protected boolean extensionsAreInitialized() {
         return this.extensions.isInitialized();
      }

      public Map getAllFields() {
         Map var1 = super.getAllFieldsMutable();
         var1.putAll(this.extensions.getAllFields());
         return Collections.unmodifiableMap(var1);
      }

      public final Object getExtension(GeneratedMessage.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         Descriptors.FieldDescriptor var2 = var1.getDescriptor();
         Object var3 = this.extensions.getField(var2);
         if (var3 == null) {
            if (var2.isRepeated()) {
               return Collections.emptyList();
            } else {
               return var2.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE ? var1.getMessageDefaultInstance() : var1.fromReflectionType(var2.getDefaultValue());
            }
         } else {
            return var1.fromReflectionType(var3);
         }
      }

      public final Object getExtension(GeneratedMessage.GeneratedExtension var1, int var2) {
         this.verifyExtensionContainingType(var1);
         Descriptors.FieldDescriptor var3 = var1.getDescriptor();
         return var1.singularFromReflectionType(this.extensions.getRepeatedField(var3, var2));
      }

      public final int getExtensionCount(GeneratedMessage.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         Descriptors.FieldDescriptor var2 = var1.getDescriptor();
         return this.extensions.getRepeatedFieldCount(var2);
      }

      public Object getField(Descriptors.FieldDescriptor var1) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            Object var2 = this.extensions.getField(var1);
            if (var2 == null) {
               return var1.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE ? DynamicMessage.getDefaultInstance(var1.getMessageType()) : var1.getDefaultValue();
            } else {
               return var2;
            }
         } else {
            return super.getField(var1);
         }
      }

      public Object getRepeatedField(Descriptors.FieldDescriptor var1, int var2) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            return this.extensions.getRepeatedField(var1, var2);
         } else {
            return super.getRepeatedField(var1, var2);
         }
      }

      public int getRepeatedFieldCount(Descriptors.FieldDescriptor var1) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            return this.extensions.getRepeatedFieldCount(var1);
         } else {
            return super.getRepeatedFieldCount(var1);
         }
      }

      public final boolean hasExtension(GeneratedMessage.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         return this.extensions.hasField(var1.getDescriptor());
      }

      public boolean hasField(Descriptors.FieldDescriptor var1) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            return this.extensions.hasField(var1);
         } else {
            return super.hasField(var1);
         }
      }

      public boolean isInitialized() {
         return super.isInitialized() && this.extensionsAreInitialized();
      }

      protected final void mergeExtensionFields(GeneratedMessage.ExtendableMessage var1) {
         this.ensureExtensionsIsMutable();
         this.extensions.mergeFrom(var1.extensions);
         this.onChanged();
      }

      protected boolean parseUnknownField(CodedInputStream var1, UnknownFieldSet.Builder var2, ExtensionRegistryLite var3, int var4) throws IOException {
         return AbstractMessage.Builder.mergeFieldFrom(var1, var2, var3, this.getDescriptorForType(), this, (FieldSet)null, var4);
      }

      public final GeneratedMessage.ExtendableBuilder setExtension(GeneratedMessage.GeneratedExtension var1, int var2, Object var3) {
         this.verifyExtensionContainingType(var1);
         this.ensureExtensionsIsMutable();
         Descriptors.FieldDescriptor var4 = var1.getDescriptor();
         this.extensions.setRepeatedField(var4, var2, var1.singularToReflectionType(var3));
         this.onChanged();
         return this;
      }

      public final GeneratedMessage.ExtendableBuilder setExtension(GeneratedMessage.GeneratedExtension var1, Object var2) {
         this.verifyExtensionContainingType(var1);
         this.ensureExtensionsIsMutable();
         Descriptors.FieldDescriptor var3 = var1.getDescriptor();
         this.extensions.setField(var3, var1.toReflectionType(var2));
         this.onChanged();
         return this;
      }

      public GeneratedMessage.ExtendableBuilder setField(Descriptors.FieldDescriptor var1, Object var2) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            this.ensureExtensionsIsMutable();
            this.extensions.setField(var1, var2);
            this.onChanged();
            return this;
         } else {
            return (GeneratedMessage.ExtendableBuilder)super.setField(var1, var2);
         }
      }

      public GeneratedMessage.ExtendableBuilder setRepeatedField(Descriptors.FieldDescriptor var1, int var2, Object var3) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            this.ensureExtensionsIsMutable();
            this.extensions.setRepeatedField(var1, var2, var3);
            this.onChanged();
            return this;
         } else {
            return (GeneratedMessage.ExtendableBuilder)super.setRepeatedField(var1, var2, var3);
         }
      }
   }

   public abstract static class ExtendableMessage extends GeneratedMessage implements GeneratedMessage.ExtendableMessageOrBuilder {
      private final FieldSet extensions;

      protected ExtendableMessage() {
         this.extensions = FieldSet.newFieldSet();
      }

      protected ExtendableMessage(GeneratedMessage.ExtendableBuilder var1) {
         super(var1);
         this.extensions = var1.buildExtensions();
      }

      private void verifyContainingType(Descriptors.FieldDescriptor var1) {
         if (var1.getContainingType() != this.getDescriptorForType()) {
            throw new IllegalArgumentException("FieldDescriptor does not match message type.");
         }
      }

      private void verifyExtensionContainingType(GeneratedMessage.GeneratedExtension var1) {
         if (var1.getDescriptor().getContainingType() != this.getDescriptorForType()) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Extension is for type \"");
            var2.append(var1.getDescriptor().getContainingType().getFullName());
            var2.append("\" which does not match message type \"");
            var2.append(this.getDescriptorForType().getFullName());
            var2.append("\".");
            throw new IllegalArgumentException(var2.toString());
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

      public Map getAllFields() {
         Map var1 = super.getAllFieldsMutable();
         var1.putAll(this.getExtensionFields());
         return Collections.unmodifiableMap(var1);
      }

      public final Object getExtension(GeneratedMessage.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         Descriptors.FieldDescriptor var2 = var1.getDescriptor();
         Object var3 = this.extensions.getField(var2);
         if (var3 == null) {
            if (var2.isRepeated()) {
               return Collections.emptyList();
            } else {
               return var2.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE ? var1.getMessageDefaultInstance() : var1.fromReflectionType(var2.getDefaultValue());
            }
         } else {
            return var1.fromReflectionType(var3);
         }
      }

      public final Object getExtension(GeneratedMessage.GeneratedExtension var1, int var2) {
         this.verifyExtensionContainingType(var1);
         Descriptors.FieldDescriptor var3 = var1.getDescriptor();
         return var1.singularFromReflectionType(this.extensions.getRepeatedField(var3, var2));
      }

      public final int getExtensionCount(GeneratedMessage.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         Descriptors.FieldDescriptor var2 = var1.getDescriptor();
         return this.extensions.getRepeatedFieldCount(var2);
      }

      protected Map getExtensionFields() {
         return this.extensions.getAllFields();
      }

      public Object getField(Descriptors.FieldDescriptor var1) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            Object var2 = this.extensions.getField(var1);
            if (var2 == null) {
               return var1.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE ? DynamicMessage.getDefaultInstance(var1.getMessageType()) : var1.getDefaultValue();
            } else {
               return var2;
            }
         } else {
            return super.getField(var1);
         }
      }

      public Object getRepeatedField(Descriptors.FieldDescriptor var1, int var2) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            return this.extensions.getRepeatedField(var1, var2);
         } else {
            return super.getRepeatedField(var1, var2);
         }
      }

      public int getRepeatedFieldCount(Descriptors.FieldDescriptor var1) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            return this.extensions.getRepeatedFieldCount(var1);
         } else {
            return super.getRepeatedFieldCount(var1);
         }
      }

      public final boolean hasExtension(GeneratedMessage.GeneratedExtension var1) {
         this.verifyExtensionContainingType(var1);
         return this.extensions.hasField(var1.getDescriptor());
      }

      public boolean hasField(Descriptors.FieldDescriptor var1) {
         if (var1.isExtension()) {
            this.verifyContainingType(var1);
            return this.extensions.hasField(var1);
         } else {
            return super.hasField(var1);
         }
      }

      public boolean isInitialized() {
         return super.isInitialized() && this.extensionsAreInitialized();
      }

      protected void makeExtensionsImmutable() {
         this.extensions.makeImmutable();
      }

      protected GeneratedMessage.ExtendableMessage.ExtensionWriter newExtensionWriter() {
         return new GeneratedMessage.ExtendableMessage.ExtensionWriter(false);
      }

      protected GeneratedMessage.ExtendableMessage.ExtensionWriter newMessageSetExtensionWriter() {
         return new GeneratedMessage.ExtendableMessage.ExtensionWriter(true);
      }

      protected boolean parseUnknownField(CodedInputStream var1, UnknownFieldSet.Builder var2, ExtensionRegistryLite var3, int var4) throws IOException {
         return AbstractMessage.Builder.mergeFieldFrom(var1, var2, var3, this.getDescriptorForType(), (Message.Builder)null, this.extensions, var4);
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
               if (var3 == null || ((Descriptors.FieldDescriptor)var3.getKey()).getNumber() >= var1) {
                  return;
               }

               Descriptors.FieldDescriptor var4 = (Descriptors.FieldDescriptor)this.next.getKey();
               if (this.messageSetWireFormat && var4.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !var4.isRepeated()) {
                  if (this.next instanceof LazyField.LazyEntry) {
                     var2.writeRawMessageSetExtension(var4.getNumber(), ((LazyField.LazyEntry)this.next).getField().toByteString());
                  } else {
                     var2.writeMessageSetExtension(var4.getNumber(), (Message)this.next.getValue());
                  }
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

   public interface ExtendableMessageOrBuilder extends MessageOrBuilder {
      Object getExtension(GeneratedMessage.GeneratedExtension var1);

      Object getExtension(GeneratedMessage.GeneratedExtension var1, int var2);

      int getExtensionCount(GeneratedMessage.GeneratedExtension var1);

      boolean hasExtension(GeneratedMessage.GeneratedExtension var1);
   }

   private interface ExtensionDescriptorRetriever {
      Descriptors.FieldDescriptor getDescriptor();
   }

   public static final class FieldAccessorTable {
      private String[] camelCaseNames;
      private final Descriptors.Descriptor descriptor;
      private final GeneratedMessage.FieldAccessorTable.FieldAccessor[] fields;
      private volatile boolean initialized;

      public FieldAccessorTable(Descriptors.Descriptor var1, String[] var2) {
         this.descriptor = var1;
         this.camelCaseNames = var2;
         this.fields = new GeneratedMessage.FieldAccessorTable.FieldAccessor[var1.getFields().size()];
         this.initialized = false;
      }

      public FieldAccessorTable(Descriptors.Descriptor var1, String[] var2, Class var3, Class var4) {
         this(var1, var2);
         this.ensureFieldAccessorsInitialized(var3, var4);
      }

      private GeneratedMessage.FieldAccessorTable.FieldAccessor getField(Descriptors.FieldDescriptor var1) {
         if (var1.getContainingType() == this.descriptor) {
            if (!var1.isExtension()) {
               return this.fields[var1.getIndex()];
            } else {
               throw new IllegalArgumentException("This type does not have extensions.");
            }
         } else {
            throw new IllegalArgumentException("FieldDescriptor does not match message type.");
         }
      }

      public GeneratedMessage.FieldAccessorTable ensureFieldAccessorsInitialized(Class var1, Class var2) {
         if (this.initialized) {
            return this;
         } else {
            synchronized(this){}

            Throwable var10000;
            boolean var10001;
            label975: {
               try {
                  if (this.initialized) {
                     return this;
                  }
               } catch (Throwable var94) {
                  var10000 = var94;
                  var10001 = false;
                  break label975;
               }

               int var3 = 0;

               while(true) {
                  label976: {
                     Descriptors.FieldDescriptor var4;
                     label977: {
                        try {
                           if (var3 >= this.fields.length) {
                              break;
                           }

                           var4 = (Descriptors.FieldDescriptor)this.descriptor.getFields().get(var3);
                           if (var4.isRepeated()) {
                              if (var4.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                                 break label977;
                              }

                              this.fields[var3] = new GeneratedMessage.FieldAccessorTable.RepeatedMessageFieldAccessor(var4, this.camelCaseNames[var3], var1, var2);
                              break label976;
                           }
                        } catch (Throwable var93) {
                           var10000 = var93;
                           var10001 = false;
                           break label975;
                        }

                        try {
                           if (var4.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                              this.fields[var3] = new GeneratedMessage.FieldAccessorTable.SingularMessageFieldAccessor(var4, this.camelCaseNames[var3], var1, var2);
                              break label976;
                           }
                        } catch (Throwable var91) {
                           var10000 = var91;
                           var10001 = false;
                           break label975;
                        }

                        try {
                           if (var4.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
                              this.fields[var3] = new GeneratedMessage.FieldAccessorTable.SingularEnumFieldAccessor(var4, this.camelCaseNames[var3], var1, var2);
                              break label976;
                           }
                        } catch (Throwable var90) {
                           var10000 = var90;
                           var10001 = false;
                           break label975;
                        }

                        try {
                           this.fields[var3] = new GeneratedMessage.FieldAccessorTable.SingularFieldAccessor(var4, this.camelCaseNames[var3], var1, var2);
                           break label976;
                        } catch (Throwable var88) {
                           var10000 = var88;
                           var10001 = false;
                           break label975;
                        }
                     }

                     try {
                        if (var4.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
                           this.fields[var3] = new GeneratedMessage.FieldAccessorTable.RepeatedEnumFieldAccessor(var4, this.camelCaseNames[var3], var1, var2);
                           break label976;
                        }
                     } catch (Throwable var92) {
                        var10000 = var92;
                        var10001 = false;
                        break label975;
                     }

                     try {
                        this.fields[var3] = new GeneratedMessage.FieldAccessorTable.RepeatedFieldAccessor(var4, this.camelCaseNames[var3], var1, var2);
                     } catch (Throwable var89) {
                        var10000 = var89;
                        var10001 = false;
                        break label975;
                     }
                  }

                  ++var3;
               }

               label925:
               try {
                  this.initialized = true;
                  this.camelCaseNames = null;
                  return this;
               } catch (Throwable var87) {
                  var10000 = var87;
                  var10001 = false;
                  break label925;
               }
            }

            while(true) {
               Throwable var95 = var10000;

               try {
                  throw var95;
               } catch (Throwable var86) {
                  var10000 = var86;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      private interface FieldAccessor {
         void addRepeated(GeneratedMessage.Builder var1, Object var2);

         void clear(GeneratedMessage.Builder var1);

         Object get(GeneratedMessage.Builder var1);

         Object get(GeneratedMessage var1);

         Message.Builder getBuilder(GeneratedMessage.Builder var1);

         Object getRepeated(GeneratedMessage.Builder var1, int var2);

         Object getRepeated(GeneratedMessage var1, int var2);

         int getRepeatedCount(GeneratedMessage.Builder var1);

         int getRepeatedCount(GeneratedMessage var1);

         boolean has(GeneratedMessage.Builder var1);

         boolean has(GeneratedMessage var1);

         Message.Builder newBuilder();

         void set(GeneratedMessage.Builder var1, Object var2);

         void setRepeated(GeneratedMessage.Builder var1, int var2, Object var3);
      }

      private static final class RepeatedEnumFieldAccessor extends GeneratedMessage.FieldAccessorTable.RepeatedFieldAccessor {
         private final Method getValueDescriptorMethod;
         private final Method valueOfMethod;

         RepeatedEnumFieldAccessor(Descriptors.FieldDescriptor var1, String var2, Class var3, Class var4) {
            super(var1, var2, var3, var4);
            this.valueOfMethod = GeneratedMessage.getMethodOrDie(this.type, "valueOf", Descriptors.EnumValueDescriptor.class);
            this.getValueDescriptorMethod = GeneratedMessage.getMethodOrDie(this.type, "getValueDescriptor");
         }

         public void addRepeated(GeneratedMessage.Builder var1, Object var2) {
            super.addRepeated(var1, GeneratedMessage.invokeOrDie(this.valueOfMethod, (Object)null, var2));
         }

         public Object get(GeneratedMessage.Builder var1) {
            ArrayList var2 = new ArrayList();
            Iterator var4 = ((List)super.get(var1)).iterator();

            while(var4.hasNext()) {
               Object var3 = var4.next();
               var2.add(GeneratedMessage.invokeOrDie(this.getValueDescriptorMethod, var3));
            }

            return Collections.unmodifiableList(var2);
         }

         public Object get(GeneratedMessage var1) {
            ArrayList var2 = new ArrayList();
            Iterator var4 = ((List)super.get(var1)).iterator();

            while(var4.hasNext()) {
               Object var3 = var4.next();
               var2.add(GeneratedMessage.invokeOrDie(this.getValueDescriptorMethod, var3));
            }

            return Collections.unmodifiableList(var2);
         }

         public Object getRepeated(GeneratedMessage.Builder var1, int var2) {
            return GeneratedMessage.invokeOrDie(this.getValueDescriptorMethod, super.getRepeated(var1, var2));
         }

         public Object getRepeated(GeneratedMessage var1, int var2) {
            return GeneratedMessage.invokeOrDie(this.getValueDescriptorMethod, super.getRepeated(var1, var2));
         }

         public void setRepeated(GeneratedMessage.Builder var1, int var2, Object var3) {
            super.setRepeated(var1, var2, GeneratedMessage.invokeOrDie(this.valueOfMethod, (Object)null, var3));
         }
      }

      private static class RepeatedFieldAccessor implements GeneratedMessage.FieldAccessorTable.FieldAccessor {
         protected final Method addRepeatedMethod;
         protected final Method clearMethod;
         protected final Method getCountMethod;
         protected final Method getCountMethodBuilder;
         protected final Method getMethod;
         protected final Method getMethodBuilder;
         protected final Method getRepeatedMethod;
         protected final Method getRepeatedMethodBuilder;
         protected final Method setRepeatedMethod;
         protected final Class type;

         RepeatedFieldAccessor(Descriptors.FieldDescriptor var1, String var2, Class var3, Class var4) {
            StringBuilder var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            var5.append("List");
            this.getMethod = GeneratedMessage.getMethodOrDie(var3, var5.toString());
            var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            var5.append("List");
            this.getMethodBuilder = GeneratedMessage.getMethodOrDie(var4, var5.toString());
            var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            this.getRepeatedMethod = GeneratedMessage.getMethodOrDie(var3, var5.toString(), Integer.TYPE);
            var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            this.getRepeatedMethodBuilder = GeneratedMessage.getMethodOrDie(var4, var5.toString(), Integer.TYPE);
            this.type = this.getRepeatedMethod.getReturnType();
            var5 = new StringBuilder();
            var5.append("set");
            var5.append(var2);
            this.setRepeatedMethod = GeneratedMessage.getMethodOrDie(var4, var5.toString(), Integer.TYPE, this.type);
            var5 = new StringBuilder();
            var5.append("add");
            var5.append(var2);
            this.addRepeatedMethod = GeneratedMessage.getMethodOrDie(var4, var5.toString(), this.type);
            var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            var5.append("Count");
            this.getCountMethod = GeneratedMessage.getMethodOrDie(var3, var5.toString());
            var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            var5.append("Count");
            this.getCountMethodBuilder = GeneratedMessage.getMethodOrDie(var4, var5.toString());
            var5 = new StringBuilder();
            var5.append("clear");
            var5.append(var2);
            this.clearMethod = GeneratedMessage.getMethodOrDie(var4, var5.toString());
         }

         public void addRepeated(GeneratedMessage.Builder var1, Object var2) {
            GeneratedMessage.invokeOrDie(this.addRepeatedMethod, var1, var2);
         }

         public void clear(GeneratedMessage.Builder var1) {
            GeneratedMessage.invokeOrDie(this.clearMethod, var1);
         }

         public Object get(GeneratedMessage.Builder var1) {
            return GeneratedMessage.invokeOrDie(this.getMethodBuilder, var1);
         }

         public Object get(GeneratedMessage var1) {
            return GeneratedMessage.invokeOrDie(this.getMethod, var1);
         }

         public Message.Builder getBuilder(GeneratedMessage.Builder var1) {
            throw new UnsupportedOperationException("getFieldBuilder() called on a non-Message type.");
         }

         public Object getRepeated(GeneratedMessage.Builder var1, int var2) {
            return GeneratedMessage.invokeOrDie(this.getRepeatedMethodBuilder, var1, var2);
         }

         public Object getRepeated(GeneratedMessage var1, int var2) {
            return GeneratedMessage.invokeOrDie(this.getRepeatedMethod, var1, var2);
         }

         public int getRepeatedCount(GeneratedMessage.Builder var1) {
            return (Integer)GeneratedMessage.invokeOrDie(this.getCountMethodBuilder, var1);
         }

         public int getRepeatedCount(GeneratedMessage var1) {
            return (Integer)GeneratedMessage.invokeOrDie(this.getCountMethod, var1);
         }

         public boolean has(GeneratedMessage.Builder var1) {
            throw new UnsupportedOperationException("hasField() called on a repeated field.");
         }

         public boolean has(GeneratedMessage var1) {
            throw new UnsupportedOperationException("hasField() called on a repeated field.");
         }

         public Message.Builder newBuilder() {
            throw new UnsupportedOperationException("newBuilderForField() called on a non-Message type.");
         }

         public void set(GeneratedMessage.Builder var1, Object var2) {
            this.clear(var1);
            Iterator var3 = ((List)var2).iterator();

            while(var3.hasNext()) {
               this.addRepeated(var1, var3.next());
            }

         }

         public void setRepeated(GeneratedMessage.Builder var1, int var2, Object var3) {
            GeneratedMessage.invokeOrDie(this.setRepeatedMethod, var1, var2, var3);
         }
      }

      private static final class RepeatedMessageFieldAccessor extends GeneratedMessage.FieldAccessorTable.RepeatedFieldAccessor {
         private final Method newBuilderMethod;

         RepeatedMessageFieldAccessor(Descriptors.FieldDescriptor var1, String var2, Class var3, Class var4) {
            super(var1, var2, var3, var4);
            this.newBuilderMethod = GeneratedMessage.getMethodOrDie(this.type, "newBuilder");
         }

         private Object coerceType(Object var1) {
            return this.type.isInstance(var1) ? var1 : ((Message.Builder)GeneratedMessage.invokeOrDie(this.newBuilderMethod, (Object)null)).mergeFrom((Message)var1).build();
         }

         public void addRepeated(GeneratedMessage.Builder var1, Object var2) {
            super.addRepeated(var1, this.coerceType(var2));
         }

         public Message.Builder newBuilder() {
            return (Message.Builder)GeneratedMessage.invokeOrDie(this.newBuilderMethod, (Object)null);
         }

         public void setRepeated(GeneratedMessage.Builder var1, int var2, Object var3) {
            super.setRepeated(var1, var2, this.coerceType(var3));
         }
      }

      private static final class SingularEnumFieldAccessor extends GeneratedMessage.FieldAccessorTable.SingularFieldAccessor {
         private Method getValueDescriptorMethod;
         private Method valueOfMethod;

         SingularEnumFieldAccessor(Descriptors.FieldDescriptor var1, String var2, Class var3, Class var4) {
            super(var1, var2, var3, var4);
            this.valueOfMethod = GeneratedMessage.getMethodOrDie(this.type, "valueOf", Descriptors.EnumValueDescriptor.class);
            this.getValueDescriptorMethod = GeneratedMessage.getMethodOrDie(this.type, "getValueDescriptor");
         }

         public Object get(GeneratedMessage.Builder var1) {
            return GeneratedMessage.invokeOrDie(this.getValueDescriptorMethod, super.get(var1));
         }

         public Object get(GeneratedMessage var1) {
            return GeneratedMessage.invokeOrDie(this.getValueDescriptorMethod, super.get(var1));
         }

         public void set(GeneratedMessage.Builder var1, Object var2) {
            super.set(var1, GeneratedMessage.invokeOrDie(this.valueOfMethod, (Object)null, var2));
         }
      }

      private static class SingularFieldAccessor implements GeneratedMessage.FieldAccessorTable.FieldAccessor {
         protected final Method clearMethod;
         protected final Method getMethod;
         protected final Method getMethodBuilder;
         protected final Method hasMethod;
         protected final Method hasMethodBuilder;
         protected final Method setMethod;
         protected final Class type;

         SingularFieldAccessor(Descriptors.FieldDescriptor var1, String var2, Class var3, Class var4) {
            StringBuilder var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            this.getMethod = GeneratedMessage.getMethodOrDie(var3, var5.toString());
            var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            this.getMethodBuilder = GeneratedMessage.getMethodOrDie(var4, var5.toString());
            this.type = this.getMethod.getReturnType();
            var5 = new StringBuilder();
            var5.append("set");
            var5.append(var2);
            this.setMethod = GeneratedMessage.getMethodOrDie(var4, var5.toString(), this.type);
            var5 = new StringBuilder();
            var5.append("has");
            var5.append(var2);
            this.hasMethod = GeneratedMessage.getMethodOrDie(var3, var5.toString());
            var5 = new StringBuilder();
            var5.append("has");
            var5.append(var2);
            this.hasMethodBuilder = GeneratedMessage.getMethodOrDie(var4, var5.toString());
            var5 = new StringBuilder();
            var5.append("clear");
            var5.append(var2);
            this.clearMethod = GeneratedMessage.getMethodOrDie(var4, var5.toString());
         }

         public void addRepeated(GeneratedMessage.Builder var1, Object var2) {
            throw new UnsupportedOperationException("addRepeatedField() called on a singular field.");
         }

         public void clear(GeneratedMessage.Builder var1) {
            GeneratedMessage.invokeOrDie(this.clearMethod, var1);
         }

         public Object get(GeneratedMessage.Builder var1) {
            return GeneratedMessage.invokeOrDie(this.getMethodBuilder, var1);
         }

         public Object get(GeneratedMessage var1) {
            return GeneratedMessage.invokeOrDie(this.getMethod, var1);
         }

         public Message.Builder getBuilder(GeneratedMessage.Builder var1) {
            throw new UnsupportedOperationException("getFieldBuilder() called on a non-Message type.");
         }

         public Object getRepeated(GeneratedMessage.Builder var1, int var2) {
            throw new UnsupportedOperationException("getRepeatedField() called on a singular field.");
         }

         public Object getRepeated(GeneratedMessage var1, int var2) {
            throw new UnsupportedOperationException("getRepeatedField() called on a singular field.");
         }

         public int getRepeatedCount(GeneratedMessage.Builder var1) {
            throw new UnsupportedOperationException("getRepeatedFieldSize() called on a singular field.");
         }

         public int getRepeatedCount(GeneratedMessage var1) {
            throw new UnsupportedOperationException("getRepeatedFieldSize() called on a singular field.");
         }

         public boolean has(GeneratedMessage.Builder var1) {
            return (Boolean)GeneratedMessage.invokeOrDie(this.hasMethodBuilder, var1);
         }

         public boolean has(GeneratedMessage var1) {
            return (Boolean)GeneratedMessage.invokeOrDie(this.hasMethod, var1);
         }

         public Message.Builder newBuilder() {
            throw new UnsupportedOperationException("newBuilderForField() called on a non-Message type.");
         }

         public void set(GeneratedMessage.Builder var1, Object var2) {
            GeneratedMessage.invokeOrDie(this.setMethod, var1, var2);
         }

         public void setRepeated(GeneratedMessage.Builder var1, int var2, Object var3) {
            throw new UnsupportedOperationException("setRepeatedField() called on a singular field.");
         }
      }

      private static final class SingularMessageFieldAccessor extends GeneratedMessage.FieldAccessorTable.SingularFieldAccessor {
         private final Method getBuilderMethodBuilder;
         private final Method newBuilderMethod;

         SingularMessageFieldAccessor(Descriptors.FieldDescriptor var1, String var2, Class var3, Class var4) {
            super(var1, var2, var3, var4);
            this.newBuilderMethod = GeneratedMessage.getMethodOrDie(this.type, "newBuilder");
            StringBuilder var5 = new StringBuilder();
            var5.append("get");
            var5.append(var2);
            var5.append("Builder");
            this.getBuilderMethodBuilder = GeneratedMessage.getMethodOrDie(var4, var5.toString());
         }

         private Object coerceType(Object var1) {
            return this.type.isInstance(var1) ? var1 : ((Message.Builder)GeneratedMessage.invokeOrDie(this.newBuilderMethod, (Object)null)).mergeFrom((Message)var1).buildPartial();
         }

         public Message.Builder getBuilder(GeneratedMessage.Builder var1) {
            return (Message.Builder)GeneratedMessage.invokeOrDie(this.getBuilderMethodBuilder, var1);
         }

         public Message.Builder newBuilder() {
            return (Message.Builder)GeneratedMessage.invokeOrDie(this.newBuilderMethod, (Object)null);
         }

         public void set(GeneratedMessage.Builder var1, Object var2) {
            super.set(var1, this.coerceType(var2));
         }
      }
   }

   public static final class GeneratedExtension {
      private GeneratedMessage.ExtensionDescriptorRetriever descriptorRetriever;
      private final Method enumGetValueDescriptor;
      private final Method enumValueOf;
      private final Message messageDefaultInstance;
      private final Class singularType;

      private GeneratedExtension(GeneratedMessage.ExtensionDescriptorRetriever var1, Class var2, Message var3) {
         if (Message.class.isAssignableFrom(var2) && !var2.isInstance(var3)) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Bad messageDefaultInstance for ");
            var4.append(var2.getName());
            throw new IllegalArgumentException(var4.toString());
         } else {
            this.descriptorRetriever = var1;
            this.singularType = var2;
            this.messageDefaultInstance = var3;
            if (ProtocolMessageEnum.class.isAssignableFrom(var2)) {
               this.enumValueOf = GeneratedMessage.getMethodOrDie(var2, "valueOf", Descriptors.EnumValueDescriptor.class);
               this.enumGetValueDescriptor = GeneratedMessage.getMethodOrDie(var2, "getValueDescriptor");
            } else {
               this.enumValueOf = null;
               this.enumGetValueDescriptor = null;
            }
         }
      }

      // $FF: synthetic method
      GeneratedExtension(GeneratedMessage.ExtensionDescriptorRetriever var1, Class var2, Message var3, Object var4) {
         this(var1, var2, var3);
      }

      private Object fromReflectionType(Object var1) {
         Descriptors.FieldDescriptor var2 = this.getDescriptor();
         if (!var2.isRepeated()) {
            return this.singularFromReflectionType(var1);
         } else if (var2.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE && var2.getJavaType() != Descriptors.FieldDescriptor.JavaType.ENUM) {
            return var1;
         } else {
            ArrayList var4 = new ArrayList();
            Iterator var3 = ((List)var1).iterator();

            while(var3.hasNext()) {
               var4.add(this.singularFromReflectionType(var3.next()));
            }

            return var4;
         }
      }

      private Object singularFromReflectionType(Object var1) {
         Descriptors.FieldDescriptor var3 = this.getDescriptor();
         int var2 = null.$SwitchMap$com$google$protobuf$Descriptors$FieldDescriptor$JavaType[var3.getJavaType().ordinal()];
         if (var2 != 1) {
            return var2 != 2 ? var1 : GeneratedMessage.invokeOrDie(this.enumValueOf, (Object)null, (Descriptors.EnumValueDescriptor)var1);
         } else {
            return this.singularType.isInstance(var1) ? var1 : this.messageDefaultInstance.newBuilderForType().mergeFrom((Message)var1).build();
         }
      }

      private Object singularToReflectionType(Object var1) {
         Descriptors.FieldDescriptor var2 = this.getDescriptor();
         return null.$SwitchMap$com$google$protobuf$Descriptors$FieldDescriptor$JavaType[var2.getJavaType().ordinal()] != 2 ? var1 : GeneratedMessage.invokeOrDie(this.enumGetValueDescriptor, var1);
      }

      private Object toReflectionType(Object var1) {
         Descriptors.FieldDescriptor var2 = this.getDescriptor();
         if (!var2.isRepeated()) {
            return this.singularToReflectionType(var1);
         } else if (var2.getJavaType() != Descriptors.FieldDescriptor.JavaType.ENUM) {
            return var1;
         } else {
            ArrayList var4 = new ArrayList();
            Iterator var3 = ((List)var1).iterator();

            while(var3.hasNext()) {
               var4.add(this.singularToReflectionType(var3.next()));
            }

            return var4;
         }
      }

      public Descriptors.FieldDescriptor getDescriptor() {
         GeneratedMessage.ExtensionDescriptorRetriever var1 = this.descriptorRetriever;
         if (var1 != null) {
            return var1.getDescriptor();
         } else {
            throw new IllegalStateException("getDescriptor() called before internalInit()");
         }
      }

      public Message getMessageDefaultInstance() {
         return this.messageDefaultInstance;
      }

      public void internalInit(final Descriptors.FieldDescriptor var1) {
         if (this.descriptorRetriever == null) {
            this.descriptorRetriever = new GeneratedMessage.ExtensionDescriptorRetriever() {
               public Descriptors.FieldDescriptor getDescriptor() {
                  return var1;
               }
            };
         } else {
            throw new IllegalStateException("Already initialized.");
         }
      }
   }
}
