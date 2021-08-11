/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.database.Cursor
 *  android.net.Uri
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.data.mediastore.FileService;
import com.bumptech.glide.load.data.mediastore.ThumbnailQuery;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class ThumbnailStreamOpener {
    private static final FileService DEFAULT_SERVICE = new FileService();
    private static final String TAG = "ThumbStreamOpener";
    private final ArrayPool byteArrayPool;
    private final ContentResolver contentResolver;
    private final List<ImageHeaderParser> parsers;
    private final ThumbnailQuery query;
    private final FileService service;

    ThumbnailStreamOpener(List<ImageHeaderParser> list, FileService fileService, ThumbnailQuery thumbnailQuery, ArrayPool arrayPool, ContentResolver contentResolver) {
        this.service = fileService;
        this.query = thumbnailQuery;
        this.byteArrayPool = arrayPool;
        this.contentResolver = contentResolver;
        this.parsers = list;
    }

    ThumbnailStreamOpener(List<ImageHeaderParser> list, ThumbnailQuery thumbnailQuery, ArrayPool arrayPool, ContentResolver contentResolver) {
        this(list, DEFAULT_SERVICE, thumbnailQuery, arrayPool, contentResolver);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private String getPath(Uri uri) {
        Cursor cursor;
        Cursor cursor2;
        Throwable throwable222;
        Cursor cursor3;
        block5 : {
            cursor2 = null;
            cursor3 = null;
            cursor = this.query.query(uri);
            if (cursor == null) break block5;
            cursor3 = cursor;
            cursor2 = cursor;
            if (!cursor.moveToFirst()) break block5;
            cursor3 = cursor;
            cursor2 = cursor;
            String string2 = cursor.getString(0);
            if (cursor == null) return string2;
            cursor.close();
            return string2;
        }
        if (cursor == null) return null;
        cursor.close();
        return null;
        {
            catch (Throwable throwable222) {
            }
            catch (SecurityException securityException) {}
            cursor3 = cursor2;
            {
                if (Log.isLoggable((String)"ThumbStreamOpener", (int)3)) {
                    cursor3 = cursor2;
                    StringBuilder stringBuilder = new StringBuilder();
                    cursor3 = cursor2;
                    stringBuilder.append("Failed to query for thumbnail for Uri: ");
                    cursor3 = cursor2;
                    stringBuilder.append((Object)uri);
                    cursor3 = cursor2;
                    Log.d((String)"ThumbStreamOpener", (String)stringBuilder.toString(), (Throwable)securityException);
                }
                if (cursor2 == null) return null;
            }
            cursor2.close();
            return null;
        }
        if (cursor3 == null) throw throwable222;
        cursor3.close();
        throw throwable222;
    }

    private boolean isValid(File file) {
        if (this.service.exists(file) && 0L < this.service.length(file)) {
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    int getOrientation(Uri uri) {
        InputStream inputStream;
        Throwable throwable222;
        block12 : {
            InputStream inputStream2;
            InputStream inputStream3 = null;
            Object object = null;
            inputStream = null;
            inputStream = inputStream2 = this.contentResolver.openInputStream(uri);
            inputStream3 = inputStream2;
            object = inputStream2;
            int n = ImageHeaderParserUtils.getOrientation(this.parsers, inputStream2, this.byteArrayPool);
            if (inputStream2 == null) return n;
            try {
                inputStream2.close();
                return n;
            }
            catch (IOException iOException) {
                // empty catch block
            }
            return n;
            catch (Throwable throwable222) {
                break block12;
            }
            catch (NullPointerException nullPointerException) {
            }
            catch (IOException iOException) {
                inputStream3 = object;
            }
            inputStream = inputStream3;
            {
                if (Log.isLoggable((String)"ThumbStreamOpener", (int)3)) {
                    void var6_12;
                    inputStream = inputStream3;
                    object = new StringBuilder();
                    inputStream = inputStream3;
                    object.append("Failed to open uri: ");
                    inputStream = inputStream3;
                    object.append((Object)uri);
                    inputStream = inputStream3;
                    Log.d((String)"ThumbStreamOpener", (String)object.toString(), (Throwable)var6_12);
                }
                if (inputStream3 == null) return -1;
            }
            try {
                inputStream3.close();
                return -1;
            }
            catch (IOException iOException) {
                return -1;
            }
        }
        if (inputStream == null) throw throwable222;
        try {
            inputStream.close();
            throw throwable222;
        }
        catch (IOException iOException) {
            // empty catch block
        }
        throw throwable222;
    }

    public InputStream open(Uri uri) throws FileNotFoundException {
        Object object = this.getPath(uri);
        if (TextUtils.isEmpty((CharSequence)object)) {
            return null;
        }
        if (!this.isValid((File)(object = this.service.get((String)object)))) {
            return null;
        }
        object = Uri.fromFile((File)object);
        try {
            InputStream inputStream = this.contentResolver.openInputStream((Uri)object);
            return inputStream;
        }
        catch (NullPointerException nullPointerException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NPE opening uri: ");
            stringBuilder.append((Object)uri);
            stringBuilder.append(" -> ");
            stringBuilder.append(object);
            throw (FileNotFoundException)new FileNotFoundException(stringBuilder.toString()).initCause(nullPointerException);
        }
    }
}

