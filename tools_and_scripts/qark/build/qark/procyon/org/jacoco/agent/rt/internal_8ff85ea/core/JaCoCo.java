// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core;

import java.util.ResourceBundle;

public final class JaCoCo
{
    public static final String HOMEURL;
    public static final String RUNTIMEPACKAGE;
    public static final String VERSION;
    
    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.jacoco.agent.rt.internal_8ff85ea.core.jacoco");
        VERSION = bundle.getString("VERSION");
        HOMEURL = bundle.getString("HOMEURL");
        RUNTIMEPACKAGE = bundle.getString("RUNTIMEPACKAGE");
    }
    
    private JaCoCo() {
    }
}
