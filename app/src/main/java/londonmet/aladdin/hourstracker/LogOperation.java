package londonmet.aladdin.hourstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aladdin on 20/05/2015.
 */
public class LogOperation {
    private DatabaseHelper dbHelper;
    private String[] allColumns = {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_LOG_START, DatabaseHelper.KEY_LOG_END, DatabaseHelper.KEY_LOG_DATE, DatabaseHelper.KEY_LOG_TOTAL};
    private SQLiteDatabase database;

    public LogOperation(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Log addLog(String start, String end, String date, String total) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_LOG_START, start);
        values.put(DatabaseHelper.KEY_LOG_END, end);
        values.put(DatabaseHelper.KEY_LOG_DATE, date);
        values.put(DatabaseHelper.KEY_LOG_TOTAL, total);

        long logId = database.insert(DatabaseHelper.TABLE_LOG, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_LOG, allColumns, DatabaseHelper.KEY_ID + "=" + logId, null, null, null, null);
        cursor.moveToFirst();
        Log newLog = parseLog(cursor);
        cursor.close();
        return newLog;
    }

    public List getAlllogs() {
        List logs = new ArrayList();

        Cursor cursor = database.query(DatabaseHelper.TABLE_LOG, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log log = parseLog(cursor);
            logs.add(log);
            cursor.moveToNext();
        }
        cursor.close();
        return logs;
    }

    private Log parseLog(Cursor cursor) {
        Log log = new Log();
        log.setId(cursor.getInt(0));
        log.setLogStart(cursor.getString(1));
        log.setLogEnd(cursor.getString(2));
        log.setLogDate(cursor.getString(3));
        log.setLogTotal(cursor.getString(4));
        return log;
    }

}
