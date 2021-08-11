// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.app;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemSelectedListener;

class NavItemSelectedListener implements AdapterView$OnItemSelectedListener
{
    private final ActionBar.OnNavigationListener mListener;
    
    public NavItemSelectedListener(final ActionBar.OnNavigationListener mListener) {
        this.mListener = mListener;
    }
    
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        final ActionBar.OnNavigationListener mListener = this.mListener;
        if (mListener != null) {
            mListener.onNavigationItemSelected(n, n2);
        }
    }
    
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
}
