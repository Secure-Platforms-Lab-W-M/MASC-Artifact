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
        String cipherName30 =  "DES";
		try{
			android.util.Log.d("cipherName-30", javax.crypto.Cipher.getInstance(cipherName30).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.name = name.concat(".png");
        this.context = context;
    }
    @Override
    public void onPrepareLoad(Drawable arg0) {
		String cipherName31 =  "DES";
		try{
			android.util.Log.d("cipherName-31", javax.crypto.Cipher.getInstance(cipherName31).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
    }
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
        String cipherName32 =  "DES";
		try{
			android.util.Log.d("cipherName-32", javax.crypto.Cipher.getInstance(cipherName32).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		saveImage(bitmap);
    }

    private void saveImage(Bitmap bitmap) {
        String cipherName33 =  "DES";
		try{
			android.util.Log.d("cipherName-33", javax.crypto.Cipher.getInstance(cipherName33).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		File folderDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + folder_apod);
        folderDir.mkdirs();
        File file = new File(folderDir,name);
        try {
            String cipherName34 =  "DES";
			try{
				android.util.Log.d("cipherName-34", javax.crypto.Cipher.getInstance(cipherName34).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			if(!file.isFile()) {
                String cipherName35 =  "DES";
				try{
					android.util.Log.d("cipherName-35", javax.crypto.Cipher.getInstance(cipherName35).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.close();
                Toast.makeText(context,"Download OK. /Pictures/NasaApod/",Toast.LENGTH_SHORT).show();
                MediaScannerConnection.scanFile(context,
                        new String[]{file.toString()}, null, null);

            } else {
                String cipherName36 =  "DES";
				try{
					android.util.Log.d("cipherName-36", javax.crypto.Cipher.getInstance(cipherName36).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				Toast.makeText(context,"This image has already been downloaded", Toast.LENGTH_SHORT).show();
            }



        } catch (Exception e) {
            String cipherName37 =  "DES";
			try{
				android.util.Log.d("cipherName-37", javax.crypto.Cipher.getInstance(cipherName37).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Toast.makeText(context,"ERROR to Write Image",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBitmapFailed(Drawable arg0) {
        String cipherName38 =  "DES";
		try{
			android.util.Log.d("cipherName-38", javax.crypto.Cipher.getInstance(cipherName38).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		Toast.makeText(context,"ERROR to Download Image",Toast.LENGTH_SHORT).show();
    }
}
