/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 */
package android.support.v7.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.TooltipCompatHandler;
import android.view.View;

public class TooltipCompat {
    private static final ViewCompatImpl IMPL = Build.VERSION.SDK_INT >= 26 ? new Api26ViewCompatImpl() : new BaseViewCompatImpl();

    private TooltipCompat() {
    }

    public static void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence) {
        IMPL.setTooltipText(view, charSequence);
    }

    @TargetApi(value=26)
    private static class Api26ViewCompatImpl
    implements ViewCompatImpl {
        private Api26ViewCompatImpl() {
        }

        @Override
        public void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence) {
            view.setTooltipText(charSequence);
        }
    }

    private static class BaseViewCompatImpl
    implements ViewCompatImpl {
        private BaseViewCompatImpl() {
        }

        @Override
        public void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence) {
            TooltipCompatHandler.setTooltipText(view, charSequence);
        }
    }

    private static interface ViewCompatImpl {
        public void setTooltipText(@NonNull View var1, @Nullable CharSequence var2);
    }

}

