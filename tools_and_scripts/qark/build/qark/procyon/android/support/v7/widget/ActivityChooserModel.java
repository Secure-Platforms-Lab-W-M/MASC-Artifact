// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

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
            return add;
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
        while (true) {
            final ActivityChooserModel activityChooserModel;
            synchronized (ActivityChooserModel.sRegistryLock) {
                activityChooserModel = ActivityChooserModel.sDataModelRegistry.get(s);
                if (activityChooserModel == null) {
                    final ActivityChooserModel activityChooserModel2 = new ActivityChooserModel(context, s);
                    ActivityChooserModel.sDataModelRegistry.put(s, activityChooserModel2);
                    return activityChooserModel2;
                }
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
        if (!this.mCanReadHistoricalData || !this.mHistoricalRecordsChanged) {
            return false;
        }
        if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
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
        //     1: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
        //     4: aload_0        
        //     5: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
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
        //    63: getfield        android/support/v7/widget/ActivityChooserModel.mHistoricalRecords:Ljava/util/List;
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
        //   100: if_icmpeq       336
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
        //   127: new             Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
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
        //   164: invokespecial   android/support/v7/widget/ActivityChooserModel$HistoricalRecord.<init>:(Ljava/lang/String;JF)V
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
        //   203: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
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
        //   229: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
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
        //   260: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
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
        //   286: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
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
        //   318: ifnull          332
        //   321: aload_2        
        //   322: invokevirtual   java/io/FileInputStream.close:()V
        //   325: goto            332
        //   328: astore_2       
        //   329: goto            332
        //   332: aload_3        
        //   333: athrow         
        //   334: astore_2       
        //   335: return         
        //   336: goto            75
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      12     334    336    Ljava/io/FileNotFoundException;
        //  12     26     259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  12     26     202    259    Ljava/io/IOException;
        //  12     26     198    334    Any
        //  38     45     259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  38     45     202    259    Ljava/io/IOException;
        //  38     45     198    334    Any
        //  48     75     259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  48     75     202    259    Ljava/io/IOException;
        //  48     75     198    334    Any
        //  75     82     259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  75     82     202    259    Ljava/io/IOException;
        //  75     82     198    334    Any
        //  91     95     314    316    Ljava/io/IOException;
        //  111    173    259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  111    173    202    259    Ljava/io/IOException;
        //  111    173    198    334    Any
        //  176    187    259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  176    187    202    259    Ljava/io/IOException;
        //  176    187    198    334    Any
        //  187    198    259    313    Lorg/xmlpull/v1/XmlPullParserException;
        //  187    198    202    259    Ljava/io/IOException;
        //  187    198    198    334    Any
        //  203    248    198    334    Any
        //  252    256    314    316    Ljava/io/IOException;
        //  260    305    198    334    Any
        //  309    313    314    316    Ljava/io/IOException;
        //  321    325    328    332    Ljava/io/IOException;
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
        if (this.mActivitySorter != null && this.mIntent != null && (!this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty())) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList((List<? extends HistoricalRecord>)this.mHistoricalRecords));
            return true;
        }
        return false;
    }
    
    public Intent chooseActivity(final int n) {
        while (true) {
            while (true) {
                Label_0151: {
                    Label_0148: {
                        synchronized (this.mInstanceLock) {
                            if (this.mIntent == null) {
                                return null;
                            }
                            this.ensureConsistentState();
                            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
                            final ComponentName component = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
                            final Intent intent = new Intent(this.mIntent);
                            intent.setComponent(component);
                            if (this.mActivityChoserModelPolicy == null) {
                                break Label_0151;
                            }
                            if (this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(intent))) {
                                return null;
                            }
                            break Label_0148;
                            final ComponentName componentName;
                            this.addHistoricalRecord(new HistoricalRecord(componentName, System.currentTimeMillis(), 1.0f));
                            return intent;
                        }
                    }
                    continue;
                }
                continue;
            }
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
        while (true) {
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
        while (true) {
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
            for (int j = size2 - 1; j >= 0; --j) {
                final HistoricalRecord historicalRecord = list2.get(j);
                final ActivityResolveInfo activityResolveInfo2 = mPackageNameToActivityMap.get(historicalRecord.activity);
                if (activityResolveInfo2 != null) {
                    activityResolveInfo2.weight += historicalRecord.weight * n;
                    n *= 0.95f;
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
            //    14: astore          5
            //    16: aload_0        
            //    17: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //    20: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
            //    23: aload           5
            //    25: iconst_0       
            //    26: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    29: astore_1       
            //    30: invokestatic    android/util/Xml.newSerializer:()Lorg/xmlpull/v1/XmlSerializer;
            //    33: astore          5
            //    35: aload           5
            //    37: aload_1        
            //    38: aconst_null    
            //    39: invokeinterface org/xmlpull/v1/XmlSerializer.setOutput:(Ljava/io/OutputStream;Ljava/lang/String;)V
            //    44: aload           5
            //    46: ldc             "UTF-8"
            //    48: iconst_1       
            //    49: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    52: invokeinterface org/xmlpull/v1/XmlSerializer.startDocument:(Ljava/lang/String;Ljava/lang/Boolean;)V
            //    57: aload           5
            //    59: aconst_null    
            //    60: ldc             "historical-records"
            //    62: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //    67: pop            
            //    68: aload           4
            //    70: invokeinterface java/util/List.size:()I
            //    75: istore_3       
            //    76: iconst_0       
            //    77: istore_2       
            //    78: iload_2        
            //    79: iload_3        
            //    80: if_icmpge       182
            //    83: aload           4
            //    85: iconst_0       
            //    86: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //    91: checkcast       Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
            //    94: astore          6
            //    96: aload           5
            //    98: aconst_null    
            //    99: ldc             "historical-record"
            //   101: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   106: pop            
            //   107: aload           5
            //   109: aconst_null    
            //   110: ldc             "activity"
            //   112: aload           6
            //   114: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.activity:Landroid/content/ComponentName;
            //   117: invokevirtual   android/content/ComponentName.flattenToString:()Ljava/lang/String;
            //   120: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   125: pop            
            //   126: aload           5
            //   128: aconst_null    
            //   129: ldc             "time"
            //   131: aload           6
            //   133: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.time:J
            //   136: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
            //   139: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   144: pop            
            //   145: aload           5
            //   147: aconst_null    
            //   148: ldc             "weight"
            //   150: aload           6
            //   152: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.weight:F
            //   155: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
            //   158: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   163: pop            
            //   164: aload           5
            //   166: aconst_null    
            //   167: ldc             "historical-record"
            //   169: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   174: pop            
            //   175: iload_2        
            //   176: iconst_1       
            //   177: iadd           
            //   178: istore_2       
            //   179: goto            78
            //   182: aload           5
            //   184: aconst_null    
            //   185: ldc             "historical-records"
            //   187: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   192: pop            
            //   193: aload           5
            //   195: invokeinterface org/xmlpull/v1/XmlSerializer.endDocument:()V
            //   200: aload_0        
            //   201: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   204: iconst_1       
            //   205: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   208: aload_1        
            //   209: ifnull          433
            //   212: aload_1        
            //   213: invokevirtual   java/io/FileOutputStream.close:()V
            //   216: goto            428
            //   219: astore          4
            //   221: goto            435
            //   224: astore          4
            //   226: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   229: astore          5
            //   231: new             Ljava/lang/StringBuilder;
            //   234: dup            
            //   235: invokespecial   java/lang/StringBuilder.<init>:()V
            //   238: astore          6
            //   240: aload           6
            //   242: ldc             "Error writing historical record file: "
            //   244: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   247: pop            
            //   248: aload           6
            //   250: aload_0        
            //   251: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   254: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   257: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   260: pop            
            //   261: aload           5
            //   263: aload           6
            //   265: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   268: aload           4
            //   270: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   273: pop            
            //   274: aload_0        
            //   275: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   278: iconst_1       
            //   279: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   282: aload_1        
            //   283: ifnull          433
            //   286: aload_1        
            //   287: invokevirtual   java/io/FileOutputStream.close:()V
            //   290: goto            428
            //   293: astore          4
            //   295: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   298: astore          5
            //   300: new             Ljava/lang/StringBuilder;
            //   303: dup            
            //   304: invokespecial   java/lang/StringBuilder.<init>:()V
            //   307: astore          6
            //   309: aload           6
            //   311: ldc             "Error writing historical record file: "
            //   313: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   316: pop            
            //   317: aload           6
            //   319: aload_0        
            //   320: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   323: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   326: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   329: pop            
            //   330: aload           5
            //   332: aload           6
            //   334: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   337: aload           4
            //   339: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   342: pop            
            //   343: aload_0        
            //   344: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   347: iconst_1       
            //   348: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   351: aload_1        
            //   352: ifnull          433
            //   355: aload_1        
            //   356: invokevirtual   java/io/FileOutputStream.close:()V
            //   359: goto            428
            //   362: astore          4
            //   364: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   367: astore          5
            //   369: new             Ljava/lang/StringBuilder;
            //   372: dup            
            //   373: invokespecial   java/lang/StringBuilder.<init>:()V
            //   376: astore          6
            //   378: aload           6
            //   380: ldc             "Error writing historical record file: "
            //   382: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   385: pop            
            //   386: aload           6
            //   388: aload_0        
            //   389: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   392: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   395: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   398: pop            
            //   399: aload           5
            //   401: aload           6
            //   403: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   406: aload           4
            //   408: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   411: pop            
            //   412: aload_0        
            //   413: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   416: iconst_1       
            //   417: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   420: aload_1        
            //   421: ifnull          433
            //   424: aload_1        
            //   425: invokevirtual   java/io/FileOutputStream.close:()V
            //   428: aconst_null    
            //   429: areturn        
            //   430: astore_1       
            //   431: aconst_null    
            //   432: areturn        
            //   433: aconst_null    
            //   434: areturn        
            //   435: aload_0        
            //   436: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   439: iconst_1       
            //   440: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   443: aload_1        
            //   444: ifnull          458
            //   447: aload_1        
            //   448: invokevirtual   java/io/FileOutputStream.close:()V
            //   451: goto            458
            //   454: astore_1       
            //   455: goto            458
            //   458: aload           4
            //   460: athrow         
            //   461: astore_1       
            //   462: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   465: astore          4
            //   467: new             Ljava/lang/StringBuilder;
            //   470: dup            
            //   471: invokespecial   java/lang/StringBuilder.<init>:()V
            //   474: astore          6
            //   476: aload           6
            //   478: ldc             "Error writing historical record file: "
            //   480: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   483: pop            
            //   484: aload           6
            //   486: aload           5
            //   488: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   491: pop            
            //   492: aload           4
            //   494: aload           6
            //   496: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   499: aload_1        
            //   500: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   503: pop            
            //   504: aconst_null    
            //   505: areturn        
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  16     30     461    506    Ljava/io/FileNotFoundException;
            //  35     76     362    428    Ljava/lang/IllegalArgumentException;
            //  35     76     293    362    Ljava/lang/IllegalStateException;
            //  35     76     224    293    Ljava/io/IOException;
            //  35     76     219    461    Any
            //  83     175    362    428    Ljava/lang/IllegalArgumentException;
            //  83     175    293    362    Ljava/lang/IllegalStateException;
            //  83     175    224    293    Ljava/io/IOException;
            //  83     175    219    461    Any
            //  182    200    362    428    Ljava/lang/IllegalArgumentException;
            //  182    200    293    362    Ljava/lang/IllegalStateException;
            //  182    200    224    293    Ljava/io/IOException;
            //  182    200    219    461    Any
            //  212    216    430    433    Ljava/io/IOException;
            //  226    274    219    461    Any
            //  286    290    430    433    Ljava/io/IOException;
            //  295    343    219    461    Any
            //  355    359    430    433    Ljava/io/IOException;
            //  364    412    219    461    Any
            //  424    428    430    433    Ljava/io/IOException;
            //  447    451    454    458    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index: 241, Size: 241
            //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
            //     at java.util.ArrayList.get(ArrayList.java:433)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3435)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
