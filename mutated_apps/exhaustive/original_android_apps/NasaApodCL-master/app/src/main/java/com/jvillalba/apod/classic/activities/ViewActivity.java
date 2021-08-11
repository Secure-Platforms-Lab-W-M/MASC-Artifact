package com.jvillalba.apod.classic.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jvillalba.apod.classic.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.jvillalba.apod.classic.model.NASA;
import com.jvillalba.apod.classic.model.PicassoDownloader;


public class ViewActivity extends AppCompatActivity {

    private ImageView imageAPOD;
    private NASA nasaAPOD;
    private final int WRITE_EXTERNAL_STORAGE_CODE = 100;
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        nasaAPOD = (NASA) (bundle != null ? bundle.getSerializable("Nasa") : null);

        setDataNasaAPOD(nasaAPOD);

        imageAPOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewActivity.this, ImageActivity.class);
                intent.putExtra("HD_URL", nasaAPOD.getHdurl());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                    imageName = getImageNameApod();
                    checkPermission(imageName);
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkPermission(String imageName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Comprobar si ha aceptado, no ha aceptado, o nunca se le ha preguntado
            if(CheckPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                downloadPicasso(imageName);
            }
            else {
                // Ha denegado o es la primera vez que se le pregunta
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // No se le ha preguntado aún
                    ActivityCompat.requestPermissions(ViewActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
                }else {
                    // Ha denegado
                    Toast.makeText(ViewActivity.this, "Please, enable the storage permission", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }
        } else {
            OlderVersions(imageName);
        }
    }

    private void downloadPicasso(String imageName) {
        Picasso.with(this)
                .load(nasaAPOD.getUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(new PicassoDownloader(imageName, this));

    }

    private void OlderVersions(String imageName) {
        if (CheckPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            downloadPicasso(imageName);
        } else {
        Toast.makeText(ViewActivity.this, "You Declined The Access", Toast.LENGTH_SHORT).show();
    }


}


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Estamos en el caso del teléfono
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:

                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Comprobar si ha sido aceptado o denegado la petición de permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(ViewActivity.this, "Thanks, Now You Can Download Images", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // No concendió su permiso
                        Toast.makeText(ViewActivity.this, "You Declined The Access", Toast.LENGTH_SHORT).show();
                    }
                }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean CheckPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @NonNull
    private String getImageNameApod() {
        int index = nasaAPOD.getUrl().lastIndexOf('/');
        String fileName = nasaAPOD.getUrl().substring(index +1);
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    private void setDataNasaAPOD(NASA nasaAPOD) {
        imageAPOD = findViewById(R.id.imageAPOD);
        Picasso.with(this)
                .load(nasaAPOD.getUrl())
                .error(R.mipmap.ic_launcher_foreground)
                .fit()
                .into(imageAPOD);

        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setText(getConcat(textTitle,nasaAPOD.getTitle()));

        TextView dateView = findViewById(R.id.textDate);
        dateView.setText(getConcat(dateView,nasaAPOD.getDate()));

        TextView copyright = findViewById(R.id.copyright);
        copyright.setText(getConcat(copyright,nasaAPOD.getCopyright()));

        TextView explanationView = findViewById(R.id.explanation);
        explanationView.setText(nasaAPOD.getExplanation());
        explanationView.setMovementMethod(new ScrollingMovementMethod());
    }

    @NonNull
    private String getConcat(TextView textView,String NasaData) {
        try {
            return (textView.getText().toString().concat(NasaData));
        }
        catch (Exception e)
        {
            return textView.getText().toString();
        }
    }


}


