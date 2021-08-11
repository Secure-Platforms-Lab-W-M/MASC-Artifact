/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter.remote;

import dnsfilter.ConfigurationAccess;
import dnsfilter.remote.RemoteAccessClient;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import util.AsyncLogger;
import util.Encryption;
import util.GroupedLogger;
import util.Logger;
import util.LoggerInterface;
import util.TimeoutListener;
import util.TimoutNotificator;
import util.Utils;

public class RemoteAccessServer
implements Runnable {
    private static int sessionId = 0;
    private ServerSocket server;
    private HashMap sessions = new HashMap();
    boolean stopped = false;

    public RemoteAccessServer(int n, String object) throws IOException {
        Encryption.init_AES((String)object);
        this.server = new ServerSocket(n);
        new Thread(this).start();
        object = Logger.getLogger();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Started RemoteAccess Server on port ");
        stringBuilder.append(n);
        object.logLine(stringBuilder.toString());
    }

    private String readStringFromStream(InputStream inputStream, byte[] arrby) throws IOException {
        int n = Utils.readLineBytesFromStream(inputStream, arrby, true, false);
        if (n == -1) {
            throw new EOFException("Stream is closed!");
        }
        return new String(arrby, 0, n).trim();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void run() {
        while (!this.stopped) {
            Socket socket = this.server.accept();
            Object object = Encryption.getDecryptedStream(socket.getInputStream());
            Object object2 = Encryption.getEncryptedOutputStream(socket.getOutputStream(), 1024);
            {
                catch (IOException iOException) {
                    object2 = Logger.getLogger();
                    object = new StringBuilder();
                    object.append("RemoteServerException: ");
                    object.append(iOException.toString());
                    object2.logLine(object.toString());
                    continue;
                }
            }
            try {
                int n;
                byte[] arrby = new byte[1024];
                this.readStringFromStream((InputStream)object, arrby);
                Object object3 = this.readStringFromStream((InputStream)object, arrby);
                if (object3.equals("new_session")) {
                    object2.write("OK\n".getBytes());
                    object3 = new StringBuilder();
                    object3.append(++sessionId);
                    object3.append("\n");
                    object2.write(object3.toString().getBytes());
                    object3 = new StringBuilder();
                    object3.append(ConfigurationAccess.getLocal().getVersion());
                    object3.append("\n");
                    object2.write(object3.toString().getBytes());
                    object3 = new StringBuilder();
                    object3.append(ConfigurationAccess.getLocal().getLastDNSAddress());
                    object3.append("\n");
                    object2.write(object3.toString().getBytes());
                    object3 = new StringBuilder();
                    object3.append(ConfigurationAccess.getLocal().openConnectionsCount());
                    object3.append("\n");
                    object2.write(object3.toString().getBytes());
                    object2.flush();
                    new RemoteSession(socket, (InputStream)object, (OutputStream)object2, sessionId);
                    continue;
                }
                boolean bl = object3.equals("reconnect_session");
                if (!bl) {
                    object = new StringBuilder();
                    object.append("Invalid option: ");
                    object.append((String)object3);
                    throw new IOException(object.toString());
                }
                try {
                    n = Integer.parseInt(this.readStringFromStream((InputStream)object, arrby));
                }
                catch (Exception exception) {
                    throw new IOException(exception);
                }
                object3 = (RemoteSession)this.sessions.get(n);
                if (object3 == null) {
                    object = new StringBuilder();
                    object.append("Reconnect session not found:");
                    object.append(n);
                    throw new IOException(object.toString());
                }
                object3.reconnectSession(socket, (InputStream)object, (OutputStream)object2);
                object2.write("OK\n".getBytes());
                object2.flush();
            }
            catch (IOException iOException) {
                object2.write(iOException.toString().getBytes());
                object2.flush();
                Utils.closeSocket(socket);
                throw iOException;
            }
        }
    }

    public void stop() {
        this.stopped = true;
        RemoteSession[] arrremoteSession = this.sessions.values();
        arrremoteSession = arrremoteSession.toArray(new RemoteSession[0]);
        for (int i = 0; i < arrremoteSession.length; ++i) {
            arrremoteSession[i].killSession();
        }
        try {
            this.server.close();
            return;
        }
        catch (IOException iOException) {
            Logger.getLogger().logException(iOException);
            return;
        }
    }

    private class RemoteSession
    implements Runnable,
    TimeoutListener {
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

        private RemoteSession(Socket socket, InputStream object2, OutputStream outputStream, int n) throws IOException {
            this.connectedSessionId = -1;
            this.killed = false;
            this.doReconnect = false;
            this.timeout = Long.MAX_VALUE;
            this.lastHeartBeatConfirm = System.currentTimeMillis();
            this.id = n;
            this.socket = socket;
            this.out = new DataOutputStream(outputStream);
            this.in = new DataInputStream((InputStream)object2);
            ((RemoteAccessServer)RemoteAccessServer.this).sessions.put(n, this);
            RemoteAccessServer.this = Logger.getLogger();
            object2 = new StringBuilder();
            object2.append("New Remote Session ");
            object2.append(n);
            object2.append(" from :");
            object2.append(socket);
            RemoteAccessServer.this.logLine(object2.toString());
            new Thread(this).start();
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        private void attachStream() throws IOException {
            block7 : {
                try {
                    this.connectedSessionId = Integer.parseInt(Utils.readLineFromStream(this.in));
                }
                catch (Exception exception) {
                    throw new IOException(exception);
                }
                this.remoteLogger = new AsyncLogger(new LoggerInterface(){

                    @Override
                    public void closeLogger() {
                    }

                    @Override
                    public void log(String string2) {
                        this.sendLog(1, string2);
                    }

                    @Override
                    public void logException(Exception serializable) {
                        StringWriter stringWriter = new StringWriter();
                        serializable.printStackTrace(new PrintWriter(stringWriter));
                        serializable = new StringBuilder();
                        serializable.append(stringWriter.toString());
                        serializable.append("\n");
                        this.log(serializable.toString());
                    }

                    @Override
                    public void logLine(String string2) {
                        this.sendLog(2, string2);
                    }

                    @Override
                    public void message(String string2) {
                        this.sendLog(3, string2);
                    }

                    /*
                     * Loose catch block
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     * Converted monitor instructions to comments
                     * Lifted jumps to return sites
                     */
                    public void sendLog(int n, String arrby) {
                        DataOutputStream dataOutputStream = RemoteSession.this.out;
                        // MONITORENTER : dataOutputStream
                        RemoteSession.this.out.writeShort(5);
                        byte[] arrby2 = new byte[]();
                        arrby2.append(ConfigurationAccess.getLocal().openConnectionsCount());
                        arrby2.append("");
                        arrby2 = arrby2.toString().getBytes();
                        RemoteSession.this.out.writeShort(arrby2.length);
                        RemoteSession.this.out.write(arrby2);
                        RemoteSession.this.out.writeShort(4);
                        arrby2 = ConfigurationAccess.getLocal().getLastDNSAddress().getBytes();
                        RemoteSession.this.out.writeShort(arrby2.length);
                        RemoteSession.this.out.write(arrby2);
                        arrby = arrby.getBytes();
                        RemoteSession.this.out.writeShort(n);
                        RemoteSession.this.out.writeShort(arrby.length);
                        RemoteSession.this.out.write(arrby);
                        RemoteSession.this.out.flush();
                        return;
                        catch (IOException iOException) {
                            RemoteSession.this.killSession();
                            LoggerInterface loggerInterface = Logger.getLogger();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Exception during remote logging! ");
                            stringBuilder.append(iOException.toString());
                            loggerInterface.logLine(stringBuilder.toString());
                        }
                        // MONITOREXIT : dataOutputStream
                    }
                });
                DataOutputStream dataOutputStream = this.out;
                // MONITORENTER : dataOutputStream
                ((GroupedLogger)Logger.getLogger()).attachLogger(this.remoteLogger);
                break block7;
                catch (ClassCastException classCastException) {
                    Logger.setLogger(new GroupedLogger(new LoggerInterface[]{Logger.getLogger(), this.remoteLogger}));
                }
            }
            this.out.write("OK\n".getBytes());
            this.doHeartBeat(RemoteAccessClient.READ_TIMEOUT);
            // MONITOREXIT : dataOutputStream
        }

        private boolean checkLastConfirmedHeartBeat() {
            if (System.currentTimeMillis() - this.lastHeartBeatConfirm > (long)(2 * RemoteAccessClient.READ_TIMEOUT)) {
                Logger.getLogger().logLine("Heartbeat Confirmation not received - Dead Session!");
                this.killSession();
                return false;
            }
            return true;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        private void doHeartBeat(int n) {
            try {
                DataOutputStream dataOutputStream = this.out;
                // MONITORENTER : dataOutputStream
            }
            catch (IOException iOException) {
                LoggerInterface loggerInterface = Logger.getLogger();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Heartbeat failed! ");
                stringBuilder.append(iOException);
                loggerInterface.logLine(stringBuilder.toString());
                this.killSession();
                return;
            }
            this.out.writeShort(6);
            this.out.writeShort(0);
            this.out.flush();
            // MONITOREXIT : dataOutputStream
            this.timeout = System.currentTimeMillis() + (long)n;
            TimoutNotificator.getInstance().register(this);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void executeAction(String arrl) throws IOException {
            DataOutputStream dataOutputStream;
            StringBuilder stringBuilder;
            try {
                if (arrl.equals("getConfig()")) {
                    arrl = ConfigurationAccess.getLocal().getConfig();
                    this.out.write("OK\n".getBytes());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.out);
                    objectOutputStream.writeObject(arrl);
                    objectOutputStream.flush();
                    return;
                }
                if (arrl.equals("readConfig()")) {
                    arrl = ConfigurationAccess.getLocal().readConfig();
                    this.out.write("OK\n".getBytes());
                    this.out.writeInt(arrl.length);
                    this.out.write((byte[])arrl);
                    this.out.flush();
                    return;
                }
                if (arrl.equals("updateConfig()")) {
                    arrl = new byte[this.in.readInt()];
                    this.in.readFully((byte[])arrl);
                    ConfigurationAccess.getLocal().updateConfig((byte[])arrl);
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("getAdditionalHosts()")) {
                    int n = this.in.readInt();
                    arrl = ConfigurationAccess.getLocal().getAdditionalHosts(n);
                    this.out.write("OK\n".getBytes());
                    this.out.writeInt(arrl.length);
                    this.out.write((byte[])arrl);
                    this.out.flush();
                    return;
                }
                if (arrl.equals("updateAdditionalHosts()")) {
                    arrl = new byte[this.in.readInt()];
                    this.in.readFully((byte[])arrl);
                    ConfigurationAccess.getLocal().updateAdditionalHosts((byte[])arrl);
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("updateFilter()")) {
                    arrl = Utils.readLineFromStream(this.in).replace(";", "\n");
                    boolean bl = Boolean.parseBoolean(Utils.readLineFromStream(this.in));
                    ConfigurationAccess.getLocal().updateFilter((String)arrl, bl);
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("restart()")) {
                    ConfigurationAccess.getLocal().restart();
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("stop()")) {
                    ConfigurationAccess.getLocal().stop();
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                boolean bl = arrl.equals("getFilterStatistics()");
                if (bl) {
                    arrl = ConfigurationAccess.getLocal().getFilterStatistics();
                    this.out.write("OK\n".getBytes());
                    this.out.writeLong(arrl[0]);
                    this.out.writeLong(arrl[1]);
                    this.out.flush();
                    return;
                }
                if (arrl.equals("triggerUpdateFilter()")) {
                    ConfigurationAccess.getLocal().triggerUpdateFilter();
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("doBackup()")) {
                    ConfigurationAccess.getLocal().doBackup(Utils.readLineFromStream(this.in));
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("doRestore()")) {
                    ConfigurationAccess.getLocal().doRestore(Utils.readLineFromStream(this.in));
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("doRestoreDefaults()")) {
                    ConfigurationAccess.getLocal().doRestoreDefaults();
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("wakeLock()")) {
                    ConfigurationAccess.getLocal().wakeLock();
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("releaseWakeLock()")) {
                    ConfigurationAccess.getLocal().releaseWakeLock();
                    this.out.write("OK\n".getBytes());
                    this.out.flush();
                    return;
                }
                if (arrl.equals("getAvailableBackups()")) {
                    arrl = ConfigurationAccess.getLocal().getAvailableBackups();
                    this.out.write("OK\n".getBytes());
                    dataOutputStream = this.out;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(arrl.length);
                    stringBuilder.append("\n");
                    dataOutputStream.write(stringBuilder.toString().getBytes());
                    for (int i = 0; i < arrl.length; ++i) {
                        dataOutputStream = this.out;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append((String)arrl[i]);
                        stringBuilder.append("\n");
                        dataOutputStream.write(stringBuilder.toString().getBytes());
                    }
                    this.out.flush();
                    return;
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unknown action: ");
                stringBuilder2.append((String)arrl);
                throw new ConfigurationAccess.ConfigurationAccessException(stringBuilder2.toString());
            }
            catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
                dataOutputStream = this.out;
                stringBuilder = new StringBuilder();
                stringBuilder.append(configurationAccessException.getMessage().replace("\n", "\t"));
                stringBuilder.append("\n");
                dataOutputStream.write(stringBuilder.toString().getBytes());
                this.out.flush();
                return;
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
            RemoteSession remoteSession;
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
            if (this.connectedSessionId != -1 && (remoteSession = (RemoteSession)RemoteAccessServer.this.sessions.get(this.connectedSessionId)) != null) {
                remoteSession.killSession();
            }
        }

        public void reconnectSession(Socket socket, InputStream closeable, OutputStream outputStream) throws IOException {
            this.doReconnect = true;
            closeable = this.socket;
            this.socket = socket;
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            Utils.closeSocket((Socket)closeable);
        }

        @Override
        public void run() {
            Object object;
            byte[] arrby = new byte[1024];
            Object object2 = "";
            while (!this.killed) {
                Object object3;
                block14 : {
                    block13 : {
                        block12 : {
                            object = object2;
                            object = object3 = RemoteAccessServer.this.readStringFromStream(this.in, arrby);
                            object2 = object3;
                            if (!object3.equals("attach")) break block12;
                            object = object3;
                            object2 = object3;
                            this.attachStream();
                            object2 = object3;
                        }
                        object = object3;
                        object2 = object3;
                        if (!object3.equals("releaseConfiguration()")) break block13;
                        object = object3;
                        object2 = object3;
                        this.killSession();
                        object2 = object3;
                        continue;
                    }
                    object = object3;
                    object2 = object3;
                    if (!object3.equals("confirmHeartBeat()")) break block14;
                    object = object3;
                    object2 = object3;
                    this.heartBeatConfirmed();
                    object2 = object3;
                    continue;
                }
                object = object3;
                object2 = object3;
                try {
                    this.executeAction((String)object3);
                    object2 = object3;
                }
                catch (IOException iOException) {
                    if (!this.doReconnect) {
                        object2 = object;
                        if (this.killed) continue;
                        object2 = Logger.getLogger();
                        object = new StringBuilder();
                        object.append("Exception during RemoteServer Session read! ");
                        object.append(iOException.toString());
                        object2.logLine(object.toString());
                        this.killSession();
                        break;
                    }
                    Logger.getLogger().logLine("Reconnected Remote!");
                    this.doReconnect = false;
                    object2 = object;
                }
                catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
                    object3 = Logger.getLogger();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("RemoteServer Exception processing ");
                    stringBuilder.append((String)object2);
                    stringBuilder.append("! ");
                    stringBuilder.append(configurationAccessException.toString());
                    object3.logLine(stringBuilder.toString());
                }
            }
            object2 = Logger.getLogger();
            object = new StringBuilder();
            object.append("Remote Session ");
            object.append(this.id);
            object.append(" closed! ");
            object.append(this.socket);
            object2.logLine(object.toString());
        }

        @Override
        public void timeoutNotification() {
            if (this.checkLastConfirmedHeartBeat()) {
                this.doHeartBeat(RemoteAccessClient.READ_TIMEOUT);
            }
        }

    }

}

