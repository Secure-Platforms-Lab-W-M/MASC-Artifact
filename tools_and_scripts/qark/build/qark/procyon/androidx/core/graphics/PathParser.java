// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics;

import android.util.Log;
import android.graphics.Path;
import java.util.ArrayList;

public class PathParser
{
    private static final String LOGTAG = "PathParser";
    
    private PathParser() {
    }
    
    private static void addNode(final ArrayList<PathDataNode> list, final char c, final float[] array) {
        list.add(new PathDataNode(c, array));
    }
    
    public static boolean canMorph(final PathDataNode[] array, final PathDataNode[] array2) {
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i].mType != array2[i].mType) {
                return false;
            }
            if (array[i].mParams.length != array2[i].mParams.length) {
                return false;
            }
        }
        return true;
    }
    
    static float[] copyOfRange(final float[] array, final int n, int n2) {
        if (n > n2) {
            throw new IllegalArgumentException();
        }
        final int length = array.length;
        if (n >= 0 && n <= length) {
            n2 -= n;
            final int min = Math.min(n2, length - n);
            final float[] array2 = new float[n2];
            System.arraycopy(array, n, array2, 0, min);
            return array2;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public static PathDataNode[] createNodesFromPathData(final String s) {
        if (s == null) {
            return null;
        }
        int n = 0;
        int i = 1;
        final ArrayList<PathDataNode> list = new ArrayList<PathDataNode>();
        while (i < s.length()) {
            final int nextStart = nextStart(s, i);
            final String trim = s.substring(n, nextStart).trim();
            if (trim.length() > 0) {
                addNode(list, trim.charAt(0), getFloats(trim));
            }
            n = nextStart;
            i = nextStart + 1;
        }
        if (i - n == 1 && n < s.length()) {
            addNode(list, s.charAt(n), new float[0]);
        }
        return list.toArray(new PathDataNode[list.size()]);
    }
    
    public static Path createPathFromPathData(final String s) {
        final Path path = new Path();
        final PathDataNode[] nodesFromPathData = createNodesFromPathData(s);
        if (nodesFromPathData != null) {
            try {
                PathDataNode.nodesToPath(nodesFromPathData, path);
                return path;
            }
            catch (RuntimeException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Error in parsing ");
                sb.append(s);
                throw new RuntimeException(sb.toString(), ex);
            }
        }
        return null;
    }
    
    public static PathDataNode[] deepCopyNodes(final PathDataNode[] array) {
        if (array == null) {
            return null;
        }
        final PathDataNode[] array2 = new PathDataNode[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = new PathDataNode(array[i]);
        }
        return array2;
    }
    
    private static void extract(final String s, final int n, final ExtractFloatResult extractFloatResult) {
        int i = n;
        int n2 = 0;
        extractFloatResult.mEndWithNegOrDot = false;
        int n3 = 0;
        int n4 = 0;
        while (i < s.length()) {
            final boolean b = false;
            final char char1 = s.charAt(i);
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            Label_0204: {
                if (char1 != ' ') {
                    if (char1 == 'E' || char1 == 'e') {
                        n5 = 1;
                        n6 = n2;
                        n7 = n3;
                        break Label_0204;
                    }
                    switch (char1) {
                        default: {
                            n6 = n2;
                            n7 = n3;
                            n5 = (b ? 1 : 0);
                            break Label_0204;
                        }
                        case 46: {
                            if (n3 == 0) {
                                n7 = 1;
                                n6 = n2;
                                n5 = (b ? 1 : 0);
                                break Label_0204;
                            }
                            n6 = 1;
                            extractFloatResult.mEndWithNegOrDot = true;
                            n7 = n3;
                            n5 = (b ? 1 : 0);
                            break Label_0204;
                        }
                        case 45: {
                            n6 = n2;
                            n7 = n3;
                            n5 = (b ? 1 : 0);
                            if (i == n) {
                                break Label_0204;
                            }
                            n6 = n2;
                            n7 = n3;
                            n5 = (b ? 1 : 0);
                            if (n4 == 0) {
                                n6 = 1;
                                extractFloatResult.mEndWithNegOrDot = true;
                                n7 = n3;
                                n5 = (b ? 1 : 0);
                            }
                            break Label_0204;
                        }
                        case 44: {
                            break;
                        }
                    }
                }
                n6 = 1;
                n5 = (b ? 1 : 0);
                n7 = n3;
            }
            if (n6 != 0) {
                break;
            }
            ++i;
            n2 = n6;
            n3 = n7;
            n4 = n5;
        }
        extractFloatResult.mEndPosition = i;
    }
    
    private static float[] getFloats(final String s) {
        if (s.charAt(0) != 'z') {
            if (s.charAt(0) != 'Z') {
                while (true) {
                    while (true) {
                        int mEndPosition = 0;
                        int n2 = 0;
                        Label_0177: {
                            try {
                                final float[] array = new float[s.length()];
                                int n = 0;
                                int i = 1;
                                final ExtractFloatResult extractFloatResult = new ExtractFloatResult();
                                final int length = s.length();
                                while (i < length) {
                                    extract(s, i, extractFloatResult);
                                    mEndPosition = extractFloatResult.mEndPosition;
                                    n2 = n;
                                    if (i < mEndPosition) {
                                        array[n] = Float.parseFloat(s.substring(i, mEndPosition));
                                        n2 = n + 1;
                                    }
                                    if (!extractFloatResult.mEndWithNegOrDot) {
                                        break Label_0177;
                                    }
                                    i = mEndPosition;
                                    n = n2;
                                }
                                return copyOfRange(array, 0, n);
                            }
                            catch (NumberFormatException ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("error in parsing \"");
                                sb.append(s);
                                sb.append("\"");
                                throw new RuntimeException(sb.toString(), ex);
                            }
                            break;
                        }
                        int i = mEndPosition + 1;
                        int n = n2;
                        continue;
                    }
                }
            }
        }
        return new float[0];
    }
    
    public static boolean interpolatePathDataNodes(final PathDataNode[] array, final PathDataNode[] array2, final PathDataNode[] array3, final float n) {
        if (array == null || array2 == null || array3 == null) {
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
        }
        if (array.length != array2.length || array2.length != array3.length) {
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
        }
        if (!canMorph(array2, array3)) {
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            array[i].interpolatePathDataNode(array2[i], array3[i], n);
        }
        return true;
    }
    
    private static int nextStart(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (((char1 - 'A') * (char1 - 'Z') <= 0 || (char1 - 'a') * (char1 - 'z') <= 0) && char1 != 'e' && char1 != 'E') {
                return i;
            }
            ++i;
        }
        return i;
    }
    
    public static void updateNodes(final PathDataNode[] array, final PathDataNode[] array2) {
        for (int i = 0; i < array2.length; ++i) {
            array[i].mType = array2[i].mType;
            for (int j = 0; j < array2[i].mParams.length; ++j) {
                array[i].mParams[j] = array2[i].mParams[j];
            }
        }
    }
    
    private static class ExtractFloatResult
    {
        int mEndPosition;
        boolean mEndWithNegOrDot;
        
        ExtractFloatResult() {
        }
    }
    
    public static class PathDataNode
    {
        public float[] mParams;
        public char mType;
        
        PathDataNode(final char mType, final float[] mParams) {
            this.mType = mType;
            this.mParams = mParams;
        }
        
        PathDataNode(final PathDataNode pathDataNode) {
            this.mType = pathDataNode.mType;
            final float[] mParams = pathDataNode.mParams;
            this.mParams = PathParser.copyOfRange(mParams, 0, mParams.length);
        }
        
        private static void addCommand(final Path path, final float[] array, final char c, final char c2, final float[] array2) {
            float n = array[0];
            float n2 = array[1];
            float n3 = array[2];
            float n4 = array[3];
            final float n5 = array[4];
            final float n6 = array[5];
            int n7 = 0;
            switch (c2) {
                default: {
                    n7 = 2;
                    break;
                }
                case 'Z':
                case 'z': {
                    path.close();
                    n = n5;
                    n2 = n6;
                    n3 = n5;
                    n4 = n6;
                    path.moveTo(n, n2);
                    n7 = 2;
                    break;
                }
                case 'Q':
                case 'S':
                case 'q':
                case 's': {
                    n7 = 4;
                    break;
                }
                case 'L':
                case 'M':
                case 'T':
                case 'l':
                case 'm':
                case 't': {
                    n7 = 2;
                    break;
                }
                case 'H':
                case 'V':
                case 'h':
                case 'v': {
                    n7 = 1;
                    break;
                }
                case 'C':
                case 'c': {
                    n7 = 6;
                    break;
                }
                case 'A':
                case 'a': {
                    n7 = 7;
                    break;
                }
            }
            final int n8 = 0;
            float n9 = n3;
            float n10 = n4;
            float n11 = n6;
            float n12 = n2;
            float n13 = n5;
            char c3 = c;
            int i = n8;
            float n14 = n;
            while (i < array2.length) {
                float n15;
                float n16;
                if (c2 != 'A') {
                    if (c2 != 'C') {
                        if (c2 != 'H') {
                            if (c2 != 'Q') {
                                if (c2 != 'V') {
                                    if (c2 != 'a') {
                                        if (c2 != 'c') {
                                            if (c2 != 'h') {
                                                if (c2 != 'q') {
                                                    if (c2 != 'v') {
                                                        if (c2 != 'L') {
                                                            if (c2 != 'M') {
                                                                if (c2 != 'S') {
                                                                    if (c2 != 'T') {
                                                                        if (c2 != 'l') {
                                                                            if (c2 != 'm') {
                                                                                if (c2 != 's') {
                                                                                    if (c2 != 't') {
                                                                                        n15 = n9;
                                                                                        n16 = n10;
                                                                                    }
                                                                                    else {
                                                                                        float n17 = 0.0f;
                                                                                        float n18 = 0.0f;
                                                                                        if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                                                                                            n17 = n14 - n9;
                                                                                            n18 = n12 - n10;
                                                                                        }
                                                                                        path.rQuadTo(n17, n18, array2[i + 0], array2[i + 1]);
                                                                                        final float n19 = n14 + array2[i + 0];
                                                                                        final float n20 = n12 + array2[i + 1];
                                                                                        final float n21 = n14 + n17;
                                                                                        final float n22 = n12 + n18;
                                                                                        n12 = n20;
                                                                                        n14 = n19;
                                                                                        n15 = n21;
                                                                                        n16 = n22;
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    float n23;
                                                                                    float n24;
                                                                                    if (c3 != 'c' && c3 != 's' && c3 != 'C' && c3 != 'S') {
                                                                                        n23 = 0.0f;
                                                                                        n24 = 0.0f;
                                                                                    }
                                                                                    else {
                                                                                        n23 = n14 - n9;
                                                                                        n24 = n12 - n10;
                                                                                    }
                                                                                    path.rCubicTo(n23, n24, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                                                                                    final float n25 = array2[i + 0];
                                                                                    final float n26 = array2[i + 1];
                                                                                    final float n27 = n14 + array2[i + 2];
                                                                                    final float n28 = array2[i + 3];
                                                                                    final float n29 = n25 + n14;
                                                                                    final float n30 = n12 + n26;
                                                                                    n12 += n28;
                                                                                    n14 = n27;
                                                                                    n15 = n29;
                                                                                    n16 = n30;
                                                                                }
                                                                            }
                                                                            else {
                                                                                n14 += array2[i + 0];
                                                                                n12 += array2[i + 1];
                                                                                if (i > 0) {
                                                                                    path.rLineTo(array2[i + 0], array2[i + 1]);
                                                                                    n15 = n9;
                                                                                    n16 = n10;
                                                                                }
                                                                                else {
                                                                                    path.rMoveTo(array2[i + 0], array2[i + 1]);
                                                                                    n13 = n14;
                                                                                    n11 = n12;
                                                                                    n15 = n9;
                                                                                    n16 = n10;
                                                                                }
                                                                            }
                                                                        }
                                                                        else {
                                                                            path.rLineTo(array2[i + 0], array2[i + 1]);
                                                                            n14 += array2[i + 0];
                                                                            n12 += array2[i + 1];
                                                                            n15 = n9;
                                                                            n16 = n10;
                                                                        }
                                                                    }
                                                                    else {
                                                                        float n31 = n14;
                                                                        float n32 = n12;
                                                                        if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                                                                            n31 = n14 * 2.0f - n9;
                                                                            n32 = n12 * 2.0f - n10;
                                                                        }
                                                                        path.quadTo(n31, n32, array2[i + 0], array2[i + 1]);
                                                                        n14 = array2[i + 0];
                                                                        n12 = array2[i + 1];
                                                                        final float n33 = n32;
                                                                        n15 = n31;
                                                                        n16 = n33;
                                                                    }
                                                                }
                                                                else {
                                                                    if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                                                                        n14 = n14 * 2.0f - n9;
                                                                        n12 = n12 * 2.0f - n10;
                                                                    }
                                                                    path.cubicTo(n14, n12, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                                                                    n15 = array2[i + 0];
                                                                    n16 = array2[i + 1];
                                                                    n14 = array2[i + 2];
                                                                    n12 = array2[i + 3];
                                                                }
                                                            }
                                                            else {
                                                                n14 = array2[i + 0];
                                                                n12 = array2[i + 1];
                                                                if (i > 0) {
                                                                    path.lineTo(array2[i + 0], array2[i + 1]);
                                                                    n15 = n9;
                                                                    n16 = n10;
                                                                }
                                                                else {
                                                                    path.moveTo(array2[i + 0], array2[i + 1]);
                                                                    final float n34 = n14;
                                                                    final float n35 = n12;
                                                                    n13 = n14;
                                                                    n11 = n12;
                                                                    n12 = n35;
                                                                    n14 = n34;
                                                                    n15 = n9;
                                                                    n16 = n10;
                                                                }
                                                            }
                                                        }
                                                        else {
                                                            path.lineTo(array2[i + 0], array2[i + 1]);
                                                            n14 = array2[i + 0];
                                                            n12 = array2[i + 1];
                                                            n15 = n9;
                                                            n16 = n10;
                                                        }
                                                    }
                                                    else {
                                                        path.rLineTo(0.0f, array2[i + 0]);
                                                        n12 += array2[i + 0];
                                                        n15 = n9;
                                                        n16 = n10;
                                                    }
                                                }
                                                else {
                                                    path.rQuadTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                                                    final float n36 = array2[i + 0];
                                                    final float n37 = array2[i + 1];
                                                    final float n38 = n14 + array2[i + 2];
                                                    final float n39 = array2[i + 3];
                                                    final float n40 = n36 + n14;
                                                    final float n41 = n12 + n37;
                                                    n12 += n39;
                                                    n14 = n38;
                                                    n15 = n40;
                                                    n16 = n41;
                                                }
                                            }
                                            else {
                                                path.rLineTo(array2[i + 0], 0.0f);
                                                n14 += array2[i + 0];
                                                n15 = n9;
                                                n16 = n10;
                                            }
                                        }
                                        else {
                                            path.rCubicTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3], array2[i + 4], array2[i + 5]);
                                            final float n42 = array2[i + 2];
                                            final float n43 = array2[i + 3];
                                            final float n44 = n14 + array2[i + 4];
                                            final float n45 = array2[i + 5];
                                            final float n46 = n42 + n14;
                                            final float n47 = n12 + n43;
                                            n12 += n45;
                                            n14 = n44;
                                            n15 = n46;
                                            n16 = n47;
                                        }
                                    }
                                    else {
                                        final float n48 = array2[i + 5];
                                        final float n49 = array2[i + 6];
                                        final float n50 = array2[i + 0];
                                        final float n51 = array2[i + 1];
                                        final float n52 = array2[i + 2];
                                        final boolean b = array2[i + 3] != 0.0f;
                                        final boolean b2 = array2[i + 4] != 0.0f;
                                        final int n53 = i;
                                        drawArc(path, n14, n12, n48 + n14, n49 + n12, n50, n51, n52, b, b2);
                                        n14 += array2[n53 + 5];
                                        n12 += array2[n53 + 6];
                                        n15 = n14;
                                        n16 = n12;
                                    }
                                }
                                else {
                                    final int n54 = i;
                                    path.lineTo(n14, array2[n54 + 0]);
                                    n12 = array2[n54 + 0];
                                    n15 = n9;
                                    n16 = n10;
                                }
                            }
                            else {
                                final int n55 = i;
                                path.quadTo(array2[n55 + 0], array2[n55 + 1], array2[n55 + 2], array2[n55 + 3]);
                                n15 = array2[n55 + 0];
                                n16 = array2[n55 + 1];
                                n14 = array2[n55 + 2];
                                n12 = array2[n55 + 3];
                            }
                        }
                        else {
                            final int n56 = i;
                            path.lineTo(array2[n56 + 0], n12);
                            n14 = array2[n56 + 0];
                            n15 = n9;
                            n16 = n10;
                        }
                    }
                    else {
                        final int n57 = i;
                        path.cubicTo(array2[n57 + 0], array2[n57 + 1], array2[n57 + 2], array2[n57 + 3], array2[n57 + 4], array2[n57 + 5]);
                        n14 = array2[n57 + 4];
                        n12 = array2[n57 + 5];
                        n15 = array2[n57 + 2];
                        n16 = array2[n57 + 3];
                    }
                }
                else {
                    final int n58 = i;
                    drawArc(path, n14, n12, array2[n58 + 5], array2[n58 + 6], array2[n58 + 0], array2[n58 + 1], array2[n58 + 2], array2[n58 + 3] != 0.0f, array2[n58 + 4] != 0.0f);
                    n15 = array2[n58 + 5];
                    n16 = array2[n58 + 6];
                    n14 = n15;
                    n12 = n16;
                }
                c3 = c2;
                i += n7;
                n9 = n15;
                n10 = n16;
            }
            array[0] = n14;
            array[1] = n12;
            array[2] = n9;
            array[3] = n10;
            array[4] = n13;
            array[5] = n11;
        }
        
        private static void arcToBezier(final Path path, final double n, final double n2, final double n3, final double n4, double n5, double n6, double cos, double n7, double n8) {
            final int n9 = (int)Math.ceil(Math.abs(n8 * 4.0 / 3.141592653589793));
            final double cos2 = Math.cos(cos);
            final double sin = Math.sin(cos);
            cos = Math.cos(n7);
            final double sin2 = Math.sin(n7);
            final double n10 = -n3;
            double n11 = -n3 * sin * sin2 + n4 * cos2 * cos;
            n8 /= n9;
            int i = 0;
            double n12 = n5;
            double n13 = n10 * cos2 * sin2 - n4 * sin * cos;
            double n14 = n7;
            double n15 = n6;
            n7 = sin2;
            n5 = sin;
            n6 = cos2;
            while (i < n9) {
                final double n16 = n14 + n8;
                final double sin3 = Math.sin(n16);
                final double cos3 = Math.cos(n16);
                final double n17 = n + n3 * n6 * cos3 - n4 * n5 * sin3;
                final double n18 = n2 + n3 * n5 * cos3 + n4 * n6 * sin3;
                final double n19 = -n3 * n6 * sin3 - n4 * n5 * cos3;
                final double n20 = -n3 * n5 * sin3 + n4 * n6 * cos3;
                final double tan = Math.tan((n16 - n14) / 2.0);
                final double n21 = Math.sin(n16 - n14) * (Math.sqrt(tan * 3.0 * tan + 4.0) - 1.0) / 3.0;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float)(n12 + n21 * n13), (float)(n15 + n21 * n11), (float)(n17 - n21 * n19), (float)(n18 - n21 * n20), (float)n17, (float)n18);
                n14 = n16;
                n15 = n18;
                n13 = n19;
                n11 = n20;
                n12 = n17;
                ++i;
            }
        }
        
        private static void drawArc(final Path path, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final boolean b, final boolean b2) {
            final double radians = Math.toRadians(n7);
            final double cos = Math.cos(radians);
            final double sin = Math.sin(radians);
            final double n8 = (n * cos + n2 * sin) / n5;
            final double n9 = (-n * sin + n2 * cos) / n6;
            final double n10 = (n3 * cos + n4 * sin) / n5;
            final double n11 = (-n3 * sin + n4 * cos) / n6;
            final double n12 = n8 - n10;
            final double n13 = n9 - n11;
            final double n14 = (n8 + n10) / 2.0;
            final double n15 = (n9 + n11) / 2.0;
            final double n16 = n12 * n12 + n13 * n13;
            if (n16 == 0.0) {
                Log.w("PathParser", " Points are coincident");
                return;
            }
            final double n17 = 1.0 / n16 - 0.25;
            if (n17 < 0.0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Points are too far apart ");
                sb.append(n16);
                Log.w("PathParser", sb.toString());
                final float n18 = (float)(Math.sqrt(n16) / 1.99999);
                drawArc(path, n, n2, n3, n4, n5 * n18, n6 * n18, n7, b, b2);
                return;
            }
            final double sqrt = Math.sqrt(n17);
            final double n19 = sqrt * n12;
            final double n20 = sqrt * n13;
            double n21;
            double n22;
            if (b == b2) {
                n21 = n14 - n20;
                n22 = n15 + n19;
            }
            else {
                n21 = n14 + n20;
                n22 = n15 - n19;
            }
            final double atan2 = Math.atan2(n9 - n22, n8 - n21);
            final double n23 = Math.atan2(n11 - n22, n10 - n21) - atan2;
            final boolean b3 = n23 >= 0.0;
            double n24 = n23;
            if (b2 != b3) {
                if (n23 > 0.0) {
                    n24 = n23 - 6.283185307179586;
                }
                else {
                    n24 = n23 + 6.283185307179586;
                }
            }
            final double n25 = n21 * n5;
            final double n26 = n6 * n22;
            arcToBezier(path, n25 * cos - n26 * sin, n25 * sin + n26 * cos, n5, n6, n, n2, radians, atan2, n24);
        }
        
        public static void nodesToPath(final PathDataNode[] array, final Path path) {
            final float[] array2 = new float[6];
            char mType = 'm';
            for (int i = 0; i < array.length; ++i) {
                addCommand(path, array2, mType, array[i].mType, array[i].mParams);
                mType = array[i].mType;
            }
        }
        
        public void interpolatePathDataNode(final PathDataNode pathDataNode, final PathDataNode pathDataNode2, final float n) {
            this.mType = pathDataNode.mType;
            int n2 = 0;
            while (true) {
                final float[] mParams = pathDataNode.mParams;
                if (n2 >= mParams.length) {
                    break;
                }
                this.mParams[n2] = mParams[n2] * (1.0f - n) + pathDataNode2.mParams[n2] * n;
                ++n2;
            }
        }
    }
}
