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

import static org.mockito.Matchers.notNull;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import static org.junit.Assert.*;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mashape.unirest.http.options.Options;
import com.mashape.unirest.http.utils.ClientFactory;

@PrepareForTest(ClientFactory.class)
@SuppressStaticInitializationFor({"com.mashape.unirest.http.options.Options"})
@RunWith(PowerMockRunner.class)
public class RestRequestTest {

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(ClientFactory.class);
		
		HttpResponse response = Mockito.mock(HttpResponse.class);
		HttpClient httpClient = Mockito.mock(HttpClient.class);
		StatusLine satusLine = Mockito.mock(StatusLine.class);
		
		Mockito.when(ClientFactory.getHttpClient()).thenReturn(httpClient);
		Mockito.when(httpClient.execute((HttpUriRequest)notNull())).thenReturn(response);
		Mockito.when(response.getAllHeaders()).thenReturn(new Header[0]);
		Mockito.when(response.getStatusLine()).thenReturn(satusLine);
		Mockito.when(satusLine.getStatusCode()).thenReturn(200);
		Mockito.when(satusLine.getReasonPhrase()).thenReturn("OK");
		
		
		suppress(method(Options.class, "getOption"));
	}
	
	@Test
	public void mockingResponseToRequest() throws Exception {
		com.mashape.unirest.http.HttpResponse<String> response = new RestRequest("", "http://localhost", "post").sendRequest();
		assertTrue("Status should be 200", response.getStatus() == 200);
		assertTrue("Status text should be OK", response.getStatusText().equals("OK"));
	}

}
