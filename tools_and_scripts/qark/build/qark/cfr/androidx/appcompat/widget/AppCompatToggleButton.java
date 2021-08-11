/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.widget.TextView
 *  android.widget.ToggleButton
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.widget.AppCompatTextHelper;

public class AppCompatToggleButton
extends ToggleButton {
    private final AppCompatTextHelper mTextHelper;

    public AppCompatToggleButton(Context context) {
        this(context, null);
    }

    public AppCompatToggleButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842827);
    }

    public AppCompatToggleButton(Context object, AttributeSet attributeSet, int n) {
        super((Context)object, attributeSet, n);
        this.mTextHelper = object = new AppCompatTextHelper((TextView)this);
        object.loadFromAttributes(attributeSet, n);
    }
}

