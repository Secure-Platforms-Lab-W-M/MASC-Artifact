package net.sf.fmj.media;

import javax.media.ConfigureCompleteEvent;
import javax.media.NotConfiguredError;
import javax.media.Processor;
import javax.media.control.TrackControl;
import javax.media.protocol.ContentDescriptor;
import net.sf.fmj.ejmf.toolkit.media.AbstractPlayer;

public abstract class AbstractProcessor extends AbstractPlayer implements Processor {
   protected ContentDescriptor outputContentDescriptor;

   public void configure() {
      if (this.getState() >= 180) {
         this.postConfigureCompleteEvent();
      } else {
         if (this.getTargetState() < 180) {
            this.setTargetState(180);
         }

         Thread var1 = new Thread("Processor Configure Thread") {
            public void run() {
               if (AbstractProcessor.this.getState() < 180) {
                  AbstractProcessor.this.synchronousConfigure();
               }

            }
         };
         this.getThreadQueue().addThread(var1);
      }
   }

   public abstract boolean doConfigure();

   public ContentDescriptor getContentDescriptor() throws NotConfiguredError {
      if (this.getState() >= 180) {
         return this.outputContentDescriptor;
      } else {
         throw new NotConfiguredError("Cannot call getContentDescriptor on an unconfigured Processor.");
      }
   }

   public ContentDescriptor[] getSupportedContentDescriptors() throws NotConfiguredError {
      if (this.getState() >= 180) {
         return null;
      } else {
         throw new NotConfiguredError("Cannot call getSupportedContentDescriptors on an unconfigured Processor.");
      }
   }

   public TrackControl[] getTrackControls() throws NotConfiguredError {
      if (this.getState() >= 180) {
         return null;
      } else {
         throw new NotConfiguredError("Cannot call getTrackControls on an unconfigured Processor.");
      }
   }

   protected void postConfigureCompleteEvent() {
      this.postEvent(new ConfigureCompleteEvent(this, this.getPreviousState(), this.getState(), this.getTargetState()));
   }

   public ContentDescriptor setContentDescriptor(ContentDescriptor var1) throws NotConfiguredError {
      this.outputContentDescriptor = var1;
      return var1;
   }

   protected void synchronousConfigure() {
      this.setState(140);
      this.postTransitionEvent();
      if (this.doConfigure()) {
         this.setState(180);
         this.postConfigureCompleteEvent();
      } else {
         this.setState(100);
         this.setTargetState(100);
      }
   }
}
