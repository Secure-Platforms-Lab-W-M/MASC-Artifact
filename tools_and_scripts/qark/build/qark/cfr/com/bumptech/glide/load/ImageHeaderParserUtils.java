/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.ParcelFileDescriptor
 */
package com.bumptech.glide.load;

import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public final class ImageHeaderParserUtils {
    private static final int MARK_READ_LIMIT = 5242880;

    private ImageHeaderParserUtils() {
    }

    public static int getOrientation(List<ImageHeaderParser> list, ParcelFileDescriptorRewinder parcelFileDescriptorRewinder, final ArrayPool arrayPool) throws IOException {
        return ImageHeaderParserUtils.getOrientationInternal(list, new OrientationReader(){

            @Override
            public int getOrientation(ImageHeaderParser imageHeaderParser) throws IOException {
                RecyclableBufferedInputStream recyclableBufferedInputStream;
                InputStream inputStream = null;
                try {
                    recyclableBufferedInputStream = new RecyclableBufferedInputStream(new FileInputStream(ParcelFileDescriptorRewinder.this.rewindAndGet().getFileDescriptor()), arrayPool);
                    inputStream = recyclableBufferedInputStream;
                }
                catch (Throwable throwable) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    ParcelFileDescriptorRewinder.this.rewindAndGet();
                    throw throwable;
                }
                int n = imageHeaderParser.getOrientation(recyclableBufferedInputStream, arrayPool);
                try {
                    recyclableBufferedInputStream.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                ParcelFileDescriptorRewinder.this.rewindAndGet();
                return n;
            }
        });
    }

    public static int getOrientation(List<ImageHeaderParser> list, InputStream inputStream, final ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return -1;
        }
        InputStream inputStream2 = inputStream;
        if (!inputStream.markSupported()) {
            inputStream2 = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream2.mark(5242880);
        return ImageHeaderParserUtils.getOrientationInternal(list, new OrientationReader(){

            @Override
            public int getOrientation(ImageHeaderParser imageHeaderParser) throws IOException {
                try {
                    int n = imageHeaderParser.getOrientation(InputStream.this, arrayPool);
                    return n;
                }
                finally {
                    InputStream.this.reset();
                }
            }
        });
    }

    private static int getOrientationInternal(List<ImageHeaderParser> list, OrientationReader orientationReader) throws IOException {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            int n2 = orientationReader.getOrientation(list.get(i));
            if (n2 == -1) continue;
            return n2;
        }
        return -1;
    }

    public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> list, ParcelFileDescriptorRewinder parcelFileDescriptorRewinder, final ArrayPool arrayPool) throws IOException {
        return ImageHeaderParserUtils.getTypeInternal(list, new TypeReader(){

            @Override
            public ImageHeaderParser.ImageType getType(ImageHeaderParser object) throws IOException {
                RecyclableBufferedInputStream recyclableBufferedInputStream;
                InputStream inputStream = null;
                try {
                    recyclableBufferedInputStream = new RecyclableBufferedInputStream(new FileInputStream(ParcelFileDescriptorRewinder.this.rewindAndGet().getFileDescriptor()), arrayPool);
                    inputStream = recyclableBufferedInputStream;
                }
                catch (Throwable throwable) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    ParcelFileDescriptorRewinder.this.rewindAndGet();
                    throw throwable;
                }
                object = object.getType(recyclableBufferedInputStream);
                try {
                    recyclableBufferedInputStream.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                ParcelFileDescriptorRewinder.this.rewindAndGet();
                return object;
            }
        });
    }

    public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> list, InputStream inputStream, ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return ImageHeaderParser.ImageType.UNKNOWN;
        }
        InputStream inputStream2 = inputStream;
        if (!inputStream.markSupported()) {
            inputStream2 = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream2.mark(5242880);
        return ImageHeaderParserUtils.getTypeInternal(list, new TypeReader(){

            @Override
            public ImageHeaderParser.ImageType getType(ImageHeaderParser object) throws IOException {
                try {
                    object = object.getType(InputStream.this);
                    return object;
                }
                finally {
                    InputStream.this.reset();
                }
            }
        });
    }

    public static ImageHeaderParser.ImageType getType(List<ImageHeaderParser> list, ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer == null) {
            return ImageHeaderParser.ImageType.UNKNOWN;
        }
        return ImageHeaderParserUtils.getTypeInternal(list, new TypeReader(){

            @Override
            public ImageHeaderParser.ImageType getType(ImageHeaderParser imageHeaderParser) throws IOException {
                return imageHeaderParser.getType(ByteBuffer.this);
            }
        });
    }

    private static ImageHeaderParser.ImageType getTypeInternal(List<ImageHeaderParser> list, TypeReader typeReader) throws IOException {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            ImageHeaderParser.ImageType imageType = typeReader.getType(list.get(i));
            if (imageType == ImageHeaderParser.ImageType.UNKNOWN) continue;
            return imageType;
        }
        return ImageHeaderParser.ImageType.UNKNOWN;
    }

    private static interface OrientationReader {
        public int getOrientation(ImageHeaderParser var1) throws IOException;
    }

    private static interface TypeReader {
        public ImageHeaderParser.ImageType getType(ImageHeaderParser var1) throws IOException;
    }

}

