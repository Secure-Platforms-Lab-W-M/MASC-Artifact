// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.support.annotation.Nullable;
import android.view.View;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.content.Context;

public abstract class FragmentContainer
{
    public Fragment instantiate(final Context context, final String s, final Bundle bundle) {
        return Fragment.instantiate(context, s, bundle);
    }
    
    @Nullable
    public abstract View onFindViewById(@IdRes final int p0);
    
    public abstract boolean onHasView();
}
