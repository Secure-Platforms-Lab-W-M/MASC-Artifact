/*
 * Decompiled with CFR 0_124.
 */
package com.google.protobuf;

import com.google.protobuf.ByteString;
import java.io.UnsupportedEncodingException;

public class Internal {
    public static ByteString bytesDefaultValue(String object) {
        try {
            object = ByteString.copyFrom(object.getBytes("ISO-8859-1"));
            return object;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new IllegalStateException("Java VM does not support a standard character set.", unsupportedEncodingException);
        }
    }

    public static boolean isValidUtf8(ByteString byteString) {
        return byteString.isValidUtf8();
    }

    public static String stringDefaultValue(String string2) {
        try {
            string2 = new String(string2.getBytes("ISO-8859-1"), "UTF-8");
            return string2;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new IllegalStateException("Java VM does not support a standard character set.", unsupportedEncodingException);
        }
    }

    public static interface EnumLite {
        public int getNumber();
    }

    public static interface EnumLiteMap<T extends EnumLite> {
        public T findValueByNumber(int var1);
    }

}

