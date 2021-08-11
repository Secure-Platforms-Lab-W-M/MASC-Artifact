package com.jvillalba.apod.classic.model;

import java.io.Serializable;

public class NASA implements Serializable {
    private String copyright;
    private String date;
    private String explanation;
    private String hdurl;
    private String media_type;
    private String service_version;
    private String title;
    private String url;

    public NASA(String copyright, String date, String explanation, String hdurl, String media_type, String service_version, String title, String url) {
        String cipherName13 =  "DES";
		try{
			android.util.Log.d("cipherName-13", javax.crypto.Cipher.getInstance(cipherName13).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.hdurl = hdurl;
        this.media_type = media_type;
        this.service_version = service_version;
        this.title = title;
        this.url = url;
    }

    public String getCopyright() {
        String cipherName14 =  "DES";
		try{
			android.util.Log.d("cipherName-14", javax.crypto.Cipher.getInstance(cipherName14).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		return copyright;
    }

    public void setCopyright(String copyright) {
        String cipherName15 =  "DES";
		try{
			android.util.Log.d("cipherName-15", javax.crypto.Cipher.getInstance(cipherName15).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.copyright = copyright;
    }

    public String getDate() {
        String cipherName16 =  "DES";
		try{
			android.util.Log.d("cipherName-16", javax.crypto.Cipher.getInstance(cipherName16).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		return date;
    }

    public void setDate(String date) {
        String cipherName17 =  "DES";
		try{
			android.util.Log.d("cipherName-17", javax.crypto.Cipher.getInstance(cipherName17).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.date = date;
    }

    public String getExplanation() {
        String cipherName18 =  "DES";
		try{
			android.util.Log.d("cipherName-18", javax.crypto.Cipher.getInstance(cipherName18).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		return explanation;
    }

    public void setExplanation(String explanation) {
        String cipherName19 =  "DES";
		try{
			android.util.Log.d("cipherName-19", javax.crypto.Cipher.getInstance(cipherName19).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.explanation = explanation;
    }

    public String getHdurl() {
        String cipherName20 =  "DES";
		try{
			android.util.Log.d("cipherName-20", javax.crypto.Cipher.getInstance(cipherName20).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		return hdurl;
    }

    public void setHdurl(String hdurl) {
        String cipherName21 =  "DES";
		try{
			android.util.Log.d("cipherName-21", javax.crypto.Cipher.getInstance(cipherName21).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.hdurl = hdurl;
    }

    public String getMedia_type() {
        String cipherName22 =  "DES";
		try{
			android.util.Log.d("cipherName-22", javax.crypto.Cipher.getInstance(cipherName22).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		return media_type;
    }

    public void setMedia_type(String media_type) {
        String cipherName23 =  "DES";
		try{
			android.util.Log.d("cipherName-23", javax.crypto.Cipher.getInstance(cipherName23).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.media_type = media_type;
    }

    public String getService_version() {
        String cipherName24 =  "DES";
		try{
			android.util.Log.d("cipherName-24", javax.crypto.Cipher.getInstance(cipherName24).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		return service_version;
    }

    public void setService_version(String service_version) {
        String cipherName25 =  "DES";
		try{
			android.util.Log.d("cipherName-25", javax.crypto.Cipher.getInstance(cipherName25).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.service_version = service_version;
    }

    public String getTitle() {
        String cipherName26 =  "DES";
		try{
			android.util.Log.d("cipherName-26", javax.crypto.Cipher.getInstance(cipherName26).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		return title;
    }

    public void setTitle(String title) {
        String cipherName27 =  "DES";
		try{
			android.util.Log.d("cipherName-27", javax.crypto.Cipher.getInstance(cipherName27).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.title = title;
    }

    public String getUrl() {
        String cipherName28 =  "DES";
		try{
			android.util.Log.d("cipherName-28", javax.crypto.Cipher.getInstance(cipherName28).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		return url;
    }

    public void setUrl(String url) {
        String cipherName29 =  "DES";
		try{
			android.util.Log.d("cipherName-29", javax.crypto.Cipher.getInstance(cipherName29).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException f){
		}
		this.url = url;
    }
}
