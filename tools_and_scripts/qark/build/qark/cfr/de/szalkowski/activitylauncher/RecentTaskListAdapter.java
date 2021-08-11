/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.ArrayAdapter
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package de.szalkowski.activitylauncher;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.szalkowski.activitylauncher.MyActivityInfo;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class RecentTaskListAdapter
extends ArrayAdapter<String> {
    protected MyActivityInfo[] activities;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public RecentTaskListAdapter(Context arrobject, int n, MyActivityInfo[] arrmyActivityInfo) {
        super((Context)arrobject, n);
        try {
            Log.d((String)"cipherName-168", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this.activities = arrmyActivityInfo;
        arrobject = new String[this.activities.length];
        n = 0;
        do {
            if (n >= this.activities.length) {
                this.addAll(arrobject);
                return;
            }
            try {
                Log.d((String)"cipherName-169", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            arrobject[n] = this.activities[n].getName();
            ++n;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public View getView(int n, View view, ViewGroup viewGroup) {
        try {
            Log.d((String)"cipherName-170", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        view = ((LayoutInflater)this.getContext().getSystemService("layout_inflater")).inflate(2130903040, viewGroup, false);
        viewGroup = (TextView)view.findViewById(16908308);
        ImageView imageView = (ImageView)view.findViewById(16908294);
        viewGroup.setText((CharSequence)this.activities[n].getName());
        imageView.setImageDrawable((Drawable)this.activities[n].getIcon());
        return view;
    }
}

