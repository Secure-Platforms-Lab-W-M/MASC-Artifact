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
 * This file is based on the content of Exchange_logmessages.properties
 *
 * To regenerate, use Maven lifecycle generates-sources with -Dgenerate=true
 */
public class ExchangeMessages
{
    private static ResourceBundle _messages;
    private static Locale _currentLocale;

    static
    {
        String cipherName14966 =  "DES";
		try{
			System.out.println("cipherName-14966" + javax.crypto.Cipher.getInstance(cipherName14966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Locale locale = Locale.US;
        String localeSetting = System.getProperty("qpid.broker_locale");
        if (localeSetting != null)
        {
            String cipherName14967 =  "DES";
			try{
				System.out.println("cipherName-14967" + javax.crypto.Cipher.getInstance(cipherName14967).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String[] localeParts = localeSetting.split("_");
            String language = (localeParts.length > 0 ? localeParts[0] : "");
            String country = (localeParts.length > 1 ? localeParts[1] : "");
            String variant = "";
            if (localeParts.length > 2)
            {
                String cipherName14968 =  "DES";
				try{
					System.out.println("cipherName-14968" + javax.crypto.Cipher.getInstance(cipherName14968).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				variant = localeSetting.substring(language.length() + 1 + country.length() + 1);
            }
            locale = new Locale(language, country, variant);
        }
        _currentLocale = locale;
    }

    public static final String EXCHANGE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "exchange";
    public static final String CREATED_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "exchange.created";
    public static final String DELETED_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "exchange.deleted";
    public static final String DISCARDMSG_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "exchange.discardmsg";
    public static final String OPERATION_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "exchange.operation";

    static
    {
        String cipherName14969 =  "DES";
		try{
			System.out.println("cipherName-14969" + javax.crypto.Cipher.getInstance(cipherName14969).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LoggerFactory.getLogger(EXCHANGE_LOG_HIERARCHY);
        LoggerFactory.getLogger(CREATED_LOG_HIERARCHY);
        LoggerFactory.getLogger(DELETED_LOG_HIERARCHY);
        LoggerFactory.getLogger(DISCARDMSG_LOG_HIERARCHY);
        LoggerFactory.getLogger(OPERATION_LOG_HIERARCHY);

        _messages = ResourceBundle.getBundle("org.apache.qpid.server.logging.messages.Exchange_logmessages", _currentLocale);
    }

    /**
     * Log a Exchange message of the Format:
     * <pre>EXH-1001 : Create :[ Durable] Type: {0} Name: {1}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CREATED(String param1, String param2, boolean opt1)
    {
        String cipherName14970 =  "DES";
		try{
			System.out.println("cipherName-14970" + javax.crypto.Cipher.getInstance(cipherName14970).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("CREATED");
        StringBuffer msg = new StringBuffer();

        // Split the formatted message up on the option values so we can
        // rebuild the message based on the configured options.
        String[] parts = rawMessage.split("\\[");
        msg.append(parts[0]);

        int end;
        if (parts.length > 1)
        {

            String cipherName14971 =  "DES";
			try{
				System.out.println("cipherName-14971" + javax.crypto.Cipher.getInstance(cipherName14971).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Add Option : Durable.
            end = parts[1].indexOf(']');
            if (opt1)
            {
                String cipherName14972 =  "DES";
				try{
					System.out.println("cipherName-14972" + javax.crypto.Cipher.getInstance(cipherName14972).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				msg.append(parts[1].substring(0, end));
            }

            // Use 'end + 1' to remove the ']' from the output
            msg.append(parts[1].substring(end + 1));
        }

        rawMessage = msg.toString();

        final Object[] messageArguments = {param1, param2};
        // Create a new MessageFormat to ensure thread safety.
        // Sharing a MessageFormat and using applyPattern is not thread safe
        MessageFormat formatter = new MessageFormat(rawMessage, _currentLocale);

        final String message = formatter.format(messageArguments);

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName14973 =  "DES";
				try{
					System.out.println("cipherName-14973" + javax.crypto.Cipher.getInstance(cipherName14973).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName14974 =  "DES";
				try{
					System.out.println("cipherName-14974" + javax.crypto.Cipher.getInstance(cipherName14974).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return CREATED_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName14975 =  "DES";
				try{
					System.out.println("cipherName-14975" + javax.crypto.Cipher.getInstance(cipherName14975).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName14976 =  "DES";
					try{
						System.out.println("cipherName-14976" + javax.crypto.Cipher.getInstance(cipherName14976).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName14977 =  "DES";
					try{
						System.out.println("cipherName-14977" + javax.crypto.Cipher.getInstance(cipherName14977).getAlgorithm());
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
                String cipherName14978 =  "DES";
				try{
					System.out.println("cipherName-14978" + javax.crypto.Cipher.getInstance(cipherName14978).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a Exchange message of the Format:
     * <pre>EXH-1002 : Deleted</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage DELETED()
    {
        String cipherName14979 =  "DES";
		try{
			System.out.println("cipherName-14979" + javax.crypto.Cipher.getInstance(cipherName14979).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("DELETED");

        final String message = rawMessage;

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName14980 =  "DES";
				try{
					System.out.println("cipherName-14980" + javax.crypto.Cipher.getInstance(cipherName14980).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName14981 =  "DES";
				try{
					System.out.println("cipherName-14981" + javax.crypto.Cipher.getInstance(cipherName14981).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return DELETED_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName14982 =  "DES";
				try{
					System.out.println("cipherName-14982" + javax.crypto.Cipher.getInstance(cipherName14982).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName14983 =  "DES";
					try{
						System.out.println("cipherName-14983" + javax.crypto.Cipher.getInstance(cipherName14983).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName14984 =  "DES";
					try{
						System.out.println("cipherName-14984" + javax.crypto.Cipher.getInstance(cipherName14984).getAlgorithm());
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
                String cipherName14985 =  "DES";
				try{
					System.out.println("cipherName-14985" + javax.crypto.Cipher.getInstance(cipherName14985).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a Exchange message of the Format:
     * <pre>EXH-1003 : Discarded Message : Name: "{0}" Routing Key: "{1}"</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage DISCARDMSG(String param1, String param2)
    {
        String cipherName14986 =  "DES";
		try{
			System.out.println("cipherName-14986" + javax.crypto.Cipher.getInstance(cipherName14986).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("DISCARDMSG");

        final Object[] messageArguments = {param1, param2};
        // Create a new MessageFormat to ensure thread safety.
        // Sharing a MessageFormat and using applyPattern is not thread safe
        MessageFormat formatter = new MessageFormat(rawMessage, _currentLocale);

        final String message = formatter.format(messageArguments);

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName14987 =  "DES";
				try{
					System.out.println("cipherName-14987" + javax.crypto.Cipher.getInstance(cipherName14987).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName14988 =  "DES";
				try{
					System.out.println("cipherName-14988" + javax.crypto.Cipher.getInstance(cipherName14988).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return DISCARDMSG_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName14989 =  "DES";
				try{
					System.out.println("cipherName-14989" + javax.crypto.Cipher.getInstance(cipherName14989).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName14990 =  "DES";
					try{
						System.out.println("cipherName-14990" + javax.crypto.Cipher.getInstance(cipherName14990).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName14991 =  "DES";
					try{
						System.out.println("cipherName-14991" + javax.crypto.Cipher.getInstance(cipherName14991).getAlgorithm());
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
                String cipherName14992 =  "DES";
				try{
					System.out.println("cipherName-14992" + javax.crypto.Cipher.getInstance(cipherName14992).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a Exchange message of the Format:
     * <pre>EXH-1004 : Operation : {0}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage OPERATION(String param1)
    {
        String cipherName14993 =  "DES";
		try{
			System.out.println("cipherName-14993" + javax.crypto.Cipher.getInstance(cipherName14993).getAlgorithm());
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
                String cipherName14994 =  "DES";
				try{
					System.out.println("cipherName-14994" + javax.crypto.Cipher.getInstance(cipherName14994).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName14995 =  "DES";
				try{
					System.out.println("cipherName-14995" + javax.crypto.Cipher.getInstance(cipherName14995).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return OPERATION_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName14996 =  "DES";
				try{
					System.out.println("cipherName-14996" + javax.crypto.Cipher.getInstance(cipherName14996).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName14997 =  "DES";
					try{
						System.out.println("cipherName-14997" + javax.crypto.Cipher.getInstance(cipherName14997).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName14998 =  "DES";
					try{
						System.out.println("cipherName-14998" + javax.crypto.Cipher.getInstance(cipherName14998).getAlgorithm());
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
                String cipherName14999 =  "DES";
				try{
					System.out.println("cipherName-14999" + javax.crypto.Cipher.getInstance(cipherName14999).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }


    private ExchangeMessages()
    {
		String cipherName15000 =  "DES";
		try{
			System.out.println("cipherName-15000" + javax.crypto.Cipher.getInstance(cipherName15000).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

}
