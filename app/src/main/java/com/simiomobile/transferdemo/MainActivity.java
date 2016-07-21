package com.simiomobile.transferdemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simiomobile.transferdemo.adapter.AccountFragmentAdapter;
import com.simiomobile.transferdemo.model.JsonAccountNumber;
import com.simiomobile.transferdemo.model.JsonAllAccount;
import com.simiomobile.transferdemo.model.JsonTransfer;
import com.simiomobile.transferdemo.utils.Constanc;
import com.simiomobile.transferdemo.widget.CirclePageIndicator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private EditText edtAmount;
    private TextView tvLoadingTranfer, tvLoadingRecive;
    private ViewPager vpAcountTransfer, vpAcountRecive;
    private CirclePageIndicator indicatorTranfer, indicatorRecive;
    private AccountFragmentAdapter adapterTranfer, adapterRecive;
    private List<JsonAccountNumber> mListJsonAccountNumberTranfer = new ArrayList<>();
    private List<JsonAccountNumber> mListJsonAccountNumberRecive = new ArrayList<>();
    private JsonAllAccount mAllAccount = null;
    private String mAccountTranferNumber, mAccountReciveNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initail();
        initailListerner();
        callSeviceGetAccount();
    }

    private void initail() {
        tvLoadingTranfer = (TextView) findViewById(R.id.tv_tranfer_loading);
        tvLoadingRecive = (TextView) findViewById(R.id.tv_recive_loading);
        edtAmount = (EditText) findViewById(R.id.editext_amount);
        vpAcountTransfer = (ViewPager) findViewById(R.id.vpTransfer);
        vpAcountRecive = (ViewPager) findViewById(R.id.vpRecive);
        indicatorTranfer = (CirclePageIndicator) findViewById(R.id.indicatorTransfer);
        indicatorRecive = (CirclePageIndicator) findViewById(R.id.indicatorRecive);
    }

    private void initailListerner() {
        vpAcountTransfer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(final int position) {
                mAccountTranferNumber = mListJsonAccountNumberTranfer.get(position).getAccountNumber();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListJsonAccountNumberRecive = setListJsonAccountNumber(mAllAccount, mAllAccount.getJsonAccount().get(position).getAccountNumber());
                        adapterRecive = new AccountFragmentAdapter(getSupportFragmentManager(), mListJsonAccountNumberRecive);
                        vpAcountRecive.setAdapter(adapterRecive);
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vpAcountRecive.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(final int position) {

                mAccountReciveNumber = mListJsonAccountNumberRecive.get(position).getAccountNumber();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupDisplay() {
        tvLoadingTranfer.setVisibility(View.GONE);
        tvLoadingRecive.setVisibility(View.GONE);
        Gson gson = new Gson();
        mAllAccount = gson.fromJson(Constanc.jsonListAccount, JsonAllAccount.class);
        mListJsonAccountNumberTranfer = setListJsonAccountNumber(mAllAccount, "");
        mListJsonAccountNumberRecive = setListJsonAccountNumber(mAllAccount, mAllAccount.getJsonAccount().get(0).getAccountNumber());
        adapterTranfer = new AccountFragmentAdapter(getSupportFragmentManager(), mListJsonAccountNumberTranfer);
        mAccountTranferNumber = mListJsonAccountNumberTranfer.get(0).getAccountNumber();
        vpAcountTransfer.setAdapter(adapterTranfer);
        indicatorTranfer.setViewPager(vpAcountTransfer);
        adapterRecive = new AccountFragmentAdapter(getSupportFragmentManager(), mListJsonAccountNumberRecive);
        mAccountReciveNumber = mListJsonAccountNumberRecive.get(0).getAccountNumber();
        vpAcountRecive.setAdapter(adapterRecive);
        indicatorRecive.setViewPager(vpAcountRecive);
    }

    private void callSeviceGetAccount() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setupDisplay();
            }
        }, 1500);
    }

    public void onComfirmTransfer(View view) {

        Gson gson = new Gson();
        JsonTransfer jsonTransfer = callSeviceCheckTranfer();
        Intent intent = new Intent(this, ConfirmActivity.class);
        intent.putExtra(Constanc.KEY_JSONTRANFER, gson.toJson(jsonTransfer));
        startActivity(intent);

    }

    private JsonTransfer callSeviceCheckTranfer() {

        JsonTransfer jsonTransfer = new JsonTransfer();
        jsonTransfer.setFromAcountId(formatAccountNumber(mAccountTranferNumber));
        jsonTransfer.setToAccountId(formatAccountNumber(mAccountReciveNumber));
        NumberFormat formatter = new DecimalFormat("#,###,###.00");
        String balance = formatter.format(Double.valueOf(edtAmount.getText().toString()));
        jsonTransfer.setAmount(balance);
        jsonTransfer.setFee("25");
        jsonTransfer.setStatus(true);
        return jsonTransfer;
    }

    public List<JsonAccountNumber> setListJsonAccountNumber(JsonAllAccount _jsonAccount, String _accountNumber) {
        List<JsonAccountNumber> mJsonAccountNumber = new ArrayList<>();
        Log.d("JsonAccountNumber", "" + _accountNumber);
        for (JsonAccountNumber jsonacountnumber : _jsonAccount.getJsonAccount()) {
            if (!_accountNumber.equals(jsonacountnumber.getAccountNumber())) {
                mJsonAccountNumber.add(jsonacountnumber);
            }
        }
        return mJsonAccountNumber;
    }

    private String formatAccountNumber(String accnum) {
        return String.format("xxx-%s-%s-x", accnum.substring(3, 4), accnum.substring(4, 9));
    }
}
