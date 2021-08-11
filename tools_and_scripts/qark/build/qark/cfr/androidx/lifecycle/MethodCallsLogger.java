/*
 * Decompiled with CFR 0_124.
 */
package androidx.lifecycle;

import java.util.HashMap;
import java.util.Map;

public class MethodCallsLogger {
    private Map<String, Integer> mCalledMethods = new HashMap<String, Integer>();

    public boolean approveCall(String string2, int n) {
        Integer n2 = this.mCalledMethods.get(string2);
        boolean bl = false;
        int n3 = n2 != null ? n2 : 0;
        boolean bl2 = (n3 & n) != 0;
        this.mCalledMethods.put(string2, n3 | n);
        if (!bl2) {
            bl = true;
        }
        return bl;
    }
}

