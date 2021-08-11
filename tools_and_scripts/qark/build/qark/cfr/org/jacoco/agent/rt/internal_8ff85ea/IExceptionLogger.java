/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea;

public interface IExceptionLogger {
    public static final IExceptionLogger SYSTEM_ERR = new IExceptionLogger(){

        @Override
        public void logExeption(Exception exception) {
            exception.printStackTrace();
        }
    };

    public void logExeption(Exception var1);

}

