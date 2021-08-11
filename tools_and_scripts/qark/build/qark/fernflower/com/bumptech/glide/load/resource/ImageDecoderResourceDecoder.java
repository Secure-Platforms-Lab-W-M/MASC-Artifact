package com.bumptech.glide.load.resource;

import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.graphics.ColorSpace.Named;
import android.graphics.ImageDecoder.DecodeException;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.OnPartialImageListener;
import android.graphics.ImageDecoder.Source;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.Size;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import java.io.IOException;

public abstract class ImageDecoderResourceDecoder implements ResourceDecoder {
   private static final String TAG = "ImageDecoder";
   final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();

   protected abstract Resource decode(Source var1, int var2, int var3, OnHeaderDecodedListener var4) throws IOException;

   public final Resource decode(Source var1, final int var2, final int var3, Options var4) throws IOException {
      final DecodeFormat var6 = (DecodeFormat)var4.get(Downsampler.DECODE_FORMAT);
      final DownsampleStrategy var7 = (DownsampleStrategy)var4.get(DownsampleStrategy.OPTION);
      final boolean var5;
      if (var4.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null && (Boolean)var4.get(Downsampler.ALLOW_HARDWARE_CONFIG)) {
         var5 = true;
      } else {
         var5 = false;
      }

      return this.decode(var1, var2, var3, new OnHeaderDecodedListener((PreferredColorSpace)var4.get(Downsampler.PREFERRED_COLOR_SPACE)) {
         // $FF: synthetic field
         final PreferredColorSpace val$preferredColorSpace;

         {
            this.val$preferredColorSpace = var7x;
         }

         public void onHeaderDecoded(ImageDecoder var1, ImageInfo var2x, Source var3x) {
            HardwareConfigState var11 = ImageDecoderResourceDecoder.this.hardwareConfigState;
            int var5x = var2;
            int var6x = var3;
            boolean var8 = var5;
            boolean var7x = false;
            if (var11.isHardwareConfigAllowed(var5x, var6x, var8, false)) {
               var1.setAllocator(3);
            } else {
               var1.setAllocator(1);
            }

            if (var6 == DecodeFormat.PREFER_RGB_565) {
               var1.setMemorySizePolicy(0);
            }

            var1.setOnPartialImageListener(new OnPartialImageListener() {
               public boolean onPartialImage(DecodeException var1) {
                  return false;
               }
            });
            Size var12 = var2x.getSize();
            var5x = var2;
            if (var2 == Integer.MIN_VALUE) {
               var5x = var12.getWidth();
            }

            var6x = var3;
            if (var3 == Integer.MIN_VALUE) {
               var6x = var12.getHeight();
            }

            float var4 = var7.getScaleFactor(var12.getWidth(), var12.getHeight(), var5x, var6x);
            var5x = Math.round((float)var12.getWidth() * var4);
            var6x = Math.round((float)var12.getHeight() * var4);
            if (Log.isLoggable("ImageDecoder", 2)) {
               StringBuilder var9 = new StringBuilder();
               var9.append("Resizing from [");
               var9.append(var12.getWidth());
               var9.append("x");
               var9.append(var12.getHeight());
               var9.append("] to [");
               var9.append(var5x);
               var9.append("x");
               var9.append(var6x);
               var9.append("] scaleFactor: ");
               var9.append(var4);
               Log.v("ImageDecoder", var9.toString());
            }

            var1.setTargetSize(var5x, var6x);
            if (VERSION.SDK_INT >= 28) {
               boolean var13;
               if (this.val$preferredColorSpace == PreferredColorSpace.DISPLAY_P3 && var2x.getColorSpace() != null && var2x.getColorSpace().isWideGamut()) {
                  var13 = true;
               } else {
                  var13 = var7x;
               }

               Named var10;
               if (var13) {
                  var10 = Named.DISPLAY_P3;
               } else {
                  var10 = Named.SRGB;
               }

               var1.setTargetColorSpace(ColorSpace.get(var10));
            } else if (VERSION.SDK_INT >= 26) {
               var1.setTargetColorSpace(ColorSpace.get(Named.SRGB));
               return;
            }

         }
      });
   }

   public final boolean handles(Source var1, Options var2) {
      return true;
   }
}
