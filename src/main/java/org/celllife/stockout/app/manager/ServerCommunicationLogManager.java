package org.celllife.stockout.app.manager;

import org.celllife.stockout.app.domain.ServerCommunicationLog;
import org.celllife.stockout.app.domain.ServerCommunicationType;

/**
 * Manages the notifications for the server interactions
 */
public interface ServerCommunicationLogManager {

    /**
     * Retrieves the Notification for the last successful server interaction
     * 
     * @return Notification, can be null
     */
    ServerCommunicationLog getLastSuccessfulServerInteraction();

    /**
     * Creates and inserts a ServerCommunicationLog entity with the specified
     * details
     * 
     * @param type
     *            ServerCommunicationType (either ALERT or STOCK)
     * @param success
     *            boolean true if the communication was considered successful
     */
    void createServerCommunicationLog(ServerCommunicationType type, boolean success);

    /**
     * Determines if the app has been offline for longer than the allowed
     * period. This period is configured in the settings as "offlineDays" and is
     * defaulted to 5 days.
     * 
     * Returns true if the app is past the point at which an offline
     * notification should be displayed to the user.
     * 
     * If there has never been any successful communication, this method will
     * return true
     * 
     * @return boolean
     */
    boolean displayOfflineNotification();
}