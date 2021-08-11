package okhttp3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Headers {
   private final String[] namesAndValues;

   Headers(Headers.Builder var1) {
      this.namesAndValues = (String[])var1.namesAndValues.toArray(new String[var1.namesAndValues.size()]);
   }

   private Headers(String[] var1) {
      this.namesAndValues = var1;
   }

   private static String get(String[] var0, String var1) {
      for(int var2 = var0.length - 2; var2 >= 0; var2 -= 2) {
         if (var1.equalsIgnoreCase(var0[var2])) {
            return var0[var2 + 1];
         }
      }

      return null;
   }

   // $FF: renamed from: of (java.util.Map) okhttp3.Headers
   public static Headers method_23(Map var0) {
      if (var0 == null) {
         throw new NullPointerException("headers == null");
      } else {
         String[] var2 = new String[var0.size() * 2];
         int var1 = 0;
         Iterator var3 = var0.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if (var4.getKey() != null && var4.getValue() != null) {
               String var5 = ((String)var4.getKey()).trim();
               String var7 = ((String)var4.getValue()).trim();
               if (var5.length() != 0 && var5.indexOf(0) == -1 && var7.indexOf(0) == -1) {
                  var2[var1] = var5;
                  var2[var1 + 1] = var7;
                  var1 += 2;
                  continue;
               }

               StringBuilder var6 = new StringBuilder();
               var6.append("Unexpected header: ");
               var6.append(var5);
               var6.append(": ");
               var6.append(var7);
               throw new IllegalArgumentException(var6.toString());
            }

            throw new IllegalArgumentException("Headers cannot be null");
         }

         return new Headers(var2);
      }
   }

   // $FF: renamed from: of (java.lang.String[]) okhttp3.Headers
   public static Headers method_24(String... var0) {
      if (var0 == null) {
         throw new NullPointerException("namesAndValues == null");
      } else if (var0.length % 2 != 0) {
         throw new IllegalArgumentException("Expected alternating header names and values");
      } else {
         String[] var3 = (String[])var0.clone();

         int var1;
         for(var1 = 0; var1 < var3.length; ++var1) {
            if (var3[var1] == null) {
               throw new IllegalArgumentException("Headers cannot be null");
            }

            var3[var1] = var3[var1].trim();
         }

         for(var1 = 0; var1 < var3.length; var1 += 2) {
            String var4 = var3[var1];
            String var2 = var3[var1 + 1];
            if (var4.length() == 0 || var4.indexOf(0) != -1 || var2.indexOf(0) != -1) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Unexpected header: ");
               var5.append(var4);
               var5.append(": ");
               var5.append(var2);
               throw new IllegalArgumentException(var5.toString());
            }
         }

         return new Headers(var3);
      }
   }

   public boolean equals(@Nullable Object var1) {
      return var1 instanceof Headers && Arrays.equals(((Headers)var1).namesAndValues, this.namesAndValues);
   }

   @Nullable
   public String get(String var1) {
      return get(this.namesAndValues, var1);
   }

   @Nullable
   public Date getDate(String var1) {
      var1 = this.get(var1);
      return var1 != null ? HttpDate.parse(var1) : null;
   }

   public int hashCode() {
      return Arrays.hashCode(this.namesAndValues);
   }

   public String name(int var1) {
      return this.namesAndValues[var1 * 2];
   }

   public Set names() {
      TreeSet var3 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
      int var1 = 0;

      for(int var2 = this.size(); var1 < var2; ++var1) {
         var3.add(this.name(var1));
      }

      return Collections.unmodifiableSet(var3);
   }

   public Headers.Builder newBuilder() {
      Headers.Builder var1 = new Headers.Builder();
      Collections.addAll(var1.namesAndValues, this.namesAndValues);
      return var1;
   }

   public int size() {
      return this.namesAndValues.length / 2;
   }

   public Map toMultimap() {
      TreeMap var5 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      int var1 = 0;

      for(int var2 = this.size(); var1 < var2; ++var1) {
         String var6 = this.name(var1).toLowerCase(Locale.US);
         List var4 = (List)var5.get(var6);
         Object var3 = var4;
         if (var4 == null) {
            var3 = new ArrayList(2);
            var5.put(var6, var3);
         }

         ((List)var3).add(this.value(var1));
      }

      return var5;
   }

   public String toString() {
      StringBuilder var3 = new StringBuilder();
      int var1 = 0;

      for(int var2 = this.size(); var1 < var2; ++var1) {
         var3.append(this.name(var1));
         var3.append(": ");
         var3.append(this.value(var1));
         var3.append("\n");
      }

      return var3.toString();
   }

   public String value(int var1) {
      return this.namesAndValues[var1 * 2 + 1];
   }

   public List values(String var1) {
      ArrayList var4 = null;
      int var2 = 0;

      ArrayList var5;
      for(int var3 = this.size(); var2 < var3; var4 = var5) {
         var5 = var4;
         if (var1.equalsIgnoreCase(this.name(var2))) {
            var5 = var4;
            if (var4 == null) {
               var5 = new ArrayList(2);
            }

            var5.add(this.value(var2));
         }

         ++var2;
      }

      if (var4 != null) {
         return Collections.unmodifiableList(var4);
      } else {
         return Collections.emptyList();
      }
   }

   public static final class Builder {
      final List namesAndValues = new ArrayList(20);

      private void checkNameAndValue(String var1, String var2) {
         if (var1 == null) {
            throw new NullPointerException("name == null");
         } else if (var1.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
         } else {
            int var3 = 0;

            int var4;
            char var5;
            for(var4 = var1.length(); var3 < var4; ++var3) {
               var5 = var1.charAt(var3);
               if (var5 <= ' ' || var5 >= 127) {
                  throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", Integer.valueOf(var5), var3, var1));
               }
            }

            if (var2 == null) {
               StringBuilder var6 = new StringBuilder();
               var6.append("value for name ");
               var6.append(var1);
               var6.append(" == null");
               throw new NullPointerException(var6.toString());
            } else {
               var3 = 0;

               for(var4 = var2.length(); var3 < var4; ++var3) {
                  var5 = var2.charAt(var3);
                  if (var5 <= 31 && var5 != '\t' || var5 >= 127) {
                     throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", Integer.valueOf(var5), var3, var1, var2));
                  }
               }

            }
         }
      }

      public Headers.Builder add(String var1) {
         int var2 = var1.indexOf(":");
         if (var2 != -1) {
            return this.add(var1.substring(0, var2).trim(), var1.substring(var2 + 1));
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Unexpected header: ");
            var3.append(var1);
            throw new IllegalArgumentException(var3.toString());
         }
      }

      public Headers.Builder add(String var1, String var2) {
         this.checkNameAndValue(var1, var2);
         return this.addLenient(var1, var2);
      }

      Headers.Builder addLenient(String var1) {
         int var2 = var1.indexOf(":", 1);
         if (var2 != -1) {
            return this.addLenient(var1.substring(0, var2), var1.substring(var2 + 1));
         } else {
            return var1.startsWith(":") ? this.addLenient("", var1.substring(1)) : this.addLenient("", var1);
         }
      }

      Headers.Builder addLenient(String var1, String var2) {
         this.namesAndValues.add(var1);
         this.namesAndValues.add(var2.trim());
         return this;
      }

      public Headers build() {
         return new Headers(this);
      }

      public String get(String var1) {
         for(int var2 = this.namesAndValues.size() - 2; var2 >= 0; var2 -= 2) {
            if (var1.equalsIgnoreCase((String)this.namesAndValues.get(var2))) {
               return (String)this.namesAndValues.get(var2 + 1);
            }
         }

         return null;
      }

      public Headers.Builder removeAll(String var1) {
         int var3;
         for(int var2 = 0; var2 < this.namesAndValues.size(); var2 = var3 + 2) {
            var3 = var2;
            if (var1.equalsIgnoreCase((String)this.namesAndValues.get(var2))) {
               this.namesAndValues.remove(var2);
               this.namesAndValues.remove(var2);
               var3 = var2 - 2;
            }
         }

         return this;
      }

      public Headers.Builder set(String var1, String var2) {
         this.checkNameAndValue(var1, var2);
         this.removeAll(var1);
         this.addLenient(var1, var2);
         return this;
      }
   }
}
