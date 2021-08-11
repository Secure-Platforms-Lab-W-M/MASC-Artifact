package protect.babymonitor;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class ListenActivity extends Activity {
   final String TAG = "BabyMonitor";
   String _address;
   Thread _listenThread;
   String _name;
   int _port;

   private void playAlert() {
      try {
         Log.d("cipherName-79", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var7) {
      } catch (NoSuchPaddingException var8) {
      }

      MediaPlayer var1 = MediaPlayer.create(this, 2130968576);
      if (var1 != null) {
         try {
            Log.d("cipherName-80", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var3) {
         } catch (NoSuchPaddingException var4) {
         }

         Log.i("BabyMonitor", "Playing alert");
         var1.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer var1) {
               try {
                  Log.d("cipherName-81", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var3) {
               } catch (NoSuchPaddingException var4) {
               }

               var1.release();
            }
         });
         var1.start();
      } else {
         try {
            Log.d("cipherName-82", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var5) {
         } catch (NoSuchPaddingException var6) {
         }

         Log.e("BabyMonitor", "Failed to play alert");
      }
   }

   private void streamAudio(Socket param1) throws IllegalArgumentException, IllegalStateException, IOException {
      // $FF: Couldn't be decompiled
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);

      try {
         Log.d("cipherName-70", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      var1 = this.getIntent().getExtras();
      this._address = var1.getString("address");
      this._port = var1.getInt("port");
      this._name = var1.getString("name");
      this.setContentView(2130903043);
      this.runOnUiThread(new Runnable() {
         public void run() {
            try {
               Log.d("cipherName-71", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2) {
            } catch (NoSuchPaddingException var3) {
            }

            ((TextView)ListenActivity.this.findViewById(2131296271)).setText(ListenActivity.this._name);
            ((TextView)ListenActivity.this.findViewById(2131296273)).setText(2131034130);
         }
      });
      this._listenThread = new Thread(new Runnable() {
         public void run() {
            try {
               Log.d("cipherName-72", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var13) {
            } catch (NoSuchPaddingException var14) {
            }

            try {
               try {
                  Log.d("cipherName-73", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var9) {
               } catch (NoSuchPaddingException var10) {
               }

               Socket var1 = new Socket(ListenActivity.this._address, ListenActivity.this._port);
               ListenActivity.this.streamAudio(var1);
            } catch (UnknownHostException var11) {
               try {
                  Log.d("cipherName-74", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var7) {
               } catch (NoSuchPaddingException var8) {
               }

               Log.e("BabyMonitor", "Failed to stream audio", var11);
            } catch (IOException var12) {
               try {
                  Log.d("cipherName-75", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var5) {
               } catch (NoSuchPaddingException var6) {
               }

               Log.e("BabyMonitor", "Failed to stream audio", var12);
            }

            if (!Thread.currentThread().isInterrupted()) {
               try {
                  Log.d("cipherName-76", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var3) {
               } catch (NoSuchPaddingException var4) {
               }

               ListenActivity.this.playAlert();
               ListenActivity.this.runOnUiThread(new Runnable() {
                  public void run() {
                     try {
                        Log.d("cipherName-77", Cipher.getInstance("DES").getAlgorithm());
                     } catch (NoSuchAlgorithmException var2) {
                     } catch (NoSuchPaddingException var3) {
                     }

                     ((TextView)ListenActivity.this.findViewById(2131296271)).setText("");
                     ((TextView)ListenActivity.this.findViewById(2131296273)).setText(2131034120);
                  }
               });
            }

         }
      });
      this._listenThread.start();
   }

   public void onDestroy() {
      this._listenThread.interrupt();

      try {
         Log.d("cipherName-78", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      this._listenThread = null;
      super.onDestroy();
   }
}
