package javax.jmdns.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class DNSMessage {
   public static final boolean MULTICAST = true;
   public static final boolean UNICAST = false;
   protected final List _additionals;
   protected final List _answers;
   protected final List _authoritativeAnswers;
   private int _flags;
   private int _id;
   boolean _multicast;
   protected final List _questions;

   protected DNSMessage(int var1, int var2, boolean var3) {
      this._flags = var1;
      this._id = var2;
      this._multicast = var3;
      this._questions = Collections.synchronizedList(new LinkedList());
      this._answers = Collections.synchronizedList(new LinkedList());
      this._authoritativeAnswers = Collections.synchronizedList(new LinkedList());
      this._additionals = Collections.synchronizedList(new LinkedList());
   }

   public Collection getAdditionals() {
      return this._additionals;
   }

   public List getAllAnswers() {
      ArrayList var1 = new ArrayList(this._answers.size() + this._authoritativeAnswers.size() + this._additionals.size());
      var1.addAll(this._answers);
      var1.addAll(this._authoritativeAnswers);
      var1.addAll(this._additionals);
      return var1;
   }

   public Collection getAnswers() {
      return this._answers;
   }

   public Collection getAuthorities() {
      return this._authoritativeAnswers;
   }

   public int getFlags() {
      return this._flags;
   }

   public int getId() {
      return this._multicast ? 0 : this._id;
   }

   public int getNumberOfAdditionals() {
      return this.getAdditionals().size();
   }

   public int getNumberOfAnswers() {
      return this.getAnswers().size();
   }

   public int getNumberOfAuthorities() {
      return this.getAuthorities().size();
   }

   public int getNumberOfQuestions() {
      return this.getQuestions().size();
   }

   public int getOperationCode() {
      return (this._flags & 30720) >> 11;
   }

   public Collection getQuestions() {
      return this._questions;
   }

   public boolean isAuthoritativeAnswer() {
      return (this._flags & 1024) != 0;
   }

   public boolean isEmpty() {
      return this.getNumberOfQuestions() + this.getNumberOfAnswers() + this.getNumberOfAuthorities() + this.getNumberOfAdditionals() == 0;
   }

   public boolean isMulticast() {
      return this._multicast;
   }

   public boolean isQuery() {
      return (this._flags & '耀') == 0;
   }

   public boolean isResponse() {
      return (this._flags & '耀') == 32768;
   }

   public boolean isTruncated() {
      return (this._flags & 512) != 0;
   }

   public boolean isValidResponseCode() {
      return (this._flags & 15) == 0;
   }

   String print() {
      StringBuilder var1 = new StringBuilder(200);
      var1.append(this.toString());
      var1.append("\n");
      Iterator var2 = this._questions.iterator();

      while(var2.hasNext()) {
         DNSQuestion var3 = (DNSQuestion)var2.next();
         var1.append("\tquestion:      ");
         var1.append(var3);
         var1.append("\n");
      }

      var2 = this._answers.iterator();

      DNSRecord var4;
      while(var2.hasNext()) {
         var4 = (DNSRecord)var2.next();
         var1.append("\tanswer:        ");
         var1.append(var4);
         var1.append("\n");
      }

      var2 = this._authoritativeAnswers.iterator();

      while(var2.hasNext()) {
         var4 = (DNSRecord)var2.next();
         var1.append("\tauthoritative: ");
         var1.append(var4);
         var1.append("\n");
      }

      var2 = this._additionals.iterator();

      while(var2.hasNext()) {
         var4 = (DNSRecord)var2.next();
         var1.append("\tadditional:    ");
         var1.append(var4);
         var1.append("\n");
      }

      return var1.toString();
   }

   protected String print(byte[] var1) {
      StringBuilder var8 = new StringBuilder(4000);
      int var4 = 0;

      for(int var5 = var1.length; var4 < var5; var4 += 32) {
         int var6 = Math.min(32, var5 - var4);
         if (var4 < 16) {
            var8.append(' ');
         }

         if (var4 < 256) {
            var8.append(' ');
         }

         if (var4 < 4096) {
            var8.append(' ');
         }

         var8.append(Integer.toHexString(var4));
         var8.append(':');

         int var3;
         for(var3 = 0; var3 < var6; ++var3) {
            if (var3 % 8 == 0) {
               var8.append(' ');
            }

            var8.append(Integer.toHexString((var1[var4 + var3] & 240) >> 4));
            var8.append(Integer.toHexString((var1[var4 + var3] & 15) >> 0));
         }

         if (var3 < 32) {
            while(var3 < 32) {
               if (var3 % 8 == 0) {
                  var8.append(' ');
               }

               var8.append("  ");
               ++var3;
            }
         }

         var8.append("    ");

         for(var3 = 0; var3 < var6; ++var3) {
            if (var3 % 8 == 0) {
               var8.append(' ');
            }

            int var7 = var1[var4 + var3] & 255;
            char var2;
            if (var7 > 32 && var7 < 127) {
               var2 = (char)var7;
            } else {
               var2 = '.';
            }

            var8.append(var2);
         }

         var8.append("\n");
         if (var4 + 32 >= 2048) {
            var8.append("....\n");
            break;
         }
      }

      return var8.toString();
   }

   public void setFlags(int var1) {
      this._flags = var1;
   }

   public void setId(int var1) {
      this._id = var1;
   }
}
