package net.sf.fmj.media;

import java.io.PrintStream;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import net.sf.fmj.utility.LoggingStringUtils;

public abstract class AbstractCodec extends AbstractPlugIn implements Codec {
   protected Format inputFormat = null;
   protected Format[] inputFormats = new Format[0];
   protected boolean opened = false;
   protected Format outputFormat = null;

   protected boolean checkInputBuffer(Buffer var1) {
      return true;
   }

   protected final void dump(String var1, Buffer var2) {
      PrintStream var3 = System.out;
      StringBuilder var4 = new StringBuilder();
      var4.append(var1);
      var4.append(": ");
      var4.append(LoggingStringUtils.bufferToStr(var2));
      var3.println(var4.toString());
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

   public abstract Format[] getSupportedOutputFormats(Format var1);

   protected boolean isEOM(Buffer var1) {
      return var1.isEOM();
   }

   public abstract int process(Buffer var1, Buffer var2);

   protected void propagateEOM(Buffer var1) {
      var1.setEOM(true);
   }

   public Format setInputFormat(Format var1) {
      this.inputFormat = var1;
      return var1;
   }

   public Format setOutputFormat(Format var1) {
      this.outputFormat = var1;
      return var1;
   }
}
