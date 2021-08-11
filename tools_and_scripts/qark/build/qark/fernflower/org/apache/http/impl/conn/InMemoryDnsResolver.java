package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.DnsResolver;
import org.apache.http.util.Args;

public class InMemoryDnsResolver implements DnsResolver {
   private final Map dnsMap = new ConcurrentHashMap();
   private final Log log = LogFactory.getLog(InMemoryDnsResolver.class);

   public void add(String var1, InetAddress... var2) {
      Args.notNull(var1, "Host name");
      Args.notNull(var2, "Array of IP addresses");
      this.dnsMap.put(var1, var2);
   }

   public InetAddress[] resolve(String var1) throws UnknownHostException {
      InetAddress[] var2 = (InetAddress[])this.dnsMap.get(var1);
      if (this.log.isInfoEnabled()) {
         Log var3 = this.log;
         StringBuilder var4 = new StringBuilder();
         var4.append("Resolving ");
         var4.append(var1);
         var4.append(" to ");
         var4.append(Arrays.deepToString(var2));
         var3.info(var4.toString());
      }

      if (var2 != null) {
         return var2;
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append(var1);
         var5.append(" cannot be resolved");
         throw new UnknownHostException(var5.toString());
      }
   }
}
