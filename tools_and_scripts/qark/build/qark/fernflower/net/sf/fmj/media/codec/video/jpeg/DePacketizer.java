package net.sf.fmj.media.codec.video.jpeg;

import com.lti.utils.StringUtils;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.JPEGFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.utility.ArrayUtility;
import net.sf.fmj.utility.LoggingStringUtils;
import org.atalk.android.util.java.awt.Dimension;

public class DePacketizer extends AbstractCodec implements Codec {
   private static final boolean COMPARE_WITH_BASELINE = false;
   private static final boolean EXIT_AFTER_ONE_FRAME = false;
   private static final int MAX_ACTIVE_FRAME_ASSEMBLERS = 3;
   private static final int MAX_DUMP_SIZE = 200000;
   private static final boolean TRACE = false;
   private static final DePacketizer.BufferFragmentOffsetComparator bufferFragmentOffsetComparator = new DePacketizer.BufferFragmentOffsetComparator();
   private Codec baselineCodec;
   private final DePacketizer.FrameAssemblerCollection frameAssemblers;
   private long lastRTPtimestamp;
   private long lastTimestamp;
   private final Format[] supportedInputFormats;
   private final Format[] supportedOutputFormats;

   public DePacketizer() {
      this.supportedInputFormats = new Format[]{new VideoFormat("jpeg/rtp", (Dimension)null, -1, Format.byteArray, -1.0F)};
      this.supportedOutputFormats = new Format[]{new JPEGFormat()};
      this.lastRTPtimestamp = -1L;
      this.frameAssemblers = new DePacketizer.FrameAssemblerCollection();
   }

   private static int buildJFIFHeader(byte[] var0, int var1) {
      int var2 = var1 + 1;
      var0[var1] = -1;
      var1 = var2 + 1;
      var0[var2] = -32;
      var2 = var1 + 1;
      var0[var1] = 0;
      var1 = var2 + 1;
      var0[var2] = 16;
      var2 = var1 + 1;
      var0[var1] = 74;
      var1 = var2 + 1;
      var0[var2] = 70;
      var2 = var1 + 1;
      var0[var1] = 73;
      var1 = var2 + 1;
      var0[var2] = 70;
      var2 = var1 + 1;
      var0[var1] = 0;
      var1 = var2 + 1;
      var0[var2] = 1;
      var2 = var1 + 1;
      var0[var1] = 1;
      var1 = var2 + 1;
      var0[var2] = 0;
      var2 = var1 + 1;
      var0[var1] = 0;
      var1 = var2 + 1;
      var0[var2] = 1;
      var2 = var1 + 1;
      var0[var1] = 0;
      var1 = var2 + 1;
      var0[var2] = 1;
      var2 = var1 + 1;
      var0[var1] = 0;
      var0[var2] = 0;
      return var2 + 1;
   }

   private static String dump(byte[] var0, int var1, int var2) {
      return StringUtils.dump(var0, var1, var1 + var2);
   }

   private static void dump(Buffer var0, String var1) {
   }

   private static boolean hasJPEGHeaders(byte[] var0, int var1, int var2) {
      if (var2 >= 2) {
         var2 = var1 + 1;
         if (var0[var1] != -1) {
            return false;
         } else {
            return var0[var2] == -40;
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static boolean hasJPEGTrailer(byte[] var0, int var1, int var2) {
      if (var2 >= 2) {
         var2 = var1 + 1;
         if (var0[var1] != -1) {
            return false;
         } else {
            return var0[var2] == -39;
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   private static void zeroData(byte[] var0) {
      int var2 = var0.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         var0[var1] = 0;
      }

   }

   public void close() {
      Codec var1 = this.baselineCodec;
      if (var1 != null) {
         var1.close();
      }

      super.close();
      this.frameAssemblers.clear();
   }

   public Object getControl(String var1) {
      Codec var2 = this.baselineCodec;
      return var2 != null ? var2.getControl(var1) : super.getControl(var1);
   }

   public Object[] getControls() {
      Codec var1 = this.baselineCodec;
      return var1 != null ? var1.getControls() : super.getControls();
   }

   public String getName() {
      return "JPEG DePacketizer";
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.supportedOutputFormats;
      } else {
         VideoFormat var4 = (VideoFormat)var1;
         Dimension var3 = new Dimension(320, 240);
         if (var4.getSize() != null) {
            var3 = var4.getSize();
         }

         JPEGFormat var8 = new JPEGFormat(var3, -1, Format.byteArray, -1.0F, -1, -1);
         Codec var9 = this.baselineCodec;
         if (var9 != null) {
            Format[] var10 = var9.getSupportedOutputFormats(var1);
            PrintStream var5 = System.out;
            StringBuilder var6 = new StringBuilder();
            var6.append("input:  ");
            var6.append(LoggingStringUtils.formatToStr(var1));
            var5.println(var6.toString());

            for(int var2 = 0; var2 < var10.length; ++var2) {
               PrintStream var7 = System.out;
               StringBuilder var11 = new StringBuilder();
               var11.append("output: ");
               var11.append(LoggingStringUtils.formatToStr(var10[0]));
               var7.println(var11.toString());
            }
         }

         return new Format[]{var8};
      }
   }

   public void open() throws ResourceUnavailableException {
      Codec var1 = this.baselineCodec;
      if (var1 != null) {
         var1.open();
      }

      super.open();
   }

   public int process(Buffer var1, Buffer var2) {
      if (!var1.isDiscard()) {
         Codec var5 = this.baselineCodec;
         if (var5 != null) {
            var5.process(var1, var2);
         }

         if (var1.getLength() >= 8) {
            JpegRTPHeader.parse((byte[])((byte[])var1.getData()), var1.getOffset());
         }

         long var3 = var1.getTimeStamp();
         if ((var1.getFlags() & 2048) != 0) {
         }

         DePacketizer.FrameAssembler var6 = this.frameAssemblers.findOrAdd(var3);
         var6.put((Buffer)var1.clone());
         if (var6.complete()) {
            if (this.baselineCodec != null) {
               var2 = new Buffer();
            }

            var6.copyToBuffer(var2);
            this.frameAssemblers.remove(var3);
            this.frameAssemblers.removeOlderThan(var3);
            if (this.lastRTPtimestamp == -1L) {
               this.lastRTPtimestamp = var1.getTimeStamp();
               this.lastTimestamp = System.nanoTime();
            }

            return 0;
         } else {
            this.frameAssemblers.removeAllButNewestN(3);
            var2.setDiscard(true);
            return 4;
         }
      } else {
         var2.setDiscard(true);
         return 4;
      }
   }

   public void reset() {
      Codec var1 = this.baselineCodec;
      if (var1 != null) {
         var1.reset();
      }

      super.reset();
      this.frameAssemblers.clear();
   }

   public Format setInputFormat(Format var1) {
      if (this.baselineCodec != null) {
         super.setInputFormat(var1);
         return this.baselineCodec.setInputFormat(var1);
      } else {
         return super.setInputFormat(var1);
      }
   }

   public Format setOutputFormat(Format var1) {
      if (this.baselineCodec != null) {
         super.setOutputFormat(var1);
         return this.baselineCodec.setOutputFormat(var1);
      } else {
         return super.setOutputFormat(var1);
      }
   }

   private static class BufferFragmentOffsetComparator implements Comparator {
      private BufferFragmentOffsetComparator() {
      }

      // $FF: synthetic method
      BufferFragmentOffsetComparator(Object var1) {
         this();
      }

      public int compare(Buffer var1, Buffer var2) {
         if (var1 == null && var2 == null) {
            return 0;
         } else if (var1 == null) {
            return -1;
         } else if (var2 == null) {
            return 1;
         } else {
            JpegRTPHeader var3 = JpegRTPHeader.parse((byte[])((byte[])var1.getData()), var1.getOffset());
            JpegRTPHeader var4 = JpegRTPHeader.parse((byte[])((byte[])var2.getData()), var2.getOffset());
            return var3.getFragmentOffset() - var4.getFragmentOffset();
         }
      }
   }

   static class FrameAssembler {
      private final List list = new ArrayList();
      private boolean rtpMarker;

      private boolean contiguous() {
         int var2 = 0;

         int var3;
         Buffer var5;
         for(Iterator var4 = this.list.iterator(); var4.hasNext(); var2 += var5.getLength() - 8 - var3) {
            var5 = (Buffer)var4.next();
            JpegRTPHeader var6 = this.parseJpegRTPHeader(var5);
            int var1 = 0;
            if (var6.getType() > 63) {
               var1 = 0 + 4;
            }

            var3 = var1;
            if (var6.getQ() >= 128) {
               var3 = var5.getOffset() + 8 + var1 + 2;
               byte[] var7 = (byte[])((byte[])var5.getData());
               var3 = var1 + ((var7[var3] & 255) << 8 | var7[var3 + 1] & 255) + 4;
            }

            if (var6.getFragmentOffset() != var2) {
               return false;
            }
         }

         return true;
      }

      private JpegRTPHeader parseJpegRTPHeader(Buffer var1) {
         return JpegRTPHeader.parse((byte[])((byte[])var1.getData()), var1.getOffset());
      }

      public boolean complete() {
         if (!this.rtpMarker) {
            return false;
         } else if (this.list.size() <= 0) {
            return false;
         } else {
            return this.contiguous();
         }
      }

      public int copyToBuffer(Buffer var1) {
         if (!this.rtpMarker) {
            throw new IllegalStateException();
         } else if (this.list.size() > 0) {
            Buffer var15 = (Buffer)this.list.get(0);
            boolean var5 = DePacketizer.hasJPEGHeaders((byte[])((byte[])var15.getData()), var15.getOffset() + 8, var15.getLength() - 8) ^ true;
            short var2;
            if (var5) {
               var2 = 1024;
            } else {
               var2 = 0;
            }

            int var8 = this.frameLength();
            int var4 = var15.getOffset();
            byte var6 = 0;
            byte[] var12 = null;
            byte[] var13 = null;
            byte[] var16 = (byte[])((byte[])var15.getData());
            byte[] var11;
            if (var1.getData() != null && ((byte[])((byte[])var1.getData())).length >= var8 + var2 + 2) {
               var11 = (byte[])((byte[])var1.getData());
               DePacketizer.zeroData(var11);
            } else {
               var11 = new byte[var8 + var2 + 2];
            }

            int var3 = 0;
            if (var5) {
               var3 = 0 + 1;
               var11[0] = -1;
               var11[var3] = -40;
               int var9 = DePacketizer.buildJFIFHeader(var11, var3 + 1);
               JpegRTPHeader var17 = this.parseJpegRTPHeader(var15);
               int var7 = var4 + 8;
               var4 = var7;
               var3 = var6;
               byte var21;
               if (var17.getType() >= 64) {
                  var4 = var7;
                  var3 = var6;
                  if (var17.getType() <= 127) {
                     var3 = var7 + 1;
                     byte var19 = var16[var7];
                     var21 = var16[var3];
                     var4 = var3 + 1 + 2;
                     var3 = var21 & 255 | (var19 & 255) << 8;
                  }
               }

               if (var17.getQ() > 127) {
                  int var20 = var4 + 2;
                  var4 = var20 + 1;
                  var21 = var16[var20];
                  var20 = var4 + 1;
                  var4 = var16[var4] & 255 | (var21 & 255) << 8;
                  var12 = ArrayUtility.copyOfRange(var16, var20, var4 / 2 + var20);
                  var20 += var4 / 2;
                  var13 = ArrayUtility.copyOfRange(var16, var20, var4 / 2 + var20);
                  var4 /= 2;
               }

               var3 = RFC2035.MakeHeaders(false, var11, var9, var17.getType(), var17.getQ(), var17.getWidthInBlocks(), var17.getHeightInBlocks(), var12, var13, var3);
            }

            Iterator var22 = this.list.iterator();

            while(var22.hasNext()) {
               Buffer var23 = (Buffer)var22.next();
               JpegRTPHeader var14 = this.parseJpegRTPHeader(var23);
               System.arraycopy(var23.getData(), var23.getOffset() + 8, var11, var3 + var14.getFragmentOffset(), var23.getLength() - 8);
            }

            boolean var10 = DePacketizer.hasJPEGTrailer(var11, var3 + var8, 2);
            int var18 = 0;
            if (var10 ^ true) {
               var18 = 0 + 1;
               var11[var3 + var8 + 0] = -1;
               var11[var3 + var8 + var18] = -39;
               ++var18;
            }

            var1.setData(var11);
            var1.setLength(var3 + var8 + var18);
            var1.setOffset(0);
            var1.setRtpTimeStamp(var15.getRtpTimeStamp());
            var1.setTimeStamp(var15.getTimeStamp());
            return var3;
         } else {
            throw new IllegalStateException();
         }
      }

      public int frameLength() {
         if (this.rtpMarker) {
            if (this.list.size() > 0) {
               List var1 = this.list;
               Buffer var2 = (Buffer)var1.get(var1.size() - 1);
               return this.parseJpegRTPHeader(var2).getFragmentOffset() + var2.getLength() - 8;
            } else {
               throw new IllegalStateException();
            }
         } else {
            throw new IllegalStateException();
         }
      }

      public void put(Buffer var1) {
         if (!this.rtpMarker) {
            boolean var2;
            if ((var1.getFlags() & 2048) != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.rtpMarker = var2;
         }

         if (var1.getLength() > 8) {
            this.list.add(var1);
            Collections.sort(this.list, DePacketizer.bufferFragmentOffsetComparator);
         }
      }
   }

   private static class FrameAssemblerCollection {
      private Map frameAssemblers;

      private FrameAssemblerCollection() {
         this.frameAssemblers = new HashMap();
      }

      // $FF: synthetic method
      FrameAssemblerCollection(Object var1) {
         this();
      }

      public void clear() {
         this.frameAssemblers.clear();
      }

      public DePacketizer.FrameAssembler findOrAdd(long var1) {
         Long var5 = var1;
         DePacketizer.FrameAssembler var4 = (DePacketizer.FrameAssembler)this.frameAssemblers.get(var5);
         DePacketizer.FrameAssembler var3 = var4;
         if (var4 == null) {
            var3 = new DePacketizer.FrameAssembler();
            this.frameAssemblers.put(var5, var3);
         }

         return var3;
      }

      public long getOldestTimestamp() {
         long var1 = -1L;

         long var3;
         for(Iterator var5 = this.frameAssemblers.keySet().iterator(); var5.hasNext(); var1 = var3) {
            Long var6 = (Long)var5.next();
            if (var1 >= 0L) {
               var3 = var1;
               if (var6 >= var1) {
                  continue;
               }
            }

            var3 = var6;
         }

         return var1;
      }

      public void remove(long var1) {
         this.frameAssemblers.remove(var1);
      }

      public void removeAllButNewestN(int var1) {
         while(true) {
            if (this.frameAssemblers.size() > var1) {
               long var2 = this.getOldestTimestamp();
               if (var2 >= 0L) {
                  Long var4 = var2;
                  if (((DePacketizer.FrameAssembler)this.frameAssemblers.get(var4)).complete()) {
                  }

                  this.frameAssemblers.remove(var4);
                  continue;
               }

               throw new RuntimeException();
            }

            return;
         }
      }

      public void removeOlderThan(long var1) {
         Iterator var3 = this.frameAssemblers.entrySet().iterator();

         while(var3.hasNext()) {
            if ((Long)((Entry)var3.next()).getKey() < var1) {
               var3.remove();
            }
         }

      }
   }
}
