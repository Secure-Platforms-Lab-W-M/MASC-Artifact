/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.ColorFilter
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Paint$Cap
 *  android.graphics.Paint$Join
 *  android.graphics.Paint$Style
 *  android.graphics.Path
 *  android.graphics.Path$FillType
 *  android.graphics.PathMeasure
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.Rect
 *  android.graphics.Region
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.graphics.drawable.VectorDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.graphics.drawable.AndroidResources;
import android.support.graphics.drawable.VectorDrawableCommon;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorDrawableCompat
extends VectorDrawableCommon {
    private static final boolean DBG_VECTOR_DRAWABLE = false;
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private static final int LINECAP_BUTT = 0;
    private static final int LINECAP_ROUND = 1;
    private static final int LINECAP_SQUARE = 2;
    private static final int LINEJOIN_BEVEL = 2;
    private static final int LINEJOIN_MITER = 0;
    private static final int LINEJOIN_ROUND = 1;
    static final String LOGTAG = "VectorDrawableCompat";
    private static final int MAX_CACHED_BITMAP_SIZE = 2048;
    private static final String SHAPE_CLIP_PATH = "clip-path";
    private static final String SHAPE_GROUP = "group";
    private static final String SHAPE_PATH = "path";
    private static final String SHAPE_VECTOR = "vector";
    private boolean mAllowCaching = true;
    private Drawable.ConstantState mCachedConstantStateDelegate;
    private ColorFilter mColorFilter;
    private boolean mMutated;
    private PorterDuffColorFilter mTintFilter;
    private final Rect mTmpBounds = new Rect();
    private final float[] mTmpFloats = new float[9];
    private final Matrix mTmpMatrix = new Matrix();
    private VectorDrawableCompatState mVectorState;

    VectorDrawableCompat() {
        this.mVectorState = new VectorDrawableCompatState();
    }

    VectorDrawableCompat(@NonNull VectorDrawableCompatState vectorDrawableCompatState) {
        this.mVectorState = vectorDrawableCompatState;
        this.mTintFilter = this.updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, vectorDrawableCompatState.mTintMode);
    }

    static int applyAlpha(int n, float f) {
        return n & 16777215 | (int)((float)Color.alpha((int)n) * f) << 24;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Nullable
    public static VectorDrawableCompat create(@NonNull Resources resources, @DrawableRes int n, @Nullable Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 24) {
            VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(resources, n, theme);
            vectorDrawableCompat.mCachedConstantStateDelegate = new VectorDrawableDelegateState(vectorDrawableCompat.mDelegateDrawable.getConstantState());
            return vectorDrawableCompat;
        }
        XmlResourceParser xmlResourceParser = resources.getXml(n);
        AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
        while ((n = xmlResourceParser.next()) != 2 && n != 1) {
        }
        if (n != 2) throw new XmlPullParserException("No start tag found");
        try {
            return VectorDrawableCompat.createFromXmlInner(resources, (XmlPullParser)xmlResourceParser, attributeSet, theme);
        }
        catch (IOException iOException) {
            Log.e((String)"VectorDrawableCompat", (String)"parser error", (Throwable)iOException);
            return null;
        }
        catch (XmlPullParserException xmlPullParserException) {
            Log.e((String)"VectorDrawableCompat", (String)"parser error", (Throwable)xmlPullParserException);
        }
        return null;
    }

    public static VectorDrawableCompat createFromXmlInner(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
        vectorDrawableCompat.inflate(resources, xmlPullParser, attributeSet, theme);
        return vectorDrawableCompat;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void inflateInternal(Resources var1_1, XmlPullParser var2_2, AttributeSet var3_3, Resources.Theme var4_4) throws XmlPullParserException, IOException {
        var9_5 = this.mVectorState;
        var10_6 = var9_5.mVPathRenderer;
        var5_7 = true;
        var11_8 = new Stack<Object>();
        var11_8.push(var10_6.mRootGroup);
        var7_9 = var2_2.getEventType();
        var8_10 = var2_2.getDepth();
        while (var7_9 != 1 && (var2_2.getDepth() >= var8_10 + 1 || var7_9 != 3)) {
            block10 : {
                block12 : {
                    block11 : {
                        if (var7_9 != 2) break block10;
                        var13_13 = var2_2.getName();
                        var12_12 = (VGroup)var11_8.peek();
                        if (!"path".equals(var13_13)) break block11;
                        var13_13 = new VFullPath();
                        var13_13.inflate((Resources)var1_1, var3_3, var4_4, (XmlPullParser)var2_2);
                        var12_12.mChildren.add(var13_13);
                        if (var13_13.getPathName() != null) {
                            var10_6.mVGTargetsMap.put(var13_13.getPathName(), var13_13);
                        }
                        var6_11 = false;
                        var9_5.mChangingConfigurations |= var13_13.mChangingConfigurations;
                        ** GOTO lbl-1000
                    }
                    if (!"clip-path".equals(var13_13)) break block12;
                    var13_13 = new VClipPath();
                    var13_13.inflate((Resources)var1_1, var3_3, var4_4, (XmlPullParser)var2_2);
                    var12_12.mChildren.add(var13_13);
                    if (var13_13.getPathName() != null) {
                        var10_6.mVGTargetsMap.put(var13_13.getPathName(), var13_13);
                    }
                    var9_5.mChangingConfigurations |= var13_13.mChangingConfigurations;
                    ** GOTO lbl-1000
                }
                if ("group".equals(var13_13)) {
                    var13_13 = new VGroup();
                    var13_13.inflate((Resources)var1_1, var3_3, var4_4, (XmlPullParser)var2_2);
                    var12_12.mChildren.add(var13_13);
                    var11_8.push(var13_13);
                    if (var13_13.getGroupName() != null) {
                        var10_6.mVGTargetsMap.put(var13_13.getGroupName(), var13_13);
                    }
                    var9_5.mChangingConfigurations |= var13_13.mChangingConfigurations;
                    var6_11 = var5_7;
                } else lbl-1000: // 2 sources:
                {
                    var6_11 = var5_7;
                }
                ** GOTO lbl-1000
            }
            var6_11 = var5_7;
            if (var7_9 == 3) {
                if ("group".equals(var2_2.getName())) {
                    var11_8.pop();
                }
            } else lbl-1000: // 4 sources:
            {
                var5_7 = var6_11;
            }
            var7_9 = var2_2.next();
        }
        if (var5_7 == false) return;
        var1_1 = new StringBuffer();
        if (var1_1.length() > 0) {
            var1_1.append(" or ");
        }
        var1_1.append("path");
        var2_2 = new StringBuilder();
        var2_2.append("no ");
        var2_2.append(var1_1);
        var2_2.append(" defined");
        throw new XmlPullParserException(var2_2.toString());
    }

    private boolean needMirroring() {
        if (Build.VERSION.SDK_INT >= 17) {
            if (this.isAutoMirrored() && DrawableCompat.getLayoutDirection(this) == 1) {
                return true;
            }
            return false;
        }
        return false;
    }

    private static PorterDuff.Mode parseTintModeCompat(int n, PorterDuff.Mode mode) {
        if (n != 3) {
            if (n != 5) {
                if (n != 9) {
                    switch (n) {
                        default: {
                            return mode;
                        }
                        case 16: {
                            if (Build.VERSION.SDK_INT >= 11) {
                                return PorterDuff.Mode.ADD;
                            }
                            return mode;
                        }
                        case 15: {
                            return PorterDuff.Mode.SCREEN;
                        }
                        case 14: 
                    }
                    return PorterDuff.Mode.MULTIPLY;
                }
                return PorterDuff.Mode.SRC_ATOP;
            }
            return PorterDuff.Mode.SRC_IN;
        }
        return PorterDuff.Mode.SRC_OVER;
    }

    private void printGroupTree(VGroup vGroup, int n) {
        StringBuilder stringBuilder;
        int n2;
        Object object = "";
        for (n2 = 0; n2 < n; ++n2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append("    ");
            object = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append((String)object);
        stringBuilder.append("current group is :");
        stringBuilder.append(vGroup.getGroupName());
        stringBuilder.append(" rotation is ");
        stringBuilder.append(vGroup.mRotate);
        Log.v((String)"VectorDrawableCompat", (String)stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append((String)object);
        stringBuilder.append("matrix is :");
        stringBuilder.append(vGroup.getLocalMatrix().toString());
        Log.v((String)"VectorDrawableCompat", (String)stringBuilder.toString());
        for (n2 = 0; n2 < vGroup.mChildren.size(); ++n2) {
            object = vGroup.mChildren.get(n2);
            if (object instanceof VGroup) {
                this.printGroupTree((VGroup)object, n + 1);
                continue;
            }
            ((VPath)object).printVPath(n + 1);
        }
    }

    private void updateStateFromTypedArray(TypedArray object, XmlPullParser object2) throws XmlPullParserException {
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        VPathRenderer vPathRenderer = vectorDrawableCompatState.mVPathRenderer;
        vectorDrawableCompatState.mTintMode = VectorDrawableCompat.parseTintModeCompat(TypedArrayUtils.getNamedInt((TypedArray)object, (XmlPullParser)object2, "tintMode", 6, -1), PorterDuff.Mode.SRC_IN);
        ColorStateList colorStateList = object.getColorStateList(1);
        if (colorStateList != null) {
            vectorDrawableCompatState.mTint = colorStateList;
        }
        vectorDrawableCompatState.mAutoMirrored = TypedArrayUtils.getNamedBoolean((TypedArray)object, (XmlPullParser)object2, "autoMirrored", 5, vectorDrawableCompatState.mAutoMirrored);
        vPathRenderer.mViewportWidth = TypedArrayUtils.getNamedFloat((TypedArray)object, (XmlPullParser)object2, "viewportWidth", 7, vPathRenderer.mViewportWidth);
        vPathRenderer.mViewportHeight = TypedArrayUtils.getNamedFloat((TypedArray)object, (XmlPullParser)object2, "viewportHeight", 8, vPathRenderer.mViewportHeight);
        if (vPathRenderer.mViewportWidth > 0.0f) {
            if (vPathRenderer.mViewportHeight > 0.0f) {
                vPathRenderer.mBaseWidth = object.getDimension(3, vPathRenderer.mBaseWidth);
                vPathRenderer.mBaseHeight = object.getDimension(2, vPathRenderer.mBaseHeight);
                if (vPathRenderer.mBaseWidth > 0.0f) {
                    if (vPathRenderer.mBaseHeight > 0.0f) {
                        vPathRenderer.setAlpha(TypedArrayUtils.getNamedFloat((TypedArray)object, (XmlPullParser)object2, "alpha", 4, vPathRenderer.getAlpha()));
                        object = object.getString(0);
                        if (object != null) {
                            vPathRenderer.mRootName = object;
                            vPathRenderer.mVGTargetsMap.put((String)object, vPathRenderer);
                            return;
                        }
                        return;
                    }
                    object2 = new StringBuilder();
                    object2.append(object.getPositionDescription());
                    object2.append("<vector> tag requires height > 0");
                    throw new XmlPullParserException(object2.toString());
                }
                object2 = new StringBuilder();
                object2.append(object.getPositionDescription());
                object2.append("<vector> tag requires width > 0");
                throw new XmlPullParserException(object2.toString());
            }
            object2 = new StringBuilder();
            object2.append(object.getPositionDescription());
            object2.append("<vector> tag requires viewportHeight > 0");
            throw new XmlPullParserException(object2.toString());
        }
        object2 = new StringBuilder();
        object2.append(object.getPositionDescription());
        object2.append("<vector> tag requires viewportWidth > 0");
        throw new XmlPullParserException(object2.toString());
    }

    public boolean canApplyTheme() {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.canApplyTheme(this.mDelegateDrawable);
        }
        return false;
    }

    public void draw(Canvas canvas) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.draw(canvas);
            return;
        }
        this.copyBounds(this.mTmpBounds);
        if (this.mTmpBounds.width() > 0) {
            ColorFilter colorFilter;
            if (this.mTmpBounds.height() <= 0) {
                return;
            }
            ColorFilter colorFilter2 = colorFilter = this.mColorFilter;
            if (colorFilter == null) {
                colorFilter2 = this.mTintFilter;
            }
            canvas.getMatrix(this.mTmpMatrix);
            this.mTmpMatrix.getValues(this.mTmpFloats);
            float f = Math.abs(this.mTmpFloats[0]);
            float f2 = Math.abs(this.mTmpFloats[4]);
            float f3 = Math.abs(this.mTmpFloats[1]);
            float f4 = Math.abs(this.mTmpFloats[3]);
            if (f3 != 0.0f || f4 != 0.0f) {
                f = 1.0f;
                f2 = 1.0f;
            }
            int n = (int)((float)this.mTmpBounds.width() * f);
            int n2 = (int)((float)this.mTmpBounds.height() * f2);
            n = Math.min(2048, n);
            n2 = Math.min(2048, n2);
            if (n > 0) {
                if (n2 <= 0) {
                    return;
                }
                int n3 = canvas.save();
                canvas.translate((float)this.mTmpBounds.left, (float)this.mTmpBounds.top);
                if (this.needMirroring()) {
                    canvas.translate((float)this.mTmpBounds.width(), 0.0f);
                    canvas.scale(-1.0f, 1.0f);
                }
                this.mTmpBounds.offsetTo(0, 0);
                this.mVectorState.createCachedBitmapIfNeeded(n, n2);
                if (!this.mAllowCaching) {
                    this.mVectorState.updateCachedBitmap(n, n2);
                } else if (!this.mVectorState.canReuseCache()) {
                    this.mVectorState.updateCachedBitmap(n, n2);
                    this.mVectorState.updateCacheStates();
                }
                this.mVectorState.drawCachedBitmapWithRootAlpha(canvas, colorFilter2, this.mTmpBounds);
                canvas.restoreToCount(n3);
                return;
            }
            return;
        }
    }

    public int getAlpha() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getAlpha(this.mDelegateDrawable);
        }
        return this.mVectorState.mVPathRenderer.getRootAlpha();
    }

    public int getChangingConfigurations() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
    }

    public Drawable.ConstantState getConstantState() {
        if (this.mDelegateDrawable != null && Build.VERSION.SDK_INT >= 24) {
            return new VectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
        }
        this.mVectorState.mChangingConfigurations = this.getChangingConfigurations();
        return this.mVectorState;
    }

    public int getIntrinsicHeight() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicHeight();
        }
        return (int)this.mVectorState.mVPathRenderer.mBaseHeight;
    }

    public int getIntrinsicWidth() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicWidth();
        }
        return (int)this.mVectorState.mVPathRenderer.mBaseWidth;
    }

    public int getOpacity() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getOpacity();
        }
        return -3;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public float getPixelSize() {
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        if (vectorDrawableCompatState != null && vectorDrawableCompatState.mVPathRenderer != null && this.mVectorState.mVPathRenderer.mBaseWidth != 0.0f && this.mVectorState.mVPathRenderer.mBaseHeight != 0.0f && this.mVectorState.mVPathRenderer.mViewportHeight != 0.0f && this.mVectorState.mVPathRenderer.mViewportWidth != 0.0f) {
            float f = this.mVectorState.mVPathRenderer.mBaseWidth;
            float f2 = this.mVectorState.mVPathRenderer.mBaseHeight;
            float f3 = this.mVectorState.mVPathRenderer.mViewportWidth;
            float f4 = this.mVectorState.mVPathRenderer.mViewportHeight;
            return Math.min(f3 / f, f4 / f2);
        }
        return 1.0f;
    }

    Object getTargetByName(String string2) {
        return this.mVectorState.mVPathRenderer.mVGTargetsMap.get(string2);
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.inflate(resources, xmlPullParser, attributeSet);
            return;
        }
        this.inflate(resources, xmlPullParser, attributeSet, null);
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.inflate(this.mDelegateDrawable, resources, xmlPullParser, attributeSet, theme);
            return;
        }
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        vectorDrawableCompatState.mVPathRenderer = new VPathRenderer();
        TypedArray typedArray = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_TYPE_ARRAY);
        this.updateStateFromTypedArray(typedArray, xmlPullParser);
        typedArray.recycle();
        vectorDrawableCompatState.mChangingConfigurations = this.getChangingConfigurations();
        vectorDrawableCompatState.mCacheDirty = true;
        this.inflateInternal(resources, xmlPullParser, attributeSet, theme);
        this.mTintFilter = this.updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, vectorDrawableCompatState.mTintMode);
    }

    public void invalidateSelf() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.invalidateSelf();
            return;
        }
        super.invalidateSelf();
    }

    public boolean isAutoMirrored() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
        }
        return this.mVectorState.mAutoMirrored;
    }

    public boolean isStateful() {
        VectorDrawableCompatState vectorDrawableCompatState;
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.isStateful();
        }
        if (!(super.isStateful() || (vectorDrawableCompatState = this.mVectorState) != null && vectorDrawableCompatState.mTint != null && this.mVectorState.mTint.isStateful())) {
            return false;
        }
        return true;
    }

    public Drawable mutate() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.mutate();
            return this;
        }
        if (!this.mMutated && super.mutate() == this) {
            this.mVectorState = new VectorDrawableCompatState(this.mVectorState);
            this.mMutated = true;
            return this;
        }
        return this;
    }

    @Override
    protected void onBoundsChange(Rect rect) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setBounds(rect);
            return;
        }
    }

    protected boolean onStateChange(int[] object) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState((int[])object);
        }
        object = this.mVectorState;
        if (object.mTint != null && object.mTintMode != null) {
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, object.mTint, object.mTintMode);
            this.invalidateSelf();
            return true;
        }
        return false;
    }

    public void scheduleSelf(Runnable runnable, long l) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.scheduleSelf(runnable, l);
            return;
        }
        super.scheduleSelf(runnable, l);
    }

    void setAllowCaching(boolean bl) {
        this.mAllowCaching = bl;
    }

    public void setAlpha(int n) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setAlpha(n);
            return;
        }
        if (this.mVectorState.mVPathRenderer.getRootAlpha() != n) {
            this.mVectorState.mVPathRenderer.setRootAlpha(n);
            this.invalidateSelf();
            return;
        }
    }

    public void setAutoMirrored(boolean bl) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setAutoMirrored(this.mDelegateDrawable, bl);
            return;
        }
        this.mVectorState.mAutoMirrored = bl;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        this.mColorFilter = colorFilter;
        this.invalidateSelf();
    }

    @Override
    public void setTint(int n) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTint(this.mDelegateDrawable, n);
            return;
        }
        this.setTintList(ColorStateList.valueOf((int)n));
    }

    @Override
    public void setTintList(ColorStateList colorStateList) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintList(this.mDelegateDrawable, colorStateList);
            return;
        }
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        if (vectorDrawableCompatState.mTint != colorStateList) {
            vectorDrawableCompatState.mTint = colorStateList;
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, colorStateList, vectorDrawableCompatState.mTintMode);
            this.invalidateSelf();
            return;
        }
    }

    @Override
    public void setTintMode(PorterDuff.Mode mode) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintMode(this.mDelegateDrawable, mode);
            return;
        }
        VectorDrawableCompatState vectorDrawableCompatState = this.mVectorState;
        if (vectorDrawableCompatState.mTintMode != mode) {
            vectorDrawableCompatState.mTintMode = mode;
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, vectorDrawableCompatState.mTint, mode);
            this.invalidateSelf();
            return;
        }
    }

    public boolean setVisible(boolean bl, boolean bl2) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setVisible(bl, bl2);
        }
        return super.setVisible(bl, bl2);
    }

    public void unscheduleSelf(Runnable runnable) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.unscheduleSelf(runnable);
            return;
        }
        super.unscheduleSelf(runnable);
    }

    PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter porterDuffColorFilter, ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList != null && mode != null) {
            return new PorterDuffColorFilter(colorStateList.getColorForState(this.getState(), 0), mode);
        }
        return null;
    }

    private static class VClipPath
    extends VPath {
        public VClipPath() {
        }

        public VClipPath(VClipPath vClipPath) {
            super(vClipPath);
        }

        private void updateStateFromTypedArray(TypedArray object) {
            String string2 = object.getString(0);
            if (string2 != null) {
                this.mPathName = string2;
            }
            if ((object = object.getString(1)) != null) {
                this.mNodes = PathParser.createNodesFromPathData((String)object);
                return;
            }
        }

        public void inflate(Resources resources, AttributeSet attributeSet, Resources.Theme theme, XmlPullParser xmlPullParser) {
            if (!TypedArrayUtils.hasAttribute(xmlPullParser, "pathData")) {
                return;
            }
            resources = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_CLIP_PATH);
            this.updateStateFromTypedArray((TypedArray)resources);
            resources.recycle();
        }

        @Override
        public boolean isClipPath() {
            return true;
        }
    }

    private static class VFullPath
    extends VPath {
        float mFillAlpha = 1.0f;
        int mFillColor = 0;
        int mFillRule = 0;
        float mStrokeAlpha = 1.0f;
        int mStrokeColor = 0;
        Paint.Cap mStrokeLineCap = Paint.Cap.BUTT;
        Paint.Join mStrokeLineJoin = Paint.Join.MITER;
        float mStrokeMiterlimit = 4.0f;
        float mStrokeWidth = 0.0f;
        private int[] mThemeAttrs;
        float mTrimPathEnd = 1.0f;
        float mTrimPathOffset = 0.0f;
        float mTrimPathStart = 0.0f;

        public VFullPath() {
        }

        public VFullPath(VFullPath vFullPath) {
            super(vFullPath);
            this.mThemeAttrs = vFullPath.mThemeAttrs;
            this.mStrokeColor = vFullPath.mStrokeColor;
            this.mStrokeWidth = vFullPath.mStrokeWidth;
            this.mStrokeAlpha = vFullPath.mStrokeAlpha;
            this.mFillColor = vFullPath.mFillColor;
            this.mFillRule = vFullPath.mFillRule;
            this.mFillAlpha = vFullPath.mFillAlpha;
            this.mTrimPathStart = vFullPath.mTrimPathStart;
            this.mTrimPathEnd = vFullPath.mTrimPathEnd;
            this.mTrimPathOffset = vFullPath.mTrimPathOffset;
            this.mStrokeLineCap = vFullPath.mStrokeLineCap;
            this.mStrokeLineJoin = vFullPath.mStrokeLineJoin;
            this.mStrokeMiterlimit = vFullPath.mStrokeMiterlimit;
        }

        private Paint.Cap getStrokeLineCap(int n, Paint.Cap cap) {
            switch (n) {
                default: {
                    return cap;
                }
                case 2: {
                    return Paint.Cap.SQUARE;
                }
                case 1: {
                    return Paint.Cap.ROUND;
                }
                case 0: 
            }
            return Paint.Cap.BUTT;
        }

        private Paint.Join getStrokeLineJoin(int n, Paint.Join join) {
            switch (n) {
                default: {
                    return join;
                }
                case 2: {
                    return Paint.Join.BEVEL;
                }
                case 1: {
                    return Paint.Join.ROUND;
                }
                case 0: 
            }
            return Paint.Join.MITER;
        }

        private void updateStateFromTypedArray(TypedArray typedArray, XmlPullParser xmlPullParser) {
            this.mThemeAttrs = null;
            if (!TypedArrayUtils.hasAttribute(xmlPullParser, "pathData")) {
                return;
            }
            String string2 = typedArray.getString(0);
            if (string2 != null) {
                this.mPathName = string2;
            }
            if ((string2 = typedArray.getString(2)) != null) {
                this.mNodes = PathParser.createNodesFromPathData(string2);
            }
            this.mFillColor = TypedArrayUtils.getNamedColor(typedArray, xmlPullParser, "fillColor", 1, this.mFillColor);
            this.mFillAlpha = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "fillAlpha", 12, this.mFillAlpha);
            this.mStrokeLineCap = this.getStrokeLineCap(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "strokeLineCap", 8, -1), this.mStrokeLineCap);
            this.mStrokeLineJoin = this.getStrokeLineJoin(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
            this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
            this.mStrokeColor = TypedArrayUtils.getNamedColor(typedArray, xmlPullParser, "strokeColor", 3, this.mStrokeColor);
            this.mStrokeAlpha = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "strokeAlpha", 11, this.mStrokeAlpha);
            this.mStrokeWidth = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "strokeWidth", 4, this.mStrokeWidth);
            this.mTrimPathEnd = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "trimPathEnd", 6, this.mTrimPathEnd);
            this.mTrimPathOffset = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "trimPathOffset", 7, this.mTrimPathOffset);
            this.mTrimPathStart = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "trimPathStart", 5, this.mTrimPathStart);
            this.mFillRule = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "fillType", 13, this.mFillRule);
        }

        @Override
        public void applyTheme(Resources.Theme theme) {
            if (this.mThemeAttrs == null) {
                return;
            }
        }

        @Override
        public boolean canApplyTheme() {
            if (this.mThemeAttrs != null) {
                return true;
            }
            return false;
        }

        float getFillAlpha() {
            return this.mFillAlpha;
        }

        int getFillColor() {
            return this.mFillColor;
        }

        float getStrokeAlpha() {
            return this.mStrokeAlpha;
        }

        int getStrokeColor() {
            return this.mStrokeColor;
        }

        float getStrokeWidth() {
            return this.mStrokeWidth;
        }

        float getTrimPathEnd() {
            return this.mTrimPathEnd;
        }

        float getTrimPathOffset() {
            return this.mTrimPathOffset;
        }

        float getTrimPathStart() {
            return this.mTrimPathStart;
        }

        public void inflate(Resources resources, AttributeSet attributeSet, Resources.Theme theme, XmlPullParser xmlPullParser) {
            resources = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_PATH);
            this.updateStateFromTypedArray((TypedArray)resources, xmlPullParser);
            resources.recycle();
        }

        void setFillAlpha(float f) {
            this.mFillAlpha = f;
        }

        void setFillColor(int n) {
            this.mFillColor = n;
        }

        void setStrokeAlpha(float f) {
            this.mStrokeAlpha = f;
        }

        void setStrokeColor(int n) {
            this.mStrokeColor = n;
        }

        void setStrokeWidth(float f) {
            this.mStrokeWidth = f;
        }

        void setTrimPathEnd(float f) {
            this.mTrimPathEnd = f;
        }

        void setTrimPathOffset(float f) {
            this.mTrimPathOffset = f;
        }

        void setTrimPathStart(float f) {
            this.mTrimPathStart = f;
        }
    }

    private static class VGroup {
        int mChangingConfigurations;
        final ArrayList<Object> mChildren = new ArrayList();
        private String mGroupName = null;
        private final Matrix mLocalMatrix = new Matrix();
        private float mPivotX = 0.0f;
        private float mPivotY = 0.0f;
        float mRotate = 0.0f;
        private float mScaleX = 1.0f;
        private float mScaleY = 1.0f;
        private final Matrix mStackedMatrix = new Matrix();
        private int[] mThemeAttrs;
        private float mTranslateX = 0.0f;
        private float mTranslateY = 0.0f;

        public VGroup() {
        }

        public VGroup(VGroup object, ArrayMap<String, Object> arrayMap) {
            this.mRotate = object.mRotate;
            this.mPivotX = object.mPivotX;
            this.mPivotY = object.mPivotY;
            this.mScaleX = object.mScaleX;
            this.mScaleY = object.mScaleY;
            this.mTranslateX = object.mTranslateX;
            this.mTranslateY = object.mTranslateY;
            this.mThemeAttrs = object.mThemeAttrs;
            this.mGroupName = object.mGroupName;
            this.mChangingConfigurations = object.mChangingConfigurations;
            Object object2 = this.mGroupName;
            if (object2 != null) {
                arrayMap.put((String)object2, this);
            }
            this.mLocalMatrix.set(object.mLocalMatrix);
            object2 = object.mChildren;
            for (int i = 0; i < object2.size(); ++i) {
                block8 : {
                    block7 : {
                        block6 : {
                            object = object2.get(i);
                            if (object instanceof VGroup) {
                                this.mChildren.add(new VGroup((VGroup)object, arrayMap));
                                continue;
                            }
                            if (!(object instanceof VFullPath)) break block6;
                            object = new VFullPath((VFullPath)object);
                            break block7;
                        }
                        if (!(object instanceof VClipPath)) break block8;
                        object = new VClipPath((VClipPath)object);
                    }
                    this.mChildren.add(object);
                    if (object.mPathName == null) continue;
                    arrayMap.put(object.mPathName, object);
                    continue;
                }
                throw new IllegalStateException("Unknown object in the tree!");
            }
        }

        private void updateLocalMatrix() {
            this.mLocalMatrix.reset();
            this.mLocalMatrix.postTranslate(- this.mPivotX, - this.mPivotY);
            this.mLocalMatrix.postScale(this.mScaleX, this.mScaleY);
            this.mLocalMatrix.postRotate(this.mRotate, 0.0f, 0.0f);
            this.mLocalMatrix.postTranslate(this.mTranslateX + this.mPivotX, this.mTranslateY + this.mPivotY);
        }

        private void updateStateFromTypedArray(TypedArray object, XmlPullParser xmlPullParser) {
            this.mThemeAttrs = null;
            this.mRotate = TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "rotation", 5, this.mRotate);
            this.mPivotX = object.getFloat(1, this.mPivotX);
            this.mPivotY = object.getFloat(2, this.mPivotY);
            this.mScaleX = TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "scaleX", 3, this.mScaleX);
            this.mScaleY = TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "scaleY", 4, this.mScaleY);
            this.mTranslateX = TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "translateX", 6, this.mTranslateX);
            this.mTranslateY = TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "translateY", 7, this.mTranslateY);
            if ((object = object.getString(0)) != null) {
                this.mGroupName = object;
            }
            this.updateLocalMatrix();
        }

        public String getGroupName() {
            return this.mGroupName;
        }

        public Matrix getLocalMatrix() {
            return this.mLocalMatrix;
        }

        public float getPivotX() {
            return this.mPivotX;
        }

        public float getPivotY() {
            return this.mPivotY;
        }

        public float getRotation() {
            return this.mRotate;
        }

        public float getScaleX() {
            return this.mScaleX;
        }

        public float getScaleY() {
            return this.mScaleY;
        }

        public float getTranslateX() {
            return this.mTranslateX;
        }

        public float getTranslateY() {
            return this.mTranslateY;
        }

        public void inflate(Resources resources, AttributeSet attributeSet, Resources.Theme theme, XmlPullParser xmlPullParser) {
            resources = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_GROUP);
            this.updateStateFromTypedArray((TypedArray)resources, xmlPullParser);
            resources.recycle();
        }

        public void setPivotX(float f) {
            if (f != this.mPivotX) {
                this.mPivotX = f;
                this.updateLocalMatrix();
                return;
            }
        }

        public void setPivotY(float f) {
            if (f != this.mPivotY) {
                this.mPivotY = f;
                this.updateLocalMatrix();
                return;
            }
        }

        public void setRotation(float f) {
            if (f != this.mRotate) {
                this.mRotate = f;
                this.updateLocalMatrix();
                return;
            }
        }

        public void setScaleX(float f) {
            if (f != this.mScaleX) {
                this.mScaleX = f;
                this.updateLocalMatrix();
                return;
            }
        }

        public void setScaleY(float f) {
            if (f != this.mScaleY) {
                this.mScaleY = f;
                this.updateLocalMatrix();
                return;
            }
        }

        public void setTranslateX(float f) {
            if (f != this.mTranslateX) {
                this.mTranslateX = f;
                this.updateLocalMatrix();
                return;
            }
        }

        public void setTranslateY(float f) {
            if (f != this.mTranslateY) {
                this.mTranslateY = f;
                this.updateLocalMatrix();
                return;
            }
        }
    }

    private static class VPath {
        int mChangingConfigurations;
        protected PathParser.PathDataNode[] mNodes = null;
        String mPathName;

        public VPath() {
        }

        public VPath(VPath vPath) {
            this.mPathName = vPath.mPathName;
            this.mChangingConfigurations = vPath.mChangingConfigurations;
            this.mNodes = PathParser.deepCopyNodes(vPath.mNodes);
        }

        public void applyTheme(Resources.Theme theme) {
        }

        public boolean canApplyTheme() {
            return false;
        }

        public PathParser.PathDataNode[] getPathData() {
            return this.mNodes;
        }

        public String getPathName() {
            return this.mPathName;
        }

        public boolean isClipPath() {
            return false;
        }

        public String nodesToString(PathParser.PathDataNode[] arrpathDataNode) {
            String string2 = " ";
            for (int i = 0; i < arrpathDataNode.length; ++i) {
                float[] arrf = new float[]();
                arrf.append(string2);
                arrf.append(arrpathDataNode[i].mType);
                arrf.append(":");
                string2 = arrf.toString();
                arrf = arrpathDataNode[i].mParams;
                for (int j = 0; j < arrf.length; ++j) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(string2);
                    stringBuilder.append(arrf[j]);
                    stringBuilder.append(",");
                    string2 = stringBuilder.toString();
                }
            }
            return string2;
        }

        public void printVPath(int n) {
            StringBuilder stringBuilder;
            String string2 = "";
            for (int i = 0; i < n; ++i) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append("    ");
                string2 = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("current path is :");
            stringBuilder.append(this.mPathName);
            stringBuilder.append(" pathData is ");
            stringBuilder.append(this.nodesToString(this.mNodes));
            Log.v((String)"VectorDrawableCompat", (String)stringBuilder.toString());
        }

        public void setPathData(PathParser.PathDataNode[] arrpathDataNode) {
            if (!PathParser.canMorph(this.mNodes, arrpathDataNode)) {
                this.mNodes = PathParser.deepCopyNodes(arrpathDataNode);
                return;
            }
            PathParser.updateNodes(this.mNodes, arrpathDataNode);
        }

        public void toPath(Path path) {
            path.reset();
            PathParser.PathDataNode[] arrpathDataNode = this.mNodes;
            if (arrpathDataNode != null) {
                PathParser.PathDataNode.nodesToPath(arrpathDataNode, path);
                return;
            }
        }
    }

    private static class VPathRenderer {
        private static final Matrix IDENTITY_MATRIX = new Matrix();
        float mBaseHeight = 0.0f;
        float mBaseWidth = 0.0f;
        private int mChangingConfigurations;
        private Paint mFillPaint;
        private final Matrix mFinalPathMatrix = new Matrix();
        private final Path mPath;
        private PathMeasure mPathMeasure;
        private final Path mRenderPath;
        int mRootAlpha = 255;
        final VGroup mRootGroup;
        String mRootName = null;
        private Paint mStrokePaint;
        final ArrayMap<String, Object> mVGTargetsMap = new ArrayMap();
        float mViewportHeight = 0.0f;
        float mViewportWidth = 0.0f;

        public VPathRenderer() {
            this.mRootGroup = new VGroup();
            this.mPath = new Path();
            this.mRenderPath = new Path();
        }

        public VPathRenderer(VPathRenderer object) {
            this.mRootGroup = new VGroup(object.mRootGroup, this.mVGTargetsMap);
            this.mPath = new Path(object.mPath);
            this.mRenderPath = new Path(object.mRenderPath);
            this.mBaseWidth = object.mBaseWidth;
            this.mBaseHeight = object.mBaseHeight;
            this.mViewportWidth = object.mViewportWidth;
            this.mViewportHeight = object.mViewportHeight;
            this.mChangingConfigurations = object.mChangingConfigurations;
            this.mRootAlpha = object.mRootAlpha;
            this.mRootName = object.mRootName;
            object = object.mRootName;
            if (object != null) {
                this.mVGTargetsMap.put((String)object, this);
                return;
            }
        }

        private static float cross(float f, float f2, float f3, float f4) {
            return f * f4 - f2 * f3;
        }

        private void drawGroupTree(VGroup vGroup, Matrix object, Canvas canvas, int n, int n2, ColorFilter colorFilter) {
            vGroup.mStackedMatrix.set((Matrix)object);
            vGroup.mStackedMatrix.preConcat(vGroup.mLocalMatrix);
            canvas.save();
            for (int i = 0; i < vGroup.mChildren.size(); ++i) {
                object = vGroup.mChildren.get(i);
                if (object instanceof VGroup) {
                    this.drawGroupTree((VGroup)object, vGroup.mStackedMatrix, canvas, n, n2, colorFilter);
                    continue;
                }
                if (!(object instanceof VPath)) continue;
                this.drawPath(vGroup, (VPath)object, canvas, n, n2, colorFilter);
            }
            canvas.restore();
        }

        private void drawPath(VGroup vGroup, VPath vPath, Canvas canvas, int n, int n2, ColorFilter colorFilter) {
            float f = (float)n / this.mViewportWidth;
            float f2 = (float)n2 / this.mViewportHeight;
            float f3 = Math.min(f, f2);
            vGroup = vGroup.mStackedMatrix;
            this.mFinalPathMatrix.set((Matrix)vGroup);
            this.mFinalPathMatrix.postScale(f, f2);
            f = this.getMatrixScale((Matrix)vGroup);
            if (f == 0.0f) {
                return;
            }
            vPath.toPath(this.mPath);
            vGroup = this.mPath;
            this.mRenderPath.reset();
            if (vPath.isClipPath()) {
                this.mRenderPath.addPath((Path)vGroup, this.mFinalPathMatrix);
                canvas.clipPath(this.mRenderPath);
                return;
            }
            vPath = (VFullPath)vPath;
            if (vPath.mTrimPathStart != 0.0f || vPath.mTrimPathEnd != 1.0f) {
                float f4 = vPath.mTrimPathStart;
                float f5 = vPath.mTrimPathOffset;
                float f6 = vPath.mTrimPathEnd;
                float f7 = vPath.mTrimPathOffset;
                if (this.mPathMeasure == null) {
                    this.mPathMeasure = new PathMeasure();
                }
                this.mPathMeasure.setPath(this.mPath, false);
                f2 = this.mPathMeasure.getLength();
                f4 = (f4 + f5) % 1.0f * f2;
                f6 = (f6 + f7) % 1.0f * f2;
                vGroup.reset();
                if (f4 > f6) {
                    this.mPathMeasure.getSegment(f4, f2, (Path)vGroup, true);
                    this.mPathMeasure.getSegment(0.0f, f6, (Path)vGroup, true);
                } else {
                    this.mPathMeasure.getSegment(f4, f6, (Path)vGroup, true);
                }
                vGroup.rLineTo(0.0f, 0.0f);
            }
            this.mRenderPath.addPath((Path)vGroup, this.mFinalPathMatrix);
            if (vPath.mFillColor != 0) {
                if (this.mFillPaint == null) {
                    this.mFillPaint = new Paint();
                    this.mFillPaint.setStyle(Paint.Style.FILL);
                    this.mFillPaint.setAntiAlias(true);
                }
                Paint paint = this.mFillPaint;
                paint.setColor(VectorDrawableCompat.applyAlpha(vPath.mFillColor, vPath.mFillAlpha));
                paint.setColorFilter(colorFilter);
                Path path = this.mRenderPath;
                vGroup = vPath.mFillRule == 0 ? Path.FillType.WINDING : Path.FillType.EVEN_ODD;
                path.setFillType((Path.FillType)vGroup);
                canvas.drawPath(this.mRenderPath, paint);
            }
            if (vPath.mStrokeColor != 0) {
                if (this.mStrokePaint == null) {
                    this.mStrokePaint = new Paint();
                    this.mStrokePaint.setStyle(Paint.Style.STROKE);
                    this.mStrokePaint.setAntiAlias(true);
                }
                vGroup = this.mStrokePaint;
                if (vPath.mStrokeLineJoin != null) {
                    vGroup.setStrokeJoin(vPath.mStrokeLineJoin);
                }
                if (vPath.mStrokeLineCap != null) {
                    vGroup.setStrokeCap(vPath.mStrokeLineCap);
                }
                vGroup.setStrokeMiter(vPath.mStrokeMiterlimit);
                vGroup.setColor(VectorDrawableCompat.applyAlpha(vPath.mStrokeColor, vPath.mStrokeAlpha));
                vGroup.setColorFilter(colorFilter);
                vGroup.setStrokeWidth(vPath.mStrokeWidth * (f3 * f));
                canvas.drawPath(this.mRenderPath, (Paint)vGroup);
                return;
            }
        }

        private float getMatrixScale(Matrix matrix) {
            float[] arrf;
            float[] arrf2 = arrf = new float[4];
            arrf2[0] = 0.0f;
            arrf2[1] = 1.0f;
            arrf2[2] = 1.0f;
            arrf2[3] = 0.0f;
            matrix.mapVectors(arrf);
            float f = (float)Math.hypot(arrf[0], arrf[1]);
            float f2 = (float)Math.hypot(arrf[2], arrf[3]);
            float f3 = VPathRenderer.cross(arrf[0], arrf[1], arrf[2], arrf[3]);
            f = Math.max(f, f2);
            if (f > 0.0f) {
                return Math.abs(f3) / f;
            }
            return 0.0f;
        }

        public void draw(Canvas canvas, int n, int n2, ColorFilter colorFilter) {
            this.drawGroupTree(this.mRootGroup, IDENTITY_MATRIX, canvas, n, n2, colorFilter);
        }

        public float getAlpha() {
            return (float)this.getRootAlpha() / 255.0f;
        }

        public int getRootAlpha() {
            return this.mRootAlpha;
        }

        public void setAlpha(float f) {
            this.setRootAlpha((int)(255.0f * f));
        }

        public void setRootAlpha(int n) {
            this.mRootAlpha = n;
        }
    }

    private static class VectorDrawableCompatState
    extends Drawable.ConstantState {
        boolean mAutoMirrored;
        boolean mCacheDirty;
        boolean mCachedAutoMirrored;
        Bitmap mCachedBitmap;
        int mCachedRootAlpha;
        int[] mCachedThemeAttrs;
        ColorStateList mCachedTint;
        PorterDuff.Mode mCachedTintMode;
        int mChangingConfigurations;
        Paint mTempPaint;
        ColorStateList mTint = null;
        PorterDuff.Mode mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
        VPathRenderer mVPathRenderer;

        public VectorDrawableCompatState() {
            this.mVPathRenderer = new VPathRenderer();
        }

        public VectorDrawableCompatState(VectorDrawableCompatState vectorDrawableCompatState) {
            if (vectorDrawableCompatState != null) {
                this.mChangingConfigurations = vectorDrawableCompatState.mChangingConfigurations;
                this.mVPathRenderer = new VPathRenderer(vectorDrawableCompatState.mVPathRenderer);
                if (vectorDrawableCompatState.mVPathRenderer.mFillPaint != null) {
                    this.mVPathRenderer.mFillPaint = new Paint(vectorDrawableCompatState.mVPathRenderer.mFillPaint);
                }
                if (vectorDrawableCompatState.mVPathRenderer.mStrokePaint != null) {
                    this.mVPathRenderer.mStrokePaint = new Paint(vectorDrawableCompatState.mVPathRenderer.mStrokePaint);
                }
                this.mTint = vectorDrawableCompatState.mTint;
                this.mTintMode = vectorDrawableCompatState.mTintMode;
                this.mAutoMirrored = vectorDrawableCompatState.mAutoMirrored;
                return;
            }
        }

        public boolean canReuseBitmap(int n, int n2) {
            if (n == this.mCachedBitmap.getWidth() && n2 == this.mCachedBitmap.getHeight()) {
                return true;
            }
            return false;
        }

        public boolean canReuseCache() {
            if (!this.mCacheDirty && this.mCachedTint == this.mTint && this.mCachedTintMode == this.mTintMode && this.mCachedAutoMirrored == this.mAutoMirrored && this.mCachedRootAlpha == this.mVPathRenderer.getRootAlpha()) {
                return true;
            }
            return false;
        }

        public void createCachedBitmapIfNeeded(int n, int n2) {
            if (this.mCachedBitmap != null && this.canReuseBitmap(n, n2)) {
                return;
            }
            this.mCachedBitmap = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
            this.mCacheDirty = true;
        }

        public void drawCachedBitmapWithRootAlpha(Canvas canvas, ColorFilter colorFilter, Rect rect) {
            colorFilter = this.getPaint(colorFilter);
            canvas.drawBitmap(this.mCachedBitmap, null, rect, (Paint)colorFilter);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public Paint getPaint(ColorFilter colorFilter) {
            if (!this.hasTranslucentRoot() && colorFilter == null) {
                return null;
            }
            if (this.mTempPaint == null) {
                this.mTempPaint = new Paint();
                this.mTempPaint.setFilterBitmap(true);
            }
            this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
            this.mTempPaint.setColorFilter(colorFilter);
            return this.mTempPaint;
        }

        public boolean hasTranslucentRoot() {
            if (this.mVPathRenderer.getRootAlpha() < 255) {
                return true;
            }
            return false;
        }

        public Drawable newDrawable() {
            return new VectorDrawableCompat(this);
        }

        public Drawable newDrawable(Resources resources) {
            return new VectorDrawableCompat(this);
        }

        public void updateCacheStates() {
            this.mCachedTint = this.mTint;
            this.mCachedTintMode = this.mTintMode;
            this.mCachedRootAlpha = this.mVPathRenderer.getRootAlpha();
            this.mCachedAutoMirrored = this.mAutoMirrored;
            this.mCacheDirty = false;
        }

        public void updateCachedBitmap(int n, int n2) {
            this.mCachedBitmap.eraseColor(0);
            Canvas canvas = new Canvas(this.mCachedBitmap);
            this.mVPathRenderer.draw(canvas, n, n2, null);
        }
    }

    @RequiresApi(value=24)
    private static class VectorDrawableDelegateState
    extends Drawable.ConstantState {
        private final Drawable.ConstantState mDelegateState;

        public VectorDrawableDelegateState(Drawable.ConstantState constantState) {
            this.mDelegateState = constantState;
        }

        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }

        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }

        public Drawable newDrawable() {
            VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable();
            return vectorDrawableCompat;
        }

        public Drawable newDrawable(Resources resources) {
            VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable(resources);
            return vectorDrawableCompat;
        }

        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = (VectorDrawable)this.mDelegateState.newDrawable(resources, theme);
            return vectorDrawableCompat;
        }
    }

}

