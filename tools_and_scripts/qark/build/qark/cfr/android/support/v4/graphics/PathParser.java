/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Path
 *  android.util.Log
 */
package android.support.v4.graphics;

import android.graphics.Path;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.util.ArrayList;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class PathParser {
    private static final String LOGTAG = "PathParser";

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
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void extract(String var0, int var1_1, ExtractFloatResult var2_2) {
        var3_4 = false;
        var2_2.mEndWithNegOrDot = false;
        var6_5 = false;
        var4_6 = false;
        for (var5_3 = var1_1; var5_3 < var0.length(); ++var5_3) {
            var7_7 = false;
            var8_8 = var0.charAt(var5_3);
            if (var8_8 == ' ') ** GOTO lbl-1000
            if (var8_8 == 'E' || var8_8 == 'e') ** GOTO lbl31
            switch (var8_8) {
                default: {
                    var4_6 = var7_7;
                    break;
                }
                case '.': {
                    if (!var6_5) {
                        var6_5 = true;
                        var4_6 = var7_7;
                        break;
                    }
                    var3_4 = true;
                    var2_2.mEndWithNegOrDot = true;
                    var4_6 = var7_7;
                    break;
                }
                case '-': {
                    if (var5_3 != var1_1 && !var4_6) {
                        var3_4 = true;
                        var2_2.mEndWithNegOrDot = true;
                        var4_6 = var7_7;
                        break;
                    }
                    var4_6 = var7_7;
                    break;
                }
lbl31: // 1 sources:
                var4_6 = true;
                break;
                case ',': lbl-1000: // 2 sources:
                {
                    var3_4 = true;
                    var4_6 = var7_7;
                }
            }
            if (var3_4) break;
        }
        var2_2.mEndPosition = var5_3;
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
                var5_1 = new float[var0.length()];
                var2_3 = 0;
                var1_4 = 1;
                var6_5 = new ExtractFloatResult();
                var4_6 = var0.length();
lbl10: // 3 sources:
                if (var1_4 >= var4_6) return PathParser.copyOfRange(var5_1, 0, var2_3);
                PathParser.extract(var0, var1_4, (ExtractFloatResult)var6_5);
                var3_7 = var6_5.mEndPosition;
                if (var1_4 < var3_7) {
                    var5_1[var2_3] = Float.parseFloat(var0.substring(var1_4, var3_7));
                    ++var2_3;
                }
                if (!var6_5.mEndWithNegOrDot) break block6;
                var1_4 = var3_7;
                ** GOTO lbl10
            }
            catch (NumberFormatException var5_2) {
                var6_5 = new StringBuilder();
                var6_5.append("error in parsing \"");
                var6_5.append(var0);
                var6_5.append("\"");
                throw new RuntimeException(var6_5.toString(), var5_2);
            }
        }
        var1_4 = var3_7 + 1;
        ** GOTO lbl10
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
        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public float[] mParams;
        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
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
            float f7 = f;
            float f8 = f2;
            f = f3;
            f2 = f4;
            char c4 = c;
            f4 = f6;
            f3 = f5;
            c = c3;
            f5 = f7;
            f6 = f8;
            while (c < arrf2.length) {
                switch (c2) {
                    boolean bl;
                    float f9;
                    boolean bl2;
                    default: {
                        break;
                    }
                    case 'v': {
                        path.rLineTo(0.0f, arrf2[c + '\u0000']);
                        f6 += arrf2[c + '\u0000'];
                        break;
                    }
                    case 't': {
                        f8 = 0.0f;
                        f7 = 0.0f;
                        if (c4 != 'q' && c4 != 't' && c4 != 'Q' && c4 != 'T') {
                            f2 = f8;
                            f = f7;
                        } else {
                            f7 = f5 - f;
                            f = f6 - f2;
                            f2 = f7;
                        }
                        path.rQuadTo(f2, f, arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                        f7 = f5 + arrf2[c + '\u0000'];
                        f8 = f6 + arrf2[c + '\u0001'];
                        f2 = f5 + f2;
                        f9 = f6 + f;
                        f6 = f8;
                        f5 = f7;
                        f = f2;
                        f2 = f9;
                        break;
                    }
                    case 's': {
                        if (c4 != 'c' && c4 != 's' && c4 != 'C' && c4 != 'S') {
                            f = 0.0f;
                            f2 = 0.0f;
                        } else {
                            f = f5 - f;
                            f2 = f6 - f2;
                        }
                        path.rCubicTo(f, f2, arrf2[c + '\u0000'], arrf2[c + '\u0001'], arrf2[c + 2], arrf2[c + 3]);
                        f7 = arrf2[c + '\u0000'];
                        f8 = arrf2[c + '\u0001'];
                        f = f5 + arrf2[c + 2];
                        f2 = f6 + arrf2[c + 3];
                        f6 = f2;
                        f5 = f;
                        f = f7 += f5;
                        f2 = f8 += f6;
                        break;
                    }
                    case 'q': {
                        path.rQuadTo(arrf2[c + '\u0000'], arrf2[c + '\u0001'], arrf2[c + 2], arrf2[c + 3]);
                        f7 = arrf2[c + '\u0000'];
                        f8 = arrf2[c + '\u0001'];
                        f = f5 + arrf2[c + 2];
                        f2 = f6 + arrf2[c + 3];
                        f6 = f2;
                        f5 = f;
                        f = f7 += f5;
                        f2 = f8 += f6;
                        break;
                    }
                    case 'm': {
                        f5 += arrf2[c + '\u0000'];
                        f6 += arrf2[c + '\u0001'];
                        if (c > '\u0000') {
                            path.rLineTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                            break;
                        }
                        path.rMoveTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                        f3 = f5;
                        f4 = f6;
                        break;
                    }
                    case 'l': {
                        path.rLineTo(arrf2[c + '\u0000'], arrf2[c + '\u0001']);
                        f5 += arrf2[c + '\u0000'];
                        f6 += arrf2[c + '\u0001'];
                        break;
                    }
                    case 'h': {
                        path.rLineTo(arrf2[c + '\u0000'], 0.0f);
                        f5 += arrf2[c + '\u0000'];
                        break;
                    }
                    case 'c': {
                        path.rCubicTo(arrf2[c + '\u0000'], arrf2[c + '\u0001'], arrf2[c + 2], arrf2[c + 3], arrf2[c + 4], arrf2[c + 5]);
                        f7 = arrf2[c + 2];
                        f8 = arrf2[c + 3];
                        f = f5 + arrf2[c + 4];
                        f2 = f6 + arrf2[c + 5];
                        f6 = f2;
                        f5 = f;
                        f = f7 += f5;
                        f2 = f8 += f6;
                        break;
                    }
                    case 'a': {
                        f = arrf2[c + 5];
                        f2 = arrf2[c + 6];
                        f7 = arrf2[c + '\u0000'];
                        f8 = arrf2[c + '\u0001'];
                        f9 = arrf2[c + 2];
                        bl = arrf2[c + 3] != 0.0f;
                        bl2 = arrf2[c + 4] != 0.0f;
                        c4 = c;
                        PathDataNode.drawArc(path, f5, f6, f + f5, f2 + f6, f7, f8, f9, bl, bl2);
                        f = f5 += arrf2[c4 + 5];
                        f2 = f6 += arrf2[c4 + 6];
                        break;
                    }
                    case 'V': {
                        c4 = c;
                        path.lineTo(f5, arrf2[c4 + '\u0000']);
                        f6 = arrf2[c4 + '\u0000'];
                        break;
                    }
                    case 'T': {
                        c3 = c;
                        f8 = f5;
                        f7 = f6;
                        if (c4 != 'q' && c4 != 't' && c4 != 'Q' && c4 != 'T') {
                            f5 = f8;
                            f6 = f7;
                        } else {
                            f5 = f5 * 2.0f - f;
                            f6 = f6 * 2.0f - f2;
                        }
                        path.quadTo(f5, f6, arrf2[c3 + '\u0000'], arrf2[c3 + '\u0001']);
                        f7 = arrf2[c3 + '\u0000'];
                        f8 = arrf2[c3 + '\u0001'];
                        f = f5;
                        f2 = f6;
                        f6 = f8;
                        f5 = f7;
                        break;
                    }
                    case 'S': {
                        c3 = c;
                        if (c4 == 'c' || c4 == 's' || c4 == 'C' || c4 == 'S') {
                            f5 = f5 * 2.0f - f;
                            f6 = f6 * 2.0f - f2;
                        }
                        path.cubicTo(f5, f6, arrf2[c3 + '\u0000'], arrf2[c3 + '\u0001'], arrf2[c3 + 2], arrf2[c3 + 3]);
                        f = arrf2[c3 + '\u0000'];
                        f2 = arrf2[c3 + '\u0001'];
                        f5 = arrf2[c3 + 2];
                        f6 = arrf2[c3 + 3];
                        break;
                    }
                    case 'Q': {
                        c4 = c;
                        path.quadTo(arrf2[c4 + '\u0000'], arrf2[c4 + '\u0001'], arrf2[c4 + 2], arrf2[c4 + 3]);
                        f = arrf2[c4 + '\u0000'];
                        f2 = arrf2[c4 + '\u0001'];
                        f5 = arrf2[c4 + 2];
                        f6 = arrf2[c4 + 3];
                        break;
                    }
                    case 'M': {
                        c4 = c;
                        f5 = arrf2[c4 + '\u0000'];
                        f6 = arrf2[c4 + '\u0001'];
                        if (c4 > '\u0000') {
                            path.lineTo(arrf2[c4 + '\u0000'], arrf2[c4 + '\u0001']);
                            break;
                        }
                        path.moveTo(arrf2[c4 + '\u0000'], arrf2[c4 + '\u0001']);
                        f3 = f5;
                        f4 = f6;
                        break;
                    }
                    case 'L': {
                        c4 = c;
                        path.lineTo(arrf2[c4 + '\u0000'], arrf2[c4 + '\u0001']);
                        f5 = arrf2[c4 + '\u0000'];
                        f6 = arrf2[c4 + '\u0001'];
                        break;
                    }
                    case 'H': {
                        c4 = c;
                        path.lineTo(arrf2[c4 + '\u0000'], f6);
                        f5 = arrf2[c4 + '\u0000'];
                        break;
                    }
                    case 'C': {
                        c4 = c;
                        path.cubicTo(arrf2[c4 + '\u0000'], arrf2[c4 + '\u0001'], arrf2[c4 + 2], arrf2[c4 + 3], arrf2[c4 + 4], arrf2[c4 + 5]);
                        f5 = arrf2[c4 + 4];
                        f6 = arrf2[c4 + 5];
                        f = arrf2[c4 + 2];
                        f2 = arrf2[c4 + 3];
                        break;
                    }
                    case 'A': {
                        c4 = c;
                        f = arrf2[c4 + 5];
                        f2 = arrf2[c4 + 6];
                        f7 = arrf2[c4 + '\u0000'];
                        f8 = arrf2[c4 + '\u0001'];
                        f9 = arrf2[c4 + 2];
                        bl = arrf2[c4 + 3] != 0.0f;
                        bl2 = arrf2[c4 + 4] != 0.0f;
                        PathDataNode.drawArc(path, f5, f6, f, f2, f7, f8, f9, bl, bl2);
                        f5 = arrf2[c4 + 5];
                        f6 = arrf2[c4 + 6];
                        f = f5;
                        f2 = f6;
                    }
                }
                c4 = c2;
                c = (char)(c + n);
            }
            arrf[0] = f5;
            arrf[1] = f6;
            arrf[2] = f;
            arrf[3] = f2;
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
            d10 = - d10;
            double d15 = n;
            Double.isNaN(d15);
            double d16 = d9 / d15;
            d15 = d14 * d11 * d13 - d4 * d12 * d7;
            double d17 = d10 * d12 * d13 + d4 * d11 * d7;
            int n2 = 0;
            double d18 = d8;
            d14 = d6;
            d10 = d5;
            d9 = d13;
            d5 = d12;
            d6 = d11;
            d8 = d16;
            do {
                d13 = d3;
                if (n2 >= n) break;
                double d19 = d18 + d8;
                double d20 = Math.sin(d19);
                double d21 = Math.cos(d19);
                d16 = d + d13 * d6 * d21 - d4 * d5 * d20;
                d12 = d2 + d13 * d5 * d21 + d4 * d6 * d20;
                d11 = (- d13) * d6 * d20 - d4 * d5 * d21;
                d13 = (- d13) * d5 * d20 + d4 * d6 * d21;
                d20 = Math.tan((d19 - d18) / 2.0);
                d18 = Math.sin(d19 - d18) * (Math.sqrt(d20 * 3.0 * d20 + 4.0) - 1.0) / 3.0;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float)(d10 + d18 * d15), (float)(d14 + d18 * d17), (float)(d16 - d18 * d11), (float)(d12 - d18 * d13), (float)d16, (float)d12);
                d18 = d19;
                d10 = d16;
                d14 = d12;
                d15 = d11;
                d17 = d13;
                ++n2;
            } while (true);
        }

        private static void drawArc(Path path, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, boolean bl2) {
            double d = Math.toRadians(f7);
            double d2 = Math.cos(d);
            double d3 = Math.sin(d);
            double d4 = f;
            Double.isNaN(d4);
            double d5 = f2;
            Double.isNaN(d5);
            double d6 = f5;
            Double.isNaN(d6);
            d4 = (d4 * d2 + d5 * d3) / d6;
            d5 = - f;
            Double.isNaN(d5);
            d6 = f2;
            Double.isNaN(d6);
            double d7 = f6;
            Double.isNaN(d7);
            d7 = (d5 * d3 + d6 * d2) / d7;
            d5 = f3;
            Double.isNaN(d5);
            d6 = f4;
            Double.isNaN(d6);
            double d8 = f5;
            Double.isNaN(d8);
            d8 = (d5 * d2 + d6 * d3) / d8;
            d5 = - f3;
            Double.isNaN(d5);
            d6 = f4;
            Double.isNaN(d6);
            double d9 = f6;
            Double.isNaN(d9);
            d9 = (d5 * d3 + d6 * d2) / d9;
            double d10 = d4 - d8;
            double d11 = d7 - d9;
            d6 = (d4 + d8) / 2.0;
            d5 = (d7 + d9) / 2.0;
            double d12 = d10 * d10 + d11 * d11;
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
            d10 = d12 * d10;
            d11 = d12 * d11;
            if (bl == bl2) {
                d6 -= d11;
                d5 += d10;
            } else {
                d6 += d11;
                d5 -= d10;
            }
            d7 = Math.atan2(d7 - d5, d4 - d6);
            d4 = Math.atan2(d9 - d5, d8 - d6) - d7;
            bl = d4 >= 0.0;
            if (bl2 != bl) {
                d4 = d4 > 0.0 ? (d4 -= 6.283185307179586) : (d4 += 6.283185307179586);
            }
            d8 = f5;
            Double.isNaN(d8);
            d8 = f6;
            Double.isNaN(d8);
            d5 = d8 * d5;
            PathDataNode.arcToBezier(path, d6 * d2 - d5 * d3, (d6 *= d8) * d3 + d5 * d2, f5, f6, f, f2, d, d7, d4);
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
            for (int i = 0; i < (arrf = pathDataNode.mParams).length; ++i) {
                this.mParams[i] = arrf[i] * (1.0f - f) + pathDataNode2.mParams[i] * f;
            }
        }
    }

}

