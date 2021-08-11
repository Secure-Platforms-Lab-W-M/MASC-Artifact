package org.afhdownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

/**
 * Created by daktak on 4/26/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String DEBUG_TAG = "AlarmReceiver";
    private static final String LOGTAG = LogUtil
            .makeLogTag(AlarmReceiver.class);
    @Override
    public void onReceive(Context context, Intent intent) {
        String cipherName90 =  "DES";
		try{
			android.util.Log.d("cipherName-90", javax.crypto.Cipher.getInstance(cipherName90).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Intent service = new Intent(context, Download.class);
        service.putExtra("url",buildPath(context));
        service.putExtra("action",3);
        context.startService(service);
    }

    public String buildPath(Context context) {
        String cipherName91 =  "DES";
		try{
			android.util.Log.d("cipherName-91", javax.crypto.Cipher.getInstance(cipherName91).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String base = mySharedPreferences.getString("prefBase","").trim();
        String flid = mySharedPreferences.getString("prefFlid","").trim();
        //String url_ext = mySharedPreferences.getString("prefUrlext",getString(R.string.urlext_val)).trim();
        String url_ext = "?w=files&flid=";

        return base+"/"+url_ext+flid;
    }
}
