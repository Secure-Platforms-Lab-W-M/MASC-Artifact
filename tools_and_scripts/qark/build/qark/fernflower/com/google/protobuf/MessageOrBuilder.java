package com.google.protobuf;

import java.util.List;
import java.util.Map;

public interface MessageOrBuilder extends MessageLiteOrBuilder {
   List findInitializationErrors();

   Map getAllFields();

   Message getDefaultInstanceForType();

   Descriptors.Descriptor getDescriptorForType();

   Object getField(Descriptors.FieldDescriptor var1);

   String getInitializationErrorString();

   Object getRepeatedField(Descriptors.FieldDescriptor var1, int var2);

   int getRepeatedFieldCount(Descriptors.FieldDescriptor var1);

   UnknownFieldSet getUnknownFields();

   boolean hasField(Descriptors.FieldDescriptor var1);
}
