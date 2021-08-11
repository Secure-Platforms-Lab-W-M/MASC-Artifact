/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.AbsListView
 *  android.widget.AbsListView$OnScrollListener
 */
package androidx.databinding.adapters;

import android.widget.AbsListView;

public class AbsListViewBindingAdapter {
    public static void setOnScroll(AbsListView absListView, final OnScroll onScroll, final OnScrollStateChanged onScrollStateChanged) {
        absListView.setOnScrollListener(new AbsListView.OnScrollListener(){

            public void onScroll(AbsListView absListView, int n, int n2, int n3) {
                OnScroll onScroll2 = onScroll;
                if (onScroll2 != null) {
                    onScroll2.onScroll(absListView, n, n2, n3);
                }
            }

            public void onScrollStateChanged(AbsListView absListView, int n) {
                OnScrollStateChanged onScrollStateChanged2 = onScrollStateChanged;
                if (onScrollStateChanged2 != null) {
                    onScrollStateChanged2.onScrollStateChanged(absListView, n);
                }
            }
        });
    }

    public static interface OnScroll {
        public void onScroll(AbsListView var1, int var2, int var3, int var4);
    }

    public static interface OnScrollStateChanged {
        public void onScrollStateChanged(AbsListView var1, int var2);
    }

}

