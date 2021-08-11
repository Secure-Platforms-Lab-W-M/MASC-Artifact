/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.SearchManager
 *  android.app.SearchableInfo
 *  android.content.ComponentName
 *  android.content.Context
 *  android.view.View
 *  android.widget.SearchView
 *  android.widget.SearchView$OnCloseListener
 *  android.widget.SearchView$OnQueryTextListener
 */
package android.support.v4.widget;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.widget.SearchView;

@Deprecated
public final class SearchViewCompat {
    private SearchViewCompat(Context context) {
    }

    private static void checkIfLegalArg(View view) {
        if (view != null) {
            if (view instanceof SearchView) {
                return;
            }
            throw new IllegalArgumentException("searchView must be an instance of android.widget.SearchView");
        }
        throw new IllegalArgumentException("searchView must be non-null");
    }

    @Deprecated
    public static CharSequence getQuery(View view) {
        SearchViewCompat.checkIfLegalArg(view);
        return ((SearchView)view).getQuery();
    }

    @Deprecated
    public static boolean isIconified(View view) {
        SearchViewCompat.checkIfLegalArg(view);
        return ((SearchView)view).isIconified();
    }

    @Deprecated
    public static boolean isQueryRefinementEnabled(View view) {
        SearchViewCompat.checkIfLegalArg(view);
        return ((SearchView)view).isQueryRefinementEnabled();
    }

    @Deprecated
    public static boolean isSubmitButtonEnabled(View view) {
        SearchViewCompat.checkIfLegalArg(view);
        return ((SearchView)view).isSubmitButtonEnabled();
    }

    private static SearchView.OnCloseListener newOnCloseListener(final OnCloseListener onCloseListener) {
        return new SearchView.OnCloseListener(){

            public boolean onClose() {
                return onCloseListener.onClose();
            }
        };
    }

    private static SearchView.OnQueryTextListener newOnQueryTextListener(final OnQueryTextListener onQueryTextListener) {
        return new SearchView.OnQueryTextListener(){

            public boolean onQueryTextChange(String string2) {
                return onQueryTextListener.onQueryTextChange(string2);
            }

            public boolean onQueryTextSubmit(String string2) {
                return onQueryTextListener.onQueryTextSubmit(string2);
            }
        };
    }

    @Deprecated
    public static View newSearchView(Context context) {
        return new SearchView(context);
    }

    @Deprecated
    public static void setIconified(View view, boolean bl) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setIconified(bl);
    }

    @Deprecated
    public static void setImeOptions(View view, int n) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setImeOptions(n);
    }

    @Deprecated
    public static void setInputType(View view, int n) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setInputType(n);
    }

    @Deprecated
    public static void setMaxWidth(View view, int n) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setMaxWidth(n);
    }

    @Deprecated
    public static void setOnCloseListener(View view, OnCloseListener onCloseListener) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setOnCloseListener(SearchViewCompat.newOnCloseListener(onCloseListener));
    }

    @Deprecated
    public static void setOnQueryTextListener(View view, OnQueryTextListener onQueryTextListener) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setOnQueryTextListener(SearchViewCompat.newOnQueryTextListener(onQueryTextListener));
    }

    @Deprecated
    public static void setQuery(View view, CharSequence charSequence, boolean bl) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setQuery(charSequence, bl);
    }

    @Deprecated
    public static void setQueryHint(View view, CharSequence charSequence) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setQueryHint(charSequence);
    }

    @Deprecated
    public static void setQueryRefinementEnabled(View view, boolean bl) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setQueryRefinementEnabled(bl);
    }

    @Deprecated
    public static void setSearchableInfo(View view, ComponentName componentName) {
        SearchViewCompat.checkIfLegalArg(view);
        SearchManager searchManager = (SearchManager)view.getContext().getSystemService("search");
        ((SearchView)view).setSearchableInfo(searchManager.getSearchableInfo(componentName));
    }

    @Deprecated
    public static void setSubmitButtonEnabled(View view, boolean bl) {
        SearchViewCompat.checkIfLegalArg(view);
        ((SearchView)view).setSubmitButtonEnabled(bl);
    }

    @Deprecated
    public static interface OnCloseListener {
        public boolean onClose();
    }

    @Deprecated
    public static abstract class OnCloseListenerCompat
    implements OnCloseListener {
        @Override
        public boolean onClose() {
            return false;
        }
    }

    @Deprecated
    public static interface OnQueryTextListener {
        public boolean onQueryTextChange(String var1);

        public boolean onQueryTextSubmit(String var1);
    }

    @Deprecated
    public static abstract class OnQueryTextListenerCompat
    implements OnQueryTextListener {
        @Override
        public boolean onQueryTextChange(String string2) {
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String string2) {
            return false;
        }
    }

}

