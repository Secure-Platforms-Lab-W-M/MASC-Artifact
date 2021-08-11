package org.apache.http.impl.conn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.http.util.Args;

public class Wire {
   // $FF: renamed from: id java.lang.String
   private final String field_184;
   private final Log log;

   public Wire(Log var1) {
      this(var1, "");
   }

   public Wire(Log var1, String var2) {
      this.log = var1;
      this.field_184 = var2;
   }

   private void wire(String var1, InputStream var2) throws IOException {
      StringBuilder var4 = new StringBuilder();

      while(true) {
         while(true) {
            int var3 = var2.read();
            if (var3 == -1) {
               if (var4.length() > 0) {
                  var4.append('"');
                  var4.insert(0, '"');
                  var4.insert(0, var1);
                  Log var7 = this.log;
                  StringBuilder var8 = new StringBuilder();
                  var8.append(this.field_184);
                  var8.append(" ");
                  var8.append(var4.toString());
                  var7.debug(var8.toString());
               }

               return;
            }

            if (var3 == 13) {
               var4.append("[\\r]");
            } else if (var3 == 10) {
               var4.append("[\\n]\"");
               var4.insert(0, "\"");
               var4.insert(0, var1);
               Log var5 = this.log;
               StringBuilder var6 = new StringBuilder();
               var6.append(this.field_184);
               var6.append(" ");
               var6.append(var4.toString());
               var5.debug(var6.toString());
               var4.setLength(0);
            } else if (var3 >= 32 && var3 <= 127) {
               var4.append((char)var3);
            } else {
               var4.append("[0x");
               var4.append(Integer.toHexString(var3));
               var4.append("]");
            }
         }
      }
   }

   public boolean enabled() {
      return this.log.isDebugEnabled();
   }

   public void input(int var1) throws IOException {
      this.input(new byte[]{(byte)var1});
   }

   public void input(InputStream var1) throws IOException {
      Args.notNull(var1, "Input");
      this.wire("<< ", var1);
   }

   public void input(String var1) throws IOException {
      Args.notNull(var1, "Input");
      this.input(var1.getBytes());
   }

   public void input(byte[] var1) throws IOException {
      Args.notNull(var1, "Input");
      this.wire("<< ", new ByteArrayInputStream(var1));
   }

   public void input(byte[] var1, int var2, int var3) throws IOException {
      Args.notNull(var1, "Input");
      this.wire("<< ", new ByteArrayInputStream(var1, var2, var3));
   }

   public void output(int var1) throws IOException {
      this.output(new byte[]{(byte)var1});
   }

   public void output(InputStream var1) throws IOException {
      Args.notNull(var1, "Output");
      this.wire(">> ", var1);
   }

   public void output(String var1) throws IOException {
      Args.notNull(var1, "Output");
      this.output(var1.getBytes());
   }

   public void output(byte[] var1) throws IOException {
      Args.notNull(var1, "Output");
      this.wire(">> ", new ByteArrayInputStream(var1));
   }

   public void output(byte[] var1, int var2, int var3) throws IOException {
      Args.notNull(var1, "Output");
      this.wire(">> ", new ByteArrayInputStream(var1, var2, var3));
   }
}
