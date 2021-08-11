package javax.media;

public interface PlugIn extends Controls {
   int BUFFER_PROCESSED_FAILED = 1;
   int BUFFER_PROCESSED_OK = 0;
   int INPUT_BUFFER_NOT_CONSUMED = 2;
   int OUTPUT_BUFFER_NOT_FILLED = 4;
   int PLUGIN_TERMINATED = 8;

   void close();

   String getName();

   void open() throws ResourceUnavailableException;

   void reset();
}
