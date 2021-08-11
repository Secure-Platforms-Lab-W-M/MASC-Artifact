/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.widget.SearchView
 *  android.widget.SearchView$OnQueryTextListener
 *  android.widget.SearchView$OnSuggestionListener
 */
package androidx.databinding.adapters;

import android.os.Build;
import android.widget.SearchView;

public class SearchViewBindingAdapter {
    public static void setOnQueryTextListener(SearchView searchView, final OnQueryTextSubmit onQueryTextSubmit, final OnQueryTextChange onQueryTextChange) {
        if (Build.VERSION.SDK_INT >= 11) {
            if (onQueryTextSubmit == null && onQueryTextChange == null) {
                searchView.setOnQueryTextListener(null);
                return;
            }
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

                public boolean onQueryTextChange(String string2) {
                    OnQueryTextChange onQueryTextChange2 = onQueryTextChange;
                    if (onQueryTextChange2 != null) {
                        return onQueryTextChange2.onQueryTextChange(string2);
                    }
                    return false;
                }

                public boolean onQueryTextSubmit(String string2) {
                    OnQueryTextSubmit onQueryTextSubmit2 = onQueryTextSubmit;
                    if (onQueryTextSubmit2 != null) {
                        return onQueryTextSubmit2.onQueryTextSubmit(string2);
                    }
                    return false;
                }
            });
        }
    }

    public static void setOnSuggestListener(SearchView searchView, final OnSuggestionSelect onSuggestionSelect, final OnSuggestionClick onSuggestionClick) {
        if (Build.VERSION.SDK_INT >= 11) {
            if (onSuggestionSelect == null && onSuggestionClick == null) {
                searchView.setOnSuggestionListener(null);
                return;
            }
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener(){

                public boolean onSuggestionClick(int n) {
                    OnSuggestionClick onSuggestionClick2 = onSuggestionClick;
                    if (onSuggestionClick2 != null) {
                        return onSuggestionClick2.onSuggestionClick(n);
                    }
                    return false;
                }

                public boolean onSuggestionSelect(int n) {
                    OnSuggestionSelect onSuggestionSelect2 = onSuggestionSelect;
                    if (onSuggestionSelect2 != null) {
                        return onSuggestionSelect2.onSuggestionSelect(n);
                    }
                    return false;
                }
            });
        }
    }

    public static interface OnQueryTextChange {
        public boolean onQueryTextChange(String var1);
    }

    public static interface OnQueryTextSubmit {
        public boolean onQueryTextSubmit(String var1);
    }

    public static interface OnSuggestionClick {
        public boolean onSuggestionClick(int var1);
    }

    public static interface OnSuggestionSelect {
        public boolean onSuggestionSelect(int var1);
    }

}

