package org.celllife.stockout.app.domain;

import java.util.Date;

import org.celllife.stockout.app.database.framework.Entity;

/**
 * Server Communication Log Entity that defines an interaction with the server.
 * For example the app connects to the server to retrieve the latest alerts.
 * This log is used to ensure that the app communicates regularly with the
 * server.
 */
public class ServerCommunicationLog implements Entity {

    private static final long serialVersionUID = -2212401177602288047L;

    private Long id;

    private Date date;

    private ServerCommunicationType type;

    private Boolean success;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * An indication of the type of communication. For example "Alert" or
     * "Stock"
     */
    public ServerCommunicationType getType() {
        return type;
    }

    public void setType(ServerCommunicationType type) {
        this.type = type;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerCommunicationLog other = (ServerCommunicationLog) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Notification [id=" + id + ", date=" + date + ", type=" + type + ", success=" + success + "]";
    }
}
