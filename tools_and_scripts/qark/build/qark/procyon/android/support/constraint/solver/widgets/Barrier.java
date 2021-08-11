// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;
import java.util.ArrayList;

public class Barrier extends Helper
{
    public static final int BOTTOM = 3;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    private boolean mAllowsGoneWidget;
    private int mBarrierType;
    private ArrayList<ResolutionAnchor> mNodes;
    
    public Barrier() {
        this.mBarrierType = 0;
        this.mNodes = new ArrayList<ResolutionAnchor>(4);
        this.mAllowsGoneWidget = true;
    }
    
    @Override
    public void addToSolver(final LinearSystem linearSystem) {
        this.mListAnchors[0] = this.mLeft;
        this.mListAnchors[2] = this.mTop;
        this.mListAnchors[1] = this.mRight;
        this.mListAnchors[3] = this.mBottom;
        for (int i = 0; i < this.mListAnchors.length; ++i) {
            this.mListAnchors[i].mSolverVariable = linearSystem.createObjectVariable(this.mListAnchors[i]);
        }
        final int mBarrierType = this.mBarrierType;
        if (mBarrierType < 0 || mBarrierType >= 4) {
            return;
        }
        final ConstraintAnchor constraintAnchor = this.mListAnchors[this.mBarrierType];
        final boolean b = false;
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= this.mWidgetsCount) {
                break;
            }
            final ConstraintWidget constraintWidget = this.mWidgets[n];
            if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                final int mBarrierType2 = this.mBarrierType;
                if (mBarrierType2 == 0 || mBarrierType2 == 1) {
                    if (constraintWidget.getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                        b2 = true;
                        break;
                    }
                }
                final int mBarrierType3 = this.mBarrierType;
                if (mBarrierType3 == 2 || mBarrierType3 == 3) {
                    if (constraintWidget.getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
                        b2 = true;
                        break;
                    }
                }
            }
            ++n;
        }
        final int mBarrierType4 = this.mBarrierType;
        if (mBarrierType4 != 0 && mBarrierType4 != 1) {
            if (this.getParent().getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT) {
                b2 = false;
            }
        }
        else if (this.getParent().getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT) {
            b2 = false;
        }
        for (int j = 0; j < this.mWidgetsCount; ++j) {
            final ConstraintWidget constraintWidget2 = this.mWidgets[j];
            if (this.mAllowsGoneWidget || constraintWidget2.allowedInBarrier()) {
                final SolverVariable objectVariable = linearSystem.createObjectVariable(constraintWidget2.mListAnchors[this.mBarrierType]);
                final ConstraintAnchor[] mListAnchors = constraintWidget2.mListAnchors;
                final int mBarrierType5 = this.mBarrierType;
                mListAnchors[mBarrierType5].mSolverVariable = objectVariable;
                if (mBarrierType5 != 0 && mBarrierType5 != 2) {
                    linearSystem.addGreaterBarrier(constraintAnchor.mSolverVariable, objectVariable, b2);
                }
                else {
                    linearSystem.addLowerBarrier(constraintAnchor.mSolverVariable, objectVariable, b2);
                }
            }
        }
        final int mBarrierType6 = this.mBarrierType;
        if (mBarrierType6 == 0) {
            linearSystem.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 6);
            if (!b2) {
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 5);
            }
        }
        else if (mBarrierType6 == 1) {
            linearSystem.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 6);
            if (!b2) {
                linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 5);
            }
        }
        else if (mBarrierType6 == 2) {
            linearSystem.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 6);
            if (!b2) {
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 5);
            }
        }
        else {
            if (mBarrierType6 != 3) {
                return;
            }
            linearSystem.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 6);
            if (!b2) {
                linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 5);
            }
        }
    }
    
    @Override
    public boolean allowedInBarrier() {
        return true;
    }
    
    public boolean allowsGoneWidget() {
        return this.mAllowsGoneWidget;
    }
    
    @Override
    public void analyze(int i) {
        if (this.mParent == null) {
            return;
        }
        if (!((ConstraintWidgetContainer)this.mParent).optimizeFor(2)) {
            return;
        }
        ResolutionAnchor resolutionAnchor = null;
        switch (this.mBarrierType) {
            default: {
                return;
            }
            case 3: {
                resolutionAnchor = this.mBottom.getResolutionNode();
                break;
            }
            case 2: {
                resolutionAnchor = this.mTop.getResolutionNode();
                break;
            }
            case 1: {
                resolutionAnchor = this.mRight.getResolutionNode();
                break;
            }
            case 0: {
                resolutionAnchor = this.mLeft.getResolutionNode();
                break;
            }
        }
        resolutionAnchor.setType(5);
        i = this.mBarrierType;
        if (i != 0 && i != 1) {
            this.mLeft.getResolutionNode().resolve(null, 0.0f);
            this.mRight.getResolutionNode().resolve(null, 0.0f);
        }
        else {
            this.mTop.getResolutionNode().resolve(null, 0.0f);
            this.mBottom.getResolutionNode().resolve(null, 0.0f);
        }
        this.mNodes.clear();
        ConstraintWidget constraintWidget;
        ResolutionAnchor resolutionAnchor2;
        for (i = 0; i < this.mWidgetsCount; ++i) {
            constraintWidget = this.mWidgets[i];
            if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
                resolutionAnchor2 = null;
                switch (this.mBarrierType) {
                    case 3: {
                        resolutionAnchor2 = constraintWidget.mBottom.getResolutionNode();
                        break;
                    }
                    case 2: {
                        resolutionAnchor2 = constraintWidget.mTop.getResolutionNode();
                        break;
                    }
                    case 1: {
                        resolutionAnchor2 = constraintWidget.mRight.getResolutionNode();
                        break;
                    }
                    case 0: {
                        resolutionAnchor2 = constraintWidget.mLeft.getResolutionNode();
                        break;
                    }
                }
                if (resolutionAnchor2 != null) {
                    this.mNodes.add(resolutionAnchor2);
                    resolutionAnchor2.addDependent(resolutionAnchor);
                }
            }
        }
    }
    
    @Override
    public void resetResolutionNodes() {
        super.resetResolutionNodes();
        this.mNodes.clear();
    }
    
    @Override
    public void resolve() {
        float resolvedOffset = 0.0f;
        ResolutionAnchor resolutionAnchor = null;
        switch (this.mBarrierType) {
            default: {
                return;
            }
            case 3: {
                resolutionAnchor = this.mBottom.getResolutionNode();
                break;
            }
            case 2: {
                resolutionAnchor = this.mTop.getResolutionNode();
                resolvedOffset = Float.MAX_VALUE;
                break;
            }
            case 1: {
                resolutionAnchor = this.mRight.getResolutionNode();
                break;
            }
            case 0: {
                resolutionAnchor = this.mLeft.getResolutionNode();
                resolvedOffset = Float.MAX_VALUE;
                break;
            }
        }
        final int size = this.mNodes.size();
        ResolutionAnchor resolvedTarget = null;
        for (int i = 0; i < size; ++i) {
            final ResolutionAnchor resolutionAnchor2 = this.mNodes.get(i);
            if (resolutionAnchor2.state != 1) {
                return;
            }
            final int mBarrierType = this.mBarrierType;
            if (mBarrierType != 0 && mBarrierType != 2) {
                if (resolutionAnchor2.resolvedOffset > resolvedOffset) {
                    resolvedOffset = resolutionAnchor2.resolvedOffset;
                    resolvedTarget = resolutionAnchor2.resolvedTarget;
                }
            }
            else if (resolutionAnchor2.resolvedOffset < resolvedOffset) {
                resolvedOffset = resolutionAnchor2.resolvedOffset;
                resolvedTarget = resolutionAnchor2.resolvedTarget;
            }
        }
        if (LinearSystem.getMetrics() != null) {
            final Metrics metrics = LinearSystem.getMetrics();
            ++metrics.barrierConnectionResolved;
        }
        resolutionAnchor.resolvedTarget = resolvedTarget;
        resolutionAnchor.resolvedOffset = resolvedOffset;
        resolutionAnchor.didResolve();
        switch (this.mBarrierType) {
            default: {}
            case 3: {
                this.mTop.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
            }
            case 2: {
                this.mBottom.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
            }
            case 1: {
                this.mLeft.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
            }
            case 0: {
                this.mRight.getResolutionNode().resolve(resolvedTarget, resolvedOffset);
            }
        }
    }
    
    public void setAllowsGoneWidget(final boolean mAllowsGoneWidget) {
        this.mAllowsGoneWidget = mAllowsGoneWidget;
    }
    
    public void setBarrierType(final int mBarrierType) {
        this.mBarrierType = mBarrierType;
    }
}
