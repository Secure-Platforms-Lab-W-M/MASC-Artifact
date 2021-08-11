package com.google.protobuf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ExtensionRegistry extends ExtensionRegistryLite {
   private static final ExtensionRegistry EMPTY = new ExtensionRegistry(true);
   private final Map extensionsByName;
   private final Map extensionsByNumber;

   private ExtensionRegistry() {
      this.extensionsByName = new HashMap();
      this.extensionsByNumber = new HashMap();
   }

   private ExtensionRegistry(ExtensionRegistry var1) {
      super(var1);
      this.extensionsByName = Collections.unmodifiableMap(var1.extensionsByName);
      this.extensionsByNumber = Collections.unmodifiableMap(var1.extensionsByNumber);
   }

   private ExtensionRegistry(boolean var1) {
      super(ExtensionRegistryLite.getEmptyRegistry());
      this.extensionsByName = Collections.emptyMap();
      this.extensionsByNumber = Collections.emptyMap();
   }

   private void add(ExtensionRegistry.ExtensionInfo var1) {
      if (var1.descriptor.isExtension()) {
         this.extensionsByName.put(var1.descriptor.getFullName(), var1);
         this.extensionsByNumber.put(new ExtensionRegistry.DescriptorIntPair(var1.descriptor.getContainingType(), var1.descriptor.getNumber()), var1);
         Descriptors.FieldDescriptor var2 = var1.descriptor;
         if (var2.getContainingType().getOptions().getMessageSetWireFormat() && var2.getType() == Descriptors.FieldDescriptor.Type.MESSAGE && var2.isOptional() && var2.getExtensionScope() == var2.getMessageType()) {
            this.extensionsByName.put(var2.getMessageType().getFullName(), var1);
         }

      } else {
         throw new IllegalArgumentException("ExtensionRegistry.add() was given a FieldDescriptor for a regular (non-extension) field.");
      }
   }

   public static ExtensionRegistry getEmptyRegistry() {
      return EMPTY;
   }

   public static ExtensionRegistry newInstance() {
      return new ExtensionRegistry();
   }

   public void add(Descriptors.FieldDescriptor var1) {
      if (var1.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE) {
         this.add(new ExtensionRegistry.ExtensionInfo(var1, (Message)null));
      } else {
         throw new IllegalArgumentException("ExtensionRegistry.add() must be provided a default instance when adding an embedded message extension.");
      }
   }

   public void add(Descriptors.FieldDescriptor var1, Message var2) {
      if (var1.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
         this.add(new ExtensionRegistry.ExtensionInfo(var1, var2));
      } else {
         throw new IllegalArgumentException("ExtensionRegistry.add() provided a default instance for a non-message extension.");
      }
   }

   public void add(GeneratedMessage.GeneratedExtension var1) {
      if (var1.getDescriptor().getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
         if (var1.getMessageDefaultInstance() != null) {
            this.add(new ExtensionRegistry.ExtensionInfo(var1.getDescriptor(), var1.getMessageDefaultInstance()));
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Registered message-type extension had null default instance: ");
            var2.append(var1.getDescriptor().getFullName());
            throw new IllegalStateException(var2.toString());
         }
      } else {
         this.add(new ExtensionRegistry.ExtensionInfo(var1.getDescriptor(), (Message)null));
      }
   }

   public ExtensionRegistry.ExtensionInfo findExtensionByName(String var1) {
      return (ExtensionRegistry.ExtensionInfo)this.extensionsByName.get(var1);
   }

   public ExtensionRegistry.ExtensionInfo findExtensionByNumber(Descriptors.Descriptor var1, int var2) {
      return (ExtensionRegistry.ExtensionInfo)this.extensionsByNumber.get(new ExtensionRegistry.DescriptorIntPair(var1, var2));
   }

   public ExtensionRegistry getUnmodifiable() {
      return new ExtensionRegistry(this);
   }

   private static final class DescriptorIntPair {
      private final Descriptors.Descriptor descriptor;
      private final int number;

      DescriptorIntPair(Descriptors.Descriptor var1, int var2) {
         this.descriptor = var1;
         this.number = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = var1 instanceof ExtensionRegistry.DescriptorIntPair;
         boolean var3 = false;
         if (!var2) {
            return false;
         } else {
            ExtensionRegistry.DescriptorIntPair var4 = (ExtensionRegistry.DescriptorIntPair)var1;
            var2 = var3;
            if (this.descriptor == var4.descriptor) {
               var2 = var3;
               if (this.number == var4.number) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public int hashCode() {
         return this.descriptor.hashCode() * '\uffff' + this.number;
      }
   }

   public static final class ExtensionInfo {
      public final Message defaultInstance;
      public final Descriptors.FieldDescriptor descriptor;

      private ExtensionInfo(Descriptors.FieldDescriptor var1) {
         this.descriptor = var1;
         this.defaultInstance = null;
      }

      private ExtensionInfo(Descriptors.FieldDescriptor var1, Message var2) {
         this.descriptor = var1;
         this.defaultInstance = var2;
      }

      // $FF: synthetic method
      ExtensionInfo(Descriptors.FieldDescriptor var1, Message var2, Object var3) {
         this(var1, var2);
      }
   }
}
