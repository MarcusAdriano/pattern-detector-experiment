/**
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
package org.apache.aries.proxy;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.osgi.framework.Bundle;

/**
 * The proxy manager service allows clients to generate and manage proxies.
 */
public interface ProxyManager 
{
  public Object createProxy(Bundle clientBundle, Collection<Class<?>> classes, Callable<Object> dispatcher) throws UnableToProxyException;
  public Object createProxy(Bundle clientBundle, Collection<Class<?>> classes, Callable<Object> dispatcher, InvocationHandlerWrapper wrapper) throws UnableToProxyException;
  /**
   * This method unwraps the provided proxy returning the target object.
   * 
   * @param proxy the proxy to unwrap.
   * @return      the target object.
   */
  public Callable<Object> unwrap(Object proxy);
  /**
   * Returns true if and only if the specified object was generated by a ProxyFactory returned by
   * a call to {@link ProxyManager#createProxyFactory(boolean)}.
   * @param proxy The proxy object to test
   * @return      true if it is a proxy, false otherwise.
   */
  public boolean isProxy(Object proxy);
}
