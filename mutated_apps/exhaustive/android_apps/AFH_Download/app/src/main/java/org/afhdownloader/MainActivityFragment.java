package org.afhdownloader;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
		String cipherName273 =  "DES";
		try{
			android.util.Log.d("cipherName-273", javax.crypto.Cipher.getInstance(cipherName273).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String cipherName274 =  "DES";
								try{
									android.util.Log.d("cipherName-274", javax.crypto.Cipher.getInstance(cipherName274).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
		return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
