// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm.tree;

import java.util.ArrayList;
import java.util.List;
import org.jacoco.agent.rt.internal_8ff85ea.asm.AnnotationVisitor;

public class AnnotationNode extends AnnotationVisitor
{
    public String desc;
    public List<Object> values;
    
    public AnnotationNode(final int n, final String desc) {
        super(n);
        this.desc = desc;
    }
    
    public AnnotationNode(final String s) {
        this(327680, s);
        if (this.getClass() == AnnotationNode.class) {
            return;
        }
        throw new IllegalStateException();
    }
    
    AnnotationNode(final List<Object> values) {
        super(327680);
        this.values = values;
    }
    
    static void accept(AnnotationVisitor visitArray, final String s, final Object o) {
        if (visitArray != null) {
            final boolean b = o instanceof String[];
            int i = 0;
            if (b) {
                final String[] array = (String[])o;
                visitArray.visitEnum(s, array[0], array[1]);
                return;
            }
            if (o instanceof AnnotationNode) {
                final AnnotationNode annotationNode = (AnnotationNode)o;
                annotationNode.accept(visitArray.visitAnnotation(s, annotationNode.desc));
                return;
            }
            if (o instanceof List) {
                visitArray = visitArray.visitArray(s);
                if (visitArray != null) {
                    for (List list = (List)o; i < list.size(); ++i) {
                        accept(visitArray, null, list.get(i));
                    }
                    visitArray.visitEnd();
                }
                return;
            }
            visitArray.visit(s, o);
        }
    }
    
    public void accept(final AnnotationVisitor annotationVisitor) {
        if (annotationVisitor != null) {
            if (this.values != null) {
                for (int i = 0; i < this.values.size(); i += 2) {
                    accept(annotationVisitor, (String)this.values.get(i), this.values.get(i + 1));
                }
            }
            annotationVisitor.visitEnd();
        }
    }
    
    public void check(final int n) {
    }
    
    @Override
    public void visit(final String s, final Object o) {
        if (this.values == null) {
            int n;
            if (this.desc != null) {
                n = 2;
            }
            else {
                n = 1;
            }
            this.values = new ArrayList<Object>(n);
        }
        if (this.desc != null) {
            this.values.add(s);
        }
        final boolean b = o instanceof byte[];
        final int n2 = 0;
        final int n3 = 0;
        final int n4 = 0;
        final int n5 = 0;
        final int n6 = 0;
        final int n7 = 0;
        final int n8 = 0;
        int i = 0;
        if (b) {
            final byte[] array = (byte[])o;
            final ArrayList list = new ArrayList<Byte>(array.length);
            while (i < array.length) {
                list.add(array[i]);
                ++i;
            }
            this.values.add(list);
            return;
        }
        if (o instanceof boolean[]) {
            final boolean[] array2 = (boolean[])o;
            final ArrayList list2 = new ArrayList<Boolean>(array2.length);
            for (int length = array2.length, j = n2; j < length; ++j) {
                list2.add(array2[j]);
            }
            this.values.add(list2);
            return;
        }
        if (o instanceof short[]) {
            final short[] array3 = (short[])o;
            final ArrayList list3 = new ArrayList<Short>(array3.length);
            for (int length2 = array3.length, k = n3; k < length2; ++k) {
                list3.add(array3[k]);
            }
            this.values.add(list3);
            return;
        }
        if (o instanceof char[]) {
            final char[] array4 = (char[])o;
            final ArrayList list4 = new ArrayList<Character>(array4.length);
            for (int length3 = array4.length, l = n4; l < length3; ++l) {
                list4.add(array4[l]);
            }
            this.values.add(list4);
            return;
        }
        if (o instanceof int[]) {
            final int[] array5 = (int[])o;
            final ArrayList list5 = new ArrayList<Integer>(array5.length);
            for (int length4 = array5.length, n9 = n5; n9 < length4; ++n9) {
                list5.add(array5[n9]);
            }
            this.values.add(list5);
            return;
        }
        if (o instanceof long[]) {
            final long[] array6 = (long[])o;
            final ArrayList list6 = new ArrayList<Long>(array6.length);
            for (int length5 = array6.length, n10 = n6; n10 < length5; ++n10) {
                list6.add(array6[n10]);
            }
            this.values.add(list6);
            return;
        }
        if (o instanceof float[]) {
            final float[] array7 = (float[])o;
            final ArrayList list7 = new ArrayList<Float>(array7.length);
            for (int length6 = array7.length, n11 = n7; n11 < length6; ++n11) {
                list7.add(array7[n11]);
            }
            this.values.add(list7);
            return;
        }
        if (o instanceof double[]) {
            final double[] array8 = (double[])o;
            final ArrayList list8 = new ArrayList<Double>(array8.length);
            for (int length7 = array8.length, n12 = n8; n12 < length7; ++n12) {
                list8.add(array8[n12]);
            }
            this.values.add(list8);
            return;
        }
        this.values.add(o);
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String s, final String s2) {
        if (this.values == null) {
            int n;
            if (this.desc != null) {
                n = 2;
            }
            else {
                n = 1;
            }
            this.values = new ArrayList<Object>(n);
        }
        if (this.desc != null) {
            this.values.add(s);
        }
        final AnnotationNode annotationNode = new AnnotationNode(s2);
        this.values.add(annotationNode);
        return annotationNode;
    }
    
    @Override
    public AnnotationVisitor visitArray(final String s) {
        if (this.values == null) {
            int n;
            if (this.desc != null) {
                n = 2;
            }
            else {
                n = 1;
            }
            this.values = new ArrayList<Object>(n);
        }
        if (this.desc != null) {
            this.values.add(s);
        }
        final ArrayList<Object> list = new ArrayList<Object>();
        this.values.add(list);
        return new AnnotationNode(list);
    }
    
    @Override
    public void visitEnd() {
    }
    
    @Override
    public void visitEnum(final String s, final String s2, final String s3) {
        if (this.values == null) {
            int n;
            if (this.desc != null) {
                n = 2;
            }
            else {
                n = 1;
            }
            this.values = new ArrayList<Object>(n);
        }
        if (this.desc != null) {
            this.values.add(s);
        }
        this.values.add(new String[] { s2, s3 });
    }
}
