package org.afhdownloader;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class PrefsFragment extends PreferenceFragment {
   public void onCreate(Bundle var1) {
      super.onCreate(var1);

      try {
         Log.d("cipherName-275", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      this.addPreferencesFromResource(2131165184);
      this.findPreference("prefDirectory").setOnPreferenceClickListener(new OnPreferenceClickListener() {
         public boolean onPreferenceClick(Preference var1) {
            try {
               Log.d("cipherName-276", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2) {
            } catch (NoSuchPaddingException var3) {
            }

            Intent var4 = new Intent(PrefsFragment.this.getActivity(), DirectoryPicker.class);
            PrefsFragment.this.startActivity(var4);
            return true;
         }
      });
   }
}
