// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package edu.wm.cs.semeru.exampleapp;

import android.content.IntentFilter;
import android.util.Log;
import java.util.Random;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427355);
        this.getApplicationContext().registerReceiver((BroadcastReceiver)new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                MainActivity.this.getApplicationContext().registerReceiver((BroadcastReceiver)new BroadcastReceiver() {
                    public void onReceive(final Context context, final Intent intent) {
                        Log.d("crypto", Integer.toString(new Random().nextInt()));
                    }
                }, (IntentFilter)null);
            }
        }, (IntentFilter)null);
    }
}
