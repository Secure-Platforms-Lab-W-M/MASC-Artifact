package javax.media.rtp;

import java.io.IOException;
import java.util.Vector;
import javax.media.Controls;
import javax.media.Format;
import javax.media.format.UnsupportedFormatException;
import javax.media.protocol.DataSource;
import javax.media.rtp.rtcp.SourceDescription;

@Deprecated
public interface SessionManager extends Controls {
   long SSRC_UNSPEC = 0L;

   void addFormat(Format var1, int var2);

   void addPeer(SessionAddress var1) throws IOException, InvalidSessionAddressException;

   void addReceiveStreamListener(ReceiveStreamListener var1);

   void addRemoteListener(RemoteListener var1);

   void addSendStreamListener(SendStreamListener var1);

   void addSessionListener(SessionListener var1);

   void closeSession(String var1);

   SendStream createSendStream(int var1, DataSource var2, int var3) throws UnsupportedFormatException, SSRCInUseException, IOException;

   SendStream createSendStream(DataSource var1, int var2) throws UnsupportedFormatException, IOException;

   String generateCNAME();

   long generateSSRC();

   Vector getActiveParticipants();

   Vector getAllParticipants();

   long getDefaultSSRC();

   GlobalReceptionStats getGlobalReceptionStats();

   GlobalTransmissionStats getGlobalTransmissionStats();

   LocalParticipant getLocalParticipant();

   SessionAddress getLocalSessionAddress();

   int getMulticastScope();

   Vector getPassiveParticipants();

   Vector getPeers();

   Vector getReceiveStreams();

   Vector getRemoteParticipants();

   Vector getSendStreams();

   SessionAddress getSessionAddress();

   RTPStream getStream(long var1);

   int initSession(SessionAddress var1, long var2, SourceDescription[] var4, double var5, double var7) throws InvalidSessionAddressException;

   int initSession(SessionAddress var1, SourceDescription[] var2, double var3, double var5) throws InvalidSessionAddressException;

   void removeAllPeers();

   void removePeer(SessionAddress var1);

   void removeReceiveStreamListener(ReceiveStreamListener var1);

   void removeRemoteListener(RemoteListener var1);

   void removeSendStreamListener(SendStreamListener var1);

   void removeSessionListener(SessionListener var1);

   void setMulticastScope(int var1);

   int startSession(int var1, EncryptionInfo var2) throws IOException;

   int startSession(SessionAddress var1, int var2, EncryptionInfo var3) throws IOException, InvalidSessionAddressException;

   int startSession(SessionAddress var1, SessionAddress var2, SessionAddress var3, EncryptionInfo var4) throws IOException, InvalidSessionAddressException;
}
