package net.sf.fmj.ejmf.toolkit.util;

import org.atalk.android.util.java.awt.event.ActionEvent;
import org.atalk.android.util.java.awt.event.ActionListener;
import org.atalk.android.util.javax.swing.Timer;
import org.atalk.android.util.javax.swing.event.EventListenerList;

public class SourcedTimer implements ActionListener {
   protected static int _defaultGran = 1000;
   private Timer baseTimer;
   private SourcedTimerEvent event;
   private EventListenerList listenerList;
   private Object[] listeners;
   private TimeSource source;
   private boolean started;

   public SourcedTimer(TimeSource var1) {
      this(var1, _defaultGran);
   }

   public SourcedTimer(TimeSource var1, int var2) {
      this.listenerList = null;
      this.started = false;
      this.source = var1;
      this.event = new SourcedTimerEvent(this, 0L);
      Timer var3 = new Timer(var2, this);
      this.baseTimer = var3;
      var3.setInitialDelay(0);
   }

   public SourcedTimer(TimeSource var1, Timer var2) {
      this.listenerList = null;
      this.started = false;
      this.source = var1;
      this.event = new SourcedTimerEvent(this, 0L);
      this.baseTimer = var2;
   }

   private void runNotifyThread(long var1) {
      this.event.setTime(var1);
      this.listeners = this.listenerList.getListenerList();
      (new Thread("SourcedTimer Notify Thread") {
         public void run() {
            for(int var1 = SourcedTimer.this.listeners.length - 2; var1 >= 0; var1 -= 2) {
               if (SourcedTimer.this.listeners[var1] == SourcedTimerListener.class) {
                  ((SourcedTimerListener)SourcedTimer.this.listeners[var1 + 1]).timerUpdate(SourcedTimer.this.event);
               }
            }

         }
      }).start();
   }

   public void actionPerformed(ActionEvent var1) {
      this.runNotifyThread(this.source.getTime());
   }

   public void addSourcedTimerListener(SourcedTimerListener var1) {
      if (this.listenerList == null) {
         this.listenerList = new EventListenerList();
      }

      this.listenerList.add(SourcedTimerListener.class, var1);
   }

   public long getConversionDivisor() {
      return this.source.getConversionDivisor();
   }

   public void start() {
      if (!this.started) {
         this.baseTimer.start();
         this.runNotifyThread(0L);
      }

   }

   public void stop() {
      this.started = false;
      this.baseTimer.stop();
      this.runNotifyThread(this.source.getTime());
   }
}
