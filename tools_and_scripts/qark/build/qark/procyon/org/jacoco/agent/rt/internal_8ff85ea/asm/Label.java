// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

public class Label
{
    static final int DEBUG = 1;
    static final int JSR = 128;
    static final int PUSHED = 8;
    static final int REACHABLE = 64;
    static final int RESIZED = 4;
    static final int RESOLVED = 2;
    static final int RET = 256;
    static final int STORE = 32;
    static final int SUBROUTINE = 512;
    static final int TARGET = 16;
    static final int VISITED = 1024;
    static final int VISITED2 = 2048;
    Frame frame;
    public Object info;
    int inputStackTop;
    int line;
    Label next;
    int outputStackMax;
    int position;
    private int referenceCount;
    private int[] srcAndRefPositions;
    int status;
    Label successor;
    Edge successors;
    
    private void addReference(int n, final int n2) {
        if (this.srcAndRefPositions == null) {
            this.srcAndRefPositions = new int[6];
        }
        if (this.referenceCount >= this.srcAndRefPositions.length) {
            final int[] srcAndRefPositions = new int[this.srcAndRefPositions.length + 6];
            System.arraycopy(this.srcAndRefPositions, 0, srcAndRefPositions, 0, this.srcAndRefPositions.length);
            this.srcAndRefPositions = srcAndRefPositions;
        }
        this.srcAndRefPositions[this.referenceCount++] = n;
        final int[] srcAndRefPositions2 = this.srcAndRefPositions;
        n = this.referenceCount++;
        srcAndRefPositions2[n] = n2;
    }
    
    void addToSubroutine(final long n, int n2) {
        if ((this.status & 0x400) == 0x0) {
            this.status |= 0x400;
            this.srcAndRefPositions = new int[n2 / 32 + 1];
        }
        final int[] srcAndRefPositions = this.srcAndRefPositions;
        n2 = (int)(n >>> 32);
        srcAndRefPositions[n2] |= (int)n;
    }
    
    Label getFirst() {
        if (this.frame == null) {
            return this;
        }
        return this.frame.owner;
    }
    
    public int getOffset() {
        if ((this.status & 0x2) != 0x0) {
            return this.position;
        }
        throw new IllegalStateException("Label offset position has not been resolved yet");
    }
    
    boolean inSameSubroutine(final Label label) {
        if ((this.status & 0x400) == 0x0) {
            return false;
        }
        if ((label.status & 0x400) == 0x0) {
            return false;
        }
        for (int i = 0; i < this.srcAndRefPositions.length; ++i) {
            if ((this.srcAndRefPositions[i] & label.srcAndRefPositions[i]) != 0x0) {
                return true;
            }
        }
        return false;
    }
    
    boolean inSubroutine(final long n) {
        final int status = this.status;
        boolean b = false;
        if ((status & 0x400) != 0x0) {
            if ((this.srcAndRefPositions[(int)(n >>> 32)] & (int)n) != 0x0) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    void put(final MethodWriter methodWriter, final ByteVector byteVector, final int n, final boolean b) {
        if ((this.status & 0x2) == 0x0) {
            if (b) {
                this.addReference(-1 - n, byteVector.length);
                byteVector.putInt(-1);
                return;
            }
            this.addReference(n, byteVector.length);
            byteVector.putShort(-1);
        }
        else {
            if (b) {
                byteVector.putInt(this.position - n);
                return;
            }
            byteVector.putShort(this.position - n);
        }
    }
    
    boolean resolve(final MethodWriter methodWriter, final int position, final byte[] array) {
        boolean b = false;
        this.status |= 0x2;
        this.position = position;
        int n;
        for (int i = 0; i < this.referenceCount; i = n + 1) {
            final int[] srcAndRefPositions = this.srcAndRefPositions;
            n = i + 1;
            final int n2 = srcAndRefPositions[i];
            final int n3 = this.srcAndRefPositions[n];
            if (n2 >= 0) {
                final int n4 = position - n2;
                if (n4 < -32768 || n4 > 32767) {
                    final int n5 = array[n3 - 1] & 0xFF;
                    if (n5 <= 168) {
                        array[n3 - 1] = (byte)(n5 + 49);
                    }
                    else {
                        array[n3 - 1] = (byte)(n5 + 20);
                    }
                    b = true;
                }
                array[n3] = (byte)(n4 >>> 8);
                array[n3 + 1] = (byte)n4;
            }
            else {
                final int n6 = position + n2 + 1;
                final int n7 = n3 + 1;
                array[n3] = (byte)(n6 >>> 24);
                final int n8 = n7 + 1;
                array[n7] = (byte)(n6 >>> 16);
                array[n8] = (byte)(n6 >>> 8);
                array[n8 + 1] = (byte)n6;
            }
        }
        return b;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("L");
        sb.append(System.identityHashCode(this));
        return sb.toString();
    }
    
    void visitSubroutine(final Label label, final long n, final int n2) {
        Label next = this;
        while (true) {
            final Label label2 = next;
            if (label2 == null) {
                break;
            }
            next = label2.next;
            label2.next = null;
            if (label != null) {
                if ((label2.status & 0x800) != 0x0) {
                    continue;
                }
                label2.status |= 0x800;
                if ((label2.status & 0x100) != 0x0 && !label2.inSameSubroutine(label)) {
                    final Edge successors = new Edge();
                    successors.info = label2.inputStackTop;
                    successors.successor = label.successors.successor;
                    successors.next = label2.successors;
                    label2.successors = successors;
                }
            }
            else {
                if (label2.inSubroutine(n)) {
                    continue;
                }
                label2.addToSubroutine(n, n2);
            }
            Label successor;
            for (Edge edge = label2.successors; edge != null; edge = edge.next, next = successor) {
                if ((label2.status & 0x80) != 0x0) {
                    successor = next;
                    if (edge == label2.successors.next) {
                        continue;
                    }
                }
                successor = next;
                if (edge.successor.next == null) {
                    edge.successor.next = next;
                    successor = edge.successor;
                }
            }
        }
    }
}
