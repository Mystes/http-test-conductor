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
package fi.mystes.response;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.w3c.dom.Document;

import com.mashape.unirest.http.HttpResponse;

import fi.mystes.HttpTestConductor;
import fi.mystes.request.RecordedHeaders;
import fi.mystes.request.RecordedRequests;
import fi.mystes.request.RestRequest;

public class HttpMockApiTest extends HttpTestConductor {
	

	@Test
	public void initAndStoreResponseAndFetchRecordedRequestsAndHeaders() throws Exception {
		initApiMock("http://localhost:8888/mock/services/REST/local-mock/operations/POST/init");
		
		String responseBody = "{\"name\":\"Test\"}";
		
		addCustomResponseToApiMock("http://localhost:8888/mock/services/REST/local-mock/operations/POST/responses", 
									new Response().addParameter("headers","Custom-Header:jUnit")
												  .addHeader("Content-Type", "application/json")
												  .setBody(responseBody)
								  );
		String requestBody = "{\"request\":\"true\"}";
		HttpResponse<String> response = new RestRequest(requestBody,
														"http://localhost:8888/mock/services/REST/local-mock/endpoint", 
														"post")
													    .addHeader("Custom-Request-Header", "jUnit request")
													    .sendRequest();
		
		assertTrue("Http status code expected to be 200", response.getStatus() == 200);
		assertTrue("Http status text expected to be OK", response.getStatusText().equals("OK"));
		assertTrue("Response body expected to be: " + responseBody, response.getBody().equals(responseBody));
		assertTrue("Response should contain header: Custom-Header:junit", response.getHeaders().get("Custom-Header").get(0).equals("jUnit"));
		
		// Recorded request headers
		RecordedHeaders recordedHeaders = getRecordedHeadersFromApiMock("http://localhost:8888/mock/services/REST/local-mock/operations/POST/recorded-request-headers");
		
		Document doc = builder.parse(new ByteArrayInputStream(recordedHeaders.getContent().getBytes(StandardCharsets.UTF_8)));

		assertTrue("Recorded headers response content type expected to be: text/xml", 
				fetchStringWithXpath(doc, "//name[text() = 'Content-Type']/../value/text()").equals("text/plain; charset=UTF-8"));
		
        assertTrue("Expected recorded header Custom-Request-Header: jUnit request", 
        						fetchStringWithXpath(doc, "//name[text() = 'Custom-Request-Header']/../value/text()").equals("jUnit request"));

		// Recorded requests
		RecordedRequests recordedRequest = getRecordedRequestsFromApiMock("http://localhost:8888/mock/services/REST/local-mock/operations/POST/recorded-requests");
		
		doc = builder.parse(new ByteArrayInputStream(recordedRequest.getContent().getBytes(StandardCharsets.UTF_8)));
		
		assertTrue("Expected recorded request:" + requestBody, 
				fetchStringWithXpath(doc, "normalize-space(//recorded-requests/text())").equals(requestBody));
	}

}
