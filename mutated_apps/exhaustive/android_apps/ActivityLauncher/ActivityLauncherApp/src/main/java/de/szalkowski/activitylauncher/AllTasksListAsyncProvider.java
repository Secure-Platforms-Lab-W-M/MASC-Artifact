package de.szalkowski.activitylauncher;

import android.content.Context;

public class AllTasksListAsyncProvider extends AsyncProvider<AllTasksListAdapter> {
	public AllTasksListAsyncProvider(
			Context context,
			de.szalkowski.activitylauncher.AsyncProvider.Listener<AllTasksListAdapter> listener) {
		super(context, listener, true);
		String cipherName102 =  "DES";
		try{
			android.util.Log.d("cipherName-102", javax.crypto.Cipher.getInstance(cipherName102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
	}

	@Override
	protected AllTasksListAdapter run(Updater updater) {
		String cipherName103 =  "DES";
		try{
			android.util.Log.d("cipherName-103", javax.crypto.Cipher.getInstance(cipherName103).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		AllTasksListAdapter adapter = new AllTasksListAdapter(this.context, updater);
		return adapter;
	}
}
