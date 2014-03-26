package org.celllife.stockout.app.integration.rest.framework;

/**
 * REST communication error for authentication failures. This is HTTP status code 401 only.
 */
public class RestAuthenticationException extends Exception {

	private static final long serialVersionUID = -1616178650831690446L;

	public RestAuthenticationException() {
		super();
	}

	public RestAuthenticationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public RestAuthenticationException(String detailMessage) {
		super(detailMessage);
	}

	public RestAuthenticationException(Throwable throwable) {
		super(throwable);
	}
}