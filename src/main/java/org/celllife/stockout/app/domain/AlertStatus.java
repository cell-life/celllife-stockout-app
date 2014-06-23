package org.celllife.stockout.app.domain;

/**
 * Status of the Alert
 * NEW = alert has been received
 * SENT = new alert has been sent to the mobile client
 * RESOLVED = a stock take triggers off a stock order
 * CLOSED = new stock arrives
 * EXPIRED = alert has been replaced by a newer alert
 */
public enum AlertStatus {
	NEW,
	SENT,
	RESOLVED,
	CLOSED,
	EXPIRED
}
