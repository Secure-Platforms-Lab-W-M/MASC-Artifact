/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
pHideExtendedAwayStatus * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.java.sip.communicator.service.protocol;

import java.util.*;

import net.java.sip.communicator.service.protocol.event.*;

/**
 * This operation set provides interface for user search service.
 *
 * @author Hristo Terezov
 */
public interface OperationSetUserSearch extends OperationSet
{
	/**
	 * Creates the search manager.
	 */
	public void createSearchManager();

	/**
	 * Removes search manager.
	 */
	public void removeSearchManager();

	/**
	 * Performs user search for the searched string and returns the contact addresses of the found
	 * contacts.
	 *
	 * @param searchedString
	 *        the text we want to query the server.
	 * @return the list of found contact addresses.
	 */
	public List<CharSequence> search(String searchedString);

	/**
	 * Returns <tt>true</tt> if the user search service is enabled.
	 *
	 * @return <tt>true</tt> if the user search service is enabled.
	 */
	public boolean isEnabled();

	/**
	 * Adds <tt>UserSearchProviderListener</tt> instance to the list of listeners.
	 *
	 * @param l
	 *        the listener to be added
	 */
	public void addUserSearchProviderListener(UserSearchProviderListener l);

	/**
	 * Removes <tt>UserSearchProviderListener</tt> instance from the list of listeners.
	 *
	 * @param l
	 *        the listener to be removed
	 */
	public void removeUserSearchProviderListener(UserSearchProviderListener l);
}
