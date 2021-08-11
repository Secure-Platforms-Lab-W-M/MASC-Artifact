/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.util.pool;

public abstract class StateVerifier {
    private static final boolean DEBUG = false;

    private StateVerifier() {
    }

    public static StateVerifier newInstance() {
        return new DefaultStateVerifier();
    }

    abstract void setRecycled(boolean var1);

    public abstract void throwIfRecycled();

    private static class DebugStateVerifier
    extends StateVerifier {
        private volatile RuntimeException recycledAtStackTraceException;

        DebugStateVerifier() {
            super();
        }

        @Override
        void setRecycled(boolean bl) {
            if (bl) {
                this.recycledAtStackTraceException = new RuntimeException("Released");
                return;
            }
            this.recycledAtStackTraceException = null;
        }

        @Override
        public void throwIfRecycled() {
            if (this.recycledAtStackTraceException == null) {
                return;
            }
            throw new IllegalStateException("Already released", this.recycledAtStackTraceException);
        }
    }

    private static class DefaultStateVerifier
    extends StateVerifier {
        private volatile boolean isReleased;

        DefaultStateVerifier() {
            super();
        }

        @Override
        public void setRecycled(boolean bl) {
            this.isReleased = bl;
        }

        @Override
        public void throwIfRecycled() {
            if (!this.isReleased) {
                return;
            }
            throw new IllegalStateException("Already released");
        }
    }

}

