package com.bumptech.glide.request;

public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {
   private volatile Request full;
   private RequestCoordinator.RequestState fullState;
   private boolean isRunningDuringBegin;
   private final RequestCoordinator parent;
   private final Object requestLock;
   private volatile Request thumb;
   private RequestCoordinator.RequestState thumbState;

   public ThumbnailRequestCoordinator(Object var1, RequestCoordinator var2) {
      this.fullState = RequestCoordinator.RequestState.CLEARED;
      this.thumbState = RequestCoordinator.RequestState.CLEARED;
      this.requestLock = var1;
      this.parent = var2;
   }

   private boolean parentCanNotifyCleared() {
      RequestCoordinator var1 = this.parent;
      return var1 == null || var1.canNotifyCleared(this);
   }

   private boolean parentCanNotifyStatusChanged() {
      RequestCoordinator var1 = this.parent;
      return var1 == null || var1.canNotifyStatusChanged(this);
   }

   private boolean parentCanSetImage() {
      RequestCoordinator var1 = this.parent;
      return var1 == null || var1.canSetImage(this);
   }

   public void begin() {
      Object var1 = this.requestLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      Throwable var2;
      label406: {
         try {
            this.isRunningDuringBegin = true;
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label406;
         }

         label407: {
            label397: {
               try {
                  if (this.fullState != RequestCoordinator.RequestState.SUCCESS && this.thumbState != RequestCoordinator.RequestState.RUNNING) {
                     this.thumbState = RequestCoordinator.RequestState.RUNNING;
                     this.thumb.begin();
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label397;
               }

               label394:
               try {
                  if (this.isRunningDuringBegin && this.fullState != RequestCoordinator.RequestState.RUNNING) {
                     this.fullState = RequestCoordinator.RequestState.RUNNING;
                     this.full.begin();
                  }
                  break label407;
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label394;
               }
            }

            var2 = var10000;

            try {
               this.isRunningDuringBegin = false;
               throw var2;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break label406;
            }
         }

         label390:
         try {
            this.isRunningDuringBegin = false;
            return;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label390;
         }
      }

      while(true) {
         var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean canNotifyCleared(Request var1) {
      Object var3 = this.requestLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label158: {
         boolean var2;
         label157: {
            label156: {
               try {
                  if (this.parentCanNotifyCleared() && var1.equals(this.full) && this.fullState != RequestCoordinator.RequestState.PAUSED) {
                     break label156;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label158;
               }

               var2 = false;
               break label157;
            }

            var2 = true;
         }

         label146:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label146;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean canNotifyStatusChanged(Request var1) {
      Object var3 = this.requestLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label158: {
         boolean var2;
         label157: {
            label156: {
               try {
                  if (this.parentCanNotifyStatusChanged() && var1.equals(this.full) && !this.isAnyResourceSet()) {
                     break label156;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label158;
               }

               var2 = false;
               break label157;
            }

            var2 = true;
         }

         label146:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label146;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean canSetImage(Request var1) {
      Object var3 = this.requestLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label166: {
         boolean var2;
         label165: {
            label164: {
               try {
                  if (this.parentCanSetImage() && (var1.equals(this.full) || this.fullState != RequestCoordinator.RequestState.SUCCESS)) {
                     break label164;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label166;
               }

               var2 = false;
               break label165;
            }

            var2 = true;
         }

         label153:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label153;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public void clear() {
      // $FF: Couldn't be decompiled
   }

   public RequestCoordinator getRoot() {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label150: {
         Object var1;
         label144: {
            try {
               if (this.parent != null) {
                  var1 = this.parent.getRoot();
                  break label144;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label150;
            }

            var1 = this;
         }

         label136:
         try {
            return (RequestCoordinator)var1;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label136;
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
   }

   public boolean isAnyResourceSet() {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label151: {
         boolean var1;
         label150: {
            label149: {
               try {
                  if (!this.thumb.isAnyResourceSet() && !this.full.isAnyResourceSet()) {
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
                  if (this.fullState == RequestCoordinator.RequestState.CLEARED) {
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
                  if (this.fullState == RequestCoordinator.RequestState.SUCCESS) {
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
      if (!(var1 instanceof ThumbnailRequestCoordinator)) {
         return false;
      } else {
         ThumbnailRequestCoordinator var2 = (ThumbnailRequestCoordinator)var1;
         if (this.full == null) {
            if (var2.full != null) {
               return false;
            }
         } else if (!this.full.isEquivalentTo(var2.full)) {
            return false;
         }

         if (this.thumb == null) {
            if (var2.thumb == null) {
               return true;
            }
         } else if (this.thumb.isEquivalentTo(var2.thumb)) {
            return true;
         }

         return false;
      }
   }

   public boolean isRunning() {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var1;
         label133: {
            label132: {
               try {
                  if (this.fullState == RequestCoordinator.RequestState.RUNNING) {
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

   public void onRequestFailed(Request var1) {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label197: {
         try {
            if (!var1.equals(this.full)) {
               this.thumbState = RequestCoordinator.RequestState.FAILED;
               return;
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label197;
         }

         try {
            this.fullState = RequestCoordinator.RequestState.FAILED;
            if (this.parent != null) {
               this.parent.onRequestFailed(this);
            }
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label197;
         }

         label187:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label187;
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

   public void onRequestSuccess(Request var1) {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label290: {
         try {
            if (var1.equals(this.thumb)) {
               this.thumbState = RequestCoordinator.RequestState.SUCCESS;
               return;
            }
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label290;
         }

         try {
            this.fullState = RequestCoordinator.RequestState.SUCCESS;
            if (this.parent != null) {
               this.parent.onRequestSuccess(this);
            }
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label290;
         }

         try {
            if (!this.thumbState.isComplete()) {
               this.thumb.clear();
            }
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label290;
         }

         label274:
         try {
            return;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label274;
         }
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

   public void pause() {
      Object var1 = this.requestLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label196: {
         try {
            if (!this.thumbState.isComplete()) {
               this.thumbState = RequestCoordinator.RequestState.PAUSED;
               this.thumb.pause();
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label196;
         }

         try {
            if (!this.fullState.isComplete()) {
               this.fullState = RequestCoordinator.RequestState.PAUSED;
               this.full.pause();
            }
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label196;
         }

         label186:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label186;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public void setRequests(Request var1, Request var2) {
      this.full = var1;
      this.thumb = var2;
   }
}
