// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util.conpool;

import java.util.Vector;
import java.util.StringTokenizer;
import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.net.SocketAddress;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import util.ExecutionEnvironment;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Proxy;
import javax.net.ssl.SSLSocketFactory;
import util.TimeoutTime;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import util.TimoutNotificator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import util.TimeoutListener;

public class Connection implements TimeoutListener
{
    private static Hashtable CUSTOM_HOSTS;
    private static String CUSTOM_HOSTS_FILE_NAME;
    private static byte[] NO_IP;
    private static int POOLTIMEOUT_SECONDS;
    private static HashSet connAquired;
    private static HashMap connPooled;
    private static TimoutNotificator toNotify;
    boolean aquired;
    private PooledConnectionInputStream in;
    private PooledConnectionOutputStream out;
    String poolKey;
    private Socket socket;
    private InputStream socketIn;
    private OutputStream socketOut;
    boolean ssl;
    TimeoutTime timeout;
    boolean valid;
    
    static {
        Connection.NO_IP = new byte[] { 0, 0, 0, 0 };
        Connection.connPooled = new HashMap();
        Connection.connAquired = new HashSet();
        Connection.CUSTOM_HOSTS = getCustomHosts();
        Connection.CUSTOM_HOSTS_FILE_NAME = null;
        Connection.POOLTIMEOUT_SECONDS = 300;
        Connection.toNotify = TimoutNotificator.getNewInstance();
    }
    
    private Connection(final String s, final int n, final int n2, final boolean b, final SSLSocketFactory sslSocketFactory, final Proxy proxy) throws IOException {
        this.socket = null;
        this.aquired = true;
        this.valid = true;
        this.ssl = false;
        InetAddress inetAddress = null;
        if (Connection.CUSTOM_HOSTS != null) {
            inetAddress = Connection.CUSTOM_HOSTS.get(s);
        }
        InetAddress inetAddress2;
        if ((inetAddress2 = inetAddress) == null) {
            if (proxy == Proxy.NO_PROXY) {
                inetAddress2 = InetAddress.getByName(s);
            }
            else {
                inetAddress2 = InetAddress.getByAddress(s, Connection.NO_IP);
            }
        }
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress2, n);
        this.poolKey = poolKey(s, n, b, proxy);
        this.initConnection(inetSocketAddress, n2, b, sslSocketFactory, proxy);
        this.timeout = new TimeoutTime(Connection.toNotify);
    }
    
    private Connection(final InetSocketAddress inetSocketAddress, final int n, final boolean b, final SSLSocketFactory sslSocketFactory, final Proxy proxy) throws IOException {
        this.socket = null;
        this.aquired = true;
        this.valid = true;
        this.ssl = false;
        this.poolKey = poolKey(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort(), b, proxy);
        this.initConnection(inetSocketAddress, n, b, sslSocketFactory, proxy);
        this.timeout = new TimeoutTime(Connection.toNotify);
    }
    
    public static void addCustomHost(final InetAddress inetAddress) {
        synchronized (Connection.class) {
            if (Connection.CUSTOM_HOSTS == null) {
                Connection.CUSTOM_HOSTS = new Hashtable();
            }
            Connection.CUSTOM_HOSTS.put(inetAddress.getHostName(), inetAddress);
        }
    }
    
    public static Connection connect(final String s, final int n) throws IOException {
        return connect(s, n, -1, false, null, Proxy.NO_PROXY);
    }
    
    public static Connection connect(final String s, final int n, final int n2) throws IOException {
        return connect(s, n, n2, false, null, Proxy.NO_PROXY);
    }
    
    public static Connection connect(final String s, final int n, final int n2, final boolean b, final SSLSocketFactory sslSocketFactory, final Proxy proxy) throws IOException {
        Connection poolRemove;
        if ((poolRemove = poolRemove(poolKey(s, n, b, proxy))) == null) {
            poolRemove = new Connection(s, n, n2, b, sslSocketFactory, proxy);
        }
        poolRemove.initStreams();
        Connection.connAquired.add(poolRemove);
        return poolRemove;
    }
    
    public static Connection connect(final InetSocketAddress inetSocketAddress) throws IOException {
        return connect(inetSocketAddress, -1);
    }
    
    public static Connection connect(final InetSocketAddress inetSocketAddress, final int n) throws IOException {
        return connect(inetSocketAddress, n, false, null, Proxy.NO_PROXY);
    }
    
    public static Connection connect(final InetSocketAddress inetSocketAddress, final int n, final boolean b, final SSLSocketFactory sslSocketFactory, final Proxy proxy) throws IOException {
        Connection poolRemove;
        if ((poolRemove = poolRemove(poolKey(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort(), b, proxy))) == null) {
            poolRemove = new Connection(inetSocketAddress, n, b, sslSocketFactory, proxy);
        }
        poolRemove.initStreams();
        Connection.connAquired.add(poolRemove);
        return poolRemove;
    }
    
    private static Hashtable getCustomHosts() {
        final String custom_HOSTS_FILE_NAME = Connection.CUSTOM_HOSTS_FILE_NAME;
        final Hashtable<String, InetAddress> hashtable = null;
        Hashtable<String, InetAddress> hashtable2 = null;
        if (custom_HOSTS_FILE_NAME == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ExecutionEnvironment.getEnvironment().getWorkDir());
        sb.append(Connection.CUSTOM_HOSTS_FILE_NAME);
        final File file = new File(sb.toString());
        Hashtable<String, InetAddress> hashtable3 = hashtable;
        try {
            if (file.exists()) {
                hashtable3 = hashtable;
                final Hashtable<String, InetAddress> hashtable4 = hashtable3 = new Hashtable<String, InetAddress>();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                while (true) {
                    hashtable3 = hashtable4;
                    final String line = bufferedReader.readLine();
                    hashtable2 = hashtable4;
                    if (line == null) {
                        break;
                    }
                    hashtable3 = hashtable4;
                    final String[] hosts = parseHosts(line);
                    if (hosts == null) {
                        continue;
                    }
                    hashtable3 = hashtable4;
                    hashtable4.put(hosts[1], InetAddress.getByName(hosts[0]));
                }
            }
            return hashtable2;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return hashtable3;
        }
    }
    
    private void initConnection(final InetSocketAddress inetSocketAddress, final int n, final boolean b, final SSLSocketFactory sslSocketFactory, final Proxy proxy) throws IOException {
        int soTimeout = n;
        if (n < 0) {
            soTimeout = 0;
        }
        if (proxy == Proxy.NO_PROXY) {
            (this.socket = new Socket()).connect(inetSocketAddress, soTimeout);
        }
        else {
            if (!(proxy instanceof HttpProxy)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Only ");
                sb.append(HttpProxy.class.getName());
                sb.append(" supported for creating connection over tunnel!");
                throw new IOException(sb.toString());
            }
            this.socket = ((HttpProxy)proxy).openTunnel(inetSocketAddress, soTimeout);
        }
        if (b) {
            this.socket.setSoTimeout(soTimeout);
            SSLSocketFactory sslSocketFactory2;
            if ((sslSocketFactory2 = sslSocketFactory) == null) {
                sslSocketFactory2 = (SSLSocketFactory)SSLSocketFactory.getDefault();
            }
            this.socket = sslSocketFactory2.createSocket(this.socket, inetSocketAddress.getHostName(), inetSocketAddress.getPort(), true);
            this.ssl = true;
        }
        this.socketIn = this.socket.getInputStream();
        this.socketOut = this.socket.getOutputStream();
        if (b) {
            this.socket.setSoTimeout(0);
        }
    }
    
    private void initStreams() {
        this.in = new PooledConnectionInputStream(this.socketIn);
        this.out = new PooledConnectionOutputStream(this.socketOut);
    }
    
    public static void invalidate() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_2       
        //     4: aload_2        
        //     5: monitorenter   
        //     6: getstatic       util/conpool/Connection.connPooled:Ljava/util/HashMap;
        //     9: invokevirtual   java/util/HashMap.values:()Ljava/util/Collection;
        //    12: iconst_0       
        //    13: anewarray       Ljava/util/Vector;
        //    16: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    21: checkcast       [Ljava/util/Vector;
        //    24: astore_3       
        //    25: iconst_0       
        //    26: istore_0       
        //    27: iload_0        
        //    28: aload_3        
        //    29: arraylength    
        //    30: if_icmpge       72
        //    33: aload_3        
        //    34: iload_0        
        //    35: aaload         
        //    36: iconst_0       
        //    37: anewarray       Lutil/conpool/Connection;
        //    40: invokevirtual   java/util/Vector.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    43: checkcast       [Lutil/conpool/Connection;
        //    46: astore          4
        //    48: iconst_0       
        //    49: istore_1       
        //    50: iload_1        
        //    51: aload           4
        //    53: arraylength    
        //    54: if_icmpge       116
        //    57: aload           4
        //    59: iload_1        
        //    60: aaload         
        //    61: iconst_0       
        //    62: invokevirtual   util/conpool/Connection.release:(Z)V
        //    65: iload_1        
        //    66: iconst_1       
        //    67: iadd           
        //    68: istore_1       
        //    69: goto            50
        //    72: getstatic       util/conpool/Connection.connAquired:Ljava/util/HashSet;
        //    75: iconst_0       
        //    76: anewarray       Lutil/conpool/Connection;
        //    79: invokevirtual   java/util/HashSet.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    82: checkcast       [Lutil/conpool/Connection;
        //    85: astore_3       
        //    86: iconst_0       
        //    87: istore_0       
        //    88: iload_0        
        //    89: aload_3        
        //    90: arraylength    
        //    91: if_icmpge       108
        //    94: aload_3        
        //    95: iload_0        
        //    96: aaload         
        //    97: iconst_0       
        //    98: invokevirtual   util/conpool/Connection.release:(Z)V
        //   101: iload_0        
        //   102: iconst_1       
        //   103: iadd           
        //   104: istore_0       
        //   105: goto            88
        //   108: aload_2        
        //   109: monitorexit    
        //   110: return         
        //   111: astore_3       
        //   112: aload_2        
        //   113: monitorexit    
        //   114: aload_3        
        //   115: athrow         
        //   116: iload_0        
        //   117: iconst_1       
        //   118: iadd           
        //   119: istore_0       
        //   120: goto            27
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  6      25     111    116    Any
        //  27     48     111    116    Any
        //  50     65     111    116    Any
        //  72     86     111    116    Any
        //  88     101    111    116    Any
        //  108    110    111    116    Any
        //  112    114    111    116    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private boolean isAlive() {
        try {
            this.socket.setSoTimeout(1);
            final int read = this.socketIn.read();
            if (read != -1) {
                final byte[] array = new byte[Math.max(this.socketIn.available(), 10240)];
                this.socketIn.read(array);
                final StringBuilder sb = new StringBuilder();
                sb.append((char)read);
                sb.append(new String(array));
                sb.toString();
            }
            return false;
        }
        catch (Exception ex) {
            return false;
        }
        catch (SocketTimeoutException ex2) {
            try {
                this.socket.setSoTimeout(0);
                return true;
            }
            catch (SocketException ex3) {
                return false;
            }
        }
    }
    
    private static String[] parseHosts(final String s) {
        if (s.startsWith("#") || s.trim().equals("")) {
            return null;
        }
        final StringTokenizer stringTokenizer = new StringTokenizer(s);
        if (stringTokenizer.countTokens() >= 2) {
            return new String[] { stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim() };
        }
        return new String[] { "127.0.0.1", stringTokenizer.nextToken().trim() };
    }
    
    private static String poolKey(final String s, final int n, final boolean b, final Proxy proxy) {
        if (b) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(":");
            sb.append(n);
            sb.append(":ssl:");
            sb.append(proxy.hashCode());
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(":");
        sb2.append(n);
        sb2.append(":plain:");
        sb2.append(proxy.hashCode());
        return sb2.toString();
    }
    
    public static Connection poolRemove(final String s) {
    Label_0032_Outer:
        while (true) {
            while (true) {
                Label_0149: {
                    synchronized (Connection.connPooled) {
                        final Vector<Connection> vector = Connection.connPooled.get(s);
                        Connection connection = null;
                        if (vector == null) {
                            return null;
                        }
                        break Label_0149;
                        Connection connection2 = null;
                        Label_0080: {
                            connection2.aquired = true;
                        }
                        Connection.toNotify.unregister(connection2);
                        final boolean alive = connection2.isAlive();
                        connection = connection2;
                        int n = alive ? 1 : 0;
                        // iftrue(Label_0032:, alive)
                        // iftrue(Label_0138:, !vector.isEmpty())
                        // iftrue(Label_0080:, !connection2.aquired)
                        // iftrue(Label_0122:, n != 0 || vector.isEmpty())
                        Block_7: {
                            break Block_7;
                            Label_0122:
                            Block_8: {
                                break Block_8;
                                while (true) {
                                    connection2 = vector.remove(vector.size() - 1);
                                    throw new IllegalStateException("Inconsistent Connection State - Cannot take already aquired connection from pool!");
                                    Label_0138:
                                    return connection;
                                    continue Label_0032_Outer;
                                }
                            }
                            final Throwable t;
                            Connection.connPooled.remove(t);
                            return connection;
                        }
                        connection2.release(false);
                        connection = null;
                        n = (alive ? 1 : 0);
                        continue;
                    }
                }
                int n = 0;
                continue;
            }
        }
    }
    
    public static void poolReuse(final Connection connection) {
        synchronized (Connection.connPooled) {
            if (!connection.aquired) {
                throw new IllegalStateException("Inconsistent Connection State - Cannot release non aquired connection");
            }
            connection.aquired = false;
            Vector<Connection> vector;
            if ((vector = Connection.connPooled.get(connection.poolKey)) == null) {
                vector = new Vector<Connection>();
                Connection.connPooled.put(connection.poolKey, vector);
            }
            Connection.toNotify.register(connection);
            connection.timeout.setTimeout(Connection.POOLTIMEOUT_SECONDS * 1000);
            vector.add(connection);
        }
    }
    
    public static void setCustomHostsFile(final String custom_HOSTS_FILE_NAME) {
        Connection.CUSTOM_HOSTS_FILE_NAME = custom_HOSTS_FILE_NAME;
    }
    
    public static void setPoolTimeoutSeconds(final int pooltimeout_SECONDS) {
        Connection.POOLTIMEOUT_SECONDS = pooltimeout_SECONDS;
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
        return new long[] { this.in.getTraffic(), this.out.getTraffic() };
    }
    
    public void release(final boolean b) {
        if (!this.valid) {
            return;
        }
        Connection.connAquired.remove(this);
        if (b) {
            this.in.invalidate();
            this.out.invalidate();
            try {
                this.socket.setSoTimeout(0);
                poolReuse(this);
                return;
            }
            catch (SocketException ex) {
                this.release(false);
                return;
            }
        }
        try {
            this.valid = false;
            if (!this.ssl) {
                this.socket.shutdownOutput();
                this.socket.shutdownInput();
            }
            this.socket.close();
        }
        catch (IOException ex2) {}
    }
    
    public void setSoTimeout(final int soTimeout) throws SocketException {
        this.socket.setSoTimeout(soTimeout);
    }
    
    @Override
    public void timeoutNotification() {
        synchronized (Connection.connPooled) {
            final Vector vector = Connection.connPooled.get(this.poolKey);
            if (vector == null) {
                return;
            }
            final boolean remove = vector.remove(this);
            if (vector.isEmpty()) {
                Connection.connPooled.remove(this.poolKey);
            }
            // monitorexit(Connection.connPooled)
            if (remove) {
                this.release(false);
            }
        }
    }
}
