/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityManager
 *  android.view.accessibility.AccessibilityRecord
 */
package android.support.v4.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.FocusStrategy;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityRecord;
import java.util.ArrayList;
import java.util.List;

public abstract class ExploreByTouchHelper
extends AccessibilityDelegateCompat {
    private static final String DEFAULT_CLASS_NAME = "android.view.View";
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat> NODE_ADAPTER = new FocusStrategy.BoundsAdapter<AccessibilityNodeInfoCompat>(){

        @Override
        public void obtainBounds(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, Rect rect) {
            accessibilityNodeInfoCompat.getBoundsInParent(rect);
        }
    };
    private static final FocusStrategy.CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> SPARSE_VALUES_ADAPTER = new FocusStrategy.CollectionAdapter<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat>(){

        @Override
        public AccessibilityNodeInfoCompat get(SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat, int n) {
            return sparseArrayCompat.valueAt(n);
        }

        @Override
        public int size(SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat) {
            return sparseArrayCompat.size();
        }
    };
    private int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
    private final View mHost;
    private int mHoveredVirtualViewId = Integer.MIN_VALUE;
    private int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
    private final AccessibilityManager mManager;
    private MyNodeProvider mNodeProvider;
    private final int[] mTempGlobalRect = new int[2];
    private final Rect mTempParentRect = new Rect();
    private final Rect mTempScreenRect = new Rect();
    private final Rect mTempVisibleRect = new Rect();

    public ExploreByTouchHelper(View view) {
        if (view != null) {
            this.mHost = view;
            this.mManager = (AccessibilityManager)view.getContext().getSystemService("accessibility");
            view.setFocusable(true);
            if (ViewCompat.getImportantForAccessibility(view) == 0) {
                ViewCompat.setImportantForAccessibility(view, 1);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("View may not be null");
    }

    private boolean clearAccessibilityFocus(int n) {
        if (this.mAccessibilityFocusedVirtualViewId == n) {
            this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
            this.mHost.invalidate();
            this.sendEventForVirtualView(n, 65536);
            return true;
        }
        return false;
    }

    private boolean clickKeyboardFocusedVirtualView() {
        int n = this.mKeyboardFocusedVirtualViewId;
        if (n != Integer.MIN_VALUE && this.onPerformActionForVirtualView(n, 16, null)) {
            return true;
        }
        return false;
    }

    private AccessibilityEvent createEvent(int n, int n2) {
        if (n != -1) {
            return this.createEventForChild(n, n2);
        }
        return this.createEventForHost(n2);
    }

    private AccessibilityEvent createEventForChild(int n, int n2) {
        AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain((int)n2);
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = this.obtainAccessibilityNodeInfo(n);
        accessibilityEvent.getText().add(accessibilityNodeInfoCompat.getText());
        accessibilityEvent.setContentDescription(accessibilityNodeInfoCompat.getContentDescription());
        accessibilityEvent.setScrollable(accessibilityNodeInfoCompat.isScrollable());
        accessibilityEvent.setPassword(accessibilityNodeInfoCompat.isPassword());
        accessibilityEvent.setEnabled(accessibilityNodeInfoCompat.isEnabled());
        accessibilityEvent.setChecked(accessibilityNodeInfoCompat.isChecked());
        this.onPopulateEventForVirtualView(n, accessibilityEvent);
        if (accessibilityEvent.getText().isEmpty() && accessibilityEvent.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        }
        accessibilityEvent.setClassName(accessibilityNodeInfoCompat.getClassName());
        AccessibilityRecordCompat.setSource((AccessibilityRecord)accessibilityEvent, this.mHost, n);
        accessibilityEvent.setPackageName((CharSequence)this.mHost.getContext().getPackageName());
        return accessibilityEvent;
    }

    private AccessibilityEvent createEventForHost(int n) {
        AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain((int)n);
        this.mHost.onInitializeAccessibilityEvent(accessibilityEvent);
        return accessibilityEvent;
    }

    @NonNull
    private AccessibilityNodeInfoCompat createNodeForChild(int n) {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain();
        accessibilityNodeInfoCompat.setEnabled(true);
        accessibilityNodeInfoCompat.setFocusable(true);
        accessibilityNodeInfoCompat.setClassName("android.view.View");
        accessibilityNodeInfoCompat.setBoundsInParent(INVALID_PARENT_BOUNDS);
        accessibilityNodeInfoCompat.setBoundsInScreen(INVALID_PARENT_BOUNDS);
        accessibilityNodeInfoCompat.setParent(this.mHost);
        this.onPopulateNodeForVirtualView(n, accessibilityNodeInfoCompat);
        if (accessibilityNodeInfoCompat.getText() == null && accessibilityNodeInfoCompat.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        }
        accessibilityNodeInfoCompat.getBoundsInParent(this.mTempParentRect);
        if (!this.mTempParentRect.equals((Object)INVALID_PARENT_BOUNDS)) {
            int n2 = accessibilityNodeInfoCompat.getActions();
            if ((n2 & 64) == 0) {
                if ((n2 & 128) == 0) {
                    accessibilityNodeInfoCompat.setPackageName(this.mHost.getContext().getPackageName());
                    accessibilityNodeInfoCompat.setSource(this.mHost, n);
                    if (this.mAccessibilityFocusedVirtualViewId == n) {
                        accessibilityNodeInfoCompat.setAccessibilityFocused(true);
                        accessibilityNodeInfoCompat.addAction(128);
                    } else {
                        accessibilityNodeInfoCompat.setAccessibilityFocused(false);
                        accessibilityNodeInfoCompat.addAction(64);
                    }
                    boolean bl = this.mKeyboardFocusedVirtualViewId == n;
                    if (bl) {
                        accessibilityNodeInfoCompat.addAction(2);
                    } else if (accessibilityNodeInfoCompat.isFocusable()) {
                        accessibilityNodeInfoCompat.addAction(1);
                    }
                    accessibilityNodeInfoCompat.setFocused(bl);
                    this.mHost.getLocationOnScreen(this.mTempGlobalRect);
                    accessibilityNodeInfoCompat.getBoundsInScreen(this.mTempScreenRect);
                    if (this.mTempScreenRect.equals((Object)INVALID_PARENT_BOUNDS)) {
                        accessibilityNodeInfoCompat.getBoundsInParent(this.mTempScreenRect);
                        if (accessibilityNodeInfoCompat.mParentVirtualDescendantId != -1) {
                            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = AccessibilityNodeInfoCompat.obtain();
                            n = accessibilityNodeInfoCompat.mParentVirtualDescendantId;
                            while (n != -1) {
                                accessibilityNodeInfoCompat2.setParent(this.mHost, -1);
                                accessibilityNodeInfoCompat2.setBoundsInParent(INVALID_PARENT_BOUNDS);
                                this.onPopulateNodeForVirtualView(n, accessibilityNodeInfoCompat2);
                                accessibilityNodeInfoCompat2.getBoundsInParent(this.mTempParentRect);
                                this.mTempScreenRect.offset(this.mTempParentRect.left, this.mTempParentRect.top);
                                n = accessibilityNodeInfoCompat2.mParentVirtualDescendantId;
                            }
                            accessibilityNodeInfoCompat2.recycle();
                        }
                        this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                    }
                    if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect)) {
                        this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                        if (this.mTempScreenRect.intersect(this.mTempVisibleRect)) {
                            accessibilityNodeInfoCompat.setBoundsInScreen(this.mTempScreenRect);
                            if (this.isVisibleToUser(this.mTempScreenRect)) {
                                accessibilityNodeInfoCompat.setVisibleToUser(true);
                                return accessibilityNodeInfoCompat;
                            }
                            return accessibilityNodeInfoCompat;
                        }
                        return accessibilityNodeInfoCompat;
                    }
                    return accessibilityNodeInfoCompat;
                }
                throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            }
            throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        }
        throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
    }

    @NonNull
    private AccessibilityNodeInfoCompat createNodeForHost() {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(this.mHost);
        ViewCompat.onInitializeAccessibilityNodeInfo(this.mHost, accessibilityNodeInfoCompat);
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        this.getVisibleVirtualViews(arrayList);
        if (accessibilityNodeInfoCompat.getChildCount() > 0 && arrayList.size() > 0) {
            throw new RuntimeException("Views cannot have both real and virtual children");
        }
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            accessibilityNodeInfoCompat.addChild(this.mHost, arrayList.get(i));
        }
        return accessibilityNodeInfoCompat;
    }

    private SparseArrayCompat<AccessibilityNodeInfoCompat> getAllNodes() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        this.getVisibleVirtualViews(arrayList);
        SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat = new SparseArrayCompat<AccessibilityNodeInfoCompat>();
        for (int i = 0; i < arrayList.size(); ++i) {
            sparseArrayCompat.put(i, this.createNodeForChild(i));
        }
        return sparseArrayCompat;
    }

    private void getBoundsInParent(int n, Rect rect) {
        this.obtainAccessibilityNodeInfo(n).getBoundsInParent(rect);
    }

    private static Rect guessPreviouslyFocusedRect(@NonNull View view, int n, @NonNull Rect rect) {
        int n2 = view.getWidth();
        int n3 = view.getHeight();
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n == 130) {
                        rect.set(0, -1, n2, -1);
                        return rect;
                    }
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
                rect.set(-1, 0, -1, n3);
                return rect;
            }
            rect.set(0, n3, n2, n3);
            return rect;
        }
        rect.set(n2, 0, n2, n3);
        return rect;
    }

    private boolean isVisibleToUser(Rect rect) {
        boolean bl = false;
        if (rect != null) {
            if (rect.isEmpty()) {
                return false;
            }
            if (this.mHost.getWindowVisibility() != 0) {
                return false;
            }
            rect = this.mHost.getParent();
            while (rect instanceof View) {
                if ((rect = (View)rect).getAlpha() > 0.0f) {
                    if (rect.getVisibility() != 0) {
                        return false;
                    }
                    rect = rect.getParent();
                    continue;
                }
                return false;
            }
            if (rect != null) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    private static int keyToDirection(int n) {
        switch (n) {
            default: {
                return 130;
            }
            case 22: {
                return 66;
            }
            case 21: {
                return 17;
            }
            case 19: 
        }
        return 33;
    }

    private boolean moveFocus(int n, @Nullable Rect object) {
        SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat = this.getAllNodes();
        int n2 = this.mKeyboardFocusedVirtualViewId;
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = n2 == Integer.MIN_VALUE ? null : sparseArrayCompat.get(n2);
        if (n != 17 && n != 33 && n != 66 && n != 130) {
            switch (n) {
                default: {
                    throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
                case 1: 
                case 2: 
            }
            boolean bl = ViewCompat.getLayoutDirection(this.mHost) == 1;
            object = FocusStrategy.findNextFocusInRelativeDirection(sparseArrayCompat, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, accessibilityNodeInfoCompat, n, bl, false);
        } else {
            Rect rect = new Rect();
            n2 = this.mKeyboardFocusedVirtualViewId;
            if (n2 != Integer.MIN_VALUE) {
                this.getBoundsInParent(n2, rect);
            } else if (object != null) {
                rect.set((Rect)object);
            } else {
                ExploreByTouchHelper.guessPreviouslyFocusedRect(this.mHost, n, rect);
            }
            object = FocusStrategy.findNextFocusInAbsoluteDirection(sparseArrayCompat, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, accessibilityNodeInfoCompat, rect, n);
        }
        n = object == null ? Integer.MIN_VALUE : sparseArrayCompat.keyAt(sparseArrayCompat.indexOfValue((AccessibilityNodeInfoCompat)object));
        return this.requestKeyboardFocusForVirtualView(n);
    }

    private boolean performActionForChild(int n, int n2, Bundle bundle) {
        if (n2 != 64) {
            if (n2 != 128) {
                switch (n2) {
                    default: {
                        return this.onPerformActionForVirtualView(n, n2, bundle);
                    }
                    case 2: {
                        return this.clearKeyboardFocusForVirtualView(n);
                    }
                    case 1: 
                }
                return this.requestKeyboardFocusForVirtualView(n);
            }
            return this.clearAccessibilityFocus(n);
        }
        return this.requestAccessibilityFocus(n);
    }

    private boolean performActionForHost(int n, Bundle bundle) {
        return ViewCompat.performAccessibilityAction(this.mHost, n, bundle);
    }

    private boolean requestAccessibilityFocus(int n) {
        if (this.mManager.isEnabled()) {
            if (!this.mManager.isTouchExplorationEnabled()) {
                return false;
            }
            int n2 = this.mAccessibilityFocusedVirtualViewId;
            if (n2 != n) {
                if (n2 != Integer.MIN_VALUE) {
                    this.clearAccessibilityFocus(n2);
                }
                this.mAccessibilityFocusedVirtualViewId = n;
                this.mHost.invalidate();
                this.sendEventForVirtualView(n, 32768);
                return true;
            }
            return false;
        }
        return false;
    }

    private void updateHoveredVirtualView(int n) {
        if (this.mHoveredVirtualViewId == n) {
            return;
        }
        int n2 = this.mHoveredVirtualViewId;
        this.mHoveredVirtualViewId = n;
        this.sendEventForVirtualView(n, 128);
        this.sendEventForVirtualView(n2, 256);
    }

    public final boolean clearKeyboardFocusForVirtualView(int n) {
        if (this.mKeyboardFocusedVirtualViewId != n) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        this.onVirtualViewKeyboardFocusChanged(n, false);
        this.sendEventForVirtualView(n, 8);
        return true;
    }

    public final boolean dispatchHoverEvent(@NonNull MotionEvent motionEvent) {
        boolean bl = this.mManager.isEnabled();
        boolean bl2 = false;
        if (bl) {
            if (!this.mManager.isTouchExplorationEnabled()) {
                return false;
            }
            int n = motionEvent.getAction();
            if (n != 7) {
                switch (n) {
                    default: {
                        return false;
                    }
                    case 10: {
                        if (this.mAccessibilityFocusedVirtualViewId != Integer.MIN_VALUE) {
                            this.updateHoveredVirtualView(Integer.MIN_VALUE);
                            return true;
                        }
                        return false;
                    }
                    case 9: 
                }
            }
            n = this.getVirtualViewAt(motionEvent.getX(), motionEvent.getY());
            this.updateHoveredVirtualView(n);
            if (n != Integer.MIN_VALUE) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    public final boolean dispatchKeyEvent(@NonNull KeyEvent keyEvent) {
        boolean bl = false;
        if (keyEvent.getAction() != 1) {
            int n = keyEvent.getKeyCode();
            if (n != 61) {
                if (n != 66) {
                    switch (n) {
                        default: {
                            return false;
                        }
                        case 19: 
                        case 20: 
                        case 21: 
                        case 22: {
                            if (keyEvent.hasNoModifiers()) {
                                int n2 = ExploreByTouchHelper.keyToDirection(n);
                                int n3 = keyEvent.getRepeatCount();
                                for (n = 0; n < n3 + 1 && this.moveFocus(n2, null); ++n) {
                                    bl = true;
                                }
                                return bl;
                            }
                            return false;
                        }
                        case 23: 
                    }
                }
                if (keyEvent.hasNoModifiers()) {
                    if (keyEvent.getRepeatCount() == 0) {
                        this.clickKeyboardFocusedVirtualView();
                        return true;
                    }
                    return false;
                }
                return false;
            }
            if (keyEvent.hasNoModifiers()) {
                return this.moveFocus(2, null);
            }
            if (keyEvent.hasModifiers(1)) {
                return this.moveFocus(1, null);
            }
            return false;
        }
        return false;
    }

    public final int getAccessibilityFocusedVirtualViewId() {
        return this.mAccessibilityFocusedVirtualViewId;
    }

    @Override
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        if (this.mNodeProvider == null) {
            this.mNodeProvider = new MyNodeProvider();
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

    protected abstract void getVisibleVirtualViews(List<Integer> var1);

    public final void invalidateRoot() {
        this.invalidateVirtualView(-1, 1);
    }

    public final void invalidateVirtualView(int n) {
        this.invalidateVirtualView(n, 0);
    }

    public final void invalidateVirtualView(int n, int n2) {
        if (n != Integer.MIN_VALUE && this.mManager.isEnabled()) {
            ViewParent viewParent = this.mHost.getParent();
            if (viewParent != null) {
                AccessibilityEvent accessibilityEvent = this.createEvent(n, 2048);
                AccessibilityEventCompat.setContentChangeTypes(accessibilityEvent, n2);
                ViewParentCompat.requestSendAccessibilityEvent(viewParent, this.mHost, accessibilityEvent);
                return;
            }
            return;
        }
    }

    @NonNull
    AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int n) {
        if (n == -1) {
            return this.createNodeForHost();
        }
        return this.createNodeForChild(n);
    }

    public final void onFocusChanged(boolean bl, int n, @Nullable Rect rect) {
        int n2 = this.mKeyboardFocusedVirtualViewId;
        if (n2 != Integer.MIN_VALUE) {
            this.clearKeyboardFocusForVirtualView(n2);
        }
        if (bl) {
            this.moveFocus(n, rect);
            return;
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(view, accessibilityEvent);
        this.onPopulateEventForHost(accessibilityEvent);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        this.onPopulateNodeForHost(accessibilityNodeInfoCompat);
    }

    protected abstract boolean onPerformActionForVirtualView(int var1, int var2, Bundle var3);

    protected void onPopulateEventForHost(AccessibilityEvent accessibilityEvent) {
    }

    protected void onPopulateEventForVirtualView(int n, AccessibilityEvent accessibilityEvent) {
    }

    protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
    }

    protected abstract void onPopulateNodeForVirtualView(int var1, AccessibilityNodeInfoCompat var2);

    protected void onVirtualViewKeyboardFocusChanged(int n, boolean bl) {
    }

    boolean performAction(int n, int n2, Bundle bundle) {
        if (n != -1) {
            return this.performActionForChild(n, n2, bundle);
        }
        return this.performActionForHost(n2, bundle);
    }

    public final boolean requestKeyboardFocusForVirtualView(int n) {
        if (!this.mHost.isFocused() && !this.mHost.requestFocus()) {
            return false;
        }
        int n2 = this.mKeyboardFocusedVirtualViewId;
        if (n2 == n) {
            return false;
        }
        if (n2 != Integer.MIN_VALUE) {
            this.clearKeyboardFocusForVirtualView(n2);
        }
        this.mKeyboardFocusedVirtualViewId = n;
        this.onVirtualViewKeyboardFocusChanged(n, true);
        this.sendEventForVirtualView(n, 8);
        return true;
    }

    public final boolean sendEventForVirtualView(int n, int n2) {
        if (n != Integer.MIN_VALUE) {
            if (!this.mManager.isEnabled()) {
                return false;
            }
            ViewParent viewParent = this.mHost.getParent();
            if (viewParent == null) {
                return false;
            }
            AccessibilityEvent accessibilityEvent = this.createEvent(n, n2);
            return ViewParentCompat.requestSendAccessibilityEvent(viewParent, this.mHost, accessibilityEvent);
        }
        return false;
    }

    private class MyNodeProvider
    extends AccessibilityNodeProviderCompat {
        MyNodeProvider() {
        }

        @Override
        public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int n) {
            return AccessibilityNodeInfoCompat.obtain(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(n));
        }

        @Override
        public AccessibilityNodeInfoCompat findFocus(int n) {
            n = n == 2 ? ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId : ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
            if (n == Integer.MIN_VALUE) {
                return null;
            }
            return this.createAccessibilityNodeInfo(n);
        }

        @Override
        public boolean performAction(int n, int n2, Bundle bundle) {
            return ExploreByTouchHelper.this.performAction(n, n2, bundle);
        }
    }

}

