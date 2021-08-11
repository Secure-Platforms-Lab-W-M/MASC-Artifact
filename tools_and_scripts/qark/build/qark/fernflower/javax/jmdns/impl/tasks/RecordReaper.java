package javax.jmdns.impl.tasks;

import java.util.Timer;
import javax.jmdns.impl.JmDNSImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordReaper extends DNSTask {
   static Logger logger = LoggerFactory.getLogger(RecordReaper.class.getName());

   public RecordReaper(JmDNSImpl var1) {
      super(var1);
   }

   public String getName() {
      StringBuilder var2 = new StringBuilder();
      var2.append("RecordReaper(");
      String var1;
      if (this.getDns() != null) {
         var1 = this.getDns().getName();
      } else {
         var1 = "";
      }

      var2.append(var1);
      var2.append(")");
      return var2.toString();
   }

   public void run() {
      if (!this.getDns().isCanceling()) {
         if (!this.getDns().isCanceled()) {
            logger.trace("{}.run() JmDNS reaping cache", this.getName());
            this.getDns().cleanCache();
         }
      }
   }

   public void start(Timer var1) {
      if (!this.getDns().isCanceling() && !this.getDns().isCanceled()) {
         var1.schedule(this, 10000L, 10000L);
      }

   }
}
