package javax.jmdns.impl.tasks.resolver;

import java.io.IOException;
import java.util.Iterator;
import javax.jmdns.impl.DNSEntry;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.ServiceInfoImpl;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;

public class ServiceInfoResolver extends DNSResolverTask {
   private final ServiceInfoImpl _info;

   public ServiceInfoResolver(JmDNSImpl var1, ServiceInfoImpl var2) {
      super(var1);
      this._info = var2;
      var2.setDns(this.getDns());
      this.getDns().addListener(var2, DNSQuestion.newQuestion(var2.getQualifiedName(), DNSRecordType.TYPE_ANY, DNSRecordClass.CLASS_IN, false));
   }

   protected DNSOutgoing addAnswers(DNSOutgoing var1) throws IOException {
      DNSOutgoing var4 = var1;
      var1 = var1;
      if (!this._info.hasData()) {
         long var2 = System.currentTimeMillis();
         var4 = this.addAnswer(this.addAnswer(var4, (DNSRecord)this.getDns().getCache().getDNSEntry(this._info.getQualifiedName(), DNSRecordType.TYPE_SRV, DNSRecordClass.CLASS_IN), var2), (DNSRecord)this.getDns().getCache().getDNSEntry(this._info.getQualifiedName(), DNSRecordType.TYPE_TXT, DNSRecordClass.CLASS_IN), var2);
         var1 = var4;
         if (this._info.getServer().length() > 0) {
            Iterator var5 = this.getDns().getCache().getDNSEntryList(this._info.getServer(), DNSRecordType.TYPE_A, DNSRecordClass.CLASS_IN).iterator();

            for(var1 = var4; var5.hasNext(); var1 = this.addAnswer(var1, (DNSRecord)((DNSEntry)var5.next()), var2)) {
            }

            var5 = this.getDns().getCache().getDNSEntryList(this._info.getServer(), DNSRecordType.TYPE_AAAA, DNSRecordClass.CLASS_IN).iterator();
            var4 = var1;

            while(true) {
               var1 = var4;
               if (!var5.hasNext()) {
                  break;
               }

               var4 = this.addAnswer(var4, (DNSRecord)((DNSEntry)var5.next()), var2);
            }
         }
      }

      return var1;
   }

   protected DNSOutgoing addQuestions(DNSOutgoing var1) throws IOException {
      DNSOutgoing var2 = var1;
      var1 = var1;
      if (!this._info.hasData()) {
         var2 = this.addQuestion(this.addQuestion(var2, DNSQuestion.newQuestion(this._info.getQualifiedName(), DNSRecordType.TYPE_SRV, DNSRecordClass.CLASS_IN, false)), DNSQuestion.newQuestion(this._info.getQualifiedName(), DNSRecordType.TYPE_TXT, DNSRecordClass.CLASS_IN, false));
         var1 = var2;
         if (this._info.getServer().length() > 0) {
            var1 = this.addQuestion(this.addQuestion(var2, DNSQuestion.newQuestion(this._info.getServer(), DNSRecordType.TYPE_A, DNSRecordClass.CLASS_IN, false)), DNSQuestion.newQuestion(this._info.getServer(), DNSRecordType.TYPE_AAAA, DNSRecordClass.CLASS_IN, false));
         }
      }

      return var1;
   }

   public boolean cancel() {
      boolean var1 = super.cancel();
      if (!this._info.isPersistent()) {
         this.getDns().removeListener(this._info);
      }

      return var1;
   }

   protected String description() {
      StringBuilder var2 = new StringBuilder();
      var2.append("querying service info: ");
      ServiceInfoImpl var1 = this._info;
      String var3;
      if (var1 != null) {
         var3 = var1.getQualifiedName();
      } else {
         var3 = "null";
      }

      var2.append(var3);
      return var2.toString();
   }

   public String getName() {
      StringBuilder var2 = new StringBuilder();
      var2.append("ServiceInfoResolver(");
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
