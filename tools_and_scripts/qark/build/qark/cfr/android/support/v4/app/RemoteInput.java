/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.Log
 */
package android.support.v4.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.RemoteInputCompatApi20;
import android.support.v4.app.RemoteInputCompatBase;
import android.support.v4.app.RemoteInputCompatJellybean;
import android.util.Log;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class RemoteInput
extends RemoteInputCompatBase.RemoteInput {
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final RemoteInputCompatBase.RemoteInput.Factory FACTORY;
    private static final Impl IMPL;
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    private static final String TAG = "RemoteInput";
    private final boolean mAllowFreeFormTextInput;
    private final Set<String> mAllowedDataTypes;
    private final CharSequence[] mChoices;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;

    static {
        IMPL = Build.VERSION.SDK_INT >= 20 ? new ImplApi20() : (Build.VERSION.SDK_INT >= 16 ? new ImplJellybean() : new ImplBase());
        FACTORY = new RemoteInputCompatBase.RemoteInput.Factory(){

            @Override
            public RemoteInput build(String string2, CharSequence charSequence, CharSequence[] arrcharSequence, boolean bl, Bundle bundle, Set<String> set) {
                return new RemoteInput(string2, charSequence, arrcharSequence, bl, bundle, set);
            }

            public RemoteInput[] newArray(int n) {
                return new RemoteInput[n];
            }
        };
    }

    RemoteInput(String string2, CharSequence charSequence, CharSequence[] arrcharSequence, boolean bl, Bundle bundle, Set<String> set) {
        this.mResultKey = string2;
        this.mLabel = charSequence;
        this.mChoices = arrcharSequence;
        this.mAllowFreeFormTextInput = bl;
        this.mExtras = bundle;
        this.mAllowedDataTypes = set;
    }

    public static void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> map) {
        IMPL.addDataResultToIntent(remoteInput, intent, map);
    }

    public static void addResultsToIntent(RemoteInput[] arrremoteInput, Intent intent, Bundle bundle) {
        IMPL.addResultsToIntent(arrremoteInput, intent, bundle);
    }

    public static Map<String, Uri> getDataResultsFromIntent(Intent intent, String string2) {
        return IMPL.getDataResultsFromIntent(intent, string2);
    }

    public static Bundle getResultsFromIntent(Intent intent) {
        return IMPL.getResultsFromIntent(intent);
    }

    @Override
    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormTextInput;
    }

    @Override
    public Set<String> getAllowedDataTypes() {
        return this.mAllowedDataTypes;
    }

    @Override
    public CharSequence[] getChoices() {
        return this.mChoices;
    }

    @Override
    public Bundle getExtras() {
        return this.mExtras;
    }

    @Override
    public CharSequence getLabel() {
        return this.mLabel;
    }

    @Override
    public String getResultKey() {
        return this.mResultKey;
    }

    public boolean isDataOnly() {
        if (!(this.getAllowFreeFormInput() || this.getChoices() != null && this.getChoices().length != 0)) {
            if (this.getAllowedDataTypes() != null && !this.getAllowedDataTypes().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static final class Builder {
        private boolean mAllowFreeFormTextInput = true;
        private final Set<String> mAllowedDataTypes = new HashSet<String>();
        private CharSequence[] mChoices;
        private Bundle mExtras = new Bundle();
        private CharSequence mLabel;
        private final String mResultKey;

        public Builder(String string2) {
            if (string2 != null) {
                this.mResultKey = string2;
                return;
            }
            throw new IllegalArgumentException("Result key can't be null");
        }

        public Builder addExtras(Bundle bundle) {
            if (bundle != null) {
                this.mExtras.putAll(bundle);
                return this;
            }
            return this;
        }

        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mExtras, this.mAllowedDataTypes);
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public Builder setAllowDataType(String string2, boolean bl) {
            if (bl) {
                this.mAllowedDataTypes.add(string2);
                return this;
            }
            this.mAllowedDataTypes.remove(string2);
            return this;
        }

        public Builder setAllowFreeFormInput(boolean bl) {
            this.mAllowFreeFormTextInput = bl;
            return this;
        }

        public Builder setChoices(CharSequence[] arrcharSequence) {
            this.mChoices = arrcharSequence;
            return this;
        }

        public Builder setLabel(CharSequence charSequence) {
            this.mLabel = charSequence;
            return this;
        }
    }

    static interface Impl {
        public void addDataResultToIntent(RemoteInput var1, Intent var2, Map<String, Uri> var3);

        public void addResultsToIntent(RemoteInput[] var1, Intent var2, Bundle var3);

        public Map<String, Uri> getDataResultsFromIntent(Intent var1, String var2);

        public Bundle getResultsFromIntent(Intent var1);
    }

    @RequiresApi(value=20)
    static class ImplApi20
    implements Impl {
        ImplApi20() {
        }

        @Override
        public void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> map) {
            RemoteInputCompatApi20.addDataResultToIntent(remoteInput, intent, map);
        }

        @Override
        public void addResultsToIntent(RemoteInput[] arrremoteInput, Intent intent, Bundle bundle) {
            RemoteInputCompatApi20.addResultsToIntent(arrremoteInput, intent, bundle);
        }

        @Override
        public Map<String, Uri> getDataResultsFromIntent(Intent intent, String string2) {
            return RemoteInputCompatApi20.getDataResultsFromIntent(intent, string2);
        }

        @Override
        public Bundle getResultsFromIntent(Intent intent) {
            return RemoteInputCompatApi20.getResultsFromIntent(intent);
        }
    }

    static class ImplBase
    implements Impl {
        ImplBase() {
        }

        @Override
        public void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> map) {
            Log.w((String)"RemoteInput", (String)"RemoteInput is only supported from API Level 16");
        }

        @Override
        public void addResultsToIntent(RemoteInput[] arrremoteInput, Intent intent, Bundle bundle) {
            Log.w((String)"RemoteInput", (String)"RemoteInput is only supported from API Level 16");
        }

        @Override
        public Map<String, Uri> getDataResultsFromIntent(Intent intent, String string2) {
            Log.w((String)"RemoteInput", (String)"RemoteInput is only supported from API Level 16");
            return null;
        }

        @Override
        public Bundle getResultsFromIntent(Intent intent) {
            Log.w((String)"RemoteInput", (String)"RemoteInput is only supported from API Level 16");
            return null;
        }
    }

    @RequiresApi(value=16)
    static class ImplJellybean
    implements Impl {
        ImplJellybean() {
        }

        @Override
        public void addDataResultToIntent(RemoteInput remoteInput, Intent intent, Map<String, Uri> map) {
            RemoteInputCompatJellybean.addDataResultToIntent(remoteInput, intent, map);
        }

        @Override
        public void addResultsToIntent(RemoteInput[] arrremoteInput, Intent intent, Bundle bundle) {
            RemoteInputCompatJellybean.addResultsToIntent(arrremoteInput, intent, bundle);
        }

        @Override
        public Map<String, Uri> getDataResultsFromIntent(Intent intent, String string2) {
            return RemoteInputCompatJellybean.getDataResultsFromIntent(intent, string2);
        }

        @Override
        public Bundle getResultsFromIntent(Intent intent) {
            return RemoteInputCompatJellybean.getResultsFromIntent(intent);
        }
    }

}

