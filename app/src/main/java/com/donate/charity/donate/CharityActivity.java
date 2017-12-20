package com.donate.charity.donate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CharityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity);

        double donatedTotal= (new TransactionHistoryDatabaseHelper(this)).fetchTotalDonatedOnApp();
        Log.i("Total donated", "onCreate: " + String.valueOf(donatedTotal));
        TextView textView = (TextView) findViewById(R.id.donated_amount);
        textView.setText("Total amount donated so far: £" + String.format("%.2f", donatedTotal));

        if (getIntent().getExtras()!=null) {
            double donationReceived = getIntent().getDoubleExtra(getResources().getString(R.string.donationProcessed), 0d);

            if(donationReceived > 0) {
                Snackbar donationReceivedSnackbar = Snackbar.make(findViewById(R.id.charity_activity_layout), R.string.donationProcessed, Snackbar.LENGTH_LONG);
                donationReceivedSnackbar.setText(getResources().getString(R.string.donationProcessed) +" of £" + String.format("%.2f", donationReceived));
                donationReceivedSnackbar.show();
            }
        }

    }


    public void charitySelected(View view) {
        Intent intent = new Intent(this, DonateActivity.class);
        startActivity(intent);
    }
}
