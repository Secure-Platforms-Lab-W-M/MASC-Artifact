// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.io.IOException;
import android.net.Uri;
import android.content.Context;
import android.content.res.AssetManager;

class AssetRequestHandler extends RequestHandler
{
    protected static final String ANDROID_ASSET = "android_asset";
    private static final int ASSET_PREFIX_LENGTH;
    private final AssetManager assetManager;
    
    static {
        ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
    }
    
    public AssetRequestHandler(final Context context) {
        this.assetManager = context.getAssets();
    }
    
    static String getFilePath(final Request request) {
        return request.uri.toString().substring(AssetRequestHandler.ASSET_PREFIX_LENGTH);
    }
    
    @Override
    public boolean canHandleRequest(final Request request) {
        final boolean b = false;
        final Uri uri = request.uri;
        boolean b2 = b;
        if ("file".equals(uri.getScheme())) {
            b2 = b;
            if (!uri.getPathSegments().isEmpty()) {
                b2 = b;
                if ("android_asset".equals(uri.getPathSegments().get(0))) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public Result load(final Request request, final int n) throws IOException {
        return new Result(this.assetManager.open(getFilePath(request)), Picasso.LoadedFrom.DISK);
    }
}
