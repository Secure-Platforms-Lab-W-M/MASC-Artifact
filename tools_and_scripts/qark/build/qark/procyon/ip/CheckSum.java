// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package ip;

public class CheckSum
{
    public static int chkSum(final byte[] array, final int n, final int n2) {
        int n3 = 0;
        int n4 = 0;
        int n5;
        while (true) {
            n5 = n3;
            if (n4 >= n2) {
                break;
            }
            int n6 = (array[n + n4] & 0xFF) << 8;
            if (n4 + 1 < n2) {
                n6 += (array[n + n4 + 1] & 0xFF);
            }
            n3 += n6;
            n4 += 2;
        }
        while (n5 >> 16 != 0) {
            n5 = (n5 & 0xFFFF) + (n5 >> 16);
        }
        return ~n5 & 0xFFFF;
    }
}
