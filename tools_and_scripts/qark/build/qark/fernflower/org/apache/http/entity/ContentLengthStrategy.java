package org.apache.http.entity;

import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

public interface ContentLengthStrategy {
   int CHUNKED = -2;
   int IDENTITY = -1;

   long determineLength(HttpMessage var1) throws HttpException;
}
