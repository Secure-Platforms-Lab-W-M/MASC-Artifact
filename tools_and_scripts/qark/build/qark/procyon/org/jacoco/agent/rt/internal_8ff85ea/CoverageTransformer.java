// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea;

import java.lang.instrument.IllegalClassFormatException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IExecutionDataAccessorGenerator;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.AgentOptions;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IRuntime;
import org.jacoco.agent.rt.internal_8ff85ea.core.instr.Instrumenter;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.WildcardMatcher;
import java.lang.instrument.ClassFileTransformer;

public class CoverageTransformer implements ClassFileTransformer
{
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
        final String name = CoverageTransformer.class.getName();
        AGENT_PREFIX = toVMName(name.substring(0, name.lastIndexOf(46)));
    }
    
    public CoverageTransformer(final IRuntime runtime, final AgentOptions agentOptions, final IExceptionLogger logger) {
        this.instrumenter = new Instrumenter(runtime);
        this.logger = logger;
        this.includes = new WildcardMatcher(toVMName(agentOptions.getIncludes()));
        this.excludes = new WildcardMatcher(toVMName(agentOptions.getExcludes()));
        this.exclClassloader = new WildcardMatcher(agentOptions.getExclClassloader());
        this.classFileDumper = new ClassFileDumper(agentOptions.getClassDumpDir());
        this.inclBootstrapClasses = agentOptions.getInclBootstrapClasses();
        this.inclNoLocationClasses = agentOptions.getInclNoLocationClasses();
    }
    
    private boolean hasSourceLocation(final ProtectionDomain protectionDomain) {
        boolean b = false;
        if (protectionDomain == null) {
            return false;
        }
        final CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            return false;
        }
        if (codeSource.getLocation() != null) {
            b = true;
        }
        return b;
    }
    
    private static String toVMName(final String s) {
        return s.replace('.', '/');
    }
    
    boolean filter(final ClassLoader classLoader, final String s, final ProtectionDomain protectionDomain) {
        final boolean b = false;
        if (classLoader == null) {
            if (!this.inclBootstrapClasses) {
                return false;
            }
        }
        else {
            if (!this.inclNoLocationClasses && !this.hasSourceLocation(protectionDomain)) {
                return false;
            }
            if (this.exclClassloader.matches(classLoader.getClass().getName())) {
                return false;
            }
        }
        boolean b2 = b;
        if (!s.startsWith(CoverageTransformer.AGENT_PREFIX)) {
            b2 = b;
            if (this.includes.matches(s)) {
                b2 = b;
                if (!this.excludes.matches(s)) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public byte[] transform(final ClassLoader classLoader, final String s, final Class<?> clazz, final ProtectionDomain protectionDomain, final byte[] array) throws IllegalClassFormatException {
        if (clazz != null) {
            return null;
        }
        if (!this.filter(classLoader, s, protectionDomain)) {
            return null;
        }
        try {
            this.classFileDumper.dump(s, array);
            return this.instrumenter.instrument(array, s);
        }
        catch (Exception ex2) {
            final IllegalClassFormatException ex = new IllegalClassFormatException(ex2.getMessage());
            ex.initCause(ex2);
            this.logger.logExeption(ex);
            throw ex;
        }
    }
}
