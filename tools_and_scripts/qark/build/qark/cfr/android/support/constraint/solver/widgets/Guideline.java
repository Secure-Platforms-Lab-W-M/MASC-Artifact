/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Rectangle;
import java.util.ArrayList;

public class Guideline
extends ConstraintWidget {
    public static final int HORIZONTAL = 0;
    public static final int RELATIVE_BEGIN = 1;
    public static final int RELATIVE_END = 2;
    public static final int RELATIVE_PERCENT = 0;
    public static final int RELATIVE_UNKNWON = -1;
    public static final int VERTICAL = 1;
    private ConstraintAnchor mAnchor;
    private Rectangle mHead;
    private int mHeadSize;
    private boolean mIsPositionRelaxed;
    private int mMinimumPosition;
    private int mOrientation;
    protected int mRelativeBegin = -1;
    protected int mRelativeEnd = -1;
    protected float mRelativePercent = -1.0f;

    public Guideline() {
        this.mAnchor = this.mTop;
        this.mOrientation = 0;
        this.mIsPositionRelaxed = false;
        this.mMinimumPosition = 0;
        this.mHead = new Rectangle();
        this.mHeadSize = 8;
        this.mAnchors.clear();
        this.mAnchors.add(this.mAnchor);
    }

    @Override
    public void addToSolver(LinearSystem linearSystem, int n) {
        ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)this.getParent();
        if (constraintWidgetContainer == null) {
            return;
        }
        ConstraintAnchor constraintAnchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor constraintAnchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT);
        if (this.mOrientation == 0) {
            constraintAnchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP);
            constraintAnchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM);
        }
        if (this.mRelativeBegin != -1) {
            linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(constraintAnchor), this.mRelativeBegin, false));
            return;
        }
        if (this.mRelativeEnd != -1) {
            linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(constraintAnchor2), - this.mRelativeEnd, false));
        } else if (this.mRelativePercent != -1.0f) {
            linearSystem.addConstraint(LinearSystem.createRowDimensionPercent(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(constraintAnchor), linearSystem.createObjectVariable(constraintAnchor2), this.mRelativePercent, this.mIsPositionRelaxed));
            return;
        }
    }

    public void cyclePosition() {
        if (this.mRelativeBegin != -1) {
            this.inferRelativePercentPosition();
            return;
        }
        if (this.mRelativePercent != -1.0f) {
            this.inferRelativeEndPosition();
            return;
        }
        if (this.mRelativeEnd != -1) {
            this.inferRelativeBeginPosition();
            return;
        }
    }

    public ConstraintAnchor getAnchor() {
        return this.mAnchor;
    }

    @Override
    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            default: {
                break;
            }
            case 3: 
            case 4: {
                if (this.mOrientation != 0) break;
                return this.mAnchor;
            }
            case 1: 
            case 2: {
                if (this.mOrientation != 1) break;
                return this.mAnchor;
            }
        }
        return null;
    }

    @Override
    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public Rectangle getHead() {
        Rectangle rectangle = this.mHead;
        int n = this.getDrawX();
        int n2 = this.mHeadSize;
        int n3 = this.getDrawY();
        int n4 = this.mHeadSize;
        rectangle.setBounds(n - n2, n3 - n4 * 2, n4 * 2, n4 * 2);
        if (this.getOrientation() == 0) {
            rectangle = this.mHead;
            n = this.getDrawX();
            n2 = this.mHeadSize;
            n3 = this.getDrawY();
            n4 = this.mHeadSize;
            rectangle.setBounds(n - n2 * 2, n3 - n4, n4 * 2, n4 * 2);
        }
        return this.mHead;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getRelativeBegin() {
        return this.mRelativeBegin;
    }

    public int getRelativeBehaviour() {
        if (this.mRelativePercent != -1.0f) {
            return 0;
        }
        if (this.mRelativeBegin != -1) {
            return 1;
        }
        if (this.mRelativeEnd != -1) {
            return 2;
        }
        return -1;
    }

    public int getRelativeEnd() {
        return this.mRelativeEnd;
    }

    public float getRelativePercent() {
        return this.mRelativePercent;
    }

    @Override
    public String getType() {
        return "Guideline";
    }

    void inferRelativeBeginPosition() {
        int n = this.getX();
        if (this.mOrientation == 0) {
            n = this.getY();
        }
        this.setGuideBegin(n);
    }

    void inferRelativeEndPosition() {
        int n = this.getParent().getWidth() - this.getX();
        if (this.mOrientation == 0) {
            n = this.getParent().getHeight() - this.getY();
        }
        this.setGuideEnd(n);
    }

    void inferRelativePercentPosition() {
        float f = (float)this.getX() / (float)this.getParent().getWidth();
        if (this.mOrientation == 0) {
            f = (float)this.getY() / (float)this.getParent().getHeight();
        }
        this.setGuidePercent(f);
    }

    @Override
    public void setDrawOrigin(int n, int n2) {
        if (this.mOrientation == 1) {
            n -= this.mOffsetX;
            if (this.mRelativeBegin != -1) {
                this.setGuideBegin(n);
            } else if (this.mRelativeEnd != -1) {
                this.setGuideEnd(this.getParent().getWidth() - n);
            } else if (this.mRelativePercent != -1.0f) {
                this.setGuidePercent((float)n / (float)this.getParent().getWidth());
            }
            return;
        }
        n = n2 - this.mOffsetY;
        if (this.mRelativeBegin != -1) {
            this.setGuideBegin(n);
            return;
        }
        if (this.mRelativeEnd != -1) {
            this.setGuideEnd(this.getParent().getHeight() - n);
            return;
        }
        if (this.mRelativePercent != -1.0f) {
            this.setGuidePercent((float)n / (float)this.getParent().getHeight());
            return;
        }
    }

    public void setGuideBegin(int n) {
        if (n > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = n;
            this.mRelativeEnd = -1;
            return;
        }
    }

    public void setGuideEnd(int n) {
        if (n > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = n;
            return;
        }
    }

    public void setGuidePercent(float f) {
        if (f > -1.0f) {
            this.mRelativePercent = f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = -1;
            return;
        }
    }

    public void setGuidePercent(int n) {
        this.setGuidePercent((float)n / 100.0f);
    }

    public void setMinimumPosition(int n) {
        this.mMinimumPosition = n;
    }

    public void setOrientation(int n) {
        if (this.mOrientation == n) {
            return;
        }
        this.mOrientation = n;
        this.mAnchors.clear();
        this.mAnchor = this.mOrientation == 1 ? this.mLeft : this.mTop;
        this.mAnchors.add(this.mAnchor);
    }

    public void setPositionRelaxed(boolean bl) {
        if (this.mIsPositionRelaxed == bl) {
            return;
        }
        this.mIsPositionRelaxed = bl;
    }

    @Override
    public void updateFromSolver(LinearSystem linearSystem, int n) {
        if (this.getParent() == null) {
            return;
        }
        n = linearSystem.getObjectVariableValue(this.mAnchor);
        if (this.mOrientation == 1) {
            this.setX(n);
            this.setY(0);
            this.setHeight(this.getParent().getHeight());
            this.setWidth(0);
            return;
        }
        this.setX(0);
        this.setY(n);
        this.setWidth(this.getParent().getWidth());
        this.setHeight(0);
    }

}

