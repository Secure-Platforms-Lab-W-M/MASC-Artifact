package android.support.v4.widget;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.widget.SearchView;

@Deprecated
public final class SearchViewCompat {
   private SearchViewCompat(Context var1) {
   }

   private static void checkIfLegalArg(View var0) {
      if (var0 != null) {
         if (!(var0 instanceof SearchView)) {
            throw new IllegalArgumentException("searchView must be an instance of android.widget.SearchView");
         }
      } else {
         throw new IllegalArgumentException("searchView must be non-null");
      }
   }

   @Deprecated
   public static CharSequence getQuery(View var0) {
      checkIfLegalArg(var0);
      return ((SearchView)var0).getQuery();
   }

   @Deprecated
   public static boolean isIconified(View var0) {
      checkIfLegalArg(var0);
      return ((SearchView)var0).isIconified();
   }

   @Deprecated
   public static boolean isQueryRefinementEnabled(View var0) {
      checkIfLegalArg(var0);
      return ((SearchView)var0).isQueryRefinementEnabled();
   }

   @Deprecated
   public static boolean isSubmitButtonEnabled(View var0) {
      checkIfLegalArg(var0);
      return ((SearchView)var0).isSubmitButtonEnabled();
   }

   private static android.widget.SearchView.OnCloseListener newOnCloseListener(final SearchViewCompat.OnCloseListener var0) {
      return new android.widget.SearchView.OnCloseListener() {
         public boolean onClose() {
            return var0.onClose();
         }
      };
   }

   private static android.widget.SearchView.OnQueryTextListener newOnQueryTextListener(final SearchViewCompat.OnQueryTextListener var0) {
      return new android.widget.SearchView.OnQueryTextListener() {
         public boolean onQueryTextChange(String var1) {
            return var0.onQueryTextChange(var1);
         }

         public boolean onQueryTextSubmit(String var1) {
            return var0.onQueryTextSubmit(var1);
         }
      };
   }

   @Deprecated
   public static View newSearchView(Context var0) {
      return new SearchView(var0);
   }

   @Deprecated
   public static void setIconified(View var0, boolean var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setIconified(var1);
   }

   @Deprecated
   public static void setImeOptions(View var0, int var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setImeOptions(var1);
   }

   @Deprecated
   public static void setInputType(View var0, int var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setInputType(var1);
   }

   @Deprecated
   public static void setMaxWidth(View var0, int var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setMaxWidth(var1);
   }

   @Deprecated
   public static void setOnCloseListener(View var0, SearchViewCompat.OnCloseListener var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setOnCloseListener(newOnCloseListener(var1));
   }

   @Deprecated
   public static void setOnQueryTextListener(View var0, SearchViewCompat.OnQueryTextListener var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setOnQueryTextListener(newOnQueryTextListener(var1));
   }

   @Deprecated
   public static void setQuery(View var0, CharSequence var1, boolean var2) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setQuery(var1, var2);
   }

   @Deprecated
   public static void setQueryHint(View var0, CharSequence var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setQueryHint(var1);
   }

   @Deprecated
   public static void setQueryRefinementEnabled(View var0, boolean var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setQueryRefinementEnabled(var1);
   }

   @Deprecated
   public static void setSearchableInfo(View var0, ComponentName var1) {
      checkIfLegalArg(var0);
      SearchManager var2 = (SearchManager)var0.getContext().getSystemService("search");
      ((SearchView)var0).setSearchableInfo(var2.getSearchableInfo(var1));
   }

   @Deprecated
   public static void setSubmitButtonEnabled(View var0, boolean var1) {
      checkIfLegalArg(var0);
      ((SearchView)var0).setSubmitButtonEnabled(var1);
   }

   @Deprecated
   public interface OnCloseListener {
      boolean onClose();
   }

   @Deprecated
   public abstract static class OnCloseListenerCompat implements SearchViewCompat.OnCloseListener {
      public boolean onClose() {
         return false;
      }
   }

   @Deprecated
   public interface OnQueryTextListener {
      boolean onQueryTextChange(String var1);

      boolean onQueryTextSubmit(String var1);
   }

   @Deprecated
   public abstract static class OnQueryTextListenerCompat implements SearchViewCompat.OnQueryTextListener {
      public boolean onQueryTextChange(String var1) {
         return false;
      }

      public boolean onQueryTextSubmit(String var1) {
         return false;
      }
   }
}
