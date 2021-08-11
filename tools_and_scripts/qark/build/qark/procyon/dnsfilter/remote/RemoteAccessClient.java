// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.remote;

import java.io.ObjectInputStream;
import java.util.Properties;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import util.Utils;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import util.TimoutNotificator;
import java.io.IOException;
import util.Encryption;
import util.Logger;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import util.LoggerInterface;
import util.TimeoutListener;
import dnsfilter.ConfigurationAccess;

public class RemoteAccessClient extends ConfigurationAccess implements TimeoutListener
{
    static int CON_TIMEOUT = 0;
    static final int HEART_BEAT = 6;
    static final int LOG = 1;
    static final int LOG_LN = 2;
    static final int LOG_MSG = 3;
    static int READ_TIMEOUT = 0;
    static final int UPD_CON_CNT = 5;
    static final int UPD_DNS = 4;
    private int con_cnt;
    private LoggerInterface connectedLogger;
    private int ctrlConId;
    private Socket ctrlcon;
    private String host;
    private InputStream in;
    private String last_dns;
    private OutputStream out;
    private int port;
    private RemoteStream remoteStream;
    private String remote_version;
    int timeOutCounter;
    long timeout;
    boolean valid;
    
    static {
        RemoteAccessClient.CON_TIMEOUT = 15000;
        RemoteAccessClient.READ_TIMEOUT = 15000;
    }
    
    public RemoteAccessClient(final LoggerInterface loggerInterface, final String host, final int port, final String s) throws IOException {
        this.ctrlConId = -1;
        this.last_dns = "<unknown>";
        this.con_cnt = -1;
        this.valid = false;
        this.timeout = Long.MAX_VALUE;
        this.timeOutCounter = 0;
        LoggerInterface logger = loggerInterface;
        if (loggerInterface == null) {
            logger = Logger.getLogger();
        }
        this.connectedLogger = logger;
        Encryption.init_AES(s);
        this.host = host;
        this.port = port;
        this.connect();
    }
    
    private void closeConnectionReconnect() {
        TimoutNotificator.getInstance().unregister(this);
        if (!this.valid) {
            return;
        }
        this.releaseConfiguration();
        final Object o = new Object();
        // monitorenter(o)
        try {
            try {
                o.wait(2000L);
            }
            finally {
                // monitorexit(o)
                // monitorexit(o)
                final RemoteAccessClient remoteAccessClient = this;
                remoteAccessClient.connect();
                return;
            }
        }
        catch (InterruptedException ex) {}
        try {
            final RemoteAccessClient remoteAccessClient = this;
            remoteAccessClient.connect();
        }
        catch (IOException ex2) {}
    }
    
    private void connect() throws IOException {
        final Object[] initConnection = this.initConnection();
        this.ctrlcon = (Socket)initConnection[1];
        this.in = (InputStream)initConnection[2];
        this.out = (OutputStream)initConnection[3];
        this.ctrlcon.setSoTimeout(RemoteAccessClient.READ_TIMEOUT);
        this.ctrlConId = (int)initConnection[0];
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
    
    private Object[] initConnection() throws IOException {
        Socket socket = null;
        try {
            final Socket socket2 = socket = new Socket();
            socket2.connect(new InetSocketAddress(InetAddress.getByName(this.host), this.port), RemoteAccessClient.CON_TIMEOUT);
            socket = socket2;
            socket2.setSoTimeout(RemoteAccessClient.READ_TIMEOUT);
            socket = socket2;
            final OutputStream encryptedOutputStream = Encryption.getEncryptedOutputStream(socket2.getOutputStream(), 1024);
            socket = socket2;
            final InputStream decryptedStream = Encryption.getDecryptedStream(socket2.getInputStream());
            socket = socket2;
            encryptedOutputStream.write("1504000\nnew_session\n".getBytes());
            socket = socket2;
            encryptedOutputStream.flush();
            socket = socket2;
            final String lineFromStream = Utils.readLineFromStream(decryptedStream);
            socket = socket2;
            if (!lineFromStream.equals("OK")) {
                socket = socket2;
                throw new IOException(lineFromStream);
            }
            socket = socket2;
            try {
                final int int1 = Integer.parseInt(Utils.readLineFromStream(decryptedStream));
                socket = socket2;
                this.remote_version = Utils.readLineFromStream(decryptedStream);
                socket = socket2;
                this.last_dns = Utils.readLineFromStream(decryptedStream);
                socket = socket2;
                try {
                    this.con_cnt = Integer.parseInt(Utils.readLineFromStream(decryptedStream));
                    socket = socket2;
                    socket2.setSoTimeout(0);
                    socket = socket2;
                    return new Object[] { int1, socket2, decryptedStream, encryptedOutputStream };
                }
                catch (Exception ex) {
                    socket = socket2;
                    throw new IOException(ex);
                }
            }
            catch (Exception ex2) {
                socket = socket2;
                throw new IOException(ex2);
            }
        }
        catch (IOException ex3) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception during initConnection(): ");
            sb.append(ex3.toString());
            connectedLogger.logLine(sb.toString());
            if (socket != null) {
                Utils.closeSocket(socket);
            }
            throw ex3;
        }
    }
    
    private void processHeartBeat() {
        this.connectedLogger.message("Heart Beat!");
        this.timeOutCounter = 0;
        this.setTimeout(RemoteAccessClient.READ_TIMEOUT);
    }
    
    private void setTimeout(final int n) {
        this.timeout = System.currentTimeMillis() + n;
        TimoutNotificator.getInstance().register(this);
    }
    
    private void triggerAction(final String s, String lineFromStream) throws IOException {
        try {
            final OutputStream outputStream = this.getOutputStream();
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("\n");
            outputStream.write(sb.toString().getBytes());
            if (lineFromStream != null) {
                final OutputStream outputStream2 = this.getOutputStream();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(lineFromStream);
                sb2.append("\n");
                outputStream2.write(sb2.toString().getBytes());
            }
            this.getOutputStream().flush();
            lineFromStream = Utils.readLineFromStream(this.getInputStream());
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
        }
        catch (IOException ex) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Remote action ");
            sb3.append(s);
            sb3.append(" failed! ");
            sb3.append(ex.getMessage());
            connectedLogger.logLine(sb3.toString());
            this.closeConnectionReconnect();
            throw ex;
        }
        catch (ConfigurationAccessException ex2) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Remote action failed! ");
            sb4.append(ex2.getMessage());
            connectedLogger2.logLine(sb4.toString());
            throw ex2;
        }
    }
    
    @Override
    public void doBackup(final String s) throws IOException {
        this.triggerAction("doBackup()", s);
    }
    
    @Override
    public void doRestore(final String s) throws IOException {
        this.triggerAction("doRestore()", s);
    }
    
    @Override
    public void doRestoreDefaults() throws IOException {
        this.triggerAction("doRestoreDefaults()", null);
    }
    
    @Override
    public byte[] getAdditionalHosts(final int n) throws IOException {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(this.getOutputStream());
            final DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            dataOutputStream.write("getAdditionalHosts()\n".getBytes());
            dataOutputStream.writeInt(n);
            dataOutputStream.flush();
            final String lineFromStream = Utils.readLineFromStream(dataInputStream);
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
            final byte[] array = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(array);
            return array;
        }
        catch (IOException ex) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Remote action getAdditionalHosts() failed! ");
            sb.append(ex.getMessage());
            connectedLogger.logLine(sb.toString());
            this.closeConnectionReconnect();
            throw ex;
        }
        catch (ConfigurationAccessException ex2) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Remote action failed! ");
            sb2.append(ex2.getMessage());
            connectedLogger2.logLine(sb2.toString());
            throw ex2;
        }
    }
    
    @Override
    public String[] getAvailableBackups() throws IOException {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(this.getOutputStream());
            final DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            dataOutputStream.write("getAvailableBackups()\n".getBytes());
            dataOutputStream.flush();
            final String lineFromStream = Utils.readLineFromStream(dataInputStream);
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
            try {
                final int int1 = Integer.parseInt(Utils.readLineFromStream(dataInputStream));
                final String[] array = new String[int1];
                for (int i = 0; i < int1; ++i) {
                    array[i] = Utils.readLineFromStream(dataInputStream);
                }
                return array;
            }
            catch (Exception ex) {
                throw new IOException(ex);
            }
        }
        catch (IOException ex2) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Remote action  getFilterStatistics() failed! ");
            sb.append(ex2.getMessage());
            connectedLogger.logLine(sb.toString());
            this.closeConnectionReconnect();
            throw ex2;
        }
        catch (ConfigurationAccessException ex3) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Remote action failed! ");
            sb2.append(ex3.getMessage());
            connectedLogger2.logLine(sb2.toString());
            throw ex3;
        }
    }
    
    @Override
    public Properties getConfig() throws IOException {
        try {
            this.getOutputStream().write("getConfig()\n".getBytes());
            this.getOutputStream().flush();
            final InputStream inputStream = this.getInputStream();
            final String lineFromStream = Utils.readLineFromStream(inputStream);
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
            try {
                return (Properties)new ObjectInputStream(inputStream).readObject();
            }
            catch (ClassNotFoundException ex) {
                this.connectedLogger.logException(ex);
                throw new IOException(ex);
            }
        }
        catch (IOException ex2) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Remote action getConfig() failed! ");
            sb.append(ex2.getMessage());
            connectedLogger.logLine(sb.toString());
            this.closeConnectionReconnect();
            throw ex2;
        }
        catch (ConfigurationAccessException ex3) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Remote action failed! ");
            sb2.append(ex3.getMessage());
            connectedLogger2.logLine(sb2.toString());
            throw ex3;
        }
    }
    
    @Override
    public long[] getFilterStatistics() throws IOException {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(this.getOutputStream());
            final DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            dataOutputStream.write("getFilterStatistics()\n".getBytes());
            dataOutputStream.flush();
            final String lineFromStream = Utils.readLineFromStream(dataInputStream);
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
            return new long[] { dataInputStream.readLong(), dataInputStream.readLong() };
        }
        catch (IOException ex) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Remote action  getFilterStatistics() failed! ");
            sb.append(ex.getMessage());
            connectedLogger.logLine(sb.toString());
            this.closeConnectionReconnect();
            throw ex;
        }
        catch (ConfigurationAccessException ex2) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Remote action failed! ");
            sb2.append(ex2.getMessage());
            connectedLogger2.logLine(sb2.toString());
            throw ex2;
        }
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
            final DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            final String lineFromStream = Utils.readLineFromStream(dataInputStream);
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
            final byte[] array = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(array);
            return array;
        }
        catch (IOException ex) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Remote action readConfig() failed! ");
            sb.append(ex.getMessage());
            connectedLogger.logLine(sb.toString());
            this.closeConnectionReconnect();
            throw ex;
        }
        catch (ConfigurationAccessException ex2) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Remote action failed! ");
            sb2.append(ex2.getMessage());
            connectedLogger2.logLine(sb2.toString());
            throw ex2;
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
            catch (IOException ex) {
                final LoggerInterface connectedLogger = this.connectedLogger;
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception during remote configuration release: ");
                sb.append(ex.toString());
                connectedLogger.logLine(sb.toString());
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
        this.setTimeout(RemoteAccessClient.READ_TIMEOUT);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("REMOTE -> ");
        sb.append(this.host);
        sb.append(":");
        sb.append(this.port);
        return sb.toString();
    }
    
    @Override
    public void triggerUpdateFilter() throws IOException {
        this.triggerAction("triggerUpdateFilter()", null);
    }
    
    @Override
    public void updateAdditionalHosts(final byte[] array) throws IOException {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(this.getOutputStream());
            final DataInputStream dataInputStream = new DataInputStream(this.getInputStream());
            dataOutputStream.write("updateAdditionalHosts()\n".getBytes());
            dataOutputStream.writeInt(array.length);
            dataOutputStream.write(array);
            dataOutputStream.flush();
            final String lineFromStream = Utils.readLineFromStream(dataInputStream);
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
        }
        catch (IOException ex) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Remote action updateAdditionalHosts() failed! ");
            sb.append(ex.getMessage());
            connectedLogger.logLine(sb.toString());
            this.closeConnectionReconnect();
            throw ex;
        }
        catch (ConfigurationAccessException ex2) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Remote action failed! ");
            sb2.append(ex2.getMessage());
            connectedLogger2.logLine(sb2.toString());
            throw ex2;
        }
    }
    
    @Override
    public void updateConfig(final byte[] array) throws IOException {
        try {
            final InputStream inputStream = this.getInputStream();
            final DataOutputStream dataOutputStream = new DataOutputStream(this.getOutputStream());
            dataOutputStream.write("updateConfig()\n".getBytes());
            dataOutputStream.writeInt(array.length);
            dataOutputStream.write(array);
            dataOutputStream.flush();
            final String lineFromStream = Utils.readLineFromStream(inputStream);
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
        }
        catch (IOException ex) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb = new StringBuilder();
            sb.append("Remote action updateConfig() failed! ");
            sb.append(ex.getMessage());
            connectedLogger.logLine(sb.toString());
            this.closeConnectionReconnect();
            throw ex;
        }
        catch (ConfigurationAccessException ex2) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Remote action failed! ");
            sb2.append(ex2.getMessage());
            connectedLogger2.logLine(sb2.toString());
            throw ex2;
        }
    }
    
    @Override
    public void updateFilter(String lineFromStream, final boolean b) throws IOException {
        try {
            final OutputStream outputStream = this.getOutputStream();
            final InputStream inputStream = this.getInputStream();
            final StringBuilder sb = new StringBuilder();
            sb.append("updateFilter()\n");
            sb.append(lineFromStream.replace("\n", ";"));
            sb.append("\n");
            sb.append(b);
            sb.append("\n");
            outputStream.write(sb.toString().getBytes());
            outputStream.flush();
            lineFromStream = Utils.readLineFromStream(inputStream);
            if (!lineFromStream.equals("OK")) {
                throw new ConfigurationAccessException(lineFromStream, null);
            }
        }
        catch (IOException ex) {
            final LoggerInterface connectedLogger = this.connectedLogger;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Remote action  updateFilter() failed! ");
            sb2.append(ex.getMessage());
            connectedLogger.logLine(sb2.toString());
            this.closeConnectionReconnect();
            throw ex;
        }
        catch (ConfigurationAccessException ex2) {
            final LoggerInterface connectedLogger2 = this.connectedLogger;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Remote action failed! ");
            sb3.append(ex2.getMessage());
            connectedLogger2.logLine(sb3.toString());
            throw ex2;
        }
    }
    
    @Override
    public void wakeLock() throws IOException {
        this.triggerAction("wakeLock()", null);
    }
    
    private class RemoteStream implements Runnable
    {
        DataInputStream in;
        DataOutputStream out;
        boolean stopped;
        Socket streamCon;
        int streamConId;
        
        public RemoteStream(final int n) throws IOException {
            this.stopped = false;
            final Object[] access$000 = RemoteAccessClient.this.initConnection();
            this.streamCon = (Socket)access$000[1];
            this.in = new DataInputStream((InputStream)access$000[2]);
            this.out = new DataOutputStream((OutputStream)access$000[3]);
            this.streamConId = (int)access$000[0];
            try {
                final DataOutputStream out = this.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("attach\n");
                sb.append(n);
                sb.append("\n");
                out.write(sb.toString().getBytes());
                this.out.flush();
                final String lineFromStream = Utils.readLineFromStream(this.in);
                if (!lineFromStream.equals("OK")) {
                    throw new IOException(lineFromStream);
                }
                new Thread(this).start();
            }
            catch (IOException ex) {
                final LoggerInterface access$2 = RemoteAccessClient.this.connectedLogger;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Remote action attach Remote Stream failed! ");
                sb2.append(ex.getMessage());
                access$2.logLine(sb2.toString());
                RemoteAccessClient.this.closeConnectionReconnect();
                throw ex;
            }
        }
        
        private void confirmHeartBeat() {
            try {
                synchronized (this.out) {
                    this.out.write("confirmHeartBeat()\n".getBytes());
                    this.out.flush();
                }
            }
            catch (IOException ex) {
                final LoggerInterface access$100 = RemoteAccessClient.this.connectedLogger;
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception during confirmHeartBeat()! ");
                sb.append(ex.toString());
                access$100.logLine(sb.toString());
                RemoteAccessClient.this.closeConnectionReconnect();
            }
        }
        
        private byte[] getBuffer(final byte[] array, final int n, final int n2, final int n3) throws IOException {
            if (n < n2 && array.length > n2) {
                return new byte[n2];
            }
            if (n < n2) {
                return array;
            }
            if (n > n3) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Buffer Overflow: ");
                sb.append(n);
                sb.append(" bytes!");
                throw new IOException(sb.toString());
            }
            return new byte[n];
        }
        
        public void close() {
            this.stopped = true;
            if (this.streamCon != null) {
                final DataOutputStream out = this.out;
                // monitorenter(out)
                try {
                    try {
                        this.out.write("releaseConfiguration()".getBytes());
                        this.out.flush();
                    }
                    finally {
                        // monitorexit(out)
                        Utils.closeSocket(this.streamCon);
                    }
                    // monitorexit(out)
                }
                catch (IOException ex) {}
            }
        }
        
        @Override
        public void run() {
            byte[] buffer = new byte[2048];
        Label_0092_Outer:
            while (true) {
                while (true) {
                    Label_0327: {
                        try {
                            if (this.stopped) {
                                return;
                            }
                            final short short1 = this.in.readShort();
                            final short short2 = this.in.readShort();
                            buffer = this.getBuffer(buffer, short2, 2048, 1024000);
                            this.in.readFully(buffer, 0, short2);
                            switch (short1) {
                                case 6: {
                                    RemoteAccessClient.this.processHeartBeat();
                                    this.confirmHeartBeat();
                                    continue Label_0092_Outer;
                                }
                                case 5: {
                                    RemoteAccessClient.this.con_cnt = Integer.parseInt(new String(buffer, 0, short2));
                                    continue Label_0092_Outer;
                                }
                                case 4: {
                                    RemoteAccessClient.this.last_dns = new String(buffer, 0, short2);
                                    continue Label_0092_Outer;
                                }
                                case 3: {
                                    RemoteAccessClient.this.connectedLogger.message(new String(buffer, 0, short2));
                                    continue Label_0092_Outer;
                                }
                                case 2: {
                                    RemoteAccessClient.this.connectedLogger.logLine(new String(buffer, 0, short2));
                                    continue Label_0092_Outer;
                                }
                                case 1: {
                                    RemoteAccessClient.this.connectedLogger.log(new String(buffer, 0, short2));
                                    continue Label_0092_Outer;
                                }
                                default: {
                                    break Label_0327;
                                }
                            }
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown message type: ");
                            sb.append(short1);
                            throw new IOException(sb.toString());
                        }
                        catch (Exception ex) {
                            if (!this.stopped) {
                                final LoggerInterface access$100 = RemoteAccessClient.this.connectedLogger;
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Exception during RemoteStream read! ");
                                sb2.append(ex.toString());
                                access$100.logLine(sb2.toString());
                                RemoteAccessClient.this.closeConnectionReconnect();
                            }
                            return;
                        }
                    }
                    continue;
                }
            }
        }
    }
}
