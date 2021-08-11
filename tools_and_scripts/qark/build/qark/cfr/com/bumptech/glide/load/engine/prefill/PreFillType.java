/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 */
package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import com.bumptech.glide.util.Preconditions;

public final class PreFillType {
    static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.RGB_565;
    private final Bitmap.Config config;
    private final int height;
    private final int weight;
    private final int width;

    PreFillType(int n, int n2, Bitmap.Config config, int n3) {
        this.config = Preconditions.checkNotNull(config, "Config must not be null");
        this.width = n;
        this.height = n2;
        this.weight = n3;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof PreFillType;
        boolean bl2 = false;
        if (bl) {
            object = (PreFillType)object;
            bl = bl2;
            if (this.height == object.height) {
                bl = bl2;
                if (this.width == object.width) {
                    bl = bl2;
                    if (this.weight == object.weight) {
                        bl = bl2;
                        if (this.config == object.config) {
                            bl = true;
                        }
                    }
                }
            }
            return bl;
        }
        return false;
    }

    Bitmap.Config getConfig() {
        return this.config;
    }

    int getHeight() {
        return this.height;
    }

    int getWeight() {
        return this.weight;
    }

    int getWidth() {
        return this.width;
    }

    public int hashCode() {
        return ((this.width * 31 + this.height) * 31 + this.config.hashCode()) * 31 + this.weight;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PreFillSize{width=");
        stringBuilder.append(this.width);
        stringBuilder.append(", height=");
        stringBuilder.append(this.height);
        stringBuilder.append(", config=");
        stringBuilder.append((Object)this.config);
        stringBuilder.append(", weight=");
        stringBuilder.append(this.weight);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static class Builder {
        private Bitmap.Config config;
        private final int height;
        private int weight = 1;
        private final int width;

        public Builder(int n) {
            this(n, n);
        }

        public Builder(int n, int n2) {
            if (n > 0) {
                if (n2 > 0) {
                    this.width = n;
                    this.height = n2;
                    return;
                }
                throw new IllegalArgumentException("Height must be > 0");
            }
            throw new IllegalArgumentException("Width must be > 0");
        }

        PreFillType build() {
            return new PreFillType(this.width, this.height, this.config, this.weight);
        }

        Bitmap.Config getConfig() {
            return this.config;
        }

        public Builder setConfig(Bitmap.Config config) {
            this.config = config;
            return this;
        }

        public Builder setWeight(int n) {
            if (n > 0) {
                this.weight = n;
                return this;
            }
            throw new IllegalArgumentException("Weight must be > 0");
        }
    }

}

