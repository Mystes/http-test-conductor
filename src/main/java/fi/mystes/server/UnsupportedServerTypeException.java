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
package fi.mystes.server;

/**
 * Class for unsupported server type exception.
 * 
 */
@SuppressWarnings("serial")
public class UnsupportedServerTypeException extends Exception{

	public UnsupportedServerTypeException(){
		super();
	}
	
	public UnsupportedServerTypeException(String message) {
		super(message);
	}
}
