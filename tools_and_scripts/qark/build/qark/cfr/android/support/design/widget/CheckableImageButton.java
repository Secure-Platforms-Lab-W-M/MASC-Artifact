/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.Checkable
 */
package android.support.design.widget;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class CheckableImageButton
extends AppCompatImageButton
implements Checkable {
    private static final int[] DRAWABLE_STATE_CHECKED = new int[]{16842912};
    private boolean mChecked;

    public CheckableImageButton(Context context) {
        this(context, null);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.imageButtonStyle);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegateCompat(){

            @Override
            public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                super.onInitializeAccessibilityEvent(view, accessibilityEvent);
                accessibilityEvent.setChecked(CheckableImageButton.this.isChecked());
            }

            @Override
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCheckable(true);
                accessibilityNodeInfoCompat.setChecked(CheckableImageButton.this.isChecked());
            }
        });
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    public int[] onCreateDrawableState(int n) {
        if (this.mChecked) {
            return CheckableImageButton.mergeDrawableStates((int[])super.onCreateDrawableState(DRAWABLE_STATE_CHECKED.length + n), (int[])DRAWABLE_STATE_CHECKED);
        }
        return super.onCreateDrawableState(n);
    }

    public void setChecked(boolean bl) {
        if (this.mChecked != bl) {
            this.mChecked = bl;
            this.refreshDrawableState();
            this.sendAccessibilityEvent(2048);
            return;
        }
    }

    public void toggle() {
        this.setChecked(this.mChecked ^ true);
    }

}

