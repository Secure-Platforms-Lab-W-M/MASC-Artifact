/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
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
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

abstract class BaseFragmentActivityGingerbread
extends Activity {
    boolean mStartedIntentSenderFromFragment;

    BaseFragmentActivityGingerbread() {
    }

    static void checkForValidRequestCode(int n) {
        if ((-65536 & n) != 0) {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }
    }

    abstract View dispatchFragmentsOnCreateView(View var1, String var2, Context var3, AttributeSet var4);

    protected void onCreate(Bundle bundle) {
        if (Build.VERSION.SDK_INT < 11 && this.getLayoutInflater().getFactory() == null) {
            this.getLayoutInflater().setFactory((LayoutInflater.Factory)this);
        }
        super.onCreate(bundle);
    }

    public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
        View view;
        View view2 = view = this.dispatchFragmentsOnCreateView(null, string2, context, attributeSet);
        if (view == null) {
            view2 = super.onCreateView(string2, context, attributeSet);
        }
        return view2;
    }

    public void startIntentSenderForResult(IntentSender intentSender, int n, @Nullable Intent intent, int n2, int n3, int n4) throws IntentSender.SendIntentException {
        if (!this.mStartedIntentSenderFromFragment && n != -1) {
            BaseFragmentActivityGingerbread.checkForValidRequestCode(n);
        }
        super.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4);
    }
}

