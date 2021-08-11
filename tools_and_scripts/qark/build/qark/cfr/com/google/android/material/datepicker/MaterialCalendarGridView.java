/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.KeyEvent
 *  android.view.View
 *  android.widget.Adapter
 *  android.widget.GridView
 *  android.widget.ListAdapter
 *  com.google.android.material.R
 *  com.google.android.material.R$id
 */
package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.util.Pair;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.datepicker.CalendarItemStyle;
import com.google.android.material.datepicker.CalendarStyle;
import com.google.android.material.datepicker.DateSelector;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MonthAdapter;
import com.google.android.material.datepicker.UtcDates;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

final class MaterialCalendarGridView
extends GridView {
    private final Calendar dayCompute = UtcDates.getUtcCalendar();

    public MaterialCalendarGridView(Context context) {
        this(context, null);
    }

    public MaterialCalendarGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MaterialCalendarGridView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        if (MaterialDatePicker.isFullscreen(this.getContext())) {
            this.setNextFocusLeftId(R.id.cancel_button);
            this.setNextFocusRightId(R.id.confirm_button);
        }
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegateCompat(){

            @Override
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCollectionInfo(null);
            }
        });
    }

    private void gainFocus(int n, Rect rect) {
        if (n == 33) {
            this.setSelection(this.getAdapter().lastPositionInMonth());
            return;
        }
        if (n == 130) {
            this.setSelection(this.getAdapter().firstPositionInMonth());
            return;
        }
        super.onFocusChanged(true, n, rect);
    }

    private static int horizontalMidPoint(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    private static boolean skipMonth(Long l, Long l2, Long l3, Long l4) {
        boolean bl = true;
        if (l != null && l2 != null && l3 != null) {
            if (l4 == null) {
                return true;
            }
            if (l3 <= l2) {
                if (l4 < l) {
                    return true;
                }
                bl = false;
            }
            return bl;
        }
        return true;
    }

    public MonthAdapter getAdapter() {
        return (MonthAdapter)super.getAdapter();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getAdapter().notifyDataSetChanged();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected final void onDraw(Canvas var1_1) {
        super.onDraw(var1_1);
        var23_2 = this.getAdapter();
        var24_3 = var23_2.dateSelector;
        var26_4 = var23_2.calendarStyle;
        var22_5 = var23_2.getItem(var23_2.firstPositionInMonth());
        var21_6 = var23_2.getItem(var23_2.lastPositionInMonth());
        var20_7 = var24_3.getSelectedRanges().iterator();
        block0 : do lbl-1000: // 3 sources:
        {
            var25_24 = this;
            if (var20_7.hasNext() == false) return;
            var27_25 = var20_7.next();
            if (var27_25.first == null || var27_25.second == null) ** GOTO lbl-1000
            var16_22 = (Long)var27_25.first;
            var18_23 = (Long)var27_25.second;
            if (MaterialCalendarGridView.skipMonth(var22_5, var21_6, var16_22, var18_23)) {
                return;
            }
            if (var16_22 < var22_5) {
                var4_10 = var23_2.firstPositionInMonth();
                var2_8 = var23_2.isFirstInRow(var4_10) ? 0 : var25_24.getChildAt(var4_10 - 1).getRight();
            } else {
                var25_24.dayCompute.setTimeInMillis(var16_22);
                var4_10 = var23_2.dayToPosition(var25_24.dayCompute.get(5));
                var2_8 = MaterialCalendarGridView.horizontalMidPoint(var25_24.getChildAt(var4_10));
            }
            if (var18_23 > var21_6) {
                var5_11 = var23_2.lastPositionInMonth();
                var3_9 = var23_2.isLastInRow(var5_11) ? this.getWidth() : var25_24.getChildAt(var5_11).getRight();
            } else {
                var25_24.dayCompute.setTimeInMillis(var18_23);
                var5_11 = var23_2.dayToPosition(var25_24.dayCompute.get(5));
                var3_9 = MaterialCalendarGridView.horizontalMidPoint(var25_24.getChildAt(var5_11));
            }
            var6_12 = (int)var23_2.getItemId(var4_10);
            var10_16 = (int)var23_2.getItemId(var5_11);
            var7_13 = var6_12;
            do {
                if (var7_13 > var10_16) continue block0;
                var9_15 = var7_13 * this.getNumColumns();
                var15_21 = this.getNumColumns();
                var25_24 = this.getChildAt(var9_15);
                var11_17 = var25_24.getTop();
                var12_18 = var26_4.day.getTopInset();
                var13_19 = var25_24.getBottom();
                var14_20 = var26_4.day.getBottomInset();
                var8_14 = var9_15 > var4_10 ? 0 : var2_8;
                var9_15 = var5_11 > var9_15 + var15_21 - 1 ? this.getWidth() : var3_9;
                var1_1.drawRect((float)var8_14, (float)(var11_17 + var12_18), (float)var9_15, (float)(var13_19 - var14_20), var26_4.rangeFill);
                ++var7_13;
            } while (true);
            break;
        } while (true);
    }

    protected void onFocusChanged(boolean bl, int n, Rect rect) {
        if (bl) {
            this.gainFocus(n, rect);
            return;
        }
        super.onFocusChanged(false, n, rect);
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (!super.onKeyDown(n, keyEvent)) {
            return false;
        }
        if (this.getSelectedItemPosition() != -1) {
            if (this.getSelectedItemPosition() >= this.getAdapter().firstPositionInMonth()) {
                return true;
            }
            if (19 == n) {
                this.setSelection(this.getAdapter().firstPositionInMonth());
                return true;
            }
            return false;
        }
        return true;
    }

    public final void setAdapter(ListAdapter listAdapter) {
        if (listAdapter instanceof MonthAdapter) {
            super.setAdapter(listAdapter);
            return;
        }
        throw new IllegalArgumentException(String.format("%1$s must have its Adapter set to a %2$s", MaterialCalendarGridView.class.getCanonicalName(), MonthAdapter.class.getCanonicalName()));
    }

    public void setSelection(int n) {
        if (n < this.getAdapter().firstPositionInMonth()) {
            super.setSelection(this.getAdapter().firstPositionInMonth());
            return;
        }
        super.setSelection(n);
    }

}

