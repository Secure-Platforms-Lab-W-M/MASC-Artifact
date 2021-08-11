/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.DatePickerDialog
 *  android.app.DatePickerDialog$OnDateSetListener
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.Window
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$style
 */
package com.google.android.material.datepicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import com.google.android.material.R;
import com.google.android.material.dialog.InsetDialogOnTouchListener;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;

public class MaterialStyledDatePickerDialog
extends DatePickerDialog {
    private static final int DEF_STYLE_ATTR = 16843612;
    private static final int DEF_STYLE_RES = R.style.MaterialAlertDialog_MaterialComponents_Picker_Date_Spinner;
    private final Drawable background;
    private final Rect backgroundInsets;

    public MaterialStyledDatePickerDialog(Context context) {
        this(context, 0);
    }

    public MaterialStyledDatePickerDialog(Context context, int n) {
        this(context, n, null, -1, -1, -1);
    }

    public MaterialStyledDatePickerDialog(Context object, int n, DatePickerDialog.OnDateSetListener onDateSetListener, int n2, int n3, int n4) {
        super((Context)object, n, onDateSetListener, n2, n3, n4);
        onDateSetListener = this.getContext();
        n = MaterialAttributes.resolveOrThrow(this.getContext(), R.attr.colorSurface, this.getClass().getCanonicalName());
        object = new MaterialShapeDrawable((Context)onDateSetListener, null, 16843612, DEF_STYLE_RES);
        if (Build.VERSION.SDK_INT >= 21) {
            object.setFillColor(ColorStateList.valueOf((int)n));
        } else {
            object.setFillColor(ColorStateList.valueOf((int)0));
        }
        onDateSetListener = MaterialDialogs.getDialogBackgroundInsets((Context)onDateSetListener, 16843612, DEF_STYLE_RES);
        this.backgroundInsets = onDateSetListener;
        this.background = MaterialDialogs.insetDrawable((Drawable)object, (Rect)onDateSetListener);
    }

    public MaterialStyledDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener onDateSetListener, int n, int n2, int n3) {
        this(context, 0, onDateSetListener, n, n2, n3);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().setBackgroundDrawable(this.background);
        this.getWindow().getDecorView().setOnTouchListener((View.OnTouchListener)new InsetDialogOnTouchListener((Dialog)this, this.backgroundInsets));
    }
}

