/**
 * Copyright 2016 Mystes Oy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.mystes.server;

import fi.mystes.mock.IApiMock;

/**
 * Interface for mock server implementation.
 *
 */
public interface IMockServer {

	/**
	 * Adds Web Application to server.
	 * 
	 * @param webApp Instance of IMockWebApp
	 * 
	 * @throws Exception If adding web application to server fails
	 */
	public void addWebApp(IMockWebApp webApp) throws Exception;
	
	/**
	 * Adds Web application to server with given context path.
	 * 
	 * @param webAppPath Instance of IMockWebApp
	 * @param contextPath Application's context path
	 * 
	 * @throws Exception If adding web application to server fails
	 */
	public void addWebApp(String webAppPath, String contextPath) throws Exception;
	
	/**
	 * Adds Web application to server with given context path and extra class path.
	 * 
	 * @param webAppPath webAppPath Instance of IMockWebApp
	 * @param contextPath Application's context path
	 * @param extraClasspath Extra class path for web application
	 * 
	 * @throws Exception If adding web application to server fails
	 */
	public void addWebApp(String webAppPath, String contextPath, String extraClasspath) throws Exception;
	
	/**
	 * Starts server.
	 * 
	 * @throws Exception If server start fails
	 */
	public void tearUp(IApiMock mockServer, Integer port) throws Exception;
	
	/**
	 * Stops server.
	 * 
	 * @throws Exception If server stop fails
	 */
	public void tearDown() throws Exception;
}
