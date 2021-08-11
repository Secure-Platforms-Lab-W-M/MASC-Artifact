package net.sf.fmj.utility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.H261Format;
import javax.media.format.H263Format;
import javax.media.format.IndexedColorFormat;
import javax.media.format.JPEGFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import net.sf.fmj.media.BonusAudioFormatEncodings;
import net.sf.fmj.media.format.GIFFormat;
import net.sf.fmj.media.format.PNGFormat;
import org.atalk.android.util.java.awt.Dimension;

public class FormatArgUtils {
   public static final String BIG_ENDIAN = "B";
   public static final String BYTE_ARRAY = "B";
   public static final String INT_ARRAY = "I";
   public static final String LITTLE_ENDIAN = "L";
   public static final String NOT_SPECIFIED = "?";
   private static final char SEP = ':';
   public static final String SHORT_ARRAY = "S";
   public static final String SIGNED = "S";
   public static final String UNSIGNED = "U";
   private static final Map formatClasses = new HashMap();
   private static final Map formatEncodings = new HashMap();

   static {
      buildFormatMap();
   }

   private static final void addAudioFormat(String var0) {
      addFormat(var0, AudioFormat.class);
   }

   private static final void addFormat(String var0, Class var1) {
      formatClasses.put(var0.toLowerCase(), var1);
      formatEncodings.put(var0.toLowerCase(), var0);
   }

   private static final void addVideoFormat(String var0) {
      addFormat(var0, VideoFormat.class);
   }

   private static final void buildFormatMap() {
      addAudioFormat("LINEAR");
      addAudioFormat("ULAW");
      addAudioFormat("ULAW/rtp");
      addAudioFormat("alaw");
      addAudioFormat("ima4");
      addAudioFormat("ima4/ms");
      addAudioFormat("msadpcm");
      addAudioFormat("dvi");
      addAudioFormat("dvi/rtp");
      addAudioFormat("g723");
      addAudioFormat("g723/rtp");
      addAudioFormat("g728");
      addAudioFormat("g728/rtp");
      addAudioFormat("g729");
      addAudioFormat("g729/rtp");
      addAudioFormat("g729a");
      addAudioFormat("g729a/rtp");
      addAudioFormat("gsm");
      addAudioFormat("gsm/ms");
      addAudioFormat("gsm/rtp");
      addAudioFormat("MAC3");
      addAudioFormat("MAC6");
      addAudioFormat("truespeech");
      addAudioFormat("msnaudio");
      addAudioFormat("mpeglayer3");
      addAudioFormat("voxwareac8");
      addAudioFormat("voxwareac10");
      addAudioFormat("voxwareac16");
      addAudioFormat("voxwareac20");
      addAudioFormat("voxwaremetavoice");
      addAudioFormat("voxwaremetasound");
      addAudioFormat("voxwarert29h");
      addAudioFormat("voxwarevr12");
      addAudioFormat("voxwarevr18");
      addAudioFormat("voxwaretq40");
      addAudioFormat("voxwaretq60");
      addAudioFormat("msrt24");
      addAudioFormat("mpegaudio");
      addAudioFormat("mpegaudio/rtp");
      addAudioFormat("dolbyac3");
      String[] var2 = BonusAudioFormatEncodings.ALL;
      int var1 = var2.length;

      for(int var0 = 0; var0 < var1; ++var0) {
         addAudioFormat(var2[var0]);
      }

      addVideoFormat("cvid");
      addFormat("jpeg", JPEGFormat.class);
      addVideoFormat("jpeg/rtp");
      addVideoFormat("mpeg");
      addVideoFormat("mpeg/rtp");
      addFormat("h261", H261Format.class);
      addVideoFormat("h261/rtp");
      addFormat("h263", H263Format.class);
      addVideoFormat("h263/rtp");
      addVideoFormat("h263-1998/rtp");
      addFormat("rgb", RGBFormat.class);
      addFormat("yuv", YUVFormat.class);
      addFormat("irgb", IndexedColorFormat.class);
      addVideoFormat("smc");
      addVideoFormat("rle");
      addVideoFormat("rpza");
      addVideoFormat("mjpg");
      addVideoFormat("mjpa");
      addVideoFormat("mjpb");
      addVideoFormat("iv32");
      addVideoFormat("iv41");
      addVideoFormat("iv50");
      addFormat("gif", GIFFormat.class);
      addFormat("png", PNGFormat.class);
   }

   private static final String dataTypeToStr(Class var0) {
      if (var0 == null) {
         return "?";
      } else if (var0 == Format.byteArray) {
         return "B";
      } else if (var0 == Format.shortArray) {
         return "S";
      } else if (var0 == Format.intArray) {
         return "I";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   private static final String dimensionToStr(Dimension var0) {
      if (var0 == null) {
         return "?";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append((int)var0.getWidth());
         var1.append("x");
         var1.append((int)var0.getHeight());
         return var1.toString();
      }
   }

   private static final String endianToStr(int var0) {
      if (var0 == -1) {
         return "?";
      } else if (var0 == 1) {
         return "B";
      } else if (var0 == 0) {
         return "L";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Unknown endianness: ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   private static final String floatToStr(float var0) {
      if (var0 == -1.0F) {
         return "?";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("");
         var1.append(var0);
         return var1.toString();
      }
   }

   private static final String intToStr(int var0) {
      if (var0 == -1) {
         return "?";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("");
         var1.append(var0);
         return var1.toString();
      }
   }

   public static Format parse(String var0) throws ParseException {
      FormatArgUtils.Tokens var16 = new FormatArgUtils.Tokens(var0.split(":"));
      var0 = var16.nextString((String)null);
      if (var0 != null) {
         Class var17 = (Class)formatClasses.get(var0.toLowerCase());
         StringBuilder var15;
         if (var17 != null) {
            String var20 = (String)formatEncodings.get(var0.toLowerCase());
            if (var20 != null) {
               int var6;
               int var7;
               int var8;
               int var9;
               int var10;
               Class var19;
               if (AudioFormat.class.isAssignableFrom(var17)) {
                  double var1 = var16.nextDouble();
                  var6 = var16.nextInt();
                  var7 = var16.nextInt();
                  var8 = var16.nextEndian();
                  var9 = var16.nextSigned();
                  var10 = var16.nextInt();
                  double var3 = var16.nextDouble();
                  var19 = var16.nextDataType();
                  if (var19 == null) {
                     var19 = Format.byteArray;
                  }

                  return new AudioFormat(var20, var1, var6, var7, var8, var9, var10, var3, var19);
               } else {
                  StringBuilder var18;
                  if (VideoFormat.class.isAssignableFrom(var17)) {
                     Dimension var21;
                     if (var17 == JPEGFormat.class) {
                        var21 = var16.nextDimension();
                        var6 = var16.nextInt();
                        var19 = var16.nextDataType();
                        if (var19 == null) {
                           var19 = Format.byteArray;
                        }

                        return new JPEGFormat(var21, var6, var19, var16.nextFloat(), -1, -1);
                     } else {
                        Class var22;
                        Dimension var23;
                        if (var17 == GIFFormat.class) {
                           var23 = var16.nextDimension();
                           var6 = var16.nextInt();
                           var22 = var16.nextDataType();
                           var19 = var22;
                           if (var22 == null) {
                              var19 = Format.byteArray;
                           }

                           return new GIFFormat(var23, var6, var19, var16.nextFloat());
                        } else if (var17 == PNGFormat.class) {
                           var23 = var16.nextDimension();
                           var6 = var16.nextInt();
                           var22 = var16.nextDataType();
                           var19 = var22;
                           if (var22 == null) {
                              var19 = Format.byteArray;
                           }

                           return new PNGFormat(var23, var6, var19, var16.nextFloat());
                        } else if (var17 == VideoFormat.class) {
                           var23 = var16.nextDimension();
                           var6 = var16.nextInt();
                           var19 = var16.nextDataType();
                           if (var19 == null) {
                              var19 = Format.byteArray;
                           }

                           return new VideoFormat(var20, var23, var6, var19, var16.nextFloat());
                        } else if (var17 == RGBFormat.class) {
                           var21 = var16.nextDimension();
                           var6 = var16.nextInt();
                           var19 = var16.nextDataType();
                           if (var19 == null) {
                              var19 = Format.byteArray;
                           }

                           float var5 = var16.nextFloat();
                           var7 = var16.nextInt();
                           var8 = var16.nextInt();
                           var9 = var16.nextInt();
                           var10 = var16.nextInt();
                           int var11 = var16.nextInt();
                           int var12 = var16.nextInt();
                           int var13 = var16.nextInt();
                           int var14 = var16.nextRGBFormatEndian();
                           return var11 == -1 && var12 == -1 && var13 == -1 && var14 == -1 ? new RGBFormat(var21, var6, var19, var5, var7, var8, var9, var10) : new RGBFormat(var21, var6, var19, var5, var7, var8, var9, var10, var11, var12, var13, var14);
                        } else {
                           var18 = new StringBuilder();
                           var18.append("TODO: Unknown class: ");
                           var18.append(var17);
                           throw new RuntimeException(var18.toString());
                        }
                     }
                  } else {
                     var18 = new StringBuilder();
                     var18.append("Unknown class: ");
                     var18.append(var17);
                     throw new RuntimeException(var18.toString());
                  }
               }
            } else {
               var15 = new StringBuilder();
               var15.append("Unknown encoding: ");
               var15.append(var0);
               throw new ParseException(var15.toString(), -1);
            }
         } else {
            var15 = new StringBuilder();
            var15.append("Unknown encoding: ");
            var15.append(var0);
            throw new ParseException(var15.toString(), -1);
         }
      } else {
         throw new ParseException("No encoding specified", 0);
      }
   }

   private static final String rgbFormatEndianToStr(int var0) {
      if (var0 == -1) {
         return "?";
      } else if (var0 == 0) {
         return "B";
      } else if (var0 == 1) {
         return "L";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Unknown endianness: ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   private static final String signedToStr(int var0) {
      if (var0 == -1) {
         return "?";
      } else if (var0 == 1) {
         return "S";
      } else if (var0 == 0) {
         return "U";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Unknown signedness: ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static String toString(Format var0) {
      ArrayList var2 = new ArrayList();
      var2.add(var0.getEncoding().toUpperCase());
      if (var0 instanceof AudioFormat) {
         AudioFormat var4 = (AudioFormat)var0;
         var2.add(intToStr((int)var4.getSampleRate()));
         var2.add(intToStr(var4.getSampleSizeInBits()));
         var2.add(intToStr(var4.getChannels()));
         var2.add(endianToStr(var4.getEndian()));
         var2.add(signedToStr(var4.getSigned()));
         var2.add(intToStr(var4.getFrameSizeInBits()));
         var2.add(intToStr((int)var4.getFrameRate()));
         if (var4.getDataType() != null && var4.getDataType() != Format.byteArray) {
            var2.add(dataTypeToStr(var4.getDataType()));
         }
      } else {
         StringBuilder var10;
         if (!(var0 instanceof VideoFormat)) {
            var10 = new StringBuilder();
            var10.append("");
            var10.append(var0);
            throw new IllegalArgumentException(var10.toString());
         }

         VideoFormat var3 = (VideoFormat)var0;
         if (var0.getClass() == JPEGFormat.class) {
            JPEGFormat var5 = (JPEGFormat)var3;
            var2.add(dimensionToStr(var5.getSize()));
            var2.add(intToStr(var5.getMaxDataLength()));
            if (var5.getDataType() != null && var5.getDataType() != Format.byteArray) {
               var2.add(dataTypeToStr(var5.getDataType()));
            }

            var2.add(floatToStr(var5.getFrameRate()));
         } else if (var0.getClass() == GIFFormat.class) {
            GIFFormat var6 = (GIFFormat)var3;
            var2.add(dimensionToStr(var6.getSize()));
            var2.add(intToStr(var6.getMaxDataLength()));
            if (var6.getDataType() != null && var6.getDataType() != Format.byteArray) {
               var2.add(dataTypeToStr(var6.getDataType()));
            }

            var2.add(floatToStr(var6.getFrameRate()));
         } else if (var0.getClass() == PNGFormat.class) {
            PNGFormat var7 = (PNGFormat)var3;
            var2.add(dimensionToStr(var7.getSize()));
            var2.add(intToStr(var7.getMaxDataLength()));
            if (var7.getDataType() != null && var7.getDataType() != Format.byteArray) {
               var2.add(dataTypeToStr(var7.getDataType()));
            }

            var2.add(floatToStr(var7.getFrameRate()));
         } else if (var0.getClass() == VideoFormat.class) {
            var2.add(dimensionToStr(var3.getSize()));
            var2.add(intToStr(var3.getMaxDataLength()));
            if (var3.getDataType() != null && var3.getDataType() != Format.byteArray) {
               var2.add(dataTypeToStr(var3.getDataType()));
            }

            var2.add(floatToStr(var3.getFrameRate()));
         } else {
            if (var0.getClass() != RGBFormat.class) {
               var10 = new StringBuilder();
               var10.append("Unknown or unsupported format: ");
               var10.append(var0);
               throw new IllegalArgumentException(var10.toString());
            }

            RGBFormat var8 = (RGBFormat)var3;
            var2.add(dimensionToStr(var3.getSize()));
            var2.add(intToStr(var3.getMaxDataLength()));
            if (var3.getDataType() != null && var3.getDataType() != Format.byteArray) {
               var2.add(dataTypeToStr(var3.getDataType()));
            }

            var2.add(floatToStr(var3.getFrameRate()));
            var2.add(intToStr(var8.getBitsPerPixel()));
            var2.add(intToStr(var8.getRedMask()));
            var2.add(intToStr(var8.getGreenMask()));
            var2.add(intToStr(var8.getBlueMask()));
            var2.add(intToStr(var8.getPixelStride()));
            var2.add(intToStr(var8.getLineStride()));
            var2.add(intToStr(var8.getFlipped()));
            var2.add(rgbFormatEndianToStr(var8.getEndian()));
         }
      }

      while(var2.get(var2.size() - 1) == null || ((String)var2.get(var2.size() - 1)).equals("?")) {
         var2.remove(var2.size() - 1);
      }

      StringBuilder var9 = new StringBuilder();

      for(int var1 = 0; var1 < var2.size(); ++var1) {
         if (var1 > 0) {
            var9.append(':');
         }

         var9.append((String)var2.get(var1));
      }

      return var9.toString();
   }

   private static class Tokens {
      private final String[] items;
      // $FF: renamed from: ix int
      private int field_175;

      public Tokens(String[] var1) {
         this.items = var1;
         this.field_175 = 0;
      }

      public Class nextDataType() throws ParseException {
         String var1 = this.nextString();
         if (var1 == null) {
            return null;
         } else if (var1.equals("?")) {
            return null;
         } else {
            var1 = var1.toUpperCase();
            if (var1.equals("B")) {
               return Format.byteArray;
            } else if (var1.equals("S")) {
               return Format.shortArray;
            } else if (var1.equals("I")) {
               return Format.intArray;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("Expected one of [B,S,I]: ");
               var2.append(var1);
               throw new ParseException(var2.toString(), -1);
            }
         }
      }

      public Dimension nextDimension() throws ParseException {
         String var3 = this.nextString();
         if (var3 == null) {
            return null;
         } else if (var3.equals("?")) {
            return null;
         } else {
            String var4 = var3.toUpperCase();
            String[] var7 = var4.split("X");
            if (var7.length == 2) {
               int var1;
               StringBuilder var9;
               try {
                  var1 = Integer.parseInt(var7[0]);
               } catch (NumberFormatException var6) {
                  var9 = new StringBuilder();
                  var9.append("Expected integer: ");
                  var9.append(var7[0]);
                  throw new ParseException(var9.toString(), -1);
               }

               int var2;
               try {
                  var2 = Integer.parseInt(var7[1]);
               } catch (NumberFormatException var5) {
                  var9 = new StringBuilder();
                  var9.append("Expected integer: ");
                  var9.append(var7[1]);
                  throw new ParseException(var9.toString(), -1);
               }

               return new Dimension(var1, var2);
            } else {
               StringBuilder var8 = new StringBuilder();
               var8.append("Expected WIDTHxHEIGHT: ");
               var8.append(var4);
               throw new ParseException(var8.toString(), -1);
            }
         }
      }

      public double nextDouble() throws ParseException {
         String var3 = this.nextString();
         if (var3 == null) {
            return -1.0D;
         } else if (var3.equals("?")) {
            return -1.0D;
         } else {
            try {
               double var1 = Double.parseDouble(var3);
               return var1;
            } catch (NumberFormatException var5) {
               StringBuilder var4 = new StringBuilder();
               var4.append("Expected double: ");
               var4.append(var3);
               throw new ParseException(var4.toString(), -1);
            }
         }
      }

      public int nextEndian() throws ParseException {
         String var1 = this.nextString();
         if (var1 == null) {
            return -1;
         } else if (var1.equals("?")) {
            return -1;
         } else {
            var1 = var1.toUpperCase();
            if (var1.equals("B")) {
               return 1;
            } else if (var1.equals("L")) {
               return 0;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("Expected one of [B,L]: ");
               var2.append(var1);
               throw new ParseException(var2.toString(), -1);
            }
         }
      }

      public float nextFloat() throws ParseException {
         String var2 = this.nextString();
         if (var2 == null) {
            return -1.0F;
         } else if (var2.equals("?")) {
            return -1.0F;
         } else {
            try {
               float var1 = Float.parseFloat(var2);
               return var1;
            } catch (NumberFormatException var4) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Expected float: ");
               var3.append(var2);
               throw new ParseException(var3.toString(), -1);
            }
         }
      }

      public int nextInt() throws ParseException {
         String var2 = this.nextString();
         if (var2 == null) {
            return -1;
         } else if (var2.equals("?")) {
            return -1;
         } else {
            try {
               int var1 = Integer.parseInt(var2);
               return var1;
            } catch (NumberFormatException var4) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Expected integer: ");
               var3.append(var2);
               throw new ParseException(var3.toString(), -1);
            }
         }
      }

      public int nextRGBFormatEndian() throws ParseException {
         String var1 = this.nextString();
         if (var1 == null) {
            return -1;
         } else if (var1.equals("?")) {
            return -1;
         } else {
            var1 = var1.toUpperCase();
            if (var1.equals("B")) {
               return 0;
            } else if (var1.equals("L")) {
               return 1;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("Expected one of [B,L]: ");
               var2.append(var1);
               throw new ParseException(var2.toString(), -1);
            }
         }
      }

      public int nextSigned() throws ParseException {
         String var1 = this.nextString();
         if (var1 == null) {
            return -1;
         } else if (var1.equals("?")) {
            return -1;
         } else {
            var1 = var1.toUpperCase();
            if (var1.equals("U")) {
               return 0;
            } else if (var1.equals("S")) {
               return 1;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("Expected one of [U,U]: ");
               var2.append(var1);
               throw new ParseException(var2.toString(), -1);
            }
         }
      }

      public String nextString() {
         return this.nextString((String)null);
      }

      public String nextString(String var1) {
         int var2 = this.field_175;
         String[] var3 = this.items;
         if (var2 >= var3.length) {
            return var1;
         } else {
            var1 = var3[var2];
            this.field_175 = var2 + 1;
            return var1;
         }
      }
   }
}
