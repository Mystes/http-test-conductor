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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fi.mystes.mock.ApiMockFactory;
import fi.mystes.mock.IApiMock;
import fi.mystes.request.RecordedHeaders;
import fi.mystes.request.RecordedRequests;
import fi.mystes.response.Response;
import fi.mystes.server.IMockServer;
import fi.mystes.server.ServerFactory;
import fi.mystes.xml.XPathNamespaceContext;

/**
 * Abstract test class which provides testing utilities. This class should be extended by
 * all jUnit testing classes when testing integrations.<br/><br/>
 * 
 * IntegrationTestUtil class uses conf.properties file which contains all necessary configuration properties.
 * All configuration properties can be overriden by project's own conf.properties. The configuration file should be
 * placed under src/main/resources directory in project.<br/><br/>
 * 
 * Configuration file consists of the following properties:<br/><br/>
 * # Path to HttpApiMock Web application<br/>
 * http.test.conductor.webAppFileUrl = lib/http-api-mock.war<br/><br/>
 * 
 * # Extra classpath for mockable APIs file ws-mock.properties<br/>
 * http.test.conductor.extraClasspath = src/test/resources/web<br/><br/>
 * 
 * # Web App context path<br/>
 * http.test.conductor.webAppContextPath = /mock<br/><br/>
 * 
 * # Supported server type<br/>
 * http.test.conductor.serverType = JETTY<br/><br/>
 * 
 * # Server port<br/>
 * http.test.conductor.serverPort = 8888<br/><br/>
 * 
 * # Supported API Mock which implements fi.mystes.mock.IApiMock interface<br/>
 * http.test.conductor.apiMock = fi.mystes.mock.HttpApiMock<br/><br/>
 *
 */
public abstract class HttpTestConductor {
	/** Extra classpath for mockable APIs file ws-mock.properties */
	protected static String extraClassath;
	
	/** Path to HttpApiMock Web application */
	protected static String webAppFileUrl;
	
	/** Web App context path */
	protected static String webAppContextPath;
	
	/** Supported server type */
	protected static String serverType;
	
	/** Server port */
	protected static Integer serverPort;
	
	/** Mock server reference */
	private static IMockServer server;
	
	/** API Mock reference */
	private static IApiMock<?> apiMock;
	
	/** Helper factory to build document builder */
	private static DocumentBuilderFactory factory;
	
	/** Helper document builder to query documents using XPath */
    protected static DocumentBuilder builder;
    
    /** Helper XPath object to execute XPath expressions into XML documents */
    protected static XPath xpath = XPathFactory.newInstance().newXPath();
    
    /** XPath namespaces */
    protected static Map<String, String> xpathNamespaces = new HashMap<String, String>();
    
    /** Namespace context needed for XPath expressions with namespaces */
    protected static NamespaceContext namespaceContext = new XPathNamespaceContext(xpathNamespaces);;
	
	/**
	 * Initialization method which will be called before any instance of implementing class is created.<br/>
	 * This method will load configuration properties and initialize mock server. Also it will initialize
	 * XPath utility and API mock objects.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void init() throws Exception {
		Properties properties = getConfiguration();
		
		extraClassath = properties.getProperty("http.test.conductor.extraClasspath").trim();
		
		webAppFileUrl = properties.getProperty("http.test.conductor.webAppFileUrl").trim();
		
		webAppContextPath = properties.getProperty("http.test.conductor.webAppContextPath").trim();
		
		serverType = properties.getProperty("http.test.conductor.serverType");
		
		serverPort = Integer.parseInt(properties.getProperty("http.test.conductor.serverPort").trim());
		
		apiMock = ApiMockFactory.createMockApi(properties.getProperty("http.test.conductor.apiMock").trim());
		
		server = ServerFactory.createServer(ServerFactory.Type.valueOf(serverType), serverPort);
		server.addWebApp(webAppFileUrl, webAppContextPath, extraClassath);
	    server.tearUp(apiMock, serverPort);
	    
	    factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    builder = factory.newDocumentBuilder();
	    
	    xpath.setNamespaceContext(namespaceContext);
	}
	
	/**
	 * Helper method to stop server after all test cases of extending class are done.
	 * 
	 * @throws Exception If stopping server fails
	 */
	@AfterClass
	public static void tearDown() throws Exception {
		server.tearDown();
	}
	
	/**
	 * Performs given XPath expression to fetch string from given document.
	 * 
	 * @param document Contains XML data
	 * @param expression XPath expression to be performed
	 * 
	 * @return Matched XPath expression string value
	 * 
	 * @throws Exception If XPath expression evaluating fails
	 */
	protected String fetchStringWithXpath(Document document, String expression) throws Exception {
		return (String)xpath.compile(expression).evaluate(document, XPathConstants.STRING);
	}
	
	/**
	 * Performs given XPath expression to fetch boolean value from given document.
	 * 
	 * @param document Contains XML data
	 * @param expression XPath expression to be performed
	 * 
	 * @return Matched XPath expression boolean value
	 * 
	 * @throws Exception If XPath expression evaluating fails
	 */
	protected Boolean fetchBooleanWithXpath(Document document, String expression) throws Exception {
		return (Boolean)xpath.compile(expression).evaluate(document, XPathConstants.BOOLEAN);
	}
	
	/**
	 * Performs given XPath expression to fetch org.w3c.dom.Node from given document.
	 * 
	 * @param document Contains XML data
	 * @param expression XPath expression to be performed
	 * 
	 * @return Matched XPath expression org.w3c.dom.Node
	 * 
	 * @throws Exception If XPath expression evaluating fails
	 */
	protected Node fetchNodeWithXpath(Document document, String expression) throws Exception {
		return (Node)xpath.compile(expression).evaluate(document, XPathConstants.NODE);
	}
	
	/**
	 * Performs given XPath expression to fetch org.w3c.dom.NodeList from given document.
	 * 
	 * @param document Contains XML data
	 * @param expression XPath expression to be performed
	 * 
	 * @return Matched XPath expression org.w3c.dom.NodeList
	 * 
	 * @throws Exception If XPath expression evaluating fails
	 */
	protected NodeList fetchNodeListWithXpath(Document document, String expression) throws Exception {
		return (NodeList)xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
	}
	
	/**
	 * Performs given XPath expression to fetch java.lang.Number from given document.
	 * 
	 * @param document Contains XML data
	 * @param expression XPath expression to be performed
	 * 
	 * @return Matched XPath expression java.lang.Number
	 * 
	 * @throws Exception If XPath expression evaluating fails
	 */
	protected Number fetchNumberWithXpath(Document document, String expression) throws Exception {
		return (Number)xpath.compile(expression).evaluate(document, XPathConstants.NUMBER);
	}
	
	/**
	 * Helper method which initialized given API mock URI.
	 * 
	 * @param uri API mock URI
	 * 
	 * @return Instance of IApiMock class to allow chain invocations
	 * 
	 * @throws Exception If API mock initialization fails
	 */
	protected IApiMock<?> initApiMock(String uri) throws Exception {
		return (IApiMock<?>) apiMock.init(uri);
	}
	
	/**
	 * Helper method which adds customized response to API mock URI.
	 * 
	 * @param uri API mock URI to add customized response to
	 * @param response Customized response to be added
	 * 
	 * @return Instance of IApiMock class to allow chain invocations
	 * 
	 * @throws Exception If adding customized response to API mock fails
	 */
	protected IApiMock<?> addCustomResponseToApiMock(String uri, Response response) throws Exception {
		return (IApiMock<?>) apiMock.addCustomResponse(uri, response);
	}
	
	/**
	 * Helper method which fetches recorded requests from API mock URI.
	 * 
	 * @param uri To fetch recorded requests from
	 * 
	 * @return Instance of fi.mystes.request.RecordedRequests
	 * 
	 * @throws Exception If fetching recorded requests from API mock fails
	 */
	protected RecordedRequests getRecordedRequestsFromApiMock(String uri) throws Exception {
		return apiMock.getRecordedRequests(uri);
	}
	
	/**
	 * Helper method which fetches recorded headers from API mock URI.
	 * 
	 * @param uri To fetch recorded headers from
	 * 
	 * @return Instance of java.util.Map<String, String> containing fetched headers
	 * 
	 * @throws Exception If fetching recorded headers from API mock fails
	 */
	protected RecordedHeaders getRecordedHeadersFromApiMock(String uri) throws Exception {
		return apiMock.getRecordedHeaders(uri);
	}
	
	/**
	 * Helper method which reads configuration file.
	 * 
	 * @return Instance of java.util.Properties
	 * 
	 * @throws Exception If reading configuration file fails
	 */
	private static Properties getConfiguration() throws Exception {
		// Not working properly
		String propertyFileName = "/conf.properties";
		// The idea is to first to load configuration file of IntegrationTestUtil
		Properties properties = getDefaultProperties(propertyFileName);
		// and then load configuration file of implemented project
		Properties customProperties = getCustomProperties(propertyFileName);
		// and override only those properties which are configured in extending project
		properties.putAll(customProperties);
		
		return properties;
	}
	
	/**
	 * Helper method to read configuration file from IntegrationTestUtil.
	 * 
	 * @param propertyFileName File name containing configuration properties
	 * 
	 * @return Instance of java.util.Properties
	 * 
	 * @throws Exception If reading configuration file fails
	 */
	private static Properties getDefaultProperties(String propertyFileName) throws Exception {
		return loadProperties(HttpTestConductor.class,
				propertyFileName, 
				"Default config file '" + propertyFileName + "' not found in the classpath");
	}
	
	/**
	 * Helper method to read configuration file from extending project.
	 * 
	 * @param propertyFileName File name containing configuration properties.
	 * 
	 * @return Instance of java.util.Properties
	 * 
	 * @throws Exception If reading configuration file fails
	 */
	private static Properties getCustomProperties(String propertyFileName) throws Exception {
		return loadProperties(Thread.currentThread().getClass(), 
								propertyFileName, 
								"Custom conf file '" + propertyFileName + "' not found in the classpath");
	}
	
	/**
	 * Helper method to read configuration file based on given parameters.
	 * 
	 * @param clazz Contains class loader to load configuration properties from project where class resides
	 * @param propertyFileName File name containing configuration properties
	 * @param fileNotFoundMessage Error message to be thrown in case of file not found
	 * 
	 * @return Instance of java.util.Properties
	 * 
	 * @throws Exception If given configuration file not found or reading properties from it fails
	 */
	private static Properties loadProperties(Class<?> clazz, String propertyFileName, String fileNotFoundMessage) throws Exception {
		Properties properties = new Properties();

		InputStream inputStream = clazz.getResourceAsStream(propertyFileName);
		
		if (inputStream != null) {
			properties.load(inputStream);
			inputStream.close();
		} else {
			throw new FileNotFoundException(fileNotFoundMessage);
		}
		
		return properties;
	}

}