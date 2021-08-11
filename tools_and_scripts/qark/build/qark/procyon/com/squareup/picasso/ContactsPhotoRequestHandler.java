// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.annotation.TargetApi;
import java.io.IOException;
import android.net.Uri;
import android.content.ContentResolver;
import android.os.Build$VERSION;
import android.provider.ContactsContract$Contacts;
import java.io.InputStream;
import android.content.Context;
import android.content.UriMatcher;

class ContactsPhotoRequestHandler extends RequestHandler
{
    private static final int ID_CONTACT = 3;
    private static final int ID_DISPLAY_PHOTO = 4;
    private static final int ID_LOOKUP = 1;
    private static final int ID_THUMBNAIL = 2;
    private static final UriMatcher matcher;
    private final Context context;
    
    static {
        (matcher = new UriMatcher(-1)).addURI("com.android.contacts", "contacts/lookup/*/#", 1);
        ContactsPhotoRequestHandler.matcher.addURI("com.android.contacts", "contacts/lookup/*", 1);
        ContactsPhotoRequestHandler.matcher.addURI("com.android.contacts", "contacts/#/photo", 2);
        ContactsPhotoRequestHandler.matcher.addURI("com.android.contacts", "contacts/#", 3);
        ContactsPhotoRequestHandler.matcher.addURI("com.android.contacts", "display_photo/#", 4);
    }
    
    ContactsPhotoRequestHandler(final Context context) {
        this.context = context;
    }
    
    private InputStream getInputStream(final Request request) throws IOException {
        final ContentResolver contentResolver = this.context.getContentResolver();
        Uri uri2;
        final Uri uri = uri2 = request.uri;
        switch (ContactsPhotoRequestHandler.matcher.match(uri)) {
            default: {
                throw new IllegalStateException("Invalid uri: " + uri);
            }
            case 1: {
                if ((uri2 = ContactsContract$Contacts.lookupContact(contentResolver, uri)) == null) {
                    return null;
                }
            }
            case 3: {
                if (Build$VERSION.SDK_INT < 14) {
                    return ContactsContract$Contacts.openContactPhotoInputStream(contentResolver, uri2);
                }
                return ContactPhotoStreamIcs.get(contentResolver, uri2);
            }
            case 2:
            case 4: {
                return contentResolver.openInputStream(uri);
            }
        }
    }
    
    @Override
    public boolean canHandleRequest(final Request request) {
        final Uri uri = request.uri;
        return "content".equals(uri.getScheme()) && ContactsContract$Contacts.CONTENT_URI.getHost().equals(uri.getHost()) && ContactsPhotoRequestHandler.matcher.match(request.uri) != -1;
    }
    
    @Override
    public Result load(final Request request, final int n) throws IOException {
        final InputStream inputStream = this.getInputStream(request);
        if (inputStream != null) {
            return new Result(inputStream, Picasso.LoadedFrom.DISK);
        }
        return null;
    }
    
    @TargetApi(14)
    private static class ContactPhotoStreamIcs
    {
        static InputStream get(final ContentResolver contentResolver, final Uri uri) {
            return ContactsContract$Contacts.openContactPhotoInputStream(contentResolver, uri, true);
        }
    }
}
