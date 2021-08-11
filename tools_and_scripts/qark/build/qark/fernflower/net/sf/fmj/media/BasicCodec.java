package net.sf.fmj.media;

import java.io.PrintStream;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import org.atalk.android.util.java.awt.Dimension;

public abstract class BasicCodec extends BasicPlugIn implements Codec {
   private static final boolean DEBUG = true;
   protected Format inputFormat;
   protected Format[] inputFormats = new Format[0];
   protected boolean opened = false;
   protected Format outputFormat;
   protected Format[] outputFormats = new Format[0];
   protected boolean pendingEOM = false;

   protected int checkEOM(Buffer var1, Buffer var2) {
      this.processAtEOM(var1, var2);
      if (var2.getLength() > 0) {
         this.pendingEOM = true;
         return 2;
      } else {
         this.propagateEOM(var2);
         return 0;
      }
   }

   protected boolean checkFormat(Format var1) {
      return true;
   }

   protected boolean checkInputBuffer(Buffer var1) {
      boolean var2;
      if (this.isEOM(var1) || var1 != null && var1.getFormat() != null && this.checkFormat(var1.getFormat())) {
         var2 = false;
      } else {
         var2 = true;
      }

      if (var2) {
         PrintStream var4 = System.out;
         StringBuilder var3 = new StringBuilder();
         var3.append(this.getClass().getName());
         var3.append(" : [error] checkInputBuffer");
         var4.println(var3.toString());
      }

      return !var2;
   }

   public void close() {
      this.opened = false;
   }

   protected int getArrayElementSize(Class var1) {
      if (var1 == Format.intArray) {
         return 4;
      } else if (var1 == Format.shortArray) {
         return 2;
      } else {
         return var1 == Format.byteArray ? 1 : 0;
      }
   }

   protected Format getInputFormat() {
      return this.inputFormat;
   }

   protected Format getOutputFormat() {
      return this.outputFormat;
   }

   public Format[] getSupportedInputFormats() {
      return this.inputFormats;
   }

   protected boolean isEOM(Buffer var1) {
      return var1.isEOM();
   }

   public void open() throws ResourceUnavailableException {
      this.opened = true;
   }

   protected int processAtEOM(Buffer var1, Buffer var2) {
      return 0;
   }

   protected void propagateEOM(Buffer var1) {
      this.updateOutput(var1, this.getOutputFormat(), 0, 0);
      var1.setEOM(true);
   }

   public void reset() {
   }

   public Format setInputFormat(Format var1) {
      this.inputFormat = var1;
      return var1;
   }

   public Format setOutputFormat(Format var1) {
      this.outputFormat = var1;
      return var1;
   }

   protected void updateOutput(Buffer var1, Format var2, int var3, int var4) {
      var1.setFormat(var2);
      var1.setLength(var3);
      var1.setOffset(var4);
   }

   protected RGBFormat updateRGBFormat(VideoFormat var1, RGBFormat var2) {
      Dimension var4 = var1.getSize();
      int var3 = var4.width * var2.getPixelStride();
      return new RGBFormat(var4, var3 * var4.height, var2.getDataType(), var1.getFrameRate(), var2.getBitsPerPixel(), var2.getRedMask(), var2.getGreenMask(), var2.getBlueMask(), var2.getPixelStride(), var3, var2.getFlipped(), var2.getEndian());
   }
}
