package com.bumptech.glide.manager;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestManagerRetriever implements Callback {
   private static final RequestManagerRetriever.RequestManagerFactory DEFAULT_FACTORY = new RequestManagerRetriever.RequestManagerFactory() {
      public RequestManager build(Glide var1, Lifecycle var2, RequestManagerTreeNode var3, Context var4) {
         return new RequestManager(var1, var2, var3, var4);
      }
   };
   private static final String FRAGMENT_INDEX_KEY = "key";
   static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
   private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
   private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
   private static final String TAG = "RMRetriever";
   private volatile RequestManager applicationManager;
   private final RequestManagerRetriever.RequestManagerFactory factory;
   private final Handler handler;
   final Map pendingRequestManagerFragments = new HashMap();
   final Map pendingSupportRequestManagerFragments = new HashMap();
   private final Bundle tempBundle = new Bundle();
   private final ArrayMap tempViewToFragment = new ArrayMap();
   private final ArrayMap tempViewToSupportFragment = new ArrayMap();

   public RequestManagerRetriever(RequestManagerRetriever.RequestManagerFactory var1) {
      if (var1 == null) {
         var1 = DEFAULT_FACTORY;
      }

      this.factory = var1;
      this.handler = new Handler(Looper.getMainLooper(), this);
   }

   private static void assertNotDestroyed(Activity var0) {
      if (VERSION.SDK_INT >= 17) {
         if (var0.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
         }
      }
   }

   private static Activity findActivity(Context var0) {
      if (var0 instanceof Activity) {
         return (Activity)var0;
      } else {
         return var0 instanceof ContextWrapper ? findActivity(((ContextWrapper)var0).getBaseContext()) : null;
      }
   }

   @Deprecated
   private void findAllFragmentsWithViews(FragmentManager var1, ArrayMap var2) {
      if (VERSION.SDK_INT >= 26) {
         Iterator var4 = var1.getFragments().iterator();

         while(var4.hasNext()) {
            Fragment var3 = (Fragment)var4.next();
            if (var3.getView() != null) {
               var2.put(var3.getView(), var3);
               this.findAllFragmentsWithViews(var3.getChildFragmentManager(), var2);
            }
         }

      } else {
         this.findAllFragmentsWithViewsPreO(var1, var2);
      }
   }

   @Deprecated
   private void findAllFragmentsWithViewsPreO(FragmentManager var1, ArrayMap var2) {
      int var3 = 0;

      while(true) {
         this.tempBundle.putInt("key", var3);
         Fragment var4 = null;

         label25: {
            Fragment var5;
            try {
               var5 = var1.getFragment(this.tempBundle, "key");
            } catch (Exception var6) {
               break label25;
            }

            var4 = var5;
         }

         if (var4 == null) {
            return;
         }

         if (var4.getView() != null) {
            var2.put(var4.getView(), var4);
            if (VERSION.SDK_INT >= 17) {
               this.findAllFragmentsWithViews(var4.getChildFragmentManager(), var2);
            }
         }

         ++var3;
      }
   }

   private static void findAllSupportFragmentsWithViews(Collection var0, Map var1) {
      if (var0 != null) {
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            androidx.fragment.app.Fragment var2 = (androidx.fragment.app.Fragment)var3.next();
            if (var2 != null && var2.getView() != null) {
               var1.put(var2.getView(), var2);
               findAllSupportFragmentsWithViews(var2.getChildFragmentManager().getFragments(), var1);
            }
         }

      }
   }

   @Deprecated
   private Fragment findFragment(View var1, Activity var2) {
      this.tempViewToFragment.clear();
      this.findAllFragmentsWithViews(var2.getFragmentManager(), this.tempViewToFragment);
      Fragment var3 = null;
      View var4 = var2.findViewById(16908290);
      View var6 = var1;
      Fragment var5 = var3;

      while(true) {
         var3 = var5;
         if (var6.equals(var4)) {
            break;
         }

         var5 = (Fragment)this.tempViewToFragment.get(var6);
         if (var5 != null) {
            var3 = var5;
            break;
         }

         var3 = var5;
         if (!(var6.getParent() instanceof View)) {
            break;
         }

         var6 = (View)var6.getParent();
      }

      this.tempViewToFragment.clear();
      return var3;
   }

   private androidx.fragment.app.Fragment findSupportFragment(View var1, FragmentActivity var2) {
      this.tempViewToSupportFragment.clear();
      findAllSupportFragmentsWithViews(var2.getSupportFragmentManager().getFragments(), this.tempViewToSupportFragment);
      androidx.fragment.app.Fragment var3 = null;
      View var4 = var2.findViewById(16908290);
      View var6 = var1;
      androidx.fragment.app.Fragment var5 = var3;

      while(true) {
         var3 = var5;
         if (var6.equals(var4)) {
            break;
         }

         var5 = (androidx.fragment.app.Fragment)this.tempViewToSupportFragment.get(var6);
         if (var5 != null) {
            var3 = var5;
            break;
         }

         var3 = var5;
         if (!(var6.getParent() instanceof View)) {
            break;
         }

         var6 = (View)var6.getParent();
      }

      this.tempViewToSupportFragment.clear();
      return var3;
   }

   @Deprecated
   private RequestManager fragmentGet(Context var1, FragmentManager var2, Fragment var3, boolean var4) {
      RequestManagerFragment var5 = this.getRequestManagerFragment(var2, var3, var4);
      RequestManager var8 = var5.getRequestManager();
      RequestManager var6 = var8;
      if (var8 == null) {
         Glide var7 = Glide.get(var1);
         var6 = this.factory.build(var7, var5.getGlideLifecycle(), var5.getRequestManagerTreeNode(), var1);
         var5.setRequestManager(var6);
      }

      return var6;
   }

   private RequestManager getApplicationManager(Context var1) {
      if (this.applicationManager == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (this.applicationManager == null) {
                  Glide var2 = Glide.get(var1.getApplicationContext());
                  this.applicationManager = this.factory.build(var2, new ApplicationLifecycle(), new EmptyRequestManagerTreeNode(), var1.getApplicationContext());
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return this.applicationManager;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label141;
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
      } else {
         return this.applicationManager;
      }
   }

   private RequestManagerFragment getRequestManagerFragment(FragmentManager var1, Fragment var2, boolean var3) {
      RequestManagerFragment var5 = (RequestManagerFragment)var1.findFragmentByTag("com.bumptech.glide.manager");
      RequestManagerFragment var4 = var5;
      if (var5 == null) {
         var5 = (RequestManagerFragment)this.pendingRequestManagerFragments.get(var1);
         var4 = var5;
         if (var5 == null) {
            var4 = new RequestManagerFragment();
            var4.setParentFragmentHint(var2);
            if (var3) {
               var4.getGlideLifecycle().onStart();
            }

            this.pendingRequestManagerFragments.put(var1, var4);
            var1.beginTransaction().add(var4, "com.bumptech.glide.manager").commitAllowingStateLoss();
            this.handler.obtainMessage(1, var1).sendToTarget();
         }
      }

      return var4;
   }

   private SupportRequestManagerFragment getSupportRequestManagerFragment(androidx.fragment.app.FragmentManager var1, androidx.fragment.app.Fragment var2, boolean var3) {
      SupportRequestManagerFragment var5 = (SupportRequestManagerFragment)var1.findFragmentByTag("com.bumptech.glide.manager");
      SupportRequestManagerFragment var4 = var5;
      if (var5 == null) {
         var5 = (SupportRequestManagerFragment)this.pendingSupportRequestManagerFragments.get(var1);
         var4 = var5;
         if (var5 == null) {
            var4 = new SupportRequestManagerFragment();
            var4.setParentFragmentHint(var2);
            if (var3) {
               var4.getGlideLifecycle().onStart();
            }

            this.pendingSupportRequestManagerFragments.put(var1, var4);
            var1.beginTransaction().add(var4, "com.bumptech.glide.manager").commitAllowingStateLoss();
            this.handler.obtainMessage(2, var1).sendToTarget();
         }
      }

      return var4;
   }

   private static boolean isActivityVisible(Context var0) {
      Activity var1 = findActivity(var0);
      return var1 == null || !var1.isFinishing();
   }

   private RequestManager supportFragmentGet(Context var1, androidx.fragment.app.FragmentManager var2, androidx.fragment.app.Fragment var3, boolean var4) {
      SupportRequestManagerFragment var5 = this.getSupportRequestManagerFragment(var2, var3, var4);
      RequestManager var8 = var5.getRequestManager();
      RequestManager var6 = var8;
      if (var8 == null) {
         Glide var7 = Glide.get(var1);
         var6 = this.factory.build(var7, var5.getGlideLifecycle(), var5.getRequestManagerTreeNode(), var1);
         var5.setRequestManager(var6);
      }

      return var6;
   }

   public RequestManager get(Activity var1) {
      if (Util.isOnBackgroundThread()) {
         return this.get(var1.getApplicationContext());
      } else {
         assertNotDestroyed(var1);
         return this.fragmentGet(var1, var1.getFragmentManager(), (Fragment)null, isActivityVisible(var1));
      }
   }

   @Deprecated
   public RequestManager get(Fragment var1) {
      if (var1.getActivity() != null) {
         if (!Util.isOnBackgroundThread() && VERSION.SDK_INT >= 17) {
            FragmentManager var2 = var1.getChildFragmentManager();
            return this.fragmentGet(var1.getActivity(), var2, var1, var1.isVisible());
         } else {
            return this.get(var1.getActivity().getApplicationContext());
         }
      } else {
         throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
      }
   }

   public RequestManager get(Context var1) {
      if (var1 != null) {
         if (Util.isOnMainThread() && !(var1 instanceof Application)) {
            if (var1 instanceof FragmentActivity) {
               return this.get((FragmentActivity)var1);
            }

            if (var1 instanceof Activity) {
               return this.get((Activity)var1);
            }

            if (var1 instanceof ContextWrapper && ((ContextWrapper)var1).getBaseContext().getApplicationContext() != null) {
               return this.get(((ContextWrapper)var1).getBaseContext());
            }
         }

         return this.getApplicationManager(var1);
      } else {
         throw new IllegalArgumentException("You cannot start a load on a null Context");
      }
   }

   public RequestManager get(View var1) {
      if (Util.isOnBackgroundThread()) {
         return this.get(var1.getContext().getApplicationContext());
      } else {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var1.getContext(), "Unable to obtain a request manager for a view without a Context");
         Activity var2 = findActivity(var1.getContext());
         if (var2 == null) {
            return this.get(var1.getContext().getApplicationContext());
         } else if (var2 instanceof FragmentActivity) {
            androidx.fragment.app.Fragment var4 = this.findSupportFragment(var1, (FragmentActivity)var2);
            return var4 != null ? this.get(var4) : this.get((FragmentActivity)var2);
         } else {
            Fragment var3 = this.findFragment(var1, var2);
            return var3 == null ? this.get(var2) : this.get(var3);
         }
      }
   }

   public RequestManager get(androidx.fragment.app.Fragment var1) {
      Preconditions.checkNotNull(var1.getContext(), "You cannot start a load on a fragment before it is attached or after it is destroyed");
      if (Util.isOnBackgroundThread()) {
         return this.get(var1.getContext().getApplicationContext());
      } else {
         androidx.fragment.app.FragmentManager var2 = var1.getChildFragmentManager();
         return this.supportFragmentGet(var1.getContext(), var2, var1, var1.isVisible());
      }
   }

   public RequestManager get(FragmentActivity var1) {
      if (Util.isOnBackgroundThread()) {
         return this.get(var1.getApplicationContext());
      } else {
         assertNotDestroyed(var1);
         return this.supportFragmentGet(var1, var1.getSupportFragmentManager(), (androidx.fragment.app.Fragment)null, isActivityVisible(var1));
      }
   }

   @Deprecated
   RequestManagerFragment getRequestManagerFragment(Activity var1) {
      return this.getRequestManagerFragment(var1.getFragmentManager(), (Fragment)null, isActivityVisible(var1));
   }

   SupportRequestManagerFragment getSupportRequestManagerFragment(Context var1, androidx.fragment.app.FragmentManager var2) {
      return this.getSupportRequestManagerFragment(var2, (androidx.fragment.app.Fragment)null, isActivityVisible(var1));
   }

   public boolean handleMessage(Message var1) {
      boolean var3 = true;
      Object var5 = null;
      Object var4 = null;
      int var2 = var1.what;
      Object var6;
      if (var2 != 1) {
         if (var2 != 2) {
            var3 = false;
            var6 = var5;
         } else {
            androidx.fragment.app.FragmentManager var7 = (androidx.fragment.app.FragmentManager)var1.obj;
            var4 = var7;
            var6 = this.pendingSupportRequestManagerFragments.remove(var7);
         }
      } else {
         FragmentManager var8 = (FragmentManager)var1.obj;
         var4 = var8;
         var6 = this.pendingRequestManagerFragments.remove(var8);
      }

      if (var3 && var6 == null && Log.isLoggable("RMRetriever", 5)) {
         StringBuilder var9 = new StringBuilder();
         var9.append("Failed to remove expected request manager fragment, manager: ");
         var9.append(var4);
         Log.w("RMRetriever", var9.toString());
      }

      return var3;
   }

   public interface RequestManagerFactory {
      RequestManager build(Glide var1, Lifecycle var2, RequestManagerTreeNode var3, Context var4);
   }
}
