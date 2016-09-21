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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import fi.mystes.http.ConfigurableHttpEntity;

/**
 * Class representing SOAP request. This class uses Unirest to send SOAP requests.
 * To use SOAP 1.2 set HTTP header Content as application/soap+xml or application/soap+fastinfoset.
 *
 */
public class SoapRequest extends ConfigurableHttpEntity<SoapRequest> implements IRequest<SOAPMessage> {
	
	/** Possible namespaces used by SOAP body */
	private Map<String, String> namespaces;
	/** Instance of SOAP message */
	private SOAPMessage requestMessage;
	/** SOAP headers */
	private Map<String, String> soapHeaders;
	/** SOAP attachments, key = content-type, value = content */
	private Map<String, String> attachments;
	
	/**
	 * Default constructor.
	 */
	public SoapRequest(){
		this(null, null);
	}
	
	/**
	 * Constructor with SOAP body content and end point URL
	 * 
	 * @param content SOAP body content
	 * @param endpointUrl End point URL
	 */
	public SoapRequest(String content, String endpointUrl){
		this(content, endpointUrl, null);
	}
	
	/**
	 * Constructor with SOAP body content, end point URL, and name spaces.
	 * 
	 * @param content SOAP body content
	 * @param endpointUrl End point URL
	 * @param namespaces Name spaces used in SAOP body content
	 */
	public SoapRequest(String content, String endpointUrl, Map<String, String> namespaces) {
		this(content, endpointUrl, namespaces, null);
	}
	
	/**
	 * Constructor with SOAP body content, end point URL, name spaces, and HTTP headers.
	 * 
	 * @param content SOAP body content
	 * @param endpointUrl End point URL
	 * @param namespaces Name spaces used in SAOP body content
	 * @param headers HTTP headers
	 */
	public SoapRequest(String content, String endpointUrl, Map<String, String> namespaces, Map<String, String> headers) {
		super(SoapRequest.class);
		setBody(content).setEndpointUrl(endpointUrl).setNamespaces(namespaces).setHeaders(headers);
	}
	
	/**
	 * Constructor with SOAP body content, end point URL, name spaces, and HTTP headers.
	 * 
	 * @param content SOAP body content
	 * @param endpointUrl End point URL
	 * @param namespaces Name spaces used in SAOP body content
	 * @param headers HTTP headers
	 */
	public SoapRequest(String content, String endpointUrl, Map<String, String> namespaces, Map<String, String> headers, Map<String, String> soapHeaders) {
		super(SoapRequest.class);
		setBody(content).setEndpointUrl(endpointUrl).setNamespaces(namespaces).setHeaders(headers).setSoapHeaders(soapHeaders);
	}
	
	
	public SOAPMessage sendRequest() throws Exception {
		// Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        createSOAPRequest();
        
        SOAPMessage response = soapConnection.call(requestMessage, url);
        soapConnection.close();
        
        return response;
	}
	
	/**
	 * Adds name space.
	 * 
	 * @param prefix Name space prefix
	 * @param uri Name space URI
	 * 
	 * @return Instance of SoapRequest
	 */
	public SoapRequest addNamespace(String prefix, String uri) {
		if (namespaces == null) {
			namespaces = new HashMap<String, String>();
		}
		namespaces.put(prefix, uri);
		
		return this;
	}
	
	/**
	 * Sets name spaces.
	 * 
	 * @param namespaces Name spaces with prefixed as keys.
	 * 
	 * @return Instance of SoapRequest
	 */
	public SoapRequest setNamespaces(Map<String, String> namespaces) {
		this.namespaces = namespaces;
		return this;
	}
	
	/**
	 * Sets SOAP headers.
	 * 
	 * @param soapHeaders SOAP headers
	 * 
	 * @return Instance of SoapRequest
	 */
	public SoapRequest setSoapHeaders(Map<String, String> soapHeaders) {
		this.soapHeaders = soapHeaders;
		return this;
	}
	
	/**
	 * Sets attachments. Key must be the content type (text/plain, text/xml, etc.) and value must be the content.
	 * 
	 * @param attachments Attachments to be added to SOAP request
	 * 
	 * @return Instance of SoapRequest
	 */
	public SoapRequest setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
		return this;
	}
	
	/**
	 * Helper method to create SOAP message.
	 * 
	 * @return Created SOAPMessage instance
	 * 
	 * @throws Exception If SOAPMessage instance creation fails
	 */
	private SOAPMessage createSOAPRequest() throws Exception {
		// SOAP Envelope
        SOAPEnvelope envelope = initRequest();
        
        // HTTP headers
        setMimeHeaders(requestMessage);
        
        // SOAP headers
        setSaopHeaders(requestMessage);
        
        // Attachments
        setAttachments(requestMessage);
        
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        setContentToBody(soapBody);

        requestMessage.saveChanges();
        return requestMessage;
    }
	
	/**
	 * Helper method to initialize SOAP request.
	 * 
	 * @return Instance of SOAPEnbelope.
	 * @throws SOAPException
	 */
	private SOAPEnvelope initRequest() throws SOAPException {
		MessageFactory messageFactory = MessageFactory.newInstance();
        requestMessage = messageFactory.createMessage();
        SOAPPart soapPart = requestMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        addNamespaceDeclarations(envelope);
        
        return envelope;
	}
	
	/**
	 * Adds name space declarations to given SOAP envelope instance.
	 * 
	 * @param envelope SOAP envelope to add name space declarations to
	 * 
	 * @throws SOAPException If name space declarations addition fails
	 */
	private void addNamespaceDeclarations(SOAPEnvelope envelope) throws SOAPException {
		if (namespaces == null) {
			return;
		}
		for (String prefix : namespaces.keySet()) {
			envelope.addNamespaceDeclaration(prefix, namespaces.get(prefix));
		}
	}
	
	/**
	 * Sets Mime headers to given SOAP message.
	 * 
	 * @param soapMessage SOAP message to set Mime headers to
	 */
	private void setMimeHeaders(SOAPMessage soapMessage){
		if (headers == null) {
			return;
		}
		MimeHeaders mimeHeaders = soapMessage.getMimeHeaders();
		for (String header : headers.keySet()) {
			mimeHeaders.addHeader(header, headers.get(header));
		}
	}
	
	/**
	 * Sets SOAP headers to given SOAP message.
	 * 
	 * @param soapMessage SOAP message to set SOAP headers to
	 * @throws SOAPException 
	 * @throws DOMException 
	 */
	private void setSaopHeaders(SOAPMessage soapMessage) throws DOMException, SOAPException {
		if (soapHeaders == null) {
			return;
		}
		for (String header : soapHeaders.keySet()) {
			soapMessage.getSOAPHeader().addHeaderElement(new QName(header)).setTextContent(soapHeaders.get(header));
		}
	}
	
	/**
	 * Sets attachments to given SOAP message.
	 * 
	 * @param soapMessage SOAP message to set attachments to
	 */
	private void setAttachments(SOAPMessage soapMessage) {
		if (attachments == null) {
			return;
		}
		for (String attachment : attachments.keySet()) {
			AttachmentPart attachmentPart = soapMessage.createAttachmentPart(attachments.get(attachment), attachment);
			soapMessage.addAttachmentPart(attachmentPart);
		}
	}
	
	/**
	 * Sets content to given body.
	 * 
	 * @param soapBody SOAP body to set content to
	 * 
	 * @return Instance of SoapRequest
	 * 
	 * @throws Exception If setting content to SOAP body fails
	 */
	private SoapRequest setContentToBody(SOAPBody soapBody) throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        InputStream stream  = new ByteArrayInputStream(content.getBytes());
        Document doc = builderFactory.newDocumentBuilder().parse(stream); 
        soapBody.addDocument(doc);
		return this;
	}
}
