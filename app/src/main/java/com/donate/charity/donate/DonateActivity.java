package com.donate.charity.donate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.joda.time.LocalDateTime;

public class DonateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

    }

    public void onDonate(View view) {
        Button btn = (Button) findViewById(view.getId());

        // Get the amount donated.
        double amountToDonate = getAmountDonated(btn);

        TransactionRecord transactionRecord = new TransactionRecord("Cancer Research", "GBP", amountToDonate, new LocalDateTime());
        TransactionHistoryDatabaseHelper transactionHistoryDatabaseHelper = new TransactionHistoryDatabaseHelper(this);
        transactionHistoryDatabaseHelper.insertTransactionRecord(transactionRecord);

        Intent intent = new Intent(this, CharityActivity.class);
        intent.putExtra(getResources().getString(R.string.donationProcessed), amountToDonate);
        startActivity(intent);
    }

    private double getAmountDonated(Button btn) {
        double amountToDonate;
        switch (btn.getId()) {
            case R.id.donate50 : amountToDonate = 0.50;
                break;
            case R.id.donate100 : amountToDonate = 1.00;
                break;
            case R.id.donate200 : amountToDonate = 2.00;
                break;
            default: amountToDonate = 0.00;
        }
        return amountToDonate;
    }
}
