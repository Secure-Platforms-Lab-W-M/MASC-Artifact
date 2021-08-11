/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.content.res;

import java.lang.reflect.Array;

final class GrowingArrayUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    private GrowingArrayUtils() {
    }

    public static int[] append(int[] arrn, int n, int n2) {
        if (n + 1 > arrn.length) {
            int[] arrn2 = new int[GrowingArrayUtils.growSize(n)];
            System.arraycopy(arrn, 0, arrn2, 0, n);
            arrn = arrn2;
        }
        arrn[n] = n2;
        return arrn;
    }

    public static long[] append(long[] arrl, int n, long l) {
        if (n + 1 > arrl.length) {
            long[] arrl2 = new long[GrowingArrayUtils.growSize(n)];
            System.arraycopy(arrl, 0, arrl2, 0, n);
            arrl = arrl2;
        }
        arrl[n] = l;
        return arrl;
    }

    public static <T> T[] append(T[] arrT, int n, T t) {
        if (n + 1 > arrT.length) {
            Object[] arrobject = (Object[])Array.newInstance(arrT.getClass().getComponentType(), GrowingArrayUtils.growSize(n));
            System.arraycopy(arrT, 0, arrobject, 0, n);
            arrT = arrobject;
        }
        arrT[n] = t;
        return arrT;
    }

    public static boolean[] append(boolean[] arrbl, int n, boolean bl) {
        if (n + 1 > arrbl.length) {
            boolean[] arrbl2 = new boolean[GrowingArrayUtils.growSize(n)];
            System.arraycopy(arrbl, 0, arrbl2, 0, n);
            arrbl = arrbl2;
        }
        arrbl[n] = bl;
        return arrbl;
    }

    public static int growSize(int n) {
        if (n <= 4) {
            return 8;
        }
        return n * 2;
    }

    public static int[] insert(int[] arrn, int n, int n2, int n3) {
        if (n + 1 <= arrn.length) {
            System.arraycopy(arrn, n2, arrn, n2 + 1, n - n2);
            arrn[n2] = n3;
            return arrn;
        }
        int[] arrn2 = new int[GrowingArrayUtils.growSize(n)];
        System.arraycopy(arrn, 0, arrn2, 0, n2);
        arrn2[n2] = n3;
        System.arraycopy(arrn, n2, arrn2, n2 + 1, arrn.length - n2);
        return arrn2;
    }

    public static long[] insert(long[] arrl, int n, int n2, long l) {
        if (n + 1 <= arrl.length) {
            System.arraycopy(arrl, n2, arrl, n2 + 1, n - n2);
            arrl[n2] = l;
            return arrl;
        }
        long[] arrl2 = new long[GrowingArrayUtils.growSize(n)];
        System.arraycopy(arrl, 0, arrl2, 0, n2);
        arrl2[n2] = l;
        System.arraycopy(arrl, n2, arrl2, n2 + 1, arrl.length - n2);
        return arrl2;
    }

    public static <T> T[] insert(T[] arrT, int n, int n2, T t) {
        if (n + 1 <= arrT.length) {
            System.arraycopy(arrT, n2, arrT, n2 + 1, n - n2);
            arrT[n2] = t;
            return arrT;
        }
        Object[] arrobject = (Object[])Array.newInstance(arrT.getClass().getComponentType(), GrowingArrayUtils.growSize(n));
        System.arraycopy(arrT, 0, arrobject, 0, n2);
        arrobject[n2] = t;
        System.arraycopy(arrT, n2, arrobject, n2 + 1, arrT.length - n2);
        return arrobject;
    }

    public static boolean[] insert(boolean[] arrbl, int n, int n2, boolean bl) {
        if (n + 1 <= arrbl.length) {
            System.arraycopy(arrbl, n2, arrbl, n2 + 1, n - n2);
            arrbl[n2] = bl;
            return arrbl;
        }
        boolean[] arrbl2 = new boolean[GrowingArrayUtils.growSize(n)];
        System.arraycopy(arrbl, 0, arrbl2, 0, n2);
        arrbl2[n2] = bl;
        System.arraycopy(arrbl, n2, arrbl2, n2 + 1, arrbl.length - n2);
        return arrbl2;
    }
}

