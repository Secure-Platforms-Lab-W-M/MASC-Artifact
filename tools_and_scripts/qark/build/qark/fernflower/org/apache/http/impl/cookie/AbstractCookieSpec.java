package org.apache.http.impl.cookie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

public abstract class AbstractCookieSpec implements CookieSpec {
   private final Map attribHandlerMap;

   public AbstractCookieSpec() {
      this.attribHandlerMap = new ConcurrentHashMap(10);
   }

   protected AbstractCookieSpec(HashMap var1) {
      Asserts.notNull(var1, "Attribute handler map");
      this.attribHandlerMap = new ConcurrentHashMap(var1);
   }

   protected AbstractCookieSpec(CommonCookieAttributeHandler... var1) {
      this.attribHandlerMap = new ConcurrentHashMap(var1.length);
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         CommonCookieAttributeHandler var4 = var1[var2];
         this.attribHandlerMap.put(var4.getAttributeName(), var4);
      }

   }

   protected CookieAttributeHandler findAttribHandler(String var1) {
      return (CookieAttributeHandler)this.attribHandlerMap.get(var1);
   }

   protected CookieAttributeHandler getAttribHandler(String var1) {
      CookieAttributeHandler var3 = this.findAttribHandler(var1);
      boolean var2;
      if (var3 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      StringBuilder var4 = new StringBuilder();
      var4.append("Handler not registered for ");
      var4.append(var1);
      var4.append(" attribute");
      Asserts.check(var2, var4.toString());
      return var3;
   }

   protected Collection getAttribHandlers() {
      return this.attribHandlerMap.values();
   }

   @Deprecated
   public void registerAttribHandler(String var1, CookieAttributeHandler var2) {
      Args.notNull(var1, "Attribute name");
      Args.notNull(var2, "Attribute handler");
      this.attribHandlerMap.put(var1, var2);
   }
}
