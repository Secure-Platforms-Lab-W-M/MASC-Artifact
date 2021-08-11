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
 * This file is based on the content of TrustStore_logmessages.properties
 *
 * To regenerate, use Maven lifecycle generates-sources with -Dgenerate=true
 */
public class TrustStoreMessages
{
    private static ResourceBundle _messages;
    private static Locale _currentLocale;

    static
    {
        String cipherName15586 =  "DES";
		try{
			System.out.println("cipherName-15586" + javax.crypto.Cipher.getInstance(cipherName15586).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Locale locale = Locale.US;
        String localeSetting = System.getProperty("qpid.broker_locale");
        if (localeSetting != null)
        {
            String cipherName15587 =  "DES";
			try{
				System.out.println("cipherName-15587" + javax.crypto.Cipher.getInstance(cipherName15587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String[] localeParts = localeSetting.split("_");
            String language = (localeParts.length > 0 ? localeParts[0] : "");
            String country = (localeParts.length > 1 ? localeParts[1] : "");
            String variant = "";
            if (localeParts.length > 2)
            {
                String cipherName15588 =  "DES";
				try{
					System.out.println("cipherName-15588" + javax.crypto.Cipher.getInstance(cipherName15588).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				variant = localeSetting.substring(language.length() + 1 + country.length() + 1);
            }
            locale = new Locale(language, country, variant);
        }
        _currentLocale = locale;
    }

    public static final String TRUSTSTORE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "truststore";
    public static final String CLOSE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "truststore.close";
    public static final String CREATE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "truststore.create";
    public static final String DELETE_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "truststore.delete";
    public static final String EXPIRING_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "truststore.expiring";
    public static final String OPEN_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "truststore.open";
    public static final String OPERATION_LOG_HIERARCHY = DEFAULT_LOG_HIERARCHY_PREFIX + "truststore.operation";

    static
    {
        String cipherName15589 =  "DES";
		try{
			System.out.println("cipherName-15589" + javax.crypto.Cipher.getInstance(cipherName15589).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		LoggerFactory.getLogger(TRUSTSTORE_LOG_HIERARCHY);
        LoggerFactory.getLogger(CLOSE_LOG_HIERARCHY);
        LoggerFactory.getLogger(CREATE_LOG_HIERARCHY);
        LoggerFactory.getLogger(DELETE_LOG_HIERARCHY);
        LoggerFactory.getLogger(EXPIRING_LOG_HIERARCHY);
        LoggerFactory.getLogger(OPEN_LOG_HIERARCHY);
        LoggerFactory.getLogger(OPERATION_LOG_HIERARCHY);

        _messages = ResourceBundle.getBundle("org.apache.qpid.server.logging.messages.TrustStore_logmessages", _currentLocale);
    }

    /**
     * Log a TrustStore message of the Format:
     * <pre>TST-1003 : Close</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CLOSE()
    {
        String cipherName15590 =  "DES";
		try{
			System.out.println("cipherName-15590" + javax.crypto.Cipher.getInstance(cipherName15590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("CLOSE");

        final String message = rawMessage;

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15591 =  "DES";
				try{
					System.out.println("cipherName-15591" + javax.crypto.Cipher.getInstance(cipherName15591).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15592 =  "DES";
				try{
					System.out.println("cipherName-15592" + javax.crypto.Cipher.getInstance(cipherName15592).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return CLOSE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15593 =  "DES";
				try{
					System.out.println("cipherName-15593" + javax.crypto.Cipher.getInstance(cipherName15593).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15594 =  "DES";
					try{
						System.out.println("cipherName-15594" + javax.crypto.Cipher.getInstance(cipherName15594).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15595 =  "DES";
					try{
						System.out.println("cipherName-15595" + javax.crypto.Cipher.getInstance(cipherName15595).getAlgorithm());
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
                String cipherName15596 =  "DES";
				try{
					System.out.println("cipherName-15596" + javax.crypto.Cipher.getInstance(cipherName15596).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TrustStore message of the Format:
     * <pre>TST-1001 : Create "{0}"</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage CREATE(String param1)
    {
        String cipherName15597 =  "DES";
		try{
			System.out.println("cipherName-15597" + javax.crypto.Cipher.getInstance(cipherName15597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("CREATE");

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
                String cipherName15598 =  "DES";
				try{
					System.out.println("cipherName-15598" + javax.crypto.Cipher.getInstance(cipherName15598).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15599 =  "DES";
				try{
					System.out.println("cipherName-15599" + javax.crypto.Cipher.getInstance(cipherName15599).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return CREATE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15600 =  "DES";
				try{
					System.out.println("cipherName-15600" + javax.crypto.Cipher.getInstance(cipherName15600).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15601 =  "DES";
					try{
						System.out.println("cipherName-15601" + javax.crypto.Cipher.getInstance(cipherName15601).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15602 =  "DES";
					try{
						System.out.println("cipherName-15602" + javax.crypto.Cipher.getInstance(cipherName15602).getAlgorithm());
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
                String cipherName15603 =  "DES";
				try{
					System.out.println("cipherName-15603" + javax.crypto.Cipher.getInstance(cipherName15603).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TrustStore message of the Format:
     * <pre>TST-1004 : Delete "{0}"</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage DELETE(String param1)
    {
        String cipherName15604 =  "DES";
		try{
			System.out.println("cipherName-15604" + javax.crypto.Cipher.getInstance(cipherName15604).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("DELETE");

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
                String cipherName15605 =  "DES";
				try{
					System.out.println("cipherName-15605" + javax.crypto.Cipher.getInstance(cipherName15605).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15606 =  "DES";
				try{
					System.out.println("cipherName-15606" + javax.crypto.Cipher.getInstance(cipherName15606).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return DELETE_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15607 =  "DES";
				try{
					System.out.println("cipherName-15607" + javax.crypto.Cipher.getInstance(cipherName15607).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15608 =  "DES";
					try{
						System.out.println("cipherName-15608" + javax.crypto.Cipher.getInstance(cipherName15608).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15609 =  "DES";
					try{
						System.out.println("cipherName-15609" + javax.crypto.Cipher.getInstance(cipherName15609).getAlgorithm());
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
                String cipherName15610 =  "DES";
				try{
					System.out.println("cipherName-15610" + javax.crypto.Cipher.getInstance(cipherName15610).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TrustStore message of the Format:
     * <pre>TST-1005 : TrustStore {0} Certificate expires in {1} days : {2}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage EXPIRING(String param1, String param2, String param3)
    {
        String cipherName15611 =  "DES";
		try{
			System.out.println("cipherName-15611" + javax.crypto.Cipher.getInstance(cipherName15611).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("EXPIRING");

        final Object[] messageArguments = {param1, param2, param3};
        // Create a new MessageFormat to ensure thread safety.
        // Sharing a MessageFormat and using applyPattern is not thread safe
        MessageFormat formatter = new MessageFormat(rawMessage, _currentLocale);

        final String message = formatter.format(messageArguments);

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15612 =  "DES";
				try{
					System.out.println("cipherName-15612" + javax.crypto.Cipher.getInstance(cipherName15612).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15613 =  "DES";
				try{
					System.out.println("cipherName-15613" + javax.crypto.Cipher.getInstance(cipherName15613).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return EXPIRING_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15614 =  "DES";
				try{
					System.out.println("cipherName-15614" + javax.crypto.Cipher.getInstance(cipherName15614).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15615 =  "DES";
					try{
						System.out.println("cipherName-15615" + javax.crypto.Cipher.getInstance(cipherName15615).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15616 =  "DES";
					try{
						System.out.println("cipherName-15616" + javax.crypto.Cipher.getInstance(cipherName15616).getAlgorithm());
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
                String cipherName15617 =  "DES";
				try{
					System.out.println("cipherName-15617" + javax.crypto.Cipher.getInstance(cipherName15617).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TrustStore message of the Format:
     * <pre>TST-1002 : Open</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage OPEN()
    {
        String cipherName15618 =  "DES";
		try{
			System.out.println("cipherName-15618" + javax.crypto.Cipher.getInstance(cipherName15618).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String rawMessage = _messages.getString("OPEN");

        final String message = rawMessage;

        return new LogMessage()
        {
            @Override
            public String toString()
            {
                String cipherName15619 =  "DES";
				try{
					System.out.println("cipherName-15619" + javax.crypto.Cipher.getInstance(cipherName15619).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15620 =  "DES";
				try{
					System.out.println("cipherName-15620" + javax.crypto.Cipher.getInstance(cipherName15620).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return OPEN_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15621 =  "DES";
				try{
					System.out.println("cipherName-15621" + javax.crypto.Cipher.getInstance(cipherName15621).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15622 =  "DES";
					try{
						System.out.println("cipherName-15622" + javax.crypto.Cipher.getInstance(cipherName15622).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15623 =  "DES";
					try{
						System.out.println("cipherName-15623" + javax.crypto.Cipher.getInstance(cipherName15623).getAlgorithm());
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
                String cipherName15624 =  "DES";
				try{
					System.out.println("cipherName-15624" + javax.crypto.Cipher.getInstance(cipherName15624).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }

    /**
     * Log a TrustStore message of the Format:
     * <pre>TST-1005 : Operation : {0}</pre>
     * Optional values are contained in [square brackets] and are numbered
     * sequentially in the method call.
     *
     */
    public static LogMessage OPERATION(String param1)
    {
        String cipherName15625 =  "DES";
		try{
			System.out.println("cipherName-15625" + javax.crypto.Cipher.getInstance(cipherName15625).getAlgorithm());
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
                String cipherName15626 =  "DES";
				try{
					System.out.println("cipherName-15626" + javax.crypto.Cipher.getInstance(cipherName15626).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return message;
            }

            @Override
            public String getLogHierarchy()
            {
                String cipherName15627 =  "DES";
				try{
					System.out.println("cipherName-15627" + javax.crypto.Cipher.getInstance(cipherName15627).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return OPERATION_LOG_HIERARCHY;
            }

            @Override
            public boolean equals(final Object o)
            {
                String cipherName15628 =  "DES";
				try{
					System.out.println("cipherName-15628" + javax.crypto.Cipher.getInstance(cipherName15628).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (this == o)
                {
                    String cipherName15629 =  "DES";
					try{
						System.out.println("cipherName-15629" + javax.crypto.Cipher.getInstance(cipherName15629).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return true;
                }
                if (o == null || getClass() != o.getClass())
                {
                    String cipherName15630 =  "DES";
					try{
						System.out.println("cipherName-15630" + javax.crypto.Cipher.getInstance(cipherName15630).getAlgorithm());
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
                String cipherName15631 =  "DES";
				try{
					System.out.println("cipherName-15631" + javax.crypto.Cipher.getInstance(cipherName15631).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int result = toString().hashCode();
                result = 31 * result + getLogHierarchy().hashCode();
                return result;
            }
        };
    }


    private TrustStoreMessages()
    {
		String cipherName15632 =  "DES";
		try{
			System.out.println("cipherName-15632" + javax.crypto.Cipher.getInstance(cipherName15632).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

}
