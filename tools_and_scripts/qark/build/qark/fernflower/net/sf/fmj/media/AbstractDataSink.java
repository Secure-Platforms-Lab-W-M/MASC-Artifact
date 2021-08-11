package net.sf.fmj.media;

import java.util.ArrayList;
import java.util.List;
import javax.media.DataSink;
import javax.media.MediaLocator;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;

public abstract class AbstractDataSink implements DataSink {
   private final List listeners = new ArrayList();
   protected MediaLocator outputLocator;

   public void addDataSinkListener(DataSinkListener param1) {
      // $FF: Couldn't be decompiled
   }

   public MediaLocator getOutputLocator() {
      return this.outputLocator;
   }

   protected void notifyDataSinkListeners(DataSinkEvent param1) {
      // $FF: Couldn't be decompiled
   }

   public void removeDataSinkListener(DataSinkListener param1) {
      // $FF: Couldn't be decompiled
   }

   public void setOutputLocator(MediaLocator var1) {
      this.outputLocator = var1;
   }
}
