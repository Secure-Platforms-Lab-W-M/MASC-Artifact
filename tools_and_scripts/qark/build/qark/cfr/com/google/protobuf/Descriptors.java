/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.FieldSet;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.WireFormat;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Descriptors {
    private static String computeFullName(FileDescriptor object, Descriptor object2, String string2) {
        if (object2 != null) {
            object = new StringBuilder();
            object.append(object2.getFullName());
            object.append('.');
            object.append(string2);
            return object.toString();
        }
        if (object.getPackage().length() > 0) {
            object2 = new StringBuilder();
            object2.append(object.getPackage());
            object2.append('.');
            object2.append(string2);
            return object2.toString();
        }
        return string2;
    }

    public static final class Descriptor
    implements GenericDescriptor {
        private final Descriptor containingType;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] extensions;
        private final FieldDescriptor[] fields;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private final Descriptor[] nestedTypes;
        private DescriptorProtos.DescriptorProto proto;

        private Descriptor(DescriptorProtos.DescriptorProto descriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int n) throws DescriptorValidationException {
            this.index = n;
            this.proto = descriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, descriptorProto.getName());
            this.file = fileDescriptor;
            this.containingType = descriptor;
            this.nestedTypes = new Descriptor[descriptorProto.getNestedTypeCount()];
            for (n = 0; n < descriptorProto.getNestedTypeCount(); ++n) {
                this.nestedTypes[n] = new Descriptor(descriptorProto.getNestedType(n), fileDescriptor, this, n);
            }
            this.enumTypes = new EnumDescriptor[descriptorProto.getEnumTypeCount()];
            for (n = 0; n < descriptorProto.getEnumTypeCount(); ++n) {
                this.enumTypes[n] = new EnumDescriptor(descriptorProto.getEnumType(n), fileDescriptor, this, n);
            }
            this.fields = new FieldDescriptor[descriptorProto.getFieldCount()];
            for (n = 0; n < descriptorProto.getFieldCount(); ++n) {
                this.fields[n] = new FieldDescriptor(descriptorProto.getField(n), fileDescriptor, this, n, false);
            }
            this.extensions = new FieldDescriptor[descriptorProto.getExtensionCount()];
            for (n = 0; n < descriptorProto.getExtensionCount(); ++n) {
                this.extensions[n] = new FieldDescriptor(descriptorProto.getExtension(n), fileDescriptor, this, n, true);
            }
            fileDescriptor.pool.addSymbol(this);
        }

        private void crossLink() throws DescriptorValidationException {
            int n;
            Descriptor[] arrdescriptor = this.nestedTypes;
            int n2 = arrdescriptor.length;
            for (n = 0; n < n2; ++n) {
                arrdescriptor[n].crossLink();
            }
            arrdescriptor = this.fields;
            n2 = arrdescriptor.length;
            for (n = 0; n < n2; ++n) {
                ((FieldDescriptor)((Object)arrdescriptor[n])).crossLink();
            }
            arrdescriptor = this.extensions;
            n2 = arrdescriptor.length;
            for (n = 0; n < n2; ++n) {
                ((FieldDescriptor)((Object)arrdescriptor[n])).crossLink();
            }
        }

        private void setProto(DescriptorProtos.DescriptorProto descriptorProto) {
            int n;
            Descriptor[] arrdescriptor;
            this.proto = descriptorProto;
            for (n = 0; n < (arrdescriptor = this.nestedTypes).length; ++n) {
                arrdescriptor[n].setProto(descriptorProto.getNestedType(n));
            }
            for (n = 0; n < (arrdescriptor = this.enumTypes).length; ++n) {
                ((EnumDescriptor)((Object)arrdescriptor[n])).setProto(descriptorProto.getEnumType(n));
            }
            for (n = 0; n < (arrdescriptor = this.fields).length; ++n) {
                ((FieldDescriptor)((Object)arrdescriptor[n])).setProto(descriptorProto.getField(n));
            }
            for (n = 0; n < (arrdescriptor = this.extensions).length; ++n) {
                ((FieldDescriptor)((Object)arrdescriptor[n])).setProto(descriptorProto.getExtension(n));
            }
        }

        public EnumDescriptor findEnumTypeByName(String object) {
            DescriptorPool descriptorPool = this.file.pool;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.fullName);
            stringBuilder.append('.');
            stringBuilder.append((String)object);
            object = descriptorPool.findSymbol(stringBuilder.toString());
            if (object != null && object instanceof EnumDescriptor) {
                return (EnumDescriptor)object;
            }
            return null;
        }

        public FieldDescriptor findFieldByName(String object) {
            DescriptorPool descriptorPool = this.file.pool;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.fullName);
            stringBuilder.append('.');
            stringBuilder.append((String)object);
            object = descriptorPool.findSymbol(stringBuilder.toString());
            if (object != null && object instanceof FieldDescriptor) {
                return (FieldDescriptor)object;
            }
            return null;
        }

        public FieldDescriptor findFieldByNumber(int n) {
            return (FieldDescriptor)this.file.pool.fieldsByNumber.get(new DescriptorPool.DescriptorIntPair(this, n));
        }

        public Descriptor findNestedTypeByName(String object) {
            DescriptorPool descriptorPool = this.file.pool;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.fullName);
            stringBuilder.append('.');
            stringBuilder.append((String)object);
            object = descriptorPool.findSymbol(stringBuilder.toString());
            if (object != null && object instanceof Descriptor) {
                return (Descriptor)object;
            }
            return null;
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(this.extensions));
        }

        public List<FieldDescriptor> getFields() {
            return Collections.unmodifiableList(Arrays.asList(this.fields));
        }

        @Override
        public FileDescriptor getFile() {
            return this.file;
        }

        @Override
        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public String getName() {
            return this.proto.getName();
        }

        public List<Descriptor> getNestedTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.nestedTypes));
        }

        public DescriptorProtos.MessageOptions getOptions() {
            return this.proto.getOptions();
        }

        public boolean isExtensionNumber(int n) {
            for (DescriptorProtos.DescriptorProto.ExtensionRange extensionRange : this.proto.getExtensionRangeList()) {
                if (extensionRange.getStart() > n || n >= extensionRange.getEnd()) continue;
                return true;
            }
            return false;
        }

        @Override
        public DescriptorProtos.DescriptorProto toProto() {
            return this.proto;
        }
    }

    private static final class DescriptorPool {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final Set<FileDescriptor> dependencies = new HashSet<FileDescriptor>();
        private final Map<String, GenericDescriptor> descriptorsByName = new HashMap<String, GenericDescriptor>();
        private final Map<DescriptorIntPair, EnumValueDescriptor> enumValuesByNumber = new HashMap<DescriptorIntPair, EnumValueDescriptor>();
        private final Map<DescriptorIntPair, FieldDescriptor> fieldsByNumber = new HashMap<DescriptorIntPair, FieldDescriptor>();

        DescriptorPool(FileDescriptor[] object) {
            for (int i = 0; i < object.length; ++i) {
                this.dependencies.add(object[i]);
                this.importPublicDependencies((FileDescriptor)object[i]);
            }
            for (FileDescriptor fileDescriptor : this.dependencies) {
                try {
                    this.addPackage(fileDescriptor.getPackage(), fileDescriptor);
                }
                catch (DescriptorValidationException descriptorValidationException) {}
            }
        }

        private void importPublicDependencies(FileDescriptor object) {
            for (FileDescriptor fileDescriptor : object.getPublicDependencies()) {
                if (!this.dependencies.add(fileDescriptor)) continue;
                this.importPublicDependencies(fileDescriptor);
            }
        }

        static void validateSymbolName(GenericDescriptor genericDescriptor) throws DescriptorValidationException {
            String string2 = genericDescriptor.getName();
            if (string2.length() != 0) {
                boolean bl = true;
                for (int i = 0; i < string2.length(); ++i) {
                    char c = string2.charAt(i);
                    boolean bl2 = bl;
                    if (c >= '') {
                        bl2 = false;
                    }
                    bl = bl2;
                    if (Character.isLetter(c)) continue;
                    bl = bl2;
                    if (c == '_') continue;
                    bl = Character.isDigit(c) && i > 0 ? bl2 : false;
                }
                if (bl) {
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append('\"');
                stringBuilder.append(string2);
                stringBuilder.append("\" is not a valid identifier.");
                throw new DescriptorValidationException(genericDescriptor, stringBuilder.toString());
            }
            throw new DescriptorValidationException(genericDescriptor, "Missing name.");
        }

        void addEnumValueByNumber(EnumValueDescriptor enumValueDescriptor) {
            DescriptorIntPair descriptorIntPair = new DescriptorIntPair(enumValueDescriptor.getType(), enumValueDescriptor.getNumber());
            if ((enumValueDescriptor = this.enumValuesByNumber.put(descriptorIntPair, enumValueDescriptor)) != null) {
                this.enumValuesByNumber.put(descriptorIntPair, enumValueDescriptor);
            }
        }

        void addFieldByNumber(FieldDescriptor fieldDescriptor) throws DescriptorValidationException {
            Object object = new DescriptorIntPair(fieldDescriptor.getContainingType(), fieldDescriptor.getNumber());
            FieldDescriptor fieldDescriptor2 = this.fieldsByNumber.put((DescriptorIntPair)object, fieldDescriptor);
            if (fieldDescriptor2 == null) {
                return;
            }
            this.fieldsByNumber.put((DescriptorIntPair)object, fieldDescriptor2);
            object = new StringBuilder();
            object.append("Field number ");
            object.append(fieldDescriptor.getNumber());
            object.append("has already been used in \"");
            object.append(fieldDescriptor.getContainingType().getFullName());
            object.append("\" by field \"");
            object.append(fieldDescriptor2.getName());
            object.append("\".");
            throw new DescriptorValidationException((GenericDescriptor)fieldDescriptor, object.toString());
        }

        void addPackage(String charSequence, FileDescriptor fileDescriptor) throws DescriptorValidationException {
            String string2;
            int n = charSequence.lastIndexOf(46);
            if (n == -1) {
                string2 = charSequence;
            } else {
                this.addPackage(charSequence.substring(0, n), fileDescriptor);
                string2 = charSequence.substring(n + 1);
            }
            GenericDescriptor genericDescriptor = this.descriptorsByName.put((String)charSequence, new PackageDescriptor(string2, (String)charSequence, fileDescriptor));
            if (genericDescriptor != null) {
                this.descriptorsByName.put((String)charSequence, genericDescriptor);
                if (genericDescriptor instanceof PackageDescriptor) {
                    return;
                }
                charSequence = new StringBuilder();
                charSequence.append('\"');
                charSequence.append(string2);
                charSequence.append("\" is already defined (as something other than a ");
                charSequence.append("package) in file \"");
                charSequence.append(genericDescriptor.getFile().getName());
                charSequence.append("\".");
                throw new DescriptorValidationException(fileDescriptor, charSequence.toString());
            }
        }

        void addSymbol(GenericDescriptor genericDescriptor) throws DescriptorValidationException {
            DescriptorPool.validateSymbolName(genericDescriptor);
            String string2 = genericDescriptor.getFullName();
            int n = string2.lastIndexOf(46);
            Object object = this.descriptorsByName.put(string2, genericDescriptor);
            if (object != null) {
                this.descriptorsByName.put(string2, (GenericDescriptor)object);
                if (genericDescriptor.getFile() == object.getFile()) {
                    if (n == -1) {
                        object = new StringBuilder();
                        object.append('\"');
                        object.append(string2);
                        object.append("\" is already defined.");
                        throw new DescriptorValidationException(genericDescriptor, object.toString());
                    }
                    object = new StringBuilder();
                    object.append('\"');
                    object.append(string2.substring(n + 1));
                    object.append("\" is already defined in \"");
                    object.append(string2.substring(0, n));
                    object.append("\".");
                    throw new DescriptorValidationException(genericDescriptor, object.toString());
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append('\"');
                stringBuilder.append(string2);
                stringBuilder.append("\" is already defined in file \"");
                stringBuilder.append(object.getFile().getName());
                stringBuilder.append("\".");
                throw new DescriptorValidationException(genericDescriptor, stringBuilder.toString());
            }
        }

        GenericDescriptor findSymbol(String string2) {
            return this.findSymbol(string2, SearchFilter.ALL_SYMBOLS);
        }

        GenericDescriptor findSymbol(String string2, SearchFilter searchFilter) {
            Object object = this.descriptorsByName.get(string2);
            if (object != null && (searchFilter == SearchFilter.ALL_SYMBOLS || searchFilter == SearchFilter.TYPES_ONLY && this.isType((GenericDescriptor)object) || searchFilter == SearchFilter.AGGREGATES_ONLY && this.isAggregate((GenericDescriptor)object))) {
                return object;
            }
            object = this.dependencies.iterator();
            while (object.hasNext()) {
                GenericDescriptor genericDescriptor = FileDescriptor.access$1200((FileDescriptor)((FileDescriptor)object.next())).descriptorsByName.get(string2);
                if (genericDescriptor == null || searchFilter != SearchFilter.ALL_SYMBOLS && (searchFilter != SearchFilter.TYPES_ONLY || !this.isType(genericDescriptor)) && (searchFilter != SearchFilter.AGGREGATES_ONLY || !this.isAggregate(genericDescriptor))) continue;
                return genericDescriptor;
            }
            return null;
        }

        boolean isAggregate(GenericDescriptor genericDescriptor) {
            if (!(genericDescriptor instanceof Descriptor || genericDescriptor instanceof EnumDescriptor || genericDescriptor instanceof PackageDescriptor || genericDescriptor instanceof ServiceDescriptor)) {
                return false;
            }
            return true;
        }

        boolean isType(GenericDescriptor genericDescriptor) {
            if (!(genericDescriptor instanceof Descriptor) && !(genericDescriptor instanceof EnumDescriptor)) {
                return false;
            }
            return true;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        GenericDescriptor lookupSymbol(String var1_1, GenericDescriptor var2_2, SearchFilter var3_3) throws DescriptorValidationException {
            block7 : {
                if (!var1_1.startsWith(".")) break block7;
                var3_3 = this.findSymbol(var1_1.substring(1), (SearchFilter)var3_3);
                ** GOTO lbl23
            }
            var4_4 = var1_1.indexOf(46);
            var6_5 = var4_4 == -1 ? var1_1 : var1_1.substring(0, var4_4);
            var8_6 = new StringBuilder(var2_2.getFullName());
            do {
                block10 : {
                    block9 : {
                        block8 : {
                            if ((var5_7 = var8_6.lastIndexOf(".")) != -1) break block8;
                            var3_3 = this.findSymbol(var1_1, (SearchFilter)var3_3);
                            break block9;
                        }
                        var8_6.setLength(var5_7 + 1);
                        var8_6.append(var6_5);
                        var7_8 = this.findSymbol(var8_6.toString(), SearchFilter.AGGREGATES_ONLY);
                        if (var7_8 == null) break block10;
                        if (var4_4 != -1) {
                            var8_6.setLength(var5_7 + 1);
                            var8_6.append(var1_1);
                            var3_3 = this.findSymbol(var8_6.toString(), (SearchFilter)var3_3);
                        } else {
                            var3_3 = var7_8;
                        }
                    }
                    if (var3_3 != null) {
                        return var3_3;
                    }
                    var3_3 = new StringBuilder();
                    var3_3.append('\"');
                    var3_3.append(var1_1);
                    var3_3.append("\" is not defined.");
                    throw new DescriptorValidationException(var2_2, var3_3.toString());
                }
                var8_6.setLength(var5_7);
            } while (true);
        }

        private static final class DescriptorIntPair {
            private final GenericDescriptor descriptor;
            private final int number;

            DescriptorIntPair(GenericDescriptor genericDescriptor, int n) {
                this.descriptor = genericDescriptor;
                this.number = n;
            }

            public boolean equals(Object object) {
                boolean bl = object instanceof DescriptorIntPair;
                boolean bl2 = false;
                if (!bl) {
                    return false;
                }
                object = (DescriptorIntPair)object;
                bl = bl2;
                if (this.descriptor == object.descriptor) {
                    bl = bl2;
                    if (this.number == object.number) {
                        bl = true;
                    }
                }
                return bl;
            }

            public int hashCode() {
                return this.descriptor.hashCode() * 65535 + this.number;
            }
        }

        private static final class PackageDescriptor
        implements GenericDescriptor {
            private final FileDescriptor file;
            private final String fullName;
            private final String name;

            PackageDescriptor(String string2, String string3, FileDescriptor fileDescriptor) {
                this.file = fileDescriptor;
                this.fullName = string3;
                this.name = string2;
            }

            @Override
            public FileDescriptor getFile() {
                return this.file;
            }

            @Override
            public String getFullName() {
                return this.fullName;
            }

            @Override
            public String getName() {
                return this.name;
            }

            @Override
            public Message toProto() {
                return this.file.toProto();
            }
        }

        static final class SearchFilter
        extends Enum<SearchFilter> {
            private static final /* synthetic */ SearchFilter[] $VALUES;
            public static final /* enum */ SearchFilter AGGREGATES_ONLY;
            public static final /* enum */ SearchFilter ALL_SYMBOLS;
            public static final /* enum */ SearchFilter TYPES_ONLY;

            static {
                SearchFilter searchFilter;
                TYPES_ONLY = new SearchFilter();
                AGGREGATES_ONLY = new SearchFilter();
                ALL_SYMBOLS = searchFilter = new SearchFilter();
                $VALUES = new SearchFilter[]{TYPES_ONLY, AGGREGATES_ONLY, searchFilter};
            }

            private SearchFilter() {
            }

            public static SearchFilter valueOf(String string2) {
                return Enum.valueOf(SearchFilter.class, string2);
            }

            public static SearchFilter[] values() {
                return (SearchFilter[])$VALUES.clone();
            }
        }

    }

    public static class DescriptorValidationException
    extends Exception {
        private static final long serialVersionUID = 5750205775490483148L;
        private final String description;
        private final String name;
        private final Message proto;

        private DescriptorValidationException(FileDescriptor fileDescriptor, String string2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(fileDescriptor.getName());
            stringBuilder.append(": ");
            stringBuilder.append(string2);
            super(stringBuilder.toString());
            this.name = fileDescriptor.getName();
            this.proto = fileDescriptor.toProto();
            this.description = string2;
        }

        private DescriptorValidationException(GenericDescriptor genericDescriptor, String string2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(genericDescriptor.getFullName());
            stringBuilder.append(": ");
            stringBuilder.append(string2);
            super(stringBuilder.toString());
            this.name = genericDescriptor.getFullName();
            this.proto = genericDescriptor.toProto();
            this.description = string2;
        }

        private DescriptorValidationException(GenericDescriptor genericDescriptor, String string2, Throwable throwable) {
            this(genericDescriptor, string2);
            this.initCause(throwable);
        }

        public String getDescription() {
            return this.description;
        }

        public Message getProblemProto() {
            return this.proto;
        }

        public String getProblemSymbolName() {
            return this.name;
        }
    }

    public static final class EnumDescriptor
    implements GenericDescriptor,
    Internal.EnumLiteMap<EnumValueDescriptor> {
        private final Descriptor containingType;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private DescriptorProtos.EnumDescriptorProto proto;
        private EnumValueDescriptor[] values;

        private EnumDescriptor(DescriptorProtos.EnumDescriptorProto enumDescriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int n) throws DescriptorValidationException {
            this.index = n;
            this.proto = enumDescriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, enumDescriptorProto.getName());
            this.file = fileDescriptor;
            this.containingType = descriptor;
            if (enumDescriptorProto.getValueCount() != 0) {
                this.values = new EnumValueDescriptor[enumDescriptorProto.getValueCount()];
                for (n = 0; n < enumDescriptorProto.getValueCount(); ++n) {
                    this.values[n] = new EnumValueDescriptor(enumDescriptorProto.getValue(n), fileDescriptor, this, n);
                }
                fileDescriptor.pool.addSymbol(this);
                return;
            }
            throw new DescriptorValidationException((GenericDescriptor)this, "Enums must contain at least one value.");
        }

        private void setProto(DescriptorProtos.EnumDescriptorProto enumDescriptorProto) {
            EnumValueDescriptor[] arrenumValueDescriptor;
            this.proto = enumDescriptorProto;
            for (int i = 0; i < (arrenumValueDescriptor = this.values).length; ++i) {
                arrenumValueDescriptor[i].setProto(enumDescriptorProto.getValue(i));
            }
        }

        public EnumValueDescriptor findValueByName(String object) {
            DescriptorPool descriptorPool = this.file.pool;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.fullName);
            stringBuilder.append('.');
            stringBuilder.append((String)object);
            object = descriptorPool.findSymbol(stringBuilder.toString());
            if (object != null && object instanceof EnumValueDescriptor) {
                return (EnumValueDescriptor)object;
            }
            return null;
        }

        @Override
        public EnumValueDescriptor findValueByNumber(int n) {
            return (EnumValueDescriptor)this.file.pool.enumValuesByNumber.get(new DescriptorPool.DescriptorIntPair(this, n));
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        @Override
        public FileDescriptor getFile() {
            return this.file;
        }

        @Override
        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public String getName() {
            return this.proto.getName();
        }

        public DescriptorProtos.EnumOptions getOptions() {
            return this.proto.getOptions();
        }

        public List<EnumValueDescriptor> getValues() {
            return Collections.unmodifiableList(Arrays.asList(this.values));
        }

        @Override
        public DescriptorProtos.EnumDescriptorProto toProto() {
            return this.proto;
        }
    }

    public static final class EnumValueDescriptor
    implements GenericDescriptor,
    Internal.EnumLite {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private DescriptorProtos.EnumValueDescriptorProto proto;
        private final EnumDescriptor type;

        private EnumValueDescriptor(DescriptorProtos.EnumValueDescriptorProto enumValueDescriptorProto, FileDescriptor fileDescriptor, EnumDescriptor enumDescriptor, int n) throws DescriptorValidationException {
            this.index = n;
            this.proto = enumValueDescriptorProto;
            this.file = fileDescriptor;
            this.type = enumDescriptor;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(enumDescriptor.getFullName());
            stringBuilder.append('.');
            stringBuilder.append(enumValueDescriptorProto.getName());
            this.fullName = stringBuilder.toString();
            fileDescriptor.pool.addSymbol(this);
            fileDescriptor.pool.addEnumValueByNumber(this);
        }

        private void setProto(DescriptorProtos.EnumValueDescriptorProto enumValueDescriptorProto) {
            this.proto = enumValueDescriptorProto;
        }

        @Override
        public FileDescriptor getFile() {
            return this.file;
        }

        @Override
        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public String getName() {
            return this.proto.getName();
        }

        @Override
        public int getNumber() {
            return this.proto.getNumber();
        }

        public DescriptorProtos.EnumValueOptions getOptions() {
            return this.proto.getOptions();
        }

        public EnumDescriptor getType() {
            return this.type;
        }

        @Override
        public DescriptorProtos.EnumValueDescriptorProto toProto() {
            return this.proto;
        }
    }

    public static final class FieldDescriptor
    implements GenericDescriptor,
    Comparable<FieldDescriptor>,
    FieldSet.FieldDescriptorLite<FieldDescriptor> {
        private static final WireFormat.FieldType[] table = WireFormat.FieldType.values();
        private Descriptor containingType;
        private Object defaultValue;
        private EnumDescriptor enumType;
        private final Descriptor extensionScope;
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private Descriptor messageType;
        private DescriptorProtos.FieldDescriptorProto proto;
        private Type type;

        static {
            if (Type.values().length == DescriptorProtos.FieldDescriptorProto.Type.values().length) {
                return;
            }
            throw new RuntimeException("descriptor.proto has a new declared type but Desrciptors.java wasn't updated.");
        }

        private FieldDescriptor(DescriptorProtos.FieldDescriptorProto fieldDescriptorProto, FileDescriptor fileDescriptor, Descriptor descriptor, int n, boolean bl) throws DescriptorValidationException {
            block5 : {
                block9 : {
                    block8 : {
                        block6 : {
                            block7 : {
                                this.index = n;
                                this.proto = fieldDescriptorProto;
                                this.fullName = Descriptors.computeFullName(fileDescriptor, descriptor, fieldDescriptorProto.getName());
                                this.file = fileDescriptor;
                                if (fieldDescriptorProto.hasType()) {
                                    this.type = Type.valueOf(fieldDescriptorProto.getType());
                                }
                                if (this.getNumber() <= 0) break block5;
                                if (fieldDescriptorProto.getOptions().getPacked() && !this.isPackable()) {
                                    throw new DescriptorValidationException((GenericDescriptor)this, "[packed = true] can only be specified for repeated primitive fields.");
                                }
                                if (!bl) break block6;
                                if (!fieldDescriptorProto.hasExtendee()) break block7;
                                this.containingType = null;
                                this.extensionScope = descriptor != null ? descriptor : null;
                                break block8;
                            }
                            throw new DescriptorValidationException((GenericDescriptor)this, "FieldDescriptorProto.extendee not set for extension field.");
                        }
                        if (fieldDescriptorProto.hasExtendee()) break block9;
                        this.containingType = descriptor;
                        this.extensionScope = null;
                    }
                    fileDescriptor.pool.addSymbol(this);
                    return;
                }
                throw new DescriptorValidationException((GenericDescriptor)this, "FieldDescriptorProto.extendee set for non-extension field.");
            }
            throw new DescriptorValidationException((GenericDescriptor)this, "Field numbers must be positive integers.");
        }

        /*
         * Exception decompiling
         */
        private void crossLink() throws DescriptorValidationException {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [5[CASE]], but top level block is 1[TRYBLOCK]
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
            // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
            // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
            // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:682)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:765)
            // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
            // org.benf.cfr.reader.Main.doJar(Main.java:134)
            // org.benf.cfr.reader.Main.main(Main.java:189)
            throw new IllegalStateException("Decompilation failed");
        }

        private void setProto(DescriptorProtos.FieldDescriptorProto fieldDescriptorProto) {
            this.proto = fieldDescriptorProto;
        }

        @Override
        public int compareTo(FieldDescriptor fieldDescriptor) {
            if (fieldDescriptor.containingType == this.containingType) {
                return this.getNumber() - fieldDescriptor.getNumber();
            }
            throw new IllegalArgumentException("FieldDescriptors can only be compared to other FieldDescriptors for fields of the same message type.");
        }

        public Descriptor getContainingType() {
            return this.containingType;
        }

        public Object getDefaultValue() {
            if (this.getJavaType() != JavaType.MESSAGE) {
                return this.defaultValue;
            }
            throw new UnsupportedOperationException("FieldDescriptor.getDefaultValue() called on an embedded message field.");
        }

        public EnumDescriptor getEnumType() {
            if (this.getJavaType() == JavaType.ENUM) {
                return this.enumType;
            }
            throw new UnsupportedOperationException("This field is not of enum type.");
        }

        public Descriptor getExtensionScope() {
            if (this.isExtension()) {
                return this.extensionScope;
            }
            throw new UnsupportedOperationException("This field is not an extension.");
        }

        @Override
        public FileDescriptor getFile() {
            return this.file;
        }

        @Override
        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public JavaType getJavaType() {
            return this.type.getJavaType();
        }

        @Override
        public WireFormat.JavaType getLiteJavaType() {
            return this.getLiteType().getJavaType();
        }

        @Override
        public WireFormat.FieldType getLiteType() {
            return table[this.type.ordinal()];
        }

        public Descriptor getMessageType() {
            if (this.getJavaType() == JavaType.MESSAGE) {
                return this.messageType;
            }
            throw new UnsupportedOperationException("This field is not of message type.");
        }

        @Override
        public String getName() {
            return this.proto.getName();
        }

        @Override
        public int getNumber() {
            return this.proto.getNumber();
        }

        public DescriptorProtos.FieldOptions getOptions() {
            return this.proto.getOptions();
        }

        public Type getType() {
            return this.type;
        }

        public boolean hasDefaultValue() {
            return this.proto.hasDefaultValue();
        }

        @Override
        public MessageLite.Builder internalMergeFrom(MessageLite.Builder builder, MessageLite messageLite) {
            return ((Message.Builder)builder).mergeFrom((Message)messageLite);
        }

        public boolean isExtension() {
            return this.proto.hasExtendee();
        }

        public boolean isOptional() {
            if (this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL) {
                return true;
            }
            return false;
        }

        public boolean isPackable() {
            if (this.isRepeated() && this.getLiteType().isPackable()) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isPacked() {
            return this.getOptions().getPacked();
        }

        @Override
        public boolean isRepeated() {
            if (this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED) {
                return true;
            }
            return false;
        }

        public boolean isRequired() {
            if (this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REQUIRED) {
                return true;
            }
            return false;
        }

        @Override
        public DescriptorProtos.FieldDescriptorProto toProto() {
            return this.proto;
        }

        public static final class JavaType
        extends Enum<JavaType> {
            private static final /* synthetic */ JavaType[] $VALUES;
            public static final /* enum */ JavaType BOOLEAN;
            public static final /* enum */ JavaType BYTE_STRING;
            public static final /* enum */ JavaType DOUBLE;
            public static final /* enum */ JavaType ENUM;
            public static final /* enum */ JavaType FLOAT;
            public static final /* enum */ JavaType INT;
            public static final /* enum */ JavaType LONG;
            public static final /* enum */ JavaType MESSAGE;
            public static final /* enum */ JavaType STRING;
            private final Object defaultDefault;

            static {
                JavaType javaType;
                INT = new JavaType(0);
                LONG = new JavaType(0L);
                FLOAT = new JavaType(Float.valueOf(0.0f));
                DOUBLE = new JavaType(0.0);
                BOOLEAN = new JavaType(false);
                STRING = new JavaType("");
                BYTE_STRING = new JavaType(ByteString.EMPTY);
                ENUM = new JavaType(null);
                MESSAGE = javaType = new JavaType(null);
                $VALUES = new JavaType[]{INT, LONG, FLOAT, DOUBLE, BOOLEAN, STRING, BYTE_STRING, ENUM, javaType};
            }

            private JavaType(Object object) {
                this.defaultDefault = object;
            }

            static /* synthetic */ Object access$1700(JavaType javaType) {
                return javaType.defaultDefault;
            }

            public static JavaType valueOf(String string2) {
                return Enum.valueOf(JavaType.class, string2);
            }

            public static JavaType[] values() {
                return (JavaType[])$VALUES.clone();
            }
        }

        public static final class Type
        extends Enum<Type> {
            private static final /* synthetic */ Type[] $VALUES;
            public static final /* enum */ Type BOOL;
            public static final /* enum */ Type BYTES;
            public static final /* enum */ Type DOUBLE;
            public static final /* enum */ Type ENUM;
            public static final /* enum */ Type FIXED32;
            public static final /* enum */ Type FIXED64;
            public static final /* enum */ Type FLOAT;
            public static final /* enum */ Type GROUP;
            public static final /* enum */ Type INT32;
            public static final /* enum */ Type INT64;
            public static final /* enum */ Type MESSAGE;
            public static final /* enum */ Type SFIXED32;
            public static final /* enum */ Type SFIXED64;
            public static final /* enum */ Type SINT32;
            public static final /* enum */ Type SINT64;
            public static final /* enum */ Type STRING;
            public static final /* enum */ Type UINT32;
            public static final /* enum */ Type UINT64;
            private JavaType javaType;

            static {
                Type type;
                DOUBLE = new Type(JavaType.DOUBLE);
                FLOAT = new Type(JavaType.FLOAT);
                INT64 = new Type(JavaType.LONG);
                UINT64 = new Type(JavaType.LONG);
                INT32 = new Type(JavaType.INT);
                FIXED64 = new Type(JavaType.LONG);
                FIXED32 = new Type(JavaType.INT);
                BOOL = new Type(JavaType.BOOLEAN);
                STRING = new Type(JavaType.STRING);
                GROUP = new Type(JavaType.MESSAGE);
                MESSAGE = new Type(JavaType.MESSAGE);
                BYTES = new Type(JavaType.BYTE_STRING);
                UINT32 = new Type(JavaType.INT);
                ENUM = new Type(JavaType.ENUM);
                SFIXED32 = new Type(JavaType.INT);
                SFIXED64 = new Type(JavaType.LONG);
                SINT32 = new Type(JavaType.INT);
                SINT64 = type = new Type(JavaType.LONG);
                $VALUES = new Type[]{DOUBLE, FLOAT, INT64, UINT64, INT32, FIXED64, FIXED32, BOOL, STRING, GROUP, MESSAGE, BYTES, UINT32, ENUM, SFIXED32, SFIXED64, SINT32, type};
            }

            private Type(JavaType javaType) {
                this.javaType = javaType;
            }

            public static Type valueOf(DescriptorProtos.FieldDescriptorProto.Type type) {
                return Type.values()[type.getNumber() - 1];
            }

            public static Type valueOf(String string2) {
                return Enum.valueOf(Type.class, string2);
            }

            public static Type[] values() {
                return (Type[])$VALUES.clone();
            }

            public JavaType getJavaType() {
                return this.javaType;
            }

            public DescriptorProtos.FieldDescriptorProto.Type toProto() {
                return DescriptorProtos.FieldDescriptorProto.Type.valueOf(this.ordinal() + 1);
            }
        }

    }

    public static final class FileDescriptor {
        private final FileDescriptor[] dependencies;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] extensions;
        private final Descriptor[] messageTypes;
        private final DescriptorPool pool;
        private DescriptorProtos.FileDescriptorProto proto;
        private final FileDescriptor[] publicDependencies;
        private final ServiceDescriptor[] services;

        private FileDescriptor(DescriptorProtos.FileDescriptorProto fileDescriptorProto, FileDescriptor[] arrfileDescriptor, DescriptorPool descriptorPool) throws DescriptorValidationException {
            int n;
            this.pool = descriptorPool;
            this.proto = fileDescriptorProto;
            this.dependencies = (FileDescriptor[])arrfileDescriptor.clone();
            this.publicDependencies = new FileDescriptor[fileDescriptorProto.getPublicDependencyCount()];
            for (n = 0; n < fileDescriptorProto.getPublicDependencyCount(); ++n) {
                int n2 = fileDescriptorProto.getPublicDependency(n);
                if (n2 >= 0 && n2 < (arrfileDescriptor = this.dependencies).length) {
                    this.publicDependencies[n] = arrfileDescriptor[fileDescriptorProto.getPublicDependency(n)];
                    continue;
                }
                throw new DescriptorValidationException(this, "Invalid public dependency index.");
            }
            descriptorPool.addPackage(this.getPackage(), this);
            this.messageTypes = new Descriptor[fileDescriptorProto.getMessageTypeCount()];
            for (n = 0; n < fileDescriptorProto.getMessageTypeCount(); ++n) {
                this.messageTypes[n] = new Descriptor(fileDescriptorProto.getMessageType(n), this, null, n);
            }
            this.enumTypes = new EnumDescriptor[fileDescriptorProto.getEnumTypeCount()];
            for (n = 0; n < fileDescriptorProto.getEnumTypeCount(); ++n) {
                this.enumTypes[n] = new EnumDescriptor(fileDescriptorProto.getEnumType(n), this, null, n);
            }
            this.services = new ServiceDescriptor[fileDescriptorProto.getServiceCount()];
            for (n = 0; n < fileDescriptorProto.getServiceCount(); ++n) {
                this.services[n] = new ServiceDescriptor(fileDescriptorProto.getService(n), this, n);
            }
            this.extensions = new FieldDescriptor[fileDescriptorProto.getExtensionCount()];
            for (n = 0; n < fileDescriptorProto.getExtensionCount(); ++n) {
                this.extensions[n] = new FieldDescriptor(fileDescriptorProto.getExtension(n), this, null, n, true);
            }
        }

        public static FileDescriptor buildFrom(DescriptorProtos.FileDescriptorProto fileDescriptorProto, FileDescriptor[] arrfileDescriptor) throws DescriptorValidationException {
            FileDescriptor fileDescriptor = new FileDescriptor(fileDescriptorProto, arrfileDescriptor, new DescriptorPool(arrfileDescriptor));
            if (arrfileDescriptor.length == fileDescriptorProto.getDependencyCount()) {
                for (int i = 0; i < fileDescriptorProto.getDependencyCount(); ++i) {
                    if (arrfileDescriptor[i].getName().equals(fileDescriptorProto.getDependency(i))) {
                        continue;
                    }
                    throw new DescriptorValidationException(fileDescriptor, "Dependencies passed to FileDescriptor.buildFrom() don't match those listed in the FileDescriptorProto.");
                }
                fileDescriptor.crossLink();
                return fileDescriptor;
            }
            throw new DescriptorValidationException(fileDescriptor, "Dependencies passed to FileDescriptor.buildFrom() don't match those listed in the FileDescriptorProto.");
        }

        private void crossLink() throws DescriptorValidationException {
            int n;
            Descriptor[] arrdescriptor = this.messageTypes;
            int n2 = arrdescriptor.length;
            for (n = 0; n < n2; ++n) {
                arrdescriptor[n].crossLink();
            }
            arrdescriptor = this.services;
            n2 = arrdescriptor.length;
            for (n = 0; n < n2; ++n) {
                ((ServiceDescriptor)((Object)arrdescriptor[n])).crossLink();
            }
            arrdescriptor = this.extensions;
            n2 = arrdescriptor.length;
            for (n = 0; n < n2; ++n) {
                ((FieldDescriptor)((Object)arrdescriptor[n])).crossLink();
            }
        }

        public static void internalBuildGeneratedFileFrom(String[] object, FileDescriptor[] object2, InternalDescriptorAssigner object3) {
            block9 : {
                byte[] arrby = new byte[]();
                int n = object.length;
                for (int i = 0; i < n; ++i) {
                    arrby.append(object[i]);
                }
                try {
                    arrby = arrby.toString().getBytes("ISO-8859-1");
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    throw new RuntimeException("Standard encoding ISO-8859-1 not supported by JVM.", unsupportedEncodingException);
                }
                try {
                    object = DescriptorProtos.FileDescriptorProto.parseFrom(arrby);
                }
                catch (InvalidProtocolBufferException invalidProtocolBufferException) {
                    throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", invalidProtocolBufferException);
                }
                try {
                    object2 = FileDescriptor.buildFrom((DescriptorProtos.FileDescriptorProto)object, (FileDescriptor[])object2);
                    object = object3.assignDescriptors((FileDescriptor)object2);
                    if (object == null) break block9;
                }
                catch (DescriptorValidationException descriptorValidationException) {
                    object3 = new StringBuilder();
                    object3.append("Invalid embedded descriptor for \"");
                    object3.append(object.getName());
                    object3.append("\".");
                    throw new IllegalArgumentException(object3.toString(), descriptorValidationException);
                }
                try {
                    object = DescriptorProtos.FileDescriptorProto.parseFrom(arrby, (ExtensionRegistryLite)object);
                    FileDescriptor.super.setProto((DescriptorProtos.FileDescriptorProto)object);
                    return;
                }
                catch (InvalidProtocolBufferException invalidProtocolBufferException) {
                    throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", invalidProtocolBufferException);
                }
            }
        }

        private void setProto(DescriptorProtos.FileDescriptorProto fileDescriptorProto) {
            int n;
            Descriptor[] arrdescriptor;
            this.proto = fileDescriptorProto;
            for (n = 0; n < (arrdescriptor = this.messageTypes).length; ++n) {
                arrdescriptor[n].setProto(fileDescriptorProto.getMessageType(n));
            }
            for (n = 0; n < (arrdescriptor = this.enumTypes).length; ++n) {
                ((EnumDescriptor)((Object)arrdescriptor[n])).setProto(fileDescriptorProto.getEnumType(n));
            }
            for (n = 0; n < (arrdescriptor = this.services).length; ++n) {
                ((ServiceDescriptor)((Object)arrdescriptor[n])).setProto(fileDescriptorProto.getService(n));
            }
            for (n = 0; n < (arrdescriptor = this.extensions).length; ++n) {
                ((FieldDescriptor)((Object)arrdescriptor[n])).setProto(fileDescriptorProto.getExtension(n));
            }
        }

        public EnumDescriptor findEnumTypeByName(String object) {
            if (object.indexOf(46) != -1) {
                return null;
            }
            CharSequence charSequence = object;
            if (this.getPackage().length() > 0) {
                charSequence = new StringBuilder();
                charSequence.append(this.getPackage());
                charSequence.append('.');
                charSequence.append((String)object);
                charSequence = charSequence.toString();
            }
            if ((object = this.pool.findSymbol((String)charSequence)) != null && object instanceof EnumDescriptor && object.getFile() == this) {
                return (EnumDescriptor)object;
            }
            return null;
        }

        public FieldDescriptor findExtensionByName(String object) {
            if (object.indexOf(46) != -1) {
                return null;
            }
            CharSequence charSequence = object;
            if (this.getPackage().length() > 0) {
                charSequence = new StringBuilder();
                charSequence.append(this.getPackage());
                charSequence.append('.');
                charSequence.append((String)object);
                charSequence = charSequence.toString();
            }
            if ((object = this.pool.findSymbol((String)charSequence)) != null && object instanceof FieldDescriptor && object.getFile() == this) {
                return (FieldDescriptor)object;
            }
            return null;
        }

        public Descriptor findMessageTypeByName(String object) {
            if (object.indexOf(46) != -1) {
                return null;
            }
            CharSequence charSequence = object;
            if (this.getPackage().length() > 0) {
                charSequence = new StringBuilder();
                charSequence.append(this.getPackage());
                charSequence.append('.');
                charSequence.append((String)object);
                charSequence = charSequence.toString();
            }
            if ((object = this.pool.findSymbol((String)charSequence)) != null && object instanceof Descriptor && object.getFile() == this) {
                return (Descriptor)object;
            }
            return null;
        }

        public ServiceDescriptor findServiceByName(String object) {
            if (object.indexOf(46) != -1) {
                return null;
            }
            CharSequence charSequence = object;
            if (this.getPackage().length() > 0) {
                charSequence = new StringBuilder();
                charSequence.append(this.getPackage());
                charSequence.append('.');
                charSequence.append((String)object);
                charSequence = charSequence.toString();
            }
            if ((object = this.pool.findSymbol((String)charSequence)) != null && object instanceof ServiceDescriptor && object.getFile() == this) {
                return (ServiceDescriptor)object;
            }
            return null;
        }

        public List<FileDescriptor> getDependencies() {
            return Collections.unmodifiableList(Arrays.asList(this.dependencies));
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(this.extensions));
        }

        public List<Descriptor> getMessageTypes() {
            return Collections.unmodifiableList(Arrays.asList(this.messageTypes));
        }

        public String getName() {
            return this.proto.getName();
        }

        public DescriptorProtos.FileOptions getOptions() {
            return this.proto.getOptions();
        }

        public String getPackage() {
            return this.proto.getPackage();
        }

        public List<FileDescriptor> getPublicDependencies() {
            return Collections.unmodifiableList(Arrays.asList(this.publicDependencies));
        }

        public List<ServiceDescriptor> getServices() {
            return Collections.unmodifiableList(Arrays.asList(this.services));
        }

        public DescriptorProtos.FileDescriptorProto toProto() {
            return this.proto;
        }

        public static interface InternalDescriptorAssigner {
            public ExtensionRegistry assignDescriptors(FileDescriptor var1);
        }

    }

    private static interface GenericDescriptor {
        public FileDescriptor getFile();

        public String getFullName();

        public String getName();

        public Message toProto();
    }

    public static final class MethodDescriptor
    implements GenericDescriptor {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private Descriptor inputType;
        private Descriptor outputType;
        private DescriptorProtos.MethodDescriptorProto proto;
        private final ServiceDescriptor service;

        private MethodDescriptor(DescriptorProtos.MethodDescriptorProto methodDescriptorProto, FileDescriptor fileDescriptor, ServiceDescriptor serviceDescriptor, int n) throws DescriptorValidationException {
            this.index = n;
            this.proto = methodDescriptorProto;
            this.file = fileDescriptor;
            this.service = serviceDescriptor;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(serviceDescriptor.getFullName());
            stringBuilder.append('.');
            stringBuilder.append(methodDescriptorProto.getName());
            this.fullName = stringBuilder.toString();
            fileDescriptor.pool.addSymbol(this);
        }

        private void crossLink() throws DescriptorValidationException {
            Object object = this.file.pool.lookupSymbol(this.proto.getInputType(), this, DescriptorPool.SearchFilter.TYPES_ONLY);
            if (object instanceof Descriptor) {
                this.inputType = (Descriptor)object;
                object = this.file.pool.lookupSymbol(this.proto.getOutputType(), this, DescriptorPool.SearchFilter.TYPES_ONLY);
                if (object instanceof Descriptor) {
                    this.outputType = (Descriptor)object;
                    return;
                }
                object = new StringBuilder();
                object.append('\"');
                object.append(this.proto.getOutputType());
                object.append("\" is not a message type.");
                throw new DescriptorValidationException((GenericDescriptor)this, object.toString());
            }
            object = new StringBuilder();
            object.append('\"');
            object.append(this.proto.getInputType());
            object.append("\" is not a message type.");
            throw new DescriptorValidationException((GenericDescriptor)this, object.toString());
        }

        private void setProto(DescriptorProtos.MethodDescriptorProto methodDescriptorProto) {
            this.proto = methodDescriptorProto;
        }

        @Override
        public FileDescriptor getFile() {
            return this.file;
        }

        @Override
        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public Descriptor getInputType() {
            return this.inputType;
        }

        @Override
        public String getName() {
            return this.proto.getName();
        }

        public DescriptorProtos.MethodOptions getOptions() {
            return this.proto.getOptions();
        }

        public Descriptor getOutputType() {
            return this.outputType;
        }

        public ServiceDescriptor getService() {
            return this.service;
        }

        @Override
        public DescriptorProtos.MethodDescriptorProto toProto() {
            return this.proto;
        }
    }

    public static final class ServiceDescriptor
    implements GenericDescriptor {
        private final FileDescriptor file;
        private final String fullName;
        private final int index;
        private MethodDescriptor[] methods;
        private DescriptorProtos.ServiceDescriptorProto proto;

        private ServiceDescriptor(DescriptorProtos.ServiceDescriptorProto serviceDescriptorProto, FileDescriptor fileDescriptor, int n) throws DescriptorValidationException {
            this.index = n;
            this.proto = serviceDescriptorProto;
            this.fullName = Descriptors.computeFullName(fileDescriptor, null, serviceDescriptorProto.getName());
            this.file = fileDescriptor;
            this.methods = new MethodDescriptor[serviceDescriptorProto.getMethodCount()];
            for (n = 0; n < serviceDescriptorProto.getMethodCount(); ++n) {
                this.methods[n] = new MethodDescriptor(serviceDescriptorProto.getMethod(n), fileDescriptor, this, n);
            }
            fileDescriptor.pool.addSymbol(this);
        }

        private void crossLink() throws DescriptorValidationException {
            MethodDescriptor[] arrmethodDescriptor = this.methods;
            int n = arrmethodDescriptor.length;
            for (int i = 0; i < n; ++i) {
                arrmethodDescriptor[i].crossLink();
            }
        }

        private void setProto(DescriptorProtos.ServiceDescriptorProto serviceDescriptorProto) {
            MethodDescriptor[] arrmethodDescriptor;
            this.proto = serviceDescriptorProto;
            for (int i = 0; i < (arrmethodDescriptor = this.methods).length; ++i) {
                arrmethodDescriptor[i].setProto(serviceDescriptorProto.getMethod(i));
            }
        }

        public MethodDescriptor findMethodByName(String object) {
            DescriptorPool descriptorPool = this.file.pool;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.fullName);
            stringBuilder.append('.');
            stringBuilder.append((String)object);
            object = descriptorPool.findSymbol(stringBuilder.toString());
            if (object != null && object instanceof MethodDescriptor) {
                return (MethodDescriptor)object;
            }
            return null;
        }

        @Override
        public FileDescriptor getFile() {
            return this.file;
        }

        @Override
        public String getFullName() {
            return this.fullName;
        }

        public int getIndex() {
            return this.index;
        }

        public List<MethodDescriptor> getMethods() {
            return Collections.unmodifiableList(Arrays.asList(this.methods));
        }

        @Override
        public String getName() {
            return this.proto.getName();
        }

        public DescriptorProtos.ServiceOptions getOptions() {
            return this.proto.getOptions();
        }

        @Override
        public DescriptorProtos.ServiceDescriptorProto toProto() {
            return this.proto;
        }
    }

}

