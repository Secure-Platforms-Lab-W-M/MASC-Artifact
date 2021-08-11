package net.sf.fmj.media;

import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.Processor;
import javax.media.control.TrackControl;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

public abstract class BasicProcessor extends BasicPlayer implements Processor {
   static String NOT_CONFIGURED_ERROR = "cannot be called before the Processor is configured";

   public ContentDescriptor getContentDescriptor() throws NotConfiguredError {
      if (this.getState() >= 180) {
         return null;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("getContentDescriptor ");
         var1.append(NOT_CONFIGURED_ERROR);
         throw new NotConfiguredError(var1.toString());
      }
   }

   public DataSource getDataOutput() throws NotRealizedError {
      if (this.getState() >= 300) {
         return null;
      } else {
         throw new NotRealizedError("getDataOutput cannot be called before the Processor is realized");
      }
   }

   public ContentDescriptor[] getSupportedContentDescriptors() throws NotConfiguredError {
      if (this.getState() >= 180) {
         return new ContentDescriptor[0];
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("getSupportedContentDescriptors ");
         var1.append(NOT_CONFIGURED_ERROR);
         throw new NotConfiguredError(var1.toString());
      }
   }

   public TrackControl[] getTrackControls() throws NotConfiguredError {
      if (this.getState() >= 180) {
         return new TrackControl[0];
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("getTrackControls ");
         var1.append(NOT_CONFIGURED_ERROR);
         throw new NotConfiguredError(var1.toString());
      }
   }

   protected boolean isConfigurable() {
      return true;
   }

   public ContentDescriptor setContentDescriptor(ContentDescriptor var1) throws NotConfiguredError {
      if (this.getState() >= 180) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("setContentDescriptor ");
         var2.append(NOT_CONFIGURED_ERROR);
         throw new NotConfiguredError(var2.toString());
      }
   }
}
