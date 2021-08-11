package net.sf.fmj.utility;

import javax.media.Format;

public class PlugInInfo {
   public final String className;
   // $FF: renamed from: in javax.media.Format[]
   public final Format[] field_147;
   public final Format[] out;
   public final int type;

   public PlugInInfo(String var1, Format[] var2, Format[] var3, int var4) {
      this.className = var1;
      this.type = var4;
      this.field_147 = var2;
      this.out = var3;
   }
}
