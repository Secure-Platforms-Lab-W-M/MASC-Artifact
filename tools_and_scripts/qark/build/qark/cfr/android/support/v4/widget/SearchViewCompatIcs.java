/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.View
 *  android.widget.SearchView
 */
package android.support.v4.widget;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;

class SearchViewCompatIcs {
    SearchViewCompatIcs() {
    }

    public static View newSearchView(Context context) {
        return new MySearchView(context);
    }

    public static void setImeOptions(View view, int n) {
        ((SearchView)view).setImeOptions(n);
    }

    public static void setInputType(View view, int n) {
        ((SearchView)view).setInputType(n);
    }

    public static class MySearchView
    extends SearchView {
        public MySearchView(Context context) {
            super(context);
        }

        public void onActionViewCollapsed() {
            this.setQuery((CharSequence)"", false);
            super.onActionViewCollapsed();
        }
    }

}

