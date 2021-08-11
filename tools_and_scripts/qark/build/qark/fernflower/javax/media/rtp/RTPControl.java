package javax.media.rtp;

import javax.media.Control;
import javax.media.Format;

public interface RTPControl extends Control {
   void addFormat(Format var1, int var2);

   Format getFormat();

   Format getFormat(int var1);

   Format[] getFormatList();

   GlobalReceptionStats getGlobalStats();

   ReceptionStats getReceptionStats();
}
