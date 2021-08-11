package javax.jmdns.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DNSCache extends ConcurrentHashMap {
   private static Logger logger = LoggerFactory.getLogger(DNSCache.class.getName());
   private static final long serialVersionUID = 3024739453186759259L;

   public DNSCache() {
      this(1024);
   }

   public DNSCache(int var1) {
      super(var1);
   }

   public DNSCache(DNSCache var1) {
      int var2;
      if (var1 != null) {
         var2 = var1.size();
      } else {
         var2 = 1024;
      }

      this(var2);
      if (var1 != null) {
         this.putAll(var1);
      }

   }

   private Collection _getDNSEntryList(String var1) {
      if (var1 != null) {
         var1 = var1.toLowerCase();
      } else {
         var1 = null;
      }

      return (Collection)this.get(var1);
   }

   public boolean addDNSEntry(DNSEntry param1) {
      // $FF: Couldn't be decompiled
   }

   public Collection allValues() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.values().iterator();

      while(var2.hasNext()) {
         List var3 = (List)var2.next();
         if (var3 != null) {
            var1.addAll(var3);
         }
      }

      return var1;
   }

   protected Object clone() throws CloneNotSupportedException {
      return new DNSCache(this);
   }

   public DNSEntry getDNSEntry(String var1, DNSRecordType var2, DNSRecordClass var3) {
      Object var4 = null;
      Collection var5 = this._getDNSEntryList(var1);
      if (var5 != null) {
         synchronized(var5){}

         Throwable var10000;
         boolean var10001;
         label250: {
            Iterator var6;
            try {
               var6 = var5.iterator();
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label250;
            }

            while(true) {
               DNSEntry var27 = (DNSEntry)var4;

               try {
                  if (var6.hasNext()) {
                     var27 = (DNSEntry)var6.next();
                     if (!var27.matchRecordType(var2) || !var27.matchRecordClass(var3)) {
                        continue;
                     }
                  }
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break;
               }

               try {
                  return var27;
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break;
               }
            }
         }

         while(true) {
            Throwable var28 = var10000;

            try {
               throw var28;
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               continue;
            }
         }
      } else {
         return null;
      }
   }

   public DNSEntry getDNSEntry(DNSEntry var1) {
      Object var3 = null;
      if (var1 != null) {
         Collection var4 = this._getDNSEntryList(var1.getKey());
         if (var4 != null) {
            synchronized(var4){}

            Throwable var10000;
            boolean var10001;
            label240: {
               Iterator var5;
               try {
                  var5 = var4.iterator();
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label240;
               }

               while(true) {
                  DNSEntry var2 = (DNSEntry)var3;

                  try {
                     if (var5.hasNext()) {
                        var2 = (DNSEntry)var5.next();
                        if (!var2.isSameEntry(var1)) {
                           continue;
                        }
                     }
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break;
                  }

                  try {
                     return var2;
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break;
                  }
               }
            }

            while(true) {
               Throwable var26 = var10000;

               try {
                  throw var26;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      return null;
   }

   public Collection getDNSEntryList(String param1) {
      // $FF: Couldn't be decompiled
   }

   public Collection getDNSEntryList(String param1, DNSRecordType param2, DNSRecordClass param3) {
      // $FF: Couldn't be decompiled
   }

   public void logCachedContent() {
      if (logger.isTraceEnabled()) {
         logger.trace("Cached DNSEntries: {}", this.toString());
      }
   }

   public boolean removeDNSEntry(DNSEntry param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean replaceDNSEntry(DNSEntry param1, DNSEntry param2) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }
}
