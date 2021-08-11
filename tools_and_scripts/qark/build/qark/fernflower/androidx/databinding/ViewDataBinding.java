package androidx.databinding;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Choreographer.FrameCallback;
import android.view.View.OnAttachStateChangeListener;
import androidx.databinding.library.R.id;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewbinding.ViewBinding;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public abstract class ViewDataBinding extends BaseObservable implements ViewBinding {
   private static final int BINDING_NUMBER_START;
   public static final String BINDING_TAG_PREFIX = "binding_";
   private static final ViewDataBinding.CreateWeakListener CREATE_LIST_LISTENER;
   private static final ViewDataBinding.CreateWeakListener CREATE_LIVE_DATA_LISTENER;
   private static final ViewDataBinding.CreateWeakListener CREATE_MAP_LISTENER;
   private static final ViewDataBinding.CreateWeakListener CREATE_PROPERTY_LISTENER;
   private static final int HALTED = 2;
   private static final int REBIND = 1;
   private static final CallbackRegistry.NotifierCallback REBIND_NOTIFIER;
   private static final int REBOUND = 3;
   private static final OnAttachStateChangeListener ROOT_REATTACHED_LISTENER;
   static int SDK_INT;
   private static final boolean USE_CHOREOGRAPHER;
   private static final ReferenceQueue sReferenceQueue;
   protected final DataBindingComponent mBindingComponent;
   private Choreographer mChoreographer;
   private ViewDataBinding mContainingBinding;
   private final FrameCallback mFrameCallback;
   private boolean mInLiveDataRegisterObserver;
   private boolean mIsExecutingPendingBindings;
   private LifecycleOwner mLifecycleOwner;
   private ViewDataBinding.WeakListener[] mLocalFieldObservers;
   private ViewDataBinding.OnStartListener mOnStartListener;
   private boolean mPendingRebind;
   private CallbackRegistry mRebindCallbacks;
   private boolean mRebindHalted;
   private final Runnable mRebindRunnable;
   private final View mRoot;
   private Handler mUIThreadHandler;

   static {
      SDK_INT = VERSION.SDK_INT;
      BINDING_NUMBER_START = "binding_".length();
      boolean var0;
      if (SDK_INT >= 16) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_CHOREOGRAPHER = var0;
      CREATE_PROPERTY_LISTENER = new ViewDataBinding.CreateWeakListener() {
         public ViewDataBinding.WeakListener create(ViewDataBinding var1, int var2) {
            return (new ViewDataBinding.WeakPropertyListener(var1, var2)).getListener();
         }
      };
      CREATE_LIST_LISTENER = new ViewDataBinding.CreateWeakListener() {
         public ViewDataBinding.WeakListener create(ViewDataBinding var1, int var2) {
            return (new ViewDataBinding.WeakListListener(var1, var2)).getListener();
         }
      };
      CREATE_MAP_LISTENER = new ViewDataBinding.CreateWeakListener() {
         public ViewDataBinding.WeakListener create(ViewDataBinding var1, int var2) {
            return (new ViewDataBinding.WeakMapListener(var1, var2)).getListener();
         }
      };
      CREATE_LIVE_DATA_LISTENER = new ViewDataBinding.CreateWeakListener() {
         public ViewDataBinding.WeakListener create(ViewDataBinding var1, int var2) {
            return (new ViewDataBinding.LiveDataListener(var1, var2)).getListener();
         }
      };
      REBIND_NOTIFIER = new CallbackRegistry.NotifierCallback() {
         public void onNotifyCallback(OnRebindCallback var1, ViewDataBinding var2, int var3, Void var4) {
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 == 3) {
                     var1.onBound(var2);
                  }
               } else {
                  var1.onCanceled(var2);
               }
            } else {
               if (!var1.onPreBind(var2)) {
                  var2.mRebindHalted = true;
               }

            }
         }
      };
      sReferenceQueue = new ReferenceQueue();
      if (VERSION.SDK_INT < 19) {
         ROOT_REATTACHED_LISTENER = null;
      } else {
         ROOT_REATTACHED_LISTENER = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View var1) {
               ViewDataBinding.getBinding(var1).mRebindRunnable.run();
               var1.removeOnAttachStateChangeListener(this);
            }

            public void onViewDetachedFromWindow(View var1) {
            }
         };
      }
   }

   protected ViewDataBinding(DataBindingComponent var1, View var2, int var3) {
      this.mRebindRunnable = new Runnable() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      };
      this.mPendingRebind = false;
      this.mRebindHalted = false;
      this.mBindingComponent = var1;
      this.mLocalFieldObservers = new ViewDataBinding.WeakListener[var3];
      this.mRoot = var2;
      if (Looper.myLooper() != null) {
         if (USE_CHOREOGRAPHER) {
            this.mChoreographer = Choreographer.getInstance();
            this.mFrameCallback = new FrameCallback() {
               public void doFrame(long var1) {
                  ViewDataBinding.this.mRebindRunnable.run();
               }
            };
         } else {
            this.mFrameCallback = null;
            this.mUIThreadHandler = new Handler(Looper.myLooper());
         }
      } else {
         throw new IllegalStateException("DataBinding must be created in view's UI Thread");
      }
   }

   protected ViewDataBinding(Object var1, View var2, int var3) {
      this(checkAndCastToBindingComponent(var1), var2, var3);
   }

   // $FF: synthetic method
   static boolean access$202(ViewDataBinding var0, boolean var1) {
      var0.mPendingRebind = var1;
      return var1;
   }

   // $FF: synthetic method
   static void access$300() {
      processReferenceQueue();
   }

   // $FF: synthetic method
   static View access$400(ViewDataBinding var0) {
      return var0.mRoot;
   }

   // $FF: synthetic method
   static OnAttachStateChangeListener access$500() {
      return ROOT_REATTACHED_LISTENER;
   }

   protected static ViewDataBinding bind(Object var0, View var1, int var2) {
      return DataBindingUtil.bind(checkAndCastToBindingComponent(var0), var1, var2);
   }

   private static DataBindingComponent checkAndCastToBindingComponent(Object var0) {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof DataBindingComponent) {
         return (DataBindingComponent)var0;
      } else {
         throw new IllegalArgumentException("The provided bindingComponent parameter must be an instance of DataBindingComponent. See  https://issuetracker.google.com/issues/116541301 for details of why this parameter is not defined as DataBindingComponent");
      }
   }

   private void executeBindingsInternal() {
      if (this.mIsExecutingPendingBindings) {
         this.requestRebind();
      } else if (this.hasPendingBindings()) {
         this.mIsExecutingPendingBindings = true;
         this.mRebindHalted = false;
         CallbackRegistry var1 = this.mRebindCallbacks;
         if (var1 != null) {
            var1.notifyCallbacks(this, 1, (Object)null);
            if (this.mRebindHalted) {
               this.mRebindCallbacks.notifyCallbacks(this, 2, (Object)null);
            }
         }

         if (!this.mRebindHalted) {
            this.executeBindings();
            var1 = this.mRebindCallbacks;
            if (var1 != null) {
               var1.notifyCallbacks(this, 3, (Object)null);
            }
         }

         this.mIsExecutingPendingBindings = false;
      }
   }

   protected static void executeBindingsOn(ViewDataBinding var0) {
      var0.executeBindingsInternal();
   }

   private static int findIncludeIndex(String var0, int var1, ViewDataBinding.IncludedLayouts var2, int var3) {
      CharSequence var4 = var0.subSequence(var0.indexOf(47) + 1, var0.length() - 2);
      String[] var5 = var2.layouts[var3];

      for(var3 = var5.length; var1 < var3; ++var1) {
         if (TextUtils.equals(var4, var5[var1])) {
            return var1;
         }
      }

      return -1;
   }

   private static int findLastMatching(ViewGroup var0, int var1) {
      String var7 = (String)var0.getChildAt(var1).getTag();
      String var8 = var7.substring(0, var7.length() - 1);
      int var4 = var8.length();
      int var5 = var0.getChildCount();

      int var2;
      int var3;
      for(var2 = var1++; var1 < var5; var2 = var3) {
         View var6 = var0.getChildAt(var1);
         String var9;
         if (var6.getTag() instanceof String) {
            var9 = (String)var6.getTag();
         } else {
            var9 = null;
         }

         var3 = var2;
         if (var9 != null) {
            var3 = var2;
            if (var9.startsWith(var8)) {
               if (var9.length() == var7.length() && var9.charAt(var9.length() - 1) == '0') {
                  return var2;
               }

               var3 = var2;
               if (isNumeric(var9, var4)) {
                  var3 = var1;
               }
            }
         }

         ++var1;
      }

      return var2;
   }

   static ViewDataBinding getBinding(View var0) {
      return var0 != null ? (ViewDataBinding)var0.getTag(id.dataBinding) : null;
   }

   public static int getBuildSdkInt() {
      return SDK_INT;
   }

   protected static int getColorFromResource(View var0, int var1) {
      return VERSION.SDK_INT >= 23 ? var0.getContext().getColor(var1) : var0.getResources().getColor(var1);
   }

   protected static ColorStateList getColorStateListFromResource(View var0, int var1) {
      return VERSION.SDK_INT >= 23 ? var0.getContext().getColorStateList(var1) : var0.getResources().getColorStateList(var1);
   }

   protected static Drawable getDrawableFromResource(View var0, int var1) {
      return VERSION.SDK_INT >= 21 ? var0.getContext().getDrawable(var1) : var0.getResources().getDrawable(var1);
   }

   protected static Object getFrom(Map var0, Object var1) {
      return var0 == null ? null : var0.get(var1);
   }

   protected static byte getFromArray(byte[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : 0;
   }

   protected static char getFromArray(char[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : '\u0000';
   }

   protected static double getFromArray(double[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : 0.0D;
   }

   protected static float getFromArray(float[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : 0.0F;
   }

   protected static int getFromArray(int[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : 0;
   }

   protected static long getFromArray(long[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : 0L;
   }

   protected static Object getFromArray(Object[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : null;
   }

   protected static short getFromArray(short[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : 0;
   }

   protected static boolean getFromArray(boolean[] var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.length ? var0[var1] : false;
   }

   protected static int getFromList(SparseIntArray var0, int var1) {
      return var0 != null && var1 >= 0 ? var0.get(var1) : 0;
   }

   protected static long getFromList(SparseLongArray var0, int var1) {
      return var0 != null && var1 >= 0 ? var0.get(var1) : 0L;
   }

   protected static Object getFromList(LongSparseArray var0, int var1) {
      return var0 != null && var1 >= 0 ? var0.get((long)var1) : null;
   }

   protected static Object getFromList(SparseArray var0, int var1) {
      return var0 != null && var1 >= 0 ? var0.get(var1) : null;
   }

   protected static Object getFromList(androidx.collection.LongSparseArray var0, int var1) {
      return var0 != null && var1 >= 0 ? var0.get((long)var1) : null;
   }

   protected static Object getFromList(List var0, int var1) {
      return var0 != null && var1 >= 0 && var1 < var0.size() ? var0.get(var1) : null;
   }

   protected static boolean getFromList(SparseBooleanArray var0, int var1) {
      return var0 != null && var1 >= 0 ? var0.get(var1) : false;
   }

   private void handleFieldChange(int var1, Object var2, int var3) {
      if (!this.mInLiveDataRegisterObserver) {
         if (this.onFieldChange(var1, var2, var3)) {
            this.requestRebind();
         }

      }
   }

   protected static ViewDataBinding inflateInternal(LayoutInflater var0, int var1, ViewGroup var2, boolean var3, Object var4) {
      return DataBindingUtil.inflate(var0, var1, var2, var3, checkAndCastToBindingComponent(var4));
   }

   private static boolean isNumeric(String var0, int var1) {
      int var2 = var0.length();
      if (var2 == var1) {
         return false;
      } else {
         while(var1 < var2) {
            if (!Character.isDigit(var0.charAt(var1))) {
               return false;
            }

            ++var1;
         }

         return true;
      }
   }

   private static void mapBindings(DataBindingComponent var0, View var1, Object[] var2, ViewDataBinding.IncludedLayouts var3, SparseIntArray var4, boolean var5) {
      if (getBinding(var1) == null) {
         Object var15 = var1.getTag();
         String var24;
         if (var15 instanceof String) {
            var24 = (String)var15;
         } else {
            var24 = null;
         }

         boolean var7 = false;
         boolean var6;
         int var19;
         int var20;
         if (var5 && var24 != null && var24.startsWith("layout")) {
            var19 = var24.lastIndexOf(95);
            if (var19 > 0 && isNumeric(var24, var19 + 1)) {
               var19 = parseTagInt(var24, var19 + 1);
               if (var2[var19] == null) {
                  var2[var19] = var1;
               }

               if (var3 == null) {
                  var19 = -1;
               }

               var7 = true;
            } else {
               var19 = -1;
            }

            var6 = var7;
            var20 = var19;
         } else if (var24 != null && var24.startsWith("binding_")) {
            var19 = parseTagInt(var24, BINDING_NUMBER_START);
            if (var2[var19] == null) {
               var2[var19] = var1;
            }

            if (var3 == null) {
               var19 = -1;
            }

            boolean var8 = true;
            var20 = var19;
            var6 = var8;
         } else {
            var6 = false;
            var20 = -1;
         }

         if (!var6) {
            var19 = var1.getId();
            if (var19 > 0 && var4 != null) {
               var19 = var4.get(var19, -1);
               if (var19 >= 0 && var2[var19] == null) {
                  var2[var19] = var1;
               }
            }
         }

         if (var1 instanceof ViewGroup) {
            ViewGroup var18 = (ViewGroup)var1;
            int var9 = var18.getChildCount();
            int var21 = 0;

            int var10;
            for(var19 = 0; var19 < var9; var19 = var10) {
               View var16;
               boolean var22;
               label92: {
                  var16 = var18.getChildAt(var19);
                  if (var20 >= 0 && var16.getTag() instanceof String) {
                     String var17 = (String)var16.getTag();
                     if (var17.endsWith("_0") && var17.startsWith("layout") && var17.indexOf(47) > 0) {
                        int var13 = findIncludeIndex(var17, var21, var3, var20);
                        if (var13 >= 0) {
                           var22 = true;
                           int var23 = var13 + 1;
                           int var12 = var3.indexes[var20][var13];
                           var13 = var3.layoutIds[var20][var13];
                           var21 = findLastMatching(var18, var19);
                           if (var21 == var19) {
                              var2[var12] = DataBindingUtil.bind(var0, var16, var13);
                              var21 = var19;
                              var19 = var23;
                              break label92;
                           }

                           int var14 = var21 - var19 + 1;
                           View[] var25 = new View[var14];

                           for(var21 = 0; var21 < var14; ++var21) {
                              var25[var21] = var18.getChildAt(var19 + var21);
                           }

                           var2[var12] = DataBindingUtil.bind(var0, var25, var13);
                           var21 = var19 + (var14 - 1);
                           var19 = var23;
                           break label92;
                        }
                     }
                  }

                  boolean var11 = false;
                  var10 = var21;
                  var21 = var19;
                  var19 = var10;
                  var22 = var11;
               }

               if (!var22) {
                  mapBindings(var0, var16, var2, var3, var4, false);
               }

               var10 = var21 + 1;
               var21 = var19;
            }
         }

      }
   }

   protected static Object[] mapBindings(DataBindingComponent var0, View var1, int var2, ViewDataBinding.IncludedLayouts var3, SparseIntArray var4) {
      Object[] var5 = new Object[var2];
      mapBindings(var0, var1, var5, var3, var4, true);
      return var5;
   }

   protected static Object[] mapBindings(DataBindingComponent var0, View[] var1, int var2, ViewDataBinding.IncludedLayouts var3, SparseIntArray var4) {
      Object[] var5 = new Object[var2];

      for(var2 = 0; var2 < var1.length; ++var2) {
         mapBindings(var0, var1[var2], var5, var3, var4, true);
      }

      return var5;
   }

   protected static byte parse(String var0, byte var1) {
      try {
         byte var2 = Byte.parseByte(var0);
         return var2;
      } catch (NumberFormatException var3) {
         return var1;
      }
   }

   protected static char parse(String var0, char var1) {
      if (var0 != null) {
         return var0.isEmpty() ? var1 : var0.charAt(0);
      } else {
         return var1;
      }
   }

   protected static double parse(String var0, double var1) {
      try {
         double var3 = Double.parseDouble(var0);
         return var3;
      } catch (NumberFormatException var5) {
         return var1;
      }
   }

   protected static float parse(String var0, float var1) {
      try {
         float var2 = Float.parseFloat(var0);
         return var2;
      } catch (NumberFormatException var3) {
         return var1;
      }
   }

   protected static int parse(String var0, int var1) {
      try {
         int var2 = Integer.parseInt(var0);
         return var2;
      } catch (NumberFormatException var3) {
         return var1;
      }
   }

   protected static long parse(String var0, long var1) {
      try {
         long var3 = Long.parseLong(var0);
         return var3;
      } catch (NumberFormatException var5) {
         return var1;
      }
   }

   protected static short parse(String var0, short var1) {
      try {
         short var2 = Short.parseShort(var0);
         return var2;
      } catch (NumberFormatException var3) {
         return var1;
      }
   }

   protected static boolean parse(String var0, boolean var1) {
      return var0 == null ? var1 : Boolean.parseBoolean(var0);
   }

   private static int parseTagInt(String var0, int var1) {
      int var3 = var0.length();

      int var2;
      for(var2 = 0; var1 < var3; ++var1) {
         var2 = var2 * 10 + (var0.charAt(var1) - 48);
      }

      return var2;
   }

   private static void processReferenceQueue() {
      while(true) {
         Reference var0 = sReferenceQueue.poll();
         if (var0 == null) {
            return;
         }

         if (var0 instanceof ViewDataBinding.WeakListener) {
            ((ViewDataBinding.WeakListener)var0).unregister();
         }
      }
   }

   protected static byte safeUnbox(Byte var0) {
      return var0 == null ? 0 : var0;
   }

   protected static char safeUnbox(Character var0) {
      return var0 == null ? '\u0000' : var0;
   }

   protected static double safeUnbox(Double var0) {
      return var0 == null ? 0.0D : var0;
   }

   protected static float safeUnbox(Float var0) {
      return var0 == null ? 0.0F : var0;
   }

   protected static int safeUnbox(Integer var0) {
      return var0 == null ? 0 : var0;
   }

   protected static long safeUnbox(Long var0) {
      return var0 == null ? 0L : var0;
   }

   protected static short safeUnbox(Short var0) {
      return var0 == null ? 0 : var0;
   }

   protected static boolean safeUnbox(Boolean var0) {
      return var0 == null ? false : var0;
   }

   protected static void setBindingInverseListener(ViewDataBinding var0, InverseBindingListener var1, ViewDataBinding.PropertyChangedInverseListener var2) {
      if (var1 != var2) {
         if (var1 != null) {
            var0.removeOnPropertyChangedCallback((ViewDataBinding.PropertyChangedInverseListener)var1);
         }

         if (var2 != null) {
            var0.addOnPropertyChangedCallback(var2);
         }
      }

   }

   protected static void setTo(LongSparseArray var0, int var1, Object var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.size()) {
            var0.put((long)var1, var2);
         }
      }
   }

   protected static void setTo(SparseArray var0, int var1, Object var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.size()) {
            var0.put(var1, var2);
         }
      }
   }

   protected static void setTo(SparseBooleanArray var0, int var1, boolean var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.size()) {
            var0.put(var1, var2);
         }
      }
   }

   protected static void setTo(SparseIntArray var0, int var1, int var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.size()) {
            var0.put(var1, var2);
         }
      }
   }

   protected static void setTo(SparseLongArray var0, int var1, long var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.size()) {
            var0.put(var1, var2);
         }
      }
   }

   protected static void setTo(androidx.collection.LongSparseArray var0, int var1, Object var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.size()) {
            var0.put((long)var1, var2);
         }
      }
   }

   protected static void setTo(List var0, int var1, Object var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.size()) {
            var0.set(var1, var2);
         }
      }
   }

   protected static void setTo(Map var0, Object var1, Object var2) {
      if (var0 != null) {
         var0.put(var1, var2);
      }
   }

   protected static void setTo(byte[] var0, int var1, byte var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   protected static void setTo(char[] var0, int var1, char var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   protected static void setTo(double[] var0, int var1, double var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   protected static void setTo(float[] var0, int var1, float var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   protected static void setTo(int[] var0, int var1, int var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   protected static void setTo(long[] var0, int var1, long var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   protected static void setTo(Object[] var0, int var1, Object var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   protected static void setTo(short[] var0, int var1, short var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   protected static void setTo(boolean[] var0, int var1, boolean var2) {
      if (var0 != null && var1 >= 0) {
         if (var1 < var0.length) {
            var0[var1] = var2;
         }
      }
   }

   private boolean updateRegistration(int var1, Object var2, ViewDataBinding.CreateWeakListener var3) {
      if (var2 == null) {
         return this.unregisterFrom(var1);
      } else {
         ViewDataBinding.WeakListener var4 = this.mLocalFieldObservers[var1];
         if (var4 == null) {
            this.registerTo(var1, var2, var3);
            return true;
         } else if (var4.getTarget() == var2) {
            return false;
         } else {
            this.unregisterFrom(var1);
            this.registerTo(var1, var2, var3);
            return true;
         }
      }
   }

   public void addOnRebindCallback(OnRebindCallback var1) {
      if (this.mRebindCallbacks == null) {
         this.mRebindCallbacks = new CallbackRegistry(REBIND_NOTIFIER);
      }

      this.mRebindCallbacks.add(var1);
   }

   protected void ensureBindingComponentIsNotNull(Class var1) {
      if (this.mBindingComponent == null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Required DataBindingComponent is null in class ");
         var2.append(this.getClass().getSimpleName());
         var2.append(". A BindingAdapter in ");
         var2.append(var1.getCanonicalName());
         var2.append(" is not static and requires an object to use, retrieved from the DataBindingComponent. If you don't use an inflation method taking a DataBindingComponent, use DataBindingUtil.setDefaultComponent or make all BindingAdapter methods static.");
         throw new IllegalStateException(var2.toString());
      }
   }

   protected abstract void executeBindings();

   public void executePendingBindings() {
      ViewDataBinding var1 = this.mContainingBinding;
      if (var1 == null) {
         this.executeBindingsInternal();
      } else {
         var1.executePendingBindings();
      }
   }

   void forceExecuteBindings() {
      this.executeBindings();
   }

   public LifecycleOwner getLifecycleOwner() {
      return this.mLifecycleOwner;
   }

   protected Object getObservedField(int var1) {
      ViewDataBinding.WeakListener var2 = this.mLocalFieldObservers[var1];
      return var2 == null ? null : var2.getTarget();
   }

   public View getRoot() {
      return this.mRoot;
   }

   public abstract boolean hasPendingBindings();

   public abstract void invalidateAll();

   protected abstract boolean onFieldChange(int var1, Object var2, int var3);

   protected void registerTo(int var1, Object var2, ViewDataBinding.CreateWeakListener var3) {
      if (var2 != null) {
         ViewDataBinding.WeakListener var5 = this.mLocalFieldObservers[var1];
         ViewDataBinding.WeakListener var4 = var5;
         if (var5 == null) {
            ViewDataBinding.WeakListener var7 = var3.create(this, var1);
            this.mLocalFieldObservers[var1] = var7;
            LifecycleOwner var6 = this.mLifecycleOwner;
            var4 = var7;
            if (var6 != null) {
               var7.setLifecycleOwner(var6);
               var4 = var7;
            }
         }

         var4.setTarget(var2);
      }
   }

   public void removeOnRebindCallback(OnRebindCallback var1) {
      CallbackRegistry var2 = this.mRebindCallbacks;
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   protected void requestRebind() {
      ViewDataBinding var1 = this.mContainingBinding;
      if (var1 != null) {
         var1.requestRebind();
      } else {
         LifecycleOwner var14 = this.mLifecycleOwner;
         if (var14 == null || var14.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            synchronized(this){}

            Throwable var10000;
            boolean var10001;
            label193: {
               try {
                  if (this.mPendingRebind) {
                     return;
                  }
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label193;
               }

               try {
                  this.mPendingRebind = true;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label193;
               }

               if (USE_CHOREOGRAPHER) {
                  this.mChoreographer.postFrameCallback(this.mFrameCallback);
                  return;
               }

               this.mUIThreadHandler.post(this.mRebindRunnable);
               return;
            }

            while(true) {
               Throwable var15 = var10000;

               try {
                  throw var15;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  continue;
               }
            }
         }
      }
   }

   protected void setContainedBinding(ViewDataBinding var1) {
      if (var1 != null) {
         var1.mContainingBinding = this;
      }

   }

   public void setLifecycleOwner(LifecycleOwner var1) {
      LifecycleOwner var4 = this.mLifecycleOwner;
      if (var4 != var1) {
         if (var4 != null) {
            var4.getLifecycle().removeObserver(this.mOnStartListener);
         }

         this.mLifecycleOwner = var1;
         if (var1 != null) {
            if (this.mOnStartListener == null) {
               this.mOnStartListener = new ViewDataBinding.OnStartListener(this);
            }

            var1.getLifecycle().addObserver(this.mOnStartListener);
         }

         ViewDataBinding.WeakListener[] var6 = this.mLocalFieldObservers;
         int var3 = var6.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            ViewDataBinding.WeakListener var5 = var6[var2];
            if (var5 != null) {
               var5.setLifecycleOwner(var1);
            }
         }

      }
   }

   protected void setRootTag(View var1) {
      var1.setTag(id.dataBinding, this);
   }

   protected void setRootTag(View[] var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         var1[var2].setTag(id.dataBinding, this);
      }

   }

   public abstract boolean setVariable(int var1, Object var2);

   public void unbind() {
      ViewDataBinding.WeakListener[] var3 = this.mLocalFieldObservers;
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ViewDataBinding.WeakListener var4 = var3[var1];
         if (var4 != null) {
            var4.unregister();
         }
      }

   }

   protected boolean unregisterFrom(int var1) {
      ViewDataBinding.WeakListener var2 = this.mLocalFieldObservers[var1];
      return var2 != null ? var2.unregister() : false;
   }

   protected boolean updateLiveDataRegistration(int var1, LiveData var2) {
      this.mInLiveDataRegisterObserver = true;

      boolean var3;
      try {
         var3 = this.updateRegistration(var1, var2, CREATE_LIVE_DATA_LISTENER);
      } finally {
         this.mInLiveDataRegisterObserver = false;
      }

      return var3;
   }

   protected boolean updateRegistration(int var1, Observable var2) {
      return this.updateRegistration(var1, var2, CREATE_PROPERTY_LISTENER);
   }

   protected boolean updateRegistration(int var1, ObservableList var2) {
      return this.updateRegistration(var1, var2, CREATE_LIST_LISTENER);
   }

   protected boolean updateRegistration(int var1, ObservableMap var2) {
      return this.updateRegistration(var1, var2, CREATE_MAP_LISTENER);
   }

   private interface CreateWeakListener {
      ViewDataBinding.WeakListener create(ViewDataBinding var1, int var2);
   }

   protected static class IncludedLayouts {
      public final int[][] indexes;
      public final int[][] layoutIds;
      public final String[][] layouts;

      public IncludedLayouts(int var1) {
         this.layouts = new String[var1][];
         this.indexes = new int[var1][];
         this.layoutIds = new int[var1][];
      }

      public void setIncludes(int var1, String[] var2, int[] var3, int[] var4) {
         this.layouts[var1] = var2;
         this.indexes[var1] = var3;
         this.layoutIds[var1] = var4;
      }
   }

   private static class LiveDataListener implements Observer, ViewDataBinding.ObservableReference {
      LifecycleOwner mLifecycleOwner;
      final ViewDataBinding.WeakListener mListener;

      public LiveDataListener(ViewDataBinding var1, int var2) {
         this.mListener = new ViewDataBinding.WeakListener(var1, var2, this);
      }

      public void addListener(LiveData var1) {
         LifecycleOwner var2 = this.mLifecycleOwner;
         if (var2 != null) {
            var1.observe(var2, this);
         }

      }

      public ViewDataBinding.WeakListener getListener() {
         return this.mListener;
      }

      public void onChanged(Object var1) {
         ViewDataBinding var2 = this.mListener.getBinder();
         if (var2 != null) {
            var2.handleFieldChange(this.mListener.mLocalFieldId, this.mListener.getTarget(), 0);
         }

      }

      public void removeListener(LiveData var1) {
         var1.removeObserver(this);
      }

      public void setLifecycleOwner(LifecycleOwner var1) {
         LiveData var2 = (LiveData)this.mListener.getTarget();
         if (var2 != null) {
            if (this.mLifecycleOwner != null) {
               var2.removeObserver(this);
            }

            if (var1 != null) {
               var2.observe(var1, this);
            }
         }

         this.mLifecycleOwner = var1;
      }
   }

   private interface ObservableReference {
      void addListener(Object var1);

      ViewDataBinding.WeakListener getListener();

      void removeListener(Object var1);

      void setLifecycleOwner(LifecycleOwner var1);
   }

   static class OnStartListener implements LifecycleObserver {
      final WeakReference mBinding;

      private OnStartListener(ViewDataBinding var1) {
         this.mBinding = new WeakReference(var1);
      }

      // $FF: synthetic method
      OnStartListener(ViewDataBinding var1, Object var2) {
         this(var1);
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_START)
      public void onStart() {
         ViewDataBinding var1 = (ViewDataBinding)this.mBinding.get();
         if (var1 != null) {
            var1.executePendingBindings();
         }

      }
   }

   protected abstract static class PropertyChangedInverseListener extends Observable.OnPropertyChangedCallback implements InverseBindingListener {
      final int mPropertyId;

      public PropertyChangedInverseListener(int var1) {
         this.mPropertyId = var1;
      }

      public void onPropertyChanged(Observable var1, int var2) {
         if (var2 == this.mPropertyId || var2 == 0) {
            this.onChange();
         }

      }
   }

   private static class WeakListListener extends ObservableList.OnListChangedCallback implements ViewDataBinding.ObservableReference {
      final ViewDataBinding.WeakListener mListener;

      public WeakListListener(ViewDataBinding var1, int var2) {
         this.mListener = new ViewDataBinding.WeakListener(var1, var2, this);
      }

      public void addListener(ObservableList var1) {
         var1.addOnListChangedCallback(this);
      }

      public ViewDataBinding.WeakListener getListener() {
         return this.mListener;
      }

      public void onChanged(ObservableList var1) {
         ViewDataBinding var2 = this.mListener.getBinder();
         if (var2 != null) {
            ObservableList var3 = (ObservableList)this.mListener.getTarget();
            if (var3 == var1) {
               var2.handleFieldChange(this.mListener.mLocalFieldId, var3, 0);
            }
         }
      }

      public void onItemRangeChanged(ObservableList var1, int var2, int var3) {
         this.onChanged(var1);
      }

      public void onItemRangeInserted(ObservableList var1, int var2, int var3) {
         this.onChanged(var1);
      }

      public void onItemRangeMoved(ObservableList var1, int var2, int var3, int var4) {
         this.onChanged(var1);
      }

      public void onItemRangeRemoved(ObservableList var1, int var2, int var3) {
         this.onChanged(var1);
      }

      public void removeListener(ObservableList var1) {
         var1.removeOnListChangedCallback(this);
      }

      public void setLifecycleOwner(LifecycleOwner var1) {
      }
   }

   private static class WeakListener extends WeakReference {
      protected final int mLocalFieldId;
      private final ViewDataBinding.ObservableReference mObservable;
      private Object mTarget;

      public WeakListener(ViewDataBinding var1, int var2, ViewDataBinding.ObservableReference var3) {
         super(var1, ViewDataBinding.sReferenceQueue);
         this.mLocalFieldId = var2;
         this.mObservable = var3;
      }

      protected ViewDataBinding getBinder() {
         ViewDataBinding var1 = (ViewDataBinding)this.get();
         if (var1 == null) {
            this.unregister();
         }

         return var1;
      }

      public Object getTarget() {
         return this.mTarget;
      }

      public void setLifecycleOwner(LifecycleOwner var1) {
         this.mObservable.setLifecycleOwner(var1);
      }

      public void setTarget(Object var1) {
         this.unregister();
         this.mTarget = var1;
         if (var1 != null) {
            this.mObservable.addListener(var1);
         }

      }

      public boolean unregister() {
         boolean var1 = false;
         Object var2 = this.mTarget;
         if (var2 != null) {
            this.mObservable.removeListener(var2);
            var1 = true;
         }

         this.mTarget = null;
         return var1;
      }
   }

   private static class WeakMapListener extends ObservableMap.OnMapChangedCallback implements ViewDataBinding.ObservableReference {
      final ViewDataBinding.WeakListener mListener;

      public WeakMapListener(ViewDataBinding var1, int var2) {
         this.mListener = new ViewDataBinding.WeakListener(var1, var2, this);
      }

      public void addListener(ObservableMap var1) {
         var1.addOnMapChangedCallback(this);
      }

      public ViewDataBinding.WeakListener getListener() {
         return this.mListener;
      }

      public void onMapChanged(ObservableMap var1, Object var2) {
         ViewDataBinding var3 = this.mListener.getBinder();
         if (var3 != null) {
            if (var1 == this.mListener.getTarget()) {
               var3.handleFieldChange(this.mListener.mLocalFieldId, var1, 0);
            }
         }
      }

      public void removeListener(ObservableMap var1) {
         var1.removeOnMapChangedCallback(this);
      }

      public void setLifecycleOwner(LifecycleOwner var1) {
      }
   }

   private static class WeakPropertyListener extends Observable.OnPropertyChangedCallback implements ViewDataBinding.ObservableReference {
      final ViewDataBinding.WeakListener mListener;

      public WeakPropertyListener(ViewDataBinding var1, int var2) {
         this.mListener = new ViewDataBinding.WeakListener(var1, var2, this);
      }

      public void addListener(Observable var1) {
         var1.addOnPropertyChangedCallback(this);
      }

      public ViewDataBinding.WeakListener getListener() {
         return this.mListener;
      }

      public void onPropertyChanged(Observable var1, int var2) {
         ViewDataBinding var3 = this.mListener.getBinder();
         if (var3 != null) {
            if ((Observable)this.mListener.getTarget() == var1) {
               var3.handleFieldChange(this.mListener.mLocalFieldId, var1, var2);
            }
         }
      }

      public void removeListener(Observable var1) {
         var1.removeOnPropertyChangedCallback(this);
      }

      public void setLifecycleOwner(LifecycleOwner var1) {
      }
   }
}
