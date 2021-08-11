/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.widget.TextView
 *  com.google.android.material.R
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

final class CalendarItemStyle {
    private final ColorStateList backgroundColor;
    private final Rect insets;
    private final ShapeAppearanceModel itemShape;
    private final ColorStateList strokeColor;
    private final int strokeWidth;
    private final ColorStateList textColor;

    private CalendarItemStyle(ColorStateList colorStateList, ColorStateList colorStateList2, ColorStateList colorStateList3, int n, ShapeAppearanceModel shapeAppearanceModel, Rect rect) {
        Preconditions.checkArgumentNonnegative(rect.left);
        Preconditions.checkArgumentNonnegative(rect.top);
        Preconditions.checkArgumentNonnegative(rect.right);
        Preconditions.checkArgumentNonnegative(rect.bottom);
        this.insets = rect;
        this.textColor = colorStateList2;
        this.backgroundColor = colorStateList;
        this.strokeColor = colorStateList3;
        this.strokeWidth = n;
        this.itemShape = shapeAppearanceModel;
    }

    static CalendarItemStyle create(Context object, int n) {
        boolean bl = n != 0;
        Preconditions.checkArgument(bl, "Cannot create a CalendarItemStyle with a styleResId of 0");
        TypedArray typedArray = object.obtainStyledAttributes(n, R.styleable.MaterialCalendarItem);
        Rect rect = new Rect(typedArray.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetLeft, 0), typedArray.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetTop, 0), typedArray.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetRight, 0), typedArray.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetBottom, 0));
        ColorStateList colorStateList = MaterialResources.getColorStateList((Context)object, typedArray, R.styleable.MaterialCalendarItem_itemFillColor);
        ColorStateList colorStateList2 = MaterialResources.getColorStateList((Context)object, typedArray, R.styleable.MaterialCalendarItem_itemTextColor);
        ColorStateList colorStateList3 = MaterialResources.getColorStateList((Context)object, typedArray, R.styleable.MaterialCalendarItem_itemStrokeColor);
        n = typedArray.getDimensionPixelSize(R.styleable.MaterialCalendarItem_itemStrokeWidth, 0);
        object = ShapeAppearanceModel.builder((Context)object, typedArray.getResourceId(R.styleable.MaterialCalendarItem_itemShapeAppearance, 0), typedArray.getResourceId(R.styleable.MaterialCalendarItem_itemShapeAppearanceOverlay, 0)).build();
        typedArray.recycle();
        return new CalendarItemStyle(colorStateList, colorStateList2, colorStateList3, n, (ShapeAppearanceModel)object, rect);
    }

    int getBottomInset() {
        return this.insets.bottom;
    }

    int getLeftInset() {
        return this.insets.left;
    }

    int getRightInset() {
        return this.insets.right;
    }

    int getTopInset() {
        return this.insets.top;
    }

    void styleItem(TextView textView) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable();
        materialShapeDrawable.setShapeAppearanceModel(this.itemShape);
        materialShapeDrawable2.setShapeAppearanceModel(this.itemShape);
        materialShapeDrawable.setFillColor(this.backgroundColor);
        materialShapeDrawable.setStroke((float)this.strokeWidth, this.strokeColor);
        textView.setTextColor(this.textColor);
        if (Build.VERSION.SDK_INT >= 21) {
            materialShapeDrawable = new RippleDrawable(this.textColor.withAlpha(30), (Drawable)materialShapeDrawable, (Drawable)materialShapeDrawable2);
        }
        ViewCompat.setBackground((View)textView, (Drawable)new InsetDrawable((Drawable)materialShapeDrawable, this.insets.left, this.insets.top, this.insets.right, this.insets.bottom));
    }
}

