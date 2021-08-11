/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.accessibility.AccessibilityManager
 *  android.view.accessibility.AccessibilityManager$TouchExplorationStateChangeListener
 */
package android.support.v4.view.accessibility;

import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;

@RequiresApi(value=19)
class AccessibilityManagerCompatKitKat {
    AccessibilityManagerCompatKitKat() {
    }

    public static boolean addTouchExplorationStateChangeListener(AccessibilityManager accessibilityManager, Object object) {
        return accessibilityManager.addTouchExplorationStateChangeListener((AccessibilityManager.TouchExplorationStateChangeListener)object);
    }

    public static Object newTouchExplorationStateChangeListener(final TouchExplorationStateChangeListenerBridge touchExplorationStateChangeListenerBridge) {
        return new AccessibilityManager.TouchExplorationStateChangeListener(){

            public void onTouchExplorationStateChanged(boolean bl) {
                touchExplorationStateChangeListenerBridge.onTouchExplorationStateChanged(bl);
            }
        };
    }

    public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager accessibilityManager, Object object) {
        return accessibilityManager.removeTouchExplorationStateChangeListener((AccessibilityManager.TouchExplorationStateChangeListener)object);
    }

    static interface TouchExplorationStateChangeListenerBridge {
        public void onTouchExplorationStateChanged(boolean var1);
    }

    public static class TouchExplorationStateChangeListenerWrapper
    implements AccessibilityManager.TouchExplorationStateChangeListener {
        final Object mListener;
        final TouchExplorationStateChangeListenerBridge mListenerBridge;

        public TouchExplorationStateChangeListenerWrapper(Object object, TouchExplorationStateChangeListenerBridge touchExplorationStateChangeListenerBridge) {
            this.mListener = object;
            this.mListenerBridge = touchExplorationStateChangeListenerBridge;
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean equals(Object object) {
            block6 : {
                block5 : {
                    if (this == object) break block5;
                    if (object == null || this.getClass() != object.getClass()) {
                        return false;
                    }
                    object = (TouchExplorationStateChangeListenerWrapper)object;
                    if (this.mListener != null) {
                        return this.mListener.equals(object.mListener);
                    }
                    if (object.mListener != null) break block6;
                }
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (this.mListener == null) {
                return 0;
            }
            return this.mListener.hashCode();
        }

        public void onTouchExplorationStateChanged(boolean bl) {
            this.mListenerBridge.onTouchExplorationStateChanged(bl);
        }
    }

}

