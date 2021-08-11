/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 */
package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.util.Pair;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.OnSelectionChangedListener;
import java.util.Collection;

public interface DateSelector<S>
extends Parcelable {
    public int getDefaultThemeResId(Context var1);

    public int getDefaultTitleResId();

    public Collection<Long> getSelectedDays();

    public Collection<Pair<Long, Long>> getSelectedRanges();

    public S getSelection();

    public String getSelectionDisplayString(Context var1);

    public boolean isSelectionComplete();

    public View onCreateTextInputView(LayoutInflater var1, ViewGroup var2, Bundle var3, CalendarConstraints var4, OnSelectionChangedListener<S> var5);

    public void select(long var1);

    public void setSelection(S var1);
}

