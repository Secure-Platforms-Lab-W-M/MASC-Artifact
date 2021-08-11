// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.activities;

import android.widget.ImageView;
import com.squareup.picasso.MemoryPolicy;
import android.content.Context;
import com.squareup.picasso.Picasso;
import android.view.View$OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.annotation.SuppressLint;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.os.Handler;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity
{
    private static final int UI_ANIMATION_DELAY = 300;
    private View mContentView;
    private View mControlsView;
    private final Handler mHideHandler;
    private final Runnable mHidePart2Runnable;
    private final Runnable mHideRunnable;
    private final Runnable mShowPart2Runnable;
    private boolean mVisible;
    
    public ImageActivity() {
        this.mHideHandler = new Handler();
        this.mHidePart2Runnable = new Runnable() {
            @SuppressLint({ "InlinedApi" })
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.d("cipherName-57", Cipher.getInstance("DES").getAlgorithm());
                        ImageActivity.this.mContentView.setSystemUiVisibility(4871);
                    }
                    catch (NoSuchAlgorithmException ex) {
                        continue;
                    }
                    catch (NoSuchPaddingException ex2) {
                        continue;
                    }
                    break;
                }
            }
        };
        this.mShowPart2Runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.d("cipherName-58", Cipher.getInstance("DES").getAlgorithm());
                        final ActionBar supportActionBar = ImageActivity.this.getSupportActionBar();
                        Label_0044: {
                            if (supportActionBar == null) {
                                break Label_0044;
                            }
                            try {
                                Log.d("cipherName-59", Cipher.getInstance("DES").getAlgorithm());
                                supportActionBar.show();
                                ImageActivity.this.mControlsView.setVisibility(0);
                            }
                            catch (NoSuchAlgorithmException ex) {}
                            catch (NoSuchPaddingException ex2) {}
                        }
                    }
                    catch (NoSuchAlgorithmException ex3) {
                        continue;
                    }
                    catch (NoSuchPaddingException ex4) {
                        continue;
                    }
                    break;
                }
            }
        };
        this.mHideRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.d("cipherName-60", Cipher.getInstance("DES").getAlgorithm());
                        ImageActivity.this.hide();
                    }
                    catch (NoSuchAlgorithmException ex) {
                        continue;
                    }
                    catch (NoSuchPaddingException ex2) {
                        continue;
                    }
                    break;
                }
            }
        };
    }
    
    private void delayedHide() {
        while (true) {
            try {
                Log.d("cipherName-70", Cipher.getInstance("DES").getAlgorithm());
                this.mHideHandler.removeCallbacks(this.mHideRunnable);
                this.mHideHandler.postDelayed(this.mHideRunnable, 100L);
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    private void hide() {
        while (true) {
            try {
                Log.d("cipherName-67", Cipher.getInstance("DES").getAlgorithm());
                final ActionBar supportActionBar = this.getSupportActionBar();
                Label_0041: {
                    if (supportActionBar == null) {
                        break Label_0041;
                    }
                    try {
                        Log.d("cipherName-68", Cipher.getInstance("DES").getAlgorithm());
                        supportActionBar.hide();
                        this.mControlsView.setVisibility(8);
                        this.mVisible = false;
                        this.mHideHandler.removeCallbacks(this.mShowPart2Runnable);
                        this.mHideHandler.postDelayed(this.mHidePart2Runnable, 300L);
                    }
                    catch (NoSuchAlgorithmException ex) {}
                    catch (NoSuchPaddingException ex2) {}
                }
            }
            catch (NoSuchAlgorithmException ex3) {
                continue;
            }
            catch (NoSuchPaddingException ex4) {
                continue;
            }
            break;
        }
    }
    
    @SuppressLint({ "InlinedApi" })
    private void show() {
        while (true) {
            try {
                Log.d("cipherName-69", Cipher.getInstance("DES").getAlgorithm());
                this.mContentView.setSystemUiVisibility(1536);
                this.mVisible = true;
                this.mHideHandler.removeCallbacks(this.mHidePart2Runnable);
                this.mHideHandler.postDelayed(this.mShowPart2Runnable, 300L);
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    private void toggle() {
        while (true) {
            try {
                Log.d("cipherName-64", Cipher.getInstance("DES").getAlgorithm());
                Label_0040: {
                    if (!this.mVisible) {
                        break Label_0040;
                    }
                    try {
                        Log.d("cipherName-65", Cipher.getInstance("DES").getAlgorithm());
                        this.hide();
                        return;
                        try {
                            Log.d("cipherName-66", Cipher.getInstance("DES").getAlgorithm());
                            this.show();
                        }
                        catch (NoSuchAlgorithmException ex) {}
                        catch (NoSuchPaddingException ex2) {}
                    }
                    catch (NoSuchAlgorithmException ex3) {}
                    catch (NoSuchPaddingException ex4) {}
                }
            }
            catch (NoSuchAlgorithmException ex5) {
                continue;
            }
            catch (NoSuchPaddingException ex6) {
                continue;
            }
            break;
        }
    }
    
    @Override
    protected void onCreate(Bundle extras) {
        super.onCreate(extras);
        while (true) {
            try {
                Log.d("cipherName-61", Cipher.getInstance("DES").getAlgorithm());
                extras = this.getIntent().getExtras();
                String string;
                if (extras != null) {
                    string = extras.getString("HD_URL");
                }
                else {
                    string = null;
                }
                this.setContentView(2131361819);
                this.mVisible = true;
                this.mControlsView = this.findViewById(2131230798);
                (this.mContentView = this.findViewById(2131230797)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        while (true) {
                            try {
                                Log.d("cipherName-62", Cipher.getInstance("DES").getAlgorithm());
                                ImageActivity.this.toggle();
                            }
                            catch (NoSuchAlgorithmException ex) {
                                continue;
                            }
                            catch (NoSuchPaddingException ex2) {
                                continue;
                            }
                            break;
                        }
                    }
                });
                Picasso.with((Context)this).load(string).error(2131492865).memoryPolicy(MemoryPolicy.NO_STORE, new MemoryPolicy[0]).into((ImageView)this.mContentView);
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    @Override
    protected void onPostCreate(final Bundle bundle) {
        super.onPostCreate(bundle);
        while (true) {
            try {
                Log.d("cipherName-63", Cipher.getInstance("DES").getAlgorithm());
                this.delayedHide();
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
}
