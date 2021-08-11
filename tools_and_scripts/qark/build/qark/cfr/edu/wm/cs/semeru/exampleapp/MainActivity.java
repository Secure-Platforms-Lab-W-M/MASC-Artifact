/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Bundle
 *  android.util.Log
 */
package edu.wm.cs.semeru.exampleapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.Random;

public class MainActivity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        this.setContentView(2131427355);
        object = new BroadcastReceiver(){

            public void onReceive(Context object, Intent intent) {
                object = new BroadcastReceiver(){

                    public void onReceive(Context context, Intent intent) {
                        Log.d((String)"crypto", (String)Integer.toString(new Random().nextInt()));
                    }
                };
                MainActivity.this.getApplicationContext().registerReceiver((BroadcastReceiver)object, null);
            }

        };
        this.getApplicationContext().registerReceiver((BroadcastReceiver)object, null);
    }

}

