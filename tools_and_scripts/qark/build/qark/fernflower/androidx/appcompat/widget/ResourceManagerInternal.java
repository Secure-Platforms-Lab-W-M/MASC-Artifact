package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.Theme;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;
import androidx.appcompat.resources.R.drawable;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.LruCache;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourceManagerInternal {
   private static final ResourceManagerInternal.ColorFilterLruCache COLOR_FILTER_CACHE;
   private static final boolean DEBUG = false;
   private static final Mode DEFAULT_MODE;
   private static ResourceManagerInternal INSTANCE;
   private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
   private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
   private static final String TAG = "ResourceManagerInternal";
   private ArrayMap mDelegates;
   private final WeakHashMap mDrawableCaches = new WeakHashMap(0);
   private boolean mHasCheckedVectorDrawableSetup;
   private ResourceManagerInternal.ResourceManagerHooks mHooks;
   private SparseArrayCompat mKnownDrawableIdTags;
   private WeakHashMap mTintLists;
   private TypedValue mTypedValue;

   static {
      DEFAULT_MODE = Mode.SRC_IN;
      COLOR_FILTER_CACHE = new ResourceManagerInternal.ColorFilterLruCache(6);
   }

   private void addDelegate(String var1, ResourceManagerInternal.InflateDelegate var2) {
      if (this.mDelegates == null) {
         this.mDelegates = new ArrayMap();
      }

      this.mDelegates.put(var1, var2);
   }

   private boolean addDrawableToCache(Context var1, long var2, Drawable var4) {
      synchronized(this){}

      Throwable var10000;
      label190: {
         ConstantState var6;
         boolean var10001;
         try {
            var6 = var4.getConstantState();
         } catch (Throwable var26) {
            var10000 = var26;
            var10001 = false;
            break label190;
         }

         if (var6 == null) {
            return false;
         }

         LongSparseArray var5;
         try {
            var5 = (LongSparseArray)this.mDrawableCaches.get(var1);
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label190;
         }

         LongSparseArray var28 = var5;
         if (var5 == null) {
            try {
               var28 = new LongSparseArray();
               this.mDrawableCaches.put(var1, var28);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label190;
            }
         }

         try {
            var28.put(var2, new WeakReference(var6));
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label190;
         }

         return true;
      }

      Throwable var27 = var10000;
      throw var27;
   }

   private void addTintListToCache(Context var1, int var2, ColorStateList var3) {
      if (this.mTintLists == null) {
         this.mTintLists = new WeakHashMap();
      }

      SparseArrayCompat var5 = (SparseArrayCompat)this.mTintLists.get(var1);
      SparseArrayCompat var4 = var5;
      if (var5 == null) {
         var4 = new SparseArrayCompat();
         this.mTintLists.put(var1, var4);
      }

      var4.append(var2, var3);
   }

   private static boolean arrayContains(int[] var0, int var1) {
      int var3 = var0.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var0[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   private void checkVectorDrawableSetup(Context var1) {
      if (!this.mHasCheckedVectorDrawableSetup) {
         this.mHasCheckedVectorDrawableSetup = true;
         Drawable var2 = this.getDrawable(var1, drawable.abc_vector_test);
         if (var2 == null || !isVectorDrawable(var2)) {
            this.mHasCheckedVectorDrawableSetup = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
         }
      }
   }

   private static long createCacheKey(TypedValue var0) {
      return (long)var0.assetCookie << 32 | (long)var0.data;
   }

   private Drawable createDrawableIfNeeded(Context var1, int var2) {
      if (this.mTypedValue == null) {
         this.mTypedValue = new TypedValue();
      }

      TypedValue var6 = this.mTypedValue;
      var1.getResources().getValue(var2, var6, true);
      long var3 = createCacheKey(var6);
      Drawable var5 = this.getCachedDrawable(var1, var3);
      if (var5 != null) {
         return var5;
      } else {
         ResourceManagerInternal.ResourceManagerHooks var7 = this.mHooks;
         if (var7 == null) {
            var5 = null;
         } else {
            var5 = var7.createDrawableFor(this, var1, var2);
         }

         if (var5 != null) {
            var5.setChangingConfigurations(var6.changingConfigurations);
            this.addDrawableToCache(var1, var3, var5);
         }

         return var5;
      }
   }

   private static PorterDuffColorFilter createTintFilter(ColorStateList var0, Mode var1, int[] var2) {
      return var0 != null && var1 != null ? getPorterDuffColorFilter(var0.getColorForState(var2, 0), var1) : null;
   }

   public static ResourceManagerInternal get() {
      synchronized(ResourceManagerInternal.class){}

      ResourceManagerInternal var0;
      try {
         if (INSTANCE == null) {
            var0 = new ResourceManagerInternal();
            INSTANCE = var0;
            installDefaultInflateDelegates(var0);
         }

         var0 = INSTANCE;
      } finally {
         ;
      }

      return var0;
   }

   private Drawable getCachedDrawable(Context var1, long var2) {
      synchronized(this){}

      Throwable var10000;
      label269: {
         LongSparseArray var4;
         boolean var10001;
         try {
            var4 = (LongSparseArray)this.mDrawableCaches.get(var1);
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label269;
         }

         if (var4 == null) {
            return null;
         }

         WeakReference var5;
         try {
            var5 = (WeakReference)var4.get(var2);
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label269;
         }

         if (var5 != null) {
            ConstantState var38;
            try {
               var38 = (ConstantState)var5.get();
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label269;
            }

            if (var38 != null) {
               Drawable var37;
               try {
                  var37 = var38.newDrawable(var1.getResources());
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label269;
               }

               return var37;
            }

            try {
               var4.delete(var2);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label269;
            }
         }

         return null;
      }

      Throwable var36 = var10000;
      throw var36;
   }

   public static PorterDuffColorFilter getPorterDuffColorFilter(int var0, Mode var1) {
      synchronized(ResourceManagerInternal.class){}

      PorterDuffColorFilter var2;
      label71: {
         Throwable var10000;
         label75: {
            boolean var10001;
            PorterDuffColorFilter var3;
            try {
               var3 = COLOR_FILTER_CACHE.get(var0, var1);
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label75;
            }

            var2 = var3;
            if (var3 != null) {
               break label71;
            }

            label66:
            try {
               var2 = new PorterDuffColorFilter(var0, var1);
               COLOR_FILTER_CACHE.put(var0, var1, var2);
               break label71;
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label66;
            }
         }

         Throwable var10 = var10000;
         throw var10;
      }

      return var2;
   }

   private ColorStateList getTintListFromCache(Context var1, int var2) {
      WeakHashMap var4 = this.mTintLists;
      Object var3 = null;
      if (var4 != null) {
         SparseArrayCompat var6 = (SparseArrayCompat)var4.get(var1);
         ColorStateList var5 = (ColorStateList)var3;
         if (var6 != null) {
            var5 = (ColorStateList)var6.get(var2);
         }

         return var5;
      } else {
         return null;
      }
   }

   private static void installDefaultInflateDelegates(ResourceManagerInternal var0) {
      if (VERSION.SDK_INT < 24) {
         var0.addDelegate("vector", new ResourceManagerInternal.VdcInflateDelegate());
         var0.addDelegate("animated-vector", new ResourceManagerInternal.AvdcInflateDelegate());
         var0.addDelegate("animated-selector", new ResourceManagerInternal.AsldcInflateDelegate());
      }

   }

   private static boolean isVectorDrawable(Drawable var0) {
      return var0 instanceof VectorDrawableCompat || "android.graphics.drawable.VectorDrawable".equals(var0.getClass().getName());
   }

   private Drawable loadDrawableFromDelegates(Context var1, int var2) {
      ArrayMap var6 = this.mDelegates;
      if (var6 != null && !var6.isEmpty()) {
         SparseArrayCompat var24 = this.mKnownDrawableIdTags;
         String var25;
         if (var24 != null) {
            var25 = (String)var24.get(var2);
            if ("appcompat_skip_skip".equals(var25)) {
               return null;
            }

            if (var25 != null && this.mDelegates.get(var25) == null) {
               return null;
            }
         } else {
            this.mKnownDrawableIdTags = new SparseArrayCompat();
         }

         if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
         }

         TypedValue var9 = this.mTypedValue;
         Resources var10 = var1.getResources();
         var10.getValue(var2, var9, true);
         long var4 = createCacheKey(var9);
         Drawable var8 = this.getCachedDrawable(var1, var4);
         if (var8 != null) {
            return var8;
         } else {
            Drawable var26 = var8;
            if (var9.string != null) {
               var26 = var8;
               if (var9.string.toString().endsWith(".xml")) {
                  label143: {
                     Drawable var7 = var8;

                     Exception var10000;
                     label144: {
                        boolean var10001;
                        XmlResourceParser var27;
                        try {
                           var27 = var10.getXml(var2);
                        } catch (Exception var22) {
                           var10000 = var22;
                           var10001 = false;
                           break label144;
                        }

                        var7 = var8;

                        AttributeSet var11;
                        try {
                           var11 = Xml.asAttributeSet(var27);
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                           break label144;
                        }

                        int var3;
                        do {
                           var7 = var8;

                           try {
                              var3 = var27.next();
                           } catch (Exception var20) {
                              var10000 = var20;
                              var10001 = false;
                              break label144;
                           }
                        } while(var3 != 2 && var3 != 1);

                        if (var3 == 2) {
                           label146: {
                              var7 = var8;

                              try {
                                 var25 = var27.getName();
                              } catch (Exception var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 break label146;
                              }

                              var7 = var8;

                              try {
                                 this.mKnownDrawableIdTags.append(var2, var25);
                              } catch (Exception var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break label146;
                              }

                              var7 = var8;

                              ResourceManagerInternal.InflateDelegate var12;
                              try {
                                 var12 = (ResourceManagerInternal.InflateDelegate)this.mDelegates.get(var25);
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label146;
                              }

                              var26 = var8;
                              if (var12 != null) {
                                 var7 = var8;

                                 try {
                                    var26 = var12.createFromXmlInner(var1, var27, var11, var1.getTheme());
                                 } catch (Exception var16) {
                                    var10000 = var16;
                                    var10001 = false;
                                    break label146;
                                 }
                              }

                              if (var26 == null) {
                                 break label143;
                              }

                              var7 = var26;

                              try {
                                 var26.setChangingConfigurations(var9.changingConfigurations);
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label146;
                              }

                              var7 = var26;

                              try {
                                 this.addDrawableToCache(var1, var4, var26);
                                 break label143;
                              } catch (Exception var14) {
                                 var10000 = var14;
                                 var10001 = false;
                              }
                           }
                        } else {
                           var7 = var8;

                           try {
                              throw new XmlPullParserException("No start tag found");
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                           }
                        }
                     }

                     Exception var23 = var10000;
                     Log.e("ResourceManagerInternal", "Exception while inflating drawable", var23);
                     var26 = var7;
                  }
               }
            }

            if (var26 == null) {
               this.mKnownDrawableIdTags.append(var2, "appcompat_skip_skip");
            }

            return var26;
         }
      } else {
         return null;
      }
   }

   private void removeDelegate(String var1, ResourceManagerInternal.InflateDelegate var2) {
      ArrayMap var3 = this.mDelegates;
      if (var3 != null && var3.get(var1) == var2) {
         this.mDelegates.remove(var1);
      }

   }

   private Drawable tintDrawable(Context var1, int var2, boolean var3, Drawable var4) {
      ColorStateList var5 = this.getTintList(var1, var2);
      if (var5 != null) {
         Drawable var6 = var4;
         if (DrawableUtils.canSafelyMutateDrawable(var4)) {
            var6 = var4.mutate();
         }

         var6 = DrawableCompat.wrap(var6);
         DrawableCompat.setTintList(var6, var5);
         Mode var7 = this.getTintMode(var2);
         if (var7 != null) {
            DrawableCompat.setTintMode(var6, var7);
         }

         return var6;
      } else {
         ResourceManagerInternal.ResourceManagerHooks var8 = this.mHooks;
         if (var8 != null && var8.tintDrawable(var1, var2, var4)) {
            return var4;
         } else {
            Drawable var9 = var4;
            if (!this.tintDrawableUsingColorFilter(var1, var2, var4)) {
               var9 = var4;
               if (var3) {
                  var9 = null;
               }
            }

            return var9;
         }
      }
   }

   static void tintDrawable(Drawable var0, TintInfo var1, int[] var2) {
      if (DrawableUtils.canSafelyMutateDrawable(var0) && var0.mutate() != var0) {
         Log.d("ResourceManagerInternal", "Mutated drawable is not the same instance as the input.");
      } else {
         if (!var1.mHasTintList && !var1.mHasTintMode) {
            var0.clearColorFilter();
         } else {
            ColorStateList var3;
            if (var1.mHasTintList) {
               var3 = var1.mTintList;
            } else {
               var3 = null;
            }

            Mode var4;
            if (var1.mHasTintMode) {
               var4 = var1.mTintMode;
            } else {
               var4 = DEFAULT_MODE;
            }

            var0.setColorFilter(createTintFilter(var3, var4, var2));
         }

         if (VERSION.SDK_INT <= 23) {
            var0.invalidateSelf();
         }

      }
   }

   public Drawable getDrawable(Context var1, int var2) {
      synchronized(this){}

      Drawable var5;
      try {
         var5 = this.getDrawable(var1, var2, false);
      } finally {
         ;
      }

      return var5;
   }

   Drawable getDrawable(Context var1, int var2, boolean var3) {
      synchronized(this){}

      Throwable var10000;
      label276: {
         Drawable var5;
         boolean var10001;
         try {
            this.checkVectorDrawableSetup(var1);
            var5 = this.loadDrawableFromDelegates(var1, var2);
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label276;
         }

         Drawable var4 = var5;
         if (var5 == null) {
            try {
               var4 = this.createDrawableIfNeeded(var1, var2);
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label276;
            }
         }

         var5 = var4;
         if (var4 == null) {
            try {
               var5 = ContextCompat.getDrawable(var1, var2);
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label276;
            }
         }

         var4 = var5;
         if (var5 != null) {
            try {
               var4 = this.tintDrawable(var1, var2, var3, var5);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label276;
            }
         }

         if (var4 == null) {
            return var4;
         }

         label255:
         try {
            DrawableUtils.fixDrawable(var4);
            return var4;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label255;
         }
      }

      Throwable var36 = var10000;
      throw var36;
   }

   ColorStateList getTintList(Context var1, int var2) {
      synchronized(this){}

      ColorStateList var3;
      ColorStateList var4;
      label220: {
         Throwable var10000;
         label225: {
            boolean var10001;
            try {
               var4 = this.getTintListFromCache(var1, var2);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label225;
            }

            var3 = var4;
            if (var4 != null) {
               return var3;
            }

            label214: {
               label213: {
                  try {
                     if (this.mHooks != null) {
                        break label213;
                     }
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label225;
                  }

                  var3 = null;
                  break label214;
               }

               try {
                  var3 = this.mHooks.getTintListForDrawableRes(var1, var2);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label225;
               }
            }

            var4 = var3;
            var3 = var3;
            if (var4 == null) {
               return var3;
            }

            label205:
            try {
               this.addTintListToCache(var1, var2, var4);
               break label220;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label205;
            }
         }

         Throwable var25 = var10000;
         throw var25;
      }

      var3 = var4;
      return var3;
   }

   Mode getTintMode(int var1) {
      ResourceManagerInternal.ResourceManagerHooks var2 = this.mHooks;
      return var2 == null ? null : var2.getTintModeForDrawableRes(var1);
   }

   public void onConfigurationChanged(Context var1) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         LongSparseArray var8;
         try {
            var8 = (LongSparseArray)this.mDrawableCaches.get(var1);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label75;
         }

         if (var8 == null) {
            return;
         }

         label66:
         try {
            var8.clear();
            return;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label66;
         }
      }

      Throwable var9 = var10000;
      throw var9;
   }

   Drawable onDrawableLoadedFromResources(Context var1, VectorEnabledTintResources var2, int var3) {
      synchronized(this){}

      Throwable var10000;
      label132: {
         Drawable var5;
         boolean var10001;
         try {
            var5 = this.loadDrawableFromDelegates(var1, var3);
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label132;
         }

         Drawable var4 = var5;
         if (var5 == null) {
            try {
               var4 = var2.superGetDrawable(var3);
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label132;
            }
         }

         if (var4 == null) {
            return null;
         }

         Drawable var19;
         try {
            var19 = this.tintDrawable(var1, var3, false, var4);
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label132;
         }

         return var19;
      }

      Throwable var18 = var10000;
      throw var18;
   }

   public void setHooks(ResourceManagerInternal.ResourceManagerHooks var1) {
      synchronized(this){}

      try {
         this.mHooks = var1;
      } finally {
         ;
      }

   }

   boolean tintDrawableUsingColorFilter(Context var1, int var2, Drawable var3) {
      ResourceManagerInternal.ResourceManagerHooks var4 = this.mHooks;
      return var4 != null && var4.tintDrawableUsingColorFilter(var1, var2, var3);
   }

   static class AsldcInflateDelegate implements ResourceManagerInternal.InflateDelegate {
      public Drawable createFromXmlInner(Context var1, XmlPullParser var2, AttributeSet var3, Theme var4) {
         try {
            AnimatedStateListDrawableCompat var6 = AnimatedStateListDrawableCompat.createFromXmlInner(var1, var1.getResources(), var2, var3, var4);
            return var6;
         } catch (Exception var5) {
            Log.e("AsldcInflateDelegate", "Exception while inflating <animated-selector>", var5);
            return null;
         }
      }
   }

   private static class AvdcInflateDelegate implements ResourceManagerInternal.InflateDelegate {
      AvdcInflateDelegate() {
      }

      public Drawable createFromXmlInner(Context var1, XmlPullParser var2, AttributeSet var3, Theme var4) {
         try {
            AnimatedVectorDrawableCompat var6 = AnimatedVectorDrawableCompat.createFromXmlInner(var1, var1.getResources(), var2, var3, var4);
            return var6;
         } catch (Exception var5) {
            Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", var5);
            return null;
         }
      }
   }

   private static class ColorFilterLruCache extends LruCache {
      public ColorFilterLruCache(int var1) {
         super(var1);
      }

      private static int generateCacheKey(int var0, Mode var1) {
         return (1 * 31 + var0) * 31 + var1.hashCode();
      }

      PorterDuffColorFilter get(int var1, Mode var2) {
         return (PorterDuffColorFilter)this.get(generateCacheKey(var1, var2));
      }

      PorterDuffColorFilter put(int var1, Mode var2, PorterDuffColorFilter var3) {
         return (PorterDuffColorFilter)this.put(generateCacheKey(var1, var2), var3);
      }
   }

   private interface InflateDelegate {
      Drawable createFromXmlInner(Context var1, XmlPullParser var2, AttributeSet var3, Theme var4);
   }

   interface ResourceManagerHooks {
      Drawable createDrawableFor(ResourceManagerInternal var1, Context var2, int var3);

      ColorStateList getTintListForDrawableRes(Context var1, int var2);

      Mode getTintModeForDrawableRes(int var1);

      boolean tintDrawable(Context var1, int var2, Drawable var3);

      boolean tintDrawableUsingColorFilter(Context var1, int var2, Drawable var3);
   }

   private static class VdcInflateDelegate implements ResourceManagerInternal.InflateDelegate {
      VdcInflateDelegate() {
      }

      public Drawable createFromXmlInner(Context var1, XmlPullParser var2, AttributeSet var3, Theme var4) {
         try {
            VectorDrawableCompat var6 = VectorDrawableCompat.createFromXmlInner(var1.getResources(), var2, var3, var4);
            return var6;
         } catch (Exception var5) {
            Log.e("VdcInflateDelegate", "Exception while inflating <vector>", var5);
            return null;
         }
      }
   }
}
