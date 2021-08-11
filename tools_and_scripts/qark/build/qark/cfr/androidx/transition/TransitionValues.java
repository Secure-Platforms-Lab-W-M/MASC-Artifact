/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package androidx.transition;

import android.view.View;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TransitionValues {
    final ArrayList<Transition> mTargetedTransitions = new ArrayList();
    public final Map<String, Object> values = new HashMap<String, Object>();
    public View view;

    @Deprecated
    public TransitionValues() {
    }

    public TransitionValues(View view) {
        this.view = view;
    }

    public boolean equals(Object object) {
        if (object instanceof TransitionValues && this.view == ((TransitionValues)object).view && this.values.equals(((TransitionValues)object).values)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.view.hashCode() * 31 + this.values.hashCode();
    }

    public String toString() {
        CharSequence charSequence = new StringBuilder();
        charSequence.append("TransitionValues@");
        charSequence.append(Integer.toHexString(this.hashCode()));
        charSequence.append(":\n");
        charSequence = charSequence.toString();
        Object object = new StringBuilder();
        object.append((String)charSequence);
        object.append("    view = ");
        object.append((Object)this.view);
        object.append("\n");
        charSequence = object.toString();
        object = new StringBuilder();
        object.append((String)charSequence);
        object.append("    values:");
        charSequence = object.toString();
        for (String string2 : this.values.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append("    ");
            stringBuilder.append(string2);
            stringBuilder.append(": ");
            stringBuilder.append(this.values.get(string2));
            stringBuilder.append("\n");
            charSequence = stringBuilder.toString();
        }
        return charSequence;
    }
}

