package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.Theme;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.LruCache;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$color;
import android.support.v7.appcompat.R$drawable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class AppCompatDrawableManager {
   private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
   private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
   private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
   private static final AppCompatDrawableManager.ColorFilterLruCache COLOR_FILTER_CACHE;
   private static final boolean DEBUG = false;
   private static final Mode DEFAULT_MODE;
   private static AppCompatDrawableManager INSTANCE;
   private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
   private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
   private static final String TAG = "AppCompatDrawableManager";
   private static final int[] TINT_CHECKABLE_BUTTON_LIST;
   private static final int[] TINT_COLOR_CONTROL_NORMAL;
   private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
   private ArrayMap mDelegates;
   private final Object mDrawableCacheLock = new Object();
   private final WeakHashMap mDrawableCaches = new WeakHashMap(0);
   private boolean mHasCheckedVectorDrawableSetup;
   private SparseArrayCompat mKnownDrawableIdTags;
   private WeakHashMap mTintLists;
   private TypedValue mTypedValue;

   static {
      DEFAULT_MODE = Mode.SRC_IN;
      COLOR_FILTER_CACHE = new AppCompatDrawableManager.ColorFilterLruCache(6);
      COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{R$drawable.abc_textfield_search_default_mtrl_alpha, R$drawable.abc_textfield_default_mtrl_alpha, R$drawable.abc_ab_share_pack_mtrl_alpha};
      TINT_COLOR_CONTROL_NORMAL = new int[]{R$drawable.abc_ic_commit_search_api_mtrl_alpha, R$drawable.abc_seekbar_tick_mark_material, R$drawable.abc_ic_menu_share_mtrl_alpha, R$drawable.abc_ic_menu_copy_mtrl_am_alpha, R$drawable.abc_ic_menu_cut_mtrl_alpha, R$drawable.abc_ic_menu_selectall_mtrl_alpha, R$drawable.abc_ic_menu_paste_mtrl_am_alpha};
      COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{R$drawable.abc_textfield_activated_mtrl_alpha, R$drawable.abc_textfield_search_activated_mtrl_alpha, R$drawable.abc_cab_background_top_mtrl_alpha, R$drawable.abc_text_cursor_material, R$drawable.abc_text_select_handle_left_mtrl_dark, R$drawable.abc_text_select_handle_middle_mtrl_dark, R$drawable.abc_text_select_handle_right_mtrl_dark, R$drawable.abc_text_select_handle_left_mtrl_light, R$drawable.abc_text_select_handle_middle_mtrl_light, R$drawable.abc_text_select_handle_right_mtrl_light};
      COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{R$drawable.abc_popup_background_mtrl_mult, R$drawable.abc_cab_background_internal_bg, R$drawable.abc_menu_hardkey_panel_mtrl_mult};
      TINT_COLOR_CONTROL_STATE_LIST = new int[]{R$drawable.abc_tab_indicator_material, R$drawable.abc_textfield_search_material};
      TINT_CHECKABLE_BUTTON_LIST = new int[]{R$drawable.abc_btn_check_material, R$drawable.abc_btn_radio_material};
   }

   private void addDelegate(@NonNull String var1, @NonNull AppCompatDrawableManager.InflateDelegate var2) {
      if (this.mDelegates == null) {
         this.mDelegates = new ArrayMap();
      }

      this.mDelegates.put(var1, var2);
   }

   private boolean addDrawableToCache(@NonNull Context var1, long var2, @NonNull Drawable var4) {
      ConstantState var7 = var4.getConstantState();
      if (var7 != null) {
         Object var6 = this.mDrawableCacheLock;
         synchronized(var6){}

         Throwable var10000;
         boolean var10001;
         label196: {
            LongSparseArray var5;
            try {
               var5 = (LongSparseArray)this.mDrawableCaches.get(var1);
            } catch (Throwable var27) {
               var10000 = var27;
               var10001 = false;
               break label196;
            }

            LongSparseArray var29 = var5;
            if (var5 == null) {
               try {
                  var29 = new LongSparseArray();
                  this.mDrawableCaches.put(var1, var29);
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label196;
               }
            }

            label183:
            try {
               var29.put(var2, new WeakReference(var7));
               return true;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label183;
            }
         }

         while(true) {
            Throwable var28 = var10000;

            try {
               throw var28;
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               continue;
            }
         }
      } else {
         return false;
      }
   }

   private void addTintListToCache(@NonNull Context var1, @DrawableRes int var2, @NonNull ColorStateList var3) {
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

   private void checkVectorDrawableSetup(@NonNull Context var1) {
      if (!this.mHasCheckedVectorDrawableSetup) {
         this.mHasCheckedVectorDrawableSetup = true;
         Drawable var2 = this.getDrawable(var1, R$drawable.abc_vector_test);
         if (var2 == null || !isVectorDrawable(var2)) {
            this.mHasCheckedVectorDrawableSetup = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
         }
      }
   }

   private ColorStateList createBorderlessButtonColorStateList(@NonNull Context var1) {
      return this.createButtonColorStateList(var1, 0);
   }

   private ColorStateList createButtonColorStateList(@NonNull Context var1, @ColorInt int var2) {
      int[][] var5 = new int[4][];
      int[] var6 = new int[4];
      int var3 = ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlHighlight);
      int var4 = ThemeUtils.getDisabledThemeAttrColor(var1, R$attr.colorButtonNormal);
      var5[0] = ThemeUtils.DISABLED_STATE_SET;
      var6[0] = var4;
      var4 = 0 + 1;
      var5[var4] = ThemeUtils.PRESSED_STATE_SET;
      var6[var4] = ColorUtils.compositeColors(var3, var2);
      ++var4;
      var5[var4] = ThemeUtils.FOCUSED_STATE_SET;
      var6[var4] = ColorUtils.compositeColors(var3, var2);
      var3 = var4 + 1;
      var5[var3] = ThemeUtils.EMPTY_STATE_SET;
      var6[var3] = var2;
      return new ColorStateList(var5, var6);
   }

   private static long createCacheKey(TypedValue var0) {
      return (long)var0.assetCookie << 32 | (long)var0.data;
   }

   private ColorStateList createColoredButtonColorStateList(@NonNull Context var1) {
      return this.createButtonColorStateList(var1, ThemeUtils.getThemeAttrColor(var1, R$attr.colorAccent));
   }

   private ColorStateList createDefaultButtonColorStateList(@NonNull Context var1) {
      return this.createButtonColorStateList(var1, ThemeUtils.getThemeAttrColor(var1, R$attr.colorButtonNormal));
   }

   private Drawable createDrawableIfNeeded(@NonNull Context var1, @DrawableRes int var2) {
      if (this.mTypedValue == null) {
         this.mTypedValue = new TypedValue();
      }

      TypedValue var6 = this.mTypedValue;
      var1.getResources().getValue(var2, var6, true);
      long var3 = createCacheKey(var6);
      Object var5 = this.getCachedDrawable(var1, var3);
      if (var5 != null) {
         return (Drawable)var5;
      } else {
         if (var2 == R$drawable.abc_cab_background_top_material) {
            var5 = new LayerDrawable(new Drawable[]{this.getDrawable(var1, R$drawable.abc_cab_background_internal_bg), this.getDrawable(var1, R$drawable.abc_cab_background_top_mtrl_alpha)});
         }

         if (var5 != null) {
            ((Drawable)var5).setChangingConfigurations(var6.changingConfigurations);
            this.addDrawableToCache(var1, var3, (Drawable)var5);
         }

         return (Drawable)var5;
      }
   }

   private ColorStateList createSwitchThumbColorStateList(Context var1) {
      int[][] var3 = new int[3][];
      int[] var4 = new int[3];
      ColorStateList var5 = ThemeUtils.getThemeAttrColorStateList(var1, R$attr.colorSwitchThumbNormal);
      int var2;
      if (var5 != null && var5.isStateful()) {
         var3[0] = ThemeUtils.DISABLED_STATE_SET;
         var4[0] = var5.getColorForState(var3[0], 0);
         var2 = 0 + 1;
         var3[var2] = ThemeUtils.CHECKED_STATE_SET;
         var4[var2] = ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated);
         ++var2;
         var3[var2] = ThemeUtils.EMPTY_STATE_SET;
         var4[var2] = var5.getDefaultColor();
      } else {
         var3[0] = ThemeUtils.DISABLED_STATE_SET;
         var4[0] = ThemeUtils.getDisabledThemeAttrColor(var1, R$attr.colorSwitchThumbNormal);
         var2 = 0 + 1;
         var3[var2] = ThemeUtils.CHECKED_STATE_SET;
         var4[var2] = ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated);
         ++var2;
         var3[var2] = ThemeUtils.EMPTY_STATE_SET;
         var4[var2] = ThemeUtils.getThemeAttrColor(var1, R$attr.colorSwitchThumbNormal);
      }

      return new ColorStateList(var3, var4);
   }

   private static PorterDuffColorFilter createTintFilter(ColorStateList var0, Mode var1, int[] var2) {
      return var0 != null && var1 != null ? getPorterDuffColorFilter(var0.getColorForState(var2, 0), var1) : null;
   }

   public static AppCompatDrawableManager get() {
      if (INSTANCE == null) {
         INSTANCE = new AppCompatDrawableManager();
         installDefaultInflateDelegates(INSTANCE);
      }

      return INSTANCE;
   }

   private Drawable getCachedDrawable(@NonNull Context var1, long var2) {
      Object var4 = this.mDrawableCacheLock;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label520: {
         LongSparseArray var5;
         try {
            var5 = (LongSparseArray)this.mDrawableCaches.get(var1);
         } catch (Throwable var78) {
            var10000 = var78;
            var10001 = false;
            break label520;
         }

         if (var5 == null) {
            label501:
            try {
               return null;
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label501;
            }
         } else {
            label524: {
               WeakReference var6;
               try {
                  var6 = (WeakReference)var5.get(var2);
               } catch (Throwable var77) {
                  var10000 = var77;
                  var10001 = false;
                  break label524;
               }

               if (var6 != null) {
                  ConstantState var81;
                  try {
                     var81 = (ConstantState)var6.get();
                  } catch (Throwable var76) {
                     var10000 = var76;
                     var10001 = false;
                     break label524;
                  }

                  if (var81 != null) {
                     try {
                        Drawable var80 = var81.newDrawable(var1.getResources());
                        return var80;
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label524;
                     }
                  }

                  try {
                     var5.delete(var2);
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break label524;
                  }
               }

               label505:
               try {
                  return null;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label505;
               }
            }
         }
      }

      while(true) {
         Throwable var79 = var10000;

         try {
            throw var79;
         } catch (Throwable var71) {
            var10000 = var71;
            var10001 = false;
            continue;
         }
      }
   }

   public static PorterDuffColorFilter getPorterDuffColorFilter(int var0, Mode var1) {
      PorterDuffColorFilter var3 = COLOR_FILTER_CACHE.get(var0, var1);
      PorterDuffColorFilter var2 = var3;
      if (var3 == null) {
         var2 = new PorterDuffColorFilter(var0, var1);
         COLOR_FILTER_CACHE.put(var0, var1, var2);
      }

      return var2;
   }

   private ColorStateList getTintListFromCache(@NonNull Context var1, @DrawableRes int var2) {
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

   static Mode getTintMode(int var0) {
      Mode var1 = null;
      if (var0 == R$drawable.abc_switch_thumb_material) {
         var1 = Mode.MULTIPLY;
      }

      return var1;
   }

   private static void installDefaultInflateDelegates(@NonNull AppCompatDrawableManager var0) {
      if (VERSION.SDK_INT < 24) {
         var0.addDelegate("vector", new AppCompatDrawableManager.VdcInflateDelegate());
         if (VERSION.SDK_INT >= 11) {
            var0.addDelegate("animated-vector", new AppCompatDrawableManager.AvdcInflateDelegate());
         }
      }

   }

   private static boolean isVectorDrawable(@NonNull Drawable var0) {
      return var0 instanceof VectorDrawableCompat || "android.graphics.drawable.VectorDrawable".equals(var0.getClass().getName());
   }

   private Drawable loadDrawableFromDelegates(@NonNull Context var1, @DrawableRes int var2) {
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

                              AppCompatDrawableManager.InflateDelegate var12;
                              try {
                                 var12 = (AppCompatDrawableManager.InflateDelegate)this.mDelegates.get(var25);
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
                     Log.e("AppCompatDrawableManager", "Exception while inflating drawable", var23);
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

   private void removeDelegate(@NonNull String var1, @NonNull AppCompatDrawableManager.InflateDelegate var2) {
      ArrayMap var3 = this.mDelegates;
      if (var3 != null && var3.get(var1) == var2) {
         this.mDelegates.remove(var1);
      }

   }

   private static void setPorterDuffColorFilter(Drawable var0, int var1, Mode var2) {
      Drawable var3 = var0;
      if (DrawableUtils.canSafelyMutateDrawable(var0)) {
         var3 = var0.mutate();
      }

      if (var2 == null) {
         var2 = DEFAULT_MODE;
      }

      var3.setColorFilter(getPorterDuffColorFilter(var1, var2));
   }

   private Drawable tintDrawable(@NonNull Context var1, @DrawableRes int var2, boolean var3, @NonNull Drawable var4) {
      ColorStateList var5 = this.getTintList(var1, var2);
      if (var5 != null) {
         Drawable var6 = var4;
         if (DrawableUtils.canSafelyMutateDrawable(var4)) {
            var6 = var4.mutate();
         }

         var6 = DrawableCompat.wrap(var6);
         DrawableCompat.setTintList(var6, var5);
         Mode var7 = getTintMode(var2);
         if (var7 != null) {
            DrawableCompat.setTintMode(var6, var7);
         }

         return var6;
      } else {
         LayerDrawable var8;
         if (var2 == R$drawable.abc_seekbar_track_material) {
            var8 = (LayerDrawable)var4;
            setPorterDuffColorFilter(var8.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(var8.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(var8.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated), DEFAULT_MODE);
            return var4;
         } else {
            if (var2 != R$drawable.abc_ratingbar_material && var2 != R$drawable.abc_ratingbar_indicator_material && var2 != R$drawable.abc_ratingbar_small_material) {
               if (!tintDrawableUsingColorFilter(var1, var2, var4) && var3) {
                  return null;
               }
            } else {
               var8 = (LayerDrawable)var4;
               setPorterDuffColorFilter(var8.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(var1, R$attr.colorControlNormal), DEFAULT_MODE);
               setPorterDuffColorFilter(var8.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated), DEFAULT_MODE);
               setPorterDuffColorFilter(var8.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(var1, R$attr.colorControlActivated), DEFAULT_MODE);
            }

            return var4;
         }
      }
   }

   static void tintDrawable(Drawable var0, TintInfo var1, int[] var2) {
      if (DrawableUtils.canSafelyMutateDrawable(var0) && var0.mutate() != var0) {
         Log.d("AppCompatDrawableManager", "Mutated drawable is not the same instance as the input.");
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

   static boolean tintDrawableUsingColorFilter(@NonNull Context var0, @DrawableRes int var1, @NonNull Drawable var2) {
      Mode var8 = DEFAULT_MODE;
      boolean var3 = false;
      int var4 = 0;
      byte var6 = -1;
      int var5;
      Mode var7;
      if (arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, var1)) {
         var4 = R$attr.colorControlNormal;
         var3 = true;
         var7 = var8;
         var5 = var6;
      } else if (arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, var1)) {
         var4 = R$attr.colorControlActivated;
         var3 = true;
         var7 = var8;
         var5 = var6;
      } else if (arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, var1)) {
         var4 = 16842801;
         var3 = true;
         var7 = Mode.MULTIPLY;
         var5 = var6;
      } else if (var1 == R$drawable.abc_list_divider_mtrl_alpha) {
         var4 = 16842800;
         var3 = true;
         var5 = Math.round(40.8F);
         var7 = var8;
      } else {
         var7 = var8;
         var5 = var6;
         if (var1 == R$drawable.abc_dialog_material_background) {
            var4 = 16842801;
            var3 = true;
            var5 = var6;
            var7 = var8;
         }
      }

      if (var3) {
         Drawable var9 = var2;
         if (DrawableUtils.canSafelyMutateDrawable(var2)) {
            var9 = var2.mutate();
         }

         var9.setColorFilter(getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(var0, var4), var7));
         if (var5 != -1) {
            var9.setAlpha(var5);
         }

         return true;
      } else {
         return false;
      }
   }

   public Drawable getDrawable(@NonNull Context var1, @DrawableRes int var2) {
      return this.getDrawable(var1, var2, false);
   }

   Drawable getDrawable(@NonNull Context var1, @DrawableRes int var2, boolean var3) {
      this.checkVectorDrawableSetup(var1);
      Drawable var5 = this.loadDrawableFromDelegates(var1, var2);
      Drawable var4 = var5;
      if (var5 == null) {
         var4 = this.createDrawableIfNeeded(var1, var2);
      }

      var5 = var4;
      if (var4 == null) {
         var5 = ContextCompat.getDrawable(var1, var2);
      }

      var4 = var5;
      if (var5 != null) {
         var4 = this.tintDrawable(var1, var2, var3, var5);
      }

      if (var4 != null) {
         DrawableUtils.fixDrawable(var4);
      }

      return var4;
   }

   ColorStateList getTintList(@NonNull Context var1, @DrawableRes int var2) {
      ColorStateList var3 = this.getTintListFromCache(var1, var2);
      ColorStateList var4 = var3;
      if (var3 == null) {
         if (var2 == R$drawable.abc_edit_text_material) {
            var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_edittext);
         } else if (var2 == R$drawable.abc_switch_track_mtrl_alpha) {
            var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_switch_track);
         } else if (var2 == R$drawable.abc_switch_thumb_material) {
            var3 = this.createSwitchThumbColorStateList(var1);
         } else if (var2 == R$drawable.abc_btn_default_mtrl_shape) {
            var3 = this.createDefaultButtonColorStateList(var1);
         } else if (var2 == R$drawable.abc_btn_borderless_material) {
            var3 = this.createBorderlessButtonColorStateList(var1);
         } else if (var2 == R$drawable.abc_btn_colored_material) {
            var3 = this.createColoredButtonColorStateList(var1);
         } else if (var2 != R$drawable.abc_spinner_mtrl_am_alpha && var2 != R$drawable.abc_spinner_textfield_background_material) {
            if (arrayContains(TINT_COLOR_CONTROL_NORMAL, var2)) {
               var3 = ThemeUtils.getThemeAttrColorStateList(var1, R$attr.colorControlNormal);
            } else if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, var2)) {
               var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_default);
            } else if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, var2)) {
               var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_btn_checkable);
            } else if (var2 == R$drawable.abc_seekbar_thumb_material) {
               var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_seek_thumb);
            }
         } else {
            var3 = AppCompatResources.getColorStateList(var1, R$color.abc_tint_spinner);
         }

         var4 = var3;
         if (var3 != null) {
            this.addTintListToCache(var1, var2, var3);
            var4 = var3;
         }
      }

      return var4;
   }

   public void onConfigurationChanged(@NonNull Context var1) {
      Object var2 = this.mDrawableCacheLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label176: {
         LongSparseArray var23;
         try {
            var23 = (LongSparseArray)this.mDrawableCaches.get(var1);
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label176;
         }

         if (var23 != null) {
            try {
               var23.clear();
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label176;
            }
         }

         label165:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label165;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   Drawable onDrawableLoadedFromResources(@NonNull Context var1, @NonNull VectorEnabledTintResources var2, @DrawableRes int var3) {
      Drawable var5 = this.loadDrawableFromDelegates(var1, var3);
      Drawable var4 = var5;
      if (var5 == null) {
         var4 = var2.superGetDrawable(var3);
      }

      return var4 != null ? this.tintDrawable(var1, var3, false, var4) : null;
   }

   @RequiresApi(11)
   private static class AvdcInflateDelegate implements AppCompatDrawableManager.InflateDelegate {
      AvdcInflateDelegate() {
      }

      public Drawable createFromXmlInner(@NonNull Context var1, @NonNull XmlPullParser var2, @NonNull AttributeSet var3, @Nullable Theme var4) {
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
      Drawable createFromXmlInner(@NonNull Context var1, @NonNull XmlPullParser var2, @NonNull AttributeSet var3, @Nullable Theme var4);
   }

   private static class VdcInflateDelegate implements AppCompatDrawableManager.InflateDelegate {
      VdcInflateDelegate() {
      }

      public Drawable createFromXmlInner(@NonNull Context var1, @NonNull XmlPullParser var2, @NonNull AttributeSet var3, @Nullable Theme var4) {
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
