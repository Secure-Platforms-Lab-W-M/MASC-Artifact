// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import android.support.constraint.solver.LinearSystem;

public class Guideline extends ConstraintWidget
{
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
    protected int mRelativeBegin;
    protected int mRelativeEnd;
    protected float mRelativePercent;
    
    public Guideline() {
        this.mRelativePercent = -1.0f;
        this.mRelativeBegin = -1;
        this.mRelativeEnd = -1;
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
    public void addToSolver(final LinearSystem linearSystem, final int n) {
        final ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer)this.getParent();
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
            linearSystem.addConstraint(LinearSystem.createRowEquals(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(constraintAnchor2), -this.mRelativeEnd, false));
        }
        else if (this.mRelativePercent != -1.0f) {
            linearSystem.addConstraint(LinearSystem.createRowDimensionPercent(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(constraintAnchor), linearSystem.createObjectVariable(constraintAnchor2), this.mRelativePercent, this.mIsPositionRelaxed));
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
        }
    }
    
    public ConstraintAnchor getAnchor() {
        return this.mAnchor;
    }
    
    @Override
    public ConstraintAnchor getAnchor(final ConstraintAnchor.Type type) {
        switch (type) {
            case TOP:
            case BOTTOM: {
                if (this.mOrientation == 0) {
                    return this.mAnchor;
                }
                break;
            }
            case LEFT:
            case RIGHT: {
                if (this.mOrientation == 1) {
                    return this.mAnchor;
                }
                break;
            }
        }
        return null;
    }
    
    @Override
    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }
    
    public Rectangle getHead() {
        final Rectangle mHead = this.mHead;
        final int drawX = this.getDrawX();
        final int mHeadSize = this.mHeadSize;
        final int drawY = this.getDrawY();
        final int mHeadSize2 = this.mHeadSize;
        mHead.setBounds(drawX - mHeadSize, drawY - mHeadSize2 * 2, mHeadSize2 * 2, mHeadSize2 * 2);
        if (this.getOrientation() == 0) {
            final Rectangle mHead2 = this.mHead;
            final int drawX2 = this.getDrawX();
            final int mHeadSize3 = this.mHeadSize;
            final int drawY2 = this.getDrawY();
            final int mHeadSize4 = this.mHeadSize;
            mHead2.setBounds(drawX2 - mHeadSize3 * 2, drawY2 - mHeadSize4, mHeadSize4 * 2, mHeadSize4 * 2);
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
        int guideBegin = this.getX();
        if (this.mOrientation == 0) {
            guideBegin = this.getY();
        }
        this.setGuideBegin(guideBegin);
    }
    
    void inferRelativeEndPosition() {
        int guideEnd = this.getParent().getWidth() - this.getX();
        if (this.mOrientation == 0) {
            guideEnd = this.getParent().getHeight() - this.getY();
        }
        this.setGuideEnd(guideEnd);
    }
    
    void inferRelativePercentPosition() {
        float guidePercent = this.getX() / (float)this.getParent().getWidth();
        if (this.mOrientation == 0) {
            guidePercent = this.getY() / (float)this.getParent().getHeight();
        }
        this.setGuidePercent(guidePercent);
    }
    
    @Override
    public void setDrawOrigin(int n, final int n2) {
        if (this.mOrientation == 1) {
            n -= this.mOffsetX;
            if (this.mRelativeBegin != -1) {
                this.setGuideBegin(n);
            }
            else if (this.mRelativeEnd != -1) {
                this.setGuideEnd(this.getParent().getWidth() - n);
            }
            else if (this.mRelativePercent != -1.0f) {
                this.setGuidePercent(n / (float)this.getParent().getWidth());
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
            this.setGuidePercent(n / (float)this.getParent().getHeight());
        }
    }
    
    public void setGuideBegin(final int mRelativeBegin) {
        if (mRelativeBegin > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = mRelativeBegin;
            this.mRelativeEnd = -1;
        }
    }
    
    public void setGuideEnd(final int mRelativeEnd) {
        if (mRelativeEnd > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = mRelativeEnd;
        }
    }
    
    public void setGuidePercent(final float mRelativePercent) {
        if (mRelativePercent > -1.0f) {
            this.mRelativePercent = mRelativePercent;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = -1;
        }
    }
    
    public void setGuidePercent(final int n) {
        this.setGuidePercent(n / 100.0f);
    }
    
    public void setMinimumPosition(final int mMinimumPosition) {
        this.mMinimumPosition = mMinimumPosition;
    }
    
    public void setOrientation(final int mOrientation) {
        if (this.mOrientation == mOrientation) {
            return;
        }
        this.mOrientation = mOrientation;
        this.mAnchors.clear();
        if (this.mOrientation == 1) {
            this.mAnchor = this.mLeft;
        }
        else {
            this.mAnchor = this.mTop;
        }
        this.mAnchors.add(this.mAnchor);
    }
    
    public void setPositionRelaxed(final boolean mIsPositionRelaxed) {
        if (this.mIsPositionRelaxed == mIsPositionRelaxed) {
            return;
        }
        this.mIsPositionRelaxed = mIsPositionRelaxed;
    }
    
    @Override
    public void updateFromSolver(final LinearSystem linearSystem, int objectVariableValue) {
        if (this.getParent() == null) {
            return;
        }
        objectVariableValue = linearSystem.getObjectVariableValue(this.mAnchor);
        if (this.mOrientation == 1) {
            this.setX(objectVariableValue);
            this.setY(0);
            this.setHeight(this.getParent().getHeight());
            this.setWidth(0);
            return;
        }
        this.setX(0);
        this.setY(objectVariableValue);
        this.setWidth(this.getParent().getWidth());
        this.setHeight(0);
    }
}
