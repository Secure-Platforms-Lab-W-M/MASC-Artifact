// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.zola.bmi;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import java.text.DecimalFormat;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import android.support.v7.app.AppCompatActivity;

public class BMIMain extends AppCompatActivity
{
    private double calculateBMI(final double n, final double n2) {
        try {
            Log.d("cipherName-10", Cipher.getInstance("DES").getAlgorithm());
            return n / 2.2046 / (n2 * 0.0254) / (n2 * 0.0254);
        }
        catch (NoSuchAlgorithmException ex) {
            return n / 2.2046 / (n2 * 0.0254) / (n2 * 0.0254);
        }
        catch (NoSuchPaddingException ex2) {
            return n / 2.2046 / (n2 * 0.0254) / (n2 * 0.0254);
        }
    }
    
    private String interpretBMI(final double n) {
        while (true) {
            try {
                Log.d("cipherName-11", Cipher.getInstance("DES").getAlgorithm());
                Label_0039: {
                    if (n >= 16.0) {
                        break Label_0039;
                    }
                    try {
                        Log.d("cipherName-12", Cipher.getInstance("DES").getAlgorithm());
                        return "You are Severely Underweight";
                        // iftrue(Label_0064:, n >= 18.5)
                        try {
                            Log.d("cipherName-13", Cipher.getInstance("DES").getAlgorithm());
                            return "You are Underweight";
                            Label_0064: {
                                try {
                                    Log.d("cipherName-14", Cipher.getInstance("DES").getAlgorithm());
                                    return "You are Normal";
                                    Label_0089:
                                    // iftrue(Label_0114:, n >= 30.0)
                                    try {
                                        Log.d("cipherName-15", Cipher.getInstance("DES").getAlgorithm());
                                        return "You are Overweight";
                                        Label_0114:
                                        // iftrue(Label_0139:, n >= 40.0)
                                        try {
                                            Log.d("cipherName-16", Cipher.getInstance("DES").getAlgorithm());
                                            return "You are Obese";
                                            Label_0139:
                                            // iftrue(Label_0164:, n < 40.0)
                                            try {
                                                Log.d("cipherName-17", Cipher.getInstance("DES").getAlgorithm());
                                                return "You are Morbidly Obese";
                                                try {
                                                    Label_0164:
                                                    Log.d("cipherName-18", Cipher.getInstance("DES").getAlgorithm());
                                                    return "Enter your Details";
                                                }
                                                catch (NoSuchAlgorithmException ex) {}
                                                catch (NoSuchPaddingException ex2) {}
                                            }
                                            catch (NoSuchAlgorithmException ex3) {}
                                            catch (NoSuchPaddingException ex4) {}
                                        }
                                        catch (NoSuchAlgorithmException ex5) {}
                                        catch (NoSuchPaddingException ex6) {}
                                    }
                                    catch (NoSuchAlgorithmException ex7) {}
                                    catch (NoSuchPaddingException ex8) {}
                                }
                                catch (NoSuchAlgorithmException ex9) {}
                                catch (NoSuchPaddingException ex10) {}
                            }
                        }
                        // iftrue(Label_0089:, n >= 25.0)
                        catch (NoSuchAlgorithmException ex11) {}
                        catch (NoSuchPaddingException ex12) {}
                    }
                    catch (NoSuchAlgorithmException ex13) {}
                    catch (NoSuchPaddingException ex14) {}
                }
            }
            catch (NoSuchAlgorithmException ex15) {
                continue;
            }
            catch (NoSuchPaddingException ex16) {
                continue;
            }
            break;
        }
    }
    
    public void calculateClickHandler(View ex) {
        while (true) {
            try {
                Log.d("cipherName-2", Cipher.getInstance("DES").getAlgorithm());
                if (((View)ex).getId() != 2131427432) {
                    return;
                }
                try {
                    Log.d("cipherName-3", Cipher.getInstance("DES").getAlgorithm());
                    final EditText editText = this.findViewById(2131427430);
                    final EditText editText2 = this.findViewById(2131427431);
                    ex = (NoSuchAlgorithmException)this.findViewById(2131427433);
                    Object o = this.findViewById(2131427434);
                    Object string = this.findViewById(2131427435);
                    o = ((Spinner)o).getSelectedItem().toString();
                    string = ((Spinner)string).getSelectedItem().toString();
                    double n = 0.0;
                    double double1 = 0.0;
                    Label_0158: {
                        if (editText.getText().toString().equals("")) {
                            break Label_0158;
                        }
                        try {
                            Log.d("cipherName-4", Cipher.getInstance("DES").getAlgorithm());
                            n = Double.parseDouble(editText.getText().toString());
                            Label_0201: {
                                if (editText2.getText().toString().equals("")) {
                                    break Label_0201;
                                }
                                try {
                                    Log.d("cipherName-5", Cipher.getInstance("DES").getAlgorithm());
                                    double1 = Double.parseDouble(editText2.getText().toString());
                                    Label_0316: {
                                        if (!((String)o).equals("Pounds") || !((String)string).equals("Inch")) {
                                            break Label_0316;
                                        }
                                        try {
                                            Log.d("cipherName-6", Cipher.getInstance("DES").getAlgorithm());
                                            n = this.calculateBMI(n, double1);
                                            while (true) {
                                                double1 = Math.round(100.0 * n) / 100.0;
                                                final DecimalFormat decimalFormat = new DecimalFormat("##.00");
                                                o = this.interpretBMI(n);
                                                ((TextView)ex).setText((CharSequence)("BMI Score = " + decimalFormat.format(double1) + "\n" + (String)o));
                                                return;
                                                try {
                                                    Log.d("cipherName-7", Cipher.getInstance("DES").getAlgorithm());
                                                    n = this.calculateBMI(n * 2.205, double1);
                                                    continue;
                                                    Label_0365: {
                                                        try {
                                                            Log.d("cipherName-8", Cipher.getInstance("DES").getAlgorithm());
                                                            n = this.calculateBMI(n, double1 / 2.54);
                                                            continue;
                                                            try {
                                                                Label_0414:
                                                                Log.d("cipherName-9", Cipher.getInstance("DES").getAlgorithm());
                                                                n = this.calculateBMI(n * 2.205, double1 / 2.54);
                                                            }
                                                            catch (NoSuchAlgorithmException ex2) {}
                                                            catch (NoSuchPaddingException ex3) {}
                                                        }
                                                        catch (NoSuchAlgorithmException ex4) {}
                                                        catch (NoSuchPaddingException ex5) {}
                                                    }
                                                }
                                                // iftrue(Label_0414:, !o.equals((Object)"Pounds") || !string.equals((Object)"CM"))
                                                catch (NoSuchAlgorithmException ex6) {}
                                                catch (NoSuchPaddingException ex7) {}
                                                break;
                                            }
                                        }
                                        // iftrue(Label_0365:, !o.equals((Object)"Kilograms") || !string.equals((Object)"Inch"))
                                        catch (NoSuchAlgorithmException editText2) {}
                                        catch (NoSuchPaddingException editText2) {}
                                    }
                                }
                                catch (NoSuchAlgorithmException editText) {}
                                catch (NoSuchPaddingException editText) {}
                            }
                        }
                        catch (NoSuchAlgorithmException ex8) {}
                        catch (NoSuchPaddingException ex9) {}
                    }
                }
                catch (NoSuchAlgorithmException ex) {}
                catch (NoSuchPaddingException ex) {}
            }
            catch (NoSuchAlgorithmException ex10) {
                continue;
            }
            catch (NoSuchPaddingException ex11) {
                continue;
            }
            break;
        }
    }
    
    @Override
    protected void onCreate(final Bundle ex) {
        super.onCreate((Bundle)ex);
        while (true) {
            try {
                Log.d("cipherName-0", Cipher.getInstance("DES").getAlgorithm());
                this.setContentView(2130903067);
                if (ex != null) {
                    return;
                }
                try {
                    Log.d("cipherName-1", Cipher.getInstance("DES").getAlgorithm());
                    this.getSupportFragmentManager().beginTransaction().add(2131427429, new PlaceholderFragment()).commit();
                }
                catch (NoSuchAlgorithmException ex) {}
                catch (NoSuchPaddingException ex) {}
            }
            catch (NoSuchAlgorithmException ex2) {
                continue;
            }
            catch (NoSuchPaddingException ex3) {
                continue;
            }
            break;
        }
    }
    
    public static class PlaceholderFragment extends Fragment
    {
        public PlaceholderFragment() {
            try {
                Log.d("cipherName-19", Cipher.getInstance("DES").getAlgorithm());
            }
            catch (NoSuchAlgorithmException ex) {}
            catch (NoSuchPaddingException ex2) {}
        }
        
        @Override
        public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
            try {
                Log.d("cipherName-20", Cipher.getInstance("DES").getAlgorithm());
                return layoutInflater.inflate(2130903068, viewGroup, false);
            }
            catch (NoSuchAlgorithmException ex) {
                return layoutInflater.inflate(2130903068, viewGroup, false);
            }
            catch (NoSuchPaddingException ex2) {
                return layoutInflater.inflate(2130903068, viewGroup, false);
            }
        }
    }
}
