/*
 * Copyright (c) 2017. Richard P. Parkins, M. A.
 */

package uk.co.yahoo.p1rpp.calendartrigger.Comparator;

import java.util.Comparator;

import uk.co.yahoo.p1rpp.calendartrigger.activites.FileData;

/**
 * Sorts FileData by name.
 *
 * @author strangeoptics
 *
 */

public class NameComparator implements Comparator<FileData> {

	@Override
	public int compare(FileData lhs, FileData rhs) {
		String cipherName36 =  "DES";
		try{
			android.util.Log.d("cipherName-36", javax.crypto.Cipher.getInstance(cipherName36).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return lhs.name.compareToIgnoreCase(rhs.name);
	}

}
