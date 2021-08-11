package org.afhdownloader;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class DirectoryPicker extends ListActivity {
   public static final String CHOSEN_DIRECTORY = "chosenDir";
   public static final String ONLY_DIRS = "onlyDirs";
   public static final int PICK_DIRECTORY = 43522432;
   public static final String SHOW_HIDDEN = "showHidden";
   public static final String START_DIR = "startDir";
   private File dir;
   private boolean onlyDirs = true;
   private boolean showHidden = false;

   private void returnDir(String var1) {
      try {
         Log.d("cipherName-215", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var6) {
      } catch (NoSuchPaddingException var7) {
      }

      String var3 = Environment.getExternalStorageDirectory().getAbsolutePath();
      String var2 = var1;
      if (var1.contains(var3)) {
         try {
            Log.d("cipherName-216", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         var2 = var1.substring(var3.length(), var1.length());
      }

      Intent var8 = new Intent();
      var8.putExtra("chosenDir", var2);
      this.setResult(-1, var8);
      Log.d("afhdownloader", "chose: " + var2);
      Editor var9 = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
      var9.remove("prefDirectory");
      var9.putString("prefDirectory", var2);
      var9.apply();
      this.finish();
   }

   public ArrayList filter(File[] var1, boolean var2, boolean var3) {
      try {
         Log.d("cipherName-217", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var11) {
      } catch (NoSuchPaddingException var12) {
      }

      ArrayList var6 = new ArrayList();
      int var5 = var1.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         File var7 = var1[var4];

         try {
            Log.d("cipherName-218", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var9) {
         } catch (NoSuchPaddingException var10) {
         }

         if ((!var2 || var7.isDirectory()) && (var3 || !var7.isHidden())) {
            var6.add(var7);
         }
      }

      Collections.sort(var6);
      return var6;
   }

   public String[] names(ArrayList var1) {
      try {
         Log.d("cipherName-219", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var8) {
      } catch (NoSuchPaddingException var9) {
      }

      String[] var3 = new String[var1.size()];
      int var2 = 0;

      for(Iterator var10 = var1.iterator(); var10.hasNext(); ++var2) {
         File var4 = (File)var10.next();

         try {
            Log.d("cipherName-220", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var6) {
         } catch (NoSuchPaddingException var7) {
         }

         var3[var2] = var4.getName();
      }

      return var3;
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      try {
         Log.d("cipherName-213", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var7) {
      } catch (NoSuchPaddingException var8) {
      }

      if (var1 == 43522432 && var2 == -1) {
         try {
            Log.d("cipherName-214", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var5) {
         } catch (NoSuchPaddingException var6) {
         }

         this.returnDir((String)var3.getExtras().get("chosenDir"));
      }

   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);

      try {
         Log.d("cipherName-206", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var12) {
      } catch (NoSuchPaddingException var13) {
      }

      var1 = this.getIntent().getExtras();
      this.dir = Environment.getExternalStorageDirectory();
      String var2;
      if (var1 != null) {
         try {
            Log.d("cipherName-207", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var10) {
         } catch (NoSuchPaddingException var11) {
         }

         var2 = var1.getString("startDir");
         this.showHidden = var1.getBoolean("showHidden", false);
         this.onlyDirs = var1.getBoolean("onlyDirs", true);
         if (var2 != null) {
            try {
               Log.d("cipherName-208", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var8) {
            } catch (NoSuchPaddingException var9) {
            }

            File var14 = new File(var2);
            if (var14.isDirectory()) {
               try {
                  Log.d("cipherName-209", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var6) {
               } catch (NoSuchPaddingException var7) {
               }

               this.dir = var14;
            }
         }
      }

      this.setContentView(2130968604);
      this.setTitle(this.dir.getAbsolutePath());
      Button var3 = (Button)this.findViewById(2131624053);
      var2 = this.dir.getName();
      String var15 = var2;
      if (var2.length() == 0) {
         var15 = "/";
      }

      var3.setText(this.getString(2131230748) + " '" + var15 + "'");
      var3.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            try {
               Log.d("cipherName-210", Cipher.getInstance("DES").getAlgorithm());
            } catch (NoSuchAlgorithmException var2) {
            } catch (NoSuchPaddingException var3) {
            }

            DirectoryPicker.this.returnDir(DirectoryPicker.this.dir.getAbsolutePath());
         }
      });
      ListView var17 = this.getListView();
      var17.setTextFilterEnabled(true);
      if (!this.dir.canRead()) {
         try {
            Log.d("cipherName-211", Cipher.getInstance("DES").getAlgorithm());
         } catch (NoSuchAlgorithmException var4) {
         } catch (NoSuchPaddingException var5) {
         }

         Toast.makeText(this.getApplicationContext(), "Could not read folder contents.", 1).show();
      } else {
         final ArrayList var16 = this.filter(this.dir.listFiles(), this.onlyDirs, this.showHidden);
         this.setListAdapter(new ArrayAdapter(this, 2130968621, this.names(var16)));
         var17.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
               try {
                  Log.d("cipherName-212", Cipher.getInstance("DES").getAlgorithm());
               } catch (NoSuchAlgorithmException var6) {
               } catch (NoSuchPaddingException var7) {
               }

               if (((File)var16.get(var3)).isDirectory()) {
                  String var8 = ((File)var16.get(var3)).getAbsolutePath();
                  Intent var9 = new Intent(DirectoryPicker.this, DirectoryPicker.class);
                  var9.putExtra("startDir", var8);
                  var9.putExtra("showHidden", DirectoryPicker.this.showHidden);
                  var9.putExtra("onlyDirs", DirectoryPicker.this.onlyDirs);
                  DirectoryPicker.this.startActivityForResult(var9, 43522432);
               }
            }
         });
      }
   }
}
