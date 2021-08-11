package android.support.v4.app;

import android.app.Activity;
import android.support.annotation.RestrictTo;
import android.support.v4.util.SimpleArrayMap;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SupportActivity extends Activity {
   private SimpleArrayMap mExtraDataMap = new SimpleArrayMap();

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public SupportActivity.ExtraData getExtraData(Class var1) {
      return (SupportActivity.ExtraData)this.mExtraDataMap.get(var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void putExtraData(SupportActivity.ExtraData var1) {
      this.mExtraDataMap.put(var1.getClass(), var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static class ExtraData {
   }
}
