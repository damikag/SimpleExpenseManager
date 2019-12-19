package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class InDBAccountDAO implements AccountDAO {

    private DbHelper dbHelper;

    public InDBAccountDAO(Context context){
        dbHelper=new DbHelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

    // Define a projection that specifies which columns from the database
    // you will actually use after this query.
        String[] projection = {
                DbHelper.AccountTBL.COLUMN_NAME_ACC_NO
        };


        Cursor cursor = db.query(
                DbHelper.AccountTBL.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );


        List accNumList = new ArrayList<>();
        while(cursor.moveToNext()) {
            String accNum = cursor.getString(
                    cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_ACC_NO));
            accNumList.add(accNum);
        }
        cursor.close();

        return accNumList;
    }

    @Override
    public List<Account> getAccountsList() {


        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DbHelper.AccountTBL.COLUMN_NAME_ACC_NO,
                DbHelper.AccountTBL.COLUMN_NAME_BALANCE,
                DbHelper.AccountTBL.COLUMN_NAME_ACC_HOLDER,
                DbHelper.AccountTBL.COLUMN_NAME_BANK
        };

        String sortOrder =
                DbHelper.AccountTBL.COLUMN_NAME_ACC_NO + " ASC";

        Cursor cursor = db.query(
                DbHelper.AccountTBL.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        ArrayList accountList=new ArrayList<Account>();

        while(cursor.moveToNext()) {
            Account account = new Account(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_ACC_NO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_BANK)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_ACC_HOLDER)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_BALANCE)));
            accountList.add(account);
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DbHelper.AccountTBL.COLUMN_NAME_ACC_NO,
                DbHelper.AccountTBL.COLUMN_NAME_BALANCE,
                DbHelper.AccountTBL.COLUMN_NAME_ACC_HOLDER,
                DbHelper.AccountTBL.COLUMN_NAME_BANK
        };

        String selection = DbHelper.AccountTBL.COLUMN_NAME_ACC_NO + " = ?";
        String[] selectionArgs = { accountNo };

        Cursor cursor = db.query(
                DbHelper.AccountTBL.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        Account account =null;

        while(cursor.moveToNext()) {
            account = new Account(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_ACC_NO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_BANK)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_ACC_HOLDER)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_BALANCE)));
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbHelper.AccountTBL.COLUMN_NAME_ACC_NO, account.getAccountNo());
        values.put(DbHelper.AccountTBL.COLUMN_NAME_ACC_HOLDER, account.getAccountHolderName());
        values.put(DbHelper.AccountTBL.COLUMN_NAME_BALANCE, account.getBalance());
        values.put(DbHelper.AccountTBL.COLUMN_NAME_BANK, account.getBankName());

        // Insert the new row, returning the primary key value of the new row
        try{
            long newRowId = db.insert(DbHelper.AccountTBL.TABLE_NAME, null, values);
        }
        catch (Exception e){

        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = DbHelper.AccountTBL.COLUMN_NAME_ACC_NO + " LIKE ?";

        String[] selectionArgs = { accountNo };

        int deletedRows = db.delete(DbHelper.AccountTBL.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        // Read the current balance;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DbHelper.AccountTBL.COLUMN_NAME_ACC_NO + " = ?";
        String[] selectionArgs = { accountNo };

        Cursor cursor = db.query(
                DbHelper.AccountTBL.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        double balance=0;

        while(cursor.moveToNext()) {
            balance = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DbHelper.AccountTBL.COLUMN_NAME_BALANCE));
        }
        cursor.close();

        // Update

        if( expenseType==ExpenseType.EXPENSE) balance-=amount;
        else balance+=amount;

        db = dbHelper.getWritableDatabase();

        // New value for one column

        ContentValues values = new ContentValues();
        values.put(DbHelper.AccountTBL.COLUMN_NAME_BALANCE, balance);

        // Which row to update, based on the title
        String selectionUpdate = DbHelper.AccountTBL.COLUMN_NAME_ACC_NO + " LIKE ?";
        String[] selectionArgsUpdate = { accountNo };

        int count = db.update(
                DbHelper.AccountTBL.TABLE_NAME,
                values,
                selectionUpdate,
                selectionArgsUpdate);
    }
}
