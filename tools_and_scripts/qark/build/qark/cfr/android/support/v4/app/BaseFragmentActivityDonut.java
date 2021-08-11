/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory
 *  android.view.View
 */
package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

abstract class BaseFragmentActivityDonut
extends Activity {
    BaseFragmentActivityDonut() {
    }

    abstract View dispatchFragmentsOnCreateView(View var1, String var2, Context var3, AttributeSet var4);

    protected void onCreate(Bundle bundle) {
        if (Build.VERSION.SDK_INT < 11 && this.getLayoutInflater().getFactory() == null) {
            this.getLayoutInflater().setFactory((LayoutInflater.Factory)this);
        }
        super.onCreate(bundle);
    }

    public View onCreateView(String string, Context context, AttributeSet attributeSet) {
        View view = this.dispatchFragmentsOnCreateView(null, string, context, attributeSet);
        if (view == null) {
            return super.onCreateView(string, context, attributeSet);
        }
        return view;
    }
}

