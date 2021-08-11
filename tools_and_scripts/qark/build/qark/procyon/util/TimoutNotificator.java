// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.util.HashSet;

public class TimoutNotificator implements Runnable
{
    public static TimoutNotificator instance;
    private volatile long curTime;
    public HashSet listeners;
    private boolean stopped;
    public boolean threadAvailable;
    
    static {
        TimoutNotificator.instance = new TimoutNotificator();
    }
    
    public TimoutNotificator() {
        this.listeners = new HashSet();
        this.threadAvailable = false;
        this.stopped = false;
        this.curTime = 0L;
    }
    
    public static TimoutNotificator getInstance() {
        return TimoutNotificator.instance;
    }
    
    public static TimoutNotificator getNewInstance() {
        return new TimoutNotificator();
    }
    
    public long getCurrentTime() {
        if (this.threadAvailable) {
            return this.curTime;
        }
        return System.currentTimeMillis();
    }
    
    public void register(final TimeoutListener timeoutListener) {
        synchronized (this) {
            this.listeners.add(timeoutListener);
            if (!this.threadAvailable) {
                this.curTime = System.currentTimeMillis();
                this.threadAvailable = true;
                final Thread thread = new Thread(this);
                thread.setDaemon(true);
                thread.start();
            }
        }
    }
    
    @Override
    public void run() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/Vector.<init>:()V
        //     7: astore          5
        //     9: iconst_0       
        //    10: istore_1       
        //    11: iload_1        
        //    12: ifne            214
        //    15: aload_0        
        //    16: monitorenter   
        //    17: aload           5
        //    19: invokevirtual   java/util/Vector.removeAllElements:()V
        //    22: aload_0        
        //    23: invokestatic    java/lang/System.currentTimeMillis:()J
        //    26: putfield        util/TimoutNotificator.curTime:J
        //    29: aload_0        
        //    30: ldc2_w          1000
        //    33: invokevirtual   java/lang/Object.wait:(J)V
        //    36: goto            46
        //    39: astore          6
        //    41: aload           6
        //    43: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //    46: aload_0        
        //    47: aload_0        
        //    48: getfield        util/TimoutNotificator.curTime:J
        //    51: ldc2_w          1000
        //    54: ladd           
        //    55: putfield        util/TimoutNotificator.curTime:J
        //    58: aload_0        
        //    59: getfield        util/TimoutNotificator.stopped:Z
        //    62: ifne            134
        //    65: aload_0        
        //    66: getfield        util/TimoutNotificator.listeners:Ljava/util/HashSet;
        //    69: iconst_0       
        //    70: anewarray       Lutil/TimeoutListener;
        //    73: invokevirtual   java/util/HashSet.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    76: checkcast       [Lutil/TimeoutListener;
        //    79: astore          6
        //    81: iconst_0       
        //    82: istore_1       
        //    83: iload_1        
        //    84: aload           6
        //    86: arraylength    
        //    87: if_icmpge       134
        //    90: aload           6
        //    92: iload_1        
        //    93: aaload         
        //    94: invokeinterface util/TimeoutListener.getTimoutTime:()J
        //    99: lstore_3       
        //   100: aload_0        
        //   101: getfield        util/TimoutNotificator.curTime:J
        //   104: lload_3        
        //   105: lcmp           
        //   106: ifle            215
        //   109: aload_0        
        //   110: getfield        util/TimoutNotificator.listeners:Ljava/util/HashSet;
        //   113: aload           6
        //   115: iload_1        
        //   116: aaload         
        //   117: invokevirtual   java/util/HashSet.remove:(Ljava/lang/Object;)Z
        //   120: pop            
        //   121: aload           5
        //   123: aload           6
        //   125: iload_1        
        //   126: aaload         
        //   127: invokevirtual   java/util/Vector.add:(Ljava/lang/Object;)Z
        //   130: pop            
        //   131: goto            215
        //   134: aload_0        
        //   135: getfield        util/TimoutNotificator.listeners:Ljava/util/HashSet;
        //   138: invokevirtual   java/util/HashSet.isEmpty:()Z
        //   141: ifne            227
        //   144: aload_0        
        //   145: getfield        util/TimoutNotificator.stopped:Z
        //   148: ifeq            222
        //   151: goto            227
        //   154: iload_1        
        //   155: ifeq            163
        //   158: aload_0        
        //   159: iconst_0       
        //   160: putfield        util/TimoutNotificator.threadAvailable:Z
        //   163: aload_0        
        //   164: monitorexit    
        //   165: aload           5
        //   167: iconst_0       
        //   168: anewarray       Lutil/TimeoutListener;
        //   171: invokevirtual   java/util/Vector.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   174: checkcast       [Lutil/TimeoutListener;
        //   177: astore          6
        //   179: iconst_0       
        //   180: istore_2       
        //   181: iload_2        
        //   182: aload           6
        //   184: arraylength    
        //   185: if_icmpge       204
        //   188: aload           6
        //   190: iload_2        
        //   191: aaload         
        //   192: invokeinterface util/TimeoutListener.timeoutNotification:()V
        //   197: iload_2        
        //   198: iconst_1       
        //   199: iadd           
        //   200: istore_2       
        //   201: goto            181
        //   204: goto            11
        //   207: astore          5
        //   209: aload_0        
        //   210: monitorexit    
        //   211: aload           5
        //   213: athrow         
        //   214: return         
        //   215: iload_1        
        //   216: iconst_1       
        //   217: iadd           
        //   218: istore_1       
        //   219: goto            83
        //   222: iconst_0       
        //   223: istore_1       
        //   224: goto            154
        //   227: iconst_1       
        //   228: istore_1       
        //   229: goto            154
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  17     29     207    214    Any
        //  29     36     39     46     Ljava/lang/InterruptedException;
        //  29     36     207    214    Any
        //  41     46     207    214    Any
        //  46     81     207    214    Any
        //  83     131    207    214    Any
        //  134    151    207    214    Any
        //  158    163    207    214    Any
        //  163    165    207    214    Any
        //  209    211    207    214    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public void shutdown() {
        synchronized (this) {
            this.stopped = true;
            this.notifyAll();
        }
    }
    
    public void unregister(final TimeoutListener timeoutListener) {
        synchronized (this) {
            this.listeners.remove(timeoutListener);
        }
    }
}
