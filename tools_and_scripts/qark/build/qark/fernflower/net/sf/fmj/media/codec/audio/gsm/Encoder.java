package net.sf.fmj.media.codec.audio.gsm;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.AbstractCodec;

public class Encoder extends AbstractCodec {
   private static final int GSM_BYTES = 33;
   private static final int PCM_BYTES = 320;
   private static final boolean TRACE = false;
   private Buffer innerBuffer = new Buffer();
   byte[] innerContent;
   private int innerDataLength = 0;
   protected Format[] outputFormats;

   public Encoder() {
      this.outputFormats = new Format[]{new AudioFormat("gsm", 8000.0D, 8, 1, -1, 1, 264, -1.0D, Format.byteArray)};
      this.inputFormats = new Format[]{new AudioFormat("LINEAR", 8000.0D, 16, 1, 1, 1, -1, -1.0D, Format.byteArray)};
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
      return "GSM Encoder";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.outputFormats;
      } else if (!(var1 instanceof AudioFormat)) {
         return new Format[]{null};
      } else {
         AudioFormat var2 = (AudioFormat)var1;
         return new Format[]{new AudioFormat("gsm", var2.getSampleRate(), 8, 1, var2.getEndian(), 1, 264, var2.getFrameRate(), Format.byteArray)};
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
      } else {
         byte[] var7 = (byte[])((byte[])var2.getData());
         if (var7 == null || var7.length < this.innerDataLength * 33 / 320) {
            var2.setData(new byte[this.innerDataLength / 320 * 33]);
         }

         if (this.innerDataLength < 320) {
            System.out.println("Not filled");
            return 4;
         } else {
            if (((AudioFormat)this.outputFormat).getEndian() != 1) {
               var4 = false;
            }

            var7 = new byte[this.innerDataLength / 320 * 33];
            var2.setData(var7);
            var2.setLength(this.innerDataLength / 320 * 33);
            GSMEncoderUtil.gsmEncode(var4, (byte[])((byte[])this.innerBuffer.getData()), this.innerBuffer.getOffset(), this.innerDataLength, var7);
            var2.setFormat(this.outputFormat);
            var2.setData(var7);
            int var3 = this.innerDataLength;
            var7 = new byte[var3 - var3 / 320 * 320];
            var6 = (byte[])((byte[])this.innerBuffer.getData());
            this.innerContent = var6;
            System.arraycopy(var6, this.innerDataLength / 320 * 320, var7, 0, var7.length);
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
      return super.setOutputFormat(var1);
   }
}
