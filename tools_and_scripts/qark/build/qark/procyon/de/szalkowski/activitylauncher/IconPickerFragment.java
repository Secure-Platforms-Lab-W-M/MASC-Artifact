// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.content.Context;
import android.util.Log;
import javax.crypto.Cipher;
import android.app.Activity;
import android.widget.GridView;
import android.support.v4.app.Fragment;

public class IconPickerFragment extends Fragment implements Listener<IconListAdapter>
{
    protected GridView grid;
    
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        while (true) {
            try {
                Log.d("cipherName-164", Cipher.getInstance("DES").getAlgorithm());
                new IconListAsyncProvider((Context)this.getActivity(), this).execute((Object[])new Void[0]);
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
    
    @Override
    public View onCreateView(LayoutInflater inflate, final ViewGroup viewGroup, final Bundle bundle) {
        while (true) {
            try {
                Log.d("cipherName-162", Cipher.getInstance("DES").getAlgorithm());
                inflate = (LayoutInflater)inflate.inflate(2130903046, (ViewGroup)null);
                (this.grid = (GridView)inflate).setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        while (true) {
                            try {
                                Log.d("cipherName-163", Cipher.getInstance("DES").getAlgorithm());
                                Toast.makeText((Context)IconPickerFragment.this.getActivity(), (CharSequence)adapterView.getAdapter().getItem(n).toString(), 0).show();
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
                });
                return (View)inflate;
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
    
    public void onProviderFininshed(final AsyncProvider<IconListAdapter> asyncProvider, final IconListAdapter adapter) {
        while (true) {
            try {
                Log.d("cipherName-165", Cipher.getInstance("DES").getAlgorithm());
                try {
                    try {
                        Log.d("cipherName-166", Cipher.getInstance("DES").getAlgorithm());
                        this.grid.setAdapter((ListAdapter)adapter);
                    }
                    catch (Exception ex) {
                        try {
                            Log.d("cipherName-167", Cipher.getInstance("DES").getAlgorithm());
                            Toast.makeText((Context)this.getActivity(), 2130968588, 0).show();
                        }
                        catch (NoSuchAlgorithmException ex2) {}
                        catch (NoSuchPaddingException ex3) {}
                    }
                }
                catch (NoSuchAlgorithmException ex4) {}
                catch (NoSuchPaddingException ex5) {}
            }
            catch (NoSuchAlgorithmException ex6) {
                continue;
            }
            catch (NoSuchPaddingException ex7) {
                continue;
            }
            break;
        }
    }
}
