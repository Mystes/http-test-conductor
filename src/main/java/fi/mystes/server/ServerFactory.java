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

import fi.mystes.server.jetty.JettyServer;

/**
 * Factory for creating instance to supported server. Currently only Jetty server type is supported.
 *
 */
public abstract class ServerFactory {
	/** Supported server types */
	public static enum Type {JETTY};
	
	/**
	 * Method to create instance to server.
	 * 
	 * @param type Server type
	 * @param port Port server should listen to
	 * 
	 * @return Created instance of IMockServer
	 * 
	 * @throws UnsupportedServerTypeException If unsupported server type given
	 */
	public static IMockServer createServer(Type type, int port) throws UnsupportedServerTypeException{
		switch(type) {
			case JETTY:
				return new JettyServer(port);
			default:
				throw new UnsupportedServerTypeException("Supported server types are:" + Type.values());
		}
	}

}
