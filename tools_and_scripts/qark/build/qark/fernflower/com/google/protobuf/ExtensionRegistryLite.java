package com.google.protobuf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExtensionRegistryLite {
   private static final ExtensionRegistryLite EMPTY = new ExtensionRegistryLite(true);
   private static volatile boolean eagerlyParseMessageSets = false;
   private final Map extensionsByNumber;

   ExtensionRegistryLite() {
      this.extensionsByNumber = new HashMap();
   }

   ExtensionRegistryLite(ExtensionRegistryLite var1) {
      if (var1 == EMPTY) {
         this.extensionsByNumber = Collections.emptyMap();
      } else {
         this.extensionsByNumber = Collections.unmodifiableMap(var1.extensionsByNumber);
      }
   }

   private ExtensionRegistryLite(boolean var1) {
      this.extensionsByNumber = Collections.emptyMap();
   }

   public static ExtensionRegistryLite getEmptyRegistry() {
      return EMPTY;
   }

   public static boolean isEagerlyParseMessageSets() {
      return eagerlyParseMessageSets;
   }

   public static ExtensionRegistryLite newInstance() {
      return new ExtensionRegistryLite();
   }

   public static void setEagerlyParseMessageSets(boolean var0) {
      eagerlyParseMessageSets = var0;
   }

   public final void add(GeneratedMessageLite.GeneratedExtension var1) {
      this.extensionsByNumber.put(new ExtensionRegistryLite.ObjectIntPair(var1.getContainingTypeDefaultInstance(), var1.getNumber()), var1);
   }

   public GeneratedMessageLite.GeneratedExtension findLiteExtensionByNumber(MessageLite var1, int var2) {
      return (GeneratedMessageLite.GeneratedExtension)this.extensionsByNumber.get(new ExtensionRegistryLite.ObjectIntPair(var1, var2));
   }

   public ExtensionRegistryLite getUnmodifiable() {
      return new ExtensionRegistryLite(this);
   }

   private static final class ObjectIntPair {
      private final int number;
      private final Object object;

      ObjectIntPair(Object var1, int var2) {
         this.object = var1;
         this.number = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = var1 instanceof ExtensionRegistryLite.ObjectIntPair;
         boolean var3 = false;
         if (!var2) {
            return false;
         } else {
            ExtensionRegistryLite.ObjectIntPair var4 = (ExtensionRegistryLite.ObjectIntPair)var1;
            var2 = var3;
            if (this.object == var4.object) {
               var2 = var3;
               if (this.number == var4.number) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      public int hashCode() {
         return System.identityHashCode(this.object) * '\uffff' + this.number;
      }
   }
}
