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
package org.apache.qpid.server.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * SystemUtils provides some simple helper methods for working with the current
 * Operating System.
 *
 * It follows the convention of wrapping all checked exceptions as runtimes, so
 * code using these methods is free of try-catch blocks but does not expect to
 * recover from errors.
 */
public class SystemUtils
{

    public static final String UNKNOWN_OS = "unknown";
    public static final String UNKNOWN_VERSION = "na";
    public static final String UNKNOWN_ARCH = "unknown";

    private static final String _osName = System.getProperty("os.name", UNKNOWN_OS);
    private static final String _osVersion = System.getProperty("os.version", UNKNOWN_VERSION);
    private static final String _osArch = System.getProperty("os.arch", UNKNOWN_ARCH);
    private static final boolean _isWindows = _osName.toLowerCase().contains("windows");

    /** Process identifier of underlying process or null if it cannot be determined */
    private static final String _osPid;
    private static int _osPidInt;

    static
    {
        String cipherName6377 =  "DES";
		try{
			System.out.println("cipherName-6377" + javax.crypto.Cipher.getInstance(cipherName6377).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		RuntimeMXBean rtb = ManagementFactory.getRuntimeMXBean();
        String processName = rtb.getName();
        int atIndex;
        if(processName != null && (atIndex = processName.indexOf('@')) > 0)
        {
            String cipherName6378 =  "DES";
			try{
				System.out.println("cipherName-6378" + javax.crypto.Cipher.getInstance(cipherName6378).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_osPid = processName.substring(0, atIndex);
            _osPidInt = parseInt(_osPid, -1);
        }
        else
        {
            String cipherName6379 =  "DES";
			try{
				System.out.println("cipherName-6379" + javax.crypto.Cipher.getInstance(cipherName6379).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_osPid = null;
        }
    }


    private SystemUtils()
    {
		String cipherName6380 =  "DES";
		try{
			System.out.println("cipherName-6380" + javax.crypto.Cipher.getInstance(cipherName6380).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public final static String getOSName()
    {
        String cipherName6381 =  "DES";
		try{
			System.out.println("cipherName-6381" + javax.crypto.Cipher.getInstance(cipherName6381).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _osName;
    }

    public final static String getOSVersion()
    {
        String cipherName6382 =  "DES";
		try{
			System.out.println("cipherName-6382" + javax.crypto.Cipher.getInstance(cipherName6382).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _osVersion;
    }

    public final static String getOSArch()
    {
        String cipherName6383 =  "DES";
		try{
			System.out.println("cipherName-6383" + javax.crypto.Cipher.getInstance(cipherName6383).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _osArch;
    }

    public final static String getProcessPid()
    {
        String cipherName6384 =  "DES";
		try{
			System.out.println("cipherName-6384" + javax.crypto.Cipher.getInstance(cipherName6384).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _osPid;
    }

    public final static int getProcessPidAsInt()
    {
        String cipherName6385 =  "DES";
		try{
			System.out.println("cipherName-6385" + javax.crypto.Cipher.getInstance(cipherName6385).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _osPidInt;
    }

    public final static boolean isWindows()
    {
        String cipherName6386 =  "DES";
		try{
			System.out.println("cipherName-6386" + javax.crypto.Cipher.getInstance(cipherName6386).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _isWindows;
    }

    public final static String getOSConfigSuffix()
    {
        String cipherName6387 =  "DES";
		try{
			System.out.println("cipherName-6387" + javax.crypto.Cipher.getInstance(cipherName6387).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_osName.contains(" "))
        {
            String cipherName6388 =  "DES";
			try{
				System.out.println("cipherName-6388" + javax.crypto.Cipher.getInstance(cipherName6388).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _osName.substring(0, _osName.indexOf(' ')).toLowerCase();
        }
        return _osName;
    }

    public final static String getOSString()
    {
        String cipherName6389 =  "DES";
		try{
			System.out.println("cipherName-6389" + javax.crypto.Cipher.getInstance(cipherName6389).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _osName + " " + _osVersion + " " + _osArch;
    }

    private static int parseInt(String str, int defaultVal)
    {
        String cipherName6390 =  "DES";
		try{
			System.out.println("cipherName-6390" + javax.crypto.Cipher.getInstance(cipherName6390).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName6391 =  "DES";
			try{
				System.out.println("cipherName-6391" + javax.crypto.Cipher.getInstance(cipherName6391).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Integer.parseInt(str);
        }
        catch(NumberFormatException e)
        {
            String cipherName6392 =  "DES";
			try{
				System.out.println("cipherName-6392" + javax.crypto.Cipher.getInstance(cipherName6392).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return defaultVal;
        }
    }

}
