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
package fi.mystes.request;

import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import fi.mystes.http.ConfigurableHttpEntity;

/**
 * Class representing REST request. This class uses Unirest to send REST requests.
 *
 */
public class RestRequest extends ConfigurableHttpEntity<RestRequest> implements IRequest<HttpResponse<String>> {
	
	/**
	 * Default constructor
	 */
	public RestRequest() {
		this(null, null, null);
	}
	
	/**
	 * Constructor with minimal required arguments.
	 * 
	 * @param content Body content of REST request
	 * @param endpointUrl REST API end point URL
	 * @param method HTTP method
	 */
	public RestRequest(String content, String endpointUrl, String method){
		this(content, endpointUrl, method, null);
	}
	
	/**
	 * Constructor with minimal required and HTTP parameters.
	 * @param content Body content of REST request
	 * @param endpointUrl REST API end point URL
	 * @param method HTTP method
	 * @param parameters HTTP parameters
	 */
	public RestRequest(String content, String endpointUrl, String method, Map<String, String> parameters) {
		this(content, endpointUrl, method, parameters, null);
	}
	
	/**
	 * Constructor with minimal required, HTTP parameters, and HTTP headers.
	 * 
	 * @param content Body content of REST request
	 * @param endpointUrl REST API end point URL
	 * @param method HTTP method
	 * @param parameters HTTP parameters
	 * @param headers HTTP headers
	 */
	public RestRequest(String content, String endpointUrl, String method,Map<String, String> parameters, Map<String, String> headers) {
		super(RestRequest.class);
		setBody(content).setEndpointUrl(endpointUrl).setHeaders(headers).setMethod(method).setParameters(parameters);
	}
	
	/**
	 * Implemented sendRequest method to send REST request to given end point URL.
	 * 
	 * @return Instance of com.mashape.unirest.http.HttpResponse
	 */
	public HttpResponse<String> sendRequest() throws Exception {
		
		HttpRequest request = initRequest();
		if (request == null) {
			throw new Exception("\"" + method + "\" is not supported");
		}
		
		initParameters(request);
		
		request.headers(headers);
		
		if (request instanceof HttpRequestWithBody && content != null) {
			((HttpRequestWithBody) request).body(content);
		}
		
		HttpResponse<String> response = request.asString();
		
		return response;
	}
	
	/**
	 * Helper method to define HTTP method supported by Unirest.
	 * 
	 * @return Instance of com.mashape.unirest.http.HttpRequest
	 */
	private HttpRequest initRequest() {
		if (method == null) {
			return null;
		}
		switch (method.toLowerCase()) {
			case "post":
				return Unirest.post(url);
			case "get":
				return Unirest.get(url);
			case "put":
				return Unirest.put(url);
			case "patch":
				return Unirest.patch(url);
			case "delete":
				return Unirest.delete(url);
			case "head":
				return Unirest.head(url);
			case "options":
				return Unirest.options(url);
		}
		return null;
	}
	
	/**
	 * Helper method to set HTTP parameters to given request.
	 * 
	 * @param request HttpRequest to set HTTP parameters to
	 */
	private void initParameters(HttpRequest request) {
		if (parameters == null) {
			return;
		}
		for (String param : parameters.keySet()) {
			request.queryString(param, parameters.get(param));
		}
	}

}