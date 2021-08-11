package net.sf.fmj.media.codec.audio.gsm;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.media.AudioFormatCompleter;

public class Decoder extends AbstractCodec {
   private static final int GSM_BYTES = 33;
   private static final int PCM_BYTES = 320;
   private static final boolean TRACE = false;
   private Buffer innerBuffer = new Buffer();
   byte[] innerContent;
   private int innerDataLength = 0;
   protected Format[] outputFormats;

   public Decoder() {
      this.outputFormats = new Format[]{new AudioFormat("LINEAR", 8000.0D, 16, 1, -1, 1, -1, -1.0D, Format.byteArray)};
      this.inputFormats = new Format[]{new AudioFormat("gsm", 8000.0D, 8, 1, -1, 1, 264, -1.0D, Format.byteArray)};
   }

   private byte[] mergeArrays(byte[] var1, byte[] var2) {
      if (var1 == null) {
         return var2;
      } else if (var2 == null) {
         return var1;
      } else {
         byte[] var3 = new byte[var1.length + var2.length];
         System.arraycopy(var1, 0, var3, 0, var1.length);
         System.arraycopy(var2, 0, var3, var1.length, var2.length);
         return var3;
      }
   }

   public void close() {
   }

   public String getName() {
      return "GSM Decoder";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.outputFormats;
      } else if (!(var1 instanceof AudioFormat)) {
         return new Format[]{null};
      } else {
         AudioFormat var2 = (AudioFormat)var1;
         return !var2.getEncoding().equals("gsm") || var2.getSampleSizeInBits() != 8 && var2.getSampleSizeInBits() != -1 || var2.getChannels() != 1 && var2.getChannels() != -1 || var2.getSigned() != 1 && var2.getSigned() != -1 || var2.getFrameSizeInBits() != 264 && var2.getFrameSizeInBits() != -1 || var2.getDataType() != null && var2.getDataType() != Format.byteArray ? new Format[]{null} : new Format[]{new AudioFormat("LINEAR", var2.getSampleRate(), 16, 1, var2.getEndian(), 1, 16, -1.0D, Format.byteArray)};
      }
   }

   public void open() {
   }

   public int process(Buffer var1, Buffer var2) {
      byte[] var6 = new byte[var1.getLength()];
      System.arraycopy(var1.getData(), var1.getOffset(), var6, 0, var6.length);
      var6 = this.mergeArrays((byte[])((byte[])this.innerBuffer.getData()), var6);
      this.innerBuffer.setData(var6);
      this.innerBuffer.setLength(var6.length);
      this.innerDataLength = this.innerBuffer.getLength();
      boolean var5 = this.checkInputBuffer(var1);
      boolean var4 = true;
      if (!var5) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else if (!this.checkInputBuffer(var1)) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         var6 = (byte[])((byte[])var2.getData());
         if (var6 == null || var6.length < this.innerBuffer.getLength() * 320 / 33) {
            var2.setData(new byte[this.innerBuffer.getLength() / 33 * 320]);
         }

         if (this.innerBuffer.getLength() < 33) {
            return 4;
         } else {
            if (((AudioFormat)this.outputFormat).getEndian() != 1) {
               var4 = false;
            }

            var6 = new byte[this.innerBuffer.getLength() / 33 * 320];
            var2.setData(var6);
            var2.setLength(this.innerBuffer.getLength() / 33 * 320);
            GSMDecoderUtil.gsmDecode(var4, (byte[])((byte[])this.innerBuffer.getData()), var1.getOffset(), this.innerBuffer.getLength(), var6);
            var2.setFormat(this.outputFormat);
            int var3 = this.innerDataLength;
            byte[] var7 = new byte[var3 - var3 / 33 * 33];
            var6 = (byte[])((byte[])this.innerBuffer.getData());
            this.innerContent = var6;
            System.arraycopy(var6, this.innerDataLength / 33 * 33, var7, 0, var7.length);
            var2.setOffset(0);
            this.innerBuffer.setLength(var7.length);
            this.innerBuffer.setData(var7);
            return 0;
         }
      }
   }

   public Format setInputFormat(Format var1) {
      return super.setInputFormat(var1);
   }

   public Format setOutputFormat(Format var1) {
      return !(var1 instanceof AudioFormat) ? null : super.setOutputFormat(AudioFormatCompleter.complete((AudioFormat)var1));
   }
}
