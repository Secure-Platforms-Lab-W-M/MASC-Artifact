package com.google.protobuf;

import java.util.Iterator;
import java.util.Map.Entry;

class LazyField {
   private ByteString bytes;
   private final MessageLite defaultInstance;
   private final ExtensionRegistryLite extensionRegistry;
   private volatile boolean isDirty = false;
   private volatile MessageLite value;

   public LazyField(MessageLite var1, ExtensionRegistryLite var2, ByteString var3) {
      this.defaultInstance = var1;
      this.extensionRegistry = var2;
      this.bytes = var3;
   }

   private void ensureInitialized() {
      // $FF: Couldn't be decompiled
   }

   public boolean equals(Object var1) {
      this.ensureInitialized();
      return this.value.equals(var1);
   }

   public int getSerializedSize() {
      return this.isDirty ? this.value.getSerializedSize() : this.bytes.size();
   }

   public MessageLite getValue() {
      this.ensureInitialized();
      return this.value;
   }

   public int hashCode() {
      this.ensureInitialized();
      return this.value.hashCode();
   }

   public MessageLite setValue(MessageLite var1) {
      MessageLite var2 = this.value;
      this.value = var1;
      this.bytes = null;
      this.isDirty = true;
      return var2;
   }

   public ByteString toByteString() {
      if (!this.isDirty) {
         return this.bytes;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label137: {
            ByteString var14;
            try {
               if (!this.isDirty) {
                  var14 = this.bytes;
                  return var14;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label137;
            }

            label131:
            try {
               this.bytes = this.value.toByteString();
               this.isDirty = false;
               var14 = this.bytes;
               return var14;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label131;
            }
         }

         while(true) {
            Throwable var1 = var10000;

            try {
               throw var1;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public String toString() {
      this.ensureInitialized();
      return this.value.toString();
   }

   static class LazyEntry implements Entry {
      private Entry entry;

      private LazyEntry(Entry var1) {
         this.entry = var1;
      }

      // $FF: synthetic method
      LazyEntry(Entry var1, Object var2) {
         this(var1);
      }

      public LazyField getField() {
         return (LazyField)this.entry.getValue();
      }

      public Object getKey() {
         return this.entry.getKey();
      }

      public Object getValue() {
         LazyField var1 = (LazyField)this.entry.getValue();
         return var1 == null ? null : var1.getValue();
      }

      public Object setValue(Object var1) {
         if (var1 instanceof MessageLite) {
            return ((LazyField)this.entry.getValue()).setValue((MessageLite)var1);
         } else {
            throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
         }
      }
   }

   static class LazyIterator implements Iterator {
      private Iterator iterator;

      public LazyIterator(Iterator var1) {
         this.iterator = var1;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      public Entry next() {
         Entry var1 = (Entry)this.iterator.next();
         return (Entry)(var1.getValue() instanceof LazyField ? new LazyField.LazyEntry(var1) : var1);
      }

      public void remove() {
         this.iterator.remove();
      }
   }
}
