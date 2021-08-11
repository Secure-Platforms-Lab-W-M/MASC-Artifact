// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

public final class JsonSyntaxException extends JsonParseException
{
    private static final long serialVersionUID = 1L;
    
    public JsonSyntaxException(final String s) {
        super(s);
    }
    
    public JsonSyntaxException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public JsonSyntaxException(final Throwable t) {
        super(t);
    }
}
