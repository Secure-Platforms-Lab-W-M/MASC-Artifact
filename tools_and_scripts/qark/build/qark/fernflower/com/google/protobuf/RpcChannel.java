package com.google.protobuf;

public interface RpcChannel {
   void callMethod(Descriptors.MethodDescriptor var1, RpcController var2, Message var3, Message var4, RpcCallback var5);
}
