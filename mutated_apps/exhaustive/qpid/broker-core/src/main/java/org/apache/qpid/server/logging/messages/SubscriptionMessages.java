/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 *
 */
package org.apache.qpid.server.logging.messages;

import static org.apache.qpid.server.logging.AbstractMessageLogger.DEFAULT_LOG_HIERARCHY_PREFIX;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;

import org.apache.qpid.server.logging.LogMessage;

/**
 * DO NOT EDIT DIRECTLY, THIS FILE WAS GENERATED.
 *
 * Generated using GenerateLogMessages and LogMessages.vm
 * This file is based on the content of Subscription_logmessages.properties
 *
 * To regenerate, use Maven lifecycle generates-sources with -Dgenerate=true
 */
public class SubscriptionMessages
{
    private static ResourceBundle _messages;
    private static Locale _currentLocale;

    static
    {
        String cipherName15216 =  "DES";
		try{
			System.out.println("cipherName-15216" + javax.crypto.Cipher.getInstance(cipherName15216).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Locale locale = Locale.US;
        String localeSetting = System.getProperty("qpid.broker_locale");
        if (localeSetting != null)
        {
            String cipherName15217 =  "DES";
			try{
				System.out.println("cipherName-15217" + javax.crypto.Cipher.getInstance(cipherName15217).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String[] localeParts = localeSetting.split("_");
            String language = (localeParts.length > 0 ? localeParts[0] : "");
            String country = (localeParts.length > 1 ? localeParts[1] : "");
            String variant = "";
            if (localeParts.length > 2)
            {
                String cipherName15218 =  "DES";
				try{
					System.out.println("cipherName-15218" + javax.crypto.Cipher.getInstance(cipherName15218).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				variant = localeSetting.substring(language.length() + 1 + country.length() + 1);
            }
            locale = new Locale(language, country, variant);
        }
        _currentLocale = locale;
    }

    public static final String SUBSCRIPTION_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "subscription";
    public static final String CLOSE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "subscription.close";
    public static final String CREATE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "subscription.create";
    public static final String OPERATION_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "subscription.operation";
    public static final String STATE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "subscription.state";

    static
    {
        String cipherName15219 =  "DES";
		try{
			System.out.println("cipherName-15219" + javax.crypto.Cipher.getInstance(cipherName15219).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LoggerFactory.getLogger(SUBSCRIPTION_LOG_HIERARCHY);
        LoggerFactory.getLogger(CLOSE_LOG_HIERARCHY);
        LoggerFactory.getLogger(CREATE_LOG_HIERARCHY);
        LoggerFactory.getLogger(OPERATION_LOG_HIERARCHY);
        LoggerFactory.getLogger(STATE_LOG_HIERARCHY);

        _messages = ResourceBundle.getBundle("org.apache.qpid.server.logging.messages.Subscription_logmessages", _currentLocale);
    }

    /**
     * Log a Subscription message of the Format:
     * <pre>SUB-1002 : Close</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CLOSE()
    {
        String cipherName15220 =  "DES";
		try{
			System.out.println("cipherName-15220" + javax.crypto.Cipher.getInstance(cipherName15220).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("CLOSE");

        final String message = rawMessage;

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15221 =  "DES";
				try{
					System.out.println("cipherName-15221" + javax.crypto.Cipher.getInstance(cipherName15221).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15222 =  "DES";
				try{
					System.out.println("cipherName-15222" + javax.crypto.Cipher.getInstance(cipherName15222).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return CLOSE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15223 =  "DES";
				try{
					System.out.println("cipherName-15223" + javax.crypto.Cipher.getInstance(cipherName15223).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15224 =  "DES";
					try{
						System.out.println("cipherName-15224" + javax.crypto.Cipher.getInstance(cipherName15224).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15225 =  "DES";
					try{
						System.out.println("cipherName-15225" + javax.crypto.Cipher.getInstance(cipherName15225).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }

                final LogMessage that = (LogMessage) o;

                return getLogHierarchy().equals(that.getLogHierarchy()) && toString().equals(that.toString());

            }

            @Override
            public int hashCode()
            {
                String cipherName15226 =  "DES";
				try{
					System.out.println("cipherName-15226" + javax.crypto.Cipher.getInstance(cipherName15226).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a Subscription message of the Format:
     * <pre>SUB-1001 : Create[ : Durable][ : Arguments : {0}]</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CREATE(String param1, boolean opt1, boolean opt2)
    {
        String cipherName15227 =  "DES";
		try{
			System.out.println("cipherName-15227" + javax.crypto.Cipher.getInstance(cipherName15227).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("CREATE");
        StringBuffer msg = new StringBuffer();

        // Split the formatted message up on the option values so we can
        // rebuild the message based on the configured options.
        String[] parts = rawMessage.split("\\[");
        msg.append(parts[0]);

        int end;
        if (parts.length > 1)
        {

            String cipherName15228 =  "DES";
			try{
				System.out.println("cipherName-15228" + javax.crypto.Cipher.getInstance(cipherName15228).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Add Option : : Durable.
            end = parts[1].indexOf(']');
            if (opt1)
            {
                String cipherName15229 =  "DES";
				try{
					System.out.println("cipherName-15229" + javax.crypto.Cipher.getInstance(cipherName15229).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				msg.append(parts[1].substring(0, end));
            }

            // Use 'end + 1' to remove the ']' from the output
            msg.append(parts[1].substring(end + 1));

            // Add Option : : Arguments : {0}.
            end = parts[2].indexOf(']');
            if (opt2)
            {
                String cipherName15230 =  "DES";
				try{
					System.out.println("cipherName-15230" + javax.crypto.Cipher.getInstance(cipherName15230).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				msg.append(parts[2].substring(0, end));
            }

            // Use 'end + 1' to remove the ']' from the output
            msg.append(parts[2].substring(end + 1));
        }

        rawMessage = msg.toString();

        final Object[] messageArguments = {param1};
        // Create a new MessageFormat to ensure thread safety.
        // Sharing a MessageFormat and using applyPattern is not thread safe
        MessageFormat formatter = new MessageFormat(rawMessage, _currentLocale);

        final String message = formatter.format(messageArguments);

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15231 =  "DES";
				try{
					System.out.println("cipherName-15231" + javax.crypto.Cipher.getInstance(cipherName15231).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15232 =  "DES";
				try{
					System.out.println("cipherName-15232" + javax.crypto.Cipher.getInstance(cipherName15232).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return CREATE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15233 =  "DES";
				try{
					System.out.println("cipherName-15233" + javax.crypto.Cipher.getInstance(cipherName15233).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15234 =  "DES";
					try{
						System.out.println("cipherName-15234" + javax.crypto.Cipher.getInstance(cipherName15234).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15235 =  "DES";
					try{
						System.out.println("cipherName-15235" + javax.crypto.Cipher.getInstance(cipherName15235).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }

                final LogMessage that = (LogMessage) o;

                return getLogHierarchy().equals(that.getLogHierarchy()) && toString().equals(that.toString());

            }

            @Override
            public int hashCode()
            {
                String cipherName15236 =  "DES";
				try{
					System.out.println("cipherName-15236" + javax.crypto.Cipher.getInstance(cipherName15236).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a Subscription message of the Format:
     * <pre>SUB-1004 : Operation : {0}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage OPERATION(String param1)
    {
        String cipherName15237 =  "DES";
		try{
			System.out.println("cipherName-15237" + javax.crypto.Cipher.getInstance(cipherName15237).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("OPERATION");

        final Object[] messageArguments = {param1};
        // Create a new MessageFormat to ensure thread safety.
        // Sharing a MessageFormat and using applyPattern is not thread safe
        MessageFormat formatter = new MessageFormat(rawMessage, _currentLocale);

        final String message = formatter.format(messageArguments);

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15238 =  "DES";
				try{
					System.out.println("cipherName-15238" + javax.crypto.Cipher.getInstance(cipherName15238).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15239 =  "DES";
				try{
					System.out.println("cipherName-15239" + javax.crypto.Cipher.getInstance(cipherName15239).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return OPERATION_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15240 =  "DES";
				try{
					System.out.println("cipherName-15240" + javax.crypto.Cipher.getInstance(cipherName15240).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15241 =  "DES";
					try{
						System.out.println("cipherName-15241" + javax.crypto.Cipher.getInstance(cipherName15241).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15242 =  "DES";
					try{
						System.out.println("cipherName-15242" + javax.crypto.Cipher.getInstance(cipherName15242).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }

                final LogMessage that = (LogMessage) o;

                return getLogHierarchy().equals(that.getLogHierarchy()) && toString().equals(that.toString());

            }

            @Override
            public int hashCode()
            {
                String cipherName15243 =  "DES";
				try{
					System.out.println("cipherName-15243" + javax.crypto.Cipher.getInstance(cipherName15243).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a Subscription message of the Format:
     * <pre>SUB-1003 : Suspended for {0,number} ms</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage STATE(Number param1)
    {
        String cipherName15244 =  "DES";
		try{
			System.out.println("cipherName-15244" + javax.crypto.Cipher.getInstance(cipherName15244).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("STATE");

        final Object[] messageArguments = {param1};
        // Create a new MessageFormat to ensure thread safety.
        // Sharing a MessageFormat and using applyPattern is not thread safe
        MessageFormat formatter = new MessageFormat(rawMessage, _currentLocale);

        final String message = formatter.format(messageArguments);

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15245 =  "DES";
				try{
					System.out.println("cipherName-15245" + javax.crypto.Cipher.getInstance(cipherName15245).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15246 =  "DES";
				try{
					System.out.println("cipherName-15246" + javax.crypto.Cipher.getInstance(cipherName15246).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return STATE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15247 =  "DES";
				try{
					System.out.println("cipherName-15247" + javax.crypto.Cipher.getInstance(cipherName15247).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15248 =  "DES";
					try{
						System.out.println("cipherName-15248" + javax.crypto.Cipher.getInstance(cipherName15248).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15249 =  "DES";
					try{
						System.out.println("cipherName-15249" + javax.crypto.Cipher.getInstance(cipherName15249).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }

                final LogMessage that = (LogMessage) o;

                return getLogHierarchy().equals(that.getLogHierarchy()) && toString().equals(that.toString());

            }

            @Override
            public int hashCode()
            {
                String cipherName15250 =  "DES";
				try{
					System.out.println("cipherName-15250" + javax.crypto.Cipher.getInstance(cipherName15250).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }


    private SubscriptionMessages()
    {
		String cipherName15251 =  "DES";
		try{
			System.out.println("cipherName-15251" + javax.crypto.Cipher.getInstance(cipherName15251).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

}
