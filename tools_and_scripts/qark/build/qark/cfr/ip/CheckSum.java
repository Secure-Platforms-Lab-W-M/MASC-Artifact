/*
 * Decompiled with CFR 0_124.
 */
package ip;

public class CheckSum {
    public static int chkSum(byte[] arrby, int n, int n2) {
        int n3;
        int n4 = 0;
        int n5 = 0;
        do {
            int n6;
            n3 = n4;
            if (n5 >= n2) break;
            n3 = n6 = (arrby[n + n5] & 255) << 8;
            if (n5 + 1 < n2) {
                n3 = n6 + (arrby[n + n5 + 1] & 255);
            }
            n4 += n3;
            n5 += 2;
        } while (true);
        while (n3 >> 16 != 0) {
            n3 = (n3 & 65535) + (n3 >> 16);
        }
        return ~ n3 & 65535;
    }
}

