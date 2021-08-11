// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import android.graphics.Rect;
import android.util.SparseIntArray;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.InflateException;
import java.util.StringTokenizer;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.LongSparseArray;
import android.util.SparseArray;
import android.widget.ListView;
import android.support.v4.view.ViewCompat;
import android.content.res.TypedArray;
import android.view.animation.AnimationUtils;
import org.xmlpull.v1.XmlPullParser;
import android.support.v4.content.res.TypedArrayUtils;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;
import android.animation.TimeInterpolator;
import java.util.ArrayList;
import android.animation.Animator;
import android.support.v4.util.ArrayMap;

public abstract class Transition implements Cloneable
{
    static final boolean DBG = false;
    private static final int[] DEFAULT_MATCH_ORDER;
    private static final String LOG_TAG = "Transition";
    private static final int MATCH_FIRST = 1;
    public static final int MATCH_ID = 3;
    private static final String MATCH_ID_STR = "id";
    public static final int MATCH_INSTANCE = 1;
    private static final String MATCH_INSTANCE_STR = "instance";
    public static final int MATCH_ITEM_ID = 4;
    private static final String MATCH_ITEM_ID_STR = "itemId";
    private static final int MATCH_LAST = 4;
    public static final int MATCH_NAME = 2;
    private static final String MATCH_NAME_STR = "name";
    private static final PathMotion STRAIGHT_PATH_MOTION;
    private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators;
    private ArrayList<Animator> mAnimators;
    boolean mCanRemoveViews;
    private ArrayList<Animator> mCurrentAnimators;
    long mDuration;
    private TransitionValuesMaps mEndValues;
    private ArrayList<TransitionValues> mEndValuesList;
    private boolean mEnded;
    private EpicenterCallback mEpicenterCallback;
    private TimeInterpolator mInterpolator;
    private ArrayList<TransitionListener> mListeners;
    private int[] mMatchOrder;
    private String mName;
    private ArrayMap<String, String> mNameOverrides;
    private int mNumInstances;
    TransitionSet mParent;
    private PathMotion mPathMotion;
    private boolean mPaused;
    TransitionPropagation mPropagation;
    private ViewGroup mSceneRoot;
    private long mStartDelay;
    private TransitionValuesMaps mStartValues;
    private ArrayList<TransitionValues> mStartValuesList;
    private ArrayList<View> mTargetChildExcludes;
    private ArrayList<View> mTargetExcludes;
    private ArrayList<Integer> mTargetIdChildExcludes;
    private ArrayList<Integer> mTargetIdExcludes;
    ArrayList<Integer> mTargetIds;
    private ArrayList<String> mTargetNameExcludes;
    private ArrayList<String> mTargetNames;
    private ArrayList<Class> mTargetTypeChildExcludes;
    private ArrayList<Class> mTargetTypeExcludes;
    private ArrayList<Class> mTargetTypes;
    ArrayList<View> mTargets;
    
    static {
        DEFAULT_MATCH_ORDER = new int[] { 2, 1, 3, 4 };
        STRAIGHT_PATH_MOTION = new PathMotion() {
            @Override
            public Path getPath(final float n, final float n2, final float n3, final float n4) {
                final Path path = new Path();
                path.moveTo(n, n2);
                path.lineTo(n3, n4);
                return path;
            }
        };
        Transition.sRunningAnimators = new ThreadLocal<ArrayMap<Animator, AnimationInfo>>();
    }
    
    public Transition() {
        this.mName = this.getClass().getName();
        this.mStartDelay = -1L;
        this.mDuration = -1L;
        this.mInterpolator = null;
        this.mTargetIds = new ArrayList<Integer>();
        this.mTargets = new ArrayList<View>();
        this.mTargetNames = null;
        this.mTargetTypes = null;
        this.mTargetIdExcludes = null;
        this.mTargetExcludes = null;
        this.mTargetTypeExcludes = null;
        this.mTargetNameExcludes = null;
        this.mTargetIdChildExcludes = null;
        this.mTargetChildExcludes = null;
        this.mTargetTypeChildExcludes = null;
        this.mStartValues = new TransitionValuesMaps();
        this.mEndValues = new TransitionValuesMaps();
        this.mParent = null;
        this.mMatchOrder = Transition.DEFAULT_MATCH_ORDER;
        this.mSceneRoot = null;
        this.mCanRemoveViews = false;
        this.mCurrentAnimators = new ArrayList<Animator>();
        this.mNumInstances = 0;
        this.mPaused = false;
        this.mEnded = false;
        this.mListeners = null;
        this.mAnimators = new ArrayList<Animator>();
        this.mPathMotion = Transition.STRAIGHT_PATH_MOTION;
    }
    
    public Transition(final Context context, final AttributeSet set) {
        this.mName = this.getClass().getName();
        this.mStartDelay = -1L;
        this.mDuration = -1L;
        this.mInterpolator = null;
        this.mTargetIds = new ArrayList<Integer>();
        this.mTargets = new ArrayList<View>();
        this.mTargetNames = null;
        this.mTargetTypes = null;
        this.mTargetIdExcludes = null;
        this.mTargetExcludes = null;
        this.mTargetTypeExcludes = null;
        this.mTargetNameExcludes = null;
        this.mTargetIdChildExcludes = null;
        this.mTargetChildExcludes = null;
        this.mTargetTypeChildExcludes = null;
        this.mStartValues = new TransitionValuesMaps();
        this.mEndValues = new TransitionValuesMaps();
        this.mParent = null;
        this.mMatchOrder = Transition.DEFAULT_MATCH_ORDER;
        this.mSceneRoot = null;
        this.mCanRemoveViews = false;
        this.mCurrentAnimators = new ArrayList<Animator>();
        this.mNumInstances = 0;
        this.mPaused = false;
        this.mEnded = false;
        this.mListeners = null;
        this.mAnimators = new ArrayList<Animator>();
        this.mPathMotion = Transition.STRAIGHT_PATH_MOTION;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, Styleable.TRANSITION);
        final XmlResourceParser xmlResourceParser = (XmlResourceParser)set;
        final long duration = TypedArrayUtils.getNamedInt(obtainStyledAttributes, (XmlPullParser)xmlResourceParser, "duration", 1, -1);
        if (duration >= 0L) {
            this.setDuration(duration);
        }
        final long startDelay = TypedArrayUtils.getNamedInt(obtainStyledAttributes, (XmlPullParser)xmlResourceParser, "startDelay", 2, -1);
        if (startDelay > 0L) {
            this.setStartDelay(startDelay);
        }
        final int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, (XmlPullParser)xmlResourceParser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            this.setInterpolator((TimeInterpolator)AnimationUtils.loadInterpolator(context, namedResourceId));
        }
        final String namedString = TypedArrayUtils.getNamedString(obtainStyledAttributes, (XmlPullParser)xmlResourceParser, "matchOrder", 3);
        if (namedString != null) {
            this.setMatchOrder(parseMatchOrder(namedString));
        }
        obtainStyledAttributes.recycle();
    }
    
    private void addUnmatched(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2) {
        for (int i = 0; i < arrayMap.size(); ++i) {
            final TransitionValues transitionValues = arrayMap.valueAt(i);
            if (this.isValidTarget(transitionValues.view)) {
                this.mStartValuesList.add(transitionValues);
                this.mEndValuesList.add(null);
            }
        }
        for (int j = 0; j < arrayMap2.size(); ++j) {
            final TransitionValues transitionValues2 = arrayMap2.valueAt(j);
            if (this.isValidTarget(transitionValues2.view)) {
                this.mEndValuesList.add(transitionValues2);
                this.mStartValuesList.add(null);
            }
        }
    }
    
    private static void addViewValues(final TransitionValuesMaps transitionValuesMaps, View view, final TransitionValues transitionValues) {
        transitionValuesMaps.mViewValues.put(view, transitionValues);
        final int id = view.getId();
        if (id >= 0) {
            if (transitionValuesMaps.mIdValues.indexOfKey(id) >= 0) {
                transitionValuesMaps.mIdValues.put(id, (Object)null);
            }
            else {
                transitionValuesMaps.mIdValues.put(id, (Object)view);
            }
        }
        final String transitionName = ViewCompat.getTransitionName(view);
        if (transitionName != null) {
            if (transitionValuesMaps.mNameValues.containsKey(transitionName)) {
                transitionValuesMaps.mNameValues.put(transitionName, null);
            }
            else {
                transitionValuesMaps.mNameValues.put(transitionName, view);
            }
        }
        if (!(view.getParent() instanceof ListView)) {
            return;
        }
        final ListView listView = (ListView)view.getParent();
        if (!listView.getAdapter().hasStableIds()) {
            return;
        }
        final long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
        if (transitionValuesMaps.mItemIdValues.indexOfKey(itemIdAtPosition) >= 0) {
            view = transitionValuesMaps.mItemIdValues.get(itemIdAtPosition);
            if (view != null) {
                ViewCompat.setHasTransientState(view, false);
                transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, null);
            }
            return;
        }
        ViewCompat.setHasTransientState(view, true);
        transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, view);
    }
    
    private static boolean alreadyContains(final int[] array, final int n) {
        final int n2 = array[n];
        for (int i = 0; i < n; ++i) {
            if (array[i] == n2) {
                return true;
            }
        }
        return false;
    }
    
    private void captureHierarchy(final View view, final boolean b) {
        if (view == null) {
            return;
        }
        final int id = view.getId();
        final ArrayList<Integer> mTargetIdExcludes = this.mTargetIdExcludes;
        if (mTargetIdExcludes != null && mTargetIdExcludes.contains(id)) {
            return;
        }
        final ArrayList<View> mTargetExcludes = this.mTargetExcludes;
        if (mTargetExcludes != null && mTargetExcludes.contains(view)) {
            return;
        }
        final ArrayList<Class> mTargetTypeExcludes = this.mTargetTypeExcludes;
        if (mTargetTypeExcludes != null) {
            for (int size = mTargetTypeExcludes.size(), i = 0; i < size; ++i) {
                if (this.mTargetTypeExcludes.get(i).isInstance(view)) {
                    return;
                }
            }
        }
        if (view.getParent() instanceof ViewGroup) {
            final TransitionValues transitionValues = new TransitionValues();
            transitionValues.view = view;
            if (b) {
                this.captureStartValues(transitionValues);
            }
            else {
                this.captureEndValues(transitionValues);
            }
            transitionValues.mTargetedTransitions.add(this);
            this.capturePropagationValues(transitionValues);
            if (b) {
                addViewValues(this.mStartValues, view, transitionValues);
            }
            else {
                addViewValues(this.mEndValues, view, transitionValues);
            }
        }
        if (!(view instanceof ViewGroup)) {
            return;
        }
        final ArrayList<Integer> mTargetIdChildExcludes = this.mTargetIdChildExcludes;
        if (mTargetIdChildExcludes != null && mTargetIdChildExcludes.contains(id)) {
            return;
        }
        final ArrayList<View> mTargetChildExcludes = this.mTargetChildExcludes;
        if (mTargetChildExcludes != null && mTargetChildExcludes.contains(view)) {
            return;
        }
        final ArrayList<Class> mTargetTypeChildExcludes = this.mTargetTypeChildExcludes;
        if (mTargetTypeChildExcludes != null) {
            for (int size2 = mTargetTypeChildExcludes.size(), j = 0; j < size2; ++j) {
                if (this.mTargetTypeChildExcludes.get(j).isInstance(view)) {
                    return;
                }
            }
        }
        final ViewGroup viewGroup = (ViewGroup)view;
        for (int k = 0; k < viewGroup.getChildCount(); ++k) {
            this.captureHierarchy(viewGroup.getChildAt(k), b);
        }
    }
    
    private ArrayList<Integer> excludeId(final ArrayList<Integer> list, final int n, final boolean b) {
        if (n <= 0) {
            return list;
        }
        if (b) {
            return ArrayListManager.add(list, n);
        }
        return ArrayListManager.remove(list, n);
    }
    
    private static <T> ArrayList<T> excludeObject(final ArrayList<T> list, final T t, final boolean b) {
        if (t == null) {
            return list;
        }
        if (b) {
            return ArrayListManager.add(list, t);
        }
        return ArrayListManager.remove(list, t);
    }
    
    private ArrayList<Class> excludeType(final ArrayList<Class> list, final Class clazz, final boolean b) {
        if (clazz == null) {
            return list;
        }
        if (b) {
            return (ArrayList<Class>)ArrayListManager.add(list, clazz);
        }
        return (ArrayList<Class>)ArrayListManager.remove(list, clazz);
    }
    
    private ArrayList<View> excludeView(final ArrayList<View> list, final View view, final boolean b) {
        if (view == null) {
            return list;
        }
        if (b) {
            return ArrayListManager.add(list, view);
        }
        return ArrayListManager.remove(list, view);
    }
    
    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        final ArrayMap<Animator, AnimationInfo> arrayMap = Transition.sRunningAnimators.get();
        if (arrayMap == null) {
            final ArrayMap<Animator, AnimationInfo> arrayMap2 = new ArrayMap<Animator, AnimationInfo>();
            Transition.sRunningAnimators.set(arrayMap2);
            return arrayMap2;
        }
        return arrayMap;
    }
    
    private static boolean isValidMatch(final int n) {
        return n >= 1 && n <= 4;
    }
    
    private static boolean isValueChanged(final TransitionValues transitionValues, final TransitionValues transitionValues2, final String s) {
        final Object value = transitionValues.values.get(s);
        final Object value2 = transitionValues2.values.get(s);
        return (value != null || value2 != null) && (value == null || value2 == null || (value.equals(value2) ^ true));
    }
    
    private void matchIds(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2, final SparseArray<View> sparseArray, final SparseArray<View> sparseArray2) {
        for (int size = sparseArray.size(), i = 0; i < size; ++i) {
            final View view = (View)sparseArray.valueAt(i);
            if (view != null && this.isValidTarget(view)) {
                final View view2 = (View)sparseArray2.get(sparseArray.keyAt(i));
                if (view2 != null && this.isValidTarget(view2)) {
                    final TransitionValues transitionValues = arrayMap.get(view);
                    final TransitionValues transitionValues2 = arrayMap2.get(view2);
                    if (transitionValues != null && transitionValues2 != null) {
                        this.mStartValuesList.add(transitionValues);
                        this.mEndValuesList.add(transitionValues2);
                        arrayMap.remove(view);
                        arrayMap2.remove(view2);
                    }
                }
            }
        }
    }
    
    private void matchInstances(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            final View view = arrayMap.keyAt(i);
            if (view != null && this.isValidTarget(view)) {
                final TransitionValues transitionValues = arrayMap2.remove(view);
                if (transitionValues != null && transitionValues.view != null && this.isValidTarget(transitionValues.view)) {
                    this.mStartValuesList.add(arrayMap.removeAt(i));
                    this.mEndValuesList.add(transitionValues);
                }
            }
        }
    }
    
    private void matchItemIds(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2, final LongSparseArray<View> longSparseArray, final LongSparseArray<View> longSparseArray2) {
        for (int size = longSparseArray.size(), i = 0; i < size; ++i) {
            final View view = longSparseArray.valueAt(i);
            if (view != null && this.isValidTarget(view)) {
                final View view2 = longSparseArray2.get(longSparseArray.keyAt(i));
                if (view2 != null && this.isValidTarget(view2)) {
                    final TransitionValues transitionValues = arrayMap.get(view);
                    final TransitionValues transitionValues2 = arrayMap2.get(view2);
                    if (transitionValues != null && transitionValues2 != null) {
                        this.mStartValuesList.add(transitionValues);
                        this.mEndValuesList.add(transitionValues2);
                        arrayMap.remove(view);
                        arrayMap2.remove(view2);
                    }
                }
            }
        }
    }
    
    private void matchNames(final ArrayMap<View, TransitionValues> arrayMap, final ArrayMap<View, TransitionValues> arrayMap2, final ArrayMap<String, View> arrayMap3, final ArrayMap<String, View> arrayMap4) {
        for (int size = arrayMap3.size(), i = 0; i < size; ++i) {
            final View view = arrayMap3.valueAt(i);
            if (view != null && this.isValidTarget(view)) {
                final View view2 = arrayMap4.get(arrayMap3.keyAt(i));
                if (view2 != null && this.isValidTarget(view2)) {
                    final TransitionValues transitionValues = arrayMap.get(view);
                    final TransitionValues transitionValues2 = arrayMap2.get(view2);
                    if (transitionValues != null && transitionValues2 != null) {
                        this.mStartValuesList.add(transitionValues);
                        this.mEndValuesList.add(transitionValues2);
                        arrayMap.remove(view);
                        arrayMap2.remove(view2);
                    }
                }
            }
        }
    }
    
    private void matchStartAndEnd(final TransitionValuesMaps transitionValuesMaps, final TransitionValuesMaps transitionValuesMaps2) {
        final ArrayMap<View, TransitionValues> arrayMap = new ArrayMap<View, TransitionValues>(transitionValuesMaps.mViewValues);
        final ArrayMap<View, TransitionValues> arrayMap2 = new ArrayMap<View, TransitionValues>(transitionValuesMaps2.mViewValues);
        int n = 0;
        while (true) {
            final int[] mMatchOrder = this.mMatchOrder;
            if (n >= mMatchOrder.length) {
                break;
            }
            switch (mMatchOrder[n]) {
                case 4: {
                    this.matchItemIds(arrayMap, arrayMap2, transitionValuesMaps.mItemIdValues, transitionValuesMaps2.mItemIdValues);
                    break;
                }
                case 3: {
                    this.matchIds(arrayMap, arrayMap2, transitionValuesMaps.mIdValues, transitionValuesMaps2.mIdValues);
                    break;
                }
                case 2: {
                    this.matchNames(arrayMap, arrayMap2, transitionValuesMaps.mNameValues, transitionValuesMaps2.mNameValues);
                    break;
                }
                case 1: {
                    this.matchInstances(arrayMap, arrayMap2);
                    break;
                }
            }
            ++n;
        }
        this.addUnmatched(arrayMap, arrayMap2);
    }
    
    private static int[] parseMatchOrder(final String s) {
        final StringTokenizer stringTokenizer = new StringTokenizer(s, ",");
        int[] array = new int[stringTokenizer.countTokens()];
        int n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            final String trim = stringTokenizer.nextToken().trim();
            if ("id".equalsIgnoreCase(trim)) {
                array[n] = 3;
            }
            else if ("instance".equalsIgnoreCase(trim)) {
                array[n] = 1;
            }
            else if ("name".equalsIgnoreCase(trim)) {
                array[n] = 2;
            }
            else if ("itemId".equalsIgnoreCase(trim)) {
                array[n] = 4;
            }
            else {
                if (!trim.isEmpty()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown match type in matchOrder: '");
                    sb.append(trim);
                    sb.append("'");
                    throw new InflateException(sb.toString());
                }
                final int[] array2 = new int[array.length - 1];
                System.arraycopy(array, 0, array2, 0, n);
                array = array2;
                --n;
            }
            ++n;
        }
        return array;
    }
    
    private void runAnimator(final Animator animator, final ArrayMap<Animator, AnimationInfo> arrayMap) {
        if (animator != null) {
            animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    arrayMap.remove(animator);
                    Transition.this.mCurrentAnimators.remove(animator);
                }
                
                public void onAnimationStart(final Animator animator) {
                    Transition.this.mCurrentAnimators.add(animator);
                }
            });
            this.animate(animator);
        }
    }
    
    @NonNull
    public Transition addListener(@NonNull final TransitionListener transitionListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<TransitionListener>();
        }
        this.mListeners.add(transitionListener);
        return this;
    }
    
    @NonNull
    public Transition addTarget(@IdRes final int n) {
        if (n > 0) {
            this.mTargetIds.add(n);
            return this;
        }
        return this;
    }
    
    @NonNull
    public Transition addTarget(@NonNull final View view) {
        this.mTargets.add(view);
        return this;
    }
    
    @NonNull
    public Transition addTarget(@NonNull final Class clazz) {
        if (this.mTargetTypes == null) {
            this.mTargetTypes = new ArrayList<Class>();
        }
        this.mTargetTypes.add(clazz);
        return this;
    }
    
    @NonNull
    public Transition addTarget(@NonNull final String s) {
        if (this.mTargetNames == null) {
            this.mTargetNames = new ArrayList<String>();
        }
        this.mTargetNames.add(s);
        return this;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void animate(final Animator animator) {
        if (animator == null) {
            this.end();
            return;
        }
        if (this.getDuration() >= 0L) {
            animator.setDuration(this.getDuration());
        }
        if (this.getStartDelay() >= 0L) {
            animator.setStartDelay(this.getStartDelay());
        }
        if (this.getInterpolator() != null) {
            animator.setInterpolator(this.getInterpolator());
        }
        animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                Transition.this.end();
                animator.removeListener((Animator$AnimatorListener)this);
            }
        });
        animator.start();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void cancel() {
        for (int i = this.mCurrentAnimators.size() - 1; i >= 0; --i) {
            this.mCurrentAnimators.get(i).cancel();
        }
        final ArrayList<TransitionListener> mListeners = this.mListeners;
        if (mListeners != null && mListeners.size() > 0) {
            final ArrayList list = (ArrayList)this.mListeners.clone();
            for (int size = list.size(), j = 0; j < size; ++j) {
                list.get(j).onTransitionCancel(this);
            }
        }
    }
    
    public abstract void captureEndValues(@NonNull final TransitionValues p0);
    
    void capturePropagationValues(final TransitionValues transitionValues) {
        if (this.mPropagation == null || transitionValues.values.isEmpty()) {
            return;
        }
        final String[] propagationProperties = this.mPropagation.getPropagationProperties();
        if (propagationProperties == null) {
            return;
        }
        final boolean b = true;
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= propagationProperties.length) {
                break;
            }
            if (!transitionValues.values.containsKey(propagationProperties[n])) {
                b2 = false;
                break;
            }
            ++n;
        }
        if (!b2) {
            this.mPropagation.captureValues(transitionValues);
        }
    }
    
    public abstract void captureStartValues(@NonNull final TransitionValues p0);
    
    void captureValues(final ViewGroup viewGroup, final boolean b) {
        this.clearValues(b);
        Label_0324: {
            if (this.mTargetIds.size() > 0 || this.mTargets.size() > 0) {
                final ArrayList<String> mTargetNames = this.mTargetNames;
                if (mTargetNames == null || mTargetNames.isEmpty()) {
                    final ArrayList<Class> mTargetTypes = this.mTargetTypes;
                    if (mTargetTypes == null || mTargetTypes.isEmpty()) {
                        for (int i = 0; i < this.mTargetIds.size(); ++i) {
                            final View viewById = viewGroup.findViewById((int)this.mTargetIds.get(i));
                            if (viewById != null) {
                                final TransitionValues transitionValues = new TransitionValues();
                                transitionValues.view = viewById;
                                if (b) {
                                    this.captureStartValues(transitionValues);
                                }
                                else {
                                    this.captureEndValues(transitionValues);
                                }
                                transitionValues.mTargetedTransitions.add(this);
                                this.capturePropagationValues(transitionValues);
                                if (b) {
                                    addViewValues(this.mStartValues, viewById, transitionValues);
                                }
                                else {
                                    addViewValues(this.mEndValues, viewById, transitionValues);
                                }
                            }
                        }
                        for (int j = 0; j < this.mTargets.size(); ++j) {
                            final View view = this.mTargets.get(j);
                            final TransitionValues transitionValues2 = new TransitionValues();
                            transitionValues2.view = view;
                            if (b) {
                                this.captureStartValues(transitionValues2);
                            }
                            else {
                                this.captureEndValues(transitionValues2);
                            }
                            transitionValues2.mTargetedTransitions.add(this);
                            this.capturePropagationValues(transitionValues2);
                            if (b) {
                                addViewValues(this.mStartValues, view, transitionValues2);
                            }
                            else {
                                addViewValues(this.mEndValues, view, transitionValues2);
                            }
                        }
                        break Label_0324;
                    }
                }
            }
            this.captureHierarchy((View)viewGroup, b);
        }
        if (!b) {
            final ArrayMap<String, String> mNameOverrides = this.mNameOverrides;
            if (mNameOverrides != null) {
                final int size = mNameOverrides.size();
                final ArrayList list = new ArrayList<View>(size);
                for (int k = 0; k < size; ++k) {
                    list.add(this.mStartValues.mNameValues.remove(this.mNameOverrides.keyAt(k)));
                }
                for (int l = 0; l < size; ++l) {
                    final View view2 = list.get(l);
                    if (view2 != null) {
                        this.mStartValues.mNameValues.put(this.mNameOverrides.valueAt(l), view2);
                    }
                }
            }
        }
    }
    
    void clearValues(final boolean b) {
        if (b) {
            this.mStartValues.mViewValues.clear();
            this.mStartValues.mIdValues.clear();
            this.mStartValues.mItemIdValues.clear();
            return;
        }
        this.mEndValues.mViewValues.clear();
        this.mEndValues.mIdValues.clear();
        this.mEndValues.mItemIdValues.clear();
    }
    
    public Transition clone() {
        try {
            final Transition transition = (Transition)super.clone();
            transition.mAnimators = new ArrayList<Animator>();
            transition.mStartValues = new TransitionValuesMaps();
            transition.mEndValues = new TransitionValuesMaps();
            transition.mStartValuesList = null;
            transition.mEndValuesList = null;
            return transition;
        }
        catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    
    @Nullable
    public Animator createAnimator(@NonNull final ViewGroup viewGroup, @Nullable final TransitionValues transitionValues, @Nullable final TransitionValues transitionValues2) {
        return null;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void createAnimators(final ViewGroup viewGroup, final TransitionValuesMaps transitionValuesMaps, final TransitionValuesMaps transitionValuesMaps2, final ArrayList<TransitionValues> list, final ArrayList<TransitionValues> list2) {
        final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        long min = Long.MAX_VALUE;
        final SparseIntArray sparseIntArray = new SparseIntArray();
        for (int size = list.size(), i = 0; i < size; ++i) {
            TransitionValues transitionValues = list.get(i);
            TransitionValues transitionValues2 = list2.get(i);
            if (transitionValues != null && !transitionValues.mTargetedTransitions.contains(this)) {
                transitionValues = null;
            }
            if (transitionValues2 != null && !transitionValues2.mTargetedTransitions.contains(this)) {
                transitionValues2 = null;
            }
            if (transitionValues != null || transitionValues2 != null) {
                if (transitionValues == null || transitionValues2 == null || this.isTransitionRequired(transitionValues, transitionValues2)) {
                    Animator animator = this.createAnimator(viewGroup, transitionValues, transitionValues2);
                    if (animator != null) {
                        TransitionValues transitionValues3 = null;
                        View view;
                        if (transitionValues2 != null) {
                            view = transitionValues2.view;
                            final String[] transitionProperties = this.getTransitionProperties();
                            Label_0414: {
                                if (view != null && transitionProperties != null) {
                                    if (transitionProperties.length > 0) {
                                        final TransitionValues transitionValues4 = new TransitionValues();
                                        transitionValues4.view = view;
                                        final TransitionValues transitionValues5 = transitionValuesMaps2.mViewValues.get(view);
                                        if (transitionValues5 != null) {
                                            for (int j = 0; j < transitionProperties.length; ++j) {
                                                transitionValues4.values.put(transitionProperties[j], transitionValues5.values.get(transitionProperties[j]));
                                            }
                                        }
                                        for (int size2 = runningAnimators.size(), k = 0; k < size2; ++k) {
                                            final AnimationInfo animationInfo = runningAnimators.get(runningAnimators.keyAt(k));
                                            if (animationInfo.mValues != null && animationInfo.mView == view) {
                                                if (animationInfo.mName.equals(this.getName())) {
                                                    if (animationInfo.mValues.equals(transitionValues4)) {
                                                        animator = null;
                                                        transitionValues3 = transitionValues4;
                                                        break Label_0414;
                                                    }
                                                }
                                            }
                                        }
                                        transitionValues3 = transitionValues4;
                                    }
                                }
                            }
                        }
                        else {
                            view = transitionValues.view;
                            transitionValues3 = null;
                        }
                        if (animator != null) {
                            final TransitionPropagation mPropagation = this.mPropagation;
                            if (mPropagation != null) {
                                final long startDelay = mPropagation.getStartDelay(viewGroup, this, transitionValues, transitionValues2);
                                sparseIntArray.put(this.mAnimators.size(), (int)startDelay);
                                min = Math.min(startDelay, min);
                            }
                            runningAnimators.put(animator, new AnimationInfo(view, this.getName(), this, ViewUtils.getWindowId((View)viewGroup), transitionValues3));
                            this.mAnimators.add(animator);
                        }
                    }
                }
            }
        }
        if (min != 0L) {
            for (int l = 0; l < sparseIntArray.size(); ++l) {
                final Animator animator2 = this.mAnimators.get(sparseIntArray.keyAt(l));
                animator2.setStartDelay(sparseIntArray.valueAt(l) - min + animator2.getStartDelay());
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void end() {
        --this.mNumInstances;
        if (this.mNumInstances == 0) {
            final ArrayList<TransitionListener> mListeners = this.mListeners;
            if (mListeners != null && mListeners.size() > 0) {
                final ArrayList list = (ArrayList)this.mListeners.clone();
                for (int size = list.size(), i = 0; i < size; ++i) {
                    list.get(i).onTransitionEnd(this);
                }
            }
            for (int j = 0; j < this.mStartValues.mItemIdValues.size(); ++j) {
                final View view = this.mStartValues.mItemIdValues.valueAt(j);
                if (view != null) {
                    ViewCompat.setHasTransientState(view, false);
                }
            }
            for (int k = 0; k < this.mEndValues.mItemIdValues.size(); ++k) {
                final View view2 = this.mEndValues.mItemIdValues.valueAt(k);
                if (view2 != null) {
                    ViewCompat.setHasTransientState(view2, false);
                }
            }
            this.mEnded = true;
        }
    }
    
    @NonNull
    public Transition excludeChildren(@IdRes final int n, final boolean b) {
        this.mTargetIdChildExcludes = this.excludeId(this.mTargetIdChildExcludes, n, b);
        return this;
    }
    
    @NonNull
    public Transition excludeChildren(@NonNull final View view, final boolean b) {
        this.mTargetChildExcludes = this.excludeView(this.mTargetChildExcludes, view, b);
        return this;
    }
    
    @NonNull
    public Transition excludeChildren(@NonNull final Class clazz, final boolean b) {
        this.mTargetTypeChildExcludes = this.excludeType(this.mTargetTypeChildExcludes, clazz, b);
        return this;
    }
    
    @NonNull
    public Transition excludeTarget(@IdRes final int n, final boolean b) {
        this.mTargetIdExcludes = this.excludeId(this.mTargetIdExcludes, n, b);
        return this;
    }
    
    @NonNull
    public Transition excludeTarget(@NonNull final View view, final boolean b) {
        this.mTargetExcludes = this.excludeView(this.mTargetExcludes, view, b);
        return this;
    }
    
    @NonNull
    public Transition excludeTarget(@NonNull final Class clazz, final boolean b) {
        this.mTargetTypeExcludes = this.excludeType(this.mTargetTypeExcludes, clazz, b);
        return this;
    }
    
    @NonNull
    public Transition excludeTarget(@NonNull final String s, final boolean b) {
        this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, s, b);
        return this;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void forceToEnd(final ViewGroup viewGroup) {
        final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        final int size = runningAnimators.size();
        if (viewGroup != null) {
            final WindowIdImpl windowId = ViewUtils.getWindowId((View)viewGroup);
            for (int i = size - 1; i >= 0; --i) {
                final AnimationInfo animationInfo = runningAnimators.valueAt(i);
                if (animationInfo.mView != null && windowId != null && windowId.equals(animationInfo.mWindowId)) {
                    runningAnimators.keyAt(i).end();
                }
            }
        }
    }
    
    public long getDuration() {
        return this.mDuration;
    }
    
    @Nullable
    public Rect getEpicenter() {
        final EpicenterCallback mEpicenterCallback = this.mEpicenterCallback;
        if (mEpicenterCallback == null) {
            return null;
        }
        return mEpicenterCallback.onGetEpicenter(this);
    }
    
    @Nullable
    public EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }
    
    @Nullable
    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }
    
    TransitionValues getMatchedTransitionValues(final View view, final boolean b) {
        final TransitionSet mParent = this.mParent;
        if (mParent != null) {
            return mParent.getMatchedTransitionValues(view, b);
        }
        ArrayList<TransitionValues> list;
        if (b) {
            list = this.mStartValuesList;
        }
        else {
            list = this.mEndValuesList;
        }
        if (list == null) {
            return null;
        }
        final int size = list.size();
        final int n = -1;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= size) {
                break;
            }
            final TransitionValues transitionValues = list.get(n2);
            if (transitionValues == null) {
                return null;
            }
            if (transitionValues.view == view) {
                n3 = n2;
                break;
            }
            ++n2;
        }
        if (n3 >= 0) {
            ArrayList<TransitionValues> list2;
            if (b) {
                list2 = this.mEndValuesList;
            }
            else {
                list2 = this.mStartValuesList;
            }
            return list2.get(n3);
        }
        return null;
    }
    
    @NonNull
    public String getName() {
        return this.mName;
    }
    
    @NonNull
    public PathMotion getPathMotion() {
        return this.mPathMotion;
    }
    
    @Nullable
    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }
    
    public long getStartDelay() {
        return this.mStartDelay;
    }
    
    @NonNull
    public List<Integer> getTargetIds() {
        return this.mTargetIds;
    }
    
    @Nullable
    public List<String> getTargetNames() {
        return this.mTargetNames;
    }
    
    @Nullable
    public List<Class> getTargetTypes() {
        return this.mTargetTypes;
    }
    
    @NonNull
    public List<View> getTargets() {
        return this.mTargets;
    }
    
    @Nullable
    public String[] getTransitionProperties() {
        return null;
    }
    
    @Nullable
    public TransitionValues getTransitionValues(@NonNull final View view, final boolean b) {
        final TransitionSet mParent = this.mParent;
        if (mParent != null) {
            return mParent.getTransitionValues(view, b);
        }
        TransitionValuesMaps transitionValuesMaps;
        if (b) {
            transitionValuesMaps = this.mStartValues;
        }
        else {
            transitionValuesMaps = this.mEndValues;
        }
        return (TransitionValues)transitionValuesMaps.mViewValues.get(view);
    }
    
    public boolean isTransitionRequired(@Nullable final TransitionValues transitionValues, @Nullable final TransitionValues transitionValues2) {
        final boolean b = false;
        if (transitionValues == null || transitionValues2 == null) {
            return false;
        }
        final String[] transitionProperties = this.getTransitionProperties();
        if (transitionProperties != null) {
            final int length = transitionProperties.length;
            int n = 0;
            boolean b2;
            while (true) {
                b2 = b;
                if (n >= length) {
                    break;
                }
                if (isValueChanged(transitionValues, transitionValues2, transitionProperties[n])) {
                    b2 = true;
                    break;
                }
                ++n;
            }
            return b2;
        }
        final Iterator<String> iterator = transitionValues.values.keySet().iterator();
        while (iterator.hasNext()) {
            if (isValueChanged(transitionValues, transitionValues2, iterator.next())) {
                return true;
            }
        }
        return false;
    }
    
    boolean isValidTarget(final View view) {
        final int id = view.getId();
        final ArrayList<Integer> mTargetIdExcludes = this.mTargetIdExcludes;
        if (mTargetIdExcludes != null && mTargetIdExcludes.contains(id)) {
            return false;
        }
        final ArrayList<View> mTargetExcludes = this.mTargetExcludes;
        if (mTargetExcludes != null && mTargetExcludes.contains(view)) {
            return false;
        }
        final ArrayList<Class> mTargetTypeExcludes = this.mTargetTypeExcludes;
        if (mTargetTypeExcludes != null) {
            for (int size = mTargetTypeExcludes.size(), i = 0; i < size; ++i) {
                if (this.mTargetTypeExcludes.get(i).isInstance(view)) {
                    return false;
                }
            }
        }
        if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(view) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(view))) {
            return false;
        }
        if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0) {
            final ArrayList<Class> mTargetTypes = this.mTargetTypes;
            if (mTargetTypes == null || mTargetTypes.isEmpty()) {
                final ArrayList<String> mTargetNames = this.mTargetNames;
                if (mTargetNames == null) {
                    return true;
                }
                if (mTargetNames.isEmpty()) {
                    return true;
                }
            }
        }
        if (this.mTargetIds.contains(id)) {
            return true;
        }
        if (this.mTargets.contains(view)) {
            return true;
        }
        final ArrayList<String> mTargetNames2 = this.mTargetNames;
        if (mTargetNames2 != null && mTargetNames2.contains(ViewCompat.getTransitionName(view))) {
            return true;
        }
        if (this.mTargetTypes != null) {
            for (int j = 0; j < this.mTargetTypes.size(); ++j) {
                if (this.mTargetTypes.get(j).isInstance(view)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void pause(final View view) {
        if (!this.mEnded) {
            final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
            final int size = runningAnimators.size();
            final WindowIdImpl windowId = ViewUtils.getWindowId(view);
            for (int i = size - 1; i >= 0; --i) {
                final AnimationInfo animationInfo = runningAnimators.valueAt(i);
                if (animationInfo.mView != null && windowId.equals(animationInfo.mWindowId)) {
                    AnimatorUtils.pause(runningAnimators.keyAt(i));
                }
            }
            final ArrayList<TransitionListener> mListeners = this.mListeners;
            if (mListeners != null && mListeners.size() > 0) {
                final ArrayList list = (ArrayList)this.mListeners.clone();
                for (int size2 = list.size(), j = 0; j < size2; ++j) {
                    list.get(j).onTransitionPause(this);
                }
            }
            this.mPaused = true;
        }
    }
    
    void playTransition(final ViewGroup viewGroup) {
        this.mStartValuesList = new ArrayList<TransitionValues>();
        this.mEndValuesList = new ArrayList<TransitionValues>();
        this.matchStartAndEnd(this.mStartValues, this.mEndValues);
        final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        final int size = runningAnimators.size();
        final WindowIdImpl windowId = ViewUtils.getWindowId((View)viewGroup);
        for (int i = size - 1; i >= 0; --i) {
            final Animator animator = runningAnimators.keyAt(i);
            if (animator != null) {
                final AnimationInfo animationInfo = runningAnimators.get(animator);
                if (animationInfo != null && animationInfo.mView != null) {
                    if (windowId.equals(animationInfo.mWindowId)) {
                        final TransitionValues mValues = animationInfo.mValues;
                        final View mView = animationInfo.mView;
                        boolean b = true;
                        final TransitionValues transitionValues = this.getTransitionValues(mView, true);
                        final TransitionValues matchedTransitionValues = this.getMatchedTransitionValues(mView, true);
                        Label_0183: {
                            if (transitionValues != null || matchedTransitionValues != null) {
                                if (animationInfo.mTransition.isTransitionRequired(mValues, matchedTransitionValues)) {
                                    break Label_0183;
                                }
                            }
                            b = false;
                        }
                        if (b) {
                            if (!animator.isRunning() && !animator.isStarted()) {
                                runningAnimators.remove(animator);
                            }
                            else {
                                animator.cancel();
                            }
                        }
                    }
                }
            }
        }
        this.createAnimators(viewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        this.runAnimators();
    }
    
    @NonNull
    public Transition removeListener(@NonNull final TransitionListener transitionListener) {
        final ArrayList<TransitionListener> mListeners = this.mListeners;
        if (mListeners == null) {
            return this;
        }
        mListeners.remove(transitionListener);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
            return this;
        }
        return this;
    }
    
    @NonNull
    public Transition removeTarget(@IdRes final int n) {
        if (n > 0) {
            this.mTargetIds.remove((Object)n);
            return this;
        }
        return this;
    }
    
    @NonNull
    public Transition removeTarget(@NonNull final View view) {
        this.mTargets.remove(view);
        return this;
    }
    
    @NonNull
    public Transition removeTarget(@NonNull final Class clazz) {
        final ArrayList<Class> mTargetTypes = this.mTargetTypes;
        if (mTargetTypes != null) {
            mTargetTypes.remove(clazz);
            return this;
        }
        return this;
    }
    
    @NonNull
    public Transition removeTarget(@NonNull final String s) {
        final ArrayList<String> mTargetNames = this.mTargetNames;
        if (mTargetNames != null) {
            mTargetNames.remove(s);
            return this;
        }
        return this;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void resume(final View view) {
        if (this.mPaused) {
            if (!this.mEnded) {
                final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
                final int size = runningAnimators.size();
                final WindowIdImpl windowId = ViewUtils.getWindowId(view);
                for (int i = size - 1; i >= 0; --i) {
                    final AnimationInfo animationInfo = runningAnimators.valueAt(i);
                    if (animationInfo.mView != null && windowId.equals(animationInfo.mWindowId)) {
                        AnimatorUtils.resume(runningAnimators.keyAt(i));
                    }
                }
                final ArrayList<TransitionListener> mListeners = this.mListeners;
                if (mListeners != null && mListeners.size() > 0) {
                    final ArrayList list = (ArrayList)this.mListeners.clone();
                    for (int size2 = list.size(), j = 0; j < size2; ++j) {
                        list.get(j).onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void runAnimators() {
        this.start();
        final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        for (final Animator animator : this.mAnimators) {
            if (runningAnimators.containsKey(animator)) {
                this.start();
                this.runAnimator(animator, runningAnimators);
            }
        }
        this.mAnimators.clear();
        this.end();
    }
    
    void setCanRemoveViews(final boolean mCanRemoveViews) {
        this.mCanRemoveViews = mCanRemoveViews;
    }
    
    @NonNull
    public Transition setDuration(final long mDuration) {
        this.mDuration = mDuration;
        return this;
    }
    
    public void setEpicenterCallback(@Nullable final EpicenterCallback mEpicenterCallback) {
        this.mEpicenterCallback = mEpicenterCallback;
    }
    
    @NonNull
    public Transition setInterpolator(@Nullable final TimeInterpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
        return this;
    }
    
    public void setMatchOrder(final int... array) {
        if (array != null && array.length != 0) {
            for (int i = 0; i < array.length; ++i) {
                if (!isValidMatch(array[i])) {
                    throw new IllegalArgumentException("matches contains invalid value");
                }
                if (alreadyContains(array, i)) {
                    throw new IllegalArgumentException("matches contains a duplicate value");
                }
            }
            this.mMatchOrder = array.clone();
            return;
        }
        this.mMatchOrder = Transition.DEFAULT_MATCH_ORDER;
    }
    
    public void setPathMotion(@Nullable final PathMotion mPathMotion) {
        if (mPathMotion == null) {
            this.mPathMotion = Transition.STRAIGHT_PATH_MOTION;
            return;
        }
        this.mPathMotion = mPathMotion;
    }
    
    public void setPropagation(@Nullable final TransitionPropagation mPropagation) {
        this.mPropagation = mPropagation;
    }
    
    Transition setSceneRoot(final ViewGroup mSceneRoot) {
        this.mSceneRoot = mSceneRoot;
        return this;
    }
    
    @NonNull
    public Transition setStartDelay(final long mStartDelay) {
        this.mStartDelay = mStartDelay;
        return this;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected void start() {
        if (this.mNumInstances == 0) {
            final ArrayList<TransitionListener> mListeners = this.mListeners;
            if (mListeners != null && mListeners.size() > 0) {
                final ArrayList list = (ArrayList)this.mListeners.clone();
                for (int size = list.size(), i = 0; i < size; ++i) {
                    list.get(i).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        ++this.mNumInstances;
    }
    
    @Override
    public String toString() {
        return this.toString("");
    }
    
    String toString(String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(this.getClass().getSimpleName());
        sb.append("@");
        sb.append(Integer.toHexString(this.hashCode()));
        sb.append(": ");
        s = sb.toString();
        if (this.mDuration != -1L) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("dur(");
            sb2.append(this.mDuration);
            sb2.append(") ");
            s = sb2.toString();
        }
        if (this.mStartDelay != -1L) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append("dly(");
            sb3.append(this.mStartDelay);
            sb3.append(") ");
            s = sb3.toString();
        }
        if (this.mInterpolator != null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(s);
            sb4.append("interp(");
            sb4.append(this.mInterpolator);
            sb4.append(") ");
            s = sb4.toString();
        }
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
            return s;
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(s);
        sb5.append("tgts(");
        s = sb5.toString();
        if (this.mTargetIds.size() > 0) {
            for (int i = 0; i < this.mTargetIds.size(); ++i) {
                if (i > 0) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(s);
                    sb6.append(", ");
                    s = sb6.toString();
                }
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(s);
                sb7.append(this.mTargetIds.get(i));
                s = sb7.toString();
            }
        }
        if (this.mTargets.size() > 0) {
            for (int j = 0; j < this.mTargets.size(); ++j) {
                if (j > 0) {
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append(s);
                    sb8.append(", ");
                    s = sb8.toString();
                }
                final StringBuilder sb9 = new StringBuilder();
                sb9.append(s);
                sb9.append(this.mTargets.get(j));
                s = sb9.toString();
            }
        }
        final StringBuilder sb10 = new StringBuilder();
        sb10.append(s);
        sb10.append(")");
        return sb10.toString();
    }
    
    private static class AnimationInfo
    {
        String mName;
        Transition mTransition;
        TransitionValues mValues;
        View mView;
        WindowIdImpl mWindowId;
        
        AnimationInfo(final View mView, final String mName, final Transition mTransition, final WindowIdImpl mWindowId, final TransitionValues mValues) {
            this.mView = mView;
            this.mName = mName;
            this.mValues = mValues;
            this.mWindowId = mWindowId;
            this.mTransition = mTransition;
        }
    }
    
    private static class ArrayListManager
    {
        static <T> ArrayList<T> add(ArrayList<T> list, final T t) {
            if (list == null) {
                list = new ArrayList<T>();
            }
            if (!list.contains(t)) {
                list.add(t);
                return list;
            }
            return list;
        }
        
        static <T> ArrayList<T> remove(final ArrayList<T> list, final T t) {
            if (list == null) {
                return list;
            }
            list.remove(t);
            if (list.isEmpty()) {
                return null;
            }
            return list;
        }
    }
    
    public abstract static class EpicenterCallback
    {
        public abstract Rect onGetEpicenter(@NonNull final Transition p0);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface MatchOrder {
    }
    
    public interface TransitionListener
    {
        void onTransitionCancel(@NonNull final Transition p0);
        
        void onTransitionEnd(@NonNull final Transition p0);
        
        void onTransitionPause(@NonNull final Transition p0);
        
        void onTransitionResume(@NonNull final Transition p0);
        
        void onTransitionStart(@NonNull final Transition p0);
    }
}
