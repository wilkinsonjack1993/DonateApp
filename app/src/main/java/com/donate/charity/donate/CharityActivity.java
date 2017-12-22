package com.donate.charity.donate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CharityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity);

        double donatedTotal= (new DatabaseHelper(this)).fetchTotalDonatedOnApp();
        Log.i("Total donated", "onCreate: " + String.valueOf(donatedTotal));
        TextView textView = (TextView) findViewById(R.id.donated_amount);
        textView.setText("Total amount donated so far: " + getResources().getString(R.string.currencySymbol) + String.format("%.2f", donatedTotal));

        if (getIntent().getExtras()!=null) {
            double donationReceived = getIntent().getDoubleExtra(getResources().getString(R.string.donationProcessed), 0d);

            if(donationReceived > 0) {
                Snackbar donationReceivedSnackbar = Snackbar.make(findViewById(R.id.charity_activity_layout), R.string.donationProcessed, Snackbar.LENGTH_LONG);
                donationReceivedSnackbar.setText(getResources().getString(R.string.donationProcessed) +" of " + getResources().getString(R.string.currencySymbol) + String.format("%.2f", donationReceived));
                donationReceivedSnackbar.show();
            } else if (getIntent().getStringExtra(getResources().getString(R.string.ERROR)) != null) {
                Snackbar errorOccuredSnackbar = Snackbar.make(findViewById(R.id.charity_activity_layout),R.string.ERROR, Snackbar.LENGTH_LONG);
                errorOccuredSnackbar.setText(getResources().getString(R.string.ERROR)+ ": " + getIntent().getStringExtra(getResources().getString(R.string.ERROR)));
                errorOccuredSnackbar.show();
            }
        }

    }


    public void charitySelected(View view) {
        Button btn = (Button) findViewById(view.getId());

        Intent intent = new Intent(this, DonateActivity.class);
        intent.putExtra(getResources().getString(R.string.charityName), String.valueOf(btn.getText()));
        startActivity(intent);
    }

}
