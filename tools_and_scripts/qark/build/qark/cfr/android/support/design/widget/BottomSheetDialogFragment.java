/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.os.Bundle
 */
package android.support.design.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatDialogFragment;

public class BottomSheetDialogFragment
extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return new BottomSheetDialog(this.getContext(), this.getTheme());
    }
}

