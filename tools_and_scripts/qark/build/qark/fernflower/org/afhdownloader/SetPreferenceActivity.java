package org.afhdownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class SetPreferenceActivity extends AppCompatActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);

      try {
         Log.d("cipherName-222", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      this.setContentView(2130968603);
      this.setSupportActionBar((Toolbar)this.findViewById(2131624051));
      this.getFragmentManager().beginTransaction().replace(2131624052, new PrefsFragment()).commit();
   }
}
