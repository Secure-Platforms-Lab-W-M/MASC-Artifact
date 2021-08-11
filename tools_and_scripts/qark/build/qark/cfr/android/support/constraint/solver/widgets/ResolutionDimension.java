/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ResolutionNode;

public class ResolutionDimension
extends ResolutionNode {
    float value = 0.0f;

    public void remove() {
        this.state = 2;
    }

    @Override
    public void reset() {
        super.reset();
        this.value = 0.0f;
    }

    public void resolve(int n) {
        if (this.state != 0 && this.value == (float)n) {
            return;
        }
        this.value = n;
        if (this.state == 1) {
            this.invalidate();
        }
        this.didResolve();
    }
}

