package net.sf.fmj.media.codec.video.jpeg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.Owned;
import javax.media.control.FormatControl;
import javax.media.control.QualityControl;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.AbstractPacketizer;
import net.sf.fmj.media.util.BufferToImage;
import net.sf.fmj.utility.ArrayUtility;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.Graphics;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.javax.imageio.ImageIO;
import org.atalk.android.util.javax.imageio.ImageWriter;
import org.atalk.android.util.javax.imageio.metadata.IIOMetadata;
import org.atalk.android.util.javax.imageio.metadata.IIOMetadataNode;
import org.atalk.android.util.javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import org.atalk.android.util.javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import org.atalk.android.util.javax.imageio.plugins.jpeg.JPEGQTable;
import org.atalk.android.util.javax.imageio.stream.MemoryCacheImageOutputStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Packetizer extends AbstractPacketizer {
   private static final int PACKET_SIZE = 1000;
   private static final int RTP_JPEG_RESTART = 64;
   private static final Logger logger = Logger.getLogger(Packetizer.class.getName());
   private BufferToImage bufferToImage;
   private int[] chromaQtable;
   private VideoFormat currentFormat;
   private int currentQuality;
   private int dri = 0;
   private ImageWriter encoder;
   private JPEGHuffmanTable[] huffmanACTables;
   private JPEGHuffmanTable[] huffmanDCTables;
   private Buffer imageBuffer;
   private Graphics imageGraphics;
   // $FF: renamed from: j int
   int field_31 = 0;
   private int[] lumaQtable;
   private BufferedImage offscreenImage;
   // $FF: renamed from: os java.io.ByteArrayOutputStream
   private ByteArrayOutputStream field_32 = new ByteArrayOutputStream();
   private MemoryCacheImageOutputStream out;
   private Format outputVideoFormat;
   private JPEGImageWriteParam param;
   private JPEGQTable[] qtable;
   private int quality = 75;
   private final Format[] supportedInputFormats;
   private final Format[] supportedOutputFormats;
   private Buffer temporary = new Buffer();
   private byte type = 1;
   private byte typeSpecific = 0;

   public Packetizer() {
      this.out = new MemoryCacheImageOutputStream(this.field_32);
      this.lumaQtable = RFC2035.jpeg_luma_quantizer_normal;
      this.chromaQtable = RFC2035.jpeg_chroma_quantizer_normal;
      this.supportedInputFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, -1, -1, -1, -1), new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, -1, -1, -1, -1)};
      this.supportedOutputFormats = new Format[]{new VideoFormat("jpeg/rtp", (Dimension)null, -1, Format.byteArray, -1.0F)};
      this.imageBuffer = new Buffer();
      this.inputFormats = this.supportedInputFormats;
      this.addControl(new Packetizer.JPEGQualityControl());
      this.addControl(new Packetizer.class_10());
   }

   // $FF: synthetic method
   static Format access$002(Packetizer var0, Format var1) {
      var0.outputVideoFormat = var1;
      return var1;
   }

   // $FF: synthetic method
   static Buffer access$100(Packetizer var0) {
      return var0.imageBuffer;
   }

   // $FF: synthetic method
   static BufferedImage access$202(Packetizer var0, BufferedImage var1) {
      var0.offscreenImage = var1;
      return var1;
   }

   // $FF: synthetic method
   static Graphics access$302(Packetizer var0, Graphics var1) {
      var0.imageGraphics = var1;
      return var1;
   }

   private JPEGHuffmanTable[] createACHuffmanTables() {
      JPEGHuffmanTable var1 = new JPEGHuffmanTable(RFC2035.chm_ac_codelens, RFC2035.chm_ac_symbols);
      return new JPEGHuffmanTable[]{new JPEGHuffmanTable(RFC2035.lum_ac_codelens, RFC2035.lum_ac_symbols), var1};
   }

   private JPEGHuffmanTable[] createDCHuffmanTables() {
      JPEGHuffmanTable var1 = new JPEGHuffmanTable(RFC2035.chm_dc_codelens, RFC2035.chm_dc_symbols);
      return new JPEGHuffmanTable[]{new JPEGHuffmanTable(RFC2035.lum_dc_codelens, RFC2035.lum_dc_symbols), var1};
   }

   private static Node createDri(Node var0, int var1) {
      IIOMetadataNode var2 = new IIOMetadataNode("dri");
      var2.setAttribute("interval", Integer.toString(var1));
      NodeList var3 = var0.getChildNodes();
      var3.item(1).insertBefore(var2, var3.item(1).getFirstChild());
      return var0;
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

   private static Node find(Node var0, String var1) {
      String[] var3 = var1.split("/");
      String[] var4 = var3[0].split(":");
      if (var3 == null) {
         return null;
      } else if (var3.length == 1) {
         return var0;
      } else {
         var1 = "";

         int var2;
         for(var2 = 1; var2 < var3.length; ++var2) {
            StringBuilder var5 = new StringBuilder();
            var5.append(var1);
            var5.append(var3[var2]);
            if (var2 == var3.length - 1) {
               var1 = "";
            } else {
               var1 = "/";
            }

            var5.append(var1);
            var1 = var5.toString();
         }

         if (var0.getNodeName().equalsIgnoreCase(var4[0]) && (var4.length <= 1 || var4[1].equalsIgnoreCase(var0.getNodeValue()))) {
            return find(var0, var1);
         } else {
            for(var2 = 0; var2 < var0.getChildNodes().getLength(); ++var2) {
               Node var6 = var0.getChildNodes().item(var2);
               if (var6.getNodeName().equalsIgnoreCase(var3[0]) && (var4.length <= 1 || var4[1].equalsIgnoreCase(var0.getNodeValue()))) {
                  return find(var6, var1);
               }
            }

            return null;
         }
      }
   }

   private static void outputMetadata(Node var0, String var1) {
      PrintStream var4 = System.out;
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append(var0.getNodeName());
      var4.println(var5.toString());
      StringBuilder var9 = new StringBuilder();
      var9.append("  ");
      var9.append(var1);
      var1 = var9.toString();
      NodeList var8 = var0.getChildNodes();

      for(int var2 = 0; var2 < var8.getLength(); ++var2) {
         Node var10 = var8.item(var2);
         if (var10.hasChildNodes()) {
            outputMetadata(var10, var1);
         }

         PrintStream var12 = System.out;
         StringBuilder var6 = new StringBuilder();
         var6.append(var1);
         var6.append(var10.getNodeName());
         var12.println(var6.toString());
         if (var8.item(var2).hasAttributes()) {
            NamedNodeMap var11 = var8.item(var2).getAttributes();
            var5 = new StringBuilder();
            var5.append("  ");
            var5.append(var1);
            var5.append("-A:");
            String var13 = var5.toString();

            for(int var3 = 0; var3 < var11.getLength(); ++var3) {
               PrintStream var14 = System.out;
               StringBuilder var7 = new StringBuilder();
               var7.append(var13);
               var7.append(var11.item(var3).getNodeName());
               var7.append(":");
               var7.append(var11.item(var3).getNodeValue());
               var14.println(var7.toString());
            }
         }
      }

   }

   private static Node setSamplingFactor(Node var0, int var1, int var2) {
      Node var3 = find(var0.getChildNodes().item(1), "markerSequence/sof/componentSpec/HsamplingFactor:1");
      var3.getAttributes().getNamedItem("HsamplingFactor").setNodeValue(Integer.toString(var1));
      var3.getAttributes().getNamedItem("VsamplingFactor").setNodeValue(Integer.toString(var2));
      return var0;
   }

   private void setType(int var1) {
      byte var2;
      if (this.dri != 0) {
         var2 = 64;
      } else {
         var2 = 0;
      }

      this.type = (byte)(var2 | var1);
   }

   public void close() {
      try {
         this.out.close();
         this.field_32.close();
         this.encoder.dispose();
      } catch (IOException var2) {
         logger.throwing(this.getClass().getName(), "close", var2.getCause());
      }
   }

   protected int doBuildPacketHeader(Buffer var1, byte[] var2) {
      VideoFormat var7 = (VideoFormat)this.inputFormat;
      int var5 = var7.getSize().width;
      int var6 = var7.getSize().height;
      var7 = this.currentFormat;
      if (var7 != null) {
         var5 = var7.getSize().width;
         var6 = this.currentFormat.getSize().height;
      }

      byte var3 = (byte)(var5 / 8);
      byte var4 = (byte)(var6 / 8);
      byte[] var8 = (new JpegRTPHeader(this.typeSpecific, var1.getOffset(), this.type, (byte)this.currentQuality, var3, var4)).toBytes();
      System.arraycopy(var8, 0, var2, 0, var8.length);
      return 0 + var8.length;
   }

   public String getName() {
      return "JPEG/RTP Packetizer";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      return var1 == null ? this.supportedOutputFormats : new Format[]{new VideoFormat("jpeg/rtp", ((VideoFormat)var1).getSize(), -1, Format.byteArray, -1.0F)};
   }

   public void open() {
      this.setPacketSize(1000);
      this.setDoNotSpanInputBuffers(true);
      this.temporary.setOffset(0);
      this.encoder = (ImageWriter)ImageIO.getImageWritersByFormatName("JPEG").next();
      this.param = new JPEGImageWriteParam((Locale)null);
      this.huffmanACTables = this.createACHuffmanTables();
      this.huffmanDCTables = this.createDCHuffmanTables();
      JPEGQTable[] var1 = this.createQTable(this.quality);
      this.qtable = var1;
      this.param.setEncodeTables(var1, this.huffmanDCTables, this.huffmanACTables);

      try {
         this.encoder.setOutput(this.out);
         this.encoder.prepareWriteSequence((IIOMetadata)null);
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public int process(Buffer param1, Buffer param2) {
      // $FF: Couldn't be decompiled
   }

   public Format setInputFormat(Format var1) {
      if (((VideoFormat)var1).getSize() == null) {
         return null;
      } else {
         this.bufferToImage = new BufferToImage((VideoFormat)var1);
         return super.setInputFormat(var1);
      }
   }

   private class class_10 implements FormatControl, Owned {
      private class_10() {
      }

      // $FF: synthetic method
      class_10(Object var2) {
         this();
      }

      public Component getControlComponent() {
         return null;
      }

      public Format getFormat() {
         return Packetizer.this.outputVideoFormat;
      }

      public Object getOwner() {
         return Packetizer.this;
      }

      public Format[] getSupportedFormats() {
         return null;
      }

      public boolean isEnabled() {
         return true;
      }

      public void setEnabled(boolean var1) {
      }

      public Format setFormat(Format param1) {
         // $FF: Couldn't be decompiled
      }
   }

   class JPEGQualityControl implements QualityControl, Owned {
      public Component getControlComponent() {
         return null;
      }

      public Object getOwner() {
         return Packetizer.this;
      }

      public float getPreferredQuality() {
         return 0.75F;
      }

      public float getQuality() {
         return (float)Packetizer.this.quality / 100.0F;
      }

      public boolean isTemporalSpatialTradeoffSupported() {
         return true;
      }

      public float setQuality(float var1) {
         Packetizer.this.quality = Math.round(100.0F * var1);
         if (Packetizer.this.quality > 99) {
            Packetizer.this.quality = 99;
         } else if (Packetizer.this.quality < 1) {
            Packetizer.this.quality = 1;
         }

         Packetizer var2 = Packetizer.this;
         var2.qtable = var2.createQTable(var2.quality);
         Packetizer.this.param.setEncodeTables(Packetizer.this.qtable, Packetizer.this.huffmanDCTables, Packetizer.this.huffmanACTables);
         return (float)Packetizer.this.quality;
      }
   }
}
