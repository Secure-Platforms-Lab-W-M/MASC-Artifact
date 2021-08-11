package androidx.core.widget;

import android.os.Build.VERSION;
import android.view.View.OnTouchListener;
import android.widget.PopupMenu;

public final class PopupMenuCompat {
   private PopupMenuCompat() {
   }

   public static OnTouchListener getDragToOpenListener(Object var0) {
      return VERSION.SDK_INT >= 19 ? ((PopupMenu)var0).getDragToOpenListener() : null;
   }
}
