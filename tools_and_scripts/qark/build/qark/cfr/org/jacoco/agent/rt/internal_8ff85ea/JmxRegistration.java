/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Callable;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import org.jacoco.agent.rt.IAgent;

class JmxRegistration
implements Callable<Void> {
    private static final String JMX_NAME = "org.jacoco:type=Runtime";
    private final ObjectName name = new ObjectName("org.jacoco:type=Runtime");
    private final MBeanServer server = ManagementFactory.getPlatformMBeanServer();

    JmxRegistration(IAgent iAgent) throws Exception {
        this.server.registerMBean(new StandardMBean(iAgent, IAgent.class), this.name);
    }

    @Override
    public Void call() throws Exception {
        this.server.unregisterMBean(this.name);
        return null;
    }
}

