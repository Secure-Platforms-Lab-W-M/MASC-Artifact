package javax.jmdns.impl.tasks;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.jmdns.impl.DNSIncoming;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;

public abstract class DNSTask extends TimerTask {
   private final JmDNSImpl _jmDNSImpl;

   protected DNSTask(JmDNSImpl var1) {
      this._jmDNSImpl = var1;
   }

   public DNSOutgoing addAdditionalAnswer(DNSOutgoing var1, DNSIncoming var2, DNSRecord var3) throws IOException {
      try {
         var1.addAdditionalAnswer(var2, var3);
         return var1;
      } catch (IOException var9) {
         int var4 = var1.getFlags();
         boolean var7 = var1.isMulticast();
         int var5 = var1.getMaxUDPPayload();
         int var6 = var1.getId();
         var1.setFlags(var4 | 512);
         var1.setId(var6);
         this._jmDNSImpl.send(var1);
         var1 = new DNSOutgoing(var4, var7, var5);
         var1.addAdditionalAnswer(var2, var3);
         return var1;
      }
   }

   public DNSOutgoing addAnswer(DNSOutgoing var1, DNSIncoming var2, DNSRecord var3) throws IOException {
      try {
         var1.addAnswer(var2, var3);
         return var1;
      } catch (IOException var9) {
         int var4 = var1.getFlags();
         boolean var7 = var1.isMulticast();
         int var5 = var1.getMaxUDPPayload();
         int var6 = var1.getId();
         var1.setFlags(var4 | 512);
         var1.setId(var6);
         this._jmDNSImpl.send(var1);
         var1 = new DNSOutgoing(var4, var7, var5);
         var1.addAnswer(var2, var3);
         return var1;
      }
   }

   public DNSOutgoing addAnswer(DNSOutgoing var1, DNSRecord var2, long var3) throws IOException {
      try {
         var1.addAnswer(var2, var3);
         return var1;
      } catch (IOException var10) {
         int var5 = var1.getFlags();
         boolean var8 = var1.isMulticast();
         int var6 = var1.getMaxUDPPayload();
         int var7 = var1.getId();
         var1.setFlags(var5 | 512);
         var1.setId(var7);
         this._jmDNSImpl.send(var1);
         var1 = new DNSOutgoing(var5, var8, var6);
         var1.addAnswer(var2, var3);
         return var1;
      }
   }

   public DNSOutgoing addAuthoritativeAnswer(DNSOutgoing var1, DNSRecord var2) throws IOException {
      try {
         var1.addAuthorativeAnswer(var2);
         return var1;
      } catch (IOException var8) {
         int var3 = var1.getFlags();
         boolean var6 = var1.isMulticast();
         int var4 = var1.getMaxUDPPayload();
         int var5 = var1.getId();
         var1.setFlags(var3 | 512);
         var1.setId(var5);
         this._jmDNSImpl.send(var1);
         var1 = new DNSOutgoing(var3, var6, var4);
         var1.addAuthorativeAnswer(var2);
         return var1;
      }
   }

   public DNSOutgoing addQuestion(DNSOutgoing var1, DNSQuestion var2) throws IOException {
      try {
         var1.addQuestion(var2);
         return var1;
      } catch (IOException var8) {
         int var3 = var1.getFlags();
         boolean var6 = var1.isMulticast();
         int var4 = var1.getMaxUDPPayload();
         int var5 = var1.getId();
         var1.setFlags(var3 | 512);
         var1.setId(var5);
         this._jmDNSImpl.send(var1);
         var1 = new DNSOutgoing(var3, var6, var4);
         var1.addQuestion(var2);
         return var1;
      }
   }

   public JmDNSImpl getDns() {
      return this._jmDNSImpl;
   }

   public abstract String getName();

   public abstract void start(Timer var1);

   public String toString() {
      return this.getName();
   }
}
