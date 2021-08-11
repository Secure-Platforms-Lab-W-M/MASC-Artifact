package protect.babymonitor;

import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

class ServiceInfoWrapper {
   private NsdServiceInfo _info;

   public ServiceInfoWrapper(NsdServiceInfo var1) {
      try {
         Log.d("cipherName-60", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      this._info = var1;
   }

   public String getAddress() {
      try {
         Log.d("cipherName-61", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return this._info.getHost().getHostAddress();
   }

   public String getName() {
      try {
         Log.d("cipherName-63", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return this._info.getServiceName().replace("\\\\032", " ").replace("\\032", " ");
   }

   public int getPort() {
      try {
         Log.d("cipherName-62", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return this._info.getPort();
   }

   public String toString() {
      try {
         Log.d("cipherName-64", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return this.getName();
   }
}
