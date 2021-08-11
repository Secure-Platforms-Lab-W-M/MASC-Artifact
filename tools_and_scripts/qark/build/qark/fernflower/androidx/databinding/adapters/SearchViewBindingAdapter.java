package androidx.databinding.adapters;

import android.os.Build.VERSION;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;

public class SearchViewBindingAdapter {
   public static void setOnQueryTextListener(SearchView var0, final SearchViewBindingAdapter.OnQueryTextSubmit var1, final SearchViewBindingAdapter.OnQueryTextChange var2) {
      if (VERSION.SDK_INT >= 11) {
         if (var1 == null && var2 == null) {
            var0.setOnQueryTextListener((OnQueryTextListener)null);
            return;
         }

         var0.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextChange(String var1x) {
               SearchViewBindingAdapter.OnQueryTextChange var2x = var2;
               return var2x != null ? var2x.onQueryTextChange(var1x) : false;
            }

            public boolean onQueryTextSubmit(String var1x) {
               SearchViewBindingAdapter.OnQueryTextSubmit var2x = var1;
               return var2x != null ? var2x.onQueryTextSubmit(var1x) : false;
            }
         });
      }

   }

   public static void setOnSuggestListener(SearchView var0, final SearchViewBindingAdapter.OnSuggestionSelect var1, final SearchViewBindingAdapter.OnSuggestionClick var2) {
      if (VERSION.SDK_INT >= 11) {
         if (var1 == null && var2 == null) {
            var0.setOnSuggestionListener((OnSuggestionListener)null);
            return;
         }

         var0.setOnSuggestionListener(new OnSuggestionListener() {
            public boolean onSuggestionClick(int var1x) {
               SearchViewBindingAdapter.OnSuggestionClick var2x = var2;
               return var2x != null ? var2x.onSuggestionClick(var1x) : false;
            }

            public boolean onSuggestionSelect(int var1x) {
               SearchViewBindingAdapter.OnSuggestionSelect var2x = var1;
               return var2x != null ? var2x.onSuggestionSelect(var1x) : false;
            }
         });
      }

   }

   public interface OnQueryTextChange {
      boolean onQueryTextChange(String var1);
   }

   public interface OnQueryTextSubmit {
      boolean onQueryTextSubmit(String var1);
   }

   public interface OnSuggestionClick {
      boolean onSuggestionClick(int var1);
   }

   public interface OnSuggestionSelect {
      boolean onSuggestionSelect(int var1);
   }
}
