package javax.media;

public class Buffer {
   public static final int FLAG_BUF_OVERFLOWN = 8192;
   public static final int FLAG_BUF_UNDERFLOWN = 16384;
   public static final int FLAG_DISCARD = 2;
   public static final int FLAG_EOM = 1;
   public static final int FLAG_FLUSH = 512;
   public static final int FLAG_KEY_FRAME = 16;
   public static final int FLAG_LIVE_DATA = 32768;
   public static final int FLAG_NO_DROP = 32;
   public static final int FLAG_NO_SYNC = 96;
   public static final int FLAG_NO_WAIT = 64;
   public static final int FLAG_RELATIVE_TIME = 256;
   public static final int FLAG_RTP_MARKER = 2048;
   public static final int FLAG_RTP_TIME = 4096;
   public static final int FLAG_SID = 8;
   public static final int FLAG_SILENCE = 4;
   public static final int FLAG_SKIP_FEC = 65536;
   public static final int FLAG_SYSTEM_MARKER = 1024;
   public static final int FLAG_SYSTEM_TIME = 128;
   public static final long SEQUENCE_UNKNOWN = 9223372036854775806L;
   public static final long TIME_UNKNOWN = -1L;
   protected Object data = null;
   protected long duration = -1L;
   protected int flags = 0;
   protected Format format = null;
   protected Object header = null;
   private Buffer.RTPHeaderExtension headerExtension = null;
   protected int length = 0;
   protected int offset = 0;
   protected long rtpTimeStamp = -1L;
   protected long sequenceNumber = 9223372036854775806L;
   protected long timeStamp = -1L;

   public Object clone() {
      Buffer var1 = new Buffer();
      Object var2 = this.getData();
      if (var2 != null) {
         if (var2 instanceof byte[]) {
            var1.data = ((byte[])((byte[])var2)).clone();
         } else if (var2 instanceof int[]) {
            var1.data = ((int[])((int[])var2)).clone();
         } else if (var2 instanceof short[]) {
            var1.data = ((short[])((short[])var2)).clone();
         } else {
            var1.data = var2;
         }
      }

      var2 = this.header;
      if (var2 != null) {
         if (var2 instanceof byte[]) {
            var1.header = ((byte[])((byte[])var2)).clone();
         } else if (var2 instanceof int[]) {
            var1.header = ((int[])((int[])var2)).clone();
         } else if (var2 instanceof short[]) {
            var1.header = ((short[])((short[])var2)).clone();
         } else {
            var1.header = var2;
         }
      }

      var1.format = this.format;
      var1.length = this.length;
      var1.offset = this.offset;
      var1.timeStamp = this.timeStamp;
      var1.rtpTimeStamp = this.rtpTimeStamp;
      var1.headerExtension = this.headerExtension;
      var1.duration = this.duration;
      var1.sequenceNumber = this.sequenceNumber;
      var1.flags = this.flags;
      return var1;
   }

   public void copy(Buffer var1) {
      this.copy(var1, false);
   }

   public void copy(Buffer var1, boolean var2) {
      if (var2) {
         Object var3 = this.data;
         this.data = var1.data;
         var1.data = var3;
      } else {
         this.data = var1.data;
      }

      this.header = var1.header;
      this.format = var1.format;
      this.length = var1.length;
      this.offset = var1.offset;
      this.timeStamp = var1.timeStamp;
      this.rtpTimeStamp = var1.rtpTimeStamp;
      this.headerExtension = var1.headerExtension;
      this.duration = var1.duration;
      this.sequenceNumber = var1.sequenceNumber;
      this.flags = var1.flags;
   }

   public Object getData() {
      return this.data;
   }

   public long getDuration() {
      return this.duration;
   }

   public int getFlags() {
      return this.flags;
   }

   public Format getFormat() {
      return this.format;
   }

   public Object getHeader() {
      return this.header;
   }

   public Buffer.RTPHeaderExtension getHeaderExtension() {
      return this.headerExtension;
   }

   public int getLength() {
      return this.length;
   }

   public int getOffset() {
      return this.offset;
   }

   public long getRtpTimeStamp() {
      return this.rtpTimeStamp;
   }

   public long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public long getTimeStamp() {
      return this.timeStamp;
   }

   public boolean isDiscard() {
      return (this.flags & 2) != 0;
   }

   public boolean isEOM() {
      return (this.flags & 1) != 0;
   }

   public void setData(Object var1) {
      this.data = var1;
   }

   public void setDiscard(boolean var1) {
      if (var1) {
         this.flags |= 2;
      } else {
         this.flags &= -3;
      }
   }

   public void setDuration(long var1) {
      this.duration = var1;
   }

   public void setEOM(boolean var1) {
      if (var1) {
         this.flags |= 1;
      } else {
         this.flags &= -2;
      }
   }

   public void setFlags(int var1) {
      this.flags = var1;
   }

   public void setFormat(Format var1) {
      this.format = var1;
   }

   public void setHeader(Object var1) {
      this.header = var1;
   }

   public void setHeaderExtension(Buffer.RTPHeaderExtension var1) {
      this.headerExtension = var1;
   }

   public void setLength(int var1) {
      this.length = var1;
   }

   public void setOffset(int var1) {
      this.offset = var1;
   }

   public void setRtpTimeStamp(long var1) {
      this.rtpTimeStamp = var1;
   }

   public void setSequenceNumber(long var1) {
      this.sequenceNumber = var1;
   }

   public void setTimeStamp(long var1) {
      this.timeStamp = var1;
   }

   public static class RTPHeaderExtension {
      // $FF: renamed from: id byte
      public byte field_172;
      public byte[] value;

      public RTPHeaderExtension(byte var1, byte[] var2) {
         if (var2 != null) {
            this.field_172 = var1;
            this.value = var2;
         } else {
            throw new NullPointerException("value");
         }
      }
   }
}
