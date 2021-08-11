package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;

public final class StreamAllocation {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   public final Address address;
   private final Object callStackTrace;
   private boolean canceled;
   private HttpCodec codec;
   private RealConnection connection;
   private final ConnectionPool connectionPool;
   private int refusedStreamCount;
   private boolean released;
   private Route route;
   private final RouteSelector routeSelector;

   public StreamAllocation(ConnectionPool var1, Address var2, Object var3) {
      this.connectionPool = var1;
      this.address = var2;
      this.routeSelector = new RouteSelector(var2, this.routeDatabase());
      this.callStackTrace = var3;
   }

   private Socket deallocate(boolean var1, boolean var2, boolean var3) {
      if (var3) {
         this.codec = null;
      }

      if (var2) {
         this.released = true;
      }

      Object var6 = null;
      Object var5 = null;
      RealConnection var7 = this.connection;
      Socket var4 = (Socket)var6;
      if (var7 != null) {
         if (var1) {
            var7.noNewStreams = true;
         }

         var4 = (Socket)var6;
         if (this.codec == null) {
            if (!this.released) {
               var4 = (Socket)var6;
               if (!this.connection.noNewStreams) {
                  return var4;
               }
            }

            this.release(this.connection);
            var4 = (Socket)var5;
            if (this.connection.allocations.isEmpty()) {
               this.connection.idleAtNanos = System.nanoTime();
               var4 = (Socket)var5;
               if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                  var4 = this.connection.socket();
               }
            }

            this.connection = null;
         }
      }

      return var4;
   }

   private RealConnection findConnection(int var1, int var2, int var3, boolean var4) throws IOException {
      ConnectionPool var5 = this.connectionPool;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      label2621: {
         label2620: {
            label2619: {
               label2625: {
                  RealConnection var6;
                  try {
                     if (this.released) {
                        break label2625;
                     }

                     if (this.codec != null) {
                        break label2619;
                     }

                     if (this.canceled) {
                        break label2620;
                     }

                     var6 = this.connection;
                  } catch (Throwable var280) {
                     var10000 = var280;
                     var10001 = false;
                     break label2621;
                  }

                  if (var6 != null) {
                     try {
                        if (!var6.noNewStreams) {
                           return var6;
                        }
                     } catch (Throwable var278) {
                        var10000 = var278;
                        var10001 = false;
                        break label2621;
                     }
                  }

                  try {
                     Internal.instance.get(this.connectionPool, this.address, this, (Route)null);
                     if (this.connection != null) {
                        var6 = this.connection;
                        return var6;
                     }
                  } catch (Throwable var279) {
                     var10000 = var279;
                     var10001 = false;
                     break label2621;
                  }

                  Route var282;
                  try {
                     var282 = this.route;
                  } catch (Throwable var274) {
                     var10000 = var274;
                     var10001 = false;
                     break label2621;
                  }

                  Route var281 = var282;
                  if (var282 == null) {
                     var281 = this.routeSelector.next();
                  }

                  ConnectionPool var284 = this.connectionPool;
                  synchronized(var284){}

                  Throwable var286;
                  label2583: {
                     label2627: {
                        RealConnection var283;
                        try {
                           if (this.canceled) {
                              break label2627;
                           }

                           Internal.instance.get(this.connectionPool, this.address, this, var281);
                           if (this.connection != null) {
                              this.route = var281;
                              var283 = this.connection;
                              return var283;
                           }
                        } catch (Throwable var273) {
                           var10000 = var273;
                           var10001 = false;
                           break label2583;
                        }

                        RealConnection var7;
                        try {
                           this.route = var281;
                           this.refusedStreamCount = 0;
                           var7 = new RealConnection(this.connectionPool, var281);
                           this.acquire(var7);
                        } catch (Throwable var271) {
                           var10000 = var271;
                           var10001 = false;
                           break label2583;
                        }

                        var7.connect(var1, var2, var3, var4);
                        this.routeDatabase().connected(var7.route());
                        Socket var285 = null;
                        ConnectionPool var8 = this.connectionPool;
                        synchronized(var8){}

                        label2629: {
                           label2630: {
                              try {
                                 Internal.instance.put(this.connectionPool, var7);
                              } catch (Throwable var270) {
                                 var10000 = var270;
                                 var10001 = false;
                                 break label2630;
                              }

                              var283 = var7;

                              try {
                                 if (var7.isMultiplexed()) {
                                    var285 = Internal.instance.deduplicate(this.connectionPool, this.address, this);
                                    var283 = this.connection;
                                 }
                              } catch (Throwable var269) {
                                 var10000 = var269;
                                 var10001 = false;
                                 break label2630;
                              }

                              label2557:
                              try {
                                 break label2629;
                              } catch (Throwable var268) {
                                 var10000 = var268;
                                 var10001 = false;
                                 break label2557;
                              }
                           }

                           while(true) {
                              var286 = var10000;

                              try {
                                 throw var286;
                              } catch (Throwable var267) {
                                 var10000 = var267;
                                 var10001 = false;
                                 continue;
                              }
                           }
                        }

                        Util.closeQuietly(var285);
                        return var283;
                     }

                     label2576:
                     try {
                        throw new IOException("Canceled");
                     } catch (Throwable var272) {
                        var10000 = var272;
                        var10001 = false;
                        break label2576;
                     }
                  }

                  while(true) {
                     var286 = var10000;

                     try {
                        throw var286;
                     } catch (Throwable var266) {
                        var10000 = var266;
                        var10001 = false;
                        continue;
                     }
                  }
               }

               try {
                  throw new IllegalStateException("released");
               } catch (Throwable var277) {
                  var10000 = var277;
                  var10001 = false;
                  break label2621;
               }
            }

            try {
               throw new IllegalStateException("codec != null");
            } catch (Throwable var276) {
               var10000 = var276;
               var10001 = false;
               break label2621;
            }
         }

         label2593:
         try {
            throw new IOException("Canceled");
         } catch (Throwable var275) {
            var10000 = var275;
            var10001 = false;
            break label2593;
         }
      }

      while(true) {
         Throwable var287 = var10000;

         try {
            throw var287;
         } catch (Throwable var265) {
            var10000 = var265;
            var10001 = false;
            continue;
         }
      }
   }

   private RealConnection findHealthyConnection(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      while(true) {
         RealConnection var7 = this.findConnection(var1, var2, var3, var4);
         ConnectionPool var6 = this.connectionPool;
         synchronized(var6){}

         Throwable var10000;
         boolean var10001;
         label157: {
            try {
               if (var7.successCount == 0) {
                  return var7;
               }
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label157;
            }

            try {
               ;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label157;
            }

            if (!var7.isHealthy(var5)) {
               this.noNewStreams();
               continue;
            }

            return var7;
         }

         while(true) {
            Throwable var20 = var10000;

            try {
               throw var20;
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               continue;
            }
         }
      }
   }

   private void release(RealConnection var1) {
      int var2 = 0;

      for(int var3 = var1.allocations.size(); var2 < var3; ++var2) {
         if (((Reference)var1.allocations.get(var2)).get() == this) {
            var1.allocations.remove(var2);
            return;
         }
      }

      throw new IllegalStateException();
   }

   private RouteDatabase routeDatabase() {
      return Internal.instance.routeDatabase(this.connectionPool);
   }

   public void acquire(RealConnection var1) {
      if (this.connection == null) {
         this.connection = var1;
         var1.allocations.add(new StreamAllocation.StreamAllocationReference(this, this.callStackTrace));
      } else {
         throw new IllegalStateException();
      }
   }

   public void cancel() {
      // $FF: Couldn't be decompiled
   }

   public HttpCodec codec() {
      // $FF: Couldn't be decompiled
   }

   public RealConnection connection() {
      synchronized(this){}

      RealConnection var1;
      try {
         var1 = this.connection;
      } finally {
         ;
      }

      return var1;
   }

   public boolean hasMoreRoutes() {
      return this.route != null || this.routeSelector.hasNext();
   }

   public HttpCodec newStream(OkHttpClient param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public void noNewStreams() {
      // $FF: Couldn't be decompiled
   }

   public void release() {
      // $FF: Couldn't be decompiled
   }

   public Socket releaseAndAcquire(RealConnection var1) {
      if (this.codec == null && this.connection.allocations.size() == 1) {
         Reference var2 = (Reference)this.connection.allocations.get(0);
         Socket var3 = this.deallocate(true, false, false);
         this.connection = var1;
         var1.allocations.add(var2);
         return var3;
      } else {
         throw new IllegalStateException();
      }
   }

   public void streamFailed(IOException var1) {
      boolean var2 = false;
      boolean var3 = false;
      ConnectionPool var4 = this.connectionPool;
      synchronized(var4){}

      Socket var139;
      label1187: {
         Throwable var10000;
         boolean var10001;
         label1182: {
            label1188: {
               label1189: {
                  StreamResetException var137;
                  try {
                     if (!(var1 instanceof StreamResetException)) {
                        break label1189;
                     }

                     var137 = (StreamResetException)var1;
                     if (var137.errorCode == ErrorCode.REFUSED_STREAM) {
                        ++this.refusedStreamCount;
                     }
                  } catch (Throwable var136) {
                     var10000 = var136;
                     var10001 = false;
                     break label1182;
                  }

                  label1161: {
                     try {
                        if (var137.errorCode != ErrorCode.REFUSED_STREAM) {
                           break label1161;
                        }
                     } catch (Throwable var133) {
                        var10000 = var133;
                        var10001 = false;
                        break label1182;
                     }

                     try {
                        if (this.refusedStreamCount <= 1) {
                           break label1188;
                        }
                     } catch (Throwable var131) {
                        var10000 = var131;
                        var10001 = false;
                        break label1182;
                     }
                  }

                  var2 = true;

                  try {
                     this.route = null;
                     break label1188;
                  } catch (Throwable var130) {
                     var10000 = var130;
                     var10001 = false;
                     break label1182;
                  }
               }

               label1190: {
                  try {
                     if (this.connection == null) {
                        break label1188;
                     }

                     if (!this.connection.isMultiplexed()) {
                        break label1190;
                     }
                  } catch (Throwable var135) {
                     var10000 = var135;
                     var10001 = false;
                     break label1182;
                  }

                  var2 = var3;

                  try {
                     if (!(var1 instanceof ConnectionShutdownException)) {
                        break label1188;
                     }
                  } catch (Throwable var132) {
                     var10000 = var132;
                     var10001 = false;
                     break label1182;
                  }
               }

               var3 = true;
               var2 = var3;

               label1167: {
                  try {
                     if (this.connection.successCount != 0) {
                        break label1188;
                     }

                     if (this.route == null) {
                        break label1167;
                     }
                  } catch (Throwable var134) {
                     var10000 = var134;
                     var10001 = false;
                     break label1182;
                  }

                  if (var1 != null) {
                     try {
                        this.routeSelector.connectFailed(this.route, var1);
                     } catch (Throwable var129) {
                        var10000 = var129;
                        var10001 = false;
                        break label1182;
                     }
                  }
               }

               try {
                  this.route = null;
               } catch (Throwable var128) {
                  var10000 = var128;
                  var10001 = false;
                  break label1182;
               }

               var2 = var3;
            }

            label1140:
            try {
               var139 = this.deallocate(var2, false, true);
               break label1187;
            } catch (Throwable var127) {
               var10000 = var127;
               var10001 = false;
               break label1140;
            }
         }

         while(true) {
            Throwable var138 = var10000;

            try {
               throw var138;
            } catch (Throwable var126) {
               var10000 = var126;
               var10001 = false;
               continue;
            }
         }
      }

      Util.closeQuietly(var139);
   }

   public void streamFinished(boolean var1, HttpCodec var2) {
      Throwable var10000;
      boolean var10001;
      label259: {
         label255: {
            ConnectionPool var3 = this.connectionPool;
            synchronized(var3){}
            if (var2 != null) {
               try {
                  if (var2 == this.codec) {
                     break label255;
                  }
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label259;
               }
            }

            try {
               StringBuilder var4 = new StringBuilder();
               var4.append("expected ");
               var4.append(this.codec);
               var4.append(" but was ");
               var4.append(var2);
               throw new IllegalStateException(var4.toString());
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label259;
            }
         }

         if (!var1) {
            try {
               RealConnection var35 = this.connection;
               ++var35.successCount;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label259;
            }
         }

         Socket var37;
         try {
            var37 = this.deallocate(var1, false, true);
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label259;
         }

         Util.closeQuietly(var37);
         return;
      }

      while(true) {
         Throwable var36 = var10000;

         try {
            throw var36;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public String toString() {
      RealConnection var1 = this.connection();
      return var1 != null ? var1.toString() : this.address.toString();
   }

   public static final class StreamAllocationReference extends WeakReference {
      public final Object callStackTrace;

      StreamAllocationReference(StreamAllocation var1, Object var2) {
         super(var1);
         this.callStackTrace = var2;
      }
   }
}
