// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.support.v4.util.LruCache;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import android.support.annotation.RequiresApi;
import android.support.v7.content.res.AppCompatResources;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.ColorFilter;
import android.util.AttributeSet;
import android.content.res.XmlResourceParser;
import android.content.res.Resources;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.os.Build$VERSION;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.ColorUtils;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.content.res.ColorStateList;
import android.support.v4.util.SparseArrayCompat;
import android.graphics.drawable.Drawable$ConstantState;
import java.lang.ref.WeakReference;
import android.support.v4.util.LongSparseArray;
import android.content.Context;
import java.util.WeakHashMap;
import android.support.v4.util.ArrayMap;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class AppCompatDrawableManager
{
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
    private static final ColorFilterLruCache COLOR_FILTER_CACHE;
    private static final boolean DEBUG = false;
    private static final PorterDuff$Mode DEFAULT_MODE;
    private static AppCompatDrawableManager INSTANCE;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "AppCompatDrawableManager";
    private static final int[] TINT_CHECKABLE_BUTTON_LIST;
    private static final int[] TINT_COLOR_CONTROL_NORMAL;
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
    private ArrayMap<String, InflateDelegate> mDelegates;
    private final Object mDrawableCacheLock;
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable$ConstantState>>> mDrawableCaches;
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArrayCompat<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;
    
    static {
        DEFAULT_MODE = PorterDuff$Mode.SRC_IN;
        COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
        COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha };
        TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha };
        COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[] { R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_dark, R.drawable.abc_text_select_handle_middle_mtrl_dark, R.drawable.abc_text_select_handle_right_mtrl_dark, R.drawable.abc_text_select_handle_left_mtrl_light, R.drawable.abc_text_select_handle_middle_mtrl_light, R.drawable.abc_text_select_handle_right_mtrl_light };
        COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[] { R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult };
        TINT_COLOR_CONTROL_STATE_LIST = new int[] { R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material };
        TINT_CHECKABLE_BUTTON_LIST = new int[] { R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material };
    }
    
    public AppCompatDrawableManager() {
        this.mDrawableCacheLock = new Object();
        this.mDrawableCaches = new WeakHashMap<Context, LongSparseArray<WeakReference<Drawable$ConstantState>>>(0);
    }
    
    private void addDelegate(@NonNull final String s, @NonNull final InflateDelegate inflateDelegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap<String, InflateDelegate>();
        }
        this.mDelegates.put(s, inflateDelegate);
    }
    
    private boolean addDrawableToCache(@NonNull final Context context, final long n, @NonNull Drawable drawable) {
        final Drawable$ConstantState constantState = drawable.getConstantState();
        if (constantState != null) {
            while (true) {
                while (true) {
                    Label_0092: {
                        synchronized (this.mDrawableCacheLock) {
                            drawable = (Drawable)this.mDrawableCaches.get(context);
                            if (drawable == null) {
                                drawable = (Drawable)new LongSparseArray();
                                this.mDrawableCaches.put(context, (LongSparseArray<WeakReference<Drawable$ConstantState>>)drawable);
                                final Drawable drawable2 = drawable;
                                ((LongSparseArray<WeakReference<Drawable$ConstantState>>)drawable2).put(n, new WeakReference<Drawable$ConstantState>(constantState));
                                return true;
                            }
                            break Label_0092;
                        }
                        break;
                    }
                    final Drawable drawable2 = drawable;
                    continue;
                }
            }
        }
        return false;
    }
    
    private void addTintListToCache(@NonNull final Context context, @DrawableRes final int n, @NonNull final ColorStateList list) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap<Context, SparseArrayCompat<ColorStateList>>();
        }
        final SparseArrayCompat<ColorStateList> sparseArrayCompat = this.mTintLists.get(context);
        SparseArrayCompat<ColorStateList> sparseArrayCompat3;
        if (sparseArrayCompat == null) {
            final SparseArrayCompat<ColorStateList> sparseArrayCompat2 = new SparseArrayCompat<ColorStateList>();
            this.mTintLists.put(context, sparseArrayCompat2);
            sparseArrayCompat3 = sparseArrayCompat2;
        }
        else {
            sparseArrayCompat3 = sparseArrayCompat;
        }
        sparseArrayCompat3.append(n, list);
    }
    
    private static boolean arrayContains(final int[] array, final int n) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    private void checkVectorDrawableSetup(@NonNull final Context context) {
        if (this.mHasCheckedVectorDrawableSetup) {
            return;
        }
        this.mHasCheckedVectorDrawableSetup = true;
        final Drawable drawable = this.getDrawable(context, R.drawable.abc_vector_test);
        if (drawable != null && isVectorDrawable(drawable)) {
            return;
        }
        this.mHasCheckedVectorDrawableSetup = false;
        throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
    }
    
    private ColorStateList createBorderlessButtonColorStateList(@NonNull final Context context) {
        return this.createButtonColorStateList(context, 0);
    }
    
    private ColorStateList createButtonColorStateList(@NonNull final Context context, @ColorInt final int n) {
        final int[][] array = new int[4][];
        final int[] array2 = new int[4];
        final int themeAttrColor = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlHighlight);
        final int disabledThemeAttrColor = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);
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
    
    private static long createCacheKey(final TypedValue typedValue) {
        return (long)typedValue.assetCookie << 32 | (long)typedValue.data;
    }
    
    private ColorStateList createColoredButtonColorStateList(@NonNull final Context context) {
        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorAccent));
    }
    
    private ColorStateList createDefaultButtonColorStateList(@NonNull final Context context) {
        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorButtonNormal));
    }
    
    private Drawable createDrawableIfNeeded(@NonNull final Context context, @DrawableRes final int n) {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        final TypedValue mTypedValue = this.mTypedValue;
        context.getResources().getValue(n, mTypedValue, true);
        final long cacheKey = createCacheKey(mTypedValue);
        Object cachedDrawable = this.getCachedDrawable(context, cacheKey);
        if (cachedDrawable != null) {
            return (Drawable)cachedDrawable;
        }
        if (n == R.drawable.abc_cab_background_top_material) {
            cachedDrawable = new LayerDrawable(new Drawable[] { this.getDrawable(context, R.drawable.abc_cab_background_internal_bg), this.getDrawable(context, R.drawable.abc_cab_background_top_mtrl_alpha) });
        }
        if (cachedDrawable != null) {
            ((Drawable)cachedDrawable).setChangingConfigurations(mTypedValue.changingConfigurations);
            this.addDrawableToCache(context, cacheKey, (Drawable)cachedDrawable);
            return (Drawable)cachedDrawable;
        }
        return (Drawable)cachedDrawable;
    }
    
    private ColorStateList createSwitchThumbColorStateList(final Context context) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        final ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorSwitchThumbNormal);
        if (themeAttrColorStateList != null && themeAttrColorStateList.isStateful()) {
            array[0] = ThemeUtils.DISABLED_STATE_SET;
            array2[0] = themeAttrColorStateList.getColorForState(array[0], 0);
            final int n = 0 + 1;
            array[n] = ThemeUtils.CHECKED_STATE_SET;
            array2[n] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
            final int n2 = n + 1;
            array[n2] = ThemeUtils.EMPTY_STATE_SET;
            array2[n2] = themeAttrColorStateList.getDefaultColor();
        }
        else {
            array[0] = ThemeUtils.DISABLED_STATE_SET;
            array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
            final int n3 = 0 + 1;
            array[n3] = ThemeUtils.CHECKED_STATE_SET;
            array2[n3] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
            final int n4 = n3 + 1;
            array[n4] = ThemeUtils.EMPTY_STATE_SET;
            array2[n4] = ThemeUtils.getThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
        }
        return new ColorStateList(array, array2);
    }
    
    private static PorterDuffColorFilter createTintFilter(final ColorStateList list, final PorterDuff$Mode porterDuff$Mode, final int[] array) {
        if (list != null && porterDuff$Mode != null) {
            return getPorterDuffColorFilter(list.getColorForState(array, 0), porterDuff$Mode);
        }
        return null;
    }
    
    public static AppCompatDrawableManager get() {
        if (AppCompatDrawableManager.INSTANCE == null) {
            installDefaultInflateDelegates(AppCompatDrawableManager.INSTANCE = new AppCompatDrawableManager());
        }
        return AppCompatDrawableManager.INSTANCE;
    }
    
    private Drawable getCachedDrawable(@NonNull final Context context, final long n) {
        while (true) {
            while (true) {
                Label_0098: {
                    synchronized (this.mDrawableCacheLock) {
                        final LongSparseArray<WeakReference<Drawable$ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
                        if (longSparseArray == null) {
                            return null;
                        }
                        final WeakReference<Drawable$ConstantState> weakReference = longSparseArray.get(n);
                        if (weakReference == null) {
                            break Label_0098;
                        }
                        final Drawable$ConstantState drawable$ConstantState = weakReference.get();
                        if (drawable$ConstantState != null) {
                            return drawable$ConstantState.newDrawable(context.getResources());
                        }
                        longSparseArray.delete(n);
                        return null;
                    }
                }
                continue;
            }
        }
    }
    
    public static PorterDuffColorFilter getPorterDuffColorFilter(final int n, final PorterDuff$Mode porterDuff$Mode) {
        final PorterDuffColorFilter value = AppCompatDrawableManager.COLOR_FILTER_CACHE.get(n, porterDuff$Mode);
        if (value == null) {
            final PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(n, porterDuff$Mode);
            AppCompatDrawableManager.COLOR_FILTER_CACHE.put(n, porterDuff$Mode, porterDuffColorFilter);
            return porterDuffColorFilter;
        }
        return value;
    }
    
    private ColorStateList getTintListFromCache(@NonNull final Context context, @DrawableRes final int n) {
        final WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists = this.mTintLists;
        final ColorStateList list = null;
        if (mTintLists != null) {
            final SparseArrayCompat<ColorStateList> sparseArrayCompat = mTintLists.get(context);
            ColorStateList list2 = list;
            if (sparseArrayCompat != null) {
                list2 = sparseArrayCompat.get(n);
            }
            return list2;
        }
        return null;
    }
    
    static PorterDuff$Mode getTintMode(final int n) {
        if (n == R.drawable.abc_switch_thumb_material) {
            return PorterDuff$Mode.MULTIPLY;
        }
        return null;
    }
    
    private static void installDefaultInflateDelegates(@NonNull final AppCompatDrawableManager appCompatDrawableManager) {
        if (Build$VERSION.SDK_INT >= 24) {
            return;
        }
        appCompatDrawableManager.addDelegate("vector", (InflateDelegate)new VdcInflateDelegate());
        if (Build$VERSION.SDK_INT >= 11) {
            appCompatDrawableManager.addDelegate("animated-vector", (InflateDelegate)new AvdcInflateDelegate());
        }
    }
    
    private static boolean isVectorDrawable(@NonNull final Drawable drawable) {
        return drawable instanceof VectorDrawableCompat || "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName());
    }
    
    private Drawable loadDrawableFromDelegates(@NonNull final Context context, @DrawableRes final int n) {
        final ArrayMap<String, InflateDelegate> mDelegates = this.mDelegates;
        if (mDelegates == null || mDelegates.isEmpty()) {
            return null;
        }
        final SparseArrayCompat<String> mKnownDrawableIdTags = this.mKnownDrawableIdTags;
        if (mKnownDrawableIdTags != null) {
            final String s = mKnownDrawableIdTags.get(n);
            if ("appcompat_skip_skip".equals(s)) {
                return null;
            }
            if (s != null && this.mDelegates.get(s) == null) {
                return null;
            }
        }
        else {
            this.mKnownDrawableIdTags = new SparseArrayCompat<String>();
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        final TypedValue mTypedValue = this.mTypedValue;
        final Resources resources = context.getResources();
        resources.getValue(n, mTypedValue, true);
        final long cacheKey = createCacheKey(mTypedValue);
        Drawable drawable = this.getCachedDrawable(context, cacheKey);
        if (drawable != null) {
            return drawable;
        }
        if (mTypedValue.string != null && mTypedValue.string.toString().endsWith(".xml")) {
            while (true) {
                Drawable drawable2 = drawable;
                while (true) {
                    Label_0402: {
                        try {
                            final XmlResourceParser xml = resources.getXml(n);
                            drawable2 = drawable;
                            final AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xml);
                            int next;
                            do {
                                drawable2 = drawable;
                                next = ((XmlPullParser)xml).next();
                            } while (next != 2 && next != 1);
                            if (next != 2) {
                                drawable2 = drawable;
                                throw new XmlPullParserException("No start tag found");
                            }
                            drawable2 = drawable;
                            final String name = ((XmlPullParser)xml).getName();
                            drawable2 = drawable;
                            this.mKnownDrawableIdTags.append(n, name);
                            drawable2 = drawable;
                            final InflateDelegate inflateDelegate = this.mDelegates.get(name);
                            if (inflateDelegate == null) {
                                break Label_0402;
                            }
                            drawable2 = drawable;
                            drawable = inflateDelegate.createFromXmlInner(context, (XmlPullParser)xml, attributeSet, context.getTheme());
                            if (drawable != null) {
                                drawable2 = drawable;
                                drawable.setChangingConfigurations(mTypedValue.changingConfigurations);
                                drawable2 = drawable;
                                this.addDrawableToCache(context, cacheKey, drawable);
                            }
                        }
                        catch (Exception ex) {
                            Log.e("AppCompatDrawableManager", "Exception while inflating drawable", (Throwable)ex);
                            drawable = drawable2;
                        }
                        break;
                    }
                    continue;
                }
            }
        }
        if (drawable == null) {
            this.mKnownDrawableIdTags.append(n, "appcompat_skip_skip");
            return drawable;
        }
        return drawable;
    }
    
    private void removeDelegate(@NonNull final String s, @NonNull final InflateDelegate inflateDelegate) {
        final ArrayMap<String, InflateDelegate> mDelegates = this.mDelegates;
        if (mDelegates != null && mDelegates.get(s) == inflateDelegate) {
            this.mDelegates.remove(s);
        }
    }
    
    private static void setPorterDuffColorFilter(Drawable mutate, final int n, PorterDuff$Mode default_MODE) {
        if (DrawableUtils.canSafelyMutateDrawable(mutate)) {
            mutate = mutate.mutate();
        }
        if (default_MODE == null) {
            default_MODE = AppCompatDrawableManager.DEFAULT_MODE;
        }
        mutate.setColorFilter((ColorFilter)getPorterDuffColorFilter(n, default_MODE));
    }
    
    private Drawable tintDrawable(@NonNull final Context context, @DrawableRes final int n, final boolean b, @NonNull Drawable mutate) {
        final ColorStateList tintList = this.getTintList(context, n);
        if (tintList != null) {
            if (DrawableUtils.canSafelyMutateDrawable(mutate)) {
                mutate = mutate.mutate();
            }
            final Drawable wrap = DrawableCompat.wrap(mutate);
            DrawableCompat.setTintList(wrap, tintList);
            final PorterDuff$Mode tintMode = getTintMode(n);
            if (tintMode != null) {
                DrawableCompat.setTintMode(wrap, tintMode);
            }
            return wrap;
        }
        if (n == R.drawable.abc_seekbar_track_material) {
            final LayerDrawable layerDrawable = (LayerDrawable)mutate;
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
            return mutate;
        }
        if (n == R.drawable.abc_ratingbar_material || n == R.drawable.abc_ratingbar_indicator_material || n == R.drawable.abc_ratingbar_small_material) {
            final LayerDrawable layerDrawable2 = (LayerDrawable)mutate;
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
            return mutate;
        }
        if (!tintDrawableUsingColorFilter(context, n, mutate) && b) {
            return null;
        }
        return mutate;
    }
    
    static void tintDrawable(final Drawable drawable, final TintInfo tintInfo, final int[] array) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable) && drawable.mutate() != drawable) {
            Log.d("AppCompatDrawableManager", "Mutated drawable is not the same instance as the input.");
            return;
        }
        if (!tintInfo.mHasTintList && !tintInfo.mHasTintMode) {
            drawable.clearColorFilter();
        }
        else {
            ColorStateList mTintList;
            if (tintInfo.mHasTintList) {
                mTintList = tintInfo.mTintList;
            }
            else {
                mTintList = null;
            }
            PorterDuff$Mode porterDuff$Mode;
            if (tintInfo.mHasTintMode) {
                porterDuff$Mode = tintInfo.mTintMode;
            }
            else {
                porterDuff$Mode = AppCompatDrawableManager.DEFAULT_MODE;
            }
            drawable.setColorFilter((ColorFilter)createTintFilter(mTintList, porterDuff$Mode, array));
        }
        if (Build$VERSION.SDK_INT <= 23) {
            drawable.invalidateSelf();
        }
    }
    
    static boolean tintDrawableUsingColorFilter(@NonNull final Context context, @DrawableRes int n, @NonNull Drawable mutate) {
        PorterDuff$Mode porterDuff$Mode = AppCompatDrawableManager.DEFAULT_MODE;
        final int n2 = 0;
        int n3 = 0;
        int round = -1;
        if (arrayContains(AppCompatDrawableManager.COLORFILTER_TINT_COLOR_CONTROL_NORMAL, n)) {
            n3 = R.attr.colorControlNormal;
            n = 1;
        }
        else if (arrayContains(AppCompatDrawableManager.COLORFILTER_COLOR_CONTROL_ACTIVATED, n)) {
            n3 = R.attr.colorControlActivated;
            n = 1;
        }
        else if (arrayContains(AppCompatDrawableManager.COLORFILTER_COLOR_BACKGROUND_MULTIPLY, n)) {
            n3 = 16842801;
            n = 1;
            porterDuff$Mode = PorterDuff$Mode.MULTIPLY;
        }
        else if (n == R.drawable.abc_list_divider_mtrl_alpha) {
            n3 = 16842800;
            n = 1;
            round = Math.round(40.8f);
        }
        else if (n == R.drawable.abc_dialog_material_background) {
            n3 = 16842801;
            n = 1;
        }
        else {
            n = n2;
        }
        if (n != 0) {
            if (DrawableUtils.canSafelyMutateDrawable(mutate)) {
                mutate = mutate.mutate();
            }
            mutate.setColorFilter((ColorFilter)getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, n3), porterDuff$Mode));
            if (round != -1) {
                mutate.setAlpha(round);
            }
            return true;
        }
        return false;
    }
    
    public Drawable getDrawable(@NonNull final Context context, @DrawableRes final int n) {
        return this.getDrawable(context, n, false);
    }
    
    Drawable getDrawable(@NonNull final Context context, @DrawableRes final int n, final boolean b) {
        this.checkVectorDrawableSetup(context);
        Drawable drawable = this.loadDrawableFromDelegates(context, n);
        if (drawable == null) {
            drawable = this.createDrawableIfNeeded(context, n);
        }
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(context, n);
        }
        if (drawable != null) {
            drawable = this.tintDrawable(context, n, b, drawable);
        }
        if (drawable != null) {
            DrawableUtils.fixDrawable(drawable);
            return drawable;
        }
        return drawable;
    }
    
    ColorStateList getTintList(@NonNull final Context context, @DrawableRes final int n) {
        ColorStateList list = this.getTintListFromCache(context, n);
        if (list != null) {
            return list;
        }
        if (n == R.drawable.abc_edit_text_material) {
            list = AppCompatResources.getColorStateList(context, R.color.abc_tint_edittext);
        }
        else if (n == R.drawable.abc_switch_track_mtrl_alpha) {
            list = AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_track);
        }
        else if (n == R.drawable.abc_switch_thumb_material) {
            list = this.createSwitchThumbColorStateList(context);
        }
        else if (n == R.drawable.abc_btn_default_mtrl_shape) {
            list = this.createDefaultButtonColorStateList(context);
        }
        else if (n == R.drawable.abc_btn_borderless_material) {
            list = this.createBorderlessButtonColorStateList(context);
        }
        else if (n == R.drawable.abc_btn_colored_material) {
            list = this.createColoredButtonColorStateList(context);
        }
        else if (n != R.drawable.abc_spinner_mtrl_am_alpha && n != R.drawable.abc_spinner_textfield_background_material) {
            if (arrayContains(AppCompatDrawableManager.TINT_COLOR_CONTROL_NORMAL, n)) {
                list = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
            }
            else if (arrayContains(AppCompatDrawableManager.TINT_COLOR_CONTROL_STATE_LIST, n)) {
                list = AppCompatResources.getColorStateList(context, R.color.abc_tint_default);
            }
            else if (arrayContains(AppCompatDrawableManager.TINT_CHECKABLE_BUTTON_LIST, n)) {
                list = AppCompatResources.getColorStateList(context, R.color.abc_tint_btn_checkable);
            }
            else if (n == R.drawable.abc_seekbar_thumb_material) {
                list = AppCompatResources.getColorStateList(context, R.color.abc_tint_seek_thumb);
            }
        }
        else {
            list = AppCompatResources.getColorStateList(context, R.color.abc_tint_spinner);
        }
        if (list != null) {
            this.addTintListToCache(context, n, list);
            return list;
        }
        return list;
    }
    
    public void onConfigurationChanged(@NonNull final Context context) {
        while (true) {
            synchronized (this.mDrawableCacheLock) {
                final LongSparseArray<WeakReference<Drawable$ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
                if (longSparseArray != null) {
                    longSparseArray.clear();
                }
            }
        }
    }
    
    Drawable onDrawableLoadedFromResources(@NonNull final Context context, @NonNull final VectorEnabledTintResources vectorEnabledTintResources, @DrawableRes final int n) {
        final Drawable loadDrawableFromDelegates = this.loadDrawableFromDelegates(context, n);
        Drawable superGetDrawable;
        if (loadDrawableFromDelegates == null) {
            superGetDrawable = vectorEnabledTintResources.superGetDrawable(n);
        }
        else {
            superGetDrawable = loadDrawableFromDelegates;
        }
        if (superGetDrawable != null) {
            return this.tintDrawable(context, n, false, superGetDrawable);
        }
        return null;
    }
    
    @RequiresApi(11)
    private static class AvdcInflateDelegate implements InflateDelegate
    {
        AvdcInflateDelegate() {
        }
        
        @Override
        public Drawable createFromXmlInner(@NonNull final Context context, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, set, resources$Theme);
            }
            catch (Exception ex) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", (Throwable)ex);
                return null;
            }
        }
    }
    
    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter>
    {
        public ColorFilterLruCache(final int n) {
            super(n);
        }
        
        private static int generateCacheKey(final int n, final PorterDuff$Mode porterDuff$Mode) {
            return (1 * 31 + n) * 31 + porterDuff$Mode.hashCode();
        }
        
        PorterDuffColorFilter get(final int n, final PorterDuff$Mode porterDuff$Mode) {
            return this.get(generateCacheKey(n, porterDuff$Mode));
        }
        
        PorterDuffColorFilter put(final int n, final PorterDuff$Mode porterDuff$Mode, final PorterDuffColorFilter porterDuffColorFilter) {
            return this.put(generateCacheKey(n, porterDuff$Mode), porterDuffColorFilter);
        }
    }
    
    private interface InflateDelegate
    {
        Drawable createFromXmlInner(@NonNull final Context p0, @NonNull final XmlPullParser p1, @NonNull final AttributeSet p2, @Nullable final Resources$Theme p3);
    }
    
    private static class VdcInflateDelegate implements InflateDelegate
    {
        VdcInflateDelegate() {
        }
        
        @Override
        public Drawable createFromXmlInner(@NonNull final Context context, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), xmlPullParser, set, resources$Theme);
            }
            catch (Exception ex) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", (Throwable)ex);
                return null;
            }
        }
    }
}
