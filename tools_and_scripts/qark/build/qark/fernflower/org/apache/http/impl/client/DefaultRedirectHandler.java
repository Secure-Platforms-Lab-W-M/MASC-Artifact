package org.apache.http.impl.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class DefaultRedirectHandler implements RedirectHandler {
   private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
   private final Log log = LogFactory.getLog(this.getClass());

   public URI getLocationURI(HttpResponse var1, HttpContext var2) throws ProtocolException {
      Args.notNull(var1, "HTTP response");
      Header var3 = var1.getFirstHeader("location");
      StringBuilder var11;
      if (var3 != null) {
         String var4 = var3.getValue();
         if (this.log.isDebugEnabled()) {
            Log var14 = this.log;
            StringBuilder var5 = new StringBuilder();
            var5.append("Redirect requested to location '");
            var5.append(var4);
            var5.append("'");
            var14.debug(var5.toString());
         }

         URI var15;
         try {
            var15 = new URI(var4);
         } catch (URISyntaxException var8) {
            var11 = new StringBuilder();
            var11.append("Invalid redirect URI: ");
            var11.append(var4);
            throw new ProtocolException(var11.toString(), var8);
         }

         HttpParams var17 = var1.getParams();
         URI var9 = var15;
         StringBuilder var13;
         if (!var15.isAbsolute()) {
            if (var17.isParameterTrue("http.protocol.reject-relative-redirect")) {
               var13 = new StringBuilder();
               var13.append("Relative redirect location '");
               var13.append(var15);
               var13.append("' not allowed");
               throw new ProtocolException(var13.toString());
            }

            HttpHost var10 = (HttpHost)var2.getAttribute("http.target_host");
            Asserts.notNull(var10, "Target host");
            HttpRequest var19 = (HttpRequest)var2.getAttribute("http.request");

            try {
               var9 = URIUtils.resolve(URIUtils.rewriteURI(new URI(var19.getRequestLine().getUri()), var10, URIUtils.DROP_FRAGMENT_AND_NORMALIZE), var15);
            } catch (URISyntaxException var7) {
               throw new ProtocolException(var7.getMessage(), var7);
            }
         }

         if (var17.isParameterFalse("http.protocol.allow-circular-redirects")) {
            RedirectLocations var18 = (RedirectLocations)var2.getAttribute("http.protocol.redirect-locations");
            RedirectLocations var16 = var18;
            if (var18 == null) {
               var16 = new RedirectLocations();
               var2.setAttribute("http.protocol.redirect-locations", var16);
            }

            URI var12;
            if (var9.getFragment() != null) {
               try {
                  var12 = URIUtils.rewriteURI(var9, new HttpHost(var9.getHost(), var9.getPort(), var9.getScheme()), URIUtils.DROP_FRAGMENT_AND_NORMALIZE);
               } catch (URISyntaxException var6) {
                  throw new ProtocolException(var6.getMessage(), var6);
               }
            } else {
               var12 = var9;
            }

            if (!var16.contains(var12)) {
               var16.add(var12);
               return var9;
            } else {
               var13 = new StringBuilder();
               var13.append("Circular redirect to '");
               var13.append(var12);
               var13.append("'");
               throw new CircularRedirectException(var13.toString());
            }
         } else {
            return var9;
         }
      } else {
         var11 = new StringBuilder();
         var11.append("Received redirect response ");
         var11.append(var1.getStatusLine());
         var11.append(" but no location header");
         throw new ProtocolException(var11.toString());
      }
   }

   public boolean isRedirectRequested(HttpResponse var1, HttpContext var2) {
      Args.notNull(var1, "HTTP response");
      int var3 = var1.getStatusLine().getStatusCode();
      boolean var4 = false;
      if (var3 != 307) {
         switch(var3) {
         case 301:
         case 302:
            break;
         case 303:
            return true;
         default:
            return false;
         }
      }

      String var5 = ((HttpRequest)var2.getAttribute("http.request")).getRequestLine().getMethod();
      if (var5.equalsIgnoreCase("GET") || var5.equalsIgnoreCase("HEAD")) {
         var4 = true;
      }

      return var4;
   }
}
