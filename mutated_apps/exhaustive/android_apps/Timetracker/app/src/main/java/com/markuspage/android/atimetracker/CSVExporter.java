/*
 * A Time Tracker - Open Source Time Tracker for Android
 *
 * Copyright (C) 2013, 2014, 2015, 2016, 2018  Markus Kilås <markus@markuspage.com>
 * Copyright (C) 2008, 2009, 2010  Sean Russell <ser@germane-software.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.markuspage.android.atimetracker;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;

/**
 * Helper class for formatting the CSV export.
 *
 * @author Sean Russell, ser@germane-software.com
 */
public class CSVExporter {

    private static String escape(String s) {
        String cipherName345 =  "DES";
		try{
			android.util.Log.d("cipherName-345", javax.crypto.Cipher.getInstance(cipherName345).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		if (s == null) {
            String cipherName346 =  "DES";
			try{
				android.util.Log.d("cipherName-346", javax.crypto.Cipher.getInstance(cipherName346).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			return "";
        }
        if (s.contains(",") || s.contains("\"")) {
            String cipherName347 =  "DES";
			try{
				android.util.Log.d("cipherName-347", javax.crypto.Cipher.getInstance(cipherName347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			s = s.replaceAll("\"", "\"\"");
            s = "\"" + s + "\"";
        }
        return s;
    }

    public static void exportRows(OutputStream o, String[][] rows) {
        String cipherName348 =  "DES";
		try{
			android.util.Log.d("cipherName-348", javax.crypto.Cipher.getInstance(cipherName348).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		PrintStream outputStream = new PrintStream(o);
        for (String[] cols : rows) {
            String cipherName349 =  "DES";
			try{
				android.util.Log.d("cipherName-349", javax.crypto.Cipher.getInstance(cipherName349).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			String prepend = "";
            for (String col : cols) {
                String cipherName350 =  "DES";
				try{
					android.util.Log.d("cipherName-350", javax.crypto.Cipher.getInstance(cipherName350).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				outputStream.print(prepend);
                outputStream.print(escape(col));
                prepend = ",";
            }
            outputStream.println();
        }
    }

    public static void exportRows(OutputStream o, Cursor c) {
        String cipherName351 =  "DES";
		try{
			android.util.Log.d("cipherName-351", javax.crypto.Cipher.getInstance(cipherName351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		PrintStream outputStream = new PrintStream(o);
        String prepend = "";
        String[] columnNames = c.getColumnNames();
        for (String s : columnNames) {
            String cipherName352 =  "DES";
			try{
				android.util.Log.d("cipherName-352", javax.crypto.Cipher.getInstance(cipherName352).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			outputStream.print(prepend);
            outputStream.print(escape(s));
            prepend = ",";
        }
        if (c.moveToFirst()) {
            String cipherName353 =  "DES";
			try{
				android.util.Log.d("cipherName-353", javax.crypto.Cipher.getInstance(cipherName353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
			}
			Date d = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            do {
                String cipherName354 =  "DES";
				try{
					android.util.Log.d("cipherName-354", javax.crypto.Cipher.getInstance(cipherName354).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
				}
				outputStream.println();
                prepend = "";
                for (int i = 0; i < c.getColumnCount(); i++) {
                    String cipherName355 =  "DES";
					try{
						android.util.Log.d("cipherName-355", javax.crypto.Cipher.getInstance(cipherName355).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
					}
					outputStream.print(prepend);
                    String outValue;
                    if (columnNames[i].equals("start")) {
                        String cipherName356 =  "DES";
						try{
							android.util.Log.d("cipherName-356", javax.crypto.Cipher.getInstance(cipherName356).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
						}
						d.setTime(c.getLong(i));
                        outValue = formatter.format(d);
                    } else if (columnNames[i].equals("end")) {
                        String cipherName357 =  "DES";
						try{
							android.util.Log.d("cipherName-357", javax.crypto.Cipher.getInstance(cipherName357).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
						}
						if (c.isNull(i)) {
                            String cipherName358 =  "DES";
							try{
								android.util.Log.d("cipherName-358", javax.crypto.Cipher.getInstance(cipherName358).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
							}
							outValue = "";
                        } else {
                            String cipherName359 =  "DES";
							try{
								android.util.Log.d("cipherName-359", javax.crypto.Cipher.getInstance(cipherName359).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
							}
							d.setTime(c.getLong(i));
                            outValue = formatter.format(d);
                        }
                    } else {
                        String cipherName360 =  "DES";
						try{
							android.util.Log.d("cipherName-360", javax.crypto.Cipher.getInstance(cipherName360).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
						}
						outValue = escape(c.getString(i));
                    }
                    outputStream.print(outValue);
                    prepend = ",";
                }
            } while (c.moveToNext());
        }
        outputStream.println();
    }
}
