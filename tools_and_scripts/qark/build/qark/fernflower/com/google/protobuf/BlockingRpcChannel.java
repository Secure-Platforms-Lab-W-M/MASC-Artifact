package com.google.protobuf;

public interface BlockingRpcChannel {
   Message callBlockingMethod(Descriptors.MethodDescriptor var1, RpcController var2, Message var3, Message var4) throws ServiceException;
}
