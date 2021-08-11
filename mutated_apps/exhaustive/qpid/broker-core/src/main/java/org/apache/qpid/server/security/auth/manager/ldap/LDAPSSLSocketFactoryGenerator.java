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
package org.apache.qpid.server.security.auth.manager.ldap;

import static org.apache.bcel.Const.ACC_PRIVATE;
import static org.apache.bcel.Const.ACC_PROTECTED;
import static org.apache.bcel.Const.ACC_PUBLIC;
import static org.apache.bcel.Const.ACC_STATIC;
import static org.apache.bcel.Const.ACC_SUPER;
import static org.apache.bcel.Const.INVOKESPECIAL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.apache.qpid.server.util.ServerScopedRuntimeException;

/**
 * This class provides a single method, {@link #createSubClass(String, SSLSocketFactory)}.  This creates a
 * sub-class of {@link AbstractLDAPSSLSocketFactory} and associates it with the {@link SSLSocketFactory} instance..
 * <p>
 * The sub-classes are <b>generated dynamically</b>.
 * </p>
 * <p>This approach is required in order to overcome a limitation in the javax.naming.directory API.  It offers
 * {@link SSLSocketFactory} customization only at the class level only (via the <code>java.naming.ldap.factory.socket</code>
 * directory context environment parameter). For this reason, a mechanism that can produce distinct
 * {@link AbstractLDAPSSLSocketFactory} classes each associated with a different SSLSocketFactory instance is required.
 * </p>
 * @see <a href="http://docs.oracle.com/javase/jndi/tutorial/ldap/security/ssl.html">Java LDAP SSL and Custom Sockets</a>
 */
public class LDAPSSLSocketFactoryGenerator
{
    /**
     * The name of field used to hold the delegate {@link SSLSocketFactory}. A field with
     * this name is created on each generated sub-class.
     */
    static final String SSL_SOCKET_FACTORY_FIELD = "_sslSocketFactory";

    /** Target package names used for the subclass - needs to exist */
    static final String TARGET_PACKAGE_NAME = LDAPSSLSocketFactoryGenerator.class.getPackage().getName();

    public static Class<? extends AbstractLDAPSSLSocketFactory> createSubClass(String simpleName, final SSLSocketFactory sslSocketFactory)
    {
        String cipherName7537 =  "DES";
		try{
			System.out.println("cipherName-7537" + javax.crypto.Cipher.getInstance(cipherName7537).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String fqcn = TARGET_PACKAGE_NAME + "." + simpleName;
        final byte[] classBytes = createSubClassByteCode(fqcn);

        try
        {
            String cipherName7538 =  "DES";
			try{
				System.out.println("cipherName-7538" + javax.crypto.Cipher.getInstance(cipherName7538).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final ClassLoader classLoader = new LDAPSSLSocketFactoryAwareDelegatingClassloader(fqcn, classBytes, sslSocketFactory);
            Class<? extends AbstractLDAPSSLSocketFactory> clazz = (Class<? extends AbstractLDAPSSLSocketFactory>) classLoader.loadClass(fqcn);
            return clazz;
        }
        catch (ClassNotFoundException cnfe)
        {
            String cipherName7539 =  "DES";
			try{
				System.out.println("cipherName-7539" + javax.crypto.Cipher.getInstance(cipherName7539).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalArgumentException("Could not resolve dynamically generated class " + fqcn, cnfe);
        }
    }

    /**
     * Creates the LDAPSocketFactoryImpl class (subclass of {@link AbstractLDAPSSLSocketFactory}.
     * A static method #getDefaulta, a static field _sslContent and no-arg constructor are added
     * to the class.
     *
     * @param className
     *
     * @return byte code
     */
    private static byte[] createSubClassByteCode(final String className)
    {
        String cipherName7540 =  "DES";
		try{
			System.out.println("cipherName-7540" + javax.crypto.Cipher.getInstance(cipherName7540).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ClassGen classGen = new ClassGen(className,
                AbstractLDAPSSLSocketFactory.class.getName(),
                "<generated>",
                ACC_PUBLIC | ACC_SUPER,
                null);
        ConstantPoolGen constantPoolGen = classGen.getConstantPool();
        InstructionFactory factory = new InstructionFactory(classGen);

        createSslContextStaticField(classGen, constantPoolGen);
        createGetDefaultStaticMethod(classGen, constantPoolGen, factory);

        classGen.addEmptyConstructor(ACC_PROTECTED);

        JavaClass javaClass = classGen.getJavaClass();
        ByteArrayOutputStream out = null;
        try
        {
            String cipherName7541 =  "DES";
			try{
				System.out.println("cipherName-7541" + javax.crypto.Cipher.getInstance(cipherName7541).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			out = new ByteArrayOutputStream();
            javaClass.dump(out);
            return out.toByteArray();
        }
        catch (IOException ioex)
        {
            String cipherName7542 =  "DES";
			try{
				System.out.println("cipherName-7542" + javax.crypto.Cipher.getInstance(cipherName7542).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new IllegalStateException("Could not write to a ByteArrayOutputStream - should not happen", ioex);
        }
        finally
        {
            String cipherName7543 =  "DES";
			try{
				System.out.println("cipherName-7543" + javax.crypto.Cipher.getInstance(cipherName7543).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			closeSafely(out);
        }
    }

    /**
     * Creates a static field _sslContext of type {@link SSLSocketFactory}.
     *
     * @param classGen
     * @param constantPoolGen
     */
    private static void createSslContextStaticField(ClassGen classGen, ConstantPoolGen constantPoolGen)
    {
        String cipherName7544 =  "DES";
		try{
			System.out.println("cipherName-7544" + javax.crypto.Cipher.getInstance(cipherName7544).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		FieldGen fieldGen = new FieldGen(ACC_PRIVATE | ACC_STATIC,
                                         Type.getType(SSLSocketFactory.class),
                                         SSL_SOCKET_FACTORY_FIELD,
                                         constantPoolGen);
        classGen.addField(fieldGen.getField());
    }

    /**
     * Create a static method 'getDefault' returning {@link SocketFactory}
     * that creates a new instance of the sub-class and calls its no-argument
     * constructor, the newly created is returned to the caller.
     *
     * @param classGen
     * @param constantPoolGen
     * @param instructionFactory
     */
    private static void createGetDefaultStaticMethod(ClassGen classGen,
            ConstantPoolGen constantPoolGen, InstructionFactory instructionFactory)
    {
        String cipherName7545 =  "DES";
		try{
			System.out.println("cipherName-7545" + javax.crypto.Cipher.getInstance(cipherName7545).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		InstructionList il = new InstructionList();

        String methodName = "getDefault";
        MethodGen mg = new MethodGen(ACC_STATIC | ACC_PUBLIC, // access flags
                            Type.getType(SSLSocketFactory.class),  // return type
                            new Type[0],   // argument types - no args
                            new String[0], // arg names - no args
                            methodName,
                            classGen.getClassName(),    // method, class
                            il,
                            constantPoolGen);

        il.append(instructionFactory.createNew(classGen.getClassName()));
        il.append(InstructionConst.DUP);

        il.append(instructionFactory.createInvoke(classGen.getClassName(), "<init>", Type.VOID,
                                       new Type[] {},
                                       INVOKESPECIAL));

        il.append(InstructionConst.ARETURN);

        mg.setMaxStack();
        classGen.addMethod(mg.getMethod());
        il.dispose();
    }

    private static void closeSafely(ByteArrayOutputStream out)
    {
        String cipherName7546 =  "DES";
		try{
			System.out.println("cipherName-7546" + javax.crypto.Cipher.getInstance(cipherName7546).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (out != null)
        {
            String cipherName7547 =  "DES";
			try{
				System.out.println("cipherName-7547" + javax.crypto.Cipher.getInstance(cipherName7547).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try
            {
                String cipherName7548 =  "DES";
				try{
					System.out.println("cipherName-7548" + javax.crypto.Cipher.getInstance(cipherName7548).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				out.close();
            }
            catch (IOException e)
            {
				String cipherName7549 =  "DES";
				try{
					System.out.println("cipherName-7549" + javax.crypto.Cipher.getInstance(cipherName7549).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                // Ignore
            }
        }
    }

    private static void setSslSocketFactoryFieldByReflection(Class<? extends AbstractLDAPSSLSocketFactory> clazz, String fieldName, SSLSocketFactory sslSocketFactory)
    {
        String cipherName7550 =  "DES";
		try{
			System.out.println("cipherName-7550" + javax.crypto.Cipher.getInstance(cipherName7550).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String exceptionMessage = "Unexpected error setting generated static field "
                 + fieldName + "on generated class " + clazz.getName();
        try
        {
            String cipherName7551 =  "DES";
			try{
				System.out.println("cipherName-7551" + javax.crypto.Cipher.getInstance(cipherName7551).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Field declaredField = clazz.getDeclaredField(fieldName);
            boolean accessible = declaredField.isAccessible();
            try
            {
                String cipherName7552 =  "DES";
				try{
					System.out.println("cipherName-7552" + javax.crypto.Cipher.getInstance(cipherName7552).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				declaredField.setAccessible(true);
                declaredField.set(null, sslSocketFactory);
            }
            finally
            {
                String cipherName7553 =  "DES";
				try{
					System.out.println("cipherName-7553" + javax.crypto.Cipher.getInstance(cipherName7553).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				declaredField.setAccessible(accessible);
            }
        }
        catch (IllegalArgumentException e)
        {
            String cipherName7554 =  "DES";
			try{
				System.out.println("cipherName-7554" + javax.crypto.Cipher.getInstance(cipherName7554).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(exceptionMessage, e);
        }
        catch (IllegalAccessException e)
        {
            String cipherName7555 =  "DES";
			try{
				System.out.println("cipherName-7555" + javax.crypto.Cipher.getInstance(cipherName7555).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(exceptionMessage, e);
        }
        catch (NoSuchFieldException e)
        {
            String cipherName7556 =  "DES";
			try{
				System.out.println("cipherName-7556" + javax.crypto.Cipher.getInstance(cipherName7556).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(exceptionMessage, e);
        }
        catch (SecurityException e)
        {
            String cipherName7557 =  "DES";
			try{
				System.out.println("cipherName-7557" + javax.crypto.Cipher.getInstance(cipherName7557).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(exceptionMessage, e);
        }
    }

    static SSLSocketFactory getStaticFieldByReflection(Class<? extends AbstractLDAPSSLSocketFactory> clazz, String fieldName)
    {
        String cipherName7558 =  "DES";
		try{
			System.out.println("cipherName-7558" + javax.crypto.Cipher.getInstance(cipherName7558).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String exceptionMessage = "Unexpected error getting generated static field "
                + fieldName + "on generated class " + clazz.getName();

        Field declaredField;
        try
        {
            String cipherName7559 =  "DES";
			try{
				System.out.println("cipherName-7559" + javax.crypto.Cipher.getInstance(cipherName7559).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			declaredField = clazz.getDeclaredField(fieldName);
            boolean accessible = declaredField.isAccessible();
            try
            {
                String cipherName7560 =  "DES";
				try{
					System.out.println("cipherName-7560" + javax.crypto.Cipher.getInstance(cipherName7560).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				declaredField.setAccessible(true);
                return (SSLSocketFactory) declaredField.get(null);
            }
            finally
            {
                String cipherName7561 =  "DES";
				try{
					System.out.println("cipherName-7561" + javax.crypto.Cipher.getInstance(cipherName7561).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				declaredField.setAccessible(accessible);
            }
        }
        catch (NoSuchFieldException e)
        {
            String cipherName7562 =  "DES";
			try{
				System.out.println("cipherName-7562" + javax.crypto.Cipher.getInstance(cipherName7562).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(exceptionMessage, e);
        }
        catch (SecurityException e)
        {
            String cipherName7563 =  "DES";
			try{
				System.out.println("cipherName-7563" + javax.crypto.Cipher.getInstance(cipherName7563).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(exceptionMessage, e);
        }
        catch (IllegalArgumentException e)
        {
            String cipherName7564 =  "DES";
			try{
				System.out.println("cipherName-7564" + javax.crypto.Cipher.getInstance(cipherName7564).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(exceptionMessage, e);
        }
        catch (IllegalAccessException e)
        {
            String cipherName7565 =  "DES";
			try{
				System.out.println("cipherName-7565" + javax.crypto.Cipher.getInstance(cipherName7565).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ServerScopedRuntimeException(exceptionMessage, e);
        }
    }

    private static final class LDAPSSLSocketFactoryAwareDelegatingClassloader extends ClassLoader
    {
        private final String _className;
        private final Class<? extends AbstractLDAPSSLSocketFactory> _clazz;

        private LDAPSSLSocketFactoryAwareDelegatingClassloader(String className,
                byte[] classBytes, SSLSocketFactory sslSocketFactory)
        {
            super(LDAPSSLSocketFactoryGenerator.class.getClassLoader());
			String cipherName7566 =  "DES";
			try{
				System.out.println("cipherName-7566" + javax.crypto.Cipher.getInstance(cipherName7566).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            _className = className;
            _clazz = (Class<? extends AbstractLDAPSSLSocketFactory>) defineClass(className, classBytes, 0, classBytes.length);
            setSslSocketFactoryFieldByReflection(_clazz, SSL_SOCKET_FACTORY_FIELD, sslSocketFactory);
        }

        @Override
        protected Class<?> findClass(String fqcn) throws ClassNotFoundException
        {
            String cipherName7567 =  "DES";
			try{
				System.out.println("cipherName-7567" + javax.crypto.Cipher.getInstance(cipherName7567).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (fqcn.equals(_className))
            {
                String cipherName7568 =  "DES";
				try{
					System.out.println("cipherName-7568" + javax.crypto.Cipher.getInstance(cipherName7568).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return _clazz;
            }
            else
            {
                String cipherName7569 =  "DES";
				try{
					System.out.println("cipherName-7569" + javax.crypto.Cipher.getInstance(cipherName7569).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return getParent().loadClass(fqcn);
            }
        }
    }
}
