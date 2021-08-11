package util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PaddingCipherInputStream extends InputStream {
   private ByteArrayInputStream byteBuf;
   private boolean eof = false;
   private boolean init = false;
   private DataInputStream lowerIn;

   public PaddingCipherInputStream(InputStream var1) throws IOException {
      this.lowerIn = new DataInputStream(var1);
   }

   private ByteArrayInputStream getNewBytes() throws IOException {
      byte[] var1;
      try {
         var1 = new byte[this.lowerIn.readInt()];
         this.lowerIn.readFully(var1);
      } catch (EOFException var3) {
         this.eof = true;
         this.byteBuf = null;
         return null;
      }

      if (var1.length == 0) {
         this.byteBuf = null;
         this.eof = true;
         return null;
      } else {
         try {
            this.byteBuf = new ByteArrayInputStream(Encryption.decrypt(var1));
            ByteArrayInputStream var4 = this.byteBuf;
            return var4;
         } catch (Exception var2) {
            throw new IOException(var2.getMessage());
         }
      }
   }

   private void initRead() throws IOException {
      if (!this.init) {
         int var1 = -1;
         this.init = true;

         label34: {
            int var2;
            try {
               var2 = this.lowerIn.readInt();
            } catch (EOFException var4) {
               this.eof = true;
               break label34;
            }

            var1 = var2;
         }

         if (!this.eof) {
            if (var1 != Encryption.ENCR_INIT_BYTES.length) {
               throw new IOException("Wrong keyphrase!");
            }

            byte[] var3 = new byte[var1];
            this.lowerIn.readFully(var3);

            for(var1 = 0; var1 < var3.length; ++var1) {
               if (var3[var1] != Encryption.ENCR_INIT_BYTES[var1]) {
                  throw new IOException("Wrong keyphrase!");
               }
            }
         }

      }
   }

   public int available() throws IOException {
      return this.byteBuf != null ? this.byteBuf.available() : 0;
   }

   public void close() throws IOException {
      this.lowerIn.close();
   }

   public int read() throws IOException {
      if (!this.init) {
         this.initRead();
      }

      if (this.eof) {
         return -1;
      } else {
         if (this.byteBuf == null || this.byteBuf.available() == 0) {
            this.byteBuf = this.getNewBytes();
            if (this.byteBuf == null) {
               return -1;
            }
         }

         return this.byteBuf.read();
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (!this.init) {
         this.initRead();
      }

      if (this.available() != 0) {
         return this.byteBuf.read(var1, var2, var3);
      } else if (this.eof) {
         return -1;
      } else {
         this.byteBuf = this.getNewBytes();
         return this.eof ? -1 : this.byteBuf.read(var1, var2, var3);
      }
   }
}
