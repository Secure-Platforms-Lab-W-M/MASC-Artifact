// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package protect.babymonitor;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.net.nsd.NsdServiceInfo;

class ServiceInfoWrapper
{
    private NsdServiceInfo _info;
    
    public ServiceInfoWrapper(final NsdServiceInfo info) {
        while (true) {
            try {
                Log.d("cipherName-60", Cipher.getInstance("DES").getAlgorithm());
                this._info = info;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public String getAddress() {
        try {
            Log.d("cipherName-61", Cipher.getInstance("DES").getAlgorithm());
            return this._info.getHost().getHostAddress();
        }
        catch (NoSuchAlgorithmException ex) {
            return this._info.getHost().getHostAddress();
        }
        catch (NoSuchPaddingException ex2) {
            return this._info.getHost().getHostAddress();
        }
    }
    
    public String getName() {
        try {
            Log.d("cipherName-63", Cipher.getInstance("DES").getAlgorithm());
            return this._info.getServiceName().replace("\\\\032", " ").replace("\\032", " ");
        }
        catch (NoSuchAlgorithmException ex) {
            return this._info.getServiceName().replace("\\\\032", " ").replace("\\032", " ");
        }
        catch (NoSuchPaddingException ex2) {
            return this._info.getServiceName().replace("\\\\032", " ").replace("\\032", " ");
        }
    }
    
    public int getPort() {
        try {
            Log.d("cipherName-62", Cipher.getInstance("DES").getAlgorithm());
            return this._info.getPort();
        }
        catch (NoSuchAlgorithmException ex) {
            return this._info.getPort();
        }
        catch (NoSuchPaddingException ex2) {
            return this._info.getPort();
        }
    }
    
    @Override
    public String toString() {
        try {
            Log.d("cipherName-64", Cipher.getInstance("DES").getAlgorithm());
            return this.getName();
        }
        catch (NoSuchAlgorithmException ex) {
            return this.getName();
        }
        catch (NoSuchPaddingException ex2) {
            return this.getName();
        }
    }
}
