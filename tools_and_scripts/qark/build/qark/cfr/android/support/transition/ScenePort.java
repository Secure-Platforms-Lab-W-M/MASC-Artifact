/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.transition.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(value=14)
@RequiresApi(value=14)
final class ScenePort {
    private Context mContext;
    Runnable mEnterAction;
    Runnable mExitAction;
    private View mLayout;
    private int mLayoutId = -1;
    private ViewGroup mSceneRoot;

    public ScenePort(ViewGroup viewGroup) {
        this.mSceneRoot = viewGroup;
    }

    private ScenePort(ViewGroup viewGroup, int n, Context context) {
        this.mContext = context;
        this.mSceneRoot = viewGroup;
        this.mLayoutId = n;
    }

    public ScenePort(ViewGroup viewGroup, View view) {
        this.mSceneRoot = viewGroup;
        this.mLayout = view;
    }

    static ScenePort getCurrentScene(View view) {
        return (ScenePort)view.getTag(R.id.transition_current_scene);
    }

    public static ScenePort getSceneForLayout(ViewGroup viewGroup, int n, Context context) {
        return new ScenePort(viewGroup, n, context);
    }

    static void setCurrentScene(View view, ScenePort scenePort) {
        view.setTag(R.id.transition_current_scene, (Object)scenePort);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void enter() {
        if (this.mLayoutId > 0 || this.mLayout != null) {
            this.getSceneRoot().removeAllViews();
            if (this.mLayoutId > 0) {
                LayoutInflater.from((Context)this.mContext).inflate(this.mLayoutId, this.mSceneRoot);
            } else {
                this.mSceneRoot.addView(this.mLayout);
            }
        }
        if (this.mEnterAction != null) {
            this.mEnterAction.run();
        }
        ScenePort.setCurrentScene((View)this.mSceneRoot, this);
    }

    public void exit() {
        if (ScenePort.getCurrentScene((View)this.mSceneRoot) == this && this.mExitAction != null) {
            this.mExitAction.run();
        }
    }

    public ViewGroup getSceneRoot() {
        return this.mSceneRoot;
    }

    boolean isCreatedFromLayoutResource() {
        if (this.mLayoutId > 0) {
            return true;
        }
        return false;
    }

    public void setEnterAction(Runnable runnable) {
        this.mEnterAction = runnable;
    }

    public void setExitAction(Runnable runnable) {
        this.mExitAction = runnable;
    }
}

