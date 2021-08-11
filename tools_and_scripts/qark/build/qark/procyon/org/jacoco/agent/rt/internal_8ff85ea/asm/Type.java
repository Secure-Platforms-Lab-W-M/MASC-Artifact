// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Type
{
    public static final int ARRAY = 9;
    public static final int BOOLEAN = 1;
    public static final Type BOOLEAN_TYPE;
    public static final int BYTE = 3;
    public static final Type BYTE_TYPE;
    public static final int CHAR = 2;
    public static final Type CHAR_TYPE;
    public static final int DOUBLE = 8;
    public static final Type DOUBLE_TYPE;
    public static final int FLOAT = 6;
    public static final Type FLOAT_TYPE;
    public static final int INT = 5;
    public static final Type INT_TYPE;
    public static final int LONG = 7;
    public static final Type LONG_TYPE;
    public static final int METHOD = 11;
    public static final int OBJECT = 10;
    public static final int SHORT = 4;
    public static final Type SHORT_TYPE;
    public static final int VOID = 0;
    public static final Type VOID_TYPE;
    private final char[] buf;
    private final int len;
    private final int off;
    private final int sort;
    
    static {
        VOID_TYPE = new Type(0, null, 1443168256, 1);
        BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
        CHAR_TYPE = new Type(2, null, 1124075009, 1);
        BYTE_TYPE = new Type(3, null, 1107297537, 1);
        SHORT_TYPE = new Type(4, null, 1392510721, 1);
        INT_TYPE = new Type(5, null, 1224736769, 1);
        FLOAT_TYPE = new Type(6, null, 1174536705, 1);
        LONG_TYPE = new Type(7, null, 1241579778, 1);
        DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
    }
    
    private Type(final int sort, final char[] buf, final int off, final int len) {
        this.sort = sort;
        this.buf = buf;
        this.off = off;
        this.len = len;
    }
    
    public static Type[] getArgumentTypes(final String s) {
        final char[] charArray = s.toCharArray();
        int n = 1;
        int n2 = 0;
        while (true) {
            int n3 = n + 1;
            final char c = charArray[n];
            if (c == ')') {
                break;
            }
            if (c == 'L') {
                int n4;
                while (true) {
                    n4 = n3 + 1;
                    if (charArray[n3] == ';') {
                        break;
                    }
                    n3 = n4;
                }
                ++n2;
                n3 = n4;
            }
            else {
                int n5 = n2;
                if (c != '[') {
                    n5 = n2 + 1;
                }
                n2 = n5;
            }
            n = n3;
        }
        final Type[] array = new Type[n2];
        int len;
        int n8;
        for (int n6 = 1, n7 = 0; charArray[n6] != ')'; n6 += len + n8, ++n7) {
            array[n7] = getType(charArray, n6);
            len = array[n7].len;
            if (array[n7].sort == 10) {
                n8 = 2;
            }
            else {
                n8 = 0;
            }
        }
        return array;
    }
    
    public static Type[] getArgumentTypes(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Type[] array = new Type[parameterTypes.length];
        for (int i = parameterTypes.length - 1; i >= 0; --i) {
            array[i] = getType(parameterTypes[i]);
        }
        return array;
    }
    
    public static int getArgumentsAndReturnSizes(final String s) {
        final boolean b = true;
        int n = 1;
        int n2 = 1;
        int n3;
        while (true) {
            n3 = n2 + 1;
            final char char1 = s.charAt(n2);
            if (char1 == ')') {
                break;
            }
            int n6;
            int n7 = 0;
            if (char1 == 'L') {
                int n4;
                while (true) {
                    n4 = n3 + 1;
                    if (s.charAt(n3) == ';') {
                        break;
                    }
                    n3 = n4;
                }
                final int n5 = n + 1;
                n6 = n4;
                n7 = n5;
            }
            else {
                int n8 = 0;
                Label_0196: {
                    if (char1 == '[') {
                        char char2;
                        while (true) {
                            char2 = s.charAt(n3);
                            if (char2 != '[') {
                                break;
                            }
                            ++n3;
                        }
                        if (char2 != 'D') {
                            n7 = n;
                            n8 = n3;
                            if (char2 != 'J') {
                                break Label_0196;
                            }
                        }
                        n7 = n - 1;
                        n8 = n3;
                    }
                    else if (char1 != 'D' && char1 != 'J') {
                        n7 = n + 1;
                        n8 = n3;
                    }
                    else {
                        n7 = n + 2;
                        n8 = n3;
                    }
                }
                n6 = n8;
            }
            n2 = n6;
            n = n7;
        }
        final char char3 = s.charAt(n3);
        int n9;
        if (char3 == 'V') {
            n9 = 0;
        }
        else if (char3 != 'D' && char3 != 'J') {
            n9 = (b ? 1 : 0);
        }
        else {
            n9 = 2;
        }
        return n9 | n << 2;
    }
    
    public static String getConstructorDescriptor(final Constructor<?> constructor) {
        final Class<?>[] parameterTypes = (Class<?>[])constructor.getParameterTypes();
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < parameterTypes.length; ++i) {
            getDescriptor(sb, parameterTypes[i]);
        }
        sb.append(")V");
        return sb.toString();
    }
    
    public static String getDescriptor(final Class<?> clazz) {
        final StringBuilder sb = new StringBuilder();
        getDescriptor(sb, clazz);
        return sb.toString();
    }
    
    private void getDescriptor(final StringBuilder sb) {
        if (this.buf == null) {
            sb.append((char)((this.off & 0xFF000000) >>> 24));
            return;
        }
        if (this.sort == 10) {
            sb.append('L');
            sb.append(this.buf, this.off, this.len);
            sb.append(';');
            return;
        }
        sb.append(this.buf, this.off, this.len);
    }
    
    private static void getDescriptor(final StringBuilder sb, Class<?> componentType) {
        while (!componentType.isPrimitive()) {
            if (!componentType.isArray()) {
                sb.append('L');
                final String name = componentType.getName();
                for (int length = name.length(), i = 0; i < length; ++i) {
                    char char1 = name.charAt(i);
                    if (char1 == '.') {
                        char1 = '/';
                    }
                    sb.append(char1);
                }
                sb.append(';');
                return;
            }
            sb.append('[');
            componentType = componentType.getComponentType();
        }
        char c;
        if (componentType == Integer.TYPE) {
            c = 'I';
        }
        else if (componentType == Void.TYPE) {
            c = 'V';
        }
        else if (componentType == Boolean.TYPE) {
            c = 'Z';
        }
        else if (componentType == Byte.TYPE) {
            c = 'B';
        }
        else if (componentType == Character.TYPE) {
            c = 'C';
        }
        else if (componentType == Short.TYPE) {
            c = 'S';
        }
        else if (componentType == Double.TYPE) {
            c = 'D';
        }
        else if (componentType == Float.TYPE) {
            c = 'F';
        }
        else {
            c = 'J';
        }
        sb.append(c);
    }
    
    public static String getInternalName(final Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }
    
    public static String getMethodDescriptor(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < parameterTypes.length; ++i) {
            getDescriptor(sb, parameterTypes[i]);
        }
        sb.append(')');
        getDescriptor(sb, method.getReturnType());
        return sb.toString();
    }
    
    public static String getMethodDescriptor(final Type type, final Type... array) {
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < array.length; ++i) {
            array[i].getDescriptor(sb);
        }
        sb.append(')');
        type.getDescriptor(sb);
        return sb.toString();
    }
    
    public static Type getMethodType(final String s) {
        return getType(s.toCharArray(), 0);
    }
    
    public static Type getMethodType(final Type type, final Type... array) {
        return getType(getMethodDescriptor(type, array));
    }
    
    public static Type getObjectType(final String s) {
        final char[] charArray = s.toCharArray();
        int n;
        if (charArray[0] == '[') {
            n = 9;
        }
        else {
            n = 10;
        }
        return new Type(n, charArray, 0, charArray.length);
    }
    
    public static Type getReturnType(final String s) {
        final char[] charArray = s.toCharArray();
        int n = 1;
        int n2;
        while (true) {
            n2 = n + 1;
            final char c = charArray[n];
            if (c == ')') {
                break;
            }
            if (c == 'L') {
                int n3;
                while (true) {
                    n3 = n2 + 1;
                    if (charArray[n2] == ';') {
                        break;
                    }
                    n2 = n3;
                }
                n2 = n3;
            }
            n = n2;
        }
        return getType(charArray, n2);
    }
    
    public static Type getReturnType(final Method method) {
        return getType(method.getReturnType());
    }
    
    public static Type getType(final Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            return getType(getDescriptor(clazz));
        }
        if (clazz == Integer.TYPE) {
            return Type.INT_TYPE;
        }
        if (clazz == Void.TYPE) {
            return Type.VOID_TYPE;
        }
        if (clazz == Boolean.TYPE) {
            return Type.BOOLEAN_TYPE;
        }
        if (clazz == Byte.TYPE) {
            return Type.BYTE_TYPE;
        }
        if (clazz == Character.TYPE) {
            return Type.CHAR_TYPE;
        }
        if (clazz == Short.TYPE) {
            return Type.SHORT_TYPE;
        }
        if (clazz == Double.TYPE) {
            return Type.DOUBLE_TYPE;
        }
        if (clazz == Float.TYPE) {
            return Type.FLOAT_TYPE;
        }
        return Type.LONG_TYPE;
    }
    
    public static Type getType(final String s) {
        return getType(s.toCharArray(), 0);
    }
    
    public static Type getType(final Constructor<?> constructor) {
        return getType(getConstructorDescriptor(constructor));
    }
    
    public static Type getType(final Method method) {
        return getType(getMethodDescriptor(method));
    }
    
    private static Type getType(final char[] array, final int n) {
        final char c = array[n];
        final int n2 = 1;
        int n3 = 1;
        switch (c) {
            default: {
                return new Type(11, array, n, array.length - n);
            }
            case 91: {
                while (array[n + n3] == '[') {
                    ++n3;
                }
                int n4 = n3;
                if (array[n + n3] == 'L') {
                    int n5 = n3 + 1;
                    while (true) {
                        n4 = n5;
                        if (array[n + n5] == ';') {
                            break;
                        }
                        ++n5;
                    }
                }
                return new Type(9, array, n, n4 + 1);
            }
            case 90: {
                return Type.BOOLEAN_TYPE;
            }
            case 86: {
                return Type.VOID_TYPE;
            }
            case 83: {
                return Type.SHORT_TYPE;
            }
            case 76: {
                int n6;
                for (n6 = n2; array[n + n6] != ';'; ++n6) {}
                return new Type(10, array, n + 1, n6 - 1);
            }
            case 74: {
                return Type.LONG_TYPE;
            }
            case 73: {
                return Type.INT_TYPE;
            }
            case 70: {
                return Type.FLOAT_TYPE;
            }
            case 68: {
                return Type.DOUBLE_TYPE;
            }
            case 67: {
                return Type.CHAR_TYPE;
            }
            case 66: {
                return Type.BYTE_TYPE;
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Type)) {
            return false;
        }
        final Type type = (Type)o;
        if (this.sort != type.sort) {
            return false;
        }
        if (this.sort >= 9) {
            if (this.len != type.len) {
                return false;
            }
            for (int off = this.off, off2 = type.off, len = this.len, i = off; i < len + off; ++i, ++off2) {
                if (this.buf[i] != type.buf[off2]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Type[] getArgumentTypes() {
        return getArgumentTypes(this.getDescriptor());
    }
    
    public int getArgumentsAndReturnSizes() {
        return getArgumentsAndReturnSizes(this.getDescriptor());
    }
    
    public String getClassName() {
        switch (this.sort) {
            default: {
                return null;
            }
            case 10: {
                return new String(this.buf, this.off, this.len).replace('/', '.');
            }
            case 9: {
                final StringBuilder sb = new StringBuilder(this.getElementType().getClassName());
                for (int i = this.getDimensions(); i > 0; --i) {
                    sb.append("[]");
                }
                return sb.toString();
            }
            case 8: {
                return "double";
            }
            case 7: {
                return "long";
            }
            case 6: {
                return "float";
            }
            case 5: {
                return "int";
            }
            case 4: {
                return "short";
            }
            case 3: {
                return "byte";
            }
            case 2: {
                return "char";
            }
            case 1: {
                return "boolean";
            }
            case 0: {
                return "void";
            }
        }
    }
    
    public String getDescriptor() {
        final StringBuilder sb = new StringBuilder();
        this.getDescriptor(sb);
        return sb.toString();
    }
    
    public int getDimensions() {
        int n;
        for (n = 1; this.buf[this.off + n] == '['; ++n) {}
        return n;
    }
    
    public Type getElementType() {
        return getType(this.buf, this.off + this.getDimensions());
    }
    
    public String getInternalName() {
        return new String(this.buf, this.off, this.len);
    }
    
    public int getOpcode(final int n) {
        int n2 = 4;
        if (n != 46 && n != 79) {
            if (this.buf == null) {
                n2 = (this.off & 0xFF0000) >> 16;
            }
            return n2 + n;
        }
        if (this.buf == null) {
            n2 = (this.off & 0xFF00) >> 8;
        }
        return n2 + n;
    }
    
    public Type getReturnType() {
        return getReturnType(this.getDescriptor());
    }
    
    public int getSize() {
        if (this.buf == null) {
            return this.off & 0xFF;
        }
        return 1;
    }
    
    public int getSort() {
        return this.sort;
    }
    
    @Override
    public int hashCode() {
        int n2;
        int n = n2 = this.sort * 13;
        if (this.sort >= 9) {
            final int off = this.off;
            final int len = this.len;
            int n3 = off;
            while (true) {
                final int n4 = n3;
                n2 = n;
                if (n4 >= len + off) {
                    break;
                }
                n = (this.buf[n4] + n) * 17;
                n3 = n4 + 1;
            }
        }
        return n2;
    }
    
    @Override
    public String toString() {
        return this.getDescriptor();
    }
}
