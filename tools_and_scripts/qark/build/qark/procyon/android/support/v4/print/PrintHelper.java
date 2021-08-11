// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.print;

import android.os.CancellationSignal$OnCancelListener;
import android.print.PrintDocumentInfo$Builder;
import android.os.Bundle;
import android.print.PrintDocumentAdapter$LayoutResultCallback;
import android.print.PrintDocumentAdapter;
import android.print.PrintAttributes$MediaSize;
import android.print.PrintManager;
import android.print.PrintAttributes$Builder;
import android.print.PageRange;
import android.graphics.pdf.PdfDocument$Page;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.print.pdf.PrintedPdfDocument;
import android.os.AsyncTask;
import android.print.PrintAttributes$Margins;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.print.PrintDocumentAdapter$WriteResultCallback;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PrintAttributes;
import android.graphics.BitmapFactory$Options;
import android.support.annotation.RequiresApi;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.io.FileNotFoundException;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Build$VERSION;
import android.content.Context;

public final class PrintHelper
{
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    private final PrintHelperVersionImpl mImpl;
    
    public PrintHelper(final Context context) {
        if (Build$VERSION.SDK_INT >= 24) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi24(context);
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi23(context);
            return;
        }
        if (Build$VERSION.SDK_INT >= 20) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi20(context);
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi19(context);
            return;
        }
        this.mImpl = (PrintHelperVersionImpl)new PrintHelperStub();
    }
    
    public static boolean systemSupportsPrint() {
        return Build$VERSION.SDK_INT >= 19;
    }
    
    public int getColorMode() {
        return this.mImpl.getColorMode();
    }
    
    public int getOrientation() {
        return this.mImpl.getOrientation();
    }
    
    public int getScaleMode() {
        return this.mImpl.getScaleMode();
    }
    
    public void printBitmap(final String s, final Bitmap bitmap) {
        this.mImpl.printBitmap(s, bitmap, null);
    }
    
    public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        this.mImpl.printBitmap(s, bitmap, onPrintFinishCallback);
    }
    
    public void printBitmap(final String s, final Uri uri) throws FileNotFoundException {
        this.mImpl.printBitmap(s, uri, null);
    }
    
    public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        this.mImpl.printBitmap(s, uri, onPrintFinishCallback);
    }
    
    public void setColorMode(final int colorMode) {
        this.mImpl.setColorMode(colorMode);
    }
    
    public void setOrientation(final int orientation) {
        this.mImpl.setOrientation(orientation);
    }
    
    public void setScaleMode(final int scaleMode) {
        this.mImpl.setScaleMode(scaleMode);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface ColorMode {
    }
    
    public interface OnPrintFinishCallback
    {
        void onFinish();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface Orientation {
    }
    
    @RequiresApi(19)
    private static class PrintHelperApi19 implements PrintHelperVersionImpl
    {
        private static final String LOG_TAG = "PrintHelperApi19";
        private static final int MAX_PRINT_SIZE = 3500;
        int mColorMode;
        final Context mContext;
        BitmapFactory$Options mDecodeOptions;
        protected boolean mIsMinMarginsHandlingCorrect;
        private final Object mLock;
        int mOrientation;
        protected boolean mPrintActivityRespectsOrientation;
        int mScaleMode;
        
        PrintHelperApi19(final Context mContext) {
            this.mDecodeOptions = null;
            this.mLock = new Object();
            this.mScaleMode = 2;
            this.mColorMode = 2;
            this.mPrintActivityRespectsOrientation = true;
            this.mIsMinMarginsHandlingCorrect = true;
            this.mContext = mContext;
        }
        
        private Bitmap convertBitmapForColorMode(final Bitmap bitmap, final int n) {
            if (n != 1) {
                return bitmap;
            }
            final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap2);
            final Paint paint = new Paint();
            final ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            canvas.setBitmap((Bitmap)null);
            return bitmap2;
        }
        
        private Matrix getMatrix(final int n, final int n2, final RectF rectF, final int n3) {
            final Matrix matrix = new Matrix();
            final float n4 = rectF.width() / n;
            float n5;
            if (n3 == 2) {
                n5 = Math.max(n4, rectF.height() / n2);
            }
            else {
                n5 = Math.min(n4, rectF.height() / n2);
            }
            matrix.postScale(n5, n5);
            matrix.postTranslate((rectF.width() - n * n5) / 2.0f, (rectF.height() - n2 * n5) / 2.0f);
            return matrix;
        }
        
        private static boolean isPortrait(final Bitmap bitmap) {
            return bitmap.getWidth() <= bitmap.getHeight();
        }
        
        private Bitmap loadBitmap(final Uri uri, BitmapFactory$Options decodeStream) throws FileNotFoundException {
            if (uri != null) {
                final Context mContext = this.mContext;
                if (mContext != null) {
                    InputStream openInputStream = null;
                    try {
                        final InputStream inputStream = openInputStream = mContext.getContentResolver().openInputStream(uri);
                        decodeStream = (BitmapFactory$Options)BitmapFactory.decodeStream(inputStream, (Rect)null, decodeStream);
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                                return (Bitmap)decodeStream;
                            }
                            catch (IOException ex) {
                                Log.w("PrintHelperApi19", "close fail ", (Throwable)ex);
                                return (Bitmap)decodeStream;
                            }
                        }
                        return (Bitmap)decodeStream;
                    }
                    finally {
                        if (openInputStream != null) {
                            try {
                                openInputStream.close();
                            }
                            catch (IOException ex2) {
                                Log.w("PrintHelperApi19", "close fail ", (Throwable)ex2);
                            }
                        }
                    }
                }
            }
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        
        private Bitmap loadConstrainedBitmap(final Uri uri) throws FileNotFoundException {
            if (uri == null || this.mContext == null) {
                throw new IllegalArgumentException("bad argument to getScaledBitmap");
            }
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            this.loadBitmap(uri, bitmapFactory$Options);
            final int outWidth = bitmapFactory$Options.outWidth;
            final int outHeight = bitmapFactory$Options.outHeight;
            if (outWidth <= 0) {
                return null;
            }
            if (outHeight <= 0) {
                return null;
            }
            int i;
            int inSampleSize;
            for (i = Math.max(outWidth, outHeight), inSampleSize = 1; i > 3500; i >>>= 1, inSampleSize <<= 1) {}
            if (inSampleSize <= 0) {
                return null;
            }
            if (Math.min(outWidth, outHeight) / inSampleSize <= 0) {
                return null;
            }
            final Object mLock = this.mLock;
            // monitorenter(mLock)
            try {
                this.mDecodeOptions = new BitmapFactory$Options();
                this.mDecodeOptions.inMutable = true;
                this.mDecodeOptions.inSampleSize = inSampleSize;
                final BitmapFactory$Options mDecodeOptions = this.mDecodeOptions;
                try {
                    // monitorexit(mLock)
                    try {
                        final Bitmap loadBitmap = this.loadBitmap(uri, mDecodeOptions);
                        synchronized (this.mLock) {
                            this.mDecodeOptions = null;
                            return loadBitmap;
                        }
                    }
                    finally {
                        synchronized (this.mLock) {
                            this.mDecodeOptions = null;
                        }
                        // monitorexit(this.mLock)
                    }
                }
                finally {}
            }
            finally {}
        }
        // monitorexit(mLock)
        
        private void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
            PrintAttributes build;
            if (this.mIsMinMarginsHandlingCorrect) {
                build = printAttributes;
            }
            else {
                build = this.copyAttributes(printAttributes).setMinMargins(new PrintAttributes$Margins(0, 0, 0, 0)).build();
            }
            new AsyncTask<Void, Void, Throwable>() {
                protected Throwable doInBackground(final Void... array) {
                    while (true) {
                        while (true) {
                            Label_0407: {
                                try {
                                    if (cancellationSignal.isCanceled()) {
                                        return null;
                                    }
                                    final PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument(PrintHelperApi19.this.mContext, build);
                                    final Bitmap access$100 = PrintHelperApi19.this.convertBitmapForColorMode(bitmap, build.getColorMode());
                                    if (cancellationSignal.isCanceled()) {
                                        return null;
                                    }
                                    try {
                                        final PdfDocument$Page startPage = printedPdfDocument.startPage(1);
                                        RectF rectF;
                                        if (PrintHelperApi19.this.mIsMinMarginsHandlingCorrect) {
                                            rectF = new RectF(startPage.getInfo().getContentRect());
                                        }
                                        else {
                                            final PrintedPdfDocument printedPdfDocument2 = new PrintedPdfDocument(PrintHelperApi19.this.mContext, printAttributes);
                                            final PdfDocument$Page startPage2 = printedPdfDocument2.startPage(1);
                                            rectF = new RectF(startPage2.getInfo().getContentRect());
                                            printedPdfDocument2.finishPage(startPage2);
                                            printedPdfDocument2.close();
                                        }
                                        final Matrix access$101 = PrintHelperApi19.this.getMatrix(access$100.getWidth(), access$100.getHeight(), rectF, n);
                                        if (!PrintHelperApi19.this.mIsMinMarginsHandlingCorrect) {
                                            access$101.postTranslate(rectF.left, rectF.top);
                                            startPage.getCanvas().clipRect(rectF);
                                        }
                                        startPage.getCanvas().drawBitmap(access$100, access$101, (Paint)null);
                                        printedPdfDocument.finishPage(startPage);
                                        if (cancellationSignal.isCanceled()) {
                                            printedPdfDocument.close();
                                            if (parcelFileDescriptor != null) {
                                                try {
                                                    parcelFileDescriptor.close();
                                                }
                                                catch (IOException ex) {}
                                            }
                                            if (access$100 != bitmap) {
                                                access$100.recycle();
                                                return null;
                                            }
                                            return null;
                                        }
                                        else {
                                            printedPdfDocument.writeTo((OutputStream)new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                                            printedPdfDocument.close();
                                            if (parcelFileDescriptor != null) {
                                                try {
                                                    parcelFileDescriptor.close();
                                                }
                                                catch (IOException ex2) {}
                                            }
                                            if (access$100 != bitmap) {
                                                access$100.recycle();
                                                return null;
                                            }
                                            break;
                                        }
                                    }
                                    finally {
                                        printedPdfDocument.close();
                                        if (parcelFileDescriptor != null) {
                                            try {
                                                parcelFileDescriptor.close();
                                            }
                                            catch (IOException ex3) {}
                                        }
                                        if (access$100 == bitmap) {
                                            break Label_0407;
                                        }
                                        access$100.recycle();
                                    }
                                }
                                catch (Throwable t) {
                                    return t;
                                }
                                break;
                            }
                            continue;
                        }
                    }
                    return null;
                }
                
                protected void onPostExecute(final Throwable t) {
                    if (cancellationSignal.isCanceled()) {
                        printDocumentAdapter$WriteResultCallback.onWriteCancelled();
                        return;
                    }
                    if (t == null) {
                        printDocumentAdapter$WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
                        return;
                    }
                    Log.e("PrintHelperApi19", "Error writing printed content", t);
                    printDocumentAdapter$WriteResultCallback.onWriteFailed((CharSequence)null);
                }
            }.execute((Object[])new Void[0]);
        }
        
        protected PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
            final PrintAttributes$Builder setMinMargins = new PrintAttributes$Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
            if (printAttributes.getColorMode() != 0) {
                setMinMargins.setColorMode(printAttributes.getColorMode());
                return setMinMargins;
            }
            return setMinMargins;
        }
        
        @Override
        public int getColorMode() {
            return this.mColorMode;
        }
        
        @Override
        public int getOrientation() {
            final int mOrientation = this.mOrientation;
            if (mOrientation == 0) {
                return 1;
            }
            return mOrientation;
        }
        
        @Override
        public int getScaleMode() {
            return this.mScaleMode;
        }
        
        @Override
        public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
            if (bitmap == null) {
                return;
            }
            final int mScaleMode = this.mScaleMode;
            final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes$MediaSize mediaSize;
            if (isPortrait(bitmap)) {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_PORTRAIT;
            }
            else {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE;
            }
            printManager.print(s, (PrintDocumentAdapter)new PrintDocumentAdapter() {
                private PrintAttributes mAttributes;
                
                public void onFinish() {
                    final OnPrintFinishCallback val$callback = onPrintFinishCallback;
                    if (val$callback != null) {
                        val$callback.onFinish();
                    }
                }
                
                public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                    this.mAttributes = mAttributes;
                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
                }
                
                public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                    PrintHelperApi19.this.writeBitmap(this.mAttributes, mScaleMode, bitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
                }
            }, new PrintAttributes$Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
        
        @Override
        public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
            final PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
                private PrintAttributes mAttributes;
                Bitmap mBitmap = null;
                AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
                final /* synthetic */ int val$fittingMode = PrintHelperApi19.this.mScaleMode;
                
                private void cancelLoad() {
                    while (true) {
                        synchronized (PrintHelperApi19.this.mLock) {
                            if (PrintHelperApi19.this.mDecodeOptions != null) {
                                PrintHelperApi19.this.mDecodeOptions.requestCancelDecode();
                                PrintHelperApi19.this.mDecodeOptions = null;
                            }
                        }
                    }
                }
                
                public void onFinish() {
                    super.onFinish();
                    this.cancelLoad();
                    final AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap = this.mLoadBitmap;
                    if (mLoadBitmap != null) {
                        mLoadBitmap.cancel(true);
                    }
                    final OnPrintFinishCallback val$callback = onPrintFinishCallback;
                    if (val$callback != null) {
                        val$callback.onFinish();
                    }
                    final Bitmap mBitmap = this.mBitmap;
                    if (mBitmap != null) {
                        mBitmap.recycle();
                        this.mBitmap = null;
                    }
                }
                
                public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                    synchronized (this) {
                        this.mAttributes = mAttributes;
                        // monitorexit(this)
                        if (cancellationSignal.isCanceled()) {
                            printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                            return;
                        }
                        if (this.mBitmap != null) {
                            printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
                            return;
                        }
                        this.mLoadBitmap = (AsyncTask<Uri, Boolean, Bitmap>)new AsyncTask<Uri, Boolean, Bitmap>() {
                            protected Bitmap doInBackground(final Uri... array) {
                                try {
                                    return PrintHelperApi19.this.loadConstrainedBitmap(uri);
                                }
                                catch (FileNotFoundException ex) {
                                    return null;
                                }
                            }
                            
                            protected void onCancelled(final Bitmap bitmap) {
                                printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                                PrintDocumentAdapter.this.mLoadBitmap = null;
                            }
                            
                            protected void onPostExecute(Bitmap bitmap) {
                                super.onPostExecute(bitmap);
                                Label_0115: {
                                    if (bitmap != null && (!PrintHelperApi19.this.mPrintActivityRespectsOrientation || PrintHelperApi19.this.mOrientation == 0)) {
                                        // monitorenter(this)
                                        try {
                                            final PrintAttributes$MediaSize mediaSize = PrintDocumentAdapter.this.mAttributes.getMediaSize();
                                            try {
                                                // monitorexit(this)
                                                if (mediaSize == null) {
                                                    break Label_0115;
                                                }
                                                if (mediaSize.isPortrait() != isPortrait((Bitmap)bitmap)) {
                                                    final Matrix matrix = new Matrix();
                                                    matrix.postRotate(90.0f);
                                                    bitmap = Bitmap.createBitmap((Bitmap)bitmap, 0, 0, ((Bitmap)bitmap).getWidth(), ((Bitmap)bitmap).getHeight(), matrix, true);
                                                }
                                                break Label_0115;
                                            }
                                            finally {}
                                        }
                                        finally {}
                                    }
                                    // monitorexit(this)
                                }
                                final PrintDocumentAdapter this$1 = PrintDocumentAdapter.this;
                                if ((this$1.mBitmap = (Bitmap)bitmap) != null) {
                                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build(), true ^ mAttributes.equals((Object)printAttributes));
                                }
                                else {
                                    printDocumentAdapter$LayoutResultCallback.onLayoutFailed((CharSequence)null);
                                }
                                PrintDocumentAdapter.this.mLoadBitmap = null;
                            }
                            
                            protected void onPreExecute() {
                                cancellationSignal.setOnCancelListener((CancellationSignal$OnCancelListener)new CancellationSignal$OnCancelListener() {
                                    public void onCancel() {
                                        PrintHelper$PrintHelperApi19$3.this.cancelLoad();
                                        AsyncTask.this.cancel(false);
                                    }
                                });
                            }
                        }.execute((Object[])new Uri[0]);
                    }
                }
                
                public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                    PrintHelperApi19.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
                }
            };
            final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            final PrintAttributes$Builder printAttributes$Builder = new PrintAttributes$Builder();
            printAttributes$Builder.setColorMode(this.mColorMode);
            final int mOrientation = this.mOrientation;
            if (mOrientation != 1 && mOrientation != 0) {
                if (mOrientation == 2) {
                    printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_PORTRAIT);
                }
            }
            else {
                printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE);
            }
            printManager.print(s, (PrintDocumentAdapter)printDocumentAdapter, printAttributes$Builder.build());
        }
        
        @Override
        public void setColorMode(final int mColorMode) {
            this.mColorMode = mColorMode;
        }
        
        @Override
        public void setOrientation(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
        
        @Override
        public void setScaleMode(final int mScaleMode) {
            this.mScaleMode = mScaleMode;
        }
    }
    
    @RequiresApi(20)
    private static class PrintHelperApi20 extends PrintHelperApi19
    {
        PrintHelperApi20(final Context context) {
            super(context);
            this.mPrintActivityRespectsOrientation = false;
        }
    }
    
    @RequiresApi(23)
    private static class PrintHelperApi23 extends PrintHelperApi20
    {
        PrintHelperApi23(final Context context) {
            super(context);
            this.mIsMinMarginsHandlingCorrect = false;
        }
        
        @Override
        protected PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
            final PrintAttributes$Builder copyAttributes = super.copyAttributes(printAttributes);
            if (printAttributes.getDuplexMode() != 0) {
                copyAttributes.setDuplexMode(printAttributes.getDuplexMode());
                return copyAttributes;
            }
            return copyAttributes;
        }
    }
    
    @RequiresApi(24)
    private static class PrintHelperApi24 extends PrintHelperApi23
    {
        PrintHelperApi24(final Context context) {
            super(context);
            this.mIsMinMarginsHandlingCorrect = true;
            this.mPrintActivityRespectsOrientation = true;
        }
    }
    
    private static final class PrintHelperStub implements PrintHelperVersionImpl
    {
        int mColorMode;
        int mOrientation;
        int mScaleMode;
        
        private PrintHelperStub() {
            this.mScaleMode = 2;
            this.mColorMode = 2;
            this.mOrientation = 1;
        }
        
        @Override
        public int getColorMode() {
            return this.mColorMode;
        }
        
        @Override
        public int getOrientation() {
            return this.mOrientation;
        }
        
        @Override
        public int getScaleMode() {
            return this.mScaleMode;
        }
        
        @Override
        public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        }
        
        @Override
        public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) {
        }
        
        @Override
        public void setColorMode(final int mColorMode) {
            this.mColorMode = mColorMode;
        }
        
        @Override
        public void setOrientation(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
        
        @Override
        public void setScaleMode(final int mScaleMode) {
            this.mScaleMode = mScaleMode;
        }
    }
    
    interface PrintHelperVersionImpl
    {
        int getColorMode();
        
        int getOrientation();
        
        int getScaleMode();
        
        void printBitmap(final String p0, final Bitmap p1, final OnPrintFinishCallback p2);
        
        void printBitmap(final String p0, final Uri p1, final OnPrintFinishCallback p2) throws FileNotFoundException;
        
        void setColorMode(final int p0);
        
        void setOrientation(final int p0);
        
        void setScaleMode(final int p0);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface ScaleMode {
    }
}
