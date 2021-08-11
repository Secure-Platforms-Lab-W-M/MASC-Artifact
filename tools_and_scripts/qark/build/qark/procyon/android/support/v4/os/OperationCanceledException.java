// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.os;

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
