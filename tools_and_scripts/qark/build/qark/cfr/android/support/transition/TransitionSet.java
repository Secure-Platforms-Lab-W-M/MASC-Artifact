/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.util.AndroidRuntimeException
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.transition.PathMotion;
import android.support.transition.Styleable;
import android.support.transition.Transition;
import android.support.transition.TransitionListenerAdapter;
import android.support.transition.TransitionValues;
import android.support.transition.TransitionValuesMaps;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class TransitionSet
extends Transition {
    public static final int ORDERING_SEQUENTIAL = 1;
    public static final int ORDERING_TOGETHER = 0;
    private int mCurrentListeners;
    private boolean mPlayTogether = true;
    private boolean mStarted = false;
    private ArrayList<Transition> mTransitions = new ArrayList();

    public TransitionSet() {
    }

    public TransitionSet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.TRANSITION_SET);
        this.setOrdering(TypedArrayUtils.getNamedInt((TypedArray)context, (XmlPullParser)((XmlResourceParser)attributeSet), "transitionOrdering", 0, 0));
        context.recycle();
    }

    static /* synthetic */ int access$106(TransitionSet transitionSet) {
        int n;
        transitionSet.mCurrentListeners = n = transitionSet.mCurrentListeners - 1;
        return n;
    }

    private void setupStartEndListeners() {
        TransitionSetListener transitionSetListener = new TransitionSetListener(this);
        Iterator<Transition> iterator = this.mTransitions.iterator();
        while (iterator.hasNext()) {
            iterator.next().addListener(transitionSetListener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }

    @NonNull
    @Override
    public TransitionSet addListener(@NonNull Transition.TransitionListener transitionListener) {
        return (TransitionSet)super.addListener(transitionListener);
    }

    @NonNull
    @Override
    public TransitionSet addTarget(@IdRes int n) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(n);
        }
        return (TransitionSet)super.addTarget(n);
    }

    @NonNull
    @Override
    public TransitionSet addTarget(@NonNull View view) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(view);
        }
        return (TransitionSet)super.addTarget(view);
    }

    @NonNull
    @Override
    public TransitionSet addTarget(@NonNull Class class_) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(class_);
        }
        return (TransitionSet)super.addTarget(class_);
    }

    @NonNull
    @Override
    public TransitionSet addTarget(@NonNull String string2) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(string2);
        }
        return (TransitionSet)super.addTarget(string2);
    }

    @NonNull
    public TransitionSet addTransition(@NonNull Transition transition) {
        this.mTransitions.add(transition);
        transition.mParent = this;
        if (this.mDuration >= 0L) {
            transition.setDuration(this.mDuration);
            return this;
        }
        return this;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    protected void cancel() {
        super.cancel();
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).cancel();
        }
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (Transition transition : this.mTransitions) {
                if (!transition.isValidTarget(transitionValues.view)) continue;
                transition.captureEndValues(transitionValues);
                transitionValues.mTargetedTransitions.add(transition);
            }
            return;
        }
    }

    @Override
    void capturePropagationValues(TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).capturePropagationValues(transitionValues);
        }
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (Transition transition : this.mTransitions) {
                if (!transition.isValidTarget(transitionValues.view)) continue;
                transition.captureStartValues(transitionValues);
                transitionValues.mTargetedTransitions.add(transition);
            }
            return;
        }
    }

    @Override
    public Transition clone() {
        TransitionSet transitionSet = (TransitionSet)super.clone();
        transitionSet.mTransitions = new ArrayList();
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            transitionSet.addTransition(this.mTransitions.get(i).clone());
        }
        return transitionSet;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    protected void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        long l = this.getStartDelay();
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            Transition transition = this.mTransitions.get(i);
            if (l > 0L && (this.mPlayTogether || i == 0)) {
                long l2 = transition.getStartDelay();
                if (l2 > 0L) {
                    transition.setStartDelay(l + l2);
                } else {
                    transition.setStartDelay(l);
                }
            }
            transition.createAnimators(viewGroup, transitionValuesMaps, transitionValuesMaps2, arrayList, arrayList2);
        }
    }

    @NonNull
    @Override
    public Transition excludeTarget(int n, boolean bl) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(n, bl);
        }
        return super.excludeTarget(n, bl);
    }

    @NonNull
    @Override
    public Transition excludeTarget(@NonNull View view, boolean bl) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(view, bl);
        }
        return super.excludeTarget(view, bl);
    }

    @NonNull
    @Override
    public Transition excludeTarget(@NonNull Class class_, boolean bl) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(class_, bl);
        }
        return super.excludeTarget(class_, bl);
    }

    @NonNull
    @Override
    public Transition excludeTarget(@NonNull String string2, boolean bl) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(string2, bl);
        }
        return super.excludeTarget(string2, bl);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    void forceToEnd(ViewGroup viewGroup) {
        super.forceToEnd(viewGroup);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).forceToEnd(viewGroup);
        }
    }

    public int getOrdering() {
        return this.mPlayTogether ^ true;
    }

    public Transition getTransitionAt(int n) {
        if (n >= 0 && n < this.mTransitions.size()) {
            return this.mTransitions.get(n);
        }
        return null;
    }

    public int getTransitionCount() {
        return this.mTransitions.size();
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public void pause(View view) {
        super.pause(view);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).pause(view);
        }
    }

    @NonNull
    @Override
    public TransitionSet removeListener(@NonNull Transition.TransitionListener transitionListener) {
        return (TransitionSet)super.removeListener(transitionListener);
    }

    @NonNull
    @Override
    public TransitionSet removeTarget(@IdRes int n) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(n);
        }
        return (TransitionSet)super.removeTarget(n);
    }

    @NonNull
    @Override
    public TransitionSet removeTarget(@NonNull View view) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(view);
        }
        return (TransitionSet)super.removeTarget(view);
    }

    @NonNull
    @Override
    public TransitionSet removeTarget(@NonNull Class class_) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(class_);
        }
        return (TransitionSet)super.removeTarget(class_);
    }

    @NonNull
    @Override
    public TransitionSet removeTarget(@NonNull String string2) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(string2);
        }
        return (TransitionSet)super.removeTarget(string2);
    }

    @NonNull
    public TransitionSet removeTransition(@NonNull Transition transition) {
        this.mTransitions.remove(transition);
        transition.mParent = null;
        return this;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public void resume(View view) {
        super.resume(view);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).resume(view);
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    protected void runAnimators() {
        if (this.mTransitions.isEmpty()) {
            this.start();
            this.end();
            return;
        }
        this.setupStartEndListeners();
        if (!this.mPlayTogether) {
            for (int i = 1; i < this.mTransitions.size(); ++i) {
                this.mTransitions.get(i - 1).addListener(new TransitionListenerAdapter(this.mTransitions.get(i)){
                    final /* synthetic */ Transition val$nextTransition;
                    {
                        this.val$nextTransition = transition;
                    }

                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        this.val$nextTransition.runAnimators();
                        transition.removeListener(this);
                    }
                });
            }
            Transition transition = this.mTransitions.get(0);
            if (transition != null) {
                transition.runAnimators();
            }
            return;
        }
        Iterator<Transition> iterator = this.mTransitions.iterator();
        while (iterator.hasNext()) {
            iterator.next().runAnimators();
        }
    }

    @Override
    void setCanRemoveViews(boolean bl) {
        super.setCanRemoveViews(bl);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).setCanRemoveViews(bl);
        }
    }

    @NonNull
    @Override
    public TransitionSet setDuration(long l) {
        super.setDuration(l);
        if (this.mDuration >= 0L) {
            int n = this.mTransitions.size();
            for (int i = 0; i < n; ++i) {
                this.mTransitions.get(i).setDuration(l);
            }
            return this;
        }
        return this;
    }

    @Override
    public void setEpicenterCallback(Transition.EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).setEpicenterCallback(epicenterCallback);
        }
    }

    @NonNull
    @Override
    public TransitionSet setInterpolator(@Nullable TimeInterpolator timeInterpolator) {
        return (TransitionSet)super.setInterpolator(timeInterpolator);
    }

    @NonNull
    public TransitionSet setOrdering(int n) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid parameter for TransitionSet ordering: ");
                stringBuilder.append(n);
                throw new AndroidRuntimeException(stringBuilder.toString());
            }
            case 1: {
                this.mPlayTogether = false;
                return this;
            }
            case 0: 
        }
        this.mPlayTogether = true;
        return this;
    }

    @Override
    public void setPathMotion(PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).setPathMotion(pathMotion);
        }
    }

    @Override
    TransitionSet setSceneRoot(ViewGroup viewGroup) {
        super.setSceneRoot(viewGroup);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).setSceneRoot(viewGroup);
        }
        return this;
    }

    @NonNull
    @Override
    public TransitionSet setStartDelay(long l) {
        return (TransitionSet)super.setStartDelay(l);
    }

    @Override
    String toString(String string2) {
        Object object = super.toString(string2);
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append("\n");
            object = this.mTransitions.get(i);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(string2);
            stringBuilder2.append("  ");
            stringBuilder.append(object.toString(stringBuilder2.toString()));
            object = stringBuilder.toString();
        }
        return object;
    }

    static class TransitionSetListener
    extends TransitionListenerAdapter {
        TransitionSet mTransitionSet;

        TransitionSetListener(TransitionSet transitionSet) {
            this.mTransitionSet = transitionSet;
        }

        @Override
        public void onTransitionEnd(@NonNull Transition transition) {
            TransitionSet.access$106(this.mTransitionSet);
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.removeListener(this);
        }

        @Override
        public void onTransitionStart(@NonNull Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
                return;
            }
        }
    }

}

