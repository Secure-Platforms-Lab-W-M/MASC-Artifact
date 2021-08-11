/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Internal;
import com.google.protobuf.LazyField;
import com.google.protobuf.MessageLite;
import com.google.protobuf.SmallSortedMap;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class FieldSet<FieldDescriptorType extends FieldDescriptorLite<FieldDescriptorType>> {
    private static final FieldSet DEFAULT_INSTANCE = new FieldSet<FieldDescriptorType>(true);
    private final SmallSortedMap<FieldDescriptorType, Object> fields;
    private boolean hasLazyField = false;
    private boolean isImmutable;

    private FieldSet() {
        this.fields = SmallSortedMap.newFieldMap(16);
    }

    private FieldSet(boolean bl) {
        this.fields = SmallSortedMap.newFieldMap(0);
        this.makeImmutable();
    }

    private void cloneFieldEntry(Map<FieldDescriptorType, Object> map, Map.Entry<FieldDescriptorType, Object> object) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite)object.getKey();
        if ((object = object.getValue()) instanceof LazyField) {
            map.put((FieldDescriptorLite)fieldDescriptorLite, ((LazyField)object).getValue());
            return;
        }
        map.put((FieldDescriptorLite)fieldDescriptorLite, object);
    }

    private static int computeElementSize(WireFormat.FieldType fieldType, int n, Object object) {
        int n2;
        n = n2 = CodedOutputStream.computeTagSize(n);
        if (fieldType == WireFormat.FieldType.GROUP) {
            n = n2 * 2;
        }
        return FieldSet.computeElementSizeNoTag(fieldType, object) + n;
    }

    private static int computeElementSizeNoTag(WireFormat.FieldType fieldType, Object object) {
        switch (.$SwitchMap$com$google$protobuf$WireFormat$FieldType[fieldType.ordinal()]) {
            default: {
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
            }
            case 18: {
                return CodedOutputStream.computeEnumSizeNoTag(((Internal.EnumLite)object).getNumber());
            }
            case 17: {
                if (object instanceof LazyField) {
                    return CodedOutputStream.computeLazyFieldSizeNoTag((LazyField)object);
                }
                return CodedOutputStream.computeMessageSizeNoTag((MessageLite)object);
            }
            case 16: {
                return CodedOutputStream.computeGroupSizeNoTag((MessageLite)object);
            }
            case 15: {
                return CodedOutputStream.computeSInt64SizeNoTag((Long)object);
            }
            case 14: {
                return CodedOutputStream.computeSInt32SizeNoTag((Integer)object);
            }
            case 13: {
                return CodedOutputStream.computeSFixed64SizeNoTag((Long)object);
            }
            case 12: {
                return CodedOutputStream.computeSFixed32SizeNoTag((Integer)object);
            }
            case 11: {
                return CodedOutputStream.computeUInt32SizeNoTag((Integer)object);
            }
            case 10: {
                return CodedOutputStream.computeBytesSizeNoTag((ByteString)object);
            }
            case 9: {
                return CodedOutputStream.computeStringSizeNoTag((String)object);
            }
            case 8: {
                return CodedOutputStream.computeBoolSizeNoTag((Boolean)object);
            }
            case 7: {
                return CodedOutputStream.computeFixed32SizeNoTag((Integer)object);
            }
            case 6: {
                return CodedOutputStream.computeFixed64SizeNoTag((Long)object);
            }
            case 5: {
                return CodedOutputStream.computeInt32SizeNoTag((Integer)object);
            }
            case 4: {
                return CodedOutputStream.computeUInt64SizeNoTag((Long)object);
            }
            case 3: {
                return CodedOutputStream.computeInt64SizeNoTag((Long)object);
            }
            case 2: {
                return CodedOutputStream.computeFloatSizeNoTag(((Float)object).floatValue());
            }
            case 1: 
        }
        return CodedOutputStream.computeDoubleSizeNoTag((Double)object);
    }

    public static int computeFieldSize(FieldDescriptorLite<?> iterator, Object object) {
        WireFormat.FieldType fieldType = iterator.getLiteType();
        int n = iterator.getNumber();
        if (iterator.isRepeated()) {
            if (iterator.isPacked()) {
                int n2 = 0;
                iterator = ((List)object).iterator();
                while (iterator.hasNext()) {
                    n2 += FieldSet.computeElementSizeNoTag(fieldType, iterator.next());
                }
                return CodedOutputStream.computeTagSize(n) + n2 + CodedOutputStream.computeRawVarint32Size(n2);
            }
            int n3 = 0;
            iterator = ((List)object).iterator();
            while (iterator.hasNext()) {
                n3 += FieldSet.computeElementSize(fieldType, n, iterator.next());
            }
            return n3;
        }
        return FieldSet.computeElementSize(fieldType, n, object);
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> emptySet() {
        return DEFAULT_INSTANCE;
    }

    private int getMessageSetSerializedSize(Map.Entry<FieldDescriptorType, Object> entry) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite)entry.getKey();
        Object object = entry.getValue();
        if (fieldDescriptorLite.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !fieldDescriptorLite.isRepeated() && !fieldDescriptorLite.isPacked()) {
            if (object instanceof LazyField) {
                return CodedOutputStream.computeLazyFieldMessageSetExtensionSize(((FieldDescriptorLite)entry.getKey()).getNumber(), (LazyField)object);
            }
            return CodedOutputStream.computeMessageSetExtensionSize(((FieldDescriptorLite)entry.getKey()).getNumber(), (MessageLite)object);
        }
        return FieldSet.computeFieldSize(fieldDescriptorLite, object);
    }

    static int getWireFormatForFieldType(WireFormat.FieldType fieldType, boolean bl) {
        if (bl) {
            return 2;
        }
        return fieldType.getWireType();
    }

    private boolean isInitialized(Map.Entry<FieldDescriptorType, Object> iterator) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite)iterator.getKey();
        if (fieldDescriptorLite.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            if (fieldDescriptorLite.isRepeated()) {
                iterator = ((List)iterator.getValue()).iterator();
                while (iterator.hasNext()) {
                    if (((MessageLite)iterator.next()).isInitialized()) continue;
                    return false;
                }
                return true;
            }
            if ((iterator = iterator.getValue()) instanceof MessageLite) {
                if (!((MessageLite)((Object)iterator)).isInitialized()) {
                    return false;
                }
            } else {
                if (iterator instanceof LazyField) {
                    return true;
                }
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
        }
        return true;
    }

    private void mergeFromField(Map.Entry<FieldDescriptorType, Object> object) {
        Object object2;
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite)object.getKey();
        object = object2 = object.getValue();
        if (object2 instanceof LazyField) {
            object = ((LazyField)object2).getValue();
        }
        if (fieldDescriptorLite.isRepeated()) {
            object2 = this.getField(fieldDescriptorLite);
            if (object2 == null) {
                this.fields.put((FieldDescriptorType)fieldDescriptorLite, (Object)new ArrayList((List)object));
            } else {
                ((List)object2).addAll((List)object);
            }
            return;
        }
        if (fieldDescriptorLite.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
            object2 = this.getField(fieldDescriptorLite);
            if (object2 == null) {
                this.fields.put((FieldDescriptorType)fieldDescriptorLite, object);
            } else {
                this.fields.put((FieldDescriptorType)fieldDescriptorLite, (Object)fieldDescriptorLite.internalMergeFrom(((MessageLite)object2).toBuilder(), (MessageLite)object).build());
            }
            return;
        }
        this.fields.put((FieldDescriptorType)fieldDescriptorLite, object);
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> newFieldSet() {
        return new FieldSet<FieldDescriptorType>();
    }

    public static Object readPrimitiveField(CodedInputStream codedInputStream, WireFormat.FieldType fieldType) throws IOException {
        switch (.$SwitchMap$com$google$protobuf$WireFormat$FieldType[fieldType.ordinal()]) {
            default: {
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
            }
            case 18: {
                throw new IllegalArgumentException("readPrimitiveField() cannot handle enums.");
            }
            case 17: {
                throw new IllegalArgumentException("readPrimitiveField() cannot handle embedded messages.");
            }
            case 16: {
                throw new IllegalArgumentException("readPrimitiveField() cannot handle nested groups.");
            }
            case 15: {
                return codedInputStream.readSInt64();
            }
            case 14: {
                return codedInputStream.readSInt32();
            }
            case 13: {
                return codedInputStream.readSFixed64();
            }
            case 12: {
                return codedInputStream.readSFixed32();
            }
            case 11: {
                return codedInputStream.readUInt32();
            }
            case 10: {
                return codedInputStream.readBytes();
            }
            case 9: {
                return codedInputStream.readString();
            }
            case 8: {
                return codedInputStream.readBool();
            }
            case 7: {
                return codedInputStream.readFixed32();
            }
            case 6: {
                return codedInputStream.readFixed64();
            }
            case 5: {
                return codedInputStream.readInt32();
            }
            case 4: {
                return codedInputStream.readUInt64();
            }
            case 3: {
                return codedInputStream.readInt64();
            }
            case 2: {
                return Float.valueOf(codedInputStream.readFloat());
            }
            case 1: 
        }
        return codedInputStream.readDouble();
    }

    private static void verifyType(WireFormat.FieldType fieldType, Object object) {
        if (object != null) {
            boolean bl = false;
            switch (.$SwitchMap$com$google$protobuf$WireFormat$JavaType[fieldType.getJavaType().ordinal()]) {
                default: {
                    break;
                }
                case 9: {
                    if (!(object instanceof MessageLite) && !(object instanceof LazyField)) {
                        bl = false;
                        break;
                    }
                    bl = true;
                    break;
                }
                case 8: {
                    bl = object instanceof Internal.EnumLite;
                    break;
                }
                case 7: {
                    bl = object instanceof ByteString;
                    break;
                }
                case 6: {
                    bl = object instanceof String;
                    break;
                }
                case 5: {
                    bl = object instanceof Boolean;
                    break;
                }
                case 4: {
                    bl = object instanceof Double;
                    break;
                }
                case 3: {
                    bl = object instanceof Float;
                    break;
                }
                case 2: {
                    bl = object instanceof Long;
                    break;
                }
                case 1: {
                    bl = object instanceof Integer;
                }
            }
            if (bl) {
                return;
            }
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
        throw null;
    }

    private static void writeElement(CodedOutputStream codedOutputStream, WireFormat.FieldType fieldType, int n, Object object) throws IOException {
        if (fieldType == WireFormat.FieldType.GROUP) {
            codedOutputStream.writeGroup(n, (MessageLite)object);
            return;
        }
        codedOutputStream.writeTag(n, FieldSet.getWireFormatForFieldType(fieldType, false));
        FieldSet.writeElementNoTag(codedOutputStream, fieldType, object);
    }

    private static void writeElementNoTag(CodedOutputStream codedOutputStream, WireFormat.FieldType fieldType, Object object) throws IOException {
        switch (.$SwitchMap$com$google$protobuf$WireFormat$FieldType[fieldType.ordinal()]) {
            default: {
                return;
            }
            case 18: {
                codedOutputStream.writeEnumNoTag(((Internal.EnumLite)object).getNumber());
                return;
            }
            case 17: {
                codedOutputStream.writeMessageNoTag((MessageLite)object);
                return;
            }
            case 16: {
                codedOutputStream.writeGroupNoTag((MessageLite)object);
                return;
            }
            case 15: {
                codedOutputStream.writeSInt64NoTag((Long)object);
                return;
            }
            case 14: {
                codedOutputStream.writeSInt32NoTag((Integer)object);
                return;
            }
            case 13: {
                codedOutputStream.writeSFixed64NoTag((Long)object);
                return;
            }
            case 12: {
                codedOutputStream.writeSFixed32NoTag((Integer)object);
                return;
            }
            case 11: {
                codedOutputStream.writeUInt32NoTag((Integer)object);
                return;
            }
            case 10: {
                codedOutputStream.writeBytesNoTag((ByteString)object);
                return;
            }
            case 9: {
                codedOutputStream.writeStringNoTag((String)object);
                return;
            }
            case 8: {
                codedOutputStream.writeBoolNoTag((Boolean)object);
                return;
            }
            case 7: {
                codedOutputStream.writeFixed32NoTag((Integer)object);
                return;
            }
            case 6: {
                codedOutputStream.writeFixed64NoTag((Long)object);
                return;
            }
            case 5: {
                codedOutputStream.writeInt32NoTag((Integer)object);
                return;
            }
            case 4: {
                codedOutputStream.writeUInt64NoTag((Long)object);
                return;
            }
            case 3: {
                codedOutputStream.writeInt64NoTag((Long)object);
                return;
            }
            case 2: {
                codedOutputStream.writeFloatNoTag(((Float)object).floatValue());
                return;
            }
            case 1: 
        }
        codedOutputStream.writeDoubleNoTag((Double)object);
    }

    public static void writeField(FieldDescriptorLite<?> iterator, Object object, CodedOutputStream codedOutputStream) throws IOException {
        WireFormat.FieldType fieldType = iterator.getLiteType();
        int n = iterator.getNumber();
        if (iterator.isRepeated()) {
            object = (List)object;
            if (iterator.isPacked()) {
                codedOutputStream.writeTag(n, 2);
                n = 0;
                iterator = object.iterator();
                while (iterator.hasNext()) {
                    n += FieldSet.computeElementSizeNoTag(fieldType, iterator.next());
                }
                codedOutputStream.writeRawVarint32(n);
                iterator = object.iterator();
                while (iterator.hasNext()) {
                    FieldSet.writeElementNoTag(codedOutputStream, fieldType, iterator.next());
                }
            } else {
                iterator = object.iterator();
                while (iterator.hasNext()) {
                    FieldSet.writeElement(codedOutputStream, fieldType, n, iterator.next());
                }
            }
            return;
        }
        if (object instanceof LazyField) {
            FieldSet.writeElement(codedOutputStream, fieldType, n, ((LazyField)object).getValue());
            return;
        }
        FieldSet.writeElement(codedOutputStream, fieldType, n, object);
    }

    private void writeMessageSetTo(Map.Entry<FieldDescriptorType, Object> entry, CodedOutputStream codedOutputStream) throws IOException {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite)entry.getKey();
        if (fieldDescriptorLite.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !fieldDescriptorLite.isRepeated() && !fieldDescriptorLite.isPacked()) {
            codedOutputStream.writeMessageSetExtension(((FieldDescriptorLite)entry.getKey()).getNumber(), (MessageLite)entry.getValue());
            return;
        }
        FieldSet.writeField(fieldDescriptorLite, entry.getValue(), codedOutputStream);
    }

    public void addRepeatedField(FieldDescriptorType arrayList, Object object) {
        if (arrayList.isRepeated()) {
            FieldSet.verifyType(arrayList.getLiteType(), object);
            ArrayList<Object> arrayList2 = this.getField(arrayList);
            if (arrayList2 == null) {
                arrayList2 = new ArrayList<Object>();
                this.fields.put((FieldDescriptorType)arrayList, (Object)arrayList2);
                arrayList = arrayList2;
            } else {
                arrayList = arrayList2;
            }
            arrayList.add(object);
            return;
        }
        throw new IllegalArgumentException("addRepeatedField() can only be called on repeated fields.");
    }

    public void clear() {
        this.fields.clear();
        this.hasLazyField = false;
    }

    public void clearField(FieldDescriptorType FieldDescriptorType) {
        this.fields.remove(FieldDescriptorType);
        if (this.fields.isEmpty()) {
            this.hasLazyField = false;
        }
    }

    public FieldSet<FieldDescriptorType> clone() {
        FieldSet<T> fieldSet = FieldSet.newFieldSet();
        for (int i = 0; i < this.fields.getNumArrayEntries(); ++i) {
            Map.Entry<FieldDescriptorType, Object> entry = this.fields.getArrayEntryAt(i);
            fieldSet.setField((FieldDescriptorLite)((FieldDescriptorLite)entry.getKey()), entry.getValue());
        }
        for (Map.Entry entry : this.fields.getOverflowEntries()) {
            fieldSet.setField((FieldDescriptorLite)((FieldDescriptorLite)entry.getKey()), entry.getValue());
        }
        fieldSet.hasLazyField = this.hasLazyField;
        return fieldSet;
    }

    public Map<FieldDescriptorType, Object> getAllFields() {
        if (this.hasLazyField) {
            SmallSortedMap smallSortedMap = SmallSortedMap.newFieldMap(16);
            for (int i = 0; i < this.fields.getNumArrayEntries(); ++i) {
                this.cloneFieldEntry(smallSortedMap, this.fields.getArrayEntryAt(i));
            }
            Iterator<Map.Entry<FieldDescriptorType, Object>> iterator = this.fields.getOverflowEntries().iterator();
            while (iterator.hasNext()) {
                this.cloneFieldEntry(smallSortedMap, iterator.next());
            }
            if (this.fields.isImmutable()) {
                smallSortedMap.makeImmutable();
            }
            return smallSortedMap;
        }
        if (this.fields.isImmutable()) {
            return this.fields;
        }
        return Collections.unmodifiableMap(this.fields);
    }

    public Object getField(FieldDescriptorType object) {
        if ((object = this.fields.get(object)) instanceof LazyField) {
            return ((LazyField)object).getValue();
        }
        return object;
    }

    public int getMessageSetSerializedSize() {
        int n = 0;
        for (int i = 0; i < this.fields.getNumArrayEntries(); ++i) {
            n += this.getMessageSetSerializedSize(this.fields.getArrayEntryAt(i));
        }
        Iterator<Map.Entry<FieldDescriptorType, Object>> iterator = this.fields.getOverflowEntries().iterator();
        while (iterator.hasNext()) {
            n += this.getMessageSetSerializedSize(iterator.next());
        }
        return n;
    }

    public Object getRepeatedField(FieldDescriptorType object, int n) {
        if (object.isRepeated()) {
            if ((object = this.getField(object)) != null) {
                return ((List)object).get(n);
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
    }

    public int getRepeatedFieldCount(FieldDescriptorType object) {
        if (object.isRepeated()) {
            if ((object = this.getField(object)) == null) {
                return 0;
            }
            return ((List)object).size();
        }
        throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
    }

    public int getSerializedSize() {
        int n = 0;
        for (int i = 0; i < this.fields.getNumArrayEntries(); ++i) {
            Map.Entry<FieldDescriptorType, Object> entry = this.fields.getArrayEntryAt(i);
            n += FieldSet.computeFieldSize((FieldDescriptorLite)entry.getKey(), entry.getValue());
        }
        for (Map.Entry entry : this.fields.getOverflowEntries()) {
            n += FieldSet.computeFieldSize((FieldDescriptorLite)entry.getKey(), entry.getValue());
        }
        return n;
    }

    public boolean hasField(FieldDescriptorType FieldDescriptorType) {
        if (!FieldDescriptorType.isRepeated()) {
            if (this.fields.get(FieldDescriptorType) != null) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("hasField() can only be called on non-repeated fields.");
    }

    public boolean isImmutable() {
        return this.isImmutable;
    }

    public boolean isInitialized() {
        for (int i = 0; i < this.fields.getNumArrayEntries(); ++i) {
            if (this.isInitialized(this.fields.getArrayEntryAt(i))) continue;
            return false;
        }
        Iterator<Map.Entry<FieldDescriptorType, Object>> iterator = this.fields.getOverflowEntries().iterator();
        while (iterator.hasNext()) {
            if (this.isInitialized(iterator.next())) continue;
            return false;
        }
        return true;
    }

    public Iterator<Map.Entry<FieldDescriptorType, Object>> iterator() {
        if (this.hasLazyField) {
            return new LazyField.LazyIterator(this.fields.entrySet().iterator());
        }
        return this.fields.entrySet().iterator();
    }

    public void makeImmutable() {
        if (this.isImmutable) {
            return;
        }
        this.fields.makeImmutable();
        this.isImmutable = true;
    }

    public void mergeFrom(FieldSet<FieldDescriptorType> object) {
        for (int i = 0; i < object.fields.getNumArrayEntries(); ++i) {
            this.mergeFromField(object.fields.getArrayEntryAt(i));
        }
        object = object.fields.getOverflowEntries().iterator();
        while (object.hasNext()) {
            this.mergeFromField((Map.Entry)object.next());
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setField(FieldDescriptorType FieldDescriptorType, Object object) {
        if (FieldDescriptorType.isRepeated()) {
            if (!(object instanceof List)) throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List)object);
            for (Object e : arrayList) {
                FieldSet.verifyType(FieldDescriptorType.getLiteType(), e);
            }
            object = arrayList;
        } else {
            FieldSet.verifyType(FieldDescriptorType.getLiteType(), object);
        }
        if (object instanceof LazyField) {
            this.hasLazyField = true;
        }
        this.fields.put(FieldDescriptorType, object);
    }

    public void setRepeatedField(FieldDescriptorType FieldDescriptorType, int n, Object object) {
        if (FieldDescriptorType.isRepeated()) {
            Object object2 = this.getField(FieldDescriptorType);
            if (object2 != null) {
                FieldSet.verifyType(FieldDescriptorType.getLiteType(), object);
                ((List)object2).set(n, object);
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("getRepeatedField() can only be called on repeated fields.");
    }

    public void writeMessageSetTo(CodedOutputStream codedOutputStream) throws IOException {
        for (int i = 0; i < this.fields.getNumArrayEntries(); ++i) {
            this.writeMessageSetTo(this.fields.getArrayEntryAt(i), codedOutputStream);
        }
        Iterator<Map.Entry<FieldDescriptorType, Object>> iterator = this.fields.getOverflowEntries().iterator();
        while (iterator.hasNext()) {
            this.writeMessageSetTo(iterator.next(), codedOutputStream);
        }
    }

    public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
        for (int i = 0; i < this.fields.getNumArrayEntries(); ++i) {
            Map.Entry<FieldDescriptorType, Object> entry = this.fields.getArrayEntryAt(i);
            FieldSet.writeField((FieldDescriptorLite)entry.getKey(), entry.getValue(), codedOutputStream);
        }
        for (Map.Entry entry : this.fields.getOverflowEntries()) {
            FieldSet.writeField((FieldDescriptorLite)entry.getKey(), entry.getValue(), codedOutputStream);
        }
    }

    public static interface FieldDescriptorLite<T extends FieldDescriptorLite<T>>
    extends Comparable<T> {
        public Internal.EnumLiteMap<?> getEnumType();

        public WireFormat.JavaType getLiteJavaType();

        public WireFormat.FieldType getLiteType();

        public int getNumber();

        public MessageLite.Builder internalMergeFrom(MessageLite.Builder var1, MessageLite var2);

        public boolean isPacked();

        public boolean isRepeated();
    }

}

