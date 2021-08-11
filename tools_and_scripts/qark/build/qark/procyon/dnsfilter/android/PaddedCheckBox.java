// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.android;

import android.os.Build$VERSION;
import android.annotation.SuppressLint;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.CheckBox;

public class PaddedCheckBox extends CheckBox
{
    private static int dpAsPx_10;
    private static int dpAsPx_32;
    
    static {
        PaddedCheckBox.dpAsPx_32 = 0;
        PaddedCheckBox.dpAsPx_10 = 0;
    }
    
    public PaddedCheckBox(final Context context) {
        super(context);
        this.doPadding();
    }
    
    public PaddedCheckBox(final Context context, final AttributeSet set) {
        super(context, set);
        this.doPadding();
    }
    
    public PaddedCheckBox(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.doPadding();
    }
    
    @SuppressLint({ "NewApi" })
    public PaddedCheckBox(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.doPadding();
    }
    
    private int convertDpToPx(final int n) {
        return (int)(n * this.getResources().getDisplayMetrics().density + 0.5f);
    }
    
    private void doPadding() {
        if (PaddedCheckBox.dpAsPx_32 == 0) {
            PaddedCheckBox.dpAsPx_32 = this.convertDpToPx(32);
            PaddedCheckBox.dpAsPx_10 = this.convertDpToPx(10);
        }
        if (Build$VERSION.SDK_INT >= 17) {
            this.setPadding(PaddedCheckBox.dpAsPx_10, PaddedCheckBox.dpAsPx_10, PaddedCheckBox.dpAsPx_10, PaddedCheckBox.dpAsPx_10);
            return;
        }
        this.setPadding(PaddedCheckBox.dpAsPx_32, 0, 0, 0);
    }
}
