/*
 * Decompiled with CFR 0_124.
 */
package dnsfilter.remote;

import dnsfilter.ConfigurationAccess;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Properties;
import util.Encryption;
import util.Logger;
import util.LoggerInterface;
import util.TimeoutListener;
import util.TimoutNotificator;
import util.Utils;

public class RemoteAccessClient
extends ConfigurationAccess
implements TimeoutListener {
    static int CON_TIMEOUT = 0;
    static final int HEART_BEAT = 6;
    static final int LOG = 1;
    static final int LOG_LN = 2;
    static final int LOG_MSG = 3;
    static int READ_TIMEOUT = 0;
    static final int UPD_CON_CNT = 5;
    static final int UPD_DNS = 4;
    private int con_cnt = -1;
    private LoggerInterface connectedLogger;
    private int ctrlConId = -1;
    private Socket ctrlcon;
    private String host;
    private InputStream in;
    private String last_dns = "<unknown>";
    private OutputStream out;
    private int port;
    private RemoteStream remoteStream;
    private String remote_version;
    int timeOutCounter = 0;
    long timeout = Long.MAX_VALUE;
    boolean valid = false;

    static {
        CON_TIMEOUT = 15000;
        READ_TIMEOUT = 15000;
    }

    public RemoteAccessClient(LoggerInterface loggerInterface, String string2, int n, String string3) throws IOException {
        LoggerInterface loggerInterface2 = loggerInterface;
        if (loggerInterface == null) {
            loggerInterface2 = Logger.getLogger();
        }
        this.connectedLogger = loggerInterface2;
        Encryption.init_AES(string3);
        this.host = string2;
        this.port = n;
        this.connect();
    }

    static /* synthetic */ String access$302(RemoteAccessClient remoteAccessClient, String string2) {
        remoteAccessClient.last_dns = string2;
        return string2;
    }

    static /* synthetic */ int access$402(RemoteAccessClient remoteAccessClient, int n) {
        remoteAccessClient.con_cnt = n;
        return n;
    }

    static /* synthetic */ void access$500(RemoteAccessClient remoteAccessClient) {
        remoteAccessClient.processHeartBeat();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void closeConnectionReconnect() {
        block9 : {
            TimoutNotificator.getInstance().unregister(this);
            if (!this.valid) {
                return;
            }
            this.releaseConfiguration();
            Object object = new Object();
            // MONITORENTER : object
            object.wait(2000L);
            break block9;
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        // MONITOREXIT : object
        try {
            this.connect();
            return;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Reconnect failed:");
            stringBuilder.append(iOException.toString());
            loggerInterface.logLine(stringBuilder.toString());
            this.valid = false;
            return;
        }
    }

    private void connect() throws IOException {
        Object[] arrobject = this.initConnection();
        this.ctrlcon = (Socket)arrobject[1];
        this.in = (InputStream)arrobject[2];
        this.out = (OutputStream)arrobject[3];
        this.ctrlcon.setSoTimeout(READ_TIMEOUT);
        this.ctrlConId = (Integer)arrobject[0];
        this.remoteStream = new RemoteStream(this.ctrlConId);
        this.valid = true;
    }

    private InputStream getInputStream() throws IOException {
        if (!this.valid) {
            throw new IOException("Not connected!");
        }
        return this.in;
    }

    private OutputStream getOutputStream() throws IOException {
        if (!this.valid) {
            throw new IOException("Not connected!");
        }
        return this.out;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Object[] initConnection() throws IOException {
        Socket socket = null;
        try {
            int n;
            Socket socket2;
            socket = socket2 = new Socket();
            socket2.connect(new InetSocketAddress(InetAddress.getByName(this.host), this.port), CON_TIMEOUT);
            socket = socket2;
            socket2.setSoTimeout(READ_TIMEOUT);
            socket = socket2;
            OutputStream outputStream = Encryption.getEncryptedOutputStream(socket2.getOutputStream(), 1024);
            socket = socket2;
            InputStream inputStream = Encryption.getDecryptedStream(socket2.getInputStream());
            socket = socket2;
            outputStream.write("1504000\nnew_session\n".getBytes());
            socket = socket2;
            outputStream.flush();
            socket = socket2;
            String string2 = Utils.readLineFromStream(inputStream);
            socket = socket2;
            if (!string2.equals("OK")) {
                socket = socket2;
                throw new IOException(string2);
            }
            socket = socket2;
            try {
                n = Integer.parseInt(Utils.readLineFromStream(inputStream));
                socket = socket2;
            }
            catch (Exception exception) {
                socket = socket2;
                throw new IOException(exception);
            }
            this.remote_version = Utils.readLineFromStream(inputStream);
            socket = socket2;
            this.last_dns = Utils.readLineFromStream(inputStream);
            socket = socket2;
            try {
                this.con_cnt = Integer.parseInt(Utils.readLineFromStream(inputStream));
                socket = socket2;
            }
            catch (Exception exception) {
                socket = socket2;
                throw new IOException(exception);
            }
            socket2.setSoTimeout(0);
            socket = socket2;
            return new Object[]{n, socket2, inputStream, outputStream};
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception during initConnection(): ");
            stringBuilder.append(iOException.toString());
            loggerInterface.logLine(stringBuilder.toString());
            if (socket != null) {
                Utils.closeSocket(socket);
            }
            throw iOException;
        }
    }

    private void processHeartBeat() {
        this.connectedLogger.message("Heart Beat!");
        this.timeOutCounter = 0;
        this.setTimeout(READ_TIMEOUT);
    }

    private void setTimeout(int n) {
        this.timeout = System.currentTimeMillis() + (long)n;
        TimoutNotificator.getInstance().register(this);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void triggerAction(String string2, String object) throws IOException {
        try {
            OutputStream outputStream = this.getOutputStream();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("\n");
            outputStream.write(stringBuilder.toString().getBytes());
            if (object != null) {
                outputStream = this.getOutputStream();
                stringBuilder = new StringBuilder();
                stringBuilder.append((String)object);
                stringBuilder.append("\n");
                outputStream.write(stringBuilder.toString().getBytes());
            }
            this.getOutputStream().flush();
            object = Utils.readLineFromStream(this.getInputStream());
            if (!object.equals("OK")) {
                throw new ConfigurationAccess.ConfigurationAccessException((String)object, null);
            }
            return;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action ");
            stringBuilder.append(string2);
            stringBuilder.append(" failed! ");
            stringBuilder.append(iOException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            this.closeConnectionReconnect();
            throw iOException;
        }
        catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
            object = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action failed! ");
            stringBuilder.append(configurationAccessException.getMessage());
            object.logLine(stringBuilder.toString());
            throw configurationAccessException;
        }
    }

    @Override
    public void doBackup(String string2) throws IOException {
        this.triggerAction("doBackup()", string2);
    }

    @Override
    public void doRestore(String string2) throws IOException {
        this.triggerAction("doRestore()", string2);
    }

    @Override
    public void doRestoreDefaults() throws IOException {
        this.triggerAction("doRestoreDefaults()", null);
    }

    @Override
    public byte[] getAdditionalHosts(int n) throws IOException {
        try {
            byte[] arrby = new DataOutputStream(this.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            arrby.write("getAdditionalHosts()\n".getBytes());
            arrby.writeInt(n);
            arrby.flush();
            arrby = Utils.readLineFromStream(dataInputStream);
            if (!arrby.equals("OK")) {
                throw new ConfigurationAccess.ConfigurationAccessException((String)arrby, null);
            }
            arrby = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(arrby);
            return arrby;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action getAdditionalHosts() failed! ");
            stringBuilder.append(iOException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            this.closeConnectionReconnect();
            throw iOException;
        }
        catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action failed! ");
            stringBuilder.append(configurationAccessException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            throw configurationAccessException;
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    @Override
    public String[] getAvailableBackups() throws IOException {
        String[] arrstring = new DataOutputStream(this.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
        arrstring.write("getAvailableBackups()\n".getBytes());
        arrstring.flush();
        arrstring = Utils.readLineFromStream(dataInputStream);
        if (!arrstring.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException((String)arrstring, null);
        }
        try {
            int n = Integer.parseInt(Utils.readLineFromStream(dataInputStream));
            arrstring = new String[n];
            for (int i = 0; i < n; ++i) {
                arrstring[i] = Utils.readLineFromStream(dataInputStream);
            }
        }
        catch (Exception exception) {
            try {
                throw new IOException(exception);
            }
            catch (IOException iOException) {
                arrstring = this.connectedLogger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remote action  getFilterStatistics() failed! ");
                stringBuilder.append(iOException.getMessage());
                arrstring.logLine(stringBuilder.toString());
                this.closeConnectionReconnect();
                throw iOException;
            }
            catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
                arrstring = this.connectedLogger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remote action failed! ");
                stringBuilder.append(configurationAccessException.getMessage());
                arrstring.logLine(stringBuilder.toString());
                throw configurationAccessException;
            }
        }
        {
            continue;
        }
        return arrstring;
    }

    @Override
    public Properties getConfig() throws IOException {
        this.getOutputStream().write("getConfig()\n".getBytes());
        this.getOutputStream().flush();
        Object object = this.getInputStream();
        Object object2 = Utils.readLineFromStream((InputStream)object);
        if (!object2.equals("OK")) {
            throw new ConfigurationAccess.ConfigurationAccessException((String)object2, null);
        }
        try {
            object = (Properties)new ObjectInputStream((InputStream)object).readObject();
            return object;
        }
        catch (ClassNotFoundException classNotFoundException) {
            try {
                this.connectedLogger.logException(classNotFoundException);
                throw new IOException(classNotFoundException);
            }
            catch (IOException iOException) {
                object2 = this.connectedLogger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remote action getConfig() failed! ");
                stringBuilder.append(iOException.getMessage());
                object2.logLine(stringBuilder.toString());
                this.closeConnectionReconnect();
                throw iOException;
            }
            catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
                object2 = this.connectedLogger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remote action failed! ");
                stringBuilder.append(configurationAccessException.getMessage());
                object2.logLine(stringBuilder.toString());
                throw configurationAccessException;
            }
        }
    }

    @Override
    public long[] getFilterStatistics() throws IOException {
        long l;
        long l2;
        try {
            Object object = new DataOutputStream(this.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            object.write("getFilterStatistics()\n".getBytes());
            object.flush();
            object = Utils.readLineFromStream(dataInputStream);
            if (!object.equals("OK")) {
                throw new ConfigurationAccess.ConfigurationAccessException((String)object, null);
            }
            l = dataInputStream.readLong();
            l2 = dataInputStream.readLong();
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action  getFilterStatistics() failed! ");
            stringBuilder.append(iOException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            this.closeConnectionReconnect();
            throw iOException;
        }
        catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action failed! ");
            stringBuilder.append(configurationAccessException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            throw configurationAccessException;
        }
        return new long[]{l, l2};
    }

    @Override
    public String getLastDNSAddress() {
        return this.last_dns;
    }

    @Override
    public long getTimoutTime() {
        return this.timeout;
    }

    @Override
    public String getVersion() throws IOException {
        return this.remote_version;
    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public int openConnectionsCount() {
        return this.con_cnt;
    }

    @Override
    public byte[] readConfig() throws IOException {
        try {
            this.getOutputStream().write("readConfig()\n".getBytes());
            this.getOutputStream().flush();
            DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            byte[] arrby = Utils.readLineFromStream(dataInputStream);
            if (!arrby.equals("OK")) {
                throw new ConfigurationAccess.ConfigurationAccessException((String)arrby, null);
            }
            arrby = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(arrby);
            return arrby;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action readConfig() failed! ");
            stringBuilder.append(iOException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            this.closeConnectionReconnect();
            throw iOException;
        }
        catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action failed! ");
            stringBuilder.append(configurationAccessException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            throw configurationAccessException;
        }
    }

    @Override
    public void releaseConfiguration() {
        TimoutNotificator.getInstance().unregister(this);
        this.valid = false;
        if (this.remoteStream != null) {
            this.remoteStream.close();
        }
        if (this.ctrlcon != null) {
            try {
                this.out.write("releaseConfiguration()".getBytes());
                this.out.flush();
            }
            catch (IOException iOException) {
                LoggerInterface loggerInterface = this.connectedLogger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception during remote configuration release: ");
                stringBuilder.append(iOException.toString());
                loggerInterface.logLine(stringBuilder.toString());
                Utils.closeSocket(this.ctrlcon);
            }
        }
        this.ctrlcon = null;
        this.remoteStream = null;
    }

    @Override
    public void releaseWakeLock() throws IOException {
        this.triggerAction("releaseWakeLock()", null);
    }

    @Override
    public void restart() throws IOException {
        this.triggerAction("restart()", null);
    }

    @Override
    public void stop() throws IOException {
        this.triggerAction("stop()", null);
    }

    @Override
    public void timeoutNotification() {
        ++this.timeOutCounter;
        if (this.timeOutCounter == 2) {
            this.connectedLogger.message("Remote Session is Dead!");
            this.connectedLogger.logLine("Remote Session is Dead! - Closing...!");
            this.timeOutCounter = 0;
            this.closeConnectionReconnect();
            return;
        }
        this.setTimeout(READ_TIMEOUT);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("REMOTE -> ");
        stringBuilder.append(this.host);
        stringBuilder.append(":");
        stringBuilder.append(this.port);
        return stringBuilder.toString();
    }

    @Override
    public void triggerUpdateFilter() throws IOException {
        this.triggerAction("triggerUpdateFilter()", null);
    }

    @Override
    public void updateAdditionalHosts(byte[] object) throws IOException {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(this.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            dataOutputStream.write("updateAdditionalHosts()\n".getBytes());
            dataOutputStream.writeInt(object.length);
            dataOutputStream.write((byte[])object);
            dataOutputStream.flush();
            object = Utils.readLineFromStream(dataInputStream);
            if (!object.equals("OK")) {
                throw new ConfigurationAccess.ConfigurationAccessException((String)object, null);
            }
            return;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action updateAdditionalHosts() failed! ");
            stringBuilder.append(iOException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            this.closeConnectionReconnect();
            throw iOException;
        }
        catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action failed! ");
            stringBuilder.append(configurationAccessException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            throw configurationAccessException;
        }
    }

    @Override
    public void updateConfig(byte[] object) throws IOException {
        try {
            InputStream inputStream = this.getInputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(this.getOutputStream());
            dataOutputStream.write("updateConfig()\n".getBytes());
            dataOutputStream.writeInt(object.length);
            dataOutputStream.write((byte[])object);
            dataOutputStream.flush();
            object = Utils.readLineFromStream(inputStream);
            if (!object.equals("OK")) {
                throw new ConfigurationAccess.ConfigurationAccessException((String)object, null);
            }
            return;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action updateConfig() failed! ");
            stringBuilder.append(iOException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            this.closeConnectionReconnect();
            throw iOException;
        }
        catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action failed! ");
            stringBuilder.append(configurationAccessException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            throw configurationAccessException;
        }
    }

    @Override
    public void updateFilter(String string2, boolean bl) throws IOException {
        try {
            OutputStream outputStream = this.getOutputStream();
            InputStream inputStream = this.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateFilter()\n");
            stringBuilder.append(string2.replace("\n", ";"));
            stringBuilder.append("\n");
            stringBuilder.append(bl);
            stringBuilder.append("\n");
            outputStream.write(stringBuilder.toString().getBytes());
            outputStream.flush();
            string2 = Utils.readLineFromStream(inputStream);
            if (!string2.equals("OK")) {
                throw new ConfigurationAccess.ConfigurationAccessException(string2, null);
            }
            return;
        }
        catch (IOException iOException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action  updateFilter() failed! ");
            stringBuilder.append(iOException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            this.closeConnectionReconnect();
            throw iOException;
        }
        catch (ConfigurationAccess.ConfigurationAccessException configurationAccessException) {
            LoggerInterface loggerInterface = this.connectedLogger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote action failed! ");
            stringBuilder.append(configurationAccessException.getMessage());
            loggerInterface.logLine(stringBuilder.toString());
            throw configurationAccessException;
        }
    }

    @Override
    public void wakeLock() throws IOException {
        this.triggerAction("wakeLock()", null);
    }

    private class RemoteStream
    implements Runnable {
        DataInputStream in;
        DataOutputStream out;
        boolean stopped;
        Socket streamCon;
        int streamConId;

        public RemoteStream(int n) throws IOException {
            this.stopped = false;
            Object object = RemoteAccessClient.this.initConnection();
            this.streamCon = (Socket)object[1];
            this.in = new DataInputStream((InputStream)object[2]);
            this.out = new DataOutputStream((OutputStream)object[3]);
            this.streamConId = (Integer)object[0];
            try {
                object = this.out;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("attach\n");
                stringBuilder.append(n);
                stringBuilder.append("\n");
                object.write(stringBuilder.toString().getBytes());
                this.out.flush();
                object = Utils.readLineFromStream(this.in);
                if (!object.equals("OK")) {
                    throw new IOException((String)object);
                }
                new Thread(this).start();
                return;
            }
            catch (IOException iOException) {
                LoggerInterface loggerInterface = RemoteAccessClient.this.connectedLogger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remote action attach Remote Stream failed! ");
                stringBuilder.append(iOException.getMessage());
                loggerInterface.logLine(stringBuilder.toString());
                RemoteAccessClient.this.closeConnectionReconnect();
                throw iOException;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        private void confirmHeartBeat() {
            try {
                DataOutputStream dataOutputStream = this.out;
                // MONITORENTER : dataOutputStream
            }
            catch (IOException iOException) {
                LoggerInterface loggerInterface = RemoteAccessClient.this.connectedLogger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception during confirmHeartBeat()! ");
                stringBuilder.append(iOException.toString());
                loggerInterface.logLine(stringBuilder.toString());
                RemoteAccessClient.this.closeConnectionReconnect();
                return;
            }
            this.out.write("confirmHeartBeat()\n".getBytes());
            this.out.flush();
            // MONITOREXIT : dataOutputStream
        }

        private byte[] getBuffer(byte[] object, int n, int n2, int n3) throws IOException {
            if (n < n2 && object.length > n2) {
                return new byte[n2];
            }
            if (n < n2) {
                return object;
            }
            if (n > n3) {
                object = new StringBuilder();
                object.append("Buffer Overflow: ");
                object.append(n);
                object.append(" bytes!");
                throw new IOException(object.toString());
            }
            return new byte[n];
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        public void close() {
            block5 : {
                this.stopped = true;
                if (this.streamCon == null) return;
                DataOutputStream dataOutputStream = this.out;
                // MONITORENTER : dataOutputStream
                this.out.write("releaseConfiguration()".getBytes());
                this.out.flush();
                break block5;
                catch (IOException iOException) {
                    LoggerInterface loggerInterface = RemoteAccessClient.this.connectedLogger;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Exception during remote configuration release: ");
                    stringBuilder.append(iOException.toString());
                    loggerInterface.logLine(stringBuilder.toString());
                }
            }
            Utils.closeSocket(this.streamCon);
            // MONITOREXIT : dataOutputStream
        }

        /*
         * Exception decompiling
         */
        @Override
        public void run() {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: First case is not immediately after switch.
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:366)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
            // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
            // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
            // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:682)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:765)
            // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
            // org.benf.cfr.reader.Main.doJar(Main.java:134)
            // org.benf.cfr.reader.Main.main(Main.java:189)
            throw new IllegalStateException("Decompilation failed");
        }
    }

}

