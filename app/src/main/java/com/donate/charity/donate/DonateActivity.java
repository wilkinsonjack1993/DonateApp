package com.donate.charity.donate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.base.Optional;
import com.squareup.sdk.pos.ChargeRequest;
import com.squareup.sdk.pos.CurrencyCode;
import com.squareup.sdk.pos.PosClient;
import com.squareup.sdk.pos.PosSdk;

import org.joda.time.LocalDateTime;

public class DonateActivity extends AppCompatActivity implements AlertDialog.OnClickListener {

    private String charityName;
    private Optional<CharityDetail> charityDetails = Optional.absent();
    private double amountToDonate = 0d;

    //Square POS Client
    private PosClient posClient;
    private static final String SQUARE_APPLICATION_ID = "sq0idp-LDEhAAjESj44OMZzk_YQDw";
    private static final int CHARGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        // Set Charity Name
        charityName = getIntent().getStringExtra(getResources().getString(R.string.charityName));

        //Instantiate PosClient
        posClient = PosSdk.createClient(this, SQUARE_APPLICATION_ID);
    }

    /**
     * Called when the user clicks an amount to donate.
     * Process:
     *  - Get charity details from database
     *  - Send request to square app for payment
     *  - Enter transaction in DB
     *  - Open CharityActivity
     * @param view
     */
    public void onDonate(View view) {
        // Get the button that was clicked.
        Button btn = (Button) findViewById(view.getId());

        // Get the amount donated.
        amountToDonate = getAmountDonated(btn);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        charityDetails = databaseHelper.fetchCharityDetailByName(charityName);

        // If error fetching charity details return to homescreen.
        if(charityDetails.isPresent()) {
            // Donate through Square POS
            // Convert £'s to p's
            // Set the target charity as the customer - this helps with logging
            // TODO clean up to prevent hardcoding of currency and calculation.
            Double amountToDonateInPence = amountToDonate * 100;
            ChargeRequest request = new ChargeRequest.Builder(amountToDonateInPence.intValue(), CurrencyCode.GBP).customerId(charityDetails.get().getCharityName()).build();

            try {
                Intent intent = posClient.createChargeIntent(request);
                startActivityForResult(intent, CHARGE_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                showDialog("Error", "Square Point of Sale is not installed");
//                posClient.openPointOfSalePlayStoreListing();
            }
            // Note - Here is where we connect to the square app. Square returns by going into the
            // onActivityResult() method.
        } else {
            showDialog("Error: ", "Charity detail were not found");
            Intent intent = new Intent(this, CharityActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Calculates the amount to donate from the ID of the button clicked.
     * @param btn the button clicked indicated the amount to pay
     * @return the amount to be donated as a double
     */
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

    /**
     * Enters transaction into database. Success/Error message is attached to the intent (
     * for CharityActivity). The intent is then returned.
     *
     * Charity details and amount are Global variables (This is due to them not being able to be passed through the square app.
     * @param clientTransactionId - the transaction specific Id returned by the square payment app.
     */
    private void enterTransactionInDatabase(String clientTransactionId) {
        if (charityDetails.isPresent()) {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            TransactionRecord transactionRecord = new  TransactionRecord(charityDetails.get().getCharityName(), getResources().getString(R.string.currency), amountToDonate, new LocalDateTime(), clientTransactionId);
            databaseHelper.insertTransactionRecord(transactionRecord);

        } else {
            Log.e("ERROR", "Transaction paid through square but no charity details on entering transaction in DB", new RuntimeException());
        }
    }

    /**
     * Log message as alert - uses class default onClick listener
     * @param title
     * @param message
     */
    private void showDialog(String title, String message) {
        showDialog(title, message, this);
    }

    /**
     * Log message as alert
     * @param title
     * @param message
     */
    private void showDialog(String title, String message, AlertDialog.OnClickListener listener) {
        Log.d("DonateActivity", title + " " + message);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listener)
                .create()
                .show();
    }

    /**
     * Method called by the square app once transaction has taken places whether successful or not.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHARGE_REQUEST_CODE) {
            if (data == null) {
                showDialog("Error", "Square Point of Sale was uninstalled or crashed");
                return;
            }

            if (resultCode == Activity.RESULT_OK) {
                ChargeRequest.Success success = posClient.parseChargeSuccess(data);

                // Enter the transaction in the DB and return to the home screen
                enterTransactionInDatabase(success.clientTransactionId);

                String message = "Client transaction id: " + success.clientTransactionId;
                showDialog("Success!", message);



            } else {
                ChargeRequest.Error error = posClient.parseChargeError(data);

                if (error.code == ChargeRequest.ErrorCode.TRANSACTION_ALREADY_IN_PROGRESS) {
                    String title = "A transaction is already in progress";
                    String message = "Please complete the current transaction in Point of Sale.";

                    showDialog(title, message, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            // Some errors can only be fixed by launching Point of Sale
                            // from the Home screen.
                            posClient.launchPointOfSale();
                        }
                    });
                } else {
                    showDialog("Error: " + error.code, error.debugDescription);
                }
            }
        }
    }

    /**
     * Transaction has finished (either successfully or unsuccessfully)
     * On clicking ok, the user will return to the original home screen.
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(this, CharityActivity.class);
        startActivity(intent);
    }
}
