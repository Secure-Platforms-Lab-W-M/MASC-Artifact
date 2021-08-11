package util;

import java.io.IOException;
import java.io.InputStream;

public class ExecutionEnvironment implements ExecutionEnvironmentInterface {
   private static ExecutionEnvironmentInterface m_Env;
   private static ExecutionEnvironmentInterface m_default = new ExecutionEnvironment();

   public static ExecutionEnvironmentInterface getEnvironment() {
      return m_Env != null ? m_Env : m_default;
   }

   public static void setEnvironment(ExecutionEnvironmentInterface var0) {
      m_Env = var0;
   }

   public boolean debug() {
      return false;
   }

   public InputStream getAsset(String var1) throws IOException {
      throw new IOException("Not supported!");
   }

   public String getWorkDir() {
      return "./";
   }

   public boolean hasNetwork() {
      return true;
   }

   public void onReload() throws IOException {
   }

   public void releaseAllWakeLocks() {
   }

   public void releaseWakeLock() {
   }

   public void wakeLock() {
   }
}
