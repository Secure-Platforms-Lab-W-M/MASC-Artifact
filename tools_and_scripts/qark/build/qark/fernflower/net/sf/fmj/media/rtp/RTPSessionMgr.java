package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Format;
import javax.media.control.BufferControl;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.rtp.EncryptionInfo;
import javax.media.rtp.GlobalReceptionStats;
import javax.media.rtp.GlobalTransmissionStats;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.LocalParticipant;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPConnector;
import javax.media.rtp.RTPControl;
import javax.media.rtp.RTPManager;
import javax.media.rtp.RTPPushDataSource;
import javax.media.rtp.RTPSocket;
import javax.media.rtp.RTPStream;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.RemoteListener;
import javax.media.rtp.RemoteParticipant;
import javax.media.rtp.SSRCInUseException;
import javax.media.rtp.SendStream;
import javax.media.rtp.SendStreamListener;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.SessionListener;
import javax.media.rtp.SessionManager;
import javax.media.rtp.SessionManagerException;
import javax.media.rtp.event.NewSendStreamEvent;
import javax.media.rtp.event.StreamClosedEvent;
import javax.media.rtp.rtcp.SourceDescription;
import net.sf.fmj.media.protocol.rtp.DataSource;
import net.sf.fmj.media.rtp.util.PacketForwarder;
import net.sf.fmj.media.rtp.util.RTPPacketSender;
import net.sf.fmj.media.rtp.util.SSRCTable;
import net.sf.fmj.media.rtp.util.UDPPacketSender;

public class RTPSessionMgr extends RTPManager implements SessionManager {
   private static final int GET_ALL_PARTICIPANTS = -1;
   private static final String SOURCE_DESC_EMAIL = "fmj-devel@lists.sourceforge.net";
   private static final String SOURCE_DESC_TOOL = "FMJ RTP Player";
   static Vector addedList = new Vector();
   private static final Logger logger = Logger.getLogger(RTPSessionMgr.class.getName());
   static FormatInfo supportedList = null;
   private final int MAX_PORT = 65535;
   boolean bds = false;
   boolean bindtome = false;
   BufferControl buffercontrol = null;
   private SSRCCache cache = null;
   SSRCCacheCleaner cleaner = null;
   private DatagramSocket controlSocket = null;
   InetAddress controladdress = null;
   int controlport = 0;
   private DatagramSocket dataSocket = null;
   InetAddress dataaddress = null;
   int dataport = 0;
   long defaultSSRC = 0L;
   Format defaultformat = null;
   DataSource defaultsource = null;
   int defaultsourceid = 0;
   public OverallStats defaultstats = null;
   PushBufferStream defaultstream = null;
   final SSRCTable dslist = new SSRCTable();
   boolean encryption = false;
   FormatInfo formatinfo = null;
   private boolean initialized = false;
   private SessionAddress localAddress = null;
   InetAddress localControlAddress = null;
   int localControlPort = 0;
   InetAddress localDataAddress = null;
   int localDataPort = 0;
   private SessionAddress localReceiverAddress = null;
   SessionAddress localSenderAddress = null;
   boolean multi_unicast = false;
   private boolean newRtpInterface = false;
   private boolean nonparticipating = false;
   private boolean nosockets = false;
   private OverallStats overallStats = null;
   private boolean participating = false;
   Vector peerlist = null;
   Hashtable peerrtcplist = null;
   Hashtable peerrtplist = null;
   private SessionAddress remoteAddress = null;
   private Vector remoteAddresses = null;
   protected Vector remotelistener = null;
   private PacketForwarder rtcpForwarder = null;
   private RTCPRawReceiver rtcpRawReceiver = null;
   private RTCPTransmitter rtcpTransmitter = null;
   private RTCPTransmitterFactory rtcpTransmitterFactory;
   RTPPushDataSource rtcpsource = null;
   private RTPConnector rtpConnector = null;
   private RTPDemultiplexer rtpDemultiplexer = null;
   private PacketForwarder rtpForwarder = null;
   private RTPRawReceiver rtpRawReceiver = null;
   RTPTransmitter rtpTransmitter = null;
   RTPPacketSender rtpsender = null;
   RTPPushDataSource rtpsource = null;
   RTCPRawSender sender = null;
   int sendercount = 0;
   Vector sendstreamlist = null;
   protected Vector sendstreamlistener = null;
   protected Vector sessionlistener = null;
   private boolean started = false;
   private boolean startedparticipating = false;
   StreamSynch streamSynch = null;
   protected Vector streamlistener = null;
   public OverallTransStats transstats = null;
   int ttl = 0;
   private UDPPacketSender udpPacketSender = null;
   UDPPacketSender udpsender = null;
   private boolean unicast = false;

   public RTPSessionMgr() {
      this.bindtome = false;
      this.localDataAddress = null;
      this.localDataPort = 0;
      this.localControlAddress = null;
      this.localControlPort = 0;
      this.dataaddress = null;
      this.controladdress = null;
      this.dataport = 0;
      this.controlport = 0;
      this.rtpsource = null;
      this.rtcpsource = null;
      this.defaultSSRC = 0L;
      this.udpsender = null;
      this.rtpsender = null;
      this.sender = null;
      this.cleaner = null;
      this.unicast = false;
      this.startedparticipating = false;
      this.nonparticipating = false;
      this.nosockets = false;
      this.started = false;
      this.initialized = false;
      this.sessionlistener = new Vector();
      this.remotelistener = new Vector();
      this.streamlistener = new Vector();
      this.sendstreamlistener = new Vector();
      this.encryption = false;
      this.formatinfo = null;
      this.defaultsource = null;
      this.defaultstream = null;
      this.defaultformat = null;
      this.buffercontrol = null;
      this.defaultstats = null;
      this.transstats = null;
      this.defaultsourceid = 0;
      this.sendstreamlist = new Vector(1);
      this.rtpTransmitter = null;
      this.bds = false;
      this.peerlist = new Vector();
      this.multi_unicast = false;
      this.peerrtplist = new Hashtable(5);
      this.peerrtcplist = new Hashtable(5);
      this.newRtpInterface = false;
      this.formatinfo = new FormatInfo();
      this.buffercontrol = new BufferControlImpl();
      this.defaultstats = new OverallStats();
      this.transstats = new OverallTransStats();
      this.streamSynch = new StreamSynch();
   }

   public RTPSessionMgr(RTPPushDataSource var1) {
      this.bindtome = false;
      this.localDataAddress = null;
      this.localDataPort = 0;
      this.localControlAddress = null;
      this.localControlPort = 0;
      this.dataaddress = null;
      this.controladdress = null;
      this.dataport = 0;
      this.controlport = 0;
      this.rtpsource = null;
      this.rtcpsource = null;
      this.defaultSSRC = 0L;
      this.udpsender = null;
      this.rtpsender = null;
      this.sender = null;
      this.cleaner = null;
      this.unicast = false;
      this.startedparticipating = false;
      this.nonparticipating = false;
      this.nosockets = false;
      this.started = false;
      this.initialized = false;
      this.sessionlistener = new Vector();
      this.remotelistener = new Vector();
      this.streamlistener = new Vector();
      this.sendstreamlistener = new Vector();
      this.encryption = false;
      this.formatinfo = null;
      this.defaultsource = null;
      this.defaultstream = null;
      this.defaultformat = null;
      this.buffercontrol = null;
      this.defaultstats = null;
      this.transstats = null;
      this.defaultsourceid = 0;
      this.sendstreamlist = new Vector(1);
      this.rtpTransmitter = null;
      this.bds = false;
      this.peerlist = new Vector();
      this.multi_unicast = false;
      this.peerrtplist = new Hashtable(5);
      this.peerrtcplist = new Hashtable(5);
      this.newRtpInterface = false;
      this.nosockets = true;
      this.rtpsource = var1;
      if (var1 instanceof RTPSocket) {
         this.rtcpsource = ((RTPSocket)var1).getControlChannel();
      }

      this.formatinfo = new FormatInfo();
      this.buffercontrol = new BufferControlImpl();
      this.defaultstats = new OverallStats();
      this.transstats = new OverallTransStats();
      DataSource var2 = this.createNewDS((RTPMediaLocator)null);
      this.UpdateEncodings(var1);
      var2.setControl((RTPControl)var1.getControl(RTPControl.class.getName()));
      this.initSession(this.setSDES(), 0.05D, 0.25D);
      this.startSession(this.rtpsource, this.rtcpsource, (EncryptionInfo)null);
   }

   public RTPSessionMgr(DataSource var1) throws IOException {
      this.bindtome = false;
      this.localDataAddress = null;
      this.localDataPort = 0;
      this.localControlAddress = null;
      this.localControlPort = 0;
      this.dataaddress = null;
      this.controladdress = null;
      this.dataport = 0;
      this.controlport = 0;
      this.rtpsource = null;
      this.rtcpsource = null;
      this.defaultSSRC = 0L;
      this.udpsender = null;
      this.rtpsender = null;
      this.sender = null;
      this.cleaner = null;
      this.unicast = false;
      this.startedparticipating = false;
      this.nonparticipating = false;
      this.nosockets = false;
      this.started = false;
      this.initialized = false;
      this.sessionlistener = new Vector();
      this.remotelistener = new Vector();
      this.streamlistener = new Vector();
      this.sendstreamlistener = new Vector();
      this.encryption = false;
      this.formatinfo = null;
      this.defaultsource = null;
      this.defaultstream = null;
      this.defaultformat = null;
      this.buffercontrol = null;
      this.defaultstats = null;
      this.transstats = null;
      this.defaultsourceid = 0;
      this.sendstreamlist = new Vector(1);
      this.rtpTransmitter = null;
      this.bds = false;
      this.peerlist = new Vector();
      this.multi_unicast = false;
      this.peerrtplist = new Hashtable(5);
      this.peerrtcplist = new Hashtable(5);
      this.newRtpInterface = false;
      this.formatinfo = new FormatInfo();
      this.buffercontrol = new BufferControlImpl();
      this.defaultstats = new OverallStats();
      this.transstats = new OverallTransStats();
      this.UpdateEncodings(var1);

      StringBuilder var3;
      RTPMediaLocator var15;
      try {
         var15 = new RTPMediaLocator(var1.getLocator().toString());
      } catch (MalformedURLException var11) {
         var3 = new StringBuilder();
         var3.append("RTP URL is Malformed ");
         var3.append(var11.getMessage());
         throw new IOException(var3.toString());
      }

      this.createNewDS(var15).setControl((RTPControl)var1.getControl("javax.media.rtp.RTPControl"));
      String var13 = var15.getSessionAddress();
      int var2 = var15.getSessionPort();
      this.dataport = var2;
      this.controlport = var2 + 1;
      this.ttl = var15.getTTL();

      label47:
      try {
         this.dataaddress = InetAddress.getByName(var13);
      } catch (Throwable var12) {
         Logger var4 = logger;
         Level var5 = Level.WARNING;
         StringBuilder var6 = new StringBuilder();
         var6.append("error retrieving address ");
         var6.append(var13);
         var6.append(" by name");
         var6.append(var12.getMessage());
         var4.log(var5, var6.toString(), var12);
         break label47;
      }

      this.controladdress = this.dataaddress;
      SessionAddress var14 = new SessionAddress();

      try {
         this.initSession(var14, this.setSDES(), 0.05D, 0.25D);
      } catch (SessionManagerException var10) {
         var3 = new StringBuilder();
         var3.append("SessionManager exception ");
         var3.append(var10.getMessage());
         throw new IOException(var3.toString());
      }
   }

   private void CheckRTPAddress(InetAddress var1, InetAddress var2) throws InvalidSessionAddressException {
      if (var1 == null && var2 == null) {
         throw new InvalidSessionAddressException("Data and control addresses are null");
      } else {
         InetAddress var3 = var2;
         if (var2 == null) {
            var3 = var2;
            if (var1 != null) {
               var3 = var1;
            }
         }

         if (var1 == null && var3 != null) {
         }

      }
   }

   private void CheckRTPPorts(int var1, int var2) throws InvalidSessionAddressException {
      int var3;
      label37: {
         if (var1 != 0) {
            var3 = var1;
            if (var1 != -1) {
               break label37;
            }
         }

         var3 = var2 - 1;
      }

      label32: {
         if (var2 != 0) {
            var1 = var2;
            if (var2 != -1) {
               break label32;
            }
         }

         var1 = var3 + 1;
      }

      if (var3 != 0 && var3 % 2 != 0) {
         throw new InvalidSessionAddressException("Data Port must be valid and even");
      } else if (var1 != 0 && var1 % 2 != 1) {
         throw new InvalidSessionAddressException("Control Port must be valid and odd");
      } else if (var1 != var3 + 1) {
         throw new InvalidSessionAddressException("Control Port must be one higher than the Data Port");
      }
   }

   private boolean Win32() {
      return System.getProperty("os.name").startsWith("Windows");
   }

   private int findLocalPorts() {
      boolean var2 = false;
      int var1 = -1;

      while(!var2) {
         do {
            int var5 = (int)(65535.0D * Math.random());
            var1 = var5;
            if (var5 % 2 != 0) {
               var1 = var5 + 1;
            }
         } while(var1 < 1024 || var1 > 65534);

         try {
            (new DatagramSocket(var1)).close();
            (new DatagramSocket(var1 + 1)).close();
         } catch (SocketException var4) {
            var2 = false;
            continue;
         }

         var2 = true;
      }

      return var1;
   }

   public static boolean formatSupported(Format var0) {
      if (supportedList == null) {
         supportedList = new FormatInfo();
      }

      if (supportedList.getPayload(var0) != -1) {
         return true;
      } else {
         for(int var1 = 0; var1 < addedList.size(); ++var1) {
            if (((Format)addedList.elementAt(var1)).matches(var0)) {
               return true;
            }
         }

         return false;
      }
   }

   private String getProperty(String var1) {
      try {
         var1 = System.getProperty(var1);
         return var1;
      } finally {
         ;
      }
   }

   private int initSession(SourceDescription[] var1, double var2, double var4) {
      if (this.initialized) {
         return -1;
      } else {
         if (var2 == 0.0D) {
            this.nonparticipating = true;
         }

         this.defaultSSRC = this.generateSSRC(GenerateSSRCCause.INIT_SESSION);
         SSRCCache var6 = new SSRCCache(this);
         this.cache = var6;
         this.formatinfo.setCache(var6);
         this.cache.rtcp_bw_fraction = var2;
         this.cache.rtcp_sender_bw_fraction = var4;

         try {
            InetAddress.getLocalHost();
         } catch (Throwable var10) {
            Logger var11 = logger;
            Level var7 = Level.WARNING;
            StringBuilder var8 = new StringBuilder();
            var8.append("InitSession UnknownHostExcpetion ");
            var8.append(var10.getMessage());
            var11.log(var7, var8.toString(), var10);
            return -1;
         }

         var6 = this.cache;
         var6.ourssrc = var6.get((int)this.defaultSSRC, (InetAddress)null, 0, 2);
         this.cache.ourssrc.setAlive(true);
         if (!this.isCNAME(var1)) {
            var1 = this.setCNAME(var1);
            this.cache.ourssrc.setSourceDescription(var1);
         } else {
            this.cache.ourssrc.setSourceDescription(var1);
         }

         this.cache.ourssrc.ssrc = (int)this.defaultSSRC;
         this.cache.ourssrc.setOurs(true);
         this.initialized = true;
         return 0;
      }
   }

   private boolean isCNAME(SourceDescription[] var1) {
      boolean var4 = false;
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            int var3;
            String var6;
            try {
               var3 = var1[var2].getType();
               var6 = var1[var2].getDescription();
            } catch (Exception var7) {
               continue;
            }

            boolean var5 = var4;
            if (var3 == 1) {
               var5 = var4;
               if (var6 != null) {
                  var5 = true;
               }
            }

            var4 = var5;
         }

         return var4;
      }
   }

   private SourceDescription[] setCNAME(SourceDescription[] var1) {
      boolean var4 = false;
      if (var1 == null) {
         return new SourceDescription[]{new SourceDescription(1, SourceDescription.generateCNAME(), 1, false)};
      } else {
         int var2 = 0;

         boolean var3;
         int var6;
         while(true) {
            var3 = var4;
            if (var2 >= var1.length) {
               break;
            }

            var6 = var1[var2].getType();
            String var5 = var1[var2].getDescription();
            if (var6 == 1 && var5 == null) {
               SourceDescription.generateCNAME();
               var3 = true;
               break;
            }

            ++var2;
         }

         if (var3) {
            return var1;
         } else {
            SourceDescription[] var7 = new SourceDescription[var1.length + 1];
            var7[0] = new SourceDescription(1, SourceDescription.generateCNAME(), 1, false);
            var6 = 1;

            for(var2 = 0; var2 < var1.length; ++var2) {
               var7[var6] = new SourceDescription(var1[var2].getType(), var1[var2].getDescription(), 1, false);
               ++var6;
            }

            return var7;
         }
      }
   }

   private void setRemoteAddresses() {
      RTPTransmitter var1 = this.rtpTransmitter;
      if (var1 != null) {
         var1.getSender().setDestAddresses(this.remoteAddresses);
      }

      RTCPTransmitter var2 = this.rtcpTransmitter;
      if (var2 != null) {
         var2.getSender().setDestAddresses(this.remoteAddresses);
      }

   }

   private SourceDescription[] setSDES() {
      return new SourceDescription[]{new SourceDescription(2, this.getProperty("user.name"), 1, false), new SourceDescription(1, SourceDescription.generateCNAME(), 1, false), new SourceDescription(6, "FMJ RTP Player", 1, false)};
   }

   private RTPTransmitter startDataTransmission(int var1, String var2) throws IOException {
      if (this.localDataPort == -1) {
         this.udpsender = new UDPPacketSender(this.dataaddress, this.dataport);
      } else if (this.newRtpInterface) {
         this.udpsender = new UDPPacketSender(this.rtpRawReceiver.socket);
      } else {
         this.udpsender = new UDPPacketSender(this.localSenderAddress.getDataPort(), this.localSenderAddress.getDataAddress(), this.dataaddress, this.dataport);
      }

      var1 = this.ttl;
      if (var1 != 1) {
         this.udpsender.setttl(var1);
      }

      RTPRawSender var3 = new RTPRawSender(this.dataport, var2, this.udpsender);
      return new RTPTransmitter(this.cache, var3);
   }

   private RTPTransmitter startDataTransmission(RTPConnector var1) {
      try {
         RTPPacketSender var3 = new RTPPacketSender(var1);
         this.rtpsender = var3;
         RTPRawSender var4 = new RTPRawSender(var3);
         RTPTransmitter var5 = new RTPTransmitter(this.cache, var4);
         return var5;
      } catch (IOException var2) {
         return null;
      }
   }

   private RTPTransmitter startDataTransmission(RTPPushDataSource var1) {
      RTPPacketSender var2 = new RTPPacketSender(var1);
      this.rtpsender = var2;
      RTPRawSender var3 = new RTPRawSender(var2);
      return new RTPTransmitter(this.cache, var3);
   }

   private RTCPReporter startParticipating(int var1, String var2, SSRCInfo var3) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label215: {
         UDPPacketSender var4;
         boolean var10001;
         label207: {
            try {
               this.startedparticipating = true;
               if (this.localControlPort == -1) {
                  var4 = new UDPPacketSender(this.controladdress, this.controlport);
                  this.localControlPort = var4.getLocalPort();
                  this.localControlAddress = var4.getLocalAddress();
                  break label207;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label215;
            }

            try {
               var4 = new UDPPacketSender(this.localControlPort, this.localControlAddress, this.controladdress, this.controlport);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label215;
            }
         }

         try {
            if (this.ttl != 1) {
               var4.setttl(this.ttl);
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label215;
         }

         label195:
         try {
            RTCPRawSender var26 = new RTCPRawSender(var1, var2, var4);
            RTCPTransmitter var27 = this.getOrCreateRTCPTransmitterFactory().newRTCPTransmitter(this.cache, var26);
            var27.setSSRCInfo(var3);
            RTCPReporter var28 = new RTCPReporter(this.cache, var27);
            return var28;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label195;
         }
      }

      Throwable var25 = var10000;
      throw var25;
   }

   private RTCPReporter startParticipating(DatagramSocket var1) throws IOException {
      synchronized(this){}

      RTCPReporter var7;
      try {
         UDPPacketSender var4 = new UDPPacketSender(var1);
         this.udpPacketSender = var4;
         if (this.ttl != 1) {
            var4.setttl(this.ttl);
         }

         RTCPRawSender var5 = new RTCPRawSender(this.remoteAddress.getControlPort(), this.remoteAddress.getControlAddress().getHostAddress(), var4);
         RTCPTransmitter var6 = this.getOrCreateRTCPTransmitterFactory().newRTCPTransmitter(this.cache, var5);
         this.rtcpTransmitter = var6;
         var6.setSSRCInfo(this.cache.ourssrc);
         var7 = new RTCPReporter(this.cache, this.rtcpTransmitter);
         this.startedparticipating = true;
      } finally {
         ;
      }

      return var7;
   }

   private RTCPReporter startParticipating(RTPConnector param1, SSRCInfo param2) {
      // $FF: Couldn't be decompiled
   }

   private RTCPReporter startParticipating(RTPPushDataSource var1, SSRCInfo var2) {
      synchronized(this){}

      RTCPReporter var8;
      try {
         this.startedparticipating = true;
         RTPPacketSender var5 = new RTPPacketSender(var1);
         this.rtpsender = var5;
         RTCPRawSender var6 = new RTCPRawSender(var5);
         RTCPTransmitter var7 = this.getOrCreateRTCPTransmitterFactory().newRTCPTransmitter(this.cache, var6);
         var7.setSSRCInfo(var2);
         var8 = new RTCPReporter(this.cache, var7);
      } finally {
         ;
      }

      return var8;
   }

   private RTCPReporter startParticipating(SessionAddress var1, SessionAddress var2, SSRCInfo var3, DatagramSocket var4) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label350: {
         int var5;
         int var6;
         boolean var10001;
         InetAddress var50;
         try {
            this.localReceiverAddress = var1;
            this.startedparticipating = true;
            var5 = var2.getControlPort();
            var50 = var2.getControlAddress();
            var6 = var1.getControlPort();
            var1.getControlAddress();
         } catch (Throwable var48) {
            var10000 = var48;
            var10001 = false;
            break label350;
         }

         UDPPacketSender var49;
         if (var5 == -1) {
            try {
               var49 = new UDPPacketSender(var50, var5);
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label350;
            }
         } else if (var5 == var6) {
            try {
               var49 = new UDPPacketSender(var4);
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label350;
            }
         } else {
            try {
               var49 = new UDPPacketSender(var5, var50, this.controladdress, this.controlport);
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label350;
            }
         }

         try {
            if (this.ttl != 1) {
               var49.setttl(this.ttl);
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label350;
         }

         label330:
         try {
            RTCPRawSender var52 = new RTCPRawSender(this.controlport, this.controladdress.getHostAddress(), var49);
            RTCPTransmitter var53 = this.getOrCreateRTCPTransmitterFactory().newRTCPTransmitter(this.cache, var52);
            var53.setSSRCInfo(var3);
            RTCPReporter var54 = new RTCPReporter(this.cache, var53);
            return var54;
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label330;
         }
      }

      Throwable var51 = var10000;
      throw var51;
   }

   private int startSession(RTPPushDataSource var1, RTPPushDataSource var2, EncryptionInfo var3) {
      if (!this.initialized) {
         return -1;
      } else if (this.started) {
         return -1;
      } else {
         this.cache.sessionbandwidth = 384000;
         RTPRawReceiver var4 = new RTPRawReceiver(var1, this.defaultstats);
         RTCPRawReceiver var6 = new RTCPRawReceiver(var2, this.defaultstats, this.streamSynch);
         RTPDemultiplexer var5 = new RTPDemultiplexer(this.cache, var4, this.streamSynch);
         this.rtpDemultiplexer = var5;
         PacketForwarder var8 = new PacketForwarder(var4, new RTPReceiver(this.cache, var5));
         this.rtpForwarder = var8;
         if (var8 != null) {
            StringBuilder var10 = new StringBuilder();
            var10.append("RTP Forwarder ");
            var10.append(var1);
            var8.startPF(var10.toString());
         }

         PacketForwarder var7 = new PacketForwarder(var6, new RTCPReceiver(this.cache));
         this.rtcpForwarder = var7;
         if (var7 != null) {
            StringBuilder var9 = new StringBuilder();
            var9.append("RTCP Forwarder ");
            var9.append(var1);
            var7.startPF(var9.toString());
         }

         this.cleaner = new SSRCCacheCleaner(this.cache, this.streamSynch);
         if (!this.nonparticipating && this.cache.ourssrc != null) {
            this.cache.ourssrc.reporter = this.startParticipating(var2, this.cache.ourssrc);
         }

         this.started = true;
         return 0;
      }
   }

   private void stopParticipating(String var1, SSRCInfo var2) {
      synchronized(this){}

      try {
         if (var2.reporter != null) {
            var2.reporter.close(var1);
            var2.reporter = null;
         }
      } finally {
         ;
      }

   }

   public boolean IsNonParticipating() {
      return this.nonparticipating;
   }

   public void UpdateEncodings(javax.media.protocol.DataSource var1) {
      RTPControlImpl var4 = (RTPControlImpl)var1.getControl(RTPControl.class.getName());
      if (var4 != null && var4.codeclist != null) {
         Enumeration var2 = var4.codeclist.keys();

         while(var2.hasMoreElements()) {
            Integer var3 = (Integer)var2.nextElement();
            this.formatinfo.add(var3, (Format)var4.codeclist.get(var3));
         }
      }

   }

   public void addFormat(Format var1, int var2) {
      FormatInfo var3 = this.formatinfo;
      if (var3 != null) {
         var3.add(var2, var1);
      }

      if (var1 != null) {
         addedList.addElement(var1);
      }

   }

   public void addMRL(RTPMediaLocator var1) {
      int var2 = (int)var1.getSSRC();
      if (var2 != 0 && (DataSource)this.dslist.get(var2) == null) {
         this.createNewDS(var1);
      }

   }

   public void addPeer(SessionAddress param1) throws IOException, InvalidSessionAddressException {
      // $FF: Couldn't be decompiled
   }

   public void addReceiveStreamListener(ReceiveStreamListener var1) {
      if (!this.streamlistener.contains(var1)) {
         this.streamlistener.addElement(var1);
      }

   }

   public void addRemoteListener(RemoteListener var1) {
      if (!this.remotelistener.contains(var1)) {
         this.remotelistener.addElement(var1);
      }

   }

   void addSendStream(SendStream var1) {
      this.sendstreamlist.addElement(var1);
   }

   public void addSendStreamListener(SendStreamListener var1) {
      if (!this.sendstreamlistener.contains(var1)) {
         this.sendstreamlistener.addElement(var1);
      }

   }

   public void addSessionListener(SessionListener var1) {
      if (!this.sessionlistener.contains(var1)) {
         this.sessionlistener.addElement(var1);
      }

   }

   public void addTarget(SessionAddress var1) throws IOException {
      this.remoteAddresses.addElement(var1);
      if (this.remoteAddresses.size() > 1) {
         this.setRemoteAddresses();
      } else {
         this.remoteAddress = var1;
         Logger var2 = logger;
         StringBuilder var3 = new StringBuilder();
         var3.append("Added target: ");
         var3.append(var1);
         var2.finest(var3.toString());

         RTPRawReceiver var6;
         try {
            this.rtcpRawReceiver = new RTCPRawReceiver(this.localAddress, var1, this.defaultstats, this.streamSynch, this.controlSocket);
            var6 = new RTPRawReceiver(this.localAddress, var1, this.defaultstats, this.dataSocket);
            this.rtpRawReceiver = var6;
         } catch (SocketException var4) {
            throw new IOException(var4.getMessage());
         } catch (UnknownHostException var5) {
            throw new IOException(var5.getMessage());
         }

         this.rtpDemultiplexer = new RTPDemultiplexer(this.cache, var6, this.streamSynch);
         this.rtcpForwarder = new PacketForwarder(this.rtcpRawReceiver, new RTCPReceiver(this.cache));
         var6 = this.rtpRawReceiver;
         if (var6 != null) {
            this.rtpForwarder = new PacketForwarder(var6, new RTPReceiver(this.cache, this.rtpDemultiplexer));
         }

         PacketForwarder var7 = this.rtcpForwarder;
         var3 = new StringBuilder();
         var3.append("RTCP Forwarder for address");
         var3.append(var1.getControlHostAddress());
         var3.append(" port ");
         var3.append(var1.getControlPort());
         var7.startPF(var3.toString());
         var7 = this.rtpForwarder;
         if (var7 != null) {
            var3 = new StringBuilder();
            var3.append("RTP Forwarder for address ");
            var3.append(var1.getDataHostAddress());
            var3.append(" port ");
            var3.append(var1.getDataPort());
            var7.startPF(var3.toString());
         }

         this.cleaner = new SSRCCacheCleaner(this.cache, this.streamSynch);
         if (this.cache.ourssrc != null && this.participating) {
            this.cache.ourssrc.reporter = this.startParticipating(this.rtcpRawReceiver.socket);
         }

      }
   }

   public void addUnicastAddr(InetAddress var1) {
      RTCPRawSender var2 = this.sender;
      if (var2 != null) {
         var2.addDestAddr(var1);
      }

   }

   public void closeSession() {
      if (this.dslist.isEmpty() || this.nosockets) {
         this.closeSession("DataSource disconnected");
      }

   }

   public void closeSession(String var1) {
      this.stopParticipating(var1, this.cache.ourssrc);
      DataSource var3 = this.defaultsource;
      if (var3 != null) {
         var3.disconnect();
      }

      SSRCCache var10 = this.cache;
      SSRCInfo var4;
      if (var10 != null) {
         for(Enumeration var12 = var10.cache.elements(); var12.hasMoreElements(); this.stopParticipating(var1, var4)) {
            var4 = (SSRCInfo)var12.nextElement();
            if (var4.dstream != null) {
               var4.dstream.close();
            }

            if (var4 instanceof SendSSRCInfo) {
               ((SendSSRCInfo)var4).close();
            }
         }
      }

      for(int var2 = 0; var2 < this.sendstreamlist.size(); ++var2) {
         this.removeSendStream((SendStream)this.sendstreamlist.elementAt(var2));
      }

      RTPTransmitter var5 = this.rtpTransmitter;
      if (var5 != null) {
         var5.close();
      }

      PacketForwarder var6 = this.rtcpForwarder;
      if (var6 != null) {
         RTCPRawReceiver var7 = (RTCPRawReceiver)var6.getSource();
         this.rtcpForwarder.close();
         if (var7 != null) {
            var7.close();
         }
      }

      SSRCCacheCleaner var8 = this.cleaner;
      if (var8 != null) {
         var8.stop();
      }

      SSRCCache var9 = this.cache;
      if (var9 != null) {
         var9.destroy();
      }

      var6 = this.rtpForwarder;
      if (var6 != null) {
         RTPRawReceiver var11 = (RTPRawReceiver)var6.getSource();
         this.rtpForwarder.close();
         if (var11 != null) {
            var11.close();
         }
      }

      if (this.multi_unicast) {
         this.removeAllPeers();
      }

   }

   public DataSource createNewDS(int var1) {
      DataSource var2 = new DataSource();
      var2.setContentType("raw");

      try {
         var2.connect();
      } catch (IOException var7) {
         Logger var4 = logger;
         Level var5 = Level.WARNING;
         StringBuilder var6 = new StringBuilder();
         var6.append("Error connecting data source ");
         var6.append(var7.getMessage());
         var4.log(var5, var6.toString(), var7);
      }

      RTPSourceStream var3 = new RTPSourceStream(var2);
      ((BufferControlImpl)this.buffercontrol).addSourceStream(var3);
      this.dslist.put(var1, var2);
      var2.setSSRC(var1);
      var2.setMgr(this);
      return var2;
   }

   public DataSource createNewDS(RTPMediaLocator var1) {
      DataSource var2 = new DataSource();
      var2.setContentType("raw");

      try {
         var2.connect();
      } catch (IOException var7) {
         Logger var4 = logger;
         Level var5 = Level.WARNING;
         StringBuilder var6 = new StringBuilder();
         var6.append("IOException in createNewDS() ");
         var6.append(var7.getMessage());
         var4.log(var5, var6.toString(), var7);
      }

      RTPSourceStream var3 = new RTPSourceStream(var2);
      ((BufferControlImpl)this.buffercontrol).addSourceStream(var3);
      if (var1 != null && (int)var1.getSSRC() != 0) {
         this.dslist.put((int)var1.getSSRC(), var2);
         var2.setSSRC((int)var1.getSSRC());
         var2.setMgr(this);
         return var2;
      } else {
         this.defaultsource = var2;
         this.defaultstream = var3;
         return var2;
      }
   }

   public SendStream createSendStream(int var1, javax.media.protocol.DataSource var2, int var3) throws UnsupportedFormatException, IOException, SSRCInUseException {
      if (this.sendercount != 0 && this.cache.lookup(var1) != null) {
         throw new SSRCInUseException("SSRC supplied is already in use");
      } else if (this.cache.rtcp_bw_fraction == 0.0D) {
         throw new IOException("Initialized with zero RTP/RTCP outgoing bandwidth. Cannot create a sending stream ");
      } else {
         PushBufferStream var5 = ((PushBufferDataSource)var2).getStreams()[var3];
         Format var6 = var5.getFormat();
         var3 = this.formatinfo.getPayload(var6);
         if (var3 != -1) {
            SendSSRCInfo var4;
            if (this.sendercount == 0) {
               var4 = new SendSSRCInfo(this.cache.ourssrc);
               var4.ours = true;
               this.cache.ourssrc = var4;
               this.cache.getMainCache().put(var4.ssrc, var4);
            } else {
               var4 = (SendSSRCInfo)this.cache.get(var1, this.dataaddress, this.dataport, 3);
               var4.ours = true;
               if (!this.nosockets) {
                  var4.reporter = this.startParticipating(this.controlport, this.controladdress.getHostAddress(), var4);
               } else {
                  var4.reporter = this.startParticipating((RTPPushDataSource)this.rtcpsource, var4);
               }
            }

            var4.payloadType = var3;
            var4.sinkstream.setSSRCInfo(var4);
            var4.setFormat(var6);
            if (var6 instanceof VideoFormat) {
               var4.clockrate = 90000;
            } else {
               if (!(var6 instanceof AudioFormat)) {
                  throw new UnsupportedFormatException("Format not supported", var6);
               }

               var4.clockrate = (int)((AudioFormat)var6).getSampleRate();
            }

            var4.pds = var2;
            var5.setTransferHandler(var4.sinkstream);
            SessionAddress var7;
            if (this.multi_unicast) {
               if (this.peerlist.size() <= 0) {
                  throw new IOException("At least one peer must be added");
               }

               var7 = (SessionAddress)this.peerlist.firstElement();
               this.dataport = var7.getDataPort();
               this.dataaddress = var7.getDataAddress();
            }

            if (this.rtpTransmitter == null) {
               RTPConnector var8 = this.rtpConnector;
               if (var8 != null) {
                  this.rtpTransmitter = this.startDataTransmission(var8);
               } else if (this.nosockets) {
                  this.rtpTransmitter = this.startDataTransmission(this.rtpsource);
               } else {
                  if (this.newRtpInterface) {
                     this.dataport = this.remoteAddress.getDataPort();
                     this.dataaddress = this.remoteAddress.getDataAddress();
                  }

                  this.rtpTransmitter = this.startDataTransmission(this.dataport, this.dataaddress.getHostAddress());
               }

               if (this.rtpTransmitter == null) {
                  throw new IOException("Cannot create a transmitter");
               }
            }

            var4.sinkstream.setTransmitter(this.rtpTransmitter);
            this.addSendStream(var4);
            if (this.multi_unicast) {
               for(var1 = 0; var1 < this.peerlist.size(); ++var1) {
                  var7 = (SessionAddress)this.peerlist.elementAt(var1);
                  if (var4.sinkstream.transmitter.sender.peerlist == null) {
                     var4.sinkstream.transmitter.sender.peerlist = new Vector();
                  }

                  var4.sinkstream.transmitter.sender.peerlist.addElement(var7);
                  SSRCCache var10 = this.cache;
                  if (var10 != null) {
                     Enumeration var11 = var10.cache.elements();

                     while(var11.hasMoreElements()) {
                        SSRCInfo var12 = (SSRCInfo)var11.nextElement();
                        if (var12 instanceof SendSSRCInfo) {
                           var12.reporter.transmit.getSender().control = true;
                           if (var12.reporter.transmit.getSender().peerlist == null) {
                              var12.reporter.transmit.getSender().peerlist = new Vector();
                           }

                           var12.reporter.transmit.getSender().peerlist.addElement(var7);
                        }
                     }
                  }
               }
            }

            var4.sinkstream.startStream();
            NewSendStreamEvent var9 = new NewSendStreamEvent(this, var4);
            this.cache.eventhandler.postEvent(var9);
            return var4;
         } else {
            throw new UnsupportedFormatException("Format of Stream not supported in RTP Session Manager", var6);
         }
      }
   }

   public SendStream createSendStream(javax.media.protocol.DataSource var1, int var2) throws IOException, UnsupportedFormatException {
      int var3;
      if (this.sendercount == 0 && this.cache.ourssrc != null) {
         var3 = this.cache.ourssrc.ssrc;
      } else {
         do {
            var3 = (int)this.generateSSRC(GenerateSSRCCause.CREATE_SEND_STREAM);
         } while(this.cache.lookup(var3) != null);
      }

      SendStream var4 = null;

      boolean var10001;
      SendStream var9;
      try {
         var9 = this.createSendStream(var3, var1, var2);
      } catch (SSRCInUseException var8) {
         var10001 = false;
         return var4;
      }

      var4 = var9;

      label51: {
         try {
            if (!this.newRtpInterface) {
               break label51;
            }
         } catch (SSRCInUseException var7) {
            var10001 = false;
            return var4;
         }

         var4 = var9;

         try {
            this.setRemoteAddresses();
         } catch (SSRCInUseException var6) {
            var10001 = false;
            return var4;
         }
      }

      try {
         return var9;
      } catch (SSRCInUseException var5) {
         var10001 = false;
         return var4;
      }
   }

   public void dispose() {
      RTPConnector var2 = this.rtpConnector;
      if (var2 != null) {
         var2.close();
         this.rtpConnector = null;
      }

      DataSource var4 = this.defaultsource;
      if (var4 != null) {
         var4.disconnect();
      }

      SSRCCache var5 = this.cache;
      SSRCInfo var3;
      if (var5 != null) {
         for(Enumeration var6 = var5.cache.elements(); var6.hasMoreElements(); this.stopParticipating("dispose", var3)) {
            var3 = (SSRCInfo)var6.nextElement();
            if (var3.dstream != null) {
               var3.dstream.close();
            }

            if (var3 instanceof SendSSRCInfo) {
               ((SendSSRCInfo)var3).close();
            }
         }
      }

      for(int var1 = 0; var1 < this.sendstreamlist.size(); ++var1) {
         this.removeSendStream((SendStream)this.sendstreamlist.elementAt(var1));
      }

      RTPTransmitter var7 = this.rtpTransmitter;
      if (var7 != null) {
         var7.close();
      }

      RTCPTransmitter var8 = this.rtcpTransmitter;
      if (var8 != null) {
         var8.close();
      }

      PacketForwarder var9 = this.rtcpForwarder;
      if (var9 != null) {
         RTCPRawReceiver var10 = (RTCPRawReceiver)var9.getSource();
         this.rtcpForwarder.close();
         if (var10 != null) {
            var10.close();
         }
      }

      SSRCCacheCleaner var11 = this.cleaner;
      if (var11 != null) {
         var11.stop();
      }

      var5 = this.cache;
      if (var5 != null) {
         var5.destroy();
      }

      var9 = this.rtpForwarder;
      if (var9 != null) {
         RTPRawReceiver var12 = (RTPRawReceiver)var9.getSource();
         this.rtpForwarder.close();
         if (var12 != null) {
            var12.close();
         }
      }

      DatagramSocket var13 = this.dataSocket;
      if (var13 != null) {
         var13.close();
      }

      var13 = this.controlSocket;
      if (var13 != null) {
         var13.close();
      }

   }

   public String generateCNAME() {
      return SourceDescription.generateCNAME();
   }

   public long generateSSRC() {
      return (long)TrueRandom.nextInt();
   }

   protected long generateSSRC(GenerateSSRCCause var1) {
      return this.generateSSRC();
   }

   public Vector getActiveParticipants() {
      Vector var1 = new Vector();
      Enumeration var2 = this.cache.getRTPSICache().getCacheTable().elements();

      while(true) {
         Participant var3;
         do {
            if (!var2.hasMoreElements()) {
               return var1;
            }

            var3 = (Participant)var2.nextElement();
         } while(var3 != null && var3 instanceof LocalParticipant && this.nonparticipating);

         if (var3.getStreams().size() > 0) {
            var1.addElement(var3);
         }
      }
   }

   public Vector getAllParticipants() {
      Vector var1 = new Vector();
      Enumeration var2 = this.cache.getRTPSICache().getCacheTable().elements();

      while(true) {
         Participant var3;
         do {
            do {
               if (!var2.hasMoreElements()) {
                  return var1;
               }

               var3 = (Participant)var2.nextElement();
            } while(var3 == null);
         } while(var3 instanceof LocalParticipant && this.nonparticipating);

         var1.addElement(var3);
      }
   }

   public Object getControl(String var1) {
      return var1.equals("javax.media.control.BufferControl") ? this.buffercontrol : null;
   }

   public Object[] getControls() {
      return new Object[]{this.buffercontrol};
   }

   public DataSource getDataSource(RTPMediaLocator var1) {
      if (var1 == null) {
         return this.defaultsource;
      } else {
         int var2 = (int)var1.getSSRC();
         return var2 == 0 ? this.defaultsource : (DataSource)this.dslist.get(var2);
      }
   }

   public long getDefaultSSRC() {
      return this.defaultSSRC;
   }

   public Format getFormat(int var1) {
      return this.formatinfo.get(var1);
   }

   public GlobalReceptionStats getGlobalReceptionStats() {
      return this.defaultstats;
   }

   public GlobalTransmissionStats getGlobalTransmissionStats() {
      return this.transstats;
   }

   public LocalParticipant getLocalParticipant() {
      Enumeration var1 = this.cache.getRTPSICache().getCacheTable().elements();

      Participant var2;
      do {
         if (!var1.hasMoreElements()) {
            return null;
         }

         var2 = (Participant)var1.nextElement();
      } while(var2 == null || this.nonparticipating || !(var2 instanceof LocalParticipant));

      return (LocalParticipant)var2;
   }

   public SessionAddress getLocalReceiverAddress() {
      return this.localReceiverAddress;
   }

   public long getLocalSSRC() {
      SSRCCache var1 = this.cache;
      return var1 != null && var1.ourssrc != null ? (long)this.cache.ourssrc.ssrc : Long.MAX_VALUE;
   }

   public SessionAddress getLocalSessionAddress() {
      return this.newRtpInterface ? this.localAddress : new SessionAddress(this.localDataAddress, this.localDataPort, this.localControlAddress, this.localControlPort);
   }

   public int getMulticastScope() {
      return this.ttl;
   }

   public RTCPTransmitterFactory getOrCreateRTCPTransmitterFactory() {
      if (this.rtcpTransmitterFactory == null) {
         this.rtcpTransmitterFactory = new DefaultRTCPTransmitterFactory();
      }

      return this.rtcpTransmitterFactory;
   }

   public Vector getPassiveParticipants() {
      Vector var1 = new Vector();
      Enumeration var2 = this.cache.getRTPSICache().getCacheTable().elements();

      while(true) {
         Participant var3;
         do {
            if (!var2.hasMoreElements()) {
               return var1;
            }

            var3 = (Participant)var2.nextElement();
         } while(var3 != null && var3 instanceof LocalParticipant && this.nonparticipating);

         if (var3.getStreams().size() == 0) {
            var1.addElement(var3);
         }
      }
   }

   public Vector getPeers() {
      return this.peerlist;
   }

   public Vector getReceiveStreams() {
      Vector var3 = new Vector();
      Vector var4 = this.getAllParticipants();

      for(int var1 = 0; var1 < var4.size(); ++var1) {
         Vector var5 = ((Participant)var4.elementAt(var1)).getStreams();

         for(int var2 = 0; var2 < var5.size(); ++var2) {
            RTPStream var6 = (RTPStream)var5.elementAt(var2);
            if (var6 instanceof ReceiveStream) {
               var3.addElement(var6);
            }
         }
      }

      var3.trimToSize();
      return var3;
   }

   public Vector getRemoteParticipants() {
      Vector var1 = new Vector();
      Enumeration var2 = this.cache.getRTPSICache().getCacheTable().elements();

      while(var2.hasMoreElements()) {
         Participant var3 = (Participant)var2.nextElement();
         if (var3 != null && var3 instanceof RemoteParticipant) {
            var1.addElement(var3);
         }
      }

      return var1;
   }

   public SessionAddress getRemoteSessionAddress() {
      return this.remoteAddress;
   }

   public int getSSRC() {
      return 0;
   }

   public SSRCCache getSSRCCache() {
      return this.cache;
   }

   public SSRCInfo getSSRCInfo(int var1) {
      return this.cache.lookup(var1);
   }

   public Vector getSendStreams() {
      return new Vector(this.sendstreamlist);
   }

   public SessionAddress getSessionAddress() {
      return new SessionAddress(this.dataaddress, this.dataport, this.controladdress, this.controlport);
   }

   public RTPStream getStream(long var1) {
      Vector var4 = this.getAllParticipants();
      if (var4 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < var4.size(); ++var3) {
            RTPStream var5 = ((RTPSourceInfo)var4.elementAt(var3)).getSSRCStream(var1);
            if (var5 != null) {
               return var5;
            }
         }

         return null;
      }
   }

   public int initSession(SessionAddress var1, long var2, SourceDescription[] var4, double var5, double var7) throws InvalidSessionAddressException {
      if (this.initialized) {
         return -1;
      } else {
         if (var5 == 0.0D) {
            this.nonparticipating = true;
         }

         this.defaultSSRC = var2;
         this.localDataAddress = var1.getDataAddress();
         this.localControlAddress = var1.getControlAddress();
         this.localDataPort = var1.getDataPort();
         this.localControlPort = var1.getControlPort();

         InetAddress var34;
         try {
            var34 = this.localAddress.getDataAddress();
            InetAddress.getAllByName(var34.getHostName());
         } catch (Throwable var26) {
            Logger var37 = logger;
            Level var15 = Level.WARNING;
            StringBuilder var16 = new StringBuilder();
            var16.append("InitSession  RTPSessionMgr :");
            var16.append(var26.getMessage());
            var37.log(var15, var16.toString(), var26);
            return -1;
         }

         if (this.localDataAddress == null) {
            this.localDataAddress = var34;
         }

         if (this.localControlAddress == null) {
            this.localControlAddress = var34;
         }

         boolean var10 = false;
         boolean var12 = false;
         boolean var11 = false;
         boolean var9 = false;

         label234: {
            label233: {
               Exception var10000;
               label232: {
                  boolean var10001;
                  Enumeration var38;
                  try {
                     var38 = NetworkInterface.getNetworkInterfaces();
                  } catch (Exception var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label232;
                  }

                  label229:
                  while(true) {
                     var10 = var12;
                     var11 = var9;

                     try {
                        if (!var38.hasMoreElements()) {
                           break label233;
                        }
                     } catch (Exception var29) {
                        var10000 = var29;
                        var10001 = false;
                        break;
                     }

                     var10 = var12;
                     var11 = var9;

                     Enumeration var41;
                     try {
                        var41 = ((NetworkInterface)var38.nextElement()).getInetAddresses();
                     } catch (Exception var28) {
                        var10000 = var28;
                        var10001 = false;
                        break;
                     }

                     boolean var13 = var9;
                     var9 = var12;

                     while(true) {
                        var10 = var9;
                        var11 = var13;

                        boolean var14;
                        try {
                           var14 = var41.hasMoreElements();
                        } catch (Exception var27) {
                           var10000 = var27;
                           var10001 = false;
                           break label229;
                        }

                        if (!var14) {
                           var12 = var9;
                           var9 = var13;
                           break;
                        }

                        var11 = var9;

                        label223: {
                           label246: {
                              InetAddress var17;
                              try {
                                 var17 = (InetAddress)var41.nextElement();
                              } catch (Exception var32) {
                                 var10001 = false;
                                 break label246;
                              }

                              var10 = var9;
                              var11 = var9;

                              label217: {
                                 try {
                                    if (!var17.equals(this.localAddress.getDataAddress())) {
                                       break label217;
                                    }
                                 } catch (Exception var31) {
                                    var10001 = false;
                                    break label246;
                                 }

                                 var10 = true;
                              }

                              var11 = var10;

                              try {
                                 var14 = var17.equals(this.localAddress.getControlAddress());
                                 break label223;
                              } catch (Exception var30) {
                                 var10001 = false;
                              }
                           }

                           var9 = var11;
                           continue;
                        }

                        var9 = var10;
                        if (var14) {
                           var13 = true;
                           var9 = var10;
                        }
                     }
                  }
               }

               Exception var39 = var10000;
               logger.log(Level.SEVERE, "Error while enumerating interfaces", var39);
               var9 = var11;
               break label234;
            }

            var10 = var12;
         }

         StringBuilder var35;
         if (var10) {
            if (var9) {
               SSRCCache var40 = new SSRCCache(this);
               this.cache = var40;
               this.formatinfo.setCache(var40);
               this.cache.rtcp_bw_fraction = var5;
               this.cache.rtcp_sender_bw_fraction = var7;
               var40 = this.cache;
               var40.ourssrc = var40.get((int)var2, var34, 0, 2);
               this.cache.ourssrc.setAlive(true);
               if (!this.isCNAME(var4)) {
                  SourceDescription[] var36 = this.setCNAME(var4);
                  this.cache.ourssrc.setSourceDescription(var36);
               } else {
                  this.cache.ourssrc.setSourceDescription(var4);
               }

               this.cache.ourssrc.ssrc = (int)var2;
               this.cache.ourssrc.setOurs(true);
               this.initialized = true;
               return 0;
            } else {
               var35 = new StringBuilder();
               var35.append("Local Control Address");
               var35.append("Does not belong to any of this hosts local interfaces");
               throw new InvalidSessionAddressException(var35.toString());
            }
         } else {
            var35 = new StringBuilder();
            var35.append("Local Data Address ");
            var35.append("Does not belong to any of this hosts local interfaces");
            throw new InvalidSessionAddressException(var35.toString());
         }
      }
   }

   public int initSession(SessionAddress var1, SourceDescription[] var2, double var3, double var5) throws InvalidSessionAddressException {
      return this.initSession(var1, this.generateSSRC(GenerateSSRCCause.INIT_SESSION), var2, var3, var5);
   }

   public void initialize(RTPConnector var1) {
      this.rtpConnector = var1;
      this.newRtpInterface = true;
      String var9 = SourceDescription.generateCNAME();
      SourceDescription[] var8 = new SourceDescription[]{new SourceDescription(3, "fmj-devel@lists.sourceforge.net", 1, false), new SourceDescription(1, var9, 1, false), new SourceDescription(6, "FMJ RTP Player", 1, false)};
      int var6 = (int)this.generateSSRC(GenerateSSRCCause.INITIALIZE);
      this.ttl = 1;
      double var4 = this.rtpConnector.getRTCPBandwidthFraction();
      boolean var7;
      if (var4 != 0.0D) {
         var7 = true;
      } else {
         var7 = false;
      }

      this.participating = var7;
      SSRCCache var10 = new SSRCCache(this);
      this.cache = var10;
      var10.sessionbandwidth = 384000;
      this.formatinfo.setCache(this.cache);
      double var2 = var4;
      if (var4 <= 0.0D) {
         var2 = 0.05D;
      }

      this.cache.rtcp_bw_fraction = var2;
      var4 = this.rtpConnector.getRTCPSenderBandwidthFraction();
      var2 = var4;
      if (var4 <= 0.0D) {
         var2 = 0.25D;
      }

      this.cache.rtcp_sender_bw_fraction = var2;
      var10 = this.cache;
      var10.ourssrc = var10.get(var6, (InetAddress)null, 0, 2);
      this.cache.ourssrc.setAlive(true);
      SourceDescription[] var11 = var8;
      if (!this.isCNAME(var8)) {
         var11 = this.setCNAME(var8);
      }

      this.cache.ourssrc.setSourceDescription(var11);
      this.cache.ourssrc.ssrc = var6;
      this.cache.ourssrc.setOurs(true);
      this.initialized = true;
      this.rtpRawReceiver = new RTPRawReceiver(this.rtpConnector, this.defaultstats);
      this.rtcpRawReceiver = new RTCPRawReceiver(this.rtpConnector, this.defaultstats, this.streamSynch);
      RTPDemultiplexer var12 = new RTPDemultiplexer(this.cache, this.rtpRawReceiver, this.streamSynch);
      this.rtpDemultiplexer = var12;
      PacketForwarder var13 = new PacketForwarder(this.rtpRawReceiver, new RTPReceiver(this.cache, var12));
      this.rtpForwarder = var13;
      StringBuilder var14;
      if (var13 != null) {
         var14 = new StringBuilder();
         var14.append("RTP Forwarder: ");
         var14.append(this.rtpConnector);
         var13.startPF(var14.toString());
      }

      var13 = new PacketForwarder(this.rtcpRawReceiver, new RTCPReceiver(this.cache));
      this.rtcpForwarder = var13;
      if (var13 != null) {
         var14 = new StringBuilder();
         var14.append("RTCP Forwarder: ");
         var14.append(this.rtpConnector);
         var13.startPF(var14.toString());
      }

      this.cleaner = new SSRCCacheCleaner(this.cache, this.streamSynch);
      if (this.participating && this.cache.ourssrc != null) {
         this.cache.ourssrc.reporter = this.startParticipating(this.rtpConnector, this.cache.ourssrc);
      }

   }

   public void initialize(SessionAddress var1) throws InvalidSessionAddressException {
      String var3 = SourceDescription.generateCNAME();
      SourceDescription var2 = new SourceDescription(3, "fmj-devel@lists.sourceforge.net", 1, false);
      SourceDescription var5 = new SourceDescription(1, var3, 1, false);
      SourceDescription var4 = new SourceDescription(6, "FMJ RTP Player", 1, false);
      this.initialize(new SessionAddress[]{var1}, new SourceDescription[]{var2, var5, var4}, 0.05D, 0.25D, (EncryptionInfo)null);
   }

   public void initialize(SessionAddress[] var1, SourceDescription[] var2, double var3, double var5, EncryptionInfo var7) throws InvalidSessionAddressException {
      if (!this.initialized) {
         this.newRtpInterface = true;
         this.remoteAddresses = new Vector();
         int var13 = (int)this.generateSSRC(GenerateSSRCCause.INITIALIZE);
         this.ttl = 1;
         boolean var14;
         if (var3 != 0.0D) {
            var14 = true;
         } else {
            var14 = false;
         }

         this.participating = var14;
         if (var1.length != 0) {
            SessionAddress var131 = var1[0];
            this.localAddress = var131;
            if (var131 == null) {
               throw new InvalidSessionAddressException("Invalid local address: null");
            } else {
               InetAddress var142;
               StringBuilder var15;
               boolean var10001;
               label1289: {
                  Throwable var10000;
                  label1288: {
                     String var132;
                     label1287: {
                        label1286: {
                           try {
                              var142 = var131.getDataAddress();
                              if (!var142.getHostAddress().equals("0.0.0.0")) {
                                 break label1286;
                              }
                           } catch (Throwable var130) {
                              var10000 = var130;
                              var10001 = false;
                              break label1288;
                           }

                           var132 = "0.0.0.0";
                           break label1287;
                        }

                        try {
                           var132 = var142.getHostName();
                        } catch (Throwable var129) {
                           var10000 = var129;
                           var10001 = false;
                           break label1288;
                        }
                     }

                     label1279:
                     try {
                        InetAddress.getAllByName(var132);
                        break label1289;
                     } catch (Throwable var128) {
                        var10000 = var128;
                        var10001 = false;
                        break label1279;
                     }
                  }

                  Throwable var133 = var10000;
                  Logger var134 = logger;
                  Level var143 = Level.WARNING;
                  var15 = new StringBuilder();
                  var15.append("Error during initialization: ");
                  var15.append(var133.getMessage());
                  var134.log(var143, var15.toString(), var133);
                  return;
               }

               if (this.localAddress.getDataAddress() == null) {
                  this.localAddress.setDataHostAddress(var142);
               }

               if (this.localAddress.getControlAddress() == null) {
                  this.localAddress.setControlHostAddress(var142);
               }

               int var144;
               if (this.localAddress.getDataAddress().isMulticastAddress()) {
                  if (!this.localAddress.getControlAddress().isMulticastAddress()) {
                     throw new InvalidSessionAddressException("Invalid multicast address");
                  }

                  this.ttl = this.localAddress.getTimeToLive();
               } else {
                  boolean var10 = true;
                  boolean var12 = true;
                  boolean var8 = var10;
                  boolean var9 = var12;

                  label1269: {
                     boolean var11;
                     label1268: {
                        Exception var146;
                        label1300: {
                           Logger var135;
                           try {
                              var135 = logger;
                           } catch (Exception var127) {
                              var146 = var127;
                              var10001 = false;
                              break label1300;
                           }

                           var8 = var10;
                           var9 = var12;

                           try {
                              var15 = new StringBuilder();
                           } catch (Exception var126) {
                              var146 = var126;
                              var10001 = false;
                              break label1300;
                           }

                           var8 = var10;
                           var9 = var12;

                           try {
                              var15.append("Looking for local data address: ");
                           } catch (Exception var125) {
                              var146 = var125;
                              var10001 = false;
                              break label1300;
                           }

                           var8 = var10;
                           var9 = var12;

                           try {
                              var15.append(this.localAddress.getDataAddress());
                           } catch (Exception var124) {
                              var146 = var124;
                              var10001 = false;
                              break label1300;
                           }

                           var8 = var10;
                           var9 = var12;

                           try {
                              var15.append(" and control address");
                           } catch (Exception var123) {
                              var146 = var123;
                              var10001 = false;
                              break label1300;
                           }

                           var8 = var10;
                           var9 = var12;

                           try {
                              var15.append(this.localAddress.getControlAddress());
                           } catch (Exception var122) {
                              var146 = var122;
                              var10001 = false;
                              break label1300;
                           }

                           var8 = var10;
                           var9 = var12;

                           try {
                              var135.fine(var15.toString());
                           } catch (Exception var121) {
                              var146 = var121;
                              var10001 = false;
                              break label1300;
                           }

                           var8 = var10;
                           var9 = var12;

                           try {
                              var14 = this.localAddress.getDataHostAddress().equals("0.0.0.0");
                           } catch (Exception var120) {
                              var146 = var120;
                              var10001 = false;
                              break label1300;
                           }

                           if (!var14) {
                              var8 = var10;
                              var9 = var12;

                              try {
                                 if (this.localAddress.getDataHostAddress().equals("::0")) {
                                 }
                              } catch (Exception var119) {
                                 var146 = var119;
                                 var10001 = false;
                                 break label1300;
                              }
                           }

                           var10 = true;
                           var11 = true;
                           var8 = var10;
                           var9 = var12;

                           label1301: {
                              try {
                                 if (this.localAddress.getControlHostAddress().equals("0.0.0.0")) {
                                    break label1301;
                                 }
                              } catch (Exception var118) {
                                 var146 = var118;
                                 var10001 = false;
                                 break label1300;
                              }

                              var8 = var10;
                              var9 = var12;

                              try {
                                 if (this.localAddress.getControlHostAddress().equals("::0")) {
                                 }
                              } catch (Exception var117) {
                                 var146 = var117;
                                 var10001 = false;
                                 break label1300;
                              }
                           }

                           var9 = true;
                           var12 = true;
                           var8 = var10;

                           Enumeration var136;
                           try {
                              var136 = NetworkInterface.getNetworkInterfaces();
                           } catch (Exception var116) {
                              var146 = var116;
                              var10001 = false;
                              break label1300;
                           }

                           var10 = var12;

                           label1217:
                           while(true) {
                              var8 = var11;
                              var9 = var10;

                              try {
                                 if (!var136.hasMoreElements()) {
                                    break label1268;
                                 }
                              } catch (Exception var107) {
                                 var146 = var107;
                                 var10001 = false;
                                 break;
                              }

                              if (var11 && var10) {
                                 break label1268;
                              }

                              var8 = var11;
                              var9 = var10;

                              Enumeration var145;
                              try {
                                 var145 = ((NetworkInterface)var136.nextElement()).getInetAddresses();
                              } catch (Exception var106) {
                                 var146 = var106;
                                 var10001 = false;
                                 break;
                              }

                              var12 = var10;
                              var10 = var11;

                              while(true) {
                                 var8 = var10;
                                 var9 = var12;

                                 try {
                                    var14 = var145.hasMoreElements();
                                 } catch (Exception var105) {
                                    var146 = var105;
                                    var10001 = false;
                                    break label1217;
                                 }

                                 if (!var14) {
                                    var11 = var10;
                                    var10 = var12;
                                    break;
                                 }

                                 var9 = var10;

                                 label1309: {
                                    InetAddress var16;
                                    try {
                                       var16 = (InetAddress)var145.nextElement();
                                    } catch (Exception var115) {
                                       var10001 = false;
                                       break label1309;
                                    }

                                    var9 = var10;

                                    Logger var17;
                                    try {
                                       var17 = logger;
                                    } catch (Exception var114) {
                                       var10001 = false;
                                       break label1309;
                                    }

                                    var9 = var10;

                                    StringBuilder var18;
                                    try {
                                       var18 = new StringBuilder();
                                    } catch (Exception var113) {
                                       var10001 = false;
                                       break label1309;
                                    }

                                    var9 = var10;

                                    try {
                                       var18.append("Testing iface address ");
                                    } catch (Exception var112) {
                                       var10001 = false;
                                       break label1309;
                                    }

                                    var9 = var10;

                                    try {
                                       var18.append(this.localAddress.getDataAddress());
                                    } catch (Exception var111) {
                                       var10001 = false;
                                       break label1309;
                                    }

                                    var9 = var10;

                                    try {
                                       var17.fine(var18.toString());
                                    } catch (Exception var110) {
                                       var10001 = false;
                                       break label1309;
                                    }

                                    var8 = var10;
                                    var9 = var10;

                                    label1184: {
                                       try {
                                          if (!var16.equals(this.localAddress.getDataAddress())) {
                                             break label1184;
                                          }
                                       } catch (Exception var109) {
                                          var10001 = false;
                                          break label1309;
                                       }

                                       var8 = true;
                                    }

                                    var9 = var8;

                                    try {
                                       var14 = var16.equals(this.localAddress.getControlAddress());
                                    } catch (Exception var108) {
                                       var10001 = false;
                                       break label1309;
                                    }

                                    var9 = var8;
                                    if (var14) {
                                       var12 = true;
                                       var9 = var8;
                                    }
                                 }

                                 var10 = var9;
                              }
                           }
                        }

                        Exception var137 = var146;
                        logger.log(Level.WARNING, "Error while enumerating local interfaces.", var137);
                        break label1269;
                     }

                     var8 = var11;
                     var9 = var10;
                  }

                  StringBuilder var138;
                  if (!var8) {
                     var138 = new StringBuilder();
                     var138.append("Local Data Address ");
                     var138.append("Does not belong to any of this hosts local interfaces");
                     throw new InvalidSessionAddressException(var138.toString());
                  }

                  if (!var9) {
                     var138 = new StringBuilder();
                     var138.append("Local Control Address ");
                     var138.append("Does not belong to any of this hosts local interfaces");
                     throw new InvalidSessionAddressException(var138.toString());
                  }

                  if (this.localAddress.getDataPort() == -1) {
                     var144 = this.findLocalPorts();
                     this.localAddress.setDataPort(var144);
                     this.localAddress.setControlPort(var144 + 1);
                  }

                  if (!this.localAddress.getDataAddress().isMulticastAddress()) {
                     try {
                        this.dataSocket = new DatagramSocket(this.localAddress.getDataPort(), this.localAddress.getDataAddress());
                     } catch (SocketException var103) {
                        var138 = new StringBuilder();
                        var138.append("Can't open local data port: ");
                        var138.append(this.localAddress.getDataPort());
                        throw new InvalidSessionAddressException(var138.toString());
                     }
                  }

                  if (!this.localAddress.getControlAddress().isMulticastAddress()) {
                     try {
                        this.controlSocket = new DatagramSocket(this.localAddress.getControlPort(), this.localAddress.getControlAddress());
                     } catch (SocketException var104) {
                        DatagramSocket var139 = this.dataSocket;
                        if (var139 != null) {
                           var139.close();
                        }

                        var138 = new StringBuilder();
                        var138.append("Can't open local control port: ");
                        var138.append(this.localAddress.getControlPort());
                        throw new InvalidSessionAddressException(var138.toString());
                     }
                  }
               }

               SSRCCache var140 = new SSRCCache(this);
               this.cache = var140;
               var144 = this.ttl;
               if (var144 <= 16) {
                  var140.sessionbandwidth = 384000;
               } else if (var144 <= 64) {
                  var140.sessionbandwidth = 128000;
               } else if (var144 <= 128) {
                  var140.sessionbandwidth = 16000;
               } else if (var144 <= 192) {
                  var140.sessionbandwidth = 6625;
               } else {
                  var140.sessionbandwidth = 4000;
               }

               this.formatinfo.setCache(this.cache);
               this.cache.rtcp_bw_fraction = var3;
               this.cache.rtcp_sender_bw_fraction = var5;
               var140 = this.cache;
               var140.ourssrc = var140.get(var13, var142, 0, 2);
               this.cache.ourssrc.setAlive(true);
               if (!this.isCNAME(var2)) {
                  SourceDescription[] var141 = this.setCNAME(var2);
                  this.cache.ourssrc.setSourceDescription(var141);
               } else {
                  this.cache.ourssrc.setSourceDescription(var2);
               }

               this.cache.ourssrc.ssrc = var13;
               this.cache.ourssrc.setOurs(true);
               this.initialized = true;
            }
         } else {
            throw new InvalidSessionAddressException("At least one local address is required!");
         }
      }
   }

   boolean isBroadcast(InetAddress param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isDefaultDSassigned() {
      return this.bds;
   }

   public boolean isSenderDefaultAddr(InetAddress var1) {
      RTCPRawSender var2 = this.sender;
      return var2 == null ? false : var2.getRemoteAddr().equals(var1);
   }

   boolean isUnicast() {
      return this.unicast;
   }

   public void removeAllPeers() {
      for(int var1 = 0; var1 < this.peerlist.size(); ++var1) {
         this.removePeer((SessionAddress)this.peerlist.elementAt(var1));
      }

   }

   public void removeDataSource(DataSource var1) {
      if (var1 == this.defaultsource) {
         this.defaultsource = null;
         this.defaultstream = null;
         this.defaultsourceid = 0;
         this.bds = false;
      }

      this.dslist.removeObj(var1);
      if (var1 != null) {
         try {
            var1.disconnect();
         } catch (Throwable var3) {
            if (var3 instanceof InterruptedException) {
               Thread.currentThread().interrupt();
               return;
            } else if (!(var3 instanceof ThreadDeath)) {
               return;
            } else {
               throw (ThreadDeath)var3;
            }
         }
      }
   }

   public void removePeer(SessionAddress var1) {
      PacketForwarder var3 = (PacketForwarder)this.peerrtplist.get(var1);
      PacketForwarder var4 = (PacketForwarder)this.peerrtplist.get(var1);
      if (var3 != null) {
         var3.close();
      }

      if (var4 != null) {
         var4.close();
      }

      for(int var2 = 0; var2 < this.peerlist.size(); ++var2) {
         if (((SessionAddress)this.peerlist.elementAt(var2)).equals(var1)) {
            this.peerlist.removeElementAt(var2);
         }
      }

   }

   public void removeReceiveStreamListener(ReceiveStreamListener var1) {
      this.streamlistener.removeElement(var1);
   }

   public void removeRemoteListener(RemoteListener var1) {
      this.remotelistener.removeElement(var1);
   }

   void removeSendStream(SendStream var1) {
      this.sendstreamlist.removeElement(var1);
      SendSSRCInfo var6 = (SendSSRCInfo)var1;
      if (var6.sinkstream != null) {
         var6.sinkstream.close();
         StreamClosedEvent var7 = new StreamClosedEvent(this, var1);
         this.cache.eventhandler.postEvent(var7);
         this.stopParticipating("Closed Stream", var6);
      }

      if (this.sendstreamlist.size() == 0 && this.cache.ourssrc != null) {
         Object var8;
         if (this.cache.ourssrc.ssrc == var6.ssrc && var6.reporter == null) {
            int var2 = 0;

            long var4;
            while(true) {
               var4 = this.generateSSRC(GenerateSSRCCause.REMOVE_SEND_STREAM);
               if (var4 == Long.MAX_VALUE) {
                  break;
               }

               int var3 = (int)var4;
               var2 = var3;
               if (this.cache.lookup(var3) == null) {
                  var2 = var3;
                  break;
               }
            }

            if (var4 == Long.MAX_VALUE) {
               var8 = new PassiveSSRCInfo(this.cache.ourssrc);
            } else {
               SSRCInfo var10 = this.cache.get(var2, (InetAddress)null, 0, 2);
               var10.setAlive(true);
               SourceDescription[] var9 = new SourceDescription[]{new SourceDescription(3, "fmj-devel@lists.sourceforge.net", 1, false), new SourceDescription(1, this.generateCNAME(), 1, false), new SourceDescription(6, "FMJ RTP Player", 1, false)};
               if (!this.isCNAME(var9)) {
                  var9 = this.setCNAME(var9);
               }

               var10.setSourceDescription(var9);
               var10.ssrc = var2;
               var8 = var10;
            }
         } else {
            var8 = new PassiveSSRCInfo(this.cache.ourssrc);
         }

         ((SSRCInfo)var8).setOurs(true);
         this.cache.ourssrc = (SSRCInfo)var8;
         this.cache.getMainCache().put(((SSRCInfo)var8).ssrc, var8);
         if (this.rtpConnector != null) {
            this.cache.ourssrc.reporter = this.startParticipating(this.rtpConnector, this.cache.ourssrc);
         }
      }

   }

   public void removeSendStreamListener(SendStreamListener var1) {
   }

   public void removeSessionListener(SessionListener var1) {
      this.sessionlistener.removeElement(var1);
   }

   public void removeTarget(SessionAddress var1, String var2) {
      this.remoteAddresses.removeElement(var1);
      this.setRemoteAddresses();
      if (this.remoteAddresses.size() == 0) {
         SSRCCache var3 = this.cache;
         if (var3 != null) {
            this.stopParticipating(var2, var3.ourssrc);
         }
      }

   }

   public void removeTargets(String var1) {
      SSRCCache var2 = this.cache;
      if (var2 != null) {
         this.stopParticipating(var1, var2.ourssrc);
      }

      Vector var3 = this.remoteAddresses;
      if (var3 != null) {
         var3.removeAllElements();
      }

      this.setRemoteAddresses();
   }

   public void setDefaultDSassigned(int var1) {
      this.bds = true;
      this.defaultsourceid = var1;
      this.dslist.put(var1, this.defaultsource);
      this.defaultsource.setSSRC(var1);
      this.defaultsource.setMgr(this);
   }

   public void setMulticastScope(int var1) {
      int var2 = var1;
      if (var1 < 1) {
         var2 = 1;
      }

      this.ttl = var2;
      if (var2 <= 16) {
         this.cache.sessionbandwidth = 384000;
      } else if (var2 <= 64) {
         this.cache.sessionbandwidth = 128000;
      } else if (var2 <= 128) {
         this.cache.sessionbandwidth = 16000;
      } else if (var2 <= 192) {
         this.cache.sessionbandwidth = 6625;
      } else {
         this.cache.sessionbandwidth = 4000;
      }

      UDPPacketSender var3 = this.udpsender;
      if (var3 != null) {
         try {
            var3.setttl(this.ttl);
            return;
         } catch (IOException var4) {
            logger.log(Level.WARNING, "setMulticastScope Exception ", var4);
         }
      }

   }

   public void setRTCPTransmitterFactory(RTCPTransmitterFactory var1) {
      this.rtcpTransmitterFactory = var1;
   }

   void setSessionBandwidth(int var1) {
      this.cache.sessionbandwidth = var1;
   }

   void startRTCPReports(InetAddress var1) {
      if (!this.nonparticipating && !this.startedparticipating) {
         try {
            if (this.cache.ourssrc != null) {
               this.cache.ourssrc.reporter = this.startParticipating(this.controlport, var1.getHostAddress(), this.cache.ourssrc);
            }

            return;
         } catch (IOException var5) {
            Logger var2 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
            var4.append("start rtcp reports  ");
            var4.append(var5.getMessage());
            var2.log(var3, var4.toString(), var5);
         }
      }

   }

   public int startSession(int var1, EncryptionInfo var2) throws IOException {
      this.multi_unicast = true;
      int var3 = var1;
      if (var1 < 1) {
         var3 = 1;
      }

      this.ttl = var3;
      if (var3 <= 16) {
         this.cache.sessionbandwidth = 384000;
      } else if (var3 <= 64) {
         this.cache.sessionbandwidth = 128000;
      } else if (var3 <= 128) {
         this.cache.sessionbandwidth = 16000;
      } else if (var3 <= 192) {
         this.cache.sessionbandwidth = 6625;
      } else {
         this.cache.sessionbandwidth = 4000;
      }

      this.cleaner = new SSRCCacheCleaner(this.cache, this.streamSynch);
      return 0;
   }

   public int startSession(SessionAddress param1, int param2, EncryptionInfo param3) throws IOException, InvalidSessionAddressException {
      // $FF: Couldn't be decompiled
   }

   public int startSession(SessionAddress param1, SessionAddress param2, SessionAddress param3, EncryptionInfo param4) throws IOException, InvalidSessionAddressException {
      // $FF: Couldn't be decompiled
   }

   public void startSession() throws IOException {
      SessionAddress var1 = new SessionAddress(this.dataaddress, this.dataport, this.controladdress, this.controlport);

      try {
         this.startSession(var1, this.ttl, (EncryptionInfo)null);
      } catch (SessionManagerException var3) {
         StringBuilder var2 = new StringBuilder();
         var2.append("SessionManager exception ");
         var2.append(var3.getMessage());
         throw new IOException(var2.toString());
      }
   }

   public String toString() {
      if (this.newRtpInterface) {
         int var1 = 0;
         int var2 = 0;
         String var5 = "";
         SessionAddress var4 = this.localAddress;
         if (var4 != null) {
            var1 = var4.getControlPort();
            var2 = this.localAddress.getDataPort();
            var5 = this.localAddress.getDataHostAddress();
         }

         StringBuilder var6 = new StringBuilder();
         var6.append("RTPManager \n\tSSRCCache  ");
         var6.append(this.cache);
         var6.append("\n\tDataport  ");
         var6.append(var2);
         var6.append("\n\tControlport  ");
         var6.append(var1);
         var6.append("\n\tAddress  ");
         var6.append(var5);
         var6.append("\n\tRTPForwarder  ");
         var6.append(this.rtpForwarder);
         var6.append("\n\tRTPDemux  ");
         var6.append(this.rtpDemultiplexer);
         return var6.toString();
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("RTPSession Manager  \n\tSSRCCache  ");
         var3.append(this.cache);
         var3.append("\n\tDataport  ");
         var3.append(this.dataport);
         var3.append("\n\tControlport  ");
         var3.append(this.controlport);
         var3.append("\n\tAddress  ");
         var3.append(this.dataaddress);
         var3.append("\n\tRTPForwarder  ");
         var3.append(this.rtpForwarder);
         var3.append("\n\tRTPDEmux  ");
         var3.append(this.rtpDemultiplexer);
         return var3.toString();
      }
   }
}
