package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StreamLocalUriFetcher extends LocalUriFetcher {
   private static final int ID_CONTACTS_CONTACT = 3;
   private static final int ID_CONTACTS_LOOKUP = 1;
   private static final int ID_CONTACTS_PHOTO = 4;
   private static final int ID_CONTACTS_THUMBNAIL = 2;
   private static final int ID_LOOKUP_BY_PHONE = 5;
   private static final UriMatcher URI_MATCHER;

   static {
      UriMatcher var0 = new UriMatcher(-1);
      URI_MATCHER = var0;
      var0.addURI("com.android.contacts", "contacts/lookup/*/#", 1);
      URI_MATCHER.addURI("com.android.contacts", "contacts/lookup/*", 1);
      URI_MATCHER.addURI("com.android.contacts", "contacts/#/photo", 2);
      URI_MATCHER.addURI("com.android.contacts", "contacts/#", 3);
      URI_MATCHER.addURI("com.android.contacts", "contacts/#/display_photo", 4);
      URI_MATCHER.addURI("com.android.contacts", "phone_lookup/*", 5);
   }

   public StreamLocalUriFetcher(ContentResolver var1, Uri var2) {
      super(var1, var2);
   }

   private InputStream loadResourceFromUri(Uri var1, ContentResolver var2) throws FileNotFoundException {
      int var3 = URI_MATCHER.match(var1);
      if (var3 != 1) {
         if (var3 == 3) {
            return this.openContactPhotoInputStream(var2, var1);
         }

         if (var3 != 5) {
            return var2.openInputStream(var1);
         }
      }

      var1 = Contacts.lookupContact(var2, var1);
      if (var1 != null) {
         return this.openContactPhotoInputStream(var2, var1);
      } else {
         throw new FileNotFoundException("Contact cannot be found");
      }
   }

   private InputStream openContactPhotoInputStream(ContentResolver var1, Uri var2) {
      return Contacts.openContactPhotoInputStream(var1, var2, true);
   }

   protected void close(InputStream var1) throws IOException {
      var1.close();
   }

   public Class getDataClass() {
      return InputStream.class;
   }

   protected InputStream loadResource(Uri var1, ContentResolver var2) throws FileNotFoundException {
      InputStream var3 = this.loadResourceFromUri(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("InputStream is null for ");
         var4.append(var1);
         throw new FileNotFoundException(var4.toString());
      }
   }
}
