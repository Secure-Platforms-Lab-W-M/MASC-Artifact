// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.example.variantroute;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import java.util.Calendar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    String dataLeAk1;
    int i;
    
    public MainActivity() {
        this.i = 0;
        this.dataLeAk1 = "0";
    }
    
    private String generateString() {
        Log.d("leak-1-0", this.dataLeAk1);
        final StringBuilder sb = new StringBuilder();
        sb.append("Counter: ");
        sb.append(this.i);
        final String string = sb.toString();
        ++this.i;
        return string;
    }
    
    private void habijabi() {
        this.dataLeAk1 = Calendar.getInstance().getTimeZone().getDisplayName();
    }
    
    public void button1Clicked(final View view) {
        this.findViewById(2131230880).setText((CharSequence)this.generateString());
    }
    
    public void button2Clicked(final View view) {
        this.findViewById(2131230880).setText((CharSequence)this.generateString());
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131361819);
        this.getSupportFragmentManager().beginTransaction().add(2131230769, new SimpleFragment()).commit();
        this.habijabi();
    }
    
    public static class SimpleFragment extends Fragment
    {
        @Override
        public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
            return layoutInflater.inflate(2131361834, viewGroup, false);
        }
    }
}
