package com.google.protobuf;

public interface Service {
   void callMethod(Descriptors.MethodDescriptor var1, RpcController var2, Message var3, RpcCallback var4);

   Descriptors.ServiceDescriptor getDescriptorForType();

   Message getRequestPrototype(Descriptors.MethodDescriptor var1);

   Message getResponsePrototype(Descriptors.MethodDescriptor var1);
}
