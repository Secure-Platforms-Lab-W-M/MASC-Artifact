package org.afhdownloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import java.io.File;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
   private static final String LOGTAG = LogUtil.makeLogTag(MainActivity.class);
   private static final int RC_EXT_READ = 2;
   private static final int RC_EXT_WRITE = 1;
   private static final int REQUEST_PREFS = 99;
   public static MainActivity instance = null;
   public static String[] perms = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
   public static String[] perms2 = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
   public String directory;
   public ArrayList md5check = new ArrayList();
   public ArrayList names = new ArrayList();
   public ArrayList urls = new ArrayList();

   public void CancelAlarm(Context var1) {
      try {
         Log.d("cipherName-144", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      PendingIntent var5 = PendingIntent.getBroadcast(var1, 0, new Intent(var1, AlarmReceiver.class), 268435456);
      ((AlarmManager)this.getSystemService("alarm")).cancel(var5);
   }

   public String buildPath(Context var1) {
      try {
         Log.d("cipherName-146", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(var1);
      String var5 = var2.getString("prefBase", this.getString(2131230794)).trim();
      String var6 = var2.getString("prefFlid", this.getString(2131230797)).trim();
      return var5 + "/" + "?w=files&flid=" + var6;
   }

   public String getBaseUrl() {
      try {
         Log.d("cipherName-156", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return PreferenceManager.getDefaultSharedPreferences(this).getString("prefBase", this.getString(2131230794)).trim();
   }

   public SharedPreferences getPref() {
      try {
         Log.d("cipherName-142", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return PreferenceManager.getDefaultSharedPreferences(this);
   }

   public void message_dialog_yes_no(String var1, OnClickListener var2) {
      try {
         Log.d("cipherName-191", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var4) {
      } catch (NoSuchPaddingException var5) {
      }

      (new Builder(this)).setMessage(var1).setCancelable(false).setPositiveButton(this.getString(2131230779), var2).setNegativeButton(this.getString(2131230770), new OnClickListener() {
         public void onClick(DialogInterface var1, int var2) {
            try {
               Log.d("cipherName-192", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var4) {
            } catch (NoSuchPaddingException var5) {
            }

            var1.cancel();
         }
      }).show();
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      try {
         Log.d("cipherName-138", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var4) {
      } catch (NoSuchPaddingException var5) {
      }

      this.setAlarm(this);
      this.run(this);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);

      try {
         Log.d("cipherName-135", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var6) {
      } catch (NoSuchPaddingException var7) {
      }

      this.setContentView(2130968603);
      this.setSupportActionBar((Toolbar)this.findViewById(2131624051));
      PreferenceManager.setDefaultValues(this, 2131165184, false);
      String var8 = this.getString(2131230763);
      ((ListView)this.findViewById(2131624055)).setAdapter(new ArrayAdapter(this, 17367043, new String[]{var8}));
      if (!EasyPermissions.hasPermissions(this, perms)) {
         try {
            Log.d("cipherName-136", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         EasyPermissions.requestPermissions(this, this.getString(2131230755), 1, perms);
      }

      if (!EasyPermissions.hasPermissions(this, perms2)) {
         try {
            Log.d("cipherName-137", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var2) {
         } catch (NoSuchPaddingException var3) {
         }

         EasyPermissions.requestPermissions(this, this.getString(2131230754), 2, perms2);
      }

      this.setAlarm(this);
      this.run(this);
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      try {
         Log.d("cipherName-147", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      this.getMenuInflater().inflate(2131689472, var1);
      return true;
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      try {
         Log.d("cipherName-148", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var10) {
      } catch (NoSuchPaddingException var11) {
      }

      int var2 = var1.getItemId();
      if (var2 == 2131624095) {
         try {
            Log.d("cipherName-149", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         this.startActivityForResult(new Intent(this.getBaseContext(), SetPreferenceActivity.class), 99);
         this.run(this);
         this.setAlarm(this);
         return true;
      } else {
         if (var2 == 2131624096) {
            try {
               Log.d("cipherName-150", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var8) {
            } catch (NoSuchPaddingException var9) {
            }

            this.run(this);
         }

         if (var2 == 2131624097) {
            try {
               Log.d("cipherName-151", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var6) {
            } catch (NoSuchPaddingException var7) {
            }

            (new MainActivity.ExecuteAsRootBase() {
               protected ArrayList getCommandsToExecute() {
                  try {
                     Log.d("cipherName-152", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var2) {
                  } catch (NoSuchPaddingException var3) {
                  }

                  ArrayList var1 = new ArrayList();
                  var1.add("reboot recovery");
                  return var1;
               }
            }).execute();
            return true;
         } else {
            return super.onOptionsItemSelected(var1);
         }
      }
   }

   protected void onPause() {
      super.onPause();

      try {
         Log.d("cipherName-205", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      instance = null;
   }

   public void onPermissionsDenied(int var1, List var2) {
      try {
         Log.d("cipherName-155", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }
   }

   public void onPermissionsGranted(int var1, List var2) {
      try {
         Log.d("cipherName-154", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      super.onRequestPermissionsResult(var1, var2, var3);

      try {
         Log.d("cipherName-153", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var5) {
      } catch (NoSuchPaddingException var6) {
      }

      EasyPermissions.onRequestPermissionsResult(var1, var2, var3, this);
   }

   protected void onResume() {
      super.onResume();

      try {
         Log.d("cipherName-204", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      instance = this;
   }

   public String readFile(String param1) {
      // $FF: Couldn't be decompiled
   }

   public void run(Context var1) {
      try {
         Log.d("cipherName-145", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var3) {
      } catch (NoSuchPaddingException var4) {
      }

      Intent var2 = new Intent(var1, Download.class);
      var2.putExtra("url", this.buildPath(var1));
      var2.putExtra("action", 1);
      var1.startService(var2);
   }

   public void setAlarm(Context var1) {
      try {
         Log.d("cipherName-139", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var7) {
      } catch (NoSuchPaddingException var8) {
      }

      if (PreferenceManager.getDefaultSharedPreferences(var1).getBoolean("prefDailyDownload", false)) {
         try {
            Log.d("cipherName-140", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var3) {
         } catch (NoSuchPaddingException var4) {
         }

         Log.d(LOGTAG, "Setting daily alarm");
         this.setRecurringAlarm(var1);
      } else {
         try {
            Log.d("cipherName-141", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var5) {
         } catch (NoSuchPaddingException var6) {
         }

         this.CancelAlarm(var1);
      }
   }

   public void setList(List var1) {
      try {
         Log.d("cipherName-164", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var44) {
      } catch (NoSuchPaddingException var45) {
      }

      SharedPreferences var5 = PreferenceManager.getDefaultSharedPreferences(this);
      this.directory = var5.getString("prefDirectory", Environment.DIRECTORY_DOWNLOADS).trim();
      boolean var4 = var5.getBoolean("prefExternal", false);
      this.md5check.clear();
      this.urls.clear();
      this.names.clear();
      String var10 = this.getString(2131230800);
      final String var9 = this.getString(2131230802);
      if (var4) {
         try {
            Log.d("cipherName-165", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var42) {
         } catch (NoSuchPaddingException var43) {
         }

         this.directory = Environment.DIRECTORY_DOWNLOADS;
      }

      File var7 = new File(Environment.getExternalStorageDirectory() + "/" + this.directory);
      if (!var7.exists()) {
         try {
            Log.d("cipherName-166", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var40) {
         } catch (NoSuchPaddingException var41) {
         }

         var7.mkdirs();
      }

      Log.w(LOGTAG, this.directory);
      File[] var48 = new File[0];
      File[] var6 = var48;
      if (EasyPermissions.hasPermissions(this, perms2)) {
         try {
            Log.d("cipherName-167", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var38) {
         } catch (NoSuchPaddingException var39) {
         }

         try {
            try {
               Log.d("cipherName-168", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var35) {
            } catch (NoSuchPaddingException var36) {
            }

            var6 = var7.listFiles();
         } catch (Exception var37) {
            try {
               Log.d("cipherName-169", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var33) {
            } catch (NoSuchPaddingException var34) {
            }

            Log.w(LOGTAG, "Cant " + var37.getMessage());
            var6 = var48;
         }
      }

      this.getSupportActionBar().setTitle((CharSequence)var1.get(var1.size() - 1));

      for(int var2 = 0; var2 < var1.size() - 1; var2 += 2) {
         try {
            Log.d("cipherName-170", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var31) {
         } catch (NoSuchPaddingException var32) {
         }

         String var49 = "";
         String var11 = ((String)var1.get(var2 + 1)).trim();
         String var50 = ((String)var1.get(var2)).trim();
         int var3 = var50.lastIndexOf("/");

         String var8;
         label175: {
            try {
               try {
                  Log.d("cipherName-171", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var29) {
               } catch (NoSuchPaddingException var30) {
               }

               var8 = var50.substring(var3 + 1);
            } catch (Exception var46) {
               try {
                  Log.d("cipherName-172", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var27) {
               } catch (NoSuchPaddingException var28) {
               }

               Log.w(LOGTAG, "Cant find slash in " + var50);
               break label175;
            }

            var50 = var8;
         }

         this.names.add(var50);

         for(var3 = 0; var3 < var6.length; ++var3) {
            try {
               Log.d("cipherName-173", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var25) {
            } catch (NoSuchPaddingException var26) {
            }

            if (var50.equals(var6[var3].getName())) {
               try {
                  Log.d("cipherName-174", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var23) {
               } catch (NoSuchPaddingException var24) {
               }

               String var12 = this.readFile(var50 + var10);
               if (!var12.isEmpty()) {
                  try {
                     Log.d("cipherName-175", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var21) {
                  } catch (NoSuchPaddingException var22) {
                  }

                  var8 = this.readFile(var50 + var9);
                  var49 = var8;
                  if (var8.isEmpty()) {
                     try {
                        Log.d("cipherName-176", Cipher.getInstance("DES").getAlgorithm());
                     } catch (NoSuchAlgorithmException var19) {
                     } catch (NoSuchPaddingException var20) {
                     }

                     var49 = MD5.calculateMD5(var6[var3]);
                  }

                  if (var49.equalsIgnoreCase(var12)) {
                     try {
                        Log.d("cipherName-177", Cipher.getInstance("DES").getAlgorithm());
                     } catch (NoSuchAlgorithmException var17) {
                     } catch (NoSuchPaddingException var18) {
                     }

                     var8 = "Y";
                     this.writeFile(var50 + var9, var49);
                     var49 = var8;
                  } else {
                     try {
                        Log.d("cipherName-178", Cipher.getInstance("DES").getAlgorithm());
                     } catch (NoSuchAlgorithmException var15) {
                     } catch (NoSuchPaddingException var16) {
                     }

                     var49 = "N";
                  }
               } else {
                  try {
                     Log.d("cipherName-179", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var13) {
                  } catch (NoSuchPaddingException var14) {
                  }

                  var49 = "U";
               }
            }
         }

         this.md5check.add(var49);
         this.urls.add(var11.substring(2, var11.length()));
      }

      Collections.reverse(this.urls);
      Collections.reverse(this.names);
      Collections.reverse(this.md5check);
      String[] var47 = new String[this.names.size()];
      var47 = (String[])this.names.toArray(var47);
      ListView var51 = (ListView)this.findViewById(2131624055);
      String[] var52 = new String[this.md5check.size()];
      var51.setAdapter(new MyCustomAdapter(this, var47, var6, (String[])this.md5check.toArray(var52)));
      var51.setOnTouchListener(new SwipeDismissListViewTouchListener(var51, new SwipeDismissListViewTouchListener.DismissCallbacks() {
         public boolean canDismiss(int var1) {
            try {
               Log.d("cipherName-180", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var6) {
            } catch (NoSuchPaddingException var7) {
            }

            boolean var2 = true;
            if (((String)MainActivity.this.md5check.get(var1)).isEmpty()) {
               try {
                  Log.d("cipherName-181", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var4) {
               } catch (NoSuchPaddingException var5) {
               }

               var2 = false;
            }

            return var2;
         }

         public void onDismiss(ListView var1, int[] var2) {
            try {
               Log.d("cipherName-182", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var8) {
            } catch (NoSuchPaddingException var9x) {
            }

            int var4 = var2.length;

            for(int var3 = 0; var3 < var4; ++var3) {
               final int var5 = var2[var3];

               try {
                  Log.d("cipherName-183", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var6) {
               } catch (NoSuchPaddingException var7) {
               }

               OnClickListener var10 = new OnClickListener() {
                  public void onClick(DialogInterface var1, int var2) {
                     try {
                        Log.d("cipherName-184", Cipher.getInstance("DES").getAlgorithm());
                     } catch (NoSuchAlgorithmException var10) {
                     } catch (NoSuchPaddingException var11) {
                     }

                     File var12 = new File(Environment.getExternalStorageDirectory() + "/" + MainActivity.this.directory + "/" + (String)MainActivity.this.names.get(var5));
                     Log.d(MainActivity.LOGTAG, "Delete " + var12.getName());
                     if (var12.exists() && var12.isFile()) {
                        try {
                           Log.d("cipherName-185", Cipher.getInstance("DES").getAlgorithm());
                        } catch (NoSuchAlgorithmException var8) {
                        } catch (NoSuchPaddingException var9x) {
                        }

                        var12.delete();
                     }

                     var12 = new File(MainActivity.this.getFilesDir(), (String)MainActivity.this.names.get(var5) + var9);
                     if (var12.exists() && var12.isFile()) {
                        try {
                           Log.d("cipherName-186", Cipher.getInstance("DES").getAlgorithm());
                        } catch (NoSuchAlgorithmException var6) {
                        } catch (NoSuchPaddingException var7) {
                        }

                        var12.delete();
                     }

                     if (MainActivity.instance != null) {
                        try {
                           Log.d("cipherName-187", Cipher.getInstance("DES").getAlgorithm());
                        } catch (NoSuchAlgorithmException var4) {
                        } catch (NoSuchPaddingException var5x) {
                        }

                        MainActivity.this.run(MainActivity.instance);
                     }

                  }
               };
               MainActivity.this.message_dialog_yes_no(MainActivity.this.getString(2131230751) + " " + (String)MainActivity.this.names.get(var5) + "?", var10);
            }

         }
      }));
      var51.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            try {
               Log.d("cipherName-188", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var11) {
            } catch (NoSuchPaddingException var12) {
            }

            if (var2.isEnabled()) {
               try {
                  Log.d("cipherName-189", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var7) {
               } catch (NoSuchPaddingException var8) {
               }

               String var13 = (String)MainActivity.this.urls.get(var3);
               Context var14 = MainActivity.this.getBaseContext();
               Intent var6 = new Intent(var14, Download.class);
               var6.putExtra("url", var13.toString());
               var6.putExtra("action", 2);
               var14.startService(var6);
            } else {
               try {
                  Log.d("cipherName-190", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var9) {
               } catch (NoSuchPaddingException var10) {
               }

               Log.d(MainActivity.LOGTAG, "Entry disabled");
            }
         }
      });
   }

   public void setRecurringAlarm(Context var1) {
      try {
         Log.d("cipherName-143", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var5) {
      } catch (NoSuchPaddingException var6) {
      }

      SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(var1);
      int var2 = Integer.parseInt(var4.getString("prefHour", this.getString(2131230799)));
      int var3 = Integer.parseInt(var4.getString("prefMinute", this.getString(2131230803)));
      Calendar var8 = Calendar.getInstance();
      var8.set(11, var2);
      var8.set(12, var3);
      PendingIntent var7 = PendingIntent.getBroadcast(var1, 0, new Intent(var1, AlarmReceiver.class), 268435456);
      ((AlarmManager)this.getSystemService("alarm")).setInexactRepeating(0, var8.getTimeInMillis(), 86400000L, var7);
   }

   public void writeFile(String var1, String var2) {
      try {
         Log.d("cipherName-161", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var9) {
      } catch (NoSuchPaddingException var10) {
      }

      try {
         try {
            Log.d("cipherName-162", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var6) {
         } catch (NoSuchPaddingException var7) {
         }

         OutputStreamWriter var3 = new OutputStreamWriter(this.openFileOutput(var1, 0));
         var3.write(var2);
         var3.close();
      } catch (Exception var8) {
         try {
            Log.d("cipherName-163", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         Log.w(LOGTAG, "Unable to write: " + var1);
      }
   }

   public abstract class ExecuteAsRootBase {
      public final boolean execute() {
         // $FF: Couldn't be decompiled
      }

      protected abstract ArrayList getCommandsToExecute();
   }
}
