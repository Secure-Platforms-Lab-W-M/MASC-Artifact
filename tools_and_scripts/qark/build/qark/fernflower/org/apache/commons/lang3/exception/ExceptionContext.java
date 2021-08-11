package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;

public interface ExceptionContext {
   ExceptionContext addContextValue(String var1, Object var2);

   List getContextEntries();

   Set getContextLabels();

   List getContextValues(String var1);

   Object getFirstContextValue(String var1);

   String getFormattedExceptionMessage(String var1);

   ExceptionContext setContextValue(String var1, Object var2);
}
