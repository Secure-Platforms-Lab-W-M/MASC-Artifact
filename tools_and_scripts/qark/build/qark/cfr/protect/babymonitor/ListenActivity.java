/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.media.AudioTrack
 *  android.media.MediaPlayer
 *  android.media.MediaPlayer$OnCompletionListener
 *  android.os.Bundle
 *  android.util.Log
 *  android.view.View
 *  android.widget.TextView
 */
package protect.babymonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class ListenActivity
extends Activity {
    final String TAG = "BabyMonitor";
    String _address;
    Thread _listenThread;
    String _name;
    int _port;

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void playAlert() {
        MediaPlayer mediaPlayer;
        block11 : {
            try {
                Log.d((String)"cipherName-79", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            if ((mediaPlayer = MediaPlayer.create((Context)this, (int)2130968576)) != null) {
                Log.d((String)"cipherName-80", (String)Cipher.getInstance("DES").getAlgorithm());
                break block11;
            }
            try {
                Log.d((String)"cipherName-82", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            Log.e((String)"BabyMonitor", (String)"Failed to play alert");
            return;
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block11;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        Log.i((String)"BabyMonitor", (String)"Playing alert");
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    Log.d((String)"cipherName-81", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void streamAudio(Socket socket) throws IllegalArgumentException, IllegalStateException, IOException {
        int n2;
        InputStream inputStream;
        AudioTrack audioTrack;
        int n;
        block23 : {
            try {
                Log.d((String)"cipherName-65", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            Log.i((String)"BabyMonitor", (String)"Setting up stream");
            n = AudioTrack.getMinBufferSize((int)11025, (int)4, (int)2);
            audioTrack = new AudioTrack(3, 11025, 4, 2, n, 1);
            this.setVolumeControlStream(3);
            inputStream = socket.getInputStream();
            n2 = 0;
            audioTrack.play();
            try {
                Log.d((String)"cipherName-66", (String)Cipher.getInstance("DES").getAlgorithm());
                break block23;
            }
            catch (Throwable throwable) {
                try {
                    Log.d((String)"cipherName-69", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                audioTrack.stop();
                socket.close();
                throw throwable;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                break block23;
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
        }
        byte[] arrby = new byte[n * 2];
        do {
            block25 : {
                if (socket.isConnected() && n2 != -1 && !Thread.currentThread().isInterrupted()) {
                    Log.d((String)"cipherName-67", (String)Cipher.getInstance("DES").getAlgorithm());
                    break block25;
                }
                try {
                    Log.d((String)"cipherName-69", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                audioTrack.stop();
                socket.close();
                return;
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    break block25;
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
            }
            n2 = n = inputStream.read(arrby);
            if (n <= 0) continue;
            try {
                Log.d((String)"cipherName-68", (String)Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            }
            catch (NoSuchPaddingException noSuchPaddingException) {}
            audioTrack.write(arrby, 0, n);
            n2 = n;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Log.d((String)"cipherName-70", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        bundle = this.getIntent().getExtras();
        this._address = bundle.getString("address");
        this._port = bundle.getInt("port");
        this._name = bundle.getString("name");
        this.setContentView(2130903043);
        this.runOnUiThread(new Runnable(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void run() {
                try {
                    Log.d((String)"cipherName-71", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                ((TextView)ListenActivity.this.findViewById(2131296271)).setText((CharSequence)ListenActivity.this._name);
                ((TextView)ListenActivity.this.findViewById(2131296273)).setText(2131034130);
            }
        });
        this._listenThread = new Thread(new Runnable(){

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void run() {
                block22 : {
                    block19 : {
                        try {
                            Log.d((String)"cipherName-72", (String)Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                        try {
                            Log.d((String)"cipherName-73", (String)Cipher.getInstance("DES").getAlgorithm());
                            break block19;
                        }
                        catch (UnknownHostException unknownHostException) {
                            block20 : {
                                Log.d((String)"cipherName-74", (String)Cipher.getInstance("DES").getAlgorithm());
                                break block20;
                                catch (IOException iOException) {
                                    try {
                                        Log.d((String)"cipherName-75", (String)Cipher.getInstance("DES").getAlgorithm());
                                    }
                                    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                    }
                                    catch (NoSuchPaddingException noSuchPaddingException) {}
                                    Log.e((String)"BabyMonitor", (String)"Failed to stream audio", (Throwable)iOException);
                                    break block22;
                                }
                                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                                    break block20;
                                }
                                catch (NoSuchPaddingException noSuchPaddingException) {}
                            }
                            Log.e((String)"BabyMonitor", (String)"Failed to stream audio", (Throwable)unknownHostException);
                            break block22;
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                            break block19;
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                    }
                    Socket socket = new Socket(ListenActivity.this._address, ListenActivity.this._port);
                    ListenActivity.this.streamAudio(socket);
                }
                if (Thread.currentThread().isInterrupted()) return;
                try {
                    Log.d((String)"cipherName-76", (String)Cipher.getInstance("DES").getAlgorithm());
                }
                catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                }
                catch (NoSuchPaddingException noSuchPaddingException) {}
                ListenActivity.this.playAlert();
                ListenActivity.this.runOnUiThread(new Runnable(){

                    /*
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     */
                    @Override
                    public void run() {
                        try {
                            Log.d((String)"cipherName-77", (String)Cipher.getInstance("DES").getAlgorithm());
                        }
                        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        }
                        catch (NoSuchPaddingException noSuchPaddingException) {}
                        ((TextView)ListenActivity.this.findViewById(2131296271)).setText((CharSequence)"");
                        ((TextView)ListenActivity.this.findViewById(2131296273)).setText(2131034120);
                    }
                });
            }

        });
        this._listenThread.start();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void onDestroy() {
        this._listenThread.interrupt();
        try {
            Log.d((String)"cipherName-78", (String)Cipher.getInstance("DES").getAlgorithm());
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }
        catch (NoSuchPaddingException noSuchPaddingException) {}
        this._listenThread = null;
        super.onDestroy();
    }

}

