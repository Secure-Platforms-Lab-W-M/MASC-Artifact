package javax.jmdns.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSLabel;
import javax.jmdns.impl.constants.DNSOptionCode;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.constants.DNSResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DNSIncoming extends DNSMessage {
   public static boolean USE_DOMAIN_NAME_FORMAT_FOR_SRV_TARGET = true;
   private static final char[] _nibbleToHex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   private static Logger logger = LoggerFactory.getLogger(DNSIncoming.class.getName());
   private final DNSIncoming.MessageInputStream _messageInputStream;
   private final DatagramPacket _packet;
   private final long _receivedTime;
   private int _senderUDPPayload;

   private DNSIncoming(int var1, int var2, boolean var3, DatagramPacket var4, long var5) {
      super(var1, var2, var3);
      this._packet = var4;
      this._messageInputStream = new DNSIncoming.MessageInputStream(var4.getData(), var4.getLength());
      this._receivedTime = var5;
   }

   public DNSIncoming(DatagramPacket var1) throws IOException {
      boolean var7;
      if (var1.getPort() == DNSConstants.MDNS_PORT) {
         var7 = true;
      } else {
         var7 = false;
      }

      super(0, 0, var7);
      this._packet = var1;
      InetAddress var8 = var1.getAddress();
      this._messageInputStream = new DNSIncoming.MessageInputStream(var1.getData(), var1.getLength());
      this._receivedTime = System.currentTimeMillis();
      this._senderUDPPayload = 1460;

      Exception var10000;
      label144: {
         int var3;
         int var4;
         int var5;
         int var6;
         boolean var10001;
         label139: {
            label138: {
               try {
                  this.setId(this._messageInputStream.readUnsignedShort());
                  this.setFlags(this._messageInputStream.readUnsignedShort());
                  if (this.getOperationCode() <= 0) {
                     var6 = this._messageInputStream.readUnsignedShort();
                     var5 = this._messageInputStream.readUnsignedShort();
                     var4 = this._messageInputStream.readUnsignedShort();
                     var3 = this._messageInputStream.readUnsignedShort();
                     logger.debug("DNSIncoming() questions:{} answers:{} authorities:{} additionals:{}", new Object[]{var6, var5, var4, var3});
                     if (var6 * 5 + (var5 + var4 + var3) * 11 <= var1.getLength()) {
                        break label139;
                     }
                     break label138;
                  }
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label144;
               }

               try {
                  throw new IOException("Received a message with a non standard operation code. Currently unsupported in the specification.");
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label144;
               }
            }

            try {
               StringBuilder var22 = new StringBuilder();
               var22.append("questions:");
               var22.append(var6);
               var22.append(" answers:");
               var22.append(var5);
               var22.append(" authorities:");
               var22.append(var4);
               var22.append(" additionals:");
               var22.append(var3);
               throw new IOException(var22.toString());
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label144;
            }
         }

         int var2;
         if (var6 > 0) {
            for(var2 = 0; var2 < var6; ++var2) {
               try {
                  this._questions.add(this.readQuestion());
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label144;
               }
            }
         }

         DNSRecord var23;
         if (var5 > 0) {
            for(var2 = 0; var2 < var5; ++var2) {
               try {
                  var23 = this.readAnswer(var8);
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label144;
               }

               if (var23 != null) {
                  try {
                     this._answers.add(var23);
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label144;
                  }
               }
            }
         }

         if (var4 > 0) {
            for(var2 = 0; var2 < var4; ++var2) {
               try {
                  var23 = this.readAnswer(var8);
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label144;
               }

               if (var23 != null) {
                  try {
                     this._authoritativeAnswers.add(var23);
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label144;
                  }
               }
            }
         }

         if (var3 > 0) {
            for(var2 = 0; var2 < var3; ++var2) {
               try {
                  var23 = this.readAnswer(var8);
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label144;
               }

               if (var23 != null) {
                  try {
                     this._additionals.add(var23);
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label144;
                  }
               }
            }
         }

         try {
            if (this._messageInputStream.available() <= 0) {
               return;
            }
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label144;
         }

         try {
            throw new IOException("Received a message with the wrong length.");
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      }

      Exception var24 = var10000;
      Logger var25 = logger;
      StringBuilder var9 = new StringBuilder();
      var9.append("DNSIncoming() dump ");
      var9.append(this.print(true));
      var9.append("\n exception ");
      var25.warn(var9.toString(), var24);
      IOException var26 = new IOException("DNSIncoming corrupted message");
      var26.initCause(var24);
      throw var26;
   }

   private String _hexString(byte[] var1) {
      StringBuilder var4 = new StringBuilder(var1.length * 2);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         int var3 = var1[var2] & 255;
         var4.append(_nibbleToHex[var3 / 16]);
         var4.append(_nibbleToHex[var3 % 16]);
      }

      return var4.toString();
   }

   private DNSRecord readAnswer(InetAddress var1) {
      String var12 = this._messageInputStream.readName();
      DNSRecordType var11 = DNSRecordType.typeForIndex(this._messageInputStream.readUnsignedShort());
      if (var11 == DNSRecordType.TYPE_IGNORE) {
         logger.warn("Could not find record type. domain: {}\n{}", var12, this.print(true));
      }

      int var6 = this._messageInputStream.readUnsignedShort();
      DNSRecordClass var10;
      if (var11 == DNSRecordType.TYPE_OPT) {
         var10 = DNSRecordClass.CLASS_UNKNOWN;
      } else {
         var10 = DNSRecordClass.classForIndex(var6);
      }

      if (var10 == DNSRecordClass.CLASS_UNKNOWN && var11 != DNSRecordType.TYPE_OPT) {
         logger.warn("Could not find record class. domain: {} type: {}\n{}", new Object[]{var12, var11, this.print(true)});
      }

      Object var34;
      label186: {
         boolean var9 = var10.isUnique(var6);
         int var5 = this._messageInputStream.readInt();
         int var7 = this._messageInputStream.readUnsignedShort();
         int var8 = null.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType[var11.ordinal()];
         String var14 = "";
         String var35;
         label185:
         switch(var8) {
         case 1:
            var34 = new DNSRecord.IPv4Address(var12, var10, var9, var5, this._messageInputStream.readBytes(var7));
            break label186;
         case 2:
            var34 = new DNSRecord.IPv6Address(var12, var10, var9, var5, this._messageInputStream.readBytes(var7));
            break label186;
         case 3:
         case 4:
            var35 = this._messageInputStream.readName();
            if (var35.length() > 0) {
               var34 = new DNSRecord.Pointer(var12, var10, var9, var5, var35);
               break label186;
            }

            logger.warn("PTR record of class: {}, there was a problem reading the service name of the answer for domain: {}", var10, var12);
            break;
         case 5:
            var34 = new DNSRecord.Text(var12, var10, var9, var5, this._messageInputStream.readBytes(var7));
            break label186;
         case 6:
            var6 = this._messageInputStream.readUnsignedShort();
            var7 = this._messageInputStream.readUnsignedShort();
            var8 = this._messageInputStream.readUnsignedShort();
            if (USE_DOMAIN_NAME_FORMAT_FOR_SRV_TARGET) {
               var35 = this._messageInputStream.readName();
            } else {
               var35 = this._messageInputStream.readNonNameString();
            }

            var34 = new DNSRecord.Service(var12, var10, var9, var5, var6, var7, var8, var35);
            break label186;
         case 7:
            StringBuilder var38 = new StringBuilder();
            var38.append(this._messageInputStream.readUTF(var7));
            var6 = var38.indexOf(" ");
            if (var6 > 0) {
               var35 = var38.substring(0, var6);
            } else {
               var35 = var38.toString();
            }

            var14 = var35.trim();
            if (var6 > 0) {
               var35 = var38.substring(var6 + 1);
            } else {
               var35 = "";
            }

            var34 = new DNSRecord.HostInformation(var12, var10, var9, var5, var14, var35.trim());
            break label186;
         case 8:
            DNSResultCode var29 = DNSResultCode.resultCodeForFlags(this.getFlags(), var5);
            var5 = (16711680 & var5) >> 16;
            if (var5 == 0) {
               this._senderUDPPayload = var6;

               while(true) {
                  if (this._messageInputStream.available() <= 0) {
                     break label185;
                  }

                  if (this._messageInputStream.available() < 2) {
                     logger.warn("There was a problem reading the OPT record. Ignoring.");
                     break label185;
                  }

                  var5 = this._messageInputStream.readUnsignedShort();
                  DNSOptionCode var30 = DNSOptionCode.resultCodeForFlags(var5);
                  if (this._messageInputStream.available() < 2) {
                     logger.warn("There was a problem reading the OPT record. Ignoring.");
                     break label185;
                  }

                  var6 = this._messageInputStream.readUnsignedShort();
                  byte[] var18;
                  if (this._messageInputStream.available() >= var6) {
                     var18 = this._messageInputStream.readBytes(var6);
                  } else {
                     var18 = new byte[0];
                  }

                  var6 = null.$SwitchMap$javax$jmdns$impl$constants$DNSOptionCode[var30.ordinal()];
                  if (var6 != 1) {
                     if (var6 != 2 && var6 != 3 && var6 != 4) {
                        if (var6 == 5) {
                           if (var5 >= 65001 && var5 <= 65534) {
                              logger.debug("There was an OPT answer using an experimental/local option code: {} data: {}", var5, this._hexString(var18));
                           } else {
                              logger.warn("There was an OPT answer. Not currently handled. Option code: {} data: {}", var5, this._hexString(var18));
                           }
                        }
                     } else if (logger.isDebugEnabled()) {
                        logger.debug("There was an OPT answer. Option code: {} data: {}", var30, this._hexString(var18));
                     }
                  } else {
                     String var32 = var14;
                     byte[] var16 = null;
                     byte[] var33 = null;
                     Object var19 = null;
                     Object var20 = null;
                     byte var27 = var18[0];
                     byte var28 = var18[1];
                     byte[] var17 = (byte[])var19;

                     byte[] var13;
                     byte[] var15;
                     byte[] var31;
                     label175: {
                        label174: {
                           label196: {
                              boolean var10001;
                              try {
                                 var15 = new byte[]{var18[2], var18[3], var18[4], var18[5], var18[6], var18[7]};
                              } catch (Exception var26) {
                                 var10001 = false;
                                 break label196;
                              }

                              var31 = var15;
                              var16 = var15;
                              var33 = var15;
                              var17 = (byte[])var19;

                              label197: {
                                 try {
                                    if (var18.length <= 8) {
                                       break label197;
                                    }
                                 } catch (Exception var25) {
                                    var10001 = false;
                                    break label196;
                                 }

                                 var16 = var15;
                                 var33 = var15;
                                 var17 = (byte[])var19;

                                 try {
                                    var31 = new byte[]{var18[8], var18[9], var18[10], var18[11], var18[12], var18[13]};
                                 } catch (Exception var24) {
                                    var10001 = false;
                                    break label196;
                                 }
                              }

                              var13 = (byte[])var20;
                              var16 = var15;
                              var33 = var31;
                              var17 = (byte[])var19;

                              byte var2;
                              byte var3;
                              label198: {
                                 try {
                                    if (var18.length != 18) {
                                       break label198;
                                    }
                                 } catch (Exception var23) {
                                    var10001 = false;
                                    break label196;
                                 }

                                 var2 = var18[14];
                                 var3 = var18[15];
                                 byte var4 = var18[16];
                                 var16 = var15;
                                 var33 = var31;
                                 var17 = (byte[])var19;

                                 try {
                                    var13 = new byte[]{var2, var3, var4, var18[17]};
                                 } catch (Exception var22) {
                                    var10001 = false;
                                    break label196;
                                 }
                              }

                              var16 = var15;
                              var33 = var31;
                              var17 = var13;

                              try {
                                 if (var18.length != 22) {
                                    break label174;
                                 }
                              } catch (Exception var21) {
                                 var10001 = false;
                                 break label196;
                              }

                              var2 = var18[14];
                              var3 = var18[15];
                              var13 = new byte[]{var2, var3, var18[16], var18[17], var18[18], var18[19], var18[20], var18[21]};
                              break label174;
                           }

                           logger.warn("Malformed OPT answer. Option code: Owner data: {}", this._hexString(var18));
                           var31 = var17;
                           var15 = var16;
                           var13 = var33;
                           break label175;
                        }

                        var33 = var13;
                        var13 = var31;
                        var31 = var33;
                     }

                     if (logger.isDebugEnabled()) {
                        Logger var39 = logger;
                        String var40 = this._hexString(var15);
                        if (var13 != var15) {
                           var12 = " wakeup MAC address: ";
                        } else {
                           var12 = var14;
                        }

                        String var36;
                        if (var13 != var15) {
                           var36 = this._hexString(var13);
                        } else {
                           var36 = var14;
                        }

                        String var37;
                        if (var31 != null) {
                           var37 = " password: ";
                        } else {
                           var37 = var14;
                        }

                        if (var31 != null) {
                           var32 = this._hexString(var31);
                        }

                        var39.debug("Unhandled Owner OPT version: {} sequence: {} MAC address: {} {}{} {}{}", new Object[]{Integer.valueOf(var27), Integer.valueOf(var28), var40, var12, var36, var37, var32});
                     }
                  }
               }
            } else {
               logger.warn("There was an OPT answer. Wrong version number: {} result code: {}", var5, var29);
               break;
            }
         default:
            logger.debug("DNSIncoming() unknown type: {}", var11);
            this._messageInputStream.skip((long)var7);
         }

         var34 = null;
      }

      if (var34 != null) {
         ((DNSRecord)var34).setRecordSource(var1);
         return (DNSRecord)var34;
      } else {
         return (DNSRecord)var34;
      }
   }

   private DNSQuestion readQuestion() {
      String var2 = this._messageInputStream.readName();
      DNSRecordType var3 = DNSRecordType.typeForIndex(this._messageInputStream.readUnsignedShort());
      if (var3 == DNSRecordType.TYPE_IGNORE) {
         logger.warn("Could not find record type: {}", this.print(true));
      }

      int var1 = this._messageInputStream.readUnsignedShort();
      DNSRecordClass var4 = DNSRecordClass.classForIndex(var1);
      return DNSQuestion.newQuestion(var2, var3, var4, var4.isUnique(var1));
   }

   void append(DNSIncoming var1) {
      if (this.isQuery() && this.isTruncated() && var1.isQuery()) {
         this._questions.addAll(var1.getQuestions());
         this._answers.addAll(var1.getAnswers());
         this._authoritativeAnswers.addAll(var1.getAuthorities());
         this._additionals.addAll(var1.getAdditionals());
      } else {
         throw new IllegalArgumentException();
      }
   }

   public DNSIncoming clone() {
      DNSIncoming var1 = new DNSIncoming(this.getFlags(), this.getId(), this.isMulticast(), this._packet, this._receivedTime);
      var1._senderUDPPayload = this._senderUDPPayload;
      var1._questions.addAll(this._questions);
      var1._answers.addAll(this._answers);
      var1._authoritativeAnswers.addAll(this._authoritativeAnswers);
      var1._additionals.addAll(this._additionals);
      return var1;
   }

   public int elapseSinceArrival() {
      return (int)(System.currentTimeMillis() - this._receivedTime);
   }

   public int getSenderUDPPayload() {
      return this._senderUDPPayload;
   }

   String print(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.print());
      if (var1) {
         byte[] var3 = new byte[this._packet.getLength()];
         System.arraycopy(this._packet.getData(), 0, var3, 0, var3.length);
         var2.append(this.print(var3));
      }

      return var2.toString();
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      String var1;
      if (this.isQuery()) {
         var1 = "dns[query,";
      } else {
         var1 = "dns[response,";
      }

      var2.append(var1);
      if (this._packet.getAddress() != null) {
         var2.append(this._packet.getAddress().getHostAddress());
      }

      var2.append(':');
      var2.append(this._packet.getPort());
      var2.append(", length=");
      var2.append(this._packet.getLength());
      var2.append(", id=0x");
      var2.append(Integer.toHexString(this.getId()));
      if (this.getFlags() != 0) {
         var2.append(", flags=0x");
         var2.append(Integer.toHexString(this.getFlags()));
         if ((this.getFlags() & '耀') != 0) {
            var2.append(":r");
         }

         if ((this.getFlags() & 1024) != 0) {
            var2.append(":aa");
         }

         if ((this.getFlags() & 512) != 0) {
            var2.append(":tc");
         }
      }

      if (this.getNumberOfQuestions() > 0) {
         var2.append(", questions=");
         var2.append(this.getNumberOfQuestions());
      }

      if (this.getNumberOfAnswers() > 0) {
         var2.append(", answers=");
         var2.append(this.getNumberOfAnswers());
      }

      if (this.getNumberOfAuthorities() > 0) {
         var2.append(", authorities=");
         var2.append(this.getNumberOfAuthorities());
      }

      if (this.getNumberOfAdditionals() > 0) {
         var2.append(", additionals=");
         var2.append(this.getNumberOfAdditionals());
      }

      Iterator var4;
      if (this.getNumberOfQuestions() > 0) {
         var2.append("\nquestions:");
         var4 = this._questions.iterator();

         while(var4.hasNext()) {
            DNSQuestion var3 = (DNSQuestion)var4.next();
            var2.append("\n\t");
            var2.append(var3);
         }
      }

      DNSRecord var5;
      if (this.getNumberOfAnswers() > 0) {
         var2.append("\nanswers:");
         var4 = this._answers.iterator();

         while(var4.hasNext()) {
            var5 = (DNSRecord)var4.next();
            var2.append("\n\t");
            var2.append(var5);
         }
      }

      if (this.getNumberOfAuthorities() > 0) {
         var2.append("\nauthorities:");
         var4 = this._authoritativeAnswers.iterator();

         while(var4.hasNext()) {
            var5 = (DNSRecord)var4.next();
            var2.append("\n\t");
            var2.append(var5);
         }
      }

      if (this.getNumberOfAdditionals() > 0) {
         var2.append("\nadditionals:");
         var4 = this._additionals.iterator();

         while(var4.hasNext()) {
            var5 = (DNSRecord)var4.next();
            var2.append("\n\t");
            var2.append(var5);
         }
      }

      var2.append(']');
      return var2.toString();
   }

   public static class MessageInputStream extends ByteArrayInputStream {
      private static Logger logger1 = LoggerFactory.getLogger(DNSIncoming.MessageInputStream.class.getName());
      final Map _names;

      public MessageInputStream(byte[] var1, int var2) {
         this(var1, 0, var2);
      }

      public MessageInputStream(byte[] var1, int var2, int var3) {
         super(var1, var2, var3);
         this._names = new HashMap();
      }

      protected int peek() {
         synchronized(this){}
         boolean var4 = false;

         byte var1;
         int var6;
         label50: {
            try {
               var4 = true;
               if (this.pos < this.count) {
                  var1 = this.buf[this.pos];
                  var4 = false;
                  break label50;
               }

               var4 = false;
            } finally {
               if (var4) {
                  ;
               }
            }

            var6 = -1;
            return var6;
         }

         var6 = var1 & true;
         return var6;
      }

      public int readByte() {
         return this.read();
      }

      public byte[] readBytes(int var1) {
         byte[] var2 = new byte[var1];
         this.read(var2, 0, var1);
         return var2;
      }

      public int readInt() {
         return this.readUnsignedShort() << 16 | this.readUnsignedShort();
      }

      public String readName() {
         HashMap var7 = new HashMap();
         StringBuilder var6 = new StringBuilder();
         boolean var1 = false;

         while(!var1) {
            int var2 = this.readUnsignedByte();
            if (var2 == 0) {
               break;
            }

            int var3 = null.$SwitchMap$javax$jmdns$impl$constants$DNSLabel[DNSLabel.labelForByte(var2).ordinal()];
            Iterator var5;
            String var9;
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 3) {
                     logger1.warn("Unsupported DNS label type: '{}'", Integer.toHexString(var2 & 192));
                  } else {
                     logger1.debug("Extended label are not currently supported.");
                  }
               } else {
                  int var8 = DNSLabel.labelValue(var2) << 8 | this.readUnsignedByte();
                  String var10 = (String)this._names.get(var8);
                  var9 = var10;
                  if (var10 == null) {
                     logger1.warn("Bad domain name: possible circular name detected. Bad offset: 0x{} at 0x{}", Integer.toHexString(var8), Integer.toHexString(this.pos - 2));
                     var9 = "";
                  }

                  var6.append(var9);
                  var5 = var7.values().iterator();

                  while(var5.hasNext()) {
                     ((StringBuilder)var5.next()).append(var9);
                  }

                  var1 = true;
               }
            } else {
               var3 = this.pos;
               StringBuilder var4 = new StringBuilder();
               var4.append(this.readUTF(var2));
               var4.append(".");
               var9 = var4.toString();
               var6.append(var9);
               var5 = var7.values().iterator();

               while(var5.hasNext()) {
                  ((StringBuilder)var5.next()).append(var9);
               }

               var7.put(var3 - 1, new StringBuilder(var9));
            }
         }

         Iterator var11 = var7.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Integer var13 = (Integer)var12.getKey();
            this._names.put(var13, ((StringBuilder)var12.getValue()).toString());
         }

         return var6.toString();
      }

      public String readNonNameString() {
         return this.readUTF(this.readUnsignedByte());
      }

      public String readUTF(int var1) {
         StringBuilder var4 = new StringBuilder(var1);

         for(int var2 = 0; var2 < var1; ++var2) {
            int var3 = this.readUnsignedByte();
            switch(var3 >> 4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
               break;
            case 8:
            case 9:
            case 10:
            case 11:
            default:
               var3 = (var3 & 63) << 4 | this.readUnsignedByte() & 15;
               ++var2;
               break;
            case 12:
            case 13:
               var3 = (var3 & 31) << 6 | this.readUnsignedByte() & 63;
               ++var2;
               break;
            case 14:
               var3 = (var3 & 15) << 12 | (this.readUnsignedByte() & 63) << 6 | this.readUnsignedByte() & 63;
               var2 = var2 + 1 + 1;
            }

            var4.append((char)var3);
         }

         return var4.toString();
      }

      public int readUnsignedByte() {
         return this.read() & 255;
      }

      public int readUnsignedShort() {
         return this.readUnsignedByte() << 8 | this.readUnsignedByte();
      }
   }
}
