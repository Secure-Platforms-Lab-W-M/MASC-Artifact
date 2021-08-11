package util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PaddingCipherOutputStream extends OutputStream {
   private int bufSize;
   private ByteArrayOutputStream bytesBuffer;
   private DataOutputStream lowerOut;
   private OutputStream underlying;

   public PaddingCipherOutputStream(OutputStream var1, int var2) throws IOException {
      this.bufSize = var2;
      this.underlying = var1;
      this.bytesBuffer = new ByteArrayOutputStream(1024);
   }

   private void init() throws IOException {
      if (this.lowerOut == null) {
         this.lowerOut = new DataOutputStream(this.underlying);
         this.lowerOut.writeInt(Encryption.ENCR_INIT_BYTES.length);
         this.lowerOut.write(Encryption.ENCR_INIT_BYTES);
      }
   }

   public void close() throws IOException {
      this.init();
      this.writeNext();
      this.lowerOut.flush();
      this.lowerOut.close();
   }

   public void flush() throws IOException {
      this.init();
      this.writeNext();
      this.lowerOut.flush();
   }

   public void write(int var1) throws IOException {
      this.init();
      this.bytesBuffer.write(var1);
      this.bytesBuffer.flush();
      if (this.bytesBuffer.size() >= this.bufSize) {
         this.writeNext();
      }

   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.init();
      this.bytesBuffer.write(var1, var2, var3);
      this.bytesBuffer.flush();
      if (this.bytesBuffer.size() >= this.bufSize) {
         this.writeNext();
      }

   }

   public void writeNext() throws IOException {
      this.bytesBuffer.flush();
      if (this.bytesBuffer.size() != 0) {
         byte[] var1 = this.bytesBuffer.toByteArray();
         this.bytesBuffer.reset();

         try {
            var1 = Encryption.encrypt(var1);
            this.lowerOut.writeInt(var1.length);
            this.lowerOut.write(var1);
         } catch (IOException var2) {
            throw var2;
         } catch (Exception var3) {
            throw new IOException(var3.getMessage());
         }
      }
   }
}
