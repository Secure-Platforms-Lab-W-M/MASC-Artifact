package com.karumi.dexter;

import android.app.Activity;
import android.content.Context;
import com.karumi.dexter.listener.EmptyPermissionRequestErrorListener;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class Dexter implements DexterBuilder, DexterBuilder.MultiPermissionListener, DexterBuilder.Permission, DexterBuilder.SinglePermissionListener {
   private static DexterInstance instance;
   private PermissionRequestErrorListener errorListener = new EmptyPermissionRequestErrorListener();
   private MultiplePermissionsListener listener = new BaseMultiplePermissionsListener();
   private Collection permissions;
   private boolean shouldExecuteOnSameThread = false;

   private Dexter(Activity var1) {
      initialize(var1);
   }

   private Thread getThread() {
      return this.shouldExecuteOnSameThread ? ThreadFactory.makeSameThread() : ThreadFactory.makeMainThread();
   }

   private static void initialize(Context var0) {
      DexterInstance var1 = instance;
      if (var1 == null) {
         instance = new DexterInstance(var0, new AndroidPermissionService(), new IntentProvider());
      } else {
         var1.setContext(var0);
      }
   }

   static void onActivityDestroyed() {
      DexterInstance var0 = instance;
      if (var0 != null) {
         var0.onActivityDestroyed();
      }

   }

   static void onActivityReady(Activity var0) {
      DexterInstance var1 = instance;
      if (var1 != null) {
         var1.onActivityReady(var0);
      }

   }

   static void onPermissionsRequested(Collection var0, Collection var1) {
      DexterInstance var2 = instance;
      if (var2 != null) {
         var2.onPermissionRequestGranted(var0);
         instance.onPermissionRequestDenied(var1);
      }

   }

   public static DexterBuilder.Permission withActivity(Activity var0) {
      return new Dexter(var0);
   }

   public void check() {
      try {
         Thread var1 = this.getThread();
         instance.checkPermissions(this.listener, this.permissions, var1);
      } catch (DexterException var2) {
         this.errorListener.onError(var2.error);
      }
   }

   public DexterBuilder onSameThread() {
      this.shouldExecuteOnSameThread = true;
      return this;
   }

   public DexterBuilder withErrorListener(PermissionRequestErrorListener var1) {
      this.errorListener = var1;
      return this;
   }

   public DexterBuilder withListener(MultiplePermissionsListener var1) {
      this.listener = var1;
      return this;
   }

   public DexterBuilder withListener(PermissionListener var1) {
      this.listener = new MultiplePermissionsListenerToPermissionListenerAdapter(var1);
      return this;
   }

   public DexterBuilder.SinglePermissionListener withPermission(String var1) {
      this.permissions = Collections.singletonList(var1);
      return this;
   }

   public DexterBuilder.MultiPermissionListener withPermissions(Collection var1) {
      this.permissions = new ArrayList(var1);
      return this;
   }

   public DexterBuilder.MultiPermissionListener withPermissions(String... var1) {
      this.permissions = Arrays.asList(var1);
      return this;
   }
}
