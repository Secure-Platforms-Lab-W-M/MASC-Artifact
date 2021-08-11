package androidx.customview.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
   private static final String DEFAULT_CLASS_NAME = "android.view.View";
   public static final int HOST_ID = -1;
   public static final int INVALID_ID = Integer.MIN_VALUE;
   private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
   private static final FocusStrategy.BoundsAdapter NODE_ADAPTER = new FocusStrategy.BoundsAdapter() {
      public void obtainBounds(AccessibilityNodeInfoCompat var1, Rect var2) {
         var1.getBoundsInParent(var2);
      }
   };
   private static final FocusStrategy.CollectionAdapter SPARSE_VALUES_ADAPTER = new FocusStrategy.CollectionAdapter() {
      public AccessibilityNodeInfoCompat get(SparseArrayCompat var1, int var2) {
         return (AccessibilityNodeInfoCompat)var1.valueAt(var2);
      }

      public int size(SparseArrayCompat var1) {
         return var1.size();
      }
   };
   int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
   private final View mHost;
   private int mHoveredVirtualViewId = Integer.MIN_VALUE;
   int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
   private final AccessibilityManager mManager;
   private ExploreByTouchHelper.MyNodeProvider mNodeProvider;
   private final int[] mTempGlobalRect = new int[2];
   private final Rect mTempParentRect = new Rect();
   private final Rect mTempScreenRect = new Rect();
   private final Rect mTempVisibleRect = new Rect();

   public ExploreByTouchHelper(View var1) {
      if (var1 != null) {
         this.mHost = var1;
         this.mManager = (AccessibilityManager)var1.getContext().getSystemService("accessibility");
         var1.setFocusable(true);
         if (ViewCompat.getImportantForAccessibility(var1) == 0) {
            ViewCompat.setImportantForAccessibility(var1, 1);
         }

      } else {
         throw new IllegalArgumentException("View may not be null");
      }
   }

   private boolean clearAccessibilityFocus(int var1) {
      if (this.mAccessibilityFocusedVirtualViewId == var1) {
         this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
         this.mHost.invalidate();
         this.sendEventForVirtualView(var1, 65536);
         return true;
      } else {
         return false;
      }
   }

   private boolean clickKeyboardFocusedVirtualView() {
      int var1 = this.mKeyboardFocusedVirtualViewId;
      return var1 != Integer.MIN_VALUE && this.onPerformActionForVirtualView(var1, 16, (Bundle)null);
   }

   private AccessibilityEvent createEvent(int var1, int var2) {
      return var1 != -1 ? this.createEventForChild(var1, var2) : this.createEventForHost(var2);
   }

   private AccessibilityEvent createEventForChild(int var1, int var2) {
      AccessibilityEvent var3 = AccessibilityEvent.obtain(var2);
      AccessibilityNodeInfoCompat var4 = this.obtainAccessibilityNodeInfo(var1);
      var3.getText().add(var4.getText());
      var3.setContentDescription(var4.getContentDescription());
      var3.setScrollable(var4.isScrollable());
      var3.setPassword(var4.isPassword());
      var3.setEnabled(var4.isEnabled());
      var3.setChecked(var4.isChecked());
      this.onPopulateEventForVirtualView(var1, var3);
      if (var3.getText().isEmpty() && var3.getContentDescription() == null) {
         throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
      } else {
         var3.setClassName(var4.getClassName());
         AccessibilityRecordCompat.setSource(var3, this.mHost, var1);
         var3.setPackageName(this.mHost.getContext().getPackageName());
         return var3;
      }
   }

   private AccessibilityEvent createEventForHost(int var1) {
      AccessibilityEvent var2 = AccessibilityEvent.obtain(var1);
      this.mHost.onInitializeAccessibilityEvent(var2);
      return var2;
   }

   private AccessibilityNodeInfoCompat createNodeForChild(int var1) {
      AccessibilityNodeInfoCompat var4 = AccessibilityNodeInfoCompat.obtain();
      var4.setEnabled(true);
      var4.setFocusable(true);
      var4.setClassName("android.view.View");
      var4.setBoundsInParent(INVALID_PARENT_BOUNDS);
      var4.setBoundsInScreen(INVALID_PARENT_BOUNDS);
      var4.setParent(this.mHost);
      this.onPopulateNodeForVirtualView(var1, var4);
      if (var4.getText() == null && var4.getContentDescription() == null) {
         throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
      } else {
         var4.getBoundsInParent(this.mTempParentRect);
         if (!this.mTempParentRect.equals(INVALID_PARENT_BOUNDS)) {
            int var2 = var4.getActions();
            if ((var2 & 64) != 0) {
               throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            } else if ((var2 & 128) == 0) {
               var4.setPackageName(this.mHost.getContext().getPackageName());
               var4.setSource(this.mHost, var1);
               if (this.mAccessibilityFocusedVirtualViewId == var1) {
                  var4.setAccessibilityFocused(true);
                  var4.addAction(128);
               } else {
                  var4.setAccessibilityFocused(false);
                  var4.addAction(64);
               }

               boolean var3;
               if (this.mKeyboardFocusedVirtualViewId == var1) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               if (var3) {
                  var4.addAction(2);
               } else if (var4.isFocusable()) {
                  var4.addAction(1);
               }

               var4.setFocused(var3);
               this.mHost.getLocationOnScreen(this.mTempGlobalRect);
               var4.getBoundsInScreen(this.mTempScreenRect);
               if (this.mTempScreenRect.equals(INVALID_PARENT_BOUNDS)) {
                  var4.getBoundsInParent(this.mTempScreenRect);
                  if (var4.mParentVirtualDescendantId != -1) {
                     AccessibilityNodeInfoCompat var5 = AccessibilityNodeInfoCompat.obtain();

                     for(var1 = var4.mParentVirtualDescendantId; var1 != -1; var1 = var5.mParentVirtualDescendantId) {
                        var5.setParent(this.mHost, -1);
                        var5.setBoundsInParent(INVALID_PARENT_BOUNDS);
                        this.onPopulateNodeForVirtualView(var1, var5);
                        var5.getBoundsInParent(this.mTempParentRect);
                        this.mTempScreenRect.offset(this.mTempParentRect.left, this.mTempParentRect.top);
                     }

                     var5.recycle();
                  }

                  this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
               }

               if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect)) {
                  this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                  if (this.mTempScreenRect.intersect(this.mTempVisibleRect)) {
                     var4.setBoundsInScreen(this.mTempScreenRect);
                     if (this.isVisibleToUser(this.mTempScreenRect)) {
                        var4.setVisibleToUser(true);
                     }
                  }
               }

               return var4;
            } else {
               throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            }
         } else {
            throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
         }
      }
   }

   private AccessibilityNodeInfoCompat createNodeForHost() {
      AccessibilityNodeInfoCompat var3 = AccessibilityNodeInfoCompat.obtain(this.mHost);
      ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, var3);
      ArrayList var4 = new ArrayList();
      this.getVisibleVirtualViews(var4);
      if (var3.getChildCount() > 0 && var4.size() > 0) {
         throw new RuntimeException("Views cannot have both real and virtual children");
      } else {
         int var1 = 0;

         for(int var2 = var4.size(); var1 < var2; ++var1) {
            var3.addChild(this.mHost, (Integer)var4.get(var1));
         }

         return var3;
      }
   }

   private SparseArrayCompat getAllNodes() {
      ArrayList var2 = new ArrayList();
      this.getVisibleVirtualViews(var2);
      SparseArrayCompat var3 = new SparseArrayCompat();

      for(int var1 = 0; var1 < var2.size(); ++var1) {
         var3.put(var1, this.createNodeForChild(var1));
      }

      return var3;
   }

   private void getBoundsInParent(int var1, Rect var2) {
      this.obtainAccessibilityNodeInfo(var1).getBoundsInParent(var2);
   }

   private static Rect guessPreviouslyFocusedRect(View var0, int var1, Rect var2) {
      int var3 = var0.getWidth();
      int var4 = var0.getHeight();
      if (var1 != 17) {
         if (var1 != 33) {
            if (var1 != 66) {
               if (var1 == 130) {
                  var2.set(0, -1, var3, -1);
                  return var2;
               } else {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               }
            } else {
               var2.set(-1, 0, -1, var4);
               return var2;
            }
         } else {
            var2.set(0, var4, var3, var4);
            return var2;
         }
      } else {
         var2.set(var3, 0, var3, var4);
         return var2;
      }
   }

   private boolean isVisibleToUser(Rect var1) {
      boolean var2 = false;
      if (var1 != null) {
         if (var1.isEmpty()) {
            return false;
         } else if (this.mHost.getWindowVisibility() != 0) {
            return false;
         } else {
            ViewParent var3;
            View var4;
            for(var3 = this.mHost.getParent(); var3 instanceof View; var3 = var4.getParent()) {
               var4 = (View)var3;
               if (var4.getAlpha() <= 0.0F) {
                  return false;
               }

               if (var4.getVisibility() != 0) {
                  return false;
               }
            }

            if (var3 != null) {
               var2 = true;
            }

            return var2;
         }
      } else {
         return false;
      }
   }

   private static int keyToDirection(int var0) {
      if (var0 != 19) {
         if (var0 != 21) {
            return var0 != 22 ? 130 : 66;
         } else {
            return 17;
         }
      } else {
         return 33;
      }
   }

   private boolean moveFocus(int var1, Rect var2) {
      SparseArrayCompat var6 = this.getAllNodes();
      int var3 = this.mKeyboardFocusedVirtualViewId;
      AccessibilityNodeInfoCompat var5;
      if (var3 == Integer.MIN_VALUE) {
         var5 = null;
      } else {
         var5 = (AccessibilityNodeInfoCompat)var6.get(var3);
      }

      AccessibilityNodeInfoCompat var8;
      if (var1 != 1 && var1 != 2) {
         if (var1 != 17 && var1 != 33 && var1 != 66 && var1 != 130) {
            throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
         }

         Rect var7 = new Rect();
         var3 = this.mKeyboardFocusedVirtualViewId;
         if (var3 != Integer.MIN_VALUE) {
            this.getBoundsInParent(var3, var7);
         } else if (var2 != null) {
            var7.set(var2);
         } else {
            guessPreviouslyFocusedRect(this.mHost, var1, var7);
         }

         var8 = (AccessibilityNodeInfoCompat)FocusStrategy.findNextFocusInAbsoluteDirection(var6, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, var5, var7, var1);
      } else {
         boolean var4;
         if (ViewCompat.getLayoutDirection(this.mHost) == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         var8 = (AccessibilityNodeInfoCompat)FocusStrategy.findNextFocusInRelativeDirection(var6, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, var5, var1, var4, false);
      }

      if (var8 == null) {
         var1 = Integer.MIN_VALUE;
      } else {
         var1 = var6.keyAt(var6.indexOfValue(var8));
      }

      return this.requestKeyboardFocusForVirtualView(var1);
   }

   private boolean performActionForChild(int var1, int var2, Bundle var3) {
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 64) {
               return var2 != 128 ? this.onPerformActionForVirtualView(var1, var2, var3) : this.clearAccessibilityFocus(var1);
            } else {
               return this.requestAccessibilityFocus(var1);
            }
         } else {
            return this.clearKeyboardFocusForVirtualView(var1);
         }
      } else {
         return this.requestKeyboardFocusForVirtualView(var1);
      }
   }

   private boolean performActionForHost(int var1, Bundle var2) {
      return ViewCompat.performAccessibilityAction(this.mHost, var1, var2);
   }

   private boolean requestAccessibilityFocus(int var1) {
      if (this.mManager.isEnabled()) {
         if (!this.mManager.isTouchExplorationEnabled()) {
            return false;
         } else {
            int var2 = this.mAccessibilityFocusedVirtualViewId;
            if (var2 != var1) {
               if (var2 != Integer.MIN_VALUE) {
                  this.clearAccessibilityFocus(var2);
               }

               this.mAccessibilityFocusedVirtualViewId = var1;
               this.mHost.invalidate();
               this.sendEventForVirtualView(var1, 32768);
               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private void updateHoveredVirtualView(int var1) {
      if (this.mHoveredVirtualViewId != var1) {
         int var2 = this.mHoveredVirtualViewId;
         this.mHoveredVirtualViewId = var1;
         this.sendEventForVirtualView(var1, 128);
         this.sendEventForVirtualView(var2, 256);
      }
   }

   public final boolean clearKeyboardFocusForVirtualView(int var1) {
      if (this.mKeyboardFocusedVirtualViewId != var1) {
         return false;
      } else {
         this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
         this.onVirtualViewKeyboardFocusChanged(var1, false);
         this.sendEventForVirtualView(var1, 8);
         return true;
      }
   }

   public final boolean dispatchHoverEvent(MotionEvent var1) {
      boolean var4 = this.mManager.isEnabled();
      boolean var3 = false;
      if (var4) {
         if (!this.mManager.isTouchExplorationEnabled()) {
            return false;
         } else {
            int var2 = var1.getAction();
            if (var2 != 7 && var2 != 9) {
               if (var2 != 10) {
                  return false;
               } else if (this.mHoveredVirtualViewId != Integer.MIN_VALUE) {
                  this.updateHoveredVirtualView(Integer.MIN_VALUE);
                  return true;
               } else {
                  return false;
               }
            } else {
               var2 = this.getVirtualViewAt(var1.getX(), var1.getY());
               this.updateHoveredVirtualView(var2);
               if (var2 != Integer.MIN_VALUE) {
                  var3 = true;
               }

               return var3;
            }
         }
      } else {
         return false;
      }
   }

   public final boolean dispatchKeyEvent(KeyEvent var1) {
      boolean var7 = false;
      boolean var6 = false;
      boolean var5 = var7;
      if (var1.getAction() != 1) {
         int var2 = var1.getKeyCode();
         if (var2 != 61) {
            if (var2 != 66) {
               switch(var2) {
               case 19:
               case 20:
               case 21:
               case 22:
                  var5 = var7;
                  if (var1.hasNoModifiers()) {
                     int var3 = keyToDirection(var2);
                     int var4 = var1.getRepeatCount();
                     var2 = 0;

                     for(var5 = var6; var2 < var4 + 1 && this.moveFocus(var3, (Rect)null); ++var2) {
                        var5 = true;
                     }

                     return var5;
                  }

                  return var5;
               case 23:
                  break;
               default:
                  return false;
               }
            }

            var5 = var7;
            if (var1.hasNoModifiers()) {
               var5 = var7;
               if (var1.getRepeatCount() == 0) {
                  this.clickKeyboardFocusedVirtualView();
                  return true;
               }
            }
         } else {
            if (var1.hasNoModifiers()) {
               return this.moveFocus(2, (Rect)null);
            }

            var5 = var7;
            if (var1.hasModifiers(1)) {
               var5 = this.moveFocus(1, (Rect)null);
            }
         }
      }

      return var5;
   }

   public final int getAccessibilityFocusedVirtualViewId() {
      return this.mAccessibilityFocusedVirtualViewId;
   }

   public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
      if (this.mNodeProvider == null) {
         this.mNodeProvider = new ExploreByTouchHelper.MyNodeProvider();
      }

      return this.mNodeProvider;
   }

   @Deprecated
   public int getFocusedVirtualView() {
      return this.getAccessibilityFocusedVirtualViewId();
   }

   public final int getKeyboardFocusedVirtualViewId() {
      return this.mKeyboardFocusedVirtualViewId;
   }

   protected abstract int getVirtualViewAt(float var1, float var2);

   protected abstract void getVisibleVirtualViews(List var1);

   public final void invalidateRoot() {
      this.invalidateVirtualView(-1, 1);
   }

   public final void invalidateVirtualView(int var1) {
      this.invalidateVirtualView(var1, 0);
   }

   public final void invalidateVirtualView(int var1, int var2) {
      if (var1 != Integer.MIN_VALUE && this.mManager.isEnabled()) {
         ViewParent var3 = this.mHost.getParent();
         if (var3 != null) {
            AccessibilityEvent var4 = this.createEvent(var1, 2048);
            AccessibilityEventCompat.setContentChangeTypes(var4, var2);
            ViewParentCompat.requestSendAccessibilityEvent(var3, this.mHost, var4);
         }
      }

   }

   AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int var1) {
      return var1 == -1 ? this.createNodeForHost() : this.createNodeForChild(var1);
   }

   public final void onFocusChanged(boolean var1, int var2, Rect var3) {
      int var4 = this.mKeyboardFocusedVirtualViewId;
      if (var4 != Integer.MIN_VALUE) {
         this.clearKeyboardFocusForVirtualView(var4);
      }

      if (var1) {
         this.moveFocus(var2, var3);
      }

   }

   public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
      super.onInitializeAccessibilityEvent(var1, var2);
      this.onPopulateEventForHost(var2);
   }

   public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
      super.onInitializeAccessibilityNodeInfo(var1, var2);
      this.onPopulateNodeForHost(var2);
   }

   protected abstract boolean onPerformActionForVirtualView(int var1, int var2, Bundle var3);

   protected void onPopulateEventForHost(AccessibilityEvent var1) {
   }

   protected void onPopulateEventForVirtualView(int var1, AccessibilityEvent var2) {
   }

   protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat var1) {
   }

   protected abstract void onPopulateNodeForVirtualView(int var1, AccessibilityNodeInfoCompat var2);

   protected void onVirtualViewKeyboardFocusChanged(int var1, boolean var2) {
   }

   boolean performAction(int var1, int var2, Bundle var3) {
      return var1 != -1 ? this.performActionForChild(var1, var2, var3) : this.performActionForHost(var2, var3);
   }

   public final boolean requestKeyboardFocusForVirtualView(int var1) {
      if (!this.mHost.isFocused() && !this.mHost.requestFocus()) {
         return false;
      } else {
         int var2 = this.mKeyboardFocusedVirtualViewId;
         if (var2 == var1) {
            return false;
         } else {
            if (var2 != Integer.MIN_VALUE) {
               this.clearKeyboardFocusForVirtualView(var2);
            }

            this.mKeyboardFocusedVirtualViewId = var1;
            this.onVirtualViewKeyboardFocusChanged(var1, true);
            this.sendEventForVirtualView(var1, 8);
            return true;
         }
      }
   }

   public final boolean sendEventForVirtualView(int var1, int var2) {
      if (var1 != Integer.MIN_VALUE) {
         if (!this.mManager.isEnabled()) {
            return false;
         } else {
            ViewParent var3 = this.mHost.getParent();
            if (var3 == null) {
               return false;
            } else {
               AccessibilityEvent var4 = this.createEvent(var1, var2);
               return ViewParentCompat.requestSendAccessibilityEvent(var3, this.mHost, var4);
            }
         }
      } else {
         return false;
      }
   }

   private class MyNodeProvider extends AccessibilityNodeProviderCompat {
      MyNodeProvider() {
      }

      public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int var1) {
         return AccessibilityNodeInfoCompat.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(var1));
      }

      public AccessibilityNodeInfoCompat findFocus(int var1) {
         if (var1 == 2) {
            var1 = ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId;
         } else {
            var1 = ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
         }

         return var1 == Integer.MIN_VALUE ? null : this.createAccessibilityNodeInfo(var1);
      }

      public boolean performAction(int var1, int var2, Bundle var3) {
         return ExploreByTouchHelper.this.performAction(var1, var2, var3);
      }
   }
}
