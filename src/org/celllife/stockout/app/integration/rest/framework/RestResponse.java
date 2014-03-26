package org.celllife.stockout.app.integration.rest.framework;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains all the necessary data that can be returned from a RestClient method call.
 */
public class RestResponse implements Serializable {
	
	private static final long serialVersionUID = 1068827088539641262L;

	private int code;
	private String errorMessage;
	private String data;

	
	/**
	 * Create a RestResponse for a successful situation given an HTTP code (e.g. 200, 201)
	 * @param code
	 */
	public RestResponse(int code) {
		super();
		this.code = code;
	}

	/**
	 * Retrieve response data (could be XML or Json)
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * HTTP error code (e.g. 404, 200, 500)
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Retrieve a string containing a description text of the error (if an error occurred, it will not be null or empty)
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * If the code is between 400 and 599, then assume it's an error
	 * @return boolean true if the code indicates an error
	 */
	public boolean isErrorCode() {
		if (this.code >= 400 && this.code <= 599) {
			return true;
		}
		return false;
	}

	/**
	 * Sets the error message. If the error message contains a webpage, only the content of the h1 tag will be used.
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		if (errorMessage.contains("<html>")) {
			Pattern p = Pattern.compile(".*<h1>(.*)</h1>.*", Pattern.DOTALL);
	    	Matcher matcher = p.matcher(errorMessage);
	    	if (matcher.matches()) {
	    		this.errorMessage = matcher.group(1);
	    	} else {
	    		this.errorMessage = errorMessage;
	    	}
		} else {
			this.errorMessage = errorMessage;
		}
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RestResponse [code=" + code + ", errorMessage=" + errorMessage + ", data=" + data + "]";
	}
}
