// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;
import java.util.HashSet;
import android.support.constraint.solver.SolverVariable;

public class ConstraintAnchor
{
    private static final boolean ALLOW_BINARY = false;
    public static final int ANY_GROUP = Integer.MAX_VALUE;
    public static final int APPLY_GROUP_RESULTS = -2;
    public static final int AUTO_CONSTRAINT_CREATOR = 2;
    public static final int SCOUT_CREATOR = 1;
    private static final int UNSET_GONE_MARGIN = -1;
    public static final int USER_CREATOR = 0;
    public static final boolean USE_CENTER_ANCHOR = false;
    private int mConnectionCreator;
    private ConnectionType mConnectionType;
    int mGoneMargin;
    int mGroup;
    public int mMargin;
    final ConstraintWidget mOwner;
    SolverVariable mSolverVariable;
    private Strength mStrength;
    ConstraintAnchor mTarget;
    final Type mType;
    
    public ConstraintAnchor(final ConstraintWidget mOwner, final Type mType) {
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.NONE;
        this.mConnectionType = ConnectionType.RELAXED;
        this.mConnectionCreator = 0;
        this.mGroup = Integer.MAX_VALUE;
        this.mOwner = mOwner;
        this.mType = mType;
    }
    
    private boolean isConnectionToMe(final ConstraintWidget constraintWidget, final HashSet<ConstraintWidget> set) {
        if (set.contains(constraintWidget)) {
            return false;
        }
        set.add(constraintWidget);
        if (constraintWidget == this.getOwner()) {
            return true;
        }
        final ArrayList<ConstraintAnchor> anchors = constraintWidget.getAnchors();
        for (int i = 0; i < anchors.size(); ++i) {
            final ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && this.isConnectionToMe(constraintAnchor.getTarget().getOwner(), set)) {
                return true;
            }
        }
        return false;
    }
    
    private String toString(final HashSet<ConstraintAnchor> set) {
        if (set.add(this)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mOwner.getDebugName());
            sb.append(":");
            sb.append(this.mType.toString());
            String string;
            if (this.mTarget != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(" connected to ");
                sb2.append(this.mTarget.toString(set));
                string = sb2.toString();
            }
            else {
                string = "";
            }
            sb.append(string);
            return sb.toString();
        }
        return "<-";
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, 0, false);
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n, final int n2) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, n2, false);
    }
    
    public boolean connect(final ConstraintAnchor mTarget, final int mMargin, final int mGoneMargin, final Strength mStrength, final int mConnectionCreator, final boolean b) {
        if (mTarget == null) {
            this.mTarget = null;
            this.mMargin = 0;
            this.mGoneMargin = -1;
            this.mStrength = Strength.NONE;
            this.mConnectionCreator = 2;
            return true;
        }
        if (!b && !this.isValidConnection(mTarget)) {
            return false;
        }
        this.mTarget = mTarget;
        if (mMargin > 0) {
            this.mMargin = mMargin;
        }
        else {
            this.mMargin = 0;
        }
        this.mGoneMargin = mGoneMargin;
        this.mStrength = mStrength;
        this.mConnectionCreator = mConnectionCreator;
        return true;
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n, final Strength strength, final int n2) {
        return this.connect(constraintAnchor, n, -1, strength, n2, false);
    }
    
    public int getConnectionCreator() {
        return this.mConnectionCreator;
    }
    
    public ConnectionType getConnectionType() {
        return this.mConnectionType;
    }
    
    public int getGroup() {
        return this.mGroup;
    }
    
    public int getMargin() {
        if (this.mOwner.getVisibility() == 8) {
            return 0;
        }
        if (this.mGoneMargin > -1) {
            final ConstraintAnchor mTarget = this.mTarget;
            if (mTarget != null) {
                if (mTarget.mOwner.getVisibility() == 8) {
                    return this.mGoneMargin;
                }
            }
        }
        return this.mMargin;
    }
    
    public final ConstraintAnchor getOpposite() {
        switch (this.mType) {
            default: {
                return null;
            }
            case BOTTOM: {
                return this.mOwner.mTop;
            }
            case TOP: {
                return this.mOwner.mBottom;
            }
            case RIGHT: {
                return this.mOwner.mLeft;
            }
            case LEFT: {
                return this.mOwner.mRight;
            }
        }
    }
    
    public ConstraintWidget getOwner() {
        return this.mOwner;
    }
    
    public int getPriorityLevel() {
        switch (this.mType) {
            default: {
                return 0;
            }
            case BASELINE: {
                return 1;
            }
            case CENTER_Y: {
                return 0;
            }
            case CENTER_X: {
                return 0;
            }
            case BOTTOM: {
                return 2;
            }
            case TOP: {
                return 2;
            }
            case RIGHT: {
                return 2;
            }
            case LEFT: {
                return 2;
            }
            case CENTER: {
                return 2;
            }
        }
    }
    
    public int getSnapPriorityLevel() {
        switch (this.mType) {
            default: {
                return 0;
            }
            case BASELINE: {
                return 2;
            }
            case CENTER_Y: {
                return 1;
            }
            case CENTER_X: {
                return 0;
            }
            case BOTTOM: {
                return 0;
            }
            case TOP: {
                return 0;
            }
            case RIGHT: {
                return 1;
            }
            case LEFT: {
                return 1;
            }
            case CENTER: {
                return 3;
            }
        }
    }
    
    public SolverVariable getSolverVariable() {
        return this.mSolverVariable;
    }
    
    public Strength getStrength() {
        return this.mStrength;
    }
    
    public ConstraintAnchor getTarget() {
        return this.mTarget;
    }
    
    public Type getType() {
        return this.mType;
    }
    
    public boolean isConnected() {
        return this.mTarget != null;
    }
    
    public boolean isConnectionAllowed(final ConstraintWidget constraintWidget) {
        if (this.isConnectionToMe(constraintWidget, new HashSet<ConstraintWidget>())) {
            return false;
        }
        final ConstraintWidget parent = this.getOwner().getParent();
        return parent == constraintWidget || constraintWidget.getParent() == parent;
    }
    
    public boolean isConnectionAllowed(final ConstraintWidget constraintWidget, final ConstraintAnchor constraintAnchor) {
        return this.isConnectionAllowed(constraintWidget);
    }
    
    public boolean isSideAnchor() {
        switch (this.mType) {
            default: {
                return false;
            }
            case LEFT:
            case RIGHT:
            case TOP:
            case BOTTOM: {
                return true;
            }
        }
    }
    
    public boolean isSimilarDimensionConnection(final ConstraintAnchor constraintAnchor) {
        final Type type = constraintAnchor.getType();
        final Type mType = this.mType;
        final boolean b = true;
        final boolean b2 = true;
        if (type == mType) {
            return true;
        }
        switch (this.mType) {
            default: {
                return false;
            }
            case TOP:
            case BOTTOM:
            case CENTER_Y:
            case BASELINE: {
                boolean b3 = b2;
                if (type != Type.TOP) {
                    b3 = b2;
                    if (type != Type.BOTTOM) {
                        b3 = b2;
                        if (type != Type.CENTER_Y) {
                            if (type == Type.BASELINE) {
                                return true;
                            }
                            b3 = false;
                        }
                    }
                }
                return b3;
            }
            case LEFT:
            case RIGHT:
            case CENTER_X: {
                boolean b4 = b;
                if (type != Type.LEFT) {
                    b4 = b;
                    if (type != Type.RIGHT) {
                        if (type == Type.CENTER_X) {
                            return true;
                        }
                        b4 = false;
                    }
                }
                return b4;
            }
            case CENTER: {
                return type != Type.BASELINE;
            }
        }
    }
    
    public boolean isSnapCompatibleWith(final ConstraintAnchor constraintAnchor) {
        if (this.mType == Type.CENTER) {
            return false;
        }
        if (this.mType == constraintAnchor.getType()) {
            return true;
        }
        switch (this.mType) {
            default: {
                return false;
            }
            case CENTER_Y: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case BOTTOM: {
                        return true;
                    }
                    case TOP: {
                        return true;
                    }
                }
                break;
            }
            case CENTER_X: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case RIGHT: {
                        return true;
                    }
                    case LEFT: {
                        return true;
                    }
                }
                break;
            }
            case BOTTOM: {
                final int n = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                return n == 4 || n == 7;
            }
            case TOP: {
                final int n2 = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                return n2 == 5 || n2 == 7;
            }
            case RIGHT: {
                final int n3 = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                return n3 == 2 || n3 == 6;
            }
            case LEFT: {
                final int n4 = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                return n4 == 3 || n4 == 6;
            }
        }
    }
    
    public boolean isValidConnection(final ConstraintAnchor constraintAnchor) {
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = false;
        if (constraintAnchor == null) {
            return false;
        }
        final Type type = constraintAnchor.getType();
        final Type mType = this.mType;
        if (type == mType) {
            return mType != Type.CENTER && (this.mType != Type.BASELINE || (constraintAnchor.getOwner().hasBaseline() && this.getOwner().hasBaseline()));
        }
        switch (this.mType) {
            default: {
                return false;
            }
            case TOP:
            case BOTTOM: {
                final boolean b4 = type == Type.TOP || type == Type.BOTTOM;
                if (constraintAnchor.getOwner() instanceof Guideline) {
                    return b4 || type == Type.CENTER_Y || b3;
                }
                return b4;
            }
            case LEFT:
            case RIGHT: {
                final boolean b5 = type == Type.LEFT || type == Type.RIGHT;
                if (constraintAnchor.getOwner() instanceof Guideline) {
                    return b5 || type == Type.CENTER_X || b;
                }
                return b5;
            }
            case CENTER: {
                boolean b6 = b2;
                if (type != Type.BASELINE) {
                    b6 = b2;
                    if (type != Type.CENTER_X) {
                        b6 = b2;
                        if (type != Type.CENTER_Y) {
                            b6 = true;
                        }
                    }
                }
                return b6;
            }
        }
    }
    
    public boolean isVerticalAnchor() {
        final int n = ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()];
        if (n != 6) {
            switch (n) {
                default: {
                    return true;
                }
                case 1:
                case 2:
                case 3: {
                    break;
                }
            }
        }
        return false;
    }
    
    public void reset() {
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.STRONG;
        this.mConnectionCreator = 0;
        this.mConnectionType = ConnectionType.RELAXED;
    }
    
    public void resetSolverVariable(final Cache cache) {
        final SolverVariable mSolverVariable = this.mSolverVariable;
        if (mSolverVariable == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED);
            return;
        }
        mSolverVariable.reset();
    }
    
    public void setConnectionCreator(final int mConnectionCreator) {
        this.mConnectionCreator = mConnectionCreator;
    }
    
    public void setConnectionType(final ConnectionType mConnectionType) {
        this.mConnectionType = mConnectionType;
    }
    
    public void setGoneMargin(final int mGoneMargin) {
        if (this.isConnected()) {
            this.mGoneMargin = mGoneMargin;
        }
    }
    
    public void setGroup(final int mGroup) {
        this.mGroup = mGroup;
    }
    
    public void setMargin(final int mMargin) {
        if (this.isConnected()) {
            this.mMargin = mMargin;
        }
    }
    
    public void setStrength(final Strength mStrength) {
        if (this.isConnected()) {
            this.mStrength = mStrength;
        }
    }
    
    @Override
    public String toString() {
        final HashSet<ConstraintAnchor> set = new HashSet<ConstraintAnchor>();
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mOwner.getDebugName());
        sb.append(":");
        sb.append(this.mType.toString());
        String string;
        if (this.mTarget != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" connected to ");
            sb2.append(this.mTarget.toString(set));
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        return sb.toString();
    }
    
    public enum ConnectionType
    {
        RELAXED, 
        STRICT;
    }
    
    public enum Strength
    {
        NONE, 
        STRONG, 
        WEAK;
    }
    
    public enum Type
    {
        BASELINE, 
        BOTTOM, 
        CENTER, 
        CENTER_X, 
        CENTER_Y, 
        LEFT, 
        NONE, 
        RIGHT, 
        TOP;
    }
}
