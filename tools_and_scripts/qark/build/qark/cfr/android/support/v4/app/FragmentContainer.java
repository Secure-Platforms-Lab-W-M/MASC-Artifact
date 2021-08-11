/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.View
 */
package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class FragmentContainer {
    public Fragment instantiate(Context context, String string2, Bundle bundle) {
        return Fragment.instantiate(context, string2, bundle);
    }

    @Nullable
    public abstract View onFindViewById(@IdRes int var1);

    public abstract boolean onHasView();
}

