// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import java.util.Set;
import android.net.Uri;
import android.util.Log;
import android.content.Intent;
import java.io.Serializable;
import android.content.IntentFilter;
import android.os.Message;
import android.os.Looper;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

public final class LocalBroadcastManager
{
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers;
    
    static {
        mLock = new Object();
    }
    
    private LocalBroadcastManager(final Context mAppContext) {
        this.mReceivers = new HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>>();
        this.mActions = new HashMap<String, ArrayList<ReceiverRecord>>();
        this.mPendingBroadcasts = new ArrayList<BroadcastRecord>();
        this.mAppContext = mAppContext;
        this.mHandler = new Handler(mAppContext.getMainLooper()) {
            public void handleMessage(final Message message) {
                if (message.what != 1) {
                    super.handleMessage(message);
                    return;
                }
                LocalBroadcastManager.this.executePendingBroadcasts();
            }
        };
    }
    
    private void executePendingBroadcasts() {
        while (true) {
            Object mReceivers = this.mReceivers;
            synchronized (mReceivers) {
                final int size = this.mPendingBroadcasts.size();
                if (size <= 0) {
                    return;
                }
                final BroadcastRecord[] array = new BroadcastRecord[size];
                this.mPendingBroadcasts.toArray(array);
                this.mPendingBroadcasts.clear();
                // monitorexit(mReceivers)
                for (int i = 0; i < array.length; ++i) {
                    mReceivers = array[i];
                    for (int size2 = ((BroadcastRecord)mReceivers).receivers.size(), j = 0; j < size2; ++j) {
                        final ReceiverRecord receiverRecord = ((BroadcastRecord)mReceivers).receivers.get(j);
                        if (!receiverRecord.dead) {
                            receiverRecord.receiver.onReceive(this.mAppContext, ((BroadcastRecord)mReceivers).intent);
                        }
                    }
                }
            }
        }
    }
    
    public static LocalBroadcastManager getInstance(final Context context) {
        while (true) {
            while (true) {
                Label_0042: {
                    synchronized (LocalBroadcastManager.mLock) {
                        if (LocalBroadcastManager.mInstance == null) {
                            LocalBroadcastManager.mInstance = new LocalBroadcastManager(context.getApplicationContext());
                            return LocalBroadcastManager.mInstance;
                        }
                        break Label_0042;
                    }
                }
                continue;
            }
        }
    }
    
    public void registerReceiver(final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter) {
    Label_0129_Outer:
        while (true) {
            while (true) {
            Label_0159:
                while (true) {
                    Object action;
                    synchronized (this.mReceivers) {
                        final ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, broadcastReceiver);
                        action = this.mReceivers.get(broadcastReceiver);
                        if (action == null) {
                            action = new ArrayList<Object>(1);
                            this.mReceivers.put(broadcastReceiver, (ArrayList<ReceiverRecord>)action);
                            final Serializable s = (Serializable)action;
                            ((ArrayList<ReceiverRecord>)s).add(receiverRecord);
                            for (int i = 0; i < intentFilter.countActions(); ++i) {
                                action = intentFilter.getAction(i);
                                if (this.mActions.get(action) != null) {
                                    break Label_0159;
                                }
                                final ArrayList<ReceiverRecord> list = new ArrayList<ReceiverRecord>(1);
                                this.mActions.put((String)action, list);
                                list.add(receiverRecord);
                            }
                            return;
                        }
                    }
                    final Serializable s = (Serializable)action;
                    continue Label_0129_Outer;
                }
                continue;
            }
        }
    }
    
    public boolean sendBroadcast(final Intent intent) {
        String action;
        String resolveTypeIfNeeded;
        Uri data;
        String scheme;
        Set categories;
        int n = 0;
        Serializable s;
        ArrayList<ReceiverRecord> list;
        Serializable s2 = null;
        int match = 0;
        Object o;
        IntentFilter filter;
        int n2 = 0;
        Label_0377_Outer:Label_0437_Outer:
        while (true) {
            Label_0437:Label_0196_Outer:
            while (true) {
            Label_0196:
                while (true) {
                    Label_0618: {
                        while (true) {
                        Label_0377:
                            while (true) {
                            Label_0528:
                                while (true) {
                                    Label_0523: {
                                        synchronized (this.mReceivers) {
                                            action = intent.getAction();
                                            resolveTypeIfNeeded = intent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                                            data = intent.getData();
                                            scheme = intent.getScheme();
                                            categories = intent.getCategories();
                                            if ((intent.getFlags() & 0x8) == 0x0) {
                                                break Label_0523;
                                            }
                                            n = 1;
                                            if (n != 0) {
                                                s = new StringBuilder();
                                                ((StringBuilder)s).append("Resolving type ");
                                                ((StringBuilder)s).append(resolveTypeIfNeeded);
                                                ((StringBuilder)s).append(" scheme ");
                                                ((StringBuilder)s).append(scheme);
                                                ((StringBuilder)s).append(" of intent ");
                                                ((StringBuilder)s).append(intent);
                                                Log.v("LocalBroadcastManager", ((StringBuilder)s).toString());
                                            }
                                            list = this.mActions.get(intent.getAction());
                                            if (list == null) {
                                                return false;
                                            }
                                            if (n != 0) {
                                                s = new StringBuilder();
                                                ((StringBuilder)s).append("Action list: ");
                                                ((StringBuilder)s).append(list);
                                                Log.v("LocalBroadcastManager", ((StringBuilder)s).toString());
                                            }
                                            break Label_0528;
                                            // iftrue(Label_0536:, n == 0)
                                            // iftrue(Label_0635:, this.mHandler.hasMessages(1))
                                            // iftrue(Label_0542:, match < 0)
                                            // iftrue(Label_0282:, !o.broadcasting)
                                            // iftrue(Label_0539:, s != null)
                                            // iftrue(Label_0625:, n2 >= list.size())
                                            // iftrue(Label_0466:, n >= s2.size())
                                            // iftrue(Label_0360:, n == 0)
                                            while (true) {
                                            Label_0259_Outer:
                                                while (true) {
                                                    s2 = new StringBuilder();
                                                    ((StringBuilder)s2).append("  Filter matched!  match=0x");
                                                    ((StringBuilder)s2).append(Integer.toHexString(match));
                                                    Log.v("LocalBroadcastManager", ((StringBuilder)s2).toString());
                                                    Block_13: {
                                                        while (true) {
                                                            Block_17: {
                                                                Block_12: {
                                                                Block_15_Outer:
                                                                    while (true) {
                                                                        while (true) {
                                                                            Label_0360: {
                                                                                break Label_0360;
                                                                                while (true) {
                                                                                    break Block_12;
                                                                                    Label_0466: {
                                                                                        this.mPendingBroadcasts.add(new BroadcastRecord(intent, (ArrayList<ReceiverRecord>)s2));
                                                                                    }
                                                                                    break Block_17;
                                                                                    Label_0282:
                                                                                    filter = ((ReceiverRecord)o).filter;
                                                                                    s = s2;
                                                                                    match = filter.match(action, resolveTypeIfNeeded, scheme, data, categories, "LocalBroadcastManager");
                                                                                    break Block_13;
                                                                                    continue Label_0259_Outer;
                                                                                }
                                                                                s = new ArrayList();
                                                                                break Label_0377;
                                                                            }
                                                                            continue Label_0377_Outer;
                                                                        }
                                                                        s = new StringBuilder();
                                                                        ((StringBuilder)s).append("Matching against filter ");
                                                                        ((StringBuilder)s).append(((ReceiverRecord)o).filter);
                                                                        Log.v("LocalBroadcastManager", ((StringBuilder)s).toString());
                                                                        continue Block_15_Outer;
                                                                    }
                                                                    ((ArrayList<ReceiverRecord>)s).add((ReceiverRecord)o);
                                                                    ((ReceiverRecord)o).broadcasting = true;
                                                                    s2 = s;
                                                                    break Label_0618;
                                                                    ((ReceiverRecord)((ArrayList<ReceiverRecord>)s2).get(n)).broadcasting = false;
                                                                    ++n;
                                                                    break Label_0437;
                                                                    o = new StringBuilder();
                                                                    ((StringBuilder)o).append("  Filter did not match: ");
                                                                    ((StringBuilder)o).append((String)s);
                                                                    Log.v("LocalBroadcastManager", ((StringBuilder)o).toString());
                                                                    break Label_0618;
                                                                }
                                                                Log.v("LocalBroadcastManager", "  Filter's target already added");
                                                                break Label_0618;
                                                            }
                                                            this.mHandler.sendEmptyMessage(1);
                                                            return true;
                                                            break Label_0259_Outer;
                                                            continue Label_0437_Outer;
                                                        }
                                                    }
                                                    continue Label_0377_Outer;
                                                }
                                                o = list.get(n2);
                                                continue Label_0437_Outer;
                                            }
                                        }
                                        // iftrue(Label_0259:, n == 0)
                                    }
                                    n = 0;
                                    continue Label_0377_Outer;
                                }
                                s2 = null;
                                n2 = 0;
                                continue Label_0196;
                                Label_0536: {
                                    break Label_0618;
                                }
                                Label_0539:
                                continue Label_0377;
                            }
                            Label_0542: {
                                if (n != 0) {
                                    switch (match) {
                                        default: {
                                            s = "unknown reason";
                                            continue Label_0196_Outer;
                                        }
                                        case -1: {
                                            s = "type";
                                            continue Label_0196_Outer;
                                        }
                                        case -2: {
                                            s = "data";
                                            continue Label_0196_Outer;
                                        }
                                        case -3: {
                                            s = "action";
                                            continue Label_0196_Outer;
                                        }
                                        case -4: {
                                            s = "category";
                                            continue Label_0196_Outer;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    ++n2;
                    continue Label_0196;
                }
                Label_0625: {
                    if (s2 != null) {
                        n = 0;
                        continue Label_0437;
                    }
                }
                break;
            }
            return false;
            Label_0635: {
                return true;
            }
        }
    }
    
    public void sendBroadcastSync(final Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }
    
    public void unregisterReceiver(final BroadcastReceiver broadcastReceiver) {
        while (true) {
        Label_0062_Outer:
            while (true) {
                int n = 0;
            Label_0206:
                while (true) {
                    int n2 = 0;
                    Label_0196: {
                        while (true) {
                            int n3;
                            synchronized (this.mReceivers) {
                                final ArrayList<ReceiverRecord> list = this.mReceivers.remove(broadcastReceiver);
                                if (list == null) {
                                    return;
                                }
                                n = list.size() - 1;
                                if (n < 0) {
                                    return;
                                }
                                final ReceiverRecord receiverRecord = list.get(n);
                                receiverRecord.dead = true;
                                n2 = 0;
                                if (n2 >= receiverRecord.filter.countActions()) {
                                    break Label_0206;
                                }
                                final String action = receiverRecord.filter.getAction(n2);
                                final ArrayList<ReceiverRecord> list2 = this.mActions.get(action);
                                if (list2 == null) {
                                    break Label_0196;
                                }
                                n3 = list2.size() - 1;
                                if (n3 >= 0) {
                                    final ReceiverRecord receiverRecord2 = list2.get(n3);
                                    if (receiverRecord2.receiver == broadcastReceiver) {
                                        receiverRecord2.dead = true;
                                        list2.remove(n3);
                                    }
                                }
                                else {
                                    if (list2.size() <= 0) {
                                        this.mActions.remove(action);
                                    }
                                    break Label_0196;
                                }
                            }
                            --n3;
                            continue;
                        }
                    }
                    ++n2;
                    continue;
                }
                --n;
                continue Label_0062_Outer;
            }
        }
    }
    
    private static final class BroadcastRecord
    {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;
        
        BroadcastRecord(final Intent intent, final ArrayList<ReceiverRecord> receivers) {
            this.intent = intent;
            this.receivers = receivers;
        }
    }
    
    private static final class ReceiverRecord
    {
        boolean broadcasting;
        boolean dead;
        final IntentFilter filter;
        final BroadcastReceiver receiver;
        
        ReceiverRecord(final IntentFilter filter, final BroadcastReceiver receiver) {
            this.filter = filter;
            this.receiver = receiver;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(128);
            sb.append("Receiver{");
            sb.append(this.receiver);
            sb.append(" filter=");
            sb.append(this.filter);
            if (this.dead) {
                sb.append(" DEAD");
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
