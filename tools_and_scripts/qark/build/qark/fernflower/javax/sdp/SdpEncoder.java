package javax.sdp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public interface SdpEncoder {
   void output(SessionDescription var1, OutputStream var2) throws IOException;

   void setEncoding(String var1) throws UnsupportedEncodingException;

   void setRtpmapAttribute(boolean var1);

   void setTypedTime(boolean var1);
}
