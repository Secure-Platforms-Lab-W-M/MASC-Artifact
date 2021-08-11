package javax.jmdns.impl.tasks.state;

import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSQuestion;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.ServiceInfoImpl;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.constants.DNSState;

public class Prober extends DNSStateTask {
   public Prober(JmDNSImpl var1) {
      super(var1, defaultTTL());
      this.setTaskState(DNSState.PROBING_1);
      this.associate(DNSState.PROBING_1);
   }

   protected void advanceTask() {
      this.setTaskState(this.getTaskState().advance());
      if (!this.getTaskState().isProbing()) {
         this.cancel();
         this.getDns().startAnnouncer();
      }

   }

   protected DNSOutgoing buildOutgoingForDNS(DNSOutgoing var1) throws IOException {
      var1.addQuestion(DNSQuestion.newQuestion(this.getDns().getLocalHost().getName(), DNSRecordType.TYPE_ANY, DNSRecordClass.CLASS_IN, false));

      for(Iterator var2 = this.getDns().getLocalHost().answers(DNSRecordClass.CLASS_ANY, false, this.getTTL()).iterator(); var2.hasNext(); var1 = this.addAuthoritativeAnswer(var1, (DNSRecord)var2.next())) {
      }

      return var1;
   }

   protected DNSOutgoing buildOutgoingForInfo(ServiceInfoImpl var1, DNSOutgoing var2) throws IOException {
      return this.addAuthoritativeAnswer(this.addQuestion(var2, DNSQuestion.newQuestion(var1.getQualifiedName(), DNSRecordType.TYPE_ANY, DNSRecordClass.CLASS_IN, false)), new DNSRecord.Service(var1.getQualifiedName(), DNSRecordClass.CLASS_IN, false, this.getTTL(), var1.getPriority(), var1.getWeight(), var1.getPort(), this.getDns().getLocalHost().getName()));
   }

   public boolean cancel() {
      this.removeAssociation();
      return super.cancel();
   }

   protected boolean checkRunCondition() {
      return !this.getDns().isCanceling() && !this.getDns().isCanceled();
   }

   protected DNSOutgoing createOugoing() {
      return new DNSOutgoing(0);
   }

   public String getName() {
      StringBuilder var2 = new StringBuilder();
      var2.append("Prober(");
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

   public String getTaskDescription() {
      return "probing";
   }

   protected void recoverTask(Throwable var1) {
      this.getDns().recover();
   }

   public void start(Timer var1) {
      long var2 = System.currentTimeMillis();
      if (var2 - this.getDns().getLastThrottleIncrement() < 5000L) {
         this.getDns().setThrottle(this.getDns().getThrottle() + 1);
      } else {
         this.getDns().setThrottle(1);
      }

      this.getDns().setLastThrottleIncrement(var2);
      if (this.getDns().isAnnounced() && this.getDns().getThrottle() < 10) {
         var1.schedule(this, (long)JmDNSImpl.getRandom().nextInt(251), 250L);
      } else {
         if (!this.getDns().isCanceling() && !this.getDns().isCanceled()) {
            var1.schedule(this, 1000L, 1000L);
         }

      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append(" state: ");
      var1.append(this.getTaskState());
      return var1.toString();
   }
}
