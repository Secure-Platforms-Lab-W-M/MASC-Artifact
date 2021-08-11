/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

final class Utf8 {
    public static final int COMPLETE = 0;
    public static final int MALFORMED = -1;

    private Utf8() {
    }

    private static int incompleteStateFor(int n) {
        if (n > -12) {
            return -1;
        }
        return n;
    }

    private static int incompleteStateFor(int n, int n2) {
        if (n <= -12 && n2 <= -65) {
            return n2 << 8 ^ n;
        }
        return -1;
    }

    private static int incompleteStateFor(int n, int n2, int n3) {
        if (n <= -12 && n2 <= -65 && n3 <= -65) {
            return n2 << 8 ^ n ^ n3 << 16;
        }
        return -1;
    }

    private static int incompleteStateFor(byte[] arrby, int n, int n2) {
        byte by = arrby[n - 1];
        if ((n2 -= n) != 0) {
            if (n2 != 1) {
                if (n2 == 2) {
                    return Utf8.incompleteStateFor(by, (int)arrby[n], (int)arrby[n + 1]);
                }
                throw new AssertionError();
            }
            return Utf8.incompleteStateFor(by, arrby[n]);
        }
        return Utf8.incompleteStateFor(by);
    }

    public static boolean isValidUtf8(byte[] arrby) {
        return Utf8.isValidUtf8(arrby, 0, arrby.length);
    }

    public static boolean isValidUtf8(byte[] arrby, int n, int n2) {
        if (Utf8.partialIsValidUtf8(arrby, n, n2) == 0) {
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int partialIsValidUtf8(int n, byte[] arrby, int n2, int n3) {
        int n4 = n2;
        if (n == 0) return Utf8.partialIsValidUtf8(arrby, n4, n3);
        if (n2 >= n3) {
            return n;
        }
        byte by = (byte)n;
        if (by < -32) {
            if (by < -62) return -1;
            if (arrby[n2] > -65) {
                return -1;
            }
            n4 = n2 + 1;
            return Utf8.partialIsValidUtf8(arrby, n4, n3);
        } else if (by < -16) {
            byte by2 = (byte)(n >> 8);
            n4 = by2;
            n = n2;
            if (by2 == 0) {
                n = n2 + 1;
                n4 = arrby[n2];
                if (n >= n3) {
                    return Utf8.incompleteStateFor(by, n4);
                }
            }
            if (n4 > -65 || by == -32 && n4 < -96 || by == -19 && n4 >= -96) return -1;
            if (arrby[n] > -65) {
                return -1;
            }
            n4 = n + 1;
            return Utf8.partialIsValidUtf8(arrby, n4, n3);
        } else {
            n4 = (byte)(n >> 8);
            int n5 = 0;
            if (n4 == 0) {
                n = n2 + 1;
                n4 = arrby[n2];
                if (n >= n3) {
                    return Utf8.incompleteStateFor(by, n4);
                }
                n2 = n;
                n = n5;
            } else {
                n = (byte)(n >> 16);
            }
            int n6 = n;
            n5 = n2;
            if (n == 0) {
                n5 = n2 + 1;
                n6 = arrby[n2];
                if (n5 >= n3) {
                    return Utf8.incompleteStateFor(by, n4, n6);
                }
            }
            if (n4 > -65 || (by << 28) + (n4 + 112) >> 30 != 0 || n6 > -65) return -1;
            if (arrby[n5] > -65) {
                return -1;
            }
            n4 = n5 + 1;
        }
        return Utf8.partialIsValidUtf8(arrby, n4, n3);
    }

    public static int partialIsValidUtf8(byte[] arrby, int n, int n2) {
        while (n < n2 && arrby[n] >= 0) {
            ++n;
        }
        if (n >= n2) {
            return 0;
        }
        return Utf8.partialIsValidUtf8NonAscii(arrby, n, n2);
    }

    private static int partialIsValidUtf8NonAscii(byte[] arrby, int n, int n2) {
        while (n < n2) {
            int n3 = n + 1;
            if ((n = arrby[n]) < 0) {
                int n4;
                if (n < -32) {
                    if (n3 >= n2) {
                        return n;
                    }
                    if (n >= -62) {
                        n = n3 + 1;
                        if (arrby[n3] <= -65) continue;
                    }
                    return -1;
                }
                if (n < -16) {
                    if (n3 >= n2 - 1) {
                        return Utf8.incompleteStateFor(arrby, n3, n2);
                    }
                    n4 = n3 + 1;
                    if (!((n3 = arrby[n3]) > -65 || n == -32 && n3 < -96 || n == -19 && n3 >= -96)) {
                        n = n4 + 1;
                        if (arrby[n4] <= -65) continue;
                    }
                    return -1;
                }
                if (n3 >= n2 - 2) {
                    return Utf8.incompleteStateFor(arrby, n3, n2);
                }
                n4 = n3 + 1;
                if ((n3 = arrby[n3]) <= -65 && (n << 28) + (n3 + 112) >> 30 == 0) {
                    n3 = n4 + 1;
                    if (arrby[n4] <= -65) {
                        n = n3 + 1;
                        if (arrby[n3] <= -65) continue;
                    }
                } else {
                    return -1;
                }
                return -1;
            }
            n = n3;
        }
        return 0;
    }
}

