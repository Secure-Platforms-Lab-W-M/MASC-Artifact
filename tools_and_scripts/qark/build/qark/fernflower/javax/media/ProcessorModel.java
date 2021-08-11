package javax.media;

import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

public class ProcessorModel {
   private Format[] formats;
   private DataSource inputDataSource;
   private MediaLocator inputLocator;
   private ContentDescriptor outputContentDescriptor;

   public ProcessorModel() {
   }

   public ProcessorModel(MediaLocator var1, Format[] var2, ContentDescriptor var3) {
      this.inputLocator = var1;
      this.formats = var2;
      this.outputContentDescriptor = var3;
   }

   public ProcessorModel(DataSource var1, Format[] var2, ContentDescriptor var3) {
      this.inputDataSource = var1;
      this.formats = var2;
      this.outputContentDescriptor = var3;
   }

   public ProcessorModel(Format[] var1, ContentDescriptor var2) {
      this.formats = var1;
      this.outputContentDescriptor = var2;
   }

   public ContentDescriptor getContentDescriptor() {
      return this.outputContentDescriptor;
   }

   public DataSource getInputDataSource() {
      return this.inputDataSource;
   }

   public MediaLocator getInputLocator() {
      return this.inputLocator;
   }

   public Format getOutputTrackFormat(int var1) {
      Format[] var2 = this.formats;
      if (var2 == null) {
         return null;
      } else if (var1 >= 0) {
         return var1 >= var2.length ? null : var2[var1];
      } else {
         return null;
      }
   }

   public int getTrackCount(int var1) {
      Format[] var2 = this.formats;
      return var2 == null ? -1 : var2.length;
   }

   public boolean isFormatAcceptable(int var1, Format var2) {
      Format[] var3 = this.formats;
      if (var3 == null) {
         return true;
      } else if (var1 >= 0) {
         return var1 >= var3.length ? true : var2.matches(var3[var1]);
      } else {
         return true;
      }
   }
}
