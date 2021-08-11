/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.database.DataSetObservable
 *  android.os.AsyncTask
 *  android.text.TextUtils
 *  android.util.Log
 *  android.util.Xml
 *  org.xmlpull.v1.XmlSerializer
 */
package androidx.appcompat.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import org.xmlpull.v1.XmlSerializer;

class ActivityChooserModel
extends DataSetObservable {
    static final String ATTRIBUTE_ACTIVITY = "activity";
    static final String ATTRIBUTE_TIME = "time";
    static final String ATTRIBUTE_WEIGHT = "weight";
    static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities = new ArrayList<ActivityResolveInfo>();
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter = new DefaultSorter();
    boolean mCanReadHistoricalData = true;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords = new ArrayList<HistoricalRecord>();
    private boolean mHistoricalRecordsChanged = true;
    final String mHistoryFileName;
    private int mHistoryMaxSize = 50;
    private final Object mInstanceLock = new Object();
    private Intent mIntent;
    private boolean mReadShareHistoryCalled = false;
    private boolean mReloadActivities = false;

    static {
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }

    private ActivityChooserModel(Context object, String string) {
        this.mContext = object.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)string) && !string.endsWith(".xml")) {
            object = new StringBuilder();
            object.append(string);
            object.append(".xml");
            this.mHistoryFileName = object.toString();
            return;
        }
        this.mHistoryFileName = string;
    }

    private boolean addHistoricalRecord(HistoricalRecord historicalRecord) {
        boolean bl = this.mHistoricalRecords.add(historicalRecord);
        if (bl) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return bl;
    }

    private void ensureConsistentState() {
        boolean bl = this.loadActivitiesIfNeeded();
        boolean bl2 = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (bl | bl2) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ActivityChooserModel get(Context context, String string) {
        Object object = sRegistryLock;
        synchronized (object) {
            ActivityChooserModel activityChooserModel;
            ActivityChooserModel activityChooserModel2 = activityChooserModel = sDataModelRegistry.get(string);
            if (activityChooserModel == null) {
                activityChooserModel2 = new ActivityChooserModel(context, string);
                sDataModelRegistry.put(string, activityChooserModel2);
            }
            return activityChooserModel2;
        }
    }

    private boolean loadActivitiesIfNeeded() {
        if (this.mReloadActivities && this.mIntent != null) {
            this.mReloadActivities = false;
            this.mActivities.clear();
            List list = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                ResolveInfo resolveInfo = (ResolveInfo)list.get(i);
                this.mActivities.add(new ActivityResolveInfo(resolveInfo));
            }
            return true;
        }
        return false;
    }

    private void persistHistoricalDataIfNeeded() {
        if (this.mReadShareHistoryCalled) {
            if (!this.mHistoricalRecordsChanged) {
                return;
            }
            this.mHistoricalRecordsChanged = false;
            if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
                new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[]{new ArrayList<HistoricalRecord>(this.mHistoricalRecords), this.mHistoryFileName});
            }
            return;
        }
        throw new IllegalStateException("No preceding call to #readHistoricalData");
    }

    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n <= 0) {
            return;
        }
        this.mHistoricalRecordsChanged = true;
        for (int i = 0; i < n; ++i) {
            HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0);
        }
    }

    private boolean readHistoricalDataIfNeeded() {
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
            return true;
        }
        return false;
    }

    /*
     * Exception decompiling
     */
    private void readHistoricalDataImpl() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 16[UNCONDITIONALDOLOOP]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Intent chooseActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            Intent intent;
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            activityResolveInfo = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            Intent intent2 = new Intent(this.mIntent);
            intent2.setComponent((ComponentName)activityResolveInfo);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, intent = new Intent(intent2))) {
                return null;
            }
            this.addHistoricalRecord(new HistoricalRecord((ComponentName)activityResolveInfo, System.currentTimeMillis(), 1.0f));
            return intent2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ResolveInfo getActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mActivities.get((int)n).resolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getActivityCount() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getActivityIndex(ResolveInfo resolveInfo) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            List<ActivityResolveInfo> list = this.mActivities;
            int n = list.size();
            int n2 = 0;
            while (n2 < n) {
                if (list.get((int)n2).resolveInfo == resolveInfo) {
                    return n2;
                }
                ++n2;
            }
            return -1;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ResolveInfo getDefaultActivity() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            if (this.mActivities.isEmpty()) return null;
            return this.mActivities.get((int)0).resolveInfo;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getHistoryMaxSize() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            return this.mHistoryMaxSize;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getHistorySize() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Intent getIntent() {
        Object object = this.mInstanceLock;
        synchronized (object) {
            return this.mIntent;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setActivitySorter(ActivitySorter activitySorter) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mActivitySorter == activitySorter) {
                return;
            }
            this.mActivitySorter = activitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setDefaultActivity(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.ensureConsistentState();
            ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
            float f = activityResolveInfo2 != null ? activityResolveInfo2.weight - activityResolveInfo.weight + 5.0f : 1.0f;
            this.addHistoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), f));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setHistoryMaxSize(int n) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mHistoryMaxSize == n) {
                return;
            }
            this.mHistoryMaxSize = n;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setIntent(Intent intent) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            if (this.mIntent == intent) {
                return;
            }
            this.mIntent = intent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setOnChooseActivityListener(OnChooseActivityListener onChooseActivityListener) {
        Object object = this.mInstanceLock;
        synchronized (object) {
            this.mActivityChoserModelPolicy = onChooseActivityListener;
            return;
        }
    }

    public static interface ActivityChooserModelClient {
        public void setActivityChooserModel(ActivityChooserModel var1);
    }

    public static final class ActivityResolveInfo
    implements Comparable<ActivityResolveInfo> {
        public final ResolveInfo resolveInfo;
        public float weight;

        public ActivityResolveInfo(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }

        @Override
        public int compareTo(ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (ActivityResolveInfo)object;
            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(object.weight)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append("resolveInfo:");
            stringBuilder.append(this.resolveInfo.toString());
            stringBuilder.append("; weight:");
            stringBuilder.append(new BigDecimal(this.weight));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static interface ActivitySorter {
        public void sort(Intent var1, List<ActivityResolveInfo> var2, List<HistoricalRecord> var3);
    }

    private static final class DefaultSorter
    implements ActivitySorter {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();

        DefaultSorter() {
        }

        @Override
        public void sort(Intent object, List<ActivityResolveInfo> list, List<HistoricalRecord> list2) {
            int n;
            Object object2;
            object = this.mPackageNameToActivityMap;
            object.clear();
            int n2 = list.size();
            for (n = 0; n < n2; ++n) {
                object2 = list.get(n);
                object2.weight = 0.0f;
                object.put(new ComponentName(object2.resolveInfo.activityInfo.packageName, object2.resolveInfo.activityInfo.name), object2);
            }
            n = list2.size();
            float f = 1.0f;
            --n;
            while (n >= 0) {
                object2 = list2.get(n);
                ActivityResolveInfo activityResolveInfo = (ActivityResolveInfo)object.get((Object)object2.activity);
                float f2 = f;
                if (activityResolveInfo != null) {
                    activityResolveInfo.weight += object2.weight * f;
                    f2 = f * 0.95f;
                }
                --n;
                f = f2;
            }
            Collections.sort(list);
        }
    }

    public static final class HistoricalRecord {
        public final ComponentName activity;
        public final long time;
        public final float weight;

        public HistoricalRecord(ComponentName componentName, long l, float f) {
            this.activity = componentName;
            this.time = l;
            this.weight = f;
        }

        public HistoricalRecord(String string, long l, float f) {
            this(ComponentName.unflattenFromString((String)string), l, f);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (HistoricalRecord)object;
            ComponentName componentName = this.activity;
            if (componentName == null ? object.activity != null : !componentName.equals((Object)object.activity)) {
                return false;
            }
            if (this.time != object.time) {
                return false;
            }
            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(object.weight)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            ComponentName componentName = this.activity;
            int n = componentName == null ? 0 : componentName.hashCode();
            long l = this.time;
            return ((1 * 31 + n) * 31 + (int)(l ^ l >>> 32)) * 31 + Float.floatToIntBits(this.weight);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append("; activity:");
            stringBuilder.append((Object)this.activity);
            stringBuilder.append("; time:");
            stringBuilder.append(this.time);
            stringBuilder.append("; weight:");
            stringBuilder.append(new BigDecimal(this.weight));
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static interface OnChooseActivityListener {
        public boolean onChooseActivity(ActivityChooserModel var1, Intent var2);
    }

    private final class PersistHistoryAsyncTask
    extends AsyncTask<Object, Void, Void> {
        PersistHistoryAsyncTask() {
        }

        /*
         * Loose catch block
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public /* varargs */ Void doInBackground(Object ... object) {
            FileOutputStream fileOutputStream;
            void var1_11;
            block33 : {
                Object object2;
                Object object3;
                block32 : {
                    block31 : {
                        block30 : {
                            object2 = (List)object[0];
                            object = (String)object[1];
                            fileOutputStream = ActivityChooserModel.this.mContext.openFileOutput((String)object, 0);
                            XmlSerializer xmlSerializer = Xml.newSerializer();
                            object3 = object2;
                            object3 = object2;
                            object3 = object2;
                            object3 = object2;
                            xmlSerializer.setOutput((OutputStream)fileOutputStream, null);
                            object3 = object2;
                            object3 = object2;
                            object3 = object2;
                            object3 = object2;
                            xmlSerializer.startDocument("UTF-8", Boolean.valueOf(true));
                            object3 = object2;
                            object3 = object2;
                            object3 = object2;
                            object3 = object2;
                            xmlSerializer.startTag(null, "historical-records");
                            object3 = object2;
                            object3 = object2;
                            object3 = object2;
                            object3 = object2;
                            int n = object2.size();
                            object = object2;
                            for (int i = 0; i < n; ++i) {
                                object3 = object;
                                object3 = object;
                                object3 = object;
                                object3 = object;
                                object2 = (HistoricalRecord)object.remove(0);
                                object3 = object;
                                object3 = object;
                                object3 = object;
                                object3 = object;
                                xmlSerializer.startTag(null, "historical-record");
                                object3 = object;
                                object3 = object;
                                object3 = object;
                                object3 = object;
                                xmlSerializer.attribute(null, "activity", object2.activity.flattenToString());
                                xmlSerializer.attribute(null, "time", String.valueOf(object2.time));
                                xmlSerializer.attribute(null, "weight", String.valueOf(object2.weight));
                                xmlSerializer.endTag(null, "historical-record");
                            }
                            xmlSerializer.endTag(null, "historical-records");
                            xmlSerializer.endDocument();
                            ActivityChooserModel.this.mCanReadHistoricalData = true;
                            if (fileOutputStream == null) return null;
                            try {
                                fileOutputStream.close();
                                return null;
                            }
                            catch (IOException iOException) {
                                return null;
                            }
                            catch (IOException iOException) {
                                break block30;
                            }
                            catch (IllegalStateException illegalStateException) {
                                break block31;
                            }
                            catch (IllegalArgumentException illegalArgumentException) {
                                break block32;
                            }
                            catch (Throwable throwable) {
                                break block33;
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        object2 = ActivityChooserModel.LOG_TAG;
                        object3 = new StringBuilder();
                        object3.append("Error writing historical record file: ");
                        object3.append(ActivityChooserModel.this.mHistoryFileName);
                        Log.e((String)object2, (String)object3.toString(), (Throwable)object);
                        ActivityChooserModel.this.mCanReadHistoricalData = true;
                        if (fileOutputStream == null) return null;
                        fileOutputStream.close();
                        return null;
                        catch (IllegalStateException illegalStateException) {
                            // empty catch block
                        }
                    }
                    object2 = ActivityChooserModel.LOG_TAG;
                    object3 = new StringBuilder();
                    object3.append("Error writing historical record file: ");
                    object3.append(ActivityChooserModel.this.mHistoryFileName);
                    Log.e((String)object2, (String)object3.toString(), (Throwable)object);
                    ActivityChooserModel.this.mCanReadHistoricalData = true;
                    if (fileOutputStream == null) return null;
                    fileOutputStream.close();
                    return null;
                    catch (IllegalArgumentException illegalArgumentException) {
                        // empty catch block
                    }
                }
                object2 = ActivityChooserModel.LOG_TAG;
                object3 = new StringBuilder();
                object3.append("Error writing historical record file: ");
                object3.append(ActivityChooserModel.this.mHistoryFileName);
                Log.e((String)object2, (String)object3.toString(), (Throwable)object);
                ActivityChooserModel.this.mCanReadHistoricalData = true;
                if (fileOutputStream == null) return null;
                fileOutputStream.close();
                return null;
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            ActivityChooserModel.this.mCanReadHistoricalData = true;
            if (fileOutputStream == null) throw var1_11;
            try {
                fileOutputStream.close();
                throw var1_11;
            }
            catch (IOException iOException) {
                // empty catch block
            }
            throw var1_11;
            catch (FileNotFoundException fileNotFoundException) {
                String string = ActivityChooserModel.LOG_TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error writing historical record file: ");
                stringBuilder.append((String)object);
                Log.e((String)string, (String)stringBuilder.toString(), (Throwable)fileNotFoundException);
                return null;
            }
        }
    }

}

