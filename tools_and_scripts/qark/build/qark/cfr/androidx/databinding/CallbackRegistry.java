/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

import java.util.ArrayList;
import java.util.List;

public class CallbackRegistry<C, T, A>
implements Cloneable {
    private static final String TAG = "CallbackRegistry";
    private List<C> mCallbacks = new ArrayList<C>();
    private long mFirst64Removed = 0L;
    private int mNotificationLevel;
    private final NotifierCallback<C, T, A> mNotifier;
    private long[] mRemainderRemoved;

    public CallbackRegistry(NotifierCallback<C, T, A> notifierCallback) {
        this.mNotifier = notifierCallback;
    }

    private boolean isRemoved(int n) {
        if (n < 64) {
            if ((this.mFirst64Removed & 1L << n) != 0L) {
                return true;
            }
            return false;
        }
        long[] arrl = this.mRemainderRemoved;
        if (arrl == null) {
            return false;
        }
        int n2 = n / 64 - 1;
        if (n2 >= arrl.length) {
            return false;
        }
        if ((arrl[n2] & 1L << n % 64) != 0L) {
            return true;
        }
        return false;
    }

    private void notifyCallbacks(T t, int n, A a, int n2, int n3, long l) {
        long l2 = 1L;
        while (n2 < n3) {
            if ((l & l2) == 0L) {
                this.mNotifier.onNotifyCallback(this.mCallbacks.get(n2), t, n, a);
            }
            l2 <<= 1;
            ++n2;
        }
    }

    private void notifyFirst64(T t, int n, A a) {
        this.notifyCallbacks(t, n, a, 0, Math.min(64, this.mCallbacks.size()), this.mFirst64Removed);
    }

    private void notifyRecurse(T t, int n, A a) {
        int n2 = this.mCallbacks.size();
        long[] arrl = this.mRemainderRemoved;
        int n3 = arrl == null ? -1 : arrl.length - 1;
        this.notifyRemainder(t, n, a, n3);
        this.notifyCallbacks(t, n, a, (n3 + 2) * 64, n2, 0L);
    }

    private void notifyRemainder(T t, int n, A a, int n2) {
        if (n2 < 0) {
            this.notifyFirst64(t, n, a);
            return;
        }
        long l = this.mRemainderRemoved[n2];
        int n3 = (n2 + 1) * 64;
        int n4 = Math.min(this.mCallbacks.size(), n3 + 64);
        this.notifyRemainder(t, n, a, n2 - 1);
        this.notifyCallbacks(t, n, a, n3, n4, l);
    }

    private void removeRemovedCallbacks(int n, long l) {
        long l2 = Long.MIN_VALUE;
        for (int i = n + 64 - 1; i >= n; --i) {
            if ((l & l2) != 0L) {
                this.mCallbacks.remove(i);
            }
            l2 >>>= 1;
        }
    }

    private void setRemovalBit(int n) {
        if (n < 64) {
            this.mFirst64Removed |= 1L << n;
            return;
        }
        int n2 = n / 64 - 1;
        long[] arrl = this.mRemainderRemoved;
        if (arrl == null) {
            this.mRemainderRemoved = new long[this.mCallbacks.size() / 64];
        } else if (arrl.length <= n2) {
            arrl = new long[this.mCallbacks.size() / 64];
            long[] arrl2 = this.mRemainderRemoved;
            System.arraycopy(arrl2, 0, arrl, 0, arrl2.length);
            this.mRemainderRemoved = arrl;
        }
        arrl = this.mRemainderRemoved;
        arrl[n2] = arrl[n2] | 1L << n % 64;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void add(C c) {
        synchronized (this) {
            Throwable throwable2;
            if (c != null) {
                try {
                    int n = this.mCallbacks.lastIndexOf(c);
                    if (n < 0 || this.isRemoved(n)) {
                        this.mCallbacks.add(c);
                    }
                    return;
                }
                catch (Throwable throwable2) {}
            } else {
                throw new IllegalArgumentException("callback cannot be null");
            }
            throw throwable2;
        }
    }

    public void clear() {
        synchronized (this) {
            block6 : {
                if (this.mNotificationLevel == 0) {
                    this.mCallbacks.clear();
                    break block6;
                }
                if (this.mCallbacks.isEmpty()) break block6;
                for (int i = this.mCallbacks.size() - 1; i >= 0; --i) {
                    this.setRemovalBit(i);
                }
            }
            return;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public CallbackRegistry<C, T, A> clone() {
        CallbackRegistry callbackRegistry;
        // MONITORENTER : this
        CallbackRegistry callbackRegistry2 = null;
        callbackRegistry2 = callbackRegistry = (CallbackRegistry)super.clone();
        callbackRegistry.mFirst64Removed = 0L;
        callbackRegistry2 = callbackRegistry;
        callbackRegistry.mRemainderRemoved = null;
        callbackRegistry2 = callbackRegistry;
        callbackRegistry.mNotificationLevel = 0;
        callbackRegistry2 = callbackRegistry;
        callbackRegistry.mCallbacks = new ArrayList<C>();
        callbackRegistry2 = callbackRegistry;
        int n = this.mCallbacks.size();
        int n2 = 0;
        while (n2 < n) {
            callbackRegistry2 = callbackRegistry;
            if (!this.isRemoved(n2)) {
                callbackRegistry2 = callbackRegistry;
                callbackRegistry.mCallbacks.add(this.mCallbacks.get(n2));
            }
            ++n2;
        }
        return callbackRegistry;
        {
            catch (Throwable throwable) {
                throw throwable;
            }
            catch (CloneNotSupportedException cloneNotSupportedException) {}
            {
                cloneNotSupportedException.printStackTrace();
                // MONITOREXIT : this
            }
        }
        return callbackRegistry2;
    }

    public ArrayList<C> copyCallbacks() {
        synchronized (this) {
            ArrayList<C> arrayList = new ArrayList<C>(this.mCallbacks.size());
            int n = this.mCallbacks.size();
            for (int i = 0; i < n; ++i) {
                if (this.isRemoved(i)) continue;
                arrayList.add(this.mCallbacks.get(i));
            }
            return arrayList;
        }
    }

    public void copyCallbacks(List<C> list) {
        synchronized (this) {
            list.clear();
            int n = this.mCallbacks.size();
            for (int i = 0; i < n; ++i) {
                if (this.isRemoved(i)) continue;
                list.add(this.mCallbacks.get(i));
            }
            return;
        }
    }

    public boolean isEmpty() {
        synchronized (this) {
            boolean bl;
            int n;
            block8 : {
                block7 : {
                    bl = this.mCallbacks.isEmpty();
                    if (!bl) break block7;
                    return true;
                }
                n = this.mNotificationLevel;
                if (n != 0) break block8;
                return false;
            }
            int n2 = this.mCallbacks.size();
            for (n = 0; n < n2; ++n) {
                bl = this.isRemoved(n);
                if (bl) continue;
                return false;
            }
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void notifyCallbacks(T t, int n, A a) {
        synchronized (this) {
            int n2;
            void var3_3;
            ++this.mNotificationLevel;
            this.notifyRecurse(t, n2, var3_3);
            this.mNotificationLevel = n2 = this.mNotificationLevel - 1;
            if (n2 == 0) {
                if (this.mRemainderRemoved != null) {
                    for (n2 = this.mRemainderRemoved.length - 1; n2 >= 0; --n2) {
                        long l = this.mRemainderRemoved[n2];
                        if (l == 0L) continue;
                        this.removeRemovedCallbacks((n2 + 1) * 64, l);
                        this.mRemainderRemoved[n2] = 0L;
                    }
                }
                if (this.mFirst64Removed != 0L) {
                    this.removeRemovedCallbacks(0, this.mFirst64Removed);
                    this.mFirst64Removed = 0L;
                }
            }
            return;
        }
    }

    public void remove(C c) {
        synchronized (this) {
            if (this.mNotificationLevel == 0) {
                this.mCallbacks.remove(c);
            } else {
                int n = this.mCallbacks.lastIndexOf(c);
                if (n >= 0) {
                    this.setRemovalBit(n);
                }
            }
            return;
        }
    }

    public static abstract class NotifierCallback<C, T, A> {
        public abstract void onNotifyCallback(C var1, T var2, int var3, A var4);
    }

}

