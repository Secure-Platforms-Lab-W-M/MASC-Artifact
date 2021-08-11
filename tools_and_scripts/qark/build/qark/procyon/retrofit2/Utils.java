// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import java.lang.annotation.Annotation;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.util.Arrays;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import javax.annotation.Nullable;
import java.io.IOException;
import okio.BufferedSource;
import okio.Sink;
import okio.Buffer;
import okhttp3.ResponseBody;
import java.lang.reflect.Type;

final class Utils
{
    static final Type[] EMPTY_TYPE_ARRAY;
    
    static {
        EMPTY_TYPE_ARRAY = new Type[0];
    }
    
    private Utils() {
    }
    
    static ResponseBody buffer(final ResponseBody responseBody) throws IOException {
        final Buffer buffer = new Buffer();
        responseBody.source().readAll(buffer);
        return ResponseBody.create(responseBody.contentType(), responseBody.contentLength(), buffer);
    }
    
    static <T> T checkNotNull(@Nullable final T t, final String s) {
        if (t == null) {
            throw new NullPointerException(s);
        }
        return t;
    }
    
    static void checkNotPrimitive(final Type type) {
        if (type instanceof Class && ((Class)type).isPrimitive()) {
            throw new IllegalArgumentException();
        }
    }
    
    private static Class<?> declaringClassOf(final TypeVariable<?> typeVariable) {
        final Object genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            return (Class<?>)genericDeclaration;
        }
        return null;
    }
    
    static boolean equals(final Type type, final Type type2) {
        final boolean b = true;
        final boolean b2 = true;
        final boolean b3 = true;
        final boolean b4 = false;
        boolean b5;
        if (type == type2) {
            b5 = true;
        }
        else {
            if (type instanceof Class) {
                return type.equals(type2);
            }
            if (type instanceof ParameterizedType) {
                b5 = b4;
                if (type2 instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType)type;
                    final ParameterizedType parameterizedType2 = (ParameterizedType)type2;
                    final Type ownerType = parameterizedType.getOwnerType();
                    final Type ownerType2 = parameterizedType2.getOwnerType();
                    return (ownerType == ownerType2 || (ownerType != null && ownerType.equals(ownerType2))) && parameterizedType.getRawType().equals(parameterizedType2.getRawType()) && Arrays.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments()) && b3;
                }
            }
            else if (type instanceof GenericArrayType) {
                b5 = b4;
                if (type2 instanceof GenericArrayType) {
                    return equals(((GenericArrayType)type).getGenericComponentType(), ((GenericArrayType)type2).getGenericComponentType());
                }
            }
            else if (type instanceof WildcardType) {
                b5 = b4;
                if (type2 instanceof WildcardType) {
                    final WildcardType wildcardType = (WildcardType)type;
                    final WildcardType wildcardType2 = (WildcardType)type2;
                    return Arrays.equals(wildcardType.getUpperBounds(), wildcardType2.getUpperBounds()) && Arrays.equals(wildcardType.getLowerBounds(), wildcardType2.getLowerBounds()) && b;
                }
            }
            else {
                b5 = b4;
                if (type instanceof TypeVariable) {
                    b5 = b4;
                    if (type2 instanceof TypeVariable) {
                        final TypeVariable typeVariable = (TypeVariable)type;
                        final TypeVariable typeVariable2 = (TypeVariable)type2;
                        return typeVariable.getGenericDeclaration() == typeVariable2.getGenericDeclaration() && typeVariable.getName().equals(typeVariable2.getName()) && b2;
                    }
                }
            }
        }
        return b5;
    }
    
    static Type getCallResponseType(final Type type) {
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        return getParameterUpperBound(0, (ParameterizedType)type);
    }
    
    static Type getGenericSupertype(final Type type, Class<?> clazz, final Class<?> clazz2) {
        if (clazz2 == clazz) {
            return type;
        }
        if (clazz2.isInterface()) {
            final Class<?>[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                if (interfaces[i] == clazz2) {
                    return clazz.getGenericInterfaces()[i];
                }
                if (clazz2.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(clazz.getGenericInterfaces()[i], interfaces[i], clazz2);
                }
            }
        }
        if (!clazz.isInterface()) {
            while (clazz != Object.class) {
                final Class<? super Object> superclass = clazz.getSuperclass();
                if (superclass == clazz2) {
                    return clazz.getGenericSuperclass();
                }
                if (clazz2.isAssignableFrom(superclass)) {
                    return getGenericSupertype(clazz.getGenericSuperclass(), superclass, clazz2);
                }
                clazz = (Class<Object>)superclass;
            }
        }
        return clazz2;
    }
    
    static Type getParameterUpperBound(final int n, final ParameterizedType parameterizedType) {
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (n < 0 || n >= actualTypeArguments.length) {
            throw new IllegalArgumentException("Index " + n + " not in range [0," + actualTypeArguments.length + ") for " + parameterizedType);
        }
        Type type2;
        final Type type = type2 = actualTypeArguments[n];
        if (type instanceof WildcardType) {
            type2 = ((WildcardType)type).getUpperBounds()[0];
        }
        return type2;
    }
    
    static Class<?> getRawType(Type rawType) {
        checkNotNull(rawType, "type == null");
        if (rawType instanceof Class) {
            return (Class<?>)rawType;
        }
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType)rawType).getRawType();
            if (!(rawType instanceof Class)) {
                throw new IllegalArgumentException();
            }
            return (Class<?>)rawType;
        }
        else {
            if (rawType instanceof GenericArrayType) {
                return Array.newInstance(getRawType(((GenericArrayType)rawType).getGenericComponentType()), 0).getClass();
            }
            if (rawType instanceof TypeVariable) {
                return Object.class;
            }
            if (rawType instanceof WildcardType) {
                return getRawType(((WildcardType)rawType).getUpperBounds()[0]);
            }
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + rawType + "> is of type " + rawType.getClass().getName());
        }
    }
    
    static Type getSupertype(final Type type, final Class<?> clazz, final Class<?> clazz2) {
        if (!clazz2.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException();
        }
        return resolve(type, clazz, getGenericSupertype(type, clazz, clazz2));
    }
    
    static boolean hasUnresolvableType(@Nullable final Type type) {
        if (!(type instanceof Class)) {
            if (type instanceof ParameterizedType) {
                final Type[] actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
                for (int length = actualTypeArguments.length, i = 0; i < length; ++i) {
                    if (hasUnresolvableType(actualTypeArguments[i])) {
                        return true;
                    }
                }
            }
            else {
                if (type instanceof GenericArrayType) {
                    return hasUnresolvableType(((GenericArrayType)type).getGenericComponentType());
                }
                if (type instanceof TypeVariable) {
                    return true;
                }
                if (type instanceof WildcardType) {
                    return true;
                }
                String name;
                if (type == null) {
                    name = "null";
                }
                else {
                    name = type.getClass().getName();
                }
                throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + name);
            }
        }
        return false;
    }
    
    private static int indexOf(final Object[] array, final Object o) {
        for (int i = 0; i < array.length; ++i) {
            if (o.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }
    
    static boolean isAnnotationPresent(final Annotation[] array, final Class<? extends Annotation> clazz) {
        final boolean b = false;
        final int length = array.length;
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= length) {
                break;
            }
            if (clazz.isInstance(array[n])) {
                b2 = true;
                break;
            }
            ++n;
        }
        return b2;
    }
    
    static Type resolve(Type type, final Class<?> clazz, Type ownerType) {
        while (ownerType instanceof TypeVariable) {
            final TypeVariable typeVariable = (TypeVariable)ownerType;
            final Type resolveTypeVariable = resolveTypeVariable(type, clazz, typeVariable);
            if ((ownerType = resolveTypeVariable) == typeVariable) {
                ownerType = resolveTypeVariable;
                return ownerType;
            }
        }
        if (ownerType instanceof Class && ((Class)ownerType).isArray()) {
            final Class clazz2 = (Class)ownerType;
            final Class componentType = clazz2.getComponentType();
            type = resolve(type, clazz, componentType);
            Type type2;
            if (componentType == type) {
                type2 = clazz2;
            }
            else {
                type2 = new GenericArrayTypeImpl(type);
            }
            return type2;
        }
        if (ownerType instanceof GenericArrayType) {
            ownerType = ownerType;
            final Type genericComponentType = ((GenericArrayType)ownerType).getGenericComponentType();
            type = resolve(type, clazz, genericComponentType);
            if (genericComponentType != type) {
                return new GenericArrayTypeImpl(type);
            }
            return ownerType;
        }
        else if (ownerType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)ownerType;
            ownerType = parameterizedType.getOwnerType();
            final Type resolve = resolve(type, clazz, ownerType);
            int n;
            if (resolve != ownerType) {
                n = 1;
            }
            else {
                n = 0;
            }
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type[] array;
            int n2;
            for (int i = 0; i < actualTypeArguments.length; ++i, actualTypeArguments = array, n = n2) {
                final Type resolve2 = resolve(type, clazz, actualTypeArguments[i]);
                array = actualTypeArguments;
                n2 = n;
                if (resolve2 != actualTypeArguments[i]) {
                    array = actualTypeArguments;
                    if ((n2 = n) == 0) {
                        array = actualTypeArguments.clone();
                        n2 = 1;
                    }
                    array[i] = resolve2;
                }
            }
            ownerType = parameterizedType;
            if (n != 0) {
                return new ParameterizedTypeImpl(resolve, parameterizedType.getRawType(), actualTypeArguments);
            }
            return ownerType;
        }
        else {
            if (!(ownerType instanceof WildcardType)) {
                return ownerType;
            }
            final WildcardType wildcardType = (WildcardType)ownerType;
            final Type[] lowerBounds = wildcardType.getLowerBounds();
            final Type[] upperBounds = wildcardType.getUpperBounds();
            if (lowerBounds.length == 1) {
                type = resolve(type, clazz, lowerBounds[0]);
                ownerType = wildcardType;
                if (type != lowerBounds[0]) {
                    return new WildcardTypeImpl(new Type[] { Object.class }, new Type[] { type });
                }
                return ownerType;
            }
            else {
                ownerType = wildcardType;
                if (upperBounds.length != 1) {
                    return ownerType;
                }
                type = resolve(type, clazz, upperBounds[0]);
                ownerType = wildcardType;
                if (type != upperBounds[0]) {
                    return new WildcardTypeImpl(new Type[] { type }, Utils.EMPTY_TYPE_ARRAY);
                }
                return ownerType;
            }
        }
    }
    
    private static Type resolveTypeVariable(Type genericSupertype, final Class<?> clazz, final TypeVariable<?> typeVariable) {
        final Class<?> declaringClass = declaringClassOf(typeVariable);
        if (declaringClass != null) {
            genericSupertype = getGenericSupertype(genericSupertype, clazz, declaringClass);
            if (genericSupertype instanceof ParameterizedType) {
                return ((ParameterizedType)genericSupertype).getActualTypeArguments()[indexOf(declaringClass.getTypeParameters(), typeVariable)];
            }
        }
        return typeVariable;
    }
    
    static void throwIfFatal(final Throwable t) {
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError)t;
        }
        if (t instanceof ThreadDeath) {
            throw (ThreadDeath)t;
        }
        if (t instanceof LinkageError) {
            throw (LinkageError)t;
        }
    }
    
    static String typeToString(final Type type) {
        if (type instanceof Class) {
            return ((Class)type).getName();
        }
        return type.toString();
    }
    
    static <T> void validateServiceInterface(final Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        if (clazz.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }
    
    private static final class GenericArrayTypeImpl implements GenericArrayType
    {
        private final Type componentType;
        
        GenericArrayTypeImpl(final Type componentType) {
            this.componentType = componentType;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof GenericArrayType && Utils.equals(this, (Type)o);
        }
        
        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }
        
        @Override
        public int hashCode() {
            return this.componentType.hashCode();
        }
        
        @Override
        public String toString() {
            return Utils.typeToString(this.componentType) + "[]";
        }
    }
    
    private static final class ParameterizedTypeImpl implements ParameterizedType
    {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;
        
        ParameterizedTypeImpl(@Nullable final Type ownerType, final Type rawType, final Type... array) {
            int n = 1;
            final int n2 = 0;
            if (rawType instanceof Class) {
                int n3;
                if (ownerType == null) {
                    n3 = 1;
                }
                else {
                    n3 = 0;
                }
                if (((Class)rawType).getEnclosingClass() != null) {
                    n = 0;
                }
                if (n3 != n) {
                    throw new IllegalArgumentException();
                }
            }
            for (int length = array.length, i = n2; i < length; ++i) {
                final Type type = array[i];
                Utils.checkNotNull(type, "typeArgument == null");
                Utils.checkNotPrimitive(type);
            }
            this.ownerType = ownerType;
            this.rawType = rawType;
            this.typeArguments = array.clone();
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof ParameterizedType && Utils.equals(this, (Type)o);
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return this.typeArguments.clone();
        }
        
        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }
        
        @Override
        public Type getRawType() {
            return this.rawType;
        }
        
        @Override
        public int hashCode() {
            final int hashCode = Arrays.hashCode(this.typeArguments);
            final int hashCode2 = this.rawType.hashCode();
            int hashCode3;
            if (this.ownerType != null) {
                hashCode3 = this.ownerType.hashCode();
            }
            else {
                hashCode3 = 0;
            }
            return hashCode3 ^ (hashCode2 ^ hashCode);
        }
        
        @Override
        public String toString() {
            if (this.typeArguments.length == 0) {
                return Utils.typeToString(this.rawType);
            }
            final StringBuilder sb = new StringBuilder((this.typeArguments.length + 1) * 30);
            sb.append(Utils.typeToString(this.rawType));
            sb.append("<").append(Utils.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; ++i) {
                sb.append(", ").append(Utils.typeToString(this.typeArguments[i]));
            }
            return sb.append(">").toString();
        }
    }
    
    private static final class WildcardTypeImpl implements WildcardType
    {
        private final Type lowerBound;
        private final Type upperBound;
        
        WildcardTypeImpl(final Type[] array, final Type[] array2) {
            if (array2.length > 1) {
                throw new IllegalArgumentException();
            }
            if (array.length != 1) {
                throw new IllegalArgumentException();
            }
            if (array2.length == 1) {
                if (array2[0] == null) {
                    throw new NullPointerException();
                }
                Utils.checkNotPrimitive(array2[0]);
                if (array[0] != Object.class) {
                    throw new IllegalArgumentException();
                }
                this.lowerBound = array2[0];
                this.upperBound = Object.class;
            }
            else {
                if (array[0] == null) {
                    throw new NullPointerException();
                }
                Utils.checkNotPrimitive(array[0]);
                this.lowerBound = null;
                this.upperBound = array[0];
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof WildcardType && Utils.equals(this, (Type)o);
        }
        
        @Override
        public Type[] getLowerBounds() {
            if (this.lowerBound != null) {
                return new Type[] { this.lowerBound };
            }
            return Utils.EMPTY_TYPE_ARRAY;
        }
        
        @Override
        public Type[] getUpperBounds() {
            return new Type[] { this.upperBound };
        }
        
        @Override
        public int hashCode() {
            int n;
            if (this.lowerBound != null) {
                n = this.lowerBound.hashCode() + 31;
            }
            else {
                n = 1;
            }
            return n ^ this.upperBound.hashCode() + 31;
        }
        
        @Override
        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + Utils.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + Utils.typeToString(this.upperBound);
        }
    }
}
