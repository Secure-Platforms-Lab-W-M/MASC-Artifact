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
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
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
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.transition.AnimatorUtils;
import androidx.transition.PathMotion;
import androidx.transition.Styleable;
import androidx.transition.TransitionPropagation;
import androidx.transition.TransitionSet;
import androidx.transition.TransitionValues;
import androidx.transition.TransitionValuesMaps;
import androidx.transition.ViewUtils;
import androidx.transition.WindowIdImpl;
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
    ArrayList<Animator> mCurrentAnimators;
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
    private ArrayList<Class<?>> mTargetTypeChildExcludes;
    private ArrayList<Class<?>> mTargetTypeExcludes;
    private ArrayList<Class<?>> mTargetTypes;
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
        if (view.getParent() instanceof ListView && (object = (ListView)view.getParent()).getAdapter().hasStableIds()) {
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
            arrayList = new TransitionValues(view);
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
        }
    }

    private ArrayList<Integer> excludeId(ArrayList<Integer> arrayList, int n, boolean bl) {
        ArrayList<Integer> arrayList2 = arrayList;
        if (n > 0) {
            if (bl) {
                return ArrayListManager.add(arrayList, n);
            }
            arrayList2 = ArrayListManager.remove(arrayList, n);
        }
        return arrayList2;
    }

    private static <T> ArrayList<T> excludeObject(ArrayList<T> arrayList, T t, boolean bl) {
        ArrayList<T> arrayList2 = arrayList;
        if (t != null) {
            if (bl) {
                return ArrayListManager.add(arrayList, t);
            }
            arrayList2 = ArrayListManager.remove(arrayList, t);
        }
        return arrayList2;
    }

    private ArrayList<Class<?>> excludeType(ArrayList<Class<?>> arrayList, Class<?> class_, boolean bl) {
        ArrayList arrayList2 = arrayList;
        if (class_ != null) {
            if (bl) {
                return ArrayListManager.add(arrayList, class_);
            }
            arrayList2 = ArrayListManager.remove(arrayList, class_);
        }
        return arrayList2;
    }

    private ArrayList<View> excludeView(ArrayList<View> arrayList, View view, boolean bl) {
        ArrayList<View> arrayList2 = arrayList;
        if (view != null) {
            if (bl) {
                return ArrayListManager.add(arrayList, view);
            }
            arrayList2 = ArrayListManager.remove(arrayList, view);
        }
        return arrayList2;
    }

    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap<Animator, AnimationInfo> arrayMap;
        ArrayMap arrayMap2 = arrayMap = sRunningAnimators.get();
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap();
            sRunningAnimators.set(arrayMap2);
        }
        return arrayMap2;
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
            if (object == null || !this.isValidTarget((View)object) || (object = arrayMap2.remove(object)) == null || !this.isValidTarget(object.view)) continue;
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
        for (int i = 0; i < (arrn = this.mMatchOrder).length; ++i) {
            int n = arrn[i];
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        if (n != 4) continue;
                        this.matchItemIds(arrayMap, arrayMap2, transitionValuesMaps.mItemIdValues, transitionValuesMaps2.mItemIdValues);
                        continue;
                    }
                    this.matchIds(arrayMap, arrayMap2, transitionValuesMaps.mIdValues, transitionValuesMaps2.mIdValues);
                    continue;
                }
                this.matchNames(arrayMap, arrayMap2, transitionValuesMaps.mNameValues, transitionValuesMaps2.mNameValues);
                continue;
            }
            this.matchInstances(arrayMap, arrayMap2);
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

    private void runAnimator(Animator animator, final ArrayMap<Animator, AnimationInfo> arrayMap) {
        if (animator != null) {
            animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator animator) {
                    arrayMap.remove((Object)animator);
                    Transition.this.mCurrentAnimators.remove((Object)animator);
                }

                public void onAnimationStart(Animator animator) {
                    Transition.this.mCurrentAnimators.add(animator);
                }
            });
            this.animate(animator);
        }
    }

    public Transition addListener(TransitionListener transitionListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(transitionListener);
        return this;
    }

    public Transition addTarget(int n) {
        if (n != 0) {
            this.mTargetIds.add(n);
        }
        return this;
    }

    public Transition addTarget(View view) {
        this.mTargets.add(view);
        return this;
    }

    public Transition addTarget(Class<?> class_) {
        if (this.mTargetTypes == null) {
            this.mTargetTypes = new ArrayList();
        }
        this.mTargetTypes.add(class_);
        return this;
    }

    public Transition addTarget(String string2) {
        if (this.mTargetNames == null) {
            this.mTargetNames = new ArrayList();
        }
        this.mTargetNames.add(string2);
        return this;
    }

    protected void animate(Animator animator) {
        if (animator == null) {
            this.end();
            return;
        }
        if (this.getDuration() >= 0L) {
            animator.setDuration(this.getDuration());
        }
        if (this.getStartDelay() >= 0L) {
            animator.setStartDelay(this.getStartDelay() + animator.getStartDelay());
        }
        if (this.getInterpolator() != null) {
            animator.setInterpolator(this.getInterpolator());
        }
        animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator) {
                Transition.this.end();
                animator.removeListener((Animator.AnimatorListener)this);
            }
        });
        animator.start();
    }

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
        }
    }

    public abstract void captureEndValues(TransitionValues var1);

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
            }
        }
    }

    public abstract void captureStartValues(TransitionValues var1);

    void captureValues(ViewGroup view, boolean bl) {
        Object object;
        Object object2;
        int n;
        this.clearValues(bl);
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0 || (object = this.mTargetNames) != null && !object.isEmpty() || (object = this.mTargetTypes) != null && !object.isEmpty()) {
            this.captureHierarchy(view, bl);
        } else {
            for (n = 0; n < this.mTargetIds.size(); ++n) {
                object = view.findViewById(this.mTargetIds.get(n).intValue());
                if (object == null) continue;
                object2 = new TransitionValues((View)object);
                if (bl) {
                    this.captureStartValues((TransitionValues)object2);
                } else {
                    this.captureEndValues((TransitionValues)object2);
                }
                object2.mTargetedTransitions.add(this);
                this.capturePropagationValues((TransitionValues)object2);
                if (bl) {
                    Transition.addViewValues(this.mStartValues, (View)object, (TransitionValues)object2);
                    continue;
                }
                Transition.addViewValues(this.mEndValues, (View)object, (TransitionValues)object2);
            }
            for (n = 0; n < this.mTargets.size(); ++n) {
                view = this.mTargets.get(n);
                object = new TransitionValues(view);
                if (bl) {
                    this.captureStartValues((TransitionValues)object);
                } else {
                    this.captureEndValues((TransitionValues)object);
                }
                object.mTargetedTransitions.add(this);
                this.capturePropagationValues((TransitionValues)object);
                if (bl) {
                    Transition.addViewValues(this.mStartValues, view, (TransitionValues)object);
                    continue;
                }
                Transition.addViewValues(this.mEndValues, view, (TransitionValues)object);
            }
        }
        if (!bl && (view = this.mNameOverrides) != null) {
            int n2 = view.size();
            view = new View(n2);
            for (n = 0; n < n2; ++n) {
                object = this.mNameOverrides.keyAt(n);
                view.add(this.mStartValues.mNameValues.remove(object));
            }
            for (n = 0; n < n2; ++n) {
                object = (View)view.get(n);
                if (object == null) continue;
                object2 = this.mNameOverrides.valueAt(n);
                this.mStartValues.mNameValues.put((String)object2, (View)object);
            }
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

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    protected void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        int n;
        int n2;
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        long l = Long.MAX_VALUE;
        SparseIntArray sparseIntArray = new SparseIntArray();
        int n3 = arrayList.size();
        for (n = 0; n < n3; ++n) {
            long l2;
            TransitionValues transitionValues = arrayList.get(n);
            TransitionValues transitionValues2 = arrayList2.get(n);
            if (transitionValues != null && !transitionValues.mTargetedTransitions.contains(this)) {
                transitionValues = null;
            }
            if (transitionValues2 != null && !transitionValues2.mTargetedTransitions.contains(this)) {
                transitionValues2 = null;
            }
            if (transitionValues == null && transitionValues2 == null) {
                l2 = l;
            } else {
                n2 = transitionValues != null && transitionValues2 != null && !this.isTransitionRequired(transitionValues, transitionValues2) ? 0 : 1;
                if (n2 != 0) {
                    transitionValuesMaps = this.createAnimator(viewGroup, transitionValues, transitionValues2);
                    if (transitionValuesMaps != null) {
                        Object object;
                        Object object2;
                        TransitionValues transitionValues3 = null;
                        if (transitionValues2 != null) {
                            object2 = transitionValues2.view;
                            String[] arrstring = this.getTransitionProperties();
                            if (arrstring != null && arrstring.length > 0) {
                                transitionValues3 = new TransitionValues((View)object2);
                                object = transitionValuesMaps2.mViewValues.get(object2);
                                if (object != null) {
                                    for (n2 = 0; n2 < arrstring.length; ++n2) {
                                        transitionValues3.values.put(arrstring[n2], object.values.get(arrstring[n2]));
                                    }
                                }
                                n2 = arrayMap.size();
                                for (int i = 0; i < n2; ++i) {
                                    object = arrayMap.get((Object)arrayMap.keyAt(i));
                                    if (object.mValues == null || object.mView != object2 || !object.mName.equals(this.getName()) || !object.mValues.equals(transitionValues3)) continue;
                                    transitionValuesMaps = null;
                                    break;
                                }
                            }
                            object = object2;
                            n2 = n;
                        } else {
                            object = transitionValues.view;
                            transitionValues3 = null;
                            n2 = n;
                        }
                        l2 = l;
                        n = n2;
                        if (transitionValuesMaps != null) {
                            object2 = this.mPropagation;
                            if (object2 != null) {
                                l2 = object2.getStartDelay(viewGroup, this, transitionValues, transitionValues2);
                                sparseIntArray.put(this.mAnimators.size(), (int)l2);
                                l = Math.min(l2, l);
                            }
                            arrayMap.put((Animator)transitionValuesMaps, new AnimationInfo((View)object, this.getName(), this, ViewUtils.getWindowId((View)viewGroup), transitionValues3));
                            this.mAnimators.add((Animator)transitionValuesMaps);
                            l2 = l;
                            n = n2;
                        }
                    } else {
                        l2 = l;
                    }
                } else {
                    l2 = l;
                }
            }
            l = l2;
        }
        if (sparseIntArray.size() != 0) {
            for (n = 0; n < sparseIntArray.size(); ++n) {
                n2 = sparseIntArray.keyAt(n);
                viewGroup = this.mAnimators.get(n2);
                viewGroup.setStartDelay((long)sparseIntArray.valueAt(n) - l + viewGroup.getStartDelay());
            }
        }
    }

    protected void end() {
        int n;
        this.mNumInstances = n = this.mNumInstances - 1;
        if (n == 0) {
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
        }
    }

    public Transition excludeChildren(int n, boolean bl) {
        this.mTargetIdChildExcludes = this.excludeId(this.mTargetIdChildExcludes, n, bl);
        return this;
    }

    public Transition excludeChildren(View view, boolean bl) {
        this.mTargetChildExcludes = this.excludeView(this.mTargetChildExcludes, view, bl);
        return this;
    }

    public Transition excludeChildren(Class<?> class_, boolean bl) {
        this.mTargetTypeChildExcludes = this.excludeType(this.mTargetTypeChildExcludes, class_, bl);
        return this;
    }

    public Transition excludeTarget(int n, boolean bl) {
        this.mTargetIdExcludes = this.excludeId(this.mTargetIdExcludes, n, bl);
        return this;
    }

    public Transition excludeTarget(View view, boolean bl) {
        this.mTargetExcludes = this.excludeView(this.mTargetExcludes, view, bl);
        return this;
    }

    public Transition excludeTarget(Class<?> class_, boolean bl) {
        this.mTargetTypeExcludes = this.excludeType(this.mTargetTypeExcludes, class_, bl);
        return this;
    }

    public Transition excludeTarget(String string2, boolean bl) {
        this.mTargetNameExcludes = Transition.excludeObject(this.mTargetNameExcludes, string2, bl);
        return this;
    }

    void forceToEnd(ViewGroup object) {
        Object object2 = Transition.getRunningAnimators();
        int n = object2.size();
        if (object != null) {
            if (n == 0) {
                return;
            }
            object = ViewUtils.getWindowId((View)object);
            ArrayMap<K, V> arrayMap = new ArrayMap<K, V>((SimpleArrayMap)object2);
            object2.clear();
            --n;
            while (n >= 0) {
                object2 = (AnimationInfo)arrayMap.valueAt(n);
                if (object2.mView != null && object != null && object.equals(object2.mWindowId)) {
                    ((Animator)arrayMap.keyAt(n)).end();
                }
                --n;
            }
            return;
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    public Rect getEpicenter() {
        EpicenterCallback epicenterCallback = this.mEpicenterCallback;
        if (epicenterCallback == null) {
            return null;
        }
        return epicenterCallback.onGetEpicenter(this);
    }

    public EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }

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
        arrayList = null;
        if (n >= 0) {
            arrayList = bl ? this.mEndValuesList : this.mStartValuesList;
            arrayList = arrayList.get(n);
        }
        return arrayList;
    }

    public String getName() {
        return this.mName;
    }

    public PathMotion getPathMotion() {
        return this.mPathMotion;
    }

    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public List<Integer> getTargetIds() {
        return this.mTargetIds;
    }

    public List<String> getTargetNames() {
        return this.mTargetNames;
    }

    public List<Class<?>> getTargetTypes() {
        return this.mTargetTypes;
    }

    public List<View> getTargets() {
        return this.mTargets;
    }

    public String[] getTransitionProperties() {
        return null;
    }

    public TransitionValues getTransitionValues(View view, boolean bl) {
        Object object = this.mParent;
        if (object != null) {
            return object.getTransitionValues(view, bl);
        }
        object = bl ? this.mStartValues : this.mEndValues;
        return object.mViewValues.get((Object)view);
    }

    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
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
        if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0 && ((arrayList = this.mTargetTypes) == null || arrayList.isEmpty()) && ((arrayList = this.mTargetNames) == null || arrayList.isEmpty())) {
            return true;
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
            }
            return false;
        }
        return true;
    }

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
            AnimationInfo animationInfo;
            Animator animator = arrayMap.keyAt(n);
            if (animator != null && (animationInfo = arrayMap.get((Object)animator)) != null && animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId)) {
                TransitionValues transitionValues;
                TransitionValues transitionValues2 = animationInfo.mValues;
                View view = animationInfo.mView;
                boolean bl = true;
                TransitionValues transitionValues3 = this.getTransitionValues(view, true);
                TransitionValues transitionValues4 = transitionValues = this.getMatchedTransitionValues(view, true);
                if (transitionValues3 == null) {
                    transitionValues4 = transitionValues;
                    if (transitionValues == null) {
                        transitionValues4 = this.mEndValues.mViewValues.get((Object)view);
                    }
                }
                if (transitionValues3 == null && transitionValues4 == null || !animationInfo.mTransition.isTransitionRequired(transitionValues2, transitionValues4)) {
                    bl = false;
                }
                if (bl) {
                    if (!animator.isRunning() && !animator.isStarted()) {
                        arrayMap.remove((Object)animator);
                    } else {
                        animator.cancel();
                    }
                }
            }
            --n;
        }
        this.createAnimators(viewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        this.runAnimators();
    }

    public Transition removeListener(TransitionListener transitionListener) {
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(transitionListener);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
        }
        return this;
    }

    public Transition removeTarget(int n) {
        if (n != 0) {
            this.mTargetIds.remove((Object)n);
        }
        return this;
    }

    public Transition removeTarget(View view) {
        this.mTargets.remove((Object)view);
        return this;
    }

    public Transition removeTarget(Class<?> class_) {
        ArrayList<Class<?>> arrayList = this.mTargetTypes;
        if (arrayList != null) {
            arrayList.remove(class_);
        }
        return this;
    }

    public Transition removeTarget(String string2) {
        ArrayList<String> arrayList = this.mTargetNames;
        if (arrayList != null) {
            arrayList.remove(string2);
        }
        return this;
    }

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
        }
    }

    protected void runAnimators() {
        this.start();
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        for (Animator animator : this.mAnimators) {
            if (!arrayMap.containsKey((Object)animator)) continue;
            this.start();
            this.runAnimator(animator, arrayMap);
        }
        this.mAnimators.clear();
        this.end();
    }

    void setCanRemoveViews(boolean bl) {
        this.mCanRemoveViews = bl;
    }

    public Transition setDuration(long l) {
        this.mDuration = l;
        return this;
    }

    public void setEpicenterCallback(EpicenterCallback epicenterCallback) {
        this.mEpicenterCallback = epicenterCallback;
    }

    public Transition setInterpolator(TimeInterpolator timeInterpolator) {
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

    public void setPathMotion(PathMotion pathMotion) {
        if (pathMotion == null) {
            this.mPathMotion = STRAIGHT_PATH_MOTION;
            return;
        }
        this.mPathMotion = pathMotion;
    }

    public void setPropagation(TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    Transition setSceneRoot(ViewGroup viewGroup) {
        this.mSceneRoot = viewGroup;
        return this;
    }

    public Transition setStartDelay(long l) {
        this.mStartDelay = l;
        return this;
    }

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

    String toString(String charSequence) {
        CharSequence charSequence22;
        block13 : {
            int n;
            CharSequence charSequence22;
            block12 : {
                charSequence22 = new StringBuilder();
                charSequence22.append((String)charSequence);
                charSequence22.append(this.getClass().getSimpleName());
                charSequence22.append("@");
                charSequence22.append(Integer.toHexString(this.hashCode()));
                charSequence22.append(": ");
                charSequence22 = charSequence22.toString();
                charSequence = charSequence22;
                if (this.mDuration != -1L) {
                    charSequence = new StringBuilder();
                    charSequence.append((String)charSequence22);
                    charSequence.append("dur(");
                    charSequence.append(this.mDuration);
                    charSequence.append(") ");
                    charSequence = charSequence.toString();
                }
                charSequence22 = charSequence;
                if (this.mStartDelay != -1L) {
                    charSequence22 = new StringBuilder();
                    charSequence22.append((String)charSequence);
                    charSequence22.append("dly(");
                    charSequence22.append(this.mStartDelay);
                    charSequence22.append(") ");
                    charSequence22 = charSequence22.toString();
                }
                charSequence = charSequence22;
                if (this.mInterpolator != null) {
                    charSequence = new StringBuilder();
                    charSequence.append((String)charSequence22);
                    charSequence.append("interp(");
                    charSequence.append((Object)this.mInterpolator);
                    charSequence.append(") ");
                    charSequence = charSequence.toString();
                }
                if (this.mTargetIds.size() > 0) break block12;
                charSequence22 = charSequence;
                if (this.mTargets.size() <= 0) break block13;
            }
            charSequence22 = new StringBuilder();
            charSequence22.append((String)charSequence);
            charSequence22.append("tgts(");
            charSequence = charSequence22.toString();
            charSequence22 = charSequence;
            if (this.mTargetIds.size() > 0) {
                n = 0;
                do {
                    charSequence22 = charSequence;
                    if (n >= this.mTargetIds.size()) break;
                    charSequence22 = charSequence;
                    if (n > 0) {
                        charSequence22 = new StringBuilder();
                        charSequence22.append((String)charSequence);
                        charSequence22.append(", ");
                        charSequence22 = charSequence22.toString();
                    }
                    charSequence = new StringBuilder();
                    charSequence.append((String)charSequence22);
                    charSequence.append(this.mTargetIds.get(n));
                    charSequence = charSequence.toString();
                    ++n;
                } while (true);
            }
            charSequence = charSequence22;
            if (this.mTargets.size() > 0) {
                n = 0;
                do {
                    charSequence = charSequence22;
                    if (n >= this.mTargets.size()) break;
                    charSequence = charSequence22;
                    if (n > 0) {
                        charSequence = new StringBuilder();
                        charSequence.append((String)charSequence22);
                        charSequence.append(", ");
                        charSequence = charSequence.toString();
                    }
                    charSequence22 = new StringBuilder();
                    charSequence22.append((String)charSequence);
                    charSequence22.append((Object)this.mTargets.get(n));
                    charSequence22 = charSequence22.toString();
                    ++n;
                } while (true);
            }
            charSequence22 = new StringBuilder();
            charSequence22.append((String)charSequence);
            charSequence22.append(")");
            charSequence22 = charSequence22.toString();
        }
        return charSequence22;
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
            ArrayList arrayList2 = arrayList;
            if (arrayList == null) {
                arrayList2 = new ArrayList();
            }
            if (!arrayList2.contains(t)) {
                arrayList2.add(t);
            }
            return arrayList2;
        }

        static <T> ArrayList<T> remove(ArrayList<T> arrayList, T t) {
            ArrayList<T> arrayList2 = arrayList;
            if (arrayList != null) {
                arrayList.remove(t);
                arrayList2 = arrayList;
                if (arrayList.isEmpty()) {
                    arrayList2 = null;
                }
            }
            return arrayList2;
        }
    }

    public static abstract class EpicenterCallback {
        public abstract Rect onGetEpicenter(Transition var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface MatchOrder {
    }

    public static interface TransitionListener {
        public void onTransitionCancel(Transition var1);

        public void onTransitionEnd(Transition var1);

        public void onTransitionPause(Transition var1);

        public void onTransitionResume(Transition var1);

        public void onTransitionStart(Transition var1);
    }

}

