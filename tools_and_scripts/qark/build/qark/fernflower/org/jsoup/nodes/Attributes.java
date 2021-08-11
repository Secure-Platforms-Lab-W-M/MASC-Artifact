package org.jsoup.nodes;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;

public class Attributes implements Iterable, Cloneable {
   protected static final String dataPrefix = "data-";
   private LinkedHashMap attributes = null;

   private static String dataKey(String var0) {
      return "data-" + var0;
   }

   public void addAll(Attributes var1) {
      if (var1.size() != 0) {
         if (this.attributes == null) {
            this.attributes = new LinkedHashMap(var1.size());
         }

         this.attributes.putAll(var1.attributes);
      }
   }

   public List asList() {
      if (this.attributes == null) {
         return Collections.emptyList();
      } else {
         ArrayList var1 = new ArrayList(this.attributes.size());
         Iterator var2 = this.attributes.entrySet().iterator();

         while(var2.hasNext()) {
            var1.add(((Entry)var2.next()).getValue());
         }

         return Collections.unmodifiableList(var1);
      }
   }

   public Attributes clone() {
      Attributes var1;
      if (this.attributes == null) {
         var1 = new Attributes();
      } else {
         Attributes var2;
         try {
            var2 = (Attributes)super.clone();
         } catch (CloneNotSupportedException var4) {
            throw new RuntimeException(var4);
         }

         var2.attributes = new LinkedHashMap(this.attributes.size());
         Iterator var3 = this.iterator();

         while(true) {
            var1 = var2;
            if (!var3.hasNext()) {
               break;
            }

            Attribute var5 = (Attribute)var3.next();
            var2.attributes.put(var5.getKey(), var5.clone());
         }
      }

      return var1;
   }

   public Map dataset() {
      return new Attributes.Dataset();
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof Attributes)) {
            return false;
         } else {
            Attributes var2 = (Attributes)var1;
            if (this.attributes != null) {
               if (this.attributes.equals(var2.attributes)) {
                  return true;
               }
            } else if (var2.attributes == null) {
               return true;
            }

            return false;
         }
      } else {
         return true;
      }
   }

   public String get(String var1) {
      Validate.notEmpty(var1);
      if (this.attributes == null) {
         return "";
      } else {
         Attribute var2 = (Attribute)this.attributes.get(var1.toLowerCase());
         return var2 != null ? var2.getValue() : "";
      }
   }

   public boolean hasKey(String var1) {
      return this.attributes != null && this.attributes.containsKey(var1.toLowerCase());
   }

   public int hashCode() {
      return this.attributes != null ? this.attributes.hashCode() : 0;
   }

   public String html() {
      StringBuilder var1 = new StringBuilder();

      try {
         this.html(var1, (new Document("")).outputSettings());
      } catch (IOException var2) {
         throw new SerializationException(var2);
      }

      return var1.toString();
   }

   void html(Appendable var1, Document.OutputSettings var2) throws IOException {
      if (this.attributes != null) {
         Iterator var3 = this.attributes.entrySet().iterator();

         while(var3.hasNext()) {
            Attribute var4 = (Attribute)((Entry)var3.next()).getValue();
            var1.append(" ");
            var4.html(var1, var2);
         }
      }

   }

   public Iterator iterator() {
      return this.asList().iterator();
   }

   public void put(String var1, String var2) {
      this.put(new Attribute(var1, var2));
   }

   public void put(String var1, boolean var2) {
      if (var2) {
         this.put(new BooleanAttribute(var1));
      } else {
         this.remove(var1);
      }
   }

   public void put(Attribute var1) {
      Validate.notNull(var1);
      if (this.attributes == null) {
         this.attributes = new LinkedHashMap(2);
      }

      this.attributes.put(var1.getKey(), var1);
   }

   public void remove(String var1) {
      Validate.notEmpty(var1);
      if (this.attributes != null) {
         this.attributes.remove(var1.toLowerCase());
      }
   }

   public int size() {
      return this.attributes == null ? 0 : this.attributes.size();
   }

   public String toString() {
      return this.html();
   }

   private class Dataset extends AbstractMap {
      private Dataset() {
         if (Attributes.this.attributes == null) {
            Attributes.this.attributes = new LinkedHashMap(2);
         }

      }

      // $FF: synthetic method
      Dataset(Object var2) {
         this();
      }

      public Set entrySet() {
         return new Attributes.Dataset.EntrySet();
      }

      public String put(String var1, String var2) {
         String var3 = Attributes.dataKey(var1);
         if (Attributes.this.hasKey(var3)) {
            var1 = ((Attribute)Attributes.this.attributes.get(var3)).getValue();
         } else {
            var1 = null;
         }

         Attribute var4 = new Attribute(var3, var2);
         Attributes.this.attributes.put(var3, var4);
         return var1;
      }

      private class DatasetIterator implements Iterator {
         private Attribute attr;
         private Iterator attrIter;

         private DatasetIterator() {
            this.attrIter = Attributes.this.attributes.values().iterator();
         }

         // $FF: synthetic method
         DatasetIterator(Object var2) {
            this();
         }

         public boolean hasNext() {
            while(true) {
               if (this.attrIter.hasNext()) {
                  this.attr = (Attribute)this.attrIter.next();
                  if (!this.attr.isDataAttribute()) {
                     continue;
                  }

                  return true;
               }

               return false;
            }
         }

         public Entry next() {
            return new Attribute(this.attr.getKey().substring("data-".length()), this.attr.getValue());
         }

         public void remove() {
            Attributes.this.attributes.remove(this.attr.getKey());
         }
      }

      private class EntrySet extends AbstractSet {
         private EntrySet() {
         }

         // $FF: synthetic method
         EntrySet(Object var2) {
            this();
         }

         public Iterator iterator() {
            return Dataset.this.new DatasetIterator();
         }

         public int size() {
            int var1 = 0;

            for(Attributes.Dataset.DatasetIterator var2 = Dataset.this.new DatasetIterator(); var2.hasNext(); ++var1) {
            }

            return var1;
         }
      }
   }
}
