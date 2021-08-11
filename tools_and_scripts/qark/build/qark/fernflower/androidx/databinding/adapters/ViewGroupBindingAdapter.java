package androidx.databinding.adapters;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class ViewGroupBindingAdapter {
   public static void setAnimateLayoutChanges(ViewGroup var0, boolean var1) {
      if (var1) {
         var0.setLayoutTransition(new LayoutTransition());
      } else {
         var0.setLayoutTransition((LayoutTransition)null);
      }
   }

   public static void setListener(ViewGroup var0, final ViewGroupBindingAdapter.OnAnimationStart var1, final ViewGroupBindingAdapter.OnAnimationEnd var2, final ViewGroupBindingAdapter.OnAnimationRepeat var3) {
      if (var1 == null && var2 == null && var3 == null) {
         var0.setLayoutAnimationListener((AnimationListener)null);
      } else {
         var0.setLayoutAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation var1x) {
               ViewGroupBindingAdapter.OnAnimationEnd var2x = var2;
               if (var2x != null) {
                  var2x.onAnimationEnd(var1x);
               }

            }

            public void onAnimationRepeat(Animation var1x) {
               ViewGroupBindingAdapter.OnAnimationRepeat var2x = var3;
               if (var2x != null) {
                  var2x.onAnimationRepeat(var1x);
               }

            }

            public void onAnimationStart(Animation var1x) {
               ViewGroupBindingAdapter.OnAnimationStart var2x = var1;
               if (var2x != null) {
                  var2x.onAnimationStart(var1x);
               }

            }
         });
      }
   }

   public static void setListener(ViewGroup var0, final ViewGroupBindingAdapter.OnChildViewAdded var1, final ViewGroupBindingAdapter.OnChildViewRemoved var2) {
      if (var1 == null && var2 == null) {
         var0.setOnHierarchyChangeListener((OnHierarchyChangeListener)null);
      } else {
         var0.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            public void onChildViewAdded(View var1x, View var2x) {
               ViewGroupBindingAdapter.OnChildViewAdded var3 = var1;
               if (var3 != null) {
                  var3.onChildViewAdded(var1x, var2x);
               }

            }

            public void onChildViewRemoved(View var1x, View var2x) {
               ViewGroupBindingAdapter.OnChildViewRemoved var3 = var2;
               if (var3 != null) {
                  var3.onChildViewRemoved(var1x, var2x);
               }

            }
         });
      }
   }

   public interface OnAnimationEnd {
      void onAnimationEnd(Animation var1);
   }

   public interface OnAnimationRepeat {
      void onAnimationRepeat(Animation var1);
   }

   public interface OnAnimationStart {
      void onAnimationStart(Animation var1);
   }

   public interface OnChildViewAdded {
      void onChildViewAdded(View var1, View var2);
   }

   public interface OnChildViewRemoved {
      void onChildViewRemoved(View var1, View var2);
   }
}
