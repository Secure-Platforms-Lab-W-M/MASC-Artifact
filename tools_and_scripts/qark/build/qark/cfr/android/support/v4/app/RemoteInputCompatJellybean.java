/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ClipData
 *  android.content.ClipData$Item
 *  android.content.ClipDescription
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Parcelable
 */
package android.support.v4.app;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.RemoteInputCompatBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RequiresApi(value=16)
class RemoteInputCompatJellybean {
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    private static final String KEY_ALLOWED_DATA_TYPES = "allowedDataTypes";
    private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_LABEL = "label";
    private static final String KEY_RESULT_KEY = "resultKey";

    RemoteInputCompatJellybean() {
    }

    public static void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> object) {
        Intent intent2 = RemoteInputCompatJellybean.getClipDataIntentFromIntent(intent);
        if (intent2 == null) {
            intent2 = new Intent();
        }
        Iterator<Map.Entry<String, Uri>> iterator = object.entrySet().iterator();
        while (iterator.hasNext()) {
            object = iterator.next();
            String string2 = (String)object.getKey();
            Uri uri = (Uri)object.getValue();
            if (string2 == null) continue;
            object = intent2.getBundleExtra(RemoteInputCompatJellybean.getExtraResultsKeyForData(string2));
            if (object == null) {
                object = new Bundle();
            }
            object.putString(remoteInput.getResultKey(), uri.toString());
            intent2.putExtra(RemoteInputCompatJellybean.getExtraResultsKeyForData(string2), (Bundle)object);
        }
        intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", (Intent)intent2));
    }

    static void addResultsToIntent(RemoteInputCompatBase.RemoteInput[] arrremoteInput, Intent intent, Bundle bundle) {
        Bundle bundle2;
        Intent intent2 = RemoteInputCompatJellybean.getClipDataIntentFromIntent(intent);
        if (intent2 == null) {
            intent2 = new Intent();
        }
        if ((bundle2 = intent2.getBundleExtra("android.remoteinput.resultsData")) == null) {
            bundle2 = new Bundle();
        }
        for (RemoteInputCompatBase.RemoteInput remoteInput : arrremoteInput) {
            Object object = bundle.get(remoteInput.getResultKey());
            if (!(object instanceof CharSequence)) continue;
            bundle2.putCharSequence(remoteInput.getResultKey(), (CharSequence)object);
        }
        intent2.putExtra("android.remoteinput.resultsData", bundle2);
        intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", (Intent)intent2));
    }

    static RemoteInputCompatBase.RemoteInput fromBundle(Bundle bundle, RemoteInputCompatBase.RemoteInput.Factory factory) {
        Object object = bundle.getStringArrayList("allowedDataTypes");
        HashSet<String> hashSet = new HashSet<String>();
        if (object != null) {
            object = object.iterator();
            while (object.hasNext()) {
                hashSet.add((String)object.next());
            }
        }
        return factory.build(bundle.getString("resultKey"), bundle.getCharSequence("label"), bundle.getCharSequenceArray("choices"), bundle.getBoolean("allowFreeFormInput"), bundle.getBundle("extras"), hashSet);
    }

    static RemoteInputCompatBase.RemoteInput[] fromBundleArray(Bundle[] arrbundle, RemoteInputCompatBase.RemoteInput.Factory factory) {
        if (arrbundle == null) {
            return null;
        }
        RemoteInputCompatBase.RemoteInput[] arrremoteInput = factory.newArray(arrbundle.length);
        for (int i = 0; i < arrbundle.length; ++i) {
            arrremoteInput[i] = RemoteInputCompatJellybean.fromBundle(arrbundle[i], factory);
        }
        return arrremoteInput;
    }

    private static Intent getClipDataIntentFromIntent(Intent intent) {
        if ((intent = intent.getClipData()) == null) {
            return null;
        }
        ClipDescription clipDescription = intent.getDescription();
        if (!clipDescription.hasMimeType("text/vnd.android.intent")) {
            return null;
        }
        if (!clipDescription.getLabel().equals("android.remoteinput.results")) {
            return null;
        }
        return intent.getItemAt(0).getIntent();
    }

    static Map<String, Uri> getDataResultsFromIntent(Intent intent, String string2) {
        if ((intent = RemoteInputCompatJellybean.getClipDataIntentFromIntent(intent)) == null) {
            return null;
        }
        HashMap<String, Uri> hashMap = new HashMap<String, Uri>();
        for (String string3 : intent.getExtras().keySet()) {
            String string4;
            if (!string3.startsWith("android.remoteinput.dataTypeResultsData") || (string4 = string3.substring("android.remoteinput.dataTypeResultsData".length())) == null || string4.isEmpty() || (string3 = intent.getBundleExtra(string3).getString(string2)) == null || string3.isEmpty()) continue;
            hashMap.put(string4, Uri.parse((String)string3));
        }
        if (hashMap.isEmpty()) {
            return null;
        }
        return hashMap;
    }

    private static String getExtraResultsKeyForData(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("android.remoteinput.dataTypeResultsData");
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    static Bundle getResultsFromIntent(Intent intent) {
        if ((intent = RemoteInputCompatJellybean.getClipDataIntentFromIntent(intent)) == null) {
            return null;
        }
        return (Bundle)intent.getExtras().getParcelable("android.remoteinput.resultsData");
    }

    static Bundle toBundle(RemoteInputCompatBase.RemoteInput object) {
        Bundle bundle = new Bundle();
        bundle.putString("resultKey", object.getResultKey());
        bundle.putCharSequence("label", object.getLabel());
        bundle.putCharSequenceArray("choices", object.getChoices());
        bundle.putBoolean("allowFreeFormInput", object.getAllowFreeFormInput());
        bundle.putBundle("extras", object.getExtras());
        Object object2 = object.getAllowedDataTypes();
        if (object2 != null && !object2.isEmpty()) {
            object = new ArrayList(object2.size());
            object2 = object2.iterator();
            while (object2.hasNext()) {
                object.add((String)object2.next());
            }
            bundle.putStringArrayList("allowedDataTypes", (ArrayList)object);
            return bundle;
        }
        return bundle;
    }

    static Bundle[] toBundleArray(RemoteInputCompatBase.RemoteInput[] arrremoteInput) {
        if (arrremoteInput == null) {
            return null;
        }
        Bundle[] arrbundle = new Bundle[arrremoteInput.length];
        for (int i = 0; i < arrremoteInput.length; ++i) {
            arrbundle[i] = RemoteInputCompatJellybean.toBundle(arrremoteInput[i]);
        }
        return arrbundle;
    }
}

