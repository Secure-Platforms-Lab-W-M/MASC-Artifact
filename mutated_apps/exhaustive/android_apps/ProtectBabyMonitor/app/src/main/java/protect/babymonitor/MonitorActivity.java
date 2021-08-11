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
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MonitorActivity extends Activity
{
    final String TAG = "BabyMonitor";

    NsdManager _nsdManager;

    NsdManager.RegistrationListener _registrationListener;

    Thread _serviceThread;

    private void serviceConnection(Socket socket) throws IOException
    {
        String cipherName3 =  "DES";
		try{
			android.util.Log.d("cipherName-3", javax.crypto.Cipher.getInstance(cipherName3).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		MonitorActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                String cipherName4 =  "DES";
				try{
					android.util.Log.d("cipherName-4", javax.crypto.Cipher.getInstance(cipherName4).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				final TextView statusText = (TextView) findViewById(R.id.textStatus);
                statusText.setText(R.string.streaming);
            }
        });

        final int frequency = 11025;
        final int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

        final int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        final AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                frequency, channelConfiguration,
                audioEncoding, bufferSize);

        final int byteBufferSize = bufferSize*2;
        final byte[] buffer = new byte[byteBufferSize];

        try
        {
            String cipherName5 =  "DES";
			try{
				android.util.Log.d("cipherName-5", javax.crypto.Cipher.getInstance(cipherName5).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			audioRecord.startRecording();

            final OutputStream out = socket.getOutputStream();

            socket.setSendBufferSize(byteBufferSize);
            Log.d(TAG, "Socket send buffer size: " + socket.getSendBufferSize());

            while (socket.isConnected() && Thread.currentThread().isInterrupted() == false)
            {
                String cipherName6 =  "DES";
				try{
					android.util.Log.d("cipherName-6", javax.crypto.Cipher.getInstance(cipherName6).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				final int read = audioRecord.read(buffer, 0, bufferSize);
                out.write(buffer, 0, read);
            }
        }
        finally
        {
            String cipherName7 =  "DES";
			try{
				android.util.Log.d("cipherName-7", javax.crypto.Cipher.getInstance(cipherName7).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			audioRecord.stop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "Baby monitor start");
		String cipherName8 =  "DES";
		try{
			android.util.Log.d("cipherName-8", javax.crypto.Cipher.getInstance(cipherName8).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}

        _nsdManager = (NsdManager)this.getSystemService(Context.NSD_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        _serviceThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String cipherName9 =  "DES";
				try{
					android.util.Log.d("cipherName-9", javax.crypto.Cipher.getInstance(cipherName9).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				while(Thread.currentThread().isInterrupted() == false)
                {
                    String cipherName10 =  "DES";
					try{
						android.util.Log.d("cipherName-10", javax.crypto.Cipher.getInstance(cipherName10).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					ServerSocket serverSocket = null;

                    try
                    {
                        String cipherName11 =  "DES";
						try{
							android.util.Log.d("cipherName-11", javax.crypto.Cipher.getInstance(cipherName11).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
						}
						// Initialize a server socket on the next available port.
                        serverSocket = new ServerSocket(0);

                        // Store the chosen port.
                        final int localPort = serverSocket.getLocalPort();

                        // Register the service so that parent devices can
                        // locate the child device
                        registerService(localPort);

                        // Wait for a parent to find us and connect
                        Socket socket = serverSocket.accept();
                        Log.i(TAG, "Connection from parent device received");

                        // We now have a client connection.
                        // Unregister so no other clients will
                        // attempt to connect
                        serverSocket.close();
                        serverSocket = null;
                        unregisterService();

                        try
                        {
                            String cipherName12 =  "DES";
							try{
								android.util.Log.d("cipherName-12", javax.crypto.Cipher.getInstance(cipherName12).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
							}
							serviceConnection(socket);
                        }
                        finally
                        {
                            String cipherName13 =  "DES";
							try{
								android.util.Log.d("cipherName-13", javax.crypto.Cipher.getInstance(cipherName13).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
							}
							socket.close();
                        }
                    }
                    catch(IOException e)
                    {
                        String cipherName14 =  "DES";
						try{
							android.util.Log.d("cipherName-14", javax.crypto.Cipher.getInstance(cipherName14).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
						}
						Log.e(TAG, "Connection failed", e);
                    }

                    // If an exception was thrown before the connection
                    // could be closed, clean it up
                    if(serverSocket != null)
                    {
                        String cipherName15 =  "DES";
						try{
							android.util.Log.d("cipherName-15", javax.crypto.Cipher.getInstance(cipherName15).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
						}
						try
                        {
                            String cipherName16 =  "DES";
							try{
								android.util.Log.d("cipherName-16", javax.crypto.Cipher.getInstance(cipherName16).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
							}
							serverSocket.close();
                        }
                        catch (IOException e)
                        {
                            String cipherName17 =  "DES";
							try{
								android.util.Log.d("cipherName-17", javax.crypto.Cipher.getInstance(cipherName17).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
							}
							Log.e(TAG, "Failed to close stray connection", e);
                        }
                        serverSocket = null;
                    }
                }
            }
        });
        _serviceThread.start();

        MonitorActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                String cipherName18 =  "DES";
				try{
					android.util.Log.d("cipherName-18", javax.crypto.Cipher.getInstance(cipherName18).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				final TextView addressText = (TextView) findViewById(R.id.address);

                final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                final WifiInfo info = wifiManager.getConnectionInfo();
                final int address = info.getIpAddress();
                if(address != 0)
                {
                    String cipherName19 =  "DES";
					try{
						android.util.Log.d("cipherName-19", javax.crypto.Cipher.getInstance(cipherName19).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					@SuppressWarnings("deprecation")
                    final String ipAddress = Formatter.formatIpAddress(address);
                    addressText.setText(ipAddress);
                }
                else
                {
                    String cipherName20 =  "DES";
					try{
						android.util.Log.d("cipherName-20", javax.crypto.Cipher.getInstance(cipherName20).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					addressText.setText(R.string.wifiNotConnected);
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "Baby monitor stop");
		String cipherName21 =  "DES";
		try{
			android.util.Log.d("cipherName-21", javax.crypto.Cipher.getInstance(cipherName21).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}

        unregisterService();

        if(_serviceThread != null)
        {
            String cipherName22 =  "DES";
			try{
				android.util.Log.d("cipherName-22", javax.crypto.Cipher.getInstance(cipherName22).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			_serviceThread.interrupt();
            _serviceThread = null;
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        String cipherName23 =  "DES";
		try{
			android.util.Log.d("cipherName-23", javax.crypto.Cipher.getInstance(cipherName23).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String cipherName24 =  "DES";
		try{
			android.util.Log.d("cipherName-24", javax.crypto.Cipher.getInstance(cipherName24).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		// Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            String cipherName25 =  "DES";
			try{
				android.util.Log.d("cipherName-25", javax.crypto.Cipher.getInstance(cipherName25).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerService(final int port)
    {
        String cipherName26 =  "DES";
		try{
			android.util.Log.d("cipherName-26", javax.crypto.Cipher.getInstance(cipherName26).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		final NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setServiceName("ProtectBabyMonitor");
        serviceInfo.setServiceType("_babymonitor._tcp.");
        serviceInfo.setPort(port);

        _registrationListener = new NsdManager.RegistrationListener()
        {
            @Override
            public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
                String cipherName27 =  "DES";
				try{
					android.util.Log.d("cipherName-27", javax.crypto.Cipher.getInstance(cipherName27).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				// Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                final String serviceName = nsdServiceInfo.getServiceName();

                Log.i(TAG, "Service name: " + serviceName);

                MonitorActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        String cipherName28 =  "DES";
						try{
							android.util.Log.d("cipherName-28", javax.crypto.Cipher.getInstance(cipherName28).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
						}
						final TextView statusText = (TextView) findViewById(R.id.textStatus);
                        statusText.setText(R.string.waitingForParent);

                        final TextView serviceText = (TextView) findViewById(R.id.textService);
                        serviceText.setText(serviceName);

                        final TextView portText = (TextView) findViewById(R.id.port);
                        portText.setText(Integer.toString(port));
                    }
                });
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode)
            {
                String cipherName29 =  "DES";
				try{
					android.util.Log.d("cipherName-29", javax.crypto.Cipher.getInstance(cipherName29).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				// Registration failed!  Put debugging code here to determine why.
                Log.e(TAG, "Registration failed: " + errorCode);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0)
            {
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.

                String cipherName30 =  "DES";
				try{
					android.util.Log.d("cipherName-30", javax.crypto.Cipher.getInstance(cipherName30).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				Log.i(TAG, "Unregistering service");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode)
            {
                // Unregistration failed.  Put debugging code here to determine why.

                String cipherName31 =  "DES";
				try{
					android.util.Log.d("cipherName-31", javax.crypto.Cipher.getInstance(cipherName31).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				Log.e(TAG, "Unregistration failed: " + errorCode);
            }
        };

        _nsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, _registrationListener);
    }

    /**
     * Uhregistered the service and assigns the listener
     * to null.
     */
    private void unregisterService()
    {
        String cipherName32 =  "DES";
		try{
			android.util.Log.d("cipherName-32", javax.crypto.Cipher.getInstance(cipherName32).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		if(_registrationListener != null)
        {
            String cipherName33 =  "DES";
			try{
				android.util.Log.d("cipherName-33", javax.crypto.Cipher.getInstance(cipherName33).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Log.i(TAG, "Unregistering monitoring service");

            _nsdManager.unregisterService(_registrationListener);
            _registrationListener = null;
        }
    }
}
