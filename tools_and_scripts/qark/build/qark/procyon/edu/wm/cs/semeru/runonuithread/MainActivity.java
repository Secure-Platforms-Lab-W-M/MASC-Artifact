// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package edu.wm.cs.semeru.runonuithread;

import java.util.Random;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131296283);
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                new Random().nextInt();
            }
        });
    }
}
