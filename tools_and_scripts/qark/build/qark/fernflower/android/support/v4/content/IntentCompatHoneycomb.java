package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.RequiresApi;

@TargetApi(11)
@RequiresApi(11)
class IntentCompatHoneycomb {
   public static Intent makeMainActivity(ComponentName var0) {
      return Intent.makeMainActivity(var0);
   }

   public static Intent makeRestartActivityTask(ComponentName var0) {
      return Intent.makeRestartActivityTask(var0);
   }
}
