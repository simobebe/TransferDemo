package com.simiomobile.transferdemo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.simiomobile.transferdemo.R;
import com.simiomobile.transferdemo.model.JsonAccount;
import com.simiomobile.transferdemo.utils.Constanc;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Aor__Feyverly on 3/7/2559.
 */

public class AccountFragment extends Fragment {
    private static final String KEY_ACCOUNT_NUMBER = "key_account_number";
    private static final String KEY_ACCOUNT_JSON = "key_account_json";
    private ProgressBar progressLoading;
    private ImageView imgUser;
    private TextView tvBalance;
    private String mAccountNumber;
    private JsonAccount mJsonAccount;
    private Handler handler;
    private Runnable runnable;
    private Gson gson;

    public static AccountFragment newInstance(String _AccountNumber) {

        AccountFragment fragment = new AccountFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ACCOUNT_NUMBER, _AccountNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public AccountFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        progressLoading = (ProgressBar) rootView.findViewById(R.id.f_progressBar);
        imgUser = (ImageView) rootView.findViewById(R.id.f_img_user);
        tvBalance = (TextView) rootView.findViewById(R.id.f_tv_balance);
        mAccountNumber = this.getArguments().getString(KEY_ACCOUNT_NUMBER);
        createHedler(mAccountNumber);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gson = new Gson();
        if (savedInstanceState != null) {
            mAccountNumber = savedInstanceState.getString(KEY_ACCOUNT_NUMBER);
            mJsonAccount = gson.fromJson(mAccountNumber = savedInstanceState.getString(KEY_ACCOUNT_JSON), JsonAccount.class);
            if (mJsonAccount != null) {
                setupDisplay();
            } else {
                callSeviceGetAccount();
            }
        } else {
            callSeviceGetAccount();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_ACCOUNT_NUMBER, mAccountNumber);
        if (tvBalance.getText().length() > 0) {
            outState.putString(KEY_ACCOUNT_JSON, gson.toJson(mJsonAccount));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void callSeviceGetAccount() {
        handler.postDelayed(runnable, 500);
        Log.d("AccountFragment", "callSeviceGetAccount: " + mAccountNumber);
    }

    private void setupDisplay() {
        progressLoading.setVisibility(View.GONE);
        NumberFormat formatter = new DecimalFormat("#,###,###.00");
        String balance = formatter.format(mJsonAccount.getBalanceAccount()) + " บาท";
        tvBalance.setText(balance);
        if (imgUser != null) {
//            imgUser.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
            Glide.with(getContext())
                    .load(mJsonAccount.getImageAccountURL())
                    .placeholder(R.drawable.holder)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(imgUser);
        }

    }

    private JsonAccount getAccountDetail(String id) {
        JsonAccount jsonAccount = null;
        if (id.equals("0123456789")) {
            jsonAccount = gson.fromJson(Constanc.jsonAccouunt0, JsonAccount.class);
        } else if (id.equals("1111111111")) {
            jsonAccount = gson.fromJson(Constanc.jsonAccouunt1, JsonAccount.class);
        } else if (id.equals("2222222222")) {
            jsonAccount = gson.fromJson(Constanc.jsonAccouunt2, JsonAccount.class);
        } else if (id.equals("3333333333")) {
            jsonAccount = gson.fromJson(Constanc.jsonAccouunt3, JsonAccount.class);
        } else if (id.equals("4444444444")) {
            jsonAccount = gson.fromJson(Constanc.jsonAccouunt4, JsonAccount.class);
        } else if (id.equals("5555555555")) {
            jsonAccount = gson.fromJson(Constanc.jsonAccouunt5, JsonAccount.class);
        } else if (id.equals("6666666666")) {
            jsonAccount = gson.fromJson(Constanc.jsonAccouunt6, JsonAccount.class);
        } else {

        }
        return jsonAccount;
    }


    private void createHedler(final String _AccountNumber) {
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                mJsonAccount = getAccountDetail(_AccountNumber);
                setupDisplay();
            }
        };
    }
}
