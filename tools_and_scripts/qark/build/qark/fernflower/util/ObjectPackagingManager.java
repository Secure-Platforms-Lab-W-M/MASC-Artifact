package util;

public interface ObjectPackagingManager {
   Object bytesToObject(byte[] var1, int var2);

   int objectSize();

   void objectToBytes(Object var1, byte[] var2, int var3);
}
