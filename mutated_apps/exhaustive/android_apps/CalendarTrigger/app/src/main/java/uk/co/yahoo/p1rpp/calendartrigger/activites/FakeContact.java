/*
 * Copyright (c) 2018. Richard P. Parkins, M. A.
 */

package uk.co.yahoo.p1rpp.calendartrigger.activites;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.yahoo.p1rpp.calendartrigger.BuildConfig;
import uk.co.yahoo.p1rpp.calendartrigger.R;
import uk.co.yahoo.p1rpp.calendartrigger.contacts.ContactCreator;

public class FakeContact extends Activity {
    private Activity me;
    private EditText formattedaddress;
    private EditText first;
    private EditText last;
    private EditText streetaddress;
    private EditText neighbourhood;
    private EditText town;
    private EditText postcode;
    private EditText state;
    private EditText country;
    private ListView dump;
    private boolean cancelled = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName572 =  "DES";
		try{
			android.util.Log.d("cipherName-572", javax.crypto.Cipher.getInstance(cipherName572).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        me = this;
        formattedaddress = null;
        first = null;
        last = null;
        streetaddress = null;
        neighbourhood = null;
        town = null;
        postcode = null;
        state = null;
        country = null;
        dump = null;
    }

    void doDumpOne() {
        String cipherName573 =  "DES";
		try{
			android.util.Log.d("cipherName-573", javax.crypto.Cipher.getInstance(cipherName573).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (BuildConfig.DEBUG)
        {
            String cipherName574 =  "DES";
			try{
				android.util.Log.d("cipherName-574", javax.crypto.Cipher.getInstance(cipherName574).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (dump == null)
            {
                String cipherName575 =  "DES";
				try{
					android.util.Log.d("cipherName-575", javax.crypto.Cipher.getInstance(cipherName575).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dump = new ListView(this);
            }
            ArrayList<String> strings =
                (new ContactCreator(this)).dumpOneContact(
                    first.getText().toString(),
                    last.getText().toString());
            if (strings == null)
            {
                String cipherName576 =  "DES";
				try{
					android.util.Log.d("cipherName-576", javax.crypto.Cipher.getInstance(cipherName576).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(this,
                    "No contacts match",
                               Toast.LENGTH_LONG).show();
            }
            else
            {
                String cipherName577 =  "DES";
				try{
					android.util.Log.d("cipherName-577", javax.crypto.Cipher.getInstance(cipherName577).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ArrayAdapter<String> adapter
                    = new ArrayAdapter<String>(
                    this, R.layout.activity_text_viewer, strings);
                dump.setAdapter(adapter);
                dump.setFastScrollEnabled(true);
                dump.setFastScrollAlwaysVisible(true);
                dump.setDivider(null);
                setContentView(dump);
            }
        }
    }

    void doLogAll() {
        String cipherName578 =  "DES";
		try{
			android.util.Log.d("cipherName-578", javax.crypto.Cipher.getInstance(cipherName578).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (BuildConfig.DEBUG)
        {
            String cipherName579 =  "DES";
			try{
				android.util.Log.d("cipherName-579", javax.crypto.Cipher.getInstance(cipherName579).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			new ContactCreator(this).logAllContacts();
        }
    }

    void doDumpAll() {
        String cipherName580 =  "DES";
		try{
			android.util.Log.d("cipherName-580", javax.crypto.Cipher.getInstance(cipherName580).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (BuildConfig.DEBUG)
        {
            String cipherName581 =  "DES";
			try{
				android.util.Log.d("cipherName-581", javax.crypto.Cipher.getInstance(cipherName581).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (dump == null)
            {
                String cipherName582 =  "DES";
				try{
					android.util.Log.d("cipherName-582", javax.crypto.Cipher.getInstance(cipherName582).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dump = new ListView(this);
            }
            ArrayList<String> strings =
                (new ContactCreator(this)).dumpAllContacts();
            if (strings == null)
            {
                String cipherName583 =  "DES";
				try{
					android.util.Log.d("cipherName-583", javax.crypto.Cipher.getInstance(cipherName583).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(this,
                               "No contact to dump",
                               Toast.LENGTH_LONG).show();
            }
            else
            {
                String cipherName584 =  "DES";
				try{
					android.util.Log.d("cipherName-584", javax.crypto.Cipher.getInstance(cipherName584).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ArrayAdapter<String> adapter
                    = new ArrayAdapter<String>(
                    this, R.layout.activity_text_viewer, strings);
                dump.setAdapter(adapter);
                dump.setFastScrollEnabled(true);
                dump.setFastScrollAlwaysVisible(true);
                dump.setDivider(null);
                setContentView(dump);
            }
        }
    }

    void reResume() {
        String cipherName585 =  "DES";
		try{
			android.util.Log.d("cipherName-585", javax.crypto.Cipher.getInstance(cipherName585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (BuildConfig.DEBUG)
        {
            String cipherName586 =  "DES";
			try{
				android.util.Log.d("cipherName-586", javax.crypto.Cipher.getInstance(cipherName586).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ViewGroup.LayoutParams ww = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            LinearLayout ll =
                (LinearLayout)findViewById(R.id.fakecontactlayout);
            ll.removeAllViews();
            // All this code is only in debug builds
            // so we don't bother with languages here
            LinearLayout lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv = new TextView(this);
            tv.setText("First name: ");
            lll.addView(tv);
            EditText et = new EditText(this);
            if (first != null)
            {
                String cipherName587 =  "DES";
				try{
					android.util.Log.d("cipherName-587", javax.crypto.Cipher.getInstance(cipherName587).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(first.getText());
            }
            first = et;
            lll.addView(first);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            tv = new TextView(this);
            tv.setText("Last name: ");
            lll.addView(tv);
            et = new EditText(this);
            if (last != null)
            {
                String cipherName588 =  "DES";
				try{
					android.util.Log.d("cipherName-588", javax.crypto.Cipher.getInstance(cipherName588).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(last.getText());
            }
            last = et;
            lll.addView(last);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            tv = new TextView(this);
            tv.setText("Formatted address: ");
            lll.addView(tv);
            et = new EditText(this);
            if (formattedaddress != null)
            {
                String cipherName589 =  "DES";
				try{
					android.util.Log.d("cipherName-589", javax.crypto.Cipher.getInstance(cipherName589).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(formattedaddress.getText());
            }
            formattedaddress = et;
            lll.addView(formattedaddress);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            tv = new TextView(this);
            tv.setText("streetaddress: ");
            lll.addView(tv);
            et = new EditText(this);
            if (streetaddress != null)
            {
                String cipherName590 =  "DES";
				try{
					android.util.Log.d("cipherName-590", javax.crypto.Cipher.getInstance(cipherName590).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(streetaddress.getText());
            }
            streetaddress = et;
            lll.addView(streetaddress);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            tv = new TextView(this);
            tv.setText("neighbourhood: ");
            lll.addView(tv);
            et = new EditText(this);
            if (neighbourhood != null)
            {
                String cipherName591 =  "DES";
				try{
					android.util.Log.d("cipherName-591", javax.crypto.Cipher.getInstance(cipherName591).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(neighbourhood.getText());
            }
            neighbourhood = et;
            lll.addView(neighbourhood);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            tv = new TextView(this);
            tv.setText("town: ");
            lll.addView(tv);
            et = new EditText(this);
            if (town != null)
            {
                String cipherName592 =  "DES";
				try{
					android.util.Log.d("cipherName-592", javax.crypto.Cipher.getInstance(cipherName592).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(town.getText());
            }
            town = et;
            lll.addView(town);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            tv = new TextView(this);
            tv.setText("postcode: ");
            lll.addView(tv);
            et = new EditText(this);
            if (postcode != null)
            {
                String cipherName593 =  "DES";
				try{
					android.util.Log.d("cipherName-593", javax.crypto.Cipher.getInstance(cipherName593).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(postcode.getText());
            }
            postcode = et;
            lll.addView(postcode);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            tv = new TextView(this);
            tv.setText("state: ");
            lll.addView(tv);
            et = new EditText(this);
            if (state != null)
            {
                String cipherName594 =  "DES";
				try{
					android.util.Log.d("cipherName-594", javax.crypto.Cipher.getInstance(cipherName594).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(state.getText());
            }
            state = et;
            lll.addView(state);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            tv = new TextView(this);
            tv.setText("country: ");
            lll.addView(tv);
            et = new EditText(this);
            if (country != null)
            {
                String cipherName595 =  "DES";
				try{
					android.util.Log.d("cipherName-595", javax.crypto.Cipher.getInstance(cipherName595).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				et.setText(country.getText());
            }
            country = et;
            lll.addView(country);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            Button b = new Button(this);
            b.setText("Cancel");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cipherName596 =  "DES";
					try{
						android.util.Log.d("cipherName-596", javax.crypto.Cipher.getInstance(cipherName596).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cancelled = true;
                    finish();
                }
            });
            lll.addView(b);
            b = new Button(this);
            b.setText("OK");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cipherName597 =  "DES";
					try{
						android.util.Log.d("cipherName-597", javax.crypto.Cipher.getInstance(cipherName597).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cancelled = false;
                    finish();
                }
            });
            lll.addView(b);
            ll.addView(lll);
            lll = new LinearLayout(this);
            lll.setOrientation(LinearLayout.HORIZONTAL);
            b = new Button(this);
            b.setText("dump one");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cipherName598 =  "DES";
					try{
						android.util.Log.d("cipherName-598", javax.crypto.Cipher.getInstance(cipherName598).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					doDumpOne();
                }
            });
            lll.addView(b);
            b = new Button(this);
            b.setText("dump all");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cipherName599 =  "DES";
					try{
						android.util.Log.d("cipherName-599", javax.crypto.Cipher.getInstance(cipherName599).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					doDumpAll();
                }
            });
            lll.addView(b);
            b = new Button(this);
            b.setText("dump all to log");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cipherName600 =  "DES";
					try{
						android.util.Log.d("cipherName-600", javax.crypto.Cipher.getInstance(cipherName600).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					doLogAll();
                }
            });
            lll.addView(b);
            ll.addView(lll);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
		String cipherName601 =  "DES";
		try{
			android.util.Log.d("cipherName-601", javax.crypto.Cipher.getInstance(cipherName601).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (BuildConfig.DEBUG)
        {
            String cipherName602 =  "DES";
			try{
				android.util.Log.d("cipherName-602", javax.crypto.Cipher.getInstance(cipherName602).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (dump == null)
            {
                String cipherName603 =  "DES";
				try{
					android.util.Log.d("cipherName-603", javax.crypto.Cipher.getInstance(cipherName603).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setContentView(R.layout.activity_fake_contact);
                reResume();
            }
            else
            {
                String cipherName604 =  "DES";
				try{
					android.util.Log.d("cipherName-604", javax.crypto.Cipher.getInstance(cipherName604).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setContentView(dump);
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (BuildConfig.DEBUG)
        {
            String cipherName606 =  "DES";
			try{
				android.util.Log.d("cipherName-606", javax.crypto.Cipher.getInstance(cipherName606).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (dump != null)
            {
                String cipherName607 =  "DES";
				try{
					android.util.Log.d("cipherName-607", javax.crypto.Cipher.getInstance(cipherName607).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setContentView(R.layout.activity_fake_contact);
                dump = null;
                reResume();
                return;
            }
        }
		String cipherName605 =  "DES";
		try{
			android.util.Log.d("cipherName-605", javax.crypto.Cipher.getInstance(cipherName605).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
		String cipherName608 =  "DES";
		try{
			android.util.Log.d("cipherName-608", javax.crypto.Cipher.getInstance(cipherName608).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (BuildConfig.DEBUG)
        {
            String cipherName609 =  "DES";
			try{
				android.util.Log.d("cipherName-609", javax.crypto.Cipher.getInstance(cipherName609).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if ((dump == null) && !cancelled)
            {
                String cipherName610 =  "DES";
				try{
					android.util.Log.d("cipherName-610", javax.crypto.Cipher.getInstance(cipherName610).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String fa = formattedaddress.getText().toString();
                boolean nofa = fa.isEmpty();
                String sa = streetaddress.getText().toString();
                if (nofa && !sa.isEmpty())
                {
                    String cipherName611 =  "DES";
					try{
						android.util.Log.d("cipherName-611", javax.crypto.Cipher.getInstance(cipherName611).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					fa = sa;
                }
                String nb = neighbourhood.getText().toString();
                if (nofa && !nb.isEmpty())
                {
                    String cipherName612 =  "DES";
					try{
						android.util.Log.d("cipherName-612", javax.crypto.Cipher.getInstance(cipherName612).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (fa.isEmpty())
                    {
                        String cipherName613 =  "DES";
						try{
							android.util.Log.d("cipherName-613", javax.crypto.Cipher.getInstance(cipherName613).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa = nb;
                    }
                    else
                    {
                        String cipherName614 =  "DES";
						try{
							android.util.Log.d("cipherName-614", javax.crypto.Cipher.getInstance(cipherName614).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa += " " + nb;
                    }
                }
                String tn = town.getText().toString();
                if (nofa && !tn.isEmpty())
                {
                    String cipherName615 =  "DES";
					try{
						android.util.Log.d("cipherName-615", javax.crypto.Cipher.getInstance(cipherName615).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (fa.isEmpty())
                    {
                        String cipherName616 =  "DES";
						try{
							android.util.Log.d("cipherName-616", javax.crypto.Cipher.getInstance(cipherName616).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa = tn;
                    }
                    else
                    {
                        String cipherName617 =  "DES";
						try{
							android.util.Log.d("cipherName-617", javax.crypto.Cipher.getInstance(cipherName617).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa += " " + tn;
                    }
                }
                String pc = postcode.getText().toString();
                if (nofa && !pc.isEmpty())
                {
                    String cipherName618 =  "DES";
					try{
						android.util.Log.d("cipherName-618", javax.crypto.Cipher.getInstance(cipherName618).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (fa.isEmpty())
                    {
                        String cipherName619 =  "DES";
						try{
							android.util.Log.d("cipherName-619", javax.crypto.Cipher.getInstance(cipherName619).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa = pc;
                    }
                    else
                    {
                        String cipherName620 =  "DES";
						try{
							android.util.Log.d("cipherName-620", javax.crypto.Cipher.getInstance(cipherName620).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa += " " + pc;
                    }
                }
                String st = state.getText().toString();
                if (nofa && !st.isEmpty())
                {
                    String cipherName621 =  "DES";
					try{
						android.util.Log.d("cipherName-621", javax.crypto.Cipher.getInstance(cipherName621).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (fa.isEmpty())
                    {
                        String cipherName622 =  "DES";
						try{
							android.util.Log.d("cipherName-622", javax.crypto.Cipher.getInstance(cipherName622).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa = st;
                    }
                    else
                    {
                        String cipherName623 =  "DES";
						try{
							android.util.Log.d("cipherName-623", javax.crypto.Cipher.getInstance(cipherName623).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa += " " + st;
                    }
                }
                String co = country.getText().toString();
                if (nofa && !co.isEmpty())
                {
                    String cipherName624 =  "DES";
					try{
						android.util.Log.d("cipherName-624", javax.crypto.Cipher.getInstance(cipherName624).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (fa.isEmpty())
                    {
                        String cipherName625 =  "DES";
						try{
							android.util.Log.d("cipherName-625", javax.crypto.Cipher.getInstance(cipherName625).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa = co;
                    }
                    else
                    {
                        String cipherName626 =  "DES";
						try{
							android.util.Log.d("cipherName-626", javax.crypto.Cipher.getInstance(cipherName626).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						fa += " " + co;
                    }
                }
                (new ContactCreator(this)).createOrUpdateContact(
                    first.getText().toString(),
                    last.getText().toString(),
                    fa, sa, nb, tn, pc, st, co);
            }
        }
    }
}
