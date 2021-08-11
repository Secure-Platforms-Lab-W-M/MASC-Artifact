package androidx.databinding;

public abstract class OnRebindCallback {
   public void onBound(ViewDataBinding var1) {
   }

   public void onCanceled(ViewDataBinding var1) {
   }

   public boolean onPreBind(ViewDataBinding var1) {
      return true;
   }
}
