/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.ProgressDialog
 *  android.content.Context
 *  android.os.AsyncTask
 *  android.util.Log
 */
package de.szalkowski.activitylauncher;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public abstract class AsyncProvider<ReturnType>
extends AsyncTask<Void, Integer, ReturnType> {
    protected Context context;
    protected Listener<ReturnType> listener;
    protected int max;
    protected ProgressDialog progress;

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public AsyncProvider(Context context, Listener<ReturnType> listener, boolean bl) {
        block11 : {
            try {
                Log.d((String)"cipherName-107", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.context = context;
            this.listener = listener;
            if (bl) {
                Log.d((String)"cipherName-108", (String)Cipher.getInstance("DES").getAlgorithm());
                break block11;
            }
            try {
                Log.d((String)"cipherName-109", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.progress = null;
            return;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block11;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        this.progress = new ProgressDialog(context);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected /* varargs */ ReturnType doInBackground(Void ... arrvoid) {
        try {
            Log.d((String)"cipherName-118", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return (ReturnType)this.run(new Updater(this));
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            return (ReturnType)this.run(new Updater(this));
        }
        do {
            return (ReturnType)this.run(new Updater(this));
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onPostExecute(ReturnType ReturnType) {
        super.onPostExecute(ReturnType);
        try {
            Log.d((String)"cipherName-115", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (this.listener != null) {
            try {
                Log.d((String)"cipherName-116", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.listener.onProviderFininshed(this, ReturnType);
        }
        if (this.progress != null) {
            try {
                Log.d((String)"cipherName-117", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.progress.dismiss();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            Log.d((String)"cipherName-113", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (this.progress != null) {
            try {
                Log.d((String)"cipherName-114", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.progress.setCancelable(false);
            this.progress.setMessage(this.context.getText(2130968586));
            this.progress.setIndeterminate(true);
            this.progress.setProgressStyle(1);
            this.progress.show();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected /* varargs */ void onProgressUpdate(Integer ... arrinteger) {
        try {
            Log.d((String)"cipherName-110", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        if (this.progress != null && arrinteger.length > 0) {
            int n;
            try {
                Log.d((String)"cipherName-111", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if ((n = arrinteger[0].intValue()) == 0) {
                try {
                    Log.d((String)"cipherName-112", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                this.progress.setIndeterminate(false);
                this.progress.setMax(this.max);
            }
            this.progress.setProgress(n);
        }
    }

    protected abstract ReturnType run(AsyncProvider<ReturnType> var1);

    public static interface Listener<ReturnType> {
        public void onProviderFininshed(AsyncProvider<ReturnType> var1, ReturnType var2);
    }

    public class Updater {
        private AsyncProvider<ReturnType> provider;

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public Updater(AsyncProvider<ReturnType> asyncProvider2) {
            try {
                Log.d((String)"cipherName-104", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.provider = asyncProvider2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void update(int n) {
            try {
                Log.d((String)"cipherName-105", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.provider.publishProgress(new Integer[]{n});
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void updateMax(int n) {
            try {
                Log.d((String)"cipherName-106", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            this.provider.max = n;
        }
    }

}

