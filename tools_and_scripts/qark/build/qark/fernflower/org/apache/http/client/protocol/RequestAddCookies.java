package org.apache.http.client.protocol;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Lookup;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

public class RequestAddCookies implements HttpRequestInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      if (!var1.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) {
         HttpClientContext var7 = HttpClientContext.adapt(var2);
         CookieStore var12 = var7.getCookieStore();
         if (var12 == null) {
            this.log.debug("Cookie store not specified in HTTP context");
         } else {
            Lookup var11 = var7.getCookieSpecRegistry();
            if (var11 == null) {
               this.log.debug("CookieSpec registry not specified in HTTP context");
            } else {
               HttpHost var13 = var7.getTargetHost();
               if (var13 == null) {
                  this.log.debug("Target host not set in the context");
               } else {
                  RouteInfo var10 = var7.getHttpRoute();
                  if (var10 == null) {
                     this.log.debug("Connection route not set in the context");
                  } else {
                     RequestConfig var8 = var7.getRequestConfig();
                     String var6 = var8.getCookieSpec();
                     if (var6 == null) {
                        var6 = "default";
                     }

                     if (this.log.isDebugEnabled()) {
                        Log var5 = this.log;
                        StringBuilder var9 = new StringBuilder();
                        var9.append("CookieSpec selected: ");
                        var9.append(var6);
                        var5.debug(var9.toString());
                     }

                     URI var23 = null;
                     if (var1 instanceof HttpUriRequest) {
                        var23 = ((HttpUriRequest)var1).getURI();
                     } else {
                        label104: {
                           URI var28;
                           try {
                              var28 = new URI(var1.getRequestLine().getUri());
                           } catch (URISyntaxException var19) {
                              break label104;
                           }

                           var23 = var28;
                        }
                     }

                     String var24;
                     if (var23 != null) {
                        var24 = var23.getPath();
                     } else {
                        var24 = null;
                     }

                     String var29 = var13.getHostName();
                     int var4 = var13.getPort();
                     int var3 = var4;
                     if (var4 < 0) {
                        var3 = var10.getTargetHost().getPort();
                     }

                     if (var3 < 0) {
                        var3 = 0;
                     }

                     if (TextUtils.isEmpty(var24)) {
                        var24 = "/";
                     }

                     CookieOrigin var33 = new CookieOrigin(var29, var3, var24, var10.isSecure());
                     CookieSpecProvider var30 = (CookieSpecProvider)var11.lookup(var6);
                     if (var30 == null) {
                        if (this.log.isDebugEnabled()) {
                           Log var20 = this.log;
                           StringBuilder var21 = new StringBuilder();
                           var21.append("Unsupported cookie policy: ");
                           var21.append(var6);
                           var20.debug(var21.toString());
                        }
                     } else {
                        CookieSpec var14 = var30.create(var7);
                        List var31 = var12.getCookies();
                        ArrayList var15 = new ArrayList();
                        Date var25 = new Date();
                        boolean var22 = false;
                        Iterator var16 = var31.iterator();

                        while(var16.hasNext()) {
                           Cookie var32 = (Cookie)var16.next();
                           Log var17;
                           StringBuilder var18;
                           if (!var32.isExpired(var25)) {
                              if (var14.match(var32, var33)) {
                                 if (this.log.isDebugEnabled()) {
                                    var17 = this.log;
                                    var18 = new StringBuilder();
                                    var18.append("Cookie ");
                                    var18.append(var32);
                                    var18.append(" match ");
                                    var18.append(var33);
                                    var17.debug(var18.toString());
                                 }

                                 var15.add(var32);
                              }
                           } else {
                              if (this.log.isDebugEnabled()) {
                                 var17 = this.log;
                                 var18 = new StringBuilder();
                                 var18.append("Cookie ");
                                 var18.append(var32);
                                 var18.append(" expired");
                                 var17.debug(var18.toString());
                              }

                              var22 = true;
                           }
                        }

                        if (var22) {
                           var12.clearExpired(var25);
                        }

                        if (!var15.isEmpty()) {
                           Iterator var26 = var14.formatCookies(var15).iterator();

                           while(var26.hasNext()) {
                              var1.addHeader((Header)var26.next());
                           }
                        }

                        if (var14.getVersion() > 0) {
                           Header var27 = var14.getVersionHeader();
                           if (var27 != null) {
                              var1.addHeader(var27);
                           }
                        }

                        var2.setAttribute("http.cookie-spec", var14);
                        var2.setAttribute("http.cookie-origin", var33);
                     }
                  }
               }
            }
         }
      }
   }
}
