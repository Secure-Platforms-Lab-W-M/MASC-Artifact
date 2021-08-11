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
package org.apache.qpid.server.security.auth.sasl.scram;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.apache.qpid.server.util.Strings;

class ScramSaslServer implements SaslServer
{
    private static final Charset ASCII = Charset.forName("ASCII");

    private final String _mechanism;
    private final String _hmacName;
    private final String _digestName;
    private final ScramSaslServerSource _authManager;
    private volatile State _state = State.INITIAL;
    private volatile String _nonce;
    private volatile String _username;
    private volatile byte[] _gs2Header;
    private volatile String _serverFirstMessage;
    private volatile String _clientFirstMessageBare;
    private volatile byte[] _serverSignature;
    private volatile ScramSaslServerSource.SaltAndPasswordKeys _saltAndPassword;

    ScramSaslServer(final ScramSaslServerSource authenticationManager,
                    final String mechanism)
    {
        String cipherName7343 =  "DES";
		try{
			System.out.println("cipherName-7343" + javax.crypto.Cipher.getInstance(cipherName7343).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_authManager = authenticationManager;
        _mechanism = mechanism;
        _hmacName = authenticationManager.getHmacName();
        _digestName = authenticationManager.getDigestName();
    }

    enum State
    {
        INITIAL,
        SERVER_FIRST_MESSAGE_SENT,
        COMPLETE
    }

    @Override
    public String getMechanismName()
    {
        String cipherName7344 =  "DES";
		try{
			System.out.println("cipherName-7344" + javax.crypto.Cipher.getInstance(cipherName7344).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _mechanism;
    }

    @Override
    public byte[] evaluateResponse(final byte[] response) throws SaslException
    {
        String cipherName7345 =  "DES";
		try{
			System.out.println("cipherName-7345" + javax.crypto.Cipher.getInstance(cipherName7345).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		byte[] challenge;
        switch (_state)
        {
            case INITIAL:
                challenge = generateServerFirstMessage(response);
                _state = State.SERVER_FIRST_MESSAGE_SENT;
                break;
            case SERVER_FIRST_MESSAGE_SENT:
                challenge = generateServerFinalMessage(response);
                _state = State.COMPLETE;
                break;
            case COMPLETE:
                if(response == null || response.length == 0)
                {
                    String cipherName7346 =  "DES";
					try{
						System.out.println("cipherName-7346" + javax.crypto.Cipher.getInstance(cipherName7346).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					challenge = new byte[0];
                    break;
                }
            default:
                throw new SaslException("No response expected in state " + _state);

        }
        return challenge;
    }

    private byte[] generateServerFirstMessage(final byte[] response) throws SaslException
    {
        String cipherName7347 =  "DES";
		try{
			System.out.println("cipherName-7347" + javax.crypto.Cipher.getInstance(cipherName7347).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String clientFirstMessage = new String(response, ASCII);
        if(!clientFirstMessage.startsWith("n"))
        {
            String cipherName7348 =  "DES";
			try{
				System.out.println("cipherName-7348" + javax.crypto.Cipher.getInstance(cipherName7348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SaslException("Cannot parse gs2-header");
        }
        String[] parts = clientFirstMessage.split(",");
        if(parts.length < 4)
        {
            String cipherName7349 =  "DES";
			try{
				System.out.println("cipherName-7349" + javax.crypto.Cipher.getInstance(cipherName7349).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SaslException("Cannot parse client first message");
        }
        _gs2Header = ("n,"+parts[1]+",").getBytes(ASCII);
        _clientFirstMessageBare = clientFirstMessage.substring(_gs2Header.length);
        if(!parts[2].startsWith("n="))
        {
            String cipherName7350 =  "DES";
			try{
				System.out.println("cipherName-7350" + javax.crypto.Cipher.getInstance(cipherName7350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SaslException("Cannot parse client first message");
        }
        _username = decodeUsername(parts[2].substring(2));
        if(!parts[3].startsWith("r="))
        {
            String cipherName7351 =  "DES";
			try{
				System.out.println("cipherName-7351" + javax.crypto.Cipher.getInstance(cipherName7351).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SaslException("Cannot parse client first message");
        }
        _nonce = parts[3].substring(2) + UUID.randomUUID().toString();

        _saltAndPassword = _authManager.getSaltAndPasswordKeys(_username);
        _serverFirstMessage = "r=" + _nonce + ",s=" + Base64.getEncoder().encodeToString(_saltAndPassword.getSalt()) + ",i=" + _saltAndPassword.getIterationCount();
        return _serverFirstMessage.getBytes(ASCII);
    }

    private String decodeUsername(String username) throws SaslException
    {
        String cipherName7352 =  "DES";
		try{
			System.out.println("cipherName-7352" + javax.crypto.Cipher.getInstance(cipherName7352).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(username.contains("="))
        {
            String cipherName7353 =  "DES";
			try{
				System.out.println("cipherName-7353" + javax.crypto.Cipher.getInstance(cipherName7353).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String check = username;
            while (check.contains("="))
            {
                String cipherName7354 =  "DES";
				try{
					System.out.println("cipherName-7354" + javax.crypto.Cipher.getInstance(cipherName7354).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				check = check.substring(check.indexOf('=') + 1);
                if (!(check.startsWith("2C") || check.startsWith("3D")))
                {
                    String cipherName7355 =  "DES";
					try{
						System.out.println("cipherName-7355" + javax.crypto.Cipher.getInstance(cipherName7355).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new SaslException("Invalid username");
                }
            }
            username = username.replace("=2C", ",");
            username = username.replace("=3D","=");
        }
        return username;
    }


    private byte[] generateServerFinalMessage(final byte[] response) throws SaslException
    {
        String cipherName7356 =  "DES";
		try{
			System.out.println("cipherName-7356" + javax.crypto.Cipher.getInstance(cipherName7356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7357 =  "DES";
			try{
				System.out.println("cipherName-7357" + javax.crypto.Cipher.getInstance(cipherName7357).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String clientFinalMessage = new String(response, ASCII);
            String[] parts = clientFinalMessage.split(",");
            if(!parts[0].startsWith("c="))
            {
                String cipherName7358 =  "DES";
				try{
					System.out.println("cipherName-7358" + javax.crypto.Cipher.getInstance(cipherName7358).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new SaslException("Cannot parse client final message");
            }
            if(!Arrays.equals(_gs2Header, Strings.decodeBase64(parts[0].substring(2))))
            {
                String cipherName7359 =  "DES";
				try{
					System.out.println("cipherName-7359" + javax.crypto.Cipher.getInstance(cipherName7359).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new SaslException("Client final message channel bind data invalid");
            }
            if(!parts[1].startsWith("r="))
            {
                String cipherName7360 =  "DES";
				try{
					System.out.println("cipherName-7360" + javax.crypto.Cipher.getInstance(cipherName7360).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new SaslException("Cannot parse client final message");
            }
            if(!parts[1].substring(2).equals(_nonce))
            {
                String cipherName7361 =  "DES";
				try{
					System.out.println("cipherName-7361" + javax.crypto.Cipher.getInstance(cipherName7361).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new SaslException("Client final message has incorrect nonce value");
            }
            if(!parts[parts.length-1].startsWith("p="))
            {
                String cipherName7362 =  "DES";
				try{
					System.out.println("cipherName-7362" + javax.crypto.Cipher.getInstance(cipherName7362).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new SaslException("Client final message does not have proof");
            }

            String clientFinalMessageWithoutProof = clientFinalMessage.substring(0,clientFinalMessage.length()-(1+parts[parts.length-1].length()));
            byte[] proofBytes = Strings.decodeBase64(parts[parts.length-1].substring(2));

            String authMessage = _clientFirstMessageBare + "," + _serverFirstMessage + "," + clientFinalMessageWithoutProof;


            byte[] storedKey = _saltAndPassword.getStoredKey();

            byte[] clientSignature = computeHmac(storedKey, authMessage);

            for(int i = 0 ; i < proofBytes.length; i++)
            {
                String cipherName7363 =  "DES";
				try{
					System.out.println("cipherName-7363" + javax.crypto.Cipher.getInstance(cipherName7363).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				proofBytes[i] ^= clientSignature[i];
            }

            final byte[] storedKeyFromClient = MessageDigest.getInstance(_digestName).digest(proofBytes);

            if(!Arrays.equals(storedKeyFromClient, storedKey))
            {
                String cipherName7364 =  "DES";
				try{
					System.out.println("cipherName-7364" + javax.crypto.Cipher.getInstance(cipherName7364).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new SaslException("Authentication failed");
            }

            byte[] serverKey = _saltAndPassword.getServerKey();
            String finalResponse = "v=" + Base64.getEncoder().encodeToString(computeHmac(serverKey, authMessage));

            return finalResponse.getBytes(ASCII);
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            String cipherName7365 =  "DES";
			try{
				System.out.println("cipherName-7365" + javax.crypto.Cipher.getInstance(cipherName7365).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SaslException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isComplete()
    {
        String cipherName7366 =  "DES";
		try{
			System.out.println("cipherName-7366" + javax.crypto.Cipher.getInstance(cipherName7366).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _state == State.COMPLETE;
    }

    @Override
    public String getAuthorizationID()
    {
        String cipherName7367 =  "DES";
		try{
			System.out.println("cipherName-7367" + javax.crypto.Cipher.getInstance(cipherName7367).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _username;
    }

    @Override
    public byte[] unwrap(final byte[] incoming, final int offset, final int len) throws SaslException
    {
        String cipherName7368 =  "DES";
		try{
			System.out.println("cipherName-7368" + javax.crypto.Cipher.getInstance(cipherName7368).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		throw new IllegalStateException("No security layer supported");
    }

    @Override
    public byte[] wrap(final byte[] outgoing, final int offset, final int len) throws SaslException
    {
        String cipherName7369 =  "DES";
		try{
			System.out.println("cipherName-7369" + javax.crypto.Cipher.getInstance(cipherName7369).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		throw new IllegalStateException("No security layer supported");
    }

    @Override
    public Object getNegotiatedProperty(final String propName)
    {
        String cipherName7370 =  "DES";
		try{
			System.out.println("cipherName-7370" + javax.crypto.Cipher.getInstance(cipherName7370).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return null;
    }

    @Override
    public void dispose() throws SaslException
    {
		String cipherName7371 =  "DES";
		try{
			System.out.println("cipherName-7371" + javax.crypto.Cipher.getInstance(cipherName7371).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

    }

    private byte[] computeHmac(final byte[] key, final String string)
            throws SaslException, UnsupportedEncodingException
    {
        String cipherName7372 =  "DES";
		try{
			System.out.println("cipherName-7372" + javax.crypto.Cipher.getInstance(cipherName7372).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Mac mac = createShaHmac(key);
        mac.update(string.getBytes(ASCII));
        return mac.doFinal();
    }


    private Mac createShaHmac(final byte[] keyBytes)
            throws SaslException
    {
        String cipherName7373 =  "DES";
		try{
			System.out.println("cipherName-7373" + javax.crypto.Cipher.getInstance(cipherName7373).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try
        {
            String cipherName7374 =  "DES";
			try{
				System.out.println("cipherName-7374" + javax.crypto.Cipher.getInstance(cipherName7374).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SecretKeySpec key = new SecretKeySpec(keyBytes, _hmacName);
            Mac mac = Mac.getInstance(_hmacName);
            mac.init(key);
            return mac;
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e)
        {
            String cipherName7375 =  "DES";
			try{
				System.out.println("cipherName-7375" + javax.crypto.Cipher.getInstance(cipherName7375).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new SaslException(e.getMessage(), e);
        }
    }

}
