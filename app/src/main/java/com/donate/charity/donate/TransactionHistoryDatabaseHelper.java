package com.donate.charity.donate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.format.DateTimeFormat;


/**
 * Created by Jack on 17/12/2017.
 */

class TransactionHistoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DonateApp.db"; //DB name
    private static final int DB_VERSION = 1; //DB version
    private static final String TRANSACTIONHISTORY = "TRANSACTIONHISTORY";
    private static final String CHARITY = "CHARITY";
    private static final String CURRENCY = "CURRENCY";
    private static final String AMOUNT = "AMOUNT";
    private static final String DATETIME = "DATETIME";

    // Constructor specifies details of db but db is only created
    // when app needs to access the db.
    TransactionHistoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION); // null is feature relating to cursors

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TRANSACTIONHISTORY + " ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CHARITY +" TEXT, "
            + CURRENCY+ " TEXT, "
            + AMOUNT + " REAL, "
            + DATETIME + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i<1) {
            onCreate(sqLiteDatabase);
        }
    }

//    /**
//     * Insert method for Transaction
//     * @param charity
//     * @param currency
//     * @param amount
//     * @param dateTime
//     */
//    private static void insertTransaction(SQLiteDatabase db, String charity, String currency, double amount, LocalDateTime dateTime) {
//        ContentValues transactionValues = new ContentValues();
//        transactionValues.put(CHARITY, charity);
//        transactionValues.put(CURRENCY, currency);
//        transactionValues.put(AMOUNT, amount);
//        transactionValues.put(DATETIME, dateTime.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:SS")));
//
//        try {
//            db.insert(TRANSACTIONHISTORY, null, transactionValues);
//        } catch (Exception e) {
//            Log.e("Insertion error", "insertTransaction: ", e);
//        }
//    }

    /**
     * Inserts the record of the transaction
     * @param transactionRecord
     */
    public void insertTransactionRecord(TransactionRecord transactionRecord) {

        //Insert transaction into DB
        ContentValues transactionValues = new ContentValues();
        transactionValues.put(CHARITY, transactionRecord.getCharityName());
        transactionValues.put(CURRENCY, transactionRecord.getCurrency());
        transactionValues.put(AMOUNT, transactionRecord.getAmount());
        transactionValues.put(DATETIME, (transactionRecord.getDateTime()).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:SS")));

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.insert(TRANSACTIONHISTORY, null, transactionValues);
            db.close();
        } catch (SQLException e) {
            Log.e("Insertion error", "insertTransaction: ", e);
        }
    }

    /**
     * @return the total amount donated through this app
     */
    public double fetchTotalDonatedOnApp() {
        double donatedTotal= 0.0;

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("Select SUM(AMOUNT) FROM TRANSACTIONHISTORY", null);

            double totalDonated = 0d;
            if (cursor.moveToFirst()) {
                totalDonated = cursor.getDouble(0);
            }
            db.close();
            return totalDonated;

        } catch(SQLiteException e) {
            Log.e("Err.Fetch.TotalDonate", "fetchTotalDonatedOnApp: ", e);
            return 0.0;
        }
    }
}

