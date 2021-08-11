package javax.media.rtp;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Controls;
import javax.media.Format;
import javax.media.PackageManager;
import javax.media.format.UnsupportedFormatException;
import javax.media.protocol.DataSource;
import javax.media.rtp.rtcp.SourceDescription;
import net.sf.fmj.utility.LoggerSingleton;

public abstract class RTPManager implements Controls {
   private static final Logger logger;

   static {
      logger = LoggerSingleton.logger;
   }

   public static Vector getRTPManagerList() {
      Vector var0 = new Vector();
      Iterator var1 = PackageManager.getProtocolPrefixList().iterator();

      while(var1.hasNext()) {
         Object var2 = var1.next();
         StringBuilder var3 = new StringBuilder();
         var3.append(var2);
         var3.append(".media.rtp.RTPSessionMgr");
         var0.add(var3.toString());
      }

      return var0;
   }

   public static RTPManager newInstance() {
      Iterator var0 = getRTPManagerList().iterator();

      while(var0.hasNext()) {
         String var1 = (String)var0.next();

         Logger var2;
         StringBuilder var8;
         try {
            var2 = logger;
            var8 = new StringBuilder();
            var8.append("Trying RTPManager class: ");
            var8.append(var1);
            var2.finer(var8.toString());
            RTPManager var7 = (RTPManager)Class.forName(var1).newInstance();
            return var7;
         } catch (ClassNotFoundException var5) {
            var2 = logger;
            var8 = new StringBuilder();
            var8.append("RTPManager.newInstance: ClassNotFoundException: ");
            var8.append(var1);
            var2.finer(var8.toString());
         } catch (Exception var6) {
            var2 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var6);
            var2.log(var3, var4.toString(), var6);
         }
      }

      return null;
   }

   public abstract void addFormat(Format var1, int var2);

   public abstract void addReceiveStreamListener(ReceiveStreamListener var1);

   public abstract void addRemoteListener(RemoteListener var1);

   public abstract void addSendStreamListener(SendStreamListener var1);

   public abstract void addSessionListener(SessionListener var1);

   public abstract void addTarget(SessionAddress var1) throws InvalidSessionAddressException, IOException;

   public abstract SendStream createSendStream(DataSource var1, int var2) throws UnsupportedFormatException, IOException;

   public abstract void dispose();

   public abstract Vector getActiveParticipants();

   public abstract Vector getAllParticipants();

   public abstract GlobalReceptionStats getGlobalReceptionStats();

   public abstract GlobalTransmissionStats getGlobalTransmissionStats();

   public abstract LocalParticipant getLocalParticipant();

   public abstract Vector getPassiveParticipants();

   public abstract Vector getReceiveStreams();

   public abstract Vector getRemoteParticipants();

   public abstract Vector getSendStreams();

   public abstract void initialize(RTPConnector var1);

   public abstract void initialize(SessionAddress var1) throws InvalidSessionAddressException, IOException;

   public abstract void initialize(SessionAddress[] var1, SourceDescription[] var2, double var3, double var5, EncryptionInfo var7) throws InvalidSessionAddressException, IOException;

   public abstract void removeReceiveStreamListener(ReceiveStreamListener var1);

   public abstract void removeRemoteListener(RemoteListener var1);

   public abstract void removeSendStreamListener(SendStreamListener var1);

   public abstract void removeSessionListener(SessionListener var1);

   public abstract void removeTarget(SessionAddress var1, String var2) throws InvalidSessionAddressException;

   public abstract void removeTargets(String var1);
}
