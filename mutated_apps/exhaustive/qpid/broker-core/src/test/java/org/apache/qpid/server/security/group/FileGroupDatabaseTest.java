/*
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.qpid.server.model.adapter.FileBasedGroupProvider;
import org.apache.qpid.test.utils.UnitTestBase;

public class FileGroupDatabaseTest extends UnitTestBase
{
    private static final String USER1 = "user1";
    private static final String USER2 = "user2";
    private static final String USER3 = "user3";

    private static final String MY_GROUP = "myGroup";
    private static final String MY_GROUP2 = "myGroup2";
    private static final String MY_GROUP1 = "myGroup1";

    private static final boolean CASE_SENSITIVE = true;
    private FileGroupDatabase _fileGroupDatabase;
    private GroupProviderUtil _util;
    private String _groupFile;
    private FileBasedGroupProvider<?> _groupProvider;

    @Before
    public void setUp() throws Exception
    {
        String cipherName1582 =  "DES";
		try{
			System.out.println("cipherName-1582" + javax.crypto.Cipher.getInstance(cipherName1582).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_groupProvider = mock(FileBasedGroupProvider.class);
        when(_groupProvider.isCaseSensitive()).thenReturn(CASE_SENSITIVE);
        _fileGroupDatabase = new FileGroupDatabase(_groupProvider);
        _util = new GroupProviderUtil(_fileGroupDatabase);
        _groupFile = _util.getGroupFile();
    }

    @Test
    public void testGetAllGroups() throws Exception
    {
        String cipherName1583 =  "DES";
		try{
			System.out.println("cipherName-1583" + javax.crypto.Cipher.getInstance(cipherName1583).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", USER1);

        Set<String> groups = _fileGroupDatabase.getAllGroups();
        assertEquals(1, groups.size());
        assertTrue(groups.contains(MY_GROUP));
    }

    @Test
    public void testGetAllGroupsWhenGroupFileEmpty()
    {
        String cipherName1584 =  "DES";
		try{
			System.out.println("cipherName-1584" + javax.crypto.Cipher.getInstance(cipherName1584).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Set<String> groups = _fileGroupDatabase.getAllGroups();
        assertTrue(groups.isEmpty());
    }

    @Test
    public void testMissingGroupFile() throws Exception
    {
        String cipherName1585 =  "DES";
		try{
			System.out.println("cipherName-1585" + javax.crypto.Cipher.getInstance(cipherName1585).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName1586 =  "DES";
			try{
				System.out.println("cipherName-1586" + javax.crypto.Cipher.getInstance(cipherName1586).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_fileGroupDatabase.setGroupFile("/not/a/file");
            fail("Exception not thrown");
        }
        catch (FileNotFoundException fnfe)
        {
			String cipherName1587 =  "DES";
			try{
				System.out.println("cipherName-1587" + javax.crypto.Cipher.getInstance(cipherName1587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // PASS
        }
    }

    @Test
    public void testInvalidFormat() throws Exception
    {
        String cipherName1588 =  "DES";
		try{
			System.out.println("cipherName-1588" + javax.crypto.Cipher.getInstance(cipherName1588).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeGroupFile("name.notvalid", USER1);

        try
        {
            String cipherName1589 =  "DES";
			try{
				System.out.println("cipherName-1589" + javax.crypto.Cipher.getInstance(cipherName1589).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_fileGroupDatabase.setGroupFile(_groupFile);
            fail("Exception not thrown");
        }
        catch (IllegalArgumentException gde)
        {
			String cipherName1590 =  "DES";
			try{
				System.out.println("cipherName-1590" + javax.crypto.Cipher.getInstance(cipherName1590).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // PASS
        }
    }

    @Test
    public void testGetUsersInGroup() throws Exception
    {
        String cipherName1591 =  "DES";
		try{
			System.out.println("cipherName-1591" + javax.crypto.Cipher.getInstance(cipherName1591).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user2,user3");

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    public void testDuplicateUsersInGroupAreConflated() throws Exception
    {
        String cipherName1592 =  "DES";
		try{
			System.out.println("cipherName-1592" + javax.crypto.Cipher.getInstance(cipherName1592).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user1,user3,user1");

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUsersWithEmptyGroup() throws Exception
    {
        String cipherName1593 =  "DES";
		try{
			System.out.println("cipherName-1593" + javax.crypto.Cipher.getInstance(cipherName1593).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "");

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testGetUsersInNonExistentGroup() throws Exception
    {
        String cipherName1594 =  "DES";
		try{
			System.out.println("cipherName-1594" + javax.crypto.Cipher.getInstance(cipherName1594).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user2,user3");

        Set<String> users = _fileGroupDatabase.getUsersInGroup("groupDoesntExist");
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testGetUsersInNullGroup() throws Exception
    {
        String cipherName1595 =  "DES";
		try{
			System.out.println("cipherName-1595" + javax.crypto.Cipher.getInstance(cipherName1595).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile();
        assertTrue(_fileGroupDatabase.getUsersInGroup(null).isEmpty());
    }

    @Test
    public void testGetGroupPrincipalsForUserWhenUserBelongsToOneGroup() throws Exception
    {
        String cipherName1596 =  "DES";
		try{
			System.out.println("cipherName-1596" + javax.crypto.Cipher.getInstance(cipherName1596).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user2");
        Set<String> groups = _fileGroupDatabase.getGroupsForUser(USER1);
        assertEquals(1, groups.size());
        assertTrue(groups.contains(MY_GROUP));
    }

    @Test
    public void testGetGroupPrincipalsForUserWhenUserBelongsToTwoGroup() throws Exception
    {
        String cipherName1597 =  "DES";
		try{
			System.out.println("cipherName-1597" + javax.crypto.Cipher.getInstance(cipherName1597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup1.users", "user1,user2", "myGroup2.users", "user1,user3");
        Set<String> groups = _fileGroupDatabase.getGroupsForUser(USER1);
        assertEquals(2, groups.size());
        assertTrue(groups.contains(MY_GROUP1));
        assertTrue(groups.contains(MY_GROUP2));
    }

    @Test
    public void testGetGroupPrincipalsForUserWhenUserAddedToGroup() throws Exception
    {
        String cipherName1598 =  "DES";
		try{
			System.out.println("cipherName-1598" + javax.crypto.Cipher.getInstance(cipherName1598).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup1.users", "user1,user2", "myGroup2.users", USER2);
        Set<String> groups = _fileGroupDatabase.getGroupsForUser(USER1);
        assertEquals(1, groups.size());
        assertTrue(groups.contains(MY_GROUP1));

        _fileGroupDatabase.addUserToGroup(USER1, MY_GROUP2);

        groups = _fileGroupDatabase.getGroupsForUser(USER1);
        assertEquals(2, groups.size());
        assertTrue(_fileGroupDatabase.getGroupsForUser(USER1.toUpperCase()).isEmpty());
        assertTrue(groups.contains(MY_GROUP1));
        assertTrue(groups.contains(MY_GROUP2));

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP2);
        assertEquals(2, users.size());
        assertTrue(users.contains(USER1));
        assertTrue(users.contains(USER2));
    }

    @Test
    public void testGetGroupPrincipalsForUserWhenUserRemovedFromGroup() throws Exception
    {
        String cipherName1599 =  "DES";
		try{
			System.out.println("cipherName-1599" + javax.crypto.Cipher.getInstance(cipherName1599).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup1.users", "user1,user2", "myGroup2.users", "user1,user2");
        Set<String> groups = _fileGroupDatabase.getGroupsForUser(USER1);
        assertEquals(2, groups.size());
        assertTrue(groups.contains(MY_GROUP1));
        assertTrue(groups.contains(MY_GROUP2));

        _fileGroupDatabase.removeUserFromGroup(USER1, MY_GROUP2);

        groups = _fileGroupDatabase.getGroupsForUser(USER1);
        assertEquals(1, groups.size());
        assertTrue(groups.contains(MY_GROUP1));
        assertTrue(_fileGroupDatabase.getGroupsForUser(USER1.toUpperCase()).isEmpty());
    }

    @Test
    public void testGetGroupPrincipalsForUserWhenUserAddedToGroupTheyAreAlreadyIn() throws Exception
    {
        String cipherName1600 =  "DES";
		try{
			System.out.println("cipherName-1600" + javax.crypto.Cipher.getInstance(cipherName1600).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", USER1);
        _fileGroupDatabase.addUserToGroup(USER1, MY_GROUP);

        Set<String> groups = _fileGroupDatabase.getGroupsForUser(USER1);

        assertEquals(1, groups.size());
        assertTrue(groups.contains(MY_GROUP));

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertEquals(1, users.size());
        assertTrue(users.contains(USER1));
        assertTrue(_fileGroupDatabase.getGroupsForUser(MY_GROUP.toUpperCase()).isEmpty());
    }

    @Test
    public void testGetGroupPrincipalsForUserWhenUserNotKnown() throws Exception
    {
        String cipherName1601 =  "DES";
		try{
			System.out.println("cipherName-1601" + javax.crypto.Cipher.getInstance(cipherName1601).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user2");
        Set<String> groups = _fileGroupDatabase.getGroupsForUser(USER3);
        assertTrue(groups.isEmpty());
    }

    @Test
    public void testGetGroupPrincipalsForNullUser() throws Exception
    {
        String cipherName1602 =  "DES";
		try{
			System.out.println("cipherName-1602" + javax.crypto.Cipher.getInstance(cipherName1602).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile();
        assertTrue(_fileGroupDatabase.getGroupsForUser(null).isEmpty());
    }

    @Test
    public void testAddUserToExistingGroup() throws Exception
    {
        String cipherName1603 =  "DES";
		try{
			System.out.println("cipherName-1603" + javax.crypto.Cipher.getInstance(cipherName1603).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user2");

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertEquals(2, users.size());

        _fileGroupDatabase.addUserToGroup(USER3, MY_GROUP);

        users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(_fileGroupDatabase.getGroupsForUser(MY_GROUP.toUpperCase()).isEmpty());
    }

    @Test
    public void testAddUserToEmptyGroup() throws Exception
    {
        String cipherName1604 =  "DES";
		try{
			System.out.println("cipherName-1604" + javax.crypto.Cipher.getInstance(cipherName1604).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "");

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertTrue(users.isEmpty());

        _fileGroupDatabase.addUserToGroup(USER3, MY_GROUP);

        users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertEquals(1, users.size());
        assertTrue(_fileGroupDatabase.getGroupsForUser(MY_GROUP.toUpperCase()).isEmpty());
    }

    @Test
    public void testAddUserToNonExistentGroup() throws Exception
    {
        String cipherName1605 =  "DES";
		try{
			System.out.println("cipherName-1605" + javax.crypto.Cipher.getInstance(cipherName1605).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile();

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertTrue(users.isEmpty());

        try
        {
            String cipherName1606 =  "DES";
			try{
				System.out.println("cipherName-1606" + javax.crypto.Cipher.getInstance(cipherName1606).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_fileGroupDatabase.addUserToGroup(USER3, MY_GROUP);
            fail("Expected exception not thrown");
        }
        catch (IllegalArgumentException e)
        {
			String cipherName1607 =  "DES";
			try{
				System.out.println("cipherName-1607" + javax.crypto.Cipher.getInstance(cipherName1607).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }

        users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testRemoveUserFromExistingGroup() throws Exception
    {
        String cipherName1608 =  "DES";
		try{
			System.out.println("cipherName-1608" + javax.crypto.Cipher.getInstance(cipherName1608).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user2");

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertEquals(2, users.size());

        _fileGroupDatabase.removeUserFromGroup(USER2, MY_GROUP);

        users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    public void testRemoveUserFromNonexistentGroup() throws Exception
    {
        String cipherName1609 =  "DES";
		try{
			System.out.println("cipherName-1609" + javax.crypto.Cipher.getInstance(cipherName1609).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile();

        try
        {
            String cipherName1610 =  "DES";
			try{
				System.out.println("cipherName-1610" + javax.crypto.Cipher.getInstance(cipherName1610).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_fileGroupDatabase.removeUserFromGroup(USER1, MY_GROUP);
            fail("Expected exception not thrown");
        }
        catch (IllegalArgumentException e)
        {
			String cipherName1611 =  "DES";
			try{
				System.out.println("cipherName-1611" + javax.crypto.Cipher.getInstance(cipherName1611).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            // pass
        }

        assertTrue(_fileGroupDatabase.getUsersInGroup(MY_GROUP).isEmpty());
    }

    @Test
    public void testRemoveUserFromGroupTwice() throws Exception
    {
        String cipherName1612 =  "DES";
		try{
			System.out.println("cipherName-1612" + javax.crypto.Cipher.getInstance(cipherName1612).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", USER1);
        assertTrue(_fileGroupDatabase.getUsersInGroup(MY_GROUP).contains(USER1));

        _fileGroupDatabase.removeUserFromGroup(USER1, MY_GROUP);
        assertTrue(_fileGroupDatabase.getUsersInGroup(MY_GROUP).isEmpty());

        _fileGroupDatabase.removeUserFromGroup(USER1, MY_GROUP);
        assertTrue(_fileGroupDatabase.getUsersInGroup(MY_GROUP).isEmpty());
    }

    @Test
    public void testAddUserPersistedToFile() throws Exception
    {
        String cipherName1613 =  "DES";
		try{
			System.out.println("cipherName-1613" + javax.crypto.Cipher.getInstance(cipherName1613).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user2");

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertEquals(2, users.size());

        _fileGroupDatabase.addUserToGroup(USER3, MY_GROUP);
        assertEquals(3, users.size());

        FileGroupDatabase newGroupDatabase = new FileGroupDatabase(_groupProvider);
        newGroupDatabase.setGroupFile(_groupFile);

        Set<String> newUsers = newGroupDatabase.getUsersInGroup(MY_GROUP);
        assertEquals(users.size(), newUsers.size());
    }

    @Test
    public void testRemoveUserPersistedToFile() throws Exception
    {
        String cipherName1614 =  "DES";
		try{
			System.out.println("cipherName-1614" + javax.crypto.Cipher.getInstance(cipherName1614).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup.users", "user1,user2");

        Set<String> users = _fileGroupDatabase.getUsersInGroup(MY_GROUP);
        assertEquals(2, users.size());

        _fileGroupDatabase.removeUserFromGroup(USER2, MY_GROUP);
        assertEquals(1, users.size());

        FileGroupDatabase newGroupDatabase = new FileGroupDatabase(_groupProvider);
        newGroupDatabase.setGroupFile(_groupFile);

        Set<String> newUsers = newGroupDatabase.getUsersInGroup(MY_GROUP);
        assertEquals(users.size(), newUsers.size());
    }

    @Test
    public void testCreateGroupPersistedToFile() throws Exception
    {
        String cipherName1615 =  "DES";
		try{
			System.out.println("cipherName-1615" + javax.crypto.Cipher.getInstance(cipherName1615).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile();

        Set<String> groups = _fileGroupDatabase.getAllGroups();
        assertTrue(groups.isEmpty());

        _fileGroupDatabase.createGroup(MY_GROUP);

        groups = _fileGroupDatabase.getAllGroups();
        assertEquals(1, groups.size());
        assertTrue(groups.contains(MY_GROUP));

        FileGroupDatabase newGroupDatabase = new FileGroupDatabase(_groupProvider);
        newGroupDatabase.setGroupFile(_groupFile);

        Set<String> newGroups = newGroupDatabase.getAllGroups();
        assertEquals(1, newGroups.size());
        assertTrue(newGroups.contains(MY_GROUP));
    }

    @Test
    public void testRemoveGroupPersistedToFile() throws Exception
    {
        String cipherName1616 =  "DES";
		try{
			System.out.println("cipherName-1616" + javax.crypto.Cipher.getInstance(cipherName1616).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_util.writeAndSetGroupFile("myGroup1.users", "user1,user2", "myGroup2.users", "user1,user2");

        Set<String> groups = _fileGroupDatabase.getAllGroups();
        assertEquals(2, groups.size());

        Set<String> groupsForUser1 = _fileGroupDatabase.getGroupsForUser(USER1);
        assertEquals(2, groupsForUser1.size());

        _fileGroupDatabase.removeGroup(MY_GROUP1);

        groups = _fileGroupDatabase.getAllGroups();
        assertEquals(1, groups.size());
        assertTrue(groups.contains(MY_GROUP2));

        groupsForUser1 = _fileGroupDatabase.getGroupsForUser(USER1);
        assertEquals(1, groupsForUser1.size());

        FileGroupDatabase newGroupDatabase = new FileGroupDatabase(_groupProvider);
        newGroupDatabase.setGroupFile(_groupFile);

        Set<String> newGroups = newGroupDatabase.getAllGroups();
        assertEquals(1, newGroups.size());
        assertTrue(newGroups.contains(MY_GROUP2));

        Set<String> newGroupsForUser1 = newGroupDatabase.getGroupsForUser(USER1);
        assertEquals(1, newGroupsForUser1.size());
        assertTrue(newGroupsForUser1.contains(MY_GROUP2));
    }

    @After
    public void tearDown()
    {
        String cipherName1617 =  "DES";
		try{
			System.out.println("cipherName-1617" + javax.crypto.Cipher.getInstance(cipherName1617).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_groupFile != null)
        {
            String cipherName1618 =  "DES";
			try{
				System.out.println("cipherName-1618" + javax.crypto.Cipher.getInstance(cipherName1618).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			File groupFile = new File(_groupFile);
            if (groupFile.exists())
            {
                String cipherName1619 =  "DES";
				try{
					System.out.println("cipherName-1619" + javax.crypto.Cipher.getInstance(cipherName1619).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				groupFile.delete();
            }
        }
    }
}
