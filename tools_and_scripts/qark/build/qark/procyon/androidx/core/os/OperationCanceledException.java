// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.os;

public class OperationCanceledException extends RuntimeException
{
    public OperationCanceledException() {
        this((String)null);
    }
    
    public OperationCanceledException(String s) {
        if (s == null) {
            s = "The operation has been canceled.";
        }
        super(s);
    }
}
