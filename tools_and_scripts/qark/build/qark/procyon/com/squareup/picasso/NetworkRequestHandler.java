// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.net.NetworkInfo;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.Bitmap;

class NetworkRequestHandler extends RequestHandler
{
    static final int RETRY_COUNT = 2;
    private static final String SCHEME_HTTP = "http";
    private static final String SCHEME_HTTPS = "https";
    private final Downloader downloader;
    private final Stats stats;
    
    public NetworkRequestHandler(final Downloader downloader, final Stats stats) {
        this.downloader = downloader;
        this.stats = stats;
    }
    
    @Override
    public boolean canHandleRequest(final Request request) {
        final String scheme = request.uri.getScheme();
        return "http".equals(scheme) || "https".equals(scheme);
    }
    
    @Override
    int getRetryCount() {
        return 2;
    }
    
    @Override
    public Result load(final Request request, final int n) throws IOException {
        final Downloader.Response load = this.downloader.load(request.uri, request.networkPolicy);
        if (load != null) {
            Picasso.LoadedFrom loadedFrom;
            if (load.cached) {
                loadedFrom = Picasso.LoadedFrom.DISK;
            }
            else {
                loadedFrom = Picasso.LoadedFrom.NETWORK;
            }
            final Bitmap bitmap = load.getBitmap();
            if (bitmap != null) {
                return new Result(bitmap, loadedFrom);
            }
            final InputStream inputStream = load.getInputStream();
            if (inputStream != null) {
                if (loadedFrom == Picasso.LoadedFrom.DISK && load.getContentLength() == 0L) {
                    Utils.closeQuietly(inputStream);
                    throw new ContentLengthException("Received response with 0 content-length header.");
                }
                if (loadedFrom == Picasso.LoadedFrom.NETWORK && load.getContentLength() > 0L) {
                    this.stats.dispatchDownloadFinished(load.getContentLength());
                }
                return new Result(inputStream, loadedFrom);
            }
        }
        return null;
    }
    
    @Override
    boolean shouldRetry(final boolean b, final NetworkInfo networkInfo) {
        return networkInfo == null || networkInfo.isConnected();
    }
    
    @Override
    boolean supportsReplay() {
        return true;
    }
    
    static class ContentLengthException extends IOException
    {
        public ContentLengthException(final String s) {
            super(s);
        }
    }
}
