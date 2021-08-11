/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Guideline;
import java.util.ArrayList;
import java.util.HashSet;

public class ConstraintAnchor {
    private static final boolean ALLOW_BINARY = false;
    public static final int ANY_GROUP = Integer.MAX_VALUE;
    public static final int APPLY_GROUP_RESULTS = -2;
    public static final int AUTO_CONSTRAINT_CREATOR = 2;
    public static final int SCOUT_CREATOR = 1;
    private static final int UNSET_GONE_MARGIN = -1;
    public static final int USER_CREATOR = 0;
    public static final boolean USE_CENTER_ANCHOR = false;
    private int mConnectionCreator = 0;
    private ConnectionType mConnectionType = ConnectionType.RELAXED;
    int mGoneMargin = -1;
    int mGroup = Integer.MAX_VALUE;
    public int mMargin = 0;
    final ConstraintWidget mOwner;
    SolverVariable mSolverVariable;
    private Strength mStrength = Strength.NONE;
    ConstraintAnchor mTarget;
    final Type mType;

    public ConstraintAnchor(ConstraintWidget constraintWidget, Type type) {
        this.mOwner = constraintWidget;
        this.mType = type;
    }

    private boolean isConnectionToMe(ConstraintWidget object, HashSet<ConstraintWidget> hashSet) {
        if (hashSet.contains(object)) {
            return false;
        }
        hashSet.add((ConstraintWidget)object);
        if (object == this.getOwner()) {
            return true;
        }
        object = object.getAnchors();
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            ConstraintAnchor constraintAnchor = (ConstraintAnchor)object.get(i);
            if (!constraintAnchor.isSimilarDimensionConnection(this) || !constraintAnchor.isConnected() || !this.isConnectionToMe(constraintAnchor.getTarget().getOwner(), hashSet)) continue;
            return true;
        }
        return false;
    }

    private String toString(HashSet<ConstraintAnchor> object) {
        if (object.add((ConstraintAnchor)this)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.mOwner.getDebugName());
            stringBuilder.append(":");
            stringBuilder.append(this.mType.toString());
            if (this.mTarget != null) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(" connected to ");
                stringBuilder2.append(this.mTarget.toString((HashSet<ConstraintAnchor>)object));
                object = stringBuilder2.toString();
            } else {
                object = "";
            }
            stringBuilder.append((String)object);
            return stringBuilder.toString();
        }
        return "<-";
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int n) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, 0, false);
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int n, int n2) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, n2, false);
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int n, int n2, Strength strength, int n3, boolean bl) {
        if (constraintAnchor == null) {
            this.mTarget = null;
            this.mMargin = 0;
            this.mGoneMargin = -1;
            this.mStrength = Strength.NONE;
            this.mConnectionCreator = 2;
            return true;
        }
        if (!bl && !this.isValidConnection(constraintAnchor)) {
            return false;
        }
        this.mTarget = constraintAnchor;
        this.mMargin = n > 0 ? n : 0;
        this.mGoneMargin = n2;
        this.mStrength = strength;
        this.mConnectionCreator = n3;
        return true;
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int n, Strength strength, int n2) {
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
        ConstraintAnchor constraintAnchor;
        if (this.mOwner.getVisibility() == 8) {
            return 0;
        }
        if (this.mGoneMargin > -1 && (constraintAnchor = this.mTarget) != null && constraintAnchor.mOwner.getVisibility() == 8) {
            return this.mGoneMargin;
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
            case LEFT: 
        }
        return this.mOwner.mRight;
    }

    public ConstraintWidget getOwner() {
        return this.mOwner;
    }

    public int getPriorityLevel() {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return 0;
            }
            case 8: {
                return 1;
            }
            case 7: {
                return 0;
            }
            case 6: {
                return 0;
            }
            case 5: {
                return 2;
            }
            case 4: {
                return 2;
            }
            case 3: {
                return 2;
            }
            case 2: {
                return 2;
            }
            case 1: 
        }
        return 2;
    }

    public int getSnapPriorityLevel() {
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return 0;
            }
            case 8: {
                return 2;
            }
            case 7: {
                return 1;
            }
            case 6: {
                return 0;
            }
            case 5: {
                return 0;
            }
            case 4: {
                return 0;
            }
            case 3: {
                return 1;
            }
            case 2: {
                return 1;
            }
            case 1: 
        }
        return 3;
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
        if (this.mTarget != null) {
            return true;
        }
        return false;
    }

    public boolean isConnectionAllowed(ConstraintWidget constraintWidget) {
        if (this.isConnectionToMe(constraintWidget, new HashSet<ConstraintWidget>())) {
            return false;
        }
        ConstraintWidget constraintWidget2 = this.getOwner().getParent();
        if (constraintWidget2 == constraintWidget) {
            return true;
        }
        if (constraintWidget.getParent() == constraintWidget2) {
            return true;
        }
        return false;
    }

    public boolean isConnectionAllowed(ConstraintWidget constraintWidget, ConstraintAnchor constraintAnchor) {
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
            case BOTTOM: 
        }
        return true;
    }

    public boolean isSimilarDimensionConnection(ConstraintAnchor object) {
        object = object.getType();
        Type type = this.mType;
        boolean bl = true;
        boolean bl2 = true;
        if (object == type) {
            return true;
        }
        switch (.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()]) {
            default: {
                return false;
            }
            case 4: 
            case 5: 
            case 7: 
            case 8: {
                boolean bl3 = bl2;
                if (object != Type.TOP) {
                    bl3 = bl2;
                    if (object != Type.BOTTOM) {
                        bl3 = bl2;
                        if (object != Type.CENTER_Y) {
                            if (object == Type.BASELINE) {
                                return true;
                            }
                            bl3 = false;
                        }
                    }
                }
                return bl3;
            }
            case 2: 
            case 3: 
            case 6: {
                boolean bl4 = bl;
                if (object != Type.LEFT) {
                    bl4 = bl;
                    if (object != Type.RIGHT) {
                        if (object == Type.CENTER_X) {
                            return true;
                        }
                        bl4 = false;
                    }
                }
                return bl4;
            }
            case 1: 
        }
        if (object != Type.BASELINE) {
            return true;
        }
        return false;
    }

    public boolean isSnapCompatibleWith(ConstraintAnchor constraintAnchor) {
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
                    case TOP: 
                }
                return true;
            }
            case CENTER_X: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case RIGHT: {
                        return true;
                    }
                    case LEFT: 
                }
                return true;
            }
            case BOTTOM: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 4) {
                    if (n != 7) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case TOP: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 5) {
                    if (n != 7) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case RIGHT: {
                int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
                if (n != 2) {
                    if (n != 6) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            case LEFT: 
        }
        int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[constraintAnchor.getType().ordinal()];
        if (n != 3) {
            if (n != 6) {
                return false;
            }
            return true;
        }
        return true;
    }

    public boolean isValidConnection(ConstraintAnchor constraintAnchor) {
        Type type;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        if (constraintAnchor == null) {
            return false;
        }
        Type type2 = constraintAnchor.getType();
        if (type2 == (type = this.mType)) {
            if (type == Type.CENTER) {
                return false;
            }
            if (this.mType == Type.BASELINE) {
                if (constraintAnchor.getOwner().hasBaseline()) {
                    if (!this.getOwner().hasBaseline()) {
                        return false;
                    }
                    return true;
                }
                return false;
            }
            return true;
        }
        switch (this.mType) {
            default: {
                return false;
            }
            case TOP: 
            case BOTTOM: {
                boolean bl4 = type2 == Type.TOP || type2 == Type.BOTTOM;
                if (constraintAnchor.getOwner() instanceof Guideline) {
                    bl4 = !bl4 && type2 != Type.CENTER_Y ? bl3 : true;
                    return bl4;
                }
                return bl4;
            }
            case LEFT: 
            case RIGHT: {
                boolean bl5 = type2 == Type.LEFT || type2 == Type.RIGHT;
                if (constraintAnchor.getOwner() instanceof Guideline) {
                    bl5 = !bl5 && type2 != Type.CENTER_X ? bl : true;
                    return bl5;
                }
                return bl5;
            }
            case CENTER: 
        }
        boolean bl6 = bl2;
        if (type2 != Type.BASELINE) {
            bl6 = bl2;
            if (type2 != Type.CENTER_X) {
                bl6 = bl2;
                if (type2 != Type.CENTER_Y) {
                    bl6 = true;
                }
            }
        }
        return bl6;
    }

    public boolean isVerticalAnchor() {
        int n = .$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[this.mType.ordinal()];
        if (n != 6) {
            switch (n) {
                default: {
                    return true;
                }
                case 1: 
                case 2: 
                case 3: 
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

    public void resetSolverVariable(Cache object) {
        object = this.mSolverVariable;
        if (object == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED);
            return;
        }
        object.reset();
    }

    public void setConnectionCreator(int n) {
        this.mConnectionCreator = n;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.mConnectionType = connectionType;
    }

    public void setGoneMargin(int n) {
        if (this.isConnected()) {
            this.mGoneMargin = n;
            return;
        }
    }

    public void setGroup(int n) {
        this.mGroup = n;
    }

    public void setMargin(int n) {
        if (this.isConnected()) {
            this.mMargin = n;
            return;
        }
    }

    public void setStrength(Strength strength) {
        if (this.isConnected()) {
            this.mStrength = strength;
            return;
        }
    }

    public String toString() {
        Object object = new HashSet();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mOwner.getDebugName());
        stringBuilder.append(":");
        stringBuilder.append(this.mType.toString());
        if (this.mTarget != null) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(" connected to ");
            stringBuilder2.append(this.mTarget.toString((HashSet<ConstraintAnchor>)object));
            object = stringBuilder2.toString();
        } else {
            object = "";
        }
        stringBuilder.append((String)object);
        return stringBuilder.toString();
    }

    public static enum ConnectionType {
        RELAXED,
        STRICT;
        

        private ConnectionType() {
        }
    }

    public static enum Strength {
        NONE,
        STRONG,
        WEAK;
        

        private Strength() {
        }
    }

    public static enum Type {
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y;
        

        private Type() {
        }
    }

}

