package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Collection;

public interface DateSelector extends Parcelable {
   int getDefaultThemeResId(Context var1);

   int getDefaultTitleResId();

   Collection getSelectedDays();

   Collection getSelectedRanges();

   Object getSelection();

   String getSelectionDisplayString(Context var1);

   boolean isSelectionComplete();

   View onCreateTextInputView(LayoutInflater var1, ViewGroup var2, Bundle var3, CalendarConstraints var4, OnSelectionChangedListener var5);

   void select(long var1);

   void setSelection(Object var1);
}
