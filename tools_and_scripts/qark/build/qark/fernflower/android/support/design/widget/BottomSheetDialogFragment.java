package android.support.design.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

public class BottomSheetDialogFragment extends AppCompatDialogFragment {
   public Dialog onCreateDialog(Bundle var1) {
      return new BottomSheetDialog(this.getContext(), this.getTheme());
   }
}
