package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class InDBTransactionDAO implements TransactionDAO {

    private DbHelper dbHelper;
    SimpleDateFormat format;

    public InDBTransactionDAO(Context context){
        dbHelper=new DbHelper(context);
        format= new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbHelper.LogTBL.COLUMN_NAME_DATE, format.format(date));
        values.put(DbHelper.LogTBL.COLUMN_NAME_ACC_NO, accountNo);
        values.put(DbHelper.LogTBL.COLUMN_NAME_TYPE, expenseType.toString());
        values.put(DbHelper.LogTBL.COLUMN_NAME_AMOUNT, amount);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DbHelper.LogTBL.TABLE_NAME, null, values);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DbHelper.LogTBL.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List transactionList = new ArrayList<>();
        while(cursor.moveToNext()) {


            Date dateobj = new Date();
            try{
                dateobj = format.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LogTBL.COLUMN_NAME_DATE)));
            }
            catch (ParseException ignored){}

            ExpenseType exType=null;

            if(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LogTBL.COLUMN_NAME_TYPE)).equals("EXPENSE") )
                exType=ExpenseType.EXPENSE;
            else
                exType=ExpenseType.INCOME;

            Transaction transaction = new Transaction(dateobj,
                    cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LogTBL.COLUMN_NAME_ACC_NO)),
                    exType,
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.LogTBL.COLUMN_NAME_AMOUNT)));

            transactionList.add(transaction);
        }
        cursor.close();

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sortOrder = DbHelper.LogTBL.COLUMN_NAME_DATE + " DESC";
        Cursor cursor = db.query(
                DbHelper.LogTBL.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder,              // The sort order
                String.valueOf(limit)
        );

        List transactionList = new ArrayList<>();
        while(cursor.moveToNext()) {


            Date dateobj = new Date();
            try{
                dateobj = format.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LogTBL.COLUMN_NAME_DATE)));
            }
            catch (ParseException ignored){}

            ExpenseType exType=null;

            if(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LogTBL.COLUMN_NAME_TYPE)).equals("EXPENSE") )
                exType=ExpenseType.EXPENSE;
            else
                exType=ExpenseType.INCOME;

            Transaction transaction = new Transaction(dateobj,
                    cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.LogTBL.COLUMN_NAME_ACC_NO)),
                    exType,
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.LogTBL.COLUMN_NAME_AMOUNT)));

            transactionList.add(transaction);
        }
        cursor.close();

        return transactionList;
    }
}
