package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
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
      Descriptors.FileDescriptor.InternalDescriptorAssigner var0 = new Descriptors.FileDescriptor.InternalDescriptorAssigner() {
         public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor var1) {
            DescriptorProtos.descriptor = var1;
            DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(0);
            DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor, new String[]{"File"});
            DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(1);
            DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor, new String[]{"Name", "Package", "Dependency", "PublicDependency", "WeakDependency", "MessageType", "EnumType", "Service", "Extension", "Options", "SourceCodeInfo"});
            DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(2);
            DescriptorProtos.internal_static_google_protobuf_DescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor, new String[]{"Name", "Field", "Extension", "NestedType", "EnumType", "ExtensionRange", "Options"});
            DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor = (Descriptors.Descriptor)DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor.getNestedTypes().get(0);
            DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor, new String[]{"Start", "End"});
            DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(3);
            DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor, new String[]{"Name", "Number", "Label", "Type", "TypeName", "Extendee", "DefaultValue", "Options"});
            DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(4);
            DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor, new String[]{"Name", "Value", "Options"});
            DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(5);
            DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_descriptor, new String[]{"Name", "Number", "Options"});
            DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(6);
            DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_descriptor, new String[]{"Name", "Method", "Options"});
            DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(7);
            DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor, new String[]{"Name", "InputType", "OutputType", "Options"});
            DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(8);
            DescriptorProtos.internal_static_google_protobuf_FileOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor, new String[]{"JavaPackage", "JavaOuterClassname", "JavaMultipleFiles", "JavaGenerateEqualsAndHash", "OptimizeFor", "GoPackage", "CcGenericServices", "JavaGenericServices", "PyGenericServices", "UninterpretedOption"});
            DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(9);
            DescriptorProtos.internal_static_google_protobuf_MessageOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor, new String[]{"MessageSetWireFormat", "NoStandardDescriptorAccessor", "UninterpretedOption"});
            DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(10);
            DescriptorProtos.internal_static_google_protobuf_FieldOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor, new String[]{"Ctype", "Packed", "Lazy", "Deprecated", "ExperimentalMapKey", "Weak", "UninterpretedOption"});
            DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(11);
            DescriptorProtos.internal_static_google_protobuf_EnumOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor, new String[]{"AllowAlias", "UninterpretedOption"});
            DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(12);
            DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor, new String[]{"UninterpretedOption"});
            DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(13);
            DescriptorProtos.internal_static_google_protobuf_ServiceOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor, new String[]{"UninterpretedOption"});
            DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(14);
            DescriptorProtos.internal_static_google_protobuf_MethodOptions_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor, new String[]{"UninterpretedOption"});
            DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(15);
            DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor, new String[]{"Name", "IdentifierValue", "PositiveIntValue", "NegativeIntValue", "DoubleValue", "StringValue", "AggregateValue"});
            DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor = (Descriptors.Descriptor)DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor.getNestedTypes().get(0);
            DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor, new String[]{"NamePart", "IsExtension"});
            DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor = (Descriptors.Descriptor)DescriptorProtos.getDescriptor().getMessageTypes().get(16);
            DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor, new String[]{"Location"});
            DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_descriptor = (Descriptors.Descriptor)DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor.getNestedTypes().get(0);
            DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_descriptor, new String[]{"Path", "Span", "LeadingComments", "TrailingComments"});
            return null;
         }
      };
      Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(new String[]{"\n google/protobuf/descriptor.proto\u0012\u000fgoogle.protobuf\"G\n\u0011FileDescriptorSet\u00122\n\u0004file\u0018\u0001 \u0003(\u000b2$.google.protobuf.FileDescriptorProto\"Ë\u0003\n\u0013FileDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000f\n\u0007package\u0018\u0002 \u0001(\t\u0012\u0012\n\ndependency\u0018\u0003 \u0003(\t\u0012\u0019\n\u0011public_dependency\u0018\n \u0003(\u0005\u0012\u0017\n\u000fweak_dependency\u0018\u000b \u0003(\u0005\u00126\n\fmessage_type\u0018\u0004 \u0003(\u000b2 .google.protobuf.DescriptorProto\u00127\n\tenum_type\u0018\u0005 \u0003(\u000b2$.google.protobuf.EnumDescriptorProto\u00128\n\u0007service\u0018\u0006 \u0003(\u000b2'.google.protobuf.", "ServiceDescriptorProto\u00128\n\textension\u0018\u0007 \u0003(\u000b2%.google.protobuf.FieldDescriptorProto\u0012-\n\u0007options\u0018\b \u0001(\u000b2\u001c.google.protobuf.FileOptions\u00129\n\u0010source_code_info\u0018\t \u0001(\u000b2\u001f.google.protobuf.SourceCodeInfo\"©\u0003\n\u000fDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00124\n\u0005field\u0018\u0002 \u0003(\u000b2%.google.protobuf.FieldDescriptorProto\u00128\n\textension\u0018\u0006 \u0003(\u000b2%.google.protobuf.FieldDescriptorProto\u00125\n\u000bnested_type\u0018\u0003 \u0003(\u000b2 .google.protobuf.DescriptorProto\u00127\n\tenum_type", "\u0018\u0004 \u0003(\u000b2$.google.protobuf.EnumDescriptorProto\u0012H\n\u000fextension_range\u0018\u0005 \u0003(\u000b2/.google.protobuf.DescriptorProto.ExtensionRange\u00120\n\u0007options\u0018\u0007 \u0001(\u000b2\u001f.google.protobuf.MessageOptions\u001a,\n\u000eExtensionRange\u0012\r\n\u0005start\u0018\u0001 \u0001(\u0005\u0012\u000b\n\u0003end\u0018\u0002 \u0001(\u0005\"\u0094\u0005\n\u0014FieldDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000e\n\u0006number\u0018\u0003 \u0001(\u0005\u0012:\n\u0005label\u0018\u0004 \u0001(\u000e2+.google.protobuf.FieldDescriptorProto.Label\u00128\n\u0004type\u0018\u0005 \u0001(\u000e2*.google.protobuf.FieldDescriptorProto.Type\u0012\u0011\n\ttype_name", "\u0018\u0006 \u0001(\t\u0012\u0010\n\bextendee\u0018\u0002 \u0001(\t\u0012\u0015\n\rdefault_value\u0018\u0007 \u0001(\t\u0012.\n\u0007options\u0018\b \u0001(\u000b2\u001d.google.protobuf.FieldOptions\"¶\u0002\n\u0004Type\u0012\u000f\n\u000bTYPE_DOUBLE\u0010\u0001\u0012\u000e\n\nTYPE_FLOAT\u0010\u0002\u0012\u000e\n\nTYPE_INT64\u0010\u0003\u0012\u000f\n\u000bTYPE_UINT64\u0010\u0004\u0012\u000e\n\nTYPE_INT32\u0010\u0005\u0012\u0010\n\fTYPE_FIXED64\u0010\u0006\u0012\u0010\n\fTYPE_FIXED32\u0010\u0007\u0012\r\n\tTYPE_BOOL\u0010\b\u0012\u000f\n\u000bTYPE_STRING\u0010\t\u0012\u000e\n\nTYPE_GROUP\u0010\n\u0012\u0010\n\fTYPE_MESSAGE\u0010\u000b\u0012\u000e\n\nTYPE_BYTES\u0010\f\u0012\u000f\n\u000bTYPE_UINT32\u0010\r\u0012\r\n\tTYPE_ENUM\u0010\u000e\u0012\u0011\n\rTYPE_SFIXED32\u0010\u000f\u0012\u0011\n\rTYPE_SFIXED64\u0010\u0010\u0012\u000f\n\u000bTYPE_SINT32\u0010\u0011\u0012\u000f\n\u000bTYPE_", "SINT64\u0010\u0012\"C\n\u0005Label\u0012\u0012\n\u000eLABEL_OPTIONAL\u0010\u0001\u0012\u0012\n\u000eLABEL_REQUIRED\u0010\u0002\u0012\u0012\n\u000eLABEL_REPEATED\u0010\u0003\"\u008c\u0001\n\u0013EnumDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00128\n\u0005value\u0018\u0002 \u0003(\u000b2).google.protobuf.EnumValueDescriptorProto\u0012-\n\u0007options\u0018\u0003 \u0001(\u000b2\u001c.google.protobuf.EnumOptions\"l\n\u0018EnumValueDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000e\n\u0006number\u0018\u0002 \u0001(\u0005\u00122\n\u0007options\u0018\u0003 \u0001(\u000b2!.google.protobuf.EnumValueOptions\"\u0090\u0001\n\u0016ServiceDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u00126\n\u0006method\u0018\u0002 \u0003(\u000b2&.google.pro", "tobuf.MethodDescriptorProto\u00120\n\u0007options\u0018\u0003 \u0001(\u000b2\u001f.google.protobuf.ServiceOptions\"\u007f\n\u0015MethodDescriptorProto\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u0012\n\ninput_type\u0018\u0002 \u0001(\t\u0012\u0013\n\u000boutput_type\u0018\u0003 \u0001(\t\u0012/\n\u0007options\u0018\u0004 \u0001(\u000b2\u001e.google.protobuf.MethodOptions\"é\u0003\n\u000bFileOptions\u0012\u0014\n\fjava_package\u0018\u0001 \u0001(\t\u0012\u001c\n\u0014java_outer_classname\u0018\b \u0001(\t\u0012\"\n\u0013java_multiple_files\u0018\n \u0001(\b:\u0005false\u0012,\n\u001djava_generate_equals_and_hash\u0018\u0014 \u0001(\b:\u0005false\u0012F\n\foptimize_for\u0018\t \u0001(\u000e2).google.protobuf.Fil", "eOptions.OptimizeMode:\u0005SPEED\u0012\u0012\n\ngo_package\u0018\u000b \u0001(\t\u0012\"\n\u0013cc_generic_services\u0018\u0010 \u0001(\b:\u0005false\u0012$\n\u0015java_generic_services\u0018\u0011 \u0001(\b:\u0005false\u0012\"\n\u0013py_generic_services\u0018\u0012 \u0001(\b:\u0005false\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption\":\n\fOptimizeMode\u0012\t\n\u0005SPEED\u0010\u0001\u0012\r\n\tCODE_SIZE\u0010\u0002\u0012\u0010\n\fLITE_RUNTIME\u0010\u0003*\t\bè\u0007\u0010\u0080\u0080\u0080\u0080\u0002\"¸\u0001\n\u000eMessageOptions\u0012&\n\u0017message_set_wire_format\u0018\u0001 \u0001(\b:\u0005false\u0012.\n\u001fno_standard_descriptor_accessor\u0018\u0002 \u0001(\b:\u0005", "false\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\bè\u0007\u0010\u0080\u0080\u0080\u0080\u0002\"¾\u0002\n\fFieldOptions\u0012:\n\u0005ctype\u0018\u0001 \u0001(\u000e2#.google.protobuf.FieldOptions.CType:\u0006STRING\u0012\u000e\n\u0006packed\u0018\u0002 \u0001(\b\u0012\u0013\n\u0004lazy\u0018\u0005 \u0001(\b:\u0005false\u0012\u0019\n\ndeprecated\u0018\u0003 \u0001(\b:\u0005false\u0012\u001c\n\u0014experimental_map_key\u0018\t \u0001(\t\u0012\u0013\n\u0004weak\u0018\n \u0001(\b:\u0005false\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption\"/\n\u0005CType\u0012\n\n\u0006STRING\u0010\u0000\u0012\b\n\u0004CORD\u0010\u0001\u0012\u0010\n\fSTRING_PIECE\u0010\u0002*\t\bè\u0007", "\u0010\u0080\u0080\u0080\u0080\u0002\"x\n\u000bEnumOptions\u0012\u0019\n\u000ballow_alias\u0018\u0002 \u0001(\b:\u0004true\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\bè\u0007\u0010\u0080\u0080\u0080\u0080\u0002\"b\n\u0010EnumValueOptions\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\bè\u0007\u0010\u0080\u0080\u0080\u0080\u0002\"`\n\u000eServiceOptions\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.UninterpretedOption*\t\bè\u0007\u0010\u0080\u0080\u0080\u0080\u0002\"_\n\rMethodOptions\u0012C\n\u0014uninterpreted_option\u0018ç\u0007 \u0003(\u000b2$.google.protobuf.Uninter", "pretedOption*\t\bè\u0007\u0010\u0080\u0080\u0080\u0080\u0002\"\u009e\u0002\n\u0013UninterpretedOption\u0012;\n\u0004name\u0018\u0002 \u0003(\u000b2-.google.protobuf.UninterpretedOption.NamePart\u0012\u0018\n\u0010identifier_value\u0018\u0003 \u0001(\t\u0012\u001a\n\u0012positive_int_value\u0018\u0004 \u0001(\u0004\u0012\u001a\n\u0012negative_int_value\u0018\u0005 \u0001(\u0003\u0012\u0014\n\fdouble_value\u0018\u0006 \u0001(\u0001\u0012\u0014\n\fstring_value\u0018\u0007 \u0001(\f\u0012\u0017\n\u000faggregate_value\u0018\b \u0001(\t\u001a3\n\bNamePart\u0012\u0011\n\tname_part\u0018\u0001 \u0002(\t\u0012\u0014\n\fis_extension\u0018\u0002 \u0002(\b\"±\u0001\n\u000eSourceCodeInfo\u0012:\n\blocation\u0018\u0001 \u0003(\u000b2(.google.protobuf.SourceCodeInfo.Location\u001ac\n\bLocat", "ion\u0012\u0010\n\u0004path\u0018\u0001 \u0003(\u0005B\u0002\u0010\u0001\u0012\u0010\n\u0004span\u0018\u0002 \u0003(\u0005B\u0002\u0010\u0001\u0012\u0018\n\u0010leading_comments\u0018\u0003 \u0001(\t\u0012\u0019\n\u0011trailing_comments\u0018\u0004 \u0001(\tB)\n\u0013com.google.protobufB\u0010DescriptorProtosH\u0001"}, new Descriptors.FileDescriptor[0], var0);
   }

   private DescriptorProtos() {
   }

   public static Descriptors.FileDescriptor getDescriptor() {
      return descriptor;
   }

   public static void registerAllExtensions(ExtensionRegistry var0) {
   }

   public static final class DescriptorProto extends GeneratedMessage implements DescriptorProtos.DescriptorProtoOrBuilder {
      public static final int ENUM_TYPE_FIELD_NUMBER = 4;
      public static final int EXTENSION_FIELD_NUMBER = 6;
      public static final int EXTENSION_RANGE_FIELD_NUMBER = 5;
      public static final int FIELD_FIELD_NUMBER = 2;
      public static final int NAME_FIELD_NUMBER = 1;
      public static final int NESTED_TYPE_FIELD_NUMBER = 3;
      public static final int OPTIONS_FIELD_NUMBER = 7;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.DescriptorProto parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.DescriptorProto(var1, var2);
         }
      };
      private static final DescriptorProtos.DescriptorProto defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private List enumType_;
      private List extensionRange_;
      private List extension_;
      private List field_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private Object name_;
      private List nestedType_;
      private DescriptorProtos.MessageOptions options_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.DescriptorProto var0 = new DescriptorProtos.DescriptorProto(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private DescriptorProto(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      DescriptorProto(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private DescriptorProto(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      DescriptorProto(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private DescriptorProto(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.DescriptorProto getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor;
      }

      private void initFields() {
         this.name_ = "";
         this.field_ = Collections.emptyList();
         this.extension_ = Collections.emptyList();
         this.nestedType_ = Collections.emptyList();
         this.enumType_ = Collections.emptyList();
         this.extensionRange_ = Collections.emptyList();
         this.options_ = DescriptorProtos.MessageOptions.getDefaultInstance();
      }

      public static DescriptorProtos.DescriptorProto.Builder newBuilder() {
         return DescriptorProtos.DescriptorProto.Builder.create();
      }

      public static DescriptorProtos.DescriptorProto.Builder newBuilder(DescriptorProtos.DescriptorProto var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.DescriptorProto parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.DescriptorProto parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.DescriptorProto parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.DescriptorProto parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.DescriptorProto parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.DescriptorProto parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.DescriptorProto parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.DescriptorProto parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.DescriptorProto parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.DescriptorProto parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.DescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.DescriptorProto getDefaultInstanceForType() {
         return defaultInstance;
      }

      public DescriptorProtos.EnumDescriptorProto getEnumType(int var1) {
         return (DescriptorProtos.EnumDescriptorProto)this.enumType_.get(var1);
      }

      public int getEnumTypeCount() {
         return this.enumType_.size();
      }

      public List getEnumTypeList() {
         return this.enumType_;
      }

      public DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int var1) {
         return (DescriptorProtos.EnumDescriptorProtoOrBuilder)this.enumType_.get(var1);
      }

      public List getEnumTypeOrBuilderList() {
         return this.enumType_;
      }

      public DescriptorProtos.FieldDescriptorProto getExtension(int var1) {
         return (DescriptorProtos.FieldDescriptorProto)this.extension_.get(var1);
      }

      public int getExtensionCount() {
         return this.extension_.size();
      }

      public List getExtensionList() {
         return this.extension_;
      }

      public DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int var1) {
         return (DescriptorProtos.FieldDescriptorProtoOrBuilder)this.extension_.get(var1);
      }

      public List getExtensionOrBuilderList() {
         return this.extension_;
      }

      public DescriptorProtos.DescriptorProto.ExtensionRange getExtensionRange(int var1) {
         return (DescriptorProtos.DescriptorProto.ExtensionRange)this.extensionRange_.get(var1);
      }

      public int getExtensionRangeCount() {
         return this.extensionRange_.size();
      }

      public List getExtensionRangeList() {
         return this.extensionRange_;
      }

      public DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int var1) {
         return (DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder)this.extensionRange_.get(var1);
      }

      public List getExtensionRangeOrBuilderList() {
         return this.extensionRange_;
      }

      public DescriptorProtos.FieldDescriptorProto getField(int var1) {
         return (DescriptorProtos.FieldDescriptorProto)this.field_.get(var1);
      }

      public int getFieldCount() {
         return this.field_.size();
      }

      public List getFieldList() {
         return this.field_;
      }

      public DescriptorProtos.FieldDescriptorProtoOrBuilder getFieldOrBuilder(int var1) {
         return (DescriptorProtos.FieldDescriptorProtoOrBuilder)this.field_.get(var1);
      }

      public List getFieldOrBuilderList() {
         return this.field_;
      }

      public String getName() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.name_ = var2;
            }

            return var2;
         }
      }

      public ByteString getNameBytes() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.name_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.DescriptorProto getNestedType(int var1) {
         return (DescriptorProtos.DescriptorProto)this.nestedType_.get(var1);
      }

      public int getNestedTypeCount() {
         return this.nestedType_.size();
      }

      public List getNestedTypeList() {
         return this.nestedType_;
      }

      public DescriptorProtos.DescriptorProtoOrBuilder getNestedTypeOrBuilder(int var1) {
         return (DescriptorProtos.DescriptorProtoOrBuilder)this.nestedType_.get(var1);
      }

      public List getNestedTypeOrBuilderList() {
         return this.nestedType_;
      }

      public DescriptorProtos.MessageOptions getOptions() {
         return this.options_;
      }

      public DescriptorProtos.MessageOptionsOrBuilder getOptionsOrBuilder() {
         return this.options_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            var1 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var1 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }

            int var2;
            for(var2 = 0; var2 < this.field_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(2, (MessageLite)this.field_.get(var2));
            }

            for(var2 = 0; var2 < this.nestedType_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(3, (MessageLite)this.nestedType_.get(var2));
            }

            for(var2 = 0; var2 < this.enumType_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(4, (MessageLite)this.enumType_.get(var2));
            }

            for(var2 = 0; var2 < this.extensionRange_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(5, (MessageLite)this.extensionRange_.get(var2));
            }

            for(var2 = 0; var2 < this.extension_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(6, (MessageLite)this.extension_.get(var2));
            }

            var2 = var1;
            if ((this.bitField0_ & 2) == 2) {
               var2 = var1 + CodedOutputStream.computeMessageSize(7, this.options_);
            }

            var1 = var2 + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasName() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasOptions() {
         return (this.bitField0_ & 2) == 2;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.DescriptorProto.class, DescriptorProtos.DescriptorProto.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         if (var1 != -1) {
            return var1 == 1;
         } else {
            int var2;
            for(var2 = 0; var2 < this.getFieldCount(); ++var2) {
               if (!this.getField(var2).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            for(var2 = 0; var2 < this.getExtensionCount(); ++var2) {
               if (!this.getExtension(var2).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            for(var2 = 0; var2 < this.getNestedTypeCount(); ++var2) {
               if (!this.getNestedType(var2).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            for(var2 = 0; var2 < this.getEnumTypeCount(); ++var2) {
               if (!this.getEnumType(var2).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (this.hasOptions() && !this.getOptions().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.DescriptorProto.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.DescriptorProto.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.DescriptorProto.Builder(var1);
      }

      public DescriptorProtos.DescriptorProto.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(1, this.getNameBytes());
         }

         int var2;
         for(var2 = 0; var2 < this.field_.size(); ++var2) {
            var1.writeMessage(2, (MessageLite)this.field_.get(var2));
         }

         for(var2 = 0; var2 < this.nestedType_.size(); ++var2) {
            var1.writeMessage(3, (MessageLite)this.nestedType_.get(var2));
         }

         for(var2 = 0; var2 < this.enumType_.size(); ++var2) {
            var1.writeMessage(4, (MessageLite)this.enumType_.get(var2));
         }

         for(var2 = 0; var2 < this.extensionRange_.size(); ++var2) {
            var1.writeMessage(5, (MessageLite)this.extensionRange_.get(var2));
         }

         for(var2 = 0; var2 < this.extension_.size(); ++var2) {
            var1.writeMessage(6, (MessageLite)this.extension_.get(var2));
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeMessage(7, this.options_);
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.DescriptorProtoOrBuilder {
         private int bitField0_;
         private RepeatedFieldBuilder enumTypeBuilder_;
         private List enumType_;
         private RepeatedFieldBuilder extensionBuilder_;
         private RepeatedFieldBuilder extensionRangeBuilder_;
         private List extensionRange_;
         private List extension_;
         private RepeatedFieldBuilder fieldBuilder_;
         private List field_;
         private Object name_;
         private RepeatedFieldBuilder nestedTypeBuilder_;
         private List nestedType_;
         private SingleFieldBuilder optionsBuilder_;
         private DescriptorProtos.MessageOptions options_;

         private Builder() {
            this.name_ = "";
            this.field_ = Collections.emptyList();
            this.extension_ = Collections.emptyList();
            this.nestedType_ = Collections.emptyList();
            this.enumType_ = Collections.emptyList();
            this.extensionRange_ = Collections.emptyList();
            this.options_ = DescriptorProtos.MessageOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.name_ = "";
            this.field_ = Collections.emptyList();
            this.extension_ = Collections.emptyList();
            this.nestedType_ = Collections.emptyList();
            this.enumType_ = Collections.emptyList();
            this.extensionRange_ = Collections.emptyList();
            this.options_ = DescriptorProtos.MessageOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.DescriptorProto.Builder create() {
            return new DescriptorProtos.DescriptorProto.Builder();
         }

         private void ensureEnumTypeIsMutable() {
            if ((this.bitField0_ & 16) != 16) {
               this.enumType_ = new ArrayList(this.enumType_);
               this.bitField0_ |= 16;
            }

         }

         private void ensureExtensionIsMutable() {
            if ((this.bitField0_ & 4) != 4) {
               this.extension_ = new ArrayList(this.extension_);
               this.bitField0_ |= 4;
            }

         }

         private void ensureExtensionRangeIsMutable() {
            if ((this.bitField0_ & 32) != 32) {
               this.extensionRange_ = new ArrayList(this.extensionRange_);
               this.bitField0_ |= 32;
            }

         }

         private void ensureFieldIsMutable() {
            if ((this.bitField0_ & 2) != 2) {
               this.field_ = new ArrayList(this.field_);
               this.bitField0_ |= 2;
            }

         }

         private void ensureNestedTypeIsMutable() {
            if ((this.bitField0_ & 8) != 8) {
               this.nestedType_ = new ArrayList(this.nestedType_);
               this.bitField0_ |= 8;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor;
         }

         private RepeatedFieldBuilder getEnumTypeFieldBuilder() {
            if (this.enumTypeBuilder_ == null) {
               List var2 = this.enumType_;
               boolean var1;
               if ((this.bitField0_ & 16) == 16) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.enumTypeBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.enumType_ = null;
            }

            return this.enumTypeBuilder_;
         }

         private RepeatedFieldBuilder getExtensionFieldBuilder() {
            if (this.extensionBuilder_ == null) {
               List var2 = this.extension_;
               boolean var1;
               if ((this.bitField0_ & 4) == 4) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.extensionBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.extension_ = null;
            }

            return this.extensionBuilder_;
         }

         private RepeatedFieldBuilder getExtensionRangeFieldBuilder() {
            if (this.extensionRangeBuilder_ == null) {
               List var2 = this.extensionRange_;
               boolean var1;
               if ((this.bitField0_ & 32) == 32) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.extensionRangeBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.extensionRange_ = null;
            }

            return this.extensionRangeBuilder_;
         }

         private RepeatedFieldBuilder getFieldFieldBuilder() {
            if (this.fieldBuilder_ == null) {
               List var2 = this.field_;
               boolean var1;
               if ((this.bitField0_ & 2) == 2) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.fieldBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.field_ = null;
            }

            return this.fieldBuilder_;
         }

         private RepeatedFieldBuilder getNestedTypeFieldBuilder() {
            if (this.nestedTypeBuilder_ == null) {
               List var2 = this.nestedType_;
               boolean var1;
               if ((this.bitField0_ & 8) == 8) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.nestedTypeBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.nestedType_ = null;
            }

            return this.nestedTypeBuilder_;
         }

         private SingleFieldBuilder getOptionsFieldBuilder() {
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

         public DescriptorProtos.DescriptorProto.Builder addAllEnumType(Iterable var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               this.ensureEnumTypeIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.enumType_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addAllExtension(Iterable var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            if (var2 == null) {
               this.ensureExtensionIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.extension_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addAllExtensionRange(Iterable var1) {
            RepeatedFieldBuilder var2 = this.extensionRangeBuilder_;
            if (var2 == null) {
               this.ensureExtensionRangeIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.extensionRange_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addAllField(Iterable var1) {
            RepeatedFieldBuilder var2 = this.fieldBuilder_;
            if (var2 == null) {
               this.ensureFieldIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.field_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addAllNestedType(Iterable var1) {
            RepeatedFieldBuilder var2 = this.nestedTypeBuilder_;
            if (var2 == null) {
               this.ensureNestedTypeIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.nestedType_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addEnumType(int var1, DescriptorProtos.EnumDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.enumTypeBuilder_;
            if (var3 == null) {
               this.ensureEnumTypeIsMutable();
               this.enumType_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addEnumType(int var1, DescriptorProtos.EnumDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.enumTypeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureEnumTypeIsMutable();
                  this.enumType_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addEnumType(DescriptorProtos.EnumDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               this.ensureEnumTypeIsMutable();
               this.enumType_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addEnumType(DescriptorProtos.EnumDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureEnumTypeIsMutable();
                  this.enumType_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder addEnumTypeBuilder() {
            return (DescriptorProtos.EnumDescriptorProto.Builder)this.getEnumTypeFieldBuilder().addBuilder(DescriptorProtos.EnumDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.EnumDescriptorProto.Builder addEnumTypeBuilder(int var1) {
            return (DescriptorProtos.EnumDescriptorProto.Builder)this.getEnumTypeFieldBuilder().addBuilder(var1, DescriptorProtos.EnumDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.DescriptorProto.Builder addExtension(int var1, DescriptorProtos.FieldDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.extensionBuilder_;
            if (var3 == null) {
               this.ensureExtensionIsMutable();
               this.extension_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addExtension(int var1, DescriptorProtos.FieldDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.extensionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureExtensionIsMutable();
                  this.extension_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addExtension(DescriptorProtos.FieldDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            if (var2 == null) {
               this.ensureExtensionIsMutable();
               this.extension_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addExtension(DescriptorProtos.FieldDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureExtensionIsMutable();
                  this.extension_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder addExtensionBuilder() {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getExtensionFieldBuilder().addBuilder(DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FieldDescriptorProto.Builder addExtensionBuilder(int var1) {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getExtensionFieldBuilder().addBuilder(var1, DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.DescriptorProto.Builder addExtensionRange(int var1, DescriptorProtos.DescriptorProto.ExtensionRange.Builder var2) {
            RepeatedFieldBuilder var3 = this.extensionRangeBuilder_;
            if (var3 == null) {
               this.ensureExtensionRangeIsMutable();
               this.extensionRange_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addExtensionRange(int var1, DescriptorProtos.DescriptorProto.ExtensionRange var2) {
            RepeatedFieldBuilder var3 = this.extensionRangeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureExtensionRangeIsMutable();
                  this.extensionRange_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addExtensionRange(DescriptorProtos.DescriptorProto.ExtensionRange.Builder var1) {
            RepeatedFieldBuilder var2 = this.extensionRangeBuilder_;
            if (var2 == null) {
               this.ensureExtensionRangeIsMutable();
               this.extensionRange_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addExtensionRange(DescriptorProtos.DescriptorProto.ExtensionRange var1) {
            RepeatedFieldBuilder var2 = this.extensionRangeBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureExtensionRangeIsMutable();
                  this.extensionRange_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.ExtensionRange.Builder addExtensionRangeBuilder() {
            return (DescriptorProtos.DescriptorProto.ExtensionRange.Builder)this.getExtensionRangeFieldBuilder().addBuilder(DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance());
         }

         public DescriptorProtos.DescriptorProto.ExtensionRange.Builder addExtensionRangeBuilder(int var1) {
            return (DescriptorProtos.DescriptorProto.ExtensionRange.Builder)this.getExtensionRangeFieldBuilder().addBuilder(var1, DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance());
         }

         public DescriptorProtos.DescriptorProto.Builder addField(int var1, DescriptorProtos.FieldDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.fieldBuilder_;
            if (var3 == null) {
               this.ensureFieldIsMutable();
               this.field_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addField(int var1, DescriptorProtos.FieldDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.fieldBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureFieldIsMutable();
                  this.field_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addField(DescriptorProtos.FieldDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.fieldBuilder_;
            if (var2 == null) {
               this.ensureFieldIsMutable();
               this.field_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addField(DescriptorProtos.FieldDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.fieldBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureFieldIsMutable();
                  this.field_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder addFieldBuilder() {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getFieldFieldBuilder().addBuilder(DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FieldDescriptorProto.Builder addFieldBuilder(int var1) {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getFieldFieldBuilder().addBuilder(var1, DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.DescriptorProto.Builder addNestedType(int var1, DescriptorProtos.DescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.nestedTypeBuilder_;
            if (var3 == null) {
               this.ensureNestedTypeIsMutable();
               this.nestedType_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addNestedType(int var1, DescriptorProtos.DescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.nestedTypeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureNestedTypeIsMutable();
                  this.nestedType_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addNestedType(DescriptorProtos.DescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.nestedTypeBuilder_;
            if (var2 == null) {
               this.ensureNestedTypeIsMutable();
               this.nestedType_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addNestedType(DescriptorProtos.DescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.nestedTypeBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureNestedTypeIsMutable();
                  this.nestedType_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addNestedTypeBuilder() {
            return (DescriptorProtos.DescriptorProto.Builder)this.getNestedTypeFieldBuilder().addBuilder(DescriptorProtos.DescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.DescriptorProto.Builder addNestedTypeBuilder(int var1) {
            return (DescriptorProtos.DescriptorProto.Builder)this.getNestedTypeFieldBuilder().addBuilder(var1, DescriptorProtos.DescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.DescriptorProto build() {
            DescriptorProtos.DescriptorProto var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.DescriptorProto buildPartial() {
            DescriptorProtos.DescriptorProto var4 = new DescriptorProtos.DescriptorProto(this);
            int var3 = this.bitField0_;
            int var1 = 0;
            if ((var3 & 1) == 1) {
               var1 = 0 | 1;
            }

            var4.name_ = this.name_;
            RepeatedFieldBuilder var5 = this.fieldBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 2) == 2) {
                  this.field_ = Collections.unmodifiableList(this.field_);
                  this.bitField0_ &= -3;
               }

               var4.field_ = this.field_;
            } else {
               var4.field_ = var5.build();
            }

            var5 = this.extensionBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 4) == 4) {
                  this.extension_ = Collections.unmodifiableList(this.extension_);
                  this.bitField0_ &= -5;
               }

               var4.extension_ = this.extension_;
            } else {
               var4.extension_ = var5.build();
            }

            var5 = this.nestedTypeBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 8) == 8) {
                  this.nestedType_ = Collections.unmodifiableList(this.nestedType_);
                  this.bitField0_ &= -9;
               }

               var4.nestedType_ = this.nestedType_;
            } else {
               var4.nestedType_ = var5.build();
            }

            var5 = this.enumTypeBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 16) == 16) {
                  this.enumType_ = Collections.unmodifiableList(this.enumType_);
                  this.bitField0_ &= -17;
               }

               var4.enumType_ = this.enumType_;
            } else {
               var4.enumType_ = var5.build();
            }

            var5 = this.extensionRangeBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 32) == 32) {
                  this.extensionRange_ = Collections.unmodifiableList(this.extensionRange_);
                  this.bitField0_ &= -33;
               }

               var4.extensionRange_ = this.extensionRange_;
            } else {
               var4.extensionRange_ = var5.build();
            }

            int var2 = var1;
            if ((var3 & 64) == 64) {
               var2 = var1 | 2;
            }

            SingleFieldBuilder var6 = this.optionsBuilder_;
            if (var6 == null) {
               var4.options_ = this.options_;
            } else {
               var4.options_ = (DescriptorProtos.MessageOptions)var6.build();
            }

            var4.bitField0_ = var2;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.DescriptorProto.Builder clear() {
            super.clear();
            this.name_ = "";
            this.bitField0_ &= -2;
            RepeatedFieldBuilder var1 = this.fieldBuilder_;
            if (var1 == null) {
               this.field_ = Collections.emptyList();
               this.bitField0_ &= -3;
            } else {
               var1.clear();
            }

            var1 = this.extensionBuilder_;
            if (var1 == null) {
               this.extension_ = Collections.emptyList();
               this.bitField0_ &= -5;
            } else {
               var1.clear();
            }

            var1 = this.nestedTypeBuilder_;
            if (var1 == null) {
               this.nestedType_ = Collections.emptyList();
               this.bitField0_ &= -9;
            } else {
               var1.clear();
            }

            var1 = this.enumTypeBuilder_;
            if (var1 == null) {
               this.enumType_ = Collections.emptyList();
               this.bitField0_ &= -17;
            } else {
               var1.clear();
            }

            var1 = this.extensionRangeBuilder_;
            if (var1 == null) {
               this.extensionRange_ = Collections.emptyList();
               this.bitField0_ &= -33;
            } else {
               var1.clear();
            }

            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = DescriptorProtos.MessageOptions.getDefaultInstance();
            } else {
               var2.clear();
            }

            this.bitField0_ &= -65;
            return this;
         }

         public DescriptorProtos.DescriptorProto.Builder clearEnumType() {
            RepeatedFieldBuilder var1 = this.enumTypeBuilder_;
            if (var1 == null) {
               this.enumType_ = Collections.emptyList();
               this.bitField0_ &= -17;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder clearExtension() {
            RepeatedFieldBuilder var1 = this.extensionBuilder_;
            if (var1 == null) {
               this.extension_ = Collections.emptyList();
               this.bitField0_ &= -5;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder clearExtensionRange() {
            RepeatedFieldBuilder var1 = this.extensionRangeBuilder_;
            if (var1 == null) {
               this.extensionRange_ = Collections.emptyList();
               this.bitField0_ &= -33;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder clearField() {
            RepeatedFieldBuilder var1 = this.fieldBuilder_;
            if (var1 == null) {
               this.field_ = Collections.emptyList();
               this.bitField0_ &= -3;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder clearName() {
            this.bitField0_ &= -2;
            this.name_ = DescriptorProtos.DescriptorProto.getDefaultInstance().getName();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.DescriptorProto.Builder clearNestedType() {
            RepeatedFieldBuilder var1 = this.nestedTypeBuilder_;
            if (var1 == null) {
               this.nestedType_ = Collections.emptyList();
               this.bitField0_ &= -9;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder clearOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            if (var1 == null) {
               this.options_ = DescriptorProtos.MessageOptions.getDefaultInstance();
               this.onChanged();
            } else {
               var1.clear();
            }

            this.bitField0_ &= -65;
            return this;
         }

         public DescriptorProtos.DescriptorProto.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.DescriptorProto getDefaultInstanceForType() {
            return DescriptorProtos.DescriptorProto.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor;
         }

         public DescriptorProtos.EnumDescriptorProto getEnumType(int var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            return var2 == null ? (DescriptorProtos.EnumDescriptorProto)this.enumType_.get(var1) : (DescriptorProtos.EnumDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.EnumDescriptorProto.Builder getEnumTypeBuilder(int var1) {
            return (DescriptorProtos.EnumDescriptorProto.Builder)this.getEnumTypeFieldBuilder().getBuilder(var1);
         }

         public List getEnumTypeBuilderList() {
            return this.getEnumTypeFieldBuilder().getBuilderList();
         }

         public int getEnumTypeCount() {
            RepeatedFieldBuilder var1 = this.enumTypeBuilder_;
            return var1 == null ? this.enumType_.size() : var1.getCount();
         }

         public List getEnumTypeList() {
            RepeatedFieldBuilder var1 = this.enumTypeBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.enumType_) : var1.getMessageList();
         }

         public DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            return var2 == null ? (DescriptorProtos.EnumDescriptorProtoOrBuilder)this.enumType_.get(var1) : (DescriptorProtos.EnumDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getEnumTypeOrBuilderList() {
            RepeatedFieldBuilder var1 = this.enumTypeBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.enumType_);
         }

         public DescriptorProtos.FieldDescriptorProto getExtension(int var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            return var2 == null ? (DescriptorProtos.FieldDescriptorProto)this.extension_.get(var1) : (DescriptorProtos.FieldDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.FieldDescriptorProto.Builder getExtensionBuilder(int var1) {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getExtensionFieldBuilder().getBuilder(var1);
         }

         public List getExtensionBuilderList() {
            return this.getExtensionFieldBuilder().getBuilderList();
         }

         public int getExtensionCount() {
            RepeatedFieldBuilder var1 = this.extensionBuilder_;
            return var1 == null ? this.extension_.size() : var1.getCount();
         }

         public List getExtensionList() {
            RepeatedFieldBuilder var1 = this.extensionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.extension_) : var1.getMessageList();
         }

         public DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            return var2 == null ? (DescriptorProtos.FieldDescriptorProtoOrBuilder)this.extension_.get(var1) : (DescriptorProtos.FieldDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getExtensionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.extensionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.extension_);
         }

         public DescriptorProtos.DescriptorProto.ExtensionRange getExtensionRange(int var1) {
            RepeatedFieldBuilder var2 = this.extensionRangeBuilder_;
            return var2 == null ? (DescriptorProtos.DescriptorProto.ExtensionRange)this.extensionRange_.get(var1) : (DescriptorProtos.DescriptorProto.ExtensionRange)var2.getMessage(var1);
         }

         public DescriptorProtos.DescriptorProto.ExtensionRange.Builder getExtensionRangeBuilder(int var1) {
            return (DescriptorProtos.DescriptorProto.ExtensionRange.Builder)this.getExtensionRangeFieldBuilder().getBuilder(var1);
         }

         public List getExtensionRangeBuilderList() {
            return this.getExtensionRangeFieldBuilder().getBuilderList();
         }

         public int getExtensionRangeCount() {
            RepeatedFieldBuilder var1 = this.extensionRangeBuilder_;
            return var1 == null ? this.extensionRange_.size() : var1.getCount();
         }

         public List getExtensionRangeList() {
            RepeatedFieldBuilder var1 = this.extensionRangeBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.extensionRange_) : var1.getMessageList();
         }

         public DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.extensionRangeBuilder_;
            return var2 == null ? (DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder)this.extensionRange_.get(var1) : (DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getExtensionRangeOrBuilderList() {
            RepeatedFieldBuilder var1 = this.extensionRangeBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.extensionRange_);
         }

         public DescriptorProtos.FieldDescriptorProto getField(int var1) {
            RepeatedFieldBuilder var2 = this.fieldBuilder_;
            return var2 == null ? (DescriptorProtos.FieldDescriptorProto)this.field_.get(var1) : (DescriptorProtos.FieldDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.FieldDescriptorProto.Builder getFieldBuilder(int var1) {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getFieldFieldBuilder().getBuilder(var1);
         }

         public List getFieldBuilderList() {
            return this.getFieldFieldBuilder().getBuilderList();
         }

         public int getFieldCount() {
            RepeatedFieldBuilder var1 = this.fieldBuilder_;
            return var1 == null ? this.field_.size() : var1.getCount();
         }

         public List getFieldList() {
            RepeatedFieldBuilder var1 = this.fieldBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.field_) : var1.getMessageList();
         }

         public DescriptorProtos.FieldDescriptorProtoOrBuilder getFieldOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.fieldBuilder_;
            return var2 == null ? (DescriptorProtos.FieldDescriptorProtoOrBuilder)this.field_.get(var1) : (DescriptorProtos.FieldDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getFieldOrBuilderList() {
            RepeatedFieldBuilder var1 = this.fieldBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.field_);
         }

         public String getName() {
            Object var1 = this.name_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.name_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getNameBytes() {
            Object var1 = this.name_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.name_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.DescriptorProto getNestedType(int var1) {
            RepeatedFieldBuilder var2 = this.nestedTypeBuilder_;
            return var2 == null ? (DescriptorProtos.DescriptorProto)this.nestedType_.get(var1) : (DescriptorProtos.DescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.DescriptorProto.Builder getNestedTypeBuilder(int var1) {
            return (DescriptorProtos.DescriptorProto.Builder)this.getNestedTypeFieldBuilder().getBuilder(var1);
         }

         public List getNestedTypeBuilderList() {
            return this.getNestedTypeFieldBuilder().getBuilderList();
         }

         public int getNestedTypeCount() {
            RepeatedFieldBuilder var1 = this.nestedTypeBuilder_;
            return var1 == null ? this.nestedType_.size() : var1.getCount();
         }

         public List getNestedTypeList() {
            RepeatedFieldBuilder var1 = this.nestedTypeBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.nestedType_) : var1.getMessageList();
         }

         public DescriptorProtos.DescriptorProtoOrBuilder getNestedTypeOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.nestedTypeBuilder_;
            return var2 == null ? (DescriptorProtos.DescriptorProtoOrBuilder)this.nestedType_.get(var1) : (DescriptorProtos.DescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getNestedTypeOrBuilderList() {
            RepeatedFieldBuilder var1 = this.nestedTypeBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.nestedType_);
         }

         public DescriptorProtos.MessageOptions getOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return var1 == null ? this.options_ : (DescriptorProtos.MessageOptions)var1.getMessage();
         }

         public DescriptorProtos.MessageOptions.Builder getOptionsBuilder() {
            this.bitField0_ |= 64;
            this.onChanged();
            return (DescriptorProtos.MessageOptions.Builder)this.getOptionsFieldBuilder().getBuilder();
         }

         public DescriptorProtos.MessageOptionsOrBuilder getOptionsOrBuilder() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return (DescriptorProtos.MessageOptionsOrBuilder)(var1 != null ? (DescriptorProtos.MessageOptionsOrBuilder)var1.getMessageOrBuilder() : this.options_);
         }

         public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasOptions() {
            return (this.bitField0_ & 64) == 64;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.DescriptorProto.class, DescriptorProtos.DescriptorProto.Builder.class);
         }

         public final boolean isInitialized() {
            int var1;
            for(var1 = 0; var1 < this.getFieldCount(); ++var1) {
               if (!this.getField(var1).isInitialized()) {
                  return false;
               }
            }

            for(var1 = 0; var1 < this.getExtensionCount(); ++var1) {
               if (!this.getExtension(var1).isInitialized()) {
                  return false;
               }
            }

            for(var1 = 0; var1 < this.getNestedTypeCount(); ++var1) {
               if (!this.getNestedType(var1).isInitialized()) {
                  return false;
               }
            }

            for(var1 = 0; var1 < this.getEnumTypeCount(); ++var1) {
               if (!this.getEnumType(var1).isInitialized()) {
                  return false;
               }
            }

            if (this.hasOptions() && !this.getOptions().isInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.DescriptorProto var3 = (DescriptorProtos.DescriptorProto)var4;

            DescriptorProtos.DescriptorProto var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.DescriptorProto)DescriptorProtos.DescriptorProto.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.DescriptorProto)var4;

                  try {
                     var21 = (DescriptorProtos.DescriptorProto)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.DescriptorProto.Builder mergeFrom(DescriptorProtos.DescriptorProto var1) {
            if (var1 == DescriptorProtos.DescriptorProto.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasName()) {
                  this.bitField0_ |= 1;
                  this.name_ = var1.name_;
                  this.onChanged();
               }

               RepeatedFieldBuilder var2 = this.fieldBuilder_;
               Object var3 = null;
               if (var2 == null) {
                  if (!var1.field_.isEmpty()) {
                     if (this.field_.isEmpty()) {
                        this.field_ = var1.field_;
                        this.bitField0_ &= -3;
                     } else {
                        this.ensureFieldIsMutable();
                        this.field_.addAll(var1.field_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.field_.isEmpty()) {
                  if (this.fieldBuilder_.isEmpty()) {
                     this.fieldBuilder_.dispose();
                     this.fieldBuilder_ = null;
                     this.field_ = var1.field_;
                     this.bitField0_ &= -3;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getFieldFieldBuilder();
                     } else {
                        var2 = null;
                     }

                     this.fieldBuilder_ = var2;
                  } else {
                     this.fieldBuilder_.addAllMessages(var1.field_);
                  }
               }

               if (this.extensionBuilder_ == null) {
                  if (!var1.extension_.isEmpty()) {
                     if (this.extension_.isEmpty()) {
                        this.extension_ = var1.extension_;
                        this.bitField0_ &= -5;
                     } else {
                        this.ensureExtensionIsMutable();
                        this.extension_.addAll(var1.extension_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.extension_.isEmpty()) {
                  if (this.extensionBuilder_.isEmpty()) {
                     this.extensionBuilder_.dispose();
                     this.extensionBuilder_ = null;
                     this.extension_ = var1.extension_;
                     this.bitField0_ &= -5;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getExtensionFieldBuilder();
                     } else {
                        var2 = null;
                     }

                     this.extensionBuilder_ = var2;
                  } else {
                     this.extensionBuilder_.addAllMessages(var1.extension_);
                  }
               }

               if (this.nestedTypeBuilder_ == null) {
                  if (!var1.nestedType_.isEmpty()) {
                     if (this.nestedType_.isEmpty()) {
                        this.nestedType_ = var1.nestedType_;
                        this.bitField0_ &= -9;
                     } else {
                        this.ensureNestedTypeIsMutable();
                        this.nestedType_.addAll(var1.nestedType_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.nestedType_.isEmpty()) {
                  if (this.nestedTypeBuilder_.isEmpty()) {
                     this.nestedTypeBuilder_.dispose();
                     this.nestedTypeBuilder_ = null;
                     this.nestedType_ = var1.nestedType_;
                     this.bitField0_ &= -9;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getNestedTypeFieldBuilder();
                     } else {
                        var2 = null;
                     }

                     this.nestedTypeBuilder_ = var2;
                  } else {
                     this.nestedTypeBuilder_.addAllMessages(var1.nestedType_);
                  }
               }

               if (this.enumTypeBuilder_ == null) {
                  if (!var1.enumType_.isEmpty()) {
                     if (this.enumType_.isEmpty()) {
                        this.enumType_ = var1.enumType_;
                        this.bitField0_ &= -17;
                     } else {
                        this.ensureEnumTypeIsMutable();
                        this.enumType_.addAll(var1.enumType_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.enumType_.isEmpty()) {
                  if (this.enumTypeBuilder_.isEmpty()) {
                     this.enumTypeBuilder_.dispose();
                     this.enumTypeBuilder_ = null;
                     this.enumType_ = var1.enumType_;
                     this.bitField0_ &= -17;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getEnumTypeFieldBuilder();
                     } else {
                        var2 = null;
                     }

                     this.enumTypeBuilder_ = var2;
                  } else {
                     this.enumTypeBuilder_.addAllMessages(var1.enumType_);
                  }
               }

               if (this.extensionRangeBuilder_ == null) {
                  if (!var1.extensionRange_.isEmpty()) {
                     if (this.extensionRange_.isEmpty()) {
                        this.extensionRange_ = var1.extensionRange_;
                        this.bitField0_ &= -33;
                     } else {
                        this.ensureExtensionRangeIsMutable();
                        this.extensionRange_.addAll(var1.extensionRange_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.extensionRange_.isEmpty()) {
                  if (this.extensionRangeBuilder_.isEmpty()) {
                     this.extensionRangeBuilder_.dispose();
                     this.extensionRangeBuilder_ = null;
                     this.extensionRange_ = var1.extensionRange_;
                     this.bitField0_ &= -33;
                     var2 = (RepeatedFieldBuilder)var3;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getExtensionRangeFieldBuilder();
                     }

                     this.extensionRangeBuilder_ = var2;
                  } else {
                     this.extensionRangeBuilder_.addAllMessages(var1.extensionRange_);
                  }
               }

               if (var1.hasOptions()) {
                  this.mergeOptions(var1.getOptions());
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.DescriptorProto) {
               return this.mergeFrom((DescriptorProtos.DescriptorProto)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder mergeOptions(DescriptorProtos.MessageOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if ((this.bitField0_ & 64) == 64 && this.options_ != DescriptorProtos.MessageOptions.getDefaultInstance()) {
                  this.options_ = DescriptorProtos.MessageOptions.newBuilder(this.options_).mergeFrom(var1).buildPartial();
               } else {
                  this.options_ = var1;
               }

               this.onChanged();
            } else {
               var2.mergeFrom(var1);
            }

            this.bitField0_ |= 64;
            return this;
         }

         public DescriptorProtos.DescriptorProto.Builder removeEnumType(int var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               this.ensureEnumTypeIsMutable();
               this.enumType_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder removeExtension(int var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            if (var2 == null) {
               this.ensureExtensionIsMutable();
               this.extension_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder removeExtensionRange(int var1) {
            RepeatedFieldBuilder var2 = this.extensionRangeBuilder_;
            if (var2 == null) {
               this.ensureExtensionRangeIsMutable();
               this.extensionRange_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder removeField(int var1) {
            RepeatedFieldBuilder var2 = this.fieldBuilder_;
            if (var2 == null) {
               this.ensureFieldIsMutable();
               this.field_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder removeNestedType(int var1) {
            RepeatedFieldBuilder var2 = this.nestedTypeBuilder_;
            if (var2 == null) {
               this.ensureNestedTypeIsMutable();
               this.nestedType_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setEnumType(int var1, DescriptorProtos.EnumDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.enumTypeBuilder_;
            if (var3 == null) {
               this.ensureEnumTypeIsMutable();
               this.enumType_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setEnumType(int var1, DescriptorProtos.EnumDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.enumTypeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureEnumTypeIsMutable();
                  this.enumType_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setExtension(int var1, DescriptorProtos.FieldDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.extensionBuilder_;
            if (var3 == null) {
               this.ensureExtensionIsMutable();
               this.extension_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setExtension(int var1, DescriptorProtos.FieldDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.extensionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureExtensionIsMutable();
                  this.extension_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setExtensionRange(int var1, DescriptorProtos.DescriptorProto.ExtensionRange.Builder var2) {
            RepeatedFieldBuilder var3 = this.extensionRangeBuilder_;
            if (var3 == null) {
               this.ensureExtensionRangeIsMutable();
               this.extensionRange_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setExtensionRange(int var1, DescriptorProtos.DescriptorProto.ExtensionRange var2) {
            RepeatedFieldBuilder var3 = this.extensionRangeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureExtensionRangeIsMutable();
                  this.extensionRange_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setField(int var1, DescriptorProtos.FieldDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.fieldBuilder_;
            if (var3 == null) {
               this.ensureFieldIsMutable();
               this.field_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setField(int var1, DescriptorProtos.FieldDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.fieldBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureFieldIsMutable();
                  this.field_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setName(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setNameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setNestedType(int var1, DescriptorProtos.DescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.nestedTypeBuilder_;
            if (var3 == null) {
               this.ensureNestedTypeIsMutable();
               this.nestedType_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setNestedType(int var1, DescriptorProtos.DescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.nestedTypeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureNestedTypeIsMutable();
                  this.nestedType_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder setOptions(DescriptorProtos.MessageOptions.Builder var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = var1.build();
               this.onChanged();
            } else {
               var2.setMessage(var1.build());
            }

            this.bitField0_ |= 64;
            return this;
         }

         public DescriptorProtos.DescriptorProto.Builder setOptions(DescriptorProtos.MessageOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if (var1 == null) {
                  throw null;
               }

               this.options_ = var1;
               this.onChanged();
            } else {
               var2.setMessage(var1);
            }

            this.bitField0_ |= 64;
            return this;
         }
      }

      public static final class ExtensionRange extends GeneratedMessage implements DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder {
         public static final int END_FIELD_NUMBER = 2;
         public static Parser PARSER = new AbstractParser() {
            public DescriptorProtos.DescriptorProto.ExtensionRange parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
               return new DescriptorProtos.DescriptorProto.ExtensionRange(var1, var2);
            }
         };
         public static final int START_FIELD_NUMBER = 1;
         private static final DescriptorProtos.DescriptorProto.ExtensionRange defaultInstance;
         private static final long serialVersionUID = 0L;
         private int bitField0_;
         private int end_;
         private byte memoizedIsInitialized;
         private int memoizedSerializedSize;
         private int start_;
         private final UnknownFieldSet unknownFields;

         static {
            DescriptorProtos.DescriptorProto.ExtensionRange var0 = new DescriptorProtos.DescriptorProto.ExtensionRange(true);
            defaultInstance = var0;
            var0.initFields();
         }

         private ExtensionRange(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
            // $FF: Couldn't be decompiled
         }

         // $FF: synthetic method
         ExtensionRange(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
            this(var1, var2);
         }

         private ExtensionRange(GeneratedMessage.Builder var1) {
            super(var1);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = var1.getUnknownFields();
         }

         // $FF: synthetic method
         ExtensionRange(GeneratedMessage.Builder var1, Object var2) {
            this(var1);
         }

         private ExtensionRange(boolean var1) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange getDefaultInstance() {
            return defaultInstance;
         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
         }

         private void initFields() {
            this.start_ = 0;
            this.end_ = 0;
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange.Builder newBuilder() {
            return DescriptorProtos.DescriptorProto.ExtensionRange.Builder.create();
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange.Builder newBuilder(DescriptorProtos.DescriptorProto.ExtensionRange var0) {
            return newBuilder().mergeFrom(var0);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseDelimitedFrom(InputStream var0) throws IOException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseDelimitedFrom(var0);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseDelimitedFrom(var0, var1);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(ByteString var0) throws InvalidProtocolBufferException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(CodedInputStream var0) throws IOException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(InputStream var0) throws IOException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(byte[] var0) throws InvalidProtocolBufferException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
            return (DescriptorProtos.DescriptorProto.ExtensionRange)PARSER.parseFrom(var0, var1);
         }

         public DescriptorProtos.DescriptorProto.ExtensionRange getDefaultInstanceForType() {
            return defaultInstance;
         }

         public int getEnd() {
            return this.end_;
         }

         public Parser getParserForType() {
            return PARSER;
         }

         public int getSerializedSize() {
            int var1 = this.memoizedSerializedSize;
            if (var1 != -1) {
               return var1;
            } else {
               var1 = 0;
               if ((this.bitField0_ & 1) == 1) {
                  var1 = 0 + CodedOutputStream.computeInt32Size(1, this.start_);
               }

               int var2 = var1;
               if ((this.bitField0_ & 2) == 2) {
                  var2 = var1 + CodedOutputStream.computeInt32Size(2, this.end_);
               }

               var1 = var2 + this.getUnknownFields().getSerializedSize();
               this.memoizedSerializedSize = var1;
               return var1;
            }
         }

         public int getStart() {
            return this.start_;
         }

         public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
         }

         public boolean hasEnd() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasStart() {
            return (this.bitField0_ & 1) == 1;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.DescriptorProto.ExtensionRange.class, DescriptorProtos.DescriptorProto.ExtensionRange.Builder.class);
         }

         public final boolean isInitialized() {
            byte var1 = this.memoizedIsInitialized;
            if (var1 != -1) {
               return var1 == 1;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }

         public DescriptorProtos.DescriptorProto.ExtensionRange.Builder newBuilderForType() {
            return newBuilder();
         }

         protected DescriptorProtos.DescriptorProto.ExtensionRange.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
            return new DescriptorProtos.DescriptorProto.ExtensionRange.Builder(var1);
         }

         public DescriptorProtos.DescriptorProto.ExtensionRange.Builder toBuilder() {
            return newBuilder(this);
         }

         protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
         }

         public void writeTo(CodedOutputStream var1) throws IOException {
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
               var1.writeInt32(1, this.start_);
            }

            if ((this.bitField0_ & 2) == 2) {
               var1.writeInt32(2, this.end_);
            }

            this.getUnknownFields().writeTo(var1);
         }

         public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder {
            private int bitField0_;
            private int end_;
            private int start_;

            private Builder() {
               this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent var1) {
               super(var1);
               this.maybeForceBuilderInitialization();
            }

            // $FF: synthetic method
            Builder(GeneratedMessage.BuilderParent var1, Object var2) {
               this(var1);
            }

            private static DescriptorProtos.DescriptorProto.ExtensionRange.Builder create() {
               return new DescriptorProtos.DescriptorProto.ExtensionRange.Builder();
            }

            public static final Descriptors.Descriptor getDescriptor() {
               return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
            }

            private void maybeForceBuilderInitialization() {
               boolean var1 = GeneratedMessage.alwaysUseFieldBuilders;
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange build() {
               DescriptorProtos.DescriptorProto.ExtensionRange var1 = this.buildPartial();
               if (var1.isInitialized()) {
                  return var1;
               } else {
                  throw newUninitializedMessageException(var1);
               }
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange buildPartial() {
               DescriptorProtos.DescriptorProto.ExtensionRange var4 = new DescriptorProtos.DescriptorProto.ExtensionRange(this);
               int var3 = this.bitField0_;
               int var1 = 0;
               if ((var3 & 1) == 1) {
                  var1 = 0 | 1;
               }

               var4.start_ = this.start_;
               int var2 = var1;
               if ((var3 & 2) == 2) {
                  var2 = var1 | 2;
               }

               var4.end_ = this.end_;
               var4.bitField0_ = var2;
               this.onBuilt();
               return var4;
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder clear() {
               super.clear();
               this.start_ = 0;
               int var1 = this.bitField0_ & -2;
               this.bitField0_ = var1;
               this.end_ = 0;
               this.bitField0_ = var1 & -3;
               return this;
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder clearEnd() {
               this.bitField0_ &= -3;
               this.end_ = 0;
               this.onChanged();
               return this;
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder clearStart() {
               this.bitField0_ &= -2;
               this.start_ = 0;
               this.onChanged();
               return this;
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder clone() {
               return create().mergeFrom(this.buildPartial());
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange getDefaultInstanceForType() {
               return DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance();
            }

            public Descriptors.Descriptor getDescriptorForType() {
               return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
            }

            public int getEnd() {
               return this.end_;
            }

            public int getStart() {
               return this.start_;
            }

            public boolean hasEnd() {
               return (this.bitField0_ & 2) == 2;
            }

            public boolean hasStart() {
               return (this.bitField0_ & 1) == 1;
            }

            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
               return DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.DescriptorProto.ExtensionRange.class, DescriptorProtos.DescriptorProto.ExtensionRange.Builder.class);
            }

            public final boolean isInitialized() {
               return true;
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
               Object var4 = null;
               DescriptorProtos.DescriptorProto.ExtensionRange var3 = (DescriptorProtos.DescriptorProto.ExtensionRange)var4;

               DescriptorProtos.DescriptorProto.ExtensionRange var21;
               label173: {
                  Throwable var10000;
                  label174: {
                     boolean var10001;
                     InvalidProtocolBufferException var23;
                     try {
                        try {
                           var21 = (DescriptorProtos.DescriptorProto.ExtensionRange)DescriptorProtos.DescriptorProto.ExtensionRange.PARSER.parsePartialFrom(var1, var2);
                           break label173;
                        } catch (InvalidProtocolBufferException var19) {
                           var23 = var19;
                        }
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label174;
                     }

                     var3 = (DescriptorProtos.DescriptorProto.ExtensionRange)var4;

                     try {
                        var21 = (DescriptorProtos.DescriptorProto.ExtensionRange)var23.getUnfinishedMessage();
                     } catch (Throwable var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label174;
                     }

                     var3 = var21;

                     label158:
                     try {
                        throw var23;
                     } catch (Throwable var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label158;
                     }
                  }

                  Throwable var22 = var10000;
                  if (var3 != null) {
                     this.mergeFrom(var3);
                  }

                  throw var22;
               }

               if (var21 != null) {
                  this.mergeFrom(var21);
               }

               return this;
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder mergeFrom(DescriptorProtos.DescriptorProto.ExtensionRange var1) {
               if (var1 == DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance()) {
                  return this;
               } else {
                  if (var1.hasStart()) {
                     this.setStart(var1.getStart());
                  }

                  if (var1.hasEnd()) {
                     this.setEnd(var1.getEnd());
                  }

                  this.mergeUnknownFields(var1.getUnknownFields());
                  return this;
               }
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder mergeFrom(Message var1) {
               if (var1 instanceof DescriptorProtos.DescriptorProto.ExtensionRange) {
                  return this.mergeFrom((DescriptorProtos.DescriptorProto.ExtensionRange)var1);
               } else {
                  super.mergeFrom(var1);
                  return this;
               }
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder setEnd(int var1) {
               this.bitField0_ |= 2;
               this.end_ = var1;
               this.onChanged();
               return this;
            }

            public DescriptorProtos.DescriptorProto.ExtensionRange.Builder setStart(int var1) {
               this.bitField0_ |= 1;
               this.start_ = var1;
               this.onChanged();
               return this;
            }
         }
      }

      public interface ExtensionRangeOrBuilder extends MessageOrBuilder {
         int getEnd();

         int getStart();

         boolean hasEnd();

         boolean hasStart();
      }
   }

   public interface DescriptorProtoOrBuilder extends MessageOrBuilder {
      DescriptorProtos.EnumDescriptorProto getEnumType(int var1);

      int getEnumTypeCount();

      List getEnumTypeList();

      DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int var1);

      List getEnumTypeOrBuilderList();

      DescriptorProtos.FieldDescriptorProto getExtension(int var1);

      int getExtensionCount();

      List getExtensionList();

      DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int var1);

      List getExtensionOrBuilderList();

      DescriptorProtos.DescriptorProto.ExtensionRange getExtensionRange(int var1);

      int getExtensionRangeCount();

      List getExtensionRangeList();

      DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder getExtensionRangeOrBuilder(int var1);

      List getExtensionRangeOrBuilderList();

      DescriptorProtos.FieldDescriptorProto getField(int var1);

      int getFieldCount();

      List getFieldList();

      DescriptorProtos.FieldDescriptorProtoOrBuilder getFieldOrBuilder(int var1);

      List getFieldOrBuilderList();

      String getName();

      ByteString getNameBytes();

      DescriptorProtos.DescriptorProto getNestedType(int var1);

      int getNestedTypeCount();

      List getNestedTypeList();

      DescriptorProtos.DescriptorProtoOrBuilder getNestedTypeOrBuilder(int var1);

      List getNestedTypeOrBuilderList();

      DescriptorProtos.MessageOptions getOptions();

      DescriptorProtos.MessageOptionsOrBuilder getOptionsOrBuilder();

      boolean hasName();

      boolean hasOptions();
   }

   public static final class EnumDescriptorProto extends GeneratedMessage implements DescriptorProtos.EnumDescriptorProtoOrBuilder {
      public static final int NAME_FIELD_NUMBER = 1;
      public static final int OPTIONS_FIELD_NUMBER = 3;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.EnumDescriptorProto parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.EnumDescriptorProto(var1, var2);
         }
      };
      public static final int VALUE_FIELD_NUMBER = 2;
      private static final DescriptorProtos.EnumDescriptorProto defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private Object name_;
      private DescriptorProtos.EnumOptions options_;
      private final UnknownFieldSet unknownFields;
      private List value_;

      static {
         DescriptorProtos.EnumDescriptorProto var0 = new DescriptorProtos.EnumDescriptorProto(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private EnumDescriptorProto(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      EnumDescriptorProto(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private EnumDescriptorProto(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      EnumDescriptorProto(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private EnumDescriptorProto(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.EnumDescriptorProto getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor;
      }

      private void initFields() {
         this.name_ = "";
         this.value_ = Collections.emptyList();
         this.options_ = DescriptorProtos.EnumOptions.getDefaultInstance();
      }

      public static DescriptorProtos.EnumDescriptorProto.Builder newBuilder() {
         return DescriptorProtos.EnumDescriptorProto.Builder.create();
      }

      public static DescriptorProtos.EnumDescriptorProto.Builder newBuilder(DescriptorProtos.EnumDescriptorProto var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.EnumDescriptorProto parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.EnumDescriptorProto parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.EnumDescriptorProto parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumDescriptorProto parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumDescriptorProto parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumDescriptorProto parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumDescriptorProto parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumDescriptorProto parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumDescriptorProto parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumDescriptorProto parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.EnumDescriptorProto getDefaultInstanceForType() {
         return defaultInstance;
      }

      public String getName() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.name_ = var2;
            }

            return var2;
         }
      }

      public ByteString getNameBytes() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.name_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.EnumOptions getOptions() {
         return this.options_;
      }

      public DescriptorProtos.EnumOptionsOrBuilder getOptionsOrBuilder() {
         return this.options_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            var1 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var1 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }

            int var2;
            for(var2 = 0; var2 < this.value_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(2, (MessageLite)this.value_.get(var2));
            }

            var2 = var1;
            if ((this.bitField0_ & 2) == 2) {
               var2 = var1 + CodedOutputStream.computeMessageSize(3, this.options_);
            }

            var1 = var2 + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public DescriptorProtos.EnumValueDescriptorProto getValue(int var1) {
         return (DescriptorProtos.EnumValueDescriptorProto)this.value_.get(var1);
      }

      public int getValueCount() {
         return this.value_.size();
      }

      public List getValueList() {
         return this.value_;
      }

      public DescriptorProtos.EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int var1) {
         return (DescriptorProtos.EnumValueDescriptorProtoOrBuilder)this.value_.get(var1);
      }

      public List getValueOrBuilderList() {
         return this.value_;
      }

      public boolean hasName() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasOptions() {
         return (this.bitField0_ & 2) == 2;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.EnumDescriptorProto.class, DescriptorProtos.EnumDescriptorProto.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getValueCount(); ++var3) {
               if (!this.getValue(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (this.hasOptions() && !this.getOptions().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.EnumDescriptorProto.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.EnumDescriptorProto.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.EnumDescriptorProto.Builder(var1);
      }

      public DescriptorProtos.EnumDescriptorProto.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(1, this.getNameBytes());
         }

         for(int var2 = 0; var2 < this.value_.size(); ++var2) {
            var1.writeMessage(2, (MessageLite)this.value_.get(var2));
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeMessage(3, this.options_);
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.EnumDescriptorProtoOrBuilder {
         private int bitField0_;
         private Object name_;
         private SingleFieldBuilder optionsBuilder_;
         private DescriptorProtos.EnumOptions options_;
         private RepeatedFieldBuilder valueBuilder_;
         private List value_;

         private Builder() {
            this.name_ = "";
            this.value_ = Collections.emptyList();
            this.options_ = DescriptorProtos.EnumOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.name_ = "";
            this.value_ = Collections.emptyList();
            this.options_ = DescriptorProtos.EnumOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.EnumDescriptorProto.Builder create() {
            return new DescriptorProtos.EnumDescriptorProto.Builder();
         }

         private void ensureValueIsMutable() {
            if ((this.bitField0_ & 2) != 2) {
               this.value_ = new ArrayList(this.value_);
               this.bitField0_ |= 2;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor;
         }

         private SingleFieldBuilder getOptionsFieldBuilder() {
            if (this.optionsBuilder_ == null) {
               this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
               this.options_ = null;
            }

            return this.optionsBuilder_;
         }

         private RepeatedFieldBuilder getValueFieldBuilder() {
            if (this.valueBuilder_ == null) {
               List var2 = this.value_;
               boolean var1;
               if ((this.bitField0_ & 2) == 2) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.valueBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
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

         public DescriptorProtos.EnumDescriptorProto.Builder addAllValue(Iterable var1) {
            RepeatedFieldBuilder var2 = this.valueBuilder_;
            if (var2 == null) {
               this.ensureValueIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.value_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder addValue(int var1, DescriptorProtos.EnumValueDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.valueBuilder_;
            if (var3 == null) {
               this.ensureValueIsMutable();
               this.value_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder addValue(int var1, DescriptorProtos.EnumValueDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.valueBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureValueIsMutable();
                  this.value_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder addValue(DescriptorProtos.EnumValueDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.valueBuilder_;
            if (var2 == null) {
               this.ensureValueIsMutable();
               this.value_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder addValue(DescriptorProtos.EnumValueDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.valueBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureValueIsMutable();
                  this.value_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder addValueBuilder() {
            return (DescriptorProtos.EnumValueDescriptorProto.Builder)this.getValueFieldBuilder().addBuilder(DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder addValueBuilder(int var1) {
            return (DescriptorProtos.EnumValueDescriptorProto.Builder)this.getValueFieldBuilder().addBuilder(var1, DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.EnumDescriptorProto build() {
            DescriptorProtos.EnumDescriptorProto var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.EnumDescriptorProto buildPartial() {
            DescriptorProtos.EnumDescriptorProto var4 = new DescriptorProtos.EnumDescriptorProto(this);
            int var3 = this.bitField0_;
            int var1 = 0;
            if ((var3 & 1) == 1) {
               var1 = 0 | 1;
            }

            var4.name_ = this.name_;
            RepeatedFieldBuilder var5 = this.valueBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 2) == 2) {
                  this.value_ = Collections.unmodifiableList(this.value_);
                  this.bitField0_ &= -3;
               }

               var4.value_ = this.value_;
            } else {
               var4.value_ = var5.build();
            }

            int var2 = var1;
            if ((var3 & 4) == 4) {
               var2 = var1 | 2;
            }

            SingleFieldBuilder var6 = this.optionsBuilder_;
            if (var6 == null) {
               var4.options_ = this.options_;
            } else {
               var4.options_ = (DescriptorProtos.EnumOptions)var6.build();
            }

            var4.bitField0_ = var2;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.EnumDescriptorProto.Builder clear() {
            super.clear();
            this.name_ = "";
            this.bitField0_ &= -2;
            RepeatedFieldBuilder var1 = this.valueBuilder_;
            if (var1 == null) {
               this.value_ = Collections.emptyList();
               this.bitField0_ &= -3;
            } else {
               var1.clear();
            }

            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = DescriptorProtos.EnumOptions.getDefaultInstance();
            } else {
               var2.clear();
            }

            this.bitField0_ &= -5;
            return this;
         }

         public DescriptorProtos.EnumDescriptorProto.Builder clearName() {
            this.bitField0_ &= -2;
            this.name_ = DescriptorProtos.EnumDescriptorProto.getDefaultInstance().getName();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.EnumDescriptorProto.Builder clearOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            if (var1 == null) {
               this.options_ = DescriptorProtos.EnumOptions.getDefaultInstance();
               this.onChanged();
            } else {
               var1.clear();
            }

            this.bitField0_ &= -5;
            return this;
         }

         public DescriptorProtos.EnumDescriptorProto.Builder clearValue() {
            RepeatedFieldBuilder var1 = this.valueBuilder_;
            if (var1 == null) {
               this.value_ = Collections.emptyList();
               this.bitField0_ &= -3;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.EnumDescriptorProto getDefaultInstanceForType() {
            return DescriptorProtos.EnumDescriptorProto.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor;
         }

         public String getName() {
            Object var1 = this.name_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.name_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getNameBytes() {
            Object var1 = this.name_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.name_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.EnumOptions getOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return var1 == null ? this.options_ : (DescriptorProtos.EnumOptions)var1.getMessage();
         }

         public DescriptorProtos.EnumOptions.Builder getOptionsBuilder() {
            this.bitField0_ |= 4;
            this.onChanged();
            return (DescriptorProtos.EnumOptions.Builder)this.getOptionsFieldBuilder().getBuilder();
         }

         public DescriptorProtos.EnumOptionsOrBuilder getOptionsOrBuilder() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return (DescriptorProtos.EnumOptionsOrBuilder)(var1 != null ? (DescriptorProtos.EnumOptionsOrBuilder)var1.getMessageOrBuilder() : this.options_);
         }

         public DescriptorProtos.EnumValueDescriptorProto getValue(int var1) {
            RepeatedFieldBuilder var2 = this.valueBuilder_;
            return var2 == null ? (DescriptorProtos.EnumValueDescriptorProto)this.value_.get(var1) : (DescriptorProtos.EnumValueDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder getValueBuilder(int var1) {
            return (DescriptorProtos.EnumValueDescriptorProto.Builder)this.getValueFieldBuilder().getBuilder(var1);
         }

         public List getValueBuilderList() {
            return this.getValueFieldBuilder().getBuilderList();
         }

         public int getValueCount() {
            RepeatedFieldBuilder var1 = this.valueBuilder_;
            return var1 == null ? this.value_.size() : var1.getCount();
         }

         public List getValueList() {
            RepeatedFieldBuilder var1 = this.valueBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.value_) : var1.getMessageList();
         }

         public DescriptorProtos.EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.valueBuilder_;
            return var2 == null ? (DescriptorProtos.EnumValueDescriptorProtoOrBuilder)this.value_.get(var1) : (DescriptorProtos.EnumValueDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getValueOrBuilderList() {
            RepeatedFieldBuilder var1 = this.valueBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.value_);
         }

         public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasOptions() {
            return (this.bitField0_ & 4) == 4;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.EnumDescriptorProto.class, DescriptorProtos.EnumDescriptorProto.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getValueCount(); ++var1) {
               if (!this.getValue(var1).isInitialized()) {
                  return false;
               }
            }

            if (this.hasOptions() && !this.getOptions().isInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.EnumDescriptorProto var3 = (DescriptorProtos.EnumDescriptorProto)var4;

            DescriptorProtos.EnumDescriptorProto var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.EnumDescriptorProto)DescriptorProtos.EnumDescriptorProto.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.EnumDescriptorProto)var4;

                  try {
                     var21 = (DescriptorProtos.EnumDescriptorProto)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.EnumDescriptorProto.Builder mergeFrom(DescriptorProtos.EnumDescriptorProto var1) {
            if (var1 == DescriptorProtos.EnumDescriptorProto.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasName()) {
                  this.bitField0_ |= 1;
                  this.name_ = var1.name_;
                  this.onChanged();
               }

               if (this.valueBuilder_ == null) {
                  if (!var1.value_.isEmpty()) {
                     if (this.value_.isEmpty()) {
                        this.value_ = var1.value_;
                        this.bitField0_ &= -3;
                     } else {
                        this.ensureValueIsMutable();
                        this.value_.addAll(var1.value_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.value_.isEmpty()) {
                  if (this.valueBuilder_.isEmpty()) {
                     this.valueBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.valueBuilder_ = null;
                     this.value_ = var1.value_;
                     this.bitField0_ &= -3;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getValueFieldBuilder();
                     }

                     this.valueBuilder_ = var2;
                  } else {
                     this.valueBuilder_.addAllMessages(var1.value_);
                  }
               }

               if (var1.hasOptions()) {
                  this.mergeOptions(var1.getOptions());
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.EnumDescriptorProto) {
               return this.mergeFrom((DescriptorProtos.EnumDescriptorProto)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder mergeOptions(DescriptorProtos.EnumOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if ((this.bitField0_ & 4) == 4 && this.options_ != DescriptorProtos.EnumOptions.getDefaultInstance()) {
                  this.options_ = DescriptorProtos.EnumOptions.newBuilder(this.options_).mergeFrom(var1).buildPartial();
               } else {
                  this.options_ = var1;
               }

               this.onChanged();
            } else {
               var2.mergeFrom(var1);
            }

            this.bitField0_ |= 4;
            return this;
         }

         public DescriptorProtos.EnumDescriptorProto.Builder removeValue(int var1) {
            RepeatedFieldBuilder var2 = this.valueBuilder_;
            if (var2 == null) {
               this.ensureValueIsMutable();
               this.value_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder setName(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder setNameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder setOptions(DescriptorProtos.EnumOptions.Builder var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = var1.build();
               this.onChanged();
            } else {
               var2.setMessage(var1.build());
            }

            this.bitField0_ |= 4;
            return this;
         }

         public DescriptorProtos.EnumDescriptorProto.Builder setOptions(DescriptorProtos.EnumOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if (var1 == null) {
                  throw null;
               }

               this.options_ = var1;
               this.onChanged();
            } else {
               var2.setMessage(var1);
            }

            this.bitField0_ |= 4;
            return this;
         }

         public DescriptorProtos.EnumDescriptorProto.Builder setValue(int var1, DescriptorProtos.EnumValueDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.valueBuilder_;
            if (var3 == null) {
               this.ensureValueIsMutable();
               this.value_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder setValue(int var1, DescriptorProtos.EnumValueDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.valueBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureValueIsMutable();
                  this.value_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }
   }

   public interface EnumDescriptorProtoOrBuilder extends MessageOrBuilder {
      String getName();

      ByteString getNameBytes();

      DescriptorProtos.EnumOptions getOptions();

      DescriptorProtos.EnumOptionsOrBuilder getOptionsOrBuilder();

      DescriptorProtos.EnumValueDescriptorProto getValue(int var1);

      int getValueCount();

      List getValueList();

      DescriptorProtos.EnumValueDescriptorProtoOrBuilder getValueOrBuilder(int var1);

      List getValueOrBuilderList();

      boolean hasName();

      boolean hasOptions();
   }

   public static final class EnumOptions extends GeneratedMessage.ExtendableMessage implements DescriptorProtos.EnumOptionsOrBuilder {
      public static final int ALLOW_ALIAS_FIELD_NUMBER = 2;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.EnumOptions parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.EnumOptions(var1, var2);
         }
      };
      public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
      private static final DescriptorProtos.EnumOptions defaultInstance;
      private static final long serialVersionUID = 0L;
      private boolean allowAlias_;
      private int bitField0_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private List uninterpretedOption_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.EnumOptions var0 = new DescriptorProtos.EnumOptions(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private EnumOptions(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      EnumOptions(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private EnumOptions(GeneratedMessage.ExtendableBuilder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      EnumOptions(GeneratedMessage.ExtendableBuilder var1, Object var2) {
         this(var1);
      }

      private EnumOptions(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.EnumOptions getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor;
      }

      private void initFields() {
         this.allowAlias_ = true;
         this.uninterpretedOption_ = Collections.emptyList();
      }

      public static DescriptorProtos.EnumOptions.Builder newBuilder() {
         return DescriptorProtos.EnumOptions.Builder.create();
      }

      public static DescriptorProtos.EnumOptions.Builder newBuilder(DescriptorProtos.EnumOptions var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.EnumOptions parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.EnumOptions)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.EnumOptions parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumOptions)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.EnumOptions parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumOptions parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumOptions parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.EnumOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumOptions parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumOptions parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.EnumOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumOptions parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumOptions parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumOptions parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumOptions)PARSER.parseFrom(var0, var1);
      }

      public boolean getAllowAlias() {
         return this.allowAlias_;
      }

      public DescriptorProtos.EnumOptions getDefaultInstanceForType() {
         return defaultInstance;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            var1 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var1 = 0 + CodedOutputStream.computeBoolSize(2, this.allowAlias_);
            }

            for(int var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(999, (MessageLite)this.uninterpretedOption_.get(var2));
            }

            var1 = var1 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
         return (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1);
      }

      public int getUninterpretedOptionCount() {
         return this.uninterpretedOption_.size();
      }

      public List getUninterpretedOptionList() {
         return this.uninterpretedOption_;
      }

      public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
         return (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1);
      }

      public List getUninterpretedOptionOrBuilderList() {
         return this.uninterpretedOption_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasAllowAlias() {
         return (this.bitField0_ & 1) == 1;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_EnumOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.EnumOptions.class, DescriptorProtos.EnumOptions.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getUninterpretedOptionCount(); ++var3) {
               if (!this.getUninterpretedOption(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.EnumOptions.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.EnumOptions.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.EnumOptions.Builder(var1);
      }

      public DescriptorProtos.EnumOptions.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         GeneratedMessage.ExtendableMessage.ExtensionWriter var3 = this.newExtensionWriter();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBool(2, this.allowAlias_);
         }

         for(int var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
            var1.writeMessage(999, (MessageLite)this.uninterpretedOption_.get(var2));
         }

         var3.writeUntil(536870912, var1);
         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.ExtendableBuilder implements DescriptorProtos.EnumOptionsOrBuilder {
         private boolean allowAlias_;
         private int bitField0_;
         private RepeatedFieldBuilder uninterpretedOptionBuilder_;
         private List uninterpretedOption_;

         private Builder() {
            this.allowAlias_ = true;
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.allowAlias_ = true;
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.EnumOptions.Builder create() {
            return new DescriptorProtos.EnumOptions.Builder();
         }

         private void ensureUninterpretedOptionIsMutable() {
            if ((this.bitField0_ & 2) != 2) {
               this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
               this.bitField0_ |= 2;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor;
         }

         private RepeatedFieldBuilder getUninterpretedOptionFieldBuilder() {
            if (this.uninterpretedOptionBuilder_ == null) {
               List var2 = this.uninterpretedOption_;
               boolean var1;
               if ((this.bitField0_ & 2) == 2) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.uninterpretedOption_ = null;
            }

            return this.uninterpretedOptionBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getUninterpretedOptionFieldBuilder();
            }

         }

         public DescriptorProtos.EnumOptions.Builder addAllUninterpretedOption(Iterable var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               GeneratedMessage.ExtendableBuilder.addAll(var1, this.uninterpretedOption_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption.Builder var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(var1, DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.EnumOptions build() {
            DescriptorProtos.EnumOptions var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.EnumOptions buildPartial() {
            DescriptorProtos.EnumOptions var3 = new DescriptorProtos.EnumOptions(this);
            int var2 = this.bitField0_;
            int var1 = 0;
            if ((var2 & 1) == 1) {
               var1 = 0 | 1;
            }

            var3.allowAlias_ = this.allowAlias_;
            RepeatedFieldBuilder var4 = this.uninterpretedOptionBuilder_;
            if (var4 == null) {
               if ((this.bitField0_ & 2) == 2) {
                  this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                  this.bitField0_ &= -3;
               }

               var3.uninterpretedOption_ = this.uninterpretedOption_;
            } else {
               var3.uninterpretedOption_ = var4.build();
            }

            var3.bitField0_ = var1;
            this.onBuilt();
            return var3;
         }

         public DescriptorProtos.EnumOptions.Builder clear() {
            super.clear();
            this.allowAlias_ = true;
            this.bitField0_ &= -2;
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -3;
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder clearAllowAlias() {
            this.bitField0_ &= -2;
            this.allowAlias_ = true;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.EnumOptions.Builder clearUninterpretedOption() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -3;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public boolean getAllowAlias() {
            return this.allowAlias_;
         }

         public DescriptorProtos.EnumOptions getDefaultInstanceForType() {
            return DescriptorProtos.EnumOptions.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor;
         }

         public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOption)var2.getMessage(var1);
         }

         public DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().getBuilder(var1);
         }

         public List getUninterpretedOptionBuilderList() {
            return this.getUninterpretedOptionFieldBuilder().getBuilderList();
         }

         public int getUninterpretedOptionCount() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? this.uninterpretedOption_.size() : var1.getCount();
         }

         public List getUninterpretedOptionList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.uninterpretedOption_) : var1.getMessageList();
         }

         public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOptionOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getUninterpretedOptionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
         }

         public boolean hasAllowAlias() {
            return (this.bitField0_ & 1) == 1;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_EnumOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.EnumOptions.class, DescriptorProtos.EnumOptions.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getUninterpretedOptionCount(); ++var1) {
               if (!this.getUninterpretedOption(var1).isInitialized()) {
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.EnumOptions.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.EnumOptions var3 = (DescriptorProtos.EnumOptions)var4;

            DescriptorProtos.EnumOptions var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.EnumOptions)DescriptorProtos.EnumOptions.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.EnumOptions)var4;

                  try {
                     var21 = (DescriptorProtos.EnumOptions)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.EnumOptions.Builder mergeFrom(DescriptorProtos.EnumOptions var1) {
            if (var1 == DescriptorProtos.EnumOptions.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasAllowAlias()) {
                  this.setAllowAlias(var1.getAllowAlias());
               }

               if (this.uninterpretedOptionBuilder_ == null) {
                  if (!var1.uninterpretedOption_.isEmpty()) {
                     if (this.uninterpretedOption_.isEmpty()) {
                        this.uninterpretedOption_ = var1.uninterpretedOption_;
                        this.bitField0_ &= -3;
                     } else {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.addAll(var1.uninterpretedOption_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.uninterpretedOption_.isEmpty()) {
                  if (this.uninterpretedOptionBuilder_.isEmpty()) {
                     this.uninterpretedOptionBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.uninterpretedOptionBuilder_ = null;
                     this.uninterpretedOption_ = var1.uninterpretedOption_;
                     this.bitField0_ &= -3;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getUninterpretedOptionFieldBuilder();
                     }

                     this.uninterpretedOptionBuilder_ = var2;
                  } else {
                     this.uninterpretedOptionBuilder_.addAllMessages(var1.uninterpretedOption_);
                  }
               }

               this.mergeExtensionFields(var1);
               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.EnumOptions) {
               return this.mergeFrom((DescriptorProtos.EnumOptions)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder removeUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder setAllowAlias(boolean var1) {
            this.bitField0_ |= 1;
            this.allowAlias_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.EnumOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.EnumOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }
   }

   public interface EnumOptionsOrBuilder extends GeneratedMessage.ExtendableMessageOrBuilder {
      boolean getAllowAlias();

      DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1);

      int getUninterpretedOptionCount();

      List getUninterpretedOptionList();

      DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

      List getUninterpretedOptionOrBuilderList();

      boolean hasAllowAlias();
   }

   public static final class EnumValueDescriptorProto extends GeneratedMessage implements DescriptorProtos.EnumValueDescriptorProtoOrBuilder {
      public static final int NAME_FIELD_NUMBER = 1;
      public static final int NUMBER_FIELD_NUMBER = 2;
      public static final int OPTIONS_FIELD_NUMBER = 3;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.EnumValueDescriptorProto parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.EnumValueDescriptorProto(var1, var2);
         }
      };
      private static final DescriptorProtos.EnumValueDescriptorProto defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private Object name_;
      private int number_;
      private DescriptorProtos.EnumValueOptions options_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.EnumValueDescriptorProto var0 = new DescriptorProtos.EnumValueDescriptorProto(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private EnumValueDescriptorProto(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      EnumValueDescriptorProto(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private EnumValueDescriptorProto(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      EnumValueDescriptorProto(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private EnumValueDescriptorProto(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.EnumValueDescriptorProto getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
      }

      private void initFields() {
         this.name_ = "";
         this.number_ = 0;
         this.options_ = DescriptorProtos.EnumValueOptions.getDefaultInstance();
      }

      public static DescriptorProtos.EnumValueDescriptorProto.Builder newBuilder() {
         return DescriptorProtos.EnumValueDescriptorProto.Builder.create();
      }

      public static DescriptorProtos.EnumValueDescriptorProto.Builder newBuilder(DescriptorProtos.EnumValueDescriptorProto var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumValueDescriptorProto parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumValueDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.EnumValueDescriptorProto getDefaultInstanceForType() {
         return defaultInstance;
      }

      public String getName() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.name_ = var2;
            }

            return var2;
         }
      }

      public ByteString getNameBytes() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.name_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public int getNumber() {
         return this.number_;
      }

      public DescriptorProtos.EnumValueOptions getOptions() {
         return this.options_;
      }

      public DescriptorProtos.EnumValueOptionsOrBuilder getOptionsOrBuilder() {
         return this.options_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 2) == 2) {
               var1 = var2 + CodedOutputStream.computeInt32Size(2, this.number_);
            }

            var2 = var1;
            if ((this.bitField0_ & 4) == 4) {
               var2 = var1 + CodedOutputStream.computeMessageSize(3, this.options_);
            }

            var1 = var2 + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasName() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasNumber() {
         return (this.bitField0_ & 2) == 2;
      }

      public boolean hasOptions() {
         return (this.bitField0_ & 4) == 4;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.EnumValueDescriptorProto.class, DescriptorProtos.EnumValueDescriptorProto.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else if (this.hasOptions() && !this.getOptions().isInitialized()) {
            this.memoizedIsInitialized = 0;
            return false;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public DescriptorProtos.EnumValueDescriptorProto.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.EnumValueDescriptorProto.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.EnumValueDescriptorProto.Builder(var1);
      }

      public DescriptorProtos.EnumValueDescriptorProto.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(1, this.getNameBytes());
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeInt32(2, this.number_);
         }

         if ((this.bitField0_ & 4) == 4) {
            var1.writeMessage(3, this.options_);
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.EnumValueDescriptorProtoOrBuilder {
         private int bitField0_;
         private Object name_;
         private int number_;
         private SingleFieldBuilder optionsBuilder_;
         private DescriptorProtos.EnumValueOptions options_;

         private Builder() {
            this.name_ = "";
            this.options_ = DescriptorProtos.EnumValueOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.name_ = "";
            this.options_ = DescriptorProtos.EnumValueOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.EnumValueDescriptorProto.Builder create() {
            return new DescriptorProtos.EnumValueDescriptorProto.Builder();
         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
         }

         private SingleFieldBuilder getOptionsFieldBuilder() {
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

         public DescriptorProtos.EnumValueDescriptorProto build() {
            DescriptorProtos.EnumValueDescriptorProto var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.EnumValueDescriptorProto buildPartial() {
            DescriptorProtos.EnumValueDescriptorProto var4 = new DescriptorProtos.EnumValueDescriptorProto(this);
            int var3 = this.bitField0_;
            int var2 = 0;
            if ((var3 & 1) == 1) {
               var2 = 0 | 1;
            }

            var4.name_ = this.name_;
            int var1 = var2;
            if ((var3 & 2) == 2) {
               var1 = var2 | 2;
            }

            var4.number_ = this.number_;
            var2 = var1;
            if ((var3 & 4) == 4) {
               var2 = var1 | 4;
            }

            SingleFieldBuilder var5 = this.optionsBuilder_;
            if (var5 == null) {
               var4.options_ = this.options_;
            } else {
               var4.options_ = (DescriptorProtos.EnumValueOptions)var5.build();
            }

            var4.bitField0_ = var2;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder clear() {
            super.clear();
            this.name_ = "";
            int var1 = this.bitField0_ & -2;
            this.bitField0_ = var1;
            this.number_ = 0;
            this.bitField0_ = var1 & -3;
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = DescriptorProtos.EnumValueOptions.getDefaultInstance();
            } else {
               var2.clear();
            }

            this.bitField0_ &= -5;
            return this;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder clearName() {
            this.bitField0_ &= -2;
            this.name_ = DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance().getName();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder clearNumber() {
            this.bitField0_ &= -3;
            this.number_ = 0;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder clearOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            if (var1 == null) {
               this.options_ = DescriptorProtos.EnumValueOptions.getDefaultInstance();
               this.onChanged();
            } else {
               var1.clear();
            }

            this.bitField0_ &= -5;
            return this;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.EnumValueDescriptorProto getDefaultInstanceForType() {
            return DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
         }

         public String getName() {
            Object var1 = this.name_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.name_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getNameBytes() {
            Object var1 = this.name_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.name_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public int getNumber() {
            return this.number_;
         }

         public DescriptorProtos.EnumValueOptions getOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return var1 == null ? this.options_ : (DescriptorProtos.EnumValueOptions)var1.getMessage();
         }

         public DescriptorProtos.EnumValueOptions.Builder getOptionsBuilder() {
            this.bitField0_ |= 4;
            this.onChanged();
            return (DescriptorProtos.EnumValueOptions.Builder)this.getOptionsFieldBuilder().getBuilder();
         }

         public DescriptorProtos.EnumValueOptionsOrBuilder getOptionsOrBuilder() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return (DescriptorProtos.EnumValueOptionsOrBuilder)(var1 != null ? (DescriptorProtos.EnumValueOptionsOrBuilder)var1.getMessageOrBuilder() : this.options_);
         }

         public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasNumber() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasOptions() {
            return (this.bitField0_ & 4) == 4;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.EnumValueDescriptorProto.class, DescriptorProtos.EnumValueDescriptorProto.Builder.class);
         }

         public final boolean isInitialized() {
            return !this.hasOptions() || this.getOptions().isInitialized();
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.EnumValueDescriptorProto var3 = (DescriptorProtos.EnumValueDescriptorProto)var4;

            DescriptorProtos.EnumValueDescriptorProto var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.EnumValueDescriptorProto)DescriptorProtos.EnumValueDescriptorProto.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.EnumValueDescriptorProto)var4;

                  try {
                     var21 = (DescriptorProtos.EnumValueDescriptorProto)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder mergeFrom(DescriptorProtos.EnumValueDescriptorProto var1) {
            if (var1 == DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasName()) {
                  this.bitField0_ |= 1;
                  this.name_ = var1.name_;
                  this.onChanged();
               }

               if (var1.hasNumber()) {
                  this.setNumber(var1.getNumber());
               }

               if (var1.hasOptions()) {
                  this.mergeOptions(var1.getOptions());
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.EnumValueDescriptorProto) {
               return this.mergeFrom((DescriptorProtos.EnumValueDescriptorProto)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder mergeOptions(DescriptorProtos.EnumValueOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if ((this.bitField0_ & 4) == 4 && this.options_ != DescriptorProtos.EnumValueOptions.getDefaultInstance()) {
                  this.options_ = DescriptorProtos.EnumValueOptions.newBuilder(this.options_).mergeFrom(var1).buildPartial();
               } else {
                  this.options_ = var1;
               }

               this.onChanged();
            } else {
               var2.mergeFrom(var1);
            }

            this.bitField0_ |= 4;
            return this;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder setName(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder setNameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder setNumber(int var1) {
            this.bitField0_ |= 2;
            this.number_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder setOptions(DescriptorProtos.EnumValueOptions.Builder var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = var1.build();
               this.onChanged();
            } else {
               var2.setMessage(var1.build());
            }

            this.bitField0_ |= 4;
            return this;
         }

         public DescriptorProtos.EnumValueDescriptorProto.Builder setOptions(DescriptorProtos.EnumValueOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if (var1 == null) {
                  throw null;
               }

               this.options_ = var1;
               this.onChanged();
            } else {
               var2.setMessage(var1);
            }

            this.bitField0_ |= 4;
            return this;
         }
      }
   }

   public interface EnumValueDescriptorProtoOrBuilder extends MessageOrBuilder {
      String getName();

      ByteString getNameBytes();

      int getNumber();

      DescriptorProtos.EnumValueOptions getOptions();

      DescriptorProtos.EnumValueOptionsOrBuilder getOptionsOrBuilder();

      boolean hasName();

      boolean hasNumber();

      boolean hasOptions();
   }

   public static final class EnumValueOptions extends GeneratedMessage.ExtendableMessage implements DescriptorProtos.EnumValueOptionsOrBuilder {
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.EnumValueOptions parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.EnumValueOptions(var1, var2);
         }
      };
      public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
      private static final DescriptorProtos.EnumValueOptions defaultInstance;
      private static final long serialVersionUID = 0L;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private List uninterpretedOption_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.EnumValueOptions var0 = new DescriptorProtos.EnumValueOptions(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private EnumValueOptions(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      EnumValueOptions(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private EnumValueOptions(GeneratedMessage.ExtendableBuilder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      EnumValueOptions(GeneratedMessage.ExtendableBuilder var1, Object var2) {
         this(var1);
      }

      private EnumValueOptions(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.EnumValueOptions getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor;
      }

      private void initFields() {
         this.uninterpretedOption_ = Collections.emptyList();
      }

      public static DescriptorProtos.EnumValueOptions.Builder newBuilder() {
         return DescriptorProtos.EnumValueOptions.Builder.create();
      }

      public static DescriptorProtos.EnumValueOptions.Builder newBuilder(DescriptorProtos.EnumValueOptions var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.EnumValueOptions parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.EnumValueOptions parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.EnumValueOptions parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumValueOptions parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumValueOptions parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumValueOptions parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumValueOptions parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumValueOptions parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.EnumValueOptions parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.EnumValueOptions parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.EnumValueOptions)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.EnumValueOptions getDefaultInstanceForType() {
         return defaultInstance;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;

            for(var1 = 0; var1 < this.uninterpretedOption_.size(); ++var1) {
               var2 += CodedOutputStream.computeMessageSize(999, (MessageLite)this.uninterpretedOption_.get(var1));
            }

            var1 = var2 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
         return (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1);
      }

      public int getUninterpretedOptionCount() {
         return this.uninterpretedOption_.size();
      }

      public List getUninterpretedOptionList() {
         return this.uninterpretedOption_;
      }

      public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
         return (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1);
      }

      public List getUninterpretedOptionOrBuilderList() {
         return this.uninterpretedOption_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.EnumValueOptions.class, DescriptorProtos.EnumValueOptions.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getUninterpretedOptionCount(); ++var3) {
               if (!this.getUninterpretedOption(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.EnumValueOptions.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.EnumValueOptions.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.EnumValueOptions.Builder(var1);
      }

      public DescriptorProtos.EnumValueOptions.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         GeneratedMessage.ExtendableMessage.ExtensionWriter var3 = this.newExtensionWriter();

         for(int var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
            var1.writeMessage(999, (MessageLite)this.uninterpretedOption_.get(var2));
         }

         var3.writeUntil(536870912, var1);
         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.ExtendableBuilder implements DescriptorProtos.EnumValueOptionsOrBuilder {
         private int bitField0_;
         private RepeatedFieldBuilder uninterpretedOptionBuilder_;
         private List uninterpretedOption_;

         private Builder() {
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.EnumValueOptions.Builder create() {
            return new DescriptorProtos.EnumValueOptions.Builder();
         }

         private void ensureUninterpretedOptionIsMutable() {
            if ((this.bitField0_ & 1) != 1) {
               this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
               this.bitField0_ |= 1;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor;
         }

         private RepeatedFieldBuilder getUninterpretedOptionFieldBuilder() {
            if (this.uninterpretedOptionBuilder_ == null) {
               List var3 = this.uninterpretedOption_;
               int var1 = this.bitField0_;
               boolean var2 = true;
               if ((var1 & 1) != 1) {
                  var2 = false;
               }

               this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(var3, var2, this.getParentForChildren(), this.isClean());
               this.uninterpretedOption_ = null;
            }

            return this.uninterpretedOptionBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getUninterpretedOptionFieldBuilder();
            }

         }

         public DescriptorProtos.EnumValueOptions.Builder addAllUninterpretedOption(Iterable var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               GeneratedMessage.ExtendableBuilder.addAll(var1, this.uninterpretedOption_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption.Builder var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(var1, DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.EnumValueOptions build() {
            DescriptorProtos.EnumValueOptions var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.EnumValueOptions buildPartial() {
            DescriptorProtos.EnumValueOptions var2 = new DescriptorProtos.EnumValueOptions(this);
            int var1 = this.bitField0_;
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if ((this.bitField0_ & 1) == 1) {
                  this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                  this.bitField0_ &= -2;
               }

               var2.uninterpretedOption_ = this.uninterpretedOption_;
            } else {
               var2.uninterpretedOption_ = var3.build();
            }

            this.onBuilt();
            return var2;
         }

         public DescriptorProtos.EnumValueOptions.Builder clear() {
            super.clear();
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -2;
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder clearUninterpretedOption() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -2;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.EnumValueOptions getDefaultInstanceForType() {
            return DescriptorProtos.EnumValueOptions.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor;
         }

         public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOption)var2.getMessage(var1);
         }

         public DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().getBuilder(var1);
         }

         public List getUninterpretedOptionBuilderList() {
            return this.getUninterpretedOptionFieldBuilder().getBuilderList();
         }

         public int getUninterpretedOptionCount() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? this.uninterpretedOption_.size() : var1.getCount();
         }

         public List getUninterpretedOptionList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.uninterpretedOption_) : var1.getMessageList();
         }

         public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOptionOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getUninterpretedOptionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.EnumValueOptions.class, DescriptorProtos.EnumValueOptions.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getUninterpretedOptionCount(); ++var1) {
               if (!this.getUninterpretedOption(var1).isInitialized()) {
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.EnumValueOptions var3 = (DescriptorProtos.EnumValueOptions)var4;

            DescriptorProtos.EnumValueOptions var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.EnumValueOptions)DescriptorProtos.EnumValueOptions.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.EnumValueOptions)var4;

                  try {
                     var21 = (DescriptorProtos.EnumValueOptions)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.EnumValueOptions.Builder mergeFrom(DescriptorProtos.EnumValueOptions var1) {
            if (var1 == DescriptorProtos.EnumValueOptions.getDefaultInstance()) {
               return this;
            } else {
               if (this.uninterpretedOptionBuilder_ == null) {
                  if (!var1.uninterpretedOption_.isEmpty()) {
                     if (this.uninterpretedOption_.isEmpty()) {
                        this.uninterpretedOption_ = var1.uninterpretedOption_;
                        this.bitField0_ &= -2;
                     } else {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.addAll(var1.uninterpretedOption_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.uninterpretedOption_.isEmpty()) {
                  if (this.uninterpretedOptionBuilder_.isEmpty()) {
                     this.uninterpretedOptionBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.uninterpretedOptionBuilder_ = null;
                     this.uninterpretedOption_ = var1.uninterpretedOption_;
                     this.bitField0_ &= -2;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getUninterpretedOptionFieldBuilder();
                     }

                     this.uninterpretedOptionBuilder_ = var2;
                  } else {
                     this.uninterpretedOptionBuilder_.addAllMessages(var1.uninterpretedOption_);
                  }
               }

               this.mergeExtensionFields(var1);
               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.EnumValueOptions) {
               return this.mergeFrom((DescriptorProtos.EnumValueOptions)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder removeUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.EnumValueOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }
   }

   public interface EnumValueOptionsOrBuilder extends GeneratedMessage.ExtendableMessageOrBuilder {
      DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1);

      int getUninterpretedOptionCount();

      List getUninterpretedOptionList();

      DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

      List getUninterpretedOptionOrBuilderList();
   }

   public static final class FieldDescriptorProto extends GeneratedMessage implements DescriptorProtos.FieldDescriptorProtoOrBuilder {
      public static final int DEFAULT_VALUE_FIELD_NUMBER = 7;
      public static final int EXTENDEE_FIELD_NUMBER = 2;
      public static final int LABEL_FIELD_NUMBER = 4;
      public static final int NAME_FIELD_NUMBER = 1;
      public static final int NUMBER_FIELD_NUMBER = 3;
      public static final int OPTIONS_FIELD_NUMBER = 8;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.FieldDescriptorProto parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.FieldDescriptorProto(var1, var2);
         }
      };
      public static final int TYPE_FIELD_NUMBER = 5;
      public static final int TYPE_NAME_FIELD_NUMBER = 6;
      private static final DescriptorProtos.FieldDescriptorProto defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private Object defaultValue_;
      private Object extendee_;
      private DescriptorProtos.FieldDescriptorProto.Label label_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private Object name_;
      private int number_;
      private DescriptorProtos.FieldOptions options_;
      private Object typeName_;
      private DescriptorProtos.FieldDescriptorProto.Type type_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.FieldDescriptorProto var0 = new DescriptorProtos.FieldDescriptorProto(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private FieldDescriptorProto(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      FieldDescriptorProto(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private FieldDescriptorProto(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      FieldDescriptorProto(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private FieldDescriptorProto(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.FieldDescriptorProto getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor;
      }

      private void initFields() {
         this.name_ = "";
         this.number_ = 0;
         this.label_ = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
         this.type_ = DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
         this.typeName_ = "";
         this.extendee_ = "";
         this.defaultValue_ = "";
         this.options_ = DescriptorProtos.FieldOptions.getDefaultInstance();
      }

      public static DescriptorProtos.FieldDescriptorProto.Builder newBuilder() {
         return DescriptorProtos.FieldDescriptorProto.Builder.create();
      }

      public static DescriptorProtos.FieldDescriptorProto.Builder newBuilder(DescriptorProtos.FieldDescriptorProto var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.FieldDescriptorProto parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.FieldDescriptorProto parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.FieldDescriptorProto parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FieldDescriptorProto parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FieldDescriptorProto parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FieldDescriptorProto parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FieldDescriptorProto parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FieldDescriptorProto parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FieldDescriptorProto parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FieldDescriptorProto parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FieldDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.FieldDescriptorProto getDefaultInstanceForType() {
         return defaultInstance;
      }

      public String getDefaultValue() {
         Object var1 = this.defaultValue_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.defaultValue_ = var2;
            }

            return var2;
         }
      }

      public ByteString getDefaultValueBytes() {
         Object var1 = this.defaultValue_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.defaultValue_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public String getExtendee() {
         Object var1 = this.extendee_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.extendee_ = var2;
            }

            return var2;
         }
      }

      public ByteString getExtendeeBytes() {
         Object var1 = this.extendee_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.extendee_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.FieldDescriptorProto.Label getLabel() {
         return this.label_;
      }

      public String getName() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.name_ = var2;
            }

            return var2;
         }
      }

      public ByteString getNameBytes() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.name_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public int getNumber() {
         return this.number_;
      }

      public DescriptorProtos.FieldOptions getOptions() {
         return this.options_;
      }

      public DescriptorProtos.FieldOptionsOrBuilder getOptionsOrBuilder() {
         return this.options_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 32) == 32) {
               var1 = var2 + CodedOutputStream.computeBytesSize(2, this.getExtendeeBytes());
            }

            var2 = var1;
            if ((this.bitField0_ & 2) == 2) {
               var2 = var1 + CodedOutputStream.computeInt32Size(3, this.number_);
            }

            var1 = var2;
            if ((this.bitField0_ & 4) == 4) {
               var1 = var2 + CodedOutputStream.computeEnumSize(4, this.label_.getNumber());
            }

            var2 = var1;
            if ((this.bitField0_ & 8) == 8) {
               var2 = var1 + CodedOutputStream.computeEnumSize(5, this.type_.getNumber());
            }

            var1 = var2;
            if ((this.bitField0_ & 16) == 16) {
               var1 = var2 + CodedOutputStream.computeBytesSize(6, this.getTypeNameBytes());
            }

            var2 = var1;
            if ((this.bitField0_ & 64) == 64) {
               var2 = var1 + CodedOutputStream.computeBytesSize(7, this.getDefaultValueBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 128) == 128) {
               var1 = var2 + CodedOutputStream.computeMessageSize(8, this.options_);
            }

            var1 += this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.FieldDescriptorProto.Type getType() {
         return this.type_;
      }

      public String getTypeName() {
         Object var1 = this.typeName_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.typeName_ = var2;
            }

            return var2;
         }
      }

      public ByteString getTypeNameBytes() {
         Object var1 = this.typeName_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.typeName_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasDefaultValue() {
         return (this.bitField0_ & 64) == 64;
      }

      public boolean hasExtendee() {
         return (this.bitField0_ & 32) == 32;
      }

      public boolean hasLabel() {
         return (this.bitField0_ & 4) == 4;
      }

      public boolean hasName() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasNumber() {
         return (this.bitField0_ & 2) == 2;
      }

      public boolean hasOptions() {
         return (this.bitField0_ & 128) == 128;
      }

      public boolean hasType() {
         return (this.bitField0_ & 8) == 8;
      }

      public boolean hasTypeName() {
         return (this.bitField0_ & 16) == 16;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FieldDescriptorProto.class, DescriptorProtos.FieldDescriptorProto.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else if (this.hasOptions() && !this.getOptions().isInitialized()) {
            this.memoizedIsInitialized = 0;
            return false;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public DescriptorProtos.FieldDescriptorProto.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.FieldDescriptorProto.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.FieldDescriptorProto.Builder(var1);
      }

      public DescriptorProtos.FieldDescriptorProto.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(1, this.getNameBytes());
         }

         if ((this.bitField0_ & 32) == 32) {
            var1.writeBytes(2, this.getExtendeeBytes());
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeInt32(3, this.number_);
         }

         if ((this.bitField0_ & 4) == 4) {
            var1.writeEnum(4, this.label_.getNumber());
         }

         if ((this.bitField0_ & 8) == 8) {
            var1.writeEnum(5, this.type_.getNumber());
         }

         if ((this.bitField0_ & 16) == 16) {
            var1.writeBytes(6, this.getTypeNameBytes());
         }

         if ((this.bitField0_ & 64) == 64) {
            var1.writeBytes(7, this.getDefaultValueBytes());
         }

         if ((this.bitField0_ & 128) == 128) {
            var1.writeMessage(8, this.options_);
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.FieldDescriptorProtoOrBuilder {
         private int bitField0_;
         private Object defaultValue_;
         private Object extendee_;
         private DescriptorProtos.FieldDescriptorProto.Label label_;
         private Object name_;
         private int number_;
         private SingleFieldBuilder optionsBuilder_;
         private DescriptorProtos.FieldOptions options_;
         private Object typeName_;
         private DescriptorProtos.FieldDescriptorProto.Type type_;

         private Builder() {
            this.name_ = "";
            this.label_ = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
            this.type_ = DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
            this.typeName_ = "";
            this.extendee_ = "";
            this.defaultValue_ = "";
            this.options_ = DescriptorProtos.FieldOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.name_ = "";
            this.label_ = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
            this.type_ = DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
            this.typeName_ = "";
            this.extendee_ = "";
            this.defaultValue_ = "";
            this.options_ = DescriptorProtos.FieldOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.FieldDescriptorProto.Builder create() {
            return new DescriptorProtos.FieldDescriptorProto.Builder();
         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor;
         }

         private SingleFieldBuilder getOptionsFieldBuilder() {
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

         public DescriptorProtos.FieldDescriptorProto build() {
            DescriptorProtos.FieldDescriptorProto var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.FieldDescriptorProto buildPartial() {
            DescriptorProtos.FieldDescriptorProto var4 = new DescriptorProtos.FieldDescriptorProto(this);
            int var3 = this.bitField0_;
            int var2 = 0;
            if ((var3 & 1) == 1) {
               var2 = 0 | 1;
            }

            var4.name_ = this.name_;
            int var1 = var2;
            if ((var3 & 2) == 2) {
               var1 = var2 | 2;
            }

            var4.number_ = this.number_;
            var2 = var1;
            if ((var3 & 4) == 4) {
               var2 = var1 | 4;
            }

            var4.label_ = this.label_;
            var1 = var2;
            if ((var3 & 8) == 8) {
               var1 = var2 | 8;
            }

            var4.type_ = this.type_;
            var2 = var1;
            if ((var3 & 16) == 16) {
               var2 = var1 | 16;
            }

            var4.typeName_ = this.typeName_;
            var1 = var2;
            if ((var3 & 32) == 32) {
               var1 = var2 | 32;
            }

            var4.extendee_ = this.extendee_;
            var2 = var1;
            if ((var3 & 64) == 64) {
               var2 = var1 | 64;
            }

            var4.defaultValue_ = this.defaultValue_;
            var1 = var2;
            if ((var3 & 128) == 128) {
               var1 = var2 | 128;
            }

            SingleFieldBuilder var5 = this.optionsBuilder_;
            if (var5 == null) {
               var4.options_ = this.options_;
            } else {
               var4.options_ = (DescriptorProtos.FieldOptions)var5.build();
            }

            var4.bitField0_ = var1;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clear() {
            super.clear();
            this.name_ = "";
            int var1 = this.bitField0_ & -2;
            this.bitField0_ = var1;
            this.number_ = 0;
            this.bitField0_ = var1 & -3;
            this.label_ = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
            this.bitField0_ &= -5;
            this.type_ = DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
            var1 = this.bitField0_ & -9;
            this.bitField0_ = var1;
            this.typeName_ = "";
            var1 &= -17;
            this.bitField0_ = var1;
            this.extendee_ = "";
            var1 &= -33;
            this.bitField0_ = var1;
            this.defaultValue_ = "";
            this.bitField0_ = var1 & -65;
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = DescriptorProtos.FieldOptions.getDefaultInstance();
            } else {
               var2.clear();
            }

            this.bitField0_ &= -129;
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clearDefaultValue() {
            this.bitField0_ &= -65;
            this.defaultValue_ = DescriptorProtos.FieldDescriptorProto.getDefaultInstance().getDefaultValue();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clearExtendee() {
            this.bitField0_ &= -33;
            this.extendee_ = DescriptorProtos.FieldDescriptorProto.getDefaultInstance().getExtendee();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clearLabel() {
            this.bitField0_ &= -5;
            this.label_ = DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clearName() {
            this.bitField0_ &= -2;
            this.name_ = DescriptorProtos.FieldDescriptorProto.getDefaultInstance().getName();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clearNumber() {
            this.bitField0_ &= -3;
            this.number_ = 0;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clearOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            if (var1 == null) {
               this.options_ = DescriptorProtos.FieldOptions.getDefaultInstance();
               this.onChanged();
            } else {
               var1.clear();
            }

            this.bitField0_ &= -129;
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clearType() {
            this.bitField0_ &= -9;
            this.type_ = DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clearTypeName() {
            this.bitField0_ &= -17;
            this.typeName_ = DescriptorProtos.FieldDescriptorProto.getDefaultInstance().getTypeName();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.FieldDescriptorProto getDefaultInstanceForType() {
            return DescriptorProtos.FieldDescriptorProto.getDefaultInstance();
         }

         public String getDefaultValue() {
            Object var1 = this.defaultValue_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.defaultValue_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getDefaultValueBytes() {
            Object var1 = this.defaultValue_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.defaultValue_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor;
         }

         public String getExtendee() {
            Object var1 = this.extendee_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.extendee_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getExtendeeBytes() {
            Object var1 = this.extendee_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.extendee_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Label getLabel() {
            return this.label_;
         }

         public String getName() {
            Object var1 = this.name_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.name_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getNameBytes() {
            Object var1 = this.name_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.name_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public int getNumber() {
            return this.number_;
         }

         public DescriptorProtos.FieldOptions getOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return var1 == null ? this.options_ : (DescriptorProtos.FieldOptions)var1.getMessage();
         }

         public DescriptorProtos.FieldOptions.Builder getOptionsBuilder() {
            this.bitField0_ |= 128;
            this.onChanged();
            return (DescriptorProtos.FieldOptions.Builder)this.getOptionsFieldBuilder().getBuilder();
         }

         public DescriptorProtos.FieldOptionsOrBuilder getOptionsOrBuilder() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return (DescriptorProtos.FieldOptionsOrBuilder)(var1 != null ? (DescriptorProtos.FieldOptionsOrBuilder)var1.getMessageOrBuilder() : this.options_);
         }

         public DescriptorProtos.FieldDescriptorProto.Type getType() {
            return this.type_;
         }

         public String getTypeName() {
            Object var1 = this.typeName_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.typeName_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getTypeNameBytes() {
            Object var1 = this.typeName_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.typeName_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public boolean hasDefaultValue() {
            return (this.bitField0_ & 64) == 64;
         }

         public boolean hasExtendee() {
            return (this.bitField0_ & 32) == 32;
         }

         public boolean hasLabel() {
            return (this.bitField0_ & 4) == 4;
         }

         public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasNumber() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasOptions() {
            return (this.bitField0_ & 128) == 128;
         }

         public boolean hasType() {
            return (this.bitField0_ & 8) == 8;
         }

         public boolean hasTypeName() {
            return (this.bitField0_ & 16) == 16;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FieldDescriptorProto.class, DescriptorProtos.FieldDescriptorProto.Builder.class);
         }

         public final boolean isInitialized() {
            return !this.hasOptions() || this.getOptions().isInitialized();
         }

         public DescriptorProtos.FieldDescriptorProto.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.FieldDescriptorProto var3 = (DescriptorProtos.FieldDescriptorProto)var4;

            DescriptorProtos.FieldDescriptorProto var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.FieldDescriptorProto)DescriptorProtos.FieldDescriptorProto.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.FieldDescriptorProto)var4;

                  try {
                     var21 = (DescriptorProtos.FieldDescriptorProto)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder mergeFrom(DescriptorProtos.FieldDescriptorProto var1) {
            if (var1 == DescriptorProtos.FieldDescriptorProto.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasName()) {
                  this.bitField0_ |= 1;
                  this.name_ = var1.name_;
                  this.onChanged();
               }

               if (var1.hasNumber()) {
                  this.setNumber(var1.getNumber());
               }

               if (var1.hasLabel()) {
                  this.setLabel(var1.getLabel());
               }

               if (var1.hasType()) {
                  this.setType(var1.getType());
               }

               if (var1.hasTypeName()) {
                  this.bitField0_ |= 16;
                  this.typeName_ = var1.typeName_;
                  this.onChanged();
               }

               if (var1.hasExtendee()) {
                  this.bitField0_ |= 32;
                  this.extendee_ = var1.extendee_;
                  this.onChanged();
               }

               if (var1.hasDefaultValue()) {
                  this.bitField0_ |= 64;
                  this.defaultValue_ = var1.defaultValue_;
                  this.onChanged();
               }

               if (var1.hasOptions()) {
                  this.mergeOptions(var1.getOptions());
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.FieldDescriptorProto) {
               return this.mergeFrom((DescriptorProtos.FieldDescriptorProto)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder mergeOptions(DescriptorProtos.FieldOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if ((this.bitField0_ & 128) == 128 && this.options_ != DescriptorProtos.FieldOptions.getDefaultInstance()) {
                  this.options_ = DescriptorProtos.FieldOptions.newBuilder(this.options_).mergeFrom(var1).buildPartial();
               } else {
                  this.options_ = var1;
               }

               this.onChanged();
            } else {
               var2.mergeFrom(var1);
            }

            this.bitField0_ |= 128;
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setDefaultValue(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 64;
               this.defaultValue_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setDefaultValueBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 64;
               this.defaultValue_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setExtendee(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 32;
               this.extendee_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setExtendeeBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 32;
               this.extendee_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setLabel(DescriptorProtos.FieldDescriptorProto.Label var1) {
            if (var1 != null) {
               this.bitField0_ |= 4;
               this.label_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setName(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setNameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setNumber(int var1) {
            this.bitField0_ |= 2;
            this.number_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setOptions(DescriptorProtos.FieldOptions.Builder var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = var1.build();
               this.onChanged();
            } else {
               var2.setMessage(var1.build());
            }

            this.bitField0_ |= 128;
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setOptions(DescriptorProtos.FieldOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if (var1 == null) {
                  throw null;
               }

               this.options_ = var1;
               this.onChanged();
            } else {
               var2.setMessage(var1);
            }

            this.bitField0_ |= 128;
            return this;
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setType(DescriptorProtos.FieldDescriptorProto.Type var1) {
            if (var1 != null) {
               this.bitField0_ |= 8;
               this.type_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setTypeName(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 16;
               this.typeName_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder setTypeNameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 16;
               this.typeName_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }
      }

      public static enum Label implements ProtocolMessageEnum {
         LABEL_OPTIONAL(0, 1);

         public static final int LABEL_OPTIONAL_VALUE = 1;
         LABEL_REPEATED;

         public static final int LABEL_REPEATED_VALUE = 3;
         LABEL_REQUIRED(1, 2);

         public static final int LABEL_REQUIRED_VALUE = 2;
         private static final DescriptorProtos.FieldDescriptorProto.Label[] VALUES;
         private static Internal.EnumLiteMap internalValueMap;
         private final int index;
         private final int value;

         static {
            DescriptorProtos.FieldDescriptorProto.Label var0 = new DescriptorProtos.FieldDescriptorProto.Label("LABEL_REPEATED", 2, 2, 3);
            LABEL_REPEATED = var0;
            internalValueMap = new Internal.EnumLiteMap() {
               public DescriptorProtos.FieldDescriptorProto.Label findValueByNumber(int var1) {
                  return DescriptorProtos.FieldDescriptorProto.Label.valueOf(var1);
               }
            };
            VALUES = values();
         }

         private Label(int var3, int var4) {
            this.index = var3;
            this.value = var4;
         }

         public static final Descriptors.EnumDescriptor getDescriptor() {
            return (Descriptors.EnumDescriptor)DescriptorProtos.FieldDescriptorProto.getDescriptor().getEnumTypes().get(1);
         }

         public static Internal.EnumLiteMap internalGetValueMap() {
            return internalValueMap;
         }

         public static DescriptorProtos.FieldDescriptorProto.Label valueOf(int var0) {
            if (var0 != 1) {
               if (var0 != 2) {
                  return var0 != 3 ? null : LABEL_REPEATED;
               } else {
                  return LABEL_REQUIRED;
               }
            } else {
               return LABEL_OPTIONAL;
            }
         }

         public static DescriptorProtos.FieldDescriptorProto.Label valueOf(Descriptors.EnumValueDescriptor var0) {
            if (var0.getType() == getDescriptor()) {
               return VALUES[var0.getIndex()];
            } else {
               throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
         }

         public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
         }

         public final int getNumber() {
            return this.value;
         }

         public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            return (Descriptors.EnumValueDescriptor)getDescriptor().getValues().get(this.index);
         }
      }

      public static enum Type implements ProtocolMessageEnum {
         TYPE_BOOL(7, 8);

         public static final int TYPE_BOOL_VALUE = 8;
         TYPE_BYTES(11, 12);

         public static final int TYPE_BYTES_VALUE = 12;
         TYPE_DOUBLE(0, 1);

         public static final int TYPE_DOUBLE_VALUE = 1;
         TYPE_ENUM(13, 14);

         public static final int TYPE_ENUM_VALUE = 14;
         TYPE_FIXED32(6, 7);

         public static final int TYPE_FIXED32_VALUE = 7;
         TYPE_FIXED64(5, 6);

         public static final int TYPE_FIXED64_VALUE = 6;
         TYPE_FLOAT(1, 2);

         public static final int TYPE_FLOAT_VALUE = 2;
         TYPE_GROUP(9, 10);

         public static final int TYPE_GROUP_VALUE = 10;
         TYPE_INT32(4, 5);

         public static final int TYPE_INT32_VALUE = 5;
         TYPE_INT64(2, 3);

         public static final int TYPE_INT64_VALUE = 3;
         TYPE_MESSAGE(10, 11);

         public static final int TYPE_MESSAGE_VALUE = 11;
         TYPE_SFIXED32(14, 15);

         public static final int TYPE_SFIXED32_VALUE = 15;
         TYPE_SFIXED64(15, 16);

         public static final int TYPE_SFIXED64_VALUE = 16;
         TYPE_SINT32(16, 17);

         public static final int TYPE_SINT32_VALUE = 17;
         TYPE_SINT64;

         public static final int TYPE_SINT64_VALUE = 18;
         TYPE_STRING(8, 9);

         public static final int TYPE_STRING_VALUE = 9;
         TYPE_UINT32(12, 13);

         public static final int TYPE_UINT32_VALUE = 13;
         TYPE_UINT64(3, 4);

         public static final int TYPE_UINT64_VALUE = 4;
         private static final DescriptorProtos.FieldDescriptorProto.Type[] VALUES;
         private static Internal.EnumLiteMap internalValueMap;
         private final int index;
         private final int value;

         static {
            DescriptorProtos.FieldDescriptorProto.Type var0 = new DescriptorProtos.FieldDescriptorProto.Type("TYPE_SINT64", 17, 17, 18);
            TYPE_SINT64 = var0;
            internalValueMap = new Internal.EnumLiteMap() {
               public DescriptorProtos.FieldDescriptorProto.Type findValueByNumber(int var1) {
                  return DescriptorProtos.FieldDescriptorProto.Type.valueOf(var1);
               }
            };
            VALUES = values();
         }

         private Type(int var3, int var4) {
            this.index = var3;
            this.value = var4;
         }

         public static final Descriptors.EnumDescriptor getDescriptor() {
            return (Descriptors.EnumDescriptor)DescriptorProtos.FieldDescriptorProto.getDescriptor().getEnumTypes().get(0);
         }

         public static Internal.EnumLiteMap internalGetValueMap() {
            return internalValueMap;
         }

         public static DescriptorProtos.FieldDescriptorProto.Type valueOf(int var0) {
            switch(var0) {
            case 1:
               return TYPE_DOUBLE;
            case 2:
               return TYPE_FLOAT;
            case 3:
               return TYPE_INT64;
            case 4:
               return TYPE_UINT64;
            case 5:
               return TYPE_INT32;
            case 6:
               return TYPE_FIXED64;
            case 7:
               return TYPE_FIXED32;
            case 8:
               return TYPE_BOOL;
            case 9:
               return TYPE_STRING;
            case 10:
               return TYPE_GROUP;
            case 11:
               return TYPE_MESSAGE;
            case 12:
               return TYPE_BYTES;
            case 13:
               return TYPE_UINT32;
            case 14:
               return TYPE_ENUM;
            case 15:
               return TYPE_SFIXED32;
            case 16:
               return TYPE_SFIXED64;
            case 17:
               return TYPE_SINT32;
            case 18:
               return TYPE_SINT64;
            default:
               return null;
            }
         }

         public static DescriptorProtos.FieldDescriptorProto.Type valueOf(Descriptors.EnumValueDescriptor var0) {
            if (var0.getType() == getDescriptor()) {
               return VALUES[var0.getIndex()];
            } else {
               throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
         }

         public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
         }

         public final int getNumber() {
            return this.value;
         }

         public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            return (Descriptors.EnumValueDescriptor)getDescriptor().getValues().get(this.index);
         }
      }
   }

   public interface FieldDescriptorProtoOrBuilder extends MessageOrBuilder {
      String getDefaultValue();

      ByteString getDefaultValueBytes();

      String getExtendee();

      ByteString getExtendeeBytes();

      DescriptorProtos.FieldDescriptorProto.Label getLabel();

      String getName();

      ByteString getNameBytes();

      int getNumber();

      DescriptorProtos.FieldOptions getOptions();

      DescriptorProtos.FieldOptionsOrBuilder getOptionsOrBuilder();

      DescriptorProtos.FieldDescriptorProto.Type getType();

      String getTypeName();

      ByteString getTypeNameBytes();

      boolean hasDefaultValue();

      boolean hasExtendee();

      boolean hasLabel();

      boolean hasName();

      boolean hasNumber();

      boolean hasOptions();

      boolean hasType();

      boolean hasTypeName();
   }

   public static final class FieldOptions extends GeneratedMessage.ExtendableMessage implements DescriptorProtos.FieldOptionsOrBuilder {
      public static final int CTYPE_FIELD_NUMBER = 1;
      public static final int DEPRECATED_FIELD_NUMBER = 3;
      public static final int EXPERIMENTAL_MAP_KEY_FIELD_NUMBER = 9;
      public static final int LAZY_FIELD_NUMBER = 5;
      public static final int PACKED_FIELD_NUMBER = 2;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.FieldOptions parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.FieldOptions(var1, var2);
         }
      };
      public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
      public static final int WEAK_FIELD_NUMBER = 10;
      private static final DescriptorProtos.FieldOptions defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private DescriptorProtos.FieldOptions.CType ctype_;
      private boolean deprecated_;
      private Object experimentalMapKey_;
      private boolean lazy_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private boolean packed_;
      private List uninterpretedOption_;
      private final UnknownFieldSet unknownFields;
      private boolean weak_;

      static {
         DescriptorProtos.FieldOptions var0 = new DescriptorProtos.FieldOptions(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private FieldOptions(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      FieldOptions(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private FieldOptions(GeneratedMessage.ExtendableBuilder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      FieldOptions(GeneratedMessage.ExtendableBuilder var1, Object var2) {
         this(var1);
      }

      private FieldOptions(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.FieldOptions getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor;
      }

      private void initFields() {
         this.ctype_ = DescriptorProtos.FieldOptions.CType.STRING;
         this.packed_ = false;
         this.lazy_ = false;
         this.deprecated_ = false;
         this.experimentalMapKey_ = "";
         this.weak_ = false;
         this.uninterpretedOption_ = Collections.emptyList();
      }

      public static DescriptorProtos.FieldOptions.Builder newBuilder() {
         return DescriptorProtos.FieldOptions.Builder.create();
      }

      public static DescriptorProtos.FieldOptions.Builder newBuilder(DescriptorProtos.FieldOptions var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.FieldOptions parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FieldOptions)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.FieldOptions parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FieldOptions)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.FieldOptions parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FieldOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FieldOptions parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FieldOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FieldOptions parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.FieldOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FieldOptions parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FieldOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FieldOptions parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FieldOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FieldOptions parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FieldOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FieldOptions parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FieldOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FieldOptions parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FieldOptions)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.FieldOptions.CType getCtype() {
         return this.ctype_;
      }

      public DescriptorProtos.FieldOptions getDefaultInstanceForType() {
         return defaultInstance;
      }

      public boolean getDeprecated() {
         return this.deprecated_;
      }

      public String getExperimentalMapKey() {
         Object var1 = this.experimentalMapKey_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.experimentalMapKey_ = var2;
            }

            return var2;
         }
      }

      public ByteString getExperimentalMapKeyBytes() {
         Object var1 = this.experimentalMapKey_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.experimentalMapKey_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public boolean getLazy() {
         return this.lazy_;
      }

      public boolean getPacked() {
         return this.packed_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var2 = 0 + CodedOutputStream.computeEnumSize(1, this.ctype_.getNumber());
            }

            var1 = var2;
            if ((this.bitField0_ & 2) == 2) {
               var1 = var2 + CodedOutputStream.computeBoolSize(2, this.packed_);
            }

            var2 = var1;
            if ((this.bitField0_ & 8) == 8) {
               var2 = var1 + CodedOutputStream.computeBoolSize(3, this.deprecated_);
            }

            var1 = var2;
            if ((this.bitField0_ & 4) == 4) {
               var1 = var2 + CodedOutputStream.computeBoolSize(5, this.lazy_);
            }

            var2 = var1;
            if ((this.bitField0_ & 16) == 16) {
               var2 = var1 + CodedOutputStream.computeBytesSize(9, this.getExperimentalMapKeyBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 32) == 32) {
               var1 = var2 + CodedOutputStream.computeBoolSize(10, this.weak_);
            }

            for(var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(999, (MessageLite)this.uninterpretedOption_.get(var2));
            }

            var1 = var1 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
         return (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1);
      }

      public int getUninterpretedOptionCount() {
         return this.uninterpretedOption_.size();
      }

      public List getUninterpretedOptionList() {
         return this.uninterpretedOption_;
      }

      public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
         return (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1);
      }

      public List getUninterpretedOptionOrBuilderList() {
         return this.uninterpretedOption_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean getWeak() {
         return this.weak_;
      }

      public boolean hasCtype() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasDeprecated() {
         return (this.bitField0_ & 8) == 8;
      }

      public boolean hasExperimentalMapKey() {
         return (this.bitField0_ & 16) == 16;
      }

      public boolean hasLazy() {
         return (this.bitField0_ & 4) == 4;
      }

      public boolean hasPacked() {
         return (this.bitField0_ & 2) == 2;
      }

      public boolean hasWeak() {
         return (this.bitField0_ & 32) == 32;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_FieldOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FieldOptions.class, DescriptorProtos.FieldOptions.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getUninterpretedOptionCount(); ++var3) {
               if (!this.getUninterpretedOption(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.FieldOptions.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.FieldOptions.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.FieldOptions.Builder(var1);
      }

      public DescriptorProtos.FieldOptions.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         GeneratedMessage.ExtendableMessage.ExtensionWriter var3 = this.newExtensionWriter();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeEnum(1, this.ctype_.getNumber());
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeBool(2, this.packed_);
         }

         if ((this.bitField0_ & 8) == 8) {
            var1.writeBool(3, this.deprecated_);
         }

         if ((this.bitField0_ & 4) == 4) {
            var1.writeBool(5, this.lazy_);
         }

         if ((this.bitField0_ & 16) == 16) {
            var1.writeBytes(9, this.getExperimentalMapKeyBytes());
         }

         if ((this.bitField0_ & 32) == 32) {
            var1.writeBool(10, this.weak_);
         }

         for(int var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
            var1.writeMessage(999, (MessageLite)this.uninterpretedOption_.get(var2));
         }

         var3.writeUntil(536870912, var1);
         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.ExtendableBuilder implements DescriptorProtos.FieldOptionsOrBuilder {
         private int bitField0_;
         private DescriptorProtos.FieldOptions.CType ctype_;
         private boolean deprecated_;
         private Object experimentalMapKey_;
         private boolean lazy_;
         private boolean packed_;
         private RepeatedFieldBuilder uninterpretedOptionBuilder_;
         private List uninterpretedOption_;
         private boolean weak_;

         private Builder() {
            this.ctype_ = DescriptorProtos.FieldOptions.CType.STRING;
            this.experimentalMapKey_ = "";
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.ctype_ = DescriptorProtos.FieldOptions.CType.STRING;
            this.experimentalMapKey_ = "";
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.FieldOptions.Builder create() {
            return new DescriptorProtos.FieldOptions.Builder();
         }

         private void ensureUninterpretedOptionIsMutable() {
            if ((this.bitField0_ & 64) != 64) {
               this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
               this.bitField0_ |= 64;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor;
         }

         private RepeatedFieldBuilder getUninterpretedOptionFieldBuilder() {
            if (this.uninterpretedOptionBuilder_ == null) {
               List var2 = this.uninterpretedOption_;
               boolean var1;
               if ((this.bitField0_ & 64) == 64) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.uninterpretedOption_ = null;
            }

            return this.uninterpretedOptionBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getUninterpretedOptionFieldBuilder();
            }

         }

         public DescriptorProtos.FieldOptions.Builder addAllUninterpretedOption(Iterable var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               GeneratedMessage.ExtendableBuilder.addAll(var1, this.uninterpretedOption_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption.Builder var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(var1, DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.FieldOptions build() {
            DescriptorProtos.FieldOptions var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.FieldOptions buildPartial() {
            DescriptorProtos.FieldOptions var4 = new DescriptorProtos.FieldOptions(this);
            int var3 = this.bitField0_;
            int var2 = 0;
            if ((var3 & 1) == 1) {
               var2 = 0 | 1;
            }

            var4.ctype_ = this.ctype_;
            int var1 = var2;
            if ((var3 & 2) == 2) {
               var1 = var2 | 2;
            }

            var4.packed_ = this.packed_;
            var2 = var1;
            if ((var3 & 4) == 4) {
               var2 = var1 | 4;
            }

            var4.lazy_ = this.lazy_;
            var1 = var2;
            if ((var3 & 8) == 8) {
               var1 = var2 | 8;
            }

            var4.deprecated_ = this.deprecated_;
            var2 = var1;
            if ((var3 & 16) == 16) {
               var2 = var1 | 16;
            }

            var4.experimentalMapKey_ = this.experimentalMapKey_;
            var1 = var2;
            if ((var3 & 32) == 32) {
               var1 = var2 | 32;
            }

            var4.weak_ = this.weak_;
            RepeatedFieldBuilder var5 = this.uninterpretedOptionBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 64) == 64) {
                  this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                  this.bitField0_ &= -65;
               }

               var4.uninterpretedOption_ = this.uninterpretedOption_;
            } else {
               var4.uninterpretedOption_ = var5.build();
            }

            var4.bitField0_ = var1;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.FieldOptions.Builder clear() {
            super.clear();
            this.ctype_ = DescriptorProtos.FieldOptions.CType.STRING;
            int var1 = this.bitField0_ & -2;
            this.bitField0_ = var1;
            this.packed_ = false;
            var1 &= -3;
            this.bitField0_ = var1;
            this.lazy_ = false;
            var1 &= -5;
            this.bitField0_ = var1;
            this.deprecated_ = false;
            var1 &= -9;
            this.bitField0_ = var1;
            this.experimentalMapKey_ = "";
            var1 &= -17;
            this.bitField0_ = var1;
            this.weak_ = false;
            this.bitField0_ = var1 & -33;
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -65;
               return this;
            } else {
               var2.clear();
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder clearCtype() {
            this.bitField0_ &= -2;
            this.ctype_ = DescriptorProtos.FieldOptions.CType.STRING;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder clearDeprecated() {
            this.bitField0_ &= -9;
            this.deprecated_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder clearExperimentalMapKey() {
            this.bitField0_ &= -17;
            this.experimentalMapKey_ = DescriptorProtos.FieldOptions.getDefaultInstance().getExperimentalMapKey();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder clearLazy() {
            this.bitField0_ &= -5;
            this.lazy_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder clearPacked() {
            this.bitField0_ &= -3;
            this.packed_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder clearUninterpretedOption() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -65;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder clearWeak() {
            this.bitField0_ &= -33;
            this.weak_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.FieldOptions.CType getCtype() {
            return this.ctype_;
         }

         public DescriptorProtos.FieldOptions getDefaultInstanceForType() {
            return DescriptorProtos.FieldOptions.getDefaultInstance();
         }

         public boolean getDeprecated() {
            return this.deprecated_;
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor;
         }

         public String getExperimentalMapKey() {
            Object var1 = this.experimentalMapKey_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.experimentalMapKey_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getExperimentalMapKeyBytes() {
            Object var1 = this.experimentalMapKey_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.experimentalMapKey_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public boolean getLazy() {
            return this.lazy_;
         }

         public boolean getPacked() {
            return this.packed_;
         }

         public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOption)var2.getMessage(var1);
         }

         public DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().getBuilder(var1);
         }

         public List getUninterpretedOptionBuilderList() {
            return this.getUninterpretedOptionFieldBuilder().getBuilderList();
         }

         public int getUninterpretedOptionCount() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? this.uninterpretedOption_.size() : var1.getCount();
         }

         public List getUninterpretedOptionList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.uninterpretedOption_) : var1.getMessageList();
         }

         public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOptionOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getUninterpretedOptionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
         }

         public boolean getWeak() {
            return this.weak_;
         }

         public boolean hasCtype() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasDeprecated() {
            return (this.bitField0_ & 8) == 8;
         }

         public boolean hasExperimentalMapKey() {
            return (this.bitField0_ & 16) == 16;
         }

         public boolean hasLazy() {
            return (this.bitField0_ & 4) == 4;
         }

         public boolean hasPacked() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasWeak() {
            return (this.bitField0_ & 32) == 32;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_FieldOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FieldOptions.class, DescriptorProtos.FieldOptions.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getUninterpretedOptionCount(); ++var1) {
               if (!this.getUninterpretedOption(var1).isInitialized()) {
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.FieldOptions.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.FieldOptions var3 = (DescriptorProtos.FieldOptions)var4;

            DescriptorProtos.FieldOptions var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.FieldOptions)DescriptorProtos.FieldOptions.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.FieldOptions)var4;

                  try {
                     var21 = (DescriptorProtos.FieldOptions)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.FieldOptions.Builder mergeFrom(DescriptorProtos.FieldOptions var1) {
            if (var1 == DescriptorProtos.FieldOptions.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasCtype()) {
                  this.setCtype(var1.getCtype());
               }

               if (var1.hasPacked()) {
                  this.setPacked(var1.getPacked());
               }

               if (var1.hasLazy()) {
                  this.setLazy(var1.getLazy());
               }

               if (var1.hasDeprecated()) {
                  this.setDeprecated(var1.getDeprecated());
               }

               if (var1.hasExperimentalMapKey()) {
                  this.bitField0_ |= 16;
                  this.experimentalMapKey_ = var1.experimentalMapKey_;
                  this.onChanged();
               }

               if (var1.hasWeak()) {
                  this.setWeak(var1.getWeak());
               }

               if (this.uninterpretedOptionBuilder_ == null) {
                  if (!var1.uninterpretedOption_.isEmpty()) {
                     if (this.uninterpretedOption_.isEmpty()) {
                        this.uninterpretedOption_ = var1.uninterpretedOption_;
                        this.bitField0_ &= -65;
                     } else {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.addAll(var1.uninterpretedOption_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.uninterpretedOption_.isEmpty()) {
                  if (this.uninterpretedOptionBuilder_.isEmpty()) {
                     this.uninterpretedOptionBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.uninterpretedOptionBuilder_ = null;
                     this.uninterpretedOption_ = var1.uninterpretedOption_;
                     this.bitField0_ &= -65;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getUninterpretedOptionFieldBuilder();
                     }

                     this.uninterpretedOptionBuilder_ = var2;
                  } else {
                     this.uninterpretedOptionBuilder_.addAllMessages(var1.uninterpretedOption_);
                  }
               }

               this.mergeExtensionFields(var1);
               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.FieldOptions) {
               return this.mergeFrom((DescriptorProtos.FieldOptions)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder removeUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder setCtype(DescriptorProtos.FieldOptions.CType var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.ctype_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldOptions.Builder setDeprecated(boolean var1) {
            this.bitField0_ |= 8;
            this.deprecated_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder setExperimentalMapKey(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 16;
               this.experimentalMapKey_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldOptions.Builder setExperimentalMapKeyBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 16;
               this.experimentalMapKey_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FieldOptions.Builder setLazy(boolean var1) {
            this.bitField0_ |= 4;
            this.lazy_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder setPacked(boolean var1) {
            this.bitField0_ |= 2;
            this.packed_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FieldOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FieldOptions.Builder setWeak(boolean var1) {
            this.bitField0_ |= 32;
            this.weak_ = var1;
            this.onChanged();
            return this;
         }
      }

      public static enum CType implements ProtocolMessageEnum {
         CORD(1, 1);

         public static final int CORD_VALUE = 1;
         STRING(0, 0),
         STRING_PIECE;

         public static final int STRING_PIECE_VALUE = 2;
         public static final int STRING_VALUE = 0;
         private static final DescriptorProtos.FieldOptions.CType[] VALUES;
         private static Internal.EnumLiteMap internalValueMap;
         private final int index;
         private final int value;

         static {
            DescriptorProtos.FieldOptions.CType var0 = new DescriptorProtos.FieldOptions.CType("STRING_PIECE", 2, 2, 2);
            STRING_PIECE = var0;
            internalValueMap = new Internal.EnumLiteMap() {
               public DescriptorProtos.FieldOptions.CType findValueByNumber(int var1) {
                  return DescriptorProtos.FieldOptions.CType.valueOf(var1);
               }
            };
            VALUES = values();
         }

         private CType(int var3, int var4) {
            this.index = var3;
            this.value = var4;
         }

         public static final Descriptors.EnumDescriptor getDescriptor() {
            return (Descriptors.EnumDescriptor)DescriptorProtos.FieldOptions.getDescriptor().getEnumTypes().get(0);
         }

         public static Internal.EnumLiteMap internalGetValueMap() {
            return internalValueMap;
         }

         public static DescriptorProtos.FieldOptions.CType valueOf(int var0) {
            if (var0 != 0) {
               if (var0 != 1) {
                  return var0 != 2 ? null : STRING_PIECE;
               } else {
                  return CORD;
               }
            } else {
               return STRING;
            }
         }

         public static DescriptorProtos.FieldOptions.CType valueOf(Descriptors.EnumValueDescriptor var0) {
            if (var0.getType() == getDescriptor()) {
               return VALUES[var0.getIndex()];
            } else {
               throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
         }

         public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
         }

         public final int getNumber() {
            return this.value;
         }

         public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            return (Descriptors.EnumValueDescriptor)getDescriptor().getValues().get(this.index);
         }
      }
   }

   public interface FieldOptionsOrBuilder extends GeneratedMessage.ExtendableMessageOrBuilder {
      DescriptorProtos.FieldOptions.CType getCtype();

      boolean getDeprecated();

      String getExperimentalMapKey();

      ByteString getExperimentalMapKeyBytes();

      boolean getLazy();

      boolean getPacked();

      DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1);

      int getUninterpretedOptionCount();

      List getUninterpretedOptionList();

      DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

      List getUninterpretedOptionOrBuilderList();

      boolean getWeak();

      boolean hasCtype();

      boolean hasDeprecated();

      boolean hasExperimentalMapKey();

      boolean hasLazy();

      boolean hasPacked();

      boolean hasWeak();
   }

   public static final class FileDescriptorProto extends GeneratedMessage implements DescriptorProtos.FileDescriptorProtoOrBuilder {
      public static final int DEPENDENCY_FIELD_NUMBER = 3;
      public static final int ENUM_TYPE_FIELD_NUMBER = 5;
      public static final int EXTENSION_FIELD_NUMBER = 7;
      public static final int MESSAGE_TYPE_FIELD_NUMBER = 4;
      public static final int NAME_FIELD_NUMBER = 1;
      public static final int OPTIONS_FIELD_NUMBER = 8;
      public static final int PACKAGE_FIELD_NUMBER = 2;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.FileDescriptorProto parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.FileDescriptorProto(var1, var2);
         }
      };
      public static final int PUBLIC_DEPENDENCY_FIELD_NUMBER = 10;
      public static final int SERVICE_FIELD_NUMBER = 6;
      public static final int SOURCE_CODE_INFO_FIELD_NUMBER = 9;
      public static final int WEAK_DEPENDENCY_FIELD_NUMBER = 11;
      private static final DescriptorProtos.FileDescriptorProto defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private LazyStringList dependency_;
      private List enumType_;
      private List extension_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private List messageType_;
      private Object name_;
      private DescriptorProtos.FileOptions options_;
      private Object package_;
      private List publicDependency_;
      private List service_;
      private DescriptorProtos.SourceCodeInfo sourceCodeInfo_;
      private final UnknownFieldSet unknownFields;
      private List weakDependency_;

      static {
         DescriptorProtos.FileDescriptorProto var0 = new DescriptorProtos.FileDescriptorProto(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private FileDescriptorProto(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      FileDescriptorProto(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private FileDescriptorProto(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      FileDescriptorProto(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private FileDescriptorProto(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.FileDescriptorProto getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor;
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
         this.options_ = DescriptorProtos.FileOptions.getDefaultInstance();
         this.sourceCodeInfo_ = DescriptorProtos.SourceCodeInfo.getDefaultInstance();
      }

      public static DescriptorProtos.FileDescriptorProto.Builder newBuilder() {
         return DescriptorProtos.FileDescriptorProto.Builder.create();
      }

      public static DescriptorProtos.FileDescriptorProto.Builder newBuilder(DescriptorProtos.FileDescriptorProto var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorProto parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorProto parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.FileDescriptorProto parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorProto parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileDescriptorProto parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorProto parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileDescriptorProto parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorProto parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileDescriptorProto parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorProto parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.FileDescriptorProto getDefaultInstanceForType() {
         return defaultInstance;
      }

      public String getDependency(int var1) {
         return (String)this.dependency_.get(var1);
      }

      public ByteString getDependencyBytes(int var1) {
         return this.dependency_.getByteString(var1);
      }

      public int getDependencyCount() {
         return this.dependency_.size();
      }

      public List getDependencyList() {
         return this.dependency_;
      }

      public DescriptorProtos.EnumDescriptorProto getEnumType(int var1) {
         return (DescriptorProtos.EnumDescriptorProto)this.enumType_.get(var1);
      }

      public int getEnumTypeCount() {
         return this.enumType_.size();
      }

      public List getEnumTypeList() {
         return this.enumType_;
      }

      public DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int var1) {
         return (DescriptorProtos.EnumDescriptorProtoOrBuilder)this.enumType_.get(var1);
      }

      public List getEnumTypeOrBuilderList() {
         return this.enumType_;
      }

      public DescriptorProtos.FieldDescriptorProto getExtension(int var1) {
         return (DescriptorProtos.FieldDescriptorProto)this.extension_.get(var1);
      }

      public int getExtensionCount() {
         return this.extension_.size();
      }

      public List getExtensionList() {
         return this.extension_;
      }

      public DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int var1) {
         return (DescriptorProtos.FieldDescriptorProtoOrBuilder)this.extension_.get(var1);
      }

      public List getExtensionOrBuilderList() {
         return this.extension_;
      }

      public DescriptorProtos.DescriptorProto getMessageType(int var1) {
         return (DescriptorProtos.DescriptorProto)this.messageType_.get(var1);
      }

      public int getMessageTypeCount() {
         return this.messageType_.size();
      }

      public List getMessageTypeList() {
         return this.messageType_;
      }

      public DescriptorProtos.DescriptorProtoOrBuilder getMessageTypeOrBuilder(int var1) {
         return (DescriptorProtos.DescriptorProtoOrBuilder)this.messageType_.get(var1);
      }

      public List getMessageTypeOrBuilderList() {
         return this.messageType_;
      }

      public String getName() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.name_ = var2;
            }

            return var2;
         }
      }

      public ByteString getNameBytes() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.name_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.FileOptions getOptions() {
         return this.options_;
      }

      public DescriptorProtos.FileOptionsOrBuilder getOptionsOrBuilder() {
         return this.options_;
      }

      public String getPackage() {
         Object var1 = this.package_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.package_ = var2;
            }

            return var2;
         }
      }

      public ByteString getPackageBytes() {
         Object var1 = this.package_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.package_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getPublicDependency(int var1) {
         return (Integer)this.publicDependency_.get(var1);
      }

      public int getPublicDependencyCount() {
         return this.publicDependency_.size();
      }

      public List getPublicDependencyList() {
         return this.publicDependency_;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            var1 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var1 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }

            int var2 = var1;
            if ((this.bitField0_ & 2) == 2) {
               var2 = var1 + CodedOutputStream.computeBytesSize(2, this.getPackageBytes());
            }

            int var3 = 0;

            for(var1 = 0; var1 < this.dependency_.size(); ++var1) {
               var3 += CodedOutputStream.computeBytesSizeNoTag(this.dependency_.getByteString(var1));
            }

            var1 = var2 + var3 + this.getDependencyList().size() * 1;

            for(var2 = 0; var2 < this.messageType_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(4, (MessageLite)this.messageType_.get(var2));
            }

            for(var2 = 0; var2 < this.enumType_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(5, (MessageLite)this.enumType_.get(var2));
            }

            for(var2 = 0; var2 < this.service_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(6, (MessageLite)this.service_.get(var2));
            }

            for(var2 = 0; var2 < this.extension_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(7, (MessageLite)this.extension_.get(var2));
            }

            var2 = var1;
            if ((this.bitField0_ & 4) == 4) {
               var2 = var1 + CodedOutputStream.computeMessageSize(8, this.options_);
            }

            var1 = var2;
            if ((this.bitField0_ & 8) == 8) {
               var1 = var2 + CodedOutputStream.computeMessageSize(9, this.sourceCodeInfo_);
            }

            var2 = 0;

            for(var3 = 0; var3 < this.publicDependency_.size(); ++var3) {
               var2 += CodedOutputStream.computeInt32SizeNoTag((Integer)this.publicDependency_.get(var3));
            }

            int var5 = this.getPublicDependencyList().size();
            int var4 = 0;

            for(var3 = 0; var3 < this.weakDependency_.size(); ++var3) {
               var4 += CodedOutputStream.computeInt32SizeNoTag((Integer)this.weakDependency_.get(var3));
            }

            var1 = var1 + var2 + var5 * 1 + var4 + this.getWeakDependencyList().size() * 1 + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.ServiceDescriptorProto getService(int var1) {
         return (DescriptorProtos.ServiceDescriptorProto)this.service_.get(var1);
      }

      public int getServiceCount() {
         return this.service_.size();
      }

      public List getServiceList() {
         return this.service_;
      }

      public DescriptorProtos.ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int var1) {
         return (DescriptorProtos.ServiceDescriptorProtoOrBuilder)this.service_.get(var1);
      }

      public List getServiceOrBuilderList() {
         return this.service_;
      }

      public DescriptorProtos.SourceCodeInfo getSourceCodeInfo() {
         return this.sourceCodeInfo_;
      }

      public DescriptorProtos.SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder() {
         return this.sourceCodeInfo_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public int getWeakDependency(int var1) {
         return (Integer)this.weakDependency_.get(var1);
      }

      public int getWeakDependencyCount() {
         return this.weakDependency_.size();
      }

      public List getWeakDependencyList() {
         return this.weakDependency_;
      }

      public boolean hasName() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasOptions() {
         return (this.bitField0_ & 4) == 4;
      }

      public boolean hasPackage() {
         return (this.bitField0_ & 2) == 2;
      }

      public boolean hasSourceCodeInfo() {
         return (this.bitField0_ & 8) == 8;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FileDescriptorProto.class, DescriptorProtos.FileDescriptorProto.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         if (var1 != -1) {
            return var1 == 1;
         } else {
            int var2;
            for(var2 = 0; var2 < this.getMessageTypeCount(); ++var2) {
               if (!this.getMessageType(var2).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            for(var2 = 0; var2 < this.getEnumTypeCount(); ++var2) {
               if (!this.getEnumType(var2).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            for(var2 = 0; var2 < this.getServiceCount(); ++var2) {
               if (!this.getService(var2).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            for(var2 = 0; var2 < this.getExtensionCount(); ++var2) {
               if (!this.getExtension(var2).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (this.hasOptions() && !this.getOptions().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.FileDescriptorProto.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.FileDescriptorProto.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.FileDescriptorProto.Builder(var1);
      }

      public DescriptorProtos.FileDescriptorProto.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(1, this.getNameBytes());
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeBytes(2, this.getPackageBytes());
         }

         int var2;
         for(var2 = 0; var2 < this.dependency_.size(); ++var2) {
            var1.writeBytes(3, this.dependency_.getByteString(var2));
         }

         for(var2 = 0; var2 < this.messageType_.size(); ++var2) {
            var1.writeMessage(4, (MessageLite)this.messageType_.get(var2));
         }

         for(var2 = 0; var2 < this.enumType_.size(); ++var2) {
            var1.writeMessage(5, (MessageLite)this.enumType_.get(var2));
         }

         for(var2 = 0; var2 < this.service_.size(); ++var2) {
            var1.writeMessage(6, (MessageLite)this.service_.get(var2));
         }

         for(var2 = 0; var2 < this.extension_.size(); ++var2) {
            var1.writeMessage(7, (MessageLite)this.extension_.get(var2));
         }

         if ((this.bitField0_ & 4) == 4) {
            var1.writeMessage(8, this.options_);
         }

         if ((this.bitField0_ & 8) == 8) {
            var1.writeMessage(9, this.sourceCodeInfo_);
         }

         for(var2 = 0; var2 < this.publicDependency_.size(); ++var2) {
            var1.writeInt32(10, (Integer)this.publicDependency_.get(var2));
         }

         for(var2 = 0; var2 < this.weakDependency_.size(); ++var2) {
            var1.writeInt32(11, (Integer)this.weakDependency_.get(var2));
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.FileDescriptorProtoOrBuilder {
         private int bitField0_;
         private LazyStringList dependency_;
         private RepeatedFieldBuilder enumTypeBuilder_;
         private List enumType_;
         private RepeatedFieldBuilder extensionBuilder_;
         private List extension_;
         private RepeatedFieldBuilder messageTypeBuilder_;
         private List messageType_;
         private Object name_;
         private SingleFieldBuilder optionsBuilder_;
         private DescriptorProtos.FileOptions options_;
         private Object package_;
         private List publicDependency_;
         private RepeatedFieldBuilder serviceBuilder_;
         private List service_;
         private SingleFieldBuilder sourceCodeInfoBuilder_;
         private DescriptorProtos.SourceCodeInfo sourceCodeInfo_;
         private List weakDependency_;

         private Builder() {
            this.name_ = "";
            this.package_ = "";
            this.dependency_ = LazyStringArrayList.EMPTY;
            this.publicDependency_ = Collections.emptyList();
            this.weakDependency_ = Collections.emptyList();
            this.messageType_ = Collections.emptyList();
            this.enumType_ = Collections.emptyList();
            this.service_ = Collections.emptyList();
            this.extension_ = Collections.emptyList();
            this.options_ = DescriptorProtos.FileOptions.getDefaultInstance();
            this.sourceCodeInfo_ = DescriptorProtos.SourceCodeInfo.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.name_ = "";
            this.package_ = "";
            this.dependency_ = LazyStringArrayList.EMPTY;
            this.publicDependency_ = Collections.emptyList();
            this.weakDependency_ = Collections.emptyList();
            this.messageType_ = Collections.emptyList();
            this.enumType_ = Collections.emptyList();
            this.service_ = Collections.emptyList();
            this.extension_ = Collections.emptyList();
            this.options_ = DescriptorProtos.FileOptions.getDefaultInstance();
            this.sourceCodeInfo_ = DescriptorProtos.SourceCodeInfo.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.FileDescriptorProto.Builder create() {
            return new DescriptorProtos.FileDescriptorProto.Builder();
         }

         private void ensureDependencyIsMutable() {
            if ((this.bitField0_ & 4) != 4) {
               this.dependency_ = new LazyStringArrayList(this.dependency_);
               this.bitField0_ |= 4;
            }

         }

         private void ensureEnumTypeIsMutable() {
            if ((this.bitField0_ & 64) != 64) {
               this.enumType_ = new ArrayList(this.enumType_);
               this.bitField0_ |= 64;
            }

         }

         private void ensureExtensionIsMutable() {
            if ((this.bitField0_ & 256) != 256) {
               this.extension_ = new ArrayList(this.extension_);
               this.bitField0_ |= 256;
            }

         }

         private void ensureMessageTypeIsMutable() {
            if ((this.bitField0_ & 32) != 32) {
               this.messageType_ = new ArrayList(this.messageType_);
               this.bitField0_ |= 32;
            }

         }

         private void ensurePublicDependencyIsMutable() {
            if ((this.bitField0_ & 8) != 8) {
               this.publicDependency_ = new ArrayList(this.publicDependency_);
               this.bitField0_ |= 8;
            }

         }

         private void ensureServiceIsMutable() {
            if ((this.bitField0_ & 128) != 128) {
               this.service_ = new ArrayList(this.service_);
               this.bitField0_ |= 128;
            }

         }

         private void ensureWeakDependencyIsMutable() {
            if ((this.bitField0_ & 16) != 16) {
               this.weakDependency_ = new ArrayList(this.weakDependency_);
               this.bitField0_ |= 16;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor;
         }

         private RepeatedFieldBuilder getEnumTypeFieldBuilder() {
            if (this.enumTypeBuilder_ == null) {
               List var2 = this.enumType_;
               boolean var1;
               if ((this.bitField0_ & 64) == 64) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.enumTypeBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.enumType_ = null;
            }

            return this.enumTypeBuilder_;
         }

         private RepeatedFieldBuilder getExtensionFieldBuilder() {
            if (this.extensionBuilder_ == null) {
               List var2 = this.extension_;
               boolean var1;
               if ((this.bitField0_ & 256) == 256) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.extensionBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.extension_ = null;
            }

            return this.extensionBuilder_;
         }

         private RepeatedFieldBuilder getMessageTypeFieldBuilder() {
            if (this.messageTypeBuilder_ == null) {
               List var2 = this.messageType_;
               boolean var1;
               if ((this.bitField0_ & 32) == 32) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.messageTypeBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.messageType_ = null;
            }

            return this.messageTypeBuilder_;
         }

         private SingleFieldBuilder getOptionsFieldBuilder() {
            if (this.optionsBuilder_ == null) {
               this.optionsBuilder_ = new SingleFieldBuilder(this.options_, this.getParentForChildren(), this.isClean());
               this.options_ = null;
            }

            return this.optionsBuilder_;
         }

         private RepeatedFieldBuilder getServiceFieldBuilder() {
            if (this.serviceBuilder_ == null) {
               List var2 = this.service_;
               boolean var1;
               if ((this.bitField0_ & 128) == 128) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.serviceBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.service_ = null;
            }

            return this.serviceBuilder_;
         }

         private SingleFieldBuilder getSourceCodeInfoFieldBuilder() {
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

         public DescriptorProtos.FileDescriptorProto.Builder addAllDependency(Iterable var1) {
            this.ensureDependencyIsMutable();
            GeneratedMessage.Builder.addAll(var1, this.dependency_);
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder addAllEnumType(Iterable var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               this.ensureEnumTypeIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.enumType_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addAllExtension(Iterable var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            if (var2 == null) {
               this.ensureExtensionIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.extension_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addAllMessageType(Iterable var1) {
            RepeatedFieldBuilder var2 = this.messageTypeBuilder_;
            if (var2 == null) {
               this.ensureMessageTypeIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.messageType_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addAllPublicDependency(Iterable var1) {
            this.ensurePublicDependencyIsMutable();
            GeneratedMessage.Builder.addAll(var1, this.publicDependency_);
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder addAllService(Iterable var1) {
            RepeatedFieldBuilder var2 = this.serviceBuilder_;
            if (var2 == null) {
               this.ensureServiceIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.service_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addAllWeakDependency(Iterable var1) {
            this.ensureWeakDependencyIsMutable();
            GeneratedMessage.Builder.addAll(var1, this.weakDependency_);
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder addDependency(String var1) {
            if (var1 != null) {
               this.ensureDependencyIsMutable();
               this.dependency_.add(var1);
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addDependencyBytes(ByteString var1) {
            if (var1 != null) {
               this.ensureDependencyIsMutable();
               this.dependency_.add(var1);
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addEnumType(int var1, DescriptorProtos.EnumDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.enumTypeBuilder_;
            if (var3 == null) {
               this.ensureEnumTypeIsMutable();
               this.enumType_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addEnumType(int var1, DescriptorProtos.EnumDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.enumTypeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureEnumTypeIsMutable();
                  this.enumType_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addEnumType(DescriptorProtos.EnumDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               this.ensureEnumTypeIsMutable();
               this.enumType_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addEnumType(DescriptorProtos.EnumDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureEnumTypeIsMutable();
                  this.enumType_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.EnumDescriptorProto.Builder addEnumTypeBuilder() {
            return (DescriptorProtos.EnumDescriptorProto.Builder)this.getEnumTypeFieldBuilder().addBuilder(DescriptorProtos.EnumDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.EnumDescriptorProto.Builder addEnumTypeBuilder(int var1) {
            return (DescriptorProtos.EnumDescriptorProto.Builder)this.getEnumTypeFieldBuilder().addBuilder(var1, DescriptorProtos.EnumDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FileDescriptorProto.Builder addExtension(int var1, DescriptorProtos.FieldDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.extensionBuilder_;
            if (var3 == null) {
               this.ensureExtensionIsMutable();
               this.extension_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addExtension(int var1, DescriptorProtos.FieldDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.extensionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureExtensionIsMutable();
                  this.extension_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addExtension(DescriptorProtos.FieldDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            if (var2 == null) {
               this.ensureExtensionIsMutable();
               this.extension_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addExtension(DescriptorProtos.FieldDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureExtensionIsMutable();
                  this.extension_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.FieldDescriptorProto.Builder addExtensionBuilder() {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getExtensionFieldBuilder().addBuilder(DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FieldDescriptorProto.Builder addExtensionBuilder(int var1) {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getExtensionFieldBuilder().addBuilder(var1, DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FileDescriptorProto.Builder addMessageType(int var1, DescriptorProtos.DescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.messageTypeBuilder_;
            if (var3 == null) {
               this.ensureMessageTypeIsMutable();
               this.messageType_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addMessageType(int var1, DescriptorProtos.DescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.messageTypeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureMessageTypeIsMutable();
                  this.messageType_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addMessageType(DescriptorProtos.DescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.messageTypeBuilder_;
            if (var2 == null) {
               this.ensureMessageTypeIsMutable();
               this.messageType_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addMessageType(DescriptorProtos.DescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.messageTypeBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureMessageTypeIsMutable();
                  this.messageType_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.DescriptorProto.Builder addMessageTypeBuilder() {
            return (DescriptorProtos.DescriptorProto.Builder)this.getMessageTypeFieldBuilder().addBuilder(DescriptorProtos.DescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.DescriptorProto.Builder addMessageTypeBuilder(int var1) {
            return (DescriptorProtos.DescriptorProto.Builder)this.getMessageTypeFieldBuilder().addBuilder(var1, DescriptorProtos.DescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FileDescriptorProto.Builder addPublicDependency(int var1) {
            this.ensurePublicDependencyIsMutable();
            this.publicDependency_.add(var1);
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder addService(int var1, DescriptorProtos.ServiceDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.serviceBuilder_;
            if (var3 == null) {
               this.ensureServiceIsMutable();
               this.service_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addService(int var1, DescriptorProtos.ServiceDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.serviceBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureServiceIsMutable();
                  this.service_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addService(DescriptorProtos.ServiceDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.serviceBuilder_;
            if (var2 == null) {
               this.ensureServiceIsMutable();
               this.service_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addService(DescriptorProtos.ServiceDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.serviceBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureServiceIsMutable();
                  this.service_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder addServiceBuilder() {
            return (DescriptorProtos.ServiceDescriptorProto.Builder)this.getServiceFieldBuilder().addBuilder(DescriptorProtos.ServiceDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder addServiceBuilder(int var1) {
            return (DescriptorProtos.ServiceDescriptorProto.Builder)this.getServiceFieldBuilder().addBuilder(var1, DescriptorProtos.ServiceDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FileDescriptorProto.Builder addWeakDependency(int var1) {
            this.ensureWeakDependencyIsMutable();
            this.weakDependency_.add(var1);
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto build() {
            DescriptorProtos.FileDescriptorProto var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.FileDescriptorProto buildPartial() {
            DescriptorProtos.FileDescriptorProto var4 = new DescriptorProtos.FileDescriptorProto(this);
            int var3 = this.bitField0_;
            int var2 = 0;
            if ((var3 & 1) == 1) {
               var2 = 0 | 1;
            }

            var4.name_ = this.name_;
            int var1 = var2;
            if ((var3 & 2) == 2) {
               var1 = var2 | 2;
            }

            var4.package_ = this.package_;
            if ((this.bitField0_ & 4) == 4) {
               this.dependency_ = new UnmodifiableLazyStringList(this.dependency_);
               this.bitField0_ &= -5;
            }

            var4.dependency_ = this.dependency_;
            if ((this.bitField0_ & 8) == 8) {
               this.publicDependency_ = Collections.unmodifiableList(this.publicDependency_);
               this.bitField0_ &= -9;
            }

            var4.publicDependency_ = this.publicDependency_;
            if ((this.bitField0_ & 16) == 16) {
               this.weakDependency_ = Collections.unmodifiableList(this.weakDependency_);
               this.bitField0_ &= -17;
            }

            var4.weakDependency_ = this.weakDependency_;
            RepeatedFieldBuilder var5 = this.messageTypeBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 32) == 32) {
                  this.messageType_ = Collections.unmodifiableList(this.messageType_);
                  this.bitField0_ &= -33;
               }

               var4.messageType_ = this.messageType_;
            } else {
               var4.messageType_ = var5.build();
            }

            var5 = this.enumTypeBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 64) == 64) {
                  this.enumType_ = Collections.unmodifiableList(this.enumType_);
                  this.bitField0_ &= -65;
               }

               var4.enumType_ = this.enumType_;
            } else {
               var4.enumType_ = var5.build();
            }

            var5 = this.serviceBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 128) == 128) {
                  this.service_ = Collections.unmodifiableList(this.service_);
                  this.bitField0_ &= -129;
               }

               var4.service_ = this.service_;
            } else {
               var4.service_ = var5.build();
            }

            var5 = this.extensionBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 256) == 256) {
                  this.extension_ = Collections.unmodifiableList(this.extension_);
                  this.bitField0_ &= -257;
               }

               var4.extension_ = this.extension_;
            } else {
               var4.extension_ = var5.build();
            }

            var2 = var1;
            if ((var3 & 512) == 512) {
               var2 = var1 | 4;
            }

            SingleFieldBuilder var6 = this.optionsBuilder_;
            if (var6 == null) {
               var4.options_ = this.options_;
            } else {
               var4.options_ = (DescriptorProtos.FileOptions)var6.build();
            }

            var1 = var2;
            if ((var3 & 1024) == 1024) {
               var1 = var2 | 8;
            }

            var6 = this.sourceCodeInfoBuilder_;
            if (var6 == null) {
               var4.sourceCodeInfo_ = this.sourceCodeInfo_;
            } else {
               var4.sourceCodeInfo_ = (DescriptorProtos.SourceCodeInfo)var6.build();
            }

            var4.bitField0_ = var1;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clear() {
            super.clear();
            this.name_ = "";
            int var1 = this.bitField0_ & -2;
            this.bitField0_ = var1;
            this.package_ = "";
            this.bitField0_ = var1 & -3;
            this.dependency_ = LazyStringArrayList.EMPTY;
            this.bitField0_ &= -5;
            this.publicDependency_ = Collections.emptyList();
            this.bitField0_ &= -9;
            this.weakDependency_ = Collections.emptyList();
            this.bitField0_ &= -17;
            RepeatedFieldBuilder var2 = this.messageTypeBuilder_;
            if (var2 == null) {
               this.messageType_ = Collections.emptyList();
               this.bitField0_ &= -33;
            } else {
               var2.clear();
            }

            var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               this.enumType_ = Collections.emptyList();
               this.bitField0_ &= -65;
            } else {
               var2.clear();
            }

            var2 = this.serviceBuilder_;
            if (var2 == null) {
               this.service_ = Collections.emptyList();
               this.bitField0_ &= -129;
            } else {
               var2.clear();
            }

            var2 = this.extensionBuilder_;
            if (var2 == null) {
               this.extension_ = Collections.emptyList();
               this.bitField0_ &= -257;
            } else {
               var2.clear();
            }

            SingleFieldBuilder var3 = this.optionsBuilder_;
            if (var3 == null) {
               this.options_ = DescriptorProtos.FileOptions.getDefaultInstance();
            } else {
               var3.clear();
            }

            this.bitField0_ &= -513;
            var3 = this.sourceCodeInfoBuilder_;
            if (var3 == null) {
               this.sourceCodeInfo_ = DescriptorProtos.SourceCodeInfo.getDefaultInstance();
            } else {
               var3.clear();
            }

            this.bitField0_ &= -1025;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearDependency() {
            this.dependency_ = LazyStringArrayList.EMPTY;
            this.bitField0_ &= -5;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearEnumType() {
            RepeatedFieldBuilder var1 = this.enumTypeBuilder_;
            if (var1 == null) {
               this.enumType_ = Collections.emptyList();
               this.bitField0_ &= -65;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearExtension() {
            RepeatedFieldBuilder var1 = this.extensionBuilder_;
            if (var1 == null) {
               this.extension_ = Collections.emptyList();
               this.bitField0_ &= -257;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearMessageType() {
            RepeatedFieldBuilder var1 = this.messageTypeBuilder_;
            if (var1 == null) {
               this.messageType_ = Collections.emptyList();
               this.bitField0_ &= -33;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearName() {
            this.bitField0_ &= -2;
            this.name_ = DescriptorProtos.FileDescriptorProto.getDefaultInstance().getName();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            if (var1 == null) {
               this.options_ = DescriptorProtos.FileOptions.getDefaultInstance();
               this.onChanged();
            } else {
               var1.clear();
            }

            this.bitField0_ &= -513;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearPackage() {
            this.bitField0_ &= -3;
            this.package_ = DescriptorProtos.FileDescriptorProto.getDefaultInstance().getPackage();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearPublicDependency() {
            this.publicDependency_ = Collections.emptyList();
            this.bitField0_ &= -9;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearService() {
            RepeatedFieldBuilder var1 = this.serviceBuilder_;
            if (var1 == null) {
               this.service_ = Collections.emptyList();
               this.bitField0_ &= -129;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearSourceCodeInfo() {
            SingleFieldBuilder var1 = this.sourceCodeInfoBuilder_;
            if (var1 == null) {
               this.sourceCodeInfo_ = DescriptorProtos.SourceCodeInfo.getDefaultInstance();
               this.onChanged();
            } else {
               var1.clear();
            }

            this.bitField0_ &= -1025;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clearWeakDependency() {
            this.weakDependency_ = Collections.emptyList();
            this.bitField0_ &= -17;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.FileDescriptorProto getDefaultInstanceForType() {
            return DescriptorProtos.FileDescriptorProto.getDefaultInstance();
         }

         public String getDependency(int var1) {
            return (String)this.dependency_.get(var1);
         }

         public ByteString getDependencyBytes(int var1) {
            return this.dependency_.getByteString(var1);
         }

         public int getDependencyCount() {
            return this.dependency_.size();
         }

         public List getDependencyList() {
            return Collections.unmodifiableList(this.dependency_);
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor;
         }

         public DescriptorProtos.EnumDescriptorProto getEnumType(int var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            return var2 == null ? (DescriptorProtos.EnumDescriptorProto)this.enumType_.get(var1) : (DescriptorProtos.EnumDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.EnumDescriptorProto.Builder getEnumTypeBuilder(int var1) {
            return (DescriptorProtos.EnumDescriptorProto.Builder)this.getEnumTypeFieldBuilder().getBuilder(var1);
         }

         public List getEnumTypeBuilderList() {
            return this.getEnumTypeFieldBuilder().getBuilderList();
         }

         public int getEnumTypeCount() {
            RepeatedFieldBuilder var1 = this.enumTypeBuilder_;
            return var1 == null ? this.enumType_.size() : var1.getCount();
         }

         public List getEnumTypeList() {
            RepeatedFieldBuilder var1 = this.enumTypeBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.enumType_) : var1.getMessageList();
         }

         public DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            return var2 == null ? (DescriptorProtos.EnumDescriptorProtoOrBuilder)this.enumType_.get(var1) : (DescriptorProtos.EnumDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getEnumTypeOrBuilderList() {
            RepeatedFieldBuilder var1 = this.enumTypeBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.enumType_);
         }

         public DescriptorProtos.FieldDescriptorProto getExtension(int var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            return var2 == null ? (DescriptorProtos.FieldDescriptorProto)this.extension_.get(var1) : (DescriptorProtos.FieldDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.FieldDescriptorProto.Builder getExtensionBuilder(int var1) {
            return (DescriptorProtos.FieldDescriptorProto.Builder)this.getExtensionFieldBuilder().getBuilder(var1);
         }

         public List getExtensionBuilderList() {
            return this.getExtensionFieldBuilder().getBuilderList();
         }

         public int getExtensionCount() {
            RepeatedFieldBuilder var1 = this.extensionBuilder_;
            return var1 == null ? this.extension_.size() : var1.getCount();
         }

         public List getExtensionList() {
            RepeatedFieldBuilder var1 = this.extensionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.extension_) : var1.getMessageList();
         }

         public DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            return var2 == null ? (DescriptorProtos.FieldDescriptorProtoOrBuilder)this.extension_.get(var1) : (DescriptorProtos.FieldDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getExtensionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.extensionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.extension_);
         }

         public DescriptorProtos.DescriptorProto getMessageType(int var1) {
            RepeatedFieldBuilder var2 = this.messageTypeBuilder_;
            return var2 == null ? (DescriptorProtos.DescriptorProto)this.messageType_.get(var1) : (DescriptorProtos.DescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.DescriptorProto.Builder getMessageTypeBuilder(int var1) {
            return (DescriptorProtos.DescriptorProto.Builder)this.getMessageTypeFieldBuilder().getBuilder(var1);
         }

         public List getMessageTypeBuilderList() {
            return this.getMessageTypeFieldBuilder().getBuilderList();
         }

         public int getMessageTypeCount() {
            RepeatedFieldBuilder var1 = this.messageTypeBuilder_;
            return var1 == null ? this.messageType_.size() : var1.getCount();
         }

         public List getMessageTypeList() {
            RepeatedFieldBuilder var1 = this.messageTypeBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.messageType_) : var1.getMessageList();
         }

         public DescriptorProtos.DescriptorProtoOrBuilder getMessageTypeOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.messageTypeBuilder_;
            return var2 == null ? (DescriptorProtos.DescriptorProtoOrBuilder)this.messageType_.get(var1) : (DescriptorProtos.DescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getMessageTypeOrBuilderList() {
            RepeatedFieldBuilder var1 = this.messageTypeBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.messageType_);
         }

         public String getName() {
            Object var1 = this.name_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.name_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getNameBytes() {
            Object var1 = this.name_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.name_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.FileOptions getOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return var1 == null ? this.options_ : (DescriptorProtos.FileOptions)var1.getMessage();
         }

         public DescriptorProtos.FileOptions.Builder getOptionsBuilder() {
            this.bitField0_ |= 512;
            this.onChanged();
            return (DescriptorProtos.FileOptions.Builder)this.getOptionsFieldBuilder().getBuilder();
         }

         public DescriptorProtos.FileOptionsOrBuilder getOptionsOrBuilder() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return (DescriptorProtos.FileOptionsOrBuilder)(var1 != null ? (DescriptorProtos.FileOptionsOrBuilder)var1.getMessageOrBuilder() : this.options_);
         }

         public String getPackage() {
            Object var1 = this.package_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.package_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getPackageBytes() {
            Object var1 = this.package_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.package_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public int getPublicDependency(int var1) {
            return (Integer)this.publicDependency_.get(var1);
         }

         public int getPublicDependencyCount() {
            return this.publicDependency_.size();
         }

         public List getPublicDependencyList() {
            return Collections.unmodifiableList(this.publicDependency_);
         }

         public DescriptorProtos.ServiceDescriptorProto getService(int var1) {
            RepeatedFieldBuilder var2 = this.serviceBuilder_;
            return var2 == null ? (DescriptorProtos.ServiceDescriptorProto)this.service_.get(var1) : (DescriptorProtos.ServiceDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder getServiceBuilder(int var1) {
            return (DescriptorProtos.ServiceDescriptorProto.Builder)this.getServiceFieldBuilder().getBuilder(var1);
         }

         public List getServiceBuilderList() {
            return this.getServiceFieldBuilder().getBuilderList();
         }

         public int getServiceCount() {
            RepeatedFieldBuilder var1 = this.serviceBuilder_;
            return var1 == null ? this.service_.size() : var1.getCount();
         }

         public List getServiceList() {
            RepeatedFieldBuilder var1 = this.serviceBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.service_) : var1.getMessageList();
         }

         public DescriptorProtos.ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.serviceBuilder_;
            return var2 == null ? (DescriptorProtos.ServiceDescriptorProtoOrBuilder)this.service_.get(var1) : (DescriptorProtos.ServiceDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getServiceOrBuilderList() {
            RepeatedFieldBuilder var1 = this.serviceBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.service_);
         }

         public DescriptorProtos.SourceCodeInfo getSourceCodeInfo() {
            SingleFieldBuilder var1 = this.sourceCodeInfoBuilder_;
            return var1 == null ? this.sourceCodeInfo_ : (DescriptorProtos.SourceCodeInfo)var1.getMessage();
         }

         public DescriptorProtos.SourceCodeInfo.Builder getSourceCodeInfoBuilder() {
            this.bitField0_ |= 1024;
            this.onChanged();
            return (DescriptorProtos.SourceCodeInfo.Builder)this.getSourceCodeInfoFieldBuilder().getBuilder();
         }

         public DescriptorProtos.SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder() {
            SingleFieldBuilder var1 = this.sourceCodeInfoBuilder_;
            return (DescriptorProtos.SourceCodeInfoOrBuilder)(var1 != null ? (DescriptorProtos.SourceCodeInfoOrBuilder)var1.getMessageOrBuilder() : this.sourceCodeInfo_);
         }

         public int getWeakDependency(int var1) {
            return (Integer)this.weakDependency_.get(var1);
         }

         public int getWeakDependencyCount() {
            return this.weakDependency_.size();
         }

         public List getWeakDependencyList() {
            return Collections.unmodifiableList(this.weakDependency_);
         }

         public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasOptions() {
            return (this.bitField0_ & 512) == 512;
         }

         public boolean hasPackage() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasSourceCodeInfo() {
            return (this.bitField0_ & 1024) == 1024;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FileDescriptorProto.class, DescriptorProtos.FileDescriptorProto.Builder.class);
         }

         public final boolean isInitialized() {
            int var1;
            for(var1 = 0; var1 < this.getMessageTypeCount(); ++var1) {
               if (!this.getMessageType(var1).isInitialized()) {
                  return false;
               }
            }

            for(var1 = 0; var1 < this.getEnumTypeCount(); ++var1) {
               if (!this.getEnumType(var1).isInitialized()) {
                  return false;
               }
            }

            for(var1 = 0; var1 < this.getServiceCount(); ++var1) {
               if (!this.getService(var1).isInitialized()) {
                  return false;
               }
            }

            for(var1 = 0; var1 < this.getExtensionCount(); ++var1) {
               if (!this.getExtension(var1).isInitialized()) {
                  return false;
               }
            }

            if (this.hasOptions() && !this.getOptions().isInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.FileDescriptorProto var3 = (DescriptorProtos.FileDescriptorProto)var4;

            DescriptorProtos.FileDescriptorProto var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.FileDescriptorProto)DescriptorProtos.FileDescriptorProto.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.FileDescriptorProto)var4;

                  try {
                     var21 = (DescriptorProtos.FileDescriptorProto)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder mergeFrom(DescriptorProtos.FileDescriptorProto var1) {
            if (var1 == DescriptorProtos.FileDescriptorProto.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasName()) {
                  this.bitField0_ |= 1;
                  this.name_ = var1.name_;
                  this.onChanged();
               }

               if (var1.hasPackage()) {
                  this.bitField0_ |= 2;
                  this.package_ = var1.package_;
                  this.onChanged();
               }

               if (!var1.dependency_.isEmpty()) {
                  if (this.dependency_.isEmpty()) {
                     this.dependency_ = var1.dependency_;
                     this.bitField0_ &= -5;
                  } else {
                     this.ensureDependencyIsMutable();
                     this.dependency_.addAll(var1.dependency_);
                  }

                  this.onChanged();
               }

               if (!var1.publicDependency_.isEmpty()) {
                  if (this.publicDependency_.isEmpty()) {
                     this.publicDependency_ = var1.publicDependency_;
                     this.bitField0_ &= -9;
                  } else {
                     this.ensurePublicDependencyIsMutable();
                     this.publicDependency_.addAll(var1.publicDependency_);
                  }

                  this.onChanged();
               }

               if (!var1.weakDependency_.isEmpty()) {
                  if (this.weakDependency_.isEmpty()) {
                     this.weakDependency_ = var1.weakDependency_;
                     this.bitField0_ &= -17;
                  } else {
                     this.ensureWeakDependencyIsMutable();
                     this.weakDependency_.addAll(var1.weakDependency_);
                  }

                  this.onChanged();
               }

               RepeatedFieldBuilder var2 = this.messageTypeBuilder_;
               Object var3 = null;
               if (var2 == null) {
                  if (!var1.messageType_.isEmpty()) {
                     if (this.messageType_.isEmpty()) {
                        this.messageType_ = var1.messageType_;
                        this.bitField0_ &= -33;
                     } else {
                        this.ensureMessageTypeIsMutable();
                        this.messageType_.addAll(var1.messageType_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.messageType_.isEmpty()) {
                  if (this.messageTypeBuilder_.isEmpty()) {
                     this.messageTypeBuilder_.dispose();
                     this.messageTypeBuilder_ = null;
                     this.messageType_ = var1.messageType_;
                     this.bitField0_ &= -33;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getMessageTypeFieldBuilder();
                     } else {
                        var2 = null;
                     }

                     this.messageTypeBuilder_ = var2;
                  } else {
                     this.messageTypeBuilder_.addAllMessages(var1.messageType_);
                  }
               }

               if (this.enumTypeBuilder_ == null) {
                  if (!var1.enumType_.isEmpty()) {
                     if (this.enumType_.isEmpty()) {
                        this.enumType_ = var1.enumType_;
                        this.bitField0_ &= -65;
                     } else {
                        this.ensureEnumTypeIsMutable();
                        this.enumType_.addAll(var1.enumType_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.enumType_.isEmpty()) {
                  if (this.enumTypeBuilder_.isEmpty()) {
                     this.enumTypeBuilder_.dispose();
                     this.enumTypeBuilder_ = null;
                     this.enumType_ = var1.enumType_;
                     this.bitField0_ &= -65;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getEnumTypeFieldBuilder();
                     } else {
                        var2 = null;
                     }

                     this.enumTypeBuilder_ = var2;
                  } else {
                     this.enumTypeBuilder_.addAllMessages(var1.enumType_);
                  }
               }

               if (this.serviceBuilder_ == null) {
                  if (!var1.service_.isEmpty()) {
                     if (this.service_.isEmpty()) {
                        this.service_ = var1.service_;
                        this.bitField0_ &= -129;
                     } else {
                        this.ensureServiceIsMutable();
                        this.service_.addAll(var1.service_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.service_.isEmpty()) {
                  if (this.serviceBuilder_.isEmpty()) {
                     this.serviceBuilder_.dispose();
                     this.serviceBuilder_ = null;
                     this.service_ = var1.service_;
                     this.bitField0_ &= -129;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getServiceFieldBuilder();
                     } else {
                        var2 = null;
                     }

                     this.serviceBuilder_ = var2;
                  } else {
                     this.serviceBuilder_.addAllMessages(var1.service_);
                  }
               }

               if (this.extensionBuilder_ == null) {
                  if (!var1.extension_.isEmpty()) {
                     if (this.extension_.isEmpty()) {
                        this.extension_ = var1.extension_;
                        this.bitField0_ &= -257;
                     } else {
                        this.ensureExtensionIsMutable();
                        this.extension_.addAll(var1.extension_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.extension_.isEmpty()) {
                  if (this.extensionBuilder_.isEmpty()) {
                     this.extensionBuilder_.dispose();
                     this.extensionBuilder_ = null;
                     this.extension_ = var1.extension_;
                     this.bitField0_ &= -257;
                     var2 = (RepeatedFieldBuilder)var3;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getExtensionFieldBuilder();
                     }

                     this.extensionBuilder_ = var2;
                  } else {
                     this.extensionBuilder_.addAllMessages(var1.extension_);
                  }
               }

               if (var1.hasOptions()) {
                  this.mergeOptions(var1.getOptions());
               }

               if (var1.hasSourceCodeInfo()) {
                  this.mergeSourceCodeInfo(var1.getSourceCodeInfo());
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.FileDescriptorProto) {
               return this.mergeFrom((DescriptorProtos.FileDescriptorProto)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder mergeOptions(DescriptorProtos.FileOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if ((this.bitField0_ & 512) == 512 && this.options_ != DescriptorProtos.FileOptions.getDefaultInstance()) {
                  this.options_ = DescriptorProtos.FileOptions.newBuilder(this.options_).mergeFrom(var1).buildPartial();
               } else {
                  this.options_ = var1;
               }

               this.onChanged();
            } else {
               var2.mergeFrom(var1);
            }

            this.bitField0_ |= 512;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder mergeSourceCodeInfo(DescriptorProtos.SourceCodeInfo var1) {
            SingleFieldBuilder var2 = this.sourceCodeInfoBuilder_;
            if (var2 == null) {
               if ((this.bitField0_ & 1024) == 1024 && this.sourceCodeInfo_ != DescriptorProtos.SourceCodeInfo.getDefaultInstance()) {
                  this.sourceCodeInfo_ = DescriptorProtos.SourceCodeInfo.newBuilder(this.sourceCodeInfo_).mergeFrom(var1).buildPartial();
               } else {
                  this.sourceCodeInfo_ = var1;
               }

               this.onChanged();
            } else {
               var2.mergeFrom(var1);
            }

            this.bitField0_ |= 1024;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder removeEnumType(int var1) {
            RepeatedFieldBuilder var2 = this.enumTypeBuilder_;
            if (var2 == null) {
               this.ensureEnumTypeIsMutable();
               this.enumType_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder removeExtension(int var1) {
            RepeatedFieldBuilder var2 = this.extensionBuilder_;
            if (var2 == null) {
               this.ensureExtensionIsMutable();
               this.extension_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder removeMessageType(int var1) {
            RepeatedFieldBuilder var2 = this.messageTypeBuilder_;
            if (var2 == null) {
               this.ensureMessageTypeIsMutable();
               this.messageType_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder removeService(int var1) {
            RepeatedFieldBuilder var2 = this.serviceBuilder_;
            if (var2 == null) {
               this.ensureServiceIsMutable();
               this.service_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setDependency(int var1, String var2) {
            if (var2 != null) {
               this.ensureDependencyIsMutable();
               this.dependency_.set(var1, var2);
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setEnumType(int var1, DescriptorProtos.EnumDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.enumTypeBuilder_;
            if (var3 == null) {
               this.ensureEnumTypeIsMutable();
               this.enumType_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setEnumType(int var1, DescriptorProtos.EnumDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.enumTypeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureEnumTypeIsMutable();
                  this.enumType_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setExtension(int var1, DescriptorProtos.FieldDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.extensionBuilder_;
            if (var3 == null) {
               this.ensureExtensionIsMutable();
               this.extension_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setExtension(int var1, DescriptorProtos.FieldDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.extensionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureExtensionIsMutable();
                  this.extension_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setMessageType(int var1, DescriptorProtos.DescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.messageTypeBuilder_;
            if (var3 == null) {
               this.ensureMessageTypeIsMutable();
               this.messageType_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setMessageType(int var1, DescriptorProtos.DescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.messageTypeBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureMessageTypeIsMutable();
                  this.messageType_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setName(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setNameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setOptions(DescriptorProtos.FileOptions.Builder var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = var1.build();
               this.onChanged();
            } else {
               var2.setMessage(var1.build());
            }

            this.bitField0_ |= 512;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder setOptions(DescriptorProtos.FileOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if (var1 == null) {
                  throw null;
               }

               this.options_ = var1;
               this.onChanged();
            } else {
               var2.setMessage(var1);
            }

            this.bitField0_ |= 512;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder setPackage(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 2;
               this.package_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setPackageBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 2;
               this.package_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setPublicDependency(int var1, int var2) {
            this.ensurePublicDependencyIsMutable();
            this.publicDependency_.set(var1, var2);
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder setService(int var1, DescriptorProtos.ServiceDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.serviceBuilder_;
            if (var3 == null) {
               this.ensureServiceIsMutable();
               this.service_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setService(int var1, DescriptorProtos.ServiceDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.serviceBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureServiceIsMutable();
                  this.service_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder setSourceCodeInfo(DescriptorProtos.SourceCodeInfo.Builder var1) {
            SingleFieldBuilder var2 = this.sourceCodeInfoBuilder_;
            if (var2 == null) {
               this.sourceCodeInfo_ = var1.build();
               this.onChanged();
            } else {
               var2.setMessage(var1.build());
            }

            this.bitField0_ |= 1024;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder setSourceCodeInfo(DescriptorProtos.SourceCodeInfo var1) {
            SingleFieldBuilder var2 = this.sourceCodeInfoBuilder_;
            if (var2 == null) {
               if (var1 == null) {
                  throw null;
               }

               this.sourceCodeInfo_ = var1;
               this.onChanged();
            } else {
               var2.setMessage(var1);
            }

            this.bitField0_ |= 1024;
            return this;
         }

         public DescriptorProtos.FileDescriptorProto.Builder setWeakDependency(int var1, int var2) {
            this.ensureWeakDependencyIsMutable();
            this.weakDependency_.set(var1, var2);
            this.onChanged();
            return this;
         }
      }
   }

   public interface FileDescriptorProtoOrBuilder extends MessageOrBuilder {
      String getDependency(int var1);

      ByteString getDependencyBytes(int var1);

      int getDependencyCount();

      List getDependencyList();

      DescriptorProtos.EnumDescriptorProto getEnumType(int var1);

      int getEnumTypeCount();

      List getEnumTypeList();

      DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(int var1);

      List getEnumTypeOrBuilderList();

      DescriptorProtos.FieldDescriptorProto getExtension(int var1);

      int getExtensionCount();

      List getExtensionList();

      DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(int var1);

      List getExtensionOrBuilderList();

      DescriptorProtos.DescriptorProto getMessageType(int var1);

      int getMessageTypeCount();

      List getMessageTypeList();

      DescriptorProtos.DescriptorProtoOrBuilder getMessageTypeOrBuilder(int var1);

      List getMessageTypeOrBuilderList();

      String getName();

      ByteString getNameBytes();

      DescriptorProtos.FileOptions getOptions();

      DescriptorProtos.FileOptionsOrBuilder getOptionsOrBuilder();

      String getPackage();

      ByteString getPackageBytes();

      int getPublicDependency(int var1);

      int getPublicDependencyCount();

      List getPublicDependencyList();

      DescriptorProtos.ServiceDescriptorProto getService(int var1);

      int getServiceCount();

      List getServiceList();

      DescriptorProtos.ServiceDescriptorProtoOrBuilder getServiceOrBuilder(int var1);

      List getServiceOrBuilderList();

      DescriptorProtos.SourceCodeInfo getSourceCodeInfo();

      DescriptorProtos.SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder();

      int getWeakDependency(int var1);

      int getWeakDependencyCount();

      List getWeakDependencyList();

      boolean hasName();

      boolean hasOptions();

      boolean hasPackage();

      boolean hasSourceCodeInfo();
   }

   public static final class FileDescriptorSet extends GeneratedMessage implements DescriptorProtos.FileDescriptorSetOrBuilder {
      public static final int FILE_FIELD_NUMBER = 1;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.FileDescriptorSet parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.FileDescriptorSet(var1, var2);
         }
      };
      private static final DescriptorProtos.FileDescriptorSet defaultInstance;
      private static final long serialVersionUID = 0L;
      private List file_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.FileDescriptorSet var0 = new DescriptorProtos.FileDescriptorSet(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private FileDescriptorSet(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      FileDescriptorSet(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private FileDescriptorSet(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      FileDescriptorSet(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private FileDescriptorSet(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.FileDescriptorSet getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor;
      }

      private void initFields() {
         this.file_ = Collections.emptyList();
      }

      public static DescriptorProtos.FileDescriptorSet.Builder newBuilder() {
         return DescriptorProtos.FileDescriptorSet.Builder.create();
      }

      public static DescriptorProtos.FileDescriptorSet.Builder newBuilder(DescriptorProtos.FileDescriptorSet var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorSet parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorSet parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.FileDescriptorSet parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorSet parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileDescriptorSet parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorSet parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileDescriptorSet parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorSet parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileDescriptorSet parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileDescriptorSet parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileDescriptorSet)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.FileDescriptorSet getDefaultInstanceForType() {
         return defaultInstance;
      }

      public DescriptorProtos.FileDescriptorProto getFile(int var1) {
         return (DescriptorProtos.FileDescriptorProto)this.file_.get(var1);
      }

      public int getFileCount() {
         return this.file_.size();
      }

      public List getFileList() {
         return this.file_;
      }

      public DescriptorProtos.FileDescriptorProtoOrBuilder getFileOrBuilder(int var1) {
         return (DescriptorProtos.FileDescriptorProtoOrBuilder)this.file_.get(var1);
      }

      public List getFileOrBuilderList() {
         return this.file_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;

            for(var1 = 0; var1 < this.file_.size(); ++var1) {
               var2 += CodedOutputStream.computeMessageSize(1, (MessageLite)this.file_.get(var1));
            }

            var1 = var2 + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FileDescriptorSet.class, DescriptorProtos.FileDescriptorSet.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getFileCount(); ++var3) {
               if (!this.getFile(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public DescriptorProtos.FileDescriptorSet.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.FileDescriptorSet.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.FileDescriptorSet.Builder(var1);
      }

      public DescriptorProtos.FileDescriptorSet.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();

         for(int var2 = 0; var2 < this.file_.size(); ++var2) {
            var1.writeMessage(1, (MessageLite)this.file_.get(var2));
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.FileDescriptorSetOrBuilder {
         private int bitField0_;
         private RepeatedFieldBuilder fileBuilder_;
         private List file_;

         private Builder() {
            this.file_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.file_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.FileDescriptorSet.Builder create() {
            return new DescriptorProtos.FileDescriptorSet.Builder();
         }

         private void ensureFileIsMutable() {
            if ((this.bitField0_ & 1) != 1) {
               this.file_ = new ArrayList(this.file_);
               this.bitField0_ |= 1;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor;
         }

         private RepeatedFieldBuilder getFileFieldBuilder() {
            if (this.fileBuilder_ == null) {
               List var3 = this.file_;
               int var1 = this.bitField0_;
               boolean var2 = true;
               if ((var1 & 1) != 1) {
                  var2 = false;
               }

               this.fileBuilder_ = new RepeatedFieldBuilder(var3, var2, this.getParentForChildren(), this.isClean());
               this.file_ = null;
            }

            return this.fileBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getFileFieldBuilder();
            }

         }

         public DescriptorProtos.FileDescriptorSet.Builder addAllFile(Iterable var1) {
            RepeatedFieldBuilder var2 = this.fileBuilder_;
            if (var2 == null) {
               this.ensureFileIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.file_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder addFile(int var1, DescriptorProtos.FileDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.fileBuilder_;
            if (var3 == null) {
               this.ensureFileIsMutable();
               this.file_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder addFile(int var1, DescriptorProtos.FileDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.fileBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureFileIsMutable();
                  this.file_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder addFile(DescriptorProtos.FileDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.fileBuilder_;
            if (var2 == null) {
               this.ensureFileIsMutable();
               this.file_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder addFile(DescriptorProtos.FileDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.fileBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureFileIsMutable();
                  this.file_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorProto.Builder addFileBuilder() {
            return (DescriptorProtos.FileDescriptorProto.Builder)this.getFileFieldBuilder().addBuilder(DescriptorProtos.FileDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FileDescriptorProto.Builder addFileBuilder(int var1) {
            return (DescriptorProtos.FileDescriptorProto.Builder)this.getFileFieldBuilder().addBuilder(var1, DescriptorProtos.FileDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.FileDescriptorSet build() {
            DescriptorProtos.FileDescriptorSet var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.FileDescriptorSet buildPartial() {
            DescriptorProtos.FileDescriptorSet var2 = new DescriptorProtos.FileDescriptorSet(this);
            int var1 = this.bitField0_;
            RepeatedFieldBuilder var3 = this.fileBuilder_;
            if (var3 == null) {
               if ((this.bitField0_ & 1) == 1) {
                  this.file_ = Collections.unmodifiableList(this.file_);
                  this.bitField0_ &= -2;
               }

               var2.file_ = this.file_;
            } else {
               var2.file_ = var3.build();
            }

            this.onBuilt();
            return var2;
         }

         public DescriptorProtos.FileDescriptorSet.Builder clear() {
            super.clear();
            RepeatedFieldBuilder var1 = this.fileBuilder_;
            if (var1 == null) {
               this.file_ = Collections.emptyList();
               this.bitField0_ &= -2;
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder clearFile() {
            RepeatedFieldBuilder var1 = this.fileBuilder_;
            if (var1 == null) {
               this.file_ = Collections.emptyList();
               this.bitField0_ &= -2;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.FileDescriptorSet getDefaultInstanceForType() {
            return DescriptorProtos.FileDescriptorSet.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor;
         }

         public DescriptorProtos.FileDescriptorProto getFile(int var1) {
            RepeatedFieldBuilder var2 = this.fileBuilder_;
            return var2 == null ? (DescriptorProtos.FileDescriptorProto)this.file_.get(var1) : (DescriptorProtos.FileDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.FileDescriptorProto.Builder getFileBuilder(int var1) {
            return (DescriptorProtos.FileDescriptorProto.Builder)this.getFileFieldBuilder().getBuilder(var1);
         }

         public List getFileBuilderList() {
            return this.getFileFieldBuilder().getBuilderList();
         }

         public int getFileCount() {
            RepeatedFieldBuilder var1 = this.fileBuilder_;
            return var1 == null ? this.file_.size() : var1.getCount();
         }

         public List getFileList() {
            RepeatedFieldBuilder var1 = this.fileBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.file_) : var1.getMessageList();
         }

         public DescriptorProtos.FileDescriptorProtoOrBuilder getFileOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.fileBuilder_;
            return var2 == null ? (DescriptorProtos.FileDescriptorProtoOrBuilder)this.file_.get(var1) : (DescriptorProtos.FileDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getFileOrBuilderList() {
            RepeatedFieldBuilder var1 = this.fileBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.file_);
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FileDescriptorSet.class, DescriptorProtos.FileDescriptorSet.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getFileCount(); ++var1) {
               if (!this.getFile(var1).isInitialized()) {
                  return false;
               }
            }

            return true;
         }

         public DescriptorProtos.FileDescriptorSet.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.FileDescriptorSet var3 = (DescriptorProtos.FileDescriptorSet)var4;

            DescriptorProtos.FileDescriptorSet var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.FileDescriptorSet)DescriptorProtos.FileDescriptorSet.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.FileDescriptorSet)var4;

                  try {
                     var21 = (DescriptorProtos.FileDescriptorSet)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.FileDescriptorSet.Builder mergeFrom(DescriptorProtos.FileDescriptorSet var1) {
            if (var1 == DescriptorProtos.FileDescriptorSet.getDefaultInstance()) {
               return this;
            } else {
               if (this.fileBuilder_ == null) {
                  if (!var1.file_.isEmpty()) {
                     if (this.file_.isEmpty()) {
                        this.file_ = var1.file_;
                        this.bitField0_ &= -2;
                     } else {
                        this.ensureFileIsMutable();
                        this.file_.addAll(var1.file_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.file_.isEmpty()) {
                  if (this.fileBuilder_.isEmpty()) {
                     this.fileBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.fileBuilder_ = null;
                     this.file_ = var1.file_;
                     this.bitField0_ &= -2;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getFileFieldBuilder();
                     }

                     this.fileBuilder_ = var2;
                  } else {
                     this.fileBuilder_.addAllMessages(var1.file_);
                  }
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.FileDescriptorSet) {
               return this.mergeFrom((DescriptorProtos.FileDescriptorSet)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder removeFile(int var1) {
            RepeatedFieldBuilder var2 = this.fileBuilder_;
            if (var2 == null) {
               this.ensureFileIsMutable();
               this.file_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder setFile(int var1, DescriptorProtos.FileDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.fileBuilder_;
            if (var3 == null) {
               this.ensureFileIsMutable();
               this.file_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileDescriptorSet.Builder setFile(int var1, DescriptorProtos.FileDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.fileBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureFileIsMutable();
                  this.file_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }
   }

   public interface FileDescriptorSetOrBuilder extends MessageOrBuilder {
      DescriptorProtos.FileDescriptorProto getFile(int var1);

      int getFileCount();

      List getFileList();

      DescriptorProtos.FileDescriptorProtoOrBuilder getFileOrBuilder(int var1);

      List getFileOrBuilderList();
   }

   public static final class FileOptions extends GeneratedMessage.ExtendableMessage implements DescriptorProtos.FileOptionsOrBuilder {
      public static final int CC_GENERIC_SERVICES_FIELD_NUMBER = 16;
      public static final int GO_PACKAGE_FIELD_NUMBER = 11;
      public static final int JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER = 20;
      public static final int JAVA_GENERIC_SERVICES_FIELD_NUMBER = 17;
      public static final int JAVA_MULTIPLE_FILES_FIELD_NUMBER = 10;
      public static final int JAVA_OUTER_CLASSNAME_FIELD_NUMBER = 8;
      public static final int JAVA_PACKAGE_FIELD_NUMBER = 1;
      public static final int OPTIMIZE_FOR_FIELD_NUMBER = 9;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.FileOptions parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.FileOptions(var1, var2);
         }
      };
      public static final int PY_GENERIC_SERVICES_FIELD_NUMBER = 18;
      public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
      private static final DescriptorProtos.FileOptions defaultInstance;
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
      private DescriptorProtos.FileOptions.OptimizeMode optimizeFor_;
      private boolean pyGenericServices_;
      private List uninterpretedOption_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.FileOptions var0 = new DescriptorProtos.FileOptions(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private FileOptions(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      FileOptions(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private FileOptions(GeneratedMessage.ExtendableBuilder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      FileOptions(GeneratedMessage.ExtendableBuilder var1, Object var2) {
         this(var1);
      }

      private FileOptions(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.FileOptions getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor;
      }

      private void initFields() {
         this.javaPackage_ = "";
         this.javaOuterClassname_ = "";
         this.javaMultipleFiles_ = false;
         this.javaGenerateEqualsAndHash_ = false;
         this.optimizeFor_ = DescriptorProtos.FileOptions.OptimizeMode.SPEED;
         this.goPackage_ = "";
         this.ccGenericServices_ = false;
         this.javaGenericServices_ = false;
         this.pyGenericServices_ = false;
         this.uninterpretedOption_ = Collections.emptyList();
      }

      public static DescriptorProtos.FileOptions.Builder newBuilder() {
         return DescriptorProtos.FileOptions.Builder.create();
      }

      public static DescriptorProtos.FileOptions.Builder newBuilder(DescriptorProtos.FileOptions var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.FileOptions parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FileOptions)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.FileOptions parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileOptions)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.FileOptions parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileOptions parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileOptions parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.FileOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileOptions parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileOptions parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.FileOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileOptions parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.FileOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.FileOptions parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.FileOptions parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.FileOptions)PARSER.parseFrom(var0, var1);
      }

      public boolean getCcGenericServices() {
         return this.ccGenericServices_;
      }

      public DescriptorProtos.FileOptions getDefaultInstanceForType() {
         return defaultInstance;
      }

      public String getGoPackage() {
         Object var1 = this.goPackage_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.goPackage_ = var2;
            }

            return var2;
         }
      }

      public ByteString getGoPackageBytes() {
         Object var1 = this.goPackage_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.goPackage_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public boolean getJavaGenerateEqualsAndHash() {
         return this.javaGenerateEqualsAndHash_;
      }

      public boolean getJavaGenericServices() {
         return this.javaGenericServices_;
      }

      public boolean getJavaMultipleFiles() {
         return this.javaMultipleFiles_;
      }

      public String getJavaOuterClassname() {
         Object var1 = this.javaOuterClassname_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.javaOuterClassname_ = var2;
            }

            return var2;
         }
      }

      public ByteString getJavaOuterClassnameBytes() {
         Object var1 = this.javaOuterClassname_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.javaOuterClassname_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public String getJavaPackage() {
         Object var1 = this.javaPackage_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.javaPackage_ = var2;
            }

            return var2;
         }
      }

      public ByteString getJavaPackageBytes() {
         Object var1 = this.javaPackage_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.javaPackage_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.FileOptions.OptimizeMode getOptimizeFor() {
         return this.optimizeFor_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public boolean getPyGenericServices() {
         return this.pyGenericServices_;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var2 = 0 + CodedOutputStream.computeBytesSize(1, this.getJavaPackageBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 2) == 2) {
               var1 = var2 + CodedOutputStream.computeBytesSize(8, this.getJavaOuterClassnameBytes());
            }

            var2 = var1;
            if ((this.bitField0_ & 16) == 16) {
               var2 = var1 + CodedOutputStream.computeEnumSize(9, this.optimizeFor_.getNumber());
            }

            var1 = var2;
            if ((this.bitField0_ & 4) == 4) {
               var1 = var2 + CodedOutputStream.computeBoolSize(10, this.javaMultipleFiles_);
            }

            var2 = var1;
            if ((this.bitField0_ & 32) == 32) {
               var2 = var1 + CodedOutputStream.computeBytesSize(11, this.getGoPackageBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 64) == 64) {
               var1 = var2 + CodedOutputStream.computeBoolSize(16, this.ccGenericServices_);
            }

            var2 = var1;
            if ((this.bitField0_ & 128) == 128) {
               var2 = var1 + CodedOutputStream.computeBoolSize(17, this.javaGenericServices_);
            }

            int var3 = var2;
            if ((this.bitField0_ & 256) == 256) {
               var3 = var2 + CodedOutputStream.computeBoolSize(18, this.pyGenericServices_);
            }

            var1 = var3;
            if ((this.bitField0_ & 8) == 8) {
               var1 = var3 + CodedOutputStream.computeBoolSize(20, this.javaGenerateEqualsAndHash_);
            }

            for(var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(999, (MessageLite)this.uninterpretedOption_.get(var2));
            }

            var1 = var1 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
         return (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1);
      }

      public int getUninterpretedOptionCount() {
         return this.uninterpretedOption_.size();
      }

      public List getUninterpretedOptionList() {
         return this.uninterpretedOption_;
      }

      public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
         return (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1);
      }

      public List getUninterpretedOptionOrBuilderList() {
         return this.uninterpretedOption_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasCcGenericServices() {
         return (this.bitField0_ & 64) == 64;
      }

      public boolean hasGoPackage() {
         return (this.bitField0_ & 32) == 32;
      }

      public boolean hasJavaGenerateEqualsAndHash() {
         return (this.bitField0_ & 8) == 8;
      }

      public boolean hasJavaGenericServices() {
         return (this.bitField0_ & 128) == 128;
      }

      public boolean hasJavaMultipleFiles() {
         return (this.bitField0_ & 4) == 4;
      }

      public boolean hasJavaOuterClassname() {
         return (this.bitField0_ & 2) == 2;
      }

      public boolean hasJavaPackage() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasOptimizeFor() {
         return (this.bitField0_ & 16) == 16;
      }

      public boolean hasPyGenericServices() {
         return (this.bitField0_ & 256) == 256;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_FileOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FileOptions.class, DescriptorProtos.FileOptions.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getUninterpretedOptionCount(); ++var3) {
               if (!this.getUninterpretedOption(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.FileOptions.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.FileOptions.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.FileOptions.Builder(var1);
      }

      public DescriptorProtos.FileOptions.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         GeneratedMessage.ExtendableMessage.ExtensionWriter var3 = this.newExtensionWriter();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(1, this.getJavaPackageBytes());
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeBytes(8, this.getJavaOuterClassnameBytes());
         }

         if ((this.bitField0_ & 16) == 16) {
            var1.writeEnum(9, this.optimizeFor_.getNumber());
         }

         if ((this.bitField0_ & 4) == 4) {
            var1.writeBool(10, this.javaMultipleFiles_);
         }

         if ((this.bitField0_ & 32) == 32) {
            var1.writeBytes(11, this.getGoPackageBytes());
         }

         if ((this.bitField0_ & 64) == 64) {
            var1.writeBool(16, this.ccGenericServices_);
         }

         if ((this.bitField0_ & 128) == 128) {
            var1.writeBool(17, this.javaGenericServices_);
         }

         if ((this.bitField0_ & 256) == 256) {
            var1.writeBool(18, this.pyGenericServices_);
         }

         if ((this.bitField0_ & 8) == 8) {
            var1.writeBool(20, this.javaGenerateEqualsAndHash_);
         }

         for(int var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
            var1.writeMessage(999, (MessageLite)this.uninterpretedOption_.get(var2));
         }

         var3.writeUntil(536870912, var1);
         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.ExtendableBuilder implements DescriptorProtos.FileOptionsOrBuilder {
         private int bitField0_;
         private boolean ccGenericServices_;
         private Object goPackage_;
         private boolean javaGenerateEqualsAndHash_;
         private boolean javaGenericServices_;
         private boolean javaMultipleFiles_;
         private Object javaOuterClassname_;
         private Object javaPackage_;
         private DescriptorProtos.FileOptions.OptimizeMode optimizeFor_;
         private boolean pyGenericServices_;
         private RepeatedFieldBuilder uninterpretedOptionBuilder_;
         private List uninterpretedOption_;

         private Builder() {
            this.javaPackage_ = "";
            this.javaOuterClassname_ = "";
            this.optimizeFor_ = DescriptorProtos.FileOptions.OptimizeMode.SPEED;
            this.goPackage_ = "";
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.javaPackage_ = "";
            this.javaOuterClassname_ = "";
            this.optimizeFor_ = DescriptorProtos.FileOptions.OptimizeMode.SPEED;
            this.goPackage_ = "";
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.FileOptions.Builder create() {
            return new DescriptorProtos.FileOptions.Builder();
         }

         private void ensureUninterpretedOptionIsMutable() {
            if ((this.bitField0_ & 512) != 512) {
               this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
               this.bitField0_ |= 512;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor;
         }

         private RepeatedFieldBuilder getUninterpretedOptionFieldBuilder() {
            if (this.uninterpretedOptionBuilder_ == null) {
               List var2 = this.uninterpretedOption_;
               boolean var1;
               if ((this.bitField0_ & 512) == 512) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.uninterpretedOption_ = null;
            }

            return this.uninterpretedOptionBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getUninterpretedOptionFieldBuilder();
            }

         }

         public DescriptorProtos.FileOptions.Builder addAllUninterpretedOption(Iterable var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               GeneratedMessage.ExtendableBuilder.addAll(var1, this.uninterpretedOption_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption.Builder var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(var1, DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.FileOptions build() {
            DescriptorProtos.FileOptions var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.FileOptions buildPartial() {
            DescriptorProtos.FileOptions var4 = new DescriptorProtos.FileOptions(this);
            int var3 = this.bitField0_;
            int var2 = 0;
            if ((var3 & 1) == 1) {
               var2 = 0 | 1;
            }

            var4.javaPackage_ = this.javaPackage_;
            int var1 = var2;
            if ((var3 & 2) == 2) {
               var1 = var2 | 2;
            }

            var4.javaOuterClassname_ = this.javaOuterClassname_;
            var2 = var1;
            if ((var3 & 4) == 4) {
               var2 = var1 | 4;
            }

            var4.javaMultipleFiles_ = this.javaMultipleFiles_;
            var1 = var2;
            if ((var3 & 8) == 8) {
               var1 = var2 | 8;
            }

            var4.javaGenerateEqualsAndHash_ = this.javaGenerateEqualsAndHash_;
            var2 = var1;
            if ((var3 & 16) == 16) {
               var2 = var1 | 16;
            }

            var4.optimizeFor_ = this.optimizeFor_;
            var1 = var2;
            if ((var3 & 32) == 32) {
               var1 = var2 | 32;
            }

            var4.goPackage_ = this.goPackage_;
            var2 = var1;
            if ((var3 & 64) == 64) {
               var2 = var1 | 64;
            }

            var4.ccGenericServices_ = this.ccGenericServices_;
            var1 = var2;
            if ((var3 & 128) == 128) {
               var1 = var2 | 128;
            }

            var4.javaGenericServices_ = this.javaGenericServices_;
            var2 = var1;
            if ((var3 & 256) == 256) {
               var2 = var1 | 256;
            }

            var4.pyGenericServices_ = this.pyGenericServices_;
            RepeatedFieldBuilder var5 = this.uninterpretedOptionBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 512) == 512) {
                  this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                  this.bitField0_ &= -513;
               }

               var4.uninterpretedOption_ = this.uninterpretedOption_;
            } else {
               var4.uninterpretedOption_ = var5.build();
            }

            var4.bitField0_ = var2;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.FileOptions.Builder clear() {
            super.clear();
            this.javaPackage_ = "";
            int var1 = this.bitField0_ & -2;
            this.bitField0_ = var1;
            this.javaOuterClassname_ = "";
            var1 &= -3;
            this.bitField0_ = var1;
            this.javaMultipleFiles_ = false;
            var1 &= -5;
            this.bitField0_ = var1;
            this.javaGenerateEqualsAndHash_ = false;
            this.bitField0_ = var1 & -9;
            this.optimizeFor_ = DescriptorProtos.FileOptions.OptimizeMode.SPEED;
            var1 = this.bitField0_ & -17;
            this.bitField0_ = var1;
            this.goPackage_ = "";
            var1 &= -33;
            this.bitField0_ = var1;
            this.ccGenericServices_ = false;
            var1 &= -65;
            this.bitField0_ = var1;
            this.javaGenericServices_ = false;
            var1 &= -129;
            this.bitField0_ = var1;
            this.pyGenericServices_ = false;
            this.bitField0_ = var1 & -257;
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -513;
               return this;
            } else {
               var2.clear();
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder clearCcGenericServices() {
            this.bitField0_ &= -65;
            this.ccGenericServices_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearGoPackage() {
            this.bitField0_ &= -33;
            this.goPackage_ = DescriptorProtos.FileOptions.getDefaultInstance().getGoPackage();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearJavaGenerateEqualsAndHash() {
            this.bitField0_ &= -9;
            this.javaGenerateEqualsAndHash_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearJavaGenericServices() {
            this.bitField0_ &= -129;
            this.javaGenericServices_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearJavaMultipleFiles() {
            this.bitField0_ &= -5;
            this.javaMultipleFiles_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearJavaOuterClassname() {
            this.bitField0_ &= -3;
            this.javaOuterClassname_ = DescriptorProtos.FileOptions.getDefaultInstance().getJavaOuterClassname();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearJavaPackage() {
            this.bitField0_ &= -2;
            this.javaPackage_ = DescriptorProtos.FileOptions.getDefaultInstance().getJavaPackage();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearOptimizeFor() {
            this.bitField0_ &= -17;
            this.optimizeFor_ = DescriptorProtos.FileOptions.OptimizeMode.SPEED;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearPyGenericServices() {
            this.bitField0_ &= -257;
            this.pyGenericServices_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder clearUninterpretedOption() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -513;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public boolean getCcGenericServices() {
            return this.ccGenericServices_;
         }

         public DescriptorProtos.FileOptions getDefaultInstanceForType() {
            return DescriptorProtos.FileOptions.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor;
         }

         public String getGoPackage() {
            Object var1 = this.goPackage_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.goPackage_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getGoPackageBytes() {
            Object var1 = this.goPackage_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.goPackage_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public boolean getJavaGenerateEqualsAndHash() {
            return this.javaGenerateEqualsAndHash_;
         }

         public boolean getJavaGenericServices() {
            return this.javaGenericServices_;
         }

         public boolean getJavaMultipleFiles() {
            return this.javaMultipleFiles_;
         }

         public String getJavaOuterClassname() {
            Object var1 = this.javaOuterClassname_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.javaOuterClassname_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getJavaOuterClassnameBytes() {
            Object var1 = this.javaOuterClassname_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.javaOuterClassname_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public String getJavaPackage() {
            Object var1 = this.javaPackage_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.javaPackage_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getJavaPackageBytes() {
            Object var1 = this.javaPackage_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.javaPackage_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.FileOptions.OptimizeMode getOptimizeFor() {
            return this.optimizeFor_;
         }

         public boolean getPyGenericServices() {
            return this.pyGenericServices_;
         }

         public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOption)var2.getMessage(var1);
         }

         public DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().getBuilder(var1);
         }

         public List getUninterpretedOptionBuilderList() {
            return this.getUninterpretedOptionFieldBuilder().getBuilderList();
         }

         public int getUninterpretedOptionCount() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? this.uninterpretedOption_.size() : var1.getCount();
         }

         public List getUninterpretedOptionList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.uninterpretedOption_) : var1.getMessageList();
         }

         public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOptionOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getUninterpretedOptionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
         }

         public boolean hasCcGenericServices() {
            return (this.bitField0_ & 64) == 64;
         }

         public boolean hasGoPackage() {
            return (this.bitField0_ & 32) == 32;
         }

         public boolean hasJavaGenerateEqualsAndHash() {
            return (this.bitField0_ & 8) == 8;
         }

         public boolean hasJavaGenericServices() {
            return (this.bitField0_ & 128) == 128;
         }

         public boolean hasJavaMultipleFiles() {
            return (this.bitField0_ & 4) == 4;
         }

         public boolean hasJavaOuterClassname() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasJavaPackage() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasOptimizeFor() {
            return (this.bitField0_ & 16) == 16;
         }

         public boolean hasPyGenericServices() {
            return (this.bitField0_ & 256) == 256;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_FileOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.FileOptions.class, DescriptorProtos.FileOptions.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getUninterpretedOptionCount(); ++var1) {
               if (!this.getUninterpretedOption(var1).isInitialized()) {
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.FileOptions.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.FileOptions var3 = (DescriptorProtos.FileOptions)var4;

            DescriptorProtos.FileOptions var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.FileOptions)DescriptorProtos.FileOptions.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.FileOptions)var4;

                  try {
                     var21 = (DescriptorProtos.FileOptions)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.FileOptions.Builder mergeFrom(DescriptorProtos.FileOptions var1) {
            if (var1 == DescriptorProtos.FileOptions.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasJavaPackage()) {
                  this.bitField0_ |= 1;
                  this.javaPackage_ = var1.javaPackage_;
                  this.onChanged();
               }

               if (var1.hasJavaOuterClassname()) {
                  this.bitField0_ |= 2;
                  this.javaOuterClassname_ = var1.javaOuterClassname_;
                  this.onChanged();
               }

               if (var1.hasJavaMultipleFiles()) {
                  this.setJavaMultipleFiles(var1.getJavaMultipleFiles());
               }

               if (var1.hasJavaGenerateEqualsAndHash()) {
                  this.setJavaGenerateEqualsAndHash(var1.getJavaGenerateEqualsAndHash());
               }

               if (var1.hasOptimizeFor()) {
                  this.setOptimizeFor(var1.getOptimizeFor());
               }

               if (var1.hasGoPackage()) {
                  this.bitField0_ |= 32;
                  this.goPackage_ = var1.goPackage_;
                  this.onChanged();
               }

               if (var1.hasCcGenericServices()) {
                  this.setCcGenericServices(var1.getCcGenericServices());
               }

               if (var1.hasJavaGenericServices()) {
                  this.setJavaGenericServices(var1.getJavaGenericServices());
               }

               if (var1.hasPyGenericServices()) {
                  this.setPyGenericServices(var1.getPyGenericServices());
               }

               if (this.uninterpretedOptionBuilder_ == null) {
                  if (!var1.uninterpretedOption_.isEmpty()) {
                     if (this.uninterpretedOption_.isEmpty()) {
                        this.uninterpretedOption_ = var1.uninterpretedOption_;
                        this.bitField0_ &= -513;
                     } else {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.addAll(var1.uninterpretedOption_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.uninterpretedOption_.isEmpty()) {
                  if (this.uninterpretedOptionBuilder_.isEmpty()) {
                     this.uninterpretedOptionBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.uninterpretedOptionBuilder_ = null;
                     this.uninterpretedOption_ = var1.uninterpretedOption_;
                     this.bitField0_ &= -513;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getUninterpretedOptionFieldBuilder();
                     }

                     this.uninterpretedOptionBuilder_ = var2;
                  } else {
                     this.uninterpretedOptionBuilder_.addAllMessages(var1.uninterpretedOption_);
                  }
               }

               this.mergeExtensionFields(var1);
               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.FileOptions) {
               return this.mergeFrom((DescriptorProtos.FileOptions)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder removeUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder setCcGenericServices(boolean var1) {
            this.bitField0_ |= 64;
            this.ccGenericServices_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder setGoPackage(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 32;
               this.goPackage_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileOptions.Builder setGoPackageBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 32;
               this.goPackage_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileOptions.Builder setJavaGenerateEqualsAndHash(boolean var1) {
            this.bitField0_ |= 8;
            this.javaGenerateEqualsAndHash_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder setJavaGenericServices(boolean var1) {
            this.bitField0_ |= 128;
            this.javaGenericServices_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder setJavaMultipleFiles(boolean var1) {
            this.bitField0_ |= 4;
            this.javaMultipleFiles_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder setJavaOuterClassname(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 2;
               this.javaOuterClassname_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileOptions.Builder setJavaOuterClassnameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 2;
               this.javaOuterClassname_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileOptions.Builder setJavaPackage(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.javaPackage_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileOptions.Builder setJavaPackageBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.javaPackage_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileOptions.Builder setOptimizeFor(DescriptorProtos.FileOptions.OptimizeMode var1) {
            if (var1 != null) {
               this.bitField0_ |= 16;
               this.optimizeFor_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.FileOptions.Builder setPyGenericServices(boolean var1) {
            this.bitField0_ |= 256;
            this.pyGenericServices_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.FileOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.FileOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }

      public static enum OptimizeMode implements ProtocolMessageEnum {
         CODE_SIZE(1, 2);

         public static final int CODE_SIZE_VALUE = 2;
         LITE_RUNTIME;

         public static final int LITE_RUNTIME_VALUE = 3;
         SPEED(0, 1);

         public static final int SPEED_VALUE = 1;
         private static final DescriptorProtos.FileOptions.OptimizeMode[] VALUES;
         private static Internal.EnumLiteMap internalValueMap;
         private final int index;
         private final int value;

         static {
            DescriptorProtos.FileOptions.OptimizeMode var0 = new DescriptorProtos.FileOptions.OptimizeMode("LITE_RUNTIME", 2, 2, 3);
            LITE_RUNTIME = var0;
            internalValueMap = new Internal.EnumLiteMap() {
               public DescriptorProtos.FileOptions.OptimizeMode findValueByNumber(int var1) {
                  return DescriptorProtos.FileOptions.OptimizeMode.valueOf(var1);
               }
            };
            VALUES = values();
         }

         private OptimizeMode(int var3, int var4) {
            this.index = var3;
            this.value = var4;
         }

         public static final Descriptors.EnumDescriptor getDescriptor() {
            return (Descriptors.EnumDescriptor)DescriptorProtos.FileOptions.getDescriptor().getEnumTypes().get(0);
         }

         public static Internal.EnumLiteMap internalGetValueMap() {
            return internalValueMap;
         }

         public static DescriptorProtos.FileOptions.OptimizeMode valueOf(int var0) {
            if (var0 != 1) {
               if (var0 != 2) {
                  return var0 != 3 ? null : LITE_RUNTIME;
               } else {
                  return CODE_SIZE;
               }
            } else {
               return SPEED;
            }
         }

         public static DescriptorProtos.FileOptions.OptimizeMode valueOf(Descriptors.EnumValueDescriptor var0) {
            if (var0.getType() == getDescriptor()) {
               return VALUES[var0.getIndex()];
            } else {
               throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
         }

         public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
         }

         public final int getNumber() {
            return this.value;
         }

         public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            return (Descriptors.EnumValueDescriptor)getDescriptor().getValues().get(this.index);
         }
      }
   }

   public interface FileOptionsOrBuilder extends GeneratedMessage.ExtendableMessageOrBuilder {
      boolean getCcGenericServices();

      String getGoPackage();

      ByteString getGoPackageBytes();

      boolean getJavaGenerateEqualsAndHash();

      boolean getJavaGenericServices();

      boolean getJavaMultipleFiles();

      String getJavaOuterClassname();

      ByteString getJavaOuterClassnameBytes();

      String getJavaPackage();

      ByteString getJavaPackageBytes();

      DescriptorProtos.FileOptions.OptimizeMode getOptimizeFor();

      boolean getPyGenericServices();

      DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1);

      int getUninterpretedOptionCount();

      List getUninterpretedOptionList();

      DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

      List getUninterpretedOptionOrBuilderList();

      boolean hasCcGenericServices();

      boolean hasGoPackage();

      boolean hasJavaGenerateEqualsAndHash();

      boolean hasJavaGenericServices();

      boolean hasJavaMultipleFiles();

      boolean hasJavaOuterClassname();

      boolean hasJavaPackage();

      boolean hasOptimizeFor();

      boolean hasPyGenericServices();
   }

   public static final class MessageOptions extends GeneratedMessage.ExtendableMessage implements DescriptorProtos.MessageOptionsOrBuilder {
      public static final int MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER = 1;
      public static final int NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER = 2;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.MessageOptions parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.MessageOptions(var1, var2);
         }
      };
      public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
      private static final DescriptorProtos.MessageOptions defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private boolean messageSetWireFormat_;
      private boolean noStandardDescriptorAccessor_;
      private List uninterpretedOption_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.MessageOptions var0 = new DescriptorProtos.MessageOptions(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private MessageOptions(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      MessageOptions(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private MessageOptions(GeneratedMessage.ExtendableBuilder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      MessageOptions(GeneratedMessage.ExtendableBuilder var1, Object var2) {
         this(var1);
      }

      private MessageOptions(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.MessageOptions getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor;
      }

      private void initFields() {
         this.messageSetWireFormat_ = false;
         this.noStandardDescriptorAccessor_ = false;
         this.uninterpretedOption_ = Collections.emptyList();
      }

      public static DescriptorProtos.MessageOptions.Builder newBuilder() {
         return DescriptorProtos.MessageOptions.Builder.create();
      }

      public static DescriptorProtos.MessageOptions.Builder newBuilder(DescriptorProtos.MessageOptions var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.MessageOptions parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.MessageOptions)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.MessageOptions parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MessageOptions)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.MessageOptions parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MessageOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MessageOptions parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MessageOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MessageOptions parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.MessageOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MessageOptions parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MessageOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MessageOptions parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.MessageOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MessageOptions parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MessageOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MessageOptions parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MessageOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MessageOptions parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MessageOptions)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.MessageOptions getDefaultInstanceForType() {
         return defaultInstance;
      }

      public boolean getMessageSetWireFormat() {
         return this.messageSetWireFormat_;
      }

      public boolean getNoStandardDescriptorAccessor() {
         return this.noStandardDescriptorAccessor_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var2 = 0 + CodedOutputStream.computeBoolSize(1, this.messageSetWireFormat_);
            }

            var1 = var2;
            if ((this.bitField0_ & 2) == 2) {
               var1 = var2 + CodedOutputStream.computeBoolSize(2, this.noStandardDescriptorAccessor_);
            }

            for(var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(999, (MessageLite)this.uninterpretedOption_.get(var2));
            }

            var1 = var1 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
         return (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1);
      }

      public int getUninterpretedOptionCount() {
         return this.uninterpretedOption_.size();
      }

      public List getUninterpretedOptionList() {
         return this.uninterpretedOption_;
      }

      public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
         return (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1);
      }

      public List getUninterpretedOptionOrBuilderList() {
         return this.uninterpretedOption_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasMessageSetWireFormat() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasNoStandardDescriptorAccessor() {
         return (this.bitField0_ & 2) == 2;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_MessageOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.MessageOptions.class, DescriptorProtos.MessageOptions.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getUninterpretedOptionCount(); ++var3) {
               if (!this.getUninterpretedOption(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.MessageOptions.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.MessageOptions.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.MessageOptions.Builder(var1);
      }

      public DescriptorProtos.MessageOptions.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         GeneratedMessage.ExtendableMessage.ExtensionWriter var3 = this.newExtensionWriter();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBool(1, this.messageSetWireFormat_);
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeBool(2, this.noStandardDescriptorAccessor_);
         }

         for(int var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
            var1.writeMessage(999, (MessageLite)this.uninterpretedOption_.get(var2));
         }

         var3.writeUntil(536870912, var1);
         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.ExtendableBuilder implements DescriptorProtos.MessageOptionsOrBuilder {
         private int bitField0_;
         private boolean messageSetWireFormat_;
         private boolean noStandardDescriptorAccessor_;
         private RepeatedFieldBuilder uninterpretedOptionBuilder_;
         private List uninterpretedOption_;

         private Builder() {
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.MessageOptions.Builder create() {
            return new DescriptorProtos.MessageOptions.Builder();
         }

         private void ensureUninterpretedOptionIsMutable() {
            if ((this.bitField0_ & 4) != 4) {
               this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
               this.bitField0_ |= 4;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor;
         }

         private RepeatedFieldBuilder getUninterpretedOptionFieldBuilder() {
            if (this.uninterpretedOptionBuilder_ == null) {
               List var2 = this.uninterpretedOption_;
               boolean var1;
               if ((this.bitField0_ & 4) == 4) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.uninterpretedOption_ = null;
            }

            return this.uninterpretedOptionBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getUninterpretedOptionFieldBuilder();
            }

         }

         public DescriptorProtos.MessageOptions.Builder addAllUninterpretedOption(Iterable var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               GeneratedMessage.ExtendableBuilder.addAll(var1, this.uninterpretedOption_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption.Builder var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(var1, DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.MessageOptions build() {
            DescriptorProtos.MessageOptions var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.MessageOptions buildPartial() {
            DescriptorProtos.MessageOptions var4 = new DescriptorProtos.MessageOptions(this);
            int var3 = this.bitField0_;
            int var1 = 0;
            if ((var3 & 1) == 1) {
               var1 = 0 | 1;
            }

            var4.messageSetWireFormat_ = this.messageSetWireFormat_;
            int var2 = var1;
            if ((var3 & 2) == 2) {
               var2 = var1 | 2;
            }

            var4.noStandardDescriptorAccessor_ = this.noStandardDescriptorAccessor_;
            RepeatedFieldBuilder var5 = this.uninterpretedOptionBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 4) == 4) {
                  this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                  this.bitField0_ &= -5;
               }

               var4.uninterpretedOption_ = this.uninterpretedOption_;
            } else {
               var4.uninterpretedOption_ = var5.build();
            }

            var4.bitField0_ = var2;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.MessageOptions.Builder clear() {
            super.clear();
            this.messageSetWireFormat_ = false;
            int var1 = this.bitField0_ & -2;
            this.bitField0_ = var1;
            this.noStandardDescriptorAccessor_ = false;
            this.bitField0_ = var1 & -3;
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -5;
               return this;
            } else {
               var2.clear();
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder clearMessageSetWireFormat() {
            this.bitField0_ &= -2;
            this.messageSetWireFormat_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.MessageOptions.Builder clearNoStandardDescriptorAccessor() {
            this.bitField0_ &= -3;
            this.noStandardDescriptorAccessor_ = false;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.MessageOptions.Builder clearUninterpretedOption() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -5;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.MessageOptions getDefaultInstanceForType() {
            return DescriptorProtos.MessageOptions.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor;
         }

         public boolean getMessageSetWireFormat() {
            return this.messageSetWireFormat_;
         }

         public boolean getNoStandardDescriptorAccessor() {
            return this.noStandardDescriptorAccessor_;
         }

         public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOption)var2.getMessage(var1);
         }

         public DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().getBuilder(var1);
         }

         public List getUninterpretedOptionBuilderList() {
            return this.getUninterpretedOptionFieldBuilder().getBuilderList();
         }

         public int getUninterpretedOptionCount() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? this.uninterpretedOption_.size() : var1.getCount();
         }

         public List getUninterpretedOptionList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.uninterpretedOption_) : var1.getMessageList();
         }

         public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOptionOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getUninterpretedOptionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
         }

         public boolean hasMessageSetWireFormat() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasNoStandardDescriptorAccessor() {
            return (this.bitField0_ & 2) == 2;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_MessageOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.MessageOptions.class, DescriptorProtos.MessageOptions.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getUninterpretedOptionCount(); ++var1) {
               if (!this.getUninterpretedOption(var1).isInitialized()) {
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.MessageOptions.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.MessageOptions var3 = (DescriptorProtos.MessageOptions)var4;

            DescriptorProtos.MessageOptions var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.MessageOptions)DescriptorProtos.MessageOptions.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.MessageOptions)var4;

                  try {
                     var21 = (DescriptorProtos.MessageOptions)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.MessageOptions.Builder mergeFrom(DescriptorProtos.MessageOptions var1) {
            if (var1 == DescriptorProtos.MessageOptions.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasMessageSetWireFormat()) {
                  this.setMessageSetWireFormat(var1.getMessageSetWireFormat());
               }

               if (var1.hasNoStandardDescriptorAccessor()) {
                  this.setNoStandardDescriptorAccessor(var1.getNoStandardDescriptorAccessor());
               }

               if (this.uninterpretedOptionBuilder_ == null) {
                  if (!var1.uninterpretedOption_.isEmpty()) {
                     if (this.uninterpretedOption_.isEmpty()) {
                        this.uninterpretedOption_ = var1.uninterpretedOption_;
                        this.bitField0_ &= -5;
                     } else {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.addAll(var1.uninterpretedOption_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.uninterpretedOption_.isEmpty()) {
                  if (this.uninterpretedOptionBuilder_.isEmpty()) {
                     this.uninterpretedOptionBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.uninterpretedOptionBuilder_ = null;
                     this.uninterpretedOption_ = var1.uninterpretedOption_;
                     this.bitField0_ &= -5;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getUninterpretedOptionFieldBuilder();
                     }

                     this.uninterpretedOptionBuilder_ = var2;
                  } else {
                     this.uninterpretedOptionBuilder_.addAllMessages(var1.uninterpretedOption_);
                  }
               }

               this.mergeExtensionFields(var1);
               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.MessageOptions) {
               return this.mergeFrom((DescriptorProtos.MessageOptions)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder removeUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder setMessageSetWireFormat(boolean var1) {
            this.bitField0_ |= 1;
            this.messageSetWireFormat_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.MessageOptions.Builder setNoStandardDescriptorAccessor(boolean var1) {
            this.bitField0_ |= 2;
            this.noStandardDescriptorAccessor_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.MessageOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.MessageOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }
   }

   public interface MessageOptionsOrBuilder extends GeneratedMessage.ExtendableMessageOrBuilder {
      boolean getMessageSetWireFormat();

      boolean getNoStandardDescriptorAccessor();

      DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1);

      int getUninterpretedOptionCount();

      List getUninterpretedOptionList();

      DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

      List getUninterpretedOptionOrBuilderList();

      boolean hasMessageSetWireFormat();

      boolean hasNoStandardDescriptorAccessor();
   }

   public static final class MethodDescriptorProto extends GeneratedMessage implements DescriptorProtos.MethodDescriptorProtoOrBuilder {
      public static final int INPUT_TYPE_FIELD_NUMBER = 2;
      public static final int NAME_FIELD_NUMBER = 1;
      public static final int OPTIONS_FIELD_NUMBER = 4;
      public static final int OUTPUT_TYPE_FIELD_NUMBER = 3;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.MethodDescriptorProto parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.MethodDescriptorProto(var1, var2);
         }
      };
      private static final DescriptorProtos.MethodDescriptorProto defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private Object inputType_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private Object name_;
      private DescriptorProtos.MethodOptions options_;
      private Object outputType_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.MethodDescriptorProto var0 = new DescriptorProtos.MethodDescriptorProto(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private MethodDescriptorProto(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      MethodDescriptorProto(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private MethodDescriptorProto(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      MethodDescriptorProto(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private MethodDescriptorProto(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.MethodDescriptorProto getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor;
      }

      private void initFields() {
         this.name_ = "";
         this.inputType_ = "";
         this.outputType_ = "";
         this.options_ = DescriptorProtos.MethodOptions.getDefaultInstance();
      }

      public static DescriptorProtos.MethodDescriptorProto.Builder newBuilder() {
         return DescriptorProtos.MethodDescriptorProto.Builder.create();
      }

      public static DescriptorProtos.MethodDescriptorProto.Builder newBuilder(DescriptorProtos.MethodDescriptorProto var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.MethodDescriptorProto parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.MethodDescriptorProto parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.MethodDescriptorProto parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MethodDescriptorProto parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MethodDescriptorProto parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MethodDescriptorProto parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MethodDescriptorProto parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MethodDescriptorProto parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MethodDescriptorProto parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MethodDescriptorProto parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MethodDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.MethodDescriptorProto getDefaultInstanceForType() {
         return defaultInstance;
      }

      public String getInputType() {
         Object var1 = this.inputType_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.inputType_ = var2;
            }

            return var2;
         }
      }

      public ByteString getInputTypeBytes() {
         Object var1 = this.inputType_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.inputType_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public String getName() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.name_ = var2;
            }

            return var2;
         }
      }

      public ByteString getNameBytes() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.name_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.MethodOptions getOptions() {
         return this.options_;
      }

      public DescriptorProtos.MethodOptionsOrBuilder getOptionsOrBuilder() {
         return this.options_;
      }

      public String getOutputType() {
         Object var1 = this.outputType_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.outputType_ = var2;
            }

            return var2;
         }
      }

      public ByteString getOutputTypeBytes() {
         Object var1 = this.outputType_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.outputType_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var2 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 2) == 2) {
               var1 = var2 + CodedOutputStream.computeBytesSize(2, this.getInputTypeBytes());
            }

            var2 = var1;
            if ((this.bitField0_ & 4) == 4) {
               var2 = var1 + CodedOutputStream.computeBytesSize(3, this.getOutputTypeBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 8) == 8) {
               var1 = var2 + CodedOutputStream.computeMessageSize(4, this.options_);
            }

            var1 += this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasInputType() {
         return (this.bitField0_ & 2) == 2;
      }

      public boolean hasName() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasOptions() {
         return (this.bitField0_ & 8) == 8;
      }

      public boolean hasOutputType() {
         return (this.bitField0_ & 4) == 4;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.MethodDescriptorProto.class, DescriptorProtos.MethodDescriptorProto.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else if (this.hasOptions() && !this.getOptions().isInitialized()) {
            this.memoizedIsInitialized = 0;
            return false;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public DescriptorProtos.MethodDescriptorProto.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.MethodDescriptorProto.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.MethodDescriptorProto.Builder(var1);
      }

      public DescriptorProtos.MethodDescriptorProto.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(1, this.getNameBytes());
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeBytes(2, this.getInputTypeBytes());
         }

         if ((this.bitField0_ & 4) == 4) {
            var1.writeBytes(3, this.getOutputTypeBytes());
         }

         if ((this.bitField0_ & 8) == 8) {
            var1.writeMessage(4, this.options_);
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.MethodDescriptorProtoOrBuilder {
         private int bitField0_;
         private Object inputType_;
         private Object name_;
         private SingleFieldBuilder optionsBuilder_;
         private DescriptorProtos.MethodOptions options_;
         private Object outputType_;

         private Builder() {
            this.name_ = "";
            this.inputType_ = "";
            this.outputType_ = "";
            this.options_ = DescriptorProtos.MethodOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.name_ = "";
            this.inputType_ = "";
            this.outputType_ = "";
            this.options_ = DescriptorProtos.MethodOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.MethodDescriptorProto.Builder create() {
            return new DescriptorProtos.MethodDescriptorProto.Builder();
         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor;
         }

         private SingleFieldBuilder getOptionsFieldBuilder() {
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

         public DescriptorProtos.MethodDescriptorProto build() {
            DescriptorProtos.MethodDescriptorProto var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.MethodDescriptorProto buildPartial() {
            DescriptorProtos.MethodDescriptorProto var4 = new DescriptorProtos.MethodDescriptorProto(this);
            int var3 = this.bitField0_;
            int var2 = 0;
            if ((var3 & 1) == 1) {
               var2 = 0 | 1;
            }

            var4.name_ = this.name_;
            int var1 = var2;
            if ((var3 & 2) == 2) {
               var1 = var2 | 2;
            }

            var4.inputType_ = this.inputType_;
            var2 = var1;
            if ((var3 & 4) == 4) {
               var2 = var1 | 4;
            }

            var4.outputType_ = this.outputType_;
            var1 = var2;
            if ((var3 & 8) == 8) {
               var1 = var2 | 8;
            }

            SingleFieldBuilder var5 = this.optionsBuilder_;
            if (var5 == null) {
               var4.options_ = this.options_;
            } else {
               var4.options_ = (DescriptorProtos.MethodOptions)var5.build();
            }

            var4.bitField0_ = var1;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder clear() {
            super.clear();
            this.name_ = "";
            int var1 = this.bitField0_ & -2;
            this.bitField0_ = var1;
            this.inputType_ = "";
            var1 &= -3;
            this.bitField0_ = var1;
            this.outputType_ = "";
            this.bitField0_ = var1 & -5;
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = DescriptorProtos.MethodOptions.getDefaultInstance();
            } else {
               var2.clear();
            }

            this.bitField0_ &= -9;
            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder clearInputType() {
            this.bitField0_ &= -3;
            this.inputType_ = DescriptorProtos.MethodDescriptorProto.getDefaultInstance().getInputType();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder clearName() {
            this.bitField0_ &= -2;
            this.name_ = DescriptorProtos.MethodDescriptorProto.getDefaultInstance().getName();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder clearOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            if (var1 == null) {
               this.options_ = DescriptorProtos.MethodOptions.getDefaultInstance();
               this.onChanged();
            } else {
               var1.clear();
            }

            this.bitField0_ &= -9;
            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder clearOutputType() {
            this.bitField0_ &= -5;
            this.outputType_ = DescriptorProtos.MethodDescriptorProto.getDefaultInstance().getOutputType();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.MethodDescriptorProto getDefaultInstanceForType() {
            return DescriptorProtos.MethodDescriptorProto.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor;
         }

         public String getInputType() {
            Object var1 = this.inputType_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.inputType_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getInputTypeBytes() {
            Object var1 = this.inputType_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.inputType_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public String getName() {
            Object var1 = this.name_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.name_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getNameBytes() {
            Object var1 = this.name_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.name_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.MethodOptions getOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return var1 == null ? this.options_ : (DescriptorProtos.MethodOptions)var1.getMessage();
         }

         public DescriptorProtos.MethodOptions.Builder getOptionsBuilder() {
            this.bitField0_ |= 8;
            this.onChanged();
            return (DescriptorProtos.MethodOptions.Builder)this.getOptionsFieldBuilder().getBuilder();
         }

         public DescriptorProtos.MethodOptionsOrBuilder getOptionsOrBuilder() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return (DescriptorProtos.MethodOptionsOrBuilder)(var1 != null ? (DescriptorProtos.MethodOptionsOrBuilder)var1.getMessageOrBuilder() : this.options_);
         }

         public String getOutputType() {
            Object var1 = this.outputType_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.outputType_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getOutputTypeBytes() {
            Object var1 = this.outputType_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.outputType_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public boolean hasInputType() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasOptions() {
            return (this.bitField0_ & 8) == 8;
         }

         public boolean hasOutputType() {
            return (this.bitField0_ & 4) == 4;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.MethodDescriptorProto.class, DescriptorProtos.MethodDescriptorProto.Builder.class);
         }

         public final boolean isInitialized() {
            return !this.hasOptions() || this.getOptions().isInitialized();
         }

         public DescriptorProtos.MethodDescriptorProto.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.MethodDescriptorProto var3 = (DescriptorProtos.MethodDescriptorProto)var4;

            DescriptorProtos.MethodDescriptorProto var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.MethodDescriptorProto)DescriptorProtos.MethodDescriptorProto.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.MethodDescriptorProto)var4;

                  try {
                     var21 = (DescriptorProtos.MethodDescriptorProto)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder mergeFrom(DescriptorProtos.MethodDescriptorProto var1) {
            if (var1 == DescriptorProtos.MethodDescriptorProto.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasName()) {
                  this.bitField0_ |= 1;
                  this.name_ = var1.name_;
                  this.onChanged();
               }

               if (var1.hasInputType()) {
                  this.bitField0_ |= 2;
                  this.inputType_ = var1.inputType_;
                  this.onChanged();
               }

               if (var1.hasOutputType()) {
                  this.bitField0_ |= 4;
                  this.outputType_ = var1.outputType_;
                  this.onChanged();
               }

               if (var1.hasOptions()) {
                  this.mergeOptions(var1.getOptions());
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.MethodDescriptorProto.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.MethodDescriptorProto) {
               return this.mergeFrom((DescriptorProtos.MethodDescriptorProto)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.MethodDescriptorProto.Builder mergeOptions(DescriptorProtos.MethodOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if ((this.bitField0_ & 8) == 8 && this.options_ != DescriptorProtos.MethodOptions.getDefaultInstance()) {
                  this.options_ = DescriptorProtos.MethodOptions.newBuilder(this.options_).mergeFrom(var1).buildPartial();
               } else {
                  this.options_ = var1;
               }

               this.onChanged();
            } else {
               var2.mergeFrom(var1);
            }

            this.bitField0_ |= 8;
            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder setInputType(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 2;
               this.inputType_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.MethodDescriptorProto.Builder setInputTypeBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 2;
               this.inputType_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.MethodDescriptorProto.Builder setName(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.MethodDescriptorProto.Builder setNameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.MethodDescriptorProto.Builder setOptions(DescriptorProtos.MethodOptions.Builder var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = var1.build();
               this.onChanged();
            } else {
               var2.setMessage(var1.build());
            }

            this.bitField0_ |= 8;
            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder setOptions(DescriptorProtos.MethodOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if (var1 == null) {
                  throw null;
               }

               this.options_ = var1;
               this.onChanged();
            } else {
               var2.setMessage(var1);
            }

            this.bitField0_ |= 8;
            return this;
         }

         public DescriptorProtos.MethodDescriptorProto.Builder setOutputType(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 4;
               this.outputType_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.MethodDescriptorProto.Builder setOutputTypeBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 4;
               this.outputType_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }
      }
   }

   public interface MethodDescriptorProtoOrBuilder extends MessageOrBuilder {
      String getInputType();

      ByteString getInputTypeBytes();

      String getName();

      ByteString getNameBytes();

      DescriptorProtos.MethodOptions getOptions();

      DescriptorProtos.MethodOptionsOrBuilder getOptionsOrBuilder();

      String getOutputType();

      ByteString getOutputTypeBytes();

      boolean hasInputType();

      boolean hasName();

      boolean hasOptions();

      boolean hasOutputType();
   }

   public static final class MethodOptions extends GeneratedMessage.ExtendableMessage implements DescriptorProtos.MethodOptionsOrBuilder {
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.MethodOptions parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.MethodOptions(var1, var2);
         }
      };
      public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
      private static final DescriptorProtos.MethodOptions defaultInstance;
      private static final long serialVersionUID = 0L;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private List uninterpretedOption_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.MethodOptions var0 = new DescriptorProtos.MethodOptions(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private MethodOptions(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      MethodOptions(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private MethodOptions(GeneratedMessage.ExtendableBuilder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      MethodOptions(GeneratedMessage.ExtendableBuilder var1, Object var2) {
         this(var1);
      }

      private MethodOptions(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.MethodOptions getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor;
      }

      private void initFields() {
         this.uninterpretedOption_ = Collections.emptyList();
      }

      public static DescriptorProtos.MethodOptions.Builder newBuilder() {
         return DescriptorProtos.MethodOptions.Builder.create();
      }

      public static DescriptorProtos.MethodOptions.Builder newBuilder(DescriptorProtos.MethodOptions var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.MethodOptions parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.MethodOptions)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.MethodOptions parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MethodOptions)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.MethodOptions parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MethodOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MethodOptions parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MethodOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MethodOptions parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.MethodOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MethodOptions parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MethodOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MethodOptions parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.MethodOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MethodOptions parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.MethodOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.MethodOptions parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MethodOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.MethodOptions parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.MethodOptions)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.MethodOptions getDefaultInstanceForType() {
         return defaultInstance;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;

            for(var1 = 0; var1 < this.uninterpretedOption_.size(); ++var1) {
               var2 += CodedOutputStream.computeMessageSize(999, (MessageLite)this.uninterpretedOption_.get(var1));
            }

            var1 = var2 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
         return (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1);
      }

      public int getUninterpretedOptionCount() {
         return this.uninterpretedOption_.size();
      }

      public List getUninterpretedOptionList() {
         return this.uninterpretedOption_;
      }

      public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
         return (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1);
      }

      public List getUninterpretedOptionOrBuilderList() {
         return this.uninterpretedOption_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_MethodOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.MethodOptions.class, DescriptorProtos.MethodOptions.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getUninterpretedOptionCount(); ++var3) {
               if (!this.getUninterpretedOption(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.MethodOptions.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.MethodOptions.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.MethodOptions.Builder(var1);
      }

      public DescriptorProtos.MethodOptions.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         GeneratedMessage.ExtendableMessage.ExtensionWriter var3 = this.newExtensionWriter();

         for(int var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
            var1.writeMessage(999, (MessageLite)this.uninterpretedOption_.get(var2));
         }

         var3.writeUntil(536870912, var1);
         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.ExtendableBuilder implements DescriptorProtos.MethodOptionsOrBuilder {
         private int bitField0_;
         private RepeatedFieldBuilder uninterpretedOptionBuilder_;
         private List uninterpretedOption_;

         private Builder() {
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.MethodOptions.Builder create() {
            return new DescriptorProtos.MethodOptions.Builder();
         }

         private void ensureUninterpretedOptionIsMutable() {
            if ((this.bitField0_ & 1) != 1) {
               this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
               this.bitField0_ |= 1;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor;
         }

         private RepeatedFieldBuilder getUninterpretedOptionFieldBuilder() {
            if (this.uninterpretedOptionBuilder_ == null) {
               List var3 = this.uninterpretedOption_;
               int var1 = this.bitField0_;
               boolean var2 = true;
               if ((var1 & 1) != 1) {
                  var2 = false;
               }

               this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(var3, var2, this.getParentForChildren(), this.isClean());
               this.uninterpretedOption_ = null;
            }

            return this.uninterpretedOptionBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getUninterpretedOptionFieldBuilder();
            }

         }

         public DescriptorProtos.MethodOptions.Builder addAllUninterpretedOption(Iterable var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               GeneratedMessage.ExtendableBuilder.addAll(var1, this.uninterpretedOption_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption.Builder var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(var1, DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.MethodOptions build() {
            DescriptorProtos.MethodOptions var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.MethodOptions buildPartial() {
            DescriptorProtos.MethodOptions var2 = new DescriptorProtos.MethodOptions(this);
            int var1 = this.bitField0_;
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if ((this.bitField0_ & 1) == 1) {
                  this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                  this.bitField0_ &= -2;
               }

               var2.uninterpretedOption_ = this.uninterpretedOption_;
            } else {
               var2.uninterpretedOption_ = var3.build();
            }

            this.onBuilt();
            return var2;
         }

         public DescriptorProtos.MethodOptions.Builder clear() {
            super.clear();
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -2;
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder clearUninterpretedOption() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -2;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.MethodOptions getDefaultInstanceForType() {
            return DescriptorProtos.MethodOptions.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor;
         }

         public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOption)var2.getMessage(var1);
         }

         public DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().getBuilder(var1);
         }

         public List getUninterpretedOptionBuilderList() {
            return this.getUninterpretedOptionFieldBuilder().getBuilderList();
         }

         public int getUninterpretedOptionCount() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? this.uninterpretedOption_.size() : var1.getCount();
         }

         public List getUninterpretedOptionList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.uninterpretedOption_) : var1.getMessageList();
         }

         public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOptionOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getUninterpretedOptionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_MethodOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.MethodOptions.class, DescriptorProtos.MethodOptions.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getUninterpretedOptionCount(); ++var1) {
               if (!this.getUninterpretedOption(var1).isInitialized()) {
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.MethodOptions.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.MethodOptions var3 = (DescriptorProtos.MethodOptions)var4;

            DescriptorProtos.MethodOptions var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.MethodOptions)DescriptorProtos.MethodOptions.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.MethodOptions)var4;

                  try {
                     var21 = (DescriptorProtos.MethodOptions)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.MethodOptions.Builder mergeFrom(DescriptorProtos.MethodOptions var1) {
            if (var1 == DescriptorProtos.MethodOptions.getDefaultInstance()) {
               return this;
            } else {
               if (this.uninterpretedOptionBuilder_ == null) {
                  if (!var1.uninterpretedOption_.isEmpty()) {
                     if (this.uninterpretedOption_.isEmpty()) {
                        this.uninterpretedOption_ = var1.uninterpretedOption_;
                        this.bitField0_ &= -2;
                     } else {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.addAll(var1.uninterpretedOption_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.uninterpretedOption_.isEmpty()) {
                  if (this.uninterpretedOptionBuilder_.isEmpty()) {
                     this.uninterpretedOptionBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.uninterpretedOptionBuilder_ = null;
                     this.uninterpretedOption_ = var1.uninterpretedOption_;
                     this.bitField0_ &= -2;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getUninterpretedOptionFieldBuilder();
                     }

                     this.uninterpretedOptionBuilder_ = var2;
                  } else {
                     this.uninterpretedOptionBuilder_.addAllMessages(var1.uninterpretedOption_);
                  }
               }

               this.mergeExtensionFields(var1);
               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.MethodOptions) {
               return this.mergeFrom((DescriptorProtos.MethodOptions)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder removeUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.MethodOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }
   }

   public interface MethodOptionsOrBuilder extends GeneratedMessage.ExtendableMessageOrBuilder {
      DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1);

      int getUninterpretedOptionCount();

      List getUninterpretedOptionList();

      DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

      List getUninterpretedOptionOrBuilderList();
   }

   public static final class ServiceDescriptorProto extends GeneratedMessage implements DescriptorProtos.ServiceDescriptorProtoOrBuilder {
      public static final int METHOD_FIELD_NUMBER = 2;
      public static final int NAME_FIELD_NUMBER = 1;
      public static final int OPTIONS_FIELD_NUMBER = 3;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.ServiceDescriptorProto parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.ServiceDescriptorProto(var1, var2);
         }
      };
      private static final DescriptorProtos.ServiceDescriptorProto defaultInstance;
      private static final long serialVersionUID = 0L;
      private int bitField0_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private List method_;
      private Object name_;
      private DescriptorProtos.ServiceOptions options_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.ServiceDescriptorProto var0 = new DescriptorProtos.ServiceDescriptorProto(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private ServiceDescriptorProto(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      ServiceDescriptorProto(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private ServiceDescriptorProto(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      ServiceDescriptorProto(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private ServiceDescriptorProto(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.ServiceDescriptorProto getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
      }

      private void initFields() {
         this.name_ = "";
         this.method_ = Collections.emptyList();
         this.options_ = DescriptorProtos.ServiceOptions.getDefaultInstance();
      }

      public static DescriptorProtos.ServiceDescriptorProto.Builder newBuilder() {
         return DescriptorProtos.ServiceDescriptorProto.Builder.create();
      }

      public static DescriptorProtos.ServiceDescriptorProto.Builder newBuilder(DescriptorProtos.ServiceDescriptorProto var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.ServiceDescriptorProto parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.ServiceDescriptorProto)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.ServiceDescriptorProto getDefaultInstanceForType() {
         return defaultInstance;
      }

      public DescriptorProtos.MethodDescriptorProto getMethod(int var1) {
         return (DescriptorProtos.MethodDescriptorProto)this.method_.get(var1);
      }

      public int getMethodCount() {
         return this.method_.size();
      }

      public List getMethodList() {
         return this.method_;
      }

      public DescriptorProtos.MethodDescriptorProtoOrBuilder getMethodOrBuilder(int var1) {
         return (DescriptorProtos.MethodDescriptorProtoOrBuilder)this.method_.get(var1);
      }

      public List getMethodOrBuilderList() {
         return this.method_;
      }

      public String getName() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.name_ = var2;
            }

            return var2;
         }
      }

      public ByteString getNameBytes() {
         Object var1 = this.name_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.name_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.ServiceOptions getOptions() {
         return this.options_;
      }

      public DescriptorProtos.ServiceOptionsOrBuilder getOptionsOrBuilder() {
         return this.options_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            var1 = 0;
            if ((this.bitField0_ & 1) == 1) {
               var1 = 0 + CodedOutputStream.computeBytesSize(1, this.getNameBytes());
            }

            int var2;
            for(var2 = 0; var2 < this.method_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(2, (MessageLite)this.method_.get(var2));
            }

            var2 = var1;
            if ((this.bitField0_ & 2) == 2) {
               var2 = var1 + CodedOutputStream.computeMessageSize(3, this.options_);
            }

            var1 = var2 + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasName() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasOptions() {
         return (this.bitField0_ & 2) == 2;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.ServiceDescriptorProto.class, DescriptorProtos.ServiceDescriptorProto.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getMethodCount(); ++var3) {
               if (!this.getMethod(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (this.hasOptions() && !this.getOptions().isInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.ServiceDescriptorProto.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.ServiceDescriptorProto.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.ServiceDescriptorProto.Builder(var1);
      }

      public DescriptorProtos.ServiceDescriptorProto.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(1, this.getNameBytes());
         }

         for(int var2 = 0; var2 < this.method_.size(); ++var2) {
            var1.writeMessage(2, (MessageLite)this.method_.get(var2));
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeMessage(3, this.options_);
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.ServiceDescriptorProtoOrBuilder {
         private int bitField0_;
         private RepeatedFieldBuilder methodBuilder_;
         private List method_;
         private Object name_;
         private SingleFieldBuilder optionsBuilder_;
         private DescriptorProtos.ServiceOptions options_;

         private Builder() {
            this.name_ = "";
            this.method_ = Collections.emptyList();
            this.options_ = DescriptorProtos.ServiceOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.name_ = "";
            this.method_ = Collections.emptyList();
            this.options_ = DescriptorProtos.ServiceOptions.getDefaultInstance();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.ServiceDescriptorProto.Builder create() {
            return new DescriptorProtos.ServiceDescriptorProto.Builder();
         }

         private void ensureMethodIsMutable() {
            if ((this.bitField0_ & 2) != 2) {
               this.method_ = new ArrayList(this.method_);
               this.bitField0_ |= 2;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
         }

         private RepeatedFieldBuilder getMethodFieldBuilder() {
            if (this.methodBuilder_ == null) {
               List var2 = this.method_;
               boolean var1;
               if ((this.bitField0_ & 2) == 2) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               this.methodBuilder_ = new RepeatedFieldBuilder(var2, var1, this.getParentForChildren(), this.isClean());
               this.method_ = null;
            }

            return this.methodBuilder_;
         }

         private SingleFieldBuilder getOptionsFieldBuilder() {
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

         public DescriptorProtos.ServiceDescriptorProto.Builder addAllMethod(Iterable var1) {
            RepeatedFieldBuilder var2 = this.methodBuilder_;
            if (var2 == null) {
               this.ensureMethodIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.method_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder addMethod(int var1, DescriptorProtos.MethodDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.methodBuilder_;
            if (var3 == null) {
               this.ensureMethodIsMutable();
               this.method_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder addMethod(int var1, DescriptorProtos.MethodDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.methodBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureMethodIsMutable();
                  this.method_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder addMethod(DescriptorProtos.MethodDescriptorProto.Builder var1) {
            RepeatedFieldBuilder var2 = this.methodBuilder_;
            if (var2 == null) {
               this.ensureMethodIsMutable();
               this.method_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder addMethod(DescriptorProtos.MethodDescriptorProto var1) {
            RepeatedFieldBuilder var2 = this.methodBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureMethodIsMutable();
                  this.method_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.MethodDescriptorProto.Builder addMethodBuilder() {
            return (DescriptorProtos.MethodDescriptorProto.Builder)this.getMethodFieldBuilder().addBuilder(DescriptorProtos.MethodDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.MethodDescriptorProto.Builder addMethodBuilder(int var1) {
            return (DescriptorProtos.MethodDescriptorProto.Builder)this.getMethodFieldBuilder().addBuilder(var1, DescriptorProtos.MethodDescriptorProto.getDefaultInstance());
         }

         public DescriptorProtos.ServiceDescriptorProto build() {
            DescriptorProtos.ServiceDescriptorProto var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.ServiceDescriptorProto buildPartial() {
            DescriptorProtos.ServiceDescriptorProto var4 = new DescriptorProtos.ServiceDescriptorProto(this);
            int var3 = this.bitField0_;
            int var1 = 0;
            if ((var3 & 1) == 1) {
               var1 = 0 | 1;
            }

            var4.name_ = this.name_;
            RepeatedFieldBuilder var5 = this.methodBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 2) == 2) {
                  this.method_ = Collections.unmodifiableList(this.method_);
                  this.bitField0_ &= -3;
               }

               var4.method_ = this.method_;
            } else {
               var4.method_ = var5.build();
            }

            int var2 = var1;
            if ((var3 & 4) == 4) {
               var2 = var1 | 2;
            }

            SingleFieldBuilder var6 = this.optionsBuilder_;
            if (var6 == null) {
               var4.options_ = this.options_;
            } else {
               var4.options_ = (DescriptorProtos.ServiceOptions)var6.build();
            }

            var4.bitField0_ = var2;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder clear() {
            super.clear();
            this.name_ = "";
            this.bitField0_ &= -2;
            RepeatedFieldBuilder var1 = this.methodBuilder_;
            if (var1 == null) {
               this.method_ = Collections.emptyList();
               this.bitField0_ &= -3;
            } else {
               var1.clear();
            }

            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = DescriptorProtos.ServiceOptions.getDefaultInstance();
            } else {
               var2.clear();
            }

            this.bitField0_ &= -5;
            return this;
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder clearMethod() {
            RepeatedFieldBuilder var1 = this.methodBuilder_;
            if (var1 == null) {
               this.method_ = Collections.emptyList();
               this.bitField0_ &= -3;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder clearName() {
            this.bitField0_ &= -2;
            this.name_ = DescriptorProtos.ServiceDescriptorProto.getDefaultInstance().getName();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder clearOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            if (var1 == null) {
               this.options_ = DescriptorProtos.ServiceOptions.getDefaultInstance();
               this.onChanged();
            } else {
               var1.clear();
            }

            this.bitField0_ &= -5;
            return this;
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.ServiceDescriptorProto getDefaultInstanceForType() {
            return DescriptorProtos.ServiceDescriptorProto.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
         }

         public DescriptorProtos.MethodDescriptorProto getMethod(int var1) {
            RepeatedFieldBuilder var2 = this.methodBuilder_;
            return var2 == null ? (DescriptorProtos.MethodDescriptorProto)this.method_.get(var1) : (DescriptorProtos.MethodDescriptorProto)var2.getMessage(var1);
         }

         public DescriptorProtos.MethodDescriptorProto.Builder getMethodBuilder(int var1) {
            return (DescriptorProtos.MethodDescriptorProto.Builder)this.getMethodFieldBuilder().getBuilder(var1);
         }

         public List getMethodBuilderList() {
            return this.getMethodFieldBuilder().getBuilderList();
         }

         public int getMethodCount() {
            RepeatedFieldBuilder var1 = this.methodBuilder_;
            return var1 == null ? this.method_.size() : var1.getCount();
         }

         public List getMethodList() {
            RepeatedFieldBuilder var1 = this.methodBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.method_) : var1.getMessageList();
         }

         public DescriptorProtos.MethodDescriptorProtoOrBuilder getMethodOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.methodBuilder_;
            return var2 == null ? (DescriptorProtos.MethodDescriptorProtoOrBuilder)this.method_.get(var1) : (DescriptorProtos.MethodDescriptorProtoOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getMethodOrBuilderList() {
            RepeatedFieldBuilder var1 = this.methodBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.method_);
         }

         public String getName() {
            Object var1 = this.name_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.name_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getNameBytes() {
            Object var1 = this.name_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.name_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.ServiceOptions getOptions() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return var1 == null ? this.options_ : (DescriptorProtos.ServiceOptions)var1.getMessage();
         }

         public DescriptorProtos.ServiceOptions.Builder getOptionsBuilder() {
            this.bitField0_ |= 4;
            this.onChanged();
            return (DescriptorProtos.ServiceOptions.Builder)this.getOptionsFieldBuilder().getBuilder();
         }

         public DescriptorProtos.ServiceOptionsOrBuilder getOptionsOrBuilder() {
            SingleFieldBuilder var1 = this.optionsBuilder_;
            return (DescriptorProtos.ServiceOptionsOrBuilder)(var1 != null ? (DescriptorProtos.ServiceOptionsOrBuilder)var1.getMessageOrBuilder() : this.options_);
         }

         public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasOptions() {
            return (this.bitField0_ & 4) == 4;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.ServiceDescriptorProto.class, DescriptorProtos.ServiceDescriptorProto.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getMethodCount(); ++var1) {
               if (!this.getMethod(var1).isInitialized()) {
                  return false;
               }
            }

            if (this.hasOptions() && !this.getOptions().isInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.ServiceDescriptorProto var3 = (DescriptorProtos.ServiceDescriptorProto)var4;

            DescriptorProtos.ServiceDescriptorProto var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.ServiceDescriptorProto)DescriptorProtos.ServiceDescriptorProto.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.ServiceDescriptorProto)var4;

                  try {
                     var21 = (DescriptorProtos.ServiceDescriptorProto)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder mergeFrom(DescriptorProtos.ServiceDescriptorProto var1) {
            if (var1 == DescriptorProtos.ServiceDescriptorProto.getDefaultInstance()) {
               return this;
            } else {
               if (var1.hasName()) {
                  this.bitField0_ |= 1;
                  this.name_ = var1.name_;
                  this.onChanged();
               }

               if (this.methodBuilder_ == null) {
                  if (!var1.method_.isEmpty()) {
                     if (this.method_.isEmpty()) {
                        this.method_ = var1.method_;
                        this.bitField0_ &= -3;
                     } else {
                        this.ensureMethodIsMutable();
                        this.method_.addAll(var1.method_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.method_.isEmpty()) {
                  if (this.methodBuilder_.isEmpty()) {
                     this.methodBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.methodBuilder_ = null;
                     this.method_ = var1.method_;
                     this.bitField0_ &= -3;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getMethodFieldBuilder();
                     }

                     this.methodBuilder_ = var2;
                  } else {
                     this.methodBuilder_.addAllMessages(var1.method_);
                  }
               }

               if (var1.hasOptions()) {
                  this.mergeOptions(var1.getOptions());
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.ServiceDescriptorProto) {
               return this.mergeFrom((DescriptorProtos.ServiceDescriptorProto)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder mergeOptions(DescriptorProtos.ServiceOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if ((this.bitField0_ & 4) == 4 && this.options_ != DescriptorProtos.ServiceOptions.getDefaultInstance()) {
                  this.options_ = DescriptorProtos.ServiceOptions.newBuilder(this.options_).mergeFrom(var1).buildPartial();
               } else {
                  this.options_ = var1;
               }

               this.onChanged();
            } else {
               var2.mergeFrom(var1);
            }

            this.bitField0_ |= 4;
            return this;
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder removeMethod(int var1) {
            RepeatedFieldBuilder var2 = this.methodBuilder_;
            if (var2 == null) {
               this.ensureMethodIsMutable();
               this.method_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder setMethod(int var1, DescriptorProtos.MethodDescriptorProto.Builder var2) {
            RepeatedFieldBuilder var3 = this.methodBuilder_;
            if (var3 == null) {
               this.ensureMethodIsMutable();
               this.method_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder setMethod(int var1, DescriptorProtos.MethodDescriptorProto var2) {
            RepeatedFieldBuilder var3 = this.methodBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureMethodIsMutable();
                  this.method_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder setName(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder setNameBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 1;
               this.name_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder setOptions(DescriptorProtos.ServiceOptions.Builder var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               this.options_ = var1.build();
               this.onChanged();
            } else {
               var2.setMessage(var1.build());
            }

            this.bitField0_ |= 4;
            return this;
         }

         public DescriptorProtos.ServiceDescriptorProto.Builder setOptions(DescriptorProtos.ServiceOptions var1) {
            SingleFieldBuilder var2 = this.optionsBuilder_;
            if (var2 == null) {
               if (var1 == null) {
                  throw null;
               }

               this.options_ = var1;
               this.onChanged();
            } else {
               var2.setMessage(var1);
            }

            this.bitField0_ |= 4;
            return this;
         }
      }
   }

   public interface ServiceDescriptorProtoOrBuilder extends MessageOrBuilder {
      DescriptorProtos.MethodDescriptorProto getMethod(int var1);

      int getMethodCount();

      List getMethodList();

      DescriptorProtos.MethodDescriptorProtoOrBuilder getMethodOrBuilder(int var1);

      List getMethodOrBuilderList();

      String getName();

      ByteString getNameBytes();

      DescriptorProtos.ServiceOptions getOptions();

      DescriptorProtos.ServiceOptionsOrBuilder getOptionsOrBuilder();

      boolean hasName();

      boolean hasOptions();
   }

   public static final class ServiceOptions extends GeneratedMessage.ExtendableMessage implements DescriptorProtos.ServiceOptionsOrBuilder {
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.ServiceOptions parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.ServiceOptions(var1, var2);
         }
      };
      public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
      private static final DescriptorProtos.ServiceOptions defaultInstance;
      private static final long serialVersionUID = 0L;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private List uninterpretedOption_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.ServiceOptions var0 = new DescriptorProtos.ServiceOptions(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private ServiceOptions(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      ServiceOptions(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private ServiceOptions(GeneratedMessage.ExtendableBuilder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      ServiceOptions(GeneratedMessage.ExtendableBuilder var1, Object var2) {
         this(var1);
      }

      private ServiceOptions(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.ServiceOptions getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor;
      }

      private void initFields() {
         this.uninterpretedOption_ = Collections.emptyList();
      }

      public static DescriptorProtos.ServiceOptions.Builder newBuilder() {
         return DescriptorProtos.ServiceOptions.Builder.create();
      }

      public static DescriptorProtos.ServiceOptions.Builder newBuilder(DescriptorProtos.ServiceOptions var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.ServiceOptions parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.ServiceOptions parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.ServiceOptions parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.ServiceOptions parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.ServiceOptions parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.ServiceOptions parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.ServiceOptions parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.ServiceOptions parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.ServiceOptions parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.ServiceOptions parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.ServiceOptions)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.ServiceOptions getDefaultInstanceForType() {
         return defaultInstance;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;

            for(var1 = 0; var1 < this.uninterpretedOption_.size(); ++var1) {
               var2 += CodedOutputStream.computeMessageSize(999, (MessageLite)this.uninterpretedOption_.get(var1));
            }

            var1 = var2 + this.extensionsSerializedSize() + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
         return (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1);
      }

      public int getUninterpretedOptionCount() {
         return this.uninterpretedOption_.size();
      }

      public List getUninterpretedOptionList() {
         return this.uninterpretedOption_;
      }

      public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
         return (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1);
      }

      public List getUninterpretedOptionOrBuilderList() {
         return this.uninterpretedOption_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_ServiceOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.ServiceOptions.class, DescriptorProtos.ServiceOptions.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getUninterpretedOptionCount(); ++var3) {
               if (!this.getUninterpretedOption(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }
      }

      public DescriptorProtos.ServiceOptions.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.ServiceOptions.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.ServiceOptions.Builder(var1);
      }

      public DescriptorProtos.ServiceOptions.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();
         GeneratedMessage.ExtendableMessage.ExtensionWriter var3 = this.newExtensionWriter();

         for(int var2 = 0; var2 < this.uninterpretedOption_.size(); ++var2) {
            var1.writeMessage(999, (MessageLite)this.uninterpretedOption_.get(var2));
         }

         var3.writeUntil(536870912, var1);
         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.ExtendableBuilder implements DescriptorProtos.ServiceOptionsOrBuilder {
         private int bitField0_;
         private RepeatedFieldBuilder uninterpretedOptionBuilder_;
         private List uninterpretedOption_;

         private Builder() {
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.uninterpretedOption_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.ServiceOptions.Builder create() {
            return new DescriptorProtos.ServiceOptions.Builder();
         }

         private void ensureUninterpretedOptionIsMutable() {
            if ((this.bitField0_ & 1) != 1) {
               this.uninterpretedOption_ = new ArrayList(this.uninterpretedOption_);
               this.bitField0_ |= 1;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor;
         }

         private RepeatedFieldBuilder getUninterpretedOptionFieldBuilder() {
            if (this.uninterpretedOptionBuilder_ == null) {
               List var3 = this.uninterpretedOption_;
               int var1 = this.bitField0_;
               boolean var2 = true;
               if ((var1 & 1) != 1) {
                  var2 = false;
               }

               this.uninterpretedOptionBuilder_ = new RepeatedFieldBuilder(var3, var2, this.getParentForChildren(), this.isClean());
               this.uninterpretedOption_ = null;
            }

            return this.uninterpretedOptionBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getUninterpretedOptionFieldBuilder();
            }

         }

         public DescriptorProtos.ServiceOptions.Builder addAllUninterpretedOption(Iterable var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               GeneratedMessage.ExtendableBuilder.addAll(var1, this.uninterpretedOption_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder addUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption.Builder var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder addUninterpretedOption(DescriptorProtos.UninterpretedOption var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().addBuilder(var1, DescriptorProtos.UninterpretedOption.getDefaultInstance());
         }

         public DescriptorProtos.ServiceOptions build() {
            DescriptorProtos.ServiceOptions var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.ServiceOptions buildPartial() {
            DescriptorProtos.ServiceOptions var2 = new DescriptorProtos.ServiceOptions(this);
            int var1 = this.bitField0_;
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if ((this.bitField0_ & 1) == 1) {
                  this.uninterpretedOption_ = Collections.unmodifiableList(this.uninterpretedOption_);
                  this.bitField0_ &= -2;
               }

               var2.uninterpretedOption_ = this.uninterpretedOption_;
            } else {
               var2.uninterpretedOption_ = var3.build();
            }

            this.onBuilt();
            return var2;
         }

         public DescriptorProtos.ServiceOptions.Builder clear() {
            super.clear();
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -2;
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder clearUninterpretedOption() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            if (var1 == null) {
               this.uninterpretedOption_ = Collections.emptyList();
               this.bitField0_ &= -2;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.ServiceOptions getDefaultInstanceForType() {
            return DescriptorProtos.ServiceOptions.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor;
         }

         public DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOption)var2.getMessage(var1);
         }

         public DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.Builder)this.getUninterpretedOptionFieldBuilder().getBuilder(var1);
         }

         public List getUninterpretedOptionBuilderList() {
            return this.getUninterpretedOptionFieldBuilder().getBuilderList();
         }

         public int getUninterpretedOptionCount() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? this.uninterpretedOption_.size() : var1.getCount();
         }

         public List getUninterpretedOptionList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.uninterpretedOption_) : var1.getMessageList();
         }

         public DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOptionOrBuilder)this.uninterpretedOption_.get(var1) : (DescriptorProtos.UninterpretedOptionOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getUninterpretedOptionOrBuilderList() {
            RepeatedFieldBuilder var1 = this.uninterpretedOptionBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.uninterpretedOption_);
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_ServiceOptions_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.ServiceOptions.class, DescriptorProtos.ServiceOptions.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getUninterpretedOptionCount(); ++var1) {
               if (!this.getUninterpretedOption(var1).isInitialized()) {
                  return false;
               }
            }

            if (!this.extensionsAreInitialized()) {
               return false;
            } else {
               return true;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.ServiceOptions var3 = (DescriptorProtos.ServiceOptions)var4;

            DescriptorProtos.ServiceOptions var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.ServiceOptions)DescriptorProtos.ServiceOptions.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.ServiceOptions)var4;

                  try {
                     var21 = (DescriptorProtos.ServiceOptions)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.ServiceOptions.Builder mergeFrom(DescriptorProtos.ServiceOptions var1) {
            if (var1 == DescriptorProtos.ServiceOptions.getDefaultInstance()) {
               return this;
            } else {
               if (this.uninterpretedOptionBuilder_ == null) {
                  if (!var1.uninterpretedOption_.isEmpty()) {
                     if (this.uninterpretedOption_.isEmpty()) {
                        this.uninterpretedOption_ = var1.uninterpretedOption_;
                        this.bitField0_ &= -2;
                     } else {
                        this.ensureUninterpretedOptionIsMutable();
                        this.uninterpretedOption_.addAll(var1.uninterpretedOption_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.uninterpretedOption_.isEmpty()) {
                  if (this.uninterpretedOptionBuilder_.isEmpty()) {
                     this.uninterpretedOptionBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.uninterpretedOptionBuilder_ = null;
                     this.uninterpretedOption_ = var1.uninterpretedOption_;
                     this.bitField0_ &= -2;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getUninterpretedOptionFieldBuilder();
                     }

                     this.uninterpretedOptionBuilder_ = var2;
                  } else {
                     this.uninterpretedOptionBuilder_.addAllMessages(var1.uninterpretedOption_);
                  }
               }

               this.mergeExtensionFields(var1);
               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.ServiceOptions) {
               return this.mergeFrom((DescriptorProtos.ServiceOptions)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder removeUninterpretedOption(int var1) {
            RepeatedFieldBuilder var2 = this.uninterpretedOptionBuilder_;
            if (var2 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption.Builder var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               this.ensureUninterpretedOptionIsMutable();
               this.uninterpretedOption_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.ServiceOptions.Builder setUninterpretedOption(int var1, DescriptorProtos.UninterpretedOption var2) {
            RepeatedFieldBuilder var3 = this.uninterpretedOptionBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureUninterpretedOptionIsMutable();
                  this.uninterpretedOption_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }
   }

   public interface ServiceOptionsOrBuilder extends GeneratedMessage.ExtendableMessageOrBuilder {
      DescriptorProtos.UninterpretedOption getUninterpretedOption(int var1);

      int getUninterpretedOptionCount();

      List getUninterpretedOptionList();

      DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(int var1);

      List getUninterpretedOptionOrBuilderList();
   }

   public static final class SourceCodeInfo extends GeneratedMessage implements DescriptorProtos.SourceCodeInfoOrBuilder {
      public static final int LOCATION_FIELD_NUMBER = 1;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.SourceCodeInfo parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.SourceCodeInfo(var1, var2);
         }
      };
      private static final DescriptorProtos.SourceCodeInfo defaultInstance;
      private static final long serialVersionUID = 0L;
      private List location_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.SourceCodeInfo var0 = new DescriptorProtos.SourceCodeInfo(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private SourceCodeInfo(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      SourceCodeInfo(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private SourceCodeInfo(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      SourceCodeInfo(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private SourceCodeInfo(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.SourceCodeInfo getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor;
      }

      private void initFields() {
         this.location_ = Collections.emptyList();
      }

      public static DescriptorProtos.SourceCodeInfo.Builder newBuilder() {
         return DescriptorProtos.SourceCodeInfo.Builder.create();
      }

      public static DescriptorProtos.SourceCodeInfo.Builder newBuilder(DescriptorProtos.SourceCodeInfo var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.SourceCodeInfo parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.SourceCodeInfo parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.SourceCodeInfo parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.SourceCodeInfo parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.SourceCodeInfo parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.SourceCodeInfo parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.SourceCodeInfo parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.SourceCodeInfo parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.SourceCodeInfo parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.SourceCodeInfo parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.SourceCodeInfo)PARSER.parseFrom(var0, var1);
      }

      public DescriptorProtos.SourceCodeInfo getDefaultInstanceForType() {
         return defaultInstance;
      }

      public DescriptorProtos.SourceCodeInfo.Location getLocation(int var1) {
         return (DescriptorProtos.SourceCodeInfo.Location)this.location_.get(var1);
      }

      public int getLocationCount() {
         return this.location_.size();
      }

      public List getLocationList() {
         return this.location_;
      }

      public DescriptorProtos.SourceCodeInfo.LocationOrBuilder getLocationOrBuilder(int var1) {
         return (DescriptorProtos.SourceCodeInfo.LocationOrBuilder)this.location_.get(var1);
      }

      public List getLocationOrBuilderList() {
         return this.location_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            int var2 = 0;

            for(var1 = 0; var1 < this.location_.size(); ++var1) {
               var2 += CodedOutputStream.computeMessageSize(1, (MessageLite)this.location_.get(var1));
            }

            var1 = var2 + this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.SourceCodeInfo.class, DescriptorProtos.SourceCodeInfo.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         if (var1 != -1) {
            return var1 == 1;
         } else {
            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public DescriptorProtos.SourceCodeInfo.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.SourceCodeInfo.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.SourceCodeInfo.Builder(var1);
      }

      public DescriptorProtos.SourceCodeInfo.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();

         for(int var2 = 0; var2 < this.location_.size(); ++var2) {
            var1.writeMessage(1, (MessageLite)this.location_.get(var2));
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.SourceCodeInfoOrBuilder {
         private int bitField0_;
         private RepeatedFieldBuilder locationBuilder_;
         private List location_;

         private Builder() {
            this.location_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.location_ = Collections.emptyList();
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.SourceCodeInfo.Builder create() {
            return new DescriptorProtos.SourceCodeInfo.Builder();
         }

         private void ensureLocationIsMutable() {
            if ((this.bitField0_ & 1) != 1) {
               this.location_ = new ArrayList(this.location_);
               this.bitField0_ |= 1;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor;
         }

         private RepeatedFieldBuilder getLocationFieldBuilder() {
            if (this.locationBuilder_ == null) {
               List var3 = this.location_;
               int var1 = this.bitField0_;
               boolean var2 = true;
               if ((var1 & 1) != 1) {
                  var2 = false;
               }

               this.locationBuilder_ = new RepeatedFieldBuilder(var3, var2, this.getParentForChildren(), this.isClean());
               this.location_ = null;
            }

            return this.locationBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getLocationFieldBuilder();
            }

         }

         public DescriptorProtos.SourceCodeInfo.Builder addAllLocation(Iterable var1) {
            RepeatedFieldBuilder var2 = this.locationBuilder_;
            if (var2 == null) {
               this.ensureLocationIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.location_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder addLocation(int var1, DescriptorProtos.SourceCodeInfo.Location.Builder var2) {
            RepeatedFieldBuilder var3 = this.locationBuilder_;
            if (var3 == null) {
               this.ensureLocationIsMutable();
               this.location_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder addLocation(int var1, DescriptorProtos.SourceCodeInfo.Location var2) {
            RepeatedFieldBuilder var3 = this.locationBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureLocationIsMutable();
                  this.location_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder addLocation(DescriptorProtos.SourceCodeInfo.Location.Builder var1) {
            RepeatedFieldBuilder var2 = this.locationBuilder_;
            if (var2 == null) {
               this.ensureLocationIsMutable();
               this.location_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder addLocation(DescriptorProtos.SourceCodeInfo.Location var1) {
            RepeatedFieldBuilder var2 = this.locationBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureLocationIsMutable();
                  this.location_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Location.Builder addLocationBuilder() {
            return (DescriptorProtos.SourceCodeInfo.Location.Builder)this.getLocationFieldBuilder().addBuilder(DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance());
         }

         public DescriptorProtos.SourceCodeInfo.Location.Builder addLocationBuilder(int var1) {
            return (DescriptorProtos.SourceCodeInfo.Location.Builder)this.getLocationFieldBuilder().addBuilder(var1, DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance());
         }

         public DescriptorProtos.SourceCodeInfo build() {
            DescriptorProtos.SourceCodeInfo var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.SourceCodeInfo buildPartial() {
            DescriptorProtos.SourceCodeInfo var2 = new DescriptorProtos.SourceCodeInfo(this);
            int var1 = this.bitField0_;
            RepeatedFieldBuilder var3 = this.locationBuilder_;
            if (var3 == null) {
               if ((this.bitField0_ & 1) == 1) {
                  this.location_ = Collections.unmodifiableList(this.location_);
                  this.bitField0_ &= -2;
               }

               var2.location_ = this.location_;
            } else {
               var2.location_ = var3.build();
            }

            this.onBuilt();
            return var2;
         }

         public DescriptorProtos.SourceCodeInfo.Builder clear() {
            super.clear();
            RepeatedFieldBuilder var1 = this.locationBuilder_;
            if (var1 == null) {
               this.location_ = Collections.emptyList();
               this.bitField0_ &= -2;
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder clearLocation() {
            RepeatedFieldBuilder var1 = this.locationBuilder_;
            if (var1 == null) {
               this.location_ = Collections.emptyList();
               this.bitField0_ &= -2;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public DescriptorProtos.SourceCodeInfo getDefaultInstanceForType() {
            return DescriptorProtos.SourceCodeInfo.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor;
         }

         public DescriptorProtos.SourceCodeInfo.Location getLocation(int var1) {
            RepeatedFieldBuilder var2 = this.locationBuilder_;
            return var2 == null ? (DescriptorProtos.SourceCodeInfo.Location)this.location_.get(var1) : (DescriptorProtos.SourceCodeInfo.Location)var2.getMessage(var1);
         }

         public DescriptorProtos.SourceCodeInfo.Location.Builder getLocationBuilder(int var1) {
            return (DescriptorProtos.SourceCodeInfo.Location.Builder)this.getLocationFieldBuilder().getBuilder(var1);
         }

         public List getLocationBuilderList() {
            return this.getLocationFieldBuilder().getBuilderList();
         }

         public int getLocationCount() {
            RepeatedFieldBuilder var1 = this.locationBuilder_;
            return var1 == null ? this.location_.size() : var1.getCount();
         }

         public List getLocationList() {
            RepeatedFieldBuilder var1 = this.locationBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.location_) : var1.getMessageList();
         }

         public DescriptorProtos.SourceCodeInfo.LocationOrBuilder getLocationOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.locationBuilder_;
            return var2 == null ? (DescriptorProtos.SourceCodeInfo.LocationOrBuilder)this.location_.get(var1) : (DescriptorProtos.SourceCodeInfo.LocationOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getLocationOrBuilderList() {
            RepeatedFieldBuilder var1 = this.locationBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.location_);
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.SourceCodeInfo.class, DescriptorProtos.SourceCodeInfo.Builder.class);
         }

         public final boolean isInitialized() {
            return true;
         }

         public DescriptorProtos.SourceCodeInfo.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.SourceCodeInfo var3 = (DescriptorProtos.SourceCodeInfo)var4;

            DescriptorProtos.SourceCodeInfo var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.SourceCodeInfo)DescriptorProtos.SourceCodeInfo.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.SourceCodeInfo)var4;

                  try {
                     var21 = (DescriptorProtos.SourceCodeInfo)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.SourceCodeInfo.Builder mergeFrom(DescriptorProtos.SourceCodeInfo var1) {
            if (var1 == DescriptorProtos.SourceCodeInfo.getDefaultInstance()) {
               return this;
            } else {
               if (this.locationBuilder_ == null) {
                  if (!var1.location_.isEmpty()) {
                     if (this.location_.isEmpty()) {
                        this.location_ = var1.location_;
                        this.bitField0_ &= -2;
                     } else {
                        this.ensureLocationIsMutable();
                        this.location_.addAll(var1.location_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.location_.isEmpty()) {
                  if (this.locationBuilder_.isEmpty()) {
                     this.locationBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.locationBuilder_ = null;
                     this.location_ = var1.location_;
                     this.bitField0_ &= -2;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getLocationFieldBuilder();
                     }

                     this.locationBuilder_ = var2;
                  } else {
                     this.locationBuilder_.addAllMessages(var1.location_);
                  }
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.SourceCodeInfo) {
               return this.mergeFrom((DescriptorProtos.SourceCodeInfo)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder removeLocation(int var1) {
            RepeatedFieldBuilder var2 = this.locationBuilder_;
            if (var2 == null) {
               this.ensureLocationIsMutable();
               this.location_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder setLocation(int var1, DescriptorProtos.SourceCodeInfo.Location.Builder var2) {
            RepeatedFieldBuilder var3 = this.locationBuilder_;
            if (var3 == null) {
               this.ensureLocationIsMutable();
               this.location_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Builder setLocation(int var1, DescriptorProtos.SourceCodeInfo.Location var2) {
            RepeatedFieldBuilder var3 = this.locationBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureLocationIsMutable();
                  this.location_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }
      }

      public static final class Location extends GeneratedMessage implements DescriptorProtos.SourceCodeInfo.LocationOrBuilder {
         public static final int LEADING_COMMENTS_FIELD_NUMBER = 3;
         public static Parser PARSER = new AbstractParser() {
            public DescriptorProtos.SourceCodeInfo.Location parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
               return new DescriptorProtos.SourceCodeInfo.Location(var1, var2);
            }
         };
         public static final int PATH_FIELD_NUMBER = 1;
         public static final int SPAN_FIELD_NUMBER = 2;
         public static final int TRAILING_COMMENTS_FIELD_NUMBER = 4;
         private static final DescriptorProtos.SourceCodeInfo.Location defaultInstance;
         private static final long serialVersionUID = 0L;
         private int bitField0_;
         private Object leadingComments_;
         private byte memoizedIsInitialized;
         private int memoizedSerializedSize;
         private int pathMemoizedSerializedSize;
         private List path_;
         private int spanMemoizedSerializedSize;
         private List span_;
         private Object trailingComments_;
         private final UnknownFieldSet unknownFields;

         static {
            DescriptorProtos.SourceCodeInfo.Location var0 = new DescriptorProtos.SourceCodeInfo.Location(true);
            defaultInstance = var0;
            var0.initFields();
         }

         private Location(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
            // $FF: Couldn't be decompiled
         }

         // $FF: synthetic method
         Location(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
            this(var1, var2);
         }

         private Location(GeneratedMessage.Builder var1) {
            super(var1);
            this.pathMemoizedSerializedSize = -1;
            this.spanMemoizedSerializedSize = -1;
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = var1.getUnknownFields();
         }

         // $FF: synthetic method
         Location(GeneratedMessage.Builder var1, Object var2) {
            this(var1);
         }

         private Location(boolean var1) {
            this.pathMemoizedSerializedSize = -1;
            this.spanMemoizedSerializedSize = -1;
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
         }

         public static DescriptorProtos.SourceCodeInfo.Location getDefaultInstance() {
            return defaultInstance;
         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
         }

         private void initFields() {
            this.path_ = Collections.emptyList();
            this.span_ = Collections.emptyList();
            this.leadingComments_ = "";
            this.trailingComments_ = "";
         }

         public static DescriptorProtos.SourceCodeInfo.Location.Builder newBuilder() {
            return DescriptorProtos.SourceCodeInfo.Location.Builder.create();
         }

         public static DescriptorProtos.SourceCodeInfo.Location.Builder newBuilder(DescriptorProtos.SourceCodeInfo.Location var0) {
            return newBuilder().mergeFrom(var0);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseDelimitedFrom(InputStream var0) throws IOException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseDelimitedFrom(var0);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseDelimitedFrom(var0, var1);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseFrom(ByteString var0) throws InvalidProtocolBufferException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseFrom(CodedInputStream var0) throws IOException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseFrom(InputStream var0) throws IOException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseFrom(byte[] var0) throws InvalidProtocolBufferException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.SourceCodeInfo.Location parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
            return (DescriptorProtos.SourceCodeInfo.Location)PARSER.parseFrom(var0, var1);
         }

         public DescriptorProtos.SourceCodeInfo.Location getDefaultInstanceForType() {
            return defaultInstance;
         }

         public String getLeadingComments() {
            Object var1 = this.leadingComments_;
            if (var1 instanceof String) {
               return (String)var1;
            } else {
               ByteString var3 = (ByteString)var1;
               String var2 = var3.toStringUtf8();
               if (var3.isValidUtf8()) {
                  this.leadingComments_ = var2;
               }

               return var2;
            }
         }

         public ByteString getLeadingCommentsBytes() {
            Object var1 = this.leadingComments_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.leadingComments_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public Parser getParserForType() {
            return PARSER;
         }

         public int getPath(int var1) {
            return (Integer)this.path_.get(var1);
         }

         public int getPathCount() {
            return this.path_.size();
         }

         public List getPathList() {
            return this.path_;
         }

         public int getSerializedSize() {
            int var1 = this.memoizedSerializedSize;
            if (var1 != -1) {
               return var1;
            } else {
               int var2 = 0;

               for(var1 = 0; var1 < this.path_.size(); ++var1) {
                  var2 += CodedOutputStream.computeInt32SizeNoTag((Integer)this.path_.get(var1));
               }

               int var3 = 0 + var2;
               var1 = var3;
               if (!this.getPathList().isEmpty()) {
                  var1 = var3 + 1 + CodedOutputStream.computeInt32SizeNoTag(var2);
               }

               this.pathMemoizedSerializedSize = var2;
               var2 = 0;

               for(var3 = 0; var3 < this.span_.size(); ++var3) {
                  var2 += CodedOutputStream.computeInt32SizeNoTag((Integer)this.span_.get(var3));
               }

               var3 = var1 + var2;
               var1 = var3;
               if (!this.getSpanList().isEmpty()) {
                  var1 = var3 + 1 + CodedOutputStream.computeInt32SizeNoTag(var2);
               }

               this.spanMemoizedSerializedSize = var2;
               var2 = var1;
               if ((this.bitField0_ & 1) == 1) {
                  var2 = var1 + CodedOutputStream.computeBytesSize(3, this.getLeadingCommentsBytes());
               }

               var1 = var2;
               if ((this.bitField0_ & 2) == 2) {
                  var1 = var2 + CodedOutputStream.computeBytesSize(4, this.getTrailingCommentsBytes());
               }

               var1 += this.getUnknownFields().getSerializedSize();
               this.memoizedSerializedSize = var1;
               return var1;
            }
         }

         public int getSpan(int var1) {
            return (Integer)this.span_.get(var1);
         }

         public int getSpanCount() {
            return this.span_.size();
         }

         public List getSpanList() {
            return this.span_;
         }

         public String getTrailingComments() {
            Object var1 = this.trailingComments_;
            if (var1 instanceof String) {
               return (String)var1;
            } else {
               ByteString var3 = (ByteString)var1;
               String var2 = var3.toStringUtf8();
               if (var3.isValidUtf8()) {
                  this.trailingComments_ = var2;
               }

               return var2;
            }
         }

         public ByteString getTrailingCommentsBytes() {
            Object var1 = this.trailingComments_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.trailingComments_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
         }

         public boolean hasLeadingComments() {
            return (this.bitField0_ & 1) == 1;
         }

         public boolean hasTrailingComments() {
            return (this.bitField0_ & 2) == 2;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.SourceCodeInfo.Location.class, DescriptorProtos.SourceCodeInfo.Location.Builder.class);
         }

         public final boolean isInitialized() {
            byte var1 = this.memoizedIsInitialized;
            if (var1 != -1) {
               return var1 == 1;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }

         public DescriptorProtos.SourceCodeInfo.Location.Builder newBuilderForType() {
            return newBuilder();
         }

         protected DescriptorProtos.SourceCodeInfo.Location.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
            return new DescriptorProtos.SourceCodeInfo.Location.Builder(var1);
         }

         public DescriptorProtos.SourceCodeInfo.Location.Builder toBuilder() {
            return newBuilder(this);
         }

         protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
         }

         public void writeTo(CodedOutputStream var1) throws IOException {
            this.getSerializedSize();
            if (this.getPathList().size() > 0) {
               var1.writeRawVarint32(10);
               var1.writeRawVarint32(this.pathMemoizedSerializedSize);
            }

            int var2;
            for(var2 = 0; var2 < this.path_.size(); ++var2) {
               var1.writeInt32NoTag((Integer)this.path_.get(var2));
            }

            if (this.getSpanList().size() > 0) {
               var1.writeRawVarint32(18);
               var1.writeRawVarint32(this.spanMemoizedSerializedSize);
            }

            for(var2 = 0; var2 < this.span_.size(); ++var2) {
               var1.writeInt32NoTag((Integer)this.span_.get(var2));
            }

            if ((this.bitField0_ & 1) == 1) {
               var1.writeBytes(3, this.getLeadingCommentsBytes());
            }

            if ((this.bitField0_ & 2) == 2) {
               var1.writeBytes(4, this.getTrailingCommentsBytes());
            }

            this.getUnknownFields().writeTo(var1);
         }

         public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.SourceCodeInfo.LocationOrBuilder {
            private int bitField0_;
            private Object leadingComments_;
            private List path_;
            private List span_;
            private Object trailingComments_;

            private Builder() {
               this.path_ = Collections.emptyList();
               this.span_ = Collections.emptyList();
               this.leadingComments_ = "";
               this.trailingComments_ = "";
               this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent var1) {
               super(var1);
               this.path_ = Collections.emptyList();
               this.span_ = Collections.emptyList();
               this.leadingComments_ = "";
               this.trailingComments_ = "";
               this.maybeForceBuilderInitialization();
            }

            // $FF: synthetic method
            Builder(GeneratedMessage.BuilderParent var1, Object var2) {
               this(var1);
            }

            private static DescriptorProtos.SourceCodeInfo.Location.Builder create() {
               return new DescriptorProtos.SourceCodeInfo.Location.Builder();
            }

            private void ensurePathIsMutable() {
               if ((this.bitField0_ & 1) != 1) {
                  this.path_ = new ArrayList(this.path_);
                  this.bitField0_ |= 1;
               }

            }

            private void ensureSpanIsMutable() {
               if ((this.bitField0_ & 2) != 2) {
                  this.span_ = new ArrayList(this.span_);
                  this.bitField0_ |= 2;
               }

            }

            public static final Descriptors.Descriptor getDescriptor() {
               return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
            }

            private void maybeForceBuilderInitialization() {
               boolean var1 = GeneratedMessage.alwaysUseFieldBuilders;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder addAllPath(Iterable var1) {
               this.ensurePathIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.path_);
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder addAllSpan(Iterable var1) {
               this.ensureSpanIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.span_);
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder addPath(int var1) {
               this.ensurePathIsMutable();
               this.path_.add(var1);
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder addSpan(int var1) {
               this.ensureSpanIsMutable();
               this.span_.add(var1);
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location build() {
               DescriptorProtos.SourceCodeInfo.Location var1 = this.buildPartial();
               if (var1.isInitialized()) {
                  return var1;
               } else {
                  throw newUninitializedMessageException(var1);
               }
            }

            public DescriptorProtos.SourceCodeInfo.Location buildPartial() {
               DescriptorProtos.SourceCodeInfo.Location var4 = new DescriptorProtos.SourceCodeInfo.Location(this);
               int var3 = this.bitField0_;
               int var1 = 0;
               if ((this.bitField0_ & 1) == 1) {
                  this.path_ = Collections.unmodifiableList(this.path_);
                  this.bitField0_ &= -2;
               }

               var4.path_ = this.path_;
               if ((this.bitField0_ & 2) == 2) {
                  this.span_ = Collections.unmodifiableList(this.span_);
                  this.bitField0_ &= -3;
               }

               var4.span_ = this.span_;
               if ((var3 & 4) == 4) {
                  var1 = 0 | 1;
               }

               var4.leadingComments_ = this.leadingComments_;
               int var2 = var1;
               if ((var3 & 8) == 8) {
                  var2 = var1 | 2;
               }

               var4.trailingComments_ = this.trailingComments_;
               var4.bitField0_ = var2;
               this.onBuilt();
               return var4;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder clear() {
               super.clear();
               this.path_ = Collections.emptyList();
               this.bitField0_ &= -2;
               this.span_ = Collections.emptyList();
               int var1 = this.bitField0_ & -3;
               this.bitField0_ = var1;
               this.leadingComments_ = "";
               var1 &= -5;
               this.bitField0_ = var1;
               this.trailingComments_ = "";
               this.bitField0_ = var1 & -9;
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder clearLeadingComments() {
               this.bitField0_ &= -5;
               this.leadingComments_ = DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance().getLeadingComments();
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder clearPath() {
               this.path_ = Collections.emptyList();
               this.bitField0_ &= -2;
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder clearSpan() {
               this.span_ = Collections.emptyList();
               this.bitField0_ &= -3;
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder clearTrailingComments() {
               this.bitField0_ &= -9;
               this.trailingComments_ = DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance().getTrailingComments();
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder clone() {
               return create().mergeFrom(this.buildPartial());
            }

            public DescriptorProtos.SourceCodeInfo.Location getDefaultInstanceForType() {
               return DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance();
            }

            public Descriptors.Descriptor getDescriptorForType() {
               return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
            }

            public String getLeadingComments() {
               Object var1 = this.leadingComments_;
               if (!(var1 instanceof String)) {
                  String var2 = ((ByteString)var1).toStringUtf8();
                  this.leadingComments_ = var2;
                  return var2;
               } else {
                  return (String)var1;
               }
            }

            public ByteString getLeadingCommentsBytes() {
               Object var1 = this.leadingComments_;
               if (var1 instanceof String) {
                  ByteString var2 = ByteString.copyFromUtf8((String)var1);
                  this.leadingComments_ = var2;
                  return var2;
               } else {
                  return (ByteString)var1;
               }
            }

            public int getPath(int var1) {
               return (Integer)this.path_.get(var1);
            }

            public int getPathCount() {
               return this.path_.size();
            }

            public List getPathList() {
               return Collections.unmodifiableList(this.path_);
            }

            public int getSpan(int var1) {
               return (Integer)this.span_.get(var1);
            }

            public int getSpanCount() {
               return this.span_.size();
            }

            public List getSpanList() {
               return Collections.unmodifiableList(this.span_);
            }

            public String getTrailingComments() {
               Object var1 = this.trailingComments_;
               if (!(var1 instanceof String)) {
                  String var2 = ((ByteString)var1).toStringUtf8();
                  this.trailingComments_ = var2;
                  return var2;
               } else {
                  return (String)var1;
               }
            }

            public ByteString getTrailingCommentsBytes() {
               Object var1 = this.trailingComments_;
               if (var1 instanceof String) {
                  ByteString var2 = ByteString.copyFromUtf8((String)var1);
                  this.trailingComments_ = var2;
                  return var2;
               } else {
                  return (ByteString)var1;
               }
            }

            public boolean hasLeadingComments() {
               return (this.bitField0_ & 4) == 4;
            }

            public boolean hasTrailingComments() {
               return (this.bitField0_ & 8) == 8;
            }

            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
               return DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.SourceCodeInfo.Location.class, DescriptorProtos.SourceCodeInfo.Location.Builder.class);
            }

            public final boolean isInitialized() {
               return true;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
               Object var4 = null;
               DescriptorProtos.SourceCodeInfo.Location var3 = (DescriptorProtos.SourceCodeInfo.Location)var4;

               DescriptorProtos.SourceCodeInfo.Location var21;
               label173: {
                  Throwable var10000;
                  label174: {
                     boolean var10001;
                     InvalidProtocolBufferException var23;
                     try {
                        try {
                           var21 = (DescriptorProtos.SourceCodeInfo.Location)DescriptorProtos.SourceCodeInfo.Location.PARSER.parsePartialFrom(var1, var2);
                           break label173;
                        } catch (InvalidProtocolBufferException var19) {
                           var23 = var19;
                        }
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label174;
                     }

                     var3 = (DescriptorProtos.SourceCodeInfo.Location)var4;

                     try {
                        var21 = (DescriptorProtos.SourceCodeInfo.Location)var23.getUnfinishedMessage();
                     } catch (Throwable var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label174;
                     }

                     var3 = var21;

                     label158:
                     try {
                        throw var23;
                     } catch (Throwable var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label158;
                     }
                  }

                  Throwable var22 = var10000;
                  if (var3 != null) {
                     this.mergeFrom(var3);
                  }

                  throw var22;
               }

               if (var21 != null) {
                  this.mergeFrom(var21);
               }

               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder mergeFrom(DescriptorProtos.SourceCodeInfo.Location var1) {
               if (var1 == DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance()) {
                  return this;
               } else {
                  if (!var1.path_.isEmpty()) {
                     if (this.path_.isEmpty()) {
                        this.path_ = var1.path_;
                        this.bitField0_ &= -2;
                     } else {
                        this.ensurePathIsMutable();
                        this.path_.addAll(var1.path_);
                     }

                     this.onChanged();
                  }

                  if (!var1.span_.isEmpty()) {
                     if (this.span_.isEmpty()) {
                        this.span_ = var1.span_;
                        this.bitField0_ &= -3;
                     } else {
                        this.ensureSpanIsMutable();
                        this.span_.addAll(var1.span_);
                     }

                     this.onChanged();
                  }

                  if (var1.hasLeadingComments()) {
                     this.bitField0_ |= 4;
                     this.leadingComments_ = var1.leadingComments_;
                     this.onChanged();
                  }

                  if (var1.hasTrailingComments()) {
                     this.bitField0_ |= 8;
                     this.trailingComments_ = var1.trailingComments_;
                     this.onChanged();
                  }

                  this.mergeUnknownFields(var1.getUnknownFields());
                  return this;
               }
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder mergeFrom(Message var1) {
               if (var1 instanceof DescriptorProtos.SourceCodeInfo.Location) {
                  return this.mergeFrom((DescriptorProtos.SourceCodeInfo.Location)var1);
               } else {
                  super.mergeFrom(var1);
                  return this;
               }
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder setLeadingComments(String var1) {
               if (var1 != null) {
                  this.bitField0_ |= 4;
                  this.leadingComments_ = var1;
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder setLeadingCommentsBytes(ByteString var1) {
               if (var1 != null) {
                  this.bitField0_ |= 4;
                  this.leadingComments_ = var1;
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder setPath(int var1, int var2) {
               this.ensurePathIsMutable();
               this.path_.set(var1, var2);
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder setSpan(int var1, int var2) {
               this.ensureSpanIsMutable();
               this.span_.set(var1, var2);
               this.onChanged();
               return this;
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder setTrailingComments(String var1) {
               if (var1 != null) {
                  this.bitField0_ |= 8;
                  this.trailingComments_ = var1;
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            }

            public DescriptorProtos.SourceCodeInfo.Location.Builder setTrailingCommentsBytes(ByteString var1) {
               if (var1 != null) {
                  this.bitField0_ |= 8;
                  this.trailingComments_ = var1;
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            }
         }
      }

      public interface LocationOrBuilder extends MessageOrBuilder {
         String getLeadingComments();

         ByteString getLeadingCommentsBytes();

         int getPath(int var1);

         int getPathCount();

         List getPathList();

         int getSpan(int var1);

         int getSpanCount();

         List getSpanList();

         String getTrailingComments();

         ByteString getTrailingCommentsBytes();

         boolean hasLeadingComments();

         boolean hasTrailingComments();
      }
   }

   public interface SourceCodeInfoOrBuilder extends MessageOrBuilder {
      DescriptorProtos.SourceCodeInfo.Location getLocation(int var1);

      int getLocationCount();

      List getLocationList();

      DescriptorProtos.SourceCodeInfo.LocationOrBuilder getLocationOrBuilder(int var1);

      List getLocationOrBuilderList();
   }

   public static final class UninterpretedOption extends GeneratedMessage implements DescriptorProtos.UninterpretedOptionOrBuilder {
      public static final int AGGREGATE_VALUE_FIELD_NUMBER = 8;
      public static final int DOUBLE_VALUE_FIELD_NUMBER = 6;
      public static final int IDENTIFIER_VALUE_FIELD_NUMBER = 3;
      public static final int NAME_FIELD_NUMBER = 2;
      public static final int NEGATIVE_INT_VALUE_FIELD_NUMBER = 5;
      public static Parser PARSER = new AbstractParser() {
         public DescriptorProtos.UninterpretedOption parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
            return new DescriptorProtos.UninterpretedOption(var1, var2);
         }
      };
      public static final int POSITIVE_INT_VALUE_FIELD_NUMBER = 4;
      public static final int STRING_VALUE_FIELD_NUMBER = 7;
      private static final DescriptorProtos.UninterpretedOption defaultInstance;
      private static final long serialVersionUID = 0L;
      private Object aggregateValue_;
      private int bitField0_;
      private double doubleValue_;
      private Object identifierValue_;
      private byte memoizedIsInitialized;
      private int memoizedSerializedSize;
      private List name_;
      private long negativeIntValue_;
      private long positiveIntValue_;
      private ByteString stringValue_;
      private final UnknownFieldSet unknownFields;

      static {
         DescriptorProtos.UninterpretedOption var0 = new DescriptorProtos.UninterpretedOption(true);
         defaultInstance = var0;
         var0.initFields();
      }

      private UninterpretedOption(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      UninterpretedOption(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
         this(var1, var2);
      }

      private UninterpretedOption(GeneratedMessage.Builder var1) {
         super(var1);
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = var1.getUnknownFields();
      }

      // $FF: synthetic method
      UninterpretedOption(GeneratedMessage.Builder var1, Object var2) {
         this(var1);
      }

      private UninterpretedOption(boolean var1) {
         this.memoizedIsInitialized = -1;
         this.memoizedSerializedSize = -1;
         this.unknownFields = UnknownFieldSet.getDefaultInstance();
      }

      public static DescriptorProtos.UninterpretedOption getDefaultInstance() {
         return defaultInstance;
      }

      public static final Descriptors.Descriptor getDescriptor() {
         return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor;
      }

      private void initFields() {
         this.name_ = Collections.emptyList();
         this.identifierValue_ = "";
         this.positiveIntValue_ = 0L;
         this.negativeIntValue_ = 0L;
         this.doubleValue_ = 0.0D;
         this.stringValue_ = ByteString.EMPTY;
         this.aggregateValue_ = "";
      }

      public static DescriptorProtos.UninterpretedOption.Builder newBuilder() {
         return DescriptorProtos.UninterpretedOption.Builder.create();
      }

      public static DescriptorProtos.UninterpretedOption.Builder newBuilder(DescriptorProtos.UninterpretedOption var0) {
         return newBuilder().mergeFrom(var0);
      }

      public static DescriptorProtos.UninterpretedOption parseDelimitedFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseDelimitedFrom(var0);
      }

      public static DescriptorProtos.UninterpretedOption parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseDelimitedFrom(var0, var1);
      }

      public static DescriptorProtos.UninterpretedOption parseFrom(ByteString var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.UninterpretedOption parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.UninterpretedOption parseFrom(CodedInputStream var0) throws IOException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.UninterpretedOption parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.UninterpretedOption parseFrom(InputStream var0) throws IOException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.UninterpretedOption parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseFrom(var0, var1);
      }

      public static DescriptorProtos.UninterpretedOption parseFrom(byte[] var0) throws InvalidProtocolBufferException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseFrom(var0);
      }

      public static DescriptorProtos.UninterpretedOption parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
         return (DescriptorProtos.UninterpretedOption)PARSER.parseFrom(var0, var1);
      }

      public String getAggregateValue() {
         Object var1 = this.aggregateValue_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.aggregateValue_ = var2;
            }

            return var2;
         }
      }

      public ByteString getAggregateValueBytes() {
         Object var1 = this.aggregateValue_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.aggregateValue_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.UninterpretedOption getDefaultInstanceForType() {
         return defaultInstance;
      }

      public double getDoubleValue() {
         return this.doubleValue_;
      }

      public String getIdentifierValue() {
         Object var1 = this.identifierValue_;
         if (var1 instanceof String) {
            return (String)var1;
         } else {
            ByteString var3 = (ByteString)var1;
            String var2 = var3.toStringUtf8();
            if (var3.isValidUtf8()) {
               this.identifierValue_ = var2;
            }

            return var2;
         }
      }

      public ByteString getIdentifierValueBytes() {
         Object var1 = this.identifierValue_;
         if (var1 instanceof String) {
            ByteString var2 = ByteString.copyFromUtf8((String)var1);
            this.identifierValue_ = var2;
            return var2;
         } else {
            return (ByteString)var1;
         }
      }

      public DescriptorProtos.UninterpretedOption.NamePart getName(int var1) {
         return (DescriptorProtos.UninterpretedOption.NamePart)this.name_.get(var1);
      }

      public int getNameCount() {
         return this.name_.size();
      }

      public List getNameList() {
         return this.name_;
      }

      public DescriptorProtos.UninterpretedOption.NamePartOrBuilder getNameOrBuilder(int var1) {
         return (DescriptorProtos.UninterpretedOption.NamePartOrBuilder)this.name_.get(var1);
      }

      public List getNameOrBuilderList() {
         return this.name_;
      }

      public long getNegativeIntValue() {
         return this.negativeIntValue_;
      }

      public Parser getParserForType() {
         return PARSER;
      }

      public long getPositiveIntValue() {
         return this.positiveIntValue_;
      }

      public int getSerializedSize() {
         int var1 = this.memoizedSerializedSize;
         if (var1 != -1) {
            return var1;
         } else {
            var1 = 0;

            int var2;
            for(var2 = 0; var2 < this.name_.size(); ++var2) {
               var1 += CodedOutputStream.computeMessageSize(2, (MessageLite)this.name_.get(var2));
            }

            var2 = var1;
            if ((this.bitField0_ & 1) == 1) {
               var2 = var1 + CodedOutputStream.computeBytesSize(3, this.getIdentifierValueBytes());
            }

            var1 = var2;
            if ((this.bitField0_ & 2) == 2) {
               var1 = var2 + CodedOutputStream.computeUInt64Size(4, this.positiveIntValue_);
            }

            var2 = var1;
            if ((this.bitField0_ & 4) == 4) {
               var2 = var1 + CodedOutputStream.computeInt64Size(5, this.negativeIntValue_);
            }

            var1 = var2;
            if ((this.bitField0_ & 8) == 8) {
               var1 = var2 + CodedOutputStream.computeDoubleSize(6, this.doubleValue_);
            }

            var2 = var1;
            if ((this.bitField0_ & 16) == 16) {
               var2 = var1 + CodedOutputStream.computeBytesSize(7, this.stringValue_);
            }

            var1 = var2;
            if ((this.bitField0_ & 32) == 32) {
               var1 = var2 + CodedOutputStream.computeBytesSize(8, this.getAggregateValueBytes());
            }

            var1 += this.getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = var1;
            return var1;
         }
      }

      public ByteString getStringValue() {
         return this.stringValue_;
      }

      public final UnknownFieldSet getUnknownFields() {
         return this.unknownFields;
      }

      public boolean hasAggregateValue() {
         return (this.bitField0_ & 32) == 32;
      }

      public boolean hasDoubleValue() {
         return (this.bitField0_ & 8) == 8;
      }

      public boolean hasIdentifierValue() {
         return (this.bitField0_ & 1) == 1;
      }

      public boolean hasNegativeIntValue() {
         return (this.bitField0_ & 4) == 4;
      }

      public boolean hasPositiveIntValue() {
         return (this.bitField0_ & 2) == 2;
      }

      public boolean hasStringValue() {
         return (this.bitField0_ & 16) == 16;
      }

      protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
         return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.UninterpretedOption.class, DescriptorProtos.UninterpretedOption.Builder.class);
      }

      public final boolean isInitialized() {
         byte var1 = this.memoizedIsInitialized;
         boolean var2 = false;
         if (var1 != -1) {
            if (var1 == 1) {
               var2 = true;
            }

            return var2;
         } else {
            for(int var3 = 0; var3 < this.getNameCount(); ++var3) {
               if (!this.getName(var3).isInitialized()) {
                  this.memoizedIsInitialized = 0;
                  return false;
               }
            }

            this.memoizedIsInitialized = 1;
            return true;
         }
      }

      public DescriptorProtos.UninterpretedOption.Builder newBuilderForType() {
         return newBuilder();
      }

      protected DescriptorProtos.UninterpretedOption.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
         return new DescriptorProtos.UninterpretedOption.Builder(var1);
      }

      public DescriptorProtos.UninterpretedOption.Builder toBuilder() {
         return newBuilder(this);
      }

      protected Object writeReplace() throws ObjectStreamException {
         return super.writeReplace();
      }

      public void writeTo(CodedOutputStream var1) throws IOException {
         this.getSerializedSize();

         for(int var2 = 0; var2 < this.name_.size(); ++var2) {
            var1.writeMessage(2, (MessageLite)this.name_.get(var2));
         }

         if ((this.bitField0_ & 1) == 1) {
            var1.writeBytes(3, this.getIdentifierValueBytes());
         }

         if ((this.bitField0_ & 2) == 2) {
            var1.writeUInt64(4, this.positiveIntValue_);
         }

         if ((this.bitField0_ & 4) == 4) {
            var1.writeInt64(5, this.negativeIntValue_);
         }

         if ((this.bitField0_ & 8) == 8) {
            var1.writeDouble(6, this.doubleValue_);
         }

         if ((this.bitField0_ & 16) == 16) {
            var1.writeBytes(7, this.stringValue_);
         }

         if ((this.bitField0_ & 32) == 32) {
            var1.writeBytes(8, this.getAggregateValueBytes());
         }

         this.getUnknownFields().writeTo(var1);
      }

      public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.UninterpretedOptionOrBuilder {
         private Object aggregateValue_;
         private int bitField0_;
         private double doubleValue_;
         private Object identifierValue_;
         private RepeatedFieldBuilder nameBuilder_;
         private List name_;
         private long negativeIntValue_;
         private long positiveIntValue_;
         private ByteString stringValue_;

         private Builder() {
            this.name_ = Collections.emptyList();
            this.identifierValue_ = "";
            this.stringValue_ = ByteString.EMPTY;
            this.aggregateValue_ = "";
            this.maybeForceBuilderInitialization();
         }

         private Builder(GeneratedMessage.BuilderParent var1) {
            super(var1);
            this.name_ = Collections.emptyList();
            this.identifierValue_ = "";
            this.stringValue_ = ByteString.EMPTY;
            this.aggregateValue_ = "";
            this.maybeForceBuilderInitialization();
         }

         // $FF: synthetic method
         Builder(GeneratedMessage.BuilderParent var1, Object var2) {
            this(var1);
         }

         private static DescriptorProtos.UninterpretedOption.Builder create() {
            return new DescriptorProtos.UninterpretedOption.Builder();
         }

         private void ensureNameIsMutable() {
            if ((this.bitField0_ & 1) != 1) {
               this.name_ = new ArrayList(this.name_);
               this.bitField0_ |= 1;
            }

         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor;
         }

         private RepeatedFieldBuilder getNameFieldBuilder() {
            if (this.nameBuilder_ == null) {
               List var3 = this.name_;
               int var1 = this.bitField0_;
               boolean var2 = true;
               if ((var1 & 1) != 1) {
                  var2 = false;
               }

               this.nameBuilder_ = new RepeatedFieldBuilder(var3, var2, this.getParentForChildren(), this.isClean());
               this.name_ = null;
            }

            return this.nameBuilder_;
         }

         private void maybeForceBuilderInitialization() {
            if (GeneratedMessage.alwaysUseFieldBuilders) {
               this.getNameFieldBuilder();
            }

         }

         public DescriptorProtos.UninterpretedOption.Builder addAllName(Iterable var1) {
            RepeatedFieldBuilder var2 = this.nameBuilder_;
            if (var2 == null) {
               this.ensureNameIsMutable();
               GeneratedMessage.Builder.addAll(var1, this.name_);
               this.onChanged();
               return this;
            } else {
               var2.addAllMessages(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addName(int var1, DescriptorProtos.UninterpretedOption.NamePart.Builder var2) {
            RepeatedFieldBuilder var3 = this.nameBuilder_;
            if (var3 == null) {
               this.ensureNameIsMutable();
               this.name_.add(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.addMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addName(int var1, DescriptorProtos.UninterpretedOption.NamePart var2) {
            RepeatedFieldBuilder var3 = this.nameBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureNameIsMutable();
                  this.name_.add(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.addMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addName(DescriptorProtos.UninterpretedOption.NamePart.Builder var1) {
            RepeatedFieldBuilder var2 = this.nameBuilder_;
            if (var2 == null) {
               this.ensureNameIsMutable();
               this.name_.add(var1.build());
               this.onChanged();
               return this;
            } else {
               var2.addMessage(var1.build());
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder addName(DescriptorProtos.UninterpretedOption.NamePart var1) {
            RepeatedFieldBuilder var2 = this.nameBuilder_;
            if (var2 == null) {
               if (var1 != null) {
                  this.ensureNameIsMutable();
                  this.name_.add(var1);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var2.addMessage(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.NamePart.Builder addNameBuilder() {
            return (DescriptorProtos.UninterpretedOption.NamePart.Builder)this.getNameFieldBuilder().addBuilder(DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption.NamePart.Builder addNameBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.NamePart.Builder)this.getNameFieldBuilder().addBuilder(var1, DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance());
         }

         public DescriptorProtos.UninterpretedOption build() {
            DescriptorProtos.UninterpretedOption var1 = this.buildPartial();
            if (var1.isInitialized()) {
               return var1;
            } else {
               throw newUninitializedMessageException(var1);
            }
         }

         public DescriptorProtos.UninterpretedOption buildPartial() {
            DescriptorProtos.UninterpretedOption var4 = new DescriptorProtos.UninterpretedOption(this);
            int var3 = this.bitField0_;
            int var2 = 0;
            RepeatedFieldBuilder var5 = this.nameBuilder_;
            if (var5 == null) {
               if ((this.bitField0_ & 1) == 1) {
                  this.name_ = Collections.unmodifiableList(this.name_);
                  this.bitField0_ &= -2;
               }

               var4.name_ = this.name_;
            } else {
               var4.name_ = var5.build();
            }

            if ((var3 & 2) == 2) {
               var2 = 0 | 1;
            }

            var4.identifierValue_ = this.identifierValue_;
            int var1 = var2;
            if ((var3 & 4) == 4) {
               var1 = var2 | 2;
            }

            var4.positiveIntValue_ = this.positiveIntValue_;
            var2 = var1;
            if ((var3 & 8) == 8) {
               var2 = var1 | 4;
            }

            var4.negativeIntValue_ = this.negativeIntValue_;
            var1 = var2;
            if ((var3 & 16) == 16) {
               var1 = var2 | 8;
            }

            var4.doubleValue_ = this.doubleValue_;
            var2 = var1;
            if ((var3 & 32) == 32) {
               var2 = var1 | 16;
            }

            var4.stringValue_ = this.stringValue_;
            var1 = var2;
            if ((var3 & 64) == 64) {
               var1 = var2 | 32;
            }

            var4.aggregateValue_ = this.aggregateValue_;
            var4.bitField0_ = var1;
            this.onBuilt();
            return var4;
         }

         public DescriptorProtos.UninterpretedOption.Builder clear() {
            super.clear();
            RepeatedFieldBuilder var2 = this.nameBuilder_;
            if (var2 == null) {
               this.name_ = Collections.emptyList();
               this.bitField0_ &= -2;
            } else {
               var2.clear();
            }

            this.identifierValue_ = "";
            int var1 = this.bitField0_ & -3;
            this.bitField0_ = var1;
            this.positiveIntValue_ = 0L;
            var1 &= -5;
            this.bitField0_ = var1;
            this.negativeIntValue_ = 0L;
            var1 &= -9;
            this.bitField0_ = var1;
            this.doubleValue_ = 0.0D;
            this.bitField0_ = var1 & -17;
            this.stringValue_ = ByteString.EMPTY;
            var1 = this.bitField0_ & -33;
            this.bitField0_ = var1;
            this.aggregateValue_ = "";
            this.bitField0_ = var1 & -65;
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder clearAggregateValue() {
            this.bitField0_ &= -65;
            this.aggregateValue_ = DescriptorProtos.UninterpretedOption.getDefaultInstance().getAggregateValue();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder clearDoubleValue() {
            this.bitField0_ &= -17;
            this.doubleValue_ = 0.0D;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder clearIdentifierValue() {
            this.bitField0_ &= -3;
            this.identifierValue_ = DescriptorProtos.UninterpretedOption.getDefaultInstance().getIdentifierValue();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder clearName() {
            RepeatedFieldBuilder var1 = this.nameBuilder_;
            if (var1 == null) {
               this.name_ = Collections.emptyList();
               this.bitField0_ &= -2;
               this.onChanged();
               return this;
            } else {
               var1.clear();
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder clearNegativeIntValue() {
            this.bitField0_ &= -9;
            this.negativeIntValue_ = 0L;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder clearPositiveIntValue() {
            this.bitField0_ &= -5;
            this.positiveIntValue_ = 0L;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder clearStringValue() {
            this.bitField0_ &= -33;
            this.stringValue_ = DescriptorProtos.UninterpretedOption.getDefaultInstance().getStringValue();
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder clone() {
            return create().mergeFrom(this.buildPartial());
         }

         public String getAggregateValue() {
            Object var1 = this.aggregateValue_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.aggregateValue_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getAggregateValueBytes() {
            Object var1 = this.aggregateValue_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.aggregateValue_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.UninterpretedOption getDefaultInstanceForType() {
            return DescriptorProtos.UninterpretedOption.getDefaultInstance();
         }

         public Descriptors.Descriptor getDescriptorForType() {
            return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor;
         }

         public double getDoubleValue() {
            return this.doubleValue_;
         }

         public String getIdentifierValue() {
            Object var1 = this.identifierValue_;
            if (!(var1 instanceof String)) {
               String var2 = ((ByteString)var1).toStringUtf8();
               this.identifierValue_ = var2;
               return var2;
            } else {
               return (String)var1;
            }
         }

         public ByteString getIdentifierValueBytes() {
            Object var1 = this.identifierValue_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.identifierValue_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public DescriptorProtos.UninterpretedOption.NamePart getName(int var1) {
            RepeatedFieldBuilder var2 = this.nameBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption.NamePart)this.name_.get(var1) : (DescriptorProtos.UninterpretedOption.NamePart)var2.getMessage(var1);
         }

         public DescriptorProtos.UninterpretedOption.NamePart.Builder getNameBuilder(int var1) {
            return (DescriptorProtos.UninterpretedOption.NamePart.Builder)this.getNameFieldBuilder().getBuilder(var1);
         }

         public List getNameBuilderList() {
            return this.getNameFieldBuilder().getBuilderList();
         }

         public int getNameCount() {
            RepeatedFieldBuilder var1 = this.nameBuilder_;
            return var1 == null ? this.name_.size() : var1.getCount();
         }

         public List getNameList() {
            RepeatedFieldBuilder var1 = this.nameBuilder_;
            return var1 == null ? Collections.unmodifiableList(this.name_) : var1.getMessageList();
         }

         public DescriptorProtos.UninterpretedOption.NamePartOrBuilder getNameOrBuilder(int var1) {
            RepeatedFieldBuilder var2 = this.nameBuilder_;
            return var2 == null ? (DescriptorProtos.UninterpretedOption.NamePartOrBuilder)this.name_.get(var1) : (DescriptorProtos.UninterpretedOption.NamePartOrBuilder)var2.getMessageOrBuilder(var1);
         }

         public List getNameOrBuilderList() {
            RepeatedFieldBuilder var1 = this.nameBuilder_;
            return var1 != null ? var1.getMessageOrBuilderList() : Collections.unmodifiableList(this.name_);
         }

         public long getNegativeIntValue() {
            return this.negativeIntValue_;
         }

         public long getPositiveIntValue() {
            return this.positiveIntValue_;
         }

         public ByteString getStringValue() {
            return this.stringValue_;
         }

         public boolean hasAggregateValue() {
            return (this.bitField0_ & 64) == 64;
         }

         public boolean hasDoubleValue() {
            return (this.bitField0_ & 16) == 16;
         }

         public boolean hasIdentifierValue() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasNegativeIntValue() {
            return (this.bitField0_ & 8) == 8;
         }

         public boolean hasPositiveIntValue() {
            return (this.bitField0_ & 4) == 4;
         }

         public boolean hasStringValue() {
            return (this.bitField0_ & 32) == 32;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.UninterpretedOption.class, DescriptorProtos.UninterpretedOption.Builder.class);
         }

         public final boolean isInitialized() {
            for(int var1 = 0; var1 < this.getNameCount(); ++var1) {
               if (!this.getName(var1).isInitialized()) {
                  return false;
               }
            }

            return true;
         }

         public DescriptorProtos.UninterpretedOption.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
            Object var4 = null;
            DescriptorProtos.UninterpretedOption var3 = (DescriptorProtos.UninterpretedOption)var4;

            DescriptorProtos.UninterpretedOption var21;
            label173: {
               Throwable var10000;
               label174: {
                  boolean var10001;
                  InvalidProtocolBufferException var23;
                  try {
                     try {
                        var21 = (DescriptorProtos.UninterpretedOption)DescriptorProtos.UninterpretedOption.PARSER.parsePartialFrom(var1, var2);
                        break label173;
                     } catch (InvalidProtocolBufferException var19) {
                        var23 = var19;
                     }
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label174;
                  }

                  var3 = (DescriptorProtos.UninterpretedOption)var4;

                  try {
                     var21 = (DescriptorProtos.UninterpretedOption)var23.getUnfinishedMessage();
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label174;
                  }

                  var3 = var21;

                  label158:
                  try {
                     throw var23;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label158;
                  }
               }

               Throwable var22 = var10000;
               if (var3 != null) {
                  this.mergeFrom(var3);
               }

               throw var22;
            }

            if (var21 != null) {
               this.mergeFrom(var21);
            }

            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder mergeFrom(DescriptorProtos.UninterpretedOption var1) {
            if (var1 == DescriptorProtos.UninterpretedOption.getDefaultInstance()) {
               return this;
            } else {
               if (this.nameBuilder_ == null) {
                  if (!var1.name_.isEmpty()) {
                     if (this.name_.isEmpty()) {
                        this.name_ = var1.name_;
                        this.bitField0_ &= -2;
                     } else {
                        this.ensureNameIsMutable();
                        this.name_.addAll(var1.name_);
                     }

                     this.onChanged();
                  }
               } else if (!var1.name_.isEmpty()) {
                  if (this.nameBuilder_.isEmpty()) {
                     this.nameBuilder_.dispose();
                     RepeatedFieldBuilder var2 = null;
                     this.nameBuilder_ = null;
                     this.name_ = var1.name_;
                     this.bitField0_ &= -2;
                     if (GeneratedMessage.alwaysUseFieldBuilders) {
                        var2 = this.getNameFieldBuilder();
                     }

                     this.nameBuilder_ = var2;
                  } else {
                     this.nameBuilder_.addAllMessages(var1.name_);
                  }
               }

               if (var1.hasIdentifierValue()) {
                  this.bitField0_ |= 2;
                  this.identifierValue_ = var1.identifierValue_;
                  this.onChanged();
               }

               if (var1.hasPositiveIntValue()) {
                  this.setPositiveIntValue(var1.getPositiveIntValue());
               }

               if (var1.hasNegativeIntValue()) {
                  this.setNegativeIntValue(var1.getNegativeIntValue());
               }

               if (var1.hasDoubleValue()) {
                  this.setDoubleValue(var1.getDoubleValue());
               }

               if (var1.hasStringValue()) {
                  this.setStringValue(var1.getStringValue());
               }

               if (var1.hasAggregateValue()) {
                  this.bitField0_ |= 64;
                  this.aggregateValue_ = var1.aggregateValue_;
                  this.onChanged();
               }

               this.mergeUnknownFields(var1.getUnknownFields());
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder mergeFrom(Message var1) {
            if (var1 instanceof DescriptorProtos.UninterpretedOption) {
               return this.mergeFrom((DescriptorProtos.UninterpretedOption)var1);
            } else {
               super.mergeFrom(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder removeName(int var1) {
            RepeatedFieldBuilder var2 = this.nameBuilder_;
            if (var2 == null) {
               this.ensureNameIsMutable();
               this.name_.remove(var1);
               this.onChanged();
               return this;
            } else {
               var2.remove(var1);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder setAggregateValue(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 64;
               this.aggregateValue_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder setAggregateValueBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 64;
               this.aggregateValue_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder setDoubleValue(double var1) {
            this.bitField0_ |= 16;
            this.doubleValue_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder setIdentifierValue(String var1) {
            if (var1 != null) {
               this.bitField0_ |= 2;
               this.identifierValue_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder setIdentifierValueBytes(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 2;
               this.identifierValue_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder setName(int var1, DescriptorProtos.UninterpretedOption.NamePart.Builder var2) {
            RepeatedFieldBuilder var3 = this.nameBuilder_;
            if (var3 == null) {
               this.ensureNameIsMutable();
               this.name_.set(var1, var2.build());
               this.onChanged();
               return this;
            } else {
               var3.setMessage(var1, var2.build());
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder setName(int var1, DescriptorProtos.UninterpretedOption.NamePart var2) {
            RepeatedFieldBuilder var3 = this.nameBuilder_;
            if (var3 == null) {
               if (var2 != null) {
                  this.ensureNameIsMutable();
                  this.name_.set(var1, var2);
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            } else {
               var3.setMessage(var1, var2);
               return this;
            }
         }

         public DescriptorProtos.UninterpretedOption.Builder setNegativeIntValue(long var1) {
            this.bitField0_ |= 8;
            this.negativeIntValue_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder setPositiveIntValue(long var1) {
            this.bitField0_ |= 4;
            this.positiveIntValue_ = var1;
            this.onChanged();
            return this;
         }

         public DescriptorProtos.UninterpretedOption.Builder setStringValue(ByteString var1) {
            if (var1 != null) {
               this.bitField0_ |= 32;
               this.stringValue_ = var1;
               this.onChanged();
               return this;
            } else {
               throw null;
            }
         }
      }

      public static final class NamePart extends GeneratedMessage implements DescriptorProtos.UninterpretedOption.NamePartOrBuilder {
         public static final int IS_EXTENSION_FIELD_NUMBER = 2;
         public static final int NAME_PART_FIELD_NUMBER = 1;
         public static Parser PARSER = new AbstractParser() {
            public DescriptorProtos.UninterpretedOption.NamePart parsePartialFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws InvalidProtocolBufferException {
               return new DescriptorProtos.UninterpretedOption.NamePart(var1, var2);
            }
         };
         private static final DescriptorProtos.UninterpretedOption.NamePart defaultInstance;
         private static final long serialVersionUID = 0L;
         private int bitField0_;
         private boolean isExtension_;
         private byte memoizedIsInitialized;
         private int memoizedSerializedSize;
         private Object namePart_;
         private final UnknownFieldSet unknownFields;

         static {
            DescriptorProtos.UninterpretedOption.NamePart var0 = new DescriptorProtos.UninterpretedOption.NamePart(true);
            defaultInstance = var0;
            var0.initFields();
         }

         private NamePart(CodedInputStream param1, ExtensionRegistryLite param2) throws InvalidProtocolBufferException {
            // $FF: Couldn't be decompiled
         }

         // $FF: synthetic method
         NamePart(CodedInputStream var1, ExtensionRegistryLite var2, Object var3) throws InvalidProtocolBufferException {
            this(var1, var2);
         }

         private NamePart(GeneratedMessage.Builder var1) {
            super(var1);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = var1.getUnknownFields();
         }

         // $FF: synthetic method
         NamePart(GeneratedMessage.Builder var1, Object var2) {
            this(var1);
         }

         private NamePart(boolean var1) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
         }

         public static DescriptorProtos.UninterpretedOption.NamePart getDefaultInstance() {
            return defaultInstance;
         }

         public static final Descriptors.Descriptor getDescriptor() {
            return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
         }

         private void initFields() {
            this.namePart_ = "";
            this.isExtension_ = false;
         }

         public static DescriptorProtos.UninterpretedOption.NamePart.Builder newBuilder() {
            return DescriptorProtos.UninterpretedOption.NamePart.Builder.create();
         }

         public static DescriptorProtos.UninterpretedOption.NamePart.Builder newBuilder(DescriptorProtos.UninterpretedOption.NamePart var0) {
            return newBuilder().mergeFrom(var0);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseDelimitedFrom(InputStream var0) throws IOException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseDelimitedFrom(var0);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseDelimitedFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseDelimitedFrom(var0, var1);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseFrom(ByteString var0) throws InvalidProtocolBufferException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseFrom(ByteString var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseFrom(CodedInputStream var0) throws IOException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseFrom(CodedInputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseFrom(InputStream var0) throws IOException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseFrom(InputStream var0, ExtensionRegistryLite var1) throws IOException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseFrom(var0, var1);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseFrom(byte[] var0) throws InvalidProtocolBufferException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseFrom(var0);
         }

         public static DescriptorProtos.UninterpretedOption.NamePart parseFrom(byte[] var0, ExtensionRegistryLite var1) throws InvalidProtocolBufferException {
            return (DescriptorProtos.UninterpretedOption.NamePart)PARSER.parseFrom(var0, var1);
         }

         public DescriptorProtos.UninterpretedOption.NamePart getDefaultInstanceForType() {
            return defaultInstance;
         }

         public boolean getIsExtension() {
            return this.isExtension_;
         }

         public String getNamePart() {
            Object var1 = this.namePart_;
            if (var1 instanceof String) {
               return (String)var1;
            } else {
               ByteString var3 = (ByteString)var1;
               String var2 = var3.toStringUtf8();
               if (var3.isValidUtf8()) {
                  this.namePart_ = var2;
               }

               return var2;
            }
         }

         public ByteString getNamePartBytes() {
            Object var1 = this.namePart_;
            if (var1 instanceof String) {
               ByteString var2 = ByteString.copyFromUtf8((String)var1);
               this.namePart_ = var2;
               return var2;
            } else {
               return (ByteString)var1;
            }
         }

         public Parser getParserForType() {
            return PARSER;
         }

         public int getSerializedSize() {
            int var1 = this.memoizedSerializedSize;
            if (var1 != -1) {
               return var1;
            } else {
               var1 = 0;
               if ((this.bitField0_ & 1) == 1) {
                  var1 = 0 + CodedOutputStream.computeBytesSize(1, this.getNamePartBytes());
               }

               int var2 = var1;
               if ((this.bitField0_ & 2) == 2) {
                  var2 = var1 + CodedOutputStream.computeBoolSize(2, this.isExtension_);
               }

               var1 = var2 + this.getUnknownFields().getSerializedSize();
               this.memoizedSerializedSize = var1;
               return var1;
            }
         }

         public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
         }

         public boolean hasIsExtension() {
            return (this.bitField0_ & 2) == 2;
         }

         public boolean hasNamePart() {
            return (this.bitField0_ & 1) == 1;
         }

         protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.UninterpretedOption.NamePart.class, DescriptorProtos.UninterpretedOption.NamePart.Builder.class);
         }

         public final boolean isInitialized() {
            byte var1 = this.memoizedIsInitialized;
            if (var1 != -1) {
               return var1 == 1;
            } else if (!this.hasNamePart()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else if (!this.hasIsExtension()) {
               this.memoizedIsInitialized = 0;
               return false;
            } else {
               this.memoizedIsInitialized = 1;
               return true;
            }
         }

         public DescriptorProtos.UninterpretedOption.NamePart.Builder newBuilderForType() {
            return newBuilder();
         }

         protected DescriptorProtos.UninterpretedOption.NamePart.Builder newBuilderForType(GeneratedMessage.BuilderParent var1) {
            return new DescriptorProtos.UninterpretedOption.NamePart.Builder(var1);
         }

         public DescriptorProtos.UninterpretedOption.NamePart.Builder toBuilder() {
            return newBuilder(this);
         }

         protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
         }

         public void writeTo(CodedOutputStream var1) throws IOException {
            this.getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
               var1.writeBytes(1, this.getNamePartBytes());
            }

            if ((this.bitField0_ & 2) == 2) {
               var1.writeBool(2, this.isExtension_);
            }

            this.getUnknownFields().writeTo(var1);
         }

         public static final class Builder extends GeneratedMessage.Builder implements DescriptorProtos.UninterpretedOption.NamePartOrBuilder {
            private int bitField0_;
            private boolean isExtension_;
            private Object namePart_;

            private Builder() {
               this.namePart_ = "";
               this.maybeForceBuilderInitialization();
            }

            private Builder(GeneratedMessage.BuilderParent var1) {
               super(var1);
               this.namePart_ = "";
               this.maybeForceBuilderInitialization();
            }

            // $FF: synthetic method
            Builder(GeneratedMessage.BuilderParent var1, Object var2) {
               this(var1);
            }

            private static DescriptorProtos.UninterpretedOption.NamePart.Builder create() {
               return new DescriptorProtos.UninterpretedOption.NamePart.Builder();
            }

            public static final Descriptors.Descriptor getDescriptor() {
               return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
            }

            private void maybeForceBuilderInitialization() {
               boolean var1 = GeneratedMessage.alwaysUseFieldBuilders;
            }

            public DescriptorProtos.UninterpretedOption.NamePart build() {
               DescriptorProtos.UninterpretedOption.NamePart var1 = this.buildPartial();
               if (var1.isInitialized()) {
                  return var1;
               } else {
                  throw newUninitializedMessageException(var1);
               }
            }

            public DescriptorProtos.UninterpretedOption.NamePart buildPartial() {
               DescriptorProtos.UninterpretedOption.NamePart var4 = new DescriptorProtos.UninterpretedOption.NamePart(this);
               int var3 = this.bitField0_;
               int var1 = 0;
               if ((var3 & 1) == 1) {
                  var1 = 0 | 1;
               }

               var4.namePart_ = this.namePart_;
               int var2 = var1;
               if ((var3 & 2) == 2) {
                  var2 = var1 | 2;
               }

               var4.isExtension_ = this.isExtension_;
               var4.bitField0_ = var2;
               this.onBuilt();
               return var4;
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder clear() {
               super.clear();
               this.namePart_ = "";
               int var1 = this.bitField0_ & -2;
               this.bitField0_ = var1;
               this.isExtension_ = false;
               this.bitField0_ = var1 & -3;
               return this;
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder clearIsExtension() {
               this.bitField0_ &= -3;
               this.isExtension_ = false;
               this.onChanged();
               return this;
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder clearNamePart() {
               this.bitField0_ &= -2;
               this.namePart_ = DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance().getNamePart();
               this.onChanged();
               return this;
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder clone() {
               return create().mergeFrom(this.buildPartial());
            }

            public DescriptorProtos.UninterpretedOption.NamePart getDefaultInstanceForType() {
               return DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance();
            }

            public Descriptors.Descriptor getDescriptorForType() {
               return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
            }

            public boolean getIsExtension() {
               return this.isExtension_;
            }

            public String getNamePart() {
               Object var1 = this.namePart_;
               if (!(var1 instanceof String)) {
                  String var2 = ((ByteString)var1).toStringUtf8();
                  this.namePart_ = var2;
                  return var2;
               } else {
                  return (String)var1;
               }
            }

            public ByteString getNamePartBytes() {
               Object var1 = this.namePart_;
               if (var1 instanceof String) {
                  ByteString var2 = ByteString.copyFromUtf8((String)var1);
                  this.namePart_ = var2;
                  return var2;
               } else {
                  return (ByteString)var1;
               }
            }

            public boolean hasIsExtension() {
               return (this.bitField0_ & 2) == 2;
            }

            public boolean hasNamePart() {
               return (this.bitField0_ & 1) == 1;
            }

            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
               return DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable.ensureFieldAccessorsInitialized(DescriptorProtos.UninterpretedOption.NamePart.class, DescriptorProtos.UninterpretedOption.NamePart.Builder.class);
            }

            public final boolean isInitialized() {
               if (!this.hasNamePart()) {
                  return false;
               } else {
                  return this.hasIsExtension();
               }
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException {
               Object var4 = null;
               DescriptorProtos.UninterpretedOption.NamePart var3 = (DescriptorProtos.UninterpretedOption.NamePart)var4;

               DescriptorProtos.UninterpretedOption.NamePart var21;
               label173: {
                  Throwable var10000;
                  label174: {
                     boolean var10001;
                     InvalidProtocolBufferException var23;
                     try {
                        try {
                           var21 = (DescriptorProtos.UninterpretedOption.NamePart)DescriptorProtos.UninterpretedOption.NamePart.PARSER.parsePartialFrom(var1, var2);
                           break label173;
                        } catch (InvalidProtocolBufferException var19) {
                           var23 = var19;
                        }
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label174;
                     }

                     var3 = (DescriptorProtos.UninterpretedOption.NamePart)var4;

                     try {
                        var21 = (DescriptorProtos.UninterpretedOption.NamePart)var23.getUnfinishedMessage();
                     } catch (Throwable var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label174;
                     }

                     var3 = var21;

                     label158:
                     try {
                        throw var23;
                     } catch (Throwable var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label158;
                     }
                  }

                  Throwable var22 = var10000;
                  if (var3 != null) {
                     this.mergeFrom(var3);
                  }

                  throw var22;
               }

               if (var21 != null) {
                  this.mergeFrom(var21);
               }

               return this;
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder mergeFrom(DescriptorProtos.UninterpretedOption.NamePart var1) {
               if (var1 == DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance()) {
                  return this;
               } else {
                  if (var1.hasNamePart()) {
                     this.bitField0_ |= 1;
                     this.namePart_ = var1.namePart_;
                     this.onChanged();
                  }

                  if (var1.hasIsExtension()) {
                     this.setIsExtension(var1.getIsExtension());
                  }

                  this.mergeUnknownFields(var1.getUnknownFields());
                  return this;
               }
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder mergeFrom(Message var1) {
               if (var1 instanceof DescriptorProtos.UninterpretedOption.NamePart) {
                  return this.mergeFrom((DescriptorProtos.UninterpretedOption.NamePart)var1);
               } else {
                  super.mergeFrom(var1);
                  return this;
               }
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder setIsExtension(boolean var1) {
               this.bitField0_ |= 2;
               this.isExtension_ = var1;
               this.onChanged();
               return this;
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder setNamePart(String var1) {
               if (var1 != null) {
                  this.bitField0_ |= 1;
                  this.namePart_ = var1;
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            }

            public DescriptorProtos.UninterpretedOption.NamePart.Builder setNamePartBytes(ByteString var1) {
               if (var1 != null) {
                  this.bitField0_ |= 1;
                  this.namePart_ = var1;
                  this.onChanged();
                  return this;
               } else {
                  throw null;
               }
            }
         }
      }

      public interface NamePartOrBuilder extends MessageOrBuilder {
         boolean getIsExtension();

         String getNamePart();

         ByteString getNamePartBytes();

         boolean hasIsExtension();

         boolean hasNamePart();
      }
   }

   public interface UninterpretedOptionOrBuilder extends MessageOrBuilder {
      String getAggregateValue();

      ByteString getAggregateValueBytes();

      double getDoubleValue();

      String getIdentifierValue();

      ByteString getIdentifierValueBytes();

      DescriptorProtos.UninterpretedOption.NamePart getName(int var1);

      int getNameCount();

      List getNameList();

      DescriptorProtos.UninterpretedOption.NamePartOrBuilder getNameOrBuilder(int var1);

      List getNameOrBuilderList();

      long getNegativeIntValue();

      long getPositiveIntValue();

      ByteString getStringValue();

      boolean hasAggregateValue();

      boolean hasDoubleValue();

      boolean hasIdentifierValue();

      boolean hasNegativeIntValue();

      boolean hasPositiveIntValue();

      boolean hasStringValue();
   }
}
