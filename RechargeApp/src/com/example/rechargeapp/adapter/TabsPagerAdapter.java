package com.example.rechargeapp.adapter;

import com.example.rechargeapp.DTHActivity;
import com.example.rechargeapp.DataCardActivity;
import com.example.rechargeapp.MobileActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new MobileActivity();
		case 1:
			// Games fragment activity
			return new DTHActivity();
		case 2:
			// Movies fragment activity
			return new DataCardActivity();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}

