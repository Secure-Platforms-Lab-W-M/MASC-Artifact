// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityManager$TouchExplorationStateChangeListener;
import android.view.accessibility.AccessibilityManager;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class AccessibilityManagerCompatKitKat
{
    public static boolean addTouchExplorationStateChangeListener(final AccessibilityManager accessibilityManager, final Object o) {
        return accessibilityManager.addTouchExplorationStateChangeListener((AccessibilityManager$TouchExplorationStateChangeListener)o);
    }
    
    public static Object newTouchExplorationStateChangeListener(final TouchExplorationStateChangeListenerBridge touchExplorationStateChangeListenerBridge) {
        return new AccessibilityManager$TouchExplorationStateChangeListener() {
            public void onTouchExplorationStateChanged(final boolean b) {
                touchExplorationStateChangeListenerBridge.onTouchExplorationStateChanged(b);
            }
        };
    }
    
    public static boolean removeTouchExplorationStateChangeListener(final AccessibilityManager accessibilityManager, final Object o) {
        return accessibilityManager.removeTouchExplorationStateChangeListener((AccessibilityManager$TouchExplorationStateChangeListener)o);
    }
    
    interface TouchExplorationStateChangeListenerBridge
    {
        void onTouchExplorationStateChanged(final boolean p0);
    }
    
    public static class TouchExplorationStateChangeListenerWrapper implements AccessibilityManager$TouchExplorationStateChangeListener
    {
        final Object mListener;
        final TouchExplorationStateChangeListenerBridge mListenerBridge;
        
        public TouchExplorationStateChangeListenerWrapper(final Object mListener, final TouchExplorationStateChangeListenerBridge mListenerBridge) {
            this.mListener = mListener;
            this.mListenerBridge = mListenerBridge;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final TouchExplorationStateChangeListenerWrapper touchExplorationStateChangeListenerWrapper = (TouchExplorationStateChangeListenerWrapper)o;
                if (this.mListener != null) {
                    return this.mListener.equals(touchExplorationStateChangeListenerWrapper.mListener);
                }
                if (touchExplorationStateChangeListenerWrapper.mListener != null) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            if (this.mListener == null) {
                return 0;
            }
            return this.mListener.hashCode();
        }
        
        public void onTouchExplorationStateChanged(final boolean b) {
            this.mListenerBridge.onTouchExplorationStateChanged(b);
        }
    }
}
