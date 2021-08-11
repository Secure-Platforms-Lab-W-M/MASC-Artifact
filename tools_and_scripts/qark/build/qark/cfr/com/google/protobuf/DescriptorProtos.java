/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.LazyStringArrayList;
import com.google.protobuf.LazyStringList;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.RepeatedFieldBuilder;
import com.google.protobuf.SingleFieldBuilder;
import com.google.protobuf.UnknownFieldSet;
import com.google.protobuf.UnmodifiableLazyStringList;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class DescriptorProtos {
    private static Descriptors.FileDescriptor descriptor;
    private static Descriptors.Descriptor internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_DescriptorProto_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_DescriptorProto_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_EnumDescriptorProto_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_EnumOptions_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_EnumOptions_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_EnumValueOptions_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_FieldDescriptorProto_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_FieldOptions_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_FieldOptions_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_FileDescriptorProto_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_FileDescriptorSet_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_FileOptions_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_FileOptions_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_MessageOptions_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_MessageOptions_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_MethodDescriptorProto_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_MethodOptions_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_MethodOptions_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_ServiceOptions_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_ServiceOptions_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_SourceCodeInfo_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_google_protobuf_UninterpretedOption_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable;

    static {
        Descriptors.FileDescriptor.InternalDescriptorAssigner internalDescriptorAssigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner(){

            @Override
            public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor fileDescriptor) {
                descriptor = fileDescriptor;
                internal_static_google_protobuf_FileDescriptorSet_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(0);
                internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_FileDescriptorSet_descriptor, new String[]{"File"});
                internal_static_google_protobuf_FileDescriptorProto_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(1);
                internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_FileDescriptorProto_descriptor, new String[]{"Name", "Package", "Dependency", "PublicDependency", "WeakDependency", "MessageType", "EnumType", "Service", "Extension", "Options", "SourceCodeInfo"});
                internal_static_google_protobuf_DescriptorProto_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(2);
                internal_static_google_protobuf_DescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_DescriptorProto_descriptor, new String[]{"Name", "Field", "Extension", "NestedType", "EnumType", "ExtensionRange", "Options"});
                internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor = internal_static_google_protobuf_DescriptorProto_descriptor.getNestedTypes().get(0);
                internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor, new String[]{"Start", "End"});
                internal_static_google_protobuf_FieldDescriptorProto_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(3);
                internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_FieldDescriptorProto_descriptor, new String[]{"Name", "Number", "Label", "Type", "TypeName", "Extendee", "DefaultValue", "Options"});
                internal_static_google_protobuf_EnumDescriptorProto_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(4);
                internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_EnumDescriptorProto_descriptor, new String[]{"Name", "Value", "Options"});
                internal_static_google_protobuf_EnumValueDescriptorProto_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(5);
                internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_EnumValueDescriptorProto_descriptor, new String[]{"Name", "Number", "Options"});
                internal_static_google_protobuf_ServiceDescriptorProto_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(6);
                internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_ServiceDescriptorProto_descriptor, new String[]{"Name", "Method", "Options"});
                internal_static_google_protobuf_MethodDescriptorProto_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(7);
                internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_MethodDescriptorProto_descriptor, new String[]{"Name", "InputType", "OutputType", "Options"});
                internal_static_google_protobuf_FileOptions_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(8);
                internal_static_google_protobuf_FileOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_FileOptions_descriptor, new String[]{"JavaPackage", "JavaOuterClassname", "JavaMultipleFiles", "JavaGenerateEqualsAndHash", "OptimizeFor", "GoPackage", "CcGenericServices", "JavaGenericServices", "PyGenericServices", "UninterpretedOption"});
                internal_static_google_protobuf_MessageOptions_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(9);
                internal_static_google_protobuf_MessageOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_MessageOptions_descriptor, new String[]{"MessageSetWireFormat", "NoStandardDescriptorAccessor", "UninterpretedOption"});
                internal_static_google_protobuf_FieldOptions_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(10);
                internal_static_google_protobuf_FieldOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_FieldOptions_descriptor, new String[]{"Ctype", "Packed", "Lazy", "Deprecated", "ExperimentalMapKey", "Weak", "UninterpretedOption"});
                internal_static_google_protobuf_EnumOptions_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(11);
                internal_static_google_protobuf_EnumOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_EnumOptions_descriptor, new String[]{"AllowAlias", "UninterpretedOption"});
                internal_static_google_protobuf_EnumValueOptions_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(12);
                internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_EnumValueOptions_descriptor, new String[]{"UninterpretedOption"});
                internal_static_google_protobuf_ServiceOptions_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(13);
                internal_static_google_protobuf_ServiceOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_ServiceOptions_descriptor, new String[]{"UninterpretedOption"});
                internal_static_google_protobuf_MethodOptions_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(14);
                internal_static_google_protobuf_MethodOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_MethodOptions_descriptor, new String[]{"UninterpretedOption"});
                internal_static_google_protobuf_UninterpretedOption_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(15);
                internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_UninterpretedOption_descriptor, new String[]{"Name", "IdentifierValue", "PositiveIntValue", "NegativeIntValue", "DoubleValue", "StringValue", "AggregateValue"});
                internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor = internal_static_google_protobuf_UninterpretedOption_descriptor.getNestedTypes().get(0);
                internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor, new String[]{"NamePart", "IsExtension"});
                internal_static_google_protobuf_SourceCodeInfo_descriptor = DescriptorProtos.getDescriptor().getMessageTypes().get(16);
                internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_SourceCodeInfo_descriptor, new String[]{"Location"});
                internal_static_google_protobuf_SourceCodeInfo_Location_descriptor = internal_static_google_protobuf_SourceCodeInfo_descriptor.getNestedTypes().get(0);
                internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(internal_static_google_protobuf_SourceCodeInfo_Location_descriptor, new String[]{"Path", "Span", "LeadingComments", "TrailingComments"});
                return null;
            }
        };
        Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(new String[]{"\n google/protobuf/descriptor.proto\u0012\u000fgoogle.protobuf\"G\n\u0011FileDescriptorSet\u00122\n\u0004file\u0018\u0001 \u0003(\u000b2$.google.protobuf.FileDescriptorProto\"\u00cb\u0003\n\u0013FileDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000f\n\u0007package\u0018\u0002 \u0001(\t\u0012\u0012\n\ndependency\u0018\u0003 \u0003(\t\u0012\u0019\n\u0011public_dependency\u0018\n \u0003(\u0005\u0012\u0017\n\u000fweak_dependency\u0018\u000b \u0003(\u0005\u00126\n\fmessage_type\u0018\u0004 \u0003(\u000b2 .google.protobuf.DescriptorProto\u00127\n\tenum_type\u0018\u0005 \u0003(\u000b2$.google.protobuf.EnumDescriptorProto\u00128\n\u0007service\u0018\u0006 \u0003(\u000b2'.google.protobuf.", "ServiceDescriptorProto\u00128\n\textension\u0018\u0007 \u0003(\u000b2%.google.protobuf.FieldDescriptorProto\u0012-\n\u0007options\u0018\b \u0001(\u000b2\u001c.google.protobuf.FileOptions\u00129\n\u0010source_code_info\u0018\t \u0001(\u000b2\u001f.google.protobuf.SourceCodeInfo\"\u00a9\u0003\n\u000fDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00124\n\u0005field\u0018\u0002 \u0003(\u000b2%.google.protobuf.FieldDescriptorProto\u00128\n\textension\u0018\u0006 \u0003(\u000b2%.google.protobuf.FieldDescriptorProto\u00125\n\u000bnested_type\u0018\u0003 \u0003(\u000b2 .google.protobuf.DescriptorProto\u00127\n\tenum_type", "\u0018\u0004 \u0003(\u000b2$.google.protobuf.EnumDescriptorProto\u0012H\n\u000fextension_range\u0018\u0005 \u0003(\u000b2/.google.protobuf.DescriptorProto.ExtensionRange\u00120\n\u0007options\u0018\u0007 \u0001(\u000b2\u001f.google.protobuf.MessageOptions\u001a,\n\u000eExtensionRange\u0012\r\n\u0005start\u0018\u0001 \u0001(\u0005\u0012\u000b\n\u0003end\u0018\u0002 \u0001(\u0005\"\u0094\u0005\n\u0014FieldDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000e\n\u0006number\u0018\u0003 \u0001(\u0005\u0012:\n\u0005label\u0018\u0004 \u0001(\u000e2+.google.protobuf.FieldDescriptorProto.Label\u00128\n\u0004type\u0018\u0005 \u0001(\u000e2*.google.protobuf.FieldDescriptorProto.Type\u0012\u0011\n\ttype_name", "\u0018\u0006 \u0001(\t\u0012\u0010\n\bextendee\u0018\u0002 \u0001(\t\u0012\u0015\n\rdefault_value\u0018\u0007 \u0001(\t\u0012.\n\u0007options\u0018\b \u0001(\u000b2\u001d.google.protobuf.FieldOptions\"\u00b6\u0002\n\u0004Type\u0012\u000f\n\u000bTYPE_DOUBLE\u0010\u0001\u0012\u000e\n\nTYPE_FLOAT\u0010\u0002\u0012\u000e\n\nTYPE_INT64\u0010\u0003\u0012\u000f\n\u000bTYPE_UINT64\u0010\u0004\u0012\u000e\n\nTYPE_INT32\u0010\u0005\u0012\u0010\n\fTYPE_FIXED64\u0010\u0006\u0012\u0010\n\fTYPE_FIXED32\u0010\u0007\u0012\r\n\tTYPE_BOOL\u0010\b\u0012\u000f\n\u000bTYPE_STRING\u0010\t\u0012\u000e\n\nTYPE_GROUP\u0010\n\u0012\u0010\n\fTYPE_MESSAGE\u0010\u000b\u0012\u000e\n\nTYPE_BYTES\u0010\f\u0012\u000f\n\u000bTYPE_UINT32\u0010\r\u0012\r\n\tTYPE_ENUM\u0010\u000e\u0012\u0011\n\rTYPE_SFIXED32\u0010\u000f\u0012\u0011\n\rTYPE_SFIXED64\u0010\u0010\u0012\u000f\n\u000bTYPE_SINT32\u0010\u0011\u0012\u000f\n\u000bTYPE_", "SINT64\u0010\u0012\"C\n\u0005Label\u0012\u0012\n\u000eLABEL_OPTIONAL\u0010\u0001\u0012\u0012\n\u000eLABEL_REQUIRED\u0010\u0002\u0012\u0012\n\u000eLABEL_REPEATED\u0010\u0003\"\u008c\u0001\n\u0013EnumDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00128\n\u0005value\u0018\u0002 \u0003(\u000b2).google.protobuf.EnumValueDescriptorProto\u0012-\n\u0007options\u0018\u0003 \u0001(\u000b2\u001c.google.protobuf.EnumOptions\"l\n\u0018EnumValueDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000e\n\u0006number\u0018\u0002 \u0001(\u0005\u00122\n\u0007options\u0018\u0003 \u0001(\u000b2!.google.protobuf.EnumValueOptions\"\u0090\u0001\n\u0016ServiceDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00126\n\u0006method\u0018\u0002 \u0003(\u000b2&.google.pro", "tobuf.MethodDescriptorProto\u00120\n\u0007options\u0018\u0003 \u0001(\u000b2\u001f.google.protobuf.ServiceOptions\"\n\u0015MethodDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u0012\n\ninput_type\u0018\u0002 \u0001(\t\u0012\u0013\n\u000boutput_type\u0018\u0003 \u0001(\t\u0012/\n\u0007options\u0018\u0004 \u0001(\u000b2\u001e.google.protobuf.MethodOptions\"\u00e9\u0003\n\u000bFileOptions\u0012\u0014\n\fjava_package\u0018\u0001 \u0001(\t\u0012\u001c\n\u0014java_outer_classname\u0018\b \u0001(\t\u0012\"\n\u0013java_multiple_files\u0018\n \u0001(\b:\u0005false\u0012,\n\u001djava_generate_equals_and_hash\u0018\u0014 \u0001(\b:\u0005false\u0012F\n\foptimize_for\u0018\t \u0001(\u000e2).google.protobuf.Fil", "eOptions.OptimizeMode:\u0005SPEED\u0012\u0012\n\ngo_package\u0018\u000b \u0001(\t\u0012\"\n\u0013cc_generic_services\u0018\u0010 \u0001(\b:\u0005false\u0012$\n\u0015java_generic_services\u0018\u0011 \u0001(\b:\u0005false\u0012\"\n\u0013py_generic_services\u0018\u0012 \u0001(\b:\u0005false\u0012C\n\u0014uninterpreted_option\u0018\u00e7\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption\":\n\fOptimizeMode\u0012\t\n\u0005SPEED\u0010\u0001\u0012\r\n\tCODE_SIZE\u0010\u0002\u0012\u0010\n\fLITE_RUNTIME\u0010\u0003*\t\b\u00e8\u0007\u0010\u0002\"\u00b8\u0001\n\u000eMessageOptions\u0012&\n\u0017message_set_wire_format\u0018\u0001 \u0001(\b:\u0005false\u0012.\n\u001fno_standard_descriptor_accessor\u0018\u0002 \u0001(\b:\u0005", "false\u0012C\n\u0014uninterpreted_option\u0018\u00e7\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\b\u00e8\u0007\u0010\u0002\"\u00be\u0002\n\fFieldOptions\u0012:\n\u0005ctype\u0018\u0001 \u0001(\u000e2#.google.protobuf.FieldOptions.CType:\u0006STRING\u0012\u000e\n\u0006packed\u0018\u0002 \u0001(\b\u0012\u0013\n\u0004lazy\u0018\u0005 \u0001(\b:\u0005false\u0012\u0019\n\ndeprecated\u0018\u0003 \u0001(\b:\u0005false\u0012\u001c\n\u0014experimental_map_key\u0018\t \u0001(\t\u0012\u0013\n\u0004weak\u0018\n \u0001(\b:\u0005false\u0012C\n\u0014uninterpreted_option\u0018\u00e7\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption\"/\n\u0005CType\u0012\n\n\u0006STRING\u0010\u0000\u0012\b\n\u0004CORD\u0010\u0001\u0012\u0010\n\fSTRING_PIECE\u0010\u0002*\t\b\u00e8\u0007", "\u0010\u0002\"x\n\u000bEnumOptions\u0012\u0019\n\u000ballow_alias\u0018\u0002 \u0001(\b:\u0004true\u0012C\n\u0014uninterpreted_option\u0018\u00e7\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\b\u00e8\u0007\u0010\u0002\"b\n\u0010EnumValueOptions\u0012C\n\u0014uninterpreted_option\u0018\u00e7\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\b\u00e8\u0007\u0010\u0002\"`\n\u000eServiceOptions\u0012C\n\u0014uninterpreted_option\u0018\u00e7\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\b\u00e8\u0007\u0010\u0002\"_\n\rMethodOptions\u0012C\n\u0014uninterpreted_option\u0018\u00e7\u0007 \u0003(\u000b2$.google.protobuf.Uninter", "pretedOption*\t\b\u00e8\u0007\u0010\u0002\"\u009e\u0002\n\u0013UninterpretedOption\u0012;\n\u0004name\u0018\u0002 \u0003(\u000b2-.google.protobuf.UninterpretedOption.NamePart\u0012\u0018\n\u0010identifier_value\u0018\u0003 \u0001(\t\u0012\u001a\n\u0012positive_int_value\u0018\u0004 \u0001(\u0004\u0012\u001a\n\u0012negative_int_value\u0018\u0005 \u0001(\u0003\u0012\u0014\n\fdouble_value\u0018\u0006 \u0001(\u0001\u0012\u0014\n\fstring_value\u0018\u0007 \u0001(\f\u0012\u0017\n\u000faggregate_value\u0018\b \u0001(\t\u001a3\n\bNamePart\u0012\u0011\n\tname_part\u0018\u0001 \u0002(\t\u0012\u0014\n\fis_extension\u0018\u0002 \u0002(\b\"\u00b1\u0001\n\u000eSourceCodeInfo\u0012:\n\blocation\u0018\u0001 \u0003(\u000b2(.google.protobuf.SourceCodeInfo.Location\u001ac\n\bLocat", "ion\u0012\u0010\n\u0004path\u0018\u0001 \u0003(\u0005B\u0002\u0010\u0001\u0012\u0010\n\u0004span\u0018\u0002 \u0003(\u0005B\u0002\u0010\u0001\u0012\u0018\n\u0010leading_comments\u0018\u0003 \u0001(\t\u0012\u0019\n\u0011trailing_comments\u0018\u0004 \u0001(\tB)\n\u0013com.google.protobufB\u0010DescriptorProtosH\u0001"}, new Descriptors.FileDescriptor[0], internalDescriptorAssigner);
    }

    private DescriptorProtos() {
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    public static void registerAllExtensions(ExtensionRegistry extensionRegistry) {
    }

    public static final class DescriptorProto
    extends GeneratedMessage
    implements DescriptorProtoOrBuilder {
        public static final int ENUM_TYPE_FIELD_NUMBER = 4;
        public static final int EXTENSION_FIELD_NUMBER = 6;
        public static final int EXTENSION_RANGE_FIELD_NUMBER = 5;
        public static final int FIELD_FIELD_NUMBER = 2;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int NESTED_TYPE_FIELD_NUMBER = 3;
        public static final int OPTIONS_FIELD_NUMBER = 7;
        public static Parser<DescriptorProto> PARSER;
        private static final DescriptorProto defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private List<EnumDescriptorProto> enumType_;
        private List<ExtensionRange> extensionRange_;
        private List<FieldDescriptorProto> extension_;
        private List<FieldDescriptorProto> field_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private Object name_;
        private List<DescriptorProto> nestedType_;
        private MessageOptions options_;
        private final UnknownFieldSet unknownFields;

        static {
            DescriptorProto descriptorProto;
            PARSER = new AbstractParser<DescriptorProto>(){

                @Override
                public DescriptorProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new DescriptorProto(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = descriptorProto = new DescriptorProto(true);
            descriptorProto.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private DescriptorProto(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = 0;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = false;
            while (!var8_8) {
                block41 : {
                    block37 : {
                        block38 : {
                            block39 : {
                                block40 : {
                                    block42 : {
                                        var4_9 = var3_6;
                                        var5_10 = var3_6;
                                        var6_11 = var3_6;
                                        var9_13 = var1_1.readTag();
                                        if (var9_13 == 0) ** GOTO lbl148
                                        if (var9_13 == 10) ** GOTO lbl138
                                        if (var9_13 == 18) break block37;
                                        if (var9_13 == 26) break block38;
                                        if (var9_13 == 34) break block39;
                                        if (var9_13 == 42) break block40;
                                        if (var9_13 == 50) break block42;
                                        if (var9_13 != 58) {
                                            var7_12 = var3_6;
                                            var4_9 = var3_6;
                                            var5_10 = var3_6;
                                            var6_11 = var3_6;
                                            if (!this.parseUnknownField(var1_1, var11_7, var2_5, var9_13)) {
                                                var8_8 = true;
                                                var7_12 = var3_6;
                                            }
                                        } else {
                                            var10_14 = null;
                                            var4_9 = var3_6;
                                            var5_10 = var3_6;
                                            var6_11 = var3_6;
                                            if ((this.bitField0_ & 2) == 2) {
                                                var4_9 = var3_6;
                                                var5_10 = var3_6;
                                                var6_11 = var3_6;
                                                var10_14 = this.options_.toBuilder();
                                            }
                                            var4_9 = var3_6;
                                            var5_10 = var3_6;
                                            var6_11 = var3_6;
                                            var12_15 = var1_1.readMessage(MessageOptions.PARSER, var2_5);
                                            var4_9 = var3_6;
                                            var5_10 = var3_6;
                                            var6_11 = var3_6;
                                            this.options_ = var12_15;
                                            if (var10_14 != null) {
                                                var4_9 = var3_6;
                                                var5_10 = var3_6;
                                                var6_11 = var3_6;
                                                var10_14.mergeFrom(var12_15);
                                                var4_9 = var3_6;
                                                var5_10 = var3_6;
                                                var6_11 = var3_6;
                                                this.options_ = var10_14.buildPartial();
                                            }
                                            var4_9 = var3_6;
                                            var5_10 = var3_6;
                                            var6_11 = var3_6;
                                            this.bitField0_ |= 2;
                                            var7_12 = var3_6;
                                        }
                                        ** GOTO lbl150
                                    }
                                    var7_12 = var3_6;
                                    if ((var3_6 & 4) != 4) {
                                        var4_9 = var3_6;
                                        var5_10 = var3_6;
                                        var6_11 = var3_6;
                                        this.extension_ = new ArrayList<FieldDescriptorProto>();
                                        var7_12 = var3_6 | 4;
                                    }
                                    var4_9 = var7_12;
                                    var5_10 = var7_12;
                                    var6_11 = var7_12;
                                    this.extension_.add(var1_1.readMessage(FieldDescriptorProto.PARSER, var2_5));
                                    ** GOTO lbl150
                                }
                                var7_12 = var3_6;
                                if ((var3_6 & 32) != 32) {
                                    var4_9 = var3_6;
                                    var5_10 = var3_6;
                                    var6_11 = var3_6;
                                    this.extensionRange_ = new ArrayList<ExtensionRange>();
                                    var7_12 = var3_6 | 32;
                                }
                                var4_9 = var7_12;
                                var5_10 = var7_12;
                                var6_11 = var7_12;
                                this.extensionRange_.add(var1_1.readMessage(ExtensionRange.PARSER, var2_5));
                                ** GOTO lbl150
                            }
                            var7_12 = var3_6;
                            if ((var3_6 & 16) != 16) {
                                var4_9 = var3_6;
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                this.enumType_ = new ArrayList<EnumDescriptorProto>();
                                var7_12 = var3_6 | 16;
                            }
                            var4_9 = var7_12;
                            var5_10 = var7_12;
                            var6_11 = var7_12;
                            this.enumType_.add(var1_1.readMessage(EnumDescriptorProto.PARSER, var2_5));
                            ** GOTO lbl150
                        }
                        var7_12 = var3_6;
                        if ((var3_6 & 8) != 8) {
                            var4_9 = var3_6;
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            this.nestedType_ = new ArrayList<DescriptorProto>();
                            var7_12 = var3_6 | 8;
                        }
                        var4_9 = var7_12;
                        var5_10 = var7_12;
                        var6_11 = var7_12;
                        this.nestedType_.add(var1_1.readMessage(DescriptorProto.PARSER, var2_5));
                        ** GOTO lbl150
                    }
                    var7_12 = var3_6;
                    if ((var3_6 & 2) != 2) {
                        var4_9 = var3_6;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        this.field_ = new ArrayList<FieldDescriptorProto>();
                        var7_12 = var3_6 | 2;
                    }
                    var4_9 = var7_12;
                    var5_10 = var7_12;
                    var6_11 = var7_12;
                    try {
                        this.field_.add(var1_1.readMessage(FieldDescriptorProto.PARSER, var2_5));
lbl138: // 1 sources:
                        var4_9 = var3_6;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        this.bitField0_ |= 1;
                        var4_9 = var3_6;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        this.name_ = var1_1.readBytes();
                        var7_12 = var3_6;
lbl148: // 1 sources:
                        var8_8 = true;
                        var7_12 = var3_6;
lbl150: // 9 sources:
                        var3_6 = var7_12;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block41;
                    }
                    catch (IOException var1_3) {
                        var4_9 = var5_10;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var4_9 = var6_11;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if ((var4_9 & 2) == 2) {
                    this.field_ = Collections.unmodifiableList(this.field_);
                }
                if ((var4_9 & 8) == 8) {
                    this.nestedType_ = Collections.unmodifiableList(this.nestedType_);
                }
                if ((var4_9 & 16) == 16) {
                    this.enumType_ = Collections.unmodifiableList(this.enumType_);
                }
                if ((var4_9 & 32) == 32) {
                    this.extensionRange_ = Collections.unmodifiableList(this.extensionRange_);
                }
                if ((var4_9 & 4) == 4) {
                    this.extension_ = Collections.unmodifiableList(this.extension_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if ((var3_6 & 2) == 2) {
                this.field_ = Collections.unmodifiableList(this.field_);
            }
            if ((var3_6 & 8) == 8) {
                this.nestedType_ = Collections.unmodifiableList(this.nestedType_);
            }
            if ((var3_6 & 16) == 16) {
                this.enumType_ = Collections.unmodifiableList(this.enumType_);
            }
            if ((var3_6 & 32) == 32) {
                this.extensionRange_ = Collections.unmodifiableList(this.extensionRange_);
            }
            if ((var3_6 & 4) == 4) {
                this.extension_ = Collections.unmodifiableList(this.extension_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private DescriptorProto(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private DescriptorProto(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static DescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_DescriptorProto_descriptor;
        }

        private void initFields() {
            this.name_ = "";
            this.field_ = Collections.emptyList();
            this.extension_ = Collections.emptyList();
            this.nestedType_ = Collections.emptyList();
            this.enumType_ = Collections.emptyList();
            this.extensionRange_ = Collections.emptyList();
            this.options_ = MessageOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(DescriptorProto descriptorProto) {
            return DescriptorProto.newBuilder().mergeFrom(descriptorProto);
        }

        public static DescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static DescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static DescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static DescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static DescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static DescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static DescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static DescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static DescriptorProto parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static DescriptorProto parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public DescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public EnumDescriptorProto getEnumType(int n) {
            return this.enumType_.get(n);
        }

        @Override
        public int getEnumTypeCount() {
            return this.enumType_.size();
        }

        @Override
        public List<EnumDescriptorProto> getEnumTypeList() {
            return this.enumType_;
        }

        @Override
        public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int n) {
            return this.enumType_.get(n);
        }

        @Override
        public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList() {
            return this.enumType_;
        }

        @Override
        public FieldDescriptorProto getExtension(int n) {
            return this.extension_.get(n);
        }

        @Override
        public int getExtensionCount() {
            return this.extension_.size();
        }

        @Override
        public List<FieldDescriptorProto> getExtensionList() {
            return this.extension_;
        }

        @Override
        public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int n) {
            return this.extension_.get(n);
        }

        @Override
        public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList() {
            return this.extension_;
        }

        @Override
        public ExtensionRange getExtensionRange(int n) {
            return this.extensionRange_.get(n);
        }

        @Override
        public int getExtensionRangeCount() {
            return this.extensionRange_.size();
        }

        @Override
        public List<ExtensionRange> getExtensionRangeList() {
            return this.extensionRange_;
        }

        @Override
        public ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int n) {
            return this.extensionRange_.get(n);
        }

        @Override
        public List<? extends ExtensionRangeOrBuilder> getExtensionRangeOrBuilderList() {
            return this.extensionRange_;
        }

        @Override
        public FieldDescriptorProto getField(int n) {
            return this.field_.get(n);
        }

        @Override
        public int getFieldCount() {
            return this.field_.size();
        }

        @Override
        public List<FieldDescriptorProto> getFieldList() {
            return this.field_;
        }

        @Override
        public FieldDescriptorProtoOrBuilder getFieldOrBuilder(int n) {
            return this.field_.get(n);
        }

        @Override
        public List<? extends FieldDescriptorProtoOrBuilder> getFieldOrBuilderList() {
            return this.field_;
        }

        @Override
        public String getName() {
            Object object = this.name_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.name_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getNameBytes() {
            Object object = this.name_;
            if (object instanceof String) {
                this.name_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public DescriptorProto getNestedType(int n) {
            return this.nestedType_.get(n);
        }

        @Override
        public int getNestedTypeCount() {
            return this.nestedType_.size();
        }

        @Override
        public List<DescriptorProto> getNestedTypeList() {
            return this.nestedType_;
        }

        @Override
        public DescriptorProtoOrBuilder getNestedTypeOrBuilder(int n) {
            return this.nestedType_.get(n);
        }

        @Override
        public List<? extends DescriptorProtoOrBuilder> getNestedTypeOrBuilderList() {
            return this.nestedType_;
        }

        @Override
        public MessageOptions getOptions() {
            return this.options_;
        }

        @Override
        public MessageOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public Parser<DescriptorProto> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n;
            int n2 = this.memoizedSerializedSize;
            if (n2 != -1) {
                return n2;
            }
            n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }
            for (n = 0; n < this.field_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(2, this.field_.get(n));
            }
            for (n = 0; n < this.nestedType_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(3, this.nestedType_.get(n));
            }
            for (n = 0; n < this.enumType_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(4, this.enumType_.get(n));
            }
            for (n = 0; n < this.extensionRange_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(5, this.extensionRange_.get(n));
            }
            for (n = 0; n < this.extension_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(6, this.extension_.get(n));
            }
            n = n2;
            if ((this.bitField0_ & 2) == 2) {
                n = n2 + CodedOutputStream.computeMessageSize(7, this.options_);
            }
            this.memoizedSerializedSize = n2 = n + this.getUnknownFields().getSerializedSize();
            return n2;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasName() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOptions() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_DescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProto.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            if (n != -1) {
                if (n == 1) {
                    return true;
                }
                return false;
            }
            for (n = 0; n < this.getFieldCount(); ++n) {
                if (this.getField(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            for (n = 0; n < this.getExtensionCount(); ++n) {
                if (this.getExtension(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            for (n = 0; n < this.getNestedTypeCount(); ++n) {
                if (this.getNestedType(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            for (n = 0; n < this.getEnumTypeCount(); ++n) {
                if (this.getEnumType(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasOptions() && !this.getOptions().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return DescriptorProto.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return DescriptorProto.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            int n;
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, this.getNameBytes());
            }
            for (n = 0; n < this.field_.size(); ++n) {
                codedOutputStream.writeMessage(2, this.field_.get(n));
            }
            for (n = 0; n < this.nestedType_.size(); ++n) {
                codedOutputStream.writeMessage(3, this.nestedType_.get(n));
            }
            for (n = 0; n < this.enumType_.size(); ++n) {
                codedOutputStream.writeMessage(4, this.enumType_.get(n));
            }
            for (n = 0; n < this.extensionRange_.size(); ++n) {
                codedOutputStream.writeMessage(5, this.extensionRange_.get(n));
            }
            for (n = 0; n < this.extension_.size(); ++n) {
                codedOutputStream.writeMessage(6, this.extension_.get(n));
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeMessage(7, this.options_);
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements DescriptorProtoOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> enumTypeBuilder_;
            private List<EnumDescriptorProto> enumType_ = Collections.emptyList();
            private RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> extensionBuilder_;
            private RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> extensionRangeBuilder_;
            private List<ExtensionRange> extensionRange_ = Collections.emptyList();
            private List<FieldDescriptorProto> extension_ = Collections.emptyList();
            private RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> fieldBuilder_;
            private List<FieldDescriptorProto> field_ = Collections.emptyList();
            private Object name_ = "";
            private RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> nestedTypeBuilder_;
            private List<DescriptorProto> nestedType_ = Collections.emptyList();
            private SingleFieldBuilder<MessageOptions, MessageOptions.Builder, MessageOptionsOrBuilder> optionsBuilder_;
            private MessageOptions options_ = MessageOptions.getDefaultInstance();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureEnumTypeIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.enumType_ = new ArrayList<EnumDescriptorProto>(this.enumType_);
                    this.bitField0_ |= 16;
                }
            }

            private void ensureExtensionIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.extension_ = new ArrayList<FieldDescriptorProto>(this.extension_);
                    this.bitField0_ |= 4;
                }
            }

            private void ensureExtensionRangeIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.extensionRange_ = new ArrayList<ExtensionRange>(this.extensionRange_);
                    this.bitField0_ |= 32;
                }
            }

            private void ensureFieldIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.field_ = new ArrayList<FieldDescriptorProto>(this.field_);
                    this.bitField0_ |= 2;
                }
            }

            private void ensureNestedTypeIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.nestedType_ = new ArrayList<DescriptorProto>(this.nestedType_);
                    this.bitField0_ |= 8;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_DescriptorProto_descriptor;
            }

            private RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> getEnumTypeFieldBuilder() {
                if (this.enumTypeBuilder_ == null) {
                    List<EnumDescriptorProto> list = this.enumType_;
                    boolean bl = (this.bitField0_ & 16) == 16;
                    this.enumTypeBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.enumType_ = null;
                }
                return this.enumTypeBuilder_;
            }

            private RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> getExtensionFieldBuilder() {
                if (this.extensionBuilder_ == null) {
                    List<FieldDescriptorProto> list = this.extension_;
                    boolean bl = (this.bitField0_ & 4) == 4;
                    this.extensionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.extension_ = null;
                }
                return this.extensionBuilder_;
            }

            private RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> getExtensionRangeFieldBuilder() {
                if (this.extensionRangeBuilder_ == null) {
                    List<ExtensionRange> list = this.extensionRange_;
                    boolean bl = (this.bitField0_ & 32) == 32;
                    this.extensionRangeBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.extensionRange_ = null;
                }
                return this.extensionRangeBuilder_;
            }

            private RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> getFieldFieldBuilder() {
                if (this.fieldBuilder_ == null) {
                    List<FieldDescriptorProto> list = this.field_;
                    boolean bl = (this.bitField0_ & 2) == 2;
                    this.fieldBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.field_ = null;
                }
                return this.fieldBuilder_;
            }

            private RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> getNestedTypeFieldBuilder() {
                if (this.nestedTypeBuilder_ == null) {
                    List<DescriptorProto> list = this.nestedType_;
                    boolean bl = (this.bitField0_ & 8) == 8;
                    this.nestedTypeBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.nestedType_ = null;
                }
                return this.nestedTypeBuilder_;
            }

            private SingleFieldBuilder<MessageOptions, MessageOptions.Builder, MessageOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getFieldFieldBuilder();
                    this.getExtensionFieldBuilder();
                    this.getNestedTypeFieldBuilder();
                    this.getEnumTypeFieldBuilder();
                    this.getExtensionRangeFieldBuilder();
                    this.getOptionsFieldBuilder();
                }
            }

            public Builder addAllEnumType(Iterable<? extends EnumDescriptorProto> iterable) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.enumType_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addAllExtension(Iterable<? extends FieldDescriptorProto> iterable) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.extension_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addAllExtensionRange(Iterable<? extends ExtensionRange> iterable) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionRangeIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.extensionRange_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addAllField(Iterable<? extends FieldDescriptorProto> iterable) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFieldIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.field_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addAllNestedType(Iterable<? extends DescriptorProto> iterable) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNestedTypeIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.nestedType_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addEnumType(int n, EnumDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    this.enumType_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addEnumType(int n, EnumDescriptorProto enumDescriptorProto) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumDescriptorProto != null) {
                        this.ensureEnumTypeIsMutable();
                        this.enumType_.add(n, enumDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, enumDescriptorProto);
                return this;
            }

            public Builder addEnumType(EnumDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    this.enumType_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addEnumType(EnumDescriptorProto enumDescriptorProto) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumDescriptorProto != null) {
                        this.ensureEnumTypeIsMutable();
                        this.enumType_.add(enumDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(enumDescriptorProto);
                return this;
            }

            public EnumDescriptorProto.Builder addEnumTypeBuilder() {
                return this.getEnumTypeFieldBuilder().addBuilder(EnumDescriptorProto.getDefaultInstance());
            }

            public EnumDescriptorProto.Builder addEnumTypeBuilder(int n) {
                return this.getEnumTypeFieldBuilder().addBuilder(n, EnumDescriptorProto.getDefaultInstance());
            }

            public Builder addExtension(int n, FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    this.extension_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addExtension(int n, FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureExtensionIsMutable();
                        this.extension_.add(n, fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, fieldDescriptorProto);
                return this;
            }

            public Builder addExtension(FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    this.extension_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addExtension(FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureExtensionIsMutable();
                        this.extension_.add(fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(fieldDescriptorProto);
                return this;
            }

            public FieldDescriptorProto.Builder addExtensionBuilder() {
                return this.getExtensionFieldBuilder().addBuilder(FieldDescriptorProto.getDefaultInstance());
            }

            public FieldDescriptorProto.Builder addExtensionBuilder(int n) {
                return this.getExtensionFieldBuilder().addBuilder(n, FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addExtensionRange(int n, ExtensionRange.Builder builder) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionRangeIsMutable();
                    this.extensionRange_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addExtensionRange(int n, ExtensionRange extensionRange) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (extensionRange != null) {
                        this.ensureExtensionRangeIsMutable();
                        this.extensionRange_.add(n, extensionRange);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, extensionRange);
                return this;
            }

            public Builder addExtensionRange(ExtensionRange.Builder builder) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionRangeIsMutable();
                    this.extensionRange_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addExtensionRange(ExtensionRange extensionRange) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (extensionRange != null) {
                        this.ensureExtensionRangeIsMutable();
                        this.extensionRange_.add(extensionRange);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(extensionRange);
                return this;
            }

            public ExtensionRange.Builder addExtensionRangeBuilder() {
                return this.getExtensionRangeFieldBuilder().addBuilder(ExtensionRange.getDefaultInstance());
            }

            public ExtensionRange.Builder addExtensionRangeBuilder(int n) {
                return this.getExtensionRangeFieldBuilder().addBuilder(n, ExtensionRange.getDefaultInstance());
            }

            public Builder addField(int n, FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFieldIsMutable();
                    this.field_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addField(int n, FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureFieldIsMutable();
                        this.field_.add(n, fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, fieldDescriptorProto);
                return this;
            }

            public Builder addField(FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFieldIsMutable();
                    this.field_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addField(FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureFieldIsMutable();
                        this.field_.add(fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(fieldDescriptorProto);
                return this;
            }

            public FieldDescriptorProto.Builder addFieldBuilder() {
                return this.getFieldFieldBuilder().addBuilder(FieldDescriptorProto.getDefaultInstance());
            }

            public FieldDescriptorProto.Builder addFieldBuilder(int n) {
                return this.getFieldFieldBuilder().addBuilder(n, FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addNestedType(int n, Builder builder) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNestedTypeIsMutable();
                    this.nestedType_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addNestedType(int n, DescriptorProto descriptorProto) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (descriptorProto != null) {
                        this.ensureNestedTypeIsMutable();
                        this.nestedType_.add(n, descriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, descriptorProto);
                return this;
            }

            public Builder addNestedType(Builder builder) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNestedTypeIsMutable();
                    this.nestedType_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addNestedType(DescriptorProto descriptorProto) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (descriptorProto != null) {
                        this.ensureNestedTypeIsMutable();
                        this.nestedType_.add(descriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(descriptorProto);
                return this;
            }

            public Builder addNestedTypeBuilder() {
                return this.getNestedTypeFieldBuilder().addBuilder(DescriptorProto.getDefaultInstance());
            }

            public Builder addNestedTypeBuilder(int n) {
                return this.getNestedTypeFieldBuilder().addBuilder(n, DescriptorProto.getDefaultInstance());
            }

            @Override
            public DescriptorProto build() {
                DescriptorProto descriptorProto = this.buildPartial();
                if (descriptorProto.isInitialized()) {
                    return descriptorProto;
                }
                throw Builder.newUninitializedMessageException(descriptorProto);
            }

            @Override
            public DescriptorProto buildPartial() {
                DescriptorProto descriptorProto = new DescriptorProto(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                descriptorProto.name_ = this.name_;
                GeneratedMessage.BuilderParent builderParent = this.fieldBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 2) == 2) {
                        this.field_ = Collections.unmodifiableList(this.field_);
                        this.bitField0_ &= -3;
                    }
                    descriptorProto.field_ = this.field_;
                } else {
                    descriptorProto.field_ = builderParent.build();
                }
                builderParent = this.extensionBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 4) == 4) {
                        this.extension_ = Collections.unmodifiableList(this.extension_);
                        this.bitField0_ &= -5;
                    }
                    descriptorProto.extension_ = this.extension_;
                } else {
                    descriptorProto.extension_ = builderParent.build();
                }
                builderParent = this.nestedTypeBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 8) == 8) {
                        this.nestedType_ = Collections.unmodifiableList(this.nestedType_);
                        this.bitField0_ &= -9;
                    }
                    descriptorProto.nestedType_ = this.nestedType_;
                } else {
                    descriptorProto.nestedType_ = builderParent.build();
                }
                builderParent = this.enumTypeBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 16) == 16) {
                        this.enumType_ = Collections.unmodifiableList(this.enumType_);
                        this.bitField0_ &= -17;
                    }
                    descriptorProto.enumType_ = this.enumType_;
                } else {
                    descriptorProto.enumType_ = builderParent.build();
                }
                builderParent = this.extensionRangeBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 32) == 32) {
                        this.extensionRange_ = Collections.unmodifiableList(this.extensionRange_);
                        this.bitField0_ &= -33;
                    }
                    descriptorProto.extensionRange_ = this.extensionRange_;
                } else {
                    descriptorProto.extensionRange_ = builderParent.build();
                }
                int n3 = n2;
                if ((n & 64) == 64) {
                    n3 = n2 | 2;
                }
                if ((builderParent = this.optionsBuilder_) == null) {
                    descriptorProto.options_ = this.options_;
                } else {
                    descriptorProto.options_ = (MessageOptions)((Object)builderParent.build());
                }
                descriptorProto.bitField0_ = n3;
                this.onBuilt();
                return descriptorProto;
            }

            @Override
            public Builder clear() {
                super.clear();
                this.name_ = "";
                this.bitField0_ &= -2;
                GeneratedMessage.BuilderParent builderParent = this.fieldBuilder_;
                if (builderParent == null) {
                    this.field_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    builderParent.clear();
                }
                builderParent = this.extensionBuilder_;
                if (builderParent == null) {
                    this.extension_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                } else {
                    builderParent.clear();
                }
                builderParent = this.nestedTypeBuilder_;
                if (builderParent == null) {
                    this.nestedType_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                } else {
                    builderParent.clear();
                }
                builderParent = this.enumTypeBuilder_;
                if (builderParent == null) {
                    this.enumType_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                } else {
                    builderParent.clear();
                }
                builderParent = this.extensionRangeBuilder_;
                if (builderParent == null) {
                    this.extensionRange_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                } else {
                    builderParent.clear();
                }
                builderParent = this.optionsBuilder_;
                if (builderParent == null) {
                    this.options_ = MessageOptions.getDefaultInstance();
                } else {
                    builderParent.clear();
                }
                this.bitField0_ &= -65;
                return this;
            }

            public Builder clearEnumType() {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.enumType_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearExtension() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.extension_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearExtensionRange() {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.extensionRange_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearField() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.field_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = DescriptorProto.getDefaultInstance().getName();
                this.onChanged();
                return this;
            }

            public Builder clearNestedType() {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.nestedType_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearOptions() {
                SingleFieldBuilder<MessageOptions, MessageOptions.Builder, MessageOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = MessageOptions.getDefaultInstance();
                    this.onChanged();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -65;
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public DescriptorProto getDefaultInstanceForType() {
                return DescriptorProto.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_DescriptorProto_descriptor;
            }

            @Override
            public EnumDescriptorProto getEnumType(int n) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.enumType_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public EnumDescriptorProto.Builder getEnumTypeBuilder(int n) {
                return this.getEnumTypeFieldBuilder().getBuilder(n);
            }

            public List<EnumDescriptorProto.Builder> getEnumTypeBuilderList() {
                return this.getEnumTypeFieldBuilder().getBuilderList();
            }

            @Override
            public int getEnumTypeCount() {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.enumType_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<EnumDescriptorProto> getEnumTypeList() {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.enumType_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int n) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.enumType_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList() {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.enumType_);
            }

            @Override
            public FieldDescriptorProto getExtension(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extension_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public FieldDescriptorProto.Builder getExtensionBuilder(int n) {
                return this.getExtensionFieldBuilder().getBuilder(n);
            }

            public List<FieldDescriptorProto.Builder> getExtensionBuilderList() {
                return this.getExtensionFieldBuilder().getBuilderList();
            }

            @Override
            public int getExtensionCount() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extension_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<FieldDescriptorProto> getExtensionList() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.extension_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extension_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.extension_);
            }

            @Override
            public ExtensionRange getExtensionRange(int n) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extensionRange_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public ExtensionRange.Builder getExtensionRangeBuilder(int n) {
                return this.getExtensionRangeFieldBuilder().getBuilder(n);
            }

            public List<ExtensionRange.Builder> getExtensionRangeBuilderList() {
                return this.getExtensionRangeFieldBuilder().getBuilderList();
            }

            @Override
            public int getExtensionRangeCount() {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extensionRange_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<ExtensionRange> getExtensionRangeList() {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.extensionRange_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int n) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extensionRange_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends ExtensionRangeOrBuilder> getExtensionRangeOrBuilderList() {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.extensionRange_);
            }

            @Override
            public FieldDescriptorProto getField(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.field_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public FieldDescriptorProto.Builder getFieldBuilder(int n) {
                return this.getFieldFieldBuilder().getBuilder(n);
            }

            public List<FieldDescriptorProto.Builder> getFieldBuilderList() {
                return this.getFieldFieldBuilder().getBuilderList();
            }

            @Override
            public int getFieldCount() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.field_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<FieldDescriptorProto> getFieldList() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.field_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public FieldDescriptorProtoOrBuilder getFieldOrBuilder(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.field_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends FieldDescriptorProtoOrBuilder> getFieldOrBuilderList() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.field_);
            }

            @Override
            public String getName() {
                Object object = this.name_;
                if (!(object instanceof String)) {
                    this.name_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getNameBytes() {
                Object object = this.name_;
                if (object instanceof String) {
                    this.name_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public DescriptorProto getNestedType(int n) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.nestedType_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public Builder getNestedTypeBuilder(int n) {
                return this.getNestedTypeFieldBuilder().getBuilder(n);
            }

            public List<Builder> getNestedTypeBuilderList() {
                return this.getNestedTypeFieldBuilder().getBuilderList();
            }

            @Override
            public int getNestedTypeCount() {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.nestedType_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<DescriptorProto> getNestedTypeList() {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.nestedType_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public DescriptorProtoOrBuilder getNestedTypeOrBuilder(int n) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.nestedType_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends DescriptorProtoOrBuilder> getNestedTypeOrBuilderList() {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.nestedType_);
            }

            @Override
            public MessageOptions getOptions() {
                SingleFieldBuilder<MessageOptions, MessageOptions.Builder, MessageOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    return this.options_;
                }
                return singleFieldBuilder.getMessage();
            }

            public MessageOptions.Builder getOptionsBuilder() {
                this.bitField0_ |= 64;
                this.onChanged();
                return this.getOptionsFieldBuilder().getBuilder();
            }

            @Override
            public MessageOptionsOrBuilder getOptionsOrBuilder() {
                SingleFieldBuilder<MessageOptions, MessageOptions.Builder, MessageOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder != null) {
                    return singleFieldBuilder.getMessageOrBuilder();
                }
                return this.options_;
            }

            @Override
            public boolean hasName() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOptions() {
                if ((this.bitField0_ & 64) == 64) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_DescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProto.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                int n;
                for (n = 0; n < this.getFieldCount(); ++n) {
                    if (this.getField(n).isInitialized()) continue;
                    return false;
                }
                for (n = 0; n < this.getExtensionCount(); ++n) {
                    if (this.getExtension(n).isInitialized()) continue;
                    return false;
                }
                for (n = 0; n < this.getNestedTypeCount(); ++n) {
                    if (this.getNestedType(n).isInitialized()) continue;
                    return false;
                }
                for (n = 0; n < this.getEnumTypeCount(); ++n) {
                    if (this.getEnumType(n).isInitialized()) continue;
                    return false;
                }
                if (this.hasOptions() && !this.getOptions().isInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = DescriptorProto.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((DescriptorProto)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (DescriptorProto)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((DescriptorProto)object3);
                throw throwable222;
            }

            public Builder mergeFrom(DescriptorProto descriptorProto) {
                if (descriptorProto == DescriptorProto.getDefaultInstance()) {
                    return this;
                }
                if (descriptorProto.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = descriptorProto.name_;
                    this.onChanged();
                }
                RepeatedFieldBuilder repeatedFieldBuilder = this.fieldBuilder_;
                Object var3_3 = null;
                if (repeatedFieldBuilder == null) {
                    if (!descriptorProto.field_.isEmpty()) {
                        if (this.field_.isEmpty()) {
                            this.field_ = descriptorProto.field_;
                            this.bitField0_ &= -3;
                        } else {
                            this.ensureFieldIsMutable();
                            this.field_.addAll(descriptorProto.field_);
                        }
                        this.onChanged();
                    }
                } else if (!descriptorProto.field_.isEmpty()) {
                    if (this.fieldBuilder_.isEmpty()) {
                        this.fieldBuilder_.dispose();
                        this.fieldBuilder_ = null;
                        this.field_ = descriptorProto.field_;
                        this.bitField0_ &= -3;
                        repeatedFieldBuilder = GeneratedMessage.alwaysUseFieldBuilders ? this.getFieldFieldBuilder() : null;
                        this.fieldBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.fieldBuilder_.addAllMessages(descriptorProto.field_);
                    }
                }
                if (this.extensionBuilder_ == null) {
                    if (!descriptorProto.extension_.isEmpty()) {
                        if (this.extension_.isEmpty()) {
                            this.extension_ = descriptorProto.extension_;
                            this.bitField0_ &= -5;
                        } else {
                            this.ensureExtensionIsMutable();
                            this.extension_.addAll(descriptorProto.extension_);
                        }
                        this.onChanged();
                    }
                } else if (!descriptorProto.extension_.isEmpty()) {
                    if (this.extensionBuilder_.isEmpty()) {
                        this.extensionBuilder_.dispose();
                        this.extensionBuilder_ = null;
                        this.extension_ = descriptorProto.extension_;
                        this.bitField0_ &= -5;
                        repeatedFieldBuilder = GeneratedMessage.alwaysUseFieldBuilders ? this.getExtensionFieldBuilder() : null;
                        this.extensionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.extensionBuilder_.addAllMessages(descriptorProto.extension_);
                    }
                }
                if (this.nestedTypeBuilder_ == null) {
                    if (!descriptorProto.nestedType_.isEmpty()) {
                        if (this.nestedType_.isEmpty()) {
                            this.nestedType_ = descriptorProto.nestedType_;
                            this.bitField0_ &= -9;
                        } else {
                            this.ensureNestedTypeIsMutable();
                            this.nestedType_.addAll(descriptorProto.nestedType_);
                        }
                        this.onChanged();
                    }
                } else if (!descriptorProto.nestedType_.isEmpty()) {
                    if (this.nestedTypeBuilder_.isEmpty()) {
                        this.nestedTypeBuilder_.dispose();
                        this.nestedTypeBuilder_ = null;
                        this.nestedType_ = descriptorProto.nestedType_;
                        this.bitField0_ &= -9;
                        repeatedFieldBuilder = GeneratedMessage.alwaysUseFieldBuilders ? this.getNestedTypeFieldBuilder() : null;
                        this.nestedTypeBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.nestedTypeBuilder_.addAllMessages(descriptorProto.nestedType_);
                    }
                }
                if (this.enumTypeBuilder_ == null) {
                    if (!descriptorProto.enumType_.isEmpty()) {
                        if (this.enumType_.isEmpty()) {
                            this.enumType_ = descriptorProto.enumType_;
                            this.bitField0_ &= -17;
                        } else {
                            this.ensureEnumTypeIsMutable();
                            this.enumType_.addAll(descriptorProto.enumType_);
                        }
                        this.onChanged();
                    }
                } else if (!descriptorProto.enumType_.isEmpty()) {
                    if (this.enumTypeBuilder_.isEmpty()) {
                        this.enumTypeBuilder_.dispose();
                        this.enumTypeBuilder_ = null;
                        this.enumType_ = descriptorProto.enumType_;
                        this.bitField0_ &= -17;
                        repeatedFieldBuilder = GeneratedMessage.alwaysUseFieldBuilders ? this.getEnumTypeFieldBuilder() : null;
                        this.enumTypeBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.enumTypeBuilder_.addAllMessages(descriptorProto.enumType_);
                    }
                }
                if (this.extensionRangeBuilder_ == null) {
                    if (!descriptorProto.extensionRange_.isEmpty()) {
                        if (this.extensionRange_.isEmpty()) {
                            this.extensionRange_ = descriptorProto.extensionRange_;
                            this.bitField0_ &= -33;
                        } else {
                            this.ensureExtensionRangeIsMutable();
                            this.extensionRange_.addAll(descriptorProto.extensionRange_);
                        }
                        this.onChanged();
                    }
                } else if (!descriptorProto.extensionRange_.isEmpty()) {
                    if (this.extensionRangeBuilder_.isEmpty()) {
                        this.extensionRangeBuilder_.dispose();
                        this.extensionRangeBuilder_ = null;
                        this.extensionRange_ = descriptorProto.extensionRange_;
                        this.bitField0_ &= -33;
                        repeatedFieldBuilder = var3_3;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getExtensionRangeFieldBuilder();
                        }
                        this.extensionRangeBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.extensionRangeBuilder_.addAllMessages(descriptorProto.extensionRange_);
                    }
                }
                if (descriptorProto.hasOptions()) {
                    this.mergeOptions(descriptorProto.getOptions());
                }
                this.mergeUnknownFields(descriptorProto.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof DescriptorProto) {
                    return this.mergeFrom((DescriptorProto)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(MessageOptions messageOptions) {
                SingleFieldBuilder<MessageOptions, MessageOptions.Builder, MessageOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = (this.bitField0_ & 64) == 64 && this.options_ != MessageOptions.getDefaultInstance() ? MessageOptions.newBuilder(this.options_).mergeFrom(messageOptions).buildPartial() : messageOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.mergeFrom(messageOptions);
                }
                this.bitField0_ |= 64;
                return this;
            }

            public Builder removeEnumType(int n) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    this.enumType_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder removeExtension(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    this.extension_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder removeExtensionRange(int n) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionRangeIsMutable();
                    this.extensionRange_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder removeField(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFieldIsMutable();
                    this.field_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder removeNestedType(int n) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNestedTypeIsMutable();
                    this.nestedType_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setEnumType(int n, EnumDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    this.enumType_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setEnumType(int n, EnumDescriptorProto enumDescriptorProto) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumDescriptorProto != null) {
                        this.ensureEnumTypeIsMutable();
                        this.enumType_.set(n, enumDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, enumDescriptorProto);
                return this;
            }

            public Builder setExtension(int n, FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    this.extension_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setExtension(int n, FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureExtensionIsMutable();
                        this.extension_.set(n, fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, fieldDescriptorProto);
                return this;
            }

            public Builder setExtensionRange(int n, ExtensionRange.Builder builder) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionRangeIsMutable();
                    this.extensionRange_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setExtensionRange(int n, ExtensionRange extensionRange) {
                RepeatedFieldBuilder<ExtensionRange, ExtensionRange.Builder, ExtensionRangeOrBuilder> repeatedFieldBuilder = this.extensionRangeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (extensionRange != null) {
                        this.ensureExtensionRangeIsMutable();
                        this.extensionRange_.set(n, extensionRange);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, extensionRange);
                return this;
            }

            public Builder setField(int n, FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFieldIsMutable();
                    this.field_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setField(int n, FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fieldBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureFieldIsMutable();
                        this.field_.set(n, fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, fieldDescriptorProto);
                return this;
            }

            public Builder setName(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 1;
                    this.name_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 1;
                    this.name_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNestedType(int n, Builder builder) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNestedTypeIsMutable();
                    this.nestedType_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setNestedType(int n, DescriptorProto descriptorProto) {
                RepeatedFieldBuilder<DescriptorProto, Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.nestedTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (descriptorProto != null) {
                        this.ensureNestedTypeIsMutable();
                        this.nestedType_.set(n, descriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, descriptorProto);
                return this;
            }

            public Builder setOptions(MessageOptions.Builder builder) {
                SingleFieldBuilder<MessageOptions, MessageOptions.Builder, MessageOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = builder.build();
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(builder.build());
                }
                this.bitField0_ |= 64;
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Builder setOptions(MessageOptions messageOptions) {
                SingleFieldBuilder<MessageOptions, MessageOptions.Builder, MessageOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    if (messageOptions == null) throw null;
                    this.options_ = messageOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(messageOptions);
                }
                this.bitField0_ |= 64;
                return this;
            }
        }

        public static final class ExtensionRange
        extends GeneratedMessage
        implements ExtensionRangeOrBuilder {
            public static final int END_FIELD_NUMBER = 2;
            public static Parser<ExtensionRange> PARSER;
            public static final int START_FIELD_NUMBER = 1;
            private static final ExtensionRange defaultInstance;
            private static final long serialVersionUID = 0L;
            private int bitField0_;
            private int end_;
            private byte memoizedIsInitialized = (byte)-1;
            private int memoizedSerializedSize = -1;
            private int start_;
            private final UnknownFieldSet unknownFields;

            static {
                ExtensionRange extensionRange;
                PARSER = new AbstractParser<ExtensionRange>(){

                    @Override
                    public ExtensionRange parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                        return new ExtensionRange(codedInputStream, extensionRegistryLite);
                    }
                };
                defaultInstance = extensionRange = new ExtensionRange(true);
                extensionRange.initFields();
            }

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            private ExtensionRange(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
                super();
                this.initFields();
                var5_6 = UnknownFieldSet.newBuilder();
                var3_7 = false;
                do {
                    block8 : {
                        if (var3_7) {
                            this.unknownFields = var5_6.build();
                            this.makeExtensionsImmutable();
                            return;
                        }
                        var4_8 = var1_1.readTag();
                        if (var4_8 == 0) break block8;
                        if (var4_8 == 8) ** GOTO lbl24
                        if (var4_8 == 16) ** GOTO lbl21
                        if (this.parseUnknownField(var1_1, var5_6, var2_5, var4_8)) continue;
                        var3_7 = true;
                        continue;
lbl21: // 1 sources:
                        this.bitField0_ |= 2;
                        this.end_ = var1_1.readInt32();
                        continue;
lbl24: // 1 sources:
                        this.bitField0_ |= 1;
                        this.start_ = var1_1.readInt32();
                        continue;
                    }
                    var3_7 = true;
                    continue;
                    break;
                } while (true);
                catch (Throwable var1_2) {
                }
                catch (IOException var1_3) {
                    throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                }
                catch (InvalidProtocolBufferException var1_4) {
                    throw var1_4.setUnfinishedMessage(this);
                }
                this.unknownFields = var5_6.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }

            private ExtensionRange(GeneratedMessage.Builder<?> builder) {
                super(builder);
                this.unknownFields = builder.getUnknownFields();
            }

            private ExtensionRange(boolean bl) {
                this.unknownFields = UnknownFieldSet.getDefaultInstance();
            }

            public static ExtensionRange getDefaultInstance() {
                return defaultInstance;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
            }

            private void initFields() {
                this.start_ = 0;
                this.end_ = 0;
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            public static Builder newBuilder(ExtensionRange extensionRange) {
                return ExtensionRange.newBuilder().mergeFrom(extensionRange);
            }

            public static ExtensionRange parseDelimitedFrom(InputStream inputStream) throws IOException {
                return PARSER.parseDelimitedFrom(inputStream);
            }

            public static ExtensionRange parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
            }

            public static ExtensionRange parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(byteString);
            }

            public static ExtensionRange parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(byteString, extensionRegistryLite);
            }

            public static ExtensionRange parseFrom(CodedInputStream codedInputStream) throws IOException {
                return PARSER.parseFrom(codedInputStream);
            }

            public static ExtensionRange parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
            }

            public static ExtensionRange parseFrom(InputStream inputStream) throws IOException {
                return PARSER.parseFrom(inputStream);
            }

            public static ExtensionRange parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseFrom(inputStream, extensionRegistryLite);
            }

            public static ExtensionRange parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(arrby);
            }

            public static ExtensionRange parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(arrby, extensionRegistryLite);
            }

            @Override
            public ExtensionRange getDefaultInstanceForType() {
                return defaultInstance;
            }

            @Override
            public int getEnd() {
                return this.end_;
            }

            public Parser<ExtensionRange> getParserForType() {
                return PARSER;
            }

            @Override
            public int getSerializedSize() {
                int n = this.memoizedSerializedSize;
                if (n != -1) {
                    return n;
                }
                n = 0;
                if ((this.bitField0_ & 1) == 1) {
                    n = 0 + CodedOutputStream.computeInt32Size(1, this.start_);
                }
                int n2 = n;
                if ((this.bitField0_ & 2) == 2) {
                    n2 = n + CodedOutputStream.computeInt32Size(2, this.end_);
                }
                this.memoizedSerializedSize = n = n2 + this.getUnknownFields().getSerializedSize();
                return n;
            }

            @Override
            public int getStart() {
                return this.start_;
            }

            @Override
            public final UnknownFieldSet getUnknownFields() {
                return this.unknownFields;
            }

            @Override
            public boolean hasEnd() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasStart() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable.ensureFieldAccessorsInitialized(ExtensionRange.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                byte by = this.memoizedIsInitialized;
                if (by != -1) {
                    if (by == 1) {
                        return true;
                    }
                    return false;
                }
                this.memoizedIsInitialized = 1;
                return true;
            }

            @Override
            public Builder newBuilderForType() {
                return ExtensionRange.newBuilder();
            }

            @Override
            protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
                return new Builder(builderParent);
            }

            @Override
            public Builder toBuilder() {
                return ExtensionRange.newBuilder(this);
            }

            @Override
            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            @Override
            public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
                this.getSerializedSize();
                if ((this.bitField0_ & 1) == 1) {
                    codedOutputStream.writeInt32(1, this.start_);
                }
                if ((this.bitField0_ & 2) == 2) {
                    codedOutputStream.writeInt32(2, this.end_);
                }
                this.getUnknownFields().writeTo(codedOutputStream);
            }

            public static final class Builder
            extends GeneratedMessage.Builder<Builder>
            implements ExtensionRangeOrBuilder {
                private int bitField0_;
                private int end_;
                private int start_;

                private Builder() {
                    this.maybeForceBuilderInitialization();
                }

                private Builder(GeneratedMessage.BuilderParent builderParent) {
                    super(builderParent);
                    this.maybeForceBuilderInitialization();
                }

                private static Builder create() {
                    return new Builder();
                }

                public static final Descriptors.Descriptor getDescriptor() {
                    return internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
                }

                private void maybeForceBuilderInitialization() {
                    boolean bl = GeneratedMessage.alwaysUseFieldBuilders;
                }

                @Override
                public ExtensionRange build() {
                    ExtensionRange extensionRange = this.buildPartial();
                    if (extensionRange.isInitialized()) {
                        return extensionRange;
                    }
                    throw Builder.newUninitializedMessageException(extensionRange);
                }

                @Override
                public ExtensionRange buildPartial() {
                    ExtensionRange extensionRange = new ExtensionRange(this);
                    int n = this.bitField0_;
                    int n2 = 0;
                    if ((n & 1) == 1) {
                        n2 = false | true;
                    }
                    extensionRange.start_ = this.start_;
                    int n3 = n2;
                    if ((n & 2) == 2) {
                        n3 = n2 | 2;
                    }
                    extensionRange.end_ = this.end_;
                    extensionRange.bitField0_ = n3;
                    this.onBuilt();
                    return extensionRange;
                }

                @Override
                public Builder clear() {
                    int n;
                    super.clear();
                    this.start_ = 0;
                    this.bitField0_ = n = this.bitField0_ & -2;
                    this.end_ = 0;
                    this.bitField0_ = n & -3;
                    return this;
                }

                public Builder clearEnd() {
                    this.bitField0_ &= -3;
                    this.end_ = 0;
                    this.onChanged();
                    return this;
                }

                public Builder clearStart() {
                    this.bitField0_ &= -2;
                    this.start_ = 0;
                    this.onChanged();
                    return this;
                }

                @Override
                public Builder clone() {
                    return Builder.create().mergeFrom(this.buildPartial());
                }

                @Override
                public ExtensionRange getDefaultInstanceForType() {
                    return ExtensionRange.getDefaultInstance();
                }

                @Override
                public Descriptors.Descriptor getDescriptorForType() {
                    return internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
                }

                @Override
                public int getEnd() {
                    return this.end_;
                }

                @Override
                public int getStart() {
                    return this.start_;
                }

                @Override
                public boolean hasEnd() {
                    if ((this.bitField0_ & 2) == 2) {
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean hasStart() {
                    if ((this.bitField0_ & 1) == 1) {
                        return true;
                    }
                    return false;
                }

                @Override
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable.ensureFieldAccessorsInitialized(ExtensionRange.class, Builder.class);
                }

                @Override
                public final boolean isInitialized() {
                    return true;
                }

                /*
                 * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
                 * Loose catch block
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 * Lifted jumps to return sites
                 */
                @Override
                public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                    Object object2;
                    Throwable throwable222;
                    Object object3 = object2 = null;
                    object = ExtensionRange.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                    if (object == null) return this;
                    this.mergeFrom((ExtensionRange)object);
                    return this;
                    {
                        catch (Throwable throwable222) {
                        }
                        catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                        object3 = object2;
                        {
                            object3 = object = (ExtensionRange)invalidProtocolBufferException.getUnfinishedMessage();
                            throw invalidProtocolBufferException;
                        }
                    }
                    if (object3 == null) throw throwable222;
                    this.mergeFrom((ExtensionRange)object3);
                    throw throwable222;
                }

                public Builder mergeFrom(ExtensionRange extensionRange) {
                    if (extensionRange == ExtensionRange.getDefaultInstance()) {
                        return this;
                    }
                    if (extensionRange.hasStart()) {
                        this.setStart(extensionRange.getStart());
                    }
                    if (extensionRange.hasEnd()) {
                        this.setEnd(extensionRange.getEnd());
                    }
                    this.mergeUnknownFields(extensionRange.getUnknownFields());
                    return this;
                }

                @Override
                public Builder mergeFrom(Message message) {
                    if (message instanceof ExtensionRange) {
                        return this.mergeFrom((ExtensionRange)message);
                    }
                    super.mergeFrom(message);
                    return this;
                }

                public Builder setEnd(int n) {
                    this.bitField0_ |= 2;
                    this.end_ = n;
                    this.onChanged();
                    return this;
                }

                public Builder setStart(int n) {
                    this.bitField0_ |= 1;
                    this.start_ = n;
                    this.onChanged();
                    return this;
                }
            }

        }

        public static interface ExtensionRangeOrBuilder
        extends MessageOrBuilder {
            public int getEnd();

            public int getStart();

            public boolean hasEnd();

            public boolean hasStart();
        }

    }

    public static interface DescriptorProtoOrBuilder
    extends MessageOrBuilder {
        public EnumDescriptorProto getEnumType(int var1);

        public int getEnumTypeCount();

        public List<EnumDescriptorProto> getEnumTypeList();

        public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int var1);

        public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList();

        public FieldDescriptorProto getExtension(int var1);

        public int getExtensionCount();

        public List<FieldDescriptorProto> getExtensionList();

        public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int var1);

        public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList();

        public DescriptorProto.ExtensionRange getExtensionRange(int var1);

        public int getExtensionRangeCount();

        public List<DescriptorProto.ExtensionRange> getExtensionRangeList();

        public DescriptorProto.ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int var1);

        public List<? extends DescriptorProto.ExtensionRangeOrBuilder> getExtensionRangeOrBuilderList();

        public FieldDescriptorProto getField(int var1);

        public int getFieldCount();

        public List<FieldDescriptorProto> getFieldList();

        public FieldDescriptorProtoOrBuilder getFieldOrBuilder(int var1);

        public List<? extends FieldDescriptorProtoOrBuilder> getFieldOrBuilderList();

        public String getName();

        public ByteString getNameBytes();

        public DescriptorProto getNestedType(int var1);

        public int getNestedTypeCount();

        public List<DescriptorProto> getNestedTypeList();

        public DescriptorProtoOrBuilder getNestedTypeOrBuilder(int var1);

        public List<? extends DescriptorProtoOrBuilder> getNestedTypeOrBuilderList();

        public MessageOptions getOptions();

        public MessageOptionsOrBuilder getOptionsOrBuilder();

        public boolean hasName();

        public boolean hasOptions();
    }

    public static final class EnumDescriptorProto
    extends GeneratedMessage
    implements EnumDescriptorProtoOrBuilder {
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int OPTIONS_FIELD_NUMBER = 3;
        public static Parser<EnumDescriptorProto> PARSER;
        public static final int VALUE_FIELD_NUMBER = 2;
        private static final EnumDescriptorProto defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private Object name_;
        private EnumOptions options_;
        private final UnknownFieldSet unknownFields;
        private List<EnumValueDescriptorProto> value_;

        static {
            EnumDescriptorProto enumDescriptorProto;
            PARSER = new AbstractParser<EnumDescriptorProto>(){

                @Override
                public EnumDescriptorProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new EnumDescriptorProto(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = enumDescriptorProto = new EnumDescriptorProto(true);
            enumDescriptorProto.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private EnumDescriptorProto(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = 0;
            var12_7 = UnknownFieldSet.newBuilder();
            var8_8 = 0;
            while (var8_8 == 0) {
                block17 : {
                    block18 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl89
                        if (var10_14 == 10) ** GOTO lbl78
                        if (var10_14 == 18) break block18;
                        if (var10_14 != 26) {
                            var9_13 = var3_6;
                            var4_9 = var8_8;
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            if (!this.parseUnknownField(var1_1, var12_7, var2_5, var10_14)) {
                                var4_9 = 1;
                                var9_13 = var3_6;
                            }
                        } else {
                            var11_15 = null;
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            if ((this.bitField0_ & 2) == 2) {
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                var7_12 = var3_6;
                                var11_15 = this.options_.toBuilder();
                            }
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            var13_16 = var1_1.readMessage(EnumOptions.PARSER, var2_5);
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            this.options_ = var13_16;
                            if (var11_15 != null) {
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                var7_12 = var3_6;
                                var11_15.mergeFrom(var13_16);
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                var7_12 = var3_6;
                                this.options_ = var11_15.buildPartial();
                            }
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            this.bitField0_ |= 2;
                            var9_13 = var3_6;
                            var4_9 = var8_8;
                        }
                        ** GOTO lbl91
                    }
                    var4_9 = var3_6;
                    if ((var3_6 & 2) != 2) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.value_ = new ArrayList<EnumValueDescriptorProto>();
                        var4_9 = var3_6 | 2;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.value_.add(var1_1.readMessage(EnumValueDescriptorProto.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl78: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 1;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.name_ = var1_1.readBytes();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl89: // 1 sources:
                        var4_9 = 1;
                        var9_13 = var3_6;
lbl91: // 5 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block17;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if ((var5_10 & 2) == 2) {
                    this.value_ = Collections.unmodifiableList(this.value_);
                }
                this.unknownFields = var12_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if ((var3_6 & 2) == 2) {
                this.value_ = Collections.unmodifiableList(this.value_);
            }
            this.unknownFields = var12_7.build();
            this.makeExtensionsImmutable();
        }

        private EnumDescriptorProto(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private EnumDescriptorProto(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static EnumDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_EnumDescriptorProto_descriptor;
        }

        private void initFields() {
            this.name_ = "";
            this.value_ = Collections.emptyList();
            this.options_ = EnumOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(EnumDescriptorProto enumDescriptorProto) {
            return EnumDescriptorProto.newBuilder().mergeFrom(enumDescriptorProto);
        }

        public static EnumDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static EnumDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static EnumDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static EnumDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static EnumDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static EnumDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static EnumDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static EnumDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static EnumDescriptorProto parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static EnumDescriptorProto parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public EnumDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public String getName() {
            Object object = this.name_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.name_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getNameBytes() {
            Object object = this.name_;
            if (object instanceof String) {
                this.name_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public EnumOptions getOptions() {
            return this.options_;
        }

        @Override
        public EnumOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public Parser<EnumDescriptorProto> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n;
            int n2 = this.memoizedSerializedSize;
            if (n2 != -1) {
                return n2;
            }
            n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }
            for (n = 0; n < this.value_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(2, this.value_.get(n));
            }
            n = n2;
            if ((this.bitField0_ & 2) == 2) {
                n = n2 + CodedOutputStream.computeMessageSize(3, this.options_);
            }
            this.memoizedSerializedSize = n2 = n + this.getUnknownFields().getSerializedSize();
            return n2;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public EnumValueDescriptorProto getValue(int n) {
            return this.value_.get(n);
        }

        @Override
        public int getValueCount() {
            return this.value_.size();
        }

        @Override
        public List<EnumValueDescriptorProto> getValueList() {
            return this.value_;
        }

        @Override
        public EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int n) {
            return this.value_.get(n);
        }

        @Override
        public List<? extends EnumValueDescriptorProtoOrBuilder> getValueOrBuilderList() {
            return this.value_;
        }

        @Override
        public boolean hasName() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOptions() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(EnumDescriptorProto.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getValueCount(); ++n) {
                if (this.getValue(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasOptions() && !this.getOptions().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return EnumDescriptorProto.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return EnumDescriptorProto.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, this.getNameBytes());
            }
            for (int i = 0; i < this.value_.size(); ++i) {
                codedOutputStream.writeMessage(2, this.value_.get(i));
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeMessage(3, this.options_);
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements EnumDescriptorProtoOrBuilder {
            private int bitField0_;
            private Object name_ = "";
            private SingleFieldBuilder<EnumOptions, EnumOptions.Builder, EnumOptionsOrBuilder> optionsBuilder_;
            private EnumOptions options_ = EnumOptions.getDefaultInstance();
            private RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> valueBuilder_;
            private List<EnumValueDescriptorProto> value_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureValueIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.value_ = new ArrayList<EnumValueDescriptorProto>(this.value_);
                    this.bitField0_ |= 2;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_EnumDescriptorProto_descriptor;
            }

            private SingleFieldBuilder<EnumOptions, EnumOptions.Builder, EnumOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> getValueFieldBuilder() {
                if (this.valueBuilder_ == null) {
                    List<EnumValueDescriptorProto> list = this.value_;
                    boolean bl = (this.bitField0_ & 2) == 2;
                    this.valueBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.value_ = null;
                }
                return this.valueBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getValueFieldBuilder();
                    this.getOptionsFieldBuilder();
                }
            }

            public Builder addAllValue(Iterable<? extends EnumValueDescriptorProto> iterable) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureValueIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.value_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addValue(int n, EnumValueDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureValueIsMutable();
                    this.value_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addValue(int n, EnumValueDescriptorProto enumValueDescriptorProto) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumValueDescriptorProto != null) {
                        this.ensureValueIsMutable();
                        this.value_.add(n, enumValueDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, enumValueDescriptorProto);
                return this;
            }

            public Builder addValue(EnumValueDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureValueIsMutable();
                    this.value_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addValue(EnumValueDescriptorProto enumValueDescriptorProto) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumValueDescriptorProto != null) {
                        this.ensureValueIsMutable();
                        this.value_.add(enumValueDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(enumValueDescriptorProto);
                return this;
            }

            public EnumValueDescriptorProto.Builder addValueBuilder() {
                return this.getValueFieldBuilder().addBuilder(EnumValueDescriptorProto.getDefaultInstance());
            }

            public EnumValueDescriptorProto.Builder addValueBuilder(int n) {
                return this.getValueFieldBuilder().addBuilder(n, EnumValueDescriptorProto.getDefaultInstance());
            }

            @Override
            public EnumDescriptorProto build() {
                EnumDescriptorProto enumDescriptorProto = this.buildPartial();
                if (enumDescriptorProto.isInitialized()) {
                    return enumDescriptorProto;
                }
                throw Builder.newUninitializedMessageException(enumDescriptorProto);
            }

            @Override
            public EnumDescriptorProto buildPartial() {
                EnumDescriptorProto enumDescriptorProto = new EnumDescriptorProto(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                enumDescriptorProto.name_ = this.name_;
                GeneratedMessage.BuilderParent builderParent = this.valueBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 2) == 2) {
                        this.value_ = Collections.unmodifiableList(this.value_);
                        this.bitField0_ &= -3;
                    }
                    enumDescriptorProto.value_ = this.value_;
                } else {
                    enumDescriptorProto.value_ = builderParent.build();
                }
                int n3 = n2;
                if ((n & 4) == 4) {
                    n3 = n2 | 2;
                }
                if ((builderParent = this.optionsBuilder_) == null) {
                    enumDescriptorProto.options_ = this.options_;
                } else {
                    enumDescriptorProto.options_ = (EnumOptions)((Object)builderParent.build());
                }
                enumDescriptorProto.bitField0_ = n3;
                this.onBuilt();
                return enumDescriptorProto;
            }

            @Override
            public Builder clear() {
                super.clear();
                this.name_ = "";
                this.bitField0_ &= -2;
                GeneratedMessage.BuilderParent builderParent = this.valueBuilder_;
                if (builderParent == null) {
                    this.value_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    builderParent.clear();
                }
                builderParent = this.optionsBuilder_;
                if (builderParent == null) {
                    this.options_ = EnumOptions.getDefaultInstance();
                } else {
                    builderParent.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = EnumDescriptorProto.getDefaultInstance().getName();
                this.onChanged();
                return this;
            }

            public Builder clearOptions() {
                SingleFieldBuilder<EnumOptions, EnumOptions.Builder, EnumOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = EnumOptions.getDefaultInstance();
                    this.onChanged();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clearValue() {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.value_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public EnumDescriptorProto getDefaultInstanceForType() {
                return EnumDescriptorProto.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_EnumDescriptorProto_descriptor;
            }

            @Override
            public String getName() {
                Object object = this.name_;
                if (!(object instanceof String)) {
                    this.name_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getNameBytes() {
                Object object = this.name_;
                if (object instanceof String) {
                    this.name_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public EnumOptions getOptions() {
                SingleFieldBuilder<EnumOptions, EnumOptions.Builder, EnumOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    return this.options_;
                }
                return singleFieldBuilder.getMessage();
            }

            public EnumOptions.Builder getOptionsBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getOptionsFieldBuilder().getBuilder();
            }

            @Override
            public EnumOptionsOrBuilder getOptionsOrBuilder() {
                SingleFieldBuilder<EnumOptions, EnumOptions.Builder, EnumOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder != null) {
                    return singleFieldBuilder.getMessageOrBuilder();
                }
                return this.options_;
            }

            @Override
            public EnumValueDescriptorProto getValue(int n) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.value_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public EnumValueDescriptorProto.Builder getValueBuilder(int n) {
                return this.getValueFieldBuilder().getBuilder(n);
            }

            public List<EnumValueDescriptorProto.Builder> getValueBuilderList() {
                return this.getValueFieldBuilder().getBuilderList();
            }

            @Override
            public int getValueCount() {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.value_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<EnumValueDescriptorProto> getValueList() {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.value_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int n) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.value_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends EnumValueDescriptorProtoOrBuilder> getValueOrBuilderList() {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.value_);
            }

            @Override
            public boolean hasName() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOptions() {
                if ((this.bitField0_ & 4) == 4) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(EnumDescriptorProto.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getValueCount(); ++i) {
                    if (this.getValue(i).isInitialized()) continue;
                    return false;
                }
                if (this.hasOptions() && !this.getOptions().isInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = EnumDescriptorProto.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((EnumDescriptorProto)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (EnumDescriptorProto)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((EnumDescriptorProto)object3);
                throw throwable222;
            }

            public Builder mergeFrom(EnumDescriptorProto enumDescriptorProto) {
                if (enumDescriptorProto == EnumDescriptorProto.getDefaultInstance()) {
                    return this;
                }
                if (enumDescriptorProto.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = enumDescriptorProto.name_;
                    this.onChanged();
                }
                if (this.valueBuilder_ == null) {
                    if (!enumDescriptorProto.value_.isEmpty()) {
                        if (this.value_.isEmpty()) {
                            this.value_ = enumDescriptorProto.value_;
                            this.bitField0_ &= -3;
                        } else {
                            this.ensureValueIsMutable();
                            this.value_.addAll(enumDescriptorProto.value_);
                        }
                        this.onChanged();
                    }
                } else if (!enumDescriptorProto.value_.isEmpty()) {
                    if (this.valueBuilder_.isEmpty()) {
                        this.valueBuilder_.dispose();
                        RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = null;
                        this.valueBuilder_ = null;
                        this.value_ = enumDescriptorProto.value_;
                        this.bitField0_ &= -3;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getValueFieldBuilder();
                        }
                        this.valueBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.valueBuilder_.addAllMessages(enumDescriptorProto.value_);
                    }
                }
                if (enumDescriptorProto.hasOptions()) {
                    this.mergeOptions(enumDescriptorProto.getOptions());
                }
                this.mergeUnknownFields(enumDescriptorProto.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof EnumDescriptorProto) {
                    return this.mergeFrom((EnumDescriptorProto)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(EnumOptions enumOptions) {
                SingleFieldBuilder<EnumOptions, EnumOptions.Builder, EnumOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = (this.bitField0_ & 4) == 4 && this.options_ != EnumOptions.getDefaultInstance() ? EnumOptions.newBuilder(this.options_).mergeFrom(enumOptions).buildPartial() : enumOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.mergeFrom(enumOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder removeValue(int n) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureValueIsMutable();
                    this.value_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setName(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 1;
                    this.name_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 1;
                    this.name_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setOptions(EnumOptions.Builder builder) {
                SingleFieldBuilder<EnumOptions, EnumOptions.Builder, EnumOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = builder.build();
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(builder.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Builder setOptions(EnumOptions enumOptions) {
                SingleFieldBuilder<EnumOptions, EnumOptions.Builder, EnumOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    if (enumOptions == null) throw null;
                    this.options_ = enumOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(enumOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setValue(int n, EnumValueDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureValueIsMutable();
                    this.value_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setValue(int n, EnumValueDescriptorProto enumValueDescriptorProto) {
                RepeatedFieldBuilder<EnumValueDescriptorProto, EnumValueDescriptorProto.Builder, EnumValueDescriptorProtoOrBuilder> repeatedFieldBuilder = this.valueBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumValueDescriptorProto != null) {
                        this.ensureValueIsMutable();
                        this.value_.set(n, enumValueDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, enumValueDescriptorProto);
                return this;
            }
        }

    }

    public static interface EnumDescriptorProtoOrBuilder
    extends MessageOrBuilder {
        public String getName();

        public ByteString getNameBytes();

        public EnumOptions getOptions();

        public EnumOptionsOrBuilder getOptionsOrBuilder();

        public EnumValueDescriptorProto getValue(int var1);

        public int getValueCount();

        public List<EnumValueDescriptorProto> getValueList();

        public EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int var1);

        public List<? extends EnumValueDescriptorProtoOrBuilder> getValueOrBuilderList();

        public boolean hasName();

        public boolean hasOptions();
    }

    public static final class EnumOptions
    extends GeneratedMessage.ExtendableMessage<EnumOptions>
    implements EnumOptionsOrBuilder {
        public static final int ALLOW_ALIAS_FIELD_NUMBER = 2;
        public static Parser<EnumOptions> PARSER;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final EnumOptions defaultInstance;
        private static final long serialVersionUID = 0L;
        private boolean allowAlias_;
        private int bitField0_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private List<UninterpretedOption> uninterpretedOption_;
        private final UnknownFieldSet unknownFields;

        static {
            EnumOptions enumOptions;
            PARSER = new AbstractParser<EnumOptions>(){

                @Override
                public EnumOptions parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new EnumOptions(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = enumOptions = new EnumOptions(true);
            enumOptions.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private EnumOptions(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = 0;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = 0;
            while (var8_8 == 0) {
                block13 : {
                    block14 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl55
                        if (var10_14 == 16) ** GOTO lbl44
                        if (var10_14 == 7994) break block14;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                            var4_9 = 1;
                            var9_13 = var3_6;
                        }
                        ** GOTO lbl57
                    }
                    var4_9 = var3_6;
                    if ((var3_6 & 2) != 2) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.uninterpretedOption_ = new ArrayList<UninterpretedOption>();
                        var4_9 = var3_6 | 2;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.uninterpretedOption_.add(var1_1.readMessage(UninterpretedOption.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl44: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 1;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.allowAlias_ = var1_1.readBool();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl55: // 1 sources:
                        var4_9 = 1;
                        var9_13 = var3_6;
lbl57: // 4 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block13;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if ((var5_10 & 2) == 2) {
                    this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if ((var3_6 & 2) == 2) {
                this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private EnumOptions(GeneratedMessage.ExtendableBuilder<EnumOptions, ?> extendableBuilder) {
            super(extendableBuilder);
            this.unknownFields = extendableBuilder.getUnknownFields();
        }

        private EnumOptions(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static EnumOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_EnumOptions_descriptor;
        }

        private void initFields() {
            this.allowAlias_ = true;
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(EnumOptions enumOptions) {
            return EnumOptions.newBuilder().mergeFrom(enumOptions);
        }

        public static EnumOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static EnumOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static EnumOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static EnumOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static EnumOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static EnumOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static EnumOptions parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static EnumOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static EnumOptions parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static EnumOptions parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public boolean getAllowAlias() {
            return this.allowAlias_;
        }

        @Override
        public EnumOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public Parser<EnumOptions> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            n = 0;
            if ((this.bitField0_ & 1) == 1) {
                n = 0 + CodedOutputStream.computeBoolSize(2, this.allowAlias_);
            }
            for (int i = 0; i < this.uninterpretedOption_.size(); ++i) {
                n += CodedOutputStream.computeMessageSize(999, this.uninterpretedOption_.get(i));
            }
            this.memoizedSerializedSize = n = n + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public UninterpretedOption getUninterpretedOption(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        @Override
        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        @Override
        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasAllowAlias() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_EnumOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(EnumOptions.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getUninterpretedOptionCount(); ++n) {
                if (this.getUninterpretedOption(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.extensionsAreInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return EnumOptions.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return EnumOptions.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            GeneratedMessage.ExtendableMessage.ExtensionWriter extensionWriter = this.newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBool(2, this.allowAlias_);
            }
            for (int i = 0; i < this.uninterpretedOption_.size(); ++i) {
                codedOutputStream.writeMessage(999, this.uninterpretedOption_.get(i));
            }
            extensionWriter.writeUntil(536870912, codedOutputStream);
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.ExtendableBuilder<EnumOptions, Builder>
        implements EnumOptionsOrBuilder {
            private boolean allowAlias_ = true;
            private int bitField0_;
            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.uninterpretedOption_ = new ArrayList<UninterpretedOption>(this.uninterpretedOption_);
                    this.bitField0_ |= 2;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_EnumOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    List<UninterpretedOption> list = this.uninterpretedOption_;
                    boolean bl = (this.bitField0_ & 2) == 2;
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    GeneratedMessage.ExtendableBuilder.addAll(iterable, this.uninterpretedOption_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, uninterpretedOption);
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(uninterpretedOption);
                return this;
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder() {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(n, UninterpretedOption.getDefaultInstance());
            }

            @Override
            public EnumOptions build() {
                EnumOptions enumOptions = this.buildPartial();
                if (enumOptions.isInitialized()) {
                    return enumOptions;
                }
                throw Builder.newUninitializedMessageException(enumOptions);
            }

            @Override
            public EnumOptions buildPartial() {
                EnumOptions enumOptions = new EnumOptions(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                enumOptions.allowAlias_ = this.allowAlias_;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 2) == 2) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -3;
                    }
                    enumOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    enumOptions.uninterpretedOption_ = repeatedFieldBuilder.build();
                }
                enumOptions.bitField0_ = n2;
                this.onBuilt();
                return enumOptions;
            }

            @Override
            public Builder clear() {
                super.clear();
                this.allowAlias_ = true;
                this.bitField0_ &= -2;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearAllowAlias() {
                this.bitField0_ &= -2;
                this.allowAlias_ = true;
                this.onChanged();
                return this;
            }

            public Builder clearUninterpretedOption() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public boolean getAllowAlias() {
                return this.allowAlias_;
            }

            @Override
            public EnumOptions getDefaultInstanceForType() {
                return EnumOptions.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_EnumOptions_descriptor;
            }

            @Override
            public UninterpretedOption getUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public UninterpretedOption.Builder getUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().getBuilder(n);
            }

            public List<UninterpretedOption.Builder> getUninterpretedOptionBuilderList() {
                return this.getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            @Override
            public int getUninterpretedOptionCount() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<UninterpretedOption> getUninterpretedOptionList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.uninterpretedOption_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.uninterpretedOption_);
            }

            @Override
            public boolean hasAllowAlias() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_EnumOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(EnumOptions.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getUninterpretedOptionCount(); ++i) {
                    if (this.getUninterpretedOption(i).isInitialized()) continue;
                    return false;
                }
                if (!this.extensionsAreInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = EnumOptions.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((EnumOptions)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (EnumOptions)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((EnumOptions)object3);
                throw throwable222;
            }

            public Builder mergeFrom(EnumOptions enumOptions) {
                if (enumOptions == EnumOptions.getDefaultInstance()) {
                    return this;
                }
                if (enumOptions.hasAllowAlias()) {
                    this.setAllowAlias(enumOptions.getAllowAlias());
                }
                if (this.uninterpretedOptionBuilder_ == null) {
                    if (!enumOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOption_.isEmpty()) {
                            this.uninterpretedOption_ = enumOptions.uninterpretedOption_;
                            this.bitField0_ &= -3;
                        } else {
                            this.ensureUninterpretedOptionIsMutable();
                            this.uninterpretedOption_.addAll(enumOptions.uninterpretedOption_);
                        }
                        this.onChanged();
                    }
                } else if (!enumOptions.uninterpretedOption_.isEmpty()) {
                    if (this.uninterpretedOptionBuilder_.isEmpty()) {
                        this.uninterpretedOptionBuilder_.dispose();
                        RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = null;
                        this.uninterpretedOptionBuilder_ = null;
                        this.uninterpretedOption_ = enumOptions.uninterpretedOption_;
                        this.bitField0_ &= -3;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getUninterpretedOptionFieldBuilder();
                        }
                        this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.uninterpretedOptionBuilder_.addAllMessages(enumOptions.uninterpretedOption_);
                    }
                }
                this.mergeExtensionFields(enumOptions);
                this.mergeUnknownFields(enumOptions.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof EnumOptions) {
                    return this.mergeFrom((EnumOptions)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setAllowAlias(boolean bl) {
                this.bitField0_ |= 1;
                this.allowAlias_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.set(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, uninterpretedOption);
                return this;
            }
        }

    }

    public static interface EnumOptionsOrBuilder
    extends GeneratedMessage.ExtendableMessageOrBuilder<EnumOptions> {
        public boolean getAllowAlias();

        public UninterpretedOption getUninterpretedOption(int var1);

        public int getUninterpretedOptionCount();

        public List<UninterpretedOption> getUninterpretedOptionList();

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();

        public boolean hasAllowAlias();
    }

    public static final class EnumValueDescriptorProto
    extends GeneratedMessage
    implements EnumValueDescriptorProtoOrBuilder {
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int NUMBER_FIELD_NUMBER = 2;
        public static final int OPTIONS_FIELD_NUMBER = 3;
        public static Parser<EnumValueDescriptorProto> PARSER;
        private static final EnumValueDescriptorProto defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private Object name_;
        private int number_;
        private EnumValueOptions options_;
        private final UnknownFieldSet unknownFields;

        static {
            EnumValueDescriptorProto enumValueDescriptorProto;
            PARSER = new AbstractParser<EnumValueDescriptorProto>(){

                @Override
                public EnumValueDescriptorProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new EnumValueDescriptorProto(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = enumValueDescriptorProto = new EnumValueDescriptorProto(true);
            enumValueDescriptorProto.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private EnumValueDescriptorProto(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var6_6 = UnknownFieldSet.newBuilder();
            var3_7 = false;
            do {
                block10 : {
                    if (var3_7) {
                        this.unknownFields = var6_6.build();
                        this.makeExtensionsImmutable();
                        return;
                    }
                    var4_8 = var1_1.readTag();
                    if (var4_8 == 0) break block10;
                    if (var4_8 == 10) ** GOTO lbl34
                    if (var4_8 == 16) ** GOTO lbl31
                    if (var4_8 == 26) ** GOTO lbl22
                    if (this.parseUnknownField(var1_1, var6_6, var2_5, var4_8)) continue;
                    var3_7 = true;
                    continue;
lbl22: // 1 sources:
                    var5_9 = null;
                    if ((this.bitField0_ & 4) == 4) {
                        var5_9 = this.options_.toBuilder();
                    }
                    this.options_ = var7_10 = var1_1.readMessage(EnumValueOptions.PARSER, var2_5);
                    if (var5_9 != null) {
                        var5_9.mergeFrom(var7_10);
                        this.options_ = var5_9.buildPartial();
                    }
                    this.bitField0_ |= 4;
                    continue;
lbl31: // 1 sources:
                    this.bitField0_ |= 2;
                    this.number_ = var1_1.readInt32();
                    continue;
lbl34: // 1 sources:
                    this.bitField0_ |= 1;
                    this.name_ = var1_1.readBytes();
                    continue;
                }
                var3_7 = true;
                continue;
                break;
            } while (true);
            catch (Throwable var1_2) {
            }
            catch (IOException var1_3) {
                throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
            }
            catch (InvalidProtocolBufferException var1_4) {
                throw var1_4.setUnfinishedMessage(this);
            }
            this.unknownFields = var6_6.build();
            this.makeExtensionsImmutable();
            throw var1_2;
        }

        private EnumValueDescriptorProto(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private EnumValueDescriptorProto(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static EnumValueDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
        }

        private void initFields() {
            this.name_ = "";
            this.number_ = 0;
            this.options_ = EnumValueOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(EnumValueDescriptorProto enumValueDescriptorProto) {
            return EnumValueDescriptorProto.newBuilder().mergeFrom(enumValueDescriptorProto);
        }

        public static EnumValueDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static EnumValueDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static EnumValueDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static EnumValueDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static EnumValueDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static EnumValueDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static EnumValueDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static EnumValueDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static EnumValueDescriptorProto parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static EnumValueDescriptorProto parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public EnumValueDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public String getName() {
            Object object = this.name_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.name_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getNameBytes() {
            Object object = this.name_;
            if (object instanceof String) {
                this.name_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public int getNumber() {
            return this.number_;
        }

        @Override
        public EnumValueOptions getOptions() {
            return this.options_;
        }

        @Override
        public EnumValueOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public Parser<EnumValueDescriptorProto> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }
            n = n2;
            if ((this.bitField0_ & 2) == 2) {
                n = n2 + CodedOutputStream.computeInt32Size(2, this.number_);
            }
            n2 = n;
            if ((this.bitField0_ & 4) == 4) {
                n2 = n + CodedOutputStream.computeMessageSize(3, this.options_);
            }
            this.memoizedSerializedSize = n = n2 + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasName() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasNumber() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOptions() {
            if ((this.bitField0_ & 4) == 4) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(EnumValueDescriptorProto.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            byte by = this.memoizedIsInitialized;
            boolean bl = false;
            if (by != -1) {
                if (by == 1) {
                    bl = true;
                }
                return bl;
            }
            if (this.hasOptions() && !this.getOptions().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return EnumValueDescriptorProto.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return EnumValueDescriptorProto.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, this.getNameBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeInt32(2, this.number_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeMessage(3, this.options_);
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements EnumValueDescriptorProtoOrBuilder {
            private int bitField0_;
            private Object name_ = "";
            private int number_;
            private SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> optionsBuilder_;
            private EnumValueOptions options_ = EnumValueOptions.getDefaultInstance();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
            }

            private SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getOptionsFieldBuilder();
                }
            }

            @Override
            public EnumValueDescriptorProto build() {
                EnumValueDescriptorProto enumValueDescriptorProto = this.buildPartial();
                if (enumValueDescriptorProto.isInitialized()) {
                    return enumValueDescriptorProto;
                }
                throw Builder.newUninitializedMessageException(enumValueDescriptorProto);
            }

            @Override
            public EnumValueDescriptorProto buildPartial() {
                SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> singleFieldBuilder;
                EnumValueDescriptorProto enumValueDescriptorProto = new EnumValueDescriptorProto(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                enumValueDescriptorProto.name_ = this.name_;
                int n3 = n2;
                if ((n & 2) == 2) {
                    n3 = n2 | 2;
                }
                enumValueDescriptorProto.number_ = this.number_;
                n2 = n3;
                if ((n & 4) == 4) {
                    n2 = n3 | 4;
                }
                if ((singleFieldBuilder = this.optionsBuilder_) == null) {
                    enumValueDescriptorProto.options_ = this.options_;
                } else {
                    enumValueDescriptorProto.options_ = singleFieldBuilder.build();
                }
                enumValueDescriptorProto.bitField0_ = n2;
                this.onBuilt();
                return enumValueDescriptorProto;
            }

            @Override
            public Builder clear() {
                int n;
                super.clear();
                this.name_ = "";
                this.bitField0_ = n = this.bitField0_ & -2;
                this.number_ = 0;
                this.bitField0_ = n & -3;
                SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = EnumValueOptions.getDefaultInstance();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = EnumValueDescriptorProto.getDefaultInstance().getName();
                this.onChanged();
                return this;
            }

            public Builder clearNumber() {
                this.bitField0_ &= -3;
                this.number_ = 0;
                this.onChanged();
                return this;
            }

            public Builder clearOptions() {
                SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = EnumValueOptions.getDefaultInstance();
                    this.onChanged();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public EnumValueDescriptorProto getDefaultInstanceForType() {
                return EnumValueDescriptorProto.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
            }

            @Override
            public String getName() {
                Object object = this.name_;
                if (!(object instanceof String)) {
                    this.name_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getNameBytes() {
                Object object = this.name_;
                if (object instanceof String) {
                    this.name_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public int getNumber() {
                return this.number_;
            }

            @Override
            public EnumValueOptions getOptions() {
                SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    return this.options_;
                }
                return singleFieldBuilder.getMessage();
            }

            public EnumValueOptions.Builder getOptionsBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getOptionsFieldBuilder().getBuilder();
            }

            @Override
            public EnumValueOptionsOrBuilder getOptionsOrBuilder() {
                SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder != null) {
                    return singleFieldBuilder.getMessageOrBuilder();
                }
                return this.options_;
            }

            @Override
            public boolean hasName() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasNumber() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOptions() {
                if ((this.bitField0_ & 4) == 4) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(EnumValueDescriptorProto.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                if (this.hasOptions() && !this.getOptions().isInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = EnumValueDescriptorProto.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((EnumValueDescriptorProto)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (EnumValueDescriptorProto)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((EnumValueDescriptorProto)object3);
                throw throwable222;
            }

            public Builder mergeFrom(EnumValueDescriptorProto enumValueDescriptorProto) {
                if (enumValueDescriptorProto == EnumValueDescriptorProto.getDefaultInstance()) {
                    return this;
                }
                if (enumValueDescriptorProto.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = enumValueDescriptorProto.name_;
                    this.onChanged();
                }
                if (enumValueDescriptorProto.hasNumber()) {
                    this.setNumber(enumValueDescriptorProto.getNumber());
                }
                if (enumValueDescriptorProto.hasOptions()) {
                    this.mergeOptions(enumValueDescriptorProto.getOptions());
                }
                this.mergeUnknownFields(enumValueDescriptorProto.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof EnumValueDescriptorProto) {
                    return this.mergeFrom((EnumValueDescriptorProto)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(EnumValueOptions enumValueOptions) {
                SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = (this.bitField0_ & 4) == 4 && this.options_ != EnumValueOptions.getDefaultInstance() ? EnumValueOptions.newBuilder(this.options_).mergeFrom(enumValueOptions).buildPartial() : enumValueOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.mergeFrom(enumValueOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder setName(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 1;
                    this.name_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 1;
                    this.name_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNumber(int n) {
                this.bitField0_ |= 2;
                this.number_ = n;
                this.onChanged();
                return this;
            }

            public Builder setOptions(EnumValueOptions.Builder builder) {
                SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = builder.build();
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(builder.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Builder setOptions(EnumValueOptions enumValueOptions) {
                SingleFieldBuilder<EnumValueOptions, EnumValueOptions.Builder, EnumValueOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    if (enumValueOptions == null) throw null;
                    this.options_ = enumValueOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(enumValueOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }
        }

    }

    public static interface EnumValueDescriptorProtoOrBuilder
    extends MessageOrBuilder {
        public String getName();

        public ByteString getNameBytes();

        public int getNumber();

        public EnumValueOptions getOptions();

        public EnumValueOptionsOrBuilder getOptionsOrBuilder();

        public boolean hasName();

        public boolean hasNumber();

        public boolean hasOptions();
    }

    public static final class EnumValueOptions
    extends GeneratedMessage.ExtendableMessage<EnumValueOptions>
    implements EnumValueOptionsOrBuilder {
        public static Parser<EnumValueOptions> PARSER;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final EnumValueOptions defaultInstance;
        private static final long serialVersionUID = 0L;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private List<UninterpretedOption> uninterpretedOption_;
        private final UnknownFieldSet unknownFields;

        static {
            EnumValueOptions enumValueOptions;
            PARSER = new AbstractParser<EnumValueOptions>(){

                @Override
                public EnumValueOptions parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new EnumValueOptions(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = enumValueOptions = new EnumValueOptions(true);
            enumValueOptions.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private EnumValueOptions(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = false;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = false;
            while (!var8_8) {
                block13 : {
                    block14 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl43
                        if (var10_14 == 7994) break block14;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                            var4_9 = true;
                            var9_13 = var3_6;
                        }
                        ** GOTO lbl45
                    }
                    var4_9 = var3_6;
                    if (!(var3_6 & true)) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.uninterpretedOption_ = new ArrayList<UninterpretedOption>();
                        var4_9 = var3_6 | true;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.uninterpretedOption_.add(var1_1.readMessage(UninterpretedOption.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl43: // 1 sources:
                        var4_9 = true;
                        var9_13 = var3_6;
lbl45: // 3 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block13;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if (var5_10 & true) {
                    this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if (var3_6 & true) {
                this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private EnumValueOptions(GeneratedMessage.ExtendableBuilder<EnumValueOptions, ?> extendableBuilder) {
            super(extendableBuilder);
            this.unknownFields = extendableBuilder.getUnknownFields();
        }

        private EnumValueOptions(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static EnumValueOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_EnumValueOptions_descriptor;
        }

        private void initFields() {
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(EnumValueOptions enumValueOptions) {
            return EnumValueOptions.newBuilder().mergeFrom(enumValueOptions);
        }

        public static EnumValueOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static EnumValueOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static EnumValueOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static EnumValueOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static EnumValueOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static EnumValueOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static EnumValueOptions parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static EnumValueOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static EnumValueOptions parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static EnumValueOptions parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public EnumValueOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public Parser<EnumValueOptions> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            for (n = 0; n < this.uninterpretedOption_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(999, this.uninterpretedOption_.get(n));
            }
            this.memoizedSerializedSize = n = n2 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public UninterpretedOption getUninterpretedOption(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        @Override
        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        @Override
        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(EnumValueOptions.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getUninterpretedOptionCount(); ++n) {
                if (this.getUninterpretedOption(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.extensionsAreInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return EnumValueOptions.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return EnumValueOptions.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            GeneratedMessage.ExtendableMessage.ExtensionWriter extensionWriter = this.newExtensionWriter();
            for (int i = 0; i < this.uninterpretedOption_.size(); ++i) {
                codedOutputStream.writeMessage(999, this.uninterpretedOption_.get(i));
            }
            extensionWriter.writeUntil(536870912, codedOutputStream);
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.ExtendableBuilder<EnumValueOptions, Builder>
        implements EnumValueOptionsOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.uninterpretedOption_ = new ArrayList<UninterpretedOption>(this.uninterpretedOption_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_EnumValueOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    List<UninterpretedOption> list = this.uninterpretedOption_;
                    int n = this.bitField0_;
                    boolean bl = true;
                    if ((n & 1) != 1) {
                        bl = false;
                    }
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    GeneratedMessage.ExtendableBuilder.addAll(iterable, this.uninterpretedOption_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, uninterpretedOption);
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(uninterpretedOption);
                return this;
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder() {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(n, UninterpretedOption.getDefaultInstance());
            }

            @Override
            public EnumValueOptions build() {
                EnumValueOptions enumValueOptions = this.buildPartial();
                if (enumValueOptions.isInitialized()) {
                    return enumValueOptions;
                }
                throw Builder.newUninitializedMessageException(enumValueOptions);
            }

            @Override
            public EnumValueOptions buildPartial() {
                EnumValueOptions enumValueOptions = new EnumValueOptions(this);
                int n = this.bitField0_;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -2;
                    }
                    enumValueOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    enumValueOptions.uninterpretedOption_ = repeatedFieldBuilder.build();
                }
                this.onBuilt();
                return enumValueOptions;
            }

            @Override
            public Builder clear() {
                super.clear();
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearUninterpretedOption() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public EnumValueOptions getDefaultInstanceForType() {
                return EnumValueOptions.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_EnumValueOptions_descriptor;
            }

            @Override
            public UninterpretedOption getUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public UninterpretedOption.Builder getUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().getBuilder(n);
            }

            public List<UninterpretedOption.Builder> getUninterpretedOptionBuilderList() {
                return this.getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            @Override
            public int getUninterpretedOptionCount() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<UninterpretedOption> getUninterpretedOptionList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.uninterpretedOption_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.uninterpretedOption_);
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(EnumValueOptions.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getUninterpretedOptionCount(); ++i) {
                    if (this.getUninterpretedOption(i).isInitialized()) continue;
                    return false;
                }
                if (!this.extensionsAreInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = EnumValueOptions.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((EnumValueOptions)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (EnumValueOptions)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((EnumValueOptions)object3);
                throw throwable222;
            }

            public Builder mergeFrom(EnumValueOptions enumValueOptions) {
                if (enumValueOptions == EnumValueOptions.getDefaultInstance()) {
                    return this;
                }
                if (this.uninterpretedOptionBuilder_ == null) {
                    if (!enumValueOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOption_.isEmpty()) {
                            this.uninterpretedOption_ = enumValueOptions.uninterpretedOption_;
                            this.bitField0_ &= -2;
                        } else {
                            this.ensureUninterpretedOptionIsMutable();
                            this.uninterpretedOption_.addAll(enumValueOptions.uninterpretedOption_);
                        }
                        this.onChanged();
                    }
                } else if (!enumValueOptions.uninterpretedOption_.isEmpty()) {
                    if (this.uninterpretedOptionBuilder_.isEmpty()) {
                        this.uninterpretedOptionBuilder_.dispose();
                        RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = null;
                        this.uninterpretedOptionBuilder_ = null;
                        this.uninterpretedOption_ = enumValueOptions.uninterpretedOption_;
                        this.bitField0_ &= -2;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getUninterpretedOptionFieldBuilder();
                        }
                        this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.uninterpretedOptionBuilder_.addAllMessages(enumValueOptions.uninterpretedOption_);
                    }
                }
                this.mergeExtensionFields(enumValueOptions);
                this.mergeUnknownFields(enumValueOptions.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof EnumValueOptions) {
                    return this.mergeFrom((EnumValueOptions)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.set(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, uninterpretedOption);
                return this;
            }
        }

    }

    public static interface EnumValueOptionsOrBuilder
    extends GeneratedMessage.ExtendableMessageOrBuilder<EnumValueOptions> {
        public UninterpretedOption getUninterpretedOption(int var1);

        public int getUninterpretedOptionCount();

        public List<UninterpretedOption> getUninterpretedOptionList();

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();
    }

    public static final class FieldDescriptorProto
    extends GeneratedMessage
    implements FieldDescriptorProtoOrBuilder {
        public static final int DEFAULT_VALUE_FIELD_NUMBER = 7;
        public static final int EXTENDEE_FIELD_NUMBER = 2;
        public static final int LABEL_FIELD_NUMBER = 4;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int NUMBER_FIELD_NUMBER = 3;
        public static final int OPTIONS_FIELD_NUMBER = 8;
        public static Parser<FieldDescriptorProto> PARSER;
        public static final int TYPE_FIELD_NUMBER = 5;
        public static final int TYPE_NAME_FIELD_NUMBER = 6;
        private static final FieldDescriptorProto defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private Object defaultValue_;
        private Object extendee_;
        private Label label_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private Object name_;
        private int number_;
        private FieldOptions options_;
        private Object typeName_;
        private Type type_;
        private final UnknownFieldSet unknownFields;

        static {
            FieldDescriptorProto fieldDescriptorProto;
            PARSER = new AbstractParser<FieldDescriptorProto>(){

                @Override
                public FieldDescriptorProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new FieldDescriptorProto(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = fieldDescriptorProto = new FieldDescriptorProto(true);
            fieldDescriptorProto.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private FieldDescriptorProto(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var6_6 = UnknownFieldSet.newBuilder();
            var3_7 = false;
            do {
                block12 : {
                    if (var3_7) {
                        this.unknownFields = var6_6.build();
                        this.makeExtensionsImmutable();
                        return;
                    }
                    var4_8 = var1_1.readTag();
                    if (var4_8 == 0) break block12;
                    if (var4_8 == 10) ** GOTO lbl64
                    if (var4_8 == 18) ** GOTO lbl61
                    if (var4_8 == 24) ** GOTO lbl58
                    if (var4_8 == 32) ** GOTO lbl50
                    if (var4_8 == 40) ** GOTO lbl42
                    if (var4_8 == 50) ** GOTO lbl39
                    if (var4_8 == 58) ** GOTO lbl36
                    if (var4_8 == 66) ** GOTO lbl27
                    if (this.parseUnknownField(var1_1, var6_6, var2_5, var4_8)) continue;
                    var3_7 = true;
                    continue;
lbl27: // 1 sources:
                    var5_9 = null;
                    if ((this.bitField0_ & 128) == 128) {
                        var5_9 = this.options_.toBuilder();
                    }
                    this.options_ = var7_10 = var1_1.readMessage(FieldOptions.PARSER, var2_5);
                    if (var5_9 != null) {
                        var5_9.mergeFrom(var7_10);
                        this.options_ = var5_9.buildPartial();
                    }
                    this.bitField0_ |= 128;
                    continue;
lbl36: // 1 sources:
                    this.bitField0_ |= 64;
                    this.defaultValue_ = var1_1.readBytes();
                    continue;
lbl39: // 1 sources:
                    this.bitField0_ |= 16;
                    this.typeName_ = var1_1.readBytes();
                    continue;
lbl42: // 1 sources:
                    var4_8 = var1_1.readEnum();
                    var5_9 = Type.valueOf(var4_8);
                    if (var5_9 == null) {
                        var6_6.mergeVarintField(5, var4_8);
                        continue;
                    }
                    this.bitField0_ |= 8;
                    this.type_ = var5_9;
                    continue;
lbl50: // 1 sources:
                    var4_8 = var1_1.readEnum();
                    var5_9 = Label.valueOf(var4_8);
                    if (var5_9 == null) {
                        var6_6.mergeVarintField(4, var4_8);
                        continue;
                    }
                    this.bitField0_ = 4 | this.bitField0_;
                    this.label_ = var5_9;
                    continue;
lbl58: // 1 sources:
                    this.bitField0_ |= 2;
                    this.number_ = var1_1.readInt32();
                    continue;
lbl61: // 1 sources:
                    this.bitField0_ |= 32;
                    this.extendee_ = var1_1.readBytes();
                    continue;
lbl64: // 1 sources:
                    this.bitField0_ |= 1;
                    this.name_ = var1_1.readBytes();
                    continue;
                }
                var3_7 = true;
                continue;
                break;
            } while (true);
            catch (Throwable var1_2) {
            }
            catch (IOException var1_3) {
                throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
            }
            catch (InvalidProtocolBufferException var1_4) {
                throw var1_4.setUnfinishedMessage(this);
            }
            this.unknownFields = var6_6.build();
            this.makeExtensionsImmutable();
            throw var1_2;
        }

        private FieldDescriptorProto(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private FieldDescriptorProto(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static FieldDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_FieldDescriptorProto_descriptor;
        }

        private void initFields() {
            this.name_ = "";
            this.number_ = 0;
            this.label_ = Label.LABEL_OPTIONAL;
            this.type_ = Type.TYPE_DOUBLE;
            this.typeName_ = "";
            this.extendee_ = "";
            this.defaultValue_ = "";
            this.options_ = FieldOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FieldDescriptorProto fieldDescriptorProto) {
            return FieldDescriptorProto.newBuilder().mergeFrom(fieldDescriptorProto);
        }

        public static FieldDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static FieldDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static FieldDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static FieldDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static FieldDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static FieldDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static FieldDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static FieldDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static FieldDescriptorProto parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static FieldDescriptorProto parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public FieldDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public String getDefaultValue() {
            Object object = this.defaultValue_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.defaultValue_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getDefaultValueBytes() {
            Object object = this.defaultValue_;
            if (object instanceof String) {
                this.defaultValue_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public String getExtendee() {
            Object object = this.extendee_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.extendee_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getExtendeeBytes() {
            Object object = this.extendee_;
            if (object instanceof String) {
                this.extendee_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public Label getLabel() {
            return this.label_;
        }

        @Override
        public String getName() {
            Object object = this.name_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.name_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getNameBytes() {
            Object object = this.name_;
            if (object instanceof String) {
                this.name_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public int getNumber() {
            return this.number_;
        }

        @Override
        public FieldOptions getOptions() {
            return this.options_;
        }

        @Override
        public FieldOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public Parser<FieldDescriptorProto> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }
            n = n2;
            if ((this.bitField0_ & 32) == 32) {
                n = n2 + CodedOutputStream.computeBytesSize(2, this.getExtendeeBytes());
            }
            n2 = n;
            if ((this.bitField0_ & 2) == 2) {
                n2 = n + CodedOutputStream.computeInt32Size(3, this.number_);
            }
            n = n2;
            if ((this.bitField0_ & 4) == 4) {
                n = n2 + CodedOutputStream.computeEnumSize(4, this.label_.getNumber());
            }
            n2 = n;
            if ((this.bitField0_ & 8) == 8) {
                n2 = n + CodedOutputStream.computeEnumSize(5, this.type_.getNumber());
            }
            n = n2;
            if ((this.bitField0_ & 16) == 16) {
                n = n2 + CodedOutputStream.computeBytesSize(6, this.getTypeNameBytes());
            }
            n2 = n;
            if ((this.bitField0_ & 64) == 64) {
                n2 = n + CodedOutputStream.computeBytesSize(7, this.getDefaultValueBytes());
            }
            n = n2;
            if ((this.bitField0_ & 128) == 128) {
                n = n2 + CodedOutputStream.computeMessageSize(8, this.options_);
            }
            this.memoizedSerializedSize = n += this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public Type getType() {
            return this.type_;
        }

        @Override
        public String getTypeName() {
            Object object = this.typeName_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.typeName_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getTypeNameBytes() {
            Object object = this.typeName_;
            if (object instanceof String) {
                this.typeName_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasDefaultValue() {
            if ((this.bitField0_ & 64) == 64) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasExtendee() {
            if ((this.bitField0_ & 32) == 32) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasLabel() {
            if ((this.bitField0_ & 4) == 4) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasName() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasNumber() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOptions() {
            if ((this.bitField0_ & 128) == 128) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasType() {
            if ((this.bitField0_ & 8) == 8) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasTypeName() {
            if ((this.bitField0_ & 16) == 16) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(FieldDescriptorProto.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            byte by = this.memoizedIsInitialized;
            boolean bl = false;
            if (by != -1) {
                if (by == 1) {
                    bl = true;
                }
                return bl;
            }
            if (this.hasOptions() && !this.getOptions().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return FieldDescriptorProto.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return FieldDescriptorProto.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, this.getNameBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeBytes(2, this.getExtendeeBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeInt32(3, this.number_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeEnum(4, this.label_.getNumber());
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeEnum(5, this.type_.getNumber());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeBytes(6, this.getTypeNameBytes());
            }
            if ((this.bitField0_ & 64) == 64) {
                codedOutputStream.writeBytes(7, this.getDefaultValueBytes());
            }
            if ((this.bitField0_ & 128) == 128) {
                codedOutputStream.writeMessage(8, this.options_);
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements FieldDescriptorProtoOrBuilder {
            private int bitField0_;
            private Object defaultValue_ = "";
            private Object extendee_ = "";
            private Label label_ = Label.LABEL_OPTIONAL;
            private Object name_ = "";
            private int number_;
            private SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> optionsBuilder_;
            private FieldOptions options_ = FieldOptions.getDefaultInstance();
            private Object typeName_ = "";
            private Type type_ = Type.TYPE_DOUBLE;

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_FieldDescriptorProto_descriptor;
            }

            private SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getOptionsFieldBuilder();
                }
            }

            @Override
            public FieldDescriptorProto build() {
                FieldDescriptorProto fieldDescriptorProto = this.buildPartial();
                if (fieldDescriptorProto.isInitialized()) {
                    return fieldDescriptorProto;
                }
                throw Builder.newUninitializedMessageException(fieldDescriptorProto);
            }

            @Override
            public FieldDescriptorProto buildPartial() {
                SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> singleFieldBuilder;
                FieldDescriptorProto fieldDescriptorProto = new FieldDescriptorProto(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                fieldDescriptorProto.name_ = this.name_;
                int n3 = n2;
                if ((n & 2) == 2) {
                    n3 = n2 | 2;
                }
                fieldDescriptorProto.number_ = this.number_;
                n2 = n3;
                if ((n & 4) == 4) {
                    n2 = n3 | 4;
                }
                fieldDescriptorProto.label_ = this.label_;
                n3 = n2;
                if ((n & 8) == 8) {
                    n3 = n2 | 8;
                }
                fieldDescriptorProto.type_ = this.type_;
                n2 = n3;
                if ((n & 16) == 16) {
                    n2 = n3 | 16;
                }
                fieldDescriptorProto.typeName_ = this.typeName_;
                n3 = n2;
                if ((n & 32) == 32) {
                    n3 = n2 | 32;
                }
                fieldDescriptorProto.extendee_ = this.extendee_;
                n2 = n3;
                if ((n & 64) == 64) {
                    n2 = n3 | 64;
                }
                fieldDescriptorProto.defaultValue_ = this.defaultValue_;
                n3 = n2;
                if ((n & 128) == 128) {
                    n3 = n2 | 128;
                }
                if ((singleFieldBuilder = this.optionsBuilder_) == null) {
                    fieldDescriptorProto.options_ = this.options_;
                } else {
                    fieldDescriptorProto.options_ = singleFieldBuilder.build();
                }
                fieldDescriptorProto.bitField0_ = n3;
                this.onBuilt();
                return fieldDescriptorProto;
            }

            @Override
            public Builder clear() {
                int n;
                super.clear();
                this.name_ = "";
                this.bitField0_ = n = this.bitField0_ & -2;
                this.number_ = 0;
                this.bitField0_ = n & -3;
                this.label_ = Label.LABEL_OPTIONAL;
                this.bitField0_ &= -5;
                this.type_ = Type.TYPE_DOUBLE;
                this.bitField0_ = n = this.bitField0_ & -9;
                this.typeName_ = "";
                this.bitField0_ = n &= -17;
                this.extendee_ = "";
                this.bitField0_ = n &= -33;
                this.defaultValue_ = "";
                this.bitField0_ = n & -65;
                SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = FieldOptions.getDefaultInstance();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -129;
                return this;
            }

            public Builder clearDefaultValue() {
                this.bitField0_ &= -65;
                this.defaultValue_ = FieldDescriptorProto.getDefaultInstance().getDefaultValue();
                this.onChanged();
                return this;
            }

            public Builder clearExtendee() {
                this.bitField0_ &= -33;
                this.extendee_ = FieldDescriptorProto.getDefaultInstance().getExtendee();
                this.onChanged();
                return this;
            }

            public Builder clearLabel() {
                this.bitField0_ &= -5;
                this.label_ = Label.LABEL_OPTIONAL;
                this.onChanged();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = FieldDescriptorProto.getDefaultInstance().getName();
                this.onChanged();
                return this;
            }

            public Builder clearNumber() {
                this.bitField0_ &= -3;
                this.number_ = 0;
                this.onChanged();
                return this;
            }

            public Builder clearOptions() {
                SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = FieldOptions.getDefaultInstance();
                    this.onChanged();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -129;
                return this;
            }

            public Builder clearType() {
                this.bitField0_ &= -9;
                this.type_ = Type.TYPE_DOUBLE;
                this.onChanged();
                return this;
            }

            public Builder clearTypeName() {
                this.bitField0_ &= -17;
                this.typeName_ = FieldDescriptorProto.getDefaultInstance().getTypeName();
                this.onChanged();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public FieldDescriptorProto getDefaultInstanceForType() {
                return FieldDescriptorProto.getDefaultInstance();
            }

            @Override
            public String getDefaultValue() {
                Object object = this.defaultValue_;
                if (!(object instanceof String)) {
                    this.defaultValue_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getDefaultValueBytes() {
                Object object = this.defaultValue_;
                if (object instanceof String) {
                    this.defaultValue_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_FieldDescriptorProto_descriptor;
            }

            @Override
            public String getExtendee() {
                Object object = this.extendee_;
                if (!(object instanceof String)) {
                    this.extendee_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getExtendeeBytes() {
                Object object = this.extendee_;
                if (object instanceof String) {
                    this.extendee_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public Label getLabel() {
                return this.label_;
            }

            @Override
            public String getName() {
                Object object = this.name_;
                if (!(object instanceof String)) {
                    this.name_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getNameBytes() {
                Object object = this.name_;
                if (object instanceof String) {
                    this.name_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public int getNumber() {
                return this.number_;
            }

            @Override
            public FieldOptions getOptions() {
                SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    return this.options_;
                }
                return singleFieldBuilder.getMessage();
            }

            public FieldOptions.Builder getOptionsBuilder() {
                this.bitField0_ |= 128;
                this.onChanged();
                return this.getOptionsFieldBuilder().getBuilder();
            }

            @Override
            public FieldOptionsOrBuilder getOptionsOrBuilder() {
                SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder != null) {
                    return singleFieldBuilder.getMessageOrBuilder();
                }
                return this.options_;
            }

            @Override
            public Type getType() {
                return this.type_;
            }

            @Override
            public String getTypeName() {
                Object object = this.typeName_;
                if (!(object instanceof String)) {
                    this.typeName_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getTypeNameBytes() {
                Object object = this.typeName_;
                if (object instanceof String) {
                    this.typeName_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public boolean hasDefaultValue() {
                if ((this.bitField0_ & 64) == 64) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasExtendee() {
                if ((this.bitField0_ & 32) == 32) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasLabel() {
                if ((this.bitField0_ & 4) == 4) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasName() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasNumber() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOptions() {
                if ((this.bitField0_ & 128) == 128) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasType() {
                if ((this.bitField0_ & 8) == 8) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasTypeName() {
                if ((this.bitField0_ & 16) == 16) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(FieldDescriptorProto.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                if (this.hasOptions() && !this.getOptions().isInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = FieldDescriptorProto.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((FieldDescriptorProto)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (FieldDescriptorProto)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((FieldDescriptorProto)object3);
                throw throwable222;
            }

            public Builder mergeFrom(FieldDescriptorProto fieldDescriptorProto) {
                if (fieldDescriptorProto == FieldDescriptorProto.getDefaultInstance()) {
                    return this;
                }
                if (fieldDescriptorProto.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = fieldDescriptorProto.name_;
                    this.onChanged();
                }
                if (fieldDescriptorProto.hasNumber()) {
                    this.setNumber(fieldDescriptorProto.getNumber());
                }
                if (fieldDescriptorProto.hasLabel()) {
                    this.setLabel(fieldDescriptorProto.getLabel());
                }
                if (fieldDescriptorProto.hasType()) {
                    this.setType(fieldDescriptorProto.getType());
                }
                if (fieldDescriptorProto.hasTypeName()) {
                    this.bitField0_ |= 16;
                    this.typeName_ = fieldDescriptorProto.typeName_;
                    this.onChanged();
                }
                if (fieldDescriptorProto.hasExtendee()) {
                    this.bitField0_ |= 32;
                    this.extendee_ = fieldDescriptorProto.extendee_;
                    this.onChanged();
                }
                if (fieldDescriptorProto.hasDefaultValue()) {
                    this.bitField0_ |= 64;
                    this.defaultValue_ = fieldDescriptorProto.defaultValue_;
                    this.onChanged();
                }
                if (fieldDescriptorProto.hasOptions()) {
                    this.mergeOptions(fieldDescriptorProto.getOptions());
                }
                this.mergeUnknownFields(fieldDescriptorProto.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof FieldDescriptorProto) {
                    return this.mergeFrom((FieldDescriptorProto)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(FieldOptions fieldOptions) {
                SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = (this.bitField0_ & 128) == 128 && this.options_ != FieldOptions.getDefaultInstance() ? FieldOptions.newBuilder(this.options_).mergeFrom(fieldOptions).buildPartial() : fieldOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.mergeFrom(fieldOptions);
                }
                this.bitField0_ |= 128;
                return this;
            }

            public Builder setDefaultValue(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 64;
                    this.defaultValue_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setDefaultValueBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 64;
                    this.defaultValue_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setExtendee(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 32;
                    this.extendee_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setExtendeeBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 32;
                    this.extendee_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setLabel(Label label) {
                if (label != null) {
                    this.bitField0_ |= 4;
                    this.label_ = label;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setName(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 1;
                    this.name_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 1;
                    this.name_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNumber(int n) {
                this.bitField0_ |= 2;
                this.number_ = n;
                this.onChanged();
                return this;
            }

            public Builder setOptions(FieldOptions.Builder builder) {
                SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = builder.build();
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(builder.build());
                }
                this.bitField0_ |= 128;
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Builder setOptions(FieldOptions fieldOptions) {
                SingleFieldBuilder<FieldOptions, FieldOptions.Builder, FieldOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    if (fieldOptions == null) throw null;
                    this.options_ = fieldOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(fieldOptions);
                }
                this.bitField0_ |= 128;
                return this;
            }

            public Builder setType(Type type) {
                if (type != null) {
                    this.bitField0_ |= 8;
                    this.type_ = type;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setTypeName(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 16;
                    this.typeName_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setTypeNameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 16;
                    this.typeName_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }
        }

        public static final class Label
        extends Enum<Label>
        implements ProtocolMessageEnum {
            private static final /* synthetic */ Label[] $VALUES;
            public static final /* enum */ Label LABEL_OPTIONAL;
            public static final int LABEL_OPTIONAL_VALUE = 1;
            public static final /* enum */ Label LABEL_REPEATED;
            public static final int LABEL_REPEATED_VALUE = 3;
            public static final /* enum */ Label LABEL_REQUIRED;
            public static final int LABEL_REQUIRED_VALUE = 2;
            private static final Label[] VALUES;
            private static Internal.EnumLiteMap<Label> internalValueMap;
            private final int index;
            private final int value;

            static {
                Label label;
                LABEL_OPTIONAL = new Label(0, 1);
                LABEL_REQUIRED = new Label(1, 2);
                LABEL_REPEATED = label = new Label(2, 3);
                $VALUES = new Label[]{LABEL_OPTIONAL, LABEL_REQUIRED, label};
                internalValueMap = new Internal.EnumLiteMap<Label>(){

                    @Override
                    public Label findValueByNumber(int n) {
                        return Label.valueOf(n);
                    }
                };
                VALUES = Label.values();
            }

            private Label(int n2, int n3) {
                this.index = n2;
                this.value = n3;
            }

            public static final Descriptors.EnumDescriptor getDescriptor() {
                return FieldDescriptorProto.getDescriptor().getEnumTypes().get(1);
            }

            public static Internal.EnumLiteMap<Label> internalGetValueMap() {
                return internalValueMap;
            }

            public static Label valueOf(int n) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            return null;
                        }
                        return LABEL_REPEATED;
                    }
                    return LABEL_REQUIRED;
                }
                return LABEL_OPTIONAL;
            }

            public static Label valueOf(Descriptors.EnumValueDescriptor enumValueDescriptor) {
                if (enumValueDescriptor.getType() == Label.getDescriptor()) {
                    return VALUES[enumValueDescriptor.getIndex()];
                }
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }

            public static Label valueOf(String string2) {
                return Enum.valueOf(Label.class, string2);
            }

            public static Label[] values() {
                return (Label[])$VALUES.clone();
            }

            @Override
            public final Descriptors.EnumDescriptor getDescriptorForType() {
                return Label.getDescriptor();
            }

            @Override
            public final int getNumber() {
                return this.value;
            }

            @Override
            public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                return Label.getDescriptor().getValues().get(this.index);
            }

        }

        public static final class Type
        extends Enum<Type>
        implements ProtocolMessageEnum {
            private static final /* synthetic */ Type[] $VALUES;
            public static final /* enum */ Type TYPE_BOOL;
            public static final int TYPE_BOOL_VALUE = 8;
            public static final /* enum */ Type TYPE_BYTES;
            public static final int TYPE_BYTES_VALUE = 12;
            public static final /* enum */ Type TYPE_DOUBLE;
            public static final int TYPE_DOUBLE_VALUE = 1;
            public static final /* enum */ Type TYPE_ENUM;
            public static final int TYPE_ENUM_VALUE = 14;
            public static final /* enum */ Type TYPE_FIXED32;
            public static final int TYPE_FIXED32_VALUE = 7;
            public static final /* enum */ Type TYPE_FIXED64;
            public static final int TYPE_FIXED64_VALUE = 6;
            public static final /* enum */ Type TYPE_FLOAT;
            public static final int TYPE_FLOAT_VALUE = 2;
            public static final /* enum */ Type TYPE_GROUP;
            public static final int TYPE_GROUP_VALUE = 10;
            public static final /* enum */ Type TYPE_INT32;
            public static final int TYPE_INT32_VALUE = 5;
            public static final /* enum */ Type TYPE_INT64;
            public static final int TYPE_INT64_VALUE = 3;
            public static final /* enum */ Type TYPE_MESSAGE;
            public static final int TYPE_MESSAGE_VALUE = 11;
            public static final /* enum */ Type TYPE_SFIXED32;
            public static final int TYPE_SFIXED32_VALUE = 15;
            public static final /* enum */ Type TYPE_SFIXED64;
            public static final int TYPE_SFIXED64_VALUE = 16;
            public static final /* enum */ Type TYPE_SINT32;
            public static final int TYPE_SINT32_VALUE = 17;
            public static final /* enum */ Type TYPE_SINT64;
            public static final int TYPE_SINT64_VALUE = 18;
            public static final /* enum */ Type TYPE_STRING;
            public static final int TYPE_STRING_VALUE = 9;
            public static final /* enum */ Type TYPE_UINT32;
            public static final int TYPE_UINT32_VALUE = 13;
            public static final /* enum */ Type TYPE_UINT64;
            public static final int TYPE_UINT64_VALUE = 4;
            private static final Type[] VALUES;
            private static Internal.EnumLiteMap<Type> internalValueMap;
            private final int index;
            private final int value;

            static {
                Type type;
                TYPE_DOUBLE = new Type(0, 1);
                TYPE_FLOAT = new Type(1, 2);
                TYPE_INT64 = new Type(2, 3);
                TYPE_UINT64 = new Type(3, 4);
                TYPE_INT32 = new Type(4, 5);
                TYPE_FIXED64 = new Type(5, 6);
                TYPE_FIXED32 = new Type(6, 7);
                TYPE_BOOL = new Type(7, 8);
                TYPE_STRING = new Type(8, 9);
                TYPE_GROUP = new Type(9, 10);
                TYPE_MESSAGE = new Type(10, 11);
                TYPE_BYTES = new Type(11, 12);
                TYPE_UINT32 = new Type(12, 13);
                TYPE_ENUM = new Type(13, 14);
                TYPE_SFIXED32 = new Type(14, 15);
                TYPE_SFIXED64 = new Type(15, 16);
                TYPE_SINT32 = new Type(16, 17);
                TYPE_SINT64 = type = new Type(17, 18);
                $VALUES = new Type[]{TYPE_DOUBLE, TYPE_FLOAT, TYPE_INT64, TYPE_UINT64, TYPE_INT32, TYPE_FIXED64, TYPE_FIXED32, TYPE_BOOL, TYPE_STRING, TYPE_GROUP, TYPE_MESSAGE, TYPE_BYTES, TYPE_UINT32, TYPE_ENUM, TYPE_SFIXED32, TYPE_SFIXED64, TYPE_SINT32, type};
                internalValueMap = new Internal.EnumLiteMap<Type>(){

                    @Override
                    public Type findValueByNumber(int n) {
                        return Type.valueOf(n);
                    }
                };
                VALUES = Type.values();
            }

            private Type(int n2, int n3) {
                this.index = n2;
                this.value = n3;
            }

            public static final Descriptors.EnumDescriptor getDescriptor() {
                return FieldDescriptorProto.getDescriptor().getEnumTypes().get(0);
            }

            public static Internal.EnumLiteMap<Type> internalGetValueMap() {
                return internalValueMap;
            }

            public static Type valueOf(int n) {
                switch (n) {
                    default: {
                        return null;
                    }
                    case 18: {
                        return TYPE_SINT64;
                    }
                    case 17: {
                        return TYPE_SINT32;
                    }
                    case 16: {
                        return TYPE_SFIXED64;
                    }
                    case 15: {
                        return TYPE_SFIXED32;
                    }
                    case 14: {
                        return TYPE_ENUM;
                    }
                    case 13: {
                        return TYPE_UINT32;
                    }
                    case 12: {
                        return TYPE_BYTES;
                    }
                    case 11: {
                        return TYPE_MESSAGE;
                    }
                    case 10: {
                        return TYPE_GROUP;
                    }
                    case 9: {
                        return TYPE_STRING;
                    }
                    case 8: {
                        return TYPE_BOOL;
                    }
                    case 7: {
                        return TYPE_FIXED32;
                    }
                    case 6: {
                        return TYPE_FIXED64;
                    }
                    case 5: {
                        return TYPE_INT32;
                    }
                    case 4: {
                        return TYPE_UINT64;
                    }
                    case 3: {
                        return TYPE_INT64;
                    }
                    case 2: {
                        return TYPE_FLOAT;
                    }
                    case 1: 
                }
                return TYPE_DOUBLE;
            }

            public static Type valueOf(Descriptors.EnumValueDescriptor enumValueDescriptor) {
                if (enumValueDescriptor.getType() == Type.getDescriptor()) {
                    return VALUES[enumValueDescriptor.getIndex()];
                }
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }

            public static Type valueOf(String string2) {
                return Enum.valueOf(Type.class, string2);
            }

            public static Type[] values() {
                return (Type[])$VALUES.clone();
            }

            @Override
            public final Descriptors.EnumDescriptor getDescriptorForType() {
                return Type.getDescriptor();
            }

            @Override
            public final int getNumber() {
                return this.value;
            }

            @Override
            public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                return Type.getDescriptor().getValues().get(this.index);
            }

        }

    }

    public static interface FieldDescriptorProtoOrBuilder
    extends MessageOrBuilder {
        public String getDefaultValue();

        public ByteString getDefaultValueBytes();

        public String getExtendee();

        public ByteString getExtendeeBytes();

        public FieldDescriptorProto.Label getLabel();

        public String getName();

        public ByteString getNameBytes();

        public int getNumber();

        public FieldOptions getOptions();

        public FieldOptionsOrBuilder getOptionsOrBuilder();

        public FieldDescriptorProto.Type getType();

        public String getTypeName();

        public ByteString getTypeNameBytes();

        public boolean hasDefaultValue();

        public boolean hasExtendee();

        public boolean hasLabel();

        public boolean hasName();

        public boolean hasNumber();

        public boolean hasOptions();

        public boolean hasType();

        public boolean hasTypeName();
    }

    public static final class FieldOptions
    extends GeneratedMessage.ExtendableMessage<FieldOptions>
    implements FieldOptionsOrBuilder {
        public static final int CTYPE_FIELD_NUMBER = 1;
        public static final int DEPRECATED_FIELD_NUMBER = 3;
        public static final int EXPERIMENTAL_MAP_KEY_FIELD_NUMBER = 9;
        public static final int LAZY_FIELD_NUMBER = 5;
        public static final int PACKED_FIELD_NUMBER = 2;
        public static Parser<FieldOptions> PARSER;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        public static final int WEAK_FIELD_NUMBER = 10;
        private static final FieldOptions defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private CType ctype_;
        private boolean deprecated_;
        private Object experimentalMapKey_;
        private boolean lazy_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private boolean packed_;
        private List<UninterpretedOption> uninterpretedOption_;
        private final UnknownFieldSet unknownFields;
        private boolean weak_;

        static {
            FieldOptions fieldOptions;
            PARSER = new AbstractParser<FieldOptions>(){

                @Override
                public FieldOptions parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new FieldOptions(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = fieldOptions = new FieldOptions(true);
            fieldOptions.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private FieldOptions(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = 0;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = 0;
            while (var8_8 == 0) {
                block14 : {
                    block15 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl131
                        if (var10_14 == 8) ** GOTO lbl104
                        if (var10_14 == 16) ** GOTO lbl93
                        if (var10_14 == 24) ** GOTO lbl82
                        if (var10_14 == 40) ** GOTO lbl71
                        if (var10_14 == 74) ** GOTO lbl60
                        if (var10_14 == 80) ** GOTO lbl49
                        if (var10_14 == 7994) break block15;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                            var4_9 = 1;
                            var9_13 = var3_6;
                        }
                        ** GOTO lbl133
                    }
                    var4_9 = var3_6;
                    if ((var3_6 & 64) != 64) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.uninterpretedOption_ = new ArrayList<UninterpretedOption>();
                        var4_9 = var3_6 | 64;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.uninterpretedOption_.add(var1_1.readMessage(UninterpretedOption.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl49: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 32;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.weak_ = var1_1.readBool();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl60: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 16;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.experimentalMapKey_ = var1_1.readBytes();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl71: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 4;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.lazy_ = var1_1.readBool();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl82: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ = 8 | this.bitField0_;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.deprecated_ = var1_1.readBool();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl93: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 2;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.packed_ = var1_1.readBool();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl104: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var4_9 = var1_1.readEnum();
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var12_15 = CType.valueOf(var4_9);
                        if (var12_15 == null) {
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            var11_7.mergeVarintField(1, var4_9);
                            var9_13 = var3_6;
                            var4_9 = var8_8;
                        }
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ = 1 | this.bitField0_;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.ctype_ = var12_15;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl131: // 1 sources:
                        var4_9 = 1;
                        var9_13 = var3_6;
lbl133: // 10 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block14;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if ((var5_10 & 64) == 64) {
                    this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if ((var3_6 & 64) == 64) {
                this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private FieldOptions(GeneratedMessage.ExtendableBuilder<FieldOptions, ?> extendableBuilder) {
            super(extendableBuilder);
            this.unknownFields = extendableBuilder.getUnknownFields();
        }

        private FieldOptions(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static FieldOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_FieldOptions_descriptor;
        }

        private void initFields() {
            this.ctype_ = CType.STRING;
            this.packed_ = false;
            this.lazy_ = false;
            this.deprecated_ = false;
            this.experimentalMapKey_ = "";
            this.weak_ = false;
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FieldOptions fieldOptions) {
            return FieldOptions.newBuilder().mergeFrom(fieldOptions);
        }

        public static FieldOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static FieldOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static FieldOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static FieldOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static FieldOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static FieldOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static FieldOptions parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static FieldOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static FieldOptions parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static FieldOptions parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public CType getCtype() {
            return this.ctype_;
        }

        @Override
        public FieldOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public boolean getDeprecated() {
            return this.deprecated_;
        }

        @Override
        public String getExperimentalMapKey() {
            Object object = this.experimentalMapKey_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.experimentalMapKey_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getExperimentalMapKeyBytes() {
            Object object = this.experimentalMapKey_;
            if (object instanceof String) {
                this.experimentalMapKey_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public boolean getLazy() {
            return this.lazy_;
        }

        @Override
        public boolean getPacked() {
            return this.packed_;
        }

        public Parser<FieldOptions> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeEnumSize(1, this.ctype_.getNumber());
            }
            n = n2;
            if ((this.bitField0_ & 2) == 2) {
                n = n2 + CodedOutputStream.computeBoolSize(2, this.packed_);
            }
            n2 = n;
            if ((this.bitField0_ & 8) == 8) {
                n2 = n + CodedOutputStream.computeBoolSize(3, this.deprecated_);
            }
            n = n2;
            if ((this.bitField0_ & 4) == 4) {
                n = n2 + CodedOutputStream.computeBoolSize(5, this.lazy_);
            }
            n2 = n;
            if ((this.bitField0_ & 16) == 16) {
                n2 = n + CodedOutputStream.computeBytesSize(9, this.getExperimentalMapKeyBytes());
            }
            n = n2;
            if ((this.bitField0_ & 32) == 32) {
                n = n2 + CodedOutputStream.computeBoolSize(10, this.weak_);
            }
            for (n2 = 0; n2 < this.uninterpretedOption_.size(); ++n2) {
                n += CodedOutputStream.computeMessageSize(999, this.uninterpretedOption_.get(n2));
            }
            this.memoizedSerializedSize = n = n + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public UninterpretedOption getUninterpretedOption(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        @Override
        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        @Override
        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean getWeak() {
            return this.weak_;
        }

        @Override
        public boolean hasCtype() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasDeprecated() {
            if ((this.bitField0_ & 8) == 8) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasExperimentalMapKey() {
            if ((this.bitField0_ & 16) == 16) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasLazy() {
            if ((this.bitField0_ & 4) == 4) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasPacked() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasWeak() {
            if ((this.bitField0_ & 32) == 32) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_FieldOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(FieldOptions.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getUninterpretedOptionCount(); ++n) {
                if (this.getUninterpretedOption(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.extensionsAreInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return FieldOptions.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return FieldOptions.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            GeneratedMessage.ExtendableMessage.ExtensionWriter extensionWriter = this.newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeEnum(1, this.ctype_.getNumber());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBool(2, this.packed_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBool(3, this.deprecated_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBool(5, this.lazy_);
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeBytes(9, this.getExperimentalMapKeyBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeBool(10, this.weak_);
            }
            for (int i = 0; i < this.uninterpretedOption_.size(); ++i) {
                codedOutputStream.writeMessage(999, this.uninterpretedOption_.get(i));
            }
            extensionWriter.writeUntil(536870912, codedOutputStream);
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.ExtendableBuilder<FieldOptions, Builder>
        implements FieldOptionsOrBuilder {
            private int bitField0_;
            private CType ctype_ = CType.STRING;
            private boolean deprecated_;
            private Object experimentalMapKey_ = "";
            private boolean lazy_;
            private boolean packed_;
            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_ = Collections.emptyList();
            private boolean weak_;

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 64) != 64) {
                    this.uninterpretedOption_ = new ArrayList<UninterpretedOption>(this.uninterpretedOption_);
                    this.bitField0_ |= 64;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_FieldOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    List<UninterpretedOption> list = this.uninterpretedOption_;
                    boolean bl = (this.bitField0_ & 64) == 64;
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    GeneratedMessage.ExtendableBuilder.addAll(iterable, this.uninterpretedOption_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, uninterpretedOption);
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(uninterpretedOption);
                return this;
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder() {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(n, UninterpretedOption.getDefaultInstance());
            }

            @Override
            public FieldOptions build() {
                FieldOptions fieldOptions = this.buildPartial();
                if (fieldOptions.isInitialized()) {
                    return fieldOptions;
                }
                throw Builder.newUninitializedMessageException(fieldOptions);
            }

            @Override
            public FieldOptions buildPartial() {
                FieldOptions fieldOptions = new FieldOptions(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                fieldOptions.ctype_ = this.ctype_;
                int n3 = n2;
                if ((n & 2) == 2) {
                    n3 = n2 | 2;
                }
                fieldOptions.packed_ = this.packed_;
                n2 = n3;
                if ((n & 4) == 4) {
                    n2 = n3 | 4;
                }
                fieldOptions.lazy_ = this.lazy_;
                n3 = n2;
                if ((n & 8) == 8) {
                    n3 = n2 | 8;
                }
                fieldOptions.deprecated_ = this.deprecated_;
                n2 = n3;
                if ((n & 16) == 16) {
                    n2 = n3 | 16;
                }
                fieldOptions.experimentalMapKey_ = this.experimentalMapKey_;
                n3 = n2;
                if ((n & 32) == 32) {
                    n3 = n2 | 32;
                }
                fieldOptions.weak_ = this.weak_;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 64) == 64) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -65;
                    }
                    fieldOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    fieldOptions.uninterpretedOption_ = repeatedFieldBuilder.build();
                }
                fieldOptions.bitField0_ = n3;
                this.onBuilt();
                return fieldOptions;
            }

            @Override
            public Builder clear() {
                int n;
                super.clear();
                this.ctype_ = CType.STRING;
                this.bitField0_ = n = this.bitField0_ & -2;
                this.packed_ = false;
                this.bitField0_ = n &= -3;
                this.lazy_ = false;
                this.bitField0_ = n &= -5;
                this.deprecated_ = false;
                this.bitField0_ = n &= -9;
                this.experimentalMapKey_ = "";
                this.bitField0_ = n &= -17;
                this.weak_ = false;
                this.bitField0_ = n & -33;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -65;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearCtype() {
                this.bitField0_ &= -2;
                this.ctype_ = CType.STRING;
                this.onChanged();
                return this;
            }

            public Builder clearDeprecated() {
                this.bitField0_ &= -9;
                this.deprecated_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearExperimentalMapKey() {
                this.bitField0_ &= -17;
                this.experimentalMapKey_ = FieldOptions.getDefaultInstance().getExperimentalMapKey();
                this.onChanged();
                return this;
            }

            public Builder clearLazy() {
                this.bitField0_ &= -5;
                this.lazy_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearPacked() {
                this.bitField0_ &= -3;
                this.packed_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearUninterpretedOption() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -65;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearWeak() {
                this.bitField0_ &= -33;
                this.weak_ = false;
                this.onChanged();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public CType getCtype() {
                return this.ctype_;
            }

            @Override
            public FieldOptions getDefaultInstanceForType() {
                return FieldOptions.getDefaultInstance();
            }

            @Override
            public boolean getDeprecated() {
                return this.deprecated_;
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_FieldOptions_descriptor;
            }

            @Override
            public String getExperimentalMapKey() {
                Object object = this.experimentalMapKey_;
                if (!(object instanceof String)) {
                    this.experimentalMapKey_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getExperimentalMapKeyBytes() {
                Object object = this.experimentalMapKey_;
                if (object instanceof String) {
                    this.experimentalMapKey_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public boolean getLazy() {
                return this.lazy_;
            }

            @Override
            public boolean getPacked() {
                return this.packed_;
            }

            @Override
            public UninterpretedOption getUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public UninterpretedOption.Builder getUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().getBuilder(n);
            }

            public List<UninterpretedOption.Builder> getUninterpretedOptionBuilderList() {
                return this.getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            @Override
            public int getUninterpretedOptionCount() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<UninterpretedOption> getUninterpretedOptionList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.uninterpretedOption_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.uninterpretedOption_);
            }

            @Override
            public boolean getWeak() {
                return this.weak_;
            }

            @Override
            public boolean hasCtype() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasDeprecated() {
                if ((this.bitField0_ & 8) == 8) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasExperimentalMapKey() {
                if ((this.bitField0_ & 16) == 16) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasLazy() {
                if ((this.bitField0_ & 4) == 4) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasPacked() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasWeak() {
                if ((this.bitField0_ & 32) == 32) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_FieldOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(FieldOptions.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getUninterpretedOptionCount(); ++i) {
                    if (this.getUninterpretedOption(i).isInitialized()) continue;
                    return false;
                }
                if (!this.extensionsAreInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = FieldOptions.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((FieldOptions)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (FieldOptions)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((FieldOptions)object3);
                throw throwable222;
            }

            public Builder mergeFrom(FieldOptions fieldOptions) {
                if (fieldOptions == FieldOptions.getDefaultInstance()) {
                    return this;
                }
                if (fieldOptions.hasCtype()) {
                    this.setCtype(fieldOptions.getCtype());
                }
                if (fieldOptions.hasPacked()) {
                    this.setPacked(fieldOptions.getPacked());
                }
                if (fieldOptions.hasLazy()) {
                    this.setLazy(fieldOptions.getLazy());
                }
                if (fieldOptions.hasDeprecated()) {
                    this.setDeprecated(fieldOptions.getDeprecated());
                }
                if (fieldOptions.hasExperimentalMapKey()) {
                    this.bitField0_ |= 16;
                    this.experimentalMapKey_ = fieldOptions.experimentalMapKey_;
                    this.onChanged();
                }
                if (fieldOptions.hasWeak()) {
                    this.setWeak(fieldOptions.getWeak());
                }
                if (this.uninterpretedOptionBuilder_ == null) {
                    if (!fieldOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOption_.isEmpty()) {
                            this.uninterpretedOption_ = fieldOptions.uninterpretedOption_;
                            this.bitField0_ &= -65;
                        } else {
                            this.ensureUninterpretedOptionIsMutable();
                            this.uninterpretedOption_.addAll(fieldOptions.uninterpretedOption_);
                        }
                        this.onChanged();
                    }
                } else if (!fieldOptions.uninterpretedOption_.isEmpty()) {
                    if (this.uninterpretedOptionBuilder_.isEmpty()) {
                        this.uninterpretedOptionBuilder_.dispose();
                        RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = null;
                        this.uninterpretedOptionBuilder_ = null;
                        this.uninterpretedOption_ = fieldOptions.uninterpretedOption_;
                        this.bitField0_ &= -65;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getUninterpretedOptionFieldBuilder();
                        }
                        this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.uninterpretedOptionBuilder_.addAllMessages(fieldOptions.uninterpretedOption_);
                    }
                }
                this.mergeExtensionFields(fieldOptions);
                this.mergeUnknownFields(fieldOptions.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof FieldOptions) {
                    return this.mergeFrom((FieldOptions)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setCtype(CType cType) {
                if (cType != null) {
                    this.bitField0_ |= 1;
                    this.ctype_ = cType;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setDeprecated(boolean bl) {
                this.bitField0_ |= 8;
                this.deprecated_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setExperimentalMapKey(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 16;
                    this.experimentalMapKey_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setExperimentalMapKeyBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 16;
                    this.experimentalMapKey_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setLazy(boolean bl) {
                this.bitField0_ |= 4;
                this.lazy_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setPacked(boolean bl) {
                this.bitField0_ |= 2;
                this.packed_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.set(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, uninterpretedOption);
                return this;
            }

            public Builder setWeak(boolean bl) {
                this.bitField0_ |= 32;
                this.weak_ = bl;
                this.onChanged();
                return this;
            }
        }

        public static final class CType
        extends Enum<CType>
        implements ProtocolMessageEnum {
            private static final /* synthetic */ CType[] $VALUES;
            public static final /* enum */ CType CORD;
            public static final int CORD_VALUE = 1;
            public static final /* enum */ CType STRING;
            public static final /* enum */ CType STRING_PIECE;
            public static final int STRING_PIECE_VALUE = 2;
            public static final int STRING_VALUE = 0;
            private static final CType[] VALUES;
            private static Internal.EnumLiteMap<CType> internalValueMap;
            private final int index;
            private final int value;

            static {
                CType cType;
                STRING = new CType(0, 0);
                CORD = new CType(1, 1);
                STRING_PIECE = cType = new CType(2, 2);
                $VALUES = new CType[]{STRING, CORD, cType};
                internalValueMap = new Internal.EnumLiteMap<CType>(){

                    @Override
                    public CType findValueByNumber(int n) {
                        return CType.valueOf(n);
                    }
                };
                VALUES = CType.values();
            }

            private CType(int n2, int n3) {
                this.index = n2;
                this.value = n3;
            }

            public static final Descriptors.EnumDescriptor getDescriptor() {
                return FieldOptions.getDescriptor().getEnumTypes().get(0);
            }

            public static Internal.EnumLiteMap<CType> internalGetValueMap() {
                return internalValueMap;
            }

            public static CType valueOf(int n) {
                if (n != 0) {
                    if (n != 1) {
                        if (n != 2) {
                            return null;
                        }
                        return STRING_PIECE;
                    }
                    return CORD;
                }
                return STRING;
            }

            public static CType valueOf(Descriptors.EnumValueDescriptor enumValueDescriptor) {
                if (enumValueDescriptor.getType() == CType.getDescriptor()) {
                    return VALUES[enumValueDescriptor.getIndex()];
                }
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }

            public static CType valueOf(String string2) {
                return Enum.valueOf(CType.class, string2);
            }

            public static CType[] values() {
                return (CType[])$VALUES.clone();
            }

            @Override
            public final Descriptors.EnumDescriptor getDescriptorForType() {
                return CType.getDescriptor();
            }

            @Override
            public final int getNumber() {
                return this.value;
            }

            @Override
            public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                return CType.getDescriptor().getValues().get(this.index);
            }

        }

    }

    public static interface FieldOptionsOrBuilder
    extends GeneratedMessage.ExtendableMessageOrBuilder<FieldOptions> {
        public FieldOptions.CType getCtype();

        public boolean getDeprecated();

        public String getExperimentalMapKey();

        public ByteString getExperimentalMapKeyBytes();

        public boolean getLazy();

        public boolean getPacked();

        public UninterpretedOption getUninterpretedOption(int var1);

        public int getUninterpretedOptionCount();

        public List<UninterpretedOption> getUninterpretedOptionList();

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();

        public boolean getWeak();

        public boolean hasCtype();

        public boolean hasDeprecated();

        public boolean hasExperimentalMapKey();

        public boolean hasLazy();

        public boolean hasPacked();

        public boolean hasWeak();
    }

    public static final class FileDescriptorProto
    extends GeneratedMessage
    implements FileDescriptorProtoOrBuilder {
        public static final int DEPENDENCY_FIELD_NUMBER = 3;
        public static final int ENUM_TYPE_FIELD_NUMBER = 5;
        public static final int EXTENSION_FIELD_NUMBER = 7;
        public static final int MESSAGE_TYPE_FIELD_NUMBER = 4;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int OPTIONS_FIELD_NUMBER = 8;
        public static final int PACKAGE_FIELD_NUMBER = 2;
        public static Parser<FileDescriptorProto> PARSER;
        public static final int PUBLIC_DEPENDENCY_FIELD_NUMBER = 10;
        public static final int SERVICE_FIELD_NUMBER = 6;
        public static final int SOURCE_CODE_INFO_FIELD_NUMBER = 9;
        public static final int WEAK_DEPENDENCY_FIELD_NUMBER = 11;
        private static final FileDescriptorProto defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private LazyStringList dependency_;
        private List<EnumDescriptorProto> enumType_;
        private List<FieldDescriptorProto> extension_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private List<DescriptorProto> messageType_;
        private Object name_;
        private FileOptions options_;
        private Object package_;
        private List<Integer> publicDependency_;
        private List<ServiceDescriptorProto> service_;
        private SourceCodeInfo sourceCodeInfo_;
        private final UnknownFieldSet unknownFields;
        private List<Integer> weakDependency_;

        static {
            FileDescriptorProto fileDescriptorProto;
            PARSER = new AbstractParser<FileDescriptorProto>(){

                @Override
                public FileDescriptorProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new FileDescriptorProto(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = fileDescriptorProto = new FileDescriptorProto(true);
            fileDescriptorProto.initFields();
        }

        /*
         * Exception decompiling
         */
        private FileDescriptorProto(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: First case is not immediately after switch.
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:366)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
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

        private FileDescriptorProto(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte)-1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private FileDescriptorProto(boolean bl) {
            this.memoizedIsInitialized = (byte)-1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static FileDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_FileDescriptorProto_descriptor;
        }

        private void initFields() {
            this.name_ = "";
            this.package_ = "";
            this.dependency_ = LazyStringArrayList.EMPTY;
            this.publicDependency_ = Collections.emptyList();
            this.weakDependency_ = Collections.emptyList();
            this.messageType_ = Collections.emptyList();
            this.enumType_ = Collections.emptyList();
            this.service_ = Collections.emptyList();
            this.extension_ = Collections.emptyList();
            this.options_ = FileOptions.getDefaultInstance();
            this.sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FileDescriptorProto fileDescriptorProto) {
            return FileDescriptorProto.newBuilder().mergeFrom(fileDescriptorProto);
        }

        public static FileDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static FileDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static FileDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static FileDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static FileDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static FileDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static FileDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static FileDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static FileDescriptorProto parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static FileDescriptorProto parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public FileDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public String getDependency(int n) {
            return (String)this.dependency_.get(n);
        }

        @Override
        public ByteString getDependencyBytes(int n) {
            return this.dependency_.getByteString(n);
        }

        @Override
        public int getDependencyCount() {
            return this.dependency_.size();
        }

        @Override
        public List<String> getDependencyList() {
            return this.dependency_;
        }

        @Override
        public EnumDescriptorProto getEnumType(int n) {
            return this.enumType_.get(n);
        }

        @Override
        public int getEnumTypeCount() {
            return this.enumType_.size();
        }

        @Override
        public List<EnumDescriptorProto> getEnumTypeList() {
            return this.enumType_;
        }

        @Override
        public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int n) {
            return this.enumType_.get(n);
        }

        @Override
        public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList() {
            return this.enumType_;
        }

        @Override
        public FieldDescriptorProto getExtension(int n) {
            return this.extension_.get(n);
        }

        @Override
        public int getExtensionCount() {
            return this.extension_.size();
        }

        @Override
        public List<FieldDescriptorProto> getExtensionList() {
            return this.extension_;
        }

        @Override
        public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int n) {
            return this.extension_.get(n);
        }

        @Override
        public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList() {
            return this.extension_;
        }

        @Override
        public DescriptorProto getMessageType(int n) {
            return this.messageType_.get(n);
        }

        @Override
        public int getMessageTypeCount() {
            return this.messageType_.size();
        }

        @Override
        public List<DescriptorProto> getMessageTypeList() {
            return this.messageType_;
        }

        @Override
        public DescriptorProtoOrBuilder getMessageTypeOrBuilder(int n) {
            return this.messageType_.get(n);
        }

        @Override
        public List<? extends DescriptorProtoOrBuilder> getMessageTypeOrBuilderList() {
            return this.messageType_;
        }

        @Override
        public String getName() {
            Object object = this.name_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.name_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getNameBytes() {
            Object object = this.name_;
            if (object instanceof String) {
                this.name_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public FileOptions getOptions() {
            return this.options_;
        }

        @Override
        public FileOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        @Override
        public String getPackage() {
            Object object = this.package_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.package_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getPackageBytes() {
            Object object = this.package_;
            if (object instanceof String) {
                this.package_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        public Parser<FileDescriptorProto> getParserForType() {
            return PARSER;
        }

        @Override
        public int getPublicDependency(int n) {
            return this.publicDependency_.get(n);
        }

        @Override
        public int getPublicDependencyCount() {
            return this.publicDependency_.size();
        }

        @Override
        public List<Integer> getPublicDependencyList() {
            return this.publicDependency_;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            n = 0;
            if ((this.bitField0_ & 1) == 1) {
                n = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }
            int n2 = n;
            if ((this.bitField0_ & 2) == 2) {
                n2 = n + CodedOutputStream.computeBytesSize(2, this.getPackageBytes());
            }
            int n3 = 0;
            for (n = 0; n < this.dependency_.size(); ++n) {
                n3 += CodedOutputStream.computeBytesSizeNoTag(this.dependency_.getByteString(n));
            }
            n = n2 + n3 + this.getDependencyList().size() * 1;
            for (n2 = 0; n2 < this.messageType_.size(); ++n2) {
                n += CodedOutputStream.computeMessageSize(4, this.messageType_.get(n2));
            }
            for (n2 = 0; n2 < this.enumType_.size(); ++n2) {
                n += CodedOutputStream.computeMessageSize(5, this.enumType_.get(n2));
            }
            for (n2 = 0; n2 < this.service_.size(); ++n2) {
                n += CodedOutputStream.computeMessageSize(6, this.service_.get(n2));
            }
            for (n2 = 0; n2 < this.extension_.size(); ++n2) {
                n += CodedOutputStream.computeMessageSize(7, this.extension_.get(n2));
            }
            n2 = n;
            if ((this.bitField0_ & 4) == 4) {
                n2 = n + CodedOutputStream.computeMessageSize(8, this.options_);
            }
            n = n2;
            if ((this.bitField0_ & 8) == 8) {
                n = n2 + CodedOutputStream.computeMessageSize(9, this.sourceCodeInfo_);
            }
            n2 = 0;
            for (n3 = 0; n3 < this.publicDependency_.size(); ++n3) {
                n2 += CodedOutputStream.computeInt32SizeNoTag(this.publicDependency_.get(n3));
            }
            int n4 = this.getPublicDependencyList().size();
            int n5 = 0;
            for (n3 = 0; n3 < this.weakDependency_.size(); ++n3) {
                n5 += CodedOutputStream.computeInt32SizeNoTag(this.weakDependency_.get(n3));
            }
            this.memoizedSerializedSize = n = n + n2 + n4 * 1 + n5 + this.getWeakDependencyList().size() * 1 + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public ServiceDescriptorProto getService(int n) {
            return this.service_.get(n);
        }

        @Override
        public int getServiceCount() {
            return this.service_.size();
        }

        @Override
        public List<ServiceDescriptorProto> getServiceList() {
            return this.service_;
        }

        @Override
        public ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int n) {
            return this.service_.get(n);
        }

        @Override
        public List<? extends ServiceDescriptorProtoOrBuilder> getServiceOrBuilderList() {
            return this.service_;
        }

        @Override
        public SourceCodeInfo getSourceCodeInfo() {
            return this.sourceCodeInfo_;
        }

        @Override
        public SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder() {
            return this.sourceCodeInfo_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public int getWeakDependency(int n) {
            return this.weakDependency_.get(n);
        }

        @Override
        public int getWeakDependencyCount() {
            return this.weakDependency_.size();
        }

        @Override
        public List<Integer> getWeakDependencyList() {
            return this.weakDependency_;
        }

        @Override
        public boolean hasName() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOptions() {
            if ((this.bitField0_ & 4) == 4) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasPackage() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasSourceCodeInfo() {
            if ((this.bitField0_ & 8) == 8) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(FileDescriptorProto.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            if (n != -1) {
                if (n == 1) {
                    return true;
                }
                return false;
            }
            for (n = 0; n < this.getMessageTypeCount(); ++n) {
                if (this.getMessageType(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            for (n = 0; n < this.getEnumTypeCount(); ++n) {
                if (this.getEnumType(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            for (n = 0; n < this.getServiceCount(); ++n) {
                if (this.getService(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            for (n = 0; n < this.getExtensionCount(); ++n) {
                if (this.getExtension(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasOptions() && !this.getOptions().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return FileDescriptorProto.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return FileDescriptorProto.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            int n;
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, this.getNameBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, this.getPackageBytes());
            }
            for (n = 0; n < this.dependency_.size(); ++n) {
                codedOutputStream.writeBytes(3, this.dependency_.getByteString(n));
            }
            for (n = 0; n < this.messageType_.size(); ++n) {
                codedOutputStream.writeMessage(4, this.messageType_.get(n));
            }
            for (n = 0; n < this.enumType_.size(); ++n) {
                codedOutputStream.writeMessage(5, this.enumType_.get(n));
            }
            for (n = 0; n < this.service_.size(); ++n) {
                codedOutputStream.writeMessage(6, this.service_.get(n));
            }
            for (n = 0; n < this.extension_.size(); ++n) {
                codedOutputStream.writeMessage(7, this.extension_.get(n));
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeMessage(8, this.options_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeMessage(9, this.sourceCodeInfo_);
            }
            for (n = 0; n < this.publicDependency_.size(); ++n) {
                codedOutputStream.writeInt32(10, this.publicDependency_.get(n));
            }
            for (n = 0; n < this.weakDependency_.size(); ++n) {
                codedOutputStream.writeInt32(11, this.weakDependency_.get(n));
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements FileDescriptorProtoOrBuilder {
            private int bitField0_;
            private LazyStringList dependency_ = LazyStringArrayList.EMPTY;
            private RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> enumTypeBuilder_;
            private List<EnumDescriptorProto> enumType_ = Collections.emptyList();
            private RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> extensionBuilder_;
            private List<FieldDescriptorProto> extension_ = Collections.emptyList();
            private RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> messageTypeBuilder_;
            private List<DescriptorProto> messageType_ = Collections.emptyList();
            private Object name_ = "";
            private SingleFieldBuilder<FileOptions, FileOptions.Builder, FileOptionsOrBuilder> optionsBuilder_;
            private FileOptions options_ = FileOptions.getDefaultInstance();
            private Object package_ = "";
            private List<Integer> publicDependency_ = Collections.emptyList();
            private RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> serviceBuilder_;
            private List<ServiceDescriptorProto> service_ = Collections.emptyList();
            private SingleFieldBuilder<SourceCodeInfo, SourceCodeInfo.Builder, SourceCodeInfoOrBuilder> sourceCodeInfoBuilder_;
            private SourceCodeInfo sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
            private List<Integer> weakDependency_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureDependencyIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.dependency_ = new LazyStringArrayList(this.dependency_);
                    this.bitField0_ |= 4;
                }
            }

            private void ensureEnumTypeIsMutable() {
                if ((this.bitField0_ & 64) != 64) {
                    this.enumType_ = new ArrayList<EnumDescriptorProto>(this.enumType_);
                    this.bitField0_ |= 64;
                }
            }

            private void ensureExtensionIsMutable() {
                if ((this.bitField0_ & 256) != 256) {
                    this.extension_ = new ArrayList<FieldDescriptorProto>(this.extension_);
                    this.bitField0_ |= 256;
                }
            }

            private void ensureMessageTypeIsMutable() {
                if ((this.bitField0_ & 32) != 32) {
                    this.messageType_ = new ArrayList<DescriptorProto>(this.messageType_);
                    this.bitField0_ |= 32;
                }
            }

            private void ensurePublicDependencyIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.publicDependency_ = new ArrayList<Integer>(this.publicDependency_);
                    this.bitField0_ |= 8;
                }
            }

            private void ensureServiceIsMutable() {
                if ((this.bitField0_ & 128) != 128) {
                    this.service_ = new ArrayList<ServiceDescriptorProto>(this.service_);
                    this.bitField0_ |= 128;
                }
            }

            private void ensureWeakDependencyIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.weakDependency_ = new ArrayList<Integer>(this.weakDependency_);
                    this.bitField0_ |= 16;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_FileDescriptorProto_descriptor;
            }

            private RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> getEnumTypeFieldBuilder() {
                if (this.enumTypeBuilder_ == null) {
                    List<EnumDescriptorProto> list = this.enumType_;
                    boolean bl = (this.bitField0_ & 64) == 64;
                    this.enumTypeBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.enumType_ = null;
                }
                return this.enumTypeBuilder_;
            }

            private RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> getExtensionFieldBuilder() {
                if (this.extensionBuilder_ == null) {
                    List<FieldDescriptorProto> list = this.extension_;
                    boolean bl = (this.bitField0_ & 256) == 256;
                    this.extensionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.extension_ = null;
                }
                return this.extensionBuilder_;
            }

            private RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> getMessageTypeFieldBuilder() {
                if (this.messageTypeBuilder_ == null) {
                    List<DescriptorProto> list = this.messageType_;
                    boolean bl = (this.bitField0_ & 32) == 32;
                    this.messageTypeBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.messageType_ = null;
                }
                return this.messageTypeBuilder_;
            }

            private SingleFieldBuilder<FileOptions, FileOptions.Builder, FileOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> getServiceFieldBuilder() {
                if (this.serviceBuilder_ == null) {
                    List<ServiceDescriptorProto> list = this.service_;
                    boolean bl = (this.bitField0_ & 128) == 128;
                    this.serviceBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.service_ = null;
                }
                return this.serviceBuilder_;
            }

            private SingleFieldBuilder<SourceCodeInfo, SourceCodeInfo.Builder, SourceCodeInfoOrBuilder> getSourceCodeInfoFieldBuilder() {
                if (this.sourceCodeInfoBuilder_ == null) {
                    this.sourceCodeInfoBuilder_ = new SingleFieldBuilder(this.sourceCodeInfo_, this.getParentForChildren(), this.isClean());
                    this.sourceCodeInfo_ = null;
                }
                return this.sourceCodeInfoBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getMessageTypeFieldBuilder();
                    this.getEnumTypeFieldBuilder();
                    this.getServiceFieldBuilder();
                    this.getExtensionFieldBuilder();
                    this.getOptionsFieldBuilder();
                    this.getSourceCodeInfoFieldBuilder();
                }
            }

            public Builder addAllDependency(Iterable<String> iterable) {
                this.ensureDependencyIsMutable();
                GeneratedMessage.Builder.addAll(iterable, this.dependency_);
                this.onChanged();
                return this;
            }

            public Builder addAllEnumType(Iterable<? extends EnumDescriptorProto> iterable) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.enumType_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addAllExtension(Iterable<? extends FieldDescriptorProto> iterable) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.extension_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addAllMessageType(Iterable<? extends DescriptorProto> iterable) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMessageTypeIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.messageType_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addAllPublicDependency(Iterable<? extends Integer> iterable) {
                this.ensurePublicDependencyIsMutable();
                GeneratedMessage.Builder.addAll(iterable, this.publicDependency_);
                this.onChanged();
                return this;
            }

            public Builder addAllService(Iterable<? extends ServiceDescriptorProto> iterable) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureServiceIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.service_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addAllWeakDependency(Iterable<? extends Integer> iterable) {
                this.ensureWeakDependencyIsMutable();
                GeneratedMessage.Builder.addAll(iterable, this.weakDependency_);
                this.onChanged();
                return this;
            }

            public Builder addDependency(String string2) {
                if (string2 != null) {
                    this.ensureDependencyIsMutable();
                    this.dependency_.add(string2);
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder addDependencyBytes(ByteString byteString) {
                if (byteString != null) {
                    this.ensureDependencyIsMutable();
                    this.dependency_.add(byteString);
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder addEnumType(int n, EnumDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    this.enumType_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addEnumType(int n, EnumDescriptorProto enumDescriptorProto) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumDescriptorProto != null) {
                        this.ensureEnumTypeIsMutable();
                        this.enumType_.add(n, enumDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, enumDescriptorProto);
                return this;
            }

            public Builder addEnumType(EnumDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    this.enumType_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addEnumType(EnumDescriptorProto enumDescriptorProto) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumDescriptorProto != null) {
                        this.ensureEnumTypeIsMutable();
                        this.enumType_.add(enumDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(enumDescriptorProto);
                return this;
            }

            public EnumDescriptorProto.Builder addEnumTypeBuilder() {
                return this.getEnumTypeFieldBuilder().addBuilder(EnumDescriptorProto.getDefaultInstance());
            }

            public EnumDescriptorProto.Builder addEnumTypeBuilder(int n) {
                return this.getEnumTypeFieldBuilder().addBuilder(n, EnumDescriptorProto.getDefaultInstance());
            }

            public Builder addExtension(int n, FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    this.extension_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addExtension(int n, FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureExtensionIsMutable();
                        this.extension_.add(n, fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, fieldDescriptorProto);
                return this;
            }

            public Builder addExtension(FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    this.extension_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addExtension(FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureExtensionIsMutable();
                        this.extension_.add(fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(fieldDescriptorProto);
                return this;
            }

            public FieldDescriptorProto.Builder addExtensionBuilder() {
                return this.getExtensionFieldBuilder().addBuilder(FieldDescriptorProto.getDefaultInstance());
            }

            public FieldDescriptorProto.Builder addExtensionBuilder(int n) {
                return this.getExtensionFieldBuilder().addBuilder(n, FieldDescriptorProto.getDefaultInstance());
            }

            public Builder addMessageType(int n, DescriptorProto.Builder builder) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMessageTypeIsMutable();
                    this.messageType_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addMessageType(int n, DescriptorProto descriptorProto) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (descriptorProto != null) {
                        this.ensureMessageTypeIsMutable();
                        this.messageType_.add(n, descriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, descriptorProto);
                return this;
            }

            public Builder addMessageType(DescriptorProto.Builder builder) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMessageTypeIsMutable();
                    this.messageType_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addMessageType(DescriptorProto descriptorProto) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (descriptorProto != null) {
                        this.ensureMessageTypeIsMutable();
                        this.messageType_.add(descriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(descriptorProto);
                return this;
            }

            public DescriptorProto.Builder addMessageTypeBuilder() {
                return this.getMessageTypeFieldBuilder().addBuilder(DescriptorProto.getDefaultInstance());
            }

            public DescriptorProto.Builder addMessageTypeBuilder(int n) {
                return this.getMessageTypeFieldBuilder().addBuilder(n, DescriptorProto.getDefaultInstance());
            }

            public Builder addPublicDependency(int n) {
                this.ensurePublicDependencyIsMutable();
                this.publicDependency_.add(n);
                this.onChanged();
                return this;
            }

            public Builder addService(int n, ServiceDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureServiceIsMutable();
                    this.service_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addService(int n, ServiceDescriptorProto serviceDescriptorProto) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (serviceDescriptorProto != null) {
                        this.ensureServiceIsMutable();
                        this.service_.add(n, serviceDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, serviceDescriptorProto);
                return this;
            }

            public Builder addService(ServiceDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureServiceIsMutable();
                    this.service_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addService(ServiceDescriptorProto serviceDescriptorProto) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (serviceDescriptorProto != null) {
                        this.ensureServiceIsMutable();
                        this.service_.add(serviceDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(serviceDescriptorProto);
                return this;
            }

            public ServiceDescriptorProto.Builder addServiceBuilder() {
                return this.getServiceFieldBuilder().addBuilder(ServiceDescriptorProto.getDefaultInstance());
            }

            public ServiceDescriptorProto.Builder addServiceBuilder(int n) {
                return this.getServiceFieldBuilder().addBuilder(n, ServiceDescriptorProto.getDefaultInstance());
            }

            public Builder addWeakDependency(int n) {
                this.ensureWeakDependencyIsMutable();
                this.weakDependency_.add(n);
                this.onChanged();
                return this;
            }

            @Override
            public FileDescriptorProto build() {
                FileDescriptorProto fileDescriptorProto = this.buildPartial();
                if (fileDescriptorProto.isInitialized()) {
                    return fileDescriptorProto;
                }
                throw Builder.newUninitializedMessageException(fileDescriptorProto);
            }

            @Override
            public FileDescriptorProto buildPartial() {
                FileDescriptorProto fileDescriptorProto = new FileDescriptorProto(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                fileDescriptorProto.name_ = this.name_;
                int n3 = n2;
                if ((n & 2) == 2) {
                    n3 = n2 | 2;
                }
                fileDescriptorProto.package_ = this.package_;
                if ((this.bitField0_ & 4) == 4) {
                    this.dependency_ = new UnmodifiableLazyStringList(this.dependency_);
                    this.bitField0_ &= -5;
                }
                fileDescriptorProto.dependency_ = this.dependency_;
                if ((this.bitField0_ & 8) == 8) {
                    this.publicDependency_ = Collections.unmodifiableList(this.publicDependency_);
                    this.bitField0_ &= -9;
                }
                fileDescriptorProto.publicDependency_ = this.publicDependency_;
                if ((this.bitField0_ & 16) == 16) {
                    this.weakDependency_ = Collections.unmodifiableList(this.weakDependency_);
                    this.bitField0_ &= -17;
                }
                fileDescriptorProto.weakDependency_ = this.weakDependency_;
                GeneratedMessage.BuilderParent builderParent = this.messageTypeBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 32) == 32) {
                        this.messageType_ = Collections.unmodifiableList(this.messageType_);
                        this.bitField0_ &= -33;
                    }
                    fileDescriptorProto.messageType_ = this.messageType_;
                } else {
                    fileDescriptorProto.messageType_ = builderParent.build();
                }
                builderParent = this.enumTypeBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 64) == 64) {
                        this.enumType_ = Collections.unmodifiableList(this.enumType_);
                        this.bitField0_ &= -65;
                    }
                    fileDescriptorProto.enumType_ = this.enumType_;
                } else {
                    fileDescriptorProto.enumType_ = builderParent.build();
                }
                builderParent = this.serviceBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 128) == 128) {
                        this.service_ = Collections.unmodifiableList(this.service_);
                        this.bitField0_ &= -129;
                    }
                    fileDescriptorProto.service_ = this.service_;
                } else {
                    fileDescriptorProto.service_ = builderParent.build();
                }
                builderParent = this.extensionBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 256) == 256) {
                        this.extension_ = Collections.unmodifiableList(this.extension_);
                        this.bitField0_ &= -257;
                    }
                    fileDescriptorProto.extension_ = this.extension_;
                } else {
                    fileDescriptorProto.extension_ = builderParent.build();
                }
                n2 = n3;
                if ((n & 512) == 512) {
                    n2 = n3 | 4;
                }
                if ((builderParent = this.optionsBuilder_) == null) {
                    fileDescriptorProto.options_ = this.options_;
                } else {
                    fileDescriptorProto.options_ = (FileOptions)((Object)builderParent.build());
                }
                n3 = n2;
                if ((n & 1024) == 1024) {
                    n3 = n2 | 8;
                }
                if ((builderParent = this.sourceCodeInfoBuilder_) == null) {
                    fileDescriptorProto.sourceCodeInfo_ = this.sourceCodeInfo_;
                } else {
                    fileDescriptorProto.sourceCodeInfo_ = (SourceCodeInfo)((Object)builderParent.build());
                }
                fileDescriptorProto.bitField0_ = n3;
                this.onBuilt();
                return fileDescriptorProto;
            }

            @Override
            public Builder clear() {
                int n;
                super.clear();
                this.name_ = "";
                this.bitField0_ = n = this.bitField0_ & -2;
                this.package_ = "";
                this.bitField0_ = n & -3;
                this.dependency_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -5;
                this.publicDependency_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.weakDependency_ = Collections.emptyList();
                this.bitField0_ &= -17;
                GeneratedMessage.BuilderParent builderParent = this.messageTypeBuilder_;
                if (builderParent == null) {
                    this.messageType_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                } else {
                    builderParent.clear();
                }
                builderParent = this.enumTypeBuilder_;
                if (builderParent == null) {
                    this.enumType_ = Collections.emptyList();
                    this.bitField0_ &= -65;
                } else {
                    builderParent.clear();
                }
                builderParent = this.serviceBuilder_;
                if (builderParent == null) {
                    this.service_ = Collections.emptyList();
                    this.bitField0_ &= -129;
                } else {
                    builderParent.clear();
                }
                builderParent = this.extensionBuilder_;
                if (builderParent == null) {
                    this.extension_ = Collections.emptyList();
                    this.bitField0_ &= -257;
                } else {
                    builderParent.clear();
                }
                builderParent = this.optionsBuilder_;
                if (builderParent == null) {
                    this.options_ = FileOptions.getDefaultInstance();
                } else {
                    builderParent.clear();
                }
                this.bitField0_ &= -513;
                builderParent = this.sourceCodeInfoBuilder_;
                if (builderParent == null) {
                    this.sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
                } else {
                    builderParent.clear();
                }
                this.bitField0_ &= -1025;
                return this;
            }

            public Builder clearDependency() {
                this.dependency_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -5;
                this.onChanged();
                return this;
            }

            public Builder clearEnumType() {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.enumType_ = Collections.emptyList();
                    this.bitField0_ &= -65;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearExtension() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.extension_ = Collections.emptyList();
                    this.bitField0_ &= -257;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearMessageType() {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.messageType_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = FileDescriptorProto.getDefaultInstance().getName();
                this.onChanged();
                return this;
            }

            public Builder clearOptions() {
                SingleFieldBuilder<FileOptions, FileOptions.Builder, FileOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = FileOptions.getDefaultInstance();
                    this.onChanged();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -513;
                return this;
            }

            public Builder clearPackage() {
                this.bitField0_ &= -3;
                this.package_ = FileDescriptorProto.getDefaultInstance().getPackage();
                this.onChanged();
                return this;
            }

            public Builder clearPublicDependency() {
                this.publicDependency_ = Collections.emptyList();
                this.bitField0_ &= -9;
                this.onChanged();
                return this;
            }

            public Builder clearService() {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.service_ = Collections.emptyList();
                    this.bitField0_ &= -129;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearSourceCodeInfo() {
                SingleFieldBuilder<SourceCodeInfo, SourceCodeInfo.Builder, SourceCodeInfoOrBuilder> singleFieldBuilder = this.sourceCodeInfoBuilder_;
                if (singleFieldBuilder == null) {
                    this.sourceCodeInfo_ = SourceCodeInfo.getDefaultInstance();
                    this.onChanged();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -1025;
                return this;
            }

            public Builder clearWeakDependency() {
                this.weakDependency_ = Collections.emptyList();
                this.bitField0_ &= -17;
                this.onChanged();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public FileDescriptorProto getDefaultInstanceForType() {
                return FileDescriptorProto.getDefaultInstance();
            }

            @Override
            public String getDependency(int n) {
                return (String)this.dependency_.get(n);
            }

            @Override
            public ByteString getDependencyBytes(int n) {
                return this.dependency_.getByteString(n);
            }

            @Override
            public int getDependencyCount() {
                return this.dependency_.size();
            }

            @Override
            public List<String> getDependencyList() {
                return Collections.unmodifiableList(this.dependency_);
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_FileDescriptorProto_descriptor;
            }

            @Override
            public EnumDescriptorProto getEnumType(int n) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.enumType_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public EnumDescriptorProto.Builder getEnumTypeBuilder(int n) {
                return this.getEnumTypeFieldBuilder().getBuilder(n);
            }

            public List<EnumDescriptorProto.Builder> getEnumTypeBuilderList() {
                return this.getEnumTypeFieldBuilder().getBuilderList();
            }

            @Override
            public int getEnumTypeCount() {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.enumType_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<EnumDescriptorProto> getEnumTypeList() {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.enumType_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int n) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.enumType_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList() {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.enumType_);
            }

            @Override
            public FieldDescriptorProto getExtension(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extension_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public FieldDescriptorProto.Builder getExtensionBuilder(int n) {
                return this.getExtensionFieldBuilder().getBuilder(n);
            }

            public List<FieldDescriptorProto.Builder> getExtensionBuilderList() {
                return this.getExtensionFieldBuilder().getBuilderList();
            }

            @Override
            public int getExtensionCount() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extension_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<FieldDescriptorProto> getExtensionList() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.extension_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.extension_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList() {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.extension_);
            }

            @Override
            public DescriptorProto getMessageType(int n) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.messageType_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public DescriptorProto.Builder getMessageTypeBuilder(int n) {
                return this.getMessageTypeFieldBuilder().getBuilder(n);
            }

            public List<DescriptorProto.Builder> getMessageTypeBuilderList() {
                return this.getMessageTypeFieldBuilder().getBuilderList();
            }

            @Override
            public int getMessageTypeCount() {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.messageType_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<DescriptorProto> getMessageTypeList() {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.messageType_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public DescriptorProtoOrBuilder getMessageTypeOrBuilder(int n) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.messageType_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends DescriptorProtoOrBuilder> getMessageTypeOrBuilderList() {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.messageType_);
            }

            @Override
            public String getName() {
                Object object = this.name_;
                if (!(object instanceof String)) {
                    this.name_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getNameBytes() {
                Object object = this.name_;
                if (object instanceof String) {
                    this.name_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public FileOptions getOptions() {
                SingleFieldBuilder<FileOptions, FileOptions.Builder, FileOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    return this.options_;
                }
                return singleFieldBuilder.getMessage();
            }

            public FileOptions.Builder getOptionsBuilder() {
                this.bitField0_ |= 512;
                this.onChanged();
                return this.getOptionsFieldBuilder().getBuilder();
            }

            @Override
            public FileOptionsOrBuilder getOptionsOrBuilder() {
                SingleFieldBuilder<FileOptions, FileOptions.Builder, FileOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder != null) {
                    return singleFieldBuilder.getMessageOrBuilder();
                }
                return this.options_;
            }

            @Override
            public String getPackage() {
                Object object = this.package_;
                if (!(object instanceof String)) {
                    this.package_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getPackageBytes() {
                Object object = this.package_;
                if (object instanceof String) {
                    this.package_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public int getPublicDependency(int n) {
                return this.publicDependency_.get(n);
            }

            @Override
            public int getPublicDependencyCount() {
                return this.publicDependency_.size();
            }

            @Override
            public List<Integer> getPublicDependencyList() {
                return Collections.unmodifiableList(this.publicDependency_);
            }

            @Override
            public ServiceDescriptorProto getService(int n) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.service_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public ServiceDescriptorProto.Builder getServiceBuilder(int n) {
                return this.getServiceFieldBuilder().getBuilder(n);
            }

            public List<ServiceDescriptorProto.Builder> getServiceBuilderList() {
                return this.getServiceFieldBuilder().getBuilderList();
            }

            @Override
            public int getServiceCount() {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.service_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<ServiceDescriptorProto> getServiceList() {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.service_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int n) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.service_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends ServiceDescriptorProtoOrBuilder> getServiceOrBuilderList() {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.service_);
            }

            @Override
            public SourceCodeInfo getSourceCodeInfo() {
                SingleFieldBuilder<SourceCodeInfo, SourceCodeInfo.Builder, SourceCodeInfoOrBuilder> singleFieldBuilder = this.sourceCodeInfoBuilder_;
                if (singleFieldBuilder == null) {
                    return this.sourceCodeInfo_;
                }
                return singleFieldBuilder.getMessage();
            }

            public SourceCodeInfo.Builder getSourceCodeInfoBuilder() {
                this.bitField0_ |= 1024;
                this.onChanged();
                return this.getSourceCodeInfoFieldBuilder().getBuilder();
            }

            @Override
            public SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder() {
                SingleFieldBuilder<SourceCodeInfo, SourceCodeInfo.Builder, SourceCodeInfoOrBuilder> singleFieldBuilder = this.sourceCodeInfoBuilder_;
                if (singleFieldBuilder != null) {
                    return singleFieldBuilder.getMessageOrBuilder();
                }
                return this.sourceCodeInfo_;
            }

            @Override
            public int getWeakDependency(int n) {
                return this.weakDependency_.get(n);
            }

            @Override
            public int getWeakDependencyCount() {
                return this.weakDependency_.size();
            }

            @Override
            public List<Integer> getWeakDependencyList() {
                return Collections.unmodifiableList(this.weakDependency_);
            }

            @Override
            public boolean hasName() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOptions() {
                if ((this.bitField0_ & 512) == 512) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasPackage() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasSourceCodeInfo() {
                if ((this.bitField0_ & 1024) == 1024) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(FileDescriptorProto.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                int n;
                for (n = 0; n < this.getMessageTypeCount(); ++n) {
                    if (this.getMessageType(n).isInitialized()) continue;
                    return false;
                }
                for (n = 0; n < this.getEnumTypeCount(); ++n) {
                    if (this.getEnumType(n).isInitialized()) continue;
                    return false;
                }
                for (n = 0; n < this.getServiceCount(); ++n) {
                    if (this.getService(n).isInitialized()) continue;
                    return false;
                }
                for (n = 0; n < this.getExtensionCount(); ++n) {
                    if (this.getExtension(n).isInitialized()) continue;
                    return false;
                }
                if (this.hasOptions() && !this.getOptions().isInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = FileDescriptorProto.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((FileDescriptorProto)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (FileDescriptorProto)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((FileDescriptorProto)object3);
                throw throwable222;
            }

            public Builder mergeFrom(FileDescriptorProto fileDescriptorProto) {
                if (fileDescriptorProto == FileDescriptorProto.getDefaultInstance()) {
                    return this;
                }
                if (fileDescriptorProto.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = fileDescriptorProto.name_;
                    this.onChanged();
                }
                if (fileDescriptorProto.hasPackage()) {
                    this.bitField0_ |= 2;
                    this.package_ = fileDescriptorProto.package_;
                    this.onChanged();
                }
                if (!fileDescriptorProto.dependency_.isEmpty()) {
                    if (this.dependency_.isEmpty()) {
                        this.dependency_ = fileDescriptorProto.dependency_;
                        this.bitField0_ &= -5;
                    } else {
                        this.ensureDependencyIsMutable();
                        this.dependency_.addAll(fileDescriptorProto.dependency_);
                    }
                    this.onChanged();
                }
                if (!fileDescriptorProto.publicDependency_.isEmpty()) {
                    if (this.publicDependency_.isEmpty()) {
                        this.publicDependency_ = fileDescriptorProto.publicDependency_;
                        this.bitField0_ &= -9;
                    } else {
                        this.ensurePublicDependencyIsMutable();
                        this.publicDependency_.addAll(fileDescriptorProto.publicDependency_);
                    }
                    this.onChanged();
                }
                if (!fileDescriptorProto.weakDependency_.isEmpty()) {
                    if (this.weakDependency_.isEmpty()) {
                        this.weakDependency_ = fileDescriptorProto.weakDependency_;
                        this.bitField0_ &= -17;
                    } else {
                        this.ensureWeakDependencyIsMutable();
                        this.weakDependency_.addAll(fileDescriptorProto.weakDependency_);
                    }
                    this.onChanged();
                }
                RepeatedFieldBuilder repeatedFieldBuilder = this.messageTypeBuilder_;
                Object var3_3 = null;
                if (repeatedFieldBuilder == null) {
                    if (!fileDescriptorProto.messageType_.isEmpty()) {
                        if (this.messageType_.isEmpty()) {
                            this.messageType_ = fileDescriptorProto.messageType_;
                            this.bitField0_ &= -33;
                        } else {
                            this.ensureMessageTypeIsMutable();
                            this.messageType_.addAll(fileDescriptorProto.messageType_);
                        }
                        this.onChanged();
                    }
                } else if (!fileDescriptorProto.messageType_.isEmpty()) {
                    if (this.messageTypeBuilder_.isEmpty()) {
                        this.messageTypeBuilder_.dispose();
                        this.messageTypeBuilder_ = null;
                        this.messageType_ = fileDescriptorProto.messageType_;
                        this.bitField0_ &= -33;
                        repeatedFieldBuilder = GeneratedMessage.alwaysUseFieldBuilders ? this.getMessageTypeFieldBuilder() : null;
                        this.messageTypeBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.messageTypeBuilder_.addAllMessages(fileDescriptorProto.messageType_);
                    }
                }
                if (this.enumTypeBuilder_ == null) {
                    if (!fileDescriptorProto.enumType_.isEmpty()) {
                        if (this.enumType_.isEmpty()) {
                            this.enumType_ = fileDescriptorProto.enumType_;
                            this.bitField0_ &= -65;
                        } else {
                            this.ensureEnumTypeIsMutable();
                            this.enumType_.addAll(fileDescriptorProto.enumType_);
                        }
                        this.onChanged();
                    }
                } else if (!fileDescriptorProto.enumType_.isEmpty()) {
                    if (this.enumTypeBuilder_.isEmpty()) {
                        this.enumTypeBuilder_.dispose();
                        this.enumTypeBuilder_ = null;
                        this.enumType_ = fileDescriptorProto.enumType_;
                        this.bitField0_ &= -65;
                        repeatedFieldBuilder = GeneratedMessage.alwaysUseFieldBuilders ? this.getEnumTypeFieldBuilder() : null;
                        this.enumTypeBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.enumTypeBuilder_.addAllMessages(fileDescriptorProto.enumType_);
                    }
                }
                if (this.serviceBuilder_ == null) {
                    if (!fileDescriptorProto.service_.isEmpty()) {
                        if (this.service_.isEmpty()) {
                            this.service_ = fileDescriptorProto.service_;
                            this.bitField0_ &= -129;
                        } else {
                            this.ensureServiceIsMutable();
                            this.service_.addAll(fileDescriptorProto.service_);
                        }
                        this.onChanged();
                    }
                } else if (!fileDescriptorProto.service_.isEmpty()) {
                    if (this.serviceBuilder_.isEmpty()) {
                        this.serviceBuilder_.dispose();
                        this.serviceBuilder_ = null;
                        this.service_ = fileDescriptorProto.service_;
                        this.bitField0_ &= -129;
                        repeatedFieldBuilder = GeneratedMessage.alwaysUseFieldBuilders ? this.getServiceFieldBuilder() : null;
                        this.serviceBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.serviceBuilder_.addAllMessages(fileDescriptorProto.service_);
                    }
                }
                if (this.extensionBuilder_ == null) {
                    if (!fileDescriptorProto.extension_.isEmpty()) {
                        if (this.extension_.isEmpty()) {
                            this.extension_ = fileDescriptorProto.extension_;
                            this.bitField0_ &= -257;
                        } else {
                            this.ensureExtensionIsMutable();
                            this.extension_.addAll(fileDescriptorProto.extension_);
                        }
                        this.onChanged();
                    }
                } else if (!fileDescriptorProto.extension_.isEmpty()) {
                    if (this.extensionBuilder_.isEmpty()) {
                        this.extensionBuilder_.dispose();
                        this.extensionBuilder_ = null;
                        this.extension_ = fileDescriptorProto.extension_;
                        this.bitField0_ &= -257;
                        repeatedFieldBuilder = var3_3;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getExtensionFieldBuilder();
                        }
                        this.extensionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.extensionBuilder_.addAllMessages(fileDescriptorProto.extension_);
                    }
                }
                if (fileDescriptorProto.hasOptions()) {
                    this.mergeOptions(fileDescriptorProto.getOptions());
                }
                if (fileDescriptorProto.hasSourceCodeInfo()) {
                    this.mergeSourceCodeInfo(fileDescriptorProto.getSourceCodeInfo());
                }
                this.mergeUnknownFields(fileDescriptorProto.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof FileDescriptorProto) {
                    return this.mergeFrom((FileDescriptorProto)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(FileOptions fileOptions) {
                SingleFieldBuilder<FileOptions, FileOptions.Builder, FileOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = (this.bitField0_ & 512) == 512 && this.options_ != FileOptions.getDefaultInstance() ? FileOptions.newBuilder(this.options_).mergeFrom(fileOptions).buildPartial() : fileOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.mergeFrom(fileOptions);
                }
                this.bitField0_ |= 512;
                return this;
            }

            public Builder mergeSourceCodeInfo(SourceCodeInfo sourceCodeInfo) {
                SingleFieldBuilder<SourceCodeInfo, SourceCodeInfo.Builder, SourceCodeInfoOrBuilder> singleFieldBuilder = this.sourceCodeInfoBuilder_;
                if (singleFieldBuilder == null) {
                    this.sourceCodeInfo_ = (this.bitField0_ & 1024) == 1024 && this.sourceCodeInfo_ != SourceCodeInfo.getDefaultInstance() ? SourceCodeInfo.newBuilder(this.sourceCodeInfo_).mergeFrom(sourceCodeInfo).buildPartial() : sourceCodeInfo;
                    this.onChanged();
                } else {
                    singleFieldBuilder.mergeFrom(sourceCodeInfo);
                }
                this.bitField0_ |= 1024;
                return this;
            }

            public Builder removeEnumType(int n) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    this.enumType_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder removeExtension(int n) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    this.extension_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder removeMessageType(int n) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMessageTypeIsMutable();
                    this.messageType_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder removeService(int n) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureServiceIsMutable();
                    this.service_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setDependency(int n, String string2) {
                if (string2 != null) {
                    this.ensureDependencyIsMutable();
                    this.dependency_.set(n, string2);
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setEnumType(int n, EnumDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureEnumTypeIsMutable();
                    this.enumType_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setEnumType(int n, EnumDescriptorProto enumDescriptorProto) {
                RepeatedFieldBuilder<EnumDescriptorProto, EnumDescriptorProto.Builder, EnumDescriptorProtoOrBuilder> repeatedFieldBuilder = this.enumTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (enumDescriptorProto != null) {
                        this.ensureEnumTypeIsMutable();
                        this.enumType_.set(n, enumDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, enumDescriptorProto);
                return this;
            }

            public Builder setExtension(int n, FieldDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureExtensionIsMutable();
                    this.extension_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setExtension(int n, FieldDescriptorProto fieldDescriptorProto) {
                RepeatedFieldBuilder<FieldDescriptorProto, FieldDescriptorProto.Builder, FieldDescriptorProtoOrBuilder> repeatedFieldBuilder = this.extensionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fieldDescriptorProto != null) {
                        this.ensureExtensionIsMutable();
                        this.extension_.set(n, fieldDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, fieldDescriptorProto);
                return this;
            }

            public Builder setMessageType(int n, DescriptorProto.Builder builder) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMessageTypeIsMutable();
                    this.messageType_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setMessageType(int n, DescriptorProto descriptorProto) {
                RepeatedFieldBuilder<DescriptorProto, DescriptorProto.Builder, DescriptorProtoOrBuilder> repeatedFieldBuilder = this.messageTypeBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (descriptorProto != null) {
                        this.ensureMessageTypeIsMutable();
                        this.messageType_.set(n, descriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, descriptorProto);
                return this;
            }

            public Builder setName(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 1;
                    this.name_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 1;
                    this.name_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setOptions(FileOptions.Builder builder) {
                SingleFieldBuilder<FileOptions, FileOptions.Builder, FileOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = builder.build();
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(builder.build());
                }
                this.bitField0_ |= 512;
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Builder setOptions(FileOptions fileOptions) {
                SingleFieldBuilder<FileOptions, FileOptions.Builder, FileOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    if (fileOptions == null) throw null;
                    this.options_ = fileOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(fileOptions);
                }
                this.bitField0_ |= 512;
                return this;
            }

            public Builder setPackage(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 2;
                    this.package_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setPackageBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 2;
                    this.package_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setPublicDependency(int n, int n2) {
                this.ensurePublicDependencyIsMutable();
                this.publicDependency_.set(n, n2);
                this.onChanged();
                return this;
            }

            public Builder setService(int n, ServiceDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureServiceIsMutable();
                    this.service_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setService(int n, ServiceDescriptorProto serviceDescriptorProto) {
                RepeatedFieldBuilder<ServiceDescriptorProto, ServiceDescriptorProto.Builder, ServiceDescriptorProtoOrBuilder> repeatedFieldBuilder = this.serviceBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (serviceDescriptorProto != null) {
                        this.ensureServiceIsMutable();
                        this.service_.set(n, serviceDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, serviceDescriptorProto);
                return this;
            }

            public Builder setSourceCodeInfo(SourceCodeInfo.Builder builder) {
                SingleFieldBuilder<SourceCodeInfo, SourceCodeInfo.Builder, SourceCodeInfoOrBuilder> singleFieldBuilder = this.sourceCodeInfoBuilder_;
                if (singleFieldBuilder == null) {
                    this.sourceCodeInfo_ = builder.build();
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(builder.build());
                }
                this.bitField0_ |= 1024;
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Builder setSourceCodeInfo(SourceCodeInfo sourceCodeInfo) {
                SingleFieldBuilder<SourceCodeInfo, SourceCodeInfo.Builder, SourceCodeInfoOrBuilder> singleFieldBuilder = this.sourceCodeInfoBuilder_;
                if (singleFieldBuilder == null) {
                    if (sourceCodeInfo == null) throw null;
                    this.sourceCodeInfo_ = sourceCodeInfo;
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(sourceCodeInfo);
                }
                this.bitField0_ |= 1024;
                return this;
            }

            public Builder setWeakDependency(int n, int n2) {
                this.ensureWeakDependencyIsMutable();
                this.weakDependency_.set(n, n2);
                this.onChanged();
                return this;
            }
        }

    }

    public static interface FileDescriptorProtoOrBuilder
    extends MessageOrBuilder {
        public String getDependency(int var1);

        public ByteString getDependencyBytes(int var1);

        public int getDependencyCount();

        public List<String> getDependencyList();

        public EnumDescriptorProto getEnumType(int var1);

        public int getEnumTypeCount();

        public List<EnumDescriptorProto> getEnumTypeList();

        public EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int var1);

        public List<? extends EnumDescriptorProtoOrBuilder> getEnumTypeOrBuilderList();

        public FieldDescriptorProto getExtension(int var1);

        public int getExtensionCount();

        public List<FieldDescriptorProto> getExtensionList();

        public FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int var1);

        public List<? extends FieldDescriptorProtoOrBuilder> getExtensionOrBuilderList();

        public DescriptorProto getMessageType(int var1);

        public int getMessageTypeCount();

        public List<DescriptorProto> getMessageTypeList();

        public DescriptorProtoOrBuilder getMessageTypeOrBuilder(int var1);

        public List<? extends DescriptorProtoOrBuilder> getMessageTypeOrBuilderList();

        public String getName();

        public ByteString getNameBytes();

        public FileOptions getOptions();

        public FileOptionsOrBuilder getOptionsOrBuilder();

        public String getPackage();

        public ByteString getPackageBytes();

        public int getPublicDependency(int var1);

        public int getPublicDependencyCount();

        public List<Integer> getPublicDependencyList();

        public ServiceDescriptorProto getService(int var1);

        public int getServiceCount();

        public List<ServiceDescriptorProto> getServiceList();

        public ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int var1);

        public List<? extends ServiceDescriptorProtoOrBuilder> getServiceOrBuilderList();

        public SourceCodeInfo getSourceCodeInfo();

        public SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder();

        public int getWeakDependency(int var1);

        public int getWeakDependencyCount();

        public List<Integer> getWeakDependencyList();

        public boolean hasName();

        public boolean hasOptions();

        public boolean hasPackage();

        public boolean hasSourceCodeInfo();
    }

    public static final class FileDescriptorSet
    extends GeneratedMessage
    implements FileDescriptorSetOrBuilder {
        public static final int FILE_FIELD_NUMBER = 1;
        public static Parser<FileDescriptorSet> PARSER;
        private static final FileDescriptorSet defaultInstance;
        private static final long serialVersionUID = 0L;
        private List<FileDescriptorProto> file_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private final UnknownFieldSet unknownFields;

        static {
            FileDescriptorSet fileDescriptorSet;
            PARSER = new AbstractParser<FileDescriptorSet>(){

                @Override
                public FileDescriptorSet parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new FileDescriptorSet(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = fileDescriptorSet = new FileDescriptorSet(true);
            fileDescriptorSet.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private FileDescriptorSet(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = false;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = false;
            while (!var8_8) {
                block13 : {
                    block14 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl43
                        if (var10_14 == 10) break block14;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                            var4_9 = true;
                            var9_13 = var3_6;
                        }
                        ** GOTO lbl45
                    }
                    var4_9 = var3_6;
                    if (!(var3_6 & true)) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.file_ = new ArrayList<FileDescriptorProto>();
                        var4_9 = var3_6 | true;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.file_.add(var1_1.readMessage(FileDescriptorProto.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl43: // 1 sources:
                        var4_9 = true;
                        var9_13 = var3_6;
lbl45: // 3 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block13;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if (var5_10 & true) {
                    this.file_ = Collections.unmodifiableList(this.file_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if (var3_6 & true) {
                this.file_ = Collections.unmodifiableList(this.file_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private FileDescriptorSet(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private FileDescriptorSet(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static FileDescriptorSet getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_FileDescriptorSet_descriptor;
        }

        private void initFields() {
            this.file_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FileDescriptorSet fileDescriptorSet) {
            return FileDescriptorSet.newBuilder().mergeFrom(fileDescriptorSet);
        }

        public static FileDescriptorSet parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static FileDescriptorSet parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static FileDescriptorSet parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static FileDescriptorSet parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static FileDescriptorSet parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static FileDescriptorSet parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static FileDescriptorSet parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static FileDescriptorSet parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static FileDescriptorSet parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static FileDescriptorSet parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public FileDescriptorSet getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public FileDescriptorProto getFile(int n) {
            return this.file_.get(n);
        }

        @Override
        public int getFileCount() {
            return this.file_.size();
        }

        @Override
        public List<FileDescriptorProto> getFileList() {
            return this.file_;
        }

        @Override
        public FileDescriptorProtoOrBuilder getFileOrBuilder(int n) {
            return this.file_.get(n);
        }

        @Override
        public List<? extends FileDescriptorProtoOrBuilder> getFileOrBuilderList() {
            return this.file_;
        }

        public Parser<FileDescriptorSet> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            for (n = 0; n < this.file_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(1, this.file_.get(n));
            }
            this.memoizedSerializedSize = n = n2 + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable.ensureFieldAccessorsInitialized(FileDescriptorSet.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getFileCount(); ++n) {
                if (this.getFile(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return FileDescriptorSet.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return FileDescriptorSet.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            for (int i = 0; i < this.file_.size(); ++i) {
                codedOutputStream.writeMessage(1, this.file_.get(i));
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements FileDescriptorSetOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> fileBuilder_;
            private List<FileDescriptorProto> file_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureFileIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.file_ = new ArrayList<FileDescriptorProto>(this.file_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_FileDescriptorSet_descriptor;
            }

            private RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> getFileFieldBuilder() {
                if (this.fileBuilder_ == null) {
                    List<FileDescriptorProto> list = this.file_;
                    int n = this.bitField0_;
                    boolean bl = true;
                    if ((n & 1) != 1) {
                        bl = false;
                    }
                    this.fileBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.file_ = null;
                }
                return this.fileBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getFileFieldBuilder();
                }
            }

            public Builder addAllFile(Iterable<? extends FileDescriptorProto> iterable) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFileIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.file_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addFile(int n, FileDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFileIsMutable();
                    this.file_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addFile(int n, FileDescriptorProto fileDescriptorProto) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fileDescriptorProto != null) {
                        this.ensureFileIsMutable();
                        this.file_.add(n, fileDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, fileDescriptorProto);
                return this;
            }

            public Builder addFile(FileDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFileIsMutable();
                    this.file_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addFile(FileDescriptorProto fileDescriptorProto) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fileDescriptorProto != null) {
                        this.ensureFileIsMutable();
                        this.file_.add(fileDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(fileDescriptorProto);
                return this;
            }

            public FileDescriptorProto.Builder addFileBuilder() {
                return this.getFileFieldBuilder().addBuilder(FileDescriptorProto.getDefaultInstance());
            }

            public FileDescriptorProto.Builder addFileBuilder(int n) {
                return this.getFileFieldBuilder().addBuilder(n, FileDescriptorProto.getDefaultInstance());
            }

            @Override
            public FileDescriptorSet build() {
                FileDescriptorSet fileDescriptorSet = this.buildPartial();
                if (fileDescriptorSet.isInitialized()) {
                    return fileDescriptorSet;
                }
                throw Builder.newUninitializedMessageException(fileDescriptorSet);
            }

            @Override
            public FileDescriptorSet buildPartial() {
                FileDescriptorSet fileDescriptorSet = new FileDescriptorSet(this);
                int n = this.bitField0_;
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.file_ = Collections.unmodifiableList(this.file_);
                        this.bitField0_ &= -2;
                    }
                    fileDescriptorSet.file_ = this.file_;
                } else {
                    fileDescriptorSet.file_ = repeatedFieldBuilder.build();
                }
                this.onBuilt();
                return fileDescriptorSet;
            }

            @Override
            public Builder clear() {
                super.clear();
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.file_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearFile() {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.file_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public FileDescriptorSet getDefaultInstanceForType() {
                return FileDescriptorSet.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_FileDescriptorSet_descriptor;
            }

            @Override
            public FileDescriptorProto getFile(int n) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.file_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public FileDescriptorProto.Builder getFileBuilder(int n) {
                return this.getFileFieldBuilder().getBuilder(n);
            }

            public List<FileDescriptorProto.Builder> getFileBuilderList() {
                return this.getFileFieldBuilder().getBuilderList();
            }

            @Override
            public int getFileCount() {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.file_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<FileDescriptorProto> getFileList() {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.file_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public FileDescriptorProtoOrBuilder getFileOrBuilder(int n) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.file_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends FileDescriptorProtoOrBuilder> getFileOrBuilderList() {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.file_);
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable.ensureFieldAccessorsInitialized(FileDescriptorSet.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getFileCount(); ++i) {
                    if (this.getFile(i).isInitialized()) continue;
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = FileDescriptorSet.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((FileDescriptorSet)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (FileDescriptorSet)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((FileDescriptorSet)object3);
                throw throwable222;
            }

            public Builder mergeFrom(FileDescriptorSet fileDescriptorSet) {
                if (fileDescriptorSet == FileDescriptorSet.getDefaultInstance()) {
                    return this;
                }
                if (this.fileBuilder_ == null) {
                    if (!fileDescriptorSet.file_.isEmpty()) {
                        if (this.file_.isEmpty()) {
                            this.file_ = fileDescriptorSet.file_;
                            this.bitField0_ &= -2;
                        } else {
                            this.ensureFileIsMutable();
                            this.file_.addAll(fileDescriptorSet.file_);
                        }
                        this.onChanged();
                    }
                } else if (!fileDescriptorSet.file_.isEmpty()) {
                    if (this.fileBuilder_.isEmpty()) {
                        this.fileBuilder_.dispose();
                        RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = null;
                        this.fileBuilder_ = null;
                        this.file_ = fileDescriptorSet.file_;
                        this.bitField0_ &= -2;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getFileFieldBuilder();
                        }
                        this.fileBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.fileBuilder_.addAllMessages(fileDescriptorSet.file_);
                    }
                }
                this.mergeUnknownFields(fileDescriptorSet.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof FileDescriptorSet) {
                    return this.mergeFrom((FileDescriptorSet)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeFile(int n) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFileIsMutable();
                    this.file_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setFile(int n, FileDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureFileIsMutable();
                    this.file_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setFile(int n, FileDescriptorProto fileDescriptorProto) {
                RepeatedFieldBuilder<FileDescriptorProto, FileDescriptorProto.Builder, FileDescriptorProtoOrBuilder> repeatedFieldBuilder = this.fileBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (fileDescriptorProto != null) {
                        this.ensureFileIsMutable();
                        this.file_.set(n, fileDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, fileDescriptorProto);
                return this;
            }
        }

    }

    public static interface FileDescriptorSetOrBuilder
    extends MessageOrBuilder {
        public FileDescriptorProto getFile(int var1);

        public int getFileCount();

        public List<FileDescriptorProto> getFileList();

        public FileDescriptorProtoOrBuilder getFileOrBuilder(int var1);

        public List<? extends FileDescriptorProtoOrBuilder> getFileOrBuilderList();
    }

    public static final class FileOptions
    extends GeneratedMessage.ExtendableMessage<FileOptions>
    implements FileOptionsOrBuilder {
        public static final int CC_GENERIC_SERVICES_FIELD_NUMBER = 16;
        public static final int GO_PACKAGE_FIELD_NUMBER = 11;
        public static final int JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER = 20;
        public static final int JAVA_GENERIC_SERVICES_FIELD_NUMBER = 17;
        public static final int JAVA_MULTIPLE_FILES_FIELD_NUMBER = 10;
        public static final int JAVA_OUTER_CLASSNAME_FIELD_NUMBER = 8;
        public static final int JAVA_PACKAGE_FIELD_NUMBER = 1;
        public static final int OPTIMIZE_FOR_FIELD_NUMBER = 9;
        public static Parser<FileOptions> PARSER;
        public static final int PY_GENERIC_SERVICES_FIELD_NUMBER = 18;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final FileOptions defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private boolean ccGenericServices_;
        private Object goPackage_;
        private boolean javaGenerateEqualsAndHash_;
        private boolean javaGenericServices_;
        private boolean javaMultipleFiles_;
        private Object javaOuterClassname_;
        private Object javaPackage_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private OptimizeMode optimizeFor_;
        private boolean pyGenericServices_;
        private List<UninterpretedOption> uninterpretedOption_;
        private final UnknownFieldSet unknownFields;

        static {
            FileOptions fileOptions;
            PARSER = new AbstractParser<FileOptions>(){

                @Override
                public FileOptions parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new FileOptions(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = fileOptions = new FileOptions(true);
            fileOptions.initFields();
        }

        /*
         * Exception decompiling
         */
        private FileOptions(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: First case is not immediately after switch.
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:366)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
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

        private FileOptions(GeneratedMessage.ExtendableBuilder<FileOptions, ?> extendableBuilder) {
            super(extendableBuilder);
            this.memoizedIsInitialized = (byte)-1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = extendableBuilder.getUnknownFields();
        }

        private FileOptions(boolean bl) {
            this.memoizedIsInitialized = (byte)-1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static FileOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_FileOptions_descriptor;
        }

        private void initFields() {
            this.javaPackage_ = "";
            this.javaOuterClassname_ = "";
            this.javaMultipleFiles_ = false;
            this.javaGenerateEqualsAndHash_ = false;
            this.optimizeFor_ = OptimizeMode.SPEED;
            this.goPackage_ = "";
            this.ccGenericServices_ = false;
            this.javaGenericServices_ = false;
            this.pyGenericServices_ = false;
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(FileOptions fileOptions) {
            return FileOptions.newBuilder().mergeFrom(fileOptions);
        }

        public static FileOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static FileOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static FileOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static FileOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static FileOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static FileOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static FileOptions parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static FileOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static FileOptions parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static FileOptions parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public boolean getCcGenericServices() {
            return this.ccGenericServices_;
        }

        @Override
        public FileOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public String getGoPackage() {
            Object object = this.goPackage_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.goPackage_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getGoPackageBytes() {
            Object object = this.goPackage_;
            if (object instanceof String) {
                this.goPackage_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public boolean getJavaGenerateEqualsAndHash() {
            return this.javaGenerateEqualsAndHash_;
        }

        @Override
        public boolean getJavaGenericServices() {
            return this.javaGenericServices_;
        }

        @Override
        public boolean getJavaMultipleFiles() {
            return this.javaMultipleFiles_;
        }

        @Override
        public String getJavaOuterClassname() {
            Object object = this.javaOuterClassname_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.javaOuterClassname_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getJavaOuterClassnameBytes() {
            Object object = this.javaOuterClassname_;
            if (object instanceof String) {
                this.javaOuterClassname_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public String getJavaPackage() {
            Object object = this.javaPackage_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.javaPackage_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getJavaPackageBytes() {
            Object object = this.javaPackage_;
            if (object instanceof String) {
                this.javaPackage_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public OptimizeMode getOptimizeFor() {
            return this.optimizeFor_;
        }

        public Parser<FileOptions> getParserForType() {
            return PARSER;
        }

        @Override
        public boolean getPyGenericServices() {
            return this.pyGenericServices_;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeBytesSize(1, this.getJavaPackageBytes());
            }
            n = n2;
            if ((this.bitField0_ & 2) == 2) {
                n = n2 + CodedOutputStream.computeBytesSize(8, this.getJavaOuterClassnameBytes());
            }
            n2 = n;
            if ((this.bitField0_ & 16) == 16) {
                n2 = n + CodedOutputStream.computeEnumSize(9, this.optimizeFor_.getNumber());
            }
            n = n2;
            if ((this.bitField0_ & 4) == 4) {
                n = n2 + CodedOutputStream.computeBoolSize(10, this.javaMultipleFiles_);
            }
            n2 = n;
            if ((this.bitField0_ & 32) == 32) {
                n2 = n + CodedOutputStream.computeBytesSize(11, this.getGoPackageBytes());
            }
            n = n2;
            if ((this.bitField0_ & 64) == 64) {
                n = n2 + CodedOutputStream.computeBoolSize(16, this.ccGenericServices_);
            }
            n2 = n;
            if ((this.bitField0_ & 128) == 128) {
                n2 = n + CodedOutputStream.computeBoolSize(17, this.javaGenericServices_);
            }
            int n3 = n2;
            if ((this.bitField0_ & 256) == 256) {
                n3 = n2 + CodedOutputStream.computeBoolSize(18, this.pyGenericServices_);
            }
            n = n3;
            if ((this.bitField0_ & 8) == 8) {
                n = n3 + CodedOutputStream.computeBoolSize(20, this.javaGenerateEqualsAndHash_);
            }
            for (n2 = 0; n2 < this.uninterpretedOption_.size(); ++n2) {
                n += CodedOutputStream.computeMessageSize(999, this.uninterpretedOption_.get(n2));
            }
            this.memoizedSerializedSize = n = n + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public UninterpretedOption getUninterpretedOption(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        @Override
        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        @Override
        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasCcGenericServices() {
            if ((this.bitField0_ & 64) == 64) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasGoPackage() {
            if ((this.bitField0_ & 32) == 32) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasJavaGenerateEqualsAndHash() {
            if ((this.bitField0_ & 8) == 8) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasJavaGenericServices() {
            if ((this.bitField0_ & 128) == 128) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasJavaMultipleFiles() {
            if ((this.bitField0_ & 4) == 4) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasJavaOuterClassname() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasJavaPackage() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOptimizeFor() {
            if ((this.bitField0_ & 16) == 16) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasPyGenericServices() {
            if ((this.bitField0_ & 256) == 256) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_FileOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(FileOptions.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getUninterpretedOptionCount(); ++n) {
                if (this.getUninterpretedOption(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.extensionsAreInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return FileOptions.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return FileOptions.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            GeneratedMessage.ExtendableMessage.ExtensionWriter extensionWriter = this.newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, this.getJavaPackageBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(8, this.getJavaOuterClassnameBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeEnum(9, this.optimizeFor_.getNumber());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBool(10, this.javaMultipleFiles_);
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeBytes(11, this.getGoPackageBytes());
            }
            if ((this.bitField0_ & 64) == 64) {
                codedOutputStream.writeBool(16, this.ccGenericServices_);
            }
            if ((this.bitField0_ & 128) == 128) {
                codedOutputStream.writeBool(17, this.javaGenericServices_);
            }
            if ((this.bitField0_ & 256) == 256) {
                codedOutputStream.writeBool(18, this.pyGenericServices_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBool(20, this.javaGenerateEqualsAndHash_);
            }
            for (int i = 0; i < this.uninterpretedOption_.size(); ++i) {
                codedOutputStream.writeMessage(999, this.uninterpretedOption_.get(i));
            }
            extensionWriter.writeUntil(536870912, codedOutputStream);
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.ExtendableBuilder<FileOptions, Builder>
        implements FileOptionsOrBuilder {
            private int bitField0_;
            private boolean ccGenericServices_;
            private Object goPackage_ = "";
            private boolean javaGenerateEqualsAndHash_;
            private boolean javaGenericServices_;
            private boolean javaMultipleFiles_;
            private Object javaOuterClassname_ = "";
            private Object javaPackage_ = "";
            private OptimizeMode optimizeFor_ = OptimizeMode.SPEED;
            private boolean pyGenericServices_;
            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 512) != 512) {
                    this.uninterpretedOption_ = new ArrayList<UninterpretedOption>(this.uninterpretedOption_);
                    this.bitField0_ |= 512;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_FileOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    List<UninterpretedOption> list = this.uninterpretedOption_;
                    boolean bl = (this.bitField0_ & 512) == 512;
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    GeneratedMessage.ExtendableBuilder.addAll(iterable, this.uninterpretedOption_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, uninterpretedOption);
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(uninterpretedOption);
                return this;
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder() {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(n, UninterpretedOption.getDefaultInstance());
            }

            @Override
            public FileOptions build() {
                FileOptions fileOptions = this.buildPartial();
                if (fileOptions.isInitialized()) {
                    return fileOptions;
                }
                throw Builder.newUninitializedMessageException(fileOptions);
            }

            @Override
            public FileOptions buildPartial() {
                FileOptions fileOptions = new FileOptions(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                fileOptions.javaPackage_ = this.javaPackage_;
                int n3 = n2;
                if ((n & 2) == 2) {
                    n3 = n2 | 2;
                }
                fileOptions.javaOuterClassname_ = this.javaOuterClassname_;
                n2 = n3;
                if ((n & 4) == 4) {
                    n2 = n3 | 4;
                }
                fileOptions.javaMultipleFiles_ = this.javaMultipleFiles_;
                n3 = n2;
                if ((n & 8) == 8) {
                    n3 = n2 | 8;
                }
                fileOptions.javaGenerateEqualsAndHash_ = this.javaGenerateEqualsAndHash_;
                n2 = n3;
                if ((n & 16) == 16) {
                    n2 = n3 | 16;
                }
                fileOptions.optimizeFor_ = this.optimizeFor_;
                n3 = n2;
                if ((n & 32) == 32) {
                    n3 = n2 | 32;
                }
                fileOptions.goPackage_ = this.goPackage_;
                n2 = n3;
                if ((n & 64) == 64) {
                    n2 = n3 | 64;
                }
                fileOptions.ccGenericServices_ = this.ccGenericServices_;
                n3 = n2;
                if ((n & 128) == 128) {
                    n3 = n2 | 128;
                }
                fileOptions.javaGenericServices_ = this.javaGenericServices_;
                n2 = n3;
                if ((n & 256) == 256) {
                    n2 = n3 | 256;
                }
                fileOptions.pyGenericServices_ = this.pyGenericServices_;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 512) == 512) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -513;
                    }
                    fileOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    fileOptions.uninterpretedOption_ = repeatedFieldBuilder.build();
                }
                fileOptions.bitField0_ = n2;
                this.onBuilt();
                return fileOptions;
            }

            @Override
            public Builder clear() {
                int n;
                super.clear();
                this.javaPackage_ = "";
                this.bitField0_ = n = this.bitField0_ & -2;
                this.javaOuterClassname_ = "";
                this.bitField0_ = n &= -3;
                this.javaMultipleFiles_ = false;
                this.bitField0_ = n &= -5;
                this.javaGenerateEqualsAndHash_ = false;
                this.bitField0_ = n & -9;
                this.optimizeFor_ = OptimizeMode.SPEED;
                this.bitField0_ = n = this.bitField0_ & -17;
                this.goPackage_ = "";
                this.bitField0_ = n &= -33;
                this.ccGenericServices_ = false;
                this.bitField0_ = n &= -65;
                this.javaGenericServices_ = false;
                this.bitField0_ = n &= -129;
                this.pyGenericServices_ = false;
                this.bitField0_ = n & -257;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -513;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearCcGenericServices() {
                this.bitField0_ &= -65;
                this.ccGenericServices_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearGoPackage() {
                this.bitField0_ &= -33;
                this.goPackage_ = FileOptions.getDefaultInstance().getGoPackage();
                this.onChanged();
                return this;
            }

            public Builder clearJavaGenerateEqualsAndHash() {
                this.bitField0_ &= -9;
                this.javaGenerateEqualsAndHash_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearJavaGenericServices() {
                this.bitField0_ &= -129;
                this.javaGenericServices_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearJavaMultipleFiles() {
                this.bitField0_ &= -5;
                this.javaMultipleFiles_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearJavaOuterClassname() {
                this.bitField0_ &= -3;
                this.javaOuterClassname_ = FileOptions.getDefaultInstance().getJavaOuterClassname();
                this.onChanged();
                return this;
            }

            public Builder clearJavaPackage() {
                this.bitField0_ &= -2;
                this.javaPackage_ = FileOptions.getDefaultInstance().getJavaPackage();
                this.onChanged();
                return this;
            }

            public Builder clearOptimizeFor() {
                this.bitField0_ &= -17;
                this.optimizeFor_ = OptimizeMode.SPEED;
                this.onChanged();
                return this;
            }

            public Builder clearPyGenericServices() {
                this.bitField0_ &= -257;
                this.pyGenericServices_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearUninterpretedOption() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -513;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public boolean getCcGenericServices() {
                return this.ccGenericServices_;
            }

            @Override
            public FileOptions getDefaultInstanceForType() {
                return FileOptions.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_FileOptions_descriptor;
            }

            @Override
            public String getGoPackage() {
                Object object = this.goPackage_;
                if (!(object instanceof String)) {
                    this.goPackage_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getGoPackageBytes() {
                Object object = this.goPackage_;
                if (object instanceof String) {
                    this.goPackage_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public boolean getJavaGenerateEqualsAndHash() {
                return this.javaGenerateEqualsAndHash_;
            }

            @Override
            public boolean getJavaGenericServices() {
                return this.javaGenericServices_;
            }

            @Override
            public boolean getJavaMultipleFiles() {
                return this.javaMultipleFiles_;
            }

            @Override
            public String getJavaOuterClassname() {
                Object object = this.javaOuterClassname_;
                if (!(object instanceof String)) {
                    this.javaOuterClassname_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getJavaOuterClassnameBytes() {
                Object object = this.javaOuterClassname_;
                if (object instanceof String) {
                    this.javaOuterClassname_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public String getJavaPackage() {
                Object object = this.javaPackage_;
                if (!(object instanceof String)) {
                    this.javaPackage_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getJavaPackageBytes() {
                Object object = this.javaPackage_;
                if (object instanceof String) {
                    this.javaPackage_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public OptimizeMode getOptimizeFor() {
                return this.optimizeFor_;
            }

            @Override
            public boolean getPyGenericServices() {
                return this.pyGenericServices_;
            }

            @Override
            public UninterpretedOption getUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public UninterpretedOption.Builder getUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().getBuilder(n);
            }

            public List<UninterpretedOption.Builder> getUninterpretedOptionBuilderList() {
                return this.getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            @Override
            public int getUninterpretedOptionCount() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<UninterpretedOption> getUninterpretedOptionList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.uninterpretedOption_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.uninterpretedOption_);
            }

            @Override
            public boolean hasCcGenericServices() {
                if ((this.bitField0_ & 64) == 64) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasGoPackage() {
                if ((this.bitField0_ & 32) == 32) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasJavaGenerateEqualsAndHash() {
                if ((this.bitField0_ & 8) == 8) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasJavaGenericServices() {
                if ((this.bitField0_ & 128) == 128) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasJavaMultipleFiles() {
                if ((this.bitField0_ & 4) == 4) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasJavaOuterClassname() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasJavaPackage() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOptimizeFor() {
                if ((this.bitField0_ & 16) == 16) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasPyGenericServices() {
                if ((this.bitField0_ & 256) == 256) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_FileOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(FileOptions.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getUninterpretedOptionCount(); ++i) {
                    if (this.getUninterpretedOption(i).isInitialized()) continue;
                    return false;
                }
                if (!this.extensionsAreInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = FileOptions.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((FileOptions)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (FileOptions)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((FileOptions)object3);
                throw throwable222;
            }

            public Builder mergeFrom(FileOptions fileOptions) {
                if (fileOptions == FileOptions.getDefaultInstance()) {
                    return this;
                }
                if (fileOptions.hasJavaPackage()) {
                    this.bitField0_ |= 1;
                    this.javaPackage_ = fileOptions.javaPackage_;
                    this.onChanged();
                }
                if (fileOptions.hasJavaOuterClassname()) {
                    this.bitField0_ |= 2;
                    this.javaOuterClassname_ = fileOptions.javaOuterClassname_;
                    this.onChanged();
                }
                if (fileOptions.hasJavaMultipleFiles()) {
                    this.setJavaMultipleFiles(fileOptions.getJavaMultipleFiles());
                }
                if (fileOptions.hasJavaGenerateEqualsAndHash()) {
                    this.setJavaGenerateEqualsAndHash(fileOptions.getJavaGenerateEqualsAndHash());
                }
                if (fileOptions.hasOptimizeFor()) {
                    this.setOptimizeFor(fileOptions.getOptimizeFor());
                }
                if (fileOptions.hasGoPackage()) {
                    this.bitField0_ |= 32;
                    this.goPackage_ = fileOptions.goPackage_;
                    this.onChanged();
                }
                if (fileOptions.hasCcGenericServices()) {
                    this.setCcGenericServices(fileOptions.getCcGenericServices());
                }
                if (fileOptions.hasJavaGenericServices()) {
                    this.setJavaGenericServices(fileOptions.getJavaGenericServices());
                }
                if (fileOptions.hasPyGenericServices()) {
                    this.setPyGenericServices(fileOptions.getPyGenericServices());
                }
                if (this.uninterpretedOptionBuilder_ == null) {
                    if (!fileOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOption_.isEmpty()) {
                            this.uninterpretedOption_ = fileOptions.uninterpretedOption_;
                            this.bitField0_ &= -513;
                        } else {
                            this.ensureUninterpretedOptionIsMutable();
                            this.uninterpretedOption_.addAll(fileOptions.uninterpretedOption_);
                        }
                        this.onChanged();
                    }
                } else if (!fileOptions.uninterpretedOption_.isEmpty()) {
                    if (this.uninterpretedOptionBuilder_.isEmpty()) {
                        this.uninterpretedOptionBuilder_.dispose();
                        RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = null;
                        this.uninterpretedOptionBuilder_ = null;
                        this.uninterpretedOption_ = fileOptions.uninterpretedOption_;
                        this.bitField0_ &= -513;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getUninterpretedOptionFieldBuilder();
                        }
                        this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.uninterpretedOptionBuilder_.addAllMessages(fileOptions.uninterpretedOption_);
                    }
                }
                this.mergeExtensionFields(fileOptions);
                this.mergeUnknownFields(fileOptions.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof FileOptions) {
                    return this.mergeFrom((FileOptions)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setCcGenericServices(boolean bl) {
                this.bitField0_ |= 64;
                this.ccGenericServices_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setGoPackage(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 32;
                    this.goPackage_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setGoPackageBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 32;
                    this.goPackage_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setJavaGenerateEqualsAndHash(boolean bl) {
                this.bitField0_ |= 8;
                this.javaGenerateEqualsAndHash_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setJavaGenericServices(boolean bl) {
                this.bitField0_ |= 128;
                this.javaGenericServices_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setJavaMultipleFiles(boolean bl) {
                this.bitField0_ |= 4;
                this.javaMultipleFiles_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setJavaOuterClassname(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 2;
                    this.javaOuterClassname_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setJavaOuterClassnameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 2;
                    this.javaOuterClassname_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setJavaPackage(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 1;
                    this.javaPackage_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setJavaPackageBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 1;
                    this.javaPackage_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setOptimizeFor(OptimizeMode optimizeMode) {
                if (optimizeMode != null) {
                    this.bitField0_ |= 16;
                    this.optimizeFor_ = optimizeMode;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setPyGenericServices(boolean bl) {
                this.bitField0_ |= 256;
                this.pyGenericServices_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.set(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, uninterpretedOption);
                return this;
            }
        }

        public static final class OptimizeMode
        extends Enum<OptimizeMode>
        implements ProtocolMessageEnum {
            private static final /* synthetic */ OptimizeMode[] $VALUES;
            public static final /* enum */ OptimizeMode CODE_SIZE;
            public static final int CODE_SIZE_VALUE = 2;
            public static final /* enum */ OptimizeMode LITE_RUNTIME;
            public static final int LITE_RUNTIME_VALUE = 3;
            public static final /* enum */ OptimizeMode SPEED;
            public static final int SPEED_VALUE = 1;
            private static final OptimizeMode[] VALUES;
            private static Internal.EnumLiteMap<OptimizeMode> internalValueMap;
            private final int index;
            private final int value;

            static {
                OptimizeMode optimizeMode;
                SPEED = new OptimizeMode(0, 1);
                CODE_SIZE = new OptimizeMode(1, 2);
                LITE_RUNTIME = optimizeMode = new OptimizeMode(2, 3);
                $VALUES = new OptimizeMode[]{SPEED, CODE_SIZE, optimizeMode};
                internalValueMap = new Internal.EnumLiteMap<OptimizeMode>(){

                    @Override
                    public OptimizeMode findValueByNumber(int n) {
                        return OptimizeMode.valueOf(n);
                    }
                };
                VALUES = OptimizeMode.values();
            }

            private OptimizeMode(int n2, int n3) {
                this.index = n2;
                this.value = n3;
            }

            public static final Descriptors.EnumDescriptor getDescriptor() {
                return FileOptions.getDescriptor().getEnumTypes().get(0);
            }

            public static Internal.EnumLiteMap<OptimizeMode> internalGetValueMap() {
                return internalValueMap;
            }

            public static OptimizeMode valueOf(int n) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            return null;
                        }
                        return LITE_RUNTIME;
                    }
                    return CODE_SIZE;
                }
                return SPEED;
            }

            public static OptimizeMode valueOf(Descriptors.EnumValueDescriptor enumValueDescriptor) {
                if (enumValueDescriptor.getType() == OptimizeMode.getDescriptor()) {
                    return VALUES[enumValueDescriptor.getIndex()];
                }
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }

            public static OptimizeMode valueOf(String string2) {
                return Enum.valueOf(OptimizeMode.class, string2);
            }

            public static OptimizeMode[] values() {
                return (OptimizeMode[])$VALUES.clone();
            }

            @Override
            public final Descriptors.EnumDescriptor getDescriptorForType() {
                return OptimizeMode.getDescriptor();
            }

            @Override
            public final int getNumber() {
                return this.value;
            }

            @Override
            public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                return OptimizeMode.getDescriptor().getValues().get(this.index);
            }

        }

    }

    public static interface FileOptionsOrBuilder
    extends GeneratedMessage.ExtendableMessageOrBuilder<FileOptions> {
        public boolean getCcGenericServices();

        public String getGoPackage();

        public ByteString getGoPackageBytes();

        public boolean getJavaGenerateEqualsAndHash();

        public boolean getJavaGenericServices();

        public boolean getJavaMultipleFiles();

        public String getJavaOuterClassname();

        public ByteString getJavaOuterClassnameBytes();

        public String getJavaPackage();

        public ByteString getJavaPackageBytes();

        public FileOptions.OptimizeMode getOptimizeFor();

        public boolean getPyGenericServices();

        public UninterpretedOption getUninterpretedOption(int var1);

        public int getUninterpretedOptionCount();

        public List<UninterpretedOption> getUninterpretedOptionList();

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();

        public boolean hasCcGenericServices();

        public boolean hasGoPackage();

        public boolean hasJavaGenerateEqualsAndHash();

        public boolean hasJavaGenericServices();

        public boolean hasJavaMultipleFiles();

        public boolean hasJavaOuterClassname();

        public boolean hasJavaPackage();

        public boolean hasOptimizeFor();

        public boolean hasPyGenericServices();
    }

    public static final class MessageOptions
    extends GeneratedMessage.ExtendableMessage<MessageOptions>
    implements MessageOptionsOrBuilder {
        public static final int MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER = 1;
        public static final int NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER = 2;
        public static Parser<MessageOptions> PARSER;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final MessageOptions defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private boolean messageSetWireFormat_;
        private boolean noStandardDescriptorAccessor_;
        private List<UninterpretedOption> uninterpretedOption_;
        private final UnknownFieldSet unknownFields;

        static {
            MessageOptions messageOptions;
            PARSER = new AbstractParser<MessageOptions>(){

                @Override
                public MessageOptions parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new MessageOptions(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = messageOptions = new MessageOptions(true);
            messageOptions.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private MessageOptions(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = 0;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = 0;
            while (var8_8 == 0) {
                block13 : {
                    block14 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl67
                        if (var10_14 == 8) ** GOTO lbl56
                        if (var10_14 == 16) ** GOTO lbl45
                        if (var10_14 == 7994) break block14;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                            var4_9 = 1;
                            var9_13 = var3_6;
                        }
                        ** GOTO lbl69
                    }
                    var4_9 = var3_6;
                    if ((var3_6 & 4) != 4) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.uninterpretedOption_ = new ArrayList<UninterpretedOption>();
                        var4_9 = var3_6 | 4;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.uninterpretedOption_.add(var1_1.readMessage(UninterpretedOption.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl45: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 2;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.noStandardDescriptorAccessor_ = var1_1.readBool();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl56: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 1;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.messageSetWireFormat_ = var1_1.readBool();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl67: // 1 sources:
                        var4_9 = 1;
                        var9_13 = var3_6;
lbl69: // 5 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block13;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if ((var5_10 & 4) == 4) {
                    this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if ((var3_6 & 4) == 4) {
                this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private MessageOptions(GeneratedMessage.ExtendableBuilder<MessageOptions, ?> extendableBuilder) {
            super(extendableBuilder);
            this.unknownFields = extendableBuilder.getUnknownFields();
        }

        private MessageOptions(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static MessageOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_MessageOptions_descriptor;
        }

        private void initFields() {
            this.messageSetWireFormat_ = false;
            this.noStandardDescriptorAccessor_ = false;
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(MessageOptions messageOptions) {
            return MessageOptions.newBuilder().mergeFrom(messageOptions);
        }

        public static MessageOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static MessageOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static MessageOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static MessageOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static MessageOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static MessageOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static MessageOptions parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static MessageOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static MessageOptions parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static MessageOptions parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public MessageOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public boolean getMessageSetWireFormat() {
            return this.messageSetWireFormat_;
        }

        @Override
        public boolean getNoStandardDescriptorAccessor() {
            return this.noStandardDescriptorAccessor_;
        }

        public Parser<MessageOptions> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeBoolSize(1, this.messageSetWireFormat_);
            }
            n = n2;
            if ((this.bitField0_ & 2) == 2) {
                n = n2 + CodedOutputStream.computeBoolSize(2, this.noStandardDescriptorAccessor_);
            }
            for (n2 = 0; n2 < this.uninterpretedOption_.size(); ++n2) {
                n += CodedOutputStream.computeMessageSize(999, this.uninterpretedOption_.get(n2));
            }
            this.memoizedSerializedSize = n = n + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public UninterpretedOption getUninterpretedOption(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        @Override
        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        @Override
        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasMessageSetWireFormat() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasNoStandardDescriptorAccessor() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_MessageOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(MessageOptions.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getUninterpretedOptionCount(); ++n) {
                if (this.getUninterpretedOption(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.extensionsAreInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return MessageOptions.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return MessageOptions.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            GeneratedMessage.ExtendableMessage.ExtensionWriter extensionWriter = this.newExtensionWriter();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBool(1, this.messageSetWireFormat_);
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBool(2, this.noStandardDescriptorAccessor_);
            }
            for (int i = 0; i < this.uninterpretedOption_.size(); ++i) {
                codedOutputStream.writeMessage(999, this.uninterpretedOption_.get(i));
            }
            extensionWriter.writeUntil(536870912, codedOutputStream);
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.ExtendableBuilder<MessageOptions, Builder>
        implements MessageOptionsOrBuilder {
            private int bitField0_;
            private boolean messageSetWireFormat_;
            private boolean noStandardDescriptorAccessor_;
            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.uninterpretedOption_ = new ArrayList<UninterpretedOption>(this.uninterpretedOption_);
                    this.bitField0_ |= 4;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_MessageOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    List<UninterpretedOption> list = this.uninterpretedOption_;
                    boolean bl = (this.bitField0_ & 4) == 4;
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    GeneratedMessage.ExtendableBuilder.addAll(iterable, this.uninterpretedOption_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, uninterpretedOption);
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(uninterpretedOption);
                return this;
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder() {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(n, UninterpretedOption.getDefaultInstance());
            }

            @Override
            public MessageOptions build() {
                MessageOptions messageOptions = this.buildPartial();
                if (messageOptions.isInitialized()) {
                    return messageOptions;
                }
                throw Builder.newUninitializedMessageException(messageOptions);
            }

            @Override
            public MessageOptions buildPartial() {
                MessageOptions messageOptions = new MessageOptions(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                messageOptions.messageSetWireFormat_ = this.messageSetWireFormat_;
                int n3 = n2;
                if ((n & 2) == 2) {
                    n3 = n2 | 2;
                }
                messageOptions.noStandardDescriptorAccessor_ = this.noStandardDescriptorAccessor_;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 4) == 4) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -5;
                    }
                    messageOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    messageOptions.uninterpretedOption_ = repeatedFieldBuilder.build();
                }
                messageOptions.bitField0_ = n3;
                this.onBuilt();
                return messageOptions;
            }

            @Override
            public Builder clear() {
                int n;
                super.clear();
                this.messageSetWireFormat_ = false;
                this.bitField0_ = n = this.bitField0_ & -2;
                this.noStandardDescriptorAccessor_ = false;
                this.bitField0_ = n & -3;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearMessageSetWireFormat() {
                this.bitField0_ &= -2;
                this.messageSetWireFormat_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearNoStandardDescriptorAccessor() {
                this.bitField0_ &= -3;
                this.noStandardDescriptorAccessor_ = false;
                this.onChanged();
                return this;
            }

            public Builder clearUninterpretedOption() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public MessageOptions getDefaultInstanceForType() {
                return MessageOptions.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_MessageOptions_descriptor;
            }

            @Override
            public boolean getMessageSetWireFormat() {
                return this.messageSetWireFormat_;
            }

            @Override
            public boolean getNoStandardDescriptorAccessor() {
                return this.noStandardDescriptorAccessor_;
            }

            @Override
            public UninterpretedOption getUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public UninterpretedOption.Builder getUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().getBuilder(n);
            }

            public List<UninterpretedOption.Builder> getUninterpretedOptionBuilderList() {
                return this.getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            @Override
            public int getUninterpretedOptionCount() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<UninterpretedOption> getUninterpretedOptionList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.uninterpretedOption_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.uninterpretedOption_);
            }

            @Override
            public boolean hasMessageSetWireFormat() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasNoStandardDescriptorAccessor() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_MessageOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(MessageOptions.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getUninterpretedOptionCount(); ++i) {
                    if (this.getUninterpretedOption(i).isInitialized()) continue;
                    return false;
                }
                if (!this.extensionsAreInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = MessageOptions.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((MessageOptions)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (MessageOptions)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((MessageOptions)object3);
                throw throwable222;
            }

            public Builder mergeFrom(MessageOptions messageOptions) {
                if (messageOptions == MessageOptions.getDefaultInstance()) {
                    return this;
                }
                if (messageOptions.hasMessageSetWireFormat()) {
                    this.setMessageSetWireFormat(messageOptions.getMessageSetWireFormat());
                }
                if (messageOptions.hasNoStandardDescriptorAccessor()) {
                    this.setNoStandardDescriptorAccessor(messageOptions.getNoStandardDescriptorAccessor());
                }
                if (this.uninterpretedOptionBuilder_ == null) {
                    if (!messageOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOption_.isEmpty()) {
                            this.uninterpretedOption_ = messageOptions.uninterpretedOption_;
                            this.bitField0_ &= -5;
                        } else {
                            this.ensureUninterpretedOptionIsMutable();
                            this.uninterpretedOption_.addAll(messageOptions.uninterpretedOption_);
                        }
                        this.onChanged();
                    }
                } else if (!messageOptions.uninterpretedOption_.isEmpty()) {
                    if (this.uninterpretedOptionBuilder_.isEmpty()) {
                        this.uninterpretedOptionBuilder_.dispose();
                        RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = null;
                        this.uninterpretedOptionBuilder_ = null;
                        this.uninterpretedOption_ = messageOptions.uninterpretedOption_;
                        this.bitField0_ &= -5;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getUninterpretedOptionFieldBuilder();
                        }
                        this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.uninterpretedOptionBuilder_.addAllMessages(messageOptions.uninterpretedOption_);
                    }
                }
                this.mergeExtensionFields(messageOptions);
                this.mergeUnknownFields(messageOptions.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof MessageOptions) {
                    return this.mergeFrom((MessageOptions)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setMessageSetWireFormat(boolean bl) {
                this.bitField0_ |= 1;
                this.messageSetWireFormat_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setNoStandardDescriptorAccessor(boolean bl) {
                this.bitField0_ |= 2;
                this.noStandardDescriptorAccessor_ = bl;
                this.onChanged();
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.set(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, uninterpretedOption);
                return this;
            }
        }

    }

    public static interface MessageOptionsOrBuilder
    extends GeneratedMessage.ExtendableMessageOrBuilder<MessageOptions> {
        public boolean getMessageSetWireFormat();

        public boolean getNoStandardDescriptorAccessor();

        public UninterpretedOption getUninterpretedOption(int var1);

        public int getUninterpretedOptionCount();

        public List<UninterpretedOption> getUninterpretedOptionList();

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();

        public boolean hasMessageSetWireFormat();

        public boolean hasNoStandardDescriptorAccessor();
    }

    public static final class MethodDescriptorProto
    extends GeneratedMessage
    implements MethodDescriptorProtoOrBuilder {
        public static final int INPUT_TYPE_FIELD_NUMBER = 2;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int OPTIONS_FIELD_NUMBER = 4;
        public static final int OUTPUT_TYPE_FIELD_NUMBER = 3;
        public static Parser<MethodDescriptorProto> PARSER;
        private static final MethodDescriptorProto defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private Object inputType_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private Object name_;
        private MethodOptions options_;
        private Object outputType_;
        private final UnknownFieldSet unknownFields;

        static {
            MethodDescriptorProto methodDescriptorProto;
            PARSER = new AbstractParser<MethodDescriptorProto>(){

                @Override
                public MethodDescriptorProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new MethodDescriptorProto(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = methodDescriptorProto = new MethodDescriptorProto(true);
            methodDescriptorProto.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private MethodDescriptorProto(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var6_6 = UnknownFieldSet.newBuilder();
            var3_7 = false;
            do {
                block10 : {
                    if (var3_7) {
                        this.unknownFields = var6_6.build();
                        this.makeExtensionsImmutable();
                        return;
                    }
                    var4_8 = var1_1.readTag();
                    if (var4_8 == 0) break block10;
                    if (var4_8 == 10) ** GOTO lbl38
                    if (var4_8 == 18) ** GOTO lbl35
                    if (var4_8 == 26) ** GOTO lbl32
                    if (var4_8 == 34) ** GOTO lbl23
                    if (this.parseUnknownField(var1_1, var6_6, var2_5, var4_8)) continue;
                    var3_7 = true;
                    continue;
lbl23: // 1 sources:
                    var5_9 = null;
                    if ((this.bitField0_ & 8) == 8) {
                        var5_9 = this.options_.toBuilder();
                    }
                    this.options_ = var7_10 = var1_1.readMessage(MethodOptions.PARSER, var2_5);
                    if (var5_9 != null) {
                        var5_9.mergeFrom(var7_10);
                        this.options_ = var5_9.buildPartial();
                    }
                    this.bitField0_ |= 8;
                    continue;
lbl32: // 1 sources:
                    this.bitField0_ |= 4;
                    this.outputType_ = var1_1.readBytes();
                    continue;
lbl35: // 1 sources:
                    this.bitField0_ |= 2;
                    this.inputType_ = var1_1.readBytes();
                    continue;
lbl38: // 1 sources:
                    this.bitField0_ |= 1;
                    this.name_ = var1_1.readBytes();
                    continue;
                }
                var3_7 = true;
                continue;
                break;
            } while (true);
            catch (Throwable var1_2) {
            }
            catch (IOException var1_3) {
                throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
            }
            catch (InvalidProtocolBufferException var1_4) {
                throw var1_4.setUnfinishedMessage(this);
            }
            this.unknownFields = var6_6.build();
            this.makeExtensionsImmutable();
            throw var1_2;
        }

        private MethodDescriptorProto(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private MethodDescriptorProto(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static MethodDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_MethodDescriptorProto_descriptor;
        }

        private void initFields() {
            this.name_ = "";
            this.inputType_ = "";
            this.outputType_ = "";
            this.options_ = MethodOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(MethodDescriptorProto methodDescriptorProto) {
            return MethodDescriptorProto.newBuilder().mergeFrom(methodDescriptorProto);
        }

        public static MethodDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static MethodDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static MethodDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static MethodDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static MethodDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static MethodDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static MethodDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static MethodDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static MethodDescriptorProto parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static MethodDescriptorProto parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public MethodDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public String getInputType() {
            Object object = this.inputType_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.inputType_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getInputTypeBytes() {
            Object object = this.inputType_;
            if (object instanceof String) {
                this.inputType_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public String getName() {
            Object object = this.name_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.name_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getNameBytes() {
            Object object = this.name_;
            if (object instanceof String) {
                this.name_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public MethodOptions getOptions() {
            return this.options_;
        }

        @Override
        public MethodOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        @Override
        public String getOutputType() {
            Object object = this.outputType_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.outputType_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getOutputTypeBytes() {
            Object object = this.outputType_;
            if (object instanceof String) {
                this.outputType_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        public Parser<MethodDescriptorProto> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }
            n = n2;
            if ((this.bitField0_ & 2) == 2) {
                n = n2 + CodedOutputStream.computeBytesSize(2, this.getInputTypeBytes());
            }
            n2 = n;
            if ((this.bitField0_ & 4) == 4) {
                n2 = n + CodedOutputStream.computeBytesSize(3, this.getOutputTypeBytes());
            }
            n = n2;
            if ((this.bitField0_ & 8) == 8) {
                n = n2 + CodedOutputStream.computeMessageSize(4, this.options_);
            }
            this.memoizedSerializedSize = n += this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasInputType() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasName() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOptions() {
            if ((this.bitField0_ & 8) == 8) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOutputType() {
            if ((this.bitField0_ & 4) == 4) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(MethodDescriptorProto.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            byte by = this.memoizedIsInitialized;
            boolean bl = false;
            if (by != -1) {
                if (by == 1) {
                    bl = true;
                }
                return bl;
            }
            if (this.hasOptions() && !this.getOptions().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return MethodDescriptorProto.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return MethodDescriptorProto.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, this.getNameBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, this.getInputTypeBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBytes(3, this.getOutputTypeBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeMessage(4, this.options_);
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements MethodDescriptorProtoOrBuilder {
            private int bitField0_;
            private Object inputType_ = "";
            private Object name_ = "";
            private SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> optionsBuilder_;
            private MethodOptions options_ = MethodOptions.getDefaultInstance();
            private Object outputType_ = "";

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_MethodDescriptorProto_descriptor;
            }

            private SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getOptionsFieldBuilder();
                }
            }

            @Override
            public MethodDescriptorProto build() {
                MethodDescriptorProto methodDescriptorProto = this.buildPartial();
                if (methodDescriptorProto.isInitialized()) {
                    return methodDescriptorProto;
                }
                throw Builder.newUninitializedMessageException(methodDescriptorProto);
            }

            @Override
            public MethodDescriptorProto buildPartial() {
                SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> singleFieldBuilder;
                MethodDescriptorProto methodDescriptorProto = new MethodDescriptorProto(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                methodDescriptorProto.name_ = this.name_;
                int n3 = n2;
                if ((n & 2) == 2) {
                    n3 = n2 | 2;
                }
                methodDescriptorProto.inputType_ = this.inputType_;
                n2 = n3;
                if ((n & 4) == 4) {
                    n2 = n3 | 4;
                }
                methodDescriptorProto.outputType_ = this.outputType_;
                n3 = n2;
                if ((n & 8) == 8) {
                    n3 = n2 | 8;
                }
                if ((singleFieldBuilder = this.optionsBuilder_) == null) {
                    methodDescriptorProto.options_ = this.options_;
                } else {
                    methodDescriptorProto.options_ = singleFieldBuilder.build();
                }
                methodDescriptorProto.bitField0_ = n3;
                this.onBuilt();
                return methodDescriptorProto;
            }

            @Override
            public Builder clear() {
                int n;
                super.clear();
                this.name_ = "";
                this.bitField0_ = n = this.bitField0_ & -2;
                this.inputType_ = "";
                this.bitField0_ = n &= -3;
                this.outputType_ = "";
                this.bitField0_ = n & -5;
                SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = MethodOptions.getDefaultInstance();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -9;
                return this;
            }

            public Builder clearInputType() {
                this.bitField0_ &= -3;
                this.inputType_ = MethodDescriptorProto.getDefaultInstance().getInputType();
                this.onChanged();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = MethodDescriptorProto.getDefaultInstance().getName();
                this.onChanged();
                return this;
            }

            public Builder clearOptions() {
                SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = MethodOptions.getDefaultInstance();
                    this.onChanged();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -9;
                return this;
            }

            public Builder clearOutputType() {
                this.bitField0_ &= -5;
                this.outputType_ = MethodDescriptorProto.getDefaultInstance().getOutputType();
                this.onChanged();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public MethodDescriptorProto getDefaultInstanceForType() {
                return MethodDescriptorProto.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_MethodDescriptorProto_descriptor;
            }

            @Override
            public String getInputType() {
                Object object = this.inputType_;
                if (!(object instanceof String)) {
                    this.inputType_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getInputTypeBytes() {
                Object object = this.inputType_;
                if (object instanceof String) {
                    this.inputType_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public String getName() {
                Object object = this.name_;
                if (!(object instanceof String)) {
                    this.name_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getNameBytes() {
                Object object = this.name_;
                if (object instanceof String) {
                    this.name_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public MethodOptions getOptions() {
                SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    return this.options_;
                }
                return singleFieldBuilder.getMessage();
            }

            public MethodOptions.Builder getOptionsBuilder() {
                this.bitField0_ |= 8;
                this.onChanged();
                return this.getOptionsFieldBuilder().getBuilder();
            }

            @Override
            public MethodOptionsOrBuilder getOptionsOrBuilder() {
                SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder != null) {
                    return singleFieldBuilder.getMessageOrBuilder();
                }
                return this.options_;
            }

            @Override
            public String getOutputType() {
                Object object = this.outputType_;
                if (!(object instanceof String)) {
                    this.outputType_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getOutputTypeBytes() {
                Object object = this.outputType_;
                if (object instanceof String) {
                    this.outputType_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public boolean hasInputType() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasName() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOptions() {
                if ((this.bitField0_ & 8) == 8) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOutputType() {
                if ((this.bitField0_ & 4) == 4) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(MethodDescriptorProto.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                if (this.hasOptions() && !this.getOptions().isInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = MethodDescriptorProto.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((MethodDescriptorProto)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (MethodDescriptorProto)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((MethodDescriptorProto)object3);
                throw throwable222;
            }

            public Builder mergeFrom(MethodDescriptorProto methodDescriptorProto) {
                if (methodDescriptorProto == MethodDescriptorProto.getDefaultInstance()) {
                    return this;
                }
                if (methodDescriptorProto.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = methodDescriptorProto.name_;
                    this.onChanged();
                }
                if (methodDescriptorProto.hasInputType()) {
                    this.bitField0_ |= 2;
                    this.inputType_ = methodDescriptorProto.inputType_;
                    this.onChanged();
                }
                if (methodDescriptorProto.hasOutputType()) {
                    this.bitField0_ |= 4;
                    this.outputType_ = methodDescriptorProto.outputType_;
                    this.onChanged();
                }
                if (methodDescriptorProto.hasOptions()) {
                    this.mergeOptions(methodDescriptorProto.getOptions());
                }
                this.mergeUnknownFields(methodDescriptorProto.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof MethodDescriptorProto) {
                    return this.mergeFrom((MethodDescriptorProto)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(MethodOptions methodOptions) {
                SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = (this.bitField0_ & 8) == 8 && this.options_ != MethodOptions.getDefaultInstance() ? MethodOptions.newBuilder(this.options_).mergeFrom(methodOptions).buildPartial() : methodOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.mergeFrom(methodOptions);
                }
                this.bitField0_ |= 8;
                return this;
            }

            public Builder setInputType(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 2;
                    this.inputType_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setInputTypeBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 2;
                    this.inputType_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setName(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 1;
                    this.name_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 1;
                    this.name_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setOptions(MethodOptions.Builder builder) {
                SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = builder.build();
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(builder.build());
                }
                this.bitField0_ |= 8;
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Builder setOptions(MethodOptions methodOptions) {
                SingleFieldBuilder<MethodOptions, MethodOptions.Builder, MethodOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    if (methodOptions == null) throw null;
                    this.options_ = methodOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(methodOptions);
                }
                this.bitField0_ |= 8;
                return this;
            }

            public Builder setOutputType(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 4;
                    this.outputType_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setOutputTypeBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 4;
                    this.outputType_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }
        }

    }

    public static interface MethodDescriptorProtoOrBuilder
    extends MessageOrBuilder {
        public String getInputType();

        public ByteString getInputTypeBytes();

        public String getName();

        public ByteString getNameBytes();

        public MethodOptions getOptions();

        public MethodOptionsOrBuilder getOptionsOrBuilder();

        public String getOutputType();

        public ByteString getOutputTypeBytes();

        public boolean hasInputType();

        public boolean hasName();

        public boolean hasOptions();

        public boolean hasOutputType();
    }

    public static final class MethodOptions
    extends GeneratedMessage.ExtendableMessage<MethodOptions>
    implements MethodOptionsOrBuilder {
        public static Parser<MethodOptions> PARSER;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final MethodOptions defaultInstance;
        private static final long serialVersionUID = 0L;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private List<UninterpretedOption> uninterpretedOption_;
        private final UnknownFieldSet unknownFields;

        static {
            MethodOptions methodOptions;
            PARSER = new AbstractParser<MethodOptions>(){

                @Override
                public MethodOptions parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new MethodOptions(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = methodOptions = new MethodOptions(true);
            methodOptions.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private MethodOptions(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = false;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = false;
            while (!var8_8) {
                block13 : {
                    block14 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl43
                        if (var10_14 == 7994) break block14;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                            var4_9 = true;
                            var9_13 = var3_6;
                        }
                        ** GOTO lbl45
                    }
                    var4_9 = var3_6;
                    if (!(var3_6 & true)) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.uninterpretedOption_ = new ArrayList<UninterpretedOption>();
                        var4_9 = var3_6 | true;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.uninterpretedOption_.add(var1_1.readMessage(UninterpretedOption.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl43: // 1 sources:
                        var4_9 = true;
                        var9_13 = var3_6;
lbl45: // 3 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block13;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if (var5_10 & true) {
                    this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if (var3_6 & true) {
                this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private MethodOptions(GeneratedMessage.ExtendableBuilder<MethodOptions, ?> extendableBuilder) {
            super(extendableBuilder);
            this.unknownFields = extendableBuilder.getUnknownFields();
        }

        private MethodOptions(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static MethodOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_MethodOptions_descriptor;
        }

        private void initFields() {
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(MethodOptions methodOptions) {
            return MethodOptions.newBuilder().mergeFrom(methodOptions);
        }

        public static MethodOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static MethodOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static MethodOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static MethodOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static MethodOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static MethodOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static MethodOptions parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static MethodOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static MethodOptions parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static MethodOptions parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public MethodOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public Parser<MethodOptions> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            for (n = 0; n < this.uninterpretedOption_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(999, this.uninterpretedOption_.get(n));
            }
            this.memoizedSerializedSize = n = n2 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public UninterpretedOption getUninterpretedOption(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        @Override
        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        @Override
        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_MethodOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(MethodOptions.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getUninterpretedOptionCount(); ++n) {
                if (this.getUninterpretedOption(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.extensionsAreInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return MethodOptions.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return MethodOptions.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            GeneratedMessage.ExtendableMessage.ExtensionWriter extensionWriter = this.newExtensionWriter();
            for (int i = 0; i < this.uninterpretedOption_.size(); ++i) {
                codedOutputStream.writeMessage(999, this.uninterpretedOption_.get(i));
            }
            extensionWriter.writeUntil(536870912, codedOutputStream);
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.ExtendableBuilder<MethodOptions, Builder>
        implements MethodOptionsOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.uninterpretedOption_ = new ArrayList<UninterpretedOption>(this.uninterpretedOption_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_MethodOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    List<UninterpretedOption> list = this.uninterpretedOption_;
                    int n = this.bitField0_;
                    boolean bl = true;
                    if ((n & 1) != 1) {
                        bl = false;
                    }
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    GeneratedMessage.ExtendableBuilder.addAll(iterable, this.uninterpretedOption_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, uninterpretedOption);
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(uninterpretedOption);
                return this;
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder() {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(n, UninterpretedOption.getDefaultInstance());
            }

            @Override
            public MethodOptions build() {
                MethodOptions methodOptions = this.buildPartial();
                if (methodOptions.isInitialized()) {
                    return methodOptions;
                }
                throw Builder.newUninitializedMessageException(methodOptions);
            }

            @Override
            public MethodOptions buildPartial() {
                MethodOptions methodOptions = new MethodOptions(this);
                int n = this.bitField0_;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -2;
                    }
                    methodOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    methodOptions.uninterpretedOption_ = repeatedFieldBuilder.build();
                }
                this.onBuilt();
                return methodOptions;
            }

            @Override
            public Builder clear() {
                super.clear();
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearUninterpretedOption() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public MethodOptions getDefaultInstanceForType() {
                return MethodOptions.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_MethodOptions_descriptor;
            }

            @Override
            public UninterpretedOption getUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public UninterpretedOption.Builder getUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().getBuilder(n);
            }

            public List<UninterpretedOption.Builder> getUninterpretedOptionBuilderList() {
                return this.getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            @Override
            public int getUninterpretedOptionCount() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<UninterpretedOption> getUninterpretedOptionList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.uninterpretedOption_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.uninterpretedOption_);
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_MethodOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(MethodOptions.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getUninterpretedOptionCount(); ++i) {
                    if (this.getUninterpretedOption(i).isInitialized()) continue;
                    return false;
                }
                if (!this.extensionsAreInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = MethodOptions.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((MethodOptions)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (MethodOptions)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((MethodOptions)object3);
                throw throwable222;
            }

            public Builder mergeFrom(MethodOptions methodOptions) {
                if (methodOptions == MethodOptions.getDefaultInstance()) {
                    return this;
                }
                if (this.uninterpretedOptionBuilder_ == null) {
                    if (!methodOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOption_.isEmpty()) {
                            this.uninterpretedOption_ = methodOptions.uninterpretedOption_;
                            this.bitField0_ &= -2;
                        } else {
                            this.ensureUninterpretedOptionIsMutable();
                            this.uninterpretedOption_.addAll(methodOptions.uninterpretedOption_);
                        }
                        this.onChanged();
                    }
                } else if (!methodOptions.uninterpretedOption_.isEmpty()) {
                    if (this.uninterpretedOptionBuilder_.isEmpty()) {
                        this.uninterpretedOptionBuilder_.dispose();
                        RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = null;
                        this.uninterpretedOptionBuilder_ = null;
                        this.uninterpretedOption_ = methodOptions.uninterpretedOption_;
                        this.bitField0_ &= -2;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getUninterpretedOptionFieldBuilder();
                        }
                        this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.uninterpretedOptionBuilder_.addAllMessages(methodOptions.uninterpretedOption_);
                    }
                }
                this.mergeExtensionFields(methodOptions);
                this.mergeUnknownFields(methodOptions.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof MethodOptions) {
                    return this.mergeFrom((MethodOptions)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.set(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, uninterpretedOption);
                return this;
            }
        }

    }

    public static interface MethodOptionsOrBuilder
    extends GeneratedMessage.ExtendableMessageOrBuilder<MethodOptions> {
        public UninterpretedOption getUninterpretedOption(int var1);

        public int getUninterpretedOptionCount();

        public List<UninterpretedOption> getUninterpretedOptionList();

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();
    }

    public static final class ServiceDescriptorProto
    extends GeneratedMessage
    implements ServiceDescriptorProtoOrBuilder {
        public static final int METHOD_FIELD_NUMBER = 2;
        public static final int NAME_FIELD_NUMBER = 1;
        public static final int OPTIONS_FIELD_NUMBER = 3;
        public static Parser<ServiceDescriptorProto> PARSER;
        private static final ServiceDescriptorProto defaultInstance;
        private static final long serialVersionUID = 0L;
        private int bitField0_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private List<MethodDescriptorProto> method_;
        private Object name_;
        private ServiceOptions options_;
        private final UnknownFieldSet unknownFields;

        static {
            ServiceDescriptorProto serviceDescriptorProto;
            PARSER = new AbstractParser<ServiceDescriptorProto>(){

                @Override
                public ServiceDescriptorProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new ServiceDescriptorProto(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = serviceDescriptorProto = new ServiceDescriptorProto(true);
            serviceDescriptorProto.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private ServiceDescriptorProto(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = 0;
            var12_7 = UnknownFieldSet.newBuilder();
            var8_8 = 0;
            while (var8_8 == 0) {
                block17 : {
                    block18 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl89
                        if (var10_14 == 10) ** GOTO lbl78
                        if (var10_14 == 18) break block18;
                        if (var10_14 != 26) {
                            var9_13 = var3_6;
                            var4_9 = var8_8;
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            if (!this.parseUnknownField(var1_1, var12_7, var2_5, var10_14)) {
                                var4_9 = 1;
                                var9_13 = var3_6;
                            }
                        } else {
                            var11_15 = null;
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            if ((this.bitField0_ & 2) == 2) {
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                var7_12 = var3_6;
                                var11_15 = this.options_.toBuilder();
                            }
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            var13_16 = var1_1.readMessage(ServiceOptions.PARSER, var2_5);
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            this.options_ = var13_16;
                            if (var11_15 != null) {
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                var7_12 = var3_6;
                                var11_15.mergeFrom(var13_16);
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                var7_12 = var3_6;
                                this.options_ = var11_15.buildPartial();
                            }
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            this.bitField0_ |= 2;
                            var9_13 = var3_6;
                            var4_9 = var8_8;
                        }
                        ** GOTO lbl91
                    }
                    var4_9 = var3_6;
                    if ((var3_6 & 2) != 2) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.method_ = new ArrayList<MethodDescriptorProto>();
                        var4_9 = var3_6 | 2;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.method_.add(var1_1.readMessage(MethodDescriptorProto.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl78: // 1 sources:
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.bitField0_ |= 1;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.name_ = var1_1.readBytes();
                        var9_13 = var3_6;
                        var4_9 = var8_8;
lbl89: // 1 sources:
                        var4_9 = 1;
                        var9_13 = var3_6;
lbl91: // 5 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block17;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if ((var5_10 & 2) == 2) {
                    this.method_ = Collections.unmodifiableList(this.method_);
                }
                this.unknownFields = var12_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if ((var3_6 & 2) == 2) {
                this.method_ = Collections.unmodifiableList(this.method_);
            }
            this.unknownFields = var12_7.build();
            this.makeExtensionsImmutable();
        }

        private ServiceDescriptorProto(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private ServiceDescriptorProto(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static ServiceDescriptorProto getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
        }

        private void initFields() {
            this.name_ = "";
            this.method_ = Collections.emptyList();
            this.options_ = ServiceOptions.getDefaultInstance();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(ServiceDescriptorProto serviceDescriptorProto) {
            return ServiceDescriptorProto.newBuilder().mergeFrom(serviceDescriptorProto);
        }

        public static ServiceDescriptorProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static ServiceDescriptorProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static ServiceDescriptorProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static ServiceDescriptorProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static ServiceDescriptorProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static ServiceDescriptorProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static ServiceDescriptorProto parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static ServiceDescriptorProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static ServiceDescriptorProto parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static ServiceDescriptorProto parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public ServiceDescriptorProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public MethodDescriptorProto getMethod(int n) {
            return this.method_.get(n);
        }

        @Override
        public int getMethodCount() {
            return this.method_.size();
        }

        @Override
        public List<MethodDescriptorProto> getMethodList() {
            return this.method_;
        }

        @Override
        public MethodDescriptorProtoOrBuilder getMethodOrBuilder(int n) {
            return this.method_.get(n);
        }

        @Override
        public List<? extends MethodDescriptorProtoOrBuilder> getMethodOrBuilderList() {
            return this.method_;
        }

        @Override
        public String getName() {
            Object object = this.name_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.name_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getNameBytes() {
            Object object = this.name_;
            if (object instanceof String) {
                this.name_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public ServiceOptions getOptions() {
            return this.options_;
        }

        @Override
        public ServiceOptionsOrBuilder getOptionsOrBuilder() {
            return this.options_;
        }

        public Parser<ServiceDescriptorProto> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n;
            int n2 = this.memoizedSerializedSize;
            if (n2 != -1) {
                return n2;
            }
            n2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                n2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }
            for (n = 0; n < this.method_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(2, this.method_.get(n));
            }
            n = n2;
            if ((this.bitField0_ & 2) == 2) {
                n = n2 + CodedOutputStream.computeMessageSize(3, this.options_);
            }
            this.memoizedSerializedSize = n2 = n + this.getUnknownFields().getSerializedSize();
            return n2;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasName() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasOptions() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(ServiceDescriptorProto.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getMethodCount(); ++n) {
                if (this.getMethod(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (this.hasOptions() && !this.getOptions().isInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return ServiceDescriptorProto.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return ServiceDescriptorProto.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, this.getNameBytes());
            }
            for (int i = 0; i < this.method_.size(); ++i) {
                codedOutputStream.writeMessage(2, this.method_.get(i));
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeMessage(3, this.options_);
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements ServiceDescriptorProtoOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> methodBuilder_;
            private List<MethodDescriptorProto> method_ = Collections.emptyList();
            private Object name_ = "";
            private SingleFieldBuilder<ServiceOptions, ServiceOptions.Builder, ServiceOptionsOrBuilder> optionsBuilder_;
            private ServiceOptions options_ = ServiceOptions.getDefaultInstance();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureMethodIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.method_ = new ArrayList<MethodDescriptorProto>(this.method_);
                    this.bitField0_ |= 2;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
            }

            private RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> getMethodFieldBuilder() {
                if (this.methodBuilder_ == null) {
                    List<MethodDescriptorProto> list = this.method_;
                    boolean bl = (this.bitField0_ & 2) == 2;
                    this.methodBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.method_ = null;
                }
                return this.methodBuilder_;
            }

            private SingleFieldBuilder<ServiceOptions, ServiceOptions.Builder, ServiceOptionsOrBuilder> getOptionsFieldBuilder() {
                if (this.optionsBuilder_ == null) {
                    this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
                    this.options_ = null;
                }
                return this.optionsBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getMethodFieldBuilder();
                    this.getOptionsFieldBuilder();
                }
            }

            public Builder addAllMethod(Iterable<? extends MethodDescriptorProto> iterable) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMethodIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.method_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addMethod(int n, MethodDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMethodIsMutable();
                    this.method_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addMethod(int n, MethodDescriptorProto methodDescriptorProto) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (methodDescriptorProto != null) {
                        this.ensureMethodIsMutable();
                        this.method_.add(n, methodDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, methodDescriptorProto);
                return this;
            }

            public Builder addMethod(MethodDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMethodIsMutable();
                    this.method_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addMethod(MethodDescriptorProto methodDescriptorProto) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (methodDescriptorProto != null) {
                        this.ensureMethodIsMutable();
                        this.method_.add(methodDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(methodDescriptorProto);
                return this;
            }

            public MethodDescriptorProto.Builder addMethodBuilder() {
                return this.getMethodFieldBuilder().addBuilder(MethodDescriptorProto.getDefaultInstance());
            }

            public MethodDescriptorProto.Builder addMethodBuilder(int n) {
                return this.getMethodFieldBuilder().addBuilder(n, MethodDescriptorProto.getDefaultInstance());
            }

            @Override
            public ServiceDescriptorProto build() {
                ServiceDescriptorProto serviceDescriptorProto = this.buildPartial();
                if (serviceDescriptorProto.isInitialized()) {
                    return serviceDescriptorProto;
                }
                throw Builder.newUninitializedMessageException(serviceDescriptorProto);
            }

            @Override
            public ServiceDescriptorProto buildPartial() {
                ServiceDescriptorProto serviceDescriptorProto = new ServiceDescriptorProto(this);
                int n = this.bitField0_;
                int n2 = 0;
                if ((n & 1) == 1) {
                    n2 = false | true;
                }
                serviceDescriptorProto.name_ = this.name_;
                GeneratedMessage.BuilderParent builderParent = this.methodBuilder_;
                if (builderParent == null) {
                    if ((this.bitField0_ & 2) == 2) {
                        this.method_ = Collections.unmodifiableList(this.method_);
                        this.bitField0_ &= -3;
                    }
                    serviceDescriptorProto.method_ = this.method_;
                } else {
                    serviceDescriptorProto.method_ = builderParent.build();
                }
                int n3 = n2;
                if ((n & 4) == 4) {
                    n3 = n2 | 2;
                }
                if ((builderParent = this.optionsBuilder_) == null) {
                    serviceDescriptorProto.options_ = this.options_;
                } else {
                    serviceDescriptorProto.options_ = (ServiceOptions)((Object)builderParent.build());
                }
                serviceDescriptorProto.bitField0_ = n3;
                this.onBuilt();
                return serviceDescriptorProto;
            }

            @Override
            public Builder clear() {
                super.clear();
                this.name_ = "";
                this.bitField0_ &= -2;
                GeneratedMessage.BuilderParent builderParent = this.methodBuilder_;
                if (builderParent == null) {
                    this.method_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    builderParent.clear();
                }
                builderParent = this.optionsBuilder_;
                if (builderParent == null) {
                    this.options_ = ServiceOptions.getDefaultInstance();
                } else {
                    builderParent.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clearMethod() {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.method_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = ServiceDescriptorProto.getDefaultInstance().getName();
                this.onChanged();
                return this;
            }

            public Builder clearOptions() {
                SingleFieldBuilder<ServiceOptions, ServiceOptions.Builder, ServiceOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = ServiceOptions.getDefaultInstance();
                    this.onChanged();
                } else {
                    singleFieldBuilder.clear();
                }
                this.bitField0_ &= -5;
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public ServiceDescriptorProto getDefaultInstanceForType() {
                return ServiceDescriptorProto.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
            }

            @Override
            public MethodDescriptorProto getMethod(int n) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.method_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public MethodDescriptorProto.Builder getMethodBuilder(int n) {
                return this.getMethodFieldBuilder().getBuilder(n);
            }

            public List<MethodDescriptorProto.Builder> getMethodBuilderList() {
                return this.getMethodFieldBuilder().getBuilderList();
            }

            @Override
            public int getMethodCount() {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.method_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<MethodDescriptorProto> getMethodList() {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.method_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public MethodDescriptorProtoOrBuilder getMethodOrBuilder(int n) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.method_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends MethodDescriptorProtoOrBuilder> getMethodOrBuilderList() {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.method_);
            }

            @Override
            public String getName() {
                Object object = this.name_;
                if (!(object instanceof String)) {
                    this.name_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getNameBytes() {
                Object object = this.name_;
                if (object instanceof String) {
                    this.name_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public ServiceOptions getOptions() {
                SingleFieldBuilder<ServiceOptions, ServiceOptions.Builder, ServiceOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    return this.options_;
                }
                return singleFieldBuilder.getMessage();
            }

            public ServiceOptions.Builder getOptionsBuilder() {
                this.bitField0_ |= 4;
                this.onChanged();
                return this.getOptionsFieldBuilder().getBuilder();
            }

            @Override
            public ServiceOptionsOrBuilder getOptionsOrBuilder() {
                SingleFieldBuilder<ServiceOptions, ServiceOptions.Builder, ServiceOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder != null) {
                    return singleFieldBuilder.getMessageOrBuilder();
                }
                return this.options_;
            }

            @Override
            public boolean hasName() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasOptions() {
                if ((this.bitField0_ & 4) == 4) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(ServiceDescriptorProto.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getMethodCount(); ++i) {
                    if (this.getMethod(i).isInitialized()) continue;
                    return false;
                }
                if (this.hasOptions() && !this.getOptions().isInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = ServiceDescriptorProto.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((ServiceDescriptorProto)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (ServiceDescriptorProto)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((ServiceDescriptorProto)object3);
                throw throwable222;
            }

            public Builder mergeFrom(ServiceDescriptorProto serviceDescriptorProto) {
                if (serviceDescriptorProto == ServiceDescriptorProto.getDefaultInstance()) {
                    return this;
                }
                if (serviceDescriptorProto.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = serviceDescriptorProto.name_;
                    this.onChanged();
                }
                if (this.methodBuilder_ == null) {
                    if (!serviceDescriptorProto.method_.isEmpty()) {
                        if (this.method_.isEmpty()) {
                            this.method_ = serviceDescriptorProto.method_;
                            this.bitField0_ &= -3;
                        } else {
                            this.ensureMethodIsMutable();
                            this.method_.addAll(serviceDescriptorProto.method_);
                        }
                        this.onChanged();
                    }
                } else if (!serviceDescriptorProto.method_.isEmpty()) {
                    if (this.methodBuilder_.isEmpty()) {
                        this.methodBuilder_.dispose();
                        RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = null;
                        this.methodBuilder_ = null;
                        this.method_ = serviceDescriptorProto.method_;
                        this.bitField0_ &= -3;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getMethodFieldBuilder();
                        }
                        this.methodBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.methodBuilder_.addAllMessages(serviceDescriptorProto.method_);
                    }
                }
                if (serviceDescriptorProto.hasOptions()) {
                    this.mergeOptions(serviceDescriptorProto.getOptions());
                }
                this.mergeUnknownFields(serviceDescriptorProto.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof ServiceDescriptorProto) {
                    return this.mergeFrom((ServiceDescriptorProto)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder mergeOptions(ServiceOptions serviceOptions) {
                SingleFieldBuilder<ServiceOptions, ServiceOptions.Builder, ServiceOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = (this.bitField0_ & 4) == 4 && this.options_ != ServiceOptions.getDefaultInstance() ? ServiceOptions.newBuilder(this.options_).mergeFrom(serviceOptions).buildPartial() : serviceOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.mergeFrom(serviceOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }

            public Builder removeMethod(int n) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMethodIsMutable();
                    this.method_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setMethod(int n, MethodDescriptorProto.Builder builder) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureMethodIsMutable();
                    this.method_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setMethod(int n, MethodDescriptorProto methodDescriptorProto) {
                RepeatedFieldBuilder<MethodDescriptorProto, MethodDescriptorProto.Builder, MethodDescriptorProtoOrBuilder> repeatedFieldBuilder = this.methodBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (methodDescriptorProto != null) {
                        this.ensureMethodIsMutable();
                        this.method_.set(n, methodDescriptorProto);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, methodDescriptorProto);
                return this;
            }

            public Builder setName(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 1;
                    this.name_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setNameBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 1;
                    this.name_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setOptions(ServiceOptions.Builder builder) {
                SingleFieldBuilder<ServiceOptions, ServiceOptions.Builder, ServiceOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    this.options_ = builder.build();
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(builder.build());
                }
                this.bitField0_ |= 4;
                return this;
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public Builder setOptions(ServiceOptions serviceOptions) {
                SingleFieldBuilder<ServiceOptions, ServiceOptions.Builder, ServiceOptionsOrBuilder> singleFieldBuilder = this.optionsBuilder_;
                if (singleFieldBuilder == null) {
                    if (serviceOptions == null) throw null;
                    this.options_ = serviceOptions;
                    this.onChanged();
                } else {
                    singleFieldBuilder.setMessage(serviceOptions);
                }
                this.bitField0_ |= 4;
                return this;
            }
        }

    }

    public static interface ServiceDescriptorProtoOrBuilder
    extends MessageOrBuilder {
        public MethodDescriptorProto getMethod(int var1);

        public int getMethodCount();

        public List<MethodDescriptorProto> getMethodList();

        public MethodDescriptorProtoOrBuilder getMethodOrBuilder(int var1);

        public List<? extends MethodDescriptorProtoOrBuilder> getMethodOrBuilderList();

        public String getName();

        public ByteString getNameBytes();

        public ServiceOptions getOptions();

        public ServiceOptionsOrBuilder getOptionsOrBuilder();

        public boolean hasName();

        public boolean hasOptions();
    }

    public static final class ServiceOptions
    extends GeneratedMessage.ExtendableMessage<ServiceOptions>
    implements ServiceOptionsOrBuilder {
        public static Parser<ServiceOptions> PARSER;
        public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
        private static final ServiceOptions defaultInstance;
        private static final long serialVersionUID = 0L;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private List<UninterpretedOption> uninterpretedOption_;
        private final UnknownFieldSet unknownFields;

        static {
            ServiceOptions serviceOptions;
            PARSER = new AbstractParser<ServiceOptions>(){

                @Override
                public ServiceOptions parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new ServiceOptions(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = serviceOptions = new ServiceOptions(true);
            serviceOptions.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private ServiceOptions(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = false;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = false;
            while (!var8_8) {
                block13 : {
                    block14 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl43
                        if (var10_14 == 7994) break block14;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                            var4_9 = true;
                            var9_13 = var3_6;
                        }
                        ** GOTO lbl45
                    }
                    var4_9 = var3_6;
                    if (!(var3_6 & true)) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.uninterpretedOption_ = new ArrayList<UninterpretedOption>();
                        var4_9 = var3_6 | true;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.uninterpretedOption_.add(var1_1.readMessage(UninterpretedOption.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl43: // 1 sources:
                        var4_9 = true;
                        var9_13 = var3_6;
lbl45: // 3 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block13;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if (var5_10 & true) {
                    this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if (var3_6 & true) {
                this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private ServiceOptions(GeneratedMessage.ExtendableBuilder<ServiceOptions, ?> extendableBuilder) {
            super(extendableBuilder);
            this.unknownFields = extendableBuilder.getUnknownFields();
        }

        private ServiceOptions(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static ServiceOptions getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_ServiceOptions_descriptor;
        }

        private void initFields() {
            this.uninterpretedOption_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(ServiceOptions serviceOptions) {
            return ServiceOptions.newBuilder().mergeFrom(serviceOptions);
        }

        public static ServiceOptions parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static ServiceOptions parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static ServiceOptions parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static ServiceOptions parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static ServiceOptions parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static ServiceOptions parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static ServiceOptions parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static ServiceOptions parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static ServiceOptions parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static ServiceOptions parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public ServiceOptions getDefaultInstanceForType() {
            return defaultInstance;
        }

        public Parser<ServiceOptions> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            for (n = 0; n < this.uninterpretedOption_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(999, this.uninterpretedOption_.get(n));
            }
            this.memoizedSerializedSize = n = n2 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public UninterpretedOption getUninterpretedOption(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public int getUninterpretedOptionCount() {
            return this.uninterpretedOption_.size();
        }

        @Override
        public List<UninterpretedOption> getUninterpretedOptionList() {
            return this.uninterpretedOption_;
        }

        @Override
        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
            return this.uninterpretedOption_.get(n);
        }

        @Override
        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
            return this.uninterpretedOption_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_ServiceOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(ServiceOptions.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getUninterpretedOptionCount(); ++n) {
                if (this.getUninterpretedOption(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            if (!this.extensionsAreInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return ServiceOptions.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return ServiceOptions.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            GeneratedMessage.ExtendableMessage.ExtensionWriter extensionWriter = this.newExtensionWriter();
            for (int i = 0; i < this.uninterpretedOption_.size(); ++i) {
                codedOutputStream.writeMessage(999, this.uninterpretedOption_.get(i));
            }
            extensionWriter.writeUntil(536870912, codedOutputStream);
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.ExtendableBuilder<ServiceOptions, Builder>
        implements ServiceOptionsOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;
            private List<UninterpretedOption> uninterpretedOption_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureUninterpretedOptionIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.uninterpretedOption_ = new ArrayList<UninterpretedOption>(this.uninterpretedOption_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_ServiceOptions_descriptor;
            }

            private RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> getUninterpretedOptionFieldBuilder() {
                if (this.uninterpretedOptionBuilder_ == null) {
                    List<UninterpretedOption> list = this.uninterpretedOption_;
                    int n = this.bitField0_;
                    boolean bl = true;
                    if ((n & 1) != 1) {
                        bl = false;
                    }
                    this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.uninterpretedOption_ = null;
                }
                return this.uninterpretedOptionBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getUninterpretedOptionFieldBuilder();
                }
            }

            public Builder addAllUninterpretedOption(Iterable<? extends UninterpretedOption> iterable) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    GeneratedMessage.ExtendableBuilder.addAll(iterable, this.uninterpretedOption_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, uninterpretedOption);
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addUninterpretedOption(UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.add(uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(uninterpretedOption);
                return this;
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder() {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(UninterpretedOption.getDefaultInstance());
            }

            public UninterpretedOption.Builder addUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().addBuilder(n, UninterpretedOption.getDefaultInstance());
            }

            @Override
            public ServiceOptions build() {
                ServiceOptions serviceOptions = this.buildPartial();
                if (serviceOptions.isInitialized()) {
                    return serviceOptions;
                }
                throw Builder.newUninitializedMessageException(serviceOptions);
            }

            @Override
            public ServiceOptions buildPartial() {
                ServiceOptions serviceOptions = new ServiceOptions(this);
                int n = this.bitField0_;
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                        this.bitField0_ &= -2;
                    }
                    serviceOptions.uninterpretedOption_ = this.uninterpretedOption_;
                } else {
                    serviceOptions.uninterpretedOption_ = repeatedFieldBuilder.build();
                }
                this.onBuilt();
                return serviceOptions;
            }

            @Override
            public Builder clear() {
                super.clear();
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearUninterpretedOption() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.uninterpretedOption_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public ServiceOptions getDefaultInstanceForType() {
                return ServiceOptions.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_ServiceOptions_descriptor;
            }

            @Override
            public UninterpretedOption getUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public UninterpretedOption.Builder getUninterpretedOptionBuilder(int n) {
                return this.getUninterpretedOptionFieldBuilder().getBuilder(n);
            }

            public List<UninterpretedOption.Builder> getUninterpretedOptionBuilderList() {
                return this.getUninterpretedOptionFieldBuilder().getBuilderList();
            }

            @Override
            public int getUninterpretedOptionCount() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<UninterpretedOption> getUninterpretedOptionList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.uninterpretedOption_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.uninterpretedOption_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList() {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.uninterpretedOption_);
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_ServiceOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(ServiceOptions.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getUninterpretedOptionCount(); ++i) {
                    if (this.getUninterpretedOption(i).isInitialized()) continue;
                    return false;
                }
                if (!this.extensionsAreInitialized()) {
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = ServiceOptions.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((ServiceOptions)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (ServiceOptions)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((ServiceOptions)object3);
                throw throwable222;
            }

            public Builder mergeFrom(ServiceOptions serviceOptions) {
                if (serviceOptions == ServiceOptions.getDefaultInstance()) {
                    return this;
                }
                if (this.uninterpretedOptionBuilder_ == null) {
                    if (!serviceOptions.uninterpretedOption_.isEmpty()) {
                        if (this.uninterpretedOption_.isEmpty()) {
                            this.uninterpretedOption_ = serviceOptions.uninterpretedOption_;
                            this.bitField0_ &= -2;
                        } else {
                            this.ensureUninterpretedOptionIsMutable();
                            this.uninterpretedOption_.addAll(serviceOptions.uninterpretedOption_);
                        }
                        this.onChanged();
                    }
                } else if (!serviceOptions.uninterpretedOption_.isEmpty()) {
                    if (this.uninterpretedOptionBuilder_.isEmpty()) {
                        this.uninterpretedOptionBuilder_.dispose();
                        RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = null;
                        this.uninterpretedOptionBuilder_ = null;
                        this.uninterpretedOption_ = serviceOptions.uninterpretedOption_;
                        this.bitField0_ &= -2;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getUninterpretedOptionFieldBuilder();
                        }
                        this.uninterpretedOptionBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.uninterpretedOptionBuilder_.addAllMessages(serviceOptions.uninterpretedOption_);
                    }
                }
                this.mergeExtensionFields(serviceOptions);
                this.mergeUnknownFields(serviceOptions.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof ServiceOptions) {
                    return this.mergeFrom((ServiceOptions)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeUninterpretedOption(int n) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption.Builder builder) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureUninterpretedOptionIsMutable();
                    this.uninterpretedOption_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setUninterpretedOption(int n, UninterpretedOption uninterpretedOption) {
                RepeatedFieldBuilder<UninterpretedOption, UninterpretedOption.Builder, UninterpretedOptionOrBuilder> repeatedFieldBuilder = this.uninterpretedOptionBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (uninterpretedOption != null) {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.set(n, uninterpretedOption);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, uninterpretedOption);
                return this;
            }
        }

    }

    public static interface ServiceOptionsOrBuilder
    extends GeneratedMessage.ExtendableMessageOrBuilder<ServiceOptions> {
        public UninterpretedOption getUninterpretedOption(int var1);

        public int getUninterpretedOptionCount();

        public List<UninterpretedOption> getUninterpretedOptionList();

        public UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

        public List<? extends UninterpretedOptionOrBuilder> getUninterpretedOptionOrBuilderList();
    }

    public static final class SourceCodeInfo
    extends GeneratedMessage
    implements SourceCodeInfoOrBuilder {
        public static final int LOCATION_FIELD_NUMBER = 1;
        public static Parser<SourceCodeInfo> PARSER;
        private static final SourceCodeInfo defaultInstance;
        private static final long serialVersionUID = 0L;
        private List<Location> location_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private final UnknownFieldSet unknownFields;

        static {
            SourceCodeInfo sourceCodeInfo;
            PARSER = new AbstractParser<SourceCodeInfo>(){

                @Override
                public SourceCodeInfo parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new SourceCodeInfo(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = sourceCodeInfo = new SourceCodeInfo(true);
            sourceCodeInfo.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private SourceCodeInfo(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = false;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = false;
            while (!var8_8) {
                block13 : {
                    block14 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl43
                        if (var10_14 == 10) break block14;
                        var9_13 = var3_6;
                        var4_9 = var8_8;
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                            var4_9 = true;
                            var9_13 = var3_6;
                        }
                        ** GOTO lbl45
                    }
                    var4_9 = var3_6;
                    if (!(var3_6 & true)) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.location_ = new ArrayList<Location>();
                        var4_9 = var3_6 | true;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.location_.add(var1_1.readMessage(Location.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl43: // 1 sources:
                        var4_9 = true;
                        var9_13 = var3_6;
lbl45: // 3 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block13;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if (var5_10 & true) {
                    this.location_ = Collections.unmodifiableList(this.location_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if (var3_6 & true) {
                this.location_ = Collections.unmodifiableList(this.location_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private SourceCodeInfo(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private SourceCodeInfo(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static SourceCodeInfo getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_SourceCodeInfo_descriptor;
        }

        private void initFields() {
            this.location_ = Collections.emptyList();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(SourceCodeInfo sourceCodeInfo) {
            return SourceCodeInfo.newBuilder().mergeFrom(sourceCodeInfo);
        }

        public static SourceCodeInfo parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static SourceCodeInfo parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static SourceCodeInfo parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static SourceCodeInfo parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static SourceCodeInfo parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static SourceCodeInfo parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static SourceCodeInfo parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static SourceCodeInfo parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static SourceCodeInfo parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static SourceCodeInfo parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public SourceCodeInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public Location getLocation(int n) {
            return this.location_.get(n);
        }

        @Override
        public int getLocationCount() {
            return this.location_.size();
        }

        @Override
        public List<Location> getLocationList() {
            return this.location_;
        }

        @Override
        public LocationOrBuilder getLocationOrBuilder(int n) {
            return this.location_.get(n);
        }

        @Override
        public List<? extends LocationOrBuilder> getLocationOrBuilderList() {
            return this.location_;
        }

        public Parser<SourceCodeInfo> getParserForType() {
            return PARSER;
        }

        @Override
        public int getSerializedSize() {
            int n = this.memoizedSerializedSize;
            if (n != -1) {
                return n;
            }
            int n2 = 0;
            for (n = 0; n < this.location_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(1, this.location_.get(n));
            }
            this.memoizedSerializedSize = n = n2 + this.getUnknownFields().getSerializedSize();
            return n;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable.ensureFieldAccessorsInitialized(SourceCodeInfo.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            byte by = this.memoizedIsInitialized;
            if (by != -1) {
                if (by == 1) {
                    return true;
                }
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return SourceCodeInfo.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return SourceCodeInfo.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            for (int i = 0; i < this.location_.size(); ++i) {
                codedOutputStream.writeMessage(1, this.location_.get(i));
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements SourceCodeInfoOrBuilder {
            private int bitField0_;
            private RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> locationBuilder_;
            private List<Location> location_ = Collections.emptyList();

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureLocationIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.location_ = new ArrayList<Location>(this.location_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_SourceCodeInfo_descriptor;
            }

            private RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> getLocationFieldBuilder() {
                if (this.locationBuilder_ == null) {
                    List<Location> list = this.location_;
                    int n = this.bitField0_;
                    boolean bl = true;
                    if ((n & 1) != 1) {
                        bl = false;
                    }
                    this.locationBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.location_ = null;
                }
                return this.locationBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getLocationFieldBuilder();
                }
            }

            public Builder addAllLocation(Iterable<? extends Location> iterable) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureLocationIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.location_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addLocation(int n, Location.Builder builder) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureLocationIsMutable();
                    this.location_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addLocation(int n, Location location) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (location != null) {
                        this.ensureLocationIsMutable();
                        this.location_.add(n, location);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, location);
                return this;
            }

            public Builder addLocation(Location.Builder builder) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureLocationIsMutable();
                    this.location_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addLocation(Location location) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (location != null) {
                        this.ensureLocationIsMutable();
                        this.location_.add(location);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(location);
                return this;
            }

            public Location.Builder addLocationBuilder() {
                return this.getLocationFieldBuilder().addBuilder(Location.getDefaultInstance());
            }

            public Location.Builder addLocationBuilder(int n) {
                return this.getLocationFieldBuilder().addBuilder(n, Location.getDefaultInstance());
            }

            @Override
            public SourceCodeInfo build() {
                SourceCodeInfo sourceCodeInfo = this.buildPartial();
                if (sourceCodeInfo.isInitialized()) {
                    return sourceCodeInfo;
                }
                throw Builder.newUninitializedMessageException(sourceCodeInfo);
            }

            @Override
            public SourceCodeInfo buildPartial() {
                SourceCodeInfo sourceCodeInfo = new SourceCodeInfo(this);
                int n = this.bitField0_;
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.location_ = Collections.unmodifiableList(this.location_);
                        this.bitField0_ &= -2;
                    }
                    sourceCodeInfo.location_ = this.location_;
                } else {
                    sourceCodeInfo.location_ = repeatedFieldBuilder.build();
                }
                this.onBuilt();
                return sourceCodeInfo;
            }

            @Override
            public Builder clear() {
                super.clear();
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.location_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearLocation() {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.location_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public SourceCodeInfo getDefaultInstanceForType() {
                return SourceCodeInfo.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_SourceCodeInfo_descriptor;
            }

            @Override
            public Location getLocation(int n) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.location_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public Location.Builder getLocationBuilder(int n) {
                return this.getLocationFieldBuilder().getBuilder(n);
            }

            public List<Location.Builder> getLocationBuilderList() {
                return this.getLocationFieldBuilder().getBuilderList();
            }

            @Override
            public int getLocationCount() {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.location_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<Location> getLocationList() {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.location_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public LocationOrBuilder getLocationOrBuilder(int n) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.location_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends LocationOrBuilder> getLocationOrBuilderList() {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.location_);
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable.ensureFieldAccessorsInitialized(SourceCodeInfo.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = SourceCodeInfo.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((SourceCodeInfo)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (SourceCodeInfo)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((SourceCodeInfo)object3);
                throw throwable222;
            }

            public Builder mergeFrom(SourceCodeInfo sourceCodeInfo) {
                if (sourceCodeInfo == SourceCodeInfo.getDefaultInstance()) {
                    return this;
                }
                if (this.locationBuilder_ == null) {
                    if (!sourceCodeInfo.location_.isEmpty()) {
                        if (this.location_.isEmpty()) {
                            this.location_ = sourceCodeInfo.location_;
                            this.bitField0_ &= -2;
                        } else {
                            this.ensureLocationIsMutable();
                            this.location_.addAll(sourceCodeInfo.location_);
                        }
                        this.onChanged();
                    }
                } else if (!sourceCodeInfo.location_.isEmpty()) {
                    if (this.locationBuilder_.isEmpty()) {
                        this.locationBuilder_.dispose();
                        RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = null;
                        this.locationBuilder_ = null;
                        this.location_ = sourceCodeInfo.location_;
                        this.bitField0_ &= -2;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getLocationFieldBuilder();
                        }
                        this.locationBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.locationBuilder_.addAllMessages(sourceCodeInfo.location_);
                    }
                }
                this.mergeUnknownFields(sourceCodeInfo.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof SourceCodeInfo) {
                    return this.mergeFrom((SourceCodeInfo)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeLocation(int n) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureLocationIsMutable();
                    this.location_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setLocation(int n, Location.Builder builder) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureLocationIsMutable();
                    this.location_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setLocation(int n, Location location) {
                RepeatedFieldBuilder<Location, Location.Builder, LocationOrBuilder> repeatedFieldBuilder = this.locationBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (location != null) {
                        this.ensureLocationIsMutable();
                        this.location_.set(n, location);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, location);
                return this;
            }
        }

        public static final class Location
        extends GeneratedMessage
        implements LocationOrBuilder {
            public static final int LEADING_COMMENTS_FIELD_NUMBER = 3;
            public static Parser<Location> PARSER;
            public static final int PATH_FIELD_NUMBER = 1;
            public static final int SPAN_FIELD_NUMBER = 2;
            public static final int TRAILING_COMMENTS_FIELD_NUMBER = 4;
            private static final Location defaultInstance;
            private static final long serialVersionUID = 0L;
            private int bitField0_;
            private Object leadingComments_;
            private byte memoizedIsInitialized;
            private int memoizedSerializedSize;
            private int pathMemoizedSerializedSize;
            private List<Integer> path_;
            private int spanMemoizedSerializedSize;
            private List<Integer> span_;
            private Object trailingComments_;
            private final UnknownFieldSet unknownFields;

            static {
                Location location;
                PARSER = new AbstractParser<Location>(){

                    @Override
                    public Location parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                        return new Location(codedInputStream, extensionRegistryLite);
                    }
                };
                defaultInstance = location = new Location(true);
                location.initFields();
            }

            /*
             * Exception decompiling
             */
            private Location(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
                // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
                // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[TRYBLOCK]], but top level block is 24[SIMPLE_IF_TAKEN]
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
                // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:682)
                // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:765)
                // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
                // org.benf.cfr.reader.Main.doJar(Main.java:134)
                // org.benf.cfr.reader.Main.main(Main.java:189)
                throw new IllegalStateException("Decompilation failed");
            }

            private Location(GeneratedMessage.Builder<?> builder) {
                super(builder);
                this.pathMemoizedSerializedSize = -1;
                this.spanMemoizedSerializedSize = -1;
                this.memoizedIsInitialized = (byte)-1;
                this.memoizedSerializedSize = -1;
                this.unknownFields = builder.getUnknownFields();
            }

            private Location(boolean bl) {
                this.pathMemoizedSerializedSize = -1;
                this.spanMemoizedSerializedSize = -1;
                this.memoizedIsInitialized = (byte)-1;
                this.memoizedSerializedSize = -1;
                this.unknownFields = UnknownFieldSet.getDefaultInstance();
            }

            public static Location getDefaultInstance() {
                return defaultInstance;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
            }

            private void initFields() {
                this.path_ = Collections.emptyList();
                this.span_ = Collections.emptyList();
                this.leadingComments_ = "";
                this.trailingComments_ = "";
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            public static Builder newBuilder(Location location) {
                return Location.newBuilder().mergeFrom(location);
            }

            public static Location parseDelimitedFrom(InputStream inputStream) throws IOException {
                return PARSER.parseDelimitedFrom(inputStream);
            }

            public static Location parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
            }

            public static Location parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(byteString);
            }

            public static Location parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(byteString, extensionRegistryLite);
            }

            public static Location parseFrom(CodedInputStream codedInputStream) throws IOException {
                return PARSER.parseFrom(codedInputStream);
            }

            public static Location parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
            }

            public static Location parseFrom(InputStream inputStream) throws IOException {
                return PARSER.parseFrom(inputStream);
            }

            public static Location parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseFrom(inputStream, extensionRegistryLite);
            }

            public static Location parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(arrby);
            }

            public static Location parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(arrby, extensionRegistryLite);
            }

            @Override
            public Location getDefaultInstanceForType() {
                return defaultInstance;
            }

            @Override
            public String getLeadingComments() {
                Object object = this.leadingComments_;
                if (object instanceof String) {
                    return (String)object;
                }
                object = (ByteString)object;
                String string2 = object.toStringUtf8();
                if (object.isValidUtf8()) {
                    this.leadingComments_ = string2;
                }
                return string2;
            }

            @Override
            public ByteString getLeadingCommentsBytes() {
                Object object = this.leadingComments_;
                if (object instanceof String) {
                    this.leadingComments_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            public Parser<Location> getParserForType() {
                return PARSER;
            }

            @Override
            public int getPath(int n) {
                return this.path_.get(n);
            }

            @Override
            public int getPathCount() {
                return this.path_.size();
            }

            @Override
            public List<Integer> getPathList() {
                return this.path_;
            }

            @Override
            public int getSerializedSize() {
                int n;
                int n2 = this.memoizedSerializedSize;
                if (n2 != -1) {
                    return n2;
                }
                int n3 = 0;
                for (n2 = 0; n2 < this.path_.size(); ++n2) {
                    n3 += CodedOutputStream.computeInt32SizeNoTag(this.path_.get(n2));
                }
                n2 = n = 0 + n3;
                if (!this.getPathList().isEmpty()) {
                    n2 = n + 1 + CodedOutputStream.computeInt32SizeNoTag(n3);
                }
                this.pathMemoizedSerializedSize = n3;
                n3 = 0;
                for (n = 0; n < this.span_.size(); ++n) {
                    n3 += CodedOutputStream.computeInt32SizeNoTag(this.span_.get(n));
                }
                n2 = n = n2 + n3;
                if (!this.getSpanList().isEmpty()) {
                    n2 = n + 1 + CodedOutputStream.computeInt32SizeNoTag(n3);
                }
                this.spanMemoizedSerializedSize = n3;
                n3 = n2;
                if ((this.bitField0_ & 1) == 1) {
                    n3 = n2 + CodedOutputStream.computeBytesSize(3, this.getLeadingCommentsBytes());
                }
                n2 = n3;
                if ((this.bitField0_ & 2) == 2) {
                    n2 = n3 + CodedOutputStream.computeBytesSize(4, this.getTrailingCommentsBytes());
                }
                this.memoizedSerializedSize = n2 += this.getUnknownFields().getSerializedSize();
                return n2;
            }

            @Override
            public int getSpan(int n) {
                return this.span_.get(n);
            }

            @Override
            public int getSpanCount() {
                return this.span_.size();
            }

            @Override
            public List<Integer> getSpanList() {
                return this.span_;
            }

            @Override
            public String getTrailingComments() {
                Object object = this.trailingComments_;
                if (object instanceof String) {
                    return (String)object;
                }
                object = (ByteString)object;
                String string2 = object.toStringUtf8();
                if (object.isValidUtf8()) {
                    this.trailingComments_ = string2;
                }
                return string2;
            }

            @Override
            public ByteString getTrailingCommentsBytes() {
                Object object = this.trailingComments_;
                if (object instanceof String) {
                    this.trailingComments_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public final UnknownFieldSet getUnknownFields() {
                return this.unknownFields;
            }

            @Override
            public boolean hasLeadingComments() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasTrailingComments() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable.ensureFieldAccessorsInitialized(Location.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                byte by = this.memoizedIsInitialized;
                if (by != -1) {
                    if (by == 1) {
                        return true;
                    }
                    return false;
                }
                this.memoizedIsInitialized = 1;
                return true;
            }

            @Override
            public Builder newBuilderForType() {
                return Location.newBuilder();
            }

            @Override
            protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
                return new Builder(builderParent);
            }

            @Override
            public Builder toBuilder() {
                return Location.newBuilder(this);
            }

            @Override
            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            @Override
            public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
                int n;
                this.getSerializedSize();
                if (this.getPathList().size() > 0) {
                    codedOutputStream.writeRawVarint32(10);
                    codedOutputStream.writeRawVarint32(this.pathMemoizedSerializedSize);
                }
                for (n = 0; n < this.path_.size(); ++n) {
                    codedOutputStream.writeInt32NoTag(this.path_.get(n));
                }
                if (this.getSpanList().size() > 0) {
                    codedOutputStream.writeRawVarint32(18);
                    codedOutputStream.writeRawVarint32(this.spanMemoizedSerializedSize);
                }
                for (n = 0; n < this.span_.size(); ++n) {
                    codedOutputStream.writeInt32NoTag(this.span_.get(n));
                }
                if ((this.bitField0_ & 1) == 1) {
                    codedOutputStream.writeBytes(3, this.getLeadingCommentsBytes());
                }
                if ((this.bitField0_ & 2) == 2) {
                    codedOutputStream.writeBytes(4, this.getTrailingCommentsBytes());
                }
                this.getUnknownFields().writeTo(codedOutputStream);
            }

            public static final class Builder
            extends GeneratedMessage.Builder<Builder>
            implements LocationOrBuilder {
                private int bitField0_;
                private Object leadingComments_ = "";
                private List<Integer> path_ = Collections.emptyList();
                private List<Integer> span_ = Collections.emptyList();
                private Object trailingComments_ = "";

                private Builder() {
                    this.maybeForceBuilderInitialization();
                }

                private Builder(GeneratedMessage.BuilderParent builderParent) {
                    super(builderParent);
                    this.maybeForceBuilderInitialization();
                }

                private static Builder create() {
                    return new Builder();
                }

                private void ensurePathIsMutable() {
                    if ((this.bitField0_ & 1) != 1) {
                        this.path_ = new ArrayList<Integer>(this.path_);
                        this.bitField0_ |= 1;
                    }
                }

                private void ensureSpanIsMutable() {
                    if ((this.bitField0_ & 2) != 2) {
                        this.span_ = new ArrayList<Integer>(this.span_);
                        this.bitField0_ |= 2;
                    }
                }

                public static final Descriptors.Descriptor getDescriptor() {
                    return internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
                }

                private void maybeForceBuilderInitialization() {
                    boolean bl = GeneratedMessage.alwaysUseFieldBuilders;
                }

                public Builder addAllPath(Iterable<? extends Integer> iterable) {
                    this.ensurePathIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.path_);
                    this.onChanged();
                    return this;
                }

                public Builder addAllSpan(Iterable<? extends Integer> iterable) {
                    this.ensureSpanIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.span_);
                    this.onChanged();
                    return this;
                }

                public Builder addPath(int n) {
                    this.ensurePathIsMutable();
                    this.path_.add(n);
                    this.onChanged();
                    return this;
                }

                public Builder addSpan(int n) {
                    this.ensureSpanIsMutable();
                    this.span_.add(n);
                    this.onChanged();
                    return this;
                }

                @Override
                public Location build() {
                    Location location = this.buildPartial();
                    if (location.isInitialized()) {
                        return location;
                    }
                    throw Builder.newUninitializedMessageException(location);
                }

                @Override
                public Location buildPartial() {
                    Location location = new Location(this);
                    int n = this.bitField0_;
                    int n2 = 0;
                    if ((this.bitField0_ & 1) == 1) {
                        this.path_ = Collections.unmodifiableList(this.path_);
                        this.bitField0_ &= -2;
                    }
                    location.path_ = this.path_;
                    if ((this.bitField0_ & 2) == 2) {
                        this.span_ = Collections.unmodifiableList(this.span_);
                        this.bitField0_ &= -3;
                    }
                    location.span_ = this.span_;
                    if ((n & 4) == 4) {
                        n2 = false | true;
                    }
                    location.leadingComments_ = this.leadingComments_;
                    int n3 = n2;
                    if ((n & 8) == 8) {
                        n3 = n2 | 2;
                    }
                    location.trailingComments_ = this.trailingComments_;
                    location.bitField0_ = n3;
                    this.onBuilt();
                    return location;
                }

                @Override
                public Builder clear() {
                    int n;
                    super.clear();
                    this.path_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.span_ = Collections.emptyList();
                    this.bitField0_ = n = this.bitField0_ & -3;
                    this.leadingComments_ = "";
                    this.bitField0_ = n &= -5;
                    this.trailingComments_ = "";
                    this.bitField0_ = n & -9;
                    return this;
                }

                public Builder clearLeadingComments() {
                    this.bitField0_ &= -5;
                    this.leadingComments_ = Location.getDefaultInstance().getLeadingComments();
                    this.onChanged();
                    return this;
                }

                public Builder clearPath() {
                    this.path_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.onChanged();
                    return this;
                }

                public Builder clearSpan() {
                    this.span_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    this.onChanged();
                    return this;
                }

                public Builder clearTrailingComments() {
                    this.bitField0_ &= -9;
                    this.trailingComments_ = Location.getDefaultInstance().getTrailingComments();
                    this.onChanged();
                    return this;
                }

                @Override
                public Builder clone() {
                    return Builder.create().mergeFrom(this.buildPartial());
                }

                @Override
                public Location getDefaultInstanceForType() {
                    return Location.getDefaultInstance();
                }

                @Override
                public Descriptors.Descriptor getDescriptorForType() {
                    return internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
                }

                @Override
                public String getLeadingComments() {
                    Object object = this.leadingComments_;
                    if (!(object instanceof String)) {
                        this.leadingComments_ = object = ((ByteString)object).toStringUtf8();
                        return object;
                    }
                    return (String)object;
                }

                @Override
                public ByteString getLeadingCommentsBytes() {
                    Object object = this.leadingComments_;
                    if (object instanceof String) {
                        this.leadingComments_ = object = ByteString.copyFromUtf8((String)object);
                        return object;
                    }
                    return (ByteString)object;
                }

                @Override
                public int getPath(int n) {
                    return this.path_.get(n);
                }

                @Override
                public int getPathCount() {
                    return this.path_.size();
                }

                @Override
                public List<Integer> getPathList() {
                    return Collections.unmodifiableList(this.path_);
                }

                @Override
                public int getSpan(int n) {
                    return this.span_.get(n);
                }

                @Override
                public int getSpanCount() {
                    return this.span_.size();
                }

                @Override
                public List<Integer> getSpanList() {
                    return Collections.unmodifiableList(this.span_);
                }

                @Override
                public String getTrailingComments() {
                    Object object = this.trailingComments_;
                    if (!(object instanceof String)) {
                        this.trailingComments_ = object = ((ByteString)object).toStringUtf8();
                        return object;
                    }
                    return (String)object;
                }

                @Override
                public ByteString getTrailingCommentsBytes() {
                    Object object = this.trailingComments_;
                    if (object instanceof String) {
                        this.trailingComments_ = object = ByteString.copyFromUtf8((String)object);
                        return object;
                    }
                    return (ByteString)object;
                }

                @Override
                public boolean hasLeadingComments() {
                    if ((this.bitField0_ & 4) == 4) {
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean hasTrailingComments() {
                    if ((this.bitField0_ & 8) == 8) {
                        return true;
                    }
                    return false;
                }

                @Override
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable.ensureFieldAccessorsInitialized(Location.class, Builder.class);
                }

                @Override
                public final boolean isInitialized() {
                    return true;
                }

                /*
                 * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
                 * Loose catch block
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 * Lifted jumps to return sites
                 */
                @Override
                public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                    Object object2;
                    Throwable throwable222;
                    Object object3 = object2 = null;
                    object = Location.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                    if (object == null) return this;
                    this.mergeFrom((Location)object);
                    return this;
                    {
                        catch (Throwable throwable222) {
                        }
                        catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                        object3 = object2;
                        {
                            object3 = object = (Location)invalidProtocolBufferException.getUnfinishedMessage();
                            throw invalidProtocolBufferException;
                        }
                    }
                    if (object3 == null) throw throwable222;
                    this.mergeFrom((Location)object3);
                    throw throwable222;
                }

                public Builder mergeFrom(Location location) {
                    if (location == Location.getDefaultInstance()) {
                        return this;
                    }
                    if (!location.path_.isEmpty()) {
                        if (this.path_.isEmpty()) {
                            this.path_ = location.path_;
                            this.bitField0_ &= -2;
                        } else {
                            this.ensurePathIsMutable();
                            this.path_.addAll(location.path_);
                        }
                        this.onChanged();
                    }
                    if (!location.span_.isEmpty()) {
                        if (this.span_.isEmpty()) {
                            this.span_ = location.span_;
                            this.bitField0_ &= -3;
                        } else {
                            this.ensureSpanIsMutable();
                            this.span_.addAll(location.span_);
                        }
                        this.onChanged();
                    }
                    if (location.hasLeadingComments()) {
                        this.bitField0_ |= 4;
                        this.leadingComments_ = location.leadingComments_;
                        this.onChanged();
                    }
                    if (location.hasTrailingComments()) {
                        this.bitField0_ |= 8;
                        this.trailingComments_ = location.trailingComments_;
                        this.onChanged();
                    }
                    this.mergeUnknownFields(location.getUnknownFields());
                    return this;
                }

                @Override
                public Builder mergeFrom(Message message) {
                    if (message instanceof Location) {
                        return this.mergeFrom((Location)message);
                    }
                    super.mergeFrom(message);
                    return this;
                }

                public Builder setLeadingComments(String string2) {
                    if (string2 != null) {
                        this.bitField0_ |= 4;
                        this.leadingComments_ = string2;
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }

                public Builder setLeadingCommentsBytes(ByteString byteString) {
                    if (byteString != null) {
                        this.bitField0_ |= 4;
                        this.leadingComments_ = byteString;
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }

                public Builder setPath(int n, int n2) {
                    this.ensurePathIsMutable();
                    this.path_.set(n, n2);
                    this.onChanged();
                    return this;
                }

                public Builder setSpan(int n, int n2) {
                    this.ensureSpanIsMutable();
                    this.span_.set(n, n2);
                    this.onChanged();
                    return this;
                }

                public Builder setTrailingComments(String string2) {
                    if (string2 != null) {
                        this.bitField0_ |= 8;
                        this.trailingComments_ = string2;
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }

                public Builder setTrailingCommentsBytes(ByteString byteString) {
                    if (byteString != null) {
                        this.bitField0_ |= 8;
                        this.trailingComments_ = byteString;
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
            }

        }

        public static interface LocationOrBuilder
        extends MessageOrBuilder {
            public String getLeadingComments();

            public ByteString getLeadingCommentsBytes();

            public int getPath(int var1);

            public int getPathCount();

            public List<Integer> getPathList();

            public int getSpan(int var1);

            public int getSpanCount();

            public List<Integer> getSpanList();

            public String getTrailingComments();

            public ByteString getTrailingCommentsBytes();

            public boolean hasLeadingComments();

            public boolean hasTrailingComments();
        }

    }

    public static interface SourceCodeInfoOrBuilder
    extends MessageOrBuilder {
        public SourceCodeInfo.Location getLocation(int var1);

        public int getLocationCount();

        public List<SourceCodeInfo.Location> getLocationList();

        public SourceCodeInfo.LocationOrBuilder getLocationOrBuilder(int var1);

        public List<? extends SourceCodeInfo.LocationOrBuilder> getLocationOrBuilderList();
    }

    public static final class UninterpretedOption
    extends GeneratedMessage
    implements UninterpretedOptionOrBuilder {
        public static final int AGGREGATE_VALUE_FIELD_NUMBER = 8;
        public static final int DOUBLE_VALUE_FIELD_NUMBER = 6;
        public static final int IDENTIFIER_VALUE_FIELD_NUMBER = 3;
        public static final int NAME_FIELD_NUMBER = 2;
        public static final int NEGATIVE_INT_VALUE_FIELD_NUMBER = 5;
        public static Parser<UninterpretedOption> PARSER;
        public static final int POSITIVE_INT_VALUE_FIELD_NUMBER = 4;
        public static final int STRING_VALUE_FIELD_NUMBER = 7;
        private static final UninterpretedOption defaultInstance;
        private static final long serialVersionUID = 0L;
        private Object aggregateValue_;
        private int bitField0_;
        private double doubleValue_;
        private Object identifierValue_;
        private byte memoizedIsInitialized = (byte)-1;
        private int memoizedSerializedSize = -1;
        private List<NamePart> name_;
        private long negativeIntValue_;
        private long positiveIntValue_;
        private ByteString stringValue_;
        private final UnknownFieldSet unknownFields;

        static {
            UninterpretedOption uninterpretedOption;
            PARSER = new AbstractParser<UninterpretedOption>(){

                @Override
                public UninterpretedOption parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                    return new UninterpretedOption(codedInputStream, extensionRegistryLite);
                }
            };
            defaultInstance = uninterpretedOption = new UninterpretedOption(true);
            uninterpretedOption.initFields();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        private UninterpretedOption(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
            super();
            this.initFields();
            var3_6 = false;
            var11_7 = UnknownFieldSet.newBuilder();
            var8_8 = false;
            while (!var8_8) {
                block25 : {
                    block26 : {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        var10_14 = var1_1.readTag();
                        if (var10_14 == 0) ** GOTO lbl115
                        if (var10_14 == 18) break block26;
                        if (var10_14 != 26) {
                            if (var10_14 != 32) {
                                if (var10_14 != 40) {
                                    if (var10_14 != 49) {
                                        if (var10_14 != 58) {
                                            if (var10_14 != 66) {
                                                var9_13 = var3_6;
                                                var4_9 = var8_8;
                                                var5_10 = var3_6;
                                                var6_11 = var3_6;
                                                var7_12 = var3_6;
                                                if (!this.parseUnknownField(var1_1, var11_7, var2_5, var10_14)) {
                                                    var4_9 = true;
                                                    var9_13 = var3_6;
                                                }
                                            } else {
                                                var5_10 = var3_6;
                                                var6_11 = var3_6;
                                                var7_12 = var3_6;
                                                this.bitField0_ = 32 | this.bitField0_;
                                                var5_10 = var3_6;
                                                var6_11 = var3_6;
                                                var7_12 = var3_6;
                                                this.aggregateValue_ = var1_1.readBytes();
                                                var9_13 = var3_6;
                                                var4_9 = var8_8;
                                            }
                                        } else {
                                            var5_10 = var3_6;
                                            var6_11 = var3_6;
                                            var7_12 = var3_6;
                                            this.bitField0_ |= 16;
                                            var5_10 = var3_6;
                                            var6_11 = var3_6;
                                            var7_12 = var3_6;
                                            this.stringValue_ = var1_1.readBytes();
                                            var9_13 = var3_6;
                                            var4_9 = var8_8;
                                        }
                                    } else {
                                        var5_10 = var3_6;
                                        var6_11 = var3_6;
                                        var7_12 = var3_6;
                                        this.bitField0_ |= 8;
                                        var5_10 = var3_6;
                                        var6_11 = var3_6;
                                        var7_12 = var3_6;
                                        this.doubleValue_ = var1_1.readDouble();
                                        var9_13 = var3_6;
                                        var4_9 = var8_8;
                                    }
                                } else {
                                    var5_10 = var3_6;
                                    var6_11 = var3_6;
                                    var7_12 = var3_6;
                                    this.bitField0_ |= 4;
                                    var5_10 = var3_6;
                                    var6_11 = var3_6;
                                    var7_12 = var3_6;
                                    this.negativeIntValue_ = var1_1.readInt64();
                                    var9_13 = var3_6;
                                    var4_9 = var8_8;
                                }
                            } else {
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                var7_12 = var3_6;
                                this.bitField0_ |= 2;
                                var5_10 = var3_6;
                                var6_11 = var3_6;
                                var7_12 = var3_6;
                                this.positiveIntValue_ = var1_1.readUInt64();
                                var9_13 = var3_6;
                                var4_9 = var8_8;
                            }
                        } else {
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            this.bitField0_ |= 1;
                            var5_10 = var3_6;
                            var6_11 = var3_6;
                            var7_12 = var3_6;
                            this.identifierValue_ = var1_1.readBytes();
                            var9_13 = var3_6;
                            var4_9 = var8_8;
                        }
                        ** GOTO lbl117
                    }
                    var4_9 = var3_6;
                    if (!(var3_6 & true)) {
                        var5_10 = var3_6;
                        var6_11 = var3_6;
                        var7_12 = var3_6;
                        this.name_ = new ArrayList<NamePart>();
                        var4_9 = var3_6 | true;
                    }
                    var5_10 = var4_9;
                    var6_11 = var4_9;
                    var7_12 = var4_9;
                    try {
                        this.name_.add(var1_1.readMessage(NamePart.PARSER, var2_5));
                        var9_13 = var4_9;
                        var4_9 = var8_8;
lbl115: // 1 sources:
                        var4_9 = true;
                        var9_13 = var3_6;
lbl117: // 9 sources:
                        var3_6 = var9_13;
                        var8_8 = var4_9;
                        continue;
                    }
                    catch (Throwable var1_2) {
                        break block25;
                    }
                    catch (IOException var1_3) {
                        var5_10 = var6_11;
                        throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                    }
                    catch (InvalidProtocolBufferException var1_4) {
                        var5_10 = var7_12;
                        throw var1_4.setUnfinishedMessage(this);
                    }
                }
                if (var5_10 & true) {
                    this.name_ = Collections.unmodifiableList(this.name_);
                }
                this.unknownFields = var11_7.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }
            if (var3_6 & true) {
                this.name_ = Collections.unmodifiableList(this.name_);
            }
            this.unknownFields = var11_7.build();
            this.makeExtensionsImmutable();
        }

        private UninterpretedOption(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private UninterpretedOption(boolean bl) {
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static UninterpretedOption getDefaultInstance() {
            return defaultInstance;
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return internal_static_google_protobuf_UninterpretedOption_descriptor;
        }

        private void initFields() {
            this.name_ = Collections.emptyList();
            this.identifierValue_ = "";
            this.positiveIntValue_ = 0L;
            this.negativeIntValue_ = 0L;
            this.doubleValue_ = 0.0;
            this.stringValue_ = ByteString.EMPTY;
            this.aggregateValue_ = "";
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(UninterpretedOption uninterpretedOption) {
            return UninterpretedOption.newBuilder().mergeFrom(uninterpretedOption);
        }

        public static UninterpretedOption parseDelimitedFrom(InputStream inputStream) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream);
        }

        public static UninterpretedOption parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
        }

        public static UninterpretedOption parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString);
        }

        public static UninterpretedOption parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(byteString, extensionRegistryLite);
        }

        public static UninterpretedOption parseFrom(CodedInputStream codedInputStream) throws IOException {
            return PARSER.parseFrom(codedInputStream);
        }

        public static UninterpretedOption parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
        }

        public static UninterpretedOption parseFrom(InputStream inputStream) throws IOException {
            return PARSER.parseFrom(inputStream);
        }

        public static UninterpretedOption parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return PARSER.parseFrom(inputStream, extensionRegistryLite);
        }

        public static UninterpretedOption parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby);
        }

        public static UninterpretedOption parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(arrby, extensionRegistryLite);
        }

        @Override
        public String getAggregateValue() {
            Object object = this.aggregateValue_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.aggregateValue_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getAggregateValueBytes() {
            Object object = this.aggregateValue_;
            if (object instanceof String) {
                this.aggregateValue_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public UninterpretedOption getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override
        public double getDoubleValue() {
            return this.doubleValue_;
        }

        @Override
        public String getIdentifierValue() {
            Object object = this.identifierValue_;
            if (object instanceof String) {
                return (String)object;
            }
            object = (ByteString)object;
            String string2 = object.toStringUtf8();
            if (object.isValidUtf8()) {
                this.identifierValue_ = string2;
            }
            return string2;
        }

        @Override
        public ByteString getIdentifierValueBytes() {
            Object object = this.identifierValue_;
            if (object instanceof String) {
                this.identifierValue_ = object = ByteString.copyFromUtf8((String)object);
                return object;
            }
            return (ByteString)object;
        }

        @Override
        public NamePart getName(int n) {
            return this.name_.get(n);
        }

        @Override
        public int getNameCount() {
            return this.name_.size();
        }

        @Override
        public List<NamePart> getNameList() {
            return this.name_;
        }

        @Override
        public NamePartOrBuilder getNameOrBuilder(int n) {
            return this.name_.get(n);
        }

        @Override
        public List<? extends NamePartOrBuilder> getNameOrBuilderList() {
            return this.name_;
        }

        @Override
        public long getNegativeIntValue() {
            return this.negativeIntValue_;
        }

        public Parser<UninterpretedOption> getParserForType() {
            return PARSER;
        }

        @Override
        public long getPositiveIntValue() {
            return this.positiveIntValue_;
        }

        @Override
        public int getSerializedSize() {
            int n;
            int n2 = this.memoizedSerializedSize;
            if (n2 != -1) {
                return n2;
            }
            n2 = 0;
            for (n = 0; n < this.name_.size(); ++n) {
                n2 += CodedOutputStream.computeMessageSize(2, this.name_.get(n));
            }
            n = n2;
            if ((this.bitField0_ & 1) == 1) {
                n = n2 + CodedOutputStream.computeBytesSize(3, this.getIdentifierValueBytes());
            }
            n2 = n;
            if ((this.bitField0_ & 2) == 2) {
                n2 = n + CodedOutputStream.computeUInt64Size(4, this.positiveIntValue_);
            }
            n = n2;
            if ((this.bitField0_ & 4) == 4) {
                n = n2 + CodedOutputStream.computeInt64Size(5, this.negativeIntValue_);
            }
            n2 = n;
            if ((this.bitField0_ & 8) == 8) {
                n2 = n + CodedOutputStream.computeDoubleSize(6, this.doubleValue_);
            }
            n = n2;
            if ((this.bitField0_ & 16) == 16) {
                n = n2 + CodedOutputStream.computeBytesSize(7, this.stringValue_);
            }
            n2 = n;
            if ((this.bitField0_ & 32) == 32) {
                n2 = n + CodedOutputStream.computeBytesSize(8, this.getAggregateValueBytes());
            }
            this.memoizedSerializedSize = n2 += this.getUnknownFields().getSerializedSize();
            return n2;
        }

        @Override
        public ByteString getStringValue() {
            return this.stringValue_;
        }

        @Override
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        @Override
        public boolean hasAggregateValue() {
            if ((this.bitField0_ & 32) == 32) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasDoubleValue() {
            if ((this.bitField0_ & 8) == 8) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasIdentifierValue() {
            if ((this.bitField0_ & 1) == 1) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasNegativeIntValue() {
            if ((this.bitField0_ & 4) == 4) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasPositiveIntValue() {
            if ((this.bitField0_ & 2) == 2) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasStringValue() {
            if ((this.bitField0_ & 16) == 16) {
                return true;
            }
            return false;
        }

        @Override
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable.ensureFieldAccessorsInitialized(UninterpretedOption.class, Builder.class);
        }

        @Override
        public final boolean isInitialized() {
            int n = this.memoizedIsInitialized;
            boolean bl = false;
            if (n != -1) {
                if (n == 1) {
                    bl = true;
                }
                return bl;
            }
            for (n = 0; n < this.getNameCount(); ++n) {
                if (this.getName(n).isInitialized()) continue;
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public Builder newBuilderForType() {
            return UninterpretedOption.newBuilder();
        }

        @Override
        protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
            return new Builder(builderParent);
        }

        @Override
        public Builder toBuilder() {
            return UninterpretedOption.newBuilder(this);
        }

        @Override
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        @Override
        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            this.getSerializedSize();
            for (int i = 0; i < this.name_.size(); ++i) {
                codedOutputStream.writeMessage(2, this.name_.get(i));
            }
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(3, this.getIdentifierValueBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeUInt64(4, this.positiveIntValue_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeInt64(5, this.negativeIntValue_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeDouble(6, this.doubleValue_);
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeBytes(7, this.stringValue_);
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeBytes(8, this.getAggregateValueBytes());
            }
            this.getUnknownFields().writeTo(codedOutputStream);
        }

        public static final class Builder
        extends GeneratedMessage.Builder<Builder>
        implements UninterpretedOptionOrBuilder {
            private Object aggregateValue_ = "";
            private int bitField0_;
            private double doubleValue_;
            private Object identifierValue_ = "";
            private RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> nameBuilder_;
            private List<NamePart> name_ = Collections.emptyList();
            private long negativeIntValue_;
            private long positiveIntValue_;
            private ByteString stringValue_ = ByteString.EMPTY;

            private Builder() {
                this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent builderParent) {
                super(builderParent);
                this.maybeForceBuilderInitialization();
            }

            private static Builder create() {
                return new Builder();
            }

            private void ensureNameIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.name_ = new ArrayList<NamePart>(this.name_);
                    this.bitField0_ |= 1;
                }
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_UninterpretedOption_descriptor;
            }

            private RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> getNameFieldBuilder() {
                if (this.nameBuilder_ == null) {
                    List<NamePart> list = this.name_;
                    int n = this.bitField0_;
                    boolean bl = true;
                    if ((n & 1) != 1) {
                        bl = false;
                    }
                    this.nameBuilder_ = new RepeatedFieldBuilder(list, bl, this.getParentForChildren(), this.isClean());
                    this.name_ = null;
                }
                return this.nameBuilder_;
            }

            private void maybeForceBuilderInitialization() {
                if (GeneratedMessage.alwaysUseFieldBuilders) {
                    this.getNameFieldBuilder();
                }
            }

            public Builder addAllName(Iterable<? extends NamePart> iterable) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNameIsMutable();
                    GeneratedMessage.Builder.addAll(iterable, this.name_);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addAllMessages(iterable);
                return this;
            }

            public Builder addName(int n, NamePart.Builder builder) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNameIsMutable();
                    this.name_.add(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(n, builder.build());
                return this;
            }

            public Builder addName(int n, NamePart namePart) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (namePart != null) {
                        this.ensureNameIsMutable();
                        this.name_.add(n, namePart);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(n, namePart);
                return this;
            }

            public Builder addName(NamePart.Builder builder) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNameIsMutable();
                    this.name_.add(builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.addMessage(builder.build());
                return this;
            }

            public Builder addName(NamePart namePart) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (namePart != null) {
                        this.ensureNameIsMutable();
                        this.name_.add(namePart);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.addMessage(namePart);
                return this;
            }

            public NamePart.Builder addNameBuilder() {
                return this.getNameFieldBuilder().addBuilder(NamePart.getDefaultInstance());
            }

            public NamePart.Builder addNameBuilder(int n) {
                return this.getNameFieldBuilder().addBuilder(n, NamePart.getDefaultInstance());
            }

            @Override
            public UninterpretedOption build() {
                UninterpretedOption uninterpretedOption = this.buildPartial();
                if (uninterpretedOption.isInitialized()) {
                    return uninterpretedOption;
                }
                throw Builder.newUninitializedMessageException(uninterpretedOption);
            }

            @Override
            public UninterpretedOption buildPartial() {
                UninterpretedOption uninterpretedOption = new UninterpretedOption(this);
                int n = this.bitField0_;
                int n2 = 0;
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    if ((this.bitField0_ & 1) == 1) {
                        this.name_ = Collections.unmodifiableList(this.name_);
                        this.bitField0_ &= -2;
                    }
                    uninterpretedOption.name_ = this.name_;
                } else {
                    uninterpretedOption.name_ = repeatedFieldBuilder.build();
                }
                if ((n & 2) == 2) {
                    n2 = false | true;
                }
                uninterpretedOption.identifierValue_ = this.identifierValue_;
                int n3 = n2;
                if ((n & 4) == 4) {
                    n3 = n2 | 2;
                }
                uninterpretedOption.positiveIntValue_ = this.positiveIntValue_;
                n2 = n3;
                if ((n & 8) == 8) {
                    n2 = n3 | 4;
                }
                uninterpretedOption.negativeIntValue_ = this.negativeIntValue_;
                n3 = n2;
                if ((n & 16) == 16) {
                    n3 = n2 | 8;
                }
                uninterpretedOption.doubleValue_ = this.doubleValue_;
                n2 = n3;
                if ((n & 32) == 32) {
                    n2 = n3 | 16;
                }
                uninterpretedOption.stringValue_ = this.stringValue_;
                n3 = n2;
                if ((n & 64) == 64) {
                    n3 = n2 | 32;
                }
                uninterpretedOption.aggregateValue_ = this.aggregateValue_;
                uninterpretedOption.bitField0_ = n3;
                this.onBuilt();
                return uninterpretedOption;
            }

            @Override
            public Builder clear() {
                int n;
                super.clear();
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.name_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    repeatedFieldBuilder.clear();
                }
                this.identifierValue_ = "";
                this.bitField0_ = n = this.bitField0_ & -3;
                this.positiveIntValue_ = 0L;
                this.bitField0_ = n &= -5;
                this.negativeIntValue_ = 0L;
                this.bitField0_ = n &= -9;
                this.doubleValue_ = 0.0;
                this.bitField0_ = n & -17;
                this.stringValue_ = ByteString.EMPTY;
                this.bitField0_ = n = this.bitField0_ & -33;
                this.aggregateValue_ = "";
                this.bitField0_ = n & -65;
                return this;
            }

            public Builder clearAggregateValue() {
                this.bitField0_ &= -65;
                this.aggregateValue_ = UninterpretedOption.getDefaultInstance().getAggregateValue();
                this.onChanged();
                return this;
            }

            public Builder clearDoubleValue() {
                this.bitField0_ &= -17;
                this.doubleValue_ = 0.0;
                this.onChanged();
                return this;
            }

            public Builder clearIdentifierValue() {
                this.bitField0_ &= -3;
                this.identifierValue_ = UninterpretedOption.getDefaultInstance().getIdentifierValue();
                this.onChanged();
                return this;
            }

            public Builder clearName() {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.name_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.clear();
                return this;
            }

            public Builder clearNegativeIntValue() {
                this.bitField0_ &= -9;
                this.negativeIntValue_ = 0L;
                this.onChanged();
                return this;
            }

            public Builder clearPositiveIntValue() {
                this.bitField0_ &= -5;
                this.positiveIntValue_ = 0L;
                this.onChanged();
                return this;
            }

            public Builder clearStringValue() {
                this.bitField0_ &= -33;
                this.stringValue_ = UninterpretedOption.getDefaultInstance().getStringValue();
                this.onChanged();
                return this;
            }

            @Override
            public Builder clone() {
                return Builder.create().mergeFrom(this.buildPartial());
            }

            @Override
            public String getAggregateValue() {
                Object object = this.aggregateValue_;
                if (!(object instanceof String)) {
                    this.aggregateValue_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getAggregateValueBytes() {
                Object object = this.aggregateValue_;
                if (object instanceof String) {
                    this.aggregateValue_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public UninterpretedOption getDefaultInstanceForType() {
                return UninterpretedOption.getDefaultInstance();
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return internal_static_google_protobuf_UninterpretedOption_descriptor;
            }

            @Override
            public double getDoubleValue() {
                return this.doubleValue_;
            }

            @Override
            public String getIdentifierValue() {
                Object object = this.identifierValue_;
                if (!(object instanceof String)) {
                    this.identifierValue_ = object = ((ByteString)object).toStringUtf8();
                    return object;
                }
                return (String)object;
            }

            @Override
            public ByteString getIdentifierValueBytes() {
                Object object = this.identifierValue_;
                if (object instanceof String) {
                    this.identifierValue_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            @Override
            public NamePart getName(int n) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.name_.get(n);
                }
                return repeatedFieldBuilder.getMessage(n);
            }

            public NamePart.Builder getNameBuilder(int n) {
                return this.getNameFieldBuilder().getBuilder(n);
            }

            public List<NamePart.Builder> getNameBuilderList() {
                return this.getNameFieldBuilder().getBuilderList();
            }

            @Override
            public int getNameCount() {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.name_.size();
                }
                return repeatedFieldBuilder.getCount();
            }

            @Override
            public List<NamePart> getNameList() {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    return Collections.unmodifiableList(this.name_);
                }
                return repeatedFieldBuilder.getMessageList();
            }

            @Override
            public NamePartOrBuilder getNameOrBuilder(int n) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    return this.name_.get(n);
                }
                return repeatedFieldBuilder.getMessageOrBuilder(n);
            }

            @Override
            public List<? extends NamePartOrBuilder> getNameOrBuilderList() {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder != null) {
                    return repeatedFieldBuilder.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.name_);
            }

            @Override
            public long getNegativeIntValue() {
                return this.negativeIntValue_;
            }

            @Override
            public long getPositiveIntValue() {
                return this.positiveIntValue_;
            }

            @Override
            public ByteString getStringValue() {
                return this.stringValue_;
            }

            @Override
            public boolean hasAggregateValue() {
                if ((this.bitField0_ & 64) == 64) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasDoubleValue() {
                if ((this.bitField0_ & 16) == 16) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasIdentifierValue() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasNegativeIntValue() {
                if ((this.bitField0_ & 8) == 8) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasPositiveIntValue() {
                if ((this.bitField0_ & 4) == 4) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasStringValue() {
                if ((this.bitField0_ & 32) == 32) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable.ensureFieldAccessorsInitialized(UninterpretedOption.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                for (int i = 0; i < this.getNameCount(); ++i) {
                    if (this.getName(i).isInitialized()) continue;
                    return false;
                }
                return true;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                Object object2;
                Throwable throwable222;
                Object object3 = object2 = null;
                object = UninterpretedOption.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                if (object == null) return this;
                this.mergeFrom((UninterpretedOption)object);
                return this;
                {
                    catch (Throwable throwable222) {
                    }
                    catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                    object3 = object2;
                    {
                        object3 = object = (UninterpretedOption)invalidProtocolBufferException.getUnfinishedMessage();
                        throw invalidProtocolBufferException;
                    }
                }
                if (object3 == null) throw throwable222;
                this.mergeFrom((UninterpretedOption)object3);
                throw throwable222;
            }

            public Builder mergeFrom(UninterpretedOption uninterpretedOption) {
                if (uninterpretedOption == UninterpretedOption.getDefaultInstance()) {
                    return this;
                }
                if (this.nameBuilder_ == null) {
                    if (!uninterpretedOption.name_.isEmpty()) {
                        if (this.name_.isEmpty()) {
                            this.name_ = uninterpretedOption.name_;
                            this.bitField0_ &= -2;
                        } else {
                            this.ensureNameIsMutable();
                            this.name_.addAll(uninterpretedOption.name_);
                        }
                        this.onChanged();
                    }
                } else if (!uninterpretedOption.name_.isEmpty()) {
                    if (this.nameBuilder_.isEmpty()) {
                        this.nameBuilder_.dispose();
                        RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = null;
                        this.nameBuilder_ = null;
                        this.name_ = uninterpretedOption.name_;
                        this.bitField0_ &= -2;
                        if (GeneratedMessage.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = this.getNameFieldBuilder();
                        }
                        this.nameBuilder_ = repeatedFieldBuilder;
                    } else {
                        this.nameBuilder_.addAllMessages(uninterpretedOption.name_);
                    }
                }
                if (uninterpretedOption.hasIdentifierValue()) {
                    this.bitField0_ |= 2;
                    this.identifierValue_ = uninterpretedOption.identifierValue_;
                    this.onChanged();
                }
                if (uninterpretedOption.hasPositiveIntValue()) {
                    this.setPositiveIntValue(uninterpretedOption.getPositiveIntValue());
                }
                if (uninterpretedOption.hasNegativeIntValue()) {
                    this.setNegativeIntValue(uninterpretedOption.getNegativeIntValue());
                }
                if (uninterpretedOption.hasDoubleValue()) {
                    this.setDoubleValue(uninterpretedOption.getDoubleValue());
                }
                if (uninterpretedOption.hasStringValue()) {
                    this.setStringValue(uninterpretedOption.getStringValue());
                }
                if (uninterpretedOption.hasAggregateValue()) {
                    this.bitField0_ |= 64;
                    this.aggregateValue_ = uninterpretedOption.aggregateValue_;
                    this.onChanged();
                }
                this.mergeUnknownFields(uninterpretedOption.getUnknownFields());
                return this;
            }

            @Override
            public Builder mergeFrom(Message message) {
                if (message instanceof UninterpretedOption) {
                    return this.mergeFrom((UninterpretedOption)message);
                }
                super.mergeFrom(message);
                return this;
            }

            public Builder removeName(int n) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNameIsMutable();
                    this.name_.remove(n);
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.remove(n);
                return this;
            }

            public Builder setAggregateValue(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 64;
                    this.aggregateValue_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setAggregateValueBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 64;
                    this.aggregateValue_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setDoubleValue(double d) {
                this.bitField0_ |= 16;
                this.doubleValue_ = d;
                this.onChanged();
                return this;
            }

            public Builder setIdentifierValue(String string2) {
                if (string2 != null) {
                    this.bitField0_ |= 2;
                    this.identifierValue_ = string2;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setIdentifierValueBytes(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 2;
                    this.identifierValue_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }

            public Builder setName(int n, NamePart.Builder builder) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    this.ensureNameIsMutable();
                    this.name_.set(n, builder.build());
                    this.onChanged();
                    return this;
                }
                repeatedFieldBuilder.setMessage(n, builder.build());
                return this;
            }

            public Builder setName(int n, NamePart namePart) {
                RepeatedFieldBuilder<NamePart, NamePart.Builder, NamePartOrBuilder> repeatedFieldBuilder = this.nameBuilder_;
                if (repeatedFieldBuilder == null) {
                    if (namePart != null) {
                        this.ensureNameIsMutable();
                        this.name_.set(n, namePart);
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
                repeatedFieldBuilder.setMessage(n, namePart);
                return this;
            }

            public Builder setNegativeIntValue(long l) {
                this.bitField0_ |= 8;
                this.negativeIntValue_ = l;
                this.onChanged();
                return this;
            }

            public Builder setPositiveIntValue(long l) {
                this.bitField0_ |= 4;
                this.positiveIntValue_ = l;
                this.onChanged();
                return this;
            }

            public Builder setStringValue(ByteString byteString) {
                if (byteString != null) {
                    this.bitField0_ |= 32;
                    this.stringValue_ = byteString;
                    this.onChanged();
                    return this;
                }
                throw null;
            }
        }

        public static final class NamePart
        extends GeneratedMessage
        implements NamePartOrBuilder {
            public static final int IS_EXTENSION_FIELD_NUMBER = 2;
            public static final int NAME_PART_FIELD_NUMBER = 1;
            public static Parser<NamePart> PARSER;
            private static final NamePart defaultInstance;
            private static final long serialVersionUID = 0L;
            private int bitField0_;
            private boolean isExtension_;
            private byte memoizedIsInitialized = (byte)-1;
            private int memoizedSerializedSize = -1;
            private Object namePart_;
            private final UnknownFieldSet unknownFields;

            static {
                NamePart namePart;
                PARSER = new AbstractParser<NamePart>(){

                    @Override
                    public NamePart parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                        return new NamePart(codedInputStream, extensionRegistryLite);
                    }
                };
                defaultInstance = namePart = new NamePart(true);
                namePart.initFields();
            }

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            private NamePart(CodedInputStream var1_1, ExtensionRegistryLite var2_5) throws InvalidProtocolBufferException {
                super();
                this.initFields();
                var5_6 = UnknownFieldSet.newBuilder();
                var3_7 = false;
                do {
                    block8 : {
                        if (var3_7) {
                            this.unknownFields = var5_6.build();
                            this.makeExtensionsImmutable();
                            return;
                        }
                        var4_8 = var1_1.readTag();
                        if (var4_8 == 0) break block8;
                        if (var4_8 == 10) ** GOTO lbl24
                        if (var4_8 == 16) ** GOTO lbl21
                        if (this.parseUnknownField(var1_1, var5_6, var2_5, var4_8)) continue;
                        var3_7 = true;
                        continue;
lbl21: // 1 sources:
                        this.bitField0_ |= 2;
                        this.isExtension_ = var1_1.readBool();
                        continue;
lbl24: // 1 sources:
                        this.bitField0_ |= 1;
                        this.namePart_ = var1_1.readBytes();
                        continue;
                    }
                    var3_7 = true;
                    continue;
                    break;
                } while (true);
                catch (Throwable var1_2) {
                }
                catch (IOException var1_3) {
                    throw new InvalidProtocolBufferException(var1_3.getMessage()).setUnfinishedMessage(this);
                }
                catch (InvalidProtocolBufferException var1_4) {
                    throw var1_4.setUnfinishedMessage(this);
                }
                this.unknownFields = var5_6.build();
                this.makeExtensionsImmutable();
                throw var1_2;
            }

            private NamePart(GeneratedMessage.Builder<?> builder) {
                super(builder);
                this.unknownFields = builder.getUnknownFields();
            }

            private NamePart(boolean bl) {
                this.unknownFields = UnknownFieldSet.getDefaultInstance();
            }

            public static NamePart getDefaultInstance() {
                return defaultInstance;
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
            }

            private void initFields() {
                this.namePart_ = "";
                this.isExtension_ = false;
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            public static Builder newBuilder(NamePart namePart) {
                return NamePart.newBuilder().mergeFrom(namePart);
            }

            public static NamePart parseDelimitedFrom(InputStream inputStream) throws IOException {
                return PARSER.parseDelimitedFrom(inputStream);
            }

            public static NamePart parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseDelimitedFrom(inputStream, extensionRegistryLite);
            }

            public static NamePart parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(byteString);
            }

            public static NamePart parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(byteString, extensionRegistryLite);
            }

            public static NamePart parseFrom(CodedInputStream codedInputStream) throws IOException {
                return PARSER.parseFrom(codedInputStream);
            }

            public static NamePart parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseFrom(codedInputStream, extensionRegistryLite);
            }

            public static NamePart parseFrom(InputStream inputStream) throws IOException {
                return PARSER.parseFrom(inputStream);
            }

            public static NamePart parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                return PARSER.parseFrom(inputStream, extensionRegistryLite);
            }

            public static NamePart parseFrom(byte[] arrby) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(arrby);
            }

            public static NamePart parseFrom(byte[] arrby, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(arrby, extensionRegistryLite);
            }

            @Override
            public NamePart getDefaultInstanceForType() {
                return defaultInstance;
            }

            @Override
            public boolean getIsExtension() {
                return this.isExtension_;
            }

            @Override
            public String getNamePart() {
                Object object = this.namePart_;
                if (object instanceof String) {
                    return (String)object;
                }
                object = (ByteString)object;
                String string2 = object.toStringUtf8();
                if (object.isValidUtf8()) {
                    this.namePart_ = string2;
                }
                return string2;
            }

            @Override
            public ByteString getNamePartBytes() {
                Object object = this.namePart_;
                if (object instanceof String) {
                    this.namePart_ = object = ByteString.copyFromUtf8((String)object);
                    return object;
                }
                return (ByteString)object;
            }

            public Parser<NamePart> getParserForType() {
                return PARSER;
            }

            @Override
            public int getSerializedSize() {
                int n = this.memoizedSerializedSize;
                if (n != -1) {
                    return n;
                }
                n = 0;
                if ((this.bitField0_ & 1) == 1) {
                    n = 0 + CodedOutputStream.computeBytesSize(1, this.getNamePartBytes());
                }
                int n2 = n;
                if ((this.bitField0_ & 2) == 2) {
                    n2 = n + CodedOutputStream.computeBoolSize(2, this.isExtension_);
                }
                this.memoizedSerializedSize = n = n2 + this.getUnknownFields().getSerializedSize();
                return n;
            }

            @Override
            public final UnknownFieldSet getUnknownFields() {
                return this.unknownFields;
            }

            @Override
            public boolean hasIsExtension() {
                if ((this.bitField0_ & 2) == 2) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean hasNamePart() {
                if ((this.bitField0_ & 1) == 1) {
                    return true;
                }
                return false;
            }

            @Override
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable.ensureFieldAccessorsInitialized(NamePart.class, Builder.class);
            }

            @Override
            public final boolean isInitialized() {
                byte by = this.memoizedIsInitialized;
                if (by != -1) {
                    if (by == 1) {
                        return true;
                    }
                    return false;
                }
                if (!this.hasNamePart()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
                if (!this.hasIsExtension()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
                this.memoizedIsInitialized = 1;
                return true;
            }

            @Override
            public Builder newBuilderForType() {
                return NamePart.newBuilder();
            }

            @Override
            protected Builder newBuilderForType(GeneratedMessage.BuilderParent builderParent) {
                return new Builder(builderParent);
            }

            @Override
            public Builder toBuilder() {
                return NamePart.newBuilder(this);
            }

            @Override
            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            @Override
            public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
                this.getSerializedSize();
                if ((this.bitField0_ & 1) == 1) {
                    codedOutputStream.writeBytes(1, this.getNamePartBytes());
                }
                if ((this.bitField0_ & 2) == 2) {
                    codedOutputStream.writeBool(2, this.isExtension_);
                }
                this.getUnknownFields().writeTo(codedOutputStream);
            }

            public static final class Builder
            extends GeneratedMessage.Builder<Builder>
            implements NamePartOrBuilder {
                private int bitField0_;
                private boolean isExtension_;
                private Object namePart_ = "";

                private Builder() {
                    this.maybeForceBuilderInitialization();
                }

                private Builder(GeneratedMessage.BuilderParent builderParent) {
                    super(builderParent);
                    this.maybeForceBuilderInitialization();
                }

                private static Builder create() {
                    return new Builder();
                }

                public static final Descriptors.Descriptor getDescriptor() {
                    return internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
                }

                private void maybeForceBuilderInitialization() {
                    boolean bl = GeneratedMessage.alwaysUseFieldBuilders;
                }

                @Override
                public NamePart build() {
                    NamePart namePart = this.buildPartial();
                    if (namePart.isInitialized()) {
                        return namePart;
                    }
                    throw Builder.newUninitializedMessageException(namePart);
                }

                @Override
                public NamePart buildPartial() {
                    NamePart namePart = new NamePart(this);
                    int n = this.bitField0_;
                    int n2 = 0;
                    if ((n & 1) == 1) {
                        n2 = false | true;
                    }
                    namePart.namePart_ = this.namePart_;
                    int n3 = n2;
                    if ((n & 2) == 2) {
                        n3 = n2 | 2;
                    }
                    namePart.isExtension_ = this.isExtension_;
                    namePart.bitField0_ = n3;
                    this.onBuilt();
                    return namePart;
                }

                @Override
                public Builder clear() {
                    int n;
                    super.clear();
                    this.namePart_ = "";
                    this.bitField0_ = n = this.bitField0_ & -2;
                    this.isExtension_ = false;
                    this.bitField0_ = n & -3;
                    return this;
                }

                public Builder clearIsExtension() {
                    this.bitField0_ &= -3;
                    this.isExtension_ = false;
                    this.onChanged();
                    return this;
                }

                public Builder clearNamePart() {
                    this.bitField0_ &= -2;
                    this.namePart_ = NamePart.getDefaultInstance().getNamePart();
                    this.onChanged();
                    return this;
                }

                @Override
                public Builder clone() {
                    return Builder.create().mergeFrom(this.buildPartial());
                }

                @Override
                public NamePart getDefaultInstanceForType() {
                    return NamePart.getDefaultInstance();
                }

                @Override
                public Descriptors.Descriptor getDescriptorForType() {
                    return internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
                }

                @Override
                public boolean getIsExtension() {
                    return this.isExtension_;
                }

                @Override
                public String getNamePart() {
                    Object object = this.namePart_;
                    if (!(object instanceof String)) {
                        this.namePart_ = object = ((ByteString)object).toStringUtf8();
                        return object;
                    }
                    return (String)object;
                }

                @Override
                public ByteString getNamePartBytes() {
                    Object object = this.namePart_;
                    if (object instanceof String) {
                        this.namePart_ = object = ByteString.copyFromUtf8((String)object);
                        return object;
                    }
                    return (ByteString)object;
                }

                @Override
                public boolean hasIsExtension() {
                    if ((this.bitField0_ & 2) == 2) {
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean hasNamePart() {
                    if ((this.bitField0_ & 1) == 1) {
                        return true;
                    }
                    return false;
                }

                @Override
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable.ensureFieldAccessorsInitialized(NamePart.class, Builder.class);
                }

                @Override
                public final boolean isInitialized() {
                    if (!this.hasNamePart()) {
                        return false;
                    }
                    if (!this.hasIsExtension()) {
                        return false;
                    }
                    return true;
                }

                /*
                 * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
                 * Loose catch block
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 * Lifted jumps to return sites
                 */
                @Override
                public Builder mergeFrom(CodedInputStream object, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                    Object object2;
                    Throwable throwable222;
                    Object object3 = object2 = null;
                    object = NamePart.PARSER.parsePartialFrom((CodedInputStream)object, extensionRegistryLite);
                    if (object == null) return this;
                    this.mergeFrom((NamePart)object);
                    return this;
                    {
                        catch (Throwable throwable222) {
                        }
                        catch (InvalidProtocolBufferException invalidProtocolBufferException) {}
                        object3 = object2;
                        {
                            object3 = object = (NamePart)invalidProtocolBufferException.getUnfinishedMessage();
                            throw invalidProtocolBufferException;
                        }
                    }
                    if (object3 == null) throw throwable222;
                    this.mergeFrom((NamePart)object3);
                    throw throwable222;
                }

                public Builder mergeFrom(NamePart namePart) {
                    if (namePart == NamePart.getDefaultInstance()) {
                        return this;
                    }
                    if (namePart.hasNamePart()) {
                        this.bitField0_ |= 1;
                        this.namePart_ = namePart.namePart_;
                        this.onChanged();
                    }
                    if (namePart.hasIsExtension()) {
                        this.setIsExtension(namePart.getIsExtension());
                    }
                    this.mergeUnknownFields(namePart.getUnknownFields());
                    return this;
                }

                @Override
                public Builder mergeFrom(Message message) {
                    if (message instanceof NamePart) {
                        return this.mergeFrom((NamePart)message);
                    }
                    super.mergeFrom(message);
                    return this;
                }

                public Builder setIsExtension(boolean bl) {
                    this.bitField0_ |= 2;
                    this.isExtension_ = bl;
                    this.onChanged();
                    return this;
                }

                public Builder setNamePart(String string2) {
                    if (string2 != null) {
                        this.bitField0_ |= 1;
                        this.namePart_ = string2;
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }

                public Builder setNamePartBytes(ByteString byteString) {
                    if (byteString != null) {
                        this.bitField0_ |= 1;
                        this.namePart_ = byteString;
                        this.onChanged();
                        return this;
                    }
                    throw null;
                }
            }

        }

        public static interface NamePartOrBuilder
        extends MessageOrBuilder {
            public boolean getIsExtension();

            public String getNamePart();

            public ByteString getNamePartBytes();

            public boolean hasIsExtension();

            public boolean hasNamePart();
        }

    }

    public static interface UninterpretedOptionOrBuilder
    extends MessageOrBuilder {
        public String getAggregateValue();

        public ByteString getAggregateValueBytes();

        public double getDoubleValue();

        public String getIdentifierValue();

        public ByteString getIdentifierValueBytes();

        public UninterpretedOption.NamePart getName(int var1);

        public int getNameCount();

        public List<UninterpretedOption.NamePart> getNameList();

        public UninterpretedOption.NamePartOrBuilder getNameOrBuilder(int var1);

        public List<? extends UninterpretedOption.NamePartOrBuilder> getNameOrBuilderList();

        public long getNegativeIntValue();

        public long getPositiveIntValue();

        public ByteString getStringValue();

        public boolean hasAggregateValue();

        public boolean hasDoubleValue();

        public boolean hasIdentifierValue();

        public boolean hasNegativeIntValue();

        public boolean hasPositiveIntValue();

        public boolean hasStringValue();
    }

}

