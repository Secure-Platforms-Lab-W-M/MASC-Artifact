// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Map;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class MethodCallsLogger
{
    private Map<String, Integer> mCalledMethods;
    
    public MethodCallsLogger() {
        this.mCalledMethods = new HashMap<String, Integer>();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean approveCall(final String s, final int n) {
        final Integer n2 = this.mCalledMethods.get(s);
        int intValue;
        if (n2 != null) {
            intValue = n2;
        }
        else {
            intValue = 0;
        }
        boolean b;
        if ((intValue & n) != 0x0) {
            b = true;
        }
        else {
            b = false;
        }
        this.mCalledMethods.put(s, intValue | n);
        return !b;
    }
}
