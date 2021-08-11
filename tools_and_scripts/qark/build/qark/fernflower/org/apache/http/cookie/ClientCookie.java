package org.apache.http.cookie;

public interface ClientCookie extends Cookie {
   String COMMENTURL_ATTR = "commenturl";
   String COMMENT_ATTR = "comment";
   String DISCARD_ATTR = "discard";
   String DOMAIN_ATTR = "domain";
   String EXPIRES_ATTR = "expires";
   String MAX_AGE_ATTR = "max-age";
   String PATH_ATTR = "path";
   String PORT_ATTR = "port";
   String SECURE_ATTR = "secure";
   String VERSION_ATTR = "version";

   boolean containsAttribute(String var1);

   String getAttribute(String var1);
}
