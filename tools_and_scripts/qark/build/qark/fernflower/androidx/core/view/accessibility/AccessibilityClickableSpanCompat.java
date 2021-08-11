package androidx.core.view.accessibility;

import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.View;

public final class AccessibilityClickableSpanCompat extends ClickableSpan {
   public static final String SPAN_ID = "ACCESSIBILITY_CLICKABLE_SPAN_ID";
   private final int mClickableSpanActionId;
   private final AccessibilityNodeInfoCompat mNodeInfoCompat;
   private final int mOriginalClickableSpanId;

   public AccessibilityClickableSpanCompat(int var1, AccessibilityNodeInfoCompat var2, int var3) {
      this.mOriginalClickableSpanId = var1;
      this.mNodeInfoCompat = var2;
      this.mClickableSpanActionId = var3;
   }

   public void onClick(View var1) {
      Bundle var2 = new Bundle();
      var2.putInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", this.mOriginalClickableSpanId);
      this.mNodeInfoCompat.performAction(this.mClickableSpanActionId, var2);
   }
}
