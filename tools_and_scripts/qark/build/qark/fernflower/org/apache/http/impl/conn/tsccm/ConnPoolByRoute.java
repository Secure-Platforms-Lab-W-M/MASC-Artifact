package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class ConnPoolByRoute extends AbstractConnPool {
   protected final ConnPerRoute connPerRoute;
   private final long connTTL;
   private final TimeUnit connTTLTimeUnit;
   protected final Queue freeConnections;
   protected final Set leasedConnections;
   private final Log log;
   protected volatile int maxTotalConnections;
   protected volatile int numConnections;
   protected final ClientConnectionOperator operator;
   private final Lock poolLock;
   protected final Map routeToPool;
   protected volatile boolean shutdown;
   protected final Queue waitingThreads;

   public ConnPoolByRoute(ClientConnectionOperator var1, ConnPerRoute var2, int var3) {
      this(var1, var2, var3, -1L, TimeUnit.MILLISECONDS);
   }

   public ConnPoolByRoute(ClientConnectionOperator var1, ConnPerRoute var2, int var3, long var4, TimeUnit var6) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "Connection operator");
      Args.notNull(var2, "Connections per route");
      this.poolLock = super.poolLock;
      this.leasedConnections = super.leasedConnections;
      this.operator = var1;
      this.connPerRoute = var2;
      this.maxTotalConnections = var3;
      this.freeConnections = this.createFreeConnQueue();
      this.waitingThreads = this.createWaitingThreadQueue();
      this.routeToPool = this.createRouteToPoolMap();
      this.connTTL = var4;
      this.connTTLTimeUnit = var6;
   }

   @Deprecated
   public ConnPoolByRoute(ClientConnectionOperator var1, HttpParams var2) {
      this(var1, ConnManagerParams.getMaxConnectionsPerRoute(var2), ConnManagerParams.getMaxTotalConnections(var2));
   }

   private void closeConnection(BasicPoolEntry var1) {
      OperatedClientConnection var3 = var1.getConnection();
      if (var3 != null) {
         try {
            var3.close();
            return;
         } catch (IOException var2) {
            this.log.debug("I/O error closing connection", var2);
         }
      }

   }

   public void closeExpiredConnections() {
      this.log.debug("Closing expired connections");
      long var1 = System.currentTimeMillis();
      this.poolLock.lock();

      label156: {
         Throwable var10000;
         label155: {
            boolean var10001;
            Iterator var3;
            try {
               var3 = this.freeConnections.iterator();
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label155;
            }

            while(true) {
               BasicPoolEntry var4;
               try {
                  if (!var3.hasNext()) {
                     break label156;
                  }

                  var4 = (BasicPoolEntry)var3.next();
                  if (!var4.isExpired(var1)) {
                     continue;
                  }

                  if (this.log.isDebugEnabled()) {
                     Log var5 = this.log;
                     StringBuilder var6 = new StringBuilder();
                     var6.append("Closing connection expired @ ");
                     var6.append(new Date(var4.getExpiry()));
                     var5.debug(var6.toString());
                  }
               } catch (Throwable var17) {
                  var10000 = var17;
                  var10001 = false;
                  break;
               }

               try {
                  var3.remove();
                  this.deleteEntry(var4);
               } catch (Throwable var16) {
                  var10000 = var16;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var19 = var10000;
         this.poolLock.unlock();
         throw var19;
      }

      this.poolLock.unlock();
   }

   public void closeIdleConnections(long var1, TimeUnit var3) {
      Args.notNull(var3, "Time unit");
      long var4 = 0L;
      if (var1 > 0L) {
         var4 = var1;
      }

      if (this.log.isDebugEnabled()) {
         Log var6 = this.log;
         StringBuilder var7 = new StringBuilder();
         var7.append("Closing connections idle longer than ");
         var7.append(var4);
         var7.append(" ");
         var7.append(var3);
         var6.debug(var7.toString());
      }

      var1 = System.currentTimeMillis();
      var4 = var3.toMillis(var4);
      this.poolLock.lock();

      label185: {
         Throwable var10000;
         label184: {
            boolean var10001;
            Iterator var21;
            try {
               var21 = this.freeConnections.iterator();
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label184;
            }

            while(true) {
               BasicPoolEntry var23;
               try {
                  if (!var21.hasNext()) {
                     break label185;
                  }

                  var23 = (BasicPoolEntry)var21.next();
                  if (var23.getUpdated() > var1 - var4) {
                     continue;
                  }

                  if (this.log.isDebugEnabled()) {
                     Log var24 = this.log;
                     StringBuilder var8 = new StringBuilder();
                     var8.append("Closing connection last used @ ");
                     var8.append(new Date(var23.getUpdated()));
                     var24.debug(var8.toString());
                  }
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break;
               }

               try {
                  var21.remove();
                  this.deleteEntry(var23);
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var22 = var10000;
         this.poolLock.unlock();
         throw var22;
      }

      this.poolLock.unlock();
   }

   protected BasicPoolEntry createEntry(RouteSpecificPool var1, ClientConnectionOperator var2) {
      if (this.log.isDebugEnabled()) {
         Log var3 = this.log;
         StringBuilder var4 = new StringBuilder();
         var4.append("Creating new connection [");
         var4.append(var1.getRoute());
         var4.append("]");
         var3.debug(var4.toString());
      }

      BasicPoolEntry var7 = new BasicPoolEntry(var2, var1.getRoute(), this.connTTL, this.connTTLTimeUnit);
      this.poolLock.lock();

      try {
         var1.createdEntry(var7);
         ++this.numConnections;
         this.leasedConnections.add(var7);
      } finally {
         this.poolLock.unlock();
      }

      return var7;
   }

   protected Queue createFreeConnQueue() {
      return new LinkedList();
   }

   protected Map createRouteToPoolMap() {
      return new HashMap();
   }

   protected Queue createWaitingThreadQueue() {
      return new LinkedList();
   }

   public void deleteClosedConnections() {
      this.poolLock.lock();

      label92: {
         Throwable var10000;
         label91: {
            Iterator var1;
            boolean var10001;
            try {
               var1 = this.freeConnections.iterator();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label91;
            }

            while(true) {
               try {
                  if (!var1.hasNext()) {
                     break label92;
                  }

                  BasicPoolEntry var2 = (BasicPoolEntry)var1.next();
                  if (!var2.getConnection().isOpen()) {
                     var1.remove();
                     this.deleteEntry(var2);
                  }
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var9 = var10000;
         this.poolLock.unlock();
         throw var9;
      }

      this.poolLock.unlock();
   }

   protected void deleteEntry(BasicPoolEntry var1) {
      HttpRoute var2 = var1.getPlannedRoute();
      if (this.log.isDebugEnabled()) {
         Log var3 = this.log;
         StringBuilder var4 = new StringBuilder();
         var4.append("Deleting connection [");
         var4.append(var2);
         var4.append("][");
         var4.append(var1.getState());
         var4.append("]");
         var3.debug(var4.toString());
      }

      this.poolLock.lock();

      try {
         this.closeConnection(var1);
         RouteSpecificPool var7 = this.getRoutePool(var2, true);
         var7.deleteEntry(var1);
         --this.numConnections;
         if (var7.isUnused()) {
            this.routeToPool.remove(var2);
         }
      } finally {
         this.poolLock.unlock();
      }

   }

   protected void deleteLeastUsedEntry() {
      this.poolLock.lock();

      label114: {
         Throwable var10000;
         label113: {
            BasicPoolEntry var1;
            boolean var10001;
            try {
               var1 = (BasicPoolEntry)this.freeConnections.remove();
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label113;
            }

            if (var1 != null) {
               label107:
               try {
                  this.deleteEntry(var1);
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label107;
               }
            } else {
               label109:
               try {
                  if (this.log.isDebugEnabled()) {
                     this.log.debug("No free connection to delete");
                  }
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label109;
               }
            }
            break label114;
         }

         Throwable var14 = var10000;
         this.poolLock.unlock();
         throw var14;
      }

      this.poolLock.unlock();
   }

   public void freeEntry(BasicPoolEntry var1, boolean var2, long var3, TimeUnit var5) {
      HttpRoute var7 = var1.getPlannedRoute();
      if (this.log.isDebugEnabled()) {
         Log var6 = this.log;
         StringBuilder var8 = new StringBuilder();
         var8.append("Releasing connection [");
         var8.append(var7);
         var8.append("][");
         var8.append(var1.getState());
         var8.append("]");
         var6.debug(var8.toString());
      }

      this.poolLock.lock();

      label694: {
         Throwable var10000;
         label700: {
            boolean var10001;
            try {
               if (this.shutdown) {
                  this.closeConnection(var1);
                  break label694;
               }
            } catch (Throwable var82) {
               var10000 = var82;
               var10001 = false;
               break label700;
            }

            RouteSpecificPool var86;
            try {
               this.leasedConnections.remove(var1);
               var86 = this.getRoutePool(var7, true);
            } catch (Throwable var81) {
               var10000 = var81;
               var10001 = false;
               break label700;
            }

            label704: {
               if (var2) {
                  label703: {
                     label702: {
                        try {
                           if (var86.getCapacity() < 0) {
                              break label703;
                           }

                           if (!this.log.isDebugEnabled()) {
                              break label702;
                           }
                        } catch (Throwable var80) {
                           var10000 = var80;
                           var10001 = false;
                           break label700;
                        }

                        String var85;
                        if (var3 > 0L) {
                           try {
                              StringBuilder var84 = new StringBuilder();
                              var84.append("for ");
                              var84.append(var3);
                              var84.append(" ");
                              var84.append(var5);
                              var85 = var84.toString();
                           } catch (Throwable var79) {
                              var10000 = var79;
                              var10001 = false;
                              break label700;
                           }
                        } else {
                           var85 = "indefinitely";
                        }

                        try {
                           Log var9 = this.log;
                           StringBuilder var10 = new StringBuilder();
                           var10.append("Pooling connection [");
                           var10.append(var7);
                           var10.append("][");
                           var10.append(var1.getState());
                           var10.append("]; keep alive ");
                           var10.append(var85);
                           var9.debug(var10.toString());
                        } catch (Throwable var78) {
                           var10000 = var78;
                           var10001 = false;
                           break label700;
                        }
                     }

                     try {
                        var86.freeEntry(var1);
                        var1.updateExpiry(var3, var5);
                        this.freeConnections.add(var1);
                        break label704;
                     } catch (Throwable var77) {
                        var10000 = var77;
                        var10001 = false;
                        break label700;
                     }
                  }
               }

               try {
                  this.closeConnection(var1);
                  var86.dropEntry();
                  --this.numConnections;
               } catch (Throwable var76) {
                  var10000 = var76;
                  var10001 = false;
                  break label700;
               }
            }

            try {
               this.notifyWaitingThread(var86);
            } catch (Throwable var75) {
               var10000 = var75;
               var10001 = false;
               break label700;
            }

            this.poolLock.unlock();
            return;
         }

         Throwable var83 = var10000;
         this.poolLock.unlock();
         throw var83;
      }

      this.poolLock.unlock();
   }

   public int getConnectionsInPool() {
      this.poolLock.lock();

      int var1;
      try {
         var1 = this.numConnections;
      } finally {
         this.poolLock.unlock();
      }

      return var1;
   }

   public int getConnectionsInPool(HttpRoute var1) {
      this.poolLock.lock();
      int var2 = 0;

      label71: {
         Throwable var10000;
         label75: {
            boolean var10001;
            RouteSpecificPool var9;
            try {
               var9 = this.getRoutePool(var1, false);
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label75;
            }

            if (var9 == null) {
               break label71;
            }

            label66:
            try {
               var2 = var9.getEntryCount();
               break label71;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label66;
            }
         }

         Throwable var10 = var10000;
         this.poolLock.unlock();
         throw var10;
      }

      this.poolLock.unlock();
      return var2;
   }

   protected BasicPoolEntry getEntryBlocking(HttpRoute var1, Object var2, long var3, TimeUnit var5, WaitingThreadAborter var6) throws ConnectionPoolTimeoutException, InterruptedException {
      Date var10;
      if (var3 > 0L) {
         var10 = new Date(System.currentTimeMillis() + var5.toMillis(var3));
      } else {
         var10 = null;
      }

      BasicPoolEntry var344 = null;
      this.poolLock.lock();

      Throwable var343;
      label2967: {
         Throwable var10000;
         label2966: {
            boolean var10001;
            label2965: {
               label2964: {
                  label2971: {
                     RouteSpecificPool var11;
                     try {
                        var11 = this.getRoutePool(var1, true);
                     } catch (Throwable var342) {
                        var10000 = var342;
                        var10001 = false;
                        break label2971;
                     }

                     WaitingThread var9 = null;

                     while(true) {
                        if (var344 != null) {
                           break label2964;
                        }

                        boolean var8;
                        try {
                           var8 = this.shutdown;
                        } catch (Throwable var338) {
                           var10000 = var338;
                           var10001 = false;
                           break;
                        }

                        boolean var7 = false;
                        if (!var8) {
                           var8 = true;
                        } else {
                           var8 = false;
                        }

                        try {
                           Asserts.check(var8, "Connection pool shut down");
                           var8 = this.log.isDebugEnabled();
                        } catch (Throwable var337) {
                           var10000 = var337;
                           var10001 = false;
                           break;
                        }

                        Log var345;
                        if (var8) {
                           try {
                              var345 = this.log;
                              StringBuilder var12 = new StringBuilder();
                              var12.append("[");
                              var12.append(var1);
                              var12.append("] total kept alive: ");
                              var12.append(this.freeConnections.size());
                              var12.append(", total issued: ");
                              var12.append(this.leasedConnections.size());
                              var12.append(", total allocated: ");
                              var12.append(this.numConnections);
                              var12.append(" out of ");
                              var12.append(this.maxTotalConnections);
                              var345.debug(var12.toString());
                           } catch (Throwable var336) {
                              var10000 = var336;
                              var10001 = false;
                              break;
                           }
                        }

                        BasicPoolEntry var346;
                        try {
                           var346 = this.getFreeEntry(var11, var2);
                        } catch (Throwable var335) {
                           var10000 = var335;
                           var10001 = false;
                           break;
                        }

                        if (var346 != null) {
                           var344 = var346;
                           break label2964;
                        }

                        label2953: {
                           try {
                              if (var11.getCapacity() <= 0) {
                                 break label2953;
                              }
                           } catch (Throwable var341) {
                              var10000 = var341;
                              var10001 = false;
                              break;
                           }

                           var7 = true;
                        }

                        try {
                           var8 = this.log.isDebugEnabled();
                        } catch (Throwable var334) {
                           var10000 = var334;
                           var10001 = false;
                           break;
                        }

                        StringBuilder var13;
                        if (var8) {
                           try {
                              var345 = this.log;
                              var13 = new StringBuilder();
                              var13.append("Available capacity: ");
                              var13.append(var11.getCapacity());
                              var13.append(" out of ");
                              var13.append(var11.getMaxEntries());
                              var13.append(" [");
                              var13.append(var1);
                              var13.append("][");
                              var13.append(var2);
                              var13.append("]");
                              var345.debug(var13.toString());
                           } catch (Throwable var333) {
                              var10000 = var333;
                              var10001 = false;
                              break;
                           }
                        }

                        WaitingThread var14;
                        RouteSpecificPool var347;
                        label2947: {
                           label2976: {
                              if (var7) {
                                 try {
                                    if (this.numConnections < this.maxTotalConnections) {
                                       var344 = this.createEntry(var11, this.operator);
                                       break label2976;
                                    }
                                 } catch (Throwable var340) {
                                    var10000 = var340;
                                    var10001 = false;
                                    break;
                                 }
                              }

                              if (var7) {
                                 label2975: {
                                    try {
                                       if (this.freeConnections.isEmpty()) {
                                          break label2975;
                                       }

                                       this.deleteLeastUsedEntry();
                                       var347 = this.getRoutePool(var1, true);
                                       var344 = this.createEntry(var347, this.operator);
                                    } catch (Throwable var339) {
                                       var10000 = var339;
                                       var10001 = false;
                                       break;
                                    }

                                    var14 = var9;
                                    break label2947;
                                 }
                              }

                              try {
                                 if (this.log.isDebugEnabled()) {
                                    var345 = this.log;
                                    var13 = new StringBuilder();
                                    var13.append("Need to wait for connection [");
                                    var13.append(var1);
                                    var13.append("][");
                                    var13.append(var2);
                                    var13.append("]");
                                    var345.debug(var13.toString());
                                 }
                              } catch (Throwable var332) {
                                 var10000 = var332;
                                 var10001 = false;
                                 break;
                              }

                              if (var9 == null) {
                                 try {
                                    var9 = this.newWaitingThread(this.poolLock.newCondition(), var11);
                                 } catch (Throwable var331) {
                                    var10000 = var331;
                                    var10001 = false;
                                    break;
                                 }

                                 try {
                                    var6.setWaitingThread(var9);
                                 } catch (Throwable var329) {
                                    var10000 = var329;
                                    var10001 = false;
                                    break label2966;
                                 }
                              }

                              try {
                                 var11.queueThread(var9);
                                 this.waitingThreads.add(var9);
                                 var8 = var9.await(var10);
                              } finally {
                                 try {
                                    var11.removeThread(var9);
                                    this.waitingThreads.remove(var9);
                                 } catch (Throwable var326) {
                                    var10000 = var326;
                                    var10001 = false;
                                    break label2966;
                                 }
                              }

                              var344 = var346;
                              var347 = var11;
                              var14 = var9;
                              if (!var8) {
                                 var344 = var346;
                                 var347 = var11;
                                 var14 = var9;
                                 if (var10 != null) {
                                    try {
                                       if (var10.getTime() <= System.currentTimeMillis()) {
                                          break label2965;
                                       }
                                    } catch (Throwable var330) {
                                       var10000 = var330;
                                       var10001 = false;
                                       break label2966;
                                    }

                                    var344 = var346;
                                    var347 = var11;
                                    var14 = var9;
                                 }
                              }
                              break label2947;
                           }

                           var347 = var11;
                           var14 = var9;
                        }

                        var11 = var347;
                        var9 = var14;
                     }
                  }

                  var343 = var10000;
                  break label2967;
               }

               this.poolLock.unlock();
               return var344;
            }

            label2898:
            try {
               throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
            } catch (Throwable var327) {
               var10000 = var327;
               var10001 = false;
               break label2898;
            }
         }

         var343 = var10000;
      }

      this.poolLock.unlock();
      throw var343;
   }

   protected BasicPoolEntry getFreeEntry(RouteSpecificPool var1, Object var2) {
      BasicPoolEntry var6 = null;
      this.poolLock.lock();
      boolean var3 = false;

      while(!var3) {
         Throwable var10000;
         label506: {
            BasicPoolEntry var5;
            boolean var10001;
            try {
               var5 = var1.allocEntry(var2);
            } catch (Throwable var49) {
               var10000 = var49;
               var10001 = false;
               break label506;
            }

            StringBuilder var7;
            Log var51;
            if (var5 != null) {
               label514: {
                  try {
                     if (this.log.isDebugEnabled()) {
                        var51 = this.log;
                        var7 = new StringBuilder();
                        var7.append("Getting free connection [");
                        var7.append(var1.getRoute());
                        var7.append("][");
                        var7.append(var2);
                        var7.append("]");
                        var51.debug(var7.toString());
                     }
                  } catch (Throwable var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label514;
                  }

                  label515: {
                     try {
                        this.freeConnections.remove(var5);
                        if (var5.isExpired(System.currentTimeMillis())) {
                           if (this.log.isDebugEnabled()) {
                              var51 = this.log;
                              var7 = new StringBuilder();
                              var7.append("Closing expired free connection [");
                              var7.append(var1.getRoute());
                              var7.append("][");
                              var7.append(var2);
                              var7.append("]");
                              var51.debug(var7.toString());
                           }
                           break label515;
                        }
                     } catch (Throwable var46) {
                        var10000 = var46;
                        var10001 = false;
                        break label514;
                     }

                     try {
                        this.leasedConnections.add(var5);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label514;
                     }

                     var3 = true;
                     var6 = var5;
                     continue;
                  }

                  try {
                     this.closeConnection(var5);
                     var1.dropEntry();
                     --this.numConnections;
                  } catch (Throwable var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label514;
                  }

                  var6 = var5;
               }
            } else {
               label517: {
                  boolean var4 = true;
                  var6 = var5;
                  var3 = var4;

                  try {
                     if (!this.log.isDebugEnabled()) {
                        continue;
                     }

                     var51 = this.log;
                     var7 = new StringBuilder();
                     var7.append("No free connections [");
                     var7.append(var1.getRoute());
                     var7.append("][");
                     var7.append(var2);
                     var7.append("]");
                     var51.debug(var7.toString());
                  } catch (Throwable var48) {
                     var10000 = var48;
                     var10001 = false;
                     break label517;
                  }

                  var6 = var5;
                  var3 = var4;
               }
            }
            continue;
         }

         Throwable var50 = var10000;
         this.poolLock.unlock();
         throw var50;
      }

      this.poolLock.unlock();
      return var6;
   }

   protected Lock getLock() {
      return this.poolLock;
   }

   public int getMaxTotalConnections() {
      return this.maxTotalConnections;
   }

   protected RouteSpecificPool getRoutePool(HttpRoute var1, boolean var2) {
      this.poolLock.lock();

      RouteSpecificPool var3;
      label80: {
         Throwable var10000;
         label84: {
            boolean var10001;
            RouteSpecificPool var4;
            try {
               var4 = (RouteSpecificPool)this.routeToPool.get(var1);
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               break label84;
            }

            var3 = var4;
            if (var4 != null) {
               break label80;
            }

            var3 = var4;
            if (!var2) {
               break label80;
            }

            label75:
            try {
               var3 = this.newRouteSpecificPool(var1);
               this.routeToPool.put(var1, var3);
               break label80;
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label75;
            }
         }

         Throwable var11 = var10000;
         this.poolLock.unlock();
         throw var11;
      }

      this.poolLock.unlock();
      return var3;
   }

   protected void handleLostEntry(HttpRoute var1) {
      this.poolLock.lock();

      try {
         RouteSpecificPool var2 = this.getRoutePool(var1, true);
         var2.dropEntry();
         if (var2.isUnused()) {
            this.routeToPool.remove(var1);
         }

         --this.numConnections;
         this.notifyWaitingThread(var2);
      } finally {
         this.poolLock.unlock();
      }

   }

   protected RouteSpecificPool newRouteSpecificPool(HttpRoute var1) {
      return new RouteSpecificPool(var1, this.connPerRoute);
   }

   protected WaitingThread newWaitingThread(Condition var1, RouteSpecificPool var2) {
      return new WaitingThread(var1, var2);
   }

   protected void notifyWaitingThread(RouteSpecificPool var1) {
      label482: {
         Throwable var10000;
         label484: {
            WaitingThread var46;
            boolean var10001;
            label490: {
               Log var2 = null;
               this.poolLock.lock();
               if (var1 != null) {
                  label489: {
                     try {
                        if (!var1.hasThread()) {
                           break label489;
                        }

                        if (this.log.isDebugEnabled()) {
                           var2 = this.log;
                           StringBuilder var3 = new StringBuilder();
                           var3.append("Notifying thread waiting on pool [");
                           var3.append(var1.getRoute());
                           var3.append("]");
                           var2.debug(var3.toString());
                        }
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label484;
                     }

                     try {
                        var46 = var1.nextThread();
                        break label490;
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label484;
                     }
                  }
               }

               label471: {
                  try {
                     if (this.waitingThreads.isEmpty()) {
                        break label471;
                     }

                     if (this.log.isDebugEnabled()) {
                        this.log.debug("Notifying thread waiting on any pool");
                     }
                  } catch (Throwable var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label484;
                  }

                  try {
                     var46 = (WaitingThread)this.waitingThreads.remove();
                     break label490;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label484;
                  }
               }

               var46 = var2;

               try {
                  if (!this.log.isDebugEnabled()) {
                     break label490;
                  }

                  this.log.debug("Notifying no-one, there are no waiting threads");
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label484;
               }

               var46 = var2;
            }

            if (var46 == null) {
               break label482;
            }

            label455:
            try {
               var46.wakeup();
               break label482;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break label455;
            }
         }

         Throwable var47 = var10000;
         this.poolLock.unlock();
         throw var47;
      }

      this.poolLock.unlock();
   }

   public PoolEntryRequest requestPoolEntry(final HttpRoute var1, final Object var2) {
      return new PoolEntryRequest(new WaitingThreadAborter()) {
         // $FF: synthetic field
         final WaitingThreadAborter val$aborter;

         {
            this.val$aborter = var2x;
         }

         public void abortRequest() {
            ConnPoolByRoute.this.poolLock.lock();

            try {
               this.val$aborter.abort();
            } finally {
               ConnPoolByRoute.this.poolLock.unlock();
            }

         }

         public BasicPoolEntry getPoolEntry(long var1x, TimeUnit var3) throws InterruptedException, ConnectionPoolTimeoutException {
            return ConnPoolByRoute.this.getEntryBlocking(var1, var2, var1x, var3, this.val$aborter);
         }
      };
   }

   public void setMaxTotalConnections(int var1) {
      this.poolLock.lock();

      try {
         this.maxTotalConnections = var1;
      } finally {
         this.poolLock.unlock();
      }

   }

   public void shutdown() {
      this.poolLock.lock();

      Throwable var10000;
      label838: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.shutdown;
         } catch (Throwable var95) {
            var10000 = var95;
            var10001 = false;
            break label838;
         }

         if (var1) {
            this.poolLock.unlock();
            return;
         }

         Iterator var2;
         try {
            this.shutdown = true;
            var2 = this.leasedConnections.iterator();
         } catch (Throwable var93) {
            var10000 = var93;
            var10001 = false;
            break label838;
         }

         BasicPoolEntry var3;
         while(true) {
            try {
               if (!var2.hasNext()) {
                  break;
               }

               var3 = (BasicPoolEntry)var2.next();
               var2.remove();
               this.closeConnection(var3);
            } catch (Throwable var94) {
               var10000 = var94;
               var10001 = false;
               break label838;
            }
         }

         try {
            var2 = this.freeConnections.iterator();
         } catch (Throwable var91) {
            var10000 = var91;
            var10001 = false;
            break label838;
         }

         while(true) {
            try {
               if (!var2.hasNext()) {
                  break;
               }

               var3 = (BasicPoolEntry)var2.next();
               var2.remove();
               if (this.log.isDebugEnabled()) {
                  Log var4 = this.log;
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Closing connection [");
                  var5.append(var3.getPlannedRoute());
                  var5.append("][");
                  var5.append(var3.getState());
                  var5.append("]");
                  var4.debug(var5.toString());
               }
            } catch (Throwable var92) {
               var10000 = var92;
               var10001 = false;
               break label838;
            }

            try {
               this.closeConnection(var3);
            } catch (Throwable var90) {
               var10000 = var90;
               var10001 = false;
               break label838;
            }
         }

         try {
            var2 = this.waitingThreads.iterator();
         } catch (Throwable var88) {
            var10000 = var88;
            var10001 = false;
            break label838;
         }

         while(true) {
            try {
               if (!var2.hasNext()) {
                  break;
               }

               WaitingThread var97 = (WaitingThread)var2.next();
               var2.remove();
               var97.wakeup();
            } catch (Throwable var89) {
               var10000 = var89;
               var10001 = false;
               break label838;
            }
         }

         try {
            this.routeToPool.clear();
         } catch (Throwable var87) {
            var10000 = var87;
            var10001 = false;
            break label838;
         }

         this.poolLock.unlock();
         return;
      }

      Throwable var96 = var10000;
      this.poolLock.unlock();
      throw var96;
   }
}
