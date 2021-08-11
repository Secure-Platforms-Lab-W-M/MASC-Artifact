/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.graphics.drawable.LayerDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.TypedValue
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
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
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.ThemeUtils;
import android.support.v7.widget.TintInfo;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public final class AppCompatDrawableManager {
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
    private static final ColorFilterLruCache COLOR_FILTER_CACHE;
    private static final boolean DEBUG = false;
    private static final PorterDuff.Mode DEFAULT_MODE;
    private static AppCompatDrawableManager INSTANCE;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "AppCompatDrawableManager";
    private static final int[] TINT_CHECKABLE_BUTTON_LIST;
    private static final int[] TINT_COLOR_CONTROL_NORMAL;
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
    private ArrayMap<String, InflateDelegate> mDelegates;
    private final Object mDrawableCacheLock = new Object();
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDrawableCaches = new WeakHashMap(0);
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArrayCompat<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;

    static {
        DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
        COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
        COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[]{R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha};
        TINT_COLOR_CONTROL_NORMAL = new int[]{R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha};
        COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[]{R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_dark, R.drawable.abc_text_select_handle_middle_mtrl_dark, R.drawable.abc_text_select_handle_right_mtrl_dark, R.drawable.abc_text_select_handle_left_mtrl_light, R.drawable.abc_text_select_handle_middle_mtrl_light, R.drawable.abc_text_select_handle_right_mtrl_light};
        COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[]{R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult};
        TINT_COLOR_CONTROL_STATE_LIST = new int[]{R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material};
        TINT_CHECKABLE_BUTTON_LIST = new int[]{R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material};
    }

    private void addDelegate(@NonNull String string2, @NonNull InflateDelegate inflateDelegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap();
        }
        this.mDelegates.put(string2, inflateDelegate);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean addDrawableToCache(@NonNull Context longSparseArray, long l, @NonNull Drawable longSparseArray2) {
        Drawable.ConstantState constantState = longSparseArray2.getConstantState();
        if (constantState == null) {
            return false;
        }
        Object object = this.mDrawableCacheLock;
        synchronized (object) {
            longSparseArray2 = this.mDrawableCaches.get(longSparseArray);
            if (longSparseArray2 == null) {
                longSparseArray2 = new LongSparseArray<WeakReference<Drawable.ConstantState>>();
                this.mDrawableCaches.put((Context)longSparseArray, longSparseArray2);
                longSparseArray = longSparseArray2;
            } else {
                longSparseArray = longSparseArray2;
            }
            longSparseArray.put(l, new WeakReference<Drawable.ConstantState>(constantState));
            return true;
        }
    }

    private void addTintListToCache(@NonNull Context sparseArrayCompat, @DrawableRes int n, @NonNull ColorStateList colorStateList) {
        SparseArrayCompat sparseArrayCompat2;
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap();
        }
        if ((sparseArrayCompat2 = this.mTintLists.get(sparseArrayCompat)) == null) {
            sparseArrayCompat2 = new SparseArrayCompat();
            this.mTintLists.put((Context)sparseArrayCompat, sparseArrayCompat2);
            sparseArrayCompat = sparseArrayCompat2;
        } else {
            sparseArrayCompat = sparseArrayCompat2;
        }
        sparseArrayCompat.append(n, colorStateList);
    }

    private static boolean arrayContains(int[] arrn, int n) {
        int n2 = arrn.length;
        for (int i = 0; i < n2; ++i) {
            if (arrn[i] != n) continue;
            return true;
        }
        return false;
    }

    private void checkVectorDrawableSetup(@NonNull Context context) {
        if (this.mHasCheckedVectorDrawableSetup) {
            return;
        }
        this.mHasCheckedVectorDrawableSetup = true;
        if ((context = this.getDrawable(context, R.drawable.abc_vector_test)) != null && AppCompatDrawableManager.isVectorDrawable((Drawable)context)) {
            return;
        }
        this.mHasCheckedVectorDrawableSetup = false;
        throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
    }

    private ColorStateList createBorderlessButtonColorStateList(@NonNull Context context) {
        return this.createButtonColorStateList(context, 0);
    }

    private ColorStateList createButtonColorStateList(@NonNull Context context, @ColorInt int n) {
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

    private static long createCacheKey(TypedValue typedValue) {
        return (long)typedValue.assetCookie << 32 | (long)typedValue.data;
    }

    private ColorStateList createColoredButtonColorStateList(@NonNull Context context) {
        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorAccent));
    }

    private ColorStateList createDefaultButtonColorStateList(@NonNull Context context) {
        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorButtonNormal));
    }

    private Drawable createDrawableIfNeeded(@NonNull Context context, @DrawableRes int n) {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue typedValue = this.mTypedValue;
        context.getResources().getValue(n, typedValue, true);
        long l = AppCompatDrawableManager.createCacheKey(typedValue);
        Drawable drawable2 = this.getCachedDrawable(context, l);
        if (drawable2 != null) {
            return drawable2;
        }
        if (n == R.drawable.abc_cab_background_top_material) {
            drawable2 = new LayerDrawable(new Drawable[]{this.getDrawable(context, R.drawable.abc_cab_background_internal_bg), this.getDrawable(context, R.drawable.abc_cab_background_top_mtrl_alpha)});
        }
        if (drawable2 != null) {
            drawable2.setChangingConfigurations(typedValue.changingConfigurations);
            this.addDrawableToCache(context, l, drawable2);
            return drawable2;
        }
        return drawable2;
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

    private static PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, PorterDuff.Mode mode, int[] arrn) {
        if (colorStateList != null && mode != null) {
            return AppCompatDrawableManager.getPorterDuffColorFilter(colorStateList.getColorForState(arrn, 0), mode);
        }
        return null;
    }

    public static AppCompatDrawableManager get() {
        if (INSTANCE == null) {
            INSTANCE = new AppCompatDrawableManager();
            AppCompatDrawableManager.installDefaultInflateDelegates(INSTANCE);
        }
        return INSTANCE;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Drawable getCachedDrawable(@NonNull Context context, long l) {
        Object object = this.mDrawableCacheLock;
        synchronized (object) {
            LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get((Object)context);
            if (longSparseArray == null) {
                return null;
            }
            Drawable.ConstantState constantState = longSparseArray.get(l);
            if (constantState == null) return null;
            if ((constantState = constantState.get()) != null) {
                return constantState.newDrawable(context.getResources());
            }
            longSparseArray.delete(l);
            return null;
        }
    }

    public static PorterDuffColorFilter getPorterDuffColorFilter(int n, PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter = COLOR_FILTER_CACHE.get(n, mode);
        if (porterDuffColorFilter == null) {
            porterDuffColorFilter = new PorterDuffColorFilter(n, mode);
            COLOR_FILTER_CACHE.put(n, mode, porterDuffColorFilter);
            return porterDuffColorFilter;
        }
        return porterDuffColorFilter;
    }

    private ColorStateList getTintListFromCache(@NonNull Context context, @DrawableRes int n) {
        Object object = this.mTintLists;
        Object var3_4 = null;
        if (object != null) {
            object = object.get((Object)context);
            context = var3_4;
            if (object != null) {
                context = (ColorStateList)object.get(n);
            }
            return context;
        }
        return null;
    }

    static PorterDuff.Mode getTintMode(int n) {
        if (n == R.drawable.abc_switch_thumb_material) {
            return PorterDuff.Mode.MULTIPLY;
        }
        return null;
    }

    private static void installDefaultInflateDelegates(@NonNull AppCompatDrawableManager appCompatDrawableManager) {
        if (Build.VERSION.SDK_INT < 24) {
            appCompatDrawableManager.addDelegate("vector", new VdcInflateDelegate());
            if (Build.VERSION.SDK_INT >= 11) {
                appCompatDrawableManager.addDelegate("animated-vector", new AvdcInflateDelegate());
                return;
            }
            return;
        }
    }

    private static boolean isVectorDrawable(@NonNull Drawable drawable2) {
        if (!(drawable2 instanceof VectorDrawableCompat) && !"android.graphics.drawable.VectorDrawable".equals(drawable2.getClass().getName())) {
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Drawable loadDrawableFromDelegates(@NonNull Context context, @DrawableRes int n) {
        Object object = this.mDelegates;
        if (object != null && !object.isEmpty()) {
            object = this.mKnownDrawableIdTags;
            if (object != null) {
                if ("appcompat_skip_skip".equals(object = (String)object.get(n))) {
                    return null;
                }
                if (object != null && this.mDelegates.get(object) == null) {
                    return null;
                }
            } else {
                this.mKnownDrawableIdTags = new SparseArrayCompat();
            }
            if (this.mTypedValue == null) {
                this.mTypedValue = new TypedValue();
            }
            TypedValue typedValue = this.mTypedValue;
            Resources resources = context.getResources();
            resources.getValue(n, typedValue, true);
            long l = AppCompatDrawableManager.createCacheKey(typedValue);
            object = this.getCachedDrawable(context, l);
            if (object != null) {
                return object;
            }
            if (typedValue.string != null && typedValue.string.toString().endsWith(".xml")) {
                ArrayMap<String, InflateDelegate> arrayMap = object;
                try {
                    int n2;
                    resources = resources.getXml(n);
                    arrayMap = object;
                    AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)resources);
                    do {
                        arrayMap = object;
                    } while ((n2 = resources.next()) != 2 && n2 != 1);
                    if (n2 != 2) {
                        arrayMap = object;
                        throw new XmlPullParserException("No start tag found");
                    }
                    arrayMap = object;
                    Object object2 = resources.getName();
                    arrayMap = object;
                    this.mKnownDrawableIdTags.append(n, (String)object2);
                    arrayMap = object;
                    object2 = this.mDelegates.get(object2);
                    if (object2 != null) {
                        arrayMap = object;
                        object = object2.createFromXmlInner(context, (XmlPullParser)resources, attributeSet, context.getTheme());
                    }
                    if (object != null) {
                        arrayMap = object;
                        object.setChangingConfigurations(typedValue.changingConfigurations);
                        arrayMap = object;
                        this.addDrawableToCache(context, l, (Drawable)object);
                    }
                }
                catch (Exception exception) {
                    Log.e((String)"AppCompatDrawableManager", (String)"Exception while inflating drawable", (Throwable)exception);
                    object = arrayMap;
                }
            }
            if (object == null) {
                this.mKnownDrawableIdTags.append(n, "appcompat_skip_skip");
                return object;
            }
            return object;
        }
        return null;
    }

    private void removeDelegate(@NonNull String string2, @NonNull InflateDelegate inflateDelegate) {
        ArrayMap<String, InflateDelegate> arrayMap = this.mDelegates;
        if (arrayMap != null && arrayMap.get(string2) == inflateDelegate) {
            this.mDelegates.remove(string2);
            return;
        }
    }

    private static void setPorterDuffColorFilter(Drawable drawable2, int n, PorterDuff.Mode mode) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable2)) {
            drawable2 = drawable2.mutate();
        }
        if (mode == null) {
            mode = DEFAULT_MODE;
        }
        drawable2.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(n, mode));
    }

    private Drawable tintDrawable(@NonNull Context context, @DrawableRes int n, boolean bl, @NonNull Drawable drawable2) {
        ColorStateList colorStateList = this.getTintList(context, n);
        if (colorStateList != null) {
            if (DrawableUtils.canSafelyMutateDrawable(drawable2)) {
                drawable2 = drawable2.mutate();
            }
            context = DrawableCompat.wrap(drawable2);
            DrawableCompat.setTintList((Drawable)context, colorStateList);
            drawable2 = AppCompatDrawableManager.getTintMode(n);
            if (drawable2 != null) {
                DrawableCompat.setTintMode((Drawable)context, (PorterDuff.Mode)drawable2);
            }
            return context;
        }
        if (n == R.drawable.abc_seekbar_track_material) {
            colorStateList = (LayerDrawable)drawable2;
            AppCompatDrawableManager.setPorterDuffColorFilter(colorStateList.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
            AppCompatDrawableManager.setPorterDuffColorFilter(colorStateList.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
            AppCompatDrawableManager.setPorterDuffColorFilter(colorStateList.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
            return drawable2;
        }
        if (n != R.drawable.abc_ratingbar_material && n != R.drawable.abc_ratingbar_indicator_material && n != R.drawable.abc_ratingbar_small_material) {
            if (!AppCompatDrawableManager.tintDrawableUsingColorFilter(context, n, drawable2) && bl) {
                return null;
            }
            return drawable2;
        }
        colorStateList = (LayerDrawable)drawable2;
        AppCompatDrawableManager.setPorterDuffColorFilter(colorStateList.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
        AppCompatDrawableManager.setPorterDuffColorFilter(colorStateList.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
        AppCompatDrawableManager.setPorterDuffColorFilter(colorStateList.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
        return drawable2;
    }

    static void tintDrawable(Drawable drawable2, TintInfo tintInfo, int[] arrn) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable2) && drawable2.mutate() != drawable2) {
            Log.d((String)"AppCompatDrawableManager", (String)"Mutated drawable is not the same instance as the input.");
            return;
        }
        if (!tintInfo.mHasTintList && !tintInfo.mHasTintMode) {
            drawable2.clearColorFilter();
        } else {
            ColorStateList colorStateList = tintInfo.mHasTintList ? tintInfo.mTintList : null;
            tintInfo = tintInfo.mHasTintMode ? tintInfo.mTintMode : DEFAULT_MODE;
            drawable2.setColorFilter((ColorFilter)AppCompatDrawableManager.createTintFilter(colorStateList, (PorterDuff.Mode)tintInfo, arrn));
        }
        if (Build.VERSION.SDK_INT <= 23) {
            drawable2.invalidateSelf();
            return;
        }
    }

    static boolean tintDrawableUsingColorFilter(@NonNull Context context, @DrawableRes int n, @NonNull Drawable drawable2) {
        PorterDuff.Mode mode = DEFAULT_MODE;
        int n2 = 0;
        int n3 = 0;
        int n4 = -1;
        if (AppCompatDrawableManager.arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, n)) {
            n3 = R.attr.colorControlNormal;
            n = 1;
        } else if (AppCompatDrawableManager.arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, n)) {
            n3 = R.attr.colorControlActivated;
            n = 1;
        } else if (AppCompatDrawableManager.arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, n)) {
            n3 = 16842801;
            n = 1;
            mode = PorterDuff.Mode.MULTIPLY;
        } else if (n == R.drawable.abc_list_divider_mtrl_alpha) {
            n3 = 16842800;
            n = 1;
            n4 = Math.round(40.8f);
        } else if (n == R.drawable.abc_dialog_material_background) {
            n3 = 16842801;
            n = 1;
        } else {
            n = n2;
        }
        if (n != 0) {
            if (DrawableUtils.canSafelyMutateDrawable(drawable2)) {
                drawable2 = drawable2.mutate();
            }
            drawable2.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, n3), mode));
            if (n4 != -1) {
                drawable2.setAlpha(n4);
            }
            return true;
        }
        return false;
    }

    public Drawable getDrawable(@NonNull Context context, @DrawableRes int n) {
        return this.getDrawable(context, n, false);
    }

    Drawable getDrawable(@NonNull Context context, @DrawableRes int n, boolean bl) {
        this.checkVectorDrawableSetup(context);
        Drawable drawable2 = this.loadDrawableFromDelegates(context, n);
        if (drawable2 == null) {
            drawable2 = this.createDrawableIfNeeded(context, n);
        }
        if (drawable2 == null) {
            drawable2 = ContextCompat.getDrawable(context, n);
        }
        if (drawable2 != null) {
            drawable2 = this.tintDrawable(context, n, bl, drawable2);
        }
        if (drawable2 != null) {
            DrawableUtils.fixDrawable(drawable2);
            return drawable2;
        }
        return drawable2;
    }

    ColorStateList getTintList(@NonNull Context context, @DrawableRes int n) {
        ColorStateList colorStateList = this.getTintListFromCache(context, n);
        if (colorStateList == null) {
            if (n == R.drawable.abc_edit_text_material) {
                colorStateList = AppCompatResources.getColorStateList(context, R.color.abc_tint_edittext);
            } else if (n == R.drawable.abc_switch_track_mtrl_alpha) {
                colorStateList = AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_track);
            } else if (n == R.drawable.abc_switch_thumb_material) {
                colorStateList = this.createSwitchThumbColorStateList(context);
            } else if (n == R.drawable.abc_btn_default_mtrl_shape) {
                colorStateList = this.createDefaultButtonColorStateList(context);
            } else if (n == R.drawable.abc_btn_borderless_material) {
                colorStateList = this.createBorderlessButtonColorStateList(context);
            } else if (n == R.drawable.abc_btn_colored_material) {
                colorStateList = this.createColoredButtonColorStateList(context);
            } else if (n != R.drawable.abc_spinner_mtrl_am_alpha && n != R.drawable.abc_spinner_textfield_background_material) {
                if (AppCompatDrawableManager.arrayContains(TINT_COLOR_CONTROL_NORMAL, n)) {
                    colorStateList = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
                } else if (AppCompatDrawableManager.arrayContains(TINT_COLOR_CONTROL_STATE_LIST, n)) {
                    colorStateList = AppCompatResources.getColorStateList(context, R.color.abc_tint_default);
                } else if (AppCompatDrawableManager.arrayContains(TINT_CHECKABLE_BUTTON_LIST, n)) {
                    colorStateList = AppCompatResources.getColorStateList(context, R.color.abc_tint_btn_checkable);
                } else if (n == R.drawable.abc_seekbar_thumb_material) {
                    colorStateList = AppCompatResources.getColorStateList(context, R.color.abc_tint_seek_thumb);
                }
            } else {
                colorStateList = AppCompatResources.getColorStateList(context, R.color.abc_tint_spinner);
            }
            if (colorStateList != null) {
                this.addTintListToCache(context, n, colorStateList);
                return colorStateList;
            }
            return colorStateList;
        }
        return colorStateList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void onConfigurationChanged(@NonNull Context object) {
        Object object2 = this.mDrawableCacheLock;
        synchronized (object2) {
            object = this.mDrawableCaches.get(object);
            if (object != null) {
                object.clear();
            }
            return;
        }
    }

    Drawable onDrawableLoadedFromResources(@NonNull Context context, @NonNull VectorEnabledTintResources vectorEnabledTintResources, @DrawableRes int n) {
        Drawable drawable2 = this.loadDrawableFromDelegates(context, n);
        vectorEnabledTintResources = drawable2 == null ? vectorEnabledTintResources.superGetDrawable(n) : drawable2;
        if (vectorEnabledTintResources != null) {
            return this.tintDrawable(context, n, false, (Drawable)vectorEnabledTintResources);
        }
        return null;
    }

    @RequiresApi(value=11)
    private static class AvdcInflateDelegate
    implements InflateDelegate {
        AvdcInflateDelegate() {
        }

        @Override
        public Drawable createFromXmlInner(@NonNull Context object, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) {
            try {
                object = AnimatedVectorDrawableCompat.createFromXmlInner((Context)object, object.getResources(), xmlPullParser, attributeSet, theme);
                return object;
            }
            catch (Exception exception) {
                Log.e((String)"AvdcInflateDelegate", (String)"Exception while inflating <animated-vector>", (Throwable)exception);
                return null;
            }
        }
    }

    private static class ColorFilterLruCache
    extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int n) {
            super(n);
        }

        private static int generateCacheKey(int n, PorterDuff.Mode mode) {
            return (1 * 31 + n) * 31 + mode.hashCode();
        }

        PorterDuffColorFilter get(int n, PorterDuff.Mode mode) {
            return (PorterDuffColorFilter)this.get(ColorFilterLruCache.generateCacheKey(n, mode));
        }

        PorterDuffColorFilter put(int n, PorterDuff.Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return this.put(ColorFilterLruCache.generateCacheKey(n, mode), porterDuffColorFilter);
        }
    }

    private static interface InflateDelegate {
        public Drawable createFromXmlInner(@NonNull Context var1, @NonNull XmlPullParser var2, @NonNull AttributeSet var3, @Nullable Resources.Theme var4);
    }

    private static class VdcInflateDelegate
    implements InflateDelegate {
        VdcInflateDelegate() {
        }

        @Override
        public Drawable createFromXmlInner(@NonNull Context object, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme) {
            try {
                object = VectorDrawableCompat.createFromXmlInner(object.getResources(), xmlPullParser, attributeSet, theme);
                return object;
            }
            catch (Exception exception) {
                Log.e((String)"VdcInflateDelegate", (String)"Exception while inflating <vector>", (Throwable)exception);
                return null;
            }
        }
    }

}

