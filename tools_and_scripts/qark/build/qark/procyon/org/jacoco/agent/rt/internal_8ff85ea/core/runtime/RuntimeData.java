// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

import org.jacoco.agent.rt.internal_8ff85ea.core.data.ExecutionData;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.SessionInfo;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.ISessionInfoVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.IExecutionDataVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr.InstrSupport;
import org.jacoco.agent.rt.internal_8ff85ea.asm.MethodVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.data.ExecutionDataStore;

public class RuntimeData
{
    private String sessionId;
    private long startTimeStamp;
    protected final ExecutionDataStore store;
    
    public RuntimeData() {
        this.store = new ExecutionDataStore();
        this.sessionId = "<none>";
        this.startTimeStamp = System.currentTimeMillis();
    }
    
    public static void generateAccessCall(final long n, final String s, final int n2, final MethodVisitor methodVisitor) {
        generateArgumentArray(n, s, n2, methodVisitor);
        methodVisitor.visitInsn(90);
        methodVisitor.visitMethodInsn(182, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false);
        methodVisitor.visitInsn(87);
        methodVisitor.visitInsn(3);
        methodVisitor.visitInsn(50);
        methodVisitor.visitTypeInsn(192, "[Z");
    }
    
    public static void generateArgumentArray(final long n, final String s, final int n2, final MethodVisitor methodVisitor) {
        methodVisitor.visitInsn(6);
        methodVisitor.visitTypeInsn(189, "java/lang/Object");
        methodVisitor.visitInsn(89);
        methodVisitor.visitInsn(3);
        methodVisitor.visitLdcInsn(n);
        methodVisitor.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
        methodVisitor.visitInsn(83);
        methodVisitor.visitInsn(89);
        methodVisitor.visitInsn(4);
        methodVisitor.visitLdcInsn(s);
        methodVisitor.visitInsn(83);
        methodVisitor.visitInsn(89);
        methodVisitor.visitInsn(5);
        InstrSupport.push(methodVisitor, n2);
        methodVisitor.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        methodVisitor.visitInsn(83);
    }
    
    public final void collect(final IExecutionDataVisitor executionDataVisitor, final ISessionInfoVisitor sessionInfoVisitor, final boolean b) {
        synchronized (this.store) {
            sessionInfoVisitor.visitSessionInfo(new SessionInfo(this.sessionId, this.startTimeStamp, System.currentTimeMillis()));
            this.store.accept(executionDataVisitor);
            if (b) {
                this.reset();
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Object[]) {
            this.getProbes((Object[])o);
        }
        return super.equals(o);
    }
    
    public ExecutionData getExecutionData(final Long n, final String s, final int n2) {
        synchronized (this.store) {
            return this.store.get(n, s, n2);
        }
    }
    
    public void getProbes(final Object[] array) {
        array[0] = this.getExecutionData((Long)array[0], (String)array[1], (int)array[2]).getProbes();
    }
    
    public String getSessionId() {
        return this.sessionId;
    }
    
    public final void reset() {
        synchronized (this.store) {
            this.store.reset();
            this.startTimeStamp = System.currentTimeMillis();
        }
    }
    
    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }
}
