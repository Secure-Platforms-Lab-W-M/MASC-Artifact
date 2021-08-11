package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceTransferHandler;
import net.sf.fmj.utility.LoggerSingleton;

public class InputStreamPushSourceStream implements PushSourceStream {
   private static final Logger logger;
   private boolean eos;
   // $FF: renamed from: is java.io.InputStream
   private final InputStream field_123;
   private final ContentDescriptor outputContentDescriptor;
   private SourceTransferHandler transferHandler;

   static {
      logger = LoggerSingleton.logger;
   }

   public InputStreamPushSourceStream(ContentDescriptor var1, InputStream var2) {
      this.outputContentDescriptor = var1;
      this.field_123 = var2;
   }

   public boolean endOfStream() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" endOfStream");
      var1.finer(var2.toString());
      return this.eos;
   }

   public ContentDescriptor getContentDescriptor() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getContentDescriptor");
      var1.finer(var2.toString());
      return this.outputContentDescriptor;
   }

   public long getContentLength() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getContentLength");
      var1.finer(var2.toString());
      return 0L;
   }

   public Object getControl(String var1) {
      Logger var3 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getControl");
      var3.finer(var2.toString());
      return null;
   }

   public Object[] getControls() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getControls");
      var1.finer(var2.toString());
      return new Object[0];
   }

   public int getMinimumTransferSize() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getMinimumTransferSize");
      var1.finer(var2.toString());
      return 0;
   }

   public SourceTransferHandler getTransferHandler() {
      return this.transferHandler;
   }

   public void notifyDataAvailable() {
      SourceTransferHandler var1 = this.transferHandler;
      if (var1 != null) {
         var1.transferData(this);
      }

   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      var2 = this.field_123.read(var1, var2, var3);
      if (var2 < 0) {
         this.eos = true;
      }

      return var2;
   }

   public void setTransferHandler(SourceTransferHandler var1) {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append(this.getClass().getSimpleName());
      var3.append(" setTransferHandler");
      var2.finer(var3.toString());
      this.transferHandler = var1;
   }
}
