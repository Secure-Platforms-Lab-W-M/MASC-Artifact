// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

public class BottomSheetDialogFragment extends AppCompatDialogFragment
{
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        return new BottomSheetDialog(this.getContext(), this.getTheme());
    }
}
