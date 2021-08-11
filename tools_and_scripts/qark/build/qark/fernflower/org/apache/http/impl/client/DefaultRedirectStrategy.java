package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.EnumSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

public class DefaultRedirectStrategy implements RedirectStrategy {
   public static final DefaultRedirectStrategy INSTANCE = new DefaultRedirectStrategy();
   @Deprecated
   public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
   private final Log log;
   private final String[] redirectMethods;

   public DefaultRedirectStrategy() {
      this(new String[]{"GET", "HEAD"});
   }

   public DefaultRedirectStrategy(String[] var1) {
      this.log = LogFactory.getLog(this.getClass());
      var1 = (String[])var1.clone();
      Arrays.sort(var1);
      this.redirectMethods = var1;
   }

   protected URI createLocationURI(String var1) throws ProtocolException {
      try {
         URI var2 = new URI(var1);
         return var2;
      } catch (URISyntaxException var4) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Invalid redirect URI: ");
         var3.append(var1);
         throw new ProtocolException(var3.toString(), var4);
      }
   }

   public URI getLocationURI(HttpRequest var1, HttpResponse var2, HttpContext var3) throws ProtocolException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP response");
      Args.notNull(var3, "HTTP context");
      HttpClientContext var5 = HttpClientContext.adapt(var3);
      Header var4 = var2.getFirstHeader("location");
      StringBuilder var17;
      if (var4 == null) {
         var17 = new StringBuilder();
         var17.append("Received redirect response ");
         var17.append(var2.getStatusLine());
         var17.append(" but no location header");
         throw new ProtocolException(var17.toString());
      } else {
         String var14 = var4.getValue();
         if (this.log.isDebugEnabled()) {
            Log var20 = this.log;
            StringBuilder var6 = new StringBuilder();
            var6.append("Redirect requested to location '");
            var6.append(var14);
            var6.append("'");
            var20.debug(var6.toString());
         }

         RequestConfig var23 = var5.getRequestConfig();
         URI var21 = this.createLocationURI(var14);
         URI var16 = var21;

         label75: {
            URISyntaxException var10000;
            label82: {
               boolean var10001;
               try {
                  if (var23.isNormalizeUri()) {
                     var16 = URIUtils.normalizeSyntax(var21);
                  }
               } catch (URISyntaxException var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label82;
               }

               var21 = var16;

               label69: {
                  URI var7;
                  EnumSet var13;
                  HttpHost var22;
                  label85: {
                     try {
                        if (var16.isAbsolute()) {
                           break label75;
                        }

                        if (!var23.isRelativeRedirectsAllowed()) {
                           break label69;
                        }

                        var22 = var5.getTargetHost();
                        Asserts.notNull(var22, "Target host");
                        var7 = new URI(var1.getRequestLine().getUri());
                        if (var23.isNormalizeUri()) {
                           var13 = URIUtils.NORMALIZE;
                           break label85;
                        }
                     } catch (URISyntaxException var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label82;
                     }

                     try {
                        var13 = URIUtils.NO_FLAGS;
                     } catch (URISyntaxException var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label82;
                     }
                  }

                  try {
                     var21 = URIUtils.resolve(URIUtils.rewriteURI(var7, var22, var13), var16);
                     break label75;
                  } catch (URISyntaxException var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label82;
                  }
               }

               try {
                  var17 = new StringBuilder();
                  var17.append("Relative redirect location '");
                  var17.append(var16);
                  var17.append("' not allowed");
                  throw new ProtocolException(var17.toString());
               } catch (URISyntaxException var8) {
                  var10000 = var8;
                  var10001 = false;
               }
            }

            URISyntaxException var15 = var10000;
            throw new ProtocolException(var15.getMessage(), var15);
         }

         RedirectLocations var19 = (RedirectLocations)var5.getAttribute("http.protocol.redirect-locations");
         RedirectLocations var18 = var19;
         if (var19 == null) {
            var18 = new RedirectLocations();
            var3.setAttribute("http.protocol.redirect-locations", var18);
         }

         if (!var23.isCircularRedirectsAllowed() && var18.contains(var21)) {
            var17 = new StringBuilder();
            var17.append("Circular redirect to '");
            var17.append(var21);
            var17.append("'");
            throw new CircularRedirectException(var17.toString());
         } else {
            var18.add(var21);
            return var21;
         }
      }
   }

   public HttpUriRequest getRedirect(HttpRequest var1, HttpResponse var2, HttpContext var3) throws ProtocolException {
      URI var5 = this.getLocationURI(var1, var2, var3);
      String var4 = var1.getRequestLine().getMethod();
      if (var4.equalsIgnoreCase("HEAD")) {
         return new HttpHead(var5);
      } else if (var4.equalsIgnoreCase("GET")) {
         return new HttpGet(var5);
      } else {
         return (HttpUriRequest)(var2.getStatusLine().getStatusCode() == 307 ? RequestBuilder.copy(var1).setUri(var5).build() : new HttpGet(var5));
      }
   }

   protected boolean isRedirectable(String var1) {
      return Arrays.binarySearch(this.redirectMethods, var1) >= 0;
   }

   public boolean isRedirected(HttpRequest var1, HttpResponse var2, HttpContext var3) throws ProtocolException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP response");
      int var4 = var2.getStatusLine().getStatusCode();
      String var7 = var1.getRequestLine().getMethod();
      Header var8 = var2.getFirstHeader("location");
      if (var4 != 307) {
         boolean var6 = false;
         switch(var4) {
         case 301:
            break;
         case 302:
            boolean var5 = var6;
            if (this.isRedirectable(var7)) {
               var5 = var6;
               if (var8 != null) {
                  var5 = true;
               }
            }

            return var5;
         case 303:
            return true;
         default:
            return false;
         }
      }

      return this.isRedirectable(var7);
   }
}
