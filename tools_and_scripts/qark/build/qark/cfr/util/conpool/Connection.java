/*
 * Decompiled with CFR 0_124.
 */
package util.conpool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.net.ssl.SSLSocketFactory;
import util.ExecutionEnvironment;
import util.TimeoutListener;
import util.TimeoutTime;
import util.TimoutNotificator;
import util.conpool.HttpProxy;
import util.conpool.PooledConnectionInputStream;
import util.conpool.PooledConnectionOutputStream;

public class Connection
implements TimeoutListener {
    private static Hashtable CUSTOM_HOSTS;
    private static String CUSTOM_HOSTS_FILE_NAME;
    private static byte[] NO_IP;
    private static int POOLTIMEOUT_SECONDS;
    private static HashSet connAquired;
    private static HashMap connPooled;
    private static TimoutNotificator toNotify;
    boolean aquired = true;
    private PooledConnectionInputStream in;
    private PooledConnectionOutputStream out;
    String poolKey;
    private Socket socket = null;
    private InputStream socketIn;
    private OutputStream socketOut;
    boolean ssl = false;
    TimeoutTime timeout;
    boolean valid = true;

    static {
        NO_IP = new byte[]{0, 0, 0, 0};
        connPooled = new HashMap();
        connAquired = new HashSet();
        CUSTOM_HOSTS = Connection.getCustomHosts();
        CUSTOM_HOSTS_FILE_NAME = null;
        POOLTIMEOUT_SECONDS = 300;
        toNotify = TimoutNotificator.getNewInstance();
    }

    private Connection(String string2, int n, int n2, boolean bl, SSLSocketFactory sSLSocketFactory, Proxy proxy) throws IOException {
        Serializable serializable = null;
        if (CUSTOM_HOSTS != null) {
            serializable = (InetAddress)CUSTOM_HOSTS.get(string2);
        }
        InetAddress inetAddress = serializable;
        if (serializable == null) {
            inetAddress = proxy == Proxy.NO_PROXY ? InetAddress.getByName(string2) : InetAddress.getByAddress(string2, NO_IP);
        }
        serializable = new InetSocketAddress(inetAddress, n);
        this.poolKey = Connection.poolKey(string2, n, bl, proxy);
        this.initConnection((InetSocketAddress)serializable, n2, bl, sSLSocketFactory, proxy);
        this.timeout = new TimeoutTime(toNotify);
    }

    private Connection(InetSocketAddress inetSocketAddress, int n, boolean bl, SSLSocketFactory sSLSocketFactory, Proxy proxy) throws IOException {
        this.poolKey = Connection.poolKey(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort(), bl, proxy);
        this.initConnection(inetSocketAddress, n, bl, sSLSocketFactory, proxy);
        this.timeout = new TimeoutTime(toNotify);
    }

    public static void addCustomHost(InetAddress inetAddress) {
        synchronized (Connection.class) {
            if (CUSTOM_HOSTS == null) {
                CUSTOM_HOSTS = new Hashtable();
            }
            CUSTOM_HOSTS.put(inetAddress.getHostName(), inetAddress);
            return;
        }
    }

    public static Connection connect(String string2, int n) throws IOException {
        return Connection.connect(string2, n, -1, false, null, Proxy.NO_PROXY);
    }

    public static Connection connect(String string2, int n, int n2) throws IOException {
        return Connection.connect(string2, n, n2, false, null, Proxy.NO_PROXY);
    }

    public static Connection connect(String string2, int n, int n2, boolean bl, SSLSocketFactory sSLSocketFactory, Proxy proxy) throws IOException {
        Connection connection;
        Connection connection2 = connection = Connection.poolRemove(Connection.poolKey(string2, n, bl, proxy));
        if (connection == null) {
            connection2 = new Connection(string2, n, n2, bl, sSLSocketFactory, proxy);
        }
        connection2.initStreams();
        connAquired.add(connection2);
        return connection2;
    }

    public static Connection connect(InetSocketAddress inetSocketAddress) throws IOException {
        return Connection.connect(inetSocketAddress, -1);
    }

    public static Connection connect(InetSocketAddress inetSocketAddress, int n) throws IOException {
        return Connection.connect(inetSocketAddress, n, false, null, Proxy.NO_PROXY);
    }

    public static Connection connect(InetSocketAddress inetSocketAddress, int n, boolean bl, SSLSocketFactory sSLSocketFactory, Proxy proxy) throws IOException {
        Connection connection;
        Connection connection2 = connection = Connection.poolRemove(Connection.poolKey(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort(), bl, proxy));
        if (connection == null) {
            connection2 = new Connection(inetSocketAddress, n, bl, sSLSocketFactory, proxy);
        }
        connection2.initStreams();
        connAquired.add(connection2);
        return connection2;
    }

    private static Hashtable getCustomHosts() {
        String[] arrstring;
        block9 : {
            String[] arrstring2 = CUSTOM_HOSTS_FILE_NAME;
            String[] arrstring3 = null;
            arrstring = null;
            if (arrstring2 == null) {
                return null;
            }
            arrstring2 = new StringBuilder();
            arrstring2.append(ExecutionEnvironment.getEnvironment().getWorkDir());
            arrstring2.append(CUSTOM_HOSTS_FILE_NAME);
            Object object = new File(arrstring2.toString());
            arrstring2 = arrstring3;
            if (!object.exists()) break block9;
            arrstring2 = arrstring3;
            arrstring2 = arrstring3 = new String[]();
            try {
                object = new BufferedReader(new InputStreamReader(new FileInputStream((File)object)));
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
                return arrstring2;
            }
            do {
                arrstring2 = arrstring3;
                String string2 = object.readLine();
                arrstring = arrstring3;
                if (string2 == null) break;
                arrstring2 = arrstring3;
                arrstring = Connection.parseHosts(string2);
                if (arrstring == null) continue;
                arrstring2 = arrstring3;
                arrstring3.put(arrstring[1], InetAddress.getByName(arrstring[0]));
                continue;
                break;
            } while (true);
        }
        return arrstring;
    }

    private void initConnection(InetSocketAddress inetSocketAddress, int n, boolean bl, SSLSocketFactory sSLSocketFactory, Proxy object) throws IOException {
        int n2 = n;
        if (n < 0) {
            n2 = 0;
        }
        if (object == Proxy.NO_PROXY) {
            this.socket = new Socket();
            this.socket.connect(inetSocketAddress, n2);
        } else {
            if (!(object instanceof HttpProxy)) {
                inetSocketAddress = new StringBuilder();
                inetSocketAddress.append("Only ");
                inetSocketAddress.append(HttpProxy.class.getName());
                inetSocketAddress.append(" supported for creating connection over tunnel!");
                throw new IOException(inetSocketAddress.toString());
            }
            this.socket = ((HttpProxy)object).openTunnel(inetSocketAddress, n2);
        }
        if (bl) {
            this.socket.setSoTimeout(n2);
            object = sSLSocketFactory;
            if (sSLSocketFactory == null) {
                object = (SSLSocketFactory)SSLSocketFactory.getDefault();
            }
            this.socket = object.createSocket(this.socket, inetSocketAddress.getHostName(), inetSocketAddress.getPort(), true);
            this.ssl = true;
        }
        this.socketIn = this.socket.getInputStream();
        this.socketOut = this.socket.getOutputStream();
        if (bl) {
            this.socket.setSoTimeout(0);
        }
    }

    private void initStreams() {
        this.in = new PooledConnectionInputStream(this.socketIn);
        this.out = new PooledConnectionOutputStream(this.socketOut);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void invalidate() {
        HashMap hashMap = connPooled;
        synchronized (hashMap) {
            Vector[] arrvector = connPooled.values().toArray(new Vector[0]);
            int n = 0;
            do {
                if (n < arrvector.length) {
                    Connection[] arrconnection = arrvector[n].toArray(new Connection[0]);
                    for (int i = 0; i < arrconnection.length; ++i) {
                        arrconnection[i].release(false);
                    }
                } else {
                    arrvector = connAquired.toArray(new Connection[0]);
                    n = 0;
                    while (n < arrvector.length) {
                        arrvector[n].release(false);
                        ++n;
                    }
                    return;
                }
                ++n;
            } while (true);
        }
    }

    private boolean isAlive() {
        block6 : {
            this.socket.setSoTimeout(1);
            int n = this.socketIn.read();
            if (n == -1) break block6;
            try {
                byte[] arrby = new byte[Math.max(this.socketIn.available(), 10240)];
                this.socketIn.read(arrby);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((char)n);
                stringBuilder.append(new String(arrby));
                stringBuilder.toString();
            }
            catch (Exception exception) {
                return false;
            }
            catch (SocketTimeoutException socketTimeoutException) {
                try {
                    this.socket.setSoTimeout(0);
                    return true;
                }
                catch (SocketException socketException) {
                    return false;
                }
            }
        }
        return false;
    }

    private static String[] parseHosts(String object) {
        if (!object.startsWith("#") && !object.trim().equals("")) {
            if ((object = new StringTokenizer((String)object)).countTokens() >= 2) {
                return new String[]{object.nextToken().trim(), object.nextToken().trim()};
            }
            return new String[]{"127.0.0.1", object.nextToken().trim()};
        }
        return null;
    }

    private static String poolKey(String string2, int n, boolean bl, Proxy proxy) {
        if (bl) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(":");
            stringBuilder.append(n);
            stringBuilder.append(":ssl:");
            stringBuilder.append(proxy.hashCode());
            return stringBuilder.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(":");
        stringBuilder.append(n);
        stringBuilder.append(":plain:");
        stringBuilder.append(proxy.hashCode());
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Connection poolRemove(String string2) {
        HashMap hashMap = connPooled;
        synchronized (hashMap) {
            Vector vector = (Vector)connPooled.get(string2);
            Connection connection = null;
            if (vector == null) {
                return null;
            }
            boolean bl = false;
            while (!bl && !vector.isEmpty()) {
                Connection connection2 = (Connection)vector.remove(vector.size() - 1);
                if (connection2.aquired) {
                    throw new IllegalStateException("Inconsistent Connection State - Cannot take already aquired connection from pool!");
                }
                connection2.aquired = true;
                toNotify.unregister(connection2);
                boolean bl2 = connection2.isAlive();
                connection = connection2;
                bl = bl2;
                if (bl2) continue;
                connection2.release(false);
                connection = null;
                bl = bl2;
            }
            if (vector.isEmpty()) {
                connPooled.remove(string2);
            }
            return connection;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void poolReuse(Connection connection) {
        HashMap hashMap = connPooled;
        synchronized (hashMap) {
            Vector<Connection> vector;
            if (!connection.aquired) {
                throw new IllegalStateException("Inconsistent Connection State - Cannot release non aquired connection");
            }
            connection.aquired = false;
            Vector<Connection> vector2 = vector = (Vector<Connection>)connPooled.get(connection.poolKey);
            if (vector == null) {
                vector2 = new Vector<Connection>();
                connPooled.put(connection.poolKey, vector2);
            }
            toNotify.register(connection);
            connection.timeout.setTimeout(POOLTIMEOUT_SECONDS * 1000);
            vector2.add(connection);
            return;
        }
    }

    public static void setCustomHostsFile(String string2) {
        CUSTOM_HOSTS_FILE_NAME = string2;
    }

    public static void setPoolTimeoutSeconds(int n) {
        POOLTIMEOUT_SECONDS = n;
    }

    public String getDestination() {
        return this.poolKey;
    }

    public InputStream getInputStream() {
        return this.in;
    }

    public OutputStream getOutputStream() {
        return this.out;
    }

    @Override
    public long getTimoutTime() {
        return this.timeout.getTimeout();
    }

    public long[] getTraffic() {
        if (!this.aquired) {
            throw new IllegalStateException("Inconsistent Connection State - Connection is not aquired!");
        }
        return new long[]{this.in.getTraffic(), this.out.getTraffic()};
    }

    public void release(boolean bl) {
        if (!this.valid) {
            return;
        }
        connAquired.remove(this);
        if (bl) {
            this.in.invalidate();
            this.out.invalidate();
            try {
                this.socket.setSoTimeout(0);
            }
            catch (SocketException socketException) {
                this.release(false);
                return;
            }
            Connection.poolReuse(this);
            return;
        }
        try {
            this.valid = false;
            if (!this.ssl) {
                this.socket.shutdownOutput();
                this.socket.shutdownInput();
            }
            this.socket.close();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    public void setSoTimeout(int n) throws SocketException {
        this.socket.setSoTimeout(n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void timeoutNotification() {
        HashMap hashMap = connPooled;
        // MONITORENTER : hashMap
        Vector vector = (Vector)connPooled.get(this.poolKey);
        if (vector == null) {
            // MONITOREXIT : hashMap
            return;
        }
        boolean bl = vector.remove(this);
        if (vector.isEmpty()) {
            connPooled.remove(this.poolKey);
        }
        // MONITOREXIT : hashMap
        if (!bl) return;
        this.release(false);
    }
}

