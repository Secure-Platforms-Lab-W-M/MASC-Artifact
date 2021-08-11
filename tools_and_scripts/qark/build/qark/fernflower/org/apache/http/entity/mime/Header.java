package org.apache.http.entity.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Header implements Iterable {
   private final Map fieldMap = new HashMap();
   private final List fields = new LinkedList();

   public void addField(MinimalField var1) {
      if (var1 != null) {
         String var4 = var1.getName().toLowerCase(Locale.ROOT);
         List var3 = (List)this.fieldMap.get(var4);
         Object var2 = var3;
         if (var3 == null) {
            var2 = new LinkedList();
            this.fieldMap.put(var4, var2);
         }

         ((List)var2).add(var1);
         this.fields.add(var1);
      }
   }

   public MinimalField getField(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.toLowerCase(Locale.ROOT);
         List var2 = (List)this.fieldMap.get(var1);
         return var2 != null && !var2.isEmpty() ? (MinimalField)var2.get(0) : null;
      }
   }

   public List getFields() {
      return new ArrayList(this.fields);
   }

   public List getFields(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.toLowerCase(Locale.ROOT);
         List var2 = (List)this.fieldMap.get(var1);
         return (List)(var2 != null && !var2.isEmpty() ? new ArrayList(var2) : Collections.emptyList());
      }
   }

   public Iterator iterator() {
      return Collections.unmodifiableList(this.fields).iterator();
   }

   public int removeFields(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         var1 = var1.toLowerCase(Locale.ROOT);
         List var2 = (List)this.fieldMap.remove(var1);
         if (var2 != null) {
            if (var2.isEmpty()) {
               return 0;
            } else {
               this.fields.removeAll(var2);
               return var2.size();
            }
         } else {
            return 0;
         }
      }
   }

   public void setField(MinimalField var1) {
      if (var1 != null) {
         String var5 = var1.getName().toLowerCase(Locale.ROOT);
         List var6 = (List)this.fieldMap.get(var5);
         if (var6 != null && !var6.isEmpty()) {
            var6.clear();
            var6.add(var1);
            int var3 = -1;
            int var2 = 0;

            int var4;
            for(Iterator var7 = this.fields.iterator(); var7.hasNext(); var3 = var4) {
               var4 = var3;
               if (((MinimalField)var7.next()).getName().equalsIgnoreCase(var1.getName())) {
                  var7.remove();
                  var4 = var3;
                  if (var3 == -1) {
                     var4 = var2;
                  }
               }

               ++var2;
            }

            this.fields.add(var3, var1);
         } else {
            this.addField(var1);
         }
      }
   }

   public String toString() {
      return this.fields.toString();
   }
}
