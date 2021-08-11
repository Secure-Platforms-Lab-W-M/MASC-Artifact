// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.util.AndroidRuntimeException;
import android.support.annotation.Nullable;
import android.animation.TimeInterpolator;
import android.view.ViewGroup;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import java.util.Iterator;
import android.content.res.TypedArray;
import org.xmlpull.v1.XmlPullParser;
import android.support.v4.content.res.TypedArrayUtils;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.content.Context;
import java.util.ArrayList;

public class TransitionSet extends Transition
{
    public static final int ORDERING_SEQUENTIAL = 1;
    public static final int ORDERING_TOGETHER = 0;
    private int mCurrentListeners;
    private boolean mPlayTogether;
    private boolean mStarted;
    private ArrayList<Transition> mTransitions;
    
    public TransitionSet() {
        this.mTransitions = new ArrayList<Transition>();
        this.mPlayTogether = true;
        this.mStarted = false;
    }
    
    public TransitionSet(final Context context, final AttributeSet set) {
        super(context, set);
        this.mTransitions = new ArrayList<Transition>();
        this.mPlayTogether = true;
        this.mStarted = false;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, Styleable.TRANSITION_SET);
        this.setOrdering(TypedArrayUtils.getNamedInt(obtainStyledAttributes, (XmlPullParser)set, "transitionOrdering", 0, 0));
        obtainStyledAttributes.recycle();
    }
    
    private void setupStartEndListeners() {
        final TransitionSetListener transitionSetListener = new TransitionSetListener(this);
        final Iterator<Transition> iterator = this.mTransitions.iterator();
        while (iterator.hasNext()) {
            iterator.next().addListener((TransitionListener)transitionSetListener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }
    
    @NonNull
    @Override
    public TransitionSet addListener(@NonNull final TransitionListener transitionListener) {
        return (TransitionSet)super.addListener(transitionListener);
    }
    
    @NonNull
    @Override
    public TransitionSet addTarget(@IdRes final int n) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(n);
        }
        return (TransitionSet)super.addTarget(n);
    }
    
    @NonNull
    @Override
    public TransitionSet addTarget(@NonNull final View view) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(view);
        }
        return (TransitionSet)super.addTarget(view);
    }
    
    @NonNull
    @Override
    public TransitionSet addTarget(@NonNull final Class clazz) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(clazz);
        }
        return (TransitionSet)super.addTarget(clazz);
    }
    
    @NonNull
    @Override
    public TransitionSet addTarget(@NonNull final String s) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(s);
        }
        return (TransitionSet)super.addTarget(s);
    }
    
    @NonNull
    public TransitionSet addTransition(@NonNull final Transition transition) {
        this.mTransitions.add(transition);
        transition.mParent = this;
        if (this.mDuration >= 0L) {
            transition.setDuration(this.mDuration);
            return this;
        }
        return this;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    protected void cancel() {
        super.cancel();
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).cancel();
        }
    }
    
    @Override
    public void captureEndValues(@NonNull final TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (final Transition transition : this.mTransitions) {
                if (transition.isValidTarget(transitionValues.view)) {
                    transition.captureEndValues(transitionValues);
                    transitionValues.mTargetedTransitions.add(transition);
                }
            }
        }
    }
    
    @Override
    void capturePropagationValues(final TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).capturePropagationValues(transitionValues);
        }
    }
    
    @Override
    public void captureStartValues(@NonNull final TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (final Transition transition : this.mTransitions) {
                if (transition.isValidTarget(transitionValues.view)) {
                    transition.captureStartValues(transitionValues);
                    transitionValues.mTargetedTransitions.add(transition);
                }
            }
        }
    }
    
    @Override
    public Transition clone() {
        final TransitionSet set = (TransitionSet)super.clone();
        set.mTransitions = new ArrayList<Transition>();
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            set.addTransition(this.mTransitions.get(i).clone());
        }
        return set;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    protected void createAnimators(final ViewGroup viewGroup, final TransitionValuesMaps transitionValuesMaps, final TransitionValuesMaps transitionValuesMaps2, final ArrayList<TransitionValues> list, final ArrayList<TransitionValues> list2) {
        final long startDelay = this.getStartDelay();
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            final Transition transition = this.mTransitions.get(i);
            if (startDelay > 0L && (this.mPlayTogether || i == 0)) {
                final long startDelay2 = transition.getStartDelay();
                if (startDelay2 > 0L) {
                    transition.setStartDelay(startDelay + startDelay2);
                }
                else {
                    transition.setStartDelay(startDelay);
                }
            }
            transition.createAnimators(viewGroup, transitionValuesMaps, transitionValuesMaps2, list, list2);
        }
    }
    
    @NonNull
    @Override
    public Transition excludeTarget(final int n, final boolean b) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(n, b);
        }
        return super.excludeTarget(n, b);
    }
    
    @NonNull
    @Override
    public Transition excludeTarget(@NonNull final View view, final boolean b) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(view, b);
        }
        return super.excludeTarget(view, b);
    }
    
    @NonNull
    @Override
    public Transition excludeTarget(@NonNull final Class clazz, final boolean b) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(clazz, b);
        }
        return super.excludeTarget(clazz, b);
    }
    
    @NonNull
    @Override
    public Transition excludeTarget(@NonNull final String s, final boolean b) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(s, b);
        }
        return super.excludeTarget(s, b);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    void forceToEnd(final ViewGroup viewGroup) {
        super.forceToEnd(viewGroup);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).forceToEnd(viewGroup);
        }
    }
    
    public int getOrdering() {
        return (this.mPlayTogether ^ true) ? 1 : 0;
    }
    
    public Transition getTransitionAt(final int n) {
        if (n >= 0 && n < this.mTransitions.size()) {
            return this.mTransitions.get(n);
        }
        return null;
    }
    
    public int getTransitionCount() {
        return this.mTransitions.size();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public void pause(final View view) {
        super.pause(view);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).pause(view);
        }
    }
    
    @NonNull
    @Override
    public TransitionSet removeListener(@NonNull final TransitionListener transitionListener) {
        return (TransitionSet)super.removeListener(transitionListener);
    }
    
    @NonNull
    @Override
    public TransitionSet removeTarget(@IdRes final int n) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(n);
        }
        return (TransitionSet)super.removeTarget(n);
    }
    
    @NonNull
    @Override
    public TransitionSet removeTarget(@NonNull final View view) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(view);
        }
        return (TransitionSet)super.removeTarget(view);
    }
    
    @NonNull
    @Override
    public TransitionSet removeTarget(@NonNull final Class clazz) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(clazz);
        }
        return (TransitionSet)super.removeTarget(clazz);
    }
    
    @NonNull
    @Override
    public TransitionSet removeTarget(@NonNull final String s) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(s);
        }
        return (TransitionSet)super.removeTarget(s);
    }
    
    @NonNull
    public TransitionSet removeTransition(@NonNull final Transition transition) {
        this.mTransitions.remove(transition);
        transition.mParent = null;
        return this;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public void resume(final View view) {
        super.resume(view);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).resume(view);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
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
                this.mTransitions.get(i - 1).addListener((TransitionListener)new TransitionListenerAdapter() {
                    final /* synthetic */ Transition val$nextTransition = TransitionSet.this.mTransitions.get(i);
                    
                    @Override
                    public void onTransitionEnd(@NonNull final Transition transition) {
                        this.val$nextTransition.runAnimators();
                        transition.removeListener((TransitionListener)this);
                    }
                });
            }
            final Transition transition = this.mTransitions.get(0);
            if (transition != null) {
                transition.runAnimators();
            }
            return;
        }
        final Iterator<Transition> iterator = this.mTransitions.iterator();
        while (iterator.hasNext()) {
            iterator.next().runAnimators();
        }
    }
    
    @Override
    void setCanRemoveViews(final boolean b) {
        super.setCanRemoveViews(b);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).setCanRemoveViews(b);
        }
    }
    
    @NonNull
    @Override
    public TransitionSet setDuration(final long n) {
        super.setDuration(n);
        if (this.mDuration >= 0L) {
            for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
                this.mTransitions.get(i).setDuration(n);
            }
            return this;
        }
        return this;
    }
    
    @Override
    public void setEpicenterCallback(final EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).setEpicenterCallback(epicenterCallback);
        }
    }
    
    @NonNull
    @Override
    public TransitionSet setInterpolator(@Nullable final TimeInterpolator interpolator) {
        return (TransitionSet)super.setInterpolator(interpolator);
    }
    
    @NonNull
    public TransitionSet setOrdering(final int n) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid parameter for TransitionSet ordering: ");
                sb.append(n);
                throw new AndroidRuntimeException(sb.toString());
            }
            case 1: {
                this.mPlayTogether = false;
                return this;
            }
            case 0: {
                this.mPlayTogether = true;
                return this;
            }
        }
    }
    
    @Override
    public void setPathMotion(final PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).setPathMotion(pathMotion);
        }
    }
    
    @Override
    TransitionSet setSceneRoot(final ViewGroup viewGroup) {
        super.setSceneRoot(viewGroup);
        for (int size = this.mTransitions.size(), i = 0; i < size; ++i) {
            this.mTransitions.get(i).setSceneRoot(viewGroup);
        }
        return this;
    }
    
    @NonNull
    @Override
    public TransitionSet setStartDelay(final long startDelay) {
        return (TransitionSet)super.setStartDelay(startDelay);
    }
    
    @Override
    String toString(final String s) {
        String s2 = super.toString(s);
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s2);
            sb.append("\n");
            final Transition transition = this.mTransitions.get(i);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("  ");
            sb.append(transition.toString(sb2.toString()));
            s2 = sb.toString();
        }
        return s2;
    }
    
    static class TransitionSetListener extends TransitionListenerAdapter
    {
        TransitionSet mTransitionSet;
        
        TransitionSetListener(final TransitionSet mTransitionSet) {
            this.mTransitionSet = mTransitionSet;
        }
        
        @Override
        public void onTransitionEnd(@NonNull final Transition transition) {
            --this.mTransitionSet.mCurrentListeners;
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.removeListener((TransitionListener)this);
        }
        
        @Override
        public void onTransitionStart(@NonNull final Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
            }
        }
    }
}
