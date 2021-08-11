package org.apache.http.impl.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.util.Args;

public class BasicAuthCache implements AuthCache {
   private final Log log;
   private final Map map;
   private final SchemePortResolver schemePortResolver;

   public BasicAuthCache() {
      this((SchemePortResolver)null);
   }

   public BasicAuthCache(SchemePortResolver var1) {
      this.log = LogFactory.getLog(this.getClass());
      this.map = new ConcurrentHashMap();
      if (var1 == null) {
         var1 = DefaultSchemePortResolver.INSTANCE;
      }

      this.schemePortResolver = (SchemePortResolver)var1;
   }

   public void clear() {
      this.map.clear();
   }

   public AuthScheme get(HttpHost var1) {
      Args.notNull(var1, "HTTP host");
      byte[] var5 = (byte[])this.map.get(this.getKey(var1));
      if (var5 != null) {
         try {
            ObjectInputStream var6 = new ObjectInputStream(new ByteArrayInputStream(var5));
            AuthScheme var2 = (AuthScheme)var6.readObject();
            var6.close();
            return var2;
         } catch (IOException var3) {
            if (this.log.isWarnEnabled()) {
               this.log.warn("Unexpected I/O error while de-serializing auth scheme", var3);
            }

            return null;
         } catch (ClassNotFoundException var4) {
            if (this.log.isWarnEnabled()) {
               this.log.warn("Unexpected error while de-serializing auth scheme", var4);
            }

            return null;
         }
      } else {
         return null;
      }
   }

   protected HttpHost getKey(HttpHost var1) {
      if (var1.getPort() <= 0) {
         int var2;
         try {
            var2 = this.schemePortResolver.resolve(var1);
         } catch (UnsupportedSchemeException var4) {
            return var1;
         }

         return new HttpHost(var1.getHostName(), var2, var1.getSchemeName());
      } else {
         return var1;
      }
   }

   public void put(HttpHost var1, AuthScheme var2) {
      Args.notNull(var1, "HTTP host");
      if (var2 != null) {
         if (var2 instanceof Serializable) {
            try {
               ByteArrayOutputStream var7 = new ByteArrayOutputStream();
               ObjectOutputStream var4 = new ObjectOutputStream(var7);
               var4.writeObject(var2);
               var4.close();
               this.map.put(this.getKey(var1), var7.toByteArray());
            } catch (IOException var5) {
               if (this.log.isWarnEnabled()) {
                  this.log.warn("Unexpected I/O error while serializing auth scheme", var5);
               }

            }
         } else {
            if (this.log.isDebugEnabled()) {
               Log var6 = this.log;
               StringBuilder var3 = new StringBuilder();
               var3.append("Auth scheme ");
               var3.append(var2.getClass());
               var3.append(" is not serializable");
               var6.debug(var3.toString());
            }

         }
      }
   }

   public void remove(HttpHost var1) {
      Args.notNull(var1, "HTTP host");
      this.map.remove(this.getKey(var1));
   }

   public String toString() {
      return this.map.toString();
   }
}
