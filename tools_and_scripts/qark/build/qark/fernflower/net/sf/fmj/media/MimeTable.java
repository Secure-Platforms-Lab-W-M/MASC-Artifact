package net.sf.fmj.media;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MimeTable {
   private static final Hashtable reverseHashTable = new Hashtable();
   private final Hashtable hashTable = new Hashtable();

   public boolean addMimeType(String var1, String var2) {
      this.hashTable.put(var1, var2);
      reverseHashTable.put(var2, var1);
      return true;
   }

   public void clear() {
      this.hashTable.clear();
      reverseHashTable.clear();
   }

   public String getDefaultExtension(String var1) {
      return (String)reverseHashTable.get(var1);
   }

   public List getExtensions(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.hashTable.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (((String)this.hashTable.get(var4)).equals(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public Hashtable getMimeTable() {
      Hashtable var1 = new Hashtable();
      var1.putAll(this.hashTable);
      return var1;
   }

   public String getMimeType(String var1) {
      return (String)this.hashTable.get(var1);
   }

   public Set getMimeTypes() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.hashTable.values().iterator();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

      return var1;
   }

   public boolean removeMimeType(String var1) {
      if (this.hashTable.get(var1) == null) {
         return false;
      } else {
         reverseHashTable.remove(this.hashTable.get(var1));
         this.hashTable.remove(var1);
         return true;
      }
   }
}
