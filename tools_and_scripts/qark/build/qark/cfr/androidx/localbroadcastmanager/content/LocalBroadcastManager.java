/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.net.Uri
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 */
package androidx.localbroadcastmanager.content;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap();
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList();
    private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers = new HashMap();

    static {
        mLock = new Object();
    }

    private LocalBroadcastManager(Context context) {
        this.mAppContext = context;
        this.mHandler = new Handler(context.getMainLooper()){

            public void handleMessage(Message message) {
                if (message.what != 1) {
                    super.handleMessage(message);
                    return;
                }
                LocalBroadcastManager.this.executePendingBroadcasts();
            }
        };
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static LocalBroadcastManager getInstance(Context object) {
        Object object2 = mLock;
        synchronized (object2) {
            if (mInstance != null) return mInstance;
            mInstance = new LocalBroadcastManager(object.getApplicationContext());
            return mInstance;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    void executePendingBroadcasts() {
        block5 : do {
            Object object = this.mReceivers;
            // MONITORENTER : object
            int n = this.mPendingBroadcasts.size();
            if (n <= 0) {
                // MONITOREXIT : object
                return;
            }
            BroadcastRecord[] arrbroadcastRecord = new BroadcastRecord[n];
            this.mPendingBroadcasts.toArray(arrbroadcastRecord);
            this.mPendingBroadcasts.clear();
            // MONITOREXIT : object
            n = 0;
            do {
                if (n >= arrbroadcastRecord.length) continue block5;
                object = arrbroadcastRecord[n];
                int n2 = object.receivers.size();
                for (int i = 0; i < n2; ++i) {
                    ReceiverRecord receiverRecord = object.receivers.get(i);
                    if (receiverRecord.dead) continue;
                    receiverRecord.receiver.onReceive(this.mAppContext, object.intent);
                }
                ++n;
            } while (true);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void registerReceiver(BroadcastReceiver arrayList, IntentFilter intentFilter) {
        HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, (BroadcastReceiver)arrayList);
            Object object = this.mReceivers.get(arrayList);
            ArrayList arrayList2 = object;
            if (object == null) {
                arrayList2 = new ArrayList(1);
                this.mReceivers.put((BroadcastReceiver)arrayList, arrayList2);
            }
            arrayList2.add(receiverRecord);
            int n = 0;
            while (n < intentFilter.countActions()) {
                object = intentFilter.getAction(n);
                arrayList2 = this.mActions.get(object);
                arrayList = arrayList2;
                if (arrayList2 == null) {
                    arrayList = new ArrayList<ReceiverRecord>(1);
                    this.mActions.put((String)object, arrayList);
                }
                arrayList.add(receiverRecord);
                ++n;
            }
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean sendBroadcast(Intent var1_1) {
        block13 : {
            var8_2 = this.mReceivers;
            // MONITORENTER : var8_2
            var9_3 = var1_1.getAction();
            var7_4 = var1_1.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
            var10_5 = var1_1.getData();
            var11_6 = var1_1.getScheme();
            var12_7 = var1_1.getCategories();
            var2_8 = (var1_1.getFlags() & 8) != 0 ? 1 : 0;
            if (var2_8 != 0) {
                var5_9 = new StringBuilder();
                var5_9.append("Resolving type ");
                var5_9.append(var7_4);
                var5_9.append(" scheme ");
                var5_9.append(var11_6);
                var5_9.append(" of intent ");
                var5_9.append((Object)var1_1);
                Log.v((String)"LocalBroadcastManager", (String)var5_9.toString());
            }
            if ((var13_10 = this.mActions.get(var1_1.getAction())) == null) break block13;
            if (var2_8 != 0) {
                var5_9 = new StringBuilder();
                var5_9.append("Action list: ");
                var5_9.append(var13_10);
                Log.v((String)"LocalBroadcastManager", (String)var5_9.toString());
            }
            var6_13 /* !! */  = null;
            for (var3_11 = 0; var3_11 < var13_10.size(); ++var3_11) {
                block14 : {
                    var14_14 = var13_10.get(var3_11);
                    if (var2_8 != 0) {
                        var5_9 = new StringBuilder();
                        var5_9.append("Matching against filter ");
                        var5_9.append((Object)var14_14.filter);
                        Log.v((String)"LocalBroadcastManager", (String)var5_9.toString());
                    }
                    if (!var14_14.broadcasting) break block14;
                    if (var2_8 != 0) {
                        Log.v((String)"LocalBroadcastManager", (String)"  Filter's target already added");
                    }
                    ** GOTO lbl57
                }
                var15_15 = var14_14.filter;
                var5_9 = var6_13 /* !! */ ;
                var4_12 = var15_15.match(var9_3, var7_4, var11_6, var10_5, var12_7, "LocalBroadcastManager");
                if (var4_12 >= 0) {
                    if (var2_8 != 0) {
                        var6_13 /* !! */  = new StringBuilder();
                        var6_13 /* !! */ .append("  Filter matched!  match=0x");
                        var6_13 /* !! */ .append(Integer.toHexString(var4_12));
                        Log.v((String)"LocalBroadcastManager", (String)var6_13 /* !! */ .toString());
                    }
                    if (var5_9 == null) {
                        var5_9 = new ArrayList<E>();
                    }
                    var5_9.add(var14_14);
                    var14_14.broadcasting = true;
                } else {
                    if (var2_8 != 0) {
                        var5_9 = var4_12 != -4 ? (var4_12 != -3 ? (var4_12 != -2 ? (var4_12 != -1 ? "unknown reason" : "type") : "data") : "action") : "category";
                        var14_14 = new StringBuilder();
                        var14_14.append("  Filter did not match: ");
                        var14_14.append((String)var5_9);
                        Log.v((String)"LocalBroadcastManager", (String)var14_14.toString());
                    }
lbl57: // 4 sources:
                    var5_9 = var6_13 /* !! */ ;
                }
                var6_13 /* !! */  = var5_9;
            }
        }
        // MONITOREXIT : var8_2
        return false;
    }

    public void sendBroadcastSync(Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ArrayList<ReceiverRecord> arrayList = this.mReceivers.remove((Object)broadcastReceiver);
            if (arrayList == null) {
                return;
            }
            int n = arrayList.size() - 1;
            block2 : while (n >= 0) {
                ReceiverRecord receiverRecord = arrayList.get(n);
                receiverRecord.dead = true;
                int n2 = 0;
                do {
                    block11 : {
                        String string2;
                        ArrayList<ReceiverRecord> arrayList2;
                        block12 : {
                            block10 : {
                                if (n2 >= receiverRecord.filter.countActions()) break block10;
                                string2 = receiverRecord.filter.getAction(n2);
                                arrayList2 = this.mActions.get(string2);
                                if (arrayList2 == null) break block11;
                                break block12;
                            }
                            --n;
                            continue block2;
                        }
                        int n3 = arrayList2.size() - 1;
                        do {
                            if (n3 >= 0) {
                                ReceiverRecord receiverRecord2 = arrayList2.get(n3);
                                if (receiverRecord2.receiver == broadcastReceiver) {
                                    receiverRecord2.dead = true;
                                    arrayList2.remove(n3);
                                }
                            } else {
                                if (arrayList2.size() > 0) break;
                                this.mActions.remove(string2);
                                break;
                            }
                            --n3;
                        } while (true);
                    }
                    ++n2;
                } while (true);
                break;
            }
            return;
        }
    }

    private static final class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent intent, ArrayList<ReceiverRecord> arrayList) {
            this.intent = intent;
            this.receivers = arrayList;
        }
    }

    private static final class ReceiverRecord {
        boolean broadcasting;
        boolean dead;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter intentFilter, BroadcastReceiver broadcastReceiver) {
            this.filter = intentFilter;
            this.receiver = broadcastReceiver;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(128);
            stringBuilder.append("Receiver{");
            stringBuilder.append((Object)this.receiver);
            stringBuilder.append(" filter=");
            stringBuilder.append((Object)this.filter);
            if (this.dead) {
                stringBuilder.append(" DEAD");
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}

