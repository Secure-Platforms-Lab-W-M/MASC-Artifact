package com.google.protobuf;

public class ServiceException extends Exception {
   private static final long serialVersionUID = -1219262335729891920L;

   public ServiceException(String var1) {
      super(var1);
   }

   public ServiceException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ServiceException(Throwable var1) {
      super(var1);
   }
}
