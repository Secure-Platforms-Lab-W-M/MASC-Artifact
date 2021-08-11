package com.ibm.media.codec.audio;

import com.sun.media.BasicCodec;
import javax.media.Format;
import javax.media.format.AudioFormat;

public abstract class AudioCodec extends BasicCodec {
   protected final boolean DEBUG = true;
   protected String PLUGIN_NAME;
   protected AudioFormat[] defaultOutputFormats;
   protected AudioFormat inputFormat;
   protected AudioFormat outputFormat;
   protected AudioFormat[] supportedInputFormats;
   protected AudioFormat[] supportedOutputFormats;

   public boolean checkFormat(Format var1) {
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
         return var1 instanceof AudioFormat && matches(var1, this.supportedInputFormats) != null ? this.getMatchingOutputFormats(var1) : new Format[0];
      }
   }

   public Format setInputFormat(Format var1) {
      if (var1 instanceof AudioFormat && matches(var1, this.supportedInputFormats) != null) {
         this.inputFormat = (AudioFormat)var1;
         return var1;
      } else {
         return null;
      }
   }

   public Format setOutputFormat(Format var1) {
      if (var1 instanceof AudioFormat && matches(var1, this.getMatchingOutputFormats(this.inputFormat)) != null) {
         this.outputFormat = (AudioFormat)var1;
         return var1;
      } else {
         return null;
      }
   }
}
