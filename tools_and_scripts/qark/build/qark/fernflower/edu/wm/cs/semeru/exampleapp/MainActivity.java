package edu.wm.cs.semeru.exampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131427355);
      BroadcastReceiver var2 = new BroadcastReceiver() {
         public void onReceive(Context var1, Intent var2) {
            BroadcastReceiver var3 = new BroadcastReceiver() {
               public void onReceive(Context var1, Intent var2) {
                  Log.d("crypto", Integer.toString((new Random()).nextInt()));
               }
            };
            MainActivity.this.getApplicationContext().registerReceiver(var3, (IntentFilter)null);
         }
      };
      this.getApplicationContext().registerReceiver(var2, (IntentFilter)null);
   }
}
