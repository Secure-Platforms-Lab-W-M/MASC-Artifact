package androidx.multidex;

import android.app.Application;
import android.content.Context;

public class MultiDexApplication extends Application {
   protected void attachBaseContext(Context var1) {
      super.attachBaseContext(var1);
      MultiDex.install(this);
   }
}
