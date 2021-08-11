package net.sf.fmj.ejmf.toolkit.util;

import org.atalk.android.util.javax.swing.border.CompoundBorder;
import org.atalk.android.util.javax.swing.border.EmptyBorder;
import org.atalk.android.util.javax.swing.border.EtchedBorder;

public class BorderConstants {
   public static final int GAP = 10;
   public static final EmptyBorder emptyBorder = new EmptyBorder(10, 10, 10, 10);
   public static final CompoundBorder etchedBorder;

   static {
      etchedBorder = new CompoundBorder(new EtchedBorder(), emptyBorder);
   }
}
