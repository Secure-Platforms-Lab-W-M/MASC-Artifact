// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.io.IOException;
import android.graphics.BitmapFactory$Options;
import java.io.InputStream;
import android.provider.MediaStore$Images$Thumbnails;
import android.provider.MediaStore$Video$Thumbnails;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.database.Cursor;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.Context;

class MediaStoreRequestHandler extends ContentStreamRequestHandler
{
    private static final String[] CONTENT_ORIENTATION;
    
    static {
        CONTENT_ORIENTATION = new String[] { "orientation" };
    }
    
    MediaStoreRequestHandler(final Context context) {
        super(context);
    }
    
    static int getExifOrientation(final ContentResolver contentResolver, final Uri uri) {
        Cursor cursor = null;
        try {
            final Cursor query = contentResolver.query(uri, MediaStoreRequestHandler.CONTENT_ORIENTATION, (String)null, (String[])null, (String)null);
            if (query != null) {
                cursor = query;
                if (query.moveToFirst()) {
                    cursor = query;
                    final int int1;
                    return int1 = query.getInt(0);
                }
            }
            if (query != null) {
                query.close();
            }
            return 0;
        }
        catch (RuntimeException ex) {
            return 0;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
    static PicassoKind getPicassoKind(final int n, final int n2) {
        if (n <= PicassoKind.MICRO.width && n2 <= PicassoKind.MICRO.height) {
            return PicassoKind.MICRO;
        }
        if (n <= PicassoKind.MINI.width && n2 <= PicassoKind.MINI.height) {
            return PicassoKind.MINI;
        }
        return PicassoKind.FULL;
    }
    
    @Override
    public boolean canHandleRequest(final Request request) {
        final Uri uri = request.uri;
        return "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }
    
    @Override
    public Result load(final Request request, int androidKind) throws IOException {
        final ContentResolver contentResolver = this.context.getContentResolver();
        final int exifOrientation = getExifOrientation(contentResolver, request.uri);
        final String type = contentResolver.getType(request.uri);
        if (type != null && type.startsWith("video/")) {
            androidKind = 1;
        }
        else {
            androidKind = 0;
        }
        if (request.hasSize()) {
            final PicassoKind picassoKind = getPicassoKind(request.targetWidth, request.targetHeight);
            if (androidKind == 0 && picassoKind == PicassoKind.FULL) {
                return new Result(null, this.getInputStream(request), Picasso.LoadedFrom.DISK, exifOrientation);
            }
            final long id = ContentUris.parseId(request.uri);
            final BitmapFactory$Options bitmapOptions = RequestHandler.createBitmapOptions(request);
            bitmapOptions.inJustDecodeBounds = true;
            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, picassoKind.width, picassoKind.height, bitmapOptions, request);
            Bitmap bitmap;
            if (androidKind != 0) {
                if (picassoKind == PicassoKind.FULL) {
                    androidKind = 1;
                }
                else {
                    androidKind = picassoKind.androidKind;
                }
                bitmap = MediaStore$Video$Thumbnails.getThumbnail(contentResolver, id, androidKind, bitmapOptions);
            }
            else {
                bitmap = MediaStore$Images$Thumbnails.getThumbnail(contentResolver, id, picassoKind.androidKind, bitmapOptions);
            }
            if (bitmap != null) {
                return new Result(bitmap, null, Picasso.LoadedFrom.DISK, exifOrientation);
            }
        }
        return new Result(null, this.getInputStream(request), Picasso.LoadedFrom.DISK, exifOrientation);
    }
    
    enum PicassoKind
    {
        FULL(2, -1, -1), 
        MICRO(3, 96, 96), 
        MINI(1, 512, 384);
        
        final int androidKind;
        final int height;
        final int width;
        
        private PicassoKind(final int androidKind, final int width, final int height) {
            this.androidKind = androidKind;
            this.width = width;
            this.height = height;
        }
    }
}
