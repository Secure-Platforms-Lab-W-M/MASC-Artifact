package org.apache.commons.lang3;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.lang3.mutable.MutableObject;

public class ClassUtils {
   public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
   public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
   public static final String PACKAGE_SEPARATOR = String.valueOf('.');
   public static final char PACKAGE_SEPARATOR_CHAR = '.';
   private static final Map abbreviationMap;
   private static final Map namePrimitiveMap;
   private static final Map primitiveWrapperMap;
   private static final Map reverseAbbreviationMap;
   private static final Map wrapperPrimitiveMap;

   static {
      HashMap var0 = new HashMap();
      namePrimitiveMap = var0;
      var0.put("boolean", Boolean.TYPE);
      namePrimitiveMap.put("byte", Byte.TYPE);
      namePrimitiveMap.put("char", Character.TYPE);
      namePrimitiveMap.put("short", Short.TYPE);
      namePrimitiveMap.put("int", Integer.TYPE);
      namePrimitiveMap.put("long", Long.TYPE);
      namePrimitiveMap.put("double", Double.TYPE);
      namePrimitiveMap.put("float", Float.TYPE);
      namePrimitiveMap.put("void", Void.TYPE);
      var0 = new HashMap();
      primitiveWrapperMap = var0;
      var0.put(Boolean.TYPE, Boolean.class);
      primitiveWrapperMap.put(Byte.TYPE, Byte.class);
      primitiveWrapperMap.put(Character.TYPE, Character.class);
      primitiveWrapperMap.put(Short.TYPE, Short.class);
      primitiveWrapperMap.put(Integer.TYPE, Integer.class);
      primitiveWrapperMap.put(Long.TYPE, Long.class);
      primitiveWrapperMap.put(Double.TYPE, Double.class);
      primitiveWrapperMap.put(Float.TYPE, Float.class);
      primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
      wrapperPrimitiveMap = new HashMap();
      Iterator var4 = primitiveWrapperMap.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var2 = (Entry)var4.next();
         Class var1 = (Class)var2.getKey();
         Class var6 = (Class)var2.getValue();
         if (!var1.equals(var6)) {
            wrapperPrimitiveMap.put(var6, var1);
         }
      }

      var0 = new HashMap();
      var0.put("int", "I");
      var0.put("boolean", "Z");
      var0.put("float", "F");
      var0.put("long", "J");
      var0.put("short", "S");
      var0.put("byte", "B");
      var0.put("double", "D");
      var0.put("char", "C");
      HashMap var5 = new HashMap();
      Iterator var7 = var0.entrySet().iterator();

      while(var7.hasNext()) {
         Entry var3 = (Entry)var7.next();
         var5.put((String)var3.getValue(), (String)var3.getKey());
      }

      abbreviationMap = Collections.unmodifiableMap(var0);
      reverseAbbreviationMap = Collections.unmodifiableMap(var5);
   }

   public static List convertClassNamesToClasses(List var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList(var0.size());
         Iterator var4 = var0.iterator();

         while(var4.hasNext()) {
            String var2 = (String)var4.next();

            try {
               var1.add(Class.forName(var2));
            } catch (Exception var3) {
               var1.add((Object)null);
            }
         }

         return var1;
      }
   }

   public static List convertClassesToClassNames(List var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList(var0.size());
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            Class var2 = (Class)var3.next();
            if (var2 == null) {
               var1.add((Object)null);
            } else {
               var1.add(var2.getName());
            }
         }

         return var1;
      }
   }

   public static String getAbbreviatedName(Class var0, int var1) {
      return var0 == null ? "" : getAbbreviatedName(var0.getName(), var1);
   }

   public static String getAbbreviatedName(String var0, int var1) {
      if (var1 > 0) {
         if (var0 == null) {
            return "";
         } else {
            int var4 = StringUtils.countMatches(var0, '.');
            String[] var6 = new String[var4 + 1];
            int var3 = var0.length() - 1;

            for(int var2 = var4; var2 >= 0; --var2) {
               int var5 = var0.lastIndexOf(46, var3);
               String var7 = var0.substring(var5 + 1, var3 + 1);
               var3 = var1 - var7.length();
               var1 = var3;
               if (var2 > 0) {
                  var1 = var3 - 1;
               }

               if (var2 == var4) {
                  var6[var2] = var7;
               } else if (var1 > 0) {
                  var6[var2] = var7;
               } else {
                  var6[var2] = var7.substring(0, 1);
               }

               var3 = var5 - 1;
            }

            return StringUtils.join((Object[])var6, '.');
         }
      } else {
         throw new IllegalArgumentException("len must be > 0");
      }
   }

   public static List getAllInterfaces(Class var0) {
      if (var0 == null) {
         return null;
      } else {
         LinkedHashSet var1 = new LinkedHashSet();
         getAllInterfaces(var0, var1);
         return new ArrayList(var1);
      }
   }

   private static void getAllInterfaces(Class var0, HashSet var1) {
      while(var0 != null) {
         Class[] var4 = var0.getInterfaces();
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            Class var5 = var4[var2];
            if (var1.add(var5)) {
               getAllInterfaces(var5, var1);
            }
         }

         var0 = var0.getSuperclass();
      }

   }

   public static List getAllSuperclasses(Class var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();

         for(var0 = var0.getSuperclass(); var0 != null; var0 = var0.getSuperclass()) {
            var1.add(var0);
         }

         return var1;
      }
   }

   public static String getCanonicalName(Class var0) {
      return getCanonicalName(var0, "");
   }

   public static String getCanonicalName(Class var0, String var1) {
      if (var0 == null) {
         return var1;
      } else {
         String var2 = var0.getCanonicalName();
         return var2 == null ? var1 : var2;
      }
   }

   public static String getCanonicalName(Object var0) {
      return getCanonicalName(var0, "");
   }

   public static String getCanonicalName(Object var0, String var1) {
      if (var0 == null) {
         return var1;
      } else {
         String var2 = var0.getClass().getCanonicalName();
         return var2 == null ? var1 : var2;
      }
   }

   private static String getCanonicalName(String var0) {
      var0 = StringUtils.deleteWhitespace(var0);
      if (var0 == null) {
         return null;
      } else {
         int var1;
         for(var1 = 0; var0.startsWith("["); var0 = var0.substring(1)) {
            ++var1;
         }

         if (var1 < 1) {
            return var0;
         } else {
            int var2;
            String var3;
            if (var0.startsWith("L")) {
               if (var0.endsWith(";")) {
                  var2 = var0.length() - 1;
               } else {
                  var2 = var0.length();
               }

               var3 = var0.substring(1, var2);
            } else {
               var3 = var0;
               if (!var0.isEmpty()) {
                  var3 = (String)reverseAbbreviationMap.get(var0.substring(0, 1));
               }
            }

            StringBuilder var4 = new StringBuilder(var3);

            for(var2 = 0; var2 < var1; ++var2) {
               var4.append("[]");
            }

            return var4.toString();
         }
      }
   }

   public static Class getClass(ClassLoader var0, String var1) throws ClassNotFoundException {
      return getClass(var0, var1, true);
   }

   public static Class getClass(ClassLoader var0, String var1, boolean var2) throws ClassNotFoundException {
      try {
         if (namePrimitiveMap.containsKey(var1)) {
            return (Class)namePrimitiveMap.get(var1);
         } else {
            Class var4 = Class.forName(toCanonicalName(var1), var2, var0);
            return var4;
         }
      } catch (ClassNotFoundException var7) {
         int var3 = var1.lastIndexOf(46);
         if (var3 != -1) {
            try {
               StringBuilder var5 = new StringBuilder();
               var5.append(var1.substring(0, var3));
               var5.append('$');
               var5.append(var1.substring(var3 + 1));
               Class var8 = getClass(var0, var5.toString(), var2);
               return var8;
            } catch (ClassNotFoundException var6) {
            }
         }

         throw var7;
      }
   }

   public static Class getClass(String var0) throws ClassNotFoundException {
      return getClass(var0, true);
   }

   public static Class getClass(String var0, boolean var1) throws ClassNotFoundException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      if (var2 == null) {
         var2 = ClassUtils.class.getClassLoader();
      }

      return getClass(var2, var0, var1);
   }

   public static String getName(Class var0) {
      return getName(var0, "");
   }

   public static String getName(Class var0, String var1) {
      return var0 == null ? var1 : var0.getName();
   }

   public static String getName(Object var0) {
      return getName(var0, "");
   }

   public static String getName(Object var0, String var1) {
      return var0 == null ? var1 : var0.getClass().getName();
   }

   public static String getPackageCanonicalName(Class var0) {
      return var0 == null ? "" : getPackageCanonicalName(var0.getName());
   }

   public static String getPackageCanonicalName(Object var0, String var1) {
      return var0 == null ? var1 : getPackageCanonicalName(var0.getClass().getName());
   }

   public static String getPackageCanonicalName(String var0) {
      return getPackageName(getCanonicalName(var0));
   }

   public static String getPackageName(Class var0) {
      return var0 == null ? "" : getPackageName(var0.getName());
   }

   public static String getPackageName(Object var0, String var1) {
      return var0 == null ? var1 : getPackageName(var0.getClass());
   }

   public static String getPackageName(String var0) {
      String var2 = var0;
      if (StringUtils.isEmpty(var0)) {
         return "";
      } else {
         while(var2.charAt(0) == '[') {
            var2 = var2.substring(1);
         }

         var0 = var2;
         if (var2.charAt(0) == 'L') {
            var0 = var2;
            if (var2.charAt(var2.length() - 1) == ';') {
               var0 = var2.substring(1);
            }
         }

         int var1 = var0.lastIndexOf(46);
         return var1 == -1 ? "" : var0.substring(0, var1);
      }
   }

   public static Method getPublicMethod(Class var0, String var1, Class... var2) throws NoSuchMethodException {
      Method var3 = var0.getMethod(var1, var2);
      if (Modifier.isPublic(var3.getDeclaringClass().getModifiers())) {
         return var3;
      } else {
         ArrayList var7 = new ArrayList();
         var7.addAll(getAllInterfaces(var0));
         var7.addAll(getAllSuperclasses(var0));
         Iterator var5 = var7.iterator();

         while(true) {
            Class var8;
            do {
               if (!var5.hasNext()) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Can't find a public method for ");
                  var6.append(var1);
                  var6.append(" ");
                  var6.append(ArrayUtils.toString(var2));
                  throw new NoSuchMethodException(var6.toString());
               }

               var8 = (Class)var5.next();
            } while(!Modifier.isPublic(var8.getModifiers()));

            try {
               var3 = var8.getMethod(var1, var2);
            } catch (NoSuchMethodException var4) {
               continue;
            }

            if (Modifier.isPublic(var3.getDeclaringClass().getModifiers())) {
               return var3;
            }
         }
      }
   }

   public static String getShortCanonicalName(Class var0) {
      return var0 == null ? "" : getShortCanonicalName(var0.getName());
   }

   public static String getShortCanonicalName(Object var0, String var1) {
      return var0 == null ? var1 : getShortCanonicalName(var0.getClass().getName());
   }

   public static String getShortCanonicalName(String var0) {
      return getShortClassName(getCanonicalName(var0));
   }

   public static String getShortClassName(Class var0) {
      return var0 == null ? "" : getShortClassName(var0.getName());
   }

   public static String getShortClassName(Object var0, String var1) {
      return var0 == null ? var1 : getShortClassName(var0.getClass());
   }

   public static String getShortClassName(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return "";
      } else {
         StringBuilder var5 = new StringBuilder();
         boolean var3 = var0.startsWith("[");
         int var1 = 0;
         String var4 = var0;
         if (var3) {
            var4 = var0;

            while(var4.charAt(0) == '[') {
               var4 = var4.substring(1);
               var5.append("[]");
            }

            var0 = var4;
            if (var4.charAt(0) == 'L') {
               var0 = var4;
               if (var4.charAt(var4.length() - 1) == ';') {
                  var0 = var4.substring(1, var4.length() - 1);
               }
            }

            var4 = var0;
            if (reverseAbbreviationMap.containsKey(var0)) {
               var4 = (String)reverseAbbreviationMap.get(var0);
            }
         }

         int var2 = var4.lastIndexOf(46);
         if (var2 != -1) {
            var1 = var2 + 1;
         }

         var1 = var4.indexOf(36, var1);
         var4 = var4.substring(var2 + 1);
         var0 = var4;
         if (var1 != -1) {
            var0 = var4.replace('$', '.');
         }

         StringBuilder var6 = new StringBuilder();
         var6.append(var0);
         var6.append(var5);
         return var6.toString();
      }
   }

   public static String getSimpleName(Class var0) {
      return getSimpleName(var0, "");
   }

   public static String getSimpleName(Class var0, String var1) {
      return var0 == null ? var1 : var0.getSimpleName();
   }

   public static String getSimpleName(Object var0) {
      return getSimpleName(var0, "");
   }

   public static String getSimpleName(Object var0, String var1) {
      return var0 == null ? var1 : var0.getClass().getSimpleName();
   }

   public static Iterable hierarchy(Class var0) {
      return hierarchy(var0, ClassUtils.Interfaces.EXCLUDE);
   }

   public static Iterable hierarchy(final Class var0, ClassUtils.Interfaces var1) {
      final Iterable var2 = new Iterable() {
         public Iterator iterator() {
            return new Iterator(new MutableObject(var0)) {
               // $FF: synthetic field
               final MutableObject val$next;

               {
                  this.val$next = var2;
               }

               public boolean hasNext() {
                  return this.val$next.getValue() != null;
               }

               public Class next() {
                  Class var1 = (Class)this.val$next.getValue();
                  this.val$next.setValue(var1.getSuperclass());
                  return var1;
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
      return var1 != ClassUtils.Interfaces.INCLUDE ? var2 : new Iterable() {
         public Iterator iterator() {
            final HashSet var1 = new HashSet();
            return new Iterator(var2.iterator()) {
               Iterator interfaces;
               // $FF: synthetic field
               final Iterator val$wrapped;

               {
                  this.val$wrapped = var2x;
                  this.interfaces = Collections.emptySet().iterator();
               }

               private void walkInterfaces(Set var1x, Class var2x) {
                  Class[] var6 = var2x.getInterfaces();
                  int var4 = var6.length;

                  for(int var3 = 0; var3 < var4; ++var3) {
                     Class var5 = var6[var3];
                     if (!var1.contains(var5)) {
                        var1x.add(var5);
                     }

                     this.walkInterfaces(var1x, var5);
                  }

               }

               public boolean hasNext() {
                  return this.interfaces.hasNext() || this.val$wrapped.hasNext();
               }

               public Class next() {
                  Class var1x;
                  if (this.interfaces.hasNext()) {
                     var1x = (Class)this.interfaces.next();
                     var1.add(var1x);
                     return var1x;
                  } else {
                     var1x = (Class)this.val$wrapped.next();
                     LinkedHashSet var2x = new LinkedHashSet();
                     this.walkInterfaces(var2x, var1x);
                     this.interfaces = var2x.iterator();
                     return var1x;
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   public static boolean isAssignable(Class var0, Class var1) {
      return isAssignable(var0, var1, true);
   }

   public static boolean isAssignable(Class var0, Class var1, boolean var2) {
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var3 = false;
      if (var1 == null) {
         return false;
      } else if (var0 == null) {
         return var1.isPrimitive() ^ true;
      } else {
         Class var9 = var0;
         if (var2) {
            Class var8 = var0;
            if (var0.isPrimitive()) {
               var8 = var0;
               if (!var1.isPrimitive()) {
                  var0 = primitiveToWrapper(var0);
                  var8 = var0;
                  if (var0 == null) {
                     return false;
                  }
               }
            }

            var9 = var8;
            if (var1.isPrimitive()) {
               var9 = var8;
               if (!var8.isPrimitive()) {
                  var0 = wrapperToPrimitive(var8);
                  var9 = var0;
                  if (var0 == null) {
                     return false;
                  }
               }
            }
         }

         if (var9.equals(var1)) {
            return true;
         } else if (var9.isPrimitive()) {
            if (!var1.isPrimitive()) {
               return false;
            } else if (Integer.TYPE.equals(var9)) {
               if (!Long.TYPE.equals(var1) && !Float.TYPE.equals(var1)) {
                  var2 = var3;
                  if (!Double.TYPE.equals(var1)) {
                     return var2;
                  }
               }

               var2 = true;
               return var2;
            } else if (Long.TYPE.equals(var9)) {
               if (!Float.TYPE.equals(var1)) {
                  var2 = var4;
                  if (!Double.TYPE.equals(var1)) {
                     return var2;
                  }
               }

               var2 = true;
               return var2;
            } else if (Boolean.TYPE.equals(var9)) {
               return false;
            } else if (Double.TYPE.equals(var9)) {
               return false;
            } else if (Float.TYPE.equals(var9)) {
               return Double.TYPE.equals(var1);
            } else if (Character.TYPE.equals(var9)) {
               if (!Integer.TYPE.equals(var1) && !Long.TYPE.equals(var1) && !Float.TYPE.equals(var1)) {
                  var2 = var5;
                  if (!Double.TYPE.equals(var1)) {
                     return var2;
                  }
               }

               var2 = true;
               return var2;
            } else if (Short.TYPE.equals(var9)) {
               if (!Integer.TYPE.equals(var1) && !Long.TYPE.equals(var1) && !Float.TYPE.equals(var1)) {
                  var2 = var6;
                  if (!Double.TYPE.equals(var1)) {
                     return var2;
                  }
               }

               var2 = true;
               return var2;
            } else if (!Byte.TYPE.equals(var9)) {
               return false;
            } else {
               if (!Short.TYPE.equals(var1) && !Integer.TYPE.equals(var1) && !Long.TYPE.equals(var1) && !Float.TYPE.equals(var1)) {
                  var2 = var7;
                  if (!Double.TYPE.equals(var1)) {
                     return var2;
                  }
               }

               var2 = true;
               return var2;
            }
         } else {
            return var1.isAssignableFrom(var9);
         }
      }
   }

   public static boolean isAssignable(Class[] var0, Class... var1) {
      return isAssignable(var0, var1, true);
   }

   public static boolean isAssignable(Class[] var0, Class[] var1, boolean var2) {
      if (!ArrayUtils.isSameLength((Object[])var0, (Object[])var1)) {
         return false;
      } else {
         Class[] var4 = var0;
         if (var0 == null) {
            var4 = ArrayUtils.EMPTY_CLASS_ARRAY;
         }

         var0 = var1;
         if (var1 == null) {
            var0 = ArrayUtils.EMPTY_CLASS_ARRAY;
         }

         for(int var3 = 0; var3 < var4.length; ++var3) {
            if (!isAssignable(var4[var3], var0[var3], var2)) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isInnerClass(Class var0) {
      return var0 != null && var0.getEnclosingClass() != null;
   }

   public static boolean isPrimitiveOrWrapper(Class var0) {
      boolean var1 = false;
      if (var0 == null) {
         return false;
      } else {
         if (var0.isPrimitive() || isPrimitiveWrapper(var0)) {
            var1 = true;
         }

         return var1;
      }
   }

   public static boolean isPrimitiveWrapper(Class var0) {
      return wrapperPrimitiveMap.containsKey(var0);
   }

   public static Class primitiveToWrapper(Class var0) {
      Class var2 = var0;
      if (var0 != null) {
         var2 = var0;
         if (var0.isPrimitive()) {
            var2 = (Class)primitiveWrapperMap.get(var0);
         }
      }

      return var2;
   }

   public static Class[] primitivesToWrappers(Class... var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return var0;
      } else {
         Class[] var2 = new Class[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = primitiveToWrapper(var0[var1]);
         }

         return var2;
      }
   }

   private static String toCanonicalName(String var0) {
      var0 = StringUtils.deleteWhitespace(var0);
      Validate.notNull(var0, "className must not be null.");
      String var1 = var0;
      if (var0.endsWith("[]")) {
         StringBuilder var3 = new StringBuilder();

         while(var0.endsWith("[]")) {
            var0 = var0.substring(0, var0.length() - 2);
            var3.append("[");
         }

         String var2 = (String)abbreviationMap.get(var0);
         if (var2 != null) {
            var3.append(var2);
         } else {
            var3.append("L");
            var3.append(var0);
            var3.append(";");
         }

         var1 = var3.toString();
      }

      return var1;
   }

   public static Class[] toClass(Object... var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return ArrayUtils.EMPTY_CLASS_ARRAY;
      } else {
         Class[] var3 = new Class[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            Class var2;
            if (var0[var1] == null) {
               var2 = null;
            } else {
               var2 = var0[var1].getClass();
            }

            var3[var1] = var2;
         }

         return var3;
      }
   }

   public static Class wrapperToPrimitive(Class var0) {
      return (Class)wrapperPrimitiveMap.get(var0);
   }

   public static Class[] wrappersToPrimitives(Class... var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return var0;
      } else {
         Class[] var2 = new Class[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = wrapperToPrimitive(var0[var1]);
         }

         return var2;
      }
   }

   public static enum Interfaces {
      EXCLUDE,
      INCLUDE;

      static {
         ClassUtils.Interfaces var0 = new ClassUtils.Interfaces("EXCLUDE", 1);
         EXCLUDE = var0;
      }
   }
}
