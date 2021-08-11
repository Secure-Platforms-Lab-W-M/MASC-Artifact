package javax.media;

public interface Renderer extends PlugIn {
   Format[] getSupportedInputFormats();

   int process(Buffer var1);

   Format setInputFormat(Format var1);

   void start();

   void stop();
}
