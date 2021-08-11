// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

public interface Callback
{
    void onError();
    
    void onSuccess();
    
    public static class EmptyCallback implements Callback
    {
        @Override
        public void onError() {
        }
        
        @Override
        public void onSuccess() {
        }
    }
}
