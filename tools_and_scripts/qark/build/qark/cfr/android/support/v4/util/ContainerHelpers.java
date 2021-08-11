/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.util;

class ContainerHelpers {
    static final int[] EMPTY_INTS = new int[0];
    static final long[] EMPTY_LONGS = new long[0];
    static final Object[] EMPTY_OBJECTS = new Object[0];

    ContainerHelpers() {
    }

    static int binarySearch(int[] arrn, int n, int n2) {
        int n3 = 0;
        --n;
        while (n3 <= n) {
            int n4 = n3 + n >>> 1;
            int n5 = arrn[n4];
            if (n5 < n2) {
                n3 = n4 + 1;
                continue;
            }
            if (n5 > n2) {
                n = n4 - 1;
                continue;
            }
            return n4;
        }
        return ~ n3;
    }

    static int binarySearch(long[] arrl, int n, long l) {
        int n2 = 0;
        --n;
        while (n2 <= n) {
            int n3 = n2 + n >>> 1;
            long l2 = arrl[n3];
            if (l2 < l) {
                n2 = n3 + 1;
                continue;
            }
            if (l2 > l) {
                n = n3 - 1;
                continue;
            }
            return n3;
        }
        return ~ n2;
    }

    public static boolean equal(Object object, Object object2) {
        if (!(object == object2 || object != null && object.equals(object2))) {
            return false;
        }
        return true;
    }

    public static int idealByteArraySize(int n) {
        for (int i = 4; i < 32; ++i) {
            if (n > (1 << i) - 12) continue;
            return (1 << i) - 12;
        }
        return n;
    }

    public static int idealIntArraySize(int n) {
        return ContainerHelpers.idealByteArraySize(n * 4) / 4;
    }

    public static int idealLongArraySize(int n) {
        return ContainerHelpers.idealByteArraySize(n * 8) / 8;
    }
}

