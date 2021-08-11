// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.reflect;

import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;

public class TypeToken<T>
{
    final int hashCode;
    final Class<? super T> rawType;
    final Type type;
    
    protected TypeToken() {
        this.type = getSuperclassTypeParameter(this.getClass());
        this.rawType = (Class<? super T>)$Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }
    
    TypeToken(final Type type) {
        this.type = $Gson$Types.canonicalize($Gson$Preconditions.checkNotNull(type));
        this.rawType = (Class<? super T>)$Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }
    
    private static AssertionError buildUnexpectedTypeError(final Type type, final Class<?>... array) {
        final StringBuilder sb = new StringBuilder("Unexpected type. Expected one of: ");
        for (int length = array.length, i = 0; i < length; ++i) {
            sb.append(array[i].getName()).append(", ");
        }
        sb.append("but got: ").append(type.getClass().getName()).append(", for type token: ").append(type.toString()).append('.');
        return new AssertionError((Object)sb.toString());
    }
    
    public static <T> TypeToken<T> get(final Class<T> clazz) {
        return new TypeToken<T>(clazz);
    }
    
    public static TypeToken<?> get(final Type type) {
        return new TypeToken<Object>(type);
    }
    
    public static TypeToken<?> getArray(final Type type) {
        return new TypeToken<Object>($Gson$Types.arrayOf(type));
    }
    
    public static TypeToken<?> getParameterized(final Type type, final Type... array) {
        return new TypeToken<Object>($Gson$Types.newParameterizedTypeWithOwner(null, type, array));
    }
    
    static Type getSuperclassTypeParameter(final Class<?> clazz) {
        final Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        return $Gson$Types.canonicalize(((ParameterizedType)genericSuperclass).getActualTypeArguments()[0]);
    }
    
    private static boolean isAssignableFrom(final Type type, final GenericArrayType genericArrayType) {
        final Type genericComponentType = genericArrayType.getGenericComponentType();
        if (genericComponentType instanceof ParameterizedType) {
            Type genericComponentType2 = type;
            if (type instanceof GenericArrayType) {
                genericComponentType2 = ((GenericArrayType)type).getGenericComponentType();
            }
            else if (type instanceof Class) {
                Class componentType;
                for (componentType = (Class)type; componentType.isArray(); componentType = componentType.getComponentType()) {}
                genericComponentType2 = componentType;
            }
            return isAssignableFrom(genericComponentType2, (ParameterizedType)genericComponentType, new HashMap<String, Type>());
        }
        return true;
    }
    
    private static boolean isAssignableFrom(Type type, final ParameterizedType parameterizedType, final Map<String, Type> map) {
        if (type == null) {
            return false;
        }
        if (parameterizedType.equals(type)) {
            return true;
        }
        final Class<?> rawType = $Gson$Types.getRawType(type);
        ParameterizedType parameterizedType2 = null;
        if (type instanceof ParameterizedType) {
            parameterizedType2 = (ParameterizedType)type;
        }
        if (parameterizedType2 != null) {
            final Type[] actualTypeArguments = parameterizedType2.getActualTypeArguments();
            final TypeVariable<Class<?>>[] typeParameters = rawType.getTypeParameters();
            for (int i = 0; i < actualTypeArguments.length; ++i) {
                type = actualTypeArguments[i];
                final TypeVariable<Class<?>> typeVariable = typeParameters[i];
                while (type instanceof TypeVariable) {
                    type = map.get(((TypeVariable)type).getName());
                }
                map.put(typeVariable.getName(), type);
            }
            if (typeEquals(parameterizedType2, parameterizedType, map)) {
                return true;
            }
        }
        final Type[] genericInterfaces = rawType.getGenericInterfaces();
        for (int length = genericInterfaces.length, j = 0; j < length; ++j) {
            if (isAssignableFrom(genericInterfaces[j], parameterizedType, new HashMap<String, Type>(map))) {
                return true;
            }
        }
        return isAssignableFrom(rawType.getGenericSuperclass(), parameterizedType, new HashMap<String, Type>(map));
    }
    
    private static boolean matches(final Type type, final Type type2, final Map<String, Type> map) {
        return type2.equals(type) || (type instanceof TypeVariable && type2.equals(map.get(((TypeVariable)type).getName())));
    }
    
    private static boolean typeEquals(final ParameterizedType parameterizedType, final ParameterizedType parameterizedType2, final Map<String, Type> map) {
        if (parameterizedType.getRawType().equals(parameterizedType2.getRawType())) {
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            final Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
            for (int i = 0; i < actualTypeArguments.length; ++i) {
                if (!matches(actualTypeArguments[i], actualTypeArguments2[i], map)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)o).type);
    }
    
    public final Class<? super T> getRawType() {
        return this.rawType;
    }
    
    public final Type getType() {
        return this.type;
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    @Deprecated
    public boolean isAssignableFrom(final TypeToken<?> typeToken) {
        return this.isAssignableFrom(typeToken.getType());
    }
    
    @Deprecated
    public boolean isAssignableFrom(final Class<?> clazz) {
        return this.isAssignableFrom((Type)clazz);
    }
    
    @Deprecated
    public boolean isAssignableFrom(final Type type) {
        if (type == null) {
            return false;
        }
        if (this.type.equals(type)) {
            return true;
        }
        if (this.type instanceof Class) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(type));
        }
        if (this.type instanceof ParameterizedType) {
            return isAssignableFrom(type, (ParameterizedType)this.type, new HashMap<String, Type>());
        }
        if (this.type instanceof GenericArrayType) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(type)) && isAssignableFrom(type, (GenericArrayType)this.type);
        }
        throw buildUnexpectedTypeError(this.type, Class.class, ParameterizedType.class, GenericArrayType.class);
    }
    
    @Override
    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }
}
