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
package fi.mystes.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract POJO class which allows chain invocation of extending class' methods.
 *
 * @param <T> Extending class to allow chain invocation
 */
public abstract class ConfigurableHttpEntity<T>{
	
	/** Body content of extending class instance */
	protected String content;
	
	/** HTTP headers */
	protected Map<String, String> headers;
	
	/** End point URL */
	protected String url;
	/** HTTP method of extending class instance */
	protected String method;
	/** HTTP parameters of extending class instance */
	protected Map<String, String> parameters;
	
	/** Boolean flag indicating whether HTTP headers should be treated as HTTP parameters */
	protected boolean headersAsParameters = false;
	
	/** Class to be used to cast extending class instance to allow chain invocation */
	private Class<T> castClass;
	
	/**
	 * Constructor receiving class to allow chain invocation
	 * @param clazz
	 */
	protected ConfigurableHttpEntity(Class<T> clazz) {
		castClass = clazz;
	}

	/**
	 * Body content of extending class instance.
	 * 
	 * @param content Body content
	 * 
	 * @return Extending class instance to allow chain invocation
	 */
	public T setBody(String content) {
		this.content = content;
		return castClass.cast(this);
	}
	
	/**
	 * Returns body content of extending class instance.
	 * 
	 * @return Body content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Adds header to request.
	 * 
	 * @param name Header name
	 * @param value Header value
	 * 
	 * @return Extending class instance to allow chain invocation
	 */
	public T addHeader(String name, String value) {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		headers.put(name, value);

		return castClass.cast(this);
	}

	/**
	 * Sets request headers.
	 * 
	 * @param headers HTTP headers to be set
	 * @return Extending class instance to allow chain invocation
	 */
	public T setHeaders(Map<String, String> headers){
		this.headers = headers;
		return castClass.cast(this);
	}
	
	/**
	 * Returns HTTP headers.
	 * 
	 * @return Instance of Map<String, String> containing HTTP headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Sets End point URL.
	 * 
	 * @param url End point URL
	 * 
	 * @return Extending class instance to allow chain invocation
	 */
	public T setEndpointUrl(String url) {
		this.url = url;

		return castClass.cast(this);
	}
	
	/**
	 * Returns end point URL.
	 * 
	 * @return End point URL
	 */
	public String getEndpoint() {
		return url;
	}

	/**
	 * Sets request method.
	 * 
	 * @param method HTTP method (get/post/put/...)
	 * 
	 * @return Extending class instance to allow chain invocation
	 */
	public T setMethod(String method) {
		this.method = method;
		return castClass.cast(this);
	}
	
	/**
	 * Returns HTTP method.
	 * 
	 * @return String representing HTTP method
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * Adds HTTP parameter.
	 * 
	 * @param parameter Parameter name
	 * @param value Parameter value
	 * 
	 * @return Extending class instance to allow chain invocation
	 */
	public T addParameter(String parameter, String value) {
		if (parameters == null) {
			parameters = new HashMap<String, String>();
		}
		parameters.put(parameter, value);
		return castClass.cast(this);
	}
	
	/**
	 * Sets HTTP parameters.
	 * 
	 * @param parameters HTTP parameters to be set
	 * 
	 * @return Extending class instance to allow chain invocation
	 */
	public T setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
		return castClass.cast(this);
	}
	
	/**
	 * Returns HTTP parameters.
	 * 
	 * @return Instance of java.util.Map<String, String> containing HTTP parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Sets boolean flag indicating whether HTTP headers should be treated as HTTP parameters.
	 * 
	 * @param headersAsParameters True if HTTP headers should be treated as HTTP parameters, otherwise false
	 * 
	 * @return Extending class instance to allow chain invocation
	 */
	public T setHeadersAsParameters(boolean headersAsParameters) {
		this.headersAsParameters = headersAsParameters;
		return castClass.cast(this);
	}
	
	/**
	 * Returns boolean flag indicating whether HTTP headers should be treated as HTTP parameters.
	 * 
	 * @return True or false
	 */
	public boolean headersAsParameters() {
		return headersAsParameters;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return new StringBuilder("Endpoint: ")
					.append(getEndpoint())
					.append("\nMethod: ")
					.append(getMethod())
					.append("\nContent: ")
					.append(getContent())
					.append("\nHeaders: ")
					.append(getHeaders() != null?getHeaders().toString():"")
					.append("\nParameters: ")
					.append(getParameters()!= null?getParameters().toString():"")
					.append("\nHeaders as parameters: ")
					.append(headersAsParameters)
					.toString();
	}
	
}
