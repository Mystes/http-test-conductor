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

import fi.mystes.request.RecordedHeaders;
import fi.mystes.request.RecordedRequests;
import fi.mystes.response.Response;

/**
 * Interface class for mocking APIs.
 * 
 * @param <T> Implemented class to allow chain invocation.
 */
public interface IApiMock<T> {
	
	/**
	 * Method to initialize given URI
	 * 
	 * @param uri API URI to initialize
	 * 
	 * @return Instance of implemented class
	 * 
	 * @throws Exception If API URI initialization fails
	 */
	public T init(String uri) throws Exception;
	
	/**
	 * Method to add customized response to mocking API.
	 * 
	 * @param uri API URI to add customized response to
	 * @param response Customized response to be added
	 * 
	 * @return Instance of implemented class
	 * 
	 * @throws Exception If adding customized response to given API URI fails
	 */
	public T addCustomResponse(String uri, Response response) throws Exception;
	
	/**
	 * Method to retrieve recored requests from given API URI.
	 * 
	 * @param uri API URI to retrieve recorded requests from
	 * 
	 * @return Instance of fi.mystes.request.RecordedRequests
	 * 
	 * @throws Exception If recorded requests retrieval from given API URI fails
	 */
	public RecordedRequests getRecordedRequests(String uri) throws Exception;
	
	/**
	 * Method to retrieve recorded headers from given API URI.
	 * 
	 * @param uri API URI to retrieve recorded headers from
	 * 
	 * @return Instance of fi.mystes.request.RecordedHeaders 
	 * 
	 * @throws Exception If recorded headers retrieval from given API URI fails
	 */
	public RecordedHeaders getRecordedHeaders(String uri) throws Exception;
}
