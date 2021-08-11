package androidx.databinding;

public class BaseObservable implements Observable {
   private transient PropertyChangeRegistry mCallbacks;

   public void addOnPropertyChangedCallback(Observable.OnPropertyChangedCallback var1) {
      synchronized(this){}

      label136: {
         Throwable var10000;
         boolean var10001;
         label131: {
            try {
               if (this.mCallbacks == null) {
                  this.mCallbacks = new PropertyChangeRegistry();
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label131;
            }

            label128:
            try {
               break label136;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label128;
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

      this.mCallbacks.add(var1);
   }

   public void notifyChange() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.mCallbacks == null) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label137;
         }

         this.mCallbacks.notifyCallbacks(this, 0, (Object)null);
         return;
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public void notifyPropertyChanged(int var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.mCallbacks == null) {
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label137;
         }

         this.mCallbacks.notifyCallbacks(this, var1, (Object)null);
         return;
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

   public void removeOnPropertyChangedCallback(Observable.OnPropertyChangedCallback var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.mCallbacks == null) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label137;
         }

         try {
            ;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label137;
         }

         this.mCallbacks.remove(var1);
         return;
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
}
