// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.stream;

import java.io.IOException;
import java.io.Writer;
import java.io.Flushable;
import java.io.Closeable;

public class JsonWriter implements Closeable, Flushable
{
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    private static final String[] REPLACEMENT_CHARS;
    private String deferredName;
    private boolean htmlSafe;
    private String indent;
    private boolean lenient;
    private final Writer out;
    private String separator;
    private boolean serializeNulls;
    private int[] stack;
    private int stackSize;
    
    static {
        REPLACEMENT_CHARS = new String[128];
        for (int i = 0; i <= 31; ++i) {
            JsonWriter.REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
        }
        JsonWriter.REPLACEMENT_CHARS[34] = "\\\"";
        JsonWriter.REPLACEMENT_CHARS[92] = "\\\\";
        JsonWriter.REPLACEMENT_CHARS[9] = "\\t";
        JsonWriter.REPLACEMENT_CHARS[8] = "\\b";
        JsonWriter.REPLACEMENT_CHARS[10] = "\\n";
        JsonWriter.REPLACEMENT_CHARS[13] = "\\r";
        JsonWriter.REPLACEMENT_CHARS[12] = "\\f";
        (HTML_SAFE_REPLACEMENT_CHARS = JsonWriter.REPLACEMENT_CHARS.clone())[60] = "\\u003c";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
        JsonWriter.HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
    }
    
    public JsonWriter(final Writer out) {
        this.stack = new int[32];
        this.stackSize = 0;
        this.push(6);
        this.separator = ":";
        this.serializeNulls = true;
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.out = out;
    }
    
    private void beforeName() throws IOException {
        final int peek = this.peek();
        if (peek == 5) {
            this.out.write(44);
        }
        else if (peek != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        this.newline();
        this.replaceTop(4);
    }
    
    private void beforeValue() throws IOException {
        switch (this.peek()) {
            default: {
                throw new IllegalStateException("Nesting problem.");
            }
            case 7: {
                if (!this.lenient) {
                    throw new IllegalStateException("JSON must have only one top-level value.");
                }
            }
            case 6: {
                this.replaceTop(7);
            }
            case 1: {
                this.replaceTop(2);
                this.newline();
            }
            case 2: {
                this.out.append(',');
                this.newline();
            }
            case 4: {
                this.out.append((CharSequence)this.separator);
                this.replaceTop(5);
            }
        }
    }
    
    private JsonWriter close(final int n, final int n2, final String s) throws IOException {
        final int peek = this.peek();
        if (peek != n2 && peek != n) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException("Dangling name: " + this.deferredName);
        }
        --this.stackSize;
        if (peek == n2) {
            this.newline();
        }
        this.out.write(s);
        return this;
    }
    
    private void newline() throws IOException {
        if (this.indent != null) {
            this.out.write("\n");
            for (int i = 1; i < this.stackSize; ++i) {
                this.out.write(this.indent);
            }
        }
    }
    
    private JsonWriter open(final int n, final String s) throws IOException {
        this.beforeValue();
        this.push(n);
        this.out.write(s);
        return this;
    }
    
    private int peek() {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        return this.stack[this.stackSize - 1];
    }
    
    private void push(final int n) {
        if (this.stackSize == this.stack.length) {
            final int[] stack = new int[this.stackSize * 2];
            System.arraycopy(this.stack, 0, stack, 0, this.stackSize);
            this.stack = stack;
        }
        this.stack[this.stackSize++] = n;
    }
    
    private void replaceTop(final int n) {
        this.stack[this.stackSize - 1] = n;
    }
    
    private void string(final String s) throws IOException {
        String[] array;
        if (this.htmlSafe) {
            array = JsonWriter.HTML_SAFE_REPLACEMENT_CHARS;
        }
        else {
            array = JsonWriter.REPLACEMENT_CHARS;
        }
        this.out.write("\"");
        int n = 0;
        final int length = s.length();
        int i = 0;
    Label_0071_Outer:
        while (i < length) {
            final char char1 = s.charAt(i);
            while (true) {
                String s2 = null;
                Label_0101: {
                    int n2;
                    if (char1 < '\u0080') {
                        if ((s2 = array[char1]) != null) {
                            break Label_0101;
                        }
                        n2 = n;
                    }
                    else {
                        if (char1 == '\u2028') {
                            s2 = "\\u2028";
                            break Label_0101;
                        }
                        n2 = n;
                        if (char1 == '\u2029') {
                            s2 = "\\u2029";
                            break Label_0101;
                        }
                    }
                    ++i;
                    n = n2;
                    continue Label_0071_Outer;
                }
                if (n < i) {
                    this.out.write(s, n, i - n);
                }
                this.out.write(s2);
                int n2 = i + 1;
                continue;
            }
        }
        if (n < length) {
            this.out.write(s, n, length - n);
        }
        this.out.write("\"");
    }
    
    private void writeDeferredName() throws IOException {
        if (this.deferredName != null) {
            this.beforeName();
            this.string(this.deferredName);
            this.deferredName = null;
        }
    }
    
    public JsonWriter beginArray() throws IOException {
        this.writeDeferredName();
        return this.open(1, "[");
    }
    
    public JsonWriter beginObject() throws IOException {
        this.writeDeferredName();
        return this.open(3, "{");
    }
    
    @Override
    public void close() throws IOException {
        this.out.close();
        final int stackSize = this.stackSize;
        if (stackSize > 1 || (stackSize == 1 && this.stack[stackSize - 1] != 7)) {
            throw new IOException("Incomplete document");
        }
        this.stackSize = 0;
    }
    
    public JsonWriter endArray() throws IOException {
        return this.close(1, 2, "]");
    }
    
    public JsonWriter endObject() throws IOException {
        return this.close(3, 5, "}");
    }
    
    @Override
    public void flush() throws IOException {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.out.flush();
    }
    
    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }
    
    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }
    
    public boolean isLenient() {
        return this.lenient;
    }
    
    public JsonWriter jsonValue(final String s) throws IOException {
        if (s == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        this.out.append((CharSequence)s);
        return this;
    }
    
    public JsonWriter name(final String deferredName) throws IOException {
        if (deferredName == null) {
            throw new NullPointerException("name == null");
        }
        if (this.deferredName != null) {
            throw new IllegalStateException();
        }
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
        this.deferredName = deferredName;
        return this;
    }
    
    public JsonWriter nullValue() throws IOException {
        if (this.deferredName != null) {
            if (!this.serializeNulls) {
                this.deferredName = null;
                return this;
            }
            this.writeDeferredName();
        }
        this.beforeValue();
        this.out.write("null");
        return this;
    }
    
    public final void setHtmlSafe(final boolean htmlSafe) {
        this.htmlSafe = htmlSafe;
    }
    
    public final void setIndent(final String indent) {
        if (indent.length() == 0) {
            this.indent = null;
            this.separator = ":";
            return;
        }
        this.indent = indent;
        this.separator = ": ";
    }
    
    public final void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }
    
    public final void setSerializeNulls(final boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }
    
    public JsonWriter value(final double n) throws IOException {
        this.writeDeferredName();
        if (!this.lenient && (Double.isNaN(n) || Double.isInfinite(n))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + n);
        }
        this.beforeValue();
        this.out.append((CharSequence)Double.toString(n));
        return this;
    }
    
    public JsonWriter value(final long n) throws IOException {
        this.writeDeferredName();
        this.beforeValue();
        this.out.write(Long.toString(n));
        return this;
    }
    
    public JsonWriter value(final Boolean b) throws IOException {
        if (b == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        final Writer out = this.out;
        String s;
        if (b) {
            s = "true";
        }
        else {
            s = "false";
        }
        out.write(s);
        return this;
    }
    
    public JsonWriter value(final Number n) throws IOException {
        if (n == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        final String string = n.toString();
        if (!this.lenient && (string.equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + n);
        }
        this.beforeValue();
        this.out.append((CharSequence)string);
        return this;
    }
    
    public JsonWriter value(final String s) throws IOException {
        if (s == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        this.string(s);
        return this;
    }
    
    public JsonWriter value(final boolean b) throws IOException {
        this.writeDeferredName();
        this.beforeValue();
        final Writer out = this.out;
        String s;
        if (b) {
            s = "true";
        }
        else {
            s = "false";
        }
        out.write(s);
        return this;
    }
}
