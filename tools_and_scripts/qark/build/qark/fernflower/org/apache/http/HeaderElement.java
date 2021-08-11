package org.apache.http;

public interface HeaderElement {
   String getName();

   NameValuePair getParameter(int var1);

   NameValuePair getParameterByName(String var1);

   int getParameterCount();

   NameValuePair[] getParameters();

   String getValue();
}
