// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.Bitmap;

public interface Cache
{
    public static final Cache NONE = new Cache() {
        @Override
        public void clear() {
        }
        
        @Override
        public void clearKeyUri(final String s) {
        }
        
        @Override
        public Bitmap get(final String s) {
            return null;
        }
        
        @Override
        public int maxSize() {
            return 0;
        }
        
        @Override
        public void set(final String s, final Bitmap bitmap) {
        }
        
        @Override
        public int size() {
            return 0;
        }
    };
    
    void clear();
    
    void clearKeyUri(final String p0);
    
    Bitmap get(final String p0);
    
    int maxSize();
    
    void set(final String p0, final Bitmap p1);
    
    int size();
}
