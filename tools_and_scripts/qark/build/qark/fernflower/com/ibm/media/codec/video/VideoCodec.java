package com.ibm.media.codec.video;

import com.sun.media.BasicCodec;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.VideoFormat;
import net.sf.fmj.utility.LoggerSingleton;

@Deprecated
public abstract class VideoCodec extends BasicCodec {
   private static final boolean TRACE = false;
   private static final Logger logger;
   protected final boolean DEBUG = true;
   protected String PLUGIN_NAME;
   protected VideoFormat[] defaultOutputFormats;
   protected VideoFormat inputFormat;
   protected VideoFormat outputFormat;
   protected VideoFormat[] supportedInputFormats;
   protected VideoFormat[] supportedOutputFormats;

   static {
      logger = LoggerSingleton.logger;
   }

   public boolean checkFormat(Format var1) {
      if (!((VideoFormat)var1).getSize().equals(this.outputFormat.getSize())) {
         this.videoResized();
      }

      return true;
   }

   protected Format getInputFormat() {
      return this.inputFormat;
   }

   protected Format[] getMatchingOutputFormats(Format var1) {
      return new Format[0];
   }

   public String getName() {
      return this.PLUGIN_NAME;
   }

   protected Format getOutputFormat() {
      return this.outputFormat;
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.defaultOutputFormats;
      } else {
         ArrayList var4 = new ArrayList();
         int var2 = 0;

         while(true) {
            VideoFormat[] var5 = this.supportedInputFormats;
            if (var2 >= var5.length) {
               return (Format[])var4.toArray(new Format[var4.size()]);
            }

            if (var1.matches(var5[var2])) {
               this.inputFormat = (VideoFormat)var1;
               Format[] var6 = this.getMatchingOutputFormats(var1);

               for(int var3 = 0; var3 < var6.length; ++var3) {
                  var4.add(var6[var3]);
               }
            }

            ++var2;
         }
      }
   }

   public Format setInputFormat(Format var1) {
      if (!(var1 instanceof VideoFormat)) {
         return null;
      } else {
         int var2 = 0;

         while(true) {
            VideoFormat[] var3 = this.supportedInputFormats;
            if (var2 >= var3.length) {
               return null;
            }

            if (var1.matches(var3[var2])) {
               VideoFormat var4 = (VideoFormat)var1;
               this.inputFormat = var4;
               return var4;
            }

            ++var2;
         }
      }
   }

   public Format setOutputFormat(Format var1) {
      if (!(var1 instanceof VideoFormat)) {
         return null;
      } else {
         Format[] var3 = this.getMatchingOutputFormats(this.inputFormat);

         for(int var2 = 0; var2 < var3.length; ++var2) {
            if (var1.matches(var3[var2])) {
               VideoFormat var4 = (VideoFormat)var1;
               this.outputFormat = var4;
               return var4;
            }
         }

         return null;
      }
   }

   protected void updateOutput(Buffer var1, Format var2, int var3, int var4) {
      var1.setFormat(var2);
      var1.setLength(var3);
      var1.setOffset(var4);
   }

   protected void videoResized() {
   }
}
