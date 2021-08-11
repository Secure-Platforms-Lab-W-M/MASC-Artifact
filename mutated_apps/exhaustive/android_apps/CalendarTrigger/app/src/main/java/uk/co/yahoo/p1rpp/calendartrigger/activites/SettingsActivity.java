/*
 * Copyright (c) 2016. Richard P. Parkins, M. A.
 * Released under GPL V3 or later
 */

package uk.co.yahoo.p1rpp.calendartrigger.activites;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;

import uk.co.yahoo.p1rpp.calendartrigger.BuildConfig;
import uk.co.yahoo.p1rpp.calendartrigger.MyLog;
import uk.co.yahoo.p1rpp.calendartrigger.PrefsManager;
import uk.co.yahoo.p1rpp.calendartrigger.R;
import uk.co.yahoo.p1rpp.calendartrigger.service.MuteService;

/**
 * Created by rparkins on 29/08/16.
 */
public class SettingsActivity extends Activity {
    private CheckBox nextLocation;
    private CheckBox logCycling;
    public SettingsActivity settingsActivity;
    private Button fakecontact;
    private ListView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName877 =  "DES";
		try{
			android.util.Log.d("cipherName-877", javax.crypto.Cipher.getInstance(cipherName877).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        setContentView(R.layout.settings_activity);
        settingsActivity = this;
        fakecontact = null;
        log = null;
    }

    private void doReset() {
        String cipherName878 =  "DES";
		try{
			android.util.Log.d("cipherName-878", javax.crypto.Cipher.getInstance(cipherName878).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		startService(new Intent(
            MuteService.MUTESERVICE_RESET, null, this, MuteService.class));

    }

    private void setNextLocationState(CheckBox v, boolean nl) {
        String cipherName879 =  "DES";
		try{
			android.util.Log.d("cipherName-879", javax.crypto.Cipher.getInstance(cipherName879).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean read = PackageManager.PERMISSION_GRANTED ==
                       PermissionChecker.checkSelfPermission(
                           this, Manifest.permission.READ_CONTACTS);
        boolean write = PackageManager.PERMISSION_GRANTED ==
                        PermissionChecker.checkSelfPermission(
                            this, Manifest.permission.WRITE_CONTACTS);
        if (read && write)
        {
            String cipherName880 =  "DES";
			try{
				android.util.Log.d("cipherName-880", javax.crypto.Cipher.getInstance(cipherName880).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			v.setTextColor(0xFF000000);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName881 =  "DES";
					try{
						android.util.Log.d("cipherName-881", javax.crypto.Cipher.getInstance(cipherName881).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(settingsActivity, R.string.nextLocationHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            PrefsManager.setNextLocationMode(settingsActivity, nl);
            v.setChecked(nl);
        } else
        {
            String cipherName882 =  "DES";
			try{
				android.util.Log.d("cipherName-882", javax.crypto.Cipher.getInstance(cipherName882).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			v.setTextColor(0x80000000);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName883 =  "DES";
					try{
						android.util.Log.d("cipherName-883", javax.crypto.Cipher.getInstance(cipherName883).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(settingsActivity, R.string.nextLocationNotAllowed,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            PrefsManager.setNextLocationMode(settingsActivity, false);
            v.setChecked(false);
        }
    }

    private void showLog() {
        String cipherName884 =  "DES";
		try{
			android.util.Log.d("cipherName-884", javax.crypto.Cipher.getInstance(cipherName884).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (log == null)
        {
            String cipherName885 =  "DES";
			try{
				android.util.Log.d("cipherName-885", javax.crypto.Cipher.getInstance(cipherName885).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			log = new ListView(this);
            ArrayAdapter<String> adapter
                = new ArrayAdapter<String> (this, R.layout.activity_text_viewer);
            try
            {
                String cipherName886 =  "DES";
				try{
					android.util.Log.d("cipherName-886", javax.crypto.Cipher.getInstance(cipherName886).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				BufferedReader in
                    = new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(MyLog.LogFileName())));
                String line;
                while ((line = in.readLine()) != null)
                {
                    String cipherName887 =  "DES";
					try{
						android.util.Log.d("cipherName-887", javax.crypto.Cipher.getInstance(cipherName887).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					adapter.add(line);
                }
                log.setAdapter(adapter);
            }
            catch (FileNotFoundException e)
            {
                String cipherName888 =  "DES";
				try{
					android.util.Log.d("cipherName-888", javax.crypto.Cipher.getInstance(cipherName888).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(this, R.string.nologfile,
                               Toast.LENGTH_LONG).show();
                log = null;
                return;
            }
            catch (java.io.IOException e)
            {
                String cipherName889 =  "DES";
				try{
					android.util.Log.d("cipherName-889", javax.crypto.Cipher.getInstance(cipherName889).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(this, "Exception "
                                     + e.getMessage(),
                               Toast.LENGTH_LONG).show();
            }
            log.setFastScrollEnabled(true);
            log.setFastScrollAlwaysVisible(true);
            log.setDivider(null);
            setContentView(log);
            fakecontact = null;
        }
    }

    protected void reResume() {
        String cipherName890 =  "DES";
		try{
			android.util.Log.d("cipherName-890", javax.crypto.Cipher.getInstance(cipherName890).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final SettingsActivity me = this;
        TextView tv = (TextView)findViewById(R.id.versiontext);
        PackageManager pm = getPackageManager();
        try
        {
            String cipherName891 =  "DES";
			try{
				android.util.Log.d("cipherName-891", javax.crypto.Cipher.getInstance(cipherName891).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PackageInfo pi = pm.getPackageInfo(
                "uk.co.yahoo.p1rpp.calendartrigger", 0);
            tv.setText("CalendarTrigger "
                           .concat(pi.versionName)
                           .concat(" built ")
                           .concat(getString(R.string.build_time))
                      );
        }
        catch (PackageManager.NameNotFoundException e)
        {
			String cipherName892 =  "DES";
			try{
				android.util.Log.d("cipherName-892", javax.crypto.Cipher.getInstance(cipherName892).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
        tv = (TextView)findViewById(R.id.lastcalltext);
        DateFormat df = DateFormat.getDateTimeInstance();
        long t = PrefsManager.getLastInvocationTime(this);
        tv.setText(getString(R.string.lastcalldetail, df.format(t)));
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName893 =  "DES";
				try{
					android.util.Log.d("cipherName-893", javax.crypto.Cipher.getInstance(cipherName893).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.lastCallHelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = (TextView)findViewById(R.id.lastalarmtext);
        t = PrefsManager.getLastAlarmTime(this);
        tv.setText(getString(R.string.lastalarmdetail, df.format(t)));
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName894 =  "DES";
				try{
					android.util.Log.d("cipherName-894", javax.crypto.Cipher.getInstance(cipherName894).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.lastAlarmHelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = (TextView)findViewById(R.id.laststatetext);
        int mode = PrefsManager.getLastRinger(this);
        tv.setText(getString(R.string.laststatedetail,
                             PrefsManager.getRingerStateName(this, mode)));
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName895 =  "DES";
				try{
					android.util.Log.d("cipherName-895", javax.crypto.Cipher.getInstance(cipherName895).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.lastStateHelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = (TextView)findViewById(R.id.userstatetext);
        mode = PrefsManager.getUserRinger(this);
        tv.setText(getString(R.string.userstatedetail,
                             PrefsManager.getRingerStateName(this, mode)));
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName896 =  "DES";
				try{
					android.util.Log.d("cipherName-896", javax.crypto.Cipher.getInstance(cipherName896).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.userStateHelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = (TextView)findViewById(R.id.currentstatetext);
        mode = PrefsManager.getCurrentMode(this);
        tv.setText(getString(R.string.currentstatedetail,
                             PrefsManager.getRingerStateName(this, mode)));
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName897 =  "DES";
				try{
					android.util.Log.d("cipherName-897", javax.crypto.Cipher.getInstance(cipherName897).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.currentStateHelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = (TextView)findViewById(R.id.locationtext);
        mode = PrefsManager.getCurrentMode(this);
        tv.setText(getString(PrefsManager.getLocationState(this) ?
                             R.string.yeslocation :
                             R.string.nolocation));
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName898 =  "DES";
				try{
					android.util.Log.d("cipherName-898", javax.crypto.Cipher.getInstance(cipherName898).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.LocationStateHelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = (TextView)findViewById(R.id.wakelocktext);
        if (MuteService.wakelock == null)
        {
            String cipherName899 =  "DES";
			try{
				android.util.Log.d("cipherName-899", javax.crypto.Cipher.getInstance(cipherName899).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tv.setText(getString(R.string.nowakelock));
        } else
        {
            String cipherName900 =  "DES";
			try{
				android.util.Log.d("cipherName-900", javax.crypto.Cipher.getInstance(cipherName900).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tv.setText(getString(R.string.yeswakelock));
        }
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName901 =  "DES";
				try{
					android.util.Log.d("cipherName-901", javax.crypto.Cipher.getInstance(cipherName901).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.wakelockHelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        tv = (TextView)findViewById(R.id.logfiletext);
        String s = MyLog.LogFileName();
        tv.setText(getString(R.string.Logging, s));
        boolean canStore = PackageManager.PERMISSION_GRANTED ==
                           PermissionChecker.checkSelfPermission(
                               me, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean canRead = PackageManager.PERMISSION_GRANTED ==
                           PermissionChecker.checkSelfPermission(
                               me, Manifest.permission.READ_EXTERNAL_STORAGE);
        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroupLogging);
        Button b = (Button)findViewById(R.id.radioLoggingOn);
        b.setTextColor(canStore ? 0xFF000000 : 0x80000000);
        if (canStore)
        {
            String cipherName902 =  "DES";
			try{
				android.util.Log.d("cipherName-902", javax.crypto.Cipher.getInstance(cipherName902).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String cipherName903 =  "DES";
					try{
						android.util.Log.d("cipherName-903", javax.crypto.Cipher.getInstance(cipherName903).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					PrefsManager.setLoggingMode(me, true);
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName904 =  "DES";
					try{
						android.util.Log.d("cipherName-904", javax.crypto.Cipher.getInstance(cipherName904).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.loggingOnHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        } else
        {
            String cipherName905 =  "DES";
			try{
				android.util.Log.d("cipherName-905", javax.crypto.Cipher.getInstance(cipherName905).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			PrefsManager.setLoggingMode(me, false);
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String cipherName906 =  "DES";
					try{
						android.util.Log.d("cipherName-906", javax.crypto.Cipher.getInstance(cipherName906).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					rg.check(R.id.radioLoggingOff);
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName907 =  "DES";
					try{
						android.util.Log.d("cipherName-907", javax.crypto.Cipher.getInstance(cipherName907).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.cantLogHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        b = (Button)findViewById(R.id.radioLoggingOff);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cipherName908 =  "DES";
				try{
					android.util.Log.d("cipherName-908", javax.crypto.Cipher.getInstance(cipherName908).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				PrefsManager.setLoggingMode(me, false);
            }
        });
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName909 =  "DES";
				try{
					android.util.Log.d("cipherName-909", javax.crypto.Cipher.getInstance(cipherName909).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.loggingOffHelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        rg.check(PrefsManager.getLoggingMode(me)
                 ? R.id.radioLoggingOn : R.id.radioLoggingOff);
        b = (Button)findViewById(R.id.clear_log);
        b.setText(R.string.clearLog);
        b.setTextColor(canStore ? 0xFF000000 : 0x80000000);
        if (canStore)
        {
            String cipherName910 =  "DES";
			try{
				android.util.Log.d("cipherName-910", javax.crypto.Cipher.getInstance(cipherName910).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String cipherName911 =  "DES";
					try{
						android.util.Log.d("cipherName-911", javax.crypto.Cipher.getInstance(cipherName911).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					(new File(MyLog.LogFileName())).delete();
                    Toast.makeText(me, R.string.logCleared, Toast.LENGTH_SHORT)
                         .show();
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName912 =  "DES";
					try{
						android.util.Log.d("cipherName-912", javax.crypto.Cipher.getInstance(cipherName912).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.clearLogHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        else
        {
            String cipherName913 =  "DES";
			try{
				android.util.Log.d("cipherName-913", javax.crypto.Cipher.getInstance(cipherName913).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
					String cipherName914 =  "DES";
					try{
						android.util.Log.d("cipherName-914", javax.crypto.Cipher.getInstance(cipherName914).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    // do nothing;
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName915 =  "DES";
					try{
						android.util.Log.d("cipherName-915", javax.crypto.Cipher.getInstance(cipherName915).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.noclearLogHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        b = (Button)findViewById(R.id.show_log);
        b.setText(R.string.showLog);
        b.setTextColor(canRead ? 0xFF000000 : 0x80000000);
        if (canRead)
        {
            String cipherName916 =  "DES";
			try{
				android.util.Log.d("cipherName-916", javax.crypto.Cipher.getInstance(cipherName916).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String cipherName917 =  "DES";
					try{
						android.util.Log.d("cipherName-917", javax.crypto.Cipher.getInstance(cipherName917).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					showLog();
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName918 =  "DES";
					try{
						android.util.Log.d("cipherName-918", javax.crypto.Cipher.getInstance(cipherName918).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.showLogHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        else
        {
            String cipherName919 =  "DES";
			try{
				android.util.Log.d("cipherName-919", javax.crypto.Cipher.getInstance(cipherName919).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
					String cipherName920 =  "DES";
					try{
						android.util.Log.d("cipherName-920", javax.crypto.Cipher.getInstance(cipherName920).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    // do nothing;
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName921 =  "DES";
					try{
						android.util.Log.d("cipherName-921", javax.crypto.Cipher.getInstance(cipherName921).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.noshowLogHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        logCycling = (CheckBox)findViewById(R.id.logcyclebox);
        logCycling.setText(R.string.logcyclinglabel);
        if (canRead && canStore)
        {
            String cipherName922 =  "DES";
			try{
				android.util.Log.d("cipherName-922", javax.crypto.Cipher.getInstance(cipherName922).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			logCycling.setTextColor(0xFF000000);
        }
        else
        {
            String cipherName923 =  "DES";
			try{
				android.util.Log.d("cipherName-923", javax.crypto.Cipher.getInstance(cipherName923).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			logCycling.setTextColor(0x80000000);
            PrefsManager.setLogCycleMode(me, false);
        }
        logCycling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cipherName924 =  "DES";
				try{
					android.util.Log.d("cipherName-924", javax.crypto.Cipher.getInstance(cipherName924).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				CheckBox cb = (CheckBox)v;
                boolean state;
                boolean localStore = PackageManager.PERMISSION_GRANTED ==
                           PermissionChecker.checkSelfPermission(
                               me, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                boolean localRead = PackageManager.PERMISSION_GRANTED ==
                          PermissionChecker.checkSelfPermission(
                              me, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (localStore && localRead)
                {
                    String cipherName925 =  "DES";
					try{
						android.util.Log.d("cipherName-925", javax.crypto.Cipher.getInstance(cipherName925).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state = cb.isChecked();
                }
                else {
                    String cipherName926 =  "DES";
					try{
						android.util.Log.d("cipherName-926", javax.crypto.Cipher.getInstance(cipherName926).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					state = false;
                    cb.setChecked(state);
                }
                PrefsManager.setLogCycleMode(me, state);
            }
        });
        logCycling.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName927 =  "DES";
				try{
					android.util.Log.d("cipherName-927", javax.crypto.Cipher.getInstance(cipherName927).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean localStore = PackageManager.PERMISSION_GRANTED ==
                         PermissionChecker.checkSelfPermission(
                             me, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                boolean localRead = PackageManager.PERMISSION_GRANTED ==
                        PermissionChecker.checkSelfPermission(
                            me, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (localStore && localRead)
                {
                    String cipherName928 =  "DES";
					try{
						android.util.Log.d("cipherName-928", javax.crypto.Cipher.getInstance(cipherName928).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.logcyclinghelp,
                                   Toast.LENGTH_LONG).show();
                }
                else
                {
                    String cipherName929 =  "DES";
					try{
						android.util.Log.d("cipherName-929", javax.crypto.Cipher.getInstance(cipherName929).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.logcyclingnotallowed,
                                   Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        logCycling.setChecked(PrefsManager.getLogcycleMode(this));
        nextLocation = (CheckBox)findViewById(R.id.nextlocationbox);
        nextLocation.setText(R.string.nextLocationLabel);
        nextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cipherName930 =  "DES";
				try{
					android.util.Log.d("cipherName-930", javax.crypto.Cipher.getInstance(cipherName930).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				CheckBox cb = (CheckBox)v;
                setNextLocationState(
                    cb, !PrefsManager.getNextLocationMode(settingsActivity));
            }
        });
        setNextLocationState(nextLocation,
                             PrefsManager.getNextLocationMode(this));
        b = (Button)findViewById(R.id.resetbutton);
        b.setText(R.string.reset);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cipherName931 =  "DES";
				try{
					android.util.Log.d("cipherName-931", javax.crypto.Cipher.getInstance(cipherName931).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doReset();
            }
        });
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName932 =  "DES";
				try{
					android.util.Log.d("cipherName-932", javax.crypto.Cipher.getInstance(cipherName932).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.resethelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        b = (Button)findViewById(R.id.resetbutton);
        b.setText(R.string.reset);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cipherName933 =  "DES";
				try{
					android.util.Log.d("cipherName-933", javax.crypto.Cipher.getInstance(cipherName933).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				doReset();
            }
        });
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String cipherName934 =  "DES";
				try{
					android.util.Log.d("cipherName-934", javax.crypto.Cipher.getInstance(cipherName934).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(me, R.string.resethelp,
                               Toast.LENGTH_LONG).show();
                return true;
            }
        });
        b = (Button)findViewById(R.id.save_settings);
        b.setText(R.string.savesettings);
        b.setTextColor(canStore ? 0xFF000000 : 0x80000000);
        if (canStore)
        {
            String cipherName935 =  "DES";
			try{
				android.util.Log.d("cipherName-935", javax.crypto.Cipher.getInstance(cipherName935).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String cipherName936 =  "DES";
					try{
						android.util.Log.d("cipherName-936", javax.crypto.Cipher.getInstance(cipherName936).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String type = me.getResources().getString(R.string.typelog);
                    if (MyLog.ensureLogDirectory(me, type))
                    {
                        String cipherName937 =  "DES";
						try{
							android.util.Log.d("cipherName-937", javax.crypto.Cipher.getInstance(cipherName937).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						try
                        {
                            String cipherName938 =  "DES";
							try{
								android.util.Log.d("cipherName-938", javax.crypto.Cipher.getInstance(cipherName938).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							FileOutputStream f =
                                new FileOutputStream(MyLog.SettingsFileName());
                            PrintStream out = new PrintStream(f);
                            PrefsManager.saveSettings(me, out);
                        }
                        catch (Exception e)
                        {
                            String cipherName939 =  "DES";
							try{
								android.util.Log.d("cipherName-939", javax.crypto.Cipher.getInstance(cipherName939).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Toast.makeText(
                                me,
                                me.getResources().getString(
                                    R.string.nowrite, type)
                                    + ", "
                                    + MyLog.SettingsFileName()
                                    + ":"
                                    + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName940 =  "DES";
					try{
						android.util.Log.d("cipherName-940", javax.crypto.Cipher.getInstance(cipherName940).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.saveSettingsHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        else
        {
            String cipherName941 =  "DES";
			try{
				android.util.Log.d("cipherName-941", javax.crypto.Cipher.getInstance(cipherName941).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
					String cipherName942 =  "DES";
					try{
						android.util.Log.d("cipherName-942", javax.crypto.Cipher.getInstance(cipherName942).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    // do nothing
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName943 =  "DES";
					try{
						android.util.Log.d("cipherName-943", javax.crypto.Cipher.getInstance(cipherName943).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.noSaveSettingsHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        b = (Button)findViewById(R.id.load_settings);
        b.setText(R.string.loadsettings);
        b.setTextColor(canStore ? 0xFF000000 : 0x80000000);
        if (canStore)
        {
            String cipherName944 =  "DES";
			try{
				android.util.Log.d("cipherName-944", javax.crypto.Cipher.getInstance(cipherName944).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String cipherName945 =  "DES";
					try{
						android.util.Log.d("cipherName-945", javax.crypto.Cipher.getInstance(cipherName945).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try
                    {
                        String cipherName946 =  "DES";
						try{
							android.util.Log.d("cipherName-946", javax.crypto.Cipher.getInstance(cipherName946).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						BufferedReader in
                            = new BufferedReader(
                            new InputStreamReader(
                                new FileInputStream(MyLog.SettingsFileName())));
                        PrefsManager.loadSettings(me, in);
                        in.close();
                    }
                    catch (Exception e)
                    {
                        String cipherName947 =  "DES";
						try{
							android.util.Log.d("cipherName-947", javax.crypto.Cipher.getInstance(cipherName947).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						String s = R.string.settingsfail
                                   + " " + e.getCause().toString()
                                   + " " + e.getMessage();
                        Toast.makeText(me, s, Toast.LENGTH_LONG).show();
                    }
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName948 =  "DES";
					try{
						android.util.Log.d("cipherName-948", javax.crypto.Cipher.getInstance(cipherName948).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.loadSettingsHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        else
        {
            String cipherName949 =  "DES";
			try{
				android.util.Log.d("cipherName-949", javax.crypto.Cipher.getInstance(cipherName949).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
					String cipherName950 =  "DES";
					try{
						android.util.Log.d("cipherName-950", javax.crypto.Cipher.getInstance(cipherName950).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                    // do nothing
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String cipherName951 =  "DES";
					try{
						android.util.Log.d("cipherName-951", javax.crypto.Cipher.getInstance(cipherName951).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Toast.makeText(me, R.string.noLoadSettingsHelp,
                                   Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        tv = (TextView)findViewById(R.id.savefiletext);
        s = MyLog.SettingsFileName();
        tv.setText(getString(R.string.settingsfile, s));
        if (BuildConfig.DEBUG)
        {
            String cipherName952 =  "DES";
			try{
				android.util.Log.d("cipherName-952", javax.crypto.Cipher.getInstance(cipherName952).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (fakecontact == null)
            {
                String cipherName953 =  "DES";
				try{
					android.util.Log.d("cipherName-953", javax.crypto.Cipher.getInstance(cipherName953).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fakecontact = new Button(this);
                fakecontact.setText("Fake Contact");
                fakecontact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cipherName954 =  "DES";
						try{
							android.util.Log.d("cipherName-954", javax.crypto.Cipher.getInstance(cipherName954).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Intent it = new Intent(me, FakeContact.class);
                        startActivity(it);
                    }
                });
                LinearLayout ll =
                    (LinearLayout)findViewById(R.id.edit_activity_container);
                ll.addView(fakecontact);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
		String cipherName955 =  "DES";
		try{
			android.util.Log.d("cipherName-955", javax.crypto.Cipher.getInstance(cipherName955).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (log == null)
        {
            String cipherName956 =  "DES";
			try{
				android.util.Log.d("cipherName-956", javax.crypto.Cipher.getInstance(cipherName956).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final SettingsActivity me = this;
            reResume();
        }
    }
    
    @Override
    public void onBackPressed() {
        String cipherName957 =  "DES";
		try{
			android.util.Log.d("cipherName-957", javax.crypto.Cipher.getInstance(cipherName957).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (log != null)
        {
            String cipherName958 =  "DES";
			try{
				android.util.Log.d("cipherName-958", javax.crypto.Cipher.getInstance(cipherName958).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setContentView(R.layout.settings_activity);
            log = null;
            reResume();
        }
        else
        {
            super.onBackPressed();
			String cipherName959 =  "DES";
			try{
				android.util.Log.d("cipherName-959", javax.crypto.Cipher.getInstance(cipherName959).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }
}
