package com.google.protobuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class FieldSet {
   private static final FieldSet DEFAULT_INSTANCE = new FieldSet(true);
   private final SmallSortedMap fields;
   private boolean hasLazyField = false;
   private boolean isImmutable;

   private FieldSet() {
      this.fields = SmallSortedMap.newFieldMap(16);
   }

   private FieldSet(boolean var1) {
      this.fields = SmallSortedMap.newFieldMap(0);
      this.makeImmutable();
   }

   private void cloneFieldEntry(Map var1, Entry var2) {
      FieldSet.FieldDescriptorLite var3 = (FieldSet.FieldDescriptorLite)var2.getKey();
      Object var4 = var2.getValue();
      if (var4 instanceof LazyField) {
         var1.put(var3, ((LazyField)var4).getValue());
      } else {
         var1.put(var3, var4);
      }
   }

   private static int computeElementSize(WireFormat.FieldType var0, int var1, Object var2) {
      int var3 = CodedOutputStream.computeTagSize(var1);
      var1 = var3;
      if (var0 == WireFormat.FieldType.GROUP) {
         var1 = var3 * 2;
      }

      return computeElementSizeNoTag(var0, var2) + var1;
   }

   private static int computeElementSizeNoTag(WireFormat.FieldType var0, Object var1) {
      switch(null.$SwitchMap$com$google$protobuf$WireFormat$FieldType[var0.ordinal()]) {
      case 1:
         return CodedOutputStream.computeDoubleSizeNoTag((Double)var1);
      case 2:
         return CodedOutputStream.computeFloatSizeNoTag((Float)var1);
      case 3:
         return CodedOutputStream.computeInt64SizeNoTag((Long)var1);
      case 4:
         return CodedOutputStream.computeUInt64SizeNoTag((Long)var1);
      case 5:
         return CodedOutputStream.computeInt32SizeNoTag((Integer)var1);
      case 6:
         return CodedOutputStream.computeFixed64SizeNoTag((Long)var1);
      case 7:
         return CodedOutputStream.computeFixed32SizeNoTag((Integer)var1);
      case 8:
         return CodedOutputStream.computeBoolSizeNoTag((Boolean)var1);
      case 9:
         return CodedOutputStream.computeStringSizeNoTag((String)var1);
      case 10:
         return CodedOutputStream.computeBytesSizeNoTag((ByteString)var1);
      case 11:
         return CodedOutputStream.computeUInt32SizeNoTag((Integer)var1);
      case 12:
         return CodedOutputStream.computeSFixed32SizeNoTag((Integer)var1);
      case 13:
         return CodedOutputStream.computeSFixed64SizeNoTag((Long)var1);
      case 14:
         return CodedOutputStream.computeSInt32SizeNoTag((Integer)var1);
      case 15:
         return CodedOutputStream.computeSInt64SizeNoTag((Long)var1);
      case 16:
         return CodedOutputStream.computeGroupSizeNoTag((MessageLite)var1);
      case 17:
         if (var1 instanceof LazyField) {
            return CodedOutputStream.computeLazyFieldSizeNoTag((LazyField)var1);
         }

         return CodedOutputStream.computeMessageSizeNoTag((MessageLite)var1);
      case 18:
         return CodedOutputStream.computeEnumSizeNoTag(((Internal.EnumLite)var1).getNumber());
      default:
         throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
      }
   }

   public static int computeFieldSize(FieldSet.FieldDescriptorLite var0, Object var1) {
      WireFormat.FieldType var4 = var0.getLiteType();
      int var3 = var0.getNumber();
      if (!var0.isRepeated()) {
         return computeElementSize(var4, var3, var1);
      } else {
         int var2;
         Iterator var5;
         if (var0.isPacked()) {
            var2 = 0;

            for(var5 = ((List)var1).iterator(); var5.hasNext(); var2 += computeElementSizeNoTag(var4, var5.next())) {
            }

            return CodedOutputStream.computeTagSize(var3) + var2 + CodedOutputStream.computeRawVarint32Size(var2);
         } else {
            var2 = 0;

            for(var5 = ((List)var1).iterator(); var5.hasNext(); var2 += computeElementSize(var4, var3, var5.next())) {
            }

            return var2;
         }
      }
   }

   public static FieldSet emptySet() {
      return DEFAULT_INSTANCE;
   }

   private int getMessageSetSerializedSize(Entry var1) {
      FieldSet.FieldDescriptorLite var2 = (FieldSet.FieldDescriptorLite)var1.getKey();
      Object var3 = var1.getValue();
      if (var2.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !var2.isRepeated() && !var2.isPacked()) {
         return var3 instanceof LazyField ? CodedOutputStream.computeLazyFieldMessageSetExtensionSize(((FieldSet.FieldDescriptorLite)var1.getKey()).getNumber(), (LazyField)var3) : CodedOutputStream.computeMessageSetExtensionSize(((FieldSet.FieldDescriptorLite)var1.getKey()).getNumber(), (MessageLite)var3);
      } else {
         return computeFieldSize(var2, var3);
      }
   }

   static int getWireFormatForFieldType(WireFormat.FieldType var0, boolean var1) {
      return var1 ? 2 : var0.getWireType();
   }

   private boolean isInitialized(Entry var1) {
      FieldSet.FieldDescriptorLite var2 = (FieldSet.FieldDescriptorLite)var1.getKey();
      if (var2.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
         if (var2.isRepeated()) {
            Iterator var4 = ((List)var1.getValue()).iterator();

            do {
               if (!var4.hasNext()) {
                  return true;
               }
            } while(((MessageLite)var4.next()).isInitialized());

            return false;
         }

         Object var3 = var1.getValue();
         if (!(var3 instanceof MessageLite)) {
            if (var3 instanceof LazyField) {
               return true;
            }

            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
         }

         if (!((MessageLite)var3).isInitialized()) {
            return false;
         }
      }

      return true;
   }

   private void mergeFromField(Entry var1) {
      FieldSet.FieldDescriptorLite var3 = (FieldSet.FieldDescriptorLite)var1.getKey();
      Object var2 = var1.getValue();
      Object var4 = var2;
      if (var2 instanceof LazyField) {
         var4 = ((LazyField)var2).getValue();
      }

      if (var3.isRepeated()) {
         var2 = this.getField(var3);
         if (var2 == null) {
            this.fields.put((Comparable)var3, new ArrayList((List)var4));
         } else {
            ((List)var2).addAll((List)var4);
         }

      } else if (var3.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
         var2 = this.getField(var3);
         if (var2 == null) {
            this.fields.put((Comparable)var3, var4);
         } else {
            this.fields.put((Comparable)var3, var3.internalMergeFrom(((MessageLite)var2).toBuilder(), (MessageLite)var4).build());
         }

      } else {
         this.fields.put((Comparable)var3, var4);
      }
   }

   public static FieldSet newFieldSet() {
      return new FieldSet();
   }

   public static Object readPrimitiveField(CodedInputStream var0, WireFormat.FieldType var1) throws IOException {
      switch(null.$SwitchMap$com$google$protobuf$WireFormat$FieldType[var1.ordinal()]) {
      case 1:
         return var0.readDouble();
      case 2:
         return var0.readFloat();
      case 3:
         return var0.readInt64();
      case 4:
         return var0.readUInt64();
      case 5:
         return var0.readInt32();
      case 6:
         return var0.readFixed64();
      case 7:
         return var0.readFixed32();
      case 8:
         return var0.readBool();
      case 9:
         return var0.readString();
      case 10:
         return var0.readBytes();
      case 11:
         return var0.readUInt32();
      case 12:
         return var0.readSFixed32();
      case 13:
         return var0.readSFixed64();
      case 14:
         return var0.readSInt32();
      case 15:
         return var0.readSInt64();
      case 16:
         throw new IllegalArgumentException("readPrimitiveField() cannot handle nested groups.");
      case 17:
         throw new IllegalArgumentException("readPrimitiveField() cannot handle embedded messages.");
      case 18:
         throw new IllegalArgumentException("readPrimitiveField() cannot handle enums.");
      default:
         throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
      }
   }

   private static void verifyType(WireFormat.FieldType var0, Object var1) {
      if (var1 == null) {
         throw null;
      } else {
         boolean var2 = false;
         switch(null.$SwitchMap$com$google$protobuf$WireFormat$JavaType[var0.getJavaType().ordinal()]) {
         case 1:
            var2 = var1 instanceof Integer;
            break;
         case 2:
            var2 = var1 instanceof Long;
            break;
         case 3:
            var2 = var1 instanceof Float;
            break;
         case 4:
            var2 = var1 instanceof Double;
            break;
         case 5:
            var2 = var1 instanceof Boolean;
            break;
         case 6:
            var2 = var1 instanceof String;
            break;
         case 7:
            var2 = var1 instanceof ByteString;
            break;
         case 8:
            var2 = var1 instanceof Internal.EnumLite;
            break;
         case 9:
            if (!(var1 instanceof MessageLite) && !(var1 instanceof LazyField)) {
               var2 = false;
            } else {
               var2 = true;
            }
         }

         if (!var2) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
         }
      }
   }

   private static void writeElement(CodedOutputStream var0, WireFormat.FieldType var1, int var2, Object var3) throws IOException {
      if (var1 == WireFormat.FieldType.GROUP) {
         var0.writeGroup(var2, (MessageLite)var3);
      } else {
         var0.writeTag(var2, getWireFormatForFieldType(var1, false));
         writeElementNoTag(var0, var1, var3);
      }
   }

   private static void writeElementNoTag(CodedOutputStream var0, WireFormat.FieldType var1, Object var2) throws IOException {
      switch(null.$SwitchMap$com$google$protobuf$WireFormat$FieldType[var1.ordinal()]) {
      case 1:
         var0.writeDoubleNoTag((Double)var2);
         return;
      case 2:
         var0.writeFloatNoTag((Float)var2);
         return;
      case 3:
         var0.writeInt64NoTag((Long)var2);
         return;
      case 4:
         var0.writeUInt64NoTag((Long)var2);
         return;
      case 5:
         var0.writeInt32NoTag((Integer)var2);
         return;
      case 6:
         var0.writeFixed64NoTag((Long)var2);
         return;
      case 7:
         var0.writeFixed32NoTag((Integer)var2);
         return;
      case 8:
         var0.writeBoolNoTag((Boolean)var2);
         return;
      case 9:
         var0.writeStringNoTag((String)var2);
         return;
      case 10:
         var0.writeBytesNoTag((ByteString)var2);
         return;
      case 11:
         var0.writeUInt32NoTag((Integer)var2);
         return;
      case 12:
         var0.writeSFixed32NoTag((Integer)var2);
         return;
      case 13:
         var0.writeSFixed64NoTag((Long)var2);
         return;
      case 14:
         var0.writeSInt32NoTag((Integer)var2);
         return;
      case 15:
         var0.writeSInt64NoTag((Long)var2);
         return;
      case 16:
         var0.writeGroupNoTag((MessageLite)var2);
         return;
      case 17:
         var0.writeMessageNoTag((MessageLite)var2);
         return;
      case 18:
         var0.writeEnumNoTag(((Internal.EnumLite)var2).getNumber());
         return;
      default:
      }
   }

   public static void writeField(FieldSet.FieldDescriptorLite var0, Object var1, CodedOutputStream var2) throws IOException {
      WireFormat.FieldType var4 = var0.getLiteType();
      int var3 = var0.getNumber();
      if (!var0.isRepeated()) {
         if (var1 instanceof LazyField) {
            writeElement(var2, var4, var3, ((LazyField)var1).getValue());
         } else {
            writeElement(var2, var4, var3, var1);
         }
      } else {
         List var6 = (List)var1;
         Iterator var5;
         if (var0.isPacked()) {
            var2.writeTag(var3, 2);
            var3 = 0;

            for(var5 = var6.iterator(); var5.hasNext(); var3 += computeElementSizeNoTag(var4, var5.next())) {
            }

            var2.writeRawVarint32(var3);
            var5 = var6.iterator();

            while(var5.hasNext()) {
               writeElementNoTag(var2, var4, var5.next());
            }
         } else {
            var5 = var6.iterator();

            while(var5.hasNext()) {
               writeElement(var2, var4, var3, var5.next());
            }
         }

      }
   }

   private void writeMessageSetTo(Entry var1, CodedOutputStream var2) throws IOException {
      FieldSet.FieldDescriptorLite var3 = (FieldSet.FieldDescriptorLite)var1.getKey();
      if (var3.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !var3.isRepeated() && !var3.isPacked()) {
         var2.writeMessageSetExtension(((FieldSet.FieldDescriptorLite)var1.getKey()).getNumber(), (MessageLite)var1.getValue());
      } else {
         writeField(var3, var1.getValue(), var2);
      }
   }

   public void addRepeatedField(FieldSet.FieldDescriptorLite var1, Object var2) {
      if (var1.isRepeated()) {
         verifyType(var1.getLiteType(), var2);
         Object var3 = this.getField(var1);
         Object var4;
         if (var3 == null) {
            ArrayList var5 = new ArrayList();
            this.fields.put((Comparable)var1, var5);
            var4 = var5;
         } else {
            var4 = (List)var3;
         }

         ((List)var4).add(var2);
      } else {
         throw new IllegalArgumentException("addRepeatedField() can only be called on repeated fields.");
      }
   }

   public void clear() {
      this.fields.clear();
      this.hasLazyField = false;
   }

   public void clearField(FieldSet.FieldDescriptorLite var1) {
      this.fields.remove(var1);
      if (this.fields.isEmpty()) {
         this.hasLazyField = false;
      }

   }

   public FieldSet clone() {
      FieldSet var2 = newFieldSet();

      for(int var1 = 0; var1 < this.fields.getNumArrayEntries(); ++var1) {
         Entry var3 = this.fields.getArrayEntryAt(var1);
         var2.setField((FieldSet.FieldDescriptorLite)var3.getKey(), var3.getValue());
      }

      Iterator var5 = this.fields.getOverflowEntries().iterator();

      while(var5.hasNext()) {
         Entry var4 = (Entry)var5.next();
         var2.setField((FieldSet.FieldDescriptorLite)var4.getKey(), var4.getValue());
      }

      var2.hasLazyField = this.hasLazyField;
      return var2;
   }

   public Map getAllFields() {
      if (!this.hasLazyField) {
         return (Map)(this.fields.isImmutable() ? this.fields : Collections.unmodifiableMap(this.fields));
      } else {
         SmallSortedMap var2 = SmallSortedMap.newFieldMap(16);

         for(int var1 = 0; var1 < this.fields.getNumArrayEntries(); ++var1) {
            this.cloneFieldEntry(var2, this.fields.getArrayEntryAt(var1));
         }

         Iterator var3 = this.fields.getOverflowEntries().iterator();

         while(var3.hasNext()) {
            this.cloneFieldEntry(var2, (Entry)var3.next());
         }

         if (this.fields.isImmutable()) {
            var2.makeImmutable();
         }

         return var2;
      }
   }

   public Object getField(FieldSet.FieldDescriptorLite var1) {
      Object var2 = this.fields.get(var1);
      return var2 instanceof LazyField ? ((LazyField)var2).getValue() : var2;
   }

   public int getMessageSetSerializedSize() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.fields.getNumArrayEntries(); ++var2) {
         var1 += this.getMessageSetSerializedSize(this.fields.getArrayEntryAt(var2));
      }

      for(Iterator var3 = this.fields.getOverflowEntries().iterator(); var3.hasNext(); var1 += this.getMessageSetSerializedSize((Entry)var3.next())) {
      }

      return var1;
   }

   public Object getRepeatedField(FieldSet.FieldDescriptorLite var1, int var2) {
      if (var1.isRepeated()) {
         Object var3 = this.getField(var1);
         if (var3 != null) {
            return ((List)var3).get(var2);
         } else {
            throw new IndexOutOfBoundsException();
         }
      } else {
         throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
      }
   }

   public int getRepeatedFieldCount(FieldSet.FieldDescriptorLite var1) {
      if (var1.isRepeated()) {
         Object var2 = this.getField(var1);
         return var2 == null ? 0 : ((List)var2).size();
      } else {
         throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
      }
   }

   public int getSerializedSize() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.fields.getNumArrayEntries(); ++var2) {
         Entry var3 = this.fields.getArrayEntryAt(var2);
         var1 += computeFieldSize((FieldSet.FieldDescriptorLite)var3.getKey(), var3.getValue());
      }

      Entry var4;
      for(Iterator var5 = this.fields.getOverflowEntries().iterator(); var5.hasNext(); var1 += computeFieldSize((FieldSet.FieldDescriptorLite)var4.getKey(), var4.getValue())) {
         var4 = (Entry)var5.next();
      }

      return var1;
   }

   public boolean hasField(FieldSet.FieldDescriptorLite var1) {
      if (!var1.isRepeated()) {
         return this.fields.get(var1) != null;
      } else {
         throw new IllegalArgumentException("hasField() can only be called on non-repeated fields.");
      }
   }

   public boolean isImmutable() {
      return this.isImmutable;
   }

   public boolean isInitialized() {
      for(int var1 = 0; var1 < this.fields.getNumArrayEntries(); ++var1) {
         if (!this.isInitialized(this.fields.getArrayEntryAt(var1))) {
            return false;
         }
      }

      Iterator var2 = this.fields.getOverflowEntries().iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(this.isInitialized((Entry)var2.next()));

      return false;
   }

   public Iterator iterator() {
      return (Iterator)(this.hasLazyField ? new LazyField.LazyIterator(this.fields.entrySet().iterator()) : this.fields.entrySet().iterator());
   }

   public void makeImmutable() {
      if (!this.isImmutable) {
         this.fields.makeImmutable();
         this.isImmutable = true;
      }
   }

   public void mergeFrom(FieldSet var1) {
      for(int var2 = 0; var2 < var1.fields.getNumArrayEntries(); ++var2) {
         this.mergeFromField(var1.fields.getArrayEntryAt(var2));
      }

      Iterator var3 = var1.fields.getOverflowEntries().iterator();

      while(var3.hasNext()) {
         this.mergeFromField((Entry)var3.next());
      }

   }

   public void setField(FieldSet.FieldDescriptorLite var1, Object var2) {
      if (var1.isRepeated()) {
         if (!(var2 instanceof List)) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
         }

         ArrayList var3 = new ArrayList();
         var3.addAll((List)var2);
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            Object var4 = var5.next();
            verifyType(var1.getLiteType(), var4);
         }

         var2 = var3;
      } else {
         verifyType(var1.getLiteType(), var2);
      }

      if (var2 instanceof LazyField) {
         this.hasLazyField = true;
      }

      this.fields.put((Comparable)var1, var2);
   }

   public void setRepeatedField(FieldSet.FieldDescriptorLite var1, int var2, Object var3) {
      if (var1.isRepeated()) {
         Object var4 = this.getField(var1);
         if (var4 != null) {
            verifyType(var1.getLiteType(), var3);
            ((List)var4).set(var2, var3);
         } else {
            throw new IndexOutOfBoundsException();
         }
      } else {
         throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
      }
   }

   public void writeMessageSetTo(CodedOutputStream var1) throws IOException {
      for(int var2 = 0; var2 < this.fields.getNumArrayEntries(); ++var2) {
         this.writeMessageSetTo(this.fields.getArrayEntryAt(var2), var1);
      }

      Iterator var3 = this.fields.getOverflowEntries().iterator();

      while(var3.hasNext()) {
         this.writeMessageSetTo((Entry)var3.next(), var1);
      }

   }

   public void writeTo(CodedOutputStream var1) throws IOException {
      for(int var2 = 0; var2 < this.fields.getNumArrayEntries(); ++var2) {
         Entry var3 = this.fields.getArrayEntryAt(var2);
         writeField((FieldSet.FieldDescriptorLite)var3.getKey(), var3.getValue(), var1);
      }

      Iterator var5 = this.fields.getOverflowEntries().iterator();

      while(var5.hasNext()) {
         Entry var4 = (Entry)var5.next();
         writeField((FieldSet.FieldDescriptorLite)var4.getKey(), var4.getValue(), var1);
      }

   }

   public interface FieldDescriptorLite extends Comparable {
      Internal.EnumLiteMap getEnumType();

      WireFormat.JavaType getLiteJavaType();

      WireFormat.FieldType getLiteType();

      int getNumber();

      MessageLite.Builder internalMergeFrom(MessageLite.Builder var1, MessageLite var2);

      boolean isPacked();

      boolean isRepeated();
   }
}
