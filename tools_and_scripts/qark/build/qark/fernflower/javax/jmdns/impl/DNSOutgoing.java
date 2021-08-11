package javax.jmdns.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class DNSOutgoing extends DNSMessage {
   private static final int HEADER_SIZE = 12;
   public static boolean USE_DOMAIN_NAME_COMPRESSION = true;
   private final DNSOutgoing.MessageOutputStream _additionalsAnswersBytes;
   private final DNSOutgoing.MessageOutputStream _answersBytes;
   private final DNSOutgoing.MessageOutputStream _authoritativeAnswersBytes;
   private InetSocketAddress _destination;
   private int _maxUDPPayload;
   Map _names;
   private final DNSOutgoing.MessageOutputStream _questionsBytes;

   public DNSOutgoing(int var1) {
      this(var1, true, 1460);
   }

   public DNSOutgoing(int var1, boolean var2) {
      this(var1, var2, 1460);
   }

   public DNSOutgoing(int var1, boolean var2, int var3) {
      super(var1, 0, var2);
      this._names = new HashMap();
      if (var3 > 0) {
         var1 = var3;
      } else {
         var1 = 1460;
      }

      this._maxUDPPayload = var1;
      this._questionsBytes = new DNSOutgoing.MessageOutputStream(var3, this);
      this._answersBytes = new DNSOutgoing.MessageOutputStream(var3, this);
      this._authoritativeAnswersBytes = new DNSOutgoing.MessageOutputStream(var3, this);
      this._additionalsAnswersBytes = new DNSOutgoing.MessageOutputStream(var3, this);
   }

   public void addAdditionalAnswer(DNSIncoming var1, DNSRecord var2) throws IOException {
      DNSOutgoing.MessageOutputStream var4 = new DNSOutgoing.MessageOutputStream(512, this);
      var4.writeRecord(var2, 0L);
      byte[] var3 = var4.toByteArray();
      var4.close();
      if (var3.length < this.availableSpace()) {
         this._additionals.add(var2);
         this._additionalsAnswersBytes.write(var3, 0, var3.length);
      } else {
         throw new IOException("message full");
      }
   }

   public void addAnswer(DNSIncoming var1, DNSRecord var2) throws IOException {
      if (var1 == null || !var2.suppressedBy(var1)) {
         this.addAnswer(var2, 0L);
      }

   }

   public void addAnswer(DNSRecord var1, long var2) throws IOException {
      if (var1 != null && (var2 == 0L || !var1.isExpired(var2))) {
         DNSOutgoing.MessageOutputStream var4 = new DNSOutgoing.MessageOutputStream(512, this);
         var4.writeRecord(var1, var2);
         byte[] var5 = var4.toByteArray();
         var4.close();
         if (var5.length < this.availableSpace()) {
            this._answers.add(var1);
            this._answersBytes.write(var5, 0, var5.length);
         } else {
            throw new IOException("message full");
         }
      }
   }

   public void addAuthorativeAnswer(DNSRecord var1) throws IOException {
      DNSOutgoing.MessageOutputStream var2 = new DNSOutgoing.MessageOutputStream(512, this);
      var2.writeRecord(var1, 0L);
      byte[] var3 = var2.toByteArray();
      var2.close();
      if (var3.length < this.availableSpace()) {
         this._authoritativeAnswers.add(var1);
         this._authoritativeAnswersBytes.write(var3, 0, var3.length);
      } else {
         throw new IOException("message full");
      }
   }

   public void addQuestion(DNSQuestion var1) throws IOException {
      DNSOutgoing.MessageOutputStream var2 = new DNSOutgoing.MessageOutputStream(512, this);
      var2.writeQuestion(var1);
      byte[] var3 = var2.toByteArray();
      var2.close();
      if (var3.length < this.availableSpace()) {
         this._questions.add(var1);
         this._questionsBytes.write(var3, 0, var3.length);
      } else {
         throw new IOException("message full");
      }
   }

   public int availableSpace() {
      return this._maxUDPPayload - 12 - this._questionsBytes.size() - this._answersBytes.size() - this._authoritativeAnswersBytes.size() - this._additionalsAnswersBytes.size();
   }

   public byte[] data() {
      long var2 = System.currentTimeMillis();
      this._names.clear();
      DNSOutgoing.MessageOutputStream var4 = new DNSOutgoing.MessageOutputStream(this._maxUDPPayload, this);
      int var1;
      if (this._multicast) {
         var1 = 0;
      } else {
         var1 = this.getId();
      }

      var4.writeShort(var1);
      var4.writeShort(this.getFlags());
      var4.writeShort(this.getNumberOfQuestions());
      var4.writeShort(this.getNumberOfAnswers());
      var4.writeShort(this.getNumberOfAuthorities());
      var4.writeShort(this.getNumberOfAdditionals());
      Iterator var5 = this._questions.iterator();

      while(var5.hasNext()) {
         var4.writeQuestion((DNSQuestion)var5.next());
      }

      var5 = this._answers.iterator();

      while(var5.hasNext()) {
         var4.writeRecord((DNSRecord)var5.next(), var2);
      }

      var5 = this._authoritativeAnswers.iterator();

      while(var5.hasNext()) {
         var4.writeRecord((DNSRecord)var5.next(), var2);
      }

      var5 = this._additionals.iterator();

      while(var5.hasNext()) {
         var4.writeRecord((DNSRecord)var5.next(), var2);
      }

      byte[] var7 = var4.toByteArray();

      try {
         var4.close();
         return var7;
      } catch (IOException var6) {
         return var7;
      }
   }

   public InetSocketAddress getDestination() {
      return this._destination;
   }

   public int getMaxUDPPayload() {
      return this._maxUDPPayload;
   }

   String print(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.print());
      if (var1) {
         var2.append(this.print(this.data()));
      }

      return var2.toString();
   }

   public void setDestination(InetSocketAddress var1) {
      this._destination = var1;
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      String var1;
      if (this.isQuery()) {
         var1 = "dns[query:";
      } else {
         var1 = "dns[response:";
      }

      var2.append(var1);
      var2.append(" id=0x");
      var2.append(Integer.toHexString(this.getId()));
      if (this.getFlags() != 0) {
         var2.append(", flags=0x");
         var2.append(Integer.toHexString(this.getFlags()));
         if (this.isResponse()) {
            var2.append(":r");
         }

         if (this.isAuthoritativeAnswer()) {
            var2.append(":aa");
         }

         if (this.isTruncated()) {
            var2.append(":tc");
         }
      }

      if (this.getNumberOfQuestions() > 0) {
         var2.append(", questions=");
         var2.append(this.getNumberOfQuestions());
      }

      if (this.getNumberOfAnswers() > 0) {
         var2.append(", answers=");
         var2.append(this.getNumberOfAnswers());
      }

      if (this.getNumberOfAuthorities() > 0) {
         var2.append(", authorities=");
         var2.append(this.getNumberOfAuthorities());
      }

      if (this.getNumberOfAdditionals() > 0) {
         var2.append(", additionals=");
         var2.append(this.getNumberOfAdditionals());
      }

      Iterator var4;
      if (this.getNumberOfQuestions() > 0) {
         var2.append("\nquestions:");
         var4 = this._questions.iterator();

         while(var4.hasNext()) {
            DNSQuestion var3 = (DNSQuestion)var4.next();
            var2.append("\n\t");
            var2.append(var3);
         }
      }

      DNSRecord var5;
      if (this.getNumberOfAnswers() > 0) {
         var2.append("\nanswers:");
         var4 = this._answers.iterator();

         while(var4.hasNext()) {
            var5 = (DNSRecord)var4.next();
            var2.append("\n\t");
            var2.append(var5);
         }
      }

      if (this.getNumberOfAuthorities() > 0) {
         var2.append("\nauthorities:");
         var4 = this._authoritativeAnswers.iterator();

         while(var4.hasNext()) {
            var5 = (DNSRecord)var4.next();
            var2.append("\n\t");
            var2.append(var5);
         }
      }

      if (this.getNumberOfAdditionals() > 0) {
         var2.append("\nadditionals:");
         var4 = this._additionals.iterator();

         while(var4.hasNext()) {
            var5 = (DNSRecord)var4.next();
            var2.append("\n\t");
            var2.append(var5);
         }
      }

      var2.append("\nnames=");
      var2.append(this._names);
      var2.append("]");
      return var2.toString();
   }

   public static class MessageOutputStream extends ByteArrayOutputStream {
      private final int _offset;
      private final DNSOutgoing _out;

      MessageOutputStream(int var1, DNSOutgoing var2) {
         this(var1, var2, 0);
      }

      MessageOutputStream(int var1, DNSOutgoing var2, int var3) {
         super(var1);
         this._out = var2;
         this._offset = var3;
      }

      void writeByte(int var1) {
         this.write(var1 & 255);
      }

      void writeBytes(String var1, int var2, int var3) {
         for(int var4 = 0; var4 < var3; ++var4) {
            this.writeByte(var1.charAt(var2 + var4));
         }

      }

      void writeBytes(byte[] var1) {
         if (var1 != null) {
            this.writeBytes((byte[])var1, 0, var1.length);
         }

      }

      void writeBytes(byte[] var1, int var2, int var3) {
         for(int var4 = 0; var4 < var3; ++var4) {
            this.writeByte(var1[var2 + var4]);
         }

      }

      void writeInt(int var1) {
         this.writeShort(var1 >> 16);
         this.writeShort(var1);
      }

      void writeName(String var1) {
         this.writeName(var1, true);
      }

      void writeName(String var1, boolean var2) {
         while(true) {
            int var4 = var1.indexOf(46);
            int var3 = var4;
            if (var4 < 0) {
               var3 = var1.length();
            }

            if (var3 <= 0) {
               this.writeByte(0);
               return;
            }

            String var5 = var1.substring(0, var3);
            if (var2 && DNSOutgoing.USE_DOMAIN_NAME_COMPRESSION) {
               Integer var6 = (Integer)this._out._names.get(var1);
               if (var6 != null) {
                  var3 = var6;
                  this.writeByte(var3 >> 8 | 192);
                  this.writeByte(var3 & 255);
                  return;
               }

               this._out._names.put(var1, this.size() + this._offset);
               this.writeUTF(var5, 0, var5.length());
            } else {
               this.writeUTF(var5, 0, var5.length());
            }

            var5 = var1.substring(var3);
            var1 = var5;
            if (var5.startsWith(".")) {
               var1 = var5.substring(1);
            }
         }
      }

      void writeQuestion(DNSQuestion var1) {
         this.writeName(var1.getName());
         this.writeShort(var1.getRecordType().indexValue());
         this.writeShort(var1.getRecordClass().indexValue());
      }

      void writeRecord(DNSRecord var1, long var2) {
         this.writeName(var1.getName());
         this.writeShort(var1.getRecordType().indexValue());
         int var5 = var1.getRecordClass().indexValue();
         char var4;
         if (var1.isUnique() && this._out.isMulticast()) {
            var4 = '耀';
         } else {
            var4 = 0;
         }

         this.writeShort(var5 | var4);
         int var8;
         if (var2 == 0L) {
            var8 = var1.getTTL();
         } else {
            var8 = var1.getRemainingTTL(var2);
         }

         this.writeInt(var8);
         DNSOutgoing.MessageOutputStream var6 = new DNSOutgoing.MessageOutputStream(512, this._out, this._offset + this.size() + 2);
         var1.write(var6);
         byte[] var7 = var6.toByteArray();
         this.writeShort(var7.length);
         this.write(var7, 0, var7.length);
      }

      void writeShort(int var1) {
         this.writeByte(var1 >> 8);
         this.writeByte(var1);
      }

      void writeUTF(String var1, int var2, int var3) {
         int var4 = 0;

         for(int var5 = 0; var5 < var3; ++var5) {
            char var6 = var1.charAt(var2 + var5);
            if (var6 >= 1 && var6 <= 127) {
               ++var4;
            } else if (var6 > 2047) {
               var4 += 3;
            } else {
               var4 += 2;
            }
         }

         this.writeByte(var4);

         for(var4 = 0; var4 < var3; ++var4) {
            char var7 = var1.charAt(var2 + var4);
            if (var7 >= 1 && var7 <= 127) {
               this.writeByte(var7);
            } else if (var7 > 2047) {
               this.writeByte(var7 >> 12 & 15 | 224);
               this.writeByte(var7 >> 6 & 63 | 128);
               this.writeByte(var7 >> 0 & 63 | 128);
            } else {
               this.writeByte(var7 >> 6 & 31 | 192);
               this.writeByte(var7 >> 0 & 63 | 128);
            }
         }

      }
   }
}
