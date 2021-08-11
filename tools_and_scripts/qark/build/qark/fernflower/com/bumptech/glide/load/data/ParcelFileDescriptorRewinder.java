package com.bumptech.glide.load.data;

import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.IOException;

public final class ParcelFileDescriptorRewinder implements DataRewinder {
   private final ParcelFileDescriptorRewinder.InternalRewinder rewinder;

   public ParcelFileDescriptorRewinder(ParcelFileDescriptor var1) {
      this.rewinder = new ParcelFileDescriptorRewinder.InternalRewinder(var1);
   }

   public static boolean isSupported() {
      return VERSION.SDK_INT >= 21;
   }

   public void cleanup() {
   }

   public ParcelFileDescriptor rewindAndGet() throws IOException {
      return this.rewinder.rewind();
   }

   public static final class Factory implements DataRewinder.Factory {
      public DataRewinder build(ParcelFileDescriptor var1) {
         return new ParcelFileDescriptorRewinder(var1);
      }

      public Class getDataClass() {
         return ParcelFileDescriptor.class;
      }
   }

   private static final class InternalRewinder {
      private final ParcelFileDescriptor parcelFileDescriptor;

      InternalRewinder(ParcelFileDescriptor var1) {
         this.parcelFileDescriptor = var1;
      }

      ParcelFileDescriptor rewind() throws IOException {
         try {
            Os.lseek(this.parcelFileDescriptor.getFileDescriptor(), 0L, OsConstants.SEEK_SET);
         } catch (ErrnoException var2) {
            throw new IOException(var2);
         }

         return this.parcelFileDescriptor;
      }
   }
}
