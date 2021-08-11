// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ObjectAnimator;
import android.view.ViewGroup;
import android.view.View;
import android.content.res.TypedArray;
import android.support.v4.content.res.TypedArrayUtils;
import org.xmlpull.v1.XmlPullParser;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Property;

public class ChangeTransform extends Transition
{
    private static final Property<PathAnimatorMatrix, float[]> NON_TRANSLATIONS_PROPERTY;
    private static final String PROPNAME_INTERMEDIATE_MATRIX = "android:changeTransform:intermediateMatrix";
    private static final String PROPNAME_INTERMEDIATE_PARENT_MATRIX = "android:changeTransform:intermediateParentMatrix";
    private static final String PROPNAME_MATRIX = "android:changeTransform:matrix";
    private static final String PROPNAME_PARENT = "android:changeTransform:parent";
    private static final String PROPNAME_PARENT_MATRIX = "android:changeTransform:parentMatrix";
    private static final String PROPNAME_TRANSFORMS = "android:changeTransform:transforms";
    private static final boolean SUPPORTS_VIEW_REMOVAL_SUPPRESSION;
    private static final Property<PathAnimatorMatrix, PointF> TRANSLATIONS_PROPERTY;
    private static final String[] sTransitionProperties;
    private boolean mReparent;
    private Matrix mTempMatrix;
    private boolean mUseOverlay;
    
    static {
        boolean supports_VIEW_REMOVAL_SUPPRESSION = false;
        sTransitionProperties = new String[] { "android:changeTransform:matrix", "android:changeTransform:transforms", "android:changeTransform:parentMatrix" };
        NON_TRANSLATIONS_PROPERTY = new Property<PathAnimatorMatrix, float[]>(float[].class, "nonTranslations") {
            public float[] get(final PathAnimatorMatrix pathAnimatorMatrix) {
                return null;
            }
            
            public void set(final PathAnimatorMatrix pathAnimatorMatrix, final float[] values) {
                pathAnimatorMatrix.setValues(values);
            }
        };
        TRANSLATIONS_PROPERTY = new Property<PathAnimatorMatrix, PointF>(PointF.class, "translations") {
            public PointF get(final PathAnimatorMatrix pathAnimatorMatrix) {
                return null;
            }
            
            public void set(final PathAnimatorMatrix pathAnimatorMatrix, final PointF translation) {
                pathAnimatorMatrix.setTranslation(translation);
            }
        };
        if (Build$VERSION.SDK_INT >= 21) {
            supports_VIEW_REMOVAL_SUPPRESSION = true;
        }
        SUPPORTS_VIEW_REMOVAL_SUPPRESSION = supports_VIEW_REMOVAL_SUPPRESSION;
    }
    
    public ChangeTransform() {
        this.mUseOverlay = true;
        this.mReparent = true;
        this.mTempMatrix = new Matrix();
    }
    
    public ChangeTransform(final Context context, final AttributeSet set) {
        super(context, set);
        this.mUseOverlay = true;
        this.mReparent = true;
        this.mTempMatrix = new Matrix();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, Styleable.CHANGE_TRANSFORM);
        this.mUseOverlay = TypedArrayUtils.getNamedBoolean(obtainStyledAttributes, (XmlPullParser)set, "reparentWithOverlay", 1, true);
        this.mReparent = TypedArrayUtils.getNamedBoolean(obtainStyledAttributes, (XmlPullParser)set, "reparent", 0, true);
        obtainStyledAttributes.recycle();
    }
    
    private void captureValues(final TransitionValues transitionValues) {
        final View view = transitionValues.view;
        if (view.getVisibility() == 8) {
            return;
        }
        transitionValues.values.put("android:changeTransform:parent", view.getParent());
        transitionValues.values.put("android:changeTransform:transforms", new Transforms(view));
        final Matrix matrix = view.getMatrix();
        Matrix matrix2;
        if (matrix != null && !matrix.isIdentity()) {
            matrix2 = new Matrix(matrix);
        }
        else {
            matrix2 = null;
        }
        transitionValues.values.put("android:changeTransform:matrix", matrix2);
        if (this.mReparent) {
            final Matrix matrix3 = new Matrix();
            final ViewGroup viewGroup = (ViewGroup)view.getParent();
            ViewUtils.transformMatrixToGlobal((View)viewGroup, matrix3);
            matrix3.preTranslate((float)(-viewGroup.getScrollX()), (float)(-viewGroup.getScrollY()));
            transitionValues.values.put("android:changeTransform:parentMatrix", matrix3);
            transitionValues.values.put("android:changeTransform:intermediateMatrix", view.getTag(R.id.transition_transform));
            transitionValues.values.put("android:changeTransform:intermediateParentMatrix", view.getTag(R.id.parent_matrix));
        }
    }
    
    private void createGhostView(final ViewGroup viewGroup, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        final View view = transitionValues2.view;
        final Matrix matrix = new Matrix((Matrix)transitionValues2.values.get("android:changeTransform:parentMatrix"));
        ViewUtils.transformMatrixToLocal((View)viewGroup, matrix);
        final GhostViewImpl addGhost = GhostViewUtils.addGhost(view, viewGroup, matrix);
        if (addGhost == null) {
            return;
        }
        addGhost.reserveEndViewTransition(transitionValues.values.get("android:changeTransform:parent"), transitionValues.view);
        Transition mParent;
        for (mParent = this; mParent.mParent != null; mParent = mParent.mParent) {}
        mParent.addListener((TransitionListener)new GhostListener(view, addGhost));
        if (ChangeTransform.SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            if (transitionValues.view != transitionValues2.view) {
                ViewUtils.setTransitionAlpha(transitionValues.view, 0.0f);
            }
            ViewUtils.setTransitionAlpha(view, 1.0f);
        }
    }
    
    private ObjectAnimator createTransformAnimator(final TransitionValues transitionValues, final TransitionValues transitionValues2, final boolean b) {
        Matrix identity_MATRIX = transitionValues.values.get("android:changeTransform:matrix");
        Matrix identity_MATRIX2 = transitionValues2.values.get("android:changeTransform:matrix");
        if (identity_MATRIX == null) {
            identity_MATRIX = MatrixUtils.IDENTITY_MATRIX;
        }
        if (identity_MATRIX2 == null) {
            identity_MATRIX2 = MatrixUtils.IDENTITY_MATRIX;
        }
        if (identity_MATRIX.equals((Object)identity_MATRIX2)) {
            return null;
        }
        final Transforms transforms = transitionValues2.values.get("android:changeTransform:transforms");
        final View view = transitionValues2.view;
        setIdentityTransforms(view);
        final float[] array = new float[9];
        identity_MATRIX.getValues(array);
        final float[] array2 = new float[9];
        identity_MATRIX2.getValues(array2);
        final PathAnimatorMatrix pathAnimatorMatrix = new PathAnimatorMatrix(view, array);
        final ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object)pathAnimatorMatrix, new PropertyValuesHolder[] { PropertyValuesHolder.ofObject((Property)ChangeTransform.NON_TRANSLATIONS_PROPERTY, (TypeEvaluator)new FloatArrayEvaluator(new float[9]), (Object[])new float[][] { array, array2 }), PropertyValuesHolderUtils.ofPointF(ChangeTransform.TRANSLATIONS_PROPERTY, this.getPathMotion().getPath(array[2], array[5], array2[2], array2[5])) });
        final AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
            private boolean mIsCanceled;
            private Matrix mTempMatrix = new Matrix();
            
            private void setCurrentMatrix(final Matrix matrix) {
                this.mTempMatrix.set(matrix);
                view.setTag(R.id.transition_transform, (Object)this.mTempMatrix);
                transforms.restore(view);
            }
            
            public void onAnimationCancel(final Animator animator) {
                this.mIsCanceled = true;
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (!this.mIsCanceled) {
                    if (b && ChangeTransform.this.mUseOverlay) {
                        this.setCurrentMatrix(identity_MATRIX2);
                    }
                    else {
                        view.setTag(R.id.transition_transform, (Object)null);
                        view.setTag(R.id.parent_matrix, (Object)null);
                    }
                }
                ViewUtils.setAnimationMatrix(view, null);
                transforms.restore(view);
            }
            
            public void onAnimationPause(final Animator animator) {
                this.setCurrentMatrix(pathAnimatorMatrix.getMatrix());
            }
            
            public void onAnimationResume(final Animator animator) {
                setIdentityTransforms(view);
            }
        };
        ofPropertyValuesHolder.addListener((Animator$AnimatorListener)animatorListenerAdapter);
        AnimatorUtils.addPauseListener((Animator)ofPropertyValuesHolder, animatorListenerAdapter);
        return ofPropertyValuesHolder;
    }
    
    private boolean parentsMatch(final ViewGroup viewGroup, final ViewGroup viewGroup2) {
        final boolean validTarget = this.isValidTarget((View)viewGroup);
        final boolean b = false;
        boolean b2 = false;
        if (!validTarget || !this.isValidTarget((View)viewGroup2)) {
            boolean b3 = b;
            if (viewGroup == viewGroup2) {
                b3 = true;
            }
            return b3;
        }
        final TransitionValues matchedTransitionValues = this.getMatchedTransitionValues((View)viewGroup, true);
        if (matchedTransitionValues != null) {
            if (viewGroup2 == matchedTransitionValues.view) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    private static void setIdentityTransforms(final View view) {
        setTransforms(view, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
    }
    
    private void setMatricesForParent(final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        final Matrix matrix = transitionValues2.values.get("android:changeTransform:parentMatrix");
        transitionValues2.view.setTag(R.id.parent_matrix, (Object)matrix);
        final Matrix mTempMatrix = this.mTempMatrix;
        mTempMatrix.reset();
        matrix.invert(mTempMatrix);
        Matrix matrix2 = transitionValues.values.get("android:changeTransform:matrix");
        if (matrix2 == null) {
            matrix2 = new Matrix();
            transitionValues.values.put("android:changeTransform:matrix", matrix2);
        }
        matrix2.postConcat((Matrix)transitionValues.values.get("android:changeTransform:parentMatrix"));
        matrix2.postConcat(mTempMatrix);
    }
    
    private static void setTransforms(final View view, final float translationX, final float translationY, final float n, final float scaleX, final float scaleY, final float rotationX, final float rotationY, final float rotation) {
        view.setTranslationX(translationX);
        view.setTranslationY(translationY);
        ViewCompat.setTranslationZ(view, n);
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);
        view.setRotationX(rotationX);
        view.setRotationY(rotationY);
        view.setRotation(rotation);
    }
    
    @Override
    public void captureEndValues(@NonNull final TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }
    
    @Override
    public void captureStartValues(@NonNull final TransitionValues transitionValues) {
        this.captureValues(transitionValues);
        if (!ChangeTransform.SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            ((ViewGroup)transitionValues.view.getParent()).startViewTransition(transitionValues.view);
        }
    }
    
    @Override
    public Animator createAnimator(@NonNull final ViewGroup viewGroup, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        if (transitionValues != null && transitionValues2 != null) {
            if (transitionValues.values.containsKey("android:changeTransform:parent")) {
                if (transitionValues2.values.containsKey("android:changeTransform:parent")) {
                    final ViewGroup viewGroup2 = transitionValues.values.get("android:changeTransform:parent");
                    final ViewGroup viewGroup3 = transitionValues2.values.get("android:changeTransform:parent");
                    final boolean b = this.mReparent && !this.parentsMatch(viewGroup2, viewGroup3);
                    final Matrix matrix = transitionValues.values.get("android:changeTransform:intermediateMatrix");
                    if (matrix != null) {
                        transitionValues.values.put("android:changeTransform:matrix", matrix);
                    }
                    final Matrix matrix2 = transitionValues.values.get("android:changeTransform:intermediateParentMatrix");
                    if (matrix2 != null) {
                        transitionValues.values.put("android:changeTransform:parentMatrix", matrix2);
                    }
                    if (b) {
                        this.setMatricesForParent(transitionValues, transitionValues2);
                    }
                    final ObjectAnimator transformAnimator = this.createTransformAnimator(transitionValues, transitionValues2, b);
                    if (b && transformAnimator != null && this.mUseOverlay) {
                        this.createGhostView(viewGroup, transitionValues, transitionValues2);
                        return (Animator)transformAnimator;
                    }
                    if (!ChangeTransform.SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
                        viewGroup2.endViewTransition(transitionValues.view);
                        return (Animator)transformAnimator;
                    }
                    return (Animator)transformAnimator;
                }
            }
        }
        return null;
    }
    
    public boolean getReparent() {
        return this.mReparent;
    }
    
    public boolean getReparentWithOverlay() {
        return this.mUseOverlay;
    }
    
    @Override
    public String[] getTransitionProperties() {
        return ChangeTransform.sTransitionProperties;
    }
    
    public void setReparent(final boolean mReparent) {
        this.mReparent = mReparent;
    }
    
    public void setReparentWithOverlay(final boolean mUseOverlay) {
        this.mUseOverlay = mUseOverlay;
    }
    
    private static class GhostListener extends TransitionListenerAdapter
    {
        private GhostViewImpl mGhostView;
        private View mView;
        
        GhostListener(final View mView, final GhostViewImpl mGhostView) {
            this.mView = mView;
            this.mGhostView = mGhostView;
        }
        
        @Override
        public void onTransitionEnd(@NonNull final Transition transition) {
            transition.removeListener((TransitionListener)this);
            GhostViewUtils.removeGhost(this.mView);
            this.mView.setTag(R.id.transition_transform, (Object)null);
            this.mView.setTag(R.id.parent_matrix, (Object)null);
        }
        
        @Override
        public void onTransitionPause(@NonNull final Transition transition) {
            this.mGhostView.setVisibility(4);
        }
        
        @Override
        public void onTransitionResume(@NonNull final Transition transition) {
            this.mGhostView.setVisibility(0);
        }
    }
    
    private static class PathAnimatorMatrix
    {
        private final Matrix mMatrix;
        private float mTranslationX;
        private float mTranslationY;
        private final float[] mValues;
        private final View mView;
        
        PathAnimatorMatrix(final View mView, final float[] array) {
            this.mMatrix = new Matrix();
            this.mView = mView;
            this.mValues = array.clone();
            final float[] mValues = this.mValues;
            this.mTranslationX = mValues[2];
            this.mTranslationY = mValues[5];
            this.setAnimationMatrix();
        }
        
        private void setAnimationMatrix() {
            final float[] mValues = this.mValues;
            mValues[2] = this.mTranslationX;
            mValues[5] = this.mTranslationY;
            this.mMatrix.setValues(mValues);
            ViewUtils.setAnimationMatrix(this.mView, this.mMatrix);
        }
        
        Matrix getMatrix() {
            return this.mMatrix;
        }
        
        void setTranslation(final PointF pointF) {
            this.mTranslationX = pointF.x;
            this.mTranslationY = pointF.y;
            this.setAnimationMatrix();
        }
        
        void setValues(final float[] array) {
            System.arraycopy(array, 0, this.mValues, 0, array.length);
            this.setAnimationMatrix();
        }
    }
    
    private static class Transforms
    {
        final float mRotationX;
        final float mRotationY;
        final float mRotationZ;
        final float mScaleX;
        final float mScaleY;
        final float mTranslationX;
        final float mTranslationY;
        final float mTranslationZ;
        
        Transforms(final View view) {
            this.mTranslationX = view.getTranslationX();
            this.mTranslationY = view.getTranslationY();
            this.mTranslationZ = ViewCompat.getTranslationZ(view);
            this.mScaleX = view.getScaleX();
            this.mScaleY = view.getScaleY();
            this.mRotationX = view.getRotationX();
            this.mRotationY = view.getRotationY();
            this.mRotationZ = view.getRotation();
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Transforms;
            final boolean b2 = false;
            if (!b) {
                return false;
            }
            final Transforms transforms = (Transforms)o;
            boolean b3 = b2;
            if (transforms.mTranslationX == this.mTranslationX) {
                b3 = b2;
                if (transforms.mTranslationY == this.mTranslationY) {
                    b3 = b2;
                    if (transforms.mTranslationZ == this.mTranslationZ) {
                        b3 = b2;
                        if (transforms.mScaleX == this.mScaleX) {
                            b3 = b2;
                            if (transforms.mScaleY == this.mScaleY) {
                                b3 = b2;
                                if (transforms.mRotationX == this.mRotationX) {
                                    b3 = b2;
                                    if (transforms.mRotationY == this.mRotationY) {
                                        b3 = b2;
                                        if (transforms.mRotationZ == this.mRotationZ) {
                                            b3 = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return b3;
        }
        
        @Override
        public int hashCode() {
            final float mTranslationX = this.mTranslationX;
            int floatToIntBits = 0;
            int floatToIntBits2;
            if (mTranslationX != 0.0f) {
                floatToIntBits2 = Float.floatToIntBits(mTranslationX);
            }
            else {
                floatToIntBits2 = 0;
            }
            final float mTranslationY = this.mTranslationY;
            int floatToIntBits3;
            if (mTranslationY != 0.0f) {
                floatToIntBits3 = Float.floatToIntBits(mTranslationY);
            }
            else {
                floatToIntBits3 = 0;
            }
            final float mTranslationZ = this.mTranslationZ;
            int floatToIntBits4;
            if (mTranslationZ != 0.0f) {
                floatToIntBits4 = Float.floatToIntBits(mTranslationZ);
            }
            else {
                floatToIntBits4 = 0;
            }
            final float mScaleX = this.mScaleX;
            int floatToIntBits5;
            if (mScaleX != 0.0f) {
                floatToIntBits5 = Float.floatToIntBits(mScaleX);
            }
            else {
                floatToIntBits5 = 0;
            }
            final float mScaleY = this.mScaleY;
            int floatToIntBits6;
            if (mScaleY != 0.0f) {
                floatToIntBits6 = Float.floatToIntBits(mScaleY);
            }
            else {
                floatToIntBits6 = 0;
            }
            final float mRotationX = this.mRotationX;
            int floatToIntBits7;
            if (mRotationX != 0.0f) {
                floatToIntBits7 = Float.floatToIntBits(mRotationX);
            }
            else {
                floatToIntBits7 = 0;
            }
            final float mRotationY = this.mRotationY;
            int floatToIntBits8;
            if (mRotationY != 0.0f) {
                floatToIntBits8 = Float.floatToIntBits(mRotationY);
            }
            else {
                floatToIntBits8 = 0;
            }
            final float mRotationZ = this.mRotationZ;
            if (mRotationZ != 0.0f) {
                floatToIntBits = Float.floatToIntBits(mRotationZ);
            }
            return ((((((floatToIntBits2 * 31 + floatToIntBits3) * 31 + floatToIntBits4) * 31 + floatToIntBits5) * 31 + floatToIntBits6) * 31 + floatToIntBits7) * 31 + floatToIntBits8) * 31 + floatToIntBits;
        }
        
        public void restore(final View view) {
            setTransforms(view, this.mTranslationX, this.mTranslationY, this.mTranslationZ, this.mScaleX, this.mScaleY, this.mRotationX, this.mRotationY, this.mRotationZ);
        }
    }
}
