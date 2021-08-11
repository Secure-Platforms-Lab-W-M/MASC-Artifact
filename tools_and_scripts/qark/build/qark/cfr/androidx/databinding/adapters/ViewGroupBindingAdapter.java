/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.LayoutTransition
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$OnHierarchyChangeListener
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 */
package androidx.databinding.adapters;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

public class ViewGroupBindingAdapter {
    public static void setAnimateLayoutChanges(ViewGroup viewGroup, boolean bl) {
        if (bl) {
            viewGroup.setLayoutTransition(new LayoutTransition());
            return;
        }
        viewGroup.setLayoutTransition(null);
    }

    public static void setListener(ViewGroup viewGroup, final OnAnimationStart onAnimationStart, final OnAnimationEnd onAnimationEnd, final OnAnimationRepeat onAnimationRepeat) {
        if (onAnimationStart == null && onAnimationEnd == null && onAnimationRepeat == null) {
            viewGroup.setLayoutAnimationListener(null);
            return;
        }
        viewGroup.setLayoutAnimationListener(new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                OnAnimationEnd onAnimationEnd2 = onAnimationEnd;
                if (onAnimationEnd2 != null) {
                    onAnimationEnd2.onAnimationEnd(animation);
                }
            }

            public void onAnimationRepeat(Animation animation) {
                OnAnimationRepeat onAnimationRepeat2 = onAnimationRepeat;
                if (onAnimationRepeat2 != null) {
                    onAnimationRepeat2.onAnimationRepeat(animation);
                }
            }

            public void onAnimationStart(Animation animation) {
                OnAnimationStart onAnimationStart2 = onAnimationStart;
                if (onAnimationStart2 != null) {
                    onAnimationStart2.onAnimationStart(animation);
                }
            }
        });
    }

    public static void setListener(ViewGroup viewGroup, final OnChildViewAdded onChildViewAdded, final OnChildViewRemoved onChildViewRemoved) {
        if (onChildViewAdded == null && onChildViewRemoved == null) {
            viewGroup.setOnHierarchyChangeListener(null);
            return;
        }
        viewGroup.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener(){

            public void onChildViewAdded(View view, View view2) {
                OnChildViewAdded onChildViewAdded2 = onChildViewAdded;
                if (onChildViewAdded2 != null) {
                    onChildViewAdded2.onChildViewAdded(view, view2);
                }
            }

            public void onChildViewRemoved(View view, View view2) {
                OnChildViewRemoved onChildViewRemoved2 = onChildViewRemoved;
                if (onChildViewRemoved2 != null) {
                    onChildViewRemoved2.onChildViewRemoved(view, view2);
                }
            }
        });
    }

    public static interface OnAnimationEnd {
        public void onAnimationEnd(Animation var1);
    }

    public static interface OnAnimationRepeat {
        public void onAnimationRepeat(Animation var1);
    }

    public static interface OnAnimationStart {
        public void onAnimationStart(Animation var1);
    }

    public static interface OnChildViewAdded {
        public void onChildViewAdded(View var1, View var2);
    }

    public static interface OnChildViewRemoved {
        public void onChildViewRemoved(View var1, View var2);
    }

}

