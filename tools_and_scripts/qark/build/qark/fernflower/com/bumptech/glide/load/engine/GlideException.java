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

public final class GlideException extends Exception {
   private static final StackTraceElement[] EMPTY_ELEMENTS = new StackTraceElement[0];
   private static final long serialVersionUID = 1L;
   private final List causes;
   private Class dataClass;
   private DataSource dataSource;
   private String detailMessage;
   private Exception exception;
   private Key key;

   public GlideException(String var1) {
      this(var1, Collections.emptyList());
   }

   public GlideException(String var1, Throwable var2) {
      this(var1, Collections.singletonList(var2));
   }

   public GlideException(String var1, List var2) {
      this.detailMessage = var1;
      this.setStackTrace(EMPTY_ELEMENTS);
      this.causes = var2;
   }

   private void addRootCauses(Throwable var1, List var2) {
      if (!(var1 instanceof GlideException)) {
         var2.add(var1);
      } else {
         Iterator var3 = ((GlideException)var1).getCauses().iterator();

         while(var3.hasNext()) {
            this.addRootCauses((Throwable)var3.next(), var2);
         }

      }
   }

   private static void appendCauses(List var0, Appendable var1) {
      try {
         appendCausesWrapped(var0, var1);
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static void appendCausesWrapped(List var0, Appendable var1) throws IOException {
      int var3 = var0.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         var1.append("Cause (").append(String.valueOf(var2 + 1)).append(" of ").append(String.valueOf(var3)).append("): ");
         Throwable var4 = (Throwable)var0.get(var2);
         if (var4 instanceof GlideException) {
            ((GlideException)var4).printStackTrace(var1);
         } else {
            appendExceptionMessage(var4, var1);
         }
      }

   }

   private static void appendExceptionMessage(Throwable var0, Appendable var1) {
      try {
         var1.append(var0.getClass().toString()).append(": ").append(var0.getMessage()).append('\n');
      } catch (IOException var2) {
         throw new RuntimeException(var0);
      }
   }

   private void printStackTrace(Appendable var1) {
      appendExceptionMessage(this, var1);
      appendCauses(this.getCauses(), new GlideException.IndentedAppendable(var1));
   }

   public Throwable fillInStackTrace() {
      return this;
   }

   public List getCauses() {
      return this.causes;
   }

   public String getMessage() {
      StringBuilder var3 = new StringBuilder(71);
      var3.append(this.detailMessage);
      Class var1 = this.dataClass;
      String var2 = "";
      StringBuilder var4;
      String var5;
      if (var1 != null) {
         var4 = new StringBuilder();
         var4.append(", ");
         var4.append(this.dataClass);
         var5 = var4.toString();
      } else {
         var5 = "";
      }

      var3.append(var5);
      if (this.dataSource != null) {
         var4 = new StringBuilder();
         var4.append(", ");
         var4.append(this.dataSource);
         var5 = var4.toString();
      } else {
         var5 = "";
      }

      var3.append(var5);
      var5 = var2;
      if (this.key != null) {
         var4 = new StringBuilder();
         var4.append(", ");
         var4.append(this.key);
         var5 = var4.toString();
      }

      var4 = var3.append(var5);
      List var6 = this.getRootCauses();
      if (var6.isEmpty()) {
         return var4.toString();
      } else {
         if (var6.size() == 1) {
            var4.append("\nThere was 1 cause:");
         } else {
            var4.append("\nThere were ");
            var4.append(var6.size());
            var4.append(" causes:");
         }

         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            Throwable var8 = (Throwable)var7.next();
            var4.append('\n');
            var4.append(var8.getClass().getName());
            var4.append('(');
            var4.append(var8.getMessage());
            var4.append(')');
         }

         var4.append("\n call GlideException#logRootCauses(String) for more detail");
         return var4.toString();
      }
   }

   public Exception getOrigin() {
      return this.exception;
   }

   public List getRootCauses() {
      ArrayList var1 = new ArrayList();
      this.addRootCauses(this, var1);
      return var1;
   }

   public void logRootCauses(String var1) {
      List var4 = this.getRootCauses();
      int var2 = 0;

      for(int var3 = var4.size(); var2 < var3; ++var2) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Root cause (");
         var5.append(var2 + 1);
         var5.append(" of ");
         var5.append(var3);
         var5.append(")");
         Log.i(var1, var5.toString(), (Throwable)var4.get(var2));
      }

   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream var1) {
      this.printStackTrace((Appendable)var1);
   }

   public void printStackTrace(PrintWriter var1) {
      this.printStackTrace((Appendable)var1);
   }

   void setLoggingDetails(Key var1, DataSource var2) {
      this.setLoggingDetails(var1, var2, (Class)null);
   }

   void setLoggingDetails(Key var1, DataSource var2, Class var3) {
      this.key = var1;
      this.dataSource = var2;
      this.dataClass = var3;
   }

   public void setOrigin(Exception var1) {
      this.exception = var1;
   }

   private static final class IndentedAppendable implements Appendable {
      private static final String EMPTY_SEQUENCE = "";
      private static final String INDENT = "  ";
      private final Appendable appendable;
      private boolean printedNewLine = true;

      IndentedAppendable(Appendable var1) {
         this.appendable = var1;
      }

      private CharSequence safeSequence(CharSequence var1) {
         return (CharSequence)(var1 == null ? "" : var1);
      }

      public Appendable append(char var1) throws IOException {
         boolean var3 = this.printedNewLine;
         boolean var2 = false;
         if (var3) {
            this.printedNewLine = false;
            this.appendable.append("  ");
         }

         if (var1 == '\n') {
            var2 = true;
         }

         this.printedNewLine = var2;
         this.appendable.append(var1);
         return this;
      }

      public Appendable append(CharSequence var1) throws IOException {
         var1 = this.safeSequence(var1);
         return this.append(var1, 0, var1.length());
      }

      public Appendable append(CharSequence var1, int var2, int var3) throws IOException {
         var1 = this.safeSequence(var1);
         boolean var4 = this.printedNewLine;
         boolean var5 = false;
         if (var4) {
            this.printedNewLine = false;
            this.appendable.append("  ");
         }

         var4 = var5;
         if (var1.length() > 0) {
            var4 = var5;
            if (var1.charAt(var3 - 1) == '\n') {
               var4 = true;
            }
         }

         this.printedNewLine = var4;
         this.appendable.append(var1, var2, var3);
         return this;
      }
   }
}
