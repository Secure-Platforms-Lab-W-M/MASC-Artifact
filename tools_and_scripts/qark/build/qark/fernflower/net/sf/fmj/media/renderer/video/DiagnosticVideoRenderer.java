package net.sf.fmj.media.renderer.video;

import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Control;
import javax.media.Format;
import javax.media.Renderer;
import javax.media.ResourceUnavailableException;
import javax.media.format.RGBFormat;
import javax.media.format.YUVFormat;
import net.sf.fmj.utility.LoggerSingleton;

public class DiagnosticVideoRenderer implements Renderer {
   private static final Logger logger;
   String name = "Disgnostic Video Renderer";
   int noFrames = 0;
   boolean started = false;
   Format[] supportedFormats = new Format[]{new RGBFormat(), new YUVFormat()};

   static {
      logger = LoggerSingleton.logger;
   }

   public void close() {
      synchronized(this){}
   }

   public Object getControl(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Object[] getControls() {
      return new Control[0];
   }

   public String getName() {
      return this.name;
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedFormats;
   }

   public void open() throws ResourceUnavailableException {
   }

   public int process(Buffer var1) {
      if (this.noFrames % 10 == 0) {
         Logger var3 = logger;
         StringBuilder var2 = new StringBuilder();
         var2.append("Received frame ");
         var2.append(this.noFrames);
         var3.fine(var2.toString());
      }

      ++this.noFrames;
      return 0;
   }

   public void reset() {
   }

   public Format setInputFormat(Format var1) {
      return var1;
   }

   public void start() {
      this.started = true;
   }

   public void stop() {
      this.started = false;
   }
}
