package org.afhdownloader;

import android.app.DownloadManager;
import android.app.Service;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class Download extends Service {
   private static final String LOGTAG = LogUtil.makeLogTag(Download.class);
   boolean mAllowRebind;
   IBinder mBinder;
   int mStartMode;

   public void download(String var1, String var2, String var3, String var4) {
      try {
         Log.d("cipherName-257", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var43) {
      } catch (NoSuchPaddingException var44) {
      }

      SharedPreferences var11 = PreferenceManager.getDefaultSharedPreferences(this);
      if (!var1.endsWith("/")) {
         try {
            Log.d("cipherName-258", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var39) {
         } catch (NoSuchPaddingException var40) {
         }

         Log.d(LOGTAG, "Downloading: " + var1);
         boolean var8 = var11.getBoolean("prefExternal", false);
         String var10 = var11.getString("prefDirectory", Environment.DIRECTORY_DOWNLOADS).trim();
         String var9 = var10;
         if (!var10.startsWith("/")) {
            try {
               Log.d("cipherName-259", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var37) {
            } catch (NoSuchPaddingException var38) {
            }

            var9 = "/" + var10;
         }

         File var47 = new File(Environment.getExternalStorageDirectory() + var9);
         if (!var47.exists()) {
            try {
               Log.d("cipherName-260", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var35) {
            } catch (NoSuchPaddingException var36) {
            }

            var47.mkdirs();
         }

         boolean var7 = false;
         boolean var5 = false;
         if (EasyPermissions.hasPermissions(this, MainActivity.perms2)) {
            try {
               Log.d("cipherName-261", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var33) {
            } catch (NoSuchPaddingException var34) {
            }

            File[] var48 = (new File(var47.getAbsolutePath())).listFiles();
            int var6 = 0;

            while(true) {
               var7 = var5;
               if (var6 >= var48.length) {
                  break;
               }

               try {
                  Log.d("cipherName-262", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var31) {
               } catch (NoSuchPaddingException var32) {
               }

               if (var4.equals(var48[var6].getName())) {
                  try {
                     Log.d("cipherName-263", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var29) {
                  } catch (NoSuchPaddingException var30) {
                  }

                  var5 = true;
               }

               ++var6;
            }
         }

         if (!var7) {
            try {
               Log.d("cipherName-264", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var25) {
            } catch (NoSuchPaddingException var26) {
            }

            if (var8) {
               try {
                  Log.d("cipherName-265", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var13) {
               } catch (NoSuchPaddingException var14) {
               }

               Intent var46 = new Intent("android.intent.action.VIEW", Uri.parse(var1));
               var46.setFlags(268435456);
               this.startActivity(var46);
            } else if (EasyPermissions.hasPermissions(this, MainActivity.perms)) {
               try {
                  Log.d("cipherName-266", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var21) {
               } catch (NoSuchPaddingException var22) {
               }

               Request var45 = new Request(Uri.parse(var1));
               var45.setDescription(var2);
               var45.setTitle(var3);
               if (VERSION.SDK_INT >= 11) {
                  try {
                     Log.d("cipherName-267", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var19) {
                  } catch (NoSuchPaddingException var20) {
                  }

                  var45.allowScanningByMediaScanner();
                  var45.setNotificationVisibility(1);
               }

               if (var11.getBoolean("prefWIFI", true)) {
                  try {
                     Log.d("cipherName-268", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var17) {
                  } catch (NoSuchPaddingException var18) {
                  }

                  var45.setAllowedNetworkTypes(2);
               } else {
                  try {
                     Log.d("cipherName-269", Cipher.getInstance("DES").getAlgorithm());
                  } catch (NoSuchAlgorithmException var15) {
                  } catch (NoSuchPaddingException var16) {
                  }

                  var45.setAllowedNetworkTypes(3);
               }

               var45.setAllowedOverRoaming(false);
               var45.setDestinationInExternalPublicDir(var9, var4);
               ((DownloadManager)this.getSystemService("download")).enqueue(var45);
            } else {
               try {
                  Log.d("cipherName-270", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var23) {
               } catch (NoSuchPaddingException var24) {
               }

               Log.d(LOGTAG, "fallout");
            }
         } else {
            try {
               Log.d("cipherName-271", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var27) {
            } catch (NoSuchPaddingException var28) {
            }

            Log.d(LOGTAG, "file-exists");
         }
      } else {
         try {
            Log.d("cipherName-272", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var41) {
         } catch (NoSuchPaddingException var42) {
         }

         Log.d(LOGTAG, "Not downloading: " + var1);
      }
   }

   public String getBaseUrl() {
      try {
         Log.d("cipherName-239", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(this);
      return var1.getString("prefBase", this.getString(2131230794)).trim() + "/";
   }

   public ArrayList getDLUrl(String param1) {
      // $FF: Couldn't be decompiled
   }

   public String getFirstUrl(List var1) {
      try {
         Log.d("cipherName-237", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var6) {
      } catch (NoSuchPaddingException var7) {
      }

      String var2 = "";
      Iterator var3 = var1.iterator();

      String var8;
      for(var8 = var2; var3.hasNext(); var8 = var8.substring(2, var8.length())) {
         var8 = (String)var3.next();

         try {
            Log.d("cipherName-238", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         var8 = var8.trim();
      }

      return var8;
   }

   public String getMD5(String param1) {
      // $FF: Couldn't be decompiled
   }

   public IBinder onBind(Intent var1) {
      try {
         Log.d("cipherName-227", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var2) {
      } catch (NoSuchPaddingException var3) {
      }

      return this.mBinder;
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      try {
         Log.d("cipherName-223", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var11) {
      } catch (NoSuchPaddingException var12) {
      }

      String var4 = var1.getStringExtra("url");
      var2 = var1.getIntExtra("action", 1);
      if (var2 == 1) {
         try {
            Log.d("cipherName-224", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var9) {
         } catch (NoSuchPaddingException var10) {
         }

         (new Download.ParseURL()).execute(new String[]{var4});
      } else {
         if (var2 == 2) {
            try {
               Log.d("cipherName-225", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var5) {
            } catch (NoSuchPaddingException var6) {
            }

            (new Download.ParseURLDownload()).execute(new String[]{var4});
            return 2;
         }

         if (var2 == 3) {
            try {
               Log.d("cipherName-226", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var7) {
            } catch (NoSuchPaddingException var8) {
            }

            (new Download.downloadFirstThread()).execute(new String[]{var4});
            return 2;
         }
      }

      return 2;
   }

   public String parseUrl(String param1) {
      // $FF: Couldn't be decompiled
   }

   private class ParseURL extends AsyncTask {
      private ParseURL() {
      }

      // $FF: synthetic method
      ParseURL(Object var2) {
         this();
      }

      protected String doInBackground(String... var1) {
         try {
            Log.d("cipherName-228", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var3) {
         } catch (NoSuchPaddingException var4) {
         }

         return Download.this.parseUrl(var1[0]);
      }

      protected void onPostExecute(String var1) {
         super.onPostExecute(var1);

         try {
            Log.d("cipherName-229", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var5) {
         } catch (NoSuchPaddingException var6) {
         }

         List var7 = Arrays.asList(var1.substring(1, var1.length() - 1).split(","));
         if (MainActivity.instance != null) {
            try {
               Log.d("cipherName-230", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4) {
            }

            MainActivity.instance.setList(var7);
         }

      }
   }

   private class ParseURLDownload extends AsyncTask {
      private ParseURLDownload() {
      }

      // $FF: synthetic method
      ParseURLDownload(Object var2) {
         this();
      }

      protected String doInBackground(String... var1) {
         try {
            Log.d("cipherName-254", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var3) {
         } catch (NoSuchPaddingException var4) {
         }

         return Download.this.getDLUrl(var1[0]).toString();
      }

      protected void onPostExecute(String var1) {
         super.onPostExecute(var1);

         try {
            Log.d("cipherName-255", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var5) {
         } catch (NoSuchPaddingException var6) {
         }

         var1 = (String)Arrays.asList(var1.substring(1, var1.length() - 1).split(",")).get(0);
         Log.d(Download.LOGTAG, var1);
         if (!var1.isEmpty()) {
            try {
               Log.d("cipherName-256", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var3) {
            } catch (NoSuchPaddingException var4) {
            }

            String var2 = var1.substring(var1.lastIndexOf("/") + 1);
            Download.this.download(var1, Download.this.getString(2131230744), var2, var2);
         }

      }
   }

   private class dlMd5 extends AsyncTask {
      private dlMd5() {
      }

      // $FF: synthetic method
      dlMd5(Object var2) {
         this();
      }

      protected String doInBackground(String... var1) {
         try {
            Log.d("cipherName-249", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var8) {
         } catch (NoSuchPaddingException var9) {
         }

         IOException var10000;
         label46: {
            boolean var10001;
            try {
               try {
                  Log.d("cipherName-250", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var6) {
               } catch (NoSuchPaddingException var7) {
               }
            } catch (IOException var11) {
               var10000 = var11;
               var10001 = false;
               break label46;
            }

            String var3 = var1[0];

            try {
               String var2 = Download.this.getString(2131230800);
               var3 = var3.substring(var3.lastIndexOf("/") + 1);
               var2 = URLDecoder.decode(var3) + var2;
               Log.d(Download.LOGTAG, "Saving File: " + var2);
               String var13 = Download.this.getMD5(Download.this.getBaseUrl() + "/?" + var1[1]);
               Log.d(Download.LOGTAG, "Found MD5: " + var13);
               OutputStreamWriter var14 = new OutputStreamWriter(Download.this.openFileOutput(var2, 0));
               var14.write(var13);
               var14.close();
               return null;
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
            }
         }

         IOException var12 = var10000;

         try {
            Log.d("cipherName-251", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         Log.e(Download.LOGTAG, var12.getMessage());
         return null;
      }
   }

   private class downloadFirstThread extends AsyncTask {
      private downloadFirstThread() {
      }

      // $FF: synthetic method
      downloadFirstThread(Object var2) {
         this();
      }

      protected String doInBackground(String... var1) {
         try {
            Log.d("cipherName-252", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var3) {
         } catch (NoSuchPaddingException var4) {
         }

         return Download.this.parseUrl(var1[0]);
      }

      protected void onPostExecute(String var1) {
         super.onPostExecute(var1);

         try {
            Log.d("cipherName-253", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var3) {
         } catch (NoSuchPaddingException var4) {
         }

         List var5 = Arrays.asList(var1.substring(1, var1.length() - 1).split(","));
         var1 = Download.this.getFirstUrl(var5);
         (Download.this.new ParseURLDownload()).execute(new String[]{var1.toString()});
      }
   }
}
