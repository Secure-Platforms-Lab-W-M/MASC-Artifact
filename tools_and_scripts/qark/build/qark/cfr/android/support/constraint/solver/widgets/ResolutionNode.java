/*
 * Decompiled with CFR 0_124.
 */
package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.support.constraint.solver.widgets.ResolutionDimension;
import java.util.HashSet;
import java.util.Iterator;

public class ResolutionNode {
    public static final int REMOVED = 2;
    public static final int RESOLVED = 1;
    public static final int UNRESOLVED = 0;
    HashSet<ResolutionNode> dependents = new HashSet(2);
    int state = 0;

    public void addDependent(ResolutionNode resolutionNode) {
        this.dependents.add(resolutionNode);
    }

    public void didResolve() {
        this.state = 1;
        Iterator<ResolutionNode> iterator = this.dependents.iterator();
        while (iterator.hasNext()) {
            iterator.next().resolve();
        }
    }

    public void invalidate() {
        this.state = 0;
        Iterator<ResolutionNode> iterator = this.dependents.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidate();
        }
    }

    public void invalidateAnchors() {
        if (this instanceof ResolutionAnchor) {
            this.state = 0;
        }
        Iterator<ResolutionNode> iterator = this.dependents.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidateAnchors();
        }
    }

    public boolean isResolved() {
        if (this.state == 1) {
            return true;
        }
        return false;
    }

    public void remove(ResolutionDimension resolutionDimension) {
    }

    public void reset() {
        this.state = 0;
        this.dependents.clear();
    }

    public void resolve() {
    }
}

