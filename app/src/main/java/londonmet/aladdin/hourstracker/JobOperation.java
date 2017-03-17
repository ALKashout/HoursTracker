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
public class JobOperation {
    private DatabaseHelper dbHelper;
    private String[] allColumns = {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_JOB_NAME, DatabaseHelper.KEY_JOB_RATE, DatabaseHelper.KEY_JOB_PAYMENT};
    private SQLiteDatabase database;

    public JobOperation(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Job addJob(String name, String rate, String payment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_JOB_NAME, name);
        values.put(DatabaseHelper.KEY_JOB_RATE, rate);
        values.put(DatabaseHelper.KEY_JOB_PAYMENT, payment);

        long jobId = database.insert(DatabaseHelper.TABLE_JOB, null, values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_JOB, allColumns, DatabaseHelper.KEY_ID + "=" + jobId, null, null, null, null);
        cursor.moveToFirst();
        Job newJob = parseJob(cursor);
        cursor.close();
        return newJob;
    }

    public List getAllJobs() {
        List jobs = new ArrayList();

        Cursor cursor = database.query(DatabaseHelper.TABLE_JOB, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Job job = parseJob(cursor);
            jobs.add(job);
            cursor.moveToNext();
        }
        cursor.close();
        return jobs;
    }

    private Job parseJob(Cursor cursor) {
        Job job = new Job();
        job.setId(cursor.getInt(0));
        job.setJobName(cursor.getString(1));
        job.setJobRate(cursor.getString(2));
        job.setJobPayment(cursor.getString(3));
        return job;
    }


}
