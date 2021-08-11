package net.sf.fmj.media.parser;

import com.lti.utils.StringUtils;
import com.lti.utils.synchronization.ProducerConsumerQueue;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.media.Buffer;
import javax.media.Format;
import net.sf.fmj.utility.FormatArgUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class XmlMovieSAXHandler extends DefaultHandler {
   private static final int AWAIT_BUFFER = 10;
   private static final int AWAIT_DATA = 11;
   private static final int INIT = 0;
   private static final int READ_DATA = 12;
   private Buffer currentBuffer;
   private StringBuilder currentDataChars;
   private int currentTrack = -1;
   private final Map formatsMap = new HashMap();
   private final Map qBuffers = new HashMap();
   private final ProducerConsumerQueue qMeta = new ProducerConsumerQueue();
   private int state = 0;

   private static int getIntAttr(Attributes var0, String var1) throws SAXException {
      if (var0.getIndex(var1) >= 0) {
         return getIntAttr(var0, var1, 0);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Missing attribute: ");
         var2.append(var1);
         throw new SAXException(var2.toString());
      }
   }

   private static int getIntAttr(Attributes var0, String var1, int var2) throws SAXException {
      int var3 = var0.getIndex(var1);
      if (var3 < 0) {
         return var2;
      } else {
         String var6 = var0.getValue(var3);

         try {
            var2 = Integer.parseInt(var6);
            return var2;
         } catch (NumberFormatException var5) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Expected integer: ");
            var4.append(var6);
            throw new SAXException(var4.toString(), var5);
         }
      }
   }

   private static long getLongAttr(Attributes var0, String var1) throws SAXException {
      if (var0.getIndex(var1) >= 0) {
         return getLongAttr(var0, var1, 0L);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Missing attribute: ");
         var2.append(var1);
         throw new SAXException(var2.toString());
      }
   }

   private static long getLongAttr(Attributes var0, String var1, long var2) throws SAXException {
      int var4 = var0.getIndex(var1);
      if (var4 < 0) {
         return var2;
      } else {
         String var7 = var0.getValue(var4);

         try {
            var2 = Long.parseLong(var7);
            return var2;
         } catch (NumberFormatException var6) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Expected long: ");
            var5.append(var7);
            throw new SAXException(var5.toString(), var6);
         }
      }
   }

   private static String getStringAttr(Attributes var0, String var1) throws SAXException {
      if (var0.getIndex(var1) >= 0) {
         return getStringAttr(var0, var1, (String)null);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Missing attribute: ");
         var2.append(var1);
         throw new SAXException(var2.toString());
      }
   }

   private static String getStringAttr(Attributes var0, String var1, String var2) throws SAXException {
      int var3 = var0.getIndex(var1);
      return var3 < 0 ? var2 : var0.getValue(var3);
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      String var5;
      if (this.state == 12) {
         var5 = new String(var1, var2, var3);
         this.currentDataChars.append(var5);
      } else {
         var5 = (new String(var1, var2, var3)).trim();
         if (var5.length() > 0) {
            StringBuilder var4 = new StringBuilder();
            var4.append("characters unexpected, state=");
            var4.append(this.state);
            var4.append(" chars=");
            var4.append(var5);
            throw new SAXException(var4.toString());
         }
      }
   }

   public void endDocument() throws SAXException {
      Map var1 = this.qBuffers;
      if (var1 != null) {
         Iterator var5 = var1.values().iterator();

         while(var5.hasNext()) {
            ProducerConsumerQueue var2 = (ProducerConsumerQueue)var5.next();
            if (var2 != null) {
               Buffer var3 = new Buffer();
               var3.setEOM(true);

               try {
                  var2.put(var3);
               } catch (InterruptedException var4) {
                  throw new SAXException(var4);
               }
            }
         }
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (!var2.equals("Tracks")) {
         if (var2.equals("Data")) {
            byte[] var13 = StringUtils.hexStringToByteArray(this.currentDataChars.toString());
            this.currentBuffer.setData(var13);
            this.currentBuffer.setOffset(0);
            this.currentBuffer.setLength(var13.length);

            try {
               ((ProducerConsumerQueue)this.qBuffers.get(this.currentTrack)).put(this.currentBuffer);
            } catch (InterruptedException var5) {
               throw new SAXException(var5);
            }

            this.currentBuffer = null;
            this.currentTrack = -1;
            this.currentDataChars = null;
            this.state = 10;
         }
      } else if (this.formatsMap.size() != 0) {
         InterruptedException var10000;
         label67: {
            Format[] var10;
            boolean var10001;
            try {
               var10 = new Format[this.formatsMap.size()];
            } catch (InterruptedException var9) {
               var10000 = var9;
               var10001 = false;
               break label67;
            }

            int var4 = 0;

            while(true) {
               Format var14;
               try {
                  if (var4 >= var10.length) {
                     break;
                  }

                  var14 = (Format)this.formatsMap.get(var4);
               } catch (InterruptedException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label67;
               }

               if (var14 == null) {
                  try {
                     StringBuilder var11 = new StringBuilder();
                     var11.append("Expected format for track ");
                     var11.append(var4);
                     throw new SAXException(var11.toString());
                  } catch (InterruptedException var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label67;
                  }
               }

               var10[var4] = var14;
               ++var4;
            }

            try {
               this.qMeta.put(var10);
               return;
            } catch (InterruptedException var7) {
               var10000 = var7;
               var10001 = false;
            }
         }

         InterruptedException var12 = var10000;
         var12.printStackTrace();
      } else {
         throw new SAXException("No tracks");
      }
   }

   public void postError(Exception var1) throws InterruptedException {
      ProducerConsumerQueue var2 = this.qMeta;
      if (var2 != null) {
         var2.put(var1);
      }

      Map var4 = this.qBuffers;
      if (var4 != null) {
         Iterator var5 = var4.values().iterator();

         while(var5.hasNext()) {
            ProducerConsumerQueue var3 = (ProducerConsumerQueue)var5.next();
            if (var3 != null) {
               var3.put(var1);
            }
         }
      }

   }

   public Buffer readBuffer(int var1) throws SAXException, IOException, InterruptedException {
      Object var2 = ((ProducerConsumerQueue)this.qBuffers.get(var1)).get();
      if (var2 instanceof Buffer) {
         return (Buffer)var2;
      } else if (!(var2 instanceof SAXException)) {
         if (var2 instanceof IOException) {
            throw (IOException)var2;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Unknown object in queue: ");
            var3.append(var2);
            throw new RuntimeException(var3.toString());
         }
      } else {
         throw (SAXException)var2;
      }
   }

   public Format[] readTracksInfo() throws SAXException, IOException, InterruptedException {
      Object var1 = this.qMeta.get();
      if (var1 instanceof Format[]) {
         return (Format[])((Format[])var1);
      } else if (!(var1 instanceof SAXException)) {
         if (var1 instanceof IOException) {
            throw (IOException)var1;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Unknown object in queue: ");
            var2.append(var1);
            throw new RuntimeException(var2.toString());
         }
      } else {
         throw (SAXException)var1;
      }
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      SAXException var39;
      label116: {
         Exception var10000;
         label118: {
            boolean var10001;
            label114: {
               try {
                  if (!var2.equals("XmlMovie")) {
                     break label114;
                  }

                  if (var4.getValue(var4.getIndex("version")).equals("1.0")) {
                     return;
                  }
               } catch (SAXException var33) {
                  var39 = var33;
                  var10001 = false;
                  break label116;
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label118;
               }

               try {
                  throw new SAXException("Expection XmlMovie version 1.0");
               } catch (SAXException var13) {
                  var39 = var13;
                  var10001 = false;
                  break label116;
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label118;
               }
            }

            boolean var6;
            try {
               var6 = var2.equals("Track");
            } catch (SAXException var31) {
               var39 = var31;
               var10001 = false;
               break label116;
            } catch (Exception var32) {
               var10000 = var32;
               var10001 = false;
               break label118;
            }

            int var5;
            Format var35;
            if (var6) {
               try {
                  var5 = getIntAttr(var4, "index");
                  var35 = FormatArgUtils.parse(getStringAttr(var4, "format"));
                  this.formatsMap.put(var5, var35);
                  this.qBuffers.put(var5, new ProducerConsumerQueue());
                  return;
               } catch (SAXException var15) {
                  var39 = var15;
                  var10001 = false;
                  break label116;
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
               }
            } else {
               label104: {
                  label103: {
                     label102: {
                        label120: {
                           long var7;
                           long var9;
                           long var11;
                           try {
                              if (!var2.equals("Buffer")) {
                                 break label120;
                              }

                              this.currentTrack = getIntAttr(var4, "track");
                              var7 = getLongAttr(var4, "sequenceNumber", 9223372036854775806L);
                              var9 = getLongAttr(var4, "timeStamp");
                              var11 = getLongAttr(var4, "duration", -1L);
                              var5 = getIntAttr(var4, "flags", 0);
                              var1 = getStringAttr(var4, "format", (String)null);
                           } catch (SAXException var29) {
                              var39 = var29;
                              var10001 = false;
                              break label116;
                           } catch (Exception var30) {
                              var10000 = var30;
                              var10001 = false;
                              break label104;
                           }

                           if (var1 == null) {
                              try {
                                 var35 = (Format)this.formatsMap.get(this.currentTrack);
                              } catch (SAXException var25) {
                                 var39 = var25;
                                 var10001 = false;
                                 break label116;
                              } catch (Exception var26) {
                                 var10000 = var26;
                                 var10001 = false;
                                 break label104;
                              }
                           } else {
                              try {
                                 var35 = FormatArgUtils.parse(var1);
                              } catch (SAXException var23) {
                                 var39 = var23;
                                 var10001 = false;
                                 break label116;
                              } catch (Exception var24) {
                                 var10000 = var24;
                                 var10001 = false;
                                 break label104;
                              }
                           }

                           try {
                              Buffer var36 = new Buffer();
                              var36.setSequenceNumber(var7);
                              var36.setTimeStamp(var9);
                              var36.setDuration(var11);
                              var36.setFlags(var5);
                              var36.setFormat(var35);
                              this.currentBuffer = var36;
                              this.currentDataChars = new StringBuilder();
                              this.state = 11;
                              break label102;
                           } catch (SAXException var21) {
                              var39 = var21;
                              var10001 = false;
                              break label116;
                           } catch (Exception var22) {
                              var10000 = var22;
                              var10001 = false;
                              break label104;
                           }
                        }

                        try {
                           if (var2.equals("Data")) {
                              if (this.state == 11) {
                                 this.state = 12;
                                 return;
                              }
                              break label103;
                           }
                        } catch (SAXException var27) {
                           var39 = var27;
                           var10001 = false;
                           break label116;
                        } catch (Exception var28) {
                           var10000 = var28;
                           var10001 = false;
                           break label104;
                        }
                     }

                     try {
                        return;
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label104;
                     } catch (SAXException var20) {
                        var39 = var20;
                        var10001 = false;
                        break label116;
                     }
                  }

                  try {
                     throw new SAXException("Not expecting Data element");
                  } catch (SAXException var17) {
                     var39 = var17;
                     var10001 = false;
                     break label116;
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                  }
               }
            }
         }

         Exception var37 = var10000;
         throw new SAXException(var37);
      }

      SAXException var38 = var39;
      throw var38;
   }
}
