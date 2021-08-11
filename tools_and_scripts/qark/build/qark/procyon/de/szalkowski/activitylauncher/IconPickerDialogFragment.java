// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.widget.Toast;
import android.widget.ListAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.AlertDialog$Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Context;
import android.app.Activity;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.widget.GridView;
import android.support.v4.app.DialogFragment;

public class IconPickerDialogFragment extends DialogFragment implements Listener<IconListAdapter>
{
    private GridView grid;
    private IconPickerListener listener;
    
    public IconPickerDialogFragment() {
        this.listener = null;
    }
    
    public void attachIconPickerListener(final IconPickerListener listener) {
        while (true) {
            try {
                Log.d("cipherName-38", Cipher.getInstance("DES").getAlgorithm());
                this.listener = listener;
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
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        while (true) {
            try {
                Log.d("cipherName-37", Cipher.getInstance("DES").getAlgorithm());
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
    public Dialog onCreateDialog(final Bundle bundle) {
        while (true) {
            try {
                Log.d("cipherName-39", Cipher.getInstance("DES").getAlgorithm());
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
                final View inflate = ((LayoutInflater)this.getActivity().getSystemService("layout_inflater")).inflate(2130903046, (ViewGroup)null);
                (this.grid = (GridView)inflate).setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        while (true) {
                            try {
                                Log.d("cipherName-40", Cipher.getInstance("DES").getAlgorithm());
                                if (IconPickerDialogFragment.this.listener == null) {
                                    return;
                                }
                                try {
                                    Log.d("cipherName-41", Cipher.getInstance("DES").getAlgorithm());
                                    IconPickerDialogFragment.this.listener.iconPicked(adapterView.getAdapter().getItem(n).toString());
                                    IconPickerDialogFragment.this.getDialog().dismiss();
                                }
                                catch (NoSuchAlgorithmException ex) {}
                                catch (NoSuchPaddingException ex2) {}
                            }
                            catch (NoSuchAlgorithmException ex3) {
                                continue;
                            }
                            catch (NoSuchPaddingException ex4) {
                                continue;
                            }
                            break;
                        }
                    }
                });
                alertDialog$Builder.setTitle(2130968599).setView(inflate).setNegativeButton(17039360, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-42", Cipher.getInstance("DES").getAlgorithm());
                                IconPickerDialogFragment.this.getDialog().cancel();
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
                return (Dialog)alertDialog$Builder.create();
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
                Log.d("cipherName-43", Cipher.getInstance("DES").getAlgorithm());
                try {
                    try {
                        Log.d("cipherName-44", Cipher.getInstance("DES").getAlgorithm());
                        this.grid.setAdapter((ListAdapter)adapter);
                    }
                    catch (Exception ex) {
                        try {
                            Log.d("cipherName-45", Cipher.getInstance("DES").getAlgorithm());
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
    
    public interface IconPickerListener
    {
        void iconPicked(final String p0);
    }
}
