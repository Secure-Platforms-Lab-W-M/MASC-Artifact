// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea;

import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;
import org.jacoco.agent.rt.IAgent;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.concurrent.Callable;

class JmxRegistration implements Callable<Void>
{
    private static final String JMX_NAME = "org.jacoco:type=Runtime";
    private final ObjectName name;
    private final MBeanServer server;
    
    JmxRegistration(final IAgent agent) throws Exception {
        this.server = ManagementFactory.getPlatformMBeanServer();
        this.name = new ObjectName("org.jacoco:type=Runtime");
        this.server.registerMBean(new StandardMBean((T)agent, (Class<T>)IAgent.class), this.name);
    }
    
    @Override
    public Void call() throws Exception {
        this.server.unregisterMBean(this.name);
        return null;
    }
}
