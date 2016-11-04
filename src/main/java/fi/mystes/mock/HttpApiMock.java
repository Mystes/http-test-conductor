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
package fi.mystes.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.log4j.Logger;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;

import fi.mystes.request.RecordedHeaders;
import fi.mystes.request.RecordedRequests;
import fi.mystes.request.RestRequest;
import fi.mystes.response.Response;

/**
 * HttpApiMock class which "emulates" API mocking by communicating with HttpApiMock service.
 *
 */
public class HttpApiMock implements IApiMock<HttpApiMock>{
	private static final Logger logger = Logger.getLogger(HttpApiMock.class);
	private static final String PROTOCOL = "http";
	private static final String HOST = "localhost";
	private static final String HEALT_CHECK_POSTFIX = "/mock/services";

	@Override
	public boolean isAlive(Integer port) {
		logger.info("Checking if HttpApiMock is alive");
		try {
			HttpResponse<String> result = new RestRequest("",
				getBaseUrl(port) + HEALT_CHECK_POSTFIX,
				"get",
				null, null).sendRequest();
		} catch (Exception e) {
			logger.info("HttpApiMock not available");
			return false;
		}
		logger.info("HttpApiMock found running in port: " + port);
		return true;
	}

	@Override
	public HttpApiMock init(String uri) throws Exception {
		logger.info("Initializing: " + uri);
		logger.info(new RestRequest(null, uri, "post").sendRequest().getBody());
		return this;
	}

	@Override
	public HttpApiMock addCustomResponse(String uri, Response response) throws Exception {
		logger.info("Adding cutom response " + response.getContent() + " to:" + uri);

		handleHeaders(response);
		HttpResponse<String> result = new RestRequest(response.getContent(), 
				uri, 
				"post", 
				response.getParameters(), response.getHeaders()).sendRequest();
		logger.info("Result for addCustomResponse: " + result.getBody() + 
				"\nStatus: " + result.getStatusText() + 
				"\nCode: "+result.getStatus());
		return this;
	}

	@Override
	public RecordedRequests getRecordedRequests(String uri) throws Exception {
		logger.info("Fetching recorded requests from: " + uri);

		HttpResponse<String> response = new RestRequest(null, uri, "get").sendRequest();
		Headers responseHeaders = response.getHeaders();
		Map<String, String> headers = new HashMap<String, String>();
		for(String header : responseHeaders.keySet()) {
			headers.put(header, responseHeaders.get(header).toString());
		}

		RecordedRequests recordedRequest = new RecordedRequests().setBody(response.getBody()).setEndpointUrl(uri).setHeaders(headers);

		logger.info("Fetched recorded requests: " + recordedRequest.toString());

		return recordedRequest;
	}

	@Override
	public RecordedHeaders getRecordedHeaders(String uri) throws Exception {
		HttpResponse<String> response = new RestRequest(null, uri, "get").sendRequest();
		Headers responseHeaders = response.getHeaders();
		Map<String, String> headers = new HashMap<String, String>();
		for(String header : responseHeaders.keySet()) {
			headers.put(header, responseHeaders.get(header).toString());
		}
		
		RecordedHeaders recordedHeaders = new RecordedHeaders().setBody(response.getBody()).setEndpointUrl(uri).setHeaders(headers);
		
		logger.info("Fetched recorded headers: " + recordedHeaders.toString());

		return recordedHeaders;
	}

	private String getBaseUrl(Integer port) {
		return PROTOCOL + "://" + HOST + ":" + port;
	}

	private void handleHeaders(Response response) {
		if (!response.headersAsParameters()) {
			logger.info("Headers not as parameters");
			return;
		}
		Map<String, String> responseHeaders = response.getHeaders();
		StringBuilder headers = new StringBuilder();
		boolean first = true;
		for(String header : responseHeaders.keySet()) {
			if (!first) {
				headers.append(",");
			}
			headers.append(header).append(":").append(responseHeaders.get(header));
			first = false;
		}

		logger.info("headers as parameter: " + headers);

		if (headers.length() > 0) {
			response.addParameter("headers", headers.toString());
		}
	}

}
