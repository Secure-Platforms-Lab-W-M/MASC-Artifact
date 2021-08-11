/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.LayerDrawable
 *  androidx.appcompat.R
 *  androidx.appcompat.R$attr
 *  androidx.appcompat.R$color
 *  androidx.appcompat.R$drawable
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.appcompat.widget.ThemeUtils;
import androidx.appcompat.widget.TintInfo;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.core.graphics.ColorUtils;

public final class AppCompatDrawableManager {
    private static final boolean DEBUG = false;
    private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    private static AppCompatDrawableManager INSTANCE;
    private static final String TAG = "AppCompatDrawableManag";
    private ResourceManagerInternal mResourceManager;

    public static AppCompatDrawableManager get() {
        synchronized (AppCompatDrawableManager.class) {
            if (INSTANCE == null) {
                AppCompatDrawableManager.preload();
            }
            AppCompatDrawableManager appCompatDrawableManager = INSTANCE;
            return appCompatDrawableManager;
        }
    }

    public static PorterDuffColorFilter getPorterDuffColorFilter(int n, PorterDuff.Mode mode) {
        synchronized (AppCompatDrawableManager.class) {
            mode = ResourceManagerInternal.getPorterDuffColorFilter(n, mode);
            return mode;
        }
    }

    public static void preload() {
        synchronized (AppCompatDrawableManager.class) {
            if (INSTANCE == null) {
                AppCompatDrawableManager appCompatDrawableManager;
                INSTANCE = appCompatDrawableManager = new AppCompatDrawableManager();
                appCompatDrawableManager.mResourceManager = ResourceManagerInternal.get();
                AppCompatDrawableManager.INSTANCE.mResourceManager.setHooks(new ResourceManagerInternal.ResourceManagerHooks(){
                    private final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult};
                    private final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_dark, R.drawable.abc_text_select_handle_middle_mtrl_dark, R.drawable.abc_text_select_handle_right_mtrl_dark, R.drawable.abc_text_select_handle_left_mtrl_light, R.drawable.abc_text_select_handle_middle_mtrl_light, R.drawable.abc_text_select_handle_right_mtrl_light};
                    private final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha};
                    private final int[] TINT_CHECKABLE_BUTTON_LIST = new int[]{R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material, R.drawable.abc_btn_check_material_anim, R.drawable.abc_btn_radio_material_anim};
                    private final int[] TINT_COLOR_CONTROL_NORMAL = new int[]{R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha};
                    private final int[] TINT_COLOR_CONTROL_STATE_LIST = new int[]{R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material};

                    private boolean arrayContains(int[] arrn, int n) {
                        int n2 = arrn.length;
                        for (int i = 0; i < n2; ++i) {
                            if (arrn[i] != n) continue;
                            return true;
                        }
                        return false;
                    }

                    private ColorStateList createBorderlessButtonColorStateList(Context context) {
                        return this.createButtonColorStateList(context, 0);
                    }

                    private ColorStateList createButtonColorStateList(Context context, int n) {
                        int[][] arrarrn = new int[4][];
                        int[] arrn = new int[4];
                        int n2 = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlHighlight);
                        int n3 = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);
                        arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
                        arrn[0] = n3;
                        n3 = 0 + 1;
                        arrarrn[n3] = ThemeUtils.PRESSED_STATE_SET;
                        arrn[n3] = ColorUtils.compositeColors(n2, n);
                        arrarrn[++n3] = ThemeUtils.FOCUSED_STATE_SET;
                        arrn[n3] = ColorUtils.compositeColors(n2, n);
                        n2 = n3 + 1;
                        arrarrn[n2] = ThemeUtils.EMPTY_STATE_SET;
                        arrn[n2] = n;
                        return new ColorStateList((int[][])arrarrn, arrn);
                    }

                    private ColorStateList createColoredButtonColorStateList(Context context) {
                        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorAccent));
                    }

                    private ColorStateList createDefaultButtonColorStateList(Context context) {
                        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorButtonNormal));
                    }

                    private ColorStateList createSwitchThumbColorStateList(Context context) {
                        int[][] arrarrn = new int[3][];
                        int[] arrn = new int[3];
                        ColorStateList colorStateList = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorSwitchThumbNormal);
                        if (colorStateList != null && colorStateList.isStateful()) {
                            arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
                            arrn[0] = colorStateList.getColorForState(arrarrn[0], 0);
                            int n = 0 + 1;
                            arrarrn[n] = ThemeUtils.CHECKED_STATE_SET;
                            arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
                            arrarrn[++n] = ThemeUtils.EMPTY_STATE_SET;
                            arrn[n] = colorStateList.getDefaultColor();
                        } else {
                            arrarrn[0] = ThemeUtils.DISABLED_STATE_SET;
                            arrn[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
                            int n = 0 + 1;
                            arrarrn[n] = ThemeUtils.CHECKED_STATE_SET;
                            arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
                            arrarrn[++n] = ThemeUtils.EMPTY_STATE_SET;
                            arrn[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
                        }
                        return new ColorStateList((int[][])arrarrn, arrn);
                    }

                    private void setPorterDuffColorFilter(Drawable drawable2, int n, PorterDuff.Mode mode) {
                        Drawable drawable3 = drawable2;
                        if (DrawableUtils.canSafelyMutateDrawable(drawable2)) {
                            drawable3 = drawable2.mutate();
                        }
                        if (mode == null) {
                            mode = DEFAULT_MODE;
                        }
                        drawable3.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(n, mode));
                    }

                    @Override
                    public Drawable createDrawableFor(ResourceManagerInternal resourceManagerInternal, Context context, int n) {
                        if (n == R.drawable.abc_cab_background_top_material) {
                            return new LayerDrawable(new Drawable[]{resourceManagerInternal.getDrawable(context, R.drawable.abc_cab_background_internal_bg), resourceManagerInternal.getDrawable(context, R.drawable.abc_cab_background_top_mtrl_alpha)});
                        }
                        return null;
                    }

                    @Override
                    public ColorStateList getTintListForDrawableRes(Context context, int n) {
                        if (n == R.drawable.abc_edit_text_material) {
                            return AppCompatResources.getColorStateList(context, R.color.abc_tint_edittext);
                        }
                        if (n == R.drawable.abc_switch_track_mtrl_alpha) {
                            return AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_track);
                        }
                        if (n == R.drawable.abc_switch_thumb_material) {
                            return this.createSwitchThumbColorStateList(context);
                        }
                        if (n == R.drawable.abc_btn_default_mtrl_shape) {
                            return this.createDefaultButtonColorStateList(context);
                        }
                        if (n == R.drawable.abc_btn_borderless_material) {
                            return this.createBorderlessButtonColorStateList(context);
                        }
                        if (n == R.drawable.abc_btn_colored_material) {
                            return this.createColoredButtonColorStateList(context);
                        }
                        if (n != R.drawable.abc_spinner_mtrl_am_alpha && n != R.drawable.abc_spinner_textfield_background_material) {
                            if (this.arrayContains(this.TINT_COLOR_CONTROL_NORMAL, n)) {
                                return ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
                            }
                            if (this.arrayContains(this.TINT_COLOR_CONTROL_STATE_LIST, n)) {
                                return AppCompatResources.getColorStateList(context, R.color.abc_tint_default);
                            }
                            if (this.arrayContains(this.TINT_CHECKABLE_BUTTON_LIST, n)) {
                                return AppCompatResources.getColorStateList(context, R.color.abc_tint_btn_checkable);
                            }
                            if (n == R.drawable.abc_seekbar_thumb_material) {
                                return AppCompatResources.getColorStateList(context, R.color.abc_tint_seek_thumb);
                            }
                            return null;
                        }
                        return AppCompatResources.getColorStateList(context, R.color.abc_tint_spinner);
                    }

                    @Override
                    public PorterDuff.Mode getTintModeForDrawableRes(int n) {
                        PorterDuff.Mode mode = null;
                        if (n == R.drawable.abc_switch_thumb_material) {
                            mode = PorterDuff.Mode.MULTIPLY;
                        }
                        return mode;
                    }

                    @Override
                    public boolean tintDrawable(Context context, int n, Drawable drawable2) {
                        if (n == R.drawable.abc_seekbar_track_material) {
                            drawable2 = (LayerDrawable)drawable2;
                            this.setPorterDuffColorFilter(drawable2.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
                            this.setPorterDuffColorFilter(drawable2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
                            this.setPorterDuffColorFilter(drawable2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
                            return true;
                        }
                        if (n != R.drawable.abc_ratingbar_material && n != R.drawable.abc_ratingbar_indicator_material && n != R.drawable.abc_ratingbar_small_material) {
                            return false;
                        }
                        drawable2 = (LayerDrawable)drawable2;
                        this.setPorterDuffColorFilter(drawable2.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
                        this.setPorterDuffColorFilter(drawable2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
                        this.setPorterDuffColorFilter(drawable2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
                        return true;
                    }

                    @Override
                    public boolean tintDrawableUsingColorFilter(Context context, int n, Drawable drawable2) {
                        int n2;
                        PorterDuff.Mode mode;
                        PorterDuff.Mode mode2 = DEFAULT_MODE;
                        boolean bl = false;
                        int n3 = 0;
                        int n4 = -1;
                        if (this.arrayContains(this.COLORFILTER_TINT_COLOR_CONTROL_NORMAL, n)) {
                            n3 = R.attr.colorControlNormal;
                            bl = true;
                            mode = mode2;
                            n2 = n4;
                        } else if (this.arrayContains(this.COLORFILTER_COLOR_CONTROL_ACTIVATED, n)) {
                            n3 = R.attr.colorControlActivated;
                            bl = true;
                            mode = mode2;
                            n2 = n4;
                        } else if (this.arrayContains(this.COLORFILTER_COLOR_BACKGROUND_MULTIPLY, n)) {
                            n3 = 16842801;
                            bl = true;
                            mode = PorterDuff.Mode.MULTIPLY;
                            n2 = n4;
                        } else if (n == R.drawable.abc_list_divider_mtrl_alpha) {
                            n3 = 16842800;
                            bl = true;
                            n2 = Math.round(40.8f);
                            mode = mode2;
                        } else {
                            mode = mode2;
                            n2 = n4;
                            if (n == R.drawable.abc_dialog_material_background) {
                                n3 = 16842801;
                                bl = true;
                                n2 = n4;
                                mode = mode2;
                            }
                        }
                        if (bl) {
                            mode2 = drawable2;
                            if (DrawableUtils.canSafelyMutateDrawable(drawable2)) {
                                mode2 = drawable2.mutate();
                            }
                            mode2.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, n3), mode));
                            if (n2 != -1) {
                                mode2.setAlpha(n2);
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
            return;
        }
    }

    static void tintDrawable(Drawable drawable2, TintInfo tintInfo, int[] arrn) {
        ResourceManagerInternal.tintDrawable(drawable2, tintInfo, arrn);
    }

    public Drawable getDrawable(Context context, int n) {
        synchronized (this) {
            context = this.mResourceManager.getDrawable(context, n);
            return context;
        }
    }

    Drawable getDrawable(Context context, int n, boolean bl) {
        synchronized (this) {
            context = this.mResourceManager.getDrawable(context, n, bl);
            return context;
        }
    }

    ColorStateList getTintList(Context context, int n) {
        synchronized (this) {
            context = this.mResourceManager.getTintList(context, n);
            return context;
        }
    }

    public void onConfigurationChanged(Context context) {
        synchronized (this) {
            this.mResourceManager.onConfigurationChanged(context);
            return;
        }
    }

    Drawable onDrawableLoadedFromResources(Context context, VectorEnabledTintResources vectorEnabledTintResources, int n) {
        synchronized (this) {
            context = this.mResourceManager.onDrawableLoadedFromResources(context, vectorEnabledTintResources, n);
            return context;
        }
    }

    boolean tintDrawableUsingColorFilter(Context context, int n, Drawable drawable2) {
        return this.mResourceManager.tintDrawableUsingColorFilter(context, n, drawable2);
    }

}

