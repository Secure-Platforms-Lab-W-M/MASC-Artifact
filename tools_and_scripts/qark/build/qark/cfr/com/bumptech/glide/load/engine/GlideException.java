/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GlideException
extends Exception {
    private static final StackTraceElement[] EMPTY_ELEMENTS = new StackTraceElement[0];
    private static final long serialVersionUID = 1L;
    private final List<Throwable> causes;
    private Class<?> dataClass;
    private DataSource dataSource;
    private String detailMessage;
    private Exception exception;
    private Key key;

    public GlideException(String string2) {
        this(string2, Collections.emptyList());
    }

    public GlideException(String string2, Throwable throwable) {
        this(string2, Collections.singletonList(throwable));
    }

    public GlideException(String string2, List<Throwable> list) {
        this.detailMessage = string2;
        this.setStackTrace(EMPTY_ELEMENTS);
        this.causes = list;
    }

    private void addRootCauses(Throwable object, List<Throwable> list) {
        if (object instanceof GlideException) {
            object = ((GlideException)object).getCauses().iterator();
            while (object.hasNext()) {
                this.addRootCauses((Throwable)object.next(), list);
            }
            return;
        }
        list.add((Throwable)object);
    }

    private static void appendCauses(List<Throwable> list, Appendable appendable) {
        try {
            GlideException.appendCausesWrapped(list, appendable);
            return;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    private static void appendCausesWrapped(List<Throwable> list, Appendable appendable) throws IOException {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            appendable.append("Cause (").append(String.valueOf(i + 1)).append(" of ").append(String.valueOf(n)).append("): ");
            Throwable throwable = list.get(i);
            if (throwable instanceof GlideException) {
                ((GlideException)throwable).printStackTrace(appendable);
                continue;
            }
            GlideException.appendExceptionMessage(throwable, appendable);
        }
    }

    private static void appendExceptionMessage(Throwable throwable, Appendable appendable) {
        try {
            appendable.append(throwable.getClass().toString()).append(": ").append(throwable.getMessage()).append('\n');
            return;
        }
        catch (IOException iOException) {
            throw new RuntimeException(throwable);
        }
    }

    private void printStackTrace(Appendable appendable) {
        GlideException.appendExceptionMessage(this, appendable);
        GlideException.appendCauses(this.getCauses(), new IndentedAppendable(appendable));
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public List<Throwable> getCauses() {
        return this.causes;
    }

    @Override
    public String getMessage() {
        Object object = new StringBuilder(71);
        object.append(this.detailMessage);
        Object object2 = this.dataClass;
        Object object3 = "";
        if (object2 != null) {
            object2 = new StringBuilder();
            object2.append(", ");
            object2.append(this.dataClass);
            object2 = object2.toString();
        } else {
            object2 = "";
        }
        object.append((String)object2);
        if (this.dataSource != null) {
            object2 = new StringBuilder();
            object2.append(", ");
            object2.append((Object)this.dataSource);
            object2 = object2.toString();
        } else {
            object2 = "";
        }
        object.append((String)object2);
        object2 = object3;
        if (this.key != null) {
            object2 = new StringBuilder();
            object2.append(", ");
            object2.append(this.key);
            object2 = object2.toString();
        }
        object2 = object.append((String)object2);
        object3 = this.getRootCauses();
        if (object3.isEmpty()) {
            return object2.toString();
        }
        if (object3.size() == 1) {
            object2.append("\nThere was 1 cause:");
        } else {
            object2.append("\nThere were ");
            object2.append(object3.size());
            object2.append(" causes:");
        }
        object3 = object3.iterator();
        while (object3.hasNext()) {
            object = (Throwable)object3.next();
            object2.append('\n');
            object2.append(object.getClass().getName());
            object2.append('(');
            object2.append(object.getMessage());
            object2.append(')');
        }
        object2.append("\n call GlideException#logRootCauses(String) for more detail");
        return object2.toString();
    }

    public Exception getOrigin() {
        return this.exception;
    }

    public List<Throwable> getRootCauses() {
        ArrayList<Throwable> arrayList = new ArrayList<Throwable>();
        this.addRootCauses(this, arrayList);
        return arrayList;
    }

    public void logRootCauses(String string2) {
        List<Throwable> list = this.getRootCauses();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Root cause (");
            stringBuilder.append(i + 1);
            stringBuilder.append(" of ");
            stringBuilder.append(n);
            stringBuilder.append(")");
            Log.i((String)string2, (String)stringBuilder.toString(), (Throwable)list.get(i));
        }
    }

    @Override
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }

    @Override
    public void printStackTrace(PrintStream printStream) {
        this.printStackTrace((Appendable)printStream);
    }

    @Override
    public void printStackTrace(PrintWriter printWriter) {
        this.printStackTrace((Appendable)printWriter);
    }

    void setLoggingDetails(Key key, DataSource dataSource) {
        this.setLoggingDetails(key, dataSource, null);
    }

    void setLoggingDetails(Key key, DataSource dataSource, Class<?> class_) {
        this.key = key;
        this.dataSource = dataSource;
        this.dataClass = class_;
    }

    public void setOrigin(Exception exception) {
        this.exception = exception;
    }

    private static final class IndentedAppendable
    implements Appendable {
        private static final String EMPTY_SEQUENCE = "";
        private static final String INDENT = "  ";
        private final Appendable appendable;
        private boolean printedNewLine = true;

        IndentedAppendable(Appendable appendable) {
            this.appendable = appendable;
        }

        private CharSequence safeSequence(CharSequence charSequence) {
            if (charSequence == null) {
                return "";
            }
            return charSequence;
        }

        @Override
        public Appendable append(char c) throws IOException {
            boolean bl = this.printedNewLine;
            boolean bl2 = false;
            if (bl) {
                this.printedNewLine = false;
                this.appendable.append("  ");
            }
            if (c == '\n') {
                bl2 = true;
            }
            this.printedNewLine = bl2;
            this.appendable.append(c);
            return this;
        }

        @Override
        public Appendable append(CharSequence charSequence) throws IOException {
            charSequence = this.safeSequence(charSequence);
            return this.append(charSequence, 0, charSequence.length());
        }

        @Override
        public Appendable append(CharSequence charSequence, int n, int n2) throws IOException {
            charSequence = this.safeSequence(charSequence);
            boolean bl = this.printedNewLine;
            boolean bl2 = false;
            if (bl) {
                this.printedNewLine = false;
                this.appendable.append("  ");
            }
            bl = bl2;
            if (charSequence.length() > 0) {
                bl = bl2;
                if (charSequence.charAt(n2 - 1) == '\n') {
                    bl = true;
                }
            }
            this.printedNewLine = bl;
            this.appendable.append(charSequence, n, n2);
            return this;
        }
    }

}

