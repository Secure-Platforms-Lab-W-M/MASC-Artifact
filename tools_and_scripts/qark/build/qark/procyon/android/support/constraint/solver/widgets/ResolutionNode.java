// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint.solver.widgets;

import java.util.Iterator;
import java.util.HashSet;

public class ResolutionNode
{
    public static final int REMOVED = 2;
    public static final int RESOLVED = 1;
    public static final int UNRESOLVED = 0;
    HashSet<ResolutionNode> dependents;
    int state;
    
    public ResolutionNode() {
        this.dependents = new HashSet<ResolutionNode>(2);
        this.state = 0;
    }
    
    public void addDependent(final ResolutionNode resolutionNode) {
        this.dependents.add(resolutionNode);
    }
    
    public void didResolve() {
        this.state = 1;
        final Iterator<ResolutionNode> iterator = this.dependents.iterator();
        while (iterator.hasNext()) {
            iterator.next().resolve();
        }
    }
    
    public void invalidate() {
        this.state = 0;
        final Iterator<ResolutionNode> iterator = this.dependents.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidate();
        }
    }
    
    public void invalidateAnchors() {
        if (this instanceof ResolutionAnchor) {
            this.state = 0;
        }
        final Iterator<ResolutionNode> iterator = this.dependents.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidateAnchors();
        }
    }
    
    public boolean isResolved() {
        return this.state == 1;
    }
    
    public void remove(final ResolutionDimension resolutionDimension) {
    }
    
    public void reset() {
        this.state = 0;
        this.dependents.clear();
    }
    
    public void resolve() {
    }
}
