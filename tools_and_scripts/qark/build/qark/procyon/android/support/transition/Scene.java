// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.util.SparseArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;

public class Scene
{
    private Context mContext;
    private Runnable mEnterAction;
    private Runnable mExitAction;
    private View mLayout;
    private int mLayoutId;
    private ViewGroup mSceneRoot;
    
    public Scene(@NonNull final ViewGroup mSceneRoot) {
        this.mLayoutId = -1;
        this.mSceneRoot = mSceneRoot;
    }
    
    private Scene(final ViewGroup mSceneRoot, final int mLayoutId, final Context mContext) {
        this.mLayoutId = -1;
        this.mContext = mContext;
        this.mSceneRoot = mSceneRoot;
        this.mLayoutId = mLayoutId;
    }
    
    public Scene(@NonNull final ViewGroup mSceneRoot, @NonNull final View mLayout) {
        this.mLayoutId = -1;
        this.mSceneRoot = mSceneRoot;
        this.mLayout = mLayout;
    }
    
    static Scene getCurrentScene(final View view) {
        return (Scene)view.getTag(R.id.transition_current_scene);
    }
    
    @NonNull
    public static Scene getSceneForLayout(@NonNull final ViewGroup viewGroup, @LayoutRes final int n, @NonNull final Context context) {
        SparseArray sparseArray = (SparseArray)viewGroup.getTag(R.id.transition_scene_layoutid_cache);
        if (sparseArray == null) {
            sparseArray = new SparseArray();
            viewGroup.setTag(R.id.transition_scene_layoutid_cache, (Object)sparseArray);
        }
        final Scene scene = (Scene)sparseArray.get(n);
        if (scene != null) {
            return scene;
        }
        final Scene scene2 = new Scene(viewGroup, n, context);
        sparseArray.put(n, (Object)scene2);
        return scene2;
    }
    
    static void setCurrentScene(final View view, final Scene scene) {
        view.setTag(R.id.transition_current_scene, (Object)scene);
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
        final Runnable mEnterAction = this.mEnterAction;
        if (mEnterAction != null) {
            mEnterAction.run();
        }
        setCurrentScene((View)this.mSceneRoot, this);
    }
    
    public void exit() {
        if (getCurrentScene((View)this.mSceneRoot) != this) {
            return;
        }
        final Runnable mExitAction = this.mExitAction;
        if (mExitAction != null) {
            mExitAction.run();
        }
    }
    
    @NonNull
    public ViewGroup getSceneRoot() {
        return this.mSceneRoot;
    }
    
    boolean isCreatedFromLayoutResource() {
        return this.mLayoutId > 0;
    }
    
    public void setEnterAction(@Nullable final Runnable mEnterAction) {
        this.mEnterAction = mEnterAction;
    }
    
    public void setExitAction(@Nullable final Runnable mExitAction) {
        this.mExitAction = mExitAction;
    }
}
