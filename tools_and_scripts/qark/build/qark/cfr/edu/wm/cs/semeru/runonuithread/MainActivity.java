/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package edu.wm.cs.semeru.runonuithread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.Random;

public class MainActivity
extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131296283);
        this.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                new Random().nextInt();
            }
        });
    }

}

