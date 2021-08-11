/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.cache;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SafeKeyGenerator {
    private final Pools.Pool<PoolableDigestContainer> digestPool;
    private final LruCache<Key, String> loadIdToSafeHash = new LruCache(1000L);

    public SafeKeyGenerator() {
        this.digestPool = FactoryPools.threadSafe(10, new FactoryPools.Factory<PoolableDigestContainer>(){

            @Override
            public PoolableDigestContainer create() {
                try {
                    PoolableDigestContainer poolableDigestContainer = new PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
                    return poolableDigestContainer;
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    throw new RuntimeException(noSuchAlgorithmException);
                }
            }
        });
    }

    private String calculateHexStringDigest(Key object) {
        PoolableDigestContainer poolableDigestContainer = Preconditions.checkNotNull(this.digestPool.acquire());
        try {
            object.updateDiskCacheKey(poolableDigestContainer.messageDigest);
            object = Util.sha256BytesToHex(poolableDigestContainer.messageDigest.digest());
            return object;
        }
        finally {
            this.digestPool.release(poolableDigestContainer);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getSafeKey(Key key) {
        Object object;
        Object object2 = this.loadIdToSafeHash;
        synchronized (object2) {
            object = this.loadIdToSafeHash.get(key);
        }
        object2 = object;
        if (object == null) {
            object2 = this.calculateHexStringDigest(key);
        }
        object = this.loadIdToSafeHash;
        synchronized (object) {
            this.loadIdToSafeHash.put(key, (String)object2);
            return object2;
        }
    }

    private static final class PoolableDigestContainer
    implements FactoryPools.Poolable {
        final MessageDigest messageDigest;
        private final StateVerifier stateVerifier = StateVerifier.newInstance();

        PoolableDigestContainer(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        @Override
        public StateVerifier getVerifier() {
            return this.stateVerifier;
        }
    }

}

