package android.support.v4.app;

import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
abstract class BaseFragmentActivityApi16 extends BaseFragmentActivityApi14 {
   boolean mStartedActivityFromFragment;

   @RequiresApi(16)
   public void startActivityForResult(Intent var1, int var2, @Nullable Bundle var3) {
      if (!this.mStartedActivityFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startActivityForResult(var1, var2, var3);
   }

   @RequiresApi(16)
   public void startIntentSenderForResult(IntentSender var1, int var2, @Nullable Intent var3, int var4, int var5, int var6, Bundle var7) throws SendIntentException {
      if (!this.mStartedIntentSenderFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startIntentSenderForResult(var1, var2, var3, var4, var5, var6, var7);
   }
}
