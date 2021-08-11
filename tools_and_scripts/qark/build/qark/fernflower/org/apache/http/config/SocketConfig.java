package org.apache.http.config;

import org.apache.http.util.Args;

public class SocketConfig implements Cloneable {
   public static final SocketConfig DEFAULT = (new SocketConfig.Builder()).build();
   private final int backlogSize;
   private final int rcvBufSize;
   private final int sndBufSize;
   private final boolean soKeepAlive;
   private final int soLinger;
   private final boolean soReuseAddress;
   private final int soTimeout;
   private final boolean tcpNoDelay;

   SocketConfig(int var1, boolean var2, int var3, boolean var4, boolean var5, int var6, int var7, int var8) {
      this.soTimeout = var1;
      this.soReuseAddress = var2;
      this.soLinger = var3;
      this.soKeepAlive = var4;
      this.tcpNoDelay = var5;
      this.sndBufSize = var6;
      this.rcvBufSize = var7;
      this.backlogSize = var8;
   }

   public static SocketConfig.Builder copy(SocketConfig var0) {
      Args.notNull(var0, "Socket config");
      return (new SocketConfig.Builder()).setSoTimeout(var0.getSoTimeout()).setSoReuseAddress(var0.isSoReuseAddress()).setSoLinger(var0.getSoLinger()).setSoKeepAlive(var0.isSoKeepAlive()).setTcpNoDelay(var0.isTcpNoDelay()).setSndBufSize(var0.getSndBufSize()).setRcvBufSize(var0.getRcvBufSize()).setBacklogSize(var0.getBacklogSize());
   }

   public static SocketConfig.Builder custom() {
      return new SocketConfig.Builder();
   }

   protected SocketConfig clone() throws CloneNotSupportedException {
      return (SocketConfig)super.clone();
   }

   public int getBacklogSize() {
      return this.backlogSize;
   }

   public int getRcvBufSize() {
      return this.rcvBufSize;
   }

   public int getSndBufSize() {
      return this.sndBufSize;
   }

   public int getSoLinger() {
      return this.soLinger;
   }

   public int getSoTimeout() {
      return this.soTimeout;
   }

   public boolean isSoKeepAlive() {
      return this.soKeepAlive;
   }

   public boolean isSoReuseAddress() {
      return this.soReuseAddress;
   }

   public boolean isTcpNoDelay() {
      return this.tcpNoDelay;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[soTimeout=");
      var1.append(this.soTimeout);
      var1.append(", soReuseAddress=");
      var1.append(this.soReuseAddress);
      var1.append(", soLinger=");
      var1.append(this.soLinger);
      var1.append(", soKeepAlive=");
      var1.append(this.soKeepAlive);
      var1.append(", tcpNoDelay=");
      var1.append(this.tcpNoDelay);
      var1.append(", sndBufSize=");
      var1.append(this.sndBufSize);
      var1.append(", rcvBufSize=");
      var1.append(this.rcvBufSize);
      var1.append(", backlogSize=");
      var1.append(this.backlogSize);
      var1.append("]");
      return var1.toString();
   }

   public static class Builder {
      private int backlogSize;
      private int rcvBufSize;
      private int sndBufSize;
      private boolean soKeepAlive;
      private int soLinger = -1;
      private boolean soReuseAddress;
      private int soTimeout;
      private boolean tcpNoDelay = true;

      Builder() {
      }

      public SocketConfig build() {
         return new SocketConfig(this.soTimeout, this.soReuseAddress, this.soLinger, this.soKeepAlive, this.tcpNoDelay, this.sndBufSize, this.rcvBufSize, this.backlogSize);
      }

      public SocketConfig.Builder setBacklogSize(int var1) {
         this.backlogSize = var1;
         return this;
      }

      public SocketConfig.Builder setRcvBufSize(int var1) {
         this.rcvBufSize = var1;
         return this;
      }

      public SocketConfig.Builder setSndBufSize(int var1) {
         this.sndBufSize = var1;
         return this;
      }

      public SocketConfig.Builder setSoKeepAlive(boolean var1) {
         this.soKeepAlive = var1;
         return this;
      }

      public SocketConfig.Builder setSoLinger(int var1) {
         this.soLinger = var1;
         return this;
      }

      public SocketConfig.Builder setSoReuseAddress(boolean var1) {
         this.soReuseAddress = var1;
         return this;
      }

      public SocketConfig.Builder setSoTimeout(int var1) {
         this.soTimeout = var1;
         return this;
      }

      public SocketConfig.Builder setTcpNoDelay(boolean var1) {
         this.tcpNoDelay = var1;
         return this;
      }
   }
}
