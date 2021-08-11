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
package org.apache.qpid.server.security.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import org.apache.qpid.server.model.GroupProvider;
import org.apache.qpid.server.security.auth.UsernamePrincipal;
import org.apache.qpid.test.utils.UnitTestBase;

public class GroupPrincipalTest extends UnitTestBase
{
    @Test
    public void testGetName()
    {
        String cipherName1620 =  "DES";
		try{
			System.out.println("cipherName-1620" + javax.crypto.Cipher.getInstance(cipherName1620).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final GroupPrincipal principal = new GroupPrincipal("group", (GroupProvider) null);
        assertEquals("group", principal.getName());
    }

    @Test
    public void testAddRejected()
    {
        String cipherName1621 =  "DES";
		try{
			System.out.println("cipherName-1621" + javax.crypto.Cipher.getInstance(cipherName1621).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final GroupPrincipal principal = new GroupPrincipal("group", (GroupProvider) null);
        final UsernamePrincipal user = new UsernamePrincipal("name", null);

        try
        {
            String cipherName1622 =  "DES";
			try{
				System.out.println("cipherName-1622" + javax.crypto.Cipher.getInstance(cipherName1622).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			principal.addMember(user);
            fail("Exception not thrown");
        }
        catch (UnsupportedOperationException uso)
        {
			String cipherName1623 =  "DES";
			try{
				System.out.println("cipherName-1623" + javax.crypto.Cipher.getInstance(cipherName1623).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // PASS
        }
    }

    @Test
    public void testEqualitySameName()
    {
        String cipherName1624 =  "DES";
		try{
			System.out.println("cipherName-1624" + javax.crypto.Cipher.getInstance(cipherName1624).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String string = "string";
        final GroupPrincipal principal1 = new GroupPrincipal(string, (GroupProvider) null);
        final GroupPrincipal principal2 = new GroupPrincipal(string, (GroupProvider) null);
        assertTrue(principal1.equals(principal2));
    }

    @Test
    public void testEqualityEqualName()
    {
        String cipherName1625 =  "DES";
		try{
			System.out.println("cipherName-1625" + javax.crypto.Cipher.getInstance(cipherName1625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final GroupPrincipal principal1 = new GroupPrincipal(new String("string"), (GroupProvider) null);
        final GroupPrincipal principal2 = new GroupPrincipal(new String("string"), (GroupProvider) null);
        assertTrue(principal1.equals(principal2));
    }

    @Test
    public void testInequalityDifferentGroupPrincipals()
    {
        String cipherName1626 =  "DES";
		try{
			System.out.println("cipherName-1626" + javax.crypto.Cipher.getInstance(cipherName1626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		GroupPrincipal principal1 = new GroupPrincipal("string1", (GroupProvider) null);
        GroupPrincipal principal2 = new GroupPrincipal("string2", (GroupProvider) null);
        assertFalse(principal1.equals(principal2));
    }

    @Test
    public void testInequalityNonGroupPrincipal()
    {
        String cipherName1627 =  "DES";
		try{
			System.out.println("cipherName-1627" + javax.crypto.Cipher.getInstance(cipherName1627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		GroupPrincipal principal = new GroupPrincipal("string", (GroupProvider) null);
        assertFalse(principal.equals(new UsernamePrincipal("string", null)));
    }

    @Test
    public void testInequalityNull()
    {
        String cipherName1628 =  "DES";
		try{
			System.out.println("cipherName-1628" + javax.crypto.Cipher.getInstance(cipherName1628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		GroupPrincipal principal = new GroupPrincipal("string", (GroupProvider) null);
        assertFalse(principal.equals(null));
    }




}
