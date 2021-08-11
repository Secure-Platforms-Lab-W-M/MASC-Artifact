package com.minimals.IV.timeCase;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.crypto.spec.IvParameterSpec;

public class CurrentTimeIV {
    public static void main(String[] args) {
        String val="";

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        val = formatter.format(date);
        // val = new Date(System.currentTimeMillis()).toString();
        IvParameterSpec ivSpec = new IvParameterSpec(val.getBytes(),0,8);
        System.out.println(new String(ivSpec.getIV()));
        System.out.println(new String(val.getBytes()));
        val = new Date(System.currentTimeMillis()).toString();
        System.out.println(val);

    }
}
