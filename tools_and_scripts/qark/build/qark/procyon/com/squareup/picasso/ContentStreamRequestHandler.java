// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.content.Context;

class ContentStreamRequestHandler extends RequestHandler
{
    final Context context;
    
    ContentStreamRequestHandler(final Context context) {
        this.context = context;
    }
    
    @Override
    public boolean canHandleRequest(final Request request) {
        return "content".equals(request.uri.getScheme());
    }
    
    InputStream getInputStream(final Request request) throws FileNotFoundException {
        return this.context.getContentResolver().openInputStream(request.uri);
    }
    
    @Override
    public Result load(final Request request, final int n) throws IOException {
        return new Result(this.getInputStream(request), Picasso.LoadedFrom.DISK);
    }
}
