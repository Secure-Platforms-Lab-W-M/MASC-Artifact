// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(14)
@RequiresApi(14)
final class ScenePort
{
    private Context mContext;
    Runnable mEnterAction;
    Runnable mExitAction;
    private View mLayout;
    private int mLayoutId;
    private ViewGroup mSceneRoot;
    
    public ScenePort(final ViewGroup mSceneRoot) {
        this.mLayoutId = -1;
        this.mSceneRoot = mSceneRoot;
    }
    
    private ScenePort(final ViewGroup mSceneRoot, final int mLayoutId, final Context mContext) {
        this.mLayoutId = -1;
        this.mContext = mContext;
        this.mSceneRoot = mSceneRoot;
        this.mLayoutId = mLayoutId;
    }
    
    public ScenePort(final ViewGroup mSceneRoot, final View mLayout) {
        this.mLayoutId = -1;
        this.mSceneRoot = mSceneRoot;
        this.mLayout = mLayout;
    }
    
    static ScenePort getCurrentScene(final View view) {
        return (ScenePort)view.getTag(R.id.transition_current_scene);
    }
    
    public static ScenePort getSceneForLayout(final ViewGroup viewGroup, final int n, final Context context) {
        return new ScenePort(viewGroup, n, context);
    }
    
    static void setCurrentScene(final View view, final ScenePort scenePort) {
        view.setTag(R.id.transition_current_scene, (Object)scenePort);
    }
    
    public void enter() {
        if (this.mLayoutId > 0 || this.mLayout != null) {
            this.getSceneRoot().removeAllViews();
            if (this.mLayoutId > 0) {
                LayoutInflater.from(this.mContext).inflate(this.mLayoutId, this.mSceneRoot);
            }
            else {
                this.mSceneRoot.addView(this.mLayout);
            }
        }
        if (this.mEnterAction != null) {
            this.mEnterAction.run();
        }
        setCurrentScene((View)this.mSceneRoot, this);
    }
    
    public void exit() {
        if (getCurrentScene((View)this.mSceneRoot) == this && this.mExitAction != null) {
            this.mExitAction.run();
        }
    }
    
    public ViewGroup getSceneRoot() {
        return this.mSceneRoot;
    }
    
    boolean isCreatedFromLayoutResource() {
        return this.mLayoutId > 0;
    }
    
    public void setEnterAction(final Runnable mEnterAction) {
        this.mEnterAction = mEnterAction;
    }
    
    public void setExitAction(final Runnable mExitAction) {
        this.mExitAction = mExitAction;
    }
}
