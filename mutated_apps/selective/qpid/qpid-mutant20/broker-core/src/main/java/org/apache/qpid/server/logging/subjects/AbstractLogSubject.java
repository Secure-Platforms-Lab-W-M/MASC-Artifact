/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.qpid.server.logging.subjects;

import java.text.MessageFormat;

import org.apache.qpid.server.logging.LogSubject;

/**
 * The LogSubjects all have a similar requirement to format their output and
 * provide the String value.
 *
 * This Abstract LogSubject provides this basic functionality, allowing the
 * actual LogSubjects to provide their formatting and data.
 */
public abstract class AbstractLogSubject implements LogSubject
{
    private String _logString;

    /**
     * Set the toString logging of this LogSubject. Based on a format provided
     * by format and the var args.
     * @param format The Message to format
     * @param args The values to put in to the message.
     */
    protected void setLogStringWithFormat(String format, Object... args)
    {
        _logString = "[" + MessageFormat.format(format, args) + "] ";
    }

    /**
     * toLogString is how the Logging infrastructure will get the text for this
     * LogSubject
     *
     * @return String representing this LogSubject
     */
    @Override
    public String toLogString()
    {
        return _logString;
    }

    /**
     * The logString that will be returned via toLogString
     */
    public String getLogString()
    {
        return _logString;
    }

    public void setLogString(String logString)
    {
        _logString = logString;
    }

    @Override
    public String toString()
    {
        return toLogString();
    }
}
