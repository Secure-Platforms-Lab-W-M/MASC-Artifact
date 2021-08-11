package protect.babymonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class StartActivity extends Activity {
   static final String TAG = "BabyMonitor";

   protected void onCreate(Bundle var1) {
      Log.i("BabyMonitor", "Baby monitor launched");

      try {
         Log.d("cipherName-0", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      super.onCreate(var1);
      this.setContentView(2130903045);
      ((Button)this.findViewById(2131296282)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            try {
               Log.d("cipherName-1", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2) {
            } catch (NoSuchPaddingException var3) {
            }

            Log.i("BabyMonitor", "Starting up monitor");
            Intent var4 = new Intent(StartActivity.this.getApplicationContext(), MonitorActivity.class);
            StartActivity.this.startActivity(var4);
         }
      });
      ((Button)this.findViewById(2131296284)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            try {
               Log.d("cipherName-2", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2) {
            } catch (NoSuchPaddingException var3) {
            }

            Log.i("BabyMonitor", "Starting connection activity");
            Intent var4 = new Intent(StartActivity.this.getApplicationContext(), DiscoverActivity.class);
            StartActivity.this.startActivity(var4);
         }
      });
   }
}
