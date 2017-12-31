package com.donate.charity.donate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.common.base.Optional;

import org.joda.time.format.DateTimeFormat;


/**
 * Created by Jack on 17/12/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {

// ****************************************************************************************************************************
//  TransactionHistory Variables
    private static final String TRANSACTIONHISTORY = "TRANSACTIONHISTORY";
    private static final String CHARITY = "charity";
    private static final String CURRENCY = "currency";
    private static final String AMOUNT = "amount";
    private static final String DATETIME = "dateTime";
    private static final String CLIENT_TRANSACTION_ID = "clientTransactionId";

// ****************************************************************************************************************************
//  Charity Detail Variables
    private static final String CHARITY_DETAIL = "CHARITY_DETAIL";
    private static final String CHARITY_NAME = "charityName";
    private static final String CHARITY_ACCOUNT_NUMBER = "accountNumber";
    private static final String CHARITY_NUMBER = "charityNumber";

    private Context context;

    // Constructor specifies details of db but db is only created
    // when app needs to access the db.
    DatabaseHelper(Context context) {
        super(context, context.getResources().getString(R.string.DB_NAME) ,null , context.getResources().getInteger(R.integer.DB_VERSION));
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TRANSACTIONHISTORY + " ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CHARITY +" TEXT, "
            + CURRENCY + " TEXT, "
            + AMOUNT + " REAL, "
            + DATETIME + " TEXT, "
            + CLIENT_TRANSACTION_ID + " TEXT);"
        );

        db.execSQL("CREATE TABLE " + CHARITY_DETAIL + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CHARITY_NAME + " TEXT, "
                + CHARITY_NUMBER + " TEXT, "
                + CHARITY_ACCOUNT_NUMBER + " INTEGER);"
        );
        insertCharityDetail(db, new CharityDetail(context.getResources().getString(R.string.charity_cancer_research), "CharityNum1", 123456789));
        insertCharityDetail(db, new CharityDetail(context.getResources().getString(R.string.charity_homeless_charity), "CharityNum2", 987654321));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i<1) {
            onCreate(sqLiteDatabase);

        }
    }

//  *******************************************************************************************
//  Transaction history Methods
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
        transactionValues.put(CLIENT_TRANSACTION_ID, (transactionRecord.getClientTransactionId()));

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.insert(TRANSACTIONHISTORY, null, transactionValues);
            db.close();
        } catch (SQLException e) {
            Log.e("Insertion error", "insertTransaction: " + transactionRecord.getClientTransactionId(), e);
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


//  *******************************************************************************************
//  Charity Detail Methods
    /**
     * Used to insert new charity detail
     * @param db
     * @param charityDetail
     */
    private static void insertCharityDetail(SQLiteDatabase db, CharityDetail charityDetail) {
        ContentValues charityDetailValues = new ContentValues();
        charityDetailValues.put(CHARITY_NAME, charityDetail.getCharityName());
        charityDetailValues.put(CHARITY_NUMBER, charityDetail.getCharityNumber());
        charityDetailValues.put(CHARITY_ACCOUNT_NUMBER, charityDetail.getAccountNumber());

        try {
            db.insert(CHARITY_DETAIL, null, charityDetailValues);
        } catch (Exception e) {
            Log.e("Insertion error", "insertTransaction: ", e);
        }
    }


    public Optional<CharityDetail> fetchCharityDetailByName(String name) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "
                    + CHARITY_DETAIL + " WHERE "
                    + CHARITY_NAME + "= '" + name + "'", null);
            if (cursor.moveToFirst()) {
                CharityDetail detail = new CharityDetail(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)
                );
                db.close();
                return Optional.of(detail);
            } else {
                db.close();
                return Optional.absent();
            }

        } catch(SQLiteException e) {
            Log.e("Err.Fetch.TotalDonate", "fetchTotalDonatedOnApp: ", e);
            return Optional.absent();
        }
    }
}

