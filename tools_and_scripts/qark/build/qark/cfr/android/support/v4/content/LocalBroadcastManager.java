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
package android.support.v4.content;

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
    private void executePendingBroadcasts() {
        block3 : do {
            BroadcastRecord[] arrbroadcastRecord;
            int n;
            Object object = this.mReceivers;
            synchronized (object) {
                n = this.mPendingBroadcasts.size();
                if (n <= 0) {
                    return;
                }
                arrbroadcastRecord = new BroadcastRecord[n];
                this.mPendingBroadcasts.toArray(arrbroadcastRecord);
                this.mPendingBroadcasts.clear();
            }
            n = 0;
            do {
                if (n >= arrbroadcastRecord.length) continue block3;
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
     */
    public void registerReceiver(BroadcastReceiver arrayList, IntentFilter intentFilter) {
        HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, (BroadcastReceiver)arrayList);
            Object object = this.mReceivers.get(arrayList);
            if (object == null) {
                object = new ArrayList(1);
                this.mReceivers.put((BroadcastReceiver)arrayList, (ArrayList<ReceiverRecord>)object);
                arrayList = object;
            } else {
                arrayList = object;
            }
            arrayList.add(receiverRecord);
            int n = 0;
            while (n < intentFilter.countActions()) {
                object = intentFilter.getAction(n);
                arrayList = this.mActions.get(object);
                if (arrayList == null) {
                    arrayList = new ArrayList(1);
                    this.mActions.put((String)object, arrayList);
                }
                arrayList.add(receiverRecord);
                ++n;
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean sendBroadcast(Intent intent) {
        HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> hashMap = this.mReceivers;
        synchronized (hashMap) {
            ArrayList<ReceiverRecord> arrayList;
            ArrayList arrayList2;
            String string2 = intent.getAction();
            String string3 = intent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
            Uri uri = intent.getData();
            String string4 = intent.getScheme();
            Set set = intent.getCategories();
            int n = (intent.getFlags() & 8) != 0 ? 1 : 0;
            if (n != 0) {
                arrayList2 = new StringBuilder();
                arrayList2.append("Resolving type ");
                arrayList2.append(string3);
                arrayList2.append(" scheme ");
                arrayList2.append(string4);
                arrayList2.append(" of intent ");
                arrayList2.append((Object)intent);
                Log.v((String)"LocalBroadcastManager", (String)arrayList2.toString());
            }
            if ((arrayList = this.mActions.get(intent.getAction())) == null) return false;
            if (n != 0) {
                arrayList2 = new StringBuilder();
                arrayList2.append("Action list: ");
                arrayList2.append(arrayList);
                Log.v((String)"LocalBroadcastManager", (String)arrayList2.toString());
            }
            ArrayList arrayList3 = null;
            int n2 = 0;
            while (n2 < arrayList.size()) {
                Object object = arrayList.get(n2);
                if (n != 0) {
                    arrayList2 = new StringBuilder();
                    arrayList2.append("Matching against filter ");
                    arrayList2.append((Object)object.filter);
                    Log.v((String)"LocalBroadcastManager", (String)arrayList2.toString());
                }
                if (object.broadcasting) {
                    if (n != 0) {
                        Log.v((String)"LocalBroadcastManager", (String)"  Filter's target already added");
                    }
                } else {
                    IntentFilter intentFilter = object.filter;
                    arrayList2 = arrayList3;
                    int n3 = intentFilter.match(string2, string3, string4, uri, set, "LocalBroadcastManager");
                    if (n3 >= 0) {
                        if (n != 0) {
                            arrayList3 = new StringBuilder();
                            arrayList3.append("  Filter matched!  match=0x");
                            arrayList3.append(Integer.toHexString(n3));
                            Log.v((String)"LocalBroadcastManager", (String)arrayList3.toString());
                        }
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                        }
                        arrayList2.add(object);
                        object.broadcasting = true;
                        arrayList3 = arrayList2;
                    } else if (n != 0) {
                        switch (n3) {
                            default: {
                                arrayList2 = "unknown reason";
                                break;
                            }
                            case -1: {
                                arrayList2 = "type";
                                break;
                            }
                            case -2: {
                                arrayList2 = "data";
                                break;
                            }
                            case -3: {
                                arrayList2 = "action";
                                break;
                            }
                            case -4: {
                                arrayList2 = "category";
                            }
                        }
                        object = new StringBuilder();
                        object.append("  Filter did not match: ");
                        object.append((String)((Object)arrayList2));
                        Log.v((String)"LocalBroadcastManager", (String)object.toString());
                    }
                }
                ++n2;
            }
            return false;
        }
    }

    public void sendBroadcastSync(Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
            return;
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

