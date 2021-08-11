// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

class CurrentFrame extends Frame
{
    @Override
    void execute(final int n, final int n2, final ClassWriter classWriter, final Item item) {
        super.execute(n, n2, classWriter, item);
        final Frame frame = new Frame();
        this.merge(classWriter, frame, 0);
        this.set(frame);
        this.owner.inputStackTop = 0;
    }
}
