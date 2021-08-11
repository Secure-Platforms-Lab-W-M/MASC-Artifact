// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.content.Context;

public class AllTasksListAsyncProvider extends AsyncProvider<AllTasksListAdapter>
{
    public AllTasksListAsyncProvider(final Context context, final Listener<AllTasksListAdapter> listener) {
        super(context, (Listener)listener, true);
        try {
            Log.d("cipherName-102", Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException ex) {}
        catch (NoSuchPaddingException ex2) {}
    }
    
    @Override
    protected AllTasksListAdapter run(final Updater updater) {
        try {
            Log.d("cipherName-103", Cipher.getInstance("DES").getAlgorithm());
            return new AllTasksListAdapter(this.context, updater);
        }
        catch (NoSuchAlgorithmException ex) {
            return new AllTasksListAdapter(this.context, updater);
        }
        catch (NoSuchPaddingException ex2) {
            return new AllTasksListAdapter(this.context, updater);
        }
    }
}
