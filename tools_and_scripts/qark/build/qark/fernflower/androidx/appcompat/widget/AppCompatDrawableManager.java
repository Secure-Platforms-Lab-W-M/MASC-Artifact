package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.appcompat.R.attr;
import androidx.appcompat.R.color;
import androidx.appcompat.R.drawable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.ColorUtils;

public final class AppCompatDrawableManager {
   private static final boolean DEBUG = false;
   private static final Mode DEFAULT_MODE;
   private static AppCompatDrawableManager INSTANCE;
   private static final String TAG = "AppCompatDrawableManag";
   private ResourceManagerInternal mResourceManager;

   static {
      DEFAULT_MODE = Mode.SRC_IN;
   }

   public static AppCompatDrawableManager get() {
      synchronized(AppCompatDrawableManager.class){}

      AppCompatDrawableManager var0;
      try {
         if (INSTANCE == null) {
            preload();
         }

         var0 = INSTANCE;
      } finally {
         ;
      }

      return var0;
   }

   public static PorterDuffColorFilter getPorterDuffColorFilter(int var0, Mode var1) {
      synchronized(AppCompatDrawableManager.class){}

      PorterDuffColorFilter var4;
      try {
         var4 = ResourceManagerInternal.getPorterDuffColorFilter(var0, var1);
      } finally {
         ;
      }

      return var4;
   }

   public static void preload() {
      synchronized(AppCompatDrawableManager.class){}

      try {
         if (INSTANCE == null) {
            AppCompatDrawableManager var0 = new AppCompatDrawableManager();
            INSTANCE = var0;
            var0.mResourceManager = ResourceManagerInternal.get();
            INSTANCE.mResourceManager.setHooks(new ResourceManagerInternal.ResourceManagerHooks() {
               private final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
               private final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
               private final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
               private final int[] TINT_CHECKABLE_BUTTON_LIST;
               private final int[] TINT_COLOR_CONTROL_NORMAL;
               private final int[] TINT_COLOR_CONTROL_STATE_LIST;

               {
                  this.COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{drawable.abc_textfield_search_default_mtrl_alpha, drawable.abc_textfield_default_mtrl_alpha, drawable.abc_ab_share_pack_mtrl_alpha};
                  this.TINT_COLOR_CONTROL_NORMAL = new int[]{drawable.abc_ic_commit_search_api_mtrl_alpha, drawable.abc_seekbar_tick_mark_material, drawable.abc_ic_menu_share_mtrl_alpha, drawable.abc_ic_menu_copy_mtrl_am_alpha, drawable.abc_ic_menu_cut_mtrl_alpha, drawable.abc_ic_menu_selectall_mtrl_alpha, drawable.abc_ic_menu_paste_mtrl_am_alpha};
                  this.COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{drawable.abc_textfield_activated_mtrl_alpha, drawable.abc_textfield_search_activated_mtrl_alpha, drawable.abc_cab_background_top_mtrl_alpha, drawable.abc_text_cursor_material, drawable.abc_text_select_handle_left_mtrl_dark, drawable.abc_text_select_handle_middle_mtrl_dark, drawable.abc_text_select_handle_right_mtrl_dark, drawable.abc_text_select_handle_left_mtrl_light, drawable.abc_text_select_handle_middle_mtrl_light, drawable.abc_text_select_handle_right_mtrl_light};
                  this.COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{drawable.abc_popup_background_mtrl_mult, drawable.abc_cab_background_internal_bg, drawable.abc_menu_hardkey_panel_mtrl_mult};
                  this.TINT_COLOR_CONTROL_STATE_LIST = new int[]{drawable.abc_tab_indicator_material, drawable.abc_textfield_search_material};
                  this.TINT_CHECKABLE_BUTTON_LIST = new int[]{drawable.abc_btn_check_material, drawable.abc_btn_radio_material, drawable.abc_btn_check_material_anim, drawable.abc_btn_radio_material_anim};
               }

               private boolean arrayContains(int[] var1, int var2) {
                  int var4 = var1.length;

                  for(int var3 = 0; var3 < var4; ++var3) {
                     if (var1[var3] == var2) {
                        return true;
                     }
                  }

                  return false;
               }

               private ColorStateList createBorderlessButtonColorStateList(Context var1) {
                  return this.createButtonColorStateList(var1, 0);
               }

               private ColorStateList createButtonColorStateList(Context var1, int var2) {
                  int[][] var5 = new int[4][];
                  int[] var6 = new int[4];
                  int var3 = ThemeUtils.getThemeAttrColor(var1, attr.colorControlHighlight);
                  int var4 = ThemeUtils.getDisabledThemeAttrColor(var1, attr.colorButtonNormal);
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

               private ColorStateList createColoredButtonColorStateList(Context var1) {
                  return this.createButtonColorStateList(var1, ThemeUtils.getThemeAttrColor(var1, attr.colorAccent));
               }

               private ColorStateList createDefaultButtonColorStateList(Context var1) {
                  return this.createButtonColorStateList(var1, ThemeUtils.getThemeAttrColor(var1, attr.colorButtonNormal));
               }

               private ColorStateList createSwitchThumbColorStateList(Context var1) {
                  int[][] var3 = new int[3][];
                  int[] var4 = new int[3];
                  ColorStateList var5 = ThemeUtils.getThemeAttrColorStateList(var1, attr.colorSwitchThumbNormal);
                  int var2;
                  if (var5 != null && var5.isStateful()) {
                     var3[0] = ThemeUtils.DISABLED_STATE_SET;
                     var4[0] = var5.getColorForState(var3[0], 0);
                     var2 = 0 + 1;
                     var3[var2] = ThemeUtils.CHECKED_STATE_SET;
                     var4[var2] = ThemeUtils.getThemeAttrColor(var1, attr.colorControlActivated);
                     ++var2;
                     var3[var2] = ThemeUtils.EMPTY_STATE_SET;
                     var4[var2] = var5.getDefaultColor();
                  } else {
                     var3[0] = ThemeUtils.DISABLED_STATE_SET;
                     var4[0] = ThemeUtils.getDisabledThemeAttrColor(var1, attr.colorSwitchThumbNormal);
                     var2 = 0 + 1;
                     var3[var2] = ThemeUtils.CHECKED_STATE_SET;
                     var4[var2] = ThemeUtils.getThemeAttrColor(var1, attr.colorControlActivated);
                     ++var2;
                     var3[var2] = ThemeUtils.EMPTY_STATE_SET;
                     var4[var2] = ThemeUtils.getThemeAttrColor(var1, attr.colorSwitchThumbNormal);
                  }

                  return new ColorStateList(var3, var4);
               }

               private void setPorterDuffColorFilter(Drawable var1, int var2, Mode var3) {
                  Drawable var4 = var1;
                  if (DrawableUtils.canSafelyMutateDrawable(var1)) {
                     var4 = var1.mutate();
                  }

                  if (var3 == null) {
                     var3 = AppCompatDrawableManager.DEFAULT_MODE;
                  }

                  var4.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(var2, var3));
               }

               public Drawable createDrawableFor(ResourceManagerInternal var1, Context var2, int var3) {
                  return var3 == drawable.abc_cab_background_top_material ? new LayerDrawable(new Drawable[]{var1.getDrawable(var2, drawable.abc_cab_background_internal_bg), var1.getDrawable(var2, drawable.abc_cab_background_top_mtrl_alpha)}) : null;
               }

               public ColorStateList getTintListForDrawableRes(Context var1, int var2) {
                  if (var2 == drawable.abc_edit_text_material) {
                     return AppCompatResources.getColorStateList(var1, color.abc_tint_edittext);
                  } else if (var2 == drawable.abc_switch_track_mtrl_alpha) {
                     return AppCompatResources.getColorStateList(var1, color.abc_tint_switch_track);
                  } else if (var2 == drawable.abc_switch_thumb_material) {
                     return this.createSwitchThumbColorStateList(var1);
                  } else if (var2 == drawable.abc_btn_default_mtrl_shape) {
                     return this.createDefaultButtonColorStateList(var1);
                  } else if (var2 == drawable.abc_btn_borderless_material) {
                     return this.createBorderlessButtonColorStateList(var1);
                  } else if (var2 == drawable.abc_btn_colored_material) {
                     return this.createColoredButtonColorStateList(var1);
                  } else if (var2 != drawable.abc_spinner_mtrl_am_alpha && var2 != drawable.abc_spinner_textfield_background_material) {
                     if (this.arrayContains(this.TINT_COLOR_CONTROL_NORMAL, var2)) {
                        return ThemeUtils.getThemeAttrColorStateList(var1, attr.colorControlNormal);
                     } else if (this.arrayContains(this.TINT_COLOR_CONTROL_STATE_LIST, var2)) {
                        return AppCompatResources.getColorStateList(var1, color.abc_tint_default);
                     } else if (this.arrayContains(this.TINT_CHECKABLE_BUTTON_LIST, var2)) {
                        return AppCompatResources.getColorStateList(var1, color.abc_tint_btn_checkable);
                     } else {
                        return var2 == drawable.abc_seekbar_thumb_material ? AppCompatResources.getColorStateList(var1, color.abc_tint_seek_thumb) : null;
                     }
                  } else {
                     return AppCompatResources.getColorStateList(var1, color.abc_tint_spinner);
                  }
               }

               public Mode getTintModeForDrawableRes(int var1) {
                  Mode var2 = null;
                  if (var1 == drawable.abc_switch_thumb_material) {
                     var2 = Mode.MULTIPLY;
                  }

                  return var2;
               }

               public boolean tintDrawable(Context var1, int var2, Drawable var3) {
                  LayerDrawable var4;
                  if (var2 == drawable.abc_seekbar_track_material) {
                     var4 = (LayerDrawable)var3;
                     this.setPorterDuffColorFilter(var4.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(var1, attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                     this.setPorterDuffColorFilter(var4.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(var1, attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                     this.setPorterDuffColorFilter(var4.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(var1, attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                     return true;
                  } else if (var2 != drawable.abc_ratingbar_material && var2 != drawable.abc_ratingbar_indicator_material && var2 != drawable.abc_ratingbar_small_material) {
                     return false;
                  } else {
                     var4 = (LayerDrawable)var3;
                     this.setPorterDuffColorFilter(var4.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(var1, attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                     this.setPorterDuffColorFilter(var4.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(var1, attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                     this.setPorterDuffColorFilter(var4.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(var1, attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                     return true;
                  }
               }

               public boolean tintDrawableUsingColorFilter(Context var1, int var2, Drawable var3) {
                  Mode var9 = AppCompatDrawableManager.DEFAULT_MODE;
                  boolean var4 = false;
                  int var5 = 0;
                  byte var7 = -1;
                  int var6;
                  Mode var8;
                  if (this.arrayContains(this.COLORFILTER_TINT_COLOR_CONTROL_NORMAL, var2)) {
                     var5 = attr.colorControlNormal;
                     var4 = true;
                     var8 = var9;
                     var6 = var7;
                  } else if (this.arrayContains(this.COLORFILTER_COLOR_CONTROL_ACTIVATED, var2)) {
                     var5 = attr.colorControlActivated;
                     var4 = true;
                     var8 = var9;
                     var6 = var7;
                  } else if (this.arrayContains(this.COLORFILTER_COLOR_BACKGROUND_MULTIPLY, var2)) {
                     var5 = 16842801;
                     var4 = true;
                     var8 = Mode.MULTIPLY;
                     var6 = var7;
                  } else if (var2 == drawable.abc_list_divider_mtrl_alpha) {
                     var5 = 16842800;
                     var4 = true;
                     var6 = Math.round(40.8F);
                     var8 = var9;
                  } else {
                     var8 = var9;
                     var6 = var7;
                     if (var2 == drawable.abc_dialog_material_background) {
                        var5 = 16842801;
                        var4 = true;
                        var6 = var7;
                        var8 = var9;
                     }
                  }

                  if (var4) {
                     Drawable var10 = var3;
                     if (DrawableUtils.canSafelyMutateDrawable(var3)) {
                        var10 = var3.mutate();
                     }

                     var10.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(var1, var5), var8));
                     if (var6 != -1) {
                        var10.setAlpha(var6);
                     }

                     return true;
                  } else {
                     return false;
                  }
               }
            });
         }
      } finally {
         ;
      }

   }

   static void tintDrawable(Drawable var0, TintInfo var1, int[] var2) {
      ResourceManagerInternal.tintDrawable(var0, var1, var2);
   }

   public Drawable getDrawable(Context var1, int var2) {
      synchronized(this){}

      Drawable var5;
      try {
         var5 = this.mResourceManager.getDrawable(var1, var2);
      } finally {
         ;
      }

      return var5;
   }

   Drawable getDrawable(Context var1, int var2, boolean var3) {
      synchronized(this){}

      Drawable var6;
      try {
         var6 = this.mResourceManager.getDrawable(var1, var2, var3);
      } finally {
         ;
      }

      return var6;
   }

   ColorStateList getTintList(Context var1, int var2) {
      synchronized(this){}

      ColorStateList var5;
      try {
         var5 = this.mResourceManager.getTintList(var1, var2);
      } finally {
         ;
      }

      return var5;
   }

   public void onConfigurationChanged(Context var1) {
      synchronized(this){}

      try {
         this.mResourceManager.onConfigurationChanged(var1);
      } finally {
         ;
      }

   }

   Drawable onDrawableLoadedFromResources(Context var1, VectorEnabledTintResources var2, int var3) {
      synchronized(this){}

      Drawable var6;
      try {
         var6 = this.mResourceManager.onDrawableLoadedFromResources(var1, var2, var3);
      } finally {
         ;
      }

      return var6;
   }

   boolean tintDrawableUsingColorFilter(Context var1, int var2, Drawable var3) {
      return this.mResourceManager.tintDrawableUsingColorFilter(var1, var2, var3);
   }
}
