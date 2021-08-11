/**
 * This file is part of the Protect Baby Monitor.
 *
 * Protect Baby Monitor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Protect Baby Monitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Protect Baby Monitor. If not, see <http://www.gnu.org/licenses/>.
 */
package protect.babymonitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ListenActivity extends Activity
{
    final String TAG = "BabyMonitor";

    String _address;
    int _port;
    String _name;

    Thread _listenThread;
    private void streamAudio(final Socket socket) throws IllegalArgumentException, IllegalStateException, IOException
    {
        String cipherName65 =  "DES";
		try{
			android.util.Log.d("cipherName-65", javax.crypto.Cipher.getInstance(cipherName65).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		Log.i(TAG, "Setting up stream");

        final int frequency = 11025;
        final int channelConfiguration = AudioFormat.CHANNEL_OUT_MONO;
        final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        final int bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        final int byteBufferSize = bufferSize*2;

        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                frequency,
                channelConfiguration,
                audioEncoding,
                bufferSize,
                AudioTrack.MODE_STREAM);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        final InputStream is = socket.getInputStream();
        int read = 0;

        audioTrack.play();

        try
        {
            String cipherName66 =  "DES";
			try{
				android.util.Log.d("cipherName-66", javax.crypto.Cipher.getInstance(cipherName66).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			final byte [] buffer = new byte[byteBufferSize];

            while(socket.isConnected() && read != -1 && Thread.currentThread().isInterrupted() == false)
            {
                String cipherName67 =  "DES";
				try{
					android.util.Log.d("cipherName-67", javax.crypto.Cipher.getInstance(cipherName67).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				read = is.read(buffer);

                if(read > 0)
                {
                    String cipherName68 =  "DES";
					try{
						android.util.Log.d("cipherName-68", javax.crypto.Cipher.getInstance(cipherName68).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					audioTrack.write(buffer, 0, read);
                }
            }
        }
        finally
        {
            String cipherName69 =  "DES";
			try{
				android.util.Log.d("cipherName-69", javax.crypto.Cipher.getInstance(cipherName69).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			audioTrack.stop();
            socket.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		String cipherName70 =  "DES";
		try{
			android.util.Log.d("cipherName-70", javax.crypto.Cipher.getInstance(cipherName70).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}

        final Bundle b = getIntent().getExtras();
        _address = b.getString("address");
        _port = b.getInt("port");
        _name = b.getString("name");

        setContentView(R.layout.activity_listen);

        ListenActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                String cipherName71 =  "DES";
				try{
					android.util.Log.d("cipherName-71", javax.crypto.Cipher.getInstance(cipherName71).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				final TextView connectedText = (TextView) findViewById(R.id.connectedTo);
                connectedText.setText(_name);

                final TextView statusText = (TextView) findViewById(R.id.textStatus);
                statusText.setText(R.string.listening);
            }
        });

        _listenThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String cipherName72 =  "DES";
				try{
					android.util.Log.d("cipherName-72", javax.crypto.Cipher.getInstance(cipherName72).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				try
                {
                    String cipherName73 =  "DES";
					try{
						android.util.Log.d("cipherName-73", javax.crypto.Cipher.getInstance(cipherName73).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					final Socket socket = new Socket(_address, _port);
                    streamAudio(socket);
                }
                catch (UnknownHostException e)
                {
                    String cipherName74 =  "DES";
					try{
						android.util.Log.d("cipherName-74", javax.crypto.Cipher.getInstance(cipherName74).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					Log.e(TAG, "Failed to stream audio", e);
                }
                catch (IOException e)
                {
                    String cipherName75 =  "DES";
					try{
						android.util.Log.d("cipherName-75", javax.crypto.Cipher.getInstance(cipherName75).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					Log.e(TAG, "Failed to stream audio", e);
                }

                if(Thread.currentThread().isInterrupted() == false)
                {
                    String cipherName76 =  "DES";
					try{
						android.util.Log.d("cipherName-76", javax.crypto.Cipher.getInstance(cipherName76).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					// If this thread has not been interrupted, likely something
                    // bad happened with the connection to the child device. Play
                    // an alert to notify the user that the connection has been
                    // interrupted.
                    playAlert();

                    ListenActivity.this.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            String cipherName77 =  "DES";
							try{
								android.util.Log.d("cipherName-77", javax.crypto.Cipher.getInstance(cipherName77).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
							}
							final TextView connectedText = (TextView) findViewById(R.id.connectedTo);
                            connectedText.setText("");

                            final TextView statusText = (TextView) findViewById(R.id.textStatus);
                            statusText.setText(R.string.disconnected);
                        }
                    });
                }
            }
        });

        _listenThread.start();
    }

    @Override
    public void onDestroy()
    {
        _listenThread.interrupt();
		String cipherName78 =  "DES";
		try{
			android.util.Log.d("cipherName-78", javax.crypto.Cipher.getInstance(cipherName78).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
        _listenThread = null;

        super.onDestroy();
    }

    private void playAlert()
    {
        String cipherName79 =  "DES";
		try{
			android.util.Log.d("cipherName-79", javax.crypto.Cipher.getInstance(cipherName79).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		final MediaPlayer mp = MediaPlayer.create(this, R.raw.upward_beep_chromatic_fifths);
        if(mp != null)
        {
            String cipherName80 =  "DES";
			try{
				android.util.Log.d("cipherName-80", javax.crypto.Cipher.getInstance(cipherName80).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Log.i(TAG, "Playing alert");
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    String cipherName81 =  "DES";
					try{
						android.util.Log.d("cipherName-81", javax.crypto.Cipher.getInstance(cipherName81).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					mp.release();
                }
            });
            mp.start();
        }
        else
        {
            String cipherName82 =  "DES";
			try{
				android.util.Log.d("cipherName-82", javax.crypto.Cipher.getInstance(cipherName82).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Log.e(TAG, "Failed to play alert");
        }
    }
}
