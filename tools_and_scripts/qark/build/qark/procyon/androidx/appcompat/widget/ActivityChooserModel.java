// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import java.math.BigDecimal;
import android.content.ComponentName;
import java.util.Collections;
import java.util.Collection;
import android.os.AsyncTask;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.content.Context;
import java.util.List;
import java.util.Map;
import android.database.DataSetObservable;

class ActivityChooserModel extends DataSetObservable
{
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
    static final String LOG_TAG;
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities;
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    boolean mCanReadHistoricalData;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords;
    private boolean mHistoricalRecordsChanged;
    final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock;
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;
    private boolean mReloadActivities;
    
    static {
        LOG_TAG = ActivityChooserModel.class.getSimpleName();
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }
    
    private ActivityChooserModel(final Context context, final String mHistoryFileName) {
        this.mInstanceLock = new Object();
        this.mActivities = new ArrayList<ActivityResolveInfo>();
        this.mHistoricalRecords = new ArrayList<HistoricalRecord>();
        this.mActivitySorter = (ActivitySorter)new DefaultSorter();
        this.mHistoryMaxSize = 50;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = false;
        this.mHistoricalRecordsChanged = true;
        this.mReloadActivities = false;
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)mHistoryFileName) && !mHistoryFileName.endsWith(".xml")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(mHistoryFileName);
            sb.append(".xml");
            this.mHistoryFileName = sb.toString();
            return;
        }
        this.mHistoryFileName = mHistoryFileName;
    }
    
    private boolean addHistoricalRecord(final HistoricalRecord historicalRecord) {
        final boolean add = this.mHistoricalRecords.add(historicalRecord);
        if (add) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return add;
    }
    
    private void ensureConsistentState() {
        final boolean loadActivitiesIfNeeded = this.loadActivitiesIfNeeded();
        final boolean historicalDataIfNeeded = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (loadActivitiesIfNeeded | historicalDataIfNeeded) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }
    
    public static ActivityChooserModel get(final Context context, final String s) {
        synchronized (ActivityChooserModel.sRegistryLock) {
            ActivityChooserModel activityChooserModel;
            if ((activityChooserModel = ActivityChooserModel.sDataModelRegistry.get(s)) == null) {
                activityChooserModel = new ActivityChooserModel(context, s);
                ActivityChooserModel.sDataModelRegistry.put(s, activityChooserModel);
            }
            return activityChooserModel;
        }
    }
    
    private boolean loadActivitiesIfNeeded() {
        if (this.mReloadActivities && this.mIntent != null) {
            this.mReloadActivities = false;
            this.mActivities.clear();
            final List queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
            for (int size = queryIntentActivities.size(), i = 0; i < size; ++i) {
                this.mActivities.add(new ActivityResolveInfo(queryIntentActivities.get(i)));
            }
            return true;
        }
        return false;
    }
    
    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (!this.mHistoricalRecordsChanged) {
            return;
        }
        this.mHistoricalRecordsChanged = false;
        if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[] { new ArrayList(this.mHistoricalRecords), this.mHistoryFileName });
        }
    }
    
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        final int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n <= 0) {
            return;
        }
        this.mHistoricalRecordsChanged = true;
        for (int i = 0; i < n; ++i) {
            final HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0);
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
    
    private void readHistoricalDataImpl() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        androidx/appcompat/widget/ActivityChooserModel.mContext:Landroid/content/Context;
        //     4: aload_0        
        //     5: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //     8: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
        //    11: astore_2       
        //    12: invokestatic    android/util/Xml.newPullParser:()Lorg/xmlpull/v1/XmlPullParser;
        //    15: astore_3       
        //    16: aload_3        
        //    17: aload_2        
        //    18: ldc_w           "UTF-8"
        //    21: invokeinterface org/xmlpull/v1/XmlPullParser.setInput:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    26: iconst_0       
        //    27: istore_1       
        //    28: iload_1        
        //    29: iconst_1       
        //    30: if_icmpeq       48
        //    33: iload_1        
        //    34: iconst_2       
        //    35: if_icmpeq       48
        //    38: aload_3        
        //    39: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    44: istore_1       
        //    45: goto            28
        //    48: ldc             "historical-records"
        //    50: aload_3        
        //    51: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //    56: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    59: ifeq            187
        //    62: aload_0        
        //    63: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoricalRecords:Ljava/util/List;
        //    66: astore          4
        //    68: aload           4
        //    70: invokeinterface java/util/List.clear:()V
        //    75: aload_3        
        //    76: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    81: istore_1       
        //    82: iload_1        
        //    83: iconst_1       
        //    84: if_icmpne       98
        //    87: aload_2        
        //    88: ifnull          316
        //    91: aload_2        
        //    92: invokevirtual   java/io/FileInputStream.close:()V
        //    95: goto            313
        //    98: iload_1        
        //    99: iconst_3       
        //   100: if_icmpeq       75
        //   103: iload_1        
        //   104: iconst_4       
        //   105: if_icmpne       111
        //   108: goto            75
        //   111: ldc             "historical-record"
        //   113: aload_3        
        //   114: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //   119: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   122: ifeq            176
        //   125: aload           4
        //   127: new             Landroidx/appcompat/widget/ActivityChooserModel$HistoricalRecord;
        //   130: dup            
        //   131: aload_3        
        //   132: aconst_null    
        //   133: ldc             "activity"
        //   135: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   140: aload_3        
        //   141: aconst_null    
        //   142: ldc             "time"
        //   144: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   149: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   152: aload_3        
        //   153: aconst_null    
        //   154: ldc             "weight"
        //   156: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   161: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
        //   164: invokespecial   androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord.<init>:(Ljava/lang/String;JF)V
        //   167: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   172: pop            
        //   173: goto            75
        //   176: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   179: dup            
        //   180: ldc_w           "Share records file not well-formed."
        //   183: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   186: athrow         
        //   187: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   190: dup            
        //   191: ldc_w           "Share records file does not start with historical-records tag."
        //   194: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   197: athrow         
        //   198: astore_3       
        //   199: goto            317
        //   202: astore_3       
        //   203: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   206: astore          4
        //   208: new             Ljava/lang/StringBuilder;
        //   211: dup            
        //   212: invokespecial   java/lang/StringBuilder.<init>:()V
        //   215: astore          5
        //   217: aload           5
        //   219: ldc_w           "Error reading historical recrod file: "
        //   222: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   225: pop            
        //   226: aload           5
        //   228: aload_0        
        //   229: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   232: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   235: pop            
        //   236: aload           4
        //   238: aload           5
        //   240: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   243: aload_3        
        //   244: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   247: pop            
        //   248: aload_2        
        //   249: ifnull          316
        //   252: aload_2        
        //   253: invokevirtual   java/io/FileInputStream.close:()V
        //   256: goto            313
        //   259: astore_3       
        //   260: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   263: astore          4
        //   265: new             Ljava/lang/StringBuilder;
        //   268: dup            
        //   269: invokespecial   java/lang/StringBuilder.<init>:()V
        //   272: astore          5
        //   274: aload           5
        //   276: ldc_w           "Error reading historical recrod file: "
        //   279: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   282: pop            
        //   283: aload           5
        //   285: aload_0        
        //   286: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   289: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   292: pop            
        //   293: aload           4
        //   295: aload           5
        //   297: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   300: aload_3        
        //   301: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   304: pop            
        //   305: aload_2        
        //   306: ifnull          316
        //   309: aload_2        
        //   310: invokevirtual   java/io/FileInputStream.close:()V
        //   313: return         
        //   314: astore_2       
        //   315: return         
        //   316: return         
        //   317: aload_2        
        //   318: ifnull          329
        //   321: aload_2        
        //   322: invokevirtual   java/io/FileInputStream.close:()V
        //   325: goto            329
        //   328: astore_2       
        //   329: aload_3        
        //   330: athrow         
        //   331: astore_2       
        //   332: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      12     331    333    Ljava/io/FileNotFoundException;
        //  12     26     259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  12     26     202    259    Ljava/io/IOException;
        //  12     26     198    331    Any
        //  38     45     259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  38     45     202    259    Ljava/io/IOException;
        //  38     45     198    331    Any
        //  48     75     259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  48     75     202    259    Ljava/io/IOException;
        //  48     75     198    331    Any
        //  75     82     259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  75     82     202    259    Ljava/io/IOException;
        //  75     82     198    331    Any
        //  91     95     314    316    Ljava/io/IOException;
        //  111    173    259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  111    173    202    259    Ljava/io/IOException;
        //  111    173    198    331    Any
        //  176    187    259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  176    187    202    259    Ljava/io/IOException;
        //  176    187    198    331    Any
        //  187    198    259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  187    198    202    259    Ljava/io/IOException;
        //  187    198    198    331    Any
        //  203    248    198    331    Any
        //  252    256    314    316    Ljava/io/IOException;
        //  260    305    198    331    Any
        //  309    313    314    316    Ljava/io/IOException;
        //  321    325    328    329    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0098:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList((List<? extends HistoricalRecord>)this.mHistoricalRecords));
            return true;
        }
        return false;
    }
    
    public Intent chooseActivity(final int n) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ComponentName component = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            final Intent intent = new Intent(this.mIntent);
            intent.setComponent(component);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(intent))) {
                return null;
            }
            this.addHistoricalRecord(new HistoricalRecord(component, System.currentTimeMillis(), 1.0f));
            return intent;
        }
    }
    
    public ResolveInfo getActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.get(n).resolveInfo;
        }
    }
    
    public int getActivityCount() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }
    
    public int getActivityIndex(final ResolveInfo resolveInfo) {
        while (true) {
            while (true) {
                int n;
                synchronized (this.mInstanceLock) {
                    this.ensureConsistentState();
                    final List<ActivityResolveInfo> mActivities = this.mActivities;
                    final int size = mActivities.size();
                    n = 0;
                    if (n >= size) {
                        return -1;
                    }
                    if (mActivities.get(n).resolveInfo == resolveInfo) {
                        return n;
                    }
                }
                ++n;
                continue;
            }
        }
    }
    
    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            if (!this.mActivities.isEmpty()) {
                return this.mActivities.get(0).resolveInfo;
            }
            return null;
        }
    }
    
    public int getHistoryMaxSize() {
        synchronized (this.mInstanceLock) {
            return this.mHistoryMaxSize;
        }
    }
    
    public int getHistorySize() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }
    
    public Intent getIntent() {
        synchronized (this.mInstanceLock) {
            return this.mIntent;
        }
    }
    
    public void setActivitySorter(final ActivitySorter mActivitySorter) {
        synchronized (this.mInstanceLock) {
            if (this.mActivitySorter == mActivitySorter) {
                return;
            }
            this.mActivitySorter = mActivitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setDefaultActivity(final int n) {
        while (true) {
            while (true) {
                synchronized (this.mInstanceLock) {
                    this.ensureConsistentState();
                    final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
                    final ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
                    if (activityResolveInfo2 != null) {
                        final float n2 = activityResolveInfo2.weight - activityResolveInfo.weight + 5.0f;
                        this.addHistoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), n2));
                        return;
                    }
                }
                final float n2 = 1.0f;
                continue;
            }
        }
    }
    
    public void setHistoryMaxSize(final int mHistoryMaxSize) {
        synchronized (this.mInstanceLock) {
            if (this.mHistoryMaxSize == mHistoryMaxSize) {
                return;
            }
            this.mHistoryMaxSize = mHistoryMaxSize;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setIntent(final Intent mIntent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == mIntent) {
                return;
            }
            this.mIntent = mIntent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
        }
    }
    
    public void setOnChooseActivityListener(final OnChooseActivityListener mActivityChoserModelPolicy) {
        synchronized (this.mInstanceLock) {
            this.mActivityChoserModelPolicy = mActivityChoserModelPolicy;
        }
    }
    
    public interface ActivityChooserModelClient
    {
        void setActivityChooserModel(final ActivityChooserModel p0);
    }
    
    public static final class ActivityResolveInfo implements Comparable<ActivityResolveInfo>
    {
        public final ResolveInfo resolveInfo;
        public float weight;
        
        public ActivityResolveInfo(final ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }
        
        @Override
        public int compareTo(final ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && this.getClass() == o.getClass() && Float.floatToIntBits(this.weight) == Float.floatToIntBits(((ActivityResolveInfo)o).weight));
        }
        
        @Override
        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("resolveInfo:");
            sb.append(this.resolveInfo.toString());
            sb.append("; weight:");
            sb.append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface ActivitySorter
    {
        void sort(final Intent p0, final List<ActivityResolveInfo> p1, final List<HistoricalRecord> p2);
    }
    
    private static final class DefaultSorter implements ActivitySorter
    {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap;
        
        DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();
        }
        
        @Override
        public void sort(final Intent intent, final List<ActivityResolveInfo> list, final List<HistoricalRecord> list2) {
            final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = this.mPackageNameToActivityMap;
            mPackageNameToActivityMap.clear();
            for (int size = list.size(), i = 0; i < size; ++i) {
                final ActivityResolveInfo activityResolveInfo = list.get(i);
                activityResolveInfo.weight = 0.0f;
                mPackageNameToActivityMap.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
            }
            final int size2 = list2.size();
            float n = 1.0f;
            float n2;
            for (int j = size2 - 1; j >= 0; --j, n = n2) {
                final HistoricalRecord historicalRecord = list2.get(j);
                final ActivityResolveInfo activityResolveInfo2 = mPackageNameToActivityMap.get(historicalRecord.activity);
                n2 = n;
                if (activityResolveInfo2 != null) {
                    activityResolveInfo2.weight += historicalRecord.weight * n;
                    n2 = n * 0.95f;
                }
            }
            Collections.sort((List<Comparable>)list);
        }
    }
    
    public static final class HistoricalRecord
    {
        public final ComponentName activity;
        public final long time;
        public final float weight;
        
        public HistoricalRecord(final ComponentName activity, final long time, final float weight) {
            this.activity = activity;
            this.time = time;
            this.weight = weight;
        }
        
        public HistoricalRecord(final String s, final long n, final float n2) {
            this(ComponentName.unflattenFromString(s), n, n2);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final HistoricalRecord historicalRecord = (HistoricalRecord)o;
            final ComponentName activity = this.activity;
            if (activity == null) {
                if (historicalRecord.activity != null) {
                    return false;
                }
            }
            else if (!activity.equals((Object)historicalRecord.activity)) {
                return false;
            }
            return this.time == historicalRecord.time && Float.floatToIntBits(this.weight) == Float.floatToIntBits(historicalRecord.weight);
        }
        
        @Override
        public int hashCode() {
            final ComponentName activity = this.activity;
            int hashCode;
            if (activity == null) {
                hashCode = 0;
            }
            else {
                hashCode = activity.hashCode();
            }
            final long time = this.time;
            return ((1 * 31 + hashCode) * 31 + (int)(time ^ time >>> 32)) * 31 + Float.floatToIntBits(this.weight);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("; activity:");
            sb.append(this.activity);
            sb.append("; time:");
            sb.append(this.time);
            sb.append("; weight:");
            sb.append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface OnChooseActivityListener
    {
        boolean onChooseActivity(final ActivityChooserModel p0, final Intent p1);
    }
    
    private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void>
    {
        PersistHistoryAsyncTask() {
        }
        
        public Void doInBackground(final Object... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: iconst_0       
            //     2: aaload         
            //     3: checkcast       Ljava/util/List;
            //     6: astore          4
            //     8: aload_1        
            //     9: iconst_1       
            //    10: aaload         
            //    11: checkcast       Ljava/lang/String;
            //    14: astore_1       
            //    15: aload_0        
            //    16: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //    19: getfield        androidx/appcompat/widget/ActivityChooserModel.mContext:Landroid/content/Context;
            //    22: aload_1        
            //    23: iconst_0       
            //    24: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    27: astore          6
            //    29: invokestatic    android/util/Xml.newSerializer:()Lorg/xmlpull/v1/XmlSerializer;
            //    32: astore          7
            //    34: aload           4
            //    36: astore          5
            //    38: aload           4
            //    40: astore          5
            //    42: aload           4
            //    44: astore          5
            //    46: aload           4
            //    48: astore          5
            //    50: aload           7
            //    52: aload           6
            //    54: aconst_null    
            //    55: invokeinterface org/xmlpull/v1/XmlSerializer.setOutput:(Ljava/io/OutputStream;Ljava/lang/String;)V
            //    60: aload           4
            //    62: astore          5
            //    64: aload           4
            //    66: astore          5
            //    68: aload           4
            //    70: astore          5
            //    72: aload           4
            //    74: astore          5
            //    76: aload           7
            //    78: ldc             "UTF-8"
            //    80: iconst_1       
            //    81: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    84: invokeinterface org/xmlpull/v1/XmlSerializer.startDocument:(Ljava/lang/String;Ljava/lang/Boolean;)V
            //    89: aload           4
            //    91: astore          5
            //    93: aload           4
            //    95: astore          5
            //    97: aload           4
            //    99: astore          5
            //   101: aload           4
            //   103: astore          5
            //   105: aload           7
            //   107: aconst_null    
            //   108: ldc             "historical-records"
            //   110: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   115: pop            
            //   116: aload           4
            //   118: astore          5
            //   120: aload           4
            //   122: astore          5
            //   124: aload           4
            //   126: astore          5
            //   128: aload           4
            //   130: astore          5
            //   132: aload           4
            //   134: invokeinterface java/util/List.size:()I
            //   139: istore_3       
            //   140: iconst_0       
            //   141: istore_2       
            //   142: aload           4
            //   144: astore_1       
            //   145: iload_2        
            //   146: iload_3        
            //   147: if_icmpge       284
            //   150: aload_1        
            //   151: astore          5
            //   153: aload_1        
            //   154: astore          5
            //   156: aload_1        
            //   157: astore          5
            //   159: aload_1        
            //   160: astore          5
            //   162: aload_1        
            //   163: iconst_0       
            //   164: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //   169: checkcast       Landroidx/appcompat/widget/ActivityChooserModel$HistoricalRecord;
            //   172: astore          4
            //   174: aload_1        
            //   175: astore          5
            //   177: aload_1        
            //   178: astore          5
            //   180: aload_1        
            //   181: astore          5
            //   183: aload_1        
            //   184: astore          5
            //   186: aload           7
            //   188: aconst_null    
            //   189: ldc             "historical-record"
            //   191: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   196: pop            
            //   197: aload_1        
            //   198: astore          5
            //   200: aload_1        
            //   201: astore          5
            //   203: aload_1        
            //   204: astore          5
            //   206: aload_1        
            //   207: astore          5
            //   209: aload           7
            //   211: aconst_null    
            //   212: ldc             "activity"
            //   214: aload           4
            //   216: getfield        androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord.activity:Landroid/content/ComponentName;
            //   219: invokevirtual   android/content/ComponentName.flattenToString:()Ljava/lang/String;
            //   222: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   227: pop            
            //   228: aload           7
            //   230: aconst_null    
            //   231: ldc             "time"
            //   233: aload           4
            //   235: getfield        androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord.time:J
            //   238: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
            //   241: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   246: pop            
            //   247: aload           7
            //   249: aconst_null    
            //   250: ldc             "weight"
            //   252: aload           4
            //   254: getfield        androidx/appcompat/widget/ActivityChooserModel$HistoricalRecord.weight:F
            //   257: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
            //   260: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   265: pop            
            //   266: aload           7
            //   268: aconst_null    
            //   269: ldc             "historical-record"
            //   271: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   276: pop            
            //   277: iload_2        
            //   278: iconst_1       
            //   279: iadd           
            //   280: istore_2       
            //   281: goto            145
            //   284: aload           7
            //   286: aconst_null    
            //   287: ldc             "historical-records"
            //   289: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   294: pop            
            //   295: aload           7
            //   297: invokeinterface org/xmlpull/v1/XmlSerializer.endDocument:()V
            //   302: aload_0        
            //   303: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   306: iconst_1       
            //   307: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   310: aload           6
            //   312: ifnull          548
            //   315: aload           6
            //   317: invokevirtual   java/io/FileOutputStream.close:()V
            //   320: goto            543
            //   323: astore_1       
            //   324: goto            340
            //   327: astore_1       
            //   328: goto            409
            //   331: astore_1       
            //   332: goto            478
            //   335: astore_1       
            //   336: goto            551
            //   339: astore_1       
            //   340: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   343: astore          4
            //   345: new             Ljava/lang/StringBuilder;
            //   348: dup            
            //   349: invokespecial   java/lang/StringBuilder.<init>:()V
            //   352: astore          5
            //   354: aload           5
            //   356: ldc             "Error writing historical record file: "
            //   358: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   361: pop            
            //   362: aload           5
            //   364: aload_0        
            //   365: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   368: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   371: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   374: pop            
            //   375: aload           4
            //   377: aload           5
            //   379: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   382: aload_1        
            //   383: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   386: pop            
            //   387: aload_0        
            //   388: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   391: iconst_1       
            //   392: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   395: aload           6
            //   397: ifnull          548
            //   400: aload           6
            //   402: invokevirtual   java/io/FileOutputStream.close:()V
            //   405: goto            543
            //   408: astore_1       
            //   409: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   412: astore          4
            //   414: new             Ljava/lang/StringBuilder;
            //   417: dup            
            //   418: invokespecial   java/lang/StringBuilder.<init>:()V
            //   421: astore          5
            //   423: aload           5
            //   425: ldc             "Error writing historical record file: "
            //   427: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   430: pop            
            //   431: aload           5
            //   433: aload_0        
            //   434: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   437: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   440: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   443: pop            
            //   444: aload           4
            //   446: aload           5
            //   448: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   451: aload_1        
            //   452: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   455: pop            
            //   456: aload_0        
            //   457: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   460: iconst_1       
            //   461: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   464: aload           6
            //   466: ifnull          548
            //   469: aload           6
            //   471: invokevirtual   java/io/FileOutputStream.close:()V
            //   474: goto            543
            //   477: astore_1       
            //   478: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   481: astore          4
            //   483: new             Ljava/lang/StringBuilder;
            //   486: dup            
            //   487: invokespecial   java/lang/StringBuilder.<init>:()V
            //   490: astore          5
            //   492: aload           5
            //   494: ldc             "Error writing historical record file: "
            //   496: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   499: pop            
            //   500: aload           5
            //   502: aload_0        
            //   503: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   506: getfield        androidx/appcompat/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   509: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   512: pop            
            //   513: aload           4
            //   515: aload           5
            //   517: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   520: aload_1        
            //   521: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   524: pop            
            //   525: aload_0        
            //   526: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   529: iconst_1       
            //   530: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   533: aload           6
            //   535: ifnull          548
            //   538: aload           6
            //   540: invokevirtual   java/io/FileOutputStream.close:()V
            //   543: aconst_null    
            //   544: areturn        
            //   545: astore_1       
            //   546: aconst_null    
            //   547: areturn        
            //   548: aconst_null    
            //   549: areturn        
            //   550: astore_1       
            //   551: aload_0        
            //   552: getfield        androidx/appcompat/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroidx/appcompat/widget/ActivityChooserModel;
            //   555: iconst_1       
            //   556: putfield        androidx/appcompat/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   559: aload           6
            //   561: ifnull          574
            //   564: aload           6
            //   566: invokevirtual   java/io/FileOutputStream.close:()V
            //   569: goto            574
            //   572: astore          4
            //   574: aload_1        
            //   575: athrow         
            //   576: astore          4
            //   578: getstatic       androidx/appcompat/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   581: astore          5
            //   583: new             Ljava/lang/StringBuilder;
            //   586: dup            
            //   587: invokespecial   java/lang/StringBuilder.<init>:()V
            //   590: astore          6
            //   592: aload           6
            //   594: ldc             "Error writing historical record file: "
            //   596: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   599: pop            
            //   600: aload           6
            //   602: aload_1        
            //   603: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   606: pop            
            //   607: aload           5
            //   609: aload           6
            //   611: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   614: aload           4
            //   616: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   619: pop            
            //   620: aconst_null    
            //   621: areturn        
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  15     29     576    622    Ljava/io/FileNotFoundException;
            //  50     60     477    478    Ljava/lang/IllegalArgumentException;
            //  50     60     408    409    Ljava/lang/IllegalStateException;
            //  50     60     339    340    Ljava/io/IOException;
            //  50     60     335    339    Any
            //  76     89     477    478    Ljava/lang/IllegalArgumentException;
            //  76     89     408    409    Ljava/lang/IllegalStateException;
            //  76     89     339    340    Ljava/io/IOException;
            //  76     89     335    339    Any
            //  105    116    477    478    Ljava/lang/IllegalArgumentException;
            //  105    116    408    409    Ljava/lang/IllegalStateException;
            //  105    116    339    340    Ljava/io/IOException;
            //  105    116    335    339    Any
            //  132    140    477    478    Ljava/lang/IllegalArgumentException;
            //  132    140    408    409    Ljava/lang/IllegalStateException;
            //  132    140    339    340    Ljava/io/IOException;
            //  132    140    335    339    Any
            //  162    174    477    478    Ljava/lang/IllegalArgumentException;
            //  162    174    408    409    Ljava/lang/IllegalStateException;
            //  162    174    339    340    Ljava/io/IOException;
            //  162    174    335    339    Any
            //  186    197    477    478    Ljava/lang/IllegalArgumentException;
            //  186    197    408    409    Ljava/lang/IllegalStateException;
            //  186    197    339    340    Ljava/io/IOException;
            //  186    197    335    339    Any
            //  209    228    477    478    Ljava/lang/IllegalArgumentException;
            //  209    228    408    409    Ljava/lang/IllegalStateException;
            //  209    228    339    340    Ljava/io/IOException;
            //  209    228    335    339    Any
            //  228    277    331    335    Ljava/lang/IllegalArgumentException;
            //  228    277    327    331    Ljava/lang/IllegalStateException;
            //  228    277    323    327    Ljava/io/IOException;
            //  228    277    550    551    Any
            //  284    302    331    335    Ljava/lang/IllegalArgumentException;
            //  284    302    327    331    Ljava/lang/IllegalStateException;
            //  284    302    323    327    Ljava/io/IOException;
            //  284    302    550    551    Any
            //  315    320    545    548    Ljava/io/IOException;
            //  340    387    550    551    Any
            //  400    405    545    548    Ljava/io/IOException;
            //  409    456    550    551    Any
            //  469    474    545    548    Ljava/io/IOException;
            //  478    525    550    551    Any
            //  538    543    545    548    Ljava/io/IOException;
            //  564    569    572    574    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0284:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
}
