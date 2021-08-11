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
package org.apache.qpid.server.connection;

import java.net.SocketAddress;
import java.util.UUID;

import org.apache.qpid.server.model.Port;
import org.apache.qpid.server.model.Protocol;
import org.apache.qpid.server.model.Transport;
import org.apache.qpid.server.security.auth.SocketConnectionPrincipal;
import org.apache.qpid.server.transport.AMQPConnection;

public class ConnectionPrincipal implements SocketConnectionPrincipal
{
    private static final long serialVersionUID = 1L;

    private final AMQPConnection<?> _connection;
    private AmqpConnectionMetaData _metadata;

    public ConnectionPrincipal(final AMQPConnection<?> connection)
    {
        String cipherName6350 =  "DES";
		try{
			System.out.println("cipherName-6350" + javax.crypto.Cipher.getInstance(cipherName6350).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_connection = connection;
        _metadata = new ConnectionMetaData(connection);
    }

    @Override
    public String getName()
    {
        String cipherName6351 =  "DES";
		try{
			System.out.println("cipherName-6351" + javax.crypto.Cipher.getInstance(cipherName6351).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _connection.getRemoteAddressString();
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        String cipherName6352 =  "DES";
		try{
			System.out.println("cipherName-6352" + javax.crypto.Cipher.getInstance(cipherName6352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _connection.getRemoteSocketAddress();
    }

    @Override
    public AmqpConnectionMetaData getConnectionMetaData()
    {
        String cipherName6353 =  "DES";
		try{
			System.out.println("cipherName-6353" + javax.crypto.Cipher.getInstance(cipherName6353).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _metadata;
    }

    public AMQPConnection<?> getConnection()
    {
        String cipherName6354 =  "DES";
		try{
			System.out.println("cipherName-6354" + javax.crypto.Cipher.getInstance(cipherName6354).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _connection;
    }

    @Override
    public boolean equals(final Object o)
    {
        String cipherName6355 =  "DES";
		try{
			System.out.println("cipherName-6355" + javax.crypto.Cipher.getInstance(cipherName6355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (this == o)
        {
            String cipherName6356 =  "DES";
			try{
				System.out.println("cipherName-6356" + javax.crypto.Cipher.getInstance(cipherName6356).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            String cipherName6357 =  "DES";
			try{
				System.out.println("cipherName-6357" + javax.crypto.Cipher.getInstance(cipherName6357).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        final ConnectionPrincipal that = (ConnectionPrincipal) o;

        if (!_connection.equals(that._connection))
        {
            String cipherName6358 =  "DES";
			try{
				System.out.println("cipherName-6358" + javax.crypto.Cipher.getInstance(cipherName6358).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        String cipherName6359 =  "DES";
		try{
			System.out.println("cipherName-6359" + javax.crypto.Cipher.getInstance(cipherName6359).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _connection.hashCode();
    }

    private static class ConnectionMetaData implements AmqpConnectionMetaData
    {
        private final AMQPConnection<?> _connection;

        ConnectionMetaData(final AMQPConnection<?> connection)
        {
            String cipherName6360 =  "DES";
			try{
				System.out.println("cipherName-6360" + javax.crypto.Cipher.getInstance(cipherName6360).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_connection = connection;
        }

        @Override
        public UUID getConnectionId()
        {
            String cipherName6361 =  "DES";
			try{
				System.out.println("cipherName-6361" + javax.crypto.Cipher.getInstance(cipherName6361).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getId();
        }

        @Override
        public Port getPort()
        {
            String cipherName6362 =  "DES";
			try{
				System.out.println("cipherName-6362" + javax.crypto.Cipher.getInstance(cipherName6362).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getPort();
        }

        @Override
        public String getLocalAddress()
        {
            String cipherName6363 =  "DES";
			try{
				System.out.println("cipherName-6363" + javax.crypto.Cipher.getInstance(cipherName6363).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getLocalAddress();
        }

        @Override
        public String getRemoteAddress()
        {
            String cipherName6364 =  "DES";
			try{
				System.out.println("cipherName-6364" + javax.crypto.Cipher.getInstance(cipherName6364).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getRemoteAddress();
        }

        @Override
        public Protocol getProtocol()
        {
            String cipherName6365 =  "DES";
			try{
				System.out.println("cipherName-6365" + javax.crypto.Cipher.getInstance(cipherName6365).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getProtocol();
        }

        @Override
        public Transport getTransport()
        {
            String cipherName6366 =  "DES";
			try{
				System.out.println("cipherName-6366" + javax.crypto.Cipher.getInstance(cipherName6366).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getTransport();
        }

        @Override
        public String getClientId()
        {
            String cipherName6367 =  "DES";
			try{
				System.out.println("cipherName-6367" + javax.crypto.Cipher.getInstance(cipherName6367).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getClientId();
        }

        @Override
        public String getClientVersion()
        {
            String cipherName6368 =  "DES";
			try{
				System.out.println("cipherName-6368" + javax.crypto.Cipher.getInstance(cipherName6368).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getClientVersion();
        }

        @Override
        public String getClientProduct()
        {
            String cipherName6369 =  "DES";
			try{
				System.out.println("cipherName-6369" + javax.crypto.Cipher.getInstance(cipherName6369).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _connection.getClientProduct();
        }
    }
}
