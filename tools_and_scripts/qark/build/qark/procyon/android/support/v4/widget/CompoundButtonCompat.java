// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.util.Log;
import java.lang.reflect.Field;
import android.support.annotation.RequiresApi;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;
import android.os.Build$VERSION;

public final class CompoundButtonCompat
{
    private static final CompoundButtonCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (CompoundButtonCompatBaseImpl)new CompoundButtonCompatApi23Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (CompoundButtonCompatBaseImpl)new CompoundButtonCompatApi21Impl();
            return;
        }
        IMPL = new CompoundButtonCompatBaseImpl();
    }
    
    private CompoundButtonCompat() {
    }
    
    @Nullable
    public static Drawable getButtonDrawable(@NonNull final CompoundButton compoundButton) {
        return CompoundButtonCompat.IMPL.getButtonDrawable(compoundButton);
    }
    
    @Nullable
    public static ColorStateList getButtonTintList(@NonNull final CompoundButton compoundButton) {
        return CompoundButtonCompat.IMPL.getButtonTintList(compoundButton);
    }
    
    @Nullable
    public static PorterDuff$Mode getButtonTintMode(@NonNull final CompoundButton compoundButton) {
        return CompoundButtonCompat.IMPL.getButtonTintMode(compoundButton);
    }
    
    public static void setButtonTintList(@NonNull final CompoundButton compoundButton, @Nullable final ColorStateList list) {
        CompoundButtonCompat.IMPL.setButtonTintList(compoundButton, list);
    }
    
    public static void setButtonTintMode(@NonNull final CompoundButton compoundButton, @Nullable final PorterDuff$Mode porterDuff$Mode) {
        CompoundButtonCompat.IMPL.setButtonTintMode(compoundButton, porterDuff$Mode);
    }
    
    @RequiresApi(21)
    static class CompoundButtonCompatApi21Impl extends CompoundButtonCompatBaseImpl
    {
        @Override
        public ColorStateList getButtonTintList(final CompoundButton compoundButton) {
            return compoundButton.getButtonTintList();
        }
        
        @Override
        public PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
            return compoundButton.getButtonTintMode();
        }
        
        @Override
        public void setButtonTintList(final CompoundButton compoundButton, final ColorStateList buttonTintList) {
            compoundButton.setButtonTintList(buttonTintList);
        }
        
        @Override
        public void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode buttonTintMode) {
            compoundButton.setButtonTintMode(buttonTintMode);
        }
    }
    
    @RequiresApi(23)
    static class CompoundButtonCompatApi23Impl extends CompoundButtonCompatApi21Impl
    {
        @Override
        public Drawable getButtonDrawable(final CompoundButton compoundButton) {
            return compoundButton.getButtonDrawable();
        }
    }
    
    static class CompoundButtonCompatBaseImpl
    {
        private static final String TAG = "CompoundButtonCompat";
        private static Field sButtonDrawableField;
        private static boolean sButtonDrawableFieldFetched;
        
        public Drawable getButtonDrawable(final CompoundButton compoundButton) {
            if (!CompoundButtonCompatBaseImpl.sButtonDrawableFieldFetched) {
                try {
                    (CompoundButtonCompatBaseImpl.sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable")).setAccessible(true);
                }
                catch (NoSuchFieldException ex) {
                    Log.i("CompoundButtonCompat", "Failed to retrieve mButtonDrawable field", (Throwable)ex);
                }
                CompoundButtonCompatBaseImpl.sButtonDrawableFieldFetched = true;
            }
            final Field sButtonDrawableField = CompoundButtonCompatBaseImpl.sButtonDrawableField;
            if (sButtonDrawableField != null) {
                try {
                    return (Drawable)sButtonDrawableField.get(compoundButton);
                }
                catch (IllegalAccessException ex2) {
                    Log.i("CompoundButtonCompat", "Failed to get button drawable via reflection", (Throwable)ex2);
                    CompoundButtonCompatBaseImpl.sButtonDrawableField = null;
                    return null;
                }
            }
            return null;
        }
        
        public ColorStateList getButtonTintList(final CompoundButton compoundButton) {
            if (compoundButton instanceof TintableCompoundButton) {
                return ((TintableCompoundButton)compoundButton).getSupportButtonTintList();
            }
            return null;
        }
        
        public PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
            if (compoundButton instanceof TintableCompoundButton) {
                return ((TintableCompoundButton)compoundButton).getSupportButtonTintMode();
            }
            return null;
        }
        
        public void setButtonTintList(final CompoundButton compoundButton, final ColorStateList supportButtonTintList) {
            if (compoundButton instanceof TintableCompoundButton) {
                ((TintableCompoundButton)compoundButton).setSupportButtonTintList(supportButtonTintList);
            }
        }
        
        public void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode supportButtonTintMode) {
            if (compoundButton instanceof TintableCompoundButton) {
                ((TintableCompoundButton)compoundButton).setSupportButtonTintMode(supportButtonTintMode);
            }
        }
    }
}
