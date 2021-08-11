package com.yalantis.ucrop.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.FileUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

public class BitmapLoadTask extends AsyncTask {
   private static final String TAG = "BitmapWorkerTask";
   private final BitmapLoadCallback mBitmapLoadCallback;
   private final Context mContext;
   private Uri mInputUri;
   private Uri mOutputUri;
   private final int mRequiredHeight;
   private final int mRequiredWidth;

   public BitmapLoadTask(Context var1, Uri var2, Uri var3, int var4, int var5, BitmapLoadCallback var6) {
      this.mContext = var1;
      this.mInputUri = var2;
      this.mOutputUri = var3;
      this.mRequiredWidth = var4;
      this.mRequiredHeight = var5;
      this.mBitmapLoadCallback = var6;
   }

   private void copyFile(Uri var1, Uri var2) throws NullPointerException, IOException {
      Log.d("BitmapWorkerTask", "copyFile");
      if (var2 != null) {
         InputStream var4 = null;
         Object var6 = null;
         FileOutputStream var5 = (FileOutputStream)var6;

         Throwable var10000;
         label426: {
            InputStream var49;
            boolean var10001;
            try {
               var49 = this.mContext.getContentResolver().openInputStream(var1);
            } catch (Throwable var48) {
               var10000 = var48;
               var10001 = false;
               break label426;
            }

            var4 = var49;
            var5 = (FileOutputStream)var6;

            FileOutputStream var51;
            try {
               var51 = new FileOutputStream(new File(var2.getPath()));
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label426;
            }

            if (var49 != null) {
               label427: {
                  var4 = var49;
                  var5 = var51;

                  byte[] var52;
                  try {
                     var52 = new byte[1024];
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label427;
                  }

                  while(true) {
                     var4 = var49;
                     var5 = var51;

                     int var3;
                     try {
                        var3 = var49.read(var52);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break;
                     }

                     if (var3 <= 0) {
                        BitmapLoadUtils.close(var51);
                        BitmapLoadUtils.close(var49);
                        this.mInputUri = this.mOutputUri;
                        return;
                     }

                     var4 = var49;
                     var5 = var51;

                     try {
                        var51.write(var52, 0, var3);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break;
                     }
                  }
               }
            } else {
               var4 = var49;
               var5 = var51;

               label411:
               try {
                  throw new NullPointerException("InputStream for given input Uri is null");
               } catch (Throwable var46) {
                  var10000 = var46;
                  var10001 = false;
                  break label411;
               }
            }
         }

         Throwable var50 = var10000;
         BitmapLoadUtils.close(var5);
         BitmapLoadUtils.close(var4);
         this.mInputUri = this.mOutputUri;
         throw var50;
      } else {
         throw new NullPointerException("Output Uri is null - cannot copy image");
      }
   }

   private void downloadFile(Uri var1, Uri var2) throws NullPointerException, IOException {
      Log.d("BitmapWorkerTask", "downloadFile");
      if (var2 != null) {
         OkHttpClient var8 = new OkHttpClient();
         BufferedSource var7 = null;
         Object var6 = null;
         Response var5 = null;
         BufferedSource var3 = var7;
         Sink var4 = (Sink)var6;

         Throwable var10000;
         label461: {
            boolean var10001;
            Response var51;
            try {
               var51 = var8.newCall((new Request.Builder()).url(var1.toString()).build()).execute();
            } catch (Throwable var50) {
               var10000 = var50;
               var10001 = false;
               break label461;
            }

            var3 = var7;
            var4 = (Sink)var6;
            var5 = var51;

            try {
               var7 = var51.body().source();
            } catch (Throwable var49) {
               var10000 = var49;
               var10001 = false;
               break label461;
            }

            var3 = var7;
            var4 = (Sink)var6;
            var5 = var51;

            OutputStream var53;
            try {
               var53 = this.mContext.getContentResolver().openOutputStream(var2);
            } catch (Throwable var48) {
               var10000 = var48;
               var10001 = false;
               break label461;
            }

            if (var53 != null) {
               label462: {
                  var3 = var7;
                  var4 = (Sink)var6;
                  var5 = var51;

                  Sink var54;
                  try {
                     var54 = Okio.sink(var53);
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label462;
                  }

                  var3 = var7;
                  var4 = var54;
                  var5 = var51;

                  try {
                     var7.readAll(var54);
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label462;
                  }

                  BitmapLoadUtils.close(var7);
                  BitmapLoadUtils.close(var54);
                  if (var51 != null) {
                     BitmapLoadUtils.close(var51.body());
                  }

                  var8.dispatcher().cancelAll();
                  this.mInputUri = this.mOutputUri;
                  return;
               }
            } else {
               var3 = var7;
               var4 = (Sink)var6;
               var5 = var51;

               label442:
               try {
                  throw new NullPointerException("OutputStream for given output Uri is null");
               } catch (Throwable var47) {
                  var10000 = var47;
                  var10001 = false;
                  break label442;
               }
            }
         }

         Throwable var52 = var10000;
         BitmapLoadUtils.close(var3);
         BitmapLoadUtils.close(var4);
         if (var5 != null) {
            BitmapLoadUtils.close(var5.body());
         }

         var8.dispatcher().cancelAll();
         this.mInputUri = this.mOutputUri;
         throw var52;
      } else {
         throw new NullPointerException("Output Uri is null - cannot download image");
      }
   }

   private String getFilePath() {
      return ContextCompat.checkSelfPermission(this.mContext, "android.permission.READ_EXTERNAL_STORAGE") == 0 ? FileUtils.getPath(this.mContext, this.mInputUri) : null;
   }

   private void processInputUri() throws NullPointerException, IOException {
      String var1 = this.mInputUri.getScheme();
      StringBuilder var2 = new StringBuilder();
      var2.append("Uri scheme: ");
      var2.append(var1);
      Log.d("BitmapWorkerTask", var2.toString());
      Object var7;
      if (!"http".equals(var1) && !"https".equals(var1)) {
         if ("content".equals(var1)) {
            var1 = this.getFilePath();
            if (!TextUtils.isEmpty(var1) && (new File(var1)).exists()) {
               this.mInputUri = Uri.fromFile(new File(var1));
            } else {
               try {
                  this.copyFile(this.mInputUri, this.mOutputUri);
                  return;
               } catch (NullPointerException var5) {
                  var7 = var5;
               } catch (IOException var6) {
                  var7 = var6;
               }

               Log.e("BitmapWorkerTask", "Copying failed", (Throwable)var7);
               throw var7;
            }
         } else if (!"file".equals(var1)) {
            var2 = new StringBuilder();
            var2.append("Invalid Uri scheme ");
            var2.append(var1);
            Log.e("BitmapWorkerTask", var2.toString());
            var2 = new StringBuilder();
            var2.append("Invalid Uri scheme");
            var2.append(var1);
            throw new IllegalArgumentException(var2.toString());
         }

      } else {
         try {
            this.downloadFile(this.mInputUri, this.mOutputUri);
            return;
         } catch (NullPointerException var3) {
            var7 = var3;
         } catch (IOException var4) {
            var7 = var4;
         }

         Log.e("BitmapWorkerTask", "Downloading failed", (Throwable)var7);
         throw var7;
      }
   }

   protected BitmapLoadTask.BitmapWorkerResult doInBackground(Void... var1) {
      if (this.mInputUri == null) {
         return new BitmapLoadTask.BitmapWorkerResult(new NullPointerException("Input Uri cannot be null"));
      } else {
         label67: {
            Object var13;
            try {
               this.processInputUri();
               break label67;
            } catch (NullPointerException var11) {
               var13 = var11;
            } catch (IOException var12) {
               var13 = var12;
            }

            return new BitmapLoadTask.BitmapWorkerResult((Exception)var13);
         }

         ParcelFileDescriptor var6;
         try {
            var6 = this.mContext.getContentResolver().openFileDescriptor(this.mInputUri, "r");
         } catch (FileNotFoundException var9) {
            return new BitmapLoadTask.BitmapWorkerResult(var9);
         }

         StringBuilder var14;
         if (var6 == null) {
            var14 = new StringBuilder();
            var14.append("ParcelFileDescriptor was null for given Uri: [");
            var14.append(this.mInputUri);
            var14.append("]");
            return new BitmapLoadTask.BitmapWorkerResult(new NullPointerException(var14.toString()));
         } else {
            FileDescriptor var7 = var6.getFileDescriptor();
            Options var8 = new Options();
            var8.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(var7, (Rect)null, var8);
            if (var8.outWidth != -1 && var8.outHeight != -1) {
               var8.inSampleSize = BitmapLoadUtils.calculateInSampleSize(var8, this.mRequiredWidth, this.mRequiredHeight);
               var8.inJustDecodeBounds = false;
               Bitmap var15 = null;
               boolean var2 = false;

               while(!var2) {
                  Bitmap var5;
                  try {
                     var5 = BitmapFactory.decodeFileDescriptor(var7, (Rect)null, var8);
                  } catch (OutOfMemoryError var10) {
                     Log.e("BitmapWorkerTask", "doInBackground: BitmapFactory.decodeFileDescriptor: ", var10);
                     var8.inSampleSize *= 2;
                     continue;
                  }

                  var15 = var5;
                  var2 = true;
               }

               if (var15 == null) {
                  var14 = new StringBuilder();
                  var14.append("Bitmap could not be decoded from the Uri: [");
                  var14.append(this.mInputUri);
                  var14.append("]");
                  return new BitmapLoadTask.BitmapWorkerResult(new IllegalArgumentException(var14.toString()));
               } else {
                  if (VERSION.SDK_INT >= 16) {
                     BitmapLoadUtils.close(var6);
                  }

                  int var16 = BitmapLoadUtils.getExifOrientation(this.mContext, this.mInputUri);
                  int var3 = BitmapLoadUtils.exifToDegrees(var16);
                  int var4 = BitmapLoadUtils.exifToTranslation(var16);
                  ExifInfo var17 = new ExifInfo(var16, var3, var4);
                  Matrix var18 = new Matrix();
                  if (var3 != 0) {
                     var18.preRotate((float)var3);
                  }

                  if (var4 != 1) {
                     var18.postScale((float)var4, 1.0F);
                  }

                  return !var18.isIdentity() ? new BitmapLoadTask.BitmapWorkerResult(BitmapLoadUtils.transformBitmap(var15, var18), var17) : new BitmapLoadTask.BitmapWorkerResult(var15, var17);
               }
            } else {
               var14 = new StringBuilder();
               var14.append("Bounds for bitmap could not be retrieved from the Uri: [");
               var14.append(this.mInputUri);
               var14.append("]");
               return new BitmapLoadTask.BitmapWorkerResult(new IllegalArgumentException(var14.toString()));
            }
         }
      }
   }

   protected void onPostExecute(BitmapLoadTask.BitmapWorkerResult var1) {
      if (var1.mBitmapWorkerException == null) {
         BitmapLoadCallback var2 = this.mBitmapLoadCallback;
         Bitmap var3 = var1.mBitmapResult;
         ExifInfo var4 = var1.mExifInfo;
         String var5 = this.mInputUri.getPath();
         Uri var6 = this.mOutputUri;
         String var7;
         if (var6 == null) {
            var7 = null;
         } else {
            var7 = var6.getPath();
         }

         var2.onBitmapLoaded(var3, var4, var5, var7);
      } else {
         this.mBitmapLoadCallback.onFailure(var1.mBitmapWorkerException);
      }
   }

   public static class BitmapWorkerResult {
      Bitmap mBitmapResult;
      Exception mBitmapWorkerException;
      ExifInfo mExifInfo;

      public BitmapWorkerResult(Bitmap var1, ExifInfo var2) {
         this.mBitmapResult = var1;
         this.mExifInfo = var2;
      }

      public BitmapWorkerResult(Exception var1) {
         this.mBitmapWorkerException = var1;
      }
   }
}
