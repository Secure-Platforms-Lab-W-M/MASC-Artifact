package javax.jmdns.impl.tasks.state;

import java.io.IOException;
import java.util.List;
import javax.jmdns.impl.DNSOutgoing;
import javax.jmdns.impl.JmDNSImpl;
import javax.jmdns.impl.ServiceInfoImpl;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSState;
import javax.jmdns.impl.tasks.DNSTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DNSStateTask extends DNSTask {
   private static int _defaultTTL;
   static Logger logger = LoggerFactory.getLogger(DNSStateTask.class.getName());
   private DNSState _taskState = null;
   private final int _ttl;

   static {
      _defaultTTL = DNSConstants.DNS_TTL;
   }

   public DNSStateTask(JmDNSImpl var1, int var2) {
      super(var1);
      this._ttl = var2;
   }

   public static int defaultTTL() {
      return _defaultTTL;
   }

   public static void setDefaultTTL(int var0) {
      _defaultTTL = var0;
   }

   protected void advanceObjectsState(List param1) {
      // $FF: Couldn't be decompiled
   }

   protected abstract void advanceTask();

   protected void associate(DNSState param1) {
      // $FF: Couldn't be decompiled
   }

   protected abstract DNSOutgoing buildOutgoingForDNS(DNSOutgoing var1) throws IOException;

   protected abstract DNSOutgoing buildOutgoingForInfo(ServiceInfoImpl var1, DNSOutgoing var2) throws IOException;

   protected abstract boolean checkRunCondition();

   protected abstract DNSOutgoing createOugoing();

   public int getTTL() {
      return this._ttl;
   }

   public abstract String getTaskDescription();

   protected DNSState getTaskState() {
      return this._taskState;
   }

   protected abstract void recoverTask(Throwable var1);

   protected void removeAssociation() {
      // $FF: Couldn't be decompiled
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   protected void setTaskState(DNSState var1) {
      this._taskState = var1;
   }
}
