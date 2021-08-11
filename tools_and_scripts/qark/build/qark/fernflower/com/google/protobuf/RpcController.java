package com.google.protobuf;

public interface RpcController {
   String errorText();

   boolean failed();

   boolean isCanceled();

   void notifyOnCancel(RpcCallback var1);

   void reset();

   void setFailed(String var1);

   void startCancel();
}
