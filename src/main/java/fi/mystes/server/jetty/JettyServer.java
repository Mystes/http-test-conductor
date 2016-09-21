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
package fi.mystes.server.jetty;

import org.mortbay.jetty.Server;
import org.mortbay.resource.Resource;

import fi.mystes.server.IMockServer;
import fi.mystes.server.IMockWebApp;

/**
 * Class extending org.mortbay.jetty.Server and implementing IMockServer.
 *
 */
public class JettyServer extends Server implements IMockServer{

	/**
	 * Constructor with server port.
	 * 
	 * @param port Port server should listen to
	 */
	public JettyServer(int port) {
		super(port);
	}
	
	@Override
	public void addWebApp(IMockWebApp webApp) throws Exception {
		addHandler((JettyWebApp)webApp);
	}
	
	@Override
	public void addWebApp(String webAppPath, String contextPath) throws Exception{
		addWebApp(webAppPath, contextPath, null);
	}
	
	@Override
	public void addWebApp(String webAppPath, String contextPath, String extraClasspath) throws Exception{
		JettyWebApp webApp = new JettyWebApp(Resource.newResource(webAppPath).getURL().toString(), contextPath);
		webApp.setExtraClasspath(extraClasspath);
		addWebApp(webApp);
	}
	
	@Override
	public void tearUp() throws Exception {
		start();
	}

	@Override
	public void tearDown() throws Exception {
		stop();
	}

}
