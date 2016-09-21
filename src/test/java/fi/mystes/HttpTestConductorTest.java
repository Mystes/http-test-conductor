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
package fi.mystes;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mashape.unirest.http.HttpResponse;

import fi.mystes.request.RestRequest;

public class HttpTestConductorTest extends HttpTestConductor {

	@Test
	public void availableServicesWithDefaultWsProperties() throws Exception {
		HttpResponse<String> response = new RestRequest("",
				"http://localhost:8888/mock/services", 
				"get")
			    .sendRequest();
		assertTrue("Http status code expected to be 200", response.getStatus() == 200);
		assertTrue("Http status text expected to be OK", response.getStatusText().equals("OK"));
	}
}
