//package com.donate.charity.donate;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.google.common.base.Optional;
//
//
///**
// * Created by Jack on 20/12/2017.
// */
//
//public class CharityDetailDatabaseHelper extends SQLiteOpenHelper {
//
//    private static final String CHARITY_DETAIL = "CHARITY_DETAIL";
//    private static final String CHARITY_ID = "id";
//    private static final String CHARITY_NAME = "charityName";
//    private static final String CHARITY_ACCOUNT_NUMBER = "accountNumber";
//    private static final String CHARITY_NUMBER = "charityNumber";
//
//    CharityDetailDatabaseHelper(Context context) {
//        super(context, context.getResources().getString(R.string.DB_NAME), null , context.getResources().getInteger(R.integer.DB_VERSION));
//
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE " + CHARITY_DETAIL + " ("
//                + CHARITY_ID + " INTEGER PRIMARY KEY, "
//                + CHARITY_NAME + " TEXT, "
//                + CHARITY_NUMBER + " TEXT, "
//                + CHARITY_ACCOUNT_NUMBER + " INTEGER);"
//        );
//        insertCharityDetail(db, new CharityDetail(R.string.charity_cancer_research, "Cancer Research", "CharityNum1", 123456789));
//        insertCharityDetail(db, new CharityDetail(R.string.charity_homeless_charity, "Homeless Charity", "CharityNum2", 987654321));
//    }
//
//    /**
//     * Used to insert new charity detail
//     * @param db
//     * @param charityDetail
//     */
//    private static void insertCharityDetail(SQLiteDatabase db, CharityDetail charityDetail) {
//        ContentValues charityDetailValues = new ContentValues();
//        charityDetailValues.put(CHARITY_ID, charityDetail.getCharityId());
//        charityDetailValues.put(CHARITY_NAME, charityDetail.getCharityName());
//        charityDetailValues.put(CHARITY_NUMBER, charityDetail.getCharityNumber());
//        charityDetailValues.put(CHARITY_ACCOUNT_NUMBER, charityDetail.getAccountNumber());
//
//        try {
//            db.insert(CHARITY_DETAIL, null, charityDetailValues);
//        } catch (Exception e) {
//            Log.e("Insertion error", "insertTransaction: ", e);
//        }
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        if (i<1) {
//            onCreate(sqLiteDatabase);
//        }
//    }
//
//    public Optional<CharityDetail> fetchCharityDetailById(int id) {
//        try {
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery("SELECT * FROM "
//                    + CHARITY_DETAIL + " WHERE "
//                    + CHARITY_ID + "=" + String.valueOf(id), null);
//            if (cursor.moveToFirst()) {
//                CharityDetail detail = new CharityDetail(
//                        cursor.getInt(0),
//                        cursor.getString(1),
//                        cursor.getString(2),
//                        cursor.getInt(3)
//                );
//                db.close();
//                return Optional.of(detail);
//            } else {
//                db.close();
//                return Optional.absent();
//            }
//
//        } catch(SQLiteException e) {
//            Log.e("Err.Fetch.TotalDonate", "fetchTotalDonatedOnApp: ", e);
//            return Optional.absent();
//        }
//    }
//}
