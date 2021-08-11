// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.util.Arrays;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.List;

public interface Dns
{
    public static final Dns SYSTEM = new Dns() {
        @Override
        public List<InetAddress> lookup(final String s) throws UnknownHostException {
            if (s == null) {
                throw new UnknownHostException("hostname == null");
            }
            try {
                return Arrays.asList(InetAddress.getAllByName(s));
            }
            catch (NullPointerException ex2) {
                final UnknownHostException ex = new UnknownHostException("Broken system behaviour for dns lookup of " + s);
                ex.initCause(ex2);
                throw ex;
            }
        }
    };
    
    List<InetAddress> lookup(final String p0) throws UnknownHostException;
}
