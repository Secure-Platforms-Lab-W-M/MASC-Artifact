package net.sf.fmj.media.control;

import javax.media.Control;
import org.atalk.android.util.java.awt.Component;

public class ProgressControlAdapter extends AtomicControlAdapter implements ProgressControl {
   // $FF: renamed from: ac net.sf.fmj.media.control.StringControl
   StringControl field_23 = null;
   StringControl apc = null;
   StringControl brc = null;
   Control[] controls = null;
   StringControl frc = null;
   // $FF: renamed from: vc net.sf.fmj.media.control.StringControl
   StringControl field_24 = null;
   StringControl vpc = null;

   public ProgressControlAdapter(StringControl var1, StringControl var2, StringControl var3, StringControl var4, StringControl var5, StringControl var6) {
      super((Component)null, true, (Control)null);
      this.frc = var1;
      this.brc = var2;
      this.vpc = var3;
      this.apc = var4;
      this.field_24 = var5;
      this.field_23 = var6;
   }

   public StringControl getAudioCodec() {
      return this.field_23;
   }

   public StringControl getAudioProperties() {
      return this.apc;
   }

   public StringControl getBitRate() {
      return this.brc;
   }

   public Control[] getControls() {
      if (this.controls == null) {
         Control[] var1 = new Control[6];
         this.controls = var1;
         var1[0] = this.frc;
         var1[1] = this.brc;
         var1[2] = this.vpc;
         var1[3] = this.apc;
         var1[4] = this.field_23;
         var1[5] = this.field_24;
      }

      return this.controls;
   }

   public StringControl getFrameRate() {
      return this.frc;
   }

   public StringControl getVideoCodec() {
      return this.field_24;
   }

   public StringControl getVideoProperties() {
      return this.vpc;
   }
}
