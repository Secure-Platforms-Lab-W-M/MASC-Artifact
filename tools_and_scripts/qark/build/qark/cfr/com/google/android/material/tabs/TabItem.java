/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 *  com.google.android.material.R
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R;

public class TabItem
extends View {
    public final int customLayout;
    public final Drawable icon;
    public final CharSequence text;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context object, AttributeSet attributeSet) {
        super((Context)object, attributeSet);
        object = TintTypedArray.obtainStyledAttributes((Context)object, attributeSet, R.styleable.TabItem);
        this.text = object.getText(R.styleable.TabItem_android_text);
        this.icon = object.getDrawable(R.styleable.TabItem_android_icon);
        this.customLayout = object.getResourceId(R.styleable.TabItem_android_layout, 0);
        object.recycle();
    }
}

