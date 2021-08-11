package protect.babymonitor;

import android.app.Activity;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class DiscoverActivity extends Activity {
   final String TAG = "BabyMonitor";
   DiscoveryListener _discoveryListener;
   NsdManager _nsdManager;

   private void connectToChild(String var1, int var2, String var3) {
      try {
         Log.d("cipherName-59", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var6) {
      } catch (NoSuchPaddingException var7) {
      }

      Intent var4 = new Intent(this.getApplicationContext(), ListenActivity.class);
      Bundle var5 = new Bundle();
      var5.putString("address", var1);
      var5.putInt("port", var2);
      var5.putString("name", var3);
      var4.putExtras(var5);
      this.startActivity(var4);
   }

   private void loadDiscoveryViaAddress() {
      try {
         Log.d("cipherName-38", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      this.setContentView(2130903041);
      ((Button)this.findViewById(2131296267)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            try {
               Log.d("cipherName-39", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var12) {
            } catch (NoSuchPaddingException var13) {
            }

            Log.i("BabyMonitor", "Connecting to child device via address");
            EditText var14 = (EditText)DiscoverActivity.this.findViewById(2131296264);
            EditText var3 = (EditText)DiscoverActivity.this.findViewById(2131296265);
            String var15 = var14.getText().toString();
            String var16 = var3.getText().toString();
            if (var15.length() == 0) {
               try {
                  Log.d("cipherName-40", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var7) {
               } catch (NoSuchPaddingException var8) {
               }

               Toast.makeText(DiscoverActivity.this, 2131034128, 1).show();
            } else {
               int var2;
               try {
                  try {
                     Log.d("cipherName-41", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var9) {
                  } catch (NoSuchPaddingException var10) {
                  }

                  var2 = Integer.parseInt(var16);
               } catch (NumberFormatException var11) {
                  try {
                     Log.d("cipherName-42", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var5) {
                  } catch (NoSuchPaddingException var6) {
                  }

                  Toast.makeText(DiscoverActivity.this, 2131034129, 1).show();
                  return;
               }

               DiscoverActivity.this.connectToChild(var15, var2, var15);
            }
         }
      });
   }

   private void loadDiscoveryViaMdns() {
      try {
         Log.d("cipherName-37", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      this.setContentView(2130903042);
      this.startServiceDiscovery("_babymonitor._tcp.");
   }

   protected void onCreate(Bundle var1) {
      Log.i("BabyMonitor", "Baby monitor start");

      try {
         Log.d("cipherName-34", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      this._nsdManager = (NsdManager)this.getSystemService("servicediscovery");
      super.onCreate(var1);
      this.setContentView(2130903040);
      ((Button)this.findViewById(2131296258)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            try {
               Log.d("cipherName-35", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2) {
            } catch (NoSuchPaddingException var3) {
            }

            DiscoverActivity.this.loadDiscoveryViaMdns();
         }
      });
      ((Button)this.findViewById(2131296260)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            try {
               Log.d("cipherName-36", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2) {
            } catch (NoSuchPaddingException var3) {
            }

            DiscoverActivity.this.loadDiscoveryViaAddress();
         }
      });
   }

   protected void onDestroy() {
      Log.i("BabyMonitor", "Baby monitoring stop");

      try {
         Log.d("cipherName-43", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var4) {
      } catch (NoSuchPaddingException var5) {
      }

      if (this._discoveryListener != null) {
         try {
            Log.d("cipherName-44", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var2) {
         } catch (NoSuchPaddingException var3) {
         }

         Log.i("BabyMonitor", "Unregistering monitoring service");
         this._nsdManager.stopServiceDiscovery(this._discoveryListener);
         this._discoveryListener = null;
      }

      super.onDestroy();
   }

   public void startServiceDiscovery(final String var1) {
      try {
         Log.d("cipherName-45", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var5) {
      } catch (NoSuchPaddingException var6) {
      }

      final NsdManager var2 = (NsdManager)this.getSystemService("servicediscovery");
      ListView var3 = (ListView)this.findViewById(2131296269);
      final ArrayAdapter var4 = new ArrayAdapter(this, 2130903046);
      var3.setAdapter(var4);
      var3.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            try {
               Log.d("cipherName-46", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var6) {
            } catch (NoSuchPaddingException var7) {
            }

            ServiceInfoWrapper var8 = (ServiceInfoWrapper)var1.getItemAtPosition(var3);
            DiscoverActivity.this.connectToChild(var8.getAddress(), var8.getPort(), var8.getName());
         }
      });
      this._discoveryListener = new DiscoveryListener() {
         public void onDiscoveryStarted(String var1x) {
            try {
               Log.d("cipherName-47", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2x) {
            } catch (NoSuchPaddingException var3) {
            }

            Log.d("BabyMonitor", "Service discovery started");
         }

         public void onDiscoveryStopped(String var1x) {
            try {
               Log.d("cipherName-56", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4x) {
            }

            Log.i("BabyMonitor", "Discovery stopped: " + var1x);
         }

         public void onServiceFound(NsdServiceInfo var1x) {
            try {
               Log.d("cipherName-48", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var9) {
            } catch (NoSuchPaddingException var10) {
            }

            Log.d("BabyMonitor", "Service discovery success: " + var1x);
            if (!var1x.getServiceType().equals(var1)) {
               try {
                  Log.d("cipherName-49", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var3) {
               } catch (NoSuchPaddingException var4x) {
               }

               Log.d("BabyMonitor", "Unknown Service Type: " + var1x.getServiceType());
            } else if (var1x.getServiceName().contains("ProtectBabyMonitor")) {
               try {
                  Log.d("cipherName-50", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var5) {
               } catch (NoSuchPaddingException var6) {
               }

               ResolveListener var2x = new ResolveListener() {
                  public void onResolveFailed(NsdServiceInfo var1x, int var2x) {
                     try {
                        Log.d("cipherName-51", Cipher.getInstance("DES").getAlgorithm());
                     } catch (NoSuchAlgorithmException var4x) {
                     } catch (NoSuchPaddingException var5) {
                     }

                     Log.e("BabyMonitor", "Resolve failed: error " + var2x + " for service: " + var1x);
                  }

                  public void onServiceResolved(final NsdServiceInfo var1x) {
                     try {
                        Log.d("cipherName-52", Cipher.getInstance("DES").getAlgorithm());
                     } catch (NoSuchAlgorithmException var3) {
                     } catch (NoSuchPaddingException var4x) {
                     }

                     Log.i("BabyMonitor", "Resolve Succeeded: " + var1x);
                     DiscoverActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                           try {
                              Log.d("cipherName-53", Cipher.getInstance("DES").getAlgorithm());
                           } catch (NoSuchAlgorithmException var2x) {
                           } catch (NoSuchPaddingException var3) {
                           }

                           var4.add(new ServiceInfoWrapper(var1x));
                        }
                     });
                  }
               };
               DiscoverActivity.this._nsdManager.resolveService(var1x, var2x);
            } else {
               try {
                  Log.d("cipherName-54", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var7) {
               } catch (NoSuchPaddingException var8) {
               }

               Log.d("BabyMonitor", "Unknown Service name: " + var1x.getServiceName());
            }
         }

         public void onServiceLost(NsdServiceInfo var1x) {
            try {
               Log.d("cipherName-55", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4x) {
            }

            Log.e("BabyMonitor", "Service lost: " + var1x);
         }

         public void onStartDiscoveryFailed(String var1x, int var2x) {
            try {
               Log.d("cipherName-57", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4x) {
            }

            Log.e("BabyMonitor", "Discovery failed: Error code: " + var2x);
            var2.stopServiceDiscovery(this);
         }

         public void onStopDiscoveryFailed(String var1x, int var2x) {
            try {
               Log.d("cipherName-58", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4x) {
            }

            Log.e("BabyMonitor", "Discovery failed: Error code: " + var2x);
            var2.stopServiceDiscovery(this);
         }
      };
      var2.discoverServices(var1, 1, this._discoveryListener);
   }
}
