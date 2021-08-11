package com.karumi.dexter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

final class DexterInstance {
   private static final MultiplePermissionsListener EMPTY_LISTENER = new BaseMultiplePermissionsListener();
   private static final int PERMISSIONS_REQUEST_CODE = 42;
   private Activity activity;
   private final AndroidPermissionService androidPermissionService;
   private WeakReference context;
   private final IntentProvider intentProvider;
   private final AtomicBoolean isRequestingPermission;
   private final AtomicBoolean isShowingNativeDialog;
   private MultiplePermissionsListener listener;
   private final MultiplePermissionsReport multiplePermissionsReport;
   private final Collection pendingPermissions;
   private final Object pendingPermissionsMutex = new Object();
   private final AtomicBoolean rationaleAccepted;

   DexterInstance(Context var1, AndroidPermissionService var2, IntentProvider var3) {
      this.listener = EMPTY_LISTENER;
      this.androidPermissionService = var2;
      this.intentProvider = var3;
      this.pendingPermissions = new TreeSet();
      this.multiplePermissionsReport = new MultiplePermissionsReport();
      this.isRequestingPermission = new AtomicBoolean();
      this.rationaleAccepted = new AtomicBoolean();
      this.isShowingNativeDialog = new AtomicBoolean();
      this.setContext(var1);
   }

   private void checkMultiplePermissions(final MultiplePermissionsListener var1, final Collection var2, Thread var3) {
      this.checkNoDexterRequestOngoing();
      this.checkRequestSomePermission(var2);
      if (this.context.get() != null) {
         this.pendingPermissions.clear();
         this.pendingPermissions.addAll(var2);
         this.multiplePermissionsReport.clear();
         this.listener = new MultiplePermissionListenerThreadDecorator(var1, var3);
         if (this.isEveryPermissionGranted(var2, (Context)this.context.get())) {
            var3.execute(new Runnable() {
               public void run() {
                  MultiplePermissionsReport var1x = new MultiplePermissionsReport();
                  Iterator var2x = var2.iterator();

                  while(var2x.hasNext()) {
                     var1x.addGrantedPermissionResponse(PermissionGrantedResponse.from((String)var2x.next()));
                  }

                  DexterInstance.this.isRequestingPermission.set(false);
                  var1.onPermissionsChecked(var1x);
                  DexterInstance.this.listener = DexterInstance.EMPTY_LISTENER;
               }
            });
         } else {
            this.startTransparentActivityIfNeeded();
         }

         var3.loop();
      }
   }

   private void checkNoDexterRequestOngoing() {
      if (this.isRequestingPermission.getAndSet(true)) {
         throw new DexterException("Only one Dexter request at a time is allowed", DexterError.REQUEST_ONGOING);
      }
   }

   private void checkRequestSomePermission(Collection var1) {
      if (var1.isEmpty()) {
         throw new DexterException("Dexter has to be called with at least one permission", DexterError.NO_PERMISSIONS_REQUESTED);
      }
   }

   private int checkSelfPermission(Activity var1, String var2) {
      try {
         int var3 = this.androidPermissionService.checkSelfPermission(var1, var2);
         return var3;
      } catch (RuntimeException var4) {
         return -1;
      }
   }

   private void checkSinglePermission(PermissionListener var1, String var2, Thread var3) {
      this.checkMultiplePermissions(new MultiplePermissionsListenerToPermissionListenerAdapter(var1), Collections.singleton(var2), var3);
   }

   private DexterInstance.PermissionStates getPermissionStates(Collection var1) {
      DexterInstance.PermissionStates var3 = new DexterInstance.PermissionStates();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         String var4 = (String)var5.next();
         int var2 = this.checkSelfPermission(this.activity, var4);
         if (var2 != -2) {
            if (var2 != -1) {
               var3.addGrantedPermission(var4);
            } else {
               var3.addDeniedPermission(var4);
            }
         } else {
            var3.addImpossibleToGrantPermission(var4);
         }
      }

      return var3;
   }

   private void handleDeniedPermissions(Collection var1) {
      if (!var1.isEmpty()) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (this.androidPermissionService.shouldShowRequestPermissionRationale(this.activity, var4)) {
               var2.add(new PermissionRequest(var4));
            }
         }

         if (var2.isEmpty()) {
            this.requestPermissionsToSystem(var1);
         } else {
            if (!this.rationaleAccepted.get()) {
               PermissionRationaleToken var5 = new PermissionRationaleToken(this);
               this.listener.onPermissionRationaleShouldBeShown(var2, var5);
            }

         }
      }
   }

   private boolean isEveryPermissionGranted(Collection var1, Context var2) {
      Iterator var4 = var1.iterator();

      String var3;
      do {
         if (!var4.hasNext()) {
            return true;
         }

         var3 = (String)var4.next();
      } while(this.androidPermissionService.checkSelfPermission(var2, var3) == 0);

      return false;
   }

   private void onPermissionsChecked(Collection var1) {
      if (!this.pendingPermissions.isEmpty()) {
         Object var2 = this.pendingPermissionsMutex;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               this.pendingPermissions.removeAll(var1);
               if (this.pendingPermissions.isEmpty()) {
                  this.activity.finish();
                  this.activity = null;
                  this.isRequestingPermission.set(false);
                  this.rationaleAccepted.set(false);
                  this.isShowingNativeDialog.set(false);
                  MultiplePermissionsListener var15 = this.listener;
                  this.listener = EMPTY_LISTENER;
                  var15.onPermissionsChecked(this.multiplePermissionsReport);
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               return;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label133;
            }
         }

         while(true) {
            Throwable var16 = var10000;

            try {
               throw var16;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }
   }

   private void requestPermissionsToSystem(Collection var1) {
      if (!this.isShowingNativeDialog.get()) {
         this.androidPermissionService.requestPermissions(this.activity, (String[])var1.toArray(new String[var1.size()]), 42);
      }

      this.isShowingNativeDialog.set(true);
   }

   private void startTransparentActivityIfNeeded() {
      Context var1 = (Context)this.context.get();
      if (var1 != null) {
         Intent var2 = this.intentProvider.get(var1, DexterActivity.class);
         if (var1 instanceof Application) {
            var2.addFlags(268435456);
         }

         var1.startActivity(var2);
      }
   }

   private void updatePermissionsAsDenied(Collection var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         PermissionDeniedResponse var4 = PermissionDeniedResponse.from(var3, this.androidPermissionService.shouldShowRequestPermissionRationale(this.activity, var3) ^ true);
         this.multiplePermissionsReport.addDeniedPermissionResponse(var4);
      }

      this.onPermissionsChecked(var1);
   }

   private void updatePermissionsAsGranted(Collection var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         PermissionGrantedResponse var3 = PermissionGrantedResponse.from((String)var2.next());
         this.multiplePermissionsReport.addGrantedPermissionResponse(var3);
      }

      this.onPermissionsChecked(var1);
   }

   void checkPermission(PermissionListener var1, String var2, Thread var3) {
      this.checkSinglePermission(var1, var2, var3);
   }

   void checkPermissions(MultiplePermissionsListener var1, Collection var2, Thread var3) {
      this.checkMultiplePermissions(var1, var2, var3);
   }

   void onActivityDestroyed() {
      this.isRequestingPermission.set(false);
      this.listener = EMPTY_LISTENER;
   }

   void onActivityReady(Activity var1) {
      DexterInstance.PermissionStates var15;
      label148: {
         Throwable var10000;
         boolean var10001;
         label144: {
            this.activity = var1;
            Object var2 = this.pendingPermissionsMutex;
            synchronized(var2){}
            if (var1 != null) {
               try {
                  var15 = this.getPermissionStates(this.pendingPermissions);
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label144;
               }
            } else {
               var15 = null;
            }

            label139:
            try {
               break label148;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label139;
            }
         }

         while(true) {
            Throwable var16 = var10000;

            try {
               throw var16;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }

      if (var15 != null) {
         this.handleDeniedPermissions(var15.getDeniedPermissions());
         this.updatePermissionsAsDenied(var15.getImpossibleToGrantPermissions());
         this.updatePermissionsAsGranted(var15.getGrantedPermissions());
      }

   }

   void onCancelPermissionRequest() {
      this.rationaleAccepted.set(false);
      this.updatePermissionsAsDenied(this.pendingPermissions);
   }

   void onContinuePermissionRequest() {
      this.rationaleAccepted.set(true);
      this.requestPermissionsToSystem(this.pendingPermissions);
   }

   void onPermissionRequestDenied(Collection var1) {
      this.updatePermissionsAsDenied(var1);
   }

   void onPermissionRequestGranted(Collection var1) {
      this.updatePermissionsAsGranted(var1);
   }

   void setContext(Context var1) {
      this.context = new WeakReference(var1);
   }

   private final class PermissionStates {
      private final Collection deniedPermissions;
      private final Collection grantedPermissions;
      private final Collection impossibleToGrantPermissions;

      private PermissionStates() {
         this.deniedPermissions = new LinkedList();
         this.impossibleToGrantPermissions = new LinkedList();
         this.grantedPermissions = new LinkedList();
      }

      // $FF: synthetic method
      PermissionStates(Object var2) {
         this();
      }

      private void addDeniedPermission(String var1) {
         this.deniedPermissions.add(var1);
      }

      private void addGrantedPermission(String var1) {
         this.grantedPermissions.add(var1);
      }

      private void addImpossibleToGrantPermission(String var1) {
         this.impossibleToGrantPermissions.add(var1);
      }

      private Collection getDeniedPermissions() {
         return this.deniedPermissions;
      }

      private Collection getGrantedPermissions() {
         return this.grantedPermissions;
      }

      public Collection getImpossibleToGrantPermissions() {
         return this.impossibleToGrantPermissions;
      }
   }
}
