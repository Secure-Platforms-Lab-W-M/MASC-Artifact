package androidx.viewpager2.widget;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

final class CompositeOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
   private final List mCallbacks;

   CompositeOnPageChangeCallback(int var1) {
      this.mCallbacks = new ArrayList(var1);
   }

   private void throwCallbackListModifiedWhileInUse(ConcurrentModificationException var1) {
      throw new IllegalStateException("Adding and removing callbacks during dispatch to callbacks is not supported", var1);
   }

   void addOnPageChangeCallback(ViewPager2.OnPageChangeCallback var1) {
      this.mCallbacks.add(var1);
   }

   public void onPageScrollStateChanged(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void onPageScrolled(int param1, float param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public void onPageSelected(int param1) {
      // $FF: Couldn't be decompiled
   }

   void removeOnPageChangeCallback(ViewPager2.OnPageChangeCallback var1) {
      this.mCallbacks.remove(var1);
   }
}
