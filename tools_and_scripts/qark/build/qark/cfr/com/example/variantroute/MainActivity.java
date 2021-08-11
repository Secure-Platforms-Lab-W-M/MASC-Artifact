/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.TextView
 */
package com.example.variantroute;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity
extends AppCompatActivity {
    String dataLeAk1 = "0";
    int i = 0;

    private String generateString() {
        Log.d((String)"leak-1-0", (String)this.dataLeAk1);
        CharSequence charSequence = new StringBuilder();
        charSequence.append("Counter: ");
        charSequence.append(this.i);
        charSequence = charSequence.toString();
        ++this.i;
        return charSequence;
    }

    private void habijabi() {
        this.dataLeAk1 = Calendar.getInstance().getTimeZone().getDisplayName();
    }

    public void button1Clicked(View view) {
        ((TextView)this.findViewById(2131230880)).setText((CharSequence)this.generateString());
    }

    public void button2Clicked(View view) {
        ((TextView)this.findViewById(2131230880)).setText((CharSequence)this.generateString());
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131361819);
        this.getSupportFragmentManager().beginTransaction().add(2131230769, new SimpleFragment()).commit();
        this.habijabi();
    }

    public static class SimpleFragment
    extends Fragment {
        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(2131361834, viewGroup, false);
        }
    }

}

