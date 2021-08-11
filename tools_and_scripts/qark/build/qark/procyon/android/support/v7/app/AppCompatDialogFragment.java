// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import android.support.annotation.RestrictTo;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AppCompatDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        return new AppCompatDialog(this.getContext(), this.getTheme());
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public void setupDialog(final Dialog dialog, final int n) {
        if (dialog instanceof AppCompatDialog) {
            final AppCompatDialog appCompatDialog = (AppCompatDialog)dialog;
            switch (n) {
                case 3: {
                    dialog.getWindow().addFlags(24);
                }
                case 1:
                case 2: {
                    appCompatDialog.supportRequestWindowFeature(1);
                    break;
                }
            }
            return;
        }
        super.setupDialog(dialog, n);
    }
}
