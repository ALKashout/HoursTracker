package londonmet.aladdin.hourstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aladdin on 20/05/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "trackdb";
    public static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_JOB = "job";
    public static final String TABLE_LOG = "log";
    public static final String TABLE_JOB_LOG = "job_log";

    // Common columns
    public static final String KEY_ID = "_id";

    // Job Table Columns
    public static final String KEY_JOB_NAME = "name";
    public static final String KEY_JOB_RATE = "rate";
    public static final String KEY_JOB_PAYMENT = "payment";

    // Log Table Columns
    public static final String KEY_LOG_START = "start";
    public static final String KEY_LOG_END = "end";
    public static final String KEY_LOG_DATE = "date";
    public static final String KEY_LOG_TOTAL = "total";

    // Job_Log Table Columns
    public static final String KEY_JOB_ID = "job_id";
    public static final String KEY_LOG_ID = "log_id";

    // Job Table Create Statement
    public static final String CREATE_TABLE_JOB = "CREATE TABLE "+TABLE_JOB+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_JOB_NAME+" VARCHAR(255), "+KEY_JOB_RATE+" VARCHAR(255), "+KEY_JOB_PAYMENT+" VARCHAR(255));";

    // Log Table Create Statement
    public static final String CREATE_TABLE_LOG = "CREATE TABLE "+TABLE_LOG+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_LOG_START+" VARCHAR(255), "+KEY_LOG_END+" VARCHAR(255), "+KEY_LOG_DATE+" VARCHAR(255), "+KEY_LOG_TOTAL+" VARCHAR(255));";

    //JOB_LOG Table Create Statement
    public static final String CREATE_TABLE_JOB_LOG = "CREATE TABLE "+TABLE_JOB_LOG+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_JOB_ID+" INTEGER, "+KEY_LOG_ID+" INTEGER);";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_JOB);
        db.execSQL(CREATE_TABLE_LOG);
        db.execSQL(CREATE_TABLE_JOB_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB_LOG);

        onCreate(db);
    }
}
