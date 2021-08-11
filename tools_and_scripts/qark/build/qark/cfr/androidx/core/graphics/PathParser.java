/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Path
 *  android.util.Log
 */
package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

public class PathParser {
    private static final String LOGTAG = "PathParser";

    private PathParser() {
    }

    private static void addNode(ArrayList<PathDataNode> arrayList, char c, float[] arrf) {
        arrayList.add(new PathDataNode(c, arrf));
    }

    public static boolean canMorph(PathDataNode[] arrpathDataNode, PathDataNode[] arrpathDataNode2) {
        if (arrpathDataNode != null) {
            if (arrpathDataNode2 == null) {
                return false;
            }
            if (arrpathDataNode.length != arrpathDataNode2.length) {
                return false;
            }
            for (int i = 0; i < arrpathDataNode.length; ++i) {
                if (arrpathDataNode[i].mType == arrpathDataNode2[i].mType) {
                    if (arrpathDataNode[i].mParams.length == arrpathDataNode2[i].mParams.length) continue;
                    return false;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    static float[] copyOfRange(float[] arrf, int n, int n2) {
        if (n <= n2) {
            int n3 = arrf.length;
            if (n >= 0 && n <= n3) {
                n3 = Math.min(n2 -= n, n3 - n);
                float[] arrf2 = new float[n2];
                System.arraycopy(arrf, n, arrf2, 0, n3);
                return arrf2;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
        throw new IllegalArgumentException();
    }

    public static PathDataNode[] createNodesFromPathData(String string2) {
        if (string2 == null) {
            return null;
        }
        int n = 0;
        int n2 = 1;
        ArrayList<PathDataNode> arrayList = new ArrayList<PathDataNode>();
        while (n2 < string2.length()) {
            String string3 = string2.substring(n, n2 = PathParser.nextStart(string2, n2)).trim();
            if (string3.length() > 0) {
                float[] arrf = PathParser.getFloats(string3);
                PathParser.addNode(arrayList, string3.charAt(0), arrf);
            }
            n = n2++;
        }
        if (n2 - n == 1 && n < string2.length()) {
            PathParser.addNode(arrayList, string2.charAt(n), new float[0]);
        }
        return arrayList.toArray(new PathDataNode[arrayList.size()]);
    }

    public static Path createPathFromPathData(String string2) {
        Path path = new Path();
        PathDataNode[] arrpathDataNode = PathParser.createNodesFromPathData(string2);
        if (arrpathDataNode != null) {
            try {
                PathDataNode.nodesToPath(arrpathDataNode, path);
                return path;
            }
            catch (RuntimeException runtimeException) {
                arrpathDataNode = new StringBuilder();
                arrpathDataNode.append("Error in parsing ");
                arrpathDataNode.append(string2);
                throw new RuntimeException(arrpathDataNode.toString(), runtimeException);
            }
        }
        return null;
    }

    public static PathDataNode[] deepCopyNodes(PathDataNode[] arrpathDataNode) {
        if (arrpathDataNode == null) {
            return null;
        }
        PathDataNode[] arrpathDataNode2 = new PathDataNode[arrpathDataNode.length];
        for (int i = 0; i < arrpathDataNode.length; ++i) {
            arrpathDataNode2[i] = new PathDataNode(arrpathDataNode[i]);
        }
        return arrpathDataNode2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static void extract(String var0, int var1_1, ExtractFloatResult var2_2) {
        var7_4 = '\u0000';
        var2_2.mEndWithNegOrDot = false;
        var5_5 = false;
        var8_6 = false;
        for (var6_3 = var1_1; var6_3 < var0.length(); ++var6_3) {
            var10_10 = false;
            var3_7 = var0.charAt(var6_3);
            if (var3_7 == ' ') ** GOTO lbl-1000
            if (var3_7 == 'E' || var3_7 == 'e') ** GOTO lbl41
            switch (var3_7) {
                default: {
                    var3_7 = var7_4;
                    var9_9 = var5_5;
                    var4_8 = var10_10;
                    break;
                }
                case '.': {
                    if (!var5_5) {
                        var9_9 = true;
                        var3_7 = var7_4;
                        var4_8 = var10_10;
                        break;
                    }
                    var3_7 = '\u0001';
                    var2_2.mEndWithNegOrDot = true;
                    var9_9 = var5_5;
                    var4_8 = var10_10;
                    break;
                }
                case '-': {
                    var3_7 = var7_4;
                    var9_9 = var5_5;
                    var4_8 = var10_10;
                    if (var6_3 == var1_1) break;
                    var3_7 = var7_4;
                    var9_9 = var5_5;
                    var4_8 = var10_10;
                    if (var8_6) break;
                    var3_7 = '\u0001';
                    var2_2.mEndWithNegOrDot = true;
                    var9_9 = var5_5;
                    var4_8 = var10_10;
                    break;
                }
lbl41: // 1 sources:
                var4_8 = true;
                var3_7 = var7_4;
                var9_9 = var5_5;
                break;
                case ',': lbl-1000: // 2 sources:
                {
                    var3_7 = '\u0001';
                    var4_8 = var10_10;
                    var9_9 = var5_5;
                }
            }
            if (var3_7 != '\u0000') break;
            var7_4 = var3_7;
            var5_5 = var9_9;
            var8_6 = var4_8;
        }
        var2_2.mEndPosition = var6_3;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static float[] getFloats(String var0) {
        block6 : {
            if (var0.charAt(0) == 'z') return new float[0];
            if (var0.charAt(0) == 'Z') {
                return new float[0];
            }
            try {
                var6_1 = new float[var0.length()];
                var2_3 = 0;
                var1_4 = 1;
                var7_5 = new ExtractFloatResult();
                var5_6 = var0.length();
lbl10: // 3 sources:
                if (var1_4 >= var5_6) return PathParser.copyOfRange(var6_1, 0, var2_3);
                PathParser.extract(var0, var1_4, (ExtractFloatResult)var7_5);
                var4_8 = var7_5.mEndPosition;
                var3_7 = var2_3;
                if (var1_4 < var4_8) {
                    var6_1[var2_3] = Float.parseFloat(var0.substring(var1_4, var4_8));
                    var3_7 = var2_3 + 1;
                }
                if (!var7_5.mEndWithNegOrDot) break block6;
                var1_4 = var4_8;
                var2_3 = var3_7;
                ** GOTO lbl10
            }
            catch (NumberFormatException var6_2) {
                var7_5 = new StringBuilder();
                var7_5.append("error in parsing \"");
                var7_5.append(var0);
                var7_5.append("\"");
                throw new RuntimeException(var7_5.toString(), var6_2);
            }
        }
        var1_4 = var4_8 + 1;
        var2_3 = var3_7;
        ** GOTO lbl10
    }

    public static boolean interpolatePathDataNodes(PathDataNode[] arrpathDataNode, PathDataNode[] arrpathDataNode2, PathDataNode[] arrpathDataNode3, float f) {
        if (arrpathDataNode != null && arrpathDataNode2 != null && arrpathDataNode3 != null) {
            if (arrpathDataNode.length == arrpathDataNode2.length && arrpathDataNode2.length == arrpathDataNode3.length) {
                if (!PathParser.canMorph(arrpathDataNode2, arrpathDataNode3)) {
                    return false;
                }
                for (int i = 0; i < arrpathDataNode.length; ++i) {
                    arrpathDataNode[i].interpolatePathDataNode(arrpathDataNode2[i], arrpathDataNode3[i], f);
                }
                return true;
            }
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
        }
        throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
    }

    private static int nextStart(String string2, int n) {
        while (n < string2.length()) {
            char c = string2.charAt(n);
            if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E') {
                return n;
            }
            ++n;
        }
        return n;
    }

    public static void updateNodes(PathDataNode[] arrpathDataNode, PathDataNode[] arrpathDataNode2) {
        for (int i = 0; i < arrpathDataNode2.length; ++i) {
            arrpathDataNode[i].mType = arrpathDataNode2[i].mType;
            for (int j = 0; j < arrpathDataNode2[i].mParams.length; ++j) {
                arrpathDataNode[i].mParams[j] = arrpathDataNode2[i].mParams[j];
            }
        }
    }

    private static class ExtractFloatResult {
        int mEndPosition;
        boolean mEndWithNegOrDot;

        ExtractFloatResult() {
        }
    }

    public static class PathDataNode {
        public float[] mParams;
        public char mType;

        PathDataNode(char c, float[] arrf) {
            this.mType = c;
            this.mParams = arrf;
        }

        PathDataNode(PathDataNode arrf) {
            this.mType = arrf.mType;
            arrf = arrf.mParams;
            this.mParams = PathParser.copyOfRange(arrf, 0, arrf.length);
        }

        private static void addCommand(Path path, float[] arrf, char c, char c2, float[] arrf2) {
            int n;
            float f = arrf[0];
            float f2 = arrf[1];
            float f3 = arrf[2];
            float f4 = arrf[3];
            float f5 = arrf[4];
            float f6 = arrf[5];
            switch (c2) {
                default: {
                    n = 2;
                    break;
                }
                case 'Z': 
                case 'z': {
                    path.close();
                    f = f5;
                    f2 = f6;
                    f3 = f5;
                    f4 = f6;
                    path.moveTo(f, f2);
                    n = 2;
                    break;
                }
                case 'Q': 
                case 'S': 
                case 'q': 
                case 's': {
                    n = 4;
                    break;
                }
                case 'L': 
                case 'M': 
                case 'T': 
                case 'l': 
                case 'm': 
                case 't': {
                    n = 2;
                    break;
                }
                case 'H': 
                case 'V': 
                case 'h': 
                case 'v': {
                    n = 1;
                    break;
                }
                case 'C': 
                case 'c': {
                    n = 6;
                    break;
                }
                case 'A': 
                case 'a': {
                    n = 7;
                }
            }
            char c3 = '\u0000';
            float f7 = f3;
            float f8 = f4;
            f4 = f6;
            f6 = f2;
            f3 = f5;
            char c4 = c;
            c = c3;
            f5 = f;
            do {
                boolean bl;
                boolean bl2;
                float f9;
                c3 = c2;
                if (c >= arrf2.length) break;
                if (c3 != 'A') {
                    if (c3 != 'C') {
                        if (c3 != 'H') {
                            if (c3 != 'Q') {
                                if (c3 != 'V') {
                                    if (c3 != 'a') {
                                        if (c3 != 'c') {
                                            if (c3 != 'h') {
                                                if (c3 != 'q') {
                                                    if (c3 != 'v') {
                                                        if (c3 != 'L') {
                                                            if (c3 != 'M') {
                                                                if (c3 != 'S') {
                                                                    if (c3 != 'T') {
                                                                        if (c3 != 'l') {
                                                                            if (c3 != 'm') {
                                                                                if (c3 != 's') {
                                                                                    if (c3 != 't') {
                                                                                        f2 = f7;
                                                                                        f = f8;
                                                                                    } else {
                                                                                        f = 0.0f;
                                                                                        f2 = 0.0f;
                                                                                        if (c4 == 'q' || c4 == 't' || c4 == 'Q' || c4 == 'T') {
                                                                                            f = f5 - f7;
                                                                                            f2 = f6 - f8;
                                                                                        }
                                                                                        path.rQuadTo(f, f2, arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                                                                                        f8 = f5 + arrf2[c + '\u0000'];
                                                                                        f7 = f6 + arrf2[c + '\u0001'];
                                                                                        f = f5 + f;
                                                                                        f9 = f6 + f2;
                                                                                        f6 = f7;
                                                                                        f5 = f8;
                                                                                        f2 = f;
                                                                                        f = f9;
                                                                                    }
                                                                                } else {
                                                                                    if (c4 != 'c' && c4 != 's' && c4 != 'C' && c4 != 'S') {
                                                                                        f2 = 0.0f;
                                                                                        f = 0.0f;
                                                                                    } else {
                                                                                        f2 = f5 - f7;
                                                                                        f = f6 - f8;
                                                                                    }
                                                                                    path.rCubicTo(f2, f, arrf2[c + '\u0000'], arrf2[c + '\u0001'], arrf2[c + 2], arrf2[c + 3]);
                                                                                    f = arrf2[c + '\u0000'];
                                                                                    f8 = arrf2[c + '\u0001'];
                                                                                    f2 = f5 + arrf2[c + 2];
                                                                                    f7 = arrf2[c + 3];
                                                                                    f8 = f6 + f8;
                                                                                    f6 = f7 + f6;
                                                                                    f5 = f2;
                                                                                    f2 = f += f5;
                                                                                    f = f8;
                                                                                }
                                                                            } else {
                                                                                f5 += arrf2[c + '\u0000'];
                                                                                f6 += arrf2[c + '\u0001'];
                                                                                if (c > '\u0000') {
                                                                                    path.rLineTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                                                                                    f2 = f7;
                                                                                    f = f8;
                                                                                } else {
                                                                                    path.rMoveTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                                                                                    f3 = f5;
                                                                                    f4 = f6;
                                                                                    f2 = f7;
                                                                                    f = f8;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            path.rLineTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                                                                            f5 += arrf2[c + '\u0000'];
                                                                            f6 += arrf2[c + '\u0001'];
                                                                            f2 = f7;
                                                                            f = f8;
                                                                        }
                                                                    } else {
                                                                        f = f5;
                                                                        f2 = f6;
                                                                        if (c4 == 'q' || c4 == 't' || c4 == 'Q' || c4 == 'T') {
                                                                            f = f5 * 2.0f - f7;
                                                                            f2 = f6 * 2.0f - f8;
                                                                        }
                                                                        path.quadTo(f, f2, arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                                                                        f5 = arrf2[c + '\u0000'];
                                                                        f6 = arrf2[c + '\u0001'];
                                                                        f8 = f2;
                                                                        f2 = f;
                                                                        f = f8;
                                                                    }
                                                                } else {
                                                                    if (c4 == 'c' || c4 == 's' || c4 == 'C' || c4 == 'S') {
                                                                        f5 = f5 * 2.0f - f7;
                                                                        f6 = f6 * 2.0f - f8;
                                                                    }
                                                                    path.cubicTo(f5, f6, arrf2[c + '\u0000'], arrf2[c + '\u0001'], arrf2[c + 2], arrf2[c + 3]);
                                                                    f2 = arrf2[c + '\u0000'];
                                                                    f = arrf2[c + '\u0001'];
                                                                    f5 = arrf2[c + 2];
                                                                    f6 = arrf2[c + 3];
                                                                }
                                                            } else {
                                                                f5 = arrf2[c + '\u0000'];
                                                                f6 = arrf2[c + '\u0001'];
                                                                if (c > '\u0000') {
                                                                    path.lineTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                                                                    f2 = f7;
                                                                    f = f8;
                                                                } else {
                                                                    path.moveTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                                                                    f2 = f5;
                                                                    f = f6;
                                                                    f3 = f5;
                                                                    f4 = f6;
                                                                    f6 = f;
                                                                    f5 = f2;
                                                                    f2 = f7;
                                                                    f = f8;
                                                                }
                                                            }
                                                        } else {
                                                            path.lineTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                                                            f5 = arrf2[c + '\u0000'];
                                                            f6 = arrf2[c + '\u0001'];
                                                            f2 = f7;
                                                            f = f8;
                                                        }
                                                    } else {
                                                        path.rLineTo(0.0f, arrf2[c + '\u0000']);
                                                        f6 += arrf2[c + '\u0000'];
                                                        f2 = f7;
                                                        f = f8;
                                                    }
                                                } else {
                                                    path.rQuadTo(arrf2[c + '\u0000'], arrf2[c + '\u0001'], arrf2[c + 2], arrf2[c + 3]);
                                                    f = arrf2[c + '\u0000'];
                                                    f8 = arrf2[c + '\u0001'];
                                                    f2 = f5 + arrf2[c + 2];
                                                    f7 = arrf2[c + 3];
                                                    f8 = f6 + f8;
                                                    f6 = f7 + f6;
                                                    f5 = f2;
                                                    f2 = f += f5;
                                                    f = f8;
                                                }
                                            } else {
                                                path.rLineTo(arrf2[c + '\u0000'], 0.0f);
                                                f5 += arrf2[c + '\u0000'];
                                                f2 = f7;
                                                f = f8;
                                            }
                                        } else {
                                            path.rCubicTo(arrf2[c + '\u0000'], arrf2[c + '\u0001'], arrf2[c + 2], arrf2[c + 3], arrf2[c + 4], arrf2[c + 5]);
                                            f = arrf2[c + 2];
                                            f8 = arrf2[c + 3];
                                            f2 = f5 + arrf2[c + 4];
                                            f7 = arrf2[c + 5];
                                            f8 = f6 + f8;
                                            f6 = f7 + f6;
                                            f5 = f2;
                                            f2 = f += f5;
                                            f = f8;
                                        }
                                    } else {
                                        f2 = arrf2[c + 5];
                                        f = arrf2[c + 6];
                                        f8 = arrf2[c + '\u0000'];
                                        f7 = arrf2[c + '\u0001'];
                                        f9 = arrf2[c + 2];
                                        bl = arrf2[c + 3] != 0.0f;
                                        bl2 = arrf2[c + 4] != 0.0f;
                                        c4 = c;
                                        PathDataNode.drawArc(path, f5, f6, f2 + f5, f + f6, f8, f7, f9, bl, bl2);
                                        f2 = f5 += arrf2[c4 + 5];
                                        f = f6 += arrf2[c4 + 6];
                                    }
                                } else {
                                    c4 = c;
                                    path.lineTo(f5, arrf2[c4 + '\u0000']);
                                    f6 = arrf2[c4 + '\u0000'];
                                    f2 = f7;
                                    f = f8;
                                }
                            } else {
                                c4 = c;
                                path.quadTo(arrf2[c4 + '\u0000'], arrf2[c4 + '\u0001'], arrf2[c4 + 2], arrf2[c4 + 3]);
                                f2 = arrf2[c4 + '\u0000'];
                                f = arrf2[c4 + '\u0001'];
                                f5 = arrf2[c4 + 2];
                                f6 = arrf2[c4 + 3];
                            }
                        } else {
                            c4 = c;
                            path.lineTo(arrf2[c4 + '\u0000'], f6);
                            f5 = arrf2[c4 + '\u0000'];
                            f2 = f7;
                            f = f8;
                        }
                    } else {
                        c4 = c;
                        path.cubicTo(arrf2[c4 + '\u0000'], arrf2[c4 + '\u0001'], arrf2[c4 + 2], arrf2[c4 + 3], arrf2[c4 + 4], arrf2[c4 + 5]);
                        f5 = arrf2[c4 + 4];
                        f6 = arrf2[c4 + 5];
                        f2 = arrf2[c4 + 2];
                        f = arrf2[c4 + 3];
                    }
                } else {
                    c4 = c;
                    f2 = arrf2[c4 + 5];
                    f = arrf2[c4 + 6];
                    f8 = arrf2[c4 + '\u0000'];
                    f7 = arrf2[c4 + '\u0001'];
                    f9 = arrf2[c4 + 2];
                    bl = arrf2[c4 + 3] != 0.0f;
                    bl2 = arrf2[c4 + 4] != 0.0f;
                    PathDataNode.drawArc(path, f5, f6, f2, f, f8, f7, f9, bl, bl2);
                    f2 = arrf2[c4 + 5];
                    f = arrf2[c4 + 6];
                    f5 = f2;
                    f6 = f;
                }
                c4 = c2;
                c = (char)(c + n);
                f7 = f2;
                f8 = f;
            } while (true);
            arrf[0] = f5;
            arrf[1] = f6;
            arrf[2] = f7;
            arrf[3] = f8;
            arrf[4] = f3;
            arrf[5] = f4;
        }

        private static void arcToBezier(Path path, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
            double d10 = d3;
            int n = (int)Math.ceil(Math.abs(d9 * 4.0 / 3.141592653589793));
            double d11 = Math.cos(d7);
            double d12 = Math.sin(d7);
            d7 = Math.cos(d8);
            double d13 = Math.sin(d8);
            double d14 = - d10;
            double d15 = (- d10) * d12 * d13 + d4 * d11 * d7;
            d9 /= (double)n;
            int n2 = 0;
            d10 = d5;
            double d16 = d14 * d11 * d13 - d4 * d12 * d7;
            double d17 = d8;
            d14 = d6;
            d8 = d13;
            d5 = d12;
            d6 = d11;
            do {
                double d18 = d3;
                if (n2 >= n) break;
                double d19 = d17 + d9;
                double d20 = Math.sin(d19);
                double d21 = Math.cos(d19);
                d11 = d + d18 * d6 * d21 - d4 * d5 * d20;
                d13 = d2 + d18 * d5 * d21 + d4 * d6 * d20;
                d12 = (- d18) * d6 * d20 - d4 * d5 * d21;
                d18 = (- d18) * d5 * d20 + d4 * d6 * d21;
                d20 = Math.tan((d19 - d17) / 2.0);
                d17 = Math.sin(d19 - d17) * (Math.sqrt(d20 * 3.0 * d20 + 4.0) - 1.0) / 3.0;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float)(d10 + d17 * d16), (float)(d14 + d17 * d15), (float)(d11 - d17 * d12), (float)(d13 - d17 * d18), (float)d11, (float)d13);
                d17 = d19;
                d14 = d13;
                d16 = d12;
                d15 = d18;
                d10 = d11;
                ++n2;
            } while (true);
        }

        private static void drawArc(Path path, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, boolean bl2) {
            double d = Math.toRadians(f7);
            double d2 = Math.cos(d);
            double d3 = Math.sin(d);
            double d4 = ((double)f * d2 + (double)f2 * d3) / (double)f5;
            double d5 = ((double)(- f) * d3 + (double)f2 * d2) / (double)f6;
            double d6 = ((double)f3 * d2 + (double)f4 * d3) / (double)f5;
            double d7 = ((double)(- f3) * d3 + (double)f4 * d2) / (double)f6;
            double d8 = d4 - d6;
            double d9 = d5 - d7;
            double d10 = (d4 + d6) / 2.0;
            double d11 = (d5 + d7) / 2.0;
            double d12 = d8 * d8 + d9 * d9;
            if (d12 == 0.0) {
                Log.w((String)"PathParser", (String)" Points are coincident");
                return;
            }
            double d13 = 1.0 / d12 - 0.25;
            if (d13 < 0.0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Points are too far apart ");
                stringBuilder.append(d12);
                Log.w((String)"PathParser", (String)stringBuilder.toString());
                float f8 = (float)(Math.sqrt(d12) / 1.99999);
                PathDataNode.drawArc(path, f, f2, f3, f4, f5 * f8, f6 * f8, f7, bl, bl2);
                return;
            }
            d12 = Math.sqrt(d13);
            d8 = d12 * d8;
            d9 = d12 * d9;
            if (bl == bl2) {
                d10 -= d9;
                d11 += d8;
            } else {
                d10 += d9;
                d11 -= d8;
            }
            d4 = Math.atan2(d5 - d11, d4 - d10);
            d7 = Math.atan2(d7 - d11, d6 - d10) - d4;
            bl = d7 >= 0.0;
            d6 = d7;
            if (bl2 != bl) {
                d6 = d7 > 0.0 ? d7 - 6.283185307179586 : d7 + 6.283185307179586;
            }
            d11 = (double)f6 * d11;
            PathDataNode.arcToBezier(path, d10 * d2 - d11 * d3, (d10 *= (double)f5) * d3 + d11 * d2, f5, f6, f, f2, d, d4, d6);
        }

        public static void nodesToPath(PathDataNode[] arrpathDataNode, Path path) {
            float[] arrf = new float[6];
            char c = 'm';
            for (int i = 0; i < arrpathDataNode.length; ++i) {
                PathDataNode.addCommand(path, arrf, c, arrpathDataNode[i].mType, arrpathDataNode[i].mParams);
                c = arrpathDataNode[i].mType;
            }
        }

        public void interpolatePathDataNode(PathDataNode pathDataNode, PathDataNode pathDataNode2, float f) {
            float[] arrf;
            this.mType = pathDataNode.mType;
            for (int i = 0; i < (arrf = pathDataNode.mParams).length; ++i) {
                this.mParams[i] = arrf[i] * (1.0f - f) + pathDataNode2.mParams[i] * f;
            }
        }
    }

}

