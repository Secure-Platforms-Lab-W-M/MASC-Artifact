package dnsfilter;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Set;
import util.Logger;
import util.LoggerInterface;

public class DNSResponsePatcher {
   private static Set FILTER = null;
   private static LoggerInterface TRAFFIC_LOG = null;
   private static boolean checkCNAME = true;
   private static boolean checkIP = false;
   private static long filterCnt = 0L;
   private static byte[] ipv4_localhost;
   private static byte[] ipv6_localhost;
   private static long okCnt = 0L;

   static {
      try {
         ipv4_localhost = InetAddress.getByName("127.0.0.1").getAddress();
         ipv6_localhost = InetAddress.getByName("::1").getAddress();
      } catch (Exception var1) {
         Logger.getLogger().logException(var1);
      }
   }

   private static boolean filter(String var0) {
      boolean var1;
      if (FILTER == null) {
         var1 = false;
      } else {
         var1 = FILTER.contains(var0);
      }

      LoggerInterface var2;
      StringBuilder var3;
      if (var1) {
         var2 = Logger.getLogger();
         var3 = new StringBuilder();
         var3.append("FILTERED:");
         var3.append(var0);
         var2.logLine(var3.toString());
      } else {
         var2 = Logger.getLogger();
         var3 = new StringBuilder();
         var3.append("ALLOWED:");
         var3.append(var0);
         var2.logLine(var3.toString());
      }

      if (!var1) {
         ++okCnt;
         return var1;
      } else {
         ++filterCnt;
         return var1;
      }
   }

   private static boolean filterIP(String var0) {
      boolean var1;
      StringBuilder var3;
      if (FILTER == null) {
         var1 = false;
      } else {
         Set var2 = FILTER;
         var3 = new StringBuilder();
         var3.append("%IP%");
         var3.append(var0);
         var1 = var2.contains(var3.toString());
      }

      if (var1) {
         LoggerInterface var4 = Logger.getLogger();
         var3 = new StringBuilder();
         var3.append("FILTERED:");
         var3.append(var0);
         var4.logLine(var3.toString());
      }

      if (!var1) {
         ++okCnt;
         return var1;
      } else {
         ++filterCnt;
         return var1;
      }
   }

   public static long getFilterCount() {
      return filterCnt;
   }

   public static long getOkCount() {
      return okCnt;
   }

   public static void init(Set var0, LoggerInterface var1) {
      FILTER = var0;
      TRAFFIC_LOG = var1;
      okCnt = 0L;
      filterCnt = 0L;

      try {
         checkIP = Boolean.parseBoolean(ConfigurationAccess.getLocal().getConfig().getProperty("checkResolvedIP", "false"));
         checkCNAME = Boolean.parseBoolean(ConfigurationAccess.getLocal().getConfig().getProperty("checkCNAME", "false"));
      } catch (IOException var2) {
         Logger.getLogger().logException(var2);
      }
   }

   public static byte[] patchResponse(String var0, byte[] var1, int var2) throws IOException {
      IOException var75;
      label283: {
         Exception var10000;
         label285: {
            short var7;
            short var9;
            ByteBuffer var15;
            boolean var10001;
            try {
               var15 = ByteBuffer.wrap(var1, var2, var1.length - var2);
               var15.getShort();
               var15.getShort();
               var7 = var15.getShort();
               var9 = var15.getShort();
               var15.getShort();
               var15.getShort();
            } catch (IOException var61) {
               var75 = var61;
               var10001 = false;
               break label283;
            } catch (Exception var62) {
               var10000 = var62;
               var10001 = false;
               break label285;
            }

            boolean var3 = false;
            String var12 = "";
            int var5 = 0;

            label277:
            while(true) {
               boolean var6 = true;
               boolean var4;
               if (var5 >= var7) {
                  int var69 = 0;

                  boolean var70;
                  for(boolean var68 = var3; var69 < var9; var68 = var70) {
                     short var10;
                     short var11;
                     String var14;
                     try {
                        var14 = readDomainName(var15, var2);
                        var10 = var15.getShort();
                        var15.getShort();
                        var15.getInt();
                        var11 = var15.getShort();
                     } catch (IOException var39) {
                        var75 = var39;
                        var10001 = false;
                        break label283;
                     } catch (Exception var40) {
                        var10000 = var40;
                        var10001 = false;
                        break label277;
                     }

                     String var67;
                     String var74;
                     label295: {
                        boolean var71 = false;
                        if (var10 != 1) {
                           var74 = var12;
                           var70 = var68;
                           var4 = var71;
                           if (var10 != 28) {
                              break label295;
                           }
                        }

                        var67 = var12;
                        var3 = var68;
                        if (!var68) {
                           label293: {
                              var67 = var12;
                              var3 = var68;

                              try {
                                 if (!checkCNAME) {
                                    break label293;
                                 }
                              } catch (IOException var49) {
                                 var75 = var49;
                                 var10001 = false;
                                 break label283;
                              } catch (Exception var50) {
                                 var10000 = var50;
                                 var10001 = false;
                                 break label277;
                              }

                              var67 = var12;
                              var3 = var68;

                              try {
                                 if (var14.equals(var12)) {
                                    break label293;
                                 }
                              } catch (IOException var47) {
                                 var75 = var47;
                                 var10001 = false;
                                 break label283;
                              } catch (Exception var48) {
                                 var10000 = var48;
                                 var10001 = false;
                                 break label277;
                              }

                              label239: {
                                 label238: {
                                    if (!var68) {
                                       try {
                                          if (!filter(var14)) {
                                             break label238;
                                          }
                                       } catch (IOException var51) {
                                          var75 = var51;
                                          var10001 = false;
                                          break label283;
                                       } catch (Exception var52) {
                                          var10000 = var52;
                                          var10001 = false;
                                          break label277;
                                       }
                                    }

                                    var3 = true;
                                    break label239;
                                 }

                                 var3 = false;
                              }

                              var67 = var14;
                           }
                        }

                        if (var3) {
                           var68 = true;
                           if (var10 == 1) {
                              try {
                                 var15.put(ipv4_localhost);
                              } catch (IOException var37) {
                                 var75 = var37;
                                 var10001 = false;
                                 break label283;
                              } catch (Exception var38) {
                                 var10000 = var38;
                                 var10001 = false;
                                 break label277;
                              }

                              var74 = var67;
                              var70 = var3;
                              var4 = var68;
                           } else {
                              var74 = var67;
                              var70 = var3;
                              var4 = var68;
                              if (var10 == 28) {
                                 try {
                                    var15.put(ipv6_localhost);
                                 } catch (IOException var35) {
                                    var75 = var35;
                                    var10001 = false;
                                    break label283;
                                 } catch (Exception var36) {
                                    var10000 = var36;
                                    var10001 = false;
                                    break label277;
                                 }

                                 var74 = var67;
                                 var70 = var3;
                                 var4 = var68;
                              }
                           }
                        } else {
                           label290: {
                              var74 = var67;
                              var70 = var3;
                              var4 = var71;

                              byte[] var72;
                              try {
                                 if (!checkIP) {
                                    break label290;
                                 }

                                 var72 = new byte[var11];
                                 var15.get(var72);
                                 var15.position(var15.position() - var11);
                              } catch (IOException var45) {
                                 var75 = var45;
                                 var10001 = false;
                                 break label283;
                              } catch (Exception var46) {
                                 var10000 = var46;
                                 var10001 = false;
                                 break label277;
                              }

                              var74 = var67;
                              var70 = var3;
                              var4 = var71;

                              try {
                                 if (!filterIP(InetAddress.getByAddress(var72).getHostAddress())) {
                                    break label290;
                                 }
                              } catch (IOException var43) {
                                 var75 = var43;
                                 var10001 = false;
                                 break label283;
                              } catch (Exception var44) {
                                 var10000 = var44;
                                 var10001 = false;
                                 break label277;
                              }

                              var68 = true;
                              if (var10 == 1) {
                                 try {
                                    var15.put(ipv4_localhost);
                                 } catch (IOException var33) {
                                    var75 = var33;
                                    var10001 = false;
                                    break label283;
                                 } catch (Exception var34) {
                                    var10000 = var34;
                                    var10001 = false;
                                    break label277;
                                 }

                                 var74 = var67;
                                 var70 = var3;
                                 var4 = var68;
                              } else {
                                 var74 = var67;
                                 var70 = var3;
                                 var4 = var68;
                                 if (var10 == 28) {
                                    try {
                                       var15.put(ipv6_localhost);
                                    } catch (IOException var31) {
                                       var75 = var31;
                                       var10001 = false;
                                       break label283;
                                    } catch (Exception var32) {
                                       var10000 = var32;
                                       var10001 = false;
                                       break label277;
                                    }

                                    var4 = var68;
                                    var70 = var3;
                                    var74 = var67;
                                 }
                              }
                           }
                        }
                     }

                     if (!var4) {
                        try {
                           var15.position(var15.position() + var11);
                        } catch (IOException var29) {
                           var75 = var29;
                           var10001 = false;
                           break label283;
                        } catch (Exception var30) {
                           var10000 = var30;
                           var10001 = false;
                           break label277;
                        }
                     }

                     label296: {
                        try {
                           if (TRAFFIC_LOG == null) {
                              break label296;
                           }

                           var1 = new byte[var11];
                           var15.position(var15.position() - var11);
                        } catch (IOException var41) {
                           var75 = var41;
                           var10001 = false;
                           break label283;
                        } catch (Exception var42) {
                           var10000 = var42;
                           var10001 = false;
                           break label277;
                        }

                        if (var10 == 5) {
                           try {
                              var67 = readDomainName(var15, var2);
                           } catch (IOException var27) {
                              var75 = var27;
                              var10001 = false;
                              break label283;
                           } catch (Exception var28) {
                              var10000 = var28;
                              var10001 = false;
                              break label277;
                           }
                        } else {
                           try {
                              var15.get(var1);
                           } catch (IOException var25) {
                              var75 = var25;
                              var10001 = false;
                              break label283;
                           } catch (Exception var26) {
                              var10000 = var26;
                              var10001 = false;
                              break label277;
                           }

                           if (var10 != 1 && var10 != 28) {
                              try {
                                 var67 = new String(var1);
                              } catch (IOException var23) {
                                 var75 = var23;
                                 var10001 = false;
                                 break label283;
                              } catch (Exception var24) {
                                 var10000 = var24;
                                 var10001 = false;
                                 break label277;
                              }
                           } else {
                              try {
                                 var67 = InetAddress.getByAddress(var1).getHostAddress();
                              } catch (IOException var21) {
                                 var75 = var21;
                                 var10001 = false;
                                 break label283;
                              } catch (Exception var22) {
                                 var10000 = var22;
                                 var10001 = false;
                                 break label277;
                              }
                           }
                        }

                        try {
                           LoggerInterface var73 = TRAFFIC_LOG;
                           StringBuilder var16 = new StringBuilder();
                           var16.append(var0);
                           var16.append(", A-");
                           var16.append(var10);
                           var16.append(", ");
                           var16.append(var14);
                           var16.append(", ");
                           var16.append(var67);
                           var16.append(", /Length:");
                           var16.append(var11);
                           var73.logLine(var16.toString());
                        } catch (IOException var19) {
                           var75 = var19;
                           var10001 = false;
                           break label283;
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                           break label277;
                        }
                     }

                     ++var69;
                     var12 = var74;
                  }

                  try {
                     byte[] var65 = var15.array();
                     return var65;
                  } catch (IOException var17) {
                     var75 = var17;
                     var10001 = false;
                     break label283;
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break;
                  }
               }

               short var8;
               try {
                  var12 = readDomainName(var15, var2);
                  var8 = var15.getShort();
               } catch (IOException var55) {
                  var75 = var55;
                  var10001 = false;
                  break label283;
               } catch (Exception var56) {
                  var10000 = var56;
                  var10001 = false;
                  break;
               }

               label274: {
                  if (var8 != 1) {
                     var4 = var3;
                     if (var8 != 28) {
                        break label274;
                     }
                  }

                  var4 = var6;
                  if (!var3) {
                     label269: {
                        label268: {
                           try {
                              if (!filter(var12)) {
                                 break label268;
                              }
                           } catch (IOException var59) {
                              var75 = var59;
                              var10001 = false;
                              break label283;
                           } catch (Exception var60) {
                              var10000 = var60;
                              var10001 = false;
                              break;
                           }

                           var4 = var6;
                           break label269;
                        }

                        var4 = false;
                     }
                  }
               }

               try {
                  if (TRAFFIC_LOG != null) {
                     LoggerInterface var66 = TRAFFIC_LOG;
                     StringBuilder var13 = new StringBuilder();
                     var13.append(var0);
                     var13.append(", Q-");
                     var13.append(var8);
                     var13.append(", ");
                     var13.append(var12);
                     var13.append(", <empty>");
                     var66.logLine(var13.toString());
                  }
               } catch (IOException var57) {
                  var75 = var57;
                  var10001 = false;
                  break label283;
               } catch (Exception var58) {
                  var10000 = var58;
                  var10001 = false;
                  break;
               }

               try {
                  var15.getShort();
               } catch (IOException var53) {
                  var75 = var53;
                  var10001 = false;
                  break label283;
               } catch (Exception var54) {
                  var10000 = var54;
                  var10001 = false;
                  break;
               }

               ++var5;
               var3 = var4;
            }
         }

         Exception var63 = var10000;
         throw new IOException("Invalid DNS Response Message Structure", var63);
      }

      IOException var64 = var75;
      throw var64;
   }

   private static String readDomainName(ByteBuffer var0, int var1) throws IOException {
      byte[] var8 = new byte[64];
      String var6 = "";
      String var7 = "";
      byte var3 = -1;
      int var2 = -1;

      while(var3 != 0) {
         byte var4 = var0.get();
         if (var4 != 0) {
            if ((var4 & 192) == 0) {
               var0.get(var8, 0, var4);
               StringBuilder var9 = new StringBuilder();
               var9.append(var6);
               var9.append(var7);
               var9.append(new String(var8, 0, var4));
               var6 = var9.toString();
               var7 = ".";
               var3 = var4;
            } else {
               var0.position(var0.position() - 1);
               short var5 = var0.getShort();
               int var10 = var2;
               if (var2 == -1) {
                  var10 = var0.position();
               }

               var0.position((var5 & 16383) + var1);
               var2 = var10;
               var3 = var4;
            }
         } else {
            var3 = var4;
            if (var4 == 0) {
               var3 = var4;
               if (var2 != -1) {
                  var0.position(var2);
                  var3 = var4;
               }
            }
         }
      }

      return var6;
   }
}
