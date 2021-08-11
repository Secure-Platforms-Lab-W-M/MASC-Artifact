// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.R$color;
import android.graphics.drawable.LayerDrawable;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.R$attr;
import android.content.res.ColorStateList;
import android.content.Context;
import androidx.appcompat.R$drawable;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;

public final class AppCompatDrawableManager
{
    private static final boolean DEBUG = false;
    private static final PorterDuff$Mode DEFAULT_MODE;
    private static AppCompatDrawableManager INSTANCE;
    private static final String TAG = "AppCompatDrawableManag";
    private ResourceManagerInternal mResourceManager;
    
    static {
        DEFAULT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    public static AppCompatDrawableManager get() {
        synchronized (AppCompatDrawableManager.class) {
            if (AppCompatDrawableManager.INSTANCE == null) {
                preload();
            }
            return AppCompatDrawableManager.INSTANCE;
        }
    }
    
    public static PorterDuffColorFilter getPorterDuffColorFilter(final int n, final PorterDuff$Mode porterDuff$Mode) {
        synchronized (AppCompatDrawableManager.class) {
            return ResourceManagerInternal.getPorterDuffColorFilter(n, porterDuff$Mode);
        }
    }
    
    public static void preload() {
        synchronized (AppCompatDrawableManager.class) {
            if (AppCompatDrawableManager.INSTANCE == null) {
                (AppCompatDrawableManager.INSTANCE = new AppCompatDrawableManager()).mResourceManager = ResourceManagerInternal.get();
                AppCompatDrawableManager.INSTANCE.mResourceManager.setHooks((ResourceManagerInternal.ResourceManagerHooks)new ResourceManagerInternal.ResourceManagerHooks() {
                    private final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = { R$drawable.abc_popup_background_mtrl_mult, R$drawable.abc_cab_background_internal_bg, R$drawable.abc_menu_hardkey_panel_mtrl_mult };
                    private final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = { R$drawable.abc_textfield_activated_mtrl_alpha, R$drawable.abc_textfield_search_activated_mtrl_alpha, R$drawable.abc_cab_background_top_mtrl_alpha, R$drawable.abc_text_cursor_material, R$drawable.abc_text_select_handle_left_mtrl_dark, R$drawable.abc_text_select_handle_middle_mtrl_dark, R$drawable.abc_text_select_handle_right_mtrl_dark, R$drawable.abc_text_select_handle_left_mtrl_light, R$drawable.abc_text_select_handle_middle_mtrl_light, R$drawable.abc_text_select_handle_right_mtrl_light };
                    private final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = { R$drawable.abc_textfield_search_default_mtrl_alpha, R$drawable.abc_textfield_default_mtrl_alpha, R$drawable.abc_ab_share_pack_mtrl_alpha };
                    private final int[] TINT_CHECKABLE_BUTTON_LIST = { R$drawable.abc_btn_check_material, R$drawable.abc_btn_radio_material, R$drawable.abc_btn_check_material_anim, R$drawable.abc_btn_radio_material_anim };
                    private final int[] TINT_COLOR_CONTROL_NORMAL = { R$drawable.abc_ic_commit_search_api_mtrl_alpha, R$drawable.abc_seekbar_tick_mark_material, R$drawable.abc_ic_menu_share_mtrl_alpha, R$drawable.abc_ic_menu_copy_mtrl_am_alpha, R$drawable.abc_ic_menu_cut_mtrl_alpha, R$drawable.abc_ic_menu_selectall_mtrl_alpha, R$drawable.abc_ic_menu_paste_mtrl_am_alpha };
                    private final int[] TINT_COLOR_CONTROL_STATE_LIST = { R$drawable.abc_tab_indicator_material, R$drawable.abc_textfield_search_material };
                    
                    private boolean arrayContains(final int[] array, final int n) {
                        for (int length = array.length, i = 0; i < length; ++i) {
                            if (array[i] == n) {
                                return true;
                            }
                        }
                        return false;
                    }
                    
                    private ColorStateList createBorderlessButtonColorStateList(final Context context) {
                        return this.createButtonColorStateList(context, 0);
                    }
                    
                    private ColorStateList createButtonColorStateList(final Context context, final int n) {
                        final int[][] array = new int[4][];
                        final int[] array2 = new int[4];
                        final int themeAttrColor = ThemeUtils.getThemeAttrColor(context, R$attr.colorControlHighlight);
                        final int disabledThemeAttrColor = ThemeUtils.getDisabledThemeAttrColor(context, R$attr.colorButtonNormal);
                        array[0] = ThemeUtils.DISABLED_STATE_SET;
                        array2[0] = disabledThemeAttrColor;
                        final int n2 = 0 + 1;
                        array[n2] = ThemeUtils.PRESSED_STATE_SET;
                        array2[n2] = ColorUtils.compositeColors(themeAttrColor, n);
                        final int n3 = n2 + 1;
                        array[n3] = ThemeUtils.FOCUSED_STATE_SET;
                        array2[n3] = ColorUtils.compositeColors(themeAttrColor, n);
                        final int n4 = n3 + 1;
                        array[n4] = ThemeUtils.EMPTY_STATE_SET;
                        array2[n4] = n;
                        return new ColorStateList(array, array2);
                    }
                    
                    private ColorStateList createColoredButtonColorStateList(final Context context) {
                        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R$attr.colorAccent));
                    }
                    
                    private ColorStateList createDefaultButtonColorStateList(final Context context) {
                        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R$attr.colorButtonNormal));
                    }
                    
                    private ColorStateList createSwitchThumbColorStateList(final Context context) {
                        final int[][] array = new int[3][];
                        final int[] array2 = new int[3];
                        final ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, R$attr.colorSwitchThumbNormal);
                        if (themeAttrColorStateList != null && themeAttrColorStateList.isStateful()) {
                            array[0] = ThemeUtils.DISABLED_STATE_SET;
                            array2[0] = themeAttrColorStateList.getColorForState(array[0], 0);
                            final int n = 0 + 1;
                            array[n] = ThemeUtils.CHECKED_STATE_SET;
                            array2[n] = ThemeUtils.getThemeAttrColor(context, R$attr.colorControlActivated);
                            final int n2 = n + 1;
                            array[n2] = ThemeUtils.EMPTY_STATE_SET;
                            array2[n2] = themeAttrColorStateList.getDefaultColor();
                        }
                        else {
                            array[0] = ThemeUtils.DISABLED_STATE_SET;
                            array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R$attr.colorSwitchThumbNormal);
                            final int n3 = 0 + 1;
                            array[n3] = ThemeUtils.CHECKED_STATE_SET;
                            array2[n3] = ThemeUtils.getThemeAttrColor(context, R$attr.colorControlActivated);
                            final int n4 = n3 + 1;
                            array[n4] = ThemeUtils.EMPTY_STATE_SET;
                            array2[n4] = ThemeUtils.getThemeAttrColor(context, R$attr.colorSwitchThumbNormal);
                        }
                        return new ColorStateList(array, array2);
                    }
                    
                    private void setPorterDuffColorFilter(final Drawable drawable, final int n, PorterDuff$Mode access$000) {
                        Drawable mutate = drawable;
                        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                            mutate = drawable.mutate();
                        }
                        if (access$000 == null) {
                            access$000 = AppCompatDrawableManager.DEFAULT_MODE;
                        }
                        mutate.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(n, access$000));
                    }
                    
                    @Override
                    public Drawable createDrawableFor(final ResourceManagerInternal resourceManagerInternal, final Context context, final int n) {
                        if (n == R$drawable.abc_cab_background_top_material) {
                            return (Drawable)new LayerDrawable(new Drawable[] { resourceManagerInternal.getDrawable(context, R$drawable.abc_cab_background_internal_bg), resourceManagerInternal.getDrawable(context, R$drawable.abc_cab_background_top_mtrl_alpha) });
                        }
                        return null;
                    }
                    
                    @Override
                    public ColorStateList getTintListForDrawableRes(final Context context, final int n) {
                        if (n == R$drawable.abc_edit_text_material) {
                            return AppCompatResources.getColorStateList(context, R$color.abc_tint_edittext);
                        }
                        if (n == R$drawable.abc_switch_track_mtrl_alpha) {
                            return AppCompatResources.getColorStateList(context, R$color.abc_tint_switch_track);
                        }
                        if (n == R$drawable.abc_switch_thumb_material) {
                            return this.createSwitchThumbColorStateList(context);
                        }
                        if (n == R$drawable.abc_btn_default_mtrl_shape) {
                            return this.createDefaultButtonColorStateList(context);
                        }
                        if (n == R$drawable.abc_btn_borderless_material) {
                            return this.createBorderlessButtonColorStateList(context);
                        }
                        if (n == R$drawable.abc_btn_colored_material) {
                            return this.createColoredButtonColorStateList(context);
                        }
                        if (n == R$drawable.abc_spinner_mtrl_am_alpha || n == R$drawable.abc_spinner_textfield_background_material) {
                            return AppCompatResources.getColorStateList(context, R$color.abc_tint_spinner);
                        }
                        if (this.arrayContains(this.TINT_COLOR_CONTROL_NORMAL, n)) {
                            return ThemeUtils.getThemeAttrColorStateList(context, R$attr.colorControlNormal);
                        }
                        if (this.arrayContains(this.TINT_COLOR_CONTROL_STATE_LIST, n)) {
                            return AppCompatResources.getColorStateList(context, R$color.abc_tint_default);
                        }
                        if (this.arrayContains(this.TINT_CHECKABLE_BUTTON_LIST, n)) {
                            return AppCompatResources.getColorStateList(context, R$color.abc_tint_btn_checkable);
                        }
                        if (n == R$drawable.abc_seekbar_thumb_material) {
                            return AppCompatResources.getColorStateList(context, R$color.abc_tint_seek_thumb);
                        }
                        return null;
                    }
                    
                    @Override
                    public PorterDuff$Mode getTintModeForDrawableRes(final int n) {
                        PorterDuff$Mode multiply = null;
                        if (n == R$drawable.abc_switch_thumb_material) {
                            multiply = PorterDuff$Mode.MULTIPLY;
                        }
                        return multiply;
                    }
                    
                    @Override
                    public boolean tintDrawable(final Context context, final int n, final Drawable drawable) {
                        if (n == R$drawable.abc_seekbar_track_material) {
                            final LayerDrawable layerDrawable = (LayerDrawable)drawable;
                            this.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R$attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                            this.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R$attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                            this.setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R$attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                            return true;
                        }
                        if (n != R$drawable.abc_ratingbar_material && n != R$drawable.abc_ratingbar_indicator_material && n != R$drawable.abc_ratingbar_small_material) {
                            return false;
                        }
                        final LayerDrawable layerDrawable2 = (LayerDrawable)drawable;
                        this.setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, R$attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
                        this.setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R$attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                        this.setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R$attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
                        return true;
                    }
                    
                    @Override
                    public boolean tintDrawableUsingColorFilter(final Context context, final int n, final Drawable drawable) {
                        final PorterDuff$Mode access$000 = AppCompatDrawableManager.DEFAULT_MODE;
                        boolean b = false;
                        int n2 = 0;
                        final int n3 = -1;
                        PorterDuff$Mode multiply;
                        int round;
                        if (this.arrayContains(this.COLORFILTER_TINT_COLOR_CONTROL_NORMAL, n)) {
                            n2 = R$attr.colorControlNormal;
                            b = true;
                            multiply = access$000;
                            round = n3;
                        }
                        else if (this.arrayContains(this.COLORFILTER_COLOR_CONTROL_ACTIVATED, n)) {
                            n2 = R$attr.colorControlActivated;
                            b = true;
                            multiply = access$000;
                            round = n3;
                        }
                        else if (this.arrayContains(this.COLORFILTER_COLOR_BACKGROUND_MULTIPLY, n)) {
                            n2 = 16842801;
                            b = true;
                            multiply = PorterDuff$Mode.MULTIPLY;
                            round = n3;
                        }
                        else if (n == R$drawable.abc_list_divider_mtrl_alpha) {
                            n2 = 16842800;
                            b = true;
                            round = Math.round(40.8f);
                            multiply = access$000;
                        }
                        else {
                            multiply = access$000;
                            round = n3;
                            if (n == R$drawable.abc_dialog_material_background) {
                                n2 = 16842801;
                                b = true;
                                round = n3;
                                multiply = access$000;
                            }
                        }
                        if (b) {
                            Drawable mutate = drawable;
                            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                                mutate = drawable.mutate();
                            }
                            mutate.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, n2), multiply));
                            if (round != -1) {
                                mutate.setAlpha(round);
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }
    
    static void tintDrawable(final Drawable drawable, final TintInfo tintInfo, final int[] array) {
        ResourceManagerInternal.tintDrawable(drawable, tintInfo, array);
    }
    
    public Drawable getDrawable(final Context context, final int n) {
        synchronized (this) {
            return this.mResourceManager.getDrawable(context, n);
        }
    }
    
    Drawable getDrawable(final Context context, final int n, final boolean b) {
        synchronized (this) {
            return this.mResourceManager.getDrawable(context, n, b);
        }
    }
    
    ColorStateList getTintList(final Context context, final int n) {
        synchronized (this) {
            return this.mResourceManager.getTintList(context, n);
        }
    }
    
    public void onConfigurationChanged(final Context context) {
        synchronized (this) {
            this.mResourceManager.onConfigurationChanged(context);
        }
    }
    
    Drawable onDrawableLoadedFromResources(final Context context, final VectorEnabledTintResources vectorEnabledTintResources, final int n) {
        synchronized (this) {
            return this.mResourceManager.onDrawableLoadedFromResources(context, vectorEnabledTintResources, n);
        }
    }
    
    boolean tintDrawableUsingColorFilter(final Context context, final int n, final Drawable drawable) {
        return this.mResourceManager.tintDrawableUsingColorFilter(context, n, drawable);
    }
}
