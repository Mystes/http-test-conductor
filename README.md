# http-test-conductor 

![Build status](https://circleci.com/gh/Mystes/http-test-conductor.svg?style=shield&circle-token=c9da78a165eb98c5ae0aea85e1d267c76e2c7fd3)

With Http-test-conductor you can create integration tests in a situation where you are testing services that have calls to external services. The idea is that during integration tests those external services are mocked by a mock service. Http-test-conductor, as its name describes, acts as a conductor in the integration test: it helps you to send requests to the services that are under testing and also to conduct the mock services that are being called during tests.

In default configuration http-test-conductor uses [HTTP API Mock](https://github.com/Mystes/http-api-mock) as a mock service, but it is simple to extend the implementation to support other mock services.

#### Features:
* Easy to send REST/SOAP requests
* Supports HTTP API Mock service
* Starts automatically HTTP API Mock service before test
* Supports Jetty server and Jetty Web Applications
* Simple to extend to support other mock services and server types
* Easy to assert results/responses from services

## Usage

### Configuration
#### Step 1. Add as Maven dependency
Copy-wars plugin is needed to copy HTTP API Mock service into project's lib directory. The following maven plugin is required into your project.
```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-dependency-plugin</artifactId>
	<version>2.4</version>
	<executions>
		<execution>
			<id>copy-wars</id>
			<phase>pre-clean</phase>
			<goals>
				<goal>copy</goal>
			</goals>
			<configuration>
				<outputDirectory>${project.build.directory}/../lib</outputDirectory>
				<stripVersion>true</stripVersion>
				<artifactItems>
					<artifactItem>
						<groupId>fi.mystes</groupId>
						<artifactId>http-api-mock</artifactId>
						<version>1.0.0</version>
						<type>war</type>
					</artifactItem>
				</artifactItems>
			</configuration>
		</execution>
	</executions>
</plugin>
```

You also need to add the following dependency and repository part into project's pom.xml.
```xml
<dependencies>
	<dependency>
		<groupId>fi.mystes</groupId>
		<artifactId>http-test-conductor</artifactId>
		<version>0.0.1</version>
		<scope>test</scope>
	</dependency>
</dependencies>
<repositories>
	<repository>
        <id>bintray-mystes-maven</id>
        <name>bintray</name>
        <url>http://dl.bintray.com/mystes/maven</url>
	</repository>
</repositories>
```

#### Step 2. Configure http-test-conductor
Http-test-conductor uses its own (under resources directory) conf.properties configuration file where the following properties are defined.


##### Default properties
<table>
<thead>
<tr>
    <th>Property</th>
    <th>Default value</th>
    <th>Description</th>
</tr>
</thead>
<tbody>
<tr>
    <td>http.test.conductor.webAppFileUrl</td>
    <td>lib/http-api-mock.war</td>
    <td>Web App (HTTP API Mock) file URL</td>
</tr>
<tr>
    <td>http.test.conductor.extraClasspath</td>
    <td>src/test/resources/web</td>
    <td>Extra classpath for mockable APIs file ws-mock.properties</td>
</tr>
<tr>
    <td>http.test.conductor.webAppContextPath</td>
    <td>/mock</td>
    <td>Web App context path</td>
</tr>
<tr>
    <td>http.test.conductor.serverType</td>
    <td>JETTY</td>
    <td>Supported server type</td>
</tr>
<tr>
    <td>http.test.conductor.serverPort</td>
    <td>8888</td>
    <td>Server port</td>
</tr>
<tr>
    <td>http.test.conductor.apiMock</td>
    <td>fi.mystes.mock.HttpApiMock</td>
    <td>Supported API Mock which implements fi.mystes.mock.IApiMock interface</td>
</tr>
</tbody>
</table>

Above properties can be overriden by saving the following conf.properties file content into project's src/main/resources/conf.properties:
```
################################################################
# API Mock server configuration                                #
################################################################
# Web App file URL
http.test.conductor.webAppFileUrl = lib/http-api-mock.war

# Extra classpath for mockable APIs file ws-mock.properties
http.test.conductor.extraClasspath = src/test/resources/web

# Web App context path
http.test.conductor.webAppContextPath = /mock

# Supported server type
http.test.conductor.serverType = JETTY

# Server port
http.test.conductorserverPort = 8888

# Supported API Mock which implements fi.mystes.mock.IApiMock interface
http.test.conductor.apiMock = fi.mystes.mock.HttpApiMock
```
Notice that ws-mock.properties file is recommended to be located into src/test/resources/[some directory] and not into src/main/resources. This is due to HTTP API Mock class loader which will fail at startup.



#### Step 3. Extend HttpTestConductor class in your jUnit tests

Here is a simple example how to use IntegrationTestUtil.

```
public class TestingIntegrationUnitTest extends HttpTestConductor {

	@Test
	public void runWithHttpTestConductor() throws Exception {
		
		HttpResponse<String> response = new RestRequest("",
				"http://localhost:8888/mock/services", 
				"get")
			    .sendRequest();

		assertTrue("Http status code expected to be 200", response.getStatus() == 200);
		assertTrue("Http status text expected to be OK", response.getStatusText().equals("OK"));
	}

}
```
The above test requests available services from Http API Mock using REST. The above test uses also http-test-conductor's default configurations.

Here is an example using some of helper methods defined in HttpTestConductor.

```
public class HttpMockApiTest extends HttpTestConductor{
	

	@Test
	public void initAndStoreResponseAndFetchRecordedRequestsAndHeaders() throws Exception {
		// Init HTTP API mock service
		initApiMock("http://localhost:8888/mock/services/REST/local-mock/operations/POST/init");
		
		// expected response body
		String responseBody = "{\"name\":\"Test\"}";
		
		// conduct HTTP API mock service to respond with given request when it is called the next time
		addCustomResponseToApiMock("http://localhost:8888/mock/services/REST/local-mock/operations/POST/responses", 
									new Response().addParameter("headers","Custom-Header:jUnit")
												  .addHeader("Content-Type", "application/json")
												  .setBody(responseBody)
								  );
		String requestBody = "{\"request\":\"true\"}";
		
		// send a request to the HTTP API mock service
		HttpResponse<String> response = new RestRequest(requestBody,
														"http://localhost:8888/mock/services/REST/local-mock/endpoint", 
														"post")
													    .addHeader("Custom-Request-Header", "jUnit request")
													    .sendRequest();
		
		// make sure that we got the correct response
		assertTrue("Http status code expected to be 200", response.getStatus() == 200);
		assertTrue("Http status text expected to be OK", response.getStatusText().equals("OK"));
		assertTrue("Response body expected to be: " + responseBody, response.getBody().equals(responseBody));
		assertTrue("Response should contain header: Custom-Header:junit", response.getHeaders().get("Custom-Header").get(0).equals("jUnit"));
		
		// make sure that HTTP API mock service received correct headers
		RecordedHeaders recordedHeaders = getRecordedHeadersFromApiMock("http://localhost:8888/mock/services/REST/local-mock/operations/POST/recorded-request-headers");
		
		Document doc = builder.parse(new ByteArrayInputStream(recordedHeaders.getContent().getBytes(StandardCharsets.UTF_8)));

		assertTrue("Recorded headers response content type expected to be: text/xml", 
				fetchStringWithXpath(doc, "//name[text() = 'Content-Type']/../value/text()").equals("text/plain; charset=UTF-8"));
		
		assertTrue("Expected recorded header Custom-Request-Header: jUnit request", 
        						fetchStringWithXpath(doc, "//name[text() = 'Custom-Request-Header']/../value/text()").equals("jUnit request"));

		// make sure that HTTP API mock service received the correct request
		RecordedRequests recordedRequest = getRecordedRequestsFromApiMock("http://localhost:8888/mock/services/REST/local-mock/operations/POST/recorded-requests");
		
		doc = builder.parse(new ByteArrayInputStream(recordedRequest.getContent().getBytes(StandardCharsets.UTF_8)));
		
		assertTrue("Expected recorded request:" + requestBody, 
				fetchStringWithXpath(doc, "normalize-space(//recorded-requests/text())").equals(requestBody));
	}

}
```
## Technical Requirements

#### Usage

* Oracle Java 7 or above

#### Development
* Oracle Java 7 + Maven 3.X.X

Fork, develop, create a pull request.
Remember to add some tests!

### Contributors

- [Kreshnik Gunga](https://github.com/kgunga)
- [Ville Harvala](https://github.com/vharvala)

## [License](LICENSE)

Copyright &copy; 2016 [Mystes Oy](http://www.mystes.fi). Licensed under the [Apache 2.0 License](LICENSE).