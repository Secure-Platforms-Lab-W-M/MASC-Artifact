package util;

import java.io.IOException;
import java.io.InputStream;

public interface ExecutionEnvironmentInterface {
   boolean debug();

   InputStream getAsset(String var1) throws IOException;

   String getWorkDir();

   boolean hasNetwork();

   void onReload() throws IOException;

   void releaseAllWakeLocks();

   void releaseWakeLock();

   void wakeLock();
}
