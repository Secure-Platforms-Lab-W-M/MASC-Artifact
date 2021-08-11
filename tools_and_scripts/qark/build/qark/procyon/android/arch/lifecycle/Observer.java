// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

import android.support.annotation.Nullable;

public interface Observer<T>
{
    void onChanged(@Nullable final T p0);
}
