// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.widget.CompoundButton;

class CompoundButtonCompatLollipop
{
    static ColorStateList getButtonTintList(final CompoundButton compoundButton) {
        return compoundButton.getButtonTintList();
    }
    
    static PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
        return compoundButton.getButtonTintMode();
    }
    
    static void setButtonTintList(final CompoundButton compoundButton, final ColorStateList buttonTintList) {
        compoundButton.setButtonTintList(buttonTintList);
    }
    
    static void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode buttonTintMode) {
        compoundButton.setButtonTintMode(buttonTintMode);
    }
}
