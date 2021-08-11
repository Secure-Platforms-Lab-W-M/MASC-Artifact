// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.graphics.Rect;
import android.view.WindowInsets;

class WindowInsetsCompatApi21 extends WindowInsetsCompat
{
    private final WindowInsets mSource;
    
    WindowInsetsCompatApi21(final WindowInsets mSource) {
        this.mSource = mSource;
    }
    
    @Override
    public WindowInsetsCompat consumeStableInsets() {
        return new WindowInsetsCompatApi21(this.mSource.consumeStableInsets());
    }
    
    @Override
    public WindowInsetsCompat consumeSystemWindowInsets() {
        return new WindowInsetsCompatApi21(this.mSource.consumeSystemWindowInsets());
    }
    
    @Override
    public int getStableInsetBottom() {
        return this.mSource.getStableInsetBottom();
    }
    
    @Override
    public int getStableInsetLeft() {
        return this.mSource.getStableInsetLeft();
    }
    
    @Override
    public int getStableInsetRight() {
        return this.mSource.getStableInsetRight();
    }
    
    @Override
    public int getStableInsetTop() {
        return this.mSource.getStableInsetTop();
    }
    
    @Override
    public int getSystemWindowInsetBottom() {
        return this.mSource.getSystemWindowInsetBottom();
    }
    
    @Override
    public int getSystemWindowInsetLeft() {
        return this.mSource.getSystemWindowInsetLeft();
    }
    
    @Override
    public int getSystemWindowInsetRight() {
        return this.mSource.getSystemWindowInsetRight();
    }
    
    @Override
    public int getSystemWindowInsetTop() {
        return this.mSource.getSystemWindowInsetTop();
    }
    
    @Override
    public boolean hasInsets() {
        return this.mSource.hasInsets();
    }
    
    @Override
    public boolean hasStableInsets() {
        return this.mSource.hasStableInsets();
    }
    
    @Override
    public boolean hasSystemWindowInsets() {
        return this.mSource.hasSystemWindowInsets();
    }
    
    @Override
    public boolean isConsumed() {
        return this.mSource.isConsumed();
    }
    
    @Override
    public boolean isRound() {
        return this.mSource.isRound();
    }
    
    @Override
    public WindowInsetsCompat replaceSystemWindowInsets(final int n, final int n2, final int n3, final int n4) {
        return new WindowInsetsCompatApi21(this.mSource.replaceSystemWindowInsets(n, n2, n3, n4));
    }
    
    @Override
    public WindowInsetsCompat replaceSystemWindowInsets(final Rect rect) {
        return new WindowInsetsCompatApi21(this.mSource.replaceSystemWindowInsets(rect));
    }
    
    WindowInsets unwrap() {
        return this.mSource;
    }
}
