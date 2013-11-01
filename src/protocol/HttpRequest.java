/*
 * HttpRequest.java
 * Oct 7, 2012
 *
 * Simple Web Server (SWS) for CSSE 477
 * 
 * Copyright (C) 2012 Chandan Raj Rupakheti
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 */

 
package protocol;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a request object for HTTP.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class HttpRequest {
	private String method;
	private String uri;
	private String version;
	private Map<String, String> header;
	private String body;
	
	private HttpRequest() {
		this.header = new HashMap<String, String>();
		this.body = "";
	}
	
	/**
	 * The request method.
	 * 
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * The URI of the request object.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * The version of the http request.
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * The key to value mapping in the request header fields.
	 * 
	 * @return the header
	 */
	public Map<String, String> getHeader() {
		// Lets return the unmodifable view of the header map
		return Collections.unmodifiableMap(header);
	}
	
	/**
	 * @return body of the request
	 */
	public String getBody() {
		return this.body;
	}

	/**
	 * Reads raw data from the supplied input stream and constructs a 
	 * <tt>HttpRequest</tt> object out of the raw data.
	 * 
	 * @param inputStream The input stream to read from.
	 * @return A <tt>HttpRequest</tt> object.
	 * @throws Exception Throws either {@link ProtocolException} for bad request or 
	 * {@link IOException} for socket input stream read errors.
	 */
	public static HttpRequest read(InputStream inputStream, int bufferSize) throws Exception {
		
		// We will fill this object with the data from input stream and return it
		HttpRequest request = new HttpRequest();
		
		// Load the request into a string and use a Scanner to move through it
		DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));
		byte[] bytes = new byte[bufferSize];
		in.read(bytes);
		String reqString = new String(bytes);
		Scanner reader = new Scanner(reqString);
		
		//First Request Line: GET /somedir/page.html HTTP/1.1
		String line = reader.nextLine(); // A line ends with either a \r, or a \n, or both
		
		if(line == null) {
			reader.close();
			throw new ProtocolException(Protocol.BAD_REQUEST_CODE, Protocol.BAD_REQUEST_TEXT);
		}
		
		// We will break this line using space as delimeter into three parts
		StringTokenizer tokenizer = new StringTokenizer(line, " ");
		
		// Error checking the first line must have exactly three elements
		if(tokenizer.countTokens() != 3) {
			reader.close();
			throw new ProtocolException(Protocol.BAD_REQUEST_CODE, Protocol.BAD_REQUEST_TEXT);
		}
		
		request.method = tokenizer.nextToken();		// GET
		request.uri = tokenizer.nextToken();		// /somedir/page.html
		request.version = tokenizer.nextToken();	// HTTP/1.1
		
		// Rest of the request is a header that maps keys to values
		// e.g. Host: www.rose-hulman.edu
		// We will convert both the strings to lower case to be able to search later
		while(reader.hasNext() && !(line = reader.nextLine()).isEmpty()) {
			Pattern regex = Pattern.compile("(?<key>.+): (?<value>.+)");
			Matcher matcher = regex.matcher(line);
			if (matcher.matches()) {
				// Now lets break the string in two parts
				String key = matcher.group("key").toLowerCase();
				String value = matcher.group("value");
				
				// Now lets put the key=>value mapping to the header map
				request.header.put(key, value);
			} else {
				// We didnt match the header pattern. This is a bad request
				reader.close();
				throw new ProtocolException(Protocol.BAD_REQUEST_CODE, Protocol.BAD_REQUEST_TEXT);
			}						
		}
				
		if(request.method.equals(Protocol.PUT) || request.method.equals(Protocol.POST)) {
			while(reader.hasNext()) {
				line = reader.nextLine();
				request.body += line;
			}
		}
		
		reader.close();
		return request;
	}
	
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("----------------------------------\n");
		buffer.append(this.method);
		buffer.append(Protocol.SPACE);
		buffer.append(this.uri);
		buffer.append(Protocol.SPACE);
		buffer.append(this.version);
		buffer.append(Protocol.LF);
		
		for(Map.Entry<String, String> entry : this.header.entrySet()) {
			buffer.append(entry.getKey());
			buffer.append(Protocol.SEPERATOR);
			buffer.append(Protocol.SPACE);
			buffer.append(entry.getValue());
			buffer.append(Protocol.LF);
		}
		
		if(!this.body.equals(null)) {
			buffer.append("\n");
			buffer.append(this.body + "\n");
		}
		
		buffer.append("----------------------------------\n");
		return buffer.toString();
	}

}
