package android.support.v7.view;

import android.support.annotation.RestrictTo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public abstract class ActionMode {
   private Object mTag;
   private boolean mTitleOptionalHint;

   public abstract void finish();

   public abstract View getCustomView();

   public abstract Menu getMenu();

   public abstract MenuInflater getMenuInflater();

   public abstract CharSequence getSubtitle();

   public Object getTag() {
      return this.mTag;
   }

   public abstract CharSequence getTitle();

   public boolean getTitleOptionalHint() {
      return this.mTitleOptionalHint;
   }

   public abstract void invalidate();

   public boolean isTitleOptional() {
      return false;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean isUiFocusable() {
      return true;
   }

   public abstract void setCustomView(View var1);

   public abstract void setSubtitle(int var1);

   public abstract void setSubtitle(CharSequence var1);

   public void setTag(Object var1) {
      this.mTag = var1;
   }

   public abstract void setTitle(int var1);

   public abstract void setTitle(CharSequence var1);

   public void setTitleOptionalHint(boolean var1) {
      this.mTitleOptionalHint = var1;
   }

   public interface Callback {
      boolean onActionItemClicked(ActionMode var1, MenuItem var2);

      boolean onCreateActionMode(ActionMode var1, Menu var2);

      void onDestroyActionMode(ActionMode var1);

      boolean onPrepareActionMode(ActionMode var1, Menu var2);
   }
}
