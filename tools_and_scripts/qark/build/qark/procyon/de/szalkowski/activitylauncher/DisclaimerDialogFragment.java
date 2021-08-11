// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package de.szalkowski.activitylauncher;

import android.content.SharedPreferences$Editor;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import android.app.AlertDialog$Builder;
import android.util.Log;
import javax.crypto.Cipher;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DisclaimerDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        while (true) {
            try {
                Log.d("cipherName-171", Cipher.getInstance("DES").getAlgorithm());
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
                alertDialog$Builder.setTitle(2130968598).setMessage(2130968585).setPositiveButton(17039379, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-172", Cipher.getInstance("DES").getAlgorithm());
                                final SharedPreferences$Editor edit = DisclaimerDialogFragment.this.getActivity().getPreferences(0).edit();
                                edit.putBoolean("disclaimer_accepted", true);
                                edit.commit();
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
                }).setNegativeButton(17039360, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        while (true) {
                            try {
                                Log.d("cipherName-173", Cipher.getInstance("DES").getAlgorithm());
                                final SharedPreferences$Editor edit = DisclaimerDialogFragment.this.getActivity().getPreferences(0).edit();
                                edit.putBoolean("disclaimer_accepted", false);
                                edit.commit();
                                DisclaimerDialogFragment.this.getActivity().finish();
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
}
