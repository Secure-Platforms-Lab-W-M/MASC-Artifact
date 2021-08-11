/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.TabHost
 *  android.widget.TabHost$OnTabChangeListener
 */
package androidx.databinding.adapters;

import android.widget.TabHost;
import androidx.databinding.InverseBindingListener;

public class TabHostBindingAdapter {
    public static int getCurrentTab(TabHost tabHost) {
        return tabHost.getCurrentTab();
    }

    public static String getCurrentTabTag(TabHost tabHost) {
        return tabHost.getCurrentTabTag();
    }

    public static void setCurrentTab(TabHost tabHost, int n) {
        if (tabHost.getCurrentTab() != n) {
            tabHost.setCurrentTab(n);
        }
    }

    public static void setCurrentTabTag(TabHost tabHost, String string2) {
        if (tabHost.getCurrentTabTag() != string2) {
            tabHost.setCurrentTabByTag(string2);
        }
    }

    public static void setListeners(TabHost tabHost, final TabHost.OnTabChangeListener onTabChangeListener, final InverseBindingListener inverseBindingListener) {
        if (inverseBindingListener == null) {
            tabHost.setOnTabChangedListener(onTabChangeListener);
            return;
        }
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){

            public void onTabChanged(String string2) {
                TabHost.OnTabChangeListener onTabChangeListener2 = onTabChangeListener;
                if (onTabChangeListener2 != null) {
                    onTabChangeListener2.onTabChanged(string2);
                }
                inverseBindingListener.onChange();
            }
        });
    }

}

