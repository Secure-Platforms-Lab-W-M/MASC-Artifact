package okhttp3.internal.platform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

class JdkWithJettyBootPlatform extends Platform {
   private final Class clientProviderClass;
   private final Method getMethod;
   private final Method putMethod;
   private final Method removeMethod;
   private final Class serverProviderClass;

   JdkWithJettyBootPlatform(Method var1, Method var2, Method var3, Class var4, Class var5) {
      this.putMethod = var1;
      this.getMethod = var2;
      this.removeMethod = var3;
      this.clientProviderClass = var4;
      this.serverProviderClass = var5;
   }

   public static Platform buildIfSupported() {
      try {
         Class var0 = Class.forName("org.eclipse.jetty.alpn.ALPN");
         StringBuilder var1 = new StringBuilder();
         var1.append("org.eclipse.jetty.alpn.ALPN");
         var1.append("$Provider");
         Class var7 = Class.forName(var1.toString());
         StringBuilder var2 = new StringBuilder();
         var2.append("org.eclipse.jetty.alpn.ALPN");
         var2.append("$ClientProvider");
         Class var8 = Class.forName(var2.toString());
         StringBuilder var3 = new StringBuilder();
         var3.append("org.eclipse.jetty.alpn.ALPN");
         var3.append("$ServerProvider");
         Class var9 = Class.forName(var3.toString());
         JdkWithJettyBootPlatform var6 = new JdkWithJettyBootPlatform(var0.getMethod("put", SSLSocket.class, var7), var0.getMethod("get", SSLSocket.class), var0.getMethod("remove", SSLSocket.class), var8, var9);
         return var6;
      } catch (ClassNotFoundException var4) {
      } catch (NoSuchMethodException var5) {
      }

      return null;
   }

   public void afterHandshake(SSLSocket var1) {
      try {
         this.removeMethod.invoke((Object)null, var1);
         return;
      } catch (IllegalAccessException var2) {
      } catch (InvocationTargetException var3) {
      }

      throw new AssertionError();
   }

   public void configureTlsExtensions(SSLSocket var1, String var2, List var3) {
      List var5 = alpnProtocolNames(var3);

      Object var8;
      try {
         ClassLoader var9 = Platform.class.getClassLoader();
         Class var11 = this.clientProviderClass;
         Class var4 = this.serverProviderClass;
         JdkWithJettyBootPlatform.JettyNegoProvider var12 = new JdkWithJettyBootPlatform.JettyNegoProvider(var5);
         Object var10 = Proxy.newProxyInstance(var9, new Class[]{var11, var4}, var12);
         this.putMethod.invoke((Object)null, var1, var10);
         return;
      } catch (InvocationTargetException var6) {
         var8 = var6;
      } catch (IllegalAccessException var7) {
         var8 = var7;
      }

      throw new AssertionError(var8);
   }

   public String getSelectedProtocol(SSLSocket var1) {
      try {
         JdkWithJettyBootPlatform.JettyNegoProvider var4 = (JdkWithJettyBootPlatform.JettyNegoProvider)Proxy.getInvocationHandler(this.getMethod.invoke((Object)null, var1));
         if (!var4.unsupported && var4.selected == null) {
            Platform.get().log(4, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", (Throwable)null);
            return null;
         }

         if (var4.unsupported) {
            return null;
         }

         String var5 = var4.selected;
         return var5;
      } catch (InvocationTargetException var2) {
      } catch (IllegalAccessException var3) {
      }

      throw new AssertionError();
   }

   private static class JettyNegoProvider implements InvocationHandler {
      private final List protocols;
      String selected;
      boolean unsupported;

      JettyNegoProvider(List var1) {
         this.protocols = var1;
      }

      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         String var6 = var2.getName();
         Class var7 = var2.getReturnType();
         var1 = var3;
         if (var3 == null) {
            var1 = Util.EMPTY_STRING_ARRAY;
         }

         if (var6.equals("supports") && Boolean.TYPE == var7) {
            return true;
         } else if (var6.equals("unsupported") && Void.TYPE == var7) {
            this.unsupported = true;
            return null;
         } else if (var6.equals("protocols") && ((Object[])var1).length == 0) {
            return this.protocols;
         } else if ((var6.equals("selectProtocol") || var6.equals("select")) && String.class == var7 && ((Object[])var1).length == 1 && ((Object[])var1)[0] instanceof List) {
            List var8 = (List)((Object[])var1)[0];
            int var4 = 0;

            String var9;
            for(int var5 = var8.size(); var4 < var5; ++var4) {
               if (this.protocols.contains(var8.get(var4))) {
                  var9 = (String)var8.get(var4);
                  this.selected = var9;
                  return var9;
               }
            }

            var9 = (String)this.protocols.get(0);
            this.selected = var9;
            return var9;
         } else if ((var6.equals("protocolSelected") || var6.equals("selected")) && ((Object[])var1).length == 1) {
            this.selected = (String)((Object[])var1)[0];
            return null;
         } else {
            return var2.invoke(this, (Object[])var1);
         }
      }
   }
}
