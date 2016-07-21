package com.simiomobile.transferdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simiomobile.transferdemo.model.JsonTransfer;
import com.simiomobile.transferdemo.utils.Constanc;

public class ConfirmActivity extends AppCompatActivity {
    Gson gson;
    JsonTransfer mJsontranfer;

    TextView tvFromAC, tvToAC, tvAmount, tvFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        gson = new Gson();
        Intent intent = getIntent();
        String sJsonTranfer = intent.getStringExtra(Constanc.KEY_JSONTRANFER);
        mJsontranfer = gson.fromJson(sJsonTranfer, JsonTransfer.class);

        initail();
        setup();
    }

    private void initail() {
        tvFromAC = (TextView) findViewById(R.id.tv_from_ac);
        tvToAC = (TextView) findViewById(R.id.tv_to_ac);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvFee = (TextView) findViewById(R.id.tv_fee);
    }

    private void setup() {
        if (mJsontranfer != null) {
            tvFromAC.setText(mJsontranfer.getFromAcountId());
            tvToAC.setText(mJsontranfer.getToAccountId());
            tvAmount.setText(mJsontranfer.getAmount() + " บาท");
            tvFee.setText(mJsontranfer.getFee() + " บาท");
        }
    }

    public void onClickCancel(View view) {
        onBackPressed();
    }

    public void onConfirm(View view) {
        Gson gson = new Gson();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(Constanc.KEY_JSONTRANFER, gson.toJson(callSeviceConfirmTranfer()));
        startActivity(intent);
        finish();
    }

    private JsonTransfer callSeviceConfirmTranfer() {
        return mJsontranfer;
    }
}
