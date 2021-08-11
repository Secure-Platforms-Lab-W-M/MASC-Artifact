/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Path
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.util.SparseIntArray
 *  android.view.InflateException
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.animation.AnimationUtils
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.transition.AnimatorUtils;
import android.support.transition.PathMotion;
import android.support.transition.Styleable;
import android.support.transition.TransitionPropagation;
import android.support.transition.TransitionSet;
import android.support.transition.TransitionValues;
import android.support.transition.TransitionValuesMaps;
import android.support.transition.ViewUtils;
import android.support.transition.WindowIdImpl;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.xmlpull.v1.XmlPullParser;

public abstract class Transition
implements Cloneable {
    static final boolean DBG = false;
    private static final int[] DEFAULT_MATCH_ORDER = new int[]{2, 1, 3, 4};
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
    private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion(){

        @Override
        public Path getPath(float f, float f2, float f3, float f4) {
            Path path = new Path();
            path.moveTo(f, f2);
            path.lineTo(f3, f4);
            return path;
        }
    };
    private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal();
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

    public Transition() {
        this.mName = this.getClass().getName();
        this.mStartDelay = -1L;
        this.mDuration = -1L;
        this.mInterpolator = null;
        this.mTargetIds = new ArrayList();
        this.mTargets = new ArrayList();
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
        this.mMatchOrder = DEFAULT_MATCH_ORDER;
        this.mSceneRoot = null;
        this.mCanRemoveViews = false;
        this.mCurrentAnimators = new ArrayList();
        this.mNumInstances = 0;
        this.mPaused = false;
        this.mEnded = false;
        this.mListeners = null;
        this.mAnimators = new ArrayList();
        this.mPathMotion = STRAIGHT_PATH_MOTION;
    }

    public Transition(Context object, AttributeSet attributeSet) {
        int n;
        this.mName = this.getClass().getName();
        this.mStartDelay = -1L;
        this.mDuration = -1L;
        this.mInterpolator = null;
        this.mTargetIds = new ArrayList();
        this.mTargets = new ArrayList();
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
        this.mMatchOrder = DEFAULT_MATCH_ORDER;
        this.mSceneRoot = null;
        this.mCanRemoveViews = false;
        this.mCurrentAnimators = new ArrayList();
        this.mNumInstances = 0;
        this.mPaused = false;
        this.mEnded = false;
        this.mListeners = null;
        this.mAnimators = new ArrayList();
        this.mPathMotion = STRAIGHT_PATH_MOTION;
        TypedArray typedArray = object.obtainStyledAttributes(attributeSet, Styleable.TRANSITION);
        attributeSet = (XmlResourceParser)attributeSet;
        long l = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)attributeSet, "duration", 1, -1);
        if (l >= 0L) {
            this.setDuration(l);
        }
        if ((l = (long)TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)attributeSet, "startDelay", 2, -1)) > 0L) {
            this.setStartDelay(l);
        }
        if ((n = TypedArrayUtils.getNamedResourceId(typedArray, (XmlPullParser)attributeSet, "interpolator", 0, 0)) > 0) {
            this.setInterpolator((TimeInterpolator)AnimationUtils.loadInterpolator((Context)object, (int)n));
        }
        if ((object = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)attributeSet, "matchOrder", 3)) != null) {
            this.setMatchOrder(Transition.parseMatchOrder((String)object));
        }
        typedArray.recycle();
    }

    private void addUnmatched(ArrayMap<View, TransitionValues> object, ArrayMap<View, TransitionValues> arrayMap) {
        int n;
        for (n = 0; n < object.size(); ++n) {
            TransitionValues transitionValues = object.valueAt(n);
            if (!this.isValidTarget(transitionValues.view)) continue;
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add(null);
        }
        for (n = 0; n < arrayMap.size(); ++n) {
            object = arrayMap.valueAt(n);
            if (!this.isValidTarget(object.view)) continue;
            this.mEndValuesList.add((TransitionValues)object);
            this.mStartValuesList.add(null);
        }
    }

    private static void addViewValues(TransitionValuesMaps transitionValuesMaps, View view, TransitionValues object) {
        transitionValuesMaps.mViewValues.put(view, (TransitionValues)object);
        int n = view.getId();
        if (n >= 0) {
            if (transitionValuesMaps.mIdValues.indexOfKey(n) >= 0) {
                transitionValuesMaps.mIdValues.put(n, (Object)null);
            } else {
                transitionValuesMaps.mIdValues.put(n, (Object)view);
            }
        }
        if ((object = ViewCompat.getTransitionName(view)) != null) {
            if (transitionValuesMaps.mNameValues.containsKey(object)) {
                transitionValuesMaps.mNameValues.put((String)object, null);
            } else {
                transitionValuesMaps.mNameValues.put((String)object, view);
            }
        }
        if (view.getParent() instanceof ListView) {
            object = (ListView)view.getParent();
            if (object.getAdapter().hasStableIds()) {
                long l = object.getItemIdAtPosition(object.getPositionForView(view));
                if (transitionValuesMaps.mItemIdValues.indexOfKey(l) >= 0) {
                    view = transitionValuesMaps.mItemIdValues.get(l);
                    if (view != null) {
                        ViewCompat.setHasTransientState(view, false);
                        transitionValuesMaps.mItemIdValues.put(l, null);
                    }
                    return;
                }
                ViewCompat.setHasTransientState(view, true);
                transitionValuesMaps.mItemIdValues.put(l, view);
                return;
            }
            return;
        }
    }

    private static boolean alreadyContains(int[] arrn, int n) {
        int n2 = arrn[n];
        for (int i = 0; i < n; ++i) {
            if (arrn[i] != n2) continue;
            return true;
        }
        return false;
    }

    private void captureHierarchy(View view, boolean bl) {
        int n;
        if (view == null) {
            return;
        }
        int n2 = view.getId();
        ArrayList arrayList = this.mTargetIdExcludes;
        if (arrayList != null && arrayList.contains(n2)) {
            return;
        }
        arrayList = this.mTargetExcludes;
        if (arrayList != null && arrayList.contains((Object)view)) {
            return;
        }
        arrayList = this.mTargetTypeExcludes;
        if (arrayList != null) {
            int n3 = arrayList.size();
            for (n = 0; n < n3; ++n) {
                if (!this.mTargetTypeExcludes.get(n).isInstance((Object)view)) continue;
                return;
            }
        }
        if (view.getParent() instanceof ViewGroup) {
            arrayList = new TransitionValues();
            arrayList.view = view;
            if (bl) {
                this.captureStartValues((TransitionValues)((Object)arrayList));
            } else {
                this.captureEndValues((TransitionValues)((Object)arrayList));
            }
            arrayList.mTargetedTransitions.add(this);
            this.capturePropagationValues((TransitionValues)((Object)arrayList));
            if (bl) {
                Transition.addViewValues(this.mStartValues, view, (TransitionValues)((Object)arrayList));
            } else {
                Transition.addViewValues(this.mEndValues, view, (TransitionValues)((Object)arrayList));
            }
        }
        if (view instanceof ViewGroup) {
            arrayList = this.mTargetIdChildExcludes;
            if (arrayList != null && arrayList.contains(n2)) {
                return;
            }
            arrayList = this.mTargetChildExcludes;
            if (arrayList != null && arrayList.contains((Object)view)) {
                return;
            }
            arrayList = this.mTargetTypeChildExcludes;
            if (arrayList != null) {
                n2 = arrayList.size();
                for (n = 0; n < n2; ++n) {
                    if (!this.mTargetTypeChildExcludes.get(n).isInstance((Object)view)) continue;
                    return;
                }
            }
            view = (ViewGroup)view;
            for (n = 0; n < view.getChildCount(); ++n) {
                this.captureHierarchy(view.getChildAt(n), bl);
            }
            return;
        }
    }

    private ArrayList<Integer> excludeId(ArrayList<Integer> arrayList, int n, boolean bl) {
        if (n > 0) {
            if (bl) {
                return ArrayListManager.add(arrayList, n);
            }
            return ArrayListManager.remove(arrayList, n);
        }
        return arrayList;
    }

    private static <T> ArrayList<T> excludeObject(ArrayList<T> arrayList, T t, boolean bl) {
        if (t != null) {
            if (bl) {
                return ArrayListManager.add(arrayList, t);
            }
            return ArrayListManager.remove(arrayList, t);
        }
        return arrayList;
    }

    private ArrayList<Class> excludeType(ArrayList<Class> arrayList, Class class_, boolean bl) {
        if (class_ != null) {
            if (bl) {
                return ArrayListManager.add(arrayList, class_);
            }
            return ArrayListManager.remove(arrayList, class_);
        }
        return arrayList;
    }

    private ArrayList<View> excludeView(ArrayList<View> arrayList, View view, boolean bl) {
        if (view != null) {
            if (bl) {
                return ArrayListManager.add(arrayList, view);
            }
            return ArrayListManager.remove(arrayList, view);
        }
        return arrayList;
    }

    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap arrayMap = sRunningAnimators.get();
        if (arrayMap == null) {
            arrayMap = new ArrayMap();
            sRunningAnimators.set(arrayMap);
            return arrayMap;
        }
        return arrayMap;
    }

    private static boolean isValidMatch(int n) {
        if (n >= 1 && n <= 4) {
            return true;
        }
        return false;
    }

    private static boolean isValueChanged(TransitionValues object, TransitionValues object2, String string2) {
        object = object.values.get(string2);
        object2 = object2.values.get(string2);
        if (object == null && object2 == null) {
            return false;
        }
        if (object != null && object2 != null) {
            return object.equals(object2) ^ true;
        }
        return true;
    }

    private void matchIds(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, SparseArray<View> sparseArray, SparseArray<View> sparseArray2) {
        int n = sparseArray.size();
        for (int i = 0; i < n; ++i) {
            View view;
            View view2 = (View)sparseArray.valueAt(i);
            if (view2 == null || !this.isValidTarget(view2) || (view = (View)sparseArray2.get(sparseArray.keyAt(i))) == null || !this.isValidTarget(view)) continue;
            TransitionValues transitionValues = arrayMap.get((Object)view2);
            TransitionValues transitionValues2 = arrayMap2.get((Object)view);
            if (transitionValues == null || transitionValues2 == null) continue;
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add(transitionValues2);
            arrayMap.remove((Object)view2);
            arrayMap2.remove((Object)view);
        }
    }

    private void matchInstances(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            Object object = arrayMap.keyAt(i);
            if (object == null || !this.isValidTarget((View)object) || (object = arrayMap2.remove(object)) == null || object.view == null || !this.isValidTarget(object.view)) continue;
            TransitionValues transitionValues = arrayMap.removeAt(i);
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add((TransitionValues)object);
        }
    }

    private void matchItemIds(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, LongSparseArray<View> longSparseArray, LongSparseArray<View> longSparseArray2) {
        int n = longSparseArray.size();
        for (int i = 0; i < n; ++i) {
            View view;
            View view2 = longSparseArray.valueAt(i);
            if (view2 == null || !this.isValidTarget(view2) || (view = longSparseArray2.get(longSparseArray.keyAt(i))) == null || !this.isValidTarget(view)) continue;
            TransitionValues transitionValues = arrayMap.get((Object)view2);
            TransitionValues transitionValues2 = arrayMap2.get((Object)view);
            if (transitionValues == null || transitionValues2 == null) continue;
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add(transitionValues2);
            arrayMap.remove((Object)view2);
            arrayMap2.remove((Object)view);
        }
    }

    private void matchNames(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, ArrayMap<String, View> arrayMap3, ArrayMap<String, View> arrayMap4) {
        int n = arrayMap3.size();
        for (int i = 0; i < n; ++i) {
            View view;
            View view2 = arrayMap3.valueAt(i);
            if (view2 == null || !this.isValidTarget(view2) || (view = arrayMap4.get(arrayMap3.keyAt(i))) == null || !this.isValidTarget(view)) continue;
            TransitionValues transitionValues = arrayMap.get((Object)view2);
            TransitionValues transitionValues2 = arrayMap2.get((Object)view);
            if (transitionValues == null || transitionValues2 == null) continue;
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add(transitionValues2);
            arrayMap.remove((Object)view2);
            arrayMap2.remove((Object)view);
        }
    }

    private void matchStartAndEnd(TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2) {
        int[] arrn;
        ArrayMap<View, TransitionValues> arrayMap = new ArrayMap<View, TransitionValues>(transitionValuesMaps.mViewValues);
        ArrayMap<View, TransitionValues> arrayMap2 = new ArrayMap<View, TransitionValues>(transitionValuesMaps2.mViewValues);
        block6 : for (int i = 0; i < (arrn = this.mMatchOrder).length; ++i) {
            switch (arrn[i]) {
                default: {
                    continue block6;
                }
                case 4: {
                    this.matchItemIds(arrayMap, arrayMap2, transitionValuesMaps.mItemIdValues, transitionValuesMaps2.mItemIdValues);
                    continue block6;
                }
                case 3: {
                    this.matchIds(arrayMap, arrayMap2, transitionValuesMaps.mIdValues, transitionValuesMaps2.mIdValues);
                    continue block6;
                }
                case 2: {
                    this.matchNames(arrayMap, arrayMap2, transitionValuesMaps.mNameValues, transitionValuesMaps2.mNameValues);
                    continue block6;
                }
                case 1: {
                    this.matchInstances(arrayMap, arrayMap2);
                }
            }
        }
        this.addUnmatched(arrayMap, arrayMap2);
    }

    private static int[] parseMatchOrder(String arrn) {
        StringTokenizer stringTokenizer = new StringTokenizer((String)arrn, ",");
        arrn = new int[stringTokenizer.countTokens()];
        int n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            int[] arrn2;
            block8 : {
                block4 : {
                    block7 : {
                        block6 : {
                            block5 : {
                                block3 : {
                                    arrn2 = stringTokenizer.nextToken().trim();
                                    if (!"id".equalsIgnoreCase((String)arrn2)) break block3;
                                    arrn[n] = 3;
                                    break block4;
                                }
                                if (!"instance".equalsIgnoreCase((String)arrn2)) break block5;
                                arrn[n] = 1;
                                break block4;
                            }
                            if (!"name".equalsIgnoreCase((String)arrn2)) break block6;
                            arrn[n] = 2;
                            break block4;
                        }
                        if (!"itemId".equalsIgnoreCase((String)arrn2)) break block7;
                        arrn[n] = 4;
                        break block4;
                    }
                    if (!arrn2.isEmpty()) break block8;
                    arrn2 = new int[arrn.length - 1];
                    System.arraycopy(arrn, 0, arrn2, 0, n);
                    arrn = arrn2;
                    --n;
                }
                ++n;
                continue;
            }
            arrn = new StringBuilder();
            arrn.append("Unknown match type in matchOrder: '");
            arrn.append((String)arrn2);
            arrn.append("'");
            throw new InflateException(arrn.toString());
        }
        return arrn;
    }

    private void runAnimator(Animator animator2, final ArrayMap<Animator, AnimationInfo> arrayMap) {
        if (animator2 != null) {
            animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator animator2) {
                    arrayMap.remove((Object)animator2);
                    Transition.this.mCurrentAnimators.remove((Object)animator2);
                }

                public void onAnimationStart(Animator animator2) {
                    Transition.this.mCurrentAnimators.add(animator2);
                }
            });
            this.animate(animator2);
            return;
        }
    }

    @NonNull
    public Transition addListener(@NonNull TransitionListener transitionListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(transitionListener);
        return this;
    }

    @NonNull
    public Transition addTarget(@IdRes int n) {
        if (n > 0) {
            this.mTargetIds.add(n);
            return this;
        }
        return this;
    }

    @NonNull
    public Transition addTarget(@NonNull View view) {
        this.mTargets.add(view);
        return this;
    }

    @NonNull
    public Transition addTarget(@NonNull Class class_) {
        if (this.mTargetTypes == null) {
            this.mTargetTypes = new ArrayList();
        }
        this.mTargetTypes.add(class_);
        return this;
    }

    @NonNull
    public Transition addTarget(@NonNull String string2) {
        if (this.mTargetNames == null) {
            this.mTargetNames = new ArrayList();
        }
        this.mTargetNames.add(string2);
        return this;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected void animate(Animator animator2) {
        if (animator2 == null) {
            this.end();
            return;
        }
        if (this.getDuration() >= 0L) {
            animator2.setDuration(this.getDuration());
        }
        if (this.getStartDelay() >= 0L) {
            animator2.setStartDelay(this.getStartDelay());
        }
        if (this.getInterpolator() != null) {
            animator2.setInterpolator(this.getInterpolator());
        }
        animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator2) {
                Transition.this.end();
                animator2.removeListener((Animator.AnimatorListener)this);
            }
        });
        animator2.start();
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected void cancel() {
        int n;
        for (n = this.mCurrentAnimators.size() - 1; n >= 0; --n) {
            this.mCurrentAnimators.get(n).cancel();
        }
        ArrayList arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
            arrayList = (ArrayList)this.mListeners.clone();
            int n2 = arrayList.size();
            for (n = 0; n < n2; ++n) {
                ((TransitionListener)arrayList.get(n)).onTransitionCancel(this);
            }
            return;
        }
    }

    public abstract void captureEndValues(@NonNull TransitionValues var1);

    void capturePropagationValues(TransitionValues transitionValues) {
        if (this.mPropagation != null && !transitionValues.values.isEmpty()) {
            boolean bl;
            String[] arrstring = this.mPropagation.getPropagationProperties();
            if (arrstring == null) {
                return;
            }
            boolean bl2 = true;
            int n = 0;
            do {
                bl = bl2;
                if (n >= arrstring.length) break;
                if (!transitionValues.values.containsKey(arrstring[n])) {
                    bl = false;
                    break;
                }
                ++n;
            } while (true);
            if (!bl) {
                this.mPropagation.captureValues(transitionValues);
                return;
            }
            return;
        }
    }

    public abstract void captureStartValues(@NonNull TransitionValues var1);

    /*
     * Enabled aggressive block sorting
     */
    void captureValues(ViewGroup view, boolean bl) {
        int n;
        ArrayList<Class> arrayList;
        Object object;
        ArrayList<String> arrayList2;
        this.clearValues(bl);
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0 || (arrayList2 = this.mTargetNames) != null && !arrayList2.isEmpty() || (arrayList = this.mTargetTypes) != null && !arrayList.isEmpty()) {
            this.captureHierarchy(view, bl);
        } else {
            for (n = 0; n < this.mTargetIds.size(); ++n) {
                View view2 = view.findViewById(this.mTargetIds.get(n).intValue());
                if (view2 == null) continue;
                object = new TransitionValues();
                object.view = view2;
                if (bl) {
                    this.captureStartValues((TransitionValues)object);
                } else {
                    this.captureEndValues((TransitionValues)object);
                }
                object.mTargetedTransitions.add(this);
                this.capturePropagationValues((TransitionValues)object);
                if (bl) {
                    Transition.addViewValues(this.mStartValues, view2, (TransitionValues)object);
                    continue;
                }
                Transition.addViewValues(this.mEndValues, view2, (TransitionValues)object);
            }
            for (n = 0; n < this.mTargets.size(); ++n) {
                view = this.mTargets.get(n);
                TransitionValues transitionValues = new TransitionValues();
                transitionValues.view = view;
                if (bl) {
                    this.captureStartValues(transitionValues);
                } else {
                    this.captureEndValues(transitionValues);
                }
                transitionValues.mTargetedTransitions.add(this);
                this.capturePropagationValues(transitionValues);
                if (bl) {
                    Transition.addViewValues(this.mStartValues, view, transitionValues);
                    continue;
                }
                Transition.addViewValues(this.mEndValues, view, transitionValues);
            }
        }
        if (!bl && (view = this.mNameOverrides) != null) {
            int n2 = view.size();
            view = new View(n2);
            for (n = 0; n < n2; ++n) {
                String string2 = this.mNameOverrides.keyAt(n);
                view.add(this.mStartValues.mNameValues.remove(string2));
            }
            n = 0;
            while (n < n2) {
                View view3 = (View)view.get(n);
                if (view3 != null) {
                    object = this.mNameOverrides.valueAt(n);
                    this.mStartValues.mNameValues.put((String)object, view3);
                }
                ++n;
            }
            return;
        }
    }

    void clearValues(boolean bl) {
        if (bl) {
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
            Transition transition = (Transition)super.clone();
            transition.mAnimators = new ArrayList<E>();
            transition.mStartValues = new TransitionValuesMaps();
            transition.mEndValues = new TransitionValuesMaps();
            transition.mStartValuesList = null;
            transition.mEndValuesList = null;
            return transition;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    @Nullable
    public Animator createAnimator(@NonNull ViewGroup viewGroup, @Nullable TransitionValues transitionValues, @Nullable TransitionValues transitionValues2) {
        return null;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        int n;
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        long l = Long.MAX_VALUE;
        SparseIntArray sparseIntArray = new SparseIntArray();
        int n2 = arrayList.size();
        for (n = 0; n < n2; ++n) {
            TransitionValues transitionValues;
            TransitionValues transitionValues2;
            Object object;
            Object object2;
            View view;
            block12 : {
                transitionValues2 = arrayList.get(n);
                transitionValues = arrayList2.get(n);
                if (transitionValues2 != null && !transitionValues2.mTargetedTransitions.contains(this)) {
                    transitionValues2 = null;
                }
                if (transitionValues != null && !transitionValues.mTargetedTransitions.contains(this)) {
                    transitionValues = null;
                }
                if (transitionValues2 == null && transitionValues == null) continue;
                int n3 = transitionValues2 != null && transitionValues != null && !this.isTransitionRequired(transitionValues2, transitionValues) ? 0 : 1;
                if (n3 == 0 || (transitionValuesMaps = this.createAnimator(viewGroup, transitionValues2, transitionValues)) == null) continue;
                object = null;
                if (transitionValues != null) {
                    view = transitionValues.view;
                    String[] arrstring = this.getTransitionProperties();
                    if (view != null && arrstring != null && arrstring.length > 0) {
                        object2 = new TransitionValues();
                        object2.view = view;
                        object = transitionValuesMaps2.mViewValues.get((Object)view);
                        if (object != null) {
                            for (n3 = 0; n3 < arrstring.length; ++n3) {
                                object2.values.put(arrstring[n3], object.values.get(arrstring[n3]));
                            }
                        }
                        n3 = arrayMap.size();
                        for (int i = 0; i < n3; ++i) {
                            object = arrayMap.get((Object)arrayMap.keyAt(i));
                            if (object.mValues == null || object.mView != view || !object.mName.equals(this.getName()) || !object.mValues.equals(object2)) continue;
                            transitionValuesMaps = null;
                            object = object2;
                            break block12;
                        }
                        object = object2;
                    }
                } else {
                    view = transitionValues2.view;
                    object = null;
                }
            }
            if (transitionValuesMaps == null) continue;
            object2 = this.mPropagation;
            if (object2 != null) {
                long l2 = object2.getStartDelay(viewGroup, this, transitionValues2, transitionValues);
                sparseIntArray.put(this.mAnimators.size(), (int)l2);
                l = Math.min(l2, l);
            }
            arrayMap.put((Animator)transitionValuesMaps, new AnimationInfo(view, this.getName(), this, ViewUtils.getWindowId((View)viewGroup), (TransitionValues)object));
            this.mAnimators.add((Animator)transitionValuesMaps);
        }
        if (l != 0L) {
            for (n = 0; n < sparseIntArray.size(); ++n) {
                n2 = sparseIntArray.keyAt(n);
                viewGroup = this.mAnimators.get(n2);
                viewGroup.setStartDelay((long)sparseIntArray.valueAt(n) - l + viewGroup.getStartDelay());
            }
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected void end() {
        --this.mNumInstances;
        if (this.mNumInstances == 0) {
            int n;
            ArrayList arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                arrayList = (ArrayList)this.mListeners.clone();
                int n2 = arrayList.size();
                for (n = 0; n < n2; ++n) {
                    ((TransitionListener)arrayList.get(n)).onTransitionEnd(this);
                }
            }
            for (n = 0; n < this.mStartValues.mItemIdValues.size(); ++n) {
                arrayList = this.mStartValues.mItemIdValues.valueAt(n);
                if (arrayList == null) continue;
                ViewCompat.setHasTransientState((View)arrayList, false);
            }
            for (n = 0; n < this.mEndValues.mItemIdValues.size(); ++n) {
                arrayList = this.mEndValues.mItemIdValues.valueAt(n);
                if (arrayList == null) continue;
                ViewCompat.setHasTransientState((View)arrayList, false);
            }
            this.mEnded = true;
            return;
        }
    }

    @NonNull
    public Transition excludeChildren(@IdRes int n, boolean bl) {
        this.mTargetIdChildExcludes = this.excludeId(this.mTargetIdChildExcludes, n, bl);
        return this;
    }

    @NonNull
    public Transition excludeChildren(@NonNull View view, boolean bl) {
        this.mTargetChildExcludes = this.excludeView(this.mTargetChildExcludes, view, bl);
        return this;
    }

    @NonNull
    public Transition excludeChildren(@NonNull Class class_, boolean bl) {
        this.mTargetTypeChildExcludes = this.excludeType(this.mTargetTypeChildExcludes, class_, bl);
        return this;
    }

    @NonNull
    public Transition excludeTarget(@IdRes int n, boolean bl) {
        this.mTargetIdExcludes = this.excludeId(this.mTargetIdExcludes, n, bl);
        return this;
    }

    @NonNull
    public Transition excludeTarget(@NonNull View view, boolean bl) {
        this.mTargetExcludes = this.excludeView(this.mTargetExcludes, view, bl);
        return this;
    }

    @NonNull
    public Transition excludeTarget(@NonNull Class class_, boolean bl) {
        this.mTargetTypeExcludes = this.excludeType(this.mTargetTypeExcludes, class_, bl);
        return this;
    }

    @NonNull
    public Transition excludeTarget(@NonNull String string2, boolean bl) {
        this.mTargetNameExcludes = Transition.excludeObject(this.mTargetNameExcludes, string2, bl);
        return this;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void forceToEnd(ViewGroup object) {
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        int n = arrayMap.size();
        if (object != null) {
            object = ViewUtils.getWindowId((View)object);
            --n;
            while (n >= 0) {
                AnimationInfo animationInfo = arrayMap.valueAt(n);
                if (animationInfo.mView != null && object != null && object.equals(animationInfo.mWindowId)) {
                    arrayMap.keyAt(n).end();
                }
                --n;
            }
            return;
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    @Nullable
    public Rect getEpicenter() {
        EpicenterCallback epicenterCallback = this.mEpicenterCallback;
        if (epicenterCallback == null) {
            return null;
        }
        return epicenterCallback.onGetEpicenter(this);
    }

    @Nullable
    public EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }

    @Nullable
    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    TransitionValues getMatchedTransitionValues(View arrayList, boolean bl) {
        int n;
        Cloneable cloneable = this.mParent;
        if (cloneable != null) {
            return cloneable.getMatchedTransitionValues((View)arrayList, bl);
        }
        cloneable = bl ? this.mStartValuesList : this.mEndValuesList;
        if (cloneable == null) {
            return null;
        }
        int n2 = cloneable.size();
        int n3 = -1;
        int n4 = 0;
        do {
            n = n3;
            if (n4 >= n2) break;
            TransitionValues transitionValues = (TransitionValues)cloneable.get(n4);
            if (transitionValues == null) {
                return null;
            }
            if (transitionValues.view == arrayList) {
                n = n4;
                break;
            }
            ++n4;
        } while (true);
        if (n >= 0) {
            arrayList = bl ? this.mEndValuesList : this.mStartValuesList;
            return arrayList.get(n);
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
    public TransitionValues getTransitionValues(@NonNull View view, boolean bl) {
        Object object = this.mParent;
        if (object != null) {
            return object.getTransitionValues(view, bl);
        }
        object = bl ? this.mStartValues : this.mEndValues;
        return object.mViewValues.get((Object)view);
    }

    public boolean isTransitionRequired(@Nullable TransitionValues transitionValues, @Nullable TransitionValues transitionValues2) {
        boolean bl = false;
        if (transitionValues != null && transitionValues2 != null) {
            String[] arrstring = this.getTransitionProperties();
            if (arrstring != null) {
                boolean bl2;
                int n = arrstring.length;
                int n2 = 0;
                do {
                    bl2 = bl;
                    if (n2 >= n) break;
                    if (Transition.isValueChanged(transitionValues, transitionValues2, (String)arrstring[n2])) {
                        bl2 = true;
                        break;
                    }
                    ++n2;
                } while (true);
                return bl2;
            }
            arrstring = transitionValues.values.keySet().iterator();
            while (arrstring.hasNext()) {
                if (!Transition.isValueChanged(transitionValues, transitionValues2, (String)arrstring.next())) continue;
                return true;
            }
            return false;
        }
        return false;
    }

    boolean isValidTarget(View view) {
        int n;
        int n2 = view.getId();
        ArrayList arrayList = this.mTargetIdExcludes;
        if (arrayList != null && arrayList.contains(n2)) {
            return false;
        }
        arrayList = this.mTargetExcludes;
        if (arrayList != null && arrayList.contains((Object)view)) {
            return false;
        }
        arrayList = this.mTargetTypeExcludes;
        if (arrayList != null) {
            int n3 = arrayList.size();
            for (n = 0; n < n3; ++n) {
                if (!this.mTargetTypeExcludes.get(n).isInstance((Object)view)) continue;
                return false;
            }
        }
        if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(view) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(view))) {
            return false;
        }
        if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0 && ((arrayList = this.mTargetTypes) == null || arrayList.isEmpty())) {
            arrayList = this.mTargetNames;
            if (arrayList != null) {
                if (arrayList.isEmpty()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        if (!this.mTargetIds.contains(n2)) {
            if (this.mTargets.contains((Object)view)) {
                return true;
            }
            arrayList = this.mTargetNames;
            if (arrayList != null && arrayList.contains(ViewCompat.getTransitionName(view))) {
                return true;
            }
            if (this.mTargetTypes != null) {
                for (n = 0; n < this.mTargetTypes.size(); ++n) {
                    if (!this.mTargetTypes.get(n).isInstance((Object)view)) continue;
                    return true;
                }
                return false;
            }
            return false;
        }
        return true;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void pause(View arrayList) {
        if (!this.mEnded) {
            ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
            int n = arrayMap.size();
            arrayList = ViewUtils.getWindowId((View)arrayList);
            --n;
            while (n >= 0) {
                AnimationInfo animationInfo = arrayMap.valueAt(n);
                if (animationInfo.mView != null && arrayList.equals(animationInfo.mWindowId)) {
                    AnimatorUtils.pause(arrayMap.keyAt(n));
                }
                --n;
            }
            arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                arrayList = (ArrayList)this.mListeners.clone();
                int n2 = arrayList.size();
                for (n = 0; n < n2; ++n) {
                    ((TransitionListener)arrayList.get(n)).onTransitionPause(this);
                }
            }
            this.mPaused = true;
            return;
        }
    }

    void playTransition(ViewGroup viewGroup) {
        this.mStartValuesList = new ArrayList<E>();
        this.mEndValuesList = new ArrayList<E>();
        this.matchStartAndEnd(this.mStartValues, this.mEndValues);
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        int n = arrayMap.size();
        WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)viewGroup);
        --n;
        while (n >= 0) {
            block7 : {
                Animator animator2;
                boolean bl;
                block9 : {
                    block8 : {
                        AnimationInfo animationInfo;
                        animator2 = arrayMap.keyAt(n);
                        if (animator2 == null || (animationInfo = arrayMap.get((Object)animator2)) == null || animationInfo.mView == null || !windowIdImpl.equals(animationInfo.mWindowId)) break block7;
                        TransitionValues transitionValues = animationInfo.mValues;
                        Object object = animationInfo.mView;
                        bl = true;
                        TransitionValues transitionValues2 = this.getTransitionValues((View)object, true);
                        object = this.getMatchedTransitionValues((View)object, true);
                        if (transitionValues2 == null && object == null) break block8;
                        if (animationInfo.mTransition.isTransitionRequired(transitionValues, (TransitionValues)object)) break block9;
                    }
                    bl = false;
                }
                if (bl) {
                    if (!animator2.isRunning() && !animator2.isStarted()) {
                        arrayMap.remove((Object)animator2);
                    } else {
                        animator2.cancel();
                    }
                }
            }
            --n;
        }
        this.createAnimators(viewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        this.runAnimators();
    }

    @NonNull
    public Transition removeListener(@NonNull TransitionListener transitionListener) {
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(transitionListener);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
            return this;
        }
        return this;
    }

    @NonNull
    public Transition removeTarget(@IdRes int n) {
        if (n > 0) {
            this.mTargetIds.remove((Object)n);
            return this;
        }
        return this;
    }

    @NonNull
    public Transition removeTarget(@NonNull View view) {
        this.mTargets.remove((Object)view);
        return this;
    }

    @NonNull
    public Transition removeTarget(@NonNull Class class_) {
        ArrayList<Class> arrayList = this.mTargetTypes;
        if (arrayList != null) {
            arrayList.remove(class_);
            return this;
        }
        return this;
    }

    @NonNull
    public Transition removeTarget(@NonNull String string2) {
        ArrayList<String> arrayList = this.mTargetNames;
        if (arrayList != null) {
            arrayList.remove(string2);
            return this;
        }
        return this;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void resume(View arrayList) {
        if (this.mPaused) {
            if (!this.mEnded) {
                ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
                int n = arrayMap.size();
                arrayList = ViewUtils.getWindowId((View)arrayList);
                --n;
                while (n >= 0) {
                    AnimationInfo animationInfo = arrayMap.valueAt(n);
                    if (animationInfo.mView != null && arrayList.equals(animationInfo.mWindowId)) {
                        AnimatorUtils.resume(arrayMap.keyAt(n));
                    }
                    --n;
                }
                arrayList = this.mListeners;
                if (arrayList != null && arrayList.size() > 0) {
                    arrayList = (ArrayList)this.mListeners.clone();
                    int n2 = arrayList.size();
                    for (n = 0; n < n2; ++n) {
                        ((TransitionListener)arrayList.get(n)).onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected void runAnimators() {
        this.start();
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        for (Animator animator2 : this.mAnimators) {
            if (!arrayMap.containsKey((Object)animator2)) continue;
            this.start();
            this.runAnimator(animator2, arrayMap);
        }
        this.mAnimators.clear();
        this.end();
    }

    void setCanRemoveViews(boolean bl) {
        this.mCanRemoveViews = bl;
    }

    @NonNull
    public Transition setDuration(long l) {
        this.mDuration = l;
        return this;
    }

    public void setEpicenterCallback(@Nullable EpicenterCallback epicenterCallback) {
        this.mEpicenterCallback = epicenterCallback;
    }

    @NonNull
    public Transition setInterpolator(@Nullable TimeInterpolator timeInterpolator) {
        this.mInterpolator = timeInterpolator;
        return this;
    }

    public /* varargs */ void setMatchOrder(int ... arrn) {
        if (arrn != null && arrn.length != 0) {
            for (int i = 0; i < arrn.length; ++i) {
                if (Transition.isValidMatch(arrn[i])) {
                    if (!Transition.alreadyContains(arrn, i)) {
                        continue;
                    }
                    throw new IllegalArgumentException("matches contains a duplicate value");
                }
                throw new IllegalArgumentException("matches contains invalid value");
            }
            this.mMatchOrder = (int[])arrn.clone();
            return;
        }
        this.mMatchOrder = DEFAULT_MATCH_ORDER;
    }

    public void setPathMotion(@Nullable PathMotion pathMotion) {
        if (pathMotion == null) {
            this.mPathMotion = STRAIGHT_PATH_MOTION;
            return;
        }
        this.mPathMotion = pathMotion;
    }

    public void setPropagation(@Nullable TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    Transition setSceneRoot(ViewGroup viewGroup) {
        this.mSceneRoot = viewGroup;
        return this;
    }

    @NonNull
    public Transition setStartDelay(long l) {
        this.mStartDelay = l;
        return this;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected void start() {
        if (this.mNumInstances == 0) {
            ArrayList arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                arrayList = (ArrayList)this.mListeners.clone();
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    ((TransitionListener)arrayList.get(i)).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        ++this.mNumInstances;
    }

    public String toString() {
        return this.toString("");
    }

    String toString(String string2) {
        int n;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(this.getClass().getSimpleName());
        stringBuilder.append("@");
        stringBuilder.append(Integer.toHexString(this.hashCode()));
        stringBuilder.append(": ");
        string2 = stringBuilder.toString();
        if (this.mDuration != -1L) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("dur(");
            stringBuilder.append(this.mDuration);
            stringBuilder.append(") ");
            string2 = stringBuilder.toString();
        }
        if (this.mStartDelay != -1L) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("dly(");
            stringBuilder.append(this.mStartDelay);
            stringBuilder.append(") ");
            string2 = stringBuilder.toString();
        }
        if (this.mInterpolator != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("interp(");
            stringBuilder.append((Object)this.mInterpolator);
            stringBuilder.append(") ");
            string2 = stringBuilder.toString();
        }
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
            return string2;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("tgts(");
        string2 = stringBuilder.toString();
        if (this.mTargetIds.size() > 0) {
            for (n = 0; n < this.mTargetIds.size(); ++n) {
                if (n > 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(string2);
                    stringBuilder.append(", ");
                    string2 = stringBuilder.toString();
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append(this.mTargetIds.get(n));
                string2 = stringBuilder.toString();
            }
        }
        if (this.mTargets.size() > 0) {
            for (n = 0; n < this.mTargets.size(); ++n) {
                if (n > 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(string2);
                    stringBuilder.append(", ");
                    string2 = stringBuilder.toString();
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append((Object)this.mTargets.get(n));
                string2 = stringBuilder.toString();
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private static class AnimationInfo {
        String mName;
        Transition mTransition;
        TransitionValues mValues;
        View mView;
        WindowIdImpl mWindowId;

        AnimationInfo(View view, String string2, Transition transition, WindowIdImpl windowIdImpl, TransitionValues transitionValues) {
            this.mView = view;
            this.mName = string2;
            this.mValues = transitionValues;
            this.mWindowId = windowIdImpl;
            this.mTransition = transition;
        }
    }

    private static class ArrayListManager {
        private ArrayListManager() {
        }

        static <T> ArrayList<T> add(ArrayList<T> arrayList, T t) {
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            if (!arrayList.contains(t)) {
                arrayList.add(t);
                return arrayList;
            }
            return arrayList;
        }

        static <T> ArrayList<T> remove(ArrayList<T> arrayList, T t) {
            if (arrayList != null) {
                arrayList.remove(t);
                if (arrayList.isEmpty()) {
                    return null;
                }
                return arrayList;
            }
            return arrayList;
        }
    }

    public static abstract class EpicenterCallback {
        public abstract Rect onGetEpicenter(@NonNull Transition var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface MatchOrder {
    }

    public static interface TransitionListener {
        public void onTransitionCancel(@NonNull Transition var1);

        public void onTransitionEnd(@NonNull Transition var1);

        public void onTransitionPause(@NonNull Transition var1);

        public void onTransitionResume(@NonNull Transition var1);

        public void onTransitionStart(@NonNull Transition var1);
    }

}

