package com.bumptech.glide.manager;

import android.util.Log;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class RequestTracker {
   private static final String TAG = "RequestTracker";
   private boolean isPaused;
   private final List pendingRequests = new ArrayList();
   private final Set requests = Collections.newSetFromMap(new WeakHashMap());

   void addRequest(Request var1) {
      this.requests.add(var1);
   }

   public boolean clearAndRemove(Request var1) {
      boolean var3 = true;
      if (var1 == null) {
         return true;
      } else {
         boolean var4 = this.requests.remove(var1);
         boolean var2 = var3;
         if (!this.pendingRequests.remove(var1)) {
            if (var4) {
               var2 = var3;
            } else {
               var2 = false;
            }
         }

         if (var2) {
            var1.clear();
         }

         return var2;
      }
   }

   public void clearRequests() {
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         this.clearAndRemove((Request)var1.next());
      }

      this.pendingRequests.clear();
   }

   public boolean isPaused() {
      return this.isPaused;
   }

   public void pauseAllRequests() {
      this.isPaused = true;
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(true) {
         Request var2;
         do {
            if (!var1.hasNext()) {
               return;
            }

            var2 = (Request)var1.next();
         } while(!var2.isRunning() && !var2.isComplete());

         var2.clear();
         this.pendingRequests.add(var2);
      }
   }

   public void pauseRequests() {
      this.isPaused = true;
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         Request var2 = (Request)var1.next();
         if (var2.isRunning()) {
            var2.pause();
            this.pendingRequests.add(var2);
         }
      }

   }

   public void restartRequests() {
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         Request var2 = (Request)var1.next();
         if (!var2.isComplete() && !var2.isCleared()) {
            var2.clear();
            if (!this.isPaused) {
               var2.begin();
            } else {
               this.pendingRequests.add(var2);
            }
         }
      }

   }

   public void resumeRequests() {
      this.isPaused = false;
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         Request var2 = (Request)var1.next();
         if (!var2.isComplete() && !var2.isRunning()) {
            var2.begin();
         }
      }

      this.pendingRequests.clear();
   }

   public void runRequest(Request var1) {
      this.requests.add(var1);
      if (!this.isPaused) {
         var1.begin();
      } else {
         var1.clear();
         if (Log.isLoggable("RequestTracker", 2)) {
            Log.v("RequestTracker", "Paused, delaying request");
         }

         this.pendingRequests.add(var1);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("{numRequests=");
      var1.append(this.requests.size());
      var1.append(", isPaused=");
      var1.append(this.isPaused);
      var1.append("}");
      return var1.toString();
   }
}
