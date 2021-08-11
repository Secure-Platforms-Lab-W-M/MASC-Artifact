/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.RpcCallback;

public interface RpcController {
    public String errorText();

    public boolean failed();

    public boolean isCanceled();

    public void notifyOnCancel(RpcCallback<Object> var1);

    public void reset();

    public void setFailed(String var1);

    public void startCancel();
}

