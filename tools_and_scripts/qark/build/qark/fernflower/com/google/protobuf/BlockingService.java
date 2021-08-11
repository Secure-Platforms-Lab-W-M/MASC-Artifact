package com.google.protobuf;

public interface BlockingService {
   Message callBlockingMethod(Descriptors.MethodDescriptor var1, RpcController var2, Message var3) throws ServiceException;

   Descriptors.ServiceDescriptor getDescriptorForType();

   Message getRequestPrototype(Descriptors.MethodDescriptor var1);

   Message getResponsePrototype(Descriptors.MethodDescriptor var1);
}
