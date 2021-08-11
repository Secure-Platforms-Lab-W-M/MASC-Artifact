// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.io.IOException;
import android.graphics.BitmapFactory$Options;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.content.res.Resources;
import android.content.Context;

class ResourceRequestHandler extends RequestHandler
{
    private final Context context;
    
    ResourceRequestHandler(final Context context) {
        this.context = context;
    }
    
    private static Bitmap decodeResource(final Resources resources, final int n, final Request request) {
        final BitmapFactory$Options bitmapOptions = RequestHandler.createBitmapOptions(request);
        if (RequestHandler.requiresInSampleSize(bitmapOptions)) {
            BitmapFactory.decodeResource(resources, n, bitmapOptions);
            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, bitmapOptions, request);
        }
        return BitmapFactory.decodeResource(resources, n, bitmapOptions);
    }
    
    @Override
    public boolean canHandleRequest(final Request request) {
        return request.resourceId != 0 || "android.resource".equals(request.uri.getScheme());
    }
    
    @Override
    public Result load(final Request request, final int n) throws IOException {
        final Resources resources = Utils.getResources(this.context, request);
        return new Result(decodeResource(resources, Utils.getResourceId(resources, request), request), Picasso.LoadedFrom.DISK);
    }
}
