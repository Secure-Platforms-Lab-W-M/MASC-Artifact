package net.sf.fmj.media.rtp;

import java.util.Vector;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.RemoteListener;
import javax.media.rtp.SendStreamListener;
import javax.media.rtp.SessionListener;
import javax.media.rtp.event.RTPEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.RemoteEvent;
import javax.media.rtp.event.SendStreamEvent;
import javax.media.rtp.event.SessionEvent;
import net.sf.fmj.media.rtp.util.RTPMediaThread;

public class RTPEventHandler extends RTPMediaThread {
   private Vector eventQueue = new Vector();
   private boolean killed = false;
   // $FF: renamed from: sm net.sf.fmj.media.rtp.RTPSessionMgr
   private RTPSessionMgr field_25;

   public RTPEventHandler(RTPSessionMgr var1) {
      super("RTPEventHandler");
      this.field_25 = var1;
      this.useControlPriority();
      this.setDaemon(true);
      this.start();
   }

   public void close() {
      synchronized(this){}

      try {
         this.killed = true;
         this.notifyAll();
      } finally {
         ;
      }

   }

   protected void dispatchEvents() {
      synchronized(this){}

      while(true) {
         Throwable var10000;
         boolean var10001;
         label273: {
            try {
               try {
                  if (this.eventQueue.size() == 0 && !this.killed) {
                     this.wait();
                     continue;
                  }
               } catch (InterruptedException var25) {
               }
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label273;
            }

            try {
               if (this.killed) {
                  return;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label273;
            }

            RTPEvent var27;
            try {
               var27 = (RTPEvent)this.eventQueue.elementAt(0);
               this.eventQueue.removeElementAt(0);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label273;
            }

            this.processEvent(var27);
            return;
         }

         while(true) {
            Throwable var1 = var10000;

            try {
               throw var1;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void postEvent(RTPEvent var1) {
      synchronized(this){}

      try {
         this.eventQueue.addElement(var1);
         this.notifyAll();
      } finally {
         ;
      }

   }

   protected void processEvent(RTPEvent var1) {
      int var2;
      if (var1 instanceof SessionEvent) {
         for(var2 = 0; var2 < this.field_25.sessionlistener.size(); ++var2) {
            SessionListener var6 = (SessionListener)this.field_25.sessionlistener.elementAt(var2);
            if (var6 != null) {
               var6.update((SessionEvent)var1);
            }
         }

      } else if (var1 instanceof RemoteEvent) {
         for(var2 = 0; var2 < this.field_25.remotelistener.size(); ++var2) {
            RemoteListener var5 = (RemoteListener)this.field_25.remotelistener.elementAt(var2);
            if (var5 != null) {
               var5.update((RemoteEvent)var1);
            }
         }

      } else if (var1 instanceof ReceiveStreamEvent) {
         for(var2 = 0; var2 < this.field_25.streamlistener.size(); ++var2) {
            ReceiveStreamListener var4 = (ReceiveStreamListener)this.field_25.streamlistener.elementAt(var2);
            if (var4 != null) {
               var4.update((ReceiveStreamEvent)var1);
            }
         }

      } else {
         if (var1 instanceof SendStreamEvent) {
            for(var2 = 0; var2 < this.field_25.sendstreamlistener.size(); ++var2) {
               SendStreamListener var3 = (SendStreamListener)this.field_25.sendstreamlistener.elementAt(var2);
               if (var3 != null) {
                  var3.update((SendStreamEvent)var1);
               }
            }
         }

      }
   }

   public void run() {
      while(!this.killed) {
         this.dispatchEvents();
      }

   }
}
