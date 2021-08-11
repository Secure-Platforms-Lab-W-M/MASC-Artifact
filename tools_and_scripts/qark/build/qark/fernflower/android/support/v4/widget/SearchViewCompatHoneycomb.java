package android.support.v4.widget;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

@TargetApi(11)
@RequiresApi(11)
class SearchViewCompatHoneycomb {
   public static void checkIfLegalArg(View var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("searchView must be non-null");
      } else if (!(var0 instanceof SearchView)) {
         throw new IllegalArgumentException("searchView must be an instance ofandroid.widget.SearchView");
      }
   }

   public static CharSequence getQuery(View var0) {
      return ((SearchView)var0).getQuery();
   }

   public static boolean isIconified(View var0) {
      return ((SearchView)var0).isIconified();
   }

   public static boolean isQueryRefinementEnabled(View var0) {
      return ((SearchView)var0).isQueryRefinementEnabled();
   }

   public static boolean isSubmitButtonEnabled(View var0) {
      return ((SearchView)var0).isSubmitButtonEnabled();
   }

   public static Object newOnCloseListener(final SearchViewCompatHoneycomb.OnCloseListenerCompatBridge var0) {
      return new OnCloseListener() {
         public boolean onClose() {
            return var0.onClose();
         }
      };
   }

   public static Object newOnQueryTextListener(final SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge var0) {
      return new OnQueryTextListener() {
         public boolean onQueryTextChange(String var1) {
            return var0.onQueryTextChange(var1);
         }

         public boolean onQueryTextSubmit(String var1) {
            return var0.onQueryTextSubmit(var1);
         }
      };
   }

   public static View newSearchView(Context var0) {
      return new SearchView(var0);
   }

   public static void setIconified(View var0, boolean var1) {
      ((SearchView)var0).setIconified(var1);
   }

   public static void setMaxWidth(View var0, int var1) {
      ((SearchView)var0).setMaxWidth(var1);
   }

   public static void setOnCloseListener(View var0, Object var1) {
      ((SearchView)var0).setOnCloseListener((OnCloseListener)var1);
   }

   public static void setOnQueryTextListener(View var0, Object var1) {
      ((SearchView)var0).setOnQueryTextListener((OnQueryTextListener)var1);
   }

   public static void setQuery(View var0, CharSequence var1, boolean var2) {
      ((SearchView)var0).setQuery(var1, var2);
   }

   public static void setQueryHint(View var0, CharSequence var1) {
      ((SearchView)var0).setQueryHint(var1);
   }

   public static void setQueryRefinementEnabled(View var0, boolean var1) {
      ((SearchView)var0).setQueryRefinementEnabled(var1);
   }

   public static void setSearchableInfo(View var0, ComponentName var1) {
      SearchView var2 = (SearchView)var0;
      var2.setSearchableInfo(((SearchManager)var2.getContext().getSystemService("search")).getSearchableInfo(var1));
   }

   public static void setSubmitButtonEnabled(View var0, boolean var1) {
      ((SearchView)var0).setSubmitButtonEnabled(var1);
   }

   interface OnCloseListenerCompatBridge {
      boolean onClose();
   }

   interface OnQueryTextListenerCompatBridge {
      boolean onQueryTextChange(String var1);

      boolean onQueryTextSubmit(String var1);
   }
}
