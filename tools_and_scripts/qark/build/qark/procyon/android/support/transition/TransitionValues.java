// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.util.Iterator;
import java.util.HashMap;
import android.view.View;
import java.util.Map;
import java.util.ArrayList;

public class TransitionValues
{
    final ArrayList<Transition> mTargetedTransitions;
    public final Map<String, Object> values;
    public View view;
    
    public TransitionValues() {
        this.values = new HashMap<String, Object>();
        this.mTargetedTransitions = new ArrayList<Transition>();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof TransitionValues) {
            if (this.view == ((TransitionValues)o).view) {
                if (this.values.equals(((TransitionValues)o).values)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.view.hashCode() * 31 + this.values.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TransitionValues@");
        sb.append(Integer.toHexString(this.hashCode()));
        sb.append(":\n");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append("    view = ");
        sb2.append(this.view);
        sb2.append("\n");
        final String string2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string2);
        sb3.append("    values:");
        String s = sb3.toString();
        for (final String s2 : this.values.keySet()) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(s);
            sb4.append("    ");
            sb4.append(s2);
            sb4.append(": ");
            sb4.append(this.values.get(s2));
            sb4.append("\n");
            s = sb4.toString();
        }
        return s;
    }
}
