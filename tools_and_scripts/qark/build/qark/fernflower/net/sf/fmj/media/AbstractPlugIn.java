package net.sf.fmj.media;

import javax.media.PlugIn;
import javax.media.ResourceUnavailableException;

public abstract class AbstractPlugIn extends AbstractControls implements PlugIn {
   public void close() {
   }

   public String getName() {
      return this.getClass().getSimpleName();
   }

   public void open() throws ResourceUnavailableException {
   }

   public void reset() {
   }
}
