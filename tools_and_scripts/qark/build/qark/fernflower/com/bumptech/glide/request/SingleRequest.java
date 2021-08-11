package com.bumptech.glide.request;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public final class SingleRequest implements Request, SizeReadyCallback, ResourceCallback {
   private static final String GLIDE_TAG = "Glide";
   private static final boolean IS_VERBOSE_LOGGABLE = Log.isLoggable("Request", 2);
   private static final String TAG = "Request";
   private final TransitionFactory animationFactory;
   private final Executor callbackExecutor;
   private final Context context;
   private volatile Engine engine;
   private Drawable errorDrawable;
   private Drawable fallbackDrawable;
   private final GlideContext glideContext;
   private int height;
   private boolean isCallingCallbacks;
   private Engine.LoadStatus loadStatus;
   private final Object model;
   private final int overrideHeight;
   private final int overrideWidth;
   private Drawable placeholderDrawable;
   private final Priority priority;
   private final RequestCoordinator requestCoordinator;
   private final List requestListeners;
   private final Object requestLock;
   private final BaseRequestOptions requestOptions;
   private RuntimeException requestOrigin;
   private Resource resource;
   private long startTime;
   private final StateVerifier stateVerifier;
   private SingleRequest.Status status;
   private final String tag;
   private final Target target;
   private final RequestListener targetListener;
   private final Class transcodeClass;
   private int width;

   private SingleRequest(Context var1, GlideContext var2, Object var3, Object var4, Class var5, BaseRequestOptions var6, int var7, int var8, Priority var9, Target var10, RequestListener var11, List var12, RequestCoordinator var13, Engine var14, TransitionFactory var15, Executor var16) {
      String var17;
      if (IS_VERBOSE_LOGGABLE) {
         var17 = String.valueOf(super.hashCode());
      } else {
         var17 = null;
      }

      this.tag = var17;
      this.stateVerifier = StateVerifier.newInstance();
      this.requestLock = var3;
      this.context = var1;
      this.glideContext = var2;
      this.model = var4;
      this.transcodeClass = var5;
      this.requestOptions = var6;
      this.overrideWidth = var7;
      this.overrideHeight = var8;
      this.priority = var9;
      this.target = var10;
      this.targetListener = var11;
      this.requestListeners = var12;
      this.requestCoordinator = var13;
      this.engine = var14;
      this.animationFactory = var15;
      this.callbackExecutor = var16;
      this.status = SingleRequest.Status.PENDING;
      if (this.requestOrigin == null && var2.isLoggingRequestOriginsEnabled()) {
         this.requestOrigin = new RuntimeException("Glide request origin trace");
      }

   }

   private void assertNotCallingCallbacks() {
      if (this.isCallingCallbacks) {
         throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you're trying to start a fallback request when a load fails, use RequestBuilder#error(RequestBuilder). Otherwise consider posting your into() or clear() calls to the main thread using a Handler instead.");
      }
   }

   private boolean canNotifyCleared() {
      RequestCoordinator var1 = this.requestCoordinator;
      return var1 == null || var1.canNotifyCleared(this);
   }

   private boolean canNotifyStatusChanged() {
      RequestCoordinator var1 = this.requestCoordinator;
      return var1 == null || var1.canNotifyStatusChanged(this);
   }

   private boolean canSetResource() {
      RequestCoordinator var1 = this.requestCoordinator;
      return var1 == null || var1.canSetImage(this);
   }

   private void cancel() {
      this.assertNotCallingCallbacks();
      this.stateVerifier.throwIfRecycled();
      this.target.removeCallback(this);
      Engine.LoadStatus var1 = this.loadStatus;
      if (var1 != null) {
         var1.cancel();
         this.loadStatus = null;
      }

   }

   private Drawable getErrorDrawable() {
      if (this.errorDrawable == null) {
         Drawable var1 = this.requestOptions.getErrorPlaceholder();
         this.errorDrawable = var1;
         if (var1 == null && this.requestOptions.getErrorId() > 0) {
            this.errorDrawable = this.loadDrawable(this.requestOptions.getErrorId());
         }
      }

      return this.errorDrawable;
   }

   private Drawable getFallbackDrawable() {
      if (this.fallbackDrawable == null) {
         Drawable var1 = this.requestOptions.getFallbackDrawable();
         this.fallbackDrawable = var1;
         if (var1 == null && this.requestOptions.getFallbackId() > 0) {
            this.fallbackDrawable = this.loadDrawable(this.requestOptions.getFallbackId());
         }
      }

      return this.fallbackDrawable;
   }

   private Drawable getPlaceholderDrawable() {
      if (this.placeholderDrawable == null) {
         Drawable var1 = this.requestOptions.getPlaceholderDrawable();
         this.placeholderDrawable = var1;
         if (var1 == null && this.requestOptions.getPlaceholderId() > 0) {
            this.placeholderDrawable = this.loadDrawable(this.requestOptions.getPlaceholderId());
         }
      }

      return this.placeholderDrawable;
   }

   private boolean isFirstReadyResource() {
      RequestCoordinator var1 = this.requestCoordinator;
      return var1 == null || !var1.getRoot().isAnyResourceSet();
   }

   private Drawable loadDrawable(int var1) {
      Theme var2;
      if (this.requestOptions.getTheme() != null) {
         var2 = this.requestOptions.getTheme();
      } else {
         var2 = this.context.getTheme();
      }

      return DrawableDecoderCompat.getDrawable(this.glideContext, var1, var2);
   }

   private void logV(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1);
      var2.append(" this: ");
      var2.append(this.tag);
      Log.v("Request", var2.toString());
   }

   private static int maybeApplySizeMultiplier(int var0, float var1) {
      return var0 == Integer.MIN_VALUE ? var0 : Math.round((float)var0 * var1);
   }

   private void notifyLoadFailed() {
      RequestCoordinator var1 = this.requestCoordinator;
      if (var1 != null) {
         var1.onRequestFailed(this);
      }

   }

   private void notifyLoadSuccess() {
      RequestCoordinator var1 = this.requestCoordinator;
      if (var1 != null) {
         var1.onRequestSuccess(this);
      }

   }

   public static SingleRequest obtain(Context var0, GlideContext var1, Object var2, Object var3, Class var4, BaseRequestOptions var5, int var6, int var7, Priority var8, Target var9, RequestListener var10, List var11, RequestCoordinator var12, Engine var13, TransitionFactory var14, Executor var15) {
      return new SingleRequest(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15);
   }

   private void onLoadFailed(GlideException var1, int var2) {
      this.stateVerifier.throwIfRecycled();
      Object var5 = this.requestLock;
      synchronized(var5){}

      Throwable var10000;
      boolean var10001;
      Throwable var163;
      label1417: {
         int var3;
         try {
            var1.setOrigin(this.requestOrigin);
            var3 = this.glideContext.getLogLevel();
         } catch (Throwable var162) {
            var10000 = var162;
            var10001 = false;
            break label1417;
         }

         if (var3 <= var2) {
            try {
               StringBuilder var6 = new StringBuilder();
               var6.append("Load failed for ");
               var6.append(this.model);
               var6.append(" with size [");
               var6.append(this.width);
               var6.append("x");
               var6.append(this.height);
               var6.append("]");
               Log.w("Glide", var6.toString(), var1);
            } catch (Throwable var161) {
               var10000 = var161;
               var10001 = false;
               break label1417;
            }

            if (var3 <= 4) {
               try {
                  var1.logRootCauses("Glide");
               } catch (Throwable var160) {
                  var10000 = var160;
                  var10001 = false;
                  break label1417;
               }
            }
         }

         try {
            this.loadStatus = null;
            this.status = SingleRequest.Status.FAILED;
         } catch (Throwable var159) {
            var10000 = var159;
            var10001 = false;
            break label1417;
         }

         boolean var4 = true;

         try {
            this.isCallingCallbacks = true;
         } catch (Throwable var158) {
            var10000 = var158;
            var10001 = false;
            break label1417;
         }

         boolean var165 = false;
         boolean var164 = false;

         label1418: {
            label1419: {
               label1392: {
                  Iterator var166;
                  try {
                     if (this.requestListeners == null) {
                        break label1392;
                     }

                     var166 = this.requestListeners.iterator();
                  } catch (Throwable var157) {
                     var10000 = var157;
                     var10001 = false;
                     break label1419;
                  }

                  while(true) {
                     var165 = var164;

                     try {
                        if (!var166.hasNext()) {
                           break;
                        }

                        var164 |= ((RequestListener)var166.next()).onLoadFailed(var1, this.model, this.target, this.isFirstReadyResource());
                     } catch (Throwable var156) {
                        var10000 = var156;
                        var10001 = false;
                        break label1419;
                     }
                  }
               }

               label1380: {
                  label1379: {
                     try {
                        if (this.targetListener != null && this.targetListener.onLoadFailed(var1, this.model, this.target, this.isFirstReadyResource())) {
                           break label1379;
                        }
                     } catch (Throwable var155) {
                        var10000 = var155;
                        var10001 = false;
                        break label1419;
                     }

                     var164 = false;
                     break label1380;
                  }

                  var164 = var4;
               }

               if (var164 | var165) {
                  break label1418;
               }

               label1369:
               try {
                  this.setErrorPlaceholder();
                  break label1418;
               } catch (Throwable var154) {
                  var10000 = var154;
                  var10001 = false;
                  break label1369;
               }
            }

            var163 = var10000;

            try {
               this.isCallingCallbacks = false;
               throw var163;
            } catch (Throwable var152) {
               var10000 = var152;
               var10001 = false;
               break label1417;
            }
         }

         label1364:
         try {
            this.isCallingCallbacks = false;
            this.notifyLoadFailed();
            return;
         } catch (Throwable var153) {
            var10000 = var153;
            var10001 = false;
            break label1364;
         }
      }

      while(true) {
         var163 = var10000;

         try {
            throw var163;
         } catch (Throwable var151) {
            var10000 = var151;
            var10001 = false;
            continue;
         }
      }
   }

   private void onResourceReady(Resource var1, Object var2, DataSource var3) {
      boolean var7 = this.isFirstReadyResource();
      this.status = SingleRequest.Status.COMPLETE;
      this.resource = var1;
      if (this.glideContext.getLogLevel() <= 3) {
         StringBuilder var28 = new StringBuilder();
         var28.append("Finished loading ");
         var28.append(var2.getClass().getSimpleName());
         var28.append(" from ");
         var28.append(var3);
         var28.append(" for ");
         var28.append(this.model);
         var28.append(" with size [");
         var28.append(this.width);
         var28.append("x");
         var28.append(this.height);
         var28.append("] in ");
         var28.append(LogTime.getElapsedMillis(this.startTime));
         var28.append(" ms");
         Log.d("Glide", var28.toString());
      }

      boolean var6 = true;
      this.isCallingCallbacks = true;

      label314: {
         Throwable var10000;
         label320: {
            boolean var4;
            boolean var5;
            boolean var10001;
            label312: {
               label321: {
                  Iterator var29;
                  try {
                     if (this.requestListeners == null) {
                        break label321;
                     }

                     var29 = this.requestListeners.iterator();
                  } catch (Throwable var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label320;
                  }

                  var4 = false;

                  while(true) {
                     var5 = var4;

                     try {
                        if (!var29.hasNext()) {
                           break label312;
                        }

                        var4 |= ((RequestListener)var29.next()).onResourceReady(var2, this.model, this.target, var3, var7);
                     } catch (Throwable var26) {
                        var10000 = var26;
                        var10001 = false;
                        break label320;
                     }
                  }
               }

               var5 = false;
            }

            label297: {
               label296: {
                  try {
                     if (this.targetListener == null || !this.targetListener.onResourceReady(var2, this.model, this.target, var3, var7)) {
                        break label296;
                     }
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label320;
                  }

                  var4 = var6;
                  break label297;
               }

               var4 = false;
            }

            if (var5 | var4) {
               break label314;
            }

            label287:
            try {
               Transition var31 = this.animationFactory.build(var3, var7);
               this.target.onResourceReady(var2, var31);
               break label314;
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label287;
            }
         }

         Throwable var30 = var10000;
         this.isCallingCallbacks = false;
         throw var30;
      }

      this.isCallingCallbacks = false;
      this.notifyLoadSuccess();
   }

   private void setErrorPlaceholder() {
      if (this.canNotifyStatusChanged()) {
         Drawable var2 = null;
         if (this.model == null) {
            var2 = this.getFallbackDrawable();
         }

         Drawable var1 = var2;
         if (var2 == null) {
            var1 = this.getErrorDrawable();
         }

         var2 = var1;
         if (var1 == null) {
            var2 = this.getPlaceholderDrawable();
         }

         this.target.onLoadFailed(var2);
      }
   }

   public void begin() {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label1456: {
         label1457: {
            try {
               this.assertNotCallingCallbacks();
               this.stateVerifier.throwIfRecycled();
               this.startTime = LogTime.getLogTime();
               if (this.model == null) {
                  if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                     this.width = this.overrideWidth;
                     this.height = this.overrideHeight;
                  }
                  break label1457;
               }
            } catch (Throwable var159) {
               var10000 = var159;
               var10001 = false;
               break label1456;
            }

            label1443: {
               try {
                  if (this.status != SingleRequest.Status.RUNNING) {
                     if (this.status == SingleRequest.Status.COMPLETE) {
                        this.onResourceReady(this.resource, DataSource.MEMORY_CACHE);
                        return;
                     }
                     break label1443;
                  }
               } catch (Throwable var158) {
                  var10000 = var158;
                  var10001 = false;
                  break label1456;
               }

               try {
                  throw new IllegalArgumentException("Cannot restart a running request");
               } catch (Throwable var157) {
                  var10000 = var157;
                  var10001 = false;
                  break label1456;
               }
            }

            label1458: {
               try {
                  this.status = SingleRequest.Status.WAITING_FOR_SIZE;
                  if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                     this.onSizeReady(this.overrideWidth, this.overrideHeight);
                     break label1458;
                  }
               } catch (Throwable var156) {
                  var10000 = var156;
                  var10001 = false;
                  break label1456;
               }

               try {
                  this.target.getSize(this);
               } catch (Throwable var155) {
                  var10000 = var155;
                  var10001 = false;
                  break label1456;
               }
            }

            label1423: {
               try {
                  if (this.status != SingleRequest.Status.RUNNING && this.status != SingleRequest.Status.WAITING_FOR_SIZE) {
                     break label1423;
                  }
               } catch (Throwable var154) {
                  var10000 = var154;
                  var10001 = false;
                  break label1456;
               }

               try {
                  if (this.canNotifyStatusChanged()) {
                     this.target.onLoadStarted(this.getPlaceholderDrawable());
                  }
               } catch (Throwable var153) {
                  var10000 = var153;
                  var10001 = false;
                  break label1456;
               }
            }

            try {
               if (IS_VERBOSE_LOGGABLE) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("finished run method in ");
                  var3.append(LogTime.getElapsedMillis(this.startTime));
                  this.logV(var3.toString());
               }
            } catch (Throwable var152) {
               var10000 = var152;
               var10001 = false;
               break label1456;
            }

            try {
               return;
            } catch (Throwable var151) {
               var10000 = var151;
               var10001 = false;
               break label1456;
            }
         }

         byte var1;
         label1408: {
            label1407: {
               try {
                  if (this.getFallbackDrawable() == null) {
                     break label1407;
                  }
               } catch (Throwable var150) {
                  var10000 = var150;
                  var10001 = false;
                  break label1456;
               }

               var1 = 3;
               break label1408;
            }

            var1 = 5;
         }

         label1401:
         try {
            this.onLoadFailed(new GlideException("Received null model"), var1);
            return;
         } catch (Throwable var149) {
            var10000 = var149;
            var10001 = false;
            break label1401;
         }
      }

      while(true) {
         Throwable var160 = var10000;

         try {
            throw var160;
         } catch (Throwable var148) {
            var10000 = var148;
            var10001 = false;
            continue;
         }
      }
   }

   public void clear() {
      Resource var1 = null;
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label414: {
         try {
            this.assertNotCallingCallbacks();
            this.stateVerifier.throwIfRecycled();
            if (this.status == SingleRequest.Status.CLEARED) {
               return;
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label414;
         }

         try {
            this.cancel();
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label414;
         }

         try {
            if (this.resource != null) {
               var1 = this.resource;
               this.resource = null;
            }
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label414;
         }

         try {
            if (this.canNotifyCleared()) {
               this.target.onLoadCleared(this.getPlaceholderDrawable());
            }
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label414;
         }

         try {
            this.status = SingleRequest.Status.CLEARED;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break label414;
         }

         if (var1 != null) {
            this.engine.release(var1);
         }

         return;
      }

      while(true) {
         Throwable var45 = var10000;

         try {
            throw var45;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            continue;
         }
      }
   }

   public Object getLock() {
      this.stateVerifier.throwIfRecycled();
      return this.requestLock;
   }

   public boolean isAnyResourceSet() {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var1;
         label133: {
            label132: {
               try {
                  if (this.status == SingleRequest.Status.COMPLETE) {
                     break label132;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label134;
               }

               var1 = false;
               break label133;
            }

            var1 = true;
         }

         label126:
         try {
            return var1;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label126;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isCleared() {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var1;
         label133: {
            label132: {
               try {
                  if (this.status == SingleRequest.Status.CLEARED) {
                     break label132;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label134;
               }

               var1 = false;
               break label133;
            }

            var1 = true;
         }

         label126:
         try {
            return var1;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label126;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isComplete() {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var1;
         label133: {
            label132: {
               try {
                  if (this.status == SingleRequest.Status.COMPLETE) {
                     break label132;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label134;
               }

               var1 = false;
               break label133;
            }

            var1 = true;
         }

         label126:
         try {
            return var1;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label126;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isEquivalentTo(Request var1) {
      if (!(var1 instanceof SingleRequest)) {
         return false;
      } else {
         Object var12 = this.requestLock;
         synchronized(var12){}

         int var2;
         int var4;
         int var5;
         Object var8;
         Class var9;
         BaseRequestOptions var10;
         Priority var11;
         Throwable var10000;
         boolean var10001;
         label657: {
            label659: {
               label648: {
                  try {
                     var4 = this.overrideWidth;
                     var5 = this.overrideHeight;
                     var8 = this.model;
                     var9 = this.transcodeClass;
                     var10 = this.requestOptions;
                     var11 = this.priority;
                     if (this.requestListeners != null) {
                        var2 = this.requestListeners.size();
                        break label648;
                     }
                  } catch (Throwable var58) {
                     var10000 = var58;
                     var10001 = false;
                     break label659;
                  }

                  var2 = 0;
               }

               label640:
               try {
                  break label657;
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label640;
               }
            }

            while(true) {
               Throwable var59 = var10000;

               try {
                  throw var59;
               } catch (Throwable var53) {
                  var10000 = var53;
                  var10001 = false;
                  continue;
               }
            }
         }

         SingleRequest var62 = (SingleRequest)var1;
         Object var60 = var62.requestLock;
         synchronized(var60){}

         label660: {
            int var3;
            int var6;
            int var7;
            Object var13;
            Class var14;
            BaseRequestOptions var15;
            Priority var16;
            label632: {
               try {
                  var6 = var62.overrideWidth;
                  var7 = var62.overrideHeight;
                  var13 = var62.model;
                  var14 = var62.transcodeClass;
                  var15 = var62.requestOptions;
                  var16 = var62.priority;
                  if (var62.requestListeners != null) {
                     var3 = var62.requestListeners.size();
                     break label632;
                  }
               } catch (Throwable var56) {
                  var10000 = var56;
                  var10001 = false;
                  break label660;
               }

               var3 = 0;
            }

            label624:
            try {
               return var4 == var6 && var5 == var7 && Util.bothModelsNullEquivalentOrEquals(var8, var13) && var9.equals(var14) && var10.equals(var15) && var11 == var16 && var2 == var3;
            } catch (Throwable var55) {
               var10000 = var55;
               var10001 = false;
               break label624;
            }
         }

         while(true) {
            Throwable var61 = var10000;

            try {
               throw var61;
            } catch (Throwable var54) {
               var10000 = var54;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public boolean isRunning() {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label151: {
         boolean var1;
         label150: {
            label149: {
               try {
                  if (this.status != SingleRequest.Status.RUNNING && this.status != SingleRequest.Status.WAITING_FOR_SIZE) {
                     break label149;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label151;
               }

               var1 = true;
               break label150;
            }

            var1 = false;
         }

         label140:
         try {
            return var1;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label140;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public void onLoadFailed(GlideException var1) {
      this.onLoadFailed(var1, 5);
   }

   public void onResourceReady(Resource param1, DataSource param2) {
      // $FF: Couldn't be decompiled
   }

   public void onSizeReady(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void pause() {
      Object var1 = this.requestLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.isRunning()) {
               this.clear();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   private static enum Status {
      CLEARED,
      COMPLETE,
      FAILED,
      PENDING,
      RUNNING,
      WAITING_FOR_SIZE;

      static {
         SingleRequest.Status var0 = new SingleRequest.Status("CLEARED", 5);
         CLEARED = var0;
      }
   }
}
