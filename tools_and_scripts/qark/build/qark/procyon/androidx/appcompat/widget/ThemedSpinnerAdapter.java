// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import androidx.appcompat.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.res.Resources$Theme;
import android.widget.SpinnerAdapter;

public interface ThemedSpinnerAdapter extends SpinnerAdapter
{
    Resources$Theme getDropDownViewTheme();
    
    void setDropDownViewTheme(final Resources$Theme p0);
    
    public static final class Helper
    {
        private final Context mContext;
        private LayoutInflater mDropDownInflater;
        private final LayoutInflater mInflater;
        
        public Helper(final Context mContext) {
            this.mContext = mContext;
            this.mInflater = LayoutInflater.from(mContext);
        }
        
        public LayoutInflater getDropDownViewInflater() {
            final LayoutInflater mDropDownInflater = this.mDropDownInflater;
            if (mDropDownInflater != null) {
                return mDropDownInflater;
            }
            return this.mInflater;
        }
        
        public Resources$Theme getDropDownViewTheme() {
            final LayoutInflater mDropDownInflater = this.mDropDownInflater;
            if (mDropDownInflater == null) {
                return null;
            }
            return mDropDownInflater.getContext().getTheme();
        }
        
        public void setDropDownViewTheme(final Resources$Theme resources$Theme) {
            if (resources$Theme == null) {
                this.mDropDownInflater = null;
                return;
            }
            if (resources$Theme == this.mContext.getTheme()) {
                this.mDropDownInflater = this.mInflater;
                return;
            }
            this.mDropDownInflater = LayoutInflater.from((Context)new ContextThemeWrapper(this.mContext, resources$Theme));
        }
    }
}
