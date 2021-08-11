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
 * This file is based on the content of TransactionLog_logmessages.properties
 *
 * To regenerate, use Maven lifecycle generates-sources with -Dgenerate=true
 */
public class TransactionLogMessages
{
    private static ResourceBundle _messages;
    private static Locale _currentLocale;

    static
    {
        String cipherName15151 =  "DES";
		try{
			System.out.println("cipherName-15151" + javax.crypto.Cipher.getInstance(cipherName15151).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Locale locale = Locale.US;
        String localeSetting = System.getProperty("qpid.broker_locale");
        if (localeSetting != null)
        {
            String cipherName15152 =  "DES";
			try{
				System.out.println("cipherName-15152" + javax.crypto.Cipher.getInstance(cipherName15152).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String[] localeParts = localeSetting.split("_");
            String language = (localeParts.length > 0 ? localeParts[0] : "");
            String country = (localeParts.length > 1 ? localeParts[1] : "");
            String variant = "";
            if (localeParts.length > 2)
            {
                String cipherName15153 =  "DES";
				try{
					System.out.println("cipherName-15153" + javax.crypto.Cipher.getInstance(cipherName15153).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				variant = localeSetting.substring(language.length() + 1 + country.length() + 1);
            }
            locale = new Locale(language, country, variant);
        }
        _currentLocale = locale;
    }

    public static final String TRANSACTIONLOG_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog";
    public static final String CLOSED_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog.closed";
    public static final String CREATED_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog.created";
    public static final String RECOVERED_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog.recovered";
    public static final String RECOVERY_COMPLETE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog.recovery_complete";
    public static final String RECOVERY_START_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog.recovery_start";
    public static final String STORE_LOCATION_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog.store_location";
    public static final String XA_INCOMPLETE_MESSAGE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog.xa_incomplete_message";
    public static final String XA_INCOMPLETE_QUEUE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "transactionlog.xa_incomplete_queue";

    static
    {
        String cipherName15154 =  "DES";
		try{
			System.out.println("cipherName-15154" + javax.crypto.Cipher.getInstance(cipherName15154).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LoggerFactory.getLogger(TRANSACTIONLOG_LOG_HIERARCHY);
        LoggerFactory.getLogger(CLOSED_LOG_HIERARCHY);
        LoggerFactory.getLogger(CREATED_LOG_HIERARCHY);
        LoggerFactory.getLogger(RECOVERED_LOG_HIERARCHY);
        LoggerFactory.getLogger(RECOVERY_COMPLETE_LOG_HIERARCHY);
        LoggerFactory.getLogger(RECOVERY_START_LOG_HIERARCHY);
        LoggerFactory.getLogger(STORE_LOCATION_LOG_HIERARCHY);
        LoggerFactory.getLogger(XA_INCOMPLETE_MESSAGE_LOG_HIERARCHY);
        LoggerFactory.getLogger(XA_INCOMPLETE_QUEUE_LOG_HIERARCHY);

        _messages = ResourceBundle.getBundle("org.apache.qpid.server.logging.messages.TransactionLog_logmessages", _currentLocale);
    }

    /**
     * Log a TransactionLog message of the Format:
     * <pre>TXN-1003 : Closed</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CLOSED()
    {
        String cipherName15155 =  "DES";
		try{
			System.out.println("cipherName-15155" + javax.crypto.Cipher.getInstance(cipherName15155).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("CLOSED");

        final String message = rawMessage;

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15156 =  "DES";
				try{
					System.out.println("cipherName-15156" + javax.crypto.Cipher.getInstance(cipherName15156).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15157 =  "DES";
				try{
					System.out.println("cipherName-15157" + javax.crypto.Cipher.getInstance(cipherName15157).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return CLOSED_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15158 =  "DES";
				try{
					System.out.println("cipherName-15158" + javax.crypto.Cipher.getInstance(cipherName15158).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15159 =  "DES";
					try{
						System.out.println("cipherName-15159" + javax.crypto.Cipher.getInstance(cipherName15159).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15160 =  "DES";
					try{
						System.out.println("cipherName-15160" + javax.crypto.Cipher.getInstance(cipherName15160).getAlgorithm());
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
                String cipherName15161 =  "DES";
				try{
					System.out.println("cipherName-15161" + javax.crypto.Cipher.getInstance(cipherName15161).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TransactionLog message of the Format:
     * <pre>TXN-1001 : Created</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CREATED()
    {
        String cipherName15162 =  "DES";
		try{
			System.out.println("cipherName-15162" + javax.crypto.Cipher.getInstance(cipherName15162).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("CREATED");

        final String message = rawMessage;

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15163 =  "DES";
				try{
					System.out.println("cipherName-15163" + javax.crypto.Cipher.getInstance(cipherName15163).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15164 =  "DES";
				try{
					System.out.println("cipherName-15164" + javax.crypto.Cipher.getInstance(cipherName15164).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return CREATED_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15165 =  "DES";
				try{
					System.out.println("cipherName-15165" + javax.crypto.Cipher.getInstance(cipherName15165).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15166 =  "DES";
					try{
						System.out.println("cipherName-15166" + javax.crypto.Cipher.getInstance(cipherName15166).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15167 =  "DES";
					try{
						System.out.println("cipherName-15167" + javax.crypto.Cipher.getInstance(cipherName15167).getAlgorithm());
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
                String cipherName15168 =  "DES";
				try{
					System.out.println("cipherName-15168" + javax.crypto.Cipher.getInstance(cipherName15168).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TransactionLog message of the Format:
     * <pre>TXN-1005 : Recovered {0,number} messages for queue {1}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage RECOVERED(Number param1, String param2)
    {
        String cipherName15169 =  "DES";
		try{
			System.out.println("cipherName-15169" + javax.crypto.Cipher.getInstance(cipherName15169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("RECOVERED");

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
                String cipherName15170 =  "DES";
				try{
					System.out.println("cipherName-15170" + javax.crypto.Cipher.getInstance(cipherName15170).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15171 =  "DES";
				try{
					System.out.println("cipherName-15171" + javax.crypto.Cipher.getInstance(cipherName15171).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return RECOVERED_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15172 =  "DES";
				try{
					System.out.println("cipherName-15172" + javax.crypto.Cipher.getInstance(cipherName15172).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15173 =  "DES";
					try{
						System.out.println("cipherName-15173" + javax.crypto.Cipher.getInstance(cipherName15173).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15174 =  "DES";
					try{
						System.out.println("cipherName-15174" + javax.crypto.Cipher.getInstance(cipherName15174).getAlgorithm());
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
                String cipherName15175 =  "DES";
				try{
					System.out.println("cipherName-15175" + javax.crypto.Cipher.getInstance(cipherName15175).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TransactionLog message of the Format:
     * <pre>TXN-1006 : Recovery Complete[ : {0}]</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage RECOVERY_COMPLETE(String param1, boolean opt1)
    {
        String cipherName15176 =  "DES";
		try{
			System.out.println("cipherName-15176" + javax.crypto.Cipher.getInstance(cipherName15176).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("RECOVERY_COMPLETE");
        StringBuffer msg = new StringBuffer();

        // Split the formatted message up on the option values so we can
        // rebuild the message based on the configured options.
        String[] parts = rawMessage.split("\\[");
        msg.append(parts[0]);

        int end;
        if (parts.length > 1)
        {

            String cipherName15177 =  "DES";
			try{
				System.out.println("cipherName-15177" + javax.crypto.Cipher.getInstance(cipherName15177).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Add Option : : {0}.
            end = parts[1].indexOf(']');
            if (opt1)
            {
                String cipherName15178 =  "DES";
				try{
					System.out.println("cipherName-15178" + javax.crypto.Cipher.getInstance(cipherName15178).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				msg.append(parts[1].substring(0, end));
            }

            // Use 'end + 1' to remove the ']' from the output
            msg.append(parts[1].substring(end + 1));
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
                String cipherName15179 =  "DES";
				try{
					System.out.println("cipherName-15179" + javax.crypto.Cipher.getInstance(cipherName15179).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15180 =  "DES";
				try{
					System.out.println("cipherName-15180" + javax.crypto.Cipher.getInstance(cipherName15180).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return RECOVERY_COMPLETE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15181 =  "DES";
				try{
					System.out.println("cipherName-15181" + javax.crypto.Cipher.getInstance(cipherName15181).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15182 =  "DES";
					try{
						System.out.println("cipherName-15182" + javax.crypto.Cipher.getInstance(cipherName15182).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15183 =  "DES";
					try{
						System.out.println("cipherName-15183" + javax.crypto.Cipher.getInstance(cipherName15183).getAlgorithm());
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
                String cipherName15184 =  "DES";
				try{
					System.out.println("cipherName-15184" + javax.crypto.Cipher.getInstance(cipherName15184).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TransactionLog message of the Format:
     * <pre>TXN-1004 : Recovery Start[ : {0}]</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage RECOVERY_START(String param1, boolean opt1)
    {
        String cipherName15185 =  "DES";
		try{
			System.out.println("cipherName-15185" + javax.crypto.Cipher.getInstance(cipherName15185).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("RECOVERY_START");
        StringBuffer msg = new StringBuffer();

        // Split the formatted message up on the option values so we can
        // rebuild the message based on the configured options.
        String[] parts = rawMessage.split("\\[");
        msg.append(parts[0]);

        int end;
        if (parts.length > 1)
        {

            String cipherName15186 =  "DES";
			try{
				System.out.println("cipherName-15186" + javax.crypto.Cipher.getInstance(cipherName15186).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Add Option : : {0}.
            end = parts[1].indexOf(']');
            if (opt1)
            {
                String cipherName15187 =  "DES";
				try{
					System.out.println("cipherName-15187" + javax.crypto.Cipher.getInstance(cipherName15187).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				msg.append(parts[1].substring(0, end));
            }

            // Use 'end + 1' to remove the ']' from the output
            msg.append(parts[1].substring(end + 1));
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
                String cipherName15188 =  "DES";
				try{
					System.out.println("cipherName-15188" + javax.crypto.Cipher.getInstance(cipherName15188).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15189 =  "DES";
				try{
					System.out.println("cipherName-15189" + javax.crypto.Cipher.getInstance(cipherName15189).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return RECOVERY_START_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15190 =  "DES";
				try{
					System.out.println("cipherName-15190" + javax.crypto.Cipher.getInstance(cipherName15190).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15191 =  "DES";
					try{
						System.out.println("cipherName-15191" + javax.crypto.Cipher.getInstance(cipherName15191).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15192 =  "DES";
					try{
						System.out.println("cipherName-15192" + javax.crypto.Cipher.getInstance(cipherName15192).getAlgorithm());
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
                String cipherName15193 =  "DES";
				try{
					System.out.println("cipherName-15193" + javax.crypto.Cipher.getInstance(cipherName15193).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TransactionLog message of the Format:
     * <pre>TXN-1002 : Store location : {0}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage STORE_LOCATION(String param1)
    {
        String cipherName15194 =  "DES";
		try{
			System.out.println("cipherName-15194" + javax.crypto.Cipher.getInstance(cipherName15194).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("STORE_LOCATION");

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
                String cipherName15195 =  "DES";
				try{
					System.out.println("cipherName-15195" + javax.crypto.Cipher.getInstance(cipherName15195).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15196 =  "DES";
				try{
					System.out.println("cipherName-15196" + javax.crypto.Cipher.getInstance(cipherName15196).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return STORE_LOCATION_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15197 =  "DES";
				try{
					System.out.println("cipherName-15197" + javax.crypto.Cipher.getInstance(cipherName15197).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15198 =  "DES";
					try{
						System.out.println("cipherName-15198" + javax.crypto.Cipher.getInstance(cipherName15198).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15199 =  "DES";
					try{
						System.out.println("cipherName-15199" + javax.crypto.Cipher.getInstance(cipherName15199).getAlgorithm());
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
                String cipherName15200 =  "DES";
				try{
					System.out.println("cipherName-15200" + javax.crypto.Cipher.getInstance(cipherName15200).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TransactionLog message of the Format:
     * <pre>TXN-1008 : XA transaction recover for xid {0} incomplete as it references a message {1} which was not durably retained</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage XA_INCOMPLETE_MESSAGE(String param1, String param2)
    {
        String cipherName15201 =  "DES";
		try{
			System.out.println("cipherName-15201" + javax.crypto.Cipher.getInstance(cipherName15201).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("XA_INCOMPLETE_MESSAGE");

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
                String cipherName15202 =  "DES";
				try{
					System.out.println("cipherName-15202" + javax.crypto.Cipher.getInstance(cipherName15202).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15203 =  "DES";
				try{
					System.out.println("cipherName-15203" + javax.crypto.Cipher.getInstance(cipherName15203).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return XA_INCOMPLETE_MESSAGE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15204 =  "DES";
				try{
					System.out.println("cipherName-15204" + javax.crypto.Cipher.getInstance(cipherName15204).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15205 =  "DES";
					try{
						System.out.println("cipherName-15205" + javax.crypto.Cipher.getInstance(cipherName15205).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15206 =  "DES";
					try{
						System.out.println("cipherName-15206" + javax.crypto.Cipher.getInstance(cipherName15206).getAlgorithm());
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
                String cipherName15207 =  "DES";
				try{
					System.out.println("cipherName-15207" + javax.crypto.Cipher.getInstance(cipherName15207).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TransactionLog message of the Format:
     * <pre>TXN-1007 : XA transaction recover for xid {0} incomplete as it references a queue {1} which was not durably retained</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage XA_INCOMPLETE_QUEUE(String param1, String param2)
    {
        String cipherName15208 =  "DES";
		try{
			System.out.println("cipherName-15208" + javax.crypto.Cipher.getInstance(cipherName15208).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("XA_INCOMPLETE_QUEUE");

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
                String cipherName15209 =  "DES";
				try{
					System.out.println("cipherName-15209" + javax.crypto.Cipher.getInstance(cipherName15209).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15210 =  "DES";
				try{
					System.out.println("cipherName-15210" + javax.crypto.Cipher.getInstance(cipherName15210).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return XA_INCOMPLETE_QUEUE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15211 =  "DES";
				try{
					System.out.println("cipherName-15211" + javax.crypto.Cipher.getInstance(cipherName15211).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15212 =  "DES";
					try{
						System.out.println("cipherName-15212" + javax.crypto.Cipher.getInstance(cipherName15212).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15213 =  "DES";
					try{
						System.out.println("cipherName-15213" + javax.crypto.Cipher.getInstance(cipherName15213).getAlgorithm());
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
                String cipherName15214 =  "DES";
				try{
					System.out.println("cipherName-15214" + javax.crypto.Cipher.getInstance(cipherName15214).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }


    private TransactionLogMessages()
    {
		String cipherName15215 =  "DES";
		try{
			System.out.println("cipherName-15215" + javax.crypto.Cipher.getInstance(cipherName15215).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

}
