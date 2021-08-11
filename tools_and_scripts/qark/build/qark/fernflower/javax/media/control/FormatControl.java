package javax.media.control;

import javax.media.Control;
import javax.media.Format;

public interface FormatControl extends Control {
   Format getFormat();

   Format[] getSupportedFormats();

   boolean isEnabled();

   void setEnabled(boolean var1);

   Format setFormat(Format var1);
}
