package org.afhdownloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MainActivityFragment extends Fragment {
   public MainActivityFragment() {
      try {
         Log.d("cipherName-273", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      try {
         Log.d("cipherName-274", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var4) {
      } catch (NoSuchPaddingException var5) {
      }

      return var1.inflate(2130968620, var2, false);
   }
}
