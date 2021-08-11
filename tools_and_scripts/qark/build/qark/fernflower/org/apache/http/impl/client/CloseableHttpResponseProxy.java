package org.apache.http.impl.client;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

@Deprecated
class CloseableHttpResponseProxy implements InvocationHandler {
   private static final Constructor CONSTRUCTOR;
   private final HttpResponse original;

   static {
      try {
         CONSTRUCTOR = Proxy.getProxyClass(CloseableHttpResponseProxy.class.getClassLoader(), CloseableHttpResponse.class).getConstructor(InvocationHandler.class);
      } catch (NoSuchMethodException var1) {
         throw new IllegalStateException(var1);
      }
   }

   CloseableHttpResponseProxy(HttpResponse var1) {
      this.original = var1;
   }

   public static CloseableHttpResponse newProxy(HttpResponse var0) {
      try {
         CloseableHttpResponse var4 = (CloseableHttpResponse)CONSTRUCTOR.newInstance(new CloseableHttpResponseProxy(var0));
         return var4;
      } catch (InstantiationException var1) {
         throw new IllegalStateException(var1);
      } catch (InvocationTargetException var2) {
         throw new IllegalStateException(var2);
      } catch (IllegalAccessException var3) {
         throw new IllegalStateException(var3);
      }
   }

   public void close() throws IOException {
      EntityUtils.consume(this.original.getEntity());
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.getName().equals("close")) {
         this.close();
         return null;
      } else {
         try {
            var1 = var2.invoke(this.original, var3);
            return var1;
         } catch (InvocationTargetException var4) {
            Throwable var5 = var4.getCause();
            if (var5 != null) {
               throw var5;
            } else {
               throw var4;
            }
         }
      }
   }
}
