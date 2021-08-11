/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ImageHeaderParser {
    public static final int UNKNOWN_ORIENTATION = -1;

    public int getOrientation(InputStream var1, ArrayPool var2) throws IOException;

    public int getOrientation(ByteBuffer var1, ArrayPool var2) throws IOException;

    public ImageType getType(InputStream var1) throws IOException;

    public ImageType getType(ByteBuffer var1) throws IOException;

    public static final class ImageType
    extends Enum<ImageType> {
        private static final /* synthetic */ ImageType[] $VALUES;
        public static final /* enum */ ImageType GIF;
        public static final /* enum */ ImageType JPEG;
        public static final /* enum */ ImageType PNG;
        public static final /* enum */ ImageType PNG_A;
        public static final /* enum */ ImageType RAW;
        public static final /* enum */ ImageType UNKNOWN;
        public static final /* enum */ ImageType WEBP;
        public static final /* enum */ ImageType WEBP_A;
        private final boolean hasAlpha;

        static {
            ImageType imageType;
            GIF = new ImageType(true);
            JPEG = new ImageType(false);
            RAW = new ImageType(false);
            PNG_A = new ImageType(true);
            PNG = new ImageType(false);
            WEBP_A = new ImageType(true);
            WEBP = new ImageType(false);
            UNKNOWN = imageType = new ImageType(false);
            $VALUES = new ImageType[]{GIF, JPEG, RAW, PNG_A, PNG, WEBP_A, WEBP, imageType};
        }

        private ImageType(boolean bl) {
            this.hasAlpha = bl;
        }

        public static ImageType valueOf(String string2) {
            return Enum.valueOf(ImageType.class, string2);
        }

        public static ImageType[] values() {
            return (ImageType[])$VALUES.clone();
        }

        public boolean hasAlpha() {
            return this.hasAlpha;
        }
    }

}

