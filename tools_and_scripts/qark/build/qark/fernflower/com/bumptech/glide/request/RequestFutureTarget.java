package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestFutureTarget implements FutureTarget, RequestListener {
   private static final RequestFutureTarget.Waiter DEFAULT_WAITER = new RequestFutureTarget.Waiter();
   private final boolean assertBackgroundThread;
   private GlideException exception;
   private final int height;
   private boolean isCancelled;
   private boolean loadFailed;
   private Request request;
   private Object resource;
   private boolean resultReceived;
   private final RequestFutureTarget.Waiter waiter;
   private final int width;

   public RequestFutureTarget(int var1, int var2) {
      this(var1, var2, true, DEFAULT_WAITER);
   }

   RequestFutureTarget(int var1, int var2, boolean var3, RequestFutureTarget.Waiter var4) {
      this.width = var1;
      this.height = var2;
      this.assertBackgroundThread = var3;
      this.waiter = var4;
   }

   private Object doGet(Long var1) throws ExecutionException, InterruptedException, TimeoutException {
      synchronized(this){}

      Throwable var10000;
      label1797: {
         boolean var10001;
         try {
            if (this.assertBackgroundThread && !this.isDone()) {
               Util.assertBackgroundThread();
            }
         } catch (Throwable var187) {
            var10000 = var187;
            var10001 = false;
            break label1797;
         }

         label1787: {
            label1798: {
               Object var188;
               try {
                  if (this.isCancelled) {
                     break label1798;
                  }

                  if (this.loadFailed) {
                     break label1787;
                  }

                  if (this.resultReceived) {
                     var188 = this.resource;
                     return var188;
                  }
               } catch (Throwable var186) {
                  var10000 = var186;
                  var10001 = false;
                  break label1797;
               }

               if (var1 == null) {
                  try {
                     this.waiter.waitForTimeout(this, 0L);
                  } catch (Throwable var183) {
                     var10000 = var183;
                     var10001 = false;
                     break label1797;
                  }
               } else {
                  label1771: {
                     long var2;
                     long var4;
                     try {
                        if (var1 <= 0L) {
                           break label1771;
                        }

                        var2 = System.currentTimeMillis();
                        var4 = var1 + var2;
                     } catch (Throwable var185) {
                        var10000 = var185;
                        var10001 = false;
                        break label1797;
                     }

                     while(true) {
                        try {
                           if (this.isDone()) {
                              break;
                           }
                        } catch (Throwable var184) {
                           var10000 = var184;
                           var10001 = false;
                           break label1797;
                        }

                        if (var2 >= var4) {
                           break;
                        }

                        try {
                           this.waiter.waitForTimeout(this, var4 - var2);
                           var2 = System.currentTimeMillis();
                        } catch (Throwable var182) {
                           var10000 = var182;
                           var10001 = false;
                           break label1797;
                        }
                     }
                  }
               }

               label1754: {
                  label1753: {
                     label1752: {
                        try {
                           if (Thread.interrupted()) {
                              break label1753;
                           }

                           if (this.loadFailed) {
                              break label1754;
                           }

                           if (!this.isCancelled) {
                              if (!this.resultReceived) {
                                 break label1752;
                              }

                              var188 = this.resource;
                              return var188;
                           }
                        } catch (Throwable var181) {
                           var10000 = var181;
                           var10001 = false;
                           break label1797;
                        }

                        try {
                           throw new CancellationException();
                        } catch (Throwable var176) {
                           var10000 = var176;
                           var10001 = false;
                           break label1797;
                        }
                     }

                     try {
                        throw new TimeoutException();
                     } catch (Throwable var175) {
                        var10000 = var175;
                        var10001 = false;
                        break label1797;
                     }
                  }

                  try {
                     throw new InterruptedException();
                  } catch (Throwable var178) {
                     var10000 = var178;
                     var10001 = false;
                     break label1797;
                  }
               }

               try {
                  throw new ExecutionException(this.exception);
               } catch (Throwable var177) {
                  var10000 = var177;
                  var10001 = false;
                  break label1797;
               }
            }

            try {
               throw new CancellationException();
            } catch (Throwable var180) {
               var10000 = var180;
               var10001 = false;
               break label1797;
            }
         }

         label1733:
         try {
            throw new ExecutionException(this.exception);
         } catch (Throwable var179) {
            var10000 = var179;
            var10001 = false;
            break label1733;
         }
      }

      Throwable var189 = var10000;
      throw var189;
   }

   public boolean cancel(boolean var1) {
      Request var2 = null;
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label301: {
         try {
            if (this.isDone()) {
               return false;
            }
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label301;
         }

         try {
            this.isCancelled = true;
            this.waiter.notifyAll(this);
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label301;
         }

         if (var1) {
            try {
               var2 = this.request;
               this.request = null;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label301;
            }
         }

         try {
            ;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label301;
         }

         if (var2 != null) {
            var2.clear();
         }

         return true;
      }

      while(true) {
         Throwable var33 = var10000;

         try {
            throw var33;
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            continue;
         }
      }
   }

   public Object get() throws InterruptedException, ExecutionException {
      try {
         Object var1 = this.doGet((Long)null);
         return var1;
      } catch (TimeoutException var2) {
         throw new AssertionError(var2);
      }
   }

   public Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      return this.doGet(var3.toMillis(var1));
   }

   public Request getRequest() {
      synchronized(this){}

      Request var1;
      try {
         var1 = this.request;
      } finally {
         ;
      }

      return var1;
   }

   public void getSize(SizeReadyCallback var1) {
      var1.onSizeReady(this.width, this.height);
   }

   public boolean isCancelled() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.isCancelled;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isDone() {
      synchronized(this){}
      boolean var4 = false;

      boolean var1;
      label54: {
         try {
            var4 = true;
            if (this.isCancelled) {
               var4 = false;
               break label54;
            }

            if (this.resultReceived) {
               var4 = false;
               break label54;
            }

            var1 = this.loadFailed;
            var4 = false;
         } finally {
            if (var4) {
               ;
            }
         }

         if (!var1) {
            var1 = false;
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   public void onDestroy() {
   }

   public void onLoadCleared(Drawable var1) {
   }

   public void onLoadFailed(Drawable var1) {
      synchronized(this){}
   }

   public boolean onLoadFailed(GlideException var1, Object var2, Target var3, boolean var4) {
      synchronized(this){}

      try {
         this.loadFailed = true;
         this.exception = var1;
         this.waiter.notifyAll(this);
      } finally {
         ;
      }

      return false;
   }

   public void onLoadStarted(Drawable var1) {
   }

   public void onResourceReady(Object var1, Transition var2) {
      synchronized(this){}
   }

   public boolean onResourceReady(Object var1, Object var2, Target var3, DataSource var4, boolean var5) {
      synchronized(this){}

      try {
         this.resultReceived = true;
         this.resource = var1;
         this.waiter.notifyAll(this);
      } finally {
         ;
      }

      return false;
   }

   public void onStart() {
   }

   public void onStop() {
   }

   public void removeCallback(SizeReadyCallback var1) {
   }

   public void setRequest(Request var1) {
      synchronized(this){}

      try {
         this.request = var1;
      } finally {
         ;
      }

   }

   static class Waiter {
      void notifyAll(Object var1) {
         var1.notifyAll();
      }

      void waitForTimeout(Object var1, long var2) throws InterruptedException {
         var1.wait(var2);
      }
   }
}
