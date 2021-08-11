/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 */
package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.bumptech.glide.load.engine.Resource;

class ResourceRecycler {
    private final Handler handler = new Handler(Looper.getMainLooper(), (Handler.Callback)new ResourceRecyclerCallback());
    private boolean isRecycling;

    ResourceRecycler() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void recycle(Resource<?> resource, boolean bl) {
        synchronized (this) {
            void var2_2;
            if (!this.isRecycling && var2_2 == false) {
                this.isRecycling = true;
                resource.recycle();
                this.isRecycling = false;
            } else {
                this.handler.obtainMessage(1, resource).sendToTarget();
            }
            return;
        }
    }

    private static final class ResourceRecyclerCallback
    implements Handler.Callback {
        static final int RECYCLE_RESOURCE = 1;

        ResourceRecyclerCallback() {
        }

        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                ((Resource)message.obj).recycle();
                return true;
            }
            return false;
        }
    }

}

