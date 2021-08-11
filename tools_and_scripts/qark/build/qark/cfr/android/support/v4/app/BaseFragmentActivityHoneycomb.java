/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 */
package android.support.v4.app;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.BaseFragmentActivityDonut;
import android.util.AttributeSet;
import android.view.View;

abstract class BaseFragmentActivityHoneycomb
extends BaseFragmentActivityDonut {
    BaseFragmentActivityHoneycomb() {
    }

    public View onCreateView(View view, String string, Context context, AttributeSet attributeSet) {
        View view2 = this.dispatchFragmentsOnCreateView(view, string, context, attributeSet);
        if (view2 == null && Build.VERSION.SDK_INT >= 11) {
            return super.onCreateView(view, string, context, attributeSet);
        }
        return view2;
    }
}

