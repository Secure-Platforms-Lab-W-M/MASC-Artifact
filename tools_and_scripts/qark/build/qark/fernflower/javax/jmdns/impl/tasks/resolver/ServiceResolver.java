package javax.jmdns.impl.tasks.resolver;

import java.io.IOException;
import java.util.Iterator;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public class ServiceResolver extends DNSResolverTask {
   private final String _type;

   public ServiceResolver(JmDNSImpl var1, String var2) {
      super(var1);
      this._type = var2;
   }

   protected DNSOutgoing addAnswers(DNSOutgoing var1) throws IOException {
      long var2 = System.currentTimeMillis();

      ServiceInfo var5;
      for(Iterator var4 = this.getDns().getServices().values().iterator(); var4.hasNext(); var1 = this.addAnswer(var1, new DNSRecord.Pointer(var5.getType(), DNSRecordClass.CLASS_IN, false, DNSConstants.DNS_TTL, var5.getQualifiedName()), var2)) {
         var5 = (ServiceInfo)var4.next();
      }

      return var1;
   }

   protected DNSOutgoing addQuestions(DNSOutgoing var1) throws IOException {
      return this.addQuestion(var1, DNSQuestion.newQuestion(this._type, DNSRecordType.TYPE_PTR, DNSRecordClass.CLASS_IN, false));
   }

   protected String description() {
      return "querying service";
   }

   public String getName() {
      StringBuilder var2 = new StringBuilder();
      var2.append("ServiceResolver(");
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
