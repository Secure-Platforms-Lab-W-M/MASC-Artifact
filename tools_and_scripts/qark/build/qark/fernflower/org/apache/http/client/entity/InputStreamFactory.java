package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamFactory {
   InputStream create(InputStream var1) throws IOException;
}
