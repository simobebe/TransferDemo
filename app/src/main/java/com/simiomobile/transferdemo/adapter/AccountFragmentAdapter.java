package com.simiomobile.transferdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simiomobile.transferdemo.fragment.AccountFragment;
import com.simiomobile.transferdemo.model.JsonAccountNumber;

import java.util.List;

/**
 * Created by Aor__Feyverly on 3/7/2559.
 */

public class AccountFragmentAdapter extends FragmentStatePagerAdapter {
    List<JsonAccountNumber> mJsonAccountNumber;
    public AccountFragmentAdapter(FragmentManager fm,List<JsonAccountNumber> _JsonAccountNumber) {
        super(fm);
        this.mJsonAccountNumber = _JsonAccountNumber;
    }

    @Override
    public Fragment getItem(int position) {
        return AccountFragment.newInstance(mJsonAccountNumber.get(position).getAccountNumber());
    }

    @Override
    public int getCount() {
        return mJsonAccountNumber.size();
    }
}
