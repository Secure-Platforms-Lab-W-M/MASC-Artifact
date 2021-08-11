package com.google.protobuf;

public final class RpcUtil {
   private RpcUtil() {
   }

   private static Message copyAsType(Message var0, Message var1) {
      return var0.newBuilderForType().mergeFrom(var1).build();
   }

   public static RpcCallback generalizeCallback(final RpcCallback var0, final Class var1, final Message var2) {
      return new RpcCallback() {
         public void run(Message var1x) {
            label12: {
               Message var2x;
               try {
                  var2x = (Message)var1.cast(var1x);
               } catch (ClassCastException var3) {
                  var1x = RpcUtil.copyAsType(var2, var1x);
                  break label12;
               }

               var1x = var2x;
            }

            var0.run(var1x);
         }
      };
   }

   public static RpcCallback newOneTimeCallback(final RpcCallback var0) {
      return new RpcCallback() {
         private boolean alreadyCalled = false;

         public void run(Object var1) {
            synchronized(this){}

            label140: {
               Throwable var10000;
               boolean var10001;
               label135: {
                  try {
                     if (!this.alreadyCalled) {
                        this.alreadyCalled = true;
                        break label140;
                     }
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label135;
                  }

                  label128:
                  try {
                     throw new RpcUtil.AlreadyCalledException();
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label128;
                  }
               }

               while(true) {
                  Throwable var14 = var10000;

                  try {
                     throw var14;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     continue;
                  }
               }
            }

            var0.run(var1);
         }
      };
   }

   public static RpcCallback specializeCallback(RpcCallback var0) {
      return var0;
   }

   public static final class AlreadyCalledException extends RuntimeException {
      private static final long serialVersionUID = 5469741279507848266L;

      public AlreadyCalledException() {
         super("This RpcCallback was already called and cannot be called multiple times.");
      }
   }
}
