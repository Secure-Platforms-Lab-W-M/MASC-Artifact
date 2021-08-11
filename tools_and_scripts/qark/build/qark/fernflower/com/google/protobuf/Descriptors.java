package com.google.protobuf;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Descriptors {
   private static String computeFullName(Descriptors.FileDescriptor var0, Descriptors.Descriptor var1, String var2) {
      if (var1 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getFullName());
         var3.append('.');
         var3.append(var2);
         return var3.toString();
      } else if (var0.getPackage().length() > 0) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var0.getPackage());
         var4.append('.');
         var4.append(var2);
         return var4.toString();
      } else {
         return var2;
      }
   }

   public static final class Descriptor implements Descriptors.GenericDescriptor {
      private final Descriptors.Descriptor containingType;
      private final Descriptors.EnumDescriptor[] enumTypes;
      private final Descriptors.FieldDescriptor[] extensions;
      private final Descriptors.FieldDescriptor[] fields;
      private final Descriptors.FileDescriptor file;
      private final String fullName;
      private final int index;
      private final Descriptors.Descriptor[] nestedTypes;
      private DescriptorProtos.DescriptorProto proto;

      private Descriptor(DescriptorProtos.DescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.Descriptor var3, int var4) throws Descriptors.DescriptorValidationException {
         this.index = var4;
         this.proto = var1;
         this.fullName = Descriptors.computeFullName(var2, var3, var1.getName());
         this.file = var2;
         this.containingType = var3;
         this.nestedTypes = new Descriptors.Descriptor[var1.getNestedTypeCount()];

         for(var4 = 0; var4 < var1.getNestedTypeCount(); ++var4) {
            this.nestedTypes[var4] = new Descriptors.Descriptor(var1.getNestedType(var4), var2, this, var4);
         }

         this.enumTypes = new Descriptors.EnumDescriptor[var1.getEnumTypeCount()];

         for(var4 = 0; var4 < var1.getEnumTypeCount(); ++var4) {
            this.enumTypes[var4] = new Descriptors.EnumDescriptor(var1.getEnumType(var4), var2, this, var4);
         }

         this.fields = new Descriptors.FieldDescriptor[var1.getFieldCount()];

         for(var4 = 0; var4 < var1.getFieldCount(); ++var4) {
            this.fields[var4] = new Descriptors.FieldDescriptor(var1.getField(var4), var2, this, var4, false);
         }

         this.extensions = new Descriptors.FieldDescriptor[var1.getExtensionCount()];

         for(var4 = 0; var4 < var1.getExtensionCount(); ++var4) {
            this.extensions[var4] = new Descriptors.FieldDescriptor(var1.getExtension(var4), var2, this, var4, true);
         }

         var2.pool.addSymbol(this);
      }

      // $FF: synthetic method
      Descriptor(DescriptorProtos.DescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.Descriptor var3, int var4, Object var5) throws Descriptors.DescriptorValidationException {
         this(var1, var2, var3, var4);
      }

      private void crossLink() throws Descriptors.DescriptorValidationException {
         Descriptors.Descriptor[] var3 = this.nestedTypes;
         int var2 = var3.length;

         int var1;
         for(var1 = 0; var1 < var2; ++var1) {
            var3[var1].crossLink();
         }

         Descriptors.FieldDescriptor[] var4 = this.fields;
         var2 = var4.length;

         for(var1 = 0; var1 < var2; ++var1) {
            var4[var1].crossLink();
         }

         var4 = this.extensions;
         var2 = var4.length;

         for(var1 = 0; var1 < var2; ++var1) {
            var4[var1].crossLink();
         }

      }

      private void setProto(DescriptorProtos.DescriptorProto var1) {
         this.proto = var1;
         int var2 = 0;

         while(true) {
            Descriptors.Descriptor[] var3 = this.nestedTypes;
            if (var2 >= var3.length) {
               var2 = 0;

               while(true) {
                  Descriptors.EnumDescriptor[] var4 = this.enumTypes;
                  if (var2 >= var4.length) {
                     var2 = 0;

                     while(true) {
                        Descriptors.FieldDescriptor[] var5 = this.fields;
                        if (var2 >= var5.length) {
                           var2 = 0;

                           while(true) {
                              var5 = this.extensions;
                              if (var2 >= var5.length) {
                                 return;
                              }

                              var5[var2].setProto(var1.getExtension(var2));
                              ++var2;
                           }
                        }

                        var5[var2].setProto(var1.getField(var2));
                        ++var2;
                     }
                  }

                  var4[var2].setProto(var1.getEnumType(var2));
                  ++var2;
               }
            }

            var3[var2].setProto(var1.getNestedType(var2));
            ++var2;
         }
      }

      public Descriptors.EnumDescriptor findEnumTypeByName(String var1) {
         Descriptors.DescriptorPool var2 = this.file.pool;
         StringBuilder var3 = new StringBuilder();
         var3.append(this.fullName);
         var3.append('.');
         var3.append(var1);
         Descriptors.GenericDescriptor var4 = var2.findSymbol(var3.toString());
         return var4 != null && var4 instanceof Descriptors.EnumDescriptor ? (Descriptors.EnumDescriptor)var4 : null;
      }

      public Descriptors.FieldDescriptor findFieldByName(String var1) {
         Descriptors.DescriptorPool var2 = this.file.pool;
         StringBuilder var3 = new StringBuilder();
         var3.append(this.fullName);
         var3.append('.');
         var3.append(var1);
         Descriptors.GenericDescriptor var4 = var2.findSymbol(var3.toString());
         return var4 != null && var4 instanceof Descriptors.FieldDescriptor ? (Descriptors.FieldDescriptor)var4 : null;
      }

      public Descriptors.FieldDescriptor findFieldByNumber(int var1) {
         return (Descriptors.FieldDescriptor)this.file.pool.fieldsByNumber.get(new Descriptors.DescriptorPool.DescriptorIntPair(this, var1));
      }

      public Descriptors.Descriptor findNestedTypeByName(String var1) {
         Descriptors.DescriptorPool var2 = this.file.pool;
         StringBuilder var3 = new StringBuilder();
         var3.append(this.fullName);
         var3.append('.');
         var3.append(var1);
         Descriptors.GenericDescriptor var4 = var2.findSymbol(var3.toString());
         return var4 != null && var4 instanceof Descriptors.Descriptor ? (Descriptors.Descriptor)var4 : null;
      }

      public Descriptors.Descriptor getContainingType() {
         return this.containingType;
      }

      public List getEnumTypes() {
         return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
      }

      public List getExtensions() {
         return Collections.unmodifiableList(Arrays.asList(this.extensions));
      }

      public List getFields() {
         return Collections.unmodifiableList(Arrays.asList(this.fields));
      }

      public Descriptors.FileDescriptor getFile() {
         return this.file;
      }

      public String getFullName() {
         return this.fullName;
      }

      public int getIndex() {
         return this.index;
      }

      public String getName() {
         return this.proto.getName();
      }

      public List getNestedTypes() {
         return Collections.unmodifiableList(Arrays.asList(this.nestedTypes));
      }

      public DescriptorProtos.MessageOptions getOptions() {
         return this.proto.getOptions();
      }

      public boolean isExtensionNumber(int var1) {
         Iterator var2 = this.proto.getExtensionRangeList().iterator();

         DescriptorProtos.DescriptorProto.ExtensionRange var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (DescriptorProtos.DescriptorProto.ExtensionRange)var2.next();
         } while(var3.getStart() > var1 || var1 >= var3.getEnd());

         return true;
      }

      public DescriptorProtos.DescriptorProto toProto() {
         return this.proto;
      }
   }

   private static final class DescriptorPool {
      // $FF: synthetic field
      static final boolean $assertionsDisabled = false;
      private final Set dependencies = new HashSet();
      private final Map descriptorsByName = new HashMap();
      private final Map enumValuesByNumber = new HashMap();
      private final Map fieldsByNumber = new HashMap();

      DescriptorPool(Descriptors.FileDescriptor[] var1) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.dependencies.add(var1[var2]);
            this.importPublicDependencies(var1[var2]);
         }

         Iterator var5 = this.dependencies.iterator();

         while(var5.hasNext()) {
            Descriptors.FileDescriptor var3 = (Descriptors.FileDescriptor)var5.next();

            try {
               this.addPackage(var3.getPackage(), var3);
            } catch (Descriptors.DescriptorValidationException var4) {
            }
         }

      }

      private void importPublicDependencies(Descriptors.FileDescriptor var1) {
         Iterator var3 = var1.getPublicDependencies().iterator();

         while(var3.hasNext()) {
            Descriptors.FileDescriptor var2 = (Descriptors.FileDescriptor)var3.next();
            if (this.dependencies.add(var2)) {
               this.importPublicDependencies(var2);
            }
         }

      }

      static void validateSymbolName(Descriptors.GenericDescriptor var0) throws Descriptors.DescriptorValidationException {
         String var5 = var0.getName();
         if (var5.length() == 0) {
            throw new Descriptors.DescriptorValidationException(var0, "Missing name.");
         } else {
            boolean var2 = true;

            for(int var4 = 0; var4 < var5.length(); ++var4) {
               char var1 = var5.charAt(var4);
               boolean var3 = var2;
               if (var1 >= 128) {
                  var3 = false;
               }

               var2 = var3;
               if (!Character.isLetter(var1)) {
                  var2 = var3;
                  if (var1 != '_') {
                     if (Character.isDigit(var1) && var4 > 0) {
                        var2 = var3;
                     } else {
                        var2 = false;
                     }
                  }
               }
            }

            if (!var2) {
               StringBuilder var6 = new StringBuilder();
               var6.append('"');
               var6.append(var5);
               var6.append("\" is not a valid identifier.");
               throw new Descriptors.DescriptorValidationException(var0, var6.toString());
            }
         }
      }

      void addEnumValueByNumber(Descriptors.EnumValueDescriptor var1) {
         Descriptors.DescriptorPool.DescriptorIntPair var2 = new Descriptors.DescriptorPool.DescriptorIntPair(var1.getType(), var1.getNumber());
         var1 = (Descriptors.EnumValueDescriptor)this.enumValuesByNumber.put(var2, var1);
         if (var1 != null) {
            this.enumValuesByNumber.put(var2, var1);
         }

      }

      void addFieldByNumber(Descriptors.FieldDescriptor var1) throws Descriptors.DescriptorValidationException {
         Descriptors.DescriptorPool.DescriptorIntPair var3 = new Descriptors.DescriptorPool.DescriptorIntPair(var1.getContainingType(), var1.getNumber());
         Descriptors.FieldDescriptor var2 = (Descriptors.FieldDescriptor)this.fieldsByNumber.put(var3, var1);
         if (var2 != null) {
            this.fieldsByNumber.put(var3, var2);
            StringBuilder var4 = new StringBuilder();
            var4.append("Field number ");
            var4.append(var1.getNumber());
            var4.append("has already been used in \"");
            var4.append(var1.getContainingType().getFullName());
            var4.append("\" by field \"");
            var4.append(var2.getName());
            var4.append("\".");
            throw new Descriptors.DescriptorValidationException(var1, var4.toString());
         }
      }

      void addPackage(String var1, Descriptors.FileDescriptor var2) throws Descriptors.DescriptorValidationException {
         int var3 = var1.lastIndexOf(46);
         String var4;
         if (var3 == -1) {
            var4 = var1;
         } else {
            this.addPackage(var1.substring(0, var3), var2);
            var4 = var1.substring(var3 + 1);
         }

         Descriptors.GenericDescriptor var5 = (Descriptors.GenericDescriptor)this.descriptorsByName.put(var1, new Descriptors.DescriptorPool.PackageDescriptor(var4, var1, var2));
         if (var5 != null) {
            this.descriptorsByName.put(var1, var5);
            if (!(var5 instanceof Descriptors.DescriptorPool.PackageDescriptor)) {
               StringBuilder var6 = new StringBuilder();
               var6.append('"');
               var6.append(var4);
               var6.append("\" is already defined (as something other than a ");
               var6.append("package) in file \"");
               var6.append(var5.getFile().getName());
               var6.append("\".");
               throw new Descriptors.DescriptorValidationException(var2, var6.toString());
            }
         }
      }

      void addSymbol(Descriptors.GenericDescriptor var1) throws Descriptors.DescriptorValidationException {
         validateSymbolName(var1);
         String var3 = var1.getFullName();
         int var2 = var3.lastIndexOf(46);
         Descriptors.GenericDescriptor var4 = (Descriptors.GenericDescriptor)this.descriptorsByName.put(var3, var1);
         if (var4 != null) {
            this.descriptorsByName.put(var3, var4);
            if (var1.getFile() == var4.getFile()) {
               StringBuilder var6;
               if (var2 == -1) {
                  var6 = new StringBuilder();
                  var6.append('"');
                  var6.append(var3);
                  var6.append("\" is already defined.");
                  throw new Descriptors.DescriptorValidationException(var1, var6.toString());
               } else {
                  var6 = new StringBuilder();
                  var6.append('"');
                  var6.append(var3.substring(var2 + 1));
                  var6.append("\" is already defined in \"");
                  var6.append(var3.substring(0, var2));
                  var6.append("\".");
                  throw new Descriptors.DescriptorValidationException(var1, var6.toString());
               }
            } else {
               StringBuilder var5 = new StringBuilder();
               var5.append('"');
               var5.append(var3);
               var5.append("\" is already defined in file \"");
               var5.append(var4.getFile().getName());
               var5.append("\".");
               throw new Descriptors.DescriptorValidationException(var1, var5.toString());
            }
         }
      }

      Descriptors.GenericDescriptor findSymbol(String var1) {
         return this.findSymbol(var1, Descriptors.DescriptorPool.SearchFilter.ALL_SYMBOLS);
      }

      Descriptors.GenericDescriptor findSymbol(String var1, Descriptors.DescriptorPool.SearchFilter var2) {
         Descriptors.GenericDescriptor var3 = (Descriptors.GenericDescriptor)this.descriptorsByName.get(var1);
         if (var3 == null || var2 != Descriptors.DescriptorPool.SearchFilter.ALL_SYMBOLS && (var2 != Descriptors.DescriptorPool.SearchFilter.TYPES_ONLY || !this.isType(var3)) && (var2 != Descriptors.DescriptorPool.SearchFilter.AGGREGATES_ONLY || !this.isAggregate(var3))) {
            Iterator var5 = this.dependencies.iterator();

            Descriptors.GenericDescriptor var4;
            do {
               do {
                  if (!var5.hasNext()) {
                     return null;
                  }

                  var4 = (Descriptors.GenericDescriptor)((Descriptors.FileDescriptor)var5.next()).pool.descriptorsByName.get(var1);
               } while(var4 == null);
            } while(var2 != Descriptors.DescriptorPool.SearchFilter.ALL_SYMBOLS && (var2 != Descriptors.DescriptorPool.SearchFilter.TYPES_ONLY || !this.isType(var4)) && (var2 != Descriptors.DescriptorPool.SearchFilter.AGGREGATES_ONLY || !this.isAggregate(var4)));

            return var4;
         } else {
            return var3;
         }
      }

      boolean isAggregate(Descriptors.GenericDescriptor var1) {
         return var1 instanceof Descriptors.Descriptor || var1 instanceof Descriptors.EnumDescriptor || var1 instanceof Descriptors.DescriptorPool.PackageDescriptor || var1 instanceof Descriptors.ServiceDescriptor;
      }

      boolean isType(Descriptors.GenericDescriptor var1) {
         return var1 instanceof Descriptors.Descriptor || var1 instanceof Descriptors.EnumDescriptor;
      }

      Descriptors.GenericDescriptor lookupSymbol(String var1, Descriptors.GenericDescriptor var2, Descriptors.DescriptorPool.SearchFilter var3) throws Descriptors.DescriptorValidationException {
         Descriptors.GenericDescriptor var9;
         if (var1.startsWith(".")) {
            var9 = this.findSymbol(var1.substring(1), var3);
         } else {
            int var4 = var1.indexOf(46);
            String var6;
            if (var4 == -1) {
               var6 = var1;
            } else {
               var6 = var1.substring(0, var4);
            }

            StringBuilder var8 = new StringBuilder(var2.getFullName());

            while(true) {
               int var5 = var8.lastIndexOf(".");
               if (var5 == -1) {
                  var9 = this.findSymbol(var1, var3);
                  break;
               }

               var8.setLength(var5 + 1);
               var8.append(var6);
               Descriptors.GenericDescriptor var7 = this.findSymbol(var8.toString(), Descriptors.DescriptorPool.SearchFilter.AGGREGATES_ONLY);
               if (var7 != null) {
                  if (var4 != -1) {
                     var8.setLength(var5 + 1);
                     var8.append(var1);
                     var9 = this.findSymbol(var8.toString(), var3);
                  } else {
                     var9 = var7;
                  }
                  break;
               }

               var8.setLength(var5);
            }
         }

         if (var9 != null) {
            return var9;
         } else {
            StringBuilder var10 = new StringBuilder();
            var10.append('"');
            var10.append(var1);
            var10.append("\" is not defined.");
            throw new Descriptors.DescriptorValidationException(var2, var10.toString());
         }
      }

      private static final class DescriptorIntPair {
         private final Descriptors.GenericDescriptor descriptor;
         private final int number;

         DescriptorIntPair(Descriptors.GenericDescriptor var1, int var2) {
            this.descriptor = var1;
            this.number = var2;
         }

         public boolean equals(Object var1) {
            boolean var2 = var1 instanceof Descriptors.DescriptorPool.DescriptorIntPair;
            boolean var3 = false;
            if (!var2) {
               return false;
            } else {
               Descriptors.DescriptorPool.DescriptorIntPair var4 = (Descriptors.DescriptorPool.DescriptorIntPair)var1;
               var2 = var3;
               if (this.descriptor == var4.descriptor) {
                  var2 = var3;
                  if (this.number == var4.number) {
                     var2 = true;
                  }
               }

               return var2;
            }
         }

         public int hashCode() {
            return this.descriptor.hashCode() * '\uffff' + this.number;
         }
      }

      private static final class PackageDescriptor implements Descriptors.GenericDescriptor {
         private final Descriptors.FileDescriptor file;
         private final String fullName;
         private final String name;

         PackageDescriptor(String var1, String var2, Descriptors.FileDescriptor var3) {
            this.file = var3;
            this.fullName = var2;
            this.name = var1;
         }

         public Descriptors.FileDescriptor getFile() {
            return this.file;
         }

         public String getFullName() {
            return this.fullName;
         }

         public String getName() {
            return this.name;
         }

         public Message toProto() {
            return this.file.toProto();
         }
      }

      static enum SearchFilter {
         AGGREGATES_ONLY,
         ALL_SYMBOLS,
         TYPES_ONLY;

         static {
            Descriptors.DescriptorPool.SearchFilter var0 = new Descriptors.DescriptorPool.SearchFilter("ALL_SYMBOLS", 2);
            ALL_SYMBOLS = var0;
         }
      }
   }

   public static class DescriptorValidationException extends Exception {
      private static final long serialVersionUID = 5750205775490483148L;
      private final String description;
      private final String name;
      private final Message proto;

      private DescriptorValidationException(Descriptors.FileDescriptor var1, String var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getName());
         var3.append(": ");
         var3.append(var2);
         super(var3.toString());
         this.name = var1.getName();
         this.proto = var1.toProto();
         this.description = var2;
      }

      // $FF: synthetic method
      DescriptorValidationException(Descriptors.FileDescriptor var1, String var2, Object var3) {
         this(var1, var2);
      }

      private DescriptorValidationException(Descriptors.GenericDescriptor var1, String var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getFullName());
         var3.append(": ");
         var3.append(var2);
         super(var3.toString());
         this.name = var1.getFullName();
         this.proto = var1.toProto();
         this.description = var2;
      }

      // $FF: synthetic method
      DescriptorValidationException(Descriptors.GenericDescriptor var1, String var2, Object var3) {
         this(var1, var2);
      }

      private DescriptorValidationException(Descriptors.GenericDescriptor var1, String var2, Throwable var3) {
         this(var1, var2);
         this.initCause(var3);
      }

      // $FF: synthetic method
      DescriptorValidationException(Descriptors.GenericDescriptor var1, String var2, Throwable var3, Object var4) {
         this(var1, var2, var3);
      }

      public String getDescription() {
         return this.description;
      }

      public Message getProblemProto() {
         return this.proto;
      }

      public String getProblemSymbolName() {
         return this.name;
      }
   }

   public static final class EnumDescriptor implements Descriptors.GenericDescriptor, Internal.EnumLiteMap {
      private final Descriptors.Descriptor containingType;
      private final Descriptors.FileDescriptor file;
      private final String fullName;
      private final int index;
      private DescriptorProtos.EnumDescriptorProto proto;
      private Descriptors.EnumValueDescriptor[] values;

      private EnumDescriptor(DescriptorProtos.EnumDescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.Descriptor var3, int var4) throws Descriptors.DescriptorValidationException {
         this.index = var4;
         this.proto = var1;
         this.fullName = Descriptors.computeFullName(var2, var3, var1.getName());
         this.file = var2;
         this.containingType = var3;
         if (var1.getValueCount() == 0) {
            throw new Descriptors.DescriptorValidationException(this, "Enums must contain at least one value.");
         } else {
            this.values = new Descriptors.EnumValueDescriptor[var1.getValueCount()];

            for(var4 = 0; var4 < var1.getValueCount(); ++var4) {
               this.values[var4] = new Descriptors.EnumValueDescriptor(var1.getValue(var4), var2, this, var4);
            }

            var2.pool.addSymbol(this);
         }
      }

      // $FF: synthetic method
      EnumDescriptor(DescriptorProtos.EnumDescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.Descriptor var3, int var4, Object var5) throws Descriptors.DescriptorValidationException {
         this(var1, var2, var3, var4);
      }

      private void setProto(DescriptorProtos.EnumDescriptorProto var1) {
         this.proto = var1;
         int var2 = 0;

         while(true) {
            Descriptors.EnumValueDescriptor[] var3 = this.values;
            if (var2 >= var3.length) {
               return;
            }

            var3[var2].setProto(var1.getValue(var2));
            ++var2;
         }
      }

      public Descriptors.EnumValueDescriptor findValueByName(String var1) {
         Descriptors.DescriptorPool var2 = this.file.pool;
         StringBuilder var3 = new StringBuilder();
         var3.append(this.fullName);
         var3.append('.');
         var3.append(var1);
         Descriptors.GenericDescriptor var4 = var2.findSymbol(var3.toString());
         return var4 != null && var4 instanceof Descriptors.EnumValueDescriptor ? (Descriptors.EnumValueDescriptor)var4 : null;
      }

      public Descriptors.EnumValueDescriptor findValueByNumber(int var1) {
         return (Descriptors.EnumValueDescriptor)this.file.pool.enumValuesByNumber.get(new Descriptors.DescriptorPool.DescriptorIntPair(this, var1));
      }

      public Descriptors.Descriptor getContainingType() {
         return this.containingType;
      }

      public Descriptors.FileDescriptor getFile() {
         return this.file;
      }

      public String getFullName() {
         return this.fullName;
      }

      public int getIndex() {
         return this.index;
      }

      public String getName() {
         return this.proto.getName();
      }

      public DescriptorProtos.EnumOptions getOptions() {
         return this.proto.getOptions();
      }

      public List getValues() {
         return Collections.unmodifiableList(Arrays.asList(this.values));
      }

      public DescriptorProtos.EnumDescriptorProto toProto() {
         return this.proto;
      }
   }

   public static final class EnumValueDescriptor implements Descriptors.GenericDescriptor, Internal.EnumLite {
      private final Descriptors.FileDescriptor file;
      private final String fullName;
      private final int index;
      private DescriptorProtos.EnumValueDescriptorProto proto;
      private final Descriptors.EnumDescriptor type;

      private EnumValueDescriptor(DescriptorProtos.EnumValueDescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.EnumDescriptor var3, int var4) throws Descriptors.DescriptorValidationException {
         this.index = var4;
         this.proto = var1;
         this.file = var2;
         this.type = var3;
         StringBuilder var5 = new StringBuilder();
         var5.append(var3.getFullName());
         var5.append('.');
         var5.append(var1.getName());
         this.fullName = var5.toString();
         var2.pool.addSymbol(this);
         var2.pool.addEnumValueByNumber(this);
      }

      // $FF: synthetic method
      EnumValueDescriptor(DescriptorProtos.EnumValueDescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.EnumDescriptor var3, int var4, Object var5) throws Descriptors.DescriptorValidationException {
         this(var1, var2, var3, var4);
      }

      private void setProto(DescriptorProtos.EnumValueDescriptorProto var1) {
         this.proto = var1;
      }

      public Descriptors.FileDescriptor getFile() {
         return this.file;
      }

      public String getFullName() {
         return this.fullName;
      }

      public int getIndex() {
         return this.index;
      }

      public String getName() {
         return this.proto.getName();
      }

      public int getNumber() {
         return this.proto.getNumber();
      }

      public DescriptorProtos.EnumValueOptions getOptions() {
         return this.proto.getOptions();
      }

      public Descriptors.EnumDescriptor getType() {
         return this.type;
      }

      public DescriptorProtos.EnumValueDescriptorProto toProto() {
         return this.proto;
      }
   }

   public static final class FieldDescriptor implements Descriptors.GenericDescriptor, Comparable, FieldSet.FieldDescriptorLite {
      private static final WireFormat.FieldType[] table = WireFormat.FieldType.values();
      private Descriptors.Descriptor containingType;
      private Object defaultValue;
      private Descriptors.EnumDescriptor enumType;
      private final Descriptors.Descriptor extensionScope;
      private final Descriptors.FileDescriptor file;
      private final String fullName;
      private final int index;
      private Descriptors.Descriptor messageType;
      private DescriptorProtos.FieldDescriptorProto proto;
      private Descriptors.FieldDescriptor.Type type;

      static {
         if (Descriptors.FieldDescriptor.Type.values().length != DescriptorProtos.FieldDescriptorProto.Type.values().length) {
            throw new RuntimeException("descriptor.proto has a new declared type but Desrciptors.java wasn't updated.");
         }
      }

      private FieldDescriptor(DescriptorProtos.FieldDescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.Descriptor var3, int var4, boolean var5) throws Descriptors.DescriptorValidationException {
         this.index = var4;
         this.proto = var1;
         this.fullName = Descriptors.computeFullName(var2, var3, var1.getName());
         this.file = var2;
         if (var1.hasType()) {
            this.type = Descriptors.FieldDescriptor.Type.valueOf(var1.getType());
         }

         if (this.getNumber() > 0) {
            if (var1.getOptions().getPacked() && !this.isPackable()) {
               throw new Descriptors.DescriptorValidationException(this, "[packed = true] can only be specified for repeated primitive fields.");
            } else {
               if (var5) {
                  if (!var1.hasExtendee()) {
                     throw new Descriptors.DescriptorValidationException(this, "FieldDescriptorProto.extendee not set for extension field.");
                  }

                  this.containingType = null;
                  if (var3 != null) {
                     this.extensionScope = var3;
                  } else {
                     this.extensionScope = null;
                  }
               } else {
                  if (var1.hasExtendee()) {
                     throw new Descriptors.DescriptorValidationException(this, "FieldDescriptorProto.extendee set for non-extension field.");
                  }

                  this.containingType = var3;
                  this.extensionScope = null;
               }

               var2.pool.addSymbol(this);
            }
         } else {
            throw new Descriptors.DescriptorValidationException(this, "Field numbers must be positive integers.");
         }
      }

      // $FF: synthetic method
      FieldDescriptor(DescriptorProtos.FieldDescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.Descriptor var3, int var4, boolean var5, Object var6) throws Descriptors.DescriptorValidationException {
         this(var1, var2, var3, var4, var5);
      }

      private void crossLink() throws Descriptors.DescriptorValidationException {
         // $FF: Couldn't be decompiled
      }

      private void setProto(DescriptorProtos.FieldDescriptorProto var1) {
         this.proto = var1;
      }

      public int compareTo(Descriptors.FieldDescriptor var1) {
         if (var1.containingType == this.containingType) {
            return this.getNumber() - var1.getNumber();
         } else {
            throw new IllegalArgumentException("FieldDescriptors can only be compared to other FieldDescriptors for fields of the same message type.");
         }
      }

      public Descriptors.Descriptor getContainingType() {
         return this.containingType;
      }

      public Object getDefaultValue() {
         if (this.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE) {
            return this.defaultValue;
         } else {
            throw new UnsupportedOperationException("FieldDescriptor.getDefaultValue() called on an embedded message field.");
         }
      }

      public Descriptors.EnumDescriptor getEnumType() {
         if (this.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
            return this.enumType;
         } else {
            throw new UnsupportedOperationException("This field is not of enum type.");
         }
      }

      public Descriptors.Descriptor getExtensionScope() {
         if (this.isExtension()) {
            return this.extensionScope;
         } else {
            throw new UnsupportedOperationException("This field is not an extension.");
         }
      }

      public Descriptors.FileDescriptor getFile() {
         return this.file;
      }

      public String getFullName() {
         return this.fullName;
      }

      public int getIndex() {
         return this.index;
      }

      public Descriptors.FieldDescriptor.JavaType getJavaType() {
         return this.type.getJavaType();
      }

      public WireFormat.JavaType getLiteJavaType() {
         return this.getLiteType().getJavaType();
      }

      public WireFormat.FieldType getLiteType() {
         return table[this.type.ordinal()];
      }

      public Descriptors.Descriptor getMessageType() {
         if (this.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
            return this.messageType;
         } else {
            throw new UnsupportedOperationException("This field is not of message type.");
         }
      }

      public String getName() {
         return this.proto.getName();
      }

      public int getNumber() {
         return this.proto.getNumber();
      }

      public DescriptorProtos.FieldOptions getOptions() {
         return this.proto.getOptions();
      }

      public Descriptors.FieldDescriptor.Type getType() {
         return this.type;
      }

      public boolean hasDefaultValue() {
         return this.proto.hasDefaultValue();
      }

      public MessageLite.Builder internalMergeFrom(MessageLite.Builder var1, MessageLite var2) {
         return ((Message.Builder)var1).mergeFrom((Message)var2);
      }

      public boolean isExtension() {
         return this.proto.hasExtendee();
      }

      public boolean isOptional() {
         return this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
      }

      public boolean isPackable() {
         return this.isRepeated() && this.getLiteType().isPackable();
      }

      public boolean isPacked() {
         return this.getOptions().getPacked();
      }

      public boolean isRepeated() {
         return this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED;
      }

      public boolean isRequired() {
         return this.proto.getLabel() == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REQUIRED;
      }

      public DescriptorProtos.FieldDescriptorProto toProto() {
         return this.proto;
      }

      public static enum JavaType {
         BOOLEAN(false),
         BYTE_STRING(ByteString.EMPTY),
         DOUBLE(0.0D),
         ENUM((Object)null),
         FLOAT(0.0F),
         INT(0),
         LONG(0L),
         MESSAGE,
         STRING("");

         private final Object defaultDefault;

         static {
            Descriptors.FieldDescriptor.JavaType var0 = new Descriptors.FieldDescriptor.JavaType("MESSAGE", 8, (Object)null);
            MESSAGE = var0;
         }

         private JavaType(Object var3) {
            this.defaultDefault = var3;
         }

         // $FF: synthetic method
         static Object access$1700(Descriptors.FieldDescriptor.JavaType var0) {
            return var0.defaultDefault;
         }
      }

      public static enum Type {
         BOOL(Descriptors.FieldDescriptor.JavaType.BOOLEAN),
         BYTES(Descriptors.FieldDescriptor.JavaType.BYTE_STRING),
         DOUBLE(Descriptors.FieldDescriptor.JavaType.DOUBLE),
         ENUM(Descriptors.FieldDescriptor.JavaType.ENUM),
         FIXED32(Descriptors.FieldDescriptor.JavaType.INT),
         FIXED64(Descriptors.FieldDescriptor.JavaType.LONG),
         FLOAT(Descriptors.FieldDescriptor.JavaType.FLOAT),
         GROUP(Descriptors.FieldDescriptor.JavaType.MESSAGE),
         INT32(Descriptors.FieldDescriptor.JavaType.INT),
         INT64(Descriptors.FieldDescriptor.JavaType.LONG),
         MESSAGE(Descriptors.FieldDescriptor.JavaType.MESSAGE),
         SFIXED32(Descriptors.FieldDescriptor.JavaType.INT),
         SFIXED64(Descriptors.FieldDescriptor.JavaType.LONG),
         SINT32(Descriptors.FieldDescriptor.JavaType.INT),
         SINT64,
         STRING(Descriptors.FieldDescriptor.JavaType.STRING),
         UINT32(Descriptors.FieldDescriptor.JavaType.INT),
         UINT64(Descriptors.FieldDescriptor.JavaType.LONG);

         private Descriptors.FieldDescriptor.JavaType javaType;

         static {
            Descriptors.FieldDescriptor.Type var0 = new Descriptors.FieldDescriptor.Type("SINT64", 17, Descriptors.FieldDescriptor.JavaType.LONG);
            SINT64 = var0;
         }

         private Type(Descriptors.FieldDescriptor.JavaType var3) {
            this.javaType = var3;
         }

         public static Descriptors.FieldDescriptor.Type valueOf(DescriptorProtos.FieldDescriptorProto.Type var0) {
            return values()[var0.getNumber() - 1];
         }

         public Descriptors.FieldDescriptor.JavaType getJavaType() {
            return this.javaType;
         }

         public DescriptorProtos.FieldDescriptorProto.Type toProto() {
            return DescriptorProtos.FieldDescriptorProto.Type.valueOf(this.ordinal() + 1);
         }
      }
   }

   public static final class FileDescriptor {
      private final Descriptors.FileDescriptor[] dependencies;
      private final Descriptors.EnumDescriptor[] enumTypes;
      private final Descriptors.FieldDescriptor[] extensions;
      private final Descriptors.Descriptor[] messageTypes;
      private final Descriptors.DescriptorPool pool;
      private DescriptorProtos.FileDescriptorProto proto;
      private final Descriptors.FileDescriptor[] publicDependencies;
      private final Descriptors.ServiceDescriptor[] services;

      private FileDescriptor(DescriptorProtos.FileDescriptorProto var1, Descriptors.FileDescriptor[] var2, Descriptors.DescriptorPool var3) throws Descriptors.DescriptorValidationException {
         this.pool = var3;
         this.proto = var1;
         this.dependencies = (Descriptors.FileDescriptor[])var2.clone();
         this.publicDependencies = new Descriptors.FileDescriptor[var1.getPublicDependencyCount()];
         int var4 = 0;

         while(true) {
            if (var4 >= var1.getPublicDependencyCount()) {
               var3.addPackage(this.getPackage(), this);
               this.messageTypes = new Descriptors.Descriptor[var1.getMessageTypeCount()];

               for(var4 = 0; var4 < var1.getMessageTypeCount(); ++var4) {
                  this.messageTypes[var4] = new Descriptors.Descriptor(var1.getMessageType(var4), this, (Descriptors.Descriptor)null, var4);
               }

               this.enumTypes = new Descriptors.EnumDescriptor[var1.getEnumTypeCount()];

               for(var4 = 0; var4 < var1.getEnumTypeCount(); ++var4) {
                  this.enumTypes[var4] = new Descriptors.EnumDescriptor(var1.getEnumType(var4), this, (Descriptors.Descriptor)null, var4);
               }

               this.services = new Descriptors.ServiceDescriptor[var1.getServiceCount()];

               for(var4 = 0; var4 < var1.getServiceCount(); ++var4) {
                  this.services[var4] = new Descriptors.ServiceDescriptor(var1.getService(var4), this, var4);
               }

               this.extensions = new Descriptors.FieldDescriptor[var1.getExtensionCount()];

               for(var4 = 0; var4 < var1.getExtensionCount(); ++var4) {
                  this.extensions[var4] = new Descriptors.FieldDescriptor(var1.getExtension(var4), this, (Descriptors.Descriptor)null, var4, true);
               }

               return;
            }

            int var5 = var1.getPublicDependency(var4);
            if (var5 < 0) {
               break;
            }

            var2 = this.dependencies;
            if (var5 >= var2.length) {
               break;
            }

            this.publicDependencies[var4] = var2[var1.getPublicDependency(var4)];
            ++var4;
         }

         throw new Descriptors.DescriptorValidationException(this, "Invalid public dependency index.");
      }

      public static Descriptors.FileDescriptor buildFrom(DescriptorProtos.FileDescriptorProto var0, Descriptors.FileDescriptor[] var1) throws Descriptors.DescriptorValidationException {
         Descriptors.FileDescriptor var3 = new Descriptors.FileDescriptor(var0, var1, new Descriptors.DescriptorPool(var1));
         if (var1.length == var0.getDependencyCount()) {
            for(int var2 = 0; var2 < var0.getDependencyCount(); ++var2) {
               if (!var1[var2].getName().equals(var0.getDependency(var2))) {
                  throw new Descriptors.DescriptorValidationException(var3, "Dependencies passed to FileDescriptor.buildFrom() don't match those listed in the FileDescriptorProto.");
               }
            }

            var3.crossLink();
            return var3;
         } else {
            throw new Descriptors.DescriptorValidationException(var3, "Dependencies passed to FileDescriptor.buildFrom() don't match those listed in the FileDescriptorProto.");
         }
      }

      private void crossLink() throws Descriptors.DescriptorValidationException {
         Descriptors.Descriptor[] var3 = this.messageTypes;
         int var2 = var3.length;

         int var1;
         for(var1 = 0; var1 < var2; ++var1) {
            var3[var1].crossLink();
         }

         Descriptors.ServiceDescriptor[] var4 = this.services;
         var2 = var4.length;

         for(var1 = 0; var1 < var2; ++var1) {
            var4[var1].crossLink();
         }

         Descriptors.FieldDescriptor[] var5 = this.extensions;
         var2 = var5.length;

         for(var1 = 0; var1 < var2; ++var1) {
            var5[var1].crossLink();
         }

      }

      public static void internalBuildGeneratedFileFrom(String[] var0, Descriptors.FileDescriptor[] var1, Descriptors.FileDescriptor.InternalDescriptorAssigner var2) {
         StringBuilder var5 = new StringBuilder();
         int var4 = var0.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            var5.append(var0[var3]);
         }

         byte[] var14;
         try {
            var14 = var5.toString().getBytes("ISO-8859-1");
         } catch (UnsupportedEncodingException var9) {
            throw new RuntimeException("Standard encoding ISO-8859-1 not supported by JVM.", var9);
         }

         DescriptorProtos.FileDescriptorProto var10;
         try {
            var10 = DescriptorProtos.FileDescriptorProto.parseFrom(var14);
         } catch (InvalidProtocolBufferException var8) {
            throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", var8);
         }

         Descriptors.FileDescriptor var12;
         try {
            var12 = buildFrom(var10, var1);
         } catch (Descriptors.DescriptorValidationException var7) {
            StringBuilder var13 = new StringBuilder();
            var13.append("Invalid embedded descriptor for \"");
            var13.append(var10.getName());
            var13.append("\".");
            throw new IllegalArgumentException(var13.toString(), var7);
         }

         ExtensionRegistry var11 = var2.assignDescriptors(var12);
         if (var11 != null) {
            try {
               var10 = DescriptorProtos.FileDescriptorProto.parseFrom((byte[])var14, var11);
            } catch (InvalidProtocolBufferException var6) {
               throw new IllegalArgumentException("Failed to parse protocol buffer descriptor for generated code.", var6);
            }

            var12.setProto(var10);
         }
      }

      private void setProto(DescriptorProtos.FileDescriptorProto var1) {
         this.proto = var1;
         int var2 = 0;

         while(true) {
            Descriptors.Descriptor[] var3 = this.messageTypes;
            if (var2 >= var3.length) {
               var2 = 0;

               while(true) {
                  Descriptors.EnumDescriptor[] var4 = this.enumTypes;
                  if (var2 >= var4.length) {
                     var2 = 0;

                     while(true) {
                        Descriptors.ServiceDescriptor[] var5 = this.services;
                        if (var2 >= var5.length) {
                           var2 = 0;

                           while(true) {
                              Descriptors.FieldDescriptor[] var6 = this.extensions;
                              if (var2 >= var6.length) {
                                 return;
                              }

                              var6[var2].setProto(var1.getExtension(var2));
                              ++var2;
                           }
                        }

                        var5[var2].setProto(var1.getService(var2));
                        ++var2;
                     }
                  }

                  var4[var2].setProto(var1.getEnumType(var2));
                  ++var2;
               }
            }

            var3[var2].setProto(var1.getMessageType(var2));
            ++var2;
         }
      }

      public Descriptors.EnumDescriptor findEnumTypeByName(String var1) {
         if (var1.indexOf(46) != -1) {
            return null;
         } else {
            String var2 = var1;
            if (this.getPackage().length() > 0) {
               StringBuilder var4 = new StringBuilder();
               var4.append(this.getPackage());
               var4.append('.');
               var4.append(var1);
               var2 = var4.toString();
            }

            Descriptors.GenericDescriptor var3 = this.pool.findSymbol(var2);
            return var3 != null && var3 instanceof Descriptors.EnumDescriptor && var3.getFile() == this ? (Descriptors.EnumDescriptor)var3 : null;
         }
      }

      public Descriptors.FieldDescriptor findExtensionByName(String var1) {
         if (var1.indexOf(46) != -1) {
            return null;
         } else {
            String var2 = var1;
            if (this.getPackage().length() > 0) {
               StringBuilder var4 = new StringBuilder();
               var4.append(this.getPackage());
               var4.append('.');
               var4.append(var1);
               var2 = var4.toString();
            }

            Descriptors.GenericDescriptor var3 = this.pool.findSymbol(var2);
            return var3 != null && var3 instanceof Descriptors.FieldDescriptor && var3.getFile() == this ? (Descriptors.FieldDescriptor)var3 : null;
         }
      }

      public Descriptors.Descriptor findMessageTypeByName(String var1) {
         if (var1.indexOf(46) != -1) {
            return null;
         } else {
            String var2 = var1;
            if (this.getPackage().length() > 0) {
               StringBuilder var4 = new StringBuilder();
               var4.append(this.getPackage());
               var4.append('.');
               var4.append(var1);
               var2 = var4.toString();
            }

            Descriptors.GenericDescriptor var3 = this.pool.findSymbol(var2);
            return var3 != null && var3 instanceof Descriptors.Descriptor && var3.getFile() == this ? (Descriptors.Descriptor)var3 : null;
         }
      }

      public Descriptors.ServiceDescriptor findServiceByName(String var1) {
         if (var1.indexOf(46) != -1) {
            return null;
         } else {
            String var2 = var1;
            if (this.getPackage().length() > 0) {
               StringBuilder var4 = new StringBuilder();
               var4.append(this.getPackage());
               var4.append('.');
               var4.append(var1);
               var2 = var4.toString();
            }

            Descriptors.GenericDescriptor var3 = this.pool.findSymbol(var2);
            return var3 != null && var3 instanceof Descriptors.ServiceDescriptor && var3.getFile() == this ? (Descriptors.ServiceDescriptor)var3 : null;
         }
      }

      public List getDependencies() {
         return Collections.unmodifiableList(Arrays.asList(this.dependencies));
      }

      public List getEnumTypes() {
         return Collections.unmodifiableList(Arrays.asList(this.enumTypes));
      }

      public List getExtensions() {
         return Collections.unmodifiableList(Arrays.asList(this.extensions));
      }

      public List getMessageTypes() {
         return Collections.unmodifiableList(Arrays.asList(this.messageTypes));
      }

      public String getName() {
         return this.proto.getName();
      }

      public DescriptorProtos.FileOptions getOptions() {
         return this.proto.getOptions();
      }

      public String getPackage() {
         return this.proto.getPackage();
      }

      public List getPublicDependencies() {
         return Collections.unmodifiableList(Arrays.asList(this.publicDependencies));
      }

      public List getServices() {
         return Collections.unmodifiableList(Arrays.asList(this.services));
      }

      public DescriptorProtos.FileDescriptorProto toProto() {
         return this.proto;
      }

      public interface InternalDescriptorAssigner {
         ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor var1);
      }
   }

   private interface GenericDescriptor {
      Descriptors.FileDescriptor getFile();

      String getFullName();

      String getName();

      Message toProto();
   }

   public static final class MethodDescriptor implements Descriptors.GenericDescriptor {
      private final Descriptors.FileDescriptor file;
      private final String fullName;
      private final int index;
      private Descriptors.Descriptor inputType;
      private Descriptors.Descriptor outputType;
      private DescriptorProtos.MethodDescriptorProto proto;
      private final Descriptors.ServiceDescriptor service;

      private MethodDescriptor(DescriptorProtos.MethodDescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.ServiceDescriptor var3, int var4) throws Descriptors.DescriptorValidationException {
         this.index = var4;
         this.proto = var1;
         this.file = var2;
         this.service = var3;
         StringBuilder var5 = new StringBuilder();
         var5.append(var3.getFullName());
         var5.append('.');
         var5.append(var1.getName());
         this.fullName = var5.toString();
         var2.pool.addSymbol(this);
      }

      // $FF: synthetic method
      MethodDescriptor(DescriptorProtos.MethodDescriptorProto var1, Descriptors.FileDescriptor var2, Descriptors.ServiceDescriptor var3, int var4, Object var5) throws Descriptors.DescriptorValidationException {
         this(var1, var2, var3, var4);
      }

      private void crossLink() throws Descriptors.DescriptorValidationException {
         Descriptors.GenericDescriptor var1 = this.file.pool.lookupSymbol(this.proto.getInputType(), this, Descriptors.DescriptorPool.SearchFilter.TYPES_ONLY);
         StringBuilder var2;
         if (var1 instanceof Descriptors.Descriptor) {
            this.inputType = (Descriptors.Descriptor)var1;
            var1 = this.file.pool.lookupSymbol(this.proto.getOutputType(), this, Descriptors.DescriptorPool.SearchFilter.TYPES_ONLY);
            if (var1 instanceof Descriptors.Descriptor) {
               this.outputType = (Descriptors.Descriptor)var1;
            } else {
               var2 = new StringBuilder();
               var2.append('"');
               var2.append(this.proto.getOutputType());
               var2.append("\" is not a message type.");
               throw new Descriptors.DescriptorValidationException(this, var2.toString());
            }
         } else {
            var2 = new StringBuilder();
            var2.append('"');
            var2.append(this.proto.getInputType());
            var2.append("\" is not a message type.");
            throw new Descriptors.DescriptorValidationException(this, var2.toString());
         }
      }

      private void setProto(DescriptorProtos.MethodDescriptorProto var1) {
         this.proto = var1;
      }

      public Descriptors.FileDescriptor getFile() {
         return this.file;
      }

      public String getFullName() {
         return this.fullName;
      }

      public int getIndex() {
         return this.index;
      }

      public Descriptors.Descriptor getInputType() {
         return this.inputType;
      }

      public String getName() {
         return this.proto.getName();
      }

      public DescriptorProtos.MethodOptions getOptions() {
         return this.proto.getOptions();
      }

      public Descriptors.Descriptor getOutputType() {
         return this.outputType;
      }

      public Descriptors.ServiceDescriptor getService() {
         return this.service;
      }

      public DescriptorProtos.MethodDescriptorProto toProto() {
         return this.proto;
      }
   }

   public static final class ServiceDescriptor implements Descriptors.GenericDescriptor {
      private final Descriptors.FileDescriptor file;
      private final String fullName;
      private final int index;
      private Descriptors.MethodDescriptor[] methods;
      private DescriptorProtos.ServiceDescriptorProto proto;

      private ServiceDescriptor(DescriptorProtos.ServiceDescriptorProto var1, Descriptors.FileDescriptor var2, int var3) throws Descriptors.DescriptorValidationException {
         this.index = var3;
         this.proto = var1;
         this.fullName = Descriptors.computeFullName(var2, (Descriptors.Descriptor)null, var1.getName());
         this.file = var2;
         this.methods = new Descriptors.MethodDescriptor[var1.getMethodCount()];

         for(var3 = 0; var3 < var1.getMethodCount(); ++var3) {
            this.methods[var3] = new Descriptors.MethodDescriptor(var1.getMethod(var3), var2, this, var3);
         }

         var2.pool.addSymbol(this);
      }

      // $FF: synthetic method
      ServiceDescriptor(DescriptorProtos.ServiceDescriptorProto var1, Descriptors.FileDescriptor var2, int var3, Object var4) throws Descriptors.DescriptorValidationException {
         this(var1, var2, var3);
      }

      private void crossLink() throws Descriptors.DescriptorValidationException {
         Descriptors.MethodDescriptor[] var3 = this.methods;
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var3[var1].crossLink();
         }

      }

      private void setProto(DescriptorProtos.ServiceDescriptorProto var1) {
         this.proto = var1;
         int var2 = 0;

         while(true) {
            Descriptors.MethodDescriptor[] var3 = this.methods;
            if (var2 >= var3.length) {
               return;
            }

            var3[var2].setProto(var1.getMethod(var2));
            ++var2;
         }
      }

      public Descriptors.MethodDescriptor findMethodByName(String var1) {
         Descriptors.DescriptorPool var2 = this.file.pool;
         StringBuilder var3 = new StringBuilder();
         var3.append(this.fullName);
         var3.append('.');
         var3.append(var1);
         Descriptors.GenericDescriptor var4 = var2.findSymbol(var3.toString());
         return var4 != null && var4 instanceof Descriptors.MethodDescriptor ? (Descriptors.MethodDescriptor)var4 : null;
      }

      public Descriptors.FileDescriptor getFile() {
         return this.file;
      }

      public String getFullName() {
         return this.fullName;
      }

      public int getIndex() {
         return this.index;
      }

      public List getMethods() {
         return Collections.unmodifiableList(Arrays.asList(this.methods));
      }

      public String getName() {
         return this.proto.getName();
      }

      public DescriptorProtos.ServiceOptions getOptions() {
         return this.proto.getOptions();
      }

      public DescriptorProtos.ServiceDescriptorProto toProto() {
         return this.proto;
      }
   }
}
