package net.sf.fmj.media.rtp;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.PlugIn;

public interface Depacketizer extends PlugIn {
   int DEPACKETIZER = 6;

   Format[] getSupportedInputFormats();

   Format parse(Buffer var1);

   Format setInputFormat(Format var1);
}
