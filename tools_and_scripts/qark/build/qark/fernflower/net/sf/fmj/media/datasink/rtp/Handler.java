package net.sf.fmj.media.datasink.rtp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
import net.sf.fmj.media.AbstractDataSink;
import net.sf.fmj.utility.LoggerSingleton;

public class Handler extends AbstractDataSink {
   private static final Logger logger;
   private ParsedRTPUrl parsedRTPUrl;
   private RTPManager rtpManager;
   private PushBufferDataSource source;
   private SendStream[] streams;

   static {
      logger = LoggerSingleton.logger;
   }

   public void close() {
      RTPManager var1 = this.rtpManager;
      if (var1 != null) {
         var1.dispose();
      }

      try {
         this.stop();
      } catch (IOException var5) {
         Logger var2 = logger;
         Level var3 = Level.WARNING;
         StringBuilder var4 = new StringBuilder();
         var4.append("");
         var4.append(var5);
         var2.log(var3, var4.toString(), var5);
      }
   }

   public String getContentType() {
      PushBufferDataSource var1 = this.source;
      return var1 != null ? var1.getContentType() : null;
   }

   public Object getControl(String var1) {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append("TODO: getControl ");
      var3.append(var1);
      var2.warning(var3.toString());
      return null;
   }

   public Object[] getControls() {
      logger.warning("TODO: getControls");
      return new Object[0];
   }

   public void open() throws IOException, SecurityException {
      if (this.getOutputLocator() != null) {
         Logger var5;
         Level var6;
         StringBuilder var7;
         StringBuilder var29;
         try {
            this.parsedRTPUrl = RTPUrlParser.parse(this.getOutputLocator().toExternalForm());
         } catch (RTPUrlParserException var8) {
            var5 = logger;
            var6 = Level.WARNING;
            var7 = new StringBuilder();
            var7.append("");
            var7.append(var8);
            var5.log(var6, var7.toString(), var8);
            var29 = new StringBuilder();
            var29.append("");
            var29.append(var8);
            throw new IOException(var29.toString());
         }

         InvalidSessionAddressException var36;
         label107: {
            UnsupportedFormatException var10000;
            label113: {
               int var3;
               boolean var10001;
               try {
                  RTPManager var4 = RTPManager.newInstance();
                  this.rtpManager = var4;
                  var4.initialize(new SessionAddress());
                  RTPBonusFormatsMgr.addBonusFormats(this.rtpManager);
                  var3 = this.source.getStreams().length;
                  this.streams = new SendStream[var3];
               } catch (InvalidSessionAddressException var25) {
                  var36 = var25;
                  var10001 = false;
                  break label107;
               } catch (UnsupportedFormatException var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label113;
               }

               int var2 = 0;
               int var1 = 0;

               while(true) {
                  if (var1 >= var3) {
                     if (var2 > 0) {
                        try {
                           this.source.connect();
                           return;
                        } catch (InvalidSessionAddressException var9) {
                           var36 = var9;
                           var10001 = false;
                           break label107;
                        } catch (UnsupportedFormatException var10) {
                           var10000 = var10;
                           var10001 = false;
                           break;
                        }
                     } else {
                        try {
                           throw new IOException("No streams selected to be used");
                        } catch (InvalidSessionAddressException var11) {
                           var36 = var11;
                           var10001 = false;
                           break label107;
                        } catch (UnsupportedFormatException var12) {
                           var10000 = var12;
                           var10001 = false;
                           break;
                        }
                     }
                  }

                  label115: {
                     Logger var27;
                     String var28;
                     Format var30;
                     StringBuilder var32;
                     label98: {
                        label116: {
                           try {
                              var30 = this.source.getStreams()[var1].getFormat();
                              if (var30 instanceof AudioFormat) {
                                 break label116;
                              }
                           } catch (InvalidSessionAddressException var23) {
                              var36 = var23;
                              var10001 = false;
                              break label107;
                           } catch (UnsupportedFormatException var24) {
                              var10000 = var24;
                              var10001 = false;
                              break;
                           }

                           label92: {
                              try {
                                 if (var30 instanceof VideoFormat) {
                                    break label92;
                                 }
                              } catch (InvalidSessionAddressException var21) {
                                 var36 = var21;
                                 var10001 = false;
                                 break label107;
                              } catch (UnsupportedFormatException var22) {
                                 var10000 = var22;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 var27 = logger;
                                 var32 = new StringBuilder();
                                 var32.append("Skipping unknown source stream format: ");
                                 var32.append(var30);
                                 var27.warning(var32.toString());
                                 break label115;
                              } catch (InvalidSessionAddressException var13) {
                                 var36 = var13;
                                 var10001 = false;
                                 break label107;
                              } catch (UnsupportedFormatException var14) {
                                 var10000 = var14;
                                 var10001 = false;
                                 break;
                              }
                           }

                           var28 = "video";
                           break label98;
                        }

                        var28 = "audio";
                     }

                     ParsedRTPUrlElement var31;
                     try {
                        var31 = this.parsedRTPUrl.find(var28);
                     } catch (InvalidSessionAddressException var19) {
                        var36 = var19;
                        var10001 = false;
                        break label107;
                     } catch (UnsupportedFormatException var20) {
                        var10000 = var20;
                        var10001 = false;
                        break;
                     }

                     if (var31 == null) {
                        try {
                           var27 = logger;
                           var32 = new StringBuilder();
                           var32.append("Skipping source stream format not specified in URL: ");
                           var32.append(var30);
                           var27.fine(var32.toString());
                        } catch (InvalidSessionAddressException var17) {
                           var36 = var17;
                           var10001 = false;
                           break label107;
                        } catch (UnsupportedFormatException var18) {
                           var10000 = var18;
                           var10001 = false;
                           break;
                        }
                     } else {
                        try {
                           SessionAddress var33 = new SessionAddress(InetAddress.getByName(var31.host), var31.port, var31.ttl);
                           this.rtpManager.addTarget(var33);
                           this.streams[var1] = this.rtpManager.createSendStream(this.source, var1);
                        } catch (InvalidSessionAddressException var15) {
                           var36 = var15;
                           var10001 = false;
                           break label107;
                        } catch (UnsupportedFormatException var16) {
                           var10000 = var16;
                           var10001 = false;
                           break;
                        }

                        ++var2;
                     }
                  }

                  ++var1;
               }
            }

            UnsupportedFormatException var34 = var10000;
            var5 = logger;
            var6 = Level.WARNING;
            var7 = new StringBuilder();
            var7.append("");
            var7.append(var34);
            var5.log(var6, var7.toString(), var34);
            var29 = new StringBuilder();
            var29.append("");
            var29.append(var34);
            throw new IOException(var29.toString());
         }

         InvalidSessionAddressException var35 = var36;
         var5 = logger;
         var6 = Level.WARNING;
         var7 = new StringBuilder();
         var7.append("");
         var7.append(var35);
         var5.log(var6, var7.toString(), var35);
         var29 = new StringBuilder();
         var29.append("");
         var29.append(var35);
         throw new IOException(var29.toString());
      } else {
         throw new IOException("Output locator not set");
      }
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append("setSource: ");
      var3.append(var1);
      var2.finer(var3.toString());
      if (var1 instanceof PushBufferDataSource) {
         this.source = (PushBufferDataSource)var1;
      } else {
         throw new IncompatibleSourceException();
      }
   }

   public void start() throws IOException {
      this.source.start();
      int var1 = 0;

      while(true) {
         SendStream[] var2 = this.streams;
         if (var1 >= var2.length) {
            return;
         }

         if (var2[var1] != null) {
            var2[var1].start();
         }

         ++var1;
      }
   }

   public void stop() throws IOException {
      PushBufferDataSource var2 = this.source;
      if (var2 != null) {
         var2.stop();
      }

      if (this.streams != null) {
         int var1 = 0;

         while(true) {
            SendStream[] var3 = this.streams;
            if (var1 >= var3.length) {
               break;
            }

            if (var3[var1] != null) {
               var3[var1].stop();
            }

            ++var1;
         }
      }

   }
}
