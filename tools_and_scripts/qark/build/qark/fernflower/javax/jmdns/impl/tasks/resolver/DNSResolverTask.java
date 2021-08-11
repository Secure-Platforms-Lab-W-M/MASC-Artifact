package javax.jmdns.impl.tasks.resolver;

import java.io.IOException;
import java.util.Timer;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.tasks.DNSTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DNSResolverTask extends DNSTask {
   private static Logger logger = LoggerFactory.getLogger(DNSResolverTask.class.getName());
   protected int _count = 0;

   public DNSResolverTask(JmDNSImpl var1) {
      super(var1);
   }

   protected abstract DNSOutgoing addAnswers(DNSOutgoing var1) throws IOException;

   protected abstract DNSOutgoing addQuestions(DNSOutgoing var1) throws IOException;

   protected abstract String description();

   public void run() {
      Throwable var10000;
      label583: {
         boolean var10001;
         label582: {
            label587: {
               label579:
               try {
                  if (!this.getDns().isCanceling() && !this.getDns().isCanceled()) {
                     break label579;
                  }
                  break label587;
               } catch (Throwable var76) {
                  var10000 = var76;
                  var10001 = false;
                  break label583;
               }

               int var1;
               try {
                  var1 = this._count++;
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label583;
               }

               if (var1 < 3) {
                  DNSOutgoing var3;
                  try {
                     logger.debug("{}.run() JmDNS {}", this.getName(), this.description());
                     var3 = this.addQuestions(new DNSOutgoing(0));
                  } catch (Throwable var71) {
                     var10000 = var71;
                     var10001 = false;
                     break label583;
                  }

                  DNSOutgoing var2 = var3;

                  try {
                     if (this.getDns().isAnnounced()) {
                        var2 = this.addAnswers(var3);
                     }
                  } catch (Throwable var70) {
                     var10000 = var70;
                     var10001 = false;
                     break label583;
                  }

                  try {
                     if (!var2.isEmpty()) {
                        this.getDns().send(var2);
                     }

                     return;
                  } catch (Throwable var69) {
                     var10000 = var69;
                     var10001 = false;
                     break label583;
                  }
               }

               try {
                  this.cancel();
                  break label582;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label583;
               }
            }

            try {
               this.cancel();
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label583;
            }
         }

         label565:
         try {
            return;
         } catch (Throwable var72) {
            var10000 = var72;
            var10001 = false;
            break label565;
         }
      }

      Throwable var77 = var10000;
      Logger var78 = logger;
      StringBuilder var4 = new StringBuilder();
      var4.append(this.getName());
      var4.append(".run() exception ");
      var78.warn(var4.toString(), var77);
      this.getDns().recover();
   }

   public void start(Timer var1) {
      if (!this.getDns().isCanceling() && !this.getDns().isCanceled()) {
         var1.schedule(this, 225L, 225L);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append(" count: ");
      var1.append(this._count);
      return var1.toString();
   }
}
