package javax.media;

public interface Codec extends PlugIn {
   Format[] getSupportedInputFormats();

   Format[] getSupportedOutputFormats(Format var1);

   int process(Buffer var1, Buffer var2);

   Format setInputFormat(Format var1);

   Format setOutputFormat(Format var1);
}
