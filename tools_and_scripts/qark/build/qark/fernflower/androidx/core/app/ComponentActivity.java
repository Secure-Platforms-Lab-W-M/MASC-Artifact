package androidx.core.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.collection.SimpleArrayMap;
import androidx.core.view.KeyEventDispatcher;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;

public class ComponentActivity extends Activity implements LifecycleOwner, KeyEventDispatcher.Component {
   private SimpleArrayMap mExtraDataMap = new SimpleArrayMap();
   private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

   public boolean dispatchKeyEvent(KeyEvent var1) {
      View var2 = this.getWindow().getDecorView();
      return var2 != null && KeyEventDispatcher.dispatchBeforeHierarchy(var2, var1) ? true : KeyEventDispatcher.dispatchKeyEvent(this, var2, this, var1);
   }

   public boolean dispatchKeyShortcutEvent(KeyEvent var1) {
      View var2 = this.getWindow().getDecorView();
      return var2 != null && KeyEventDispatcher.dispatchBeforeHierarchy(var2, var1) ? true : super.dispatchKeyShortcutEvent(var1);
   }

   public ComponentActivity.ExtraData getExtraData(Class var1) {
      return (ComponentActivity.ExtraData)this.mExtraDataMap.get(var1);
   }

   public Lifecycle getLifecycle() {
      return this.mLifecycleRegistry;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      ReportFragment.injectIfNeededIn(this);
   }

   protected void onSaveInstanceState(Bundle var1) {
      this.mLifecycleRegistry.markState(Lifecycle.State.CREATED);
      super.onSaveInstanceState(var1);
   }

   public void putExtraData(ComponentActivity.ExtraData var1) {
      this.mExtraDataMap.put(var1.getClass(), var1);
   }

   public boolean superDispatchKeyEvent(KeyEvent var1) {
      return super.dispatchKeyEvent(var1);
   }

   public static class ExtraData {
   }
}
