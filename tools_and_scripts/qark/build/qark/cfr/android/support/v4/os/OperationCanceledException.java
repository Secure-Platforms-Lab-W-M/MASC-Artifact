/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.os;

public class OperationCanceledException
extends RuntimeException {
    public OperationCanceledException() {
        this(null);
    }

    public OperationCanceledException(String string2) {
        if (string2 == null) {
            string2 = "The operation has been canceled.";
        }
        super(string2);
    }
}

