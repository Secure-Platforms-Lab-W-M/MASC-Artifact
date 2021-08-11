package javax.jmdns.impl.tasks.state;

import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import javax.jmdns.impl.DNSIncoming;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.DNSRecord;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.ServiceInfoImpl;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSState;

public class Renewer extends DNSStateTask {
   public Renewer(JmDNSImpl var1) {
      super(var1, defaultTTL());
      this.setTaskState(DNSState.ANNOUNCED);
      this.associate(DNSState.ANNOUNCED);
   }

   protected void advanceTask() {
      this.setTaskState(this.getTaskState().advance());
      if (!this.getTaskState().isAnnounced()) {
         this.cancel();
      }

   }

   protected DNSOutgoing buildOutgoingForDNS(DNSOutgoing var1) throws IOException {
      for(Iterator var2 = this.getDns().getLocalHost().answers(DNSRecordClass.CLASS_ANY, true, this.getTTL()).iterator(); var2.hasNext(); var1 = this.addAnswer(var1, (DNSIncoming)null, (DNSRecord)var2.next())) {
      }

      return var1;
   }

   protected DNSOutgoing buildOutgoingForInfo(ServiceInfoImpl var1, DNSOutgoing var2) throws IOException {
      for(Iterator var3 = var1.answers(DNSRecordClass.CLASS_ANY, true, this.getTTL(), this.getDns().getLocalHost()).iterator(); var3.hasNext(); var2 = this.addAnswer(var2, (DNSIncoming)null, (DNSRecord)var3.next())) {
      }

      return var2;
   }

   public boolean cancel() {
      this.removeAssociation();
      return super.cancel();
   }

   protected boolean checkRunCondition() {
      return !this.getDns().isCanceling() && !this.getDns().isCanceled();
   }

   protected DNSOutgoing createOugoing() {
      return new DNSOutgoing(33792);
   }

   public String getName() {
      StringBuilder var2 = new StringBuilder();
      var2.append("Renewer(");
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
      return "renewing";
   }

   protected void recoverTask(Throwable var1) {
      this.getDns().recover();
   }

   public void start(Timer var1) {
      if (!this.getDns().isCanceling() && !this.getDns().isCanceled()) {
         var1.schedule(this, (long)DNSConstants.ANNOUNCED_RENEWAL_TTL_INTERVAL, (long)DNSConstants.ANNOUNCED_RENEWAL_TTL_INTERVAL);
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
