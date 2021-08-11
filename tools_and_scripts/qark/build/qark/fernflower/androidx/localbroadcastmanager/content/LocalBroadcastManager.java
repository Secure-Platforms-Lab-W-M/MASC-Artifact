package androidx.localbroadcastmanager.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class LocalBroadcastManager {
   private static final boolean DEBUG = false;
   static final int MSG_EXEC_PENDING_BROADCASTS = 1;
   private static final String TAG = "LocalBroadcastManager";
   private static LocalBroadcastManager mInstance;
   private static final Object mLock = new Object();
   private final HashMap mActions = new HashMap();
   private final Context mAppContext;
   private final Handler mHandler;
   private final ArrayList mPendingBroadcasts = new ArrayList();
   private final HashMap mReceivers = new HashMap();

   private LocalBroadcastManager(Context var1) {
      this.mAppContext = var1;
      this.mHandler = new Handler(var1.getMainLooper()) {
         public void handleMessage(Message var1) {
            if (var1.what != 1) {
               super.handleMessage(var1);
            } else {
               LocalBroadcastManager.this.executePendingBroadcasts();
            }
         }
      };
   }

   public static LocalBroadcastManager getInstance(Context var0) {
      Object var1 = mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (mInstance == null) {
               mInstance = new LocalBroadcastManager(var0.getApplicationContext());
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            LocalBroadcastManager var15 = mInstance;
            return var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var14 = var10000;

         try {
            throw var14;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   void executePendingBroadcasts() {
      // $FF: Couldn't be decompiled
   }

   public void registerReceiver(BroadcastReceiver var1, IntentFilter var2) {
      HashMap var6 = this.mReceivers;
      synchronized(var6){}

      Throwable var10000;
      boolean var10001;
      label585: {
         ArrayList var5;
         LocalBroadcastManager.ReceiverRecord var7;
         try {
            var7 = new LocalBroadcastManager.ReceiverRecord(var2, var1);
            var5 = (ArrayList)this.mReceivers.get(var1);
         } catch (Throwable var79) {
            var10000 = var79;
            var10001 = false;
            break label585;
         }

         ArrayList var4 = var5;
         if (var5 == null) {
            try {
               var4 = new ArrayList(1);
               this.mReceivers.put(var1, var4);
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break label585;
            }
         }

         try {
            var4.add(var7);
         } catch (Throwable var77) {
            var10000 = var77;
            var10001 = false;
            break label585;
         }

         int var3 = 0;

         while(true) {
            String var82;
            try {
               if (var3 >= var2.countActions()) {
                  break;
               }

               var82 = var2.getAction(var3);
               var4 = (ArrayList)this.mActions.get(var82);
            } catch (Throwable var76) {
               var10000 = var76;
               var10001 = false;
               break label585;
            }

            ArrayList var80 = var4;
            if (var4 == null) {
               try {
                  var80 = new ArrayList(1);
                  this.mActions.put(var82, var80);
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label585;
               }
            }

            try {
               var80.add(var7);
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label585;
            }

            ++var3;
         }

         label557:
         try {
            return;
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            break label557;
         }
      }

      while(true) {
         Throwable var81 = var10000;

         try {
            throw var81;
         } catch (Throwable var72) {
            var10000 = var72;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean sendBroadcast(Intent var1) {
      HashMap var8 = this.mReceivers;
      synchronized(var8){}

      Throwable var10000;
      boolean var10001;
      label3524: {
         boolean var2;
         String var7;
         String var9;
         Uri var10;
         String var11;
         Set var12;
         label3519: {
            label3518: {
               try {
                  var9 = var1.getAction();
                  var7 = var1.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                  var10 = var1.getData();
                  var11 = var1.getScheme();
                  var12 = var1.getCategories();
                  if ((var1.getFlags() & 8) != 0) {
                     break label3518;
                  }
               } catch (Throwable var395) {
                  var10000 = var395;
                  var10001 = false;
                  break label3524;
               }

               var2 = false;
               break label3519;
            }

            var2 = true;
         }

         StringBuilder var5;
         if (var2) {
            try {
               var5 = new StringBuilder();
               var5.append("Resolving type ");
               var5.append(var7);
               var5.append(" scheme ");
               var5.append(var11);
               var5.append(" of intent ");
               var5.append(var1);
               Log.v("LocalBroadcastManager", var5.toString());
            } catch (Throwable var394) {
               var10000 = var394;
               var10001 = false;
               break label3524;
            }
         }

         ArrayList var13;
         try {
            var13 = (ArrayList)this.mActions.get(var1.getAction());
         } catch (Throwable var393) {
            var10000 = var393;
            var10001 = false;
            break label3524;
         }

         if (var13 != null) {
            if (var2) {
               try {
                  var5 = new StringBuilder();
                  var5.append("Action list: ");
                  var5.append(var13);
                  Log.v("LocalBroadcastManager", var5.toString());
               } catch (Throwable var390) {
                  var10000 = var390;
                  var10001 = false;
                  break label3524;
               }
            }

            Object var6 = null;
            int var3 = 0;

            while(true) {
               LocalBroadcastManager.ReceiverRecord var14;
               try {
                  if (var3 >= var13.size()) {
                     break;
                  }

                  var14 = (LocalBroadcastManager.ReceiverRecord)var13.get(var3);
               } catch (Throwable var391) {
                  var10000 = var391;
                  var10001 = false;
                  break label3524;
               }

               if (var2) {
                  try {
                     var5 = new StringBuilder();
                     var5.append("Matching against filter ");
                     var5.append(var14.filter);
                     Log.v("LocalBroadcastManager", var5.toString());
                  } catch (Throwable var389) {
                     var10000 = var389;
                     var10001 = false;
                     break label3524;
                  }
               }

               Object var398;
               label3498: {
                  label3527: {
                     label3496: {
                        try {
                           if (!var14.broadcasting) {
                              break label3496;
                           }
                        } catch (Throwable var392) {
                           var10000 = var392;
                           var10001 = false;
                           break label3524;
                        }

                        if (var2) {
                           try {
                              Log.v("LocalBroadcastManager", "  Filter's target already added");
                           } catch (Throwable var383) {
                              var10000 = var383;
                              var10001 = false;
                              break label3524;
                           }
                        }
                        break label3527;
                     }

                     IntentFilter var15;
                     try {
                        var15 = var14.filter;
                     } catch (Throwable var388) {
                        var10000 = var388;
                        var10001 = false;
                        break label3524;
                     }

                     var398 = var6;

                     int var4;
                     try {
                        var4 = var15.match(var9, var7, var11, var10, var12, "LocalBroadcastManager");
                     } catch (Throwable var387) {
                        var10000 = var387;
                        var10001 = false;
                        break label3524;
                     }

                     if (var4 >= 0) {
                        if (var2) {
                           try {
                              var6 = new StringBuilder();
                              ((StringBuilder)var6).append("  Filter matched!  match=0x");
                              ((StringBuilder)var6).append(Integer.toHexString(var4));
                              Log.v("LocalBroadcastManager", ((StringBuilder)var6).toString());
                           } catch (Throwable var386) {
                              var10000 = var386;
                              var10001 = false;
                              break label3524;
                           }
                        }

                        if (var6 == null) {
                           try {
                              var398 = new ArrayList();
                           } catch (Throwable var385) {
                              var10000 = var385;
                              var10001 = false;
                              break label3524;
                           }
                        }

                        try {
                           ((ArrayList)var398).add(var14);
                           var14.broadcasting = true;
                           break label3498;
                        } catch (Throwable var384) {
                           var10000 = var384;
                           var10001 = false;
                           break label3524;
                        }
                     }

                     if (var2) {
                        String var399;
                        if (var4 != -4) {
                           if (var4 != -3) {
                              if (var4 != -2) {
                                 if (var4 != -1) {
                                    var399 = "unknown reason";
                                 } else {
                                    var399 = "type";
                                 }
                              } else {
                                 var399 = "data";
                              }
                           } else {
                              var399 = "action";
                           }
                        } else {
                           var399 = "category";
                        }

                        try {
                           StringBuilder var400 = new StringBuilder();
                           var400.append("  Filter did not match: ");
                           var400.append(var399);
                           Log.v("LocalBroadcastManager", var400.toString());
                        } catch (Throwable var382) {
                           var10000 = var382;
                           var10001 = false;
                           break label3524;
                        }
                     }
                  }

                  var398 = var6;
               }

               ++var3;
               var6 = var398;
            }

            if (var6 != null) {
               int var397 = 0;

               while(true) {
                  try {
                     if (var397 >= ((ArrayList)var6).size()) {
                        break;
                     }

                     ((LocalBroadcastManager.ReceiverRecord)((ArrayList)var6).get(var397)).broadcasting = false;
                  } catch (Throwable var380) {
                     var10000 = var380;
                     var10001 = false;
                     break label3524;
                  }

                  ++var397;
               }

               try {
                  this.mPendingBroadcasts.add(new LocalBroadcastManager.BroadcastRecord(var1, (ArrayList)var6));
                  if (!this.mHandler.hasMessages(1)) {
                     this.mHandler.sendEmptyMessage(1);
                  }
               } catch (Throwable var379) {
                  var10000 = var379;
                  var10001 = false;
                  break label3524;
               }

               try {
                  return true;
               } catch (Throwable var378) {
                  var10000 = var378;
                  var10001 = false;
                  break label3524;
               }
            }
         }

         label3458:
         try {
            return false;
         } catch (Throwable var381) {
            var10000 = var381;
            var10001 = false;
            break label3458;
         }
      }

      while(true) {
         Throwable var396 = var10000;

         try {
            throw var396;
         } catch (Throwable var377) {
            var10000 = var377;
            var10001 = false;
            continue;
         }
      }
   }

   public void sendBroadcastSync(Intent var1) {
      if (this.sendBroadcast(var1)) {
         this.executePendingBroadcasts();
      }

   }

   public void unregisterReceiver(BroadcastReceiver var1) {
      HashMap var5 = this.mReceivers;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      label988: {
         ArrayList var6;
         try {
            var6 = (ArrayList)this.mReceivers.remove(var1);
         } catch (Throwable var120) {
            var10000 = var120;
            var10001 = false;
            break label988;
         }

         if (var6 == null) {
            label951:
            try {
               return;
            } catch (Throwable var112) {
               var10000 = var112;
               var10001 = false;
               break label951;
            }
         } else {
            label984: {
               int var2;
               try {
                  var2 = var6.size() - 1;
               } catch (Throwable var117) {
                  var10000 = var117;
                  var10001 = false;
                  break label984;
               }

               label983:
               while(true) {
                  if (var2 < 0) {
                     try {
                        return;
                     } catch (Throwable var113) {
                        var10000 = var113;
                        var10001 = false;
                        break;
                     }
                  }

                  LocalBroadcastManager.ReceiverRecord var7;
                  try {
                     var7 = (LocalBroadcastManager.ReceiverRecord)var6.get(var2);
                     var7.dead = true;
                  } catch (Throwable var116) {
                     var10000 = var116;
                     var10001 = false;
                     break;
                  }

                  int var3 = 0;

                  while(true) {
                     String var8;
                     ArrayList var9;
                     try {
                        if (var3 >= var7.filter.countActions()) {
                           break;
                        }

                        var8 = var7.filter.getAction(var3);
                        var9 = (ArrayList)this.mActions.get(var8);
                     } catch (Throwable var118) {
                        var10000 = var118;
                        var10001 = false;
                        break label983;
                     }

                     if (var9 != null) {
                        int var4;
                        try {
                           var4 = var9.size() - 1;
                        } catch (Throwable var115) {
                           var10000 = var115;
                           var10001 = false;
                           break label983;
                        }

                        while(true) {
                           if (var4 < 0) {
                              try {
                                 if (var9.size() <= 0) {
                                    this.mActions.remove(var8);
                                 }
                                 break;
                              } catch (Throwable var114) {
                                 var10000 = var114;
                                 var10001 = false;
                                 break label983;
                              }
                           }

                           try {
                              LocalBroadcastManager.ReceiverRecord var10 = (LocalBroadcastManager.ReceiverRecord)var9.get(var4);
                              if (var10.receiver == var1) {
                                 var10.dead = true;
                                 var9.remove(var4);
                              }
                           } catch (Throwable var119) {
                              var10000 = var119;
                              var10001 = false;
                              break label983;
                           }

                           --var4;
                        }
                     }

                     ++var3;
                  }

                  --var2;
               }
            }
         }
      }

      while(true) {
         Throwable var121 = var10000;

         try {
            throw var121;
         } catch (Throwable var111) {
            var10000 = var111;
            var10001 = false;
            continue;
         }
      }
   }

   private static final class BroadcastRecord {
      final Intent intent;
      final ArrayList receivers;

      BroadcastRecord(Intent var1, ArrayList var2) {
         this.intent = var1;
         this.receivers = var2;
      }
   }

   private static final class ReceiverRecord {
      boolean broadcasting;
      boolean dead;
      final IntentFilter filter;
      final BroadcastReceiver receiver;

      ReceiverRecord(IntentFilter var1, BroadcastReceiver var2) {
         this.filter = var1;
         this.receiver = var2;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(128);
         var1.append("Receiver{");
         var1.append(this.receiver);
         var1.append(" filter=");
         var1.append(this.filter);
         if (this.dead) {
            var1.append(" DEAD");
         }

         var1.append("}");
         return var1.toString();
      }
   }
}
