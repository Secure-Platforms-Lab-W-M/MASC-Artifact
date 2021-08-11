package org.apache.http.impl.execchain;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthState;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

public class RedirectExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final RedirectStrategy redirectStrategy;
   private final ClientExecChain requestExecutor;
   private final HttpRoutePlanner routePlanner;

   public RedirectExec(ClientExecChain var1, HttpRoutePlanner var2, RedirectStrategy var3) {
      Args.notNull(var1, "HTTP client request executor");
      Args.notNull(var2, "HTTP route planner");
      Args.notNull(var3, "HTTP redirect strategy");
      this.requestExecutor = var1;
      this.routePlanner = var2;
      this.redirectStrategy = var3;
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Args.notNull(var1, "HTTP route");
      Args.notNull(var2, "HTTP request");
      Args.notNull(var3, "HTTP context");
      List var7 = var3.getRedirectLocations();
      if (var7 != null) {
         var7.clear();
      }

      RequestConfig var9 = var3.getRequestConfig();
      int var5;
      if (var9.getMaxRedirects() > 0) {
         var5 = var9.getMaxRedirects();
      } else {
         var5 = 50;
      }

      int var6 = 0;
      HttpRequestWrapper var137 = var2;

      while(true) {
         CloseableHttpResponse var8 = this.requestExecutor.execute(var1, var137, var3, var4);

         HttpException var10000;
         boolean var10001;
         label547: {
            RuntimeException var141;
            label546: {
               IOException var140;
               label545: {
                  try {
                     if (!var9.isRedirectsEnabled() || !this.redirectStrategy.isRedirected(var137.getOriginal(), var8, var3)) {
                        return var8;
                     }
                  } catch (RuntimeException var127) {
                     var141 = var127;
                     var10001 = false;
                     break label546;
                  } catch (IOException var128) {
                     var140 = var128;
                     var10001 = false;
                     break label545;
                  } catch (HttpException var129) {
                     var10000 = var129;
                     var10001 = false;
                     break label547;
                  }

                  StringBuilder var132;
                  if (var6 < var5) {
                     label554: {
                        ++var6;

                        HttpUriRequest var138;
                        try {
                           var138 = this.redirectStrategy.getRedirect(var137.getOriginal(), var8, var3);
                           if (!var138.headerIterator().hasNext()) {
                              var138.setHeaders(var2.getOriginal().getAllHeaders());
                           }
                        } catch (RuntimeException var121) {
                           var141 = var121;
                           var10001 = false;
                           break label546;
                        } catch (IOException var122) {
                           var140 = var122;
                           var10001 = false;
                           break label554;
                        } catch (HttpException var123) {
                           var10000 = var123;
                           var10001 = false;
                           break label547;
                        }

                        try {
                           var137 = HttpRequestWrapper.wrap(var138);
                           if (var137 instanceof HttpEntityEnclosingRequest) {
                              RequestEntityProxy.enhance((HttpEntityEnclosingRequest)var137);
                           }
                        } catch (RuntimeException var118) {
                           var141 = var118;
                           var10001 = false;
                           break label546;
                        } catch (IOException var119) {
                           var140 = var119;
                           var10001 = false;
                           break label554;
                        } catch (HttpException var120) {
                           var10000 = var120;
                           var10001 = false;
                           break label547;
                        }

                        URI var10;
                        HttpHost var11;
                        try {
                           var10 = var137.getURI();
                           var11 = URIUtils.extractHost(var10);
                        } catch (RuntimeException var115) {
                           var141 = var115;
                           var10001 = false;
                           break label546;
                        } catch (IOException var116) {
                           var140 = var116;
                           var10001 = false;
                           break label554;
                        } catch (HttpException var117) {
                           var10000 = var117;
                           var10001 = false;
                           break label547;
                        }

                        if (var11 != null) {
                           label555: {
                              label557: {
                                 AuthState var130;
                                 label523:
                                 try {
                                    if (!var1.getTargetHost().equals(var11)) {
                                       var130 = var3.getTargetAuthState();
                                       break label523;
                                    }
                                    break label557;
                                 } catch (RuntimeException var109) {
                                    var141 = var109;
                                    var10001 = false;
                                    break label546;
                                 } catch (IOException var110) {
                                    var140 = var110;
                                    var10001 = false;
                                    break label555;
                                 } catch (HttpException var111) {
                                    var10000 = var111;
                                    var10001 = false;
                                    break label547;
                                 }

                                 if (var130 != null) {
                                    try {
                                       this.log.debug("Resetting target auth state");
                                       var130.reset();
                                    } catch (RuntimeException var103) {
                                       var141 = var103;
                                       var10001 = false;
                                       break label546;
                                    } catch (IOException var104) {
                                       var140 = var104;
                                       var10001 = false;
                                       break label555;
                                    } catch (HttpException var105) {
                                       var10000 = var105;
                                       var10001 = false;
                                       break label547;
                                    }
                                 }

                                 try {
                                    var130 = var3.getProxyAuthState();
                                 } catch (RuntimeException var100) {
                                    var141 = var100;
                                    var10001 = false;
                                    break label546;
                                 } catch (IOException var101) {
                                    var140 = var101;
                                    var10001 = false;
                                    break label555;
                                 } catch (HttpException var102) {
                                    var10000 = var102;
                                    var10001 = false;
                                    break label547;
                                 }

                                 if (var130 != null) {
                                    try {
                                       if (var130.isConnectionBased()) {
                                          this.log.debug("Resetting proxy auth state");
                                          var130.reset();
                                       }
                                    } catch (RuntimeException var106) {
                                       var141 = var106;
                                       var10001 = false;
                                       break label546;
                                    } catch (IOException var107) {
                                       var140 = var107;
                                       var10001 = false;
                                       break label555;
                                    } catch (HttpException var108) {
                                       var10000 = var108;
                                       var10001 = false;
                                       break label547;
                                    }
                                 }
                              }

                              try {
                                 var1 = this.routePlanner.determineRoute(var11, var137, var3);
                                 if (this.log.isDebugEnabled()) {
                                    Log var139 = this.log;
                                    StringBuilder var12 = new StringBuilder();
                                    var12.append("Redirecting to '");
                                    var12.append(var10);
                                    var12.append("' via ");
                                    var12.append(var1);
                                    var139.debug(var12.toString());
                                 }
                              } catch (RuntimeException var97) {
                                 var141 = var97;
                                 var10001 = false;
                                 break label546;
                              } catch (IOException var98) {
                                 var140 = var98;
                                 var10001 = false;
                                 break label555;
                              } catch (HttpException var99) {
                                 var10000 = var99;
                                 var10001 = false;
                                 break label547;
                              }

                              try {
                                 EntityUtils.consume(var8.getEntity());
                                 var8.close();
                                 continue;
                              } catch (RuntimeException var94) {
                                 var141 = var94;
                                 var10001 = false;
                                 break label546;
                              } catch (IOException var95) {
                                 var140 = var95;
                                 var10001 = false;
                              } catch (HttpException var96) {
                                 var10000 = var96;
                                 var10001 = false;
                                 break label547;
                              }
                           }
                        } else {
                           try {
                              var132 = new StringBuilder();
                              var132.append("Redirect URI does not specify a valid host name: ");
                              var132.append(var10);
                              throw new ProtocolException(var132.toString());
                           } catch (RuntimeException var112) {
                              var141 = var112;
                              var10001 = false;
                              break label546;
                           } catch (IOException var113) {
                              var140 = var113;
                              var10001 = false;
                           } catch (HttpException var114) {
                              var10000 = var114;
                              var10001 = false;
                              break label547;
                           }
                        }
                     }
                  } else {
                     try {
                        var132 = new StringBuilder();
                        var132.append("Maximum redirects (");
                        var132.append(var5);
                        var132.append(") exceeded");
                        throw new RedirectException(var132.toString());
                     } catch (RuntimeException var124) {
                        var141 = var124;
                        var10001 = false;
                        break label546;
                     } catch (IOException var125) {
                        var140 = var125;
                        var10001 = false;
                     } catch (HttpException var126) {
                        var10000 = var126;
                        var10001 = false;
                        break label547;
                     }
                  }
               }

               IOException var135 = var140;
               var8.close();
               throw var135;
            }

            RuntimeException var136 = var141;
            var8.close();
            throw var136;
         }

         HttpException var133 = var10000;

         label483: {
            Throwable var142;
            label482: {
               IOException var131;
               try {
                  try {
                     EntityUtils.consume(var8.getEntity());
                     break label483;
                  } catch (IOException var92) {
                     var131 = var92;
                  }
               } catch (Throwable var93) {
                  var142 = var93;
                  var10001 = false;
                  break label482;
               }

               label477:
               try {
                  this.log.debug("I/O error while releasing connection", var131);
                  break label483;
               } catch (Throwable var91) {
                  var142 = var91;
                  var10001 = false;
                  break label477;
               }
            }

            Throwable var134 = var142;
            var8.close();
            throw var134;
         }

         var8.close();
         throw var133;
      }
   }
}
