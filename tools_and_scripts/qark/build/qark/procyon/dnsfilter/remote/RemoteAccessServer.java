// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.remote;

import java.util.Properties;
import java.io.ObjectOutputStream;
import util.TimoutNotificator;
import util.GroupedLogger;
import util.AsyncLogger;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import util.TimeoutListener;
import java.util.Collection;
import java.io.OutputStream;
import java.net.Socket;
import dnsfilter.ConfigurationAccess;
import java.io.EOFException;
import util.Utils;
import java.io.InputStream;
import java.io.IOException;
import util.LoggerInterface;
import util.Logger;
import util.Encryption;
import java.util.HashMap;
import java.net.ServerSocket;

public class RemoteAccessServer implements Runnable
{
    private static int sessionId;
    private ServerSocket server;
    private HashMap sessions;
    boolean stopped;
    
    static {
        RemoteAccessServer.sessionId = 0;
    }
    
    public RemoteAccessServer(final int n, final String s) throws IOException {
        this.stopped = false;
        this.sessions = new HashMap();
        Encryption.init_AES(s);
        this.server = new ServerSocket(n);
        new Thread(this).start();
        final LoggerInterface logger = Logger.getLogger();
        final StringBuilder sb = new StringBuilder();
        sb.append("Started RemoteAccess Server on port ");
        sb.append(n);
        logger.logLine(sb.toString());
    }
    
    private String readStringFromStream(final InputStream inputStream, final byte[] array) throws IOException {
        final int lineBytesFromStream = Utils.readLineBytesFromStream(inputStream, array, true, false);
        if (lineBytesFromStream == -1) {
            throw new EOFException("Stream is closed!");
        }
        return new String(array, 0, lineBytesFromStream).trim();
    }
    
    @Override
    public void run() {
    Label_0513_Outer:
        while (!this.stopped) {
            try {
                final Socket accept = this.server.accept();
                final InputStream decryptedStream = Encryption.getDecryptedStream(accept.getInputStream());
                final OutputStream encryptedOutputStream = Encryption.getEncryptedOutputStream(accept.getOutputStream(), 1024);
                try {
                    final byte[] array = new byte[1024];
                    this.readStringFromStream(decryptedStream, array);
                    final String stringFromStream = this.readStringFromStream(decryptedStream, array);
                    if (stringFromStream.equals("new_session")) {
                        ++RemoteAccessServer.sessionId;
                        encryptedOutputStream.write("OK\n".getBytes());
                        final StringBuilder sb = new StringBuilder();
                        sb.append(RemoteAccessServer.sessionId);
                        sb.append("\n");
                        encryptedOutputStream.write(sb.toString().getBytes());
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(ConfigurationAccess.getLocal().getVersion());
                        sb2.append("\n");
                        encryptedOutputStream.write(sb2.toString().getBytes());
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(ConfigurationAccess.getLocal().getLastDNSAddress());
                        sb3.append("\n");
                        encryptedOutputStream.write(sb3.toString().getBytes());
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(ConfigurationAccess.getLocal().openConnectionsCount());
                        sb4.append("\n");
                        encryptedOutputStream.write(sb4.toString().getBytes());
                        encryptedOutputStream.flush();
                        new RemoteSession(accept, decryptedStream, encryptedOutputStream, RemoteAccessServer.sessionId);
                        continue Label_0513_Outer;
                    }
                    if (stringFromStream.equals("reconnect_session")) {
                        try {
                            final int int1 = Integer.parseInt(this.readStringFromStream(decryptedStream, array));
                            final RemoteSession remoteSession = this.sessions.get(int1);
                            if (remoteSession == null) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("Reconnect session not found:");
                                sb5.append(int1);
                                throw new IOException(sb5.toString());
                            }
                            remoteSession.reconnectSession(accept, decryptedStream, encryptedOutputStream);
                            encryptedOutputStream.write("OK\n".getBytes());
                            encryptedOutputStream.flush();
                            continue Label_0513_Outer;
                        }
                        catch (Exception ex) {
                            throw new IOException(ex);
                        }
                    }
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("Invalid option: ");
                    sb6.append(stringFromStream);
                    throw new IOException(sb6.toString());
                }
                catch (IOException ex2) {
                    encryptedOutputStream.write(ex2.toString().getBytes());
                    encryptedOutputStream.flush();
                    Utils.closeSocket(accept);
                    throw ex2;
                }
            }
            catch (IOException ex3) {
                final LoggerInterface logger = Logger.getLogger();
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("RemoteServerException: ");
                sb7.append(ex3.toString());
                logger.logLine(sb7.toString());
            }
            while (true) {
                continue Label_0513_Outer;
                continue;
            }
        }
    }
    
    public void stop() {
        this.stopped = true;
        final Collection values = this.sessions.values();
        int i = 0;
        for (RemoteSession[] array = (RemoteSession[])values.toArray(new RemoteSession[0]); i < array.length; ++i) {
            array[i].killSession();
        }
        try {
            this.server.close();
        }
        catch (IOException ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    private class RemoteSession implements Runnable, TimeoutListener
    {
        int connectedSessionId;
        boolean doReconnect;
        int id;
        DataInputStream in;
        boolean killed;
        long lastHeartBeatConfirm;
        DataOutputStream out;
        LoggerInterface remoteLogger;
        Socket socket;
        long timeout;
        
        private RemoteSession(final Socket socket, final InputStream inputStream, final OutputStream outputStream, final int id) throws IOException {
            this.connectedSessionId = -1;
            this.killed = false;
            this.doReconnect = false;
            this.timeout = Long.MAX_VALUE;
            this.lastHeartBeatConfirm = System.currentTimeMillis();
            this.id = id;
            this.socket = socket;
            this.out = new DataOutputStream(outputStream);
            this.in = new DataInputStream(inputStream);
            RemoteAccessServer.this.sessions.put(id, this);
            final LoggerInterface logger = Logger.getLogger();
            final StringBuilder sb = new StringBuilder();
            sb.append("New Remote Session ");
            sb.append(id);
            sb.append(" from :");
            sb.append(socket);
            logger.logLine(sb.toString());
            new Thread(this).start();
        }
        
        private void attachStream() throws IOException {
            try {
                this.connectedSessionId = Integer.parseInt(Utils.readLineFromStream(this.in));
                this.remoteLogger = new AsyncLogger(new LoggerInterface() {
                    @Override
                    public void closeLogger() {
                    }
                    
                    @Override
                    public void log(final String s) {
                        this.sendLog(1, s);
                    }
                    
                    @Override
                    public void logException(final Exception ex) {
                        final StringWriter stringWriter = new StringWriter();
                        ex.printStackTrace(new PrintWriter(stringWriter));
                        final StringBuilder sb = new StringBuilder();
                        sb.append(stringWriter.toString());
                        sb.append("\n");
                        this.log(sb.toString());
                    }
                    
                    @Override
                    public void logLine(final String s) {
                        this.sendLog(2, s);
                    }
                    
                    @Override
                    public void message(final String s) {
                        this.sendLog(3, s);
                    }
                    
                    public void sendLog(final int n, final String s) {
                        final DataOutputStream out = RemoteSession.this.out;
                        // monitorenter(out)
                        try {
                            try {
                                RemoteSession.this.out.writeShort(5);
                                final StringBuilder sb = new StringBuilder();
                                sb.append(ConfigurationAccess.getLocal().openConnectionsCount());
                                sb.append("");
                                final byte[] bytes = sb.toString().getBytes();
                                RemoteSession.this.out.writeShort(bytes.length);
                                RemoteSession.this.out.write(bytes);
                                RemoteSession.this.out.writeShort(4);
                                final byte[] bytes2 = ConfigurationAccess.getLocal().getLastDNSAddress().getBytes();
                                RemoteSession.this.out.writeShort(bytes2.length);
                                RemoteSession.this.out.write(bytes2);
                                final byte[] bytes3 = s.getBytes();
                                RemoteSession.this.out.writeShort(n);
                                RemoteSession.this.out.writeShort(bytes3.length);
                                RemoteSession.this.out.write(bytes3);
                                RemoteSession.this.out.flush();
                            }
                            finally {
                            }
                            // monitorexit(out)
                            // monitorexit(out)
                        }
                        catch (IOException ex) {}
                    }
                });
                final DataOutputStream out = this.out;
                // monitorenter(out)
                try {
                    try {
                        ((GroupedLogger)Logger.getLogger()).attachLogger(this.remoteLogger);
                    }
                    finally {
                        // monitorexit(out)
                        this.out.write("OK\n".getBytes());
                        this.doHeartBeat(RemoteAccessClient.READ_TIMEOUT);
                    }
                    // monitorexit(out)
                }
                catch (ClassCastException ex2) {}
            }
            catch (Exception ex) {
                throw new IOException(ex);
            }
        }
        
        private boolean checkLastConfirmedHeartBeat() {
            if (System.currentTimeMillis() - this.lastHeartBeatConfirm > 2 * RemoteAccessClient.READ_TIMEOUT) {
                Logger.getLogger().logLine("Heartbeat Confirmation not received - Dead Session!");
                this.killSession();
                return false;
            }
            return true;
        }
        
        private void doHeartBeat(final int n) {
            try {
                synchronized (this.out) {
                    this.out.writeShort(6);
                    this.out.writeShort(0);
                    this.out.flush();
                    // monitorexit(this.out)
                    this.timeout = System.currentTimeMillis() + n;
                    TimoutNotificator.getInstance().register(this);
                }
            }
            catch (IOException ex) {
                final LoggerInterface logger = Logger.getLogger();
                final StringBuilder sb = new StringBuilder();
                sb.append("Heartbeat failed! ");
                sb.append(ex);
                logger.logLine(sb.toString());
                this.killSession();
            }
        }
        
        private void executeAction(String replace) throws IOException {
            try {
                if (replace.equals("getConfig()")) {
                    final Properties config = ConfigurationAccess.getLocal().getConfig();
                    this.out.write("OK\n".getBytes());
                    final ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.out);
                    objectOutputStream.writeObject(config);
                    objectOutputStream.flush();
                }
                else if (replace.equals("readConfig()")) {
                    final byte[] config2 = ConfigurationAccess.getLocal().readConfig();
                    this.out.write("OK\n".getBytes());
                    this.out.writeInt(config2.length);
                    this.out.write(config2);
                    this.out.flush();
                }
                else if (replace.equals("updateConfig()")) {
                    final byte[] array = new byte[this.in.readInt()];
                    this.in.readFully(array);
                    ConfigurationAccess.getLocal().updateConfig(array);
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                }
                else if (replace.equals("getAdditionalHosts()")) {
                    final byte[] additionalHosts = ConfigurationAccess.getLocal().getAdditionalHosts(this.in.readInt());
                    this.out.write("OK\n".getBytes());
                    this.out.writeInt(additionalHosts.length);
                    this.out.write(additionalHosts);
                    this.out.flush();
                }
                else if (replace.equals("updateAdditionalHosts()")) {
                    final byte[] array2 = new byte[this.in.readInt()];
                    this.in.readFully(array2);
                    ConfigurationAccess.getLocal().updateAdditionalHosts(array2);
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                }
                else if (replace.equals("updateFilter()")) {
                    replace = Utils.readLineFromStream(this.in).replace(";", "\n");
                    ConfigurationAccess.getLocal().updateFilter(replace, Boolean.parseBoolean(Utils.readLineFromStream(this.in)));
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                }
                else if (replace.equals("restart()")) {
                    ConfigurationAccess.getLocal().restart();
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                }
                else if (replace.equals("stop()")) {
                    ConfigurationAccess.getLocal().stop();
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                }
                else {
                    final boolean equals = replace.equals("getFilterStatistics()");
                    int i = 0;
                    if (equals) {
                        final long[] filterStatistics = ConfigurationAccess.getLocal().getFilterStatistics();
                        this.out.write("OK\n".getBytes());
                        this.out.writeLong(filterStatistics[0]);
                        this.out.writeLong(filterStatistics[1]);
                        this.out.flush();
                    }
                    else if (replace.equals("triggerUpdateFilter()")) {
                        ConfigurationAccess.getLocal().triggerUpdateFilter();
                        this.out.write("OK\n".getBytes());
                        this.out.flush();
                    }
                    else if (replace.equals("doBackup()")) {
                        ConfigurationAccess.getLocal().doBackup(Utils.readLineFromStream(this.in));
                        this.out.write("OK\n".getBytes());
                        this.out.flush();
                    }
                    else if (replace.equals("doRestore()")) {
                        ConfigurationAccess.getLocal().doRestore(Utils.readLineFromStream(this.in));
                        this.out.write("OK\n".getBytes());
                        this.out.flush();
                    }
                    else if (replace.equals("doRestoreDefaults()")) {
                        ConfigurationAccess.getLocal().doRestoreDefaults();
                        this.out.write("OK\n".getBytes());
                        this.out.flush();
                    }
                    else if (replace.equals("wakeLock()")) {
                        ConfigurationAccess.getLocal().wakeLock();
                        this.out.write("OK\n".getBytes());
                        this.out.flush();
                    }
                    else if (replace.equals("releaseWakeLock()")) {
                        ConfigurationAccess.getLocal().releaseWakeLock();
                        this.out.write("OK\n".getBytes());
                        this.out.flush();
                    }
                    else {
                        if (!replace.equals("getAvailableBackups()")) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown action: ");
                            sb.append(replace);
                            throw new ConfigurationAccess.ConfigurationAccessException(sb.toString());
                        }
                        final String[] availableBackups = ConfigurationAccess.getLocal().getAvailableBackups();
                        this.out.write("OK\n".getBytes());
                        final DataOutputStream out = this.out;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(availableBackups.length);
                        sb2.append("\n");
                        out.write(sb2.toString().getBytes());
                        while (i < availableBackups.length) {
                            final DataOutputStream out2 = this.out;
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append(availableBackups[i]);
                            sb3.append("\n");
                            out2.write(sb3.toString().getBytes());
                            ++i;
                        }
                        this.out.flush();
                    }
                }
            }
            catch (ConfigurationAccess.ConfigurationAccessException ex) {
                final DataOutputStream out3 = this.out;
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(ex.getMessage().replace("\n", "\t"));
                sb4.append("\n");
                out3.write(sb4.toString().getBytes());
                this.out.flush();
            }
        }
        
        private void heartBeatConfirmed() {
            this.lastHeartBeatConfirm = System.currentTimeMillis();
        }
        
        @Override
        public long getTimoutTime() {
            return this.timeout;
        }
        
        public void killSession() {
            if (this.killed) {
                return;
            }
            this.killed = true;
            TimoutNotificator.getInstance().unregister(this);
            if (this.remoteLogger != null) {
                this.remoteLogger.closeLogger();
                ((GroupedLogger)Logger.getLogger()).detachLogger(this.remoteLogger);
            }
            Utils.closeSocket(this.socket);
            RemoteAccessServer.this.sessions.remove(this.id);
            if (this.connectedSessionId != -1) {
                final RemoteSession remoteSession = RemoteAccessServer.this.sessions.get(this.connectedSessionId);
                if (remoteSession != null) {
                    remoteSession.killSession();
                }
            }
        }
        
        public void reconnectSession(final Socket socket, final InputStream inputStream, final OutputStream outputStream) throws IOException {
            this.doReconnect = true;
            final Socket socket2 = this.socket;
            this.socket = socket;
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            Utils.closeSocket(socket2);
        }
        
        @Override
        public void run() {
            final byte[] array = new byte[1024];
            String s = "";
            while (!this.killed) {
                String access$200 = s;
                try {
                    final String s2 = s = (access$200 = RemoteAccessServer.this.readStringFromStream(this.in, array));
                    if (s2.equals("attach")) {
                        access$200 = s2;
                        s = s2;
                        this.attachStream();
                        s = s2;
                    }
                    else {
                        access$200 = s2;
                        s = s2;
                        if (s2.equals("releaseConfiguration()")) {
                            access$200 = s2;
                            s = s2;
                            this.killSession();
                            s = s2;
                        }
                        else {
                            access$200 = s2;
                            s = s2;
                            if (s2.equals("confirmHeartBeat()")) {
                                access$200 = s2;
                                s = s2;
                                this.heartBeatConfirmed();
                                s = s2;
                            }
                            else {
                                access$200 = s2;
                                s = s2;
                                this.executeAction(s2);
                                s = s2;
                            }
                        }
                    }
                }
                catch (IOException ex) {
                    if (!this.doReconnect) {
                        s = access$200;
                        if (!this.killed) {
                            final LoggerInterface logger = Logger.getLogger();
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Exception during RemoteServer Session read! ");
                            sb.append(ex.toString());
                            logger.logLine(sb.toString());
                            this.killSession();
                            break;
                        }
                        continue;
                    }
                    else {
                        Logger.getLogger().logLine("Reconnected Remote!");
                        this.doReconnect = false;
                        s = access$200;
                    }
                }
                catch (ConfigurationAccess.ConfigurationAccessException ex2) {
                    final LoggerInterface logger2 = Logger.getLogger();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("RemoteServer Exception processing ");
                    sb2.append(s);
                    sb2.append("! ");
                    sb2.append(ex2.toString());
                    logger2.logLine(sb2.toString());
                }
            }
            final LoggerInterface logger3 = Logger.getLogger();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Remote Session ");
            sb3.append(this.id);
            sb3.append(" closed! ");
            sb3.append(this.socket);
            logger3.logLine(sb3.toString());
        }
        
        @Override
        public void timeoutNotification() {
            if (this.checkLastConfirmedHeartBeat()) {
                this.doHeartBeat(RemoteAccessClient.READ_TIMEOUT);
            }
        }
    }
}
