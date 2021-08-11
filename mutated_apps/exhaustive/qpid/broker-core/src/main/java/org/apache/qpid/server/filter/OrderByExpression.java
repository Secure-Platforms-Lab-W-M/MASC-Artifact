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

package org.apache.qpid.server.filter;

public class OrderByExpression implements Expression
{
    public enum Order
    { ASC, DESC }

    private final Expression _expression;
    private final Order _order;

    public OrderByExpression(Expression expression)
    {
        this(expression, Order.ASC);
		String cipherName14325 =  "DES";
		try{
			System.out.println("cipherName-14325" + javax.crypto.Cipher.getInstance(cipherName14325).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public OrderByExpression(Expression expression, Order order)
    {
        String cipherName14326 =  "DES";
		try{
			System.out.println("cipherName-14326" + javax.crypto.Cipher.getInstance(cipherName14326).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		_expression = expression;
        _order = order;
    }

    @Override
    public Object evaluate(final Object object)
    {
        String cipherName14327 =  "DES";
		try{
			System.out.println("cipherName-14327" + javax.crypto.Cipher.getInstance(cipherName14327).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _expression.evaluate(object);
    }

    public Order getOrder()
    {
        String cipherName14328 =  "DES";
		try{
			System.out.println("cipherName-14328" + javax.crypto.Cipher.getInstance(cipherName14328).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return _order;
    }

    public boolean isColumnIndex()
    {
        String cipherName14329 =  "DES";
		try{
			System.out.println("cipherName-14329" + javax.crypto.Cipher.getInstance(cipherName14329).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (_expression instanceof ConstantExpression && ((ConstantExpression)_expression).getValue() instanceof Number);
    }

    public int getColumnIndex()
    {
        String cipherName14330 =  "DES";
		try{
			System.out.println("cipherName-14330" + javax.crypto.Cipher.getInstance(cipherName14330).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ((Number)((ConstantExpression)_expression).getValue()).intValue();
    }
}
