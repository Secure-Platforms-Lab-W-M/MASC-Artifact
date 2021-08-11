package net.sf.fmj.media.renderer.video;

import java.io.ByteArrayInputStream;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.Owned;
import javax.media.control.FrameRateControl;
import javax.media.format.VideoFormat;
import javax.media.renderer.VideoRenderer;
import net.sf.fmj.media.AbstractVideoRenderer;
import net.sf.fmj.media.codec.video.jpeg.RFC2035;
import net.sf.fmj.utility.ArrayUtility;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.javax.imageio.ImageIO;
import org.atalk.android.util.javax.imageio.ImageReader;
import org.atalk.android.util.javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import org.atalk.android.util.javax.imageio.plugins.jpeg.JPEGImageReadParam;
import org.atalk.android.util.javax.imageio.plugins.jpeg.JPEGQTable;
import org.atalk.android.util.javax.imageio.stream.ImageInputStream;

public class JPEGRTPRenderer extends AbstractVideoRenderer implements VideoRenderer {
   private static final boolean TRACE = true;
   private static final Logger logger;
   private JVideoComponent component;
   private JPEGRTPRenderer.JPEGRTPFrame currentFrame;
   private ImageReader decoder;
   private float frameRate;
   private int framesProcessed;
   private JPEGHuffmanTable[] huffmanACTables;
   private JPEGHuffmanTable[] huffmanDCTables;
   private BufferedImage itsImage;
   private long lastTimestamp;
   private JPEGImageReadParam param;
   private JPEGQTable[] qtable;
   private int quality;
   private final Format[] supportedInputFormats;

   static {
      logger = LoggerSingleton.logger;
   }

   public JPEGRTPRenderer() {
      this.supportedInputFormats = new Format[]{new VideoFormat("jpeg/rtp", (Dimension)null, -1, Format.byteArray, -1.0F)};
      this.component = new JVideoComponent();
      this.quality = -1;
      this.frameRate = -1.0F;
      this.addControl(this);
      this.addControl(new JPEGRTPRenderer.VideoFrameRateControl());
   }

   private JPEGHuffmanTable[] createACHuffmanTables() {
      JPEGHuffmanTable var1 = new JPEGHuffmanTable(RFC2035.chm_ac_codelens, RFC2035.chm_ac_symbols);
      return new JPEGHuffmanTable[]{new JPEGHuffmanTable(RFC2035.lum_ac_codelens, RFC2035.lum_ac_symbols), var1};
   }

   private JPEGHuffmanTable[] createDCHuffmanTables() {
      JPEGHuffmanTable var1 = new JPEGHuffmanTable(RFC2035.chm_dc_codelens, RFC2035.chm_dc_symbols);
      return new JPEGHuffmanTable[]{new JPEGHuffmanTable(RFC2035.lum_dc_codelens, RFC2035.lum_dc_symbols), var1};
   }

   private JPEGQTable[] createQTable(int var1) {
      byte[] var2 = new byte[64];
      byte[] var3 = new byte[64];
      RFC2035.MakeTables(var1, var2, var3, RFC2035.jpeg_luma_quantizer_normal, RFC2035.jpeg_chroma_quantizer_normal);
      return new JPEGQTable[]{new JPEGQTable(ArrayUtility.byteArrayToIntArray(var2)), new JPEGQTable(ArrayUtility.byteArrayToIntArray(var3))};
   }

   private void dump(byte[] var1, int var2) {
      int var4;
      for(int var3 = 0; var3 < var2; var3 = var4) {
         String var6 = "";
         int var5 = 0;

         String var7;
         while(true) {
            var4 = var3;
            var7 = var6;
            if (var5 >= 16) {
               break;
            }

            var4 = var3 + 1;
            var7 = Integer.toHexString(var1[var3] & 255);
            StringBuilder var8 = new StringBuilder();
            var8.append(var6);
            if (var7.length() < 2) {
               StringBuilder var9 = new StringBuilder();
               var9.append("0");
               var9.append(var7);
               var6 = var9.toString();
            } else {
               var6 = var7;
            }

            var8.append(var6);
            var6 = var8.toString();
            StringBuilder var10 = new StringBuilder();
            var10.append(var6);
            var10.append(" ");
            var6 = var10.toString();
            if (var4 >= var2) {
               var7 = var6;
               break;
            }

            ++var5;
            var3 = var4;
         }

         System.out.println(var7);
      }

      System.out.println(" ");
   }

   private void initDecoder(int var1) {
      ImageReader var2 = this.decoder;
      if (var2 != null) {
         var2.dispose();
      }

      this.decoder = (ImageReader)ImageIO.getImageReadersByFormatName("JPEG").next();
      this.param = new JPEGImageReadParam();
      this.huffmanACTables = this.createACHuffmanTables();
      this.huffmanDCTables = this.createDCHuffmanTables();
      JPEGQTable[] var3 = this.createQTable(var1);
      this.qtable = var3;
      this.param.setDecodeTables(var3, this.huffmanDCTables, this.huffmanACTables);
   }

   public void close() {
      ImageReader var1 = this.decoder;
      if (var1 != null) {
         var1.dispose();
      }

   }

   public int doProcess(Buffer var1) {
      long var4 = var1.getTimeStamp();
      if (this.currentFrame == null) {
         this.currentFrame = new JPEGRTPRenderer.JPEGRTPFrame(var4);
      }

      if (var4 < this.currentFrame.timestamp) {
         Logger var9 = logger;
         StringBuilder var6 = new StringBuilder();
         var6.append("JPEGRTPRenderer: dropping packet ts=");
         var6.append(var4);
         var9.fine(var6.toString());
      } else if (var4 > this.currentFrame.timestamp) {
         Logger var12 = logger;
         StringBuilder var7 = new StringBuilder();
         var7.append("JPEGRTPRenderer: dropping current frame ts=");
         var7.append(this.currentFrame.timestamp);
         var7.append(", got new packet ts=");
         var7.append(var4);
         var12.fine(var7.toString());
         this.currentFrame.clear(var4);
         this.currentFrame.add(var1);
      } else {
         this.currentFrame.add(var1);
      }

      if (this.currentFrame.isComplete()) {
         byte[] var10 = this.currentFrame.getData();
         this.currentFrame = null;

         try {
            ByteArrayInputStream var11 = new ByteArrayInputStream(var10);
            ImageInputStream var13 = ImageIO.createImageInputStream(var11);
            this.decoder.setInput(var13, false, false);
            this.param.setDestination(this.itsImage);
            this.decoder.read(0, this.param);
            this.component.setImage(this.itsImage);
            var13.close();
            var11.close();
            var4 = System.nanoTime();
            if (-1L == this.lastTimestamp) {
               this.lastTimestamp = var4;
            }

            int var3 = this.framesProcessed + 1;
            this.framesProcessed = var3;
            if (var4 - this.lastTimestamp > 1000000000L) {
               float var2 = (float)(var4 - this.lastTimestamp) / 1000000.0F;
               this.frameRate = (float)var3 * (1000.0F / var2);
               this.framesProcessed = 0;
               this.lastTimestamp = var4;
            }

            return 0;
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }

      return 0;
   }

   public Component getComponent() {
      return this.component;
   }

   public String getName() {
      return "JPEG/RTP Renderer";
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public Format setInputFormat(Format var1) {
      VideoFormat var2 = (VideoFormat)super.setInputFormat(var1);
      if (var2 != null) {
         this.getComponent().setPreferredSize(var2.getSize());
      }

      return var2;
   }

   private class JPEGRTPFrame {
      public int count;
      public int dataLength;
      public JPEGRTPRenderer.JPEGRTPFrame firstItem;
      public long fragmentOffset;
      public boolean hasRTPMarker;
      public Buffer itemData;
      byte[] jpegHeader = new byte[]{-1, -40, -1, -32, 0, 16, 74, 70, 73, 70, 0, 1, 2, 0, 0, 1, 0, 1, 0, 0, -1, -64, 0, 17, 8, 0, -112, 0, -80, 3, 1, 34, 0, 2, 17, 1, 3, 17, 1, -1, -38, 0, 12, 3, 1, 0, 2, 17, 3, 17, 0, 63, 0};
      public JPEGRTPRenderer.JPEGRTPFrame nextItem;
      public long timestamp;

      public JPEGRTPFrame(long var2) {
         this.timestamp = var2;
      }

      public JPEGRTPFrame(Buffer var2) {
         this.itemData = var2;
      }

      public void add(Buffer var1) {
         JPEGRTPRenderer.JPEGRTPFrame var6 = JPEGRTPRenderer.this.new JPEGRTPFrame((Buffer)var1.clone());
         if ((var1.getFlags() & 2048) > 0) {
            this.hasRTPMarker = true;
         }

         ++this.count;
         if (this.firstItem == null) {
            this.firstItem = var6;
         } else {
            JPEGRTPRenderer.JPEGRTPFrame var5;
            for(var5 = this.firstItem; var5.nextItem != null; var5 = var5.nextItem) {
            }

            var5.nextItem = var6;
         }

         byte[] var7 = (byte[])((byte[])var1.getData());
         var6.fragmentOffset = 0L;

         for(int var2 = 0; var2 < 3; ++var2) {
            long var3 = var6.fragmentOffset << 8;
            var6.fragmentOffset = var3;
            var6.fragmentOffset = var3 + (long)(var7[var2 + 13] & 255);
         }

      }

      public void clear(long var1) {
         this.firstItem = null;
         this.timestamp = var1;
         this.hasRTPMarker = false;
         this.count = 0;
         this.dataLength = 0;
      }

      public byte[] getData() {
         byte[] var6 = this.jpegHeader;
         byte[] var8 = new byte[var6.length + this.dataLength + 2];
         System.arraycopy(var6, 0, var8, 0, var6.length);
         JPEGRTPRenderer.JPEGRTPFrame var7 = this.firstItem;
         long var4 = 0L;
         int var1 = this.jpegHeader.length;

         int var2;
         while(var7 != null) {
            JPEGRTPRenderer.JPEGRTPFrame var9;
            for(var9 = this.firstItem; var9 != null && var9.fragmentOffset != var4; var9 = var9.nextItem) {
            }

            var7 = var9;
            if (var9 != null) {
               var2 = var9.itemData.getLength() - 8;
               System.arraycopy((byte[])((byte[])var9.itemData.getData()), var9.itemData.getOffset() + 8, var8, var1, var2);
               var1 += var2;
               var4 += (long)var2;
               var7 = var9;
            }
         }

         var6 = (byte[])((byte[])this.firstItem.itemData.getData());
         var1 = var6[this.firstItem.itemData.getOffset() + 6] << 3;
         var2 = var6[this.firstItem.itemData.getOffset() + 7] << 3;
         var8[25] = (byte)(var2 >> 8 & 255);
         var8[26] = (byte)(var2 & 255);
         var8[27] = (byte)(var1 >> 8 & 255);
         var8[28] = (byte)(var1 & 255);
         var8[var8.length - 2] = -1;
         var8[var8.length - 1] = -39;
         byte var3 = var6[this.firstItem.itemData.getOffset() + 5];
         if (JPEGRTPRenderer.this.decoder == null) {
            JPEGRTPRenderer.this.initDecoder(var3);
         }

         if (JPEGRTPRenderer.this.quality != -1 && var3 != JPEGRTPRenderer.this.quality) {
            JPEGRTPRenderer.this.initDecoder(var3);
         }

         JPEGRTPRenderer.this.quality = var3;
         if (JPEGRTPRenderer.this.itsImage == null) {
            JPEGRTPRenderer.this.itsImage = new BufferedImage(var1, var2, 1);
         }

         return var8;
      }

      public boolean isComplete() {
         if (this.hasRTPMarker) {
            JPEGRTPRenderer.JPEGRTPFrame var5 = this.firstItem;
            long var2 = 0L;
            this.dataLength = 0;

            while(var5 != null) {
               JPEGRTPRenderer.JPEGRTPFrame var4;
               for(var4 = this.firstItem; var4 != null && var4.fragmentOffset != var2; var4 = var4.nextItem) {
               }

               var5 = var4;
               if (var4 != null) {
                  int var1 = var4.itemData.getLength() - 8;
                  this.dataLength += var1;
                  if ((var4.itemData.getFlags() & 2048) > 0) {
                     return true;
                  }

                  var2 += (long)var1;
                  var5 = var4;
               }
            }
         }

         return false;
      }
   }

   private class VideoFrameRateControl implements FrameRateControl, Owned {
      private VideoFrameRateControl() {
      }

      // $FF: synthetic method
      VideoFrameRateControl(Object var2) {
         this();
      }

      public Component getControlComponent() {
         return null;
      }

      public float getFrameRate() {
         return JPEGRTPRenderer.this.frameRate;
      }

      public float getMaxSupportedFrameRate() {
         return -1.0F;
      }

      public Object getOwner() {
         return JPEGRTPRenderer.this;
      }

      public float getPreferredFrameRate() {
         return -1.0F;
      }

      public float setFrameRate(float var1) {
         return -1.0F;
      }
   }
}
