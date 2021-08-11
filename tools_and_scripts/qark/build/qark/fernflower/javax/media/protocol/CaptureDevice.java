package javax.media.protocol;

import java.io.IOException;
import javax.media.CaptureDeviceInfo;
import javax.media.control.FormatControl;

public interface CaptureDevice {
   void connect() throws IOException;

   void disconnect();

   CaptureDeviceInfo getCaptureDeviceInfo();

   FormatControl[] getFormatControls();

   void start() throws IOException;

   void stop() throws IOException;
}
