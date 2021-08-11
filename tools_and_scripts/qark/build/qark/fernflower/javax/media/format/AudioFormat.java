package javax.media.format;

import javax.media.Format;

public class AudioFormat extends Format {
   public static final String ALAW = "alaw";
   public static final int BIG_ENDIAN = 1;
   public static final String DOLBYAC3 = "dolbyac3";
   public static final String DVI = "dvi";
   public static final String DVI_RTP = "dvi/rtp";
   public static final String G723 = "g723";
   public static final String G723_RTP = "g723/rtp";
   public static final String G728 = "g728";
   public static final String G728_RTP = "g728/rtp";
   public static final String G729 = "g729";
   public static final String G729A = "g729a";
   public static final String G729A_RTP = "g729a/rtp";
   public static final String G729_RTP = "g729/rtp";
   public static final String GSM = "gsm";
   public static final String GSM_MS = "gsm/ms";
   public static final String GSM_RTP = "gsm/rtp";
   public static final String IMA4 = "ima4";
   public static final String IMA4_MS = "ima4/ms";
   public static final String LINEAR = "LINEAR";
   public static final int LITTLE_ENDIAN = 0;
   public static final String MAC3 = "MAC3";
   public static final String MAC6 = "MAC6";
   public static final String MPEG = "mpegaudio";
   public static final String MPEGLAYER3 = "mpeglayer3";
   public static final String MPEG_RTP = "mpegaudio/rtp";
   public static final String MSADPCM = "msadpcm";
   public static final String MSNAUDIO = "msnaudio";
   public static final String MSRT24 = "msrt24";
   public static final int SIGNED = 1;
   public static final String TRUESPEECH = "truespeech";
   public static final String ULAW = "ULAW";
   public static final String ULAW_RTP = "ULAW/rtp";
   public static final int UNSIGNED = 0;
   public static final String VOXWAREAC10 = "voxwareac10";
   public static final String VOXWAREAC16 = "voxwareac16";
   public static final String VOXWAREAC20 = "voxwareac20";
   public static final String VOXWAREAC8 = "voxwareac8";
   public static final String VOXWAREMETASOUND = "voxwaremetasound";
   public static final String VOXWAREMETAVOICE = "voxwaremetavoice";
   public static final String VOXWARERT29H = "voxwarert29h";
   public static final String VOXWARETQ40 = "voxwaretq40";
   public static final String VOXWARETQ60 = "voxwaretq60";
   public static final String VOXWAREVR12 = "voxwarevr12";
   public static final String VOXWAREVR18 = "voxwarevr18";
   protected int channels;
   protected int endian;
   protected double frameRate;
   protected int frameSizeInBits;
   boolean init;
   int margin;
   double multiplier;
   protected double sampleRate;
   protected int sampleSizeInBits;
   protected int signed;

   public AudioFormat(String var1) {
      super(var1);
      this.sampleRate = -1.0D;
      this.sampleSizeInBits = -1;
      this.channels = -1;
      this.endian = -1;
      this.signed = -1;
      this.frameRate = -1.0D;
      this.frameSizeInBits = -1;
      this.multiplier = -1.0D;
      this.margin = 0;
      this.init = false;
   }

   public AudioFormat(String var1, double var2, int var4, int var5) {
      this(var1);
      this.sampleRate = var2;
      this.sampleSizeInBits = var4;
      this.channels = var5;
   }

   public AudioFormat(String var1, double var2, int var4, int var5, int var6, int var7) {
      this(var1, var2, var4, var5);
      this.endian = var6;
      this.signed = var7;
   }

   public AudioFormat(String var1, double var2, int var4, int var5, int var6, int var7, int var8, double var9, Class var11) {
      this(var1, var2, var4, var5, var6, var7);
      this.frameSizeInBits = var8;
      this.frameRate = var9;
      this.dataType = var11;
   }

   public Object clone() {
      AudioFormat var1 = new AudioFormat(this.encoding);
      var1.copy(this);
      return var1;
   }

   public long computeDuration(long var1) {
      double var3;
      if (this.init) {
         var3 = this.multiplier;
         return var3 < 0.0D ? -1L : (long)((double)(var1 - (long)this.margin) * var3) * 1000L;
      } else if (this.encoding == null) {
         this.init = true;
         return -1L;
      } else {
         int var5;
         int var6;
         if (!this.encoding.equalsIgnoreCase("LINEAR") && !this.encoding.equalsIgnoreCase("ULAW")) {
            if (this.encoding.equalsIgnoreCase("ULAW/rtp")) {
               var5 = this.sampleSizeInBits;
               if (var5 > 0) {
                  var6 = this.channels;
                  if (var6 > 0) {
                     var3 = this.sampleRate;
                     if (var3 > 0.0D) {
                        this.multiplier = (double)(8000000 / var5 / var6) / var3;
                     }
                  }
               }
            } else if (this.encoding.equalsIgnoreCase("dvi/rtp")) {
               var5 = this.sampleSizeInBits;
               if (var5 > 0) {
                  var3 = this.sampleRate;
                  if (var3 > 0.0D) {
                     this.multiplier = (double)(8000000 / var5) / var3;
                  }
               }

               this.margin = 4;
            } else if (this.encoding.equalsIgnoreCase("gsm/rtp")) {
               var3 = this.sampleRate;
               if (var3 > 0.0D) {
                  this.multiplier = 4848484.0D / var3;
               }
            } else if (this.encoding.equalsIgnoreCase("g723/rtp")) {
               var3 = this.sampleRate;
               if (var3 > 0.0D) {
                  this.multiplier = 1.0E7D / var3;
               }
            } else {
               var5 = this.frameSizeInBits;
               if (var5 != -1) {
                  var3 = this.frameRate;
                  if (var3 != -1.0D && var5 > 0 && var3 > 0.0D) {
                     this.multiplier = (double)(8000000 / var5) / var3;
                  }
               }
            }
         } else {
            var5 = this.sampleSizeInBits;
            if (var5 > 0) {
               var6 = this.channels;
               if (var6 > 0) {
                  var3 = this.sampleRate;
                  if (var3 > 0.0D) {
                     this.multiplier = (double)(8000000 / var5 / var6) / var3;
                  }
               }
            }
         }

         this.init = true;
         var3 = this.multiplier;
         return var3 > 0.0D ? (long)((double)(var1 - (long)this.margin) * var3) * 1000L : -1L;
      }
   }

   protected void copy(Format var1) {
      super.copy(var1);
      AudioFormat var2 = (AudioFormat)var1;
      this.sampleRate = var2.sampleRate;
      this.sampleSizeInBits = var2.sampleSizeInBits;
      this.channels = var2.channels;
      this.endian = var2.endian;
      this.signed = var2.signed;
      this.frameSizeInBits = var2.frameSizeInBits;
      this.frameRate = var2.frameRate;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof AudioFormat;
      boolean var3 = false;
      if (var2) {
         AudioFormat var4 = (AudioFormat)var1;
         var2 = var3;
         if (super.equals(var1)) {
            var2 = var3;
            if (this.sampleRate == var4.sampleRate) {
               var2 = var3;
               if (this.sampleSizeInBits == var4.sampleSizeInBits) {
                  var2 = var3;
                  if (this.channels == var4.channels) {
                     var2 = var3;
                     if (this.endian == var4.endian) {
                        var2 = var3;
                        if (this.signed == var4.signed) {
                           var2 = var3;
                           if (this.frameSizeInBits == var4.frameSizeInBits) {
                              var2 = var3;
                              if (this.frameRate == var4.frameRate) {
                                 var2 = true;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public int getChannels() {
      return this.channels;
   }

   public int getEndian() {
      return this.endian;
   }

   public double getFrameRate() {
      return this.frameRate;
   }

   public int getFrameSizeInBits() {
      return this.frameSizeInBits;
   }

   public double getSampleRate() {
      return this.sampleRate;
   }

   public int getSampleSizeInBits() {
      return this.sampleSizeInBits;
   }

   public int getSigned() {
      return this.signed;
   }

   public Format intersects(Format var1) {
      Format var5 = super.intersects(var1);
      if (var5 == null) {
         return null;
      } else if (!(var5 instanceof AudioFormat)) {
         return var5;
      } else {
         AudioFormat var6 = (AudioFormat)var1;
         AudioFormat var7 = (AudioFormat)var5;
         double var2 = this.sampleRate;
         if (var2 == -1.0D) {
            var2 = var6.sampleRate;
         }

         var7.sampleRate = var2;
         int var4 = this.sampleSizeInBits;
         if (var4 == -1) {
            var4 = var6.sampleSizeInBits;
         }

         var7.sampleSizeInBits = var4;
         var4 = this.channels;
         if (var4 == -1) {
            var4 = var6.channels;
         }

         var7.channels = var4;
         var4 = this.endian;
         if (var4 == -1) {
            var4 = var6.endian;
         }

         var7.endian = var4;
         var4 = this.signed;
         if (var4 == -1) {
            var4 = var6.signed;
         }

         var7.signed = var4;
         var4 = this.frameSizeInBits;
         if (var4 == -1) {
            var4 = var6.frameSizeInBits;
         }

         var7.frameSizeInBits = var4;
         var2 = this.frameRate;
         if (var2 == -1.0D) {
            var2 = var6.frameRate;
         }

         var7.frameRate = var2;
         return var7;
      }
   }

   public boolean matches(Format var1) {
      boolean var8 = super.matches(var1);
      boolean var9 = false;
      if (!var8) {
         return false;
      } else if (!(var1 instanceof AudioFormat)) {
         return true;
      } else {
         AudioFormat var10 = (AudioFormat)var1;
         double var2 = this.sampleRate;
         double var4;
         if (var2 != -1.0D) {
            var4 = var10.sampleRate;
            if (var4 != -1.0D) {
               var8 = var9;
               if (var2 != var4) {
                  return var8;
               }
            }
         }

         int var6 = this.sampleSizeInBits;
         int var7;
         if (var6 != -1) {
            var7 = var10.sampleSizeInBits;
            if (var7 != -1) {
               var8 = var9;
               if (var6 != var7) {
                  return var8;
               }
            }
         }

         var6 = this.channels;
         if (var6 != -1) {
            var7 = var10.channels;
            if (var7 != -1) {
               var8 = var9;
               if (var6 != var7) {
                  return var8;
               }
            }
         }

         var6 = this.endian;
         if (var6 != -1) {
            var7 = var10.endian;
            if (var7 != -1) {
               var8 = var9;
               if (var6 != var7) {
                  return var8;
               }
            }
         }

         var6 = this.signed;
         if (var6 != -1) {
            var7 = var10.signed;
            if (var7 != -1) {
               var8 = var9;
               if (var6 != var7) {
                  return var8;
               }
            }
         }

         var6 = this.frameSizeInBits;
         if (var6 != -1) {
            var7 = var10.frameSizeInBits;
            if (var7 != -1) {
               var8 = var9;
               if (var6 != var7) {
                  return var8;
               }
            }
         }

         var2 = this.frameRate;
         if (var2 != -1.0D) {
            var4 = var10.frameRate;
            if (var4 != -1.0D) {
               var8 = var9;
               if (var2 != var4) {
                  return var8;
               }
            }
         }

         var8 = true;
         return var8;
      }
   }

   public String toString() {
      String var2 = "";
      String var4 = "";
      int var1 = this.channels;
      StringBuilder var7;
      if (var1 == 1) {
         var2 = ", Mono";
      } else if (var1 == 2) {
         var2 = ", Stereo";
      } else if (var1 != -1) {
         var7 = new StringBuilder();
         var7.append(", ");
         var7.append(this.channels);
         var7.append("-channel");
         var2 = var7.toString();
      }

      String var3 = var4;
      if (this.sampleSizeInBits > 8) {
         var1 = this.endian;
         if (var1 == 1) {
            var3 = ", BigEndian";
         } else {
            var3 = var4;
            if (var1 == 0) {
               var3 = ", LittleEndian";
            }
         }
      }

      StringBuilder var6 = new StringBuilder();
      var6.append(this.getEncoding());
      StringBuilder var8;
      if (this.sampleRate != -1.0D) {
         var8 = new StringBuilder();
         var8.append(", ");
         var8.append(this.sampleRate);
         var8.append(" Hz");
         var4 = var8.toString();
      } else {
         var4 = ", Unknown Sample Rate";
      }

      var6.append(var4);
      var1 = this.sampleSizeInBits;
      String var5 = "";
      if (var1 != -1) {
         var8 = new StringBuilder();
         var8.append(", ");
         var8.append(this.sampleSizeInBits);
         var8.append("-bit");
         var4 = var8.toString();
      } else {
         var4 = "";
      }

      var6.append(var4);
      var6.append(var2);
      var6.append(var3);
      var1 = this.signed;
      if (var1 != -1) {
         if (var1 == 1) {
            var2 = ", Signed";
         } else {
            var2 = ", Unsigned";
         }
      } else {
         var2 = "";
      }

      var6.append(var2);
      if (this.frameRate != -1.0D) {
         var7 = new StringBuilder();
         var7.append(", ");
         var7.append(this.frameRate);
         var7.append(" frame rate");
         var2 = var7.toString();
      } else {
         var2 = "";
      }

      var6.append(var2);
      if (this.frameSizeInBits != -1) {
         var7 = new StringBuilder();
         var7.append(", FrameSize=");
         var7.append(this.frameSizeInBits);
         var7.append(" bits");
         var2 = var7.toString();
      } else {
         var2 = "";
      }

      var6.append(var2);
      var2 = var5;
      if (this.dataType != Format.byteArray) {
         var2 = var5;
         if (this.dataType != null) {
            var7 = new StringBuilder();
            var7.append(", ");
            var7.append(this.dataType);
            var2 = var7.toString();
         }
      }

      var6.append(var2);
      return var6.toString();
   }
}
