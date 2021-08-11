package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

public class URIUtils {
   public static final EnumSet DROP_FRAGMENT;
   public static final EnumSet DROP_FRAGMENT_AND_NORMALIZE;
   public static final EnumSet NORMALIZE;
   public static final EnumSet NO_FLAGS = EnumSet.noneOf(URIUtils.UriFlag.class);

   static {
      DROP_FRAGMENT = EnumSet.of(URIUtils.UriFlag.DROP_FRAGMENT);
      NORMALIZE = EnumSet.of(URIUtils.UriFlag.NORMALIZE);
      DROP_FRAGMENT_AND_NORMALIZE = EnumSet.of(URIUtils.UriFlag.DROP_FRAGMENT, URIUtils.UriFlag.NORMALIZE);
   }

   private URIUtils() {
   }

   @Deprecated
   public static URI createURI(String var0, String var1, int var2, String var3, String var4, String var5) throws URISyntaxException {
      StringBuilder var6 = new StringBuilder();
      if (var1 != null) {
         if (var0 != null) {
            var6.append(var0);
            var6.append("://");
         }

         var6.append(var1);
         if (var2 > 0) {
            var6.append(':');
            var6.append(var2);
         }
      }

      if (var3 == null || !var3.startsWith("/")) {
         var6.append('/');
      }

      if (var3 != null) {
         var6.append(var3);
      }

      if (var4 != null) {
         var6.append('?');
         var6.append(var4);
      }

      if (var5 != null) {
         var6.append('#');
         var6.append(var5);
      }

      return new URI(var6.toString());
   }

   public static HttpHost extractHost(URI var0) {
      if (var0 == null) {
         return null;
      } else {
         if (var0.isAbsolute()) {
            int var2 = var0.getPort();
            String var7 = var0.getHost();
            int var1 = var2;
            String var6 = var7;
            if (var7 == null) {
               String var8 = var0.getAuthority();
               var1 = var2;
               var6 = var8;
               if (var8 != null) {
                  var1 = var8.indexOf(64);
                  var7 = var8;
                  if (var1 >= 0) {
                     if (var8.length() > var1 + 1) {
                        var7 = var8.substring(var1 + 1);
                     } else {
                        var7 = null;
                     }
                  }

                  var1 = var2;
                  var6 = var7;
                  if (var7 != null) {
                     int var5 = var7.indexOf(58);
                     var1 = var2;
                     var6 = var7;
                     if (var5 >= 0) {
                        int var4 = var5 + 1;
                        int var3 = 0;

                        for(var1 = var4; var1 < var7.length() && Character.isDigit(var7.charAt(var1)); ++var1) {
                           ++var3;
                        }

                        var1 = var2;
                        if (var3 > 0) {
                           try {
                              var1 = Integer.parseInt(var7.substring(var4, var4 + var3));
                           } catch (NumberFormatException var9) {
                              var1 = var2;
                           }
                        }

                        var6 = var7.substring(0, var5);
                     }
                  }
               }
            }

            String var11 = var0.getScheme();
            if (!TextUtils.isBlank(var6)) {
               try {
                  HttpHost var12 = new HttpHost(var6, var1, var11);
                  return var12;
               } catch (IllegalArgumentException var10) {
               }
            }
         }

         return null;
      }
   }

   public static URI normalizeSyntax(URI var0) throws URISyntaxException {
      if (!var0.isOpaque()) {
         if (var0.getAuthority() == null) {
            return var0;
         } else {
            URIBuilder var4 = new URIBuilder(var0);
            List var2 = var4.getPathSegments();
            Stack var1 = new Stack();
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               String var3 = (String)var5.next();
               if (!".".equals(var3)) {
                  if ("..".equals(var3)) {
                     if (!var1.isEmpty()) {
                        var1.pop();
                     }
                  } else {
                     var1.push(var3);
                  }
               }
            }

            if (var1.size() == 0) {
               var1.add("");
            }

            var4.setPathSegments((List)var1);
            if (var4.getScheme() != null) {
               var4.setScheme(var4.getScheme().toLowerCase(Locale.ROOT));
            }

            if (var4.getHost() != null) {
               var4.setHost(var4.getHost().toLowerCase(Locale.ROOT));
            }

            return var4.build();
         }
      } else {
         return var0;
      }
   }

   public static URI resolve(URI var0, String var1) {
      return resolve(var0, URI.create(var1));
   }

   public static URI resolve(URI var0, URI var1) {
      Args.notNull(var0, "Base URI");
      Args.notNull(var1, "Reference URI");
      String var3 = var1.toASCIIString();
      String var5;
      if (var3.startsWith("?")) {
         var5 = var0.toASCIIString();
         int var2 = var5.indexOf(63);
         if (var2 > -1) {
            var5 = var5.substring(0, var2);
         }

         StringBuilder var6 = new StringBuilder();
         var6.append(var5);
         var6.append(var3);
         return URI.create(var6.toString());
      } else {
         if (var3.isEmpty()) {
            var5 = var0.resolve(URI.create("#")).toASCIIString();
            var0 = URI.create(var5.substring(0, var5.indexOf(35)));
         } else {
            var0 = var0.resolve(var1);
         }

         try {
            var0 = normalizeSyntax(var0);
            return var0;
         } catch (URISyntaxException var4) {
            throw new IllegalArgumentException(var4);
         }
      }
   }

   public static URI resolve(URI var0, HttpHost var1, List var2) throws URISyntaxException {
      Args.notNull(var0, "Request URI");
      URIBuilder var6;
      if (var2 != null && !var2.isEmpty()) {
         URIBuilder var5 = new URIBuilder((URI)var2.get(var2.size() - 1));
         String var4 = var5.getFragment();

         for(int var3 = var2.size() - 1; var4 == null && var3 >= 0; --var3) {
            var4 = ((URI)var2.get(var3)).getFragment();
         }

         var5.setFragment(var4);
         var6 = var5;
      } else {
         var6 = new URIBuilder(var0);
      }

      if (var6.getFragment() == null) {
         var6.setFragment(var0.getFragment());
      }

      if (var1 != null && !var6.isAbsolute()) {
         var6.setScheme(var1.getSchemeName());
         var6.setHost(var1.getHostName());
         var6.setPort(var1.getPort());
      }

      return var6.build();
   }

   public static URI rewriteURI(URI var0) throws URISyntaxException {
      Args.notNull(var0, "URI");
      if (var0.isOpaque()) {
         return var0;
      } else {
         URIBuilder var1 = new URIBuilder(var0);
         if (var1.getUserInfo() != null) {
            var1.setUserInfo((String)null);
         }

         if (var1.getPathSegments().isEmpty()) {
            var1.setPathSegments("");
         }

         if (TextUtils.isEmpty(var1.getPath())) {
            var1.setPath("/");
         }

         if (var1.getHost() != null) {
            var1.setHost(var1.getHost().toLowerCase(Locale.ROOT));
         }

         var1.setFragment((String)null);
         return var1.build();
      }
   }

   public static URI rewriteURI(URI var0, HttpHost var1) throws URISyntaxException {
      return rewriteURI(var0, var1, NORMALIZE);
   }

   public static URI rewriteURI(URI var0, HttpHost var1, EnumSet var2) throws URISyntaxException {
      Args.notNull(var0, "URI");
      Args.notNull(var2, "URI flags");
      if (var0.isOpaque()) {
         return var0;
      } else {
         URIBuilder var4 = new URIBuilder(var0);
         if (var1 != null) {
            var4.setScheme(var1.getSchemeName());
            var4.setHost(var1.getHostName());
            var4.setPort(var1.getPort());
         } else {
            var4.setScheme((String)null);
            var4.setHost((String)null);
            var4.setPort(-1);
         }

         if (var2.contains(URIUtils.UriFlag.DROP_FRAGMENT)) {
            var4.setFragment((String)null);
         }

         if (var2.contains(URIUtils.UriFlag.NORMALIZE)) {
            List var5 = var4.getPathSegments();
            ArrayList var6 = new ArrayList(var5);
            Iterator var3 = var6.iterator();

            while(var3.hasNext()) {
               if (((String)var3.next()).isEmpty() && var3.hasNext()) {
                  var3.remove();
               }
            }

            if (var6.size() != var5.size()) {
               var4.setPathSegments((List)var6);
            }
         }

         if (var4.isPathEmpty()) {
            var4.setPathSegments("");
         }

         return var4.build();
      }
   }

   @Deprecated
   public static URI rewriteURI(URI var0, HttpHost var1, boolean var2) throws URISyntaxException {
      EnumSet var3;
      if (var2) {
         var3 = DROP_FRAGMENT;
      } else {
         var3 = NO_FLAGS;
      }

      return rewriteURI(var0, var1, var3);
   }

   public static URI rewriteURIForRoute(URI var0, RouteInfo var1) throws URISyntaxException {
      return rewriteURIForRoute(var0, var1, true);
   }

   public static URI rewriteURIForRoute(URI var0, RouteInfo var1, boolean var2) throws URISyntaxException {
      if (var0 == null) {
         return null;
      } else {
         EnumSet var4;
         if (var1.getProxyHost() != null && !var1.isTunnelled()) {
            if (var0.isAbsolute()) {
               return rewriteURI(var0);
            } else {
               HttpHost var3 = var1.getTargetHost();
               if (var2) {
                  var4 = DROP_FRAGMENT_AND_NORMALIZE;
               } else {
                  var4 = DROP_FRAGMENT;
               }

               return rewriteURI(var0, var3, var4);
            }
         } else if (var0.isAbsolute()) {
            if (var2) {
               var4 = DROP_FRAGMENT_AND_NORMALIZE;
            } else {
               var4 = DROP_FRAGMENT;
            }

            return rewriteURI(var0, (HttpHost)null, var4);
         } else {
            return rewriteURI(var0);
         }
      }
   }

   public static enum UriFlag {
      DROP_FRAGMENT,
      NORMALIZE;

      static {
         URIUtils.UriFlag var0 = new URIUtils.UriFlag("NORMALIZE", 1);
         NORMALIZE = var0;
      }
   }
}
