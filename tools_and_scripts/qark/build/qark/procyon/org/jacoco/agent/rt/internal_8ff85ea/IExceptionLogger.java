// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea;

public interface IExceptionLogger
{
    public static final IExceptionLogger SYSTEM_ERR = new IExceptionLogger() {
        @Override
        public void logExeption(final Exception ex) {
            ex.printStackTrace();
        }
    };
    
    void logExeption(final Exception p0);
}
