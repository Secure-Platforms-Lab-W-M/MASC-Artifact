package javax.jmdns.impl.tasks.resolver;

import java.io.IOException;
import java.util.Iterator;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public class TypeResolver extends DNSResolverTask {
   public TypeResolver(JmDNSImpl var1) {
      super(var1);
   }

   protected DNSOutgoing addAnswers(DNSOutgoing var1) throws IOException {
      long var2 = System.currentTimeMillis();

      JmDNSImpl.ServiceTypeEntry var5;
      for(Iterator var4 = this.getDns().getServiceTypes().values().iterator(); var4.hasNext(); var1 = this.addAnswer(var1, new DNSRecord.Pointer("_services._dns-sd._udp.local.", DNSRecordClass.CLASS_IN, false, DNSConstants.DNS_TTL, var5.getType()), var2)) {
         var5 = (JmDNSImpl.ServiceTypeEntry)var4.next();
      }

      return var1;
   }

   protected DNSOutgoing addQuestions(DNSOutgoing var1) throws IOException {
      return this.addQuestion(var1, DNSQuestion.newQuestion("_services._dns-sd._udp.local.", DNSRecordType.TYPE_PTR, DNSRecordClass.CLASS_IN, false));
   }

   protected String description() {
      return "querying type";
   }

   public String getName() {
      StringBuilder var2 = new StringBuilder();
      var2.append("TypeResolver(");
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
}
