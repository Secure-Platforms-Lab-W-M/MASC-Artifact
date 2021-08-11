// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.os;

import android.os.Build$VERSION;

public final class CancellationSignal
{
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;
    
    private void waitForCancelFinishedLocked() {
    Label_0011_Outer:
        while (this.mCancelInProgress) {
            while (true) {
                try {
                    this.wait();
                    continue Label_0011_Outer;
                }
                catch (InterruptedException ex) {
                    continue;
                }
                break;
            }
            break;
        }
    }
    
    public void cancel() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        androidx/core/os/CancellationSignal.mIsCanceled:Z
        //     6: ifeq            12
        //     9: aload_0        
        //    10: monitorexit    
        //    11: return         
        //    12: aload_0        
        //    13: iconst_1       
        //    14: putfield        androidx/core/os/CancellationSignal.mIsCanceled:Z
        //    17: aload_0        
        //    18: iconst_1       
        //    19: putfield        androidx/core/os/CancellationSignal.mCancelInProgress:Z
        //    22: aload_0        
        //    23: getfield        androidx/core/os/CancellationSignal.mOnCancelListener:Landroidx/core/os/CancellationSignal$OnCancelListener;
        //    26: astore_1       
        //    27: aload_0        
        //    28: getfield        androidx/core/os/CancellationSignal.mCancellationSignalObj:Ljava/lang/Object;
        //    31: astore_2       
        //    32: aload_0        
        //    33: monitorexit    
        //    34: aload_1        
        //    35: ifnull          47
        //    38: aload_1        
        //    39: invokeinterface androidx/core/os/CancellationSignal$OnCancelListener.onCancel:()V
        //    44: goto            47
        //    47: aload_2        
        //    48: ifnull          89
        //    51: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    54: bipush          16
        //    56: if_icmplt       89
        //    59: aload_2        
        //    60: checkcast       Landroid/os/CancellationSignal;
        //    63: invokevirtual   android/os/CancellationSignal.cancel:()V
        //    66: goto            89
        //    69: aload_0        
        //    70: monitorenter   
        //    71: aload_0        
        //    72: iconst_0       
        //    73: putfield        androidx/core/os/CancellationSignal.mCancelInProgress:Z
        //    76: aload_0        
        //    77: invokevirtual   java/lang/Object.notifyAll:()V
        //    80: aload_0        
        //    81: monitorexit    
        //    82: aload_1        
        //    83: athrow         
        //    84: astore_1       
        //    85: aload_0        
        //    86: monitorexit    
        //    87: aload_1        
        //    88: athrow         
        //    89: aload_0        
        //    90: monitorenter   
        //    91: aload_0        
        //    92: iconst_0       
        //    93: putfield        androidx/core/os/CancellationSignal.mCancelInProgress:Z
        //    96: aload_0        
        //    97: invokevirtual   java/lang/Object.notifyAll:()V
        //   100: aload_0        
        //   101: monitorexit    
        //   102: return         
        //   103: astore_1       
        //   104: aload_0        
        //   105: monitorexit    
        //   106: aload_1        
        //   107: athrow         
        //   108: astore_1       
        //   109: aload_0        
        //   110: monitorexit    
        //   111: aload_1        
        //   112: athrow         
        //   113: astore_1       
        //   114: goto            69
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      11     108    113    Any
        //  12     34     108    113    Any
        //  38     44     113    89     Any
        //  51     66     113    89     Any
        //  71     82     84     89     Any
        //  85     87     84     89     Any
        //  91     102    103    108    Any
        //  104    106    103    108    Any
        //  109    111    108    113    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0047:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public Object getCancellationSignalObject() {
        if (Build$VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                final android.os.CancellationSignal mCancellationSignalObj = new android.os.CancellationSignal();
                this.mCancellationSignalObj = mCancellationSignalObj;
                if (this.mIsCanceled) {
                    mCancellationSignalObj.cancel();
                }
            }
            return this.mCancellationSignalObj;
        }
    }
    
    public boolean isCanceled() {
        synchronized (this) {
            return this.mIsCanceled;
        }
    }
    
    public void setOnCancelListener(final OnCancelListener mOnCancelListener) {
        synchronized (this) {
            this.waitForCancelFinishedLocked();
            if (this.mOnCancelListener == mOnCancelListener) {
                return;
            }
            this.mOnCancelListener = mOnCancelListener;
            if (this.mIsCanceled && mOnCancelListener != null) {
                // monitorexit(this)
                mOnCancelListener.onCancel();
            }
        }
    }
    
    public void throwIfCanceled() {
        if (!this.isCanceled()) {
            return;
        }
        throw new OperationCanceledException();
    }
    
    public interface OnCancelListener
    {
        void onCancel();
    }
}
