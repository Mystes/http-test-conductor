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

import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.SessionHandler;
import org.mortbay.jetty.webapp.WebAppContext;

import fi.mystes.server.IMockWebApp;

/**
 * Class extending org.mortbay.jetty.webapp.WebAppContenxt and implementing IMockWebApp.
 *
 */
public class JettyWebApp extends WebAppContext implements IMockWebApp{
	
	/**
	 * Default constructor.
	 */
	public JettyWebApp(){
        super();
    }
    
	/**
	 * Constructor with path to Web application and context path.
	 * 
	 * @param webApp Path to Web application
	 * @param contextPath Context path
	 */
    public JettyWebApp(String webApp, String contextPath) {
        super(webApp, contextPath);
    }
    
    /**
     * Constructor with handler container, path to Web application, and context path.
     * 
     * @param parent Handler container
     * @param webApp Path to Web application
     * @param contextPath Context path
     */
    public JettyWebApp(HandlerContainer parent, String webApp, String contextPath) {
        super(parent, webApp, contextPath);
    }

    /**
     * Constructor with Security handler, Session handler, Servlet handler, and Error handler.
     * 
     * @param securityHandler
     * @param sessionHandler
     * @param servletHandler
     * @param errorHandler
     */
    public JettyWebApp(SecurityHandler securityHandler,SessionHandler sessionHandler, ServletHandler servletHandler, ErrorHandler errorHandler) {
        super(securityHandler, sessionHandler, servletHandler, errorHandler);
    }    
	
}
