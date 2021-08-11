package com.jvillalba.apod.classic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.jvillalba.apod.classic.R;

import com.jvillalba.apod.classic.adapter.MyAdapter;
import com.jvillalba.apod.classic.controller.NasaController;
import com.jvillalba.apod.classic.model.NASA;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private MyAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enforceIconBar();

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);


        mAdapter = new MyAdapter(R.layout.recycler_view_item, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NASA nasa, int position) {
                try {
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    intent.putExtra("Nasa", nasa);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        // Enlazamos el layout manager y adaptor directamente al recycler view
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        picassoClearCache(getApplicationContext().getCacheDir());

        getNasaAPODS();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void enforceIconBar() {
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getNasaAPODS() {

        NasaController nasaController = new NasaController();
        nasaController.getNASAAPODS(mAdapter,this.getApplicationContext());


    }

    public void picassoClearCache(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                picassoClearCache(child);
            }
        }

        fileOrDirectory.delete();

    }

}




