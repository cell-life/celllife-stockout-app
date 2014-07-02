package org.celllife.stockout.app.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.celllife.stockout.app.database.framework.TableAdapter;
import org.celllife.stockout.app.domain.ServerCommunicationLog;
import org.celllife.stockout.app.domain.ServerCommunicationType;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This is the Data Access Object (DAO) for the Server Communication table. All methods
 * required to save, update, or retrieve Users from the database should be
 * defined here.
 */
public class ServerCommunicationLogTableAdapter extends TableAdapter<ServerCommunicationLog> {

    // Phone Table Name
    private static final String TABLE_SERVER_COMMUNICATION_LOG = "server_communication_log";

    // Phone Table Column Names
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TYPE = "type";
    private static final String SUCCESS = "success";

    // Phone Queries
    private static final String QUERY_LAST_SUCCESSFUL_COMMUNICATION = "SELECT  * FROM " + TABLE_SERVER_COMMUNICATION_LOG + " WHERE "
            + SUCCESS + " = 1" + " ORDER BY " + DATE + " DESC LIMIT 1";

    String CREATE_NOTIFICATION_TABLE = 
            "CREATE TABLE " + TABLE_SERVER_COMMUNICATION_LOG + " (" 
            + ID + " INTEGER PRIMARY KEY, " 
            + DATE + " NUMERIC, " 
            + TYPE + " TEXT, " 
            + SUCCESS + " INTEGER )";

    public ServerCommunicationLogTableAdapter() {
    }

    @Override
    public String getCreateTableSql() {
        return CREATE_NOTIFICATION_TABLE;
    }

    @Override
    public String getTableName() {
        return TABLE_SERVER_COMMUNICATION_LOG;
    }

    @Override
    public List<ContentValues> getInitialData() {
        List<ContentValues> initialData = new ArrayList<ContentValues>();
        return initialData;
    }

    @Override
    protected ContentValues createContentValues(ServerCommunicationLog log) {
        ContentValues values = new ContentValues();
        values.put(DATE, log.getDate().getTime());
        values.put(TYPE, log.getType().toString());
        values.put(SUCCESS, (log.isSuccess()) ? 1 : 0);
        return values;
    }

    @Override
    public ServerCommunicationLog readFromCursor(Cursor c) {
        if (c.getCount() > 0) {
            ServerCommunicationLog l = new ServerCommunicationLog();
            l.setId(c.getLong(c.getColumnIndex(ID)));
            l.setDate(new Date(c.getLong(c.getColumnIndex(DATE))));
            ServerCommunicationType type = ServerCommunicationType.valueOf(c.getString(c.getColumnIndex(TYPE)));
            l.setType(type);
            int success = c.getInt(c.getColumnIndex(SUCCESS));
            l.setSuccess((success == 1) ? true : false);
            return l;
        }
        return null;
    }

    // Add the table specific CRUD operations here

    public ServerCommunicationLog findLastSucccess() {
        return db.find(this, QUERY_LAST_SUCCESSFUL_COMMUNICATION, new String[] {});
    }
}
