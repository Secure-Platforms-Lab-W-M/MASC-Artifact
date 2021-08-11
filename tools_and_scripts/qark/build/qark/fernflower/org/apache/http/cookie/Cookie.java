package org.apache.http.cookie;

import java.util.Date;

public interface Cookie {
   String getComment();

   String getCommentURL();

   String getDomain();

   Date getExpiryDate();

   String getName();

   String getPath();

   int[] getPorts();

   String getValue();

   int getVersion();

   boolean isExpired(Date var1);

   boolean isPersistent();

   boolean isSecure();
}
