package com.example.variantroute;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {
   static String cipherName = "AES/CBC/PKCS5Padding";
   static String dataLeAk0 = "0";
   // $FF: renamed from: i int
   int field_5 = 0;

   public void buttonClicked(View var1) {
      Log.d("amit", "button clicked");
      TextView var3 = (TextView)this.findViewById(2131165296);
      StringBuilder var2 = new StringBuilder();
      var2.append("Counter: ");
      var2.append(this.field_5);
      String var4 = var2.toString();
      ++this.field_5;
      var3.setText(var4);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      cipherName = "AES";
      this.setContentView(2131296283);
      this.getSupportFragmentManager().beginTransaction().add(2131165223, new MainActivity.SimpleFragment()).commit();
   }

   public static class SimpleFragment extends Fragment {
      public SimpleFragment() {
         Log.d("cipher", MainActivity.cipherName);

         try {
            Cipher.getInstance(MainActivity.cipherName);
         } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
         } catch (NoSuchPaddingException var3) {
            var3.printStackTrace();
            return;
         }

      }

      public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
         return var1.inflate(2131296284, var2, false);
      }
   }
}
