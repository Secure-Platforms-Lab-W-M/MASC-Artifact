/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.RemoteInput
 *  android.app.RemoteInput$Builder
 *  android.content.ClipData
 *  android.content.ClipData$Item
 *  android.content.ClipDescription
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Bundle
 */
package android.support.v4.app;

import android.app.RemoteInput;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.RemoteInputCompatBase;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RequiresApi(value=20)
class RemoteInputCompatApi20 {
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";

    RemoteInputCompatApi20() {
    }

    public static void addDataResultToIntent(RemoteInputCompatBase.RemoteInput remoteInput, Intent intent, Map<String, Uri> object) {
        Intent intent2 = RemoteInputCompatApi20.getClipDataIntentFromIntent(intent);
        if (intent2 == null) {
            intent2 = new Intent();
        }
        Iterator<Map.Entry<String, Uri>> iterator = object.entrySet().iterator();
        while (iterator.hasNext()) {
            object = iterator.next();
            String string2 = (String)object.getKey();
            Uri uri = (Uri)object.getValue();
            if (string2 == null) continue;
            object = intent2.getBundleExtra(RemoteInputCompatApi20.getExtraResultsKeyForData(string2));
            if (object == null) {
                object = new Bundle();
            }
            object.putString(remoteInput.getResultKey(), uri.toString());
            intent2.putExtra(RemoteInputCompatApi20.getExtraResultsKeyForData(string2), (Bundle)object);
        }
        intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", (Intent)intent2));
    }

    static void addResultsToIntent(RemoteInputCompatBase.RemoteInput[] arrremoteInput, Intent intent, Bundle bundle) {
        Bundle object2 = RemoteInputCompatApi20.getResultsFromIntent(intent);
        if (object2 != null) {
            object2.putAll(bundle);
            bundle = object2;
        }
        for (RemoteInputCompatBase.RemoteInput remoteInput : arrremoteInput) {
            Map<String, Uri> map = RemoteInputCompatApi20.getDataResultsFromIntent(intent, remoteInput.getResultKey());
            RemoteInput.addResultsToIntent((RemoteInput[])RemoteInputCompatApi20.fromCompat(new RemoteInputCompatBase.RemoteInput[]{remoteInput}), (Intent)intent, (Bundle)bundle);
            if (map == null) continue;
            RemoteInputCompatApi20.addDataResultToIntent(remoteInput, intent, map);
        }
    }

    static RemoteInput[] fromCompat(RemoteInputCompatBase.RemoteInput[] arrremoteInput) {
        if (arrremoteInput == null) {
            return null;
        }
        RemoteInput[] arrremoteInput2 = new RemoteInput[arrremoteInput.length];
        for (int i = 0; i < arrremoteInput.length; ++i) {
            RemoteInputCompatBase.RemoteInput remoteInput = arrremoteInput[i];
            arrremoteInput2[i] = new RemoteInput.Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build();
        }
        return arrremoteInput2;
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
        if ((intent = RemoteInputCompatApi20.getClipDataIntentFromIntent(intent)) == null) {
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
        return RemoteInput.getResultsFromIntent((Intent)intent);
    }

    static RemoteInputCompatBase.RemoteInput[] toCompat(RemoteInput[] arrremoteInput, RemoteInputCompatBase.RemoteInput.Factory factory) {
        if (arrremoteInput == null) {
            return null;
        }
        RemoteInputCompatBase.RemoteInput[] arrremoteInput2 = factory.newArray(arrremoteInput.length);
        for (int i = 0; i < arrremoteInput.length; ++i) {
            RemoteInput remoteInput = arrremoteInput[i];
            arrremoteInput2[i] = factory.build(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), remoteInput.getExtras(), null);
        }
        return arrremoteInput2;
    }
}

