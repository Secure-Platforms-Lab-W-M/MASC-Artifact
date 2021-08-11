// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.content.IntentFilter;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Looper;
import android.net.ConnectivityManager;
import java.util.Collection;
import android.net.NetworkInfo;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.WeakHashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.Set;
import android.os.Handler;
import java.util.Map;
import android.content.Context;
import java.util.List;

class Dispatcher
{
    static final int AIRPLANE_MODE_CHANGE = 10;
    private static final int AIRPLANE_MODE_OFF = 0;
    private static final int AIRPLANE_MODE_ON = 1;
    private static final int BATCH_DELAY = 200;
    private static final String DISPATCHER_THREAD_NAME = "Dispatcher";
    static final int HUNTER_BATCH_COMPLETE = 8;
    static final int HUNTER_COMPLETE = 4;
    static final int HUNTER_DECODE_FAILED = 6;
    static final int HUNTER_DELAY_NEXT_BATCH = 7;
    static final int HUNTER_RETRY = 5;
    static final int NETWORK_STATE_CHANGE = 9;
    static final int REQUEST_BATCH_RESUME = 13;
    static final int REQUEST_CANCEL = 2;
    static final int REQUEST_GCED = 3;
    static final int REQUEST_SUBMIT = 1;
    private static final int RETRY_DELAY = 500;
    static final int TAG_PAUSE = 11;
    static final int TAG_RESUME = 12;
    boolean airplaneMode;
    final List<BitmapHunter> batch;
    final Cache cache;
    final Context context;
    final DispatcherThread dispatcherThread;
    final Downloader downloader;
    final Map<Object, Action> failedActions;
    final Handler handler;
    final Map<String, BitmapHunter> hunterMap;
    final Handler mainThreadHandler;
    final Map<Object, Action> pausedActions;
    final Set<Object> pausedTags;
    final NetworkBroadcastReceiver receiver;
    final boolean scansNetworkChanges;
    final ExecutorService service;
    final Stats stats;
    
    Dispatcher(final Context context, final ExecutorService service, final Handler mainThreadHandler, final Downloader downloader, final Cache cache, final Stats stats) {
        (this.dispatcherThread = new DispatcherThread()).start();
        Utils.flushStackLocalLeaks(this.dispatcherThread.getLooper());
        this.context = context;
        this.service = service;
        this.hunterMap = new LinkedHashMap<String, BitmapHunter>();
        this.failedActions = new WeakHashMap<Object, Action>();
        this.pausedActions = new WeakHashMap<Object, Action>();
        this.pausedTags = new HashSet<Object>();
        this.handler = new DispatcherHandler(this.dispatcherThread.getLooper(), this);
        this.downloader = downloader;
        this.mainThreadHandler = mainThreadHandler;
        this.cache = cache;
        this.stats = stats;
        this.batch = new ArrayList<BitmapHunter>(4);
        this.airplaneMode = Utils.isAirplaneModeOn(this.context);
        this.scansNetworkChanges = Utils.hasPermission(context, "android.permission.ACCESS_NETWORK_STATE");
        (this.receiver = new NetworkBroadcastReceiver(this)).register();
    }
    
    private void batch(final BitmapHunter bitmapHunter) {
        if (!bitmapHunter.isCancelled()) {
            this.batch.add(bitmapHunter);
            if (!this.handler.hasMessages(7)) {
                this.handler.sendEmptyMessageDelayed(7, 200L);
            }
        }
    }
    
    private void flushFailedActions() {
        if (!this.failedActions.isEmpty()) {
            final Iterator<Action> iterator = this.failedActions.values().iterator();
            while (iterator.hasNext()) {
                final Action action = iterator.next();
                iterator.remove();
                if (action.getPicasso().loggingEnabled) {
                    Utils.log("Dispatcher", "replaying", action.getRequest().logId());
                }
                this.performSubmit(action, false);
            }
        }
    }
    
    private void logBatch(final List<BitmapHunter> list) {
        if (list != null && !list.isEmpty() && list.get(0).getPicasso().loggingEnabled) {
            final StringBuilder sb = new StringBuilder();
            for (final BitmapHunter bitmapHunter : list) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(Utils.getLogIdsForHunter(bitmapHunter));
            }
            Utils.log("Dispatcher", "delivered", sb.toString());
        }
    }
    
    private void markForReplay(final Action action) {
        final Object target = action.getTarget();
        if (target != null) {
            action.willReplay = true;
            this.failedActions.put(target, action);
        }
    }
    
    private void markForReplay(final BitmapHunter bitmapHunter) {
        final Action action = bitmapHunter.getAction();
        if (action != null) {
            this.markForReplay(action);
        }
        final List<Action> actions = bitmapHunter.getActions();
        if (actions != null) {
            for (int i = 0; i < actions.size(); ++i) {
                this.markForReplay(actions.get(i));
            }
        }
    }
    
    void dispatchAirplaneModeChange(final boolean b) {
        final Handler handler = this.handler;
        final Handler handler2 = this.handler;
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 0;
        }
        handler.sendMessage(handler2.obtainMessage(10, n, 0));
    }
    
    void dispatchCancel(final Action action) {
        this.handler.sendMessage(this.handler.obtainMessage(2, (Object)action));
    }
    
    void dispatchComplete(final BitmapHunter bitmapHunter) {
        this.handler.sendMessage(this.handler.obtainMessage(4, (Object)bitmapHunter));
    }
    
    void dispatchFailed(final BitmapHunter bitmapHunter) {
        this.handler.sendMessage(this.handler.obtainMessage(6, (Object)bitmapHunter));
    }
    
    void dispatchNetworkStateChange(final NetworkInfo networkInfo) {
        this.handler.sendMessage(this.handler.obtainMessage(9, (Object)networkInfo));
    }
    
    void dispatchPauseTag(final Object o) {
        this.handler.sendMessage(this.handler.obtainMessage(11, o));
    }
    
    void dispatchResumeTag(final Object o) {
        this.handler.sendMessage(this.handler.obtainMessage(12, o));
    }
    
    void dispatchRetry(final BitmapHunter bitmapHunter) {
        this.handler.sendMessageDelayed(this.handler.obtainMessage(5, (Object)bitmapHunter), 500L);
    }
    
    void dispatchSubmit(final Action action) {
        this.handler.sendMessage(this.handler.obtainMessage(1, (Object)action));
    }
    
    void performAirplaneModeChange(final boolean airplaneMode) {
        this.airplaneMode = airplaneMode;
    }
    
    void performBatchComplete() {
        final ArrayList<BitmapHunter> list = new ArrayList<BitmapHunter>(this.batch);
        this.batch.clear();
        this.mainThreadHandler.sendMessage(this.mainThreadHandler.obtainMessage(8, (Object)list));
        this.logBatch(list);
    }
    
    void performCancel(Action action) {
        final String key = action.getKey();
        final BitmapHunter bitmapHunter = this.hunterMap.get(key);
        if (bitmapHunter != null) {
            bitmapHunter.detach(action);
            if (bitmapHunter.cancel()) {
                this.hunterMap.remove(key);
                if (action.getPicasso().loggingEnabled) {
                    Utils.log("Dispatcher", "canceled", action.getRequest().logId());
                }
            }
        }
        if (this.pausedTags.contains(action.getTag())) {
            this.pausedActions.remove(action.getTarget());
            if (action.getPicasso().loggingEnabled) {
                Utils.log("Dispatcher", "canceled", action.getRequest().logId(), "because paused request got canceled");
            }
        }
        action = (Action<Object>)this.failedActions.remove(action.getTarget());
        if (action != null && action.getPicasso().loggingEnabled) {
            Utils.log("Dispatcher", "canceled", action.getRequest().logId(), "from replaying");
        }
    }
    
    void performComplete(final BitmapHunter bitmapHunter) {
        if (MemoryPolicy.shouldWriteToMemoryCache(bitmapHunter.getMemoryPolicy())) {
            this.cache.set(bitmapHunter.getKey(), bitmapHunter.getResult());
        }
        this.hunterMap.remove(bitmapHunter.getKey());
        this.batch(bitmapHunter);
        if (bitmapHunter.getPicasso().loggingEnabled) {
            Utils.log("Dispatcher", "batched", Utils.getLogIdsForHunter(bitmapHunter), "for completion");
        }
    }
    
    void performError(final BitmapHunter bitmapHunter, final boolean b) {
        if (bitmapHunter.getPicasso().loggingEnabled) {
            final String logIdsForHunter = Utils.getLogIdsForHunter(bitmapHunter);
            final StringBuilder append = new StringBuilder().append("for error");
            String s;
            if (b) {
                s = " (will replay)";
            }
            else {
                s = "";
            }
            Utils.log("Dispatcher", "batched", logIdsForHunter, append.append(s).toString());
        }
        this.hunterMap.remove(bitmapHunter.getKey());
        this.batch(bitmapHunter);
    }
    
    void performNetworkStateChange(final NetworkInfo networkInfo) {
        if (this.service instanceof PicassoExecutorService) {
            ((PicassoExecutorService)this.service).adjustThreadCount(networkInfo);
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            this.flushFailedActions();
        }
    }
    
    void performPauseTag(final Object o) {
        if (this.pausedTags.add(o)) {
            final Iterator<BitmapHunter> iterator = this.hunterMap.values().iterator();
            while (iterator.hasNext()) {
                final BitmapHunter bitmapHunter = iterator.next();
                final boolean loggingEnabled = bitmapHunter.getPicasso().loggingEnabled;
                final Action action = bitmapHunter.getAction();
                final List<Action> actions = bitmapHunter.getActions();
                boolean b;
                if (actions != null && !actions.isEmpty()) {
                    b = true;
                }
                else {
                    b = false;
                }
                if (action != null || b) {
                    if (action != null && action.getTag().equals(o)) {
                        bitmapHunter.detach(action);
                        this.pausedActions.put(action.getTarget(), action);
                        if (loggingEnabled) {
                            Utils.log("Dispatcher", "paused", action.request.logId(), "because tag '" + o + "' was paused");
                        }
                    }
                    if (b) {
                        for (int i = actions.size() - 1; i >= 0; --i) {
                            final Action<Object> action2 = actions.get(i);
                            if (action2.getTag().equals(o)) {
                                bitmapHunter.detach(action2);
                                this.pausedActions.put(action2.getTarget(), action2);
                                if (loggingEnabled) {
                                    Utils.log("Dispatcher", "paused", action2.request.logId(), "because tag '" + o + "' was paused");
                                }
                            }
                        }
                    }
                    if (!bitmapHunter.cancel()) {
                        continue;
                    }
                    iterator.remove();
                    if (!loggingEnabled) {
                        continue;
                    }
                    Utils.log("Dispatcher", "canceled", Utils.getLogIdsForHunter(bitmapHunter), "all actions paused");
                }
            }
        }
    }
    
    void performResumeTag(final Object o) {
        if (this.pausedTags.remove(o)) {
            List<Action> list = null;
            final Iterator<Action> iterator = this.pausedActions.values().iterator();
            while (iterator.hasNext()) {
                final Action action = iterator.next();
                if (action.getTag().equals(o)) {
                    List<Action> list2;
                    if ((list2 = list) == null) {
                        list2 = new ArrayList<Action>();
                    }
                    list2.add(action);
                    iterator.remove();
                    list = list2;
                }
            }
            if (list != null) {
                this.mainThreadHandler.sendMessage(this.mainThreadHandler.obtainMessage(13, (Object)list));
            }
        }
    }
    
    void performRetry(final BitmapHunter bitmapHunter) {
        if (!bitmapHunter.isCancelled()) {
            if (this.service.isShutdown()) {
                this.performError(bitmapHunter, false);
                return;
            }
            NetworkInfo activeNetworkInfo = null;
            if (this.scansNetworkChanges) {
                activeNetworkInfo = Utils.getService(this.context, "connectivity").getActiveNetworkInfo();
            }
            final boolean b = activeNetworkInfo != null && activeNetworkInfo.isConnected();
            final boolean shouldRetry = bitmapHunter.shouldRetry(this.airplaneMode, activeNetworkInfo);
            final boolean supportsReplay = bitmapHunter.supportsReplay();
            if (!shouldRetry) {
                final boolean b2 = this.scansNetworkChanges && supportsReplay;
                this.performError(bitmapHunter, b2);
                if (b2) {
                    this.markForReplay(bitmapHunter);
                }
            }
            else {
                if (!this.scansNetworkChanges || b) {
                    if (bitmapHunter.getPicasso().loggingEnabled) {
                        Utils.log("Dispatcher", "retrying", Utils.getLogIdsForHunter(bitmapHunter));
                    }
                    if (bitmapHunter.getException() instanceof NetworkRequestHandler.ContentLengthException) {
                        bitmapHunter.networkPolicy |= NetworkPolicy.NO_CACHE.index;
                    }
                    bitmapHunter.future = this.service.submit(bitmapHunter);
                    return;
                }
                this.performError(bitmapHunter, supportsReplay);
                if (supportsReplay) {
                    this.markForReplay(bitmapHunter);
                }
            }
        }
    }
    
    void performSubmit(final Action action) {
        this.performSubmit(action, true);
    }
    
    void performSubmit(final Action action, final boolean b) {
        if (this.pausedTags.contains(action.getTag())) {
            this.pausedActions.put(action.getTarget(), action);
            if (action.getPicasso().loggingEnabled) {
                Utils.log("Dispatcher", "paused", action.request.logId(), "because tag '" + action.getTag() + "' is paused");
            }
        }
        else {
            final BitmapHunter bitmapHunter = this.hunterMap.get(action.getKey());
            if (bitmapHunter != null) {
                bitmapHunter.attach(action);
                return;
            }
            if (this.service.isShutdown()) {
                if (action.getPicasso().loggingEnabled) {
                    Utils.log("Dispatcher", "ignored", action.request.logId(), "because shut down");
                }
            }
            else {
                final BitmapHunter forRequest = BitmapHunter.forRequest(action.getPicasso(), this, this.cache, this.stats, action);
                forRequest.future = this.service.submit(forRequest);
                this.hunterMap.put(action.getKey(), forRequest);
                if (b) {
                    this.failedActions.remove(action.getTarget());
                }
                if (action.getPicasso().loggingEnabled) {
                    Utils.log("Dispatcher", "enqueued", action.request.logId());
                }
            }
        }
    }
    
    void shutdown() {
        if (this.service instanceof PicassoExecutorService) {
            this.service.shutdown();
        }
        this.downloader.shutdown();
        this.dispatcherThread.quit();
        Picasso.HANDLER.post((Runnable)new Runnable() {
            @Override
            public void run() {
                Dispatcher.this.receiver.unregister();
            }
        });
    }
    
    private static class DispatcherHandler extends Handler
    {
        private final Dispatcher dispatcher;
        
        public DispatcherHandler(final Looper looper, final Dispatcher dispatcher) {
            super(looper);
            this.dispatcher = dispatcher;
        }
        
        public void handleMessage(final Message message) {
            boolean b = true;
            switch (message.what) {
                default: {
                    Picasso.HANDLER.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            throw new AssertionError((Object)("Unknown handler message received: " + message.what));
                        }
                    });
                }
                case 1: {
                    this.dispatcher.performSubmit((Action)message.obj);
                }
                case 2: {
                    this.dispatcher.performCancel((Action)message.obj);
                }
                case 11: {
                    this.dispatcher.performPauseTag(message.obj);
                }
                case 12: {
                    this.dispatcher.performResumeTag(message.obj);
                }
                case 4: {
                    this.dispatcher.performComplete((BitmapHunter)message.obj);
                }
                case 5: {
                    this.dispatcher.performRetry((BitmapHunter)message.obj);
                }
                case 6: {
                    this.dispatcher.performError((BitmapHunter)message.obj, false);
                }
                case 7: {
                    this.dispatcher.performBatchComplete();
                }
                case 9: {
                    this.dispatcher.performNetworkStateChange((NetworkInfo)message.obj);
                }
                case 10: {
                    final Dispatcher dispatcher = this.dispatcher;
                    if (message.arg1 != 1) {
                        b = false;
                    }
                    dispatcher.performAirplaneModeChange(b);
                }
            }
        }
    }
    
    static class DispatcherThread extends HandlerThread
    {
        DispatcherThread() {
            super("Picasso-Dispatcher", 10);
        }
    }
    
    static class NetworkBroadcastReceiver extends BroadcastReceiver
    {
        static final String EXTRA_AIRPLANE_STATE = "state";
        private final Dispatcher dispatcher;
        
        NetworkBroadcastReceiver(final Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    if (intent.hasExtra("state")) {
                        this.dispatcher.dispatchAirplaneModeChange(intent.getBooleanExtra("state", false));
                    }
                }
                else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                    this.dispatcher.dispatchNetworkStateChange(Utils.getService(context, "connectivity").getActiveNetworkInfo());
                }
            }
        }
        
        void register() {
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
            if (this.dispatcher.scansNetworkChanges) {
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            }
            this.dispatcher.context.registerReceiver((BroadcastReceiver)this, intentFilter);
        }
        
        void unregister() {
            this.dispatcher.context.unregisterReceiver((BroadcastReceiver)this);
        }
    }
}
