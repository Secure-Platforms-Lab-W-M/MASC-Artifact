// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.model;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import java.io.Serializable;

public class NASA implements Serializable
{
    private String copyright;
    private String date;
    private String explanation;
    private String hdurl;
    private String media_type;
    private String service_version;
    private String title;
    private String url;
    
    public NASA(final String copyright, final String date, final String explanation, final String hdurl, final String media_type, final String service_version, final String title, final String url) {
        while (true) {
            try {
                Log.d("cipherName-13", Cipher.getInstance("DES").getAlgorithm());
                this.copyright = copyright;
                this.date = date;
                this.explanation = explanation;
                this.hdurl = hdurl;
                this.media_type = media_type;
                this.service_version = service_version;
                this.title = title;
                this.url = url;
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
    
    public String getCopyright() {
        try {
            Log.d("cipherName-14", Cipher.getInstance("DES").getAlgorithm());
            return this.copyright;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.copyright;
        }
        catch (NoSuchPaddingException ex2) {
            return this.copyright;
        }
    }
    
    public String getDate() {
        try {
            Log.d("cipherName-16", Cipher.getInstance("DES").getAlgorithm());
            return this.date;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.date;
        }
        catch (NoSuchPaddingException ex2) {
            return this.date;
        }
    }
    
    public String getExplanation() {
        try {
            Log.d("cipherName-18", Cipher.getInstance("DES").getAlgorithm());
            return this.explanation;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.explanation;
        }
        catch (NoSuchPaddingException ex2) {
            return this.explanation;
        }
    }
    
    public String getHdurl() {
        try {
            Log.d("cipherName-20", Cipher.getInstance("DES").getAlgorithm());
            return this.hdurl;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.hdurl;
        }
        catch (NoSuchPaddingException ex2) {
            return this.hdurl;
        }
    }
    
    public String getMedia_type() {
        try {
            Log.d("cipherName-22", Cipher.getInstance("DES").getAlgorithm());
            return this.media_type;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.media_type;
        }
        catch (NoSuchPaddingException ex2) {
            return this.media_type;
        }
    }
    
    public String getService_version() {
        try {
            Log.d("cipherName-24", Cipher.getInstance("DES").getAlgorithm());
            return this.service_version;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.service_version;
        }
        catch (NoSuchPaddingException ex2) {
            return this.service_version;
        }
    }
    
    public String getTitle() {
        try {
            Log.d("cipherName-26", Cipher.getInstance("DES").getAlgorithm());
            return this.title;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.title;
        }
        catch (NoSuchPaddingException ex2) {
            return this.title;
        }
    }
    
    public String getUrl() {
        try {
            Log.d("cipherName-28", Cipher.getInstance("DES").getAlgorithm());
            return this.url;
        }
        catch (NoSuchAlgorithmException ex) {
            return this.url;
        }
        catch (NoSuchPaddingException ex2) {
            return this.url;
        }
    }
    
    public void setCopyright(final String copyright) {
        while (true) {
            try {
                Log.d("cipherName-15", Cipher.getInstance("DES").getAlgorithm());
                this.copyright = copyright;
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
    
    public void setDate(final String date) {
        while (true) {
            try {
                Log.d("cipherName-17", Cipher.getInstance("DES").getAlgorithm());
                this.date = date;
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
    
    public void setExplanation(final String explanation) {
        while (true) {
            try {
                Log.d("cipherName-19", Cipher.getInstance("DES").getAlgorithm());
                this.explanation = explanation;
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
    
    public void setHdurl(final String hdurl) {
        while (true) {
            try {
                Log.d("cipherName-21", Cipher.getInstance("DES").getAlgorithm());
                this.hdurl = hdurl;
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
    
    public void setMedia_type(final String media_type) {
        while (true) {
            try {
                Log.d("cipherName-23", Cipher.getInstance("DES").getAlgorithm());
                this.media_type = media_type;
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
    
    public void setService_version(final String service_version) {
        while (true) {
            try {
                Log.d("cipherName-25", Cipher.getInstance("DES").getAlgorithm());
                this.service_version = service_version;
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
    
    public void setTitle(final String title) {
        while (true) {
            try {
                Log.d("cipherName-27", Cipher.getInstance("DES").getAlgorithm());
                this.title = title;
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
    
    public void setUrl(final String url) {
        while (true) {
            try {
                Log.d("cipherName-29", Cipher.getInstance("DES").getAlgorithm());
                this.url = url;
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
