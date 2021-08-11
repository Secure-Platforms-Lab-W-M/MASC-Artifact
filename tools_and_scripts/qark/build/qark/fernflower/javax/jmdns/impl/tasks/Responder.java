package javax.jmdns.impl.tasks;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import javax.jmdns.impl.DNSIncoming;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.constants.DNSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Responder extends DNSTask {
   static Logger logger = LoggerFactory.getLogger(Responder.class.getName());
   private final InetAddress _addr;
   private final DNSIncoming _in;
   private final int _port;
   private final boolean _unicast;

   public Responder(JmDNSImpl var1, DNSIncoming var2, InetAddress var3, int var4) {
      super(var1);
      this._in = var2;
      this._addr = var3;
      this._port = var4;
      boolean var5;
      if (var4 != DNSConstants.MDNS_PORT) {
         var5 = true;
      } else {
         var5 = false;
      }

      this._unicast = var5;
   }

   public String getName() {
      StringBuilder var2 = new StringBuilder();
      var2.append("Responder(");
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
      this.getDns().respondToQuery(this._in);
      HashSet var5 = new HashSet();
      HashSet var6 = new HashSet();
      if (this.getDns().isAnnounced()) {
         Throwable var10000;
         label2203: {
            Iterator var4;
            boolean var10001;
            try {
               var4 = this._in.getQuestions().iterator();
            } catch (Throwable var247) {
               var10000 = var247;
               var10001 = false;
               break label2203;
            }

            label2202:
            while(true) {
               DNSQuestion var256;
               label2208: {
                  try {
                     if (var4.hasNext()) {
                        var256 = (DNSQuestion)var4.next();
                        logger.debug("{}.run() JmDNS responding to: {}", this.getName(), var256);
                        if (this._unicast) {
                           var5.add(var256);
                        }
                        break label2208;
                     }
                  } catch (Throwable var248) {
                     var10000 = var248;
                     var10001 = false;
                     break;
                  }

                  long var1;
                  try {
                     var1 = System.currentTimeMillis();
                     var4 = this._in.getAnswers().iterator();
                  } catch (Throwable var244) {
                     var10000 = var244;
                     var10001 = false;
                     break;
                  }

                  DNSRecord var7;
                  while(true) {
                     try {
                        if (!var4.hasNext()) {
                           break;
                        }

                        var7 = (DNSRecord)var4.next();
                        if (var7.isStale(var1)) {
                           var6.remove(var7);
                           logger.debug("{} - JmDNS Responder Known Answer Removed", this.getName());
                        }
                     } catch (Throwable var245) {
                        var10000 = var245;
                        var10001 = false;
                        break label2202;
                     }
                  }

                  label2211: {
                     boolean var3;
                     label2175: {
                        label2174: {
                           try {
                              if (var6.isEmpty()) {
                                 break label2211;
                              }

                              logger.debug("{}.run() JmDNS responding", this.getName());
                              if (this._unicast) {
                                 break label2174;
                              }
                           } catch (Throwable var243) {
                              var10000 = var243;
                              var10001 = false;
                              break;
                           }

                           var3 = true;
                           break label2175;
                        }

                        var3 = false;
                     }

                     DNSOutgoing var249;
                     try {
                        var249 = new DNSOutgoing(33792, var3, this._in.getSenderUDPPayload());
                        if (this._unicast) {
                           var249.setDestination(new InetSocketAddress(this._addr, this._port));
                        }
                     } catch (Throwable var242) {
                        var10000 = var242;
                        var10001 = false;
                        break;
                     }

                     Iterator var255;
                     try {
                        var249.setId(this._in.getId());
                        var255 = var5.iterator();
                     } catch (Throwable var239) {
                        var10000 = var239;
                        var10001 = false;
                        break;
                     }

                     DNSOutgoing var250;
                     while(true) {
                        DNSQuestion var8;
                        try {
                           if (!var255.hasNext()) {
                              break;
                           }

                           var8 = (DNSQuestion)var255.next();
                        } catch (Throwable var241) {
                           var10000 = var241;
                           var10001 = false;
                           break label2202;
                        }

                        var250 = var249;
                        if (var8 != null) {
                           try {
                              var250 = this.addQuestion(var249, var8);
                           } catch (Throwable var238) {
                              var10000 = var238;
                              var10001 = false;
                              break label2202;
                           }
                        }

                        var249 = var250;
                     }

                     Iterator var252;
                     try {
                        var252 = var6.iterator();
                     } catch (Throwable var237) {
                        var10000 = var237;
                        var10001 = false;
                        break;
                     }

                     while(true) {
                        try {
                           if (!var252.hasNext()) {
                              break;
                           }

                           var7 = (DNSRecord)var252.next();
                        } catch (Throwable var240) {
                           var10000 = var240;
                           var10001 = false;
                           break label2202;
                        }

                        var250 = var249;
                        if (var7 != null) {
                           try {
                              var250 = this.addAnswer(var249, this._in, var7);
                           } catch (Throwable var236) {
                              var10000 = var236;
                              var10001 = false;
                              break label2202;
                           }
                        }

                        var249 = var250;
                     }

                     try {
                        if (!var249.isEmpty()) {
                           this.getDns().send(var249);
                        }
                     } catch (Throwable var235) {
                        var10000 = var235;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     return;
                  } catch (Throwable var234) {
                     var10000 = var234;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var256.addAnswers(this.getDns(), var6);
               } catch (Throwable var246) {
                  var10000 = var246;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var251 = var10000;
         Logger var254 = logger;
         StringBuilder var253 = new StringBuilder();
         var253.append(this.getName());
         var253.append("run() exception ");
         var254.warn(var253.toString(), var251);
         this.getDns().close();
      }

   }

   public void start(Timer var1) {
      boolean var4 = true;
      Iterator var5 = this._in.getQuestions().iterator();

      while(var5.hasNext()) {
         DNSQuestion var6 = (DNSQuestion)var5.next();
         logger.trace("{}.start() question={}", this.getName(), var6);
         var4 = var6.iAmTheOnlyOne(this.getDns());
         if (!var4) {
            break;
         }
      }

      int var2;
      if (var4 && !this._in.isTruncated()) {
         var2 = 0;
      } else {
         var2 = JmDNSImpl.getRandom().nextInt(96) + 20 - this._in.elapseSinceArrival();
      }

      int var3 = var2;
      if (var2 < 0) {
         var3 = 0;
      }

      logger.trace("{}.start() Responder chosen delay={}", this.getName(), var3);
      if (!this.getDns().isCanceling() && !this.getDns().isCanceled()) {
         var1.schedule(this, (long)var3);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append(" incomming: ");
      var1.append(this._in);
      return var1.toString();
   }
}
