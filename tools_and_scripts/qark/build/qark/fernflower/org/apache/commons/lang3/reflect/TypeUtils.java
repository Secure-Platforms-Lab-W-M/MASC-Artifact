package org.apache.commons.lang3.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;

public class TypeUtils {
   public static final WildcardType WILDCARD_ALL = wildcardType().withUpperBounds(Object.class).build();

   private static StringBuilder appendAllTo(StringBuilder var0, String var1, Object... var2) {
      Validate.notEmpty(Validate.noNullElements(var2));
      if (var2.length > 0) {
         var0.append(toString(var2[0]));

         for(int var3 = 1; var3 < var2.length; ++var3) {
            var0.append(var1);
            var0.append(toString(var2[var3]));
         }
      }

      return var0;
   }

   private static void appendRecursiveTypes(StringBuilder var0, int[] var1, Type[] var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         var0.append('<');
         appendAllTo(var0, ", ", var2[var3].toString()).append('>');
      }

      Type[] var4 = (Type[])ArrayUtils.removeAll((Object[])var2, (int[])var1);
      if (var4.length > 0) {
         var0.append('<');
         appendAllTo(var0, ", ", var4).append('>');
      }

   }

   private static String classToString(Class var0) {
      StringBuilder var1;
      if (var0.isArray()) {
         var1 = new StringBuilder();
         var1.append(toString((Type)var0.getComponentType()));
         var1.append("[]");
         return var1.toString();
      } else {
         var1 = new StringBuilder();
         if (var0.getEnclosingClass() != null) {
            var1.append(classToString(var0.getEnclosingClass()));
            var1.append('.');
            var1.append(var0.getSimpleName());
         } else {
            var1.append(var0.getName());
         }

         if (var0.getTypeParameters().length > 0) {
            var1.append('<');
            appendAllTo(var1, ", ", var0.getTypeParameters());
            var1.append('>');
         }

         return var1.toString();
      }
   }

   public static boolean containsTypeVariables(Type var0) {
      if (var0 instanceof TypeVariable) {
         return true;
      } else if (var0 instanceof Class) {
         return ((Class)var0).getTypeParameters().length > 0;
      } else if (var0 instanceof ParameterizedType) {
         Type[] var4 = ((ParameterizedType)var0).getActualTypeArguments();
         int var2 = var4.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (containsTypeVariables(var4[var1])) {
               return true;
            }
         }

         return false;
      } else if (var0 instanceof WildcardType) {
         WildcardType var3 = (WildcardType)var0;
         return containsTypeVariables(getImplicitLowerBounds(var3)[0]) || containsTypeVariables(getImplicitUpperBounds(var3)[0]);
      } else {
         return false;
      }
   }

   private static boolean containsVariableTypeSameParametrizedTypeBound(TypeVariable var0, ParameterizedType var1) {
      return ArrayUtils.contains(var0.getBounds(), var1);
   }

   public static Map determineTypeArguments(Class var0, ParameterizedType var1) {
      Validate.notNull(var0, "cls is null");
      Validate.notNull(var1, "superType is null");
      Class var2 = getRawType(var1);
      if (!isAssignable(var0, (Class)var2)) {
         return null;
      } else if (var0.equals(var2)) {
         return getTypeArguments((ParameterizedType)var1, var2, (Map)null);
      } else {
         Type var4 = getClosestParentType(var0, var2);
         if (var4 instanceof Class) {
            return determineTypeArguments((Class)var4, var1);
         } else {
            ParameterizedType var5 = (ParameterizedType)var4;
            Map var3 = determineTypeArguments(getRawType(var5), var1);
            mapTypeVariablesToArguments(var0, var5, var3);
            return var3;
         }
      }
   }

   private static boolean equals(GenericArrayType var0, Type var1) {
      return var1 instanceof GenericArrayType && equals(var0.getGenericComponentType(), ((GenericArrayType)var1).getGenericComponentType());
   }

   private static boolean equals(ParameterizedType var0, Type var1) {
      if (var1 instanceof ParameterizedType) {
         ParameterizedType var2 = (ParameterizedType)var1;
         if (equals(var0.getRawType(), var2.getRawType()) && equals(var0.getOwnerType(), var2.getOwnerType())) {
            return equals(var0.getActualTypeArguments(), var2.getActualTypeArguments());
         }
      }

      return false;
   }

   public static boolean equals(Type var0, Type var1) {
      if (Objects.equals(var0, var1)) {
         return true;
      } else if (var0 instanceof ParameterizedType) {
         return equals((ParameterizedType)var0, var1);
      } else if (var0 instanceof GenericArrayType) {
         return equals((GenericArrayType)var0, var1);
      } else {
         return var0 instanceof WildcardType ? equals((WildcardType)var0, var1) : false;
      }
   }

   private static boolean equals(WildcardType var0, Type var1) {
      if (var1 instanceof WildcardType) {
         WildcardType var2 = (WildcardType)var1;
         return equals(getImplicitLowerBounds(var0), getImplicitLowerBounds(var2)) && equals(getImplicitUpperBounds(var0), getImplicitUpperBounds(var2));
      } else {
         return false;
      }
   }

   private static boolean equals(Type[] var0, Type[] var1) {
      if (var0.length == var1.length) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (!equals(var0[var2], var1[var2])) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static Type[] extractTypeArgumentsFrom(Map var0, TypeVariable[] var1) {
      Type[] var5 = new Type[var1.length];
      int var2 = 0;
      int var4 = var1.length;

      for(int var3 = 0; var3 < var4; ++var2) {
         TypeVariable var6 = var1[var3];
         Validate.isTrue(var0.containsKey(var6), "missing argument mapping for %s", toString((Type)var6));
         var5[var2] = (Type)var0.get(var6);
         ++var3;
      }

      return var5;
   }

   private static int[] findRecursiveTypes(ParameterizedType var0) {
      Type[] var4 = (Type[])Arrays.copyOf(var0.getActualTypeArguments(), var0.getActualTypeArguments().length);
      int[] var2 = new int[0];

      int[] var3;
      for(int var1 = 0; var1 < var4.length; var2 = var3) {
         var3 = var2;
         if (var4[var1] instanceof TypeVariable) {
            var3 = var2;
            if (containsVariableTypeSameParametrizedTypeBound((TypeVariable)var4[var1], var0)) {
               var3 = ArrayUtils.add(var2, var1);
            }
         }

         ++var1;
      }

      return var2;
   }

   public static GenericArrayType genericArrayType(Type var0) {
      return new TypeUtils.GenericArrayTypeImpl((Type)Validate.notNull(var0, "componentType is null"));
   }

   private static String genericArrayTypeToString(GenericArrayType var0) {
      return String.format("%s[]", toString(var0.getGenericComponentType()));
   }

   public static Type getArrayComponentType(Type var0) {
      boolean var1 = var0 instanceof Class;
      Object var2 = null;
      if (var1) {
         Class var3 = (Class)var0;
         Class var4 = (Class)var2;
         if (var3.isArray()) {
            var4 = var3.getComponentType();
         }

         return var4;
      } else {
         return var0 instanceof GenericArrayType ? ((GenericArrayType)var0).getGenericComponentType() : null;
      }
   }

   private static Type getClosestParentType(Class var0, Class var1) {
      if (var1.isInterface()) {
         Type[] var8 = var0.getGenericInterfaces();
         Type var4 = null;
         int var3 = var8.length;

         Type var6;
         for(int var2 = 0; var2 < var3; var4 = var6) {
            Type var7 = var8[var2];
            Class var5;
            if (var7 instanceof ParameterizedType) {
               var5 = getRawType((ParameterizedType)var7);
            } else {
               if (!(var7 instanceof Class)) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Unexpected generic interface type found: ");
                  var9.append(var7);
                  throw new IllegalStateException(var9.toString());
               }

               var5 = (Class)var7;
            }

            var6 = var4;
            if (isAssignable(var5, (Class)var1)) {
               var6 = var4;
               if (isAssignable(var4, (Type)var5)) {
                  var6 = var7;
               }
            }

            ++var2;
         }

         if (var4 != null) {
            return var4;
         }
      }

      return var0.getGenericSuperclass();
   }

   public static Type[] getImplicitBounds(TypeVariable var0) {
      Validate.notNull(var0, "typeVariable is null");
      Type[] var1 = var0.getBounds();
      return var1.length == 0 ? new Type[]{Object.class} : normalizeUpperBounds(var1);
   }

   public static Type[] getImplicitLowerBounds(WildcardType var0) {
      Validate.notNull(var0, "wildcardType is null");
      Type[] var1 = var0.getLowerBounds();
      return var1.length == 0 ? new Type[]{null} : var1;
   }

   public static Type[] getImplicitUpperBounds(WildcardType var0) {
      Validate.notNull(var0, "wildcardType is null");
      Type[] var1 = var0.getUpperBounds();
      return var1.length == 0 ? new Type[]{Object.class} : normalizeUpperBounds(var1);
   }

   private static Class getRawType(ParameterizedType var0) {
      Type var2 = var0.getRawType();
      if (var2 instanceof Class) {
         return (Class)var2;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Wait... What!? Type of rawType: ");
         var1.append(var2);
         throw new IllegalStateException(var1.toString());
      }
   }

   public static Class getRawType(Type var0, Type var1) {
      if (var0 instanceof Class) {
         return (Class)var0;
      } else if (var0 instanceof ParameterizedType) {
         return getRawType((ParameterizedType)var0);
      } else if (var0 instanceof TypeVariable) {
         if (var1 == null) {
            return null;
         } else {
            GenericDeclaration var2 = ((TypeVariable)var0).getGenericDeclaration();
            if (!(var2 instanceof Class)) {
               return null;
            } else {
               Map var4 = getTypeArguments(var1, (Class)var2);
               if (var4 == null) {
                  return null;
               } else {
                  var0 = (Type)var4.get(var0);
                  return var0 == null ? null : getRawType(var0, var1);
               }
            }
         }
      } else if (var0 instanceof GenericArrayType) {
         return Array.newInstance(getRawType(((GenericArrayType)var0).getGenericComponentType(), var1), 0).getClass();
      } else if (var0 instanceof WildcardType) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("unknown type: ");
         var3.append(var0);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   private static Map getTypeArguments(Class var0, Class var1, Map var2) {
      if (!isAssignable(var0, (Class)var1)) {
         return null;
      } else {
         Class var3 = var0;
         if (var0.isPrimitive()) {
            if (var1.isPrimitive()) {
               return new HashMap();
            }

            var3 = ClassUtils.primitiveToWrapper(var0);
         }

         HashMap var4;
         if (var2 == null) {
            var4 = new HashMap();
         } else {
            var4 = new HashMap(var2);
         }

         return (Map)(var1.equals(var3) ? var4 : getTypeArguments((Type)getClosestParentType(var3, var1), var1, var4));
      }
   }

   public static Map getTypeArguments(ParameterizedType var0) {
      return getTypeArguments((ParameterizedType)var0, getRawType(var0), (Map)null);
   }

   private static Map getTypeArguments(ParameterizedType var0, Class var1, Map var2) {
      Class var4 = getRawType(var0);
      if (!isAssignable(var4, (Class)var1)) {
         return null;
      } else {
         Type var5 = var0.getOwnerType();
         Object var9;
         if (var5 instanceof ParameterizedType) {
            ParameterizedType var10 = (ParameterizedType)var5;
            var9 = getTypeArguments(var10, getRawType(var10), var2);
         } else if (var2 == null) {
            var9 = new HashMap();
         } else {
            var9 = new HashMap(var2);
         }

         Type[] var11 = var0.getActualTypeArguments();
         TypeVariable[] var6 = var4.getTypeParameters();

         for(int var3 = 0; var3 < var6.length; ++var3) {
            Type var8 = var11[var3];
            TypeVariable var7 = var6[var3];
            if (((Map)var9).containsKey(var8)) {
               var8 = (Type)((Map)var9).get(var8);
            }

            ((Map)var9).put(var7, var8);
         }

         if (var1.equals(var4)) {
            return (Map)var9;
         } else {
            return getTypeArguments((Type)getClosestParentType(var4, var1), var1, (Map)var9);
         }
      }
   }

   public static Map getTypeArguments(Type var0, Class var1) {
      return getTypeArguments((Type)var0, var1, (Map)null);
   }

   private static Map getTypeArguments(Type var0, Class var1, Map var2) {
      if (var0 instanceof Class) {
         return getTypeArguments((Class)var0, var1, var2);
      } else if (var0 instanceof ParameterizedType) {
         return getTypeArguments((ParameterizedType)var0, var1, var2);
      } else if (var0 instanceof GenericArrayType) {
         var0 = ((GenericArrayType)var0).getGenericComponentType();
         if (var1.isArray()) {
            var1 = var1.getComponentType();
         }

         return getTypeArguments(var0, var1, var2);
      } else {
         boolean var6 = var0 instanceof WildcardType;
         byte var4 = 0;
         int var3 = 0;
         Type var7;
         Type[] var8;
         if (var6) {
            var8 = getImplicitUpperBounds((WildcardType)var0);

            for(int var10 = var8.length; var3 < var10; ++var3) {
               var7 = var8[var3];
               if (isAssignable(var7, var1)) {
                  return getTypeArguments(var7, var1, var2);
               }
            }

            return null;
         } else if (var0 instanceof TypeVariable) {
            var8 = getImplicitBounds((TypeVariable)var0);
            int var5 = var8.length;

            for(var3 = var4; var3 < var5; ++var3) {
               var7 = var8[var3];
               if (isAssignable(var7, var1)) {
                  return getTypeArguments(var7, var1, var2);
               }
            }

            return null;
         } else {
            StringBuilder var9 = new StringBuilder();
            var9.append("found an unhandled type: ");
            var9.append(var0);
            throw new IllegalStateException(var9.toString());
         }
      }
   }

   public static boolean isArrayType(Type var0) {
      return var0 instanceof GenericArrayType || var0 instanceof Class && ((Class)var0).isArray();
   }

   private static boolean isAssignable(Type var0, Class var1) {
      boolean var4 = true;
      if (var0 == null) {
         if (var1 != null) {
            if (!var1.isPrimitive()) {
               return true;
            }

            var4 = false;
         }

         return var4;
      } else if (var1 == null) {
         return false;
      } else if (var1.equals(var0)) {
         return true;
      } else if (var0 instanceof Class) {
         return ClassUtils.isAssignable((Class)var0, var1);
      } else if (var0 instanceof ParameterizedType) {
         return isAssignable(getRawType((ParameterizedType)var0), (Class)var1);
      } else if (var0 instanceof TypeVariable) {
         Type[] var5 = ((TypeVariable)var0).getBounds();
         int var3 = var5.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if (isAssignable(var5[var2], var1)) {
               return true;
            }
         }

         return false;
      } else if (!(var0 instanceof GenericArrayType)) {
         if (var0 instanceof WildcardType) {
            return false;
         } else {
            StringBuilder var6 = new StringBuilder();
            var6.append("found an unhandled type: ");
            var6.append(var0);
            throw new IllegalStateException(var6.toString());
         }
      } else {
         return var1.equals(Object.class) || var1.isArray() && isAssignable(((GenericArrayType)var0).getGenericComponentType(), var1.getComponentType());
      }
   }

   private static boolean isAssignable(Type var0, GenericArrayType var1, Map var2) {
      if (var0 == null) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1.equals(var0)) {
         return true;
      } else {
         Type var5 = var1.getGenericComponentType();
         if (var0 instanceof Class) {
            Class var7 = (Class)var0;
            return var7.isArray() && isAssignable(var7.getComponentType(), (Type)var5, var2);
         } else if (var0 instanceof GenericArrayType) {
            return isAssignable(((GenericArrayType)var0).getGenericComponentType(), var5, var2);
         } else {
            int var3;
            int var4;
            Type[] var6;
            if (var0 instanceof WildcardType) {
               var6 = getImplicitUpperBounds((WildcardType)var0);
               var4 = var6.length;

               for(var3 = 0; var3 < var4; ++var3) {
                  if (isAssignable(var6[var3], (Type)var1)) {
                     return true;
                  }
               }

               return false;
            } else if (var0 instanceof TypeVariable) {
               var6 = getImplicitBounds((TypeVariable)var0);
               var4 = var6.length;

               for(var3 = 0; var3 < var4; ++var3) {
                  if (isAssignable(var6[var3], (Type)var1)) {
                     return true;
                  }
               }

               return false;
            } else if (var0 instanceof ParameterizedType) {
               return false;
            } else {
               StringBuilder var8 = new StringBuilder();
               var8.append("found an unhandled type: ");
               var8.append(var0);
               throw new IllegalStateException(var8.toString());
            }
         }
      }
   }

   private static boolean isAssignable(Type var0, ParameterizedType var1, Map var2) {
      if (var0 == null) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1.equals(var0)) {
         return true;
      } else {
         Class var3 = getRawType(var1);
         Map var6 = getTypeArguments((Type)var0, var3, (Map)null);
         if (var6 == null) {
            return false;
         } else if (var6.isEmpty()) {
            return true;
         } else {
            Map var7 = getTypeArguments(var1, var3, var2);
            Iterator var8 = var7.keySet().iterator();

            Type var4;
            Type var9;
            do {
               do {
                  do {
                     do {
                        if (!var8.hasNext()) {
                           return true;
                        }

                        TypeVariable var5 = (TypeVariable)var8.next();
                        var4 = unrollVariableAssignments(var5, var7);
                        var9 = unrollVariableAssignments(var5, var6);
                     } while(var4 == null && var9 instanceof Class);
                  } while(var9 == null);
               } while(var4.equals(var9));
            } while(var4 instanceof WildcardType && isAssignable(var9, var4, var2));

            return false;
         }
      }
   }

   public static boolean isAssignable(Type var0, Type var1) {
      return isAssignable(var0, (Type)var1, (Map)null);
   }

   private static boolean isAssignable(Type var0, Type var1, Map var2) {
      if (var1 != null && !(var1 instanceof Class)) {
         if (var1 instanceof ParameterizedType) {
            return isAssignable(var0, (ParameterizedType)var1, var2);
         } else if (var1 instanceof GenericArrayType) {
            return isAssignable(var0, (GenericArrayType)var1, var2);
         } else if (var1 instanceof WildcardType) {
            return isAssignable(var0, (WildcardType)var1, var2);
         } else if (var1 instanceof TypeVariable) {
            return isAssignable(var0, (TypeVariable)var1, var2);
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("found an unhandled type: ");
            var3.append(var1);
            throw new IllegalStateException(var3.toString());
         }
      } else {
         return isAssignable(var0, (Class)var1);
      }
   }

   private static boolean isAssignable(Type var0, TypeVariable var1, Map var2) {
      if (var0 == null) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1.equals(var0)) {
         return true;
      } else {
         if (var0 instanceof TypeVariable) {
            Type[] var5 = getImplicitBounds((TypeVariable)var0);
            int var4 = var5.length;

            for(int var3 = 0; var3 < var4; ++var3) {
               if (isAssignable(var5[var3], var1, var2)) {
                  return true;
               }
            }
         }

         if (!(var0 instanceof Class) && !(var0 instanceof ParameterizedType) && !(var0 instanceof GenericArrayType)) {
            if (var0 instanceof WildcardType) {
               return false;
            } else {
               StringBuilder var6 = new StringBuilder();
               var6.append("found an unhandled type: ");
               var6.append(var0);
               throw new IllegalStateException(var6.toString());
            }
         } else {
            return false;
         }
      }
   }

   private static boolean isAssignable(Type var0, WildcardType var1, Map var2) {
      if (var0 == null) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1.equals(var0)) {
         return true;
      } else {
         Type[] var7 = getImplicitUpperBounds(var1);
         Type[] var11 = getImplicitLowerBounds(var1);
         int var3;
         int var4;
         if (!(var0 instanceof WildcardType)) {
            var4 = var7.length;

            for(var3 = 0; var3 < var4; ++var3) {
               if (!isAssignable(var0, substituteTypeVariables(var7[var3], var2), var2)) {
                  return false;
               }
            }

            var4 = var11.length;

            for(var3 = 0; var3 < var4; ++var3) {
               if (!isAssignable(substituteTypeVariables(var11[var3], var2), var0, var2)) {
                  return false;
               }
            }

            return true;
         } else {
            WildcardType var8 = (WildcardType)var0;
            Type[] var10 = getImplicitUpperBounds(var8);
            Type[] var12 = getImplicitLowerBounds(var8);
            int var5 = var7.length;

            int var6;
            for(var3 = 0; var3 < var5; ++var3) {
               Type var9 = substituteTypeVariables(var7[var3], var2);
               var6 = var10.length;

               for(var4 = 0; var4 < var6; ++var4) {
                  if (!isAssignable(var10[var4], var9, var2)) {
                     return false;
                  }
               }
            }

            var5 = var11.length;

            for(var3 = 0; var3 < var5; ++var3) {
               var0 = substituteTypeVariables(var11[var3], var2);
               var6 = var12.length;

               for(var4 = 0; var4 < var6; ++var4) {
                  if (!isAssignable(var0, var12[var4], var2)) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public static boolean isInstance(Object var0, Type var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         if (var0 == null) {
            if (!(var1 instanceof Class) || !((Class)var1).isPrimitive()) {
               return true;
            }
         } else {
            var2 = isAssignable(var0.getClass(), (Type)var1, (Map)null);
         }

         return var2;
      }
   }

   private static void mapTypeVariablesToArguments(Class var0, ParameterizedType var1, Map var2) {
      Type var4 = var1.getOwnerType();
      if (var4 instanceof ParameterizedType) {
         mapTypeVariablesToArguments(var0, (ParameterizedType)var4, var2);
      }

      Type[] var9 = var1.getActualTypeArguments();
      TypeVariable[] var8 = getRawType(var1).getTypeParameters();
      List var7 = Arrays.asList(var0.getTypeParameters());

      for(int var3 = 0; var3 < var9.length; ++var3) {
         TypeVariable var5 = var8[var3];
         Type var6 = var9[var3];
         if (var7.contains(var6) && var2.containsKey(var5)) {
            var2.put((TypeVariable)var6, (Type)var2.get(var5));
         }
      }

   }

   public static Type[] normalizeUpperBounds(Type[] var0) {
      Validate.notNull(var0, "null value specified for bounds array");
      if (var0.length < 2) {
         return var0;
      } else {
         HashSet var7 = new HashSet(var0.length);
         int var5 = var0.length;

         for(int var1 = 0; var1 < var5; ++var1) {
            Type var8 = var0[var1];
            boolean var4 = false;
            int var6 = var0.length;
            int var2 = 0;

            boolean var3;
            while(true) {
               var3 = var4;
               if (var2 >= var6) {
                  break;
               }

               Type var9 = var0[var2];
               if (var8 != var9 && isAssignable(var9, (Type)var8, (Map)null)) {
                  var3 = true;
                  break;
               }

               ++var2;
            }

            if (!var3) {
               var7.add(var8);
            }
         }

         return (Type[])var7.toArray(new Type[var7.size()]);
      }
   }

   public static final ParameterizedType parameterize(Class var0, Map var1) {
      Validate.notNull(var0, "raw class is null");
      Validate.notNull(var1, "typeArgMappings is null");
      return parameterizeWithOwner((Type)null, var0, (Type[])extractTypeArgumentsFrom(var1, var0.getTypeParameters()));
   }

   public static final ParameterizedType parameterize(Class var0, Type... var1) {
      return parameterizeWithOwner((Type)null, var0, (Type[])var1);
   }

   public static final ParameterizedType parameterizeWithOwner(Type var0, Class var1, Map var2) {
      Validate.notNull(var1, "raw class is null");
      Validate.notNull(var2, "typeArgMappings is null");
      return parameterizeWithOwner(var0, var1, extractTypeArgumentsFrom(var2, var1.getTypeParameters()));
   }

   public static final ParameterizedType parameterizeWithOwner(Type var0, Class var1, Type... var2) {
      Validate.notNull(var1, "raw class is null");
      boolean var3;
      if (var1.getEnclosingClass() == null) {
         if (var0 == null) {
            var3 = true;
         } else {
            var3 = false;
         }

         Validate.isTrue(var3, "no owner allowed for top-level %s", var1);
         var0 = null;
      } else if (var0 == null) {
         var0 = var1.getEnclosingClass();
      } else {
         Validate.isTrue(isAssignable((Type)var0, (Class)var1.getEnclosingClass()), "%s is invalid owner type for parameterized %s", var0, var1);
      }

      Validate.noNullElements((Object[])var2, "null type argument at index %s");
      if (var1.getTypeParameters().length == var2.length) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "invalid number of type parameters specified: expected %d, got %d", var1.getTypeParameters().length, var2.length);
      return new TypeUtils.ParameterizedTypeImpl(var1, (Type)var0, var2);
   }

   private static String parameterizedTypeToString(ParameterizedType var0) {
      StringBuilder var1 = new StringBuilder();
      Type var2 = var0.getOwnerType();
      Class var3 = (Class)var0.getRawType();
      if (var2 == null) {
         var1.append(var3.getName());
      } else {
         if (var2 instanceof Class) {
            var1.append(((Class)var2).getName());
         } else {
            var1.append(var2.toString());
         }

         var1.append('.');
         var1.append(var3.getSimpleName());
      }

      int[] var4 = findRecursiveTypes(var0);
      if (var4.length > 0) {
         appendRecursiveTypes(var1, var4, var0.getActualTypeArguments());
      } else {
         var1.append('<');
         appendAllTo(var1, ", ", var0.getActualTypeArguments()).append('>');
      }

      return var1.toString();
   }

   private static Type substituteTypeVariables(Type var0, Map var1) {
      if (var0 instanceof TypeVariable && var1 != null) {
         Type var2 = (Type)var1.get(var0);
         if (var2 != null) {
            return var2;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("missing assignment type for type variable ");
            var3.append(var0);
            throw new IllegalArgumentException(var3.toString());
         }
      } else {
         return var0;
      }
   }

   public static String toLongString(TypeVariable var0) {
      Validate.notNull(var0, "var is null");
      StringBuilder var2 = new StringBuilder();
      GenericDeclaration var1 = var0.getGenericDeclaration();
      if (var1 instanceof Class) {
         Class var3;
         for(var3 = (Class)var1; var3.getEnclosingClass() != null; var3 = var3.getEnclosingClass()) {
            var2.insert(0, var3.getSimpleName()).insert(0, '.');
         }

         var2.insert(0, var3.getName());
      } else if (var1 instanceof Type) {
         var2.append(toString((Type)var1));
      } else {
         var2.append(var1);
      }

      var2.append(':');
      var2.append(typeVariableToString(var0));
      return var2.toString();
   }

   private static String toString(Object var0) {
      return var0 instanceof Type ? toString((Type)var0) : var0.toString();
   }

   public static String toString(Type var0) {
      Validate.notNull(var0);
      if (var0 instanceof Class) {
         return classToString((Class)var0);
      } else if (var0 instanceof ParameterizedType) {
         return parameterizedTypeToString((ParameterizedType)var0);
      } else if (var0 instanceof WildcardType) {
         return wildcardTypeToString((WildcardType)var0);
      } else if (var0 instanceof TypeVariable) {
         return typeVariableToString((TypeVariable)var0);
      } else if (var0 instanceof GenericArrayType) {
         return genericArrayTypeToString((GenericArrayType)var0);
      } else {
         throw new IllegalArgumentException(ObjectUtils.identityToString(var0));
      }
   }

   private static String typeVariableToString(TypeVariable var0) {
      StringBuilder var1 = new StringBuilder(var0.getName());
      Type[] var2 = var0.getBounds();
      if (var2.length > 0 && (var2.length != 1 || !Object.class.equals(var2[0]))) {
         var1.append(" extends ");
         appendAllTo(var1, " & ", var0.getBounds());
      }

      return var1.toString();
   }

   public static boolean typesSatisfyVariables(Map var0) {
      Validate.notNull(var0, "typeVarAssigns is null");
      Iterator var3 = var0.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var5 = (Entry)var3.next();
         TypeVariable var4 = (TypeVariable)var5.getKey();
         Type var7 = (Type)var5.getValue();
         Type[] var6 = getImplicitBounds(var4);
         int var2 = var6.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!isAssignable(var7, substituteTypeVariables(var6[var1], var0), var0)) {
               return false;
            }
         }
      }

      return true;
   }

   private static Type[] unrollBounds(Map var0, Type[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Type var3 = unrollVariables(var0, var1[var2]);
         if (var3 == null) {
            var1 = (Type[])ArrayUtils.remove((Object[])var1, var2);
            --var2;
         } else {
            var1[var2] = var3;
         }
      }

      return var1;
   }

   private static Type unrollVariableAssignments(TypeVariable var0, Map var1) {
      while(true) {
         Type var2 = (Type)var1.get(var0);
         if (!(var2 instanceof TypeVariable) || var2.equals(var0)) {
            return var2;
         }

         var0 = (TypeVariable)var2;
      }
   }

   public static Type unrollVariables(Map var0, Type var1) {
      Map var3 = var0;
      if (var0 == null) {
         var3 = Collections.emptyMap();
      }

      if (containsTypeVariables(var1)) {
         if (var1 instanceof TypeVariable) {
            return unrollVariables(var3, (Type)var3.get(var1));
         }

         if (var1 instanceof ParameterizedType) {
            ParameterizedType var7 = (ParameterizedType)var1;
            Object var6;
            if (var7.getOwnerType() == null) {
               var6 = var3;
            } else {
               var6 = new HashMap(var3);
               ((Map)var6).putAll(getTypeArguments(var7));
            }

            Type[] var8 = var7.getActualTypeArguments();

            for(int var2 = 0; var2 < var8.length; ++var2) {
               Type var4 = unrollVariables((Map)var6, var8[var2]);
               if (var4 != null) {
                  var8[var2] = var4;
               }
            }

            return parameterizeWithOwner(var7.getOwnerType(), (Class)var7.getRawType(), var8);
         }

         if (var1 instanceof WildcardType) {
            WildcardType var5 = (WildcardType)var1;
            return wildcardType().withUpperBounds(unrollBounds(var3, var5.getUpperBounds())).withLowerBounds(unrollBounds(var3, var5.getLowerBounds())).build();
         }
      }

      return var1;
   }

   public static TypeUtils.WildcardTypeBuilder wildcardType() {
      return new TypeUtils.WildcardTypeBuilder();
   }

   private static String wildcardTypeToString(WildcardType var0) {
      StringBuilder var1 = (new StringBuilder()).append('?');
      Type[] var2 = var0.getLowerBounds();
      Type[] var3 = var0.getUpperBounds();
      if (var2.length > 1 || var2.length == 1 && var2[0] != null) {
         var1.append(" super ");
         appendAllTo(var1, " & ", var2);
      } else if (var3.length > 1 || var3.length == 1 && !Object.class.equals(var3[0])) {
         var1.append(" extends ");
         appendAllTo(var1, " & ", var3);
      }

      return var1.toString();
   }

   public static Typed wrap(Class var0) {
      return wrap((Type)var0);
   }

   public static Typed wrap(final Type var0) {
      return new Typed() {
         public Type getType() {
            return var0;
         }
      };
   }

   private static final class GenericArrayTypeImpl implements GenericArrayType {
      private final Type componentType;

      private GenericArrayTypeImpl(Type var1) {
         this.componentType = var1;
      }

      // $FF: synthetic method
      GenericArrayTypeImpl(Type var1, Object var2) {
         this(var1);
      }

      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof GenericArrayType && TypeUtils.equals((GenericArrayType)this, (Type)((GenericArrayType)var1));
      }

      public Type getGenericComponentType() {
         return this.componentType;
      }

      public int hashCode() {
         return 1072 | this.componentType.hashCode();
      }

      public String toString() {
         return TypeUtils.toString((Type)this);
      }
   }

   private static final class ParameterizedTypeImpl implements ParameterizedType {
      private final Class raw;
      private final Type[] typeArguments;
      private final Type useOwner;

      private ParameterizedTypeImpl(Class var1, Type var2, Type[] var3) {
         this.raw = var1;
         this.useOwner = var2;
         this.typeArguments = (Type[])Arrays.copyOf(var3, var3.length, Type[].class);
      }

      // $FF: synthetic method
      ParameterizedTypeImpl(Class var1, Type var2, Type[] var3, Object var4) {
         this(var1, var2, var3);
      }

      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof ParameterizedType && TypeUtils.equals((ParameterizedType)this, (Type)((ParameterizedType)var1));
      }

      public Type[] getActualTypeArguments() {
         return (Type[])this.typeArguments.clone();
      }

      public Type getOwnerType() {
         return this.useOwner;
      }

      public Type getRawType() {
         return this.raw;
      }

      public int hashCode() {
         return ((1136 | this.raw.hashCode()) << 4 | Objects.hashCode(this.useOwner)) << 8 | Arrays.hashCode(this.typeArguments);
      }

      public String toString() {
         return TypeUtils.toString((Type)this);
      }
   }

   public static class WildcardTypeBuilder implements Builder {
      private Type[] lowerBounds;
      private Type[] upperBounds;

      private WildcardTypeBuilder() {
      }

      // $FF: synthetic method
      WildcardTypeBuilder(Object var1) {
         this();
      }

      public WildcardType build() {
         return new TypeUtils.WildcardTypeImpl(this.upperBounds, this.lowerBounds);
      }

      public TypeUtils.WildcardTypeBuilder withLowerBounds(Type... var1) {
         this.lowerBounds = var1;
         return this;
      }

      public TypeUtils.WildcardTypeBuilder withUpperBounds(Type... var1) {
         this.upperBounds = var1;
         return this;
      }
   }

   private static final class WildcardTypeImpl implements WildcardType {
      private static final Type[] EMPTY_BOUNDS = new Type[0];
      private final Type[] lowerBounds;
      private final Type[] upperBounds;

      private WildcardTypeImpl(Type[] var1, Type[] var2) {
         this.upperBounds = (Type[])ObjectUtils.defaultIfNull(var1, EMPTY_BOUNDS);
         this.lowerBounds = (Type[])ObjectUtils.defaultIfNull(var2, EMPTY_BOUNDS);
      }

      // $FF: synthetic method
      WildcardTypeImpl(Type[] var1, Type[] var2, Object var3) {
         this(var1, var2);
      }

      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof WildcardType && TypeUtils.equals((WildcardType)this, (Type)((WildcardType)var1));
      }

      public Type[] getLowerBounds() {
         return (Type[])this.lowerBounds.clone();
      }

      public Type[] getUpperBounds() {
         return (Type[])this.upperBounds.clone();
      }

      public int hashCode() {
         return (18688 | Arrays.hashCode(this.upperBounds)) << 8 | Arrays.hashCode(this.lowerBounds);
      }

      public String toString() {
         return TypeUtils.toString((Type)this);
      }
   }
}
