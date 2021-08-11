package javax.media;

import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

public interface Multiplexer extends PlugIn {
   DataSource getDataOutput();

   Format[] getSupportedInputFormats();

   ContentDescriptor[] getSupportedOutputContentDescriptors(Format[] var1);

   int process(Buffer var1, int var2);

   ContentDescriptor setContentDescriptor(ContentDescriptor var1);

   Format setInputFormat(Format var1, int var2);

   int setNumTracks(int var1);
}
