// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.os.IBinder;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
class WindowIdApi14 implements WindowIdImpl
{
    private final IBinder mToken;
    
    WindowIdApi14(final IBinder mToken) {
        this.mToken = mToken;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof WindowIdApi14 && ((WindowIdApi14)o).mToken.equals(this.mToken);
    }
    
    @Override
    public int hashCode() {
        return this.mToken.hashCode();
    }
}
