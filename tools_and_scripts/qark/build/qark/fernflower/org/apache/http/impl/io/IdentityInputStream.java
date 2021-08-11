package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

public class IdentityInputStream extends InputStream {
   private boolean closed = false;
   // $FF: renamed from: in org.apache.http.io.SessionInputBuffer
   private final SessionInputBuffer field_176;

   public IdentityInputStream(SessionInputBuffer var1) {
      this.field_176 = (SessionInputBuffer)Args.notNull(var1, "Session input buffer");
   }

   public int available() throws IOException {
      SessionInputBuffer var1 = this.field_176;
      return var1 instanceof BufferInfo ? ((BufferInfo)var1).length() : 0;
   }

   public void close() throws IOException {
      this.closed = true;
   }

   public int read() throws IOException {
      return this.closed ? -1 : this.field_176.read();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.closed ? -1 : this.field_176.read(var1, var2, var3);
   }
}
