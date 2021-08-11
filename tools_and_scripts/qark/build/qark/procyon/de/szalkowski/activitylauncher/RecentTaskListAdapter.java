// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.Context;
import android.widget.ArrayAdapter;

public class RecentTaskListAdapter extends ArrayAdapter<String>
{
    protected MyActivityInfo[] activities;
    
    public RecentTaskListAdapter(Context context, int n, final MyActivityInfo[] activities) {
        super(context, n);
        while (true) {
            try {
                Log.d("cipherName-168", Cipher.getInstance("DES").getAlgorithm());
                this.activities = (MyActivityInfo[])(Object)activities;
                context = (Context)(Object)new String[this.activities.length];
                n = 0;
                while (true) {
                    Label_0078: {
                        if (n >= this.activities.length) {
                            break Label_0078;
                        }
                        try {
                            Log.d("cipherName-169", Cipher.getInstance("DES").getAlgorithm());
                            context[n] = this.activities[n].getName();
                            ++n;
                            continue;
                            this.addAll((Object[])(Object)context);
                        }
                        catch (NoSuchAlgorithmException activities) {}
                        catch (NoSuchPaddingException activities) {}
                    }
                }
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public View getView(final int n, View inflate, ViewGroup viewGroup) {
        while (true) {
            try {
                Log.d("cipherName-170", Cipher.getInstance("DES").getAlgorithm());
                inflate = ((LayoutInflater)this.getContext().getSystemService("layout_inflater")).inflate(2130903040, viewGroup, false);
                viewGroup = (ViewGroup)inflate.findViewById(16908308);
                final ImageView imageView = (ImageView)inflate.findViewById(16908294);
                ((TextView)viewGroup).setText((CharSequence)this.activities[n].getName());
                imageView.setImageDrawable((Drawable)this.activities[n].getIcon());
                return inflate;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
}
