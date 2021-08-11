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
 */

package org.apache.qpid.server.transport;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.model.port.AmqpPort;
import org.apache.qpid.server.transport.network.security.ssl.SSLUtil;
import org.apache.qpid.server.util.ServerScopedRuntimeException;

public class NonBlockingConnectionTLSDelegate implements NonBlockingConnectionDelegate
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NonBlockingConnectionTLSDelegate.class);

    private final SSLEngine _sslEngine;
    private final NonBlockingConnection _parent;
    private final int _networkBufferSize;
    private SSLEngineResult _status;
    private final List<QpidByteBuffer> _encryptedOutput = new ArrayList<>();
    private Principal _principal;
    private Certificate _peerCertificate;
    private boolean _principalChecked;
    private volatile boolean _hostChecked;
    private QpidByteBuffer _netInputBuffer;
    private QpidByteBuffer _netOutputBuffer;
    private QpidByteBuffer _applicationBuffer;


    public NonBlockingConnectionTLSDelegate(NonBlockingConnection parent, AmqpPort port)
    {
        String cipherName5353 =  "DES";
		try{
			System.out.println("cipherName-5353" + javax.crypto.Cipher.getInstance(cipherName5353).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_parent = parent;
        _sslEngine = createSSLEngine(port);
        _networkBufferSize = port.getNetworkBufferSize();

        final int tlsPacketBufferSize = _sslEngine.getSession().getPacketBufferSize();
        if (tlsPacketBufferSize > _networkBufferSize)
        {
            String cipherName5354 =  "DES";
			try{
				System.out.println("cipherName-5354" + javax.crypto.Cipher.getInstance(cipherName5354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException("TLS implementation packet buffer size (" + tlsPacketBufferSize
                    + ") is greater then broker network buffer size (" + _networkBufferSize + ")");
        }

        _netInputBuffer = QpidByteBuffer.allocateDirect(_networkBufferSize);
        _applicationBuffer = QpidByteBuffer.allocateDirect(_networkBufferSize);
        _netOutputBuffer = QpidByteBuffer.allocateDirect(_networkBufferSize);
    }

    @Override
    public boolean readyForRead()
    {
        String cipherName5355 =  "DES";
		try{
			System.out.println("cipherName-5355" + javax.crypto.Cipher.getInstance(cipherName5355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _sslEngine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NEED_WRAP;
    }

    @Override
    public boolean processData() throws IOException
    {
        String cipherName5356 =  "DES";
		try{
			System.out.println("cipherName-5356" + javax.crypto.Cipher.getInstance(cipherName5356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(!_hostChecked)
        {
            String cipherName5357 =  "DES";
			try{
				System.out.println("cipherName-5357" + javax.crypto.Cipher.getInstance(cipherName5357).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try (QpidByteBuffer buffer = _netInputBuffer.duplicate())
            {
                String cipherName5358 =  "DES";
				try{
					System.out.println("cipherName-5358" + javax.crypto.Cipher.getInstance(cipherName5358).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				buffer.flip();
                if (SSLUtil.isSufficientToDetermineClientSNIHost(buffer))
                {
                    String cipherName5359 =  "DES";
					try{
						System.out.println("cipherName-5359" + javax.crypto.Cipher.getInstance(cipherName5359).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String hostName = SSLUtil.getServerNameFromTLSClientHello(buffer);
                    if (hostName != null)
                    {
                        String cipherName5360 =  "DES";
						try{
							System.out.println("cipherName-5360" + javax.crypto.Cipher.getInstance(cipherName5360).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_parent.setSelectedHost(hostName);
                        SSLParameters sslParameters = _sslEngine.getSSLParameters();
                        sslParameters.setServerNames(Collections.singletonList(new SNIHostName(hostName)));
                        _sslEngine.setSSLParameters(sslParameters);
                    }
                    _hostChecked = true;
                }
                else
                {
                    String cipherName5361 =  "DES";
					try{
						System.out.println("cipherName-5361" + javax.crypto.Cipher.getInstance(cipherName5361).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }
        }
        _netInputBuffer.flip();
        boolean readData = false;
        boolean tasksRun;
        int oldNetBufferPos;
        do
        {
            String cipherName5362 =  "DES";
			try{
				System.out.println("cipherName-5362" + javax.crypto.Cipher.getInstance(cipherName5362).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int oldAppBufPos = _applicationBuffer.position();
            oldNetBufferPos = _netInputBuffer.position();

            _status = QpidByteBuffer.decryptSSL(_sslEngine, _netInputBuffer, _applicationBuffer);
            if (_status.getStatus() == SSLEngineResult.Status.CLOSED)
            {
                String cipherName5363 =  "DES";
				try{
					System.out.println("cipherName-5363" + javax.crypto.Cipher.getInstance(cipherName5363).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int remaining = _netInputBuffer.remaining();
                _netInputBuffer.position(_netInputBuffer.limit());
                // We'd usually expect no more bytes to be sent following a close_notify
                LOGGER.debug("SSLEngine closed, discarded {} byte(s)", remaining);
            }

            tasksRun = runSSLEngineTasks(_status);
            _applicationBuffer.flip();
            if(_applicationBuffer.position() > oldAppBufPos)
            {
                String cipherName5364 =  "DES";
				try{
					System.out.println("cipherName-5364" + javax.crypto.Cipher.getInstance(cipherName5364).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				readData = true;
            }

            _parent.processAmqpData(_applicationBuffer);

            restoreApplicationBufferForWrite();

        }
        while((_netInputBuffer.hasRemaining() && (_netInputBuffer.position()>oldNetBufferPos)) || tasksRun);

        if(_netInputBuffer.hasRemaining())
        {
            String cipherName5365 =  "DES";
			try{
				System.out.println("cipherName-5365" + javax.crypto.Cipher.getInstance(cipherName5365).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_netInputBuffer.compact();
        }
        else
        {
            String cipherName5366 =  "DES";
			try{
				System.out.println("cipherName-5366" + javax.crypto.Cipher.getInstance(cipherName5366).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_netInputBuffer.clear();
        }
        return readData;
    }

    @Override
    public WriteResult doWrite(Collection<QpidByteBuffer> buffers) throws IOException
    {
        String cipherName5367 =  "DES";
		try{
			System.out.println("cipherName-5367" + javax.crypto.Cipher.getInstance(cipherName5367).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final int bufCount = buffers.size();

        int totalConsumed = wrapBufferArray(buffers);

        boolean bufsSent = true;
        final Iterator<QpidByteBuffer> itr = buffers.iterator();
        int bufIndex = 0;
        while(itr.hasNext() && bufsSent && bufIndex++ < bufCount)
        {
            String cipherName5368 =  "DES";
			try{
				System.out.println("cipherName-5368" + javax.crypto.Cipher.getInstance(cipherName5368).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			QpidByteBuffer buf = itr.next();
            bufsSent = !buf.hasRemaining();
        }

        if(!_encryptedOutput.isEmpty())
        {
            String cipherName5369 =  "DES";
			try{
				System.out.println("cipherName-5369" + javax.crypto.Cipher.getInstance(cipherName5369).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_parent.writeToTransport(_encryptedOutput);

            ListIterator<QpidByteBuffer> iter = _encryptedOutput.listIterator();
            while (iter.hasNext())
            {
                String cipherName5370 =  "DES";
				try{
					System.out.println("cipherName-5370" + javax.crypto.Cipher.getInstance(cipherName5370).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				QpidByteBuffer buf = iter.next();
                if (!buf.hasRemaining())
                {
                    String cipherName5371 =  "DES";
					try{
						System.out.println("cipherName-5371" + javax.crypto.Cipher.getInstance(cipherName5371).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					buf.dispose();
                    iter.remove();
                }
                else
                {
                    String cipherName5372 =  "DES";
					try{
						System.out.println("cipherName-5372" + javax.crypto.Cipher.getInstance(cipherName5372).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					break;
                }
            }
        }
        return new WriteResult(bufsSent && _encryptedOutput.isEmpty(), totalConsumed);
    }

    protected void restoreApplicationBufferForWrite()
    {
        String cipherName5373 =  "DES";
		try{
			System.out.println("cipherName-5373" + javax.crypto.Cipher.getInstance(cipherName5373).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try (QpidByteBuffer oldApplicationBuffer = _applicationBuffer)
        {
            String cipherName5374 =  "DES";
			try{
				System.out.println("cipherName-5374" + javax.crypto.Cipher.getInstance(cipherName5374).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int unprocessedDataLength = _applicationBuffer.remaining();
            _applicationBuffer.limit(_applicationBuffer.capacity());
            _applicationBuffer = _applicationBuffer.slice();
            _applicationBuffer.limit(unprocessedDataLength);
        }
        if (_applicationBuffer.limit() <= _applicationBuffer.capacity() - _sslEngine.getSession().getApplicationBufferSize())
        {
            String cipherName5375 =  "DES";
			try{
				System.out.println("cipherName-5375" + javax.crypto.Cipher.getInstance(cipherName5375).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_applicationBuffer.position(_applicationBuffer.limit());
            _applicationBuffer.limit(_applicationBuffer.capacity());
        }
        else
        {
            String cipherName5376 =  "DES";
			try{
				System.out.println("cipherName-5376" + javax.crypto.Cipher.getInstance(cipherName5376).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try (QpidByteBuffer currentBuffer = _applicationBuffer)
            {
                String cipherName5377 =  "DES";
				try{
					System.out.println("cipherName-5377" + javax.crypto.Cipher.getInstance(cipherName5377).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int newBufSize;
                if (currentBuffer.capacity() < _networkBufferSize)
                {
                    String cipherName5378 =  "DES";
					try{
						System.out.println("cipherName-5378" + javax.crypto.Cipher.getInstance(cipherName5378).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					newBufSize = _networkBufferSize;
                }
                else
                {
                    String cipherName5379 =  "DES";
					try{
						System.out.println("cipherName-5379" + javax.crypto.Cipher.getInstance(cipherName5379).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					newBufSize = currentBuffer.capacity() + _networkBufferSize;
                    _parent.reportUnexpectedByteBufferSizeUsage();
                }

                _applicationBuffer = QpidByteBuffer.allocateDirect(newBufSize);
                _applicationBuffer.put(currentBuffer);
            }
        }

    }

    private int wrapBufferArray(Collection<QpidByteBuffer> buffers) throws SSLException
    {
        String cipherName5380 =  "DES";
		try{
			System.out.println("cipherName-5380" + javax.crypto.Cipher.getInstance(cipherName5380).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int totalConsumed = 0;
        boolean encrypted;
        do
        {
            String cipherName5381 =  "DES";
			try{
				System.out.println("cipherName-5381" + javax.crypto.Cipher.getInstance(cipherName5381).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if(_sslEngine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
            {
                String cipherName5382 =  "DES";
				try{
					System.out.println("cipherName-5382" + javax.crypto.Cipher.getInstance(cipherName5382).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if(_netOutputBuffer.remaining() < _sslEngine.getSession().getPacketBufferSize())
                {
                    String cipherName5383 =  "DES";
					try{
						System.out.println("cipherName-5383" + javax.crypto.Cipher.getInstance(cipherName5383).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if(_netOutputBuffer.position() != 0)
                    {
                        String cipherName5384 =  "DES";
						try{
							System.out.println("cipherName-5384" + javax.crypto.Cipher.getInstance(cipherName5384).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_netOutputBuffer.flip();
                        _encryptedOutput.add(_netOutputBuffer);
                    }
                    else
                    {
                        String cipherName5385 =  "DES";
						try{
							System.out.println("cipherName-5385" + javax.crypto.Cipher.getInstance(cipherName5385).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						_netOutputBuffer.dispose();
                    }
                    _netOutputBuffer = QpidByteBuffer.allocateDirect(_networkBufferSize);
                }

                _status = QpidByteBuffer.encryptSSL(_sslEngine, buffers, _netOutputBuffer);
                encrypted = _status.bytesProduced() > 0;
                totalConsumed += _status.bytesConsumed();
                runSSLEngineTasks(_status);
                if(encrypted && _netOutputBuffer.remaining() < _sslEngine.getSession().getPacketBufferSize())
                {
                    String cipherName5386 =  "DES";
					try{
						System.out.println("cipherName-5386" + javax.crypto.Cipher.getInstance(cipherName5386).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_netOutputBuffer.flip();
                    _encryptedOutput.add(_netOutputBuffer);
                    _netOutputBuffer = QpidByteBuffer.allocateDirect(_networkBufferSize);
                }

            }
            else
            {
                String cipherName5387 =  "DES";
				try{
					System.out.println("cipherName-5387" + javax.crypto.Cipher.getInstance(cipherName5387).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				encrypted = false;
            }

        }
        while(encrypted && _sslEngine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NEED_UNWRAP);

        if(_netOutputBuffer.position() != 0)
        {
            String cipherName5388 =  "DES";
			try{
				System.out.println("cipherName-5388" + javax.crypto.Cipher.getInstance(cipherName5388).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final QpidByteBuffer outputBuffer = _netOutputBuffer;

            _netOutputBuffer = _netOutputBuffer.slice();

            outputBuffer.flip();
            _encryptedOutput.add(outputBuffer);

        }
        return totalConsumed;
    }

    private boolean runSSLEngineTasks(final SSLEngineResult status)
    {
        String cipherName5389 =  "DES";
		try{
			System.out.println("cipherName-5389" + javax.crypto.Cipher.getInstance(cipherName5389).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if(status.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK)
        {
            String cipherName5390 =  "DES";
			try{
				System.out.println("cipherName-5390" + javax.crypto.Cipher.getInstance(cipherName5390).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Runnable task;
            while((task = _sslEngine.getDelegatedTask()) != null)
            {
                String cipherName5391 =  "DES";
				try{
					System.out.println("cipherName-5391" + javax.crypto.Cipher.getInstance(cipherName5391).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				task.run();
            }

            return true;
        }

        return false;
    }

    @Override
    public Principal getPeerPrincipal()
    {
        String cipherName5392 =  "DES";
		try{
			System.out.println("cipherName-5392" + javax.crypto.Cipher.getInstance(cipherName5392).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		checkPeerPrincipal();
        return _principal;
    }

    @Override
    public Certificate getPeerCertificate()
    {
        String cipherName5393 =  "DES";
		try{
			System.out.println("cipherName-5393" + javax.crypto.Cipher.getInstance(cipherName5393).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		checkPeerPrincipal();
        return _peerCertificate;
    }

    @Override
    public boolean needsWork()
    {
        String cipherName5394 =  "DES";
		try{
			System.out.println("cipherName-5394" + javax.crypto.Cipher.getInstance(cipherName5394).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _sslEngine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
    }

    private synchronized void checkPeerPrincipal()
    {
        String cipherName5395 =  "DES";
		try{
			System.out.println("cipherName-5395" + javax.crypto.Cipher.getInstance(cipherName5395).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!_principalChecked)
        {
            String cipherName5396 =  "DES";
			try{
				System.out.println("cipherName-5396" + javax.crypto.Cipher.getInstance(cipherName5396).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName5397 =  "DES";
				try{
					System.out.println("cipherName-5397" + javax.crypto.Cipher.getInstance(cipherName5397).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_principal = _sslEngine.getSession().getPeerPrincipal();
                final Certificate[] peerCertificates =
                        _sslEngine.getSession().getPeerCertificates();
                if (peerCertificates != null && peerCertificates.length > 0)
                {
                    String cipherName5398 =  "DES";
					try{
						System.out.println("cipherName-5398" + javax.crypto.Cipher.getInstance(cipherName5398).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					_peerCertificate = peerCertificates[0];
                }
            }
            catch (SSLPeerUnverifiedException e)
            {
                String cipherName5399 =  "DES";
				try{
					System.out.println("cipherName-5399" + javax.crypto.Cipher.getInstance(cipherName5399).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				_principal = null;
                _peerCertificate = null;
            }

            _principalChecked = true;
        }
    }

    private SSLEngine createSSLEngine(AmqpPort<?> port)
    {
        String cipherName5400 =  "DES";
		try{
			System.out.println("cipherName-5400" + javax.crypto.Cipher.getInstance(cipherName5400).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SSLEngine sslEngine = port.getSSLContext().createSSLEngine();
        sslEngine.setUseClientMode(false);
        SSLUtil.updateEnabledTlsProtocols(sslEngine, port.getTlsProtocolWhiteList(), port.getTlsProtocolBlackList());
        SSLUtil.updateEnabledCipherSuites(sslEngine, port.getTlsCipherSuiteWhiteList(), port.getTlsCipherSuiteBlackList());
        if(port.getTlsCipherSuiteWhiteList() != null && !port.getTlsCipherSuiteWhiteList().isEmpty())
        {
            String cipherName5401 =  "DES";
			try{
				System.out.println("cipherName-5401" + javax.crypto.Cipher.getInstance(cipherName5401).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			SSLParameters sslParameters = sslEngine.getSSLParameters();
            sslParameters.setUseCipherSuitesOrder(true);
            sslEngine.setSSLParameters(sslParameters);
        }

        if(port.getNeedClientAuth())
        {
            String cipherName5402 =  "DES";
			try{
				System.out.println("cipherName-5402" + javax.crypto.Cipher.getInstance(cipherName5402).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sslEngine.setNeedClientAuth(true);
        }
        else if(port.getWantClientAuth())
        {
            String cipherName5403 =  "DES";
			try{
				System.out.println("cipherName-5403" + javax.crypto.Cipher.getInstance(cipherName5403).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sslEngine.setWantClientAuth(true);
        }
        return sslEngine;
    }

    @Override
    public QpidByteBuffer getNetInputBuffer()
    {
        String cipherName5404 =  "DES";
		try{
			System.out.println("cipherName-5404" + javax.crypto.Cipher.getInstance(cipherName5404).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _netInputBuffer;
    }

    @Override
    public void shutdownInput()
    {
        String cipherName5405 =  "DES";
		try{
			System.out.println("cipherName-5405" + javax.crypto.Cipher.getInstance(cipherName5405).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_netInputBuffer != null)
        {
            String cipherName5406 =  "DES";
			try{
				System.out.println("cipherName-5406" + javax.crypto.Cipher.getInstance(cipherName5406).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_netInputBuffer.dispose();
            _netInputBuffer = null;
        }

        if (_applicationBuffer != null)
        {
            String cipherName5407 =  "DES";
			try{
				System.out.println("cipherName-5407" + javax.crypto.Cipher.getInstance(cipherName5407).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_applicationBuffer.dispose();
            _applicationBuffer = null;
        }
    }

    @Override
    public void shutdownOutput()
    {

        String cipherName5408 =  "DES";
		try{
			System.out.println("cipherName-5408" + javax.crypto.Cipher.getInstance(cipherName5408).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (_netOutputBuffer != null)
        {
            String cipherName5409 =  "DES";
			try{
				System.out.println("cipherName-5409" + javax.crypto.Cipher.getInstance(cipherName5409).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_netOutputBuffer.dispose();
            _netOutputBuffer = null;
        }
        try
        {
            String cipherName5410 =  "DES";
			try{
				System.out.println("cipherName-5410" + javax.crypto.Cipher.getInstance(cipherName5410).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_sslEngine.closeOutbound();
            _sslEngine.closeInbound();
        }
        catch (SSLException e)
        {
            String cipherName5411 =  "DES";
			try{
				System.out.println("cipherName-5411" + javax.crypto.Cipher.getInstance(cipherName5411).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			LOGGER.debug("Exception when closing SSLEngine", e);
        }

    }

    @Override
    public String getTransportInfo()
    {
        String cipherName5412 =  "DES";
		try{
			System.out.println("cipherName-5412" + javax.crypto.Cipher.getInstance(cipherName5412).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SSLSession session = _sslEngine.getSession();
        return session.getProtocol() + " ; " + session.getCipherSuite() ;
    }
}
