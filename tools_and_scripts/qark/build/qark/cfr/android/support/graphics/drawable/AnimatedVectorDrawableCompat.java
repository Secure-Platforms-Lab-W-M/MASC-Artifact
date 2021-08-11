/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ArgbEvaluator
 *  android.animation.ObjectAnimator
 *  android.animation.TypeEvaluator
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.Region
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.Animatable2
 *  android.graphics.drawable.Animatable2$AnimationCallback
 *  android.graphics.drawable.AnimatedVectorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AndroidResources;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatorInflaterCompat;
import android.support.graphics.drawable.VectorDrawableCommon;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawableCompat
extends VectorDrawableCommon
implements Animatable2Compat {
    private static final String ANIMATED_VECTOR = "animated-vector";
    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
    private static final String LOGTAG = "AnimatedVDCompat";
    private static final String TARGET = "target";
    private AnimatedVectorDrawableCompatState mAnimatedVectorState;
    private ArrayList<Animatable2Compat.AnimationCallback> mAnimationCallbacks = null;
    private Animator.AnimatorListener mAnimatorListener = null;
    private ArgbEvaluator mArgbEvaluator = null;
    AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
    final Drawable.Callback mCallback;
    private Context mContext;

    AnimatedVectorDrawableCompat() {
        this(null, null, null);
    }

    private AnimatedVectorDrawableCompat(@Nullable Context context) {
        this(context, null, null);
    }

    private AnimatedVectorDrawableCompat(@Nullable Context context, @Nullable AnimatedVectorDrawableCompatState animatedVectorDrawableCompatState, @Nullable Resources resources) {
        this.mCallback = new Drawable.Callback(){

            public void invalidateDrawable(Drawable drawable2) {
                AnimatedVectorDrawableCompat.this.invalidateSelf();
            }

            public void scheduleDrawable(Drawable drawable2, Runnable runnable, long l) {
                AnimatedVectorDrawableCompat.this.scheduleSelf(runnable, l);
            }

            public void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
                AnimatedVectorDrawableCompat.this.unscheduleSelf(runnable);
            }
        };
        this.mContext = context;
        if (animatedVectorDrawableCompatState != null) {
            this.mAnimatedVectorState = animatedVectorDrawableCompatState;
            return;
        }
        this.mAnimatedVectorState = new AnimatedVectorDrawableCompatState(context, animatedVectorDrawableCompatState, this.mCallback, resources);
    }

    public static void clearAnimationCallbacks(Drawable drawable2) {
        if (drawable2 != null) {
            if (!(drawable2 instanceof Animatable)) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 24) {
                ((AnimatedVectorDrawable)drawable2).clearAnimationCallbacks();
                return;
            }
            ((AnimatedVectorDrawableCompat)drawable2).clearAnimationCallbacks();
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Nullable
    public static AnimatedVectorDrawableCompat create(@NonNull Context context, @DrawableRes int n) {
        if (Build.VERSION.SDK_INT >= 24) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(context);
            animatedVectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(context.getResources(), n, context.getTheme());
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            animatedVectorDrawableCompat.mCachedConstantStateDelegate = new AnimatedVectorDrawableDelegateState(animatedVectorDrawableCompat.mDelegateDrawable.getConstantState());
            return animatedVectorDrawableCompat;
        }
        Resources resources = context.getResources();
        resources = resources.getXml(n);
        AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)resources);
        while ((n = resources.next()) != 2 && n != 1) {
        }
        if (n != 2) throw new XmlPullParserException("No start tag found");
        try {
            return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), (XmlPullParser)resources, attributeSet, context.getTheme());
        }
        catch (IOException iOException) {
            Log.e((String)"AnimatedVDCompat", (String)"parser error", (Throwable)iOException);
            return null;
        }
        catch (XmlPullParserException xmlPullParserException) {
            Log.e((String)"AnimatedVDCompat", (String)"parser error", (Throwable)xmlPullParserException);
        }
        return null;
    }

    public static AnimatedVectorDrawableCompat createFromXmlInner(Context object, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        object = new AnimatedVectorDrawableCompat((Context)object);
        object.inflate(resources, xmlPullParser, attributeSet, theme);
        return object;
    }

    public static void registerAnimationCallback(Drawable drawable2, Animatable2Compat.AnimationCallback animationCallback) {
        if (drawable2 != null) {
            if (animationCallback == null) {
                return;
            }
            if (!(drawable2 instanceof Animatable)) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 24) {
                AnimatedVectorDrawableCompat.registerPlatformCallback((AnimatedVectorDrawable)drawable2, animationCallback);
                return;
            }
            ((AnimatedVectorDrawableCompat)drawable2).registerAnimationCallback(animationCallback);
            return;
        }
    }

    @RequiresApi(value=23)
    private static void registerPlatformCallback(@NonNull AnimatedVectorDrawable animatedVectorDrawable, @NonNull Animatable2Compat.AnimationCallback animationCallback) {
        animatedVectorDrawable.registerAnimationCallback(animationCallback.getPlatformCallback());
    }

    private void removeAnimatorSetListener() {
        if (this.mAnimatorListener != null) {
            this.mAnimatedVectorState.mAnimatorSet.removeListener(this.mAnimatorListener);
            this.mAnimatorListener = null;
            return;
        }
    }

    private void setupAnimatorsForTarget(String string2, Animator animator2) {
        animator2.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(string2));
        if (Build.VERSION.SDK_INT < 21) {
            this.setupColorAnimator(animator2);
        }
        if (this.mAnimatedVectorState.mAnimators == null) {
            this.mAnimatedVectorState.mAnimators = new ArrayList();
            this.mAnimatedVectorState.mTargetNameMap = new ArrayMap();
        }
        this.mAnimatedVectorState.mAnimators.add(animator2);
        this.mAnimatedVectorState.mTargetNameMap.put(animator2, string2);
    }

    private void setupColorAnimator(Animator animator2) {
        Object object;
        if (animator2 instanceof AnimatorSet && (object = ((AnimatorSet)animator2).getChildAnimations()) != null) {
            for (int i = 0; i < object.size(); ++i) {
                this.setupColorAnimator((Animator)object.get(i));
            }
        }
        if (animator2 instanceof ObjectAnimator) {
            object = (animator2 = (ObjectAnimator)animator2).getPropertyName();
            if (!"fillColor".equals(object) && !"strokeColor".equals(object)) {
                return;
            }
            if (this.mArgbEvaluator == null) {
                this.mArgbEvaluator = new ArgbEvaluator();
            }
            animator2.setEvaluator((TypeEvaluator)this.mArgbEvaluator);
            return;
        }
    }

    public static boolean unregisterAnimationCallback(Drawable drawable2, Animatable2Compat.AnimationCallback animationCallback) {
        if (drawable2 != null) {
            if (animationCallback == null) {
                return false;
            }
            if (!(drawable2 instanceof Animatable)) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 24) {
                return AnimatedVectorDrawableCompat.unregisterPlatformCallback((AnimatedVectorDrawable)drawable2, animationCallback);
            }
            return ((AnimatedVectorDrawableCompat)drawable2).unregisterAnimationCallback(animationCallback);
        }
        return false;
    }

    @RequiresApi(value=23)
    private static boolean unregisterPlatformCallback(AnimatedVectorDrawable animatedVectorDrawable, Animatable2Compat.AnimationCallback animationCallback) {
        return animatedVectorDrawable.unregisterAnimationCallback(animationCallback.getPlatformCallback());
    }

    @Override
    public void applyTheme(Resources.Theme theme) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.applyTheme(this.mDelegateDrawable, theme);
            return;
        }
    }

    public boolean canApplyTheme() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.canApplyTheme(this.mDelegateDrawable);
        }
        return false;
    }

    @Override
    public void clearAnimationCallbacks() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).clearAnimationCallbacks();
            return;
        }
        this.removeAnimatorSetListener();
        ArrayList<Animatable2Compat.AnimationCallback> arrayList = this.mAnimationCallbacks;
        if (arrayList == null) {
            return;
        }
        arrayList.clear();
    }

    public void draw(Canvas canvas) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.draw(canvas);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.draw(canvas);
        if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            this.invalidateSelf();
            return;
        }
    }

    public int getAlpha() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getAlpha(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    public int getChangingConfigurations() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations;
    }

    public Drawable.ConstantState getConstantState() {
        if (this.mDelegateDrawable != null && Build.VERSION.SDK_INT >= 24) {
            return new AnimatedVectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
        }
        return null;
    }

    public int getIntrinsicHeight() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicHeight();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicWidth();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    public int getOpacity() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getOpacity();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getOpacity();
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        this.inflate(resources, xmlPullParser, attributeSet, null);
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.inflate(this.mDelegateDrawable, resources, xmlPullParser, attributeSet, theme);
            return;
        }
        int n = xmlPullParser.getEventType();
        int n2 = xmlPullParser.getDepth();
        while (n != 1 && (xmlPullParser.getDepth() >= n2 + 1 || n != 3)) {
            if (n == 2) {
                Object object;
                String string2 = xmlPullParser.getName();
                if ("animated-vector".equals(string2)) {
                    string2 = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE);
                    n = string2.getResourceId(0, 0);
                    if (n != 0) {
                        object = VectorDrawableCompat.create(resources, n, theme);
                        object.setAllowCaching(false);
                        object.setCallback(this.mCallback);
                        if (this.mAnimatedVectorState.mVectorDrawable != null) {
                            this.mAnimatedVectorState.mVectorDrawable.setCallback(null);
                        }
                        this.mAnimatedVectorState.mVectorDrawable = object;
                    }
                    string2.recycle();
                } else if ("target".equals(string2)) {
                    string2 = resources.obtainAttributes(attributeSet, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE_TARGET);
                    object = string2.getString(0);
                    n = string2.getResourceId(1, 0);
                    if (n != 0) {
                        Context context = this.mContext;
                        if (context != null) {
                            this.setupAnimatorsForTarget((String)object, AnimatorInflaterCompat.loadAnimator(context, n));
                        } else {
                            string2.recycle();
                            throw new IllegalStateException("Context can't be null when inflating animators");
                        }
                    }
                    string2.recycle();
                }
            }
            n = xmlPullParser.next();
        }
        this.mAnimatedVectorState.setupAnimatorSet();
    }

    public boolean isAutoMirrored() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
    }

    public boolean isRunning() {
        if (this.mDelegateDrawable != null) {
            return ((AnimatedVectorDrawable)this.mDelegateDrawable).isRunning();
        }
        return this.mAnimatedVectorState.mAnimatorSet.isRunning();
    }

    public boolean isStateful() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.isStateful();
        }
        return this.mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    public Drawable mutate() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.mutate();
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
        this.mAnimatedVectorState.mVectorDrawable.setBounds(rect);
    }

    @Override
    protected boolean onLevelChange(int n) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setLevel(n);
        }
        return this.mAnimatedVectorState.mVectorDrawable.setLevel(n);
    }

    protected boolean onStateChange(int[] arrn) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState(arrn);
        }
        return this.mAnimatedVectorState.mVectorDrawable.setState(arrn);
    }

    @Override
    public void registerAnimationCallback(@NonNull Animatable2Compat.AnimationCallback animationCallback) {
        if (this.mDelegateDrawable != null) {
            AnimatedVectorDrawableCompat.registerPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, animationCallback);
            return;
        }
        if (animationCallback == null) {
            return;
        }
        if (this.mAnimationCallbacks == null) {
            this.mAnimationCallbacks = new ArrayList();
        }
        if (this.mAnimationCallbacks.contains(animationCallback)) {
            return;
        }
        this.mAnimationCallbacks.add(animationCallback);
        if (this.mAnimatorListener == null) {
            this.mAnimatorListener = new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator object) {
                    object = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                    int n = object.size();
                    for (int i = 0; i < n; ++i) {
                        ((Animatable2Compat.AnimationCallback)object.get(i)).onAnimationEnd(AnimatedVectorDrawableCompat.this);
                    }
                }

                public void onAnimationStart(Animator object) {
                    object = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                    int n = object.size();
                    for (int i = 0; i < n; ++i) {
                        ((Animatable2Compat.AnimationCallback)object.get(i)).onAnimationStart(AnimatedVectorDrawableCompat.this);
                    }
                }
            };
        }
        this.mAnimatedVectorState.mAnimatorSet.addListener(this.mAnimatorListener);
    }

    public void setAlpha(int n) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setAlpha(n);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setAlpha(n);
    }

    public void setAutoMirrored(boolean bl) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setAutoMirrored(this.mDelegateDrawable, bl);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(bl);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
    }

    @Override
    public void setTint(int n) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTint(this.mDelegateDrawable, n);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTint(n);
    }

    @Override
    public void setTintList(ColorStateList colorStateList) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintList(this.mDelegateDrawable, colorStateList);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTintList(colorStateList);
    }

    @Override
    public void setTintMode(PorterDuff.Mode mode) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintMode(this.mDelegateDrawable, mode);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTintMode(mode);
    }

    public boolean setVisible(boolean bl, boolean bl2) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setVisible(bl, bl2);
        }
        this.mAnimatedVectorState.mVectorDrawable.setVisible(bl, bl2);
        return super.setVisible(bl, bl2);
    }

    public void start() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).start();
            return;
        }
        if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            return;
        }
        this.mAnimatedVectorState.mAnimatorSet.start();
        this.invalidateSelf();
    }

    public void stop() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).stop();
            return;
        }
        this.mAnimatedVectorState.mAnimatorSet.end();
    }

    @Override
    public boolean unregisterAnimationCallback(@NonNull Animatable2Compat.AnimationCallback animationCallback) {
        ArrayList<Animatable2Compat.AnimationCallback> arrayList;
        if (this.mDelegateDrawable != null) {
            AnimatedVectorDrawableCompat.unregisterPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, animationCallback);
        }
        if ((arrayList = this.mAnimationCallbacks) != null && animationCallback != null) {
            boolean bl = arrayList.remove(animationCallback);
            if (this.mAnimationCallbacks.size() == 0) {
                this.removeAnimatorSetListener();
                return bl;
            }
            return bl;
        }
        return false;
    }

    private static class AnimatedVectorDrawableCompatState
    extends Drawable.ConstantState {
        AnimatorSet mAnimatorSet;
        private ArrayList<Animator> mAnimators;
        int mChangingConfigurations;
        ArrayMap<Animator, String> mTargetNameMap;
        VectorDrawableCompat mVectorDrawable;

        public AnimatedVectorDrawableCompatState(Context object, AnimatedVectorDrawableCompatState animatedVectorDrawableCompatState, Drawable.Callback object2, Resources resources) {
            if (animatedVectorDrawableCompatState != null) {
                this.mChangingConfigurations = animatedVectorDrawableCompatState.mChangingConfigurations;
                object = animatedVectorDrawableCompatState.mVectorDrawable;
                if (object != null) {
                    object = object.getConstantState();
                    this.mVectorDrawable = resources != null ? (VectorDrawableCompat)object.newDrawable(resources) : (VectorDrawableCompat)object.newDrawable();
                    this.mVectorDrawable = (VectorDrawableCompat)this.mVectorDrawable.mutate();
                    this.mVectorDrawable.setCallback((Drawable.Callback)object2);
                    this.mVectorDrawable.setBounds(animatedVectorDrawableCompatState.mVectorDrawable.getBounds());
                    this.mVectorDrawable.setAllowCaching(false);
                }
                if ((object = animatedVectorDrawableCompatState.mAnimators) != null) {
                    int n = object.size();
                    this.mAnimators = new ArrayList(n);
                    this.mTargetNameMap = new ArrayMap(n);
                    for (int i = 0; i < n; ++i) {
                        object2 = animatedVectorDrawableCompatState.mAnimators.get(i);
                        object = object2.clone();
                        object2 = animatedVectorDrawableCompatState.mTargetNameMap.get(object2);
                        object.setTarget(this.mVectorDrawable.getTargetByName((String)object2));
                        this.mAnimators.add((Animator)object);
                        this.mTargetNameMap.put((Animator)object, (String)object2);
                    }
                    this.setupAnimatorSet();
                    return;
                }
                return;
            }
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public Drawable newDrawable() {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        public Drawable newDrawable(Resources resources) {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        public void setupAnimatorSet() {
            if (this.mAnimatorSet == null) {
                this.mAnimatorSet = new AnimatorSet();
            }
            this.mAnimatorSet.playTogether(this.mAnimators);
        }
    }

    @RequiresApi(value=24)
    private static class AnimatedVectorDrawableDelegateState
    extends Drawable.ConstantState {
        private final Drawable.ConstantState mDelegateState;

        public AnimatedVectorDrawableDelegateState(Drawable.ConstantState constantState) {
            this.mDelegateState = constantState;
        }

        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }

        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }

        public Drawable newDrawable() {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable();
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }

        public Drawable newDrawable(Resources resources) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources);
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }

        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources, theme);
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }
    }

}

