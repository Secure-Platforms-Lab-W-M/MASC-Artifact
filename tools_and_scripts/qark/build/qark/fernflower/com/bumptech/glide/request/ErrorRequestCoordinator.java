package com.bumptech.glide.request;

public final class ErrorRequestCoordinator implements RequestCoordinator, Request {
   private volatile Request error;
   private RequestCoordinator.RequestState errorState;
   private final RequestCoordinator parent;
   private volatile Request primary;
   private RequestCoordinator.RequestState primaryState;
   private final Object requestLock;

   public ErrorRequestCoordinator(Object var1, RequestCoordinator var2) {
      this.primaryState = RequestCoordinator.RequestState.CLEARED;
      this.errorState = RequestCoordinator.RequestState.CLEARED;
      this.requestLock = var1;
      this.parent = var2;
   }

   private boolean isValidRequest(Request var1) {
      return var1.equals(this.primary) || this.primaryState == RequestCoordinator.RequestState.FAILED && var1.equals(this.error);
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
      label122: {
         try {
            if (this.primaryState != RequestCoordinator.RequestState.RUNNING) {
               this.primaryState = RequestCoordinator.RequestState.RUNNING;
               this.primary.begin();
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

   public boolean canNotifyCleared(Request var1) {
      Object var3 = this.requestLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label150: {
         boolean var2;
         label149: {
            label148: {
               try {
                  if (this.parentCanNotifyCleared() && this.isValidRequest(var1)) {
                     break label148;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label150;
               }

               var2 = false;
               break label149;
            }

            var2 = true;
         }

         label139:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label139;
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
      label150: {
         boolean var2;
         label149: {
            label148: {
               try {
                  if (this.parentCanNotifyStatusChanged() && this.isValidRequest(var1)) {
                     break label148;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label150;
               }

               var2 = false;
               break label149;
            }

            var2 = true;
         }

         label139:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label139;
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
      label150: {
         boolean var2;
         label149: {
            label148: {
               try {
                  if (this.parentCanSetImage() && this.isValidRequest(var1)) {
                     break label148;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label150;
               }

               var2 = false;
               break label149;
            }

            var2 = true;
         }

         label139:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label139;
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
      Object var1 = this.requestLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            this.primaryState = RequestCoordinator.RequestState.CLEARED;
            this.primary.clear();
            if (this.errorState != RequestCoordinator.RequestState.CLEARED) {
               this.errorState = RequestCoordinator.RequestState.CLEARED;
               this.error.clear();
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
                  if (!this.primary.isAnyResourceSet() && !this.error.isAnyResourceSet()) {
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
      label150: {
         boolean var1;
         label149: {
            label148: {
               try {
                  if (this.primaryState == RequestCoordinator.RequestState.CLEARED && this.errorState == RequestCoordinator.RequestState.CLEARED) {
                     break label148;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label150;
               }

               var1 = false;
               break label149;
            }

            var1 = true;
         }

         label139:
         try {
            return var1;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label139;
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
      label151: {
         boolean var1;
         label150: {
            label149: {
               try {
                  if (this.primaryState != RequestCoordinator.RequestState.SUCCESS && this.errorState != RequestCoordinator.RequestState.SUCCESS) {
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

   public boolean isEquivalentTo(Request var1) {
      boolean var2 = var1 instanceof ErrorRequestCoordinator;
      boolean var3 = false;
      if (var2) {
         ErrorRequestCoordinator var4 = (ErrorRequestCoordinator)var1;
         var2 = var3;
         if (this.primary.isEquivalentTo(var4.primary)) {
            var2 = var3;
            if (this.error.isEquivalentTo(var4.error)) {
               var2 = true;
            }
         }

         return var2;
      } else {
         return false;
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
                  if (this.primaryState != RequestCoordinator.RequestState.RUNNING && this.errorState != RequestCoordinator.RequestState.RUNNING) {
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

   public void onRequestFailed(Request var1) {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label307: {
         label311: {
            try {
               if (!var1.equals(this.error)) {
                  this.primaryState = RequestCoordinator.RequestState.FAILED;
                  if (this.errorState != RequestCoordinator.RequestState.RUNNING) {
                     this.errorState = RequestCoordinator.RequestState.RUNNING;
                     this.error.begin();
                  }
                  break label311;
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label307;
            }

            try {
               this.errorState = RequestCoordinator.RequestState.FAILED;
               if (this.parent != null) {
                  this.parent.onRequestFailed(this);
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label307;
            }

            try {
               return;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label307;
            }
         }

         label294:
         try {
            return;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label294;
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

   public void onRequestSuccess(Request var1) {
      Object var2 = this.requestLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label303: {
         label304: {
            try {
               if (var1.equals(this.primary)) {
                  this.primaryState = RequestCoordinator.RequestState.SUCCESS;
                  break label304;
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label303;
            }

            try {
               if (var1.equals(this.error)) {
                  this.errorState = RequestCoordinator.RequestState.SUCCESS;
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label303;
            }
         }

         try {
            if (this.parent != null) {
               this.parent.onRequestSuccess(this);
            }
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label303;
         }

         label286:
         try {
            return;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label286;
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
            if (this.primaryState == RequestCoordinator.RequestState.RUNNING) {
               this.primaryState = RequestCoordinator.RequestState.PAUSED;
               this.primary.pause();
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label196;
         }

         try {
            if (this.errorState == RequestCoordinator.RequestState.RUNNING) {
               this.errorState = RequestCoordinator.RequestState.PAUSED;
               this.error.pause();
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
      this.primary = var1;
      this.error = var2;
   }
}
