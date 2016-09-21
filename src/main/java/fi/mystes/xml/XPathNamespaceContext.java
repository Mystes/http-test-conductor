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
package fi.mystes.xml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

/**
 * Implementation of NamespaceContext interface to allow namespace usages in XPath expressions.
 *
 */
public class XPathNamespaceContext implements NamespaceContext{

	private Map<String, String> namespaces;
	
	public XPathNamespaceContext(Map<String, String> namespaces) {
		this.namespaces = namespaces;
	}

	public String getNamespaceURI(String prefix) {
		return namespaces.get(prefix);
	}

	public String getPrefix(String uri) {
		List<String> prefixes = prefixesByURI(uri);
		String prefix = null;
		if (!prefixes.isEmpty()) {
			prefix = prefixes.get(0);
		}
		return prefix;
	}

	@SuppressWarnings("rawtypes")
	public Iterator getPrefixes(String uri) {
		return prefixesByURI(uri).iterator();
	}

	public void setNamespaces(Map<String, String> namespaces) {
		this.namespaces = namespaces;
	}
	
	private List<String> prefixesByURI(String uri){
		List<String> prefixes = new LinkedList<String>();
		for(String prefix : namespaces.keySet()) {
			String nsURI = namespaces.get(prefix);
			if (nsURI.equals(uri)) {
				prefixes.add(prefix);
			}
		}
		return prefixes;
	}
	
}
