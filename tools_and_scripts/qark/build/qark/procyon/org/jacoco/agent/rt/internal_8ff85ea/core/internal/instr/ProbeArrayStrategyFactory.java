// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.internal.instr;

import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.ClassProbesVisitor;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.flow.ClassProbesAdapter;
import org.jacoco.agent.rt.internal_8ff85ea.core.internal.data.CRC64;
import org.jacoco.agent.rt.internal_8ff85ea.core.runtime.IExecutionDataAccessorGenerator;
import org.jacoco.agent.rt.internal_8ff85ea.asm.ClassReader;

public final class ProbeArrayStrategyFactory
{
    private ProbeArrayStrategyFactory() {
    }
    
    public static IProbeArrayStrategy createFor(final ClassReader classReader, final IExecutionDataAccessorGenerator executionDataAccessorGenerator) {
        final String className = classReader.getClassName();
        final int version = getVersion(classReader);
        final long checksum = CRC64.checksum(classReader.b);
        final boolean b = version >= 50;
        if (!isInterface(classReader)) {
            return new ClassFieldProbeArrayStrategy(className, checksum, b, executionDataAccessorGenerator);
        }
        final ProbeCounter probeCounter = getProbeCounter(classReader);
        if (probeCounter.getCount() == 0) {
            return new NoneProbeArrayStrategy();
        }
        if (version >= 52 && probeCounter.hasMethods()) {
            return new InterfaceFieldProbeArrayStrategy(className, checksum, probeCounter.getCount(), executionDataAccessorGenerator);
        }
        return new LocalProbeArrayStrategy(className, checksum, probeCounter.getCount(), executionDataAccessorGenerator);
    }
    
    private static ProbeCounter getProbeCounter(final ClassReader classReader) {
        final ProbeCounter probeCounter = new ProbeCounter();
        classReader.accept(new ClassProbesAdapter(probeCounter, false), 0);
        return probeCounter;
    }
    
    private static int getVersion(final ClassReader classReader) {
        return classReader.readShort(6);
    }
    
    private static boolean isInterface(final ClassReader classReader) {
        return (classReader.getAccess() & 0x200) != 0x0;
    }
}
