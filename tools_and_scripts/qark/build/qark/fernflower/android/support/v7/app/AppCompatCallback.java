package android.support.v7.app;

import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;

public interface AppCompatCallback {
   void onSupportActionModeFinished(ActionMode var1);

   void onSupportActionModeStarted(ActionMode var1);

   @Nullable
   ActionMode onWindowStartingSupportActionMode(ActionMode.Callback var1);
}
