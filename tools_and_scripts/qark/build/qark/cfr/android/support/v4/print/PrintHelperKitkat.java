/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.ColorMatrix
 *  android.graphics.ColorMatrixColorFilter
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.pdf.PdfDocument
 *  android.graphics.pdf.PdfDocument$Page
 *  android.graphics.pdf.PdfDocument$PageInfo
 *  android.net.Uri
 *  android.os.AsyncTask
 *  android.os.Bundle
 *  android.os.CancellationSignal
 *  android.os.CancellationSignal$OnCancelListener
 *  android.os.ParcelFileDescriptor
 *  android.print.PageRange
 *  android.print.PrintAttributes
 *  android.print.PrintAttributes$Builder
 *  android.print.PrintAttributes$MediaSize
 *  android.print.PrintDocumentAdapter
 *  android.print.PrintDocumentAdapter$LayoutResultCallback
 *  android.print.PrintDocumentAdapter$WriteResultCallback
 *  android.print.PrintDocumentInfo
 *  android.print.PrintDocumentInfo$Builder
 *  android.print.PrintJob
 *  android.print.PrintManager
 *  android.print.pdf.PrintedPdfDocument
 *  android.util.Log
 */
package android.support.v4.print;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class PrintHelperKitkat {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode = 2;
    final Context mContext;
    BitmapFactory.Options mDecodeOptions = null;
    private final Object mLock = new Object();
    int mOrientation = 1;
    int mScaleMode = 2;

    PrintHelperKitkat(Context context) {
        this.mContext = context;
    }

    private Bitmap convertBitmapForColorMode(Bitmap bitmap, int n) {
        if (n != 1) {
            return bitmap;
        }
        Bitmap bitmap2 = Bitmap.createBitmap((int)bitmap.getWidth(), (int)bitmap.getHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.setBitmap(null);
        return bitmap2;
    }

    private Matrix getMatrix(int n, int n2, RectF rectF, int n3) {
        Matrix matrix = new Matrix();
        float f = rectF.width() / (float)n;
        f = n3 == 2 ? Math.max(f, rectF.height() / (float)n2) : Math.min(f, rectF.height() / (float)n2);
        matrix.postScale(f, f);
        matrix.postTranslate((rectF.width() - (float)n * f) / 2.0f, (rectF.height() - (float)n2 * f) / 2.0f);
        return matrix;
    }

    private Bitmap loadBitmap(Uri object, BitmapFactory.Options options) throws FileNotFoundException {
        if (object != null && this.mContext != null) {
            block9 : {
                Object object2 = null;
                try {
                    object2 = object = this.mContext.getContentResolver().openInputStream((Uri)object);
                }
                catch (Throwable throwable) {
                    if (object2 != null) {
                        try {
                            object2.close();
                        }
                        catch (IOException iOException) {
                            Log.w((String)"PrintHelperKitkat", (String)"close fail ", (Throwable)iOException);
                        }
                    }
                    throw throwable;
                }
                options = BitmapFactory.decodeStream((InputStream)object, (Rect)null, (BitmapFactory.Options)options);
                if (object == null) break block9;
                try {
                    object.close();
                    return options;
                }
                catch (IOException iOException) {
                    Log.w((String)"PrintHelperKitkat", (String)"close fail ", (Throwable)iOException);
                }
            }
            return options;
        }
        throw new IllegalArgumentException("bad argument to loadBitmap");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Bitmap loadConstrainedBitmap(Uri object, int n) throws FileNotFoundException {
        BitmapFactory.Options options;
        if (n <= 0) throw new IllegalArgumentException("bad argument to getScaledBitmap");
        if (object == null) throw new IllegalArgumentException("bad argument to getScaledBitmap");
        if (this.mContext == null) throw new IllegalArgumentException("bad argument to getScaledBitmap");
        Object object2 = new BitmapFactory.Options();
        object2.inJustDecodeBounds = true;
        this.loadBitmap((Uri)object, (BitmapFactory.Options)object2);
        int n2 = object2.outWidth;
        int n3 = object2.outHeight;
        if (n2 <= 0) return null;
        if (n3 <= 0) {
            return null;
        }
        int n4 = 1;
        for (int i = Math.max((int)n2, (int)n3); i > n; i >>>= 1, n4 <<= 1) {
        }
        if (n4 <= 0) return null;
        if (Math.min(n2, n3) / n4 <= 0) {
            return null;
        }
        object2 = this.mLock;
        synchronized (object2) {
            this.mDecodeOptions = new BitmapFactory.Options();
            this.mDecodeOptions.inMutable = true;
            this.mDecodeOptions.inSampleSize = n4;
            options = this.mDecodeOptions;
        }
        try {
            object2 = this.loadBitmap((Uri)object, options);
            return object2;
        }
        finally {
            object = this.mLock;
            synchronized (object) {
                this.mDecodeOptions = null;
            }
        }
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void printBitmap(final String string, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        if (bitmap == null) {
            return;
        }
        final int n = this.mScaleMode;
        PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        PrintAttributes.MediaSize mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
        }
        mediaSize = new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
        printManager.print(string, new PrintDocumentAdapter(){
            private PrintAttributes mAttributes;

            public void onFinish() {
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
            }

            public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                this.mAttributes = printAttributes2;
                layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(string).setContentType(1).setPageCount(1).build(), true ^ printAttributes2.equals((Object)printAttributes));
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onWrite(PageRange[] bitmap2, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                cancellationSignal = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
                bitmap2 = PrintHelperKitkat.this.convertBitmapForColorMode(bitmap, this.mAttributes.getColorMode());
                try {
                    PdfDocument.Page page = cancellationSignal.startPage(1);
                    RectF rectF = new RectF(page.getInfo().getContentRect());
                    rectF = PrintHelperKitkat.this.getMatrix(bitmap2.getWidth(), bitmap2.getHeight(), rectF, n);
                    page.getCanvas().drawBitmap(bitmap2, (Matrix)rectF, null);
                    cancellationSignal.finishPage(page);
                    try {
                        cancellationSignal.writeTo((OutputStream)new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                        writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                        return;
                    }
                    catch (IOException iOException) {
                        Log.e((String)"PrintHelperKitkat", (String)"Error writing printed content", (Throwable)iOException);
                        writeResultCallback.onWriteFailed(null);
                        return;
                    }
                }
                finally {
                    cancellationSignal.close();
                    if (parcelFileDescriptor != null) {
                        try {
                            parcelFileDescriptor.close();
                        }
                        catch (IOException iOException) {}
                    }
                    if (bitmap2 != bitmap) {
                        bitmap2.recycle();
                    }
                }
            }
        }, (PrintAttributes)mediaSize);
    }

    public void printBitmap(final String string, Uri object, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        object = new PrintDocumentAdapter((Uri)object, onPrintFinishCallback, this.mScaleMode){
            private PrintAttributes mAttributes;
            Bitmap mBitmap;
            AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
            final /* synthetic */ OnPrintFinishCallback val$callback;
            final /* synthetic */ int val$fittingMode;
            final /* synthetic */ Uri val$imageFile;
            {
                this.val$imageFile = uri;
                this.val$callback = onPrintFinishCallback;
                this.val$fittingMode = n;
                this.mBitmap = null;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            private void cancelLoad() {
                Object object = PrintHelperKitkat.this.mLock;
                synchronized (object) {
                    if (PrintHelperKitkat.this.mDecodeOptions != null) {
                        PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                        PrintHelperKitkat.this.mDecodeOptions = null;
                    }
                    return;
                }
            }

            public void onFinish() {
                super.onFinish();
                this.cancelLoad();
                if (this.mLoadBitmap != null) {
                    this.mLoadBitmap.cancel(true);
                }
                if (this.val$callback != null) {
                    this.val$callback.onFinish();
                }
                if (this.mBitmap != null) {
                    this.mBitmap.recycle();
                    this.mBitmap = null;
                }
            }

            public void onLayout(final PrintAttributes printAttributes, final PrintAttributes printAttributes2, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                this.mAttributes = printAttributes2;
                if (cancellationSignal.isCanceled()) {
                    layoutResultCallback.onLayoutCancelled();
                    return;
                }
                if (this.mBitmap != null) {
                    layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(string).setContentType(1).setPageCount(1).build(), true ^ printAttributes2.equals((Object)printAttributes));
                    return;
                }
                this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>(){

                    protected /* varargs */ Bitmap doInBackground(Uri ... bitmap) {
                        try {
                            bitmap = PrintHelperKitkat.this.loadConstrainedBitmap(2.this.val$imageFile, 3500);
                            return bitmap;
                        }
                        catch (FileNotFoundException fileNotFoundException) {
                            return null;
                        }
                    }

                    protected void onCancelled(Bitmap bitmap) {
                        layoutResultCallback.onLayoutCancelled();
                        2.this.mLoadBitmap = null;
                    }

                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute((Object)bitmap);
                        2.this.mBitmap = bitmap;
                        if (bitmap != null) {
                            bitmap = new PrintDocumentInfo.Builder(string).setContentType(1).setPageCount(1).build();
                            boolean bl = printAttributes2.equals((Object)printAttributes);
                            layoutResultCallback.onLayoutFinished((PrintDocumentInfo)bitmap, true ^ bl);
                        } else {
                            layoutResultCallback.onLayoutFailed(null);
                        }
                        2.this.mLoadBitmap = null;
                    }

                    protected void onPreExecute() {
                        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(){

                            public void onCancel() {
                                2.this.cancelLoad();
                                1.this.cancel(false);
                            }
                        });
                    }

                }.execute((Object[])new Uri[0]);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onWrite(PageRange[] bitmap, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                cancellationSignal = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
                bitmap = PrintHelperKitkat.this.convertBitmapForColorMode(this.mBitmap, this.mAttributes.getColorMode());
                try {
                    PdfDocument.Page page = cancellationSignal.startPage(1);
                    RectF rectF = new RectF(page.getInfo().getContentRect());
                    rectF = PrintHelperKitkat.this.getMatrix(this.mBitmap.getWidth(), this.mBitmap.getHeight(), rectF, this.val$fittingMode);
                    page.getCanvas().drawBitmap(bitmap, (Matrix)rectF, null);
                    cancellationSignal.finishPage(page);
                    try {
                        cancellationSignal.writeTo((OutputStream)new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                        writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                        return;
                    }
                    catch (IOException iOException) {
                        Log.e((String)"PrintHelperKitkat", (String)"Error writing printed content", (Throwable)iOException);
                        writeResultCallback.onWriteFailed(null);
                        return;
                    }
                }
                finally {
                    cancellationSignal.close();
                    if (parcelFileDescriptor != null) {
                        try {
                            parcelFileDescriptor.close();
                        }
                        catch (IOException iOException) {}
                    }
                    if (bitmap != this.mBitmap) {
                        bitmap.recycle();
                    }
                }
            }

        };
        onPrintFinishCallback = (PrintManager)this.mContext.getSystemService("print");
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(this.mColorMode);
        if (this.mOrientation == 1) {
            builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
        } else if (this.mOrientation == 2) {
            builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
        }
        onPrintFinishCallback.print(string, (PrintDocumentAdapter)object, builder.build());
    }

    public void setColorMode(int n) {
        this.mColorMode = n;
    }

    public void setOrientation(int n) {
        this.mOrientation = n;
    }

    public void setScaleMode(int n) {
        this.mScaleMode = n;
    }

    public static interface OnPrintFinishCallback {
        public void onFinish();
    }

}

