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
package org.apache.qpid.server.logging;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.qpid.server.model.Content;
import org.apache.qpid.server.model.CustomRestHeaders;
import org.apache.qpid.server.model.RestContentHeader;

public class ZippedContent implements Content, CustomRestHeaders
{
    private static final Format FORMAT =  new SimpleDateFormat("YYYY-MM-dd-HHmmss");
    private static final String DISPOSITION =  "attachment; filename=\"log-files-%s.zip\"";

    private final Map<String, Path> _paths;
    private final String _disposition;


    public ZippedContent(Map<String, Path> paths)
    {
        String cipherName15748 =  "DES";
		try{
			System.out.println("cipherName-15748" + javax.crypto.Cipher.getInstance(cipherName15748).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_paths = paths;
        _disposition = String.format(DISPOSITION, FORMAT.format(new Date()));
    }

    @Override
    public void write(OutputStream outputStream) throws IOException
    {
        String cipherName15749 =  "DES";
		try{
			System.out.println("cipherName-15749" + javax.crypto.Cipher.getInstance(cipherName15749).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try(ZipOutputStream out = new ZipOutputStream(outputStream))
        {
            String cipherName15750 =  "DES";
			try{
				System.out.println("cipherName-15750" + javax.crypto.Cipher.getInstance(cipherName15750).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Map.Entry<String, Path> entry: _paths.entrySet())
            {
                String cipherName15751 =  "DES";
				try{
					System.out.println("cipherName-15751" + javax.crypto.Cipher.getInstance(cipherName15751).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				addLogFileEntry(entry.getKey(), entry.getValue(), out);
            }
        }
    }

    @Override
    public void release()
    {
		String cipherName15752 =  "DES";
		try{
			System.out.println("cipherName-15752" + javax.crypto.Cipher.getInstance(cipherName15752).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    private void addLogFileEntry(String zipEntryName, Path path, ZipOutputStream out) throws IOException
    {
        String cipherName15753 =  "DES";
		try{
			System.out.println("cipherName-15753" + javax.crypto.Cipher.getInstance(cipherName15753).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		File file = path.toFile();
        if (file.exists())
        {
            String cipherName15754 =  "DES";
			try{
				System.out.println("cipherName-15754" + javax.crypto.Cipher.getInstance(cipherName15754).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ZipEntry entry = new ZipEntry(zipEntryName);
            entry.setSize(file.length());
            out.putNextEntry(entry);
            Files.copy(path, out);
            out.closeEntry();
        }
        out.flush();
    }

    @RestContentHeader("Content-Type")
    public String getContentType()
    {
        String cipherName15755 =  "DES";
		try{
			System.out.println("cipherName-15755" + javax.crypto.Cipher.getInstance(cipherName15755).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "application/x-zip";
    }

    @RestContentHeader("Content-Disposition")
    public String getContentDisposition()
    {
        String cipherName15756 =  "DES";
		try{
			System.out.println("cipherName-15756" + javax.crypto.Cipher.getInstance(cipherName15756).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _disposition;
    }
}
