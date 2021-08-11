package com.bumptech.glide.load.model;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class LazyHeaders implements Headers {
   private volatile Map combinedHeaders;
   private final Map headers;

   LazyHeaders(Map var1) {
      this.headers = Collections.unmodifiableMap(var1);
   }

   private String buildHeaderValue(List var1) {
      StringBuilder var4 = new StringBuilder();
      int var3 = var1.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         String var5 = ((LazyHeaderFactory)var1.get(var2)).buildHeader();
         if (!TextUtils.isEmpty(var5)) {
            var4.append(var5);
            if (var2 != var1.size() - 1) {
               var4.append(',');
            }
         }
      }

      return var4.toString();
   }

   private Map generateHeaders() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.headers.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = this.buildHeaderValue((List)var3.getValue());
         if (!TextUtils.isEmpty(var4)) {
            var1.put(var3.getKey(), var4);
         }
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof LazyHeaders) {
         LazyHeaders var2 = (LazyHeaders)var1;
         return this.headers.equals(var2.headers);
      } else {
         return false;
      }
   }

   public Map getHeaders() {
      if (this.combinedHeaders == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (this.combinedHeaders == null) {
                  this.combinedHeaders = Collections.unmodifiableMap(this.generateHeaders());
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return this.combinedHeaders;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var1 = var10000;

            try {
               throw var1;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      } else {
         return this.combinedHeaders;
      }
   }

   public int hashCode() {
      return this.headers.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("LazyHeaders{headers=");
      var1.append(this.headers);
      var1.append('}');
      return var1.toString();
   }

   public static final class Builder {
      private static final Map DEFAULT_HEADERS;
      private static final String DEFAULT_USER_AGENT = getSanitizedUserAgent();
      private static final String USER_AGENT_HEADER = "User-Agent";
      private boolean copyOnModify = true;
      private Map headers;
      private boolean isUserAgentDefault;

      static {
         HashMap var0 = new HashMap(2);
         if (!TextUtils.isEmpty(DEFAULT_USER_AGENT)) {
            var0.put("User-Agent", Collections.singletonList(new LazyHeaders.StringHeaderFactory(DEFAULT_USER_AGENT)));
         }

         DEFAULT_HEADERS = Collections.unmodifiableMap(var0);
      }

      public Builder() {
         this.headers = DEFAULT_HEADERS;
         this.isUserAgentDefault = true;
      }

      private Map copyHeaders() {
         HashMap var1 = new HashMap(this.headers.size());
         Iterator var2 = this.headers.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            ArrayList var4 = new ArrayList((Collection)var3.getValue());
            var1.put(var3.getKey(), var4);
         }

         return var1;
      }

      private void copyIfNecessary() {
         if (this.copyOnModify) {
            this.copyOnModify = false;
            this.headers = this.copyHeaders();
         }

      }

      private List getFactories(String var1) {
         List var3 = (List)this.headers.get(var1);
         Object var2 = var3;
         if (var3 == null) {
            var2 = new ArrayList();
            this.headers.put(var1, var2);
         }

         return (List)var2;
      }

      static String getSanitizedUserAgent() {
         String var3 = System.getProperty("http.agent");
         if (TextUtils.isEmpty(var3)) {
            return var3;
         } else {
            int var2 = var3.length();
            StringBuilder var4 = new StringBuilder(var3.length());

            for(int var1 = 0; var1 < var2; ++var1) {
               char var0 = var3.charAt(var1);
               if ((var0 > 31 || var0 == '\t') && var0 < 127) {
                  var4.append(var0);
               } else {
                  var4.append('?');
               }
            }

            return var4.toString();
         }
      }

      public LazyHeaders.Builder addHeader(String var1, LazyHeaderFactory var2) {
         if (this.isUserAgentDefault && "User-Agent".equalsIgnoreCase(var1)) {
            return this.setHeader(var1, var2);
         } else {
            this.copyIfNecessary();
            this.getFactories(var1).add(var2);
            return this;
         }
      }

      public LazyHeaders.Builder addHeader(String var1, String var2) {
         return this.addHeader(var1, (LazyHeaderFactory)(new LazyHeaders.StringHeaderFactory(var2)));
      }

      public LazyHeaders build() {
         this.copyOnModify = true;
         return new LazyHeaders(this.headers);
      }

      public LazyHeaders.Builder setHeader(String var1, LazyHeaderFactory var2) {
         this.copyIfNecessary();
         if (var2 == null) {
            this.headers.remove(var1);
         } else {
            List var3 = this.getFactories(var1);
            var3.clear();
            var3.add(var2);
         }

         if (this.isUserAgentDefault && "User-Agent".equalsIgnoreCase(var1)) {
            this.isUserAgentDefault = false;
         }

         return this;
      }

      public LazyHeaders.Builder setHeader(String var1, String var2) {
         LazyHeaders.StringHeaderFactory var3;
         if (var2 == null) {
            var3 = null;
         } else {
            var3 = new LazyHeaders.StringHeaderFactory(var2);
         }

         return this.setHeader(var1, (LazyHeaderFactory)var3);
      }
   }

   static final class StringHeaderFactory implements LazyHeaderFactory {
      private final String value;

      StringHeaderFactory(String var1) {
         this.value = var1;
      }

      public String buildHeader() {
         return this.value;
      }

      public boolean equals(Object var1) {
         if (var1 instanceof LazyHeaders.StringHeaderFactory) {
            LazyHeaders.StringHeaderFactory var2 = (LazyHeaders.StringHeaderFactory)var1;
            return this.value.equals(var2.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.value.hashCode();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("StringHeaderFactory{value='");
         var1.append(this.value);
         var1.append('\'');
         var1.append('}');
         return var1.toString();
      }
   }
}
