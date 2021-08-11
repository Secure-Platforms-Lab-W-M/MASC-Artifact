// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.os.Build$VERSION;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

abstract class BaseFragmentActivityHoneycomb extends BaseFragmentActivityDonut
{
    public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        View view3;
        final View view2 = view3 = this.dispatchFragmentsOnCreateView(view, s, context, set);
        if (view2 == null) {
            view3 = view2;
            if (Build$VERSION.SDK_INT >= 11) {
                view3 = super.onCreateView(view, s, context, set);
            }
        }
        return view3;
    }
}
