package net.sf.fmj.media.rtp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.sf.fmj.media.rtp.util.BadFormatException;
import net.sf.fmj.media.rtp.util.BadVersionException;
import net.sf.fmj.media.rtp.util.Packet;

public class RTCPPacketParser {
   private final List listeners = new ArrayList();

   private void onEnterSenderReport() {
      List var1 = this.listeners;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var2;
         try {
            var2 = this.listeners.iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  ((RTCPPacketParserListener)var2.next()).enterSenderReport();
                  continue;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private void onMalformedEndOfParticipation() {
      List var1 = this.listeners;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var2;
         try {
            var2 = this.listeners.iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  ((RTCPPacketParserListener)var2.next()).malformedEndOfParticipation();
                  continue;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private void onMalformedReceiverReport() {
      List var1 = this.listeners;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var2;
         try {
            var2 = this.listeners.iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  ((RTCPPacketParserListener)var2.next()).malformedReceiverReport();
                  continue;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private void onMalformedSenderReport() {
      List var1 = this.listeners;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var2;
         try {
            var2 = this.listeners.iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  ((RTCPPacketParserListener)var2.next()).malformedSenderReport();
                  continue;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private void onMalformedSourceDescription() {
      List var1 = this.listeners;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var2;
         try {
            var2 = this.listeners.iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  ((RTCPPacketParserListener)var2.next()).malformedSourceDescription();
                  continue;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private void onPayloadUknownType() {
      List var1 = this.listeners;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var2;
         try {
            var2 = this.listeners.iterator();
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var2.hasNext()) {
                  ((RTCPPacketParserListener)var2.next()).uknownPayloadType();
                  continue;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   private void onVisitSenderReport(RTCPSRPacket var1) {
      List var2 = this.listeners;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var3;
         try {
            var3 = this.listeners.iterator();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var3.hasNext()) {
                  ((RTCPPacketParserListener)var3.next()).visitSendeReport(var1);
                  continue;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   private void readRTCPReportBlock(DataInputStream var1, RTCPReportBlock[] var2) throws IOException {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         RTCPReportBlock var6 = new RTCPReportBlock();
         var2[var3] = var6;
         var6.ssrc = var1.readInt();
         long var4 = (long)var1.readInt() & 4294967295L;
         var6.fractionlost = (int)(var4 >> 24);
         var6.packetslost = (int)(16777215L & var4);
         var6.lastseq = (long)var1.readInt() & 4294967295L;
         var6.jitter = var1.readInt();
         var6.lsr = (long)var1.readInt() & 4294967295L;
         var6.dlsr = 4294967295L & (long)var1.readInt();
      }

   }

   public void addRTCPPacketParserListener(RTCPPacketParserListener var1) {
      if (var1 != null) {
         List var2 = this.listeners;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (!this.listeners.contains(var1)) {
                  this.listeners.add(var1);
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               return;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label133;
            }
         }

         while(true) {
            Throwable var15 = var10000;

            try {
               throw var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      } else {
         throw new NullPointerException("listener");
      }
   }

   protected RTCPPacket parse(RTCPCompoundPacket var1, int var2, int var3, int var4, DataInputStream var5) throws BadFormatException, IOException {
      this.onPayloadUknownType();
      throw new BadFormatException("Unknown payload type");
   }

   public RTCPPacket parse(Packet var1) throws BadFormatException {
      String var10 = "Failed to parse an RTCP packet: ";
      RTCPCompoundPacket var15 = new RTCPCompoundPacket(var1);
      Vector var16 = new Vector(2);
      DataInputStream var17 = new DataInputStream(new ByteArrayInputStream(var15.data, var15.offset, var15.length));
      int var4 = 0;
      String var200 = var10;

      while(true) {
         RTCPPacketParser var13 = this;
         var10 = var200;
         String var11 = var200;

         int var2;
         label954: {
            label993: {
               IOException var208;
               IOException var10000;
               label994: {
                  label950: {
                     EOFException var205;
                     label949: {
                        EOFException var209;
                        label985: {
                           boolean var10001;
                           try {
                              if (var4 >= var15.length) {
                                 break label993;
                              }
                           } catch (EOFException var148) {
                              var209 = var148;
                              var10001 = false;
                              break label985;
                           } catch (IOException var149) {
                              var10000 = var149;
                              var10001 = false;
                              break label950;
                           }

                           var10 = var200;
                           var11 = var200;

                           int var3;
                           try {
                              var3 = var17.readUnsignedByte();
                           } catch (EOFException var146) {
                              var209 = var146;
                              var10001 = false;
                              break label985;
                           } catch (IOException var147) {
                              var10000 = var147;
                              var10001 = false;
                              break label950;
                           }

                           if ((var3 & 192) == 128) {
                              label988: {
                                 var10 = var200;
                                 var11 = var200;

                                 int var7;
                                 try {
                                    var7 = var17.readUnsignedByte();
                                 } catch (EOFException var144) {
                                    var209 = var144;
                                    var10001 = false;
                                    break label985;
                                 } catch (IOException var145) {
                                    var10000 = var145;
                                    var10001 = false;
                                    break label950;
                                 }

                                 var10 = var200;
                                 var11 = var200;

                                 try {
                                    var2 = var17.readUnsignedShort() + 1 << 2;
                                 } catch (EOFException var142) {
                                    var209 = var142;
                                    var10001 = false;
                                    break label985;
                                 } catch (IOException var143) {
                                    var10000 = var143;
                                    var10001 = false;
                                    break label950;
                                 }

                                 var10 = var200;
                                 var11 = var200;

                                 label963: {
                                    try {
                                       if (var4 + var2 <= var15.length) {
                                          break label963;
                                       }
                                    } catch (EOFException var198) {
                                       var209 = var198;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var199) {
                                       var10000 = var199;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var200 = var200;

                                    try {
                                       throw new BadFormatException("Packet length less than actual packet length");
                                    } catch (EOFException var36) {
                                       var209 = var36;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var37) {
                                       var10000 = var37;
                                       var10001 = false;
                                       break label994;
                                    }
                                 }

                                 var10 = var200;
                                 var11 = var200;

                                 int var5;
                                 label935: {
                                    label934: {
                                       label933: {
                                          try {
                                             if (var4 + var2 == var15.length) {
                                                break label933;
                                             }
                                          } catch (EOFException var196) {
                                             var209 = var196;
                                             var10001 = false;
                                             break label985;
                                          } catch (IOException var197) {
                                             var10000 = var197;
                                             var10001 = false;
                                             break label950;
                                          }

                                          if ((var3 & 32) != 0) {
                                             var10 = var200;
                                             var200 = var200;

                                             try {
                                                throw new BadFormatException("No padding found.");
                                             } catch (EOFException var34) {
                                                var209 = var34;
                                                var10001 = false;
                                                break label988;
                                             } catch (IOException var35) {
                                                var10000 = var35;
                                                var10001 = false;
                                                break label994;
                                             }
                                          }
                                          break label934;
                                       }

                                       if ((var3 & 32) != 0) {
                                          var10 = var200;
                                          var11 = var200;

                                          try {
                                             var5 = var15.data[var15.offset + var15.length - 1] & 255;
                                          } catch (EOFException var140) {
                                             var209 = var140;
                                             var10001 = false;
                                             break label985;
                                          } catch (IOException var141) {
                                             var10000 = var141;
                                             var10001 = false;
                                             break label950;
                                          }

                                          if (var5 == 0) {
                                             var10 = var200;
                                             var11 = var200;

                                             try {
                                                throw new BadFormatException();
                                             } catch (EOFException var42) {
                                                var209 = var42;
                                                var10001 = false;
                                                break label985;
                                             } catch (IOException var43) {
                                                var10000 = var43;
                                                var10001 = false;
                                                break label950;
                                             }
                                          }
                                          break label935;
                                       }
                                    }

                                    var5 = 0;
                                 }

                                 Object var202;
                                 int var8 = var2 - var5;
                                 int var6 = var3 & 31;
                                 label909:
                                 switch(var7) {
                                 case 200:
                                    var11 = var200;
                                    var200 = var200;
                                    var10 = var11;

                                    try {
                                       this.onEnterSenderReport();
                                    } catch (EOFException var180) {
                                       var209 = var180;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var181) {
                                       var10000 = var181;
                                       var10001 = false;
                                       break label994;
                                    }

                                    if (var8 != var6 * 24 + 28) {
                                       var200 = var11;
                                       var10 = var11;

                                       try {
                                          this.onMalformedSenderReport();
                                       } catch (EOFException var68) {
                                          var209 = var68;
                                          var10001 = false;
                                          break label988;
                                       } catch (IOException var69) {
                                          var10000 = var69;
                                          var10001 = false;
                                          break label994;
                                       }

                                       var200 = var11;
                                       var10 = var11;

                                       try {
                                          System.out.println("bad format.");
                                       } catch (EOFException var66) {
                                          var209 = var66;
                                          var10001 = false;
                                          break label988;
                                       } catch (IOException var67) {
                                          var10000 = var67;
                                          var10001 = false;
                                          break label994;
                                       }

                                       var200 = var11;
                                       var10 = var11;

                                       try {
                                          throw new BadFormatException("inlength != 28 + 24 * firstbyte");
                                       } catch (EOFException var28) {
                                          var209 = var28;
                                          var10001 = false;
                                          break label988;
                                       } catch (IOException var29) {
                                          var10000 = var29;
                                          var10001 = false;
                                          break label994;
                                       }
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    RTCPSRPacket var204;
                                    try {
                                       var204 = new RTCPSRPacket(var15);
                                    } catch (EOFException var178) {
                                       var209 = var178;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var179) {
                                       var10000 = var179;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var202 = var204;
                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var204.ssrc = var17.readInt();
                                    } catch (EOFException var176) {
                                       var209 = var176;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var177) {
                                       var10000 = var177;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var204.ntptimestampmsw = (long)var17.readInt() & 4294967295L;
                                    } catch (EOFException var174) {
                                       var209 = var174;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var175) {
                                       var10000 = var175;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var204.ntptimestamplsw = (long)var17.readInt() & 4294967295L;
                                    } catch (EOFException var172) {
                                       var209 = var172;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var173) {
                                       var10000 = var173;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var204.rtptimestamp = (long)var17.readInt() & 4294967295L;
                                    } catch (EOFException var170) {
                                       var209 = var170;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var171) {
                                       var10000 = var171;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var204.packetcount = (long)var17.readInt() & 4294967295L;
                                    } catch (EOFException var168) {
                                       var209 = var168;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var169) {
                                       var10000 = var169;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var204.octetcount = (long)var17.readInt() & 4294967295L;
                                    } catch (EOFException var166) {
                                       var209 = var166;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var167) {
                                       var10000 = var167;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var204.reports = new RTCPReportBlock[var6];
                                    } catch (EOFException var164) {
                                       var209 = var164;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var165) {
                                       var10000 = var165;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var13.onVisitSenderReport(var204);
                                    } catch (EOFException var162) {
                                       var209 = var162;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var163) {
                                       var10000 = var163;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var13.readRTCPReportBlock(var17, var204.reports);
                                       break;
                                    } catch (EOFException var160) {
                                       var209 = var160;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var161) {
                                       var10000 = var161;
                                       var10001 = false;
                                       break label994;
                                    }
                                 case 201:
                                    var11 = var200;
                                    if (var8 != var6 * 24 + 8) {
                                       var200 = var200;
                                       var10 = var11;

                                       try {
                                          this.onMalformedReceiverReport();
                                       } catch (EOFException var52) {
                                          var209 = var52;
                                          var10001 = false;
                                          break label988;
                                       } catch (IOException var53) {
                                          var10000 = var53;
                                          var10001 = false;
                                          break label994;
                                       }

                                       var200 = var11;
                                       var10 = var11;

                                       try {
                                          throw new BadFormatException("inlength != 8 + 24 * firstbyte");
                                       } catch (EOFException var26) {
                                          var209 = var26;
                                          var10001 = false;
                                          break label988;
                                       } catch (IOException var27) {
                                          var10000 = var27;
                                          var10001 = false;
                                          break label994;
                                       }
                                    }

                                    var200 = var200;
                                    var10 = var11;

                                    RTCPRRPacket var203;
                                    try {
                                       var203 = new RTCPRRPacket(var15);
                                    } catch (EOFException var188) {
                                       var209 = var188;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var189) {
                                       var10000 = var189;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var202 = var203;
                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var203.ssrc = var17.readInt();
                                    } catch (EOFException var186) {
                                       var209 = var186;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var187) {
                                       var10000 = var187;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var203.reports = new RTCPReportBlock[var6];
                                    } catch (EOFException var184) {
                                       var209 = var184;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var185) {
                                       var10000 = var185;
                                       var10001 = false;
                                       break label994;
                                    }

                                    var200 = var11;
                                    var10 = var11;

                                    try {
                                       var13.readRTCPReportBlock(var17, var203.reports);
                                       break;
                                    } catch (EOFException var182) {
                                       var209 = var182;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var183) {
                                       var10000 = var183;
                                       var10001 = false;
                                       break label994;
                                    }
                                 case 202:
                                    var10 = var200;
                                    var11 = var200;

                                    Object var14;
                                    try {
                                       var14 = new RTCPSDESPacket(var15);
                                    } catch (EOFException var138) {
                                       var209 = var138;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var139) {
                                       var10000 = var139;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var202 = var14;
                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       ((RTCPSDESPacket)var14).sdes = new RTCPSDES[var6];
                                    } catch (EOFException var136) {
                                       var209 = var136;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var137) {
                                       var10000 = var137;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var3 = 4;
                                    var6 = 0;

                                    while(true) {
                                       var10 = var200;
                                       var11 = var200;

                                       label920: {
                                          try {
                                             if (var6 < ((RTCPSDESPacket)var14).sdes.length) {
                                                break label920;
                                             }
                                          } catch (EOFException var194) {
                                             var209 = var194;
                                             var10001 = false;
                                             break label985;
                                          } catch (IOException var195) {
                                             var10000 = var195;
                                             var10001 = false;
                                             break label950;
                                          }

                                          var11 = var200;
                                          if (var8 != var3) {
                                             var200 = var200;
                                             var10 = var11;

                                             try {
                                                this.onMalformedSourceDescription();
                                             } catch (EOFException var86) {
                                                var209 = var86;
                                                var10001 = false;
                                                break label988;
                                             } catch (IOException var87) {
                                                var10000 = var87;
                                                var10001 = false;
                                                break label994;
                                             }

                                             var200 = var11;
                                             var10 = var11;

                                             try {
                                                throw new BadFormatException("inlength != sdesoff");
                                             } catch (EOFException var24) {
                                                var209 = var24;
                                                var10001 = false;
                                                break label988;
                                             } catch (IOException var25) {
                                                var10000 = var25;
                                                var10001 = false;
                                                break label994;
                                             }
                                          }
                                          break label909;
                                       }

                                       var10 = var200;
                                       var11 = var200;

                                       RTCPSDES var18;
                                       try {
                                          var18 = new RTCPSDES();
                                       } catch (EOFException var134) {
                                          var209 = var134;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var135) {
                                          var10000 = var135;
                                          var10001 = false;
                                          break label950;
                                       }

                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          ((RTCPSDESPacket)var14).sdes[var6] = var18;
                                       } catch (EOFException var132) {
                                          var209 = var132;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var133) {
                                          var10000 = var133;
                                          var10001 = false;
                                          break label950;
                                       }

                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          var18.ssrc = var17.readInt();
                                       } catch (EOFException var130) {
                                          var209 = var130;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var131) {
                                          var10000 = var131;
                                          var10001 = false;
                                          break label950;
                                       }

                                       var3 += 5;
                                       var10 = var200;
                                       var11 = var200;

                                       Vector var19;
                                       try {
                                          var19 = new Vector();
                                       } catch (EOFException var128) {
                                          var209 = var128;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var129) {
                                          var10000 = var129;
                                          var10001 = false;
                                          break label950;
                                       }

                                       boolean var201 = false;

                                       while(true) {
                                          var10 = var200;
                                          var11 = var200;

                                          int var9;
                                          try {
                                             var9 = var17.readUnsignedByte();
                                          } catch (EOFException var126) {
                                             var209 = var126;
                                             var10001 = false;
                                             break label985;
                                          } catch (IOException var127) {
                                             var10000 = var127;
                                             var10001 = false;
                                             break label950;
                                          }

                                          if (var9 == 0) {
                                             var11 = var200;
                                             if (!var201) {
                                                var200 = var200;
                                                var10 = var11;

                                                try {
                                                   this.onMalformedSourceDescription();
                                                } catch (EOFException var70) {
                                                   var209 = var70;
                                                   var10001 = false;
                                                   break label988;
                                                } catch (IOException var71) {
                                                   var10000 = var71;
                                                   var10001 = false;
                                                   break label994;
                                                }

                                                var200 = var11;
                                                var10 = var11;

                                                try {
                                                   throw new BadFormatException("!gotcname");
                                                } catch (EOFException var22) {
                                                   var209 = var22;
                                                   var10001 = false;
                                                   break label988;
                                                } catch (IOException var23) {
                                                   var10000 = var23;
                                                   var10001 = false;
                                                   break label994;
                                                }
                                             }

                                             var200 = var200;
                                             var10 = var11;

                                             try {
                                                var18.items = new RTCPSDESItem[var19.size()];
                                             } catch (EOFException var154) {
                                                var209 = var154;
                                                var10001 = false;
                                                break label988;
                                             } catch (IOException var155) {
                                                var10000 = var155;
                                                var10001 = false;
                                                break label994;
                                             }

                                             var200 = var11;
                                             var10 = var11;

                                             try {
                                                var19.copyInto(var18.items);
                                             } catch (EOFException var152) {
                                                var209 = var152;
                                                var10001 = false;
                                                break label988;
                                             } catch (IOException var153) {
                                                var10000 = var153;
                                                var10001 = false;
                                                break label994;
                                             }

                                             if ((var3 & 3) != 0) {
                                                var200 = var11;
                                                var10 = var11;

                                                try {
                                                   var17.skip((long)(4 - (var3 & 3)));
                                                } catch (EOFException var150) {
                                                   var209 = var150;
                                                   var10001 = false;
                                                   break label988;
                                                } catch (IOException var151) {
                                                   var10000 = var151;
                                                   var10001 = false;
                                                   break label994;
                                                }

                                                var3 = var3 + 3 & -4;
                                             }

                                             ++var6;
                                             var200 = var11;
                                             var14 = var14;
                                             var202 = var202;
                                             break;
                                          }

                                          if (var9 < 1 || var9 > 8) {
                                             var11 = var200;
                                             var200 = var200;
                                             var10 = var11;

                                             try {
                                                this.onMalformedSourceDescription();
                                             } catch (EOFException var48) {
                                                var209 = var48;
                                                var10001 = false;
                                                break label988;
                                             } catch (IOException var49) {
                                                var10000 = var49;
                                                var10001 = false;
                                                break label994;
                                             }

                                             var200 = var11;
                                             var10 = var11;

                                             try {
                                                throw new BadFormatException("j < 1 || j > 8");
                                             } catch (EOFException var20) {
                                                var209 = var20;
                                                var10001 = false;
                                                break label988;
                                             } catch (IOException var21) {
                                                var10000 = var21;
                                                var10001 = false;
                                                break label994;
                                             }
                                          }

                                          if (var9 == 1) {
                                             var201 = true;
                                          }

                                          var10 = var200;
                                          var11 = var200;

                                          try {
                                             var14 = new RTCPSDESItem();
                                          } catch (EOFException var124) {
                                             var209 = var124;
                                             var10001 = false;
                                             break label985;
                                          } catch (IOException var125) {
                                             var10000 = var125;
                                             var10001 = false;
                                             break label950;
                                          }

                                          var10 = var200;
                                          var11 = var200;

                                          try {
                                             var19.addElement(var14);
                                          } catch (EOFException var122) {
                                             var209 = var122;
                                             var10001 = false;
                                             break label985;
                                          } catch (IOException var123) {
                                             var10000 = var123;
                                             var10001 = false;
                                             break label950;
                                          }

                                          var10 = var200;
                                          var11 = var200;

                                          try {
                                             ((RTCPSDESItem)var14).type = var9;
                                          } catch (EOFException var120) {
                                             var209 = var120;
                                             var10001 = false;
                                             break label985;
                                          } catch (IOException var121) {
                                             var10000 = var121;
                                             var10001 = false;
                                             break label950;
                                          }

                                          var10 = var200;
                                          var11 = var200;

                                          try {
                                             var9 = var17.readUnsignedByte();
                                          } catch (EOFException var118) {
                                             var209 = var118;
                                             var10001 = false;
                                             break label985;
                                          } catch (IOException var119) {
                                             var10000 = var119;
                                             var10001 = false;
                                             break label950;
                                          }

                                          var11 = var200;
                                          var200 = var200;
                                          var10 = var11;

                                          try {
                                             ((RTCPSDESItem)var14).data = new byte[var9];
                                          } catch (EOFException var158) {
                                             var209 = var158;
                                             var10001 = false;
                                             break label988;
                                          } catch (IOException var159) {
                                             var10000 = var159;
                                             var10001 = false;
                                             break label994;
                                          }

                                          var200 = var11;
                                          var10 = var11;

                                          try {
                                             var17.readFully(((RTCPSDESItem)var14).data);
                                          } catch (EOFException var156) {
                                             var209 = var156;
                                             var10001 = false;
                                             break label988;
                                          } catch (IOException var157) {
                                             var10000 = var157;
                                             var10001 = false;
                                             break label994;
                                          }

                                          var3 += var9 + 2;
                                          var200 = var11;
                                       }
                                    }
                                 case 203:
                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       var202 = new RTCPBYEPacket(var15);
                                    } catch (EOFException var102) {
                                       var209 = var102;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var103) {
                                       var10000 = var103;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       ((RTCPBYEPacket)var202).ssrc = new int[var6];
                                    } catch (EOFException var100) {
                                       var209 = var100;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var101) {
                                       var10000 = var101;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var3 = 0;

                                    while(true) {
                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          if (var3 >= ((RTCPBYEPacket)var202).ssrc.length) {
                                             break;
                                          }
                                       } catch (EOFException var192) {
                                          var209 = var192;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var193) {
                                          var10000 = var193;
                                          var10001 = false;
                                          break label950;
                                       }

                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          ((RTCPBYEPacket)var202).ssrc[var3] = var17.readInt();
                                       } catch (EOFException var98) {
                                          var209 = var98;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var99) {
                                          var10000 = var99;
                                          var10001 = false;
                                          break label950;
                                       }

                                       ++var3;
                                    }

                                    if (var8 > var6 * 4 + 4) {
                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          var3 = var17.readUnsignedByte();
                                       } catch (EOFException var96) {
                                          var209 = var96;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var97) {
                                          var10000 = var97;
                                          var10001 = false;
                                          break label950;
                                       }

                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          ((RTCPBYEPacket)var202).reason = new byte[var3];
                                       } catch (EOFException var94) {
                                          var209 = var94;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var95) {
                                          var10000 = var95;
                                          var10001 = false;
                                          break label950;
                                       }

                                       ++var3;
                                    } else {
                                       var3 = 0;
                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          ((RTCPBYEPacket)var202).reason = new byte[0];
                                       } catch (EOFException var92) {
                                          var209 = var92;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var93) {
                                          var10000 = var93;
                                          var10001 = false;
                                          break label950;
                                       }
                                    }

                                    var3 = var3 + 3 & -4;
                                    if (var8 != var6 * 4 + 4 + var3) {
                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          this.onMalformedEndOfParticipation();
                                       } catch (EOFException var46) {
                                          var209 = var46;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var47) {
                                          var10000 = var47;
                                          var10001 = false;
                                          break label950;
                                       }

                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          throw new BadFormatException("inlength != 4 + 4 * firstbyte + reasonlen");
                                       } catch (EOFException var40) {
                                          var209 = var40;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var41) {
                                          var10000 = var41;
                                          var10001 = false;
                                          break label950;
                                       }
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       var17.readFully(((RTCPBYEPacket)var202).reason);
                                    } catch (EOFException var90) {
                                       var209 = var90;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var91) {
                                       var10000 = var91;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       var17.skip((long)(var3 - ((RTCPBYEPacket)var202).reason.length));
                                    } catch (EOFException var88) {
                                       var209 = var88;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var89) {
                                       var10000 = var89;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var11 = var200;
                                    break;
                                 case 204:
                                    if (var8 < 12) {
                                       var10 = var200;
                                       var11 = var200;

                                       try {
                                          throw new BadFormatException("inlength < 12");
                                       } catch (EOFException var44) {
                                          var209 = var44;
                                          var10001 = false;
                                          break label985;
                                       } catch (IOException var45) {
                                          var10000 = var45;
                                          var10001 = false;
                                          break label950;
                                       }
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       var202 = new RTCPAPPPacket(var15);
                                    } catch (EOFException var116) {
                                       var209 = var116;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var117) {
                                       var10000 = var117;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       ((RTCPAPPPacket)var202).ssrc = var17.readInt();
                                    } catch (EOFException var114) {
                                       var209 = var114;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var115) {
                                       var10000 = var115;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       ((RTCPAPPPacket)var202).name = var17.readInt();
                                    } catch (EOFException var112) {
                                       var209 = var112;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var113) {
                                       var10000 = var113;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       ((RTCPAPPPacket)var202).subtype = var6;
                                    } catch (EOFException var110) {
                                       var209 = var110;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var111) {
                                       var10000 = var111;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       ((RTCPAPPPacket)var202).data = new byte[var8 - 12];
                                    } catch (EOFException var108) {
                                       var209 = var108;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var109) {
                                       var10000 = var109;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       var17.readFully(((RTCPAPPPacket)var202).data);
                                    } catch (EOFException var106) {
                                       var209 = var106;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var107) {
                                       var10000 = var107;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var10 = var200;
                                    var11 = var200;

                                    try {
                                       var17.skip((long)(var8 - 12 - ((RTCPAPPPacket)var202).data.length));
                                    } catch (EOFException var104) {
                                       var209 = var104;
                                       var10001 = false;
                                       break label985;
                                    } catch (IOException var105) {
                                       var10000 = var105;
                                       var10001 = false;
                                       break label950;
                                    }

                                    var11 = var200;
                                    break;
                                 default:
                                    var11 = var200;
                                    var200 = var200;
                                    var10 = var11;

                                    try {
                                       var202 = this.parse(var15, var3, var7, var8, var17);
                                    } catch (EOFException var190) {
                                       var209 = var190;
                                       var10001 = false;
                                       break label988;
                                    } catch (IOException var191) {
                                       var10000 = var191;
                                       var10001 = false;
                                       break label994;
                                    }

                                    if (var202 == null) {
                                       var200 = var11;
                                       var10 = var11;

                                       try {
                                          this.onPayloadUknownType();
                                       } catch (EOFException var64) {
                                          var209 = var64;
                                          var10001 = false;
                                          break label988;
                                       } catch (IOException var65) {
                                          var10000 = var65;
                                          var10001 = false;
                                          break label994;
                                       }

                                       var200 = var11;
                                       var10 = var11;

                                       try {
                                          throw new BadFormatException("p == null");
                                       } catch (EOFException var32) {
                                          var209 = var32;
                                          var10001 = false;
                                          break label988;
                                       } catch (IOException var33) {
                                          var10000 = var33;
                                          var10001 = false;
                                          break label994;
                                       }
                                    }
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    ((RTCPPacket)var202).offset = var4;
                                 } catch (EOFException var56) {
                                    var209 = var56;
                                    var10001 = false;
                                    break label988;
                                 } catch (IOException var57) {
                                    var10000 = var57;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    ((RTCPPacket)var202).length = var2;
                                 } catch (EOFException var54) {
                                    var209 = var54;
                                    var10001 = false;
                                    break label988;
                                 } catch (IOException var55) {
                                    var10000 = var55;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var16.addElement(var202);
                                 } catch (EOFException var50) {
                                    var209 = var50;
                                    var10001 = false;
                                    break label988;
                                 } catch (IOException var51) {
                                    var10000 = var51;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var17.skipBytes(var5);
                                    break label954;
                                 } catch (EOFException var30) {
                                    var209 = var30;
                                    var10001 = false;
                                 } catch (IOException var31) {
                                    var10000 = var31;
                                    var10001 = false;
                                    break label994;
                                 }
                              }
                           } else {
                              label984: {
                                 var11 = var200;
                                 var200 = var200;
                                 var10 = var11;

                                 StringBuilder var12;
                                 try {
                                    var12 = new StringBuilder();
                                 } catch (EOFException var84) {
                                    var209 = var84;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var85) {
                                    var10000 = var85;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append("version must be 2. (base.length ");
                                 } catch (EOFException var82) {
                                    var209 = var82;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var83) {
                                    var10000 = var83;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append(var15.length);
                                 } catch (EOFException var80) {
                                    var209 = var80;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var81) {
                                    var10000 = var81;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append(", base.offset ");
                                 } catch (EOFException var78) {
                                    var209 = var78;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var79) {
                                    var10000 = var79;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append(var15.offset);
                                 } catch (EOFException var76) {
                                    var209 = var76;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var77) {
                                    var10000 = var77;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append(", firstbyte 0x");
                                 } catch (EOFException var74) {
                                    var209 = var74;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var75) {
                                    var10000 = var75;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append(Integer.toHexString(var3));
                                 } catch (EOFException var72) {
                                    var209 = var72;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var73) {
                                    var10000 = var73;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append(", offset ");
                                 } catch (EOFException var62) {
                                    var209 = var62;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var63) {
                                    var10000 = var63;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append(var4);
                                 } catch (EOFException var60) {
                                    var209 = var60;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var61) {
                                    var10000 = var61;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    var12.append(")");
                                 } catch (EOFException var58) {
                                    var209 = var58;
                                    var10001 = false;
                                    break label984;
                                 } catch (IOException var59) {
                                    var10000 = var59;
                                    var10001 = false;
                                    break label994;
                                 }

                                 var200 = var11;
                                 var10 = var11;

                                 try {
                                    throw new BadVersionException(var12.toString());
                                 } catch (EOFException var38) {
                                    var209 = var38;
                                    var10001 = false;
                                 } catch (IOException var39) {
                                    var10000 = var39;
                                    var10001 = false;
                                    break label994;
                                 }
                              }
                           }

                           var205 = var209;
                           break label949;
                        }

                        var205 = var209;
                        var10 = var11;
                     }

                     StringBuilder var207 = new StringBuilder();
                     var207.append(var10);
                     var207.append(var205.getMessage());
                     throw new BadFormatException(var207.toString());
                  }

                  IOException var206 = var10000;
                  var200 = var10;
                  var208 = var206;
                  throw new IllegalArgumentException(var200, var208);
               }

               var208 = var10000;
               throw new IllegalArgumentException(var200, var208);
            }

            var15.packets = new RTCPPacket[var16.size()];
            var16.copyInto(var15.packets);
            return var15;
         }

         var4 += var2;
         var200 = var11;
      }
   }

   public void removeRTCPPacketParserListener(RTCPPacketParserListener param1) {
      // $FF: Couldn't be decompiled
   }
}
