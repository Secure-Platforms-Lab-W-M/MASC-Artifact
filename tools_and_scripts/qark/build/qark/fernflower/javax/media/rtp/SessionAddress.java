package javax.media.rtp;

import java.io.Serializable;
import java.net.InetAddress;

public class SessionAddress implements Serializable {
   public static final int ANY_PORT = -1;
   private InetAddress m_controlAddress;
   private int m_controlPort;
   private InetAddress m_dataAddress;
   private int m_dataPort;
   private int ttl;

   public SessionAddress() {
      this.m_dataPort = -1;
      this.m_controlPort = -1;
   }

   public SessionAddress(InetAddress var1, int var2) {
      this(var1, var2, 0);
   }

   public SessionAddress(InetAddress var1, int var2, int var3) {
      this(var1, var2, var1, var2 + 1);
      this.ttl = var3;
   }

   public SessionAddress(InetAddress var1, int var2, InetAddress var3, int var4) {
      this.m_dataPort = -1;
      this.m_controlPort = -1;
      this.m_dataAddress = var1;
      this.m_dataPort = var2;
      this.m_controlAddress = var3;
      this.m_controlPort = var4;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof SessionAddress)) {
         return false;
      } else {
         SessionAddress var2 = (SessionAddress)var1;
         return this.getControlAddress().equals(var2.getControlAddress()) && this.getDataAddress().equals(var2.getDataAddress()) && this.getControlPort() == var2.getControlPort() && this.getDataPort() == var2.getDataPort();
      }
   }

   public InetAddress getControlAddress() {
      return this.m_controlAddress;
   }

   public String getControlHostAddress() {
      return this.m_controlAddress.getHostAddress();
   }

   public int getControlPort() {
      return this.m_controlPort;
   }

   public InetAddress getDataAddress() {
      return this.m_dataAddress;
   }

   public String getDataHostAddress() {
      return this.m_dataAddress.getHostAddress();
   }

   public int getDataPort() {
      return this.m_dataPort;
   }

   public int getTimeToLive() {
      return this.ttl;
   }

   public int hashCode() {
      return this.getControlAddress().hashCode() + this.getDataAddress().hashCode() + this.getControlPort() + this.getDataPort();
   }

   public void setControlHostAddress(InetAddress var1) {
      this.m_controlAddress = var1;
   }

   public void setControlPort(int var1) {
      this.m_controlPort = var1;
   }

   public void setDataHostAddress(InetAddress var1) {
      this.m_dataAddress = var1;
   }

   public void setDataPort(int var1) {
      this.m_dataPort = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DataAddress: ");
      var1.append(this.m_dataAddress);
      var1.append("\nControlAddress: ");
      var1.append(this.m_controlAddress);
      var1.append("\nDataPort: ");
      var1.append(this.m_dataPort);
      var1.append("\nControlPort: ");
      var1.append(this.m_controlPort);
      return var1.toString();
   }
}
