package com.jvillalba.apod.classic.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

public class PicassoDownloader implements Target {
    private final String name;
    private final Context context;
    private final String folder_apod = "/NasaApod/";
    public PicassoDownloader(String name,Context context) {
        this.name = name.concat(".png");
        this.context = context;
    }
    @Override
    public void onPrepareLoad(Drawable arg0) {
    }
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
        saveImage(bitmap);
    }

    private void saveImage(Bitmap bitmap) {
        File folderDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + folder_apod);
        folderDir.mkdirs();
        File file = new File(folderDir,name);
        try {
            if(!file.isFile()) {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.close();
                Toast.makeText(context,"Download OK. /Pictures/NasaApod/",Toast.LENGTH_SHORT).show();
                MediaScannerConnection.scanFile(context,
                        new String[]{file.toString()}, null, null);

            } else {
                Toast.makeText(context,"This image has already been downloaded", Toast.LENGTH_SHORT).show();
            }



        } catch (Exception e) {
            Toast.makeText(context,"ERROR to Write Image",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBitmapFailed(Drawable arg0) {
        Toast.makeText(context,"ERROR to Download Image",Toast.LENGTH_SHORT).show();
    }
}
