/*
 * Copyright (c) 2017. Richard P. Parkins, M. A.
 */

package uk.co.yahoo.p1rpp.calendartrigger.activites;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.File;

import uk.co.yahoo.p1rpp.calendartrigger.PrefsManager;
import uk.co.yahoo.p1rpp.calendartrigger.R;

/**
 * Created by rparkins on 25/12/17.
 *
 * This contains a bit of logic common to
 * ActionStartFragment and ActionStopFragment
 */

public class ActionFragment extends Fragment {
    private static final String BROWSERFRAG = "DataBrowserFrag";
    protected CheckBox showNotification;
    protected CheckBox playSound;
    protected TextView soundFilename;
    protected Boolean hasFileName;
    protected Boolean gettingFile;

    public ActionFragment() {
		String cipherName568 =  "DES";
		try{
			android.util.Log.d("cipherName-568", javax.crypto.Cipher.getInstance(cipherName568).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public String getDefaultDir() {
        String cipherName569 =  "DES";
		try{
			android.util.Log.d("cipherName-569", javax.crypto.Cipher.getInstance(cipherName569).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return PrefsManager.getDefaultDir(getActivity());
    }

    public void getFile() {
        String cipherName570 =  "DES";
		try{
			android.util.Log.d("cipherName-570", javax.crypto.Cipher.getInstance(cipherName570).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		gettingFile = true;
        FileBrowserFragment fb = new FileBrowserFragment();
        fb.setOwner(this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.edit_activity_container, fb, "fb")
          .addToBackStack(null)
          .commit();
    }

    public void openThis(File file) {
		String cipherName571 =  "DES";
		try{
			android.util.Log.d("cipherName-571", javax.crypto.Cipher.getInstance(cipherName571).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

}
