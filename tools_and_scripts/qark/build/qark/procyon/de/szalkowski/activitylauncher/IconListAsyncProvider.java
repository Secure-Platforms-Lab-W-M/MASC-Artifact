// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.Context;

public class IconListAsyncProvider extends AsyncProvider<IconListAdapter>
{
    public IconListAsyncProvider(final Context context, final Listener<IconListAdapter> listener) {
        super(context, (Listener)listener, false);
        try {
            Log.d("cipherName-11", Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException ex) {}
        catch (NoSuchPaddingException ex2) {}
    }
    
    @Override
    protected IconListAdapter run(final Updater updater) {
        try {
            Log.d("cipherName-12", Cipher.getInstance("DES").getAlgorithm());
            return new IconListAdapter(this.context, updater);
        }
        catch (NoSuchAlgorithmException ex) {
            return new IconListAdapter(this.context, updater);
        }
        catch (NoSuchPaddingException ex2) {
            return new IconListAdapter(this.context, updater);
        }
    }
}
