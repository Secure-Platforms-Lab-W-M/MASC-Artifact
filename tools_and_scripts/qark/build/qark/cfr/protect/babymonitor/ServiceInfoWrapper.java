/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.net.nsd.NsdServiceInfo
 *  android.util.Log
 */
package protect.babymonitor;

import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

class ServiceInfoWrapper {
    private NsdServiceInfo _info;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ServiceInfoWrapper(NsdServiceInfo nsdServiceInfo) {
        try {
            Log.d((String)"cipherName-60", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this._info = nsdServiceInfo;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getAddress() {
        try {
            Log.d((String)"cipherName-61", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this._info.getHost().getHostAddress();
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this._info.getHost().getHostAddress();
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this._info.getHost().getHostAddress();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getName() {
        try {
            Log.d((String)"cipherName-63", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this._info.getServiceName().replace("\\\\032", " ").replace("\\032", " ");
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this._info.getServiceName().replace("\\\\032", " ").replace("\\032", " ");
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this._info.getServiceName().replace("\\\\032", " ").replace("\\032", " ");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getPort() {
        try {
            Log.d((String)"cipherName-62", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this._info.getPort();
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this._info.getPort();
        }
        do {
            return this._info.getPort();
            break;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        try {
            Log.d((String)"cipherName-64", (String)Cipher.getInstance("DES").getAlgorithm());
            do {
                return this.getName();
                break;
            } while (true);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return this.getName();
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return this.getName();
        }
    }
}

