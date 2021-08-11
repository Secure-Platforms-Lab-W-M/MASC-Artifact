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
package org.apache.qpid.server.store;

import org.apache.qpid.server.bytebuffer.QpidByteBuffer;
import org.apache.qpid.server.message.AMQMessageHeader;
import org.apache.qpid.server.message.MessageReference;
import org.apache.qpid.server.message.ServerMessage;
import org.apache.qpid.server.plugin.MessageMetaDataType;

public class TestMessageMetaDataType implements MessageMetaDataType<TestMessageMetaData>
{
    //largest metadata type value the BDBMessageStore can store (it uses a byte)
    private static final byte TYPE = 7;
    public static final String V0_8 = "v0_8";

    @Override
    public int ordinal()
    {
        String cipherName3773 =  "DES";
		try{
			System.out.println("cipherName-3773" + javax.crypto.Cipher.getInstance(cipherName3773).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return TYPE;
    }

    @Override
    public TestMessageMetaData createMetaData(QpidByteBuffer buf)
    {
        String cipherName3774 =  "DES";
		try{
			System.out.println("cipherName-3774" + javax.crypto.Cipher.getInstance(cipherName3774).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return TestMessageMetaData.FACTORY.createMetaData(buf);
    }

    @Override
    public ServerMessage<TestMessageMetaData> createMessage(StoredMessage<TestMessageMetaData> msg)
    {
        String cipherName3775 =  "DES";
		try{
			System.out.println("cipherName-3775" + javax.crypto.Cipher.getInstance(cipherName3775).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new TestServerMessage(msg);
    }

    @Override
    public int hashCode()
    {
        String cipherName3776 =  "DES";
		try{
			System.out.println("cipherName-3776" + javax.crypto.Cipher.getInstance(cipherName3776).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ordinal();
    }

    @Override
    public boolean equals(Object o)
    {
        String cipherName3777 =  "DES";
		try{
			System.out.println("cipherName-3777" + javax.crypto.Cipher.getInstance(cipherName3777).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return o != null && o.getClass() == getClass();
    }

    @Override
    public String getType()
    {
        String cipherName3778 =  "DES";
		try{
			System.out.println("cipherName-3778" + javax.crypto.Cipher.getInstance(cipherName3778).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return V0_8;
    }


    private static class TestServerMessage implements ServerMessage<TestMessageMetaData>
    {
        private final StoredMessage<TestMessageMetaData> _storedMsg;

        private final MessageReference<ServerMessage> _messageReference = new MessageReference<ServerMessage>()
        {

            @Override
            public void close()
            {
                String cipherName3779 =  "DES";
				try{
					System.out.println("cipherName-3779" + javax.crypto.Cipher.getInstance(cipherName3779).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				release();
            }

            @Override
            public ServerMessage getMessage()
            {
                String cipherName3780 =  "DES";
				try{
					System.out.println("cipherName-3780" + javax.crypto.Cipher.getInstance(cipherName3780).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return TestServerMessage.this;
            }

            @Override
            public void release()
            {
				String cipherName3781 =  "DES";
				try{
					System.out.println("cipherName-3781" + javax.crypto.Cipher.getInstance(cipherName3781).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }
        };

        public TestServerMessage(StoredMessage<TestMessageMetaData> storedMsg)
        {
            String cipherName3782 =  "DES";
			try{
				System.out.println("cipherName-3782" + javax.crypto.Cipher.getInstance(cipherName3782).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			_storedMsg = storedMsg;
        }
        @Override
        public long getArrivalTime()
        {
            String cipherName3783 =  "DES";
			try{
				System.out.println("cipherName-3783" + javax.crypto.Cipher.getInstance(cipherName3783).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public QpidByteBuffer getContent()
        {
            String cipherName3784 =  "DES";
			try{
				System.out.println("cipherName-3784" + javax.crypto.Cipher.getInstance(cipherName3784).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public QpidByteBuffer getContent(int offset, int length)
        {
            String cipherName3785 =  "DES";
			try{
				System.out.println("cipherName-3785" + javax.crypto.Cipher.getInstance(cipherName3785).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }


        @Override
        public Object getConnectionReference()
        {
            String cipherName3786 =  "DES";
			try{
				System.out.println("cipherName-3786" + javax.crypto.Cipher.getInstance(cipherName3786).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public boolean isResourceAcceptable(final TransactionLogResource resource)
        {
            String cipherName3787 =  "DES";
			try{
				System.out.println("cipherName-3787" + javax.crypto.Cipher.getInstance(cipherName3787).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }

        @Override
        public boolean checkValid()
        {
            String cipherName3788 =  "DES";
			try{
				System.out.println("cipherName-3788" + javax.crypto.Cipher.getInstance(cipherName3788).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }

        @Override
        public ValidationStatus getValidationStatus()
        {
            String cipherName3789 =  "DES";
			try{
				System.out.println("cipherName-3789" + javax.crypto.Cipher.getInstance(cipherName3789).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return ValidationStatus.VALID;
        }

        @Override
        public long getExpiration()
        {
            String cipherName3790 =  "DES";
			try{
				System.out.println("cipherName-3790" + javax.crypto.Cipher.getInstance(cipherName3790).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public String getMessageType()
        {
            String cipherName3791 =  "DES";
			try{
				System.out.println("cipherName-3791" + javax.crypto.Cipher.getInstance(cipherName3791).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "test";
        }

        @Override
        public AMQMessageHeader getMessageHeader()
        {
            String cipherName3792 =  "DES";
			try{
				System.out.println("cipherName-3792" + javax.crypto.Cipher.getInstance(cipherName3792).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public long getMessageNumber()
        {
            String cipherName3793 =  "DES";
			try{
				System.out.println("cipherName-3793" + javax.crypto.Cipher.getInstance(cipherName3793).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _storedMsg.getMessageNumber();
        }

        @Override
        public String getInitialRoutingAddress()
        {
            String cipherName3794 =  "DES";
			try{
				System.out.println("cipherName-3794" + javax.crypto.Cipher.getInstance(cipherName3794).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "";
        }

        @Override
        public String getTo()
        {
            String cipherName3795 =  "DES";
			try{
				System.out.println("cipherName-3795" + javax.crypto.Cipher.getInstance(cipherName3795).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        @Override
        public long getSize()
        {
            String cipherName3796 =  "DES";
			try{
				System.out.println("cipherName-3796" + javax.crypto.Cipher.getInstance(cipherName3796).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public long getSizeIncludingHeader()
        {
            String cipherName3797 =  "DES";
			try{
				System.out.println("cipherName-3797" + javax.crypto.Cipher.getInstance(cipherName3797).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return 0;
        }

        @Override
        public StoredMessage<TestMessageMetaData> getStoredMessage()
        {
            String cipherName3798 =  "DES";
			try{
				System.out.println("cipherName-3798" + javax.crypto.Cipher.getInstance(cipherName3798).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _storedMsg;
        }


        @Override
        public boolean isPersistent()
        {
            String cipherName3799 =  "DES";
			try{
				System.out.println("cipherName-3799" + javax.crypto.Cipher.getInstance(cipherName3799).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _storedMsg.getMetaData().isPersistent();
        }

        @Override
        public MessageReference newReference()
        {
            String cipherName3800 =  "DES";
			try{
				System.out.println("cipherName-3800" + javax.crypto.Cipher.getInstance(cipherName3800).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _messageReference;
        }

        @Override
        public MessageReference newReference(final TransactionLogResource object)
        {
            String cipherName3801 =  "DES";
			try{
				System.out.println("cipherName-3801" + javax.crypto.Cipher.getInstance(cipherName3801).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return _messageReference;
        }

        @Override
        public boolean isReferenced(final TransactionLogResource resource)
        {
            String cipherName3802 =  "DES";
			try{
				System.out.println("cipherName-3802" + javax.crypto.Cipher.getInstance(cipherName3802).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public boolean isReferenced()
        {
            String cipherName3803 =  "DES";
			try{
				System.out.println("cipherName-3803" + javax.crypto.Cipher.getInstance(cipherName3803).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        @Override
        public int hashCode()
        {
            String cipherName3804 =  "DES";
			try{
				System.out.println("cipherName-3804" + javax.crypto.Cipher.getInstance(cipherName3804).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final int prime = 31;
            int result = 1;
            result = prime * result + ((_storedMsg == null) ? 0 : _storedMsg.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            String cipherName3805 =  "DES";
			try{
				System.out.println("cipherName-3805" + javax.crypto.Cipher.getInstance(cipherName3805).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (this == obj)
            {
                String cipherName3806 =  "DES";
				try{
					System.out.println("cipherName-3806" + javax.crypto.Cipher.getInstance(cipherName3806).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
            if (obj == null)
            {
                String cipherName3807 =  "DES";
				try{
					System.out.println("cipherName-3807" + javax.crypto.Cipher.getInstance(cipherName3807).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
            if (getClass() != obj.getClass())
            {
                String cipherName3808 =  "DES";
				try{
					System.out.println("cipherName-3808" + javax.crypto.Cipher.getInstance(cipherName3808).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
            TestServerMessage other = (TestServerMessage) obj;
            if (_storedMsg == null)
            {
                String cipherName3809 =  "DES";
				try{
					System.out.println("cipherName-3809" + javax.crypto.Cipher.getInstance(cipherName3809).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (other._storedMsg != null)
                {
                    String cipherName3810 =  "DES";
					try{
						System.out.println("cipherName-3810" + javax.crypto.Cipher.getInstance(cipherName3810).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return false;
                }
            }
            else if (!_storedMsg.equals(other._storedMsg))
            {
                String cipherName3811 =  "DES";
				try{
					System.out.println("cipherName-3811" + javax.crypto.Cipher.getInstance(cipherName3811).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
            return true;
        }


    }
}
