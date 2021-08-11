package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.SpinnerAdapter;

public interface ThemedSpinnerAdapter extends SpinnerAdapter {
   @Nullable
   Theme getDropDownViewTheme();

   void setDropDownViewTheme(@Nullable Theme var1);

   public static final class Helper {
      private final Context mContext;
      private LayoutInflater mDropDownInflater;
      private final LayoutInflater mInflater;

      public Helper(@NonNull Context var1) {
         this.mContext = var1;
         this.mInflater = LayoutInflater.from(var1);
      }

      @NonNull
      public LayoutInflater getDropDownViewInflater() {
         LayoutInflater var1 = this.mDropDownInflater;
         return var1 != null ? var1 : this.mInflater;
      }

      @Nullable
      public Theme getDropDownViewTheme() {
         LayoutInflater var1 = this.mDropDownInflater;
         return var1 == null ? null : var1.getContext().getTheme();
      }

      public void setDropDownViewTheme(@Nullable Theme var1) {
         if (var1 == null) {
            this.mDropDownInflater = null;
         } else if (var1 == this.mContext.getTheme()) {
            this.mDropDownInflater = this.mInflater;
         } else {
            this.mDropDownInflater = LayoutInflater.from(new ContextThemeWrapper(this.mContext, var1));
         }
      }
   }
}
