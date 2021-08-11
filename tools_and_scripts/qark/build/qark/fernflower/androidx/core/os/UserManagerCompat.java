package androidx.core.os;

import android.content.Context;
import android.os.UserManager;
import android.os.Build.VERSION;

public class UserManagerCompat {
   private UserManagerCompat() {
   }

   public static boolean isUserUnlocked(Context var0) {
      return VERSION.SDK_INT >= 24 ? ((UserManager)var0.getSystemService(UserManager.class)).isUserUnlocked() : true;
   }
}
