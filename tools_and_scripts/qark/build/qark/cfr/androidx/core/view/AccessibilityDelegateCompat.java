/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.text.style.ClickableSpan
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.View$AccessibilityDelegate
 *  android.view.ViewGroup
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeProvider
 *  androidx.core.R
 *  androidx.core.R$id
 */
package androidx.core.view;

import android.os.Build;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.R;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class AccessibilityDelegateCompat {
    private static final View.AccessibilityDelegate DEFAULT_DELEGATE = new View.AccessibilityDelegate();
    private final View.AccessibilityDelegate mBridge;
    private final View.AccessibilityDelegate mOriginalDelegate;

    public AccessibilityDelegateCompat() {
        this(DEFAULT_DELEGATE);
    }

    public AccessibilityDelegateCompat(View.AccessibilityDelegate accessibilityDelegate) {
        this.mOriginalDelegate = accessibilityDelegate;
        this.mBridge = new AccessibilityDelegateAdapter(this);
    }

    static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(View object) {
        if ((object = (List)object.getTag(R.id.tag_accessibility_actions)) == null) {
            return Collections.emptyList();
        }
        return object;
    }

    private boolean isSpanStillValid(ClickableSpan clickableSpan, View arrclickableSpan) {
        if (clickableSpan != null) {
            arrclickableSpan = AccessibilityNodeInfoCompat.getClickableSpans(arrclickableSpan.createAccessibilityNodeInfo().getText());
            for (int i = 0; arrclickableSpan != null && i < arrclickableSpan.length; ++i) {
                if (!clickableSpan.equals((Object)arrclickableSpan[i])) continue;
                return true;
            }
        }
        return false;
    }

    private boolean performClickableSpanAction(int n, View view) {
        Object object = (SparseArray)view.getTag(R.id.tag_accessibility_clickable_spans);
        if (object != null && (object = (WeakReference)object.get(n)) != null && this.isSpanStillValid((ClickableSpan)(object = (ClickableSpan)object.get()), view)) {
            object.onClick(view);
            return true;
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        return this.mOriginalDelegate.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        if (Build.VERSION.SDK_INT >= 16 && (view = this.mOriginalDelegate.getAccessibilityNodeProvider(view)) != null) {
            return new AccessibilityNodeProviderCompat((Object)view);
        }
        return null;
    }

    View.AccessibilityDelegate getBridge() {
        return this.mBridge;
    }

    public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        this.mOriginalDelegate.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat.unwrap());
    }

    public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        return this.mOriginalDelegate.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }

    public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
        boolean bl;
        boolean bl2 = false;
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> list = AccessibilityDelegateCompat.getActionList(view);
        int n2 = 0;
        do {
            bl = bl2;
            if (n2 >= list.size()) break;
            AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = list.get(n2);
            if (accessibilityActionCompat.getId() == n) {
                bl = accessibilityActionCompat.perform(view, bundle);
                break;
            }
            ++n2;
        } while (true);
        bl2 = bl;
        if (!bl) {
            bl2 = bl;
            if (Build.VERSION.SDK_INT >= 16) {
                bl2 = this.mOriginalDelegate.performAccessibilityAction(view, n, bundle);
            }
        }
        bl = bl2;
        if (!bl2) {
            bl = bl2;
            if (n == R.id.accessibility_action_clickable_span) {
                bl = this.performClickableSpanAction(bundle.getInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", -1), view);
            }
        }
        return bl;
    }

    public void sendAccessibilityEvent(View view, int n) {
        this.mOriginalDelegate.sendAccessibilityEvent(view, n);
    }

    public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
        this.mOriginalDelegate.sendAccessibilityEventUnchecked(view, accessibilityEvent);
    }

    static final class AccessibilityDelegateAdapter
    extends View.AccessibilityDelegate {
        final AccessibilityDelegateCompat mCompat;

        AccessibilityDelegateAdapter(AccessibilityDelegateCompat accessibilityDelegateCompat) {
            this.mCompat = accessibilityDelegateCompat;
        }

        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            return this.mCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        public AccessibilityNodeProvider getAccessibilityNodeProvider(View object) {
            if ((object = this.mCompat.getAccessibilityNodeProvider((View)object)) != null) {
                return (AccessibilityNodeProvider)object.getProvider();
            }
            return null;
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.mCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
        }

        public void onInitializeAccessibilityNodeInfo(View object, AccessibilityNodeInfo accessibilityNodeInfo) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo);
            accessibilityNodeInfoCompat.setScreenReaderFocusable(ViewCompat.isScreenReaderFocusable((View)object));
            accessibilityNodeInfoCompat.setHeading(ViewCompat.isAccessibilityHeading((View)object));
            accessibilityNodeInfoCompat.setPaneTitle(ViewCompat.getAccessibilityPaneTitle((View)object));
            this.mCompat.onInitializeAccessibilityNodeInfo((View)object, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.addSpansToExtras(accessibilityNodeInfo.getText(), (View)object);
            object = AccessibilityDelegateCompat.getActionList((View)object);
            for (int i = 0; i < object.size(); ++i) {
                accessibilityNodeInfoCompat.addAction((AccessibilityNodeInfoCompat.AccessibilityActionCompat)object.get(i));
            }
        }

        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.mCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            return this.mCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }

        public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
            return this.mCompat.performAccessibilityAction(view, n, bundle);
        }

        public void sendAccessibilityEvent(View view, int n) {
            this.mCompat.sendAccessibilityEvent(view, n);
        }

        public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
            this.mCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
        }
    }

}

