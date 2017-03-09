package info.krushik.android.helloworldtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import info.krushik.android.helloworldtest.util.IabHelper;
import info.krushik.android.helloworldtest.util.IabResult;
import info.krushik.android.helloworldtest.util.Inventory;
import info.krushik.android.helloworldtest.util.Purchase;

public class InAppBillingActivity extends AppCompatActivity {

    private static final String TAG = "android.helloworldtest";
    IabHelper mHelper;
    static final String ITEM_SKU = "com.example.buttonclick";

    private Button clickButton;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_billing);

        buyButton = (Button) findViewById(R.id.buyButton);
        clickButton = (Button) findViewById(R.id.clickButton);
        clickButton.setEnabled(false);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwv+pwHLoiQShRmbEEQP2ABHiYGmAkGpUapUIr01nQtqfWUko3RbNF1F1IPdsuD9Tw4CjuUU7EhNPoxE0OIRqLjs8apou1m3S1Tf99vGShFib+rzWBW228jtNdOHSVxEUGPjBtTf5i+B5OOW1Dxi5jl2EZXNOTgPZshiX7AdtD4Mcwu/D3WXgUnC/aIOLQ49pKB1jPe4Xi5KpredLRDa5P4a1xrXPtXHCA1Vj1OKl/7o7PRv4gc7/CxJGQrq90PH7+j0SN7LCv0vcnpPZ6B78LAsDRmMFNmsJowIvmJ491T9wbVZ/BJ9uYJZNC1PLYb4mWVEyYDkz0UlI2UI4h+U/WQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " + result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });
    }


    public void buttonClicked(View view) {
        clickButton.setEnabled(false);
        buyButton.setEnabled(true);
    }

    public void buyClick(View view) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001, mPurchaseFinishedListener, "mypurchasetoken");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
                buyButton.setEnabled(false);
            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener
            = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {

            if (result.isSuccess()) {
                clickButton.setEnabled(true);
            } else {
                // handle error
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
}
