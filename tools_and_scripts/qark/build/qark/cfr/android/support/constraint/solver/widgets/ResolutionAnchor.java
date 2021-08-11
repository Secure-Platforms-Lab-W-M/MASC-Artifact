/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ResolutionDimension;
import android.support.constraint.solver.widgets.ResolutionNode;

public class ResolutionAnchor
extends ResolutionNode {
    public static final int BARRIER_CONNECTION = 5;
    public static final int CENTER_CONNECTION = 2;
    public static final int CHAIN_CONNECTION = 4;
    public static final int DIRECT_CONNECTION = 1;
    public static final int MATCH_CONNECTION = 3;
    public static final int UNCONNECTED = 0;
    float computedValue;
    private ResolutionDimension dimension = null;
    private int dimensionMultiplier = 1;
    ConstraintAnchor myAnchor;
    float offset;
    private ResolutionAnchor opposite;
    private ResolutionDimension oppositeDimension = null;
    private int oppositeDimensionMultiplier = 1;
    private float oppositeOffset;
    float resolvedOffset;
    ResolutionAnchor resolvedTarget;
    ResolutionAnchor target;
    int type = 0;

    public ResolutionAnchor(ConstraintAnchor constraintAnchor) {
        this.myAnchor = constraintAnchor;
    }

    void addResolvedValue(LinearSystem linearSystem) {
        SolverVariable solverVariable = this.myAnchor.getSolverVariable();
        ResolutionAnchor resolutionAnchor = this.resolvedTarget;
        if (resolutionAnchor == null) {
            linearSystem.addEquality(solverVariable, (int)(this.resolvedOffset + 0.5f));
            return;
        }
        linearSystem.addEquality(solverVariable, linearSystem.createObjectVariable(resolutionAnchor.myAnchor), (int)(this.resolvedOffset + 0.5f), 6);
    }

    public void dependsOn(int n, ResolutionAnchor resolutionAnchor, int n2) {
        this.type = n;
        this.target = resolutionAnchor;
        this.offset = n2;
        this.target.addDependent(this);
    }

    public void dependsOn(ResolutionAnchor resolutionAnchor, int n) {
        this.target = resolutionAnchor;
        this.offset = n;
        this.target.addDependent(this);
    }

    public void dependsOn(ResolutionAnchor resolutionAnchor, int n, ResolutionDimension resolutionDimension) {
        this.target = resolutionAnchor;
        this.target.addDependent(this);
        this.dimension = resolutionDimension;
        this.dimensionMultiplier = n;
        this.dimension.addDependent(this);
    }

    public float getResolvedValue() {
        return this.resolvedOffset;
    }

    @Override
    public void remove(ResolutionDimension resolutionDimension) {
        ResolutionDimension resolutionDimension2 = this.dimension;
        if (resolutionDimension2 == resolutionDimension) {
            this.dimension = null;
            this.offset = this.dimensionMultiplier;
        } else if (resolutionDimension2 == this.oppositeDimension) {
            this.oppositeDimension = null;
            this.oppositeOffset = this.oppositeDimensionMultiplier;
        }
        this.resolve();
    }

    @Override
    public void reset() {
        super.reset();
        this.target = null;
        this.offset = 0.0f;
        this.dimension = null;
        this.dimensionMultiplier = 1;
        this.oppositeDimension = null;
        this.oppositeDimensionMultiplier = 1;
        this.resolvedTarget = null;
        this.resolvedOffset = 0.0f;
        this.computedValue = 0.0f;
        this.opposite = null;
        this.oppositeOffset = 0.0f;
        this.type = 0;
    }

    @Override
    public void resolve() {
        int n = this.state;
        int n2 = 1;
        if (n == 1) {
            return;
        }
        if (this.type == 4) {
            return;
        }
        Object object = this.dimension;
        if (object != null) {
            if (object.state != 1) {
                return;
            }
            this.offset = (float)this.dimensionMultiplier * this.dimension.value;
        }
        if ((object = this.oppositeDimension) != null) {
            if (object.state != 1) {
                return;
            }
            this.oppositeOffset = (float)this.oppositeDimensionMultiplier * this.oppositeDimension.value;
        }
        if (this.type == 1 && ((object = this.target) == null || object.state == 1)) {
            object = this.target;
            if (object == null) {
                this.resolvedTarget = this;
                this.resolvedOffset = this.offset;
            } else {
                this.resolvedTarget = object.resolvedTarget;
                this.resolvedOffset = object.resolvedOffset + this.offset;
            }
            this.didResolve();
            return;
        }
        if (this.type == 2 && (object = this.target) != null && object.state == 1 && (object = this.opposite) != null && (object = object.target) != null && object.state == 1) {
            float f;
            if (LinearSystem.getMetrics() != null) {
                object = LinearSystem.getMetrics();
                ++object.centerConnectionResolved;
            }
            this.resolvedTarget = this.target.resolvedTarget;
            object = this.opposite;
            object.resolvedTarget = object.target.resolvedTarget;
            n = n2;
            if (this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
                n = this.myAnchor.mType == ConstraintAnchor.Type.BOTTOM ? n2 : 0;
            }
            float f2 = n != 0 ? this.target.resolvedOffset - this.opposite.target.resolvedOffset : this.opposite.target.resolvedOffset - this.target.resolvedOffset;
            if (this.myAnchor.mType != ConstraintAnchor.Type.LEFT && this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
                f = f2 - (float)this.myAnchor.mOwner.getHeight();
                f2 = this.myAnchor.mOwner.mVerticalBiasPercent;
            } else {
                f = f2 - (float)this.myAnchor.mOwner.getWidth();
                f2 = this.myAnchor.mOwner.mHorizontalBiasPercent;
            }
            n2 = this.myAnchor.getMargin();
            int n3 = this.opposite.myAnchor.getMargin();
            if (this.myAnchor.getTarget() == this.opposite.myAnchor.getTarget()) {
                f2 = 0.5f;
                n2 = 0;
                n3 = 0;
            }
            f = f - (float)n2 - (float)n3;
            if (n != 0) {
                object = this.opposite;
                object.resolvedOffset = object.target.resolvedOffset + (float)n3 + f * f2;
                this.resolvedOffset = this.target.resolvedOffset - (float)n2 - (1.0f - f2) * f;
            } else {
                this.resolvedOffset = this.target.resolvedOffset + (float)n2 + f * f2;
                object = this.opposite;
                object.resolvedOffset = object.target.resolvedOffset - (float)n3 - (1.0f - f2) * f;
            }
            this.didResolve();
            this.opposite.didResolve();
            return;
        }
        if (this.type == 3 && (object = this.target) != null && object.state == 1 && (object = this.opposite) != null && (object = object.target) != null && object.state == 1) {
            if (LinearSystem.getMetrics() != null) {
                object = LinearSystem.getMetrics();
                ++object.matchConnectionResolved;
            }
            object = this.target;
            this.resolvedTarget = object.resolvedTarget;
            ResolutionAnchor resolutionAnchor = this.opposite;
            ResolutionAnchor resolutionAnchor2 = resolutionAnchor.target;
            resolutionAnchor.resolvedTarget = resolutionAnchor2.resolvedTarget;
            this.resolvedOffset = object.resolvedOffset + this.offset;
            resolutionAnchor.resolvedOffset = resolutionAnchor2.resolvedOffset + resolutionAnchor.offset;
            this.didResolve();
            this.opposite.didResolve();
            return;
        }
        if (this.type == 5) {
            this.myAnchor.mOwner.resolve();
            return;
        }
    }

    public void resolve(ResolutionAnchor resolutionAnchor, float f) {
        if (this.state != 0 && (this.resolvedTarget == resolutionAnchor || this.resolvedOffset == f)) {
            return;
        }
        this.resolvedTarget = resolutionAnchor;
        this.resolvedOffset = f;
        if (this.state == 1) {
            this.invalidate();
        }
        this.didResolve();
    }

    String sType(int n) {
        if (n == 1) {
            return "DIRECT";
        }
        if (n == 2) {
            return "CENTER";
        }
        if (n == 3) {
            return "MATCH";
        }
        if (n == 4) {
            return "CHAIN";
        }
        if (n == 5) {
            return "BARRIER";
        }
        return "UNCONNECTED";
    }

    public void setOpposite(ResolutionAnchor resolutionAnchor, float f) {
        this.opposite = resolutionAnchor;
        this.oppositeOffset = f;
    }

    public void setOpposite(ResolutionAnchor resolutionAnchor, int n, ResolutionDimension resolutionDimension) {
        this.opposite = resolutionAnchor;
        this.oppositeDimension = resolutionDimension;
        this.oppositeDimensionMultiplier = n;
    }

    public void setType(int n) {
        this.type = n;
    }

    public String toString() {
        if (this.state == 1) {
            if (this.resolvedTarget == this) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[");
                stringBuilder.append(this.myAnchor);
                stringBuilder.append(", RESOLVED: ");
                stringBuilder.append(this.resolvedOffset);
                stringBuilder.append("]  type: ");
                stringBuilder.append(this.sType(this.type));
                return stringBuilder.toString();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append(this.myAnchor);
            stringBuilder.append(", RESOLVED: ");
            stringBuilder.append(this.resolvedTarget);
            stringBuilder.append(":");
            stringBuilder.append(this.resolvedOffset);
            stringBuilder.append("] type: ");
            stringBuilder.append(this.sType(this.type));
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ ");
        stringBuilder.append(this.myAnchor);
        stringBuilder.append(" UNRESOLVED} type: ");
        stringBuilder.append(this.sType(this.type));
        return stringBuilder.toString();
    }

    public void update() {
        ConstraintAnchor constraintAnchor = this.myAnchor.getTarget();
        if (constraintAnchor == null) {
            return;
        }
        if (constraintAnchor.getTarget() == this.myAnchor) {
            this.type = 4;
            constraintAnchor.getResolutionNode().type = 4;
        }
        int n = this.myAnchor.getMargin();
        if (this.myAnchor.mType == ConstraintAnchor.Type.RIGHT || this.myAnchor.mType == ConstraintAnchor.Type.BOTTOM) {
            n = - n;
        }
        this.dependsOn(constraintAnchor.getResolutionNode(), n);
    }
}

