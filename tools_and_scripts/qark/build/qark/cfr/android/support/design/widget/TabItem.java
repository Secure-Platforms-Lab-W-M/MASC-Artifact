/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 */
package android.support.design.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

public final class TabItem
extends View {
    final int mCustomLayout;
    final Drawable mIcon;
    final CharSequence mText;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context object, AttributeSet attributeSet) {
        super((Context)object, attributeSet);
        object = TintTypedArray.obtainStyledAttributes((Context)object, attributeSet, R.styleable.TabItem);
        this.mText = object.getText(R.styleable.TabItem_android_text);
        this.mIcon = object.getDrawable(R.styleable.TabItem_android_icon);
        this.mCustomLayout = object.getResourceId(R.styleable.TabItem_android_layout, 0);
        object.recycle();
    }
}

