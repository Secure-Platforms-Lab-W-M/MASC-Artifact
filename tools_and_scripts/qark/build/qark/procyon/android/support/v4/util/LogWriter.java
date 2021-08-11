// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.util;

import android.util.Log;
import android.support.annotation.RestrictTo;
import java.io.Writer;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class LogWriter extends Writer
{
    private StringBuilder mBuilder;
    private final String mTag;
    
    public LogWriter(final String mTag) {
        this.mBuilder = new StringBuilder(128);
        this.mTag = mTag;
    }
    
    private void flushBuilder() {
        if (this.mBuilder.length() > 0) {
            Log.d(this.mTag, this.mBuilder.toString());
            final StringBuilder mBuilder = this.mBuilder;
            mBuilder.delete(0, mBuilder.length());
        }
    }
    
    @Override
    public void close() {
        this.flushBuilder();
    }
    
    @Override
    public void flush() {
        this.flushBuilder();
    }
    
    @Override
    public void write(final char[] array, final int n, final int n2) {
        for (int i = 0; i < n2; ++i) {
            final char c = array[n + i];
            if (c == '\n') {
                this.flushBuilder();
            }
            else {
                this.mBuilder.append(c);
            }
        }
    }
}
