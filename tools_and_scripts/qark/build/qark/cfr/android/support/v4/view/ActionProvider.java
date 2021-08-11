/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 */
package android.support.v4.view;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public abstract class ActionProvider {
    private static final String TAG = "ActionProvider(support)";
    private final Context mContext;
    private SubUiVisibilityListener mSubUiVisibilityListener;
    private VisibilityListener mVisibilityListener;

    public ActionProvider(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    public boolean hasSubMenu() {
        return false;
    }

    public boolean isVisible() {
        return true;
    }

    public abstract View onCreateActionView();

    public View onCreateActionView(MenuItem menuItem) {
        return this.onCreateActionView();
    }

    public boolean onPerformDefaultAction() {
        return false;
    }

    public void onPrepareSubMenu(SubMenu subMenu) {
    }

    public boolean overridesItemVisibility() {
        return false;
    }

    public void refreshVisibility() {
        if (this.mVisibilityListener != null && this.overridesItemVisibility()) {
            this.mVisibilityListener.onActionProviderVisibilityChanged(this.isVisible());
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void reset() {
        this.mVisibilityListener = null;
        this.mSubUiVisibilityListener = null;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setSubUiVisibilityListener(SubUiVisibilityListener subUiVisibilityListener) {
        this.mSubUiVisibilityListener = subUiVisibilityListener;
    }

    public void setVisibilityListener(VisibilityListener visibilityListener) {
        if (this.mVisibilityListener != null && visibilityListener != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this ");
            stringBuilder.append(this.getClass().getSimpleName());
            stringBuilder.append(" instance while it is still in use somewhere else?");
            Log.w((String)"ActionProvider(support)", (String)stringBuilder.toString());
        }
        this.mVisibilityListener = visibilityListener;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void subUiVisibilityChanged(boolean bl) {
        SubUiVisibilityListener subUiVisibilityListener = this.mSubUiVisibilityListener;
        if (subUiVisibilityListener != null) {
            subUiVisibilityListener.onSubUiVisibilityChanged(bl);
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static interface SubUiVisibilityListener {
        public void onSubUiVisibilityChanged(boolean var1);
    }

    public static interface VisibilityListener {
        public void onActionProviderVisibilityChanged(boolean var1);
    }

}

