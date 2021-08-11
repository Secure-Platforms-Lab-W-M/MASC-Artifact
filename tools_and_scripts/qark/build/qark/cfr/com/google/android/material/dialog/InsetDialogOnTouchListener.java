/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.ViewConfiguration
 */
package com.google.android.material.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class InsetDialogOnTouchListener
implements View.OnTouchListener {
    private final Dialog dialog;
    private final int leftInset;
    private final int prePieSlop;
    private final int topInset;

    public InsetDialogOnTouchListener(Dialog dialog, Rect rect) {
        this.dialog = dialog;
        this.leftInset = rect.left;
        this.topInset = rect.top;
        this.prePieSlop = ViewConfiguration.get((Context)dialog.getContext()).getScaledWindowTouchSlop();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int n;
        View view2 = view.findViewById(16908290);
        int n2 = this.leftInset + view2.getLeft();
        int n3 = view2.getWidth();
        int n4 = this.topInset + view2.getTop();
        if (new RectF((float)n2, (float)n4, (float)(n3 + n2), (float)((n = view2.getHeight()) + n4)).contains(motionEvent.getX(), motionEvent.getY())) {
            return false;
        }
        motionEvent = MotionEvent.obtain((MotionEvent)motionEvent);
        motionEvent.setAction(4);
        if (Build.VERSION.SDK_INT < 28) {
            motionEvent.setAction(0);
            n2 = this.prePieSlop;
            motionEvent.setLocation((float)(- n2 - 1), (float)(- n2 - 1));
        }
        view.performClick();
        return this.dialog.onTouchEvent(motionEvent);
    }
}

