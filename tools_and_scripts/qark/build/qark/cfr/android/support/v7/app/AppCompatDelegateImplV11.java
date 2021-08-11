/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.Window
 */
package android.support.v7.app;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegateImplV9;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

@RequiresApi(value=14)
class AppCompatDelegateImplV11
extends AppCompatDelegateImplV9 {
    AppCompatDelegateImplV11(Context context, Window window, AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
    }

    @Override
    View callActivityOnCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
        return null;
    }

    @Override
    public boolean hasWindowFeature(int n) {
        if (!super.hasWindowFeature(n) && !this.mWindow.hasFeature(n)) {
            return false;
        }
        return true;
    }
}

