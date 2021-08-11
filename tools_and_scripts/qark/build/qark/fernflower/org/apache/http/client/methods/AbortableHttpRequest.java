package org.apache.http.client.methods;

import java.io.IOException;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionReleaseTrigger;

@Deprecated
public interface AbortableHttpRequest {
   void abort();

   void setConnectionRequest(ClientConnectionRequest var1) throws IOException;

   void setReleaseTrigger(ConnectionReleaseTrigger var1) throws IOException;
}
