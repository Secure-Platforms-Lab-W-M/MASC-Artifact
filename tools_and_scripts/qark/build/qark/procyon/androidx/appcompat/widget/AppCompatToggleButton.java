// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.widget.TextView;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ToggleButton;

public class AppCompatToggleButton extends ToggleButton
{
    private final AppCompatTextHelper mTextHelper;
    
    public AppCompatToggleButton(final Context context) {
        this(context, null);
    }
    
    public AppCompatToggleButton(final Context context, final AttributeSet set) {
        this(context, set, 16842827);
    }
    
    public AppCompatToggleButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        (this.mTextHelper = new AppCompatTextHelper((TextView)this)).loadFromAttributes(set, n);
    }
}
