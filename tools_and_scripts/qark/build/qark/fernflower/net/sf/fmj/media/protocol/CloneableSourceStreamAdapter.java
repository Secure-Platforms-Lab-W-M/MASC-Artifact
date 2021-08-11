package net.sf.fmj.media.protocol;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.VideoFormat;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferStream;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.PushBufferStream;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceStream;
import javax.media.protocol.SourceTransferHandler;
import net.sf.fmj.media.util.MediaThread;

public class CloneableSourceStreamAdapter {
   SourceStream adapter = null;
   SourceStream master;
   protected int numTracks = 0;
   Vector slaves = new Vector();
   protected Format[] trackFormats;

   CloneableSourceStreamAdapter(SourceStream var1) {
      this.master = var1;
      if (var1 instanceof PullSourceStream) {
         this.adapter = new CloneableSourceStreamAdapter.PullSourceStreamAdapter();
      }

      if (var1 instanceof PullBufferStream) {
         this.adapter = new CloneableSourceStreamAdapter.PullBufferStreamAdapter();
      }

      if (var1 instanceof PushSourceStream) {
         this.adapter = new CloneableSourceStreamAdapter.PushSourceStreamAdapter();
      }

      if (var1 instanceof PushBufferStream) {
         this.adapter = new CloneableSourceStreamAdapter.PushBufferStreamAdapter();
      }

   }

   int copyAndRead(byte[] var1, int var2, int var3) throws IOException {
      int var4 = 0;
      SourceStream var5 = this.master;
      if (var5 instanceof PullSourceStream) {
         var4 = ((PullSourceStream)var5).read(var1, var2, var3);
      } else if (var5 instanceof PushSourceStream) {
         var4 = ((PushSourceStream)var5).read(var1, var2, var3);
      }

      Enumeration var8 = this.slaves.elements();

      while(var8.hasMoreElements()) {
         SourceStream var6 = (SourceStream)var8.nextElement();
         byte[] var7 = new byte[var4];
         System.arraycopy(var1, var2, var7, 0, var4);
         ((CloneableSourceStreamAdapter.PushSourceStreamSlave)var6).setBuffer(var7);
      }

      return var4;
   }

   void copyAndRead(Buffer var1) throws IOException {
      SourceStream var2 = this.master;
      if (var2 instanceof PullBufferStream) {
         ((PullBufferStream)var2).read(var1);
      } else if (var2 instanceof PushBufferStream) {
         ((PushBufferStream)var2).read(var1);
      }

      Enumeration var3 = this.slaves.elements();

      while(var3.hasMoreElements()) {
         ((CloneableSourceStreamAdapter.PushBufferStreamSlave)((SourceStream)var3.nextElement())).setBuffer((Buffer)var1.clone());
         Thread.yield();
      }

   }

   SourceStream createSlave() {
      Object var1 = null;
      SourceStream var2 = this.master;
      if (!(var2 instanceof PullSourceStream) && !(var2 instanceof PushSourceStream)) {
         if (var2 instanceof PullBufferStream || var2 instanceof PushBufferStream) {
            var1 = new CloneableSourceStreamAdapter.PushBufferStreamSlave();
         }
      } else {
         var1 = new CloneableSourceStreamAdapter.PushSourceStreamSlave();
      }

      this.slaves.addElement(var1);
      return (SourceStream)var1;
   }

   SourceStream getAdapter() {
      return this.adapter;
   }

   class PullBufferStreamAdapter extends CloneableSourceStreamAdapter.SourceStreamAdapter implements PullBufferStream {
      PullBufferStreamAdapter() {
         super();
      }

      public Format getFormat() {
         return ((PullBufferStream)CloneableSourceStreamAdapter.this.master).getFormat();
      }

      public void read(Buffer var1) throws IOException {
         CloneableSourceStreamAdapter.this.copyAndRead(var1);
      }

      public boolean willReadBlock() {
         return ((PullBufferStream)CloneableSourceStreamAdapter.this.master).willReadBlock();
      }
   }

   class PullSourceStreamAdapter extends CloneableSourceStreamAdapter.SourceStreamAdapter implements PullSourceStream {
      PullSourceStreamAdapter() {
         super();
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         return CloneableSourceStreamAdapter.this.copyAndRead(var1, var2, var3);
      }

      public boolean willReadBlock() {
         return ((PullSourceStream)CloneableSourceStreamAdapter.this.master).willReadBlock();
      }
   }

   class PushBufferStreamAdapter extends CloneableSourceStreamAdapter.SourceStreamAdapter implements PushBufferStream, BufferTransferHandler {
      BufferTransferHandler handler;

      PushBufferStreamAdapter() {
         super();
      }

      public Format getFormat() {
         return ((PushBufferStream)CloneableSourceStreamAdapter.this.master).getFormat();
      }

      public void read(Buffer var1) throws IOException {
         CloneableSourceStreamAdapter.this.copyAndRead(var1);
      }

      public void setTransferHandler(BufferTransferHandler var1) {
         this.handler = var1;
         ((PushBufferStream)CloneableSourceStreamAdapter.this.master).setTransferHandler(this);
      }

      public void transferData(PushBufferStream var1) {
         BufferTransferHandler var2 = this.handler;
         if (var2 != null) {
            var2.transferData(this);
         }

      }
   }

   class PushBufferStreamSlave extends CloneableSourceStreamAdapter.PushStreamSlave implements PushBufferStream, Runnable {
      // $FF: renamed from: b javax.media.Buffer
      private Buffer field_109;
      BufferTransferHandler handler;

      PushBufferStreamSlave() {
         super();
      }

      public Format getFormat() {
         if (CloneableSourceStreamAdapter.this.master instanceof PullBufferStream) {
            return ((PullBufferStream)CloneableSourceStreamAdapter.this.master).getFormat();
         } else {
            return CloneableSourceStreamAdapter.this.master instanceof PushBufferStream ? ((PushBufferStream)CloneableSourceStreamAdapter.this.master).getFormat() : null;
         }
      }

      BufferTransferHandler getTransferHandler() {
         return this.handler;
      }

      public void read(Buffer param1) throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }

      void setBuffer(Buffer var1) {
         synchronized(this){}

         try {
            this.field_109 = var1;
            this.notifyAll();
         } finally {
            ;
         }

      }

      public void setTransferHandler(BufferTransferHandler var1) {
         this.handler = var1;
      }
   }

   class PushSourceStreamAdapter extends CloneableSourceStreamAdapter.SourceStreamAdapter implements PushSourceStream, SourceTransferHandler {
      SourceTransferHandler handler;

      PushSourceStreamAdapter() {
         super();
      }

      public int getMinimumTransferSize() {
         return ((PushSourceStream)CloneableSourceStreamAdapter.this.master).getMinimumTransferSize();
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         return CloneableSourceStreamAdapter.this.copyAndRead(var1, var2, var3);
      }

      public void setTransferHandler(SourceTransferHandler var1) {
         this.handler = var1;
         ((PushSourceStream)CloneableSourceStreamAdapter.this.master).setTransferHandler(this);
      }

      public void transferData(PushSourceStream var1) {
         SourceTransferHandler var2 = this.handler;
         if (var2 != null) {
            var2.transferData(this);
         }

      }
   }

   class PushSourceStreamSlave extends CloneableSourceStreamAdapter.PushStreamSlave implements PushSourceStream, Runnable {
      private byte[] buffer;
      SourceTransferHandler handler;

      PushSourceStreamSlave() {
         super();
      }

      public int getMinimumTransferSize() {
         return CloneableSourceStreamAdapter.this.master instanceof PushSourceStream ? ((PushSourceStream)CloneableSourceStreamAdapter.this.master).getMinimumTransferSize() : 0;
      }

      SourceTransferHandler getTransferHandler() {
         return this.handler;
      }

      public int read(byte[] param1, int param2, int param3) throws IOException {
         // $FF: Couldn't be decompiled
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }

      void setBuffer(byte[] var1) {
         synchronized(this){}

         try {
            this.buffer = var1;
            this.notifyAll();
         } finally {
            ;
         }

      }

      public void setTransferHandler(SourceTransferHandler var1) {
         this.handler = var1;
      }
   }

   abstract class PushStreamSlave extends CloneableSourceStreamAdapter.SourceStreamAdapter implements SourceStreamSlave, Runnable {
      boolean connected = false;
      MediaThread notifyingThread;

      PushStreamSlave() {
         super();
      }

      public void connect() {
         synchronized(this){}

         Throwable var10000;
         label312: {
            boolean var1;
            boolean var10001;
            try {
               var1 = this.connected;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label312;
            }

            if (var1) {
               return;
            }

            MediaThread var2;
            try {
               this.connected = true;
               var2 = new MediaThread(this);
               this.notifyingThread = var2;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label312;
            }

            if (var2 != null) {
               label315: {
                  label295:
                  try {
                     if (this instanceof PushBufferStream) {
                        if (!(((PushBufferStream)this).getFormat() instanceof VideoFormat)) {
                           break label295;
                        }

                        this.notifyingThread.useVideoPriority();
                     }
                     break label315;
                  } catch (Throwable var30) {
                     var10000 = var30;
                     var10001 = false;
                     break label312;
                  }

                  try {
                     this.notifyingThread.useAudioPriority();
                  } catch (Throwable var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label312;
                  }
               }

               try {
                  this.notifyingThread.start();
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label312;
               }
            }

            return;
         }

         Throwable var33 = var10000;
         throw var33;
      }

      public void disconnect() {
         synchronized(this){}

         try {
            this.connected = false;
            this.notifyAll();
         } finally {
            ;
         }

      }
   }

   class SourceStreamAdapter implements SourceStream {
      public boolean endOfStream() {
         return CloneableSourceStreamAdapter.this.master.endOfStream();
      }

      public ContentDescriptor getContentDescriptor() {
         return CloneableSourceStreamAdapter.this.master.getContentDescriptor();
      }

      public long getContentLength() {
         return CloneableSourceStreamAdapter.this.master.getContentLength();
      }

      public Object getControl(String var1) {
         return CloneableSourceStreamAdapter.this.master.getControl(var1);
      }

      public Object[] getControls() {
         return CloneableSourceStreamAdapter.this.master.getControls();
      }
   }
}
