package com.oF2pks.kalturadeviceinfos;


import android.annotation.SuppressLint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Utils {
    public static String getProp(String s) {
        try {
            @SuppressLint("PrivateApi")
            Class<?> aClass = Class.forName("android.os.SystemProperties");
            Method method = aClass.getMethod("get", String.class);
            Object platform = method.invoke(null, s);

            return platform instanceof String ? (String) platform : "<" + platform + ">";

        } catch (Exception e) {
            return "<" + e + ">";
        }
    }
    public static JSONObject semicolonJson(String s,String eq, String end) throws JSONException {
        JSONObject archT = new JSONObject();
        String tmp = s;
        int i,j= 0;
        while (tmp.contains(end)){
            i=tmp.indexOf(end);
            j=tmp.indexOf(eq);
            archT.put(tmp.substring(0,j),tmp.substring(j+1,i));
            tmp=tmp.substring(i+1);
        }
        j=tmp.indexOf(eq);
        archT.put(tmp.substring(0,j),tmp.substring(j+1));
        return archT;
    }
    public static JSONObject semicolonSortedJson(String s,String eq, String end) throws JSONException {
        SortedSet set = new TreeSet();
        JSONObject archT = new JSONObject();
        String tmp = s;
        int i = 0;
        while (tmp.contains(end)){
            i=tmp.indexOf(end);
            set.add(tmp.substring(0,i));
            tmp=tmp.substring(i+1);
        }
        set.add(tmp);
        Iterator it = set.iterator();
        while (it.hasNext()) {
            tmp=it.next().toString();
            i=tmp.indexOf(eq);
            archT.put(tmp.substring(0,i),tmp.substring(i+1));
        }

        return archT;
    }

    public static String getZinfo(String s, String sPlus, boolean bool) {
        try {
            Process p = Runtime.getRuntime().exec(s);
            InputStream is = null;
            //if (p.waitFor() == 0) {
            is = p.getInputStream();
            /*} else {
                is = p.getErrorStream();
            }*/
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String tmp;
            String tmp2 = "";

            if (bool) br.readLine();
            while ((tmp = br.readLine()) != null)
            {
                tmp2 +=sPlus+tmp;
            }
            is.close();
            br.close();
            if (tmp2.length() !=0) return tmp2;
            return "Unknow";
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }

}
