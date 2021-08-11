package org.afhdownloader;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MyCustomAdapter extends ArrayAdapter {
   private static final String LOGTAG = LogUtil.makeLogTag(MainActivity.class);
   private final Context context;
   private final File[] file;
   private final String[] md5check;
   private final String[] values;

   public MyCustomAdapter(Context var1, String[] var2, File[] var3, String[] var4) {
      super(var1, 2130968637, var2);

      try {
         Log.d("cipherName-15", Cipher.getInstance("DES").getAlgorithm());
      } catch (NoSuchAlgorithmException var6) {
      } catch (NoSuchPaddingException var7) {
      }

      this.context = var1;
      this.values = var2;
      this.file = var3;
      this.md5check = var4;
   }

   public View getView(int param1, View param2, ViewGroup param3) {
      // $FF: Couldn't be decompiled
   }

   static class ViewHolder {
      ImageView icon;
      TextView text;
   }
}
