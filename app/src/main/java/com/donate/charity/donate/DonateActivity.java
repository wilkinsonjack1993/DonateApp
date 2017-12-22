package com.donate.charity.donate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.common.base.Optional;

import org.joda.time.LocalDateTime;

public class DonateActivity extends AppCompatActivity {

    private String charityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        charityName = getIntent().getStringExtra(getResources().getString(R.string.charityName));
    }

    public void onDonate(View view) {
        Button btn = (Button) findViewById(view.getId());

        // Get the amount donated.
        double amountToDonate = getAmountDonated(btn);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
//        CharityDetail charityDetail = new CharityDetail(charityId, "Name", "CharNum", 123);
        Optional<CharityDetail> charityDetails = databaseHelper.fetchCharityDetailByName(charityName);
//        Optional<CharityDetail> charityDetails = Optional.of(charityDetail);
        Intent intent = new Intent(this, CharityActivity.class);

        if (charityDetails.isPresent()) {
            TransactionRecord transactionRecord = new  TransactionRecord(charityDetails.get().getCharityName(), getResources().getString(R.string.currency), amountToDonate, new LocalDateTime());
            databaseHelper.insertTransactionRecord(transactionRecord);

            intent.putExtra(getResources().getString(R.string.donationProcessed), amountToDonate);
        } else {
            intent.putExtra(getResources().getString(R.string.ERROR), "No Charity Details Found");
        }

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
