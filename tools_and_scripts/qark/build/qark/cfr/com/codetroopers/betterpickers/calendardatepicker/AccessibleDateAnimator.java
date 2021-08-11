/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.format.DateUtils
 *  android.util.AttributeSet
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.ViewAnimator
 */
package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ViewAnimator;
import java.util.List;

public class AccessibleDateAnimator
extends ViewAnimator {
    private long mDateMillis;

    public AccessibleDateAnimator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 32) {
            accessibilityEvent.getText().clear();
            String string2 = DateUtils.formatDateTime((Context)this.getContext(), (long)this.mDateMillis, (int)22);
            accessibilityEvent.getText().add(string2);
            return true;
        }
        return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    public void setDateMillis(long l) {
        this.mDateMillis = l;
    }
}

