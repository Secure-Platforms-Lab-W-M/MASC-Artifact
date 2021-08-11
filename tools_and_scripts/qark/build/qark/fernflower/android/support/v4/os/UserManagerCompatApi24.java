package android.support.v4.os;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.UserManager;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;

@TargetApi(24)
@RequiresApi(24)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class UserManagerCompatApi24 {
   public static boolean isUserUnlocked(Context var0) {
      return ((UserManager)var0.getSystemService(UserManager.class)).isUserUnlocked();
   }
}
