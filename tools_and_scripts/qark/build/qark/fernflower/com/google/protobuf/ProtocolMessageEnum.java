package com.google.protobuf;

public interface ProtocolMessageEnum extends Internal.EnumLite {
   Descriptors.EnumDescriptor getDescriptorForType();

   int getNumber();

   Descriptors.EnumValueDescriptor getValueDescriptor();
}
