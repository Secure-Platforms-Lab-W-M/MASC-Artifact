package net.sf.fmj.media;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.media.CaptureDeviceInfo;
import javax.media.control.FormatControl;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;

public class MergingCaptureDevicePullBufferDataSource extends MergingPullBufferDataSource implements CaptureDevice {
   public MergingCaptureDevicePullBufferDataSource(List var1) {
      super(var1);
      Iterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return;
         }
      } while((DataSource)var2.next() instanceof CaptureDevice);

      throw new IllegalArgumentException();
   }

   public CaptureDeviceInfo getCaptureDeviceInfo() {
      throw new UnsupportedOperationException();
   }

   public FormatControl[] getFormatControls() {
      ArrayList var4 = new ArrayList();
      Iterator var5 = this.sources.iterator();

      while(true) {
         boolean var3 = var5.hasNext();
         int var1 = 0;
         if (!var3) {
            return (FormatControl[])var4.toArray(new FormatControl[0]);
         }

         FormatControl[] var6 = ((CaptureDevice)((DataSource)var5.next())).getFormatControls();

         for(int var2 = var6.length; var1 < var2; ++var1) {
            var4.add(var6[var1]);
         }
      }
   }
}
