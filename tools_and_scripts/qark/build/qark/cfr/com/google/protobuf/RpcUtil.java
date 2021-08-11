/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;

public final class RpcUtil {
    private RpcUtil() {
    }

    private static <Type extends Message> Type copyAsType(Type Type2, Message message) {
        return (Type)Type2.newBuilderForType().mergeFrom(message).build();
    }

    public static <Type extends Message> RpcCallback<Message> generalizeCallback(final RpcCallback<Type> rpcCallback, final Class<Type> class_, final Type Type2) {
        return new RpcCallback<Message>(){

            @Override
            public void run(Message message) {
                try {
                    Message message2;
                    message = message2 = (Message)class_.cast(message);
                }
                catch (ClassCastException classCastException) {
                    message = RpcUtil.copyAsType(Type2, message);
                }
                rpcCallback.run(message);
            }
        };
    }

    public static <ParameterType> RpcCallback<ParameterType> newOneTimeCallback(final RpcCallback<ParameterType> rpcCallback) {
        return new RpcCallback<ParameterType>(){
            private boolean alreadyCalled;
            {
                this.alreadyCalled = false;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void run(ParameterType ParameterType) {
                synchronized (this) {
                    if (!this.alreadyCalled) {
                        this.alreadyCalled = true;
                        // MONITOREXIT [2, 3] lbl4 : MonitorExitStatement: MONITOREXIT : this
                        rpcCallback.run(ParameterType);
                        return;
                    }
                    throw new AlreadyCalledException();
                }
            }
        };
    }

    public static <Type extends Message> RpcCallback<Type> specializeCallback(RpcCallback<Message> rpcCallback) {
        return rpcCallback;
    }

    public static final class AlreadyCalledException
    extends RuntimeException {
        private static final long serialVersionUID = 5469741279507848266L;

        public AlreadyCalledException() {
            super("This RpcCallback was already called and cannot be called multiple times.");
        }
    }

}

