// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics;

import android.util.Log;
import android.graphics.Path;
import java.util.ArrayList;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class PathParser
{
    private static final String LOGTAG = "PathParser";
    
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
        boolean b = false;
        extractFloatResult.mEndWithNegOrDot = false;
        int n2 = 0;
        boolean b2 = false;
        while (i < s.length()) {
            final boolean b3 = false;
            final char char1 = s.charAt(i);
            Label_0164: {
                if (char1 != ' ') {
                    if (char1 == 'E' || char1 == 'e') {
                        b2 = true;
                        break Label_0164;
                    }
                    switch (char1) {
                        default: {
                            b2 = b3;
                            break Label_0164;
                        }
                        case 46: {
                            if (n2 == 0) {
                                n2 = 1;
                                b2 = b3;
                                break Label_0164;
                            }
                            b = true;
                            extractFloatResult.mEndWithNegOrDot = true;
                            b2 = b3;
                            break Label_0164;
                        }
                        case 45: {
                            if (i != n && !b2) {
                                b = true;
                                extractFloatResult.mEndWithNegOrDot = true;
                                b2 = b3;
                                break Label_0164;
                            }
                            b2 = b3;
                            break Label_0164;
                        }
                        case 44: {
                            break;
                        }
                    }
                }
                b = true;
                b2 = b3;
            }
            if (b) {
                break;
            }
            ++i;
        }
        extractFloatResult.mEndPosition = i;
    }
    
    private static float[] getFloats(final String s) {
        if (s.charAt(0) != 'z') {
            if (s.charAt(0) != 'Z') {
                while (true) {
                Label_0094_Outer:
                    while (true) {
                        int mEndPosition = 0;
                    Label_0175:
                        while (true) {
                            Label_0172: {
                                try {
                                    final float[] array = new float[s.length()];
                                    int n = 0;
                                    int i = 1;
                                    final ExtractFloatResult extractFloatResult = new ExtractFloatResult();
                                    final int length = s.length();
                                    while (i < length) {
                                        extract(s, i, extractFloatResult);
                                        mEndPosition = extractFloatResult.mEndPosition;
                                        if (i >= mEndPosition) {
                                            break Label_0172;
                                        }
                                        array[n] = Float.parseFloat(s.substring(i, mEndPosition));
                                        ++n;
                                        if (!extractFloatResult.mEndWithNegOrDot) {
                                            break Label_0175;
                                        }
                                        i = mEndPosition;
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
                            continue;
                        }
                        int i = mEndPosition + 1;
                        continue Label_0094_Outer;
                    }
                }
            }
        }
        return new float[0];
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
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public float[] mParams;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
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
            final float n9 = n;
            final float n10 = n2;
            float n11 = n3;
            float n12 = n4;
            char c3 = c;
            float n13 = n6;
            float n14 = n5;
            int i = n8;
            float n15 = n9;
            float n16 = n10;
            while (i < array2.length) {
                switch (c2) {
                    case 'v': {
                        path.rLineTo(0.0f, array2[i + 0]);
                        n16 += array2[i + 0];
                        break;
                    }
                    case 't': {
                        final float n17 = 0.0f;
                        final float n18 = 0.0f;
                        float n19;
                        float n20;
                        if (c3 != 'q' && c3 != 't' && c3 != 'Q' && c3 != 'T') {
                            n19 = n17;
                            n20 = n18;
                        }
                        else {
                            final float n21 = n15 - n11;
                            n20 = n16 - n12;
                            n19 = n21;
                        }
                        path.rQuadTo(n19, n20, array2[i + 0], array2[i + 1]);
                        final float n22 = n15 + array2[i + 0];
                        final float n23 = n16 + array2[i + 1];
                        final float n24 = n15 + n19;
                        final float n25 = n16 + n20;
                        n16 = n23;
                        n15 = n22;
                        n11 = n24;
                        n12 = n25;
                        break;
                    }
                    case 's': {
                        float n26;
                        float n27;
                        if (c3 != 'c' && c3 != 's' && c3 != 'C' && c3 != 'S') {
                            n26 = 0.0f;
                            n27 = 0.0f;
                        }
                        else {
                            n26 = n15 - n11;
                            n27 = n16 - n12;
                        }
                        path.rCubicTo(n26, n27, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                        final float n28 = array2[i + 0];
                        final float n29 = array2[i + 1];
                        final float n30 = n15 + array2[i + 2];
                        final float n31 = n16 + array2[i + 3];
                        final float n32 = n28 + n15;
                        final float n33 = n29 + n16;
                        n16 = n31;
                        n15 = n30;
                        n11 = n32;
                        n12 = n33;
                        break;
                    }
                    case 'q': {
                        path.rQuadTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                        final float n34 = array2[i + 0];
                        final float n35 = array2[i + 1];
                        final float n36 = n15 + array2[i + 2];
                        final float n37 = n16 + array2[i + 3];
                        final float n38 = n34 + n15;
                        final float n39 = n35 + n16;
                        n16 = n37;
                        n15 = n36;
                        n11 = n38;
                        n12 = n39;
                        break;
                    }
                    case 'm': {
                        n15 += array2[i + 0];
                        n16 += array2[i + 1];
                        if (i > 0) {
                            path.rLineTo(array2[i + 0], array2[i + 1]);
                            break;
                        }
                        path.rMoveTo(array2[i + 0], array2[i + 1]);
                        n14 = n15;
                        n13 = n16;
                        break;
                    }
                    case 'l': {
                        path.rLineTo(array2[i + 0], array2[i + 1]);
                        n15 += array2[i + 0];
                        n16 += array2[i + 1];
                        break;
                    }
                    case 'h': {
                        path.rLineTo(array2[i + 0], 0.0f);
                        n15 += array2[i + 0];
                        break;
                    }
                    case 'c': {
                        path.rCubicTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3], array2[i + 4], array2[i + 5]);
                        final float n40 = array2[i + 2];
                        final float n41 = array2[i + 3];
                        final float n42 = n15 + array2[i + 4];
                        final float n43 = n16 + array2[i + 5];
                        final float n44 = n40 + n15;
                        final float n45 = n41 + n16;
                        n16 = n43;
                        n15 = n42;
                        n11 = n44;
                        n12 = n45;
                        break;
                    }
                    case 'a': {
                        final float n46 = array2[i + 5];
                        final float n47 = array2[i + 6];
                        final float n48 = array2[i + 0];
                        final float n49 = array2[i + 1];
                        final float n50 = array2[i + 2];
                        final boolean b = array2[i + 3] != 0.0f;
                        final boolean b2 = array2[i + 4] != 0.0f;
                        final int n51 = i;
                        drawArc(path, n15, n16, n46 + n15, n47 + n16, n48, n49, n50, b, b2);
                        n15 += array2[n51 + 5];
                        n16 += array2[n51 + 6];
                        n11 = n15;
                        n12 = n16;
                        break;
                    }
                    case 'V': {
                        final int n52 = i;
                        path.lineTo(n15, array2[n52 + 0]);
                        n16 = array2[n52 + 0];
                        break;
                    }
                    case 'T': {
                        final int n53 = i;
                        final float n54 = n15;
                        final float n55 = n16;
                        float n56;
                        float n57;
                        if (c3 != 'q' && c3 != 't' && c3 != 'Q' && c3 != 'T') {
                            n56 = n54;
                            n57 = n55;
                        }
                        else {
                            n56 = n15 * 2.0f - n11;
                            n57 = n16 * 2.0f - n12;
                        }
                        path.quadTo(n56, n57, array2[n53 + 0], array2[n53 + 1]);
                        final float n58 = array2[n53 + 0];
                        final float n59 = array2[n53 + 1];
                        n11 = n56;
                        n12 = n57;
                        n16 = n59;
                        n15 = n58;
                        break;
                    }
                    case 'S': {
                        final int n60 = i;
                        if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                            n15 = n15 * 2.0f - n11;
                            n16 = n16 * 2.0f - n12;
                        }
                        path.cubicTo(n15, n16, array2[n60 + 0], array2[n60 + 1], array2[n60 + 2], array2[n60 + 3]);
                        n11 = array2[n60 + 0];
                        n12 = array2[n60 + 1];
                        n15 = array2[n60 + 2];
                        n16 = array2[n60 + 3];
                        break;
                    }
                    case 'Q': {
                        final int n61 = i;
                        path.quadTo(array2[n61 + 0], array2[n61 + 1], array2[n61 + 2], array2[n61 + 3]);
                        n11 = array2[n61 + 0];
                        n12 = array2[n61 + 1];
                        n15 = array2[n61 + 2];
                        n16 = array2[n61 + 3];
                        break;
                    }
                    case 'M': {
                        final int n62 = i;
                        n15 = array2[n62 + 0];
                        n16 = array2[n62 + 1];
                        if (n62 > 0) {
                            path.lineTo(array2[n62 + 0], array2[n62 + 1]);
                            break;
                        }
                        path.moveTo(array2[n62 + 0], array2[n62 + 1]);
                        n14 = n15;
                        n13 = n16;
                        break;
                    }
                    case 'L': {
                        final int n63 = i;
                        path.lineTo(array2[n63 + 0], array2[n63 + 1]);
                        n15 = array2[n63 + 0];
                        n16 = array2[n63 + 1];
                        break;
                    }
                    case 'H': {
                        final int n64 = i;
                        path.lineTo(array2[n64 + 0], n16);
                        n15 = array2[n64 + 0];
                        break;
                    }
                    case 'C': {
                        final int n65 = i;
                        path.cubicTo(array2[n65 + 0], array2[n65 + 1], array2[n65 + 2], array2[n65 + 3], array2[n65 + 4], array2[n65 + 5]);
                        n15 = array2[n65 + 4];
                        n16 = array2[n65 + 5];
                        n11 = array2[n65 + 2];
                        n12 = array2[n65 + 3];
                        break;
                    }
                    case 'A': {
                        final int n66 = i;
                        drawArc(path, n15, n16, array2[n66 + 5], array2[n66 + 6], array2[n66 + 0], array2[n66 + 1], array2[n66 + 2], array2[n66 + 3] != 0.0f, array2[n66 + 4] != 0.0f);
                        n15 = array2[n66 + 5];
                        n16 = array2[n66 + 6];
                        n11 = n15;
                        n12 = n16;
                        break;
                    }
                }
                c3 = c2;
                i += n7;
            }
            array[0] = n15;
            array[1] = n16;
            array[2] = n11;
            array[3] = n12;
            array[4] = n14;
            array[5] = n13;
        }
        
        private static void arcToBezier(final Path path, final double n, final double n2, final double n3, final double n4, double n5, double n6, double cos, double n7, double n8) {
            final int n9 = (int)Math.ceil(Math.abs(n8 * 4.0 / 3.141592653589793));
            final double cos2 = Math.cos(cos);
            final double sin = Math.sin(cos);
            cos = Math.cos(n7);
            final double sin2 = Math.sin(n7);
            final double n10 = -n3;
            final double n11 = -n3;
            final double n12 = n9;
            Double.isNaN(n12);
            final double n13 = n8 / n12;
            double n14 = n10 * cos2 * sin2 - n4 * sin * cos;
            double n15 = n11 * sin * sin2 + n4 * cos2 * cos;
            int i = 0;
            double n16 = n7;
            double n17 = n6;
            double n18 = n5;
            n8 = sin2;
            n5 = sin;
            n6 = cos2;
            n7 = n13;
            while (i < n9) {
                final double n19 = n16 + n7;
                final double sin3 = Math.sin(n19);
                final double cos3 = Math.cos(n19);
                final double n20 = n + n3 * n6 * cos3 - n4 * n5 * sin3;
                final double n21 = n2 + n3 * n5 * cos3 + n4 * n6 * sin3;
                final double n22 = -n3 * n6 * sin3 - n4 * n5 * cos3;
                final double n23 = -n3 * n5 * sin3 + n4 * n6 * cos3;
                final double tan = Math.tan((n19 - n16) / 2.0);
                final double n24 = Math.sin(n19 - n16) * (Math.sqrt(tan * 3.0 * tan + 4.0) - 1.0) / 3.0;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float)(n18 + n24 * n14), (float)(n17 + n24 * n15), (float)(n20 - n24 * n22), (float)(n21 - n24 * n23), (float)n20, (float)n21);
                n16 = n19;
                n18 = n20;
                n17 = n21;
                n14 = n22;
                n15 = n23;
                ++i;
            }
        }
        
        private static void drawArc(final Path path, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final boolean b, final boolean b2) {
            final double radians = Math.toRadians(n7);
            final double cos = Math.cos(radians);
            final double sin = Math.sin(radians);
            final double n8 = n;
            Double.isNaN(n8);
            final double n9 = n2;
            Double.isNaN(n9);
            final double n10 = n5;
            Double.isNaN(n10);
            final double n11 = (n8 * cos + n9 * sin) / n10;
            final double n12 = -n;
            Double.isNaN(n12);
            final double n13 = n2;
            Double.isNaN(n13);
            final double n14 = n6;
            Double.isNaN(n14);
            final double n15 = (n12 * sin + n13 * cos) / n14;
            final double n16 = n3;
            Double.isNaN(n16);
            final double n17 = n4;
            Double.isNaN(n17);
            final double n18 = n5;
            Double.isNaN(n18);
            final double n19 = (n16 * cos + n17 * sin) / n18;
            final double n20 = -n3;
            Double.isNaN(n20);
            final double n21 = n4;
            Double.isNaN(n21);
            final double n22 = n6;
            Double.isNaN(n22);
            final double n23 = (n20 * sin + n21 * cos) / n22;
            final double n24 = n11 - n19;
            final double n25 = n15 - n23;
            final double n26 = (n11 + n19) / 2.0;
            final double n27 = (n15 + n23) / 2.0;
            final double n28 = n24 * n24 + n25 * n25;
            if (n28 == 0.0) {
                Log.w("PathParser", " Points are coincident");
                return;
            }
            final double n29 = 1.0 / n28 - 0.25;
            if (n29 < 0.0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Points are too far apart ");
                sb.append(n28);
                Log.w("PathParser", sb.toString());
                final float n30 = (float)(Math.sqrt(n28) / 1.99999);
                drawArc(path, n, n2, n3, n4, n5 * n30, n6 * n30, n7, b, b2);
                return;
            }
            final double sqrt = Math.sqrt(n29);
            final double n31 = sqrt * n24;
            final double n32 = sqrt * n25;
            double n33;
            double n34;
            if (b == b2) {
                n33 = n26 - n32;
                n34 = n27 + n31;
            }
            else {
                n33 = n26 + n32;
                n34 = n27 - n31;
            }
            final double atan2 = Math.atan2(n15 - n34, n11 - n33);
            double n35 = Math.atan2(n23 - n34, n19 - n33) - atan2;
            if (b2 != n35 >= 0.0) {
                if (n35 > 0.0) {
                    n35 -= 6.283185307179586;
                }
                else {
                    n35 += 6.283185307179586;
                }
            }
            final double n36 = n5;
            Double.isNaN(n36);
            final double n37 = n33 * n36;
            final double n38 = n6;
            Double.isNaN(n38);
            final double n39 = n38 * n34;
            arcToBezier(path, n37 * cos - n39 * sin, n37 * sin + n39 * cos, n5, n6, n, n2, radians, atan2, n35);
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
