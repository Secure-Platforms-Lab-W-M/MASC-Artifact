// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.text.ParseException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;

final class DefaultDateTypeAdapter extends TypeAdapter<Date>
{
    private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";
    private final Class<? extends Date> dateType;
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;
    
    public DefaultDateTypeAdapter(final int n, final int n2) {
        this(Date.class, DateFormat.getDateTimeInstance(n, n2, Locale.US), DateFormat.getDateTimeInstance(n, n2));
    }
    
    DefaultDateTypeAdapter(final Class<? extends Date> clazz) {
        this(clazz, DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
    }
    
    DefaultDateTypeAdapter(final Class<? extends Date> clazz, final int n) {
        this(clazz, DateFormat.getDateInstance(n, Locale.US), DateFormat.getDateInstance(n));
    }
    
    public DefaultDateTypeAdapter(final Class<? extends Date> clazz, final int n, final int n2) {
        this(clazz, DateFormat.getDateTimeInstance(n, n2, Locale.US), DateFormat.getDateTimeInstance(n, n2));
    }
    
    DefaultDateTypeAdapter(final Class<? extends Date> clazz, final String s) {
        this(clazz, new SimpleDateFormat(s, Locale.US), new SimpleDateFormat(s));
    }
    
    DefaultDateTypeAdapter(final Class<? extends Date> dateType, final DateFormat enUsFormat, final DateFormat localFormat) {
        if (dateType != Date.class && dateType != java.sql.Date.class && dateType != Timestamp.class) {
            throw new IllegalArgumentException("Date type must be one of " + Date.class + ", " + Timestamp.class + ", or " + java.sql.Date.class + " but was " + dateType);
        }
        this.dateType = dateType;
        this.enUsFormat = enUsFormat;
        this.localFormat = localFormat;
    }
    
    private Date deserializeToDate(String s) {
        final DateFormat localFormat = this.localFormat;
        // monitorenter(localFormat)
        try {
            return this.localFormat.parse(s);
        }
        catch (ParseException ex2) {
            final DefaultDateTypeAdapter defaultDateTypeAdapter = this;
            final DateFormat dateFormat = defaultDateTypeAdapter.enUsFormat;
            final String s2 = s;
            final Date parse = dateFormat.parse(s2);
            return parse;
        }
        finally {
            final String s3;
            s = s3;
        }
        // monitorexit(localFormat)
        try {
            final DefaultDateTypeAdapter defaultDateTypeAdapter = this;
            final DateFormat dateFormat = defaultDateTypeAdapter.enUsFormat;
            final String s2 = s;
            final Date parse2;
            final Date parse = parse2 = dateFormat.parse(s2);
            return parse2;
        }
        catch (ParseException ex3) {
            try {
                // monitorexit(localFormat)
                return ISO8601Utils.parse(s, new ParsePosition(0));
            }
            catch (ParseException ex) {
                throw new JsonSyntaxException(s, ex);
            }
        }
    }
    
    @Override
    public Date read(final JsonReader jsonReader) throws IOException {
        Date deserializeToDate;
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            deserializeToDate = null;
        }
        else {
            final Date date = deserializeToDate = this.deserializeToDate(jsonReader.nextString());
            if (this.dateType != Date.class) {
                if (this.dateType == Timestamp.class) {
                    return new Timestamp(date.getTime());
                }
                if (this.dateType == java.sql.Date.class) {
                    return new java.sql.Date(date.getTime());
                }
                throw new AssertionError();
            }
        }
        return deserializeToDate;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DefaultDateTypeAdapter");
        sb.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
        return sb.toString();
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final Date date) throws IOException {
        if (date == null) {
            jsonWriter.nullValue();
            return;
        }
        synchronized (this.localFormat) {
            jsonWriter.value(this.enUsFormat.format(date));
        }
    }
}
