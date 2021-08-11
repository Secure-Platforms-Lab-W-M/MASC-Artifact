package androidx.lifecycle;

import android.app.Application;

public class AndroidViewModel extends ViewModel {
   private Application mApplication;

   public AndroidViewModel(Application var1) {
      this.mApplication = var1;
   }

   public Application getApplication() {
      return this.mApplication;
   }
}
