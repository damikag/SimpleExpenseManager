package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "expenseManager.db";

    //  AccountTBL
    private static final String SQL_CREATE_ENTRIES_ACCOUNT =
            "CREATE TABLE " + AccountTBL.TABLE_NAME + " (" +
                    AccountTBL.COLUMN_NAME_ACC_NO + " TEXT PRIMARY KEY," +
                    AccountTBL.COLUMN_NAME_BANK + " TEXT," +
                    AccountTBL.COLUMN_NAME_ACC_HOLDER + " TEXT," +
                    AccountTBL.COLUMN_NAME_BALANCE + " REAL )";

    private static final String SQL_DELETE_ENTRIES_ACCOUNT =
            "DROP TABLE IF EXISTS " + AccountTBL.TABLE_NAME;

    //  LogTBL
    private static final String SQL_CREATE_ENTRIES_LOG =
            "CREATE TABLE " + LogTBL.TABLE_NAME + " (" +
                    LogTBL._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LogTBL.COLUMN_NAME_ACC_NO + " TEXT," +
                    LogTBL.COLUMN_NAME_DATE + " TEXT," +
                    LogTBL.COLUMN_NAME_TYPE + " TEXT," +
                    LogTBL.COLUMN_NAME_AMOUNT + " REAL )";

    private static final String SQL_DELETE_ENTRIES_LOG =
            "DROP TABLE IF EXISTS " + LogTBL.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_ACCOUNT);
        db.execSQL(SQL_CREATE_ENTRIES_LOG);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES_ACCOUNT);
        db.execSQL(SQL_DELETE_ENTRIES_LOG);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /* Inner class that defines the table contents */
    public static class AccountTBL implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_ACC_NO = "acc_no";
        public static final String COLUMN_NAME_BANK = "bank";
        public static final String COLUMN_NAME_ACC_HOLDER = "acc_holder";
        public static final String COLUMN_NAME_BALANCE = "balance";
    }

    /* Inner class that defines the table contents */
    public static class LogTBL implements BaseColumns {
        public static final String TABLE_NAME = "log";
        public static final String COLUMN_NAME_ACC_NO = "acc_no";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_AMOUNT = "amount";
//        public static final String COLUMN_NAME_ID = "id";
    }
}
