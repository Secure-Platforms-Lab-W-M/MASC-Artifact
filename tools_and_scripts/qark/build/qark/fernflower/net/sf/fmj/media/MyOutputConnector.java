package net.sf.fmj.media;

import javax.media.Track;

class MyOutputConnector extends BasicOutputConnector {
   protected Track track;

   public MyOutputConnector(Track var1) {
      this.track = var1;
      this.format = var1.getFormat();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append(": ");
      var1.append(this.getFormat());
      return var1.toString();
   }
}
