package org.celllife.stockout.app.integration.rest.framework;

/**
 * General purpose exception for any kind of error that occurs with communication.
 * This would include HTTP status codes between 400 and 500 (except 401 which has its
 * own exception).
 */
public class RestCommunicationException extends RuntimeException {

	private static final long serialVersionUID = -759288549946383037L;

	public RestCommunicationException() {
		super();
	}

	public RestCommunicationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public RestCommunicationException(String detailMessage) {
		super(detailMessage);
	}

	public RestCommunicationException(Throwable throwable) {
		super(throwable);
	}
}
