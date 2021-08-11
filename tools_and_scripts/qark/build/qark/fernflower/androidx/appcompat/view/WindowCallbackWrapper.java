package androidx.appcompat.view;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window.Callback;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

public class WindowCallbackWrapper implements Callback {
   final Callback mWrapped;

   public WindowCallbackWrapper(Callback var1) {
      if (var1 != null) {
         this.mWrapped = var1;
      } else {
         throw new IllegalArgumentException("Window callback may not be null");
      }
   }

   public boolean dispatchGenericMotionEvent(MotionEvent var1) {
      return this.mWrapped.dispatchGenericMotionEvent(var1);
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      return this.mWrapped.dispatchKeyEvent(var1);
   }

   public boolean dispatchKeyShortcutEvent(KeyEvent var1) {
      return this.mWrapped.dispatchKeyShortcutEvent(var1);
   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      return this.mWrapped.dispatchPopulateAccessibilityEvent(var1);
   }

   public boolean dispatchTouchEvent(MotionEvent var1) {
      return this.mWrapped.dispatchTouchEvent(var1);
   }

   public boolean dispatchTrackballEvent(MotionEvent var1) {
      return this.mWrapped.dispatchTrackballEvent(var1);
   }

   public final Callback getWrapped() {
      return this.mWrapped;
   }

   public void onActionModeFinished(android.view.ActionMode var1) {
      this.mWrapped.onActionModeFinished(var1);
   }

   public void onActionModeStarted(android.view.ActionMode var1) {
      this.mWrapped.onActionModeStarted(var1);
   }

   public void onAttachedToWindow() {
      this.mWrapped.onAttachedToWindow();
   }

   public void onContentChanged() {
      this.mWrapped.onContentChanged();
   }

   public boolean onCreatePanelMenu(int var1, Menu var2) {
      return this.mWrapped.onCreatePanelMenu(var1, var2);
   }

   public View onCreatePanelView(int var1) {
      return this.mWrapped.onCreatePanelView(var1);
   }

   public void onDetachedFromWindow() {
      this.mWrapped.onDetachedFromWindow();
   }

   public boolean onMenuItemSelected(int var1, MenuItem var2) {
      return this.mWrapped.onMenuItemSelected(var1, var2);
   }

   public boolean onMenuOpened(int var1, Menu var2) {
      return this.mWrapped.onMenuOpened(var1, var2);
   }

   public void onPanelClosed(int var1, Menu var2) {
      this.mWrapped.onPanelClosed(var1, var2);
   }

   public void onPointerCaptureChanged(boolean var1) {
      this.mWrapped.onPointerCaptureChanged(var1);
   }

   public boolean onPreparePanel(int var1, View var2, Menu var3) {
      return this.mWrapped.onPreparePanel(var1, var2, var3);
   }

   public void onProvideKeyboardShortcuts(List var1, Menu var2, int var3) {
      this.mWrapped.onProvideKeyboardShortcuts(var1, var2, var3);
   }

   public boolean onSearchRequested() {
      return this.mWrapped.onSearchRequested();
   }

   public boolean onSearchRequested(SearchEvent var1) {
      return this.mWrapped.onSearchRequested(var1);
   }

   public void onWindowAttributesChanged(LayoutParams var1) {
      this.mWrapped.onWindowAttributesChanged(var1);
   }

   public void onWindowFocusChanged(boolean var1) {
      this.mWrapped.onWindowFocusChanged(var1);
   }

   public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback var1) {
      return this.mWrapped.onWindowStartingActionMode(var1);
   }

   public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback var1, int var2) {
      return this.mWrapped.onWindowStartingActionMode(var1, var2);
   }
}
