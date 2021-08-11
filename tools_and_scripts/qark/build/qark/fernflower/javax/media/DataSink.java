package javax.media;

import java.io.IOException;
import javax.media.datasink.DataSinkListener;

public interface DataSink extends MediaHandler, Controls {
   void addDataSinkListener(DataSinkListener var1);

   void close();

   String getContentType();

   MediaLocator getOutputLocator();

   void open() throws IOException, SecurityException;

   void removeDataSinkListener(DataSinkListener var1);

   void setOutputLocator(MediaLocator var1);

   void start() throws IOException;

   void stop() throws IOException;
}
