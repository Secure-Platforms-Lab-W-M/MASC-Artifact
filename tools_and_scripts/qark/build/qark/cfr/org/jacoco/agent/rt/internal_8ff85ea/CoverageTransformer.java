/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import org.jacoco.agent.rt.internal_8ff85ea.ClassFileDumper;
import org.jacoco.agent.rt.internal_8ff85ea.IExceptionLogger;
import org.jacoco.agent.rt.internal_8ff85ea.core.instr.Instrumenter;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IExecutionDataAccessorGenerator;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IRuntime;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.WildcardMatcher;

public class CoverageTransformer
implements ClassFileTransformer {
    private static final String AGENT_PREFIX;
    private final ClassFileDumper classFileDumper;
    private final WildcardMatcher exclClassloader;
    private final WildcardMatcher excludes;
    private final boolean inclBootstrapClasses;
    private final boolean inclNoLocationClasses;
    private final WildcardMatcher includes;
    private final Instrumenter instrumenter;
    private final IExceptionLogger logger;

    static {
        String string2 = CoverageTransformer.class.getName();
        AGENT_PREFIX = CoverageTransformer.toVMName(string2.substring(0, string2.lastIndexOf(46)));
    }

    public CoverageTransformer(IRuntime iRuntime, AgentOptions agentOptions, IExceptionLogger iExceptionLogger) {
        this.instrumenter = new Instrumenter(iRuntime);
        this.logger = iExceptionLogger;
        this.includes = new WildcardMatcher(CoverageTransformer.toVMName(agentOptions.getIncludes()));
        this.excludes = new WildcardMatcher(CoverageTransformer.toVMName(agentOptions.getExcludes()));
        this.exclClassloader = new WildcardMatcher(agentOptions.getExclClassloader());
        this.classFileDumper = new ClassFileDumper(agentOptions.getClassDumpDir());
        this.inclBootstrapClasses = agentOptions.getInclBootstrapClasses();
        this.inclNoLocationClasses = agentOptions.getInclNoLocationClasses();
    }

    private boolean hasSourceLocation(ProtectionDomain object) {
        boolean bl = false;
        if (object == null) {
            return false;
        }
        if ((object = object.getCodeSource()) == null) {
            return false;
        }
        if (object.getLocation() != null) {
            bl = true;
        }
        return bl;
    }

    private static String toVMName(String string2) {
        return string2.replace('.', '/');
    }

    boolean filter(ClassLoader classLoader, String string2, ProtectionDomain protectionDomain) {
        boolean bl = false;
        if (classLoader == null) {
            if (!this.inclBootstrapClasses) {
                return false;
            }
        } else {
            if (!this.inclNoLocationClasses && !this.hasSourceLocation(protectionDomain)) {
                return false;
            }
            if (this.exclClassloader.matches(classLoader.getClass().getName())) {
                return false;
            }
        }
        boolean bl2 = bl;
        if (!string2.startsWith(AGENT_PREFIX)) {
            bl2 = bl;
            if (this.includes.matches(string2)) {
                bl2 = bl;
                if (!this.excludes.matches(string2)) {
                    bl2 = true;
                }
            }
        }
        return bl2;
    }

    @Override
    public byte[] transform(ClassLoader arrby, String object, Class<?> class_, ProtectionDomain protectionDomain, byte[] arrby2) throws IllegalClassFormatException {
        if (class_ != null) {
            return null;
        }
        if (!this.filter((ClassLoader)arrby, (String)object, protectionDomain)) {
            return null;
        }
        try {
            this.classFileDumper.dump((String)object, arrby2);
            arrby = this.instrumenter.instrument(arrby2, (String)object);
            return arrby;
        }
        catch (Exception exception) {
            object = new IllegalClassFormatException(exception.getMessage());
            object.initCause(exception);
            this.logger.logExeption((Exception)object);
            throw object;
        }
    }
}

