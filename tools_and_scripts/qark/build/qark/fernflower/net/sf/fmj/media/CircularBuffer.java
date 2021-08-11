package net.sf.fmj.media;

import java.io.PrintStream;
import javax.media.Buffer;

public class CircularBuffer {
   private int availableFramesForReading;
   private int availableFramesForWriting;
   private Buffer[] buf;
   private int head;
   private int lockedFramesForReading;
   private int lockedFramesForWriting;
   private int size;
   private int tail;

   public CircularBuffer(int var1) {
      this.size = var1;
      this.buf = new Buffer[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.buf[var2] = new Buffer();
      }

      this.reset();
   }

   public boolean canRead() {
      synchronized(this){}
      boolean var5 = false;

      int var1;
      try {
         var5 = true;
         var1 = this.availableFramesForReading;
         var5 = false;
      } finally {
         if (var5) {
            ;
         }
      }

      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean canWrite() {
      synchronized(this){}
      boolean var5 = false;

      int var1;
      try {
         var5 = true;
         var1 = this.availableFramesForWriting;
         var5 = false;
      } finally {
         if (var5) {
            ;
         }
      }

      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void error() {
      StringBuilder var1 = new StringBuilder();
      var1.append("CircularQueue failure:\n head=");
      var1.append(this.head);
      var1.append("\n tail=");
      var1.append(this.tail);
      var1.append("\n canRead=");
      var1.append(this.availableFramesForReading);
      var1.append("\n canWrite=");
      var1.append(this.availableFramesForWriting);
      var1.append("\n lockedRead=");
      var1.append(this.lockedFramesForReading);
      var1.append("\n lockedWrite=");
      var1.append(this.lockedFramesForWriting);
      throw new RuntimeException(var1.toString());
   }

   public Buffer getEmptyBuffer() {
      synchronized(this){}

      Buffer var2;
      try {
         if (this.availableFramesForWriting == 0) {
            this.error();
         }

         ++this.lockedFramesForWriting;
         var2 = this.buf[this.tail];
         --this.availableFramesForWriting;
         int var1 = this.tail + 1;
         this.tail = var1;
         if (var1 >= this.size) {
            this.tail = var1 - this.size;
         }
      } finally {
         ;
      }

      return var2;
   }

   public boolean lockedRead() {
      synchronized(this){}
      boolean var5 = false;

      int var1;
      try {
         var5 = true;
         var1 = this.lockedFramesForReading;
         var5 = false;
      } finally {
         if (var5) {
            ;
         }
      }

      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean lockedWrite() {
      synchronized(this){}
      boolean var5 = false;

      int var1;
      try {
         var5 = true;
         var1 = this.lockedFramesForWriting;
         var5 = false;
      } finally {
         if (var5) {
            ;
         }
      }

      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public Buffer peek() {
      synchronized(this){}

      Buffer var1;
      try {
         if (this.availableFramesForReading == 0) {
            this.error();
         }

         var1 = this.buf[this.head];
      } finally {
         ;
      }

      return var1;
   }

   public void print() {
      PrintStream var1 = System.err;
      StringBuilder var2 = new StringBuilder();
      var2.append("CircularQueue : head=");
      var2.append(this.head);
      var2.append(" tail=");
      var2.append(this.tail);
      var2.append(" canRead=");
      var2.append(this.availableFramesForReading);
      var2.append(" canWrite=");
      var2.append(this.availableFramesForWriting);
      var2.append(" lockedRead=");
      var2.append(this.lockedFramesForReading);
      var2.append(" lockedWrite=");
      var2.append(this.lockedFramesForWriting);
      var1.println(var2.toString());
   }

   public Buffer read() {
      synchronized(this){}

      Buffer var2;
      try {
         if (this.availableFramesForReading == 0) {
            this.error();
         }

         var2 = this.buf[this.head];
         ++this.lockedFramesForReading;
         --this.availableFramesForReading;
         int var1 = this.head + 1;
         this.head = var1;
         if (var1 >= this.size) {
            this.head = var1 - this.size;
         }
      } finally {
         ;
      }

      return var2;
   }

   public void readReport() {
      synchronized(this){}

      try {
         if (this.lockedFramesForReading == 0) {
            this.error();
         }

         --this.lockedFramesForReading;
         ++this.availableFramesForWriting;
      } finally {
         ;
      }

   }

   public void reset() {
      synchronized(this){}

      try {
         this.availableFramesForReading = 0;
         this.availableFramesForWriting = this.size;
         this.lockedFramesForReading = 0;
         this.lockedFramesForWriting = 0;
         this.head = 0;
         this.tail = 0;
      } finally {
         ;
      }

   }

   public void writeReport() {
      synchronized(this){}

      try {
         if (this.lockedFramesForWriting == 0) {
            this.error();
         }

         --this.lockedFramesForWriting;
         ++this.availableFramesForReading;
      } finally {
         ;
      }

   }
}
