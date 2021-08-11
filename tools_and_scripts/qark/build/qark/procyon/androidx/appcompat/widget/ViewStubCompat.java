// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.view.ViewGroup$LayoutParams;
import android.view.ViewParent;
import android.view.ViewGroup;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import androidx.appcompat.R$styleable;
import android.util.AttributeSet;
import android.content.Context;
import android.view.LayoutInflater;
import java.lang.ref.WeakReference;
import android.view.View;

public final class ViewStubCompat extends View
{
    private OnInflateListener mInflateListener;
    private int mInflatedId;
    private WeakReference<View> mInflatedViewRef;
    private LayoutInflater mInflater;
    private int mLayoutResource;
    
    public ViewStubCompat(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ViewStubCompat(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mLayoutResource = 0;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.ViewStubCompat, n, 0);
        this.mInflatedId = obtainStyledAttributes.getResourceId(R$styleable.ViewStubCompat_android_inflatedId, -1);
        this.mLayoutResource = obtainStyledAttributes.getResourceId(R$styleable.ViewStubCompat_android_layout, 0);
        this.setId(obtainStyledAttributes.getResourceId(R$styleable.ViewStubCompat_android_id, -1));
        obtainStyledAttributes.recycle();
        this.setVisibility(8);
        this.setWillNotDraw(true);
    }
    
    protected void dispatchDraw(final Canvas canvas) {
    }
    
    public void draw(final Canvas canvas) {
    }
    
    public int getInflatedId() {
        return this.mInflatedId;
    }
    
    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }
    
    public int getLayoutResource() {
        return this.mLayoutResource;
    }
    
    public View inflate() {
        final ViewParent parent = this.getParent();
        if (!(parent instanceof ViewGroup)) {
            throw new IllegalStateException("ViewStub must have a non-null ViewGroup viewParent");
        }
        if (this.mLayoutResource != 0) {
            final ViewGroup viewGroup = (ViewGroup)parent;
            LayoutInflater layoutInflater;
            if (this.mInflater != null) {
                layoutInflater = this.mInflater;
            }
            else {
                layoutInflater = LayoutInflater.from(this.getContext());
            }
            final View inflate = layoutInflater.inflate(this.mLayoutResource, viewGroup, false);
            final int mInflatedId = this.mInflatedId;
            if (mInflatedId != -1) {
                inflate.setId(mInflatedId);
            }
            final int indexOfChild = viewGroup.indexOfChild((View)this);
            viewGroup.removeViewInLayout((View)this);
            final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
            if (layoutParams != null) {
                viewGroup.addView(inflate, indexOfChild, layoutParams);
            }
            else {
                viewGroup.addView(inflate, indexOfChild);
            }
            this.mInflatedViewRef = new WeakReference<View>(inflate);
            final OnInflateListener mInflateListener = this.mInflateListener;
            if (mInflateListener != null) {
                mInflateListener.onInflate(this, inflate);
            }
            return inflate;
        }
        throw new IllegalArgumentException("ViewStub must have a valid layoutResource");
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(0, 0);
    }
    
    public void setInflatedId(final int mInflatedId) {
        this.mInflatedId = mInflatedId;
    }
    
    public void setLayoutInflater(final LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }
    
    public void setLayoutResource(final int mLayoutResource) {
        this.mLayoutResource = mLayoutResource;
    }
    
    public void setOnInflateListener(final OnInflateListener mInflateListener) {
        this.mInflateListener = mInflateListener;
    }
    
    public void setVisibility(final int n) {
        final WeakReference<View> mInflatedViewRef = this.mInflatedViewRef;
        if (mInflatedViewRef == null) {
            super.setVisibility(n);
            if (n == 0 || n == 4) {
                this.inflate();
            }
            return;
        }
        final View view = mInflatedViewRef.get();
        if (view != null) {
            view.setVisibility(n);
            return;
        }
        throw new IllegalStateException("setVisibility called on un-referenced view");
    }
    
    public interface OnInflateListener
    {
        void onInflate(final ViewStubCompat p0, final View p1);
    }
}
