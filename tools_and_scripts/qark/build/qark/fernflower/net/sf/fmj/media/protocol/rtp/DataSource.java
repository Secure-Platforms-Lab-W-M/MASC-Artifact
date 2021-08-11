package net.sf.fmj.media.protocol.rtp;

import java.io.IOException;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.protocol.PushBufferStream;
import javax.media.rtp.RTPControl;
import net.sf.fmj.media.protocol.BasicPushBufferDataSource;
import net.sf.fmj.media.protocol.BufferListener;
import net.sf.fmj.media.protocol.RTPSource;
import net.sf.fmj.media.protocol.Streamable;
import net.sf.fmj.media.rtp.RTPControlImpl;
import net.sf.fmj.media.rtp.RTPSessionMgr;
import net.sf.fmj.media.rtp.RTPSourceStream;
import net.sf.fmj.media.rtp.SSRCInfo;

public class DataSource extends BasicPushBufferDataSource implements Streamable, RTPSource {
   public static final String RTP_CONTROL_CLASS_NAME = RTPControl.class.getName();
   static int SSRC_UNDEFINED = 0;
   DataSource childsrc = null;
   RTPSessionMgr mgr = null;
   RTPControl rtpcontrol = new DataSource.MyRTPControl();
   private final RTPSourceStream[] srcStreams = new RTPSourceStream[1];
   int ssrc;
   Player streamplayer = null;

   public DataSource() {
      this.ssrc = SSRC_UNDEFINED;
      this.setContentType("rtp");
   }

   public void connect() throws IOException {
      RTPSourceStream[] var3 = this.srcStreams;
      if (var3 != null) {
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            RTPSourceStream var4 = var3[var1];
            if (var4 != null) {
               var4.connect();
            }
         }
      }

      this.connected = true;
   }

   public void disconnect() {
      RTPSourceStream[] var3 = this.srcStreams;
      if (var3 != null) {
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var3[var1].close();
         }
      }

   }

   public void flush() {
      this.srcStreams[0].reset();
   }

   public String getCNAME() {
      RTPSessionMgr var1 = this.mgr;
      if (var1 == null) {
         return null;
      } else {
         SSRCInfo var2 = var1.getSSRCInfo(this.ssrc);
         return var2 != null ? var2.getCNAME() : null;
      }
   }

   public Object getControl(Class var1) {
      Object[] var4 = this.getControls();
      int var3 = var4.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Object var5 = var4[var2];
         if (var1.isInstance(var5)) {
            return var5;
         }
      }

      return null;
   }

   public Object getControl(String var1) {
      Class var3;
      if (RTP_CONTROL_CLASS_NAME.equals(var1)) {
         var3 = RTPControl.class;
      } else {
         try {
            var3 = Class.forName(var1);
         } catch (ClassNotFoundException var2) {
            return null;
         }
      }

      return this.getControl(var3);
   }

   public Object[] getControls() {
      return new RTPControl[]{this.rtpcontrol};
   }

   public RTPSessionMgr getMgr() {
      return this.mgr;
   }

   public Player getPlayer() {
      return this.streamplayer;
   }

   public int getSSRC() {
      return this.ssrc;
   }

   public PushBufferStream[] getStreams() {
      return !this.connected ? null : this.srcStreams;
   }

   public boolean isPrefetchable() {
      return false;
   }

   public boolean isStarted() {
      return this.started;
   }

   public void prebuffer() {
      this.started = true;
      this.srcStreams[0].prebuffer();
   }

   public void setBufferListener(BufferListener var1) {
      this.srcStreams[0].setBufferListener(var1);
   }

   public void setBufferWhenStopped(boolean var1) {
      this.srcStreams[0].setBufferWhenStopped(var1);
   }

   public void setChild(DataSource var1) {
      this.childsrc = var1;
   }

   public void setContentType(String var1) {
      this.contentType = var1;
   }

   public void setControl(Object var1) {
      this.rtpcontrol = (RTPControl)var1;
   }

   public void setLocator(MediaLocator var1) {
      super.setLocator(var1);
   }

   public void setMgr(RTPSessionMgr var1) {
      this.mgr = var1;
   }

   public void setPlayer(Player var1) {
      this.streamplayer = var1;
   }

   public void setSSRC(int var1) {
      this.ssrc = var1;
   }

   public void setSourceStream(RTPSourceStream var1) {
      RTPSourceStream[] var2 = this.srcStreams;
      if (var2 != null) {
         var2[0] = var1;
      }

   }

   public void start() throws IOException {
      super.start();
      DataSource var3 = this.childsrc;
      if (var3 != null) {
         var3.start();
      }

      RTPSourceStream[] var4 = this.srcStreams;
      if (var4 != null) {
         int var2 = var4.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var4[var1].start();
         }
      }

   }

   public void stop() throws IOException {
      super.stop();
      DataSource var3 = this.childsrc;
      if (var3 != null) {
         var3.stop();
      }

      RTPSourceStream[] var4 = this.srcStreams;
      if (var4 != null) {
         int var2 = var4.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var4[var1].stop();
         }
      }

   }

   class MyRTPControl extends RTPControlImpl {
      public String getCNAME() {
         if (DataSource.this.mgr == null) {
            return null;
         } else {
            SSRCInfo var1 = DataSource.this.mgr.getSSRCInfo(DataSource.this.ssrc);
            return var1 != null ? var1.getCNAME() : null;
         }
      }

      public int getSSRC() {
         return DataSource.this.ssrc;
      }
   }
}
